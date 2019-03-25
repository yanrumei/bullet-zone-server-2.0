/*    */ package org.apache.coyote.http11;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Http11NioProtocol
/*    */   extends AbstractHttp11JsseProtocol<NioChannel>
/*    */ {
/* 35 */   private static final Log log = LogFactory.getLog(Http11NioProtocol.class);
/*    */   
/*    */   public Http11NioProtocol()
/*    */   {
/* 39 */     super(new NioEndpoint());
/*    */   }
/*    */   
/*    */   protected Log getLog()
/*    */   {
/* 44 */     return log;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setPollerThreadCount(int count)
/*    */   {
/* 50 */     ((NioEndpoint)getEndpoint()).setPollerThreadCount(count);
/*    */   }
/*    */   
/*    */   public int getPollerThreadCount() {
/* 54 */     return ((NioEndpoint)getEndpoint()).getPollerThreadCount();
/*    */   }
/*    */   
/*    */   public void setSelectorTimeout(long timeout) {
/* 58 */     ((NioEndpoint)getEndpoint()).setSelectorTimeout(timeout);
/*    */   }
/*    */   
/*    */   public long getSelectorTimeout() {
/* 62 */     return ((NioEndpoint)getEndpoint()).getSelectorTimeout();
/*    */   }
/*    */   
/*    */   public void setPollerThreadPriority(int threadPriority) {
/* 66 */     ((NioEndpoint)getEndpoint()).setPollerThreadPriority(threadPriority);
/*    */   }
/*    */   
/*    */   public int getPollerThreadPriority() {
/* 70 */     return ((NioEndpoint)getEndpoint()).getPollerThreadPriority();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String getNamePrefix()
/*    */   {
/* 78 */     if (isSSLEnabled()) {
/* 79 */       return "https-" + getSslImplementationShortName() + "-nio";
/*    */     }
/* 81 */     return "http-nio";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\Http11NioProtocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */