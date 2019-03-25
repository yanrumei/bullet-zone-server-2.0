/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.ObjectBuffer;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class UntypedObjectDeserializer
/*     */   extends StdDeserializer<Object>
/*     */   implements ResolvableDeserializer, ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  38 */   protected static final Object[] NO_OBJECTS = new Object[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*  44 */   public static final UntypedObjectDeserializer instance = new UntypedObjectDeserializer(null, null);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<Object> _mapDeserializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<Object> _listDeserializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<Object> _stringDeserializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<Object> _numberDeserializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JavaType _listType;
/*     */   
/*     */ 
/*     */ 
/*     */   protected JavaType _mapType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public UntypedObjectDeserializer()
/*     */   {
/*  81 */     this(null, null);
/*     */   }
/*     */   
/*     */   public UntypedObjectDeserializer(JavaType listType, JavaType mapType) {
/*  85 */     super(Object.class);
/*  86 */     this._listType = listType;
/*  87 */     this._mapType = mapType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public UntypedObjectDeserializer(UntypedObjectDeserializer base, JsonDeserializer<?> mapDeser, JsonDeserializer<?> listDeser, JsonDeserializer<?> stringDeser, JsonDeserializer<?> numberDeser)
/*     */   {
/*  95 */     super(Object.class);
/*  96 */     this._mapDeserializer = mapDeser;
/*  97 */     this._listDeserializer = listDeser;
/*  98 */     this._stringDeserializer = stringDeser;
/*  99 */     this._numberDeserializer = numberDeser;
/* 100 */     this._listType = base._listType;
/* 101 */     this._mapType = base._mapType;
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
/*     */   public void resolve(DeserializationContext ctxt)
/*     */     throws JsonMappingException
/*     */   {
/* 119 */     JavaType obType = ctxt.constructType(Object.class);
/* 120 */     JavaType stringType = ctxt.constructType(String.class);
/* 121 */     TypeFactory tf = ctxt.getTypeFactory();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 133 */     if (this._listType == null) {
/* 134 */       this._listDeserializer = _clearIfStdImpl(_findCustomDeser(ctxt, tf.constructCollectionType(List.class, obType)));
/*     */     }
/*     */     else {
/* 137 */       this._listDeserializer = _findCustomDeser(ctxt, this._listType);
/*     */     }
/* 139 */     if (this._mapType == null) {
/* 140 */       this._mapDeserializer = _clearIfStdImpl(_findCustomDeser(ctxt, tf.constructMapType(Map.class, stringType, obType)));
/*     */     }
/*     */     else {
/* 143 */       this._mapDeserializer = _findCustomDeser(ctxt, this._mapType);
/*     */     }
/* 145 */     this._stringDeserializer = _clearIfStdImpl(_findCustomDeser(ctxt, stringType));
/* 146 */     this._numberDeserializer = _clearIfStdImpl(_findCustomDeser(ctxt, tf.constructType(Number.class)));
/*     */     
/*     */ 
/*     */ 
/* 150 */     JavaType unknown = TypeFactory.unknownType();
/* 151 */     this._mapDeserializer = ctxt.handleSecondaryContextualization(this._mapDeserializer, null, unknown);
/* 152 */     this._listDeserializer = ctxt.handleSecondaryContextualization(this._listDeserializer, null, unknown);
/* 153 */     this._stringDeserializer = ctxt.handleSecondaryContextualization(this._stringDeserializer, null, unknown);
/* 154 */     this._numberDeserializer = ctxt.handleSecondaryContextualization(this._numberDeserializer, null, unknown);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JsonDeserializer<Object> _findCustomDeser(DeserializationContext ctxt, JavaType type)
/*     */     throws JsonMappingException
/*     */   {
/* 162 */     return ctxt.findNonContextualValueDeserializer(type);
/*     */   }
/*     */   
/*     */   protected JsonDeserializer<Object> _clearIfStdImpl(JsonDeserializer<Object> deser) {
/* 166 */     return ClassUtil.isJacksonStdImpl(deser) ? null : deser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 179 */     if ((this._stringDeserializer == null) && (this._numberDeserializer == null) && (this._mapDeserializer == null) && (this._listDeserializer == null) && (getClass() == UntypedObjectDeserializer.class))
/*     */     {
/*     */ 
/* 182 */       return Vanilla.std;
/*     */     }
/* 184 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   protected JsonDeserializer<?> _withResolved(JsonDeserializer<?> mapDeser, JsonDeserializer<?> listDeser, JsonDeserializer<?> stringDeser, JsonDeserializer<?> numberDeser)
/*     */   {
/* 190 */     return new UntypedObjectDeserializer(this, mapDeser, listDeser, stringDeser, numberDeser);
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
/*     */   public boolean isCachable()
/*     */   {
/* 210 */     return true;
/*     */   }
/*     */   
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 216 */     switch (p.getCurrentTokenId())
/*     */     {
/*     */ 
/*     */     case 1: 
/*     */     case 2: 
/*     */     case 5: 
/* 222 */       if (this._mapDeserializer != null) {
/* 223 */         return this._mapDeserializer.deserialize(p, ctxt);
/*     */       }
/* 225 */       return mapObject(p, ctxt);
/*     */     case 3: 
/* 227 */       if (ctxt.isEnabled(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY)) {
/* 228 */         return mapArrayToArray(p, ctxt);
/*     */       }
/* 230 */       if (this._listDeserializer != null) {
/* 231 */         return this._listDeserializer.deserialize(p, ctxt);
/*     */       }
/* 233 */       return mapArray(p, ctxt);
/*     */     case 12: 
/* 235 */       return p.getEmbeddedObject();
/*     */     case 6: 
/* 237 */       if (this._stringDeserializer != null) {
/* 238 */         return this._stringDeserializer.deserialize(p, ctxt);
/*     */       }
/* 240 */       return p.getText();
/*     */     
/*     */     case 7: 
/* 243 */       if (this._numberDeserializer != null) {
/* 244 */         return this._numberDeserializer.deserialize(p, ctxt);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 249 */       if (ctxt.hasSomeOfFeatures(F_MASK_INT_COERCIONS)) {
/* 250 */         return _coerceIntegral(p, ctxt);
/*     */       }
/* 252 */       return p.getNumberValue();
/*     */     
/*     */     case 8: 
/* 255 */       if (this._numberDeserializer != null) {
/* 256 */         return this._numberDeserializer.deserialize(p, ctxt);
/*     */       }
/*     */       
/* 259 */       if (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 260 */         return p.getDecimalValue();
/*     */       }
/*     */       
/* 263 */       return p.getNumberValue();
/*     */     
/*     */     case 9: 
/* 266 */       return Boolean.TRUE;
/*     */     case 10: 
/* 268 */       return Boolean.FALSE;
/*     */     
/*     */     case 11: 
/* 271 */       return null;
/*     */     }
/*     */     
/*     */     
/*     */ 
/* 276 */     return ctxt.handleUnexpectedToken(Object.class, p);
/*     */   }
/*     */   
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/* 282 */     switch (p.getCurrentTokenId())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */     case 1: 
/*     */     case 3: 
/*     */     case 5: 
/* 290 */       return typeDeserializer.deserializeTypedFromAny(p, ctxt);
/*     */     
/*     */     case 12: 
/* 293 */       return p.getEmbeddedObject();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     case 6: 
/* 299 */       if (this._stringDeserializer != null) {
/* 300 */         return this._stringDeserializer.deserialize(p, ctxt);
/*     */       }
/* 302 */       return p.getText();
/*     */     
/*     */     case 7: 
/* 305 */       if (this._numberDeserializer != null) {
/* 306 */         return this._numberDeserializer.deserialize(p, ctxt);
/*     */       }
/*     */       
/* 309 */       if (ctxt.hasSomeOfFeatures(F_MASK_INT_COERCIONS)) {
/* 310 */         return _coerceIntegral(p, ctxt);
/*     */       }
/* 312 */       return p.getNumberValue();
/*     */     
/*     */     case 8: 
/* 315 */       if (this._numberDeserializer != null) {
/* 316 */         return this._numberDeserializer.deserialize(p, ctxt);
/*     */       }
/* 318 */       if (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 319 */         return p.getDecimalValue();
/*     */       }
/* 321 */       return p.getNumberValue();
/*     */     
/*     */     case 9: 
/* 324 */       return Boolean.TRUE;
/*     */     case 10: 
/* 326 */       return Boolean.FALSE;
/*     */     
/*     */     case 11: 
/* 329 */       return null;
/*     */     }
/*     */     
/* 332 */     return ctxt.handleUnexpectedToken(Object.class, p);
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
/*     */   protected Object mapArray(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 347 */     if (p.nextToken() == JsonToken.END_ARRAY) {
/* 348 */       return new ArrayList(2);
/*     */     }
/* 350 */     Object value = deserialize(p, ctxt);
/* 351 */     if (p.nextToken() == JsonToken.END_ARRAY) {
/* 352 */       ArrayList<Object> l = new ArrayList(2);
/* 353 */       l.add(value);
/* 354 */       return l;
/*     */     }
/* 356 */     Object value2 = deserialize(p, ctxt);
/* 357 */     if (p.nextToken() == JsonToken.END_ARRAY) {
/* 358 */       ArrayList<Object> l = new ArrayList(2);
/* 359 */       l.add(value);
/* 360 */       l.add(value2);
/* 361 */       return l;
/*     */     }
/* 363 */     ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/* 364 */     Object[] values = buffer.resetAndStart();
/* 365 */     int ptr = 0;
/* 366 */     values[(ptr++)] = value;
/* 367 */     values[(ptr++)] = value2;
/* 368 */     int totalSize = ptr;
/*     */     do {
/* 370 */       value = deserialize(p, ctxt);
/* 371 */       totalSize++;
/* 372 */       if (ptr >= values.length) {
/* 373 */         values = buffer.appendCompletedChunk(values);
/* 374 */         ptr = 0;
/*     */       }
/* 376 */       values[(ptr++)] = value;
/* 377 */     } while (p.nextToken() != JsonToken.END_ARRAY);
/*     */     
/* 379 */     ArrayList<Object> result = new ArrayList(totalSize);
/* 380 */     buffer.completeAndClearBuffer(values, ptr, result);
/* 381 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object mapObject(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 391 */     JsonToken t = p.getCurrentToken();
/*     */     String key1;
/* 393 */     String key1; if (t == JsonToken.START_OBJECT) {
/* 394 */       key1 = p.nextFieldName(); } else { String key1;
/* 395 */       if (t == JsonToken.FIELD_NAME) {
/* 396 */         key1 = p.getCurrentName();
/*     */       } else {
/* 398 */         if (t != JsonToken.END_OBJECT) {
/* 399 */           return ctxt.handleUnexpectedToken(handledType(), p);
/*     */         }
/* 401 */         key1 = null;
/*     */       } }
/* 403 */     if (key1 == null)
/*     */     {
/* 405 */       return new LinkedHashMap(2);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 410 */     p.nextToken();
/* 411 */     Object value1 = deserialize(p, ctxt);
/*     */     
/* 413 */     String key2 = p.nextFieldName();
/* 414 */     if (key2 == null)
/*     */     {
/* 416 */       LinkedHashMap<String, Object> result = new LinkedHashMap(2);
/* 417 */       result.put(key1, value1);
/* 418 */       return result;
/*     */     }
/* 420 */     p.nextToken();
/* 421 */     Object value2 = deserialize(p, ctxt);
/*     */     
/* 423 */     String key = p.nextFieldName();
/*     */     
/* 425 */     if (key == null) {
/* 426 */       LinkedHashMap<String, Object> result = new LinkedHashMap(4);
/* 427 */       result.put(key1, value1);
/* 428 */       result.put(key2, value2);
/* 429 */       return result;
/*     */     }
/*     */     
/* 432 */     LinkedHashMap<String, Object> result = new LinkedHashMap();
/* 433 */     result.put(key1, value1);
/* 434 */     result.put(key2, value2);
/*     */     do
/*     */     {
/* 437 */       p.nextToken();
/* 438 */       result.put(key, deserialize(p, ctxt));
/* 439 */     } while ((key = p.nextFieldName()) != null);
/* 440 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object[] mapArrayToArray(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 449 */     if (p.nextToken() == JsonToken.END_ARRAY) {
/* 450 */       return NO_OBJECTS;
/*     */     }
/* 452 */     ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/* 453 */     Object[] values = buffer.resetAndStart();
/* 454 */     int ptr = 0;
/*     */     do {
/* 456 */       Object value = deserialize(p, ctxt);
/* 457 */       if (ptr >= values.length) {
/* 458 */         values = buffer.appendCompletedChunk(values);
/* 459 */         ptr = 0;
/*     */       }
/* 461 */       values[(ptr++)] = value;
/* 462 */     } while (p.nextToken() != JsonToken.END_ARRAY);
/* 463 */     return buffer.completeAndClearBuffer(values, ptr);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class Vanilla
/*     */     extends StdDeserializer<Object>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 479 */     public static final Vanilla std = new Vanilla();
/*     */     
/* 481 */     public Vanilla() { super(); }
/*     */     
/*     */     public Object deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 486 */       switch (p.getCurrentTokenId())
/*     */       {
/*     */       case 1: 
/* 489 */         JsonToken t = p.nextToken();
/* 490 */         if (t == JsonToken.END_OBJECT) {
/* 491 */           return new LinkedHashMap(2);
/*     */         }
/*     */       
/*     */       case 5: 
/* 495 */         return mapObject(p, ctxt);
/*     */       
/*     */       case 3: 
/* 498 */         JsonToken t = p.nextToken();
/* 499 */         if (t == JsonToken.END_ARRAY) {
/* 500 */           if (ctxt.isEnabled(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY)) {
/* 501 */             return UntypedObjectDeserializer.NO_OBJECTS;
/*     */           }
/* 503 */           return new ArrayList(2);
/*     */         }
/*     */         
/* 506 */         if (ctxt.isEnabled(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY)) {
/* 507 */           return mapArrayToArray(p, ctxt);
/*     */         }
/* 509 */         return mapArray(p, ctxt);
/*     */       case 12: 
/* 511 */         return p.getEmbeddedObject();
/*     */       case 6: 
/* 513 */         return p.getText();
/*     */       
/*     */       case 7: 
/* 516 */         if (ctxt.hasSomeOfFeatures(F_MASK_INT_COERCIONS)) {
/* 517 */           return _coerceIntegral(p, ctxt);
/*     */         }
/* 519 */         return p.getNumberValue();
/*     */       
/*     */       case 8: 
/* 522 */         if (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 523 */           return p.getDecimalValue();
/*     */         }
/* 525 */         return p.getNumberValue();
/*     */       
/*     */       case 9: 
/* 528 */         return Boolean.TRUE;
/*     */       case 10: 
/* 530 */         return Boolean.FALSE;
/*     */       
/*     */       case 11: 
/* 533 */         return null;
/*     */       
/*     */ 
/*     */ 
/*     */       case 2: 
/* 538 */         return new LinkedHashMap(2);
/*     */       }
/*     */       
/*     */       
/*     */ 
/* 543 */       return ctxt.handleUnexpectedToken(Object.class, p);
/*     */     }
/*     */     
/*     */     public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */       throws IOException
/*     */     {
/* 549 */       switch (p.getCurrentTokenId()) {
/*     */       case 1: 
/*     */       case 3: 
/*     */       case 5: 
/* 553 */         return typeDeserializer.deserializeTypedFromAny(p, ctxt);
/*     */       
/*     */       case 6: 
/* 556 */         return p.getText();
/*     */       
/*     */       case 7: 
/* 559 */         if (ctxt.isEnabled(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS)) {
/* 560 */           return p.getBigIntegerValue();
/*     */         }
/* 562 */         return p.getNumberValue();
/*     */       
/*     */       case 8: 
/* 565 */         if (ctxt.isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 566 */           return p.getDecimalValue();
/*     */         }
/* 568 */         return p.getNumberValue();
/*     */       
/*     */       case 9: 
/* 571 */         return Boolean.TRUE;
/*     */       case 10: 
/* 573 */         return Boolean.FALSE;
/*     */       case 12: 
/* 575 */         return p.getEmbeddedObject();
/*     */       
/*     */       case 11: 
/* 578 */         return null;
/*     */       }
/*     */       
/* 581 */       return ctxt.handleUnexpectedToken(Object.class, p);
/*     */     }
/*     */     
/*     */     protected Object mapArray(JsonParser p, DeserializationContext ctxt) throws IOException
/*     */     {
/* 586 */       Object value = deserialize(p, ctxt);
/* 587 */       if (p.nextToken() == JsonToken.END_ARRAY) {
/* 588 */         ArrayList<Object> l = new ArrayList(2);
/* 589 */         l.add(value);
/* 590 */         return l;
/*     */       }
/* 592 */       Object value2 = deserialize(p, ctxt);
/* 593 */       if (p.nextToken() == JsonToken.END_ARRAY) {
/* 594 */         ArrayList<Object> l = new ArrayList(2);
/* 595 */         l.add(value);
/* 596 */         l.add(value2);
/* 597 */         return l;
/*     */       }
/* 599 */       ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/* 600 */       Object[] values = buffer.resetAndStart();
/* 601 */       int ptr = 0;
/* 602 */       values[(ptr++)] = value;
/* 603 */       values[(ptr++)] = value2;
/* 604 */       int totalSize = ptr;
/*     */       do {
/* 606 */         value = deserialize(p, ctxt);
/* 607 */         totalSize++;
/* 608 */         if (ptr >= values.length) {
/* 609 */           values = buffer.appendCompletedChunk(values);
/* 610 */           ptr = 0;
/*     */         }
/* 612 */         values[(ptr++)] = value;
/* 613 */       } while (p.nextToken() != JsonToken.END_ARRAY);
/*     */       
/* 615 */       ArrayList<Object> result = new ArrayList(totalSize);
/* 616 */       buffer.completeAndClearBuffer(values, ptr, result);
/* 617 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected Object mapObject(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 626 */       String key1 = p.getText();
/* 627 */       p.nextToken();
/* 628 */       Object value1 = deserialize(p, ctxt);
/*     */       
/* 630 */       String key2 = p.nextFieldName();
/* 631 */       if (key2 == null) {
/* 632 */         LinkedHashMap<String, Object> result = new LinkedHashMap(2);
/* 633 */         result.put(key1, value1);
/* 634 */         return result;
/*     */       }
/* 636 */       p.nextToken();
/* 637 */       Object value2 = deserialize(p, ctxt);
/*     */       
/* 639 */       String key = p.nextFieldName();
/* 640 */       if (key == null) {
/* 641 */         LinkedHashMap<String, Object> result = new LinkedHashMap(4);
/* 642 */         result.put(key1, value1);
/* 643 */         result.put(key2, value2);
/* 644 */         return result;
/*     */       }
/*     */       
/* 647 */       LinkedHashMap<String, Object> result = new LinkedHashMap();
/* 648 */       result.put(key1, value1);
/* 649 */       result.put(key2, value2);
/*     */       do {
/* 651 */         p.nextToken();
/* 652 */         result.put(key, deserialize(p, ctxt));
/* 653 */       } while ((key = p.nextFieldName()) != null);
/* 654 */       return result;
/*     */     }
/*     */     
/*     */ 
/*     */     protected Object[] mapArrayToArray(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 661 */       ObjectBuffer buffer = ctxt.leaseObjectBuffer();
/* 662 */       Object[] values = buffer.resetAndStart();
/* 663 */       int ptr = 0;
/*     */       do {
/* 665 */         Object value = deserialize(p, ctxt);
/* 666 */         if (ptr >= values.length) {
/* 667 */           values = buffer.appendCompletedChunk(values);
/* 668 */           ptr = 0;
/*     */         }
/* 670 */         values[(ptr++)] = value;
/* 671 */       } while (p.nextToken() != JsonToken.END_ARRAY);
/* 672 */       return buffer.completeAndClearBuffer(values, ptr);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\UntypedObjectDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */