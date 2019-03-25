/*      */ package org.apache.tomcat.websocket;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.nio.Buffer;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.nio.charset.CharsetDecoder;
/*      */ import java.nio.charset.CoderResult;
/*      */ import java.nio.charset.CodingErrorAction;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
/*      */ import javax.websocket.CloseReason;
/*      */ import javax.websocket.CloseReason.CloseCodes;
/*      */ import javax.websocket.Endpoint;
/*      */ import javax.websocket.Extension;
/*      */ import javax.websocket.MessageHandler;
/*      */ import javax.websocket.MessageHandler.Partial;
/*      */ import javax.websocket.MessageHandler.Whole;
/*      */ import javax.websocket.PongMessage;
/*      */ import javax.websocket.RemoteEndpoint.Basic;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.buf.Utf8Decoder;
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
/*      */ public abstract class WsFrameBase
/*      */ {
/*   46 */   private static final StringManager sm = StringManager.getManager(WsFrameBase.class);
/*      */   
/*      */ 
/*      */   protected final WsSession wsSession;
/*      */   
/*      */ 
/*      */   protected final ByteBuffer inputBuffer;
/*      */   
/*      */   private final Transformation transformation;
/*      */   
/*   56 */   private final ByteBuffer controlBufferBinary = ByteBuffer.allocate(125);
/*   57 */   private final CharBuffer controlBufferText = CharBuffer.allocate(125);
/*      */   
/*      */ 
/*   60 */   private final CharsetDecoder utf8DecoderControl = new Utf8Decoder()
/*   61 */     .onMalformedInput(CodingErrorAction.REPORT)
/*   62 */     .onUnmappableCharacter(CodingErrorAction.REPORT);
/*   63 */   private final CharsetDecoder utf8DecoderMessage = new Utf8Decoder()
/*   64 */     .onMalformedInput(CodingErrorAction.REPORT)
/*   65 */     .onUnmappableCharacter(CodingErrorAction.REPORT);
/*   66 */   private boolean continuationExpected = false;
/*   67 */   private boolean textMessage = false;
/*      */   
/*      */   private ByteBuffer messageBufferBinary;
/*      */   
/*      */   private CharBuffer messageBufferText;
/*   72 */   private MessageHandler binaryMsgHandler = null;
/*   73 */   private MessageHandler textMsgHandler = null;
/*      */   
/*      */ 
/*   76 */   private boolean fin = false;
/*   77 */   private int rsv = 0;
/*   78 */   private byte opCode = 0;
/*   79 */   private final byte[] mask = new byte[4];
/*   80 */   private int maskIndex = 0;
/*   81 */   private long payloadLength = 0L;
/*   82 */   private volatile long payloadWritten = 0L;
/*      */   
/*      */ 
/*   85 */   private volatile State state = State.NEW_FRAME;
/*   86 */   private volatile boolean open = true;
/*      */   
/*      */ 
/*   89 */   private static final AtomicReferenceFieldUpdater<WsFrameBase, ReadState> READ_STATE_UPDATER = AtomicReferenceFieldUpdater.newUpdater(WsFrameBase.class, ReadState.class, "readState");
/*   90 */   private volatile ReadState readState = ReadState.WAITING;
/*      */   
/*      */   public WsFrameBase(WsSession wsSession, Transformation transformation) {
/*   93 */     this.inputBuffer = ByteBuffer.allocate(Constants.DEFAULT_BUFFER_SIZE);
/*   94 */     this.inputBuffer.position(0).limit(0);
/*   95 */     this.messageBufferBinary = ByteBuffer.allocate(wsSession.getMaxBinaryMessageBufferSize());
/*   96 */     this.messageBufferText = CharBuffer.allocate(wsSession.getMaxTextMessageBufferSize());
/*   97 */     wsSession.setWsFrame(this);
/*   98 */     this.wsSession = wsSession;
/*      */     Transformation finalTransformation;
/*  100 */     Transformation finalTransformation; if (isMasked()) {
/*  101 */       finalTransformation = new UnmaskTransformation(null);
/*      */     } else {
/*  103 */       finalTransformation = new NoopTransformation(null);
/*      */     }
/*  105 */     if (transformation == null) {
/*  106 */       this.transformation = finalTransformation;
/*      */     } else {
/*  108 */       transformation.setNext(finalTransformation);
/*  109 */       this.transformation = transformation;
/*      */     }
/*      */   }
/*      */   
/*      */   protected void processInputBuffer() throws IOException
/*      */   {
/*  115 */     while (!isSuspended()) {
/*  116 */       this.wsSession.updateLastActive();
/*  117 */       if (this.state == State.NEW_FRAME) {
/*  118 */         if (!processInitialHeader()) {
/*      */           break;
/*      */         }
/*      */         
/*      */ 
/*  123 */         if (!this.open) {
/*  124 */           throw new IOException(sm.getString("wsFrame.closed"));
/*      */         }
/*      */       }
/*  127 */       if ((this.state != State.PARTIAL_HEADER) || 
/*  128 */         (processRemainingHeader()))
/*      */       {
/*      */ 
/*      */ 
/*  132 */         if ((this.state == State.DATA) && 
/*  133 */           (!processData())) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean processInitialHeader()
/*      */     throws IOException
/*      */   {
/*  147 */     if (this.inputBuffer.remaining() < 2) {
/*  148 */       return false;
/*      */     }
/*  150 */     int b = this.inputBuffer.get();
/*  151 */     this.fin = ((b & 0x80) != 0);
/*  152 */     this.rsv = ((b & 0x70) >>> 4);
/*  153 */     this.opCode = ((byte)(b & 0xF));
/*  154 */     if (!this.transformation.validateRsv(this.rsv, this.opCode))
/*      */     {
/*      */ 
/*  157 */       throw new WsIOException(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, sm.getString("wsFrame.wrongRsv", new Object[] {Integer.valueOf(this.rsv), Integer.valueOf(this.opCode) })));
/*      */     }
/*      */     
/*  160 */     if (Util.isControl(this.opCode)) {
/*  161 */       if (!this.fin)
/*      */       {
/*      */ 
/*  164 */         throw new WsIOException(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, sm.getString("wsFrame.controlFragmented")));
/*      */       }
/*  166 */       if ((this.opCode != 9) && (this.opCode != 10) && (this.opCode != 8))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  171 */         throw new WsIOException(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, sm.getString("wsFrame.invalidOpCode", new Object[] {Integer.valueOf(this.opCode) })));
/*      */       }
/*      */     } else {
/*  174 */       if (this.continuationExpected) {
/*  175 */         if (!Util.isContinuation(this.opCode))
/*      */         {
/*      */ 
/*  178 */           throw new WsIOException(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, sm.getString("wsFrame.noContinuation")));
/*      */         }
/*      */       } else {
/*      */         try {
/*  182 */           if (this.opCode == 2)
/*      */           {
/*  184 */             this.textMessage = false;
/*  185 */             int size = this.wsSession.getMaxBinaryMessageBufferSize();
/*  186 */             if (size != this.messageBufferBinary.capacity()) {
/*  187 */               this.messageBufferBinary = ByteBuffer.allocate(size);
/*      */             }
/*  189 */             this.binaryMsgHandler = this.wsSession.getBinaryMessageHandler();
/*  190 */             this.textMsgHandler = null;
/*  191 */           } else if (this.opCode == 1)
/*      */           {
/*  193 */             this.textMessage = true;
/*  194 */             int size = this.wsSession.getMaxTextMessageBufferSize();
/*  195 */             if (size != this.messageBufferText.capacity()) {
/*  196 */               this.messageBufferText = CharBuffer.allocate(size);
/*      */             }
/*  198 */             this.binaryMsgHandler = null;
/*  199 */             this.textMsgHandler = this.wsSession.getTextMessageHandler();
/*      */           }
/*      */           else
/*      */           {
/*  203 */             throw new WsIOException(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, sm.getString("wsFrame.invalidOpCode", new Object[] {Integer.valueOf(this.opCode) })));
/*      */           }
/*      */           
/*      */         }
/*      */         catch (IllegalStateException ise)
/*      */         {
/*  209 */           throw new WsIOException(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, sm.getString("wsFrame.sessionClosed")));
/*      */         }
/*      */       }
/*  212 */       this.continuationExpected = (!this.fin);
/*      */     }
/*  214 */     b = this.inputBuffer.get();
/*      */     
/*  216 */     if (((b & 0x80) == 0) && (isMasked()))
/*      */     {
/*      */ 
/*  219 */       throw new WsIOException(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, sm.getString("wsFrame.notMasked")));
/*      */     }
/*  221 */     this.payloadLength = (b & 0x7F);
/*  222 */     this.state = State.PARTIAL_HEADER;
/*  223 */     if (getLog().isDebugEnabled()) {
/*  224 */       getLog().debug(sm.getString("wsFrame.partialHeaderComplete", new Object[] { Boolean.toString(this.fin), 
/*  225 */         Integer.toString(this.rsv), Integer.toString(this.opCode), Long.toString(this.payloadLength) }));
/*      */     }
/*  227 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   protected abstract boolean isMasked();
/*      */   
/*      */ 
/*      */   protected abstract Log getLog();
/*      */   
/*      */ 
/*      */   private boolean processRemainingHeader()
/*      */     throws IOException
/*      */   {
/*      */     int headerLength;
/*      */     int headerLength;
/*  242 */     if (isMasked()) {
/*  243 */       headerLength = 4;
/*      */     } else {
/*  245 */       headerLength = 0;
/*      */     }
/*      */     
/*  248 */     if (this.payloadLength == 126L) {
/*  249 */       headerLength += 2;
/*  250 */     } else if (this.payloadLength == 127L) {
/*  251 */       headerLength += 8;
/*      */     }
/*  253 */     if (this.inputBuffer.remaining() < headerLength) {
/*  254 */       return false;
/*      */     }
/*      */     
/*  257 */     if (this.payloadLength == 126L) {
/*  258 */       this.payloadLength = byteArrayToLong(this.inputBuffer.array(), this.inputBuffer
/*  259 */         .arrayOffset() + this.inputBuffer.position(), 2);
/*  260 */       this.inputBuffer.position(this.inputBuffer.position() + 2);
/*  261 */     } else if (this.payloadLength == 127L) {
/*  262 */       this.payloadLength = byteArrayToLong(this.inputBuffer.array(), this.inputBuffer
/*  263 */         .arrayOffset() + this.inputBuffer.position(), 8);
/*  264 */       this.inputBuffer.position(this.inputBuffer.position() + 8);
/*      */     }
/*  266 */     if (Util.isControl(this.opCode)) {
/*  267 */       if (this.payloadLength > 125L)
/*      */       {
/*      */ 
/*  270 */         throw new WsIOException(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, sm.getString("wsFrame.controlPayloadTooBig", new Object[] {Long.valueOf(this.payloadLength) })));
/*      */       }
/*  272 */       if (!this.fin)
/*      */       {
/*      */ 
/*  275 */         throw new WsIOException(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, sm.getString("wsFrame.controlNoFin")));
/*      */       }
/*      */     }
/*  278 */     if (isMasked()) {
/*  279 */       this.inputBuffer.get(this.mask, 0, 4);
/*      */     }
/*  281 */     this.state = State.DATA;
/*  282 */     return true;
/*      */   }
/*      */   
/*      */   private boolean processData() throws IOException {
/*      */     boolean result;
/*      */     boolean result;
/*  288 */     if (Util.isControl(this.opCode)) {
/*  289 */       result = processDataControl(); } else { boolean result;
/*  290 */       if (this.textMessage) { boolean result;
/*  291 */         if (this.textMsgHandler == null) {
/*  292 */           result = swallowInput();
/*      */         } else
/*  294 */           result = processDataText();
/*      */       } else {
/*      */         boolean result;
/*  297 */         if (this.binaryMsgHandler == null) {
/*  298 */           result = swallowInput();
/*      */         } else
/*  300 */           result = processDataBinary();
/*      */       }
/*      */     }
/*  303 */     checkRoomPayload();
/*  304 */     return result;
/*      */   }
/*      */   
/*      */   private boolean processDataControl() throws IOException
/*      */   {
/*  309 */     TransformationResult tr = this.transformation.getMoreData(this.opCode, this.fin, this.rsv, this.controlBufferBinary);
/*  310 */     if (TransformationResult.UNDERFLOW.equals(tr)) {
/*  311 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  316 */     this.controlBufferBinary.flip();
/*  317 */     if (this.opCode == 8) {
/*  318 */       this.open = false;
/*  319 */       String reason = null;
/*  320 */       int code = CloseReason.CloseCodes.NORMAL_CLOSURE.getCode();
/*  321 */       if (this.controlBufferBinary.remaining() == 1) {
/*  322 */         this.controlBufferBinary.clear();
/*      */         
/*      */ 
/*      */ 
/*  326 */         throw new WsIOException(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, sm.getString("wsFrame.oneByteCloseCode")));
/*      */       }
/*  328 */       if (this.controlBufferBinary.remaining() > 1) {
/*  329 */         code = this.controlBufferBinary.getShort();
/*  330 */         if (this.controlBufferBinary.remaining() > 0) {
/*  331 */           CoderResult cr = this.utf8DecoderControl.decode(this.controlBufferBinary, this.controlBufferText, true);
/*      */           
/*  333 */           if (cr.isError()) {
/*  334 */             this.controlBufferBinary.clear();
/*  335 */             this.controlBufferText.clear();
/*      */             
/*      */ 
/*  338 */             throw new WsIOException(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, sm.getString("wsFrame.invalidUtf8Close")));
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*  343 */           this.controlBufferText.flip();
/*  344 */           reason = this.controlBufferText.toString();
/*      */         }
/*      */       }
/*  347 */       this.wsSession.onClose(new CloseReason(Util.getCloseCode(code), reason));
/*  348 */     } else if (this.opCode == 9) {
/*  349 */       if (this.wsSession.isOpen()) {
/*  350 */         this.wsSession.getBasicRemote().sendPong(this.controlBufferBinary);
/*      */       }
/*  352 */     } else if (this.opCode == 10) {
/*  353 */       MessageHandler.Whole<PongMessage> mhPong = this.wsSession.getPongMessageHandler();
/*  354 */       if (mhPong != null) {
/*      */         try {
/*  356 */           mhPong.onMessage(new WsPongMessage(this.controlBufferBinary));
/*      */         } catch (Throwable t) {
/*  358 */           handleThrowableOnSend(t);
/*      */         } finally {
/*  360 */           this.controlBufferBinary.clear();
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*  365 */       this.controlBufferBinary.clear();
/*      */       
/*      */ 
/*  368 */       throw new WsIOException(new CloseReason(CloseReason.CloseCodes.PROTOCOL_ERROR, sm.getString("wsFrame.invalidOpCode", new Object[] {Integer.valueOf(this.opCode) })));
/*      */     }
/*  370 */     this.controlBufferBinary.clear();
/*  371 */     newFrame();
/*  372 */     return true;
/*      */   }
/*      */   
/*      */   protected void sendMessageText(boolean last)
/*      */     throws WsIOException
/*      */   {
/*  378 */     if ((this.textMsgHandler instanceof WrappedMessageHandler)) {
/*  379 */       long maxMessageSize = ((WrappedMessageHandler)this.textMsgHandler).getMaxMessageSize();
/*  380 */       if ((maxMessageSize > -1L) && (this.messageBufferText.remaining() > maxMessageSize))
/*      */       {
/*  382 */         throw new WsIOException(new CloseReason(CloseReason.CloseCodes.TOO_BIG, sm.getString("wsFrame.messageTooBig", new Object[] {
/*  383 */           Long.valueOf(this.messageBufferText.remaining()), 
/*  384 */           Long.valueOf(maxMessageSize) })));
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/*  389 */       if ((this.textMsgHandler instanceof MessageHandler.Partial))
/*      */       {
/*  391 */         ((MessageHandler.Partial)this.textMsgHandler).onMessage(this.messageBufferText.toString(), last);
/*      */       }
/*      */       else
/*      */       {
/*  395 */         ((MessageHandler.Whole)this.textMsgHandler).onMessage(this.messageBufferText.toString());
/*      */       }
/*      */     } catch (Throwable t) {
/*  398 */       handleThrowableOnSend(t);
/*      */     } finally {
/*  400 */       this.messageBufferText.clear();
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean processDataText()
/*      */     throws IOException
/*      */   {
/*  407 */     TransformationResult tr = this.transformation.getMoreData(this.opCode, this.fin, this.rsv, this.messageBufferBinary);
/*  408 */     while (!TransformationResult.END_OF_FRAME.equals(tr))
/*      */     {
/*      */ 
/*  411 */       this.messageBufferBinary.flip();
/*      */       for (;;) {
/*  413 */         CoderResult cr = this.utf8DecoderMessage.decode(this.messageBufferBinary, this.messageBufferText, false);
/*      */         
/*  415 */         if (cr.isError())
/*      */         {
/*      */ 
/*  418 */           throw new WsIOException(new CloseReason(CloseReason.CloseCodes.NOT_CONSISTENT, sm.getString("wsFrame.invalidUtf8"))); }
/*  419 */         if (cr.isOverflow())
/*      */         {
/*  421 */           if (usePartial()) {
/*  422 */             this.messageBufferText.flip();
/*  423 */             sendMessageText(false);
/*  424 */             this.messageBufferText.clear();
/*      */           }
/*      */           else
/*      */           {
/*  428 */             throw new WsIOException(new CloseReason(CloseReason.CloseCodes.TOO_BIG, sm.getString("wsFrame.textMessageTooBig")));
/*      */           }
/*  430 */         } else if (cr.isUnderflow())
/*      */         {
/*  432 */           this.messageBufferBinary.compact();
/*      */           
/*      */ 
/*      */ 
/*  436 */           if (TransformationResult.OVERFLOW.equals(tr)) {
/*      */             break;
/*      */           }
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  443 */           return false;
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  448 */       tr = this.transformation.getMoreData(this.opCode, this.fin, this.rsv, this.messageBufferBinary);
/*      */     }
/*      */     
/*  451 */     this.messageBufferBinary.flip();
/*  452 */     boolean last = false;
/*      */     
/*      */     for (;;)
/*      */     {
/*  456 */       CoderResult cr = this.utf8DecoderMessage.decode(this.messageBufferBinary, this.messageBufferText, last);
/*      */       
/*  458 */       if (cr.isError())
/*      */       {
/*      */ 
/*  461 */         throw new WsIOException(new CloseReason(CloseReason.CloseCodes.NOT_CONSISTENT, sm.getString("wsFrame.invalidUtf8"))); }
/*  462 */       if (cr.isOverflow())
/*      */       {
/*  464 */         if (usePartial()) {
/*  465 */           this.messageBufferText.flip();
/*  466 */           sendMessageText(false);
/*  467 */           this.messageBufferText.clear();
/*      */         }
/*      */         else
/*      */         {
/*  471 */           throw new WsIOException(new CloseReason(CloseReason.CloseCodes.TOO_BIG, sm.getString("wsFrame.textMessageTooBig")));
/*      */         }
/*  473 */       } else if ((cr.isUnderflow()) && (!last))
/*      */       {
/*      */ 
/*  476 */         if (this.continuationExpected)
/*      */         {
/*      */ 
/*  479 */           if (usePartial()) {
/*  480 */             this.messageBufferText.flip();
/*  481 */             sendMessageText(false);
/*  482 */             this.messageBufferText.clear();
/*      */           }
/*  484 */           this.messageBufferBinary.compact();
/*  485 */           newFrame();
/*      */           
/*  487 */           return true;
/*      */         }
/*      */         
/*  490 */         last = true;
/*      */       }
/*      */       else
/*      */       {
/*  494 */         this.messageBufferText.flip();
/*  495 */         sendMessageText(true);
/*      */         
/*  497 */         newMessage();
/*  498 */         return true;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private boolean processDataBinary()
/*      */     throws IOException
/*      */   {
/*  506 */     TransformationResult tr = this.transformation.getMoreData(this.opCode, this.fin, this.rsv, this.messageBufferBinary);
/*  507 */     while (!TransformationResult.END_OF_FRAME.equals(tr))
/*      */     {
/*  509 */       if (TransformationResult.UNDERFLOW.equals(tr))
/*      */       {
/*  511 */         return false;
/*      */       }
/*      */       
/*      */ 
/*  515 */       if (!usePartial())
/*      */       {
/*  517 */         CloseReason cr = new CloseReason(CloseReason.CloseCodes.TOO_BIG, sm.getString("wsFrame.bufferTooSmall", new Object[] {
/*  518 */           Integer.valueOf(this.messageBufferBinary.capacity()), 
/*  519 */           Long.valueOf(this.payloadLength) }));
/*  520 */         throw new WsIOException(cr);
/*      */       }
/*  522 */       this.messageBufferBinary.flip();
/*  523 */       ByteBuffer copy = ByteBuffer.allocate(this.messageBufferBinary.limit());
/*  524 */       copy.put(this.messageBufferBinary);
/*  525 */       copy.flip();
/*  526 */       sendMessageBinary(copy, false);
/*  527 */       this.messageBufferBinary.clear();
/*      */       
/*  529 */       tr = this.transformation.getMoreData(this.opCode, this.fin, this.rsv, this.messageBufferBinary);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  536 */     if ((usePartial()) || (!this.continuationExpected)) {
/*  537 */       this.messageBufferBinary.flip();
/*  538 */       ByteBuffer copy = ByteBuffer.allocate(this.messageBufferBinary.limit());
/*  539 */       copy.put(this.messageBufferBinary);
/*  540 */       copy.flip();
/*  541 */       sendMessageBinary(copy, !this.continuationExpected);
/*  542 */       this.messageBufferBinary.clear();
/*      */     }
/*      */     
/*  545 */     if (this.continuationExpected)
/*      */     {
/*  547 */       newFrame();
/*      */     }
/*      */     else {
/*  550 */       newMessage();
/*      */     }
/*      */     
/*  553 */     return true;
/*      */   }
/*      */   
/*      */   private void handleThrowableOnSend(Throwable t) throws WsIOException
/*      */   {
/*  558 */     ExceptionUtils.handleThrowable(t);
/*  559 */     this.wsSession.getLocal().onError(this.wsSession, t);
/*      */     
/*  561 */     CloseReason cr = new CloseReason(CloseReason.CloseCodes.CLOSED_ABNORMALLY, sm.getString("wsFrame.ioeTriggeredClose"));
/*  562 */     throw new WsIOException(cr);
/*      */   }
/*      */   
/*      */   protected void sendMessageBinary(ByteBuffer msg, boolean last)
/*      */     throws WsIOException
/*      */   {
/*  568 */     if ((this.binaryMsgHandler instanceof WrappedMessageHandler)) {
/*  569 */       long maxMessageSize = ((WrappedMessageHandler)this.binaryMsgHandler).getMaxMessageSize();
/*  570 */       if ((maxMessageSize > -1L) && (msg.remaining() > maxMessageSize))
/*      */       {
/*  572 */         throw new WsIOException(new CloseReason(CloseReason.CloseCodes.TOO_BIG, sm.getString("wsFrame.messageTooBig", new Object[] {
/*  573 */           Long.valueOf(msg.remaining()), 
/*  574 */           Long.valueOf(maxMessageSize) })));
/*      */       }
/*      */     }
/*      */     try {
/*  578 */       if ((this.binaryMsgHandler instanceof MessageHandler.Partial)) {
/*  579 */         ((MessageHandler.Partial)this.binaryMsgHandler).onMessage(msg, last);
/*      */       }
/*      */       else {
/*  582 */         ((MessageHandler.Whole)this.binaryMsgHandler).onMessage(msg);
/*      */       }
/*      */     } catch (Throwable t) {
/*  585 */       handleThrowableOnSend(t);
/*      */     }
/*      */   }
/*      */   
/*      */   private void newMessage()
/*      */   {
/*  591 */     this.messageBufferBinary.clear();
/*  592 */     this.messageBufferText.clear();
/*  593 */     this.utf8DecoderMessage.reset();
/*  594 */     this.continuationExpected = false;
/*  595 */     newFrame();
/*      */   }
/*      */   
/*      */   private void newFrame()
/*      */   {
/*  600 */     if (this.inputBuffer.remaining() == 0) {
/*  601 */       this.inputBuffer.position(0).limit(0);
/*      */     }
/*      */     
/*  604 */     this.maskIndex = 0;
/*  605 */     this.payloadWritten = 0L;
/*  606 */     this.state = State.NEW_FRAME;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  611 */     checkRoomHeaders();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void checkRoomHeaders()
/*      */   {
/*  618 */     if (this.inputBuffer.capacity() - this.inputBuffer.position() < 131)
/*      */     {
/*  620 */       makeRoom();
/*      */     }
/*      */   }
/*      */   
/*      */   private void checkRoomPayload()
/*      */   {
/*  626 */     if (this.inputBuffer.capacity() - this.inputBuffer.position() - this.payloadLength + this.payloadWritten < 0L) {
/*  627 */       makeRoom();
/*      */     }
/*      */   }
/*      */   
/*      */   private void makeRoom()
/*      */   {
/*  633 */     this.inputBuffer.compact();
/*  634 */     this.inputBuffer.flip();
/*      */   }
/*      */   
/*      */   private boolean usePartial()
/*      */   {
/*  639 */     if (Util.isControl(this.opCode))
/*  640 */       return false;
/*  641 */     if (this.textMessage) {
/*  642 */       return this.textMsgHandler instanceof MessageHandler.Partial;
/*      */     }
/*      */     
/*  645 */     return this.binaryMsgHandler instanceof MessageHandler.Partial;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean swallowInput()
/*      */   {
/*  651 */     long toSkip = Math.min(this.payloadLength - this.payloadWritten, this.inputBuffer.remaining());
/*  652 */     this.inputBuffer.position(this.inputBuffer.position() + (int)toSkip);
/*  653 */     this.payloadWritten += toSkip;
/*  654 */     if (this.payloadWritten == this.payloadLength) {
/*  655 */       if (this.continuationExpected) {
/*  656 */         newFrame();
/*      */       } else {
/*  658 */         newMessage();
/*      */       }
/*  660 */       return true;
/*      */     }
/*  662 */     return false;
/*      */   }
/*      */   
/*      */   protected static long byteArrayToLong(byte[] b, int start, int len)
/*      */     throws IOException
/*      */   {
/*  668 */     if (len > 8) {
/*  669 */       throw new IOException(sm.getString("wsFrame.byteToLongFail", new Object[] { Long.valueOf(len) }));
/*      */     }
/*  671 */     int shift = 0;
/*  672 */     long result = 0L;
/*  673 */     for (int i = start + len - 1; i >= start; i--) {
/*  674 */       result += ((b[i] & 0xFF) << shift);
/*  675 */       shift += 8;
/*      */     }
/*  677 */     return result;
/*      */   }
/*      */   
/*      */   protected boolean isOpen()
/*      */   {
/*  682 */     return this.open;
/*      */   }
/*      */   
/*      */   protected Transformation getTransformation()
/*      */   {
/*  687 */     return this.transformation;
/*      */   }
/*      */   
/*      */   private static enum State
/*      */   {
/*  692 */     NEW_FRAME,  PARTIAL_HEADER,  DATA;
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
/*      */     private State() {}
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
/*      */   protected static enum ReadState
/*      */   {
/*  753 */     WAITING(false), 
/*  754 */     PROCESSING(false), 
/*  755 */     SUSPENDING_WAIT(true), 
/*  756 */     SUSPENDING_PROCESS(true), 
/*  757 */     SUSPENDED(true), 
/*  758 */     CLOSING(false);
/*      */     
/*      */     private final boolean isSuspended;
/*      */     
/*      */     private ReadState(boolean isSuspended) {
/*  763 */       this.isSuspended = isSuspended;
/*      */     }
/*      */     
/*      */ 
/*  767 */     public boolean isSuspended() { return this.isSuspended; }
/*      */   }
/*      */   
/*      */   public void suspend() {
/*      */     do {
/*      */       do {
/*  773 */         do { do { do { switch (this.readState) {
/*      */               }
/*  775 */             } while (!READ_STATE_UPDATER.compareAndSet(this, ReadState.WAITING, ReadState.SUSPENDING_WAIT));
/*      */             
/*      */ 
/*      */ 
/*  779 */             return;
/*      */           }
/*  781 */           while (!READ_STATE_UPDATER.compareAndSet(this, ReadState.PROCESSING, ReadState.SUSPENDING_PROCESS));
/*      */           
/*      */ 
/*      */ 
/*  785 */           return;
/*      */         }
/*  787 */         while (this.readState != ReadState.SUSPENDING_WAIT);
/*      */         
/*      */ 
/*  790 */         if (getLog().isWarnEnabled()) {
/*  791 */           getLog().warn(sm.getString("wsFrame.suspendRequested"));
/*      */         }
/*      */         
/*  794 */         return;
/*      */       }
/*  796 */       while (this.readState != ReadState.SUSPENDING_PROCESS);
/*      */       
/*      */ 
/*  799 */       if (getLog().isWarnEnabled()) {
/*  800 */         getLog().warn(sm.getString("wsFrame.suspendRequested"));
/*      */       }
/*      */       
/*  803 */       return;
/*      */     }
/*  805 */     while (this.readState != ReadState.SUSPENDED);
/*      */     
/*      */ 
/*  808 */     if (getLog().isWarnEnabled()) {
/*  809 */       getLog().warn(sm.getString("wsFrame.alreadySuspended"));
/*      */     }
/*      */     
/*  812 */     return;
/*      */     
/*  814 */     return;
/*      */     
/*  816 */     throw new IllegalStateException(sm.getString("wsFrame.illegalReadState", new Object[] { this.state }));
/*      */   }
/*      */   
/*      */   public void resume() {
/*      */     do {
/*      */       do {
/*      */         do {
/*  823 */           do { do { switch (this.readState) {
/*      */               }
/*  825 */             } while (this.readState != ReadState.WAITING);
/*      */             
/*      */ 
/*  828 */             if (getLog().isWarnEnabled()) {
/*  829 */               getLog().warn(sm.getString("wsFrame.alreadyResumed"));
/*      */             }
/*      */             
/*  832 */             return;
/*      */           }
/*  834 */           while (this.readState != ReadState.PROCESSING);
/*      */           
/*      */ 
/*  837 */           if (getLog().isWarnEnabled()) {
/*  838 */             getLog().warn(sm.getString("wsFrame.alreadyResumed"));
/*      */           }
/*      */           
/*  841 */           return;
/*      */         }
/*  843 */         while (!READ_STATE_UPDATER.compareAndSet(this, ReadState.SUSPENDING_WAIT, ReadState.WAITING));
/*      */         
/*      */ 
/*      */ 
/*  847 */         return;
/*      */       }
/*  849 */       while (!READ_STATE_UPDATER.compareAndSet(this, ReadState.SUSPENDING_PROCESS, ReadState.PROCESSING));
/*      */       
/*      */ 
/*      */ 
/*  853 */       return;
/*      */     }
/*  855 */     while (!READ_STATE_UPDATER.compareAndSet(this, ReadState.SUSPENDED, ReadState.WAITING));
/*      */     
/*      */ 
/*      */ 
/*  859 */     resumeProcessing();
/*  860 */     return;
/*      */     
/*  862 */     return;
/*      */     
/*  864 */     throw new IllegalStateException(sm.getString("wsFrame.illegalReadState", new Object[] { this.state }));
/*      */   }
/*      */   
/*      */ 
/*      */   protected boolean isSuspended()
/*      */   {
/*  870 */     return this.readState.isSuspended();
/*      */   }
/*      */   
/*      */   protected ReadState getReadState() {
/*  874 */     return this.readState;
/*      */   }
/*      */   
/*      */   protected void changeReadState(ReadState newState) {
/*  878 */     READ_STATE_UPDATER.set(this, newState);
/*      */   }
/*      */   
/*      */   protected boolean changeReadState(ReadState oldState, ReadState newState) {
/*  882 */     return READ_STATE_UPDATER.compareAndSet(this, oldState, newState);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected abstract void resumeProcessing();
/*      */   
/*      */ 
/*      */ 
/*      */   private abstract class TerminalTransformation
/*      */     implements Transformation
/*      */   {
/*      */     private TerminalTransformation() {}
/*      */     
/*      */ 
/*      */ 
/*      */     public boolean validateRsvBits(int i)
/*      */     {
/*  901 */       return true;
/*      */     }
/*      */     
/*      */ 
/*      */     public Extension getExtensionResponse()
/*      */     {
/*  907 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setNext(Transformation t) {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean validateRsv(int rsv, byte opCode)
/*      */     {
/*  922 */       return rsv == 0;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public void close() {}
/*      */   }
/*      */   
/*      */ 
/*      */   private final class NoopTransformation
/*      */     extends WsFrameBase.TerminalTransformation
/*      */   {
/*      */     private NoopTransformation()
/*      */     {
/*  936 */       super(null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public TransformationResult getMoreData(byte opCode, boolean fin, int rsv, ByteBuffer dest)
/*      */     {
/*  944 */       long toWrite = Math.min(WsFrameBase.this.payloadLength - WsFrameBase.this.payloadWritten, WsFrameBase.this.inputBuffer.remaining());
/*  945 */       toWrite = Math.min(toWrite, dest.remaining());
/*      */       
/*  947 */       int orgLimit = WsFrameBase.this.inputBuffer.limit();
/*  948 */       WsFrameBase.this.inputBuffer.limit(WsFrameBase.this.inputBuffer.position() + (int)toWrite);
/*  949 */       dest.put(WsFrameBase.this.inputBuffer);
/*  950 */       WsFrameBase.this.inputBuffer.limit(orgLimit);
/*  951 */       WsFrameBase.this.payloadWritten = (WsFrameBase.this.payloadWritten + toWrite);
/*      */       
/*  953 */       if (WsFrameBase.this.payloadWritten == WsFrameBase.this.payloadLength)
/*  954 */         return TransformationResult.END_OF_FRAME;
/*  955 */       if (WsFrameBase.this.inputBuffer.remaining() == 0) {
/*  956 */         return TransformationResult.UNDERFLOW;
/*      */       }
/*      */       
/*  959 */       return TransformationResult.OVERFLOW;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public List<MessagePart> sendMessagePart(List<MessagePart> messageParts)
/*      */     {
/*  968 */       return messageParts;
/*      */     }
/*      */   }
/*      */   
/*      */   private final class UnmaskTransformation
/*      */     extends WsFrameBase.TerminalTransformation
/*      */   {
/*      */     private UnmaskTransformation()
/*      */     {
/*  977 */       super(null);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public TransformationResult getMoreData(byte opCode, boolean fin, int rsv, ByteBuffer dest)
/*      */     {
/*  985 */       while ((WsFrameBase.this.payloadWritten < WsFrameBase.this.payloadLength) && (WsFrameBase.this.inputBuffer.remaining() > 0) && 
/*  986 */         (dest.hasRemaining())) {
/*  987 */         byte b = (byte)((WsFrameBase.this.inputBuffer.get() ^ WsFrameBase.this.mask[WsFrameBase.this.maskIndex]) & 0xFF);
/*  988 */         WsFrameBase.access$608(WsFrameBase.this);
/*  989 */         if (WsFrameBase.this.maskIndex == 4) {
/*  990 */           WsFrameBase.this.maskIndex = 0;
/*      */         }
/*  992 */         WsFrameBase.access$408(WsFrameBase.this);
/*  993 */         dest.put(b);
/*      */       }
/*  995 */       if (WsFrameBase.this.payloadWritten == WsFrameBase.this.payloadLength)
/*  996 */         return TransformationResult.END_OF_FRAME;
/*  997 */       if (WsFrameBase.this.inputBuffer.remaining() == 0) {
/*  998 */         return TransformationResult.UNDERFLOW;
/*      */       }
/*      */       
/* 1001 */       return TransformationResult.OVERFLOW;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public List<MessagePart> sendMessagePart(List<MessagePart> messageParts)
/*      */     {
/* 1008 */       return messageParts;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\WsFrameBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */