/*    */ package org.apache.coyote.http11;
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
/*    */ public class Http11Protocol
/*    */   extends Http11NioProtocol
/*    */ {
/* 29 */   private static final Log log = LogFactory.getLog(Http11Protocol.class);
/* 30 */   private static final StringManager sm = StringManager.getManager(Http11Protocol.class);
/*    */   
/*    */ 
/*    */   public Http11Protocol()
/*    */   {
/* 35 */     log.warn(sm.getString("http11protocol.noBio"));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\Http11Protocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */