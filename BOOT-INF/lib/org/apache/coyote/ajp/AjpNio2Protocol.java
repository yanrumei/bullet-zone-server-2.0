/*    */ package org.apache.coyote.ajp;
/*    */ 
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.juli.logging.LogFactory;
/*    */ import org.apache.tomcat.util.net.Nio2Channel;
/*    */ import org.apache.tomcat.util.net.Nio2Endpoint;
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
/*    */ public class AjpNio2Protocol
/*    */   extends AbstractAjpProtocol<Nio2Channel>
/*    */ {
/* 30 */   private static final Log log = LogFactory.getLog(AjpNio2Protocol.class);
/*    */   
/*    */   protected Log getLog() {
/* 33 */     return log;
/*    */   }
/*    */   
/*    */ 
/*    */   public AjpNio2Protocol()
/*    */   {
/* 39 */     super(new Nio2Endpoint());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String getNamePrefix()
/*    */   {
/* 47 */     return "ajp-nio2";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\ajp\AjpNio2Protocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */