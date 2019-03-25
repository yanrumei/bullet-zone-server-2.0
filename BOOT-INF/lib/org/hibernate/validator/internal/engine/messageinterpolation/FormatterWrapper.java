/*    */ package org.hibernate.validator.internal.engine.messageinterpolation;
/*    */ 
/*    */ import java.util.Formatter;
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
/*    */ public class FormatterWrapper
/*    */ {
/*    */   private final Formatter formatter;
/*    */   
/*    */   public FormatterWrapper(Locale locale)
/*    */   {
/* 22 */     this.formatter = new Formatter(locale);
/*    */   }
/*    */   
/*    */   public String format(String format, Object... args) {
/* 26 */     return this.formatter.format(format, args).toString();
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 31 */     StringBuilder sb = new StringBuilder();
/* 32 */     sb.append("FormatterWrapper");
/* 33 */     sb.append("{}");
/* 34 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\FormatterWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */