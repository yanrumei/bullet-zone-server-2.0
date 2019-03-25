/*     */ package org.apache.tomcat.websocket;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public class BackgroundProcessManager
/*     */ {
/*  35 */   private static final Log log = LogFactory.getLog(BackgroundProcessManager.class);
/*     */   
/*  37 */   private static final StringManager sm = StringManager.getManager(BackgroundProcessManager.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  42 */   private static final BackgroundProcessManager instance = new BackgroundProcessManager();
/*     */   
/*     */ 
/*     */   public static BackgroundProcessManager getInstance()
/*     */   {
/*  47 */     return instance;
/*     */   }
/*     */   
/*  50 */   private final Set<BackgroundProcess> processes = new HashSet();
/*  51 */   private final Object processesLock = new Object();
/*  52 */   private WsBackgroundThread wsBackgroundThread = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void register(BackgroundProcess process)
/*     */   {
/*  60 */     synchronized (this.processesLock) {
/*  61 */       if (this.processes.size() == 0) {
/*  62 */         this.wsBackgroundThread = new WsBackgroundThread(this);
/*  63 */         this.wsBackgroundThread.setContextClassLoader(
/*  64 */           getClass().getClassLoader());
/*  65 */         this.wsBackgroundThread.setDaemon(true);
/*  66 */         this.wsBackgroundThread.start();
/*     */       }
/*  68 */       this.processes.add(process);
/*     */     }
/*     */   }
/*     */   
/*     */   public void unregister(BackgroundProcess process)
/*     */   {
/*  74 */     synchronized (this.processesLock) {
/*  75 */       this.processes.remove(process);
/*  76 */       if ((this.wsBackgroundThread != null) && (this.processes.size() == 0)) {
/*  77 */         this.wsBackgroundThread.halt();
/*  78 */         this.wsBackgroundThread = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void process()
/*     */   {
/*  85 */     Set<BackgroundProcess> currentProcesses = new HashSet();
/*  86 */     synchronized (this.processesLock) {
/*  87 */       currentProcesses.addAll(this.processes);
/*     */     }
/*  89 */     for (??? = currentProcesses.iterator(); ((Iterator)???).hasNext();) { BackgroundProcess process = (BackgroundProcess)((Iterator)???).next();
/*     */       try {
/*  91 */         process.backgroundProcess();
/*     */       } catch (Throwable t) {
/*  93 */         ExceptionUtils.handleThrowable(t);
/*  94 */         log.error(sm.getString("backgroundProcessManager.processFailed"), t);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   int getProcessCount()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 8	org/apache/tomcat/websocket/BackgroundProcessManager:processesLock	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 6	org/apache/tomcat/websocket/BackgroundProcessManager:processes	Ljava/util/Set;
/*     */     //   11: invokeinterface 10 1 0
/*     */     //   16: aload_1
/*     */     //   17: monitorexit
/*     */     //   18: ireturn
/*     */     //   19: astore_2
/*     */     //   20: aload_1
/*     */     //   21: monitorexit
/*     */     //   22: aload_2
/*     */     //   23: athrow
/*     */     // Line number table:
/*     */     //   Java source line #105	-> byte code offset #0
/*     */     //   Java source line #106	-> byte code offset #7
/*     */     //   Java source line #107	-> byte code offset #19
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	24	0	this	BackgroundProcessManager
/*     */     //   5	16	1	Ljava/lang/Object;	Object
/*     */     //   19	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	18	19	finally
/*     */     //   19	22	19	finally
/*     */   }
/*     */   
/*     */   void shutdown()
/*     */   {
/* 112 */     synchronized (this.processesLock) {
/* 113 */       this.processes.clear();
/* 114 */       if (this.wsBackgroundThread != null) {
/* 115 */         this.wsBackgroundThread.halt();
/* 116 */         this.wsBackgroundThread = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static class WsBackgroundThread
/*     */     extends Thread
/*     */   {
/*     */     private final BackgroundProcessManager manager;
/* 125 */     private volatile boolean running = true;
/*     */     
/*     */     public WsBackgroundThread(BackgroundProcessManager manager) {
/* 128 */       setName("WebSocket background processing");
/* 129 */       this.manager = manager;
/*     */     }
/*     */     
/*     */     public void run()
/*     */     {
/* 134 */       while (this.running) {
/*     */         try {
/* 136 */           Thread.sleep(1000L);
/*     */         }
/*     */         catch (InterruptedException localInterruptedException) {}
/*     */         
/* 140 */         this.manager.process();
/*     */       }
/*     */     }
/*     */     
/*     */     public void halt() {
/* 145 */       setName("WebSocket background processing - stopping");
/* 146 */       this.running = false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\BackgroundProcessManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */