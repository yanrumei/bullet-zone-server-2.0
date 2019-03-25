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
/*    */ 
/*    */ public class BooleanNode
/*    */   extends ValueNode
/*    */ {
/* 19 */   public static final BooleanNode TRUE = new BooleanNode(true);
/* 20 */   public static final BooleanNode FALSE = new BooleanNode(false);
/*    */   
/*    */   private final boolean _value;
/*    */   
/* 24 */   private BooleanNode(boolean v) { this._value = v; }
/*    */   
/* 26 */   public static BooleanNode getTrue() { return TRUE; }
/* 27 */   public static BooleanNode getFalse() { return FALSE; }
/*    */   
/* 29 */   public static BooleanNode valueOf(boolean b) { return b ? TRUE : FALSE; }
/*    */   
/*    */   public JsonNodeType getNodeType()
/*    */   {
/* 33 */     return JsonNodeType.BOOLEAN;
/*    */   }
/*    */   
/*    */   public JsonToken asToken() {
/* 37 */     return this._value ? JsonToken.VALUE_TRUE : JsonToken.VALUE_FALSE;
/*    */   }
/*    */   
/*    */   public boolean booleanValue()
/*    */   {
/* 42 */     return this._value;
/*    */   }
/*    */   
/*    */   public String asText()
/*    */   {
/* 47 */     return this._value ? "true" : "false";
/*    */   }
/*    */   
/*    */   public boolean asBoolean()
/*    */   {
/* 52 */     return this._value;
/*    */   }
/*    */   
/*    */   public boolean asBoolean(boolean defaultValue)
/*    */   {
/* 57 */     return this._value;
/*    */   }
/*    */   
/*    */   public int asInt(int defaultValue)
/*    */   {
/* 62 */     return this._value ? 1 : 0;
/*    */   }
/*    */   
/*    */   public long asLong(long defaultValue) {
/* 66 */     return this._value ? 1L : 0L;
/*    */   }
/*    */   
/*    */   public double asDouble(double defaultValue) {
/* 70 */     return this._value ? 1.0D : 0.0D;
/*    */   }
/*    */   
/*    */   public final void serialize(JsonGenerator g, SerializerProvider provider) throws IOException
/*    */   {
/* 75 */     g.writeBoolean(this._value);
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 80 */     return this._value ? 3 : 1;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean equals(Object o)
/*    */   {
/* 90 */     if (o == this) return true;
/* 91 */     if (o == null) return false;
/* 92 */     if (!(o instanceof BooleanNode)) {
/* 93 */       return false;
/*    */     }
/* 95 */     return this._value == ((BooleanNode)o)._value;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\node\BooleanNode.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */