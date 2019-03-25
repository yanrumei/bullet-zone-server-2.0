/*     */ package org.apache.coyote;
/*     */ 
/*     */ import java.util.ArrayList;
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
/*     */ public class RequestGroupInfo
/*     */ {
/*  26 */   private final ArrayList<RequestInfo> processors = new ArrayList();
/*  27 */   private long deadMaxTime = 0L;
/*  28 */   private long deadProcessingTime = 0L;
/*  29 */   private int deadRequestCount = 0;
/*  30 */   private int deadErrorCount = 0;
/*  31 */   private long deadBytesReceived = 0L;
/*  32 */   private long deadBytesSent = 0L;
/*     */   
/*     */   public synchronized void addRequestProcessor(RequestInfo rp) {
/*  35 */     this.processors.add(rp);
/*     */   }
/*     */   
/*     */   public synchronized void removeRequestProcessor(RequestInfo rp) {
/*  39 */     if (rp != null) {
/*  40 */       if (this.deadMaxTime < rp.getMaxTime())
/*  41 */         this.deadMaxTime = rp.getMaxTime();
/*  42 */       this.deadProcessingTime += rp.getProcessingTime();
/*  43 */       this.deadRequestCount += rp.getRequestCount();
/*  44 */       this.deadErrorCount += rp.getErrorCount();
/*  45 */       this.deadBytesReceived += rp.getBytesReceived();
/*  46 */       this.deadBytesSent += rp.getBytesSent();
/*     */       
/*  48 */       this.processors.remove(rp);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized long getMaxTime() {
/*  53 */     long maxTime = this.deadMaxTime;
/*  54 */     for (RequestInfo rp : this.processors) {
/*  55 */       if (maxTime < rp.getMaxTime()) {
/*  56 */         maxTime = rp.getMaxTime();
/*     */       }
/*     */     }
/*  59 */     return maxTime;
/*     */   }
/*     */   
/*     */   public synchronized void setMaxTime(long maxTime)
/*     */   {
/*  64 */     this.deadMaxTime = maxTime;
/*  65 */     for (RequestInfo rp : this.processors) {
/*  66 */       rp.setMaxTime(maxTime);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized long getProcessingTime() {
/*  71 */     long time = this.deadProcessingTime;
/*  72 */     for (RequestInfo rp : this.processors) {
/*  73 */       time += rp.getProcessingTime();
/*     */     }
/*  75 */     return time;
/*     */   }
/*     */   
/*     */   public synchronized void setProcessingTime(long totalTime) {
/*  79 */     this.deadProcessingTime = totalTime;
/*  80 */     for (RequestInfo rp : this.processors) {
/*  81 */       rp.setProcessingTime(totalTime);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized int getRequestCount() {
/*  86 */     int requestCount = this.deadRequestCount;
/*  87 */     for (RequestInfo rp : this.processors) {
/*  88 */       requestCount += rp.getRequestCount();
/*     */     }
/*  90 */     return requestCount;
/*     */   }
/*     */   
/*     */   public synchronized void setRequestCount(int requestCount) {
/*  94 */     this.deadRequestCount = requestCount;
/*  95 */     for (RequestInfo rp : this.processors) {
/*  96 */       rp.setRequestCount(requestCount);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized int getErrorCount() {
/* 101 */     int requestCount = this.deadErrorCount;
/* 102 */     for (RequestInfo rp : this.processors) {
/* 103 */       requestCount += rp.getErrorCount();
/*     */     }
/* 105 */     return requestCount;
/*     */   }
/*     */   
/*     */   public synchronized void setErrorCount(int errorCount) {
/* 109 */     this.deadErrorCount = errorCount;
/* 110 */     for (RequestInfo rp : this.processors) {
/* 111 */       rp.setErrorCount(errorCount);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized long getBytesReceived() {
/* 116 */     long bytes = this.deadBytesReceived;
/* 117 */     for (RequestInfo rp : this.processors) {
/* 118 */       bytes += rp.getBytesReceived();
/*     */     }
/* 120 */     return bytes;
/*     */   }
/*     */   
/*     */   public synchronized void setBytesReceived(long bytesReceived) {
/* 124 */     this.deadBytesReceived = bytesReceived;
/* 125 */     for (RequestInfo rp : this.processors) {
/* 126 */       rp.setBytesReceived(bytesReceived);
/*     */     }
/*     */   }
/*     */   
/*     */   public synchronized long getBytesSent() {
/* 131 */     long bytes = this.deadBytesSent;
/* 132 */     for (RequestInfo rp : this.processors) {
/* 133 */       bytes += rp.getBytesSent();
/*     */     }
/* 135 */     return bytes;
/*     */   }
/*     */   
/*     */   public synchronized void setBytesSent(long bytesSent) {
/* 139 */     this.deadBytesSent = bytesSent;
/* 140 */     for (RequestInfo rp : this.processors) {
/* 141 */       rp.setBytesSent(bytesSent);
/*     */     }
/*     */   }
/*     */   
/*     */   public void resetCounters() {
/* 146 */     setBytesReceived(0L);
/* 147 */     setBytesSent(0L);
/* 148 */     setRequestCount(0);
/* 149 */     setProcessingTime(0L);
/* 150 */     setMaxTime(0L);
/* 151 */     setErrorCount(0);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\RequestGroupInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */