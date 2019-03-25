/*     */ package org.apache.coyote.http11.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.zip.GZIPOutputStream;
/*     */ import org.apache.coyote.Response;
/*     */ import org.apache.coyote.http11.HttpOutputBuffer;
/*     */ import org.apache.coyote.http11.OutputFilter;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GzipOutputFilter
/*     */   implements OutputFilter
/*     */ {
/*  38 */   protected static final Log log = LogFactory.getLog(GzipOutputFilter.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HttpOutputBuffer buffer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  52 */   protected GZIPOutputStream compressionStream = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  58 */   protected final OutputStream fakeOutputStream = new FakeOutputStream();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public int doWrite(ByteChunk chunk)
/*     */     throws IOException
/*     */   {
/*  70 */     if (this.compressionStream == null) {
/*  71 */       this.compressionStream = new GZIPOutputStream(this.fakeOutputStream, true);
/*     */     }
/*  73 */     this.compressionStream.write(chunk.getBytes(), chunk.getStart(), chunk
/*  74 */       .getLength());
/*  75 */     return chunk.getLength();
/*     */   }
/*     */   
/*     */   public int doWrite(ByteBuffer chunk)
/*     */     throws IOException
/*     */   {
/*  81 */     if (this.compressionStream == null) {
/*  82 */       this.compressionStream = new GZIPOutputStream(this.fakeOutputStream, true);
/*     */     }
/*  84 */     int len = chunk.remaining();
/*  85 */     if (chunk.hasArray()) {
/*  86 */       this.compressionStream.write(chunk.array(), chunk.arrayOffset() + chunk.position(), len);
/*     */     } else {
/*  88 */       byte[] bytes = new byte[len];
/*  89 */       chunk.put(bytes);
/*  90 */       this.compressionStream.write(bytes, 0, len);
/*     */     }
/*  92 */     return len;
/*     */   }
/*     */   
/*     */ 
/*     */   public long getBytesWritten()
/*     */   {
/*  98 */     return this.buffer.getBytesWritten();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 109 */     if (this.compressionStream != null) {
/*     */       try {
/* 111 */         if (log.isDebugEnabled()) {
/* 112 */           log.debug("Flushing the compression stream!");
/*     */         }
/* 114 */         this.compressionStream.flush();
/*     */       } catch (IOException e) {
/* 116 */         if (log.isDebugEnabled()) {
/* 117 */           log.debug("Ignored exception while flushing gzip filter", e);
/*     */         }
/*     */       }
/*     */     }
/* 121 */     this.buffer.flush();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResponse(Response response) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void setBuffer(HttpOutputBuffer buffer)
/*     */   {
/* 133 */     this.buffer = buffer;
/*     */   }
/*     */   
/*     */   public void end()
/*     */     throws IOException
/*     */   {
/* 139 */     if (this.compressionStream == null) {
/* 140 */       this.compressionStream = new GZIPOutputStream(this.fakeOutputStream, true);
/*     */     }
/* 142 */     this.compressionStream.finish();
/* 143 */     this.compressionStream.close();
/* 144 */     this.buffer.end();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void recycle()
/*     */   {
/* 154 */     this.compressionStream = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected class FakeOutputStream
/*     */     extends OutputStream
/*     */   {
/* 163 */     protected final ByteBuffer outputChunk = ByteBuffer.allocate(1);
/*     */     
/*     */     protected FakeOutputStream() {}
/*     */     
/*     */     public void write(int b) throws IOException
/*     */     {
/* 169 */       this.outputChunk.put(0, (byte)(b & 0xFF));
/* 170 */       GzipOutputFilter.this.buffer.doWrite(this.outputChunk);
/*     */     }
/*     */     
/*     */     public void write(byte[] b, int off, int len) throws IOException
/*     */     {
/* 175 */       GzipOutputFilter.this.buffer.doWrite(ByteBuffer.wrap(b, off, len));
/*     */     }
/*     */     
/*     */     public void flush()
/*     */       throws IOException
/*     */     {}
/*     */     
/*     */     public void close()
/*     */       throws IOException
/*     */     {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\filters\GzipOutputFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */