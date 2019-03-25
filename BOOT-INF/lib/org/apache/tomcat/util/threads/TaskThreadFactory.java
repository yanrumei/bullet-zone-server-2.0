/*    */ package org.apache.tomcat.util.threads;
/*    */ 
/*    */ import java.security.AccessController;
/*    */ import java.security.PrivilegedAction;
/*    */ import java.util.concurrent.ThreadFactory;
/*    */ import java.util.concurrent.atomic.AtomicInteger;
/*    */ import org.apache.tomcat.util.security.PrivilegedSetTccl;
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
/*    */ public class TaskThreadFactory
/*    */   implements ThreadFactory
/*    */ {
/*    */   private final ThreadGroup group;
/* 33 */   private final AtomicInteger threadNumber = new AtomicInteger(1);
/*    */   private final String namePrefix;
/*    */   private final boolean daemon;
/*    */   private final int threadPriority;
/*    */   
/*    */   public TaskThreadFactory(String namePrefix, boolean daemon, int priority) {
/* 39 */     SecurityManager s = System.getSecurityManager();
/* 40 */     this.group = (s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup());
/* 41 */     this.namePrefix = namePrefix;
/* 42 */     this.daemon = daemon;
/* 43 */     this.threadPriority = priority;
/*    */   }
/*    */   
/*    */   public Thread newThread(Runnable r)
/*    */   {
/* 48 */     TaskThread t = new TaskThread(this.group, r, this.namePrefix + this.threadNumber.getAndIncrement());
/* 49 */     t.setDaemon(this.daemon);
/* 50 */     t.setPriority(this.threadPriority);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 55 */     if (Constants.IS_SECURITY_ENABLED)
/*    */     {
/* 57 */       PrivilegedAction<Void> pa = new PrivilegedSetTccl(t, getClass().getClassLoader());
/* 58 */       AccessController.doPrivileged(pa);
/*    */     } else {
/* 60 */       t.setContextClassLoader(getClass().getClassLoader());
/*    */     }
/*    */     
/* 63 */     return t;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\threads\TaskThreadFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */