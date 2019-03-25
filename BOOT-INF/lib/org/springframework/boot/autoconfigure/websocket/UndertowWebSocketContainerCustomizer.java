/*    */ package org.springframework.boot.autoconfigure.websocket;
/*    */ 
/*    */ import io.undertow.servlet.api.DeploymentInfo;
/*    */ import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
/*    */ import org.springframework.boot.context.embedded.undertow.UndertowDeploymentInfoCustomizer;
/*    */ import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
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
/*    */ public class UndertowWebSocketContainerCustomizer
/*    */   extends WebSocketContainerCustomizer<UndertowEmbeddedServletContainerFactory>
/*    */ {
/*    */   protected void doCustomize(UndertowEmbeddedServletContainerFactory container)
/*    */   {
/* 37 */     WebsocketDeploymentInfoCustomizer customizer = new WebsocketDeploymentInfoCustomizer(null);
/* 38 */     container.addDeploymentInfoCustomizers(new UndertowDeploymentInfoCustomizer[] { customizer });
/*    */   }
/*    */   
/*    */   private static class WebsocketDeploymentInfoCustomizer
/*    */     implements UndertowDeploymentInfoCustomizer
/*    */   {
/*    */     public void customize(DeploymentInfo deploymentInfo)
/*    */     {
/* 46 */       WebSocketDeploymentInfo info = new WebSocketDeploymentInfo();
/* 47 */       deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", info);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\websocket\UndertowWebSocketContainerCustomizer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */