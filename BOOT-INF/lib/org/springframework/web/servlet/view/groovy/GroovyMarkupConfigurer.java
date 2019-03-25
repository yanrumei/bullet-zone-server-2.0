/*     */ package org.springframework.web.servlet.view.groovy;
/*     */ 
/*     */ import groovy.text.markup.MarkupTemplateEngine;
/*     */ import groovy.text.markup.MarkupTemplateEngine.TemplateResource;
/*     */ import groovy.text.markup.TemplateConfiguration;
/*     */ import groovy.text.markup.TemplateResolver;
/*     */ import java.io.IOException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.i18n.LocaleContextHolder;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class GroovyMarkupConfigurer
/*     */   extends TemplateConfiguration
/*     */   implements GroovyMarkupConfig, ApplicationContextAware, InitializingBean
/*     */ {
/*  87 */   private String resourceLoaderPath = "classpath:";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private MarkupTemplateEngine templateEngine;
/*     */   
/*     */ 
/*     */ 
/*     */   private ApplicationContext applicationContext;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResourceLoaderPath(String resourceLoaderPath)
/*     */   {
/* 103 */     this.resourceLoaderPath = resourceLoaderPath;
/*     */   }
/*     */   
/*     */   public String getResourceLoaderPath() {
/* 107 */     return this.resourceLoaderPath;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTemplateEngine(MarkupTemplateEngine templateEngine)
/*     */   {
/* 117 */     this.templateEngine = templateEngine;
/*     */   }
/*     */   
/*     */   public MarkupTemplateEngine getTemplateEngine() {
/* 121 */     return this.templateEngine;
/*     */   }
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext)
/*     */   {
/* 126 */     this.applicationContext = applicationContext;
/*     */   }
/*     */   
/*     */   protected ApplicationContext getApplicationContext() {
/* 130 */     return this.applicationContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setLocale(Locale locale)
/*     */   {
/* 139 */     super.setLocale(locale);
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet()
/*     */     throws Exception
/*     */   {
/* 145 */     if (this.templateEngine == null) {
/* 146 */       this.templateEngine = createTemplateEngine();
/*     */     }
/*     */   }
/*     */   
/*     */   protected MarkupTemplateEngine createTemplateEngine() throws IOException {
/* 151 */     if (this.templateEngine == null) {
/* 152 */       ClassLoader templateClassLoader = createTemplateClassLoader();
/* 153 */       this.templateEngine = new MarkupTemplateEngine(templateClassLoader, this, new LocaleTemplateResolver(null));
/*     */     }
/* 155 */     return this.templateEngine;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ClassLoader createTemplateClassLoader()
/*     */     throws IOException
/*     */   {
/* 163 */     String[] paths = StringUtils.commaDelimitedListToStringArray(getResourceLoaderPath());
/* 164 */     List<URL> urls = new ArrayList();
/* 165 */     for (String path : paths) {
/* 166 */       Resource[] resources = getApplicationContext().getResources(path);
/* 167 */       if (resources.length > 0) {
/* 168 */         for (Resource resource : resources) {
/* 169 */           if (resource.exists()) {
/* 170 */             urls.add(resource.getURL());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 175 */     ClassLoader classLoader = getApplicationContext().getClassLoader();
/* 176 */     return urls.size() > 0 ? new URLClassLoader((URL[])urls.toArray(new URL[urls.size()]), classLoader) : classLoader;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected URL resolveTemplate(ClassLoader classLoader, String templatePath)
/*     */     throws IOException
/*     */   {
/* 188 */     MarkupTemplateEngine.TemplateResource resource = MarkupTemplateEngine.TemplateResource.parse(templatePath);
/* 189 */     Locale locale = LocaleContextHolder.getLocale();
/* 190 */     URL url = classLoader.getResource(resource.withLocale(locale.toString().replace("-", "_")).toString());
/* 191 */     if (url == null) {
/* 192 */       url = classLoader.getResource(resource.withLocale(locale.getLanguage()).toString());
/*     */     }
/* 194 */     if (url == null) {
/* 195 */       url = classLoader.getResource(resource.withLocale(null).toString());
/*     */     }
/* 197 */     if (url == null) {
/* 198 */       throw new IOException("Unable to load template:" + templatePath);
/*     */     }
/* 200 */     return url;
/*     */   }
/*     */   
/*     */ 
/*     */   private class LocaleTemplateResolver
/*     */     implements TemplateResolver
/*     */   {
/*     */     private ClassLoader classLoader;
/*     */     
/*     */ 
/*     */     private LocaleTemplateResolver() {}
/*     */     
/*     */     public void configure(ClassLoader templateClassLoader, TemplateConfiguration configuration)
/*     */     {
/* 214 */       this.classLoader = templateClassLoader;
/*     */     }
/*     */     
/*     */     public URL resolveTemplate(String templatePath) throws IOException
/*     */     {
/* 219 */       return GroovyMarkupConfigurer.this.resolveTemplate(this.classLoader, templatePath);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\groovy\GroovyMarkupConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */