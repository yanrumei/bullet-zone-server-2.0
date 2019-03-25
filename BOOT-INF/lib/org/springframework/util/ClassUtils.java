/*      */ package org.springframework.util;
/*      */ 
/*      */ import java.beans.Introspector;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.IdentityHashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class ClassUtils
/*      */ {
/*      */   public static final String ARRAY_SUFFIX = "[]";
/*      */   private static final String INTERNAL_ARRAY_PREFIX = "[";
/*      */   private static final String NON_PRIMITIVE_ARRAY_PREFIX = "[L";
/*      */   private static final char PACKAGE_SEPARATOR = '.';
/*      */   private static final char PATH_SEPARATOR = '/';
/*      */   private static final char INNER_CLASS_SEPARATOR = '$';
/*      */   public static final String CGLIB_CLASS_SEPARATOR = "$$";
/*      */   public static final String CLASS_FILE_SUFFIX = ".class";
/*   79 */   private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap(8);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   85 */   private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap(8);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   91 */   private static final Map<String, Class<?>> primitiveTypeNameMap = new HashMap(32);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   97 */   private static final Map<String, Class<?>> commonClassCache = new HashMap(32);
/*      */   
/*      */   static
/*      */   {
/*  101 */     primitiveWrapperTypeMap.put(Boolean.class, Boolean.TYPE);
/*  102 */     primitiveWrapperTypeMap.put(Byte.class, Byte.TYPE);
/*  103 */     primitiveWrapperTypeMap.put(Character.class, Character.TYPE);
/*  104 */     primitiveWrapperTypeMap.put(Double.class, Double.TYPE);
/*  105 */     primitiveWrapperTypeMap.put(Float.class, Float.TYPE);
/*  106 */     primitiveWrapperTypeMap.put(Integer.class, Integer.TYPE);
/*  107 */     primitiveWrapperTypeMap.put(Long.class, Long.TYPE);
/*  108 */     primitiveWrapperTypeMap.put(Short.class, Short.TYPE);
/*      */     
/*  110 */     for (Iterator localIterator = primitiveWrapperTypeMap.entrySet().iterator(); localIterator.hasNext();) { entry = (Map.Entry)localIterator.next();
/*  111 */       primitiveTypeToWrapperMap.put(entry.getValue(), entry.getKey());
/*  112 */       registerCommonClasses(new Class[] { (Class)entry.getKey() });
/*      */     }
/*      */     Map.Entry<Class<?>, Class<?>> entry;
/*  115 */     Object primitiveTypes = new HashSet(32);
/*  116 */     ((Set)primitiveTypes).addAll(primitiveWrapperTypeMap.values());
/*  117 */     ((Set)primitiveTypes).addAll(Arrays.asList(new Class[] { boolean[].class, byte[].class, char[].class, double[].class, float[].class, int[].class, long[].class, short[].class }));
/*      */     
/*      */ 
/*  120 */     ((Set)primitiveTypes).add(Void.TYPE);
/*  121 */     for (Class<?> primitiveType : (Set)primitiveTypes) {
/*  122 */       primitiveTypeNameMap.put(primitiveType.getName(), primitiveType);
/*      */     }
/*      */     
/*  125 */     registerCommonClasses(new Class[] { Boolean[].class, Byte[].class, Character[].class, Double[].class, Float[].class, Integer[].class, Long[].class, Short[].class });
/*      */     
/*  127 */     registerCommonClasses(new Class[] { Number.class, Number[].class, String.class, String[].class, Object.class, Object[].class, Class.class, Class[].class });
/*      */     
/*  129 */     registerCommonClasses(new Class[] { Throwable.class, Exception.class, RuntimeException.class, Error.class, StackTraceElement.class, StackTraceElement[].class });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static void registerCommonClasses(Class<?>... commonClasses)
/*      */   {
/*  138 */     for (Class<?> clazz : commonClasses) {
/*  139 */       commonClassCache.put(clazz.getName(), clazz);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ClassLoader getDefaultClassLoader()
/*      */   {
/*  158 */     ClassLoader cl = null;
/*      */     try {
/*  160 */       cl = Thread.currentThread().getContextClassLoader();
/*      */     }
/*      */     catch (Throwable localThrowable) {}
/*      */     
/*      */ 
/*  165 */     if (cl == null)
/*      */     {
/*  167 */       cl = ClassUtils.class.getClassLoader();
/*  168 */       if (cl == null) {
/*      */         try
/*      */         {
/*  171 */           cl = ClassLoader.getSystemClassLoader();
/*      */         }
/*      */         catch (Throwable localThrowable1) {}
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  178 */     return cl;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static ClassLoader overrideThreadContextClassLoader(ClassLoader classLoaderToUse)
/*      */   {
/*  189 */     Thread currentThread = Thread.currentThread();
/*  190 */     ClassLoader threadContextClassLoader = currentThread.getContextClassLoader();
/*  191 */     if ((classLoaderToUse != null) && (!classLoaderToUse.equals(threadContextClassLoader))) {
/*  192 */       currentThread.setContextClassLoader(classLoaderToUse);
/*  193 */       return threadContextClassLoader;
/*      */     }
/*      */     
/*  196 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?> forName(String name, ClassLoader classLoader)
/*      */     throws ClassNotFoundException, LinkageError
/*      */   {
/*  214 */     Assert.notNull(name, "Name must not be null");
/*      */     
/*  216 */     Class<?> clazz = resolvePrimitiveClassName(name);
/*  217 */     if (clazz == null) {
/*  218 */       clazz = (Class)commonClassCache.get(name);
/*      */     }
/*  220 */     if (clazz != null) {
/*  221 */       return clazz;
/*      */     }
/*      */     
/*      */ 
/*  225 */     if (name.endsWith("[]")) {
/*  226 */       String elementClassName = name.substring(0, name.length() - "[]".length());
/*  227 */       Class<?> elementClass = forName(elementClassName, classLoader);
/*  228 */       return Array.newInstance(elementClass, 0).getClass();
/*      */     }
/*      */     
/*      */ 
/*  232 */     if ((name.startsWith("[L")) && (name.endsWith(";"))) {
/*  233 */       String elementName = name.substring("[L".length(), name.length() - 1);
/*  234 */       Class<?> elementClass = forName(elementName, classLoader);
/*  235 */       return Array.newInstance(elementClass, 0).getClass();
/*      */     }
/*      */     
/*      */ 
/*  239 */     if (name.startsWith("[")) {
/*  240 */       String elementName = name.substring("[".length());
/*  241 */       Class<?> elementClass = forName(elementName, classLoader);
/*  242 */       return Array.newInstance(elementClass, 0).getClass();
/*      */     }
/*      */     
/*  245 */     ClassLoader clToUse = classLoader;
/*  246 */     if (clToUse == null) {
/*  247 */       clToUse = getDefaultClassLoader();
/*      */     }
/*      */     try {
/*  250 */       return clToUse != null ? clToUse.loadClass(name) : Class.forName(name);
/*      */     }
/*      */     catch (ClassNotFoundException ex) {
/*  253 */       int lastDotIndex = name.lastIndexOf('.');
/*  254 */       if (lastDotIndex != -1)
/*      */       {
/*  256 */         String innerClassName = name.substring(0, lastDotIndex) + '$' + name.substring(lastDotIndex + 1);
/*      */         try {
/*  258 */           return clToUse != null ? clToUse.loadClass(innerClassName) : Class.forName(innerClassName);
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException1) {}
/*      */       }
/*      */       
/*      */ 
/*  264 */       throw ex;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?> resolveClassName(String className, ClassLoader classLoader)
/*      */     throws IllegalArgumentException
/*      */   {
/*      */     try
/*      */     {
/*  284 */       return forName(className, classLoader);
/*      */     }
/*      */     catch (ClassNotFoundException ex) {
/*  287 */       throw new IllegalArgumentException("Cannot find class [" + className + "]", ex);
/*      */     }
/*      */     catch (LinkageError ex) {
/*  290 */       throw new IllegalArgumentException("Error loading class [" + className + "]: problem with class file or dependent class.", ex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?> resolvePrimitiveClassName(String name)
/*      */   {
/*  306 */     Class<?> result = null;
/*      */     
/*      */ 
/*  309 */     if ((name != null) && (name.length() <= 8))
/*      */     {
/*  311 */       result = (Class)primitiveTypeNameMap.get(name);
/*      */     }
/*  313 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isPresent(String className, ClassLoader classLoader)
/*      */   {
/*      */     try
/*      */     {
/*  327 */       forName(className, classLoader);
/*  328 */       return true;
/*      */     }
/*      */     catch (Throwable ex) {}
/*      */     
/*  332 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?> getUserClass(Object instance)
/*      */   {
/*  344 */     Assert.notNull(instance, "Instance must not be null");
/*  345 */     return getUserClass(instance.getClass());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?> getUserClass(Class<?> clazz)
/*      */   {
/*  355 */     if ((clazz != null) && (clazz.getName().contains("$$"))) {
/*  356 */       Class<?> superclass = clazz.getSuperclass();
/*  357 */       if ((superclass != null) && (Object.class != superclass)) {
/*  358 */         return superclass;
/*      */       }
/*      */     }
/*  361 */     return clazz;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isCacheSafe(Class<?> clazz, ClassLoader classLoader)
/*      */   {
/*  371 */     Assert.notNull(clazz, "Class must not be null");
/*      */     try {
/*  373 */       ClassLoader target = clazz.getClassLoader();
/*  374 */       if (target == null) {
/*  375 */         return true;
/*      */       }
/*  377 */       ClassLoader cur = classLoader;
/*  378 */       if (cur == target) {
/*  379 */         return true;
/*      */       }
/*  381 */       while (cur != null) {
/*  382 */         cur = cur.getParent();
/*  383 */         if (cur == target) {
/*  384 */           return true;
/*      */         }
/*      */       }
/*  387 */       return false;
/*      */     }
/*      */     catch (SecurityException ex) {}
/*      */     
/*  391 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getShortName(String className)
/*      */   {
/*  403 */     Assert.hasLength(className, "Class name must not be empty");
/*  404 */     int lastDotIndex = className.lastIndexOf('.');
/*  405 */     int nameEndIndex = className.indexOf("$$");
/*  406 */     if (nameEndIndex == -1) {
/*  407 */       nameEndIndex = className.length();
/*      */     }
/*  409 */     String shortName = className.substring(lastDotIndex + 1, nameEndIndex);
/*  410 */     shortName = shortName.replace('$', '.');
/*  411 */     return shortName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getShortName(Class<?> clazz)
/*      */   {
/*  420 */     return getShortName(getQualifiedName(clazz));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getShortNameAsProperty(Class<?> clazz)
/*      */   {
/*  431 */     String shortName = getShortName(clazz);
/*  432 */     int dotIndex = shortName.lastIndexOf('.');
/*  433 */     shortName = dotIndex != -1 ? shortName.substring(dotIndex + 1) : shortName;
/*  434 */     return Introspector.decapitalize(shortName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getClassFileName(Class<?> clazz)
/*      */   {
/*  444 */     Assert.notNull(clazz, "Class must not be null");
/*  445 */     String className = clazz.getName();
/*  446 */     int lastDotIndex = className.lastIndexOf('.');
/*  447 */     return className.substring(lastDotIndex + 1) + ".class";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getPackageName(Class<?> clazz)
/*      */   {
/*  458 */     Assert.notNull(clazz, "Class must not be null");
/*  459 */     return getPackageName(clazz.getName());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getPackageName(String fqClassName)
/*      */   {
/*  470 */     Assert.notNull(fqClassName, "Class name must not be null");
/*  471 */     int lastDotIndex = fqClassName.lastIndexOf('.');
/*  472 */     return lastDotIndex != -1 ? fqClassName.substring(0, lastDotIndex) : "";
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getQualifiedName(Class<?> clazz)
/*      */   {
/*  482 */     Assert.notNull(clazz, "Class must not be null");
/*  483 */     if (clazz.isArray()) {
/*  484 */       return getQualifiedNameForArray(clazz);
/*      */     }
/*      */     
/*  487 */     return clazz.getName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static String getQualifiedNameForArray(Class<?> clazz)
/*      */   {
/*  498 */     StringBuilder result = new StringBuilder();
/*  499 */     while (clazz.isArray()) {
/*  500 */       clazz = clazz.getComponentType();
/*  501 */       result.append("[]");
/*      */     }
/*  503 */     result.insert(0, clazz.getName());
/*  504 */     return result.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getQualifiedMethodName(Method method)
/*      */   {
/*  514 */     return getQualifiedMethodName(method, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getQualifiedMethodName(Method method, Class<?> clazz)
/*      */   {
/*  527 */     Assert.notNull(method, "Method must not be null");
/*  528 */     return (clazz != null ? clazz : method.getDeclaringClass()).getName() + '.' + method.getName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String getDescriptiveType(Object value)
/*      */   {
/*  539 */     if (value == null) {
/*  540 */       return null;
/*      */     }
/*  542 */     Class<?> clazz = value.getClass();
/*  543 */     if (Proxy.isProxyClass(clazz)) {
/*  544 */       StringBuilder result = new StringBuilder(clazz.getName());
/*  545 */       result.append(" implementing ");
/*  546 */       Class<?>[] ifcs = clazz.getInterfaces();
/*  547 */       for (int i = 0; i < ifcs.length; i++) {
/*  548 */         result.append(ifcs[i].getName());
/*  549 */         if (i < ifcs.length - 1) {
/*  550 */           result.append(',');
/*      */         }
/*      */       }
/*  553 */       return result.toString();
/*      */     }
/*  555 */     if (clazz.isArray()) {
/*  556 */       return getQualifiedNameForArray(clazz);
/*      */     }
/*      */     
/*  559 */     return clazz.getName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean matchesTypeName(Class<?> clazz, String typeName)
/*      */   {
/*  569 */     return (typeName != null) && (
/*  570 */       (typeName.equals(clazz.getName())) || (typeName.equals(clazz.getSimpleName())) || (
/*  571 */       (clazz.isArray()) && (typeName.equals(getQualifiedNameForArray(clazz)))));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean hasConstructor(Class<?> clazz, Class<?>... paramTypes)
/*      */   {
/*  584 */     return getConstructorIfAvailable(clazz, paramTypes) != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static <T> Constructor<T> getConstructorIfAvailable(Class<T> clazz, Class<?>... paramTypes)
/*      */   {
/*  597 */     Assert.notNull(clazz, "Class must not be null");
/*      */     try {
/*  599 */       return clazz.getConstructor(paramTypes);
/*      */     }
/*      */     catch (NoSuchMethodException ex) {}
/*  602 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean hasMethod(Class<?> clazz, String methodName, Class<?>... paramTypes)
/*      */   {
/*  616 */     return getMethodIfAvailable(clazz, methodName, paramTypes) != null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Method getMethod(Class<?> clazz, String methodName, Class<?>... paramTypes)
/*      */   {
/*  634 */     Assert.notNull(clazz, "Class must not be null");
/*  635 */     Assert.notNull(methodName, "Method name must not be null");
/*  636 */     if (paramTypes != null) {
/*      */       try {
/*  638 */         return clazz.getMethod(methodName, paramTypes);
/*      */       }
/*      */       catch (NoSuchMethodException ex) {
/*  641 */         throw new IllegalStateException("Expected method not found: " + ex);
/*      */       }
/*      */     }
/*      */     
/*  645 */     Set<Method> candidates = new HashSet(1);
/*  646 */     Method[] methods = clazz.getMethods();
/*  647 */     for (Method method : methods) {
/*  648 */       if (methodName.equals(method.getName())) {
/*  649 */         candidates.add(method);
/*      */       }
/*      */     }
/*  652 */     if (candidates.size() == 1) {
/*  653 */       return (Method)candidates.iterator().next();
/*      */     }
/*  655 */     if (candidates.isEmpty()) {
/*  656 */       throw new IllegalStateException("Expected method not found: " + clazz.getName() + '.' + methodName);
/*      */     }
/*      */     
/*  659 */     throw new IllegalStateException("No unique method found: " + clazz.getName() + '.' + methodName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Method getMethodIfAvailable(Class<?> clazz, String methodName, Class<?>... paramTypes)
/*      */   {
/*  678 */     Assert.notNull(clazz, "Class must not be null");
/*  679 */     Assert.notNull(methodName, "Method name must not be null");
/*  680 */     if (paramTypes != null) {
/*      */       try {
/*  682 */         return clazz.getMethod(methodName, paramTypes);
/*      */       }
/*      */       catch (NoSuchMethodException ex) {
/*  685 */         return null;
/*      */       }
/*      */     }
/*      */     
/*  689 */     Set<Method> candidates = new HashSet(1);
/*  690 */     Method[] methods = clazz.getMethods();
/*  691 */     for (Method method : methods) {
/*  692 */       if (methodName.equals(method.getName())) {
/*  693 */         candidates.add(method);
/*      */       }
/*      */     }
/*  696 */     if (candidates.size() == 1) {
/*  697 */       return (Method)candidates.iterator().next();
/*      */     }
/*  699 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static int getMethodCountForName(Class<?> clazz, String methodName)
/*      */   {
/*  711 */     Assert.notNull(clazz, "Class must not be null");
/*  712 */     Assert.notNull(methodName, "Method name must not be null");
/*  713 */     int count = 0;
/*  714 */     Method[] declaredMethods = clazz.getDeclaredMethods();
/*  715 */     Method[] arrayOfMethod1 = declaredMethods;int i = arrayOfMethod1.length; for (Method localMethod1 = 0; localMethod1 < i; localMethod1++) { method = arrayOfMethod1[localMethod1];
/*  716 */       if (methodName.equals(method.getName())) {
/*  717 */         count++;
/*      */       }
/*      */     }
/*  720 */     Object ifcs = clazz.getInterfaces();
/*  721 */     Object localObject1 = ifcs;localMethod1 = localObject1.length; for (Method method = 0; method < localMethod1; method++) { Class<?> ifc = localObject1[method];
/*  722 */       count += getMethodCountForName(ifc, methodName);
/*      */     }
/*  724 */     if (clazz.getSuperclass() != null) {
/*  725 */       count += getMethodCountForName(clazz.getSuperclass(), methodName);
/*      */     }
/*  727 */     return count;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean hasAtLeastOneMethodWithName(Class<?> clazz, String methodName)
/*      */   {
/*  739 */     Assert.notNull(clazz, "Class must not be null");
/*  740 */     Assert.notNull(methodName, "Method name must not be null");
/*  741 */     Method[] declaredMethods = clazz.getDeclaredMethods();
/*  742 */     Method[] arrayOfMethod1 = declaredMethods;int i = arrayOfMethod1.length; for (Method localMethod1 = 0; localMethod1 < i; localMethod1++) { method = arrayOfMethod1[localMethod1];
/*  743 */       if (method.getName().equals(methodName)) {
/*  744 */         return true;
/*      */       }
/*      */     }
/*  747 */     Object ifcs = clazz.getInterfaces();
/*  748 */     Object localObject1 = ifcs;localMethod1 = localObject1.length; for (Method method = 0; method < localMethod1; method++) { Class<?> ifc = localObject1[method];
/*  749 */       if (hasAtLeastOneMethodWithName(ifc, methodName)) {
/*  750 */         return true;
/*      */       }
/*      */     }
/*  753 */     return (clazz.getSuperclass() != null) && (hasAtLeastOneMethodWithName(clazz.getSuperclass(), methodName));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Method getMostSpecificMethod(Method method, Class<?> targetClass)
/*      */   {
/*  777 */     if ((method != null) && (isOverridable(method, targetClass)) && (targetClass != null) && 
/*  778 */       (targetClass != method.getDeclaringClass())) {
/*      */       try {
/*  780 */         if (Modifier.isPublic(method.getModifiers())) {
/*      */           try {
/*  782 */             return targetClass.getMethod(method.getName(), method.getParameterTypes());
/*      */           }
/*      */           catch (NoSuchMethodException ex) {
/*  785 */             return method;
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*  790 */         Method specificMethod = ReflectionUtils.findMethod(targetClass, method.getName(), method.getParameterTypes());
/*  791 */         return specificMethod != null ? specificMethod : method;
/*      */       }
/*      */       catch (SecurityException localSecurityException) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  798 */     return method;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isUserLevelMethod(Method method)
/*      */   {
/*  813 */     Assert.notNull(method, "Method must not be null");
/*  814 */     return (method.isBridge()) || ((!method.isSynthetic()) && (!isGroovyObjectMethod(method)));
/*      */   }
/*      */   
/*      */   private static boolean isGroovyObjectMethod(Method method) {
/*  818 */     return method.getDeclaringClass().getName().equals("groovy.lang.GroovyObject");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static boolean isOverridable(Method method, Class<?> targetClass)
/*      */   {
/*  827 */     if (Modifier.isPrivate(method.getModifiers())) {
/*  828 */       return false;
/*      */     }
/*  830 */     if ((Modifier.isPublic(method.getModifiers())) || (Modifier.isProtected(method.getModifiers()))) {
/*  831 */       return true;
/*      */     }
/*  833 */     return getPackageName(method.getDeclaringClass()).equals(getPackageName(targetClass));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Method getStaticMethod(Class<?> clazz, String methodName, Class<?>... args)
/*      */   {
/*  845 */     Assert.notNull(clazz, "Class must not be null");
/*  846 */     Assert.notNull(methodName, "Method name must not be null");
/*      */     try {
/*  848 */       Method method = clazz.getMethod(methodName, args);
/*  849 */       return Modifier.isStatic(method.getModifiers()) ? method : null;
/*      */     }
/*      */     catch (NoSuchMethodException ex) {}
/*  852 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isPrimitiveWrapper(Class<?> clazz)
/*      */   {
/*  864 */     Assert.notNull(clazz, "Class must not be null");
/*  865 */     return primitiveWrapperTypeMap.containsKey(clazz);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isPrimitiveOrWrapper(Class<?> clazz)
/*      */   {
/*  876 */     Assert.notNull(clazz, "Class must not be null");
/*  877 */     return (clazz.isPrimitive()) || (isPrimitiveWrapper(clazz));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isPrimitiveArray(Class<?> clazz)
/*      */   {
/*  887 */     Assert.notNull(clazz, "Class must not be null");
/*  888 */     return (clazz.isArray()) && (clazz.getComponentType().isPrimitive());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isPrimitiveWrapperArray(Class<?> clazz)
/*      */   {
/*  898 */     Assert.notNull(clazz, "Class must not be null");
/*  899 */     return (clazz.isArray()) && (isPrimitiveWrapper(clazz.getComponentType()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?> resolvePrimitiveIfNecessary(Class<?> clazz)
/*      */   {
/*  909 */     Assert.notNull(clazz, "Class must not be null");
/*  910 */     return (clazz.isPrimitive()) && (clazz != Void.TYPE) ? (Class)primitiveTypeToWrapperMap.get(clazz) : clazz;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType)
/*      */   {
/*  923 */     Assert.notNull(lhsType, "Left-hand side type must not be null");
/*  924 */     Assert.notNull(rhsType, "Right-hand side type must not be null");
/*  925 */     if (lhsType.isAssignableFrom(rhsType)) {
/*  926 */       return true;
/*      */     }
/*  928 */     if (lhsType.isPrimitive()) {
/*  929 */       Class<?> resolvedPrimitive = (Class)primitiveWrapperTypeMap.get(rhsType);
/*  930 */       if (lhsType == resolvedPrimitive) {
/*  931 */         return true;
/*      */       }
/*      */     }
/*      */     else {
/*  935 */       Class<?> resolvedWrapper = (Class)primitiveTypeToWrapperMap.get(rhsType);
/*  936 */       if ((resolvedWrapper != null) && (lhsType.isAssignableFrom(resolvedWrapper))) {
/*  937 */         return true;
/*      */       }
/*      */     }
/*  940 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isAssignableValue(Class<?> type, Object value)
/*      */   {
/*  952 */     Assert.notNull(type, "Type must not be null");
/*  953 */     return !type.isPrimitive() ? true : value != null ? isAssignable(type, value.getClass()) : false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String convertResourcePathToClassName(String resourcePath)
/*      */   {
/*  963 */     Assert.notNull(resourcePath, "Resource path must not be null");
/*  964 */     return resourcePath.replace('/', '.');
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String convertClassNameToResourcePath(String className)
/*      */   {
/*  973 */     Assert.notNull(className, "Class name must not be null");
/*  974 */     return className.replace('.', '/');
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String addResourcePathToPackagePath(Class<?> clazz, String resourceName)
/*      */   {
/*  994 */     Assert.notNull(resourceName, "Resource name must not be null");
/*  995 */     if (!resourceName.startsWith("/")) {
/*  996 */       return classPackageAsResourcePath(clazz) + '/' + resourceName;
/*      */     }
/*  998 */     return classPackageAsResourcePath(clazz) + resourceName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String classPackageAsResourcePath(Class<?> clazz)
/*      */   {
/* 1016 */     if (clazz == null) {
/* 1017 */       return "";
/*      */     }
/* 1019 */     String className = clazz.getName();
/* 1020 */     int packageEndIndex = className.lastIndexOf('.');
/* 1021 */     if (packageEndIndex == -1) {
/* 1022 */       return "";
/*      */     }
/* 1024 */     String packageName = className.substring(0, packageEndIndex);
/* 1025 */     return packageName.replace('.', '/');
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String classNamesToString(Class<?>... classes)
/*      */   {
/* 1038 */     return classNamesToString(Arrays.asList(classes));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String classNamesToString(Collection<Class<?>> classes)
/*      */   {
/* 1051 */     if (CollectionUtils.isEmpty(classes)) {
/* 1052 */       return "[]";
/*      */     }
/* 1054 */     StringBuilder sb = new StringBuilder("[");
/* 1055 */     for (Iterator<Class<?>> it = classes.iterator(); it.hasNext();) {
/* 1056 */       Class<?> clazz = (Class)it.next();
/* 1057 */       sb.append(clazz.getName());
/* 1058 */       if (it.hasNext()) {
/* 1059 */         sb.append(", ");
/*      */       }
/*      */     }
/* 1062 */     sb.append("]");
/* 1063 */     return sb.toString();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?>[] toClassArray(Collection<Class<?>> collection)
/*      */   {
/* 1074 */     if (collection == null) {
/* 1075 */       return null;
/*      */     }
/* 1077 */     return (Class[])collection.toArray(new Class[collection.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?>[] getAllInterfaces(Object instance)
/*      */   {
/* 1087 */     Assert.notNull(instance, "Instance must not be null");
/* 1088 */     return getAllInterfacesForClass(instance.getClass());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?>[] getAllInterfacesForClass(Class<?> clazz)
/*      */   {
/* 1099 */     return getAllInterfacesForClass(clazz, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?>[] getAllInterfacesForClass(Class<?> clazz, ClassLoader classLoader)
/*      */   {
/* 1112 */     Set<Class<?>> ifcs = getAllInterfacesForClassAsSet(clazz, classLoader);
/* 1113 */     return (Class[])ifcs.toArray(new Class[ifcs.size()]);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Set<Class<?>> getAllInterfacesAsSet(Object instance)
/*      */   {
/* 1123 */     Assert.notNull(instance, "Instance must not be null");
/* 1124 */     return getAllInterfacesForClassAsSet(instance.getClass());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz)
/*      */   {
/* 1135 */     return getAllInterfacesForClassAsSet(clazz, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Set<Class<?>> getAllInterfacesForClassAsSet(Class<?> clazz, ClassLoader classLoader)
/*      */   {
/* 1148 */     Assert.notNull(clazz, "Class must not be null");
/* 1149 */     if ((clazz.isInterface()) && (isVisible(clazz, classLoader))) {
/* 1150 */       return Collections.singleton(clazz);
/*      */     }
/* 1152 */     Set<Class<?>> interfaces = new LinkedHashSet();
/* 1153 */     while (clazz != null) {
/* 1154 */       Class<?>[] ifcs = clazz.getInterfaces();
/* 1155 */       for (Class<?> ifc : ifcs) {
/* 1156 */         interfaces.addAll(getAllInterfacesForClassAsSet(ifc, classLoader));
/*      */       }
/* 1158 */       clazz = clazz.getSuperclass();
/*      */     }
/* 1160 */     return interfaces;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?> createCompositeInterface(Class<?>[] interfaces, ClassLoader classLoader)
/*      */   {
/* 1173 */     Assert.notEmpty(interfaces, "Interfaces must not be empty");
/* 1174 */     return Proxy.getProxyClass(classLoader, interfaces);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?> determineCommonAncestor(Class<?> clazz1, Class<?> clazz2)
/*      */   {
/* 1187 */     if (clazz1 == null) {
/* 1188 */       return clazz2;
/*      */     }
/* 1190 */     if (clazz2 == null) {
/* 1191 */       return clazz1;
/*      */     }
/* 1193 */     if (clazz1.isAssignableFrom(clazz2)) {
/* 1194 */       return clazz1;
/*      */     }
/* 1196 */     if (clazz2.isAssignableFrom(clazz1)) {
/* 1197 */       return clazz2;
/*      */     }
/* 1199 */     Class<?> ancestor = clazz1;
/*      */     do {
/* 1201 */       ancestor = ancestor.getSuperclass();
/* 1202 */       if ((ancestor == null) || (Object.class == ancestor)) {
/* 1203 */         return null;
/*      */       }
/*      */       
/* 1206 */     } while (!ancestor.isAssignableFrom(clazz2));
/* 1207 */     return ancestor;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isVisible(Class<?> clazz, ClassLoader classLoader)
/*      */   {
/* 1217 */     if (classLoader == null) {
/* 1218 */       return true;
/*      */     }
/*      */     try {
/* 1221 */       Class<?> actualClass = classLoader.loadClass(clazz.getName());
/* 1222 */       return clazz == actualClass;
/*      */     }
/*      */     catch (ClassNotFoundException ex) {}
/*      */     
/*      */ 
/* 1227 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isCglibProxy(Object object)
/*      */   {
/* 1238 */     return isCglibProxyClass(object.getClass());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isCglibProxyClass(Class<?> clazz)
/*      */   {
/* 1247 */     return (clazz != null) && (isCglibProxyClassName(clazz.getName()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isCglibProxyClassName(String className)
/*      */   {
/* 1255 */     return (className != null) && (className.contains("$$"));
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\ClassUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */