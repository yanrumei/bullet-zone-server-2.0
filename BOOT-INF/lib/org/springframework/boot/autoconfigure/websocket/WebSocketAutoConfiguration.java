/*    */ package org.springframework.boot.autoconfigure.websocket;
/*    */ 
/*    */ import io.undertow.websockets.jsr.Bootstrap;
/*    */ import javax.servlet.Servlet;
/*    */ import javax.websocket.server.ServerContainer;
/*    */ import org.apache.catalina.startup.Tomcat;
/*    */ import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureBefore;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnJava.JavaVersion;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*    */ import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
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
/*    */ @Configuration
/*    */ @ConditionalOnClass({Servlet.class, ServerContainer.class})
/*    */ @ConditionalOnWebApplication
/*    */ @AutoConfigureBefore({EmbeddedServletContainerAutoConfiguration.class})
/*    */ public class WebSocketAutoConfiguration
/*    */ {
/*    */   @Configuration
/*    */   @ConditionalOnClass(name={"org.apache.tomcat.websocket.server.WsSci"}, value={Tomcat.class})
/*    */   static class TomcatWebSocketConfiguration
/*    */   {
/*    */     @Bean
/*    */     @ConditionalOnMissingBean(name={"websocketContainerCustomizer"})
/*    */     @ConditionalOnJava(ConditionalOnJava.JavaVersion.SEVEN)
/*    */     public TomcatWebSocketContainerCustomizer websocketContainerCustomizer()
/*    */     {
/* 69 */       return new TomcatWebSocketContainerCustomizer();
/*    */     }
/*    */   }
/*    */   
/*    */   @Configuration
/*    */   @ConditionalOnClass({WebSocketServerContainerInitializer.class})
/*    */   static class JettyWebSocketConfiguration
/*    */   {
/*    */     @Bean
/*    */     @ConditionalOnMissingBean(name={"websocketContainerCustomizer"})
/*    */     public JettyWebSocketContainerCustomizer websocketContainerCustomizer()
/*    */     {
/* 81 */       return new JettyWebSocketContainerCustomizer();
/*    */     }
/*    */   }
/*    */   
/*    */   @Configuration
/*    */   @ConditionalOnClass({Bootstrap.class})
/*    */   static class UndertowWebSocketConfiguration
/*    */   {
/*    */     @Bean
/*    */     @ConditionalOnMissingBean(name={"websocketContainerCustomizer"})
/*    */     public UndertowWebSocketContainerCustomizer websocketContainerCustomizer()
/*    */     {
/* 93 */       return new UndertowWebSocketContainerCustomizer();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\websocket\WebSocketAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */