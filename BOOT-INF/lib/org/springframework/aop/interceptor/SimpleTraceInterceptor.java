/*    */ package org.springframework.aop.interceptor;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.aopalliance.intercept.MethodInvocation;
/*    */ import org.apache.commons.logging.Log;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SimpleTraceInterceptor
/*    */   extends AbstractTraceInterceptor
/*    */ {
/*    */   public SimpleTraceInterceptor() {}
/*    */   
/*    */   public SimpleTraceInterceptor(boolean useDynamicLogger)
/*    */   {
/* 51 */     setUseDynamicLogger(useDynamicLogger);
/*    */   }
/*    */   
/*    */   protected Object invokeUnderTrace(MethodInvocation invocation, Log logger)
/*    */     throws Throwable
/*    */   {
/* 57 */     String invocationDescription = getInvocationDescription(invocation);
/* 58 */     writeToLog(logger, "Entering " + invocationDescription);
/*    */     try {
/* 60 */       Object rval = invocation.proceed();
/* 61 */       writeToLog(logger, "Exiting " + invocationDescription);
/* 62 */       return rval;
/*    */     }
/*    */     catch (Throwable ex) {
/* 65 */       writeToLog(logger, "Exception thrown in " + invocationDescription, ex);
/* 66 */       throw ex;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String getInvocationDescription(MethodInvocation invocation)
/*    */   {
/* 76 */     return 
/* 77 */       "method '" + invocation.getMethod().getName() + "' of class [" + invocation.getThis().getClass().getName() + "]";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\interceptor\SimpleTraceInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */