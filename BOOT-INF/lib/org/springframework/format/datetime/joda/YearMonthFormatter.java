/*    */ package org.springframework.format.datetime.joda;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.util.Locale;
/*    */ import org.joda.time.YearMonth;
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
/*    */ class YearMonthFormatter
/*    */   implements Formatter<YearMonth>
/*    */ {
/*    */   public YearMonth parse(String text, Locale locale)
/*    */     throws ParseException
/*    */   {
/* 38 */     return YearMonth.parse(text);
/*    */   }
/*    */   
/*    */   public String print(YearMonth object, Locale locale)
/*    */   {
/* 43 */     return object.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\datetime\joda\YearMonthFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */