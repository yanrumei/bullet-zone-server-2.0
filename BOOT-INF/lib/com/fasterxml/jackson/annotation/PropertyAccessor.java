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
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum PropertyAccessor
/*    */ {
/* 26 */   GETTER, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 31 */   SETTER, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 37 */   CREATOR, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 45 */   FIELD, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 53 */   IS_GETTER, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 58 */   NONE, 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 63 */   ALL;
/*    */   
/*    */   private PropertyAccessor() {}
/*    */   
/*    */   public boolean creatorEnabled()
/*    */   {
/* 69 */     return (this == CREATOR) || (this == ALL);
/*    */   }
/*    */   
/*    */   public boolean getterEnabled() {
/* 73 */     return (this == GETTER) || (this == ALL);
/*    */   }
/*    */   
/*    */   public boolean isGetterEnabled() {
/* 77 */     return (this == IS_GETTER) || (this == ALL);
/*    */   }
/*    */   
/*    */   public boolean setterEnabled() {
/* 81 */     return (this == SETTER) || (this == ALL);
/*    */   }
/*    */   
/*    */   public boolean fieldEnabled() {
/* 85 */     return (this == FIELD) || (this == ALL);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-annotations-2.8.0.jar!\com\fasterxml\jackson\annotation\PropertyAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */