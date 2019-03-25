/*     */ package org.apache.coyote.http11.filters;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.coyote.InputBuffer;
/*     */ import org.apache.coyote.Request;
/*     */ import org.apache.coyote.http11.InputFilter;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.net.ApplicationBufferHandler;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class IdentityInputFilter
/*     */   implements InputFilter, ApplicationBufferHandler
/*     */ {
/*  38 */   private static final StringManager sm = StringManager.getManager(IdentityInputFilter.class
/*  39 */     .getPackage().getName());
/*     */   
/*     */ 
/*     */ 
/*     */   protected static final String ENCODING_NAME = "identity";
/*     */   
/*     */ 
/*  46 */   protected static final ByteChunk ENCODING = new ByteChunk();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  53 */     ENCODING.setBytes("identity".getBytes(StandardCharsets.ISO_8859_1), 0, "identity"
/*  54 */       .length());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  64 */   protected long contentLength = -1L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  70 */   protected long remaining = 0L;
/*     */   
/*     */ 
/*     */ 
/*     */   protected InputBuffer buffer;
/*     */   
/*     */ 
/*     */ 
/*     */   protected ByteBuffer tempRead;
/*     */   
/*     */ 
/*     */ 
/*     */   private final int maxSwallowSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public IdentityInputFilter(int maxSwallowSize)
/*     */   {
/*  89 */     this.maxSwallowSize = maxSwallowSize;
/*     */   }
/*     */   
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
/* 103 */     int result = -1;
/*     */     
/* 105 */     if (this.contentLength >= 0L) {
/* 106 */       if (this.remaining > 0L) {
/* 107 */         int nRead = this.buffer.doRead(chunk);
/* 108 */         if (nRead > this.remaining)
/*     */         {
/*     */ 
/*     */ 
/* 112 */           chunk.setBytes(chunk.getBytes(), chunk.getStart(), (int)this.remaining);
/*     */           
/* 114 */           result = (int)this.remaining;
/*     */         } else {
/* 116 */           result = nRead;
/*     */         }
/* 118 */         if (nRead > 0) {
/* 119 */           this.remaining -= nRead;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 124 */         chunk.recycle();
/* 125 */         result = -1;
/*     */       }
/*     */     }
/*     */     
/* 129 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public int doRead(ApplicationBufferHandler handler)
/*     */     throws IOException
/*     */   {
/* 136 */     int result = -1;
/*     */     
/* 138 */     if (this.contentLength >= 0L) {
/* 139 */       if (this.remaining > 0L) {
/* 140 */         int nRead = this.buffer.doRead(handler);
/* 141 */         if (nRead > this.remaining)
/*     */         {
/*     */ 
/*     */ 
/* 145 */           handler.getByteBuffer().limit(handler.getByteBuffer().position() + (int)this.remaining);
/* 146 */           result = (int)this.remaining;
/*     */         } else {
/* 148 */           result = nRead;
/*     */         }
/* 150 */         if (nRead > 0) {
/* 151 */           this.remaining -= nRead;
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 156 */         if (handler.getByteBuffer() != null) {
/* 157 */           handler.getByteBuffer().position(0).limit(0);
/*     */         }
/* 159 */         result = -1;
/*     */       }
/*     */     }
/*     */     
/* 163 */     return result;
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
/*     */   public void setRequest(Request request)
/*     */   {
/* 176 */     this.contentLength = request.getContentLengthLong();
/* 177 */     this.remaining = this.contentLength;
/*     */   }
/*     */   
/*     */ 
/*     */   public long end()
/*     */     throws IOException
/*     */   {
/* 184 */     boolean maxSwallowSizeExceeded = (this.maxSwallowSize > -1) && (this.remaining > this.maxSwallowSize);
/* 185 */     long swallowed = 0L;
/*     */     
/*     */ 
/* 188 */     while (this.remaining > 0L)
/*     */     {
/* 190 */       int nread = this.buffer.doRead(this);
/* 191 */       this.tempRead = null;
/* 192 */       if (nread > 0) {
/* 193 */         swallowed += nread;
/* 194 */         this.remaining -= nread;
/* 195 */         if ((maxSwallowSizeExceeded) && (swallowed > this.maxSwallowSize))
/*     */         {
/*     */ 
/*     */ 
/* 199 */           throw new IOException(sm.getString("inputFilter.maxSwallow"));
/*     */         }
/*     */       } else {
/* 202 */         this.remaining = 0L;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 207 */     return -this.remaining;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int available()
/*     */   {
/* 217 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBuffer(InputBuffer buffer)
/*     */   {
/* 226 */     this.buffer = buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void recycle()
/*     */   {
/* 235 */     this.contentLength = -1L;
/* 236 */     this.remaining = 0L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteChunk getEncodingName()
/*     */   {
/* 246 */     return ENCODING;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFinished()
/*     */   {
/* 254 */     return (this.contentLength > -1L) && (this.remaining <= 0L);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setByteBuffer(ByteBuffer buffer)
/*     */   {
/* 260 */     this.tempRead = buffer;
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer getByteBuffer()
/*     */   {
/* 266 */     return this.tempRead;
/*     */   }
/*     */   
/*     */   public void expand(int size) {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\filters\IdentityInputFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */