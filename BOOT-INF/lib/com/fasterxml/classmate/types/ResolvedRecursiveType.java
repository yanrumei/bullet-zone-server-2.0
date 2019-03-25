/*     */ package com.fasterxml.classmate.types;
/*     */ 
/*     */ import com.fasterxml.classmate.ResolvedType;
/*     */ import com.fasterxml.classmate.TypeBindings;
/*     */ import com.fasterxml.classmate.members.RawConstructor;
/*     */ import com.fasterxml.classmate.members.RawField;
/*     */ import com.fasterxml.classmate.members.RawMethod;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ public class ResolvedRecursiveType
/*     */   extends ResolvedType
/*     */ {
/*     */   protected ResolvedType _referencedType;
/*     */   
/*     */   public ResolvedRecursiveType(Class<?> erased, TypeBindings bindings)
/*     */   {
/*  35 */     super(erased, bindings);
/*     */   }
/*     */   
/*     */   public boolean canCreateSubtypes()
/*     */   {
/*  40 */     return this._referencedType.canCreateSubtypes();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setReference(ResolvedType ref)
/*     */   {
/*  46 */     if (this._referencedType != null) {
/*  47 */       throw new IllegalStateException("Trying to re-set self reference; old value = " + this._referencedType + ", new = " + ref);
/*     */     }
/*  49 */     this._referencedType = ref;
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
/*     */   public ResolvedType getParentClass()
/*     */   {
/*  63 */     return null;
/*     */   }
/*     */   
/*     */   public ResolvedType getSelfReferencedType() {
/*  67 */     return this._referencedType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<ResolvedType> getImplementedInterfaces()
/*     */   {
/*  74 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResolvedType getArrayElementType()
/*     */   {
/*  82 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInterface()
/*     */   {
/*  92 */     return this._erasedType.isInterface();
/*     */   }
/*     */   
/*  95 */   public boolean isAbstract() { return Modifier.isAbstract(this._erasedType.getModifiers()); }
/*     */   
/*     */   public boolean isArray() {
/*  98 */     return this._erasedType.isArray();
/*     */   }
/*     */   
/* 101 */   public boolean isPrimitive() { return false; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 110 */   public List<RawField> getMemberFields() { return this._referencedType.getMemberFields(); }
/*     */   
/* 112 */   public List<RawField> getStaticFields() { return this._referencedType.getStaticFields(); }
/*     */   
/* 114 */   public List<RawMethod> getStaticMethods() { return this._referencedType.getStaticMethods(); }
/*     */   
/* 116 */   public List<RawMethod> getMemberMethods() { return this._referencedType.getMemberMethods(); }
/*     */   
/* 118 */   public List<RawConstructor> getConstructors() { return this._referencedType.getConstructors(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringBuilder appendSignature(StringBuilder sb)
/*     */   {
/* 129 */     return appendErasedSignature(sb);
/*     */   }
/*     */   
/*     */   public StringBuilder appendErasedSignature(StringBuilder sb)
/*     */   {
/* 134 */     return _appendErasedClassSignature(sb);
/*     */   }
/*     */   
/*     */   public StringBuilder appendBriefDescription(StringBuilder sb)
/*     */   {
/* 139 */     return _appendClassDescription(sb);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public StringBuilder appendFullDescription(StringBuilder sb)
/*     */   {
/* 146 */     return appendBriefDescription(sb);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 157 */     if (!super.equals(o)) {
/* 158 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 162 */     ResolvedRecursiveType other = (ResolvedRecursiveType)o;
/* 163 */     if (this._referencedType == null) {
/* 164 */       return other._referencedType == null;
/*     */     }
/* 166 */     return this._referencedType.equals(other._referencedType);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\types\ResolvedRecursiveType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */