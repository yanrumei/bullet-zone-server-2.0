/*     */ package org.springframework.boot.autoconfigure.info;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Properties;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.ItemsBuilder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
/*     */ import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.boot.info.BuildProperties;
/*     */ import org.springframework.boot.info.GitProperties;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
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
/*     */ @EnableConfigurationProperties({ProjectInfoProperties.class})
/*     */ public class ProjectInfoAutoConfiguration
/*     */ {
/*     */   private final ProjectInfoProperties properties;
/*     */   
/*     */   public ProjectInfoAutoConfiguration(ProjectInfoProperties properties)
/*     */   {
/*  56 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   @Conditional({GitResourceAvailableCondition.class})
/*     */   @ConditionalOnMissingBean
/*     */   @Bean
/*     */   public GitProperties gitProperties() throws Exception {
/*  63 */     return new GitProperties(loadFrom(this.properties.getGit().getLocation(), "git"));
/*     */   }
/*     */   
/*     */   @ConditionalOnResource(resources={"${spring.info.build.location:classpath:META-INF/build-info.properties}"})
/*     */   @ConditionalOnMissingBean
/*     */   @Bean
/*     */   public BuildProperties buildProperties() throws Exception {
/*  70 */     return new BuildProperties(
/*  71 */       loadFrom(this.properties.getBuild().getLocation(), "build"));
/*     */   }
/*     */   
/*     */   protected Properties loadFrom(Resource location, String prefix) throws IOException {
/*  75 */     String p = prefix + ".";
/*  76 */     Properties source = PropertiesLoaderUtils.loadProperties(location);
/*  77 */     Properties target = new Properties();
/*  78 */     for (String key : source.stringPropertyNames()) {
/*  79 */       if (key.startsWith(p)) {
/*  80 */         target.put(key.substring(p.length()), source.get(key));
/*     */       }
/*     */     }
/*  83 */     return target;
/*     */   }
/*     */   
/*     */   static class GitResourceAvailableCondition extends SpringBootCondition
/*     */   {
/*  88 */     private final ResourceLoader defaultResourceLoader = new DefaultResourceLoader();
/*     */     
/*     */ 
/*     */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/*  93 */       ResourceLoader loader = context.getResourceLoader();
/*  94 */       if (loader == null) {
/*  95 */         loader = this.defaultResourceLoader;
/*     */       }
/*  97 */       PropertyResolver propertyResolver = context.getEnvironment();
/*  98 */       RelaxedPropertyResolver resolver = new RelaxedPropertyResolver(propertyResolver, "spring.info.git.");
/*     */       
/* 100 */       String location = resolver.getProperty("location");
/* 101 */       if (location == null) {
/* 102 */         resolver = new RelaxedPropertyResolver(propertyResolver, "spring.git.");
/* 103 */         location = resolver.getProperty("properties");
/* 104 */         if (location == null) {
/* 105 */           location = "classpath:git.properties";
/*     */         }
/*     */       }
/*     */       
/* 109 */       ConditionMessage.Builder message = ConditionMessage.forCondition("GitResource", new Object[0]);
/* 110 */       if (loader.getResource(location).exists()) {
/* 111 */         return 
/* 112 */           ConditionOutcome.match(message.found("git info at").items(new Object[] { location }));
/*     */       }
/* 114 */       return 
/* 115 */         ConditionOutcome.noMatch(message.didNotFind("git info at").items(new Object[] { location }));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\info\ProjectInfoAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */