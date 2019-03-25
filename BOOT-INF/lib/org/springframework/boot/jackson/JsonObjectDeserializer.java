/*     */ package org.springframework.boot.jackson;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.ObjectCodec;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.node.NullNode;
/*     */ import java.io.IOException;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ public abstract class JsonObjectDeserializer<T>
/*     */   extends JsonDeserializer<T>
/*     */ {
/*     */   public final T deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  50 */       ObjectCodec codec = jp.getCodec();
/*  51 */       JsonNode tree = (JsonNode)codec.readTree(jp);
/*  52 */       return (T)deserializeObject(jp, ctxt, codec, tree);
/*     */     }
/*     */     catch (Exception ex) {
/*  55 */       if ((ex instanceof IOException)) {
/*  56 */         throw ((IOException)ex);
/*     */       }
/*  58 */       throw new JsonMappingException(jp, "Object deserialize error", ex);
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
/*     */ 
/*     */   protected abstract T deserializeObject(JsonParser paramJsonParser, DeserializationContext paramDeserializationContext, ObjectCodec paramObjectCodec, JsonNode paramJsonNode)
/*     */     throws IOException;
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
/*     */   protected final <D> D nullSafeValue(JsonNode jsonNode, Class<D> type)
/*     */   {
/*  90 */     Assert.notNull(type, "Type must not be null");
/*  91 */     if (jsonNode == null) {
/*  92 */       return null;
/*     */     }
/*  94 */     if (type == String.class) {
/*  95 */       return jsonNode.textValue();
/*     */     }
/*  97 */     if (type == Boolean.class) {
/*  98 */       return Boolean.valueOf(jsonNode.booleanValue());
/*     */     }
/* 100 */     if (type == Long.class) {
/* 101 */       return Long.valueOf(jsonNode.longValue());
/*     */     }
/* 103 */     if (type == Integer.class) {
/* 104 */       return Integer.valueOf(jsonNode.intValue());
/*     */     }
/* 106 */     if (type == Short.class) {
/* 107 */       return Short.valueOf(jsonNode.shortValue());
/*     */     }
/* 109 */     if (type == Double.class) {
/* 110 */       return Double.valueOf(jsonNode.doubleValue());
/*     */     }
/* 112 */     if (type == Float.class) {
/* 113 */       return Float.valueOf(jsonNode.floatValue());
/*     */     }
/* 115 */     if (type == BigDecimal.class) {
/* 116 */       return jsonNode.decimalValue();
/*     */     }
/* 118 */     if (type == BigInteger.class) {
/* 119 */       return jsonNode.bigIntegerValue();
/*     */     }
/* 121 */     throw new IllegalArgumentException("Unsupported value type " + type.getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonNode getRequiredNode(JsonNode tree, String fieldName)
/*     */   {
/* 131 */     Assert.notNull(tree, "Tree must not be null");
/* 132 */     JsonNode node = tree.get(fieldName);
/* 133 */     Assert.state((node != null) && (!(node instanceof NullNode)), "Missing JSON field '" + fieldName + "'");
/*     */     
/* 135 */     return node;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jackson\JsonObjectDeserializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */