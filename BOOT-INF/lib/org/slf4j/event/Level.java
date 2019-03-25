/*    */ package org.slf4j.event;
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
/*    */ public enum Level
/*    */ {
/* 16 */   ERROR(40, "ERROR"),  WARN(30, "WARN"),  INFO(20, "INFO"),  DEBUG(10, "DEBUG"),  TRACE(0, "TRACE");
/*    */   
/*    */   private int levelInt;
/*    */   private String levelStr;
/*    */   
/*    */   private Level(int i, String s) {
/* 22 */     this.levelInt = i;
/* 23 */     this.levelStr = s;
/*    */   }
/*    */   
/*    */   public int toInt() {
/* 27 */     return this.levelInt;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 34 */     return this.levelStr;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\slf4j-api-1.7.25.jar!\org\slf4j\event\Level.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */