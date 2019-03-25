/*    */ package org.hibernate.validator.internal.engine.messageinterpolation.parser;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class TokenIterator
/*    */ {
/*    */   private final List<Token> tokenList;
/*    */   private int currentPosition;
/*    */   private Token currentToken;
/*    */   private boolean allInterpolationTermsProcessed;
/*    */   private boolean currentTokenAvailable;
/*    */   
/*    */   public TokenIterator(List<Token> tokens)
/*    */   {
/* 26 */     this.tokenList = new ArrayList(tokens);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean hasMoreInterpolationTerms()
/*    */     throws MessageDescriptorFormatException
/*    */   {
/* 39 */     while (this.currentPosition < this.tokenList.size()) {
/* 40 */       this.currentToken = ((Token)this.tokenList.get(this.currentPosition));
/* 41 */       this.currentPosition += 1;
/* 42 */       if (this.currentToken.isParameter()) {
/* 43 */         this.currentTokenAvailable = true;
/* 44 */         return true;
/*    */       }
/*    */     }
/* 47 */     this.allInterpolationTermsProcessed = true;
/* 48 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String nextInterpolationTerm()
/*    */   {
/* 55 */     if (!this.currentTokenAvailable) {
/* 56 */       throw new IllegalStateException("Trying to call #nextInterpolationTerm without calling #hasMoreInterpolationTerms");
/*    */     }
/*    */     
/*    */ 
/* 60 */     this.currentTokenAvailable = false;
/* 61 */     return this.currentToken.getTokenValue();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void replaceCurrentInterpolationTerm(String replacement)
/*    */   {
/* 70 */     Token token = new Token(replacement);
/* 71 */     token.terminate();
/* 72 */     this.tokenList.set(this.currentPosition - 1, token);
/*    */   }
/*    */   
/*    */   public String getInterpolatedMessage() {
/* 76 */     if (!this.allInterpolationTermsProcessed) {
/* 77 */       throw new IllegalStateException("Not all interpolation terms have been processed yet.");
/*    */     }
/* 79 */     StringBuilder messageBuilder = new StringBuilder();
/* 80 */     for (Token token : this.tokenList) {
/* 81 */       messageBuilder.append(token.getTokenValue());
/*    */     }
/*    */     
/* 84 */     return messageBuilder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\parser\TokenIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */