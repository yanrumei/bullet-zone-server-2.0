/*    */ package org.springframework.boot.autoconfigure.mobile;
/*    */ 
/*    */ import org.springframework.core.Ordered;
/*    */ import org.springframework.mobile.device.view.LiteDeviceDelegatingViewResolver;
/*    */ import org.springframework.web.servlet.ViewResolver;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DeviceDelegatingViewResolverFactory
/*    */ {
/*    */   private final DeviceDelegatingViewResolverProperties properties;
/*    */   
/*    */   public DeviceDelegatingViewResolverFactory(DeviceDelegatingViewResolverProperties properties)
/*    */   {
/* 36 */     this.properties = properties;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LiteDeviceDelegatingViewResolver createViewResolver(ViewResolver delegate, int delegatingOrder)
/*    */   {
/* 48 */     LiteDeviceDelegatingViewResolver resolver = new LiteDeviceDelegatingViewResolver(delegate);
/*    */     
/* 50 */     resolver.setEnableFallback(this.properties.isEnableFallback());
/* 51 */     resolver.setNormalPrefix(this.properties.getNormalPrefix());
/* 52 */     resolver.setNormalSuffix(this.properties.getNormalSuffix());
/* 53 */     resolver.setMobilePrefix(this.properties.getMobilePrefix());
/* 54 */     resolver.setMobileSuffix(this.properties.getMobileSuffix());
/* 55 */     resolver.setTabletPrefix(this.properties.getTabletPrefix());
/* 56 */     resolver.setTabletSuffix(this.properties.getTabletSuffix());
/* 57 */     resolver.setOrder(delegatingOrder);
/* 58 */     return resolver;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public LiteDeviceDelegatingViewResolver createViewResolver(ViewResolver delegate)
/*    */   {
/* 70 */     if (!(delegate instanceof Ordered))
/*    */     {
/* 72 */       throw new IllegalStateException("ViewResolver " + delegate + " should implement " + Ordered.class.getName());
/*    */     }
/* 74 */     int delegateOrder = ((Ordered)delegate).getOrder();
/* 75 */     return createViewResolver(delegate, adjustOrder(delegateOrder));
/*    */   }
/*    */   
/*    */   private int adjustOrder(int order) {
/* 79 */     if (order == Integer.MIN_VALUE) {
/* 80 */       return Integer.MIN_VALUE;
/*    */     }
/*    */     
/*    */ 
/* 84 */     return order - 1;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\mobile\DeviceDelegatingViewResolverFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */