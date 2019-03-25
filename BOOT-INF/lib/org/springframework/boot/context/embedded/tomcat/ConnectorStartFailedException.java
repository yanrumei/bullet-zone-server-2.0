/*    */ package org.springframework.boot.context.embedded.tomcat;
/*    */ 
/*    */ import org.springframework.boot.context.embedded.EmbeddedServletContainerException;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ConnectorStartFailedException
/*    */   extends EmbeddedServletContainerException
/*    */ {
/*    */   private final int port;
/*    */   
/*    */   public ConnectorStartFailedException(int port)
/*    */   {
/* 40 */     super("Connector configured to listen on port " + port + " failed to start", null);
/*    */     
/* 42 */     this.port = port;
/*    */   }
/*    */   
/*    */   public int getPort() {
/* 46 */     return this.port;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\tomcat\ConnectorStartFailedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */