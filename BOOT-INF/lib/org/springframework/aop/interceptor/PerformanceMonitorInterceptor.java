/*    */ package org.springframework.aop.interceptor;
/*    */ 
/*    */ import org.aopalliance.intercept.MethodInvocation;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.springframework.util.StopWatch;
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
/*    */ 
/*    */ public class PerformanceMonitorInterceptor
/*    */   extends AbstractMonitoringInterceptor
/*    */ {
/*    */   public PerformanceMonitorInterceptor() {}
/*    */   
/*    */   public PerformanceMonitorInterceptor(boolean useDynamicLogger)
/*    */   {
/* 52 */     setUseDynamicLogger(useDynamicLogger);
/*    */   }
/*    */   
/*    */   protected Object invokeUnderTrace(MethodInvocation invocation, Log logger)
/*    */     throws Throwable
/*    */   {
/* 58 */     String name = createInvocationTraceName(invocation);
/* 59 */     StopWatch stopWatch = new StopWatch(name);
/* 60 */     stopWatch.start(name);
/*    */     try {
/* 62 */       return invocation.proceed();
/*    */     }
/*    */     finally {
/* 65 */       stopWatch.stop();
/* 66 */       writeToLog(logger, stopWatch.shortSummary());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\interceptor\PerformanceMonitorInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */