/*     */ package org.springframework.boot.diagnostics.analyzer;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.beans.BeanInstantiationException;
/*     */ import org.springframework.beans.factory.InjectionPoint;
/*     */ import org.springframework.beans.factory.UnsatisfiedDependencyException;
/*     */ import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
/*     */ import org.springframework.boot.diagnostics.FailureAnalysis;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ public abstract class AbstractInjectionFailureAnalyzer<T extends Throwable>
/*     */   extends AbstractFailureAnalyzer<T>
/*     */ {
/*     */   protected final FailureAnalysis analyze(Throwable rootFailure, T cause)
/*     */   {
/*  41 */     return analyze(rootFailure, cause, getDescription(rootFailure));
/*     */   }
/*     */   
/*     */   private String getDescription(Throwable rootFailure) {
/*  45 */     UnsatisfiedDependencyException unsatisfiedDependency = (UnsatisfiedDependencyException)findMostNestedCause(rootFailure, UnsatisfiedDependencyException.class);
/*     */     
/*  47 */     if (unsatisfiedDependency != null) {
/*  48 */       return getDescription(unsatisfiedDependency);
/*     */     }
/*  50 */     BeanInstantiationException beanInstantiationException = (BeanInstantiationException)findMostNestedCause(rootFailure, BeanInstantiationException.class);
/*     */     
/*  52 */     if (beanInstantiationException != null) {
/*  53 */       return getDescription(beanInstantiationException);
/*     */     }
/*  55 */     return null;
/*     */   }
/*     */   
/*     */   private <C extends Exception> C findMostNestedCause(Throwable root, Class<C> type)
/*     */   {
/*  60 */     Throwable candidate = root;
/*  61 */     C result = null;
/*  62 */     while (candidate != null) {
/*  63 */       if (type.isAssignableFrom(candidate.getClass())) {
/*  64 */         result = (Exception)candidate;
/*     */       }
/*  66 */       candidate = candidate.getCause();
/*     */     }
/*  68 */     return result;
/*     */   }
/*     */   
/*     */   private String getDescription(UnsatisfiedDependencyException ex) {
/*  72 */     InjectionPoint injectionPoint = ex.getInjectionPoint();
/*  73 */     if (injectionPoint != null) {
/*  74 */       if (injectionPoint.getField() != null) {
/*  75 */         return String.format("Field %s in %s", new Object[] {injectionPoint
/*  76 */           .getField().getName(), injectionPoint
/*  77 */           .getField().getDeclaringClass().getName() });
/*     */       }
/*  79 */       if (injectionPoint.getMethodParameter() != null) {
/*  80 */         if (injectionPoint.getMethodParameter().getConstructor() != null) {
/*  81 */           return String.format("Parameter %d of constructor in %s", new Object[] {
/*  82 */             Integer.valueOf(injectionPoint.getMethodParameter().getParameterIndex()), injectionPoint
/*  83 */             .getMethodParameter().getDeclaringClass()
/*  84 */             .getName() });
/*     */         }
/*  86 */         return String.format("Parameter %d of method %s in %s", new Object[] {
/*  87 */           Integer.valueOf(injectionPoint.getMethodParameter().getParameterIndex()), injectionPoint
/*  88 */           .getMethodParameter().getMethod().getName(), injectionPoint
/*  89 */           .getMethodParameter().getDeclaringClass()
/*  90 */           .getName() });
/*     */       }
/*     */     }
/*  93 */     return ex.getResourceDescription();
/*     */   }
/*     */   
/*     */   private String getDescription(BeanInstantiationException ex) {
/*  97 */     if (ex.getConstructingMethod() != null) {
/*  98 */       return String.format("Method %s in %s", new Object[] { ex.getConstructingMethod().getName(), ex
/*  99 */         .getConstructingMethod().getDeclaringClass().getName() });
/*     */     }
/* 101 */     if (ex.getConstructor() != null) {
/* 102 */       return String.format("Constructor in %s", new Object[] {
/* 103 */         ClassUtils.getUserClass(ex.getConstructor().getDeclaringClass()).getName() });
/*     */     }
/* 105 */     return ex.getBeanClass().getName();
/*     */   }
/*     */   
/*     */   protected abstract FailureAnalysis analyze(Throwable paramThrowable, T paramT, String paramString);
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\diagnostics\analyzer\AbstractInjectionFailureAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */