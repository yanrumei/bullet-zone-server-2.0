/*     */ package org.springframework.format.datetime.joda;
/*     */ 
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import org.joda.time.DateMidnight;
/*     */ import org.joda.time.DateTime;
/*     */ import org.joda.time.Instant;
/*     */ import org.joda.time.LocalDate;
/*     */ import org.joda.time.LocalDateTime;
/*     */ import org.joda.time.LocalTime;
/*     */ import org.joda.time.MutableDateTime;
/*     */ import org.joda.time.ReadableInstant;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.format.datetime.DateFormatterRegistrar;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class JodaTimeConverters
/*     */ {
/*     */   public static void registerConverters(ConverterRegistry registry)
/*     */   {
/*  55 */     DateFormatterRegistrar.addDateConverters(registry);
/*     */     
/*  57 */     registry.addConverter(new DateTimeToLocalDateConverter(null));
/*  58 */     registry.addConverter(new DateTimeToLocalTimeConverter(null));
/*  59 */     registry.addConverter(new DateTimeToLocalDateTimeConverter(null));
/*  60 */     registry.addConverter(new DateTimeToDateMidnightConverter(null));
/*  61 */     registry.addConverter(new DateTimeToMutableDateTimeConverter(null));
/*  62 */     registry.addConverter(new DateTimeToInstantConverter(null));
/*  63 */     registry.addConverter(new DateTimeToDateConverter(null));
/*  64 */     registry.addConverter(new DateTimeToCalendarConverter(null));
/*  65 */     registry.addConverter(new DateTimeToLongConverter(null));
/*  66 */     registry.addConverter(new DateToReadableInstantConverter(null));
/*  67 */     registry.addConverter(new CalendarToReadableInstantConverter(null));
/*  68 */     registry.addConverter(new LongToReadableInstantConverter(null));
/*  69 */     registry.addConverter(new LocalDateTimeToLocalDateConverter(null));
/*  70 */     registry.addConverter(new LocalDateTimeToLocalTimeConverter(null));
/*     */   }
/*     */   
/*     */   private static class DateTimeToLocalDateConverter
/*     */     implements Converter<DateTime, LocalDate>
/*     */   {
/*     */     public LocalDate convert(DateTime source)
/*     */     {
/*  78 */       return source.toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToLocalTimeConverter
/*     */     implements Converter<DateTime, LocalTime>
/*     */   {
/*     */     public LocalTime convert(DateTime source)
/*     */     {
/*  87 */       return source.toLocalTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToLocalDateTimeConverter
/*     */     implements Converter<DateTime, LocalDateTime>
/*     */   {
/*     */     public LocalDateTime convert(DateTime source)
/*     */     {
/*  96 */       return source.toLocalDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   private static class DateTimeToDateMidnightConverter
/*     */     implements Converter<DateTime, DateMidnight>
/*     */   {
/*     */     public DateMidnight convert(DateTime source)
/*     */     {
/* 106 */       return source.toDateMidnight();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToMutableDateTimeConverter
/*     */     implements Converter<DateTime, MutableDateTime>
/*     */   {
/*     */     public MutableDateTime convert(DateTime source)
/*     */     {
/* 115 */       return source.toMutableDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToInstantConverter
/*     */     implements Converter<DateTime, Instant>
/*     */   {
/*     */     public Instant convert(DateTime source)
/*     */     {
/* 124 */       return source.toInstant();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToDateConverter
/*     */     implements Converter<DateTime, Date>
/*     */   {
/*     */     public Date convert(DateTime source)
/*     */     {
/* 133 */       return source.toDate();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToCalendarConverter
/*     */     implements Converter<DateTime, Calendar>
/*     */   {
/*     */     public Calendar convert(DateTime source)
/*     */     {
/* 142 */       return source.toGregorianCalendar();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DateTimeToLongConverter
/*     */     implements Converter<DateTime, Long>
/*     */   {
/*     */     public Long convert(DateTime source)
/*     */     {
/* 151 */       return Long.valueOf(source.getMillis());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class DateToReadableInstantConverter
/*     */     implements Converter<Date, ReadableInstant>
/*     */   {
/*     */     public ReadableInstant convert(Date source)
/*     */     {
/* 165 */       return new DateTime(source);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class CalendarToReadableInstantConverter
/*     */     implements Converter<Calendar, ReadableInstant>
/*     */   {
/*     */     public ReadableInstant convert(Calendar source)
/*     */     {
/* 179 */       return new DateTime(source);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class LongToReadableInstantConverter
/*     */     implements Converter<Long, ReadableInstant>
/*     */   {
/*     */     public ReadableInstant convert(Long source)
/*     */     {
/* 193 */       return new DateTime(source.longValue());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LocalDateTimeToLocalDateConverter
/*     */     implements Converter<LocalDateTime, LocalDate>
/*     */   {
/*     */     public LocalDate convert(LocalDateTime source)
/*     */     {
/* 202 */       return source.toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LocalDateTimeToLocalTimeConverter
/*     */     implements Converter<LocalDateTime, LocalTime>
/*     */   {
/*     */     public LocalTime convert(LocalDateTime source)
/*     */     {
/* 211 */       return source.toLocalTime();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\datetime\joda\JodaTimeConverters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */