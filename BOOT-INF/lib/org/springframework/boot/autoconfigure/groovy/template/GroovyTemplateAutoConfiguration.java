/*     */ package org.springframework.boot.autoconfigure.groovy.template;
/*     */ 
/*     */ import groovy.text.markup.MarkupTemplateEngine;
/*     */ import java.net.URL;
/*     */ import java.security.CodeSource;
/*     */ import java.security.ProtectionDomain;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.servlet.Servlet;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.template.TemplateLocation;
/*     */ import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
/*     */ import org.springframework.boot.context.properties.ConfigurationProperties;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.i18n.LocaleContextHolder;
/*     */ import org.springframework.web.servlet.view.UrlBasedViewResolver;
/*     */ import org.springframework.web.servlet.view.groovy.GroovyMarkupConfig;
/*     */ import org.springframework.web.servlet.view.groovy.GroovyMarkupConfigurer;
/*     */ import org.springframework.web.servlet.view.groovy.GroovyMarkupViewResolver;
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
/*     */ @Configuration
/*     */ @ConditionalOnClass({MarkupTemplateEngine.class})
/*     */ @AutoConfigureAfter({WebMvcAutoConfiguration.class})
/*     */ @EnableConfigurationProperties({GroovyTemplateProperties.class})
/*     */ public class GroovyTemplateAutoConfiguration
/*     */ {
/*  66 */   private static final Log logger = LogFactory.getLog(GroovyTemplateAutoConfiguration.class);
/*     */   
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass({GroovyMarkupConfigurer.class})
/*     */   public static class GroovyMarkupConfiguration
/*     */   {
/*     */     private final ApplicationContext applicationContext;
/*     */     
/*     */     private final GroovyTemplateProperties properties;
/*     */     
/*     */     private final MarkupTemplateEngine templateEngine;
/*     */     
/*     */     public GroovyMarkupConfiguration(ApplicationContext applicationContext, GroovyTemplateProperties properties, ObjectProvider<MarkupTemplateEngine> templateEngine)
/*     */     {
/*  81 */       this.applicationContext = applicationContext;
/*  82 */       this.properties = properties;
/*  83 */       this.templateEngine = ((MarkupTemplateEngine)templateEngine.getIfAvailable());
/*     */     }
/*     */     
/*     */     @PostConstruct
/*     */     public void checkTemplateLocationExists() {
/*  88 */       if ((this.properties.isCheckTemplateLocation()) && (!isUsingGroovyAllJar()))
/*     */       {
/*  90 */         TemplateLocation location = new TemplateLocation(this.properties.getResourceLoaderPath());
/*  91 */         if (!location.exists(this.applicationContext)) {
/*  92 */           GroovyTemplateAutoConfiguration.logger.warn("Cannot find template location: " + location + " (please add some templates, check your Groovy configuration, or set spring.groovy.template.check-template-location=false)");
/*     */         }
/*     */       }
/*     */     }
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
/*     */     private boolean isUsingGroovyAllJar()
/*     */     {
/*     */       try
/*     */       {
/* 110 */         ProtectionDomain domain = MarkupTemplateEngine.class.getProtectionDomain();
/* 111 */         CodeSource codeSource = domain.getCodeSource();
/* 112 */         if ((codeSource != null) && 
/* 113 */           (codeSource.getLocation().toString().contains("-all"))) {
/* 114 */           return true;
/*     */         }
/* 116 */         return false;
/*     */       }
/*     */       catch (Exception ex) {}
/* 119 */       return false;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({GroovyMarkupConfig.class})
/*     */     @ConfigurationProperties(prefix="spring.groovy.template.configuration")
/*     */     public GroovyMarkupConfigurer groovyMarkupConfigurer()
/*     */     {
/* 127 */       GroovyMarkupConfigurer configurer = new GroovyMarkupConfigurer();
/* 128 */       configurer.setResourceLoaderPath(this.properties.getResourceLoaderPath());
/* 129 */       configurer.setCacheTemplates(this.properties.isCache());
/* 130 */       if (this.templateEngine != null) {
/* 131 */         configurer.setTemplateEngine(this.templateEngine);
/*     */       }
/* 133 */       return configurer;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass({Servlet.class, LocaleContextHolder.class, UrlBasedViewResolver.class})
/*     */   @ConditionalOnWebApplication
/*     */   @ConditionalOnProperty(name={"spring.groovy.template.enabled"}, matchIfMissing=true)
/*     */   public static class GroovyWebConfiguration
/*     */   {
/*     */     private final GroovyTemplateProperties properties;
/*     */     
/*     */     public GroovyWebConfiguration(GroovyTemplateProperties properties)
/*     */     {
/* 148 */       this.properties = properties;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean(name={"groovyMarkupViewResolver"})
/*     */     public GroovyMarkupViewResolver groovyMarkupViewResolver() {
/* 154 */       GroovyMarkupViewResolver resolver = new GroovyMarkupViewResolver();
/* 155 */       this.properties.applyToViewResolver(resolver);
/* 156 */       return resolver;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\groovy\template\GroovyTemplateAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */