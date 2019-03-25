/*     */ package org.springframework.aop.target;
/*     */ 
/*     */ import org.springframework.aop.support.DefaultIntroductionAdvisor;
/*     */ import org.springframework.aop.support.DelegatingIntroductionInterceptor;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.beans.factory.DisposableBean;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractPoolingTargetSource
/*     */   extends AbstractPrototypeBasedTargetSource
/*     */   implements PoolingConfig, DisposableBean
/*     */ {
/*  58 */   private int maxSize = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMaxSize(int maxSize)
/*     */   {
/*  66 */     this.maxSize = maxSize;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getMaxSize()
/*     */   {
/*  74 */     return this.maxSize;
/*     */   }
/*     */   
/*     */   public final void setBeanFactory(BeanFactory beanFactory)
/*     */     throws BeansException
/*     */   {
/*  80 */     super.setBeanFactory(beanFactory);
/*     */     try {
/*  82 */       createPool();
/*     */     }
/*     */     catch (Throwable ex) {
/*  85 */       throw new BeanInitializationException("Could not create instance pool for TargetSource", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void createPool()
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Object getTarget()
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void releaseTarget(Object paramObject)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultIntroductionAdvisor getPoolingConfigMixin()
/*     */   {
/* 121 */     DelegatingIntroductionInterceptor dii = new DelegatingIntroductionInterceptor(this);
/* 122 */     return new DefaultIntroductionAdvisor(dii, PoolingConfig.class);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\target\AbstractPoolingTargetSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */