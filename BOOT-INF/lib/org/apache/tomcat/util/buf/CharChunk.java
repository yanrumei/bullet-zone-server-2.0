/*     */ package org.apache.tomcat.util.buf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class CharChunk
/*     */   extends AbstractChunk
/*     */   implements CharSequence
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private char[] buff;
/*  74 */   private transient CharInputChannel in = null;
/*  75 */   private transient CharOutputChannel out = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public CharChunk() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public CharChunk(int initial)
/*     */   {
/*  86 */     allocate(initial, -1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object clone()
/*     */     throws CloneNotSupportedException
/*     */   {
/*  94 */     return super.clone();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void allocate(int initial, int limit)
/*     */   {
/* 101 */     if ((this.buff == null) || (this.buff.length < initial)) {
/* 102 */       this.buff = new char[initial];
/*     */     }
/* 104 */     setLimit(limit);
/* 105 */     this.start = 0;
/* 106 */     this.end = 0;
/* 107 */     this.isSet = true;
/* 108 */     this.hasHashCode = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setChars(char[] c, int off, int len)
/*     */   {
/* 120 */     this.buff = c;
/* 121 */     this.start = off;
/* 122 */     this.end = (this.start + len);
/* 123 */     this.isSet = true;
/* 124 */     this.hasHashCode = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public char[] getChars()
/*     */   {
/* 132 */     return getBuffer();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public char[] getBuffer()
/*     */   {
/* 140 */     return this.buff;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharInputChannel(CharInputChannel in)
/*     */   {
/* 150 */     this.in = in;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharOutputChannel(CharOutputChannel out)
/*     */   {
/* 162 */     this.out = out;
/*     */   }
/*     */   
/*     */ 
/*     */   public void append(char b)
/*     */     throws IOException
/*     */   {
/* 169 */     makeSpace(1);
/* 170 */     int limit = getLimitInternal();
/*     */     
/*     */ 
/* 173 */     if (this.end >= limit) {
/* 174 */       flushBuffer();
/*     */     }
/* 176 */     this.buff[(this.end++)] = b;
/*     */   }
/*     */   
/*     */   public void append(CharChunk src) throws IOException
/*     */   {
/* 181 */     append(src.getBuffer(), src.getOffset(), src.getLength());
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
/*     */   public void append(char[] src, int off, int len)
/*     */     throws IOException
/*     */   {
/* 195 */     makeSpace(len);
/* 196 */     int limit = getLimitInternal();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 202 */     if ((len == limit) && (this.end == this.start) && (this.out != null)) {
/* 203 */       this.out.realWriteChars(src, off, len);
/* 204 */       return;
/*     */     }
/*     */     
/*     */ 
/* 208 */     if (len <= limit - this.end) {
/* 209 */       System.arraycopy(src, off, this.buff, this.end, len);
/* 210 */       this.end += len;
/* 211 */       return;
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
/*     */ 
/*     */ 
/* 224 */     if (len + this.end < 2 * limit)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 230 */       int avail = limit - this.end;
/* 231 */       System.arraycopy(src, off, this.buff, this.end, avail);
/* 232 */       this.end += avail;
/*     */       
/* 234 */       flushBuffer();
/*     */       
/* 236 */       System.arraycopy(src, off + avail, this.buff, this.end, len - avail);
/* 237 */       this.end += len - avail;
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 242 */       flushBuffer();
/*     */       
/* 244 */       this.out.realWriteChars(src, off, len);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void append(String s)
/*     */     throws IOException
/*     */   {
/* 256 */     append(s, 0, s.length());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void append(String s, int off, int len)
/*     */     throws IOException
/*     */   {
/* 269 */     if (s == null) {
/* 270 */       return;
/*     */     }
/*     */     
/*     */ 
/* 274 */     makeSpace(len);
/* 275 */     int limit = getLimitInternal();
/*     */     
/* 277 */     int sOff = off;
/* 278 */     int sEnd = off + len;
/* 279 */     while (sOff < sEnd) {
/* 280 */       int d = min(limit - this.end, sEnd - sOff);
/* 281 */       s.getChars(sOff, sOff + d, this.buff, this.end);
/* 282 */       sOff += d;
/* 283 */       this.end += d;
/* 284 */       if (this.end >= limit) {
/* 285 */         flushBuffer();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int substract()
/*     */     throws IOException
/*     */   {
/* 294 */     if (checkEof()) {
/* 295 */       return -1;
/*     */     }
/* 297 */     return this.buff[(this.start++)];
/*     */   }
/*     */   
/*     */   public int substract(char[] dest, int off, int len) throws IOException
/*     */   {
/* 302 */     if (checkEof()) {
/* 303 */       return -1;
/*     */     }
/* 305 */     int n = len;
/* 306 */     if (len > getLength()) {
/* 307 */       n = getLength();
/*     */     }
/* 309 */     System.arraycopy(this.buff, this.start, dest, off, n);
/* 310 */     this.start += n;
/* 311 */     return n;
/*     */   }
/*     */   
/*     */   private boolean checkEof() throws IOException
/*     */   {
/* 316 */     if (this.end - this.start == 0) {
/* 317 */       if (this.in == null) {
/* 318 */         return true;
/*     */       }
/* 320 */       int n = this.in.realReadChars();
/* 321 */       if (n < 0) {
/* 322 */         return true;
/*     */       }
/*     */     }
/* 325 */     return false;
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
/* 337 */     if (this.out == null) {
/* 338 */       throw new IOException("Buffer overflow, no sink " + getLimit() + " " + this.buff.length);
/*     */     }
/* 340 */     this.out.realWriteChars(this.buff, this.start, this.end - this.start);
/* 341 */     this.end = this.start;
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
/* 352 */     char[] tmp = null;
/*     */     
/* 354 */     int limit = getLimitInternal();
/*     */     
/*     */ 
/* 357 */     long desiredSize = this.end + count;
/*     */     
/*     */ 
/* 360 */     if (desiredSize > limit) {
/* 361 */       desiredSize = limit;
/*     */     }
/*     */     
/* 364 */     if (this.buff == null) {
/* 365 */       if (desiredSize < 256L) {
/* 366 */         desiredSize = 256L;
/*     */       }
/* 368 */       this.buff = new char[(int)desiredSize];
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 373 */     if (desiredSize <= this.buff.length)
/*     */       return;
/*     */     long newSize;
/*     */     long newSize;
/* 377 */     if (desiredSize < 2L * this.buff.length) {
/* 378 */       newSize = this.buff.length * 2L;
/*     */     } else {
/* 380 */       newSize = this.buff.length * 2L + count;
/*     */     }
/*     */     
/* 383 */     if (newSize > limit) {
/* 384 */       newSize = limit;
/*     */     }
/* 386 */     tmp = new char[(int)newSize];
/*     */     
/*     */ 
/* 389 */     System.arraycopy(this.buff, 0, tmp, 0, this.end);
/* 390 */     this.buff = tmp;
/* 391 */     tmp = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 399 */     if (null == this.buff)
/* 400 */       return null;
/* 401 */     if (this.end - this.start == 0) {
/* 402 */       return "";
/*     */     }
/* 404 */     return StringCache.toString(this);
/*     */   }
/*     */   
/*     */   public String toStringInternal()
/*     */   {
/* 409 */     return new String(this.buff, this.start, this.end - this.start);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 417 */     if ((obj instanceof CharChunk)) {
/* 418 */       return equals((CharChunk)obj);
/*     */     }
/* 420 */     return false;
/*     */   }
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
/* 432 */     char[] c = this.buff;
/* 433 */     int len = this.end - this.start;
/* 434 */     if ((c == null) || (len != s.length())) {
/* 435 */       return false;
/*     */     }
/* 437 */     int off = this.start;
/* 438 */     for (int i = 0; i < len; i++) {
/* 439 */       if (c[(off++)] != s.charAt(i)) {
/* 440 */         return false;
/*     */       }
/*     */     }
/* 443 */     return true;
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
/* 455 */     char[] c = this.buff;
/* 456 */     int len = this.end - this.start;
/* 457 */     if ((c == null) || (len != s.length())) {
/* 458 */       return false;
/*     */     }
/* 460 */     int off = this.start;
/* 461 */     for (int i = 0; i < len; i++) {
/* 462 */       if (Ascii.toLower(c[(off++)]) != Ascii.toLower(s.charAt(i))) {
/* 463 */         return false;
/*     */       }
/*     */     }
/* 466 */     return true;
/*     */   }
/*     */   
/*     */   public boolean equals(CharChunk cc)
/*     */   {
/* 471 */     return equals(cc.getChars(), cc.getOffset(), cc.getLength());
/*     */   }
/*     */   
/*     */   public boolean equals(char[] b2, int off2, int len2)
/*     */   {
/* 476 */     char[] b1 = this.buff;
/* 477 */     if ((b1 == null) && (b2 == null)) {
/* 478 */       return true;
/*     */     }
/*     */     
/* 481 */     int len = this.end - this.start;
/* 482 */     if ((len != len2) || (b1 == null) || (b2 == null)) {
/* 483 */       return false;
/*     */     }
/*     */     
/* 486 */     int off1 = this.start;
/*     */     
/* 488 */     while (len-- > 0) {
/* 489 */       if (b1[(off1++)] != b2[(off2++)]) {
/* 490 */         return false;
/*     */       }
/*     */     }
/* 493 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean startsWith(String s)
/*     */   {
/* 503 */     char[] c = this.buff;
/* 504 */     int len = s.length();
/* 505 */     if ((c == null) || (len > this.end - this.start)) {
/* 506 */       return false;
/*     */     }
/* 508 */     int off = this.start;
/* 509 */     for (int i = 0; i < len; i++) {
/* 510 */       if (c[(off++)] != s.charAt(i)) {
/* 511 */         return false;
/*     */       }
/*     */     }
/* 514 */     return true;
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
/* 527 */     char[] c = this.buff;
/* 528 */     int len = s.length();
/* 529 */     if ((c == null) || (len + pos > this.end - this.start)) {
/* 530 */       return false;
/*     */     }
/* 532 */     int off = this.start + pos;
/* 533 */     for (int i = 0; i < len; i++) {
/* 534 */       if (Ascii.toLower(c[(off++)]) != Ascii.toLower(s.charAt(i))) {
/* 535 */         return false;
/*     */       }
/*     */     }
/* 538 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean endsWith(String s)
/*     */   {
/* 548 */     char[] c = this.buff;
/* 549 */     int len = s.length();
/* 550 */     if ((c == null) || (len > this.end - this.start)) {
/* 551 */       return false;
/*     */     }
/* 553 */     int off = this.end - len;
/* 554 */     for (int i = 0; i < len; i++) {
/* 555 */       if (c[(off++)] != s.charAt(i)) {
/* 556 */         return false;
/*     */       }
/*     */     }
/* 559 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   protected int getBufferElement(int index)
/*     */   {
/* 565 */     return this.buff[index];
/*     */   }
/*     */   
/*     */   public int indexOf(char c)
/*     */   {
/* 570 */     return indexOf(c, this.start);
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
/*     */   public int indexOf(char c, int starting)
/*     */   {
/* 585 */     int ret = indexOf(this.buff, this.start + starting, this.end, c);
/* 586 */     return ret >= this.start ? ret - this.start : -1;
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
/*     */   public static int indexOf(char[] chars, int start, int end, char s)
/*     */   {
/* 602 */     int offset = start;
/*     */     
/* 604 */     while (offset < end) {
/* 605 */       char c = chars[offset];
/* 606 */       if (c == s) {
/* 607 */         return offset;
/*     */       }
/* 609 */       offset++;
/*     */     }
/* 611 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */   private int min(int a, int b)
/*     */   {
/* 617 */     if (a < b) {
/* 618 */       return a;
/*     */     }
/* 620 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public char charAt(int index)
/*     */   {
/* 628 */     return this.buff[(index + this.start)];
/*     */   }
/*     */   
/*     */   public CharSequence subSequence(int start, int end)
/*     */   {
/*     */     try
/*     */     {
/* 635 */       CharChunk result = (CharChunk)clone();
/* 636 */       result.setOffset(this.start + start);
/* 637 */       result.setEnd(this.start + end);
/* 638 */       return result;
/*     */     }
/*     */     catch (CloneNotSupportedException e) {}
/* 641 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int length()
/*     */   {
/* 648 */     return this.end - this.start;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public void setOptimizedWrite(boolean optimizedWrite) {}
/*     */   
/*     */   public static abstract interface CharOutputChannel
/*     */   {
/*     */     public abstract void realWriteChars(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */       throws IOException;
/*     */   }
/*     */   
/*     */   public static abstract interface CharInputChannel
/*     */   {
/*     */     public abstract int realReadChars()
/*     */       throws IOException;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\CharChunk.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */