/*    */ package org.apache.coyote.http2;
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
/*    */ public enum Setting
/*    */ {
/* 20 */   HEADER_TABLE_SIZE(1), 
/* 21 */   ENABLE_PUSH(2), 
/* 22 */   MAX_CONCURRENT_STREAMS(3), 
/* 23 */   INITIAL_WINDOW_SIZE(4), 
/* 24 */   MAX_FRAME_SIZE(5), 
/* 25 */   MAX_HEADER_LIST_SIZE(6), 
/* 26 */   UNKNOWN(Integer.MAX_VALUE);
/*    */   
/*    */   private final int id;
/*    */   
/*    */   private Setting(int id) {
/* 31 */     this.id = id;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 35 */     return this.id;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 40 */     return Integer.toString(this.id);
/*    */   }
/*    */   
/*    */   public static Setting valueOf(int i) {
/* 44 */     switch (i) {
/*    */     case 1: 
/* 46 */       return HEADER_TABLE_SIZE;
/*    */     
/*    */     case 2: 
/* 49 */       return ENABLE_PUSH;
/*    */     
/*    */     case 3: 
/* 52 */       return MAX_CONCURRENT_STREAMS;
/*    */     
/*    */     case 4: 
/* 55 */       return INITIAL_WINDOW_SIZE;
/*    */     
/*    */     case 5: 
/* 58 */       return MAX_FRAME_SIZE;
/*    */     
/*    */     case 6: 
/* 61 */       return MAX_HEADER_LIST_SIZE;
/*    */     }
/*    */     
/* 64 */     return UNKNOWN;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http2\Setting.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */