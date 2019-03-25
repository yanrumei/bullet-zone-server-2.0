/*     */ package org.apache.coyote.http11.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.coyote.InputBuffer;
/*     */ import org.apache.coyote.Request;
/*     */ import org.apache.coyote.http11.InputFilter;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.net.ApplicationBufferHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SavedRequestInputFilter
/*     */   implements InputFilter
/*     */ {
/*  37 */   protected ByteChunk input = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SavedRequestInputFilter(ByteChunk input)
/*     */   {
/*  45 */     this.input = input;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public int doRead(ByteChunk chunk)
/*     */     throws IOException
/*     */   {
/*  55 */     if (this.input.getOffset() >= this.input.getEnd()) {
/*  56 */       return -1;
/*     */     }
/*  58 */     int writeLength = 0;
/*     */     
/*  60 */     if ((chunk.getLimit() > 0) && (chunk.getLimit() < this.input.getLength())) {
/*  61 */       writeLength = chunk.getLimit();
/*     */     } else {
/*  63 */       writeLength = this.input.getLength();
/*     */     }
/*     */     
/*  66 */     this.input.substract(chunk.getBuffer(), 0, writeLength);
/*  67 */     chunk.setOffset(0);
/*  68 */     chunk.setEnd(writeLength);
/*     */     
/*  70 */     return writeLength;
/*     */   }
/*     */   
/*     */   public int doRead(ApplicationBufferHandler handler) throws IOException
/*     */   {
/*  75 */     if (this.input.getOffset() >= this.input.getEnd()) {
/*  76 */       return -1;
/*     */     }
/*  78 */     ByteBuffer byteBuffer = handler.getByteBuffer();
/*  79 */     byteBuffer.position(byteBuffer.limit()).limit(byteBuffer.capacity());
/*  80 */     this.input.substract(byteBuffer);
/*     */     
/*  82 */     return byteBuffer.remaining();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRequest(Request request)
/*     */   {
/*  90 */     request.setContentLength(this.input.getLength());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void recycle()
/*     */   {
/*  98 */     this.input = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteChunk getEncodingName()
/*     */   {
/* 106 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBuffer(InputBuffer buffer) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int available()
/*     */   {
/* 122 */     return this.input.getLength();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public long end()
/*     */     throws IOException
/*     */   {
/* 130 */     return 0L;
/*     */   }
/*     */   
/*     */   public boolean isFinished()
/*     */   {
/* 135 */     return this.input.getOffset() >= this.input.getEnd();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\filters\SavedRequestInputFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */