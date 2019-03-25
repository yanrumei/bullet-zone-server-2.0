/*     */ package org.apache.juli.logging;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.Iterator;
/*     */ import java.util.ServiceLoader;
/*     */ import java.util.logging.LogManager;
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
/*     */ public class LogFactory
/*     */ {
/*  66 */   private static final LogFactory singleton = new LogFactory();
/*     */   
/*     */ 
/*     */ 
/*     */   private final Constructor<? extends Log> discoveredLogConstructor;
/*     */   
/*     */ 
/*     */ 
/*     */   private LogFactory()
/*     */   {
/*  76 */     ServiceLoader<Log> logLoader = ServiceLoader.load(Log.class);
/*  77 */     Constructor<? extends Log> m = null;
/*  78 */     Iterator localIterator = logLoader.iterator(); if (localIterator.hasNext()) { Log log = (Log)localIterator.next();
/*  79 */       Class<? extends Log> c = log.getClass();
/*     */       try {
/*  81 */         m = c.getConstructor(new Class[] { String.class });
/*     */       }
/*     */       catch (NoSuchMethodException|SecurityException e)
/*     */       {
/*  85 */         throw new Error(e);
/*     */       }
/*     */     }
/*  88 */     this.discoveredLogConstructor = m;
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
/*     */   public Log getInstance(String name)
/*     */     throws LogConfigurationException
/*     */   {
/* 116 */     if (this.discoveredLogConstructor == null) {
/* 117 */       return DirectJDKLog.getInstance(name);
/*     */     }
/*     */     try
/*     */     {
/* 121 */       return (Log)this.discoveredLogConstructor.newInstance(new Object[] { name });
/*     */     }
/*     */     catch (InstantiationException|IllegalAccessException|IllegalArgumentException|InvocationTargetException e) {
/* 124 */       throw new LogConfigurationException(e);
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
/*     */   public Log getInstance(Class<?> clazz)
/*     */     throws LogConfigurationException
/*     */   {
/* 141 */     return getInstance(clazz.getName());
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
/*     */   public static LogFactory getFactory()
/*     */     throws LogConfigurationException
/*     */   {
/* 179 */     return singleton;
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
/*     */   public static Log getLog(Class<?> clazz)
/*     */     throws LogConfigurationException
/*     */   {
/* 196 */     return getFactory().getInstance(clazz);
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
/*     */   public static Log getLog(String name)
/*     */     throws LogConfigurationException
/*     */   {
/* 216 */     return getFactory().getInstance(name);
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
/*     */   public static void release(ClassLoader classLoader)
/*     */   {
/* 233 */     if (!LogManager.getLogManager().getClass().getName().equals("java.util.logging.LogManager"))
/*     */     {
/* 235 */       LogManager.getLogManager().reset();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\juli\logging\LogFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */