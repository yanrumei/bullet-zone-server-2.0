/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Modifier;
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
/*     */ 
/*     */ 
/*     */ public final class AnnotatedField
/*     */   extends AnnotatedMember
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final transient Field _field;
/*     */   protected Serialization _serialization;
/*     */   
/*     */   public AnnotatedField(TypeResolutionContext contextClass, Field field, AnnotationMap annMap)
/*     */   {
/*  39 */     super(contextClass, annMap);
/*  40 */     this._field = field;
/*     */   }
/*     */   
/*     */   public AnnotatedField withAnnotations(AnnotationMap ann)
/*     */   {
/*  45 */     return new AnnotatedField(this._typeContext, this._field, ann);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AnnotatedField(Serialization ser)
/*     */   {
/*  53 */     super(null, null);
/*  54 */     this._field = null;
/*  55 */     this._serialization = ser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Field getAnnotated()
/*     */   {
/*  65 */     return this._field;
/*     */   }
/*     */   
/*  68 */   public int getModifiers() { return this._field.getModifiers(); }
/*     */   
/*     */   public String getName() {
/*  71 */     return this._field.getName();
/*     */   }
/*     */   
/*     */   public Class<?> getRawType() {
/*  75 */     return this._field.getType();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Type getGenericType()
/*     */   {
/*  81 */     return this._field.getGenericType();
/*     */   }
/*     */   
/*     */   public JavaType getType()
/*     */   {
/*  86 */     return this._typeContext.resolveType(this._field.getGenericType());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getDeclaringClass()
/*     */   {
/*  96 */     return this._field.getDeclaringClass();
/*     */   }
/*     */   
/*  99 */   public Member getMember() { return this._field; }
/*     */   
/*     */   public void setValue(Object pojo, Object value) throws IllegalArgumentException
/*     */   {
/*     */     try
/*     */     {
/* 105 */       this._field.set(pojo, value);
/*     */     } catch (IllegalAccessException e) {
/* 107 */       throw new IllegalArgumentException("Failed to setValue() for field " + getFullName() + ": " + e.getMessage(), e);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object getValue(Object pojo)
/*     */     throws IllegalArgumentException
/*     */   {
/*     */     try
/*     */     {
/* 116 */       return this._field.get(pojo);
/*     */     } catch (IllegalAccessException e) {
/* 118 */       throw new IllegalArgumentException("Failed to getValue() for field " + getFullName() + ": " + e.getMessage(), e);
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
/* 130 */     return getDeclaringClass().getName() + "#" + getName();
/*     */   }
/*     */   
/* 133 */   public int getAnnotationCount() { return this._annotations.size(); }
/*     */   
/*     */ 
/*     */   public boolean isTransient()
/*     */   {
/* 138 */     return Modifier.isTransient(getModifiers());
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 142 */     return this._field.getName().hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 147 */     if (o == this) return true;
/* 148 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 149 */     return ((AnnotatedField)o)._field == this._field;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 154 */     return "[field " + getFullName() + "]";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Object writeReplace()
/*     */   {
/* 164 */     return new AnnotatedField(new Serialization(this._field));
/*     */   }
/*     */   
/*     */   Object readResolve() {
/* 168 */     Class<?> clazz = this._serialization.clazz;
/*     */     try {
/* 170 */       Field f = clazz.getDeclaredField(this._serialization.name);
/*     */       
/* 172 */       if (!f.isAccessible()) {
/* 173 */         ClassUtil.checkAndFixAccess(f, false);
/*     */       }
/* 175 */       return new AnnotatedField(null, f, null);
/*     */     } catch (Exception e) {
/* 177 */       throw new IllegalArgumentException("Could not find method '" + this._serialization.name + "' from Class '" + clazz.getName());
/*     */     }
/*     */   }
/*     */   
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
/*     */ 
/*     */     public Serialization(Field f)
/*     */     {
/* 195 */       this.clazz = f.getDeclaringClass();
/* 196 */       this.name = f.getName();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedField.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */