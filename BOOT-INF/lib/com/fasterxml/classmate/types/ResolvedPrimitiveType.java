/*     */ package com.fasterxml.classmate.types;
/*     */ 
/*     */ import com.fasterxml.classmate.ResolvedType;
/*     */ import com.fasterxml.classmate.TypeBindings;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ResolvedPrimitiveType
/*     */   extends ResolvedType
/*     */ {
/*  17 */   private static final ResolvedPrimitiveType VOID = new ResolvedPrimitiveType(Void.TYPE, 'V', "void");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String _signature;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final String _description;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ResolvedPrimitiveType(Class<?> erased, char sig, String desc)
/*     */   {
/*  38 */     super(erased, TypeBindings.emptyBindings());
/*  39 */     this._signature = String.valueOf(sig);
/*  40 */     this._description = desc;
/*     */   }
/*     */   
/*     */   public static List<ResolvedPrimitiveType> all()
/*     */   {
/*  45 */     ArrayList<ResolvedPrimitiveType> all = new ArrayList();
/*  46 */     all.add(new ResolvedPrimitiveType(Boolean.TYPE, 'Z', "boolean"));
/*  47 */     all.add(new ResolvedPrimitiveType(Byte.TYPE, 'B', "byte"));
/*  48 */     all.add(new ResolvedPrimitiveType(Short.TYPE, 'S', "short"));
/*  49 */     all.add(new ResolvedPrimitiveType(Character.TYPE, 'C', "char"));
/*  50 */     all.add(new ResolvedPrimitiveType(Integer.TYPE, 'I', "int"));
/*  51 */     all.add(new ResolvedPrimitiveType(Long.TYPE, 'J', "long"));
/*  52 */     all.add(new ResolvedPrimitiveType(Float.TYPE, 'F', "float"));
/*  53 */     all.add(new ResolvedPrimitiveType(Double.TYPE, 'D', "double"));
/*  54 */     return all;
/*     */   }
/*     */   
/*     */   public static ResolvedPrimitiveType voidType()
/*     */   {
/*  59 */     return VOID;
/*     */   }
/*     */   
/*     */   public boolean canCreateSubtypes()
/*     */   {
/*  64 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResolvedType getSelfReferencedType()
/*     */   {
/*  74 */     return null;
/*     */   }
/*     */   
/*  77 */   public ResolvedType getParentClass() { return null; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInterface()
/*     */   {
/*  86 */     return false;
/*     */   }
/*     */   
/*  89 */   public boolean isAbstract() { return false; }
/*     */   
/*     */   public ResolvedType getArrayElementType() {
/*  92 */     return null;
/*     */   }
/*     */   
/*  95 */   public boolean isArray() { return false; }
/*     */   
/*     */   public boolean isPrimitive() {
/*  98 */     return true;
/*     */   }
/*     */   
/*     */   public List<ResolvedType> getImplementedInterfaces() {
/* 102 */     return Collections.emptyList();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getSignature()
/*     */   {
/* 121 */     return this._signature;
/*     */   }
/*     */   
/*     */   public String getErasedSignature()
/*     */   {
/* 126 */     return this._signature;
/*     */   }
/*     */   
/*     */   public String getFullDescription()
/*     */   {
/* 131 */     return this._description;
/*     */   }
/*     */   
/*     */   public StringBuilder appendSignature(StringBuilder sb)
/*     */   {
/* 136 */     sb.append(this._signature);
/* 137 */     return sb;
/*     */   }
/*     */   
/*     */   public StringBuilder appendErasedSignature(StringBuilder sb)
/*     */   {
/* 142 */     sb.append(this._signature);
/* 143 */     return sb;
/*     */   }
/*     */   
/*     */   public StringBuilder appendFullDescription(StringBuilder sb)
/*     */   {
/* 148 */     sb.append(this._description);
/* 149 */     return sb;
/*     */   }
/*     */   
/*     */   public StringBuilder appendBriefDescription(StringBuilder sb)
/*     */   {
/* 154 */     sb.append(this._description);
/* 155 */     return sb;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\types\ResolvedPrimitiveType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */