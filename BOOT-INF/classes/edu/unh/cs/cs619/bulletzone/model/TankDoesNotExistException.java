/*   */ package edu.unh.cs.cs619.bulletzone.model;
/*   */ 
/*   */ import org.springframework.http.HttpStatus;
/*   */ 
/*   */ @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.NOT_FOUND)
/*   */ public final class TankDoesNotExistException extends Exception
/*   */ {
/*   */   public TankDoesNotExistException(Long tankId) {
/* 9 */     super(String.format("Tank '%d' does not exist", new Object[] { tankId }));
/*   */   }
/*   */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\classes\ed\\unh\cs\cs619\bulletzone\model\TankDoesNotExistException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */