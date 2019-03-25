/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
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
/*     */ public abstract class DelegatingDeserializer
/*     */   extends StdDeserializer<Object>
/*     */   implements ContextualDeserializer, ResolvableDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JsonDeserializer<?> _delegatee;
/*     */   
/*     */   public DelegatingDeserializer(JsonDeserializer<?> delegatee)
/*     */   {
/*  37 */     super(_figureType(delegatee));
/*  38 */     this._delegatee = delegatee;
/*     */   }
/*     */   
/*     */   protected abstract JsonDeserializer<?> newDelegatingInstance(JsonDeserializer<?> paramJsonDeserializer);
/*     */   
/*     */   private static Class<?> _figureType(JsonDeserializer<?> deser)
/*     */   {
/*  45 */     Class<?> cls = deser.handledType();
/*  46 */     if (cls != null) {
/*  47 */       return cls;
/*     */     }
/*  49 */     return Object.class;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resolve(DeserializationContext ctxt)
/*     */     throws JsonMappingException
/*     */   {
/*  60 */     if ((this._delegatee instanceof ResolvableDeserializer)) {
/*  61 */       ((ResolvableDeserializer)this._delegatee).resolve(ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  70 */     JavaType vt = ctxt.constructType(this._delegatee.handledType());
/*  71 */     JsonDeserializer<?> del = ctxt.handleSecondaryContextualization(this._delegatee, property, vt);
/*     */     
/*  73 */     if (del == this._delegatee) {
/*  74 */       return this;
/*     */     }
/*  76 */     return newDelegatingInstance(del);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected JsonDeserializer<?> _createContextual(DeserializationContext ctxt, BeanProperty property, JsonDeserializer<?> newDelegatee)
/*     */   {
/*  86 */     if (newDelegatee == this._delegatee) {
/*  87 */       return this;
/*     */     }
/*  89 */     return newDelegatingInstance(newDelegatee);
/*     */   }
/*     */   
/*     */ 
/*     */   public SettableBeanProperty findBackReference(String logicalName)
/*     */   {
/*  95 */     return this._delegatee.findBackReference(logicalName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 108 */     return this._delegatee.deserialize(jp, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserialize(JsonParser jp, DeserializationContext ctxt, Object intoValue)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 117 */     return this._delegatee.deserialize(jp, ctxt, intoValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 125 */     return this._delegatee.deserializeWithType(jp, ctxt, typeDeserializer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> replaceDelegatee(JsonDeserializer<?> delegatee)
/*     */   {
/* 137 */     if (delegatee == this._delegatee) {
/* 138 */       return this;
/*     */     }
/* 140 */     return newDelegatingInstance(delegatee);
/*     */   }
/*     */   
/*     */   public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException
/*     */   {
/* 145 */     return this._delegatee.getNullValue(ctxt);
/*     */   }
/*     */   
/*     */   public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException
/*     */   {
/* 150 */     return this._delegatee.getEmptyValue(ctxt);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Object getNullValue() {
/* 155 */     return this._delegatee.getNullValue();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Object getEmptyValue() {
/* 160 */     return this._delegatee.getEmptyValue();
/*     */   }
/*     */   
/*     */   public Collection<Object> getKnownPropertyNames() {
/* 164 */     return this._delegatee.getKnownPropertyNames();
/*     */   }
/*     */   
/* 167 */   public boolean isCachable() { return this._delegatee.isCachable(); }
/*     */   
/*     */   public ObjectIdReader getObjectIdReader() {
/* 170 */     return this._delegatee.getObjectIdReader();
/*     */   }
/*     */   
/*     */   public JsonDeserializer<?> getDelegatee() {
/* 174 */     return this._delegatee;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\DelegatingDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */