/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*     */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*     */ import com.fasterxml.jackson.databind.node.NullNode;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonNodeDeserializer
/*     */   extends BaseNodeDeserializer<JsonNode>
/*     */ {
/*  23 */   private static final JsonNodeDeserializer instance = new JsonNodeDeserializer();
/*     */   
/*  25 */   protected JsonNodeDeserializer() { super(JsonNode.class); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JsonDeserializer<? extends JsonNode> getDeserializer(Class<?> nodeClass)
/*     */   {
/*  32 */     if (nodeClass == ObjectNode.class) {
/*  33 */       return ObjectDeserializer.getInstance();
/*     */     }
/*  35 */     if (nodeClass == ArrayNode.class) {
/*  36 */       return ArrayDeserializer.getInstance();
/*     */     }
/*     */     
/*  39 */     return instance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode getNullValue(DeserializationContext ctxt)
/*     */   {
/*  50 */     return NullNode.getInstance();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public JsonNode getNullValue()
/*     */   {
/*  56 */     return NullNode.getInstance();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/*  67 */     switch (p.getCurrentTokenId()) {
/*     */     case 1: 
/*  69 */       return deserializeObject(p, ctxt, ctxt.getNodeFactory());
/*     */     case 3: 
/*  71 */       return deserializeArray(p, ctxt, ctxt.getNodeFactory());
/*     */     }
/*  73 */     return deserializeAny(p, ctxt, ctxt.getNodeFactory());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static final class ObjectDeserializer
/*     */     extends BaseNodeDeserializer<ObjectNode>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  88 */     protected static final ObjectDeserializer _instance = new ObjectDeserializer();
/*     */     
/*  90 */     protected ObjectDeserializer() { super(); }
/*     */     
/*  92 */     public static ObjectDeserializer getInstance() { return _instance; }
/*     */     
/*     */     public ObjectNode deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/*  97 */       if ((p.isExpectedStartObjectToken()) || (p.hasToken(JsonToken.FIELD_NAME))) {
/*  98 */         return deserializeObject(p, ctxt, ctxt.getNodeFactory());
/*     */       }
/*     */       
/*     */ 
/* 102 */       if (p.hasToken(JsonToken.END_OBJECT)) {
/* 103 */         return ctxt.getNodeFactory().objectNode();
/*     */       }
/* 105 */       return (ObjectNode)ctxt.handleUnexpectedToken(ObjectNode.class, p);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class ArrayDeserializer
/*     */     extends BaseNodeDeserializer<ArrayNode>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/* 114 */     protected static final ArrayDeserializer _instance = new ArrayDeserializer();
/*     */     
/* 116 */     protected ArrayDeserializer() { super(); }
/*     */     
/* 118 */     public static ArrayDeserializer getInstance() { return _instance; }
/*     */     
/*     */     public ArrayNode deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 123 */       if (p.isExpectedStartArrayToken()) {
/* 124 */         return deserializeArray(p, ctxt, ctxt.getNodeFactory());
/*     */       }
/* 126 */       return (ArrayNode)ctxt.handleUnexpectedToken(ArrayNode.class, p);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\JsonNodeDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */