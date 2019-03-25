/*    */ package org.springframework.scheduling.annotation;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import java.util.concurrent.Executor;
/*    */ import org.springframework.aop.interceptor.AsyncExecutionInterceptor;
/*    */ import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
/*    */ import org.springframework.core.annotation.AnnotatedElementUtils;
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
/*    */ public class AnnotationAsyncExecutionInterceptor
/*    */   extends AsyncExecutionInterceptor
/*    */ {
/*    */   public AnnotationAsyncExecutionInterceptor(Executor defaultExecutor)
/*    */   {
/* 49 */     super(defaultExecutor);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public AnnotationAsyncExecutionInterceptor(Executor defaultExecutor, AsyncUncaughtExceptionHandler exceptionHandler)
/*    */   {
/* 62 */     super(defaultExecutor, exceptionHandler);
/*    */   }
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
/*    */   protected String getExecutorQualifier(Method method)
/*    */   {
/* 81 */     Async async = (Async)AnnotatedElementUtils.findMergedAnnotation(method, Async.class);
/* 82 */     if (async == null) {
/* 83 */       async = (Async)AnnotatedElementUtils.findMergedAnnotation(method.getDeclaringClass(), Async.class);
/*    */     }
/* 85 */     return async != null ? async.value() : null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\annotation\AnnotationAsyncExecutionInterceptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */