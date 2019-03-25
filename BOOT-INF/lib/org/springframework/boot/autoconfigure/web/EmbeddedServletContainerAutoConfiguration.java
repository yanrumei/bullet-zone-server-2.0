/*     */ package org.springframework.boot.autoconfigure.web;
/*     */ 
/*     */ import io.undertow.Undertow;
/*     */ import javax.servlet.Servlet;
/*     */ import org.apache.catalina.startup.Tomcat;
/*     */ import org.eclipse.jetty.server.Server;
/*     */ import org.eclipse.jetty.util.Loader;
/*     */ import org.eclipse.jetty.webapp.WebAppContext;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureOrder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.condition.SearchStrategy;
/*     */ import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizerBeanPostProcessor;
/*     */ import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
/*     */ import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
/*     */ import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
/*     */ import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;
/*     */ import org.springframework.boot.web.servlet.ErrorPageRegistrarBeanPostProcessor;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.Import;
/*     */ import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.xnio.SslClientAuthMode;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @AutoConfigureOrder(Integer.MIN_VALUE)
/*     */ @Configuration
/*     */ @ConditionalOnWebApplication
/*     */ @Import({BeanPostProcessorsRegistrar.class})
/*     */ public class EmbeddedServletContainerAutoConfiguration
/*     */ {
/*     */   @Configuration
/*     */   @ConditionalOnClass({Servlet.class, Tomcat.class})
/*     */   @ConditionalOnMissingBean(value={EmbeddedServletContainerFactory.class}, search=SearchStrategy.CURRENT)
/*     */   public static class EmbeddedTomcat
/*     */   {
/*     */     @Bean
/*     */     public TomcatEmbeddedServletContainerFactory tomcatEmbeddedServletContainerFactory()
/*     */     {
/*  79 */       return new TomcatEmbeddedServletContainerFactory();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass({Servlet.class, Server.class, Loader.class, WebAppContext.class})
/*     */   @ConditionalOnMissingBean(value={EmbeddedServletContainerFactory.class}, search=SearchStrategy.CURRENT)
/*     */   public static class EmbeddedJetty
/*     */   {
/*     */     @Bean
/*     */     public JettyEmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory()
/*     */     {
/*  95 */       return new JettyEmbeddedServletContainerFactory();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Configuration
/*     */   @ConditionalOnClass({Servlet.class, Undertow.class, SslClientAuthMode.class})
/*     */   @ConditionalOnMissingBean(value={EmbeddedServletContainerFactory.class}, search=SearchStrategy.CURRENT)
/*     */   public static class EmbeddedUndertow
/*     */   {
/*     */     @Bean
/*     */     public UndertowEmbeddedServletContainerFactory undertowEmbeddedServletContainerFactory()
/*     */     {
/* 110 */       return new UndertowEmbeddedServletContainerFactory();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class BeanPostProcessorsRegistrar
/*     */     implements ImportBeanDefinitionRegistrar, BeanFactoryAware
/*     */   {
/*     */     private ConfigurableListableBeanFactory beanFactory;
/*     */     
/*     */ 
/*     */ 
/*     */     public void setBeanFactory(BeanFactory beanFactory)
/*     */       throws BeansException
/*     */     {
/* 126 */       if ((beanFactory instanceof ConfigurableListableBeanFactory)) {
/* 127 */         this.beanFactory = ((ConfigurableListableBeanFactory)beanFactory);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry)
/*     */     {
/* 134 */       if (this.beanFactory == null) {
/* 135 */         return;
/*     */       }
/* 137 */       registerSyntheticBeanIfMissing(registry, "embeddedServletContainerCustomizerBeanPostProcessor", EmbeddedServletContainerCustomizerBeanPostProcessor.class);
/*     */       
/*     */ 
/* 140 */       registerSyntheticBeanIfMissing(registry, "errorPageRegistrarBeanPostProcessor", ErrorPageRegistrarBeanPostProcessor.class);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     private void registerSyntheticBeanIfMissing(BeanDefinitionRegistry registry, String name, Class<?> beanClass)
/*     */     {
/* 147 */       if (ObjectUtils.isEmpty(this.beanFactory
/* 148 */         .getBeanNamesForType(beanClass, true, false))) {
/* 149 */         RootBeanDefinition beanDefinition = new RootBeanDefinition(beanClass);
/* 150 */         beanDefinition.setSynthetic(true);
/* 151 */         registry.registerBeanDefinition(name, beanDefinition);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\EmbeddedServletContainerAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */