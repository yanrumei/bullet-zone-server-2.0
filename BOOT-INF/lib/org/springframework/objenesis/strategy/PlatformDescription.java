/*     */ package org.springframework.objenesis.strategy;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import org.springframework.objenesis.ObjenesisException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class PlatformDescription
/*     */ {
/*     */   public static final String JROCKIT = "BEA";
/*     */   public static final String GNU = "GNU libgcj";
/*     */   public static final String HOTSPOT = "Java HotSpot";
/*     */   @Deprecated
/*     */   public static final String SUN = "Java HotSpot";
/*     */   public static final String OPENJDK = "OpenJDK";
/*     */   public static final String PERC = "PERC";
/*     */   public static final String DALVIK = "Dalvik";
/*  57 */   public static final String SPECIFICATION_VERSION = System.getProperty("java.specification.version");
/*     */   
/*     */ 
/*  60 */   public static final String VM_VERSION = System.getProperty("java.runtime.version");
/*     */   
/*     */ 
/*  63 */   public static final String VM_INFO = System.getProperty("java.vm.info");
/*     */   
/*     */ 
/*  66 */   public static final String VENDOR_VERSION = System.getProperty("java.vm.version");
/*     */   
/*     */ 
/*  69 */   public static final String VENDOR = System.getProperty("java.vm.vendor");
/*     */   
/*     */ 
/*  72 */   public static final String JVM_NAME = System.getProperty("java.vm.name");
/*     */   
/*     */ 
/*  75 */   public static final int ANDROID_VERSION = getAndroidVersion();
/*     */   
/*     */ 
/*  78 */   public static final boolean IS_ANDROID_OPENJDK = getIsAndroidOpenJDK();
/*     */   
/*     */ 
/*  81 */   public static final String GAE_VERSION = getGaeRuntimeVersion();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String describePlatform()
/*     */   {
/*  89 */     String desc = "Java " + SPECIFICATION_VERSION + " (VM vendor name=\"" + VENDOR + "\", VM vendor version=" + VENDOR_VERSION + ", JVM name=\"" + JVM_NAME + "\", JVM version=" + VM_VERSION + ", JVM info=" + VM_INFO;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  97 */     int androidVersion = ANDROID_VERSION;
/*  98 */     if (androidVersion != 0) {
/*  99 */       desc = desc + ", API level=" + ANDROID_VERSION;
/*     */     }
/* 101 */     desc = desc + ")";
/*     */     
/* 103 */     return desc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isThisJVM(String name)
/*     */   {
/* 115 */     return JVM_NAME.startsWith(name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isAndroidOpenJDK()
/*     */   {
/* 124 */     return IS_ANDROID_OPENJDK;
/*     */   }
/*     */   
/*     */   private static boolean getIsAndroidOpenJDK() {
/* 128 */     if (getAndroidVersion() == 0) {
/* 129 */       return false;
/*     */     }
/*     */     
/*     */ 
/* 133 */     String bootClasspath = System.getProperty("java.boot.class.path");
/* 134 */     return (bootClasspath != null) && (bootClasspath.toLowerCase().contains("core-oj.jar"));
/*     */   }
/*     */   
/*     */   public static boolean isGoogleAppEngine() {
/* 138 */     return GAE_VERSION != null;
/*     */   }
/*     */   
/*     */   private static String getGaeRuntimeVersion() {
/* 142 */     return System.getProperty("com.google.appengine.runtime.version");
/*     */   }
/*     */   
/*     */   private static int getAndroidVersion() {
/* 146 */     if (!isThisJVM("Dalvik")) {
/* 147 */       return 0;
/*     */     }
/* 149 */     return getAndroidVersion0();
/*     */   }
/*     */   
/*     */   private static int getAndroidVersion0()
/*     */   {
/*     */     try {
/* 155 */       clazz = Class.forName("android.os.Build$VERSION");
/*     */     } catch (ClassNotFoundException e) {
/*     */       Class<?> clazz;
/* 158 */       throw new ObjenesisException(e);
/*     */     }
/*     */     try
/*     */     {
/* 162 */       field = clazz.getField("SDK_INT");
/*     */     } catch (NoSuchFieldException e) {
/*     */       Class<?> clazz;
/*     */       Field field;
/* 166 */       return getOldAndroidVersion(clazz);
/*     */     }
/*     */     try {
/*     */       Field field;
/* 170 */       version = ((Integer)field.get(null)).intValue();
/*     */     } catch (IllegalAccessException e) {
/*     */       int version;
/* 173 */       throw new RuntimeException(e); }
/*     */     int version;
/* 175 */     return version;
/*     */   }
/*     */   
/*     */   private static int getOldAndroidVersion(Class<?> versionClass)
/*     */   {
/*     */     try {
/* 181 */       field = versionClass.getField("SDK");
/*     */     } catch (NoSuchFieldException e) {
/*     */       Field field;
/* 184 */       throw new ObjenesisException(e);
/*     */     }
/*     */     try {
/*     */       Field field;
/* 188 */       version = (String)field.get(null);
/*     */     } catch (IllegalAccessException e) {
/*     */       String version;
/* 191 */       throw new RuntimeException(e); }
/*     */     String version;
/* 193 */     return Integer.parseInt(version);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\objenesis\strategy\PlatformDescription.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */