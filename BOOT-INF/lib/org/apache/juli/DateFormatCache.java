/*     */ package org.apache.juli;
/*     */ 
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DateFormatCache
/*     */ {
/*     */   private static final String msecPattern = "#";
/*     */   private final String format;
/*     */   private final int cacheSize;
/*     */   private final Cache cache;
/*     */   
/*     */   private String tidyFormat(String format)
/*     */   {
/*  64 */     boolean escape = false;
/*  65 */     StringBuilder result = new StringBuilder();
/*  66 */     int len = format.length();
/*     */     
/*  68 */     for (int i = 0; i < len; i++) {
/*  69 */       char x = format.charAt(i);
/*  70 */       if ((escape) || (x != 'S')) {
/*  71 */         result.append(x);
/*     */       } else {
/*  73 */         result.append("#");
/*     */       }
/*  75 */       if (x == '\'') {
/*  76 */         escape = !escape;
/*     */       }
/*     */     }
/*  79 */     return result.toString();
/*     */   }
/*     */   
/*     */   public DateFormatCache(int size, String format, DateFormatCache parent) {
/*  83 */     this.cacheSize = size;
/*  84 */     this.format = tidyFormat(format);
/*  85 */     Cache parentCache = null;
/*  86 */     if (parent != null) {
/*  87 */       synchronized (parent) {
/*  88 */         parentCache = parent.cache;
/*     */       }
/*     */     }
/*  91 */     this.cache = new Cache(parentCache, null);
/*     */   }
/*     */   
/*     */   public String getFormat(long time) {
/*  95 */     return this.cache.getFormat(time);
/*     */   }
/*     */   
/*     */   public String getTimeFormat() {
/*  99 */     return this.format;
/*     */   }
/*     */   
/*     */ 
/*     */   private class Cache
/*     */   {
/* 105 */     private long previousSeconds = Long.MIN_VALUE;
/*     */     
/* 107 */     private String previousFormat = "";
/*     */     
/*     */ 
/* 110 */     private long first = Long.MIN_VALUE;
/*     */     
/* 112 */     private long last = Long.MIN_VALUE;
/*     */     
/* 114 */     private int offset = 0;
/*     */     
/* 116 */     private final Date currentDate = new Date();
/*     */     
/*     */     private String[] cache;
/*     */     
/*     */     private SimpleDateFormat formatter;
/* 121 */     private Cache parent = null;
/*     */     
/*     */     private Cache(Cache parent) {
/* 124 */       this.cache = new String[DateFormatCache.this.cacheSize];
/* 125 */       this.formatter = new SimpleDateFormat(DateFormatCache.this.format, Locale.US);
/* 126 */       this.formatter.setTimeZone(TimeZone.getDefault());
/* 127 */       this.parent = parent;
/*     */     }
/*     */     
/*     */     private String getFormat(long time)
/*     */     {
/* 132 */       long seconds = time / 1000L;
/*     */       
/*     */ 
/*     */ 
/* 136 */       if (seconds == this.previousSeconds) {
/* 137 */         return this.previousFormat;
/*     */       }
/*     */       
/*     */ 
/* 141 */       this.previousSeconds = seconds;
/* 142 */       int index = (this.offset + (int)(seconds - this.first)) % DateFormatCache.this.cacheSize;
/* 143 */       if (index < 0) {
/* 144 */         index += DateFormatCache.this.cacheSize;
/*     */       }
/* 146 */       if ((seconds >= this.first) && (seconds <= this.last)) {
/* 147 */         if (this.cache[index] != null)
/*     */         {
/* 149 */           this.previousFormat = this.cache[index];
/* 150 */           return this.previousFormat;
/*     */         }
/*     */         
/*     */       }
/* 154 */       else if ((seconds >= this.last + DateFormatCache.this.cacheSize) || (seconds <= this.first - DateFormatCache.this.cacheSize)) {
/* 155 */         this.first = seconds;
/* 156 */         this.last = (this.first + DateFormatCache.this.cacheSize - 1L);
/* 157 */         index = 0;
/* 158 */         this.offset = 0;
/* 159 */         for (int i = 1; i < DateFormatCache.this.cacheSize; i++) {
/* 160 */           this.cache[i] = null;
/*     */         }
/* 162 */       } else if (seconds > this.last) {
/* 163 */         for (int i = 1; i < seconds - this.last; i++) {
/* 164 */           this.cache[((index + DateFormatCache.this.cacheSize - i) % DateFormatCache.this.cacheSize)] = null;
/*     */         }
/* 166 */         this.first = (seconds - (DateFormatCache.this.cacheSize - 1));
/* 167 */         this.last = seconds;
/* 168 */         this.offset = ((index + 1) % DateFormatCache.this.cacheSize);
/* 169 */       } else if (seconds < this.first) {
/* 170 */         for (int i = 1; i < this.first - seconds; i++) {
/* 171 */           this.cache[((index + i) % DateFormatCache.this.cacheSize)] = null;
/*     */         }
/* 173 */         this.first = seconds;
/* 174 */         this.last = (seconds + (DateFormatCache.this.cacheSize - 1));
/* 175 */         this.offset = index;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 180 */       if (this.parent != null) {
/* 181 */         synchronized (this.parent) {
/* 182 */           this.previousFormat = this.parent.getFormat(time);
/*     */         }
/*     */       } else {
/* 185 */         this.currentDate.setTime(time);
/* 186 */         this.previousFormat = this.formatter.format(this.currentDate);
/*     */       }
/* 188 */       this.cache[index] = this.previousFormat;
/* 189 */       return this.previousFormat;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\juli\DateFormatCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */