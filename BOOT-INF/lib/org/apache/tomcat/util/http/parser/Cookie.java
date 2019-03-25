/*     */ package org.apache.tomcat.util.http.parser;
/*     */ 
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
/*     */ import org.apache.tomcat.util.http.ServerCookie;
/*     */ import org.apache.tomcat.util.http.ServerCookies;
/*     */ import org.apache.tomcat.util.log.UserDataHelper;
/*     */ import org.apache.tomcat.util.log.UserDataHelper.Mode;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Cookie
/*     */ {
/*  54 */   private static final Log log = LogFactory.getLog(Cookie.class);
/*  55 */   private static final UserDataHelper invalidCookieVersionLog = new UserDataHelper(log);
/*  56 */   private static final UserDataHelper invalidCookieLog = new UserDataHelper(log);
/*     */   
/*  58 */   private static final StringManager sm = StringManager.getManager("org.apache.tomcat.util.http.parser");
/*     */   
/*  60 */   private static final boolean[] isCookieOctet = new boolean['Ā'];
/*  61 */   private static final boolean[] isText = new boolean['Ā'];
/*  62 */   private static final byte[] VERSION_BYTES = "$Version".getBytes(StandardCharsets.ISO_8859_1);
/*  63 */   private static final byte[] PATH_BYTES = "$Path".getBytes(StandardCharsets.ISO_8859_1);
/*  64 */   private static final byte[] DOMAIN_BYTES = "$Domain".getBytes(StandardCharsets.ISO_8859_1);
/*  65 */   private static final byte[] EMPTY_BYTES = new byte[0];
/*     */   
/*     */   private static final byte TAB_BYTE = 9;
/*     */   
/*     */   private static final byte SPACE_BYTE = 32;
/*     */   private static final byte QUOTE_BYTE = 34;
/*     */   private static final byte COMMA_BYTE = 44;
/*     */   private static final byte FORWARDSLASH_BYTE = 47;
/*     */   private static final byte SEMICOLON_BYTE = 59;
/*     */   private static final byte EQUALS_BYTE = 61;
/*     */   private static final byte SLASH_BYTE = 92;
/*     */   private static final byte DEL_BYTE = 127;
/*     */   
/*     */   static
/*     */   {
/*  80 */     for (int i = 0; i < 256; i++) {
/*  81 */       if ((i < 33) || (i == 34) || (i == 44) || (i == 59) || (i == 92) || (i == 127))
/*     */       {
/*  83 */         isCookieOctet[i] = false;
/*     */       } else {
/*  85 */         isCookieOctet[i] = true;
/*     */       }
/*     */     }
/*  88 */     for (int i = 0; i < 256; i++) {
/*  89 */       if ((i < 9) || ((i > 9) && (i < 32)) || (i == 127)) {
/*  90 */         isText[i] = false;
/*     */       } else {
/*  92 */         isText[i] = true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void parseCookie(byte[] bytes, int offset, int len, ServerCookies serverCookies)
/*     */   {
/* 108 */     ByteBuffer bb = new ByteBuffer(bytes, offset, len);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 114 */     skipLWS(bb);
/*     */     
/*     */ 
/* 117 */     int mark = bb.position();
/*     */     
/* 119 */     SkipResult skipResult = skipBytes(bb, VERSION_BYTES);
/* 120 */     if (skipResult != SkipResult.FOUND)
/*     */     {
/* 122 */       parseCookieRfc6265(bb, serverCookies);
/* 123 */       return;
/*     */     }
/*     */     
/* 126 */     skipLWS(bb);
/*     */     
/* 128 */     skipResult = skipByte(bb, (byte)61);
/* 129 */     if (skipResult != SkipResult.FOUND)
/*     */     {
/*     */ 
/* 132 */       bb.position(mark);
/* 133 */       parseCookieRfc6265(bb, serverCookies);
/* 134 */       return;
/*     */     }
/*     */     
/* 137 */     skipLWS(bb);
/*     */     
/* 139 */     ByteBuffer value = readCookieValue(bb);
/* 140 */     if ((value != null) && (value.remaining() == 1)) {
/* 141 */       int version = value.get() - 48;
/* 142 */       if ((version == 1) || (version == 0))
/*     */       {
/*     */ 
/* 145 */         skipLWS(bb);
/* 146 */         byte b = bb.get();
/* 147 */         if ((b == 59) || (b == 44)) {
/* 148 */           parseCookieRfc2109(bb, serverCookies, version);
/*     */         }
/* 150 */         return;
/*     */       }
/*     */       
/*     */ 
/* 154 */       value.rewind();
/* 155 */       logInvalidVersion(value);
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 160 */       logInvalidVersion(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String unescapeCookieValueRfc2109(String input)
/*     */   {
/* 166 */     if ((input == null) || (input.length() < 2)) {
/* 167 */       return input;
/*     */     }
/* 169 */     if ((input.charAt(0) != '"') && (input.charAt(input.length() - 1) != '"')) {
/* 170 */       return input;
/*     */     }
/*     */     
/* 173 */     StringBuilder sb = new StringBuilder(input.length());
/* 174 */     char[] chars = input.toCharArray();
/* 175 */     boolean escaped = false;
/*     */     
/* 177 */     for (int i = 1; i < input.length() - 1; i++) {
/* 178 */       if (chars[i] == '\\') {
/* 179 */         escaped = true;
/* 180 */       } else if (escaped) {
/* 181 */         escaped = false;
/* 182 */         if (chars[i] < '') {
/* 183 */           sb.append(chars[i]);
/*     */         } else {
/* 185 */           sb.append('\\');
/* 186 */           sb.append(chars[i]);
/*     */         }
/*     */       } else {
/* 189 */         sb.append(chars[i]);
/*     */       }
/*     */     }
/* 192 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */   private static void parseCookieRfc6265(ByteBuffer bb, ServerCookies serverCookies)
/*     */   {
/* 198 */     boolean moreToProcess = true;
/*     */     
/* 200 */     while (moreToProcess) {
/* 201 */       skipLWS(bb);
/*     */       
/* 203 */       ByteBuffer name = readToken(bb);
/* 204 */       ByteBuffer value = null;
/*     */       
/* 206 */       skipLWS(bb);
/*     */       
/* 208 */       SkipResult skipResult = skipByte(bb, (byte)61);
/* 209 */       if (skipResult == SkipResult.FOUND) {
/* 210 */         skipLWS(bb);
/* 211 */         value = readCookieValueRfc6265(bb);
/* 212 */         if (value == null) {
/* 213 */           logInvalidHeader(bb);
/*     */           
/* 215 */           skipUntilSemiColon(bb);
/*     */         }
/*     */         else {
/* 218 */           skipLWS(bb);
/*     */         }
/*     */       } else {
/* 221 */         skipResult = skipByte(bb, (byte)59);
/* 222 */         if (skipResult != SkipResult.FOUND)
/*     */         {
/* 224 */           if (skipResult == SkipResult.NOT_FOUND) {
/* 225 */             logInvalidHeader(bb);
/*     */             
/* 227 */             skipUntilSemiColon(bb);
/*     */           }
/*     */           else
/*     */           {
/* 231 */             moreToProcess = false;
/*     */           }
/*     */         }
/* 234 */         else if (name.hasRemaining()) {
/* 235 */           ServerCookie sc = serverCookies.addCookie();
/* 236 */           sc.getName().setBytes(name.array(), name.position(), name.remaining());
/* 237 */           if (value == null) {
/* 238 */             sc.getValue().setBytes(EMPTY_BYTES, 0, EMPTY_BYTES.length);
/*     */           } else {
/* 240 */             sc.getValue().setBytes(value.array(), value.position(), value.remaining());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static void parseCookieRfc2109(ByteBuffer bb, ServerCookies serverCookies, int version)
/*     */   {
/* 250 */     boolean moreToProcess = true;
/*     */     
/* 252 */     while (moreToProcess) {
/* 253 */       skipLWS(bb);
/*     */       
/* 255 */       boolean parseAttributes = true;
/*     */       
/* 257 */       ByteBuffer name = readToken(bb);
/* 258 */       ByteBuffer value = null;
/* 259 */       ByteBuffer path = null;
/* 260 */       ByteBuffer domain = null;
/*     */       
/* 262 */       skipLWS(bb);
/*     */       
/* 264 */       SkipResult skipResult = skipByte(bb, (byte)61);
/* 265 */       if (skipResult == SkipResult.FOUND) {
/* 266 */         skipLWS(bb);
/* 267 */         value = readCookieValueRfc2109(bb, false);
/* 268 */         if (value == null) {
/* 269 */           skipInvalidCookie(bb);
/*     */         }
/*     */         else {
/* 272 */           skipLWS(bb);
/*     */         }
/*     */       } else {
/* 275 */         skipResult = skipByte(bb, (byte)44);
/* 276 */         if (skipResult == SkipResult.FOUND) {
/* 277 */           parseAttributes = false;
/*     */         }
/* 279 */         skipResult = skipByte(bb, (byte)59);
/* 280 */         if (skipResult == SkipResult.EOF) {
/* 281 */           parseAttributes = false;
/* 282 */           moreToProcess = false;
/* 283 */         } else if (skipResult == SkipResult.NOT_FOUND) {
/* 284 */           skipInvalidCookie(bb);
/* 285 */           continue;
/*     */         }
/*     */         
/* 288 */         if (parseAttributes) {
/* 289 */           skipResult = skipBytes(bb, PATH_BYTES);
/* 290 */           if (skipResult == SkipResult.FOUND) {
/* 291 */             skipLWS(bb);
/* 292 */             skipResult = skipByte(bb, (byte)61);
/* 293 */             if (skipResult != SkipResult.FOUND) {
/* 294 */               skipInvalidCookie(bb);
/* 295 */               continue;
/*     */             }
/* 297 */             path = readCookieValueRfc2109(bb, true);
/* 298 */             if (path == null) {
/* 299 */               skipInvalidCookie(bb);
/* 300 */               continue;
/*     */             }
/* 302 */             skipLWS(bb);
/*     */             
/* 304 */             skipResult = skipByte(bb, (byte)44);
/* 305 */             if (skipResult == SkipResult.FOUND) {
/* 306 */               parseAttributes = false;
/*     */             }
/* 308 */             skipResult = skipByte(bb, (byte)59);
/* 309 */             if (skipResult == SkipResult.EOF) {
/* 310 */               parseAttributes = false;
/* 311 */               moreToProcess = false;
/* 312 */             } else if (skipResult == SkipResult.NOT_FOUND) {
/* 313 */               skipInvalidCookie(bb);
/* 314 */               continue;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 319 */         if (parseAttributes) {
/* 320 */           skipResult = skipBytes(bb, DOMAIN_BYTES);
/* 321 */           if (skipResult == SkipResult.FOUND) {
/* 322 */             skipLWS(bb);
/* 323 */             skipResult = skipByte(bb, (byte)61);
/* 324 */             if (skipResult != SkipResult.FOUND) {
/* 325 */               skipInvalidCookie(bb);
/* 326 */               continue;
/*     */             }
/* 328 */             domain = readCookieValueRfc2109(bb, false);
/* 329 */             if (domain == null) {
/* 330 */               skipInvalidCookie(bb);
/* 331 */               continue;
/*     */             }
/*     */             
/* 334 */             skipResult = skipByte(bb, (byte)44);
/* 335 */             if (skipResult == SkipResult.FOUND) {
/* 336 */               parseAttributes = false;
/*     */             }
/* 338 */             skipResult = skipByte(bb, (byte)59);
/* 339 */             if (skipResult == SkipResult.EOF) {
/* 340 */               parseAttributes = false;
/* 341 */               moreToProcess = false;
/* 342 */             } else if (skipResult == SkipResult.NOT_FOUND) {
/* 343 */               skipInvalidCookie(bb);
/* 344 */               continue;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 349 */         if ((name.hasRemaining()) && (value != null) && (value.hasRemaining())) {
/* 350 */           ServerCookie sc = serverCookies.addCookie();
/* 351 */           sc.setVersion(version);
/* 352 */           sc.getName().setBytes(name.array(), name.position(), name.remaining());
/* 353 */           sc.getValue().setBytes(value.array(), value.position(), value.remaining());
/* 354 */           if (domain != null) {
/* 355 */             sc.getDomain().setBytes(domain.array(), domain.position(), domain.remaining());
/*     */           }
/* 357 */           if (path != null) {
/* 358 */             sc.getPath().setBytes(path.array(), path.position(), path.remaining());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void skipInvalidCookie(ByteBuffer bb) {
/* 366 */     logInvalidHeader(bb);
/*     */     
/* 368 */     skipUntilSemiColonOrComma(bb);
/*     */   }
/*     */   
/*     */   private static void skipLWS(ByteBuffer bb)
/*     */   {
/* 373 */     while (bb.hasRemaining()) {
/* 374 */       byte b = bb.get();
/* 375 */       if ((b != 9) && (b != 32)) {
/* 376 */         bb.rewind();
/* 377 */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void skipUntilSemiColon(ByteBuffer bb)
/*     */   {
/* 384 */     while (bb.hasRemaining()) {
/* 385 */       if (bb.get() == 59) {
/*     */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void skipUntilSemiColonOrComma(ByteBuffer bb)
/*     */   {
/* 393 */     while (bb.hasRemaining()) {
/* 394 */       byte b = bb.get();
/* 395 */       if ((b == 59) || (b == 44)) {
/*     */         break;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static SkipResult skipByte(ByteBuffer bb, byte target)
/*     */   {
/* 404 */     if (!bb.hasRemaining()) {
/* 405 */       return SkipResult.EOF;
/*     */     }
/* 407 */     if (bb.get() == target) {
/* 408 */       return SkipResult.FOUND;
/*     */     }
/*     */     
/* 411 */     bb.rewind();
/* 412 */     return SkipResult.NOT_FOUND;
/*     */   }
/*     */   
/*     */   private static SkipResult skipBytes(ByteBuffer bb, byte[] target)
/*     */   {
/* 417 */     int mark = bb.position();
/*     */     
/* 419 */     for (int i = 0; i < target.length; i++) {
/* 420 */       if (!bb.hasRemaining()) {
/* 421 */         bb.position(mark);
/* 422 */         return SkipResult.EOF;
/*     */       }
/* 424 */       if (bb.get() != target[i]) {
/* 425 */         bb.position(mark);
/* 426 */         return SkipResult.NOT_FOUND;
/*     */       }
/*     */     }
/* 429 */     return SkipResult.FOUND;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ByteBuffer readCookieValue(ByteBuffer bb)
/*     */   {
/* 438 */     boolean quoted = false;
/* 439 */     if (bb.hasRemaining()) {
/* 440 */       if (bb.get() == 34) {
/* 441 */         quoted = true;
/*     */       } else {
/* 443 */         bb.rewind();
/*     */       }
/*     */     }
/* 446 */     int start = bb.position();
/* 447 */     int end = bb.limit();
/* 448 */     while (bb.hasRemaining()) {
/* 449 */       byte b = bb.get();
/* 450 */       if (isCookieOctet[(b & 0xFF)] == 0)
/*     */       {
/* 452 */         if ((b == 59) || (b == 44) || (b == 32) || (b == 9)) {
/* 453 */           end = bb.position() - 1;
/* 454 */           bb.position(end);
/* 455 */           break; }
/* 456 */         if ((quoted) && (b == 34)) {
/* 457 */           end = bb.position() - 1;
/* 458 */           break;
/*     */         }
/*     */         
/* 461 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 465 */     return new ByteBuffer(bb.bytes, start, end - start);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static ByteBuffer readCookieValueRfc6265(ByteBuffer bb)
/*     */   {
/* 474 */     boolean quoted = false;
/* 475 */     if (bb.hasRemaining()) {
/* 476 */       if (bb.get() == 34) {
/* 477 */         quoted = true;
/*     */       } else {
/* 479 */         bb.rewind();
/*     */       }
/*     */     }
/* 482 */     int start = bb.position();
/* 483 */     int end = bb.limit();
/* 484 */     while (bb.hasRemaining()) {
/* 485 */       byte b = bb.get();
/* 486 */       if (isCookieOctet[(b & 0xFF)] == 0)
/*     */       {
/* 488 */         if ((b == 59) || (b == 32) || (b == 9)) {
/* 489 */           end = bb.position() - 1;
/* 490 */           bb.position(end);
/* 491 */           break; }
/* 492 */         if ((quoted) && (b == 34)) {
/* 493 */           end = bb.position() - 1;
/* 494 */           break;
/*     */         }
/*     */         
/* 497 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 501 */     return new ByteBuffer(bb.bytes, start, end - start);
/*     */   }
/*     */   
/*     */   private static ByteBuffer readCookieValueRfc2109(ByteBuffer bb, boolean allowForwardSlash)
/*     */   {
/* 506 */     if (!bb.hasRemaining()) {
/* 507 */       return null;
/*     */     }
/*     */     
/* 510 */     if (bb.peek() == 34) {
/* 511 */       return readQuotedString(bb);
/*     */     }
/* 513 */     if (allowForwardSlash) {
/* 514 */       return readTokenAllowForwardSlash(bb);
/*     */     }
/* 516 */     return readToken(bb);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static ByteBuffer readToken(ByteBuffer bb)
/*     */   {
/* 523 */     int start = bb.position();
/* 524 */     int end = bb.limit();
/* 525 */     while (bb.hasRemaining()) {
/* 526 */       if (!HttpParser.isToken(bb.get())) {
/* 527 */         end = bb.position() - 1;
/* 528 */         bb.position(end);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 533 */     return new ByteBuffer(bb.bytes, start, end - start);
/*     */   }
/*     */   
/*     */   private static ByteBuffer readTokenAllowForwardSlash(ByteBuffer bb)
/*     */   {
/* 538 */     int start = bb.position();
/* 539 */     int end = bb.limit();
/* 540 */     while (bb.hasRemaining()) {
/* 541 */       byte b = bb.get();
/* 542 */       if ((b != 47) && (!HttpParser.isToken(b))) {
/* 543 */         end = bb.position() - 1;
/* 544 */         bb.position(end);
/* 545 */         break;
/*     */       }
/*     */     }
/*     */     
/* 549 */     return new ByteBuffer(bb.bytes, start, end - start);
/*     */   }
/*     */   
/*     */   private static ByteBuffer readQuotedString(ByteBuffer bb)
/*     */   {
/* 554 */     int start = bb.position();
/*     */     
/*     */ 
/* 557 */     bb.get();
/* 558 */     boolean escaped = false;
/* 559 */     while (bb.hasRemaining()) {
/* 560 */       byte b = bb.get();
/* 561 */       if (b == 92)
/*     */       {
/* 563 */         escaped = true;
/* 564 */       } else if ((escaped) && (b > -1)) {
/* 565 */         escaped = false;
/* 566 */       } else { if (b == 34)
/* 567 */           return new ByteBuffer(bb.bytes, start, bb.position() - start);
/* 568 */         if (isText[(b & 0xFF)] != 0) {
/* 569 */           escaped = false;
/*     */         } else {
/* 571 */           return null;
/*     */         }
/*     */       }
/*     */     }
/* 575 */     return null;
/*     */   }
/*     */   
/*     */   private static void logInvalidHeader(ByteBuffer bb)
/*     */   {
/* 580 */     UserDataHelper.Mode logMode = invalidCookieLog.getNextMode();
/* 581 */     if (logMode != null) {
/* 582 */       String headerValue = new String(bb.array(), bb.position(), bb.limit() - bb.position(), StandardCharsets.UTF_8);
/*     */       
/* 584 */       String message = sm.getString("cookie.invalidCookieValue", new Object[] { headerValue });
/* 585 */       switch (logMode) {
/*     */       case INFO_THEN_DEBUG: 
/* 587 */         message = message + sm.getString("cookie.fallToDebug");
/*     */       
/*     */       case INFO: 
/* 590 */         log.info(message);
/* 591 */         break;
/*     */       case DEBUG: 
/* 593 */         log.debug(message);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static void logInvalidVersion(ByteBuffer value)
/*     */   {
/* 600 */     UserDataHelper.Mode logMode = invalidCookieVersionLog.getNextMode();
/* 601 */     if (logMode != null) { String version;
/*     */       String version;
/* 603 */       if (value == null) {
/* 604 */         version = sm.getString("cookie.valueNotPresent");
/*     */       }
/*     */       else {
/* 607 */         version = new String(value.bytes, value.position(), value.limit() - value.position(), StandardCharsets.UTF_8);
/*     */       }
/* 609 */       String message = sm.getString("cookie.invalidCookieVersion", new Object[] { version });
/* 610 */       switch (logMode) {
/*     */       case INFO_THEN_DEBUG: 
/* 612 */         message = message + sm.getString("cookie.fallToDebug");
/*     */       
/*     */       case INFO: 
/* 615 */         log.info(message);
/* 616 */         break;
/*     */       case DEBUG: 
/* 618 */         log.debug(message);
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class ByteBuffer
/*     */   {
/*     */     private final byte[] bytes;
/*     */     
/*     */     private int limit;
/*     */     
/* 632 */     private int position = 0;
/*     */     
/*     */     public ByteBuffer(byte[] bytes, int offset, int len) {
/* 635 */       this.bytes = bytes;
/* 636 */       this.position = offset;
/* 637 */       this.limit = (offset + len);
/*     */     }
/*     */     
/*     */     public int position() {
/* 641 */       return this.position;
/*     */     }
/*     */     
/*     */     public void position(int position) {
/* 645 */       this.position = position;
/*     */     }
/*     */     
/*     */     public int limit() {
/* 649 */       return this.limit;
/*     */     }
/*     */     
/*     */     public int remaining() {
/* 653 */       return this.limit - this.position;
/*     */     }
/*     */     
/*     */     public boolean hasRemaining() {
/* 657 */       return this.position < this.limit;
/*     */     }
/*     */     
/*     */     public byte get() {
/* 661 */       return this.bytes[(this.position++)];
/*     */     }
/*     */     
/*     */     public byte peek() {
/* 665 */       return this.bytes[this.position];
/*     */     }
/*     */     
/*     */     public void rewind() {
/* 669 */       this.position -= 1;
/*     */     }
/*     */     
/*     */     public byte[] array() {
/* 673 */       return this.bytes;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 679 */       return "position [" + this.position + "], limit [" + this.limit + "]";
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\parser\Cookie.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */