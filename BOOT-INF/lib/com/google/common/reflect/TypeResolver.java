/*     */ package com.google.common.reflect;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.base.Objects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableMap.Builder;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.annotation.Nullable;
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
/*     */ @Beta
/*     */ public final class TypeResolver
/*     */ {
/*     */   private final TypeTable typeTable;
/*     */   
/*     */   public TypeResolver()
/*     */   {
/*  59 */     this.typeTable = new TypeTable();
/*     */   }
/*     */   
/*     */   private TypeResolver(TypeTable typeTable) {
/*  63 */     this.typeTable = typeTable;
/*     */   }
/*     */   
/*     */   static TypeResolver accordingTo(Type type) {
/*  67 */     return new TypeResolver().where(TypeMappingIntrospector.getTypeMappings(type));
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
/*     */   public TypeResolver where(Type formal, Type actual)
/*     */   {
/*  90 */     Map<TypeVariableKey, Type> mappings = Maps.newHashMap();
/*  91 */     populateTypeMappings(mappings, (Type)Preconditions.checkNotNull(formal), (Type)Preconditions.checkNotNull(actual));
/*  92 */     return where(mappings);
/*     */   }
/*     */   
/*     */   TypeResolver where(Map<TypeVariableKey, ? extends Type> mappings)
/*     */   {
/*  97 */     return new TypeResolver(this.typeTable.where(mappings));
/*     */   }
/*     */   
/*     */   private static void populateTypeMappings(Map<TypeVariableKey, Type> mappings, Type from, final Type to)
/*     */   {
/* 102 */     if (from.equals(to)) {
/* 103 */       return;
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 183 */     new TypeVisitor()
/*     */     {
/*     */       void visitTypeVariable(TypeVariable<?> typeVariable)
/*     */       {
/* 108 */         this.val$mappings.put(new TypeResolver.TypeVariableKey(typeVariable), to);
/*     */       }
/*     */       
/*     */       void visitWildcardType(WildcardType fromWildcardType)
/*     */       {
/* 113 */         if (!(to instanceof WildcardType)) {
/* 114 */           return;
/*     */         }
/* 116 */         WildcardType toWildcardType = (WildcardType)to;
/* 117 */         Type[] fromUpperBounds = fromWildcardType.getUpperBounds();
/* 118 */         Type[] toUpperBounds = toWildcardType.getUpperBounds();
/* 119 */         Type[] fromLowerBounds = fromWildcardType.getLowerBounds();
/* 120 */         Type[] toLowerBounds = toWildcardType.getLowerBounds();
/* 121 */         Preconditions.checkArgument((fromUpperBounds.length == toUpperBounds.length) && (fromLowerBounds.length == toLowerBounds.length), "Incompatible type: %s vs. %s", fromWildcardType, to);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 127 */         for (int i = 0; i < fromUpperBounds.length; i++) {
/* 128 */           TypeResolver.populateTypeMappings(this.val$mappings, fromUpperBounds[i], toUpperBounds[i]);
/*     */         }
/* 130 */         for (int i = 0; i < fromLowerBounds.length; i++) {
/* 131 */           TypeResolver.populateTypeMappings(this.val$mappings, fromLowerBounds[i], toLowerBounds[i]);
/*     */         }
/*     */       }
/*     */       
/*     */       void visitParameterizedType(ParameterizedType fromParameterizedType)
/*     */       {
/* 137 */         if ((to instanceof WildcardType)) {
/* 138 */           return;
/*     */         }
/* 140 */         ParameterizedType toParameterizedType = (ParameterizedType)TypeResolver.expectArgument(ParameterizedType.class, to);
/* 141 */         if ((fromParameterizedType.getOwnerType() != null) && 
/* 142 */           (toParameterizedType.getOwnerType() != null)) {
/* 143 */           TypeResolver.populateTypeMappings(this.val$mappings, fromParameterizedType
/* 144 */             .getOwnerType(), toParameterizedType.getOwnerType());
/*     */         }
/* 146 */         Preconditions.checkArgument(
/* 147 */           fromParameterizedType.getRawType().equals(toParameterizedType.getRawType()), "Inconsistent raw type: %s vs. %s", fromParameterizedType, to);
/*     */         
/*     */ 
/*     */ 
/* 151 */         Type[] fromArgs = fromParameterizedType.getActualTypeArguments();
/* 152 */         Type[] toArgs = toParameterizedType.getActualTypeArguments();
/* 153 */         Preconditions.checkArgument(fromArgs.length == toArgs.length, "%s not compatible with %s", fromParameterizedType, toParameterizedType);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 158 */         for (int i = 0; i < fromArgs.length; i++) {
/* 159 */           TypeResolver.populateTypeMappings(this.val$mappings, fromArgs[i], toArgs[i]);
/*     */         }
/*     */       }
/*     */       
/*     */       void visitGenericArrayType(GenericArrayType fromArrayType)
/*     */       {
/* 165 */         if ((to instanceof WildcardType)) {
/* 166 */           return;
/*     */         }
/* 168 */         Type componentType = Types.getComponentType(to);
/* 169 */         Preconditions.checkArgument(componentType != null, "%s is not an array type.", to);
/* 170 */         TypeResolver.populateTypeMappings(this.val$mappings, fromArrayType.getGenericComponentType(), componentType);
/*     */       }
/*     */       
/*     */       void visitClass(Class<?> fromClass)
/*     */       {
/* 175 */         if ((to instanceof WildcardType)) {
/* 176 */           return;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 181 */         throw new IllegalArgumentException("No type mapping from " + fromClass + " to " + to); } }
/*     */     
/* 183 */       .visit(new Type[] { from });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Type resolveType(Type type)
/*     */   {
/* 191 */     Preconditions.checkNotNull(type);
/* 192 */     if ((type instanceof TypeVariable))
/* 193 */       return this.typeTable.resolve((TypeVariable)type);
/* 194 */     if ((type instanceof ParameterizedType))
/* 195 */       return resolveParameterizedType((ParameterizedType)type);
/* 196 */     if ((type instanceof GenericArrayType))
/* 197 */       return resolveGenericArrayType((GenericArrayType)type);
/* 198 */     if ((type instanceof WildcardType)) {
/* 199 */       return resolveWildcardType((WildcardType)type);
/*     */     }
/*     */     
/* 202 */     return type;
/*     */   }
/*     */   
/*     */   private Type[] resolveTypes(Type[] types)
/*     */   {
/* 207 */     Type[] result = new Type[types.length];
/* 208 */     for (int i = 0; i < types.length; i++) {
/* 209 */       result[i] = resolveType(types[i]);
/*     */     }
/* 211 */     return result;
/*     */   }
/*     */   
/*     */   private WildcardType resolveWildcardType(WildcardType type) {
/* 215 */     Type[] lowerBounds = type.getLowerBounds();
/* 216 */     Type[] upperBounds = type.getUpperBounds();
/* 217 */     return new Types.WildcardTypeImpl(resolveTypes(lowerBounds), resolveTypes(upperBounds));
/*     */   }
/*     */   
/*     */   private Type resolveGenericArrayType(GenericArrayType type) {
/* 221 */     Type componentType = type.getGenericComponentType();
/* 222 */     Type resolvedComponentType = resolveType(componentType);
/* 223 */     return Types.newArrayType(resolvedComponentType);
/*     */   }
/*     */   
/*     */   private ParameterizedType resolveParameterizedType(ParameterizedType type) {
/* 227 */     Type owner = type.getOwnerType();
/* 228 */     Type resolvedOwner = owner == null ? null : resolveType(owner);
/* 229 */     Type resolvedRawType = resolveType(type.getRawType());
/*     */     
/* 231 */     Type[] args = type.getActualTypeArguments();
/* 232 */     Type[] resolvedArgs = resolveTypes(args);
/* 233 */     return Types.newParameterizedTypeWithOwner(resolvedOwner, (Class)resolvedRawType, resolvedArgs);
/*     */   }
/*     */   
/*     */   private static <T> T expectArgument(Class<T> type, Object arg)
/*     */   {
/*     */     try {
/* 239 */       return (T)type.cast(arg);
/*     */     } catch (ClassCastException e) {
/* 241 */       throw new IllegalArgumentException(arg + " is not a " + type.getSimpleName());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class TypeTable
/*     */   {
/*     */     private final ImmutableMap<TypeResolver.TypeVariableKey, Type> map;
/*     */     
/*     */     TypeTable() {
/* 250 */       this.map = ImmutableMap.of();
/*     */     }
/*     */     
/*     */     private TypeTable(ImmutableMap<TypeResolver.TypeVariableKey, Type> map) {
/* 254 */       this.map = map;
/*     */     }
/*     */     
/*     */     final TypeTable where(Map<TypeResolver.TypeVariableKey, ? extends Type> mappings)
/*     */     {
/* 259 */       ImmutableMap.Builder<TypeResolver.TypeVariableKey, Type> builder = ImmutableMap.builder();
/* 260 */       builder.putAll(this.map);
/* 261 */       for (Map.Entry<TypeResolver.TypeVariableKey, ? extends Type> mapping : mappings.entrySet()) {
/* 262 */         TypeResolver.TypeVariableKey variable = (TypeResolver.TypeVariableKey)mapping.getKey();
/* 263 */         Type type = (Type)mapping.getValue();
/* 264 */         Preconditions.checkArgument(!variable.equalsType(type), "Type variable %s bound to itself", variable);
/* 265 */         builder.put(variable, type);
/*     */       }
/* 267 */       return new TypeTable(builder.build());
/*     */     }
/*     */     
/*     */     final Type resolve(final TypeVariable<?> var) {
/* 271 */       final TypeTable unguarded = this;
/* 272 */       TypeTable guarded = new TypeTable()
/*     */       {
/*     */         public Type resolveInternal(TypeVariable<?> intermediateVar, TypeResolver.TypeTable forDependent)
/*     */         {
/* 276 */           if (intermediateVar.getGenericDeclaration().equals(var.getGenericDeclaration())) {
/* 277 */             return intermediateVar;
/*     */           }
/* 279 */           return unguarded.resolveInternal(intermediateVar, forDependent);
/*     */         }
/* 281 */       };
/* 282 */       return resolveInternal(var, guarded);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     Type resolveInternal(TypeVariable<?> var, TypeTable forDependants)
/*     */     {
/* 294 */       Type type = (Type)this.map.get(new TypeResolver.TypeVariableKey(var));
/* 295 */       if (type == null) {
/* 296 */         Type[] bounds = var.getBounds();
/* 297 */         if (bounds.length == 0) {
/* 298 */           return var;
/*     */         }
/* 300 */         Type[] resolvedBounds = new TypeResolver(forDependants, null).resolveTypes(bounds);
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
/* 329 */         if ((Types.NativeTypeVariableEquals.NATIVE_TYPE_VARIABLE_ONLY) && 
/* 330 */           (Arrays.equals(bounds, resolvedBounds))) {
/* 331 */           return var;
/*     */         }
/* 333 */         return Types.newArtificialTypeVariable(var
/* 334 */           .getGenericDeclaration(), var.getName(), resolvedBounds);
/*     */       }
/*     */       
/* 337 */       return new TypeResolver(forDependants, null).resolveType(type);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class TypeMappingIntrospector extends TypeVisitor
/*     */   {
/* 343 */     private static final TypeResolver.WildcardCapturer wildcardCapturer = new TypeResolver.WildcardCapturer();
/*     */     
/* 345 */     private final Map<TypeResolver.TypeVariableKey, Type> mappings = Maps.newHashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     static ImmutableMap<TypeResolver.TypeVariableKey, Type> getTypeMappings(Type contextType)
/*     */     {
/* 352 */       TypeMappingIntrospector introspector = new TypeMappingIntrospector();
/* 353 */       introspector.visit(new Type[] { wildcardCapturer.capture(contextType) });
/* 354 */       return ImmutableMap.copyOf(introspector.mappings);
/*     */     }
/*     */     
/*     */     void visitClass(Class<?> clazz)
/*     */     {
/* 359 */       visit(new Type[] { clazz.getGenericSuperclass() });
/* 360 */       visit(clazz.getGenericInterfaces());
/*     */     }
/*     */     
/*     */     void visitParameterizedType(ParameterizedType parameterizedType)
/*     */     {
/* 365 */       Class<?> rawClass = (Class)parameterizedType.getRawType();
/* 366 */       TypeVariable<?>[] vars = rawClass.getTypeParameters();
/* 367 */       Type[] typeArgs = parameterizedType.getActualTypeArguments();
/* 368 */       Preconditions.checkState(vars.length == typeArgs.length);
/* 369 */       for (int i = 0; i < vars.length; i++) {
/* 370 */         map(new TypeResolver.TypeVariableKey(vars[i]), typeArgs[i]);
/*     */       }
/* 372 */       visit(new Type[] { rawClass });
/* 373 */       visit(new Type[] { parameterizedType.getOwnerType() });
/*     */     }
/*     */     
/*     */     void visitTypeVariable(TypeVariable<?> t)
/*     */     {
/* 378 */       visit(t.getBounds());
/*     */     }
/*     */     
/*     */     void visitWildcardType(WildcardType t)
/*     */     {
/* 383 */       visit(t.getUpperBounds());
/*     */     }
/*     */     
/*     */     private void map(TypeResolver.TypeVariableKey var, Type arg) {
/* 387 */       if (this.mappings.containsKey(var))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 393 */         return;
/*     */       }
/*     */       
/* 396 */       for (Type t = arg; t != null; t = (Type)this.mappings.get(TypeResolver.TypeVariableKey.forLookup(t))) {
/* 397 */         if (var.equalsType(t))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 402 */           for (Type x = arg; x != null; x = (Type)this.mappings.remove(TypeResolver.TypeVariableKey.forLookup(x))) {}
/* 403 */           return;
/*     */         }
/*     */       }
/* 406 */       this.mappings.put(var, arg);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class WildcardCapturer
/*     */   {
/*     */     private final AtomicInteger id;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     WildcardCapturer()
/*     */     {
/* 422 */       this(new AtomicInteger());
/*     */     }
/*     */     
/*     */     private WildcardCapturer(AtomicInteger id) {
/* 426 */       this.id = id;
/*     */     }
/*     */     
/*     */     final Type capture(Type type) {
/* 430 */       Preconditions.checkNotNull(type);
/* 431 */       if ((type instanceof Class)) {
/* 432 */         return type;
/*     */       }
/* 434 */       if ((type instanceof TypeVariable)) {
/* 435 */         return type;
/*     */       }
/* 437 */       if ((type instanceof GenericArrayType)) {
/* 438 */         GenericArrayType arrayType = (GenericArrayType)type;
/* 439 */         return Types.newArrayType(
/* 440 */           notForTypeVariable().capture(arrayType.getGenericComponentType()));
/*     */       }
/* 442 */       if ((type instanceof ParameterizedType)) {
/* 443 */         ParameterizedType parameterizedType = (ParameterizedType)type;
/* 444 */         Class<?> rawType = (Class)parameterizedType.getRawType();
/* 445 */         TypeVariable<?>[] typeVars = rawType.getTypeParameters();
/* 446 */         Type[] typeArgs = parameterizedType.getActualTypeArguments();
/* 447 */         for (int i = 0; i < typeArgs.length; i++) {
/* 448 */           typeArgs[i] = forTypeVariable(typeVars[i]).capture(typeArgs[i]);
/*     */         }
/* 450 */         return Types.newParameterizedTypeWithOwner(
/* 451 */           notForTypeVariable().captureNullable(parameterizedType.getOwnerType()), rawType, typeArgs);
/*     */       }
/*     */       
/* 454 */       if ((type instanceof WildcardType)) {
/* 455 */         WildcardType wildcardType = (WildcardType)type;
/* 456 */         Type[] lowerBounds = wildcardType.getLowerBounds();
/* 457 */         if (lowerBounds.length == 0) {
/* 458 */           return captureAsTypeVariable(wildcardType.getUpperBounds());
/*     */         }
/*     */         
/* 461 */         return type;
/*     */       }
/*     */       
/* 464 */       throw new AssertionError("must have been one of the known types");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     TypeVariable<?> captureAsTypeVariable(Type[] upperBounds)
/*     */     {
/* 472 */       String name = "capture#" + this.id.incrementAndGet() + "-of ? extends " + Joiner.on('&').join(upperBounds);
/* 473 */       return Types.newArtificialTypeVariable(WildcardCapturer.class, name, upperBounds);
/*     */     }
/*     */     
/*     */     private WildcardCapturer forTypeVariable(final TypeVariable<?> typeParam) {
/* 477 */       new WildcardCapturer(this.id, typeParam) {
/*     */         TypeVariable<?> captureAsTypeVariable(Type[] upperBounds) {
/* 479 */           Set<Type> combined = new LinkedHashSet(Arrays.asList(upperBounds));
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 486 */           combined.addAll(Arrays.asList(typeParam.getBounds()));
/* 487 */           if (combined.size() > 1) {
/* 488 */             combined.remove(Object.class);
/*     */           }
/* 490 */           return super.captureAsTypeVariable((Type[])combined.toArray(new Type[0]));
/*     */         }
/*     */       };
/*     */     }
/*     */     
/*     */     private WildcardCapturer notForTypeVariable() {
/* 496 */       return new WildcardCapturer(this.id);
/*     */     }
/*     */     
/*     */     private Type captureNullable(@Nullable Type type) {
/* 500 */       if (type == null) {
/* 501 */         return null;
/*     */       }
/* 503 */       return capture(type);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final class TypeVariableKey
/*     */   {
/*     */     private final TypeVariable<?> var;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     TypeVariableKey(TypeVariable<?> var)
/*     */     {
/* 524 */       this.var = ((TypeVariable)Preconditions.checkNotNull(var));
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 529 */       return Objects.hashCode(new Object[] { this.var.getGenericDeclaration(), this.var.getName() });
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 534 */       if ((obj instanceof TypeVariableKey)) {
/* 535 */         TypeVariableKey that = (TypeVariableKey)obj;
/* 536 */         return equalsTypeVariable(that.var);
/*     */       }
/* 538 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */     public String toString()
/*     */     {
/* 544 */       return this.var.toString();
/*     */     }
/*     */     
/*     */     static TypeVariableKey forLookup(Type t)
/*     */     {
/* 549 */       if ((t instanceof TypeVariable)) {
/* 550 */         return new TypeVariableKey((TypeVariable)t);
/*     */       }
/* 552 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     boolean equalsType(Type type)
/*     */     {
/* 561 */       if ((type instanceof TypeVariable)) {
/* 562 */         return equalsTypeVariable((TypeVariable)type);
/*     */       }
/* 564 */       return false;
/*     */     }
/*     */     
/*     */     private boolean equalsTypeVariable(TypeVariable<?> that)
/*     */     {
/* 569 */       return (this.var.getGenericDeclaration().equals(that.getGenericDeclaration())) && 
/* 570 */         (this.var.getName().equals(that.getName()));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\common\reflect\TypeResolver.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */