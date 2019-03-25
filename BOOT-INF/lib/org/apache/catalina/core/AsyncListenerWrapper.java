/*    */ package org.apache.catalina.core;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import javax.servlet.AsyncEvent;
/*    */ import javax.servlet.AsyncListener;
/*    */ import javax.servlet.ServletRequest;
/*    */ import javax.servlet.ServletResponse;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AsyncListenerWrapper
/*    */ {
/* 28 */   private AsyncListener listener = null;
/* 29 */   private ServletRequest servletRequest = null;
/* 30 */   private ServletResponse servletResponse = null;
/*    */   
/*    */   public void fireOnStartAsync(AsyncEvent event) throws IOException
/*    */   {
/* 34 */     this.listener.onStartAsync(customizeEvent(event));
/*    */   }
/*    */   
/*    */   public void fireOnComplete(AsyncEvent event) throws IOException
/*    */   {
/* 39 */     this.listener.onComplete(customizeEvent(event));
/*    */   }
/*    */   
/*    */   public void fireOnTimeout(AsyncEvent event) throws IOException
/*    */   {
/* 44 */     this.listener.onTimeout(customizeEvent(event));
/*    */   }
/*    */   
/*    */   public void fireOnError(AsyncEvent event) throws IOException
/*    */   {
/* 49 */     this.listener.onError(customizeEvent(event));
/*    */   }
/*    */   
/*    */   public AsyncListener getListener()
/*    */   {
/* 54 */     return this.listener;
/*    */   }
/*    */   
/*    */   public void setListener(AsyncListener listener)
/*    */   {
/* 59 */     this.listener = listener;
/*    */   }
/*    */   
/*    */   public void setServletRequest(ServletRequest servletRequest)
/*    */   {
/* 64 */     this.servletRequest = servletRequest;
/*    */   }
/*    */   
/*    */   public void setServletResponse(ServletResponse servletResponse)
/*    */   {
/* 69 */     this.servletResponse = servletResponse;
/*    */   }
/*    */   
/*    */   private AsyncEvent customizeEvent(AsyncEvent event)
/*    */   {
/* 74 */     if ((this.servletRequest != null) && (this.servletResponse != null)) {
/* 75 */       return new AsyncEvent(event.getAsyncContext(), this.servletRequest, this.servletResponse, event
/* 76 */         .getThrowable());
/*    */     }
/* 78 */     return event;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\AsyncListenerWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */