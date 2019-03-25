/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties.Value;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyBasedCreator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.PropertyValueBuffer;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class MapDeserializer
/*     */   extends ContainerDeserializerBase<Map<Object, Object>>
/*     */   implements ContextualDeserializer, ResolvableDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JavaType _mapType;
/*     */   protected final KeyDeserializer _keyDeserializer;
/*     */   protected boolean _standardStringKey;
/*     */   protected final JsonDeserializer<Object> _valueDeserializer;
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   protected final ValueInstantiator _valueInstantiator;
/*     */   protected final boolean _hasDefaultCreator;
/*     */   protected JsonDeserializer<Object> _delegateDeserializer;
/*     */   protected PropertyBasedCreator _propertyBasedCreator;
/*     */   protected Set<String> _ignorableProperties;
/*     */   
/*     */   public MapDeserializer(JavaType mapType, ValueInstantiator valueInstantiator, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser)
/*     */   {
/*  99 */     super(mapType);
/* 100 */     this._mapType = mapType;
/* 101 */     this._keyDeserializer = keyDeser;
/* 102 */     this._valueDeserializer = valueDeser;
/* 103 */     this._valueTypeDeserializer = valueTypeDeser;
/* 104 */     this._valueInstantiator = valueInstantiator;
/* 105 */     this._hasDefaultCreator = valueInstantiator.canCreateUsingDefault();
/* 106 */     this._delegateDeserializer = null;
/* 107 */     this._propertyBasedCreator = null;
/* 108 */     this._standardStringKey = _isStdKeyDeser(mapType, keyDeser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapDeserializer(MapDeserializer src)
/*     */   {
/* 117 */     super(src._mapType);
/* 118 */     this._mapType = src._mapType;
/* 119 */     this._keyDeserializer = src._keyDeserializer;
/* 120 */     this._valueDeserializer = src._valueDeserializer;
/* 121 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/* 122 */     this._valueInstantiator = src._valueInstantiator;
/* 123 */     this._propertyBasedCreator = src._propertyBasedCreator;
/* 124 */     this._delegateDeserializer = src._delegateDeserializer;
/* 125 */     this._hasDefaultCreator = src._hasDefaultCreator;
/*     */     
/* 127 */     this._ignorableProperties = src._ignorableProperties;
/*     */     
/* 129 */     this._standardStringKey = src._standardStringKey;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapDeserializer(MapDeserializer src, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, Set<String> ignorable)
/*     */   {
/* 137 */     super(src._mapType);
/* 138 */     this._mapType = src._mapType;
/* 139 */     this._keyDeserializer = keyDeser;
/* 140 */     this._valueDeserializer = valueDeser;
/* 141 */     this._valueTypeDeserializer = valueTypeDeser;
/* 142 */     this._valueInstantiator = src._valueInstantiator;
/* 143 */     this._propertyBasedCreator = src._propertyBasedCreator;
/* 144 */     this._delegateDeserializer = src._delegateDeserializer;
/* 145 */     this._hasDefaultCreator = src._hasDefaultCreator;
/* 146 */     this._ignorableProperties = ignorable;
/*     */     
/* 148 */     this._standardStringKey = _isStdKeyDeser(this._mapType, keyDeser);
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
/*     */   protected MapDeserializer withResolved(KeyDeserializer keyDeser, TypeDeserializer valueTypeDeser, JsonDeserializer<?> valueDeser, Set<String> ignorable)
/*     */   {
/* 161 */     if ((this._keyDeserializer == keyDeser) && (this._valueDeserializer == valueDeser) && (this._valueTypeDeserializer == valueTypeDeser) && (this._ignorableProperties == ignorable))
/*     */     {
/* 163 */       return this;
/*     */     }
/* 165 */     return new MapDeserializer(this, keyDeser, valueDeser, valueTypeDeser, ignorable);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean _isStdKeyDeser(JavaType mapType, KeyDeserializer keyDeser)
/*     */   {
/* 175 */     if (keyDeser == null) {
/* 176 */       return true;
/*     */     }
/* 178 */     JavaType keyType = mapType.getKeyType();
/* 179 */     if (keyType == null) {
/* 180 */       return true;
/*     */     }
/* 182 */     Class<?> rawKeyType = keyType.getRawClass();
/* 183 */     return ((rawKeyType == String.class) || (rawKeyType == Object.class)) && (isDefaultKeyDeserializer(keyDeser));
/*     */   }
/*     */   
/*     */   public void setIgnorableProperties(String[] ignorable)
/*     */   {
/* 188 */     this._ignorableProperties = ((ignorable == null) || (ignorable.length == 0) ? null : ArrayBuilders.arrayToSet(ignorable));
/*     */   }
/*     */   
/*     */   public void setIgnorableProperties(Set<String> ignorable)
/*     */   {
/* 193 */     this._ignorableProperties = ((ignorable == null) || (ignorable.size() == 0) ? null : ignorable);
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
/*     */   public void resolve(DeserializationContext ctxt)
/*     */     throws JsonMappingException
/*     */   {
/* 207 */     if (this._valueInstantiator != null) {
/* 208 */       if (this._valueInstantiator.canCreateUsingDelegate()) {
/* 209 */         JavaType delegateType = this._valueInstantiator.getDelegateType(ctxt.getConfig());
/* 210 */         if (delegateType == null) {
/* 211 */           throw new IllegalArgumentException("Invalid delegate-creator definition for " + this._mapType + ": value instantiator (" + this._valueInstantiator.getClass().getName() + ") returned true for 'canCreateUsingDelegate()', but null for 'getDelegateType()'");
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 219 */         this._delegateDeserializer = findDeserializer(ctxt, delegateType, null);
/* 220 */       } else if (this._valueInstantiator.canCreateUsingArrayDelegate()) {
/* 221 */         JavaType delegateType = this._valueInstantiator.getArrayDelegateType(ctxt.getConfig());
/* 222 */         if (delegateType == null) {
/* 223 */           throw new IllegalArgumentException("Invalid delegate-creator definition for " + this._mapType + ": value instantiator (" + this._valueInstantiator.getClass().getName() + ") returned true for 'canCreateUsingDelegate()', but null for 'getArrayDelegateType()'");
/*     */         }
/*     */         
/*     */ 
/* 227 */         this._delegateDeserializer = findDeserializer(ctxt, delegateType, null);
/*     */       }
/*     */     }
/* 230 */     if (this._valueInstantiator.canCreateFromObjectWith()) {
/* 231 */       SettableBeanProperty[] creatorProps = this._valueInstantiator.getFromObjectArguments(ctxt.getConfig());
/* 232 */       this._propertyBasedCreator = PropertyBasedCreator.construct(ctxt, this._valueInstantiator, creatorProps);
/*     */     }
/* 234 */     this._standardStringKey = _isStdKeyDeser(this._mapType, this._keyDeserializer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 245 */     KeyDeserializer kd = this._keyDeserializer;
/* 246 */     if (kd == null) {
/* 247 */       kd = ctxt.findKeyDeserializer(this._mapType.getKeyType(), property);
/*     */     }
/* 249 */     else if ((kd instanceof ContextualKeyDeserializer)) {
/* 250 */       kd = ((ContextualKeyDeserializer)kd).createContextual(ctxt, property);
/*     */     }
/*     */     
/*     */ 
/* 254 */     JsonDeserializer<?> vd = this._valueDeserializer;
/*     */     
/* 256 */     if (property != null) {
/* 257 */       vd = findConvertingContentDeserializer(ctxt, property, vd);
/*     */     }
/* 259 */     JavaType vt = this._mapType.getContentType();
/* 260 */     if (vd == null) {
/* 261 */       vd = ctxt.findContextualValueDeserializer(vt, property);
/*     */     } else {
/* 263 */       vd = ctxt.handleSecondaryContextualization(vd, property, vt);
/*     */     }
/* 265 */     TypeDeserializer vtd = this._valueTypeDeserializer;
/* 266 */     if (vtd != null) {
/* 267 */       vtd = vtd.forProperty(property);
/*     */     }
/* 269 */     Set<String> ignored = this._ignorableProperties;
/* 270 */     AnnotationIntrospector intr = ctxt.getAnnotationIntrospector();
/* 271 */     if ((intr != null) && (property != null)) {
/* 272 */       AnnotatedMember member = property.getMember();
/* 273 */       if (member != null) {
/* 274 */         JsonIgnoreProperties.Value ignorals = intr.findPropertyIgnorals(member);
/* 275 */         if (ignorals != null) {
/* 276 */           Set<String> ignoresToAdd = ignorals.findIgnoredForDeserialization();
/* 277 */           if (!ignoresToAdd.isEmpty()) {
/* 278 */             ignored = ignored == null ? new HashSet() : new HashSet(ignored);
/* 279 */             for (String str : ignoresToAdd) {
/* 280 */               ignored.add(str);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 286 */     return withResolved(kd, vtd, vd, ignored);
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
/* 297 */     return this._mapType.getContentType();
/*     */   }
/*     */   
/*     */   public JsonDeserializer<Object> getContentDeserializer()
/*     */   {
/* 302 */     return this._valueDeserializer;
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
/*     */   public boolean isCachable()
/*     */   {
/* 329 */     return (this._valueDeserializer == null) && (this._keyDeserializer == null) && (this._valueTypeDeserializer == null) && (this._ignorableProperties == null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<Object, Object> deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 339 */     if (this._propertyBasedCreator != null) {
/* 340 */       return _deserializeUsingCreator(p, ctxt);
/*     */     }
/* 342 */     if (this._delegateDeserializer != null) {
/* 343 */       return (Map)this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
/*     */     }
/*     */     
/* 346 */     if (!this._hasDefaultCreator) {
/* 347 */       return (Map)ctxt.handleMissingInstantiator(getMapClass(), p, "no default constructor found", new Object[0]);
/*     */     }
/*     */     
/*     */ 
/* 351 */     JsonToken t = p.getCurrentToken();
/* 352 */     if ((t != JsonToken.START_OBJECT) && (t != JsonToken.FIELD_NAME) && (t != JsonToken.END_OBJECT))
/*     */     {
/* 354 */       if (t == JsonToken.VALUE_STRING) {
/* 355 */         return (Map)this._valueInstantiator.createFromString(ctxt, p.getText());
/*     */       }
/*     */       
/* 358 */       return (Map)_deserializeFromEmpty(p, ctxt);
/*     */     }
/* 360 */     Map<Object, Object> result = (Map)this._valueInstantiator.createUsingDefault(ctxt);
/* 361 */     if (this._standardStringKey) {
/* 362 */       _readAndBindStringKeyMap(p, ctxt, result);
/* 363 */       return result;
/*     */     }
/* 365 */     _readAndBind(p, ctxt, result);
/* 366 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<Object, Object> deserialize(JsonParser p, DeserializationContext ctxt, Map<Object, Object> result)
/*     */     throws IOException
/*     */   {
/* 376 */     p.setCurrentValue(result);
/*     */     
/*     */ 
/* 379 */     JsonToken t = p.getCurrentToken();
/* 380 */     if ((t != JsonToken.START_OBJECT) && (t != JsonToken.FIELD_NAME)) {
/* 381 */       return (Map)ctxt.handleUnexpectedToken(getMapClass(), p);
/*     */     }
/* 383 */     if (this._standardStringKey) {
/* 384 */       _readAndBindStringKeyMap(p, ctxt, result);
/* 385 */       return result;
/*     */     }
/* 387 */     _readAndBind(p, ctxt, result);
/* 388 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 397 */     return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 407 */   public final Class<?> getMapClass() { return this._mapType.getRawClass(); }
/*     */   
/* 409 */   public JavaType getValueType() { return this._mapType; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void _readAndBind(JsonParser p, DeserializationContext ctxt, Map<Object, Object> result)
/*     */     throws IOException
/*     */   {
/* 420 */     KeyDeserializer keyDes = this._keyDeserializer;
/* 421 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 422 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*     */     
/* 424 */     MapReferringAccumulator referringAccumulator = null;
/* 425 */     boolean useObjectId = valueDes.getObjectIdReader() != null;
/* 426 */     if (useObjectId) {
/* 427 */       referringAccumulator = new MapReferringAccumulator(this._mapType.getContentType().getRawClass(), result);
/*     */     }
/*     */     
/*     */     String keyStr;
/* 431 */     if (p.isExpectedStartObjectToken()) {
/* 432 */       keyStr = p.nextFieldName();
/*     */     } else {
/* 434 */       JsonToken t = p.getCurrentToken();
/* 435 */       if (t == JsonToken.END_OBJECT) {
/* 436 */         return;
/*     */       }
/* 438 */       if (t != JsonToken.FIELD_NAME)
/* 439 */         ctxt.reportWrongTokenException(p, JsonToken.FIELD_NAME, null, new Object[0]);
/*     */     }
/* 441 */     for (String keyStr = p.getCurrentName(); 
/*     */         
/*     */ 
/* 444 */         keyStr != null; keyStr = p.nextFieldName()) {
/* 445 */       Object key = keyDes.deserializeKey(keyStr, ctxt);
/*     */       
/* 447 */       JsonToken t = p.nextToken();
/* 448 */       if ((this._ignorableProperties != null) && (this._ignorableProperties.contains(keyStr))) {
/* 449 */         p.skipChildren();
/*     */       } else {
/*     */         try
/*     */         {
/*     */           Object value;
/*     */           Object value;
/* 455 */           if (t == JsonToken.VALUE_NULL) {
/* 456 */             value = valueDes.getNullValue(ctxt); } else { Object value;
/* 457 */             if (typeDeser == null) {
/* 458 */               value = valueDes.deserialize(p, ctxt);
/*     */             } else
/* 460 */               value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */           }
/* 462 */           if (useObjectId) {
/* 463 */             referringAccumulator.put(key, value);
/*     */           } else {
/* 465 */             result.put(key, value);
/*     */           }
/*     */         } catch (UnresolvedForwardReference reference) {
/* 468 */           handleUnresolvedReference(p, referringAccumulator, key, reference);
/*     */         } catch (Exception e) {
/* 470 */           wrapAndThrow(e, result, keyStr);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void _readAndBindStringKeyMap(JsonParser p, DeserializationContext ctxt, Map<Object, Object> result)
/*     */     throws IOException
/*     */   {
/* 483 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 484 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/* 485 */     MapReferringAccumulator referringAccumulator = null;
/* 486 */     boolean useObjectId = valueDes.getObjectIdReader() != null;
/* 487 */     if (useObjectId) {
/* 488 */       referringAccumulator = new MapReferringAccumulator(this._mapType.getContentType().getRawClass(), result);
/*     */     }
/*     */     
/*     */     String key;
/* 492 */     if (p.isExpectedStartObjectToken()) {
/* 493 */       key = p.nextFieldName();
/*     */     } else {
/* 495 */       JsonToken t = p.getCurrentToken();
/* 496 */       if (t == JsonToken.END_OBJECT) {
/* 497 */         return;
/*     */       }
/* 499 */       if (t != JsonToken.FIELD_NAME)
/* 500 */         ctxt.reportWrongTokenException(p, JsonToken.FIELD_NAME, null, new Object[0]);
/*     */     }
/* 502 */     for (String key = p.getCurrentName(); 
/*     */         
/*     */ 
/* 505 */         key != null; key = p.nextFieldName()) {
/* 506 */       JsonToken t = p.nextToken();
/* 507 */       if ((this._ignorableProperties != null) && (this._ignorableProperties.contains(key))) {
/* 508 */         p.skipChildren();
/*     */       } else {
/*     */         try
/*     */         {
/*     */           Object value;
/*     */           Object value;
/* 514 */           if (t == JsonToken.VALUE_NULL) {
/* 515 */             value = valueDes.getNullValue(ctxt); } else { Object value;
/* 516 */             if (typeDeser == null) {
/* 517 */               value = valueDes.deserialize(p, ctxt);
/*     */             } else
/* 519 */               value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */           }
/* 521 */           if (useObjectId) {
/* 522 */             referringAccumulator.put(key, value);
/*     */           } else {
/* 524 */             result.put(key, value);
/*     */           }
/*     */         } catch (UnresolvedForwardReference reference) {
/* 527 */           handleUnresolvedReference(p, referringAccumulator, key, reference);
/*     */         } catch (Exception e) {
/* 529 */           wrapAndThrow(e, result, key);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Map<Object, Object> _deserializeUsingCreator(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 538 */     PropertyBasedCreator creator = this._propertyBasedCreator;
/*     */     
/* 540 */     PropertyValueBuffer buffer = creator.startBuilding(p, ctxt, null);
/*     */     
/* 542 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 543 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*     */     
/*     */     String key;
/* 546 */     if (p.isExpectedStartObjectToken()) {
/* 547 */       key = p.nextFieldName(); } else { String key;
/* 548 */       if (p.hasToken(JsonToken.FIELD_NAME))
/* 549 */         key = p.getCurrentName();
/*     */     }
/* 551 */     for (String key = null; 
/*     */         
/*     */ 
/* 554 */         key != null; key = p.nextFieldName()) {
/* 555 */       JsonToken t = p.nextToken();
/* 556 */       if ((this._ignorableProperties != null) && (this._ignorableProperties.contains(key))) {
/* 557 */         p.skipChildren();
/*     */       }
/*     */       else
/*     */       {
/* 561 */         SettableBeanProperty prop = creator.findCreatorProperty(key);
/* 562 */         if (prop != null)
/*     */         {
/* 564 */           if (buffer.assignParameter(prop, prop.deserialize(p, ctxt))) {
/* 565 */             p.nextToken();
/*     */             Map<Object, Object> result;
/*     */             try {
/* 568 */               result = (Map)creator.build(ctxt, buffer);
/*     */             } catch (Exception e) {
/* 570 */               wrapAndThrow(e, this._mapType.getRawClass(), key);
/* 571 */               return null;
/*     */             }
/* 573 */             _readAndBind(p, ctxt, result);
/* 574 */             return result;
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 579 */           Object actualKey = this._keyDeserializer.deserializeKey(key, ctxt);
/*     */           Object value;
/*     */           try {
/*     */             Object value;
/* 583 */             if (t == JsonToken.VALUE_NULL) {
/* 584 */               value = valueDes.getNullValue(ctxt); } else { Object value;
/* 585 */               if (typeDeser == null) {
/* 586 */                 value = valueDes.deserialize(p, ctxt);
/*     */               } else
/* 588 */                 value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */             }
/*     */           } catch (Exception e) {
/* 591 */             wrapAndThrow(e, this._mapType.getRawClass(), key);
/* 592 */             return null;
/*     */           }
/* 594 */           buffer.bufferMapProperty(actualKey, value);
/*     */         }
/*     */       }
/*     */     }
/*     */     try {
/* 599 */       return (Map)creator.build(ctxt, buffer);
/*     */     } catch (Exception e) {
/* 601 */       wrapAndThrow(e, this._mapType.getRawClass(), key); }
/* 602 */     return null;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected void wrapAndThrow(Throwable t, Object ref) throws IOException
/*     */   {
/* 608 */     wrapAndThrow(t, ref, null);
/*     */   }
/*     */   
/*     */ 
/*     */   private void handleUnresolvedReference(JsonParser jp, MapReferringAccumulator accumulator, Object key, UnresolvedForwardReference reference)
/*     */     throws JsonMappingException
/*     */   {
/* 615 */     if (accumulator == null) {
/* 616 */       throw JsonMappingException.from(jp, "Unresolved forward reference but no identity info.", reference);
/*     */     }
/* 618 */     ReadableObjectId.Referring referring = accumulator.handleUnresolvedReference(reference, key);
/* 619 */     reference.getRoid().appendReferring(referring);
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class MapReferringAccumulator
/*     */   {
/*     */     private final Class<?> _valueType;
/*     */     
/*     */     private Map<Object, Object> _result;
/* 628 */     private List<MapDeserializer.MapReferring> _accumulator = new ArrayList();
/*     */     
/*     */     public MapReferringAccumulator(Class<?> valueType, Map<Object, Object> result) {
/* 631 */       this._valueType = valueType;
/* 632 */       this._result = result;
/*     */     }
/*     */     
/*     */     public void put(Object key, Object value)
/*     */     {
/* 637 */       if (this._accumulator.isEmpty()) {
/* 638 */         this._result.put(key, value);
/*     */       } else {
/* 640 */         MapDeserializer.MapReferring ref = (MapDeserializer.MapReferring)this._accumulator.get(this._accumulator.size() - 1);
/* 641 */         ref.next.put(key, value);
/*     */       }
/*     */     }
/*     */     
/*     */     public ReadableObjectId.Referring handleUnresolvedReference(UnresolvedForwardReference reference, Object key)
/*     */     {
/* 647 */       MapDeserializer.MapReferring id = new MapDeserializer.MapReferring(this, reference, this._valueType, key);
/* 648 */       this._accumulator.add(id);
/* 649 */       return id;
/*     */     }
/*     */     
/*     */     public void resolveForwardReference(Object id, Object value) throws IOException
/*     */     {
/* 654 */       Iterator<MapDeserializer.MapReferring> iterator = this._accumulator.iterator();
/*     */       
/*     */ 
/*     */ 
/* 658 */       Map<Object, Object> previous = this._result;
/* 659 */       while (iterator.hasNext()) {
/* 660 */         MapDeserializer.MapReferring ref = (MapDeserializer.MapReferring)iterator.next();
/* 661 */         if (ref.hasId(id)) {
/* 662 */           iterator.remove();
/* 663 */           previous.put(ref.key, value);
/* 664 */           previous.putAll(ref.next);
/* 665 */           return;
/*     */         }
/* 667 */         previous = ref.next;
/*     */       }
/*     */       
/* 670 */       throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id + "] that wasn't previously seen as unresolved.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static class MapReferring
/*     */     extends ReadableObjectId.Referring
/*     */   {
/*     */     private final MapDeserializer.MapReferringAccumulator _parent;
/*     */     
/*     */ 
/* 683 */     public final Map<Object, Object> next = new LinkedHashMap();
/*     */     
/*     */     public final Object key;
/*     */     
/*     */     MapReferring(MapDeserializer.MapReferringAccumulator parent, UnresolvedForwardReference ref, Class<?> valueType, Object key)
/*     */     {
/* 689 */       super(valueType);
/* 690 */       this._parent = parent;
/* 691 */       this.key = key;
/*     */     }
/*     */     
/*     */     public void handleResolvedForwardReference(Object id, Object value) throws IOException
/*     */     {
/* 696 */       this._parent.resolveForwardReference(id, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\MapDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */