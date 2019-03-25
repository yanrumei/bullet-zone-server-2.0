/*      */ package com.fasterxml.jackson.databind.ser;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonFormat.Shape;
/*      */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*      */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties.Value;
/*      */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.BeanDescription;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.JsonSerializer;
/*      */ import com.fasterxml.jackson.databind.MapperFeature;
/*      */ import com.fasterxml.jackson.databind.SerializationConfig;
/*      */ import com.fasterxml.jackson.databind.SerializerProvider;
/*      */ import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;
/*      */ import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
/*      */ import com.fasterxml.jackson.databind.ext.OptionalHandlerFactory;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*      */ import com.fasterxml.jackson.databind.introspect.BasicBeanDescription;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.impl.IteratorSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.impl.MapEntrySerializer;
/*      */ import com.fasterxml.jackson.databind.ser.impl.StringArraySerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.BooleanSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.CalendarSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.DateSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.MapSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.NumberSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
/*      */ import com.fasterxml.jackson.databind.ser.std.TimeZoneSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
/*      */ import com.fasterxml.jackson.databind.type.ArrayType;
/*      */ import com.fasterxml.jackson.databind.type.CollectionLikeType;
/*      */ import com.fasterxml.jackson.databind.type.CollectionType;
/*      */ import com.fasterxml.jackson.databind.type.MapLikeType;
/*      */ import com.fasterxml.jackson.databind.type.MapType;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.Converter;
/*      */ import java.lang.reflect.Method;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.net.InetAddress;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.util.Calendar;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map.Entry;
/*      */ 
/*      */ public abstract class BasicSerializerFactory extends SerializerFactory implements java.io.Serializable
/*      */ {
/*      */   protected static final HashMap<String, JsonSerializer<?>> _concrete;
/*      */   protected static final HashMap<String, Class<? extends JsonSerializer<?>>> _concreteLazy;
/*      */   protected final SerializerFactoryConfig _factoryConfig;
/*      */   
/*      */   static
/*      */   {
/*   62 */     HashMap<String, Class<? extends JsonSerializer<?>>> concLazy = new HashMap();
/*      */     
/*   64 */     HashMap<String, JsonSerializer<?>> concrete = new HashMap();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   71 */     concrete.put(String.class.getName(), new com.fasterxml.jackson.databind.ser.std.StringSerializer());
/*   72 */     ToStringSerializer sls = ToStringSerializer.instance;
/*   73 */     concrete.put(StringBuffer.class.getName(), sls);
/*   74 */     concrete.put(StringBuilder.class.getName(), sls);
/*   75 */     concrete.put(Character.class.getName(), sls);
/*   76 */     concrete.put(Character.TYPE.getName(), sls);
/*      */     
/*      */ 
/*   79 */     com.fasterxml.jackson.databind.ser.std.NumberSerializers.addAll(concrete);
/*   80 */     concrete.put(Boolean.TYPE.getName(), new BooleanSerializer(true));
/*   81 */     concrete.put(Boolean.class.getName(), new BooleanSerializer(false));
/*      */     
/*      */ 
/*   84 */     concrete.put(BigInteger.class.getName(), new NumberSerializer(BigInteger.class));
/*   85 */     concrete.put(BigDecimal.class.getName(), new NumberSerializer(BigDecimal.class));
/*      */     
/*      */ 
/*      */ 
/*   89 */     concrete.put(Calendar.class.getName(), CalendarSerializer.instance);
/*   90 */     concrete.put(Date.class.getName(), DateSerializer.instance);
/*      */     
/*      */ 
/*   93 */     for (Map.Entry<Class<?>, Object> en : com.fasterxml.jackson.databind.ser.std.StdJdkSerializers.all()) {
/*   94 */       Object value = en.getValue();
/*   95 */       if ((value instanceof JsonSerializer)) {
/*   96 */         concrete.put(((Class)en.getKey()).getName(), (JsonSerializer)value);
/*   97 */       } else if ((value instanceof Class))
/*      */       {
/*   99 */         Class<? extends JsonSerializer<?>> cls = (Class)value;
/*  100 */         concLazy.put(((Class)en.getKey()).getName(), cls);
/*      */       } else {
/*  102 */         throw new IllegalStateException("Internal error: unrecognized value of type " + en.getClass().getName());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  108 */     concLazy.put(com.fasterxml.jackson.databind.util.TokenBuffer.class.getName(), com.fasterxml.jackson.databind.ser.std.TokenBufferSerializer.class);
/*      */     
/*  110 */     _concrete = concrete;
/*  111 */     _concreteLazy = concLazy;
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
/*      */   protected BasicSerializerFactory(SerializerFactoryConfig config)
/*      */   {
/*  138 */     this._factoryConfig = (config == null ? new SerializerFactoryConfig() : config);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SerializerFactoryConfig getFactoryConfig()
/*      */   {
/*  149 */     return this._factoryConfig;
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
/*      */   public final SerializerFactory withAdditionalSerializers(Serializers additional)
/*      */   {
/*  170 */     return withConfig(this._factoryConfig.withAdditionalSerializers(additional));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final SerializerFactory withAdditionalKeySerializers(Serializers additional)
/*      */   {
/*  179 */     return withConfig(this._factoryConfig.withAdditionalKeySerializers(additional));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final SerializerFactory withSerializerModifier(BeanSerializerModifier modifier)
/*      */   {
/*  188 */     return withConfig(this._factoryConfig.withSerializerModifier(modifier));
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
/*      */   public JsonSerializer<Object> createKeySerializer(SerializationConfig config, JavaType keyType, JsonSerializer<Object> defaultImpl)
/*      */   {
/*  210 */     BeanDescription beanDesc = config.introspectClassAnnotations(keyType.getRawClass());
/*  211 */     JsonSerializer<?> ser = null;
/*      */     
/*  213 */     if (this._factoryConfig.hasKeySerializers())
/*      */     {
/*  215 */       for (Serializers serializers : this._factoryConfig.keySerializers()) {
/*  216 */         ser = serializers.findSerializer(config, keyType, beanDesc);
/*  217 */         if (ser != null) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*  222 */     if (ser == null) {
/*  223 */       ser = defaultImpl;
/*  224 */       if (ser == null) {
/*  225 */         ser = StdKeySerializers.getStdKeySerializer(config, keyType.getRawClass(), false);
/*      */         
/*  227 */         if (ser == null) {
/*  228 */           beanDesc = config.introspect(keyType);
/*  229 */           AnnotatedMethod am = beanDesc.findJsonValueMethod();
/*  230 */           if (am != null) {
/*  231 */             Class<?> rawType = am.getRawReturnType();
/*  232 */             JsonSerializer<?> delegate = StdKeySerializers.getStdKeySerializer(config, rawType, true);
/*      */             
/*  234 */             Method m = am.getAnnotated();
/*  235 */             if (config.canOverrideAccessModifiers()) {
/*  236 */               ClassUtil.checkAndFixAccess(m, config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*      */             }
/*  238 */             ser = new JsonValueSerializer(am, delegate);
/*      */           } else {
/*  240 */             ser = StdKeySerializers.getFallbackKeySerializer(config, keyType.getRawClass());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  247 */     if (this._factoryConfig.hasSerializerModifiers()) {
/*  248 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  249 */         ser = mod.modifyKeySerializer(config, keyType, beanDesc, ser);
/*      */       }
/*      */     }
/*  252 */     return ser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TypeSerializer createTypeSerializer(SerializationConfig config, JavaType baseType)
/*      */   {
/*  264 */     BeanDescription bean = config.introspectClassAnnotations(baseType.getRawClass());
/*  265 */     com.fasterxml.jackson.databind.introspect.AnnotatedClass ac = bean.getClassInfo();
/*  266 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/*  267 */     TypeResolverBuilder<?> b = ai.findTypeResolver(config, ac, baseType);
/*      */     
/*      */ 
/*      */ 
/*  271 */     java.util.Collection<com.fasterxml.jackson.databind.jsontype.NamedType> subtypes = null;
/*  272 */     if (b == null) {
/*  273 */       b = config.getDefaultTyper(baseType);
/*      */     } else {
/*  275 */       subtypes = config.getSubtypeResolver().collectAndResolveSubtypesByClass(config, ac);
/*      */     }
/*  277 */     if (b == null) {
/*  278 */       return null;
/*      */     }
/*      */     
/*      */ 
/*  282 */     return b.buildTypeSerializer(config, baseType, subtypes);
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
/*      */   protected final JsonSerializer<?> findSerializerByLookup(JavaType type, SerializationConfig config, BeanDescription beanDesc, boolean staticTyping)
/*      */   {
/*  307 */     Class<?> raw = type.getRawClass();
/*  308 */     String clsName = raw.getName();
/*  309 */     JsonSerializer<?> ser = (JsonSerializer)_concrete.get(clsName);
/*  310 */     if (ser == null) {
/*  311 */       Class<? extends JsonSerializer<?>> serClass = (Class)_concreteLazy.get(clsName);
/*  312 */       if (serClass != null) {
/*      */         try {
/*  314 */           return (JsonSerializer)serClass.newInstance();
/*      */         } catch (Exception e) {
/*  316 */           throw new IllegalStateException("Failed to instantiate standard serializer (of type " + serClass.getName() + "): " + e.getMessage(), e);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  321 */     return ser;
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
/*      */   protected final JsonSerializer<?> findSerializerByAnnotations(SerializerProvider prov, JavaType type, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/*  344 */     Class<?> raw = type.getRawClass();
/*      */     
/*  346 */     if (com.fasterxml.jackson.databind.JsonSerializable.class.isAssignableFrom(raw)) {
/*  347 */       return com.fasterxml.jackson.databind.ser.std.SerializableSerializer.instance;
/*      */     }
/*      */     
/*  350 */     AnnotatedMethod valueMethod = beanDesc.findJsonValueMethod();
/*  351 */     if (valueMethod != null) {
/*  352 */       Method m = valueMethod.getAnnotated();
/*  353 */       if (prov.canOverrideAccessModifiers()) {
/*  354 */         ClassUtil.checkAndFixAccess(m, prov.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*      */       }
/*  356 */       JsonSerializer<Object> ser = findSerializerFromAnnotation(prov, valueMethod);
/*  357 */       return new JsonValueSerializer(valueMethod, ser);
/*      */     }
/*      */     
/*  360 */     return null;
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
/*      */   protected final JsonSerializer<?> findSerializerByPrimaryType(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping)
/*      */     throws JsonMappingException
/*      */   {
/*  375 */     Class<?> raw = type.getRawClass();
/*      */     
/*      */ 
/*  378 */     JsonSerializer<?> ser = findOptionalStdSerializer(prov, type, beanDesc, staticTyping);
/*  379 */     if (ser != null) {
/*  380 */       return ser;
/*      */     }
/*      */     
/*  383 */     if (Calendar.class.isAssignableFrom(raw)) {
/*  384 */       return CalendarSerializer.instance;
/*      */     }
/*  386 */     if (Date.class.isAssignableFrom(raw)) {
/*  387 */       return DateSerializer.instance;
/*      */     }
/*  389 */     if (Map.Entry.class.isAssignableFrom(raw))
/*      */     {
/*  391 */       JavaType mapEntryType = type.findSuperType(Map.Entry.class);
/*      */       
/*      */ 
/*  394 */       JavaType kt = mapEntryType.containedTypeOrUnknown(0);
/*  395 */       JavaType vt = mapEntryType.containedTypeOrUnknown(1);
/*  396 */       return buildMapEntrySerializer(prov.getConfig(), type, beanDesc, staticTyping, kt, vt);
/*      */     }
/*  398 */     if (java.nio.ByteBuffer.class.isAssignableFrom(raw)) {
/*  399 */       return new com.fasterxml.jackson.databind.ser.std.ByteBufferSerializer();
/*      */     }
/*  401 */     if (InetAddress.class.isAssignableFrom(raw)) {
/*  402 */       return new com.fasterxml.jackson.databind.ser.std.InetAddressSerializer();
/*      */     }
/*  404 */     if (InetSocketAddress.class.isAssignableFrom(raw)) {
/*  405 */       return new com.fasterxml.jackson.databind.ser.std.InetSocketAddressSerializer();
/*      */     }
/*  407 */     if (java.util.TimeZone.class.isAssignableFrom(raw)) {
/*  408 */       return new TimeZoneSerializer();
/*      */     }
/*  410 */     if (java.nio.charset.Charset.class.isAssignableFrom(raw)) {
/*  411 */       return ToStringSerializer.instance;
/*      */     }
/*  413 */     if (Number.class.isAssignableFrom(raw))
/*      */     {
/*  415 */       JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/*  416 */       if (format != null) {
/*  417 */         switch (format.getShape()) {
/*      */         case STRING: 
/*  419 */           return ToStringSerializer.instance;
/*      */         case OBJECT: 
/*      */         case ARRAY: 
/*  422 */           return null;
/*      */         }
/*      */         
/*      */       }
/*  426 */       return NumberSerializer.instance;
/*      */     }
/*  428 */     if (Enum.class.isAssignableFrom(raw)) {
/*  429 */       return buildEnumSerializer(prov.getConfig(), type, beanDesc);
/*      */     }
/*  431 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<?> findOptionalStdSerializer(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping)
/*      */     throws JsonMappingException
/*      */   {
/*  443 */     return OptionalHandlerFactory.instance.findSerializer(prov.getConfig(), type, beanDesc);
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
/*      */   protected final JsonSerializer<?> findSerializerByAddonType(SerializationConfig config, JavaType javaType, BeanDescription beanDesc, boolean staticTyping)
/*      */     throws JsonMappingException
/*      */   {
/*  457 */     Class<?> rawType = javaType.getRawClass();
/*      */     
/*  459 */     if (Iterator.class.isAssignableFrom(rawType)) {
/*  460 */       JavaType[] params = config.getTypeFactory().findTypeParameters(javaType, Iterator.class);
/*  461 */       JavaType vt = (params == null) || (params.length != 1) ? TypeFactory.unknownType() : params[0];
/*      */       
/*  463 */       return buildIteratorSerializer(config, javaType, beanDesc, staticTyping, vt);
/*      */     }
/*  465 */     if (Iterable.class.isAssignableFrom(rawType)) {
/*  466 */       JavaType[] params = config.getTypeFactory().findTypeParameters(javaType, Iterable.class);
/*  467 */       JavaType vt = (params == null) || (params.length != 1) ? TypeFactory.unknownType() : params[0];
/*      */       
/*  469 */       return buildIterableSerializer(config, javaType, beanDesc, staticTyping, vt);
/*      */     }
/*  471 */     if (CharSequence.class.isAssignableFrom(rawType)) {
/*  472 */       return ToStringSerializer.instance;
/*      */     }
/*  474 */     return null;
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
/*      */   protected JsonSerializer<Object> findSerializerFromAnnotation(SerializerProvider prov, Annotated a)
/*      */     throws JsonMappingException
/*      */   {
/*  489 */     Object serDef = prov.getAnnotationIntrospector().findSerializer(a);
/*  490 */     if (serDef == null) {
/*  491 */       return null;
/*      */     }
/*  493 */     JsonSerializer<Object> ser = prov.serializerInstance(a, serDef);
/*      */     
/*  495 */     return findConvertingSerializer(prov, a, ser);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<?> findConvertingSerializer(SerializerProvider prov, Annotated a, JsonSerializer<?> ser)
/*      */     throws JsonMappingException
/*      */   {
/*  508 */     Converter<Object, Object> conv = findConverter(prov, a);
/*  509 */     if (conv == null) {
/*  510 */       return ser;
/*      */     }
/*  512 */     JavaType delegateType = conv.getOutputType(prov.getTypeFactory());
/*  513 */     return new com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer(conv, delegateType, ser);
/*      */   }
/*      */   
/*      */ 
/*      */   protected Converter<Object, Object> findConverter(SerializerProvider prov, Annotated a)
/*      */     throws JsonMappingException
/*      */   {
/*  520 */     Object convDef = prov.getAnnotationIntrospector().findSerializationConverter(a);
/*  521 */     if (convDef == null) {
/*  522 */       return null;
/*      */     }
/*  524 */     return prov.converterInstance(a, convDef);
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
/*      */   protected JsonSerializer<?> buildContainerSerializer(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping)
/*      */     throws JsonMappingException
/*      */   {
/*  540 */     SerializationConfig config = prov.getConfig();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  546 */     if ((!staticTyping) && (type.useStaticType()) && (
/*  547 */       (!type.isContainerType()) || (type.getContentType().getRawClass() != Object.class))) {
/*  548 */       staticTyping = true;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  553 */     JavaType elementType = type.getContentType();
/*  554 */     TypeSerializer elementTypeSerializer = createTypeSerializer(config, elementType);
/*      */     
/*      */ 
/*      */ 
/*  558 */     if (elementTypeSerializer != null) {
/*  559 */       staticTyping = false;
/*      */     }
/*  561 */     JsonSerializer<Object> elementValueSerializer = _findContentSerializer(prov, beanDesc.getClassInfo());
/*      */     
/*  563 */     if (type.isMapLikeType()) {
/*  564 */       MapLikeType mlt = (MapLikeType)type;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  570 */       JsonSerializer<Object> keySerializer = _findKeySerializer(prov, beanDesc.getClassInfo());
/*  571 */       if (mlt.isTrueMapType()) {
/*  572 */         return buildMapSerializer(prov, (MapType)mlt, beanDesc, staticTyping, keySerializer, elementTypeSerializer, elementValueSerializer);
/*      */       }
/*      */       
/*      */ 
/*  576 */       JsonSerializer<?> ser = null;
/*  577 */       MapLikeType mlType = (MapLikeType)type;
/*  578 */       for (Serializers serializers : customSerializers()) {
/*  579 */         ser = serializers.findMapLikeSerializer(config, mlType, beanDesc, keySerializer, elementTypeSerializer, elementValueSerializer);
/*      */         
/*  581 */         if (ser != null) {
/*      */           break;
/*      */         }
/*      */       }
/*  585 */       if (ser == null) {
/*  586 */         ser = findSerializerByAnnotations(prov, type, beanDesc);
/*      */       }
/*  588 */       if ((ser != null) && 
/*  589 */         (this._factoryConfig.hasSerializerModifiers())) {
/*  590 */         for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  591 */           ser = mod.modifyMapLikeSerializer(config, mlType, beanDesc, ser);
/*      */         }
/*      */       }
/*      */       
/*  595 */       return ser;
/*      */     }
/*  597 */     if (type.isCollectionLikeType()) {
/*  598 */       CollectionLikeType clt = (CollectionLikeType)type;
/*  599 */       if (clt.isTrueCollectionType()) {
/*  600 */         return buildCollectionSerializer(prov, (CollectionType)clt, beanDesc, staticTyping, elementTypeSerializer, elementValueSerializer);
/*      */       }
/*      */       
/*      */ 
/*  604 */       JsonSerializer<?> ser = null;
/*  605 */       CollectionLikeType clType = (CollectionLikeType)type;
/*  606 */       for (Serializers serializers : customSerializers()) {
/*  607 */         ser = serializers.findCollectionLikeSerializer(config, clType, beanDesc, elementTypeSerializer, elementValueSerializer);
/*      */         
/*  609 */         if (ser != null) {
/*      */           break;
/*      */         }
/*      */       }
/*  613 */       if (ser == null) {
/*  614 */         ser = findSerializerByAnnotations(prov, type, beanDesc);
/*      */       }
/*  616 */       if ((ser != null) && 
/*  617 */         (this._factoryConfig.hasSerializerModifiers())) {
/*  618 */         for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  619 */           ser = mod.modifyCollectionLikeSerializer(config, clType, beanDesc, ser);
/*      */         }
/*      */       }
/*      */       
/*  623 */       return ser;
/*      */     }
/*  625 */     if (type.isArrayType()) {
/*  626 */       return buildArraySerializer(prov, (ArrayType)type, beanDesc, staticTyping, elementTypeSerializer, elementValueSerializer);
/*      */     }
/*      */     
/*  629 */     return null;
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
/*      */   protected JsonSerializer<?> buildCollectionSerializer(SerializerProvider prov, CollectionType type, BeanDescription beanDesc, boolean staticTyping, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer)
/*      */     throws JsonMappingException
/*      */   {
/*  643 */     SerializationConfig config = prov.getConfig();
/*  644 */     JsonSerializer<?> ser = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  649 */     for (Serializers serializers : customSerializers()) {
/*  650 */       ser = serializers.findCollectionSerializer(config, type, beanDesc, elementTypeSerializer, elementValueSerializer);
/*      */       
/*  652 */       if (ser != null) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*  657 */     if (ser == null) {
/*  658 */       ser = findSerializerByAnnotations(prov, type, beanDesc);
/*  659 */       if (ser == null)
/*      */       {
/*      */ 
/*  662 */         JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/*  663 */         if ((format != null) && (format.getShape() == JsonFormat.Shape.OBJECT)) {
/*  664 */           return null;
/*      */         }
/*  666 */         Class<?> raw = type.getRawClass();
/*  667 */         if (java.util.EnumSet.class.isAssignableFrom(raw))
/*      */         {
/*  669 */           JavaType enumType = type.getContentType();
/*      */           
/*  671 */           if (!enumType.isEnumType()) {
/*  672 */             enumType = null;
/*      */           }
/*  674 */           ser = buildEnumSetSerializer(enumType);
/*      */         } else {
/*  676 */           Class<?> elementRaw = type.getContentType().getRawClass();
/*  677 */           if (isIndexedList(raw)) {
/*  678 */             if (elementRaw == String.class)
/*      */             {
/*  680 */               if ((elementValueSerializer == null) || (ClassUtil.isJacksonStdImpl(elementValueSerializer))) {
/*  681 */                 ser = com.fasterxml.jackson.databind.ser.impl.IndexedStringListSerializer.instance;
/*      */               }
/*      */             } else {
/*  684 */               ser = buildIndexedListSerializer(type.getContentType(), staticTyping, elementTypeSerializer, elementValueSerializer);
/*      */             }
/*      */           }
/*  687 */           else if (elementRaw == String.class)
/*      */           {
/*  689 */             if ((elementValueSerializer == null) || (ClassUtil.isJacksonStdImpl(elementValueSerializer))) {
/*  690 */               ser = com.fasterxml.jackson.databind.ser.impl.StringCollectionSerializer.instance;
/*      */             }
/*      */           }
/*  693 */           if (ser == null) {
/*  694 */             ser = buildCollectionSerializer(type.getContentType(), staticTyping, elementTypeSerializer, elementValueSerializer);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  701 */     if (this._factoryConfig.hasSerializerModifiers()) {
/*  702 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  703 */         ser = mod.modifyCollectionSerializer(config, type, beanDesc, ser);
/*      */       }
/*      */     }
/*  706 */     return ser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean isIndexedList(Class<?> cls)
/*      */   {
/*  717 */     return java.util.RandomAccess.class.isAssignableFrom(cls);
/*      */   }
/*      */   
/*      */   public ContainerSerializer<?> buildIndexedListSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> valueSerializer)
/*      */   {
/*  722 */     return new com.fasterxml.jackson.databind.ser.impl.IndexedListSerializer(elemType, staticTyping, vts, valueSerializer);
/*      */   }
/*      */   
/*      */   public ContainerSerializer<?> buildCollectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> valueSerializer) {
/*  726 */     return new com.fasterxml.jackson.databind.ser.std.CollectionSerializer(elemType, staticTyping, vts, valueSerializer);
/*      */   }
/*      */   
/*      */   public JsonSerializer<?> buildEnumSetSerializer(JavaType enumType) {
/*  730 */     return new com.fasterxml.jackson.databind.ser.std.EnumSetSerializer(enumType);
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
/*      */   protected JsonSerializer<?> buildMapSerializer(SerializerProvider prov, MapType type, BeanDescription beanDesc, boolean staticTyping, JsonSerializer<Object> keySerializer, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer)
/*      */     throws JsonMappingException
/*      */   {
/*  749 */     SerializationConfig config = prov.getConfig();
/*  750 */     JsonSerializer<?> ser = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  757 */     for (Serializers serializers : customSerializers()) {
/*  758 */       ser = serializers.findMapSerializer(config, type, beanDesc, keySerializer, elementTypeSerializer, elementValueSerializer);
/*      */       
/*  760 */       if (ser != null) break;
/*      */     }
/*  762 */     if (ser == null) {
/*  763 */       ser = findSerializerByAnnotations(prov, type, beanDesc);
/*  764 */       if (ser == null) {
/*  765 */         Object filterId = findFilterId(config, beanDesc);
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  770 */         JsonIgnoreProperties.Value ignorals = config.getDefaultPropertyIgnorals(java.util.Map.class, beanDesc.getClassInfo());
/*      */         
/*  772 */         java.util.Set<String> ignored = ignorals == null ? null : ignorals.findIgnoredForSerialization();
/*      */         
/*  774 */         MapSerializer mapSer = MapSerializer.construct(ignored, type, staticTyping, elementTypeSerializer, keySerializer, elementValueSerializer, filterId);
/*      */         
/*      */ 
/*  777 */         Object suppressableValue = findSuppressableContentValue(config, type.getContentType(), beanDesc);
/*      */         
/*  779 */         if (suppressableValue != null) {
/*  780 */           mapSer = mapSer.withContentInclusion(suppressableValue);
/*      */         }
/*  782 */         ser = mapSer;
/*      */       }
/*      */     }
/*      */     
/*  786 */     if (this._factoryConfig.hasSerializerModifiers()) {
/*  787 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  788 */         ser = mod.modifyMapSerializer(config, type, beanDesc, ser);
/*      */       }
/*      */     }
/*  791 */     return ser;
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
/*      */   protected Object findSuppressableContentValue(SerializationConfig config, JavaType contentType, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/*  809 */     JsonInclude.Value inclV = beanDesc.findPropertyInclusion(config.getDefaultPropertyInclusion());
/*      */     
/*  811 */     if (inclV == null) {
/*  812 */       return null;
/*      */     }
/*  814 */     com.fasterxml.jackson.annotation.JsonInclude.Include incl = inclV.getContentInclusion();
/*  815 */     switch (incl) {
/*      */     case USE_DEFAULTS: 
/*  817 */       return null;
/*      */     case NON_DEFAULT: 
/*      */       break;
/*      */     }
/*      */     
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  830 */     return incl;
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
/*      */   protected JsonSerializer<?> buildArraySerializer(SerializerProvider prov, ArrayType type, BeanDescription beanDesc, boolean staticTyping, TypeSerializer elementTypeSerializer, JsonSerializer<Object> elementValueSerializer)
/*      */     throws JsonMappingException
/*      */   {
/*  853 */     SerializationConfig config = prov.getConfig();
/*  854 */     JsonSerializer<?> ser = null;
/*      */     
/*  856 */     for (Serializers serializers : customSerializers()) {
/*  857 */       ser = serializers.findArraySerializer(config, type, beanDesc, elementTypeSerializer, elementValueSerializer);
/*      */       
/*  859 */       if (ser != null) {
/*      */         break;
/*      */       }
/*      */     }
/*      */     
/*  864 */     if (ser == null) {
/*  865 */       Class<?> raw = type.getRawClass();
/*      */       
/*  867 */       if ((elementValueSerializer == null) || (ClassUtil.isJacksonStdImpl(elementValueSerializer))) {
/*  868 */         if (String[].class == raw) {
/*  869 */           ser = StringArraySerializer.instance;
/*      */         }
/*      */         else {
/*  872 */           ser = com.fasterxml.jackson.databind.ser.std.StdArraySerializers.findStandardImpl(raw);
/*      */         }
/*      */       }
/*  875 */       if (ser == null) {
/*  876 */         ser = new com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer(type.getContentType(), staticTyping, elementTypeSerializer, elementValueSerializer);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  881 */     if (this._factoryConfig.hasSerializerModifiers()) {
/*  882 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  883 */         ser = mod.modifyArraySerializer(config, type, beanDesc, ser);
/*      */       }
/*      */     }
/*  886 */     return ser;
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
/*      */   protected JsonSerializer<?> buildIteratorSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc, boolean staticTyping, JavaType valueType)
/*      */     throws JsonMappingException
/*      */   {
/*  903 */     return new IteratorSerializer(valueType, staticTyping, createTypeSerializer(config, valueType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<?> buildIterableSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc, boolean staticTyping, JavaType valueType)
/*      */     throws JsonMappingException
/*      */   {
/*  914 */     return new com.fasterxml.jackson.databind.ser.std.IterableSerializer(valueType, staticTyping, createTypeSerializer(config, valueType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<?> buildMapEntrySerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc, boolean staticTyping, JavaType keyType, JavaType valueType)
/*      */     throws JsonMappingException
/*      */   {
/*  925 */     return new MapEntrySerializer(valueType, keyType, valueType, staticTyping, createTypeSerializer(config, valueType), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<?> buildEnumSerializer(SerializationConfig config, JavaType type, BeanDescription beanDesc)
/*      */     throws JsonMappingException
/*      */   {
/*  938 */     JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/*  939 */     if ((format != null) && (format.getShape() == JsonFormat.Shape.OBJECT))
/*      */     {
/*  941 */       ((BasicBeanDescription)beanDesc).removeProperty("declaringClass");
/*      */       
/*  943 */       return null;
/*      */     }
/*      */     
/*  946 */     Class<Enum<?>> enumClass = type.getRawClass();
/*  947 */     JsonSerializer<?> ser = com.fasterxml.jackson.databind.ser.std.EnumSerializer.construct(enumClass, config, beanDesc, format);
/*      */     
/*  949 */     if (this._factoryConfig.hasSerializerModifiers()) {
/*  950 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/*  951 */         ser = mod.modifyEnumSerializer(config, type, beanDesc, ser);
/*      */       }
/*      */     }
/*  954 */     return ser;
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
/*      */   protected JsonSerializer<Object> _findKeySerializer(SerializerProvider prov, Annotated a)
/*      */     throws JsonMappingException
/*      */   {
/*  972 */     AnnotationIntrospector intr = prov.getAnnotationIntrospector();
/*  973 */     Object serDef = intr.findKeySerializer(a);
/*  974 */     if (serDef != null) {
/*  975 */       return prov.serializerInstance(a, serDef);
/*      */     }
/*  977 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<Object> _findContentSerializer(SerializerProvider prov, Annotated a)
/*      */     throws JsonMappingException
/*      */   {
/*  989 */     AnnotationIntrospector intr = prov.getAnnotationIntrospector();
/*  990 */     Object serDef = intr.findContentSerializer(a);
/*  991 */     if (serDef != null) {
/*  992 */       return prov.serializerInstance(a, serDef);
/*      */     }
/*  994 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object findFilterId(SerializationConfig config, BeanDescription beanDesc)
/*      */   {
/* 1002 */     return config.getAnnotationIntrospector().findFilterId(beanDesc.getClassInfo());
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
/*      */   protected boolean usesStaticTyping(SerializationConfig config, BeanDescription beanDesc, TypeSerializer typeSer)
/*      */   {
/* 1019 */     if (typeSer != null) {
/* 1020 */       return false;
/*      */     }
/* 1022 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 1023 */     JsonSerialize.Typing t = intr.findSerializationTyping(beanDesc.getClassInfo());
/* 1024 */     if ((t != null) && (t != JsonSerialize.Typing.DEFAULT_TYPING)) {
/* 1025 */       return t == JsonSerialize.Typing.STATIC;
/*      */     }
/* 1027 */     return config.isEnabled(MapperFeature.USE_STATIC_TYPING);
/*      */   }
/*      */   
/*      */   protected Class<?> _verifyAsClass(Object src, String methodName, Class<?> noneClass)
/*      */   {
/* 1032 */     if (src == null) {
/* 1033 */       return null;
/*      */     }
/* 1035 */     if (!(src instanceof Class)) {
/* 1036 */       throw new IllegalStateException("AnnotationIntrospector." + methodName + "() returned value of type " + src.getClass().getName() + ": expected type JsonSerializer or Class<JsonSerializer> instead");
/*      */     }
/* 1038 */     Class<?> cls = (Class)src;
/* 1039 */     if ((cls == noneClass) || (ClassUtil.isBogusClass(cls))) {
/* 1040 */       return null;
/*      */     }
/* 1042 */     return cls;
/*      */   }
/*      */   
/*      */   public abstract SerializerFactory withConfig(SerializerFactoryConfig paramSerializerFactoryConfig);
/*      */   
/*      */   public abstract JsonSerializer<Object> createSerializer(SerializerProvider paramSerializerProvider, JavaType paramJavaType)
/*      */     throws JsonMappingException;
/*      */   
/*      */   protected abstract Iterable<Serializers> customSerializers();
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\BasicSerializerFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */