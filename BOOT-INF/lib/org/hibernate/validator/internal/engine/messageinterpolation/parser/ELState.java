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
/*    */ public class ELState
/*    */   implements ParserState
/*    */ {
/* 16 */   private static final Log log = ;
/*    */   
/*    */   public void terminate(TokenCollector tokenCollector) throws MessageDescriptorFormatException
/*    */   {
/* 20 */     tokenCollector.appendToToken('$');
/* 21 */     tokenCollector.terminateToken();
/*    */   }
/*    */   
/*    */   public void handleNonMetaCharacter(char character, TokenCollector tokenCollector)
/*    */     throws MessageDescriptorFormatException
/*    */   {
/* 27 */     tokenCollector.appendToToken('$');
/* 28 */     tokenCollector.appendToToken(character);
/* 29 */     tokenCollector.terminateToken();
/* 30 */     tokenCollector.transitionState(new BeginState());
/*    */   }
/*    */   
/*    */   public void handleBeginTerm(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException
/*    */   {
/* 35 */     tokenCollector.terminateToken();
/*    */     
/* 37 */     tokenCollector.appendToToken('$');
/* 38 */     tokenCollector.appendToToken(character);
/* 39 */     tokenCollector.makeELToken();
/* 40 */     tokenCollector.transitionState(new InterpolationTermState());
/*    */   }
/*    */   
/*    */   public void handleEndTerm(char character, TokenCollector tokenCollector) throws MessageDescriptorFormatException
/*    */   {
/* 45 */     throw log.getNonTerminatedParameterException(tokenCollector
/* 46 */       .getOriginalMessageDescriptor(), character);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void handleEscapeCharacter(char character, TokenCollector tokenCollector)
/*    */     throws MessageDescriptorFormatException
/*    */   {
/* 54 */     tokenCollector.transitionState(new EscapedState(this));
/*    */   }
/*    */   
/*    */   public void handleELDesignator(char character, TokenCollector tokenCollector)
/*    */     throws MessageDescriptorFormatException
/*    */   {
/* 60 */     handleNonMetaCharacter(character, tokenCollector);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\messageinterpolation\parser\ELState.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */