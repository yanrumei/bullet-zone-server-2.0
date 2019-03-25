/*    */ package com.fasterxml.jackson.databind.deser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*    */ import java.io.IOException;
/*    */ import java.io.Serializable;
/*    */ import java.util.Collection;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class TypeWrappedDeserializer
/*    */   extends JsonDeserializer<Object>
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected final TypeDeserializer _typeDeserializer;
/*    */   protected final JsonDeserializer<Object> _deserializer;
/*    */   
/*    */   public TypeWrappedDeserializer(TypeDeserializer typeDeser, JsonDeserializer<?> deser)
/*    */   {
/* 31 */     this._typeDeserializer = typeDeser;
/* 32 */     this._deserializer = deser;
/*    */   }
/*    */   
/*    */   public Class<?> handledType()
/*    */   {
/* 37 */     return this._deserializer.handledType();
/*    */   }
/*    */   
/*    */   public JsonDeserializer<?> getDelegatee()
/*    */   {
/* 42 */     return this._deserializer.getDelegatee();
/*    */   }
/*    */   
/*    */   public Collection<Object> getKnownPropertyNames()
/*    */   {
/* 47 */     return this._deserializer.getKnownPropertyNames();
/*    */   }
/*    */   
/*    */   public Object getNullValue(DeserializationContext ctxt) throws JsonMappingException
/*    */   {
/* 52 */     return this._deserializer.getNullValue(ctxt);
/*    */   }
/*    */   
/*    */   public Object getEmptyValue(DeserializationContext ctxt) throws JsonMappingException
/*    */   {
/* 57 */     return this._deserializer.getEmptyValue(ctxt);
/*    */   }
/*    */   
/*    */   public Object deserialize(JsonParser jp, DeserializationContext ctxt)
/*    */     throws IOException
/*    */   {
/* 63 */     return this._deserializer.deserializeWithType(jp, ctxt, this._typeDeserializer);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*    */     throws IOException
/*    */   {
/* 71 */     throw new IllegalStateException("Type-wrapped deserializer's deserializeWithType should never get called");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object deserialize(JsonParser jp, DeserializationContext ctxt, Object intoValue)
/*    */     throws IOException
/*    */   {
/* 81 */     return this._deserializer.deserialize(jp, ctxt, intoValue);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\impl\TypeWrappedDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */