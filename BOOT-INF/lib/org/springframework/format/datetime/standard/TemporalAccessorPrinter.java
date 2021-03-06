/*    */ package org.springframework.format.datetime.standard;
/*    */ 
/*    */ import java.time.format.DateTimeFormatter;
/*    */ import java.time.temporal.TemporalAccessor;
/*    */ import java.util.Locale;
/*    */ import org.springframework.format.Printer;
/*    */ import org.springframework.lang.UsesJava8;
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
/*    */ 
/*    */ @UsesJava8
/*    */ public final class TemporalAccessorPrinter
/*    */   implements Printer<TemporalAccessor>
/*    */ {
/*    */   private final DateTimeFormatter formatter;
/*    */   
/*    */   public TemporalAccessorPrinter(DateTimeFormatter formatter)
/*    */   {
/* 46 */     this.formatter = formatter;
/*    */   }
/*    */   
/*    */ 
/*    */   public String print(TemporalAccessor partial, Locale locale)
/*    */   {
/* 52 */     return DateTimeContextHolder.getFormatter(this.formatter, locale).format(partial);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\datetime\standard\TemporalAccessorPrinter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */