/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import org.springframework.aop.Pointcut;
/*    */ import org.springframework.aop.framework.AbstractSingletonProxyFactoryBean;
/*    */ import org.springframework.aop.support.DefaultPointcutAdvisor;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
/*    */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CacheProxyFactoryBean
/*    */   extends AbstractSingletonProxyFactoryBean
/*    */   implements BeanFactoryAware, SmartInitializingSingleton
/*    */ {
/* 50 */   private final CacheInterceptor cacheInterceptor = new CacheInterceptor();
/*    */   
/* 52 */   private Pointcut pointcut = Pointcut.TRUE;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setCacheOperationSources(CacheOperationSource... cacheOperationSources)
/*    */   {
/* 60 */     this.cacheInterceptor.setCacheOperationSources(cacheOperationSources);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setPointcut(Pointcut pointcut)
/*    */   {
/* 71 */     this.pointcut = pointcut;
/*    */   }
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory)
/*    */   {
/* 76 */     this.cacheInterceptor.setBeanFactory(beanFactory);
/*    */   }
/*    */   
/*    */   public void afterSingletonsInstantiated()
/*    */   {
/* 81 */     this.cacheInterceptor.afterSingletonsInstantiated();
/*    */   }
/*    */   
/*    */ 
/*    */   protected Object createMainInterceptor()
/*    */   {
/* 87 */     this.cacheInterceptor.afterPropertiesSet();
/* 88 */     return new DefaultPointcutAdvisor(this.pointcut, this.cacheInterceptor);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\interceptor\CacheProxyFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */