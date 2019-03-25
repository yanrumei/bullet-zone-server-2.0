/*    */ package org.springframework.format.datetime.joda;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.util.Locale;
/*    */ import org.joda.time.Period;
/*    */ import org.springframework.format.Formatter;
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
/*    */ class PeriodFormatter
/*    */   implements Formatter<Period>
/*    */ {
/*    */   public Period parse(String text, Locale locale)
/*    */     throws ParseException
/*    */   {
/* 38 */     return Period.parse(text);
/*    */   }
/*    */   
/*    */   public String print(Period object, Locale locale)
/*    */   {
/* 43 */     return object.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\datetime\joda\PeriodFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */