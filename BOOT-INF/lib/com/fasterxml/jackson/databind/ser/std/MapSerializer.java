/*      */ package com.fasterxml.jackson.databind.ser.std;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*      */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties.Value;
/*      */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*      */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.BeanProperty;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.JsonSerializer;
/*      */ import com.fasterxml.jackson.databind.SerializationFeature;
/*      */ import com.fasterxml.jackson.databind.SerializerProvider;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*      */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*      */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonMapFormatVisitor;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.PropertyFilter;
/*      */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*      */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap.SerializerAndMapResult;
/*      */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Type;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
/*      */ 
/*      */ @com.fasterxml.jackson.databind.annotation.JacksonStdImpl
/*      */ public class MapSerializer extends ContainerSerializer<Map<?, ?>> implements com.fasterxml.jackson.databind.ser.ContextualSerializer
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   37 */   protected static final JavaType UNSPECIFIED_TYPE = ;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final BeanProperty _property;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Set<String> _ignoredEntries;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean _valueTypeIsStatic;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JavaType _keyType;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JavaType _valueType;
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
/*      */ 
/*      */   protected JsonSerializer<Object> _valueSerializer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final TypeSerializer _valueTypeSerializer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected PropertySerializerMap _dynamicValueSerializers;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Object _filterId;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean _sortKeys;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Object _suppressableValue;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected MapSerializer(Set<String> ignoredEntries, JavaType keyType, JavaType valueType, boolean valueTypeIsStatic, TypeSerializer vts, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer)
/*      */   {
/*  127 */     super(Map.class, false);
/*  128 */     this._ignoredEntries = ((ignoredEntries == null) || (ignoredEntries.isEmpty()) ? null : ignoredEntries);
/*      */     
/*  130 */     this._keyType = keyType;
/*  131 */     this._valueType = valueType;
/*  132 */     this._valueTypeIsStatic = valueTypeIsStatic;
/*  133 */     this._valueTypeSerializer = vts;
/*  134 */     this._keySerializer = keySerializer;
/*  135 */     this._valueSerializer = valueSerializer;
/*  136 */     this._dynamicValueSerializers = PropertySerializerMap.emptyForProperties();
/*  137 */     this._property = null;
/*  138 */     this._filterId = null;
/*  139 */     this._sortKeys = false;
/*  140 */     this._suppressableValue = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void _ensureOverride()
/*      */   {
/*  147 */     if (getClass() != MapSerializer.class) {
/*  148 */       throw new IllegalStateException("Missing override in class " + getClass().getName());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected MapSerializer(MapSerializer src, BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, Set<String> ignoredEntries)
/*      */   {
/*  157 */     super(Map.class, false);
/*  158 */     this._ignoredEntries = ((ignoredEntries == null) || (ignoredEntries.isEmpty()) ? null : ignoredEntries);
/*      */     
/*  160 */     this._keyType = src._keyType;
/*  161 */     this._valueType = src._valueType;
/*  162 */     this._valueTypeIsStatic = src._valueTypeIsStatic;
/*  163 */     this._valueTypeSerializer = src._valueTypeSerializer;
/*  164 */     this._keySerializer = keySerializer;
/*  165 */     this._valueSerializer = valueSerializer;
/*  166 */     this._dynamicValueSerializers = src._dynamicValueSerializers;
/*  167 */     this._property = property;
/*  168 */     this._filterId = src._filterId;
/*  169 */     this._sortKeys = src._sortKeys;
/*  170 */     this._suppressableValue = src._suppressableValue;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   protected MapSerializer(MapSerializer src, TypeSerializer vts) {
/*  175 */     this(src, vts, src._suppressableValue);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected MapSerializer(MapSerializer src, TypeSerializer vts, Object suppressableValue)
/*      */   {
/*  184 */     super(Map.class, false);
/*  185 */     this._ignoredEntries = src._ignoredEntries;
/*  186 */     this._keyType = src._keyType;
/*  187 */     this._valueType = src._valueType;
/*  188 */     this._valueTypeIsStatic = src._valueTypeIsStatic;
/*  189 */     this._valueTypeSerializer = vts;
/*  190 */     this._keySerializer = src._keySerializer;
/*  191 */     this._valueSerializer = src._valueSerializer;
/*  192 */     this._dynamicValueSerializers = src._dynamicValueSerializers;
/*  193 */     this._property = src._property;
/*  194 */     this._filterId = src._filterId;
/*  195 */     this._sortKeys = src._sortKeys;
/*      */     
/*  197 */     if (suppressableValue == JsonInclude.Include.NON_ABSENT) {
/*  198 */       suppressableValue = this._valueType.isReferenceType() ? JsonInclude.Include.NON_EMPTY : JsonInclude.Include.NON_NULL;
/*      */     }
/*      */     
/*  201 */     this._suppressableValue = suppressableValue;
/*      */   }
/*      */   
/*      */   protected MapSerializer(MapSerializer src, Object filterId, boolean sortKeys)
/*      */   {
/*  206 */     super(Map.class, false);
/*  207 */     this._ignoredEntries = src._ignoredEntries;
/*  208 */     this._keyType = src._keyType;
/*  209 */     this._valueType = src._valueType;
/*  210 */     this._valueTypeIsStatic = src._valueTypeIsStatic;
/*  211 */     this._valueTypeSerializer = src._valueTypeSerializer;
/*  212 */     this._keySerializer = src._keySerializer;
/*  213 */     this._valueSerializer = src._valueSerializer;
/*  214 */     this._dynamicValueSerializers = src._dynamicValueSerializers;
/*  215 */     this._property = src._property;
/*  216 */     this._filterId = filterId;
/*  217 */     this._sortKeys = sortKeys;
/*  218 */     this._suppressableValue = src._suppressableValue;
/*      */   }
/*      */   
/*      */   public MapSerializer _withValueTypeSerializer(TypeSerializer vts)
/*      */   {
/*  223 */     if (this._valueTypeSerializer == vts) {
/*  224 */       return this;
/*      */     }
/*  226 */     _ensureOverride();
/*  227 */     return new MapSerializer(this, vts, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MapSerializer withResolved(BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer, Set<String> ignored, boolean sortKeys)
/*      */   {
/*  237 */     _ensureOverride();
/*  238 */     MapSerializer ser = new MapSerializer(this, property, keySerializer, valueSerializer, ignored);
/*  239 */     if (sortKeys != ser._sortKeys) {
/*  240 */       ser = new MapSerializer(ser, this._filterId, sortKeys);
/*      */     }
/*  242 */     return ser;
/*      */   }
/*      */   
/*      */   public MapSerializer withFilterId(Object filterId)
/*      */   {
/*  247 */     if (this._filterId == filterId) {
/*  248 */       return this;
/*      */     }
/*  250 */     _ensureOverride();
/*  251 */     return new MapSerializer(this, filterId, this._sortKeys);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MapSerializer withContentInclusion(Object suppressableValue)
/*      */   {
/*  261 */     if (suppressableValue == this._suppressableValue) {
/*  262 */       return this;
/*      */     }
/*  264 */     _ensureOverride();
/*  265 */     return new MapSerializer(this, this._valueTypeSerializer, suppressableValue);
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
/*      */   public static MapSerializer construct(String[] ignoredList, JavaType mapType, boolean staticValueType, TypeSerializer vts, JsonSerializer<Object> keySerializer, JsonSerializer<Object> valueSerializer, Object filterId)
/*      */   {
/*  279 */     Set<String> ignoredEntries = (ignoredList == null) || (ignoredList.length == 0) ? null : ArrayBuilders.arrayToSet(ignoredList);
/*      */     
/*  281 */     return construct(ignoredEntries, mapType, staticValueType, vts, keySerializer, valueSerializer, filterId);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static MapSerializer construct(Set<String> ignoredEntries, JavaType mapType, boolean staticValueType, TypeSerializer vts, JsonSerializer<Object> keySerializer, JsonSerializer<Object> valueSerializer, Object filterId)
/*      */   {
/*      */     JavaType keyType;
/*      */     
/*      */ 
/*      */     JavaType keyType;
/*      */     
/*      */     JavaType valueType;
/*      */     
/*  295 */     if (mapType == null) { JavaType valueType;
/*  296 */       keyType = valueType = UNSPECIFIED_TYPE;
/*      */     } else {
/*  298 */       keyType = mapType.getKeyType();
/*  299 */       valueType = mapType.getContentType();
/*      */     }
/*      */     
/*  302 */     if (!staticValueType) {
/*  303 */       staticValueType = (valueType != null) && (valueType.isFinal());
/*      */ 
/*      */     }
/*  306 */     else if (valueType.getRawClass() == Object.class) {
/*  307 */       staticValueType = false;
/*      */     }
/*      */     
/*  310 */     MapSerializer ser = new MapSerializer(ignoredEntries, keyType, valueType, staticValueType, vts, keySerializer, valueSerializer);
/*      */     
/*  312 */     if (filterId != null) {
/*  313 */       ser = ser.withFilterId(filterId);
/*      */     }
/*  315 */     return ser;
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
/*      */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  329 */     JsonSerializer<?> ser = null;
/*  330 */     JsonSerializer<?> keySer = null;
/*  331 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/*  332 */     AnnotatedMember propertyAcc = property == null ? null : property.getMember();
/*  333 */     Object suppressableValue = this._suppressableValue;
/*      */     
/*      */ 
/*  336 */     if ((propertyAcc != null) && (intr != null)) {
/*  337 */       Object serDef = intr.findKeySerializer(propertyAcc);
/*  338 */       if (serDef != null) {
/*  339 */         keySer = provider.serializerInstance(propertyAcc, serDef);
/*      */       }
/*  341 */       serDef = intr.findContentSerializer(propertyAcc);
/*  342 */       if (serDef != null) {
/*  343 */         ser = provider.serializerInstance(propertyAcc, serDef);
/*      */       }
/*      */     }
/*      */     
/*  347 */     JsonInclude.Value inclV = findIncludeOverrides(provider, property, Map.class);
/*  348 */     JsonInclude.Include incl = inclV.getContentInclusion();
/*  349 */     if ((incl != null) && (incl != JsonInclude.Include.USE_DEFAULTS)) {
/*  350 */       suppressableValue = incl;
/*      */     }
/*  352 */     if (ser == null) {
/*  353 */       ser = this._valueSerializer;
/*      */     }
/*      */     
/*  356 */     ser = findConvertingContentSerializer(provider, property, ser);
/*  357 */     if (ser == null)
/*      */     {
/*      */ 
/*      */ 
/*  361 */       if ((this._valueTypeIsStatic) && (!this._valueType.isJavaLangObject())) {
/*  362 */         ser = provider.findValueSerializer(this._valueType, property);
/*      */       }
/*      */     } else {
/*  365 */       ser = provider.handleSecondaryContextualization(ser, property);
/*      */     }
/*  367 */     if (keySer == null) {
/*  368 */       keySer = this._keySerializer;
/*      */     }
/*  370 */     if (keySer == null) {
/*  371 */       keySer = provider.findKeySerializer(this._keyType, property);
/*      */     } else {
/*  373 */       keySer = provider.handleSecondaryContextualization(keySer, property);
/*      */     }
/*  375 */     Set<String> ignored = this._ignoredEntries;
/*  376 */     boolean sortKeys = false;
/*  377 */     if ((intr != null) && (propertyAcc != null)) {
/*  378 */       JsonIgnoreProperties.Value ignorals = intr.findPropertyIgnorals(propertyAcc);
/*  379 */       if (ignorals != null) {
/*  380 */         Set<String> newIgnored = ignorals.findIgnoredForSerialization();
/*  381 */         if ((newIgnored != null) && (!newIgnored.isEmpty())) {
/*  382 */           ignored = ignored == null ? new HashSet() : new HashSet(ignored);
/*  383 */           for (String str : newIgnored) {
/*  384 */             ignored.add(str);
/*      */           }
/*      */         }
/*      */       }
/*  388 */       Boolean b = intr.findSerializationSortAlphabetically(propertyAcc);
/*  389 */       sortKeys = (b != null) && (b.booleanValue());
/*      */     }
/*  391 */     JsonFormat.Value format = findFormatOverrides(provider, property, Map.class);
/*  392 */     if (format != null) {
/*  393 */       Boolean B = format.getFeature(com.fasterxml.jackson.annotation.JsonFormat.Feature.WRITE_SORTED_MAP_ENTRIES);
/*  394 */       if (B != null) {
/*  395 */         sortKeys = B.booleanValue();
/*      */       }
/*      */     }
/*  398 */     MapSerializer mser = withResolved(property, keySer, ser, ignored, sortKeys);
/*  399 */     if (suppressableValue != this._suppressableValue) {
/*  400 */       mser = mser.withContentInclusion(suppressableValue);
/*      */     }
/*      */     
/*      */ 
/*  404 */     if (property != null) {
/*  405 */       AnnotatedMember m = property.getMember();
/*  406 */       if (m != null) {
/*  407 */         Object filterId = intr.findFilterId(m);
/*  408 */         if (filterId != null) {
/*  409 */           mser = mser.withFilterId(filterId);
/*      */         }
/*      */       }
/*      */     }
/*  413 */     return mser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType getContentType()
/*      */   {
/*  424 */     return this._valueType;
/*      */   }
/*      */   
/*      */   public JsonSerializer<?> getContentSerializer()
/*      */   {
/*  429 */     return this._valueSerializer;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isEmpty(SerializerProvider prov, Map<?, ?> value)
/*      */   {
/*  435 */     if ((value == null) || (value.isEmpty())) {
/*  436 */       return true;
/*      */     }
/*      */     
/*      */ 
/*  440 */     Object supp = this._suppressableValue;
/*      */     
/*  442 */     if ((supp == null) || (supp == JsonInclude.Include.ALWAYS)) {
/*  443 */       return false;
/*      */     }
/*  445 */     JsonSerializer<Object> valueSer = this._valueSerializer;
/*  446 */     if (valueSer != null) {
/*  447 */       for (Object elemValue : value.values()) {
/*  448 */         if ((elemValue != null) && (!valueSer.isEmpty(prov, elemValue))) {
/*  449 */           return false;
/*      */         }
/*      */       }
/*  452 */       return true;
/*      */     }
/*      */     
/*  455 */     PropertySerializerMap serializers = this._dynamicValueSerializers;
/*  456 */     for (Object elemValue : value.values()) {
/*  457 */       if (elemValue != null)
/*      */       {
/*      */ 
/*  460 */         Class<?> cc = elemValue.getClass();
/*      */         
/*      */ 
/*  463 */         valueSer = serializers.serializerFor(cc);
/*  464 */         if (valueSer == null) {
/*      */           try {
/*  466 */             valueSer = _findAndAddDynamic(serializers, cc, prov);
/*      */           }
/*      */           catch (JsonMappingException e) {
/*  469 */             return false;
/*      */           }
/*  471 */           serializers = this._dynamicValueSerializers;
/*      */         }
/*  473 */         if (!valueSer.isEmpty(prov, elemValue))
/*  474 */           return false;
/*      */       }
/*      */     }
/*  477 */     return true;
/*      */   }
/*      */   
/*      */   public boolean hasSingleElement(Map<?, ?> value)
/*      */   {
/*  482 */     return value.size() == 1;
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
/*      */   public JsonSerializer<?> getKeySerializer()
/*      */   {
/*  502 */     return this._keySerializer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void serialize(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider)
/*      */     throws IOException
/*      */   {
/*  515 */     gen.writeStartObject(value);
/*  516 */     if (!value.isEmpty()) {
/*  517 */       Object suppressableValue = this._suppressableValue;
/*  518 */       if (suppressableValue == JsonInclude.Include.ALWAYS) {
/*  519 */         suppressableValue = null;
/*  520 */       } else if ((suppressableValue == null) && 
/*  521 */         (!provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES))) {
/*  522 */         suppressableValue = JsonInclude.Include.NON_NULL;
/*      */       }
/*      */       
/*  525 */       if ((this._sortKeys) || (provider.isEnabled(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS))) {
/*  526 */         value = _orderEntries(value, gen, provider, suppressableValue);
/*      */       }
/*      */       PropertyFilter pf;
/*  529 */       if ((this._filterId != null) && ((pf = findPropertyFilter(provider, this._filterId, value)) != null)) {
/*  530 */         serializeFilteredFields(value, gen, provider, pf, suppressableValue);
/*  531 */       } else if (suppressableValue != null) {
/*  532 */         serializeOptionalFields(value, gen, provider, suppressableValue);
/*  533 */       } else if (this._valueSerializer != null) {
/*  534 */         serializeFieldsUsing(value, gen, provider, this._valueSerializer);
/*      */       } else {
/*  536 */         serializeFields(value, gen, provider);
/*      */       }
/*      */     }
/*  539 */     gen.writeEndObject();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void serializeWithType(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*      */     throws IOException
/*      */   {
/*  547 */     typeSer.writeTypePrefixForObject(value, gen);
/*      */     
/*  549 */     gen.setCurrentValue(value);
/*  550 */     if (!value.isEmpty()) {
/*  551 */       Object suppressableValue = this._suppressableValue;
/*  552 */       if (suppressableValue == JsonInclude.Include.ALWAYS) {
/*  553 */         suppressableValue = null;
/*  554 */       } else if ((suppressableValue == null) && 
/*  555 */         (!provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES))) {
/*  556 */         suppressableValue = JsonInclude.Include.NON_NULL;
/*      */       }
/*      */       
/*  559 */       if ((this._sortKeys) || (provider.isEnabled(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS))) {
/*  560 */         value = _orderEntries(value, gen, provider, suppressableValue);
/*      */       }
/*      */       PropertyFilter pf;
/*  563 */       if ((this._filterId != null) && ((pf = findPropertyFilter(provider, this._filterId, value)) != null)) {
/*  564 */         serializeFilteredFields(value, gen, provider, pf, suppressableValue);
/*  565 */       } else if (suppressableValue != null) {
/*  566 */         serializeOptionalFields(value, gen, provider, suppressableValue);
/*  567 */       } else if (this._valueSerializer != null) {
/*  568 */         serializeFieldsUsing(value, gen, provider, this._valueSerializer);
/*      */       } else {
/*  570 */         serializeFields(value, gen, provider);
/*      */       }
/*      */     }
/*  573 */     typeSer.writeTypeSuffixForObject(value, gen);
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
/*      */   public void serializeFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider)
/*      */     throws IOException
/*      */   {
/*  591 */     if (this._valueTypeSerializer != null) {
/*  592 */       serializeTypedFields(value, gen, provider, null);
/*  593 */       return;
/*      */     }
/*  595 */     JsonSerializer<Object> keySerializer = this._keySerializer;
/*  596 */     Set<String> ignored = this._ignoredEntries;
/*      */     
/*  598 */     PropertySerializerMap serializers = this._dynamicValueSerializers;
/*      */     
/*  600 */     for (Map.Entry<?, ?> entry : value.entrySet()) {
/*  601 */       Object valueElem = entry.getValue();
/*      */       
/*  603 */       Object keyElem = entry.getKey();
/*      */       
/*  605 */       if (keyElem == null) {
/*  606 */         provider.findNullKeySerializer(this._keyType, this._property).serialize(null, gen, provider);
/*      */       }
/*      */       else {
/*  609 */         if ((ignored != null) && (ignored.contains(keyElem))) continue;
/*  610 */         keySerializer.serialize(keyElem, gen, provider);
/*      */       }
/*      */       
/*      */ 
/*  614 */       if (valueElem == null) {
/*  615 */         provider.defaultSerializeNull(gen);
/*      */       }
/*      */       else {
/*  618 */         JsonSerializer<Object> serializer = this._valueSerializer;
/*  619 */         if (serializer == null) {
/*  620 */           Class<?> cc = valueElem.getClass();
/*  621 */           serializer = serializers.serializerFor(cc);
/*  622 */           if (serializer == null) {
/*  623 */             if (this._valueType.hasGenericTypes()) {
/*  624 */               serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._valueType, cc), provider);
/*      */             }
/*      */             else {
/*  627 */               serializer = _findAndAddDynamic(serializers, cc, provider);
/*      */             }
/*  629 */             serializers = this._dynamicValueSerializers;
/*      */           }
/*      */         }
/*      */         try {
/*  633 */           serializer.serialize(valueElem, gen, provider);
/*      */         }
/*      */         catch (Exception e) {
/*  636 */           String keyDesc = "" + keyElem;
/*  637 */           wrapAndThrow(provider, e, value, keyDesc);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void serializeOptionalFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, Object suppressableValue)
/*      */     throws IOException
/*      */   {
/*  650 */     if (this._valueTypeSerializer != null) {
/*  651 */       serializeTypedFields(value, gen, provider, suppressableValue);
/*  652 */       return;
/*      */     }
/*  654 */     Set<String> ignored = this._ignoredEntries;
/*  655 */     PropertySerializerMap serializers = this._dynamicValueSerializers;
/*      */     
/*  657 */     for (Map.Entry<?, ?> entry : value.entrySet())
/*      */     {
/*  659 */       Object keyElem = entry.getKey();
/*      */       JsonSerializer<Object> keySerializer;
/*  661 */       JsonSerializer<Object> keySerializer; if (keyElem == null) {
/*  662 */         keySerializer = provider.findNullKeySerializer(this._keyType, this._property);
/*      */       } else {
/*  664 */         if ((ignored != null) && (ignored.contains(keyElem))) continue;
/*  665 */         keySerializer = this._keySerializer;
/*      */       }
/*      */       
/*      */ 
/*  669 */       Object valueElem = entry.getValue();
/*      */       JsonSerializer<Object> valueSer;
/*  671 */       if (valueElem == null) {
/*  672 */         if (suppressableValue != null) {
/*      */           continue;
/*      */         }
/*  675 */         JsonSerializer<Object> valueSer = provider.getDefaultNullValueSerializer();
/*      */       } else {
/*  677 */         valueSer = this._valueSerializer;
/*  678 */         if (valueSer == null) {
/*  679 */           Class<?> cc = valueElem.getClass();
/*  680 */           valueSer = serializers.serializerFor(cc);
/*  681 */           if (valueSer == null) {
/*  682 */             if (this._valueType.hasGenericTypes()) {
/*  683 */               valueSer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._valueType, cc), provider);
/*      */             }
/*      */             else {
/*  686 */               valueSer = _findAndAddDynamic(serializers, cc, provider);
/*      */             }
/*  688 */             serializers = this._dynamicValueSerializers;
/*      */           }
/*      */         }
/*      */         
/*  692 */         if ((suppressableValue == JsonInclude.Include.NON_EMPTY) && (valueSer.isEmpty(provider, valueElem))) {
/*      */           continue;
/*      */         }
/*      */       }
/*      */       
/*      */       try
/*      */       {
/*  699 */         keySerializer.serialize(keyElem, gen, provider);
/*  700 */         valueSer.serialize(valueElem, gen, provider);
/*      */       } catch (Exception e) {
/*  702 */         String keyDesc = "" + keyElem;
/*  703 */         wrapAndThrow(provider, e, value, keyDesc);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void serializeFieldsUsing(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, JsonSerializer<Object> ser)
/*      */     throws IOException
/*      */   {
/*  717 */     JsonSerializer<Object> keySerializer = this._keySerializer;
/*  718 */     Set<String> ignored = this._ignoredEntries;
/*  719 */     TypeSerializer typeSer = this._valueTypeSerializer;
/*      */     
/*  721 */     for (Map.Entry<?, ?> entry : value.entrySet()) {
/*  722 */       Object keyElem = entry.getKey();
/*  723 */       if ((ignored == null) || (!ignored.contains(keyElem)))
/*      */       {
/*  725 */         if (keyElem == null) {
/*  726 */           provider.findNullKeySerializer(this._keyType, this._property).serialize(null, gen, provider);
/*      */         } else {
/*  728 */           keySerializer.serialize(keyElem, gen, provider);
/*      */         }
/*  730 */         Object valueElem = entry.getValue();
/*  731 */         if (valueElem == null) {
/*  732 */           provider.defaultSerializeNull(gen);
/*      */         } else {
/*      */           try {
/*  735 */             if (typeSer == null) {
/*  736 */               ser.serialize(valueElem, gen, provider);
/*      */             } else {
/*  738 */               ser.serializeWithType(valueElem, gen, provider, typeSer);
/*      */             }
/*      */           } catch (Exception e) {
/*  741 */             String keyDesc = "" + keyElem;
/*  742 */             wrapAndThrow(provider, e, value, keyDesc);
/*      */           }
/*      */         }
/*      */       }
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
/*      */   public void serializeFilteredFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, PropertyFilter filter, Object suppressableValue)
/*      */     throws IOException
/*      */   {
/*  759 */     Set<String> ignored = this._ignoredEntries;
/*      */     
/*  761 */     PropertySerializerMap serializers = this._dynamicValueSerializers;
/*  762 */     MapProperty prop = new MapProperty(this._valueTypeSerializer, this._property);
/*      */     
/*  764 */     for (Map.Entry<?, ?> entry : value.entrySet())
/*      */     {
/*  766 */       Object keyElem = entry.getKey();
/*  767 */       if ((ignored == null) || (!ignored.contains(keyElem))) {
/*      */         JsonSerializer<Object> keySerializer;
/*      */         JsonSerializer<Object> keySerializer;
/*  770 */         if (keyElem == null) {
/*  771 */           keySerializer = provider.findNullKeySerializer(this._keyType, this._property);
/*      */         } else {
/*  773 */           keySerializer = this._keySerializer;
/*      */         }
/*      */         
/*  776 */         Object valueElem = entry.getValue();
/*      */         
/*      */         JsonSerializer<Object> valueSer;
/*      */         
/*  780 */         if (valueElem == null) {
/*  781 */           if (suppressableValue != null) {
/*      */             continue;
/*      */           }
/*  784 */           JsonSerializer<Object> valueSer = provider.getDefaultNullValueSerializer();
/*      */         } else {
/*  786 */           valueSer = this._valueSerializer;
/*  787 */           if (valueSer == null) {
/*  788 */             Class<?> cc = valueElem.getClass();
/*  789 */             valueSer = serializers.serializerFor(cc);
/*  790 */             if (valueSer == null) {
/*  791 */               if (this._valueType.hasGenericTypes()) {
/*  792 */                 valueSer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._valueType, cc), provider);
/*      */               }
/*      */               else {
/*  795 */                 valueSer = _findAndAddDynamic(serializers, cc, provider);
/*      */               }
/*  797 */               serializers = this._dynamicValueSerializers;
/*      */             }
/*      */           }
/*      */           
/*  801 */           if ((suppressableValue == JsonInclude.Include.NON_EMPTY) && (valueSer.isEmpty(provider, valueElem))) {
/*      */             continue;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  807 */         prop.reset(keyElem, keySerializer, valueSer);
/*      */         try {
/*  809 */           filter.serializeAsField(valueElem, gen, provider, prop);
/*      */         } catch (Exception e) {
/*  811 */           String keyDesc = "" + keyElem;
/*  812 */           wrapAndThrow(provider, e, value, keyDesc);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public void serializeFilteredFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, PropertyFilter filter) throws IOException {
/*  820 */     serializeFilteredFields(value, gen, provider, filter, provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES) ? null : JsonInclude.Include.NON_NULL);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void serializeTypedFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider, Object suppressableValue)
/*      */     throws IOException
/*      */   {
/*  831 */     Set<String> ignored = this._ignoredEntries;
/*  832 */     PropertySerializerMap serializers = this._dynamicValueSerializers;
/*      */     
/*  834 */     for (Map.Entry<?, ?> entry : value.entrySet()) {
/*  835 */       Object keyElem = entry.getKey();
/*      */       JsonSerializer<Object> keySerializer;
/*  837 */       JsonSerializer<Object> keySerializer; if (keyElem == null) {
/*  838 */         keySerializer = provider.findNullKeySerializer(this._keyType, this._property);
/*      */       }
/*      */       else {
/*  841 */         if ((ignored != null) && (ignored.contains(keyElem))) continue;
/*  842 */         keySerializer = this._keySerializer;
/*      */       }
/*  844 */       Object valueElem = entry.getValue();
/*      */       
/*      */       JsonSerializer<Object> valueSer;
/*      */       
/*  848 */       if (valueElem == null) {
/*  849 */         if (suppressableValue != null) {
/*      */           continue;
/*      */         }
/*  852 */         JsonSerializer<Object> valueSer = provider.getDefaultNullValueSerializer();
/*      */       } else {
/*  854 */         valueSer = this._valueSerializer;
/*  855 */         Class<?> cc = valueElem.getClass();
/*  856 */         valueSer = serializers.serializerFor(cc);
/*  857 */         if (valueSer == null) {
/*  858 */           if (this._valueType.hasGenericTypes()) {
/*  859 */             valueSer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._valueType, cc), provider);
/*      */           }
/*      */           else {
/*  862 */             valueSer = _findAndAddDynamic(serializers, cc, provider);
/*      */           }
/*  864 */           serializers = this._dynamicValueSerializers;
/*      */         }
/*      */         
/*  867 */         if ((suppressableValue == JsonInclude.Include.NON_EMPTY) && (valueSer.isEmpty(provider, valueElem))) {
/*      */           continue;
/*      */         }
/*      */       }
/*      */       
/*  872 */       keySerializer.serialize(keyElem, gen, provider);
/*      */       try {
/*  874 */         valueSer.serializeWithType(valueElem, gen, provider, this._valueTypeSerializer);
/*      */       } catch (Exception e) {
/*  876 */         String keyDesc = "" + keyElem;
/*  877 */         wrapAndThrow(provider, e, value, keyDesc);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   protected void serializeTypedFields(Map<?, ?> value, JsonGenerator gen, SerializerProvider provider) throws IOException
/*      */   {
/*  885 */     serializeTypedFields(value, gen, provider, provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES) ? null : JsonInclude.Include.NON_NULL);
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
/*      */   public com.fasterxml.jackson.databind.JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*      */   {
/*  900 */     return createSchemaNode("object", true);
/*      */   }
/*      */   
/*      */ 
/*      */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*      */     throws JsonMappingException
/*      */   {
/*  907 */     JsonMapFormatVisitor v2 = visitor == null ? null : visitor.expectMapFormat(typeHint);
/*  908 */     if (v2 != null) {
/*  909 */       v2.keyFormat(this._keySerializer, this._keyType);
/*  910 */       JsonSerializer<?> valueSer = this._valueSerializer;
/*  911 */       if (valueSer == null) {
/*  912 */         valueSer = _findAndAddDynamic(this._dynamicValueSerializers, this._valueType, visitor.getProvider());
/*      */       }
/*      */       
/*  915 */       v2.valueFormat(valueSer, this._valueType);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider)
/*      */     throws JsonMappingException
/*      */   {
/*  928 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/*      */     
/*  930 */     if (map != result.map) {
/*  931 */       this._dynamicValueSerializers = result.map;
/*      */     }
/*  933 */     return result.serializer;
/*      */   }
/*      */   
/*      */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider)
/*      */     throws JsonMappingException
/*      */   {
/*  939 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/*  940 */     if (map != result.map) {
/*  941 */       this._dynamicValueSerializers = result.map;
/*      */     }
/*  943 */     return result.serializer;
/*      */   }
/*      */   
/*      */ 
/*      */   protected Map<?, ?> _orderEntries(Map<?, ?> input, JsonGenerator gen, SerializerProvider provider, Object suppressableValue)
/*      */     throws IOException
/*      */   {
/*  950 */     if ((input instanceof java.util.SortedMap)) {
/*  951 */       return input;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  957 */     if (_hasNullKey(input)) {
/*  958 */       TreeMap<Object, Object> result = new TreeMap();
/*  959 */       for (Map.Entry<?, ?> entry : input.entrySet()) {
/*  960 */         Object key = entry.getKey();
/*  961 */         if (key == null) {
/*  962 */           _writeNullKeyedEntry(gen, provider, suppressableValue, entry.getValue());
/*      */         }
/*      */         else
/*  965 */           result.put(key, entry.getValue());
/*      */       }
/*  967 */       return result;
/*      */     }
/*  969 */     return new TreeMap(input);
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
/*      */   protected boolean _hasNullKey(Map<?, ?> input)
/*      */   {
/*  985 */     return ((input instanceof HashMap)) && (input.containsKey(null));
/*      */   }
/*      */   
/*      */   protected void _writeNullKeyedEntry(JsonGenerator gen, SerializerProvider provider, Object suppressableValue, Object value)
/*      */     throws IOException
/*      */   {
/*  991 */     JsonSerializer<Object> keySerializer = provider.findNullKeySerializer(this._keyType, this._property);
/*      */     JsonSerializer<Object> valueSer;
/*  993 */     JsonSerializer<Object> valueSer; if (value == null) {
/*  994 */       if (suppressableValue != null) {
/*  995 */         return;
/*      */       }
/*  997 */       valueSer = provider.getDefaultNullValueSerializer();
/*      */     } else {
/*  999 */       valueSer = this._valueSerializer;
/* 1000 */       if (valueSer == null) {
/* 1001 */         Class<?> cc = value.getClass();
/* 1002 */         valueSer = this._dynamicValueSerializers.serializerFor(cc);
/* 1003 */         if (valueSer == null) {
/* 1004 */           if (this._valueType.hasGenericTypes()) {
/* 1005 */             valueSer = _findAndAddDynamic(this._dynamicValueSerializers, provider.constructSpecializedType(this._valueType, cc), provider);
/*      */           }
/*      */           else {
/* 1008 */             valueSer = _findAndAddDynamic(this._dynamicValueSerializers, cc, provider);
/*      */           }
/*      */         }
/*      */       }
/*      */       
/* 1013 */       if ((suppressableValue == JsonInclude.Include.NON_EMPTY) && (valueSer.isEmpty(provider, value)))
/*      */       {
/* 1015 */         return;
/*      */       }
/*      */     }
/*      */     try
/*      */     {
/* 1020 */       keySerializer.serialize(null, gen, provider);
/* 1021 */       valueSer.serialize(value, gen, provider);
/*      */     } catch (Exception e) {
/* 1023 */       String keyDesc = "";
/* 1024 */       wrapAndThrow(provider, e, value, keyDesc);
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\MapSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */