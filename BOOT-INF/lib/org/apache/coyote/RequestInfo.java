/*     */ package org.apache.coyote;
/*     */ 
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
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
/*     */ public class RequestInfo
/*     */ {
/*  36 */   private RequestGroupInfo global = null;
/*     */   private final Request req;
/*     */   
/*     */   public RequestInfo(Request req)
/*     */   {
/*  41 */     this.req = req;
/*     */   }
/*     */   
/*     */   public RequestGroupInfo getGlobalProcessor() {
/*  45 */     return this.global;
/*     */   }
/*     */   
/*     */   public void setGlobalProcessor(RequestGroupInfo global) {
/*  49 */     if (global != null) {
/*  50 */       this.global = global;
/*  51 */       global.addRequestProcessor(this);
/*     */     }
/*  53 */     else if (this.global != null) {
/*  54 */       this.global.removeRequestProcessor(this);
/*  55 */       this.global = null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  63 */   private int stage = 0;
/*     */   private String workerThreadName;
/*     */   private ObjectName rpName;
/*     */   private long bytesSent;
/*     */   private long bytesReceived;
/*     */   
/*     */   public String getMethod()
/*     */   {
/*  71 */     return this.req.method().toString();
/*     */   }
/*     */   
/*     */   public String getCurrentUri() {
/*  75 */     return this.req.requestURI().toString();
/*     */   }
/*     */   
/*     */   public String getCurrentQueryString() {
/*  79 */     return this.req.queryString().toString();
/*     */   }
/*     */   
/*     */   public String getProtocol() {
/*  83 */     return this.req.protocol().toString();
/*     */   }
/*     */   
/*     */   public String getVirtualHost() {
/*  87 */     return this.req.serverName().toString();
/*     */   }
/*     */   
/*     */   public int getServerPort() {
/*  91 */     return this.req.getServerPort();
/*     */   }
/*     */   
/*     */   public String getRemoteAddr() {
/*  95 */     this.req.action(ActionCode.REQ_HOST_ADDR_ATTRIBUTE, null);
/*  96 */     return this.req.remoteAddr().toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getRemoteAddrForwarded()
/*     */   {
/* 106 */     String remoteAddrProxy = (String)this.req.getAttribute("org.apache.tomcat.remoteAddr");
/* 107 */     if (remoteAddrProxy == null) {
/* 108 */       return getRemoteAddr();
/*     */     }
/* 110 */     return remoteAddrProxy;
/*     */   }
/*     */   
/*     */   public int getContentLength() {
/* 114 */     return this.req.getContentLength();
/*     */   }
/*     */   
/*     */   public long getRequestBytesReceived() {
/* 118 */     return this.req.getBytesRead();
/*     */   }
/*     */   
/*     */   public long getRequestBytesSent() {
/* 122 */     return this.req.getResponse().getContentWritten();
/*     */   }
/*     */   
/*     */ 
/*     */   public long getRequestProcessingTime()
/*     */   {
/* 128 */     long startTime = this.req.getStartTime();
/* 129 */     if ((getStage() == 7) || (startTime < 0L)) {
/* 130 */       return 0L;
/*     */     }
/* 132 */     return System.currentTimeMillis() - startTime;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private long processingTime;
/*     */   
/*     */ 
/*     */   private long maxTime;
/*     */   
/*     */ 
/*     */   private String maxRequestUri;
/*     */   
/*     */ 
/*     */   private int requestCount;
/*     */   
/*     */ 
/*     */   private int errorCount;
/*     */   
/*     */ 
/* 153 */   private long lastRequestProcessingTime = 0L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void updateCounters()
/*     */   {
/* 160 */     this.bytesReceived += this.req.getBytesRead();
/* 161 */     this.bytesSent += this.req.getResponse().getContentWritten();
/*     */     
/* 163 */     this.requestCount += 1;
/* 164 */     if (this.req.getResponse().getStatus() >= 400)
/* 165 */       this.errorCount += 1;
/* 166 */     long t0 = this.req.getStartTime();
/* 167 */     long t1 = System.currentTimeMillis();
/* 168 */     long time = t1 - t0;
/* 169 */     this.lastRequestProcessingTime = time;
/* 170 */     this.processingTime += time;
/* 171 */     if (this.maxTime < time) {
/* 172 */       this.maxTime = time;
/* 173 */       this.maxRequestUri = this.req.requestURI().toString();
/*     */     }
/*     */   }
/*     */   
/*     */   public int getStage() {
/* 178 */     return this.stage;
/*     */   }
/*     */   
/*     */   public void setStage(int stage) {
/* 182 */     this.stage = stage;
/*     */   }
/*     */   
/*     */   public long getBytesSent() {
/* 186 */     return this.bytesSent;
/*     */   }
/*     */   
/*     */   public void setBytesSent(long bytesSent) {
/* 190 */     this.bytesSent = bytesSent;
/*     */   }
/*     */   
/*     */   public long getBytesReceived() {
/* 194 */     return this.bytesReceived;
/*     */   }
/*     */   
/*     */   public void setBytesReceived(long bytesReceived) {
/* 198 */     this.bytesReceived = bytesReceived;
/*     */   }
/*     */   
/*     */   public long getProcessingTime() {
/* 202 */     return this.processingTime;
/*     */   }
/*     */   
/*     */   public void setProcessingTime(long processingTime) {
/* 206 */     this.processingTime = processingTime;
/*     */   }
/*     */   
/*     */   public long getMaxTime() {
/* 210 */     return this.maxTime;
/*     */   }
/*     */   
/*     */   public void setMaxTime(long maxTime) {
/* 214 */     this.maxTime = maxTime;
/*     */   }
/*     */   
/*     */   public String getMaxRequestUri() {
/* 218 */     return this.maxRequestUri;
/*     */   }
/*     */   
/*     */   public void setMaxRequestUri(String maxRequestUri) {
/* 222 */     this.maxRequestUri = maxRequestUri;
/*     */   }
/*     */   
/*     */   public int getRequestCount() {
/* 226 */     return this.requestCount;
/*     */   }
/*     */   
/*     */   public void setRequestCount(int requestCount) {
/* 230 */     this.requestCount = requestCount;
/*     */   }
/*     */   
/*     */   public int getErrorCount() {
/* 234 */     return this.errorCount;
/*     */   }
/*     */   
/*     */   public void setErrorCount(int errorCount) {
/* 238 */     this.errorCount = errorCount;
/*     */   }
/*     */   
/*     */   public String getWorkerThreadName() {
/* 242 */     return this.workerThreadName;
/*     */   }
/*     */   
/*     */   public ObjectName getRpName() {
/* 246 */     return this.rpName;
/*     */   }
/*     */   
/*     */   public long getLastRequestProcessingTime() {
/* 250 */     return this.lastRequestProcessingTime;
/*     */   }
/*     */   
/*     */   public void setWorkerThreadName(String workerThreadName) {
/* 254 */     this.workerThreadName = workerThreadName;
/*     */   }
/*     */   
/*     */   public void setRpName(ObjectName rpName) {
/* 258 */     this.rpName = rpName;
/*     */   }
/*     */   
/*     */   public void setLastRequestProcessingTime(long lastRequestProcessingTime) {
/* 262 */     this.lastRequestProcessingTime = lastRequestProcessingTime;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\RequestInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */