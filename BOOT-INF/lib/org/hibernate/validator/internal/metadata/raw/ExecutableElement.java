/*     */ package org.hibernate.validator.internal.metadata.raw;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.ExecutableHelper;
/*     */ import org.hibernate.validator.internal.util.ReflectionHelper;
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
/*     */ public abstract class ExecutableElement
/*     */ {
/*     */   private String signature;
/*     */   
/*     */   public static ExecutableElement forConstructor(Constructor<?> constructor)
/*     */   {
/*  36 */     return new ConstructorElement(constructor, null);
/*     */   }
/*     */   
/*     */   public static List<ExecutableElement> forConstructors(Constructor<?>[] constructors) {
/*  40 */     List<ExecutableElement> executableElements = CollectionHelper.newArrayList(constructors.length);
/*     */     
/*  42 */     for (Constructor<?> constructor : constructors) {
/*  43 */       executableElements.add(forConstructor(constructor));
/*     */     }
/*     */     
/*  46 */     return executableElements;
/*     */   }
/*     */   
/*     */   public static ExecutableElement forMethod(Method method) {
/*  50 */     return new MethodElement(method);
/*     */   }
/*     */   
/*     */   public static List<ExecutableElement> forMethods(Method[] methods) {
/*  54 */     List<ExecutableElement> executableElements = CollectionHelper.newArrayList(methods.length);
/*     */     
/*  56 */     for (Method method : methods) {
/*  57 */       executableElements.add(forMethod(method));
/*     */     }
/*     */     
/*  60 */     return executableElements;
/*     */   }
/*     */   
/*     */   private ExecutableElement(String name, Class<?>[] parameterTypes) {
/*  64 */     this.signature = ExecutableHelper.getSignature(name, parameterTypes);
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract List<String> getParameterNames(ParameterNameProvider paramParameterNameProvider);
/*     */   
/*     */ 
/*     */   public abstract Annotation[][] getParameterAnnotations();
/*     */   
/*     */ 
/*     */   public abstract Class<?>[] getParameterTypes();
/*     */   
/*     */ 
/*     */   public abstract Class<?> getReturnType();
/*     */   
/*     */ 
/*     */   public abstract Type[] getGenericParameterTypes();
/*     */   
/*     */   public abstract AccessibleObject getAccessibleObject();
/*     */   
/*     */   public abstract Member getMember();
/*     */   
/*     */   public abstract ElementType getElementType();
/*     */   
/*     */   public abstract String getSimpleName();
/*     */   
/*     */   public abstract boolean isGetterMethod();
/*     */   
/*     */   public String getAsString()
/*     */   {
/*  94 */     return getExecutableAsString(getSimpleName(), getParameterTypes());
/*     */   }
/*     */   
/*     */   public String getSignature() {
/*  98 */     return this.signature;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean equals(Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int hashCode();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String getExecutableAsString(String name, Class<?>... parameterTypes)
/*     */   {
/* 117 */     StringBuilder sb = new StringBuilder(name);
/* 118 */     sb.append("(");
/*     */     
/* 120 */     boolean isFirst = true;
/*     */     
/* 122 */     for (Class<?> parameterType : parameterTypes) {
/* 123 */       if (!isFirst) {
/* 124 */         sb.append(", ");
/*     */       }
/*     */       else {
/* 127 */         isFirst = false;
/*     */       }
/*     */       
/* 130 */       sb.append(parameterType.getSimpleName());
/*     */     }
/*     */     
/* 133 */     sb.append(")");
/* 134 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private static class ConstructorElement extends ExecutableElement
/*     */   {
/*     */     private final Constructor<?> constructor;
/*     */     
/*     */     private ConstructorElement(Constructor<?> constructor) {
/* 142 */       super(constructor.getParameterTypes(), null);
/* 143 */       this.constructor = constructor;
/*     */     }
/*     */     
/*     */     public List<String> getParameterNames(ParameterNameProvider parameterNameProvider)
/*     */     {
/* 148 */       return parameterNameProvider.getParameterNames(this.constructor);
/*     */     }
/*     */     
/*     */ 
/*     */     public Annotation[][] getParameterAnnotations()
/*     */     {
/* 154 */       Annotation[][] parameterAnnotations = this.constructor.getParameterAnnotations();
/*     */       
/* 156 */       int parameterCount = this.constructor.getParameterTypes().length;
/*     */       
/* 158 */       if (parameterAnnotations.length == parameterCount) {
/* 159 */         return parameterAnnotations;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 165 */       return (Annotation[][])paddedLeft(parameterAnnotations, new Annotation[parameterCount][], new Annotation[0]);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Class<?>[] getParameterTypes()
/*     */     {
/* 175 */       return this.constructor.getParameterTypes();
/*     */     }
/*     */     
/*     */     public Class<?> getReturnType()
/*     */     {
/* 180 */       return this.constructor.getDeclaringClass();
/*     */     }
/*     */     
/*     */     public Type[] getGenericParameterTypes()
/*     */     {
/* 185 */       return this.constructor.getGenericParameterTypes();
/*     */     }
/*     */     
/*     */     public AccessibleObject getAccessibleObject()
/*     */     {
/* 190 */       return this.constructor;
/*     */     }
/*     */     
/*     */     public Member getMember()
/*     */     {
/* 195 */       return this.constructor;
/*     */     }
/*     */     
/*     */     public ElementType getElementType()
/*     */     {
/* 200 */       return ElementType.CONSTRUCTOR;
/*     */     }
/*     */     
/*     */     public String getSimpleName()
/*     */     {
/* 205 */       return this.constructor.getDeclaringClass().getSimpleName();
/*     */     }
/*     */     
/*     */     public boolean isGetterMethod()
/*     */     {
/* 210 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 216 */       return this.constructor.toGenericString();
/*     */     }
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
/*     */     private <T> T[] paddedLeft(T[] src, T[] dest, T fillElement)
/*     */     {
/* 231 */       int originalCount = src.length;
/* 232 */       int targetCount = dest.length;
/*     */       
/* 234 */       System.arraycopy(src, 0, dest, targetCount - originalCount, originalCount);
/* 235 */       Arrays.fill(dest, 0, targetCount - originalCount, fillElement);
/*     */       
/* 237 */       return dest;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 242 */       int prime = 31;
/* 243 */       int result = 1;
/*     */       
/* 245 */       result = 31 * result + (this.constructor == null ? 0 : this.constructor.hashCode());
/* 246 */       return result;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 251 */       if (this == obj) {
/* 252 */         return true;
/*     */       }
/* 254 */       if (obj == null) {
/* 255 */         return false;
/*     */       }
/* 257 */       if (getClass() != obj.getClass()) {
/* 258 */         return false;
/*     */       }
/* 260 */       ConstructorElement other = (ConstructorElement)obj;
/* 261 */       if (this.constructor == null) {
/* 262 */         if (other.constructor != null) {
/* 263 */           return false;
/*     */         }
/*     */       }
/* 266 */       else if (!this.constructor.equals(other.constructor)) {
/* 267 */         return false;
/*     */       }
/* 269 */       return true;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class MethodElement extends ExecutableElement
/*     */   {
/*     */     private final Method method;
/*     */     private final boolean isGetterMethod;
/*     */     
/*     */     public MethodElement(Method method) {
/* 279 */       super(method.getParameterTypes(), null);
/* 280 */       this.method = method;
/* 281 */       this.isGetterMethod = ReflectionHelper.isGetterMethod(method);
/*     */     }
/*     */     
/*     */     public List<String> getParameterNames(ParameterNameProvider parameterNameProvider)
/*     */     {
/* 286 */       return parameterNameProvider.getParameterNames(this.method);
/*     */     }
/*     */     
/*     */     public Annotation[][] getParameterAnnotations()
/*     */     {
/* 291 */       return this.method.getParameterAnnotations();
/*     */     }
/*     */     
/*     */     public Class<?>[] getParameterTypes()
/*     */     {
/* 296 */       return this.method.getParameterTypes();
/*     */     }
/*     */     
/*     */     public Class<?> getReturnType()
/*     */     {
/* 301 */       return this.method.getReturnType();
/*     */     }
/*     */     
/*     */     public Type[] getGenericParameterTypes()
/*     */     {
/* 306 */       return this.method.getGenericParameterTypes();
/*     */     }
/*     */     
/*     */     public AccessibleObject getAccessibleObject()
/*     */     {
/* 311 */       return this.method;
/*     */     }
/*     */     
/*     */     public Member getMember()
/*     */     {
/* 316 */       return this.method;
/*     */     }
/*     */     
/*     */     public ElementType getElementType()
/*     */     {
/* 321 */       return ElementType.METHOD;
/*     */     }
/*     */     
/*     */     public String getSimpleName()
/*     */     {
/* 326 */       return this.method.getName();
/*     */     }
/*     */     
/*     */     public boolean isGetterMethod()
/*     */     {
/* 331 */       return this.isGetterMethod;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 336 */       return this.method.toGenericString();
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 341 */       int prime = 31;
/* 342 */       int result = 1;
/*     */       
/* 344 */       result = 31 * result + (this.method == null ? 0 : this.method.hashCode());
/* 345 */       return result;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 350 */       if (this == obj) {
/* 351 */         return true;
/*     */       }
/* 353 */       if (obj == null) {
/* 354 */         return false;
/*     */       }
/* 356 */       if (getClass() != obj.getClass()) {
/* 357 */         return false;
/*     */       }
/* 359 */       MethodElement other = (MethodElement)obj;
/* 360 */       if (this.method == null) {
/* 361 */         if (other.method != null) {
/* 362 */           return false;
/*     */         }
/*     */       }
/* 365 */       else if (!this.method.equals(other.method)) {
/* 366 */         return false;
/*     */       }
/* 368 */       return true;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\raw\ExecutableElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */