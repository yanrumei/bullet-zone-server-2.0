/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.util.Iterator;
/*     */ import javax.annotation.Nullable;
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
/*     */ @GwtIncompatible
/*     */ final class MultiInputStream
/*     */   extends InputStream
/*     */ {
/*     */   private Iterator<? extends ByteSource> it;
/*     */   private InputStream in;
/*     */   
/*     */   public MultiInputStream(Iterator<? extends ByteSource> it)
/*     */     throws IOException
/*     */   {
/*  44 */     this.it = ((Iterator)Preconditions.checkNotNull(it));
/*  45 */     advance();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 6	com/google/common/io/MultiInputStream:in	Ljava/io/InputStream;
/*     */     //   4: ifnull +26 -> 30
/*     */     //   7: aload_0
/*     */     //   8: getfield 6	com/google/common/io/MultiInputStream:in	Ljava/io/InputStream;
/*     */     //   11: invokevirtual 7	java/io/InputStream:close	()V
/*     */     //   14: aload_0
/*     */     //   15: aconst_null
/*     */     //   16: putfield 6	com/google/common/io/MultiInputStream:in	Ljava/io/InputStream;
/*     */     //   19: goto +11 -> 30
/*     */     //   22: astore_1
/*     */     //   23: aload_0
/*     */     //   24: aconst_null
/*     */     //   25: putfield 6	com/google/common/io/MultiInputStream:in	Ljava/io/InputStream;
/*     */     //   28: aload_1
/*     */     //   29: athrow
/*     */     //   30: return
/*     */     // Line number table:
/*     */     //   Java source line #50	-> byte code offset #0
/*     */     //   Java source line #52	-> byte code offset #7
/*     */     //   Java source line #54	-> byte code offset #14
/*     */     //   Java source line #55	-> byte code offset #19
/*     */     //   Java source line #54	-> byte code offset #22
/*     */     //   Java source line #57	-> byte code offset #30
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	31	0	this	MultiInputStream
/*     */     //   22	7	1	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	14	22	finally
/*     */   }
/*     */   
/*     */   private void advance()
/*     */     throws IOException
/*     */   {
/*  63 */     close();
/*  64 */     if (this.it.hasNext()) {
/*  65 */       this.in = ((ByteSource)this.it.next()).openStream();
/*     */     }
/*     */   }
/*     */   
/*     */   public int available() throws IOException
/*     */   {
/*  71 */     if (this.in == null) {
/*  72 */       return 0;
/*     */     }
/*  74 */     return this.in.available();
/*     */   }
/*     */   
/*     */   public boolean markSupported()
/*     */   {
/*  79 */     return false;
/*     */   }
/*     */   
/*     */   public int read() throws IOException
/*     */   {
/*  84 */     if (this.in == null) {
/*  85 */       return -1;
/*     */     }
/*  87 */     int result = this.in.read();
/*  88 */     if (result == -1) {
/*  89 */       advance();
/*  90 */       return read();
/*     */     }
/*  92 */     return result;
/*     */   }
/*     */   
/*     */   public int read(@Nullable byte[] b, int off, int len) throws IOException
/*     */   {
/*  97 */     if (this.in == null) {
/*  98 */       return -1;
/*     */     }
/* 100 */     int result = this.in.read(b, off, len);
/* 101 */     if (result == -1) {
/* 102 */       advance();
/* 103 */       return read(b, off, len);
/*     */     }
/* 105 */     return result;
/*     */   }
/*     */   
/*     */   public long skip(long n) throws IOException
/*     */   {
/* 110 */     if ((this.in == null) || (n <= 0L)) {
/* 111 */       return 0L;
/*     */     }
/* 113 */     long result = this.in.skip(n);
/* 114 */     if (result != 0L) {
/* 115 */       return result;
/*     */     }
/* 117 */     if (read() == -1) {
/* 118 */       return 0L;
/*     */     }
/* 120 */     return 1L + this.in.skip(n - 1L);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\io\MultiInputStream.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */