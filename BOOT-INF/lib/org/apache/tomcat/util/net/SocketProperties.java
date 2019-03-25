/*     */ package org.apache.tomcat.util.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.net.StandardSocketOptions;
/*     */ import java.nio.channels.AsynchronousServerSocketChannel;
/*     */ import java.nio.channels.AsynchronousSocketChannel;
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
/*     */ public class SocketProperties
/*     */ {
/*  41 */   protected int processorCache = 500;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  51 */   protected int eventCache = 500;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  57 */   protected boolean directBuffer = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  63 */   protected boolean directSslBuffer = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   protected Integer rxBufSize = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  75 */   protected Integer txBufSize = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */   protected int appReadBufSize = 8192;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  87 */   protected int appWriteBufSize = 8192;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  95 */   protected int bufferPool = 500;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */   protected int bufferPoolSize = 104857600;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 107 */   protected Boolean tcpNoDelay = Boolean.TRUE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 112 */   protected Boolean soKeepAlive = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 117 */   protected Boolean ooBInline = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 122 */   protected Boolean soReuseAddress = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 128 */   protected Boolean soLingerOn = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 134 */   protected Integer soLingerTime = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 139 */   protected Integer soTimeout = Integer.valueOf(20000);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 147 */   protected Integer performanceConnectionTime = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 155 */   protected Integer performanceLatency = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 163 */   protected Integer performanceBandwidth = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 169 */   protected long timeoutInterval = 1000L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 174 */   protected int unlockTimeout = 250;
/*     */   
/*     */   public void setProperties(Socket socket) throws SocketException {
/* 177 */     if (this.rxBufSize != null)
/* 178 */       socket.setReceiveBufferSize(this.rxBufSize.intValue());
/* 179 */     if (this.txBufSize != null)
/* 180 */       socket.setSendBufferSize(this.txBufSize.intValue());
/* 181 */     if (this.ooBInline != null)
/* 182 */       socket.setOOBInline(this.ooBInline.booleanValue());
/* 183 */     if (this.soKeepAlive != null)
/* 184 */       socket.setKeepAlive(this.soKeepAlive.booleanValue());
/* 185 */     if ((this.performanceConnectionTime != null) && (this.performanceLatency != null) && (this.performanceBandwidth != null))
/*     */     {
/* 187 */       socket.setPerformancePreferences(this.performanceConnectionTime
/* 188 */         .intValue(), this.performanceLatency
/* 189 */         .intValue(), this.performanceBandwidth
/* 190 */         .intValue()); }
/* 191 */     if (this.soReuseAddress != null)
/* 192 */       socket.setReuseAddress(this.soReuseAddress.booleanValue());
/* 193 */     if ((this.soLingerOn != null) && (this.soLingerTime != null))
/* 194 */       socket.setSoLinger(this.soLingerOn.booleanValue(), this.soLingerTime
/* 195 */         .intValue());
/* 196 */     if ((this.soTimeout != null) && (this.soTimeout.intValue() >= 0))
/* 197 */       socket.setSoTimeout(this.soTimeout.intValue());
/* 198 */     if (this.tcpNoDelay != null)
/* 199 */       socket.setTcpNoDelay(this.tcpNoDelay.booleanValue());
/*     */   }
/*     */   
/*     */   public void setProperties(ServerSocket socket) throws SocketException {
/* 203 */     if (this.rxBufSize != null)
/* 204 */       socket.setReceiveBufferSize(this.rxBufSize.intValue());
/* 205 */     if ((this.performanceConnectionTime != null) && (this.performanceLatency != null) && (this.performanceBandwidth != null))
/*     */     {
/* 207 */       socket.setPerformancePreferences(this.performanceConnectionTime
/* 208 */         .intValue(), this.performanceLatency
/* 209 */         .intValue(), this.performanceBandwidth
/* 210 */         .intValue()); }
/* 211 */     if (this.soReuseAddress != null)
/* 212 */       socket.setReuseAddress(this.soReuseAddress.booleanValue());
/* 213 */     if ((this.soTimeout != null) && (this.soTimeout.intValue() >= 0))
/* 214 */       socket.setSoTimeout(this.soTimeout.intValue());
/*     */   }
/*     */   
/*     */   public void setProperties(AsynchronousSocketChannel socket) throws IOException {
/* 218 */     if (this.rxBufSize != null)
/* 219 */       socket.setOption(StandardSocketOptions.SO_RCVBUF, this.rxBufSize);
/* 220 */     if (this.txBufSize != null)
/* 221 */       socket.setOption(StandardSocketOptions.SO_SNDBUF, this.txBufSize);
/* 222 */     if (this.soKeepAlive != null)
/* 223 */       socket.setOption(StandardSocketOptions.SO_KEEPALIVE, this.soKeepAlive);
/* 224 */     if (this.soReuseAddress != null)
/* 225 */       socket.setOption(StandardSocketOptions.SO_REUSEADDR, this.soReuseAddress);
/* 226 */     if ((this.soLingerOn != null) && (this.soLingerOn.booleanValue()) && (this.soLingerTime != null))
/* 227 */       socket.setOption(StandardSocketOptions.SO_LINGER, this.soLingerTime);
/* 228 */     if (this.tcpNoDelay != null)
/* 229 */       socket.setOption(StandardSocketOptions.TCP_NODELAY, this.tcpNoDelay);
/*     */   }
/*     */   
/*     */   public void setProperties(AsynchronousServerSocketChannel socket) throws IOException {
/* 233 */     if (this.rxBufSize != null)
/* 234 */       socket.setOption(StandardSocketOptions.SO_RCVBUF, this.rxBufSize);
/* 235 */     if (this.soReuseAddress != null)
/* 236 */       socket.setOption(StandardSocketOptions.SO_REUSEADDR, this.soReuseAddress);
/*     */   }
/*     */   
/*     */   public boolean getDirectBuffer() {
/* 240 */     return this.directBuffer;
/*     */   }
/*     */   
/*     */   public boolean getDirectSslBuffer() {
/* 244 */     return this.directSslBuffer;
/*     */   }
/*     */   
/*     */   public boolean getOoBInline() {
/* 248 */     return this.ooBInline.booleanValue();
/*     */   }
/*     */   
/*     */   public int getPerformanceBandwidth() {
/* 252 */     return this.performanceBandwidth.intValue();
/*     */   }
/*     */   
/*     */   public int getPerformanceConnectionTime() {
/* 256 */     return this.performanceConnectionTime.intValue();
/*     */   }
/*     */   
/*     */   public int getPerformanceLatency() {
/* 260 */     return this.performanceLatency.intValue();
/*     */   }
/*     */   
/*     */   public int getRxBufSize() {
/* 264 */     return this.rxBufSize.intValue();
/*     */   }
/*     */   
/*     */   public boolean getSoKeepAlive() {
/* 268 */     return this.soKeepAlive.booleanValue();
/*     */   }
/*     */   
/*     */   public boolean getSoLingerOn() {
/* 272 */     return this.soLingerOn.booleanValue();
/*     */   }
/*     */   
/*     */   public int getSoLingerTime() {
/* 276 */     return this.soLingerTime.intValue();
/*     */   }
/*     */   
/*     */   public boolean getSoReuseAddress() {
/* 280 */     return this.soReuseAddress.booleanValue();
/*     */   }
/*     */   
/*     */   public int getSoTimeout() {
/* 284 */     return this.soTimeout.intValue();
/*     */   }
/*     */   
/*     */   public boolean getTcpNoDelay() {
/* 288 */     return this.tcpNoDelay.booleanValue();
/*     */   }
/*     */   
/*     */   public int getTxBufSize() {
/* 292 */     return this.txBufSize.intValue();
/*     */   }
/*     */   
/*     */   public int getBufferPool() {
/* 296 */     return this.bufferPool;
/*     */   }
/*     */   
/*     */   public int getBufferPoolSize() {
/* 300 */     return this.bufferPoolSize;
/*     */   }
/*     */   
/*     */   public int getEventCache() {
/* 304 */     return this.eventCache;
/*     */   }
/*     */   
/*     */   public int getAppReadBufSize() {
/* 308 */     return this.appReadBufSize;
/*     */   }
/*     */   
/*     */   public int getAppWriteBufSize() {
/* 312 */     return this.appWriteBufSize;
/*     */   }
/*     */   
/*     */   public int getProcessorCache() {
/* 316 */     return this.processorCache;
/*     */   }
/*     */   
/*     */   public long getTimeoutInterval() {
/* 320 */     return this.timeoutInterval;
/*     */   }
/*     */   
/*     */   public int getDirectBufferPool() {
/* 324 */     return this.bufferPool;
/*     */   }
/*     */   
/*     */   public void setPerformanceConnectionTime(int performanceConnectionTime)
/*     */   {
/* 329 */     this.performanceConnectionTime = Integer.valueOf(performanceConnectionTime);
/*     */   }
/*     */   
/*     */   public void setTxBufSize(int txBufSize) {
/* 333 */     this.txBufSize = Integer.valueOf(txBufSize);
/*     */   }
/*     */   
/*     */   public void setTcpNoDelay(boolean tcpNoDelay) {
/* 337 */     this.tcpNoDelay = Boolean.valueOf(tcpNoDelay);
/*     */   }
/*     */   
/*     */   public void setSoTimeout(int soTimeout) {
/* 341 */     this.soTimeout = Integer.valueOf(soTimeout);
/*     */   }
/*     */   
/*     */   public void setSoReuseAddress(boolean soReuseAddress) {
/* 345 */     this.soReuseAddress = Boolean.valueOf(soReuseAddress);
/*     */   }
/*     */   
/*     */   public void setSoLingerTime(int soLingerTime) {
/* 349 */     this.soLingerTime = Integer.valueOf(soLingerTime);
/*     */   }
/*     */   
/*     */   public void setSoKeepAlive(boolean soKeepAlive) {
/* 353 */     this.soKeepAlive = Boolean.valueOf(soKeepAlive);
/*     */   }
/*     */   
/*     */   public void setRxBufSize(int rxBufSize) {
/* 357 */     this.rxBufSize = Integer.valueOf(rxBufSize);
/*     */   }
/*     */   
/*     */   public void setPerformanceLatency(int performanceLatency) {
/* 361 */     this.performanceLatency = Integer.valueOf(performanceLatency);
/*     */   }
/*     */   
/*     */   public void setPerformanceBandwidth(int performanceBandwidth) {
/* 365 */     this.performanceBandwidth = Integer.valueOf(performanceBandwidth);
/*     */   }
/*     */   
/*     */   public void setOoBInline(boolean ooBInline) {
/* 369 */     this.ooBInline = Boolean.valueOf(ooBInline);
/*     */   }
/*     */   
/*     */   public void setDirectBuffer(boolean directBuffer) {
/* 373 */     this.directBuffer = directBuffer;
/*     */   }
/*     */   
/*     */   public void setDirectSslBuffer(boolean directSslBuffer) {
/* 377 */     this.directSslBuffer = directSslBuffer;
/*     */   }
/*     */   
/*     */   public void setSoLingerOn(boolean soLingerOn) {
/* 381 */     this.soLingerOn = Boolean.valueOf(soLingerOn);
/*     */   }
/*     */   
/*     */   public void setBufferPool(int bufferPool) {
/* 385 */     this.bufferPool = bufferPool;
/*     */   }
/*     */   
/*     */   public void setBufferPoolSize(int bufferPoolSize) {
/* 389 */     this.bufferPoolSize = bufferPoolSize;
/*     */   }
/*     */   
/*     */   public void setEventCache(int eventCache) {
/* 393 */     this.eventCache = eventCache;
/*     */   }
/*     */   
/*     */   public void setAppReadBufSize(int appReadBufSize) {
/* 397 */     this.appReadBufSize = appReadBufSize;
/*     */   }
/*     */   
/*     */   public void setAppWriteBufSize(int appWriteBufSize) {
/* 401 */     this.appWriteBufSize = appWriteBufSize;
/*     */   }
/*     */   
/*     */   public void setProcessorCache(int processorCache) {
/* 405 */     this.processorCache = processorCache;
/*     */   }
/*     */   
/*     */   public void setTimeoutInterval(long timeoutInterval) {
/* 409 */     this.timeoutInterval = timeoutInterval;
/*     */   }
/*     */   
/*     */   public void setDirectBufferPool(int directBufferPool) {
/* 413 */     this.bufferPool = directBufferPool;
/*     */   }
/*     */   
/*     */   public int getUnlockTimeout() {
/* 417 */     return this.unlockTimeout;
/*     */   }
/*     */   
/*     */   public void setUnlockTimeout(int unlockTimeout) {
/* 421 */     this.unlockTimeout = unlockTimeout;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\SocketProperties.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */