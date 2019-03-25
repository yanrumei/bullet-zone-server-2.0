/*    */ package org.hibernate.validator.internal.engine.messageinterpolation;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import javax.el.ExpressionFactory;
/*    */ import javax.validation.MessageInterpolator.Context;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InterpolationTerm
/*    */ {
/*    */   private static final String EL_DESIGNATION_CHARACTER = "$";
/*    */   private final String expression;
/*    */   private final InterpolationTermType type;
/*    */   private final TermResolver resolver;
/*    */   
/*    */   public InterpolationTerm(String expression, Locale locale, ExpressionFactory expressionFactory)
/*    */   {
/* 48 */     this.expression = expression;
/* 49 */     if (isElExpression(expression)) {
/* 50 */       this.type = InterpolationTermType.EL;
/* 51 */       this.resolver = new ElTermResolver(locale, expressionFactory);
/*    */     }
/*    */     else {
/* 54 */       this.type = InterpolationTermType.PARAMETER;
/* 55 */       this.resolver = new ParameterTermResolver();
/*    */     }
/*    */   }
/*    */   
/*    */   public static boolean isElExpression(String expression) {
/* 60 */     return expression.startsWith("$");
/*    */   }
/*    */   
/*    */   public String interpolate(MessageInterpolator.Context context) {
/* 64 */     return this.resolver.interpolate(context, this.expression);
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 69 */     StringBuilder sb = new StringBuilder();
/* 70 */     sb.append("InterpolationExpression");
/* 71 */     sb.append("{expression='").append(this.expression).append('\'');
/* 72 */     sb.append(", type=").append(this.type);
/* 73 */     sb.append('}');
/* 74 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\InterpolationTerm.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */