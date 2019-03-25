/*    */ package org.apache.coyote.ajp;
/*    */ 
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.juli.logging.LogFactory;
/*    */ import org.apache.tomcat.util.res.StringManager;
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
/*    */ @Deprecated
/*    */ public class AjpProtocol
/*    */   extends AjpNioProtocol
/*    */ {
/* 29 */   private static final Log log = LogFactory.getLog(AjpProtocol.class);
/* 30 */   private static final StringManager sm = StringManager.getManager(AjpProtocol.class);
/*    */   
/*    */ 
/*    */   public AjpProtocol()
/*    */   {
/* 35 */     log.warn(sm.getString("ajpprotocol.noBio"));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\ajp\AjpProtocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */