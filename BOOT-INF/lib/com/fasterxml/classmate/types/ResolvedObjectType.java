/*     */ package com.fasterxml.classmate.types;
/*     */ 
/*     */ import com.fasterxml.classmate.ResolvedType;
/*     */ import com.fasterxml.classmate.TypeBindings;
/*     */ import com.fasterxml.classmate.members.RawConstructor;
/*     */ import com.fasterxml.classmate.members.RawField;
/*     */ import com.fasterxml.classmate.members.RawMethod;
/*     */ import java.lang.reflect.Modifier;
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
/*     */ public class ResolvedObjectType
/*     */   extends ResolvedType
/*     */ {
/*     */   protected final ResolvedType _superClass;
/*     */   protected final ResolvedType[] _superInterfaces;
/*     */   protected final int _modifiers;
/*     */   protected RawConstructor[] _constructors;
/*     */   protected RawField[] _memberFields;
/*     */   protected RawField[] _staticFields;
/*     */   protected RawMethod[] _memberMethods;
/*     */   protected RawMethod[] _staticMethods;
/*     */   
/*     */   public ResolvedObjectType(Class<?> erased, TypeBindings bindings, ResolvedType superClass, List<ResolvedType> interfaces)
/*     */   {
/*  57 */     this(erased, bindings, superClass, (interfaces == null) || (interfaces.isEmpty()) ? NO_TYPES : (ResolvedType[])interfaces.toArray(new ResolvedType[interfaces.size()]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResolvedObjectType(Class<?> erased, TypeBindings bindings, ResolvedType superClass, ResolvedType[] interfaces)
/*     */   {
/*  65 */     super(erased, bindings);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  72 */     if ((superClass != null) && 
/*  73 */       (!(superClass instanceof ResolvedObjectType)) && (!(superClass instanceof ResolvedRecursiveType)))
/*     */     {
/*     */ 
/*  76 */       throw new IllegalArgumentException("Unexpected parent type for " + erased.getName() + ": " + superClass.getClass().getName());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  81 */     this._superClass = superClass;
/*  82 */     this._superInterfaces = (interfaces == null ? NO_TYPES : interfaces);
/*  83 */     this._modifiers = erased.getModifiers();
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public ResolvedObjectType(Class<?> erased, TypeBindings bindings, ResolvedObjectType superClass, List<ResolvedType> interfaces)
/*     */   {
/*  90 */     this(erased, bindings, superClass, interfaces);
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public ResolvedObjectType(Class<?> erased, TypeBindings bindings, ResolvedObjectType superClass, ResolvedType[] interfaces)
/*     */   {
/*  97 */     this(erased, bindings, superClass, interfaces);
/*     */   }
/*     */   
/*     */ 
/*     */   public static ResolvedObjectType create(Class<?> erased, TypeBindings bindings, ResolvedType superClass, List<ResolvedType> interfaces)
/*     */   {
/* 103 */     return new ResolvedObjectType(erased, bindings, superClass, interfaces);
/*     */   }
/*     */   
/*     */   public boolean canCreateSubtypes()
/*     */   {
/* 108 */     return true;
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
/*     */   public ResolvedObjectType getParentClass()
/*     */   {
/* 125 */     if (this._superClass == null) {
/* 126 */       return null;
/*     */     }
/* 128 */     if ((this._superClass instanceof ResolvedObjectType)) {
/* 129 */       return (ResolvedObjectType)this._superClass;
/*     */     }
/* 131 */     ResolvedType rt = ((ResolvedRecursiveType)this._superClass).getSelfReferencedType();
/* 132 */     if (!(rt instanceof ResolvedObjectType)) {
/* 133 */       throw new IllegalStateException("Internal error: self-referential parent type (" + this._superClass + ") does not resolve into proper ResolvedObjectType, but instead to: " + rt);
/*     */     }
/*     */     
/*     */ 
/* 137 */     return (ResolvedObjectType)rt;
/*     */   }
/*     */   
/*     */   public ResolvedType getSelfReferencedType() {
/* 141 */     return null;
/*     */   }
/*     */   
/*     */   public List<ResolvedType> getImplementedInterfaces() {
/* 145 */     return this._superInterfaces.length == 0 ? Collections.emptyList() : Arrays.asList(this._superInterfaces);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ResolvedType getArrayElementType()
/*     */   {
/* 156 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isInterface()
/*     */   {
/* 165 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isAbstract() {
/* 169 */     return Modifier.isAbstract(this._modifiers);
/*     */   }
/*     */   
/*     */   public final boolean isArray() {
/* 173 */     return false;
/*     */   }
/*     */   
/* 176 */   public final boolean isPrimitive() { return false; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized List<RawField> getMemberFields()
/*     */   {
/* 187 */     if (this._memberFields == null) {
/* 188 */       this._memberFields = _getFields(false);
/*     */     }
/* 190 */     if (this._memberFields.length == 0) {
/* 191 */       return Collections.emptyList();
/*     */     }
/* 193 */     return Arrays.asList(this._memberFields);
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized List<RawField> getStaticFields()
/*     */   {
/* 199 */     if (this._staticFields == null) {
/* 200 */       this._staticFields = _getFields(true);
/*     */     }
/* 202 */     if (this._staticFields.length == 0) {
/* 203 */       return Collections.emptyList();
/*     */     }
/* 205 */     return Arrays.asList(this._staticFields);
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized List<RawMethod> getMemberMethods()
/*     */   {
/* 211 */     if (this._memberMethods == null) {
/* 212 */       this._memberMethods = _getMethods(false);
/*     */     }
/* 214 */     if (this._memberMethods.length == 0) {
/* 215 */       return Collections.emptyList();
/*     */     }
/* 217 */     return Arrays.asList(this._memberMethods);
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized List<RawMethod> getStaticMethods()
/*     */   {
/* 223 */     if (this._staticMethods == null) {
/* 224 */       this._staticMethods = _getMethods(true);
/*     */     }
/* 226 */     if (this._staticMethods.length == 0) {
/* 227 */       return Collections.emptyList();
/*     */     }
/* 229 */     return Arrays.asList(this._staticMethods);
/*     */   }
/*     */   
/*     */ 
/*     */   public List<RawConstructor> getConstructors()
/*     */   {
/* 235 */     if (this._constructors == null) {
/* 236 */       this._constructors = _getConstructors();
/*     */     }
/* 238 */     if (this._constructors.length == 0) {
/* 239 */       return Collections.emptyList();
/*     */     }
/* 241 */     return Arrays.asList(this._constructors);
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
/* 252 */     return _appendClassSignature(sb);
/*     */   }
/*     */   
/*     */   public StringBuilder appendErasedSignature(StringBuilder sb)
/*     */   {
/* 257 */     return _appendErasedClassSignature(sb);
/*     */   }
/*     */   
/*     */   public StringBuilder appendBriefDescription(StringBuilder sb)
/*     */   {
/* 262 */     return _appendClassDescription(sb);
/*     */   }
/*     */   
/*     */ 
/*     */   public StringBuilder appendFullDescription(StringBuilder sb)
/*     */   {
/* 268 */     sb = _appendClassDescription(sb);
/* 269 */     if (this._superClass != null) {
/* 270 */       sb.append(" extends ");
/* 271 */       sb = this._superClass.appendBriefDescription(sb);
/*     */     }
/*     */     
/* 274 */     int count = this._superInterfaces.length;
/* 275 */     if (count > 0) {
/* 276 */       sb.append(" implements ");
/* 277 */       for (int i = 0; i < count; i++) {
/* 278 */         if (i > 0) {
/* 279 */           sb.append(",");
/*     */         }
/* 281 */         sb = this._superInterfaces[i].appendBriefDescription(sb);
/*     */       }
/*     */     }
/* 284 */     return sb;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\types\ResolvedObjectType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */