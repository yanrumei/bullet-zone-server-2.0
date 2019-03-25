/*    */ package com.fasterxml.jackson.databind.node;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.core.JsonToken;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class NullNode
/*    */   extends ValueNode
/*    */ {
/* 18 */   public static final NullNode instance = new NullNode();
/*    */   
/*    */   public static NullNode getInstance()
/*    */   {
/* 22 */     return instance;
/*    */   }
/*    */   
/*    */   public JsonNodeType getNodeType() {
/* 26 */     return JsonNodeType.NULL;
/*    */   }
/*    */   
/* 29 */   public JsonToken asToken() { return JsonToken.VALUE_NULL; }
/*    */   
/* 31 */   public String asText(String defaultValue) { return defaultValue; }
/* 32 */   public String asText() { return "null"; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public final void serialize(JsonGenerator g, SerializerProvider provider)
/*    */     throws IOException
/*    */   {
/* 47 */     provider.defaultSerializeNull(g);
/*    */   }
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 52 */     return o == this;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 57 */     return JsonNodeType.NULL.ordinal();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\node\NullNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */