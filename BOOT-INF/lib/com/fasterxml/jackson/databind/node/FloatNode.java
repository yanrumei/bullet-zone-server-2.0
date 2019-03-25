/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FloatNode
/*     */   extends NumericNode
/*     */ {
/*     */   protected final float _value;
/*     */   
/*  26 */   public FloatNode(float v) { this._value = v; }
/*     */   
/*  28 */   public static FloatNode valueOf(float v) { return new FloatNode(v); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonToken asToken()
/*     */   {
/*  36 */     return JsonToken.VALUE_NUMBER_FLOAT;
/*     */   }
/*     */   
/*  39 */   public JsonParser.NumberType numberType() { return JsonParser.NumberType.FLOAT; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFloatingPointNumber()
/*     */   {
/*  48 */     return true;
/*     */   }
/*     */   
/*  51 */   public boolean isFloat() { return true; }
/*     */   
/*     */   public boolean canConvertToInt() {
/*  54 */     return (this._value >= -2.14748365E9F) && (this._value <= 2.14748365E9F);
/*     */   }
/*     */   
/*     */   public boolean canConvertToLong() {
/*  58 */     return (this._value >= -9.223372E18F) && (this._value <= 9.223372E18F);
/*     */   }
/*     */   
/*     */   public Number numberValue()
/*     */   {
/*  63 */     return Float.valueOf(this._value);
/*     */   }
/*     */   
/*     */   public short shortValue() {
/*  67 */     return (short)(int)this._value;
/*     */   }
/*     */   
/*  70 */   public int intValue() { return (int)this._value; }
/*     */   
/*     */   public long longValue() {
/*  73 */     return this._value;
/*     */   }
/*     */   
/*  76 */   public float floatValue() { return this._value; }
/*     */   
/*     */   public double doubleValue() {
/*  79 */     return this._value;
/*     */   }
/*     */   
/*  82 */   public BigDecimal decimalValue() { return BigDecimal.valueOf(this._value); }
/*     */   
/*     */   public BigInteger bigIntegerValue()
/*     */   {
/*  86 */     return decimalValue().toBigInteger();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String asText()
/*     */   {
/*  94 */     return Float.toString(this._value);
/*     */   }
/*     */   
/*     */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 100 */     jg.writeNumber(this._value);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 106 */     if (o == this) return true;
/* 107 */     if (o == null) return false;
/* 108 */     if ((o instanceof FloatNode))
/*     */     {
/*     */ 
/* 111 */       float otherValue = ((FloatNode)o)._value;
/* 112 */       return Float.compare(this._value, otherValue) == 0;
/*     */     }
/* 114 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 119 */     return Float.floatToIntBits(this._value);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\node\FloatNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */