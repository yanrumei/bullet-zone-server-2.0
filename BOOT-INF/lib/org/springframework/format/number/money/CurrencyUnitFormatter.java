/*    */ package org.springframework.format.number.money;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import javax.money.CurrencyUnit;
/*    */ import javax.money.Monetary;
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
/*    */ public class CurrencyUnitFormatter
/*    */   implements Formatter<CurrencyUnit>
/*    */ {
/*    */   public String print(CurrencyUnit object, Locale locale)
/*    */   {
/* 36 */     return object.getCurrencyCode();
/*    */   }
/*    */   
/*    */   public CurrencyUnit parse(String text, Locale locale)
/*    */   {
/* 41 */     return Monetary.getCurrency(text, new String[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\format\number\money\CurrencyUnitFormatter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */