/*    */ package javax.servlet;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AsyncEvent
/*    */ {
/*    */   private final AsyncContext context;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private final ServletRequest request;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private final ServletResponse response;
/*    */   
/*    */ 
/*    */ 
/*    */   private final Throwable throwable;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public AsyncEvent(AsyncContext context)
/*    */   {
/* 30 */     this.context = context;
/* 31 */     this.request = null;
/* 32 */     this.response = null;
/* 33 */     this.throwable = null;
/*    */   }
/*    */   
/*    */   public AsyncEvent(AsyncContext context, ServletRequest request, ServletResponse response)
/*    */   {
/* 38 */     this.context = context;
/* 39 */     this.request = request;
/* 40 */     this.response = response;
/* 41 */     this.throwable = null;
/*    */   }
/*    */   
/*    */   public AsyncEvent(AsyncContext context, Throwable throwable) {
/* 45 */     this.context = context;
/* 46 */     this.throwable = throwable;
/* 47 */     this.request = null;
/* 48 */     this.response = null;
/*    */   }
/*    */   
/*    */   public AsyncEvent(AsyncContext context, ServletRequest request, ServletResponse response, Throwable throwable)
/*    */   {
/* 53 */     this.context = context;
/* 54 */     this.request = request;
/* 55 */     this.response = response;
/* 56 */     this.throwable = throwable;
/*    */   }
/*    */   
/*    */   public AsyncContext getAsyncContext() {
/* 60 */     return this.context;
/*    */   }
/*    */   
/*    */   public ServletRequest getSuppliedRequest() {
/* 64 */     return this.request;
/*    */   }
/*    */   
/*    */   public ServletResponse getSuppliedResponse() {
/* 68 */     return this.response;
/*    */   }
/*    */   
/*    */   public Throwable getThrowable() {
/* 72 */     return this.throwable;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\javax\servlet\AsyncEvent.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */