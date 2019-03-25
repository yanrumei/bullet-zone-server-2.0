/*      */ package com.fasterxml.jackson.databind;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*      */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*      */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*      */ import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.SerializerCache;
/*      */ import com.fasterxml.jackson.databind.ser.SerializerFactory;
/*      */ import com.fasterxml.jackson.databind.ser.impl.FailingSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.impl.ReadOnlyClassToSerializerMap;
/*      */ import com.fasterxml.jackson.databind.ser.impl.TypeWrappedSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.impl.UnknownSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
/*      */ import com.fasterxml.jackson.databind.ser.std.NullSerializer;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import java.io.IOException;
/*      */ import java.text.DateFormat;
/*      */ import java.util.Date;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
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
/*      */ public abstract class SerializerProvider
/*      */   extends DatabindContext
/*      */ {
/*      */   protected static final boolean CACHE_UNKNOWN_MAPPINGS = false;
/*   55 */   public static final JsonSerializer<Object> DEFAULT_NULL_KEY_SERIALIZER = new FailingSerializer("Null key for a Map not allowed in JSON (use a converting NullKeySerializer?)");
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
/*   67 */   protected static final JsonSerializer<Object> DEFAULT_UNKNOWN_SERIALIZER = new UnknownSerializer();
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
/*      */   protected final SerializationConfig _config;
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
/*      */   protected final Class<?> _serializationView;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final SerializerFactory _serializerFactory;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final SerializerCache _serializerCache;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected transient ContextAttributes _attributes;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  129 */   protected JsonSerializer<Object> _unknownTypeSerializer = DEFAULT_UNKNOWN_SERIALIZER;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonSerializer<Object> _keySerializer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  142 */   protected JsonSerializer<Object> _nullValueSerializer = NullSerializer.instance;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  151 */   protected JsonSerializer<Object> _nullKeySerializer = DEFAULT_NULL_KEY_SERIALIZER;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final ReadOnlyClassToSerializerMap _knownSerializers;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DateFormat _dateFormat;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean _stdNullValueSerializer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SerializerProvider()
/*      */   {
/*  192 */     this._config = null;
/*  193 */     this._serializerFactory = null;
/*  194 */     this._serializerCache = new SerializerCache();
/*      */     
/*  196 */     this._knownSerializers = null;
/*      */     
/*  198 */     this._serializationView = null;
/*  199 */     this._attributes = null;
/*      */     
/*      */ 
/*  202 */     this._stdNullValueSerializer = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SerializerProvider(SerializerProvider src, SerializationConfig config, SerializerFactory f)
/*      */   {
/*  214 */     if (config == null) {
/*  215 */       throw new NullPointerException();
/*      */     }
/*  217 */     this._serializerFactory = f;
/*  218 */     this._config = config;
/*      */     
/*  220 */     this._serializerCache = src._serializerCache;
/*  221 */     this._unknownTypeSerializer = src._unknownTypeSerializer;
/*  222 */     this._keySerializer = src._keySerializer;
/*  223 */     this._nullValueSerializer = src._nullValueSerializer;
/*  224 */     this._nullKeySerializer = src._nullKeySerializer;
/*      */     
/*  226 */     this._stdNullValueSerializer = (this._nullValueSerializer == DEFAULT_NULL_KEY_SERIALIZER);
/*      */     
/*  228 */     this._serializationView = config.getActiveView();
/*  229 */     this._attributes = config.getAttributes();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  234 */     this._knownSerializers = this._serializerCache.getReadOnlyLookupMap();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SerializerProvider(SerializerProvider src)
/*      */   {
/*  245 */     this._config = null;
/*  246 */     this._serializationView = null;
/*  247 */     this._serializerFactory = null;
/*  248 */     this._knownSerializers = null;
/*      */     
/*      */ 
/*  251 */     this._serializerCache = new SerializerCache();
/*      */     
/*  253 */     this._unknownTypeSerializer = src._unknownTypeSerializer;
/*  254 */     this._keySerializer = src._keySerializer;
/*  255 */     this._nullValueSerializer = src._nullValueSerializer;
/*  256 */     this._nullKeySerializer = src._nullKeySerializer;
/*      */     
/*  258 */     this._stdNullValueSerializer = src._stdNullValueSerializer;
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
/*      */   public void setDefaultKeySerializer(JsonSerializer<Object> ks)
/*      */   {
/*  275 */     if (ks == null) {
/*  276 */       throw new IllegalArgumentException("Can not pass null JsonSerializer");
/*      */     }
/*  278 */     this._keySerializer = ks;
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
/*      */   public void setNullValueSerializer(JsonSerializer<Object> nvs)
/*      */   {
/*  292 */     if (nvs == null) {
/*  293 */       throw new IllegalArgumentException("Can not pass null JsonSerializer");
/*      */     }
/*  295 */     this._nullValueSerializer = nvs;
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
/*      */   public void setNullKeySerializer(JsonSerializer<Object> nks)
/*      */   {
/*  309 */     if (nks == null) {
/*  310 */       throw new IllegalArgumentException("Can not pass null JsonSerializer");
/*      */     }
/*  312 */     this._nullKeySerializer = nks;
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
/*      */   public final SerializationConfig getConfig()
/*      */   {
/*  326 */     return this._config;
/*      */   }
/*      */   
/*      */   public final AnnotationIntrospector getAnnotationIntrospector() {
/*  330 */     return this._config.getAnnotationIntrospector();
/*      */   }
/*      */   
/*      */   public final TypeFactory getTypeFactory()
/*      */   {
/*  335 */     return this._config.getTypeFactory();
/*      */   }
/*      */   
/*      */   public final Class<?> getActiveView() {
/*  339 */     return this._serializationView;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public final Class<?> getSerializationView()
/*      */   {
/*  345 */     return this._serializationView;
/*      */   }
/*      */   
/*      */   public final boolean canOverrideAccessModifiers() {
/*  349 */     return this._config.canOverrideAccessModifiers();
/*      */   }
/*      */   
/*      */   public final boolean isEnabled(MapperFeature feature)
/*      */   {
/*  354 */     return this._config.isEnabled(feature);
/*      */   }
/*      */   
/*      */   public final JsonFormat.Value getDefaultPropertyFormat(Class<?> baseType)
/*      */   {
/*  359 */     return this._config.getDefaultPropertyFormat(baseType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final JsonInclude.Value getDefaultPropertyInclusion(Class<?> baseType)
/*      */   {
/*  366 */     return this._config.getDefaultPropertyInclusion();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  377 */     return this._config.getLocale();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZone getTimeZone()
/*      */   {
/*  388 */     return this._config.getTimeZone();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getAttribute(Object key)
/*      */   {
/*  399 */     return this._attributes.getAttribute(key);
/*      */   }
/*      */   
/*      */ 
/*      */   public SerializerProvider setAttribute(Object key, Object value)
/*      */   {
/*  405 */     this._attributes = this._attributes.withPerCallAttribute(key, value);
/*  406 */     return this;
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
/*      */   public final boolean isEnabled(SerializationFeature feature)
/*      */   {
/*  424 */     return this._config.isEnabled(feature);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean hasSerializationFeatures(int featureMask)
/*      */   {
/*  434 */     return this._config.hasSerializationFeatures(featureMask);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final FilterProvider getFilterProvider()
/*      */   {
/*  445 */     return this._config.getFilterProvider();
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
/*      */   public abstract WritableObjectId findObjectId(Object paramObject, ObjectIdGenerator<?> paramObjectIdGenerator);
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
/*      */   public JsonSerializer<Object> findValueSerializer(Class<?> valueType, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  491 */     JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer(valueType);
/*  492 */     if (ser == null)
/*      */     {
/*  494 */       ser = this._serializerCache.untypedValueSerializer(valueType);
/*  495 */       if (ser == null)
/*      */       {
/*  497 */         ser = this._serializerCache.untypedValueSerializer(this._config.constructType(valueType));
/*  498 */         if (ser == null)
/*      */         {
/*  500 */           ser = _createAndCacheUntypedSerializer(valueType);
/*      */           
/*  502 */           if (ser == null) {
/*  503 */             ser = getUnknownTypeSerializer(valueType);
/*      */             
/*      */ 
/*      */ 
/*      */ 
/*  508 */             return ser;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  514 */     return handleSecondaryContextualization(ser, property);
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
/*      */   public JsonSerializer<Object> findValueSerializer(JavaType valueType, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  532 */     JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer(valueType);
/*  533 */     if (ser == null) {
/*  534 */       ser = this._serializerCache.untypedValueSerializer(valueType);
/*  535 */       if (ser == null) {
/*  536 */         ser = _createAndCacheUntypedSerializer(valueType);
/*  537 */         if (ser == null) {
/*  538 */           ser = getUnknownTypeSerializer(valueType.getRawClass());
/*      */           
/*      */ 
/*      */ 
/*  542 */           return ser;
/*      */         }
/*      */       }
/*      */     }
/*  546 */     return handleSecondaryContextualization(ser, property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> findValueSerializer(Class<?> valueType)
/*      */     throws JsonMappingException
/*      */   {
/*  559 */     JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer(valueType);
/*  560 */     if (ser == null) {
/*  561 */       ser = this._serializerCache.untypedValueSerializer(valueType);
/*  562 */       if (ser == null) {
/*  563 */         ser = this._serializerCache.untypedValueSerializer(this._config.constructType(valueType));
/*  564 */         if (ser == null) {
/*  565 */           ser = _createAndCacheUntypedSerializer(valueType);
/*  566 */           if (ser == null) {
/*  567 */             ser = getUnknownTypeSerializer(valueType);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  575 */     return ser;
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
/*      */   public JsonSerializer<Object> findValueSerializer(JavaType valueType)
/*      */     throws JsonMappingException
/*      */   {
/*  589 */     JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer(valueType);
/*  590 */     if (ser == null) {
/*  591 */       ser = this._serializerCache.untypedValueSerializer(valueType);
/*  592 */       if (ser == null) {
/*  593 */         ser = _createAndCacheUntypedSerializer(valueType);
/*  594 */         if (ser == null) {
/*  595 */           ser = getUnknownTypeSerializer(valueType.getRawClass());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  602 */     return ser;
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
/*      */   public JsonSerializer<Object> findPrimaryPropertySerializer(JavaType valueType, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  621 */     JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer(valueType);
/*  622 */     if (ser == null) {
/*  623 */       ser = this._serializerCache.untypedValueSerializer(valueType);
/*  624 */       if (ser == null) {
/*  625 */         ser = _createAndCacheUntypedSerializer(valueType);
/*  626 */         if (ser == null) {
/*  627 */           ser = getUnknownTypeSerializer(valueType.getRawClass());
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  632 */           return ser;
/*      */         }
/*      */       }
/*      */     }
/*  636 */     return handlePrimaryContextualization(ser, property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> findPrimaryPropertySerializer(Class<?> valueType, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  647 */     JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer(valueType);
/*  648 */     if (ser == null) {
/*  649 */       ser = this._serializerCache.untypedValueSerializer(valueType);
/*  650 */       if (ser == null) {
/*  651 */         ser = this._serializerCache.untypedValueSerializer(this._config.constructType(valueType));
/*  652 */         if (ser == null) {
/*  653 */           ser = _createAndCacheUntypedSerializer(valueType);
/*  654 */           if (ser == null) {
/*  655 */             ser = getUnknownTypeSerializer(valueType);
/*      */             
/*      */ 
/*      */ 
/*  659 */             return ser;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  664 */     return handlePrimaryContextualization(ser, property);
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
/*      */   public JsonSerializer<Object> findTypedValueSerializer(Class<?> valueType, boolean cache, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  687 */     JsonSerializer<Object> ser = this._knownSerializers.typedValueSerializer(valueType);
/*  688 */     if (ser != null) {
/*  689 */       return ser;
/*      */     }
/*      */     
/*  692 */     ser = this._serializerCache.typedValueSerializer(valueType);
/*  693 */     if (ser != null) {
/*  694 */       return ser;
/*      */     }
/*      */     
/*      */ 
/*  698 */     ser = findValueSerializer(valueType, property);
/*  699 */     TypeSerializer typeSer = this._serializerFactory.createTypeSerializer(this._config, this._config.constructType(valueType));
/*      */     
/*  701 */     if (typeSer != null) {
/*  702 */       typeSer = typeSer.forProperty(property);
/*  703 */       ser = new TypeWrappedSerializer(typeSer, ser);
/*      */     }
/*  705 */     if (cache) {
/*  706 */       this._serializerCache.addTypedSerializer(valueType, ser);
/*      */     }
/*  708 */     return ser;
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
/*      */   public JsonSerializer<Object> findTypedValueSerializer(JavaType valueType, boolean cache, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  732 */     JsonSerializer<Object> ser = this._knownSerializers.typedValueSerializer(valueType);
/*  733 */     if (ser != null) {
/*  734 */       return ser;
/*      */     }
/*      */     
/*  737 */     ser = this._serializerCache.typedValueSerializer(valueType);
/*  738 */     if (ser != null) {
/*  739 */       return ser;
/*      */     }
/*      */     
/*      */ 
/*  743 */     ser = findValueSerializer(valueType, property);
/*  744 */     TypeSerializer typeSer = this._serializerFactory.createTypeSerializer(this._config, valueType);
/*  745 */     if (typeSer != null) {
/*  746 */       typeSer = typeSer.forProperty(property);
/*  747 */       ser = new TypeWrappedSerializer(typeSer, ser);
/*      */     }
/*  749 */     if (cache) {
/*  750 */       this._serializerCache.addTypedSerializer(valueType, ser);
/*      */     }
/*  752 */     return ser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TypeSerializer findTypeSerializer(JavaType javaType)
/*      */     throws JsonMappingException
/*      */   {
/*  763 */     return this._serializerFactory.createTypeSerializer(this._config, javaType);
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
/*      */   public JsonSerializer<Object> findKeySerializer(JavaType keyType, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  779 */     JsonSerializer<Object> ser = this._serializerFactory.createKeySerializer(this._config, keyType, this._keySerializer);
/*      */     
/*  781 */     return _handleContextualResolvable(ser, property);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> findKeySerializer(Class<?> rawKeyType, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  790 */     return findKeySerializer(this._config.constructType(rawKeyType), property);
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
/*      */   public JsonSerializer<Object> getDefaultNullKeySerializer()
/*      */   {
/*  803 */     return this._nullKeySerializer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JsonSerializer<Object> getDefaultNullValueSerializer()
/*      */   {
/*  810 */     return this._nullValueSerializer;
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
/*      */   public JsonSerializer<Object> findNullKeySerializer(JavaType serializationType, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  834 */     return this._nullKeySerializer;
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
/*      */   public JsonSerializer<Object> findNullValueSerializer(BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  850 */     return this._nullValueSerializer;
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
/*      */   public JsonSerializer<Object> getUnknownTypeSerializer(Class<?> unknownType)
/*      */   {
/*  867 */     if (unknownType == Object.class) {
/*  868 */       return this._unknownTypeSerializer;
/*      */     }
/*      */     
/*  871 */     return new UnknownSerializer(unknownType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isUnknownTypeSerializer(JsonSerializer<?> ser)
/*      */   {
/*  882 */     if ((ser == this._unknownTypeSerializer) || (ser == null)) {
/*  883 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  887 */     if ((isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS)) && 
/*  888 */       (ser.getClass() == UnknownSerializer.class)) {
/*  889 */       return true;
/*      */     }
/*      */     
/*  892 */     return false;
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
/*      */   public abstract JsonSerializer<Object> serializerInstance(Annotated paramAnnotated, Object paramObject)
/*      */     throws JsonMappingException;
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
/*      */   public JsonSerializer<?> handlePrimaryContextualization(JsonSerializer<?> ser, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  936 */     if ((ser != null) && 
/*  937 */       ((ser instanceof ContextualSerializer))) {
/*  938 */       ser = ((ContextualSerializer)ser).createContextual(this, property);
/*      */     }
/*      */     
/*  941 */     return ser;
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
/*      */   public JsonSerializer<?> handleSecondaryContextualization(JsonSerializer<?> ser, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  964 */     if ((ser != null) && 
/*  965 */       ((ser instanceof ContextualSerializer))) {
/*  966 */       ser = ((ContextualSerializer)ser).createContextual(this, property);
/*      */     }
/*      */     
/*  969 */     return ser;
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
/*      */   public final void defaultSerializeValue(Object value, JsonGenerator gen)
/*      */     throws IOException
/*      */   {
/*  987 */     if (value == null) {
/*  988 */       if (this._stdNullValueSerializer) {
/*  989 */         gen.writeNull();
/*      */       } else {
/*  991 */         this._nullValueSerializer.serialize(null, gen, this);
/*      */       }
/*      */     } else {
/*  994 */       Class<?> cls = value.getClass();
/*  995 */       findTypedValueSerializer(cls, true, null).serialize(value, gen, this);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void defaultSerializeField(String fieldName, Object value, JsonGenerator gen)
/*      */     throws IOException
/*      */   {
/* 1007 */     gen.writeFieldName(fieldName);
/* 1008 */     if (value == null)
/*      */     {
/*      */ 
/*      */ 
/* 1012 */       if (this._stdNullValueSerializer) {
/* 1013 */         gen.writeNull();
/*      */       } else {
/* 1015 */         this._nullValueSerializer.serialize(null, gen, this);
/*      */       }
/*      */     } else {
/* 1018 */       Class<?> cls = value.getClass();
/* 1019 */       findTypedValueSerializer(cls, true, null).serialize(value, gen, this);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void defaultSerializeDateValue(long timestamp, JsonGenerator gen)
/*      */     throws IOException
/*      */   {
/* 1033 */     if (isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)) {
/* 1034 */       gen.writeNumber(timestamp);
/*      */     } else {
/* 1036 */       gen.writeString(_dateFormat().format(new Date(timestamp)));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void defaultSerializeDateValue(Date date, JsonGenerator gen)
/*      */     throws IOException
/*      */   {
/* 1049 */     if (isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)) {
/* 1050 */       gen.writeNumber(date.getTime());
/*      */     } else {
/* 1052 */       gen.writeString(_dateFormat().format(date));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void defaultSerializeDateKey(long timestamp, JsonGenerator gen)
/*      */     throws IOException
/*      */   {
/* 1063 */     if (isEnabled(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)) {
/* 1064 */       gen.writeFieldName(String.valueOf(timestamp));
/*      */     } else {
/* 1066 */       gen.writeFieldName(_dateFormat().format(new Date(timestamp)));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void defaultSerializeDateKey(Date date, JsonGenerator gen)
/*      */     throws IOException
/*      */   {
/* 1077 */     if (isEnabled(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS)) {
/* 1078 */       gen.writeFieldName(String.valueOf(date.getTime()));
/*      */     } else {
/* 1080 */       gen.writeFieldName(_dateFormat().format(date));
/*      */     }
/*      */   }
/*      */   
/*      */   public final void defaultSerializeNull(JsonGenerator gen) throws IOException
/*      */   {
/* 1086 */     if (this._stdNullValueSerializer) {
/* 1087 */       gen.writeNull();
/*      */     } else {
/* 1089 */       this._nullValueSerializer.serialize(null, gen, this);
/*      */     }
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
/*      */   public JsonMappingException mappingException(String message, Object... args)
/*      */   {
/* 1107 */     if ((args != null) && (args.length > 0)) {
/* 1108 */       message = String.format(message, args);
/*      */     }
/* 1110 */     return JsonMappingException.from(getGenerator(), message);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonMappingException mappingException(Throwable t, String message, Object... args)
/*      */   {
/* 1121 */     if ((args != null) && (args.length > 0)) {
/* 1122 */       message = String.format(message, args);
/*      */     }
/* 1124 */     return JsonMappingException.from(getGenerator(), message, t);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void reportMappingProblem(String message, Object... args)
/*      */     throws JsonMappingException
/*      */   {
/* 1135 */     throw mappingException(message, args);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void reportMappingProblem(Throwable t, String message, Object... args)
/*      */     throws JsonMappingException
/*      */   {
/* 1146 */     throw mappingException(t, message, args);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T reportBadTypeDefinition(BeanDescription bean, String message, Object... args)
/*      */     throws JsonMappingException
/*      */   {
/* 1158 */     if ((args != null) && (args.length > 0)) {
/* 1159 */       message = String.format(message, args);
/*      */     }
/* 1161 */     String beanDesc = bean == null ? "N/A" : _desc(bean.getType().getGenericSignature());
/* 1162 */     throw mappingException("Invalid type definition for type %s: %s", new Object[] { beanDesc, message });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T reportBadPropertyDefinition(BeanDescription bean, BeanPropertyDefinition prop, String message, Object... args)
/*      */     throws JsonMappingException
/*      */   {
/* 1175 */     if ((args != null) && (args.length > 0)) {
/* 1176 */       message = String.format(message, args);
/*      */     }
/* 1178 */     String propName = prop == null ? "N/A" : _quotedString(prop.getName());
/* 1179 */     String beanDesc = bean == null ? "N/A" : _desc(bean.getType().getGenericSignature());
/* 1180 */     throw mappingException("Invalid definition for property %s (of type %s): %s", new Object[] { propName, beanDesc, message });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonGenerator getGenerator()
/*      */   {
/* 1188 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _reportIncompatibleRootType(Object value, JavaType rootType)
/*      */     throws IOException
/*      */   {
/* 1200 */     if (rootType.isPrimitive()) {
/* 1201 */       Class<?> wrapperType = ClassUtil.wrapperType(rootType.getRawClass());
/*      */       
/* 1203 */       if (wrapperType.isAssignableFrom(value.getClass())) {
/* 1204 */         return;
/*      */       }
/*      */     }
/* 1207 */     reportMappingProblem("Incompatible types: declared root type (%s) vs %s", new Object[] { rootType, value.getClass().getName() });
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
/*      */   protected JsonSerializer<Object> _findExplicitUntypedSerializer(Class<?> runtimeType)
/*      */     throws JsonMappingException
/*      */   {
/* 1222 */     JsonSerializer<Object> ser = this._knownSerializers.untypedValueSerializer(runtimeType);
/* 1223 */     if (ser == null)
/*      */     {
/* 1225 */       ser = this._serializerCache.untypedValueSerializer(runtimeType);
/* 1226 */       if (ser == null) {
/* 1227 */         ser = _createAndCacheUntypedSerializer(runtimeType);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1235 */     if (isUnknownTypeSerializer(ser)) {
/* 1236 */       return null;
/*      */     }
/* 1238 */     return ser;
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
/*      */   protected JsonSerializer<Object> _createAndCacheUntypedSerializer(Class<?> rawType)
/*      */     throws JsonMappingException
/*      */   {
/* 1255 */     JavaType fullType = this._config.constructType(rawType);
/*      */     JsonSerializer<Object> ser;
/*      */     try {
/* 1258 */       ser = _createUntypedSerializer(fullType);
/*      */ 
/*      */     }
/*      */     catch (IllegalArgumentException iae)
/*      */     {
/* 1263 */       reportMappingProblem(iae, iae.getMessage(), new Object[0]);
/* 1264 */       return null;
/*      */     }
/*      */     
/* 1267 */     if (ser != null)
/*      */     {
/* 1269 */       this._serializerCache.addAndResolveNonTypedSerializer(rawType, fullType, ser, this);
/*      */     }
/* 1271 */     return ser;
/*      */   }
/*      */   
/*      */   protected JsonSerializer<Object> _createAndCacheUntypedSerializer(JavaType type) throws JsonMappingException
/*      */   {
/*      */     JsonSerializer<Object> ser;
/*      */     try
/*      */     {
/* 1279 */       ser = _createUntypedSerializer(type);
/*      */ 
/*      */     }
/*      */     catch (IllegalArgumentException iae)
/*      */     {
/* 1284 */       reportMappingProblem(iae, iae.getMessage(), new Object[0]);
/* 1285 */       return null;
/*      */     }
/*      */     
/* 1288 */     if (ser != null)
/*      */     {
/* 1290 */       this._serializerCache.addAndResolveNonTypedSerializer(type, ser, this);
/*      */     }
/* 1292 */     return ser;
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   protected JsonSerializer<Object> _createUntypedSerializer(JavaType type)
/*      */     throws JsonMappingException
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 12	com/fasterxml/jackson/databind/SerializerProvider:_serializerCache	Lcom/fasterxml/jackson/databind/ser/SerializerCache;
/*      */     //   4: dup
/*      */     //   5: astore_2
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 9	com/fasterxml/jackson/databind/SerializerProvider:_serializerFactory	Lcom/fasterxml/jackson/databind/ser/SerializerFactory;
/*      */     //   11: aload_0
/*      */     //   12: aload_1
/*      */     //   13: invokevirtual 113	com/fasterxml/jackson/databind/ser/SerializerFactory:createSerializer	(Lcom/fasterxml/jackson/databind/SerializerProvider;Lcom/fasterxml/jackson/databind/JavaType;)Lcom/fasterxml/jackson/databind/JsonSerializer;
/*      */     //   16: aload_2
/*      */     //   17: monitorexit
/*      */     //   18: areturn
/*      */     //   19: astore_3
/*      */     //   20: aload_2
/*      */     //   21: monitorexit
/*      */     //   22: aload_3
/*      */     //   23: athrow
/*      */     // Line number table:
/*      */     //   Java source line #1306	-> byte code offset #0
/*      */     //   Java source line #1308	-> byte code offset #7
/*      */     //   Java source line #1309	-> byte code offset #19
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	24	0	this	SerializerProvider
/*      */     //   0	24	1	type	JavaType
/*      */     //   5	16	2	Ljava/lang/Object;	Object
/*      */     //   19	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	18	19	finally
/*      */     //   19	22	19	finally
/*      */   }
/*      */   
/*      */   protected JsonSerializer<Object> _handleContextualResolvable(JsonSerializer<?> ser, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/* 1321 */     if ((ser instanceof ResolvableSerializer)) {
/* 1322 */       ((ResolvableSerializer)ser).resolve(this);
/*      */     }
/* 1324 */     return handleSecondaryContextualization(ser, property);
/*      */   }
/*      */   
/*      */ 
/*      */   protected JsonSerializer<Object> _handleResolvable(JsonSerializer<?> ser)
/*      */     throws JsonMappingException
/*      */   {
/* 1331 */     if ((ser instanceof ResolvableSerializer)) {
/* 1332 */       ((ResolvableSerializer)ser).resolve(this);
/*      */     }
/* 1334 */     return ser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String _desc(Object value)
/*      */   {
/* 1344 */     if (value == null) {
/* 1345 */       return "N/A";
/*      */     }
/* 1347 */     return "'" + value + "'";
/*      */   }
/*      */   
/*      */   protected String _quotedString(Object value) {
/* 1351 */     if (value == null) {
/* 1352 */       return "N/A";
/*      */     }
/* 1354 */     return String.valueOf(value);
/*      */   }
/*      */   
/*      */   protected final DateFormat _dateFormat()
/*      */   {
/* 1359 */     if (this._dateFormat != null) {
/* 1360 */       return this._dateFormat;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1366 */     DateFormat df = this._config.getDateFormat();
/* 1367 */     this._dateFormat = (df = (DateFormat)df.clone());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1376 */     return df;
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\SerializerProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */