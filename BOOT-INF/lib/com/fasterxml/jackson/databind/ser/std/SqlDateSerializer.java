/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerationException;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.sql.Date;
/*    */ import java.text.DateFormat;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public class SqlDateSerializer
/*    */   extends DateTimeSerializerBase<Date>
/*    */ {
/*    */   public SqlDateSerializer()
/*    */   {
/* 27 */     this(Boolean.FALSE);
/*    */   }
/*    */   
/*    */   protected SqlDateSerializer(Boolean useTimestamp) {
/* 31 */     super(Date.class, useTimestamp, null);
/*    */   }
/*    */   
/*    */   public SqlDateSerializer withFormat(Boolean timestamp, DateFormat customFormat)
/*    */   {
/* 36 */     return new SqlDateSerializer(timestamp);
/*    */   }
/*    */   
/*    */   protected long _timestamp(Date value)
/*    */   {
/* 41 */     return value == null ? 0L : value.getTime();
/*    */   }
/*    */   
/*    */ 
/*    */   public void serialize(Date value, JsonGenerator gen, SerializerProvider provider)
/*    */     throws IOException, JsonGenerationException
/*    */   {
/* 48 */     if (_asTimestamp(provider)) {
/* 49 */       gen.writeNumber(_timestamp(value));
/*    */     } else {
/* 51 */       gen.writeString(value.toString());
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */   {
/* 59 */     return createSchemaNode("string", true);
/*    */   }
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*    */     throws JsonMappingException
/*    */   {
/* 65 */     _acceptJsonFormatVisitor(visitor, typeHint, this._useTimestamp.booleanValue());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\SqlDateSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */