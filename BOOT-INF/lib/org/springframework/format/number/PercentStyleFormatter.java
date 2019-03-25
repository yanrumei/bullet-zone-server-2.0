/*    */ package org.springframework.format.number;
/*    */ 
/*    */ import java.text.DecimalFormat;
/*    */ import java.text.NumberFormat;
/*    */ import java.util.Locale;
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
/*    */ public class PercentStyleFormatter
/*    */   extends AbstractNumberFormatter
/*    */ {
/*    */   protected NumberFormat getNumberFormat(Locale locale)
/*    */   {
/* 39 */     NumberFormat format = NumberFormat.getPercentInstance(locale);
/* 40 */     if ((format instanceof DecimalFormat)) {
/* 41 */       ((DecimalFormat)format).setParseBigDecimal(true);
/*    */     }
/* 43 */     return format;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\number\PercentStyleFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */