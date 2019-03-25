/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.Base64Variant;
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*    */ import java.io.IOException;
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public final class StringDeserializer
/*    */   extends StdScalarDeserializer<String>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 17 */   protected static final int FEATURES_ACCEPT_ARRAYS = DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS.getMask() | DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT.getMask();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 24 */   public static final StringDeserializer instance = new StringDeserializer();
/*    */   
/* 26 */   public StringDeserializer() { super(String.class); }
/*    */   
/*    */   public boolean isCachable()
/*    */   {
/* 30 */     return true;
/*    */   }
/*    */   
/*    */   public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
/*    */   {
/* 35 */     if (p.hasToken(JsonToken.VALUE_STRING)) {
/* 36 */       return p.getText();
/*    */     }
/* 38 */     JsonToken t = p.getCurrentToken();
/*    */     
/* 40 */     if (t == JsonToken.START_ARRAY) {
/* 41 */       return _deserializeFromArray(p, ctxt);
/*    */     }
/*    */     
/* 44 */     if (t == JsonToken.VALUE_EMBEDDED_OBJECT) {
/* 45 */       Object ob = p.getEmbeddedObject();
/* 46 */       if (ob == null) {
/* 47 */         return null;
/*    */       }
/* 49 */       if ((ob instanceof byte[])) {
/* 50 */         return ctxt.getBase64Variant().encode((byte[])ob, false);
/*    */       }
/*    */       
/* 53 */       return ob.toString();
/*    */     }
/*    */     
/* 56 */     String text = p.getValueAsString();
/* 57 */     if (text != null) {
/* 58 */       return text;
/*    */     }
/* 60 */     return (String)ctxt.handleUnexpectedToken(this._valueClass, p);
/*    */   }
/*    */   
/*    */ 
/*    */   public String deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*    */     throws IOException
/*    */   {
/* 67 */     return deserialize(p, ctxt);
/*    */   }
/*    */   
/*    */   protected String _deserializeFromArray(JsonParser p, DeserializationContext ctxt)
/*    */     throws IOException
/*    */   {
/*    */     JsonToken t;
/* 74 */     if (ctxt.hasSomeOfFeatures(FEATURES_ACCEPT_ARRAYS)) {
/* 75 */       JsonToken t = p.nextToken();
/* 76 */       if ((t == JsonToken.END_ARRAY) && 
/* 77 */         (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT))) {
/* 78 */         return (String)getNullValue(ctxt);
/*    */       }
/*    */       
/* 81 */       if (ctxt.isEnabled(DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS)) {
/* 82 */         String parsed = _parseString(p, ctxt);
/* 83 */         if (p.nextToken() != JsonToken.END_ARRAY) {
/* 84 */           handleMissingEndArrayForSingle(p, ctxt);
/*    */         }
/* 86 */         return parsed;
/*    */       }
/*    */     } else {
/* 89 */       t = p.getCurrentToken();
/*    */     }
/* 91 */     return (String)ctxt.handleUnexpectedToken(this._valueClass, t, p, null, new Object[0]);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\StringDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */