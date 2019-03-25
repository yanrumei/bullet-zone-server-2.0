/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Feature;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap.SerializerAndMapResult;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AsArraySerializerBase<T>
/*     */   extends ContainerSerializer<T>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   protected final JavaType _elementType;
/*     */   protected final BeanProperty _property;
/*     */   protected final boolean _staticTyping;
/*     */   protected final Boolean _unwrapSingle;
/*     */   protected final TypeSerializer _valueTypeSerializer;
/*     */   protected final JsonSerializer<Object> _elementSerializer;
/*     */   protected PropertySerializerMap _dynamicSerializers;
/*     */   
/*     */   protected AsArraySerializerBase(Class<?> cls, JavaType et, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> elementSerializer)
/*     */   {
/*  79 */     super(cls, false);
/*  80 */     this._elementType = et;
/*     */     
/*  82 */     this._staticTyping = ((staticTyping) || ((et != null) && (et.isFinal())));
/*  83 */     this._valueTypeSerializer = vts;
/*  84 */     this._property = null;
/*  85 */     this._elementSerializer = elementSerializer;
/*  86 */     this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
/*  87 */     this._unwrapSingle = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected AsArraySerializerBase(Class<?> cls, JavaType et, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> elementSerializer)
/*     */   {
/*  99 */     super(cls, false);
/* 100 */     this._elementType = et;
/*     */     
/* 102 */     this._staticTyping = ((staticTyping) || ((et != null) && (et.isFinal())));
/* 103 */     this._valueTypeSerializer = vts;
/* 104 */     this._property = property;
/* 105 */     this._elementSerializer = elementSerializer;
/* 106 */     this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
/* 107 */     this._unwrapSingle = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AsArraySerializerBase(AsArraySerializerBase<?> src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer, Boolean unwrapSingle)
/*     */   {
/* 115 */     super(src);
/* 116 */     this._elementType = src._elementType;
/* 117 */     this._staticTyping = src._staticTyping;
/* 118 */     this._valueTypeSerializer = vts;
/* 119 */     this._property = property;
/* 120 */     this._elementSerializer = elementSerializer;
/* 121 */     this._dynamicSerializers = src._dynamicSerializers;
/* 122 */     this._unwrapSingle = unwrapSingle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected AsArraySerializerBase(AsArraySerializerBase<?> src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer)
/*     */   {
/* 132 */     this(src, property, vts, elementSerializer, src._unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final AsArraySerializerBase<T> withResolved(BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer)
/*     */   {
/* 141 */     return withResolved(property, vts, elementSerializer, this._unwrapSingle);
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
/*     */   public abstract AsArraySerializerBase<T> withResolved(BeanProperty paramBeanProperty, TypeSerializer paramTypeSerializer, JsonSerializer<?> paramJsonSerializer, Boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 168 */     TypeSerializer typeSer = this._valueTypeSerializer;
/* 169 */     if (typeSer != null) {
/* 170 */       typeSer = typeSer.forProperty(property);
/*     */     }
/* 172 */     JsonSerializer<?> ser = null;
/* 173 */     Boolean unwrapSingle = null;
/*     */     
/*     */ 
/* 176 */     if (property != null) {
/* 177 */       AnnotationIntrospector intr = serializers.getAnnotationIntrospector();
/* 178 */       AnnotatedMember m = property.getMember();
/* 179 */       if (m != null) {
/* 180 */         Object serDef = intr.findContentSerializer(m);
/* 181 */         if (serDef != null) {
/* 182 */           ser = serializers.serializerInstance(m, serDef);
/*     */         }
/*     */       }
/*     */     }
/* 186 */     JsonFormat.Value format = findFormatOverrides(serializers, property, handledType());
/* 187 */     if (format != null) {
/* 188 */       unwrapSingle = format.getFeature(JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);
/*     */     }
/* 190 */     if (ser == null) {
/* 191 */       ser = this._elementSerializer;
/*     */     }
/*     */     
/* 194 */     ser = findConvertingContentSerializer(serializers, property, ser);
/* 195 */     if (ser == null)
/*     */     {
/*     */ 
/* 198 */       if ((this._elementType != null) && 
/* 199 */         (this._staticTyping) && (!this._elementType.isJavaLangObject())) {
/* 200 */         ser = serializers.findValueSerializer(this._elementType, property);
/*     */       }
/*     */     }
/*     */     else {
/* 204 */       ser = serializers.handleSecondaryContextualization(ser, property);
/*     */     }
/* 206 */     if ((ser != this._elementSerializer) || (property != this._property) || (this._valueTypeSerializer != typeSer) || (this._unwrapSingle != unwrapSingle))
/*     */     {
/*     */ 
/*     */ 
/* 210 */       return withResolved(property, typeSer, ser, unwrapSingle);
/*     */     }
/* 212 */     return this;
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
/* 223 */     return this._elementType;
/*     */   }
/*     */   
/*     */   public JsonSerializer<?> getContentSerializer()
/*     */   {
/* 228 */     return this._elementSerializer;
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
/*     */   public void serialize(T value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 243 */     if ((provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED)) && (hasSingleElement(value)))
/*     */     {
/* 245 */       serializeContents(value, gen, provider);
/* 246 */       return;
/*     */     }
/* 248 */     gen.writeStartArray();
/*     */     
/* 250 */     gen.setCurrentValue(value);
/* 251 */     serializeContents(value, gen, provider);
/* 252 */     gen.writeEndArray();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void serializeWithType(T value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 260 */     typeSer.writeTypePrefixForArray(value, gen);
/*     */     
/* 262 */     gen.setCurrentValue(value);
/* 263 */     serializeContents(value, gen, provider);
/* 264 */     typeSer.writeTypeSuffixForArray(value, gen);
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract void serializeContents(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 275 */     ObjectNode o = createSchemaNode("array", true);
/* 276 */     JavaType contentType = this._elementType;
/* 277 */     if (contentType != null) {
/* 278 */       JsonNode schemaNode = null;
/*     */       
/* 280 */       if (contentType.getRawClass() != Object.class) {
/* 281 */         JsonSerializer<Object> ser = provider.findValueSerializer(contentType, this._property);
/* 282 */         if ((ser instanceof SchemaAware)) {
/* 283 */           schemaNode = ((SchemaAware)ser).getSchema(provider, null);
/*     */         }
/*     */       }
/* 286 */       if (schemaNode == null) {
/* 287 */         schemaNode = JsonSchema.getDefaultSchemaNode();
/*     */       }
/* 289 */       o.set("items", schemaNode);
/*     */     }
/* 291 */     return o;
/*     */   }
/*     */   
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 298 */     JsonSerializer<?> valueSer = this._elementSerializer;
/* 299 */     if (valueSer == null) {
/* 300 */       valueSer = visitor.getProvider().findValueSerializer(this._elementType, this._property);
/*     */     }
/* 302 */     visitArrayFormat(visitor, typeHint, valueSer, this._elementType);
/*     */   }
/*     */   
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 308 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/*     */     
/* 310 */     if (map != result.map) {
/* 311 */       this._dynamicSerializers = result.map;
/*     */     }
/* 313 */     return result.serializer;
/*     */   }
/*     */   
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 319 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/* 320 */     if (map != result.map) {
/* 321 */       this._dynamicSerializers = result.map;
/*     */     }
/* 323 */     return result.serializer;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\AsArraySerializerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */