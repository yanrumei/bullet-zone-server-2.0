/*    */ package org.hibernate.validator.internal.util.classhierarchy;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Filters
/*    */ {
/* 17 */   private static final Filter PROXY_FILTER = new WeldProxyFilter(null);
/* 18 */   private static final Filter INTERFACES_FILTER = new InterfacesFilter(null);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Filter excludeInterfaces()
/*    */   {
/* 30 */     return INTERFACES_FILTER;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static Filter excludeProxies()
/*    */   {
/* 39 */     return PROXY_FILTER;
/*    */   }
/*    */   
/*    */   private static class InterfacesFilter implements Filter
/*    */   {
/*    */     public boolean accepts(Class<?> clazz)
/*    */     {
/* 46 */       return !clazz.isInterface();
/*    */     }
/*    */   }
/*    */   
/*    */   private static class WeldProxyFilter implements Filter
/*    */   {
/*    */     private static final String WELD_PROXY_INTERFACE_NAME = "org.jboss.weld.bean.proxy.ProxyObject";
/*    */     
/*    */     public boolean accepts(Class<?> clazz)
/*    */     {
/* 56 */       return !isWeldProxy(clazz);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     private boolean isWeldProxy(Class<?> clazz)
/*    */     {
/* 70 */       for (Class<?> implementedInterface : clazz.getInterfaces()) {
/* 71 */         if (implementedInterface.getName().equals("org.jboss.weld.bean.proxy.ProxyObject")) {
/* 72 */           return true;
/*    */         }
/*    */       }
/*    */       
/* 76 */       return false;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\classhierarchy\Filters.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */