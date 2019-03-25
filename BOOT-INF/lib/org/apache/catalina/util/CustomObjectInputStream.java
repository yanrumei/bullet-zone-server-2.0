/*     */ package org.apache.catalina.util;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InvalidClassException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectStreamClass;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.Collections;
/*     */ import java.util.Set;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.util.res.StringManager;
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
/*     */ public final class CustomObjectInputStream
/*     */   extends ObjectInputStream
/*     */ {
/*  44 */   private static final StringManager sm = StringManager.getManager(CustomObjectInputStream.class);
/*     */   
/*  46 */   private static final WeakHashMap<ClassLoader, Set<String>> reportedClassCache = new WeakHashMap();
/*     */   
/*     */ 
/*     */   private final ClassLoader classLoader;
/*     */   
/*     */ 
/*     */   private final Set<String> reportedClasses;
/*     */   
/*     */ 
/*     */   private final Log log;
/*     */   
/*     */ 
/*     */   private final Pattern allowedClassNamePattern;
/*     */   
/*     */ 
/*     */   private final String allowedClassNameFilter;
/*     */   
/*     */ 
/*     */   private final boolean warnOnFailure;
/*     */   
/*     */ 
/*     */ 
/*     */   public CustomObjectInputStream(InputStream stream, ClassLoader classLoader)
/*     */     throws IOException
/*     */   {
/*  71 */     this(stream, classLoader, null, null, false);
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
/*     */   public CustomObjectInputStream(InputStream stream, ClassLoader classLoader, Log log, Pattern allowedClassNamePattern, boolean warnOnFailure)
/*     */     throws IOException
/*     */   {
/*  95 */     super(stream);
/*  96 */     if ((log == null) && (allowedClassNamePattern != null) && (warnOnFailure))
/*     */     {
/*  98 */       throw new IllegalArgumentException(sm.getString("customObjectInputStream.logRequired"));
/*     */     }
/* 100 */     this.classLoader = classLoader;
/* 101 */     this.log = log;
/* 102 */     this.allowedClassNamePattern = allowedClassNamePattern;
/* 103 */     if (allowedClassNamePattern == null) {
/* 104 */       this.allowedClassNameFilter = null;
/*     */     } else {
/* 106 */       this.allowedClassNameFilter = allowedClassNamePattern.toString();
/*     */     }
/* 108 */     this.warnOnFailure = warnOnFailure;
/*     */     
/*     */     Set<String> reportedClasses;
/* 111 */     synchronized (reportedClassCache) {
/* 112 */       reportedClasses = (Set)reportedClassCache.get(classLoader); }
/*     */     Set<String> reportedClasses;
/* 114 */     if (reportedClasses == null) {
/* 115 */       reportedClasses = Collections.newSetFromMap(new ConcurrentHashMap());
/* 116 */       synchronized (reportedClassCache) {
/* 117 */         Object original = (Set)reportedClassCache.get(classLoader);
/* 118 */         if (original == null) {
/* 119 */           reportedClassCache.put(classLoader, reportedClasses);
/*     */         }
/*     */         else
/*     */         {
/* 123 */           reportedClasses = (Set<String>)original;
/*     */         }
/*     */       }
/*     */     }
/* 127 */     this.reportedClasses = reportedClasses;
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
/*     */   public Class<?> resolveClass(ObjectStreamClass classDesc)
/*     */     throws ClassNotFoundException, IOException
/*     */   {
/* 144 */     String name = classDesc.getName();
/* 145 */     if (this.allowedClassNamePattern != null) {
/* 146 */       boolean allowed = this.allowedClassNamePattern.matcher(name).matches();
/* 147 */       if (!allowed) {
/* 148 */         boolean doLog = (this.warnOnFailure) && (this.reportedClasses.add(name));
/* 149 */         String msg = sm.getString("customObjectInputStream.nomatch", new Object[] { name, this.allowedClassNameFilter });
/* 150 */         if (doLog) {
/* 151 */           this.log.warn(msg);
/* 152 */         } else if (this.log.isDebugEnabled()) {
/* 153 */           this.log.debug(msg);
/*     */         }
/* 155 */         throw new InvalidClassException(msg);
/*     */       }
/*     */     }
/*     */     try
/*     */     {
/* 160 */       return Class.forName(name, false, this.classLoader);
/*     */     }
/*     */     catch (ClassNotFoundException e) {
/*     */       try {
/* 164 */         return super.resolveClass(classDesc);
/*     */       }
/*     */       catch (ClassNotFoundException e2)
/*     */       {
/* 168 */         throw e;
/*     */       }
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
/*     */   protected Class<?> resolveProxyClass(String[] interfaces)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 183 */     Class<?>[] cinterfaces = new Class[interfaces.length];
/* 184 */     for (int i = 0; i < interfaces.length; i++) {
/* 185 */       cinterfaces[i] = this.classLoader.loadClass(interfaces[i]);
/*     */     }
/*     */     try
/*     */     {
/* 189 */       return Proxy.getProxyClass(this.classLoader, cinterfaces);
/*     */     } catch (IllegalArgumentException e) {
/* 191 */       throw new ClassNotFoundException(null, e);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalin\\util\CustomObjectInputStream.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */