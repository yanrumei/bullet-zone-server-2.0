/*    */ package org.apache.coyote.http11;
/*    */ 
/*    */ import org.apache.tomcat.util.net.AbstractJsseEndpoint;
/*    */ import org.apache.tomcat.util.net.openssl.OpenSSLImplementation;
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
/*    */ public abstract class AbstractHttp11JsseProtocol<S>
/*    */   extends AbstractHttp11Protocol<S>
/*    */ {
/*    */   public AbstractHttp11JsseProtocol(AbstractJsseEndpoint<S> endpoint)
/*    */   {
/* 26 */     super(endpoint);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected AbstractJsseEndpoint<S> getEndpoint()
/*    */   {
/* 33 */     return (AbstractJsseEndpoint)super.getEndpoint();
/*    */   }
/*    */   
/*    */   protected String getSslImplementationShortName()
/*    */   {
/* 38 */     if (OpenSSLImplementation.class.getName().equals(getSslImplementationName())) {
/* 39 */       return "openssl";
/*    */     }
/* 41 */     return "jsse";
/*    */   }
/*    */   
/* 44 */   public String getSslImplementationName() { return getEndpoint().getSslImplementationName(); }
/* 45 */   public void setSslImplementationName(String s) { getEndpoint().setSslImplementationName(s); }
/*    */   
/*    */ 
/* 48 */   public int getSniParseLimit() { return getEndpoint().getSniParseLimit(); }
/*    */   
/* 50 */   public void setSniParseLimit(int sniParseLimit) { getEndpoint().setSniParseLimit(sniParseLimit); }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\http11\AbstractHttp11JsseProtocol.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */