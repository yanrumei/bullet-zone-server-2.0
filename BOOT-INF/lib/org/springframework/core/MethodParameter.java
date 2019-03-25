/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodParameter
/*     */ {
/*     */   private static final Class<?> javaUtilOptionalClass;
/*     */   private final Method method;
/*     */   private final Constructor<?> constructor;
/*     */   private final int parameterIndex;
/*     */   
/*     */   static
/*     */   {
/*     */     Class<?> clazz;
/*     */     try
/*     */     {
/*  55 */       clazz = ClassUtils.forName("java.util.Optional", MethodParameter.class.getClassLoader());
/*     */     }
/*     */     catch (ClassNotFoundException ex) {
/*     */       Class<?> clazz;
/*  59 */       clazz = null;
/*     */     }
/*  61 */     javaUtilOptionalClass = clazz;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   private int nestingLevel = 1;
/*     */   
/*     */ 
/*     */   Map<Integer, Integer> typeIndexesPerLevel;
/*     */   
/*     */ 
/*     */   private volatile Class<?> containingClass;
/*     */   
/*     */ 
/*     */   private volatile Class<?> parameterType;
/*     */   
/*     */ 
/*     */   private volatile Type genericParameterType;
/*     */   
/*     */ 
/*     */   private volatile Annotation[] parameterAnnotations;
/*     */   
/*     */ 
/*     */   private volatile ParameterNameDiscoverer parameterNameDiscoverer;
/*     */   
/*     */ 
/*     */   private volatile String parameterName;
/*     */   
/*     */   private volatile MethodParameter nestedMethodParameter;
/*     */   
/*     */ 
/*     */   public MethodParameter(Method method, int parameterIndex)
/*     */   {
/*  99 */     this(method, parameterIndex, 1);
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
/*     */   public MethodParameter(Method method, int parameterIndex, int nestingLevel)
/*     */   {
/* 113 */     Assert.notNull(method, "Method must not be null");
/* 114 */     this.method = method;
/* 115 */     this.parameterIndex = parameterIndex;
/* 116 */     this.nestingLevel = nestingLevel;
/* 117 */     this.constructor = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MethodParameter(Constructor<?> constructor, int parameterIndex)
/*     */   {
/* 126 */     this(constructor, parameterIndex, 1);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MethodParameter(Constructor<?> constructor, int parameterIndex, int nestingLevel)
/*     */   {
/* 138 */     Assert.notNull(constructor, "Constructor must not be null");
/* 139 */     this.constructor = constructor;
/* 140 */     this.parameterIndex = parameterIndex;
/* 141 */     this.nestingLevel = nestingLevel;
/* 142 */     this.method = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MethodParameter(MethodParameter original)
/*     */   {
/* 151 */     Assert.notNull(original, "Original must not be null");
/* 152 */     this.method = original.method;
/* 153 */     this.constructor = original.constructor;
/* 154 */     this.parameterIndex = original.parameterIndex;
/* 155 */     this.nestingLevel = original.nestingLevel;
/* 156 */     this.typeIndexesPerLevel = original.typeIndexesPerLevel;
/* 157 */     this.containingClass = original.containingClass;
/* 158 */     this.parameterType = original.parameterType;
/* 159 */     this.genericParameterType = original.genericParameterType;
/* 160 */     this.parameterAnnotations = original.parameterAnnotations;
/* 161 */     this.parameterNameDiscoverer = original.parameterNameDiscoverer;
/* 162 */     this.parameterName = original.parameterName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Method getMethod()
/*     */   {
/* 172 */     return this.method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Constructor<?> getConstructor()
/*     */   {
/* 181 */     return this.constructor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Class<?> getDeclaringClass()
/*     */   {
/* 188 */     return getMember().getDeclaringClass();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Member getMember()
/*     */   {
/* 199 */     if (this.method != null) {
/* 200 */       return this.method;
/*     */     }
/*     */     
/* 203 */     return this.constructor;
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
/*     */   public AnnotatedElement getAnnotatedElement()
/*     */   {
/* 217 */     if (this.method != null) {
/* 218 */       return this.method;
/*     */     }
/*     */     
/* 221 */     return this.constructor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getParameterIndex()
/*     */   {
/* 230 */     return this.parameterIndex;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void increaseNestingLevel()
/*     */   {
/* 238 */     this.nestingLevel += 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void decreaseNestingLevel()
/*     */   {
/* 246 */     getTypeIndexesPerLevel().remove(Integer.valueOf(this.nestingLevel));
/* 247 */     this.nestingLevel -= 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getNestingLevel()
/*     */   {
/* 256 */     return this.nestingLevel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTypeIndexForCurrentLevel(int typeIndex)
/*     */   {
/* 266 */     getTypeIndexesPerLevel().put(Integer.valueOf(this.nestingLevel), Integer.valueOf(typeIndex));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getTypeIndexForCurrentLevel()
/*     */   {
/* 276 */     return getTypeIndexForLevel(this.nestingLevel);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Integer getTypeIndexForLevel(int nestingLevel)
/*     */   {
/* 286 */     return (Integer)getTypeIndexesPerLevel().get(Integer.valueOf(nestingLevel));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Map<Integer, Integer> getTypeIndexesPerLevel()
/*     */   {
/* 293 */     if (this.typeIndexesPerLevel == null) {
/* 294 */       this.typeIndexesPerLevel = new HashMap(4);
/*     */     }
/* 296 */     return this.typeIndexesPerLevel;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MethodParameter nested()
/*     */   {
/* 307 */     if (this.nestedMethodParameter != null) {
/* 308 */       return this.nestedMethodParameter;
/*     */     }
/* 310 */     MethodParameter nestedParam = clone();
/* 311 */     this.nestingLevel += 1;
/* 312 */     this.nestedMethodParameter = nestedParam;
/* 313 */     return nestedParam;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isOptional()
/*     */   {
/* 322 */     return getParameterType() == javaUtilOptionalClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MethodParameter nestedIfOptional()
/*     */   {
/* 334 */     return isOptional() ? nested() : this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void setContainingClass(Class<?> containingClass)
/*     */   {
/* 342 */     this.containingClass = containingClass;
/*     */   }
/*     */   
/*     */   public Class<?> getContainingClass() {
/* 346 */     return this.containingClass != null ? this.containingClass : getDeclaringClass();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   void setParameterType(Class<?> parameterType)
/*     */   {
/* 353 */     this.parameterType = parameterType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getParameterType()
/*     */   {
/* 361 */     Class<?> paramType = this.parameterType;
/* 362 */     if (paramType == null) {
/* 363 */       if (this.parameterIndex < 0) {
/* 364 */         Method method = getMethod();
/* 365 */         paramType = method != null ? method.getReturnType() : Void.TYPE;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 370 */         paramType = this.method != null ? this.method.getParameterTypes()[this.parameterIndex] : this.constructor.getParameterTypes()[this.parameterIndex];
/*     */       }
/* 372 */       this.parameterType = paramType;
/*     */     }
/* 374 */     return paramType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Type getGenericParameterType()
/*     */   {
/* 383 */     Type paramType = this.genericParameterType;
/* 384 */     if (paramType == null) {
/* 385 */       if (this.parameterIndex < 0) {
/* 386 */         Method method = getMethod();
/* 387 */         paramType = method != null ? method.getGenericReturnType() : Void.TYPE;
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 392 */         paramType = this.method != null ? this.method.getGenericParameterTypes()[this.parameterIndex] : this.constructor.getGenericParameterTypes()[this.parameterIndex];
/*     */       }
/* 394 */       this.genericParameterType = paramType;
/*     */     }
/* 396 */     return paramType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getNestedParameterType()
/*     */   {
/* 406 */     if (this.nestingLevel > 1) {
/* 407 */       Type type = getGenericParameterType();
/* 408 */       for (int i = 2; i <= this.nestingLevel; i++) {
/* 409 */         if ((type instanceof ParameterizedType)) {
/* 410 */           Type[] args = ((ParameterizedType)type).getActualTypeArguments();
/* 411 */           Integer index = getTypeIndexForLevel(i);
/* 412 */           type = args[(args.length - 1)];
/*     */         }
/*     */       }
/*     */       
/* 416 */       if ((type instanceof Class)) {
/* 417 */         return (Class)type;
/*     */       }
/* 419 */       if ((type instanceof ParameterizedType)) {
/* 420 */         Type arg = ((ParameterizedType)type).getRawType();
/* 421 */         if ((arg instanceof Class)) {
/* 422 */           return (Class)arg;
/*     */         }
/*     */       }
/* 425 */       return Object.class;
/*     */     }
/*     */     
/* 428 */     return getParameterType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Type getNestedGenericParameterType()
/*     */   {
/* 439 */     if (this.nestingLevel > 1) {
/* 440 */       Type type = getGenericParameterType();
/* 441 */       for (int i = 2; i <= this.nestingLevel; i++) {
/* 442 */         if ((type instanceof ParameterizedType)) {
/* 443 */           Type[] args = ((ParameterizedType)type).getActualTypeArguments();
/* 444 */           Integer index = getTypeIndexForLevel(i);
/* 445 */           type = args[(args.length - 1)];
/*     */         }
/*     */       }
/* 448 */       return type;
/*     */     }
/*     */     
/* 451 */     return getGenericParameterType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Annotation[] getMethodAnnotations()
/*     */   {
/* 459 */     return adaptAnnotationArray(getAnnotatedElement().getAnnotations());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <A extends Annotation> A getMethodAnnotation(Class<A> annotationType)
/*     */   {
/* 468 */     return adaptAnnotation(getAnnotatedElement().getAnnotation(annotationType));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <A extends Annotation> boolean hasMethodAnnotation(Class<A> annotationType)
/*     */   {
/* 478 */     return getAnnotatedElement().isAnnotationPresent(annotationType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Annotation[] getParameterAnnotations()
/*     */   {
/* 485 */     if (this.parameterAnnotations == null)
/*     */     {
/* 487 */       Annotation[][] annotationArray = this.method != null ? this.method.getParameterAnnotations() : this.constructor.getParameterAnnotations();
/* 488 */       if ((this.parameterIndex >= 0) && (this.parameterIndex < annotationArray.length)) {
/* 489 */         this.parameterAnnotations = adaptAnnotationArray(annotationArray[this.parameterIndex]);
/*     */       }
/*     */       else {
/* 492 */         this.parameterAnnotations = new Annotation[0];
/*     */       }
/*     */     }
/* 495 */     return this.parameterAnnotations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasParameterAnnotations()
/*     */   {
/* 504 */     return getParameterAnnotations().length != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <A extends Annotation> A getParameterAnnotation(Class<A> annotationType)
/*     */   {
/* 514 */     Annotation[] anns = getParameterAnnotations();
/* 515 */     for (Annotation ann : anns) {
/* 516 */       if (annotationType.isInstance(ann)) {
/* 517 */         return ann;
/*     */       }
/*     */     }
/* 520 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <A extends Annotation> boolean hasParameterAnnotation(Class<A> annotationType)
/*     */   {
/* 529 */     return getParameterAnnotation(annotationType) != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initParameterNameDiscovery(ParameterNameDiscoverer parameterNameDiscoverer)
/*     */   {
/* 539 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getParameterName()
/*     */   {
/* 550 */     ParameterNameDiscoverer discoverer = this.parameterNameDiscoverer;
/* 551 */     if (discoverer != null)
/*     */     {
/* 553 */       String[] parameterNames = this.method != null ? discoverer.getParameterNames(this.method) : discoverer.getParameterNames(this.constructor);
/* 554 */       if (parameterNames != null) {
/* 555 */         this.parameterName = parameterNames[this.parameterIndex];
/*     */       }
/* 557 */       this.parameterNameDiscoverer = null;
/*     */     }
/* 559 */     return this.parameterName;
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
/*     */   protected <A extends Annotation> A adaptAnnotation(A annotation)
/*     */   {
/* 572 */     return annotation;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Annotation[] adaptAnnotationArray(Annotation[] annotations)
/*     */   {
/* 584 */     return annotations;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 590 */     if (this == other) {
/* 591 */       return true;
/*     */     }
/* 593 */     if (!(other instanceof MethodParameter)) {
/* 594 */       return false;
/*     */     }
/* 596 */     MethodParameter otherParam = (MethodParameter)other;
/* 597 */     return (this.parameterIndex == otherParam.parameterIndex) && (getMember().equals(otherParam.getMember()));
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 602 */     return getMember().hashCode() * 31 + this.parameterIndex;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 607 */     return (this.method != null ? "method '" + this.method.getName() + "'" : "constructor") + " parameter " + this.parameterIndex;
/*     */   }
/*     */   
/*     */ 
/*     */   public MethodParameter clone()
/*     */   {
/* 613 */     return new MethodParameter(this);
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
/*     */   public static MethodParameter forMethodOrConstructor(Object methodOrConstructor, int parameterIndex)
/*     */   {
/* 626 */     if ((methodOrConstructor instanceof Method)) {
/* 627 */       return new MethodParameter((Method)methodOrConstructor, parameterIndex);
/*     */     }
/* 629 */     if ((methodOrConstructor instanceof Constructor)) {
/* 630 */       return new MethodParameter((Constructor)methodOrConstructor, parameterIndex);
/*     */     }
/*     */     
/* 633 */     throw new IllegalArgumentException("Given object [" + methodOrConstructor + "] is neither a Method nor a Constructor");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\MethodParameter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */