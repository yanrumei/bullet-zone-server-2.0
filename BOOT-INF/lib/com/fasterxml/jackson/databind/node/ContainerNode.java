/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.util.RawValue;
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
/*     */ public abstract class ContainerNode<T extends ContainerNode<T>>
/*     */   extends BaseJsonNode
/*     */   implements JsonNodeCreator
/*     */ {
/*     */   protected final JsonNodeFactory _nodeFactory;
/*     */   
/*     */   protected ContainerNode(JsonNodeFactory nc)
/*     */   {
/*  26 */     this._nodeFactory = nc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract JsonToken asToken();
/*     */   
/*     */ 
/*     */   public String asText()
/*     */   {
/*  36 */     return "";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int size();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JsonNode get(int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JsonNode get(String paramString);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ArrayNode arrayNode()
/*     */   {
/*  65 */     return this._nodeFactory.arrayNode();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ArrayNode arrayNode(int capacity)
/*     */   {
/*  73 */     return this._nodeFactory.arrayNode(capacity);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final ObjectNode objectNode()
/*     */   {
/*  80 */     return this._nodeFactory.objectNode();
/*     */   }
/*     */   
/*  83 */   public final NullNode nullNode() { return this._nodeFactory.nullNode(); }
/*     */   
/*     */   public final BooleanNode booleanNode(boolean v) {
/*  86 */     return this._nodeFactory.booleanNode(v);
/*     */   }
/*     */   
/*  89 */   public final NumericNode numberNode(byte v) { return this._nodeFactory.numberNode(v); }
/*     */   
/*  91 */   public final NumericNode numberNode(short v) { return this._nodeFactory.numberNode(v); }
/*     */   
/*  93 */   public final NumericNode numberNode(int v) { return this._nodeFactory.numberNode(v); }
/*     */   
/*     */   public final NumericNode numberNode(long v) {
/*  96 */     return this._nodeFactory.numberNode(v);
/*     */   }
/*     */   
/*     */   public final NumericNode numberNode(BigInteger v)
/*     */   {
/* 101 */     return this._nodeFactory.numberNode(v);
/*     */   }
/*     */   
/* 104 */   public final NumericNode numberNode(float v) { return this._nodeFactory.numberNode(v); }
/*     */   
/* 106 */   public final NumericNode numberNode(double v) { return this._nodeFactory.numberNode(v); }
/*     */   
/* 108 */   public final NumericNode numberNode(BigDecimal v) { return this._nodeFactory.numberNode(v); }
/*     */   
/*     */ 
/*     */ 
/* 112 */   public final ValueNode numberNode(Byte v) { return this._nodeFactory.numberNode(v); }
/*     */   
/* 114 */   public final ValueNode numberNode(Short v) { return this._nodeFactory.numberNode(v); }
/*     */   
/* 116 */   public final ValueNode numberNode(Integer v) { return this._nodeFactory.numberNode(v); }
/*     */   
/* 118 */   public final ValueNode numberNode(Long v) { return this._nodeFactory.numberNode(v); }
/*     */   
/*     */ 
/* 121 */   public final ValueNode numberNode(Float v) { return this._nodeFactory.numberNode(v); }
/*     */   
/* 123 */   public final ValueNode numberNode(Double v) { return this._nodeFactory.numberNode(v); }
/*     */   
/*     */   public final TextNode textNode(String text) {
/* 126 */     return this._nodeFactory.textNode(text);
/*     */   }
/*     */   
/* 129 */   public final BinaryNode binaryNode(byte[] data) { return this._nodeFactory.binaryNode(data); }
/*     */   
/* 131 */   public final BinaryNode binaryNode(byte[] data, int offset, int length) { return this._nodeFactory.binaryNode(data, offset, length); }
/*     */   
/*     */   public final ValueNode pojoNode(Object pojo) {
/* 134 */     return this._nodeFactory.pojoNode(pojo);
/*     */   }
/*     */   
/* 137 */   public final ValueNode rawValueNode(RawValue value) { return this._nodeFactory.rawValueNode(value); }
/*     */   
/*     */   public abstract T removeAll();
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\node\ContainerNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */