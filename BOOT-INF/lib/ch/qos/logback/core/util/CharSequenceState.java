/*    */ package ch.qos.logback.core.util;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class CharSequenceState
/*    */ {
/*    */   final char c;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   int occurrences;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public CharSequenceState(char c)
/*    */   {
/* 25 */     this.c = c;
/* 26 */     this.occurrences = 1;
/*    */   }
/*    */   
/*    */   void incrementOccurrences() {
/* 30 */     this.occurrences += 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\cor\\util\CharSequenceState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */