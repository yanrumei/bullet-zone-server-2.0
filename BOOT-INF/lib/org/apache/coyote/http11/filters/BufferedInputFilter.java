/*     */ package org.apache.coyote.http11.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.BufferOverflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
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
/*     */ public class BufferedInputFilter
/*     */   implements InputFilter, ApplicationBufferHandler
/*     */ {
/*     */   private static final String ENCODING_NAME = "buffered";
/*  40 */   private static final ByteChunk ENCODING = new ByteChunk();
/*     */   
/*     */   private ByteBuffer buffered;
/*     */   
/*     */   private ByteBuffer tempRead;
/*     */   
/*     */   private InputBuffer buffer;
/*     */   
/*  48 */   private boolean hasRead = false;
/*     */   
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  54 */     ENCODING.setBytes("buffered".getBytes(StandardCharsets.ISO_8859_1), 0, "buffered"
/*  55 */       .length());
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
/*     */   public void setLimit(int limit)
/*     */   {
/*  69 */     if (this.buffered == null) {
/*  70 */       this.buffered = ByteBuffer.allocate(limit);
/*  71 */       this.buffered.flip();
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
/*     */   public void setRequest(Request request)
/*     */   {
/*     */     try
/*     */     {
/*  86 */       while (this.buffer.doRead(this) >= 0) {
/*  87 */         this.buffered.mark().position(this.buffered.limit()).limit(this.buffered.capacity());
/*  88 */         this.buffered.put(this.tempRead);
/*  89 */         this.buffered.limit(this.buffered.position()).reset();
/*  90 */         this.tempRead = null;
/*     */       }
/*     */     }
/*     */     catch (IOException|BufferOverflowException ioe) {
/*  94 */       throw new IllegalStateException("Request body too large for buffer");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public int doRead(ByteChunk chunk)
/*     */     throws IOException
/*     */   {
/* 108 */     if (isFinished()) {
/* 109 */       return -1;
/*     */     }
/*     */     
/* 112 */     chunk.setBytes(this.buffered.array(), this.buffered.arrayOffset() + this.buffered.position(), this.buffered
/* 113 */       .remaining());
/* 114 */     this.hasRead = true;
/* 115 */     return chunk.getLength();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int doRead(ApplicationBufferHandler handler)
/*     */     throws IOException
/*     */   {
/* 123 */     if (isFinished()) {
/* 124 */       return -1;
/*     */     }
/*     */     
/* 127 */     handler.setByteBuffer(this.buffered);
/* 128 */     this.hasRead = true;
/* 129 */     return this.buffered.remaining();
/*     */   }
/*     */   
/*     */   public void setBuffer(InputBuffer buffer)
/*     */   {
/* 134 */     this.buffer = buffer;
/*     */   }
/*     */   
/*     */   public void recycle()
/*     */   {
/* 139 */     if (this.buffered != null) {
/* 140 */       if (this.buffered.capacity() > 65536) {
/* 141 */         this.buffered = null;
/*     */       } else {
/* 143 */         this.buffered.position(0).limit(0);
/*     */       }
/*     */     }
/* 146 */     this.hasRead = false;
/* 147 */     this.buffer = null;
/*     */   }
/*     */   
/*     */   public ByteChunk getEncodingName()
/*     */   {
/* 152 */     return ENCODING;
/*     */   }
/*     */   
/*     */   public long end() throws IOException
/*     */   {
/* 157 */     return 0L;
/*     */   }
/*     */   
/*     */   public int available()
/*     */   {
/* 162 */     return this.buffered.remaining();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isFinished()
/*     */   {
/* 168 */     return (this.hasRead) || (this.buffered.remaining() <= 0);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setByteBuffer(ByteBuffer buffer)
/*     */   {
/* 174 */     this.tempRead = buffer;
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer getByteBuffer()
/*     */   {
/* 180 */     return this.tempRead;
/*     */   }
/*     */   
/*     */   public void expand(int size) {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\filters\BufferedInputFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */