/*    */ package org.springframework.boot.validation;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.Set;
/*    */ import javax.validation.Configuration;
/*    */ import javax.validation.MessageInterpolator;
/*    */ import javax.validation.Validation;
/*    */ import javax.validation.ValidationException;
/*    */ import javax.validation.bootstrap.GenericBootstrap;
/*    */ import org.springframework.beans.BeanUtils;
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.ObjectFactory;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public class MessageInterpolatorFactory
/*    */   implements ObjectFactory<MessageInterpolator>
/*    */ {
/*    */   private static final Set<String> FALLBACKS;
/*    */   
/*    */   static
/*    */   {
/* 44 */     Set<String> fallbacks = new LinkedHashSet();
/* 45 */     fallbacks.add("org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator");
/*    */     
/* 47 */     FALLBACKS = Collections.unmodifiableSet(fallbacks);
/*    */   }
/*    */   
/*    */   public MessageInterpolator getObject() throws BeansException
/*    */   {
/*    */     try {
/* 53 */       return 
/* 54 */         Validation.byDefaultProvider().configure().getDefaultMessageInterpolator();
/*    */     }
/*    */     catch (ValidationException ex) {
/* 57 */       MessageInterpolator fallback = getFallback();
/* 58 */       if (fallback != null) {
/* 59 */         return fallback;
/*    */       }
/* 61 */       throw ex;
/*    */     }
/*    */   }
/*    */   
/*    */   private MessageInterpolator getFallback() {
/* 66 */     for (String fallback : FALLBACKS) {
/*    */       try {
/* 68 */         return getFallback(fallback);
/*    */       }
/*    */       catch (Exception localException) {}
/*    */     }
/*    */     
/*    */ 
/* 74 */     return null;
/*    */   }
/*    */   
/*    */   private MessageInterpolator getFallback(String fallback) {
/* 78 */     Class<?> interpolatorClass = ClassUtils.resolveClassName(fallback, null);
/* 79 */     Object interpolator = BeanUtils.instantiate(interpolatorClass);
/* 80 */     return (MessageInterpolator)interpolator;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\validation\MessageInterpolatorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */