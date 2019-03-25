/*     */ package org.apache.tomcat.util.http.fileupload;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
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
/*     */ public abstract class ThresholdingOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   private final int threshold;
/*     */   private long written;
/*     */   private boolean thresholdExceeded;
/*     */   
/*     */   public ThresholdingOutputStream(int threshold)
/*     */   {
/*  73 */     this.threshold = threshold;
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
/*     */   public void write(int b)
/*     */     throws IOException
/*     */   {
/*  90 */     checkThreshold(1);
/*  91 */     getStream().write(b);
/*  92 */     this.written += 1L;
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
/*     */   public void write(byte[] b)
/*     */     throws IOException
/*     */   {
/* 107 */     checkThreshold(b.length);
/* 108 */     getStream().write(b);
/* 109 */     this.written += b.length;
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
/*     */   public void write(byte[] b, int off, int len)
/*     */     throws IOException
/*     */   {
/* 126 */     checkThreshold(len);
/* 127 */     getStream().write(b, off, len);
/* 128 */     this.written += len;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 141 */     getStream().flush();
/*     */   }
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
/*     */     try
/*     */     {
/* 156 */       flush();
/*     */     }
/*     */     catch (IOException localIOException) {}
/*     */     
/*     */ 
/*     */ 
/* 162 */     getStream().close();
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
/*     */   public boolean isThresholdExceeded()
/*     */   {
/* 178 */     return this.written > this.threshold;
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
/*     */   protected void checkThreshold(int count)
/*     */     throws IOException
/*     */   {
/* 197 */     if ((!this.thresholdExceeded) && (this.written + count > this.threshold))
/*     */     {
/* 199 */       this.thresholdExceeded = true;
/* 200 */       thresholdReached();
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract OutputStream getStream()
/*     */     throws IOException;
/*     */   
/*     */   protected abstract void thresholdReached()
/*     */     throws IOException;
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\ThresholdingOutputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */