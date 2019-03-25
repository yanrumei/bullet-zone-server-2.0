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
/*     */ public class LongNode
/*     */   extends NumericNode
/*     */ {
/*     */   protected final long _value;
/*     */   
/*  26 */   public LongNode(long v) { this._value = v; }
/*     */   
/*  28 */   public static LongNode valueOf(long l) { return new LongNode(l); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonToken asToken()
/*     */   {
/*  36 */     return JsonToken.VALUE_NUMBER_INT;
/*     */   }
/*     */   
/*  39 */   public JsonParser.NumberType numberType() { return JsonParser.NumberType.LONG; }
/*     */   
/*     */   public boolean isIntegralNumber()
/*     */   {
/*  43 */     return true;
/*     */   }
/*     */   
/*  46 */   public boolean isLong() { return true; }
/*     */   
/*     */ 
/*  49 */   public boolean canConvertToInt() { return (this._value >= -2147483648L) && (this._value <= 2147483647L); }
/*     */   
/*  51 */   public boolean canConvertToLong() { return true; }
/*     */   
/*     */   public Number numberValue()
/*     */   {
/*  55 */     return Long.valueOf(this._value);
/*     */   }
/*     */   
/*     */   public short shortValue() {
/*  59 */     return (short)(int)this._value;
/*     */   }
/*     */   
/*  62 */   public int intValue() { return (int)this._value; }
/*     */   
/*     */   public long longValue() {
/*  65 */     return this._value;
/*     */   }
/*     */   
/*  68 */   public float floatValue() { return (float)this._value; }
/*     */   
/*     */   public double doubleValue() {
/*  71 */     return this._value;
/*     */   }
/*     */   
/*  74 */   public BigDecimal decimalValue() { return BigDecimal.valueOf(this._value); }
/*     */   
/*     */   public BigInteger bigIntegerValue() {
/*  77 */     return BigInteger.valueOf(this._value);
/*     */   }
/*     */   
/*     */   public String asText() {
/*  81 */     return NumberOutput.toString(this._value);
/*     */   }
/*     */   
/*     */   public boolean asBoolean(boolean defaultValue)
/*     */   {
/*  86 */     return this._value != 0L;
/*     */   }
/*     */   
/*     */ 
/*     */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  93 */     jg.writeNumber(this._value);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/*  99 */     if (o == this) return true;
/* 100 */     if (o == null) return false;
/* 101 */     if ((o instanceof LongNode)) {
/* 102 */       return ((LongNode)o)._value == this._value;
/*     */     }
/* 104 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 109 */     return (int)this._value ^ (int)(this._value >> 32);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\node\LongNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */