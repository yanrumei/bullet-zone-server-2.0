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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public final class BooleanSerializer
/*    */   extends NonTypedScalarSerializerBase<Boolean>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected final boolean _forPrimitive;
/*    */   
/*    */   public BooleanSerializer(boolean forPrimitive)
/*    */   {
/* 35 */     super(Boolean.class);
/* 36 */     this._forPrimitive = forPrimitive;
/*    */   }
/*    */   
/*    */   public void serialize(Boolean value, JsonGenerator jgen, SerializerProvider provider) throws IOException
/*    */   {
/* 41 */     jgen.writeBoolean(value.booleanValue());
/*    */   }
/*    */   
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */   {
/* 46 */     return createSchemaNode("boolean", !this._forPrimitive);
/*    */   }
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*    */     throws JsonMappingException
/*    */   {
/* 52 */     if (visitor != null) {
/* 53 */       visitor.expectBooleanFormat(typeHint);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\BooleanSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */