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
/*    */ public class CommentToken
/*    */   extends Token
/*    */ {
/*    */   public CommentToken(Mark startMark, Mark endMark)
/*    */   {
/* 22 */     super(startMark, endMark);
/*    */   }
/*    */   
/*    */   public Token.ID getTokenId()
/*    */   {
/* 27 */     return Token.ID.Comment;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\snakeyaml-1.17.jar!\org\yaml\snakeyaml\tokens\CommentToken.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */