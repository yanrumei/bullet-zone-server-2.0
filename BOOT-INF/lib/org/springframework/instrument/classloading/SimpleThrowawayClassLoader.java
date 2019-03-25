/*    */ package org.springframework.instrument.classloading;
/*    */ 
/*    */ import org.springframework.core.OverridingClassLoader;
/*    */ import org.springframework.lang.UsesJava7;
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
/*    */ @UsesJava7
/*    */ public class SimpleThrowawayClassLoader
/*    */   extends OverridingClassLoader
/*    */ {
/*    */   static
/*    */   {
/* 34 */     if (parallelCapableClassLoaderAvailable) {
/* 35 */       ClassLoader.registerAsParallelCapable();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SimpleThrowawayClassLoader(ClassLoader parent)
/*    */   {
/* 45 */     super(parent);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\instrument\classloading\SimpleThrowawayClassLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */