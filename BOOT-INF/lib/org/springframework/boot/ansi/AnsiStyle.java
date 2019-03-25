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
/*    */ public enum AnsiStyle
/*    */   implements AnsiElement
/*    */ {
/* 27 */   NORMAL("0"), 
/*    */   
/* 29 */   BOLD("1"), 
/*    */   
/* 31 */   FAINT("2"), 
/*    */   
/* 33 */   ITALIC("3"), 
/*    */   
/* 35 */   UNDERLINE("4");
/*    */   
/*    */   private final String code;
/*    */   
/*    */   private AnsiStyle(String code) {
/* 40 */     this.code = code;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 45 */     return this.code;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\ansi\AnsiStyle.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */