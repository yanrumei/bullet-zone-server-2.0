/*     */ package org.springframework.instrument.classloading.glassfish;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import org.springframework.core.OverridingClassLoader;
/*     */ import org.springframework.instrument.classloading.LoadTimeWeaver;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GlassFishLoadTimeWeaver
/*     */   implements LoadTimeWeaver
/*     */ {
/*     */   private static final String INSTRUMENTABLE_LOADER_CLASS_NAME = "org.glassfish.api.deployment.InstrumentableClassLoader";
/*     */   private final ClassLoader classLoader;
/*     */   private final Method addTransformerMethod;
/*     */   private final Method copyMethod;
/*     */   
/*     */   public GlassFishLoadTimeWeaver()
/*     */   {
/*  57 */     this(ClassUtils.getDefaultClassLoader());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GlassFishLoadTimeWeaver(ClassLoader classLoader)
/*     */   {
/*  66 */     Assert.notNull(classLoader, "ClassLoader must not be null");
/*     */     
/*     */     try
/*     */     {
/*  70 */       Class<?> instrumentableLoaderClass = classLoader.loadClass("org.glassfish.api.deployment.InstrumentableClassLoader");
/*  71 */       this.addTransformerMethod = instrumentableLoaderClass.getMethod("addTransformer", new Class[] { ClassFileTransformer.class });
/*  72 */       this.copyMethod = instrumentableLoaderClass.getMethod("copy", new Class[0]);
/*     */     }
/*     */     catch (Throwable ex) {
/*  75 */       throw new IllegalStateException("Could not initialize GlassFishLoadTimeWeaver because GlassFish API classes are not available", ex);
/*     */     }
/*     */     
/*     */     Class<?> instrumentableLoaderClass;
/*  79 */     ClassLoader clazzLoader = null;
/*     */     
/*     */ 
/*  82 */     for (ClassLoader cl = classLoader; (cl != null) && (clazzLoader == null); cl = cl.getParent()) {
/*  83 */       if (instrumentableLoaderClass.isInstance(cl)) {
/*  84 */         clazzLoader = cl;
/*     */       }
/*     */     }
/*     */     
/*  88 */     if (clazzLoader == null)
/*     */     {
/*  90 */       throw new IllegalArgumentException(classLoader + " and its parents are not suitable ClassLoaders: A [" + instrumentableLoaderClass.getName() + "] implementation is required.");
/*     */     }
/*     */     
/*  93 */     this.classLoader = clazzLoader;
/*     */   }
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer)
/*     */   {
/*     */     try
/*     */     {
/* 100 */       this.addTransformerMethod.invoke(this.classLoader, new Object[] { transformer });
/*     */     }
/*     */     catch (InvocationTargetException ex) {
/* 103 */       throw new IllegalStateException("GlassFish addTransformer method threw exception", ex.getCause());
/*     */     }
/*     */     catch (Throwable ex) {
/* 106 */       throw new IllegalStateException("Could not invoke GlassFish addTransformer method", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public ClassLoader getInstrumentableClassLoader()
/*     */   {
/* 112 */     return this.classLoader;
/*     */   }
/*     */   
/*     */   public ClassLoader getThrowawayClassLoader()
/*     */   {
/*     */     try {
/* 118 */       return new OverridingClassLoader(this.classLoader, (ClassLoader)this.copyMethod.invoke(this.classLoader, new Object[0]));
/*     */     }
/*     */     catch (InvocationTargetException ex) {
/* 121 */       throw new IllegalStateException("GlassFish copy method threw exception", ex.getCause());
/*     */     }
/*     */     catch (Throwable ex) {
/* 124 */       throw new IllegalStateException("Could not invoke GlassFish copy method", ex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\instrument\classloading\glassfish\GlassFishLoadTimeWeaver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */