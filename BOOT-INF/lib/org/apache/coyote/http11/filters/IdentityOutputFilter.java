/*     */ package org.apache.coyote.http11.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.coyote.Response;
/*     */ import org.apache.coyote.http11.HttpOutputBuffer;
/*     */ import org.apache.coyote.http11.OutputFilter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IdentityOutputFilter
/*     */   implements OutputFilter
/*     */ {
/*  39 */   protected long contentLength = -1L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  45 */   protected long remaining = 0L;
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
/*     */ 
/*     */   @Deprecated
/*     */   public int doWrite(ByteChunk chunk)
/*     */     throws IOException
/*     */   {
/*  64 */     int result = -1;
/*     */     
/*  66 */     if (this.contentLength >= 0L) {
/*  67 */       if (this.remaining > 0L) {
/*  68 */         result = chunk.getLength();
/*  69 */         if (result > this.remaining)
/*     */         {
/*     */ 
/*     */ 
/*  73 */           chunk.setBytes(chunk.getBytes(), chunk.getStart(), (int)this.remaining);
/*     */           
/*  75 */           result = (int)this.remaining;
/*  76 */           this.remaining = 0L;
/*     */         } else {
/*  78 */           this.remaining -= result;
/*     */         }
/*  80 */         this.buffer.doWrite(chunk);
/*     */       }
/*     */       else
/*     */       {
/*  84 */         chunk.recycle();
/*  85 */         result = -1;
/*     */       }
/*     */     }
/*     */     else {
/*  89 */       this.buffer.doWrite(chunk);
/*  90 */       result = chunk.getLength();
/*     */     }
/*     */     
/*  93 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int doWrite(ByteBuffer chunk)
/*     */     throws IOException
/*     */   {
/* 101 */     int result = -1;
/*     */     
/* 103 */     if (this.contentLength >= 0L) {
/* 104 */       if (this.remaining > 0L) {
/* 105 */         result = chunk.remaining();
/* 106 */         if (result > this.remaining)
/*     */         {
/*     */ 
/*     */ 
/* 110 */           chunk.limit(chunk.position() + (int)this.remaining);
/* 111 */           result = (int)this.remaining;
/* 112 */           this.remaining = 0L;
/*     */         } else {
/* 114 */           this.remaining -= result;
/*     */         }
/* 116 */         this.buffer.doWrite(chunk);
/*     */       }
/*     */       else
/*     */       {
/* 120 */         chunk.position(0);
/* 121 */         chunk.limit(0);
/* 122 */         result = -1;
/*     */       }
/*     */     }
/*     */     else {
/* 126 */       result = chunk.remaining();
/* 127 */       this.buffer.doWrite(chunk);
/* 128 */       result -= chunk.remaining();
/*     */     }
/*     */     
/* 131 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long getBytesWritten()
/*     */   {
/* 138 */     return this.buffer.getBytesWritten();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResponse(Response response)
/*     */   {
/* 146 */     this.contentLength = response.getContentLengthLong();
/* 147 */     this.remaining = this.contentLength;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setBuffer(HttpOutputBuffer buffer)
/*     */   {
/* 153 */     this.buffer = buffer;
/*     */   }
/*     */   
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 160 */     this.buffer.flush();
/*     */   }
/*     */   
/*     */   public void end()
/*     */     throws IOException
/*     */   {
/* 166 */     this.buffer.end();
/*     */   }
/*     */   
/*     */ 
/*     */   public void recycle()
/*     */   {
/* 172 */     this.contentLength = -1L;
/* 173 */     this.remaining = 0L;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\filters\IdentityOutputFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */