/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class StdScalarDeserializer<T>
/*    */   extends StdDeserializer<T>
/*    */ {
/* 18 */   protected static final int FEATURES_ACCEPT_ARRAYS = DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS.getMask() | DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT.getMask();
/*    */   
/*    */ 
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */ 
/* 24 */   protected StdScalarDeserializer(Class<?> vc) { super(vc); }
/* 25 */   protected StdScalarDeserializer(JavaType valueType) { super(valueType); }
/*    */   
/*    */   protected StdScalarDeserializer(StdScalarDeserializer<?> src) {
/* 28 */     super(src);
/*    */   }
/*    */   
/*    */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
/* 32 */     return typeDeserializer.deserializeTypedFromScalar(p, ctxt);
/*    */   }
/*    */   
/*    */   protected T _deserializeFromArray(JsonParser p, DeserializationContext ctxt) throws IOException
/*    */   {
/*    */     JsonToken t;
/* 38 */     if (ctxt.hasSomeOfFeatures(FEATURES_ACCEPT_ARRAYS)) {
/* 39 */       JsonToken t = p.nextToken();
/* 40 */       if ((t == JsonToken.END_ARRAY) && 
/* 41 */         (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT))) {
/* 42 */         return (T)getNullValue(ctxt);
/*    */       }
/*    */       
/* 45 */       if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/* 46 */         T parsed = deserialize(p, ctxt);
/* 47 */         if (p.nextToken() != JsonToken.END_ARRAY) {
/* 48 */           handleMissingEndArrayForSingle(p, ctxt);
/*    */         }
/* 50 */         return parsed;
/*    */       }
/*    */     } else {
/* 53 */       t = p.getCurrentToken();
/*    */     }
/*    */     
/* 56 */     T result = ctxt.handleUnexpectedToken(this._valueClass, t, p, null, new Object[0]);
/* 57 */     return result;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\StdScalarDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */