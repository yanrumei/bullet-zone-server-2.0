/*    */ package edu.unh.cs.cs619.bulletzone.repository;
/*    */ 
/*    */ import org.springframework.http.HttpStatus;
/*    */ 
/*    */ @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.NOT_FOUND)
/*    */ public final class GameDoesNotExistException extends Exception
/*    */ {
/*    */   GameDoesNotExistException(int gameId)
/*    */   {
/* 10 */     super(String.format("Game with id '%s' does not exist", new Object[] { Integer.valueOf(gameId) }));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\repository\GameDoesNotExistException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */