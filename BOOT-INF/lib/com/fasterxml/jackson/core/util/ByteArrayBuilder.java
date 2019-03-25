/*     */ package com.fasterxml.jackson.core.util;
/*     */ 
/*     */ import java.io.OutputStream;
/*     */ import java.util.LinkedList;
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
/*     */ public final class ByteArrayBuilder
/*     */   extends OutputStream
/*     */ {
/*  31 */   public static final byte[] NO_BYTES = new byte[0];
/*     */   
/*     */ 
/*     */   private static final int INITIAL_BLOCK_SIZE = 500;
/*     */   
/*     */ 
/*     */   private static final int MAX_BLOCK_SIZE = 262144;
/*     */   
/*     */ 
/*     */   static final int DEFAULT_BLOCK_ARRAY_SIZE = 40;
/*     */   
/*     */   private final BufferRecycler _bufferRecycler;
/*     */   
/*  44 */   private final LinkedList<byte[]> _pastBlocks = new LinkedList();
/*     */   
/*     */   private int _pastLen;
/*     */   
/*     */   private byte[] _currBlock;
/*     */   private int _currBlockPtr;
/*     */   
/*  51 */   public ByteArrayBuilder() { this(null); }
/*  52 */   public ByteArrayBuilder(BufferRecycler br) { this(br, 500); }
/*  53 */   public ByteArrayBuilder(int firstBlockSize) { this(null, firstBlockSize); }
/*     */   
/*     */   public ByteArrayBuilder(BufferRecycler br, int firstBlockSize) {
/*  56 */     this._bufferRecycler = br;
/*  57 */     this._currBlock = (br == null ? new byte[firstBlockSize] : br.allocByteBuffer(2));
/*     */   }
/*     */   
/*     */   public void reset() {
/*  61 */     this._pastLen = 0;
/*  62 */     this._currBlockPtr = 0;
/*     */     
/*  64 */     if (!this._pastBlocks.isEmpty()) {
/*  65 */       this._pastBlocks.clear();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void release()
/*     */   {
/*  75 */     reset();
/*  76 */     if ((this._bufferRecycler != null) && (this._currBlock != null)) {
/*  77 */       this._bufferRecycler.releaseByteBuffer(2, this._currBlock);
/*  78 */       this._currBlock = null;
/*     */     }
/*     */   }
/*     */   
/*     */   public void append(int i) {
/*  83 */     if (this._currBlockPtr >= this._currBlock.length) {
/*  84 */       _allocMore();
/*     */     }
/*  86 */     this._currBlock[(this._currBlockPtr++)] = ((byte)i);
/*     */   }
/*     */   
/*     */   public void appendTwoBytes(int b16) {
/*  90 */     if (this._currBlockPtr + 1 < this._currBlock.length) {
/*  91 */       this._currBlock[(this._currBlockPtr++)] = ((byte)(b16 >> 8));
/*  92 */       this._currBlock[(this._currBlockPtr++)] = ((byte)b16);
/*     */     } else {
/*  94 */       append(b16 >> 8);
/*  95 */       append(b16);
/*     */     }
/*     */   }
/*     */   
/*     */   public void appendThreeBytes(int b24) {
/* 100 */     if (this._currBlockPtr + 2 < this._currBlock.length) {
/* 101 */       this._currBlock[(this._currBlockPtr++)] = ((byte)(b24 >> 16));
/* 102 */       this._currBlock[(this._currBlockPtr++)] = ((byte)(b24 >> 8));
/* 103 */       this._currBlock[(this._currBlockPtr++)] = ((byte)b24);
/*     */     } else {
/* 105 */       append(b24 >> 16);
/* 106 */       append(b24 >> 8);
/* 107 */       append(b24);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] toByteArray()
/*     */   {
/* 117 */     int totalLen = this._pastLen + this._currBlockPtr;
/*     */     
/* 119 */     if (totalLen == 0) {
/* 120 */       return NO_BYTES;
/*     */     }
/*     */     
/* 123 */     byte[] result = new byte[totalLen];
/* 124 */     int offset = 0;
/*     */     
/* 126 */     for (byte[] block : this._pastBlocks) {
/* 127 */       int len = block.length;
/* 128 */       System.arraycopy(block, 0, result, offset, len);
/* 129 */       offset += len;
/*     */     }
/* 131 */     System.arraycopy(this._currBlock, 0, result, offset, this._currBlockPtr);
/* 132 */     offset += this._currBlockPtr;
/* 133 */     if (offset != totalLen) {
/* 134 */       throw new RuntimeException("Internal error: total len assumed to be " + totalLen + ", copied " + offset + " bytes");
/*     */     }
/*     */     
/* 137 */     if (!this._pastBlocks.isEmpty()) {
/* 138 */       reset();
/*     */     }
/* 140 */     return result;
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
/*     */   public byte[] resetAndGetFirstSegment()
/*     */   {
/* 154 */     reset();
/* 155 */     return this._currBlock;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] finishCurrentSegment()
/*     */   {
/* 164 */     _allocMore();
/* 165 */     return this._currBlock;
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
/*     */   public byte[] completeAndCoalesce(int lastBlockLength)
/*     */   {
/* 178 */     this._currBlockPtr = lastBlockLength;
/* 179 */     return toByteArray();
/*     */   }
/*     */   
/* 182 */   public byte[] getCurrentSegment() { return this._currBlock; }
/* 183 */   public void setCurrentSegmentLength(int len) { this._currBlockPtr = len; }
/* 184 */   public int getCurrentSegmentLength() { return this._currBlockPtr; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void write(byte[] b)
/*     */   {
/* 194 */     write(b, 0, b.length);
/*     */   }
/*     */   
/*     */   public void write(byte[] b, int off, int len)
/*     */   {
/*     */     for (;;)
/*     */     {
/* 201 */       int max = this._currBlock.length - this._currBlockPtr;
/* 202 */       int toCopy = Math.min(max, len);
/* 203 */       if (toCopy > 0) {
/* 204 */         System.arraycopy(b, off, this._currBlock, this._currBlockPtr, toCopy);
/* 205 */         off += toCopy;
/* 206 */         this._currBlockPtr += toCopy;
/* 207 */         len -= toCopy;
/*     */       }
/* 209 */       if (len <= 0) break;
/* 210 */       _allocMore();
/*     */     }
/*     */   }
/*     */   
/*     */   public void write(int b)
/*     */   {
/* 216 */     append(b);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public void flush() {}
/*     */   
/*     */ 
/*     */   private void _allocMore()
/*     */   {
/* 230 */     int newPastLen = this._pastLen + this._currBlock.length;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 235 */     if (newPastLen < 0) {
/* 236 */       throw new IllegalStateException("Maximum Java array size (2GB) exceeded by `ByteArrayBuilder`");
/*     */     }
/*     */     
/* 239 */     this._pastLen = newPastLen;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 247 */     int newSize = Math.max(this._pastLen >> 1, 1000);
/*     */     
/* 249 */     if (newSize > 262144) {
/* 250 */       newSize = 262144;
/*     */     }
/* 252 */     this._pastBlocks.add(this._currBlock);
/* 253 */     this._currBlock = new byte[newSize];
/* 254 */     this._currBlockPtr = 0;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\cor\\util\ByteArrayBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */