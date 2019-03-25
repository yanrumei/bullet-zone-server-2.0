/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer.None;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DefaultSerializerProvider
/*     */   extends SerializerProvider
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected transient Map<Object, WritableObjectId> _seenObjectIds;
/*     */   protected transient ArrayList<ObjectIdGenerator<?>> _objectIdGenerators;
/*     */   protected transient JsonGenerator _generator;
/*     */   
/*     */   protected DefaultSerializerProvider() {}
/*     */   
/*     */   protected DefaultSerializerProvider(SerializerProvider src, SerializationConfig config, SerializerFactory f)
/*     */   {
/*  69 */     super(src, config, f);
/*     */   }
/*     */   
/*     */   protected DefaultSerializerProvider(DefaultSerializerProvider src) {
/*  73 */     super(src);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract DefaultSerializerProvider createInstance(SerializationConfig paramSerializationConfig, SerializerFactory paramSerializerFactory);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultSerializerProvider copy()
/*     */   {
/*  93 */     throw new IllegalStateException("DefaultSerializerProvider sub-class not overriding copy()");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonSerializer<Object> serializerInstance(Annotated annotated, Object serDef)
/*     */     throws JsonMappingException
/*     */   {
/* 105 */     if (serDef == null) {
/* 106 */       return null;
/*     */     }
/*     */     JsonSerializer<?> ser;
/*     */     JsonSerializer<?> ser;
/* 110 */     if ((serDef instanceof JsonSerializer)) {
/* 111 */       ser = (JsonSerializer)serDef;
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 116 */       if (!(serDef instanceof Class)) {
/* 117 */         throw new IllegalStateException("AnnotationIntrospector returned serializer definition of type " + serDef.getClass().getName() + "; expected type JsonSerializer or Class<JsonSerializer> instead");
/*     */       }
/*     */       
/* 120 */       Class<?> serClass = (Class)serDef;
/*     */       
/* 122 */       if ((serClass == JsonSerializer.None.class) || (ClassUtil.isBogusClass(serClass))) {
/* 123 */         return null;
/*     */       }
/* 125 */       if (!JsonSerializer.class.isAssignableFrom(serClass)) {
/* 126 */         throw new IllegalStateException("AnnotationIntrospector returned Class " + serClass.getName() + "; expected Class<JsonSerializer>");
/*     */       }
/*     */       
/* 129 */       HandlerInstantiator hi = this._config.getHandlerInstantiator();
/* 130 */       ser = hi == null ? null : hi.serializerInstance(this._config, annotated, serClass);
/* 131 */       if (ser == null) {
/* 132 */         ser = (JsonSerializer)ClassUtil.createInstance(serClass, this._config.canOverrideAccessModifiers());
/*     */       }
/*     */     }
/*     */     
/* 136 */     return _handleResolvable(ser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public WritableObjectId findObjectId(Object forPojo, ObjectIdGenerator<?> generatorType)
/*     */   {
/* 148 */     if (this._seenObjectIds == null) {
/* 149 */       this._seenObjectIds = _createObjectIdMap();
/*     */     } else {
/* 151 */       WritableObjectId oid = (WritableObjectId)this._seenObjectIds.get(forPojo);
/* 152 */       if (oid != null) {
/* 153 */         return oid;
/*     */       }
/*     */     }
/*     */     
/* 157 */     ObjectIdGenerator<?> generator = null;
/*     */     
/* 159 */     if (this._objectIdGenerators == null) {
/* 160 */       this._objectIdGenerators = new ArrayList(8);
/*     */     } else {
/* 162 */       int i = 0; for (int len = this._objectIdGenerators.size(); i < len; i++) {
/* 163 */         ObjectIdGenerator<?> gen = (ObjectIdGenerator)this._objectIdGenerators.get(i);
/* 164 */         if (gen.canUseFor(generatorType)) {
/* 165 */           generator = gen;
/* 166 */           break;
/*     */         }
/*     */       }
/*     */     }
/* 170 */     if (generator == null) {
/* 171 */       generator = generatorType.newForSerialization(this);
/* 172 */       this._objectIdGenerators.add(generator);
/*     */     }
/* 174 */     WritableObjectId oid = new WritableObjectId(generator);
/* 175 */     this._seenObjectIds.put(forPojo, oid);
/* 176 */     return oid;
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
/*     */   protected Map<Object, WritableObjectId> _createObjectIdMap()
/*     */   {
/* 191 */     if (isEnabled(SerializationFeature.USE_EQUALITY_FOR_OBJECT_ID)) {
/* 192 */       return new HashMap();
/*     */     }
/* 194 */     return new IdentityHashMap();
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
/*     */   public boolean hasSerializerFor(Class<?> cls, AtomicReference<Throwable> cause)
/*     */   {
/* 215 */     if ((cls == Object.class) && 
/* 216 */       (!this._config.isEnabled(SerializationFeature.FAIL_ON_EMPTY_BEANS))) {
/* 217 */       return true;
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 222 */       JsonSerializer<?> ser = _findExplicitUntypedSerializer(cls);
/* 223 */       return ser != null;
/*     */     } catch (JsonMappingException e) {
/* 225 */       if (cause != null) {
/* 226 */         cause.set(e);
/*     */       }
/*     */     } catch (RuntimeException e) {
/* 229 */       if (cause == null) {
/* 230 */         throw e;
/*     */       }
/* 232 */       cause.set(e);
/*     */     }
/* 234 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonGenerator getGenerator()
/*     */   {
/* 246 */     return this._generator;
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
/*     */   public void serializeValue(JsonGenerator gen, Object value)
/*     */     throws IOException
/*     */   {
/* 263 */     this._generator = gen;
/* 264 */     if (value == null) {
/* 265 */       _serializeNull(gen);
/* 266 */       return;
/*     */     }
/* 268 */     Class<?> cls = value.getClass();
/*     */     
/* 270 */     JsonSerializer<Object> ser = findTypedValueSerializer(cls, true, null);
/*     */     
/*     */ 
/*     */ 
/* 274 */     PropertyName rootName = this._config.getFullRootName();
/*     */     boolean wrap;
/* 276 */     if (rootName == null) {
/* 277 */       boolean wrap = this._config.isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
/* 278 */       if (wrap) {
/* 279 */         gen.writeStartObject();
/* 280 */         PropertyName pname = this._config.findRootName(value.getClass());
/* 281 */         gen.writeFieldName(pname.simpleAsEncoded(this._config));
/*     */       } } else { boolean wrap;
/* 283 */       if (rootName.isEmpty()) {
/* 284 */         wrap = false;
/*     */       }
/*     */       else {
/* 287 */         wrap = true;
/* 288 */         gen.writeStartObject();
/* 289 */         gen.writeFieldName(rootName.getSimpleName());
/*     */       }
/*     */     }
/* 292 */     try { ser.serialize(value, gen, this);
/* 293 */       if (wrap) {
/* 294 */         gen.writeEndObject();
/*     */       }
/*     */     } catch (IOException ioe) {
/* 297 */       throw ioe;
/*     */     } catch (Exception e) {
/* 299 */       String msg = e.getMessage();
/* 300 */       if (msg == null) {
/* 301 */         msg = "[no message for " + e.getClass().getName() + "]";
/*     */       }
/* 303 */       throw new JsonMappingException(gen, msg, e);
/*     */     }
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
/*     */   public void serializeValue(JsonGenerator gen, Object value, JavaType rootType)
/*     */     throws IOException
/*     */   {
/* 320 */     this._generator = gen;
/* 321 */     if (value == null) {
/* 322 */       _serializeNull(gen);
/* 323 */       return;
/*     */     }
/*     */     
/* 326 */     if (!rootType.getRawClass().isAssignableFrom(value.getClass())) {
/* 327 */       _reportIncompatibleRootType(value, rootType);
/*     */     }
/*     */     
/* 330 */     JsonSerializer<Object> ser = findTypedValueSerializer(rootType, true, null);
/*     */     
/*     */ 
/*     */ 
/* 334 */     PropertyName rootName = this._config.getFullRootName();
/* 335 */     boolean wrap; if (rootName == null) {
/* 336 */       boolean wrap = this._config.isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
/* 337 */       if (wrap) {
/* 338 */         gen.writeStartObject();
/* 339 */         PropertyName pname = this._config.findRootName(value.getClass());
/* 340 */         gen.writeFieldName(pname.simpleAsEncoded(this._config));
/*     */       } } else { boolean wrap;
/* 342 */       if (rootName.isEmpty()) {
/* 343 */         wrap = false;
/*     */       }
/*     */       else {
/* 346 */         wrap = true;
/* 347 */         gen.writeStartObject();
/* 348 */         gen.writeFieldName(rootName.getSimpleName());
/*     */       }
/*     */     }
/* 351 */     try { ser.serialize(value, gen, this);
/* 352 */       if (wrap) {
/* 353 */         gen.writeEndObject();
/*     */       }
/*     */     } catch (IOException ioe) {
/* 356 */       throw ioe;
/*     */     } catch (Exception e) {
/* 358 */       String msg = e.getMessage();
/* 359 */       if (msg == null) {
/* 360 */         msg = "[no message for " + e.getClass().getName() + "]";
/*     */       }
/* 362 */       reportMappingProblem(e, msg, new Object[0]);
/*     */     }
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
/*     */   public void serializeValue(JsonGenerator gen, Object value, JavaType rootType, JsonSerializer<Object> ser)
/*     */     throws IOException
/*     */   {
/* 381 */     this._generator = gen;
/* 382 */     if (value == null) {
/* 383 */       _serializeNull(gen);
/* 384 */       return;
/*     */     }
/*     */     
/* 387 */     if ((rootType != null) && (!rootType.getRawClass().isAssignableFrom(value.getClass()))) {
/* 388 */       _reportIncompatibleRootType(value, rootType);
/*     */     }
/*     */     
/* 391 */     if (ser == null) {
/* 392 */       ser = findTypedValueSerializer(rootType, true, null);
/*     */     }
/*     */     
/*     */ 
/* 396 */     PropertyName rootName = this._config.getFullRootName();
/* 397 */     boolean wrap; if (rootName == null)
/*     */     {
/* 399 */       boolean wrap = this._config.isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
/* 400 */       if (wrap) {
/* 401 */         gen.writeStartObject();
/* 402 */         PropertyName pname = rootType == null ? this._config.findRootName(value.getClass()) : this._config.findRootName(rootType);
/*     */         
/*     */ 
/* 405 */         gen.writeFieldName(pname.simpleAsEncoded(this._config));
/*     */       } } else { boolean wrap;
/* 407 */       if (rootName.isEmpty()) {
/* 408 */         wrap = false;
/*     */       }
/*     */       else {
/* 411 */         wrap = true;
/* 412 */         gen.writeStartObject();
/* 413 */         gen.writeFieldName(rootName.getSimpleName());
/*     */       }
/*     */     }
/* 416 */     try { ser.serialize(value, gen, this);
/* 417 */       if (wrap) {
/* 418 */         gen.writeEndObject();
/*     */       }
/*     */     } catch (IOException ioe) {
/* 421 */       throw ioe;
/*     */     } catch (Exception e) {
/* 423 */       String msg = e.getMessage();
/* 424 */       if (msg == null) {
/* 425 */         msg = "[no message for " + e.getClass().getName() + "]";
/*     */       }
/* 427 */       reportMappingProblem(e, msg, new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serializePolymorphic(JsonGenerator gen, Object value, JavaType rootType, JsonSerializer<Object> valueSer, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 441 */     this._generator = gen;
/* 442 */     if (value == null) {
/* 443 */       _serializeNull(gen);
/* 444 */       return;
/*     */     }
/*     */     
/* 447 */     if ((rootType != null) && (!rootType.getRawClass().isAssignableFrom(value.getClass()))) {
/* 448 */       _reportIncompatibleRootType(value, rootType);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 455 */     if (valueSer == null) {
/* 456 */       if ((rootType != null) && (rootType.isContainerType())) {
/* 457 */         valueSer = findValueSerializer(rootType, null);
/*     */       } else {
/* 459 */         valueSer = findValueSerializer(value.getClass(), null);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 464 */     PropertyName rootName = this._config.getFullRootName();
/* 465 */     boolean wrap; if (rootName == null) {
/* 466 */       boolean wrap = this._config.isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
/* 467 */       if (wrap) {
/* 468 */         gen.writeStartObject();
/* 469 */         PropertyName pname = this._config.findRootName(value.getClass());
/* 470 */         gen.writeFieldName(pname.simpleAsEncoded(this._config));
/*     */       } } else { boolean wrap;
/* 472 */       if (rootName.isEmpty()) {
/* 473 */         wrap = false;
/*     */       } else {
/* 475 */         wrap = true;
/* 476 */         gen.writeStartObject();
/* 477 */         gen.writeFieldName(rootName.getSimpleName());
/*     */       }
/*     */     }
/* 480 */     try { valueSer.serializeWithType(value, gen, this, typeSer);
/* 481 */       if (wrap) {
/* 482 */         gen.writeEndObject();
/*     */       }
/*     */     } catch (IOException ioe) {
/* 485 */       throw ioe;
/*     */     } catch (Exception e) {
/* 487 */       String msg = e.getMessage();
/* 488 */       if (msg == null) {
/* 489 */         msg = "[no message for " + e.getClass().getName() + "]";
/*     */       }
/* 491 */       reportMappingProblem(e, msg, new Object[0]);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public void serializePolymorphic(JsonGenerator gen, Object value, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 502 */     JavaType t = value == null ? null : this._config.constructType(value.getClass());
/* 503 */     serializePolymorphic(gen, value, t, null, typeSer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _serializeNull(JsonGenerator gen)
/*     */     throws IOException
/*     */   {
/* 513 */     JsonSerializer<Object> ser = getDefaultNullValueSerializer();
/*     */     try {
/* 515 */       ser.serialize(null, gen, this);
/*     */     } catch (IOException ioe) {
/* 517 */       throw ioe;
/*     */     } catch (Exception e) {
/* 519 */       String msg = e.getMessage();
/* 520 */       if (msg == null) {
/* 521 */         msg = "[no message for " + e.getClass().getName() + "]";
/*     */       }
/* 523 */       reportMappingProblem(e, msg, new Object[0]);
/*     */     }
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
/*     */   public int cachedSerializersCount()
/*     */   {
/* 544 */     return this._serializerCache.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flushCachedSerializers()
/*     */   {
/* 554 */     this._serializerCache.flush();
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
/*     */   public void acceptJsonFormatVisitor(JavaType javaType, JsonFormatVisitorWrapper visitor)
/*     */     throws JsonMappingException
/*     */   {
/* 573 */     if (javaType == null) {
/* 574 */       throw new IllegalArgumentException("A class must be provided");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 579 */     visitor.setProvider(this);
/* 580 */     findValueSerializer(javaType, null).acceptJsonFormatVisitor(visitor, javaType);
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
/*     */   @Deprecated
/*     */   public JsonSchema generateJsonSchema(Class<?> type)
/*     */     throws JsonMappingException
/*     */   {
/* 596 */     if (type == null) {
/* 597 */       throw new IllegalArgumentException("A class must be provided");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 602 */     JsonSerializer<Object> ser = findValueSerializer(type, null);
/* 603 */     JsonNode schemaNode = (ser instanceof SchemaAware) ? ((SchemaAware)ser).getSchema(this, null) : JsonSchema.getDefaultSchemaNode();
/*     */     
/* 605 */     if (!(schemaNode instanceof ObjectNode)) {
/* 606 */       throw new IllegalArgumentException("Class " + type.getName() + " would not be serialized as a JSON object and therefore has no schema");
/*     */     }
/*     */     
/* 609 */     return new JsonSchema((ObjectNode)schemaNode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static final class Impl
/*     */     extends DefaultSerializerProvider
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */     public Impl() {}
/*     */     
/*     */ 
/*     */ 
/*     */     public Impl(Impl src)
/*     */     {
/* 627 */       super();
/*     */     }
/*     */     
/*     */     protected Impl(SerializerProvider src, SerializationConfig config, SerializerFactory f) {
/* 631 */       super(config, f);
/*     */     }
/*     */     
/*     */ 
/*     */     public DefaultSerializerProvider copy()
/*     */     {
/* 637 */       if (getClass() != Impl.class) {
/* 638 */         return super.copy();
/*     */       }
/* 640 */       return new Impl(this);
/*     */     }
/*     */     
/*     */     public Impl createInstance(SerializationConfig config, SerializerFactory jsf)
/*     */     {
/* 645 */       return new Impl(this, config, jsf);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\DefaultSerializerProvider.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */