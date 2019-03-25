/*     */ package javax.el;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.UnsupportedEncodingException;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
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
/*     */ public abstract class ExpressionFactory
/*     */ {
/*  49 */   private static final boolean IS_SECURITY_ENABLED = System.getSecurityManager() != null;
/*     */   
/*     */ 
/*     */   private static final String SERVICE_RESOURCE_NAME = "META-INF/services/javax.el.ExpressionFactory";
/*     */   
/*     */   private static final String PROPERTY_NAME = "javax.el.ExpressionFactory";
/*     */   
/*     */   private static final String PROPERTY_FILE;
/*     */   
/*  58 */   private static final CacheValue nullTcclFactory = new CacheValue();
/*  59 */   private static final ConcurrentMap<CacheKey, CacheValue> factoryCache = new ConcurrentHashMap();
/*     */   
/*     */   static
/*     */   {
/*  63 */     if (IS_SECURITY_ENABLED) {
/*  64 */       PROPERTY_FILE = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public String run()
/*     */         {
/*  68 */           return System.getProperty("java.home") + File.separator + "lib" + File.separator + "el.properties";
/*     */         }
/*     */         
/*     */ 
/*     */       });
/*     */     }
/*     */     else {
/*  75 */       PROPERTY_FILE = System.getProperty("java.home") + File.separator + "lib" + File.separator + "el.properties";
/*     */     }
/*     */   }
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
/*     */   public static ExpressionFactory newInstance()
/*     */   {
/*  93 */     return newInstance(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static ExpressionFactory newInstance(Properties properties)
/*     */   {
/* 104 */     ExpressionFactory result = null;
/*     */     
/* 106 */     ClassLoader tccl = Thread.currentThread().getContextClassLoader();
/*     */     
/*     */     CacheValue cacheValue;
/*     */     
/*     */     CacheValue cacheValue;
/* 111 */     if (tccl == null) {
/* 112 */       cacheValue = nullTcclFactory;
/*     */     } else {
/* 114 */       CacheKey key = new CacheKey(tccl);
/* 115 */       cacheValue = (CacheValue)factoryCache.get(key);
/* 116 */       if (cacheValue == null) {
/* 117 */         CacheValue newCacheValue = new CacheValue();
/* 118 */         cacheValue = (CacheValue)factoryCache.putIfAbsent(key, newCacheValue);
/* 119 */         if (cacheValue == null) {
/* 120 */           cacheValue = newCacheValue;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 125 */     Lock readLock = cacheValue.getLock().readLock();
/* 126 */     readLock.lock();
/*     */     try {
/* 128 */       clazz = cacheValue.getFactoryClass();
/*     */     } finally { Class<?> clazz;
/* 130 */       readLock.unlock();
/*     */     }
/*     */     Class<?> clazz;
/* 133 */     if (clazz == null) {
/* 134 */       String className = null;
/*     */       try {
/* 136 */         Lock writeLock = cacheValue.getLock().writeLock();
/* 137 */         writeLock.lock();
/*     */         try {
/* 139 */           className = cacheValue.getFactoryClassName();
/* 140 */           if (className == null) {
/* 141 */             className = discoverClassName(tccl);
/* 142 */             cacheValue.setFactoryClassName(className);
/*     */           }
/* 144 */           if (tccl == null) {
/* 145 */             clazz = Class.forName(className);
/*     */           } else {
/* 147 */             clazz = tccl.loadClass(className);
/*     */           }
/* 149 */           cacheValue.setFactoryClass(clazz);
/*     */         } finally {
/* 151 */           writeLock.unlock();
/*     */         }
/*     */       } catch (ClassNotFoundException e) {
/* 154 */         throw new ELException("Unable to find ExpressionFactory of type: " + className, e);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 161 */       Constructor<?> constructor = null;
/*     */       
/* 163 */       if (properties != null) {
/*     */         try {
/* 165 */           constructor = clazz.getConstructor(new Class[] { Properties.class });
/*     */         } catch (SecurityException se) {
/* 167 */           throw new ELException(se);
/*     */         }
/*     */         catch (NoSuchMethodException localNoSuchMethodException1) {}
/*     */       }
/*     */       
/*     */ 
/* 173 */       if (constructor == null) {
/* 174 */         result = (ExpressionFactory)clazz.getConstructor(new Class[0]).newInstance(new Object[0]);
/*     */       }
/*     */       else {
/* 177 */         result = (ExpressionFactory)constructor.newInstance(new Object[] { properties });
/*     */       }
/*     */       
/*     */     }
/*     */     catch (InstantiationException|IllegalAccessException|IllegalArgumentException|NoSuchMethodException e)
/*     */     {
/* 183 */       throw new ELException("Unable to create ExpressionFactory of type: " + clazz.getName(), e);
/*     */     }
/*     */     catch (InvocationTargetException e) {
/* 186 */       Throwable cause = e.getCause();
/* 187 */       Util.handleThrowable(cause);
/*     */       
/* 189 */       throw new ELException("Unable to create ExpressionFactory of type: " + clazz.getName(), e);
/*     */     }
/*     */     
/*     */ 
/* 193 */     return result;
/*     */   }
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
/*     */   public ELResolver getStreamELResolver()
/*     */   {
/* 257 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, Method> getInitFunctionMap()
/*     */   {
/* 266 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class CacheKey
/*     */   {
/*     */     private final int hash;
/*     */     
/*     */     private final WeakReference<ClassLoader> ref;
/*     */     
/*     */ 
/*     */     public CacheKey(ClassLoader cl)
/*     */     {
/* 279 */       this.hash = cl.hashCode();
/* 280 */       this.ref = new WeakReference(cl);
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 285 */       return this.hash;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 290 */       if (obj == this) {
/* 291 */         return true;
/*     */       }
/* 293 */       if (!(obj instanceof CacheKey)) {
/* 294 */         return false;
/*     */       }
/* 296 */       ClassLoader thisCl = (ClassLoader)this.ref.get();
/* 297 */       if (thisCl == null) {
/* 298 */         return false;
/*     */       }
/* 300 */       return thisCl == ((CacheKey)obj).ref.get();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CacheValue {
/* 305 */     private final ReadWriteLock lock = new ReentrantReadWriteLock();
/*     */     
/*     */     private String className;
/*     */     
/*     */     private WeakReference<Class<?>> ref;
/*     */     
/*     */     public ReadWriteLock getLock()
/*     */     {
/* 313 */       return this.lock;
/*     */     }
/*     */     
/*     */     public String getFactoryClassName() {
/* 317 */       return this.className;
/*     */     }
/*     */     
/*     */     public void setFactoryClassName(String className) {
/* 321 */       this.className = className;
/*     */     }
/*     */     
/*     */     public Class<?> getFactoryClass() {
/* 325 */       return this.ref != null ? (Class)this.ref.get() : null;
/*     */     }
/*     */     
/*     */     public void setFactoryClass(Class<?> clazz) {
/* 329 */       this.ref = new WeakReference(clazz);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String discoverClassName(ClassLoader tccl)
/*     */   {
/* 341 */     String className = null;
/*     */     
/*     */ 
/* 344 */     className = getClassNameServices(tccl);
/* 345 */     if (className == null) {
/* 346 */       if (IS_SECURITY_ENABLED) {
/* 347 */         className = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public String run()
/*     */           {
/* 351 */             return ExpressionFactory.access$000();
/*     */           }
/*     */           
/*     */ 
/*     */         });
/*     */       } else {
/* 357 */         className = getClassNameJreDir();
/*     */       }
/*     */     }
/* 360 */     if (className == null) {
/* 361 */       if (IS_SECURITY_ENABLED) {
/* 362 */         className = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public String run()
/*     */           {
/* 366 */             return ExpressionFactory.access$100();
/*     */           }
/*     */           
/*     */ 
/*     */         });
/*     */       } else {
/* 372 */         className = getClassNameSysProp();
/*     */       }
/*     */     }
/* 375 */     if (className == null)
/*     */     {
/* 377 */       className = "org.apache.el.ExpressionFactoryImpl";
/*     */     }
/* 379 */     return className;
/*     */   }
/*     */   
/*     */   private static String getClassNameServices(ClassLoader tccl) {
/* 383 */     InputStream is = null;
/*     */     
/* 385 */     if (tccl == null) {
/* 386 */       is = ClassLoader.getSystemResourceAsStream("META-INF/services/javax.el.ExpressionFactory");
/*     */     } else {
/* 388 */       is = tccl.getResourceAsStream("META-INF/services/javax.el.ExpressionFactory");
/*     */     }
/*     */     
/* 391 */     if (is != null) {
/* 392 */       String line = null;
/* 393 */       try { InputStreamReader isr = new InputStreamReader(is, "UTF-8");Throwable localThrowable8 = null;
/* 394 */         try { BufferedReader br = new BufferedReader(isr);Throwable localThrowable9 = null;
/* 395 */           try { line = br.readLine();
/* 396 */             if ((line != null) && (line.trim().length() > 0)) {
/* 397 */               String str1 = line.trim();
/*     */               
/* 399 */               if (br != null) if (localThrowable9 != null) try { br.close(); } catch (Throwable localThrowable) { localThrowable9.addSuppressed(localThrowable); } else br.close(); if (isr != null) if (localThrowable8 != null) try { isr.close(); } catch (Throwable localThrowable1) { localThrowable8.addSuppressed(localThrowable1); } else isr.close();
/* 397 */               return str1;
/*     */             }
/*     */           }
/*     */           catch (Throwable localThrowable11)
/*     */           {
/* 393 */             localThrowable9 = localThrowable11;throw localThrowable11; } finally {} } catch (Throwable localThrowable6) { localThrowable8 = localThrowable6;throw localThrowable6;
/*     */ 
/*     */         }
/*     */         finally
/*     */         {
/*     */ 
/* 399 */           if (isr != null) { if (localThrowable8 != null) try { isr.close(); } catch (Throwable localThrowable7) { localThrowable8.addSuppressed(localThrowable7); } else { isr.close();
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 412 */         return null;
/*     */       }
/*     */       catch (UnsupportedEncodingException localUnsupportedEncodingException) {}catch (IOException e)
/*     */       {
/* 403 */         throw new ELException("Failed to read META-INF/services/javax.el.ExpressionFactory", e);
/*     */       }
/*     */       finally {
/*     */         try {
/* 407 */           is.close();
/*     */         }
/*     */         catch (IOException localIOException4) {}
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static String getClassNameJreDir()
/*     */   {
/* 416 */     File file = new File(PROPERTY_FILE);
/* 417 */     if (file.canRead()) {
/* 418 */       try { InputStream is = new FileInputStream(file);Throwable localThrowable4 = null;
/* 419 */         try { Properties props = new Properties();
/* 420 */           props.load(is);
/* 421 */           String value = props.getProperty("javax.el.ExpressionFactory");
/* 422 */           if ((value != null) && (value.trim().length() > 0)) {
/* 423 */             return value.trim();
/*     */           }
/*     */         }
/*     */         catch (Throwable localThrowable2)
/*     */         {
/* 418 */           localThrowable4 = localThrowable2;throw localThrowable2;
/*     */ 
/*     */ 
/*     */         }
/*     */         finally
/*     */         {
/*     */ 
/* 425 */           if (is != null) if (localThrowable4 != null) try { is.close(); } catch (Throwable localThrowable3) { localThrowable4.addSuppressed(localThrowable3); } else is.close();
/*     */         }
/*     */       } catch (FileNotFoundException localFileNotFoundException) {}catch (IOException e) {
/* 428 */         throw new ELException("Failed to read " + PROPERTY_FILE, e);
/*     */       }
/*     */     }
/* 431 */     return null;
/*     */   }
/*     */   
/*     */   private static final String getClassNameSysProp() {
/* 435 */     String value = System.getProperty("javax.el.ExpressionFactory");
/* 436 */     if ((value != null) && (value.trim().length() > 0)) {
/* 437 */       return value.trim();
/*     */     }
/* 439 */     return null;
/*     */   }
/*     */   
/*     */   public abstract ValueExpression createValueExpression(ELContext paramELContext, String paramString, Class<?> paramClass);
/*     */   
/*     */   public abstract ValueExpression createValueExpression(Object paramObject, Class<?> paramClass);
/*     */   
/*     */   public abstract MethodExpression createMethodExpression(ELContext paramELContext, String paramString, Class<?> paramClass, Class<?>[] paramArrayOfClass);
/*     */   
/*     */   public abstract Object coerceToType(Object paramObject, Class<?> paramClass);
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\ExpressionFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */