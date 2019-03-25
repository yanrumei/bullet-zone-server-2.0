/*    */ package org.springframework.boot.autoconfigure.websocket;
/*    */ 
/*    */ import org.eclipse.jetty.util.thread.ShutdownThread;
/*    */ import org.eclipse.jetty.webapp.AbstractConfiguration;
/*    */ import org.eclipse.jetty.webapp.Configuration;
/*    */ import org.eclipse.jetty.webapp.WebAppContext;
/*    */ import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
/*    */ import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
/*    */ import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
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
/*    */ public class JettyWebSocketContainerCustomizer
/*    */   extends WebSocketContainerCustomizer<JettyEmbeddedServletContainerFactory>
/*    */ {
/*    */   protected void doCustomize(JettyEmbeddedServletContainerFactory container)
/*    */   {
/* 40 */     container.addConfigurations(new Configuration[] { new AbstractConfiguration()
/*    */     {
/*    */       public void configure(WebAppContext context)
/*    */         throws Exception
/*    */       {
/* 45 */         ServerContainer serverContainer = WebSocketServerContainerInitializer.configureContext(context);
/* 46 */         ShutdownThread.deregister(serverContainer);
/*    */       }
/*    */     } });
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\websocket\JettyWebSocketContainerCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */