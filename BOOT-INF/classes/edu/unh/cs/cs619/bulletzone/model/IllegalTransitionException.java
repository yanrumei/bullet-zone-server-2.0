/*    */ package edu.unh.cs.cs619.bulletzone.model;
/*    */ 
/*    */ import org.springframework.http.HttpStatus;
/*    */ import org.springframework.web.bind.annotation.ResponseStatus;
/*    */ 
/*    */ @ResponseStatus(HttpStatus.CONFLICT)
/*    */ public final class IllegalTransitionException extends Exception
/*    */ {
/*    */   IllegalTransitionException(Long gameId, Long tankId, Direction from, Direction to)
/*    */   {
/* 11 */     super(String.format("It is illegal to turn tank '%d' in game '%d' from '%s' to '%s'", new Object[] { tankId, gameId, from, to }));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\model\IllegalTransitionException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */