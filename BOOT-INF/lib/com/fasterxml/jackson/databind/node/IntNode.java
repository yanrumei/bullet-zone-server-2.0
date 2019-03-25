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
/*     */ public class IntNode
/*     */   extends NumericNode
/*     */ {
/*     */   static final int MIN_CANONICAL = -1;
/*     */   static final int MAX_CANONICAL = 10;
/*     */   private static final IntNode[] CANONICALS;
/*     */   protected final int _value;
/*     */   
/*     */   static
/*     */   {
/*  25 */     int count = 12;
/*  26 */     CANONICALS = new IntNode[count];
/*  27 */     for (int i = 0; i < count; i++) {
/*  28 */       CANONICALS[i] = new IntNode(-1 + i);
/*     */     }
/*     */   }
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
/*     */ 
/*  43 */   public IntNode(int v) { this._value = v; }
/*     */   
/*     */   public static IntNode valueOf(int i) {
/*  46 */     if ((i > 10) || (i < -1)) return new IntNode(i);
/*  47 */     return CANONICALS[(i - -1)];
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonToken asToken()
/*     */   {
/*  56 */     return JsonToken.VALUE_NUMBER_INT;
/*     */   }
/*     */   
/*  59 */   public JsonParser.NumberType numberType() { return JsonParser.NumberType.INT; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isIntegralNumber()
/*     */   {
/*  68 */     return true;
/*     */   }
/*     */   
/*  71 */   public boolean isInt() { return true; }
/*     */   
/*  73 */   public boolean canConvertToInt() { return true; }
/*  74 */   public boolean canConvertToLong() { return true; }
/*     */   
/*     */   public Number numberValue()
/*     */   {
/*  78 */     return Integer.valueOf(this._value);
/*     */   }
/*     */   
/*     */   public short shortValue() {
/*  82 */     return (short)this._value;
/*     */   }
/*     */   
/*  85 */   public int intValue() { return this._value; }
/*     */   
/*     */   public long longValue() {
/*  88 */     return this._value;
/*     */   }
/*     */   
/*  91 */   public float floatValue() { return this._value; }
/*     */   
/*     */   public double doubleValue() {
/*  94 */     return this._value;
/*     */   }
/*     */   
/*     */   public BigDecimal decimalValue() {
/*  98 */     return BigDecimal.valueOf(this._value);
/*     */   }
/*     */   
/* 101 */   public BigInteger bigIntegerValue() { return BigInteger.valueOf(this._value); }
/*     */   
/*     */   public String asText()
/*     */   {
/* 105 */     return NumberOutput.toString(this._value);
/*     */   }
/*     */   
/*     */   public boolean asBoolean(boolean defaultValue)
/*     */   {
/* 110 */     return this._value != 0;
/*     */   }
/*     */   
/*     */ 
/*     */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 117 */     jg.writeNumber(this._value);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object o)
/*     */   {
/* 123 */     if (o == this) return true;
/* 124 */     if (o == null) return false;
/* 125 */     if ((o instanceof IntNode)) {
/* 126 */       return ((IntNode)o)._value == this._value;
/*     */     }
/* 128 */     return false;
/*     */   }
/*     */   
/*     */   public int hashCode() {
/* 132 */     return this._value;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\node\IntNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */