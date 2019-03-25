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
/*     */ public final class ResolvedArrayType
/*     */   extends ResolvedType
/*     */ {
/*     */   protected final ResolvedType _elementType;
/*     */   
/*     */   public ResolvedArrayType(Class<?> erased, TypeBindings bindings, ResolvedType elementType)
/*     */   {
/*  21 */     super(erased, bindings);
/*  22 */     this._elementType = elementType;
/*     */   }
/*     */   
/*     */   public boolean canCreateSubtypes()
/*     */   {
/*  27 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResolvedType getParentClass()
/*     */   {
/*  37 */     return null;
/*     */   }
/*     */   
/*  40 */   public ResolvedType getSelfReferencedType() { return null; }
/*     */   
/*     */   public List<ResolvedType> getImplementedInterfaces()
/*     */   {
/*  44 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInterface()
/*     */   {
/*  55 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/*  59 */     return false;
/*     */   }
/*     */   
/*  62 */   public ResolvedType getArrayElementType() { return this._elementType; }
/*     */   
/*     */   public boolean isArray() {
/*  65 */     return true;
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
/*  86 */     sb.append('[');
/*  87 */     return this._elementType.appendSignature(sb);
/*     */   }
/*     */   
/*     */   public StringBuilder appendErasedSignature(StringBuilder sb)
/*     */   {
/*  92 */     sb.append('[');
/*  93 */     return this._elementType.appendErasedSignature(sb);
/*     */   }
/*     */   
/*     */ 
/*     */   public StringBuilder appendBriefDescription(StringBuilder sb)
/*     */   {
/*  99 */     sb = this._elementType.appendBriefDescription(sb);
/* 100 */     sb.append("[]");
/* 101 */     return sb;
/*     */   }
/*     */   
/*     */   public StringBuilder appendFullDescription(StringBuilder sb)
/*     */   {
/* 106 */     return appendBriefDescription(sb);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\types\ResolvedArrayType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */