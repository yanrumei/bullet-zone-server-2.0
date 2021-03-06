/*     */ package org.springframework.web.method.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.core.ExceptionDepthComparator;
/*     */ import org.springframework.core.MethodIntrospector;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils.MethodFilter;
/*     */ import org.springframework.web.bind.annotation.ExceptionHandler;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExceptionHandlerMethodResolver
/*     */ {
/*  48 */   public static final ReflectionUtils.MethodFilter EXCEPTION_HANDLER_METHODS = new ReflectionUtils.MethodFilter()
/*     */   {
/*     */     public boolean matches(Method method) {
/*  51 */       return AnnotationUtils.findAnnotation(method, ExceptionHandler.class) != null;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  58 */   private static final Method NO_METHOD_FOUND = ClassUtils.getMethodIfAvailable(System.class, "currentTimeMillis", new Class[0]);
/*     */   
/*     */ 
/*  61 */   private final Map<Class<? extends Throwable>, Method> mappedMethods = new ConcurrentHashMap(16);
/*     */   
/*     */ 
/*  64 */   private final Map<Class<? extends Throwable>, Method> exceptionLookupCache = new ConcurrentHashMap(16);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ExceptionHandlerMethodResolver(Class<?> handlerType)
/*     */   {
/*  73 */     for (Iterator localIterator1 = MethodIntrospector.selectMethods(handlerType, EXCEPTION_HANDLER_METHODS).iterator(); localIterator1.hasNext();) { method = (Method)localIterator1.next();
/*  74 */       for (Class<? extends Throwable> exceptionType : detectExceptionMappings(method)) {
/*  75 */         addExceptionMapping(exceptionType, method);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     Method method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private List<Class<? extends Throwable>> detectExceptionMappings(Method method)
/*     */   {
/*  87 */     List<Class<? extends Throwable>> result = new ArrayList();
/*  88 */     detectAnnotationExceptionMappings(method, result);
/*  89 */     if (result.isEmpty()) {
/*  90 */       for (Class<?> paramType : method.getParameterTypes()) {
/*  91 */         if (Throwable.class.isAssignableFrom(paramType)) {
/*  92 */           result.add(paramType);
/*     */         }
/*     */       }
/*     */     }
/*  96 */     if (result.isEmpty()) {
/*  97 */       throw new IllegalStateException("No exception types mapped to " + method);
/*     */     }
/*  99 */     return result;
/*     */   }
/*     */   
/*     */   protected void detectAnnotationExceptionMappings(Method method, List<Class<? extends Throwable>> result) {
/* 103 */     ExceptionHandler ann = (ExceptionHandler)AnnotationUtils.findAnnotation(method, ExceptionHandler.class);
/* 104 */     result.addAll(Arrays.asList(ann.value()));
/*     */   }
/*     */   
/*     */   private void addExceptionMapping(Class<? extends Throwable> exceptionType, Method method) {
/* 108 */     Method oldMethod = (Method)this.mappedMethods.put(exceptionType, method);
/* 109 */     if ((oldMethod != null) && (!oldMethod.equals(method))) {
/* 110 */       throw new IllegalStateException("Ambiguous @ExceptionHandler method mapped for [" + exceptionType + "]: {" + oldMethod + ", " + method + "}");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasExceptionMappings()
/*     */   {
/* 119 */     return !this.mappedMethods.isEmpty();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Method resolveMethod(Exception exception)
/*     */   {
/* 129 */     Method method = resolveMethodByExceptionType(exception.getClass());
/* 130 */     if (method == null) {
/* 131 */       Throwable cause = exception.getCause();
/* 132 */       if (cause != null) {
/* 133 */         method = resolveMethodByExceptionType(cause.getClass());
/*     */       }
/*     */     }
/* 136 */     return method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Method resolveMethodByExceptionType(Class<? extends Throwable> exceptionType)
/*     */   {
/* 146 */     Method method = (Method)this.exceptionLookupCache.get(exceptionType);
/* 147 */     if (method == null) {
/* 148 */       method = getMappedMethod(exceptionType);
/* 149 */       this.exceptionLookupCache.put(exceptionType, method != null ? method : NO_METHOD_FOUND);
/*     */     }
/* 151 */     return method != NO_METHOD_FOUND ? method : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Method getMappedMethod(Class<? extends Throwable> exceptionType)
/*     */   {
/* 158 */     List<Class<? extends Throwable>> matches = new ArrayList();
/* 159 */     for (Class<? extends Throwable> mappedException : this.mappedMethods.keySet()) {
/* 160 */       if (mappedException.isAssignableFrom(exceptionType)) {
/* 161 */         matches.add(mappedException);
/*     */       }
/*     */     }
/* 164 */     if (!matches.isEmpty()) {
/* 165 */       Collections.sort(matches, new ExceptionDepthComparator(exceptionType));
/* 166 */       return (Method)this.mappedMethods.get(matches.get(0));
/*     */     }
/*     */     
/* 169 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\method\annotation\ExceptionHandlerMethodResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */