/*     */ package org.springframework.boot.autoconfigure.thymeleaf;
/*     */ 
/*     */ import com.github.mxab.thymeleaf.extras.dataattribute.dialect.DataAttributeDialect;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collection;
/*     */ import javax.servlet.Servlet;
/*     */ import nz.net.ultraq.thymeleaf.LayoutDialect;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnJava;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnJava.JavaVersion;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.web.ConditionalOnEnabledResourceChain;
/*     */ import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.web.servlet.resource.ResourceUrlEncodingFilter;
/*     */ import org.thymeleaf.dialect.IDialect;
/*     */ import org.thymeleaf.extras.conditionalcomments.dialect.ConditionalCommentsDialect;
/*     */ import org.thymeleaf.extras.java8time.dialect.Java8TimeDialect;
/*     */ import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
/*     */ import org.thymeleaf.spring4.SpringTemplateEngine;
/*     */ import org.thymeleaf.spring4.resourceresolver.SpringResourceResourceResolver;
/*     */ import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
/*     */ import org.thymeleaf.spring4.view.ThymeleafViewResolver;
/*     */ import org.thymeleaf.templateresolver.ITemplateResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @EnableConfigurationProperties({ThymeleafProperties.class})
/*     */ @ConditionalOnClass({SpringTemplateEngine.class})
/*     */ @AutoConfigureAfter({WebMvcAutoConfiguration.class})
/*     */ public class ThymeleafAutoConfiguration
/*     */ {
/*     */   @Configuration
/*     */   @ConditionalOnMissingClass({"org.thymeleaf.templatemode.TemplateMode"})
/*     */   static class Thymeleaf2Configuration
/*     */   {
/*     */     @Configuration
/*     */     @ConditionalOnMissingBean(name={"defaultTemplateResolver"})
/*     */     static class DefaultTemplateResolverConfiguration
/*     */       extends AbstractTemplateResolverConfiguration
/*     */     {
/*     */       DefaultTemplateResolverConfiguration(ThymeleafProperties properties, ApplicationContext applicationContext)
/*     */       {
/*  80 */         super(applicationContext);
/*     */       }
/*     */       
/*     */       @Bean
/*     */       public SpringResourceResourceResolver thymeleafResourceResolver() {
/*  85 */         return new SpringResourceResourceResolver();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     @Configuration
/*     */     @ConditionalOnClass({Servlet.class})
/*     */     @ConditionalOnWebApplication
/*     */     static class Thymeleaf2ViewResolverConfiguration
/*     */       extends AbstractThymeleafViewResolverConfiguration
/*     */     {
/*     */       Thymeleaf2ViewResolverConfiguration(ThymeleafProperties properties, SpringTemplateEngine templateEngine)
/*     */       {
/*  98 */         super(templateEngine);
/*     */       }
/*     */       
/*     */ 
/*     */       protected void configureTemplateEngine(ThymeleafViewResolver resolver, SpringTemplateEngine templateEngine)
/*     */       {
/* 104 */         resolver.setTemplateEngine(templateEngine);
/*     */       }
/*     */     }
/*     */     
/*     */     @Configuration
/*     */     @ConditionalOnClass({ConditionalCommentsDialect.class})
/*     */     static class ThymeleafConditionalCommentsDialectConfiguration
/*     */     {
/*     */       @Bean
/*     */       @ConditionalOnMissingBean
/*     */       public ConditionalCommentsDialect conditionalCommentsDialect()
/*     */       {
/* 116 */         return new ConditionalCommentsDialect();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass(name={"org.thymeleaf.templatemode.TemplateMode"})
/*     */   static class Thymeleaf3Configuration
/*     */   {
/*     */     @Configuration
/*     */     @ConditionalOnMissingBean(name={"defaultTemplateResolver"})
/*     */     static class DefaultTemplateResolverConfiguration
/*     */       extends AbstractTemplateResolverConfiguration
/*     */     {
/*     */       DefaultTemplateResolverConfiguration(ThymeleafProperties properties, ApplicationContext applicationContext)
/*     */       {
/* 134 */         super(applicationContext);
/*     */       }
/*     */       
/*     */       @Bean
/*     */       public SpringResourceTemplateResolver defaultTemplateResolver()
/*     */       {
/* 140 */         SpringResourceTemplateResolver resolver = super.defaultTemplateResolver();
/* 141 */         Method setCheckExistence = ReflectionUtils.findMethod(resolver.getClass(), "setCheckExistence", new Class[] { Boolean.TYPE });
/*     */         
/* 143 */         ReflectionUtils.invokeMethod(setCheckExistence, resolver, new Object[] {
/* 144 */           Boolean.valueOf(getProperties().isCheckTemplate()) });
/* 145 */         return resolver;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     @Configuration
/*     */     @ConditionalOnClass({Servlet.class})
/*     */     @ConditionalOnWebApplication
/*     */     static class Thymeleaf3ViewResolverConfiguration
/*     */       extends AbstractThymeleafViewResolverConfiguration
/*     */     {
/*     */       Thymeleaf3ViewResolverConfiguration(ThymeleafProperties properties, SpringTemplateEngine templateEngine)
/*     */       {
/* 158 */         super(templateEngine);
/*     */       }
/*     */       
/*     */ 
/*     */       protected void configureTemplateEngine(ThymeleafViewResolver resolver, SpringTemplateEngine templateEngine)
/*     */       {
/*     */         try
/*     */         {
/* 166 */           setTemplateEngine = ReflectionUtils.findMethod(resolver.getClass(), "setTemplateEngine", new Class[] {
/*     */           
/* 168 */             Class.forName("org.thymeleaf.ITemplateEngine", true, resolver
/* 169 */             .getClass().getClassLoader()) });
/*     */         } catch (ClassNotFoundException ex) {
/*     */           Method setTemplateEngine;
/* 172 */           throw new IllegalStateException(ex); }
/*     */         Method setTemplateEngine;
/* 174 */         ReflectionUtils.invokeMethod(setTemplateEngine, resolver, new Object[] { templateEngine });
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnMissingBean({SpringTemplateEngine.class})
/*     */   protected static class ThymeleafDefaultConfiguration
/*     */   {
/*     */     private final Collection<ITemplateResolver> templateResolvers;
/*     */     
/*     */     private final Collection<IDialect> dialects;
/*     */     
/*     */ 
/*     */     public ThymeleafDefaultConfiguration(Collection<ITemplateResolver> templateResolvers, ObjectProvider<Collection<IDialect>> dialectsProvider)
/*     */     {
/* 192 */       this.templateResolvers = templateResolvers;
/* 193 */       this.dialects = ((Collection)dialectsProvider.getIfAvailable());
/*     */     }
/*     */     
/*     */     @Bean
/*     */     public SpringTemplateEngine templateEngine() {
/* 198 */       SpringTemplateEngine engine = new SpringTemplateEngine();
/* 199 */       for (ITemplateResolver templateResolver : this.templateResolvers) {
/* 200 */         engine.addTemplateResolver(templateResolver);
/*     */       }
/* 202 */       if (!CollectionUtils.isEmpty(this.dialects)) {
/* 203 */         for (IDialect dialect : this.dialects) {
/* 204 */           engine.addDialect(dialect);
/*     */         }
/*     */       }
/* 207 */       return engine;
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnClass(name={"nz.net.ultraq.thymeleaf.LayoutDialect"})
/*     */   protected static class ThymeleafWebLayoutConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnMissingBean
/*     */     public LayoutDialect layoutDialect()
/*     */     {
/* 219 */       return new LayoutDialect();
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnClass({DataAttributeDialect.class})
/*     */   protected static class DataAttributeDialectConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnMissingBean
/*     */     public DataAttributeDialect dialect()
/*     */     {
/* 231 */       return new DataAttributeDialect();
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnClass({SpringSecurityDialect.class})
/*     */   protected static class ThymeleafSecurityDialectConfiguration
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnMissingBean
/*     */     public SpringSecurityDialect securityDialect()
/*     */     {
/* 243 */       return new SpringSecurityDialect();
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnJava(ConditionalOnJava.JavaVersion.EIGHT)
/*     */   @ConditionalOnClass({Java8TimeDialect.class})
/*     */   protected static class ThymeleafJava8TimeDialect
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnMissingBean
/*     */     public Java8TimeDialect java8TimeDialect()
/*     */     {
/* 256 */       return new Java8TimeDialect();
/*     */     }
/*     */   }
/*     */   
/*     */   @Configuration
/*     */   @ConditionalOnWebApplication
/*     */   protected static class ThymeleafResourceHandlingConfig
/*     */   {
/*     */     @Bean
/*     */     @ConditionalOnMissingBean
/*     */     @ConditionalOnEnabledResourceChain
/*     */     public ResourceUrlEncodingFilter resourceUrlEncodingFilter()
/*     */     {
/* 269 */       return new ResourceUrlEncodingFilter();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\thymeleaf\ThymeleafAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */