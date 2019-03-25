/*    */ package com.fasterxml.jackson.databind;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RuntimeJsonMappingException
/*    */   extends RuntimeException
/*    */ {
/*    */   public RuntimeJsonMappingException(JsonMappingException cause)
/*    */   {
/* 11 */     super(cause);
/*    */   }
/*    */   
/*    */   public RuntimeJsonMappingException(String message) {
/* 15 */     super(message);
/*    */   }
/*    */   
/*    */   public RuntimeJsonMappingException(String message, JsonMappingException cause) {
/* 19 */     super(message, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\RuntimeJsonMappingException.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */