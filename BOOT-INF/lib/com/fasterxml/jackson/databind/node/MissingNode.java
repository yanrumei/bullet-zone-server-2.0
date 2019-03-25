/*     */ package com.fasterxml.jackson.databind.node;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import java.io.IOException;
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
/*     */ public final class MissingNode
/*     */   extends ValueNode
/*     */ {
/*  25 */   private static final MissingNode instance = new MissingNode();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  32 */   public <T extends JsonNode> T deepCopy() { return this; }
/*     */   
/*  34 */   public static MissingNode getInstance() { return instance; }
/*     */   
/*     */ 
/*     */   public JsonNodeType getNodeType()
/*     */   {
/*  39 */     return JsonNodeType.MISSING;
/*     */   }
/*     */   
/*  42 */   public JsonToken asToken() { return JsonToken.NOT_AVAILABLE; }
/*     */   
/*  44 */   public String asText() { return ""; }
/*     */   
/*  46 */   public String asText(String defaultValue) { return defaultValue; }
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
/*     */   public final void serialize(JsonGenerator jg, SerializerProvider provider)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  67 */     jg.writeNull();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void serializeWithType(JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/*  75 */     g.writeNull();
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
/*     */   public boolean equals(Object o)
/*     */   {
/*  89 */     return o == this;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/*  95 */     return "";
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 100 */     return JsonNodeType.MISSING.ordinal();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\node\MissingNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */