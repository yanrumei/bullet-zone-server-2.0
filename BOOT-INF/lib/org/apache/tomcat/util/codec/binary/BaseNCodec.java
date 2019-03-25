/*     */ package org.apache.tomcat.util.codec.binary;
/*     */ 
/*     */ import org.apache.tomcat.util.buf.HexUtils;
/*     */ import org.apache.tomcat.util.codec.BinaryDecoder;
/*     */ import org.apache.tomcat.util.codec.BinaryEncoder;
/*     */ import org.apache.tomcat.util.codec.DecoderException;
/*     */ import org.apache.tomcat.util.codec.EncoderException;
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
/*     */ public abstract class BaseNCodec
/*     */   implements BinaryEncoder, BinaryDecoder
/*     */ {
/*     */   static final int EOF = -1;
/*     */   public static final int MIME_CHUNK_SIZE = 76;
/*     */   public static final int PEM_CHUNK_SIZE = 64;
/*     */   private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
/*     */   private static final int DEFAULT_BUFFER_SIZE = 128;
/*     */   protected static final int MASK_8BITS = 255;
/*     */   protected static final byte PAD_DEFAULT = 61;
/*     */   protected final byte pad;
/*     */   private final int unencodedBlockSize;
/*     */   private final int encodedBlockSize;
/*     */   protected final int lineLength;
/*     */   private final int chunkSeparatorLength;
/*     */   
/*     */   static class Context
/*     */   {
/*     */     int ibitWorkArea;
/*     */     byte[] buffer;
/*     */     int pos;
/*     */     int readPos;
/*     */     boolean eof;
/*     */     int currentLinePos;
/*     */     int modulus;
/*     */     
/*     */     public String toString()
/*     */     {
/*  94 */       return String.format("%s[buffer=%s, currentLinePos=%s, eof=%s, ibitWorkArea=%s, modulus=%s, pos=%s, readPos=%s]", new Object[] {
/*     */       
/*  96 */         getClass().getSimpleName(), 
/*  97 */         HexUtils.toHexString(this.buffer), Integer.valueOf(this.currentLinePos), Boolean.valueOf(this.eof), 
/*  98 */         Integer.valueOf(this.ibitWorkArea), Integer.valueOf(this.modulus), Integer.valueOf(this.pos), Integer.valueOf(this.readPos) });
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
/*     */   protected BaseNCodec(int unencodedBlockSize, int encodedBlockSize, int lineLength, int chunkSeparatorLength)
/*     */   {
/* 179 */     this(unencodedBlockSize, encodedBlockSize, lineLength, chunkSeparatorLength, (byte)61);
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
/*     */   protected BaseNCodec(int unencodedBlockSize, int encodedBlockSize, int lineLength, int chunkSeparatorLength, byte pad)
/*     */   {
/* 193 */     this.unencodedBlockSize = unencodedBlockSize;
/* 194 */     this.encodedBlockSize = encodedBlockSize;
/* 195 */     boolean useChunking = (lineLength > 0) && (chunkSeparatorLength > 0);
/* 196 */     this.lineLength = (useChunking ? lineLength / encodedBlockSize * encodedBlockSize : 0);
/* 197 */     this.chunkSeparatorLength = chunkSeparatorLength;
/*     */     
/* 199 */     this.pad = pad;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   boolean hasData(Context context)
/*     */   {
/* 209 */     return context.buffer != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   int available(Context context)
/*     */   {
/* 219 */     return context.buffer != null ? context.pos - context.readPos : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getDefaultBufferSize()
/*     */   {
/* 228 */     return 128;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private byte[] resizeBuffer(Context context)
/*     */   {
/* 236 */     if (context.buffer == null) {
/* 237 */       context.buffer = new byte[getDefaultBufferSize()];
/* 238 */       context.pos = 0;
/* 239 */       context.readPos = 0;
/*     */     } else {
/* 241 */       byte[] b = new byte[context.buffer.length * 2];
/* 242 */       System.arraycopy(context.buffer, 0, b, 0, context.buffer.length);
/* 243 */       context.buffer = b;
/*     */     }
/* 245 */     return context.buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected byte[] ensureBufferSize(int size, Context context)
/*     */   {
/* 256 */     if ((context.buffer == null) || (context.buffer.length < context.pos + size)) {
/* 257 */       return resizeBuffer(context);
/*     */     }
/* 259 */     return context.buffer;
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
/*     */ 
/*     */ 
/*     */   int readResults(byte[] b, int bPos, int bAvail, Context context)
/*     */   {
/* 279 */     if (context.buffer != null) {
/* 280 */       int len = Math.min(available(context), bAvail);
/* 281 */       System.arraycopy(context.buffer, context.readPos, b, bPos, len);
/* 282 */       context.readPos += len;
/* 283 */       if (context.readPos >= context.pos) {
/* 284 */         context.buffer = null;
/*     */       }
/* 286 */       return len;
/*     */     }
/* 288 */     return context.eof ? -1 : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static boolean isWhiteSpace(byte byteToCheck)
/*     */   {
/* 299 */     switch (byteToCheck) {
/*     */     case 9: 
/*     */     case 10: 
/*     */     case 13: 
/*     */     case 32: 
/* 304 */       return true;
/*     */     }
/* 306 */     return false;
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
/*     */   @Deprecated
/*     */   public Object encode(Object obj)
/*     */     throws EncoderException
/*     */   {
/* 324 */     if (!(obj instanceof byte[])) {
/* 325 */       throw new EncoderException("Parameter supplied to Base-N encode is not a byte[]");
/*     */     }
/* 327 */     return encode((byte[])obj);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String encodeToString(byte[] pArray)
/*     */   {
/* 339 */     return StringUtils.newStringUtf8(encode(pArray));
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
/*     */   public String encodeAsString(byte[] pArray)
/*     */   {
/* 352 */     return StringUtils.newStringUtf8(encode(pArray));
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
/*     */   @Deprecated
/*     */   public Object decode(Object obj)
/*     */     throws DecoderException
/*     */   {
/* 370 */     if ((obj instanceof byte[]))
/* 371 */       return decode((byte[])obj);
/* 372 */     if ((obj instanceof String)) {
/* 373 */       return decode((String)obj);
/*     */     }
/* 375 */     throw new DecoderException("Parameter supplied to Base-N decode is not a byte[] or a String");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] decode(String pArray)
/*     */   {
/* 387 */     return decode(StringUtils.getBytesUtf8(pArray));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] decode(byte[] pArray)
/*     */   {
/* 399 */     return decode(pArray, 0, pArray.length);
/*     */   }
/*     */   
/*     */   public byte[] decode(byte[] pArray, int off, int len) {
/* 403 */     if ((pArray == null) || (len == 0)) {
/* 404 */       return new byte[0];
/*     */     }
/* 406 */     Context context = new Context();
/* 407 */     decode(pArray, off, len, context);
/* 408 */     decode(pArray, off, -1, context);
/* 409 */     byte[] result = new byte[context.pos];
/* 410 */     readResults(result, 0, result.length, context);
/* 411 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] encode(byte[] pArray)
/*     */   {
/* 423 */     if ((pArray == null) || (pArray.length == 0)) {
/* 424 */       return pArray;
/*     */     }
/* 426 */     return encode(pArray, 0, pArray.length);
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
/*     */   public byte[] encode(byte[] pArray, int offset, int length)
/*     */   {
/* 443 */     if ((pArray == null) || (pArray.length == 0)) {
/* 444 */       return pArray;
/*     */     }
/* 446 */     Context context = new Context();
/* 447 */     encode(pArray, offset, length, context);
/* 448 */     encode(pArray, offset, -1, context);
/* 449 */     byte[] buf = new byte[context.pos - context.readPos];
/* 450 */     readResults(buf, 0, buf.length, context);
/* 451 */     return buf;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract void encode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, Context paramContext);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   abstract void decode(byte[] paramArrayOfByte, int paramInt1, int paramInt2, Context paramContext);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean isInAlphabet(byte paramByte);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInAlphabet(byte[] arrayOctet, boolean allowWSPad)
/*     */   {
/* 481 */     for (byte octet : arrayOctet) {
/* 482 */       if ((!isInAlphabet(octet)) && ((!allowWSPad) || ((octet != this.pad) && 
/* 483 */         (!isWhiteSpace(octet))))) {
/* 484 */         return false;
/*     */       }
/*     */     }
/* 487 */     return true;
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
/*     */   public boolean isInAlphabet(String basen)
/*     */   {
/* 500 */     return isInAlphabet(StringUtils.getBytesUtf8(basen), true);
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
/*     */   protected boolean containsAlphabetOrPad(byte[] arrayOctet)
/*     */   {
/* 513 */     if (arrayOctet == null) {
/* 514 */       return false;
/*     */     }
/* 516 */     for (byte element : arrayOctet) {
/* 517 */       if ((this.pad == element) || (isInAlphabet(element))) {
/* 518 */         return true;
/*     */       }
/*     */     }
/* 521 */     return false;
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
/*     */   public long getEncodedLength(byte[] pArray)
/*     */   {
/* 535 */     long len = (pArray.length + this.unencodedBlockSize - 1) / this.unencodedBlockSize * this.encodedBlockSize;
/* 536 */     if (this.lineLength > 0)
/*     */     {
/* 538 */       len += (len + this.lineLength - 1L) / this.lineLength * this.chunkSeparatorLength;
/*     */     }
/* 540 */     return len;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\codec\binary\BaseNCodec.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */