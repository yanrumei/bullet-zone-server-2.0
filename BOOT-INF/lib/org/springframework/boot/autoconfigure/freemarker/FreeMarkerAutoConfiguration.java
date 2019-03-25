/*     */ package org.springframework.boot.autoconfigure.freemarker;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.servlet.Servlet;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWebApplication;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.template.TemplateLocation;
/*     */ import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
/*     */ import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
/*     */ import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
/*     */ import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
/*     */ import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
/*     */ import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
/*     */ import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
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
/*     */ @org.springframework.context.annotation.Configuration
/*     */ @ConditionalOnClass({freemarker.template.Configuration.class, FreeMarkerConfigurationFactory.class})
/*     */ @AutoConfigureAfter({WebMvcAutoConfiguration.class})
/*     */ @EnableConfigurationProperties({FreeMarkerProperties.class})
/*     */ public class FreeMarkerAutoConfiguration
/*     */ {
/*  67 */   private static final Log logger = LogFactory.getLog(FreeMarkerAutoConfiguration.class);
/*     */   
/*     */   private final ApplicationContext applicationContext;
/*     */   
/*     */   private final FreeMarkerProperties properties;
/*     */   
/*     */   public FreeMarkerAutoConfiguration(ApplicationContext applicationContext, FreeMarkerProperties properties)
/*     */   {
/*  75 */     this.applicationContext = applicationContext;
/*  76 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   @PostConstruct
/*     */   public void checkTemplateLocationExists() {
/*  81 */     if (this.properties.isCheckTemplateLocation()) {
/*  82 */       TemplateLocation templatePathLocation = null;
/*  83 */       List<TemplateLocation> locations = new ArrayList();
/*  84 */       for (String templateLoaderPath : this.properties.getTemplateLoaderPath()) {
/*  85 */         TemplateLocation location = new TemplateLocation(templateLoaderPath);
/*  86 */         locations.add(location);
/*  87 */         if (location.exists(this.applicationContext)) {
/*  88 */           templatePathLocation = location;
/*  89 */           break;
/*     */         }
/*     */       }
/*  92 */       if (templatePathLocation == null) {
/*  93 */         logger.warn("Cannot find template location(s): " + locations + " (please add some templates, check your FreeMarker configuration, or set spring.freemarker.checkTemplateLocation=false)");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected static class FreeMarkerConfiguration
/*     */   {
/*     */     @Autowired
/*     */     protected FreeMarkerProperties properties;
/*     */     
/*     */ 
/*     */     protected void applyProperties(FreeMarkerConfigurationFactory factory)
/*     */     {
/* 107 */       factory.setTemplateLoaderPaths(this.properties.getTemplateLoaderPath());
/* 108 */       factory.setPreferFileSystemAccess(this.properties.isPreferFileSystemAccess());
/* 109 */       factory.setDefaultEncoding(this.properties.getCharsetName());
/* 110 */       Properties settings = new Properties();
/* 111 */       settings.putAll(this.properties.getSettings());
/* 112 */       factory.setFreemarkerSettings(settings);
/*     */     }
/*     */   }
/*     */   
/*     */   @org.springframework.context.annotation.Configuration
/*     */   @ConditionalOnNotWebApplication
/*     */   public static class FreeMarkerNonWebConfiguration extends FreeMarkerAutoConfiguration.FreeMarkerConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnMissingBean
/*     */     public FreeMarkerConfigurationFactoryBean freeMarkerConfiguration()
/*     */     {
/* 124 */       FreeMarkerConfigurationFactoryBean freeMarkerFactoryBean = new FreeMarkerConfigurationFactoryBean();
/* 125 */       applyProperties(freeMarkerFactoryBean);
/* 126 */       return freeMarkerFactoryBean;
/*     */     }
/*     */   }
/*     */   
/*     */   @org.springframework.context.annotation.Configuration
/*     */   @ConditionalOnClass({Servlet.class, FreeMarkerConfigurer.class})
/*     */   @ConditionalOnWebApplication
/*     */   public static class FreeMarkerWebConfiguration extends FreeMarkerAutoConfiguration.FreeMarkerConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({FreeMarkerConfig.class})
/*     */     public FreeMarkerConfigurer freeMarkerConfigurer()
/*     */     {
/* 139 */       FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
/* 140 */       applyProperties(configurer);
/* 141 */       return configurer;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     public freemarker.template.Configuration freeMarkerConfiguration(FreeMarkerConfig configurer)
/*     */     {
/* 147 */       return configurer.getConfiguration();
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean(name={"freeMarkerViewResolver"})
/*     */     @ConditionalOnProperty(name={"spring.freemarker.enabled"}, matchIfMissing=true)
/*     */     public FreeMarkerViewResolver freeMarkerViewResolver() {
/* 154 */       FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
/* 155 */       this.properties.applyToViewResolver(resolver);
/* 156 */       return resolver;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean
/*     */     @ConditionalOnEnabledResourceChain
/*     */     public ResourceUrlEncodingFilter resourceUrlEncodingFilter() {
/* 163 */       return new ResourceUrlEncodingFilter();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\freemarker\FreeMarkerAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */