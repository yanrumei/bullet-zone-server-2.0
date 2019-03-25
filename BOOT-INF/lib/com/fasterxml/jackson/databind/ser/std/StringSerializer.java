/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
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
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public final class StringSerializer
/*    */   extends NonTypedScalarSerializerBase<Object>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public StringSerializer()
/*    */   {
/* 29 */     super(String.class, false);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public boolean isEmpty(Object value)
/*    */   {
/* 37 */     String str = (String)value;
/* 38 */     return (str == null) || (str.length() == 0);
/*    */   }
/*    */   
/*    */   public boolean isEmpty(SerializerProvider prov, Object value)
/*    */   {
/* 43 */     String str = (String)value;
/* 44 */     return (str == null) || (str.length() == 0);
/*    */   }
/*    */   
/*    */   public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException
/*    */   {
/* 49 */     gen.writeString((String)value);
/*    */   }
/*    */   
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */   {
/* 54 */     return createSchemaNode("string", true);
/*    */   }
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException
/*    */   {
/* 59 */     visitStringFormat(visitor, typeHint);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\StringSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */