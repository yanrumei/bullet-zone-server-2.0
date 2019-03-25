/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ObjectIdValueProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final ObjectIdReader _objectIdReader;
/*     */   
/*     */   public ObjectIdValueProperty(ObjectIdReader objectIdReader, PropertyMetadata metadata)
/*     */   {
/*  27 */     super(objectIdReader.propertyName, objectIdReader.getIdType(), metadata, objectIdReader.getDeserializer());
/*     */     
/*  29 */     this._objectIdReader = objectIdReader;
/*     */   }
/*     */   
/*     */   protected ObjectIdValueProperty(ObjectIdValueProperty src, JsonDeserializer<?> deser)
/*     */   {
/*  34 */     super(src, deser);
/*  35 */     this._objectIdReader = src._objectIdReader;
/*     */   }
/*     */   
/*     */   protected ObjectIdValueProperty(ObjectIdValueProperty src, PropertyName newName) {
/*  39 */     super(src, newName);
/*  40 */     this._objectIdReader = src._objectIdReader;
/*     */   }
/*     */   
/*     */   public ObjectIdValueProperty withName(PropertyName newName)
/*     */   {
/*  45 */     return new ObjectIdValueProperty(this, newName);
/*     */   }
/*     */   
/*     */   public ObjectIdValueProperty withValueDeserializer(JsonDeserializer<?> deser)
/*     */   {
/*  50 */     if (this._valueDeserializer == deser) {
/*  51 */       return this;
/*     */     }
/*  53 */     return new ObjectIdValueProperty(this, deser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/*  60 */     return null;
/*     */   }
/*     */   
/*  63 */   public AnnotatedMember getMember() { return null; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
/*     */     throws IOException
/*     */   {
/*  75 */     deserializeSetAndReturn(p, ctxt, instance);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance)
/*     */     throws IOException
/*     */   {
/*  88 */     if (p.hasToken(JsonToken.VALUE_NULL)) {
/*  89 */       return null;
/*     */     }
/*  91 */     Object id = this._valueDeserializer.deserialize(p, ctxt);
/*  92 */     ReadableObjectId roid = ctxt.findObjectId(id, this._objectIdReader.generator, this._objectIdReader.resolver);
/*  93 */     roid.bindItem(instance);
/*     */     
/*  95 */     SettableBeanProperty idProp = this._objectIdReader.idProperty;
/*  96 */     if (idProp != null) {
/*  97 */       return idProp.setAndReturn(instance, id);
/*     */     }
/*  99 */     return instance;
/*     */   }
/*     */   
/*     */   public void set(Object instance, Object value) throws IOException
/*     */   {
/* 104 */     setAndReturn(instance, value);
/*     */   }
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value)
/*     */     throws IOException
/*     */   {
/* 110 */     SettableBeanProperty idProp = this._objectIdReader.idProperty;
/* 111 */     if (idProp == null) {
/* 112 */       throw new UnsupportedOperationException("Should not call set() on ObjectIdProperty that has no SettableBeanProperty");
/*     */     }
/*     */     
/* 115 */     return idProp.setAndReturn(instance, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\impl\ObjectIdValueProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */