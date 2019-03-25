/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Shape;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties.Value;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException.Reference;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSerializableSchema;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.AnyGetterWriter;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.ser.BeanSerializerBuilder;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.PropertyFilter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Set;
/*     */ 
/*     */ public abstract class BeanSerializerBase extends StdSerializer<Object> implements com.fasterxml.jackson.databind.ser.ContextualSerializer, com.fasterxml.jackson.databind.ser.ResolvableSerializer, com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable, com.fasterxml.jackson.databind.jsonschema.SchemaAware
/*     */ {
/*  39 */   protected static final PropertyName NAME_FOR_OBJECT_REF = new PropertyName("#object-ref");
/*     */   
/*  41 */   protected static final BeanPropertyWriter[] NO_PROPS = new BeanPropertyWriter[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final BeanPropertyWriter[] _props;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final BeanPropertyWriter[] _filteredProps;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final AnyGetterWriter _anyGetterWriter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object _propertyFilterId;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final AnnotatedMember _typeId;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ObjectIdWriter _objectIdWriter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonFormat.Shape _serializationShape;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanSerializerBase(JavaType type, BeanSerializerBuilder builder, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties)
/*     */   {
/* 105 */     super(type);
/* 106 */     this._props = properties;
/* 107 */     this._filteredProps = filteredProperties;
/* 108 */     if (builder == null) {
/* 109 */       this._typeId = null;
/* 110 */       this._anyGetterWriter = null;
/* 111 */       this._propertyFilterId = null;
/* 112 */       this._objectIdWriter = null;
/* 113 */       this._serializationShape = null;
/*     */     } else {
/* 115 */       this._typeId = builder.getTypeId();
/* 116 */       this._anyGetterWriter = builder.getAnyGetter();
/* 117 */       this._propertyFilterId = builder.getFilterId();
/* 118 */       this._objectIdWriter = builder.getObjectIdWriter();
/* 119 */       JsonFormat.Value format = builder.getBeanDescription().findExpectedFormat(null);
/* 120 */       this._serializationShape = (format == null ? null : format.getShape());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public BeanSerializerBase(BeanSerializerBase src, BeanPropertyWriter[] properties, BeanPropertyWriter[] filteredProperties)
/*     */   {
/* 127 */     super(src._handledType);
/* 128 */     this._props = properties;
/* 129 */     this._filteredProps = filteredProperties;
/*     */     
/* 131 */     this._typeId = src._typeId;
/* 132 */     this._anyGetterWriter = src._anyGetterWriter;
/* 133 */     this._objectIdWriter = src._objectIdWriter;
/* 134 */     this._propertyFilterId = src._propertyFilterId;
/* 135 */     this._serializationShape = src._serializationShape;
/*     */   }
/*     */   
/*     */ 
/*     */   protected BeanSerializerBase(BeanSerializerBase src, ObjectIdWriter objectIdWriter)
/*     */   {
/* 141 */     this(src, objectIdWriter, src._propertyFilterId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanSerializerBase(BeanSerializerBase src, ObjectIdWriter objectIdWriter, Object filterId)
/*     */   {
/* 150 */     super(src._handledType);
/* 151 */     this._props = src._props;
/* 152 */     this._filteredProps = src._filteredProps;
/*     */     
/* 154 */     this._typeId = src._typeId;
/* 155 */     this._anyGetterWriter = src._anyGetterWriter;
/* 156 */     this._objectIdWriter = objectIdWriter;
/* 157 */     this._propertyFilterId = filterId;
/* 158 */     this._serializationShape = src._serializationShape;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected BeanSerializerBase(BeanSerializerBase src, String[] toIgnore)
/*     */   {
/* 164 */     this(src, ArrayBuilders.arrayToSet(toIgnore));
/*     */   }
/*     */   
/*     */   protected BeanSerializerBase(BeanSerializerBase src, Set<String> toIgnore)
/*     */   {
/* 169 */     super(src._handledType);
/*     */     
/* 171 */     BeanPropertyWriter[] propsIn = src._props;
/* 172 */     BeanPropertyWriter[] fpropsIn = src._filteredProps;
/* 173 */     int len = propsIn.length;
/*     */     
/* 175 */     ArrayList<BeanPropertyWriter> propsOut = new ArrayList(len);
/* 176 */     ArrayList<BeanPropertyWriter> fpropsOut = fpropsIn == null ? null : new ArrayList(len);
/*     */     
/* 178 */     for (int i = 0; i < len; i++) {
/* 179 */       BeanPropertyWriter bpw = propsIn[i];
/*     */       
/* 181 */       if ((toIgnore == null) || (!toIgnore.contains(bpw.getName())))
/*     */       {
/*     */ 
/* 184 */         propsOut.add(bpw);
/* 185 */         if (fpropsIn != null)
/* 186 */           fpropsOut.add(fpropsIn[i]);
/*     */       }
/*     */     }
/* 189 */     this._props = ((BeanPropertyWriter[])propsOut.toArray(new BeanPropertyWriter[propsOut.size()]));
/* 190 */     this._filteredProps = (fpropsOut == null ? null : (BeanPropertyWriter[])fpropsOut.toArray(new BeanPropertyWriter[fpropsOut.size()]));
/*     */     
/* 192 */     this._typeId = src._typeId;
/* 193 */     this._anyGetterWriter = src._anyGetterWriter;
/* 194 */     this._objectIdWriter = src._objectIdWriter;
/* 195 */     this._propertyFilterId = src._propertyFilterId;
/* 196 */     this._serializationShape = src._serializationShape;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract BeanSerializerBase withObjectIdWriter(ObjectIdWriter paramObjectIdWriter);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract BeanSerializerBase withIgnorals(Set<String> paramSet);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected BeanSerializerBase withIgnorals(String[] toIgnore)
/*     */   {
/* 223 */     return withIgnorals(ArrayBuilders.arrayToSet(toIgnore));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract BeanSerializerBase asArraySerializer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract BeanSerializerBase withFilterId(Object paramObject);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanSerializerBase(BeanSerializerBase src)
/*     */   {
/* 249 */     this(src, src._props, src._filteredProps);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanSerializerBase(BeanSerializerBase src, NameTransformer unwrapper)
/*     */   {
/* 257 */     this(src, rename(src._props, unwrapper), rename(src._filteredProps, unwrapper));
/*     */   }
/*     */   
/*     */ 
/*     */   private static final BeanPropertyWriter[] rename(BeanPropertyWriter[] props, NameTransformer transformer)
/*     */   {
/* 263 */     if ((props == null) || (props.length == 0) || (transformer == null) || (transformer == NameTransformer.NOP)) {
/* 264 */       return props;
/*     */     }
/* 266 */     int len = props.length;
/* 267 */     BeanPropertyWriter[] result = new BeanPropertyWriter[len];
/* 268 */     for (int i = 0; i < len; i++) {
/* 269 */       BeanPropertyWriter bpw = props[i];
/* 270 */       if (bpw != null) {
/* 271 */         result[i] = bpw.rename(transformer);
/*     */       }
/*     */     }
/* 274 */     return result;
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
/*     */   public void resolve(SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 291 */     int filteredCount = this._filteredProps == null ? 0 : this._filteredProps.length;
/* 292 */     int i = 0; for (int len = this._props.length; i < len; i++) {
/* 293 */       BeanPropertyWriter prop = this._props[i];
/*     */       
/* 295 */       if ((!prop.willSuppressNulls()) && (!prop.hasNullSerializer())) {
/* 296 */         JsonSerializer<Object> nullSer = provider.findNullValueSerializer(prop);
/* 297 */         if (nullSer != null) {
/* 298 */           prop.assignNullSerializer(nullSer);
/*     */           
/* 300 */           if (i < filteredCount) {
/* 301 */             BeanPropertyWriter w2 = this._filteredProps[i];
/* 302 */             if (w2 != null) {
/* 303 */               w2.assignNullSerializer(nullSer);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 309 */       if (!prop.hasSerializer())
/*     */       {
/*     */ 
/*     */ 
/* 313 */         JsonSerializer<Object> ser = findConvertingSerializer(provider, prop);
/* 314 */         if (ser == null)
/*     */         {
/* 316 */           JavaType type = prop.getSerializationType();
/*     */           
/*     */ 
/*     */ 
/* 320 */           if (type == null)
/*     */           {
/*     */ 
/*     */ 
/* 324 */             type = prop.getType();
/* 325 */             if (!type.isFinal()) {
/* 326 */               if ((!type.isContainerType()) && (type.containedTypeCount() <= 0)) continue;
/* 327 */               prop.setNonTrivialBaseType(type); continue;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 332 */           ser = provider.findValueSerializer(type, prop);
/*     */           
/*     */ 
/*     */ 
/* 336 */           if (type.isContainerType()) {
/* 337 */             TypeSerializer typeSer = (TypeSerializer)type.getContentType().getTypeHandler();
/* 338 */             if (typeSer != null)
/*     */             {
/* 340 */               if ((ser instanceof ContainerSerializer))
/*     */               {
/*     */ 
/* 343 */                 JsonSerializer<Object> ser2 = ((ContainerSerializer)ser).withValueTypeSerializer(typeSer);
/* 344 */                 ser = ser2;
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 349 */         prop.assignSerializer(ser);
/*     */         
/* 351 */         if (i < filteredCount) {
/* 352 */           BeanPropertyWriter w2 = this._filteredProps[i];
/* 353 */           if (w2 != null) {
/* 354 */             w2.assignSerializer(ser);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 360 */     if (this._anyGetterWriter != null)
/*     */     {
/* 362 */       this._anyGetterWriter.resolve(provider);
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
/*     */   protected JsonSerializer<Object> findConvertingSerializer(SerializerProvider provider, BeanPropertyWriter prop)
/*     */     throws JsonMappingException
/*     */   {
/* 377 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/* 378 */     if (intr != null) {
/* 379 */       AnnotatedMember m = prop.getMember();
/* 380 */       if (m != null) {
/* 381 */         Object convDef = intr.findSerializationConverter(m);
/* 382 */         if (convDef != null) {
/* 383 */           Converter<Object, Object> conv = provider.converterInstance(prop.getMember(), convDef);
/* 384 */           JavaType delegateType = conv.getOutputType(provider.getTypeFactory());
/*     */           
/* 386 */           JsonSerializer<?> ser = delegateType.isJavaLangObject() ? null : provider.findValueSerializer(delegateType, prop);
/*     */           
/* 388 */           return new StdDelegatingSerializer(conv, delegateType, ser);
/*     */         }
/*     */       }
/*     */     }
/* 392 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 401 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/* 402 */     AnnotatedMember accessor = (property == null) || (intr == null) ? null : property.getMember();
/*     */     
/* 404 */     SerializationConfig config = provider.getConfig();
/*     */     
/*     */ 
/*     */ 
/* 408 */     JsonFormat.Value format = findFormatOverrides(provider, property, handledType());
/* 409 */     JsonFormat.Shape shape = null;
/* 410 */     if ((format != null) && (format.hasShape())) {
/* 411 */       shape = format.getShape();
/*     */       
/* 413 */       if ((shape != JsonFormat.Shape.ANY) && (shape != this._serializationShape) && 
/* 414 */         (this._handledType.isEnum())) {
/* 415 */         switch (shape)
/*     */         {
/*     */ 
/*     */         case STRING: 
/*     */         case NUMBER: 
/*     */         case NUMBER_INT: 
/* 421 */           com.fasterxml.jackson.databind.BeanDescription desc = config.introspectClassAnnotations(this._handledType);
/* 422 */           JsonSerializer<?> ser = EnumSerializer.construct(this._handledType, provider.getConfig(), desc, format);
/*     */           
/* 424 */           return provider.handlePrimaryContextualization(ser, property);
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     
/* 430 */     ObjectIdWriter oiw = this._objectIdWriter;
/* 431 */     Set<String> ignoredProps = null;
/* 432 */     Object newFilterId = null;
/*     */     
/*     */ 
/* 435 */     if (accessor != null) {
/* 436 */       JsonIgnoreProperties.Value ignorals = intr.findPropertyIgnorals(accessor);
/* 437 */       if (ignorals != null) {
/* 438 */         ignoredProps = ignorals.findIgnoredForSerialization();
/*     */       }
/* 440 */       ObjectIdInfo objectIdInfo = intr.findObjectIdInfo(accessor);
/* 441 */       if (objectIdInfo == null)
/*     */       {
/* 443 */         if (oiw != null) {
/* 444 */           objectIdInfo = intr.findObjectReferenceInfo(accessor, null);
/* 445 */           if (objectIdInfo != null) {
/* 446 */             oiw = this._objectIdWriter.withAlwaysAsId(objectIdInfo.getAlwaysAsId());
/*     */           }
/*     */           
/*     */         }
/*     */         
/*     */       }
/*     */       else
/*     */       {
/* 454 */         objectIdInfo = intr.findObjectReferenceInfo(accessor, objectIdInfo);
/*     */         
/* 456 */         Class<?> implClass = objectIdInfo.getGeneratorType();
/* 457 */         JavaType type = provider.constructType(implClass);
/* 458 */         JavaType idType = provider.getTypeFactory().findTypeParameters(type, com.fasterxml.jackson.annotation.ObjectIdGenerator.class)[0];
/*     */         
/* 460 */         if (implClass == com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class) {
/* 461 */           String propName = objectIdInfo.getPropertyName().getSimpleName();
/* 462 */           BeanPropertyWriter idProp = null;
/*     */           
/* 464 */           int i = 0; for (int len = this._props.length;; i++) {
/* 465 */             if (i == len) {
/* 466 */               throw new IllegalArgumentException("Invalid Object Id definition for " + this._handledType.getName() + ": can not find property with name '" + propName + "'");
/*     */             }
/*     */             
/* 469 */             BeanPropertyWriter prop = this._props[i];
/* 470 */             if (propName.equals(prop.getName())) {
/* 471 */               idProp = prop;
/*     */               
/*     */ 
/*     */ 
/* 475 */               if (i <= 0) break;
/* 476 */               System.arraycopy(this._props, 0, this._props, 1, i);
/* 477 */               this._props[0] = idProp;
/* 478 */               if (this._filteredProps == null) break;
/* 479 */               BeanPropertyWriter fp = this._filteredProps[i];
/* 480 */               System.arraycopy(this._filteredProps, 0, this._filteredProps, 1, i);
/* 481 */               this._filteredProps[0] = fp;
/* 482 */               break;
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 487 */           idType = idProp.getType();
/* 488 */           com.fasterxml.jackson.annotation.ObjectIdGenerator<?> gen = new com.fasterxml.jackson.databind.ser.impl.PropertyBasedObjectIdGenerator(objectIdInfo, idProp);
/* 489 */           oiw = ObjectIdWriter.construct(idType, (PropertyName)null, gen, objectIdInfo.getAlwaysAsId());
/*     */         } else {
/* 491 */           com.fasterxml.jackson.annotation.ObjectIdGenerator<?> gen = provider.objectIdGeneratorInstance(accessor, objectIdInfo);
/* 492 */           oiw = ObjectIdWriter.construct(idType, objectIdInfo.getPropertyName(), gen, objectIdInfo.getAlwaysAsId());
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 498 */       Object filterId = intr.findFilterId(accessor);
/* 499 */       if (filterId != null)
/*     */       {
/* 501 */         if ((this._propertyFilterId == null) || (!filterId.equals(this._propertyFilterId))) {
/* 502 */           newFilterId = filterId;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 507 */     BeanSerializerBase contextual = this;
/* 508 */     if (oiw != null) {
/* 509 */       JsonSerializer<?> ser = provider.findValueSerializer(oiw.idType, property);
/* 510 */       oiw = oiw.withSerializer(ser);
/* 511 */       if (oiw != this._objectIdWriter) {
/* 512 */         contextual = contextual.withObjectIdWriter(oiw);
/*     */       }
/*     */     }
/*     */     
/* 516 */     if ((ignoredProps != null) && (!ignoredProps.isEmpty())) {
/* 517 */       contextual = contextual.withIgnorals(ignoredProps);
/*     */     }
/* 519 */     if (newFilterId != null) {
/* 520 */       contextual = contextual.withFilterId(newFilterId);
/*     */     }
/* 522 */     if (shape == null) {
/* 523 */       shape = this._serializationShape;
/*     */     }
/* 525 */     if (shape == JsonFormat.Shape.ARRAY) {
/* 526 */       return contextual.asArraySerializer();
/*     */     }
/* 528 */     return contextual;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public java.util.Iterator<com.fasterxml.jackson.databind.ser.PropertyWriter> properties()
/*     */   {
/* 539 */     return java.util.Arrays.asList(this._props).iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean usesObjectId()
/*     */   {
/* 550 */     return this._objectIdWriter != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void serialize(Object paramObject, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */   public void serializeWithType(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 564 */     if (this._objectIdWriter != null) {
/* 565 */       gen.setCurrentValue(bean);
/* 566 */       _serializeWithObjectId(bean, gen, provider, typeSer);
/* 567 */       return;
/*     */     }
/*     */     
/* 570 */     String typeStr = this._typeId == null ? null : _customTypeId(bean);
/* 571 */     if (typeStr == null) {
/* 572 */       typeSer.writeTypePrefixForObject(bean, gen);
/*     */     } else {
/* 574 */       typeSer.writeCustomTypePrefixForObject(bean, gen, typeStr);
/*     */     }
/* 576 */     gen.setCurrentValue(bean);
/* 577 */     if (this._propertyFilterId != null) {
/* 578 */       serializeFieldsFiltered(bean, gen, provider);
/*     */     } else {
/* 580 */       serializeFields(bean, gen, provider);
/*     */     }
/* 582 */     if (typeStr == null) {
/* 583 */       typeSer.writeTypeSuffixForObject(bean, gen);
/*     */     } else {
/* 585 */       typeSer.writeCustomTypeSuffixForObject(bean, gen, typeStr);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void _serializeWithObjectId(Object bean, JsonGenerator gen, SerializerProvider provider, boolean startEndObject)
/*     */     throws IOException
/*     */   {
/* 592 */     ObjectIdWriter w = this._objectIdWriter;
/* 593 */     WritableObjectId objectId = provider.findObjectId(bean, w.generator);
/*     */     
/* 595 */     if (objectId.writeAsId(gen, provider, w)) {
/* 596 */       return;
/*     */     }
/*     */     
/* 599 */     Object id = objectId.generateId(bean);
/* 600 */     if (w.alwaysAsId) {
/* 601 */       w.serializer.serialize(id, gen, provider);
/* 602 */       return;
/*     */     }
/* 604 */     if (startEndObject) {
/* 605 */       gen.writeStartObject(bean);
/*     */     }
/* 607 */     objectId.writeAsField(gen, provider, w);
/* 608 */     if (this._propertyFilterId != null) {
/* 609 */       serializeFieldsFiltered(bean, gen, provider);
/*     */     } else {
/* 611 */       serializeFields(bean, gen, provider);
/*     */     }
/* 613 */     if (startEndObject) {
/* 614 */       gen.writeEndObject();
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void _serializeWithObjectId(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 621 */     ObjectIdWriter w = this._objectIdWriter;
/* 622 */     WritableObjectId objectId = provider.findObjectId(bean, w.generator);
/*     */     
/* 624 */     if (objectId.writeAsId(gen, provider, w)) {
/* 625 */       return;
/*     */     }
/*     */     
/* 628 */     Object id = objectId.generateId(bean);
/* 629 */     if (w.alwaysAsId) {
/* 630 */       w.serializer.serialize(id, gen, provider);
/* 631 */       return;
/*     */     }
/*     */     
/* 634 */     _serializeObjectId(bean, gen, provider, typeSer, objectId);
/*     */   }
/*     */   
/*     */   protected void _serializeObjectId(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer, WritableObjectId objectId)
/*     */     throws IOException
/*     */   {
/* 640 */     ObjectIdWriter w = this._objectIdWriter;
/* 641 */     String typeStr = this._typeId == null ? null : _customTypeId(bean);
/* 642 */     if (typeStr == null) {
/* 643 */       typeSer.writeTypePrefixForObject(bean, gen);
/*     */     } else {
/* 645 */       typeSer.writeCustomTypePrefixForObject(bean, gen, typeStr);
/*     */     }
/* 647 */     objectId.writeAsField(gen, provider, w);
/* 648 */     if (this._propertyFilterId != null) {
/* 649 */       serializeFieldsFiltered(bean, gen, provider);
/*     */     } else {
/* 651 */       serializeFields(bean, gen, provider);
/*     */     }
/* 653 */     if (typeStr == null) {
/* 654 */       typeSer.writeTypeSuffixForObject(bean, gen);
/*     */     } else {
/* 656 */       typeSer.writeCustomTypeSuffixForObject(bean, gen, typeStr);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final String _customTypeId(Object bean)
/*     */   {
/* 662 */     Object typeId = this._typeId.getValue(bean);
/* 663 */     if (typeId == null) {
/* 664 */       return "";
/*     */     }
/* 666 */     return (typeId instanceof String) ? (String)typeId : typeId.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void serializeFields(Object bean, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/*     */     BeanPropertyWriter[] props;
/*     */     
/*     */ 
/*     */     BeanPropertyWriter[] props;
/*     */     
/* 679 */     if ((this._filteredProps != null) && (provider.getActiveView() != null)) {
/* 680 */       props = this._filteredProps;
/*     */     } else {
/* 682 */       props = this._props;
/*     */     }
/* 684 */     int i = 0;
/*     */     try {
/* 686 */       for (int len = props.length; i < len; i++) {
/* 687 */         BeanPropertyWriter prop = props[i];
/* 688 */         if (prop != null) {
/* 689 */           prop.serializeAsField(bean, gen, provider);
/*     */         }
/*     */       }
/* 692 */       if (this._anyGetterWriter != null) {
/* 693 */         this._anyGetterWriter.getAndSerialize(bean, gen, provider);
/*     */       }
/*     */     } catch (Exception e) {
/* 696 */       String name = i == props.length ? "[anySetter]" : props[i].getName();
/* 697 */       wrapAndThrow(provider, e, bean, name);
/*     */ 
/*     */ 
/*     */     }
/*     */     catch (StackOverflowError e)
/*     */     {
/*     */ 
/*     */ 
/* 705 */       JsonMappingException mapE = new JsonMappingException(gen, "Infinite recursion (StackOverflowError)", e);
/*     */       
/* 707 */       String name = i == props.length ? "[anySetter]" : props[i].getName();
/* 708 */       mapE.prependPath(new JsonMappingException.Reference(bean, name));
/* 709 */       throw mapE;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void serializeFieldsFiltered(Object bean, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException, com.fasterxml.jackson.core.JsonGenerationException
/*     */   {
/*     */     BeanPropertyWriter[] props;
/*     */     
/*     */ 
/*     */ 
/*     */     BeanPropertyWriter[] props;
/*     */     
/*     */ 
/* 726 */     if ((this._filteredProps != null) && (provider.getActiveView() != null)) {
/* 727 */       props = this._filteredProps;
/*     */     } else {
/* 729 */       props = this._props;
/*     */     }
/* 731 */     PropertyFilter filter = findPropertyFilter(provider, this._propertyFilterId, bean);
/*     */     
/* 733 */     if (filter == null) {
/* 734 */       serializeFields(bean, gen, provider);
/* 735 */       return;
/*     */     }
/* 737 */     int i = 0;
/*     */     try {
/* 739 */       for (int len = props.length; i < len; i++) {
/* 740 */         BeanPropertyWriter prop = props[i];
/* 741 */         if (prop != null) {
/* 742 */           filter.serializeAsField(bean, gen, provider, prop);
/*     */         }
/*     */       }
/* 745 */       if (this._anyGetterWriter != null) {
/* 746 */         this._anyGetterWriter.getAndFilter(bean, gen, provider, filter);
/*     */       }
/*     */     } catch (Exception e) {
/* 749 */       String name = i == props.length ? "[anySetter]" : props[i].getName();
/* 750 */       wrapAndThrow(provider, e, bean, name);
/*     */     }
/*     */     catch (StackOverflowError e)
/*     */     {
/* 754 */       JsonMappingException mapE = new JsonMappingException(gen, "Infinite recursion (StackOverflowError)", e);
/* 755 */       String name = i == props.length ? "[anySetter]" : props[i].getName();
/* 756 */       mapE.prependPath(new JsonMappingException.Reference(bean, name));
/* 757 */       throw mapE;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public com.fasterxml.jackson.databind.JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 766 */     ObjectNode o = createSchemaNode("object", true);
/*     */     
/*     */ 
/* 769 */     JsonSerializableSchema ann = (JsonSerializableSchema)this._handledType.getAnnotation(JsonSerializableSchema.class);
/* 770 */     if (ann != null) {
/* 771 */       String id = ann.id();
/* 772 */       if ((id != null) && (id.length() > 0)) {
/* 773 */         o.put("id", id);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 779 */     ObjectNode propertiesNode = o.objectNode();
/*     */     PropertyFilter filter;
/* 781 */     PropertyFilter filter; if (this._propertyFilterId != null) {
/* 782 */       filter = findPropertyFilter(provider, this._propertyFilterId, null);
/*     */     } else {
/* 784 */       filter = null;
/*     */     }
/*     */     
/* 787 */     for (int i = 0; i < this._props.length; i++) {
/* 788 */       BeanPropertyWriter prop = this._props[i];
/* 789 */       if (filter == null) {
/* 790 */         prop.depositSchemaProperty(propertiesNode, provider);
/*     */       } else {
/* 792 */         filter.depositSchemaProperty(prop, propertiesNode, provider);
/*     */       }
/*     */     }
/*     */     
/* 796 */     o.set("properties", propertiesNode);
/* 797 */     return o;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 805 */     if (visitor == null) {
/* 806 */       return;
/*     */     }
/* 808 */     com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor objectVisitor = visitor.expectObjectFormat(typeHint);
/* 809 */     if (objectVisitor == null) {
/* 810 */       return;
/*     */     }
/* 812 */     SerializerProvider provider = visitor.getProvider();
/* 813 */     if (this._propertyFilterId != null) {
/* 814 */       PropertyFilter filter = findPropertyFilter(visitor.getProvider(), this._propertyFilterId, null);
/*     */       
/* 816 */       int i = 0; for (int end = this._props.length; i < end; i++) {
/* 817 */         filter.depositSchemaProperty(this._props[i], objectVisitor, provider);
/*     */       }
/*     */     } else {
/* 820 */       Class<?> view = (this._filteredProps == null) || (provider == null) ? null : provider.getActiveView();
/*     */       BeanPropertyWriter[] props;
/*     */       BeanPropertyWriter[] props;
/* 823 */       if (view != null) {
/* 824 */         props = this._filteredProps;
/*     */       } else {
/* 826 */         props = this._props;
/*     */       }
/*     */       
/* 829 */       int i = 0; for (int end = props.length; i < end; i++) {
/* 830 */         BeanPropertyWriter prop = props[i];
/* 831 */         if (prop != null) {
/* 832 */           prop.depositSchemaProperty(objectVisitor, provider);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\BeanSerializerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */