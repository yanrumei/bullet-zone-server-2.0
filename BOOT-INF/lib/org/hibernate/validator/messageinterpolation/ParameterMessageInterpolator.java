/*    */ package org.hibernate.validator.messageinterpolation;
/*    */ 
/*    */ import java.util.Locale;
/*    */ import javax.validation.MessageInterpolator.Context;
/*    */ import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTerm;
/*    */ import org.hibernate.validator.internal.engine.messageinterpolation.ParameterTermResolver;
/*    */ import org.hibernate.validator.internal.util.logging.Log;
/*    */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
/*    */ public class ParameterMessageInterpolator
/*    */   extends AbstractMessageInterpolator
/*    */ {
/* 25 */   private static final Log log = ;
/*    */   
/*    */   public ParameterMessageInterpolator() {
/* 28 */     log.creationOfParameterMessageInterpolation();
/*    */   }
/*    */   
/*    */   public String interpolate(MessageInterpolator.Context context, Locale locale, String term)
/*    */   {
/* 33 */     if (InterpolationTerm.isElExpression(term)) {
/* 34 */       log.getElUnsupported(term);
/* 35 */       return term;
/*    */     }
/*    */     
/* 38 */     ParameterTermResolver parameterTermResolver = new ParameterTermResolver();
/* 39 */     return parameterTermResolver.interpolate(context, term);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\messageinterpolation\ParameterMessageInterpolator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */