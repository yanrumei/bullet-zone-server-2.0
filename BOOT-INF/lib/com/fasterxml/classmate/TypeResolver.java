/*     */ package com.fasterxml.classmate;
/*     */ 
/*     */ import com.fasterxml.classmate.types.ResolvedArrayType;
/*     */ import com.fasterxml.classmate.types.ResolvedInterfaceType;
/*     */ import com.fasterxml.classmate.types.ResolvedObjectType;
/*     */ import com.fasterxml.classmate.types.ResolvedPrimitiveType;
/*     */ import com.fasterxml.classmate.types.ResolvedRecursiveType;
/*     */ import com.fasterxml.classmate.types.TypePlaceHolder;
/*     */ import com.fasterxml.classmate.util.ClassKey;
/*     */ import com.fasterxml.classmate.util.ClassStack;
/*     */ import com.fasterxml.classmate.util.ResolvedTypeCache;
/*     */ import com.fasterxml.classmate.util.ResolvedTypeCache.Key;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ 
/*     */ public class TypeResolver
/*     */   implements Serializable
/*     */ {
/*  26 */   private static final ResolvedType[] NO_TYPES = new ResolvedType[0];
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
/*  39 */   private static final ResolvedObjectType sJavaLangObject = ResolvedObjectType.create(Object.class, null, null, null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  49 */   protected static final HashMap<ClassKey, ResolvedType> _primitiveTypes = new HashMap(16);
/*  50 */   static { for (ResolvedPrimitiveType type : ResolvedPrimitiveType.all()) {
/*  51 */       _primitiveTypes.put(new ClassKey(type.getErasedType()), type);
/*     */     }
/*     */     
/*  54 */     _primitiveTypes.put(new ClassKey(Void.TYPE), ResolvedPrimitiveType.voidType());
/*     */     
/*  56 */     _primitiveTypes.put(new ClassKey(Object.class), sJavaLangObject);
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
/*  72 */   protected final ResolvedTypeCache _resolvedTypes = new ResolvedTypeCache(200);
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
/*     */   public ResolvedType resolve(Type type, Type... typeParameters)
/*     */   {
/* 105 */     boolean noParams = (typeParameters == null) || (typeParameters.length == 0);
/*     */     Class<?> rawBase;
/*     */     TypeBindings bindings;
/*     */     Class<?> rawBase;
/* 109 */     if ((type instanceof Class)) {
/* 110 */       TypeBindings bindings = TypeBindings.emptyBindings();
/* 111 */       if (noParams) {
/* 112 */         return _fromClass(null, (Class)type, bindings);
/*     */       }
/* 114 */       rawBase = (Class)type; } else { Class<?> rawBase;
/* 115 */       if ((type instanceof GenericType)) {
/* 116 */         TypeBindings bindings = TypeBindings.emptyBindings();
/* 117 */         if (noParams) {
/* 118 */           return _fromGenericType(null, (GenericType)type, bindings);
/*     */         }
/* 120 */         ResolvedType rt = _fromAny(null, type, bindings);
/* 121 */         rawBase = rt.getErasedType(); } else { Class<?> rawBase;
/* 122 */         if ((type instanceof ResolvedType)) {
/* 123 */           ResolvedType rt = (ResolvedType)type;
/* 124 */           if (noParams) {
/* 125 */             return rt;
/*     */           }
/* 127 */           TypeBindings bindings = rt.getTypeBindings();
/* 128 */           rawBase = rt.getErasedType();
/*     */         } else {
/* 130 */           bindings = TypeBindings.emptyBindings();
/* 131 */           if (noParams) {
/* 132 */             return resolve(bindings, type);
/*     */           }
/*     */           
/* 135 */           ResolvedType rt = _fromAny(null, type, bindings);
/* 136 */           rawBase = rt.getErasedType();
/*     */         }
/*     */       }
/*     */     }
/* 140 */     int len = typeParameters.length;
/* 141 */     ResolvedType[] resolvedParams = new ResolvedType[len];
/* 142 */     for (int i = 0; i < len; i++) {
/* 143 */       resolvedParams[i] = _fromAny(null, typeParameters[i], bindings);
/*     */     }
/* 145 */     return _fromClass(null, rawBase, TypeBindings.create(rawBase, resolvedParams));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResolvedArrayType arrayType(Type elementType)
/*     */   {
/* 153 */     ResolvedType resolvedElementType = resolve(TypeBindings.emptyBindings(), elementType);
/*     */     
/* 155 */     Object emptyArray = Array.newInstance(resolvedElementType.getErasedType(), 0);
/*     */     
/* 157 */     return new ResolvedArrayType(emptyArray.getClass(), TypeBindings.emptyBindings(), resolvedElementType);
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
/*     */   public ResolvedType resolve(TypeBindings typeBindings, Type jdkType)
/*     */   {
/* 174 */     return _fromAny(null, jdkType, typeBindings);
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
/*     */   public ResolvedType resolveSubtype(ResolvedType supertype, Class<?> subtype)
/*     */     throws IllegalArgumentException, UnsupportedOperationException
/*     */   {
/* 214 */     ResolvedType refType = supertype.getSelfReferencedType();
/* 215 */     if (refType != null) {
/* 216 */       supertype = refType;
/*     */     }
/*     */     
/* 219 */     Class<?> superclass = supertype.getErasedType();
/* 220 */     if (superclass == subtype) {
/* 221 */       return supertype;
/*     */     }
/*     */     
/* 224 */     if (!supertype.canCreateSubtypes()) {
/* 225 */       throw new UnsupportedOperationException("Can not subtype primitive or array types (type " + supertype.getFullDescription() + ")");
/*     */     }
/*     */     
/* 228 */     if (!superclass.isAssignableFrom(subtype)) {
/* 229 */       throw new IllegalArgumentException("Can not sub-class " + supertype.getBriefDescription() + " into " + subtype.getName());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 234 */     int paramCount = subtype.getTypeParameters().length;
/*     */     ResolvedType resolvedSubtype;
/*     */     TypePlaceHolder[] placeholders;
/* 237 */     ResolvedType resolvedSubtype; if (paramCount == 0) {
/* 238 */       TypePlaceHolder[] placeholders = null;
/*     */       
/*     */ 
/*     */ 
/* 242 */       resolvedSubtype = _fromClass(null, subtype, TypeBindings.emptyBindings());
/*     */     }
/*     */     else {
/* 245 */       placeholders = new TypePlaceHolder[paramCount];
/* 246 */       for (int i = 0; i < paramCount; i++) {
/* 247 */         placeholders[i] = new TypePlaceHolder(i);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 252 */       ResolvedType[] resolvedParams = new ResolvedType[paramCount];
/* 253 */       TypeBindings bindings = TypeBindings.emptyBindings();
/* 254 */       for (int i = 0; i < paramCount; i++) {
/* 255 */         resolvedParams[i] = _fromAny(null, placeholders[i], bindings);
/*     */       }
/* 257 */       resolvedSubtype = _fromClass(null, subtype, TypeBindings.create(subtype, resolvedParams));
/*     */     }
/*     */     
/* 260 */     ResolvedType resolvedSupertype = resolvedSubtype.findSupertype(superclass);
/* 261 */     if (resolvedSupertype == null) {
/* 262 */       throw new IllegalArgumentException("Internal error: unable to locate supertype (" + subtype.getName() + ") for type " + supertype.getBriefDescription());
/*     */     }
/*     */     
/* 265 */     _resolveTypePlaceholders(supertype, resolvedSupertype);
/*     */     
/* 267 */     if (paramCount == 0) {
/* 268 */       return resolvedSubtype;
/*     */     }
/*     */     
/* 271 */     ResolvedType[] typeParams = new ResolvedType[paramCount];
/* 272 */     for (int i = 0; i < paramCount; i++) {
/* 273 */       ResolvedType t = placeholders[i].actualType();
/*     */       
/* 275 */       if (t == null) {
/* 276 */         throw new IllegalArgumentException("Failed to find type parameter #" + (i + 1) + "/" + paramCount + " for " + subtype.getName());
/*     */       }
/*     */       
/* 279 */       typeParams[i] = t;
/*     */     }
/* 281 */     return resolve(subtype, typeParams);
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
/*     */   public static boolean isSelfReference(ResolvedType type)
/*     */   {
/* 300 */     return type instanceof ResolvedRecursiveType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ResolvedType _fromAny(ClassStack context, Type mainType, TypeBindings typeBindings)
/*     */   {
/* 311 */     if ((mainType instanceof Class)) {
/* 312 */       return _fromClass(context, (Class)mainType, typeBindings);
/*     */     }
/* 314 */     if ((mainType instanceof ParameterizedType)) {
/* 315 */       return _fromParamType(context, (ParameterizedType)mainType, typeBindings);
/*     */     }
/* 317 */     if ((mainType instanceof ResolvedType)) {
/* 318 */       return (ResolvedType)mainType;
/*     */     }
/* 320 */     if ((mainType instanceof GenericType)) {
/* 321 */       return _fromGenericType(context, (GenericType)mainType, typeBindings);
/*     */     }
/* 323 */     if ((mainType instanceof GenericArrayType)) {
/* 324 */       return _fromArrayType(context, (GenericArrayType)mainType, typeBindings);
/*     */     }
/* 326 */     if ((mainType instanceof TypeVariable)) {
/* 327 */       return _fromVariable(context, (TypeVariable)mainType, typeBindings);
/*     */     }
/* 329 */     if ((mainType instanceof WildcardType)) {
/* 330 */       return _fromWildcard(context, (WildcardType)mainType, typeBindings);
/*     */     }
/*     */     
/* 333 */     throw new IllegalArgumentException("Unrecognized type class: " + mainType.getClass().getName());
/*     */   }
/*     */   
/*     */ 
/*     */   private ResolvedType _fromClass(ClassStack context, Class<?> rawType, TypeBindings typeBindings)
/*     */   {
/* 339 */     ResolvedType type = (ResolvedType)_primitiveTypes.get(new ClassKey(rawType));
/* 340 */     if (type != null) {
/* 341 */       return type;
/*     */     }
/*     */     
/* 344 */     if (context == null) {
/* 345 */       context = new ClassStack(rawType);
/*     */     } else {
/* 347 */       ClassStack prev = context.find(rawType);
/* 348 */       if (prev != null)
/*     */       {
/* 350 */         ResolvedRecursiveType selfRef = new ResolvedRecursiveType(rawType, typeBindings);
/* 351 */         prev.addSelfReference(selfRef);
/* 352 */         return selfRef;
/*     */       }
/*     */       
/* 355 */       context = context.child(rawType);
/*     */     }
/*     */     
/*     */ 
/* 359 */     ResolvedType[] typeParameters = typeBindings.typeParameterArray();
/* 360 */     ResolvedTypeCache.Key key = this._resolvedTypes.key(rawType, typeParameters);
/*     */     
/*     */ 
/* 363 */     if (key == null) {
/* 364 */       type = _constructType(context, rawType, typeBindings);
/*     */     } else {
/* 366 */       type = this._resolvedTypes.find(key);
/* 367 */       if (type == null) {
/* 368 */         type = _constructType(context, rawType, typeBindings);
/* 369 */         this._resolvedTypes.put(key, type);
/*     */       }
/*     */     }
/* 372 */     context.resolveSelfReferences(type);
/* 373 */     return type;
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
/*     */   private ResolvedType _fromGenericType(ClassStack context, GenericType<?> generic, TypeBindings typeBindings)
/*     */   {
/* 387 */     ResolvedType type = _fromClass(context, generic.getClass(), typeBindings);
/* 388 */     ResolvedType genType = type.findSupertype(GenericType.class);
/* 389 */     if (genType == null) {
/* 390 */       throw new IllegalArgumentException("Unparameterized GenericType instance (" + generic.getClass().getName() + ")");
/*     */     }
/* 392 */     TypeBindings b = genType.getTypeBindings();
/* 393 */     ResolvedType[] params = b.typeParameterArray();
/* 394 */     if (params.length == 0) {
/* 395 */       throw new IllegalArgumentException("Unparameterized GenericType instance (" + generic.getClass().getName() + ")");
/*     */     }
/* 397 */     return params[0];
/*     */   }
/*     */   
/*     */ 
/*     */   private ResolvedType _constructType(ClassStack context, Class<?> rawType, TypeBindings typeBindings)
/*     */   {
/* 403 */     if (rawType.isArray()) {
/* 404 */       ResolvedType elementType = _fromAny(context, rawType.getComponentType(), typeBindings);
/* 405 */       return new ResolvedArrayType(rawType, typeBindings, elementType);
/*     */     }
/*     */     
/*     */ 
/* 409 */     if ((!typeBindings.isEmpty()) && (rawType.getTypeParameters().length == 0)) {
/* 410 */       typeBindings = TypeBindings.emptyBindings();
/*     */     }
/*     */     
/* 413 */     if (rawType.isInterface()) {
/* 414 */       return new ResolvedInterfaceType(rawType, typeBindings, _resolveSuperInterfaces(context, rawType, typeBindings));
/*     */     }
/*     */     
/*     */ 
/* 418 */     return new ResolvedObjectType(rawType, typeBindings, _resolveSuperClass(context, rawType, typeBindings), _resolveSuperInterfaces(context, rawType, typeBindings));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private ResolvedType[] _resolveSuperInterfaces(ClassStack context, Class<?> rawType, TypeBindings typeBindings)
/*     */   {
/* 425 */     Type[] types = rawType.getGenericInterfaces();
/* 426 */     if ((types == null) || (types.length == 0)) {
/* 427 */       return NO_TYPES;
/*     */     }
/* 429 */     int len = types.length;
/* 430 */     ResolvedType[] resolved = new ResolvedType[len];
/* 431 */     for (int i = 0; i < len; i++) {
/* 432 */       resolved[i] = _fromAny(context, types[i], typeBindings);
/*     */     }
/* 434 */     return resolved;
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
/*     */   private ResolvedType _resolveSuperClass(ClassStack context, Class<?> rawType, TypeBindings typeBindings)
/*     */   {
/* 447 */     Type parent = rawType.getGenericSuperclass();
/* 448 */     if (parent == null) {
/* 449 */       return null;
/*     */     }
/* 451 */     return _fromAny(context, parent, typeBindings);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ResolvedType _fromParamType(ClassStack context, ParameterizedType ptype, TypeBindings parentBindings)
/*     */   {
/* 460 */     Class<?> rawType = (Class)ptype.getRawType();
/* 461 */     Type[] params = ptype.getActualTypeArguments();
/* 462 */     int len = params.length;
/* 463 */     ResolvedType[] types = new ResolvedType[len];
/*     */     
/* 465 */     for (int i = 0; i < len; i++) {
/* 466 */       types[i] = _fromAny(context, params[i], parentBindings);
/*     */     }
/*     */     
/* 469 */     TypeBindings newBindings = TypeBindings.create(rawType, types);
/* 470 */     return _fromClass(context, rawType, newBindings);
/*     */   }
/*     */   
/*     */   private ResolvedType _fromArrayType(ClassStack context, GenericArrayType arrayType, TypeBindings typeBindings)
/*     */   {
/* 475 */     ResolvedType elementType = _fromAny(context, arrayType.getGenericComponentType(), typeBindings);
/*     */     
/* 477 */     Object emptyArray = Array.newInstance(elementType.getErasedType(), 0);
/* 478 */     return new ResolvedArrayType(emptyArray.getClass(), typeBindings, elementType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ResolvedType _fromWildcard(ClassStack context, WildcardType wildType, TypeBindings typeBindings)
/*     */   {
/* 488 */     return _fromAny(context, wildType.getUpperBounds()[0], typeBindings);
/*     */   }
/*     */   
/*     */ 
/*     */   private ResolvedType _fromVariable(ClassStack context, TypeVariable<?> variable, TypeBindings typeBindings)
/*     */   {
/* 494 */     String name = variable.getName();
/* 495 */     ResolvedType type = typeBindings.findBoundType(name);
/*     */     
/* 497 */     if (type != null) {
/* 498 */       return type;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 507 */     if (typeBindings.hasUnbound(name)) {
/* 508 */       return sJavaLangObject;
/*     */     }
/* 510 */     typeBindings = typeBindings.withUnboundVariable(name);
/*     */     
/* 512 */     Type[] bounds = variable.getBounds();
/* 513 */     return _fromAny(context, bounds[0], typeBindings);
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
/*     */   private void _resolveTypePlaceholders(ResolvedType sourceType, ResolvedType actualType)
/*     */     throws IllegalArgumentException
/*     */   {
/* 532 */     List<ResolvedType> expectedTypes = sourceType.getTypeParameters();
/* 533 */     List<ResolvedType> actualTypes = actualType.getTypeParameters();
/* 534 */     int i = 0; for (int len = expectedTypes.size(); i < len; i++) {
/* 535 */       ResolvedType exp = (ResolvedType)expectedTypes.get(i);
/* 536 */       ResolvedType act = (ResolvedType)actualTypes.get(i);
/* 537 */       if (!_verifyAndResolve(exp, act)) {
/* 538 */         throw new IllegalArgumentException("Type parameter #" + (i + 1) + "/" + len + " differs; expected " + exp.getBriefDescription() + ", got " + act.getBriefDescription());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean _verifyAndResolve(ResolvedType exp, ResolvedType act)
/*     */   {
/* 547 */     if ((act instanceof TypePlaceHolder)) {
/* 548 */       ((TypePlaceHolder)act).actualType(exp);
/* 549 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 553 */     if (exp.getErasedType() != act.getErasedType()) {
/* 554 */       return false;
/*     */     }
/*     */     
/* 557 */     List<ResolvedType> expectedTypes = exp.getTypeParameters();
/* 558 */     List<ResolvedType> actualTypes = act.getTypeParameters();
/* 559 */     int i = 0; for (int len = expectedTypes.size(); i < len; i++) {
/* 560 */       ResolvedType exp2 = (ResolvedType)expectedTypes.get(i);
/* 561 */       ResolvedType act2 = (ResolvedType)actualTypes.get(i);
/* 562 */       if (!_verifyAndResolve(exp2, act2)) {
/* 563 */         return false;
/*     */       }
/*     */     }
/* 566 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\TypeResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */