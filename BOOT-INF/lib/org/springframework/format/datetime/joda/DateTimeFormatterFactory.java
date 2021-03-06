/*     */ package org.springframework.format.datetime.joda;
/*     */ 
/*     */ import java.util.TimeZone;
/*     */ import org.joda.time.DateTimeZone;
/*     */ import org.joda.time.format.DateTimeFormat;
/*     */ import org.joda.time.format.DateTimeFormatter;
/*     */ import org.joda.time.format.ISODateTimeFormat;
/*     */ import org.springframework.format.annotation.DateTimeFormat.ISO;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DateTimeFormatterFactory
/*     */ {
/*     */   private String pattern;
/*     */   private DateTimeFormat.ISO iso;
/*     */   private String style;
/*     */   private TimeZone timeZone;
/*     */   
/*     */   public DateTimeFormatterFactory() {}
/*     */   
/*     */   public DateTimeFormatterFactory(String pattern)
/*     */   {
/*  67 */     this.pattern = pattern;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPattern(String pattern)
/*     */   {
/*  76 */     this.pattern = pattern;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setIso(DateTimeFormat.ISO iso)
/*     */   {
/*  84 */     this.iso = iso;
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
/*     */   public void setStyle(String style)
/*     */   {
/* 101 */     this.style = style;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimeZone(TimeZone timeZone)
/*     */   {
/* 109 */     this.timeZone = timeZone;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DateTimeFormatter createDateTimeFormatter()
/*     */   {
/* 121 */     return createDateTimeFormatter(DateTimeFormat.mediumDateTime());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DateTimeFormatter createDateTimeFormatter(DateTimeFormatter fallbackFormatter)
/*     */   {
/* 133 */     DateTimeFormatter dateTimeFormatter = null;
/* 134 */     if (StringUtils.hasLength(this.pattern)) {
/* 135 */       dateTimeFormatter = DateTimeFormat.forPattern(this.pattern);
/*     */     } else {
/* 137 */       if ((this.iso != null) && (this.iso != DateTimeFormat.ISO.NONE)) {}
/* 138 */       switch (this.iso) {
/*     */       case DATE: 
/* 140 */         dateTimeFormatter = ISODateTimeFormat.date();
/* 141 */         break;
/*     */       case TIME: 
/* 143 */         dateTimeFormatter = ISODateTimeFormat.time();
/* 144 */         break;
/*     */       case DATE_TIME: 
/* 146 */         dateTimeFormatter = ISODateTimeFormat.dateTime();
/* 147 */         break;
/*     */       case NONE: 
/*     */         break;
/*     */       
/*     */       default: 
/* 152 */         throw new IllegalStateException("Unsupported ISO format: " + this.iso);
/*     */         
/*     */ 
/* 155 */         if (StringUtils.hasLength(this.style))
/* 156 */           dateTimeFormatter = DateTimeFormat.forStyle(this.style);
/*     */         break; }
/*     */     }
/* 159 */     if ((dateTimeFormatter != null) && (this.timeZone != null)) {
/* 160 */       dateTimeFormatter = dateTimeFormatter.withZone(DateTimeZone.forTimeZone(this.timeZone));
/*     */     }
/* 162 */     return dateTimeFormatter != null ? dateTimeFormatter : fallbackFormatter;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\datetime\joda\DateTimeFormatterFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */