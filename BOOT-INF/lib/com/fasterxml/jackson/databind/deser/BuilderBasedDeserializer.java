/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.BeanAsArrayBuilderDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.UnwrappedPropertyHandler;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class BuilderBasedDeserializer
/*     */   extends BeanDeserializerBase
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotatedMethod _buildMethod;
/*     */   
/*     */   public BuilderBasedDeserializer(BeanDeserializerBuilder builder, BeanDescription beanDesc, BeanPropertyMap properties, Map<String, SettableBeanProperty> backRefs, Set<String> ignorableProps, boolean ignoreAllUnknown, boolean hasViews)
/*     */   {
/*  45 */     super(builder, beanDesc, properties, backRefs, ignorableProps, ignoreAllUnknown, hasViews);
/*     */     
/*  47 */     this._buildMethod = builder.getBuildMethod();
/*     */     
/*  49 */     if (this._objectIdReader != null) {
/*  50 */       throw new IllegalArgumentException("Can not use Object Id with Builder-based deserialization (type " + beanDesc.getType() + ")");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BuilderBasedDeserializer(BuilderBasedDeserializer src)
/*     */   {
/*  61 */     this(src, src._ignoreAllUnknown);
/*     */   }
/*     */   
/*     */   protected BuilderBasedDeserializer(BuilderBasedDeserializer src, boolean ignoreAllUnknown)
/*     */   {
/*  66 */     super(src, ignoreAllUnknown);
/*  67 */     this._buildMethod = src._buildMethod;
/*     */   }
/*     */   
/*     */   protected BuilderBasedDeserializer(BuilderBasedDeserializer src, NameTransformer unwrapper) {
/*  71 */     super(src, unwrapper);
/*  72 */     this._buildMethod = src._buildMethod;
/*     */   }
/*     */   
/*     */   public BuilderBasedDeserializer(BuilderBasedDeserializer src, ObjectIdReader oir) {
/*  76 */     super(src, oir);
/*  77 */     this._buildMethod = src._buildMethod;
/*     */   }
/*     */   
/*     */   public BuilderBasedDeserializer(BuilderBasedDeserializer src, Set<String> ignorableProps) {
/*  81 */     super(src, ignorableProps);
/*  82 */     this._buildMethod = src._buildMethod;
/*     */   }
/*     */   
/*     */   public BuilderBasedDeserializer(BuilderBasedDeserializer src, BeanPropertyMap props) {
/*  86 */     super(src, props);
/*  87 */     this._buildMethod = src._buildMethod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<Object> unwrappingDeserializer(NameTransformer unwrapper)
/*     */   {
/*  97 */     return new BuilderBasedDeserializer(this, unwrapper);
/*     */   }
/*     */   
/*     */   public BeanDeserializerBase withObjectIdReader(ObjectIdReader oir)
/*     */   {
/* 102 */     return new BuilderBasedDeserializer(this, oir);
/*     */   }
/*     */   
/*     */   public BeanDeserializerBase withIgnorableProperties(Set<String> ignorableProps)
/*     */   {
/* 107 */     return new BuilderBasedDeserializer(this, ignorableProps);
/*     */   }
/*     */   
/*     */   public BeanDeserializerBase withBeanProperties(BeanPropertyMap props)
/*     */   {
/* 112 */     return new BuilderBasedDeserializer(this, props);
/*     */   }
/*     */   
/*     */   protected BeanDeserializerBase asArrayDeserializer()
/*     */   {
/* 117 */     SettableBeanProperty[] props = this._beanProperties.getPropertiesInInsertionOrder();
/* 118 */     return new BeanAsArrayBuilderDeserializer(this, props, this._buildMethod);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object finishBuild(DeserializationContext ctxt, Object builder)
/*     */     throws IOException
/*     */   {
/* 131 */     if (null == this._buildMethod) {
/* 132 */       return builder;
/*     */     }
/*     */     try {
/* 135 */       return this._buildMethod.getMember().invoke(builder, new Object[0]);
/*     */     } catch (Exception e) {
/* 137 */       return wrapInstantiationProblem(e, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Object deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 148 */     JsonToken t = p.getCurrentToken();
/*     */     
/*     */ 
/* 151 */     if (t == JsonToken.START_OBJECT) {
/* 152 */       t = p.nextToken();
/* 153 */       if (this._vanillaProcessing) {
/* 154 */         return finishBuild(ctxt, vanillaDeserialize(p, ctxt, t));
/*     */       }
/* 156 */       Object builder = deserializeFromObject(p, ctxt);
/* 157 */       return finishBuild(ctxt, builder);
/*     */     }
/*     */     
/* 160 */     if (t != null) {
/* 161 */       switch (t) {
/*     */       case VALUE_STRING: 
/* 163 */         return finishBuild(ctxt, deserializeFromString(p, ctxt));
/*     */       case VALUE_NUMBER_INT: 
/* 165 */         return finishBuild(ctxt, deserializeFromNumber(p, ctxt));
/*     */       case VALUE_NUMBER_FLOAT: 
/* 167 */         return finishBuild(ctxt, deserializeFromDouble(p, ctxt));
/*     */       case VALUE_EMBEDDED_OBJECT: 
/* 169 */         return p.getEmbeddedObject();
/*     */       case VALUE_TRUE: 
/*     */       case VALUE_FALSE: 
/* 172 */         return finishBuild(ctxt, deserializeFromBoolean(p, ctxt));
/*     */       
/*     */       case START_ARRAY: 
/* 175 */         return finishBuild(ctxt, deserializeFromArray(p, ctxt));
/*     */       case FIELD_NAME: 
/*     */       case END_OBJECT: 
/* 178 */         return finishBuild(ctxt, deserializeFromObject(p, ctxt));
/*     */       }
/*     */       
/*     */     }
/* 182 */     return ctxt.handleUnexpectedToken(handledType(), p);
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
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt, Object builder)
/*     */     throws IOException
/*     */   {
/* 198 */     return finishBuild(ctxt, _deserialize(p, ctxt, builder));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object _deserialize(JsonParser p, DeserializationContext ctxt, Object builder)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 211 */     if (this._injectables != null) {
/* 212 */       injectValues(ctxt, builder);
/*     */     }
/* 214 */     if (this._unwrappedPropertyHandler != null) {
/* 215 */       return deserializeWithUnwrapped(p, ctxt, builder);
/*     */     }
/* 217 */     if (this._externalTypeIdHandler != null) {
/* 218 */       return deserializeWithExternalTypeId(p, ctxt, builder);
/*     */     }
/* 220 */     if (this._needViewProcesing) {
/* 221 */       Class<?> view = ctxt.getActiveView();
/* 222 */       if (view != null) {
/* 223 */         return deserializeWithView(p, ctxt, builder, view);
/*     */       }
/*     */     }
/* 226 */     JsonToken t = p.getCurrentToken();
/*     */     
/* 228 */     if (t == JsonToken.START_OBJECT) {}
/* 229 */     for (t = p.nextToken(); 
/*     */         
/* 231 */         t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 232 */       String propName = p.getCurrentName();
/*     */       
/* 234 */       p.nextToken();
/* 235 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/*     */       
/* 237 */       if (prop != null) {
/*     */         try {
/* 239 */           builder = prop.deserializeSetAndReturn(p, ctxt, builder);
/*     */         } catch (Exception e) {
/* 241 */           wrapAndThrow(e, builder, propName, ctxt);
/*     */         }
/*     */         
/*     */       } else
/* 245 */         handleUnknownVanilla(p, ctxt, handledType(), propName);
/*     */     }
/* 247 */     return builder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Object vanillaDeserialize(JsonParser p, DeserializationContext ctxt, JsonToken t)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 258 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/* 259 */     for (; p.getCurrentToken() != JsonToken.END_OBJECT; p.nextToken()) {
/* 260 */       String propName = p.getCurrentName();
/*     */       
/* 262 */       p.nextToken();
/* 263 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 264 */       if (prop != null) {
/*     */         try {
/* 266 */           bean = prop.deserializeSetAndReturn(p, ctxt, bean);
/*     */         } catch (Exception e) {
/* 268 */           wrapAndThrow(e, bean, propName, ctxt);
/*     */         }
/*     */       } else {
/* 271 */         handleUnknownVanilla(p, ctxt, bean, propName);
/*     */       }
/*     */     }
/* 274 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserializeFromObject(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 285 */     if (this._nonStandardCreation) {
/* 286 */       if (this._unwrappedPropertyHandler != null) {
/* 287 */         return deserializeWithUnwrapped(p, ctxt);
/*     */       }
/* 289 */       if (this._externalTypeIdHandler != null) {
/* 290 */         return deserializeWithExternalTypeId(p, ctxt);
/*     */       }
/* 292 */       return deserializeFromObjectUsingNonDefault(p, ctxt);
/*     */     }
/* 294 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/* 295 */     if (this._injectables != null) {
/* 296 */       injectValues(ctxt, bean);
/*     */     }
/* 298 */     if (this._needViewProcesing) {
/* 299 */       Class<?> view = ctxt.getActiveView();
/* 300 */       if (view != null) {
/* 301 */         return deserializeWithView(p, ctxt, bean, view);
/*     */       }
/*     */     }
/* 304 */     for (; p.getCurrentToken() != JsonToken.END_OBJECT; p.nextToken()) {
/* 305 */       String propName = p.getCurrentName();
/*     */       
/* 307 */       p.nextToken();
/* 308 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 309 */       if (prop != null) {
/*     */         try {
/* 311 */           bean = prop.deserializeSetAndReturn(p, ctxt, bean);
/*     */         } catch (Exception e) {
/* 313 */           wrapAndThrow(e, bean, propName, ctxt);
/*     */         }
/*     */         
/*     */       } else
/* 317 */         handleUnknownVanilla(p, ctxt, bean, propName);
/*     */     }
/* 319 */     return bean;
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
/*     */   protected final Object _deserializeUsingPropertyBased(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 336 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/* 337 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/*     */     
/*     */ 
/* 340 */     TokenBuffer unknown = null;
/*     */     
/* 342 */     for (JsonToken t = p.getCurrentToken(); 
/* 343 */         t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 344 */       String propName = p.getCurrentName();
/* 345 */       p.nextToken();
/*     */       
/* 347 */       SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/* 348 */       if (creatorProp != null)
/*     */       {
/* 350 */         if (buffer.assignParameter(creatorProp, creatorProp.deserialize(p, ctxt))) {
/* 351 */           p.nextToken();
/*     */           Object bean;
/*     */           try {
/* 354 */             bean = creator.build(ctxt, buffer);
/*     */           } catch (Exception e) {
/* 356 */             wrapAndThrow(e, this._beanType.getRawClass(), propName, ctxt);
/* 357 */             continue;
/*     */           }
/*     */           
/* 360 */           if (bean.getClass() != this._beanType.getRawClass()) {
/* 361 */             return handlePolymorphic(p, ctxt, bean, unknown);
/*     */           }
/* 363 */           if (unknown != null) {
/* 364 */             bean = handleUnknownProperties(ctxt, bean, unknown);
/*     */           }
/*     */           
/* 367 */           return _deserialize(p, ctxt, bean);
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 372 */       else if (!buffer.readIdProperty(propName))
/*     */       {
/*     */ 
/*     */ 
/* 376 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/* 377 */         if (prop != null) {
/* 378 */           buffer.bufferProperty(prop, prop.deserialize(p, ctxt));
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/* 383 */         else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 384 */           handleIgnoredProperty(p, ctxt, handledType(), propName);
/*     */ 
/*     */ 
/*     */         }
/* 388 */         else if (this._anySetter != null) {
/* 389 */           buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(p, ctxt));
/*     */         }
/*     */         else
/*     */         {
/* 393 */           if (unknown == null) {
/* 394 */             unknown = new TokenBuffer(p, ctxt);
/*     */           }
/* 396 */           unknown.writeFieldName(propName);
/* 397 */           unknown.copyCurrentStructure(p);
/*     */         }
/*     */       }
/*     */     }
/*     */     Object bean;
/*     */     try {
/* 403 */       bean = creator.build(ctxt, buffer);
/*     */     } catch (Exception e) {
/* 405 */       bean = wrapInstantiationProblem(e, ctxt);
/*     */     }
/* 407 */     if (unknown != null)
/*     */     {
/* 409 */       if (bean.getClass() != this._beanType.getRawClass()) {
/* 410 */         return handlePolymorphic(null, ctxt, bean, unknown);
/*     */       }
/*     */       
/* 413 */       return handleUnknownProperties(ctxt, bean, unknown);
/*     */     }
/* 415 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object deserializeWithView(JsonParser p, DeserializationContext ctxt, Object bean, Class<?> activeView)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 428 */     for (JsonToken t = p.getCurrentToken(); 
/* 429 */         t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 430 */       String propName = p.getCurrentName();
/*     */       
/* 432 */       p.nextToken();
/* 433 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 434 */       if (prop != null) {
/* 435 */         if (!prop.visibleInView(activeView)) {
/* 436 */           p.skipChildren();
/*     */         } else {
/*     */           try
/*     */           {
/* 440 */             bean = prop.deserializeSetAndReturn(p, ctxt, bean);
/*     */           } catch (Exception e) {
/* 442 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */         }
/*     */       } else
/* 446 */         handleUnknownVanilla(p, ctxt, bean, propName);
/*     */     }
/* 448 */     return bean;
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
/*     */   protected Object deserializeWithUnwrapped(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 465 */     if (this._delegateDeserializer != null) {
/* 466 */       return this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
/*     */     }
/* 468 */     if (this._propertyBasedCreator != null) {
/* 469 */       return deserializeUsingPropertyBasedWithUnwrapped(p, ctxt);
/*     */     }
/* 471 */     TokenBuffer tokens = new TokenBuffer(p, ctxt);
/* 472 */     tokens.writeStartObject();
/* 473 */     Object bean = this._valueInstantiator.createUsingDefault(ctxt);
/*     */     
/* 475 */     if (this._injectables != null) {
/* 476 */       injectValues(ctxt, bean);
/*     */     }
/*     */     
/* 479 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/* 481 */     for (; 
/* 481 */         p.getCurrentToken() != JsonToken.END_OBJECT; p.nextToken()) {
/* 482 */       String propName = p.getCurrentName();
/* 483 */       p.nextToken();
/* 484 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 485 */       if (prop != null) {
/* 486 */         if ((activeView != null) && (!prop.visibleInView(activeView))) {
/* 487 */           p.skipChildren();
/*     */         } else {
/*     */           try
/*     */           {
/* 491 */             bean = prop.deserializeSetAndReturn(p, ctxt, bean);
/*     */           } catch (Exception e) {
/* 493 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 498 */       else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 499 */         handleIgnoredProperty(p, ctxt, bean, propName);
/*     */       }
/*     */       else
/*     */       {
/* 503 */         tokens.writeFieldName(propName);
/* 504 */         tokens.copyCurrentStructure(p);
/*     */         
/* 506 */         if (this._anySetter != null) {
/*     */           try {
/* 508 */             this._anySetter.deserializeAndSet(p, ctxt, bean, propName);
/*     */           } catch (Exception e) {
/* 510 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 515 */     tokens.writeEndObject();
/* 516 */     this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
/* 517 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Object deserializeWithUnwrapped(JsonParser p, DeserializationContext ctxt, Object bean)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 525 */     JsonToken t = p.getCurrentToken();
/* 526 */     if (t == JsonToken.START_OBJECT) {
/* 527 */       t = p.nextToken();
/*     */     }
/* 529 */     TokenBuffer tokens = new TokenBuffer(p, ctxt);
/* 530 */     tokens.writeStartObject();
/* 531 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/* 532 */     for (; t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 533 */       String propName = p.getCurrentName();
/* 534 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 535 */       p.nextToken();
/* 536 */       if (prop != null) {
/* 537 */         if ((activeView != null) && (!prop.visibleInView(activeView))) {
/* 538 */           p.skipChildren();
/*     */         } else {
/*     */           try
/*     */           {
/* 542 */             bean = prop.deserializeSetAndReturn(p, ctxt, bean);
/*     */           } catch (Exception e) {
/* 544 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */         }
/*     */       }
/* 548 */       else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 549 */         handleIgnoredProperty(p, ctxt, bean, propName);
/*     */       }
/*     */       else
/*     */       {
/* 553 */         tokens.writeFieldName(propName);
/* 554 */         tokens.copyCurrentStructure(p);
/*     */         
/* 556 */         if (this._anySetter != null)
/* 557 */           this._anySetter.deserializeAndSet(p, ctxt, bean, propName);
/*     */       }
/*     */     }
/* 560 */     tokens.writeEndObject();
/* 561 */     this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
/* 562 */     return bean;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Object deserializeUsingPropertyBasedWithUnwrapped(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 570 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/* 571 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, this._objectIdReader);
/*     */     
/* 573 */     TokenBuffer tokens = new TokenBuffer(p, ctxt);
/* 574 */     tokens.writeStartObject();
/*     */     
/* 576 */     for (JsonToken t = p.getCurrentToken(); 
/* 577 */         t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 578 */       String propName = p.getCurrentName();
/* 579 */       p.nextToken();
/*     */       
/* 581 */       SettableBeanProperty creatorProp = creator.findCreatorProperty(propName);
/* 582 */       if (creatorProp != null) {
/* 583 */         buffer.assignParameter(creatorProp, creatorProp.deserialize(p, ctxt));
/*     */ 
/*     */ 
/*     */       }
/* 587 */       else if (!buffer.readIdProperty(propName))
/*     */       {
/*     */ 
/*     */ 
/* 591 */         SettableBeanProperty prop = this._beanProperties.find(propName);
/* 592 */         if (prop != null) {
/* 593 */           buffer.bufferProperty(prop, prop.deserialize(p, ctxt));
/*     */ 
/*     */         }
/* 596 */         else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 597 */           handleIgnoredProperty(p, ctxt, handledType(), propName);
/*     */         }
/*     */         else {
/* 600 */           tokens.writeFieldName(propName);
/* 601 */           tokens.copyCurrentStructure(p);
/*     */           
/* 603 */           if (this._anySetter != null) {
/* 604 */             buffer.bufferAnyProperty(this._anySetter, propName, this._anySetter.deserialize(p, ctxt));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     Object bean;
/*     */     try
/*     */     {
/* 612 */       bean = creator.build(ctxt, buffer);
/*     */     } catch (Exception e) {
/* 614 */       return wrapInstantiationProblem(e, ctxt);
/*     */     }
/* 616 */     return this._unwrappedPropertyHandler.processUnwrapped(p, ctxt, bean, tokens);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object deserializeWithExternalTypeId(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 629 */     if (this._propertyBasedCreator != null) {
/* 630 */       return deserializeUsingPropertyBasedWithExternalTypeId(p, ctxt);
/*     */     }
/* 632 */     return deserializeWithExternalTypeId(p, ctxt, this._valueInstantiator.createUsingDefault(ctxt));
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object deserializeWithExternalTypeId(JsonParser p, DeserializationContext ctxt, Object bean)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 639 */     Class<?> activeView = this._needViewProcesing ? ctxt.getActiveView() : null;
/* 640 */     ExternalTypeHandler ext = this._externalTypeIdHandler.start();
/*     */     
/* 642 */     for (JsonToken t = p.getCurrentToken(); t == JsonToken.FIELD_NAME; t = p.nextToken()) {
/* 643 */       String propName = p.getCurrentName();
/* 644 */       t = p.nextToken();
/* 645 */       SettableBeanProperty prop = this._beanProperties.find(propName);
/* 646 */       if (prop != null)
/*     */       {
/* 648 */         if (t.isScalarValue()) {
/* 649 */           ext.handleTypePropertyValue(p, ctxt, propName, bean);
/*     */         }
/* 651 */         if ((activeView != null) && (!prop.visibleInView(activeView))) {
/* 652 */           p.skipChildren();
/*     */         } else {
/*     */           try
/*     */           {
/* 656 */             bean = prop.deserializeSetAndReturn(p, ctxt, bean);
/*     */           } catch (Exception e) {
/* 658 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 663 */       else if ((this._ignorableProps != null) && (this._ignorableProps.contains(propName))) {
/* 664 */         handleIgnoredProperty(p, ctxt, bean, propName);
/*     */ 
/*     */ 
/*     */       }
/* 668 */       else if (!ext.handlePropertyValue(p, ctxt, propName, bean))
/*     */       {
/*     */ 
/*     */ 
/* 672 */         if (this._anySetter != null) {
/*     */           try {
/* 674 */             this._anySetter.deserializeAndSet(p, ctxt, bean, propName);
/*     */           } catch (Exception e) {
/* 676 */             wrapAndThrow(e, bean, propName, ctxt);
/*     */           }
/*     */           
/*     */         } else {
/* 680 */           handleUnknownProperty(p, ctxt, bean, propName);
/*     */         }
/*     */       }
/*     */     }
/* 684 */     return ext.complete(p, ctxt, bean);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Object deserializeUsingPropertyBasedWithExternalTypeId(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 692 */     throw new IllegalStateException("Deserialization with Builder, External type id, @JsonCreator not yet implemented");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\BuilderBasedDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */