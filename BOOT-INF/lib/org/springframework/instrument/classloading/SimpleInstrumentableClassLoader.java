/*    */ package org.springframework.instrument.classloading;
/*    */ 
/*    */ import java.lang.instrument.ClassFileTransformer;
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
/*    */ public class SimpleInstrumentableClassLoader
/*    */   extends OverridingClassLoader
/*    */ {
/*    */   private final WeavingTransformer weavingTransformer;
/*    */   
/*    */   static
/*    */   {
/* 37 */     if (parallelCapableClassLoaderAvailable) {
/* 38 */       ClassLoader.registerAsParallelCapable();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SimpleInstrumentableClassLoader(ClassLoader parent)
/*    */   {
/* 51 */     super(parent);
/* 52 */     this.weavingTransformer = new WeavingTransformer(parent);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void addTransformer(ClassFileTransformer transformer)
/*    */   {
/* 61 */     this.weavingTransformer.addTransformer(transformer);
/*    */   }
/*    */   
/*    */ 
/*    */   protected byte[] transformIfNecessary(String name, byte[] bytes)
/*    */   {
/* 67 */     return this.weavingTransformer.transformIfNecessary(name, bytes);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\instrument\classloading\SimpleInstrumentableClassLoader.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */