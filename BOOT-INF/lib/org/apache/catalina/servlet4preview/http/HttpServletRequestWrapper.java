/*    */ package org.apache.catalina.servlet4preview.http;
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
/*    */ public class HttpServletRequestWrapper
/*    */   extends javax.servlet.http.HttpServletRequestWrapper
/*    */   implements HttpServletRequest
/*    */ {
/*    */   public HttpServletRequestWrapper(javax.servlet.http.HttpServletRequest request)
/*    */   {
/* 35 */     super(request);
/*    */   }
/*    */   
/*    */   private HttpServletRequest _getHttpServletRequest() {
/* 39 */     return (HttpServletRequest)super.getRequest();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ServletMapping getServletMapping()
/*    */   {
/* 52 */     return _getHttpServletRequest().getServletMapping();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public PushBuilder newPushBuilder()
/*    */   {
/* 65 */     return _getHttpServletRequest().newPushBuilder();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\servlet4preview\http\HttpServletRequestWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */