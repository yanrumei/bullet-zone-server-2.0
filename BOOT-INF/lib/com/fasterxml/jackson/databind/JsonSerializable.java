/*    */ package com.fasterxml.jackson.databind;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import java.io.IOException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract interface JsonSerializable
/*    */ {
/*    */   public abstract void serialize(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*    */     throws IOException;
/*    */   
/*    */   public abstract void serializeWithType(JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider, TypeSerializer paramTypeSerializer)
/*    */     throws IOException;
/*    */   
/*    */   public static abstract class Base
/*    */     implements JsonSerializable
/*    */   {
/*    */     public boolean isEmpty(SerializerProvider serializers)
/*    */     {
/* 63 */       return false;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\JsonSerializable.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */