/*    */ package org.springframework.format.datetime.standard;
/*    */ 
/*    */ import java.text.ParseException;
/*    */ import java.time.YearMonth;
/*    */ import java.util.Locale;
/*    */ import org.springframework.format.Formatter;
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
/*    */ @UsesJava8
/*    */ class YearMonthFormatter
/*    */   implements Formatter<YearMonth>
/*    */ {
/*    */   public YearMonth parse(String text, Locale locale)
/*    */     throws ParseException
/*    */   {
/* 39 */     return YearMonth.parse(text);
/*    */   }
/*    */   
/*    */   public String print(YearMonth object, Locale locale)
/*    */   {
/* 44 */     return object.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\datetime\standard\YearMonthFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */