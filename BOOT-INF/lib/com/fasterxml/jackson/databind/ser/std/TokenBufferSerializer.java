/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public class TokenBufferSerializer
/*    */   extends StdSerializer<TokenBuffer>
/*    */ {
/*    */   public TokenBufferSerializer()
/*    */   {
/* 27 */     super(TokenBuffer.class);
/*    */   }
/*    */   
/*    */   public void serialize(TokenBuffer value, JsonGenerator jgen, SerializerProvider provider)
/*    */     throws IOException
/*    */   {
/* 33 */     value.serialize(jgen);
/*    */   }
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
/*    */   public final void serializeWithType(TokenBuffer value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*    */     throws IOException
/*    */   {
/* 50 */     typeSer.writeTypePrefixForScalar(value, jgen);
/* 51 */     serialize(value, jgen, provider);
/* 52 */     typeSer.writeTypeSuffixForScalar(value, jgen);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */   {
/* 60 */     return createSchemaNode("any", true);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*    */     throws JsonMappingException
/*    */   {
/* 69 */     visitor.expectAnyFormat(typeHint);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\TokenBufferSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */