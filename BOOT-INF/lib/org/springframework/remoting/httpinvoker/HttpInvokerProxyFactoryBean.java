/*    */ package org.springframework.remoting.httpinvoker;
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
/*    */ public class HttpInvokerProxyFactoryBean
/*    */   extends HttpInvokerClientInterceptor
/*    */   implements FactoryBean<Object>
/*    */ {
/*    */   private Object serviceProxy;
/*    */   
/*    */   public void afterPropertiesSet()
/*    */   {
/* 63 */     super.afterPropertiesSet();
/* 64 */     if (getServiceInterface() == null) {
/* 65 */       throw new IllegalArgumentException("Property 'serviceInterface' is required");
/*    */     }
/* 67 */     this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(getBeanClassLoader());
/*    */   }
/*    */   
/*    */ 
/*    */   public Object getObject()
/*    */   {
/* 73 */     return this.serviceProxy;
/*    */   }
/*    */   
/*    */   public Class<?> getObjectType()
/*    */   {
/* 78 */     return getServiceInterface();
/*    */   }
/*    */   
/*    */   public boolean isSingleton()
/*    */   {
/* 83 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\remoting\httpinvoker\HttpInvokerProxyFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */