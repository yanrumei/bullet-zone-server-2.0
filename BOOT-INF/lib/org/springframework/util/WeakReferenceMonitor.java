/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class WeakReferenceMonitor
/*     */ {
/*  52 */   private static final Log logger = LogFactory.getLog(WeakReferenceMonitor.class);
/*     */   
/*     */ 
/*  55 */   private static final ReferenceQueue<Object> handleQueue = new ReferenceQueue();
/*     */   
/*     */ 
/*  58 */   private static final Map<Reference<?>, ReleaseListener> trackedEntries = new HashMap();
/*     */   
/*     */ 
/*  61 */   private static Thread monitoringThread = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void monitor(Object handle, ReleaseListener listener)
/*     */   {
/*  71 */     if (logger.isDebugEnabled()) {
/*  72 */       logger.debug("Monitoring handle [" + handle + "] with release listener [" + listener + "]");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  77 */     WeakReference<Object> weakRef = new WeakReference(handle, handleQueue);
/*     */     
/*     */ 
/*  80 */     addEntry(weakRef, listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void addEntry(Reference<?> ref, ReleaseListener entry)
/*     */   {
/*  90 */     synchronized (WeakReferenceMonitor.class)
/*     */     {
/*  92 */       trackedEntries.put(ref, entry);
/*     */       
/*     */ 
/*  95 */       if (monitoringThread == null) {
/*  96 */         monitoringThread = new Thread(new MonitoringProcess(null), WeakReferenceMonitor.class.getName());
/*  97 */         monitoringThread.setDaemon(true);
/*  98 */         monitoringThread.start();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private static ReleaseListener removeEntry(Reference<?> reference)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: ldc 20
/*     */     //   2: dup
/*     */     //   3: astore_1
/*     */     //   4: monitorenter
/*     */     //   5: getstatic 21	org/springframework/util/WeakReferenceMonitor:trackedEntries	Ljava/util/Map;
/*     */     //   8: aload_0
/*     */     //   9: invokeinterface 30 2 0
/*     */     //   14: checkcast 31	org/springframework/util/WeakReferenceMonitor$ReleaseListener
/*     */     //   17: aload_1
/*     */     //   18: monitorexit
/*     */     //   19: areturn
/*     */     //   20: astore_2
/*     */     //   21: aload_1
/*     */     //   22: monitorexit
/*     */     //   23: aload_2
/*     */     //   24: athrow
/*     */     // Line number table:
/*     */     //   Java source line #109	-> byte code offset #0
/*     */     //   Java source line #110	-> byte code offset #5
/*     */     //   Java source line #111	-> byte code offset #20
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	25	0	reference	Reference<?>
/*     */     //   3	19	1	Ljava/lang/Object;	Object
/*     */     //   20	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   5	19	20	finally
/*     */     //   20	23	20	finally
/*     */   }
/*     */   
/*     */   private static boolean keepMonitoringThreadAlive()
/*     */   {
/* 119 */     synchronized (WeakReferenceMonitor.class) {
/* 120 */       if (!trackedEntries.isEmpty()) {
/* 121 */         return true;
/*     */       }
/*     */       
/* 124 */       logger.debug("No entries left to track - stopping reference monitor thread");
/* 125 */       monitoringThread = null;
/* 126 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface ReleaseListener
/*     */   {
/*     */     public abstract void released();
/*     */   }
/*     */   
/*     */   private static class MonitoringProcess implements Runnable
/*     */   {
/*     */     public void run()
/*     */     {
/* 139 */       WeakReferenceMonitor.logger.debug("Starting reference monitor thread");
/*     */       for (;;) {
/* 141 */         if (WeakReferenceMonitor.access$200()) {
/*     */           try {
/* 143 */             Reference<?> reference = WeakReferenceMonitor.handleQueue.remove();
/*     */             
/* 145 */             WeakReferenceMonitor.ReleaseListener entry = WeakReferenceMonitor.removeEntry(reference);
/* 146 */             if (entry != null) {
/*     */               try
/*     */               {
/* 149 */                 entry.released();
/*     */               }
/*     */               catch (Throwable ex) {
/* 152 */                 WeakReferenceMonitor.logger.warn("Reference release listener threw exception", ex);
/*     */               }
/*     */             }
/*     */           }
/*     */           catch (InterruptedException ex) {
/* 157 */             synchronized (WeakReferenceMonitor.class) {
/* 158 */               WeakReferenceMonitor.access$502(null);
/*     */             }
/* 160 */             WeakReferenceMonitor.logger.debug("Reference monitor thread interrupted", ex);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\WeakReferenceMonitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */