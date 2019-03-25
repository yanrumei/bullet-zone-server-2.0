/*     */ package com.google.common.io;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.IOException;
/*     */ import java.io.Writer;
/*     */ import java.util.Iterator;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public abstract class CharSink
/*     */ {
/*     */   public abstract Writer openStream()
/*     */     throws IOException;
/*     */   
/*     */   public Writer openBufferedStream()
/*     */     throws IOException
/*     */   {
/*  82 */     Writer writer = openStream();
/*  83 */     return (writer instanceof BufferedWriter) ? (BufferedWriter)writer : new BufferedWriter(writer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void write(CharSequence charSequence)
/*     */     throws IOException
/*     */   {
/*  94 */     Preconditions.checkNotNull(charSequence);
/*     */     
/*  96 */     Closer closer = Closer.create();
/*     */     try {
/*  98 */       Writer out = (Writer)closer.register(openStream());
/*  99 */       out.append(charSequence);
/* 100 */       out.flush();
/*     */     } catch (Throwable e) {
/* 102 */       throw closer.rethrow(e);
/*     */     } finally {
/* 104 */       closer.close();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeLines(Iterable<? extends CharSequence> lines)
/*     */     throws IOException
/*     */   {
/* 116 */     writeLines(lines, System.getProperty("line.separator"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeLines(Iterable<? extends CharSequence> lines, String lineSeparator)
/*     */     throws IOException
/*     */   {
/* 127 */     writeLines(lines.iterator(), lineSeparator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public void writeLines(Stream<? extends CharSequence> lines)
/*     */     throws IOException
/*     */   {
/* 140 */     writeLines(lines, System.getProperty("line.separator"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public void writeLines(Stream<? extends CharSequence> lines, String lineSeparator)
/*     */     throws IOException
/*     */   {
/* 153 */     writeLines(lines.iterator(), lineSeparator);
/*     */   }
/*     */   
/*     */   private void writeLines(Iterator<? extends CharSequence> lines, String lineSeparator) throws IOException
/*     */   {
/* 158 */     Preconditions.checkNotNull(lineSeparator);
/*     */     
/* 160 */     Writer out = openBufferedStream();Throwable localThrowable3 = null;
/* 161 */     try { while (lines.hasNext()) {
/* 162 */         out.append((CharSequence)lines.next()).append(lineSeparator);
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable1)
/*     */     {
/* 160 */       localThrowable3 = localThrowable1;throw localThrowable1;
/*     */     }
/*     */     finally
/*     */     {
/* 164 */       if (out != null) { if (localThrowable3 != null) try { out.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else { out.close();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public long writeFrom(Readable readable)
/*     */     throws IOException
/*     */   {
/* 177 */     Preconditions.checkNotNull(readable);
/*     */     
/* 179 */     Closer closer = Closer.create();
/*     */     try {
/* 181 */       Writer out = (Writer)closer.register(openStream());
/* 182 */       long written = CharStreams.copy(readable, out);
/* 183 */       out.flush();
/* 184 */       return written;
/*     */     } catch (Throwable e) {
/* 186 */       throw closer.rethrow(e);
/*     */     } finally {
/* 188 */       closer.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\io\CharSink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */