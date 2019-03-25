/*    */ package org.springframework.remoting.rmi;
/*    */ 
/*    */ import org.springframework.aop.framework.ProxyFactory;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*    */ import org.springframework.beans.factory.FactoryBean;
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
/*    */ public class RmiProxyFactoryBean
/*    */   extends RmiClientInterceptor
/*    */   implements FactoryBean<Object>, BeanClassLoaderAware
/*    */ {
/*    */   private Object serviceProxy;
/*    */   
/*    */   public void afterPropertiesSet()
/*    */   {
/* 68 */     super.afterPropertiesSet();
/* 69 */     if (getServiceInterface() == null) {
/* 70 */       throw new IllegalArgumentException("Property 'serviceInterface' is required");
/*    */     }
/* 72 */     this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(getBeanClassLoader());
/*    */   }
/*    */   
/*    */ 
/*    */   public Object getObject()
/*    */   {
/* 78 */     return this.serviceProxy;
/*    */   }
/*    */   
/*    */   public Class<?> getObjectType()
/*    */   {
/* 83 */     return getServiceInterface();
/*    */   }
/*    */   
/*    */   public boolean isSingleton()
/*    */   {
/* 88 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\remoting\rmi\RmiProxyFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */