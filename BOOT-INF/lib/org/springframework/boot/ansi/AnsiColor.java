/*    */ package org.springframework.boot.ansi;
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
/*    */ 
/*    */ public enum AnsiColor
/*    */   implements AnsiElement
/*    */ {
/* 28 */   DEFAULT("39"), 
/*    */   
/* 30 */   BLACK("30"), 
/*    */   
/* 32 */   RED("31"), 
/*    */   
/* 34 */   GREEN("32"), 
/*    */   
/* 36 */   YELLOW("33"), 
/*    */   
/* 38 */   BLUE("34"), 
/*    */   
/* 40 */   MAGENTA("35"), 
/*    */   
/* 42 */   CYAN("36"), 
/*    */   
/* 44 */   WHITE("37"), 
/*    */   
/* 46 */   BRIGHT_BLACK("90"), 
/*    */   
/* 48 */   BRIGHT_RED("91"), 
/*    */   
/* 50 */   BRIGHT_GREEN("92"), 
/*    */   
/* 52 */   BRIGHT_YELLOW("93"), 
/*    */   
/* 54 */   BRIGHT_BLUE("94"), 
/*    */   
/* 56 */   BRIGHT_MAGENTA("95"), 
/*    */   
/* 58 */   BRIGHT_CYAN("96"), 
/*    */   
/* 60 */   BRIGHT_WHITE("97");
/*    */   
/*    */   private final String code;
/*    */   
/*    */   private AnsiColor(String code) {
/* 65 */     this.code = code;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 70 */     return this.code;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\ansi\AnsiColor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */