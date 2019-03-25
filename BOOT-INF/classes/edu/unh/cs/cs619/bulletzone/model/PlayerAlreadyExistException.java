/*    */ package edu.unh.cs.cs619.bulletzone.model;
/*    */ 
/*    */ import org.springframework.http.HttpStatus;
/*    */ 
/*    */ @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.NOT_FOUND)
/*    */ public final class PlayerAlreadyExistException extends Exception
/*    */ {
/*    */   PlayerAlreadyExistException(String name)
/*    */   {
/* 10 */     super(String.format("Player with name '%s' already exist", new Object[] { name }));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\model\PlayerAlreadyExistException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */