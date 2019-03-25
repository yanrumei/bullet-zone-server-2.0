/*    */ package edu.unh.cs.cs619.bulletzone.model;
/*    */ 
/*    */ import org.springframework.http.HttpStatus;
/*    */ 
/*    */ @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.NOT_FOUND)
/*    */ public final class PlayerDoesNotExistException extends Exception
/*    */ {
/*    */   public PlayerDoesNotExistException(long name)
/*    */   {
/* 10 */     super(String.format("Soldier with tank ID '%d' does not exist", new Object[] { Long.valueOf(name) }));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\model\PlayerDoesNotExistException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */