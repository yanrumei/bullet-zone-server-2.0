/*     */ package org.hibernate.validator.internal.util;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
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
/*     */ public final class ReflectionHelper
/*     */ {
/*     */   private static final String PROPERTY_ACCESSOR_PREFIX_GET = "get";
/*     */   private static final String PROPERTY_ACCESSOR_PREFIX_IS = "is";
/*     */   private static final String PROPERTY_ACCESSOR_PREFIX_HAS = "has";
/*  44 */   public static final String[] PROPERTY_ACCESSOR_PREFIXES = { "get", "is", "has" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  50 */   private static final Log log = LoggerFactory.make();
/*     */   private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPES;
/*     */   private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPES;
/*     */   
/*     */   static {
/*  55 */     Map<Class<?>, Class<?>> tmpMap = CollectionHelper.newHashMap(9);
/*     */     
/*  57 */     tmpMap.put(Boolean.TYPE, Boolean.class);
/*  58 */     tmpMap.put(Character.TYPE, Character.class);
/*  59 */     tmpMap.put(Double.TYPE, Double.class);
/*  60 */     tmpMap.put(Float.TYPE, Float.class);
/*  61 */     tmpMap.put(Long.TYPE, Long.class);
/*  62 */     tmpMap.put(Integer.TYPE, Integer.class);
/*  63 */     tmpMap.put(Short.TYPE, Short.class);
/*  64 */     tmpMap.put(Byte.TYPE, Byte.class);
/*  65 */     tmpMap.put(Void.TYPE, Void.TYPE);
/*     */     
/*  67 */     PRIMITIVE_TO_WRAPPER_TYPES = Collections.unmodifiableMap(tmpMap);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  73 */     Map<Class<?>, Class<?>> tmpMap = CollectionHelper.newHashMap(9);
/*     */     
/*  75 */     tmpMap.put(Boolean.class, Boolean.TYPE);
/*  76 */     tmpMap.put(Character.class, Character.TYPE);
/*  77 */     tmpMap.put(Double.class, Double.TYPE);
/*  78 */     tmpMap.put(Float.class, Float.TYPE);
/*  79 */     tmpMap.put(Long.class, Long.TYPE);
/*  80 */     tmpMap.put(Integer.class, Integer.TYPE);
/*  81 */     tmpMap.put(Short.class, Short.TYPE);
/*  82 */     tmpMap.put(Byte.class, Byte.TYPE);
/*  83 */     tmpMap.put(Void.TYPE, Void.TYPE);
/*     */     
/*  85 */     WRAPPER_TO_PRIMITIVE_TYPES = Collections.unmodifiableMap(tmpMap);
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
/*     */   public static String getPropertyName(Member member)
/*     */   {
/* 117 */     String name = null;
/*     */     
/* 119 */     if ((member instanceof Field)) {
/* 120 */       name = member.getName();
/*     */     }
/*     */     
/* 123 */     if ((member instanceof Method)) {
/* 124 */       String methodName = member.getName();
/* 125 */       for (String prefix : PROPERTY_ACCESSOR_PREFIXES) {
/* 126 */         if (methodName.startsWith(prefix)) {
/* 127 */           name = StringHelper.decapitalize(methodName.substring(prefix.length()));
/*     */         }
/*     */       }
/*     */     }
/* 131 */     return name;
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
/*     */   public static boolean isGetterMethod(Method method)
/*     */   {
/* 151 */     if (method.getParameterTypes().length != 0) {
/* 152 */       return false;
/*     */     }
/*     */     
/* 155 */     String methodName = method.getName();
/*     */     
/*     */ 
/* 158 */     if ((methodName.startsWith("get")) && (method.getReturnType() != Void.TYPE)) {
/* 159 */       return true;
/*     */     }
/*     */     
/* 162 */     if ((methodName.startsWith("is")) && (method.getReturnType() == Boolean.TYPE)) {
/* 163 */       return true;
/*     */     }
/*     */     
/* 166 */     if ((methodName.startsWith("has")) && (method.getReturnType() == Boolean.TYPE)) {
/* 167 */       return true;
/*     */     }
/*     */     
/* 170 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Type typeOf(Member member)
/*     */   {
/*     */     Type type;
/*     */     
/*     */ 
/*     */ 
/* 182 */     if ((member instanceof Field)) {
/* 183 */       type = ((Field)member).getGenericType();
/*     */     } else { Type type;
/* 185 */       if ((member instanceof Method)) {
/* 186 */         type = ((Method)member).getGenericReturnType();
/*     */       } else { Type type;
/* 188 */         if ((member instanceof Constructor)) {
/* 189 */           type = member.getDeclaringClass();
/*     */         }
/*     */         else
/*     */         {
/* 193 */           throw log.getMemberIsNeitherAFieldNorAMethodException(member); } } }
/*     */     Type type;
/* 195 */     if ((type instanceof TypeVariable)) {
/* 196 */       type = TypeHelper.getErasedType(type);
/*     */     }
/* 198 */     return type;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Type typeOf(ExecutableElement executable, int parameterIndex)
/*     */   {
/* 210 */     Type[] genericParameterTypes = executable.getGenericParameterTypes();
/*     */     
/*     */ 
/* 213 */     if (parameterIndex >= genericParameterTypes.length) {
/* 214 */       genericParameterTypes = executable.getParameterTypes();
/*     */     }
/*     */     
/* 217 */     Type type = genericParameterTypes[parameterIndex];
/*     */     
/* 219 */     if ((type instanceof TypeVariable)) {
/* 220 */       type = TypeHelper.getErasedType(type);
/*     */     }
/* 222 */     return type;
/*     */   }
/*     */   
/*     */   public static Object getValue(Member member, Object object) {
/* 226 */     if ((member instanceof Method)) {
/* 227 */       return getValue((Method)member, object);
/*     */     }
/* 229 */     if ((member instanceof Field)) {
/* 230 */       return getValue((Field)member, object);
/*     */     }
/* 232 */     return null;
/*     */   }
/*     */   
/*     */   public static Object getValue(Field field, Object object) {
/*     */     try {
/* 237 */       return field.get(object);
/*     */     }
/*     */     catch (IllegalAccessException e) {
/* 240 */       throw log.getUnableToAccessMemberException(field.getName(), e);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Object getValue(Method method, Object object) {
/*     */     try {
/* 246 */       return method.invoke(object, new Object[0]);
/*     */     }
/*     */     catch (IllegalAccessException e) {
/* 249 */       throw log.getUnableToAccessMemberException(method.getName(), e);
/*     */     }
/*     */     catch (InvocationTargetException e) {
/* 252 */       throw log.getUnableToAccessMemberException(method.getName(), e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isCollection(Type type)
/*     */   {
/* 261 */     return (isIterable(type)) || 
/* 262 */       (isMap(type)) || 
/* 263 */       (TypeHelper.isArray(type));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Type getCollectionElementType(Type type)
/*     */   {
/* 270 */     Type indexedType = null;
/* 271 */     if ((isIterable(type)) && ((type instanceof ParameterizedType))) {
/* 272 */       ParameterizedType paramType = (ParameterizedType)type;
/* 273 */       indexedType = paramType.getActualTypeArguments()[0];
/*     */     }
/* 275 */     else if ((isMap(type)) && ((type instanceof ParameterizedType))) {
/* 276 */       ParameterizedType paramType = (ParameterizedType)type;
/* 277 */       indexedType = paramType.getActualTypeArguments()[1];
/*     */     }
/* 279 */     else if (TypeHelper.isArray(type)) {
/* 280 */       indexedType = TypeHelper.getComponentType(type);
/*     */     }
/* 282 */     return indexedType;
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
/*     */   public static boolean isIndexable(Type type)
/*     */   {
/* 295 */     return (isList(type)) || 
/* 296 */       (isMap(type)) || 
/* 297 */       (TypeHelper.isArray(type));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class<?> getClassFromType(Type type)
/*     */   {
/* 307 */     if ((type instanceof Class)) {
/* 308 */       return (Class)type;
/*     */     }
/* 310 */     if ((type instanceof ParameterizedType)) {
/* 311 */       return getClassFromType(((ParameterizedType)type).getRawType());
/*     */     }
/* 313 */     throw log.getUnableToConvertTypeToClassException(type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isIterable(Type type)
/*     */   {
/* 322 */     if (((type instanceof Class)) && (Iterable.class.isAssignableFrom((Class)type))) {
/* 323 */       return true;
/*     */     }
/* 325 */     if ((type instanceof ParameterizedType)) {
/* 326 */       return isIterable(((ParameterizedType)type).getRawType());
/*     */     }
/* 328 */     if ((type instanceof WildcardType)) {
/* 329 */       Type[] upperBounds = ((WildcardType)type).getUpperBounds();
/* 330 */       return (upperBounds.length != 0) && (isIterable(upperBounds[0]));
/*     */     }
/* 332 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isMap(Type type)
/*     */   {
/* 341 */     if (((type instanceof Class)) && (Map.class.isAssignableFrom((Class)type))) {
/* 342 */       return true;
/*     */     }
/* 344 */     if ((type instanceof ParameterizedType)) {
/* 345 */       return isMap(((ParameterizedType)type).getRawType());
/*     */     }
/* 347 */     if ((type instanceof WildcardType)) {
/* 348 */       Type[] upperBounds = ((WildcardType)type).getUpperBounds();
/* 349 */       return (upperBounds.length != 0) && (isMap(upperBounds[0]));
/*     */     }
/* 351 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isList(Type type)
/*     */   {
/* 360 */     if (((type instanceof Class)) && (List.class.isAssignableFrom((Class)type))) {
/* 361 */       return true;
/*     */     }
/* 363 */     if ((type instanceof ParameterizedType)) {
/* 364 */       return isList(((ParameterizedType)type).getRawType());
/*     */     }
/* 366 */     if ((type instanceof WildcardType)) {
/* 367 */       Type[] upperBounds = ((WildcardType)type).getUpperBounds();
/* 368 */       return (upperBounds.length != 0) && (isList(upperBounds[0]));
/*     */     }
/* 370 */     return false;
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
/*     */   public static Object getIndexedValue(Object value, Integer index)
/*     */   {
/* 385 */     if (value == null) {
/* 386 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 390 */     Type type = value.getClass();
/* 391 */     Iterator<?> iter; if (isIterable(type)) {
/* 392 */       iter = ((Iterable)value).iterator();
/*     */     } else { Iterator<?> iter;
/* 394 */       if (TypeHelper.isArray(type)) {
/* 395 */         List<?> arrayList = Arrays.asList(new Object[] { value });
/* 396 */         iter = arrayList.iterator();
/*     */       }
/*     */       else {
/* 399 */         return null;
/*     */       } }
/*     */     Iterator<?> iter;
/* 402 */     int i = 0;
/*     */     
/* 404 */     while (iter.hasNext()) {
/* 405 */       Object o = iter.next();
/* 406 */       if (i == index.intValue()) {
/* 407 */         return o;
/*     */       }
/* 409 */       i++;
/*     */     }
/* 411 */     return null;
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
/*     */   public static Object getMappedValue(Object value, Object key)
/*     */   {
/* 424 */     if (!(value instanceof Map)) {
/* 425 */       return null;
/*     */     }
/*     */     
/* 428 */     Map<?, ?> map = (Map)value;
/*     */     
/* 430 */     return map.get(key);
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
/*     */   public static Class<?> boxedType(Class<?> primitiveType)
/*     */   {
/* 446 */     Class<?> wrapperType = (Class)PRIMITIVE_TO_WRAPPER_TYPES.get(primitiveType);
/*     */     
/* 448 */     if (wrapperType == null) {
/* 449 */       throw log.getHasToBeAPrimitiveTypeException(primitiveType.getClass());
/*     */     }
/*     */     
/* 452 */     return wrapperType;
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
/*     */   public static Class<?> unBoxedType(Class<?> type)
/*     */   {
/* 468 */     Class<?> wrapperType = (Class)WRAPPER_TO_PRIMITIVE_TYPES.get(type);
/*     */     
/* 470 */     if (wrapperType == null) {
/* 471 */       throw log.getHasToBeABoxedTypeException(type.getClass());
/*     */     }
/*     */     
/* 474 */     return wrapperType;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\ReflectionHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */