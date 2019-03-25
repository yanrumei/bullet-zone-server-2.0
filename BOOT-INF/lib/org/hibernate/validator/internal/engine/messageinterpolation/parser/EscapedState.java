/*    */ package org.hibernate.validator.internal.engine.messageinterpolation.parser;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EscapedState
/*    */   implements ParserState
/*    */ {
/*    */   ParserState previousState;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public EscapedState(ParserState previousState)
/*    */   {
/* 16 */     this.previousState = previousState;
/*    */   }
/*    */   
/*    */   public void terminate(TokenCollector tokenCollector) throws MessageDescriptorFormatException
/*    */   {
/* 21 */     tokenCollector.terminateToken();
/*    */   }
/*    */   
/*    */   public void handleNonMetaCharacter(char character, TokenCollector tokenCollector)
/*    */     throws MessageDescriptorFormatException
/*    */   {
/* 27 */     handleEscapedCharacter(character, tokenCollector);
/*    */   }
/*    */   
/*    */   public void handleBeginTerm(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException
/*    */   {
/* 32 */     handleEscapedCharacter(character, tokenCollector);
/*    */   }
/*    */   
/*    */   public void handleEndTerm(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException
/*    */   {
/* 37 */     handleEscapedCharacter(character, tokenCollector);
/*    */   }
/*    */   
/*    */   public void handleEscapeCharacter(char character, TokenCollector tokenCollector)
/*    */     throws MessageDescriptorFormatException
/*    */   {
/* 43 */     handleEscapedCharacter(character, tokenCollector);
/*    */   }
/*    */   
/*    */   public void handleELDesignator(char character, TokenCollector tokenCollector)
/*    */     throws MessageDescriptorFormatException
/*    */   {
/* 49 */     handleEscapedCharacter(character, tokenCollector);
/*    */   }
/*    */   
/*    */   private void handleEscapedCharacter(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException
/*    */   {
/* 54 */     tokenCollector.appendToToken(character);
/* 55 */     tokenCollector.transitionState(this.previousState);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\parser\EscapedState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */