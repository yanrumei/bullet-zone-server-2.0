/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.SerializationFeature;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.util.Date;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Deprecated
/*    */ public class StdKeySerializer
/*    */   extends StdSerializer<Object>
/*    */ {
/*    */   public StdKeySerializer()
/*    */   {
/* 22 */     super(Object.class);
/*    */   }
/*    */   
/*    */   public void serialize(Object value, JsonGenerator g, SerializerProvider provider) throws IOException
/*    */   {
/* 27 */     Class<?> cls = value.getClass();
/*    */     String str;
/* 29 */     String str; if (cls == String.class) {
/* 30 */       str = (String)value; } else { String str;
/* 31 */       if (cls.isEnum())
/*    */       {
/*    */         String str;
/* 34 */         if (provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)) {
/* 35 */           str = value.toString();
/*    */         } else {
/* 37 */           Enum<?> en = (Enum)value;
/* 38 */           String str; if (provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)) {
/* 39 */             str = String.valueOf(en.ordinal());
/*    */           } else
/* 41 */             str = en.name();
/*    */         }
/*    */       } else {
/* 44 */         if ((value instanceof Date)) {
/* 45 */           provider.defaultSerializeDateKey((Date)value, g); return; }
/*    */         String str;
/* 47 */         if (cls == Class.class) {
/* 48 */           str = ((Class)value).getName();
/*    */         } else
/* 50 */           str = value.toString();
/*    */       } }
/* 52 */     g.writeFieldName(str);
/*    */   }
/*    */   
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint) throws JsonMappingException
/*    */   {
/* 57 */     return createSchemaNode("string");
/*    */   }
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException
/*    */   {
/* 62 */     visitStringFormat(visitor, typeHint);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\StdKeySerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */