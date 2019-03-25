/*     */ package org.hibernate.validator.internal.engine.messageinterpolation;
/*     */ 
/*     */ import java.util.Iterator;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.el.ELException;
/*     */ import javax.el.ExpressionFactory;
/*     */ import javax.el.PropertyNotFoundException;
/*     */ import javax.el.ValueExpression;
/*     */ import javax.validation.MessageInterpolator.Context;
/*     */ import javax.validation.metadata.ConstraintDescriptor;
/*     */ import org.hibernate.validator.internal.engine.MessageInterpolatorContext;
/*     */ import org.hibernate.validator.internal.engine.messageinterpolation.el.SimpleELContext;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ElTermResolver
/*     */   implements TermResolver
/*     */ {
/*  31 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String VALIDATED_VALUE_NAME = "validatedValue";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Locale locale;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ExpressionFactory expressionFactory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ElTermResolver(Locale locale, ExpressionFactory expressionFactory)
/*     */   {
/*  55 */     this.locale = locale;
/*  56 */     this.expressionFactory = expressionFactory;
/*     */   }
/*     */   
/*     */   public String interpolate(MessageInterpolator.Context context, String expression)
/*     */   {
/*  61 */     String resolvedExpression = expression;
/*  62 */     SimpleELContext elContext = new SimpleELContext(this.expressionFactory);
/*     */     try {
/*  64 */       ValueExpression valueExpression = bindContextValues(expression, context, elContext);
/*  65 */       resolvedExpression = (String)valueExpression.getValue(elContext);
/*     */     }
/*     */     catch (PropertyNotFoundException pnfe) {
/*  68 */       log.unknownPropertyInExpressionLanguage(expression, pnfe);
/*     */     }
/*     */     catch (ELException e) {
/*  71 */       log.errorInExpressionLanguage(expression, e);
/*     */     }
/*     */     catch (Exception e) {
/*  74 */       log.evaluatingExpressionLanguageExpressionCausedException(expression, e);
/*     */     }
/*     */     
/*  77 */     return resolvedExpression;
/*     */   }
/*     */   
/*     */   private ValueExpression bindContextValues(String messageTemplate, MessageInterpolator.Context messageInterpolatorContext, SimpleELContext elContext)
/*     */   {
/*  82 */     ValueExpression valueExpression = this.expressionFactory.createValueExpression(messageInterpolatorContext
/*  83 */       .getValidatedValue(), Object.class);
/*     */     
/*     */ 
/*  86 */     elContext.setVariable("validatedValue", valueExpression);
/*     */     
/*     */ 
/*  89 */     valueExpression = this.expressionFactory.createValueExpression(new FormatterWrapper(this.locale), FormatterWrapper.class);
/*     */     
/*     */ 
/*     */ 
/*  93 */     elContext.setVariable("formatter", valueExpression);
/*     */     
/*     */ 
/*  96 */     for (Iterator localIterator = messageInterpolatorContext.getConstraintDescriptor()
/*  97 */           .getAttributes()
/*  98 */           .entrySet().iterator(); localIterator.hasNext();) { entry = (Map.Entry)localIterator.next();
/*     */       
/*     */ 
/*  99 */       valueExpression = this.expressionFactory.createValueExpression(entry.getValue(), Object.class);
/* 100 */       elContext.setVariable((String)entry.getKey(), valueExpression);
/*     */     }
/*     */     
/*     */     Map.Entry<String, Object> entry;
/* 104 */     if ((messageInterpolatorContext instanceof MessageInterpolatorContext)) {
/* 105 */       MessageInterpolatorContext internalContext = (MessageInterpolatorContext)messageInterpolatorContext;
/* 106 */       for (Map.Entry<String, Object> entry : internalContext.getMessageParameters().entrySet()) {
/* 107 */         valueExpression = this.expressionFactory.createValueExpression(entry.getValue(), Object.class);
/* 108 */         elContext.setVariable((String)entry.getKey(), valueExpression);
/*     */       }
/*     */     }
/*     */     
/* 112 */     return this.expressionFactory.createValueExpression(elContext, messageTemplate, String.class);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\ElTermResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */