/*    */ package org.apache.tomcat.util.threads;
/*    */ 
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.juli.logging.LogFactory;
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
/*    */ public class TaskThread
/*    */   extends Thread
/*    */ {
/* 28 */   private static final Log log = LogFactory.getLog(TaskThread.class);
/*    */   private final long creationTime;
/*    */   
/*    */   public TaskThread(ThreadGroup group, Runnable target, String name) {
/* 32 */     super(group, new WrappingRunnable(target), name);
/* 33 */     this.creationTime = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */   public TaskThread(ThreadGroup group, Runnable target, String name, long stackSize)
/*    */   {
/* 38 */     super(group, new WrappingRunnable(target), name, stackSize);
/* 39 */     this.creationTime = System.currentTimeMillis();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public final long getCreationTime()
/*    */   {
/* 46 */     return this.creationTime;
/*    */   }
/*    */   
/*    */   private static class WrappingRunnable
/*    */     implements Runnable
/*    */   {
/*    */     private Runnable wrappedRunnable;
/*    */     
/*    */     WrappingRunnable(Runnable wrappedRunnable)
/*    */     {
/* 56 */       this.wrappedRunnable = wrappedRunnable;
/*    */     }
/*    */     
/*    */     public void run() {
/*    */       try {
/* 61 */         this.wrappedRunnable.run();
/*    */       }
/*    */       catch (StopPooledThreadException exc)
/*    */       {
/* 65 */         TaskThread.log.debug("Thread exiting on purpose", exc);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\threads\TaskThread.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */