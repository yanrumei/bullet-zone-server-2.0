/*     */ package org.springframework.beans.factory.config;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.InjectionPoint;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.core.GenericCollectionTypeResolver;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.ResolvableType;
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
/*     */ public class DependencyDescriptor
/*     */   extends InjectionPoint
/*     */   implements Serializable
/*     */ {
/*     */   private final Class<?> declaringClass;
/*     */   private String methodName;
/*     */   private Class<?>[] parameterTypes;
/*     */   private int parameterIndex;
/*     */   private String fieldName;
/*     */   private final boolean required;
/*     */   private final boolean eager;
/*  61 */   private int nestingLevel = 1;
/*     */   
/*     */ 
/*     */ 
/*     */   private Class<?> containingClass;
/*     */   
/*     */ 
/*     */ 
/*     */   private volatile ResolvableType resolvableType;
/*     */   
/*     */ 
/*     */ 
/*     */   public DependencyDescriptor(MethodParameter methodParameter, boolean required)
/*     */   {
/*  75 */     this(methodParameter, required, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DependencyDescriptor(MethodParameter methodParameter, boolean required, boolean eager)
/*     */   {
/*  86 */     super(methodParameter);
/*  87 */     this.declaringClass = methodParameter.getDeclaringClass();
/*  88 */     if (this.methodParameter.getMethod() != null) {
/*  89 */       this.methodName = methodParameter.getMethod().getName();
/*  90 */       this.parameterTypes = methodParameter.getMethod().getParameterTypes();
/*     */     }
/*     */     else {
/*  93 */       this.parameterTypes = methodParameter.getConstructor().getParameterTypes();
/*     */     }
/*  95 */     this.parameterIndex = methodParameter.getParameterIndex();
/*  96 */     this.containingClass = methodParameter.getContainingClass();
/*  97 */     this.required = required;
/*  98 */     this.eager = eager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DependencyDescriptor(Field field, boolean required)
/*     */   {
/* 108 */     this(field, required, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DependencyDescriptor(Field field, boolean required, boolean eager)
/*     */   {
/* 119 */     super(field);
/* 120 */     this.declaringClass = field.getDeclaringClass();
/* 121 */     this.fieldName = field.getName();
/* 122 */     this.required = required;
/* 123 */     this.eager = eager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DependencyDescriptor(DependencyDescriptor original)
/*     */   {
/* 131 */     super(original);
/* 132 */     this.declaringClass = original.declaringClass;
/* 133 */     this.methodName = original.methodName;
/* 134 */     this.parameterTypes = original.parameterTypes;
/* 135 */     this.parameterIndex = original.parameterIndex;
/* 136 */     this.fieldName = original.fieldName;
/* 137 */     this.containingClass = original.containingClass;
/* 138 */     this.required = original.required;
/* 139 */     this.eager = original.eager;
/* 140 */     this.nestingLevel = original.nestingLevel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isRequired()
/*     */   {
/* 148 */     return this.required;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEager()
/*     */   {
/* 156 */     return this.eager;
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
/*     */   public Object resolveNotUnique(Class<?> type, Map<String, Object> matchingBeans)
/*     */     throws BeansException
/*     */   {
/* 173 */     throw new NoUniqueBeanDefinitionException(type, matchingBeans.keySet());
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
/*     */   public Object resolveShortcut(BeanFactory beanFactory)
/*     */     throws BeansException
/*     */   {
/* 189 */     return null;
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
/*     */ 
/*     */   public Object resolveCandidate(String beanName, Class<?> requiredType, BeanFactory beanFactory)
/*     */     throws BeansException
/*     */   {
/* 208 */     return beanFactory.getBean(beanName, requiredType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void increaseNestingLevel()
/*     */   {
/* 217 */     this.nestingLevel += 1;
/* 218 */     this.resolvableType = null;
/* 219 */     if (this.methodParameter != null) {
/* 220 */       this.methodParameter.increaseNestingLevel();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContainingClass(Class<?> containingClass)
/*     */   {
/* 231 */     this.containingClass = containingClass;
/* 232 */     this.resolvableType = null;
/* 233 */     if (this.methodParameter != null) {
/* 234 */       GenericTypeResolver.resolveParameterType(this.methodParameter, containingClass);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResolvableType getResolvableType()
/*     */   {
/* 243 */     ResolvableType resolvableType = this.resolvableType;
/* 244 */     if (resolvableType == null)
/*     */     {
/*     */ 
/* 247 */       resolvableType = this.field != null ? ResolvableType.forField(this.field, this.nestingLevel, this.containingClass) : ResolvableType.forMethodParameter(this.methodParameter);
/* 248 */       this.resolvableType = resolvableType;
/*     */     }
/* 250 */     return resolvableType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean fallbackMatchAllowed()
/*     */   {
/* 261 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DependencyDescriptor forFallbackMatch()
/*     */   {
/* 270 */     new DependencyDescriptor(this)
/*     */     {
/*     */       public boolean fallbackMatchAllowed() {
/* 273 */         return true;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initParameterNameDiscovery(ParameterNameDiscoverer parameterNameDiscoverer)
/*     */   {
/* 285 */     if (this.methodParameter != null) {
/* 286 */       this.methodParameter.initParameterNameDiscovery(parameterNameDiscoverer);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDependencyName()
/*     */   {
/* 295 */     return this.field != null ? this.field.getName() : this.methodParameter.getParameterName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getDependencyType()
/*     */   {
/* 303 */     if (this.field != null) {
/* 304 */       if (this.nestingLevel > 1) {
/* 305 */         Type type = this.field.getGenericType();
/* 306 */         for (int i = 2; i <= this.nestingLevel; i++) {
/* 307 */           if ((type instanceof ParameterizedType)) {
/* 308 */             Type[] args = ((ParameterizedType)type).getActualTypeArguments();
/* 309 */             type = args[(args.length - 1)];
/*     */           }
/*     */         }
/*     */         
/* 313 */         if ((type instanceof Class)) {
/* 314 */           return (Class)type;
/*     */         }
/* 316 */         if ((type instanceof ParameterizedType)) {
/* 317 */           Type arg = ((ParameterizedType)type).getRawType();
/* 318 */           if ((arg instanceof Class)) {
/* 319 */             return (Class)arg;
/*     */           }
/*     */         }
/* 322 */         return Object.class;
/*     */       }
/*     */       
/* 325 */       return this.field.getType();
/*     */     }
/*     */     
/*     */ 
/* 329 */     return this.methodParameter.getNestedParameterType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Class<?> getCollectionType()
/*     */   {
/* 340 */     return this.field != null ? 
/* 341 */       GenericCollectionTypeResolver.getCollectionFieldType(this.field, this.nestingLevel) : 
/* 342 */       GenericCollectionTypeResolver.getCollectionParameterType(this.methodParameter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Class<?> getMapKeyType()
/*     */   {
/* 352 */     return this.field != null ? 
/* 353 */       GenericCollectionTypeResolver.getMapKeyFieldType(this.field, this.nestingLevel) : 
/* 354 */       GenericCollectionTypeResolver.getMapKeyParameterType(this.methodParameter);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Class<?> getMapValueType()
/*     */   {
/* 364 */     return this.field != null ? 
/* 365 */       GenericCollectionTypeResolver.getMapValueFieldType(this.field, this.nestingLevel) : 
/* 366 */       GenericCollectionTypeResolver.getMapValueParameterType(this.methodParameter);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 372 */     if (this == other) {
/* 373 */       return true;
/*     */     }
/* 375 */     if (!super.equals(other)) {
/* 376 */       return false;
/*     */     }
/* 378 */     DependencyDescriptor otherDesc = (DependencyDescriptor)other;
/* 379 */     return (this.required == otherDesc.required) && (this.eager == otherDesc.eager) && (this.nestingLevel == otherDesc.nestingLevel) && (this.containingClass == otherDesc.containingClass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readObject(ObjectInputStream ois)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 390 */     ois.defaultReadObject();
/*     */     
/*     */     try
/*     */     {
/* 394 */       if (this.fieldName != null) {
/* 395 */         this.field = this.declaringClass.getDeclaredField(this.fieldName);
/*     */       }
/*     */       else {
/* 398 */         if (this.methodName != null)
/*     */         {
/* 400 */           this.methodParameter = new MethodParameter(this.declaringClass.getDeclaredMethod(this.methodName, this.parameterTypes), this.parameterIndex);
/*     */         }
/*     */         else
/*     */         {
/* 404 */           this.methodParameter = new MethodParameter(this.declaringClass.getDeclaredConstructor(this.parameterTypes), this.parameterIndex);
/*     */         }
/* 406 */         for (int i = 1; i < this.nestingLevel; i++) {
/* 407 */           this.methodParameter.increaseNestingLevel();
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Throwable ex) {
/* 412 */       throw new IllegalStateException("Could not find original class structure", ex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\config\DependencyDescriptor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */