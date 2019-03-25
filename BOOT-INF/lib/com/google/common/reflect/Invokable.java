/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableList.Builder;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.util.Arrays;
/*     */ import javax.annotation.Nullable;
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
/*     */ @Beta
/*     */ public abstract class Invokable<T, R>
/*     */   extends Element
/*     */   implements GenericDeclaration
/*     */ {
/*     */   <M extends AccessibleObject,  extends Member> Invokable(M member)
/*     */   {
/*  60 */     super(member);
/*     */   }
/*     */   
/*     */   public static Invokable<?, Object> from(Method method)
/*     */   {
/*  65 */     return new MethodInvokable(method);
/*     */   }
/*     */   
/*     */   public static <T> Invokable<T, T> from(Constructor<T> constructor)
/*     */   {
/*  70 */     return new ConstructorInvokable(constructor);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean isOverridable();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean isVarArgs();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public final R invoke(@Nullable T receiver, Object... args)
/*     */     throws InvocationTargetException, IllegalAccessException
/*     */   {
/* 100 */     return (R)invokeInternal(receiver, (Object[])Preconditions.checkNotNull(args));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final TypeToken<? extends R> getReturnType()
/*     */   {
/* 107 */     return TypeToken.of(getGenericReturnType());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ImmutableList<Parameter> getParameters()
/*     */   {
/* 116 */     Type[] parameterTypes = getGenericParameterTypes();
/* 117 */     Annotation[][] annotations = getParameterAnnotations();
/* 118 */     ImmutableList.Builder<Parameter> builder = ImmutableList.builder();
/* 119 */     for (int i = 0; i < parameterTypes.length; i++) {
/* 120 */       builder.add(new Parameter(this, i, TypeToken.of(parameterTypes[i]), annotations[i]));
/*     */     }
/* 122 */     return builder.build();
/*     */   }
/*     */   
/*     */   public final ImmutableList<TypeToken<? extends Throwable>> getExceptionTypes()
/*     */   {
/* 127 */     ImmutableList.Builder<TypeToken<? extends Throwable>> builder = ImmutableList.builder();
/* 128 */     for (Type type : getGenericExceptionTypes())
/*     */     {
/*     */ 
/*     */ 
/* 132 */       TypeToken<? extends Throwable> exceptionType = TypeToken.of(type);
/* 133 */       builder.add(exceptionType);
/*     */     }
/* 135 */     return builder.build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final <R1 extends R> Invokable<T, R1> returning(Class<R1> returnType)
/*     */   {
/* 145 */     return returning(TypeToken.of(returnType));
/*     */   }
/*     */   
/*     */   public final <R1 extends R> Invokable<T, R1> returning(TypeToken<R1> returnType)
/*     */   {
/* 150 */     if (!returnType.isSupertypeOf(getReturnType()))
/*     */     {
/* 152 */       throw new IllegalArgumentException("Invokable is known to return " + getReturnType() + ", not " + returnType);
/*     */     }
/*     */     
/* 155 */     Invokable<T, R1> specialized = this;
/* 156 */     return specialized;
/*     */   }
/*     */   
/*     */ 
/*     */   public final Class<? super T> getDeclaringClass()
/*     */   {
/* 162 */     return super.getDeclaringClass();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeToken<T> getOwnerType()
/*     */   {
/* 170 */     return TypeToken.of(getDeclaringClass());
/*     */   }
/*     */   
/*     */   abstract Object invokeInternal(@Nullable Object paramObject, Object[] paramArrayOfObject) throws InvocationTargetException, IllegalAccessException;
/*     */   
/*     */   abstract Type[] getGenericParameterTypes();
/*     */   
/*     */   abstract Type[] getGenericExceptionTypes();
/*     */   
/*     */   abstract Annotation[][] getParameterAnnotations();
/*     */   
/*     */   abstract Type getGenericReturnType();
/*     */   
/*     */   static class MethodInvokable<T>
/*     */     extends Invokable<T, Object>
/*     */   {
/*     */     final Method method;
/*     */     
/*     */     MethodInvokable(Method method)
/*     */     {
/* 190 */       super();
/* 191 */       this.method = method;
/*     */     }
/*     */     
/*     */     final Object invokeInternal(@Nullable Object receiver, Object[] args)
/*     */       throws InvocationTargetException, IllegalAccessException
/*     */     {
/* 197 */       return this.method.invoke(receiver, args);
/*     */     }
/*     */     
/*     */     Type getGenericReturnType()
/*     */     {
/* 202 */       return this.method.getGenericReturnType();
/*     */     }
/*     */     
/*     */     Type[] getGenericParameterTypes()
/*     */     {
/* 207 */       return this.method.getGenericParameterTypes();
/*     */     }
/*     */     
/*     */     Type[] getGenericExceptionTypes()
/*     */     {
/* 212 */       return this.method.getGenericExceptionTypes();
/*     */     }
/*     */     
/*     */     final Annotation[][] getParameterAnnotations()
/*     */     {
/* 217 */       return this.method.getParameterAnnotations();
/*     */     }
/*     */     
/*     */     public final TypeVariable<?>[] getTypeParameters()
/*     */     {
/* 222 */       return this.method.getTypeParameters();
/*     */     }
/*     */     
/*     */     public final boolean isOverridable()
/*     */     {
/* 227 */       return (!isFinal()) && 
/* 228 */         (!isPrivate()) && 
/* 229 */         (!isStatic()) && 
/* 230 */         (!Modifier.isFinal(getDeclaringClass().getModifiers()));
/*     */     }
/*     */     
/*     */     public final boolean isVarArgs()
/*     */     {
/* 235 */       return this.method.isVarArgs();
/*     */     }
/*     */   }
/*     */   
/*     */   static class ConstructorInvokable<T> extends Invokable<T, T>
/*     */   {
/*     */     final Constructor<?> constructor;
/*     */     
/*     */     ConstructorInvokable(Constructor<?> constructor) {
/* 244 */       super();
/* 245 */       this.constructor = constructor;
/*     */     }
/*     */     
/*     */     final Object invokeInternal(@Nullable Object receiver, Object[] args) throws InvocationTargetException, IllegalAccessException
/*     */     {
/*     */       try
/*     */       {
/* 252 */         return this.constructor.newInstance(args);
/*     */       } catch (InstantiationException e) {
/* 254 */         throw new RuntimeException(this.constructor + " failed.", e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     Type getGenericReturnType()
/*     */     {
/* 264 */       Class<?> declaringClass = getDeclaringClass();
/* 265 */       TypeVariable<?>[] typeParams = declaringClass.getTypeParameters();
/* 266 */       if (typeParams.length > 0) {
/* 267 */         return Types.newParameterizedType(declaringClass, typeParams);
/*     */       }
/* 269 */       return declaringClass;
/*     */     }
/*     */     
/*     */ 
/*     */     Type[] getGenericParameterTypes()
/*     */     {
/* 275 */       Type[] types = this.constructor.getGenericParameterTypes();
/* 276 */       if ((types.length > 0) && (mayNeedHiddenThis())) {
/* 277 */         Class<?>[] rawParamTypes = this.constructor.getParameterTypes();
/* 278 */         if ((types.length == rawParamTypes.length) && 
/* 279 */           (rawParamTypes[0] == getDeclaringClass().getEnclosingClass()))
/*     */         {
/* 281 */           return (Type[])Arrays.copyOfRange(types, 1, types.length);
/*     */         }
/*     */       }
/* 284 */       return types;
/*     */     }
/*     */     
/*     */     Type[] getGenericExceptionTypes()
/*     */     {
/* 289 */       return this.constructor.getGenericExceptionTypes();
/*     */     }
/*     */     
/*     */     final Annotation[][] getParameterAnnotations()
/*     */     {
/* 294 */       return this.constructor.getParameterAnnotations();
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
/*     */     public final TypeVariable<?>[] getTypeParameters()
/*     */     {
/* 308 */       TypeVariable<?>[] declaredByClass = getDeclaringClass().getTypeParameters();
/* 309 */       TypeVariable<?>[] declaredByConstructor = this.constructor.getTypeParameters();
/* 310 */       TypeVariable<?>[] result = new TypeVariable[declaredByClass.length + declaredByConstructor.length];
/*     */       
/* 312 */       System.arraycopy(declaredByClass, 0, result, 0, declaredByClass.length);
/* 313 */       System.arraycopy(declaredByConstructor, 0, result, declaredByClass.length, declaredByConstructor.length);
/*     */       
/*     */ 
/*     */ 
/* 317 */       return result;
/*     */     }
/*     */     
/*     */     public final boolean isOverridable()
/*     */     {
/* 322 */       return false;
/*     */     }
/*     */     
/*     */     public final boolean isVarArgs()
/*     */     {
/* 327 */       return this.constructor.isVarArgs();
/*     */     }
/*     */     
/*     */     private boolean mayNeedHiddenThis() {
/* 331 */       Class<?> declaringClass = this.constructor.getDeclaringClass();
/* 332 */       if (declaringClass.getEnclosingConstructor() != null)
/*     */       {
/* 334 */         return true;
/*     */       }
/* 336 */       Method enclosingMethod = declaringClass.getEnclosingMethod();
/* 337 */       if (enclosingMethod != null)
/*     */       {
/* 339 */         return !Modifier.isStatic(enclosingMethod.getModifiers());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 347 */       return (declaringClass.getEnclosingClass() != null) && 
/* 348 */         (!Modifier.isStatic(declaringClass.getModifiers()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\reflect\Invokable.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */