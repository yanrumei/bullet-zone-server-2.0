/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Shape;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer.None;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.std.StdDelegatingDeserializer;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.type.ArrayType;
/*     */ import com.fasterxml.jackson.databind.type.CollectionLikeType;
/*     */ import com.fasterxml.jackson.databind.type.CollectionType;
/*     */ import com.fasterxml.jackson.databind.type.MapLikeType;
/*     */ import com.fasterxml.jackson.databind.type.MapType;
/*     */ import com.fasterxml.jackson.databind.type.ReferenceType;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public final class DeserializerCache
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  42 */   protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _cachedDeserializers = new ConcurrentHashMap(64, 0.75F, 4);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  50 */   protected final HashMap<JavaType, JsonDeserializer<Object>> _incompleteDeserializers = new HashMap(8);
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
/*     */   Object writeReplace()
/*     */   {
/*  69 */     this._incompleteDeserializers.clear();
/*     */     
/*  71 */     return this;
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
/*     */   public int cachedDeserializersCount()
/*     */   {
/*  93 */     return this._cachedDeserializers.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void flushCachedDeserializers()
/*     */   {
/* 104 */     this._cachedDeserializers.clear();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<Object> findValueDeserializer(DeserializationContext ctxt, DeserializerFactory factory, JavaType propertyType)
/*     */     throws JsonMappingException
/*     */   {
/* 139 */     JsonDeserializer<Object> deser = _findCachedDeserializer(propertyType);
/* 140 */     if (deser == null)
/*     */     {
/* 142 */       deser = _createAndCacheValueDeserializer(ctxt, factory, propertyType);
/* 143 */       if (deser == null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 148 */         deser = _handleUnknownValueDeserializer(ctxt, propertyType);
/*     */       }
/*     */     }
/* 151 */     return deser;
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
/*     */   public KeyDeserializer findKeyDeserializer(DeserializationContext ctxt, DeserializerFactory factory, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 166 */     KeyDeserializer kd = factory.createKeyDeserializer(ctxt, type);
/* 167 */     if (kd == null) {
/* 168 */       return _handleUnknownKeyDeserializer(ctxt, type);
/*     */     }
/*     */     
/* 171 */     if ((kd instanceof ResolvableDeserializer)) {
/* 172 */       ((ResolvableDeserializer)kd).resolve(ctxt);
/*     */     }
/* 174 */     return kd;
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
/*     */   public boolean hasValueDeserializerFor(DeserializationContext ctxt, DeserializerFactory factory, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 189 */     JsonDeserializer<Object> deser = _findCachedDeserializer(type);
/* 190 */     if (deser == null) {
/* 191 */       deser = _createAndCacheValueDeserializer(ctxt, factory, type);
/*     */     }
/* 193 */     return deser != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<Object> _findCachedDeserializer(JavaType type)
/*     */   {
/* 204 */     if (type == null) {
/* 205 */       throw new IllegalArgumentException("Null JavaType passed");
/*     */     }
/* 207 */     if (_hasCustomValueHandler(type)) {
/* 208 */       return null;
/*     */     }
/* 210 */     return (JsonDeserializer)this._cachedDeserializers.get(type);
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
/*     */   protected JsonDeserializer<Object> _createAndCacheValueDeserializer(DeserializationContext ctxt, DeserializerFactory factory, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 228 */     synchronized (this._incompleteDeserializers)
/*     */     {
/* 230 */       JsonDeserializer<Object> deser = _findCachedDeserializer(type);
/* 231 */       if (deser != null) {
/* 232 */         return deser;
/*     */       }
/* 234 */       int count = this._incompleteDeserializers.size();
/*     */       
/* 236 */       if (count > 0) {
/* 237 */         deser = (JsonDeserializer)this._incompleteDeserializers.get(type);
/* 238 */         if (deser != null) {
/* 239 */           return deser;
/*     */         }
/*     */       }
/*     */       try
/*     */       {
/* 244 */         JsonDeserializer localJsonDeserializer = _createAndCache2(ctxt, factory, type);
/*     */         
/*     */ 
/* 247 */         if ((count == 0) && (this._incompleteDeserializers.size() > 0))
/* 248 */           this._incompleteDeserializers.clear(); return localJsonDeserializer;
/*     */       }
/*     */       finally
/*     */       {
/* 247 */         if ((count == 0) && (this._incompleteDeserializers.size() > 0)) {
/* 248 */           this._incompleteDeserializers.clear();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<Object> _createAndCache2(DeserializationContext ctxt, DeserializerFactory factory, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/*     */     JsonDeserializer<Object> deser;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 264 */       deser = _createDeserializer(ctxt, factory, type);
/*     */     }
/*     */     catch (IllegalArgumentException iae)
/*     */     {
/* 268 */       throw JsonMappingException.from(ctxt, iae.getMessage(), iae);
/*     */     }
/* 270 */     if (deser == null) {
/* 271 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 277 */     boolean addToCache = (!_hasCustomValueHandler(type)) && (deser.isCachable());
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
/* 291 */     if ((deser instanceof ResolvableDeserializer)) {
/* 292 */       this._incompleteDeserializers.put(type, deser);
/* 293 */       ((ResolvableDeserializer)deser).resolve(ctxt);
/* 294 */       this._incompleteDeserializers.remove(type);
/*     */     }
/* 296 */     if (addToCache) {
/* 297 */       this._cachedDeserializers.put(type, deser);
/*     */     }
/* 299 */     return deser;
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
/*     */   protected JsonDeserializer<Object> _createDeserializer(DeserializationContext ctxt, DeserializerFactory factory, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 318 */     DeserializationConfig config = ctxt.getConfig();
/*     */     
/*     */ 
/* 321 */     if ((type.isAbstract()) || (type.isMapLikeType()) || (type.isCollectionLikeType())) {
/* 322 */       type = factory.mapAbstractType(config, type);
/*     */     }
/* 324 */     BeanDescription beanDesc = config.introspect(type);
/*     */     
/* 326 */     JsonDeserializer<Object> deser = findDeserializerFromAnnotation(ctxt, beanDesc.getClassInfo());
/*     */     
/* 328 */     if (deser != null) {
/* 329 */       return deser;
/*     */     }
/*     */     
/*     */ 
/* 333 */     JavaType newType = modifyTypeByAnnotation(ctxt, beanDesc.getClassInfo(), type);
/* 334 */     if (newType != type) {
/* 335 */       type = newType;
/* 336 */       beanDesc = config.introspect(newType);
/*     */     }
/*     */     
/*     */ 
/* 340 */     Class<?> builder = beanDesc.findPOJOBuilder();
/* 341 */     if (builder != null) {
/* 342 */       return factory.createBuilderBasedDeserializer(ctxt, type, beanDesc, builder);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 347 */     Converter<Object, Object> conv = beanDesc.findDeserializationConverter();
/* 348 */     if (conv == null) {
/* 349 */       return _createDeserializer2(ctxt, factory, type, beanDesc);
/*     */     }
/*     */     
/* 352 */     JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
/*     */     
/* 354 */     if (!delegateType.hasRawClass(type.getRawClass())) {
/* 355 */       beanDesc = config.introspect(delegateType);
/*     */     }
/* 357 */     return new StdDelegatingDeserializer(conv, delegateType, _createDeserializer2(ctxt, factory, delegateType, beanDesc));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<?> _createDeserializer2(DeserializationContext ctxt, DeserializerFactory factory, JavaType type, BeanDescription beanDesc)
/*     */     throws JsonMappingException
/*     */   {
/* 365 */     DeserializationConfig config = ctxt.getConfig();
/*     */     
/* 367 */     if (type.isEnumType()) {
/* 368 */       return factory.createEnumDeserializer(ctxt, type, beanDesc);
/*     */     }
/* 370 */     if (type.isContainerType()) {
/* 371 */       if (type.isArrayType()) {
/* 372 */         return factory.createArrayDeserializer(ctxt, (ArrayType)type, beanDesc);
/*     */       }
/* 374 */       if (type.isMapLikeType()) {
/* 375 */         MapLikeType mlt = (MapLikeType)type;
/* 376 */         if (mlt.isTrueMapType()) {
/* 377 */           return factory.createMapDeserializer(ctxt, (MapType)mlt, beanDesc);
/*     */         }
/* 379 */         return factory.createMapLikeDeserializer(ctxt, mlt, beanDesc);
/*     */       }
/* 381 */       if (type.isCollectionLikeType())
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 387 */         JsonFormat.Value format = beanDesc.findExpectedFormat(null);
/* 388 */         if ((format == null) || (format.getShape() != JsonFormat.Shape.OBJECT)) {
/* 389 */           CollectionLikeType clt = (CollectionLikeType)type;
/* 390 */           if (clt.isTrueCollectionType()) {
/* 391 */             return factory.createCollectionDeserializer(ctxt, (CollectionType)clt, beanDesc);
/*     */           }
/* 393 */           return factory.createCollectionLikeDeserializer(ctxt, clt, beanDesc);
/*     */         }
/*     */       }
/*     */     }
/* 397 */     if (type.isReferenceType()) {
/* 398 */       return factory.createReferenceDeserializer(ctxt, (ReferenceType)type, beanDesc);
/*     */     }
/* 400 */     if (JsonNode.class.isAssignableFrom(type.getRawClass())) {
/* 401 */       return factory.createTreeDeserializer(config, type, beanDesc);
/*     */     }
/* 403 */     return factory.createBeanDeserializer(ctxt, type, beanDesc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<Object> findDeserializerFromAnnotation(DeserializationContext ctxt, Annotated ann)
/*     */     throws JsonMappingException
/*     */   {
/* 415 */     Object deserDef = ctxt.getAnnotationIntrospector().findDeserializer(ann);
/* 416 */     if (deserDef == null) {
/* 417 */       return null;
/*     */     }
/* 419 */     JsonDeserializer<Object> deser = ctxt.deserializerInstance(ann, deserDef);
/*     */     
/* 421 */     return findConvertingDeserializer(ctxt, ann, deser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<Object> findConvertingDeserializer(DeserializationContext ctxt, Annotated a, JsonDeserializer<Object> deser)
/*     */     throws JsonMappingException
/*     */   {
/* 434 */     Converter<Object, Object> conv = findConverter(ctxt, a);
/* 435 */     if (conv == null) {
/* 436 */       return deser;
/*     */     }
/* 438 */     JavaType delegateType = conv.getInputType(ctxt.getTypeFactory());
/* 439 */     return new StdDelegatingDeserializer(conv, delegateType, deser);
/*     */   }
/*     */   
/*     */ 
/*     */   protected Converter<Object, Object> findConverter(DeserializationContext ctxt, Annotated a)
/*     */     throws JsonMappingException
/*     */   {
/* 446 */     Object convDef = ctxt.getAnnotationIntrospector().findDeserializationConverter(a);
/* 447 */     if (convDef == null) {
/* 448 */       return null;
/*     */     }
/* 450 */     return ctxt.converterInstance(a, convDef);
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
/*     */   private JavaType modifyTypeByAnnotation(DeserializationContext ctxt, Annotated a, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 472 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 473 */     if (intr == null) {
/* 474 */       return type;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 480 */     if (type.isMapLikeType()) {
/* 481 */       JavaType keyType = type.getKeyType();
/*     */       
/*     */ 
/*     */ 
/* 485 */       if ((keyType != null) && (keyType.getValueHandler() == null)) {
/* 486 */         Object kdDef = intr.findKeyDeserializer(a);
/* 487 */         if (kdDef != null) {
/* 488 */           KeyDeserializer kd = ctxt.keyDeserializerInstance(a, kdDef);
/* 489 */           if (kd != null) {
/* 490 */             type = ((MapLikeType)type).withKeyValueHandler(kd);
/* 491 */             keyType = type.getKeyType();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 496 */     JavaType contentType = type.getContentType();
/* 497 */     if ((contentType != null) && 
/* 498 */       (contentType.getValueHandler() == null)) {
/* 499 */       Object cdDef = intr.findContentDeserializer(a);
/* 500 */       if (cdDef != null) {
/* 501 */         JsonDeserializer<?> cd = null;
/* 502 */         if ((cdDef instanceof JsonDeserializer)) {
/* 503 */           cdDef = (JsonDeserializer)cdDef;
/*     */         } else {
/* 505 */           Class<?> cdClass = _verifyAsClass(cdDef, "findContentDeserializer", JsonDeserializer.None.class);
/* 506 */           if (cdClass != null) {
/* 507 */             cd = ctxt.deserializerInstance(a, cdClass);
/*     */           }
/*     */         }
/* 510 */         if (cd != null) {
/* 511 */           type = type.withContentValueHandler(cd);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 519 */     type = intr.refineDeserializationType(ctxt.getConfig(), a, type);
/*     */     
/* 521 */     return type;
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
/*     */   private boolean _hasCustomValueHandler(JavaType t)
/*     */   {
/* 537 */     if (t.isContainerType()) {
/* 538 */       JavaType ct = t.getContentType();
/* 539 */       if (ct != null) {
/* 540 */         return (ct.getValueHandler() != null) || (ct.getTypeHandler() != null);
/*     */       }
/*     */     }
/* 543 */     return false;
/*     */   }
/*     */   
/*     */   private Class<?> _verifyAsClass(Object src, String methodName, Class<?> noneClass)
/*     */   {
/* 548 */     if (src == null) {
/* 549 */       return null;
/*     */     }
/* 551 */     if (!(src instanceof Class)) {
/* 552 */       throw new IllegalStateException("AnnotationIntrospector." + methodName + "() returned value of type " + src.getClass().getName() + ": expected type JsonSerializer or Class<JsonSerializer> instead");
/*     */     }
/* 554 */     Class<?> cls = (Class)src;
/* 555 */     if ((cls == noneClass) || (ClassUtil.isBogusClass(cls))) {
/* 556 */       return null;
/*     */     }
/* 558 */     return cls;
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
/*     */   protected JsonDeserializer<Object> _handleUnknownValueDeserializer(DeserializationContext ctxt, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 574 */     Class<?> rawClass = type.getRawClass();
/* 575 */     if (!ClassUtil.isConcrete(rawClass)) {
/* 576 */       ctxt.reportMappingException("Can not find a Value deserializer for abstract type %s", new Object[] { type });
/*     */     }
/* 578 */     ctxt.reportMappingException("Can not find a Value deserializer for type %s", new Object[] { type });
/* 579 */     return null;
/*     */   }
/*     */   
/*     */   protected KeyDeserializer _handleUnknownKeyDeserializer(DeserializationContext ctxt, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 585 */     ctxt.reportMappingException("Can not find a (Map) Key deserializer for type %s", new Object[] { type });
/* 586 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\DeserializerCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */