/*     */ package org.springframework.web.servlet.view.velocity;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.velocity.app.VelocityEngine;
/*     */ import org.apache.velocity.exception.VelocityException;
/*     */ import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.ui.velocity.VelocityEngineFactory;
/*     */ import org.springframework.web.context.ServletContextAware;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Deprecated
/*     */ public class VelocityConfigurer
/*     */   extends VelocityEngineFactory
/*     */   implements VelocityConfig, InitializingBean, ResourceLoaderAware, ServletContextAware
/*     */ {
/*     */   private static final String SPRING_MACRO_RESOURCE_LOADER_NAME = "springMacro";
/*     */   private static final String SPRING_MACRO_RESOURCE_LOADER_CLASS = "springMacro.resource.loader.class";
/*     */   private static final String SPRING_MACRO_LIBRARY = "org/springframework/web/servlet/view/velocity/spring.vm";
/*     */   private VelocityEngine velocityEngine;
/*     */   private ServletContext servletContext;
/*     */   
/*     */   public void setVelocityEngine(VelocityEngine velocityEngine)
/*     */   {
/* 104 */     this.velocityEngine = velocityEngine;
/*     */   }
/*     */   
/*     */   public void setServletContext(ServletContext servletContext)
/*     */   {
/* 109 */     this.servletContext = servletContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */     throws IOException, VelocityException
/*     */   {
/* 120 */     if (this.velocityEngine == null) {
/* 121 */       this.velocityEngine = createVelocityEngine();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void postProcessVelocityEngine(VelocityEngine velocityEngine)
/*     */   {
/* 132 */     velocityEngine.setApplicationAttribute(ServletContext.class.getName(), this.servletContext);
/* 133 */     velocityEngine.setProperty("springMacro.resource.loader.class", ClasspathResourceLoader.class
/* 134 */       .getName());
/* 135 */     velocityEngine.addProperty("resource.loader", "springMacro");
/*     */     
/* 137 */     velocityEngine.addProperty("velocimacro.library", "org/springframework/web/servlet/view/velocity/spring.vm");
/*     */     
/*     */ 
/* 140 */     if (this.logger.isInfoEnabled()) {
/* 141 */       this.logger.info("ClasspathResourceLoader with name 'springMacro' added to configured VelocityEngine");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public VelocityEngine getVelocityEngine()
/*     */   {
/* 148 */     return this.velocityEngine;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\velocity\VelocityConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */