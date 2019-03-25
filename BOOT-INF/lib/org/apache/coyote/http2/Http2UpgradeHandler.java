/*      */ package org.apache.coyote.http2;
/*      */ 
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.Collections;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentLinkedQueue;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import java.util.concurrent.Executor;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ import javax.servlet.http.WebConnection;
/*      */ import org.apache.coyote.Adapter;
/*      */ import org.apache.coyote.CloseNowException;
/*      */ import org.apache.coyote.ProtocolException;
/*      */ import org.apache.coyote.Request;
/*      */ import org.apache.coyote.http11.upgrade.InternalHttpUpgradeHandler;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.codec.binary.Base64;
/*      */ import org.apache.tomcat.util.http.MimeHeaders;
/*      */ import org.apache.tomcat.util.net.AbstractEndpoint;
/*      */ import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
/*      */ import org.apache.tomcat.util.net.SSLSupport;
/*      */ import org.apache.tomcat.util.net.SocketEvent;
/*      */ import org.apache.tomcat.util.net.SocketWrapperBase;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Http2UpgradeHandler
/*      */   extends AbstractStream
/*      */   implements InternalHttpUpgradeHandler, Http2Parser.Input, Http2Parser.Output
/*      */ {
/*   77 */   private static final Log log = LogFactory.getLog(Http2UpgradeHandler.class);
/*   78 */   private static final StringManager sm = StringManager.getManager(Http2UpgradeHandler.class);
/*      */   
/*   80 */   private static final AtomicInteger connectionIdGenerator = new AtomicInteger(0);
/*   81 */   private static final Integer STREAM_ID_ZERO = Integer.valueOf(0);
/*      */   
/*      */   private static final int FLAG_END_OF_STREAM = 1;
/*      */   
/*      */   private static final int FLAG_END_OF_HEADERS = 4;
/*   86 */   private static final byte[] PING = { 0, 0, 8, 6, 0, 0, 0, 0, 0 };
/*   87 */   private static final byte[] PING_ACK = { 0, 0, 8, 6, 1, 0, 0, 0, 0 };
/*      */   
/*   89 */   private static final byte[] SETTINGS_ACK = { 0, 0, 0, 4, 1, 0, 0, 0, 0 };
/*      */   
/*   91 */   private static final byte[] GOAWAY = { 7, 0, 0, 0, 0, 0 };
/*      */   
/*      */   private static final String HTTP2_SETTINGS_HEADER = "HTTP2-Settings";
/*      */   
/*   95 */   private static final HeaderSink HEADER_SINK = new HeaderSink();
/*      */   
/*      */   private final String connectionId;
/*      */   
/*      */   private final Http2Protocol protocol;
/*      */   
/*      */   private final Adapter adapter;
/*      */   
/*      */   private volatile SocketWrapperBase<?> socketWrapper;
/*      */   
/*      */   private volatile SSLSupport sslSupport;
/*      */   private volatile Http2Parser parser;
/*  107 */   private AtomicReference<ConnectionState> connectionState = new AtomicReference(ConnectionState.NEW);
/*      */   
/*  109 */   private volatile long pausedNanoTime = Long.MAX_VALUE;
/*      */   
/*      */ 
/*      */ 
/*      */   private final ConnectionSettingsRemote remoteSettings;
/*      */   
/*      */ 
/*      */ 
/*      */   private final ConnectionSettingsLocal localSettings;
/*      */   
/*      */ 
/*      */   private HpackDecoder hpackDecoder;
/*      */   
/*      */ 
/*      */   private HpackEncoder hpackEncoder;
/*      */   
/*      */ 
/*  126 */   private long readTimeout = 10000L;
/*  127 */   private long keepAliveTimeout = -1L;
/*  128 */   private long writeTimeout = 10000L;
/*      */   
/*  130 */   private final Map<Integer, Stream> streams = new ConcurrentHashMap();
/*  131 */   private final AtomicInteger activeRemoteStreamCount = new AtomicInteger(0);
/*      */   
/*  133 */   private volatile int maxActiveRemoteStreamId = -1;
/*      */   private volatile int maxProcessedStreamId;
/*  135 */   private final AtomicInteger nextLocalStreamId = new AtomicInteger(2);
/*  136 */   private final PingManager pingManager = new PingManager(null);
/*  137 */   private volatile int newStreamsSinceLastPrune = 0;
/*      */   
/*  139 */   private final ConcurrentMap<AbstractStream, int[]> backLogStreams = new ConcurrentHashMap();
/*  140 */   private long backLogSize = 0L;
/*      */   
/*      */ 
/*  143 */   private int maxConcurrentStreamExecution = 20;
/*  144 */   private AtomicInteger streamConcurrency = null;
/*  145 */   private Queue<StreamRunnable> queuedRunnable = null;
/*      */   
/*      */ 
/*  148 */   private Set<String> allowedTrailerHeaders = Collections.emptySet();
/*  149 */   private int maxHeaderCount = 100;
/*  150 */   private int maxHeaderSize = 8192;
/*  151 */   private int maxTrailerCount = 100;
/*  152 */   private int maxTrailerSize = 8192;
/*      */   
/*      */   public Http2UpgradeHandler(Adapter adapter, Request coyoteRequest)
/*      */   {
/*  156 */     this(null, adapter, coyoteRequest);
/*      */   }
/*      */   
/*      */   public Http2UpgradeHandler(Http2Protocol protocol, Adapter adapter, Request coyoteRequest) {
/*  160 */     super(STREAM_ID_ZERO);
/*  161 */     this.protocol = protocol;
/*  162 */     this.adapter = adapter;
/*  163 */     this.connectionId = Integer.toString(connectionIdGenerator.getAndIncrement());
/*      */     
/*  165 */     this.remoteSettings = new ConnectionSettingsRemote(this.connectionId);
/*  166 */     this.localSettings = new ConnectionSettingsLocal(this.connectionId);
/*      */     
/*      */ 
/*  169 */     if (coyoteRequest != null) {
/*  170 */       if (log.isDebugEnabled()) {
/*  171 */         log.debug(sm.getString("upgradeHandler.upgrade", new Object[] { this.connectionId }));
/*      */       }
/*  173 */       Integer key = Integer.valueOf(1);
/*  174 */       Stream stream = new Stream(key, this, coyoteRequest);
/*  175 */       this.streams.put(key, stream);
/*  176 */       this.maxActiveRemoteStreamId = 1;
/*  177 */       this.activeRemoteStreamCount.set(1);
/*  178 */       this.maxProcessedStreamId = 1;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void init(WebConnection webConnection)
/*      */   {
/*  185 */     if (log.isDebugEnabled()) {
/*  186 */       log.debug(sm.getString("upgradeHandler.init", new Object[] { this.connectionId, this.connectionState.get() }));
/*      */     }
/*      */     
/*  189 */     if (!this.connectionState.compareAndSet(ConnectionState.NEW, ConnectionState.CONNECTED)) {
/*  190 */       return;
/*      */     }
/*      */     
/*      */ 
/*  194 */     if (this.maxConcurrentStreamExecution < this.localSettings.getMaxConcurrentStreams()) {
/*  195 */       this.streamConcurrency = new AtomicInteger(0);
/*  196 */       this.queuedRunnable = new ConcurrentLinkedQueue();
/*      */     }
/*      */     
/*  199 */     this.parser = new Http2Parser(this.connectionId, this, this);
/*      */     
/*  201 */     Stream stream = null;
/*      */     
/*  203 */     this.socketWrapper.setReadTimeout(getReadTimeout());
/*  204 */     this.socketWrapper.setWriteTimeout(getWriteTimeout());
/*      */     
/*  206 */     if (webConnection != null)
/*      */     {
/*      */ 
/*      */       try
/*      */       {
/*      */ 
/*  212 */         stream = getStream(1, true);
/*  213 */         String base64Settings = stream.getCoyoteRequest().getHeader("HTTP2-Settings");
/*  214 */         byte[] settings = Base64.decodeBase64(base64Settings);
/*      */         
/*      */ 
/*  217 */         FrameType.SETTINGS.check(0, settings.length);
/*      */         
/*  219 */         for (int i = 0; i < settings.length % 6; i++) {
/*  220 */           int id = ByteUtil.getTwoBytes(settings, i * 6);
/*  221 */           long value = ByteUtil.getFourBytes(settings, i * 6 + 2);
/*  222 */           this.remoteSettings.set(Setting.valueOf(id), value);
/*      */         }
/*      */       }
/*      */       catch (Http2Exception e) {
/*  226 */         throw new ProtocolException(sm.getString("upgradeHandler.upgrade.fail", new Object[] { this.connectionId }));
/*      */       }
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  232 */       byte[] settings = this.localSettings.getSettingsFrameForPending();
/*  233 */       this.socketWrapper.write(true, settings, 0, settings.length);
/*  234 */       this.socketWrapper.flush(true);
/*      */     } catch (IOException ioe) {
/*  236 */       String msg = sm.getString("upgradeHandler.sendPrefaceFail", new Object[] { this.connectionId });
/*  237 */       if (log.isDebugEnabled()) {
/*  238 */         log.debug(msg);
/*      */       }
/*  240 */       throw new ProtocolException(msg, ioe);
/*      */     }
/*      */     
/*      */ 
/*      */     try
/*      */     {
/*  246 */       this.parser.readConnectionPreface();
/*      */     } catch (Http2Exception e) {
/*  248 */       String msg = sm.getString("upgradeHandler.invalidPreface", new Object[] { this.connectionId });
/*  249 */       if (log.isDebugEnabled()) {
/*  250 */         log.debug(msg);
/*      */       }
/*  252 */       throw new ProtocolException(msg);
/*      */     }
/*  254 */     if (log.isDebugEnabled()) {
/*  255 */       log.debug(sm.getString("upgradeHandler.prefaceReceived", new Object[] { this.connectionId }));
/*      */     }
/*      */     
/*      */     try
/*      */     {
/*  260 */       this.pingManager.sendPing(true);
/*      */     } catch (IOException ioe) {
/*  262 */       throw new ProtocolException(sm.getString("upgradeHandler.pingFailed"), ioe);
/*      */     }
/*      */     
/*  265 */     if (webConnection != null) {
/*  266 */       processStreamOnContainerThread(stream);
/*      */     }
/*      */   }
/*      */   
/*      */   private void processStreamOnContainerThread(Stream stream)
/*      */   {
/*  272 */     StreamProcessor streamProcessor = new StreamProcessor(this, stream, this.adapter, this.socketWrapper);
/*  273 */     streamProcessor.setSslSupport(this.sslSupport);
/*  274 */     processStreamOnContainerThread(streamProcessor, SocketEvent.OPEN_READ);
/*      */   }
/*      */   
/*      */   void processStreamOnContainerThread(StreamProcessor streamProcessor, SocketEvent event)
/*      */   {
/*  279 */     StreamRunnable streamRunnable = new StreamRunnable(streamProcessor, event);
/*  280 */     if (this.streamConcurrency == null) {
/*  281 */       this.socketWrapper.getEndpoint().getExecutor().execute(streamRunnable);
/*      */     }
/*  283 */     else if (getStreamConcurrency() < this.maxConcurrentStreamExecution) {
/*  284 */       increaseStreamConcurrency();
/*  285 */       this.socketWrapper.getEndpoint().getExecutor().execute(streamRunnable);
/*      */     } else {
/*  287 */       this.queuedRunnable.offer(streamRunnable);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setSocketWrapper(SocketWrapperBase<?> wrapper)
/*      */   {
/*  295 */     this.socketWrapper = wrapper;
/*      */   }
/*      */   
/*      */ 
/*      */   public void setSslSupport(SSLSupport sslSupport)
/*      */   {
/*  301 */     this.sslSupport = sslSupport;
/*      */   }
/*      */   
/*      */ 
/*      */   public AbstractEndpoint.Handler.SocketState upgradeDispatch(SocketEvent status)
/*      */   {
/*  307 */     if (log.isDebugEnabled()) {
/*  308 */       log.debug(sm.getString("upgradeHandler.upgradeDispatch.entry", new Object[] { this.connectionId, status }));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  313 */     init(null);
/*      */     
/*      */ 
/*  316 */     AbstractEndpoint.Handler.SocketState result = AbstractEndpoint.Handler.SocketState.CLOSED;
/*      */     try
/*      */     {
/*  319 */       this.pingManager.sendPing(false);
/*      */       
/*  321 */       checkPauseState();
/*      */       
/*  323 */       switch (status)
/*      */       {
/*      */       case OPEN_READ: 
/*      */         try
/*      */         {
/*  328 */           this.socketWrapper.setReadTimeout(getReadTimeout());
/*      */           try {
/*      */             Stream stream;
/*  331 */             while (this.parser.readFrame(false)) {}
/*      */ 
/*      */           }
/*      */           catch (StreamException se)
/*      */           {
/*      */ 
/*  337 */             stream = getStream(se.getStreamId(), false);
/*  338 */             if (stream == null) {
/*  339 */               sendStreamReset(se);
/*      */             } else {
/*  341 */               stream.close(se);
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  347 */           this.socketWrapper.setReadTimeout(getKeepAliveTimeout());
/*      */         }
/*      */         catch (Http2Exception ce) {
/*  350 */           if (log.isDebugEnabled()) {
/*  351 */             log.debug(sm.getString("upgradeHandler.connectionError"), ce);
/*      */           }
/*  353 */           closeConnection(ce);
/*  354 */           break;
/*      */         }
/*      */         
/*  357 */         if (this.connectionState.get() != ConnectionState.CLOSED) {
/*  358 */           result = AbstractEndpoint.Handler.SocketState.UPGRADED;
/*      */         }
/*      */         
/*      */         break;
/*      */       case OPEN_WRITE: 
/*  363 */         processWrites();
/*      */         
/*  365 */         result = AbstractEndpoint.Handler.SocketState.UPGRADED;
/*  366 */         break;
/*      */       
/*      */       case DISCONNECT: 
/*      */       case ERROR: 
/*      */       case TIMEOUT: 
/*      */       case STOP: 
/*  372 */         close();
/*      */       }
/*      */     }
/*      */     catch (IOException ioe) {
/*  376 */       if (log.isDebugEnabled()) {
/*  377 */         log.debug(sm.getString("upgradeHandler.ioerror", new Object[] { this.connectionId }), ioe);
/*      */       }
/*  379 */       close();
/*      */     }
/*      */     
/*  382 */     if (log.isDebugEnabled()) {
/*  383 */       log.debug(sm.getString("upgradeHandler.upgradeDispatch.exit", new Object[] { this.connectionId, result }));
/*      */     }
/*  385 */     return result;
/*      */   }
/*      */   
/*      */   ConnectionSettingsRemote getRemoteSettings()
/*      */   {
/*  390 */     return this.remoteSettings;
/*      */   }
/*      */   
/*      */   ConnectionSettingsLocal getLocalSettings()
/*      */   {
/*  395 */     return this.localSettings;
/*      */   }
/*      */   
/*      */   Http2Protocol getProtocol()
/*      */   {
/*  400 */     return this.protocol;
/*      */   }
/*      */   
/*      */ 
/*      */   public void pause()
/*      */   {
/*  406 */     if (log.isDebugEnabled()) {
/*  407 */       log.debug(sm.getString("upgradeHandler.pause.entry", new Object[] { this.connectionId }));
/*      */     }
/*      */     
/*  410 */     if (this.connectionState.compareAndSet(ConnectionState.CONNECTED, ConnectionState.PAUSING)) {
/*  411 */       this.pausedNanoTime = System.nanoTime();
/*      */       try
/*      */       {
/*  414 */         writeGoAwayFrame(Integer.MAX_VALUE, Http2Error.NO_ERROR.getCode(), null);
/*      */       }
/*      */       catch (IOException localIOException) {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void destroy() {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void checkPauseState()
/*      */     throws IOException
/*      */   {
/*  431 */     if ((this.connectionState.get() == ConnectionState.PAUSING) && 
/*  432 */       (this.pausedNanoTime + this.pingManager.getRoundTripTimeNano() < System.nanoTime())) {
/*  433 */       this.connectionState.compareAndSet(ConnectionState.PAUSING, ConnectionState.PAUSED);
/*  434 */       writeGoAwayFrame(this.maxProcessedStreamId, Http2Error.NO_ERROR.getCode(), null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private int increaseStreamConcurrency()
/*      */   {
/*  441 */     return this.streamConcurrency.incrementAndGet();
/*      */   }
/*      */   
/*      */   private int decreaseStreamConcurrency() {
/*  445 */     return this.streamConcurrency.decrementAndGet();
/*      */   }
/*      */   
/*      */   private int getStreamConcurrency() {
/*  449 */     return this.streamConcurrency.get();
/*      */   }
/*      */   
/*      */   void executeQueuedStream() {
/*  453 */     if (this.streamConcurrency == null) {
/*  454 */       return;
/*      */     }
/*  456 */     decreaseStreamConcurrency();
/*  457 */     if (getStreamConcurrency() < this.maxConcurrentStreamExecution) {
/*  458 */       StreamRunnable streamRunnable = (StreamRunnable)this.queuedRunnable.poll();
/*  459 */       if (streamRunnable != null) {
/*  460 */         increaseStreamConcurrency();
/*  461 */         this.socketWrapper.getEndpoint().getExecutor().execute(streamRunnable);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   void sendStreamReset(StreamException se)
/*      */     throws IOException
/*      */   {
/*  469 */     if (log.isDebugEnabled()) {
/*  470 */       log.debug(sm.getString("upgradeHandler.rst.debug", new Object[] { this.connectionId, 
/*  471 */         Integer.toString(se.getStreamId()), se.getError(), se.getMessage() }));
/*      */     }
/*      */     
/*      */ 
/*  475 */     byte[] rstFrame = new byte[13];
/*      */     
/*  477 */     ByteUtil.setThreeBytes(rstFrame, 0, 4);
/*      */     
/*  479 */     rstFrame[3] = FrameType.RST.getIdByte();
/*      */     
/*      */ 
/*  482 */     ByteUtil.set31Bits(rstFrame, 5, se.getStreamId());
/*      */     
/*  484 */     ByteUtil.setFourBytes(rstFrame, 9, se.getError().getCode());
/*      */     
/*  486 */     synchronized (this.socketWrapper) {
/*  487 */       this.socketWrapper.write(true, rstFrame, 0, rstFrame.length);
/*  488 */       this.socketWrapper.flush(true);
/*      */     }
/*      */   }
/*      */   
/*      */   void closeConnection(Http2Exception ce)
/*      */   {
/*      */     try {
/*  495 */       writeGoAwayFrame(this.maxProcessedStreamId, ce.getError().getCode(), ce
/*  496 */         .getMessage().getBytes(StandardCharsets.UTF_8));
/*      */     }
/*      */     catch (IOException localIOException) {}
/*      */     
/*      */ 
/*  501 */     close();
/*      */   }
/*      */   
/*      */   private void writeGoAwayFrame(int maxStreamId, long errorCode, byte[] debugMsg)
/*      */     throws IOException
/*      */   {
/*  507 */     byte[] fixedPayload = new byte[8];
/*  508 */     ByteUtil.set31Bits(fixedPayload, 0, maxStreamId);
/*  509 */     ByteUtil.setFourBytes(fixedPayload, 4, errorCode);
/*  510 */     int len = 8;
/*  511 */     if (debugMsg != null) {
/*  512 */       len += debugMsg.length;
/*      */     }
/*  514 */     byte[] payloadLength = new byte[3];
/*  515 */     ByteUtil.setThreeBytes(payloadLength, 0, len);
/*      */     
/*  517 */     synchronized (this.socketWrapper) {
/*  518 */       this.socketWrapper.write(true, payloadLength, 0, payloadLength.length);
/*  519 */       this.socketWrapper.write(true, GOAWAY, 0, GOAWAY.length);
/*  520 */       this.socketWrapper.write(true, fixedPayload, 0, 8);
/*  521 */       if (debugMsg != null) {
/*  522 */         this.socketWrapper.write(true, debugMsg, 0, debugMsg.length);
/*      */       }
/*  524 */       this.socketWrapper.flush(true);
/*      */     }
/*      */   }
/*      */   
/*      */   void writeHeaders(Stream stream, int pushedStreamId, MimeHeaders mimeHeaders, boolean endOfStream, int payloadSize)
/*      */     throws IOException
/*      */   {
/*  531 */     if (log.isDebugEnabled()) {
/*  532 */       log.debug(sm.getString("upgradeHandler.writeHeaders", new Object[] { this.connectionId, stream
/*  533 */         .getIdentifier(), Integer.valueOf(pushedStreamId), 
/*  534 */         Boolean.valueOf(endOfStream) }));
/*      */     }
/*      */     
/*  537 */     if (!stream.canWrite()) {
/*  538 */       return;
/*      */     }
/*      */     
/*  541 */     byte[] header = new byte[9];
/*  542 */     ByteBuffer payload = ByteBuffer.allocate(payloadSize);
/*      */     
/*  544 */     byte[] pushedStreamIdBytes = null;
/*  545 */     if (pushedStreamId > 0) {
/*  546 */       pushedStreamIdBytes = new byte[4];
/*  547 */       ByteUtil.set31Bits(pushedStreamIdBytes, 0, pushedStreamId);
/*      */     }
/*      */     
/*  550 */     boolean first = true;
/*  551 */     HpackEncoder.State state = null;
/*      */     
/*      */ 
/*  554 */     synchronized (this.socketWrapper) {
/*  555 */       while (state != HpackEncoder.State.COMPLETE) {
/*  556 */         if ((first) && (pushedStreamIdBytes != null)) {
/*  557 */           payload.put(pushedStreamIdBytes);
/*      */         }
/*  559 */         state = getHpackEncoder().encode(mimeHeaders, payload);
/*  560 */         payload.flip();
/*  561 */         if ((state == HpackEncoder.State.COMPLETE) || (payload.limit() > 0)) {
/*  562 */           ByteUtil.setThreeBytes(header, 0, payload.limit());
/*  563 */           if (first) {
/*  564 */             first = false;
/*  565 */             if (pushedStreamIdBytes == null) {
/*  566 */               header[3] = FrameType.HEADERS.getIdByte();
/*      */             } else {
/*  568 */               header[3] = FrameType.PUSH_PROMISE.getIdByte();
/*      */             }
/*  570 */             if (endOfStream) {
/*  571 */               header[4] = 1;
/*      */             }
/*      */           } else {
/*  574 */             header[3] = FrameType.CONTINUATION.getIdByte();
/*      */           }
/*  576 */           if (state == HpackEncoder.State.COMPLETE) {
/*  577 */             int tmp255_254 = 4; byte[] tmp255_252 = header;tmp255_252[tmp255_254] = ((byte)(tmp255_252[tmp255_254] + 4));
/*      */           }
/*  579 */           if (log.isDebugEnabled()) {
/*  580 */             log.debug(payload.limit() + " bytes");
/*      */           }
/*  582 */           ByteUtil.set31Bits(header, 5, stream.getIdentifier().intValue());
/*      */           try {
/*  584 */             this.socketWrapper.write(true, header, 0, header.length);
/*  585 */             this.socketWrapper.write(true, payload);
/*  586 */             this.socketWrapper.flush(true);
/*      */           } catch (IOException ioe) {
/*  588 */             handleAppInitiatedIOException(ioe);
/*      */           }
/*  590 */           payload.clear();
/*  591 */         } else if (state == HpackEncoder.State.UNDERFLOW) {
/*  592 */           payload = ByteBuffer.allocate(payload.capacity() * 2);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private HpackEncoder getHpackEncoder()
/*      */   {
/*  600 */     if (this.hpackEncoder == null) {
/*  601 */       this.hpackEncoder = new HpackEncoder();
/*      */     }
/*      */     
/*  604 */     this.hpackEncoder.setMaxTableSize(this.remoteSettings.getHeaderTableSize());
/*  605 */     return this.hpackEncoder;
/*      */   }
/*      */   
/*      */   void writeBody(Stream stream, ByteBuffer data, int len, boolean finished) throws IOException
/*      */   {
/*  610 */     if (log.isDebugEnabled()) {
/*  611 */       log.debug(sm.getString("upgradeHandler.writeBody", new Object[] { this.connectionId, stream.getIdentifier(), 
/*  612 */         Integer.toString(len) }));
/*      */     }
/*      */     
/*  615 */     boolean writeable = stream.canWrite();
/*  616 */     byte[] header = new byte[9];
/*  617 */     ByteUtil.setThreeBytes(header, 0, len);
/*  618 */     header[3] = FrameType.DATA.getIdByte();
/*  619 */     if (finished) {
/*  620 */       header[4] = 1;
/*  621 */       stream.sentEndOfStream();
/*  622 */       if (!stream.isActive()) {
/*  623 */         this.activeRemoteStreamCount.decrementAndGet();
/*      */       }
/*      */     }
/*  626 */     if (writeable) {
/*  627 */       ByteUtil.set31Bits(header, 5, stream.getIdentifier().intValue());
/*  628 */       synchronized (this.socketWrapper) {
/*      */         try {
/*  630 */           this.socketWrapper.write(true, header, 0, header.length);
/*  631 */           int orgLimit = data.limit();
/*  632 */           data.limit(data.position() + len);
/*  633 */           this.socketWrapper.write(true, data);
/*  634 */           data.limit(orgLimit);
/*  635 */           this.socketWrapper.flush(true);
/*      */         } catch (IOException ioe) {
/*  637 */           handleAppInitiatedIOException(ioe);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void handleAppInitiatedIOException(IOException ioe)
/*      */     throws IOException
/*      */   {
/*  655 */     close();
/*  656 */     throw ioe;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   void writeWindowUpdate(Stream stream, int increment, boolean applicationInitiated)
/*      */     throws IOException
/*      */   {
/*  666 */     if (!stream.canWrite()) {
/*  667 */       return;
/*      */     }
/*  669 */     synchronized (this.socketWrapper)
/*      */     {
/*  671 */       byte[] frame = new byte[13];
/*  672 */       ByteUtil.setThreeBytes(frame, 0, 4);
/*  673 */       frame[3] = FrameType.WINDOW_UPDATE.getIdByte();
/*  674 */       ByteUtil.set31Bits(frame, 9, increment);
/*  675 */       this.socketWrapper.write(true, frame, 0, frame.length);
/*      */       
/*  677 */       ByteUtil.set31Bits(frame, 5, stream.getIdentifier().intValue());
/*      */       try {
/*  679 */         this.socketWrapper.write(true, frame, 0, frame.length);
/*  680 */         this.socketWrapper.flush(true);
/*      */       } catch (IOException ioe) {
/*  682 */         if (applicationInitiated) {
/*  683 */           handleAppInitiatedIOException(ioe);
/*      */         } else {
/*  685 */           throw ioe;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void processWrites() throws IOException
/*      */   {
/*  693 */     synchronized (this.socketWrapper) {
/*  694 */       if (this.socketWrapper.flush(false)) {
/*  695 */         this.socketWrapper.registerWriteInterest();
/*  696 */         return;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   int reserveWindowSize(Stream stream, int reservation)
/*      */     throws IOException
/*      */   {
/*  705 */     int allocation = 0;
/*  706 */     synchronized (stream) {
/*      */       do {
/*  708 */         synchronized (this) {
/*  709 */           if (!stream.canWrite())
/*      */           {
/*  711 */             throw new CloseNowException(sm.getString("upgradeHandler.stream.notWritable", new Object[] {stream
/*  712 */               .getConnectionId(), stream.getIdentifier() }));
/*      */           }
/*  714 */           long windowSize = getWindowSize();
/*  715 */           if ((windowSize < 1L) || (this.backLogSize > 0L))
/*      */           {
/*  717 */             int[] value = (int[])this.backLogStreams.get(stream);
/*  718 */             if (value == null) {
/*  719 */               value = new int[] { reservation, 0 };
/*  720 */               this.backLogStreams.put(stream, value);
/*  721 */               this.backLogSize += reservation;
/*      */               
/*  723 */               AbstractStream parent = stream.getParentStream();
/*  724 */               while ((parent != null) && (this.backLogStreams.putIfAbsent(parent, new int[2]) == null)) {
/*  725 */                 parent = parent.getParentStream();
/*      */               }
/*      */             }
/*  728 */             else if (value[1] > 0) {
/*  729 */               allocation = value[1];
/*  730 */               decrementWindowSize(allocation);
/*  731 */               if (value[0] == 0)
/*      */               {
/*      */ 
/*      */ 
/*  735 */                 this.backLogStreams.remove(stream);
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/*      */ 
/*  741 */                 value[1] = 0;
/*      */               }
/*      */             }
/*      */           }
/*  745 */           else if (windowSize < reservation) {
/*  746 */             allocation = (int)windowSize;
/*  747 */             decrementWindowSize(allocation);
/*      */           } else {
/*  749 */             allocation = reservation;
/*  750 */             decrementWindowSize(allocation);
/*      */           }
/*      */         }
/*  753 */         if (allocation == 0) {
/*      */           try {
/*  755 */             stream.wait();
/*      */           } catch (InterruptedException e) {
/*  757 */             throw new IOException(sm.getString("upgradeHandler.windowSizeReservationInterrupted", new Object[] { this.connectionId, stream
/*      */             
/*  759 */               .getIdentifier(), Integer.toString(reservation) }), e);
/*      */           }
/*      */         }
/*  762 */       } while (allocation == 0);
/*      */     }
/*  764 */     return allocation;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void incrementWindowSize(int increment)
/*      */     throws Http2Exception
/*      */   {
/*  773 */     Set<AbstractStream> streamsToNotify = null;
/*      */     
/*  775 */     synchronized (this) {
/*  776 */       long windowSize = getWindowSize();
/*  777 */       if ((windowSize < 1L) && (windowSize + increment > 0L)) {
/*  778 */         streamsToNotify = releaseBackLog((int)(windowSize + increment));
/*      */       }
/*  780 */       super.incrementWindowSize(increment);
/*      */     }
/*      */     
/*  783 */     if (streamsToNotify != null) {
/*  784 */       for (??? = streamsToNotify.iterator(); ((Iterator)???).hasNext();) { AbstractStream stream = (AbstractStream)((Iterator)???).next();
/*  785 */         synchronized (stream) {
/*  786 */           stream.notifyAll();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private synchronized Set<AbstractStream> releaseBackLog(int increment)
/*      */   {
/*  794 */     Set<AbstractStream> result = new HashSet();
/*  795 */     if (this.backLogSize < increment)
/*      */     {
/*  797 */       result.addAll(this.backLogStreams.keySet());
/*  798 */       this.backLogStreams.clear();
/*  799 */       this.backLogSize = 0L;
/*      */     } else {
/*  801 */       int leftToAllocate = increment;
/*  802 */       while (leftToAllocate > 0) {
/*  803 */         leftToAllocate = allocate(this, leftToAllocate);
/*      */       }
/*  805 */       for (Map.Entry<AbstractStream, int[]> entry : this.backLogStreams.entrySet()) {
/*  806 */         int allocation = ((int[])entry.getValue())[1];
/*  807 */         if (allocation > 0) {
/*  808 */           this.backLogSize -= allocation;
/*  809 */           result.add(entry.getKey());
/*      */         }
/*      */       }
/*      */     }
/*  813 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   protected synchronized void doNotifyAll() {}
/*      */   
/*      */ 
/*      */ 
/*      */   private int allocate(AbstractStream stream, int allocation)
/*      */   {
/*  826 */     if (log.isDebugEnabled()) {
/*  827 */       log.debug(sm.getString("upgradeHandler.allocate.debug", new Object[] { getConnectionId(), stream
/*  828 */         .getIdentifier(), Integer.toString(allocation) }));
/*      */     }
/*      */     
/*  831 */     int[] value = (int[])this.backLogStreams.get(stream);
/*  832 */     if (value[0] >= allocation) {
/*  833 */       value[0] -= allocation;
/*  834 */       value[1] += allocation;
/*  835 */       return 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  840 */     int leftToAllocate = allocation;
/*  841 */     value[1] = value[0];
/*  842 */     value[0] = 0;
/*  843 */     leftToAllocate -= value[1];
/*      */     
/*  845 */     if (log.isDebugEnabled()) {
/*  846 */       log.debug(sm.getString("upgradeHandler.allocate.left", new Object[] {
/*  847 */         getConnectionId(), stream.getIdentifier(), Integer.toString(leftToAllocate) }));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  852 */     Set<AbstractStream> recipients = new HashSet();
/*  853 */     recipients.addAll(stream.getChildStreams());
/*  854 */     recipients.retainAll(this.backLogStreams.keySet());
/*      */     
/*      */ 
/*  857 */     while (leftToAllocate > 0) {
/*  858 */       if (recipients.size() == 0) {
/*  859 */         this.backLogStreams.remove(stream);
/*  860 */         return leftToAllocate;
/*      */       }
/*      */       
/*  863 */       int totalWeight = 0;
/*  864 */       for (AbstractStream recipient : recipients) {
/*  865 */         if (log.isDebugEnabled()) {
/*  866 */           log.debug(sm.getString("upgradeHandler.allocate.recipient", new Object[] {
/*  867 */             getConnectionId(), stream.getIdentifier(), recipient.getIdentifier(), 
/*  868 */             Integer.toString(recipient.getWeight()) }));
/*      */         }
/*  870 */         totalWeight += recipient.getWeight();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  875 */       Object iter = recipients.iterator();
/*  876 */       int allocated = 0;
/*  877 */       while (((Iterator)iter).hasNext()) {
/*  878 */         AbstractStream recipient = (AbstractStream)((Iterator)iter).next();
/*  879 */         int share = leftToAllocate * recipient.getWeight() / totalWeight;
/*  880 */         if (share == 0)
/*      */         {
/*      */ 
/*      */ 
/*  884 */           share = 1;
/*      */         }
/*  886 */         int remainder = allocate(recipient, share);
/*      */         
/*      */ 
/*  889 */         if (remainder > 0) {
/*  890 */           ((Iterator)iter).remove();
/*      */         }
/*  892 */         allocated += share - remainder;
/*      */       }
/*  894 */       leftToAllocate -= allocated;
/*      */     }
/*      */     
/*  897 */     return 0;
/*      */   }
/*      */   
/*      */   private Stream getStream(int streamId, boolean unknownIsError) throws ConnectionException
/*      */   {
/*  902 */     Integer key = Integer.valueOf(streamId);
/*  903 */     Stream result = (Stream)this.streams.get(key);
/*  904 */     if ((result == null) && (unknownIsError))
/*      */     {
/*  906 */       throw new ConnectionException(sm.getString("upgradeHandler.stream.closed", new Object[] { key }), Http2Error.PROTOCOL_ERROR);
/*      */     }
/*      */     
/*  909 */     return result;
/*      */   }
/*      */   
/*      */   private Stream createRemoteStream(int streamId) throws ConnectionException
/*      */   {
/*  914 */     Integer key = Integer.valueOf(streamId);
/*      */     
/*  916 */     if (streamId % 2 != 1)
/*      */     {
/*  918 */       throw new ConnectionException(sm.getString("upgradeHandler.stream.even", new Object[] { key }), Http2Error.PROTOCOL_ERROR);
/*      */     }
/*      */     
/*  921 */     pruneClosedStreams();
/*      */     
/*  923 */     Stream result = new Stream(key, this);
/*  924 */     this.streams.put(key, result);
/*  925 */     return result;
/*      */   }
/*      */   
/*      */   private Stream createLocalStream(Request request)
/*      */   {
/*  930 */     int streamId = this.nextLocalStreamId.getAndAdd(2);
/*      */     
/*  932 */     Integer key = Integer.valueOf(streamId);
/*      */     
/*  934 */     Stream result = new Stream(key, this, request);
/*  935 */     this.streams.put(key, result);
/*  936 */     return result;
/*      */   }
/*      */   
/*      */   private void close()
/*      */   {
/*  941 */     this.connectionState.set(ConnectionState.CLOSED);
/*  942 */     for (Stream stream : this.streams.values())
/*      */     {
/*      */ 
/*  945 */       stream.receiveReset(Http2Error.CANCEL.getCode());
/*      */     }
/*      */     try {
/*  948 */       this.socketWrapper.close();
/*      */     } catch (IOException ioe) {
/*  950 */       log.debug(sm.getString("upgradeHandler.socketCloseFailed"), ioe);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void pruneClosedStreams()
/*      */   {
/*  957 */     if (this.newStreamsSinceLastPrune < 9)
/*      */     {
/*  959 */       this.newStreamsSinceLastPrune += 1;
/*  960 */       return;
/*      */     }
/*      */     
/*  963 */     this.newStreamsSinceLastPrune = 0;
/*      */     
/*      */ 
/*      */ 
/*  967 */     long max = this.localSettings.getMaxConcurrentStreams();
/*      */     
/*  969 */     if (log.isDebugEnabled()) {
/*  970 */       log.debug(sm.getString("upgradeHandler.pruneStart", new Object[] { this.connectionId, 
/*  971 */         Long.toString(max), Integer.toString(this.streams.size()) }));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  976 */     max += max / 10L;
/*  977 */     if (max > 2147483647L) {
/*  978 */       max = 2147483647L;
/*      */     }
/*      */     
/*  981 */     int toClose = this.streams.size() - (int)max;
/*  982 */     if (toClose < 1) {
/*  983 */       return;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  997 */     TreeSet<Integer> candidatesStepOne = new TreeSet();
/*  998 */     TreeSet<Integer> candidatesStepTwo = new TreeSet();
/*  999 */     TreeSet<Integer> candidatesStepThree = new TreeSet();
/*      */     
/* 1001 */     Iterator<Map.Entry<Integer, Stream>> entryIter = this.streams.entrySet().iterator();
/* 1002 */     while (entryIter.hasNext()) {
/* 1003 */       Map.Entry<Integer, Stream> entry = (Map.Entry)entryIter.next();
/* 1004 */       Stream stream = (Stream)entry.getValue();
/*      */       
/* 1006 */       if (!stream.isActive())
/*      */       {
/*      */ 
/*      */ 
/* 1010 */         if (stream.isClosedFinal())
/*      */         {
/*      */ 
/* 1013 */           candidatesStepThree.add(entry.getKey());
/* 1014 */         } else if (stream.getChildStreams().size() == 0)
/*      */         {
/* 1016 */           candidatesStepOne.add(entry.getKey());
/*      */         }
/*      */         else {
/* 1019 */           candidatesStepTwo.add(entry.getKey());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1024 */     Iterator<Integer> stepOneIter = candidatesStepOne.iterator();
/* 1025 */     for (; stepOneIter.hasNext(); 
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1047 */         goto 394)
/*      */     {
/* 1026 */       Integer streamIdToRemove = (Integer)stepOneIter.next();
/*      */       
/* 1028 */       Stream removedStream = (Stream)this.streams.remove(streamIdToRemove);
/* 1029 */       removedStream.detachFromParent();
/* 1030 */       toClose--;
/* 1031 */       if (log.isDebugEnabled()) {
/* 1032 */         log.debug(sm.getString("upgradeHandler.pruned", new Object[] { this.connectionId, streamIdToRemove }));
/*      */       }
/*      */       
/*      */ 
/* 1036 */       AbstractStream parent = removedStream.getParentStream();
/* 1037 */       if (((parent instanceof Stream)) && (!((Stream)parent).isActive()) && 
/* 1038 */         (!((Stream)parent).isClosedFinal()) && (parent.getChildStreams().size() == 0)) {
/* 1039 */         this.streams.remove(parent.getIdentifier());
/* 1040 */         parent.detachFromParent();
/* 1041 */         toClose--;
/* 1042 */         if (log.isDebugEnabled()) {
/* 1043 */           log.debug(sm.getString("upgradeHandler.pruned", new Object[] { this.connectionId, streamIdToRemove }));
/*      */         }
/*      */         
/* 1046 */         candidatesStepTwo.remove(parent.getIdentifier());
/* 1047 */         parent = parent.getParentStream();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1052 */     Iterator<Integer> stepTwoIter = candidatesStepTwo.iterator();
/* 1053 */     while (stepTwoIter.hasNext()) {
/* 1054 */       Integer streamIdToRemove = (Integer)stepTwoIter.next();
/* 1055 */       removeStreamFromPriorityTree(streamIdToRemove);
/* 1056 */       toClose--;
/* 1057 */       if (log.isDebugEnabled()) {
/* 1058 */         log.debug(sm.getString("upgradeHandler.pruned", new Object[] { this.connectionId, streamIdToRemove }));
/*      */       }
/*      */     }
/*      */     
/* 1062 */     while ((toClose > 0) && (candidatesStepThree.size() > 0)) {
/* 1063 */       Integer streamIdToRemove = (Integer)candidatesStepThree.pollLast();
/* 1064 */       removeStreamFromPriorityTree(streamIdToRemove);
/* 1065 */       toClose--;
/* 1066 */       if (log.isDebugEnabled()) {
/* 1067 */         log.debug(sm.getString("upgradeHandler.prunedPriority", new Object[] { this.connectionId, streamIdToRemove }));
/*      */       }
/*      */     }
/*      */     
/* 1071 */     if (toClose > 0) {
/* 1072 */       log.warn(sm.getString("upgradeHandler.pruneIncomplete", new Object[] { this.connectionId, 
/* 1073 */         Integer.toString(toClose) }));
/*      */     }
/*      */   }
/*      */   
/*      */   private void removeStreamFromPriorityTree(Integer streamIdToRemove)
/*      */   {
/* 1079 */     Stream streamToRemove = (Stream)this.streams.remove(streamIdToRemove);
/*      */     
/*      */ 
/* 1082 */     Set<Stream> children = streamToRemove.getChildStreams();
/* 1083 */     int totalWeight; if (streamToRemove.getChildStreams().size() == 1)
/*      */     {
/* 1085 */       ((Stream)streamToRemove.getChildStreams().iterator().next()).rePrioritise(streamToRemove
/* 1086 */         .getParentStream(), streamToRemove.getWeight());
/*      */     } else {
/* 1088 */       totalWeight = 0;
/* 1089 */       for (Stream child : children) {
/* 1090 */         totalWeight += child.getWeight();
/*      */       }
/* 1092 */       for (Stream child : children) {
/* 1093 */         ((Stream)streamToRemove.getChildStreams().iterator().next()).rePrioritise(streamToRemove
/* 1094 */           .getParentStream(), streamToRemove
/* 1095 */           .getWeight() * child.getWeight() / totalWeight);
/*      */       }
/*      */     }
/* 1098 */     streamToRemove.detachFromParent();
/* 1099 */     streamToRemove.getChildStreams().clear();
/*      */   }
/*      */   
/*      */   void push(Request request, Stream associatedStream) throws IOException
/*      */   {
/* 1104 */     Stream pushStream = createLocalStream(request);
/*      */     
/*      */ 
/* 1107 */     writeHeaders(associatedStream, pushStream.getIdentifier().intValue(), request
/* 1108 */       .getMimeHeaders(), false, 1024);
/*      */     
/* 1110 */     pushStream.sentPushPromise();
/*      */     
/* 1112 */     processStreamOnContainerThread(pushStream);
/*      */   }
/*      */   
/*      */ 
/*      */   protected final String getConnectionId()
/*      */   {
/* 1118 */     return this.connectionId;
/*      */   }
/*      */   
/*      */ 
/*      */   protected final int getWeight()
/*      */   {
/* 1124 */     return 0;
/*      */   }
/*      */   
/*      */   boolean isTrailerHeaderAllowed(String headerName)
/*      */   {
/* 1129 */     return this.allowedTrailerHeaders.contains(headerName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public long getReadTimeout()
/*      */   {
/* 1136 */     return this.readTimeout;
/*      */   }
/*      */   
/*      */   public void setReadTimeout(long readTimeout)
/*      */   {
/* 1141 */     this.readTimeout = readTimeout;
/*      */   }
/*      */   
/*      */   public long getKeepAliveTimeout()
/*      */   {
/* 1146 */     return this.keepAliveTimeout;
/*      */   }
/*      */   
/*      */   public void setKeepAliveTimeout(long keepAliveTimeout)
/*      */   {
/* 1151 */     this.keepAliveTimeout = keepAliveTimeout;
/*      */   }
/*      */   
/*      */   public long getWriteTimeout()
/*      */   {
/* 1156 */     return this.writeTimeout;
/*      */   }
/*      */   
/*      */   public void setWriteTimeout(long writeTimeout)
/*      */   {
/* 1161 */     this.writeTimeout = writeTimeout;
/*      */   }
/*      */   
/*      */   public void setMaxConcurrentStreams(long maxConcurrentStreams)
/*      */   {
/* 1166 */     this.localSettings.set(Setting.MAX_CONCURRENT_STREAMS, maxConcurrentStreams);
/*      */   }
/*      */   
/*      */   public void setMaxConcurrentStreamExecution(int maxConcurrentStreamExecution)
/*      */   {
/* 1171 */     this.maxConcurrentStreamExecution = maxConcurrentStreamExecution;
/*      */   }
/*      */   
/*      */   public void setInitialWindowSize(int initialWindowSize)
/*      */   {
/* 1176 */     this.localSettings.set(Setting.INITIAL_WINDOW_SIZE, initialWindowSize);
/*      */   }
/*      */   
/*      */   public void setAllowedTrailerHeaders(Set<String> allowedTrailerHeaders)
/*      */   {
/* 1181 */     this.allowedTrailerHeaders = allowedTrailerHeaders;
/*      */   }
/*      */   
/*      */   public void setMaxHeaderCount(int maxHeaderCount)
/*      */   {
/* 1186 */     this.maxHeaderCount = maxHeaderCount;
/*      */   }
/*      */   
/*      */   public int getMaxHeaderCount()
/*      */   {
/* 1191 */     return this.maxHeaderCount;
/*      */   }
/*      */   
/*      */   public void setMaxHeaderSize(int maxHeaderSize)
/*      */   {
/* 1196 */     this.maxHeaderSize = maxHeaderSize;
/*      */   }
/*      */   
/*      */   public int getMaxHeaderSize()
/*      */   {
/* 1201 */     return this.maxHeaderSize;
/*      */   }
/*      */   
/*      */   public void setMaxTrailerCount(int maxTrailerCount)
/*      */   {
/* 1206 */     this.maxTrailerCount = maxTrailerCount;
/*      */   }
/*      */   
/*      */   public int getMaxTrailerCount()
/*      */   {
/* 1211 */     return this.maxTrailerCount;
/*      */   }
/*      */   
/*      */   public void setMaxTrailerSize(int maxTrailerSize)
/*      */   {
/* 1216 */     this.maxTrailerSize = maxTrailerSize;
/*      */   }
/*      */   
/*      */   public int getMaxTrailerSize()
/*      */   {
/* 1221 */     return this.maxTrailerSize;
/*      */   }
/*      */   
/*      */   public void setInitiatePingDisabled(boolean initiatePingDisabled)
/*      */   {
/* 1226 */     this.pingManager.initiateDisabled = initiatePingDisabled;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean fill(boolean block, byte[] data)
/*      */     throws IOException
/*      */   {
/* 1234 */     return fill(block, data, 0, data.length);
/*      */   }
/*      */   
/*      */   public boolean fill(boolean block, ByteBuffer data, int len) throws IOException
/*      */   {
/* 1239 */     boolean result = fill(block, data.array(), data.arrayOffset() + data.position(), len);
/* 1240 */     if (result) {
/* 1241 */       data.position(data.position() + len);
/*      */     }
/* 1243 */     return result;
/*      */   }
/*      */   
/*      */   public boolean fill(boolean block, byte[] data, int offset, int length) throws IOException
/*      */   {
/* 1248 */     int len = length;
/* 1249 */     int pos = offset;
/* 1250 */     boolean nextReadBlock = block;
/* 1251 */     int thisRead = 0;
/*      */     
/* 1253 */     while (len > 0) {
/* 1254 */       thisRead = this.socketWrapper.read(nextReadBlock, data, pos, len);
/* 1255 */       if (thisRead == 0) {
/* 1256 */         if (nextReadBlock)
/*      */         {
/* 1258 */           throw new IllegalStateException();
/*      */         }
/* 1260 */         return false;
/*      */       }
/* 1262 */       if (thisRead == -1) {
/* 1263 */         if (((ConnectionState)this.connectionState.get()).isNewStreamAllowed()) {
/* 1264 */           throw new EOFException();
/*      */         }
/* 1266 */         return false;
/*      */       }
/*      */       
/* 1269 */       pos += thisRead;
/* 1270 */       len -= thisRead;
/* 1271 */       nextReadBlock = true;
/*      */     }
/*      */     
/*      */ 
/* 1275 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   public int getMaxFrameSize()
/*      */   {
/* 1281 */     return this.localSettings.getMaxFrameSize();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public HpackDecoder getHpackDecoder()
/*      */   {
/* 1289 */     if (this.hpackDecoder == null) {
/* 1290 */       this.hpackDecoder = new HpackDecoder(this.localSettings.getHeaderTableSize());
/*      */     }
/* 1292 */     return this.hpackDecoder;
/*      */   }
/*      */   
/*      */   public ByteBuffer startRequestBodyFrame(int streamId, int payloadSize)
/*      */     throws Http2Exception
/*      */   {
/* 1298 */     Stream stream = getStream(streamId, true);
/* 1299 */     stream.checkState(FrameType.DATA);
/* 1300 */     stream.receivedData(payloadSize);
/* 1301 */     return stream.getInputByteBuffer();
/*      */   }
/*      */   
/*      */ 
/*      */   public void endRequestBodyFrame(int streamId)
/*      */     throws Http2Exception
/*      */   {
/* 1308 */     Stream stream = getStream(streamId, true);
/* 1309 */     stream.getInputBuffer().onDataAvailable();
/*      */   }
/*      */   
/*      */   public void receivedEndOfStream(int streamId)
/*      */     throws ConnectionException
/*      */   {
/* 1315 */     Stream stream = getStream(streamId, ((ConnectionState)this.connectionState.get()).isNewStreamAllowed());
/* 1316 */     if (stream != null) {
/* 1317 */       stream.receivedEndOfStream();
/* 1318 */       if (!stream.isActive()) {
/* 1319 */         this.activeRemoteStreamCount.decrementAndGet();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void swallowedPadding(int streamId, int paddingLength)
/*      */     throws ConnectionException, IOException
/*      */   {
/* 1328 */     Stream stream = getStream(streamId, true);
/*      */     
/* 1330 */     writeWindowUpdate(stream, paddingLength + 1, false);
/*      */   }
/*      */   
/*      */   public HpackDecoder.HeaderEmitter headersStart(int streamId, boolean headersEndStream)
/*      */     throws Http2Exception
/*      */   {
/* 1336 */     if (((ConnectionState)this.connectionState.get()).isNewStreamAllowed()) {
/* 1337 */       Stream stream = getStream(streamId, false);
/* 1338 */       if (stream == null) {
/* 1339 */         stream = createRemoteStream(streamId);
/*      */       }
/* 1341 */       if (streamId < this.maxActiveRemoteStreamId) {
/* 1342 */         throw new ConnectionException(sm.getString("upgradeHandler.stream.old", new Object[] {
/* 1343 */           Integer.valueOf(streamId), Integer.valueOf(this.maxActiveRemoteStreamId) }), Http2Error.PROTOCOL_ERROR);
/*      */       }
/*      */       
/* 1346 */       stream.checkState(FrameType.HEADERS);
/* 1347 */       stream.receivedStartOfHeaders(headersEndStream);
/* 1348 */       closeIdleStreams(streamId);
/* 1349 */       if (this.localSettings.getMaxConcurrentStreams() < this.activeRemoteStreamCount.incrementAndGet()) {
/* 1350 */         this.activeRemoteStreamCount.decrementAndGet();
/* 1351 */         throw new StreamException(sm.getString("upgradeHandler.tooManyRemoteStreams", new Object[] {
/* 1352 */           Long.toString(this.localSettings.getMaxConcurrentStreams()) }), Http2Error.REFUSED_STREAM, streamId);
/*      */       }
/*      */       
/* 1355 */       return stream;
/*      */     }
/* 1357 */     if (log.isDebugEnabled()) {
/* 1358 */       log.debug(sm.getString("upgradeHandler.noNewStreams", new Object[] { this.connectionId, 
/* 1359 */         Integer.toString(streamId) }));
/*      */     }
/*      */     
/* 1362 */     return HEADER_SINK;
/*      */   }
/*      */   
/*      */   private void closeIdleStreams(int newMaxActiveRemoteStreamId)
/*      */     throws Http2Exception
/*      */   {
/* 1368 */     for (int i = this.maxActiveRemoteStreamId + 2; i < newMaxActiveRemoteStreamId; i += 2) {
/* 1369 */       Stream stream = getStream(i, false);
/* 1370 */       if (stream != null) {
/* 1371 */         stream.closeIfIdle();
/*      */       }
/*      */     }
/* 1374 */     this.maxActiveRemoteStreamId = newMaxActiveRemoteStreamId;
/*      */   }
/*      */   
/*      */ 
/*      */   public void reprioritise(int streamId, int parentStreamId, boolean exclusive, int weight)
/*      */     throws Http2Exception
/*      */   {
/* 1381 */     if (streamId == parentStreamId) {
/* 1382 */       throw new ConnectionException(sm.getString("upgradeHandler.dependency.invalid", new Object[] {
/* 1383 */         getConnectionId(), Integer.valueOf(streamId) }), Http2Error.PROTOCOL_ERROR);
/*      */     }
/* 1385 */     Stream stream = getStream(streamId, false);
/* 1386 */     if (stream == null) {
/* 1387 */       stream = createRemoteStream(streamId);
/*      */     }
/* 1389 */     stream.checkState(FrameType.PRIORITY);
/* 1390 */     AbstractStream parentStream = getStream(parentStreamId, false);
/* 1391 */     if (parentStream == null) {
/* 1392 */       parentStream = this;
/*      */     }
/* 1394 */     stream.rePrioritise(parentStream, exclusive, weight);
/*      */   }
/*      */   
/*      */   public void headersEnd(int streamId)
/*      */     throws ConnectionException
/*      */   {
/* 1400 */     setMaxProcessedStream(streamId);
/* 1401 */     Stream stream = getStream(streamId, ((ConnectionState)this.connectionState.get()).isNewStreamAllowed());
/* 1402 */     if ((stream != null) && (stream.isActive()) && 
/* 1403 */       (stream.receivedEndOfHeaders())) {
/* 1404 */       processStreamOnContainerThread(stream);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private void setMaxProcessedStream(int streamId)
/*      */   {
/* 1411 */     if (this.maxProcessedStreamId < streamId) {
/* 1412 */       this.maxProcessedStreamId = streamId;
/*      */     }
/*      */   }
/*      */   
/*      */   public void reset(int streamId, long errorCode)
/*      */     throws Http2Exception
/*      */   {
/* 1419 */     Stream stream = getStream(streamId, true);
/* 1420 */     stream.checkState(FrameType.RST);
/* 1421 */     stream.receiveReset(errorCode);
/*      */   }
/*      */   
/*      */   public void setting(Setting setting, long value)
/*      */     throws ConnectionException
/*      */   {
/*      */     int diff;
/* 1428 */     if (setting == Setting.INITIAL_WINDOW_SIZE) {
/* 1429 */       long oldValue = this.remoteSettings.getInitialWindowSize();
/*      */       
/* 1431 */       this.remoteSettings.set(setting, value);
/* 1432 */       diff = (int)(value - oldValue);
/* 1433 */       for (Stream stream : this.streams.values()) {
/*      */         try {
/* 1435 */           stream.incrementWindowSize(diff);
/*      */         } catch (Http2Exception h2e) {
/* 1437 */           stream.close(new StreamException(sm.getString("upgradeHandler.windowSizeTooBig", new Object[] { this.connectionId, stream
/*      */           
/* 1439 */             .getIdentifier() }), h2e
/* 1440 */             .getError(), stream.getIdentifier().intValue()));
/*      */         }
/*      */       }
/*      */     } else {
/* 1444 */       this.remoteSettings.set(setting, value);
/*      */     }
/*      */   }
/*      */   
/*      */   public void settingsEnd(boolean ack)
/*      */     throws IOException
/*      */   {
/* 1451 */     if (ack) {
/* 1452 */       if (!this.localSettings.ack())
/*      */       {
/* 1454 */         log.warn(sm.getString("upgradeHandler.unexpectedAck", new Object[] { this.connectionId, 
/* 1455 */           getIdentifier() }));
/*      */       }
/*      */     } else {
/* 1458 */       synchronized (this.socketWrapper) {
/* 1459 */         this.socketWrapper.write(true, SETTINGS_ACK, 0, SETTINGS_ACK.length);
/* 1460 */         this.socketWrapper.flush(true);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public void pingReceive(byte[] payload, boolean ack)
/*      */     throws IOException
/*      */   {
/* 1468 */     this.pingManager.receivePing(payload, ack);
/*      */   }
/*      */   
/*      */ 
/*      */   public void goaway(int lastStreamId, long errorCode, String debugData)
/*      */   {
/* 1474 */     if (log.isDebugEnabled()) {
/* 1475 */       log.debug(sm.getString("upgradeHandler.goaway.debug", new Object[] { this.connectionId, 
/* 1476 */         Integer.toString(lastStreamId), Long.toHexString(errorCode), debugData }));
/*      */     }
/* 1478 */     close();
/*      */   }
/*      */   
/*      */   public void incrementWindowSize(int streamId, int increment)
/*      */     throws Http2Exception
/*      */   {
/* 1484 */     if (streamId == 0) {
/* 1485 */       incrementWindowSize(increment);
/*      */     } else {
/* 1487 */       Stream stream = getStream(streamId, true);
/* 1488 */       stream.checkState(FrameType.WINDOW_UPDATE);
/* 1489 */       stream.incrementWindowSize(increment);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void swallowed(int streamId, FrameType frameType, int flags, int size)
/*      */     throws IOException
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */   private class PingManager
/*      */   {
/* 1503 */     protected boolean initiateDisabled = false;
/*      */     
/*      */ 
/* 1506 */     private final long pingIntervalNano = 10000000000L;
/*      */     
/* 1508 */     private int sequence = 0;
/* 1509 */     private long lastPingNanoTime = Long.MIN_VALUE;
/*      */     
/* 1511 */     private Queue<Http2UpgradeHandler.PingRecord> inflightPings = new ConcurrentLinkedQueue();
/* 1512 */     private Queue<Long> roundTripTimes = new ConcurrentLinkedQueue();
/*      */     
/*      */ 
/*      */ 
/*      */     private PingManager() {}
/*      */     
/*      */ 
/*      */     public void sendPing(boolean force)
/*      */       throws IOException
/*      */     {
/* 1522 */       if (this.initiateDisabled) {
/* 1523 */         return;
/*      */       }
/* 1525 */       long now = System.nanoTime();
/* 1526 */       if ((force) || (now - this.lastPingNanoTime > 10000000000L)) {
/* 1527 */         this.lastPingNanoTime = now;
/* 1528 */         byte[] payload = new byte[8];
/* 1529 */         synchronized (Http2UpgradeHandler.this.socketWrapper) {
/* 1530 */           int sentSequence = ++this.sequence;
/* 1531 */           Http2UpgradeHandler.PingRecord pingRecord = new Http2UpgradeHandler.PingRecord(sentSequence, now);
/* 1532 */           this.inflightPings.add(pingRecord);
/* 1533 */           ByteUtil.set31Bits(payload, 4, sentSequence);
/* 1534 */           Http2UpgradeHandler.this.socketWrapper.write(true, Http2UpgradeHandler.PING, 0, Http2UpgradeHandler.PING.length);
/* 1535 */           Http2UpgradeHandler.this.socketWrapper.write(true, payload, 0, payload.length);
/* 1536 */           Http2UpgradeHandler.this.socketWrapper.flush(true);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public void receivePing(byte[] payload, boolean ack) throws IOException {
/* 1542 */       if (ack)
/*      */       {
/* 1544 */         int receivedSequence = ByteUtil.get31Bits(payload, 4);
/* 1545 */         Http2UpgradeHandler.PingRecord pingRecord = (Http2UpgradeHandler.PingRecord)this.inflightPings.poll();
/* 1546 */         while ((pingRecord != null) && (pingRecord.getSequence() < receivedSequence)) {
/* 1547 */           pingRecord = (Http2UpgradeHandler.PingRecord)this.inflightPings.poll();
/*      */         }
/* 1549 */         if (pingRecord != null)
/*      */         {
/*      */ 
/* 1552 */           long roundTripTime = System.nanoTime() - pingRecord.getSentNanoTime();
/* 1553 */           this.roundTripTimes.add(Long.valueOf(roundTripTime));
/* 1554 */           while (this.roundTripTimes.size() > 3)
/*      */           {
/*      */ 
/* 1557 */             this.roundTripTimes.poll();
/*      */           }
/* 1559 */           if (Http2UpgradeHandler.log.isDebugEnabled()) {
/* 1560 */             Http2UpgradeHandler.log.debug(Http2UpgradeHandler.sm.getString("pingManager.roundTripTime", new Object[] {
/* 1561 */               Http2UpgradeHandler.this.connectionId, Long.valueOf(roundTripTime) }));
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1567 */         synchronized (Http2UpgradeHandler.this.socketWrapper) {
/* 1568 */           Http2UpgradeHandler.this.socketWrapper.write(true, Http2UpgradeHandler.PING_ACK, 0, Http2UpgradeHandler.PING_ACK.length);
/* 1569 */           Http2UpgradeHandler.this.socketWrapper.write(true, payload, 0, payload.length);
/* 1570 */           Http2UpgradeHandler.this.socketWrapper.flush(true);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     public long getRoundTripTimeNano() {
/* 1576 */       long sum = 0L;
/* 1577 */       long count = 0L;
/* 1578 */       for (Long roundTripTime : this.roundTripTimes) {
/* 1579 */         sum += roundTripTime.longValue();
/* 1580 */         count += 1L;
/*      */       }
/* 1582 */       if (count > 0L) {
/* 1583 */         return sum / count;
/*      */       }
/* 1585 */       return 0L;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class PingRecord
/*      */   {
/*      */     private final int sequence;
/*      */     private final long sentNanoTime;
/*      */     
/*      */     public PingRecord(int sequence, long sentNanoTime)
/*      */     {
/* 1596 */       this.sequence = sequence;
/* 1597 */       this.sentNanoTime = sentNanoTime;
/*      */     }
/*      */     
/*      */     public int getSequence() {
/* 1601 */       return this.sequence;
/*      */     }
/*      */     
/*      */     public long getSentNanoTime() {
/* 1605 */       return this.sentNanoTime;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static enum ConnectionState
/*      */   {
/* 1612 */     NEW(true), 
/* 1613 */     CONNECTED(true), 
/* 1614 */     PAUSING(true), 
/* 1615 */     PAUSED(false), 
/* 1616 */     CLOSED(false);
/*      */     
/*      */     private final boolean newStreamsAllowed;
/*      */     
/*      */     private ConnectionState(boolean newStreamsAllowed) {
/* 1621 */       this.newStreamsAllowed = newStreamsAllowed;
/*      */     }
/*      */     
/*      */     public boolean isNewStreamAllowed() {
/* 1625 */       return this.newStreamsAllowed;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\Http2UpgradeHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */