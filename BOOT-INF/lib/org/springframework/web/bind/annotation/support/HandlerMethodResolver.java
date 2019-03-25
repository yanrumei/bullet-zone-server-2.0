/*     */ package org.springframework.web.bind.annotation.support;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.ReflectionUtils.MethodCallback;
/*     */ import org.springframework.web.bind.annotation.InitBinder;
/*     */ import org.springframework.web.bind.annotation.ModelAttribute;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.SessionAttributes;
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
/*     */ @Deprecated
/*     */ public class HandlerMethodResolver
/*     */ {
/*     */   private final Set<Method> handlerMethods;
/*     */   private final Set<Method> initBinderMethods;
/*     */   private final Set<Method> modelAttributeMethods;
/*     */   private RequestMapping typeLevelMapping;
/*     */   private boolean sessionAttributesFound;
/*     */   private final Set<String> sessionAttributeNames;
/*     */   private final Set<Class<?>> sessionAttributeTypes;
/*     */   
/*     */   public HandlerMethodResolver()
/*     */   {
/*  56 */     this.handlerMethods = new LinkedHashSet();
/*     */     
/*  58 */     this.initBinderMethods = new LinkedHashSet();
/*     */     
/*  60 */     this.modelAttributeMethods = new LinkedHashSet();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */     this.sessionAttributeNames = new HashSet();
/*     */     
/*  68 */     this.sessionAttributeTypes = new HashSet();
/*     */   }
/*     */   
/*  71 */   private final Set<String> actualSessionAttributeNames = Collections.newSetFromMap(new ConcurrentHashMap(4));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void init(Class<?> handlerType)
/*     */   {
/*  79 */     Set<Class<?>> handlerTypes = new LinkedHashSet();
/*  80 */     Class<?> specificHandlerType = null;
/*  81 */     if (!Proxy.isProxyClass(handlerType)) {
/*  82 */       handlerTypes.add(handlerType);
/*  83 */       specificHandlerType = handlerType;
/*     */     }
/*  85 */     handlerTypes.addAll(Arrays.asList(handlerType.getInterfaces()));
/*  86 */     for (Class<?> currentHandlerType : handlerTypes) {
/*  87 */       final Class<?> targetClass = specificHandlerType != null ? specificHandlerType : currentHandlerType;
/*  88 */       ReflectionUtils.doWithMethods(currentHandlerType, new ReflectionUtils.MethodCallback()
/*     */       {
/*     */         public void doWith(Method method) {
/*  91 */           Method specificMethod = ClassUtils.getMostSpecificMethod(method, targetClass);
/*  92 */           Method bridgedMethod = BridgeMethodResolver.findBridgedMethod(specificMethod);
/*  93 */           if ((HandlerMethodResolver.this.isHandlerMethod(specificMethod)) && ((bridgedMethod == specificMethod) || 
/*  94 */             (!HandlerMethodResolver.this.isHandlerMethod(bridgedMethod)))) {
/*  95 */             HandlerMethodResolver.this.handlerMethods.add(specificMethod);
/*     */           }
/*  97 */           else if ((HandlerMethodResolver.this.isInitBinderMethod(specificMethod)) && ((bridgedMethod == specificMethod) || 
/*  98 */             (!HandlerMethodResolver.this.isInitBinderMethod(bridgedMethod)))) {
/*  99 */             HandlerMethodResolver.this.initBinderMethods.add(specificMethod);
/*     */           }
/* 101 */           else if ((HandlerMethodResolver.this.isModelAttributeMethod(specificMethod)) && ((bridgedMethod == specificMethod) || 
/* 102 */             (!HandlerMethodResolver.this.isModelAttributeMethod(bridgedMethod))))
/* 103 */             HandlerMethodResolver.this.modelAttributeMethods.add(specificMethod); } }, ReflectionUtils.USER_DECLARED_METHODS);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 108 */     this.typeLevelMapping = ((RequestMapping)AnnotationUtils.findAnnotation(handlerType, RequestMapping.class));
/* 109 */     SessionAttributes sessionAttributes = (SessionAttributes)AnnotationUtils.findAnnotation(handlerType, SessionAttributes.class);
/* 110 */     this.sessionAttributesFound = (sessionAttributes != null);
/* 111 */     if (this.sessionAttributesFound) {
/* 112 */       this.sessionAttributeNames.addAll(Arrays.asList(sessionAttributes.names()));
/* 113 */       this.sessionAttributeTypes.addAll(Arrays.asList(sessionAttributes.types()));
/*     */     }
/*     */   }
/*     */   
/*     */   protected boolean isHandlerMethod(Method method) {
/* 118 */     return AnnotationUtils.findAnnotation(method, RequestMapping.class) != null;
/*     */   }
/*     */   
/*     */   protected boolean isInitBinderMethod(Method method) {
/* 122 */     return AnnotationUtils.findAnnotation(method, InitBinder.class) != null;
/*     */   }
/*     */   
/*     */   protected boolean isModelAttributeMethod(Method method) {
/* 126 */     return AnnotationUtils.findAnnotation(method, ModelAttribute.class) != null;
/*     */   }
/*     */   
/*     */   public final boolean hasHandlerMethods()
/*     */   {
/* 131 */     return !this.handlerMethods.isEmpty();
/*     */   }
/*     */   
/*     */   public final Set<Method> getHandlerMethods() {
/* 135 */     return this.handlerMethods;
/*     */   }
/*     */   
/*     */   public final Set<Method> getInitBinderMethods() {
/* 139 */     return this.initBinderMethods;
/*     */   }
/*     */   
/*     */   public final Set<Method> getModelAttributeMethods() {
/* 143 */     return this.modelAttributeMethods;
/*     */   }
/*     */   
/*     */   public boolean hasTypeLevelMapping() {
/* 147 */     return this.typeLevelMapping != null;
/*     */   }
/*     */   
/*     */   public RequestMapping getTypeLevelMapping() {
/* 151 */     return this.typeLevelMapping;
/*     */   }
/*     */   
/*     */   public boolean hasSessionAttributes() {
/* 155 */     return this.sessionAttributesFound;
/*     */   }
/*     */   
/*     */   public boolean isSessionAttribute(String attrName, Class<?> attrType) {
/* 159 */     if ((this.sessionAttributeNames.contains(attrName)) || (this.sessionAttributeTypes.contains(attrType))) {
/* 160 */       this.actualSessionAttributeNames.add(attrName);
/* 161 */       return true;
/*     */     }
/*     */     
/* 164 */     return false;
/*     */   }
/*     */   
/*     */   public Set<String> getActualSessionAttributeNames()
/*     */   {
/* 169 */     return this.actualSessionAttributeNames;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\annotation\support\HandlerMethodResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */