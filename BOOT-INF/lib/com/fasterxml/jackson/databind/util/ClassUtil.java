/*      */ package com.fasterxml.jackson.databind.util;
/*      */ 
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import java.io.IOException;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Member;
/*      */ import java.util.Collection;
/*      */ import java.util.List;
/*      */ 
/*      */ public final class ClassUtil
/*      */ {
/*   15 */   private static final Class<?> CLS_OBJECT = Object.class;
/*      */   
/*   17 */   private static final Annotation[] NO_ANNOTATIONS = new Annotation[0];
/*   18 */   private static final Ctor[] NO_CTORS = new Ctor[0];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final class EmptyIterator<T>
/*      */     implements java.util.Iterator<T>
/*      */   {
/*   31 */     public boolean hasNext() { return false; }
/*   32 */     public T next() { throw new java.util.NoSuchElementException(); }
/*   33 */     public void remove() { throw new UnsupportedOperationException(); }
/*      */   }
/*      */   
/*   36 */   private static final EmptyIterator<?> EMPTY_ITERATOR = new EmptyIterator(null);
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
/*      */   public static <T> java.util.Iterator<T> emptyIterator()
/*      */   {
/*   51 */     return EMPTY_ITERATOR;
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
/*      */ 
/*      */   public static List<JavaType> findSuperTypes(JavaType type, Class<?> endBefore, boolean addClassItself)
/*      */   {
/*   76 */     if ((type == null) || (type.hasRawClass(endBefore)) || (type.hasRawClass(Object.class))) {
/*   77 */       return java.util.Collections.emptyList();
/*      */     }
/*   79 */     List<JavaType> result = new java.util.ArrayList(8);
/*   80 */     _addSuperTypes(type, endBefore, result, addClassItself);
/*   81 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static List<Class<?>> findRawSuperTypes(Class<?> cls, Class<?> endBefore, boolean addClassItself)
/*      */   {
/*   88 */     if ((cls == null) || (cls == endBefore) || (cls == Object.class)) {
/*   89 */       return java.util.Collections.emptyList();
/*      */     }
/*   91 */     List<Class<?>> result = new java.util.ArrayList(8);
/*   92 */     _addRawSuperTypes(cls, endBefore, result, addClassItself);
/*   93 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static List<Class<?>> findSuperClasses(Class<?> cls, Class<?> endBefore, boolean addClassItself)
/*      */   {
/*  105 */     List<Class<?>> result = new java.util.LinkedList();
/*  106 */     if ((cls != null) && (cls != endBefore)) {
/*  107 */       if (addClassItself) {
/*  108 */         result.add(cls);
/*      */       }
/*  110 */       while (((cls = cls.getSuperclass()) != null) && 
/*  111 */         (cls != endBefore))
/*      */       {
/*      */ 
/*  114 */         result.add(cls);
/*      */       }
/*      */     }
/*  117 */     return result;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore) {
/*  122 */     return findSuperTypes(cls, endBefore, new java.util.ArrayList(8));
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public static List<Class<?>> findSuperTypes(Class<?> cls, Class<?> endBefore, List<Class<?>> result) {
/*  127 */     _addRawSuperTypes(cls, endBefore, result, false);
/*  128 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   private static void _addSuperTypes(JavaType type, Class<?> endBefore, Collection<JavaType> result, boolean addClassItself)
/*      */   {
/*  134 */     if (type == null) {
/*  135 */       return;
/*      */     }
/*  137 */     Class<?> cls = type.getRawClass();
/*  138 */     if ((cls == endBefore) || (cls == Object.class)) return;
/*  139 */     if (addClassItself) {
/*  140 */       if (result.contains(type)) {
/*  141 */         return;
/*      */       }
/*  143 */       result.add(type);
/*      */     }
/*  145 */     for (JavaType intCls : type.getInterfaces()) {
/*  146 */       _addSuperTypes(intCls, endBefore, result, true);
/*      */     }
/*  148 */     _addSuperTypes(type.getSuperClass(), endBefore, result, true);
/*      */   }
/*      */   
/*      */   private static void _addRawSuperTypes(Class<?> cls, Class<?> endBefore, Collection<Class<?>> result, boolean addClassItself) {
/*  152 */     if ((cls == endBefore) || (cls == null) || (cls == Object.class)) return;
/*  153 */     if (addClassItself) {
/*  154 */       if (result.contains(cls)) {
/*  155 */         return;
/*      */       }
/*  157 */       result.add(cls);
/*      */     }
/*  159 */     for (Class<?> intCls : _interfaces(cls)) {
/*  160 */       _addRawSuperTypes(intCls, endBefore, result, true);
/*      */     }
/*  162 */     _addRawSuperTypes(cls.getSuperclass(), endBefore, result, true);
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
/*      */   public static String canBeABeanType(Class<?> type)
/*      */   {
/*  178 */     if (type.isAnnotation()) {
/*  179 */       return "annotation";
/*      */     }
/*  181 */     if (type.isArray()) {
/*  182 */       return "array";
/*      */     }
/*  184 */     if (type.isEnum()) {
/*  185 */       return "enum";
/*      */     }
/*  187 */     if (type.isPrimitive()) {
/*  188 */       return "primitive";
/*      */     }
/*      */     
/*      */ 
/*  192 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static String isLocalType(Class<?> type, boolean allowNonStatic)
/*      */   {
/*      */     try
/*      */     {
/*  203 */       if (hasEnclosingMethod(type)) {
/*  204 */         return "local/anonymous";
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  211 */       if ((!allowNonStatic) && 
/*  212 */         (!java.lang.reflect.Modifier.isStatic(type.getModifiers())) && 
/*  213 */         (getEnclosingClass(type) != null)) {
/*  214 */         return "non-static member class";
/*      */       }
/*      */     }
/*      */     catch (SecurityException e) {}catch (NullPointerException e) {}
/*      */     
/*      */ 
/*      */ 
/*  221 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?> getOuterClass(Class<?> type)
/*      */   {
/*      */     try
/*      */     {
/*  232 */       if (hasEnclosingMethod(type)) {
/*  233 */         return null;
/*      */       }
/*  235 */       if (!java.lang.reflect.Modifier.isStatic(type.getModifiers())) {
/*  236 */         return getEnclosingClass(type);
/*      */       }
/*      */     } catch (SecurityException e) {}
/*  239 */     return null;
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
/*      */   public static boolean isProxyType(Class<?> type)
/*      */   {
/*  257 */     String name = type.getName();
/*      */     
/*  259 */     if ((name.startsWith("net.sf.cglib.proxy.")) || (name.startsWith("org.hibernate.proxy.")))
/*      */     {
/*  261 */       return true;
/*      */     }
/*      */     
/*  264 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isConcrete(Class<?> type)
/*      */   {
/*  273 */     int mod = type.getModifiers();
/*  274 */     return (mod & 0x600) == 0;
/*      */   }
/*      */   
/*      */   public static boolean isConcrete(Member member)
/*      */   {
/*  279 */     int mod = member.getModifiers();
/*  280 */     return (mod & 0x600) == 0;
/*      */   }
/*      */   
/*      */   public static boolean isCollectionMapOrArray(Class<?> type)
/*      */   {
/*  285 */     if (type.isArray()) return true;
/*  286 */     if (Collection.class.isAssignableFrom(type)) return true;
/*  287 */     if (java.util.Map.class.isAssignableFrom(type)) return true;
/*  288 */     return false;
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
/*      */   public static String getClassDescription(Object classOrInstance)
/*      */   {
/*  304 */     if (classOrInstance == null) {
/*  305 */       return "unknown";
/*      */     }
/*  307 */     Class<?> cls = (classOrInstance instanceof Class) ? (Class)classOrInstance : classOrInstance.getClass();
/*      */     
/*  309 */     return cls.getName();
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
/*      */   @Deprecated
/*      */   public static Class<?> findClass(String className)
/*      */     throws ClassNotFoundException
/*      */   {
/*  325 */     if (className.indexOf('.') < 0) {
/*  326 */       if ("int".equals(className)) return Integer.TYPE;
/*  327 */       if ("long".equals(className)) return Long.TYPE;
/*  328 */       if ("float".equals(className)) return Float.TYPE;
/*  329 */       if ("double".equals(className)) return Double.TYPE;
/*  330 */       if ("boolean".equals(className)) return Boolean.TYPE;
/*  331 */       if ("byte".equals(className)) return Byte.TYPE;
/*  332 */       if ("char".equals(className)) return Character.TYPE;
/*  333 */       if ("short".equals(className)) return Short.TYPE;
/*  334 */       if ("void".equals(className)) { return Void.TYPE;
/*      */       }
/*      */     }
/*  337 */     Throwable prob = null;
/*  338 */     ClassLoader loader = Thread.currentThread().getContextClassLoader();
/*      */     
/*  340 */     if (loader != null) {
/*      */       try {
/*  342 */         return Class.forName(className, true, loader);
/*      */       } catch (Exception e) {
/*  344 */         prob = getRootCause(e);
/*      */       }
/*      */     }
/*      */     try {
/*  348 */       return Class.forName(className);
/*      */     } catch (Exception e) {
/*  350 */       if (prob == null) {
/*  351 */         prob = getRootCause(e);
/*      */       }
/*      */       
/*  354 */       if ((prob instanceof RuntimeException)) {
/*  355 */         throw ((RuntimeException)prob);
/*      */       }
/*  357 */       throw new ClassNotFoundException(prob.getMessage(), prob);
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
/*      */   @Deprecated
/*      */   public static boolean hasGetterSignature(java.lang.reflect.Method m)
/*      */   {
/*  373 */     if (java.lang.reflect.Modifier.isStatic(m.getModifiers())) {
/*  374 */       return false;
/*      */     }
/*      */     
/*  377 */     Class<?>[] pts = m.getParameterTypes();
/*  378 */     if ((pts != null) && (pts.length != 0)) {
/*  379 */       return false;
/*      */     }
/*      */     
/*  382 */     if (Void.TYPE == m.getReturnType()) {
/*  383 */       return false;
/*      */     }
/*      */     
/*  386 */     return true;
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
/*      */   public static Throwable getRootCause(Throwable t)
/*      */   {
/*  401 */     while (t.getCause() != null) {
/*  402 */       t = t.getCause();
/*      */     }
/*  404 */     return t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void throwRootCause(Throwable t)
/*      */     throws Exception
/*      */   {
/*  415 */     t = getRootCause(t);
/*  416 */     if ((t instanceof Exception)) {
/*  417 */       throw ((Exception)t);
/*      */     }
/*  419 */     throw ((Error)t);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Throwable throwRootCauseIfIOE(Throwable t)
/*      */     throws IOException
/*      */   {
/*  430 */     t = getRootCause(t);
/*  431 */     if ((t instanceof IOException)) {
/*  432 */       throw ((IOException)t);
/*      */     }
/*  434 */     return t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void throwAsIAE(Throwable t)
/*      */   {
/*  443 */     throwAsIAE(t, t.getMessage());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void throwAsIAE(Throwable t, String msg)
/*      */   {
/*  453 */     if ((t instanceof RuntimeException)) {
/*  454 */       throw ((RuntimeException)t);
/*      */     }
/*  456 */     if ((t instanceof Error)) {
/*  457 */       throw ((Error)t);
/*      */     }
/*  459 */     throw new IllegalArgumentException(msg, t);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void unwrapAndThrowAsIAE(Throwable t)
/*      */   {
/*  469 */     throwAsIAE(getRootCause(t));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static void unwrapAndThrowAsIAE(Throwable t, String msg)
/*      */   {
/*  479 */     throwAsIAE(getRootCause(t), msg);
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
/*      */   public static void closeOnFailAndThrowAsIAE(JsonGenerator g, Exception fail)
/*      */     throws IOException
/*      */   {
/*  497 */     g.disable(com.fasterxml.jackson.core.JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
/*      */     try {
/*  499 */       g.close();
/*      */     } catch (Exception e) {
/*  501 */       fail.addSuppressed(e);
/*      */     }
/*  503 */     if ((fail instanceof IOException)) {
/*  504 */       throw ((IOException)fail);
/*      */     }
/*  506 */     if ((fail instanceof RuntimeException)) {
/*  507 */       throw ((RuntimeException)fail);
/*      */     }
/*  509 */     throw new RuntimeException(fail);
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
/*      */   public static void closeOnFailAndThrowAsIAE(JsonGenerator g, java.io.Closeable toClose, Exception fail)
/*      */     throws IOException
/*      */   {
/*  525 */     if (g != null) {
/*  526 */       g.disable(com.fasterxml.jackson.core.JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT);
/*      */       try {
/*  528 */         g.close();
/*      */       } catch (Exception e) {
/*  530 */         fail.addSuppressed(e);
/*      */       }
/*      */     }
/*  533 */     if (toClose != null) {
/*      */       try {
/*  535 */         toClose.close();
/*      */       } catch (Exception e) {
/*  537 */         fail.addSuppressed(e);
/*      */       }
/*      */     }
/*  540 */     if ((fail instanceof IOException)) {
/*  541 */       throw ((IOException)fail);
/*      */     }
/*  543 */     if ((fail instanceof RuntimeException)) {
/*  544 */       throw ((RuntimeException)fail);
/*      */     }
/*  546 */     throw new RuntimeException(fail);
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
/*      */   public static <T> T createInstance(Class<T> cls, boolean canFixAccess)
/*      */     throws IllegalArgumentException
/*      */   {
/*  571 */     Constructor<T> ctor = findConstructor(cls, canFixAccess);
/*  572 */     if (ctor == null) {
/*  573 */       throw new IllegalArgumentException("Class " + cls.getName() + " has no default (no arg) constructor");
/*      */     }
/*      */     try {
/*  576 */       return (T)ctor.newInstance(new Object[0]);
/*      */     } catch (Exception e) {
/*  578 */       unwrapAndThrowAsIAE(e, "Failed to instantiate class " + cls.getName() + ", problem: " + e.getMessage()); }
/*  579 */     return null;
/*      */   }
/*      */   
/*      */   public static <T> Constructor<T> findConstructor(Class<T> cls, boolean canFixAccess)
/*      */     throws IllegalArgumentException
/*      */   {
/*      */     try
/*      */     {
/*  587 */       Constructor<T> ctor = cls.getDeclaredConstructor(new Class[0]);
/*  588 */       if (canFixAccess) {
/*  589 */         checkAndFixAccess(ctor);
/*      */ 
/*      */       }
/*  592 */       else if (!java.lang.reflect.Modifier.isPublic(ctor.getModifiers())) {
/*  593 */         throw new IllegalArgumentException("Default constructor for " + cls.getName() + " is not accessible (non-public?): not allowed to try modify access via Reflection: can not instantiate type");
/*      */       }
/*      */       
/*  596 */       return ctor;
/*      */     }
/*      */     catch (NoSuchMethodException e) {}catch (Exception e)
/*      */     {
/*  600 */       unwrapAndThrowAsIAE(e, "Failed to find default constructor of class " + cls.getName() + ", problem: " + e.getMessage());
/*      */     }
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
/*      */ 
/*      */   public static Object defaultValue(Class<?> cls)
/*      */   {
/*  617 */     if (cls == Integer.TYPE) {
/*  618 */       return Integer.valueOf(0);
/*      */     }
/*  620 */     if (cls == Long.TYPE) {
/*  621 */       return Long.valueOf(0L);
/*      */     }
/*  623 */     if (cls == Boolean.TYPE) {
/*  624 */       return Boolean.FALSE;
/*      */     }
/*  626 */     if (cls == Double.TYPE) {
/*  627 */       return Double.valueOf(0.0D);
/*      */     }
/*  629 */     if (cls == Float.TYPE) {
/*  630 */       return Float.valueOf(0.0F);
/*      */     }
/*  632 */     if (cls == Byte.TYPE) {
/*  633 */       return Byte.valueOf((byte)0);
/*      */     }
/*  635 */     if (cls == Short.TYPE) {
/*  636 */       return Short.valueOf((short)0);
/*      */     }
/*  638 */     if (cls == Character.TYPE) {
/*  639 */       return Character.valueOf('\000');
/*      */     }
/*  641 */     throw new IllegalArgumentException("Class " + cls.getName() + " is not a primitive type");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?> wrapperType(Class<?> primitiveType)
/*      */   {
/*  650 */     if (primitiveType == Integer.TYPE) {
/*  651 */       return Integer.class;
/*      */     }
/*  653 */     if (primitiveType == Long.TYPE) {
/*  654 */       return Long.class;
/*      */     }
/*  656 */     if (primitiveType == Boolean.TYPE) {
/*  657 */       return Boolean.class;
/*      */     }
/*  659 */     if (primitiveType == Double.TYPE) {
/*  660 */       return Double.class;
/*      */     }
/*  662 */     if (primitiveType == Float.TYPE) {
/*  663 */       return Float.class;
/*      */     }
/*  665 */     if (primitiveType == Byte.TYPE) {
/*  666 */       return Byte.class;
/*      */     }
/*  668 */     if (primitiveType == Short.TYPE) {
/*  669 */       return Short.class;
/*      */     }
/*  671 */     if (primitiveType == Character.TYPE) {
/*  672 */       return Character.class;
/*      */     }
/*  674 */     throw new IllegalArgumentException("Class " + primitiveType.getName() + " is not a primitive type");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?> primitiveType(Class<?> type)
/*      */   {
/*  685 */     if (type.isPrimitive()) {
/*  686 */       return type;
/*      */     }
/*      */     
/*  689 */     if (type == Integer.class) {
/*  690 */       return Integer.TYPE;
/*      */     }
/*  692 */     if (type == Long.class) {
/*  693 */       return Long.TYPE;
/*      */     }
/*  695 */     if (type == Boolean.class) {
/*  696 */       return Boolean.TYPE;
/*      */     }
/*  698 */     if (type == Double.class) {
/*  699 */       return Double.TYPE;
/*      */     }
/*  701 */     if (type == Float.class) {
/*  702 */       return Float.TYPE;
/*      */     }
/*  704 */     if (type == Byte.class) {
/*  705 */       return Byte.TYPE;
/*      */     }
/*  707 */     if (type == Short.class) {
/*  708 */       return Short.TYPE;
/*      */     }
/*  710 */     if (type == Character.class) {
/*  711 */       return Character.TYPE;
/*      */     }
/*  713 */     return null;
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
/*      */   @Deprecated
/*      */   public static void checkAndFixAccess(Member member)
/*      */   {
/*  732 */     checkAndFixAccess(member, false);
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
/*      */   public static void checkAndFixAccess(Member member, boolean force)
/*      */   {
/*  749 */     java.lang.reflect.AccessibleObject ao = (java.lang.reflect.AccessibleObject)member;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     try
/*      */     {
/*  756 */       if ((force) || (!java.lang.reflect.Modifier.isPublic(member.getModifiers())) || (!java.lang.reflect.Modifier.isPublic(member.getDeclaringClass().getModifiers())))
/*      */       {
/*      */ 
/*  759 */         ao.setAccessible(true);
/*      */       }
/*      */     }
/*      */     catch (SecurityException se)
/*      */     {
/*  764 */       if (!ao.isAccessible()) {
/*  765 */         Class<?> declClass = member.getDeclaringClass();
/*  766 */         throw new IllegalArgumentException("Can not access " + member + " (from class " + declClass.getName() + "; failed to set access: " + se.getMessage());
/*      */       }
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
/*      */   public static Class<? extends Enum<?>> findEnumType(java.util.EnumSet<?> s)
/*      */   {
/*  786 */     if (!s.isEmpty()) {
/*  787 */       return findEnumType((Enum)s.iterator().next());
/*      */     }
/*      */     
/*  790 */     return EnumTypeLocator.instance.enumTypeFor(s);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<? extends Enum<?>> findEnumType(java.util.EnumMap<?, ?> m)
/*      */   {
/*  801 */     if (!m.isEmpty()) {
/*  802 */       return findEnumType((Enum)m.keySet().iterator().next());
/*      */     }
/*      */     
/*  805 */     return EnumTypeLocator.instance.enumTypeFor(m);
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
/*      */   public static Class<? extends Enum<?>> findEnumType(Enum<?> en)
/*      */   {
/*  818 */     Class<?> ec = en.getClass();
/*  819 */     if (ec.getSuperclass() != Enum.class) {
/*  820 */       ec = ec.getSuperclass();
/*      */     }
/*  822 */     return ec;
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
/*      */   public static Class<? extends Enum<?>> findEnumType(Class<?> cls)
/*      */   {
/*  835 */     if (cls.getSuperclass() != Enum.class) {
/*  836 */       cls = cls.getSuperclass();
/*      */     }
/*  838 */     return cls;
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
/*      */   public static <T extends Annotation> Enum<?> findFirstAnnotatedEnumValue(Class<Enum<?>> enumClass, Class<T> annotationClass)
/*      */   {
/*  854 */     Field[] fields = getDeclaredFields(enumClass);
/*  855 */     for (Field field : fields) {
/*  856 */       if (field.isEnumConstant()) {
/*  857 */         Annotation defaultValueAnnotation = field.getAnnotation(annotationClass);
/*  858 */         if (defaultValueAnnotation != null) {
/*  859 */           String name = field.getName();
/*  860 */           for (Enum<?> enumValue : (Enum[])enumClass.getEnumConstants()) {
/*  861 */             if (name.equals(enumValue.name())) {
/*  862 */               return enumValue;
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  868 */     return null;
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
/*      */   public static boolean isJacksonStdImpl(Object impl)
/*      */   {
/*  885 */     return (impl != null) && (isJacksonStdImpl(impl.getClass()));
/*      */   }
/*      */   
/*      */   public static boolean isJacksonStdImpl(Class<?> implClass) {
/*  889 */     return implClass.getAnnotation(com.fasterxml.jackson.databind.annotation.JacksonStdImpl.class) != null;
/*      */   }
/*      */   
/*      */   public static boolean isBogusClass(Class<?> cls) {
/*  893 */     return (cls == Void.class) || (cls == Void.TYPE) || (cls == com.fasterxml.jackson.databind.annotation.NoClass.class);
/*      */   }
/*      */   
/*      */   public static boolean isNonStaticInnerClass(Class<?> cls)
/*      */   {
/*  898 */     return (!java.lang.reflect.Modifier.isStatic(cls.getModifiers())) && (getEnclosingClass(cls) != null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static boolean isObjectOrPrimitive(Class<?> cls)
/*      */   {
/*  906 */     return (cls == CLS_OBJECT) || (cls.isPrimitive());
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
/*      */   public static String getPackageName(Class<?> cls)
/*      */   {
/*  922 */     Package pkg = cls.getPackage();
/*  923 */     return pkg == null ? null : pkg.getName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static boolean hasEnclosingMethod(Class<?> cls)
/*      */   {
/*  930 */     return (!isObjectOrPrimitive(cls)) && (cls.getEnclosingMethod() != null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static Field[] getDeclaredFields(Class<?> cls)
/*      */   {
/*  937 */     return cls.getDeclaredFields();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static java.lang.reflect.Method[] getDeclaredMethods(Class<?> cls)
/*      */   {
/*  944 */     return cls.getDeclaredMethods();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static Annotation[] findClassAnnotations(Class<?> cls)
/*      */   {
/*  951 */     if (isObjectOrPrimitive(cls)) {
/*  952 */       return NO_ANNOTATIONS;
/*      */     }
/*  954 */     return cls.getDeclaredAnnotations();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Ctor[] getConstructors(Class<?> cls)
/*      */   {
/*  963 */     if ((cls.isInterface()) || (isObjectOrPrimitive(cls))) {
/*  964 */       return NO_CTORS;
/*      */     }
/*  966 */     Constructor<?>[] rawCtors = cls.getDeclaredConstructors();
/*  967 */     int len = rawCtors.length;
/*  968 */     Ctor[] result = new Ctor[len];
/*  969 */     for (int i = 0; i < len; i++) {
/*  970 */       result[i] = new Ctor(rawCtors[i]);
/*      */     }
/*  972 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?> getDeclaringClass(Class<?> cls)
/*      */   {
/*  982 */     return isObjectOrPrimitive(cls) ? null : cls.getDeclaringClass();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static java.lang.reflect.Type getGenericSuperclass(Class<?> cls)
/*      */   {
/*  989 */     return cls.getGenericSuperclass();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static java.lang.reflect.Type[] getGenericInterfaces(Class<?> cls)
/*      */   {
/*  996 */     return cls.getGenericInterfaces();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static Class<?> getEnclosingClass(Class<?> cls)
/*      */   {
/* 1004 */     return isObjectOrPrimitive(cls) ? null : cls.getEnclosingClass();
/*      */   }
/*      */   
/*      */   private static Class<?>[] _interfaces(Class<?> cls) {
/* 1008 */     return cls.getInterfaces();
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
/*      */   private static class EnumTypeLocator
/*      */   {
/* 1023 */     static final EnumTypeLocator instance = new EnumTypeLocator();
/*      */     
/*      */     private final Field enumSetTypeField;
/*      */     
/*      */     private final Field enumMapTypeField;
/*      */     
/*      */     private EnumTypeLocator()
/*      */     {
/* 1031 */       this.enumSetTypeField = locateField(java.util.EnumSet.class, "elementType", Class.class);
/* 1032 */       this.enumMapTypeField = locateField(java.util.EnumMap.class, "elementType", Class.class);
/*      */     }
/*      */     
/*      */ 
/*      */     public Class<? extends Enum<?>> enumTypeFor(java.util.EnumSet<?> set)
/*      */     {
/* 1038 */       if (this.enumSetTypeField != null) {
/* 1039 */         return (Class)get(set, this.enumSetTypeField);
/*      */       }
/* 1041 */       throw new IllegalStateException("Can not figure out type for EnumSet (odd JDK platform?)");
/*      */     }
/*      */     
/*      */ 
/*      */     public Class<? extends Enum<?>> enumTypeFor(java.util.EnumMap<?, ?> set)
/*      */     {
/* 1047 */       if (this.enumMapTypeField != null) {
/* 1048 */         return (Class)get(set, this.enumMapTypeField);
/*      */       }
/* 1050 */       throw new IllegalStateException("Can not figure out type for EnumMap (odd JDK platform?)");
/*      */     }
/*      */     
/*      */     private Object get(Object bean, Field field)
/*      */     {
/*      */       try {
/* 1056 */         return field.get(bean);
/*      */       } catch (Exception e) {
/* 1058 */         throw new IllegalArgumentException(e);
/*      */       }
/*      */     }
/*      */     
/*      */     private static Field locateField(Class<?> fromClass, String expectedName, Class<?> type)
/*      */     {
/* 1064 */       Field found = null;
/*      */       
/* 1066 */       Field[] fields = ClassUtil.getDeclaredFields(fromClass);
/* 1067 */       for (Field f : fields) {
/* 1068 */         if ((expectedName.equals(f.getName())) && (f.getType() == type)) {
/* 1069 */           found = f;
/* 1070 */           break;
/*      */         }
/*      */       }
/*      */       
/* 1074 */       if (found == null) {
/* 1075 */         for (Field f : fields) {
/* 1076 */           if (f.getType() == type)
/*      */           {
/* 1078 */             if (found != null) return null;
/* 1079 */             found = f;
/*      */           }
/*      */         }
/*      */       }
/* 1083 */       if (found != null) {
/*      */         try {
/* 1085 */           found.setAccessible(true);
/*      */         } catch (Throwable t) {}
/*      */       }
/* 1088 */       return found;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final class Ctor
/*      */   {
/*      */     public final Constructor<?> _ctor;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private Annotation[] _annotations;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private Annotation[][] _paramAnnotations;
/*      */     
/*      */ 
/*      */ 
/* 1112 */     private int _paramCount = -1;
/*      */     
/*      */     public Ctor(Constructor<?> ctor) {
/* 1115 */       this._ctor = ctor;
/*      */     }
/*      */     
/*      */     public Constructor<?> getConstructor() {
/* 1119 */       return this._ctor;
/*      */     }
/*      */     
/*      */     public int getParamCount() {
/* 1123 */       int c = this._paramCount;
/* 1124 */       if (c < 0) {
/* 1125 */         c = this._ctor.getParameterTypes().length;
/* 1126 */         this._paramCount = c;
/*      */       }
/* 1128 */       return c;
/*      */     }
/*      */     
/*      */     public Class<?> getDeclaringClass() {
/* 1132 */       return this._ctor.getDeclaringClass();
/*      */     }
/*      */     
/*      */     public Annotation[] getDeclaredAnnotations()
/*      */     {
/* 1137 */       Annotation[] result = this._annotations;
/* 1138 */       if (result == null) {
/* 1139 */         result = this._ctor.getDeclaredAnnotations();
/* 1140 */         this._annotations = result;
/*      */       }
/* 1142 */       return result;
/*      */     }
/*      */     
/*      */     public Annotation[][] getParameterAnnotations()
/*      */     {
/* 1147 */       Annotation[][] result = this._paramAnnotations;
/* 1148 */       if (result == null) {
/* 1149 */         result = this._ctor.getParameterAnnotations();
/* 1150 */         this._paramAnnotations = result;
/*      */       }
/* 1152 */       return result;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databin\\util\ClassUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */