/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonPointer;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ValueNode
/*     */   extends BaseJsonNode
/*     */ {
/*     */   protected JsonNode _at(JsonPointer ptr)
/*     */   {
/*  25 */     return MissingNode.getInstance();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends JsonNode> T deepCopy()
/*     */   {
/*  34 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract JsonToken asToken();
/*     */   
/*     */   public void serializeWithType(JsonGenerator jg, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  43 */     typeSer.writeTypePrefixForScalar(this, jg);
/*  44 */     serialize(jg, provider);
/*  45 */     typeSer.writeTypeSuffixForScalar(this, jg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/*  55 */     return asText();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final JsonNode get(int index)
/*     */   {
/*  64 */     return null;
/*     */   }
/*     */   
/*  67 */   public final JsonNode path(int index) { return MissingNode.getInstance(); }
/*     */   
/*     */   public final boolean has(int index) {
/*  70 */     return false;
/*     */   }
/*     */   
/*  73 */   public final boolean hasNonNull(int index) { return false; }
/*     */   
/*     */   public final JsonNode get(String fieldName) {
/*  76 */     return null;
/*     */   }
/*     */   
/*  79 */   public final JsonNode path(String fieldName) { return MissingNode.getInstance(); }
/*     */   
/*     */   public final boolean has(String fieldName) {
/*  82 */     return false;
/*     */   }
/*     */   
/*  85 */   public final boolean hasNonNull(String fieldName) { return false; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final JsonNode findValue(String fieldName)
/*     */   {
/*  95 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public final ObjectNode findParent(String fieldName)
/*     */   {
/* 101 */     return null;
/*     */   }
/*     */   
/*     */   public final List<JsonNode> findValues(String fieldName, List<JsonNode> foundSoFar)
/*     */   {
/* 106 */     return foundSoFar;
/*     */   }
/*     */   
/*     */   public final List<String> findValuesAsText(String fieldName, List<String> foundSoFar)
/*     */   {
/* 111 */     return foundSoFar;
/*     */   }
/*     */   
/*     */   public final List<JsonNode> findParents(String fieldName, List<JsonNode> foundSoFar)
/*     */   {
/* 116 */     return foundSoFar;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\node\ValueNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */