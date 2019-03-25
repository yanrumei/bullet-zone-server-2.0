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
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap.SerializerAndMapResult;
/*     */ import com.fasterxml.jackson.databind.type.ArrayType;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
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
/*     */ @JacksonStdImpl
/*     */ public class ObjectArraySerializer
/*     */   extends ArraySerializerBase<Object[]>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   protected final boolean _staticTyping;
/*     */   protected final JavaType _elementType;
/*     */   protected final TypeSerializer _valueTypeSerializer;
/*     */   protected JsonSerializer<Object> _elementSerializer;
/*     */   protected PropertySerializerMap _dynamicSerializers;
/*     */   
/*     */   public ObjectArraySerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> elementSerializer)
/*     */   {
/*  68 */     super(Object[].class);
/*  69 */     this._elementType = elemType;
/*  70 */     this._staticTyping = staticTyping;
/*  71 */     this._valueTypeSerializer = vts;
/*  72 */     this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
/*  73 */     this._elementSerializer = elementSerializer;
/*     */   }
/*     */   
/*     */   public ObjectArraySerializer(ObjectArraySerializer src, TypeSerializer vts)
/*     */   {
/*  78 */     super(src);
/*  79 */     this._elementType = src._elementType;
/*  80 */     this._valueTypeSerializer = vts;
/*  81 */     this._staticTyping = src._staticTyping;
/*  82 */     this._dynamicSerializers = src._dynamicSerializers;
/*  83 */     this._elementSerializer = src._elementSerializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectArraySerializer(ObjectArraySerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer, Boolean unwrapSingle)
/*     */   {
/*  91 */     super(src, property, unwrapSingle);
/*  92 */     this._elementType = src._elementType;
/*  93 */     this._valueTypeSerializer = vts;
/*  94 */     this._staticTyping = src._staticTyping;
/*  95 */     this._dynamicSerializers = src._dynamicSerializers;
/*  96 */     this._elementSerializer = elementSerializer;
/*     */   }
/*     */   
/*     */   public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle)
/*     */   {
/* 101 */     return new ObjectArraySerializer(this, prop, this._valueTypeSerializer, this._elementSerializer, unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */   public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/* 107 */     return new ObjectArraySerializer(this._elementType, this._staticTyping, vts, this._elementSerializer);
/*     */   }
/*     */   
/*     */   public ObjectArraySerializer withResolved(BeanProperty prop, TypeSerializer vts, JsonSerializer<?> ser, Boolean unwrapSingle)
/*     */   {
/* 112 */     if ((this._property == prop) && (ser == this._elementSerializer) && (this._valueTypeSerializer == vts) && (this._unwrapSingle == unwrapSingle))
/*     */     {
/* 114 */       return this;
/*     */     }
/* 116 */     return new ObjectArraySerializer(this, prop, vts, ser, unwrapSingle);
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
/*     */   public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 130 */     TypeSerializer vts = this._valueTypeSerializer;
/* 131 */     if (vts != null) {
/* 132 */       vts = vts.forProperty(property);
/*     */     }
/* 134 */     JsonSerializer<?> ser = null;
/* 135 */     Boolean unwrapSingle = null;
/*     */     
/*     */ 
/* 138 */     if (property != null) {
/* 139 */       AnnotatedMember m = property.getMember();
/* 140 */       AnnotationIntrospector intr = serializers.getAnnotationIntrospector();
/* 141 */       if (m != null) {
/* 142 */         Object serDef = intr.findContentSerializer(m);
/* 143 */         if (serDef != null) {
/* 144 */           ser = serializers.serializerInstance(m, serDef);
/*     */         }
/*     */       }
/*     */     }
/* 148 */     JsonFormat.Value format = findFormatOverrides(serializers, property, handledType());
/* 149 */     if (format != null) {
/* 150 */       unwrapSingle = format.getFeature(JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);
/*     */     }
/* 152 */     if (ser == null) {
/* 153 */       ser = this._elementSerializer;
/*     */     }
/*     */     
/* 156 */     ser = findConvertingContentSerializer(serializers, property, ser);
/* 157 */     if (ser == null)
/*     */     {
/*     */ 
/* 160 */       if ((this._elementType != null) && 
/* 161 */         (this._staticTyping) && (!this._elementType.isJavaLangObject())) {
/* 162 */         ser = serializers.findValueSerializer(this._elementType, property);
/*     */       }
/*     */     }
/*     */     else {
/* 166 */       ser = serializers.handleSecondaryContextualization(ser, property);
/*     */     }
/* 168 */     return withResolved(property, vts, ser, unwrapSingle);
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
/* 179 */     return this._elementType;
/*     */   }
/*     */   
/*     */   public JsonSerializer<?> getContentSerializer()
/*     */   {
/* 184 */     return this._elementSerializer;
/*     */   }
/*     */   
/*     */   public boolean isEmpty(SerializerProvider prov, Object[] value)
/*     */   {
/* 189 */     return (value == null) || (value.length == 0);
/*     */   }
/*     */   
/*     */   public boolean hasSingleElement(Object[] value)
/*     */   {
/* 194 */     return value.length == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void serialize(Object[] value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 206 */     int len = value.length;
/* 207 */     if ((len == 1) && (
/* 208 */       ((this._unwrapSingle == null) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) || (this._unwrapSingle == Boolean.TRUE)))
/*     */     {
/*     */ 
/* 211 */       serializeContents(value, gen, provider);
/* 212 */       return;
/*     */     }
/*     */     
/* 215 */     gen.writeStartArray(len);
/* 216 */     serializeContents(value, gen, provider);
/* 217 */     gen.writeEndArray();
/*     */   }
/*     */   
/*     */   public void serializeContents(Object[] value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 223 */     int len = value.length;
/* 224 */     if (len == 0) {
/* 225 */       return;
/*     */     }
/* 227 */     if (this._elementSerializer != null) {
/* 228 */       serializeContentsUsing(value, gen, provider, this._elementSerializer);
/* 229 */       return;
/*     */     }
/* 231 */     if (this._valueTypeSerializer != null) {
/* 232 */       serializeTypedContents(value, gen, provider);
/* 233 */       return;
/*     */     }
/* 235 */     int i = 0;
/* 236 */     Object elem = null;
/*     */     try {
/* 238 */       PropertySerializerMap serializers = this._dynamicSerializers;
/* 239 */       for (; i < len; i++) {
/* 240 */         elem = value[i];
/* 241 */         if (elem == null) {
/* 242 */           provider.defaultSerializeNull(gen);
/*     */         }
/*     */         else {
/* 245 */           Class<?> cc = elem.getClass();
/* 246 */           JsonSerializer<Object> serializer = serializers.serializerFor(cc);
/* 247 */           if (serializer == null)
/*     */           {
/* 249 */             if (this._elementType.hasGenericTypes()) {
/* 250 */               serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._elementType, cc), provider);
/*     */             }
/*     */             else {
/* 253 */               serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */             }
/*     */           }
/* 256 */           serializer.serialize(elem, gen, provider);
/*     */         }
/*     */       }
/* 259 */     } catch (IOException ioe) { throw ioe;
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/*     */ 
/* 266 */       Throwable t = e;
/* 267 */       while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 268 */         t = t.getCause();
/*     */       }
/* 270 */       if ((t instanceof Error)) {
/* 271 */         throw ((Error)t);
/*     */       }
/* 273 */       throw JsonMappingException.wrapWithPath(t, elem, i);
/*     */     }
/*     */   }
/*     */   
/*     */   public void serializeContentsUsing(Object[] value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser)
/*     */     throws IOException
/*     */   {
/* 280 */     int len = value.length;
/* 281 */     TypeSerializer typeSer = this._valueTypeSerializer;
/*     */     
/* 283 */     int i = 0;
/* 284 */     Object elem = null;
/*     */     try {
/* 286 */       for (; i < len; i++) {
/* 287 */         elem = value[i];
/* 288 */         if (elem == null) {
/* 289 */           provider.defaultSerializeNull(jgen);
/*     */ 
/*     */         }
/* 292 */         else if (typeSer == null) {
/* 293 */           ser.serialize(elem, jgen, provider);
/*     */         } else {
/* 295 */           ser.serializeWithType(elem, jgen, provider, typeSer);
/*     */         }
/*     */       }
/*     */     } catch (IOException ioe) {
/* 299 */       throw ioe;
/*     */     } catch (Exception e) {
/* 301 */       Throwable t = e;
/* 302 */       while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 303 */         t = t.getCause();
/*     */       }
/* 305 */       if ((t instanceof Error)) {
/* 306 */         throw ((Error)t);
/*     */       }
/* 308 */       throw JsonMappingException.wrapWithPath(t, elem, i);
/*     */     }
/*     */   }
/*     */   
/*     */   public void serializeTypedContents(Object[] value, JsonGenerator jgen, SerializerProvider provider) throws IOException
/*     */   {
/* 314 */     int len = value.length;
/* 315 */     TypeSerializer typeSer = this._valueTypeSerializer;
/* 316 */     int i = 0;
/* 317 */     Object elem = null;
/*     */     try {
/* 319 */       PropertySerializerMap serializers = this._dynamicSerializers;
/* 320 */       for (; i < len; i++) {
/* 321 */         elem = value[i];
/* 322 */         if (elem == null) {
/* 323 */           provider.defaultSerializeNull(jgen);
/*     */         }
/*     */         else {
/* 326 */           Class<?> cc = elem.getClass();
/* 327 */           JsonSerializer<Object> serializer = serializers.serializerFor(cc);
/* 328 */           if (serializer == null) {
/* 329 */             serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */           }
/* 331 */           serializer.serializeWithType(elem, jgen, provider, typeSer);
/*     */         }
/*     */       }
/* 334 */     } catch (IOException ioe) { throw ioe;
/*     */     } catch (Exception e) {
/* 336 */       Throwable t = e;
/* 337 */       while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 338 */         t = t.getCause();
/*     */       }
/* 340 */       if ((t instanceof Error)) {
/* 341 */         throw ((Error)t);
/*     */       }
/* 343 */       throw JsonMappingException.wrapWithPath(t, elem, i);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 352 */     ObjectNode o = createSchemaNode("array", true);
/* 353 */     if (typeHint != null) {
/* 354 */       JavaType javaType = provider.constructType(typeHint);
/* 355 */       if (javaType.isArrayType()) {
/* 356 */         Class<?> componentType = ((ArrayType)javaType).getContentType().getRawClass();
/*     */         
/* 358 */         if (componentType == Object.class) {
/* 359 */           o.set("items", JsonSchema.getDefaultSchemaNode());
/*     */         } else {
/* 361 */           JsonSerializer<Object> ser = provider.findValueSerializer(componentType, this._property);
/* 362 */           JsonNode schemaNode = (ser instanceof SchemaAware) ? ((SchemaAware)ser).getSchema(provider, null) : JsonSchema.getDefaultSchemaNode();
/*     */           
/*     */ 
/* 365 */           o.set("items", schemaNode);
/*     */         }
/*     */       }
/*     */     }
/* 369 */     return o;
/*     */   }
/*     */   
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 376 */     JsonArrayFormatVisitor arrayVisitor = visitor.expectArrayFormat(typeHint);
/* 377 */     if (arrayVisitor != null) {
/* 378 */       TypeFactory tf = visitor.getProvider().getTypeFactory();
/* 379 */       JavaType contentType = tf.moreSpecificType(this._elementType, typeHint.getContentType());
/* 380 */       if (contentType == null) {
/* 381 */         throw JsonMappingException.from(visitor.getProvider(), "Could not resolve type");
/*     */       }
/* 383 */       JsonSerializer<?> valueSer = this._elementSerializer;
/* 384 */       if (valueSer == null) {
/* 385 */         valueSer = visitor.getProvider().findValueSerializer(contentType, this._property);
/*     */       }
/* 387 */       arrayVisitor.itemsFormat(valueSer, contentType);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 394 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/*     */     
/* 396 */     if (map != result.map) {
/* 397 */       this._dynamicSerializers = result.map;
/*     */     }
/* 399 */     return result.serializer;
/*     */   }
/*     */   
/*     */   protected final JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, JavaType type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 405 */     PropertySerializerMap.SerializerAndMapResult result = map.findAndAddSecondarySerializer(type, provider, this._property);
/*     */     
/* 407 */     if (map != result.map) {
/* 408 */       this._dynamicSerializers = result.map;
/*     */     }
/* 410 */     return result.serializer;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\ObjectArraySerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */