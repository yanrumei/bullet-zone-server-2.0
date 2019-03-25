/*    */ package org.springframework.remoting.caucho;
/*    */ 
/*    */ import org.springframework.aop.framework.ProxyFactory;
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
/*    */ public class HessianProxyFactoryBean
/*    */   extends HessianClientInterceptor
/*    */   implements FactoryBean<Object>
/*    */ {
/*    */   private Object serviceProxy;
/*    */   
/*    */   public void afterPropertiesSet()
/*    */   {
/* 50 */     super.afterPropertiesSet();
/* 51 */     this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(getBeanClassLoader());
/*    */   }
/*    */   
/*    */ 
/*    */   public Object getObject()
/*    */   {
/* 57 */     return this.serviceProxy;
/*    */   }
/*    */   
/*    */   public Class<?> getObjectType()
/*    */   {
/* 62 */     return getServiceInterface();
/*    */   }
/*    */   
/*    */   public boolean isSingleton()
/*    */   {
/* 67 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\remoting\caucho\HessianProxyFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */