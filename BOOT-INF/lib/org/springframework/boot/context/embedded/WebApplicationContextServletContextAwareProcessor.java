/*    */ package org.springframework.boot.context.embedded;
/*    */ 
/*    */ import javax.servlet.ServletConfig;
/*    */ import javax.servlet.ServletContext;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.web.context.ConfigurableWebApplicationContext;
/*    */ import org.springframework.web.context.support.ServletContextAwareProcessor;
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
/*    */ public class WebApplicationContextServletContextAwareProcessor
/*    */   extends ServletContextAwareProcessor
/*    */ {
/*    */   private final ConfigurableWebApplicationContext webApplicationContext;
/*    */   
/*    */   public WebApplicationContextServletContextAwareProcessor(ConfigurableWebApplicationContext webApplicationContext)
/*    */   {
/* 41 */     Assert.notNull(webApplicationContext, "WebApplicationContext must not be null");
/* 42 */     this.webApplicationContext = webApplicationContext;
/*    */   }
/*    */   
/*    */   protected ServletContext getServletContext()
/*    */   {
/* 47 */     ServletContext servletContext = this.webApplicationContext.getServletContext();
/* 48 */     return servletContext != null ? servletContext : super.getServletContext();
/*    */   }
/*    */   
/*    */   protected ServletConfig getServletConfig()
/*    */   {
/* 53 */     ServletConfig servletConfig = this.webApplicationContext.getServletConfig();
/* 54 */     return servletConfig != null ? servletConfig : super.getServletConfig();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\WebApplicationContextServletContextAwareProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */