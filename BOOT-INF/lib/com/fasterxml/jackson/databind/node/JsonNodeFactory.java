/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.util.RawValue;
/*     */ import java.io.Serializable;
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
/*     */ 
/*     */ public class JsonNodeFactory
/*     */   implements Serializable, JsonNodeCreator
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final boolean _cfgBigDecimalExact;
/*  24 */   private static final JsonNodeFactory decimalsNormalized = new JsonNodeFactory(false);
/*     */   
/*  26 */   private static final JsonNodeFactory decimalsAsIs = new JsonNodeFactory(true);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  34 */   public static final JsonNodeFactory instance = decimalsNormalized;
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
/*     */ 
/*     */ 
/*     */   public JsonNodeFactory(boolean bigDecimalExact)
/*     */   {
/*  63 */     this._cfgBigDecimalExact = bigDecimalExact;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonNodeFactory()
/*     */   {
/*  74 */     this(false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JsonNodeFactory withExactBigDecimals(boolean bigDecimalExact)
/*     */   {
/*  86 */     return bigDecimalExact ? decimalsAsIs : decimalsNormalized;
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
/*     */   public BooleanNode booleanNode(boolean v)
/*     */   {
/* 101 */     return v ? BooleanNode.getTrue() : BooleanNode.getFalse();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NullNode nullNode()
/*     */   {
/* 109 */     return NullNode.getInstance();
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
/*     */   public NumericNode numberNode(byte v)
/*     */   {
/* 122 */     return IntNode.valueOf(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueNode numberNode(Byte value)
/*     */   {
/* 132 */     return value == null ? nullNode() : IntNode.valueOf(value.intValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NumericNode numberNode(short v)
/*     */   {
/* 140 */     return ShortNode.valueOf(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueNode numberNode(Short value)
/*     */   {
/* 150 */     return value == null ? nullNode() : ShortNode.valueOf(value.shortValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NumericNode numberNode(int v)
/*     */   {
/* 158 */     return IntNode.valueOf(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueNode numberNode(Integer value)
/*     */   {
/* 168 */     return value == null ? nullNode() : IntNode.valueOf(value.intValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NumericNode numberNode(long v)
/*     */   {
/* 177 */     return LongNode.valueOf(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueNode numberNode(Long value)
/*     */   {
/* 187 */     if (value == null) {
/* 188 */       return nullNode();
/*     */     }
/* 190 */     return LongNode.valueOf(value.longValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NumericNode numberNode(BigInteger v)
/*     */   {
/* 198 */     return BigIntegerNode.valueOf(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public NumericNode numberNode(float v)
/*     */   {
/* 205 */     return FloatNode.valueOf(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueNode numberNode(Float value)
/*     */   {
/* 215 */     return value == null ? nullNode() : FloatNode.valueOf(value.floatValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public NumericNode numberNode(double v)
/*     */   {
/* 223 */     return DoubleNode.valueOf(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueNode numberNode(Double value)
/*     */   {
/* 233 */     return value == null ? nullNode() : DoubleNode.valueOf(value.doubleValue());
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NumericNode numberNode(BigDecimal v)
/*     */   {
/* 253 */     if (this._cfgBigDecimalExact) {
/* 254 */       return DecimalNode.valueOf(v);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 265 */     return v.compareTo(BigDecimal.ZERO) == 0 ? DecimalNode.ZERO : DecimalNode.valueOf(v.stripTrailingZeros());
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
/*     */   public TextNode textNode(String text)
/*     */   {
/* 280 */     return TextNode.valueOf(text);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BinaryNode binaryNode(byte[] data)
/*     */   {
/* 288 */     return BinaryNode.valueOf(data);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BinaryNode binaryNode(byte[] data, int offset, int length)
/*     */   {
/* 297 */     return BinaryNode.valueOf(data, offset, length);
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
/*     */   public ArrayNode arrayNode()
/*     */   {
/* 310 */     return new ArrayNode(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ArrayNode arrayNode(int capacity)
/*     */   {
/* 318 */     return new ArrayNode(this, capacity);
/*     */   }
/*     */   
/*     */ 
/*     */   public ObjectNode objectNode()
/*     */   {
/* 324 */     return new ObjectNode(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValueNode pojoNode(Object pojo)
/*     */   {
/* 333 */     return new POJONode(pojo);
/*     */   }
/*     */   
/*     */   public ValueNode rawValueNode(RawValue value) {
/* 337 */     return new POJONode(value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _inIntRange(long l)
/*     */   {
/* 348 */     int i = (int)l;
/* 349 */     long l2 = i;
/* 350 */     return l2 == l;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\node\JsonNodeFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */