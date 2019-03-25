/*      */ package org.apache.tomcat.websocket;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.net.SocketTimeoutException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.charset.CharsetEncoder;
/*      */ import java.nio.charset.CoderResult;
/*      */ import java.util.ArrayDeque;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Queue;
/*      */ import java.util.concurrent.Future;
/*      */ import java.util.concurrent.Semaphore;
/*      */ import java.util.concurrent.TimeUnit;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import javax.websocket.CloseReason;
/*      */ import javax.websocket.CloseReason.CloseCodes;
/*      */ import javax.websocket.DeploymentException;
/*      */ import javax.websocket.EncodeException;
/*      */ import javax.websocket.Encoder;
/*      */ import javax.websocket.Encoder.Binary;
/*      */ import javax.websocket.Encoder.BinaryStream;
/*      */ import javax.websocket.Encoder.Text;
/*      */ import javax.websocket.Encoder.TextStream;
/*      */ import javax.websocket.EndpointConfig;
/*      */ import javax.websocket.RemoteEndpoint;
/*      */ import javax.websocket.SendHandler;
/*      */ import javax.websocket.SendResult;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.buf.Utf8Encoder;
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
/*      */ public abstract class WsRemoteEndpointImplBase
/*      */   implements RemoteEndpoint
/*      */ {
/*   54 */   private static final StringManager sm = StringManager.getManager(WsRemoteEndpointImplBase.class);
/*      */   
/*   56 */   protected static final SendResult SENDRESULT_OK = new SendResult();
/*      */   
/*   58 */   private final Log log = LogFactory.getLog(WsRemoteEndpointImplBase.class);
/*      */   
/*   60 */   private final StateMachine stateMachine = new StateMachine(null);
/*      */   
/*   62 */   private final IntermediateMessageHandler intermediateMessageHandler = new IntermediateMessageHandler(this);
/*      */   
/*      */ 
/*   65 */   private Transformation transformation = null;
/*   66 */   private final Semaphore messagePartInProgress = new Semaphore(1);
/*   67 */   private final Queue<MessagePart> messagePartQueue = new ArrayDeque();
/*   68 */   private final Object messagePartLock = new Object();
/*      */   
/*      */ 
/*   71 */   private volatile boolean closed = false;
/*   72 */   private boolean fragmented = false;
/*   73 */   private boolean nextFragmented = false;
/*   74 */   private boolean text = false;
/*   75 */   private boolean nextText = false;
/*      */   
/*      */ 
/*   78 */   private final ByteBuffer headerBuffer = ByteBuffer.allocate(14);
/*   79 */   private final ByteBuffer outputBuffer = ByteBuffer.allocate(Constants.DEFAULT_BUFFER_SIZE);
/*   80 */   private final CharsetEncoder encoder = new Utf8Encoder();
/*   81 */   private final ByteBuffer encoderBuffer = ByteBuffer.allocate(Constants.DEFAULT_BUFFER_SIZE);
/*   82 */   private final AtomicBoolean batchingAllowed = new AtomicBoolean(false);
/*   83 */   private volatile long sendTimeout = -1L;
/*      */   private WsSession wsSession;
/*   85 */   private List<EncoderEntry> encoderEntries = new ArrayList();
/*      */   
/*      */   protected void setTransformation(Transformation transformation)
/*      */   {
/*   89 */     this.transformation = transformation;
/*      */   }
/*      */   
/*      */   public long getSendTimeout()
/*      */   {
/*   94 */     return this.sendTimeout;
/*      */   }
/*      */   
/*      */   public void setSendTimeout(long timeout)
/*      */   {
/*   99 */     this.sendTimeout = timeout;
/*      */   }
/*      */   
/*      */   public void setBatchingAllowed(boolean batchingAllowed)
/*      */     throws IOException
/*      */   {
/*  105 */     boolean oldValue = this.batchingAllowed.getAndSet(batchingAllowed);
/*      */     
/*  107 */     if ((oldValue) && (!batchingAllowed)) {
/*  108 */       flushBatch();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean getBatchingAllowed()
/*      */   {
/*  115 */     return this.batchingAllowed.get();
/*      */   }
/*      */   
/*      */   public void flushBatch()
/*      */     throws IOException
/*      */   {
/*  121 */     sendMessageBlock((byte)24, null, true);
/*      */   }
/*      */   
/*      */   public void sendBytes(ByteBuffer data) throws IOException
/*      */   {
/*  126 */     if (data == null) {
/*  127 */       throw new IllegalArgumentException(sm.getString("wsRemoteEndpoint.nullData"));
/*      */     }
/*  129 */     this.stateMachine.binaryStart();
/*  130 */     sendMessageBlock((byte)2, data, true);
/*  131 */     this.stateMachine.complete(true);
/*      */   }
/*      */   
/*      */   public Future<Void> sendBytesByFuture(ByteBuffer data)
/*      */   {
/*  136 */     FutureToSendHandler f2sh = new FutureToSendHandler(this.wsSession);
/*  137 */     sendBytesByCompletion(data, f2sh);
/*  138 */     return f2sh;
/*      */   }
/*      */   
/*      */   public void sendBytesByCompletion(ByteBuffer data, SendHandler handler)
/*      */   {
/*  143 */     if (data == null) {
/*  144 */       throw new IllegalArgumentException(sm.getString("wsRemoteEndpoint.nullData"));
/*      */     }
/*  146 */     if (handler == null) {
/*  147 */       throw new IllegalArgumentException(sm.getString("wsRemoteEndpoint.nullHandler"));
/*      */     }
/*  149 */     StateUpdateSendHandler sush = new StateUpdateSendHandler(handler, this.stateMachine);
/*  150 */     this.stateMachine.binaryStart();
/*  151 */     startMessage((byte)2, data, true, sush);
/*      */   }
/*      */   
/*      */   public void sendPartialBytes(ByteBuffer partialByte, boolean last)
/*      */     throws IOException
/*      */   {
/*  157 */     if (partialByte == null) {
/*  158 */       throw new IllegalArgumentException(sm.getString("wsRemoteEndpoint.nullData"));
/*      */     }
/*  160 */     this.stateMachine.binaryPartialStart();
/*  161 */     sendMessageBlock((byte)2, partialByte, last);
/*  162 */     this.stateMachine.complete(last);
/*      */   }
/*      */   
/*      */ 
/*      */   public void sendPing(ByteBuffer applicationData)
/*      */     throws IOException, IllegalArgumentException
/*      */   {
/*  169 */     if (applicationData.remaining() > 125) {
/*  170 */       throw new IllegalArgumentException(sm.getString("wsRemoteEndpoint.tooMuchData"));
/*      */     }
/*  172 */     sendMessageBlock((byte)9, applicationData, true);
/*      */   }
/*      */   
/*      */ 
/*      */   public void sendPong(ByteBuffer applicationData)
/*      */     throws IOException, IllegalArgumentException
/*      */   {
/*  179 */     if (applicationData.remaining() > 125) {
/*  180 */       throw new IllegalArgumentException(sm.getString("wsRemoteEndpoint.tooMuchData"));
/*      */     }
/*  182 */     sendMessageBlock((byte)10, applicationData, true);
/*      */   }
/*      */   
/*      */   public void sendString(String text) throws IOException
/*      */   {
/*  187 */     if (text == null) {
/*  188 */       throw new IllegalArgumentException(sm.getString("wsRemoteEndpoint.nullData"));
/*      */     }
/*  190 */     this.stateMachine.textStart();
/*  191 */     sendMessageBlock(CharBuffer.wrap(text), true);
/*      */   }
/*      */   
/*      */   public Future<Void> sendStringByFuture(String text)
/*      */   {
/*  196 */     FutureToSendHandler f2sh = new FutureToSendHandler(this.wsSession);
/*  197 */     sendStringByCompletion(text, f2sh);
/*  198 */     return f2sh;
/*      */   }
/*      */   
/*      */   public void sendStringByCompletion(String text, SendHandler handler)
/*      */   {
/*  203 */     if (text == null) {
/*  204 */       throw new IllegalArgumentException(sm.getString("wsRemoteEndpoint.nullData"));
/*      */     }
/*  206 */     if (handler == null) {
/*  207 */       throw new IllegalArgumentException(sm.getString("wsRemoteEndpoint.nullHandler"));
/*      */     }
/*  209 */     this.stateMachine.textStart();
/*      */     
/*  211 */     TextMessageSendHandler tmsh = new TextMessageSendHandler(handler, CharBuffer.wrap(text), true, this.encoder, this.encoderBuffer, this);
/*  212 */     tmsh.write();
/*      */   }
/*      */   
/*      */ 
/*      */   public void sendPartialString(String fragment, boolean isLast)
/*      */     throws IOException
/*      */   {
/*  219 */     if (fragment == null) {
/*  220 */       throw new IllegalArgumentException(sm.getString("wsRemoteEndpoint.nullData"));
/*      */     }
/*  222 */     this.stateMachine.textPartialStart();
/*  223 */     sendMessageBlock(CharBuffer.wrap(fragment), isLast);
/*      */   }
/*      */   
/*      */   public OutputStream getSendStream()
/*      */   {
/*  228 */     this.stateMachine.streamStart();
/*  229 */     return new WsOutputStream(this);
/*      */   }
/*      */   
/*      */   public Writer getSendWriter()
/*      */   {
/*  234 */     this.stateMachine.writeStart();
/*  235 */     return new WsWriter(this);
/*      */   }
/*      */   
/*      */   void sendMessageBlock(CharBuffer part, boolean last) throws IOException
/*      */   {
/*  240 */     long timeoutExpiry = getTimeoutExpiry();
/*  241 */     boolean isDone = false;
/*  242 */     while (!isDone) {
/*  243 */       this.encoderBuffer.clear();
/*  244 */       CoderResult cr = this.encoder.encode(part, this.encoderBuffer, true);
/*  245 */       if (cr.isError()) {
/*  246 */         throw new IllegalArgumentException(cr.toString());
/*      */       }
/*  248 */       isDone = !cr.isOverflow();
/*  249 */       this.encoderBuffer.flip();
/*  250 */       sendMessageBlock((byte)1, this.encoderBuffer, (last) && (isDone), timeoutExpiry);
/*      */     }
/*  252 */     this.stateMachine.complete(last);
/*      */   }
/*      */   
/*      */   void sendMessageBlock(byte opCode, ByteBuffer payload, boolean last)
/*      */     throws IOException
/*      */   {
/*  258 */     sendMessageBlock(opCode, payload, last, getTimeoutExpiry());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private long getTimeoutExpiry()
/*      */   {
/*  266 */     long timeout = getBlockingSendTimeout();
/*  267 */     if (timeout < 0L) {
/*  268 */       return Long.MAX_VALUE;
/*      */     }
/*  270 */     return System.currentTimeMillis() + timeout;
/*      */   }
/*      */   
/*      */ 
/*      */   private void sendMessageBlock(byte opCode, ByteBuffer payload, boolean last, long timeoutExpiry)
/*      */     throws IOException
/*      */   {
/*  277 */     this.wsSession.updateLastActive();
/*      */     
/*  279 */     BlockingSendHandler bsh = new BlockingSendHandler(null);
/*      */     
/*  281 */     List<MessagePart> messageParts = new ArrayList();
/*  282 */     messageParts.add(new MessagePart(last, 0, opCode, payload, bsh, bsh, timeoutExpiry));
/*      */     
/*  284 */     messageParts = this.transformation.sendMessagePart(messageParts);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  289 */     if (messageParts.size() == 0) {
/*  290 */       return;
/*      */     }
/*      */     
/*  293 */     long timeout = timeoutExpiry - System.currentTimeMillis();
/*      */     try {
/*  295 */       if (!this.messagePartInProgress.tryAcquire(timeout, TimeUnit.MILLISECONDS)) {
/*  296 */         String msg = sm.getString("wsRemoteEndpoint.acquireTimeout");
/*  297 */         this.wsSession.doClose(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, msg), new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, msg));
/*      */         
/*  299 */         throw new SocketTimeoutException(msg);
/*      */       }
/*      */     } catch (InterruptedException e) {
/*  302 */       String msg = sm.getString("wsRemoteEndpoint.sendInterrupt");
/*  303 */       this.wsSession.doClose(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, msg), new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, msg));
/*      */       
/*  305 */       throw new IOException(msg, e);
/*      */     }
/*      */     
/*  308 */     for (MessagePart mp : messageParts) {
/*  309 */       writeMessagePart(mp);
/*  310 */       if (!bsh.getSendResult().isOK()) {
/*  311 */         this.messagePartInProgress.release();
/*  312 */         Throwable t = bsh.getSendResult().getException();
/*  313 */         this.wsSession.doClose(new CloseReason(CloseReason.CloseCodes.GOING_AWAY, t.getMessage()), new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, t
/*  314 */           .getMessage()));
/*  315 */         throw new IOException(t);
/*      */       }
/*      */       
/*      */ 
/*  319 */       this.fragmented = this.nextFragmented;
/*  320 */       this.text = this.nextText;
/*      */     }
/*      */     
/*  323 */     if (payload != null) {
/*  324 */       payload.clear();
/*      */     }
/*      */     
/*  327 */     endMessage(null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   void startMessage(byte opCode, ByteBuffer payload, boolean last, SendHandler handler)
/*      */   {
/*  334 */     this.wsSession.updateLastActive();
/*      */     
/*  336 */     List<MessagePart> messageParts = new ArrayList();
/*  337 */     messageParts.add(new MessagePart(last, 0, opCode, payload, this.intermediateMessageHandler, new EndMessageHandler(this, handler), -1L));
/*      */     
/*      */ 
/*      */ 
/*  341 */     messageParts = this.transformation.sendMessagePart(messageParts);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  346 */     if (messageParts.size() == 0) {
/*  347 */       handler.onResult(new SendResult());
/*  348 */       return;
/*      */     }
/*      */     
/*  351 */     MessagePart mp = (MessagePart)messageParts.remove(0);
/*      */     
/*  353 */     boolean doWrite = false;
/*  354 */     synchronized (this.messagePartLock) {
/*  355 */       if ((8 == mp.getOpCode()) && (getBatchingAllowed()))
/*      */       {
/*      */ 
/*  358 */         this.log.warn(sm.getString("wsRemoteEndpoint.flushOnCloseFailed"));
/*      */       }
/*  360 */       if (this.messagePartInProgress.tryAcquire()) {
/*  361 */         doWrite = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  372 */         this.messagePartQueue.add(mp);
/*      */       }
/*      */       
/*  375 */       this.messagePartQueue.addAll(messageParts);
/*      */     }
/*  377 */     if (doWrite)
/*      */     {
/*      */ 
/*      */ 
/*  381 */       writeMessagePart(mp);
/*      */     }
/*      */   }
/*      */   
/*      */   void endMessage(SendHandler handler, SendResult result)
/*      */   {
/*  387 */     boolean doWrite = false;
/*  388 */     MessagePart mpNext = null;
/*  389 */     synchronized (this.messagePartLock)
/*      */     {
/*  391 */       this.fragmented = this.nextFragmented;
/*  392 */       this.text = this.nextText;
/*      */       
/*  394 */       mpNext = (MessagePart)this.messagePartQueue.poll();
/*  395 */       if (mpNext == null) {
/*  396 */         this.messagePartInProgress.release();
/*  397 */       } else if (!this.closed)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  402 */         doWrite = true;
/*      */       }
/*      */     }
/*  405 */     if (doWrite)
/*      */     {
/*      */ 
/*      */ 
/*  409 */       writeMessagePart(mpNext);
/*      */     }
/*      */     
/*  412 */     this.wsSession.updateLastActive();
/*      */     
/*      */ 
/*      */ 
/*  416 */     if (handler != null) {
/*  417 */       handler.onResult(result);
/*      */     }
/*      */   }
/*      */   
/*      */   void writeMessagePart(MessagePart mp)
/*      */   {
/*  423 */     if (this.closed)
/*      */     {
/*  425 */       throw new IllegalStateException(sm.getString("wsRemoteEndpoint.closed"));
/*      */     }
/*      */     
/*  428 */     if (24 == mp.getOpCode()) {
/*  429 */       this.nextFragmented = this.fragmented;
/*  430 */       this.nextText = this.text;
/*  431 */       this.outputBuffer.flip();
/*      */       
/*  433 */       SendHandler flushHandler = new OutputBufferFlushSendHandler(this.outputBuffer, mp.getEndHandler());
/*  434 */       doWrite(flushHandler, mp.getBlockingWriteTimeoutExpiry(), new ByteBuffer[] { this.outputBuffer }); return;
/*      */     }
/*      */     
/*      */     boolean first;
/*      */     
/*      */     boolean first;
/*      */     
/*  441 */     if (Util.isControl(mp.getOpCode())) {
/*  442 */       this.nextFragmented = this.fragmented;
/*  443 */       this.nextText = this.text;
/*  444 */       if (mp.getOpCode() == 8) {
/*  445 */         this.closed = true;
/*      */       }
/*  447 */       first = true;
/*      */     } else {
/*  449 */       boolean isText = Util.isText(mp.getOpCode());
/*      */       boolean first;
/*  451 */       if (this.fragmented)
/*      */       {
/*  453 */         if (this.text != isText)
/*      */         {
/*  455 */           throw new IllegalStateException(sm.getString("wsRemoteEndpoint.changeType"));
/*      */         }
/*  457 */         this.nextText = this.text;
/*  458 */         this.nextFragmented = (!mp.isFin());
/*  459 */         first = false;
/*      */       }
/*      */       else {
/*  462 */         if (mp.isFin()) {
/*  463 */           this.nextFragmented = false;
/*      */         } else {
/*  465 */           this.nextFragmented = true;
/*  466 */           this.nextText = isText;
/*      */         }
/*  468 */         first = true;
/*      */       }
/*      */     }
/*      */     
/*      */     byte[] mask;
/*      */     byte[] mask;
/*  474 */     if (isMasked()) {
/*  475 */       mask = Util.generateMask();
/*      */     } else {
/*  477 */       mask = null;
/*      */     }
/*      */     
/*  480 */     this.headerBuffer.clear();
/*  481 */     writeHeader(this.headerBuffer, mp.isFin(), mp.getRsv(), mp.getOpCode(), 
/*  482 */       isMasked(), mp.getPayload(), mask, first);
/*  483 */     this.headerBuffer.flip();
/*      */     
/*  485 */     if ((getBatchingAllowed()) || (isMasked()))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*  490 */       OutputBufferSendHandler obsh = new OutputBufferSendHandler(mp.getEndHandler(), mp.getBlockingWriteTimeoutExpiry(), this.headerBuffer, mp.getPayload(), mask, this.outputBuffer, !getBatchingAllowed(), this);
/*  491 */       obsh.write();
/*      */     }
/*      */     else {
/*  494 */       doWrite(mp.getEndHandler(), mp.getBlockingWriteTimeoutExpiry(), new ByteBuffer[] { this.headerBuffer, mp
/*  495 */         .getPayload() });
/*      */     }
/*      */   }
/*      */   
/*      */   private long getBlockingSendTimeout()
/*      */   {
/*  501 */     Object obj = this.wsSession.getUserProperties().get("org.apache.tomcat.websocket.BLOCKING_SEND_TIMEOUT");
/*  502 */     Long userTimeout = null;
/*  503 */     if ((obj instanceof Long)) {
/*  504 */       userTimeout = (Long)obj;
/*      */     }
/*  506 */     if (userTimeout == null) {
/*  507 */       return 20000L;
/*      */     }
/*  509 */     return userTimeout.longValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private static class EndMessageHandler
/*      */     implements SendHandler
/*      */   {
/*      */     private final WsRemoteEndpointImplBase endpoint;
/*      */     
/*      */ 
/*      */     private final SendHandler handler;
/*      */     
/*      */ 
/*      */     public EndMessageHandler(WsRemoteEndpointImplBase endpoint, SendHandler handler)
/*      */     {
/*  525 */       this.endpoint = endpoint;
/*  526 */       this.handler = handler;
/*      */     }
/*      */     
/*      */ 
/*      */     public void onResult(SendResult result)
/*      */     {
/*  532 */       this.endpoint.endMessage(this.handler, result);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static class IntermediateMessageHandler
/*      */     implements SendHandler
/*      */   {
/*      */     private final WsRemoteEndpointImplBase endpoint;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public IntermediateMessageHandler(WsRemoteEndpointImplBase endpoint)
/*      */     {
/*  551 */       this.endpoint = endpoint;
/*      */     }
/*      */     
/*      */ 
/*      */     public void onResult(SendResult result)
/*      */     {
/*  557 */       this.endpoint.endMessage(null, result);
/*      */     }
/*      */   }
/*      */   
/*      */   public void sendObject(Object obj)
/*      */     throws IOException, EncodeException
/*      */   {
/*  564 */     if (obj == null) {
/*  565 */       throw new IllegalArgumentException(sm.getString("wsRemoteEndpoint.nullData"));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  572 */     Encoder encoder = findEncoder(obj);
/*  573 */     if ((encoder == null) && (Util.isPrimitive(obj.getClass()))) {
/*  574 */       String msg = obj.toString();
/*  575 */       sendString(msg);
/*  576 */       return;
/*      */     }
/*  578 */     if ((encoder == null) && (byte[].class.isAssignableFrom(obj.getClass()))) {
/*  579 */       ByteBuffer msg = ByteBuffer.wrap((byte[])obj);
/*  580 */       sendBytes(msg);
/*  581 */       return;
/*      */     }
/*      */     
/*  584 */     if ((encoder instanceof Encoder.Text)) {
/*  585 */       String msg = ((Encoder.Text)encoder).encode(obj);
/*  586 */       sendString(msg); } else { Throwable localThrowable6;
/*  587 */       if ((encoder instanceof Encoder.TextStream)) {
/*  588 */         Writer w = getSendWriter();localThrowable6 = null;
/*  589 */         try { ((Encoder.TextStream)encoder).encode(obj, w);
/*      */         }
/*      */         catch (Throwable localThrowable1)
/*      */         {
/*  588 */           localThrowable6 = localThrowable1;throw localThrowable1;
/*      */         } finally {
/*  590 */           if (w != null) if (localThrowable6 != null) try { w.close(); } catch (Throwable localThrowable2) { localThrowable6.addSuppressed(localThrowable2); } else w.close();
/*  591 */         } } else if ((encoder instanceof Encoder.Binary)) {
/*  592 */         ByteBuffer msg = ((Encoder.Binary)encoder).encode(obj);
/*  593 */         sendBytes(msg);
/*  594 */       } else if ((encoder instanceof Encoder.BinaryStream)) {
/*  595 */         OutputStream os = getSendStream();localThrowable6 = null;
/*  596 */         try { ((Encoder.BinaryStream)encoder).encode(obj, os);
/*      */         }
/*      */         catch (Throwable localThrowable4)
/*      */         {
/*  595 */           localThrowable6 = localThrowable4;throw localThrowable4;
/*      */         } finally {
/*  597 */           if (os != null) if (localThrowable6 != null) try { os.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else os.close();
/*      */         }
/*  599 */       } else { throw new EncodeException(obj, sm.getString("wsRemoteEndpoint.noEncoder", new Object[] {obj
/*  600 */           .getClass() }));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   public Future<Void> sendObjectByFuture(Object obj) {
/*  606 */     FutureToSendHandler f2sh = new FutureToSendHandler(this.wsSession);
/*  607 */     sendObjectByCompletion(obj, f2sh);
/*  608 */     return f2sh;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void sendObjectByCompletion(Object obj, SendHandler completion)
/*      */   {
/*  615 */     if (obj == null) {
/*  616 */       throw new IllegalArgumentException(sm.getString("wsRemoteEndpoint.nullData"));
/*      */     }
/*  618 */     if (completion == null) {
/*  619 */       throw new IllegalArgumentException(sm.getString("wsRemoteEndpoint.nullHandler"));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  627 */     Encoder encoder = findEncoder(obj);
/*  628 */     if ((encoder == null) && (Util.isPrimitive(obj.getClass()))) {
/*  629 */       String msg = obj.toString();
/*  630 */       sendStringByCompletion(msg, completion);
/*  631 */       return;
/*      */     }
/*  633 */     if ((encoder == null) && (byte[].class.isAssignableFrom(obj.getClass()))) {
/*  634 */       ByteBuffer msg = ByteBuffer.wrap((byte[])obj);
/*  635 */       sendBytesByCompletion(msg, completion);
/*  636 */       return;
/*      */     }
/*      */     try
/*      */     {
/*  640 */       if ((encoder instanceof Encoder.Text)) {
/*  641 */         String msg = ((Encoder.Text)encoder).encode(obj);
/*  642 */         sendStringByCompletion(msg, completion); } else { Throwable localThrowable6;
/*  643 */         if ((encoder instanceof Encoder.TextStream)) {
/*  644 */           Writer w = getSendWriter();localThrowable6 = null;
/*  645 */           try { ((Encoder.TextStream)encoder).encode(obj, w);
/*      */           }
/*      */           catch (Throwable localThrowable1)
/*      */           {
/*  644 */             localThrowable6 = localThrowable1;throw localThrowable1;
/*      */           } finally {
/*  646 */             if (w != null) if (localThrowable6 != null) try { w.close(); } catch (Throwable localThrowable2) { localThrowable6.addSuppressed(localThrowable2); } else w.close(); }
/*  647 */           completion.onResult(new SendResult());
/*  648 */         } else if ((encoder instanceof Encoder.Binary)) {
/*  649 */           ByteBuffer msg = ((Encoder.Binary)encoder).encode(obj);
/*  650 */           sendBytesByCompletion(msg, completion);
/*  651 */         } else if ((encoder instanceof Encoder.BinaryStream)) {
/*  652 */           OutputStream os = getSendStream();localThrowable6 = null;
/*  653 */           try { ((Encoder.BinaryStream)encoder).encode(obj, os);
/*      */           }
/*      */           catch (Throwable localThrowable4)
/*      */           {
/*  652 */             localThrowable6 = localThrowable4;throw localThrowable4;
/*      */           } finally {
/*  654 */             if (os != null) if (localThrowable6 != null) try { os.close(); } catch (Throwable localThrowable5) { localThrowable6.addSuppressed(localThrowable5); } else os.close(); }
/*  655 */           completion.onResult(new SendResult());
/*      */         } else {
/*  657 */           throw new EncodeException(obj, sm.getString("wsRemoteEndpoint.noEncoder", new Object[] {obj
/*  658 */             .getClass() }));
/*      */         }
/*      */       }
/*  661 */     } catch (Exception e) { SendResult sr = new SendResult(e);
/*  662 */       completion.onResult(sr);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void setSession(WsSession wsSession)
/*      */   {
/*  668 */     this.wsSession = wsSession;
/*      */   }
/*      */   
/*      */   protected void setEncoders(EndpointConfig endpointConfig)
/*      */     throws DeploymentException
/*      */   {
/*  674 */     this.encoderEntries.clear();
/*      */     
/*  676 */     for (Class<? extends Encoder> encoderClazz : endpointConfig.getEncoders())
/*      */     {
/*      */       try {
/*  679 */         Encoder instance = (Encoder)encoderClazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*  680 */         instance.init(endpointConfig);
/*      */       }
/*      */       catch (ReflectiveOperationException e) {
/*  683 */         throw new DeploymentException(sm.getString("wsRemoteEndpoint.invalidEncoder", new Object[] {encoderClazz
/*  684 */           .getName() }), e);
/*      */       }
/*      */       Encoder instance;
/*  687 */       EncoderEntry entry = new EncoderEntry(Util.getEncoderType(encoderClazz), instance);
/*  688 */       this.encoderEntries.add(entry);
/*      */     }
/*      */   }
/*      */   
/*      */   private Encoder findEncoder(Object obj)
/*      */   {
/*  694 */     for (EncoderEntry entry : this.encoderEntries) {
/*  695 */       if (entry.getClazz().isAssignableFrom(obj.getClass())) {
/*  696 */         return entry.getEncoder();
/*      */       }
/*      */     }
/*  699 */     return null;
/*      */   }
/*      */   
/*      */   public final void close()
/*      */   {
/*  704 */     for (EncoderEntry entry : this.encoderEntries) {
/*  705 */       entry.getEncoder().destroy();
/*      */     }
/*      */     
/*      */ 
/*  709 */     this.transformation.close();
/*  710 */     doClose();
/*      */   }
/*      */   
/*      */ 
/*      */   protected abstract void doWrite(SendHandler paramSendHandler, long paramLong, ByteBuffer... paramVarArgs);
/*      */   
/*      */ 
/*      */   protected abstract boolean isMasked();
/*      */   
/*      */   protected abstract void doClose();
/*      */   
/*      */   private static void writeHeader(ByteBuffer headerBuffer, boolean fin, int rsv, byte opCode, boolean masked, ByteBuffer payload, byte[] mask, boolean first)
/*      */   {
/*  723 */     byte b = 0;
/*      */     
/*  725 */     if (fin)
/*      */     {
/*  727 */       b = (byte)(b - 128);
/*      */     }
/*      */     
/*  730 */     b = (byte)(b + (rsv << 4));
/*      */     
/*  732 */     if (first)
/*      */     {
/*  734 */       b = (byte)(b + opCode);
/*      */     }
/*      */     
/*      */ 
/*  738 */     headerBuffer.put(b);
/*      */     
/*  740 */     if (masked) {
/*  741 */       b = Byte.MIN_VALUE;
/*      */     } else {
/*  743 */       b = 0;
/*      */     }
/*      */     
/*      */ 
/*  747 */     if (payload.limit() < 126) {
/*  748 */       headerBuffer.put((byte)(payload.limit() | b));
/*  749 */     } else if (payload.limit() < 65536) {
/*  750 */       headerBuffer.put((byte)(0x7E | b));
/*  751 */       headerBuffer.put((byte)(payload.limit() >>> 8));
/*  752 */       headerBuffer.put((byte)(payload.limit() & 0xFF));
/*      */     }
/*      */     else {
/*  755 */       headerBuffer.put((byte)(0x7F | b));
/*  756 */       headerBuffer.put((byte)0);
/*  757 */       headerBuffer.put((byte)0);
/*  758 */       headerBuffer.put((byte)0);
/*  759 */       headerBuffer.put((byte)0);
/*  760 */       headerBuffer.put((byte)(payload.limit() >>> 24));
/*  761 */       headerBuffer.put((byte)(payload.limit() >>> 16));
/*  762 */       headerBuffer.put((byte)(payload.limit() >>> 8));
/*  763 */       headerBuffer.put((byte)(payload.limit() & 0xFF));
/*      */     }
/*  765 */     if (masked) {
/*  766 */       headerBuffer.put(mask[0]);
/*  767 */       headerBuffer.put(mask[1]);
/*  768 */       headerBuffer.put(mask[2]);
/*  769 */       headerBuffer.put(mask[3]);
/*      */     }
/*      */   }
/*      */   
/*      */   private class TextMessageSendHandler
/*      */     implements SendHandler
/*      */   {
/*      */     private final SendHandler handler;
/*      */     private final CharBuffer message;
/*      */     private final boolean isLast;
/*      */     private final CharsetEncoder encoder;
/*      */     private final ByteBuffer buffer;
/*      */     private final WsRemoteEndpointImplBase endpoint;
/*  782 */     private volatile boolean isDone = false;
/*      */     
/*      */ 
/*      */     public TextMessageSendHandler(SendHandler handler, CharBuffer message, boolean isLast, CharsetEncoder encoder, ByteBuffer encoderBuffer, WsRemoteEndpointImplBase endpoint)
/*      */     {
/*  787 */       this.handler = handler;
/*  788 */       this.message = message;
/*  789 */       this.isLast = isLast;
/*  790 */       this.encoder = encoder.reset();
/*  791 */       this.buffer = encoderBuffer;
/*  792 */       this.endpoint = endpoint;
/*      */     }
/*      */     
/*      */     public void write() {
/*  796 */       this.buffer.clear();
/*  797 */       CoderResult cr = this.encoder.encode(this.message, this.buffer, true);
/*  798 */       if (cr.isError()) {
/*  799 */         throw new IllegalArgumentException(cr.toString());
/*      */       }
/*  801 */       this.isDone = (!cr.isOverflow());
/*  802 */       this.buffer.flip();
/*  803 */       this.endpoint.startMessage((byte)1, this.buffer, (this.isDone) && (this.isLast), this);
/*      */     }
/*      */     
/*      */ 
/*      */     public void onResult(SendResult result)
/*      */     {
/*  809 */       if (this.isDone) {
/*  810 */         this.endpoint.stateMachine.complete(this.isLast);
/*  811 */         this.handler.onResult(result);
/*  812 */       } else if (!result.isOK()) {
/*  813 */         this.handler.onResult(result);
/*  814 */       } else if (WsRemoteEndpointImplBase.this.closed)
/*      */       {
/*  816 */         SendResult sr = new SendResult(new IOException(WsRemoteEndpointImplBase.sm.getString("wsRemoteEndpoint.closedDuringMessage")));
/*  817 */         this.handler.onResult(sr);
/*      */       } else {
/*  819 */         write();
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class OutputBufferSendHandler
/*      */     implements SendHandler
/*      */   {
/*      */     private final SendHandler handler;
/*      */     
/*      */     private final long blockingWriteTimeoutExpiry;
/*      */     
/*      */     private final ByteBuffer headerBuffer;
/*      */     
/*      */     private final ByteBuffer payload;
/*      */     private final byte[] mask;
/*      */     private final ByteBuffer outputBuffer;
/*      */     private final boolean flushRequired;
/*      */     private final WsRemoteEndpointImplBase endpoint;
/*  839 */     private int maskIndex = 0;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public OutputBufferSendHandler(SendHandler completion, long blockingWriteTimeoutExpiry, ByteBuffer headerBuffer, ByteBuffer payload, byte[] mask, ByteBuffer outputBuffer, boolean flushRequired, WsRemoteEndpointImplBase endpoint)
/*      */     {
/*  846 */       this.blockingWriteTimeoutExpiry = blockingWriteTimeoutExpiry;
/*  847 */       this.handler = completion;
/*  848 */       this.headerBuffer = headerBuffer;
/*  849 */       this.payload = payload;
/*  850 */       this.mask = mask;
/*  851 */       this.outputBuffer = outputBuffer;
/*  852 */       this.flushRequired = flushRequired;
/*  853 */       this.endpoint = endpoint;
/*      */     }
/*      */     
/*      */     public void write()
/*      */     {
/*  858 */       while ((this.headerBuffer.hasRemaining()) && (this.outputBuffer.hasRemaining())) {
/*  859 */         this.outputBuffer.put(this.headerBuffer.get());
/*      */       }
/*  861 */       if (this.headerBuffer.hasRemaining())
/*      */       {
/*  863 */         this.outputBuffer.flip();
/*  864 */         this.endpoint.doWrite(this, this.blockingWriteTimeoutExpiry, new ByteBuffer[] { this.outputBuffer });
/*  865 */         return;
/*      */       }
/*      */       
/*      */ 
/*  869 */       int payloadLeft = this.payload.remaining();
/*  870 */       int payloadLimit = this.payload.limit();
/*  871 */       int outputSpace = this.outputBuffer.remaining();
/*  872 */       int toWrite = payloadLeft;
/*      */       
/*  874 */       if (payloadLeft > outputSpace) {
/*  875 */         toWrite = outputSpace;
/*      */         
/*  877 */         this.payload.limit(this.payload.position() + toWrite);
/*      */       }
/*      */       
/*  880 */       if (this.mask == null)
/*      */       {
/*  882 */         this.outputBuffer.put(this.payload);
/*      */       } else {
/*  884 */         for (int i = 0; i < toWrite; i++) {
/*  885 */           this.outputBuffer.put(
/*  886 */             (byte)(this.payload.get() ^ this.mask[(this.maskIndex++)] & 0xFF));
/*  887 */           if (this.maskIndex > 3) {
/*  888 */             this.maskIndex = 0;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  893 */       if (payloadLeft > outputSpace)
/*      */       {
/*  895 */         this.payload.limit(payloadLimit);
/*      */         
/*  897 */         this.outputBuffer.flip();
/*  898 */         this.endpoint.doWrite(this, this.blockingWriteTimeoutExpiry, new ByteBuffer[] { this.outputBuffer });
/*  899 */         return;
/*      */       }
/*      */       
/*  902 */       if (this.flushRequired) {
/*  903 */         this.outputBuffer.flip();
/*  904 */         if (this.outputBuffer.remaining() == 0) {
/*  905 */           this.handler.onResult(WsRemoteEndpointImplBase.SENDRESULT_OK);
/*      */         } else {
/*  907 */           this.endpoint.doWrite(this, this.blockingWriteTimeoutExpiry, new ByteBuffer[] { this.outputBuffer });
/*      */         }
/*      */       } else {
/*  910 */         this.handler.onResult(WsRemoteEndpointImplBase.SENDRESULT_OK);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */     public void onResult(SendResult result)
/*      */     {
/*  917 */       if (result.isOK()) {
/*  918 */         if (this.outputBuffer.hasRemaining()) {
/*  919 */           this.endpoint.doWrite(this, this.blockingWriteTimeoutExpiry, new ByteBuffer[] { this.outputBuffer });
/*      */         } else {
/*  921 */           this.outputBuffer.clear();
/*  922 */           write();
/*      */         }
/*      */       } else {
/*  925 */         this.handler.onResult(result);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private static class OutputBufferFlushSendHandler
/*      */     implements SendHandler
/*      */   {
/*      */     private final ByteBuffer outputBuffer;
/*      */     
/*      */     private final SendHandler handler;
/*      */     
/*      */     public OutputBufferFlushSendHandler(ByteBuffer outputBuffer, SendHandler handler)
/*      */     {
/*  940 */       this.outputBuffer = outputBuffer;
/*  941 */       this.handler = handler;
/*      */     }
/*      */     
/*      */     public void onResult(SendResult result)
/*      */     {
/*  946 */       if (result.isOK()) {
/*  947 */         this.outputBuffer.clear();
/*      */       }
/*  949 */       this.handler.onResult(result);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class WsOutputStream
/*      */     extends OutputStream
/*      */   {
/*      */     private final WsRemoteEndpointImplBase endpoint;
/*  957 */     private final ByteBuffer buffer = ByteBuffer.allocate(Constants.DEFAULT_BUFFER_SIZE);
/*  958 */     private final Object closeLock = new Object();
/*  959 */     private volatile boolean closed = false;
/*  960 */     private volatile boolean used = false;
/*      */     
/*      */     public WsOutputStream(WsRemoteEndpointImplBase endpoint) {
/*  963 */       this.endpoint = endpoint;
/*      */     }
/*      */     
/*      */     public void write(int b) throws IOException
/*      */     {
/*  968 */       if (this.closed)
/*      */       {
/*  970 */         throw new IllegalStateException(WsRemoteEndpointImplBase.sm.getString("wsRemoteEndpoint.closedOutputStream"));
/*      */       }
/*      */       
/*  973 */       this.used = true;
/*  974 */       if (this.buffer.remaining() == 0) {
/*  975 */         flush();
/*      */       }
/*  977 */       this.buffer.put((byte)b);
/*      */     }
/*      */     
/*      */     public void write(byte[] b, int off, int len) throws IOException
/*      */     {
/*  982 */       if (this.closed)
/*      */       {
/*  984 */         throw new IllegalStateException(WsRemoteEndpointImplBase.sm.getString("wsRemoteEndpoint.closedOutputStream"));
/*      */       }
/*  986 */       if (len == 0) {
/*  987 */         return;
/*      */       }
/*  989 */       if ((off < 0) || (off > b.length) || (len < 0) || (off + len > b.length) || (off + len < 0))
/*      */       {
/*  991 */         throw new IndexOutOfBoundsException();
/*      */       }
/*      */       
/*  994 */       this.used = true;
/*  995 */       if (this.buffer.remaining() == 0) {
/*  996 */         flush();
/*      */       }
/*  998 */       int remaining = this.buffer.remaining();
/*  999 */       int written = 0;
/*      */       
/* 1001 */       while (remaining < len - written) {
/* 1002 */         this.buffer.put(b, off + written, remaining);
/* 1003 */         written += remaining;
/* 1004 */         flush();
/* 1005 */         remaining = this.buffer.remaining();
/*      */       }
/* 1007 */       this.buffer.put(b, off + written, len - written);
/*      */     }
/*      */     
/*      */     public void flush() throws IOException
/*      */     {
/* 1012 */       if (this.closed)
/*      */       {
/* 1014 */         throw new IllegalStateException(WsRemoteEndpointImplBase.sm.getString("wsRemoteEndpoint.closedOutputStream"));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/* 1019 */       if ((!Constants.STREAMS_DROP_EMPTY_MESSAGES) || (this.buffer.position() > 0)) {
/* 1020 */         doWrite(false);
/*      */       }
/*      */     }
/*      */     
/*      */     public void close() throws IOException
/*      */     {
/* 1026 */       synchronized (this.closeLock) {
/* 1027 */         if (this.closed) {
/* 1028 */           return;
/*      */         }
/* 1030 */         this.closed = true;
/*      */       }
/*      */       
/* 1033 */       doWrite(true);
/*      */     }
/*      */     
/*      */     private void doWrite(boolean last) throws IOException {
/* 1037 */       if ((!Constants.STREAMS_DROP_EMPTY_MESSAGES) || (this.used)) {
/* 1038 */         this.buffer.flip();
/* 1039 */         this.endpoint.sendMessageBlock((byte)2, this.buffer, last);
/*      */       }
/* 1041 */       this.endpoint.stateMachine.complete(last);
/* 1042 */       this.buffer.clear();
/*      */     }
/*      */   }
/*      */   
/*      */   private static class WsWriter
/*      */     extends Writer
/*      */   {
/*      */     private final WsRemoteEndpointImplBase endpoint;
/* 1050 */     private final CharBuffer buffer = CharBuffer.allocate(Constants.DEFAULT_BUFFER_SIZE);
/* 1051 */     private final Object closeLock = new Object();
/* 1052 */     private volatile boolean closed = false;
/* 1053 */     private volatile boolean used = false;
/*      */     
/*      */     public WsWriter(WsRemoteEndpointImplBase endpoint) {
/* 1056 */       this.endpoint = endpoint;
/*      */     }
/*      */     
/*      */     public void write(char[] cbuf, int off, int len) throws IOException
/*      */     {
/* 1061 */       if (this.closed)
/*      */       {
/* 1063 */         throw new IllegalStateException(WsRemoteEndpointImplBase.sm.getString("wsRemoteEndpoint.closedWriter"));
/*      */       }
/* 1065 */       if (len == 0) {
/* 1066 */         return;
/*      */       }
/* 1068 */       if ((off < 0) || (off > cbuf.length) || (len < 0) || (off + len > cbuf.length) || (off + len < 0))
/*      */       {
/* 1070 */         throw new IndexOutOfBoundsException();
/*      */       }
/*      */       
/* 1073 */       this.used = true;
/* 1074 */       if (this.buffer.remaining() == 0) {
/* 1075 */         flush();
/*      */       }
/* 1077 */       int remaining = this.buffer.remaining();
/* 1078 */       int written = 0;
/*      */       
/* 1080 */       while (remaining < len - written) {
/* 1081 */         this.buffer.put(cbuf, off + written, remaining);
/* 1082 */         written += remaining;
/* 1083 */         flush();
/* 1084 */         remaining = this.buffer.remaining();
/*      */       }
/* 1086 */       this.buffer.put(cbuf, off + written, len - written);
/*      */     }
/*      */     
/*      */     public void flush() throws IOException
/*      */     {
/* 1091 */       if (this.closed)
/*      */       {
/* 1093 */         throw new IllegalStateException(WsRemoteEndpointImplBase.sm.getString("wsRemoteEndpoint.closedWriter"));
/*      */       }
/*      */       
/* 1096 */       if ((!Constants.STREAMS_DROP_EMPTY_MESSAGES) || (this.buffer.position() > 0)) {
/* 1097 */         doWrite(false);
/*      */       }
/*      */     }
/*      */     
/*      */     public void close() throws IOException
/*      */     {
/* 1103 */       synchronized (this.closeLock) {
/* 1104 */         if (this.closed) {
/* 1105 */           return;
/*      */         }
/* 1107 */         this.closed = true;
/*      */       }
/*      */       
/* 1110 */       doWrite(true);
/*      */     }
/*      */     
/*      */     private void doWrite(boolean last) throws IOException {
/* 1114 */       if ((!Constants.STREAMS_DROP_EMPTY_MESSAGES) || (this.used)) {
/* 1115 */         this.buffer.flip();
/* 1116 */         this.endpoint.sendMessageBlock(this.buffer, last);
/* 1117 */         this.buffer.clear();
/*      */       } else {
/* 1119 */         this.endpoint.stateMachine.complete(last);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static class EncoderEntry
/*      */   {
/*      */     private final Class<?> clazz;
/*      */     private final Encoder encoder;
/*      */     
/*      */     public EncoderEntry(Class<?> clazz, Encoder encoder)
/*      */     {
/* 1131 */       this.clazz = clazz;
/* 1132 */       this.encoder = encoder;
/*      */     }
/*      */     
/*      */     public Class<?> getClazz() {
/* 1136 */       return this.clazz;
/*      */     }
/*      */     
/*      */     public Encoder getEncoder() {
/* 1140 */       return this.encoder;
/*      */     }
/*      */   }
/*      */   
/*      */   private static enum State
/*      */   {
/* 1146 */     OPEN, 
/* 1147 */     STREAM_WRITING, 
/* 1148 */     WRITER_WRITING, 
/* 1149 */     BINARY_PARTIAL_WRITING, 
/* 1150 */     BINARY_PARTIAL_READY, 
/* 1151 */     BINARY_FULL_WRITING, 
/* 1152 */     TEXT_PARTIAL_WRITING, 
/* 1153 */     TEXT_PARTIAL_READY, 
/* 1154 */     TEXT_FULL_WRITING;
/*      */     
/*      */     private State() {}
/*      */   }
/*      */   
/* 1159 */   private static class StateMachine { private WsRemoteEndpointImplBase.State state = WsRemoteEndpointImplBase.State.OPEN;
/*      */     
/*      */     public synchronized void streamStart() {
/* 1162 */       checkState(new WsRemoteEndpointImplBase.State[] { WsRemoteEndpointImplBase.State.OPEN });
/* 1163 */       this.state = WsRemoteEndpointImplBase.State.STREAM_WRITING;
/*      */     }
/*      */     
/*      */     public synchronized void writeStart() {
/* 1167 */       checkState(new WsRemoteEndpointImplBase.State[] { WsRemoteEndpointImplBase.State.OPEN });
/* 1168 */       this.state = WsRemoteEndpointImplBase.State.WRITER_WRITING;
/*      */     }
/*      */     
/*      */     public synchronized void binaryPartialStart() {
/* 1172 */       checkState(new WsRemoteEndpointImplBase.State[] { WsRemoteEndpointImplBase.State.OPEN, WsRemoteEndpointImplBase.State.BINARY_PARTIAL_READY });
/* 1173 */       this.state = WsRemoteEndpointImplBase.State.BINARY_PARTIAL_WRITING;
/*      */     }
/*      */     
/*      */     public synchronized void binaryStart() {
/* 1177 */       checkState(new WsRemoteEndpointImplBase.State[] { WsRemoteEndpointImplBase.State.OPEN });
/* 1178 */       this.state = WsRemoteEndpointImplBase.State.BINARY_FULL_WRITING;
/*      */     }
/*      */     
/*      */     public synchronized void textPartialStart() {
/* 1182 */       checkState(new WsRemoteEndpointImplBase.State[] { WsRemoteEndpointImplBase.State.OPEN, WsRemoteEndpointImplBase.State.TEXT_PARTIAL_READY });
/* 1183 */       this.state = WsRemoteEndpointImplBase.State.TEXT_PARTIAL_WRITING;
/*      */     }
/*      */     
/*      */     public synchronized void textStart() {
/* 1187 */       checkState(new WsRemoteEndpointImplBase.State[] { WsRemoteEndpointImplBase.State.OPEN });
/* 1188 */       this.state = WsRemoteEndpointImplBase.State.TEXT_FULL_WRITING;
/*      */     }
/*      */     
/*      */     public synchronized void complete(boolean last) {
/* 1192 */       if (last) {
/* 1193 */         checkState(new WsRemoteEndpointImplBase.State[] { WsRemoteEndpointImplBase.State.TEXT_PARTIAL_WRITING, WsRemoteEndpointImplBase.State.TEXT_FULL_WRITING, WsRemoteEndpointImplBase.State.BINARY_PARTIAL_WRITING, WsRemoteEndpointImplBase.State.BINARY_FULL_WRITING, WsRemoteEndpointImplBase.State.STREAM_WRITING, WsRemoteEndpointImplBase.State.WRITER_WRITING });
/*      */         
/*      */ 
/* 1196 */         this.state = WsRemoteEndpointImplBase.State.OPEN;
/*      */       } else {
/* 1198 */         checkState(new WsRemoteEndpointImplBase.State[] { WsRemoteEndpointImplBase.State.TEXT_PARTIAL_WRITING, WsRemoteEndpointImplBase.State.BINARY_PARTIAL_WRITING, WsRemoteEndpointImplBase.State.STREAM_WRITING, WsRemoteEndpointImplBase.State.WRITER_WRITING });
/*      */         
/* 1200 */         if (this.state == WsRemoteEndpointImplBase.State.TEXT_PARTIAL_WRITING) {
/* 1201 */           this.state = WsRemoteEndpointImplBase.State.TEXT_PARTIAL_READY;
/* 1202 */         } else if (this.state == WsRemoteEndpointImplBase.State.BINARY_PARTIAL_WRITING) {
/* 1203 */           this.state = WsRemoteEndpointImplBase.State.BINARY_PARTIAL_READY;
/* 1204 */         } else if (this.state != WsRemoteEndpointImplBase.State.WRITER_WRITING)
/*      */         {
/* 1206 */           if (this.state != WsRemoteEndpointImplBase.State.STREAM_WRITING)
/*      */           {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1212 */             throw new IllegalStateException("BUG: This code should never be called");
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */     private void checkState(WsRemoteEndpointImplBase.State... required) {
/* 1219 */       for (WsRemoteEndpointImplBase.State state : required) {
/* 1220 */         if (this.state == state) {
/* 1221 */           return;
/*      */         }
/*      */       }
/*      */       
/* 1225 */       throw new IllegalStateException(WsRemoteEndpointImplBase.sm.getString("wsRemoteEndpoint.wrongState", new Object[] { this.state }));
/*      */     }
/*      */   }
/*      */   
/*      */   private static class StateUpdateSendHandler implements SendHandler
/*      */   {
/*      */     private final SendHandler handler;
/*      */     private final WsRemoteEndpointImplBase.StateMachine stateMachine;
/*      */     
/*      */     public StateUpdateSendHandler(SendHandler handler, WsRemoteEndpointImplBase.StateMachine stateMachine)
/*      */     {
/* 1236 */       this.handler = handler;
/* 1237 */       this.stateMachine = stateMachine;
/*      */     }
/*      */     
/*      */     public void onResult(SendResult result)
/*      */     {
/* 1242 */       if (result.isOK()) {
/* 1243 */         this.stateMachine.complete(true);
/*      */       }
/* 1245 */       this.handler.onResult(result);
/*      */     }
/*      */   }
/*      */   
/*      */   private static class BlockingSendHandler
/*      */     implements SendHandler
/*      */   {
/* 1252 */     private SendResult sendResult = null;
/*      */     
/*      */     public void onResult(SendResult result)
/*      */     {
/* 1256 */       this.sendResult = result;
/*      */     }
/*      */     
/*      */     public SendResult getSendResult() {
/* 1260 */       return this.sendResult;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\WsRemoteEndpointImplBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */