/*     */ package com.fasterxml.jackson.core.io;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonEncoding;
/*     */ import com.fasterxml.jackson.core.util.BufferRecycler;
/*     */ import com.fasterxml.jackson.core.util.TextBuffer;
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
/*     */ public class IOContext
/*     */ {
/*     */   protected final Object _sourceRef;
/*     */   protected JsonEncoding _encoding;
/*     */   protected final boolean _managedResource;
/*     */   protected final BufferRecycler _bufferRecycler;
/*     */   protected byte[] _readIOBuffer;
/*     */   protected byte[] _writeEncodingBuffer;
/*     */   protected byte[] _base64Buffer;
/*     */   protected char[] _tokenCBuffer;
/*     */   protected char[] _concatCBuffer;
/*     */   protected char[] _nameCopyBuffer;
/*     */   
/*     */   public IOContext(BufferRecycler br, Object sourceRef, boolean managedResource)
/*     */   {
/* 103 */     this._bufferRecycler = br;
/* 104 */     this._sourceRef = sourceRef;
/* 105 */     this._managedResource = managedResource;
/*     */   }
/*     */   
/*     */   public void setEncoding(JsonEncoding enc) {
/* 109 */     this._encoding = enc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public IOContext withEncoding(JsonEncoding enc)
/*     */   {
/* 116 */     this._encoding = enc;
/* 117 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 126 */   public Object getSourceReference() { return this._sourceRef; }
/* 127 */   public JsonEncoding getEncoding() { return this._encoding; }
/* 128 */   public boolean isResourceManaged() { return this._managedResource; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TextBuffer constructTextBuffer()
/*     */   {
/* 137 */     return new TextBuffer(this._bufferRecycler);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] allocReadIOBuffer()
/*     */   {
/* 146 */     _verifyAlloc(this._readIOBuffer);
/* 147 */     return this._readIOBuffer = this._bufferRecycler.allocByteBuffer(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public byte[] allocReadIOBuffer(int minSize)
/*     */   {
/* 154 */     _verifyAlloc(this._readIOBuffer);
/* 155 */     return this._readIOBuffer = this._bufferRecycler.allocByteBuffer(0, minSize);
/*     */   }
/*     */   
/*     */   public byte[] allocWriteEncodingBuffer() {
/* 159 */     _verifyAlloc(this._writeEncodingBuffer);
/* 160 */     return this._writeEncodingBuffer = this._bufferRecycler.allocByteBuffer(1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public byte[] allocWriteEncodingBuffer(int minSize)
/*     */   {
/* 167 */     _verifyAlloc(this._writeEncodingBuffer);
/* 168 */     return this._writeEncodingBuffer = this._bufferRecycler.allocByteBuffer(1, minSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public byte[] allocBase64Buffer()
/*     */   {
/* 175 */     _verifyAlloc(this._base64Buffer);
/* 176 */     return this._base64Buffer = this._bufferRecycler.allocByteBuffer(3);
/*     */   }
/*     */   
/*     */   public char[] allocTokenBuffer() {
/* 180 */     _verifyAlloc(this._tokenCBuffer);
/* 181 */     return this._tokenCBuffer = this._bufferRecycler.allocCharBuffer(0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public char[] allocTokenBuffer(int minSize)
/*     */   {
/* 188 */     _verifyAlloc(this._tokenCBuffer);
/* 189 */     return this._tokenCBuffer = this._bufferRecycler.allocCharBuffer(0, minSize);
/*     */   }
/*     */   
/*     */   public char[] allocConcatBuffer() {
/* 193 */     _verifyAlloc(this._concatCBuffer);
/* 194 */     return this._concatCBuffer = this._bufferRecycler.allocCharBuffer(1);
/*     */   }
/*     */   
/*     */   public char[] allocNameCopyBuffer(int minSize) {
/* 198 */     _verifyAlloc(this._nameCopyBuffer);
/* 199 */     return this._nameCopyBuffer = this._bufferRecycler.allocCharBuffer(3, minSize);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void releaseReadIOBuffer(byte[] buf)
/*     */   {
/* 207 */     if (buf != null)
/*     */     {
/*     */ 
/*     */ 
/* 211 */       _verifyRelease(buf, this._readIOBuffer);
/* 212 */       this._readIOBuffer = null;
/* 213 */       this._bufferRecycler.releaseByteBuffer(0, buf);
/*     */     }
/*     */   }
/*     */   
/*     */   public void releaseWriteEncodingBuffer(byte[] buf) {
/* 218 */     if (buf != null)
/*     */     {
/*     */ 
/*     */ 
/* 222 */       _verifyRelease(buf, this._writeEncodingBuffer);
/* 223 */       this._writeEncodingBuffer = null;
/* 224 */       this._bufferRecycler.releaseByteBuffer(1, buf);
/*     */     }
/*     */   }
/*     */   
/*     */   public void releaseBase64Buffer(byte[] buf) {
/* 229 */     if (buf != null) {
/* 230 */       _verifyRelease(buf, this._base64Buffer);
/* 231 */       this._base64Buffer = null;
/* 232 */       this._bufferRecycler.releaseByteBuffer(3, buf);
/*     */     }
/*     */   }
/*     */   
/*     */   public void releaseTokenBuffer(char[] buf) {
/* 237 */     if (buf != null) {
/* 238 */       _verifyRelease(buf, this._tokenCBuffer);
/* 239 */       this._tokenCBuffer = null;
/* 240 */       this._bufferRecycler.releaseCharBuffer(0, buf);
/*     */     }
/*     */   }
/*     */   
/*     */   public void releaseConcatBuffer(char[] buf) {
/* 245 */     if (buf != null)
/*     */     {
/* 247 */       _verifyRelease(buf, this._concatCBuffer);
/* 248 */       this._concatCBuffer = null;
/* 249 */       this._bufferRecycler.releaseCharBuffer(1, buf);
/*     */     }
/*     */   }
/*     */   
/*     */   public void releaseNameCopyBuffer(char[] buf) {
/* 254 */     if (buf != null)
/*     */     {
/* 256 */       _verifyRelease(buf, this._nameCopyBuffer);
/* 257 */       this._nameCopyBuffer = null;
/* 258 */       this._bufferRecycler.releaseCharBuffer(3, buf);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void _verifyAlloc(Object buffer)
/*     */   {
/* 269 */     if (buffer != null) throw new IllegalStateException("Trying to call same allocXxx() method second time");
/*     */   }
/*     */   
/*     */   protected final void _verifyRelease(byte[] toRelease, byte[] src)
/*     */   {
/* 274 */     if ((toRelease != src) && (toRelease.length < src.length)) throw wrongBuf();
/*     */   }
/*     */   
/*     */   protected final void _verifyRelease(char[] toRelease, char[] src)
/*     */   {
/* 279 */     if ((toRelease != src) && (toRelease.length < src.length)) throw wrongBuf();
/*     */   }
/*     */   
/*     */   private IllegalArgumentException wrongBuf()
/*     */   {
/* 284 */     return new IllegalArgumentException("Trying to release buffer smaller than original");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\io\IOContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */