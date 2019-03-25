/*     */ package org.apache.coyote.http2;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import org.apache.coyote.AbstractProcessor;
/*     */ import org.apache.coyote.ActionCode;
/*     */ import org.apache.coyote.Adapter;
/*     */ import org.apache.coyote.ContainerThreadMarker;
/*     */ import org.apache.coyote.ErrorState;
/*     */ import org.apache.coyote.Request;
/*     */ import org.apache.coyote.Response;
/*     */ import org.apache.coyote.http11.HttpOutputBuffer;
/*     */ import org.apache.coyote.http11.filters.GzipOutputFilter;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.http.FastHttpDateFormat;
/*     */ import org.apache.tomcat.util.http.MimeHeaders;
/*     */ import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
/*     */ import org.apache.tomcat.util.net.DispatchType;
/*     */ import org.apache.tomcat.util.net.SocketEvent;
/*     */ import org.apache.tomcat.util.net.SocketWrapperBase;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class StreamProcessor
/*     */   extends AbstractProcessor
/*     */ {
/*  43 */   private static final Log log = LogFactory.getLog(StreamProcessor.class);
/*  44 */   private static final StringManager sm = StringManager.getManager(StreamProcessor.class);
/*     */   
/*     */   private final Http2UpgradeHandler handler;
/*     */   
/*     */   private final Stream stream;
/*     */   
/*     */   StreamProcessor(Http2UpgradeHandler handler, Stream stream, Adapter adapter, SocketWrapperBase<?> socketWrapper)
/*     */   {
/*  52 */     super(socketWrapper.getEndpoint(), stream.getCoyoteRequest(), stream.getCoyoteResponse());
/*  53 */     this.handler = handler;
/*  54 */     this.stream = stream;
/*  55 */     setAdapter(adapter);
/*  56 */     setSocketWrapper(socketWrapper);
/*     */   }
/*     */   
/*     */   final void process(SocketEvent event)
/*     */   {
/*     */     try
/*     */     {
/*  63 */       synchronized (this)
/*     */       {
/*     */ 
/*  66 */         ContainerThreadMarker.set();
/*  67 */         AbstractEndpoint.Handler.SocketState state = AbstractEndpoint.Handler.SocketState.CLOSED;
/*     */         try {
/*  69 */           state = process(this.socketWrapper, event);
/*     */           
/*  71 */           if (state == AbstractEndpoint.Handler.SocketState.CLOSED) {
/*  72 */             if (!getErrorState().isConnectionIoAllowed()) {
/*  73 */               ConnectionException ce = new ConnectionException(sm.getString("streamProcessor.error.connection", new Object[] {this.stream
/*  74 */                 .getConnectionId(), this.stream
/*  75 */                 .getIdentifier() }), Http2Error.INTERNAL_ERROR);
/*  76 */               this.stream.close(ce);
/*  77 */             } else if (!getErrorState().isIoAllowed())
/*     */             {
/*     */ 
/*     */ 
/*  81 */               StreamException se = new StreamException(sm.getString("streamProcessor.error.stream", new Object[] { this.stream.getConnectionId(), this.stream.getIdentifier() }), Http2Error.INTERNAL_ERROR, this.stream.getIdentifier().intValue());
/*  82 */               this.stream.close(se);
/*     */             }
/*     */           }
/*     */         } catch (Exception e) {
/*  86 */           ConnectionException ce = new ConnectionException(sm.getString("streamProcessor.error.connection", new Object[] {this.stream
/*  87 */             .getConnectionId(), this.stream
/*  88 */             .getIdentifier() }), Http2Error.INTERNAL_ERROR);
/*  89 */           ce.initCause(e);
/*  90 */           this.stream.close(ce);
/*     */         } finally {
/*  92 */           ContainerThreadMarker.clear();
/*     */         }
/*     */       }
/*     */     } finally {
/*  96 */       this.handler.executeQueuedStream();
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void prepareResponse()
/*     */     throws IOException
/*     */   {
/* 103 */     this.response.setCommitted(true);
/* 104 */     prepareHeaders(this.request, this.response, this.handler.getProtocol(), this.stream);
/* 105 */     this.stream.writeHeaders();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static void prepareHeaders(Request coyoteRequest, Response coyoteResponse, Http2Protocol protocol, Stream stream)
/*     */   {
/* 113 */     MimeHeaders headers = coyoteResponse.getMimeHeaders();
/* 114 */     int statusCode = coyoteResponse.getStatus();
/*     */     
/*     */ 
/* 117 */     headers.addValue(":status").setString(Integer.toString(statusCode));
/*     */     
/*     */ 
/* 120 */     if ((statusCode >= 200) && (statusCode != 205) && (statusCode != 304)) {
/* 121 */       String contentType = coyoteResponse.getContentType();
/* 122 */       if (contentType != null) {
/* 123 */         headers.setValue("content-type").setString(contentType);
/*     */       }
/* 125 */       String contentLanguage = coyoteResponse.getContentLanguage();
/* 126 */       if (contentLanguage != null) {
/* 127 */         headers.setValue("content-language").setString(contentLanguage);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 133 */     if ((statusCode >= 200) && (headers.getValue("date") == null)) {
/* 134 */       headers.addValue("date").setString(FastHttpDateFormat.getCurrentDate());
/*     */     }
/*     */     
/* 137 */     if ((protocol != null) && (protocol.useCompression(coyoteRequest, coyoteResponse)))
/*     */     {
/*     */ 
/* 140 */       stream.addOutputFilter(new GzipOutputFilter());
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void finishResponse()
/*     */     throws IOException
/*     */   {
/* 147 */     this.stream.getOutputBuffer().end();
/*     */   }
/*     */   
/*     */ 
/*     */   protected final void ack()
/*     */   {
/* 153 */     if ((!this.response.isCommitted()) && (this.request.hasExpectation())) {
/*     */       try {
/* 155 */         this.stream.writeAck();
/*     */       } catch (IOException ioe) {
/* 157 */         setErrorState(ErrorState.CLOSE_CONNECTION_NOW, ioe);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void flush()
/*     */     throws IOException
/*     */   {
/* 165 */     this.stream.getOutputBuffer().flush();
/*     */   }
/*     */   
/*     */ 
/*     */   protected final int available(boolean doRead)
/*     */   {
/* 171 */     return this.stream.getInputBuffer().available();
/*     */   }
/*     */   
/*     */ 
/*     */   protected final void setRequestBody(ByteChunk body)
/*     */   {
/* 177 */     this.stream.getInputBuffer().insertReplayedBody(body);
/*     */     try {
/* 179 */       this.stream.receivedEndOfStream();
/*     */     }
/*     */     catch (ConnectionException localConnectionException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void setSwallowResponse() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void disableSwallowRequest() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void processSocketEvent(SocketEvent event, boolean dispatch)
/*     */   {
/* 202 */     if (dispatch) {
/* 203 */       this.handler.processStreamOnContainerThread(this, event);
/*     */     } else {
/* 205 */       process(event);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected final boolean isRequestBodyFullyRead()
/*     */   {
/* 212 */     return this.stream.getInputBuffer().isRequestBodyFullyRead();
/*     */   }
/*     */   
/*     */ 
/*     */   protected final void registerReadInterest()
/*     */   {
/* 218 */     this.stream.getInputBuffer().registerReadInterest();
/*     */   }
/*     */   
/*     */ 
/*     */   protected final boolean isReady()
/*     */   {
/* 224 */     return this.stream.isReady();
/*     */   }
/*     */   
/*     */ 
/*     */   protected final void executeDispatches()
/*     */   {
/* 230 */     Iterator<DispatchType> dispatches = getIteratorAndClearDispatches();
/* 231 */     synchronized (this)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 236 */       while ((dispatches != null) && (dispatches.hasNext())) {
/* 237 */         DispatchType dispatchType = (DispatchType)dispatches.next();
/* 238 */         processSocketEvent(dispatchType.getSocketStatus(), false);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected final boolean isPushSupported()
/*     */   {
/* 246 */     return this.stream.isPushSupported();
/*     */   }
/*     */   
/*     */   protected final void doPush(Request pushTarget)
/*     */   {
/*     */     try
/*     */     {
/* 253 */       this.stream.push(pushTarget);
/*     */     } catch (IOException ioe) {
/* 255 */       setErrorState(ErrorState.CLOSE_CONNECTION_NOW, ioe);
/* 256 */       this.response.setErrorException(ioe);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void recycle()
/*     */   {
/* 266 */     setSocketWrapper(null);
/* 267 */     setAdapter(null);
/*     */   }
/*     */   
/*     */ 
/*     */   protected Log getLog()
/*     */   {
/* 273 */     return log;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void pause() {}
/*     */   
/*     */ 
/*     */   public AbstractEndpoint.Handler.SocketState service(SocketWrapperBase<?> socket)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 286 */       this.adapter.service(this.request, this.response);
/*     */     } catch (Exception e) {
/* 288 */       if (log.isDebugEnabled()) {
/* 289 */         log.debug(sm.getString("streamProcessor.service.error"), e);
/*     */       }
/* 291 */       this.response.setStatus(500);
/* 292 */       setErrorState(ErrorState.CLOSE_NOW, e);
/*     */     }
/*     */     
/* 295 */     if (getErrorState().isError()) {
/* 296 */       action(ActionCode.CLOSE, null);
/* 297 */       this.request.updateCounters();
/* 298 */       return AbstractEndpoint.Handler.SocketState.CLOSED; }
/* 299 */     if (isAsync()) {
/* 300 */       return AbstractEndpoint.Handler.SocketState.LONG;
/*     */     }
/* 302 */     action(ActionCode.CLOSE, null);
/* 303 */     this.request.updateCounters();
/* 304 */     return AbstractEndpoint.Handler.SocketState.CLOSED;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean flushBufferedWrite()
/*     */     throws IOException
/*     */   {
/* 311 */     if (this.stream.flush(false))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 319 */       if (this.stream.isReady())
/*     */       {
/* 321 */         throw new IllegalStateException();
/*     */       }
/* 323 */       return true;
/*     */     }
/* 325 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   protected AbstractEndpoint.Handler.SocketState dispatchEndRequest()
/*     */   {
/* 331 */     return AbstractEndpoint.Handler.SocketState.CLOSED;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\StreamProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */