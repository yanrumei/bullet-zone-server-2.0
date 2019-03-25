/*     */ package org.apache.tomcat.util.http;
/*     */ 
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FastHttpDateFormat
/*     */ {
/*  40 */   private static final int CACHE_SIZE = Integer.parseInt(System.getProperty("org.apache.tomcat.util.http.FastHttpDateFormat.CACHE_SIZE", "1000"));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String RFC1123_DATE = "EEE, dd MMM yyyy HH:mm:ss zzz";
/*     */   
/*     */ 
/*     */ 
/*  49 */   private static final SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.US);
/*     */   
/*     */ 
/*     */ 
/*  53 */   private static final TimeZone gmtZone = TimeZone.getTimeZone("GMT");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  60 */     format.setTimeZone(gmtZone);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  67 */   private static volatile long currentDateGenerated = 0L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  73 */   private static String currentDate = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  79 */   private static final Map<Long, String> formatCache = new ConcurrentHashMap(CACHE_SIZE);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */   private static final Map<String, Long> parseCache = new ConcurrentHashMap(CACHE_SIZE);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final String getCurrentDate()
/*     */   {
/*  97 */     long now = System.currentTimeMillis();
/*  98 */     if (now - currentDateGenerated > 1000L) {
/*  99 */       synchronized (format) {
/* 100 */         if (now - currentDateGenerated > 1000L) {
/* 101 */           currentDate = format.format(new Date(now));
/* 102 */           currentDateGenerated = now;
/*     */         }
/*     */       }
/*     */     }
/* 106 */     return currentDate;
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
/*     */   public static final String formatDate(long value, DateFormat threadLocalformat)
/*     */   {
/* 120 */     Long longValue = Long.valueOf(value);
/* 121 */     String cachedDate = (String)formatCache.get(longValue);
/* 122 */     if (cachedDate != null) {
/* 123 */       return cachedDate;
/*     */     }
/*     */     
/* 126 */     String newDate = null;
/* 127 */     Date dateValue = new Date(value);
/* 128 */     if (threadLocalformat != null) {
/* 129 */       newDate = threadLocalformat.format(dateValue);
/* 130 */       updateFormatCache(longValue, newDate);
/*     */     } else {
/* 132 */       synchronized (format) {
/* 133 */         newDate = format.format(dateValue);
/*     */       }
/* 135 */       updateFormatCache(longValue, newDate);
/*     */     }
/* 137 */     return newDate;
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
/*     */   public static final long parseDate(String value, DateFormat[] threadLocalformats)
/*     */   {
/* 150 */     Long cachedDate = (Long)parseCache.get(value);
/* 151 */     if (cachedDate != null) {
/* 152 */       return cachedDate.longValue();
/*     */     }
/*     */     
/* 155 */     Long date = null;
/* 156 */     if (threadLocalformats != null) {
/* 157 */       date = internalParseDate(value, threadLocalformats);
/* 158 */       updateParseCache(value, date);
/*     */     } else {
/* 160 */       throw new IllegalArgumentException();
/*     */     }
/* 162 */     if (date == null) {
/* 163 */       return -1L;
/*     */     }
/*     */     
/* 166 */     return date.longValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final Long internalParseDate(String value, DateFormat[] formats)
/*     */   {
/* 175 */     Date date = null;
/* 176 */     for (int i = 0; (date == null) && (i < formats.length); i++) {
/*     */       try {
/* 178 */         date = formats[i].parse(value);
/*     */       }
/*     */       catch (ParseException localParseException) {}
/*     */     }
/*     */     
/* 183 */     if (date == null) {
/* 184 */       return null;
/*     */     }
/* 186 */     return Long.valueOf(date.getTime());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void updateFormatCache(Long key, String value)
/*     */   {
/* 194 */     if (value == null) {
/* 195 */       return;
/*     */     }
/* 197 */     if (formatCache.size() > CACHE_SIZE) {
/* 198 */       formatCache.clear();
/*     */     }
/* 200 */     formatCache.put(key, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void updateParseCache(String key, Long value)
/*     */   {
/* 208 */     if (value == null) {
/* 209 */       return;
/*     */     }
/* 211 */     if (parseCache.size() > CACHE_SIZE) {
/* 212 */       parseCache.clear();
/*     */     }
/* 214 */     parseCache.put(key, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\http\FastHttpDateFormat.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */