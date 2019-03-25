/*    */ package org.springframework.boot.context.embedded;
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
/*    */ public class PortInUseException
/*    */   extends EmbeddedServletContainerException
/*    */ {
/*    */   private final int port;
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
/*    */   public PortInUseException(int port)
/*    */   {
/* 35 */     super("Port " + port + " is already in use", null);
/* 36 */     this.port = port;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int getPort()
/*    */   {
/* 44 */     return this.port;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\PortInUseException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */