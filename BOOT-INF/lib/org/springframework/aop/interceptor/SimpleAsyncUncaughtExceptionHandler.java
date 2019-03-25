/*    */ package org.springframework.aop.interceptor;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
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
/*    */ public class SimpleAsyncUncaughtExceptionHandler
/*    */   implements AsyncUncaughtExceptionHandler
/*    */ {
/* 32 */   private final Log logger = LogFactory.getLog(SimpleAsyncUncaughtExceptionHandler.class);
/*    */   
/*    */   public void handleUncaughtException(Throwable ex, Method method, Object... params)
/*    */   {
/* 36 */     if (this.logger.isErrorEnabled()) {
/* 37 */       this.logger.error(String.format("Unexpected error occurred invoking async method '%s'.", new Object[] { method }), ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\interceptor\SimpleAsyncUncaughtExceptionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */