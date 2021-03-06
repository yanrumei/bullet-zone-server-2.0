/*    */ package org.springframework.format.datetime.joda;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.util.Locale;
/*    */ import org.joda.time.LocalTime;
/*    */ import org.joda.time.format.DateTimeFormatter;
/*    */ import org.springframework.format.Parser;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class LocalTimeParser
/*    */   implements Parser<LocalTime>
/*    */ {
/*    */   private final DateTimeFormatter formatter;
/*    */   
/*    */   public LocalTimeParser(DateTimeFormatter formatter)
/*    */   {
/* 44 */     this.formatter = formatter;
/*    */   }
/*    */   
/*    */   public LocalTime parse(String text, Locale locale)
/*    */     throws ParseException
/*    */   {
/* 50 */     return JodaTimeContextHolder.getFormatter(this.formatter, locale).parseLocalTime(text);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\datetime\joda\LocalTimeParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */