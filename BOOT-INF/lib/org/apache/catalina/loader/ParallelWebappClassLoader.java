/*    */ package org.apache.catalina.loader;
/*    */ 
/*    */ import org.apache.catalina.LifecycleException;
/*    */ import org.apache.juli.logging.Log;
/*    */ import org.apache.juli.logging.LogFactory;
/*    */ import org.apache.tomcat.util.res.StringManager;
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
/*    */ public class ParallelWebappClassLoader
/*    */   extends WebappClassLoaderBase
/*    */ {
/* 25 */   private static final Log log = LogFactory.getLog(ParallelWebappClassLoader.class);
/*    */   
/*    */   static {
/* 28 */     boolean result = ClassLoader.registerAsParallelCapable();
/* 29 */     if (!result) {
/* 30 */       log.warn(sm.getString("webappClassLoaderParallel.registrationFailed"));
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ParallelWebappClassLoader(ClassLoader parent)
/*    */   {
/* 40 */     super(parent);
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
/*    */   public ParallelWebappClassLoader copyWithoutTransformers()
/*    */   {
/* 62 */     ParallelWebappClassLoader result = new ParallelWebappClassLoader(getParent());
/*    */     
/* 64 */     super.copyStateWithoutTransformers(result);
/*    */     try
/*    */     {
/* 67 */       result.start();
/*    */     } catch (LifecycleException e) {
/* 69 */       throw new IllegalStateException(e);
/*    */     }
/*    */     
/* 72 */     return result;
/*    */   }
/*    */   
/*    */   public ParallelWebappClassLoader() {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\loader\ParallelWebappClassLoader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */