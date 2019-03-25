/*     */ package org.springframework.format.datetime.standard;
/*     */ 
/*     */ import java.time.Duration;
/*     */ import java.time.Instant;
/*     */ import java.time.LocalDate;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.LocalTime;
/*     */ import java.time.MonthDay;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.time.OffsetTime;
/*     */ import java.time.Period;
/*     */ import java.time.YearMonth;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.time.format.FormatStyle;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.format.FormatterRegistrar;
/*     */ import org.springframework.format.FormatterRegistry;
/*     */ import org.springframework.format.annotation.DateTimeFormat.ISO;
/*     */ import org.springframework.lang.UsesJava8;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @UsesJava8
/*     */ public class DateTimeFormatterRegistrar
/*     */   implements FormatterRegistrar
/*     */ {
/*     */   private static enum Type
/*     */   {
/*  57 */     DATE,  TIME,  DATE_TIME;
/*     */     
/*     */ 
/*     */     private Type() {}
/*     */   }
/*     */   
/*  63 */   private final Map<Type, DateTimeFormatter> formatters = new EnumMap(Type.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   private final Map<Type, DateTimeFormatterFactory> factories = new EnumMap(Type.class);
/*     */   
/*     */ 
/*     */   public DateTimeFormatterRegistrar()
/*     */   {
/*  74 */     for (Type type : Type.values()) {
/*  75 */       this.factories.put(type, new DateTimeFormatterFactory());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUseIsoFormat(boolean useIsoFormat)
/*     */   {
/*  87 */     ((DateTimeFormatterFactory)this.factories.get(Type.DATE)).setIso(useIsoFormat ? DateTimeFormat.ISO.DATE : null);
/*  88 */     ((DateTimeFormatterFactory)this.factories.get(Type.TIME)).setIso(useIsoFormat ? DateTimeFormat.ISO.TIME : null);
/*  89 */     ((DateTimeFormatterFactory)this.factories.get(Type.DATE_TIME)).setIso(useIsoFormat ? DateTimeFormat.ISO.DATE_TIME : null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDateStyle(FormatStyle dateStyle)
/*     */   {
/*  97 */     ((DateTimeFormatterFactory)this.factories.get(Type.DATE)).setDateStyle(dateStyle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTimeStyle(FormatStyle timeStyle)
/*     */   {
/* 105 */     ((DateTimeFormatterFactory)this.factories.get(Type.TIME)).setTimeStyle(timeStyle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDateTimeStyle(FormatStyle dateTimeStyle)
/*     */   {
/* 113 */     ((DateTimeFormatterFactory)this.factories.get(Type.DATE_TIME)).setDateTimeStyle(dateTimeStyle);
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
/*     */   public void setDateFormatter(DateTimeFormatter formatter)
/*     */   {
/* 126 */     this.formatters.put(Type.DATE, formatter);
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
/*     */   public void setTimeFormatter(DateTimeFormatter formatter)
/*     */   {
/* 139 */     this.formatters.put(Type.TIME, formatter);
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
/*     */   public void setDateTimeFormatter(DateTimeFormatter formatter)
/*     */   {
/* 153 */     this.formatters.put(Type.DATE_TIME, formatter);
/*     */   }
/*     */   
/*     */ 
/*     */   public void registerFormatters(FormatterRegistry registry)
/*     */   {
/* 159 */     DateTimeConverters.registerConverters(registry);
/*     */     
/* 161 */     DateTimeFormatter df = getFormatter(Type.DATE);
/* 162 */     DateTimeFormatter tf = getFormatter(Type.TIME);
/* 163 */     DateTimeFormatter dtf = getFormatter(Type.DATE_TIME);
/*     */     
/*     */ 
/*     */ 
/* 167 */     registry.addFormatterForFieldType(LocalDate.class, new TemporalAccessorPrinter(df == DateTimeFormatter.ISO_DATE ? DateTimeFormatter.ISO_LOCAL_DATE : df), new TemporalAccessorParser(LocalDate.class, df));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 172 */     registry.addFormatterForFieldType(LocalTime.class, new TemporalAccessorPrinter(tf == DateTimeFormatter.ISO_TIME ? DateTimeFormatter.ISO_LOCAL_TIME : tf), new TemporalAccessorParser(LocalTime.class, tf));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 177 */     registry.addFormatterForFieldType(LocalDateTime.class, new TemporalAccessorPrinter(dtf == DateTimeFormatter.ISO_DATE_TIME ? DateTimeFormatter.ISO_LOCAL_DATE_TIME : dtf), new TemporalAccessorParser(LocalDateTime.class, dtf));
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 182 */     registry.addFormatterForFieldType(ZonedDateTime.class, new TemporalAccessorPrinter(dtf), new TemporalAccessorParser(ZonedDateTime.class, dtf));
/*     */     
/*     */ 
/*     */ 
/* 186 */     registry.addFormatterForFieldType(OffsetDateTime.class, new TemporalAccessorPrinter(dtf), new TemporalAccessorParser(OffsetDateTime.class, dtf));
/*     */     
/*     */ 
/*     */ 
/* 190 */     registry.addFormatterForFieldType(OffsetTime.class, new TemporalAccessorPrinter(tf), new TemporalAccessorParser(OffsetTime.class, tf));
/*     */     
/*     */ 
/*     */ 
/* 194 */     registry.addFormatterForFieldType(Instant.class, new InstantFormatter());
/* 195 */     registry.addFormatterForFieldType(Period.class, new PeriodFormatter());
/* 196 */     registry.addFormatterForFieldType(Duration.class, new DurationFormatter());
/* 197 */     registry.addFormatterForFieldType(YearMonth.class, new YearMonthFormatter());
/* 198 */     registry.addFormatterForFieldType(MonthDay.class, new MonthDayFormatter());
/*     */     
/* 200 */     registry.addFormatterForFieldAnnotation(new Jsr310DateTimeFormatAnnotationFormatterFactory());
/*     */   }
/*     */   
/*     */   private DateTimeFormatter getFormatter(Type type) {
/* 204 */     DateTimeFormatter formatter = (DateTimeFormatter)this.formatters.get(type);
/* 205 */     if (formatter != null) {
/* 206 */       return formatter;
/*     */     }
/* 208 */     DateTimeFormatter fallbackFormatter = getFallbackFormatter(type);
/* 209 */     return ((DateTimeFormatterFactory)this.factories.get(type)).createDateTimeFormatter(fallbackFormatter);
/*     */   }
/*     */   
/*     */   private DateTimeFormatter getFallbackFormatter(Type type) {
/* 213 */     switch (type) {
/* 214 */     case DATE:  return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
/* 215 */     case TIME:  return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT); }
/* 216 */     return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\datetime\standard\DateTimeFormatterRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */