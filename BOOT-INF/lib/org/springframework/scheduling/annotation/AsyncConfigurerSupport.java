/*    */ package org.springframework.scheduling.annotation;
/*    */ 
/*    */ import java.util.concurrent.Executor;
/*    */ import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
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
/*    */ public class AsyncConfigurerSupport
/*    */   implements AsyncConfigurer
/*    */ {
/*    */   public Executor getAsyncExecutor()
/*    */   {
/* 35 */     return null;
/*    */   }
/*    */   
/*    */   public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler()
/*    */   {
/* 40 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\annotation\AsyncConfigurerSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */