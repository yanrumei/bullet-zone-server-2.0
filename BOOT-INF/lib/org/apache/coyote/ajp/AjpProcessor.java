/*      */ package org.apache.coyote.ajp;
/*      */ 
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.EOFException;
/*      */ import java.io.IOException;
/*      */ import java.io.InterruptedIOException;
/*      */ import java.net.InetAddress;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.security.NoSuchProviderException;
/*      */ import java.security.cert.CertificateException;
/*      */ import java.security.cert.CertificateFactory;
/*      */ import java.security.cert.X509Certificate;
/*      */ import org.apache.coyote.AbstractProcessor;
/*      */ import org.apache.coyote.ActionCode;
/*      */ import org.apache.coyote.Adapter;
/*      */ import org.apache.coyote.ErrorState;
/*      */ import org.apache.coyote.InputBuffer;
/*      */ import org.apache.coyote.OutputBuffer;
/*      */ import org.apache.coyote.Request;
/*      */ import org.apache.coyote.RequestInfo;
/*      */ import org.apache.coyote.Response;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.buf.ByteChunk;
/*      */ import org.apache.tomcat.util.buf.HexUtils;
/*      */ import org.apache.tomcat.util.buf.MessageBytes;
/*      */ import org.apache.tomcat.util.http.HttpMessages;
/*      */ import org.apache.tomcat.util.http.MimeHeaders;
/*      */ import org.apache.tomcat.util.net.AbstractEndpoint;
/*      */ import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
/*      */ import org.apache.tomcat.util.net.ApplicationBufferHandler;
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
/*      */ public class AjpProcessor
/*      */   extends AbstractProcessor
/*      */ {
/*   57 */   private static final Log log = LogFactory.getLog(AjpProcessor.class);
/*      */   
/*      */ 
/*      */ 
/*   61 */   private static final StringManager sm = StringManager.getManager(AjpProcessor.class);
/*      */   
/*      */ 
/*      */   private static final byte[] endMessageArray;
/*      */   
/*      */ 
/*      */   private static final byte[] endAndCloseMessageArray;
/*      */   
/*      */ 
/*      */   private static final byte[] flushMessageArray;
/*      */   
/*      */   private static final byte[] pongMessageArray;
/*      */   
/*      */   private final byte[] getBodyMessageArray;
/*      */   
/*      */   private final int outputMaxChunkSize;
/*      */   
/*      */   private final AjpMessage requestHeaderMessage;
/*      */   
/*      */   private final AjpMessage responseMessage;
/*      */   
/*      */ 
/*      */   static
/*      */   {
/*   85 */     AjpMessage endMessage = new AjpMessage(16);
/*   86 */     endMessage.reset();
/*   87 */     endMessage.appendByte(5);
/*   88 */     endMessage.appendByte(1);
/*   89 */     endMessage.end();
/*   90 */     endMessageArray = new byte[endMessage.getLen()];
/*   91 */     System.arraycopy(endMessage.getBuffer(), 0, endMessageArray, 0, endMessage
/*   92 */       .getLen());
/*      */     
/*      */ 
/*   95 */     AjpMessage endAndCloseMessage = new AjpMessage(16);
/*   96 */     endAndCloseMessage.reset();
/*   97 */     endAndCloseMessage.appendByte(5);
/*   98 */     endAndCloseMessage.appendByte(0);
/*   99 */     endAndCloseMessage.end();
/*  100 */     endAndCloseMessageArray = new byte[endAndCloseMessage.getLen()];
/*  101 */     System.arraycopy(endAndCloseMessage.getBuffer(), 0, endAndCloseMessageArray, 0, endAndCloseMessage
/*  102 */       .getLen());
/*      */     
/*      */ 
/*  105 */     AjpMessage flushMessage = new AjpMessage(16);
/*  106 */     flushMessage.reset();
/*  107 */     flushMessage.appendByte(3);
/*  108 */     flushMessage.appendInt(0);
/*  109 */     flushMessage.appendByte(0);
/*  110 */     flushMessage.end();
/*  111 */     flushMessageArray = new byte[flushMessage.getLen()];
/*  112 */     System.arraycopy(flushMessage.getBuffer(), 0, flushMessageArray, 0, flushMessage
/*  113 */       .getLen());
/*      */     
/*      */ 
/*  116 */     AjpMessage pongMessage = new AjpMessage(16);
/*  117 */     pongMessage.reset();
/*  118 */     pongMessage.appendByte(9);
/*  119 */     pongMessage.end();
/*  120 */     pongMessageArray = new byte[pongMessage.getLen()];
/*  121 */     System.arraycopy(pongMessage.getBuffer(), 0, pongMessageArray, 0, pongMessage
/*  122 */       .getLen());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  161 */   private int responseMsgPos = -1;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final AjpMessage bodyMessage;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  173 */   private final MessageBytes bodyBytes = MessageBytes.newInstance();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  179 */   private char[] hostNameC = new char[0];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  185 */   private final MessageBytes tmpMB = MessageBytes.newInstance();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  191 */   private final MessageBytes certificates = MessageBytes.newInstance();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  197 */   private boolean endOfStream = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  203 */   private boolean empty = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  209 */   private boolean first = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  216 */   private boolean waitingForBodyMessage = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  222 */   private boolean replay = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  228 */   private boolean swallowResponse = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  234 */   private boolean responseFinished = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  240 */   private long bytesWritten = 0L;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AjpProcessor(int packetSize, AbstractEndpoint<?> endpoint)
/*      */   {
/*  249 */     super(endpoint);
/*      */     
/*      */ 
/*      */ 
/*  253 */     this.outputMaxChunkSize = (8184 + packetSize - 8192);
/*      */     
/*      */ 
/*  256 */     this.request.setInputBuffer(new SocketInputBuffer());
/*      */     
/*  258 */     this.requestHeaderMessage = new AjpMessage(packetSize);
/*  259 */     this.responseMessage = new AjpMessage(packetSize);
/*  260 */     this.bodyMessage = new AjpMessage(packetSize);
/*      */     
/*      */ 
/*  263 */     AjpMessage getBodyMessage = new AjpMessage(16);
/*  264 */     getBodyMessage.reset();
/*  265 */     getBodyMessage.appendByte(6);
/*      */     
/*  267 */     getBodyMessage.appendInt(8186 + packetSize - 8192);
/*      */     
/*  269 */     getBodyMessage.end();
/*  270 */     this.getBodyMessageArray = new byte[getBodyMessage.getLen()];
/*  271 */     System.arraycopy(getBodyMessage.getBuffer(), 0, this.getBodyMessageArray, 0, getBodyMessage
/*  272 */       .getLen());
/*      */     
/*  274 */     this.response.setOutputBuffer(new SocketOutputBuffer());
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
/*  292 */   protected boolean ajpFlush = true;
/*  293 */   public boolean getAjpFlush() { return this.ajpFlush; }
/*      */   
/*  295 */   public void setAjpFlush(boolean ajpFlush) { this.ajpFlush = ajpFlush; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  304 */   private int keepAliveTimeout = -1;
/*  305 */   public int getKeepAliveTimeout() { return this.keepAliveTimeout; }
/*  306 */   public void setKeepAliveTimeout(int timeout) { this.keepAliveTimeout = timeout; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  312 */   private boolean tomcatAuthentication = true;
/*  313 */   public boolean getTomcatAuthentication() { return this.tomcatAuthentication; }
/*      */   
/*  315 */   public void setTomcatAuthentication(boolean tomcatAuthentication) { this.tomcatAuthentication = tomcatAuthentication; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  322 */   private boolean tomcatAuthorization = false;
/*  323 */   public boolean getTomcatAuthorization() { return this.tomcatAuthorization; }
/*      */   
/*  325 */   public void setTomcatAuthorization(boolean tomcatAuthorization) { this.tomcatAuthorization = tomcatAuthorization; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  332 */   private String requiredSecret = null;
/*      */   
/*  334 */   public void setRequiredSecret(String requiredSecret) { this.requiredSecret = requiredSecret; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  344 */   private String clientCertProvider = null;
/*  345 */   public String getClientCertProvider() { return this.clientCertProvider; }
/*      */   
/*  347 */   public void setClientCertProvider(String clientCertProvider) { this.clientCertProvider = clientCertProvider; }
/*      */   
/*      */   @Deprecated
/*  350 */   private boolean sendReasonPhrase = false;
/*      */   
/*      */   @Deprecated
/*      */   void setSendReasonPhrase(boolean sendReasonPhrase) {
/*  354 */     this.sendReasonPhrase = sendReasonPhrase;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected boolean flushBufferedWrite()
/*      */     throws IOException
/*      */   {
/*  362 */     if (hasDataToWrite()) {
/*  363 */       this.socketWrapper.flush(false);
/*  364 */       if (hasDataToWrite())
/*      */       {
/*      */ 
/*  367 */         this.response.checkRegisterForWrite();
/*  368 */         return true;
/*      */       }
/*      */     }
/*  371 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */   protected void dispatchNonBlockingRead()
/*      */   {
/*  377 */     if (available(true) > 0) {
/*  378 */       super.dispatchNonBlockingRead();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected AbstractEndpoint.Handler.SocketState dispatchEndRequest()
/*      */   {
/*  386 */     if (this.keepAliveTimeout > 0) {
/*  387 */       this.socketWrapper.setReadTimeout(this.keepAliveTimeout);
/*      */     }
/*  389 */     recycle();
/*  390 */     return AbstractEndpoint.Handler.SocketState.OPEN;
/*      */   }
/*      */   
/*      */ 
/*      */   public AbstractEndpoint.Handler.SocketState service(SocketWrapperBase<?> socket)
/*      */     throws IOException
/*      */   {
/*  397 */     RequestInfo rp = this.request.getRequestProcessor();
/*  398 */     rp.setStage(1);
/*      */     
/*      */ 
/*  401 */     this.socketWrapper = socket;
/*      */     
/*  403 */     int soTimeout = this.endpoint.getConnectionTimeout();
/*  404 */     boolean cping = false;
/*      */     
/*  406 */     boolean keptAlive = false;
/*      */     
/*  408 */     while ((!getErrorState().isError()) && (!this.endpoint.isPaused()))
/*      */     {
/*      */       try
/*      */       {
/*  412 */         if (!readMessage(this.requestHeaderMessage, !keptAlive)) {
/*      */           break;
/*      */         }
/*      */         
/*  416 */         if (this.keepAliveTimeout > 0) {
/*  417 */           this.socketWrapper.setReadTimeout(soTimeout);
/*      */         }
/*      */         
/*      */ 
/*  421 */         int type = this.requestHeaderMessage.getByte();
/*  422 */         if (type == 10) {
/*  423 */           if (this.endpoint.isPaused()) {
/*  424 */             recycle();
/*  425 */             break;
/*      */           }
/*  427 */           cping = true;
/*      */           try {
/*  429 */             this.socketWrapper.write(true, pongMessageArray, 0, pongMessageArray.length);
/*  430 */             this.socketWrapper.flush(true);
/*      */           } catch (IOException e) {
/*  432 */             setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
/*      */           }
/*  434 */           recycle();
/*  435 */           continue; }
/*  436 */         if (type != 2)
/*      */         {
/*      */ 
/*  439 */           if (getLog().isDebugEnabled()) {
/*  440 */             getLog().debug("Unexpected message: " + type);
/*      */           }
/*  442 */           setErrorState(ErrorState.CLOSE_CONNECTION_NOW, null);
/*  443 */           break;
/*      */         }
/*  445 */         keptAlive = true;
/*  446 */         this.request.setStartTime(System.currentTimeMillis());
/*      */       } catch (IOException e) {
/*  448 */         setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
/*  449 */         break;
/*      */       } catch (Throwable t) {
/*  451 */         ExceptionUtils.handleThrowable(t);
/*  452 */         getLog().debug(sm.getString("ajpprocessor.header.error"), t);
/*      */         
/*  454 */         this.response.setStatus(400);
/*  455 */         setErrorState(ErrorState.CLOSE_CLEAN, t);
/*  456 */         getAdapter().log(this.request, this.response, 0L);
/*      */       }
/*      */       
/*  459 */       if (!getErrorState().isError())
/*      */       {
/*  461 */         rp.setStage(2);
/*      */         try {
/*  463 */           prepareRequest();
/*      */         } catch (Throwable t) {
/*  465 */           ExceptionUtils.handleThrowable(t);
/*  466 */           getLog().debug(sm.getString("ajpprocessor.request.prepare"), t);
/*      */           
/*  468 */           this.response.setStatus(500);
/*  469 */           setErrorState(ErrorState.CLOSE_CLEAN, t);
/*  470 */           getAdapter().log(this.request, this.response, 0L);
/*      */         }
/*      */       }
/*      */       
/*  474 */       if ((!getErrorState().isError()) && (!cping) && (this.endpoint.isPaused()))
/*      */       {
/*  476 */         this.response.setStatus(503);
/*  477 */         setErrorState(ErrorState.CLOSE_CLEAN, null);
/*  478 */         getAdapter().log(this.request, this.response, 0L);
/*      */       }
/*  480 */       cping = false;
/*      */       
/*      */ 
/*  483 */       if (!getErrorState().isError()) {
/*      */         try {
/*  485 */           rp.setStage(3);
/*  486 */           getAdapter().service(this.request, this.response);
/*      */         } catch (InterruptedIOException e) {
/*  488 */           setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
/*      */         } catch (Throwable t) {
/*  490 */           ExceptionUtils.handleThrowable(t);
/*  491 */           getLog().error(sm.getString("ajpprocessor.request.process"), t);
/*      */           
/*  493 */           this.response.setStatus(500);
/*  494 */           setErrorState(ErrorState.CLOSE_CLEAN, t);
/*  495 */           getAdapter().log(this.request, this.response, 0L);
/*      */         }
/*      */       }
/*      */       
/*  499 */       if ((isAsync()) && (!getErrorState().isError())) {
/*      */         break;
/*      */       }
/*      */       
/*      */ 
/*  504 */       if ((!this.responseFinished) && (getErrorState().isIoAllowed())) {
/*      */         try {
/*  506 */           action(ActionCode.COMMIT, null);
/*  507 */           finishResponse();
/*      */         } catch (IOException ioe) {
/*  509 */           setErrorState(ErrorState.CLOSE_CONNECTION_NOW, ioe);
/*      */         } catch (Throwable t) {
/*  511 */           ExceptionUtils.handleThrowable(t);
/*  512 */           setErrorState(ErrorState.CLOSE_NOW, t);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  518 */       if (getErrorState().isError()) {
/*  519 */         this.response.setStatus(500);
/*      */       }
/*  521 */       this.request.updateCounters();
/*      */       
/*  523 */       rp.setStage(6);
/*      */       
/*  525 */       if (this.keepAliveTimeout > 0) {
/*  526 */         this.socketWrapper.setReadTimeout(this.keepAliveTimeout);
/*      */       }
/*      */       
/*  529 */       recycle();
/*      */     }
/*      */     
/*  532 */     rp.setStage(7);
/*      */     
/*  534 */     if ((getErrorState().isError()) || (this.endpoint.isPaused())) {
/*  535 */       return AbstractEndpoint.Handler.SocketState.CLOSED;
/*      */     }
/*  537 */     if (isAsync()) {
/*  538 */       return AbstractEndpoint.Handler.SocketState.LONG;
/*      */     }
/*  540 */     return AbstractEndpoint.Handler.SocketState.OPEN;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void recycle()
/*      */   {
/*  548 */     getAdapter().checkRecycled(this.request, this.response);
/*  549 */     super.recycle();
/*  550 */     this.request.recycle();
/*  551 */     this.response.recycle();
/*  552 */     this.first = true;
/*  553 */     this.endOfStream = false;
/*  554 */     this.waitingForBodyMessage = false;
/*  555 */     this.empty = true;
/*  556 */     this.replay = false;
/*  557 */     this.responseFinished = false;
/*  558 */     this.certificates.recycle();
/*  559 */     this.swallowResponse = false;
/*  560 */     this.bytesWritten = 0L;
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
/*      */   private boolean receive(boolean block)
/*      */     throws IOException
/*      */   {
/*  585 */     this.bodyMessage.reset();
/*      */     
/*  587 */     if (!readMessage(this.bodyMessage, block)) {
/*  588 */       return false;
/*      */     }
/*      */     
/*  591 */     this.waitingForBodyMessage = false;
/*      */     
/*      */ 
/*  594 */     if (this.bodyMessage.getLen() == 0)
/*      */     {
/*  596 */       return false;
/*      */     }
/*  598 */     int blen = this.bodyMessage.peekInt();
/*  599 */     if (blen == 0) {
/*  600 */       return false;
/*      */     }
/*      */     
/*  603 */     this.bodyMessage.getBodyBytes(this.bodyBytes);
/*  604 */     this.empty = false;
/*  605 */     return true;
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
/*      */   private boolean readMessage(AjpMessage message, boolean block)
/*      */     throws IOException
/*      */   {
/*  623 */     byte[] buf = message.getBuffer();
/*      */     
/*  625 */     if (!read(buf, 0, 4, block)) {
/*  626 */       return false;
/*      */     }
/*      */     
/*  629 */     int messageLength = message.processHeader(true);
/*  630 */     if (messageLength < 0)
/*      */     {
/*  632 */       throw new IOException(sm.getString("ajpmessage.invalidLength", new Object[] {
/*  633 */         Integer.valueOf(messageLength) }));
/*      */     }
/*  635 */     if (messageLength == 0)
/*      */     {
/*  637 */       return true;
/*      */     }
/*      */     
/*  640 */     if (messageLength > message.getBuffer().length)
/*      */     {
/*      */ 
/*  643 */       throw new IllegalArgumentException(sm.getString("ajpprocessor.header.tooLong", new Object[] {
/*      */       
/*  645 */         Integer.valueOf(messageLength), 
/*  646 */         Integer.valueOf(buf.length) }));
/*      */     }
/*  648 */     read(buf, 4, messageLength, true);
/*  649 */     return true;
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
/*      */   protected boolean refillReadBuffer(boolean block)
/*      */     throws IOException
/*      */   {
/*  665 */     if (this.replay) {
/*  666 */       this.endOfStream = true;
/*      */     }
/*  668 */     if (this.endOfStream) {
/*  669 */       return false;
/*      */     }
/*      */     
/*  672 */     if (this.first) {
/*  673 */       this.first = false;
/*  674 */       long contentLength = this.request.getContentLengthLong();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  680 */       if (contentLength > 0L) {
/*  681 */         this.waitingForBodyMessage = true;
/*  682 */       } else if (contentLength == 0L) {
/*  683 */         this.endOfStream = true;
/*  684 */         return false;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  689 */     if (!this.waitingForBodyMessage) {
/*  690 */       this.socketWrapper.write(true, this.getBodyMessageArray, 0, this.getBodyMessageArray.length);
/*  691 */       this.socketWrapper.flush(true);
/*  692 */       this.waitingForBodyMessage = true;
/*      */     }
/*      */     
/*  695 */     boolean moreData = receive(block);
/*  696 */     if ((!moreData) && (!this.waitingForBodyMessage)) {
/*  697 */       this.endOfStream = true;
/*      */     }
/*  699 */     return moreData;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void prepareRequest()
/*      */   {
/*  709 */     byte methodCode = this.requestHeaderMessage.getByte();
/*  710 */     if (methodCode != -1) {
/*  711 */       String methodName = Constants.getMethodForCode(methodCode - 1);
/*  712 */       this.request.method().setString(methodName);
/*      */     }
/*      */     
/*  715 */     this.requestHeaderMessage.getBytes(this.request.protocol());
/*  716 */     this.requestHeaderMessage.getBytes(this.request.requestURI());
/*      */     
/*  718 */     this.requestHeaderMessage.getBytes(this.request.remoteAddr());
/*  719 */     this.requestHeaderMessage.getBytes(this.request.remoteHost());
/*  720 */     this.requestHeaderMessage.getBytes(this.request.localName());
/*  721 */     this.request.setLocalPort(this.requestHeaderMessage.getInt());
/*      */     
/*  723 */     boolean isSSL = this.requestHeaderMessage.getByte() != 0;
/*  724 */     if (isSSL) {
/*  725 */       this.request.scheme().setString("https");
/*      */     }
/*      */     
/*      */ 
/*  729 */     MimeHeaders headers = this.request.getMimeHeaders();
/*      */     
/*      */ 
/*  732 */     headers.setLimit(this.endpoint.getMaxHeaderCount());
/*      */     
/*  734 */     boolean contentLengthSet = false;
/*  735 */     int hCount = this.requestHeaderMessage.getInt();
/*  736 */     for (int i = 0; i < hCount; i++) {
/*  737 */       String hName = null;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  742 */       int isc = this.requestHeaderMessage.peekInt();
/*  743 */       int hId = isc & 0xFF;
/*      */       
/*  745 */       MessageBytes vMB = null;
/*  746 */       isc &= 0xFF00;
/*  747 */       if (40960 == isc) {
/*  748 */         this.requestHeaderMessage.getInt();
/*  749 */         hName = Constants.getHeaderForCode(hId - 1);
/*  750 */         vMB = headers.addValue(hName);
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/*  758 */         hId = -1;
/*  759 */         this.requestHeaderMessage.getBytes(this.tmpMB);
/*  760 */         ByteChunk bc = this.tmpMB.getByteChunk();
/*  761 */         vMB = headers.addValue(bc.getBuffer(), bc
/*  762 */           .getStart(), bc.getLength());
/*      */       }
/*      */       
/*  765 */       this.requestHeaderMessage.getBytes(vMB);
/*      */       
/*  767 */       if ((hId == 8) || ((hId == -1) && 
/*  768 */         (this.tmpMB.equalsIgnoreCase("Content-Length")))) {
/*  769 */         long cl = vMB.getLong();
/*  770 */         if (contentLengthSet) {
/*  771 */           this.response.setStatus(400);
/*  772 */           setErrorState(ErrorState.CLOSE_CLEAN, null);
/*      */         } else {
/*  774 */           contentLengthSet = true;
/*      */           
/*  776 */           this.request.setContentLength(cl);
/*      */         }
/*  778 */       } else if ((hId == 7) || ((hId == -1) && 
/*  779 */         (this.tmpMB.equalsIgnoreCase("Content-Type"))))
/*      */       {
/*  781 */         ByteChunk bchunk = vMB.getByteChunk();
/*  782 */         this.request.contentType().setBytes(bchunk.getBytes(), bchunk
/*  783 */           .getOffset(), bchunk
/*  784 */           .getLength());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  789 */     boolean secret = false;
/*      */     byte attributeCode;
/*  791 */     while ((attributeCode = this.requestHeaderMessage.getByte()) != -1)
/*      */     {
/*      */ 
/*  794 */       switch (attributeCode)
/*      */       {
/*      */       case 10: 
/*  797 */         this.requestHeaderMessage.getBytes(this.tmpMB);
/*  798 */         String n = this.tmpMB.toString();
/*  799 */         this.requestHeaderMessage.getBytes(this.tmpMB);
/*  800 */         String v = this.tmpMB.toString();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  808 */         if (n.equals("AJP_LOCAL_ADDR")) {
/*  809 */           this.request.localAddr().setString(v);
/*  810 */         } else if (n.equals("AJP_REMOTE_PORT")) {
/*      */           try {
/*  812 */             this.request.setRemotePort(Integer.parseInt(v));
/*      */ 
/*      */           }
/*      */           catch (NumberFormatException localNumberFormatException) {}
/*  816 */         } else if (n.equals("AJP_SSL_PROTOCOL")) {
/*  817 */           this.request.setAttribute("org.apache.tomcat.util.net.secure_protocol_version", v);
/*      */         } else {
/*  819 */           this.request.setAttribute(n, v);
/*      */         }
/*  821 */         break;
/*      */       
/*      */       case 1: 
/*  824 */         this.requestHeaderMessage.getBytes(this.tmpMB);
/*      */         
/*  826 */         break;
/*      */       
/*      */       case 2: 
/*  829 */         this.requestHeaderMessage.getBytes(this.tmpMB);
/*      */         
/*  831 */         break;
/*      */       
/*      */       case 3: 
/*  834 */         if ((this.tomcatAuthorization) || (!this.tomcatAuthentication))
/*      */         {
/*  836 */           this.requestHeaderMessage.getBytes(this.request.getRemoteUser());
/*  837 */           this.request.setRemoteUserNeedsAuthorization(this.tomcatAuthorization);
/*      */         }
/*      */         else {
/*  840 */           this.requestHeaderMessage.getBytes(this.tmpMB);
/*      */         }
/*  842 */         break;
/*      */       
/*      */       case 4: 
/*  845 */         if (this.tomcatAuthentication)
/*      */         {
/*  847 */           this.requestHeaderMessage.getBytes(this.tmpMB);
/*      */         } else {
/*  849 */           this.requestHeaderMessage.getBytes(this.request.getAuthType());
/*      */         }
/*  851 */         break;
/*      */       
/*      */       case 5: 
/*  854 */         this.requestHeaderMessage.getBytes(this.request.queryString());
/*  855 */         break;
/*      */       
/*      */       case 6: 
/*  858 */         this.requestHeaderMessage.getBytes(this.tmpMB);
/*      */         
/*  860 */         break;
/*      */       
/*      */ 
/*      */       case 7: 
/*  864 */         this.requestHeaderMessage.getBytes(this.certificates);
/*  865 */         break;
/*      */       
/*      */       case 8: 
/*  868 */         this.requestHeaderMessage.getBytes(this.tmpMB);
/*  869 */         this.request.setAttribute("javax.servlet.request.cipher_suite", this.tmpMB
/*  870 */           .toString());
/*  871 */         break;
/*      */       
/*      */       case 9: 
/*  874 */         this.requestHeaderMessage.getBytes(this.tmpMB);
/*  875 */         this.request.setAttribute("javax.servlet.request.ssl_session_id", this.tmpMB
/*  876 */           .toString());
/*  877 */         break;
/*      */       
/*      */       case 11: 
/*  880 */         this.request.setAttribute("javax.servlet.request.key_size", 
/*  881 */           Integer.valueOf(this.requestHeaderMessage.getInt()));
/*  882 */         break;
/*      */       
/*      */       case 13: 
/*  885 */         this.requestHeaderMessage.getBytes(this.request.method());
/*  886 */         break;
/*      */       
/*      */       case 12: 
/*  889 */         this.requestHeaderMessage.getBytes(this.tmpMB);
/*  890 */         if (this.requiredSecret != null) {
/*  891 */           secret = true;
/*  892 */           if (!this.tmpMB.equals(this.requiredSecret)) {
/*  893 */             this.response.setStatus(403);
/*  894 */             setErrorState(ErrorState.CLOSE_CLEAN, null);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */         break;
/*      */       }
/*      */       
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  908 */     if ((this.requiredSecret != null) && (!secret)) {
/*  909 */       this.response.setStatus(403);
/*  910 */       setErrorState(ErrorState.CLOSE_CLEAN, null);
/*      */     }
/*      */     
/*      */ 
/*  914 */     ByteChunk uriBC = this.request.requestURI().getByteChunk();
/*  915 */     if (uriBC.startsWithIgnoreCase("http", 0))
/*      */     {
/*  917 */       int pos = uriBC.indexOf("://", 0, 3, 4);
/*  918 */       int uriBCStart = uriBC.getStart();
/*  919 */       int slashPos = -1;
/*  920 */       if (pos != -1) {
/*  921 */         byte[] uriB = uriBC.getBytes();
/*  922 */         slashPos = uriBC.indexOf('/', pos + 3);
/*  923 */         if (slashPos == -1) {
/*  924 */           slashPos = uriBC.getLength();
/*      */           
/*  926 */           this.request.requestURI()
/*  927 */             .setBytes(uriB, uriBCStart + pos + 1, 1);
/*      */         }
/*      */         else {
/*  930 */           this.request.requestURI().setBytes(uriB, uriBCStart + slashPos, uriBC
/*  931 */             .getLength() - slashPos);
/*      */         }
/*  933 */         MessageBytes hostMB = headers.setValue("host");
/*  934 */         hostMB.setBytes(uriB, uriBCStart + pos + 3, slashPos - pos - 3);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  940 */     MessageBytes valueMB = this.request.getMimeHeaders().getValue("host");
/*  941 */     parseHost(valueMB);
/*      */     
/*  943 */     if (getErrorState().isError()) {
/*  944 */       getAdapter().log(this.request, this.response, 0L);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void parseHost(MessageBytes valueMB)
/*      */   {
/*  954 */     if ((valueMB == null) || (valueMB.isNull()))
/*      */     {
/*  956 */       this.request.setServerPort(this.request.getLocalPort());
/*      */       try {
/*  958 */         this.request.serverName().duplicate(this.request.localName());
/*      */       } catch (IOException e) {
/*  960 */         this.response.setStatus(400);
/*  961 */         setErrorState(ErrorState.CLOSE_CLEAN, e);
/*      */       }
/*  963 */       return;
/*      */     }
/*      */     
/*  966 */     ByteChunk valueBC = valueMB.getByteChunk();
/*  967 */     byte[] valueB = valueBC.getBytes();
/*  968 */     int valueL = valueBC.getLength();
/*  969 */     int valueS = valueBC.getStart();
/*  970 */     int colonPos = -1;
/*  971 */     if (this.hostNameC.length < valueL) {
/*  972 */       this.hostNameC = new char[valueL];
/*      */     }
/*      */     
/*  975 */     boolean ipv6 = valueB[valueS] == 91;
/*  976 */     boolean bracketClosed = false;
/*  977 */     for (int i = 0; i < valueL; i++) {
/*  978 */       char b = (char)valueB[(i + valueS)];
/*  979 */       this.hostNameC[i] = b;
/*  980 */       if (b == ']') {
/*  981 */         bracketClosed = true;
/*  982 */       } else if ((b == ':') && (
/*  983 */         (!ipv6) || (bracketClosed))) {
/*  984 */         colonPos = i;
/*  985 */         break;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  990 */     if (colonPos < 0) {
/*  991 */       this.request.serverName().setChars(this.hostNameC, 0, valueL);
/*      */     }
/*      */     else {
/*  994 */       this.request.serverName().setChars(this.hostNameC, 0, colonPos);
/*      */       
/*  996 */       int port = 0;
/*  997 */       int mult = 1;
/*  998 */       for (int i = valueL - 1; i > colonPos; i--) {
/*  999 */         int charValue = HexUtils.getDec(valueB[(i + valueS)]);
/* 1000 */         if (charValue == -1)
/*      */         {
/*      */ 
/* 1003 */           this.response.setStatus(400);
/* 1004 */           setErrorState(ErrorState.CLOSE_CLEAN, null);
/* 1005 */           break;
/*      */         }
/* 1007 */         port += charValue * mult;
/* 1008 */         mult = 10 * mult;
/*      */       }
/* 1010 */       this.request.setServerPort(port);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void prepareResponse()
/*      */     throws IOException
/*      */   {
/* 1023 */     this.response.setCommitted(true);
/*      */     
/* 1025 */     this.tmpMB.recycle();
/* 1026 */     this.responseMsgPos = -1;
/* 1027 */     this.responseMessage.reset();
/* 1028 */     this.responseMessage.appendByte(4);
/*      */     
/*      */ 
/*      */ 
/* 1032 */     int statusCode = this.response.getStatus();
/* 1033 */     if ((statusCode < 200) || (statusCode == 204) || (statusCode == 205) || (statusCode == 304))
/*      */     {
/*      */ 
/* 1036 */       this.swallowResponse = true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1041 */     MessageBytes methodMB = this.request.method();
/* 1042 */     if (methodMB.equals("HEAD"))
/*      */     {
/* 1044 */       this.swallowResponse = true;
/*      */     }
/*      */     
/*      */ 
/* 1048 */     this.responseMessage.appendInt(statusCode);
/* 1049 */     if (this.sendReasonPhrase) {
/* 1050 */       String message = null;
/* 1051 */       if ((org.apache.coyote.Constants.USE_CUSTOM_STATUS_MSG_IN_HEADER) && 
/* 1052 */         (HttpMessages.isSafeInHttpHeader(this.response.getMessage()))) {
/* 1053 */         message = this.response.getMessage();
/*      */       }
/* 1055 */       if (message == null)
/*      */       {
/* 1057 */         message = HttpMessages.getInstance(this.response.getLocale()).getMessage(this.response.getStatus());
/*      */       }
/* 1059 */       if (message == null)
/*      */       {
/* 1061 */         message = Integer.toString(this.response.getStatus());
/*      */       }
/* 1063 */       this.tmpMB.setString(message);
/*      */     }
/*      */     else
/*      */     {
/* 1067 */       this.tmpMB.setString(Integer.toString(this.response.getStatus()));
/*      */     }
/* 1069 */     this.responseMessage.appendBytes(this.tmpMB);
/*      */     
/*      */ 
/* 1072 */     MimeHeaders headers = this.response.getMimeHeaders();
/* 1073 */     String contentType = this.response.getContentType();
/* 1074 */     if (contentType != null) {
/* 1075 */       headers.setValue("Content-Type").setString(contentType);
/*      */     }
/* 1077 */     String contentLanguage = this.response.getContentLanguage();
/* 1078 */     if (contentLanguage != null) {
/* 1079 */       headers.setValue("Content-Language").setString(contentLanguage);
/*      */     }
/* 1081 */     long contentLength = this.response.getContentLengthLong();
/* 1082 */     if (contentLength >= 0L) {
/* 1083 */       headers.setValue("Content-Length").setLong(contentLength);
/*      */     }
/*      */     
/*      */ 
/* 1087 */     int numHeaders = headers.size();
/* 1088 */     this.responseMessage.appendInt(numHeaders);
/* 1089 */     for (int i = 0; i < numHeaders; i++) {
/* 1090 */       MessageBytes hN = headers.getName(i);
/* 1091 */       int hC = Constants.getResponseAjpIndex(hN.toString());
/* 1092 */       if (hC > 0) {
/* 1093 */         this.responseMessage.appendInt(hC);
/*      */       }
/*      */       else {
/* 1096 */         this.responseMessage.appendBytes(hN);
/*      */       }
/* 1098 */       MessageBytes hV = headers.getValue(i);
/* 1099 */       this.responseMessage.appendBytes(hV);
/*      */     }
/*      */     
/*      */ 
/* 1103 */     this.responseMessage.end();
/* 1104 */     this.socketWrapper.write(true, this.responseMessage.getBuffer(), 0, this.responseMessage.getLen());
/* 1105 */     this.socketWrapper.flush(true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void flush()
/*      */     throws IOException
/*      */   {
/* 1117 */     if (!this.responseFinished) {
/* 1118 */       if (this.ajpFlush)
/*      */       {
/* 1120 */         this.socketWrapper.write(true, flushMessageArray, 0, flushMessageArray.length);
/*      */       }
/* 1122 */       this.socketWrapper.flush(true);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void finishResponse()
/*      */     throws IOException
/*      */   {
/* 1132 */     if (this.responseFinished) {
/* 1133 */       return;
/*      */     }
/* 1135 */     this.responseFinished = true;
/*      */     
/*      */ 
/* 1138 */     if ((this.waitingForBodyMessage) || ((this.first) && (this.request.getContentLengthLong() > 0L))) {
/* 1139 */       refillReadBuffer(true);
/*      */     }
/*      */     
/*      */ 
/* 1143 */     if (getErrorState().isError()) {
/* 1144 */       this.socketWrapper.write(true, endAndCloseMessageArray, 0, endAndCloseMessageArray.length);
/*      */     } else {
/* 1146 */       this.socketWrapper.write(true, endMessageArray, 0, endMessageArray.length);
/*      */     }
/* 1148 */     this.socketWrapper.flush(true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int available(boolean doRead)
/*      */   {
/* 1160 */     if (this.endOfStream) {
/* 1161 */       return 0;
/*      */     }
/* 1163 */     if ((this.empty) && (doRead)) {
/*      */       try {
/* 1165 */         refillReadBuffer(false);
/*      */ 
/*      */       }
/*      */       catch (IOException timeout)
/*      */       {
/* 1170 */         return 1;
/*      */       }
/*      */     }
/* 1173 */     if (this.empty) {
/* 1174 */       return 0;
/*      */     }
/* 1176 */     return this.bodyBytes.getByteChunk().getLength();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected final void setRequestBody(ByteChunk body)
/*      */   {
/* 1183 */     int length = body.getLength();
/* 1184 */     this.bodyBytes.setBytes(body.getBytes(), body.getStart(), length);
/* 1185 */     this.request.setContentLength(length);
/* 1186 */     this.first = false;
/* 1187 */     this.empty = false;
/* 1188 */     this.replay = true;
/* 1189 */     this.endOfStream = false;
/*      */   }
/*      */   
/*      */ 
/*      */   protected final void setSwallowResponse()
/*      */   {
/* 1195 */     this.swallowResponse = true;
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
/*      */   protected final boolean getPopulateRequestAttributesFromSocket()
/*      */   {
/* 1213 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected final void populateRequestAttributeRemoteHost()
/*      */   {
/* 1220 */     if (this.request.remoteHost().isNull()) {
/*      */       try {
/* 1222 */         this.request.remoteHost().setString(
/* 1223 */           InetAddress.getByName(this.request.remoteAddr().toString()).getHostName());
/*      */       }
/*      */       catch (IOException localIOException) {}
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected final void populateSslRequestAttributes()
/*      */   {
/* 1233 */     if (!this.certificates.isNull()) {
/* 1234 */       ByteChunk certData = this.certificates.getByteChunk();
/* 1235 */       X509Certificate[] jsseCerts = null;
/*      */       
/*      */ 
/*      */ 
/* 1239 */       ByteArrayInputStream bais = new ByteArrayInputStream(certData.getBytes(), certData.getStart(), certData.getLength());
/*      */       
/*      */       try
/*      */       {
/* 1243 */         String clientCertProvider = getClientCertProvider();
/* 1244 */         CertificateFactory cf; CertificateFactory cf; if (clientCertProvider == null) {
/* 1245 */           cf = CertificateFactory.getInstance("X.509");
/*      */         } else {
/* 1247 */           cf = CertificateFactory.getInstance("X.509", clientCertProvider);
/*      */         }
/*      */         
/* 1250 */         while (bais.available() > 0)
/*      */         {
/* 1252 */           X509Certificate cert = (X509Certificate)cf.generateCertificate(bais);
/* 1253 */           if (jsseCerts == null) {
/* 1254 */             jsseCerts = new X509Certificate[1];
/* 1255 */             jsseCerts[0] = cert;
/*      */           } else {
/* 1257 */             X509Certificate[] temp = new X509Certificate[jsseCerts.length + 1];
/* 1258 */             System.arraycopy(jsseCerts, 0, temp, 0, jsseCerts.length);
/* 1259 */             temp[jsseCerts.length] = cert;
/* 1260 */             jsseCerts = temp;
/*      */           }
/*      */         }
/*      */       } catch (CertificateException e) {
/* 1264 */         getLog().error(sm.getString("ajpprocessor.certs.fail"), e);
/* 1265 */         return;
/*      */       } catch (NoSuchProviderException e) {
/* 1267 */         getLog().error(sm.getString("ajpprocessor.certs.fail"), e);
/* 1268 */         return;
/*      */       }
/* 1270 */       this.request.setAttribute("javax.servlet.request.X509Certificate", jsseCerts);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected final boolean isRequestBodyFullyRead()
/*      */   {
/* 1277 */     return this.endOfStream;
/*      */   }
/*      */   
/*      */ 
/*      */   protected final void registerReadInterest()
/*      */   {
/* 1283 */     this.socketWrapper.registerReadInterest();
/*      */   }
/*      */   
/*      */ 
/*      */   protected final boolean isReady()
/*      */   {
/* 1289 */     return (this.responseMsgPos == -1) && (this.socketWrapper.isReadyForWrite());
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
/*      */   private boolean read(byte[] buf, int pos, int n, boolean block)
/*      */     throws IOException
/*      */   {
/* 1309 */     int read = this.socketWrapper.read(block, buf, pos, n);
/* 1310 */     if ((read > 0) && (read < n)) {
/* 1311 */       int left = n - read;
/* 1312 */       int start = pos + read;
/* 1313 */       while (left > 0) {
/* 1314 */         read = this.socketWrapper.read(true, buf, start, left);
/* 1315 */         if (read == -1) {
/* 1316 */           throw new EOFException();
/*      */         }
/* 1318 */         left -= read;
/* 1319 */         start += read;
/*      */       }
/* 1321 */     } else if (read == -1) {
/* 1322 */       throw new EOFException();
/*      */     }
/*      */     
/* 1325 */     return read > 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   private void writeData(ByteChunk chunk)
/*      */     throws IOException
/*      */   {
/* 1335 */     boolean blocking = this.response.getWriteListener() == null;
/*      */     
/* 1337 */     int len = chunk.getLength();
/* 1338 */     int off = 0;
/*      */     
/*      */ 
/* 1341 */     while (len > 0) {
/* 1342 */       int thisTime = Math.min(len, this.outputMaxChunkSize);
/*      */       
/* 1344 */       this.responseMessage.reset();
/* 1345 */       this.responseMessage.appendByte(3);
/* 1346 */       this.responseMessage.appendBytes(chunk.getBytes(), chunk.getOffset() + off, thisTime);
/* 1347 */       this.responseMessage.end();
/* 1348 */       this.socketWrapper.write(blocking, this.responseMessage.getBuffer(), 0, this.responseMessage.getLen());
/* 1349 */       this.socketWrapper.flush(blocking);
/*      */       
/* 1351 */       len -= thisTime;
/* 1352 */       off += thisTime;
/*      */     }
/*      */     
/* 1355 */     this.bytesWritten += off;
/*      */   }
/*      */   
/*      */   private void writeData(ByteBuffer chunk) throws IOException
/*      */   {
/* 1360 */     boolean blocking = this.response.getWriteListener() == null;
/*      */     
/* 1362 */     int len = chunk.remaining();
/* 1363 */     int off = 0;
/*      */     
/*      */ 
/* 1366 */     while (len > 0) {
/* 1367 */       int thisTime = Math.min(len, this.outputMaxChunkSize);
/*      */       
/* 1369 */       this.responseMessage.reset();
/* 1370 */       this.responseMessage.appendByte(3);
/* 1371 */       chunk.limit(chunk.position() + thisTime);
/* 1372 */       this.responseMessage.appendBytes(chunk);
/* 1373 */       this.responseMessage.end();
/* 1374 */       this.socketWrapper.write(blocking, this.responseMessage.getBuffer(), 0, this.responseMessage.getLen());
/* 1375 */       this.socketWrapper.flush(blocking);
/*      */       
/* 1377 */       len -= thisTime;
/* 1378 */       off += thisTime;
/*      */     }
/*      */     
/* 1381 */     this.bytesWritten += off;
/*      */   }
/*      */   
/*      */   private boolean hasDataToWrite()
/*      */   {
/* 1386 */     return (this.responseMsgPos != -1) || (this.socketWrapper.hasDataToWrite());
/*      */   }
/*      */   
/*      */ 
/*      */   protected Log getLog()
/*      */   {
/* 1392 */     return log;
/*      */   }
/*      */   
/*      */ 
/*      */   public void pause() {}
/*      */   
/*      */ 
/*      */   protected final void ack() {}
/*      */   
/*      */   protected final void disableSwallowRequest() {}
/*      */   
/*      */   protected class SocketInputBuffer
/*      */     implements InputBuffer
/*      */   {
/*      */     protected SocketInputBuffer() {}
/*      */     
/*      */     @Deprecated
/*      */     public int doRead(ByteChunk chunk)
/*      */       throws IOException
/*      */     {
/* 1412 */       if (AjpProcessor.this.endOfStream) {
/* 1413 */         return -1;
/*      */       }
/* 1415 */       if ((AjpProcessor.this.empty) && 
/* 1416 */         (!AjpProcessor.this.refillReadBuffer(true))) {
/* 1417 */         return -1;
/*      */       }
/*      */       
/* 1420 */       ByteChunk bc = AjpProcessor.this.bodyBytes.getByteChunk();
/* 1421 */       chunk.setBytes(bc.getBuffer(), bc.getStart(), bc.getLength());
/* 1422 */       AjpProcessor.this.empty = true;
/* 1423 */       return chunk.getLength();
/*      */     }
/*      */     
/*      */     public int doRead(ApplicationBufferHandler handler)
/*      */       throws IOException
/*      */     {
/* 1429 */       if (AjpProcessor.this.endOfStream) {
/* 1430 */         return -1;
/*      */       }
/* 1432 */       if ((AjpProcessor.this.empty) && 
/* 1433 */         (!AjpProcessor.this.refillReadBuffer(true))) {
/* 1434 */         return -1;
/*      */       }
/*      */       
/* 1437 */       ByteChunk bc = AjpProcessor.this.bodyBytes.getByteChunk();
/* 1438 */       handler.setByteBuffer(ByteBuffer.wrap(bc.getBuffer(), bc.getStart(), bc.getLength()));
/* 1439 */       AjpProcessor.this.empty = true;
/* 1440 */       return handler.getByteBuffer().remaining();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected class SocketOutputBuffer
/*      */     implements OutputBuffer
/*      */   {
/*      */     protected SocketOutputBuffer() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     @Deprecated
/*      */     public int doWrite(ByteChunk chunk)
/*      */       throws IOException
/*      */     {
/* 1461 */       if (!AjpProcessor.this.response.isCommitted()) {
/*      */         try
/*      */         {
/* 1464 */           AjpProcessor.this.prepareResponse();
/*      */         } catch (IOException e) {
/* 1466 */           AjpProcessor.this.setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
/*      */         }
/*      */       }
/*      */       
/* 1470 */       if (!AjpProcessor.this.swallowResponse) {
/* 1471 */         AjpProcessor.this.writeData(chunk);
/*      */       }
/* 1473 */       return chunk.getLength();
/*      */     }
/*      */     
/*      */     public int doWrite(ByteBuffer chunk)
/*      */       throws IOException
/*      */     {
/* 1479 */       if (!AjpProcessor.this.response.isCommitted()) {
/*      */         try
/*      */         {
/* 1482 */           AjpProcessor.this.prepareResponse();
/*      */         } catch (IOException e) {
/* 1484 */           AjpProcessor.this.setErrorState(ErrorState.CLOSE_CONNECTION_NOW, e);
/*      */         }
/*      */       }
/*      */       
/* 1488 */       int len = 0;
/* 1489 */       if (!AjpProcessor.this.swallowResponse) {
/*      */         try {
/* 1491 */           len = chunk.remaining();
/* 1492 */           AjpProcessor.this.writeData(chunk);
/* 1493 */           len -= chunk.remaining();
/*      */         } catch (IOException ioe) {
/* 1495 */           AjpProcessor.this.setErrorState(ErrorState.CLOSE_CONNECTION_NOW, ioe);
/*      */           
/* 1497 */           throw ioe;
/*      */         }
/*      */       }
/* 1500 */       return len;
/*      */     }
/*      */     
/*      */     public long getBytesWritten()
/*      */     {
/* 1505 */       return AjpProcessor.this.bytesWritten;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\ajp\AjpProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */