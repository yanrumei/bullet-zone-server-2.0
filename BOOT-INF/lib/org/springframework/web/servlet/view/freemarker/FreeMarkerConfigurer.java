/*     */ package org.springframework.web.servlet.view.freemarker;
/*     */ 
/*     */ import freemarker.cache.ClassTemplateLoader;
/*     */ import freemarker.cache.TemplateLoader;
/*     */ import freemarker.ext.jsp.TaglibFactory;
/*     */ import freemarker.template.Configuration;
/*     */ import freemarker.template.TemplateException;
/*     */ import java.io.IOException;
/*     */ import java.util.List;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
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
/*     */ public class FreeMarkerConfigurer
/*     */   extends FreeMarkerConfigurationFactory
/*     */   implements FreeMarkerConfig, InitializingBean, ResourceLoaderAware, ServletContextAware
/*     */ {
/*     */   private Configuration configuration;
/*     */   private TaglibFactory taglibFactory;
/*     */   
/*     */   public void setConfiguration(Configuration configuration)
/*     */   {
/*  94 */     this.configuration = configuration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServletContext(ServletContext servletContext)
/*     */   {
/* 102 */     this.taglibFactory = new TaglibFactory(servletContext);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */     throws IOException, TemplateException
/*     */   {
/* 115 */     if (this.configuration == null) {
/* 116 */       this.configuration = createConfiguration();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void postProcessTemplateLoaders(List<TemplateLoader> templateLoaders)
/*     */   {
/* 126 */     templateLoaders.add(new ClassTemplateLoader(FreeMarkerConfigurer.class, ""));
/* 127 */     this.logger.info("ClassTemplateLoader for Spring macros added to FreeMarker configuration");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Configuration getConfiguration()
/*     */   {
/* 136 */     return this.configuration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public TaglibFactory getTaglibFactory()
/*     */   {
/* 144 */     return this.taglibFactory;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\freemarker\FreeMarkerConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */