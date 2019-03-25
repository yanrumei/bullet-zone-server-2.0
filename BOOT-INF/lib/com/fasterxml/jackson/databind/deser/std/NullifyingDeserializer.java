/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NullifyingDeserializer
/*    */   extends StdDeserializer<Object>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/* 20 */   public static final NullifyingDeserializer instance = new NullifyingDeserializer();
/*    */   
/* 22 */   public NullifyingDeserializer() { super(Object.class); }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object deserialize(JsonParser p, DeserializationContext ctxt)
/*    */     throws IOException
/*    */   {
/* 35 */     if (p.hasToken(JsonToken.FIELD_NAME)) {
/*    */       for (;;) {
/* 37 */         JsonToken t = p.nextToken();
/* 38 */         if ((t == null) || (t == JsonToken.END_OBJECT)) {
/*    */           break;
/*    */         }
/* 41 */         p.skipChildren();
/*    */       }
/*    */     }
/* 44 */     p.skipChildren();
/*    */     
/* 46 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*    */     throws IOException
/*    */   {
/* 55 */     switch (p.getCurrentTokenId()) {
/*    */     case 1: 
/*    */     case 3: 
/*    */     case 5: 
/* 59 */       return typeDeserializer.deserializeTypedFromAny(p, ctxt);
/*    */     }
/* 61 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\NullifyingDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */