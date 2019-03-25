/*     */ package org.hibernate.validator.internal.engine.messageinterpolation.el;
/*     */ 
/*     */ import java.beans.FeatureDescriptor;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.IllegalFormatException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.el.ELContext;
/*     */ import javax.el.ELException;
/*     */ import javax.el.ELResolver;
/*     */ import javax.el.PropertyNotFoundException;
/*     */ import javax.el.ValueExpression;
/*     */ import javax.el.VariableMapper;
/*     */ import javax.validation.ValidationException;
/*     */ import org.hibernate.validator.internal.engine.messageinterpolation.FormatterWrapper;
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
/*     */ 
/*     */ public class RootResolver
/*     */   extends ELResolver
/*     */ {
/*     */   public static final String FORMATTER = "formatter";
/*     */   private static final String FORMAT = "format";
/*  33 */   private final Map<String, Object> map = Collections.synchronizedMap(new HashMap());
/*     */   
/*     */   public Class<?> getCommonPropertyType(ELContext context, Object base)
/*     */   {
/*  37 */     return base == null ? String.class : null;
/*     */   }
/*     */   
/*     */   public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base)
/*     */   {
/*  42 */     return null;
/*     */   }
/*     */   
/*     */   public Class<?> getType(ELContext context, Object base, Object property)
/*     */   {
/*  47 */     return resolve(context, base, property) ? Object.class : null;
/*     */   }
/*     */   
/*     */   public Object getValue(ELContext context, Object base, Object property)
/*     */   {
/*  52 */     if (resolve(context, base, property)) {
/*  53 */       if (!isProperty((String)property)) {
/*  54 */         throw new PropertyNotFoundException("Cannot find property " + property);
/*     */       }
/*  56 */       return getProperty((String)property);
/*     */     }
/*  58 */     return null;
/*     */   }
/*     */   
/*     */   public boolean isReadOnly(ELContext context, Object base, Object property)
/*     */   {
/*  63 */     return false;
/*     */   }
/*     */   
/*     */   public void setValue(ELContext context, Object base, Object property, Object value)
/*     */   {
/*  68 */     if (resolve(context, base, property)) {
/*  69 */       setProperty((String)property, value);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object invoke(ELContext context, Object base, Object method, Class<?>[] paramTypes, Object[] params)
/*     */   {
/*  75 */     if (resolve(context, base, method)) {
/*  76 */       throw new ValidationException("Invalid property");
/*     */     }
/*     */     
/*  79 */     Object returnValue = null;
/*     */     
/*     */ 
/*     */ 
/*  83 */     if ((base instanceof FormatterWrapper)) {
/*  84 */       returnValue = evaluateFormatExpression(context, method, params);
/*     */     }
/*     */     
/*  87 */     return returnValue;
/*     */   }
/*     */   
/*     */   private Object evaluateFormatExpression(ELContext context, Object method, Object[] params) {
/*  91 */     if (!"format".equals(method)) {
/*  92 */       throw new ELException("Wrong method name 'formatter#" + method + "' does not exist. Only formatter#format is supported.");
/*     */     }
/*     */     
/*  95 */     if (params.length == 0) {
/*  96 */       throw new ELException("Invalid number of arguments to Formatter#format");
/*     */     }
/*     */     
/*  99 */     if (!(params[0] instanceof String)) {
/* 100 */       throw new ELException("The first argument to Formatter#format must be String");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 105 */     FormatterWrapper formatterWrapper = (FormatterWrapper)context.getVariableMapper().resolveVariable("formatter").getValue(context);
/* 106 */     Object[] formattingParameters = new Object[params.length - 1];
/* 107 */     System.arraycopy(params, 1, formattingParameters, 0, params.length - 1);
/*     */     
/*     */     try
/*     */     {
/* 111 */       Object returnValue = formatterWrapper.format((String)params[0], formattingParameters);
/* 112 */       context.setPropertyResolved(true);
/*     */     }
/*     */     catch (IllegalFormatException e) {
/* 115 */       throw new ELException("Error in Formatter#format call", e);
/*     */     }
/*     */     Object returnValue;
/* 118 */     return returnValue;
/*     */   }
/*     */   
/*     */   private Object getProperty(String property) {
/* 122 */     return this.map.get(property);
/*     */   }
/*     */   
/*     */   private void setProperty(String property, Object value) {
/* 126 */     this.map.put(property, value);
/*     */   }
/*     */   
/*     */   private boolean isProperty(String property) {
/* 130 */     return this.map.containsKey(property);
/*     */   }
/*     */   
/*     */   private boolean resolve(ELContext context, Object base, Object property) {
/* 134 */     context.setPropertyResolved((base == null) && ((property instanceof String)));
/* 135 */     return context.isPropertyResolved();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\el\RootResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */