/*     */ package org.springframework.util;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.UndeclaredThrowableException;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ReflectionUtils
/*     */ {
/*     */   private static final String CGLIB_RENAMED_METHOD_PREFIX = "CGLIB$";
/*  54 */   private static final Method[] NO_METHODS = new Method[0];
/*     */   
/*  56 */   private static final Field[] NO_FIELDS = new Field[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  63 */   private static final Map<Class<?>, Method[]> declaredMethodsCache = new ConcurrentReferenceHashMap(256);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   private static final Map<Class<?>, Field[]> declaredFieldsCache = new ConcurrentReferenceHashMap(256);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Field findField(Class<?> clazz, String name)
/*     */   {
/*  81 */     return findField(clazz, name, null);
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
/*     */   public static Field findField(Class<?> clazz, String name, Class<?> type)
/*     */   {
/*  94 */     Assert.notNull(clazz, "Class must not be null");
/*  95 */     Assert.isTrue((name != null) || (type != null), "Either name or type of the field must be specified");
/*  96 */     Class<?> searchType = clazz;
/*  97 */     while ((Object.class != searchType) && (searchType != null)) {
/*  98 */       Field[] fields = getDeclaredFields(searchType);
/*  99 */       for (Field field : fields) {
/* 100 */         if (((name == null) || (name.equals(field.getName()))) && ((type == null) || 
/* 101 */           (type.equals(field.getType())))) {
/* 102 */           return field;
/*     */         }
/*     */       }
/* 105 */       searchType = searchType.getSuperclass();
/*     */     }
/* 107 */     return null;
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
/*     */   public static void setField(Field field, Object target, Object value)
/*     */   {
/*     */     try
/*     */     {
/* 122 */       field.set(target, value);
/*     */     }
/*     */     catch (IllegalAccessException ex) {
/* 125 */       handleReflectionException(ex);
/*     */       
/* 127 */       throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
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
/*     */   public static Object getField(Field field, Object target)
/*     */   {
/*     */     try
/*     */     {
/* 143 */       return field.get(target);
/*     */     }
/*     */     catch (IllegalAccessException ex) {
/* 146 */       handleReflectionException(ex);
/*     */       
/* 148 */       throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": " + ex.getMessage());
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
/*     */   public static Method findMethod(Class<?> clazz, String name)
/*     */   {
/* 161 */     return findMethod(clazz, name, new Class[0]);
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
/*     */   public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes)
/*     */   {
/* 175 */     Assert.notNull(clazz, "Class must not be null");
/* 176 */     Assert.notNull(name, "Method name must not be null");
/* 177 */     Class<?> searchType = clazz;
/* 178 */     while (searchType != null) {
/* 179 */       Method[] methods = searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType);
/* 180 */       for (Method method : methods) {
/* 181 */         if ((name.equals(method.getName())) && ((paramTypes == null) || 
/* 182 */           (Arrays.equals(paramTypes, method.getParameterTypes())))) {
/* 183 */           return method;
/*     */         }
/*     */       }
/* 186 */       searchType = searchType.getSuperclass();
/*     */     }
/* 188 */     return null;
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
/*     */   public static Object invokeMethod(Method method, Object target)
/*     */   {
/* 201 */     return invokeMethod(method, target, new Object[0]);
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
/*     */   public static Object invokeMethod(Method method, Object target, Object... args)
/*     */   {
/*     */     try
/*     */     {
/* 216 */       return method.invoke(target, args);
/*     */     }
/*     */     catch (Exception ex) {
/* 219 */       handleReflectionException(ex);
/*     */       
/* 221 */       throw new IllegalStateException("Should never get here");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object invokeJdbcMethod(Method method, Object target)
/*     */     throws SQLException
/*     */   {
/* 234 */     return invokeJdbcMethod(method, target, new Object[0]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Object invokeJdbcMethod(Method method, Object target, Object... args)
/*     */     throws SQLException
/*     */   {
/*     */     try
/*     */     {
/* 249 */       return method.invoke(target, args);
/*     */     }
/*     */     catch (IllegalAccessException ex) {
/* 252 */       handleReflectionException(ex);
/*     */     }
/*     */     catch (InvocationTargetException ex) {
/* 255 */       if ((ex.getTargetException() instanceof SQLException)) {
/* 256 */         throw ((SQLException)ex.getTargetException());
/*     */       }
/* 258 */       handleInvocationTargetException(ex);
/*     */     }
/* 260 */     throw new IllegalStateException("Should never get here");
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
/*     */   public static void handleReflectionException(Exception ex)
/*     */   {
/* 273 */     if ((ex instanceof NoSuchMethodException)) {
/* 274 */       throw new IllegalStateException("Method not found: " + ex.getMessage());
/*     */     }
/* 276 */     if ((ex instanceof IllegalAccessException)) {
/* 277 */       throw new IllegalStateException("Could not access method: " + ex.getMessage());
/*     */     }
/* 279 */     if ((ex instanceof InvocationTargetException)) {
/* 280 */       handleInvocationTargetException((InvocationTargetException)ex);
/*     */     }
/* 282 */     if ((ex instanceof RuntimeException)) {
/* 283 */       throw ((RuntimeException)ex);
/*     */     }
/* 285 */     throw new UndeclaredThrowableException(ex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void handleInvocationTargetException(InvocationTargetException ex)
/*     */   {
/* 296 */     rethrowRuntimeException(ex.getTargetException());
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
/*     */   public static void rethrowRuntimeException(Throwable ex)
/*     */   {
/* 311 */     if ((ex instanceof RuntimeException)) {
/* 312 */       throw ((RuntimeException)ex);
/*     */     }
/* 314 */     if ((ex instanceof Error)) {
/* 315 */       throw ((Error)ex);
/*     */     }
/* 317 */     throw new UndeclaredThrowableException(ex);
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
/*     */   public static void rethrowException(Throwable ex)
/*     */     throws Exception
/*     */   {
/* 332 */     if ((ex instanceof Exception)) {
/* 333 */       throw ((Exception)ex);
/*     */     }
/* 335 */     if ((ex instanceof Error)) {
/* 336 */       throw ((Error)ex);
/*     */     }
/* 338 */     throw new UndeclaredThrowableException(ex);
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
/*     */   public static boolean declaresException(Method method, Class<?> exceptionType)
/*     */   {
/* 351 */     Assert.notNull(method, "Method must not be null");
/* 352 */     Class<?>[] declaredExceptions = method.getExceptionTypes();
/* 353 */     for (Class<?> declaredException : declaredExceptions) {
/* 354 */       if (declaredException.isAssignableFrom(exceptionType)) {
/* 355 */         return true;
/*     */       }
/*     */     }
/* 358 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isPublicStaticFinal(Field field)
/*     */   {
/* 366 */     int modifiers = field.getModifiers();
/* 367 */     return (Modifier.isPublic(modifiers)) && (Modifier.isStatic(modifiers)) && (Modifier.isFinal(modifiers));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isEqualsMethod(Method method)
/*     */   {
/* 375 */     if ((method == null) || (!method.getName().equals("equals"))) {
/* 376 */       return false;
/*     */     }
/* 378 */     Class<?>[] paramTypes = method.getParameterTypes();
/* 379 */     return (paramTypes.length == 1) && (paramTypes[0] == Object.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isHashCodeMethod(Method method)
/*     */   {
/* 387 */     return (method != null) && (method.getName().equals("hashCode")) && (method.getParameterTypes().length == 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isToStringMethod(Method method)
/*     */   {
/* 395 */     return (method != null) && (method.getName().equals("toString")) && (method.getParameterTypes().length == 0);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean isObjectMethod(Method method)
/*     */   {
/* 402 */     if (method == null) {
/* 403 */       return false;
/*     */     }
/*     */     try {
/* 406 */       Object.class.getDeclaredMethod(method.getName(), method.getParameterTypes());
/* 407 */       return true;
/*     */     }
/*     */     catch (Exception ex) {}
/* 410 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static boolean isCglibRenamedMethod(Method renamedMethod)
/*     */   {
/* 421 */     String name = renamedMethod.getName();
/* 422 */     if (name.startsWith("CGLIB$")) {
/* 423 */       int i = name.length() - 1;
/* 424 */       while ((i >= 0) && (Character.isDigit(name.charAt(i)))) {
/* 425 */         i--;
/*     */       }
/* 427 */       return (i > "CGLIB$".length()) && 
/* 428 */         (i < name.length() - 1) && (name.charAt(i) == '$');
/*     */     }
/* 430 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void makeAccessible(Field field)
/*     */   {
/* 442 */     if (((!Modifier.isPublic(field.getModifiers())) || 
/* 443 */       (!Modifier.isPublic(field.getDeclaringClass().getModifiers())) || 
/* 444 */       (Modifier.isFinal(field.getModifiers()))) && (!field.isAccessible())) {
/* 445 */       field.setAccessible(true);
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
/*     */   public static void makeAccessible(Method method)
/*     */   {
/* 458 */     if (((!Modifier.isPublic(method.getModifiers())) || 
/* 459 */       (!Modifier.isPublic(method.getDeclaringClass().getModifiers()))) && (!method.isAccessible())) {
/* 460 */       method.setAccessible(true);
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
/*     */   public static void makeAccessible(Constructor<?> ctor)
/*     */   {
/* 473 */     if (((!Modifier.isPublic(ctor.getModifiers())) || 
/* 474 */       (!Modifier.isPublic(ctor.getDeclaringClass().getModifiers()))) && (!ctor.isAccessible())) {
/* 475 */       ctor.setAccessible(true);
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
/*     */   public static void doWithLocalMethods(Class<?> clazz, MethodCallback mc)
/*     */   {
/* 489 */     Method[] methods = getDeclaredMethods(clazz);
/* 490 */     for (Method method : methods) {
/*     */       try {
/* 492 */         mc.doWith(method);
/*     */       }
/*     */       catch (IllegalAccessException ex) {
/* 495 */         throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + ex);
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
/*     */ 
/*     */   public static void doWithMethods(Class<?> clazz, MethodCallback mc)
/*     */   {
/* 510 */     doWithMethods(clazz, mc, null);
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
/*     */   public static void doWithMethods(Class<?> clazz, MethodCallback mc, MethodFilter mf)
/*     */   {
/* 524 */     Method[] methods = getDeclaredMethods(clazz);
/* 525 */     for (Method method : methods) {
/* 526 */       if ((mf == null) || (mf.matches(method)))
/*     */       {
/*     */         try
/*     */         {
/* 530 */           mc.doWith(method);
/*     */         }
/*     */         catch (IllegalAccessException ex) {
/* 533 */           throw new IllegalStateException("Not allowed to access method '" + method.getName() + "': " + ex);
/*     */         } }
/*     */     }
/* 536 */     if (clazz.getSuperclass() != null) {
/* 537 */       doWithMethods(clazz.getSuperclass(), mc, mf);
/*     */     }
/* 539 */     else if (clazz.isInterface()) {
/* 540 */       for (Class<?> superIfc : clazz.getInterfaces()) {
/* 541 */         doWithMethods(superIfc, mc, mf);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Method[] getAllDeclaredMethods(Class<?> leafClass)
/*     */   {
/* 552 */     List<Method> methods = new ArrayList(32);
/* 553 */     doWithMethods(leafClass, new MethodCallback()
/*     */     {
/*     */       public void doWith(Method method) {
/* 556 */         this.val$methods.add(method);
/*     */       }
/* 558 */     });
/* 559 */     return (Method[])methods.toArray(new Method[methods.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Method[] getUniqueDeclaredMethods(Class<?> leafClass)
/*     */   {
/* 569 */     List<Method> methods = new ArrayList(32);
/* 570 */     doWithMethods(leafClass, new MethodCallback()
/*     */     {
/*     */       public void doWith(Method method) {
/* 573 */         boolean knownSignature = false;
/* 574 */         Method methodBeingOverriddenWithCovariantReturnType = null;
/* 575 */         for (Method existingMethod : this.val$methods) {
/* 576 */           if ((method.getName().equals(existingMethod.getName())) && 
/* 577 */             (Arrays.equals(method.getParameterTypes(), existingMethod.getParameterTypes())))
/*     */           {
/* 579 */             if ((existingMethod.getReturnType() != method.getReturnType()) && 
/* 580 */               (existingMethod.getReturnType().isAssignableFrom(method.getReturnType()))) {
/* 581 */               methodBeingOverriddenWithCovariantReturnType = existingMethod; break;
/*     */             }
/*     */             
/* 584 */             knownSignature = true;
/*     */             
/* 586 */             break;
/*     */           }
/*     */         }
/* 589 */         if (methodBeingOverriddenWithCovariantReturnType != null) {
/* 590 */           this.val$methods.remove(methodBeingOverriddenWithCovariantReturnType);
/*     */         }
/* 592 */         if ((!knownSignature) && (!ReflectionUtils.isCglibRenamedMethod(method))) {
/* 593 */           this.val$methods.add(method);
/*     */         }
/*     */       }
/* 596 */     });
/* 597 */     return (Method[])methods.toArray(new Method[methods.size()]);
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
/*     */   private static Method[] getDeclaredMethods(Class<?> clazz)
/*     */   {
/* 610 */     Assert.notNull(clazz, "Class must not be null");
/* 611 */     Method[] result = (Method[])declaredMethodsCache.get(clazz);
/* 612 */     if (result == null) {
/* 613 */       Method[] declaredMethods = clazz.getDeclaredMethods();
/* 614 */       List<Method> defaultMethods = findConcreteMethodsOnInterfaces(clazz);
/* 615 */       int index; if (defaultMethods != null) {
/* 616 */         result = new Method[declaredMethods.length + defaultMethods.size()];
/* 617 */         System.arraycopy(declaredMethods, 0, result, 0, declaredMethods.length);
/* 618 */         index = declaredMethods.length;
/* 619 */         for (Method defaultMethod : defaultMethods) {
/* 620 */           result[index] = defaultMethod;
/* 621 */           index++;
/*     */         }
/*     */       }
/*     */       else {
/* 625 */         result = declaredMethods;
/*     */       }
/* 627 */       declaredMethodsCache.put(clazz, result.length == 0 ? NO_METHODS : result);
/*     */     }
/* 629 */     return result;
/*     */   }
/*     */   
/*     */   private static List<Method> findConcreteMethodsOnInterfaces(Class<?> clazz) {
/* 633 */     List<Method> result = null;
/* 634 */     for (Class<?> ifc : clazz.getInterfaces()) {
/* 635 */       for (Method ifcMethod : ifc.getMethods()) {
/* 636 */         if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
/* 637 */           if (result == null) {
/* 638 */             result = new LinkedList();
/*     */           }
/* 640 */           result.add(ifcMethod);
/*     */         }
/*     */       }
/*     */     }
/* 644 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void doWithLocalFields(Class<?> clazz, FieldCallback fc)
/*     */   {
/* 656 */     for (Field field : getDeclaredFields(clazz)) {
/*     */       try {
/* 658 */         fc.doWith(field);
/*     */       }
/*     */       catch (IllegalAccessException ex) {
/* 661 */         throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void doWithFields(Class<?> clazz, FieldCallback fc)
/*     */   {
/* 673 */     doWithFields(clazz, fc, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void doWithFields(Class<?> clazz, FieldCallback fc, FieldFilter ff)
/*     */   {
/* 685 */     Class<?> targetClass = clazz;
/*     */     do {
/* 687 */       Field[] fields = getDeclaredFields(targetClass);
/* 688 */       for (Field field : fields) {
/* 689 */         if ((ff == null) || (ff.matches(field)))
/*     */         {
/*     */           try
/*     */           {
/* 693 */             fc.doWith(field);
/*     */           }
/*     */           catch (IllegalAccessException ex) {
/* 696 */             throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + ex);
/*     */           } }
/*     */       }
/* 699 */       targetClass = targetClass.getSuperclass();
/*     */     }
/* 701 */     while ((targetClass != null) && (targetClass != Object.class));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Field[] getDeclaredFields(Class<?> clazz)
/*     */   {
/* 712 */     Assert.notNull(clazz, "Class must not be null");
/* 713 */     Field[] result = (Field[])declaredFieldsCache.get(clazz);
/* 714 */     if (result == null) {
/* 715 */       result = clazz.getDeclaredFields();
/* 716 */       declaredFieldsCache.put(clazz, result.length == 0 ? NO_FIELDS : result);
/*     */     }
/* 718 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void shallowCopyFieldState(Object src, final Object dest)
/*     */   {
/* 727 */     Assert.notNull(src, "Source for field copy cannot be null");
/* 728 */     Assert.notNull(dest, "Destination for field copy cannot be null");
/* 729 */     if (!src.getClass().isAssignableFrom(dest.getClass()))
/*     */     {
/* 731 */       throw new IllegalArgumentException("Destination class [" + dest.getClass().getName() + "] must be same or subclass as source class [" + src.getClass().getName() + "]");
/*     */     }
/* 733 */     doWithFields(src.getClass(), new FieldCallback()
/*     */     {
/*     */       public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
/* 736 */         ReflectionUtils.makeAccessible(field);
/* 737 */         Object srcValue = field.get(this.val$src);
/* 738 */         field.set(dest, srcValue); } }, COPYABLE_FIELDS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void clearCache()
/*     */   {
/* 748 */     declaredMethodsCache.clear();
/* 749 */     declaredFieldsCache.clear();
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
/* 808 */   public static final FieldFilter COPYABLE_FIELDS = new FieldFilter()
/*     */   {
/*     */     public boolean matches(Field field)
/*     */     {
/* 812 */       return (!Modifier.isStatic(field.getModifiers())) && (!Modifier.isFinal(field.getModifiers()));
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 820 */   public static final MethodFilter NON_BRIDGED_METHODS = new MethodFilter()
/*     */   {
/*     */     public boolean matches(Method method)
/*     */     {
/* 824 */       return !method.isBridge();
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 833 */   public static final MethodFilter USER_DECLARED_METHODS = new MethodFilter()
/*     */   {
/*     */     public boolean matches(Method method)
/*     */     {
/* 837 */       return (!method.isBridge()) && (method.getDeclaringClass() != Object.class);
/*     */     }
/*     */   };
/*     */   
/*     */   public static abstract interface FieldFilter
/*     */   {
/*     */     public abstract boolean matches(Field paramField);
/*     */   }
/*     */   
/*     */   public static abstract interface FieldCallback
/*     */   {
/*     */     public abstract void doWith(Field paramField)
/*     */       throws IllegalArgumentException, IllegalAccessException;
/*     */   }
/*     */   
/*     */   public static abstract interface MethodFilter
/*     */   {
/*     */     public abstract boolean matches(Method paramMethod);
/*     */   }
/*     */   
/*     */   public static abstract interface MethodCallback
/*     */   {
/*     */     public abstract void doWith(Method paramMethod)
/*     */       throws IllegalArgumentException, IllegalAccessException;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframewor\\util\ReflectionUtils.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */