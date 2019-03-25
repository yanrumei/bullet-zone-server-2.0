/*     */ package org.apache.coyote.http11.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.coyote.Response;
/*     */ import org.apache.coyote.http11.HttpOutputBuffer;
/*     */ import org.apache.coyote.http11.OutputFilter;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.buf.HexUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChunkedOutputFilter
/*     */   implements OutputFilter
/*     */ {
/*  38 */   private static final byte[] END_CHUNK_BYTES = { 48, 13, 10, 13, 10 };
/*     */   
/*     */ 
/*     */ 
/*     */   protected HttpOutputBuffer buffer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChunkedOutputFilter()
/*     */   {
/*  49 */     this.chunkHeader.put(8, (byte)13);
/*  50 */     this.chunkHeader.put(9, (byte)10);
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
/*  66 */   protected final ByteBuffer chunkHeader = ByteBuffer.allocate(10);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */   protected final ByteBuffer endChunk = ByteBuffer.wrap(END_CHUNK_BYTES);
/*     */   
/*     */ 
/*     */ 
/*     */ 
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
/*  88 */     int result = chunk.getLength();
/*     */     
/*  90 */     if (result <= 0) {
/*  91 */       return 0;
/*     */     }
/*     */     
/*  94 */     int pos = calculateChunkHeader(result);
/*     */     
/*  96 */     this.chunkHeader.position(pos + 1).limit(this.chunkHeader.position() + 9 - pos);
/*  97 */     this.buffer.doWrite(this.chunkHeader);
/*     */     
/*  99 */     this.buffer.doWrite(chunk);
/*     */     
/* 101 */     this.chunkHeader.position(8).limit(10);
/* 102 */     this.buffer.doWrite(this.chunkHeader);
/*     */     
/* 104 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int doWrite(ByteBuffer chunk)
/*     */     throws IOException
/*     */   {
/* 112 */     int result = chunk.remaining();
/*     */     
/* 114 */     if (result <= 0) {
/* 115 */       return 0;
/*     */     }
/*     */     
/* 118 */     int pos = calculateChunkHeader(result);
/*     */     
/* 120 */     this.chunkHeader.position(pos + 1).limit(this.chunkHeader.position() + 9 - pos);
/* 121 */     this.buffer.doWrite(this.chunkHeader);
/*     */     
/* 123 */     this.buffer.doWrite(chunk);
/*     */     
/* 125 */     this.chunkHeader.position(8).limit(10);
/* 126 */     this.buffer.doWrite(this.chunkHeader);
/*     */     
/* 128 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private int calculateChunkHeader(int len)
/*     */   {
/* 135 */     int pos = 7;
/* 136 */     int current = len;
/* 137 */     while (current > 0) {
/* 138 */       int digit = current % 16;
/* 139 */       current /= 16;
/* 140 */       this.chunkHeader.put(pos--, HexUtils.getHex(digit));
/*     */     }
/* 142 */     return pos;
/*     */   }
/*     */   
/*     */ 
/*     */   public long getBytesWritten()
/*     */   {
/* 148 */     return this.buffer.getBytesWritten();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResponse(Response response) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBuffer(HttpOutputBuffer buffer)
/*     */   {
/* 162 */     this.buffer = buffer;
/*     */   }
/*     */   
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 169 */     this.buffer.flush();
/*     */   }
/*     */   
/*     */ 
/*     */   public void end()
/*     */     throws IOException
/*     */   {
/* 176 */     this.buffer.doWrite(this.endChunk);
/* 177 */     this.endChunk.position(0).limit(this.endChunk.capacity());
/*     */     
/* 179 */     this.buffer.end();
/*     */   }
/*     */   
/*     */   public void recycle() {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\filters\ChunkedOutputFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */