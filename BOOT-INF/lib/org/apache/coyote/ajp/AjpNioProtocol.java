/*    */ package org.apache.coyote.ajp;
/*    */ 
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.juli.logging.LogFactory;
/*    */ import org.apache.tomcat.util.net.NioChannel;
/*    */ import org.apache.tomcat.util.net.NioEndpoint;
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
/*    */ public class AjpNioProtocol
/*    */   extends AbstractAjpProtocol<NioChannel>
/*    */ {
/* 29 */   private static final Log log = LogFactory.getLog(AjpNioProtocol.class);
/*    */   
/*    */   protected Log getLog() {
/* 32 */     return log;
/*    */   }
/*    */   
/*    */ 
/*    */   public AjpNioProtocol()
/*    */   {
/* 38 */     super(new NioEndpoint());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String getNamePrefix()
/*    */   {
/* 46 */     return "ajp-nio";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\ajp\AjpNioProtocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */