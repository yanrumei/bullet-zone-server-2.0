/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.ReflectionUtils.MethodCallback;
/*     */ import org.springframework.util.ReflectionUtils.MethodFilter;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MethodIntrospector
/*     */ {
/*     */   public static <T> Map<Method, T> selectMethods(Class<?> targetType, final MetadataLookup<T> metadataLookup)
/*     */   {
/*  55 */     final Map<Method, T> methodMap = new LinkedHashMap();
/*  56 */     Set<Class<?>> handlerTypes = new LinkedHashSet();
/*  57 */     Class<?> specificHandlerType = null;
/*     */     
/*  59 */     if (!Proxy.isProxyClass(targetType)) {
/*  60 */       handlerTypes.add(targetType);
/*  61 */       specificHandlerType = targetType;
/*     */     }
/*  63 */     handlerTypes.addAll(Arrays.asList(targetType.getInterfaces()));
/*     */     
/*  65 */     for (Class<?> currentHandlerType : handlerTypes) {
/*  66 */       Class<?> targetClass = specificHandlerType != null ? specificHandlerType : currentHandlerType;
/*     */       
/*  68 */       ReflectionUtils.doWithMethods(currentHandlerType, new ReflectionUtils.MethodCallback()
/*     */       {
/*     */         public void doWith(Method method) {
/*  71 */           Method specificMethod = ClassUtils.getMostSpecificMethod(method, this.val$targetClass);
/*  72 */           T result = metadataLookup.inspect(specificMethod);
/*  73 */           if (result != null) {
/*  74 */             Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
/*  75 */             if ((bridgedMethod == specificMethod) || (metadataLookup.inspect(bridgedMethod) == null))
/*  76 */               methodMap.put(specificMethod, result); } } }, ReflectionUtils.USER_DECLARED_METHODS);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  83 */     return methodMap;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Set<Method> selectMethods(Class<?> targetType, ReflectionUtils.MethodFilter methodFilter)
/*     */   {
/*  95 */     
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 100 */       selectMethods(targetType, new MetadataLookup()
/*     */       {
/*     */         public Boolean inspect(Method method)
/*     */         {
/*  98 */           return this.val$methodFilter.matches(method) ? Boolean.TRUE : null;
/*     */         }
/*     */       }).keySet();
/*     */   }
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
/*     */   public static Method selectInvocableMethod(Method method, Class<?> targetType)
/*     */   {
/* 117 */     if (method.getDeclaringClass().isAssignableFrom(targetType)) {
/* 118 */       return method;
/*     */     }
/*     */     try {
/* 121 */       String methodName = method.getName();
/* 122 */       Class<?>[] parameterTypes = method.getParameterTypes();
/* 123 */       for (Class<?> ifc : targetType.getInterfaces()) {
/*     */         try {
/* 125 */           return ifc.getMethod(methodName, parameterTypes);
/*     */         }
/*     */         catch (NoSuchMethodException localNoSuchMethodException1) {}
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 132 */       return targetType.getMethod(methodName, parameterTypes);
/*     */     }
/*     */     catch (NoSuchMethodException ex) {
/* 135 */       throw new IllegalStateException(String.format("Need to invoke method '%s' declared on target class '%s', but not found in any interface(s) of the exposed proxy type. Either pull the method up to an interface or switch to CGLIB proxies by enforcing proxy-target-class mode in your configuration.", new Object[] {method
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 140 */         .getName(), method.getDeclaringClass().getSimpleName() }));
/*     */     }
/*     */   }
/*     */   
/*     */   public static abstract interface MetadataLookup<T>
/*     */   {
/*     */     public abstract T inspect(Method paramMethod);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\MethodIntrospector.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */