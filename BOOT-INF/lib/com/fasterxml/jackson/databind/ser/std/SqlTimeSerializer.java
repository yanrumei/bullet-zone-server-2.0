/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import java.lang.reflect.Type;
/*    */ import java.sql.Time;
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public class SqlTimeSerializer extends StdScalarSerializer<Time>
/*    */ {
/*    */   public SqlTimeSerializer()
/*    */   {
/* 17 */     super(Time.class);
/*    */   }
/*    */   
/*    */   public void serialize(Time value, JsonGenerator g, SerializerProvider provider) throws java.io.IOException
/*    */   {
/* 22 */     g.writeString(value.toString());
/*    */   }
/*    */   
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */   {
/* 27 */     return createSchemaNode("string", true);
/*    */   }
/*    */   
/*    */ 
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*    */     throws com.fasterxml.jackson.databind.JsonMappingException
/*    */   {
/* 34 */     visitStringFormat(visitor, typeHint, com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat.DATE_TIME);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\SqlTimeSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */