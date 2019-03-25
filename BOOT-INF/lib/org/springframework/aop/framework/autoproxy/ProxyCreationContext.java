/*    */ package org.springframework.aop.framework.autoproxy;
/*    */ 
/*    */ import org.springframework.core.NamedThreadLocal;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProxyCreationContext
/*    */ {
/* 32 */   private static final ThreadLocal<String> currentProxiedBeanName = new NamedThreadLocal("Name of currently proxied bean");
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static String getCurrentProxiedBeanName()
/*    */   {
/* 41 */     return (String)currentProxiedBeanName.get();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   static void setCurrentProxiedBeanName(String beanName)
/*    */   {
/* 49 */     if (beanName != null) {
/* 50 */       currentProxiedBeanName.set(beanName);
/*    */     }
/*    */     else {
/* 53 */       currentProxiedBeanName.remove();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\autoproxy\ProxyCreationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */