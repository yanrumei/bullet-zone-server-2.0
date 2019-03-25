/*     */ package ch.qos.logback.core;
/*     */ 
/*     */ import ch.qos.logback.core.helpers.CyclicBuffer;
/*     */ import ch.qos.logback.core.spi.LogbackLock;
/*     */ import ch.qos.logback.core.status.OnConsoleStatusListener;
/*     */ import ch.qos.logback.core.status.Status;
/*     */ import ch.qos.logback.core.status.StatusListener;
/*     */ import ch.qos.logback.core.status.StatusManager;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class BasicStatusManager
/*     */   implements StatusManager
/*     */ {
/*     */   public static final int MAX_HEADER_COUNT = 150;
/*     */   public static final int TAIL_SIZE = 150;
/*  31 */   int count = 0;
/*     */   
/*     */ 
/*  34 */   protected final List<Status> statusList = new ArrayList();
/*  35 */   protected final CyclicBuffer<Status> tailBuffer = new CyclicBuffer(150);
/*  36 */   protected final LogbackLock statusListLock = new LogbackLock();
/*     */   
/*  38 */   int level = 0;
/*     */   
/*     */ 
/*  41 */   protected final List<StatusListener> statusListenerList = new ArrayList();
/*  42 */   protected final LogbackLock statusListenerListLock = new LogbackLock();
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
/*     */   public void add(Status newStatus)
/*     */   {
/*  59 */     fireStatusAddEvent(newStatus);
/*     */     
/*  61 */     this.count += 1;
/*  62 */     if (newStatus.getLevel() > this.level) {
/*  63 */       this.level = newStatus.getLevel();
/*     */     }
/*     */     
/*  66 */     synchronized (this.statusListLock) {
/*  67 */       if (this.statusList.size() < 150) {
/*  68 */         this.statusList.add(newStatus);
/*     */       } else {
/*  70 */         this.tailBuffer.add(newStatus);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public List<Status> getCopyOfStatusList()
/*     */   {
/*  77 */     synchronized (this.statusListLock) {
/*  78 */       List<Status> tList = new ArrayList(this.statusList);
/*  79 */       tList.addAll(this.tailBuffer.asList());
/*  80 */       return tList;
/*     */     }
/*     */   }
/*     */   
/*     */   private void fireStatusAddEvent(Status status) {
/*  85 */     synchronized (this.statusListenerListLock) {
/*  86 */       for (StatusListener sl : this.statusListenerList) {
/*  87 */         sl.addStatusEvent(status);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/*  93 */     synchronized (this.statusListLock) {
/*  94 */       this.count = 0;
/*  95 */       this.statusList.clear();
/*  96 */       this.tailBuffer.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public int getLevel() {
/* 101 */     return this.level;
/*     */   }
/*     */   
/*     */   public int getCount() {
/* 105 */     return this.count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean add(StatusListener listener)
/*     */   {
/* 113 */     synchronized (this.statusListenerListLock) {
/* 114 */       if ((listener instanceof OnConsoleStatusListener)) {
/* 115 */         boolean alreadyPresent = checkForPresence(this.statusListenerList, listener.getClass());
/* 116 */         if (alreadyPresent)
/* 117 */           return false;
/*     */       }
/* 119 */       this.statusListenerList.add(listener);
/*     */     }
/* 121 */     return true;
/*     */   }
/*     */   
/*     */   private boolean checkForPresence(List<StatusListener> statusListenerList, Class<?> aClass) {
/* 125 */     for (StatusListener e : statusListenerList) {
/* 126 */       if (e.getClass() == aClass)
/* 127 */         return true;
/*     */     }
/* 129 */     return false;
/*     */   }
/*     */   
/*     */   public void remove(StatusListener listener) {
/* 133 */     synchronized (this.statusListenerListLock) {
/* 134 */       this.statusListenerList.remove(listener);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public List<StatusListener> getCopyOfStatusListenerList()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 14	ch/qos/logback/core/BasicStatusManager:statusListenerListLock	Lch/qos/logback/core/spi/LogbackLock;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: new 3	java/util/ArrayList
/*     */     //   10: dup
/*     */     //   11: aload_0
/*     */     //   12: getfield 13	ch/qos/logback/core/BasicStatusManager:statusListenerList	Ljava/util/List;
/*     */     //   15: invokespecial 20	java/util/ArrayList:<init>	(Ljava/util/Collection;)V
/*     */     //   18: aload_1
/*     */     //   19: monitorexit
/*     */     //   20: areturn
/*     */     //   21: astore_2
/*     */     //   22: aload_1
/*     */     //   23: monitorexit
/*     */     //   24: aload_2
/*     */     //   25: athrow
/*     */     // Line number table:
/*     */     //   Java source line #139	-> byte code offset #0
/*     */     //   Java source line #140	-> byte code offset #7
/*     */     //   Java source line #141	-> byte code offset #21
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	26	0	this	BasicStatusManager
/*     */     //   5	18	1	Ljava/lang/Object;	Object
/*     */     //   21	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	20	21	finally
/*     */     //   21	24	21	finally
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\BasicStatusManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */