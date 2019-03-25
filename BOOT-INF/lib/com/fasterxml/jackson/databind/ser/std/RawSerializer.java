/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RawSerializer<T>
/*    */   extends StdSerializer<T>
/*    */ {
/*    */   public RawSerializer(Class<?> cls)
/*    */   {
/* 25 */     super(cls, false);
/*    */   }
/*    */   
/*    */   public void serialize(T value, JsonGenerator jgen, SerializerProvider provider) throws IOException
/*    */   {
/* 30 */     jgen.writeRawValue(value.toString());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void serializeWithType(T value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*    */     throws IOException
/*    */   {
/* 38 */     typeSer.writeTypePrefixForScalar(value, jgen);
/* 39 */     serialize(value, jgen, provider);
/* 40 */     typeSer.writeTypeSuffixForScalar(value, jgen);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */   {
/* 47 */     return createSchemaNode("string", true);
/*    */   }
/*    */   
/*    */ 
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*    */     throws JsonMappingException
/*    */   {
/* 54 */     visitStringFormat(visitor, typeHint);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\RawSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */