/*     */ package org.springframework.aop.interceptor;
/*     */ 
/*     */ import com.jamonapi.MonKey;
/*     */ import com.jamonapi.MonKeyImp;
/*     */ import com.jamonapi.Monitor;
/*     */ import com.jamonapi.MonitorFactory;
/*     */ import com.jamonapi.utils.Misc;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
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
/*     */ public class JamonPerformanceMonitorInterceptor
/*     */   extends AbstractMonitoringInterceptor
/*     */ {
/*  46 */   private boolean trackAllInvocations = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JamonPerformanceMonitorInterceptor() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JamonPerformanceMonitorInterceptor(boolean useDynamicLogger)
/*     */   {
/*  62 */     setUseDynamicLogger(useDynamicLogger);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JamonPerformanceMonitorInterceptor(boolean useDynamicLogger, boolean trackAllInvocations)
/*     */   {
/*  74 */     setUseDynamicLogger(useDynamicLogger);
/*  75 */     setTrackAllInvocations(trackAllInvocations);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTrackAllInvocations(boolean trackAllInvocations)
/*     */   {
/*  87 */     this.trackAllInvocations = trackAllInvocations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isInterceptorEnabled(MethodInvocation invocation, Log logger)
/*     */   {
/*  99 */     return (this.trackAllInvocations) || (isLogEnabled(logger));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object invokeUnderTrace(MethodInvocation invocation, Log logger)
/*     */     throws Throwable
/*     */   {
/* 110 */     String name = createInvocationTraceName(invocation);
/* 111 */     MonKey key = new MonKeyImp(name, name, "ms.");
/*     */     
/* 113 */     Monitor monitor = MonitorFactory.start(key);
/*     */     try {
/* 115 */       return invocation.proceed();
/*     */     }
/*     */     catch (Throwable ex) {
/* 118 */       trackException(key, ex);
/* 119 */       throw ex;
/*     */     }
/*     */     finally {
/* 122 */       monitor.stop();
/* 123 */       if ((!this.trackAllInvocations) || (isLogEnabled(logger))) {
/* 124 */         writeToLog(logger, "JAMon performance statistics for method [" + name + "]:\n" + monitor);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void trackException(MonKey key, Throwable ex)
/*     */   {
/* 134 */     String stackTrace = "stackTrace=" + Misc.getExceptionTrace(ex);
/* 135 */     key.setDetails(stackTrace);
/*     */     
/*     */ 
/* 138 */     MonitorFactory.add(new MonKeyImp(ex.getClass().getName(), stackTrace, "Exception"), 1.0D);
/*     */     
/*     */ 
/* 141 */     MonitorFactory.add(new MonKeyImp("com.jamonapi.Exceptions", stackTrace, "Exception"), 1.0D);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\interceptor\JamonPerformanceMonitorInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */