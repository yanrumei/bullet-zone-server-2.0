/*    */ package org.apache.coyote;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ErrorState
/*    */ {
/* 24 */   NONE(false, 0, true, true), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 33 */   CLOSE_CLEAN(true, 1, true, true), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 42 */   CLOSE_NOW(true, 2, false, true), 
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 50 */   CLOSE_CONNECTION_NOW(true, 3, false, false);
/*    */   
/*    */   private final boolean error;
/*    */   private final int severity;
/*    */   private final boolean ioAllowed;
/*    */   private final boolean connectionIoAllowed;
/*    */   
/*    */   private ErrorState(boolean error, int severity, boolean ioAllowed, boolean connectionIoAllowed)
/*    */   {
/* 59 */     this.error = error;
/* 60 */     this.severity = severity;
/* 61 */     this.ioAllowed = ioAllowed;
/* 62 */     this.connectionIoAllowed = connectionIoAllowed;
/*    */   }
/*    */   
/*    */   public boolean isError() {
/* 66 */     return this.error;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ErrorState getMostSevere(ErrorState input)
/*    */   {
/* 79 */     if (input.severity > this.severity) {
/* 80 */       return input;
/*    */     }
/* 82 */     return this;
/*    */   }
/*    */   
/*    */   public boolean isIoAllowed()
/*    */   {
/* 87 */     return this.ioAllowed;
/*    */   }
/*    */   
/*    */   public boolean isConnectionIoAllowed() {
/* 91 */     return this.connectionIoAllowed;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\ErrorState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */