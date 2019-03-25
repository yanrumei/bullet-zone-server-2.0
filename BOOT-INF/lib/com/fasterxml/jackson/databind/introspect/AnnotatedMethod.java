/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
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
/*     */ public final class AnnotatedMethod
/*     */   extends AnnotatedWithParams
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final transient Method _method;
/*     */   protected Class<?>[] _paramClasses;
/*     */   protected Serialization _serialization;
/*     */   
/*     */   public AnnotatedMethod(TypeResolutionContext ctxt, Method method, AnnotationMap classAnn, AnnotationMap[] paramAnnotations)
/*     */   {
/*  37 */     super(ctxt, classAnn, paramAnnotations);
/*  38 */     if (method == null) {
/*  39 */       throw new IllegalArgumentException("Can not construct AnnotatedMethod with null Method");
/*     */     }
/*  41 */     this._method = method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AnnotatedMethod(Serialization ser)
/*     */   {
/*  50 */     super(null, null, null);
/*  51 */     this._method = null;
/*  52 */     this._serialization = ser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedMethod withMethod(Method m)
/*     */   {
/*  60 */     return new AnnotatedMethod(this._typeContext, m, this._annotations, this._paramAnnotations);
/*     */   }
/*     */   
/*     */   public AnnotatedMethod withAnnotations(AnnotationMap ann)
/*     */   {
/*  65 */     return new AnnotatedMethod(this._typeContext, this._method, ann, this._paramAnnotations);
/*     */   }
/*     */   
/*     */   public Method getAnnotated()
/*     */   {
/*  70 */     return this._method;
/*     */   }
/*     */   
/*  73 */   public int getModifiers() { return this._method.getModifiers(); }
/*     */   
/*     */   public String getName() {
/*  76 */     return this._method.getName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType getType()
/*     */   {
/*  85 */     return this._typeContext.resolveType(this._method.getGenericReturnType());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getRawType()
/*     */   {
/*  95 */     return this._method.getReturnType();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Type getGenericType()
/*     */   {
/* 101 */     return this._method.getGenericReturnType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Object call()
/*     */     throws Exception
/*     */   {
/* 112 */     return this._method.invoke(null, new Object[0]);
/*     */   }
/*     */   
/*     */   public final Object call(Object[] args) throws Exception
/*     */   {
/* 117 */     return this._method.invoke(null, args);
/*     */   }
/*     */   
/*     */   public final Object call1(Object arg) throws Exception
/*     */   {
/* 122 */     return this._method.invoke(null, new Object[] { arg });
/*     */   }
/*     */   
/*     */   public final Object callOn(Object pojo) throws Exception {
/* 126 */     return this._method.invoke(pojo, new Object[0]);
/*     */   }
/*     */   
/*     */   public final Object callOnWith(Object pojo, Object... args) throws Exception {
/* 130 */     return this._method.invoke(pojo, args);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getParameterCount()
/*     */   {
/* 141 */     return getRawParameterTypes().length;
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?> getRawParameterType(int index)
/*     */   {
/* 147 */     Class<?>[] types = getRawParameterTypes();
/* 148 */     return index >= types.length ? null : types[index];
/*     */   }
/*     */   
/*     */   public JavaType getParameterType(int index)
/*     */   {
/* 153 */     Type[] types = this._method.getGenericParameterTypes();
/* 154 */     if (index >= types.length) {
/* 155 */       return null;
/*     */     }
/* 157 */     return this._typeContext.resolveType(types[index]);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Type getGenericParameterType(int index)
/*     */   {
/* 163 */     Type[] types = getGenericParameterTypes();
/* 164 */     if (index >= types.length) {
/* 165 */       return null;
/*     */     }
/* 167 */     return types[index];
/*     */   }
/*     */   
/*     */   public Class<?> getDeclaringClass() {
/* 171 */     return this._method.getDeclaringClass();
/*     */   }
/*     */   
/* 174 */   public Method getMember() { return this._method; }
/*     */   
/*     */   public void setValue(Object pojo, Object value) throws IllegalArgumentException
/*     */   {
/*     */     try
/*     */     {
/* 180 */       this._method.invoke(pojo, new Object[] { value });
/*     */     } catch (IllegalAccessException e) {
/* 182 */       throw new IllegalArgumentException("Failed to setValue() with method " + getFullName() + ": " + e.getMessage(), e);
/*     */     }
/*     */     catch (InvocationTargetException e) {
/* 185 */       throw new IllegalArgumentException("Failed to setValue() with method " + getFullName() + ": " + e.getMessage(), e);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object getValue(Object pojo)
/*     */     throws IllegalArgumentException
/*     */   {
/*     */     try
/*     */     {
/* 194 */       return this._method.invoke(pojo, new Object[0]);
/*     */     } catch (IllegalAccessException e) {
/* 196 */       throw new IllegalArgumentException("Failed to getValue() with method " + getFullName() + ": " + e.getMessage(), e);
/*     */     }
/*     */     catch (InvocationTargetException e) {
/* 199 */       throw new IllegalArgumentException("Failed to getValue() with method " + getFullName() + ": " + e.getMessage(), e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFullName()
/*     */   {
/* 211 */     return getDeclaringClass().getName() + "#" + getName() + "(" + getParameterCount() + " params)";
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?>[] getRawParameterTypes()
/*     */   {
/* 217 */     if (this._paramClasses == null) {
/* 218 */       this._paramClasses = this._method.getParameterTypes();
/*     */     }
/* 220 */     return this._paramClasses;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Type[] getGenericParameterTypes() {
/* 225 */     return this._method.getGenericParameterTypes();
/*     */   }
/*     */   
/*     */   public Class<?> getRawReturnType() {
/* 229 */     return this._method.getReturnType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasReturnType()
/*     */   {
/* 240 */     Class<?> rt = getRawReturnType();
/* 241 */     return (rt != Void.TYPE) && (rt != Void.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 252 */     return "[method " + getFullName() + "]";
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 257 */     return this._method.getName().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 262 */     if (o == this) return true;
/* 263 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 264 */     return ((AnnotatedMethod)o)._method == this._method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 274 */     return new AnnotatedMethod(new Serialization(this._method));
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 278 */     Class<?> clazz = this._serialization.clazz;
/*     */     try {
/* 280 */       Method m = clazz.getDeclaredMethod(this._serialization.name, this._serialization.args);
/*     */       
/*     */ 
/* 283 */       if (!m.isAccessible()) {
/* 284 */         ClassUtil.checkAndFixAccess(m, false);
/*     */       }
/* 286 */       return new AnnotatedMethod(null, m, null, null);
/*     */     } catch (Exception e) {
/* 288 */       throw new IllegalArgumentException("Could not find method '" + this._serialization.name + "' from Class '" + clazz.getName());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class Serialization
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected Class<?> clazz;
/*     */     
/*     */     protected String name;
/*     */     
/*     */     protected Class<?>[] args;
/*     */     
/*     */ 
/*     */     public Serialization(Method setter)
/*     */     {
/* 307 */       this.clazz = setter.getDeclaringClass();
/* 308 */       this.name = setter.getName();
/* 309 */       this.args = setter.getParameterTypes();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedMethod.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */