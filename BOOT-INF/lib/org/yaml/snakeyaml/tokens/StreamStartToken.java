/*    */ package org.yaml.snakeyaml.tokens;
/*    */ 
/*    */ import org.yaml.snakeyaml.error.Mark;
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
/*    */ public final class StreamStartToken
/*    */   extends Token
/*    */ {
/*    */   public StreamStartToken(Mark startMark, Mark endMark)
/*    */   {
/* 23 */     super(startMark, endMark);
/*    */   }
/*    */   
/*    */   public Token.ID getTokenId()
/*    */   {
/* 28 */     return Token.ID.StreamStart;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\tokens\StreamStartToken.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */