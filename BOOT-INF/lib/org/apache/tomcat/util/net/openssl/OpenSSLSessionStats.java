/*     */ package org.apache.tomcat.util.net.openssl;
/*     */ 
/*     */ import org.apache.tomcat.jni.SSLContext;
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
/*     */ public final class OpenSSLSessionStats
/*     */ {
/*     */   private final long context;
/*     */   
/*     */   OpenSSLSessionStats(long context)
/*     */   {
/*  31 */     this.context = context;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long number()
/*     */   {
/*  38 */     return SSLContext.sessionNumber(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long connect()
/*     */   {
/*  45 */     return SSLContext.sessionConnect(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long connectGood()
/*     */   {
/*  52 */     return SSLContext.sessionConnectGood(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long connectRenegotiate()
/*     */   {
/*  59 */     return SSLContext.sessionConnectRenegotiate(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long accept()
/*     */   {
/*  66 */     return SSLContext.sessionAccept(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long acceptGood()
/*     */   {
/*  73 */     return SSLContext.sessionAcceptGood(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long acceptRenegotiate()
/*     */   {
/*  80 */     return SSLContext.sessionAcceptRenegotiate(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long hits()
/*     */   {
/*  90 */     return SSLContext.sessionHits(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long cbHits()
/*     */   {
/*  98 */     return SSLContext.sessionCbHits(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long misses()
/*     */   {
/* 106 */     return SSLContext.sessionMisses(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long timeouts()
/*     */   {
/* 116 */     return SSLContext.sessionTimeouts(this.context);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long cacheFull()
/*     */   {
/* 124 */     return SSLContext.sessionCacheFull(this.context);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\openssl\OpenSSLSessionStats.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */