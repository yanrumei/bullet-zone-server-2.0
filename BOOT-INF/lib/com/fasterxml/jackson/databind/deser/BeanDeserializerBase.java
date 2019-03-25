/*      */ package com.fasterxml.jackson.databind.deser;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.BeanDescription;
/*      */ import com.fasterxml.jackson.databind.DeserializationContext;
/*      */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*      */ import com.fasterxml.jackson.databind.JsonMappingException;
/*      */ import com.fasterxml.jackson.databind.PropertyName;
/*      */ import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
/*      */ import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*      */ import com.fasterxml.jackson.databind.deser.impl.UnwrappedPropertyHandler;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ValueInjector;
/*      */ import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
/*      */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*      */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*      */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*      */ import java.io.IOException;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ public abstract class BeanDeserializerBase extends StdDeserializer<Object> implements ContextualDeserializer, ResolvableDeserializer, java.io.Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   33 */   protected static final PropertyName TEMP_PROPERTY_NAME = new PropertyName("#temporary-name");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final transient com.fasterxml.jackson.databind.util.Annotations _classAnnotations;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JavaType _beanType;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final com.fasterxml.jackson.annotation.JsonFormat.Shape _serializationShape;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final ValueInstantiator _valueInstantiator;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonDeserializer<Object> _delegateDeserializer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonDeserializer<Object> _arrayDelegateDeserializer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected PropertyBasedCreator _propertyBasedCreator;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _nonStandardCreation;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _vanillaProcessing;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final BeanPropertyMap _beanProperties;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final ValueInjector[] _injectables;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SettableAnyProperty _anySetter;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Set<String> _ignorableProps;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean _ignoreAllUnknown;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean _needViewProcesing;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Map<String, SettableBeanProperty> _backRefs;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected transient java.util.HashMap<com.fasterxml.jackson.databind.type.ClassKey, JsonDeserializer<Object>> _subDeserializers;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected UnwrappedPropertyHandler _unwrappedPropertyHandler;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler _externalTypeIdHandler;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final ObjectIdReader _objectIdReader;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected BeanDeserializerBase(BeanDeserializerBuilder builder, BeanDescription beanDesc, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, Set<String> ignorableProps, boolean ignoreAllUnknown, boolean hasViews)
/*      */   {
/*  210 */     super(beanDesc.getType());
/*      */     
/*  212 */     com.fasterxml.jackson.databind.introspect.AnnotatedClass ac = beanDesc.getClassInfo();
/*  213 */     this._classAnnotations = ac.getAnnotations();
/*  214 */     this._beanType = beanDesc.getType();
/*  215 */     this._valueInstantiator = builder.getValueInstantiator();
/*      */     
/*  217 */     this._beanProperties = properties;
/*  218 */     this._backRefs = backRefs;
/*  219 */     this._ignorableProps = ignorableProps;
/*  220 */     this._ignoreAllUnknown = ignoreAllUnknown;
/*      */     
/*  222 */     this._anySetter = builder.getAnySetter();
/*  223 */     List<ValueInjector> injectables = builder.getInjectables();
/*  224 */     this._injectables = ((injectables == null) || (injectables.isEmpty()) ? null : (ValueInjector[])injectables.toArray(new ValueInjector[injectables.size()]));
/*      */     
/*  226 */     this._objectIdReader = builder.getObjectIdReader();
/*  227 */     this._nonStandardCreation = ((this._unwrappedPropertyHandler != null) || (this._valueInstantiator.canCreateUsingDelegate()) || (this._valueInstantiator.canCreateUsingArrayDelegate()) || (this._valueInstantiator.canCreateFromObjectWith()) || (!this._valueInstantiator.canCreateUsingDefault()));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  235 */     JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/*  236 */     this._serializationShape = (format == null ? null : format.getShape());
/*      */     
/*  238 */     this._needViewProcesing = hasViews;
/*  239 */     this._vanillaProcessing = ((!this._nonStandardCreation) && (this._injectables == null) && (!this._needViewProcesing) && (this._objectIdReader == null));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected BeanDeserializerBase(BeanDeserializerBase src)
/*      */   {
/*  248 */     this(src, src._ignoreAllUnknown);
/*      */   }
/*      */   
/*      */   protected BeanDeserializerBase(BeanDeserializerBase src, boolean ignoreAllUnknown)
/*      */   {
/*  253 */     super(src._beanType);
/*      */     
/*  255 */     this._classAnnotations = src._classAnnotations;
/*  256 */     this._beanType = src._beanType;
/*      */     
/*  258 */     this._valueInstantiator = src._valueInstantiator;
/*  259 */     this._delegateDeserializer = src._delegateDeserializer;
/*  260 */     this._propertyBasedCreator = src._propertyBasedCreator;
/*      */     
/*  262 */     this._beanProperties = src._beanProperties;
/*  263 */     this._backRefs = src._backRefs;
/*  264 */     this._ignorableProps = src._ignorableProps;
/*  265 */     this._ignoreAllUnknown = ignoreAllUnknown;
/*  266 */     this._anySetter = src._anySetter;
/*  267 */     this._injectables = src._injectables;
/*  268 */     this._objectIdReader = src._objectIdReader;
/*      */     
/*  270 */     this._nonStandardCreation = src._nonStandardCreation;
/*  271 */     this._unwrappedPropertyHandler = src._unwrappedPropertyHandler;
/*  272 */     this._needViewProcesing = src._needViewProcesing;
/*  273 */     this._serializationShape = src._serializationShape;
/*      */     
/*  275 */     this._vanillaProcessing = src._vanillaProcessing;
/*      */   }
/*      */   
/*      */   protected BeanDeserializerBase(BeanDeserializerBase src, NameTransformer unwrapper)
/*      */   {
/*  280 */     super(src._beanType);
/*      */     
/*  282 */     this._classAnnotations = src._classAnnotations;
/*  283 */     this._beanType = src._beanType;
/*      */     
/*  285 */     this._valueInstantiator = src._valueInstantiator;
/*  286 */     this._delegateDeserializer = src._delegateDeserializer;
/*  287 */     this._propertyBasedCreator = src._propertyBasedCreator;
/*      */     
/*  289 */     this._backRefs = src._backRefs;
/*  290 */     this._ignorableProps = src._ignorableProps;
/*  291 */     this._ignoreAllUnknown = ((unwrapper != null) || (src._ignoreAllUnknown));
/*  292 */     this._anySetter = src._anySetter;
/*  293 */     this._injectables = src._injectables;
/*  294 */     this._objectIdReader = src._objectIdReader;
/*      */     
/*  296 */     this._nonStandardCreation = src._nonStandardCreation;
/*  297 */     UnwrappedPropertyHandler uph = src._unwrappedPropertyHandler;
/*      */     
/*  299 */     if (unwrapper != null)
/*      */     {
/*  301 */       if (uph != null) {
/*  302 */         uph = uph.renameAll(unwrapper);
/*      */       }
/*      */       
/*  305 */       this._beanProperties = src._beanProperties.renameAll(unwrapper);
/*      */     } else {
/*  307 */       this._beanProperties = src._beanProperties;
/*      */     }
/*  309 */     this._unwrappedPropertyHandler = uph;
/*  310 */     this._needViewProcesing = src._needViewProcesing;
/*  311 */     this._serializationShape = src._serializationShape;
/*      */     
/*      */ 
/*  314 */     this._vanillaProcessing = false;
/*      */   }
/*      */   
/*      */   public BeanDeserializerBase(BeanDeserializerBase src, ObjectIdReader oir)
/*      */   {
/*  319 */     super(src._beanType);
/*      */     
/*  321 */     this._classAnnotations = src._classAnnotations;
/*  322 */     this._beanType = src._beanType;
/*      */     
/*  324 */     this._valueInstantiator = src._valueInstantiator;
/*  325 */     this._delegateDeserializer = src._delegateDeserializer;
/*  326 */     this._propertyBasedCreator = src._propertyBasedCreator;
/*      */     
/*  328 */     this._backRefs = src._backRefs;
/*  329 */     this._ignorableProps = src._ignorableProps;
/*  330 */     this._ignoreAllUnknown = src._ignoreAllUnknown;
/*  331 */     this._anySetter = src._anySetter;
/*  332 */     this._injectables = src._injectables;
/*      */     
/*  334 */     this._nonStandardCreation = src._nonStandardCreation;
/*  335 */     this._unwrappedPropertyHandler = src._unwrappedPropertyHandler;
/*  336 */     this._needViewProcesing = src._needViewProcesing;
/*  337 */     this._serializationShape = src._serializationShape;
/*      */     
/*      */ 
/*  340 */     this._objectIdReader = oir;
/*      */     
/*  342 */     if (oir == null) {
/*  343 */       this._beanProperties = src._beanProperties;
/*  344 */       this._vanillaProcessing = src._vanillaProcessing;
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/*  350 */       com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty idProp = new com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty(oir, com.fasterxml.jackson.databind.PropertyMetadata.STD_REQUIRED);
/*  351 */       this._beanProperties = src._beanProperties.withProperty(idProp);
/*  352 */       this._vanillaProcessing = false;
/*      */     }
/*      */   }
/*      */   
/*      */   public BeanDeserializerBase(BeanDeserializerBase src, Set<String> ignorableProps)
/*      */   {
/*  358 */     super(src._beanType);
/*  359 */     this._classAnnotations = src._classAnnotations;
/*  360 */     this._beanType = src._beanType;
/*      */     
/*  362 */     this._valueInstantiator = src._valueInstantiator;
/*  363 */     this._delegateDeserializer = src._delegateDeserializer;
/*  364 */     this._propertyBasedCreator = src._propertyBasedCreator;
/*      */     
/*  366 */     this._backRefs = src._backRefs;
/*  367 */     this._ignorableProps = ignorableProps;
/*  368 */     this._ignoreAllUnknown = src._ignoreAllUnknown;
/*  369 */     this._anySetter = src._anySetter;
/*  370 */     this._injectables = src._injectables;
/*      */     
/*  372 */     this._nonStandardCreation = src._nonStandardCreation;
/*  373 */     this._unwrappedPropertyHandler = src._unwrappedPropertyHandler;
/*  374 */     this._needViewProcesing = src._needViewProcesing;
/*  375 */     this._serializationShape = src._serializationShape;
/*      */     
/*  377 */     this._vanillaProcessing = src._vanillaProcessing;
/*  378 */     this._objectIdReader = src._objectIdReader;
/*      */     
/*      */ 
/*      */ 
/*  382 */     this._beanProperties = src._beanProperties.withoutProperties(ignorableProps);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected BeanDeserializerBase(BeanDeserializerBase src, BeanPropertyMap beanProps)
/*      */   {
/*  390 */     super(src._beanType);
/*      */     
/*  392 */     this._classAnnotations = src._classAnnotations;
/*  393 */     this._beanType = src._beanType;
/*      */     
/*  395 */     this._valueInstantiator = src._valueInstantiator;
/*  396 */     this._delegateDeserializer = src._delegateDeserializer;
/*  397 */     this._propertyBasedCreator = src._propertyBasedCreator;
/*      */     
/*  399 */     this._beanProperties = beanProps;
/*  400 */     this._backRefs = src._backRefs;
/*  401 */     this._ignorableProps = src._ignorableProps;
/*  402 */     this._ignoreAllUnknown = src._ignoreAllUnknown;
/*  403 */     this._anySetter = src._anySetter;
/*  404 */     this._injectables = src._injectables;
/*  405 */     this._objectIdReader = src._objectIdReader;
/*      */     
/*  407 */     this._nonStandardCreation = src._nonStandardCreation;
/*  408 */     this._unwrappedPropertyHandler = src._unwrappedPropertyHandler;
/*  409 */     this._needViewProcesing = src._needViewProcesing;
/*  410 */     this._serializationShape = src._serializationShape;
/*      */     
/*  412 */     this._vanillaProcessing = src._vanillaProcessing;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public abstract JsonDeserializer<Object> unwrappingDeserializer(NameTransformer paramNameTransformer);
/*      */   
/*      */ 
/*      */ 
/*      */   public abstract BeanDeserializerBase withObjectIdReader(ObjectIdReader paramObjectIdReader);
/*      */   
/*      */ 
/*      */   public abstract BeanDeserializerBase withIgnorableProperties(Set<String> paramSet);
/*      */   
/*      */ 
/*      */   public BeanDeserializerBase withBeanProperties(BeanPropertyMap props)
/*      */   {
/*  429 */     throw new UnsupportedOperationException("Class " + getClass().getName() + " does not override `withBeanProperties()`, needs to");
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
/*      */   protected abstract BeanDeserializerBase asArrayDeserializer();
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
/*      */   public void resolve(DeserializationContext ctxt)
/*      */     throws JsonMappingException
/*      */   {
/*  457 */     com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder extTypes = null;
/*      */     
/*      */     SettableBeanProperty[] creatorProps;
/*      */     SettableBeanProperty[] creatorProps;
/*  461 */     if (this._valueInstantiator.canCreateFromObjectWith()) {
/*  462 */       creatorProps = this._valueInstantiator.getFromObjectArguments(ctxt.getConfig());
/*      */     } else {
/*  464 */       creatorProps = null;
/*      */     }
/*  466 */     UnwrappedPropertyHandler unwrapped = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  474 */     for (SettableBeanProperty prop : this._beanProperties) {
/*  475 */       if (!prop.hasValueDeserializer())
/*      */       {
/*  477 */         JsonDeserializer<?> deser = findConvertingDeserializer(ctxt, prop);
/*  478 */         if (deser == null) {
/*  479 */           deser = ctxt.findNonContextualValueDeserializer(prop.getType());
/*      */         }
/*  481 */         SettableBeanProperty newProp = prop.withValueDeserializer(deser);
/*  482 */         _replaceProperty(this._beanProperties, creatorProps, prop, newProp);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  487 */     for (SettableBeanProperty origProp : this._beanProperties) {
/*  488 */       SettableBeanProperty prop = origProp;
/*  489 */       JsonDeserializer<?> deser = prop.getValueDeserializer();
/*  490 */       deser = ctxt.handlePrimaryContextualization(deser, prop, prop.getType());
/*  491 */       prop = prop.withValueDeserializer(deser);
/*      */       
/*  493 */       prop = _resolveManagedReferenceProperty(ctxt, prop);
/*      */       
/*      */ 
/*  496 */       if (!(prop instanceof com.fasterxml.jackson.databind.deser.impl.ManagedReferenceProperty)) {
/*  497 */         prop = _resolvedObjectIdProperty(ctxt, prop);
/*      */       }
/*      */       
/*  500 */       SettableBeanProperty u = _resolveUnwrappedProperty(ctxt, prop);
/*  501 */       if (u != null) {
/*  502 */         prop = u;
/*  503 */         if (unwrapped == null) {
/*  504 */           unwrapped = new UnwrappedPropertyHandler();
/*      */         }
/*  506 */         unwrapped.addProperty(prop);
/*      */         
/*      */ 
/*      */ 
/*  510 */         this._beanProperties.remove(prop);
/*      */       }
/*      */       else
/*      */       {
/*  514 */         prop = _resolveInnerClassValuedProperty(ctxt, prop);
/*  515 */         if (prop != origProp) {
/*  516 */           _replaceProperty(this._beanProperties, creatorProps, origProp, prop);
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  521 */         if (prop.hasValueTypeDeserializer()) {
/*  522 */           TypeDeserializer typeDeser = prop.getValueTypeDeserializer();
/*  523 */           if (typeDeser.getTypeInclusion() == com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXTERNAL_PROPERTY) {
/*  524 */             if (extTypes == null) {
/*  525 */               extTypes = new com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder();
/*      */             }
/*  527 */             extTypes.addExternal(prop, typeDeser);
/*      */             
/*  529 */             this._beanProperties.remove(prop);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  535 */     if ((this._anySetter != null) && (!this._anySetter.hasValueDeserializer())) {
/*  536 */       this._anySetter = this._anySetter.withValueDeserializer(findDeserializer(ctxt, this._anySetter.getType(), this._anySetter.getProperty()));
/*      */     }
/*      */     
/*      */ 
/*  540 */     if (this._valueInstantiator.canCreateUsingDelegate()) {
/*  541 */       JavaType delegateType = this._valueInstantiator.getDelegateType(ctxt.getConfig());
/*  542 */       if (delegateType == null) {
/*  543 */         throw new IllegalArgumentException("Invalid delegate-creator definition for " + this._beanType + ": value instantiator (" + this._valueInstantiator.getClass().getName() + ") returned true for 'canCreateUsingDelegate()', but null for 'getDelegateType()'");
/*      */       }
/*      */       
/*      */ 
/*  547 */       this._delegateDeserializer = _findDelegateDeserializer(ctxt, delegateType, this._valueInstantiator.getDelegateCreator());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  552 */     if (this._valueInstantiator.canCreateUsingArrayDelegate()) {
/*  553 */       JavaType delegateType = this._valueInstantiator.getArrayDelegateType(ctxt.getConfig());
/*  554 */       if (delegateType == null) {
/*  555 */         throw new IllegalArgumentException("Invalid array-delegate-creator definition for " + this._beanType + ": value instantiator (" + this._valueInstantiator.getClass().getName() + ") returned true for 'canCreateUsingArrayDelegate()', but null for 'getArrayDelegateType()'");
/*      */       }
/*      */       
/*      */ 
/*  559 */       this._arrayDelegateDeserializer = _findDelegateDeserializer(ctxt, delegateType, this._valueInstantiator.getArrayDelegateCreator());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  564 */     if (creatorProps != null) {
/*  565 */       this._propertyBasedCreator = PropertyBasedCreator.construct(ctxt, this._valueInstantiator, creatorProps);
/*      */     }
/*      */     
/*  568 */     if (extTypes != null)
/*      */     {
/*      */ 
/*  571 */       this._externalTypeIdHandler = extTypes.build(this._beanProperties);
/*      */       
/*  573 */       this._nonStandardCreation = true;
/*      */     }
/*      */     
/*  576 */     this._unwrappedPropertyHandler = unwrapped;
/*  577 */     if (unwrapped != null) {
/*  578 */       this._nonStandardCreation = true;
/*      */     }
/*      */     
/*  581 */     this._vanillaProcessing = ((this._vanillaProcessing) && (!this._nonStandardCreation));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _replaceProperty(BeanPropertyMap props, SettableBeanProperty[] creatorProps, SettableBeanProperty origProp, SettableBeanProperty newProp)
/*      */   {
/*  590 */     props.replace(newProp);
/*      */     
/*  592 */     if (creatorProps != null)
/*      */     {
/*      */ 
/*  595 */       int i = 0; for (int len = creatorProps.length; i < len; i++) {
/*  596 */         if (creatorProps[i] == origProp) {
/*  597 */           creatorProps[i] = newProp;
/*  598 */           return;
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
/*      */ 
/*      */   private JsonDeserializer<Object> _findDelegateDeserializer(DeserializationContext ctxt, JavaType delegateType, com.fasterxml.jackson.databind.introspect.AnnotatedWithParams delegateCreator)
/*      */     throws JsonMappingException
/*      */   {
/*  615 */     com.fasterxml.jackson.databind.BeanProperty.Std property = new com.fasterxml.jackson.databind.BeanProperty.Std(TEMP_PROPERTY_NAME, delegateType, null, this._classAnnotations, delegateCreator, com.fasterxml.jackson.databind.PropertyMetadata.STD_OPTIONAL);
/*      */     
/*      */ 
/*      */ 
/*  619 */     TypeDeserializer td = (TypeDeserializer)delegateType.getTypeHandler();
/*  620 */     if (td == null) {
/*  621 */       td = ctxt.getConfig().findTypeDeserializer(delegateType);
/*      */     }
/*  623 */     JsonDeserializer<Object> dd = findDeserializer(ctxt, delegateType, property);
/*  624 */     if (td != null) {
/*  625 */       td = td.forProperty(property);
/*  626 */       return new com.fasterxml.jackson.databind.deser.impl.TypeWrappedDeserializer(td, dd);
/*      */     }
/*  628 */     return dd;
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
/*      */   protected JsonDeserializer<Object> findConvertingDeserializer(DeserializationContext ctxt, SettableBeanProperty prop)
/*      */     throws JsonMappingException
/*      */   {
/*  646 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/*  647 */     if (intr != null) {
/*  648 */       Object convDef = intr.findDeserializationConverter(prop.getMember());
/*  649 */       if (convDef != null) {
/*  650 */         com.fasterxml.jackson.databind.util.Converter<Object, Object> conv = ctxt.converterInstance(prop.getMember(), convDef);
/*  651 */         JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
/*      */         
/*      */ 
/*  654 */         JsonDeserializer<?> deser = ctxt.findNonContextualValueDeserializer(delegateType);
/*  655 */         return new com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer(conv, delegateType, deser);
/*      */       }
/*      */     }
/*  658 */     return null;
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
/*      */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, com.fasterxml.jackson.databind.BeanProperty property)
/*      */     throws JsonMappingException
/*      */   {
/*  672 */     ObjectIdReader oir = this._objectIdReader;
/*      */     
/*      */ 
/*  675 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/*  676 */     com.fasterxml.jackson.databind.introspect.AnnotatedMember accessor = (property == null) || (intr == null) ? null : property.getMember();
/*      */     
/*  678 */     if ((accessor != null) && (intr != null)) {
/*  679 */       ObjectIdInfo objectIdInfo = intr.findObjectIdInfo(accessor);
/*  680 */       if (objectIdInfo != null)
/*      */       {
/*  682 */         objectIdInfo = intr.findObjectReferenceInfo(accessor, objectIdInfo);
/*      */         
/*  684 */         Class<?> implClass = objectIdInfo.getGeneratorType();
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*  689 */         com.fasterxml.jackson.annotation.ObjectIdResolver resolver = ctxt.objectIdResolverInstance(accessor, objectIdInfo);
/*  690 */         com.fasterxml.jackson.annotation.ObjectIdGenerator<?> idGen; JavaType idType; SettableBeanProperty idProp; com.fasterxml.jackson.annotation.ObjectIdGenerator<?> idGen; if (implClass == com.fasterxml.jackson.annotation.ObjectIdGenerators.PropertyGenerator.class) {
/*  691 */           PropertyName propName = objectIdInfo.getPropertyName();
/*  692 */           SettableBeanProperty idProp = findProperty(propName);
/*  693 */           if (idProp == null) {
/*  694 */             throw new IllegalArgumentException("Invalid Object Id definition for " + handledType().getName() + ": can not find property with name '" + propName + "'");
/*      */           }
/*      */           
/*  697 */           JavaType idType = idProp.getType();
/*  698 */           idGen = new com.fasterxml.jackson.databind.deser.impl.PropertyBasedObjectIdGenerator(objectIdInfo.getScope());
/*      */         } else {
/*  700 */           JavaType type = ctxt.constructType(implClass);
/*  701 */           idType = ctxt.getTypeFactory().findTypeParameters(type, com.fasterxml.jackson.annotation.ObjectIdGenerator.class)[0];
/*  702 */           idProp = null;
/*  703 */           idGen = ctxt.objectIdGeneratorInstance(accessor, objectIdInfo);
/*      */         }
/*  705 */         JsonDeserializer<?> deser = ctxt.findRootValueDeserializer(idType);
/*  706 */         oir = ObjectIdReader.construct(idType, objectIdInfo.getPropertyName(), idGen, deser, idProp, resolver);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  711 */     BeanDeserializerBase contextual = this;
/*  712 */     if ((oir != null) && (oir != this._objectIdReader)) {
/*  713 */       contextual = contextual.withObjectIdReader(oir);
/*      */     }
/*      */     
/*  716 */     if (accessor != null) {
/*  717 */       com.fasterxml.jackson.annotation.JsonIgnoreProperties.Value ignorals = intr.findPropertyIgnorals(accessor);
/*  718 */       if (ignorals != null) {
/*  719 */         Set<String> ignored = ignorals.findIgnoredForDeserialization();
/*  720 */         if (!ignored.isEmpty()) {
/*  721 */           Set<String> prev = contextual._ignorableProps;
/*  722 */           if ((prev != null) && (!prev.isEmpty())) {
/*  723 */             ignored = new java.util.HashSet(ignored);
/*  724 */             ignored.addAll(prev);
/*      */           }
/*  726 */           contextual = contextual.withIgnorableProperties(ignored);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  732 */     JsonFormat.Value format = findFormatOverrides(ctxt, property, handledType());
/*  733 */     com.fasterxml.jackson.annotation.JsonFormat.Shape shape = null;
/*  734 */     if (format != null) {
/*  735 */       if (format.hasShape()) {
/*  736 */         shape = format.getShape();
/*      */       }
/*      */       
/*  739 */       Boolean B = format.getFeature(com.fasterxml.jackson.annotation.JsonFormat.Feature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
/*  740 */       if (B != null)
/*      */       {
/*  742 */         BeanPropertyMap propsOrig = this._beanProperties;
/*  743 */         BeanPropertyMap props = propsOrig.withCaseInsensitivity(B.booleanValue());
/*  744 */         if (props != propsOrig) {
/*  745 */           contextual = contextual.withBeanProperties(props);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  750 */     if (shape == null) {
/*  751 */       shape = this._serializationShape;
/*      */     }
/*  753 */     if (shape == com.fasterxml.jackson.annotation.JsonFormat.Shape.ARRAY) {
/*  754 */       contextual = contextual.asArrayDeserializer();
/*      */     }
/*  756 */     return contextual;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SettableBeanProperty _resolveManagedReferenceProperty(DeserializationContext ctxt, SettableBeanProperty prop)
/*      */   {
/*  766 */     String refName = prop.getManagedReferenceName();
/*  767 */     if (refName == null) {
/*  768 */       return prop;
/*      */     }
/*  770 */     JsonDeserializer<?> valueDeser = prop.getValueDeserializer();
/*  771 */     SettableBeanProperty backProp = valueDeser.findBackReference(refName);
/*  772 */     if (backProp == null) {
/*  773 */       throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': no back reference property found from type " + prop.getType());
/*      */     }
/*      */     
/*      */ 
/*  777 */     JavaType referredType = this._beanType;
/*  778 */     JavaType backRefType = backProp.getType();
/*  779 */     boolean isContainer = prop.getType().isContainerType();
/*  780 */     if (!backRefType.getRawClass().isAssignableFrom(referredType.getRawClass())) {
/*  781 */       throw new IllegalArgumentException("Can not handle managed/back reference '" + refName + "': back reference type (" + backRefType.getRawClass().getName() + ") not compatible with managed type (" + referredType.getRawClass().getName() + ")");
/*      */     }
/*      */     
/*      */ 
/*  785 */     return new com.fasterxml.jackson.databind.deser.impl.ManagedReferenceProperty(prop, refName, backProp, this._classAnnotations, isContainer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SettableBeanProperty _resolvedObjectIdProperty(DeserializationContext ctxt, SettableBeanProperty prop)
/*      */     throws JsonMappingException
/*      */   {
/*  796 */     ObjectIdInfo objectIdInfo = prop.getObjectIdInfo();
/*  797 */     JsonDeserializer<Object> valueDeser = prop.getValueDeserializer();
/*  798 */     ObjectIdReader objectIdReader = valueDeser == null ? null : valueDeser.getObjectIdReader();
/*  799 */     if ((objectIdInfo == null) && (objectIdReader == null)) {
/*  800 */       return prop;
/*      */     }
/*  802 */     return new com.fasterxml.jackson.databind.deser.impl.ObjectIdReferenceProperty(prop, objectIdInfo);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SettableBeanProperty _resolveUnwrappedProperty(DeserializationContext ctxt, SettableBeanProperty prop)
/*      */   {
/*  812 */     com.fasterxml.jackson.databind.introspect.AnnotatedMember am = prop.getMember();
/*  813 */     if (am != null) {
/*  814 */       NameTransformer unwrapper = ctxt.getAnnotationIntrospector().findUnwrappingNameTransformer(am);
/*  815 */       if (unwrapper != null) {
/*  816 */         JsonDeserializer<Object> orig = prop.getValueDeserializer();
/*  817 */         JsonDeserializer<Object> unwrapping = orig.unwrappingDeserializer(unwrapper);
/*  818 */         if ((unwrapping != orig) && (unwrapping != null))
/*      */         {
/*  820 */           return prop.withValueDeserializer(unwrapping);
/*      */         }
/*      */       }
/*      */     }
/*  824 */     return null;
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
/*      */   protected SettableBeanProperty _resolveInnerClassValuedProperty(DeserializationContext ctxt, SettableBeanProperty prop)
/*      */   {
/*  837 */     JsonDeserializer<Object> deser = prop.getValueDeserializer();
/*      */     
/*  839 */     if ((deser instanceof BeanDeserializerBase)) {
/*  840 */       BeanDeserializerBase bd = (BeanDeserializerBase)deser;
/*  841 */       ValueInstantiator vi = bd.getValueInstantiator();
/*  842 */       if (!vi.canCreateUsingDefault()) {
/*  843 */         Class<?> valueClass = prop.getType().getRawClass();
/*      */         
/*  845 */         Class<?> enclosing = com.fasterxml.jackson.databind.util.ClassUtil.getOuterClass(valueClass);
/*      */         
/*  847 */         if ((enclosing != null) && (enclosing == this._beanType.getRawClass())) {
/*  848 */           for (java.lang.reflect.Constructor<?> ctor : valueClass.getConstructors()) {
/*  849 */             Class<?>[] paramTypes = ctor.getParameterTypes();
/*  850 */             if ((paramTypes.length == 1) && 
/*  851 */               (enclosing.equals(paramTypes[0]))) {
/*  852 */               if (ctxt.canOverrideAccessModifiers()) {
/*  853 */                 com.fasterxml.jackson.databind.util.ClassUtil.checkAndFixAccess(ctor, ctxt.isEnabled(com.fasterxml.jackson.databind.MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*      */               }
/*  855 */               return new com.fasterxml.jackson.databind.deser.impl.InnerClassProperty(prop, ctor);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  862 */     return prop;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isCachable()
/*      */   {
/*  872 */     return true;
/*      */   }
/*      */   
/*      */   public Class<?> handledType() {
/*  876 */     return this._beanType.getRawClass();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectIdReader getObjectIdReader()
/*      */   {
/*  886 */     return this._objectIdReader;
/*      */   }
/*      */   
/*      */   public boolean hasProperty(String propertyName) {
/*  890 */     return this._beanProperties.find(propertyName) != null;
/*      */   }
/*      */   
/*      */   public boolean hasViews() {
/*  894 */     return this._needViewProcesing;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public int getPropertyCount()
/*      */   {
/*  901 */     return this._beanProperties.size();
/*      */   }
/*      */   
/*      */   public java.util.Collection<Object> getKnownPropertyNames()
/*      */   {
/*  906 */     java.util.ArrayList<Object> names = new java.util.ArrayList();
/*  907 */     for (SettableBeanProperty prop : this._beanProperties) {
/*  908 */       names.add(prop.getName());
/*      */     }
/*  910 */     return names;
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public final Class<?> getBeanClass()
/*      */   {
/*  917 */     return this._beanType.getRawClass();
/*      */   }
/*      */   
/*  920 */   public JavaType getValueType() { return this._beanType; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.util.Iterator<SettableBeanProperty> properties()
/*      */   {
/*  931 */     if (this._beanProperties == null) {
/*  932 */       throw new IllegalStateException("Can only call after BeanDeserializer has been resolved");
/*      */     }
/*  934 */     return this._beanProperties.iterator();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public java.util.Iterator<SettableBeanProperty> creatorProperties()
/*      */   {
/*  946 */     if (this._propertyBasedCreator == null) {
/*  947 */       return java.util.Collections.emptyList().iterator();
/*      */     }
/*  949 */     return this._propertyBasedCreator.properties().iterator();
/*      */   }
/*      */   
/*      */ 
/*      */   public SettableBeanProperty findProperty(PropertyName propertyName)
/*      */   {
/*  955 */     return findProperty(propertyName.getSimpleName());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SettableBeanProperty findProperty(String propertyName)
/*      */   {
/*  967 */     SettableBeanProperty prop = this._beanProperties == null ? null : this._beanProperties.find(propertyName);
/*      */     
/*  969 */     if ((prop == null) && (this._propertyBasedCreator != null)) {
/*  970 */       prop = this._propertyBasedCreator.findCreatorProperty(propertyName);
/*      */     }
/*  972 */     return prop;
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
/*      */   public SettableBeanProperty findProperty(int propertyIndex)
/*      */   {
/*  987 */     SettableBeanProperty prop = this._beanProperties == null ? null : this._beanProperties.find(propertyIndex);
/*      */     
/*  989 */     if ((prop == null) && (this._propertyBasedCreator != null)) {
/*  990 */       prop = this._propertyBasedCreator.findCreatorProperty(propertyIndex);
/*      */     }
/*  992 */     return prop;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SettableBeanProperty findBackReference(String logicalName)
/*      */   {
/* 1002 */     if (this._backRefs == null) {
/* 1003 */       return null;
/*      */     }
/* 1005 */     return (SettableBeanProperty)this._backRefs.get(logicalName);
/*      */   }
/*      */   
/*      */   public ValueInstantiator getValueInstantiator() {
/* 1009 */     return this._valueInstantiator;
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
/*      */   public void replaceProperty(SettableBeanProperty original, SettableBeanProperty replacement)
/*      */   {
/* 1033 */     this._beanProperties.replace(replacement);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract Object deserializeFromObject(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*      */     throws IOException
/*      */   {
/* 1055 */     if (this._objectIdReader != null)
/*      */     {
/* 1057 */       if (p.canReadObjectId()) {
/* 1058 */         Object id = p.getObjectId();
/* 1059 */         if (id != null) {
/* 1060 */           Object ob = typeDeserializer.deserializeTypedFromObject(p, ctxt);
/* 1061 */           return _handleTypedObjectId(p, ctxt, ob, id);
/*      */         }
/*      */       }
/*      */       
/* 1065 */       JsonToken t = p.getCurrentToken();
/* 1066 */       if (t != null)
/*      */       {
/* 1068 */         if (t.isScalarValue()) {
/* 1069 */           return deserializeFromObjectId(p, ctxt);
/*      */         }
/*      */         
/* 1072 */         if (t == JsonToken.START_OBJECT) {
/* 1073 */           t = p.nextToken();
/*      */         }
/* 1075 */         if ((t == JsonToken.FIELD_NAME) && (this._objectIdReader.maySerializeAsObject()) && (this._objectIdReader.isValidReferencePropertyName(p.getCurrentName(), p)))
/*      */         {
/* 1077 */           return deserializeFromObjectId(p, ctxt);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1082 */     return typeDeserializer.deserializeTypedFromObject(p, ctxt);
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
/*      */   protected Object _handleTypedObjectId(JsonParser p, DeserializationContext ctxt, Object pojo, Object rawId)
/*      */     throws IOException
/*      */   {
/* 1097 */     JsonDeserializer<Object> idDeser = this._objectIdReader.getDeserializer();
/*      */     
/*      */     Object id;
/*      */     Object id;
/* 1101 */     if (idDeser.handledType() == rawId.getClass())
/*      */     {
/* 1103 */       id = rawId;
/*      */     } else {
/* 1105 */       id = _convertObjectId(p, ctxt, rawId, idDeser);
/*      */     }
/*      */     
/* 1108 */     ReadableObjectId roid = ctxt.findObjectId(id, this._objectIdReader.generator, this._objectIdReader.resolver);
/* 1109 */     roid.bindItem(pojo);
/*      */     
/* 1111 */     SettableBeanProperty idProp = this._objectIdReader.idProperty;
/* 1112 */     if (idProp != null) {
/* 1113 */       return idProp.setAndReturn(pojo, id);
/*      */     }
/* 1115 */     return pojo;
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
/*      */   protected Object _convertObjectId(JsonParser p, DeserializationContext ctxt, Object rawId, JsonDeserializer<Object> idDeser)
/*      */     throws IOException
/*      */   {
/* 1131 */     TokenBuffer buf = new TokenBuffer(p, ctxt);
/* 1132 */     if ((rawId instanceof String)) {
/* 1133 */       buf.writeString((String)rawId);
/* 1134 */     } else if ((rawId instanceof Long)) {
/* 1135 */       buf.writeNumber(((Long)rawId).longValue());
/* 1136 */     } else if ((rawId instanceof Integer)) {
/* 1137 */       buf.writeNumber(((Integer)rawId).intValue());
/*      */ 
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */ 
/* 1144 */       buf.writeObject(rawId);
/*      */     }
/* 1146 */     JsonParser bufParser = buf.asParser();
/* 1147 */     bufParser.nextToken();
/* 1148 */     return idDeser.deserialize(bufParser, ctxt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object deserializeWithObjectId(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1161 */     return deserializeFromObject(p, ctxt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object deserializeFromObjectId(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1170 */     Object id = this._objectIdReader.readObjectReference(p, ctxt);
/* 1171 */     ReadableObjectId roid = ctxt.findObjectId(id, this._objectIdReader.generator, this._objectIdReader.resolver);
/*      */     
/* 1173 */     Object pojo = roid.resolve();
/* 1174 */     if (pojo == null) {
/* 1175 */       throw new UnresolvedForwardReference(p, "Could not resolve Object Id [" + id + "] (for " + this._beanType + ").", p.getCurrentLocation(), roid);
/*      */     }
/*      */     
/*      */ 
/* 1179 */     return pojo;
/*      */   }
/*      */   
/*      */   protected Object deserializeFromObjectUsingNonDefault(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1185 */     JsonDeserializer<Object> delegateDeser = _delegateDeserializer();
/* 1186 */     if (delegateDeser != null) {
/* 1187 */       return this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser.deserialize(p, ctxt));
/*      */     }
/*      */     
/* 1190 */     if (this._propertyBasedCreator != null) {
/* 1191 */       return _deserializeUsingPropertyBased(p, ctxt);
/*      */     }
/*      */     
/* 1194 */     if (this._beanType.isAbstract()) {
/* 1195 */       return ctxt.handleMissingInstantiator(handledType(), p, "abstract type (need to add/enable type information?)", new Object[0]);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1201 */     Class<?> raw = this._beanType.getRawClass();
/* 1202 */     if (com.fasterxml.jackson.databind.util.ClassUtil.isNonStaticInnerClass(raw)) {
/* 1203 */       return ctxt.handleMissingInstantiator(raw, p, "can only instantiate non-static inner class by using default, no-argument constructor", new Object[0]);
/*      */     }
/*      */     
/* 1206 */     return ctxt.handleMissingInstantiator(raw, p, "no suitable constructor found, can not deserialize from Object value (missing default constructor or creator, or perhaps need to add/enable type information?)", new Object[0]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected abstract Object _deserializeUsingPropertyBased(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext)
/*      */     throws IOException, com.fasterxml.jackson.core.JsonProcessingException;
/*      */   
/*      */ 
/*      */ 
/*      */   public Object deserializeFromNumber(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1219 */     if (this._objectIdReader != null) {
/* 1220 */       return deserializeFromObjectId(p, ctxt);
/*      */     }
/* 1222 */     JsonDeserializer<Object> delegateDeser = _delegateDeserializer();
/* 1223 */     switch (p.getNumberType()) {
/*      */     case INT: 
/* 1225 */       if ((delegateDeser != null) && 
/* 1226 */         (!this._valueInstantiator.canCreateFromInt())) {
/* 1227 */         Object bean = this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser.deserialize(p, ctxt));
/*      */         
/* 1229 */         if (this._injectables != null) {
/* 1230 */           injectValues(ctxt, bean);
/*      */         }
/* 1232 */         return bean;
/*      */       }
/*      */       
/* 1235 */       return this._valueInstantiator.createFromInt(ctxt, p.getIntValue());
/*      */     case LONG: 
/* 1237 */       if ((delegateDeser != null) && 
/* 1238 */         (!this._valueInstantiator.canCreateFromInt())) {
/* 1239 */         Object bean = this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser.deserialize(p, ctxt));
/*      */         
/* 1241 */         if (this._injectables != null) {
/* 1242 */           injectValues(ctxt, bean);
/*      */         }
/* 1244 */         return bean;
/*      */       }
/*      */       
/* 1247 */       return this._valueInstantiator.createFromLong(ctxt, p.getLongValue());
/*      */     }
/*      */     
/* 1250 */     if (delegateDeser != null) {
/* 1251 */       Object bean = this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser.deserialize(p, ctxt));
/*      */       
/* 1253 */       if (this._injectables != null) {
/* 1254 */         injectValues(ctxt, bean);
/*      */       }
/* 1256 */       return bean;
/*      */     }
/* 1258 */     return ctxt.handleMissingInstantiator(handledType(), p, "no suitable creator method found to deserialize from Number value (%s)", new Object[] { p.getNumberValue() });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Object deserializeFromString(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1266 */     if (this._objectIdReader != null) {
/* 1267 */       return deserializeFromObjectId(p, ctxt);
/*      */     }
/*      */     
/*      */ 
/* 1271 */     JsonDeserializer<Object> delegateDeser = _delegateDeserializer();
/* 1272 */     if ((delegateDeser != null) && 
/* 1273 */       (!this._valueInstantiator.canCreateFromString())) {
/* 1274 */       Object bean = this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser.deserialize(p, ctxt));
/*      */       
/* 1276 */       if (this._injectables != null) {
/* 1277 */         injectValues(ctxt, bean);
/*      */       }
/* 1279 */       return bean;
/*      */     }
/*      */     
/* 1282 */     return this._valueInstantiator.createFromString(ctxt, p.getText());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object deserializeFromDouble(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1291 */     com.fasterxml.jackson.core.JsonParser.NumberType t = p.getNumberType();
/*      */     
/* 1293 */     if ((t == com.fasterxml.jackson.core.JsonParser.NumberType.DOUBLE) || (t == com.fasterxml.jackson.core.JsonParser.NumberType.FLOAT)) {
/* 1294 */       JsonDeserializer<Object> delegateDeser = _delegateDeserializer();
/* 1295 */       if ((delegateDeser != null) && 
/* 1296 */         (!this._valueInstantiator.canCreateFromDouble())) {
/* 1297 */         Object bean = this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser.deserialize(p, ctxt));
/*      */         
/* 1299 */         if (this._injectables != null) {
/* 1300 */           injectValues(ctxt, bean);
/*      */         }
/* 1302 */         return bean;
/*      */       }
/*      */       
/* 1305 */       return this._valueInstantiator.createFromDouble(ctxt, p.getDoubleValue());
/*      */     }
/*      */     
/* 1308 */     JsonDeserializer<Object> delegateDeser = _delegateDeserializer();
/* 1309 */     if (delegateDeser != null) {
/* 1310 */       return this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser.deserialize(p, ctxt));
/*      */     }
/*      */     
/* 1313 */     return ctxt.handleMissingInstantiator(handledType(), p, "no suitable creator method found to deserialize from Number value (%s)", new Object[] { p.getNumberValue() });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object deserializeFromBoolean(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1323 */     JsonDeserializer<Object> delegateDeser = _delegateDeserializer();
/* 1324 */     if ((delegateDeser != null) && 
/* 1325 */       (!this._valueInstantiator.canCreateFromBoolean())) {
/* 1326 */       Object bean = this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser.deserialize(p, ctxt));
/*      */       
/* 1328 */       if (this._injectables != null) {
/* 1329 */         injectValues(ctxt, bean);
/*      */       }
/* 1331 */       return bean;
/*      */     }
/*      */     
/* 1334 */     boolean value = p.getCurrentToken() == JsonToken.VALUE_TRUE;
/* 1335 */     return this._valueInstantiator.createFromBoolean(ctxt, value);
/*      */   }
/*      */   
/*      */   public Object deserializeFromArray(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1341 */     JsonDeserializer<Object> delegateDeser = this._arrayDelegateDeserializer;
/*      */     
/* 1343 */     if ((delegateDeser != null) || ((delegateDeser = this._delegateDeserializer) != null)) {
/* 1344 */       Object bean = this._valueInstantiator.createUsingArrayDelegate(ctxt, delegateDeser.deserialize(p, ctxt));
/*      */       
/* 1346 */       if (this._injectables != null) {
/* 1347 */         injectValues(ctxt, bean);
/*      */       }
/* 1349 */       return bean;
/*      */     }
/* 1351 */     if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/* 1352 */       JsonToken t = p.nextToken();
/* 1353 */       if ((t == JsonToken.END_ARRAY) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT))) {
/* 1354 */         return null;
/*      */       }
/* 1356 */       Object value = deserialize(p, ctxt);
/* 1357 */       if (p.nextToken() != JsonToken.END_ARRAY) {
/* 1358 */         handleMissingEndArrayForSingle(p, ctxt);
/*      */       }
/* 1360 */       return value;
/*      */     }
/* 1362 */     if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT)) {
/* 1363 */       JsonToken t = p.nextToken();
/* 1364 */       if (t == JsonToken.END_ARRAY) {
/* 1365 */         return null;
/*      */       }
/* 1367 */       return ctxt.handleUnexpectedToken(handledType(), JsonToken.START_ARRAY, p, null, new Object[0]);
/*      */     }
/*      */     
/* 1370 */     return ctxt.handleUnexpectedToken(handledType(), p);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Object deserializeFromEmbedded(JsonParser p, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1378 */     if (this._objectIdReader != null) {
/* 1379 */       return deserializeFromObjectId(p, ctxt);
/*      */     }
/*      */     
/* 1382 */     JsonDeserializer<Object> delegateDeser = _delegateDeserializer();
/* 1383 */     if ((delegateDeser != null) && 
/* 1384 */       (!this._valueInstantiator.canCreateFromString())) {
/* 1385 */       Object bean = this._valueInstantiator.createUsingDelegate(ctxt, delegateDeser.deserialize(p, ctxt));
/*      */       
/* 1387 */       if (this._injectables != null) {
/* 1388 */         injectValues(ctxt, bean);
/*      */       }
/* 1390 */       return bean;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1396 */     return p.getEmbeddedObject();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private final JsonDeserializer<Object> _delegateDeserializer()
/*      */   {
/* 1403 */     JsonDeserializer<Object> deser = this._delegateDeserializer;
/* 1404 */     if (deser == null) {
/* 1405 */       deser = this._arrayDelegateDeserializer;
/*      */     }
/* 1407 */     return deser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void injectValues(DeserializationContext ctxt, Object bean)
/*      */     throws IOException
/*      */   {
/* 1419 */     for (ValueInjector injector : this._injectables) {
/* 1420 */       injector.inject(ctxt, bean);
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
/*      */   protected Object handleUnknownProperties(DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens)
/*      */     throws IOException
/*      */   {
/* 1435 */     unknownTokens.writeEndObject();
/*      */     
/*      */ 
/* 1438 */     JsonParser bufferParser = unknownTokens.asParser();
/* 1439 */     while (bufferParser.nextToken() != JsonToken.END_OBJECT) {
/* 1440 */       String propName = bufferParser.getCurrentName();
/*      */       
/* 1442 */       bufferParser.nextToken();
/* 1443 */       handleUnknownProperty(bufferParser, ctxt, bean, propName);
/*      */     }
/* 1445 */     return bean;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void handleUnknownVanilla(JsonParser p, DeserializationContext ctxt, Object bean, String propName)
/*      */     throws IOException
/*      */   {
/* 1456 */     if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 1457 */       handleIgnoredProperty(p, ctxt, bean, propName);
/* 1458 */     } else if (this._anySetter != null) {
/*      */       try
/*      */       {
/* 1461 */         this._anySetter.deserializeAndSet(p, ctxt, bean, propName);
/*      */       } catch (Exception e) {
/* 1463 */         wrapAndThrow(e, bean, propName, ctxt);
/*      */       }
/*      */       
/*      */     } else {
/* 1467 */       handleUnknownProperty(p, ctxt, bean, propName);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void handleUnknownProperty(JsonParser p, DeserializationContext ctxt, Object beanOrClass, String propName)
/*      */     throws IOException
/*      */   {
/* 1480 */     if (this._ignoreAllUnknown) {
/* 1481 */       p.skipChildren();
/* 1482 */       return;
/*      */     }
/* 1484 */     if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 1485 */       handleIgnoredProperty(p, ctxt, beanOrClass, propName);
/*      */     }
/*      */     
/*      */ 
/* 1489 */     super.handleUnknownProperty(p, ctxt, beanOrClass, propName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void handleIgnoredProperty(JsonParser p, DeserializationContext ctxt, Object beanOrClass, String propName)
/*      */     throws IOException
/*      */   {
/* 1502 */     if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)) {
/* 1503 */       throw com.fasterxml.jackson.databind.exc.IgnoredPropertyException.from(p, beanOrClass, propName, getKnownPropertyNames());
/*      */     }
/* 1505 */     p.skipChildren();
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
/*      */   protected Object handlePolymorphic(JsonParser p, DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens)
/*      */     throws IOException
/*      */   {
/* 1525 */     JsonDeserializer<Object> subDeser = _findSubclassDeserializer(ctxt, bean, unknownTokens);
/* 1526 */     if (subDeser != null) {
/* 1527 */       if (unknownTokens != null)
/*      */       {
/* 1529 */         unknownTokens.writeEndObject();
/* 1530 */         JsonParser p2 = unknownTokens.asParser();
/* 1531 */         p2.nextToken();
/* 1532 */         bean = subDeser.deserialize(p2, ctxt, bean);
/*      */       }
/*      */       
/* 1535 */       if (p != null) {
/* 1536 */         bean = subDeser.deserialize(p, ctxt, bean);
/*      */       }
/* 1538 */       return bean;
/*      */     }
/*      */     
/* 1541 */     if (unknownTokens != null) {
/* 1542 */       bean = handleUnknownProperties(ctxt, bean, unknownTokens);
/*      */     }
/*      */     
/* 1545 */     if (p != null) {
/* 1546 */       bean = deserialize(p, ctxt, bean);
/*      */     }
/* 1548 */     return bean;
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
/*      */   protected JsonDeserializer<Object> _findSubclassDeserializer(DeserializationContext ctxt, Object bean, TokenBuffer unknownTokens)
/*      */     throws IOException
/*      */   {
/* 1562 */     synchronized (this) {
/* 1563 */       subDeser = this._subDeserializers == null ? null : (JsonDeserializer)this._subDeserializers.get(new com.fasterxml.jackson.databind.type.ClassKey(bean.getClass()));
/*      */     }
/* 1565 */     if (subDeser != null) {
/* 1566 */       return subDeser;
/*      */     }
/*      */     
/* 1569 */     JavaType type = ctxt.constructType(bean.getClass());
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1576 */     JsonDeserializer<Object> subDeser = ctxt.findRootValueDeserializer(type);
/*      */     
/* 1578 */     if (subDeser != null) {
/* 1579 */       synchronized (this) {
/* 1580 */         if (this._subDeserializers == null) {
/* 1581 */           this._subDeserializers = new java.util.HashMap();
/*      */         }
/* 1583 */         this._subDeserializers.put(new com.fasterxml.jackson.databind.type.ClassKey(bean.getClass()), subDeser);
/*      */       }
/*      */     }
/* 1586 */     return subDeser;
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
/*      */   public void wrapAndThrow(Throwable t, Object bean, String fieldName, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1611 */     throw JsonMappingException.wrapWithPath(throwOrReturnThrowable(t, ctxt), bean, fieldName);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public void wrapAndThrow(Throwable t, Object bean, int index, DeserializationContext ctxt) throws IOException
/*      */   {
/* 1617 */     throw JsonMappingException.wrapWithPath(throwOrReturnThrowable(t, ctxt), bean, index);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private Throwable throwOrReturnThrowable(Throwable t, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1627 */     while (((t instanceof java.lang.reflect.InvocationTargetException)) && (t.getCause() != null)) {
/* 1628 */       t = t.getCause();
/*      */     }
/*      */     
/* 1631 */     if ((t instanceof Error)) {
/* 1632 */       throw ((Error)t);
/*      */     }
/* 1634 */     boolean wrap = (ctxt == null) || (ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS));
/*      */     
/* 1636 */     if ((t instanceof IOException)) {
/* 1637 */       if ((!wrap) || (!(t instanceof com.fasterxml.jackson.core.JsonProcessingException))) {
/* 1638 */         throw ((IOException)t);
/*      */       }
/* 1640 */     } else if ((!wrap) && 
/* 1641 */       ((t instanceof RuntimeException))) {
/* 1642 */       throw ((RuntimeException)t);
/*      */     }
/*      */     
/* 1645 */     return t;
/*      */   }
/*      */   
/*      */   protected Object wrapInstantiationProblem(Throwable t, DeserializationContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1651 */     while (((t instanceof java.lang.reflect.InvocationTargetException)) && (t.getCause() != null)) {
/* 1652 */       t = t.getCause();
/*      */     }
/*      */     
/* 1655 */     if ((t instanceof Error)) {
/* 1656 */       throw ((Error)t);
/*      */     }
/* 1658 */     boolean wrap = (ctxt == null) || (ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS));
/* 1659 */     if ((t instanceof IOException))
/*      */     {
/* 1661 */       throw ((IOException)t); }
/* 1662 */     if ((!wrap) && 
/* 1663 */       ((t instanceof RuntimeException))) {
/* 1664 */       throw ((RuntimeException)t);
/*      */     }
/*      */     
/* 1667 */     return ctxt.handleInstantiationProblem(this._beanType.getRawClass(), null, t);
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\BeanDeserializerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */