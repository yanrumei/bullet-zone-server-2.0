/*    */ package org.springframework.boot;
/*    */ 
/*    */ import java.lang.reflect.Method;
/*    */ import org.springframework.context.ApplicationListener;
/*    */ import org.springframework.context.event.ContextRefreshedEvent;
/*    */ import org.springframework.util.ReflectionUtils;
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
/*    */ class ClearCachesApplicationListener
/*    */   implements ApplicationListener<ContextRefreshedEvent>
/*    */ {
/*    */   public void onApplicationEvent(ContextRefreshedEvent event)
/*    */   {
/* 35 */     ReflectionUtils.clearCache();
/* 36 */     clearClassLoaderCaches(Thread.currentThread().getContextClassLoader());
/*    */   }
/*    */   
/*    */   private void clearClassLoaderCaches(ClassLoader classLoader) {
/* 40 */     if (classLoader == null) {
/* 41 */       return;
/*    */     }
/*    */     try
/*    */     {
/* 45 */       Method clearCacheMethod = classLoader.getClass().getDeclaredMethod("clearCache", new Class[0]);
/* 46 */       clearCacheMethod.invoke(classLoader, new Object[0]);
/*    */     }
/*    */     catch (Exception localException) {}
/*    */     
/*    */ 
/* 51 */     clearClassLoaderCaches(classLoader.getParent());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\ClearCachesApplicationListener.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */