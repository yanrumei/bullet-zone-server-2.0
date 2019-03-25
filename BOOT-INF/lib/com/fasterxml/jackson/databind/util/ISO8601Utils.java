/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import java.text.ParseException;
/*     */ import java.text.ParsePosition;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
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
/*     */ public class ISO8601Utils
/*     */ {
/*     */   @Deprecated
/*     */   private static final String GMT_ID = "GMT";
/*     */   private static final String UTC_ID = "UTC";
/*     */   @Deprecated
/*  33 */   private static final TimeZone TIMEZONE_GMT = TimeZone.getTimeZone("GMT");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  40 */   private static final TimeZone TIMEZONE_UTC = TimeZone.getTimeZone("UTC");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  46 */   private static final TimeZone TIMEZONE_Z = TIMEZONE_UTC;
/*     */   
/*     */ 
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
/*     */   public static TimeZone timeZoneGMT()
/*     */   {
/*  61 */     return TIMEZONE_GMT;
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
/*     */   public static String format(Date date)
/*     */   {
/*  77 */     return format(date, false, TIMEZONE_UTC);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String format(Date date, boolean millis)
/*     */   {
/*  88 */     return format(date, millis, TIMEZONE_UTC);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String format(Date date, boolean millis, TimeZone tz)
/*     */   {
/* 100 */     Calendar calendar = new GregorianCalendar(tz, Locale.US);
/* 101 */     calendar.setTime(date);
/*     */     
/*     */ 
/* 104 */     int capacity = "yyyy-MM-ddThh:mm:ss".length();
/* 105 */     capacity += (millis ? ".sss".length() : 0);
/* 106 */     capacity += (tz.getRawOffset() == 0 ? "Z".length() : "+hh:mm".length());
/* 107 */     StringBuilder formatted = new StringBuilder(capacity);
/*     */     
/* 109 */     padInt(formatted, calendar.get(1), "yyyy".length());
/* 110 */     formatted.append('-');
/* 111 */     padInt(formatted, calendar.get(2) + 1, "MM".length());
/* 112 */     formatted.append('-');
/* 113 */     padInt(formatted, calendar.get(5), "dd".length());
/* 114 */     formatted.append('T');
/* 115 */     padInt(formatted, calendar.get(11), "hh".length());
/* 116 */     formatted.append(':');
/* 117 */     padInt(formatted, calendar.get(12), "mm".length());
/* 118 */     formatted.append(':');
/* 119 */     padInt(formatted, calendar.get(13), "ss".length());
/* 120 */     if (millis) {
/* 121 */       formatted.append('.');
/* 122 */       padInt(formatted, calendar.get(14), "sss".length());
/*     */     }
/*     */     
/* 125 */     int offset = tz.getOffset(calendar.getTimeInMillis());
/* 126 */     if (offset != 0) {
/* 127 */       int hours = Math.abs(offset / 60000 / 60);
/* 128 */       int minutes = Math.abs(offset / 60000 % 60);
/* 129 */       formatted.append(offset < 0 ? '-' : '+');
/* 130 */       padInt(formatted, hours, "hh".length());
/* 131 */       formatted.append(':');
/* 132 */       padInt(formatted, minutes, "mm".length());
/*     */     } else {
/* 134 */       formatted.append('Z');
/*     */     }
/*     */     
/* 137 */     return formatted.toString();
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
/*     */   public static Date parse(String date, ParsePosition pos)
/*     */     throws ParseException
/*     */   {
/* 156 */     Exception fail = null;
/*     */     try {
/* 158 */       int offset = pos.getIndex();
/*     */       
/*     */ 
/* 161 */       int year = parseInt(date, , offset);
/* 162 */       if (checkOffset(date, offset, '-')) {
/* 163 */         offset++;
/*     */       }
/*     */       
/*     */ 
/* 167 */       int month = parseInt(date, , offset);
/* 168 */       if (checkOffset(date, offset, '-')) {
/* 169 */         offset++;
/*     */       }
/*     */       
/*     */ 
/* 173 */       int day = parseInt(date, , offset);
/*     */       
/* 175 */       int hour = 0;
/* 176 */       int minutes = 0;
/* 177 */       int seconds = 0;
/* 178 */       int milliseconds = 0;
/*     */       
/*     */ 
/* 181 */       boolean hasT = checkOffset(date, offset, 'T');
/*     */       
/* 183 */       if ((!hasT) && (date.length() <= offset)) {
/* 184 */         Calendar calendar = new GregorianCalendar(year, month - 1, day);
/*     */         
/* 186 */         pos.setIndex(offset);
/* 187 */         return calendar.getTime();
/*     */       }
/*     */       
/* 190 */       if (hasT)
/*     */       {
/*     */ 
/* 193 */         offset += 2;hour = parseInt(date, ++offset, offset);
/* 194 */         if (checkOffset(date, offset, ':')) {
/* 195 */           offset++;
/*     */         }
/*     */         
/* 198 */         minutes = parseInt(date, , offset);
/* 199 */         if (checkOffset(date, offset, ':')) {
/* 200 */           offset++;
/*     */         }
/*     */         
/* 203 */         if (date.length() > offset) {
/* 204 */           char c = date.charAt(offset);
/* 205 */           if ((c != 'Z') && (c != '+') && (c != '-')) {
/* 206 */             seconds = parseInt(date, , offset);
/* 207 */             if ((seconds > 59) && (seconds < 63)) { seconds = 59;
/*     */             }
/* 209 */             if (checkOffset(date, offset, '.')) {
/* 210 */               offset++;
/* 211 */               int endOffset = indexOfNonDigit(date, offset + 1);
/* 212 */               int parseEndOffset = Math.min(endOffset, offset + 3);
/* 213 */               int fraction = parseInt(date, offset, parseEndOffset);
/*     */               
/* 215 */               switch (parseEndOffset - offset) {
/*     */               case 2: 
/* 217 */                 milliseconds = fraction * 10;
/* 218 */                 break;
/*     */               case 1: 
/* 220 */                 milliseconds = fraction * 100;
/* 221 */                 break;
/*     */               default: 
/* 223 */                 milliseconds = fraction;
/*     */               }
/* 225 */               offset = endOffset;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 232 */       if (date.length() <= offset) {
/* 233 */         throw new IllegalArgumentException("No time zone indicator");
/*     */       }
/*     */       
/* 236 */       TimeZone timezone = null;
/* 237 */       char timezoneIndicator = date.charAt(offset);
/*     */       
/* 239 */       if (timezoneIndicator == 'Z') {
/* 240 */         timezone = TIMEZONE_Z;
/* 241 */         offset++;
/* 242 */       } else if ((timezoneIndicator == '+') || (timezoneIndicator == '-')) {
/* 243 */         String timezoneOffset = date.substring(offset);
/* 244 */         offset += timezoneOffset.length();
/*     */         
/* 246 */         if (("+0000".equals(timezoneOffset)) || ("+00:00".equals(timezoneOffset))) {
/* 247 */           timezone = TIMEZONE_Z;
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/* 253 */           String timezoneId = "GMT" + timezoneOffset;
/*     */           
/*     */ 
/* 256 */           timezone = TimeZone.getTimeZone(timezoneId);
/*     */           
/* 258 */           String act = timezone.getID();
/* 259 */           if (!act.equals(timezoneId))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 265 */             String cleaned = act.replace(":", "");
/* 266 */             if (!cleaned.equals(timezoneId)) {
/* 267 */               throw new IndexOutOfBoundsException("Mismatching time zone indicator: " + timezoneId + " given, resolves to " + timezone.getID());
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       else {
/* 273 */         throw new IndexOutOfBoundsException("Invalid time zone indicator '" + timezoneIndicator + "'");
/*     */       }
/*     */       
/* 276 */       Calendar calendar = new GregorianCalendar(timezone);
/* 277 */       calendar.setLenient(false);
/* 278 */       calendar.set(1, year);
/* 279 */       calendar.set(2, month - 1);
/* 280 */       calendar.set(5, day);
/* 281 */       calendar.set(11, hour);
/* 282 */       calendar.set(12, minutes);
/* 283 */       calendar.set(13, seconds);
/* 284 */       calendar.set(14, milliseconds);
/*     */       
/* 286 */       pos.setIndex(offset);
/* 287 */       return calendar.getTime();
/*     */     }
/*     */     catch (IndexOutOfBoundsException e)
/*     */     {
/* 291 */       fail = e;
/*     */     } catch (NumberFormatException e) {
/* 293 */       fail = e;
/*     */     } catch (IllegalArgumentException e) {
/* 295 */       fail = e;
/*     */     }
/* 297 */     String input = '"' + date + '"';
/* 298 */     String msg = fail.getMessage();
/* 299 */     if ((msg == null) || (msg.isEmpty())) {
/* 300 */       msg = "(" + fail.getClass().getName() + ")";
/*     */     }
/* 302 */     ParseException ex = new ParseException("Failed to parse date " + input + ": " + msg, pos.getIndex());
/* 303 */     ex.initCause(fail);
/* 304 */     throw ex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean checkOffset(String value, int offset, char expected)
/*     */   {
/* 316 */     return (offset < value.length()) && (value.charAt(offset) == expected);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static int parseInt(String value, int beginIndex, int endIndex)
/*     */     throws NumberFormatException
/*     */   {
/* 329 */     if ((beginIndex < 0) || (endIndex > value.length()) || (beginIndex > endIndex)) {
/* 330 */       throw new NumberFormatException(value);
/*     */     }
/*     */     
/* 333 */     int i = beginIndex;
/* 334 */     int result = 0;
/*     */     
/* 336 */     if (i < endIndex) {
/* 337 */       int digit = Character.digit(value.charAt(i++), 10);
/* 338 */       if (digit < 0) {
/* 339 */         throw new NumberFormatException("Invalid number: " + value.substring(beginIndex, endIndex));
/*     */       }
/* 341 */       result = -digit;
/*     */     }
/* 343 */     while (i < endIndex) {
/* 344 */       int digit = Character.digit(value.charAt(i++), 10);
/* 345 */       if (digit < 0) {
/* 346 */         throw new NumberFormatException("Invalid number: " + value.substring(beginIndex, endIndex));
/*     */       }
/* 348 */       result *= 10;
/* 349 */       result -= digit;
/*     */     }
/* 351 */     return -result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void padInt(StringBuilder buffer, int value, int length)
/*     */   {
/* 362 */     String strValue = Integer.toString(value);
/* 363 */     for (int i = length - strValue.length(); i > 0; i--) {
/* 364 */       buffer.append('0');
/*     */     }
/* 366 */     buffer.append(strValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static int indexOfNonDigit(String string, int offset)
/*     */   {
/* 373 */     for (int i = offset; i < string.length(); i++) {
/* 374 */       char c = string.charAt(i);
/* 375 */       if ((c < '0') || (c > '9')) return i;
/*     */     }
/* 377 */     return string.length();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databin\\util\ISO8601Utils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */