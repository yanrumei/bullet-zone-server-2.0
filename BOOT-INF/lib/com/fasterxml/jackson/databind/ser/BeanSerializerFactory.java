/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties.Value;
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.BeanProperty.Std;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.cfg.ConfigOverride;
/*     */ import com.fasterxml.jackson.databind.cfg.SerializerFactoryConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.FilteredBeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertyBasedObjectIdGenerator;
/*     */ import com.fasterxml.jackson.databind.ser.std.AtomicReferenceSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.MapSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
/*     */ import com.fasterxml.jackson.databind.type.ReferenceType;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ public class BeanSerializerFactory
/*     */   extends BasicSerializerFactory
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  67 */   public static final BeanSerializerFactory instance = new BeanSerializerFactory(null);
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
/*     */   protected BeanSerializerFactory(SerializerFactoryConfig config)
/*     */   {
/*  80 */     super(config);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerializerFactory withConfig(SerializerFactoryConfig config)
/*     */   {
/*  92 */     if (this._factoryConfig == config) {
/*  93 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 101 */     if (getClass() != BeanSerializerFactory.class) {
/* 102 */       throw new IllegalStateException("Subtype of BeanSerializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalSerializers': can not instantiate subtype with " + "additional serializer definitions");
/*     */     }
/*     */     
/*     */ 
/* 106 */     return new BeanSerializerFactory(config);
/*     */   }
/*     */   
/*     */   protected Iterable<Serializers> customSerializers()
/*     */   {
/* 111 */     return this._factoryConfig.serializers();
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
/*     */   public JsonSerializer<Object> createSerializer(SerializerProvider prov, JavaType origType)
/*     */     throws JsonMappingException
/*     */   {
/* 137 */     SerializationConfig config = prov.getConfig();
/* 138 */     BeanDescription beanDesc = config.introspect(origType);
/* 139 */     JsonSerializer<?> ser = findSerializerFromAnnotation(prov, beanDesc.getClassInfo());
/* 140 */     if (ser != null) {
/* 141 */       return ser;
/*     */     }
/*     */     
/*     */ 
/* 145 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/*     */     JavaType type;
/*     */     JavaType type;
/* 148 */     if (intr == null) {
/* 149 */       type = origType;
/*     */     } else
/*     */       try {
/* 152 */         type = intr.refineSerializationType(config, beanDesc.getClassInfo(), origType);
/*     */       } catch (JsonMappingException e) {
/* 154 */         return (JsonSerializer)prov.reportBadTypeDefinition(beanDesc, e.getMessage(), new Object[0]); }
/*     */     boolean staticTyping;
/*     */     boolean staticTyping;
/* 157 */     if (type == origType) {
/* 158 */       staticTyping = false;
/*     */     } else {
/* 160 */       staticTyping = true;
/* 161 */       if (!type.hasRawClass(origType.getRawClass())) {
/* 162 */         beanDesc = config.introspect(type);
/*     */       }
/*     */     }
/*     */     
/* 166 */     Converter<Object, Object> conv = beanDesc.findSerializationConverter();
/* 167 */     if (conv == null) {
/* 168 */       return _createSerializer2(prov, type, beanDesc, staticTyping);
/*     */     }
/* 170 */     JavaType delegateType = conv.getOutputType(prov.getTypeFactory());
/*     */     
/*     */ 
/* 173 */     if (!delegateType.hasRawClass(type.getRawClass())) {
/* 174 */       beanDesc = config.introspect(delegateType);
/*     */       
/* 176 */       ser = findSerializerFromAnnotation(prov, beanDesc.getClassInfo());
/*     */     }
/*     */     
/* 179 */     if ((ser == null) && (!delegateType.isJavaLangObject())) {
/* 180 */       ser = _createSerializer2(prov, delegateType, beanDesc, true);
/*     */     }
/* 182 */     return new StdDelegatingSerializer(conv, delegateType, ser);
/*     */   }
/*     */   
/*     */ 
/*     */   protected JsonSerializer<?> _createSerializer2(SerializerProvider prov, JavaType type, BeanDescription beanDesc, boolean staticTyping)
/*     */     throws JsonMappingException
/*     */   {
/* 189 */     JsonSerializer<?> ser = null;
/* 190 */     SerializationConfig config = prov.getConfig();
/*     */     
/*     */ 
/*     */ 
/* 194 */     if (type.isContainerType()) {
/* 195 */       if (!staticTyping) {
/* 196 */         staticTyping = usesStaticTyping(config, beanDesc, null);
/*     */       }
/*     */       
/* 199 */       ser = buildContainerSerializer(prov, type, beanDesc, staticTyping);
/*     */       
/* 201 */       if (ser != null) {
/* 202 */         return ser;
/*     */       }
/*     */     } else {
/* 205 */       if (type.isReferenceType()) {
/* 206 */         ser = findReferenceSerializer(prov, (ReferenceType)type, beanDesc, staticTyping);
/*     */       }
/*     */       else {
/* 209 */         for (Serializers serializers : customSerializers()) {
/* 210 */           ser = serializers.findSerializer(config, type, beanDesc);
/* 211 */           if (ser != null) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 218 */       if (ser == null) {
/* 219 */         ser = findSerializerByAnnotations(prov, type, beanDesc);
/*     */       }
/*     */     }
/*     */     
/* 223 */     if (ser == null)
/*     */     {
/*     */ 
/*     */ 
/* 227 */       ser = findSerializerByLookup(type, config, beanDesc, staticTyping);
/* 228 */       if (ser == null) {
/* 229 */         ser = findSerializerByPrimaryType(prov, type, beanDesc, staticTyping);
/* 230 */         if (ser == null)
/*     */         {
/*     */ 
/*     */ 
/* 234 */           ser = findBeanSerializer(prov, type, beanDesc);
/*     */           
/* 236 */           if (ser == null) {
/* 237 */             ser = findSerializerByAddonType(config, type, beanDesc, staticTyping);
/*     */             
/*     */ 
/*     */ 
/* 241 */             if (ser == null) {
/* 242 */               ser = prov.getUnknownTypeSerializer(beanDesc.getBeanClass());
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 248 */     if (ser != null)
/*     */     {
/* 250 */       if (this._factoryConfig.hasSerializerModifiers()) {
/* 251 */         for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 252 */           ser = mod.modifySerializer(config, beanDesc, ser);
/*     */         }
/*     */       }
/*     */     }
/* 256 */     return ser;
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
/*     */   public JsonSerializer<Object> findBeanSerializer(SerializerProvider prov, JavaType type, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 275 */     if (!isPotentialBeanType(type.getRawClass()))
/*     */     {
/*     */ 
/* 278 */       if (!type.isEnumType()) {
/* 279 */         return null;
/*     */       }
/*     */     }
/* 282 */     return constructBeanSerializer(prov, beanDesc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonSerializer<?> findReferenceSerializer(SerializerProvider prov, ReferenceType refType, BeanDescription beanDesc, boolean staticTyping)
/*     */     throws JsonMappingException
/*     */   {
/* 292 */     JavaType contentType = refType.getContentType();
/* 293 */     TypeSerializer contentTypeSerializer = (TypeSerializer)contentType.getTypeHandler();
/* 294 */     SerializationConfig config = prov.getConfig();
/* 295 */     if (contentTypeSerializer == null) {
/* 296 */       contentTypeSerializer = createTypeSerializer(config, contentType);
/*     */     }
/* 298 */     JsonSerializer<Object> contentSerializer = (JsonSerializer)contentType.getValueHandler();
/* 299 */     for (Serializers serializers : customSerializers()) {
/* 300 */       JsonSerializer<?> ser = serializers.findReferenceSerializer(config, refType, beanDesc, contentTypeSerializer, contentSerializer);
/*     */       
/* 302 */       if (ser != null) {
/* 303 */         return ser;
/*     */       }
/*     */     }
/* 306 */     if (refType.isTypeOrSubTypeOf(AtomicReference.class)) {
/* 307 */       return new AtomicReferenceSerializer(refType, staticTyping, contentTypeSerializer, contentSerializer);
/*     */     }
/*     */     
/* 310 */     return null;
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
/*     */   public TypeSerializer findPropertyTypeSerializer(JavaType baseType, SerializationConfig config, AnnotatedMember accessor)
/*     */     throws JsonMappingException
/*     */   {
/* 327 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 328 */     TypeResolverBuilder<?> b = ai.findPropertyTypeResolver(config, accessor, baseType);
/*     */     
/*     */     TypeSerializer typeSer;
/*     */     TypeSerializer typeSer;
/* 332 */     if (b == null) {
/* 333 */       typeSer = createTypeSerializer(config, baseType);
/*     */     } else {
/* 335 */       Collection<NamedType> subtypes = config.getSubtypeResolver().collectAndResolveSubtypesByClass(config, accessor, baseType);
/*     */       
/* 337 */       typeSer = b.buildTypeSerializer(config, baseType, subtypes);
/*     */     }
/* 339 */     return typeSer;
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
/*     */   public TypeSerializer findPropertyContentTypeSerializer(JavaType containerType, SerializationConfig config, AnnotatedMember accessor)
/*     */     throws JsonMappingException
/*     */   {
/* 356 */     JavaType contentType = containerType.getContentType();
/* 357 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 358 */     TypeResolverBuilder<?> b = ai.findPropertyContentTypeResolver(config, accessor, containerType);
/*     */     
/*     */     TypeSerializer typeSer;
/*     */     TypeSerializer typeSer;
/* 362 */     if (b == null) {
/* 363 */       typeSer = createTypeSerializer(config, contentType);
/*     */     } else {
/* 365 */       Collection<NamedType> subtypes = config.getSubtypeResolver().collectAndResolveSubtypesByClass(config, accessor, contentType);
/*     */       
/* 367 */       typeSer = b.buildTypeSerializer(config, contentType, subtypes);
/*     */     }
/* 369 */     return typeSer;
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
/*     */   protected JsonSerializer<Object> constructBeanSerializer(SerializerProvider prov, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 390 */     if (beanDesc.getBeanClass() == Object.class) {
/* 391 */       return prov.getUnknownTypeSerializer(Object.class);
/*     */     }
/*     */     
/* 394 */     SerializationConfig config = prov.getConfig();
/* 395 */     BeanSerializerBuilder builder = constructBeanSerializerBuilder(beanDesc);
/* 396 */     builder.setConfig(config);
/*     */     
/*     */ 
/* 399 */     List<BeanPropertyWriter> props = findBeanProperties(prov, beanDesc, builder);
/* 400 */     if (props == null) {
/* 401 */       props = new ArrayList();
/*     */     } else {
/* 403 */       props = removeOverlappingTypeIds(prov, beanDesc, builder, props);
/*     */     }
/*     */     
/*     */ 
/* 407 */     prov.getAnnotationIntrospector().findAndAddVirtualProperties(config, beanDesc.getClassInfo(), props);
/*     */     
/*     */ 
/* 410 */     if (this._factoryConfig.hasSerializerModifiers()) {
/* 411 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 412 */         props = mod.changeProperties(config, beanDesc, props);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 417 */     props = filterBeanProperties(config, beanDesc, props);
/*     */     
/*     */ 
/* 420 */     if (this._factoryConfig.hasSerializerModifiers()) {
/* 421 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 422 */         props = mod.orderProperties(config, beanDesc, props);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 430 */     builder.setObjectIdWriter(constructObjectIdHandler(prov, beanDesc, props));
/*     */     
/* 432 */     builder.setProperties(props);
/* 433 */     builder.setFilterId(findFilterId(config, beanDesc));
/*     */     
/* 435 */     AnnotatedMember anyGetter = beanDesc.findAnyGetter();
/* 436 */     if (anyGetter != null) {
/* 437 */       JavaType type = anyGetter.getType();
/*     */       
/* 439 */       boolean staticTyping = config.isEnabled(MapperFeature.USE_STATIC_TYPING);
/* 440 */       JavaType valueType = type.getContentType();
/* 441 */       TypeSerializer typeSer = createTypeSerializer(config, valueType);
/*     */       
/*     */ 
/* 444 */       JsonSerializer<?> anySer = findSerializerFromAnnotation(prov, anyGetter);
/* 445 */       if (anySer == null)
/*     */       {
/* 447 */         anySer = MapSerializer.construct((Set)null, type, staticTyping, typeSer, null, null, null);
/*     */       }
/*     */       
/*     */ 
/* 451 */       PropertyName name = PropertyName.construct(anyGetter.getName());
/* 452 */       BeanProperty.Std anyProp = new BeanProperty.Std(name, valueType, null, beanDesc.getClassAnnotations(), anyGetter, PropertyMetadata.STD_OPTIONAL);
/*     */       
/* 454 */       builder.setAnyGetter(new AnyGetterWriter(anyProp, anyGetter, anySer));
/*     */     }
/*     */     
/* 457 */     processViews(config, builder);
/*     */     
/*     */ 
/* 460 */     if (this._factoryConfig.hasSerializerModifiers()) {
/* 461 */       for (BeanSerializerModifier mod : this._factoryConfig.serializerModifiers()) {
/* 462 */         builder = mod.updateBuilder(config, beanDesc, builder);
/*     */       }
/*     */     }
/*     */     
/* 466 */     JsonSerializer<Object> ser = builder.build();
/* 467 */     if (ser == null)
/*     */     {
/*     */ 
/*     */ 
/* 471 */       if (beanDesc.hasKnownClassAnnotations()) {
/* 472 */         return builder.createDummy();
/*     */       }
/*     */     }
/* 475 */     return ser;
/*     */   }
/*     */   
/*     */ 
/*     */   protected ObjectIdWriter constructObjectIdHandler(SerializerProvider prov, BeanDescription beanDesc, List<BeanPropertyWriter> props)
/*     */     throws JsonMappingException
/*     */   {
/* 482 */     ObjectIdInfo objectIdInfo = beanDesc.getObjectIdInfo();
/* 483 */     if (objectIdInfo == null) {
/* 484 */       return null;
/*     */     }
/*     */     
/* 487 */     Class<?> implClass = objectIdInfo.getGeneratorType();
/*     */     
/*     */ 
/* 490 */     if (implClass == ObjectIdGenerators.PropertyGenerator.class) {
/* 491 */       String propName = objectIdInfo.getPropertyName().getSimpleName();
/* 492 */       BeanPropertyWriter idProp = null;
/*     */       
/* 494 */       int i = 0; for (int len = props.size();; i++) {
/* 495 */         if (i == len) {
/* 496 */           throw new IllegalArgumentException("Invalid Object Id definition for " + beanDesc.getBeanClass().getName() + ": can not find property with name '" + propName + "'");
/*     */         }
/*     */         
/* 499 */         BeanPropertyWriter prop = (BeanPropertyWriter)props.get(i);
/* 500 */         if (propName.equals(prop.getName())) {
/* 501 */           idProp = prop;
/*     */           
/*     */ 
/*     */ 
/* 505 */           if (i <= 0) break;
/* 506 */           props.remove(i);
/* 507 */           props.add(0, idProp); break;
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 512 */       JavaType idType = idProp.getType();
/* 513 */       ObjectIdGenerator<?> gen = new PropertyBasedObjectIdGenerator(objectIdInfo, idProp);
/*     */       
/* 515 */       return ObjectIdWriter.construct(idType, (PropertyName)null, gen, objectIdInfo.getAlwaysAsId());
/*     */     }
/*     */     
/*     */ 
/* 519 */     JavaType type = prov.constructType(implClass);
/*     */     
/* 521 */     JavaType idType = prov.getTypeFactory().findTypeParameters(type, ObjectIdGenerator.class)[0];
/* 522 */     ObjectIdGenerator<?> gen = prov.objectIdGeneratorInstance(beanDesc.getClassInfo(), objectIdInfo);
/* 523 */     return ObjectIdWriter.construct(idType, objectIdInfo.getPropertyName(), gen, objectIdInfo.getAlwaysAsId());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanPropertyWriter constructFilteredBeanWriter(BeanPropertyWriter writer, Class<?>[] inViews)
/*     */   {
/* 535 */     return FilteredBeanPropertyWriter.constructViewBased(writer, inViews);
/*     */   }
/*     */   
/*     */ 
/*     */   protected PropertyBuilder constructPropertyBuilder(SerializationConfig config, BeanDescription beanDesc)
/*     */   {
/* 541 */     return new PropertyBuilder(config, beanDesc);
/*     */   }
/*     */   
/*     */   protected BeanSerializerBuilder constructBeanSerializerBuilder(BeanDescription beanDesc) {
/* 545 */     return new BeanSerializerBuilder(beanDesc);
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
/*     */   protected boolean isPotentialBeanType(Class<?> type)
/*     */   {
/* 564 */     return (ClassUtil.canBeABeanType(type) == null) && (!ClassUtil.isProxyType(type));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<BeanPropertyWriter> findBeanProperties(SerializerProvider prov, BeanDescription beanDesc, BeanSerializerBuilder builder)
/*     */     throws JsonMappingException
/*     */   {
/* 575 */     List<BeanPropertyDefinition> properties = beanDesc.findProperties();
/* 576 */     SerializationConfig config = prov.getConfig();
/*     */     
/*     */ 
/* 579 */     removeIgnorableTypes(config, beanDesc, properties);
/*     */     
/*     */ 
/* 582 */     if (config.isEnabled(MapperFeature.REQUIRE_SETTERS_FOR_GETTERS)) {
/* 583 */       removeSetterlessGetters(config, beanDesc, properties);
/*     */     }
/*     */     
/*     */ 
/* 587 */     if (properties.isEmpty()) {
/* 588 */       return null;
/*     */     }
/*     */     
/* 591 */     boolean staticTyping = usesStaticTyping(config, beanDesc, null);
/* 592 */     PropertyBuilder pb = constructPropertyBuilder(config, beanDesc);
/*     */     
/* 594 */     ArrayList<BeanPropertyWriter> result = new ArrayList(properties.size());
/* 595 */     for (BeanPropertyDefinition property : properties) {
/* 596 */       AnnotatedMember accessor = property.getAccessor();
/*     */       
/* 598 */       if (property.isTypeId()) {
/* 599 */         if (accessor != null) {
/* 600 */           builder.setTypeId(accessor);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 605 */         AnnotationIntrospector.ReferenceProperty refType = property.findReferenceType();
/* 606 */         if ((refType == null) || (!refType.isBackReference()))
/*     */         {
/*     */ 
/* 609 */           if ((accessor instanceof AnnotatedMethod)) {
/* 610 */             result.add(_constructWriter(prov, property, pb, staticTyping, (AnnotatedMethod)accessor));
/*     */           } else
/* 612 */             result.add(_constructWriter(prov, property, pb, staticTyping, (AnnotatedField)accessor)); }
/*     */       }
/*     */     }
/* 615 */     return result;
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
/*     */   protected List<BeanPropertyWriter> filterBeanProperties(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyWriter> props)
/*     */   {
/* 635 */     JsonIgnoreProperties.Value ignorals = config.getDefaultPropertyIgnorals(beanDesc.getBeanClass(), beanDesc.getClassInfo());
/*     */     
/* 637 */     if (ignorals != null) {
/* 638 */       Set<String> ignored = ignorals.findIgnoredForSerialization();
/* 639 */       if (!ignored.isEmpty()) {
/* 640 */         Iterator<BeanPropertyWriter> it = props.iterator();
/* 641 */         while (it.hasNext()) {
/* 642 */           if (ignored.contains(((BeanPropertyWriter)it.next()).getName())) {
/* 643 */             it.remove();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 648 */     return props;
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
/*     */   protected void processViews(SerializationConfig config, BeanSerializerBuilder builder)
/*     */   {
/* 663 */     List<BeanPropertyWriter> props = builder.getProperties();
/* 664 */     boolean includeByDefault = config.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION);
/* 665 */     int propCount = props.size();
/* 666 */     int viewsFound = 0;
/* 667 */     BeanPropertyWriter[] filtered = new BeanPropertyWriter[propCount];
/*     */     
/* 669 */     for (int i = 0; i < propCount; i++) {
/* 670 */       BeanPropertyWriter bpw = (BeanPropertyWriter)props.get(i);
/* 671 */       Class<?>[] views = bpw.getViews();
/* 672 */       if (views == null) {
/* 673 */         if (includeByDefault) {
/* 674 */           filtered[i] = bpw;
/*     */         }
/*     */       } else {
/* 677 */         viewsFound++;
/* 678 */         filtered[i] = constructFilteredBeanWriter(bpw, views);
/*     */       }
/*     */     }
/*     */     
/* 682 */     if ((includeByDefault) && (viewsFound == 0)) {
/* 683 */       return;
/*     */     }
/* 685 */     builder.setFilteredProperties(filtered);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void removeIgnorableTypes(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyDefinition> properties)
/*     */   {
/* 696 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 697 */     HashMap<Class<?>, Boolean> ignores = new HashMap();
/* 698 */     Iterator<BeanPropertyDefinition> it = properties.iterator();
/* 699 */     while (it.hasNext()) {
/* 700 */       BeanPropertyDefinition property = (BeanPropertyDefinition)it.next();
/* 701 */       AnnotatedMember accessor = property.getAccessor();
/* 702 */       if (accessor == null) {
/* 703 */         it.remove();
/*     */       }
/*     */       else {
/* 706 */         Class<?> type = accessor.getRawType();
/* 707 */         Boolean result = (Boolean)ignores.get(type);
/* 708 */         if (result == null)
/*     */         {
/* 710 */           ConfigOverride override = config.findConfigOverride(type);
/* 711 */           if (override != null) {
/* 712 */             result = override.getIsIgnoredType();
/*     */           }
/* 714 */           if (result == null) {
/* 715 */             BeanDescription desc = config.introspectClassAnnotations(type);
/* 716 */             AnnotatedClass ac = desc.getClassInfo();
/* 717 */             result = intr.isIgnorableType(ac);
/*     */             
/* 719 */             if (result == null) {
/* 720 */               result = Boolean.FALSE;
/*     */             }
/*     */           }
/* 723 */           ignores.put(type, result);
/*     */         }
/*     */         
/* 726 */         if (result.booleanValue()) {
/* 727 */           it.remove();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void removeSetterlessGetters(SerializationConfig config, BeanDescription beanDesc, List<BeanPropertyDefinition> properties)
/*     */   {
/* 738 */     Iterator<BeanPropertyDefinition> it = properties.iterator();
/* 739 */     while (it.hasNext()) {
/* 740 */       BeanPropertyDefinition property = (BeanPropertyDefinition)it.next();
/*     */       
/*     */ 
/* 743 */       if ((!property.couldDeserialize()) && (!property.isExplicitlyIncluded())) {
/* 744 */         it.remove();
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
/*     */ 
/*     */ 
/*     */   protected List<BeanPropertyWriter> removeOverlappingTypeIds(SerializerProvider prov, BeanDescription beanDesc, BeanSerializerBuilder builder, List<BeanPropertyWriter> props)
/*     */   {
/* 759 */     int i = 0; BeanPropertyWriter bpw; PropertyName typePropName; for (int end = props.size(); i < end; i++) {
/* 760 */       bpw = (BeanPropertyWriter)props.get(i);
/* 761 */       TypeSerializer td = bpw.getTypeSerializer();
/* 762 */       if ((td != null) && (td.getTypeInclusion() == JsonTypeInfo.As.EXTERNAL_PROPERTY))
/*     */       {
/*     */ 
/* 765 */         String n = td.getPropertyName();
/* 766 */         typePropName = PropertyName.construct(n);
/*     */         
/* 768 */         for (BeanPropertyWriter w2 : props)
/* 769 */           if ((w2 != bpw) && (w2.wouldConflictWithName(typePropName))) {
/* 770 */             bpw.assignTypeSerializer(null);
/* 771 */             break;
/*     */           }
/*     */       }
/*     */     }
/* 775 */     return props;
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
/*     */   protected BeanPropertyWriter _constructWriter(SerializerProvider prov, BeanPropertyDefinition propDef, PropertyBuilder pb, boolean staticTyping, AnnotatedMember accessor)
/*     */     throws JsonMappingException
/*     */   {
/* 793 */     PropertyName name = propDef.getFullName();
/* 794 */     JavaType type = accessor.getType();
/* 795 */     BeanProperty.Std property = new BeanProperty.Std(name, type, propDef.getWrapperName(), pb.getClassAnnotations(), accessor, propDef.getMetadata());
/*     */     
/*     */ 
/*     */ 
/* 799 */     JsonSerializer<?> annotatedSerializer = findSerializerFromAnnotation(prov, accessor);
/*     */     
/*     */ 
/*     */ 
/* 803 */     if ((annotatedSerializer instanceof ResolvableSerializer)) {
/* 804 */       ((ResolvableSerializer)annotatedSerializer).resolve(prov);
/*     */     }
/*     */     
/* 807 */     annotatedSerializer = prov.handlePrimaryContextualization(annotatedSerializer, property);
/*     */     
/* 809 */     TypeSerializer contentTypeSer = null;
/*     */     
/* 811 */     if ((type.isContainerType()) || (type.isReferenceType())) {
/* 812 */       contentTypeSer = findPropertyContentTypeSerializer(type, prov.getConfig(), accessor);
/*     */     }
/*     */     
/* 815 */     TypeSerializer typeSer = findPropertyTypeSerializer(type, prov.getConfig(), accessor);
/* 816 */     return pb.buildWriter(prov, propDef, type, annotatedSerializer, typeSer, contentTypeSer, accessor, staticTyping);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\BeanSerializerFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */