/*    */ package org.apache.coyote.http11;
/*    */ 
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.juli.logging.LogFactory;
/*    */ import org.apache.tomcat.util.net.AprEndpoint;
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
/*    */ public class Http11AprProtocol
/*    */   extends AbstractHttp11Protocol<Long>
/*    */ {
/* 34 */   private static final Log log = LogFactory.getLog(Http11AprProtocol.class);
/*    */   
/*    */   public Http11AprProtocol() {
/* 37 */     super(new AprEndpoint());
/*    */   }
/*    */   
/*    */   protected Log getLog()
/*    */   {
/* 42 */     return log;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean isAprRequired()
/*    */   {
/* 48 */     return true;
/*    */   }
/*    */   
/* 51 */   public int getPollTime() { return ((AprEndpoint)getEndpoint()).getPollTime(); }
/* 52 */   public void setPollTime(int pollTime) { ((AprEndpoint)getEndpoint()).setPollTime(pollTime); }
/*    */   
/* 54 */   public int getSendfileSize() { return ((AprEndpoint)getEndpoint()).getSendfileSize(); }
/* 55 */   public void setSendfileSize(int sendfileSize) { ((AprEndpoint)getEndpoint()).setSendfileSize(sendfileSize); }
/*    */   
/* 57 */   public boolean getDeferAccept() { return ((AprEndpoint)getEndpoint()).getDeferAccept(); }
/* 58 */   public void setDeferAccept(boolean deferAccept) { ((AprEndpoint)getEndpoint()).setDeferAccept(deferAccept); }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String getNamePrefix()
/*    */   {
/* 65 */     if (isSSLEnabled()) {
/* 66 */       return "https-openssl-apr";
/*    */     }
/* 68 */     return "http-apr";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\Http11AprProtocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */