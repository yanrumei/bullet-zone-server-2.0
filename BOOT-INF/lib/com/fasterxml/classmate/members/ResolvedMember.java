/*     */ package com.fasterxml.classmate.members;
/*     */ 
/*     */ import com.fasterxml.classmate.Annotations;
/*     */ import com.fasterxml.classmate.ResolvedType;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Modifier;
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
/*     */ public abstract class ResolvedMember<T extends Member>
/*     */ {
/*     */   protected final ResolvedType _declaringType;
/*     */   protected final Annotations _annotations;
/*     */   protected final T _member;
/*     */   protected final ResolvedType _type;
/*     */   protected final int _hashCode;
/*     */   
/*     */   protected ResolvedMember(ResolvedType context, Annotations ann, T member, ResolvedType type)
/*     */   {
/*  39 */     this._declaringType = context;
/*  40 */     this._annotations = ann;
/*  41 */     this._member = member;
/*  42 */     this._type = type;
/*  43 */     this._hashCode = (this._member == null ? 0 : this._member.hashCode());
/*     */   }
/*     */   
/*     */   public void applyOverride(Annotation override)
/*     */   {
/*  48 */     this._annotations.add(override);
/*     */   }
/*     */   
/*     */   public void applyOverrides(Annotations overrides)
/*     */   {
/*  53 */     this._annotations.addAll(overrides);
/*     */   }
/*     */   
/*     */   public void applyDefault(Annotation override)
/*     */   {
/*  58 */     this._annotations.addAsDefault(override);
/*     */   }
/*     */   
/*     */   public <A extends Annotation> A get(Class<A> cls)
/*     */   {
/*  63 */     return this._annotations.get(cls);
/*     */   }
/*     */   
/*     */   public Annotations getAnnotations()
/*     */   {
/*  68 */     return this._annotations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ResolvedType getDeclaringType()
/*     */   {
/*  78 */     return this._declaringType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResolvedType getType()
/*     */   {
/*  86 */     return this._type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public T getRawMember()
/*     */   {
/*  93 */     return this._member;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  97 */     return getRawMember().getName();
/*     */   }
/*     */   
/*     */   public boolean isStatic() {
/* 101 */     return Modifier.isStatic(getModifiers());
/*     */   }
/*     */   
/*     */   public boolean isFinal() {
/* 105 */     return Modifier.isFinal(getModifiers());
/*     */   }
/*     */   
/*     */   public boolean isPrivate() {
/* 109 */     return Modifier.isPrivate(getModifiers());
/*     */   }
/*     */   
/*     */   public boolean isProtected() {
/* 113 */     return Modifier.isProtected(getModifiers());
/*     */   }
/*     */   
/*     */   public boolean isPublic() {
/* 117 */     return Modifier.isPublic(getModifiers());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 127 */     return getName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int getModifiers()
/*     */   {
/* 136 */     return getRawMember().getModifiers();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 145 */     return this._hashCode;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/* 150 */     if (o == this) return true;
/* 151 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 152 */     ResolvedMember<?> other = (ResolvedMember)o;
/* 153 */     return other._member == this._member;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\members\ResolvedMember.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */