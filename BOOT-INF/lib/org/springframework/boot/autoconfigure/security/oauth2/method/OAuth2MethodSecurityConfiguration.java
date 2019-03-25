/*     */ package org.springframework.boot.autoconfigure.security.oauth2.method;
/*     */ 
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
/*     */ import org.springframework.security.authentication.AuthenticationTrustResolver;
/*     */ import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
/*     */ import org.springframework.security.oauth2.common.OAuth2AccessToken;
/*     */ import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @ConditionalOnClass({OAuth2AccessToken.class})
/*     */ @ConditionalOnBean({GlobalMethodSecurityConfiguration.class})
/*     */ public class OAuth2MethodSecurityConfiguration
/*     */   implements BeanFactoryPostProcessor, ApplicationContextAware
/*     */ {
/*     */   private ApplicationContext applicationContext;
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext)
/*     */     throws BeansException
/*     */   {
/*  54 */     this.applicationContext = applicationContext;
/*     */   }
/*     */   
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*     */     throws BeansException
/*     */   {
/*  60 */     OAuth2ExpressionHandlerInjectionPostProcessor processor = new OAuth2ExpressionHandlerInjectionPostProcessor(this.applicationContext);
/*     */     
/*  62 */     beanFactory.addBeanPostProcessor(processor);
/*     */   }
/*     */   
/*     */   private static class OAuth2ExpressionHandlerInjectionPostProcessor
/*     */     implements BeanPostProcessor
/*     */   {
/*     */     private ApplicationContext applicationContext;
/*     */     
/*     */     OAuth2ExpressionHandlerInjectionPostProcessor(ApplicationContext applicationContext)
/*     */     {
/*  72 */       this.applicationContext = applicationContext;
/*     */     }
/*     */     
/*     */     public Object postProcessBeforeInitialization(Object bean, String beanName)
/*     */       throws BeansException
/*     */     {
/*  78 */       return bean;
/*     */     }
/*     */     
/*     */     public Object postProcessAfterInitialization(Object bean, String beanName)
/*     */       throws BeansException
/*     */     {
/*  84 */       if (((bean instanceof DefaultMethodSecurityExpressionHandler)) && (!(bean instanceof OAuth2MethodSecurityExpressionHandler)))
/*     */       {
/*  86 */         return getExpressionHandler((DefaultMethodSecurityExpressionHandler)bean);
/*     */       }
/*     */       
/*  89 */       return bean;
/*     */     }
/*     */     
/*     */     private OAuth2MethodSecurityExpressionHandler getExpressionHandler(DefaultMethodSecurityExpressionHandler bean)
/*     */     {
/*  94 */       OAuth2MethodSecurityExpressionHandler handler = new OAuth2MethodSecurityExpressionHandler();
/*  95 */       handler.setApplicationContext(this.applicationContext);
/*  96 */       AuthenticationTrustResolver trustResolver = (AuthenticationTrustResolver)findInContext(AuthenticationTrustResolver.class);
/*     */       
/*  98 */       if (trustResolver != null) {
/*  99 */         handler.setTrustResolver(trustResolver);
/*     */       }
/* 101 */       handler.setExpressionParser(bean.getExpressionParser());
/* 102 */       return handler;
/*     */     }
/*     */     
/*     */     private <T> T findInContext(Class<T> type) {
/* 106 */       if (BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.applicationContext, type).length == 1)
/*     */       {
/* 108 */         return (T)this.applicationContext.getBean(type);
/*     */       }
/* 110 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\method\OAuth2MethodSecurityConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */