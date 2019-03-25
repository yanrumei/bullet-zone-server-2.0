/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties.Value;
/*     */ import com.fasterxml.jackson.databind.AbstractTypeResolver;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.BeanProperty.Std;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value;
/*     */ import com.fasterxml.jackson.databind.cfg.ConfigOverride;
/*     */ import com.fasterxml.jackson.databind.cfg.DeserializerFactoryConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class BeanDeserializerFactory extends BasicDeserializerFactory implements java.io.Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  38 */   private static final Class<?>[] INIT_CAUSE_PARAMS = { Throwable.class };
/*     */   
/*  40 */   private static final Class<?>[] NO_VIEWS = new Class[0];
/*     */   
/*     */ 
/*     */ 
/*     */   protected static final Set<String> DEFAULT_NO_DESER_CLASS_NAMES;
/*     */   
/*     */ 
/*     */ 
/*     */   static
/*     */   {
/*  50 */     Set<String> s = new java.util.HashSet();
/*     */     
/*     */ 
/*  53 */     s.add("org.apache.commons.collections.functors.InvokerTransformer");
/*  54 */     s.add("org.apache.commons.collections.functors.InstantiateTransformer");
/*  55 */     s.add("org.apache.commons.collections4.functors.InvokerTransformer");
/*  56 */     s.add("org.apache.commons.collections4.functors.InstantiateTransformer");
/*  57 */     s.add("org.codehaus.groovy.runtime.ConvertedClosure");
/*  58 */     s.add("org.codehaus.groovy.runtime.MethodClosure");
/*  59 */     s.add("org.springframework.beans.factory.ObjectFactory");
/*  60 */     s.add("com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl");
/*  61 */     s.add("org.apache.xalan.xsltc.trax.TemplatesImpl");
/*     */     
/*  63 */     s.add("com.sun.rowset.JdbcRowSetImpl");
/*     */     
/*  65 */     s.add("java.util.logging.FileHandler");
/*  66 */     s.add("java.rmi.server.UnicastRemoteObject");
/*     */     
/*  68 */     s.add("org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor");
/*  69 */     s.add("org.springframework.beans.factory.config.PropertyPathFactoryBean");
/*  70 */     s.add("com.mchange.v2.c3p0.JndiRefForwardingDataSource");
/*  71 */     s.add("com.mchange.v2.c3p0.WrapperConnectionPoolDataSource");
/*     */     
/*  73 */     DEFAULT_NO_DESER_CLASS_NAMES = Collections.unmodifiableSet(s);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  81 */   protected Set<String> _cfgIllegalClassNames = DEFAULT_NO_DESER_CLASS_NAMES;
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
/*  93 */   public static final BeanDeserializerFactory instance = new BeanDeserializerFactory(new DeserializerFactoryConfig());
/*     */   
/*     */   public BeanDeserializerFactory(DeserializerFactoryConfig config)
/*     */   {
/*  97 */     super(config);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializerFactory withConfig(DeserializerFactoryConfig config)
/*     */   {
/* 108 */     if (this._factoryConfig == config) {
/* 109 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 117 */     if (getClass() != BeanDeserializerFactory.class) {
/* 118 */       throw new IllegalStateException("Subtype of BeanDeserializerFactory (" + getClass().getName() + ") has not properly overridden method 'withAdditionalDeserializers': can not instantiate subtype with " + "additional deserializer definitions");
/*     */     }
/*     */     
/*     */ 
/* 122 */     return new BeanDeserializerFactory(config);
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
/*     */   public JsonDeserializer<Object> createBeanDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 141 */     DeserializationConfig config = ctxt.getConfig();
/*     */     
/* 143 */     JsonDeserializer<Object> custom = _findCustomBeanDeserializer(type, config, beanDesc);
/* 144 */     if (custom != null) {
/* 145 */       return custom;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 151 */     if (type.isThrowable()) {
/* 152 */       return buildThrowableDeserializer(ctxt, type, beanDesc);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 159 */     if ((type.isAbstract()) && (!type.isPrimitive()) && (!type.isEnumType()))
/*     */     {
/* 161 */       JavaType concreteType = materializeAbstractType(ctxt, type, beanDesc);
/* 162 */       if (concreteType != null)
/*     */       {
/*     */ 
/*     */ 
/* 166 */         beanDesc = config.introspect(concreteType);
/* 167 */         return buildBeanDeserializer(ctxt, concreteType, beanDesc);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 172 */     JsonDeserializer<Object> deser = findStdDeserializer(ctxt, type, beanDesc);
/* 173 */     if (deser != null) {
/* 174 */       return deser;
/*     */     }
/*     */     
/*     */ 
/* 178 */     if (!isPotentialBeanType(type.getRawClass())) {
/* 179 */       return null;
/*     */     }
/*     */     
/* 182 */     checkIllegalTypes(ctxt, type, beanDesc);
/*     */     
/* 184 */     return buildBeanDeserializer(ctxt, type, beanDesc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<Object> createBuilderBasedDeserializer(DeserializationContext ctxt, JavaType valueType, BeanDescription beanDesc, Class<?> builderClass)
/*     */     throws JsonMappingException
/*     */   {
/* 194 */     JavaType builderType = ctxt.constructType(builderClass);
/* 195 */     BeanDescription builderDesc = ctxt.getConfig().introspectForBuilder(builderType);
/* 196 */     return buildBuilderBasedDeserializer(ctxt, valueType, builderDesc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<?> findStdDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 209 */     JsonDeserializer<?> deser = findDefaultDeserializer(ctxt, type, beanDesc);
/*     */     
/* 211 */     if ((deser != null) && 
/* 212 */       (this._factoryConfig.hasDeserializerModifiers())) {
/* 213 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 214 */         deser = mod.modifyDeserializer(ctxt.getConfig(), beanDesc, deser);
/*     */       }
/*     */     }
/*     */     
/* 218 */     return deser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JavaType materializeAbstractType(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 226 */     for (AbstractTypeResolver r : this._factoryConfig.abstractTypeResolvers()) {
/* 227 */       JavaType concrete = r.resolveAbstractType(ctxt.getConfig(), beanDesc);
/* 228 */       if (concrete != null) {
/* 229 */         return concrete;
/*     */       }
/*     */     }
/* 232 */     return null;
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
/*     */   public JsonDeserializer<Object> buildBeanDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/*     */     ValueInstantiator valueInstantiator;
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
/*     */     try
/*     */     {
/* 262 */       valueInstantiator = findValueInstantiator(ctxt, beanDesc);
/*     */     } catch (NoClassDefFoundError error) {
/* 264 */       return new com.fasterxml.jackson.databind.deser.impl.ErrorThrowingDeserializer(error);
/*     */     }
/* 266 */     BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(ctxt, beanDesc);
/* 267 */     builder.setValueInstantiator(valueInstantiator);
/*     */     
/* 269 */     addBeanProps(ctxt, beanDesc, builder);
/* 270 */     addObjectIdReader(ctxt, beanDesc, builder);
/*     */     
/*     */ 
/* 273 */     addReferenceProperties(ctxt, beanDesc, builder);
/* 274 */     addInjectables(ctxt, beanDesc, builder);
/*     */     
/* 276 */     DeserializationConfig config = ctxt.getConfig();
/* 277 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 278 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 279 */         builder = mod.updateBuilder(config, beanDesc, builder);
/*     */       }
/*     */     }
/*     */     
/*     */     JsonDeserializer<?> deserializer;
/*     */     
/*     */     JsonDeserializer<?> deserializer;
/*     */     
/* 287 */     if ((type.isAbstract()) && (!valueInstantiator.canInstantiate())) {
/* 288 */       deserializer = builder.buildAbstract();
/*     */     } else {
/* 290 */       deserializer = builder.build();
/*     */     }
/*     */     
/*     */ 
/* 294 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 295 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 296 */         deserializer = mod.modifyDeserializer(config, beanDesc, deserializer);
/*     */       }
/*     */     }
/* 299 */     return deserializer;
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
/*     */   protected JsonDeserializer<Object> buildBuilderBasedDeserializer(DeserializationContext ctxt, JavaType valueType, BeanDescription builderDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 315 */     ValueInstantiator valueInstantiator = findValueInstantiator(ctxt, builderDesc);
/* 316 */     DeserializationConfig config = ctxt.getConfig();
/* 317 */     BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(ctxt, builderDesc);
/* 318 */     builder.setValueInstantiator(valueInstantiator);
/*     */     
/* 320 */     addBeanProps(ctxt, builderDesc, builder);
/* 321 */     addObjectIdReader(ctxt, builderDesc, builder);
/*     */     
/*     */ 
/* 324 */     addReferenceProperties(ctxt, builderDesc, builder);
/* 325 */     addInjectables(ctxt, builderDesc, builder);
/*     */     
/* 327 */     JsonPOJOBuilder.Value builderConfig = builderDesc.findPOJOBuilderConfig();
/* 328 */     String buildMethodName = builderConfig == null ? "build" : builderConfig.buildMethodName;
/*     */     
/*     */ 
/*     */ 
/* 332 */     AnnotatedMethod buildMethod = builderDesc.findMethod(buildMethodName, null);
/* 333 */     if ((buildMethod != null) && 
/* 334 */       (config.canOverrideAccessModifiers())) {
/* 335 */       ClassUtil.checkAndFixAccess(buildMethod.getMember(), config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*     */     }
/*     */     
/* 338 */     builder.setPOJOBuilder(buildMethod, builderConfig);
/*     */     
/* 340 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 341 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 342 */         builder = mod.updateBuilder(config, builderDesc, builder);
/*     */       }
/*     */     }
/* 345 */     JsonDeserializer<?> deserializer = builder.buildBuilderBased(valueType, buildMethodName);
/*     */     
/*     */ 
/*     */ 
/* 349 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 350 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 351 */         deserializer = mod.modifyDeserializer(config, builderDesc, deserializer);
/*     */       }
/*     */     }
/* 354 */     return deserializer;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void addObjectIdReader(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder)
/*     */     throws JsonMappingException
/*     */   {
/* 361 */     ObjectIdInfo objectIdInfo = beanDesc.getObjectIdInfo();
/* 362 */     if (objectIdInfo == null) {
/* 363 */       return;
/*     */     }
/* 365 */     Class<?> implClass = objectIdInfo.getGeneratorType();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 370 */     com.fasterxml.jackson.annotation.ObjectIdResolver resolver = ctxt.objectIdResolverInstance(beanDesc.getClassInfo(), objectIdInfo);
/*     */     com.fasterxml.jackson.annotation.ObjectIdGenerator<?> gen;
/*     */     JavaType idType;
/* 373 */     SettableBeanProperty idProp; com.fasterxml.jackson.annotation.ObjectIdGenerator<?> gen; if (implClass == com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class) {
/* 374 */       PropertyName propName = objectIdInfo.getPropertyName();
/* 375 */       SettableBeanProperty idProp = builder.findProperty(propName);
/* 376 */       if (idProp == null) {
/* 377 */         throw new IllegalArgumentException("Invalid Object Id definition for " + beanDesc.getBeanClass().getName() + ": can not find property with name '" + propName + "'");
/*     */       }
/*     */       
/* 380 */       JavaType idType = idProp.getType();
/* 381 */       gen = new com.fasterxml.jackson.databind.deser.impl.PropertyBasedObjectIdGenerator(objectIdInfo.getScope());
/*     */     } else {
/* 383 */       JavaType type = ctxt.constructType(implClass);
/* 384 */       idType = ctxt.getTypeFactory().findTypeParameters(type, com.fasterxml.jackson.annotation.ObjectIdGenerator.class)[0];
/* 385 */       idProp = null;
/* 386 */       gen = ctxt.objectIdGeneratorInstance(beanDesc.getClassInfo(), objectIdInfo);
/*     */     }
/*     */     
/* 389 */     JsonDeserializer<?> deser = ctxt.findRootValueDeserializer(idType);
/* 390 */     builder.setObjectIdReader(com.fasterxml.jackson.databind.deser.impl.ObjectIdReader.construct(idType, objectIdInfo.getPropertyName(), gen, deser, idProp, resolver));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<Object> buildThrowableDeserializer(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 399 */     DeserializationConfig config = ctxt.getConfig();
/*     */     
/* 401 */     BeanDeserializerBuilder builder = constructBeanDeserializerBuilder(ctxt, beanDesc);
/* 402 */     builder.setValueInstantiator(findValueInstantiator(ctxt, beanDesc));
/*     */     
/* 404 */     addBeanProps(ctxt, beanDesc, builder);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 411 */     AnnotatedMethod am = beanDesc.findMethod("initCause", INIT_CAUSE_PARAMS);
/* 412 */     if (am != null) {
/* 413 */       SimpleBeanPropertyDefinition propDef = SimpleBeanPropertyDefinition.construct(ctxt.getConfig(), am, new PropertyName("cause"));
/*     */       
/* 415 */       SettableBeanProperty prop = constructSettableProperty(ctxt, beanDesc, propDef, am.getParameterType(0));
/*     */       
/* 417 */       if (prop != null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 422 */         builder.addOrReplaceProperty(prop, true);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 427 */     builder.addIgnorable("localizedMessage");
/*     */     
/* 429 */     builder.addIgnorable("suppressed");
/*     */     
/*     */ 
/*     */ 
/* 433 */     builder.addIgnorable("message");
/*     */     
/*     */ 
/* 436 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 437 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 438 */         builder = mod.updateBuilder(config, beanDesc, builder);
/*     */       }
/*     */     }
/* 441 */     JsonDeserializer<?> deserializer = builder.build();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 446 */     if ((deserializer instanceof BeanDeserializer)) {
/* 447 */       deserializer = new com.fasterxml.jackson.databind.deser.std.ThrowableDeserializer((BeanDeserializer)deserializer);
/*     */     }
/*     */     
/*     */ 
/* 451 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 452 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 453 */         deserializer = mod.modifyDeserializer(config, beanDesc, deserializer);
/*     */       }
/*     */     }
/* 456 */     return deserializer;
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
/*     */   protected BeanDeserializerBuilder constructBeanDeserializerBuilder(DeserializationContext ctxt, BeanDescription beanDesc)
/*     */   {
/* 473 */     return new BeanDeserializerBuilder(beanDesc, ctxt.getConfig());
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
/*     */   protected void addBeanProps(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder)
/*     */     throws JsonMappingException
/*     */   {
/* 487 */     boolean isConcrete = !beanDesc.getType().isAbstract();
/* 488 */     SettableBeanProperty[] creatorProps = isConcrete ? builder.getValueInstantiator().getFromObjectArguments(ctxt.getConfig()) : null;
/*     */     
/*     */ 
/* 491 */     boolean hasCreatorProps = creatorProps != null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 497 */     JsonIgnoreProperties.Value ignorals = ctxt.getConfig().getDefaultPropertyIgnorals(beanDesc.getBeanClass(), beanDesc.getClassInfo());
/*     */     
/*     */ 
/*     */     Set<String> ignored;
/*     */     
/* 502 */     if (ignorals != null) {
/* 503 */       boolean ignoreAny = ignorals.getIgnoreUnknown();
/* 504 */       builder.setIgnoreUnknownProperties(ignoreAny);
/*     */       
/* 506 */       Set<String> ignored = ignorals.findIgnoredForDeserialization();
/* 507 */       for (String propName : ignored) {
/* 508 */         builder.addIgnorable(propName);
/*     */       }
/*     */     } else {
/* 511 */       ignored = Collections.emptySet();
/*     */     }
/*     */     
/*     */ 
/* 515 */     AnnotatedMethod anySetterMethod = beanDesc.findAnySetter();
/* 516 */     AnnotatedMember anySetterField = null;
/* 517 */     if (anySetterMethod != null) {
/* 518 */       builder.setAnySetter(constructAnySetter(ctxt, beanDesc, anySetterMethod));
/*     */     }
/*     */     else {
/* 521 */       anySetterField = beanDesc.findAnySetterField();
/* 522 */       if (anySetterField != null) {
/* 523 */         builder.setAnySetter(constructAnySetter(ctxt, beanDesc, anySetterField));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 528 */     if ((anySetterMethod == null) && (anySetterField == null)) {
/* 529 */       Collection<String> ignored2 = beanDesc.getIgnoredPropertyNames();
/* 530 */       if (ignored2 != null) {
/* 531 */         for (String propName : ignored2)
/*     */         {
/*     */ 
/* 534 */           builder.addIgnorable(propName);
/*     */         }
/*     */       }
/*     */     }
/* 538 */     boolean useGettersAsSetters = (ctxt.isEnabled(MapperFeature.USE_GETTERS_AS_SETTERS)) && (ctxt.isEnabled(MapperFeature.AUTO_DETECT_GETTERS));
/*     */     
/*     */ 
/*     */ 
/* 542 */     List<BeanPropertyDefinition> propDefs = filterBeanProps(ctxt, beanDesc, builder, beanDesc.findProperties(), ignored);
/*     */     
/*     */ 
/*     */ 
/* 546 */     if (this._factoryConfig.hasDeserializerModifiers()) {
/* 547 */       for (BeanDeserializerModifier mod : this._factoryConfig.deserializerModifiers()) {
/* 548 */         propDefs = mod.updateProperties(ctxt.getConfig(), beanDesc, propDefs);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 553 */     for (BeanPropertyDefinition propDef : propDefs) {
/* 554 */       SettableBeanProperty prop = null;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 559 */       if (propDef.hasSetter()) {
/* 560 */         JavaType propertyType = propDef.getSetter().getParameterType(0);
/* 561 */         prop = constructSettableProperty(ctxt, beanDesc, propDef, propertyType);
/* 562 */       } else if (propDef.hasField()) {
/* 563 */         JavaType propertyType = propDef.getField().getType();
/* 564 */         prop = constructSettableProperty(ctxt, beanDesc, propDef, propertyType);
/* 565 */       } else if ((useGettersAsSetters) && (propDef.hasGetter()))
/*     */       {
/*     */ 
/*     */ 
/* 569 */         AnnotatedMethod getter = propDef.getGetter();
/*     */         
/* 571 */         Class<?> rawPropertyType = getter.getRawType();
/* 572 */         if ((Collection.class.isAssignableFrom(rawPropertyType)) || (Map.class.isAssignableFrom(rawPropertyType)))
/*     */         {
/* 574 */           prop = constructSetterlessProperty(ctxt, beanDesc, propDef);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 579 */       if ((hasCreatorProps) && (propDef.hasConstructorParameter()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 585 */         String name = propDef.getName();
/* 586 */         CreatorProperty cprop = null;
/* 587 */         if (creatorProps != null) {
/* 588 */           for (SettableBeanProperty cp : creatorProps) {
/* 589 */             if ((name.equals(cp.getName())) && ((cp instanceof CreatorProperty))) {
/* 590 */               cprop = (CreatorProperty)cp;
/* 591 */               break;
/*     */             }
/*     */           }
/*     */         }
/* 595 */         if (cprop == null) {
/* 596 */           List<String> n = new ArrayList();
/* 597 */           for (SettableBeanProperty cp : creatorProps) {
/* 598 */             n.add(cp.getName());
/*     */           }
/* 600 */           ctxt.reportBadPropertyDefinition(beanDesc, propDef, "Could not find creator property with name '%s' (known Creator properties: %s)", new Object[] { name, n });
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 605 */           if (prop != null) {
/* 606 */             cprop.setFallbackSetter(prop);
/*     */           }
/* 608 */           prop = cprop;
/* 609 */           builder.addCreatorProperty(cprop);
/*     */         }
/*     */         
/*     */       }
/* 613 */       else if (prop != null) {
/* 614 */         Class<?>[] views = propDef.findViews();
/* 615 */         if (views == null)
/*     */         {
/* 617 */           if (!ctxt.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION)) {
/* 618 */             views = NO_VIEWS;
/*     */           }
/*     */         }
/*     */         
/* 622 */         prop.setViews(views);
/* 623 */         builder.addProperty(prop);
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
/*     */ 
/*     */   protected List<BeanPropertyDefinition> filterBeanProps(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder, List<BeanPropertyDefinition> propDefsIn, Set<String> ignored)
/*     */     throws JsonMappingException
/*     */   {
/* 640 */     ArrayList<BeanPropertyDefinition> result = new ArrayList(Math.max(4, propDefsIn.size()));
/*     */     
/* 642 */     java.util.HashMap<Class<?>, Boolean> ignoredTypes = new java.util.HashMap();
/*     */     
/* 644 */     for (BeanPropertyDefinition property : propDefsIn) {
/* 645 */       String name = property.getName();
/* 646 */       if (!ignored.contains(name))
/*     */       {
/*     */ 
/* 649 */         if (!property.hasConstructorParameter()) {
/* 650 */           Class<?> rawPropertyType = null;
/* 651 */           if (property.hasSetter()) {
/* 652 */             rawPropertyType = property.getSetter().getRawParameterType(0);
/* 653 */           } else if (property.hasField()) {
/* 654 */             rawPropertyType = property.getField().getRawType();
/*     */           }
/*     */           
/*     */ 
/* 658 */           if ((rawPropertyType != null) && (isIgnorableType(ctxt.getConfig(), beanDesc, rawPropertyType, ignoredTypes)))
/*     */           {
/*     */ 
/* 661 */             builder.addIgnorable(name);
/* 662 */             continue;
/*     */           }
/*     */         }
/* 665 */         result.add(property);
/*     */       } }
/* 667 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void addReferenceProperties(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder)
/*     */     throws JsonMappingException
/*     */   {
/* 679 */     Map<String, AnnotatedMember> refs = beanDesc.findBackReferenceProperties();
/* 680 */     if (refs != null) {
/* 681 */       for (Map.Entry<String, AnnotatedMember> en : refs.entrySet()) {
/* 682 */         String name = (String)en.getKey();
/* 683 */         AnnotatedMember m = (AnnotatedMember)en.getValue();
/*     */         JavaType type;
/* 685 */         JavaType type; if ((m instanceof AnnotatedMethod)) {
/* 686 */           type = ((AnnotatedMethod)m).getParameterType(0);
/*     */         } else {
/* 688 */           type = m.getType();
/*     */           
/*     */ 
/* 691 */           if ((m instanceof AnnotatedParameter)) {
/* 692 */             ctxt.reportBadTypeDefinition(beanDesc, "Can not bind back references as Creator parameters: type %s (reference '%s', parameter index #%d)", new Object[] { beanDesc.getBeanClass().getName(), name, Integer.valueOf(((AnnotatedParameter)m).getIndex()) });
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 697 */         SimpleBeanPropertyDefinition propDef = SimpleBeanPropertyDefinition.construct(ctxt.getConfig(), m, PropertyName.construct(name));
/*     */         
/* 699 */         builder.addBackReferenceProperty(name, constructSettableProperty(ctxt, beanDesc, propDef, type));
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
/*     */   protected void addInjectables(DeserializationContext ctxt, BeanDescription beanDesc, BeanDeserializerBuilder builder)
/*     */     throws JsonMappingException
/*     */   {
/* 713 */     Map<Object, AnnotatedMember> raw = beanDesc.findInjectables();
/* 714 */     if (raw != null) {
/* 715 */       for (Map.Entry<Object, AnnotatedMember> entry : raw.entrySet()) {
/* 716 */         AnnotatedMember m = (AnnotatedMember)entry.getValue();
/* 717 */         builder.addInjectable(PropertyName.construct(m.getName()), m.getType(), beanDesc.getClassAnnotations(), m, entry.getKey());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableAnyProperty constructAnySetter(DeserializationContext ctxt, BeanDescription beanDesc, AnnotatedMember mutator)
/*     */     throws JsonMappingException
/*     */   {
/* 738 */     JavaType type = null;
/* 739 */     if ((mutator instanceof AnnotatedMethod))
/*     */     {
/* 741 */       type = ((AnnotatedMethod)mutator).getParameterType(1);
/* 742 */     } else if ((mutator instanceof AnnotatedField))
/*     */     {
/* 744 */       type = ((AnnotatedField)mutator).getType().getContentType();
/*     */     }
/*     */     
/*     */ 
/* 748 */     type = resolveMemberAndTypeAnnotations(ctxt, mutator, type);
/* 749 */     BeanProperty.Std prop = new BeanProperty.Std(PropertyName.construct(mutator.getName()), type, null, beanDesc.getClassAnnotations(), mutator, com.fasterxml.jackson.databind.PropertyMetadata.STD_OPTIONAL);
/*     */     
/*     */ 
/*     */ 
/* 753 */     JsonDeserializer<Object> deser = findDeserializerFromAnnotation(ctxt, mutator);
/* 754 */     if (deser == null) {
/* 755 */       deser = (JsonDeserializer)type.getValueHandler();
/*     */     }
/* 757 */     if (deser != null)
/*     */     {
/* 759 */       deser = ctxt.handlePrimaryContextualization(deser, prop, type);
/*     */     }
/* 761 */     TypeDeserializer typeDeser = (TypeDeserializer)type.getTypeHandler();
/* 762 */     return new SettableAnyProperty(prop, mutator, type, deser, typeDeser);
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
/*     */   protected SettableBeanProperty constructSettableProperty(DeserializationContext ctxt, BeanDescription beanDesc, BeanPropertyDefinition propDef, JavaType propType0)
/*     */     throws JsonMappingException
/*     */   {
/* 778 */     AnnotatedMember mutator = propDef.getNonConstructorMutator();
/*     */     
/*     */ 
/*     */ 
/* 782 */     if (mutator == null) {
/* 783 */       ctxt.reportBadPropertyDefinition(beanDesc, propDef, "No non-constructor mutator available", new Object[0]);
/*     */     }
/* 785 */     JavaType type = resolveMemberAndTypeAnnotations(ctxt, mutator, propType0);
/*     */     
/* 787 */     TypeDeserializer typeDeser = (TypeDeserializer)type.getTypeHandler();
/*     */     SettableBeanProperty prop;
/* 789 */     SettableBeanProperty prop; if ((mutator instanceof AnnotatedMethod)) {
/* 790 */       prop = new com.fasterxml.jackson.databind.deser.impl.MethodProperty(propDef, type, typeDeser, beanDesc.getClassAnnotations(), (AnnotatedMethod)mutator);
/*     */     }
/*     */     else
/*     */     {
/* 794 */       prop = new com.fasterxml.jackson.databind.deser.impl.FieldProperty(propDef, type, typeDeser, beanDesc.getClassAnnotations(), (AnnotatedField)mutator);
/*     */     }
/*     */     
/* 797 */     JsonDeserializer<?> deser = findDeserializerFromAnnotation(ctxt, mutator);
/* 798 */     if (deser == null) {
/* 799 */       deser = (JsonDeserializer)type.getValueHandler();
/*     */     }
/* 801 */     if (deser != null) {
/* 802 */       deser = ctxt.handlePrimaryContextualization(deser, prop, type);
/* 803 */       prop = prop.withValueDeserializer(deser);
/*     */     }
/*     */     
/* 806 */     AnnotationIntrospector.ReferenceProperty ref = propDef.findReferenceType();
/* 807 */     if ((ref != null) && (ref.isManagedReference())) {
/* 808 */       prop.setManagedReferenceName(ref.getName());
/*     */     }
/* 810 */     ObjectIdInfo objectIdInfo = propDef.findObjectIdInfo();
/* 811 */     if (objectIdInfo != null) {
/* 812 */       prop.setObjectIdInfo(objectIdInfo);
/*     */     }
/* 814 */     return prop;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableBeanProperty constructSetterlessProperty(DeserializationContext ctxt, BeanDescription beanDesc, BeanPropertyDefinition propDef)
/*     */     throws JsonMappingException
/*     */   {
/* 825 */     AnnotatedMethod getter = propDef.getGetter();
/* 826 */     JavaType type = resolveMemberAndTypeAnnotations(ctxt, getter, getter.getType());
/* 827 */     TypeDeserializer typeDeser = (TypeDeserializer)type.getTypeHandler();
/* 828 */     SettableBeanProperty prop = new com.fasterxml.jackson.databind.deser.impl.SetterlessProperty(propDef, type, typeDeser, beanDesc.getClassAnnotations(), getter);
/*     */     
/* 830 */     JsonDeserializer<?> deser = findDeserializerFromAnnotation(ctxt, getter);
/* 831 */     if (deser == null) {
/* 832 */       deser = (JsonDeserializer)type.getValueHandler();
/*     */     }
/* 834 */     if (deser != null) {
/* 835 */       deser = ctxt.handlePrimaryContextualization(deser, prop, type);
/* 836 */       prop = prop.withValueDeserializer(deser);
/*     */     }
/* 838 */     return prop;
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
/* 857 */     String typeStr = ClassUtil.canBeABeanType(type);
/* 858 */     if (typeStr != null) {
/* 859 */       throw new IllegalArgumentException("Can not deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
/*     */     }
/* 861 */     if (ClassUtil.isProxyType(type)) {
/* 862 */       throw new IllegalArgumentException("Can not deserialize Proxy class " + type.getName() + " as a Bean");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 867 */     typeStr = ClassUtil.isLocalType(type, true);
/* 868 */     if (typeStr != null) {
/* 869 */       throw new IllegalArgumentException("Can not deserialize Class " + type.getName() + " (of type " + typeStr + ") as a Bean");
/*     */     }
/* 871 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isIgnorableType(DeserializationConfig config, BeanDescription beanDesc, Class<?> type, Map<Class<?>, Boolean> ignoredTypes)
/*     */   {
/* 881 */     Boolean status = (Boolean)ignoredTypes.get(type);
/* 882 */     if (status != null) {
/* 883 */       return status.booleanValue();
/*     */     }
/*     */     
/* 886 */     ConfigOverride override = config.findConfigOverride(type);
/* 887 */     if (override != null) {
/* 888 */       status = override.getIsIgnoredType();
/*     */     }
/* 890 */     if (status == null) {
/* 891 */       BeanDescription desc = config.introspectClassAnnotations(type);
/* 892 */       status = config.getAnnotationIntrospector().isIgnorableType(desc.getClassInfo());
/*     */       
/* 894 */       if (status == null) {
/* 895 */         status = Boolean.FALSE;
/*     */       }
/*     */     }
/* 898 */     ignoredTypes.put(type, status);
/* 899 */     return status.booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void checkIllegalTypes(DeserializationContext ctxt, JavaType type, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 911 */     String full = type.getRawClass().getName();
/*     */     
/* 913 */     if (this._cfgIllegalClassNames.contains(full)) {
/* 914 */       ctxt.reportBadTypeDefinition(beanDesc, "Illegal type (%s) to deserialize: prevented for security reasons", new Object[] { full });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\BeanDeserializerFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */