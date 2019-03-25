/*     */ package org.springframework.boot.autoconfigure.mustache;
/*     */ 
/*     */ import com.samskivert.mustache.Mustache;
/*     */ import com.samskivert.mustache.Mustache.Collector;
/*     */ import com.samskivert.mustache.Mustache.Compiler;
/*     */ import com.samskivert.mustache.Mustache.TemplateLoader;
/*     */ import javax.annotation.PostConstruct;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.mustache.web.MustacheViewResolver;
/*     */ import org.springframework.boot.autoconfigure.template.TemplateLocation;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.core.env.Environment;
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
/*     */ @ConditionalOnClass({Mustache.class})
/*     */ @EnableConfigurationProperties({MustacheProperties.class})
/*     */ public class MustacheAutoConfiguration
/*     */ {
/*  52 */   private static final Log logger = LogFactory.getLog(MustacheAutoConfiguration.class);
/*     */   
/*     */   private final MustacheProperties mustache;
/*     */   
/*     */   private final Environment environment;
/*     */   
/*     */   private final ApplicationContext applicationContext;
/*     */   
/*     */   public MustacheAutoConfiguration(MustacheProperties mustache, Environment environment, ApplicationContext applicationContext)
/*     */   {
/*  62 */     this.mustache = mustache;
/*  63 */     this.environment = environment;
/*  64 */     this.applicationContext = applicationContext;
/*     */   }
/*     */   
/*     */   @PostConstruct
/*     */   public void checkTemplateLocationExists() {
/*  69 */     if (this.mustache.isCheckTemplateLocation()) {
/*  70 */       TemplateLocation location = new TemplateLocation(this.mustache.getPrefix());
/*  71 */       if (!location.exists(this.applicationContext)) {
/*  72 */         logger.warn("Cannot find template location: " + location + " (please add some templates, check your Mustache configuration, or set spring.mustache.check-template-location=false)");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({Mustache.Compiler.class})
/*     */   public Mustache.Compiler mustacheCompiler(Mustache.TemplateLoader mustacheTemplateLoader)
/*     */   {
/*  83 */     return 
/*  84 */       Mustache.compiler().withLoader(mustacheTemplateLoader).withCollector(collector());
/*     */   }
/*     */   
/*     */   private Mustache.Collector collector() {
/*  88 */     MustacheEnvironmentCollector collector = new MustacheEnvironmentCollector();
/*  89 */     collector.setEnvironment(this.environment);
/*  90 */     return collector;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({Mustache.TemplateLoader.class})
/*     */   public MustacheResourceTemplateLoader mustacheTemplateLoader()
/*     */   {
/*  97 */     MustacheResourceTemplateLoader loader = new MustacheResourceTemplateLoader(this.mustache.getPrefix(), this.mustache.getSuffix());
/*  98 */     loader.setCharset(this.mustache.getCharsetName());
/*  99 */     return loader;
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnWebApplication
/*     */   protected static class MustacheWebConfiguration
/*     */   {
/*     */     private final MustacheProperties mustache;
/*     */     
/*     */     protected MustacheWebConfiguration(MustacheProperties mustache) {
/* 109 */       this.mustache = mustache;
/*     */     }
/*     */     
/*     */     @Bean
/*     */     @ConditionalOnMissingBean({MustacheViewResolver.class})
/*     */     public MustacheViewResolver mustacheViewResolver(Mustache.Compiler mustacheCompiler) {
/* 115 */       MustacheViewResolver resolver = new MustacheViewResolver();
/* 116 */       this.mustache.applyToViewResolver(resolver);
/* 117 */       resolver.setCharset(this.mustache.getCharsetName());
/* 118 */       resolver.setCompiler(mustacheCompiler);
/* 119 */       resolver.setOrder(2147483637);
/* 120 */       return resolver;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mustache\MustacheAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */