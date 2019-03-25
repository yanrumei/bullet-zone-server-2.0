/*     */ package org.apache.tomcat.util.http;
/*     */ 
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.DateFormat;
/*     */ import java.text.FieldPosition;
/*     */ import java.util.BitSet;
/*     */ import java.util.Date;
/*     */ import javax.servlet.http.Cookie;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.buf.ByteChunk;
/*     */ import org.apache.tomcat.util.buf.MessageBytes;
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
/*     */ public final class LegacyCookieProcessor
/*     */   extends CookieProcessorBase
/*     */ {
/*  45 */   private static final Log log = LogFactory.getLog(LegacyCookieProcessor.class);
/*     */   
/*  47 */   private static final UserDataHelper userDataLog = new UserDataHelper(log);
/*     */   
/*     */ 
/*  50 */   private static final StringManager sm = StringManager.getManager("org.apache.tomcat.util.http");
/*     */   
/*  52 */   private static final char[] V0_SEPARATORS = { ',', ';', ' ', '\t' };
/*  53 */   private static final BitSet V0_SEPARATOR_FLAGS = new BitSet(128);
/*     */   
/*     */ 
/*     */ 
/*  57 */   private static final char[] HTTP_SEPARATORS = { '\t', ' ', '"', '(', ')', ',', ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '{', '}' };
/*     */   
/*     */ 
/*     */   static
/*     */   {
/*  62 */     for (char c : V0_SEPARATORS) {
/*  63 */       V0_SEPARATOR_FLAGS.set(c);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*  68 */   private final boolean STRICT_SERVLET_COMPLIANCE = Boolean.getBoolean("org.apache.catalina.STRICT_SERVLET_COMPLIANCE");
/*     */   
/*  70 */   private boolean allowEqualsInValue = false;
/*     */   
/*  72 */   private boolean allowNameOnly = false;
/*     */   
/*  74 */   private boolean allowHttpSepsInV0 = false;
/*     */   
/*  76 */   private boolean alwaysAddExpires = !this.STRICT_SERVLET_COMPLIANCE;
/*     */   
/*  78 */   private final BitSet httpSeparatorFlags = new BitSet(128);
/*     */   
/*  80 */   private final BitSet allowedWithoutQuotes = new BitSet(128);
/*     */   
/*     */ 
/*     */   public LegacyCookieProcessor()
/*     */   {
/*  85 */     for (c : HTTP_SEPARATORS) {
/*  86 */       this.httpSeparatorFlags.set(c);
/*     */     }
/*  88 */     boolean b = this.STRICT_SERVLET_COMPLIANCE;
/*  89 */     if (b) {
/*  90 */       this.httpSeparatorFlags.set(47);
/*     */     }
/*     */     String separators;
/*     */     String separators;
/*  94 */     if (getAllowHttpSepsInV0())
/*     */     {
/*  96 */       separators = ",; ";
/*     */     }
/*     */     else {
/*  99 */       separators = "()<>@,;:\\\"/[]?={} \t";
/*     */     }
/*     */     
/*     */ 
/* 103 */     this.allowedWithoutQuotes.set(32, 127);
/* 104 */     char[] arrayOfChar2 = separators.toCharArray();char c = arrayOfChar2.length; for (char c1 = '\000'; c1 < c; c1++) { char ch = arrayOfChar2[c1];
/* 105 */       this.allowedWithoutQuotes.clear(ch);
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
/* 117 */     if ((!getAllowHttpSepsInV0()) && (!getForwardSlashIsSeparator())) {
/* 118 */       this.allowedWithoutQuotes.set(47);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean getAllowEqualsInValue()
/*     */   {
/* 124 */     return this.allowEqualsInValue;
/*     */   }
/*     */   
/*     */   public void setAllowEqualsInValue(boolean allowEqualsInValue)
/*     */   {
/* 129 */     this.allowEqualsInValue = allowEqualsInValue;
/*     */   }
/*     */   
/*     */   public boolean getAllowNameOnly()
/*     */   {
/* 134 */     return this.allowNameOnly;
/*     */   }
/*     */   
/*     */   public void setAllowNameOnly(boolean allowNameOnly)
/*     */   {
/* 139 */     this.allowNameOnly = allowNameOnly;
/*     */   }
/*     */   
/*     */   public boolean getAllowHttpSepsInV0()
/*     */   {
/* 144 */     return this.allowHttpSepsInV0;
/*     */   }
/*     */   
/*     */   public void setAllowHttpSepsInV0(boolean allowHttpSepsInV0)
/*     */   {
/* 149 */     this.allowHttpSepsInV0 = allowHttpSepsInV0;
/*     */     
/*     */ 
/*     */ 
/* 153 */     char[] seps = "()<>@:\\\"[]?={}\t".toCharArray();
/* 154 */     for (char sep : seps) {
/* 155 */       if (allowHttpSepsInV0) {
/* 156 */         this.allowedWithoutQuotes.set(sep);
/*     */       } else {
/* 158 */         this.allowedWithoutQuotes.clear(sep);
/*     */       }
/*     */     }
/* 161 */     if ((getForwardSlashIsSeparator()) && (!allowHttpSepsInV0)) {
/* 162 */       this.allowedWithoutQuotes.clear(47);
/*     */     } else {
/* 164 */       this.allowedWithoutQuotes.set(47);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean getForwardSlashIsSeparator()
/*     */   {
/* 170 */     return this.httpSeparatorFlags.get(47);
/*     */   }
/*     */   
/*     */   public void setForwardSlashIsSeparator(boolean forwardSlashIsSeparator)
/*     */   {
/* 175 */     if (forwardSlashIsSeparator) {
/* 176 */       this.httpSeparatorFlags.set(47);
/*     */     } else {
/* 178 */       this.httpSeparatorFlags.clear(47);
/*     */     }
/* 180 */     if ((forwardSlashIsSeparator) && (!getAllowHttpSepsInV0())) {
/* 181 */       this.allowedWithoutQuotes.clear(47);
/*     */     } else {
/* 183 */       this.allowedWithoutQuotes.set(47);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean getAlwaysAddExpires()
/*     */   {
/* 189 */     return this.alwaysAddExpires;
/*     */   }
/*     */   
/*     */   public void setAlwaysAddExpires(boolean alwaysAddExpires)
/*     */   {
/* 194 */     this.alwaysAddExpires = alwaysAddExpires;
/*     */   }
/*     */   
/*     */ 
/*     */   public Charset getCharset()
/*     */   {
/* 200 */     return StandardCharsets.ISO_8859_1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void parseCookieHeader(MimeHeaders headers, ServerCookies serverCookies)
/*     */   {
/* 207 */     if (headers == null)
/*     */     {
/* 209 */       return;
/*     */     }
/*     */     
/* 212 */     int pos = headers.findHeader("Cookie", 0);
/* 213 */     while (pos >= 0) {
/* 214 */       MessageBytes cookieValue = headers.getValue(pos);
/*     */       
/* 216 */       if ((cookieValue != null) && (!cookieValue.isNull())) {
/* 217 */         if (cookieValue.getType() != 2) {
/* 218 */           Exception e = new Exception();
/*     */           
/* 220 */           log.debug("Cookies: Parsing cookie as String. Expected bytes.", e);
/* 221 */           cookieValue.toBytes();
/*     */         }
/* 223 */         if (log.isDebugEnabled()) {
/* 224 */           log.debug("Cookies: Parsing b[]: " + cookieValue.toString());
/*     */         }
/* 226 */         ByteChunk bc = cookieValue.getByteChunk();
/* 227 */         processCookieHeader(bc.getBytes(), bc.getOffset(), bc.getLength(), serverCookies);
/*     */       }
/*     */       
/*     */ 
/* 231 */       pos = headers.findHeader("Cookie", ++pos);
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
/*     */   public String generateHeader(Cookie cookie)
/*     */   {
/* 247 */     int version = cookie.getVersion();
/* 248 */     String value = cookie.getValue();
/* 249 */     String path = cookie.getPath();
/* 250 */     String domain = cookie.getDomain();
/* 251 */     String comment = cookie.getComment();
/*     */     
/* 253 */     if (version == 0)
/*     */     {
/* 255 */       if ((needsQuotes(value, 0)) || (comment != null) || (needsQuotes(path, 0)) || (needsQuotes(domain, 0))) {
/* 256 */         version = 1;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 261 */     StringBuffer buf = new StringBuffer();
/*     */     
/*     */ 
/* 264 */     buf.append(cookie.getName());
/* 265 */     buf.append("=");
/*     */     
/*     */ 
/* 268 */     maybeQuote(buf, value, version);
/*     */     
/*     */ 
/* 271 */     if (version == 1)
/*     */     {
/* 273 */       buf.append("; Version=1");
/*     */       
/*     */ 
/* 276 */       if (comment != null) {
/* 277 */         buf.append("; Comment=");
/* 278 */         maybeQuote(buf, comment, version);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 283 */     if (domain != null) {
/* 284 */       buf.append("; Domain=");
/* 285 */       maybeQuote(buf, domain, version);
/*     */     }
/*     */     
/*     */ 
/* 289 */     int maxAge = cookie.getMaxAge();
/* 290 */     if (maxAge >= 0) {
/* 291 */       if (version > 0) {
/* 292 */         buf.append("; Max-Age=");
/* 293 */         buf.append(maxAge);
/*     */       }
/*     */       
/*     */ 
/* 297 */       if ((version == 0) || (getAlwaysAddExpires()))
/*     */       {
/* 299 */         buf.append("; Expires=");
/*     */         
/* 301 */         if (maxAge == 0) {
/* 302 */           buf.append(ANCIENT_DATE);
/*     */         } else {
/* 304 */           ((DateFormat)COOKIE_DATE_FORMAT.get()).format(new Date(
/* 305 */             System.currentTimeMillis() + maxAge * 1000L), buf, new FieldPosition(0));
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 313 */     if (path != null) {
/* 314 */       buf.append("; Path=");
/* 315 */       maybeQuote(buf, path, version);
/*     */     }
/*     */     
/*     */ 
/* 319 */     if (cookie.getSecure()) {
/* 320 */       buf.append("; Secure");
/*     */     }
/*     */     
/*     */ 
/* 324 */     if (cookie.isHttpOnly()) {
/* 325 */       buf.append("; HttpOnly");
/*     */     }
/* 327 */     return buf.toString();
/*     */   }
/*     */   
/*     */   private void maybeQuote(StringBuffer buf, String value, int version)
/*     */   {
/* 332 */     if ((value == null) || (value.length() == 0)) {
/* 333 */       buf.append("\"\"");
/* 334 */     } else if (alreadyQuoted(value)) {
/* 335 */       buf.append('"');
/* 336 */       escapeDoubleQuotes(buf, value, 1, value.length() - 1);
/* 337 */       buf.append('"');
/* 338 */     } else if (needsQuotes(value, version)) {
/* 339 */       buf.append('"');
/* 340 */       escapeDoubleQuotes(buf, value, 0, value.length());
/* 341 */       buf.append('"');
/*     */     } else {
/* 343 */       buf.append(value);
/*     */     }
/*     */   }
/*     */   
/*     */   private static void escapeDoubleQuotes(StringBuffer b, String s, int beginIndex, int endIndex)
/*     */   {
/* 349 */     if ((s.indexOf('"') == -1) && (s.indexOf('\\') == -1)) {
/* 350 */       b.append(s);
/* 351 */       return;
/*     */     }
/*     */     
/* 354 */     for (int i = beginIndex; i < endIndex; i++) {
/* 355 */       char c = s.charAt(i);
/* 356 */       if (c == '\\') {
/* 357 */         b.append('\\').append('\\');
/* 358 */       } else if (c == '"') {
/* 359 */         b.append('\\').append('"');
/*     */       } else {
/* 361 */         b.append(c);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean needsQuotes(String value, int version)
/*     */   {
/* 368 */     if (value == null) {
/* 369 */       return false;
/*     */     }
/*     */     
/* 372 */     int i = 0;
/* 373 */     int len = value.length();
/*     */     
/* 375 */     if (alreadyQuoted(value)) {
/* 376 */       i++;
/* 377 */       len--;
/*     */     }
/* 380 */     for (; 
/* 380 */         i < len; i++) {
/* 381 */       char c = value.charAt(i);
/* 382 */       if (((c < ' ') && (c != '\t')) || (c >= '')) {
/* 383 */         throw new IllegalArgumentException("Control character in cookie value or attribute.");
/*     */       }
/*     */       
/* 386 */       if (((version == 0) && (!this.allowedWithoutQuotes.get(c))) || ((version == 1) && 
/* 387 */         (isHttpSeparator(c)))) {
/* 388 */         return true;
/*     */       }
/*     */     }
/* 391 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean alreadyQuoted(String value)
/*     */   {
/* 396 */     return (value.length() >= 2) && 
/* 397 */       (value.charAt(0) == '"') && 
/* 398 */       (value.charAt(value.length() - 1) == '"');
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
/*     */   private final void processCookieHeader(byte[] bytes, int off, int len, ServerCookies serverCookies)
/*     */   {
/* 411 */     if ((len <= 0) || (bytes == null)) {
/* 412 */       return;
/*     */     }
/* 414 */     int end = off + len;
/* 415 */     int pos = off;
/* 416 */     int nameStart = 0;
/* 417 */     int nameEnd = 0;
/* 418 */     int valueStart = 0;
/* 419 */     int valueEnd = 0;
/* 420 */     int version = 0;
/* 421 */     ServerCookie sc = null;
/*     */     
/*     */ 
/*     */ 
/* 425 */     while (pos < end) {
/* 426 */       boolean isSpecial = false;
/* 427 */       boolean isQuoted = false;
/*     */       
/*     */ 
/* 430 */       while ((pos < end) && (
/* 431 */         ((isHttpSeparator((char)bytes[pos])) && 
/* 432 */         (!getAllowHttpSepsInV0())) || 
/* 433 */         (isV0Separator((char)bytes[pos])) || 
/* 434 */         (isWhiteSpace(bytes[pos])))) {
/* 435 */         pos++;
/*     */       }
/* 437 */       if (pos >= end) {
/* 438 */         return;
/*     */       }
/*     */       
/*     */ 
/* 442 */       if (bytes[pos] == 36) {
/* 443 */         isSpecial = true;
/* 444 */         pos++;
/*     */       }
/*     */       
/*     */ 
/* 448 */       valueEnd = valueStart = nameStart = pos;
/* 449 */       pos = nameEnd = getTokenEndPosition(bytes, pos, end, version, true);
/*     */       
/*     */ 
/* 452 */       while ((pos < end) && (isWhiteSpace(bytes[pos]))) { pos++;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 459 */       if ((pos < end - 1) && (bytes[pos] == 61))
/*     */       {
/*     */         do
/*     */         {
/* 463 */           pos++;
/* 464 */         } while ((pos < end) && (isWhiteSpace(bytes[pos])));
/*     */         
/* 466 */         if (pos >= end) {
/* 467 */           return;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 472 */       switch (bytes[pos]) {
/*     */       case 34: 
/* 474 */         isQuoted = true;
/* 475 */         valueStart = pos + 1;
/*     */         
/*     */ 
/*     */ 
/* 479 */         valueEnd = getQuotedValueEndPosition(bytes, valueStart, end);
/*     */         
/* 481 */         pos = valueEnd;
/*     */         
/*     */ 
/*     */ 
/* 485 */         if (pos >= end) {
/*     */           return;
/*     */         }
/*     */         
/*     */ 
/*     */         break;
/*     */       case 44: 
/*     */       case 59: 
/* 493 */         valueStart = valueEnd = -1;
/*     */         
/* 495 */         break;
/*     */       default: 
/* 497 */         if (((version == 0) && 
/* 498 */           (!isV0Separator((char)bytes[pos])) && 
/* 499 */           (getAllowHttpSepsInV0())) || 
/* 500 */           (!isHttpSeparator((char)bytes[pos])) || (bytes[pos] == 61))
/*     */         {
/*     */ 
/* 503 */           valueStart = pos;
/*     */           
/*     */ 
/* 506 */           valueEnd = getTokenEndPosition(bytes, valueStart, end, version, false);
/*     */           
/* 508 */           pos = valueEnd;
/*     */           
/*     */ 
/*     */ 
/* 512 */           if (valueStart == valueEnd) {
/* 513 */             valueStart = -1;
/* 514 */             valueEnd = -1;
/*     */           }
/*     */           
/*     */         }
/*     */         else
/*     */         {
/* 520 */           UserDataHelper.Mode logMode = userDataLog.getNextMode();
/* 521 */           if (logMode != null) {
/* 522 */             String message = sm.getString("cookies.invalidCookieToken");
/*     */             
/* 524 */             switch (logMode) {
/*     */             case INFO_THEN_DEBUG: 
/* 526 */               message = message + sm.getString("cookies.fallToDebug");
/*     */             
/*     */ 
/*     */             case INFO: 
/* 530 */               log.info(message);
/* 531 */               break;
/*     */             case DEBUG: 
/* 533 */               log.debug(message);
/*     */             }
/*     */           }
/* 536 */           while ((pos < end) && (bytes[pos] != 59) && (bytes[pos] != 44))
/*     */           {
/* 538 */             pos++; }
/* 539 */           pos++;
/*     */           
/*     */ 
/*     */ 
/* 543 */           sc = null;
/* 544 */           continue;
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 549 */           valueStart = valueEnd = -1;
/* 550 */           pos = nameEnd;
/*     */         }
/*     */         
/*     */ 
/*     */         break;
/*     */       }
/*     */       
/*     */       
/*     */ 
/* 559 */       while ((pos < end) && (isWhiteSpace(bytes[pos]))) { pos++;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 564 */       while ((pos < end) && (bytes[pos] != 59) && (bytes[pos] != 44)) {
/* 565 */         pos++;
/*     */       }
/*     */       
/* 568 */       pos++;
/*     */       
/*     */ 
/*     */ 
/* 572 */       if (isSpecial) {
/* 573 */         isSpecial = false;
/*     */         
/*     */ 
/* 576 */         if ((equals("Version", bytes, nameStart, nameEnd)) && (sc == null))
/*     */         {
/*     */ 
/* 579 */           if ((bytes[valueStart] == 49) && (valueEnd == valueStart + 1)) {
/* 580 */             version = 1;
/*     */ 
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */         }
/* 588 */         else if (sc != null)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 593 */           if (equals("Domain", bytes, nameStart, nameEnd)) {
/* 594 */             sc.getDomain().setBytes(bytes, valueStart, valueEnd - valueStart);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/* 600 */           else if (equals("Path", bytes, nameStart, nameEnd)) {
/* 601 */             sc.getPath().setBytes(bytes, valueStart, valueEnd - valueStart);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/* 608 */           else if ((!equals("Port", bytes, nameStart, nameEnd)) && 
/*     */           
/*     */ 
/* 611 */             (!equals("CommentURL", bytes, nameStart, nameEnd)))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 616 */             UserDataHelper.Mode logMode = userDataLog.getNextMode();
/* 617 */             if (logMode != null) {
/* 618 */               String message = sm.getString("cookies.invalidSpecial");
/* 619 */               switch (logMode) {
/*     */               case INFO_THEN_DEBUG: 
/* 621 */                 message = message + sm.getString("cookies.fallToDebug");
/*     */               
/*     */               case INFO: 
/* 624 */                 log.info(message);
/* 625 */                 break;
/*     */               case DEBUG: 
/* 627 */                 log.debug(message);
/*     */               }
/*     */             }
/*     */           } }
/* 631 */       } else if ((valueStart != -1) || (getAllowNameOnly()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 636 */         sc = serverCookies.addCookie();
/* 637 */         sc.setVersion(version);
/* 638 */         sc.getName().setBytes(bytes, nameStart, nameEnd - nameStart);
/*     */         
/*     */ 
/* 641 */         if (valueStart != -1) {
/* 642 */           sc.getValue().setBytes(bytes, valueStart, valueEnd - valueStart);
/*     */           
/* 644 */           if (isQuoted)
/*     */           {
/* 646 */             unescapeDoubleQuotes(sc.getValue().getByteChunk());
/*     */           }
/*     */         }
/*     */         else {
/* 650 */           sc.getValue().setString("");
/*     */         }
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
/*     */   private final int getTokenEndPosition(byte[] bytes, int off, int end, int version, boolean isName)
/*     */   {
/* 665 */     int pos = off;
/* 666 */     while ((pos < end) && (
/* 667 */       (!isHttpSeparator((char)bytes[pos])) || ((version == 0) && 
/* 668 */       (getAllowHttpSepsInV0()) && (bytes[pos] != 61) && 
/* 669 */       (!isV0Separator((char)bytes[pos]))) || ((!isName) && (bytes[pos] == 61) && 
/* 670 */       (getAllowEqualsInValue())))) {
/* 671 */       pos++;
/*     */     }
/*     */     
/* 674 */     if (pos > end) {
/* 675 */       return end;
/*     */     }
/* 677 */     return pos;
/*     */   }
/*     */   
/*     */   private boolean isHttpSeparator(char c)
/*     */   {
/* 682 */     if (((c < ' ') || (c >= '')) && 
/* 683 */       (c != '\t')) {
/* 684 */       throw new IllegalArgumentException("Control character in cookie value or attribute.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 689 */     return this.httpSeparatorFlags.get(c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isV0Separator(char c)
/*     */   {
/* 698 */     if (((c < ' ') || (c >= '')) && 
/* 699 */       (c != '\t')) {
/* 700 */       throw new IllegalArgumentException("Control character in cookie value or attribute.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 705 */     return V0_SEPARATOR_FLAGS.get(c);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final int getQuotedValueEndPosition(byte[] bytes, int off, int end)
/*     */   {
/* 715 */     int pos = off;
/* 716 */     while (pos < end) {
/* 717 */       if (bytes[pos] == 34)
/* 718 */         return pos;
/* 719 */       if ((bytes[pos] == 92) && (pos < end - 1)) {
/* 720 */         pos += 2;
/*     */       } else {
/* 722 */         pos++;
/*     */       }
/*     */     }
/*     */     
/* 726 */     return end;
/*     */   }
/*     */   
/*     */   private static final boolean equals(String s, byte[] b, int start, int end)
/*     */   {
/* 731 */     int blen = end - start;
/* 732 */     if ((b == null) || (blen != s.length())) {
/* 733 */       return false;
/*     */     }
/* 735 */     int boff = start;
/* 736 */     for (int i = 0; i < blen; i++) {
/* 737 */       if (b[(boff++)] != s.charAt(i)) {
/* 738 */         return false;
/*     */       }
/*     */     }
/* 741 */     return true;
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
/*     */   private static final boolean isWhiteSpace(byte c)
/*     */   {
/* 766 */     if ((c == 32) || (c == 9) || (c == 10) || (c == 13) || (c == 12)) {
/* 767 */       return true;
/*     */     }
/* 769 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final void unescapeDoubleQuotes(ByteChunk bc)
/*     */   {
/* 781 */     if ((bc == null) || (bc.getLength() == 0) || (bc.indexOf('"', 0) == -1)) {
/* 782 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 787 */     byte[] original = bc.getBuffer();
/* 788 */     int len = bc.getLength();
/*     */     
/* 790 */     byte[] copy = new byte[len];
/* 791 */     System.arraycopy(original, bc.getStart(), copy, 0, len);
/*     */     
/* 793 */     int src = 0;
/* 794 */     int dest = 0;
/*     */     
/* 796 */     while (src < len) {
/* 797 */       if ((copy[src] == 92) && (src < len) && (copy[(src + 1)] == 34)) {
/* 798 */         src++;
/*     */       }
/* 800 */       copy[dest] = copy[src];
/* 801 */       dest++;
/* 802 */       src++;
/*     */     }
/* 804 */     bc.setBytes(copy, 0, dest);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\LegacyCookieProcessor.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */