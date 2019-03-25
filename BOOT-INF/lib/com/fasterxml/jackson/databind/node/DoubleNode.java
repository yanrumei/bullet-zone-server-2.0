/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.io.NumberOutput;
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
/*     */ public class DoubleNode
/*     */   extends NumericNode
/*     */ {
/*     */   protected final double _value;
/*     */   
/*  27 */   public DoubleNode(double v) { this._value = v; }
/*     */   
/*  29 */   public static DoubleNode valueOf(double v) { return new DoubleNode(v); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonToken asToken()
/*     */   {
/*  37 */     return JsonToken.VALUE_NUMBER_FLOAT;
/*     */   }
/*     */   
/*  40 */   public JsonParser.NumberType numberType() { return JsonParser.NumberType.DOUBLE; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isFloatingPointNumber()
/*     */   {
/*  49 */     return true;
/*     */   }
/*     */   
/*  52 */   public boolean isDouble() { return true; }
/*     */   
/*     */   public boolean canConvertToInt() {
/*  55 */     return (this._value >= -2.147483648E9D) && (this._value <= 2.147483647E9D);
/*     */   }
/*     */   
/*  58 */   public boolean canConvertToLong() { return (this._value >= -9.223372036854776E18D) && (this._value <= 9.223372036854776E18D); }
/*     */   
/*     */ 
/*     */   public Number numberValue()
/*     */   {
/*  63 */     return Double.valueOf(this._value);
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
/*  76 */   public float floatValue() { return (float)this._value; }
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
/*     */   public String asText()
/*     */   {
/*  91 */     return NumberOutput.toString(this._value);
/*     */   }
/*     */   
/*     */ 
/*     */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  98 */     jg.writeNumber(this._value);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 104 */     if (o == this) return true;
/* 105 */     if (o == null) return false;
/* 106 */     if ((o instanceof DoubleNode))
/*     */     {
/*     */ 
/* 109 */       double otherValue = ((DoubleNode)o)._value;
/* 110 */       return Double.compare(this._value, otherValue) == 0;
/*     */     }
/* 112 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 119 */     long l = Double.doubleToLongBits(this._value);
/* 120 */     return (int)l ^ (int)(l >> 32);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\node\DoubleNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */