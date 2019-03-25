/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import java.io.IOException;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class MapEntrySerializer
/*     */   extends ContainerSerializer<Map.Entry<?, ?>>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   protected final BeanProperty _property;
/*     */   protected final boolean _valueTypeIsStatic;
/*     */   protected final JavaType _entryType;
/*     */   protected final JavaType _keyType;
/*     */   protected final JavaType _valueType;
/*     */   protected JsonSerializer<Object> _keySerializer;
/*     */   protected JsonSerializer<Object> _valueSerializer;
/*     */   protected final TypeSerializer _valueTypeSerializer;
/*     */   protected PropertySerializerMap _dynamicValueSerializers;
/*     */   
/*     */   public MapEntrySerializer(JavaType type, JavaType keyType, JavaType valueType, boolean staticTyping, TypeSerializer vts, BeanProperty property)
/*     */   {
/*  69 */     super(type);
/*  70 */     this._entryType = type;
/*  71 */     this._keyType = keyType;
/*  72 */     this._valueType = valueType;
/*  73 */     this._valueTypeIsStatic = staticTyping;
/*  74 */     this._valueTypeSerializer = vts;
/*  75 */     this._property = property;
/*  76 */     this._dynamicValueSerializers = PropertySerializerMap.emptyForProperties();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapEntrySerializer(MapEntrySerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> keySer, JsonSerializer<?> valueSer)
/*     */   {
/*  84 */     super(Map.class, false);
/*  85 */     this._entryType = src._entryType;
/*  86 */     this._keyType = src._keyType;
/*  87 */     this._valueType = src._valueType;
/*  88 */     this._valueTypeIsStatic = src._valueTypeIsStatic;
/*  89 */     this._valueTypeSerializer = src._valueTypeSerializer;
/*  90 */     this._keySerializer = keySer;
/*  91 */     this._valueSerializer = valueSer;
/*  92 */     this._dynamicValueSerializers = src._dynamicValueSerializers;
/*  93 */     this._property = src._property;
/*     */   }
/*     */   
/*     */   public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/*  98 */     return new MapEntrySerializer(this, this._property, vts, this._keySerializer, this._valueSerializer);
/*     */   }
/*     */   
/*     */   public MapEntrySerializer withResolved(BeanProperty property, JsonSerializer<?> keySerializer, JsonSerializer<?> valueSerializer)
/*     */   {
/* 103 */     return new MapEntrySerializer(this, property, this._valueTypeSerializer, keySerializer, valueSerializer);
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 110 */     JsonSerializer<?> ser = null;
/* 111 */     JsonSerializer<?> keySer = null;
/* 112 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/* 113 */     AnnotatedMember propertyAcc = property == null ? null : property.getMember();
/*     */     
/*     */ 
/* 116 */     if ((propertyAcc != null) && (intr != null)) {
/* 117 */       Object serDef = intr.findKeySerializer(propertyAcc);
/* 118 */       if (serDef != null) {
/* 119 */         keySer = provider.serializerInstance(propertyAcc, serDef);
/*     */       }
/* 121 */       serDef = intr.findContentSerializer(propertyAcc);
/* 122 */       if (serDef != null) {
/* 123 */         ser = provider.serializerInstance(propertyAcc, serDef);
/*     */       }
/*     */     }
/* 126 */     if (ser == null) {
/* 127 */       ser = this._valueSerializer;
/*     */     }
/*     */     
/* 130 */     ser = findConvertingContentSerializer(provider, property, ser);
/* 131 */     if (ser == null)
/*     */     {
/*     */ 
/*     */ 
/* 135 */       if ((this._valueTypeIsStatic) && (!this._valueType.isJavaLangObject())) {
/* 136 */         ser = provider.findValueSerializer(this._valueType, property);
/*     */       }
/*     */     } else {
/* 139 */       ser = provider.handleSecondaryContextualization(ser, property);
/*     */     }
/* 141 */     if (keySer == null) {
/* 142 */       keySer = this._keySerializer;
/*     */     }
/* 144 */     if (keySer == null) {
/* 145 */       keySer = provider.findKeySerializer(this._keyType, property);
/*     */     } else {
/* 147 */       keySer = provider.handleSecondaryContextualization(keySer, property);
/*     */     }
/* 149 */     MapEntrySerializer mser = withResolved(property, keySer, ser);
/*     */     
/* 151 */     return mser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType getContentType()
/*     */   {
/* 162 */     return this._valueType;
/*     */   }
/*     */   
/*     */   public JsonSerializer<?> getContentSerializer()
/*     */   {
/* 167 */     return this._valueSerializer;
/*     */   }
/*     */   
/*     */   public boolean hasSingleElement(Map.Entry<?, ?> value)
/*     */   {
/* 172 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isEmpty(SerializerProvider prov, Map.Entry<?, ?> value)
/*     */   {
/* 177 */     return value == null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serialize(Map.Entry<?, ?> value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 190 */     gen.writeStartObject(value);
/* 191 */     if (this._valueSerializer != null) {
/* 192 */       serializeUsing(value, gen, provider, this._valueSerializer);
/*     */     } else {
/* 194 */       serializeDynamic(value, gen, provider);
/*     */     }
/* 196 */     gen.writeEndObject();
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeWithType(Map.Entry<?, ?> value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 203 */     typeSer.writeTypePrefixForObject(value, gen);
/*     */     
/* 205 */     gen.setCurrentValue(value);
/* 206 */     if (this._valueSerializer != null) {
/* 207 */       serializeUsing(value, gen, provider, this._valueSerializer);
/*     */     } else {
/* 209 */       serializeDynamic(value, gen, provider);
/*     */     }
/* 211 */     typeSer.writeTypeSuffixForObject(value, gen);
/*     */   }
/*     */   
/*     */   protected void serializeDynamic(Map.Entry<?, ?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 217 */     JsonSerializer<Object> keySerializer = this._keySerializer;
/* 218 */     boolean skipNulls = !provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES);
/* 219 */     TypeSerializer vts = this._valueTypeSerializer;
/*     */     
/* 221 */     PropertySerializerMap serializers = this._dynamicValueSerializers;
/*     */     
/* 223 */     Object valueElem = value.getValue();
/* 224 */     Object keyElem = value.getKey();
/* 225 */     if (keyElem == null) {
/* 226 */       provider.findNullKeySerializer(this._keyType, this._property).serialize(null, jgen, provider);
/*     */     }
/*     */     else {
/* 229 */       if ((skipNulls) && (valueElem == null)) return;
/* 230 */       keySerializer.serialize(keyElem, jgen, provider);
/*     */     }
/*     */     
/* 233 */     if (valueElem == null) {
/* 234 */       provider.defaultSerializeNull(jgen);
/*     */     } else {
/* 236 */       Class<?> cc = valueElem.getClass();
/* 237 */       JsonSerializer<Object> ser = serializers.serializerFor(cc);
/* 238 */       if (ser == null) {
/* 239 */         if (this._valueType.hasGenericTypes()) {
/* 240 */           ser = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._valueType, cc), provider);
/*     */         }
/*     */         else {
/* 243 */           ser = _findAndAddDynamic(serializers, cc, provider);
/*     */         }
/* 245 */         serializers = this._dynamicValueSerializers;
/*     */       }
/*     */       try {
/* 248 */         if (vts == null) {
/* 249 */           ser.serialize(valueElem, jgen, provider);
/*     */         } else {
/* 251 */           ser.serializeWithType(valueElem, jgen, provider, vts);
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 255 */         String keyDesc = "" + keyElem;
/* 256 */         wrapAndThrow(provider, e, value, keyDesc);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void serializeUsing(Map.Entry<?, ?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 270 */     JsonSerializer<Object> keySerializer = this._keySerializer;
/* 271 */     TypeSerializer vts = this._valueTypeSerializer;
/* 272 */     boolean skipNulls = !provider.isEnabled(SerializationFeature.WRITE_NULL_MAP_VALUES);
/*     */     
/* 274 */     Object valueElem = value.getValue();
/* 275 */     Object keyElem = value.getKey();
/* 276 */     if (keyElem == null) {
/* 277 */       provider.findNullKeySerializer(this._keyType, this._property).serialize(null, jgen, provider);
/*     */     }
/*     */     else {
/* 280 */       if ((skipNulls) && (valueElem == null)) return;
/* 281 */       keySerializer.serialize(keyElem, jgen, provider);
/*     */     }
/* 283 */     if (valueElem == null) {
/* 284 */       provider.defaultSerializeNull(jgen);
/*     */     } else {
/*     */       try {
/* 287 */         if (vts == null) {
/* 288 */           ser.serialize(valueElem, jgen, provider);
/*     */         } else {
/* 290 */           ser.serializeWithType(valueElem, jgen, provider, vts);
/*     */         }
/*     */       }
/*     */       catch (Exception e) {
/* 294 */         String keyDesc = "" + keyElem;
/* 295 */         wrapAndThrow(provider, e, value, keyDesc);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 309 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/* 310 */     if (map != result.map) {
/* 311 */       this._dynamicValueSerializers = result.map;
/*     */     }
/* 313 */     return result.serializer;
/*     */   }
/*     */   
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 319 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/* 320 */     if (map != result.map) {
/* 321 */       this._dynamicValueSerializers = result.map;
/*     */     }
/* 323 */     return result.serializer;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\impl\MapEntrySerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */