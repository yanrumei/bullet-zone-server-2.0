/*    */ package ch.qos.logback.classic.turbo;
/*    */ 
/*    */ import ch.qos.logback.classic.Level;
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
/*    */ public class MDCValueLevelPair
/*    */ {
/*    */   private String value;
/*    */   private Level level;
/*    */   
/*    */   public String getValue()
/*    */   {
/* 29 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String name) {
/* 33 */     this.value = name;
/*    */   }
/*    */   
/*    */   public Level getLevel() {
/* 37 */     return this.level;
/*    */   }
/*    */   
/*    */   public void setLevel(Level level) {
/* 41 */     this.level = level;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\turbo\MDCValueLevelPair.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */