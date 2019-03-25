/*     */ package com.fasterxml.classmate.types;
/*     */ 
/*     */ import com.fasterxml.classmate.ResolvedType;
/*     */ import com.fasterxml.classmate.TypeBindings;
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
/*     */ public class TypePlaceHolder
/*     */   extends ResolvedType
/*     */ {
/*     */   protected final int _ordinal;
/*     */   protected ResolvedType _actualType;
/*     */   
/*     */   public TypePlaceHolder(int ordinal)
/*     */   {
/*  24 */     super(Object.class, TypeBindings.emptyBindings());
/*  25 */     this._ordinal = ordinal;
/*     */   }
/*     */   
/*     */ 
/*  29 */   public boolean canCreateSubtypes() { return false; }
/*     */   
/*  31 */   public ResolvedType actualType() { return this._actualType; }
/*  32 */   public void actualType(ResolvedType t) { this._actualType = t; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResolvedType getParentClass()
/*     */   {
/*  41 */     return null;
/*     */   }
/*     */   
/*  44 */   public ResolvedType getSelfReferencedType() { return null; }
/*     */   
/*     */   public List<ResolvedType> getImplementedInterfaces() {
/*  47 */     return Collections.emptyList();
/*     */   }
/*     */   
/*  50 */   public ResolvedType getArrayElementType() { return null; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInterface()
/*     */   {
/*  59 */     return false;
/*     */   }
/*     */   
/*  62 */   public boolean isAbstract() { return true; }
/*     */   
/*     */   public boolean isArray() {
/*  65 */     return false;
/*     */   }
/*     */   
/*  68 */   public boolean isPrimitive() { return false; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringBuilder appendSignature(StringBuilder sb)
/*     */   {
/*  78 */     return _appendClassSignature(sb);
/*     */   }
/*     */   
/*     */   public StringBuilder appendErasedSignature(StringBuilder sb)
/*     */   {
/*  83 */     return _appendErasedClassSignature(sb);
/*     */   }
/*     */   
/*     */   public StringBuilder appendBriefDescription(StringBuilder sb)
/*     */   {
/*  88 */     sb.append('<').append(this._ordinal).append('>');
/*  89 */     return sb;
/*     */   }
/*     */   
/*     */   public StringBuilder appendFullDescription(StringBuilder sb)
/*     */   {
/*  94 */     return appendBriefDescription(sb);
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
/*     */   public boolean equals(Object o)
/*     */   {
/* 107 */     return o == this;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\types\TypePlaceHolder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */