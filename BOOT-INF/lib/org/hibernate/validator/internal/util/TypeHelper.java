/*     */ package org.hibernate.validator.internal.util;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.MalformedParameterizedTypeException;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.validation.ConstraintValidator;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class TypeHelper
/*     */ {
/*     */   private static final Map<Class<?>, Set<Class<?>>> SUBTYPES_BY_PRIMITIVE;
/*     */   private static final int VALIDATOR_TYPE_INDEX = 1;
/*  52 */   private static final Log log = ;
/*     */   
/*     */   static {
/*  55 */     Map<Class<?>, Set<Class<?>>> subtypesByPrimitive = CollectionHelper.newHashMap();
/*     */     
/*  57 */     putPrimitiveSubtypes(subtypesByPrimitive, Void.TYPE, new Class[0]);
/*  58 */     putPrimitiveSubtypes(subtypesByPrimitive, Boolean.TYPE, new Class[0]);
/*  59 */     putPrimitiveSubtypes(subtypesByPrimitive, Byte.TYPE, new Class[0]);
/*  60 */     putPrimitiveSubtypes(subtypesByPrimitive, Character.TYPE, new Class[0]);
/*  61 */     putPrimitiveSubtypes(subtypesByPrimitive, Short.TYPE, new Class[] { Byte.TYPE });
/*  62 */     putPrimitiveSubtypes(subtypesByPrimitive, Integer.TYPE, new Class[] { Character.TYPE, Short.TYPE });
/*  63 */     putPrimitiveSubtypes(subtypesByPrimitive, Long.TYPE, new Class[] { Integer.TYPE });
/*  64 */     putPrimitiveSubtypes(subtypesByPrimitive, Float.TYPE, new Class[] { Long.TYPE });
/*  65 */     putPrimitiveSubtypes(subtypesByPrimitive, Double.TYPE, new Class[] { Float.TYPE });
/*     */     
/*  67 */     SUBTYPES_BY_PRIMITIVE = Collections.unmodifiableMap(subtypesByPrimitive);
/*     */   }
/*     */   
/*     */   private TypeHelper() {
/*  71 */     throw new AssertionError();
/*     */   }
/*     */   
/*     */   public static boolean isAssignable(Type supertype, Type type) {
/*  75 */     Contracts.assertNotNull(supertype, "supertype");
/*  76 */     Contracts.assertNotNull(type, "type");
/*     */     
/*  78 */     if (supertype.equals(type)) {
/*  79 */       return true;
/*     */     }
/*     */     
/*  82 */     if ((supertype instanceof Class)) {
/*  83 */       if ((type instanceof Class)) {
/*  84 */         return isClassAssignable((Class)supertype, (Class)type);
/*     */       }
/*     */       
/*  87 */       if ((type instanceof ParameterizedType)) {
/*  88 */         return isAssignable(supertype, ((ParameterizedType)type).getRawType());
/*     */       }
/*     */       
/*  91 */       if ((type instanceof TypeVariable)) {
/*  92 */         return isTypeVariableAssignable(supertype, (TypeVariable)type);
/*     */       }
/*     */       
/*  95 */       if ((type instanceof GenericArrayType)) {
/*  96 */         if (((Class)supertype).isArray()) {
/*  97 */           return isAssignable(getComponentType(supertype), getComponentType(type));
/*     */         }
/*     */         
/* 100 */         return isArraySupertype((Class)supertype);
/*     */       }
/*     */       
/* 103 */       if ((type instanceof WildcardType)) {
/* 104 */         return isClassAssignableToWildcardType((Class)supertype, (WildcardType)type);
/*     */       }
/*     */       
/* 107 */       return false;
/*     */     }
/*     */     
/* 110 */     if ((supertype instanceof ParameterizedType)) {
/* 111 */       if ((type instanceof Class)) {
/* 112 */         return isSuperAssignable(supertype, type);
/*     */       }
/*     */       
/* 115 */       if ((type instanceof ParameterizedType)) {
/* 116 */         return isParameterizedTypeAssignable((ParameterizedType)supertype, (ParameterizedType)type);
/*     */       }
/*     */       
/* 119 */       return false;
/*     */     }
/*     */     
/* 122 */     if ((type instanceof TypeVariable)) {
/* 123 */       return isTypeVariableAssignable(supertype, (TypeVariable)type);
/*     */     }
/*     */     
/* 126 */     if ((supertype instanceof GenericArrayType)) {
/* 127 */       if (isArray(type)) {
/* 128 */         return isAssignable(getComponentType(supertype), getComponentType(type));
/*     */       }
/*     */       
/* 131 */       return false;
/*     */     }
/*     */     
/* 134 */     if ((supertype instanceof WildcardType)) {
/* 135 */       return isWildcardTypeAssignable((WildcardType)supertype, type);
/*     */     }
/*     */     
/* 138 */     return false;
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
/*     */   public static Type getErasedType(Type type)
/*     */   {
/* 152 */     if ((type instanceof ParameterizedType)) {
/* 153 */       Type rawType = ((ParameterizedType)type).getRawType();
/*     */       
/* 155 */       return getErasedType(rawType);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 161 */     if (isArray(type)) {
/* 162 */       Type componentType = getComponentType(type);
/* 163 */       Type erasedComponentType = getErasedType(componentType);
/*     */       
/* 165 */       return getArrayType(erasedComponentType);
/*     */     }
/*     */     
/*     */ 
/* 169 */     if ((type instanceof TypeVariable)) {
/* 170 */       Type[] bounds = ((TypeVariable)type).getBounds();
/*     */       
/* 172 */       return getErasedType(bounds[0]);
/*     */     }
/*     */     
/*     */ 
/* 176 */     return type;
/*     */   }
/*     */   
/*     */   private static Class<?> getErasedReferenceType(Type type) {
/* 180 */     Contracts.assertTrue(isReferenceType(type), "type is not a reference type: " + type);
/* 181 */     return (Class)getErasedType(type);
/*     */   }
/*     */   
/*     */   public static boolean isArray(Type type) {
/* 185 */     return (((type instanceof Class)) && (((Class)type).isArray())) || ((type instanceof GenericArrayType));
/*     */   }
/*     */   
/*     */   public static Type getComponentType(Type type)
/*     */   {
/* 190 */     if ((type instanceof Class)) {
/* 191 */       Class<?> klass = (Class)type;
/*     */       
/* 193 */       return klass.isArray() ? klass.getComponentType() : null;
/*     */     }
/*     */     
/* 196 */     if ((type instanceof GenericArrayType)) {
/* 197 */       return ((GenericArrayType)type).getGenericComponentType();
/*     */     }
/*     */     
/* 200 */     return null;
/*     */   }
/*     */   
/*     */   private static Type getArrayType(Type componentType) {
/* 204 */     Contracts.assertNotNull(componentType, "componentType");
/*     */     
/* 206 */     if ((componentType instanceof Class)) {
/* 207 */       return Array.newInstance((Class)componentType, 0).getClass();
/*     */     }
/*     */     
/* 210 */     return genericArrayType(componentType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static GenericArrayType genericArrayType(Type componentType)
/*     */   {
/* 221 */     new GenericArrayType()
/*     */     {
/*     */       public Type getGenericComponentType()
/*     */       {
/* 225 */         return this.val$componentType;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public static boolean isInstance(Type type, Object object) {
/* 231 */     return getErasedReferenceType(type).isInstance(object);
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
/*     */   public static ParameterizedType parameterizedType(final Class<?> rawType, Type... actualTypeArguments)
/*     */   {
/* 245 */     new ParameterizedType()
/*     */     {
/*     */       public Type[] getActualTypeArguments() {
/* 248 */         return this.val$actualTypeArguments;
/*     */       }
/*     */       
/*     */       public Type getRawType()
/*     */       {
/* 253 */         return rawType;
/*     */       }
/*     */       
/*     */       public Type getOwnerType()
/*     */       {
/* 258 */         return null;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   private static Type getResolvedSuperclass(Type type) {
/* 264 */     Contracts.assertNotNull(type, "type");
/*     */     
/* 266 */     Class<?> rawType = getErasedReferenceType(type);
/* 267 */     Type supertype = rawType.getGenericSuperclass();
/*     */     
/* 269 */     if (supertype == null) {
/* 270 */       return null;
/*     */     }
/*     */     
/* 273 */     return resolveTypeVariables(supertype, type);
/*     */   }
/*     */   
/*     */   private static Type[] getResolvedInterfaces(Type type) {
/* 277 */     Contracts.assertNotNull(type, "type");
/*     */     
/* 279 */     Class<?> rawType = getErasedReferenceType(type);
/* 280 */     Type[] interfaces = rawType.getGenericInterfaces();
/* 281 */     Type[] resolvedInterfaces = new Type[interfaces.length];
/*     */     
/* 283 */     for (int i = 0; i < interfaces.length; i++) {
/* 284 */       resolvedInterfaces[i] = resolveTypeVariables(interfaces[i], type);
/*     */     }
/*     */     
/* 287 */     return resolvedInterfaces;
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
/*     */   public static <A extends Annotation> Map<Type, Class<? extends ConstraintValidator<A, ?>>> getValidatorsTypes(Class<A> annotationType, List<Class<? extends ConstraintValidator<A, ?>>> validators)
/*     */   {
/* 302 */     Map<Type, Class<? extends ConstraintValidator<A, ?>>> validatorsTypes = CollectionHelper.newHashMap();
/* 303 */     for (Class<? extends ConstraintValidator<A, ?>> validator : validators) {
/* 304 */       Type type = extractType(validator);
/*     */       
/* 306 */       if (validatorsTypes.containsKey(type)) {
/* 307 */         throw log.getMultipleValidatorsForSameTypeException(annotationType.getName(), type.toString());
/*     */       }
/*     */       
/* 310 */       validatorsTypes.put(type, validator);
/*     */     }
/* 312 */     return validatorsTypes;
/*     */   }
/*     */   
/*     */   private static Type extractType(Class<? extends ConstraintValidator<?, ?>> validator)
/*     */   {
/* 317 */     Map<Type, Type> resolvedTypes = CollectionHelper.newHashMap();
/* 318 */     Type constraintValidatorType = resolveTypes(resolvedTypes, validator);
/*     */     
/*     */ 
/* 321 */     Type validatorType = ((ParameterizedType)constraintValidatorType).getActualTypeArguments()[1];
/* 322 */     if (validatorType == null) {
/* 323 */       throw log.getNullIsAnInvalidTypeForAConstraintValidatorException();
/*     */     }
/* 325 */     if ((validatorType instanceof GenericArrayType)) {
/* 326 */       validatorType = getArrayType(getComponentType(validatorType));
/*     */     }
/*     */     
/* 329 */     while (resolvedTypes.containsKey(validatorType)) {
/* 330 */       validatorType = (Type)resolvedTypes.get(validatorType);
/*     */     }
/*     */     
/* 333 */     return validatorType;
/*     */   }
/*     */   
/*     */   private static Type resolveTypes(Map<Type, Type> resolvedTypes, Type type) {
/* 337 */     if (type == null) {
/* 338 */       return null;
/*     */     }
/* 340 */     if ((type instanceof Class)) {
/* 341 */       Class<?> clazz = (Class)type;
/* 342 */       Type returnedType = resolveTypeForClassAndHierarchy(resolvedTypes, clazz);
/* 343 */       if (returnedType != null) {
/* 344 */         return returnedType;
/*     */       }
/*     */     }
/* 347 */     else if ((type instanceof ParameterizedType)) {
/* 348 */       ParameterizedType paramType = (ParameterizedType)type;
/* 349 */       if (!(paramType.getRawType() instanceof Class)) {
/* 350 */         return null;
/*     */       }
/* 352 */       Class<?> rawType = (Class)paramType.getRawType();
/*     */       
/* 354 */       TypeVariable<?>[] originalTypes = rawType.getTypeParameters();
/* 355 */       Type[] partiallyResolvedTypes = paramType.getActualTypeArguments();
/* 356 */       int nbrOfParams = originalTypes.length;
/* 357 */       for (int i = 0; i < nbrOfParams; i++) {
/* 358 */         resolvedTypes.put(originalTypes[i], partiallyResolvedTypes[i]);
/*     */       }
/*     */       
/* 361 */       if (rawType.equals(ConstraintValidator.class))
/*     */       {
/* 363 */         return type;
/*     */       }
/*     */       
/* 366 */       Type returnedType = resolveTypeForClassAndHierarchy(resolvedTypes, rawType);
/* 367 */       if (returnedType != null) {
/* 368 */         return returnedType;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 373 */     return null;
/*     */   }
/*     */   
/*     */   private static Type resolveTypeForClassAndHierarchy(Map<Type, Type> resolvedTypes, Class<?> clazz) {
/* 377 */     Type returnedType = resolveTypes(resolvedTypes, clazz.getGenericSuperclass());
/* 378 */     if (returnedType != null) {
/* 379 */       return returnedType;
/*     */     }
/* 381 */     for (Type genericInterface : clazz.getGenericInterfaces()) {
/* 382 */       returnedType = resolveTypes(resolvedTypes, genericInterface);
/* 383 */       if (returnedType != null) {
/* 384 */         return returnedType;
/*     */       }
/*     */     }
/* 387 */     return null;
/*     */   }
/*     */   
/*     */   private static void putPrimitiveSubtypes(Map<Class<?>, Set<Class<?>>> subtypesByPrimitive, Class<?> primitiveType, Class<?>... directSubtypes)
/*     */   {
/* 392 */     Set<Class<?>> subtypes = CollectionHelper.newHashSet();
/*     */     
/* 394 */     for (Class<?> directSubtype : directSubtypes) {
/* 395 */       subtypes.add(directSubtype);
/* 396 */       subtypes.addAll((Collection)subtypesByPrimitive.get(directSubtype));
/*     */     }
/*     */     
/* 399 */     subtypesByPrimitive.put(primitiveType, Collections.unmodifiableSet(subtypes));
/*     */   }
/*     */   
/*     */   private static boolean isClassAssignable(Class<?> supertype, Class<?> type)
/*     */   {
/* 404 */     if ((supertype.isPrimitive()) && (type.isPrimitive())) {
/* 405 */       return ((Set)SUBTYPES_BY_PRIMITIVE.get(supertype)).contains(type);
/*     */     }
/*     */     
/* 408 */     return supertype.isAssignableFrom(type);
/*     */   }
/*     */   
/*     */   private static boolean isClassAssignableToWildcardType(Class<?> supertype, WildcardType type) {
/* 412 */     for (Type upperBound : type.getUpperBounds()) {
/* 413 */       if (!isAssignable(supertype, upperBound)) {
/* 414 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 418 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isParameterizedTypeAssignable(ParameterizedType supertype, ParameterizedType type) {
/* 422 */     Type rawSupertype = supertype.getRawType();
/* 423 */     Type rawType = type.getRawType();
/*     */     
/* 425 */     if (!rawSupertype.equals(rawType))
/*     */     {
/* 427 */       if (((rawSupertype instanceof Class)) && ((rawType instanceof Class)) && 
/* 428 */         (!((Class)rawSupertype).isAssignableFrom((Class)rawType))) {
/* 429 */         return false;
/*     */       }
/*     */       
/* 432 */       return isSuperAssignable(supertype, type);
/*     */     }
/*     */     
/* 435 */     Type[] supertypeArgs = supertype.getActualTypeArguments();
/* 436 */     Type[] typeArgs = type.getActualTypeArguments();
/*     */     
/* 438 */     if (supertypeArgs.length != typeArgs.length) {
/* 439 */       return false;
/*     */     }
/*     */     
/* 442 */     for (int i = 0; i < supertypeArgs.length; i++) {
/* 443 */       Type supertypeArg = supertypeArgs[i];
/* 444 */       Type typeArg = typeArgs[i];
/*     */       
/* 446 */       if ((supertypeArg instanceof WildcardType)) {
/* 447 */         if (!isWildcardTypeAssignable((WildcardType)supertypeArg, typeArg)) {
/* 448 */           return false;
/*     */         }
/*     */       }
/* 451 */       else if (!supertypeArg.equals(typeArg)) {
/* 452 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 456 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isTypeVariableAssignable(Type supertype, TypeVariable<?> type) {
/* 460 */     for (Type bound : type.getBounds()) {
/* 461 */       if (isAssignable(supertype, bound)) {
/* 462 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 466 */     return false;
/*     */   }
/*     */   
/*     */   private static boolean isWildcardTypeAssignable(WildcardType supertype, Type type) {
/* 470 */     for (Type upperBound : supertype.getUpperBounds()) {
/* 471 */       if (!isAssignable(upperBound, type)) {
/* 472 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 476 */     for (Type lowerBound : supertype.getLowerBounds()) {
/* 477 */       if (!isAssignable(type, lowerBound)) {
/* 478 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 482 */     return true;
/*     */   }
/*     */   
/*     */   private static boolean isSuperAssignable(Type supertype, Type type) {
/* 486 */     Type superclass = getResolvedSuperclass(type);
/*     */     
/* 488 */     if ((superclass != null) && (isAssignable(supertype, superclass))) {
/* 489 */       return true;
/*     */     }
/*     */     
/* 492 */     for (Type interphace : getResolvedInterfaces(type)) {
/* 493 */       if (isAssignable(supertype, interphace)) {
/* 494 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 498 */     return false;
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
/*     */   private static boolean isReferenceType(Type type)
/*     */   {
/* 521 */     return (type == null) || ((type instanceof Class)) || ((type instanceof ParameterizedType)) || ((type instanceof TypeVariable)) || ((type instanceof GenericArrayType));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isArraySupertype(Class<?> type)
/*     */   {
/* 529 */     return (Object.class.equals(type)) || 
/* 530 */       (Cloneable.class.equals(type)) || 
/* 531 */       (Serializable.class.equals(type));
/*     */   }
/*     */   
/*     */   private static Type resolveTypeVariables(Type type, Type subtype)
/*     */   {
/* 536 */     if (!(type instanceof ParameterizedType)) {
/* 537 */       return type;
/*     */     }
/*     */     
/* 540 */     Map<Type, Type> actualTypeArgumentsByParameter = getActualTypeArgumentsByParameter(new Type[] { type, subtype });
/* 541 */     Class<?> rawType = getErasedReferenceType(type);
/*     */     
/* 543 */     return parameterizeClass(rawType, actualTypeArgumentsByParameter);
/*     */   }
/*     */   
/*     */ 
/*     */   private static Map<Type, Type> getActualTypeArgumentsByParameter(Type... types)
/*     */   {
/* 549 */     Map<Type, Type> actualTypeArgumentsByParameter = new LinkedHashMap();
/*     */     
/* 551 */     for (Type type : types) {
/* 552 */       actualTypeArgumentsByParameter.putAll(getActualTypeArgumentsByParameterInternal(type));
/*     */     }
/*     */     
/* 555 */     return normalize(actualTypeArgumentsByParameter);
/*     */   }
/*     */   
/*     */   private static Map<Type, Type> getActualTypeArgumentsByParameterInternal(Type type)
/*     */   {
/* 560 */     if (!(type instanceof ParameterizedType)) {
/* 561 */       return Collections.emptyMap();
/*     */     }
/*     */     
/* 564 */     TypeVariable<?>[] typeParameters = getErasedReferenceType(type).getTypeParameters();
/* 565 */     Type[] typeArguments = ((ParameterizedType)type).getActualTypeArguments();
/*     */     
/* 567 */     if (typeParameters.length != typeArguments.length) {
/* 568 */       throw new MalformedParameterizedTypeException();
/*     */     }
/*     */     
/* 571 */     Map<Type, Type> actualTypeArgumentsByParameter = new LinkedHashMap();
/*     */     
/* 573 */     for (int i = 0; i < typeParameters.length; i++)
/*     */     {
/* 575 */       if (!typeParameters[i].equals(typeArguments[i])) {
/* 576 */         actualTypeArgumentsByParameter.put(typeParameters[i], typeArguments[i]);
/*     */       }
/*     */     }
/*     */     
/* 580 */     return actualTypeArgumentsByParameter;
/*     */   }
/*     */   
/*     */   private static ParameterizedType parameterizeClass(Class<?> type, Map<Type, Type> actualTypeArgumentsByParameter) {
/* 584 */     return parameterizeClassCapture(type, actualTypeArgumentsByParameter);
/*     */   }
/*     */   
/*     */ 
/*     */   private static <T> ParameterizedType parameterizeClassCapture(Class<T> type, Map<Type, Type> actualTypeArgumentsByParameter)
/*     */   {
/* 590 */     TypeVariable<Class<T>>[] typeParameters = type.getTypeParameters();
/* 591 */     Type[] actualTypeArguments = new Type[typeParameters.length];
/*     */     
/* 593 */     for (int i = 0; i < typeParameters.length; i++) {
/* 594 */       TypeVariable<Class<T>> typeParameter = typeParameters[i];
/* 595 */       Type actualTypeArgument = (Type)actualTypeArgumentsByParameter.get(typeParameter);
/*     */       
/* 597 */       if (actualTypeArgument == null) {
/* 598 */         throw log.getMissingActualTypeArgumentForTypeParameterException(typeParameter);
/*     */       }
/*     */       
/* 601 */       actualTypeArguments[i] = actualTypeArgument;
/*     */     }
/*     */     
/* 604 */     return parameterizedType(getErasedReferenceType(type), actualTypeArguments);
/*     */   }
/*     */   
/*     */ 
/*     */   private static <K, V> Map<K, V> normalize(Map<K, V> map)
/*     */   {
/* 610 */     for (Map.Entry<K, V> entry : map.entrySet()) {
/* 611 */       K key = entry.getKey();
/* 612 */       V value = entry.getValue();
/*     */       
/* 614 */       while (map.containsKey(value)) {
/* 615 */         value = map.get(value);
/*     */       }
/*     */       
/* 618 */       map.put(key, value);
/*     */     }
/*     */     
/* 621 */     return map;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\TypeHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */