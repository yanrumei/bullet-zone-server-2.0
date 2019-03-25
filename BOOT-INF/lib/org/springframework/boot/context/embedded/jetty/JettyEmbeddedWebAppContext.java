/*    */ package org.springframework.boot.context.embedded.jetty;
/*    */ 
/*    */ import org.eclipse.jetty.servlet.ServletHandler;
/*    */ import org.eclipse.jetty.webapp.WebAppContext;
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
/*    */ class JettyEmbeddedWebAppContext
/*    */   extends WebAppContext
/*    */ {
/*    */   protected ServletHandler newServletHandler()
/*    */   {
/* 32 */     return new JettyEmbeddedServletHandler(null);
/*    */   }
/*    */   
/*    */   public void deferredInitialize() throws Exception {
/* 36 */     ((JettyEmbeddedServletHandler)getServletHandler()).deferredInitialize();
/*    */   }
/*    */   
/*    */   private static class JettyEmbeddedServletHandler extends ServletHandler
/*    */   {
/*    */     public void initialize() throws Exception
/*    */     {}
/*    */     
/*    */     public void deferredInitialize() throws Exception
/*    */     {
/* 46 */       super.initialize();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\jetty\JettyEmbeddedWebAppContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */