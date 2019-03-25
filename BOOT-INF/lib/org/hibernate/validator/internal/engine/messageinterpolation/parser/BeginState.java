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
/*    */ public class BeginState
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
/* 28 */     tokenCollector.transitionState(new MessageState());
/*    */   }
/*    */   
/*    */   public void handleBeginTerm(char character, TokenCollector tokenCollector)
/*    */     throws MessageDescriptorFormatException
/*    */   {
/* 34 */     tokenCollector.terminateToken();
/*    */     
/* 36 */     tokenCollector.appendToToken(character);
/* 37 */     if (tokenCollector.getInterpolationType().equals(InterpolationTermType.PARAMETER)) {
/* 38 */       tokenCollector.makeParameterToken();
/*    */     }
/* 40 */     tokenCollector.transitionState(new InterpolationTermState());
/*    */   }
/*    */   
/*    */   public void handleEndTerm(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException
/*    */   {
/* 45 */     throw log.getNonTerminatedParameterException(tokenCollector.getOriginalMessageDescriptor(), character);
/*    */   }
/*    */   
/*    */   public void handleEscapeCharacter(char character, TokenCollector tokenCollector)
/*    */     throws MessageDescriptorFormatException
/*    */   {
/* 51 */     tokenCollector.appendToToken(character);
/* 52 */     tokenCollector.transitionState(new EscapedState(this));
/*    */   }
/*    */   
/*    */   public void handleELDesignator(char character, TokenCollector tokenCollector)
/*    */     throws MessageDescriptorFormatException
/*    */   {
/* 58 */     if (tokenCollector.getInterpolationType().equals(InterpolationTermType.PARAMETER)) {
/* 59 */       handleNonMetaCharacter(character, tokenCollector);
/*    */     }
/*    */     else {
/* 62 */       ParserState state = new ELState();
/* 63 */       tokenCollector.transitionState(state);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\parser\BeginState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */