/*    */ package org.springframework.beans.factory.serviceloader;
/*    */ 
/*    */ import java.util.LinkedList;
/*    */ import java.util.List;
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
/*    */ public class ServiceListFactoryBean
/*    */   extends AbstractServiceLoaderBasedFactoryBean
/*    */   implements BeanClassLoaderAware
/*    */ {
/*    */   protected Object getObjectToExpose(ServiceLoader<?> serviceLoader)
/*    */   {
/* 38 */     List<Object> result = new LinkedList();
/* 39 */     for (Object loaderObject : serviceLoader) {
/* 40 */       result.add(loaderObject);
/*    */     }
/* 42 */     return result;
/*    */   }
/*    */   
/*    */   public Class<?> getObjectType()
/*    */   {
/* 47 */     return List.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\serviceloader\ServiceListFactoryBean.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */