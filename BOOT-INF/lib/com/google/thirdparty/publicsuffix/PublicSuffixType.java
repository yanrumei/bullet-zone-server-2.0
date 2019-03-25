/*    */ package com.google.thirdparty.publicsuffix;
/*    */ 
/*    */ import com.google.common.annotations.GwtCompatible;
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
/*    */ @GwtCompatible
/*    */  enum PublicSuffixType
/*    */ {
/* 26 */   PRIVATE(':', ','), 
/*    */   
/* 28 */   ICANN('!', '?');
/*    */   
/*    */ 
/*    */   private final char innerNodeCode;
/*    */   
/*    */   private final char leafNodeCode;
/*    */   
/*    */   private PublicSuffixType(char innerNodeCode, char leafNodeCode)
/*    */   {
/* 37 */     this.innerNodeCode = innerNodeCode;
/* 38 */     this.leafNodeCode = leafNodeCode;
/*    */   }
/*    */   
/*    */   char getLeafNodeCode() {
/* 42 */     return this.leafNodeCode;
/*    */   }
/*    */   
/*    */   char getInnerNodeCode() {
/* 46 */     return this.innerNodeCode;
/*    */   }
/*    */   
/*    */   static PublicSuffixType fromCode(char code)
/*    */   {
/* 51 */     for (PublicSuffixType value : ) {
/* 52 */       if ((value.getInnerNodeCode() == code) || (value.getLeafNodeCode() == code)) {
/* 53 */         return value;
/*    */       }
/*    */     }
/* 56 */     throw new IllegalArgumentException("No enum corresponding to given code: " + code);
/*    */   }
/*    */   
/*    */   static PublicSuffixType fromIsPrivate(boolean isPrivate) {
/* 60 */     return isPrivate ? PRIVATE : ICANN;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\thirdparty\publicsuffix\PublicSuffixType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */