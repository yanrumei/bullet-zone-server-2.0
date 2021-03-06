/*    */ package org.springframework.remoting.jaxws;
/*    */ 
/*    */ import javax.xml.ws.BindingProvider;
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
/*    */ public class JaxWsPortProxyFactoryBean
/*    */   extends JaxWsPortClientInterceptor
/*    */   implements FactoryBean<Object>
/*    */ {
/*    */   private Object serviceProxy;
/*    */   
/*    */   public void afterPropertiesSet()
/*    */   {
/* 42 */     super.afterPropertiesSet();
/*    */     
/*    */ 
/* 45 */     ProxyFactory pf = new ProxyFactory();
/* 46 */     pf.addInterface(getServiceInterface());
/* 47 */     pf.addInterface(BindingProvider.class);
/* 48 */     pf.addAdvice(this);
/* 49 */     this.serviceProxy = pf.getProxy(getBeanClassLoader());
/*    */   }
/*    */   
/*    */ 
/*    */   public Object getObject()
/*    */   {
/* 55 */     return this.serviceProxy;
/*    */   }
/*    */   
/*    */   public Class<?> getObjectType()
/*    */   {
/* 60 */     return getServiceInterface();
/*    */   }
/*    */   
/*    */   public boolean isSingleton()
/*    */   {
/* 65 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\remoting\jaxws\JaxWsPortProxyFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */