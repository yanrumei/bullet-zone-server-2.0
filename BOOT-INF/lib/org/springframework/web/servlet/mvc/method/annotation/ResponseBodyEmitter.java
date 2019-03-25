/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.server.ServerHttpResponse;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ResponseBodyEmitter
/*     */ {
/*     */   private final Long timeout;
/*  65 */   private final Set<DataWithMediaType> earlySendAttempts = new LinkedHashSet(8);
/*     */   
/*     */   private Handler handler;
/*     */   
/*     */   private boolean complete;
/*     */   
/*     */   private Throwable failure;
/*     */   
/*  73 */   private final DefaultCallback timeoutCallback = new DefaultCallback(null);
/*     */   
/*  75 */   private final DefaultCallback completionCallback = new DefaultCallback(null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResponseBodyEmitter()
/*     */   {
/*  82 */     this.timeout = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResponseBodyEmitter(Long timeout)
/*     */   {
/*  93 */     this.timeout = timeout;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Long getTimeout()
/*     */   {
/* 101 */     return this.timeout;
/*     */   }
/*     */   
/*     */   synchronized void initialize(Handler handler) throws IOException
/*     */   {
/* 106 */     this.handler = handler;
/*     */     
/* 108 */     for (DataWithMediaType sendAttempt : this.earlySendAttempts) {
/* 109 */       sendInternal(sendAttempt.getData(), sendAttempt.getMediaType());
/*     */     }
/* 111 */     this.earlySendAttempts.clear();
/*     */     
/* 113 */     if (this.complete) {
/* 114 */       if (this.failure != null) {
/* 115 */         this.handler.completeWithError(this.failure);
/*     */       }
/*     */       else {
/* 118 */         this.handler.complete();
/*     */       }
/*     */     }
/*     */     else {
/* 122 */       this.handler.onTimeout(this.timeoutCallback);
/* 123 */       this.handler.onCompletion(this.completionCallback);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void extendResponse(ServerHttpResponse outputMessage) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void send(Object object)
/*     */     throws IOException
/*     */   {
/* 145 */     send(object, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void send(Object object, MediaType mediaType)
/*     */     throws IOException
/*     */   {
/* 158 */     Assert.state(!this.complete, "ResponseBodyEmitter is already set complete");
/* 159 */     sendInternal(object, mediaType);
/*     */   }
/*     */   
/*     */   private void sendInternal(Object object, MediaType mediaType) throws IOException {
/* 163 */     if (object != null) {
/* 164 */       if (this.handler != null) {
/*     */         try {
/* 166 */           this.handler.send(object, mediaType);
/*     */         }
/*     */         catch (IOException ex) {
/* 169 */           throw ex;
/*     */         }
/*     */         catch (Throwable ex) {
/* 172 */           throw new IllegalStateException("Failed to send " + object, ex);
/*     */         }
/*     */         
/*     */       } else {
/* 176 */         this.earlySendAttempts.add(new DataWithMediaType(object, mediaType));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void complete()
/*     */   {
/* 187 */     this.complete = true;
/* 188 */     if (this.handler != null) {
/* 189 */       this.handler.complete();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void completeWithError(Throwable ex)
/*     */   {
/* 199 */     this.complete = true;
/* 200 */     this.failure = ex;
/* 201 */     if (this.handler != null) {
/* 202 */       this.handler.completeWithError(ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void onTimeout(Runnable callback)
/*     */   {
/* 211 */     this.timeoutCallback.setDelegate(callback);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void onCompletion(Runnable callback)
/*     */   {
/* 221 */     this.completionCallback.setDelegate(callback);
/*     */   }
/*     */   
/*     */ 
/*     */   static abstract interface Handler
/*     */   {
/*     */     public abstract void send(Object paramObject, MediaType paramMediaType)
/*     */       throws IOException;
/*     */     
/*     */ 
/*     */     public abstract void complete();
/*     */     
/*     */ 
/*     */     public abstract void completeWithError(Throwable paramThrowable);
/*     */     
/*     */ 
/*     */     public abstract void onTimeout(Runnable paramRunnable);
/*     */     
/*     */ 
/*     */     public abstract void onCompletion(Runnable paramRunnable);
/*     */   }
/*     */   
/*     */ 
/*     */   public static class DataWithMediaType
/*     */   {
/*     */     private final Object data;
/*     */     
/*     */     private final MediaType mediaType;
/*     */     
/*     */ 
/*     */     public DataWithMediaType(Object data, MediaType mediaType)
/*     */     {
/* 253 */       this.data = data;
/* 254 */       this.mediaType = mediaType;
/*     */     }
/*     */     
/*     */     public Object getData() {
/* 258 */       return this.data;
/*     */     }
/*     */     
/*     */     public MediaType getMediaType() {
/* 262 */       return this.mediaType;
/*     */     }
/*     */   }
/*     */   
/*     */   private class DefaultCallback implements Runnable {
/*     */     private Runnable delegate;
/*     */     
/*     */     private DefaultCallback() {}
/*     */     
/*     */     public void setDelegate(Runnable delegate) {
/* 272 */       this.delegate = delegate;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 277 */       ResponseBodyEmitter.this.complete = true;
/* 278 */       if (this.delegate != null) {
/* 279 */         this.delegate.run();
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ResponseBodyEmitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */