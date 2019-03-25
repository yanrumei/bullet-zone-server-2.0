/*    */ package org.apache.coyote.http11;
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
/*    */ public class Http11Nio2Protocol
/*    */   extends AbstractHttp11JsseProtocol<Nio2Channel>
/*    */ {
/* 30 */   private static final Log log = LogFactory.getLog(Http11Nio2Protocol.class);
/*    */   
/*    */   public Http11Nio2Protocol()
/*    */   {
/* 34 */     super(new Nio2Endpoint());
/*    */   }
/*    */   
/*    */   protected Log getLog()
/*    */   {
/* 39 */     return log;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected String getNamePrefix()
/*    */   {
/* 46 */     if (isSSLEnabled()) {
/* 47 */       return "https-" + getSslImplementationShortName() + "-nio2";
/*    */     }
/* 49 */     return "http-nio2";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\Http11Nio2Protocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */