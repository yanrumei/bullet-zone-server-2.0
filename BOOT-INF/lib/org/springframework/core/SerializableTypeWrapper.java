/*     */ package org.springframework.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ abstract class SerializableTypeWrapper
/*     */ {
/*  59 */   private static final Class<?>[] SUPPORTED_SERIALIZABLE_TYPES = { GenericArrayType.class, ParameterizedType.class, TypeVariable.class, WildcardType.class };
/*     */   
/*     */ 
/*  62 */   static final ConcurrentReferenceHashMap<Type, Type> cache = new ConcurrentReferenceHashMap(256);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Type forField(Field field)
/*     */   {
/*  69 */     Assert.notNull(field, "Field must not be null");
/*  70 */     return forTypeProvider(new FieldTypeProvider(field));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Type forMethodParameter(MethodParameter methodParameter)
/*     */   {
/*  78 */     return forTypeProvider(new MethodParameterTypeProvider(methodParameter));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Type forGenericSuperclass(Class<?> type)
/*     */   {
/*  86 */     forTypeProvider(new SimpleTypeProvider(type)
/*     */     {
/*     */       public Type getType() {
/*  89 */         return this.val$type.getGenericSuperclass();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Type[] forGenericInterfaces(Class<?> type)
/*     */   {
/*  99 */     Type[] result = new Type[type.getGenericInterfaces().length];
/* 100 */     for (int i = 0; i < result.length; i++) {
/* 101 */       final int index = i;
/* 102 */       result[i = forTypeProvider(new SimpleTypeProvider(type)
/*     */       {
/*     */         public Type getType() {
/* 105 */           return this.val$type.getGenericInterfaces()[index];
/*     */         }
/*     */       });
/*     */     }
/* 109 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Type[] forTypeParameters(Class<?> type)
/*     */   {
/* 117 */     Type[] result = new Type[type.getTypeParameters().length];
/* 118 */     for (int i = 0; i < result.length; i++) {
/* 119 */       final int index = i;
/* 120 */       result[i = forTypeProvider(new SimpleTypeProvider(type)
/*     */       {
/*     */         public Type getType() {
/* 123 */           return this.val$type.getTypeParameters()[index];
/*     */         }
/*     */       });
/*     */     }
/* 127 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <T extends Type> T unwrap(T type)
/*     */   {
/* 137 */     Type unwrapped = type;
/* 138 */     while ((unwrapped instanceof SerializableTypeProxy)) {
/* 139 */       unwrapped = ((SerializableTypeProxy)type).getTypeProvider().getType();
/*     */     }
/* 141 */     return unwrapped;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static Type forTypeProvider(TypeProvider provider)
/*     */   {
/* 148 */     Assert.notNull(provider, "Provider must not be null");
/* 149 */     if (((provider.getType() instanceof Serializable)) || (provider.getType() == null)) {
/* 150 */       return provider.getType();
/*     */     }
/* 152 */     Type cached = (Type)cache.get(provider.getType());
/* 153 */     if (cached != null) {
/* 154 */       return cached;
/*     */     }
/* 156 */     for (Class<?> type : SUPPORTED_SERIALIZABLE_TYPES) {
/* 157 */       if (type.isAssignableFrom(provider.getType().getClass())) {
/* 158 */         ClassLoader classLoader = provider.getClass().getClassLoader();
/* 159 */         Class<?>[] interfaces = { type, SerializableTypeProxy.class, Serializable.class };
/* 160 */         InvocationHandler handler = new TypeProxyInvocationHandler(provider);
/* 161 */         cached = (Type)Proxy.newProxyInstance(classLoader, interfaces, handler);
/* 162 */         cache.put(provider.getType(), cached);
/* 163 */         return cached;
/*     */       }
/*     */     }
/* 166 */     throw new IllegalArgumentException("Unsupported Type class: " + provider.getType().getClass().getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static abstract interface SerializableTypeProxy
/*     */   {
/*     */     public abstract SerializableTypeWrapper.TypeProvider getTypeProvider();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static abstract interface TypeProvider
/*     */     extends Serializable
/*     */   {
/*     */     public abstract Type getType();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract Object getSource();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static abstract class SimpleTypeProvider
/*     */     implements SerializableTypeWrapper.TypeProvider
/*     */   {
/*     */     public Object getSource()
/*     */     {
/* 207 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class TypeProxyInvocationHandler
/*     */     implements InvocationHandler, Serializable
/*     */   {
/*     */     private final SerializableTypeWrapper.TypeProvider provider;
/*     */     
/*     */ 
/*     */ 
/*     */     public TypeProxyInvocationHandler(SerializableTypeWrapper.TypeProvider provider)
/*     */     {
/* 223 */       this.provider = provider;
/*     */     }
/*     */     
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
/*     */     {
/* 228 */       if (method.getName().equals("equals")) {
/* 229 */         Object other = args[0];
/*     */         
/* 231 */         if ((other instanceof Type)) {
/* 232 */           other = SerializableTypeWrapper.unwrap((Type)other);
/*     */         }
/* 234 */         return Boolean.valueOf(this.provider.getType().equals(other));
/*     */       }
/* 236 */       if (method.getName().equals("hashCode")) {
/* 237 */         return Integer.valueOf(this.provider.getType().hashCode());
/*     */       }
/* 239 */       if (method.getName().equals("getTypeProvider")) {
/* 240 */         return this.provider;
/*     */       }
/*     */       
/* 243 */       if ((Type.class == method.getReturnType()) && (args == null)) {
/* 244 */         return SerializableTypeWrapper.forTypeProvider(new SerializableTypeWrapper.MethodInvokeTypeProvider(this.provider, method, -1));
/*     */       }
/* 246 */       if ((Type[].class == method.getReturnType()) && (args == null)) {
/* 247 */         Type[] result = new Type[((Type[])method.invoke(this.provider.getType(), args)).length];
/* 248 */         for (int i = 0; i < result.length; i++) {
/* 249 */           result[i] = SerializableTypeWrapper.forTypeProvider(new SerializableTypeWrapper.MethodInvokeTypeProvider(this.provider, method, i));
/*     */         }
/* 251 */         return result;
/*     */       }
/*     */       try
/*     */       {
/* 255 */         return method.invoke(this.provider.getType(), args);
/*     */       }
/*     */       catch (InvocationTargetException ex) {
/* 258 */         throw ex.getTargetException();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class FieldTypeProvider
/*     */     implements SerializableTypeWrapper.TypeProvider
/*     */   {
/*     */     private final String fieldName;
/*     */     
/*     */     private final Class<?> declaringClass;
/*     */     
/*     */     private transient Field field;
/*     */     
/*     */ 
/*     */     public FieldTypeProvider(Field field)
/*     */     {
/* 277 */       this.fieldName = field.getName();
/* 278 */       this.declaringClass = field.getDeclaringClass();
/* 279 */       this.field = field;
/*     */     }
/*     */     
/*     */     public Type getType()
/*     */     {
/* 284 */       return this.field.getGenericType();
/*     */     }
/*     */     
/*     */     public Object getSource()
/*     */     {
/* 289 */       return this.field;
/*     */     }
/*     */     
/*     */     private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
/* 293 */       inputStream.defaultReadObject();
/*     */       try {
/* 295 */         this.field = this.declaringClass.getDeclaredField(this.fieldName);
/*     */       }
/*     */       catch (Throwable ex) {
/* 298 */         throw new IllegalStateException("Could not find original class structure", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class MethodParameterTypeProvider
/*     */     implements SerializableTypeWrapper.TypeProvider
/*     */   {
/*     */     private final String methodName;
/*     */     
/*     */     private final Class<?>[] parameterTypes;
/*     */     
/*     */     private final Class<?> declaringClass;
/*     */     
/*     */     private final int parameterIndex;
/*     */     
/*     */     private transient MethodParameter methodParameter;
/*     */     
/*     */ 
/*     */     public MethodParameterTypeProvider(MethodParameter methodParameter)
/*     */     {
/* 321 */       if (methodParameter.getMethod() != null) {
/* 322 */         this.methodName = methodParameter.getMethod().getName();
/* 323 */         this.parameterTypes = methodParameter.getMethod().getParameterTypes();
/*     */       }
/*     */       else {
/* 326 */         this.methodName = null;
/* 327 */         this.parameterTypes = methodParameter.getConstructor().getParameterTypes();
/*     */       }
/* 329 */       this.declaringClass = methodParameter.getDeclaringClass();
/* 330 */       this.parameterIndex = methodParameter.getParameterIndex();
/* 331 */       this.methodParameter = methodParameter;
/*     */     }
/*     */     
/*     */ 
/*     */     public Type getType()
/*     */     {
/* 337 */       return this.methodParameter.getGenericParameterType();
/*     */     }
/*     */     
/*     */     public Object getSource()
/*     */     {
/* 342 */       return this.methodParameter;
/*     */     }
/*     */     
/*     */     private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
/* 346 */       inputStream.defaultReadObject();
/*     */       try {
/* 348 */         if (this.methodName != null)
/*     */         {
/* 350 */           this.methodParameter = new MethodParameter(this.declaringClass.getDeclaredMethod(this.methodName, this.parameterTypes), this.parameterIndex);
/*     */         }
/*     */         else
/*     */         {
/* 354 */           this.methodParameter = new MethodParameter(this.declaringClass.getDeclaredConstructor(this.parameterTypes), this.parameterIndex);
/*     */         }
/*     */       }
/*     */       catch (Throwable ex) {
/* 358 */         throw new IllegalStateException("Could not find original class structure", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class MethodInvokeTypeProvider
/*     */     implements SerializableTypeWrapper.TypeProvider
/*     */   {
/*     */     private final SerializableTypeWrapper.TypeProvider provider;
/*     */     
/*     */     private final String methodName;
/*     */     
/*     */     private final Class<?> declaringClass;
/*     */     
/*     */     private final int index;
/*     */     
/*     */     private transient Method method;
/*     */     
/*     */     private volatile transient Object result;
/*     */     
/*     */ 
/*     */     public MethodInvokeTypeProvider(SerializableTypeWrapper.TypeProvider provider, Method method, int index)
/*     */     {
/* 383 */       this.provider = provider;
/* 384 */       this.methodName = method.getName();
/* 385 */       this.declaringClass = method.getDeclaringClass();
/* 386 */       this.index = index;
/* 387 */       this.method = method;
/*     */     }
/*     */     
/*     */     public Type getType()
/*     */     {
/* 392 */       Object result = this.result;
/* 393 */       if (result == null)
/*     */       {
/* 395 */         result = ReflectionUtils.invokeMethod(this.method, this.provider.getType());
/*     */         
/* 397 */         this.result = result;
/*     */       }
/* 399 */       return (result instanceof Type[]) ? ((Type[])(Type[])result)[this.index] : (Type)result;
/*     */     }
/*     */     
/*     */     public Object getSource()
/*     */     {
/* 404 */       return null;
/*     */     }
/*     */     
/*     */     private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
/* 408 */       inputStream.defaultReadObject();
/* 409 */       this.method = ReflectionUtils.findMethod(this.declaringClass, this.methodName);
/* 410 */       if ((this.method.getReturnType() != Type.class) && (this.method.getReturnType() != Type[].class)) {
/* 411 */         throw new IllegalStateException("Invalid return type on deserialized method - needs to be Type or Type[]: " + this.method);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\core\SerializableTypeWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */