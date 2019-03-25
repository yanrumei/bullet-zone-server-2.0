/*    */ package org.springframework.format.datetime.joda;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.joda.time.ReadableInstant;
/*    */ import org.joda.time.format.DateTimeFormatter;
/*    */ import org.springframework.format.Printer;
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
/*    */ public final class ReadableInstantPrinter
/*    */   implements Printer<ReadableInstant>
/*    */ {
/*    */   private final DateTimeFormatter formatter;
/*    */   
/*    */   public ReadableInstantPrinter(DateTimeFormatter formatter)
/*    */   {
/* 42 */     this.formatter = formatter;
/*    */   }
/*    */   
/*    */ 
/*    */   public String print(ReadableInstant instant, Locale locale)
/*    */   {
/* 48 */     return JodaTimeContextHolder.getFormatter(this.formatter, locale).print(instant);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\datetime\joda\ReadableInstantPrinter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */