/*    */ package org.springframework.format.datetime.joda;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import org.joda.time.ReadablePartial;
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
/*    */ public final class ReadablePartialPrinter
/*    */   implements Printer<ReadablePartial>
/*    */ {
/*    */   private final DateTimeFormatter formatter;
/*    */   
/*    */   public ReadablePartialPrinter(DateTimeFormatter formatter)
/*    */   {
/* 42 */     this.formatter = formatter;
/*    */   }
/*    */   
/*    */ 
/*    */   public String print(ReadablePartial partial, Locale locale)
/*    */   {
/* 48 */     return JodaTimeContextHolder.getFormatter(this.formatter, locale).print(partial);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\datetime\joda\ReadablePartialPrinter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */