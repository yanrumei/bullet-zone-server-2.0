/*     */ package org.springframework.instrument.classloading.weblogic;
/*     */ 
/*     */ import java.lang.instrument.ClassFileTransformer;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import org.springframework.util.Assert;
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
/*     */ class WebLogicClassLoaderAdapter
/*     */ {
/*     */   private static final String GENERIC_CLASS_LOADER_NAME = "weblogic.utils.classloaders.GenericClassLoader";
/*     */   private static final String CLASS_PRE_PROCESSOR_NAME = "weblogic.utils.classloaders.ClassPreProcessor";
/*     */   private final ClassLoader classLoader;
/*     */   private final Class<?> wlPreProcessorClass;
/*     */   private final Method addPreProcessorMethod;
/*     */   private final Method getClassFinderMethod;
/*     */   private final Method getParentMethod;
/*     */   private final Constructor<?> wlGenericClassLoaderConstructor;
/*     */   
/*     */   public WebLogicClassLoaderAdapter(ClassLoader classLoader)
/*     */   {
/*     */     try
/*     */     {
/*  60 */       Class<?> wlGenericClassLoaderClass = classLoader.loadClass("weblogic.utils.classloaders.GenericClassLoader");
/*  61 */       this.wlPreProcessorClass = classLoader.loadClass("weblogic.utils.classloaders.ClassPreProcessor");
/*  62 */       this.addPreProcessorMethod = classLoader.getClass().getMethod("addInstanceClassPreProcessor", new Class[] { this.wlPreProcessorClass });
/*     */       
/*  64 */       this.getClassFinderMethod = classLoader.getClass().getMethod("getClassFinder", new Class[0]);
/*  65 */       this.getParentMethod = classLoader.getClass().getMethod("getParent", new Class[0]);
/*  66 */       this.wlGenericClassLoaderConstructor = wlGenericClassLoaderClass.getConstructor(new Class[] {this.getClassFinderMethod
/*  67 */         .getReturnType(), ClassLoader.class });
/*     */     }
/*     */     catch (Throwable ex) {
/*  70 */       throw new IllegalStateException("Could not initialize WebLogic LoadTimeWeaver because WebLogic 10 API classes are not available", ex);
/*     */     }
/*     */     Class<?> wlGenericClassLoaderClass;
/*  73 */     if (!wlGenericClassLoaderClass.isInstance(classLoader))
/*     */     {
/*  75 */       throw new IllegalArgumentException("ClassLoader must be an instance of [" + wlGenericClassLoaderClass.getName() + "]: " + classLoader);
/*     */     }
/*  77 */     this.classLoader = classLoader;
/*     */   }
/*     */   
/*     */   public void addTransformer(ClassFileTransformer transformer)
/*     */   {
/*  82 */     Assert.notNull(transformer, "ClassFileTransformer must not be null");
/*     */     try {
/*  84 */       InvocationHandler adapter = new WebLogicClassPreProcessorAdapter(transformer, this.classLoader);
/*  85 */       Object adapterInstance = Proxy.newProxyInstance(this.wlPreProcessorClass.getClassLoader(), new Class[] { this.wlPreProcessorClass }, adapter);
/*     */       
/*  87 */       this.addPreProcessorMethod.invoke(this.classLoader, new Object[] { adapterInstance });
/*     */     }
/*     */     catch (InvocationTargetException ex) {
/*  90 */       throw new IllegalStateException("WebLogic addInstanceClassPreProcessor method threw exception", ex.getCause());
/*     */     }
/*     */     catch (Throwable ex) {
/*  93 */       throw new IllegalStateException("Could not invoke WebLogic addInstanceClassPreProcessor method", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   public ClassLoader getClassLoader() {
/*  98 */     return this.classLoader;
/*     */   }
/*     */   
/*     */   public ClassLoader getThrowawayClassLoader() {
/*     */     try {
/* 103 */       Object classFinder = this.getClassFinderMethod.invoke(this.classLoader, new Object[0]);
/* 104 */       Object parent = this.getParentMethod.invoke(this.classLoader, new Object[0]);
/*     */       
/* 106 */       return (ClassLoader)this.wlGenericClassLoaderConstructor.newInstance(new Object[] { classFinder, parent });
/*     */     }
/*     */     catch (InvocationTargetException ex) {
/* 109 */       throw new IllegalStateException("WebLogic GenericClassLoader constructor failed", ex.getCause());
/*     */     }
/*     */     catch (Throwable ex) {
/* 112 */       throw new IllegalStateException("Could not construct WebLogic GenericClassLoader", ex);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\instrument\classloading\weblogic\WebLogicClassLoaderAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */