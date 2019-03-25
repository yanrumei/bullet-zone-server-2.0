/*    */ package org.springframework.instrument.classloading;
/*    */ 
/*    */ import java.lang.instrument.ClassFileTransformer;
/*    */ import org.springframework.util.Assert;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public class SimpleLoadTimeWeaver
/*    */   implements LoadTimeWeaver
/*    */ {
/*    */   private final SimpleInstrumentableClassLoader classLoader;
/*    */   
/*    */   public SimpleLoadTimeWeaver()
/*    */   {
/* 50 */     this.classLoader = new SimpleInstrumentableClassLoader(ClassUtils.getDefaultClassLoader());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public SimpleLoadTimeWeaver(SimpleInstrumentableClassLoader classLoader)
/*    */   {
/* 60 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/* 61 */     this.classLoader = classLoader;
/*    */   }
/*    */   
/*    */ 
/*    */   public void addTransformer(ClassFileTransformer transformer)
/*    */   {
/* 67 */     this.classLoader.addTransformer(transformer);
/*    */   }
/*    */   
/*    */   public ClassLoader getInstrumentableClassLoader()
/*    */   {
/* 72 */     return this.classLoader;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public ClassLoader getThrowawayClassLoader()
/*    */   {
/* 80 */     return new SimpleThrowawayClassLoader(getInstrumentableClassLoader());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\instrument\classloading\SimpleLoadTimeWeaver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */