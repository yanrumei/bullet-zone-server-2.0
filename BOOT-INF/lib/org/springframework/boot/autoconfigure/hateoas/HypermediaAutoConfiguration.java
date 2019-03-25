/*     */ package org.springframework.boot.autoconfigure.hateoas;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.Import;
/*     */ import org.springframework.hateoas.EntityLinks;
/*     */ import org.springframework.hateoas.LinkDiscoverers;
/*     */ import org.springframework.hateoas.Resource;
/*     */ import org.springframework.hateoas.config.EnableEntityLinks;
/*     */ import org.springframework.hateoas.config.EnableHypermediaSupport;
/*     */ import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
/*     */ import org.springframework.plugin.core.Plugin;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @ConditionalOnClass({Resource.class, RequestMapping.class, Plugin.class})
/*     */ @ConditionalOnWebApplication
/*     */ @AutoConfigureAfter({WebMvcAutoConfiguration.class, JacksonAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class, RepositoryRestMvcAutoConfiguration.class})
/*     */ @EnableConfigurationProperties({HateoasProperties.class})
/*     */ @Import({HypermediaHttpMessageConverterConfiguration.class})
/*     */ public class HypermediaAutoConfiguration
/*     */ {
/*     */   @Configuration
/*     */   @ConditionalOnMissingBean({LinkDiscoverers.class})
/*     */   @ConditionalOnClass({ObjectMapper.class})
/*     */   @EnableHypermediaSupport(type={org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType.HAL})
/*     */   protected static class HypermediaConfiguration
/*     */   {
/*     */     @Bean
/*     */     public static HypermediaAutoConfiguration.HalObjectMapperConfigurer halObjectMapperConfigurer()
/*     */     {
/*  76 */       return new HypermediaAutoConfiguration.HalObjectMapperConfigurer(null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnMissingBean({EntityLinks.class})
/*     */   @EnableEntityLinks
/*     */   protected static class EntityLinksConfiguration {}
/*     */   
/*     */ 
/*     */ 
/*     */   private static class HalObjectMapperConfigurer
/*     */     implements BeanPostProcessor, BeanFactoryAware
/*     */   {
/*     */     private BeanFactory beanFactory;
/*     */     
/*     */ 
/*     */ 
/*     */     public Object postProcessBeforeInitialization(Object bean, String beanName)
/*     */       throws BeansException
/*     */     {
/* 100 */       if (((bean instanceof ObjectMapper)) && ("_halObjectMapper".equals(beanName))) {
/* 101 */         postProcessHalObjectMapper((ObjectMapper)bean);
/*     */       }
/* 103 */       return bean;
/*     */     }
/*     */     
/*     */     private void postProcessHalObjectMapper(ObjectMapper objectMapper)
/*     */     {
/*     */       try {
/* 109 */         Jackson2ObjectMapperBuilder builder = (Jackson2ObjectMapperBuilder)this.beanFactory.getBean(Jackson2ObjectMapperBuilder.class);
/* 110 */         builder.configure(objectMapper);
/*     */       }
/*     */       catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {}
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Object postProcessAfterInitialization(Object bean, String beanName)
/*     */       throws BeansException
/*     */     {
/* 120 */       return bean;
/*     */     }
/*     */     
/*     */     public void setBeanFactory(BeanFactory beanFactory) throws BeansException
/*     */     {
/* 125 */       this.beanFactory = beanFactory;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\hateoas\HypermediaAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */