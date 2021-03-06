/*    */ package org.springframework.jmx.support;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import java.lang.reflect.Method;
/*    */ import javax.management.MBeanServer;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.jmx.MBeanServerNotFoundException;
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
/*    */ public class WebSphereMBeanServerFactoryBean
/*    */   implements FactoryBean<MBeanServer>, InitializingBean
/*    */ {
/*    */   private static final String ADMIN_SERVICE_FACTORY_CLASS = "com.ibm.websphere.management.AdminServiceFactory";
/*    */   private static final String GET_MBEAN_FACTORY_METHOD = "getMBeanFactory";
/*    */   private static final String GET_MBEAN_SERVER_METHOD = "getMBeanServer";
/*    */   private MBeanServer mbeanServer;
/*    */   
/*    */   public void afterPropertiesSet()
/*    */     throws MBeanServerNotFoundException
/*    */   {
/*    */     try
/*    */     {
/* 64 */       Class<?> adminServiceClass = getClass().getClassLoader().loadClass("com.ibm.websphere.management.AdminServiceFactory");
/* 65 */       Method getMBeanFactoryMethod = adminServiceClass.getMethod("getMBeanFactory", new Class[0]);
/* 66 */       Object mbeanFactory = getMBeanFactoryMethod.invoke(null, new Object[0]);
/* 67 */       Method getMBeanServerMethod = mbeanFactory.getClass().getMethod("getMBeanServer", new Class[0]);
/* 68 */       this.mbeanServer = ((MBeanServer)getMBeanServerMethod.invoke(mbeanFactory, new Object[0]));
/*    */     }
/*    */     catch (ClassNotFoundException ex) {
/* 71 */       throw new MBeanServerNotFoundException("Could not find WebSphere's AdminServiceFactory class", ex);
/*    */     }
/*    */     catch (InvocationTargetException ex)
/*    */     {
/* 75 */       throw new MBeanServerNotFoundException("WebSphere's AdminServiceFactory.getMBeanFactory/getMBeanServer method failed", ex.getTargetException());
/*    */     }
/*    */     catch (Exception ex) {
/* 78 */       throw new MBeanServerNotFoundException("Could not access WebSphere's AdminServiceFactory.getMBeanFactory/getMBeanServer method", ex);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public MBeanServer getObject()
/*    */   {
/* 86 */     return this.mbeanServer;
/*    */   }
/*    */   
/*    */   public Class<? extends MBeanServer> getObjectType()
/*    */   {
/* 91 */     return this.mbeanServer != null ? this.mbeanServer.getClass() : MBeanServer.class;
/*    */   }
/*    */   
/*    */   public boolean isSingleton()
/*    */   {
/* 96 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\support\WebSphereMBeanServerFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */