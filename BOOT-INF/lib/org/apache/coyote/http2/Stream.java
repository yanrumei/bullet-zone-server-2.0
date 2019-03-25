/*      */ package org.apache.coyote.http2;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Set;
/*      */ import org.apache.coyote.ActionCode;
/*      */ import org.apache.coyote.CloseNowException;
/*      */ import org.apache.coyote.Constants;
/*      */ import org.apache.coyote.InputBuffer;
/*      */ import org.apache.coyote.Request;
/*      */ import org.apache.coyote.Response;
/*      */ import org.apache.coyote.http11.HttpOutputBuffer;
/*      */ import org.apache.coyote.http11.OutputFilter;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.buf.ByteChunk;
/*      */ import org.apache.tomcat.util.buf.MessageBytes;
/*      */ import org.apache.tomcat.util.http.MimeHeaders;
/*      */ import org.apache.tomcat.util.net.ApplicationBufferHandler;
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
/*      */ public class Stream
/*      */   extends AbstractStream
/*      */   implements HpackDecoder.HeaderEmitter
/*      */ {
/*   45 */   private static final Log log = LogFactory.getLog(Stream.class);
/*   46 */   private static final StringManager sm = StringManager.getManager(Stream.class);
/*      */   
/*      */   private static final int HEADER_STATE_START = 0;
/*      */   private static final int HEADER_STATE_PSEUDO = 1;
/*      */   private static final int HEADER_STATE_REGULAR = 2;
/*      */   private static final int HEADER_STATE_TRAILER = 3;
/*      */   private static final MimeHeaders ACK_HEADERS;
/*      */   
/*      */   static
/*      */   {
/*   56 */     Response response = new Response();
/*   57 */     response.setStatus(100);
/*   58 */     StreamProcessor.prepareHeaders(null, response, null, null);
/*   59 */     ACK_HEADERS = response.getMimeHeaders();
/*      */   }
/*      */   
/*   62 */   private volatile int weight = 16;
/*   63 */   private volatile long contentLengthReceived = 0L;
/*      */   
/*      */   private final Http2UpgradeHandler handler;
/*      */   
/*      */   private final StreamStateMachine state;
/*   68 */   private int headerState = 0;
/*   69 */   private StreamException headerException = null;
/*      */   
/*      */   private final Request coyoteRequest;
/*   72 */   private StringBuilder cookieHeader = null;
/*   73 */   private final Response coyoteResponse = new Response();
/*      */   private final StreamInputBuffer inputBuffer;
/*   75 */   private final StreamOutputBuffer streamOutputBuffer = new StreamOutputBuffer();
/*   76 */   private final Http2OutputBuffer http2OutputBuffer = new Http2OutputBuffer(this.coyoteResponse, this.streamOutputBuffer);
/*      */   
/*      */ 
/*      */   public Stream(Integer identifier, Http2UpgradeHandler handler)
/*      */   {
/*   81 */     this(identifier, handler, null);
/*      */   }
/*      */   
/*      */   public Stream(Integer identifier, Http2UpgradeHandler handler, Request coyoteRequest)
/*      */   {
/*   86 */     super(identifier);
/*   87 */     this.handler = handler;
/*   88 */     handler.addChild(this);
/*   89 */     setWindowSize(handler.getRemoteSettings().getInitialWindowSize());
/*   90 */     this.state = new StreamStateMachine(this);
/*   91 */     if (coyoteRequest == null)
/*      */     {
/*   93 */       this.coyoteRequest = new Request();
/*   94 */       this.inputBuffer = new StreamInputBuffer();
/*   95 */       this.coyoteRequest.setInputBuffer(this.inputBuffer);
/*      */     }
/*      */     else {
/*   98 */       this.coyoteRequest = coyoteRequest;
/*   99 */       this.inputBuffer = null;
/*      */       
/*  101 */       this.state.receivedStartOfHeaders();
/*      */       
/*  103 */       this.state.receivedEndOfStream();
/*      */     }
/*      */     
/*  106 */     this.coyoteRequest.setSendfile(false);
/*  107 */     this.coyoteResponse.setOutputBuffer(this.http2OutputBuffer);
/*  108 */     this.coyoteRequest.setResponse(this.coyoteResponse);
/*  109 */     this.coyoteRequest.protocol().setString("HTTP/2.0");
/*  110 */     if (this.coyoteRequest.getStartTime() < 0L) {
/*  111 */       this.coyoteRequest.setStartTime(System.currentTimeMillis());
/*      */     }
/*      */   }
/*      */   
/*      */   void rePrioritise(AbstractStream parent, boolean exclusive, int weight)
/*      */   {
/*  117 */     if (log.isDebugEnabled()) {
/*  118 */       log.debug(sm.getString("stream.reprioritisation.debug", new Object[] {
/*  119 */         getConnectionId(), getIdentifier(), Boolean.toString(exclusive), parent
/*  120 */         .getIdentifier(), Integer.toString(weight) }));
/*      */     }
/*      */     
/*      */ 
/*  124 */     if (isDescendant(parent)) {
/*  125 */       parent.detachFromParent();
/*      */       
/*      */ 
/*  128 */       getParentStream().addChild((Stream)parent);
/*      */     }
/*      */     
/*  131 */     if (exclusive)
/*      */     {
/*      */ 
/*  134 */       Iterator<Stream> parentsChildren = parent.getChildStreams().iterator();
/*  135 */       while (parentsChildren.hasNext()) {
/*  136 */         Stream parentsChild = (Stream)parentsChildren.next();
/*  137 */         parentsChildren.remove();
/*  138 */         addChild(parentsChild);
/*      */       }
/*      */     }
/*  141 */     detachFromParent();
/*  142 */     parent.addChild(this);
/*  143 */     this.weight = weight;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   final void rePrioritise(AbstractStream parent, int weight)
/*      */   {
/*  152 */     if (log.isDebugEnabled()) {
/*  153 */       log.debug(sm.getString("stream.reprioritisation.debug", new Object[] {
/*  154 */         getConnectionId(), getIdentifier(), Boolean.FALSE, parent
/*  155 */         .getIdentifier(), Integer.toString(weight) }));
/*      */     }
/*      */     
/*  158 */     parent.addChild(this);
/*  159 */     this.weight = weight;
/*      */   }
/*      */   
/*      */   void receiveReset(long errorCode)
/*      */   {
/*  164 */     if (log.isDebugEnabled()) {
/*  165 */       log.debug(sm.getString("stream.reset.debug", new Object[] { getConnectionId(), getIdentifier(), 
/*  166 */         Long.toString(errorCode) }));
/*      */     }
/*      */     
/*  169 */     this.state.receivedReset();
/*      */     
/*  171 */     if (this.inputBuffer != null) {
/*  172 */       this.inputBuffer.receiveReset();
/*      */     }
/*      */     
/*  175 */     synchronized (this) {
/*  176 */       notifyAll();
/*      */     }
/*      */   }
/*      */   
/*      */   void checkState(FrameType frameType) throws Http2Exception
/*      */   {
/*  182 */     this.state.checkFrameType(frameType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected synchronized void incrementWindowSize(int windowSizeIncrement)
/*      */     throws Http2Exception
/*      */   {
/*  192 */     boolean notify = getWindowSize() < 1L;
/*  193 */     super.incrementWindowSize(windowSizeIncrement);
/*  194 */     if ((notify) && (getWindowSize() > 0L)) {
/*  195 */       notifyAll();
/*      */     }
/*      */   }
/*      */   
/*      */   private synchronized int reserveWindowSize(int reservation, boolean block) throws IOException
/*      */   {
/*  201 */     long windowSize = getWindowSize();
/*  202 */     while (windowSize < 1L) {
/*  203 */       if (!canWrite()) {
/*  204 */         throw new CloseNowException(sm.getString("stream.notWritable", new Object[] {
/*  205 */           getConnectionId(), getIdentifier() }));
/*      */       }
/*      */       try {
/*  208 */         if (block) {
/*  209 */           wait();
/*      */         } else {
/*  211 */           return 0;
/*      */         }
/*      */         
/*      */       }
/*      */       catch (InterruptedException e)
/*      */       {
/*  217 */         throw new IOException(e);
/*      */       }
/*  219 */       windowSize = getWindowSize(); }
/*      */     int allocation;
/*      */     int allocation;
/*  222 */     if (windowSize < reservation) {
/*  223 */       allocation = (int)windowSize;
/*      */     } else {
/*  225 */       allocation = reservation;
/*      */     }
/*  227 */     decrementWindowSize(allocation);
/*  228 */     return allocation;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void emitHeader(String name, String value)
/*      */     throws HpackException
/*      */   {
/*  241 */     if (log.isDebugEnabled()) {
/*  242 */       log.debug(sm.getString("stream.header.debug", new Object[] { getConnectionId(), getIdentifier(), name, value }));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  247 */     if (!name.toLowerCase(Locale.US).equals(name)) {
/*  248 */       throw new HpackException(sm.getString("stream.header.case", new Object[] {
/*  249 */         getConnectionId(), getIdentifier(), name }));
/*      */     }
/*      */     
/*  252 */     if ("connection".equals(name)) {
/*  253 */       throw new HpackException(sm.getString("stream.header.connection", new Object[] {
/*  254 */         getConnectionId(), getIdentifier() }));
/*      */     }
/*      */     
/*  257 */     if (("te".equals(name)) && 
/*  258 */       (!"trailers".equals(value))) {
/*  259 */       throw new HpackException(sm.getString("stream.header.te", new Object[] {
/*  260 */         getConnectionId(), getIdentifier(), value }));
/*      */     }
/*      */     
/*      */ 
/*  264 */     if (this.headerException != null)
/*      */     {
/*      */ 
/*  267 */       return;
/*      */     }
/*      */     
/*  270 */     boolean pseudoHeader = name.charAt(0) == ':';
/*      */     
/*  272 */     if ((pseudoHeader) && (this.headerState != 1))
/*      */     {
/*      */ 
/*  275 */       this.headerException = new StreamException(sm.getString("stream.header.unexpectedPseudoHeader", new Object[] {getConnectionId(), getIdentifier(), name }), Http2Error.PROTOCOL_ERROR, getIdentifier().intValue());
/*      */       
/*  277 */       return;
/*      */     }
/*      */     
/*  280 */     if ((this.headerState == 1) && (!pseudoHeader)) {
/*  281 */       this.headerState = 2;
/*      */     }
/*      */     
/*  284 */     switch (name) {
/*      */     case ":method": 
/*  286 */       if (this.coyoteRequest.method().isNull()) {
/*  287 */         this.coyoteRequest.method().setString(value);
/*      */       } else {
/*  289 */         throw new HpackException(sm.getString("stream.header.duplicate", new Object[] {
/*  290 */           getConnectionId(), getIdentifier(), ":method" }));
/*      */       }
/*      */       
/*      */       break;
/*      */     case ":scheme": 
/*  295 */       if (this.coyoteRequest.scheme().isNull()) {
/*  296 */         this.coyoteRequest.scheme().setString(value);
/*      */       } else {
/*  298 */         throw new HpackException(sm.getString("stream.header.duplicate", new Object[] {
/*  299 */           getConnectionId(), getIdentifier(), ":scheme" }));
/*      */       }
/*      */       
/*      */       break;
/*      */     case ":path": 
/*  304 */       if (!this.coyoteRequest.requestURI().isNull()) {
/*  305 */         throw new HpackException(sm.getString("stream.header.duplicate", new Object[] {
/*  306 */           getConnectionId(), getIdentifier(), ":path" }));
/*      */       }
/*  308 */       if (value.length() == 0) {
/*  309 */         throw new HpackException(sm.getString("stream.header.noPath", new Object[] {
/*  310 */           getConnectionId(), getIdentifier() }));
/*      */       }
/*  312 */       int queryStart = value.indexOf('?');
/*      */       String uri;
/*  314 */       String uri; if (queryStart == -1) {
/*  315 */         uri = value;
/*      */       } else {
/*  317 */         uri = value.substring(0, queryStart);
/*  318 */         String query = value.substring(queryStart + 1);
/*  319 */         this.coyoteRequest.queryString().setString(query);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  325 */       byte[] uriBytes = uri.getBytes(StandardCharsets.ISO_8859_1);
/*  326 */       this.coyoteRequest.requestURI().setBytes(uriBytes, 0, uriBytes.length);
/*  327 */       break;
/*      */     
/*      */     case ":authority": 
/*  330 */       if (this.coyoteRequest.serverName().isNull()) {
/*  331 */         int i = value.lastIndexOf(':');
/*  332 */         if (i > -1) {
/*  333 */           this.coyoteRequest.serverName().setString(value.substring(0, i));
/*  334 */           this.coyoteRequest.setServerPort(Integer.parseInt(value.substring(i + 1)));
/*      */         } else {
/*  336 */           this.coyoteRequest.serverName().setString(value);
/*      */         }
/*      */       } else {
/*  339 */         throw new HpackException(sm.getString("stream.header.duplicate", new Object[] {
/*  340 */           getConnectionId(), getIdentifier(), ":authority" }));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       break;
/*      */     case "cookie": 
/*  347 */       if (this.cookieHeader == null) {
/*  348 */         this.cookieHeader = new StringBuilder();
/*      */       } else {
/*  350 */         this.cookieHeader.append("; ");
/*      */       }
/*  352 */       this.cookieHeader.append(value);
/*  353 */       break;
/*      */     
/*      */     default: 
/*  356 */       if ((this.headerState != 3) || (this.handler.isTrailerHeaderAllowed(name)))
/*      */       {
/*      */ 
/*  359 */         if (("expect".equals(name)) && ("100-continue".equals(value))) {
/*  360 */           this.coyoteRequest.setExpectation(true);
/*      */         }
/*  362 */         if (pseudoHeader)
/*      */         {
/*      */ 
/*  365 */           this.headerException = new StreamException(sm.getString("stream.header.unknownPseudoHeader", new Object[] {getConnectionId(), getIdentifier(), name }), Http2Error.PROTOCOL_ERROR, getIdentifier().intValue());
/*      */         }
/*      */         
/*  368 */         this.coyoteRequest.getMimeHeaders().addValue(name).setString(value);
/*      */       }
/*      */       break;
/*      */     }
/*      */   }
/*      */   
/*      */   public void setHeaderException(StreamException streamException)
/*      */   {
/*  376 */     if (this.headerException == null) {
/*  377 */       this.headerException = streamException;
/*      */     }
/*      */   }
/*      */   
/*      */   public void validateHeaders()
/*      */     throws StreamException
/*      */   {
/*  384 */     if (this.headerException == null) {
/*  385 */       return;
/*      */     }
/*      */     
/*  388 */     throw this.headerException;
/*      */   }
/*      */   
/*      */   final boolean receivedEndOfHeaders() throws ConnectionException
/*      */   {
/*  393 */     if ((this.coyoteRequest.method().isNull()) || (this.coyoteRequest.scheme().isNull()) || 
/*  394 */       (this.coyoteRequest.requestURI().isNull())) {
/*  395 */       throw new ConnectionException(sm.getString("stream.header.required", new Object[] {
/*  396 */         getConnectionId(), getIdentifier() }), Http2Error.PROTOCOL_ERROR);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  401 */     if (this.cookieHeader != null) {
/*  402 */       this.coyoteRequest.getMimeHeaders().addValue("cookie").setString(this.cookieHeader.toString());
/*      */     }
/*  404 */     return (this.headerState == 2) || (this.headerState == 1);
/*      */   }
/*      */   
/*      */   void writeHeaders() throws IOException
/*      */   {
/*  409 */     boolean endOfStream = this.streamOutputBuffer.hasNoBody();
/*      */     
/*  411 */     this.handler.writeHeaders(this, 0, this.coyoteResponse.getMimeHeaders(), endOfStream, 1024);
/*      */   }
/*      */   
/*      */   final void addOutputFilter(OutputFilter filter)
/*      */   {
/*  416 */     this.http2OutputBuffer.addFilter(filter);
/*      */   }
/*      */   
/*      */   void writeAck()
/*      */     throws IOException
/*      */   {
/*  422 */     this.handler.writeHeaders(this, 0, ACK_HEADERS, false, 64);
/*      */   }
/*      */   
/*      */ 
/*      */   protected final String getConnectionId()
/*      */   {
/*  428 */     return this.handler.getConnectionId();
/*      */   }
/*      */   
/*      */ 
/*      */   protected int getWeight()
/*      */   {
/*  434 */     return this.weight;
/*      */   }
/*      */   
/*      */   Request getCoyoteRequest()
/*      */   {
/*  439 */     return this.coyoteRequest;
/*      */   }
/*      */   
/*      */   Response getCoyoteResponse()
/*      */   {
/*  444 */     return this.coyoteResponse;
/*      */   }
/*      */   
/*      */   ByteBuffer getInputByteBuffer()
/*      */   {
/*  449 */     return this.inputBuffer.getInBuffer();
/*      */   }
/*      */   
/*      */   final void receivedStartOfHeaders(boolean headersEndStream) throws Http2Exception
/*      */   {
/*  454 */     if (this.headerState == 0) {
/*  455 */       this.headerState = 1;
/*  456 */       this.handler.getHpackDecoder().setMaxHeaderCount(this.handler.getMaxHeaderCount());
/*  457 */       this.handler.getHpackDecoder().setMaxHeaderSize(this.handler.getMaxHeaderSize());
/*  458 */     } else if ((this.headerState == 1) || (this.headerState == 2))
/*      */     {
/*  460 */       if (headersEndStream) {
/*  461 */         this.headerState = 3;
/*  462 */         this.handler.getHpackDecoder().setMaxHeaderCount(this.handler.getMaxTrailerCount());
/*  463 */         this.handler.getHpackDecoder().setMaxHeaderSize(this.handler.getMaxTrailerSize());
/*      */       } else {
/*  465 */         throw new ConnectionException(sm.getString("stream.trailerHeader.noEndOfStream", new Object[] {
/*  466 */           getConnectionId(), getIdentifier() }), Http2Error.PROTOCOL_ERROR);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  471 */     this.state.receivedStartOfHeaders();
/*      */   }
/*      */   
/*      */   final void receivedData(int payloadSize) throws ConnectionException
/*      */   {
/*  476 */     this.contentLengthReceived += payloadSize;
/*  477 */     long contentLengthHeader = this.coyoteRequest.getContentLengthLong();
/*  478 */     if ((contentLengthHeader > -1L) && (this.contentLengthReceived > contentLengthHeader)) {
/*  479 */       throw new ConnectionException(sm.getString("stream.header.contentLength", new Object[] {
/*  480 */         getConnectionId(), getIdentifier(), Long.valueOf(contentLengthHeader), 
/*  481 */         Long.valueOf(this.contentLengthReceived) }), Http2Error.PROTOCOL_ERROR);
/*      */     }
/*      */   }
/*      */   
/*      */   final void receivedEndOfStream() throws ConnectionException
/*      */   {
/*  487 */     long contentLengthHeader = this.coyoteRequest.getContentLengthLong();
/*  488 */     if ((contentLengthHeader > -1L) && (this.contentLengthReceived != contentLengthHeader)) {
/*  489 */       throw new ConnectionException(sm.getString("stream.header.contentLength", new Object[] {
/*  490 */         getConnectionId(), getIdentifier(), Long.valueOf(contentLengthHeader), 
/*  491 */         Long.valueOf(this.contentLengthReceived) }), Http2Error.PROTOCOL_ERROR);
/*      */     }
/*  493 */     this.state.receivedEndOfStream();
/*  494 */     if (this.inputBuffer != null) {
/*  495 */       this.inputBuffer.notifyEof();
/*      */     }
/*      */   }
/*      */   
/*      */   void sentEndOfStream()
/*      */   {
/*  501 */     this.streamOutputBuffer.endOfStreamSent = true;
/*  502 */     this.state.sentEndOfStream();
/*      */   }
/*      */   
/*      */   final boolean isReady()
/*      */   {
/*  507 */     return this.streamOutputBuffer.isReady();
/*      */   }
/*      */   
/*      */   final boolean flush(boolean block) throws IOException
/*      */   {
/*  512 */     return this.streamOutputBuffer.flush(block);
/*      */   }
/*      */   
/*      */   StreamInputBuffer getInputBuffer()
/*      */   {
/*  517 */     return this.inputBuffer;
/*      */   }
/*      */   
/*      */   final HttpOutputBuffer getOutputBuffer()
/*      */   {
/*  522 */     return this.http2OutputBuffer;
/*      */   }
/*      */   
/*      */   void sentPushPromise()
/*      */   {
/*  527 */     this.state.sentPushPromise();
/*      */   }
/*      */   
/*      */   boolean isActive()
/*      */   {
/*  532 */     return this.state.isActive();
/*      */   }
/*      */   
/*      */   boolean canWrite()
/*      */   {
/*  537 */     return this.state.canWrite();
/*      */   }
/*      */   
/*      */   boolean isClosedFinal()
/*      */   {
/*  542 */     return this.state.isClosedFinal();
/*      */   }
/*      */   
/*      */   void closeIfIdle()
/*      */   {
/*  547 */     this.state.closeIfIdle();
/*      */   }
/*      */   
/*      */   boolean isInputFinished()
/*      */   {
/*  552 */     return !this.state.isFrameTypePermitted(FrameType.DATA);
/*      */   }
/*      */   
/*      */   void close(Http2Exception http2Exception)
/*      */   {
/*  557 */     if ((http2Exception instanceof StreamException)) {
/*      */       try {
/*  559 */         StreamException se = (StreamException)http2Exception;
/*  560 */         if (log.isDebugEnabled()) {
/*  561 */           log.debug(sm.getString("stream.reset.send", new Object[] { getConnectionId(), getIdentifier(), se
/*  562 */             .getError() }));
/*      */         }
/*  564 */         this.state.sendReset();
/*  565 */         this.handler.sendStreamReset(se);
/*      */       }
/*      */       catch (IOException ioe) {
/*  568 */         ConnectionException ce = new ConnectionException(sm.getString("stream.reset.fail"), Http2Error.PROTOCOL_ERROR);
/*  569 */         ce.initCause(ioe);
/*  570 */         this.handler.closeConnection(ce);
/*      */       }
/*      */     } else {
/*  573 */       this.handler.closeConnection(http2Exception);
/*      */     }
/*      */   }
/*      */   
/*      */   boolean isPushSupported()
/*      */   {
/*  579 */     return this.handler.getRemoteSettings().getEnablePush();
/*      */   }
/*      */   
/*      */   final void push(Request request) throws IOException
/*      */   {
/*  584 */     if (!isPushSupported()) {
/*  585 */       return;
/*      */     }
/*      */     
/*  588 */     request.getMimeHeaders().addValue(":method").duplicate(request.method());
/*  589 */     request.getMimeHeaders().addValue(":scheme").duplicate(request.scheme());
/*  590 */     StringBuilder path = new StringBuilder(request.requestURI().toString());
/*  591 */     if (!request.queryString().isNull()) {
/*  592 */       path.append('?');
/*  593 */       path.append(request.queryString().toString());
/*      */     }
/*  595 */     request.getMimeHeaders().addValue(":path").setString(path.toString());
/*      */     
/*      */ 
/*      */ 
/*  599 */     if (((!request.scheme().equals("http")) || (request.getServerPort() != 80)) && (
/*  600 */       (!request.scheme().equals("https")) || (request.getServerPort() != 443))) {
/*  601 */       request.getMimeHeaders().addValue(":authority").setString(request
/*  602 */         .serverName().getString() + ":" + request.getServerPort());
/*      */     } else {
/*  604 */       request.getMimeHeaders().addValue(":authority").duplicate(request.serverName());
/*      */     }
/*      */     
/*  607 */     push(this.handler, request, this);
/*      */   }
/*      */   
/*      */   private static void push(Http2UpgradeHandler handler, Request request, Stream stream)
/*      */     throws IOException
/*      */   {
/*  613 */     if (Constants.IS_SECURITY_ENABLED) {
/*      */       try {
/*  615 */         AccessController.doPrivileged(new PrivilegedPush(handler, request, stream));
/*      */       } catch (PrivilegedActionException ex) {
/*  617 */         Exception e = ex.getException();
/*  618 */         if ((e instanceof IOException)) {
/*  619 */           throw ((IOException)e);
/*      */         }
/*  621 */         throw new IOException(ex);
/*      */       }
/*      */       
/*      */     }
/*      */     else {
/*  626 */       handler.push(request, stream);
/*      */     }
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   protected synchronized void doNotifyAll() {}
/*      */   
/*      */   private static class PrivilegedPush implements PrivilegedExceptionAction<Void> {
/*      */     private final Http2UpgradeHandler handler;
/*      */     private final Request request;
/*      */     private final Stream stream;
/*      */     
/*      */     public PrivilegedPush(Http2UpgradeHandler handler, Request request, Stream stream) {
/*  639 */       this.handler = handler;
/*  640 */       this.request = request;
/*  641 */       this.stream = stream;
/*      */     }
/*      */     
/*      */     public Void run() throws IOException
/*      */     {
/*  646 */       this.handler.push(this.request, this.stream);
/*  647 */       return null;
/*      */     }
/*      */   }
/*      */   
/*      */   class StreamOutputBuffer
/*      */     implements HttpOutputBuffer
/*      */   {
/*  654 */     private final ByteBuffer buffer = ByteBuffer.allocate(8192);
/*  655 */     private volatile long written = 0L;
/*  656 */     private volatile boolean closed = false;
/*  657 */     private volatile boolean endOfStreamSent = false;
/*  658 */     private volatile boolean writeInterest = false;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     StreamOutputBuffer() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     @Deprecated
/*      */     public synchronized int doWrite(ByteChunk chunk)
/*      */       throws IOException
/*      */     {
/*  672 */       if (this.closed)
/*      */       {
/*  674 */         throw new IllegalStateException(Stream.sm.getString("stream.closed", new Object[] { Stream.this.getConnectionId(), Stream.this.getIdentifier() }));
/*      */       }
/*  676 */       int len = chunk.getLength();
/*  677 */       int offset = 0;
/*  678 */       while (len > 0) {
/*  679 */         int thisTime = Math.min(this.buffer.remaining(), len);
/*  680 */         this.buffer.put(chunk.getBytes(), chunk.getOffset() + offset, thisTime);
/*  681 */         offset += thisTime;
/*  682 */         len -= thisTime;
/*  683 */         if ((len > 0) && (!this.buffer.hasRemaining()))
/*      */         {
/*      */ 
/*  686 */           if (flush(true, Stream.this.coyoteResponse.getWriteListener() == null)) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*  691 */       this.written += offset;
/*  692 */       return offset;
/*      */     }
/*      */     
/*      */     public synchronized int doWrite(ByteBuffer chunk) throws IOException
/*      */     {
/*  697 */       if (this.closed)
/*      */       {
/*  699 */         throw new IllegalStateException(Stream.sm.getString("stream.closed", new Object[] { Stream.this.getConnectionId(), Stream.this.getIdentifier() }));
/*      */       }
/*  701 */       if (!Stream.this.coyoteResponse.isCommitted()) {
/*  702 */         Stream.this.coyoteResponse.sendHeaders();
/*      */       }
/*  704 */       int chunkLimit = chunk.limit();
/*  705 */       int offset = 0;
/*  706 */       while (chunk.remaining() > 0) {
/*  707 */         int thisTime = Math.min(this.buffer.remaining(), chunk.remaining());
/*  708 */         chunk.limit(chunk.position() + thisTime);
/*  709 */         this.buffer.put(chunk);
/*  710 */         chunk.limit(chunkLimit);
/*  711 */         offset += thisTime;
/*  712 */         if ((chunk.remaining() > 0) && (!this.buffer.hasRemaining()))
/*      */         {
/*      */ 
/*  715 */           if (flush(true, Stream.this.coyoteResponse.getWriteListener() == null)) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/*  720 */       this.written += offset;
/*  721 */       return offset;
/*      */     }
/*      */     
/*      */     public synchronized boolean flush(boolean block) throws IOException {
/*  725 */       return flush(false, block);
/*      */     }
/*      */     
/*      */     private synchronized boolean flush(boolean writeInProgress, boolean block) throws IOException
/*      */     {
/*  730 */       if (Stream.log.isDebugEnabled()) {
/*  731 */         Stream.log.debug(Stream.sm.getString("stream.outputBuffer.flush.debug", new Object[] { Stream.this.getConnectionId(), Stream.this
/*  732 */           .getIdentifier(), Integer.toString(this.buffer.position()), 
/*  733 */           Boolean.toString(writeInProgress), Boolean.toString(this.closed) }));
/*      */       }
/*  735 */       if (this.buffer.position() == 0) {
/*  736 */         if ((this.closed) && (!this.endOfStreamSent))
/*      */         {
/*      */ 
/*  739 */           Stream.this.handler.writeBody(Stream.this, this.buffer, 0, true);
/*      */         }
/*      */         
/*  742 */         return false;
/*      */       }
/*  744 */       this.buffer.flip();
/*  745 */       int left = this.buffer.remaining();
/*  746 */       while (left > 0) {
/*  747 */         int streamReservation = Stream.this.reserveWindowSize(left, block);
/*  748 */         if (streamReservation == 0)
/*      */         {
/*  750 */           this.buffer.compact();
/*  751 */           return true;
/*      */         }
/*  753 */         while (streamReservation > 0)
/*      */         {
/*  755 */           int connectionReservation = Stream.this.handler.reserveWindowSize(Stream.this, streamReservation);
/*      */           
/*  757 */           Stream.this.handler.writeBody(Stream.this, this.buffer, connectionReservation, (!writeInProgress) && (this.closed) && (left == connectionReservation));
/*      */           
/*  759 */           streamReservation -= connectionReservation;
/*  760 */           left -= connectionReservation;
/*      */         }
/*      */       }
/*  763 */       this.buffer.clear();
/*  764 */       return false;
/*      */     }
/*      */     
/*      */     synchronized boolean isReady() {
/*  768 */       if ((Stream.this.getWindowSize() > 0L) && (Stream.this.handler.getWindowSize() > 0L)) {
/*  769 */         return true;
/*      */       }
/*  771 */       this.writeInterest = true;
/*  772 */       return false;
/*      */     }
/*      */     
/*      */     synchronized boolean isRegisteredForWrite()
/*      */     {
/*  777 */       if (this.writeInterest) {
/*  778 */         this.writeInterest = false;
/*  779 */         return true;
/*      */       }
/*  781 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     public long getBytesWritten()
/*      */     {
/*  787 */       return this.written;
/*      */     }
/*      */     
/*      */     public final void end() throws IOException
/*      */     {
/*  792 */       this.closed = true;
/*  793 */       flush(true);
/*      */     }
/*      */     
/*      */     public boolean isClosed() {
/*  797 */       return this.closed;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean hasNoBody()
/*      */     {
/*  805 */       return (this.written == 0L) && (this.closed);
/*      */     }
/*      */     
/*      */     public void flush() throws IOException
/*      */     {
/*  810 */       flush(true);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   class StreamInputBuffer
/*      */     implements InputBuffer
/*      */   {
/*      */     private byte[] outBuffer;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private volatile ByteBuffer inBuffer;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private volatile boolean readInterest;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  842 */     private boolean reset = false;
/*      */     
/*      */ 
/*      */     StreamInputBuffer() {}
/*      */     
/*      */ 
/*      */     @Deprecated
/*      */     public int doRead(ByteChunk chunk)
/*      */       throws IOException
/*      */     {
/*  852 */       ensureBuffersExist();
/*      */       
/*  854 */       int written = -1;
/*      */       
/*      */ 
/*  857 */       synchronized (this.inBuffer) {
/*  858 */         boolean canRead = false;
/*  859 */         while (this.inBuffer.position() == 0) { if ((canRead = (Stream.this.isActive()) && (!Stream.this.isInputFinished()) ? 1 : 0) != 0) {
/*      */             try
/*      */             {
/*  862 */               if (Stream.log.isDebugEnabled()) {
/*  863 */                 Stream.log.debug(Stream.sm.getString("stream.inputBuffer.empty"));
/*      */               }
/*  865 */               this.inBuffer.wait();
/*  866 */               if (this.reset)
/*      */               {
/*  868 */                 throw new IOException("HTTP/2 Stream reset");
/*      */               }
/*      */               
/*      */             }
/*      */             catch (InterruptedException e)
/*      */             {
/*  874 */               throw new IOException(e);
/*      */             }
/*      */           }
/*      */         }
/*  878 */         if (this.inBuffer.position() > 0)
/*      */         {
/*      */ 
/*  881 */           this.inBuffer.flip();
/*  882 */           written = this.inBuffer.remaining();
/*  883 */           if (Stream.log.isDebugEnabled()) {
/*  884 */             Stream.log.debug(Stream.sm.getString("stream.inputBuffer.copy", new Object[] {
/*  885 */               Integer.toString(written) }));
/*      */           }
/*  887 */           this.inBuffer.get(this.outBuffer, 0, written);
/*  888 */           this.inBuffer.clear();
/*  889 */         } else { if (!canRead) {
/*  890 */             return -1;
/*      */           }
/*      */           
/*  893 */           throw new IllegalStateException();
/*      */         }
/*      */       }
/*      */       
/*  897 */       chunk.setBytes(this.outBuffer, 0, written);
/*      */       
/*      */ 
/*      */ 
/*  901 */       Stream.this.handler.writeWindowUpdate(Stream.this, written, true);
/*      */       
/*  903 */       return written;
/*      */     }
/*      */     
/*      */     public int doRead(ApplicationBufferHandler applicationBufferHandler)
/*      */       throws IOException
/*      */     {
/*  909 */       ensureBuffersExist();
/*      */       
/*  911 */       int written = -1;
/*      */       
/*      */ 
/*  914 */       synchronized (this.inBuffer) {
/*  915 */         boolean canRead = false;
/*  916 */         while (this.inBuffer.position() == 0) { if ((canRead = (Stream.this.isActive()) && (!Stream.this.isInputFinished()) ? 1 : 0) != 0) {
/*      */             try
/*      */             {
/*  919 */               if (Stream.log.isDebugEnabled()) {
/*  920 */                 Stream.log.debug(Stream.sm.getString("stream.inputBuffer.empty"));
/*      */               }
/*  922 */               this.inBuffer.wait();
/*  923 */               if (this.reset)
/*      */               {
/*  925 */                 throw new IOException("HTTP/2 Stream reset");
/*      */               }
/*      */               
/*      */             }
/*      */             catch (InterruptedException e)
/*      */             {
/*  931 */               throw new IOException(e);
/*      */             }
/*      */           }
/*      */         }
/*  935 */         if (this.inBuffer.position() > 0)
/*      */         {
/*      */ 
/*  938 */           this.inBuffer.flip();
/*  939 */           written = this.inBuffer.remaining();
/*  940 */           if (Stream.log.isDebugEnabled()) {
/*  941 */             Stream.log.debug(Stream.sm.getString("stream.inputBuffer.copy", new Object[] {
/*  942 */               Integer.toString(written) }));
/*      */           }
/*  944 */           this.inBuffer.get(this.outBuffer, 0, written);
/*  945 */           this.inBuffer.clear();
/*  946 */         } else { if (!canRead) {
/*  947 */             return -1;
/*      */           }
/*      */           
/*  950 */           throw new IllegalStateException();
/*      */         }
/*      */       }
/*      */       
/*  954 */       applicationBufferHandler.setByteBuffer(ByteBuffer.wrap(this.outBuffer, 0, written));
/*      */       
/*      */ 
/*      */ 
/*  958 */       Stream.this.handler.writeWindowUpdate(Stream.this, written, true);
/*      */       
/*  960 */       return written;
/*      */     }
/*      */     
/*      */     void registerReadInterest()
/*      */     {
/*  965 */       if (this.inBuffer != null) {
/*  966 */         synchronized (this.inBuffer) {
/*  967 */           this.readInterest = true;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     synchronized boolean isRequestBodyFullyRead()
/*      */     {
/*  974 */       return ((this.inBuffer == null) || (this.inBuffer.position() == 0)) && (Stream.this.isInputFinished());
/*      */     }
/*      */     
/*      */     synchronized int available()
/*      */     {
/*  979 */       if (this.inBuffer == null) {
/*  980 */         return 0;
/*      */       }
/*  982 */       return this.inBuffer.position();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     synchronized boolean onDataAvailable()
/*      */     {
/*  990 */       if (this.readInterest) {
/*  991 */         if (Stream.log.isDebugEnabled()) {
/*  992 */           Stream.log.debug(Stream.sm.getString("stream.inputBuffer.dispatch"));
/*      */         }
/*  994 */         this.readInterest = false;
/*  995 */         Stream.this.coyoteRequest.action(ActionCode.DISPATCH_READ, null);
/*      */         
/*      */ 
/*      */ 
/*  999 */         Stream.this.coyoteRequest.action(ActionCode.DISPATCH_EXECUTE, null);
/* 1000 */         return true;
/*      */       }
/* 1002 */       if (Stream.log.isDebugEnabled()) {
/* 1003 */         Stream.log.debug(Stream.sm.getString("stream.inputBuffer.signal"));
/*      */       }
/* 1005 */       synchronized (this.inBuffer) {
/* 1006 */         this.inBuffer.notifyAll();
/*      */       }
/* 1008 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */     public ByteBuffer getInBuffer()
/*      */     {
/* 1014 */       ensureBuffersExist();
/* 1015 */       return this.inBuffer;
/*      */     }
/*      */     
/*      */     protected synchronized void insertReplayedBody(ByteChunk body)
/*      */     {
/* 1020 */       this.inBuffer = ByteBuffer.wrap(body.getBytes(), body.getOffset(), body.getLength());
/*      */     }
/*      */     
/*      */     private void ensureBuffersExist()
/*      */     {
/* 1025 */       if (this.inBuffer == null)
/*      */       {
/*      */ 
/*      */ 
/* 1029 */         int size = Stream.this.handler.getLocalSettings().getInitialWindowSize();
/* 1030 */         synchronized (this) {
/* 1031 */           if (this.inBuffer == null) {
/* 1032 */             this.inBuffer = ByteBuffer.allocate(size);
/* 1033 */             this.outBuffer = new byte[size];
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     protected void receiveReset()
/*      */     {
/* 1041 */       if (this.inBuffer != null) {
/* 1042 */         synchronized (this.inBuffer) {
/* 1043 */           this.reset = true;
/* 1044 */           this.inBuffer.notifyAll();
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private final void notifyEof() {
/* 1050 */       if (this.inBuffer != null) {
/* 1051 */         synchronized (this.inBuffer) {
/* 1052 */           this.inBuffer.notifyAll();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\Stream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */