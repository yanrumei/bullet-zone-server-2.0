/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.io.NumberInput;
/*     */ import java.text.DateFormat;
/*     */ import java.text.FieldPosition;
/*     */ import java.text.ParseException;
/*     */ import java.text.ParsePosition;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StdDateFormat
/*     */   extends DateFormat
/*     */ {
/*     */   public static final String DATE_FORMAT_STR_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
/*     */   protected static final String DATE_FORMAT_STR_ISO8601_Z = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
/*     */   protected static final String DATE_FORMAT_STR_ISO8601_NO_TZ = "yyyy-MM-dd'T'HH:mm:ss.SSS";
/*     */   protected static final String DATE_FORMAT_STR_PLAIN = "yyyy-MM-dd";
/*     */   protected static final String DATE_FORMAT_STR_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";
/*  63 */   protected static final String[] ALL_FORMATS = { "yyyy-MM-dd'T'HH:mm:ss.SSSZ", "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd'T'HH:mm:ss.SSS", "EEE, dd MMM yyyy HH:mm:ss zzz", "yyyy-MM-dd" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  77 */   private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("UTC");
/*     */   
/*     */ 
/*  80 */   private static final Locale DEFAULT_LOCALE = Locale.US;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */   protected static final DateFormat DATE_FORMAT_RFC1123 = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", DEFAULT_LOCALE);
/* 100 */   static { DATE_FORMAT_RFC1123.setTimeZone(DEFAULT_TIMEZONE);
/* 101 */     DATE_FORMAT_ISO8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ", DEFAULT_LOCALE);
/* 102 */     DATE_FORMAT_ISO8601.setTimeZone(DEFAULT_TIMEZONE);
/* 103 */     DATE_FORMAT_ISO8601_Z = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", DEFAULT_LOCALE);
/* 104 */     DATE_FORMAT_ISO8601_Z.setTimeZone(DEFAULT_TIMEZONE);
/* 105 */     DATE_FORMAT_ISO8601_NO_TZ = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", DEFAULT_LOCALE);
/* 106 */     DATE_FORMAT_ISO8601_NO_TZ.setTimeZone(DEFAULT_TIMEZONE);
/* 107 */     DATE_FORMAT_PLAIN = new SimpleDateFormat("yyyy-MM-dd", DEFAULT_LOCALE);
/* 108 */     DATE_FORMAT_PLAIN.setTimeZone(DEFAULT_TIMEZONE); }
/*     */   
/*     */   protected static final DateFormat DATE_FORMAT_ISO8601;
/*     */   protected static final DateFormat DATE_FORMAT_ISO8601_Z;
/*     */   protected static final DateFormat DATE_FORMAT_ISO8601_NO_TZ;
/*     */   protected static final DateFormat DATE_FORMAT_PLAIN;
/* 114 */   public static final StdDateFormat instance = new StdDateFormat();
/*     */   
/*     */ 
/*     */ 
/*     */   protected transient TimeZone _timezone;
/*     */   
/*     */ 
/*     */ 
/*     */   protected final Locale _locale;
/*     */   
/*     */ 
/*     */ 
/*     */   protected Boolean _lenient;
/*     */   
/*     */ 
/*     */   protected transient DateFormat _formatRFC1123;
/*     */   
/*     */ 
/*     */   protected transient DateFormat _formatISO8601;
/*     */   
/*     */ 
/*     */   protected transient DateFormat _formatISO8601_z;
/*     */   
/*     */ 
/*     */   protected transient DateFormat _formatISO8601_noTz;
/*     */   
/*     */ 
/*     */   protected transient DateFormat _formatPlain;
/*     */   
/*     */ 
/*     */ 
/*     */   public StdDateFormat()
/*     */   {
/* 147 */     this._locale = DEFAULT_LOCALE;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public StdDateFormat(TimeZone tz, Locale loc) {
/* 152 */     this._timezone = tz;
/* 153 */     this._locale = loc;
/*     */   }
/*     */   
/*     */   protected StdDateFormat(TimeZone tz, Locale loc, Boolean lenient) {
/* 157 */     this._timezone = tz;
/* 158 */     this._locale = loc;
/* 159 */     this._lenient = lenient;
/*     */   }
/*     */   
/*     */   public static TimeZone getDefaultTimeZone() {
/* 163 */     return DEFAULT_TIMEZONE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public StdDateFormat withTimeZone(TimeZone tz)
/*     */   {
/* 171 */     if (tz == null) {
/* 172 */       tz = DEFAULT_TIMEZONE;
/*     */     }
/* 174 */     if ((tz == this._timezone) || (tz.equals(this._timezone))) {
/* 175 */       return this;
/*     */     }
/* 177 */     return new StdDateFormat(tz, this._locale, this._lenient);
/*     */   }
/*     */   
/*     */   public StdDateFormat withLocale(Locale loc) {
/* 181 */     if (loc.equals(this._locale)) {
/* 182 */       return this;
/*     */     }
/* 184 */     return new StdDateFormat(this._timezone, loc, this._lenient);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public StdDateFormat clone()
/*     */   {
/* 192 */     return new StdDateFormat(this._timezone, this._locale, this._lenient);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static DateFormat getISO8601Format(TimeZone tz)
/*     */   {
/* 200 */     return getISO8601Format(tz, DEFAULT_LOCALE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DateFormat getISO8601Format(TimeZone tz, Locale loc)
/*     */   {
/* 211 */     return _cloneFormat(DATE_FORMAT_ISO8601, "yyyy-MM-dd'T'HH:mm:ss.SSSZ", tz, loc, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static DateFormat getRFC1123Format(TimeZone tz, Locale loc)
/*     */   {
/* 222 */     return _cloneFormat(DATE_FORMAT_RFC1123, "EEE, dd MMM yyyy HH:mm:ss zzz", tz, loc, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static DateFormat getRFC1123Format(TimeZone tz)
/*     */   {
/* 231 */     return getRFC1123Format(tz, DEFAULT_LOCALE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TimeZone getTimeZone()
/*     */   {
/* 242 */     return this._timezone;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimeZone(TimeZone tz)
/*     */   {
/* 251 */     if (!tz.equals(this._timezone)) {
/* 252 */       _clearFormats();
/* 253 */       this._timezone = tz;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLenient(boolean enabled)
/*     */   {
/* 264 */     Boolean newValue = Boolean.valueOf(enabled);
/* 265 */     if (this._lenient != newValue) {
/* 266 */       this._lenient = newValue;
/*     */       
/* 268 */       _clearFormats();
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isLenient()
/*     */   {
/* 274 */     if (this._lenient == null)
/*     */     {
/* 276 */       return true;
/*     */     }
/* 278 */     return this._lenient.booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date parse(String dateStr)
/*     */     throws ParseException
/*     */   {
/* 290 */     dateStr = dateStr.trim();
/* 291 */     ParsePosition pos = new ParsePosition(0);
/*     */     
/*     */     Date dt;
/*     */     Date dt;
/* 295 */     if (looksLikeISO8601(dateStr)) {
/* 296 */       dt = parseAsISO8601(dateStr, pos, true);
/*     */     }
/*     */     else {
/* 299 */       int i = dateStr.length();
/* 300 */       for (;;) { i--; if (i < 0) break;
/* 301 */         char ch = dateStr.charAt(i);
/* 302 */         if (((ch < '0') || (ch > '9')) && (
/*     */         
/* 304 */           (i > 0) || (ch != '-'))) {
/*     */           break;
/*     */         }
/*     */       }
/*     */       Date dt;
/* 309 */       if ((i < 0) && ((dateStr.charAt(0) == '-') || (NumberInput.inLongRange(dateStr, false))))
/*     */       {
/*     */ 
/* 312 */         dt = new Date(Long.parseLong(dateStr));
/*     */       }
/*     */       else {
/* 315 */         dt = parseAsRFC1123(dateStr, pos);
/*     */       }
/*     */     }
/* 318 */     if (dt != null) {
/* 319 */       return dt;
/*     */     }
/*     */     
/* 322 */     StringBuilder sb = new StringBuilder();
/* 323 */     for (String f : ALL_FORMATS) {
/* 324 */       if (sb.length() > 0) {
/* 325 */         sb.append("\", \"");
/*     */       } else {
/* 327 */         sb.append('"');
/*     */       }
/* 329 */       sb.append(f);
/*     */     }
/* 331 */     sb.append('"');
/* 332 */     throw new ParseException(String.format("Can not parse date \"%s\": not compatible with any of standard forms (%s)", new Object[] { dateStr, sb.toString() }), pos.getErrorIndex());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Date parse(String dateStr, ParsePosition pos)
/*     */   {
/* 340 */     if (looksLikeISO8601(dateStr)) {
/*     */       try {
/* 342 */         return parseAsISO8601(dateStr, pos, false);
/*     */       } catch (ParseException e) {
/* 344 */         return null;
/*     */       }
/*     */     }
/*     */     
/* 348 */     int i = dateStr.length();
/* 349 */     for (;;) { i--; if (i < 0) break;
/* 350 */       char ch = dateStr.charAt(i);
/* 351 */       if (((ch < '0') || (ch > '9')) && (
/*     */       
/* 353 */         (i > 0) || (ch != '-'))) {
/*     */         break;
/*     */       }
/*     */     }
/*     */     
/* 358 */     if (i < 0)
/*     */     {
/* 360 */       if ((dateStr.charAt(0) == '-') || (NumberInput.inLongRange(dateStr, false))) {
/* 361 */         return new Date(Long.parseLong(dateStr));
/*     */       }
/*     */     }
/*     */     
/* 365 */     return parseAsRFC1123(dateStr, pos);
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
/*     */   public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition)
/*     */   {
/* 378 */     if (this._formatISO8601 == null) {
/* 379 */       this._formatISO8601 = _cloneFormat(DATE_FORMAT_ISO8601, "yyyy-MM-dd'T'HH:mm:ss.SSSZ", this._timezone, this._locale, this._lenient);
/*     */     }
/*     */     
/* 382 */     return this._formatISO8601.format(date, toAppendTo, fieldPosition);
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
/* 393 */     String str = "DateFormat " + getClass().getName();
/* 394 */     TimeZone tz = this._timezone;
/* 395 */     if (tz != null) {
/* 396 */       str = str + " (timezone: " + tz + ")";
/*     */     }
/* 398 */     str = str + "(locale: " + this._locale + ")";
/* 399 */     return str;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 404 */     return o == this;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 409 */     return System.identityHashCode(this);
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
/*     */   protected boolean looksLikeISO8601(String dateStr)
/*     */   {
/* 424 */     if ((dateStr.length() >= 5) && (Character.isDigit(dateStr.charAt(0))) && (Character.isDigit(dateStr.charAt(3))) && (dateStr.charAt(4) == '-'))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 429 */       return true;
/*     */     }
/* 431 */     return false;
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
/*     */   protected Date parseAsISO8601(String dateStr, ParsePosition pos, boolean throwErrors)
/*     */     throws ParseException
/*     */   {
/* 445 */     int len = dateStr.length();
/* 446 */     char c = dateStr.charAt(len - 1);
/*     */     
/*     */     DateFormat df;
/*     */     
/*     */     String formatStr;
/* 451 */     if ((len <= 10) && (Character.isDigit(c))) {
/* 452 */       DateFormat df = this._formatPlain;
/* 453 */       String formatStr = "yyyy-MM-dd";
/* 454 */       if (df == null) {
/* 455 */         df = this._formatPlain = _cloneFormat(DATE_FORMAT_PLAIN, formatStr, this._timezone, this._locale, this._lenient);
/*     */       }
/*     */     }
/* 458 */     else if (c == 'Z') {
/* 459 */       DateFormat df = this._formatISO8601_z;
/* 460 */       String formatStr = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
/* 461 */       if (df == null)
/*     */       {
/*     */ 
/*     */ 
/* 465 */         df = this._formatISO8601_z = _cloneFormat(DATE_FORMAT_ISO8601_Z, formatStr, DEFAULT_TIMEZONE, this._locale, this._lenient);
/*     */       }
/*     */       
/*     */ 
/* 469 */       if (dateStr.charAt(len - 4) == ':') {
/* 470 */         StringBuilder sb = new StringBuilder(dateStr);
/* 471 */         sb.insert(len - 1, ".000");
/* 472 */         dateStr = sb.toString();
/*     */       }
/*     */       
/*     */     }
/* 476 */     else if (hasTimeZone(dateStr)) {
/* 477 */       c = dateStr.charAt(len - 3);
/* 478 */       if (c == ':')
/*     */       {
/* 480 */         StringBuilder sb = new StringBuilder(dateStr);
/* 481 */         sb.delete(len - 3, len - 2);
/* 482 */         dateStr = sb.toString();
/* 483 */       } else if ((c == '+') || (c == '-'))
/*     */       {
/* 485 */         dateStr = dateStr + "00";
/*     */       }
/*     */       
/* 488 */       len = dateStr.length();
/*     */       
/* 490 */       int timeLen = len - dateStr.lastIndexOf('T') - 6;
/* 491 */       if (timeLen < 12) {
/* 492 */         int offset = len - 5;
/* 493 */         StringBuilder sb = new StringBuilder(dateStr);
/* 494 */         switch (timeLen) {
/*     */         case 11: 
/* 496 */           sb.insert(offset, '0'); break;
/*     */         case 10: 
/* 498 */           sb.insert(offset, "00"); break;
/*     */         case 9: 
/* 500 */           sb.insert(offset, "000"); break;
/*     */         case 8: 
/* 502 */           sb.insert(offset, ".000"); break;
/*     */         case 7: 
/*     */           break;
/*     */         case 6: 
/* 506 */           sb.insert(offset, "00.000");
/*     */         case 5: 
/* 508 */           sb.insert(offset, ":00.000");
/*     */         }
/* 510 */         dateStr = sb.toString();
/*     */       }
/* 512 */       DateFormat df = this._formatISO8601;
/* 513 */       String formatStr = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
/* 514 */       if (this._formatISO8601 == null) {
/* 515 */         df = this._formatISO8601 = _cloneFormat(DATE_FORMAT_ISO8601, formatStr, this._timezone, this._locale, this._lenient);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 520 */       int timeLen = len - dateStr.lastIndexOf('T') - 1;
/*     */       
/* 522 */       if (timeLen < 12) {
/* 523 */         StringBuilder sb = new StringBuilder(dateStr);
/* 524 */         switch (timeLen) {
/* 525 */         case 11:  sb.append('0');
/* 526 */         case 10:  sb.append('0');
/* 527 */         case 9:  sb.append('0');
/* 528 */           break;
/*     */         default: 
/* 530 */           sb.append(".000");
/*     */         }
/* 532 */         dateStr = sb.toString();
/*     */       }
/* 534 */       df = this._formatISO8601_noTz;
/* 535 */       formatStr = "yyyy-MM-dd'T'HH:mm:ss.SSS";
/* 536 */       if (df == null)
/*     */       {
/*     */ 
/*     */ 
/* 540 */         df = this._formatISO8601_noTz = _cloneFormat(DATE_FORMAT_ISO8601_NO_TZ, formatStr, this._timezone, this._locale, this._lenient);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 545 */     Date dt = df.parse(dateStr, pos);
/*     */     
/* 547 */     if (dt == null) {
/* 548 */       throw new ParseException(String.format("Can not parse date \"%s\": while it seems to fit format '%s', parsing fails (leniency? %s)", new Object[] { dateStr, formatStr, this._lenient }), pos.getErrorIndex());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 553 */     return dt;
/*     */   }
/*     */   
/*     */   protected Date parseAsRFC1123(String dateStr, ParsePosition pos)
/*     */   {
/* 558 */     if (this._formatRFC1123 == null) {
/* 559 */       this._formatRFC1123 = _cloneFormat(DATE_FORMAT_RFC1123, "EEE, dd MMM yyyy HH:mm:ss zzz", this._timezone, this._locale, this._lenient);
/*     */     }
/*     */     
/* 562 */     return this._formatRFC1123.parse(dateStr, pos);
/*     */   }
/*     */   
/*     */ 
/*     */   private static final boolean hasTimeZone(String str)
/*     */   {
/* 568 */     int len = str.length();
/* 569 */     if (len >= 6) {
/* 570 */       char c = str.charAt(len - 6);
/* 571 */       if ((c == '+') || (c == '-')) return true;
/* 572 */       c = str.charAt(len - 5);
/* 573 */       if ((c == '+') || (c == '-')) return true;
/* 574 */       c = str.charAt(len - 3);
/* 575 */       if ((c == '+') || (c == '-')) return true;
/*     */     }
/* 577 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   private static final DateFormat _cloneFormat(DateFormat df, String format, TimeZone tz, Locale loc, Boolean lenient)
/*     */   {
/* 583 */     if (!loc.equals(DEFAULT_LOCALE)) {
/* 584 */       df = new SimpleDateFormat(format, loc);
/* 585 */       df.setTimeZone(tz == null ? DEFAULT_TIMEZONE : tz);
/*     */     } else {
/* 587 */       df = (DateFormat)df.clone();
/* 588 */       if (tz != null) {
/* 589 */         df.setTimeZone(tz);
/*     */       }
/*     */     }
/* 592 */     if (lenient != null) {
/* 593 */       df.setLenient(lenient.booleanValue());
/*     */     }
/* 595 */     return df;
/*     */   }
/*     */   
/*     */   protected void _clearFormats() {
/* 599 */     this._formatRFC1123 = null;
/* 600 */     this._formatISO8601 = null;
/* 601 */     this._formatISO8601_z = null;
/* 602 */     this._formatISO8601_noTz = null;
/*     */     
/* 604 */     this._formatPlain = null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databin\\util\StdDateFormat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */