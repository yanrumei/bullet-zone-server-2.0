/*    */ package org.hibernate.validator.internal.engine.messageinterpolation.parser;
/*    */ 
/*    */ import org.hibernate.validator.internal.util.logging.Log;
/*    */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class InterpolationTermState
/*    */   implements ParserState
/*    */ {
/* 16 */   private static final Log log = ;
/*    */   
/*    */   public void terminate(TokenCollector tokenCollector) throws MessageDescriptorFormatException
/*    */   {
/* 20 */     throw log.getNonTerminatedParameterException(tokenCollector
/* 21 */       .getOriginalMessageDescriptor(), '{');
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void handleNonMetaCharacter(char character, TokenCollector tokenCollector)
/*    */     throws MessageDescriptorFormatException
/*    */   {
/* 29 */     tokenCollector.appendToToken(character);
/*    */   }
/*    */   
/*    */   public void handleBeginTerm(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException
/*    */   {
/* 34 */     throw log.getNestedParameterException(tokenCollector.getOriginalMessageDescriptor());
/*    */   }
/*    */   
/*    */   public void handleEndTerm(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException
/*    */   {
/* 39 */     tokenCollector.appendToToken(character);
/* 40 */     tokenCollector.terminateToken();
/* 41 */     BeginState beginState = new BeginState();
/* 42 */     tokenCollector.transitionState(beginState);
/*    */   }
/*    */   
/*    */   public void handleEscapeCharacter(char character, TokenCollector tokenCollector)
/*    */     throws MessageDescriptorFormatException
/*    */   {
/* 48 */     tokenCollector.appendToToken(character);
/* 49 */     ParserState state = new EscapedState(this);
/* 50 */     tokenCollector.transitionState(state);
/*    */   }
/*    */   
/*    */   public void handleELDesignator(char character, TokenCollector tokenCollector)
/*    */     throws MessageDescriptorFormatException
/*    */   {
/* 56 */     tokenCollector.appendToToken(character);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\parser\InterpolationTermState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */