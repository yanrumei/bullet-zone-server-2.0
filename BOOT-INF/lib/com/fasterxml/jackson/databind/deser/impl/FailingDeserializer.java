/*    */ package com.fasterxml.jackson.databind.deser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonParser;
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JsonMappingException;
/*    */ import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class FailingDeserializer
/*    */   extends StdDeserializer<Object>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected final String _message;
/*    */   
/*    */   public FailingDeserializer(String m)
/*    */   {
/* 21 */     super(Object.class);
/* 22 */     this._message = m;
/*    */   }
/*    */   
/*    */   public Object deserialize(JsonParser jp, DeserializationContext ctxt) throws JsonMappingException
/*    */   {
/* 27 */     ctxt.reportMappingException(this._message, new Object[0]);
/* 28 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\impl\FailingDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */