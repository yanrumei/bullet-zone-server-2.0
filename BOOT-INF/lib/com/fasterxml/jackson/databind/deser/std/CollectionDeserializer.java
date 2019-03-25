/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Feature;
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
/*     */ import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class CollectionDeserializer
/*     */   extends ContainerDeserializerBase<Collection<Object>>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = -1L;
/*     */   protected final JavaType _collectionType;
/*     */   protected final JsonDeserializer<Object> _valueDeserializer;
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   protected final ValueInstantiator _valueInstantiator;
/*     */   protected final JsonDeserializer<Object> _delegateDeserializer;
/*     */   protected final Boolean _unwrapSingle;
/*     */   
/*     */   public CollectionDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, ValueInstantiator valueInstantiator)
/*     */   {
/*  83 */     this(collectionType, valueDeser, valueTypeDeser, valueInstantiator, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CollectionDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, ValueInstantiator valueInstantiator, JsonDeserializer<Object> delegateDeser, Boolean unwrapSingle)
/*     */   {
/*  95 */     super(collectionType);
/*  96 */     this._collectionType = collectionType;
/*  97 */     this._valueDeserializer = valueDeser;
/*  98 */     this._valueTypeDeserializer = valueTypeDeser;
/*  99 */     this._valueInstantiator = valueInstantiator;
/* 100 */     this._delegateDeserializer = delegateDeser;
/* 101 */     this._unwrapSingle = unwrapSingle;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CollectionDeserializer(CollectionDeserializer src)
/*     */   {
/* 110 */     super(src._collectionType);
/* 111 */     this._collectionType = src._collectionType;
/* 112 */     this._valueDeserializer = src._valueDeserializer;
/* 113 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/* 114 */     this._valueInstantiator = src._valueInstantiator;
/* 115 */     this._delegateDeserializer = src._delegateDeserializer;
/* 116 */     this._unwrapSingle = src._unwrapSingle;
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
/*     */   protected CollectionDeserializer withResolved(JsonDeserializer<?> dd, JsonDeserializer<?> vd, TypeDeserializer vtd, Boolean unwrapSingle)
/*     */   {
/* 129 */     if ((dd == this._delegateDeserializer) && (vd == this._valueDeserializer) && (vtd == this._valueTypeDeserializer) && (this._unwrapSingle == unwrapSingle))
/*     */     {
/* 131 */       return this;
/*     */     }
/* 133 */     return new CollectionDeserializer(this._collectionType, vd, vtd, this._valueInstantiator, dd, unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected CollectionDeserializer withResolved(JsonDeserializer<?> dd, JsonDeserializer<?> vd, TypeDeserializer vtd)
/*     */   {
/* 145 */     return withResolved(dd, vd, vtd, this._unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isCachable()
/*     */   {
/* 152 */     return (this._valueDeserializer == null) && (this._valueTypeDeserializer == null) && (this._delegateDeserializer == null);
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
/*     */   public CollectionDeserializer createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 174 */     JsonDeserializer<Object> delegateDeser = null;
/* 175 */     if (this._valueInstantiator != null) {
/* 176 */       if (this._valueInstantiator.canCreateUsingDelegate()) {
/* 177 */         JavaType delegateType = this._valueInstantiator.getDelegateType(ctxt.getConfig());
/* 178 */         if (delegateType == null) {
/* 179 */           throw new IllegalArgumentException("Invalid delegate-creator definition for " + this._collectionType + ": value instantiator (" + this._valueInstantiator.getClass().getName() + ") returned true for 'canCreateUsingDelegate()', but null for 'getDelegateType()'");
/*     */         }
/*     */         
/*     */ 
/* 183 */         delegateDeser = findDeserializer(ctxt, delegateType, property);
/* 184 */       } else if (this._valueInstantiator.canCreateUsingArrayDelegate()) {
/* 185 */         JavaType delegateType = this._valueInstantiator.getArrayDelegateType(ctxt.getConfig());
/* 186 */         if (delegateType == null) {
/* 187 */           throw new IllegalArgumentException("Invalid array-delegate-creator definition for " + this._collectionType + ": value instantiator (" + this._valueInstantiator.getClass().getName() + ") returned true for 'canCreateUsingArrayDelegate()', but null for 'getArrayDelegateType()'");
/*     */         }
/*     */         
/*     */ 
/* 191 */         delegateDeser = findDeserializer(ctxt, delegateType, property);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 197 */     Boolean unwrapSingle = findFormatFeature(ctxt, property, Collection.class, JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
/*     */     
/*     */ 
/* 200 */     JsonDeserializer<?> valueDeser = this._valueDeserializer;
/*     */     
/*     */ 
/* 203 */     valueDeser = findConvertingContentDeserializer(ctxt, property, valueDeser);
/* 204 */     JavaType vt = this._collectionType.getContentType();
/* 205 */     if (valueDeser == null) {
/* 206 */       valueDeser = ctxt.findContextualValueDeserializer(vt, property);
/*     */     } else {
/* 208 */       valueDeser = ctxt.handleSecondaryContextualization(valueDeser, property, vt);
/*     */     }
/*     */     
/* 211 */     TypeDeserializer valueTypeDeser = this._valueTypeDeserializer;
/* 212 */     if (valueTypeDeser != null) {
/* 213 */       valueTypeDeser = valueTypeDeser.forProperty(property);
/*     */     }
/* 215 */     return withResolved(delegateDeser, valueDeser, valueTypeDeser, unwrapSingle);
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
/* 226 */     return this._collectionType.getContentType();
/*     */   }
/*     */   
/*     */   public JsonDeserializer<Object> getContentDeserializer()
/*     */   {
/* 231 */     return this._valueDeserializer;
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
/*     */   public Collection<Object> deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 245 */     if (this._delegateDeserializer != null) {
/* 246 */       return (Collection)this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 253 */     if (p.hasToken(JsonToken.VALUE_STRING)) {
/* 254 */       String str = p.getText();
/* 255 */       if (str.length() == 0) {
/* 256 */         return (Collection)this._valueInstantiator.createFromString(ctxt, str);
/*     */       }
/*     */     }
/* 259 */     return deserialize(p, ctxt, (Collection)this._valueInstantiator.createUsingDefault(ctxt));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<Object> deserialize(JsonParser p, DeserializationContext ctxt, Collection<Object> result)
/*     */     throws IOException
/*     */   {
/* 268 */     if (!p.isExpectedStartArrayToken()) {
/* 269 */       return handleNonArray(p, ctxt, result);
/*     */     }
/*     */     
/* 272 */     p.setCurrentValue(result);
/*     */     
/* 274 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 275 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/* 276 */     CollectionReferringAccumulator referringAccumulator = valueDes.getObjectIdReader() == null ? null : new CollectionReferringAccumulator(this._collectionType.getContentType().getRawClass(), result);
/*     */     
/*     */ 
/*     */     JsonToken t;
/*     */     
/* 281 */     while ((t = p.nextToken()) != JsonToken.END_ARRAY) {
/*     */       try { Object value;
/*     */         Object value;
/* 284 */         if (t == JsonToken.VALUE_NULL) {
/* 285 */           value = valueDes.getNullValue(ctxt); } else { Object value;
/* 286 */           if (typeDeser == null) {
/* 287 */             value = valueDes.deserialize(p, ctxt);
/*     */           } else
/* 289 */             value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */         }
/* 291 */         if (referringAccumulator != null) {
/* 292 */           referringAccumulator.add(value);
/*     */         } else {
/* 294 */           result.add(value);
/*     */         }
/*     */       } catch (UnresolvedForwardReference reference) {
/* 297 */         if (referringAccumulator == null) {
/* 298 */           throw JsonMappingException.from(p, "Unresolved forward reference but no identity info", reference);
/*     */         }
/*     */         
/* 301 */         ReadableObjectId.Referring ref = referringAccumulator.handleUnresolvedReference(reference);
/* 302 */         reference.getRoid().appendReferring(ref);
/*     */       } catch (Exception e) {
/* 304 */         boolean wrap = (ctxt == null) || (ctxt.isEnabled(DeserializationFeature.WRAP_EXCEPTIONS));
/* 305 */         if ((!wrap) && ((e instanceof RuntimeException))) {
/* 306 */           throw ((RuntimeException)e);
/*     */         }
/* 308 */         throw JsonMappingException.wrapWithPath(e, result, result.size());
/*     */       }
/*     */     }
/* 311 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/* 320 */     return typeDeserializer.deserializeTypedFromArray(p, ctxt);
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
/*     */   protected final Collection<Object> handleNonArray(JsonParser p, DeserializationContext ctxt, Collection<Object> result)
/*     */     throws IOException
/*     */   {
/* 334 */     boolean canWrap = (this._unwrapSingle == Boolean.TRUE) || ((this._unwrapSingle == null) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)));
/*     */     
/*     */ 
/* 337 */     if (!canWrap) {
/* 338 */       return (Collection)ctxt.handleUnexpectedToken(this._collectionType.getRawClass(), p);
/*     */     }
/* 340 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 341 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/* 342 */     JsonToken t = p.getCurrentToken();
/*     */     Object value;
/*     */     try
/*     */     {
/*     */       Object value;
/* 347 */       if (t == JsonToken.VALUE_NULL) {
/* 348 */         value = valueDes.getNullValue(ctxt); } else { Object value;
/* 349 */         if (typeDeser == null) {
/* 350 */           value = valueDes.deserialize(p, ctxt);
/*     */         } else {
/* 352 */           value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 356 */       throw JsonMappingException.wrapWithPath(e, Object.class, result.size());
/*     */     }
/* 358 */     result.add(value);
/* 359 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public static final class CollectionReferringAccumulator
/*     */   {
/*     */     private final Class<?> _elementType;
/*     */     
/*     */     private final Collection<Object> _result;
/*     */     
/* 369 */     private List<CollectionDeserializer.CollectionReferring> _accumulator = new ArrayList();
/*     */     
/*     */     public CollectionReferringAccumulator(Class<?> elementType, Collection<Object> result) {
/* 372 */       this._elementType = elementType;
/* 373 */       this._result = result;
/*     */     }
/*     */     
/*     */     public void add(Object value)
/*     */     {
/* 378 */       if (this._accumulator.isEmpty()) {
/* 379 */         this._result.add(value);
/*     */       } else {
/* 381 */         CollectionDeserializer.CollectionReferring ref = (CollectionDeserializer.CollectionReferring)this._accumulator.get(this._accumulator.size() - 1);
/* 382 */         ref.next.add(value);
/*     */       }
/*     */     }
/*     */     
/*     */     public ReadableObjectId.Referring handleUnresolvedReference(UnresolvedForwardReference reference)
/*     */     {
/* 388 */       CollectionDeserializer.CollectionReferring id = new CollectionDeserializer.CollectionReferring(this, reference, this._elementType);
/* 389 */       this._accumulator.add(id);
/* 390 */       return id;
/*     */     }
/*     */     
/*     */     public void resolveForwardReference(Object id, Object value) throws IOException
/*     */     {
/* 395 */       Iterator<CollectionDeserializer.CollectionReferring> iterator = this._accumulator.iterator();
/*     */       
/*     */ 
/*     */ 
/* 399 */       Collection<Object> previous = this._result;
/* 400 */       while (iterator.hasNext()) {
/* 401 */         CollectionDeserializer.CollectionReferring ref = (CollectionDeserializer.CollectionReferring)iterator.next();
/* 402 */         if (ref.hasId(id)) {
/* 403 */           iterator.remove();
/* 404 */           previous.add(value);
/* 405 */           previous.addAll(ref.next);
/* 406 */           return;
/*     */         }
/* 408 */         previous = ref.next;
/*     */       }
/*     */       
/* 411 */       throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id + "] that wasn't previously seen as unresolved.");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class CollectionReferring
/*     */     extends ReadableObjectId.Referring
/*     */   {
/*     */     private final CollectionDeserializer.CollectionReferringAccumulator _parent;
/*     */     
/*     */ 
/* 423 */     public final List<Object> next = new ArrayList();
/*     */     
/*     */ 
/*     */     CollectionReferring(CollectionDeserializer.CollectionReferringAccumulator parent, UnresolvedForwardReference reference, Class<?> contentType)
/*     */     {
/* 428 */       super(contentType);
/* 429 */       this._parent = parent;
/*     */     }
/*     */     
/*     */     public void handleResolvedForwardReference(Object id, Object value) throws IOException
/*     */     {
/* 434 */       this._parent.resolveForwardReference(id, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\CollectionDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */