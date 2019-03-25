/*    */ package org.apache.catalina.startup;
/*    */ 
/*    */ import java.util.concurrent.ForkJoinPool;
/*    */ import java.util.concurrent.ForkJoinPool.ForkJoinWorkerThreadFactory;
/*    */ import java.util.concurrent.ForkJoinWorkerThread;
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
/*    */ public class SafeForkJoinWorkerThreadFactory
/*    */   implements ForkJoinPool.ForkJoinWorkerThreadFactory
/*    */ {
/*    */   public ForkJoinWorkerThread newThread(ForkJoinPool pool)
/*    */   {
/* 35 */     return new SafeForkJoinWorkerThread(pool);
/*    */   }
/*    */   
/*    */   private static class SafeForkJoinWorkerThread extends ForkJoinWorkerThread
/*    */   {
/*    */     protected SafeForkJoinWorkerThread(ForkJoinPool pool)
/*    */     {
/* 42 */       super();
/* 43 */       setContextClassLoader(ForkJoinPool.class.getClassLoader());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\startup\SafeForkJoinWorkerThreadFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */