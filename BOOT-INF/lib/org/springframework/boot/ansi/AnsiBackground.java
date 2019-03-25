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
/*    */ public enum AnsiBackground
/*    */   implements AnsiElement
/*    */ {
/* 28 */   DEFAULT("49"), 
/*    */   
/* 30 */   BLACK("40"), 
/*    */   
/* 32 */   RED("41"), 
/*    */   
/* 34 */   GREEN("42"), 
/*    */   
/* 36 */   YELLOW("43"), 
/*    */   
/* 38 */   BLUE("44"), 
/*    */   
/* 40 */   MAGENTA("45"), 
/*    */   
/* 42 */   CYAN("46"), 
/*    */   
/* 44 */   WHITE("47"), 
/*    */   
/* 46 */   BRIGHT_BLACK("100"), 
/*    */   
/* 48 */   BRIGHT_RED("101"), 
/*    */   
/* 50 */   BRIGHT_GREEN("102"), 
/*    */   
/* 52 */   BRIGHT_YELLOW("103"), 
/*    */   
/* 54 */   BRIGHT_BLUE("104"), 
/*    */   
/* 56 */   BRIGHT_MAGENTA("105"), 
/*    */   
/* 58 */   BRIGHT_CYAN("106"), 
/*    */   
/* 60 */   BRIGHT_WHITE("107");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private AnsiBackground(String code) {
/* 65 */     this.code = code;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 70 */     return this.code;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\ansi\AnsiBackground.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */