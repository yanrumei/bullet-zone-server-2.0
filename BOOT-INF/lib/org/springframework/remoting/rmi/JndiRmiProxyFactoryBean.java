/*    */ package org.springframework.remoting.rmi;
/*    */ 
/*    */ import javax.naming.NamingException;
/*    */ import org.springframework.aop.framework.ProxyFactory;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ 
/*    */ public class JndiRmiProxyFactoryBean
/*    */   extends JndiRmiClientInterceptor
/*    */   implements FactoryBean<Object>, BeanClassLoaderAware
/*    */ {
/* 67 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*    */   
/*    */   private Object serviceProxy;
/*    */   
/*    */ 
/*    */   public void setBeanClassLoader(ClassLoader classLoader)
/*    */   {
/* 74 */     this.beanClassLoader = classLoader;
/*    */   }
/*    */   
/*    */   public void afterPropertiesSet() throws NamingException
/*    */   {
/* 79 */     super.afterPropertiesSet();
/* 80 */     if (getServiceInterface() == null) {
/* 81 */       throw new IllegalArgumentException("Property 'serviceInterface' is required");
/*    */     }
/* 83 */     this.serviceProxy = new ProxyFactory(getServiceInterface(), this).getProxy(this.beanClassLoader);
/*    */   }
/*    */   
/*    */ 
/*    */   public Object getObject()
/*    */   {
/* 89 */     return this.serviceProxy;
/*    */   }
/*    */   
/*    */   public Class<?> getObjectType()
/*    */   {
/* 94 */     return getServiceInterface();
/*    */   }
/*    */   
/*    */   public boolean isSingleton()
/*    */   {
/* 99 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\remoting\rmi\JndiRmiProxyFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */