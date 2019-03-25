/*     */ package org.apache.coyote.http11.filters;
/*     */ 
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Locale;
/*     */ import java.util.Set;
/*     */ import org.apache.coyote.InputBuffer;
/*     */ import org.apache.coyote.Request;
/*     */ import org.apache.coyote.http11.InputFilter;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.buf.HexUtils;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.http.MimeHeaders;
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
/*     */ 
/*     */ public class ChunkedInputFilter
/*     */   implements InputFilter, ApplicationBufferHandler
/*     */ {
/*  45 */   private static final StringManager sm = StringManager.getManager(ChunkedInputFilter.class
/*  46 */     .getPackage().getName());
/*     */   
/*     */ 
/*     */   protected static final String ENCODING_NAME = "chunked";
/*     */   
/*     */ 
/*  52 */   protected static final ByteChunk ENCODING = new ByteChunk();
/*     */   
/*     */   protected InputBuffer buffer;
/*     */   
/*     */   static
/*     */   {
/*  58 */     ENCODING.setBytes("chunked".getBytes(StandardCharsets.ISO_8859_1), 0, "chunked"
/*  59 */       .length());
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
/*  74 */   protected int remaining = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ByteBuffer readChunk;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */   protected boolean endChunk = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  92 */   protected final ByteChunk trailingHeaders = new ByteChunk();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */   protected boolean needCRLFParse = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Request request;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final long maxExtensionSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int maxTrailerSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private long extensionSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final int maxSwallowSize;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean error;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Set<String> allowedTrailerHeaders;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ChunkedInputFilter(int maxTrailerSize, Set<String> allowedTrailerHeaders, int maxExtensionSize, int maxSwallowSize)
/*     */   {
/* 141 */     this.trailingHeaders.setLimit(maxTrailerSize);
/* 142 */     this.allowedTrailerHeaders = allowedTrailerHeaders;
/* 143 */     this.maxExtensionSize = maxExtensionSize;
/* 144 */     this.maxTrailerSize = maxTrailerSize;
/* 145 */     this.maxSwallowSize = maxSwallowSize;
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
/* 158 */     if (this.endChunk) {
/* 159 */       return -1;
/*     */     }
/*     */     
/* 162 */     checkError();
/*     */     
/* 164 */     if (this.needCRLFParse) {
/* 165 */       this.needCRLFParse = false;
/* 166 */       parseCRLF(false);
/*     */     }
/*     */     
/* 169 */     if (this.remaining <= 0) {
/* 170 */       if (!parseChunkHeader()) {
/* 171 */         throwIOException(sm.getString("chunkedInputFilter.invalidHeader"));
/*     */       }
/* 173 */       if (this.endChunk) {
/* 174 */         parseEndChunk();
/* 175 */         return -1;
/*     */       }
/*     */     }
/*     */     
/* 179 */     int result = 0;
/*     */     
/* 181 */     if (((this.readChunk == null) || (this.readChunk.position() >= this.readChunk.limit())) && 
/* 182 */       (readBytes() < 0)) {
/* 183 */       throwIOException(sm.getString("chunkedInputFilter.eos"));
/*     */     }
/*     */     
/*     */ 
/* 187 */     if (this.remaining > this.readChunk.remaining()) {
/* 188 */       result = this.readChunk.remaining();
/* 189 */       this.remaining -= result;
/* 190 */       chunk.setBytes(this.readChunk.array(), this.readChunk.arrayOffset() + this.readChunk.position(), result);
/* 191 */       this.readChunk.position(this.readChunk.limit());
/*     */     } else {
/* 193 */       result = this.remaining;
/* 194 */       chunk.setBytes(this.readChunk.array(), this.readChunk.arrayOffset() + this.readChunk.position(), this.remaining);
/* 195 */       this.readChunk.position(this.readChunk.position() + this.remaining);
/* 196 */       this.remaining = 0;
/*     */       
/* 198 */       if (this.readChunk.position() + 1 >= this.readChunk.limit())
/*     */       {
/*     */ 
/* 201 */         this.needCRLFParse = true;
/*     */       } else {
/* 203 */         parseCRLF(false);
/*     */       }
/*     */     }
/*     */     
/* 207 */     return result;
/*     */   }
/*     */   
/*     */   public int doRead(ApplicationBufferHandler handler) throws IOException
/*     */   {
/* 212 */     if (this.endChunk) {
/* 213 */       return -1;
/*     */     }
/*     */     
/* 216 */     checkError();
/*     */     
/* 218 */     if (this.needCRLFParse) {
/* 219 */       this.needCRLFParse = false;
/* 220 */       parseCRLF(false);
/*     */     }
/*     */     
/* 223 */     if (this.remaining <= 0) {
/* 224 */       if (!parseChunkHeader()) {
/* 225 */         throwIOException(sm.getString("chunkedInputFilter.invalidHeader"));
/*     */       }
/* 227 */       if (this.endChunk) {
/* 228 */         parseEndChunk();
/* 229 */         return -1;
/*     */       }
/*     */     }
/*     */     
/* 233 */     int result = 0;
/*     */     
/* 235 */     if (((this.readChunk == null) || (this.readChunk.position() >= this.readChunk.limit())) && 
/* 236 */       (readBytes() < 0)) {
/* 237 */       throwIOException(sm.getString("chunkedInputFilter.eos"));
/*     */     }
/*     */     
/*     */ 
/* 241 */     if (this.remaining > this.readChunk.remaining()) {
/* 242 */       result = this.readChunk.remaining();
/* 243 */       this.remaining -= result;
/* 244 */       if (this.readChunk != handler.getByteBuffer()) {
/* 245 */         handler.setByteBuffer(this.readChunk.duplicate());
/*     */       }
/* 247 */       this.readChunk.position(this.readChunk.limit());
/*     */     } else {
/* 249 */       result = this.remaining;
/* 250 */       if (this.readChunk != handler.getByteBuffer()) {
/* 251 */         handler.setByteBuffer(this.readChunk.duplicate());
/* 252 */         handler.getByteBuffer().limit(this.readChunk.position() + this.remaining);
/*     */       }
/* 254 */       this.readChunk.position(this.readChunk.position() + this.remaining);
/* 255 */       this.remaining = 0;
/*     */       
/* 257 */       if (this.readChunk.position() + 1 >= this.readChunk.limit())
/*     */       {
/*     */ 
/* 260 */         this.needCRLFParse = true;
/*     */       } else {
/* 262 */         parseCRLF(false);
/*     */       }
/*     */     }
/*     */     
/* 266 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRequest(Request request)
/*     */   {
/* 277 */     this.request = request;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public long end()
/*     */     throws IOException
/*     */   {
/* 286 */     long swallowed = 0L;
/* 287 */     int read = 0;
/*     */     
/* 289 */     while ((read = doRead(this)) >= 0) {
/* 290 */       swallowed += read;
/* 291 */       if ((this.maxSwallowSize > -1) && (swallowed > this.maxSwallowSize)) {
/* 292 */         throwIOException(sm.getString("inputFilter.maxSwallow"));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 297 */     return this.readChunk.remaining();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int available()
/*     */   {
/* 306 */     return this.readChunk != null ? this.readChunk.remaining() : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBuffer(InputBuffer buffer)
/*     */   {
/* 315 */     this.buffer = buffer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void recycle()
/*     */   {
/* 324 */     this.remaining = 0;
/* 325 */     if (this.readChunk != null) {
/* 326 */       this.readChunk.position(0).limit(0);
/*     */     }
/* 328 */     this.endChunk = false;
/* 329 */     this.needCRLFParse = false;
/* 330 */     this.trailingHeaders.recycle();
/* 331 */     this.trailingHeaders.setLimit(this.maxTrailerSize);
/* 332 */     this.extensionSize = 0L;
/* 333 */     this.error = false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ByteChunk getEncodingName()
/*     */   {
/* 343 */     return ENCODING;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isFinished()
/*     */   {
/* 349 */     return this.endChunk;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int readBytes()
/*     */     throws IOException
/*     */   {
/* 361 */     return this.buffer.doRead(this);
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
/*     */   protected boolean parseChunkHeader()
/*     */     throws IOException
/*     */   {
/* 381 */     int result = 0;
/* 382 */     boolean eol = false;
/* 383 */     int readDigit = 0;
/* 384 */     boolean extension = false;
/*     */     
/* 386 */     while (!eol)
/*     */     {
/* 388 */       if (((this.readChunk == null) || (this.readChunk.position() >= this.readChunk.limit())) && 
/* 389 */         (readBytes() <= 0)) {
/* 390 */         return false;
/*     */       }
/*     */       
/* 393 */       byte chr = this.readChunk.get(this.readChunk.position());
/* 394 */       if ((chr == 13) || (chr == 10)) {
/* 395 */         parseCRLF(false);
/* 396 */         eol = true;
/* 397 */       } else if ((chr == 59) && (!extension))
/*     */       {
/*     */ 
/*     */ 
/* 401 */         extension = true;
/* 402 */         this.extensionSize += 1L;
/* 403 */       } else if (!extension)
/*     */       {
/* 405 */         int charValue = HexUtils.getDec(chr);
/* 406 */         if ((charValue != -1) && (readDigit < 8)) {
/* 407 */           readDigit++;
/* 408 */           result = result << 4 | charValue;
/*     */         }
/*     */         else
/*     */         {
/* 412 */           return false;
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 418 */         this.extensionSize += 1L;
/* 419 */         if ((this.maxExtensionSize > -1L) && (this.extensionSize > this.maxExtensionSize)) {
/* 420 */           throwIOException(sm.getString("chunkedInputFilter.maxExtension"));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 425 */       if (!eol) {
/* 426 */         this.readChunk.position(this.readChunk.position() + 1);
/*     */       }
/*     */     }
/*     */     
/* 430 */     if ((readDigit == 0) || (result < 0)) {
/* 431 */       return false;
/*     */     }
/*     */     
/* 434 */     if (result == 0) {
/* 435 */       this.endChunk = true;
/*     */     }
/*     */     
/* 438 */     this.remaining = result;
/* 439 */     return true;
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
/*     */   protected void parseCRLF(boolean tolerant)
/*     */     throws IOException
/*     */   {
/* 453 */     boolean eol = false;
/* 454 */     boolean crfound = false;
/*     */     
/* 456 */     while (!eol) {
/* 457 */       if (((this.readChunk == null) || (this.readChunk.position() >= this.readChunk.limit())) && 
/* 458 */         (readBytes() <= 0)) {
/* 459 */         throwIOException(sm.getString("chunkedInputFilter.invalidCrlfNoData"));
/*     */       }
/*     */       
/*     */ 
/* 463 */       byte chr = this.readChunk.get(this.readChunk.position());
/* 464 */       if (chr == 13) {
/* 465 */         if (crfound) {
/* 466 */           throwIOException(sm.getString("chunkedInputFilter.invalidCrlfCRCR"));
/*     */         }
/* 468 */         crfound = true;
/* 469 */       } else if (chr == 10) {
/* 470 */         if ((!tolerant) && (!crfound)) {
/* 471 */           throwIOException(sm.getString("chunkedInputFilter.invalidCrlfNoCR"));
/*     */         }
/* 473 */         eol = true;
/*     */       } else {
/* 475 */         throwIOException(sm.getString("chunkedInputFilter.invalidCrlf"));
/*     */       }
/*     */       
/* 478 */       this.readChunk.position(this.readChunk.position() + 1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void parseEndChunk()
/*     */     throws IOException
/*     */   {
/* 489 */     while (parseHeader()) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean parseHeader()
/*     */     throws IOException
/*     */   {
/* 497 */     MimeHeaders headers = this.request.getMimeHeaders();
/*     */     
/* 499 */     byte chr = 0;
/*     */     
/*     */ 
/* 502 */     if (((this.readChunk == null) || (this.readChunk.position() >= this.readChunk.limit())) && 
/* 503 */       (readBytes() < 0)) {
/* 504 */       throwEOFException(sm.getString("chunkedInputFilter.eosTrailer"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 509 */     chr = this.readChunk.get(this.readChunk.position());
/*     */     
/*     */ 
/* 512 */     if ((chr == 13) || (chr == 10)) {
/* 513 */       parseCRLF(false);
/* 514 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 518 */     int startPos = this.trailingHeaders.getEnd();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 525 */     boolean colon = false;
/* 526 */     while (!colon)
/*     */     {
/*     */ 
/* 529 */       if (((this.readChunk == null) || (this.readChunk.position() >= this.readChunk.limit())) && 
/* 530 */         (readBytes() < 0)) {
/* 531 */         throwEOFException(sm.getString("chunkedInputFilter.eosTrailer"));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 536 */       chr = this.readChunk.get(this.readChunk.position());
/* 537 */       if ((chr >= 65) && (chr <= 90)) {
/* 538 */         chr = (byte)(chr - -32);
/*     */       }
/*     */       
/* 541 */       if (chr == 58) {
/* 542 */         colon = true;
/*     */       } else {
/* 544 */         this.trailingHeaders.append(chr);
/*     */       }
/*     */       
/* 547 */       this.readChunk.position(this.readChunk.position() + 1);
/*     */     }
/*     */     
/* 550 */     int colonPos = this.trailingHeaders.getEnd();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 556 */     boolean eol = false;
/* 557 */     boolean validLine = true;
/* 558 */     int lastSignificantChar = 0;
/*     */     
/* 560 */     while (validLine)
/*     */     {
/* 562 */       boolean space = true;
/*     */       
/*     */ 
/* 565 */       while (space)
/*     */       {
/*     */ 
/* 568 */         if (((this.readChunk == null) || (this.readChunk.position() >= this.readChunk.limit())) && 
/* 569 */           (readBytes() < 0)) {
/* 570 */           throwEOFException(sm.getString("chunkedInputFilter.eosTrailer"));
/*     */         }
/*     */         
/*     */ 
/* 574 */         chr = this.readChunk.get(this.readChunk.position());
/* 575 */         if ((chr == 32) || (chr == 9)) {
/* 576 */           this.readChunk.position(this.readChunk.position() + 1);
/*     */           
/*     */ 
/* 579 */           int newlimit = this.trailingHeaders.getLimit() - 1;
/* 580 */           if (this.trailingHeaders.getEnd() > newlimit) {
/* 581 */             throwIOException(sm.getString("chunkedInputFilter.maxTrailer"));
/*     */           }
/* 583 */           this.trailingHeaders.setLimit(newlimit);
/*     */         } else {
/* 585 */           space = false;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 591 */       while (!eol)
/*     */       {
/*     */ 
/* 594 */         if (((this.readChunk == null) || (this.readChunk.position() >= this.readChunk.limit())) && 
/* 595 */           (readBytes() < 0)) {
/* 596 */           throwEOFException(sm.getString("chunkedInputFilter.eosTrailer"));
/*     */         }
/*     */         
/*     */ 
/* 600 */         chr = this.readChunk.get(this.readChunk.position());
/* 601 */         if ((chr == 13) || (chr == 10)) {
/* 602 */           parseCRLF(true);
/* 603 */           eol = true;
/* 604 */         } else if (chr == 32) {
/* 605 */           this.trailingHeaders.append(chr);
/*     */         } else {
/* 607 */           this.trailingHeaders.append(chr);
/* 608 */           lastSignificantChar = this.trailingHeaders.getEnd();
/*     */         }
/*     */         
/* 611 */         if (!eol) {
/* 612 */           this.readChunk.position(this.readChunk.position() + 1);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 620 */       if (((this.readChunk == null) || (this.readChunk.position() >= this.readChunk.limit())) && 
/* 621 */         (readBytes() < 0)) {
/* 622 */         throwEOFException(sm.getString("chunkedInputFilter.eosTrailer"));
/*     */       }
/*     */       
/*     */ 
/* 626 */       chr = this.readChunk.get(this.readChunk.position());
/* 627 */       if ((chr != 32) && (chr != 9)) {
/* 628 */         validLine = false;
/*     */       } else {
/* 630 */         eol = false;
/*     */         
/*     */ 
/* 633 */         this.trailingHeaders.append(chr);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 638 */     String headerName = new String(this.trailingHeaders.getBytes(), startPos, colonPos - startPos, StandardCharsets.ISO_8859_1);
/*     */     
/*     */ 
/* 641 */     if (this.allowedTrailerHeaders.contains(headerName.toLowerCase(Locale.ENGLISH))) {
/* 642 */       MessageBytes headerValue = headers.addValue(headerName);
/*     */       
/*     */ 
/* 645 */       headerValue.setBytes(this.trailingHeaders.getBytes(), colonPos, lastSignificantChar - colonPos);
/*     */     }
/*     */     
/*     */ 
/* 649 */     return true;
/*     */   }
/*     */   
/*     */   private void throwIOException(String msg) throws IOException
/*     */   {
/* 654 */     this.error = true;
/* 655 */     throw new IOException(msg);
/*     */   }
/*     */   
/*     */   private void throwEOFException(String msg) throws IOException
/*     */   {
/* 660 */     this.error = true;
/* 661 */     throw new EOFException(msg);
/*     */   }
/*     */   
/*     */   private void checkError() throws IOException
/*     */   {
/* 666 */     if (this.error) {
/* 667 */       throw new IOException(sm.getString("chunkedInputFilter.error"));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void setByteBuffer(ByteBuffer buffer)
/*     */   {
/* 674 */     this.readChunk = buffer;
/*     */   }
/*     */   
/*     */ 
/*     */   public ByteBuffer getByteBuffer()
/*     */   {
/* 680 */     return this.readChunk;
/*     */   }
/*     */   
/*     */   public void expand(int size) {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\filters\ChunkedInputFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */