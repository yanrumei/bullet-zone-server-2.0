/*     */ package org.hibernate.validator.internal.xml;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.LoadClass;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class ClassLoadingHelper
/*     */ {
/*     */   private static final String PACKAGE_SEPARATOR = ".";
/*     */   private static final String ARRAY_CLASS_NAME_PREFIX = "[L";
/*     */   private static final String ARRAY_CLASS_NAME_SUFFIX = ";";
/*     */   private static final Map<String, Class<?>> PRIMITIVE_NAME_TO_PRIMITIVE;
/*     */   private final ClassLoader externalClassLoader;
/*     */   
/*     */   static
/*     */   {
/*  33 */     Map<String, Class<?>> tmpMap = CollectionHelper.newHashMap(9);
/*     */     
/*  35 */     tmpMap.put(Boolean.TYPE.getName(), Boolean.TYPE);
/*  36 */     tmpMap.put(Character.TYPE.getName(), Character.TYPE);
/*  37 */     tmpMap.put(Double.TYPE.getName(), Double.TYPE);
/*  38 */     tmpMap.put(Float.TYPE.getName(), Float.TYPE);
/*  39 */     tmpMap.put(Long.TYPE.getName(), Long.TYPE);
/*  40 */     tmpMap.put(Integer.TYPE.getName(), Integer.TYPE);
/*  41 */     tmpMap.put(Short.TYPE.getName(), Short.TYPE);
/*  42 */     tmpMap.put(Byte.TYPE.getName(), Byte.TYPE);
/*  43 */     tmpMap.put(Void.TYPE.getName(), Void.TYPE);
/*     */     
/*  45 */     PRIMITIVE_NAME_TO_PRIMITIVE = Collections.unmodifiableMap(tmpMap);
/*     */   }
/*     */   
/*     */ 
/*     */   ClassLoadingHelper(ClassLoader externalClassLoader)
/*     */   {
/*  51 */     this.externalClassLoader = externalClassLoader;
/*     */   }
/*     */   
/*     */   Class<?> loadClass(String className, String defaultPackage) {
/*  55 */     if (PRIMITIVE_NAME_TO_PRIMITIVE.containsKey(className)) {
/*  56 */       return (Class)PRIMITIVE_NAME_TO_PRIMITIVE.get(className);
/*     */     }
/*     */     
/*  59 */     StringBuilder fullyQualifiedClass = new StringBuilder();
/*  60 */     String tmpClassName = className;
/*  61 */     if (isArrayClassName(className)) {
/*  62 */       fullyQualifiedClass.append("[L");
/*  63 */       tmpClassName = getArrayElementClassName(className);
/*     */     }
/*     */     
/*  66 */     if (isQualifiedClass(tmpClassName)) {
/*  67 */       fullyQualifiedClass.append(tmpClassName);
/*     */     }
/*     */     else {
/*  70 */       fullyQualifiedClass.append(defaultPackage);
/*  71 */       fullyQualifiedClass.append(".");
/*  72 */       fullyQualifiedClass.append(tmpClassName);
/*     */     }
/*     */     
/*  75 */     if (isArrayClassName(className)) {
/*  76 */       fullyQualifiedClass.append(";");
/*     */     }
/*     */     
/*  79 */     return loadClass(fullyQualifiedClass.toString());
/*     */   }
/*     */   
/*     */   private Class<?> loadClass(String className) {
/*  83 */     return (Class)run(LoadClass.action(className, this.externalClassLoader));
/*     */   }
/*     */   
/*     */   private static boolean isArrayClassName(String className) {
/*  87 */     return (className.startsWith("[L")) && (className.endsWith(";"));
/*     */   }
/*     */   
/*     */   private static String getArrayElementClassName(String className) {
/*  91 */     return className.substring(2, className.length() - 1);
/*     */   }
/*     */   
/*     */   private static boolean isQualifiedClass(String clazz) {
/*  95 */     return clazz.contains(".");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 105 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\ClassLoadingHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */