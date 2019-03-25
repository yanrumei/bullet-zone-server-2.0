/*     */ package org.springframework.format.datetime;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.format.FormatterRegistrar;
/*     */ import org.springframework.format.FormatterRegistry;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DateFormatterRegistrar
/*     */   implements FormatterRegistrar
/*     */ {
/*     */   private DateFormatter dateFormatter;
/*     */   
/*     */   public void setFormatter(DateFormatter dateFormatter)
/*     */   {
/*  54 */     Assert.notNull(dateFormatter, "DateFormatter must not be null");
/*  55 */     this.dateFormatter = dateFormatter;
/*     */   }
/*     */   
/*     */ 
/*     */   public void registerFormatters(FormatterRegistry registry)
/*     */   {
/*  61 */     addDateConverters(registry);
/*  62 */     registry.addFormatterForFieldAnnotation(new DateTimeFormatAnnotationFormatterFactory());
/*     */     
/*     */ 
/*     */ 
/*  66 */     if (this.dateFormatter != null) {
/*  67 */       registry.addFormatter(this.dateFormatter);
/*  68 */       registry.addFormatterForFieldType(Calendar.class, this.dateFormatter);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void addDateConverters(ConverterRegistry converterRegistry)
/*     */   {
/*  77 */     converterRegistry.addConverter(new DateToLongConverter(null));
/*  78 */     converterRegistry.addConverter(new DateToCalendarConverter(null));
/*  79 */     converterRegistry.addConverter(new CalendarToDateConverter(null));
/*  80 */     converterRegistry.addConverter(new CalendarToLongConverter(null));
/*  81 */     converterRegistry.addConverter(new LongToDateConverter(null));
/*  82 */     converterRegistry.addConverter(new LongToCalendarConverter(null));
/*     */   }
/*     */   
/*     */   private static class DateToLongConverter
/*     */     implements Converter<Date, Long>
/*     */   {
/*     */     public Long convert(Date source)
/*     */     {
/*  90 */       return Long.valueOf(source.getTime());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateToCalendarConverter
/*     */     implements Converter<Date, Calendar>
/*     */   {
/*     */     public Calendar convert(Date source)
/*     */     {
/*  99 */       Calendar calendar = Calendar.getInstance();
/* 100 */       calendar.setTime(source);
/* 101 */       return calendar;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CalendarToDateConverter
/*     */     implements Converter<Calendar, Date>
/*     */   {
/*     */     public Date convert(Calendar source)
/*     */     {
/* 110 */       return source.getTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CalendarToLongConverter
/*     */     implements Converter<Calendar, Long>
/*     */   {
/*     */     public Long convert(Calendar source)
/*     */     {
/* 119 */       return Long.valueOf(source.getTimeInMillis());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LongToDateConverter
/*     */     implements Converter<Long, Date>
/*     */   {
/*     */     public Date convert(Long source)
/*     */     {
/* 128 */       return new Date(source.longValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LongToCalendarConverter
/*     */     implements Converter<Long, Calendar>
/*     */   {
/*     */     public Calendar convert(Long source)
/*     */     {
/* 137 */       Calendar calendar = Calendar.getInstance();
/* 138 */       calendar.setTimeInMillis(source.longValue());
/* 139 */       return calendar;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\datetime\DateFormatterRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */