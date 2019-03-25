/*     */ package org.apache.tomcat.util.http.fileupload;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ByteArrayOutputStream
/*     */   extends OutputStream
/*     */ {
/*     */   static final int DEFAULT_SIZE = 1024;
/*  51 */   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
/*     */   
/*     */ 
/*  54 */   private final List<byte[]> buffers = new ArrayList();
/*     */   
/*     */ 
/*     */   private int currentBufferIndex;
/*     */   
/*     */ 
/*     */   private int filledBufferSum;
/*     */   
/*     */   private byte[] currentBuffer;
/*     */   
/*     */   private int count;
/*     */   
/*     */ 
/*     */   public ByteArrayOutputStream()
/*     */   {
/*  69 */     this(1024);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteArrayOutputStream(int size)
/*     */   {
/*  80 */     if (size < 0) {
/*  81 */       throw new IllegalArgumentException("Negative initial size: " + size);
/*     */     }
/*     */     
/*  84 */     synchronized (this) {
/*  85 */       needNewBuffer(size);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void needNewBuffer(int newcount)
/*     */   {
/*  96 */     if (this.currentBufferIndex < this.buffers.size() - 1)
/*     */     {
/*  98 */       this.filledBufferSum += this.currentBuffer.length;
/*     */       
/* 100 */       this.currentBufferIndex += 1;
/* 101 */       this.currentBuffer = ((byte[])this.buffers.get(this.currentBufferIndex));
/*     */     }
/*     */     else {
/*     */       int newBufferSize;
/* 105 */       if (this.currentBuffer == null) {
/* 106 */         int newBufferSize = newcount;
/* 107 */         this.filledBufferSum = 0;
/*     */       } else {
/* 109 */         newBufferSize = Math.max(this.currentBuffer.length << 1, newcount - this.filledBufferSum);
/*     */         
/*     */ 
/* 112 */         this.filledBufferSum += this.currentBuffer.length;
/*     */       }
/*     */       
/* 115 */       this.currentBufferIndex += 1;
/* 116 */       this.currentBuffer = new byte[newBufferSize];
/* 117 */       this.buffers.add(this.currentBuffer);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void write(byte[] b, int off, int len)
/*     */   {
/* 129 */     if ((off < 0) || (off > b.length) || (len < 0) || (off + len > b.length) || (off + len < 0))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 134 */       throw new IndexOutOfBoundsException(); }
/* 135 */     if (len == 0) {
/* 136 */       return;
/*     */     }
/* 138 */     synchronized (this) {
/* 139 */       int newcount = this.count + len;
/* 140 */       int remaining = len;
/* 141 */       int inBufferPos = this.count - this.filledBufferSum;
/* 142 */       while (remaining > 0) {
/* 143 */         int part = Math.min(remaining, this.currentBuffer.length - inBufferPos);
/* 144 */         System.arraycopy(b, off + len - remaining, this.currentBuffer, inBufferPos, part);
/* 145 */         remaining -= part;
/* 146 */         if (remaining > 0) {
/* 147 */           needNewBuffer(newcount);
/* 148 */           inBufferPos = 0;
/*     */         }
/*     */       }
/* 151 */       this.count = newcount;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void write(int b)
/*     */   {
/* 161 */     int inBufferPos = this.count - this.filledBufferSum;
/* 162 */     if (inBufferPos == this.currentBuffer.length) {
/* 163 */       needNewBuffer(this.count + 1);
/* 164 */       inBufferPos = 0;
/*     */     }
/* 166 */     this.currentBuffer[inBufferPos] = ((byte)b);
/* 167 */     this.count += 1;
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
/*     */   public synchronized int write(InputStream in)
/*     */     throws IOException
/*     */   {
/* 182 */     int readCount = 0;
/* 183 */     int inBufferPos = this.count - this.filledBufferSum;
/* 184 */     int n = in.read(this.currentBuffer, inBufferPos, this.currentBuffer.length - inBufferPos);
/* 185 */     while (n != -1) {
/* 186 */       readCount += n;
/* 187 */       inBufferPos += n;
/* 188 */       this.count += n;
/* 189 */       if (inBufferPos == this.currentBuffer.length) {
/* 190 */         needNewBuffer(this.currentBuffer.length);
/* 191 */         inBufferPos = 0;
/*     */       }
/* 193 */       n = in.read(this.currentBuffer, inBufferPos, this.currentBuffer.length - inBufferPos);
/*     */     }
/* 195 */     return readCount;
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
/*     */   public void close()
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized void writeTo(OutputStream out)
/*     */     throws IOException
/*     */   {
/* 220 */     int remaining = this.count;
/* 221 */     for (byte[] buf : this.buffers) {
/* 222 */       int c = Math.min(buf.length, remaining);
/* 223 */       out.write(buf, 0, c);
/* 224 */       remaining -= c;
/* 225 */       if (remaining == 0) {
/*     */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized byte[] toByteArray()
/*     */   {
/* 239 */     int remaining = this.count;
/* 240 */     if (remaining == 0) {
/* 241 */       return EMPTY_BYTE_ARRAY;
/*     */     }
/* 243 */     byte[] newbuf = new byte[remaining];
/* 244 */     int pos = 0;
/* 245 */     for (byte[] buf : this.buffers) {
/* 246 */       int c = Math.min(buf.length, remaining);
/* 247 */       System.arraycopy(buf, 0, newbuf, pos, c);
/* 248 */       pos += c;
/* 249 */       remaining -= c;
/* 250 */       if (remaining == 0) {
/*     */         break;
/*     */       }
/*     */     }
/* 254 */     return newbuf;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\fileupload\ByteArrayOutputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */