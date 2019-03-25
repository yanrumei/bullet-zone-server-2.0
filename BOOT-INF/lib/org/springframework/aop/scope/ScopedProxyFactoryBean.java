/*     */ package org.springframework.aop.scope;
/*     */ 
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.springframework.aop.framework.AopInfrastructureBean;
/*     */ import org.springframework.aop.framework.ProxyConfig;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.aop.support.DelegatingIntroductionInterceptor;
/*     */ import org.springframework.aop.target.SimpleBeanTargetSource;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.FactoryBeanNotInitializedException;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class ScopedProxyFactoryBean
/*     */   extends ProxyConfig
/*     */   implements FactoryBean<Object>, BeanFactoryAware
/*     */ {
/*  56 */   private final SimpleBeanTargetSource scopedTargetSource = new SimpleBeanTargetSource();
/*     */   
/*     */ 
/*     */ 
/*     */   private String targetBeanName;
/*     */   
/*     */ 
/*     */   private Object proxy;
/*     */   
/*     */ 
/*     */ 
/*     */   public ScopedProxyFactoryBean()
/*     */   {
/*  69 */     setProxyTargetClass(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTargetBeanName(String targetBeanName)
/*     */   {
/*  77 */     this.targetBeanName = targetBeanName;
/*  78 */     this.scopedTargetSource.setTargetBeanName(targetBeanName);
/*     */   }
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory)
/*     */   {
/*  83 */     if (!(beanFactory instanceof ConfigurableBeanFactory)) {
/*  84 */       throw new IllegalStateException("Not running in a ConfigurableBeanFactory: " + beanFactory);
/*     */     }
/*  86 */     ConfigurableBeanFactory cbf = (ConfigurableBeanFactory)beanFactory;
/*     */     
/*  88 */     this.scopedTargetSource.setBeanFactory(beanFactory);
/*     */     
/*  90 */     ProxyFactory pf = new ProxyFactory();
/*  91 */     pf.copyFrom(this);
/*  92 */     pf.setTargetSource(this.scopedTargetSource);
/*     */     
/*  94 */     Class<?> beanType = beanFactory.getType(this.targetBeanName);
/*  95 */     if (beanType == null) {
/*  96 */       throw new IllegalStateException("Cannot create scoped proxy for bean '" + this.targetBeanName + "': Target type could not be determined at the time of proxy creation.");
/*     */     }
/*     */     
/*  99 */     if ((!isProxyTargetClass()) || (beanType.isInterface()) || (Modifier.isPrivate(beanType.getModifiers()))) {
/* 100 */       pf.setInterfaces(ClassUtils.getAllInterfacesForClass(beanType, cbf.getBeanClassLoader()));
/*     */     }
/*     */     
/*     */ 
/* 104 */     ScopedObject scopedObject = new DefaultScopedObject(cbf, this.scopedTargetSource.getTargetBeanName());
/* 105 */     pf.addAdvice(new DelegatingIntroductionInterceptor(scopedObject));
/*     */     
/*     */ 
/*     */ 
/* 109 */     pf.addInterface(AopInfrastructureBean.class);
/*     */     
/* 111 */     this.proxy = pf.getProxy(cbf.getBeanClassLoader());
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getObject()
/*     */   {
/* 117 */     if (this.proxy == null) {
/* 118 */       throw new FactoryBeanNotInitializedException();
/*     */     }
/* 120 */     return this.proxy;
/*     */   }
/*     */   
/*     */   public Class<?> getObjectType()
/*     */   {
/* 125 */     if (this.proxy != null) {
/* 126 */       return this.proxy.getClass();
/*     */     }
/* 128 */     return this.scopedTargetSource.getTargetClass();
/*     */   }
/*     */   
/*     */   public boolean isSingleton()
/*     */   {
/* 133 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\scope\ScopedProxyFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */