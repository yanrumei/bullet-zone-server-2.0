/*     */ package org.springframework.format.datetime.standard;
/*     */ 
/*     */ import java.time.Instant;
/*     */ import java.time.LocalDate;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.LocalTime;
/*     */ import java.time.OffsetDateTime;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.chrono.ChronoZonedDateTime;
/*     */ import java.util.Calendar;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.TimeZone;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.core.convert.converter.ConverterRegistry;
/*     */ import org.springframework.format.datetime.DateFormatterRegistrar;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ @UsesJava8
/*     */ final class DateTimeConverters
/*     */ {
/*     */   public static void registerConverters(ConverterRegistry registry)
/*     */   {
/*  54 */     DateFormatterRegistrar.addDateConverters(registry);
/*     */     
/*  56 */     registry.addConverter(new LocalDateTimeToLocalDateConverter(null));
/*  57 */     registry.addConverter(new LocalDateTimeToLocalTimeConverter(null));
/*  58 */     registry.addConverter(new ZonedDateTimeToLocalDateConverter(null));
/*  59 */     registry.addConverter(new ZonedDateTimeToLocalTimeConverter(null));
/*  60 */     registry.addConverter(new ZonedDateTimeToLocalDateTimeConverter(null));
/*  61 */     registry.addConverter(new ZonedDateTimeToOffsetDateTimeConverter(null));
/*  62 */     registry.addConverter(new ZonedDateTimeToInstantConverter(null));
/*  63 */     registry.addConverter(new OffsetDateTimeToLocalDateConverter(null));
/*  64 */     registry.addConverter(new OffsetDateTimeToLocalTimeConverter(null));
/*  65 */     registry.addConverter(new OffsetDateTimeToLocalDateTimeConverter(null));
/*  66 */     registry.addConverter(new OffsetDateTimeToZonedDateTimeConverter(null));
/*  67 */     registry.addConverter(new OffsetDateTimeToInstantConverter(null));
/*  68 */     registry.addConverter(new CalendarToZonedDateTimeConverter(null));
/*  69 */     registry.addConverter(new CalendarToOffsetDateTimeConverter(null));
/*  70 */     registry.addConverter(new CalendarToLocalDateConverter(null));
/*  71 */     registry.addConverter(new CalendarToLocalTimeConverter(null));
/*  72 */     registry.addConverter(new CalendarToLocalDateTimeConverter(null));
/*  73 */     registry.addConverter(new CalendarToInstantConverter(null));
/*  74 */     registry.addConverter(new LongToInstantConverter(null));
/*  75 */     registry.addConverter(new InstantToLongConverter(null));
/*     */   }
/*     */   
/*     */   private static ZonedDateTime calendarToZonedDateTime(Calendar source) {
/*  79 */     if ((source instanceof GregorianCalendar)) {
/*  80 */       return ((GregorianCalendar)source).toZonedDateTime();
/*     */     }
/*     */     
/*  83 */     return ZonedDateTime.ofInstant(Instant.ofEpochMilli(source.getTimeInMillis()), source
/*  84 */       .getTimeZone().toZoneId());
/*     */   }
/*     */   
/*     */ 
/*     */   @UsesJava8
/*     */   private static class LocalDateTimeToLocalDateConverter
/*     */     implements Converter<LocalDateTime, LocalDate>
/*     */   {
/*     */     public LocalDate convert(LocalDateTime source)
/*     */     {
/*  94 */       return source.toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class LocalDateTimeToLocalTimeConverter
/*     */     implements Converter<LocalDateTime, LocalTime>
/*     */   {
/*     */     public LocalTime convert(LocalDateTime source)
/*     */     {
/* 104 */       return source.toLocalTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class ZonedDateTimeToLocalDateConverter
/*     */     implements Converter<ZonedDateTime, LocalDate>
/*     */   {
/*     */     public LocalDate convert(ZonedDateTime source)
/*     */     {
/* 114 */       return source.toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class ZonedDateTimeToLocalTimeConverter
/*     */     implements Converter<ZonedDateTime, LocalTime>
/*     */   {
/*     */     public LocalTime convert(ZonedDateTime source)
/*     */     {
/* 124 */       return source.toLocalTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class ZonedDateTimeToLocalDateTimeConverter
/*     */     implements Converter<ZonedDateTime, LocalDateTime>
/*     */   {
/*     */     public LocalDateTime convert(ZonedDateTime source)
/*     */     {
/* 134 */       return source.toLocalDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class ZonedDateTimeToOffsetDateTimeConverter implements Converter<ZonedDateTime, OffsetDateTime>
/*     */   {
/*     */     public OffsetDateTime convert(ZonedDateTime source)
/*     */     {
/* 143 */       return source.toOffsetDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @UsesJava8
/*     */   private static class ZonedDateTimeToInstantConverter
/*     */     implements Converter<ZonedDateTime, Instant>
/*     */   {
/*     */     public Instant convert(ZonedDateTime source)
/*     */     {
/* 154 */       return source.toInstant();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class OffsetDateTimeToLocalDateConverter
/*     */     implements Converter<OffsetDateTime, LocalDate>
/*     */   {
/*     */     public LocalDate convert(OffsetDateTime source)
/*     */     {
/* 164 */       return source.toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class OffsetDateTimeToLocalTimeConverter
/*     */     implements Converter<OffsetDateTime, LocalTime>
/*     */   {
/*     */     public LocalTime convert(OffsetDateTime source)
/*     */     {
/* 174 */       return source.toLocalTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class OffsetDateTimeToLocalDateTimeConverter
/*     */     implements Converter<OffsetDateTime, LocalDateTime>
/*     */   {
/*     */     public LocalDateTime convert(OffsetDateTime source)
/*     */     {
/* 184 */       return source.toLocalDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class OffsetDateTimeToZonedDateTimeConverter
/*     */     implements Converter<OffsetDateTime, ZonedDateTime>
/*     */   {
/*     */     public ZonedDateTime convert(OffsetDateTime source)
/*     */     {
/* 194 */       return source.toZonedDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class OffsetDateTimeToInstantConverter
/*     */     implements Converter<OffsetDateTime, Instant>
/*     */   {
/*     */     public Instant convert(OffsetDateTime source)
/*     */     {
/* 204 */       return source.toInstant();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class CalendarToZonedDateTimeConverter
/*     */     implements Converter<Calendar, ZonedDateTime>
/*     */   {
/*     */     public ZonedDateTime convert(Calendar source)
/*     */     {
/* 214 */       return DateTimeConverters.calendarToZonedDateTime(source);
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class CalendarToOffsetDateTimeConverter
/*     */     implements Converter<Calendar, OffsetDateTime>
/*     */   {
/*     */     public OffsetDateTime convert(Calendar source)
/*     */     {
/* 224 */       return DateTimeConverters.calendarToZonedDateTime(source).toOffsetDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class CalendarToLocalDateConverter
/*     */     implements Converter<Calendar, LocalDate>
/*     */   {
/*     */     public LocalDate convert(Calendar source)
/*     */     {
/* 234 */       return DateTimeConverters.calendarToZonedDateTime(source).toLocalDate();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class CalendarToLocalTimeConverter
/*     */     implements Converter<Calendar, LocalTime>
/*     */   {
/*     */     public LocalTime convert(Calendar source)
/*     */     {
/* 244 */       return DateTimeConverters.calendarToZonedDateTime(source).toLocalTime();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class CalendarToLocalDateTimeConverter
/*     */     implements Converter<Calendar, LocalDateTime>
/*     */   {
/*     */     public LocalDateTime convert(Calendar source)
/*     */     {
/* 254 */       return DateTimeConverters.calendarToZonedDateTime(source).toLocalDateTime();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @UsesJava8
/*     */   private static class CalendarToInstantConverter
/*     */     implements Converter<Calendar, Instant>
/*     */   {
/*     */     public Instant convert(Calendar source)
/*     */     {
/* 265 */       return DateTimeConverters.calendarToZonedDateTime(source).toInstant();
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class LongToInstantConverter
/*     */     implements Converter<Long, Instant>
/*     */   {
/*     */     public Instant convert(Long source)
/*     */     {
/* 275 */       return Instant.ofEpochMilli(source.longValue());
/*     */     }
/*     */   }
/*     */   
/*     */   @UsesJava8
/*     */   private static class InstantToLongConverter
/*     */     implements Converter<Instant, Long>
/*     */   {
/*     */     public Long convert(Instant source)
/*     */     {
/* 285 */       return Long.valueOf(source.toEpochMilli());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\datetime\standard\DateTimeConverters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */