/*     */ package org.springframework.boot.autoconfigure.security.oauth2.client;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import org.aopalliance.intercept.MethodInterceptor;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.ImportAware;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.security.config.annotation.web.builders.HttpSecurity;
/*     */ import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @Conditional({EnableOAuth2SsoCondition.class})
/*     */ public class OAuth2SsoCustomConfiguration
/*     */   implements ImportAware, BeanPostProcessor, ApplicationContextAware
/*     */ {
/*     */   private Class<?> configType;
/*     */   private ApplicationContext applicationContext;
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext)
/*     */   {
/*  57 */     this.applicationContext = applicationContext;
/*     */   }
/*     */   
/*     */   public void setImportMetadata(AnnotationMetadata importMetadata)
/*     */   {
/*  62 */     this.configType = ClassUtils.resolveClassName(importMetadata.getClassName(), null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/*     */     throws BeansException
/*     */   {
/*  70 */     return bean;
/*     */   }
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName)
/*     */     throws BeansException
/*     */   {
/*  76 */     if ((this.configType.isAssignableFrom(bean.getClass())) && ((bean instanceof WebSecurityConfigurerAdapter)))
/*     */     {
/*  78 */       ProxyFactory factory = new ProxyFactory();
/*  79 */       factory.setTarget(bean);
/*  80 */       factory.addAdvice(new SsoSecurityAdapter(this.applicationContext));
/*  81 */       bean = factory.getProxy();
/*     */     }
/*  83 */     return bean;
/*     */   }
/*     */   
/*     */   private static class SsoSecurityAdapter implements MethodInterceptor
/*     */   {
/*     */     private SsoSecurityConfigurer configurer;
/*     */     
/*     */     SsoSecurityAdapter(ApplicationContext applicationContext) {
/*  91 */       this.configurer = new SsoSecurityConfigurer(applicationContext);
/*     */     }
/*     */     
/*     */     public Object invoke(MethodInvocation invocation) throws Throwable
/*     */     {
/*  96 */       if (invocation.getMethod().getName().equals("init"))
/*     */       {
/*  98 */         Method method = ReflectionUtils.findMethod(WebSecurityConfigurerAdapter.class, "getHttp");
/*  99 */         ReflectionUtils.makeAccessible(method);
/* 100 */         HttpSecurity http = (HttpSecurity)ReflectionUtils.invokeMethod(method, invocation
/* 101 */           .getThis());
/* 102 */         this.configurer.configure(http);
/*     */       }
/* 104 */       return invocation.proceed();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\security\oauth2\client\OAuth2SsoCustomConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */