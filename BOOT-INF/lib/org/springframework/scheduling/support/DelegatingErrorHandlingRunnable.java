/*    */ package org.springframework.scheduling.support;
/*    */ 
/*    */ import java.lang.reflect.UndeclaredThrowableException;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ErrorHandler;
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
/*    */ public class DelegatingErrorHandlingRunnable
/*    */   implements Runnable
/*    */ {
/*    */   private final Runnable delegate;
/*    */   private final ErrorHandler errorHandler;
/*    */   
/*    */   public DelegatingErrorHandlingRunnable(Runnable delegate, ErrorHandler errorHandler)
/*    */   {
/* 45 */     Assert.notNull(delegate, "Delegate must not be null");
/* 46 */     Assert.notNull(errorHandler, "ErrorHandler must not be null");
/* 47 */     this.delegate = delegate;
/* 48 */     this.errorHandler = errorHandler;
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/*    */     try {
/* 54 */       this.delegate.run();
/*    */     }
/*    */     catch (UndeclaredThrowableException ex) {
/* 57 */       this.errorHandler.handleError(ex.getUndeclaredThrowable());
/*    */     }
/*    */     catch (Throwable ex) {
/* 60 */       this.errorHandler.handleError(ex);
/*    */     }
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 66 */     return "DelegatingErrorHandlingRunnable for " + this.delegate;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\support\DelegatingErrorHandlingRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */