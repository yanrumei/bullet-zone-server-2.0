/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import java.lang.reflect.AnnotatedElement;
/*     */ import java.lang.reflect.Member;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AnnotatedParameter
/*     */   extends AnnotatedMember
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotatedWithParams _owner;
/*     */   protected final JavaType _type;
/*     */   protected final int _index;
/*     */   
/*     */   public AnnotatedParameter(AnnotatedWithParams owner, JavaType type, AnnotationMap annotations, int index)
/*     */   {
/*  43 */     super(owner == null ? null : owner.getTypeContext(), annotations);
/*  44 */     this._owner = owner;
/*  45 */     this._type = type;
/*  46 */     this._index = index;
/*     */   }
/*     */   
/*     */   public AnnotatedParameter withAnnotations(AnnotationMap ann)
/*     */   {
/*  51 */     if (ann == this._annotations) {
/*  52 */       return this;
/*     */     }
/*  54 */     return this._owner.replaceParameterAnnotations(this._index, ann);
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
/*     */   public AnnotatedElement getAnnotated()
/*     */   {
/*  68 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getModifiers()
/*     */   {
/*  75 */     return this._owner.getModifiers();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/*  82 */     return "";
/*     */   }
/*     */   
/*     */   public Class<?> getRawType() {
/*  86 */     return this._type.getRawClass();
/*     */   }
/*     */   
/*     */   public JavaType getType()
/*     */   {
/*  91 */     return this._type;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Type getGenericType()
/*     */   {
/*  97 */     return this._owner.getGenericParameterType(this._index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> getDeclaringClass()
/*     */   {
/* 108 */     return this._owner.getDeclaringClass();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Member getMember()
/*     */   {
/* 116 */     return this._owner.getMember();
/*     */   }
/*     */   
/*     */   public void setValue(Object pojo, Object value)
/*     */     throws UnsupportedOperationException
/*     */   {
/* 122 */     throw new UnsupportedOperationException("Cannot call setValue() on constructor parameter of " + getDeclaringClass().getName());
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getValue(Object pojo)
/*     */     throws UnsupportedOperationException
/*     */   {
/* 129 */     throw new UnsupportedOperationException("Cannot call getValue() on constructor parameter of " + getDeclaringClass().getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Type getParameterType()
/*     */   {
/* 139 */     return this._type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedWithParams getOwner()
/*     */   {
/* 147 */     return this._owner;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int getIndex()
/*     */   {
/* 154 */     return this._index;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 164 */     return this._owner.hashCode() + this._index;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 169 */     if (o == this) return true;
/* 170 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 171 */     AnnotatedParameter other = (AnnotatedParameter)o;
/* 172 */     return (other._owner.equals(this._owner)) && (other._index == this._index);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 177 */     return "[parameter #" + getIndex() + ", annotations: " + this._annotations + "]";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedParameter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */