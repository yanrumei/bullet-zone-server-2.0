/*     */ package org.apache.tomcat.util.http.fileupload.util;
/*     */ 
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
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
/*     */ public abstract class LimitedInputStream
/*     */   extends FilterInputStream
/*     */   implements Closeable
/*     */ {
/*     */   private final long sizeMax;
/*     */   private long count;
/*     */   private boolean closed;
/*     */   
/*     */   public LimitedInputStream(InputStream inputStream, long pSizeMax)
/*     */   {
/*  52 */     super(inputStream);
/*  53 */     this.sizeMax = pSizeMax;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void raiseError(long paramLong1, long paramLong2)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void checkLimit()
/*     */     throws IOException
/*     */   {
/*  75 */     if (this.count > this.sizeMax) {
/*  76 */       raiseError(this.sizeMax, this.count);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int read()
/*     */     throws IOException
/*     */   {
/*  99 */     int res = super.read();
/* 100 */     if (res != -1) {
/* 101 */       this.count += 1L;
/* 102 */       checkLimit();
/*     */     }
/* 104 */     return res;
/*     */   }
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
/*     */   public int read(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 132 */     int res = super.read(b, off, len);
/* 133 */     if (res > 0) {
/* 134 */       this.count += res;
/* 135 */       checkLimit();
/*     */     }
/* 137 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isClosed()
/*     */     throws IOException
/*     */   {
/* 148 */     return this.closed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 162 */     this.closed = true;
/* 163 */     super.close();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileuploa\\util\LimitedInputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */