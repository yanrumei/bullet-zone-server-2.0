/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.ArrayBlockingQueue;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ArrayBlockingQueueDeserializer
/*     */   extends CollectionDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public ArrayBlockingQueueDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, ValueInstantiator valueInstantiator)
/*     */   {
/*  33 */     super(collectionType, valueDeser, valueTypeDeser, valueInstantiator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ArrayBlockingQueueDeserializer(JavaType collectionType, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser, ValueInstantiator valueInstantiator, JsonDeserializer<Object> delegateDeser, Boolean unwrapSingle)
/*     */   {
/*  44 */     super(collectionType, valueDeser, valueTypeDeser, valueInstantiator, delegateDeser, unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ArrayBlockingQueueDeserializer(ArrayBlockingQueueDeserializer src)
/*     */   {
/*  53 */     super(src);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ArrayBlockingQueueDeserializer withResolved(JsonDeserializer<?> dd, JsonDeserializer<?> vd, TypeDeserializer vtd, Boolean unwrapSingle)
/*     */   {
/*  64 */     if ((dd == this._delegateDeserializer) && (vd == this._valueDeserializer) && (vtd == this._valueTypeDeserializer) && (this._unwrapSingle == unwrapSingle))
/*     */     {
/*  66 */       return this;
/*     */     }
/*  68 */     return new ArrayBlockingQueueDeserializer(this._collectionType, vd, vtd, this._valueInstantiator, dd, unwrapSingle);
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
/*     */   public Collection<Object> deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  84 */     if (this._delegateDeserializer != null) {
/*  85 */       return (Collection)this._valueInstantiator.createUsingDelegate(ctxt, this._delegateDeserializer.deserialize(jp, ctxt));
/*     */     }
/*     */     
/*  88 */     if (jp.getCurrentToken() == JsonToken.VALUE_STRING) {
/*  89 */       String str = jp.getText();
/*  90 */       if (str.length() == 0) {
/*  91 */         return (Collection)this._valueInstantiator.createFromString(ctxt, str);
/*     */       }
/*     */     }
/*  94 */     return deserialize(jp, ctxt, null);
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection<Object> deserialize(JsonParser jp, DeserializationContext ctxt, Collection<Object> result0)
/*     */     throws IOException
/*     */   {
/* 101 */     if (!jp.isExpectedStartArrayToken()) {
/* 102 */       return handleNonArray(jp, ctxt, new ArrayBlockingQueue(1));
/*     */     }
/* 104 */     ArrayList<Object> tmp = new ArrayList();
/*     */     
/* 106 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/*     */     
/* 108 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*     */     try {
/*     */       JsonToken t;
/* 111 */       while ((t = jp.nextToken()) != JsonToken.END_ARRAY) {
/*     */         Object value;
/*     */         Object value;
/* 114 */         if (t == JsonToken.VALUE_NULL) {
/* 115 */           value = valueDes.getNullValue(ctxt); } else { Object value;
/* 116 */           if (typeDeser == null) {
/* 117 */             value = valueDes.deserialize(jp, ctxt);
/*     */           } else
/* 119 */             value = valueDes.deserializeWithType(jp, ctxt, typeDeser);
/*     */         }
/* 121 */         tmp.add(value);
/*     */       }
/*     */     } catch (Exception e) {
/* 124 */       throw JsonMappingException.wrapWithPath(e, tmp, tmp.size());
/*     */     }
/* 126 */     if (result0 != null) {
/* 127 */       result0.addAll(tmp);
/* 128 */       return result0;
/*     */     }
/* 130 */     return new ArrayBlockingQueue(tmp.size(), false, tmp);
/*     */   }
/*     */   
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/* 136 */     return typeDeserializer.deserializeTypedFromArray(jp, ctxt);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\ArrayBlockingQueueDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */