/*    */ package org.hibernate.validator.internal.engine.messageinterpolation;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.Map;
/*    */ import javax.validation.MessageInterpolator.Context;
/*    */ import javax.validation.metadata.ConstraintDescriptor;
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
/*    */ public class ParameterTermResolver
/*    */   implements TermResolver
/*    */ {
/*    */   public String interpolate(MessageInterpolator.Context context, String expression)
/*    */   {
/* 26 */     Object variable = context.getConstraintDescriptor().getAttributes().get(removeCurlyBraces(expression));
/* 27 */     String resolvedExpression; String resolvedExpression; if (variable != null) { String resolvedExpression;
/* 28 */       if (variable.getClass().isArray()) {
/* 29 */         resolvedExpression = Arrays.toString((Object[])variable);
/*    */       }
/*    */       else {
/* 32 */         resolvedExpression = variable.toString();
/*    */       }
/*    */     }
/*    */     else {
/* 36 */       resolvedExpression = expression;
/*    */     }
/* 38 */     return resolvedExpression;
/*    */   }
/*    */   
/*    */   private String removeCurlyBraces(String parameter) {
/* 42 */     return parameter.substring(1, parameter.length() - 1);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\ParameterTermResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */