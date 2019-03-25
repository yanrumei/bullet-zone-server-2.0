/*     */ package com.fasterxml.classmate.types;
/*     */ 
/*     */ import com.fasterxml.classmate.ResolvedType;
/*     */ import com.fasterxml.classmate.TypeBindings;
/*     */ import com.fasterxml.classmate.members.RawField;
/*     */ import com.fasterxml.classmate.members.RawMethod;
/*     */ import java.util.Arrays;
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
/*     */ public class ResolvedInterfaceType
/*     */   extends ResolvedType
/*     */ {
/*     */   protected final ResolvedType[] _superInterfaces;
/*     */   protected RawField[] _constantFields;
/*     */   protected RawMethod[] _memberMethods;
/*     */   
/*     */   public ResolvedInterfaceType(Class<?> erased, TypeBindings bindings, ResolvedType[] superInterfaces)
/*     */   {
/*  36 */     super(erased, bindings);
/*  37 */     this._superInterfaces = (superInterfaces == null ? NO_TYPES : superInterfaces);
/*     */   }
/*     */   
/*     */   public boolean canCreateSubtypes()
/*     */   {
/*  42 */     return true;
/*     */   }
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
/*  54 */     return null;
/*     */   }
/*     */   
/*     */   public ResolvedType getSelfReferencedType() {
/*  58 */     return null;
/*     */   }
/*     */   
/*     */   public List<ResolvedType> getImplementedInterfaces() {
/*  62 */     return this._superInterfaces.length == 0 ? Collections.emptyList() : Arrays.asList(this._superInterfaces);
/*     */   }
/*     */   
/*     */ 
/*     */   public ResolvedType getArrayElementType()
/*     */   {
/*  68 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isInterface()
/*     */   {
/*  78 */     return true;
/*     */   }
/*     */   
/*  81 */   public boolean isAbstract() { return true; }
/*     */   
/*     */   public boolean isArray() {
/*  84 */     return false;
/*     */   }
/*     */   
/*  87 */   public boolean isPrimitive() { return false; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized List<RawField> getStaticFields()
/*     */   {
/*  99 */     if (this._constantFields == null) {
/* 100 */       this._constantFields = _getFields(true);
/*     */     }
/* 102 */     if (this._constantFields.length == 0) {
/* 103 */       return Collections.emptyList();
/*     */     }
/* 105 */     return Arrays.asList(this._constantFields);
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized List<RawMethod> getMemberMethods()
/*     */   {
/* 111 */     if (this._memberMethods == null) {
/* 112 */       this._memberMethods = _getMethods(false);
/*     */     }
/* 114 */     if (this._memberMethods.length == 0) {
/* 115 */       return Collections.emptyList();
/*     */     }
/* 117 */     return Arrays.asList(this._memberMethods);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StringBuilder appendSignature(StringBuilder sb)
/*     */   {
/* 128 */     return _appendClassSignature(sb);
/*     */   }
/*     */   
/*     */   public StringBuilder appendErasedSignature(StringBuilder sb)
/*     */   {
/* 133 */     return _appendErasedClassSignature(sb);
/*     */   }
/*     */   
/*     */   public StringBuilder appendBriefDescription(StringBuilder sb)
/*     */   {
/* 138 */     return _appendClassDescription(sb);
/*     */   }
/*     */   
/*     */ 
/*     */   public StringBuilder appendFullDescription(StringBuilder sb)
/*     */   {
/* 144 */     sb = _appendClassDescription(sb);
/*     */     
/* 146 */     int count = this._superInterfaces.length;
/* 147 */     if (count > 0) {
/* 148 */       sb.append(" extends ");
/* 149 */       for (int i = 0; i < count; i++) {
/* 150 */         if (i > 0) {
/* 151 */           sb.append(",");
/*     */         }
/* 153 */         sb = this._superInterfaces[i].appendBriefDescription(sb);
/*     */       }
/*     */     }
/* 156 */     return sb;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\types\ResolvedInterfaceType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */