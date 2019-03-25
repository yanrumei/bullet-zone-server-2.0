/*    */ package org.springframework.beans.factory.serviceloader;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.ServiceLoader;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
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
/*    */ public class ServiceFactoryBean
/*    */   extends AbstractServiceLoaderBasedFactoryBean
/*    */   implements BeanClassLoaderAware
/*    */ {
/*    */   protected Object getObjectToExpose(ServiceLoader<?> serviceLoader)
/*    */   {
/* 37 */     Iterator<?> it = serviceLoader.iterator();
/* 38 */     if (!it.hasNext())
/*    */     {
/* 40 */       throw new IllegalStateException("ServiceLoader could not find service for type [" + getServiceType() + "]");
/*    */     }
/* 42 */     return it.next();
/*    */   }
/*    */   
/*    */   public Class<?> getObjectType()
/*    */   {
/* 47 */     return getServiceType();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\serviceloader\ServiceFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */