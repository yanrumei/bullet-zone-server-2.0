/*     */ package org.hibernate.validator.internal.util.privilegedactions;
/*     */ 
/*     */ import java.security.PrivilegedAction;
/*     */ import org.hibernate.validator.HibernateValidator;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
/*     */ public final class LoadClass
/*     */   implements PrivilegedAction<Class<?>>
/*     */ {
/*  31 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */   private static final String HIBERNATE_VALIDATOR_CLASS_NAME = "org.hibernate.validator";
/*     */   
/*     */   private final String className;
/*     */   
/*     */   private final ClassLoader classLoader;
/*     */   
/*     */   private final boolean fallbackOnTCCL;
/*     */   
/*     */ 
/*     */   public static LoadClass action(String className, ClassLoader classLoader)
/*     */   {
/*  45 */     return new LoadClass(className, classLoader, true);
/*     */   }
/*     */   
/*     */   public static LoadClass action(String className, ClassLoader classLoader, boolean fallbackOnTCCL) {
/*  49 */     return new LoadClass(className, classLoader, fallbackOnTCCL);
/*     */   }
/*     */   
/*     */   private LoadClass(String className, ClassLoader classLoader, boolean fallbackOnTCCL) {
/*  53 */     this.className = className;
/*  54 */     this.classLoader = classLoader;
/*  55 */     this.fallbackOnTCCL = fallbackOnTCCL;
/*     */   }
/*     */   
/*     */   public Class<?> run()
/*     */   {
/*  60 */     if (this.className.startsWith("org.hibernate.validator")) {
/*  61 */       return loadClassInValidatorNameSpace();
/*     */     }
/*     */     
/*  64 */     return loadNonValidatorClass();
/*     */   }
/*     */   
/*     */ 
/*     */   private Class<?> loadClassInValidatorNameSpace()
/*     */   {
/*  70 */     ClassLoader loader = HibernateValidator.class.getClassLoader();
/*     */     Exception exception;
/*     */     try {
/*  73 */       return Class.forName(this.className, true, HibernateValidator.class.getClassLoader());
/*     */     }
/*     */     catch (ClassNotFoundException e) {
/*  76 */       exception = e;
/*     */     } catch (RuntimeException e) {
/*     */       Exception exception;
/*  79 */       exception = e;
/*     */     }
/*  81 */     if (this.fallbackOnTCCL) {
/*  82 */       ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
/*  83 */       if (contextClassLoader != null) {
/*     */         try {
/*  85 */           return Class.forName(this.className, false, contextClassLoader);
/*     */         }
/*     */         catch (ClassNotFoundException e) {
/*  88 */           throw log.getUnableToLoadClassException(this.className, contextClassLoader, e);
/*     */         }
/*     */       }
/*     */       
/*  92 */       throw log.getUnableToLoadClassException(this.className, loader, exception);
/*     */     }
/*     */     
/*     */ 
/*  96 */     throw log.getUnableToLoadClassException(this.className, loader, exception);
/*     */   }
/*     */   
/*     */   private Class<?> loadNonValidatorClass()
/*     */   {
/* 101 */     Exception exception = null;
/*     */     try {
/* 103 */       if (this.classLoader != null) {
/* 104 */         return Class.forName(this.className, false, this.classLoader);
/*     */       }
/*     */     }
/*     */     catch (ClassNotFoundException e) {
/* 108 */       exception = e;
/*     */     }
/*     */     catch (RuntimeException e) {
/* 111 */       exception = e;
/*     */     }
/* 113 */     if (this.fallbackOnTCCL) {
/*     */       try {
/* 115 */         ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
/* 116 */         if (contextClassLoader != null) {
/* 117 */           return Class.forName(this.className, false, contextClassLoader);
/*     */         }
/*     */       }
/*     */       catch (ClassNotFoundException localClassNotFoundException1) {}catch (RuntimeException localRuntimeException1) {}
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 127 */       ClassLoader loader = LoadClass.class.getClassLoader();
/*     */       try {
/* 129 */         return Class.forName(this.className, true, loader);
/*     */       }
/*     */       catch (ClassNotFoundException e) {
/* 132 */         throw log.getUnableToLoadClassException(this.className, loader, e);
/*     */       }
/*     */     }
/*     */     
/* 136 */     throw log.getUnableToLoadClassException(this.className, this.classLoader, exception);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\privilegedactions\LoadClass.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */