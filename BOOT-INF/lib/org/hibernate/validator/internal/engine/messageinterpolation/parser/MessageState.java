/*    */ package org.hibernate.validator.internal.engine.messageinterpolation.parser;
/*    */ 
/*    */ import org.hibernate.validator.internal.engine.messageinterpolation.InterpolationTermType;
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
/*    */ public class MessageState
/*    */   implements ParserState
/*    */ {
/* 17 */   private static final Log log = ;
/*    */   
/*    */   public void terminate(TokenCollector tokenCollector) throws MessageDescriptorFormatException
/*    */   {
/* 21 */     tokenCollector.terminateToken();
/*    */   }
/*    */   
/*    */   public void handleNonMetaCharacter(char character, TokenCollector tokenCollector)
/*    */     throws MessageDescriptorFormatException
/*    */   {
/* 27 */     tokenCollector.appendToToken(character);
/*    */   }
/*    */   
/*    */   public void handleBeginTerm(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException
/*    */   {
/* 32 */     tokenCollector.terminateToken();
/*    */     
/* 34 */     tokenCollector.appendToToken(character);
/* 35 */     if (tokenCollector.getInterpolationType().equals(InterpolationTermType.PARAMETER)) {
/* 36 */       tokenCollector.makeParameterToken();
/*    */     }
/* 38 */     tokenCollector.transitionState(new InterpolationTermState());
/*    */   }
/*    */   
/*    */   public void handleEndTerm(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException
/*    */   {
/* 43 */     throw log.getNonTerminatedParameterException(tokenCollector
/* 44 */       .getOriginalMessageDescriptor(), character);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void handleEscapeCharacter(char character, TokenCollector tokenCollector)
/*    */     throws MessageDescriptorFormatException
/*    */   {
/* 52 */     tokenCollector.appendToToken(character);
/*    */     
/* 54 */     tokenCollector.transitionState(new EscapedState(this));
/*    */   }
/*    */   
/*    */   public void handleELDesignator(char character, TokenCollector tokenCollector)
/*    */     throws MessageDescriptorFormatException
/*    */   {
/* 60 */     if (tokenCollector.getInterpolationType().equals(InterpolationTermType.PARAMETER)) {
/* 61 */       handleNonMetaCharacter(character, tokenCollector);
/*    */     }
/*    */     else {
/* 64 */       tokenCollector.transitionState(new ELState());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\parser\MessageState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */