/*      */ package com.fasterxml.jackson.databind.type;
/*      */ 
/*      */ import com.fasterxml.jackson.core.type.TypeReference;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.LRUMap;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.GenericArrayType;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.TypeVariable;
/*      */ import java.lang.reflect.WildcardType;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.EnumMap;
/*      */ import java.util.EnumSet;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.TreeMap;
/*      */ import java.util.TreeSet;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class TypeFactory
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   39 */   private static final JavaType[] NO_TYPES = new JavaType[0];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   46 */   protected static final TypeFactory instance = new TypeFactory();
/*      */   
/*   48 */   protected static final TypeBindings EMPTY_BINDINGS = TypeBindings.emptyBindings();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   60 */   private static final Class<?> CLS_STRING = String.class;
/*   61 */   private static final Class<?> CLS_OBJECT = Object.class;
/*      */   
/*   63 */   private static final Class<?> CLS_COMPARABLE = Comparable.class;
/*   64 */   private static final Class<?> CLS_CLASS = Class.class;
/*   65 */   private static final Class<?> CLS_ENUM = Enum.class;
/*      */   
/*   67 */   private static final Class<?> CLS_BOOL = Boolean.TYPE;
/*   68 */   private static final Class<?> CLS_INT = Integer.TYPE;
/*   69 */   private static final Class<?> CLS_LONG = Long.TYPE;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   78 */   protected static final SimpleType CORE_TYPE_BOOL = new SimpleType(CLS_BOOL);
/*   79 */   protected static final SimpleType CORE_TYPE_INT = new SimpleType(CLS_INT);
/*   80 */   protected static final SimpleType CORE_TYPE_LONG = new SimpleType(CLS_LONG);
/*      */   
/*      */ 
/*   83 */   protected static final SimpleType CORE_TYPE_STRING = new SimpleType(CLS_STRING);
/*      */   
/*      */ 
/*   86 */   protected static final SimpleType CORE_TYPE_OBJECT = new SimpleType(CLS_OBJECT);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   94 */   protected static final SimpleType CORE_TYPE_COMPARABLE = new SimpleType(CLS_COMPARABLE);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  102 */   protected static final SimpleType CORE_TYPE_ENUM = new SimpleType(CLS_ENUM);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  110 */   protected static final SimpleType CORE_TYPE_CLASS = new SimpleType(CLS_CLASS);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final LRUMap<Object, JavaType> _typeCache;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final TypeModifier[] _modifiers;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final TypeParser _parser;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final ClassLoader _classLoader;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private TypeFactory()
/*      */   {
/*  145 */     this(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected TypeFactory(LRUMap<Object, JavaType> typeCache)
/*      */   {
/*  152 */     if (typeCache == null) {
/*  153 */       typeCache = new LRUMap(16, 200);
/*      */     }
/*  155 */     this._typeCache = typeCache;
/*  156 */     this._parser = new TypeParser(this);
/*  157 */     this._modifiers = null;
/*  158 */     this._classLoader = null;
/*      */   }
/*      */   
/*      */ 
/*      */   protected TypeFactory(LRUMap<Object, JavaType> typeCache, TypeParser p, TypeModifier[] mods, ClassLoader classLoader)
/*      */   {
/*  164 */     if (typeCache == null) {
/*  165 */       typeCache = new LRUMap(16, 200);
/*      */     }
/*  167 */     this._typeCache = typeCache;
/*      */     
/*  169 */     this._parser = p.withFactory(this);
/*  170 */     this._modifiers = mods;
/*  171 */     this._classLoader = classLoader;
/*      */   }
/*      */   
/*      */   public TypeFactory withModifier(TypeModifier mod)
/*      */   {
/*  176 */     LRUMap<Object, JavaType> typeCache = this._typeCache;
/*      */     TypeModifier[] mods;
/*  178 */     if (mod == null) {
/*  179 */       TypeModifier[] mods = null;
/*      */       
/*      */ 
/*  182 */       typeCache = null; } else { TypeModifier[] mods;
/*  183 */       if (this._modifiers == null) {
/*  184 */         mods = new TypeModifier[] { mod };
/*      */       } else
/*  186 */         mods = (TypeModifier[])ArrayBuilders.insertInListNoDup(this._modifiers, mod);
/*      */     }
/*  188 */     return new TypeFactory(typeCache, this._parser, mods, this._classLoader);
/*      */   }
/*      */   
/*      */   public TypeFactory withClassLoader(ClassLoader classLoader) {
/*  192 */     return new TypeFactory(this._typeCache, this._parser, this._modifiers, classLoader);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TypeFactory withCache(LRUMap<Object, JavaType> cache)
/*      */   {
/*  203 */     return new TypeFactory(cache, this._parser, this._modifiers, this._classLoader);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static TypeFactory defaultInstance()
/*      */   {
/*  211 */     return instance;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void clearCache()
/*      */   {
/*  224 */     this._typeCache.clear();
/*      */   }
/*      */   
/*      */   public ClassLoader getClassLoader() {
/*  228 */     return this._classLoader;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static JavaType unknownType()
/*      */   {
/*  243 */     return defaultInstance()._unknownType();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?> rawClass(Type t)
/*      */   {
/*  253 */     if ((t instanceof Class)) {
/*  254 */       return (Class)t;
/*      */     }
/*      */     
/*  257 */     return defaultInstance().constructType(t).getRawClass();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<?> findClass(String className)
/*      */     throws ClassNotFoundException
/*      */   {
/*  274 */     if (className.indexOf('.') < 0) {
/*  275 */       Class<?> prim = _findPrimitive(className);
/*  276 */       if (prim != null) {
/*  277 */         return prim;
/*      */       }
/*      */     }
/*      */     
/*  281 */     Throwable prob = null;
/*  282 */     ClassLoader loader = getClassLoader();
/*  283 */     if (loader == null) {
/*  284 */       loader = Thread.currentThread().getContextClassLoader();
/*      */     }
/*  286 */     if (loader != null) {
/*      */       try {
/*  288 */         return classForName(className, true, loader);
/*      */       } catch (Exception e) {
/*  290 */         prob = ClassUtil.getRootCause(e);
/*      */       }
/*      */     }
/*      */     try {
/*  294 */       return classForName(className);
/*      */     } catch (Exception e) {
/*  296 */       if (prob == null) {
/*  297 */         prob = ClassUtil.getRootCause(e);
/*      */       }
/*      */       
/*  300 */       if ((prob instanceof RuntimeException)) {
/*  301 */         throw ((RuntimeException)prob);
/*      */       }
/*  303 */       throw new ClassNotFoundException(prob.getMessage(), prob);
/*      */     }
/*      */   }
/*      */   
/*      */   protected Class<?> classForName(String name, boolean initialize, ClassLoader loader) throws ClassNotFoundException {
/*  308 */     return Class.forName(name, true, loader);
/*      */   }
/*      */   
/*      */   protected Class<?> classForName(String name) throws ClassNotFoundException {
/*  312 */     return Class.forName(name);
/*      */   }
/*      */   
/*      */   protected Class<?> _findPrimitive(String className)
/*      */   {
/*  317 */     if ("int".equals(className)) return Integer.TYPE;
/*  318 */     if ("long".equals(className)) return Long.TYPE;
/*  319 */     if ("float".equals(className)) return Float.TYPE;
/*  320 */     if ("double".equals(className)) return Double.TYPE;
/*  321 */     if ("boolean".equals(className)) return Boolean.TYPE;
/*  322 */     if ("byte".equals(className)) return Byte.TYPE;
/*  323 */     if ("char".equals(className)) return Character.TYPE;
/*  324 */     if ("short".equals(className)) return Short.TYPE;
/*  325 */     if ("void".equals(className)) return Void.TYPE;
/*  326 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass)
/*      */   {
/*  345 */     Class<?> rawBase = baseType.getRawClass();
/*  346 */     if (rawBase == subclass) {
/*  347 */       return baseType;
/*      */     }
/*      */     
/*      */ 
/*      */     JavaType newType;
/*      */     
/*      */ 
/*  354 */     if (rawBase == Object.class) {
/*  355 */       newType = _fromClass(null, subclass, TypeBindings.emptyBindings());
/*      */     }
/*      */     else {
/*  358 */       if (!rawBase.isAssignableFrom(subclass)) {
/*  359 */         throw new IllegalArgumentException(String.format("Class %s not subtype of %s", new Object[] { subclass.getName(), baseType }));
/*      */       }
/*      */       
/*      */ 
/*      */       JavaType newType;
/*      */       
/*  365 */       if (baseType.getBindings().isEmpty()) {
/*  366 */         newType = _fromClass(null, subclass, TypeBindings.emptyBindings());
/*      */       }
/*      */       else
/*      */       {
/*  370 */         if (baseType.isContainerType()) {
/*  371 */           if (baseType.isMapLikeType()) {
/*  372 */             if ((subclass == HashMap.class) || (subclass == LinkedHashMap.class) || (subclass == EnumMap.class) || (subclass == TreeMap.class))
/*      */             {
/*      */ 
/*      */ 
/*  376 */               JavaType newType = _fromClass(null, subclass, TypeBindings.create(subclass, baseType.getKeyType(), baseType.getContentType()));
/*      */               break label313;
/*      */             }
/*      */           }
/*  380 */           else if (baseType.isCollectionLikeType()) {
/*  381 */             if ((subclass == ArrayList.class) || (subclass == LinkedList.class) || (subclass == HashSet.class) || (subclass == TreeSet.class))
/*      */             {
/*      */ 
/*      */ 
/*  385 */               JavaType newType = _fromClass(null, subclass, TypeBindings.create(subclass, baseType.getContentType()));
/*      */               
/*      */ 
/*      */               break label313;
/*      */             }
/*      */             
/*  391 */             if (rawBase == EnumSet.class) {
/*  392 */               return baseType;
/*      */             }
/*      */           }
/*      */         }
/*      */         
/*  397 */         int typeParamCount = subclass.getTypeParameters().length;
/*  398 */         JavaType newType; if (typeParamCount == 0) {
/*  399 */           newType = _fromClass(null, subclass, TypeBindings.emptyBindings());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  414 */           TypeBindings tb = _bindingsForSubtype(baseType, typeParamCount, subclass);
/*  415 */           JavaType newType; if (baseType.isInterface()) {
/*  416 */             newType = baseType.refine(subclass, tb, null, new JavaType[] { baseType });
/*      */           } else {
/*  418 */             newType = baseType.refine(subclass, tb, baseType, NO_TYPES);
/*      */           }
/*      */           
/*  421 */           if (newType == null) {
/*  422 */             newType = _fromClass(null, subclass, tb);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     label313:
/*  428 */     JavaType newType = newType.withHandlersFrom(baseType);
/*  429 */     return newType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private TypeBindings _bindingsForSubtype(JavaType baseType, int typeParamCount, Class<?> subclass)
/*      */   {
/*  481 */     int baseCount = baseType.containedTypeCount();
/*  482 */     if (baseCount == typeParamCount) {
/*  483 */       if (typeParamCount == 1) {
/*  484 */         return TypeBindings.create(subclass, baseType.containedType(0));
/*      */       }
/*  486 */       if (typeParamCount == 2) {
/*  487 */         return TypeBindings.create(subclass, baseType.containedType(0), baseType.containedType(1));
/*      */       }
/*      */       
/*  490 */       List<JavaType> types = new ArrayList(baseCount);
/*  491 */       for (int i = 0; i < baseCount; i++) {
/*  492 */         types.add(baseType.containedType(i));
/*      */       }
/*  494 */       return TypeBindings.create(subclass, types);
/*      */     }
/*      */     
/*  497 */     return TypeBindings.emptyBindings();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructGeneralizedType(JavaType baseType, Class<?> superClass)
/*      */   {
/*  513 */     Class<?> rawBase = baseType.getRawClass();
/*  514 */     if (rawBase == superClass) {
/*  515 */       return baseType;
/*      */     }
/*  517 */     JavaType superType = baseType.findSuperType(superClass);
/*  518 */     if (superType == null)
/*      */     {
/*  520 */       if (!superClass.isAssignableFrom(rawBase)) {
/*  521 */         throw new IllegalArgumentException(String.format("Class %s not a super-type of %s", new Object[] { superClass.getName(), baseType }));
/*      */       }
/*      */       
/*      */ 
/*  525 */       throw new IllegalArgumentException(String.format("Internal error: class %s not included as super-type for %s", new Object[] { superClass.getName(), baseType }));
/*      */     }
/*      */     
/*      */ 
/*  529 */     return superType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructFromCanonical(String canonical)
/*      */     throws IllegalArgumentException
/*      */   {
/*  544 */     return this._parser.parse(canonical);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType[] findTypeParameters(JavaType type, Class<?> expType)
/*      */   {
/*  558 */     JavaType match = type.findSuperType(expType);
/*  559 */     if (match == null) {
/*  560 */       return NO_TYPES;
/*      */     }
/*  562 */     return match.getBindings().typeParameterArray();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JavaType[] findTypeParameters(Class<?> clz, Class<?> expType, TypeBindings bindings)
/*      */   {
/*  570 */     return findTypeParameters(constructType(clz, bindings), expType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JavaType[] findTypeParameters(Class<?> clz, Class<?> expType)
/*      */   {
/*  578 */     return findTypeParameters(constructType(clz), expType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType moreSpecificType(JavaType type1, JavaType type2)
/*      */   {
/*  593 */     if (type1 == null) {
/*  594 */       return type2;
/*      */     }
/*  596 */     if (type2 == null) {
/*  597 */       return type1;
/*      */     }
/*  599 */     Class<?> raw1 = type1.getRawClass();
/*  600 */     Class<?> raw2 = type2.getRawClass();
/*  601 */     if (raw1 == raw2) {
/*  602 */       return type1;
/*      */     }
/*      */     
/*  605 */     if (raw1.isAssignableFrom(raw2)) {
/*  606 */       return type2;
/*      */     }
/*  608 */     return type1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructType(Type type)
/*      */   {
/*  618 */     return _fromAny(null, type, EMPTY_BINDINGS);
/*      */   }
/*      */   
/*      */   public JavaType constructType(Type type, TypeBindings bindings) {
/*  622 */     return _fromAny(null, type, bindings);
/*      */   }
/*      */   
/*      */ 
/*      */   public JavaType constructType(TypeReference<?> typeRef)
/*      */   {
/*  628 */     return _fromAny(null, typeRef.getType(), EMPTY_BINDINGS);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JavaType constructType(Type type, Class<?> contextClass)
/*      */   {
/*  654 */     JavaType contextType = contextClass == null ? null : constructType(contextClass);
/*  655 */     return constructType(type, contextType);
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public JavaType constructType(Type type, JavaType contextType)
/*      */   {
/*      */     TypeBindings bindings;
/*      */     TypeBindings bindings;
/*  664 */     if (contextType == null) {
/*  665 */       bindings = TypeBindings.emptyBindings();
/*      */     } else {
/*  667 */       bindings = contextType.getBindings();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  672 */       if (type.getClass() != Class.class)
/*      */       {
/*      */ 
/*  675 */         while (bindings.isEmpty()) {
/*  676 */           contextType = contextType.getSuperClass();
/*  677 */           if (contextType == null) {
/*      */             break;
/*      */           }
/*  680 */           bindings = contextType.getBindings();
/*      */         }
/*      */       }
/*      */     }
/*  684 */     return _fromAny(null, type, bindings);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ArrayType constructArrayType(Class<?> elementType)
/*      */   {
/*  700 */     return ArrayType.construct(_fromAny(null, elementType, null), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ArrayType constructArrayType(JavaType elementType)
/*      */   {
/*  710 */     return ArrayType.construct(elementType, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CollectionType constructCollectionType(Class<? extends Collection> collectionClass, Class<?> elementClass)
/*      */   {
/*  721 */     return constructCollectionType(collectionClass, _fromClass(null, elementClass, EMPTY_BINDINGS));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CollectionType constructCollectionType(Class<? extends Collection> collectionClass, JavaType elementType)
/*      */   {
/*  735 */     return (CollectionType)_fromClass(null, collectionClass, TypeBindings.create(collectionClass, elementType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CollectionLikeType constructCollectionLikeType(Class<?> collectionClass, Class<?> elementClass)
/*      */   {
/*  746 */     return constructCollectionLikeType(collectionClass, _fromClass(null, elementClass, EMPTY_BINDINGS));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CollectionLikeType constructCollectionLikeType(Class<?> collectionClass, JavaType elementType)
/*      */   {
/*  757 */     JavaType type = _fromClass(null, collectionClass, TypeBindings.createIfNeeded(collectionClass, elementType));
/*      */     
/*  759 */     if ((type instanceof CollectionLikeType)) {
/*  760 */       return (CollectionLikeType)type;
/*      */     }
/*  762 */     return CollectionLikeType.upgradeFrom(type, elementType);
/*      */   }
/*      */   
/*      */ 
/*      */   public MapType constructMapType(Class<? extends Map> mapClass, Class<?> keyClass, Class<?> valueClass)
/*      */   {
/*      */     JavaType kt;
/*      */     
/*      */     JavaType kt;
/*      */     
/*      */     JavaType vt;
/*      */     
/*  774 */     if (mapClass == Properties.class) { JavaType vt;
/*  775 */       kt = vt = CORE_TYPE_STRING;
/*      */     } else {
/*  777 */       kt = _fromClass(null, keyClass, EMPTY_BINDINGS);
/*  778 */       vt = _fromClass(null, valueClass, EMPTY_BINDINGS);
/*      */     }
/*  780 */     return constructMapType(mapClass, kt, vt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MapType constructMapType(Class<? extends Map> mapClass, JavaType keyType, JavaType valueType)
/*      */   {
/*  790 */     return (MapType)_fromClass(null, mapClass, TypeBindings.create(mapClass, keyType, valueType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MapLikeType constructMapLikeType(Class<?> mapClass, Class<?> keyClass, Class<?> valueClass)
/*      */   {
/*  801 */     return constructMapLikeType(mapClass, _fromClass(null, keyClass, EMPTY_BINDINGS), _fromClass(null, valueClass, EMPTY_BINDINGS));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MapLikeType constructMapLikeType(Class<?> mapClass, JavaType keyType, JavaType valueType)
/*      */   {
/*  815 */     JavaType type = _fromClass(null, mapClass, TypeBindings.createIfNeeded(mapClass, new JavaType[] { keyType, valueType }));
/*      */     
/*  817 */     if ((type instanceof MapLikeType)) {
/*  818 */       return (MapLikeType)type;
/*      */     }
/*  820 */     return MapLikeType.upgradeFrom(type, keyType, valueType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructSimpleType(Class<?> rawType, JavaType[] parameterTypes)
/*      */   {
/*  829 */     return _fromClass(null, rawType, TypeBindings.create(rawType, parameterTypes));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JavaType constructSimpleType(Class<?> rawType, Class<?> parameterTarget, JavaType[] parameterTypes)
/*      */   {
/*  843 */     return constructSimpleType(rawType, parameterTypes);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructReferenceType(Class<?> rawType, JavaType referredType)
/*      */   {
/*  851 */     return ReferenceType.construct(rawType, null, null, null, referredType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JavaType uncheckedSimpleType(Class<?> cls)
/*      */   {
/*  870 */     return _constructSimple(cls, EMPTY_BINDINGS, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructParametricType(Class<?> parametrized, Class<?>... parameterClasses)
/*      */   {
/*  901 */     int len = parameterClasses.length;
/*  902 */     JavaType[] pt = new JavaType[len];
/*  903 */     for (int i = 0; i < len; i++) {
/*  904 */       pt[i] = _fromClass(null, parameterClasses[i], null);
/*      */     }
/*  906 */     return constructParametricType(parametrized, pt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructParametricType(Class<?> rawType, JavaType... parameterTypes)
/*      */   {
/*  938 */     return _fromClass(null, rawType, TypeBindings.create(rawType, parameterTypes));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructParametrizedType(Class<?> parametrized, Class<?> parametersFor, JavaType... parameterTypes)
/*      */   {
/*  947 */     return constructParametricType(parametrized, parameterTypes);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructParametrizedType(Class<?> parametrized, Class<?> parametersFor, Class<?>... parameterClasses)
/*      */   {
/*  956 */     return constructParametricType(parametrized, parameterClasses);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CollectionType constructRawCollectionType(Class<? extends Collection> collectionClass)
/*      */   {
/*  978 */     return constructCollectionType(collectionClass, unknownType());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public CollectionLikeType constructRawCollectionLikeType(Class<?> collectionClass)
/*      */   {
/*  993 */     return constructCollectionLikeType(collectionClass, unknownType());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MapType constructRawMapType(Class<? extends Map> mapClass)
/*      */   {
/* 1008 */     return constructMapType(mapClass, unknownType(), unknownType());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MapLikeType constructRawMapLikeType(Class<?> mapClass)
/*      */   {
/* 1023 */     return constructMapLikeType(mapClass, unknownType(), unknownType());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private JavaType _mapType(Class<?> rawClass, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces)
/*      */   {
/*      */     JavaType kt;
/*      */     
/*      */ 
/*      */     JavaType vt;
/*      */     
/*      */ 
/*      */     JavaType kt;
/*      */     
/* 1038 */     if (rawClass == Properties.class) { JavaType vt;
/* 1039 */       kt = vt = CORE_TYPE_STRING;
/*      */     } else {
/* 1041 */       List<JavaType> typeParams = bindings.getTypeParameters();
/*      */       
/* 1043 */       switch (typeParams.size()) {
/*      */       case 0: 
/* 1045 */         kt = vt = _unknownType();
/* 1046 */         break;
/*      */       case 2: 
/* 1048 */         kt = (JavaType)typeParams.get(0);
/* 1049 */         vt = (JavaType)typeParams.get(1);
/* 1050 */         break;
/*      */       default: 
/* 1052 */         throw new IllegalArgumentException("Strange Map type " + rawClass.getName() + ": can not determine type parameters");
/*      */       }
/*      */     }
/* 1055 */     return MapType.construct(rawClass, bindings, superClass, superInterfaces, kt, vt);
/*      */   }
/*      */   
/*      */ 
/*      */   private JavaType _collectionType(Class<?> rawClass, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces)
/*      */   {
/* 1061 */     List<JavaType> typeParams = bindings.getTypeParameters();
/*      */     
/*      */     JavaType ct;
/* 1064 */     if (typeParams.isEmpty()) {
/* 1065 */       ct = _unknownType(); } else { JavaType ct;
/* 1066 */       if (typeParams.size() == 1) {
/* 1067 */         ct = (JavaType)typeParams.get(0);
/*      */       } else
/* 1069 */         throw new IllegalArgumentException("Strange Collection type " + rawClass.getName() + ": can not determine type parameters"); }
/*      */     JavaType ct;
/* 1071 */     return CollectionType.construct(rawClass, bindings, superClass, superInterfaces, ct);
/*      */   }
/*      */   
/*      */ 
/*      */   private JavaType _referenceType(Class<?> rawClass, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces)
/*      */   {
/* 1077 */     List<JavaType> typeParams = bindings.getTypeParameters();
/*      */     
/*      */     JavaType ct;
/* 1080 */     if (typeParams.isEmpty()) {
/* 1081 */       ct = _unknownType(); } else { JavaType ct;
/* 1082 */       if (typeParams.size() == 1) {
/* 1083 */         ct = (JavaType)typeParams.get(0);
/*      */       } else
/* 1085 */         throw new IllegalArgumentException("Strange Reference type " + rawClass.getName() + ": can not determine type parameters"); }
/*      */     JavaType ct;
/* 1087 */     return ReferenceType.construct(rawClass, bindings, superClass, superInterfaces, ct);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JavaType _constructSimple(Class<?> raw, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces)
/*      */   {
/* 1101 */     if (bindings.isEmpty()) {
/* 1102 */       JavaType result = _findWellKnownSimple(raw);
/* 1103 */       if (result != null) {
/* 1104 */         return result;
/*      */       }
/*      */     }
/* 1107 */     return _newSimpleType(raw, bindings, superClass, superInterfaces);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JavaType _newSimpleType(Class<?> raw, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces)
/*      */   {
/* 1120 */     return new SimpleType(raw, bindings, superClass, superInterfaces);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JavaType _unknownType()
/*      */   {
/* 1129 */     return CORE_TYPE_OBJECT;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JavaType _findWellKnownSimple(Class<?> clz)
/*      */   {
/* 1140 */     if (clz.isPrimitive()) {
/* 1141 */       if (clz == CLS_BOOL) return CORE_TYPE_BOOL;
/* 1142 */       if (clz == CLS_INT) return CORE_TYPE_INT;
/* 1143 */       if (clz == CLS_LONG) return CORE_TYPE_LONG;
/*      */     } else {
/* 1145 */       if (clz == CLS_STRING) return CORE_TYPE_STRING;
/* 1146 */       if (clz == CLS_OBJECT) return CORE_TYPE_OBJECT;
/*      */     }
/* 1148 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JavaType _fromAny(ClassStack context, Type type, TypeBindings bindings)
/*      */   {
/*      */     JavaType resultType;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1167 */     if ((type instanceof Class))
/*      */     {
/* 1169 */       resultType = _fromClass(context, (Class)type, EMPTY_BINDINGS);
/*      */     } else {
/*      */       JavaType resultType;
/* 1172 */       if ((type instanceof ParameterizedType)) {
/* 1173 */         resultType = _fromParamType(context, (ParameterizedType)type, bindings);
/*      */       } else {
/* 1175 */         if ((type instanceof JavaType))
/*      */         {
/* 1177 */           return (JavaType)type; }
/*      */         JavaType resultType;
/* 1179 */         if ((type instanceof GenericArrayType)) {
/* 1180 */           resultType = _fromArrayType(context, (GenericArrayType)type, bindings);
/*      */         } else { JavaType resultType;
/* 1182 */           if ((type instanceof TypeVariable)) {
/* 1183 */             resultType = _fromVariable(context, (TypeVariable)type, bindings);
/*      */           } else { JavaType resultType;
/* 1185 */             if ((type instanceof WildcardType)) {
/* 1186 */               resultType = _fromWildcard(context, (WildcardType)type, bindings);
/*      */             }
/*      */             else
/* 1189 */               throw new IllegalArgumentException("Unrecognized Type: " + (type == null ? "[null]" : type.toString()));
/*      */           }
/*      */         }
/*      */       } }
/*      */     JavaType resultType;
/* 1194 */     if (this._modifiers != null) {
/* 1195 */       TypeBindings b = resultType.getBindings();
/* 1196 */       if (b == null) {
/* 1197 */         b = EMPTY_BINDINGS;
/*      */       }
/* 1199 */       for (TypeModifier mod : this._modifiers) {
/* 1200 */         JavaType t = mod.modifyType(resultType, type, b, this);
/* 1201 */         if (t == null) {
/* 1202 */           throw new IllegalStateException(String.format("TypeModifier %s (of type %s) return null for type %s", new Object[] { mod, mod.getClass().getName(), resultType }));
/*      */         }
/*      */         
/*      */ 
/* 1206 */         resultType = t;
/*      */       }
/*      */     }
/* 1209 */     return resultType;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JavaType _fromClass(ClassStack context, Class<?> rawType, TypeBindings bindings)
/*      */   {
/* 1219 */     JavaType result = _findWellKnownSimple(rawType);
/* 1220 */     if (result != null) {
/* 1221 */       return result;
/*      */     }
/*      */     Object key;
/*      */     Object key;
/* 1225 */     if ((bindings == null) || (bindings.isEmpty())) {
/* 1226 */       key = rawType;
/*      */     } else {
/* 1228 */       key = bindings.asKey(rawType);
/*      */     }
/* 1230 */     result = (JavaType)this._typeCache.get(key);
/* 1231 */     if (result != null) {
/* 1232 */       return result;
/*      */     }
/*      */     
/*      */ 
/* 1236 */     if (context == null) {
/* 1237 */       context = new ClassStack(rawType);
/*      */     } else {
/* 1239 */       ClassStack prev = context.find(rawType);
/* 1240 */       if (prev != null)
/*      */       {
/* 1242 */         ResolvedRecursiveType selfRef = new ResolvedRecursiveType(rawType, EMPTY_BINDINGS);
/* 1243 */         prev.addSelfReference(selfRef);
/* 1244 */         return selfRef;
/*      */       }
/*      */       
/* 1247 */       context = context.child(rawType);
/*      */     }
/*      */     
/*      */ 
/* 1251 */     if (rawType.isArray()) {
/* 1252 */       result = ArrayType.construct(_fromAny(context, rawType.getComponentType(), bindings), bindings);
/*      */     }
/*      */     else
/*      */     {
/*      */       JavaType[] superInterfaces;
/*      */       
/*      */       JavaType superClass;
/*      */       JavaType[] superInterfaces;
/* 1260 */       if (rawType.isInterface()) {
/* 1261 */         JavaType superClass = null;
/* 1262 */         superInterfaces = _resolveSuperInterfaces(context, rawType, bindings);
/*      */       }
/*      */       else {
/* 1265 */         superClass = _resolveSuperClass(context, rawType, bindings);
/* 1266 */         superInterfaces = _resolveSuperInterfaces(context, rawType, bindings);
/*      */       }
/*      */       
/*      */ 
/* 1270 */       if (rawType == Properties.class) {
/* 1271 */         result = MapType.construct(rawType, bindings, superClass, superInterfaces, CORE_TYPE_STRING, CORE_TYPE_STRING);
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/* 1276 */       else if (superClass != null) {
/* 1277 */         result = superClass.refine(rawType, bindings, superClass, superInterfaces);
/*      */       }
/*      */       
/* 1280 */       if (result == null) {
/* 1281 */         result = _fromWellKnownClass(context, rawType, bindings, superClass, superInterfaces);
/* 1282 */         if (result == null) {
/* 1283 */           result = _fromWellKnownInterface(context, rawType, bindings, superClass, superInterfaces);
/* 1284 */           if (result == null)
/*      */           {
/* 1286 */             result = _newSimpleType(rawType, bindings, superClass, superInterfaces);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1291 */     context.resolveSelfReferences(result);
/*      */     
/*      */ 
/* 1294 */     if (!result.hasHandlers()) {
/* 1295 */       this._typeCache.putIfAbsent(key, result);
/*      */     }
/* 1297 */     return result;
/*      */   }
/*      */   
/*      */   protected JavaType _resolveSuperClass(ClassStack context, Class<?> rawType, TypeBindings parentBindings)
/*      */   {
/* 1302 */     Type parent = ClassUtil.getGenericSuperclass(rawType);
/* 1303 */     if (parent == null) {
/* 1304 */       return null;
/*      */     }
/* 1306 */     return _fromAny(context, parent, parentBindings);
/*      */   }
/*      */   
/*      */   protected JavaType[] _resolveSuperInterfaces(ClassStack context, Class<?> rawType, TypeBindings parentBindings)
/*      */   {
/* 1311 */     Type[] types = ClassUtil.getGenericInterfaces(rawType);
/* 1312 */     if ((types == null) || (types.length == 0)) {
/* 1313 */       return NO_TYPES;
/*      */     }
/* 1315 */     int len = types.length;
/* 1316 */     JavaType[] resolved = new JavaType[len];
/* 1317 */     for (int i = 0; i < len; i++) {
/* 1318 */       Type type = types[i];
/* 1319 */       resolved[i] = _fromAny(context, type, parentBindings);
/*      */     }
/* 1321 */     return resolved;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JavaType _fromWellKnownClass(ClassStack context, Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces)
/*      */   {
/* 1332 */     if (bindings == null) {
/* 1333 */       bindings = TypeBindings.emptyBindings();
/*      */     }
/*      */     
/*      */ 
/* 1337 */     if (rawType == Map.class) {
/* 1338 */       return _mapType(rawType, bindings, superClass, superInterfaces);
/*      */     }
/* 1340 */     if (rawType == Collection.class) {
/* 1341 */       return _collectionType(rawType, bindings, superClass, superInterfaces);
/*      */     }
/*      */     
/* 1344 */     if (rawType == AtomicReference.class) {
/* 1345 */       return _referenceType(rawType, bindings, superClass, superInterfaces);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1351 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JavaType _fromWellKnownInterface(ClassStack context, Class<?> rawType, TypeBindings bindings, JavaType superClass, JavaType[] superInterfaces)
/*      */   {
/* 1359 */     int intCount = superInterfaces.length;
/*      */     
/* 1361 */     for (int i = 0; i < intCount; i++) {
/* 1362 */       JavaType result = superInterfaces[i].refine(rawType, bindings, superClass, superInterfaces);
/* 1363 */       if (result != null) {
/* 1364 */         return result;
/*      */       }
/*      */     }
/* 1367 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JavaType _fromParamType(ClassStack context, ParameterizedType ptype, TypeBindings parentBindings)
/*      */   {
/* 1378 */     Class<?> rawType = (Class)ptype.getRawType();
/*      */     
/*      */ 
/*      */ 
/* 1382 */     if (rawType == CLS_ENUM) {
/* 1383 */       return CORE_TYPE_ENUM;
/*      */     }
/* 1385 */     if (rawType == CLS_COMPARABLE) {
/* 1386 */       return CORE_TYPE_COMPARABLE;
/*      */     }
/* 1388 */     if (rawType == CLS_CLASS) {
/* 1389 */       return CORE_TYPE_CLASS;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1395 */     Type[] args = ptype.getActualTypeArguments();
/* 1396 */     int paramCount = args == null ? 0 : args.length;
/*      */     
/*      */     TypeBindings newBindings;
/*      */     TypeBindings newBindings;
/* 1400 */     if (paramCount == 0) {
/* 1401 */       newBindings = EMPTY_BINDINGS;
/*      */     } else {
/* 1403 */       JavaType[] pt = new JavaType[paramCount];
/* 1404 */       for (int i = 0; i < paramCount; i++) {
/* 1405 */         pt[i] = _fromAny(context, args[i], parentBindings);
/*      */       }
/* 1407 */       newBindings = TypeBindings.create(rawType, pt);
/*      */     }
/* 1409 */     return _fromClass(context, rawType, newBindings);
/*      */   }
/*      */   
/*      */   protected JavaType _fromArrayType(ClassStack context, GenericArrayType type, TypeBindings bindings)
/*      */   {
/* 1414 */     JavaType elementType = _fromAny(context, type.getGenericComponentType(), bindings);
/* 1415 */     return ArrayType.construct(elementType, bindings);
/*      */   }
/*      */   
/*      */ 
/*      */   protected JavaType _fromVariable(ClassStack context, TypeVariable<?> var, TypeBindings bindings)
/*      */   {
/* 1421 */     String name = var.getName();
/* 1422 */     JavaType type = bindings.findBoundType(name);
/* 1423 */     if (type != null) {
/* 1424 */       return type;
/*      */     }
/*      */     
/*      */ 
/* 1428 */     if (bindings.hasUnbound(name)) {
/* 1429 */       return CORE_TYPE_OBJECT;
/*      */     }
/* 1431 */     bindings = bindings.withUnboundVariable(name);
/*      */     
/* 1433 */     Type[] bounds = var.getBounds();
/* 1434 */     return _fromAny(context, bounds[0], bindings);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JavaType _fromWildcard(ClassStack context, WildcardType type, TypeBindings bindings)
/*      */   {
/* 1444 */     return _fromAny(context, type.getUpperBounds()[0], bindings);
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\type\TypeFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */