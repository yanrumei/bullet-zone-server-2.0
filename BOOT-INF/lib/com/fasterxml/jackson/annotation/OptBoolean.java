/*    */ package com.fasterxml.jackson.annotation;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum OptBoolean
/*    */ {
/* 23 */   TRUE, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 29 */   FALSE, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 37 */   DEFAULT;
/*    */   
/*    */   private OptBoolean() {}
/* 40 */   public Boolean asBoolean() { if (this == DEFAULT) return null;
/* 41 */     return this == TRUE ? Boolean.TRUE : Boolean.FALSE;
/*    */   }
/*    */   
/*    */   public boolean asPrimitive() {
/* 45 */     return this == TRUE;
/*    */   }
/*    */   
/*    */   public static OptBoolean fromBoolean(Boolean b) {
/* 49 */     if (b == null) {
/* 50 */       return DEFAULT;
/*    */     }
/* 52 */     return b.booleanValue() ? TRUE : FALSE;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-annotations-2.8.0.jar!\com\fasterxml\jackson\annotation\OptBoolean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */