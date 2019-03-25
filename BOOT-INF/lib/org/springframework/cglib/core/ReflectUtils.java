/*     */ package org.springframework.cglib.core;
/*     */ 
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.springframework.asm.Attribute;
/*     */ import org.springframework.asm.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectUtils
/*     */ {
/*  34 */   private static final Map primitives = new HashMap(8);
/*  35 */   private static final Map transforms = new HashMap(8);
/*  36 */   private static final ClassLoader defaultLoader = ReflectUtils.class.getClassLoader();
/*     */   private static Method DEFINE_CLASS;
/*     */   private static Method DEFINE_CLASS_UNSAFE;
/*     */   private static final ProtectionDomain PROTECTION_DOMAIN;
/*     */   private static final Object UNSAFE;
/*     */   private static final Throwable THROWABLE;
/*  42 */   private static final List<Method> OBJECT_METHODS = new ArrayList();
/*     */   
/*     */   private static final String[] CGLIB_PACKAGES;
/*     */   
/*     */   static
/*     */   {
/*  48 */     Throwable throwable = null;
/*     */     ProtectionDomain protectionDomain;
/*  50 */     Method defineClass; Method defineClassUnsafe; Object unsafe; try { ProtectionDomain protectionDomain = getProtectionDomain(ReflectUtils.class);
/*     */       Method defineClassUnsafe;
/*  52 */       try { Method defineClass = (Method)AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */           public Object run() throws Exception {
/*  54 */             Class loader = Class.forName("java.lang.ClassLoader");
/*  55 */             Method defineClass = loader.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class });
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  61 */             defineClass.setAccessible(true);
/*  62 */             return defineClass;
/*     */           }
/*  64 */         });
/*  65 */         Method defineClassUnsafe = null;
/*  66 */         unsafe = null;
/*     */       } catch (Throwable t) {
/*     */         Object unsafe;
/*  69 */         throwable = t;
/*  70 */         Method defineClass = null;
/*  71 */         Object unsafe = AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */           public Object run() throws Exception {
/*  73 */             Class u = Class.forName("sun.misc.Unsafe");
/*  74 */             Field theUnsafe = u.getDeclaredField("theUnsafe");
/*  75 */             theUnsafe.setAccessible(true);
/*  76 */             return theUnsafe.get(null);
/*     */           }
/*  78 */         });
/*  79 */         Class u = Class.forName("sun.misc.Unsafe");
/*  80 */         defineClassUnsafe = u.getMethod("defineClass", new Class[] { String.class, byte[].class, Integer.TYPE, Integer.TYPE, ClassLoader.class, ProtectionDomain.class });
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  88 */       AccessController.doPrivileged(new PrivilegedExceptionAction() {
/*     */         public Object run() throws Exception {
/*  90 */           Method[] methods = Object.class.getDeclaredMethods();
/*  91 */           for (Method method : methods) {
/*  92 */             if ((!"finalize".equals(method.getName())) && 
/*  93 */               ((method.getModifiers() & 0x18) <= 0))
/*     */             {
/*     */ 
/*  96 */               ReflectUtils.OBJECT_METHODS.add(method); }
/*     */           }
/*  98 */           return null;
/*     */         }
/*     */       });
/*     */     } catch (Throwable t) {
/* 102 */       if (throwable == null) {
/* 103 */         throwable = t;
/*     */       }
/* 105 */       protectionDomain = null;
/* 106 */       defineClass = null;
/* 107 */       defineClassUnsafe = null;
/* 108 */       unsafe = null;
/*     */     }
/* 110 */     PROTECTION_DOMAIN = protectionDomain;
/* 111 */     DEFINE_CLASS = defineClass;
/* 112 */     DEFINE_CLASS_UNSAFE = defineClassUnsafe;
/* 113 */     UNSAFE = unsafe;
/* 114 */     THROWABLE = throwable;
/*     */     
/*     */ 
/* 117 */     CGLIB_PACKAGES = new String[] { "java.lang" };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 122 */     primitives.put("byte", Byte.TYPE);
/* 123 */     primitives.put("char", Character.TYPE);
/* 124 */     primitives.put("double", Double.TYPE);
/* 125 */     primitives.put("float", Float.TYPE);
/* 126 */     primitives.put("int", Integer.TYPE);
/* 127 */     primitives.put("long", Long.TYPE);
/* 128 */     primitives.put("short", Short.TYPE);
/* 129 */     primitives.put("boolean", Boolean.TYPE);
/*     */     
/* 131 */     transforms.put("byte", "B");
/* 132 */     transforms.put("char", "C");
/* 133 */     transforms.put("double", "D");
/* 134 */     transforms.put("float", "F");
/* 135 */     transforms.put("int", "I");
/* 136 */     transforms.put("long", "J");
/* 137 */     transforms.put("short", "S");
/* 138 */     transforms.put("boolean", "Z");
/*     */   }
/*     */   
/*     */   public static ProtectionDomain getProtectionDomain(Class source) {
/* 142 */     if (source == null) {
/* 143 */       return null;
/*     */     }
/* 145 */     (ProtectionDomain)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Object run() {
/* 147 */         return this.val$source.getProtectionDomain();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   public static Type[] getExceptionTypes(Member member) {
/* 153 */     if ((member instanceof Method))
/* 154 */       return TypeUtils.getTypes(((Method)member).getExceptionTypes());
/* 155 */     if ((member instanceof Constructor)) {
/* 156 */       return TypeUtils.getTypes(((Constructor)member).getExceptionTypes());
/*     */     }
/* 158 */     throw new IllegalArgumentException("Cannot get exception types of a field");
/*     */   }
/*     */   
/*     */   public static Signature getSignature(Member member)
/*     */   {
/* 163 */     if ((member instanceof Method))
/* 164 */       return new Signature(member.getName(), Type.getMethodDescriptor((Method)member));
/* 165 */     if ((member instanceof Constructor)) {
/* 166 */       Type[] types = TypeUtils.getTypes(((Constructor)member).getParameterTypes());
/* 167 */       return new Signature("<init>", 
/* 168 */         Type.getMethodDescriptor(Type.VOID_TYPE, types));
/*     */     }
/*     */     
/* 171 */     throw new IllegalArgumentException("Cannot get signature of a field");
/*     */   }
/*     */   
/*     */   public static Constructor findConstructor(String desc)
/*     */   {
/* 176 */     return findConstructor(desc, defaultLoader);
/*     */   }
/*     */   
/*     */   public static Constructor findConstructor(String desc, ClassLoader loader) {
/*     */     try {
/* 181 */       int lparen = desc.indexOf('(');
/* 182 */       String className = desc.substring(0, lparen).trim();
/* 183 */       return getClass(className, loader).getConstructor(parseTypes(desc, loader));
/*     */     } catch (ClassNotFoundException e) {
/* 185 */       throw new CodeGenerationException(e);
/*     */     } catch (NoSuchMethodException e) {
/* 187 */       throw new CodeGenerationException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Method findMethod(String desc) {
/* 192 */     return findMethod(desc, defaultLoader);
/*     */   }
/*     */   
/*     */   public static Method findMethod(String desc, ClassLoader loader) {
/*     */     try {
/* 197 */       int lparen = desc.indexOf('(');
/* 198 */       int dot = desc.lastIndexOf('.', lparen);
/* 199 */       String className = desc.substring(0, dot).trim();
/* 200 */       String methodName = desc.substring(dot + 1, lparen).trim();
/* 201 */       return getClass(className, loader).getDeclaredMethod(methodName, parseTypes(desc, loader));
/*     */     } catch (ClassNotFoundException e) {
/* 203 */       throw new CodeGenerationException(e);
/*     */     } catch (NoSuchMethodException e) {
/* 205 */       throw new CodeGenerationException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   private static Class[] parseTypes(String desc, ClassLoader loader) throws ClassNotFoundException {
/* 210 */     int lparen = desc.indexOf('(');
/* 211 */     int rparen = desc.indexOf(')', lparen);
/* 212 */     List params = new ArrayList();
/* 213 */     int start = lparen + 1;
/*     */     for (;;) {
/* 215 */       int comma = desc.indexOf(',', start);
/* 216 */       if (comma < 0) {
/*     */         break;
/*     */       }
/* 219 */       params.add(desc.substring(start, comma).trim());
/* 220 */       start = comma + 1;
/*     */     }
/* 222 */     if (start < rparen) {
/* 223 */       params.add(desc.substring(start, rparen).trim());
/*     */     }
/* 225 */     Class[] types = new Class[params.size()];
/* 226 */     for (int i = 0; i < types.length; i++) {
/* 227 */       types[i] = getClass((String)params.get(i), loader);
/*     */     }
/* 229 */     return types;
/*     */   }
/*     */   
/*     */   private static Class getClass(String className, ClassLoader loader) throws ClassNotFoundException {
/* 233 */     return getClass(className, loader, CGLIB_PACKAGES);
/*     */   }
/*     */   
/*     */   private static Class getClass(String className, ClassLoader loader, String[] packages) throws ClassNotFoundException {
/* 237 */     String save = className;
/* 238 */     int dimensions = 0;
/* 239 */     int index = 0;
/* 240 */     while ((index = className.indexOf("[]", index) + 1) > 0) {
/* 241 */       dimensions++;
/*     */     }
/* 243 */     StringBuffer brackets = new StringBuffer(className.length() - dimensions);
/* 244 */     for (int i = 0; i < dimensions; i++) {
/* 245 */       brackets.append('[');
/*     */     }
/* 247 */     className = className.substring(0, className.length() - 2 * dimensions);
/*     */     
/* 249 */     String prefix = dimensions > 0 ? brackets + "L" : "";
/* 250 */     String suffix = dimensions > 0 ? ";" : "";
/*     */     try {
/* 252 */       return Class.forName(prefix + className + suffix, false, loader);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 254 */       for (int i = 0; i < packages.length; i++) {
/*     */         try {
/* 256 */           return Class.forName(prefix + packages[i] + '.' + className + suffix, false, loader);
/*     */         } catch (ClassNotFoundException localClassNotFoundException1) {}
/*     */       }
/* 259 */       if (dimensions == 0) {
/* 260 */         Class c = (Class)primitives.get(className);
/* 261 */         if (c != null) {
/* 262 */           return c;
/*     */         }
/*     */       } else {
/* 265 */         String transform = (String)transforms.get(className);
/* 266 */         if (transform != null) {
/*     */           try {
/* 268 */             return Class.forName(brackets + transform, false, loader);
/*     */           } catch (ClassNotFoundException localClassNotFoundException2) {}
/*     */         }
/*     */       }
/* 272 */       throw new ClassNotFoundException(save);
/*     */     }
/*     */   }
/*     */   
/*     */   public static Object newInstance(Class type) {
/* 277 */     return newInstance(type, Constants.EMPTY_CLASS_ARRAY, null);
/*     */   }
/*     */   
/*     */   public static Object newInstance(Class type, Class[] parameterTypes, Object[] args) {
/* 281 */     return newInstance(getConstructor(type, parameterTypes), args);
/*     */   }
/*     */   
/*     */   public static Object newInstance(Constructor cstruct, Object[] args)
/*     */   {
/* 286 */     boolean flag = cstruct.isAccessible();
/*     */     try {
/* 288 */       if (!flag) {
/* 289 */         cstruct.setAccessible(true);
/*     */       }
/* 291 */       Object result = cstruct.newInstance(args);
/* 292 */       return result;
/*     */     } catch (InstantiationException e) {
/* 294 */       throw new CodeGenerationException(e);
/*     */     } catch (IllegalAccessException e) {
/* 296 */       throw new CodeGenerationException(e);
/*     */     } catch (InvocationTargetException e) {
/* 298 */       throw new CodeGenerationException(e.getTargetException());
/*     */     } finally {
/* 300 */       if (!flag) {
/* 301 */         cstruct.setAccessible(flag);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static Constructor getConstructor(Class type, Class[] parameterTypes)
/*     */   {
/*     */     try {
/* 309 */       Constructor constructor = type.getDeclaredConstructor(parameterTypes);
/* 310 */       constructor.setAccessible(true);
/* 311 */       return constructor;
/*     */     } catch (NoSuchMethodException e) {
/* 313 */       throw new CodeGenerationException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public static String[] getNames(Class[] classes)
/*     */   {
/* 319 */     if (classes == null)
/* 320 */       return null;
/* 321 */     String[] names = new String[classes.length];
/* 322 */     for (int i = 0; i < names.length; i++) {
/* 323 */       names[i] = classes[i].getName();
/*     */     }
/* 325 */     return names;
/*     */   }
/*     */   
/*     */   public static Class[] getClasses(Object[] objects) {
/* 329 */     Class[] classes = new Class[objects.length];
/* 330 */     for (int i = 0; i < objects.length; i++) {
/* 331 */       classes[i] = objects[i].getClass();
/*     */     }
/* 333 */     return classes;
/*     */   }
/*     */   
/*     */   public static Method findNewInstance(Class iface) {
/* 337 */     Method m = findInterfaceMethod(iface);
/* 338 */     if (!m.getName().equals("newInstance")) {
/* 339 */       throw new IllegalArgumentException(iface + " missing newInstance method");
/*     */     }
/* 341 */     return m;
/*     */   }
/*     */   
/*     */   public static Method[] getPropertyMethods(PropertyDescriptor[] properties, boolean read, boolean write) {
/* 345 */     Set methods = new HashSet();
/* 346 */     for (int i = 0; i < properties.length; i++) {
/* 347 */       PropertyDescriptor pd = properties[i];
/* 348 */       if (read) {
/* 349 */         methods.add(pd.getReadMethod());
/*     */       }
/* 351 */       if (write) {
/* 352 */         methods.add(pd.getWriteMethod());
/*     */       }
/*     */     }
/* 355 */     methods.remove(null);
/* 356 */     return (Method[])methods.toArray(new Method[methods.size()]);
/*     */   }
/*     */   
/*     */   public static PropertyDescriptor[] getBeanProperties(Class type) {
/* 360 */     return getPropertiesHelper(type, true, true);
/*     */   }
/*     */   
/*     */   public static PropertyDescriptor[] getBeanGetters(Class type) {
/* 364 */     return getPropertiesHelper(type, true, false);
/*     */   }
/*     */   
/*     */   public static PropertyDescriptor[] getBeanSetters(Class type) {
/* 368 */     return getPropertiesHelper(type, false, true);
/*     */   }
/*     */   
/*     */   private static PropertyDescriptor[] getPropertiesHelper(Class type, boolean read, boolean write) {
/*     */     try {
/* 373 */       BeanInfo info = Introspector.getBeanInfo(type, Object.class);
/* 374 */       PropertyDescriptor[] all = info.getPropertyDescriptors();
/* 375 */       if ((read) && (write)) {
/* 376 */         return all;
/*     */       }
/* 378 */       List properties = new ArrayList(all.length);
/* 379 */       for (int i = 0; i < all.length; i++) {
/* 380 */         PropertyDescriptor pd = all[i];
/* 381 */         if (((read) && (pd.getReadMethod() != null)) || ((write) && 
/* 382 */           (pd.getWriteMethod() != null))) {
/* 383 */           properties.add(pd);
/*     */         }
/*     */       }
/* 386 */       return (PropertyDescriptor[])properties.toArray(new PropertyDescriptor[properties.size()]);
/*     */     } catch (IntrospectionException e) {
/* 388 */       throw new CodeGenerationException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Method findDeclaredMethod(Class type, String methodName, Class[] parameterTypes)
/*     */     throws NoSuchMethodException
/*     */   {
/* 398 */     Class cl = type;
/* 399 */     while (cl != null) {
/*     */       try {
/* 401 */         return cl.getDeclaredMethod(methodName, parameterTypes);
/*     */       } catch (NoSuchMethodException e) {
/* 403 */         cl = cl.getSuperclass();
/*     */       }
/*     */     }
/* 406 */     throw new NoSuchMethodException(methodName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static List addAllMethods(Class type, List list)
/*     */   {
/* 413 */     if (type == Object.class) {
/* 414 */       list.addAll(OBJECT_METHODS);
/*     */     } else {
/* 416 */       list.addAll(Arrays.asList(type.getDeclaredMethods()));
/*     */     }
/* 418 */     Class superclass = type.getSuperclass();
/* 419 */     if (superclass != null) {
/* 420 */       addAllMethods(superclass, list);
/*     */     }
/* 422 */     Class[] interfaces = type.getInterfaces();
/* 423 */     for (int i = 0; i < interfaces.length; i++) {
/* 424 */       addAllMethods(interfaces[i], list);
/*     */     }
/*     */     
/* 427 */     return list;
/*     */   }
/*     */   
/*     */   public static List addAllInterfaces(Class type, List list) {
/* 431 */     Class superclass = type.getSuperclass();
/* 432 */     if (superclass != null) {
/* 433 */       list.addAll(Arrays.asList(type.getInterfaces()));
/* 434 */       addAllInterfaces(superclass, list);
/*     */     }
/* 436 */     return list;
/*     */   }
/*     */   
/*     */   public static Method findInterfaceMethod(Class iface)
/*     */   {
/* 441 */     if (!iface.isInterface()) {
/* 442 */       throw new IllegalArgumentException(iface + " is not an interface");
/*     */     }
/* 444 */     Method[] methods = iface.getDeclaredMethods();
/* 445 */     if (methods.length != 1) {
/* 446 */       throw new IllegalArgumentException("expecting exactly 1 method in " + iface);
/*     */     }
/* 448 */     return methods[0];
/*     */   }
/*     */   
/*     */   public static Class defineClass(String className, byte[] b, ClassLoader loader) throws Exception {
/* 452 */     return defineClass(className, b, loader, PROTECTION_DOMAIN);
/*     */   }
/*     */   
/*     */   public static Class defineClass(String className, byte[] b, ClassLoader loader, ProtectionDomain protectionDomain) throws Exception {
/*     */     Class c;
/* 457 */     if (DEFINE_CLASS != null) {
/* 458 */       Object[] args = { className, b, new Integer(0), new Integer(b.length), protectionDomain };
/* 459 */       c = (Class)DEFINE_CLASS.invoke(loader, args); } else { Class c;
/* 460 */       if (DEFINE_CLASS_UNSAFE != null) {
/* 461 */         Object[] args = { className, b, new Integer(0), new Integer(b.length), loader, protectionDomain };
/* 462 */         c = (Class)DEFINE_CLASS_UNSAFE.invoke(UNSAFE, args);
/*     */       } else {
/* 464 */         throw new CodeGenerationException(THROWABLE);
/*     */       } }
/*     */     Class c;
/* 467 */     Class.forName(className, true, loader);
/* 468 */     return c;
/*     */   }
/*     */   
/*     */   public static int findPackageProtected(Class[] classes) {
/* 472 */     for (int i = 0; i < classes.length; i++) {
/* 473 */       if (!Modifier.isPublic(classes[i].getModifiers())) {
/* 474 */         return i;
/*     */       }
/*     */     }
/* 477 */     return 0;
/*     */   }
/*     */   
/*     */   public static MethodInfo getMethodInfo(Member member, final int modifiers) {
/* 481 */     final Signature sig = getSignature(member);
/* 482 */     new MethodInfo() {
/*     */       private ClassInfo ci;
/*     */       
/* 485 */       public ClassInfo getClassInfo() { if (this.ci == null)
/* 486 */           this.ci = ReflectUtils.getClassInfo(this.val$member.getDeclaringClass());
/* 487 */         return this.ci;
/*     */       }
/*     */       
/* 490 */       public int getModifiers() { return modifiers; }
/*     */       
/*     */       public Signature getSignature() {
/* 493 */         return sig;
/*     */       }
/*     */       
/* 496 */       public Type[] getExceptionTypes() { return ReflectUtils.getExceptionTypes(this.val$member); }
/*     */       
/*     */       public Attribute getAttribute() {
/* 499 */         return null;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public static MethodInfo getMethodInfo(Member member) {
/* 505 */     return getMethodInfo(member, member.getModifiers());
/*     */   }
/*     */   
/*     */   public static ClassInfo getClassInfo(final Class clazz) {
/* 509 */     Type type = Type.getType(clazz);
/* 510 */     final Type sc = clazz.getSuperclass() == null ? null : Type.getType(clazz.getSuperclass());
/* 511 */     new ClassInfo() {
/*     */       public Type getType() {
/* 513 */         return this.val$type;
/*     */       }
/*     */       
/* 516 */       public Type getSuperType() { return sc; }
/*     */       
/*     */       public Type[] getInterfaces() {
/* 519 */         return TypeUtils.getTypes(clazz.getInterfaces());
/*     */       }
/*     */       
/* 522 */       public int getModifiers() { return clazz.getModifiers(); }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static Method[] findMethods(String[] namesAndDescriptors, Method[] methods)
/*     */   {
/* 530 */     Map map = new HashMap();
/* 531 */     for (int i = 0; i < methods.length; i++) {
/* 532 */       Method method = methods[i];
/* 533 */       map.put(method.getName() + Type.getMethodDescriptor(method), method);
/*     */     }
/* 535 */     Method[] result = new Method[namesAndDescriptors.length / 2];
/* 536 */     for (int i = 0; i < result.length; i++) {
/* 537 */       result[i] = ((Method)map.get(namesAndDescriptors[(i * 2)] + namesAndDescriptors[(i * 2 + 1)]));
/* 538 */       if (result[i] != null) {}
/*     */     }
/*     */     
/*     */ 
/* 542 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-core-4.3.14.RELEASE.jar!\org\springframework\cglib\core\ReflectUtils.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */