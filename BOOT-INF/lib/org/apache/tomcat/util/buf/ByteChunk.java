/*     */ package org.apache.tomcat.util.buf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ByteChunk
/*     */   extends AbstractChunk
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/* 126 */   public static final Charset DEFAULT_CHARSET = StandardCharsets.ISO_8859_1;
/*     */   
/*     */ 
/*     */   private transient Charset charset;
/*     */   
/*     */ 
/*     */   private byte[] buff;
/*     */   
/* 134 */   private transient ByteInputChannel in = null;
/* 135 */   private transient ByteOutputChannel out = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteChunk() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public ByteChunk(int initial)
/*     */   {
/* 146 */     allocate(initial, -1);
/*     */   }
/*     */   
/*     */   private void writeObject(ObjectOutputStream oos) throws IOException
/*     */   {
/* 151 */     oos.defaultWriteObject();
/* 152 */     oos.writeUTF(getCharset().name());
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException
/*     */   {
/* 157 */     ois.defaultReadObject();
/* 158 */     this.charset = Charset.forName(ois.readUTF());
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/* 164 */     return super.clone();
/*     */   }
/*     */   
/*     */ 
/*     */   public void recycle()
/*     */   {
/* 170 */     super.recycle();
/* 171 */     this.charset = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void allocate(int initial, int limit)
/*     */   {
/* 178 */     if ((this.buff == null) || (this.buff.length < initial)) {
/* 179 */       this.buff = new byte[initial];
/*     */     }
/* 181 */     setLimit(limit);
/* 182 */     this.start = 0;
/* 183 */     this.end = 0;
/* 184 */     this.isSet = true;
/* 185 */     this.hasHashCode = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBytes(byte[] b, int off, int len)
/*     */   {
/* 197 */     this.buff = b;
/* 198 */     this.start = off;
/* 199 */     this.end = (this.start + len);
/* 200 */     this.isSet = true;
/* 201 */     this.hasHashCode = false;
/*     */   }
/*     */   
/*     */   public void setCharset(Charset charset)
/*     */   {
/* 206 */     this.charset = charset;
/*     */   }
/*     */   
/*     */   public Charset getCharset()
/*     */   {
/* 211 */     if (this.charset == null) {
/* 212 */       this.charset = DEFAULT_CHARSET;
/*     */     }
/* 214 */     return this.charset;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] getBytes()
/*     */   {
/* 222 */     return getBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public byte[] getBuffer()
/*     */   {
/* 230 */     return this.buff;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setByteInputChannel(ByteInputChannel in)
/*     */   {
/* 240 */     this.in = in;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setByteOutputChannel(ByteOutputChannel out)
/*     */   {
/* 252 */     this.out = out;
/*     */   }
/*     */   
/*     */ 
/*     */   public void append(byte b)
/*     */     throws IOException
/*     */   {
/* 259 */     makeSpace(1);
/* 260 */     int limit = getLimitInternal();
/*     */     
/*     */ 
/* 263 */     if (this.end >= limit) {
/* 264 */       flushBuffer();
/*     */     }
/* 266 */     this.buff[(this.end++)] = b;
/*     */   }
/*     */   
/*     */   public void append(ByteChunk src) throws IOException
/*     */   {
/* 271 */     append(src.getBytes(), src.getStart(), src.getLength());
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
/*     */   public void append(byte[] src, int off, int len)
/*     */     throws IOException
/*     */   {
/* 285 */     makeSpace(len);
/* 286 */     int limit = getLimitInternal();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 292 */     if ((len == limit) && (this.end == this.start) && (this.out != null)) {
/* 293 */       this.out.realWriteBytes(src, off, len);
/* 294 */       return;
/*     */     }
/*     */     
/*     */ 
/* 298 */     if (len <= limit - this.end) {
/* 299 */       System.arraycopy(src, off, this.buff, this.end, len);
/* 300 */       this.end += len;
/* 301 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 311 */     int avail = limit - this.end;
/* 312 */     System.arraycopy(src, off, this.buff, this.end, avail);
/* 313 */     this.end += avail;
/*     */     
/* 315 */     flushBuffer();
/*     */     
/* 317 */     int remain = len - avail;
/*     */     
/* 319 */     while (remain > limit - this.end) {
/* 320 */       this.out.realWriteBytes(src, off + len - remain, limit - this.end);
/* 321 */       remain -= limit - this.end;
/*     */     }
/*     */     
/* 324 */     System.arraycopy(src, off + len - remain, this.buff, this.end, remain);
/* 325 */     this.end += remain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void append(ByteBuffer from)
/*     */     throws IOException
/*     */   {
/* 336 */     int len = from.remaining();
/*     */     
/*     */ 
/* 339 */     makeSpace(len);
/* 340 */     int limit = getLimitInternal();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 346 */     if ((len == limit) && (this.end == this.start) && (this.out != null)) {
/* 347 */       this.out.realWriteBytes(from);
/* 348 */       from.position(from.limit());
/* 349 */       return;
/*     */     }
/*     */     
/* 352 */     if (len <= limit - this.end)
/*     */     {
/*     */ 
/* 355 */       from.get(this.buff, this.end, len);
/* 356 */       this.end += len;
/* 357 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 368 */     int avail = limit - this.end;
/* 369 */     from.get(this.buff, this.end, avail);
/* 370 */     this.end += avail;
/*     */     
/* 372 */     flushBuffer();
/*     */     
/* 374 */     int fromLimit = from.limit();
/* 375 */     int remain = len - avail;
/* 376 */     avail = limit - this.end;
/* 377 */     while (remain >= avail) {
/* 378 */       from.limit(from.position() + avail);
/* 379 */       this.out.realWriteBytes(from);
/* 380 */       from.position(from.limit());
/* 381 */       remain -= avail;
/*     */     }
/*     */     
/* 384 */     from.limit(fromLimit);
/* 385 */     from.get(this.buff, this.end, remain);
/* 386 */     this.end += remain;
/*     */   }
/*     */   
/*     */ 
/*     */   public int substract()
/*     */     throws IOException
/*     */   {
/* 393 */     if (checkEof()) {
/* 394 */       return -1;
/*     */     }
/* 396 */     return this.buff[(this.start++)] & 0xFF;
/*     */   }
/*     */   
/*     */   public byte substractB() throws IOException
/*     */   {
/* 401 */     if (checkEof()) {
/* 402 */       return -1;
/*     */     }
/* 404 */     return this.buff[(this.start++)];
/*     */   }
/*     */   
/*     */   public int substract(byte[] dest, int off, int len) throws IOException
/*     */   {
/* 409 */     if (checkEof()) {
/* 410 */       return -1;
/*     */     }
/* 412 */     int n = len;
/* 413 */     if (len > getLength()) {
/* 414 */       n = getLength();
/*     */     }
/* 416 */     System.arraycopy(this.buff, this.start, dest, off, n);
/* 417 */     this.start += n;
/* 418 */     return n;
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
/*     */   public int substract(ByteBuffer to)
/*     */     throws IOException
/*     */   {
/* 434 */     if (checkEof()) {
/* 435 */       return -1;
/*     */     }
/* 437 */     int n = Math.min(to.remaining(), getLength());
/* 438 */     to.put(this.buff, this.start, n);
/* 439 */     to.limit(to.position());
/* 440 */     to.position(to.position() - n);
/* 441 */     this.start += n;
/* 442 */     return n;
/*     */   }
/*     */   
/*     */   private boolean checkEof() throws IOException
/*     */   {
/* 447 */     if (this.end - this.start == 0) {
/* 448 */       if (this.in == null) {
/* 449 */         return true;
/*     */       }
/* 451 */       int n = this.in.realReadBytes();
/* 452 */       if (n < 0) {
/* 453 */         return true;
/*     */       }
/*     */     }
/* 456 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flushBuffer()
/*     */     throws IOException
/*     */   {
/* 468 */     if (this.out == null) {
/* 469 */       throw new IOException("Buffer overflow, no sink " + getLimit() + " " + this.buff.length);
/*     */     }
/* 471 */     this.out.realWriteBytes(this.buff, this.start, this.end - this.start);
/* 472 */     this.end = this.start;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void makeSpace(int count)
/*     */   {
/* 483 */     byte[] tmp = null;
/*     */     
/* 485 */     int limit = getLimitInternal();
/*     */     
/*     */ 
/* 488 */     long desiredSize = this.end + count;
/*     */     
/*     */ 
/* 491 */     if (desiredSize > limit) {
/* 492 */       desiredSize = limit;
/*     */     }
/*     */     
/* 495 */     if (this.buff == null) {
/* 496 */       if (desiredSize < 256L) {
/* 497 */         desiredSize = 256L;
/*     */       }
/* 499 */       this.buff = new byte[(int)desiredSize];
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 504 */     if (desiredSize <= this.buff.length)
/*     */       return;
/*     */     long newSize;
/*     */     long newSize;
/* 508 */     if (desiredSize < 2L * this.buff.length) {
/* 509 */       newSize = this.buff.length * 2L;
/*     */     } else {
/* 511 */       newSize = this.buff.length * 2L + count;
/*     */     }
/*     */     
/* 514 */     if (newSize > limit) {
/* 515 */       newSize = limit;
/*     */     }
/* 517 */     tmp = new byte[(int)newSize];
/*     */     
/*     */ 
/* 520 */     System.arraycopy(this.buff, this.start, tmp, 0, this.end - this.start);
/* 521 */     this.buff = tmp;
/* 522 */     tmp = null;
/* 523 */     this.end -= this.start;
/* 524 */     this.start = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 532 */     if (null == this.buff)
/* 533 */       return null;
/* 534 */     if (this.end - this.start == 0) {
/* 535 */       return "";
/*     */     }
/* 537 */     return StringCache.toString(this);
/*     */   }
/*     */   
/*     */   public String toStringInternal()
/*     */   {
/* 542 */     if (this.charset == null) {
/* 543 */       this.charset = DEFAULT_CHARSET;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 548 */     CharBuffer cb = this.charset.decode(ByteBuffer.wrap(this.buff, this.start, this.end - this.start));
/* 549 */     return new String(cb.array(), cb.arrayOffset(), cb.length());
/*     */   }
/*     */   
/*     */   public long getLong()
/*     */   {
/* 554 */     return Ascii.parseLong(this.buff, this.start, this.end - this.start);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 562 */     if ((obj instanceof ByteChunk)) {
/* 563 */       return equals((ByteChunk)obj);
/*     */     }
/* 565 */     return false;
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
/*     */   public boolean equals(String s)
/*     */   {
/* 580 */     byte[] b = this.buff;
/* 581 */     int len = this.end - this.start;
/* 582 */     if ((b == null) || (len != s.length())) {
/* 583 */       return false;
/*     */     }
/* 585 */     int off = this.start;
/* 586 */     for (int i = 0; i < len; i++) {
/* 587 */       if (b[(off++)] != s.charAt(i)) {
/* 588 */         return false;
/*     */       }
/*     */     }
/* 591 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equalsIgnoreCase(String s)
/*     */   {
/* 603 */     byte[] b = this.buff;
/* 604 */     int len = this.end - this.start;
/* 605 */     if ((b == null) || (len != s.length())) {
/* 606 */       return false;
/*     */     }
/* 608 */     int off = this.start;
/* 609 */     for (int i = 0; i < len; i++) {
/* 610 */       if (Ascii.toLower(b[(off++)]) != Ascii.toLower(s.charAt(i))) {
/* 611 */         return false;
/*     */       }
/*     */     }
/* 614 */     return true;
/*     */   }
/*     */   
/*     */   public boolean equals(ByteChunk bb)
/*     */   {
/* 619 */     return equals(bb.getBytes(), bb.getStart(), bb.getLength());
/*     */   }
/*     */   
/*     */   public boolean equals(byte[] b2, int off2, int len2)
/*     */   {
/* 624 */     byte[] b1 = this.buff;
/* 625 */     if ((b1 == null) && (b2 == null)) {
/* 626 */       return true;
/*     */     }
/*     */     
/* 629 */     int len = this.end - this.start;
/* 630 */     if ((len != len2) || (b1 == null) || (b2 == null)) {
/* 631 */       return false;
/*     */     }
/*     */     
/* 634 */     int off1 = this.start;
/*     */     
/* 636 */     while (len-- > 0) {
/* 637 */       if (b1[(off1++)] != b2[(off2++)]) {
/* 638 */         return false;
/*     */       }
/*     */     }
/* 641 */     return true;
/*     */   }
/*     */   
/*     */   public boolean equals(CharChunk cc)
/*     */   {
/* 646 */     return equals(cc.getChars(), cc.getStart(), cc.getLength());
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(char[] c2, int off2, int len2)
/*     */   {
/* 652 */     byte[] b1 = this.buff;
/* 653 */     if ((c2 == null) && (b1 == null)) {
/* 654 */       return true;
/*     */     }
/*     */     
/* 657 */     if ((b1 == null) || (c2 == null) || (this.end - this.start != len2)) {
/* 658 */       return false;
/*     */     }
/* 660 */     int off1 = this.start;
/* 661 */     int len = this.end - this.start;
/*     */     
/* 663 */     while (len-- > 0) {
/* 664 */       if ((char)b1[(off1++)] != c2[(off2++)]) {
/* 665 */         return false;
/*     */       }
/*     */     }
/* 668 */     return true;
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
/*     */   public boolean startsWithIgnoreCase(String s, int pos)
/*     */   {
/* 681 */     byte[] b = this.buff;
/* 682 */     int len = s.length();
/* 683 */     if ((b == null) || (len + pos > this.end - this.start)) {
/* 684 */       return false;
/*     */     }
/* 686 */     int off = this.start + pos;
/* 687 */     for (int i = 0; i < len; i++) {
/* 688 */       if (Ascii.toLower(b[(off++)]) != Ascii.toLower(s.charAt(i))) {
/* 689 */         return false;
/*     */       }
/*     */     }
/* 692 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   protected int getBufferElement(int index)
/*     */   {
/* 698 */     return this.buff[index];
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
/*     */   public int indexOf(char c, int starting)
/*     */   {
/* 714 */     int ret = indexOf(this.buff, this.start + starting, this.end, c);
/* 715 */     return ret >= this.start ? ret - this.start : -1;
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
/*     */   public static int indexOf(byte[] bytes, int start, int end, char s)
/*     */   {
/* 732 */     int offset = start;
/*     */     
/* 734 */     while (offset < end) {
/* 735 */       byte b = bytes[offset];
/* 736 */       if (b == s) {
/* 737 */         return offset;
/*     */       }
/* 739 */       offset++;
/*     */     }
/* 741 */     return -1;
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
/*     */   public static int findByte(byte[] bytes, int start, int end, byte b)
/*     */   {
/* 757 */     int offset = start;
/* 758 */     while (offset < end) {
/* 759 */       if (bytes[offset] == b) {
/* 760 */         return offset;
/*     */       }
/* 762 */       offset++;
/*     */     }
/* 764 */     return -1;
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
/*     */   public static int findBytes(byte[] bytes, int start, int end, byte[] b)
/*     */   {
/* 780 */     int blen = b.length;
/* 781 */     int offset = start;
/* 782 */     while (offset < end) {
/* 783 */       for (int i = 0; i < blen; i++) {
/* 784 */         if (bytes[offset] == b[i]) {
/* 785 */           return offset;
/*     */         }
/*     */       }
/* 788 */       offset++;
/*     */     }
/* 790 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final byte[] convertToBytes(String value)
/*     */   {
/* 802 */     byte[] result = new byte[value.length()];
/* 803 */     for (int i = 0; i < value.length(); i++) {
/* 804 */       result[i] = ((byte)value.charAt(i));
/*     */     }
/* 806 */     return result;
/*     */   }
/*     */   
/*     */   public static abstract interface ByteOutputChannel
/*     */   {
/*     */     public abstract void realWriteBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */       throws IOException;
/*     */     
/*     */     public abstract void realWriteBytes(ByteBuffer paramByteBuffer)
/*     */       throws IOException;
/*     */   }
/*     */   
/*     */   public static abstract interface ByteInputChannel
/*     */   {
/*     */     public abstract int realReadBytes()
/*     */       throws IOException;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\ByteChunk.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */