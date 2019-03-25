/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.Writer;
/*     */ import java.nio.charset.Charset;
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
/*     */ @GwtIncompatible
/*     */ public abstract class ByteSink
/*     */ {
/*     */   public CharSink asCharSink(Charset charset)
/*     */   {
/*  60 */     return new AsCharSink(charset, null);
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
/*     */   public abstract OutputStream openStream()
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public OutputStream openBufferedStream()
/*     */     throws IOException
/*     */   {
/*  86 */     OutputStream out = openStream();
/*  87 */     return (out instanceof BufferedOutputStream) ? (BufferedOutputStream)out : new BufferedOutputStream(out);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void write(byte[] bytes)
/*     */     throws IOException
/*     */   {
/*  98 */     Preconditions.checkNotNull(bytes);
/*     */     
/* 100 */     Closer closer = Closer.create();
/*     */     try {
/* 102 */       OutputStream out = (OutputStream)closer.register(openStream());
/* 103 */       out.write(bytes);
/* 104 */       out.flush();
/*     */     } catch (Throwable e) {
/* 106 */       throw closer.rethrow(e);
/*     */     } finally {
/* 108 */       closer.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public long writeFrom(InputStream input)
/*     */     throws IOException
/*     */   {
/* 121 */     Preconditions.checkNotNull(input);
/*     */     
/* 123 */     Closer closer = Closer.create();
/*     */     try {
/* 125 */       OutputStream out = (OutputStream)closer.register(openStream());
/* 126 */       long written = ByteStreams.copy(input, out);
/* 127 */       out.flush();
/* 128 */       return written;
/*     */     } catch (Throwable e) {
/* 130 */       throw closer.rethrow(e);
/*     */     } finally {
/* 132 */       closer.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private final class AsCharSink
/*     */     extends CharSink
/*     */   {
/*     */     private final Charset charset;
/*     */     
/*     */ 
/*     */     private AsCharSink(Charset charset)
/*     */     {
/* 145 */       this.charset = ((Charset)Preconditions.checkNotNull(charset));
/*     */     }
/*     */     
/*     */     public Writer openStream() throws IOException
/*     */     {
/* 150 */       return new OutputStreamWriter(ByteSink.this.openStream(), this.charset);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 155 */       return ByteSink.this.toString() + ".asCharSink(" + this.charset + ")";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\io\ByteSink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */