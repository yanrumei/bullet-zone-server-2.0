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
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedWithParams;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public final class StringCollectionDeserializer
/*     */   extends ContainerDeserializerBase<Collection<String>>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JavaType _collectionType;
/*     */   protected final JsonDeserializer<String> _valueDeserializer;
/*     */   protected final ValueInstantiator _valueInstantiator;
/*     */   protected final JsonDeserializer<Object> _delegateDeserializer;
/*     */   protected final Boolean _unwrapSingle;
/*     */   
/*     */   public StringCollectionDeserializer(JavaType collectionType, JsonDeserializer<?> valueDeser, ValueInstantiator valueInstantiator)
/*     */   {
/*  70 */     this(collectionType, valueInstantiator, null, valueDeser, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StringCollectionDeserializer(JavaType collectionType, ValueInstantiator valueInstantiator, JsonDeserializer<?> delegateDeser, JsonDeserializer<?> valueDeser, Boolean unwrapSingle)
/*     */   {
/*  78 */     super(collectionType);
/*  79 */     this._collectionType = collectionType;
/*  80 */     this._valueDeserializer = valueDeser;
/*  81 */     this._valueInstantiator = valueInstantiator;
/*  82 */     this._delegateDeserializer = delegateDeser;
/*  83 */     this._unwrapSingle = unwrapSingle;
/*     */   }
/*     */   
/*     */ 
/*     */   protected StringCollectionDeserializer withResolved(JsonDeserializer<?> delegateDeser, JsonDeserializer<?> valueDeser, Boolean unwrapSingle)
/*     */   {
/*  89 */     if ((this._unwrapSingle == unwrapSingle) && (this._valueDeserializer == valueDeser) && (this._delegateDeserializer == delegateDeser))
/*     */     {
/*  91 */       return this;
/*     */     }
/*  93 */     return new StringCollectionDeserializer(this._collectionType, this._valueInstantiator, delegateDeser, valueDeser, unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isCachable()
/*     */   {
/* 100 */     return (this._valueDeserializer == null) && (this._delegateDeserializer == null);
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
/* 113 */     JsonDeserializer<Object> delegate = null;
/* 114 */     if (this._valueInstantiator != null) {
/* 115 */       AnnotatedWithParams delegateCreator = this._valueInstantiator.getDelegateCreator();
/* 116 */       if (delegateCreator != null) {
/* 117 */         JavaType delegateType = this._valueInstantiator.getDelegateType(ctxt.getConfig());
/* 118 */         delegate = findDeserializer(ctxt, delegateType, property);
/*     */       }
/*     */     }
/* 121 */     JsonDeserializer<?> valueDeser = this._valueDeserializer;
/* 122 */     JavaType valueType = this._collectionType.getContentType();
/* 123 */     if (valueDeser == null)
/*     */     {
/* 125 */       valueDeser = findConvertingContentDeserializer(ctxt, property, valueDeser);
/* 126 */       if (valueDeser == null)
/*     */       {
/* 128 */         valueDeser = ctxt.findContextualValueDeserializer(valueType, property);
/*     */       }
/*     */     } else {
/* 131 */       valueDeser = ctxt.handleSecondaryContextualization(valueDeser, property, valueType);
/*     */     }
/*     */     
/*     */ 
/* 135 */     Boolean unwrapSingle = findFormatFeature(ctxt, property, Collection.class, JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
/*     */     
/* 137 */     if (isDefaultDeserializer(valueDeser)) {
/* 138 */       valueDeser = null;
/*     */     }
/* 140 */     return withResolved(delegate, valueDeser, unwrapSingle);
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
/* 151 */     return this._collectionType.getContentType();
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonDeserializer<Object> getContentDeserializer()
/*     */   {
/* 157 */     JsonDeserializer<?> deser = this._valueDeserializer;
/* 158 */     return deser;
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
/*     */   public Collection<String> deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 172 */     if (this._delegateDeserializer != null) {
/* 173 */       return (Collection)this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(p, ctxt));
/*     */     }
/*     */     
/* 176 */     Collection<String> result = (Collection)this._valueInstantiator.createUsingDefault(ctxt);
/* 177 */     return deserialize(p, ctxt, result);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Collection<String> deserialize(JsonParser p, DeserializationContext ctxt, Collection<String> result)
/*     */     throws IOException
/*     */   {
/* 186 */     if (!p.isExpectedStartArrayToken()) {
/* 187 */       return handleNonArray(p, ctxt, result);
/*     */     }
/*     */     
/* 190 */     if (this._valueDeserializer != null) {
/* 191 */       return deserializeUsingCustom(p, ctxt, result, this._valueDeserializer);
/*     */     }
/*     */     try
/*     */     {
/*     */       for (;;) {
/* 196 */         String value = p.nextTextValue();
/* 197 */         if (value != null) {
/* 198 */           result.add(value);
/*     */         }
/*     */         else {
/* 201 */           JsonToken t = p.getCurrentToken();
/* 202 */           if (t == JsonToken.END_ARRAY) {
/*     */             break;
/*     */           }
/* 205 */           if (t != JsonToken.VALUE_NULL) {
/* 206 */             value = _parseString(p, ctxt);
/*     */           }
/* 208 */           result.add(value);
/*     */         }
/*     */       }
/* 211 */     } catch (Exception e) { throw JsonMappingException.wrapWithPath(e, result, result.size());
/*     */     }
/* 213 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   private Collection<String> deserializeUsingCustom(JsonParser p, DeserializationContext ctxt, Collection<String> result, JsonDeserializer<String> deser)
/*     */     throws IOException
/*     */   {
/*     */     for (;;)
/*     */     {
/*     */       String value;
/*     */       
/*     */       String value;
/*     */       
/* 226 */       if (p.nextTextValue() == null) {
/* 227 */         JsonToken t = p.getCurrentToken();
/* 228 */         if (t == JsonToken.END_ARRAY) {
/*     */           break;
/*     */         }
/*     */         
/* 232 */         value = t == JsonToken.VALUE_NULL ? (String)deser.getNullValue(ctxt) : (String)deser.deserialize(p, ctxt);
/*     */       } else {
/* 234 */         value = (String)deser.deserialize(p, ctxt);
/*     */       }
/* 236 */       result.add(value);
/*     */     }
/* 238 */     return result;
/*     */   }
/*     */   
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/* 244 */     return typeDeserializer.deserializeTypedFromArray(p, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Collection<String> handleNonArray(JsonParser p, DeserializationContext ctxt, Collection<String> result)
/*     */     throws IOException
/*     */   {
/* 256 */     boolean canWrap = (this._unwrapSingle == Boolean.TRUE) || ((this._unwrapSingle == null) && (ctxt.isEnabled(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY)));
/*     */     
/*     */ 
/* 259 */     if (!canWrap) {
/* 260 */       return (Collection)ctxt.handleUnexpectedToken(this._collectionType.getRawClass(), p);
/*     */     }
/*     */     
/* 263 */     JsonDeserializer<String> valueDes = this._valueDeserializer;
/* 264 */     JsonToken t = p.getCurrentToken();
/*     */     
/*     */     String value;
/*     */     String value;
/* 268 */     if (t == JsonToken.VALUE_NULL) {
/* 269 */       value = valueDes == null ? null : (String)valueDes.getNullValue(ctxt);
/*     */     } else {
/* 271 */       value = valueDes == null ? _parseString(p, ctxt) : (String)valueDes.deserialize(p, ctxt);
/*     */     }
/* 273 */     result.add(value);
/* 274 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\StringCollectionDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */