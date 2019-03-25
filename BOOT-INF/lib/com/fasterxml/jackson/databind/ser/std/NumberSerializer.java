/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.JsonNode;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*    */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Type;
/*    */ import java.math.BigDecimal;
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @JacksonStdImpl
/*    */ public class NumberSerializer
/*    */   extends StdScalarSerializer<Number>
/*    */ {
/* 27 */   public static final NumberSerializer instance = new NumberSerializer(Number.class);
/*    */   
/*    */ 
/*    */   protected final boolean _isInt;
/*    */   
/*    */ 
/*    */   public NumberSerializer(Class<? extends Number> rawType)
/*    */   {
/* 35 */     super(rawType, false);
/*    */     
/* 37 */     this._isInt = (rawType == BigInteger.class);
/*    */   }
/*    */   
/*    */ 
/*    */   public void serialize(Number value, JsonGenerator g, SerializerProvider provider)
/*    */     throws IOException
/*    */   {
/* 44 */     if ((value instanceof BigDecimal)) {
/* 45 */       g.writeNumber((BigDecimal)value);
/* 46 */     } else if ((value instanceof BigInteger)) {
/* 47 */       g.writeNumber((BigInteger)value);
/*    */ 
/*    */ 
/*    */     }
/* 51 */     else if ((value instanceof Long)) {
/* 52 */       g.writeNumber(value.longValue());
/* 53 */     } else if ((value instanceof Double)) {
/* 54 */       g.writeNumber(value.doubleValue());
/* 55 */     } else if ((value instanceof Float)) {
/* 56 */       g.writeNumber(value.floatValue());
/* 57 */     } else if (((value instanceof Integer)) || ((value instanceof Byte)) || ((value instanceof Short))) {
/* 58 */       g.writeNumber(value.intValue());
/*    */     }
/*    */     else {
/* 61 */       g.writeNumber(value.toString());
/*    */     }
/*    */   }
/*    */   
/*    */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*    */   {
/* 67 */     return createSchemaNode(this._isInt ? "integer" : "number", true);
/*    */   }
/*    */   
/*    */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*    */     throws JsonMappingException
/*    */   {
/* 73 */     if (this._isInt) {
/* 74 */       visitIntFormat(visitor, typeHint, JsonParser.NumberType.BIG_INTEGER);
/*    */     } else {
/* 76 */       Class<?> h = handledType();
/* 77 */       if (h == BigDecimal.class) {
/* 78 */         visitFloatFormat(visitor, typeHint, JsonParser.NumberType.BIG_DECIMAL);
/*    */       }
/*    */       else {
/* 81 */         visitor.expectNumberFormat(typeHint);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\NumberSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */