/*    */ package org.apache.catalina.loader;
/*    */ 
/*    */ import org.apache.catalina.LifecycleException;
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
/*    */ public class WebappClassLoader
/*    */   extends WebappClassLoaderBase
/*    */ {
/*    */   public WebappClassLoader() {}
/*    */   
/*    */   public WebappClassLoader(ClassLoader parent)
/*    */   {
/* 29 */     super(parent);
/*    */   }
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
/*    */   public WebappClassLoader copyWithoutTransformers()
/*    */   {
/* 51 */     WebappClassLoader result = new WebappClassLoader(getParent());
/*    */     
/* 53 */     super.copyStateWithoutTransformers(result);
/*    */     try
/*    */     {
/* 56 */       result.start();
/*    */     } catch (LifecycleException e) {
/* 58 */       throw new IllegalStateException(e);
/*    */     }
/*    */     
/* 61 */     return result;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Object getClassLoadingLock(String className)
/*    */   {
/* 71 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\loader\WebappClassLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */