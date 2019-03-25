/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
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
/*     */ public class VirtualAnnotatedMember
/*     */   extends AnnotatedMember
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final Class<?> _declaringClass;
/*     */   protected final JavaType _type;
/*     */   protected final String _name;
/*     */   
/*     */   public VirtualAnnotatedMember(TypeResolutionContext typeContext, Class<?> declaringClass, String name, JavaType type)
/*     */   {
/*  36 */     super(typeContext, null);
/*  37 */     this._declaringClass = declaringClass;
/*  38 */     this._type = type;
/*  39 */     this._name = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public VirtualAnnotatedMember(TypeResolutionContext typeContext, Class<?> declaringClass, String name, Class<?> rawType)
/*     */   {
/*  48 */     this(typeContext, declaringClass, name, typeContext.resolveType(rawType));
/*     */   }
/*     */   
/*     */   public Annotated withAnnotations(AnnotationMap fallback)
/*     */   {
/*  53 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Field getAnnotated()
/*     */   {
/*  63 */     return null;
/*     */   }
/*     */   
/*  66 */   public int getModifiers() { return 0; }
/*     */   
/*     */   public String getName() {
/*  69 */     return this._name;
/*     */   }
/*     */   
/*     */   public Class<?> getRawType() {
/*  73 */     return this._type.getRawClass();
/*     */   }
/*     */   
/*     */   public JavaType getType()
/*     */   {
/*  78 */     return this._type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getDeclaringClass()
/*     */   {
/*  88 */     return this._declaringClass;
/*     */   }
/*     */   
/*  91 */   public Member getMember() { return null; }
/*     */   
/*     */   public void setValue(Object pojo, Object value) throws IllegalArgumentException
/*     */   {
/*  95 */     throw new IllegalArgumentException("Can not set virtual property '" + this._name + "'");
/*     */   }
/*     */   
/*     */   public Object getValue(Object pojo) throws IllegalArgumentException
/*     */   {
/* 100 */     throw new IllegalArgumentException("Can not get virtual property '" + this._name + "'");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getFullName()
/*     */   {
/* 110 */     return getDeclaringClass().getName() + "#" + getName();
/*     */   }
/*     */   
/* 113 */   public int getAnnotationCount() { return 0; }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 117 */     return this._name.hashCode();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 122 */     if (o == this) return true;
/* 123 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 124 */     VirtualAnnotatedMember other = (VirtualAnnotatedMember)o;
/* 125 */     return (other._declaringClass == this._declaringClass) && (other._name.equals(this._name));
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 131 */     return "[field " + getFullName() + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\VirtualAnnotatedMember.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */