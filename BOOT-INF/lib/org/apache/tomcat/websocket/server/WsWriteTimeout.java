/*     */ package org.apache.tomcat.websocket.server;
/*     */ 
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentSkipListSet;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.tomcat.websocket.BackgroundProcess;
/*     */ import org.apache.tomcat.websocket.BackgroundProcessManager;
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
/*     */ public class WsWriteTimeout
/*     */   implements BackgroundProcess
/*     */ {
/*  36 */   private final Set<WsRemoteEndpointImplServer> endpoints = new ConcurrentSkipListSet(new EndpointComparator(null));
/*     */   
/*  38 */   private final AtomicInteger count = new AtomicInteger(0);
/*  39 */   private int backgroundProcessCount = 0;
/*  40 */   private volatile int processPeriod = 1;
/*     */   
/*     */ 
/*     */   public void backgroundProcess()
/*     */   {
/*  45 */     this.backgroundProcessCount += 1;
/*     */     
/*  47 */     if (this.backgroundProcessCount >= this.processPeriod) {
/*  48 */       this.backgroundProcessCount = 0;
/*     */       
/*  50 */       long now = System.currentTimeMillis();
/*  51 */       Iterator<WsRemoteEndpointImplServer> iter = this.endpoints.iterator();
/*  52 */       while (iter.hasNext()) {
/*  53 */         WsRemoteEndpointImplServer endpoint = (WsRemoteEndpointImplServer)iter.next();
/*  54 */         if (endpoint.getTimeoutExpiry() >= now) {
/*     */           break;
/*     */         }
/*  57 */         endpoint.onTimeout(false);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setProcessPeriod(int period)
/*     */   {
/*  71 */     this.processPeriod = period;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getProcessPeriod()
/*     */   {
/*  83 */     return this.processPeriod;
/*     */   }
/*     */   
/*     */   public void register(WsRemoteEndpointImplServer endpoint)
/*     */   {
/*  88 */     boolean result = this.endpoints.add(endpoint);
/*  89 */     if (result) {
/*  90 */       int newCount = this.count.incrementAndGet();
/*  91 */       if (newCount == 1) {
/*  92 */         BackgroundProcessManager.getInstance().register(this);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void unregister(WsRemoteEndpointImplServer endpoint)
/*     */   {
/*  99 */     boolean result = this.endpoints.remove(endpoint);
/* 100 */     if (result) {
/* 101 */       int newCount = this.count.decrementAndGet();
/* 102 */       if (newCount == 0) {
/* 103 */         BackgroundProcessManager.getInstance().unregister(this);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class EndpointComparator
/*     */     implements Comparator<WsRemoteEndpointImplServer>
/*     */   {
/*     */     public int compare(WsRemoteEndpointImplServer o1, WsRemoteEndpointImplServer o2)
/*     */     {
/* 119 */       long t1 = o1.getTimeoutExpiry();
/* 120 */       long t2 = o2.getTimeoutExpiry();
/*     */       
/* 122 */       if (t1 < t2)
/* 123 */         return -1;
/* 124 */       if (t1 == t2) {
/* 125 */         return 0;
/*     */       }
/* 127 */       return 1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\server\WsWriteTimeout.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */