/*     */ package org.apache.tomcat.util.buf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Locale;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MessageBytes
/*     */   implements Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  41 */   private int type = 0;
/*     */   
/*     */ 
/*     */   public static final int T_NULL = 0;
/*     */   
/*     */ 
/*     */   public static final int T_STR = 1;
/*     */   
/*     */ 
/*     */   public static final int T_BYTES = 2;
/*     */   
/*     */   public static final int T_CHARS = 3;
/*     */   
/*  54 */   private int hashCode = 0;
/*     */   
/*  56 */   private boolean hasHashCode = false;
/*     */   
/*     */ 
/*  59 */   private final ByteChunk byteC = new ByteChunk();
/*  60 */   private final CharChunk charC = new CharChunk();
/*     */   
/*     */ 
/*     */   private String strValue;
/*     */   
/*     */ 
/*  66 */   private boolean hasStrValue = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private long longValue;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MessageBytes newInstance()
/*     */   {
/*  81 */     return factory.newInstance();
/*     */   }
/*     */   
/*     */   public Object clone() throws CloneNotSupportedException
/*     */   {
/*  86 */     return super.clone();
/*     */   }
/*     */   
/*     */   public boolean isNull() {
/*  90 */     return (this.byteC.isNull()) && (this.charC.isNull()) && (!this.hasStrValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void recycle()
/*     */   {
/*  97 */     this.type = 0;
/*  98 */     this.byteC.recycle();
/*  99 */     this.charC.recycle();
/*     */     
/* 101 */     this.strValue = null;
/*     */     
/* 103 */     this.hasStrValue = false;
/* 104 */     this.hasHashCode = false;
/* 105 */     this.hasLongValue = false;
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
/* 117 */     this.byteC.setBytes(b, off, len);
/* 118 */     this.type = 2;
/* 119 */     this.hasStrValue = false;
/* 120 */     this.hasHashCode = false;
/* 121 */     this.hasLongValue = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setChars(char[] c, int off, int len)
/*     */   {
/* 132 */     this.charC.setChars(c, off, len);
/* 133 */     this.type = 3;
/* 134 */     this.hasStrValue = false;
/* 135 */     this.hasHashCode = false;
/* 136 */     this.hasLongValue = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setString(String s)
/*     */   {
/* 144 */     this.strValue = s;
/* 145 */     this.hasHashCode = false;
/* 146 */     this.hasLongValue = false;
/* 147 */     if (s == null) {
/* 148 */       this.hasStrValue = false;
/* 149 */       this.type = 0;
/*     */     } else {
/* 151 */       this.hasStrValue = true;
/* 152 */       this.type = 1;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 164 */     if (this.hasStrValue) {
/* 165 */       return this.strValue;
/*     */     }
/*     */     
/* 168 */     switch (this.type) {
/*     */     case 3: 
/* 170 */       this.strValue = this.charC.toString();
/* 171 */       this.hasStrValue = true;
/* 172 */       return this.strValue;
/*     */     case 2: 
/* 174 */       this.strValue = this.byteC.toString();
/* 175 */       this.hasStrValue = true;
/* 176 */       return this.strValue;
/*     */     }
/* 178 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getType()
/*     */   {
/* 188 */     return this.type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteChunk getByteChunk()
/*     */   {
/* 197 */     return this.byteC;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CharChunk getCharChunk()
/*     */   {
/* 206 */     return this.charC;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getString()
/*     */   {
/* 215 */     return this.strValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Charset getCharset()
/*     */   {
/* 222 */     return this.byteC.getCharset();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCharset(Charset charset)
/*     */   {
/* 230 */     this.byteC.setCharset(charset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void toBytes()
/*     */   {
/* 237 */     if (!this.byteC.isNull()) {
/* 238 */       this.type = 2;
/* 239 */       return;
/*     */     }
/* 241 */     toString();
/* 242 */     this.type = 2;
/* 243 */     Charset charset = this.byteC.getCharset();
/* 244 */     ByteBuffer result = charset.encode(this.strValue);
/* 245 */     this.byteC.setBytes(result.array(), result.arrayOffset(), result.limit());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void toChars()
/*     */   {
/* 253 */     if (!this.charC.isNull()) {
/* 254 */       this.type = 3;
/* 255 */       return;
/*     */     }
/*     */     
/* 258 */     toString();
/* 259 */     this.type = 3;
/* 260 */     char[] cc = this.strValue.toCharArray();
/* 261 */     this.charC.setChars(cc, 0, cc.length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 272 */     if (this.type == 2) {
/* 273 */       return this.byteC.getLength();
/*     */     }
/* 275 */     if (this.type == 3) {
/* 276 */       return this.charC.getLength();
/*     */     }
/* 278 */     if (this.type == 1) {
/* 279 */       return this.strValue.length();
/*     */     }
/* 281 */     toString();
/* 282 */     if (this.strValue == null) {
/* 283 */       return 0;
/*     */     }
/* 285 */     return this.strValue.length();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(String s)
/*     */   {
/* 296 */     switch (this.type) {
/*     */     case 1: 
/* 298 */       if (this.strValue == null) {
/* 299 */         return s == null;
/*     */       }
/* 301 */       return this.strValue.equals(s);
/*     */     case 3: 
/* 303 */       return this.charC.equals(s);
/*     */     case 2: 
/* 305 */       return this.byteC.equals(s);
/*     */     }
/* 307 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equalsIgnoreCase(String s)
/*     */   {
/* 317 */     switch (this.type) {
/*     */     case 1: 
/* 319 */       if (this.strValue == null) {
/* 320 */         return s == null;
/*     */       }
/* 322 */       return this.strValue.equalsIgnoreCase(s);
/*     */     case 3: 
/* 324 */       return this.charC.equalsIgnoreCase(s);
/*     */     case 2: 
/* 326 */       return this.byteC.equalsIgnoreCase(s);
/*     */     }
/* 328 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 334 */     if ((obj instanceof MessageBytes)) {
/* 335 */       return equals((MessageBytes)obj);
/*     */     }
/* 337 */     return false;
/*     */   }
/*     */   
/*     */   public boolean equals(MessageBytes mb) {
/* 341 */     switch (this.type) {
/*     */     case 1: 
/* 343 */       return mb.equals(this.strValue);
/*     */     }
/*     */     
/* 346 */     if ((mb.type != 3) && (mb.type != 2))
/*     */     {
/*     */ 
/* 349 */       return equals(mb.toString());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 356 */     if ((mb.type == 3) && (this.type == 3)) {
/* 357 */       return this.charC.equals(mb.charC);
/*     */     }
/* 359 */     if ((mb.type == 2) && (this.type == 2)) {
/* 360 */       return this.byteC.equals(mb.byteC);
/*     */     }
/* 362 */     if ((mb.type == 3) && (this.type == 2)) {
/* 363 */       return this.byteC.equals(mb.charC);
/*     */     }
/* 365 */     if ((mb.type == 2) && (this.type == 3)) {
/* 366 */       return mb.byteC.equals(this.charC);
/*     */     }
/*     */     
/* 369 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean startsWithIgnoreCase(String s, int pos)
/*     */   {
/* 379 */     switch (this.type) {
/*     */     case 1: 
/* 381 */       if (this.strValue == null) {
/* 382 */         return false;
/*     */       }
/* 384 */       if (this.strValue.length() < pos + s.length()) {
/* 385 */         return false;
/*     */       }
/*     */       
/* 388 */       for (int i = 0; i < s.length(); i++)
/*     */       {
/* 390 */         if (Ascii.toLower(s.charAt(i)) != Ascii.toLower(this.strValue.charAt(pos + i))) {
/* 391 */           return false;
/*     */         }
/*     */       }
/* 394 */       return true;
/*     */     case 3: 
/* 396 */       return this.charC.startsWithIgnoreCase(s, pos);
/*     */     case 2: 
/* 398 */       return this.byteC.startsWithIgnoreCase(s, pos);
/*     */     }
/* 400 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 408 */     if (this.hasHashCode) {
/* 409 */       return this.hashCode;
/*     */     }
/* 411 */     int code = 0;
/*     */     
/* 413 */     code = hash();
/* 414 */     this.hashCode = code;
/* 415 */     this.hasHashCode = true;
/* 416 */     return code;
/*     */   }
/*     */   
/*     */   private int hash()
/*     */   {
/* 421 */     int code = 0;
/* 422 */     switch (this.type)
/*     */     {
/*     */     case 1: 
/* 425 */       for (int i = 0; i < this.strValue.length(); i++) {
/* 426 */         code = code * 37 + this.strValue.charAt(i);
/*     */       }
/* 428 */       return code;
/*     */     case 3: 
/* 430 */       return this.charC.hash();
/*     */     case 2: 
/* 432 */       return this.byteC.hash();
/*     */     }
/* 434 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int indexOf(String s, int starting)
/*     */   {
/* 441 */     toString();
/* 442 */     return this.strValue.indexOf(s, starting);
/*     */   }
/*     */   
/*     */ 
/*     */   public int indexOf(String s)
/*     */   {
/* 448 */     return indexOf(s, 0);
/*     */   }
/*     */   
/*     */   public int indexOfIgnoreCase(String s, int starting) {
/* 452 */     toString();
/* 453 */     String upper = this.strValue.toUpperCase(Locale.ENGLISH);
/* 454 */     String sU = s.toUpperCase(Locale.ENGLISH);
/* 455 */     return upper.indexOf(sU, starting);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void duplicate(MessageBytes src)
/*     */     throws IOException
/*     */   {
/* 465 */     switch (src.getType()) {
/*     */     case 2: 
/* 467 */       this.type = 2;
/* 468 */       ByteChunk bc = src.getByteChunk();
/* 469 */       this.byteC.allocate(2 * bc.getLength(), -1);
/* 470 */       this.byteC.append(bc);
/* 471 */       break;
/*     */     case 3: 
/* 473 */       this.type = 3;
/* 474 */       CharChunk cc = src.getCharChunk();
/* 475 */       this.charC.allocate(2 * cc.getLength(), -1);
/* 476 */       this.charC.append(cc);
/* 477 */       break;
/*     */     case 1: 
/* 479 */       this.type = 1;
/* 480 */       String sc = src.getString();
/* 481 */       setString(sc);
/*     */     }
/*     */     
/* 484 */     setCharset(src.getCharset());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 491 */   private boolean hasLongValue = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLong(long l)
/*     */   {
/* 498 */     this.byteC.allocate(32, 64);
/* 499 */     long current = l;
/* 500 */     byte[] buf = this.byteC.getBuffer();
/* 501 */     int start = 0;
/* 502 */     int end = 0;
/* 503 */     if (l == 0L) {
/* 504 */       buf[(end++)] = 48;
/*     */     }
/* 506 */     if (l < 0L) {
/* 507 */       current = -l;
/* 508 */       buf[(end++)] = 45;
/*     */     }
/* 510 */     while (current > 0L) {
/* 511 */       int digit = (int)(current % 10L);
/* 512 */       current /= 10L;
/* 513 */       buf[(end++)] = HexUtils.getHex(digit);
/*     */     }
/* 515 */     this.byteC.setOffset(0);
/* 516 */     this.byteC.setEnd(end);
/*     */     
/* 518 */     end--;
/* 519 */     if (l < 0L) {
/* 520 */       start++;
/*     */     }
/* 522 */     while (end > start) {
/* 523 */       byte temp = buf[start];
/* 524 */       buf[start] = buf[end];
/* 525 */       buf[end] = temp;
/* 526 */       start++;
/* 527 */       end--;
/*     */     }
/* 529 */     this.longValue = l;
/* 530 */     this.hasStrValue = false;
/* 531 */     this.hasHashCode = false;
/* 532 */     this.hasLongValue = true;
/* 533 */     this.type = 2;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public long getLong()
/*     */   {
/* 542 */     if (this.hasLongValue) {
/* 543 */       return this.longValue;
/*     */     }
/*     */     
/* 546 */     switch (this.type) {
/*     */     case 2: 
/* 548 */       this.longValue = this.byteC.getLong();
/* 549 */       break;
/*     */     default: 
/* 551 */       this.longValue = Long.parseLong(toString());
/*     */     }
/*     */     
/* 554 */     this.hasLongValue = true;
/* 555 */     return this.longValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 561 */   private static final MessageBytesFactory factory = new MessageBytesFactory();
/*     */   
/*     */   private static class MessageBytesFactory
/*     */   {
/*     */     public MessageBytes newInstance()
/*     */     {
/* 567 */       return new MessageBytes(null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\buf\MessageBytes.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */