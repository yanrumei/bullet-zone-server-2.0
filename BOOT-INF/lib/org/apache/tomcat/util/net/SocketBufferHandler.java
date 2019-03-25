/*     */ package org.apache.tomcat.util.net;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import org.apache.tomcat.util.buf.ByteBufferUtils;
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
/*     */ public class SocketBufferHandler
/*     */ {
/*  25 */   private volatile boolean readBufferConfiguredForWrite = true;
/*     */   
/*     */   private volatile ByteBuffer readBuffer;
/*  28 */   private volatile boolean writeBufferConfiguredForWrite = true;
/*     */   
/*     */   private volatile ByteBuffer writeBuffer;
/*     */   private final boolean direct;
/*     */   
/*     */   public SocketBufferHandler(int readBufferSize, int writeBufferSize, boolean direct)
/*     */   {
/*  35 */     this.direct = direct;
/*  36 */     if (direct) {
/*  37 */       this.readBuffer = ByteBuffer.allocateDirect(readBufferSize);
/*  38 */       this.writeBuffer = ByteBuffer.allocateDirect(writeBufferSize);
/*     */     } else {
/*  40 */       this.readBuffer = ByteBuffer.allocate(readBufferSize);
/*  41 */       this.writeBuffer = ByteBuffer.allocate(writeBufferSize);
/*     */     }
/*     */   }
/*     */   
/*     */   public void configureReadBufferForWrite()
/*     */   {
/*  47 */     setReadBufferConfiguredForWrite(true);
/*     */   }
/*     */   
/*     */   public void configureReadBufferForRead()
/*     */   {
/*  52 */     setReadBufferConfiguredForWrite(false);
/*     */   }
/*     */   
/*     */ 
/*     */   private void setReadBufferConfiguredForWrite(boolean readBufferConFiguredForWrite)
/*     */   {
/*  58 */     if (this.readBufferConfiguredForWrite != readBufferConFiguredForWrite) {
/*  59 */       if (readBufferConFiguredForWrite)
/*     */       {
/*  61 */         int remaining = this.readBuffer.remaining();
/*  62 */         if (remaining == 0) {
/*  63 */           this.readBuffer.clear();
/*     */         } else {
/*  65 */           this.readBuffer.compact();
/*     */         }
/*     */       }
/*     */       else {
/*  69 */         this.readBuffer.flip();
/*     */       }
/*  71 */       this.readBufferConfiguredForWrite = readBufferConFiguredForWrite;
/*     */     }
/*     */   }
/*     */   
/*     */   public ByteBuffer getReadBuffer()
/*     */   {
/*  77 */     return this.readBuffer;
/*     */   }
/*     */   
/*     */   public boolean isReadBufferEmpty()
/*     */   {
/*  82 */     if (this.readBufferConfiguredForWrite) {
/*  83 */       return this.readBuffer.position() == 0;
/*     */     }
/*  85 */     return this.readBuffer.remaining() == 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void configureWriteBufferForWrite()
/*     */   {
/*  91 */     setWriteBufferConfiguredForWrite(true);
/*     */   }
/*     */   
/*     */   public void configureWriteBufferForRead()
/*     */   {
/*  96 */     setWriteBufferConfiguredForWrite(false);
/*     */   }
/*     */   
/*     */ 
/*     */   private void setWriteBufferConfiguredForWrite(boolean writeBufferConfiguredForWrite)
/*     */   {
/* 102 */     if (this.writeBufferConfiguredForWrite != writeBufferConfiguredForWrite) {
/* 103 */       if (writeBufferConfiguredForWrite)
/*     */       {
/* 105 */         int remaining = this.writeBuffer.remaining();
/* 106 */         if (remaining == 0) {
/* 107 */           this.writeBuffer.clear();
/*     */         } else {
/* 109 */           this.writeBuffer.compact();
/* 110 */           this.writeBuffer.position(remaining);
/* 111 */           this.writeBuffer.limit(this.writeBuffer.capacity());
/*     */         }
/*     */       }
/*     */       else {
/* 115 */         this.writeBuffer.flip();
/*     */       }
/* 117 */       this.writeBufferConfiguredForWrite = writeBufferConfiguredForWrite;
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isWriteBufferWritable()
/*     */   {
/* 123 */     if (this.writeBufferConfiguredForWrite) {
/* 124 */       return this.writeBuffer.hasRemaining();
/*     */     }
/* 126 */     return this.writeBuffer.remaining() == 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer getWriteBuffer()
/*     */   {
/* 132 */     return this.writeBuffer;
/*     */   }
/*     */   
/*     */   public boolean isWriteBufferEmpty()
/*     */   {
/* 137 */     if (this.writeBufferConfiguredForWrite) {
/* 138 */       return this.writeBuffer.position() == 0;
/*     */     }
/* 140 */     return this.writeBuffer.remaining() == 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public void reset()
/*     */   {
/* 146 */     this.readBuffer.clear();
/* 147 */     this.readBufferConfiguredForWrite = true;
/* 148 */     this.writeBuffer.clear();
/* 149 */     this.writeBufferConfiguredForWrite = true;
/*     */   }
/*     */   
/*     */   public void expand(int newSize)
/*     */   {
/* 154 */     configureReadBufferForWrite();
/* 155 */     this.readBuffer = ByteBufferUtils.expand(this.readBuffer, newSize);
/* 156 */     configureWriteBufferForWrite();
/* 157 */     this.writeBuffer = ByteBufferUtils.expand(this.writeBuffer, newSize);
/*     */   }
/*     */   
/*     */   public void free() {
/* 161 */     if (this.direct) {
/* 162 */       ByteBufferUtils.cleanDirectBuffer(this.readBuffer);
/* 163 */       ByteBufferUtils.cleanDirectBuffer(this.writeBuffer);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\net\SocketBufferHandler.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */