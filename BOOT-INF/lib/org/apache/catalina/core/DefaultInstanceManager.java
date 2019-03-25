/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Properties;
/*     */ import java.util.Set;
/*     */ import javax.annotation.PostConstruct;
/*     */ import javax.annotation.PreDestroy;
/*     */ import javax.annotation.Resource;
/*     */ import javax.ejb.EJB;
/*     */ import javax.naming.NamingException;
/*     */ import javax.persistence.PersistenceContext;
/*     */ import javax.persistence.PersistenceUnit;
/*     */ import javax.xml.ws.WebServiceRef;
/*     */ import org.apache.catalina.ContainerServlet;
/*     */ import org.apache.catalina.Globals;
/*     */ import org.apache.catalina.Loader;
/*     */ import org.apache.catalina.security.SecurityUtil;
/*     */ import org.apache.catalina.util.Introspection;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.InstanceManager;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.collections.ManagedConcurrentWeakHashMap;
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
/*     */ public class DefaultInstanceManager
/*     */   implements InstanceManager
/*     */ {
/*  61 */   private static final AnnotationCacheEntry[] ANNOTATIONS_EMPTY = new AnnotationCacheEntry[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  68 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*     */   private static final boolean EJB_PRESENT;
/*     */   private static final boolean JPA_PRESENT;
/*     */   private static final boolean WS_PRESENT;
/*     */   private final javax.naming.Context context;
/*     */   private final Map<String, Map<String, String>> injectionMap;
/*     */   
/*  75 */   static { Class<?> clazz = null;
/*     */     try {
/*  77 */       clazz = Class.forName("javax.ejb.EJB");
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {}
/*     */     
/*  81 */     EJB_PRESENT = clazz != null;
/*     */     
/*  83 */     clazz = null;
/*     */     try {
/*  85 */       clazz = Class.forName("javax.persistence.PersistenceContext");
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException1) {}
/*     */     
/*  89 */     JPA_PRESENT = clazz != null;
/*     */     
/*  91 */     clazz = null;
/*     */     try {
/*  93 */       clazz = Class.forName("javax.xml.ws.WebServiceRef");
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException2) {}
/*     */     
/*  97 */     WS_PRESENT = clazz != null;
/*     */   }
/*     */   
/*     */ 
/*     */   protected final ClassLoader classLoader;
/*     */   
/*     */   protected final ClassLoader containerClassLoader;
/*     */   
/*     */   protected final boolean privileged;
/*     */   protected final boolean ignoreAnnotations;
/*     */   private final Set<String> restrictedClasses;
/* 108 */   private final ManagedConcurrentWeakHashMap<Class<?>, AnnotationCacheEntry[]> annotationCache = new ManagedConcurrentWeakHashMap();
/*     */   
/*     */   private final Map<String, String> postConstructMethods;
/*     */   
/*     */   private final Map<String, String> preDestroyMethods;
/*     */   
/*     */ 
/*     */   public DefaultInstanceManager(javax.naming.Context context, Map<String, Map<String, String>> injectionMap, org.apache.catalina.Context catalinaContext, ClassLoader containerClassLoader)
/*     */   {
/* 117 */     this.classLoader = catalinaContext.getLoader().getClassLoader();
/* 118 */     this.privileged = catalinaContext.getPrivileged();
/* 119 */     this.containerClassLoader = containerClassLoader;
/* 120 */     this.ignoreAnnotations = catalinaContext.getIgnoreAnnotations();
/* 121 */     Log log = catalinaContext.getLogger();
/* 122 */     Set<String> classNames = new HashSet();
/* 123 */     loadProperties(classNames, "org/apache/catalina/core/RestrictedServlets.properties", "defaultInstanceManager.restrictedServletsResource", log);
/*     */     
/*     */ 
/* 126 */     loadProperties(classNames, "org/apache/catalina/core/RestrictedListeners.properties", "defaultInstanceManager.restrictedListenersResource", log);
/*     */     
/*     */ 
/* 129 */     loadProperties(classNames, "org/apache/catalina/core/RestrictedFilters.properties", "defaultInstanceManager.restrictedFiltersResource", log);
/*     */     
/*     */ 
/* 132 */     this.restrictedClasses = Collections.unmodifiableSet(classNames);
/* 133 */     this.context = context;
/* 134 */     this.injectionMap = injectionMap;
/* 135 */     this.postConstructMethods = catalinaContext.findPostConstructMethods();
/* 136 */     this.preDestroyMethods = catalinaContext.findPreDestroyMethods();
/*     */   }
/*     */   
/*     */ 
/*     */   public Object newInstance(Class<?> clazz)
/*     */     throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, IllegalArgumentException, NoSuchMethodException, SecurityException
/*     */   {
/* 143 */     return newInstance(clazz.getConstructor(new Class[0]).newInstance(new Object[0]), clazz);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object newInstance(String className)
/*     */     throws IllegalAccessException, InvocationTargetException, NamingException, InstantiationException, ClassNotFoundException, IllegalArgumentException, NoSuchMethodException, SecurityException
/*     */   {
/* 150 */     Class<?> clazz = loadClassMaybePrivileged(className, this.classLoader);
/* 151 */     return newInstance(clazz.getConstructor(new Class[0]).newInstance(new Object[0]), clazz);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object newInstance(String className, ClassLoader classLoader)
/*     */     throws IllegalAccessException, NamingException, InvocationTargetException, InstantiationException, ClassNotFoundException, IllegalArgumentException, NoSuchMethodException, SecurityException
/*     */   {
/* 159 */     Class<?> clazz = classLoader.loadClass(className);
/* 160 */     return newInstance(clazz.getConstructor(new Class[0]).newInstance(new Object[0]), clazz);
/*     */   }
/*     */   
/*     */   public void newInstance(Object o)
/*     */     throws IllegalAccessException, InvocationTargetException, NamingException
/*     */   {
/* 166 */     newInstance(o, o.getClass());
/*     */   }
/*     */   
/*     */   private Object newInstance(Object instance, Class<?> clazz) throws IllegalAccessException, InvocationTargetException, NamingException
/*     */   {
/* 171 */     if (!this.ignoreAnnotations) {
/* 172 */       Map<String, String> injections = assembleInjectionsFromClassHierarchy(clazz);
/* 173 */       populateAnnotationsCache(clazz, injections);
/* 174 */       processAnnotations(instance, injections);
/* 175 */       postConstruct(instance, clazz);
/*     */     }
/* 177 */     return instance;
/*     */   }
/*     */   
/*     */   private Map<String, String> assembleInjectionsFromClassHierarchy(Class<?> clazz) {
/* 181 */     Map<String, String> injections = new HashMap();
/* 182 */     Map<String, String> currentInjections = null;
/* 183 */     while (clazz != null) {
/* 184 */       currentInjections = (Map)this.injectionMap.get(clazz.getName());
/* 185 */       if (currentInjections != null) {
/* 186 */         injections.putAll(currentInjections);
/*     */       }
/* 188 */       clazz = clazz.getSuperclass();
/*     */     }
/* 190 */     return injections;
/*     */   }
/*     */   
/*     */   public void destroyInstance(Object instance)
/*     */     throws IllegalAccessException, InvocationTargetException
/*     */   {
/* 196 */     if (!this.ignoreAnnotations) {
/* 197 */       preDestroy(instance, instance.getClass());
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
/*     */   protected void postConstruct(Object instance, Class<?> clazz)
/*     */     throws IllegalAccessException, InvocationTargetException
/*     */   {
/* 213 */     if (this.context == null)
/*     */     {
/* 215 */       return;
/*     */     }
/*     */     
/* 218 */     Class<?> superClass = clazz.getSuperclass();
/* 219 */     if (superClass != Object.class) {
/* 220 */       postConstruct(instance, superClass);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 225 */     AnnotationCacheEntry[] annotations = (AnnotationCacheEntry[])this.annotationCache.get(clazz);
/* 226 */     for (AnnotationCacheEntry entry : annotations) {
/* 227 */       if (entry.getType() == AnnotationCacheEntryType.POST_CONSTRUCT) {
/* 228 */         Method postConstruct = getMethod(clazz, entry);
/* 229 */         synchronized (postConstruct) {
/* 230 */           boolean accessibility = postConstruct.isAccessible();
/* 231 */           postConstruct.setAccessible(true);
/* 232 */           postConstruct.invoke(instance, new Object[0]);
/* 233 */           postConstruct.setAccessible(accessibility);
/*     */         }
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
/*     */ 
/*     */ 
/*     */   protected void preDestroy(Object instance, Class<?> clazz)
/*     */     throws IllegalAccessException, InvocationTargetException
/*     */   {
/* 252 */     Class<?> superClass = clazz.getSuperclass();
/* 253 */     if (superClass != Object.class) {
/* 254 */       preDestroy(instance, superClass);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 259 */     AnnotationCacheEntry[] annotations = (AnnotationCacheEntry[])this.annotationCache.get(clazz);
/* 260 */     if (annotations == null)
/*     */     {
/* 262 */       return;
/*     */     }
/* 264 */     for (AnnotationCacheEntry entry : annotations) {
/* 265 */       if (entry.getType() == AnnotationCacheEntryType.PRE_DESTROY) {
/* 266 */         Method preDestroy = getMethod(clazz, entry);
/* 267 */         synchronized (preDestroy) {
/* 268 */           boolean accessibility = preDestroy.isAccessible();
/* 269 */           preDestroy.setAccessible(true);
/* 270 */           preDestroy.invoke(instance, new Object[0]);
/* 271 */           preDestroy.setAccessible(accessibility);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void backgroundProcess()
/*     */   {
/* 279 */     this.annotationCache.maintain();
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
/*     */   protected void populateAnnotationsCache(Class<?> clazz, Map<String, String> injections)
/*     */     throws IllegalAccessException, InvocationTargetException, NamingException
/*     */   {
/* 299 */     List<AnnotationCacheEntry> annotations = null;
/*     */     
/* 301 */     while (clazz != null) {
/* 302 */       AnnotationCacheEntry[] annotationsArray = (AnnotationCacheEntry[])this.annotationCache.get(clazz);
/* 303 */       if (annotationsArray == null) {
/* 304 */         if (annotations == null) {
/* 305 */           annotations = new ArrayList();
/*     */         } else {
/* 307 */           annotations.clear();
/*     */         }
/*     */         
/* 310 */         if (this.context != null)
/*     */         {
/*     */ 
/* 313 */           Field[] fields = Introspection.getDeclaredFields(clazz);
/* 314 */           for (Field field : fields)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 320 */             if ((injections != null) && (injections.containsKey(field.getName()))) {
/* 321 */               annotations.add(new AnnotationCacheEntry(field
/* 322 */                 .getName(), null, 
/* 323 */                 (String)injections.get(field.getName()), AnnotationCacheEntryType.FIELD));
/*     */ 
/*     */             }
/* 326 */             else if ((resourceAnnotation = (Resource)field.getAnnotation(Resource.class)) != null) {
/* 327 */               annotations.add(new AnnotationCacheEntry(field.getName(), null, resourceAnnotation
/* 328 */                 .name(), AnnotationCacheEntryType.FIELD));
/* 329 */             } else if ((EJB_PRESENT) && 
/* 330 */               ((ejbAnnotation = field.getAnnotation(EJB.class)) != null)) {
/* 331 */               annotations.add(new AnnotationCacheEntry(field.getName(), null, ((EJB)ejbAnnotation)
/* 332 */                 .name(), AnnotationCacheEntryType.FIELD));
/* 333 */             } else if ((WS_PRESENT) && 
/* 334 */               ((webServiceRefAnnotation = field.getAnnotation(WebServiceRef.class)) != null)) {
/* 335 */               annotations.add(new AnnotationCacheEntry(field.getName(), null, ((WebServiceRef)webServiceRefAnnotation)
/* 336 */                 .name(), AnnotationCacheEntryType.FIELD));
/*     */             } else { Annotation persistenceContextAnnotation;
/* 338 */               if ((JPA_PRESENT) && 
/* 339 */                 ((persistenceContextAnnotation = field.getAnnotation(PersistenceContext.class)) != null)) {
/* 340 */                 annotations.add(new AnnotationCacheEntry(field.getName(), null, ((PersistenceContext)persistenceContextAnnotation)
/* 341 */                   .name(), AnnotationCacheEntryType.FIELD));
/*     */               } else { Annotation persistenceUnitAnnotation;
/* 343 */                 if ((JPA_PRESENT) && 
/* 344 */                   ((persistenceUnitAnnotation = field.getAnnotation(PersistenceUnit.class)) != null)) {
/* 345 */                   annotations.add(new AnnotationCacheEntry(field.getName(), null, ((PersistenceUnit)persistenceUnitAnnotation)
/* 346 */                     .name(), AnnotationCacheEntryType.FIELD));
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 353 */         Method[] methods = Introspection.getDeclaredMethods(clazz);
/* 354 */         Method postConstruct = null;
/* 355 */         String postConstructFromXml = (String)this.postConstructMethods.get(clazz.getName());
/* 356 */         Method preDestroy = null;
/* 357 */         String preDestroyFromXml = (String)this.preDestroyMethods.get(clazz.getName());
/* 358 */         Resource resourceAnnotation = methods;Annotation ejbAnnotation = resourceAnnotation.length; for (Annotation webServiceRefAnnotation = 0; webServiceRefAnnotation < ejbAnnotation; webServiceRefAnnotation++) { Method method = resourceAnnotation[webServiceRefAnnotation];
/* 359 */           if (this.context != null)
/*     */           {
/* 361 */             if ((injections != null) && 
/* 362 */               (Introspection.isValidSetter(method))) {
/* 363 */               String fieldName = Introspection.getPropertyName(method);
/* 364 */               if (injections.containsKey(fieldName)) {
/* 365 */                 annotations.add(new AnnotationCacheEntry(method
/* 366 */                   .getName(), method
/* 367 */                   .getParameterTypes(), 
/* 368 */                   (String)injections.get(fieldName), AnnotationCacheEntryType.SETTER));
/*     */                 
/* 370 */                 continue;
/*     */               }
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */             Resource resourceAnnotation;
/*     */             
/*     */ 
/* 379 */             if ((resourceAnnotation = (Resource)method.getAnnotation(Resource.class)) != null) {
/* 380 */               annotations.add(new AnnotationCacheEntry(method
/* 381 */                 .getName(), method
/* 382 */                 .getParameterTypes(), resourceAnnotation
/* 383 */                 .name(), AnnotationCacheEntryType.SETTER));
/*     */             } else { Annotation ejbAnnotation;
/* 385 */               if ((EJB_PRESENT) && 
/* 386 */                 ((ejbAnnotation = method.getAnnotation(EJB.class)) != null)) {
/* 387 */                 annotations.add(new AnnotationCacheEntry(method
/* 388 */                   .getName(), method
/* 389 */                   .getParameterTypes(), ((EJB)ejbAnnotation)
/* 390 */                   .name(), AnnotationCacheEntryType.SETTER));
/*     */               } else { Annotation webServiceRefAnnotation;
/* 392 */                 if ((WS_PRESENT) && 
/* 393 */                   ((webServiceRefAnnotation = method.getAnnotation(WebServiceRef.class)) != null)) {
/* 394 */                   annotations.add(new AnnotationCacheEntry(method
/* 395 */                     .getName(), method
/* 396 */                     .getParameterTypes(), ((WebServiceRef)webServiceRefAnnotation)
/* 397 */                     .name(), AnnotationCacheEntryType.SETTER));
/*     */                 } else { Annotation persistenceContextAnnotation;
/* 399 */                   if ((JPA_PRESENT) && 
/* 400 */                     ((persistenceContextAnnotation = method.getAnnotation(PersistenceContext.class)) != null)) {
/* 401 */                     annotations.add(new AnnotationCacheEntry(method
/* 402 */                       .getName(), method
/* 403 */                       .getParameterTypes(), ((PersistenceContext)persistenceContextAnnotation)
/* 404 */                       .name(), AnnotationCacheEntryType.SETTER));
/*     */                   } else { Annotation persistenceUnitAnnotation;
/* 406 */                     if ((JPA_PRESENT) && 
/* 407 */                       ((persistenceUnitAnnotation = method.getAnnotation(PersistenceUnit.class)) != null))
/* 408 */                       annotations.add(new AnnotationCacheEntry(method
/* 409 */                         .getName(), method
/* 410 */                         .getParameterTypes(), ((PersistenceUnit)persistenceUnitAnnotation)
/* 411 */                         .name(), AnnotationCacheEntryType.SETTER));
/*     */                   }
/*     */                 }
/*     */               }
/*     */             } }
/* 416 */           postConstruct = findPostConstruct(postConstruct, postConstructFromXml, method);
/*     */           
/* 418 */           preDestroy = findPreDestroy(preDestroy, preDestroyFromXml, method);
/*     */         }
/*     */         
/* 421 */         if (postConstruct != null) {
/* 422 */           annotations.add(new AnnotationCacheEntry(postConstruct
/* 423 */             .getName(), postConstruct
/* 424 */             .getParameterTypes(), null, AnnotationCacheEntryType.POST_CONSTRUCT));
/*     */         }
/* 426 */         else if (postConstructFromXml != null)
/*     */         {
/* 428 */           throw new IllegalArgumentException("Post construct method " + postConstructFromXml + " for class " + clazz.getName() + " is declared in deployment descriptor but cannot be found.");
/*     */         }
/*     */         
/* 431 */         if (preDestroy != null) {
/* 432 */           annotations.add(new AnnotationCacheEntry(preDestroy
/* 433 */             .getName(), preDestroy
/* 434 */             .getParameterTypes(), null, AnnotationCacheEntryType.PRE_DESTROY));
/*     */         }
/* 436 */         else if (preDestroyFromXml != null)
/*     */         {
/* 438 */           throw new IllegalArgumentException("Pre destroy method " + preDestroyFromXml + " for class " + clazz.getName() + " is declared in deployment descriptor but cannot be found.");
/*     */         }
/*     */         
/* 441 */         if (annotations.isEmpty())
/*     */         {
/* 443 */           annotationsArray = ANNOTATIONS_EMPTY;
/*     */         } else {
/* 445 */           annotationsArray = (AnnotationCacheEntry[])annotations.toArray(
/* 446 */             new AnnotationCacheEntry[annotations.size()]);
/*     */         }
/* 448 */         synchronized (this.annotationCache) {
/* 449 */           this.annotationCache.put(clazz, annotationsArray);
/*     */         }
/*     */       }
/* 452 */       clazz = clazz.getSuperclass();
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
/*     */   protected void processAnnotations(Object instance, Map<String, String> injections)
/*     */     throws IllegalAccessException, InvocationTargetException, NamingException
/*     */   {
/* 470 */     if (this.context == null)
/*     */     {
/* 472 */       return;
/*     */     }
/*     */     
/* 475 */     Class<?> clazz = instance.getClass();
/*     */     
/* 477 */     while (clazz != null) {
/* 478 */       AnnotationCacheEntry[] annotations = (AnnotationCacheEntry[])this.annotationCache.get(clazz);
/* 479 */       for (AnnotationCacheEntry entry : annotations) {
/* 480 */         if (entry.getType() == AnnotationCacheEntryType.SETTER) {
/* 481 */           lookupMethodResource(this.context, instance, 
/* 482 */             getMethod(clazz, entry), entry
/* 483 */             .getName(), clazz);
/* 484 */         } else if (entry.getType() == AnnotationCacheEntryType.FIELD) {
/* 485 */           lookupFieldResource(this.context, instance, 
/* 486 */             getField(clazz, entry), entry
/* 487 */             .getName(), clazz);
/*     */         }
/*     */       }
/* 490 */       clazz = clazz.getSuperclass();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int getAnnotationCacheSize()
/*     */   {
/* 501 */     return this.annotationCache.size();
/*     */   }
/*     */   
/*     */   protected Class<?> loadClassMaybePrivileged(final String className, final ClassLoader classLoader)
/*     */     throws ClassNotFoundException
/*     */   {
/*     */     Class<?> clazz;
/* 508 */     if (SecurityUtil.isPackageProtectionEnabled()) {
/*     */       try {
/* 510 */         clazz = (Class)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */ 
/*     */           public Class<?> run() throws Exception {
/* 514 */             return DefaultInstanceManager.this.loadClass(className, classLoader); }
/*     */         });
/*     */       } catch (PrivilegedActionException e) {
/*     */         Class<?> clazz;
/* 518 */         Throwable t = e.getCause();
/* 519 */         if ((t instanceof ClassNotFoundException)) {
/* 520 */           throw ((ClassNotFoundException)t);
/*     */         }
/* 522 */         throw new RuntimeException(t);
/*     */       }
/*     */     } else {
/* 525 */       clazz = loadClass(className, classLoader);
/*     */     }
/* 527 */     checkAccess(clazz);
/* 528 */     return clazz;
/*     */   }
/*     */   
/*     */   protected Class<?> loadClass(String className, ClassLoader classLoader) throws ClassNotFoundException
/*     */   {
/* 533 */     if (className.startsWith("org.apache.catalina")) {
/* 534 */       return this.containerClassLoader.loadClass(className);
/*     */     }
/*     */     try {
/* 537 */       Class<?> clazz = this.containerClassLoader.loadClass(className);
/* 538 */       if (ContainerServlet.class.isAssignableFrom(clazz)) {
/* 539 */         return clazz;
/*     */       }
/*     */     } catch (Throwable t) {
/* 542 */       ExceptionUtils.handleThrowable(t);
/*     */     }
/* 544 */     return classLoader.loadClass(className);
/*     */   }
/*     */   
/*     */   private void checkAccess(Class<?> clazz) {
/* 548 */     if (this.privileged) {
/* 549 */       return;
/*     */     }
/* 551 */     if (ContainerServlet.class.isAssignableFrom(clazz)) {
/* 552 */       throw new SecurityException(sm.getString("defaultInstanceManager.restrictedContainerServlet", new Object[] { clazz }));
/*     */     }
/*     */     
/* 555 */     while (clazz != null) {
/* 556 */       if (this.restrictedClasses.contains(clazz.getName())) {
/* 557 */         throw new SecurityException(sm.getString("defaultInstanceManager.restrictedClass", new Object[] { clazz }));
/*     */       }
/*     */       
/* 560 */       clazz = clazz.getSuperclass();
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
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static void lookupFieldResource(javax.naming.Context context, Object instance, Field field, String name, Class<?> clazz)
/*     */     throws NamingException, IllegalAccessException
/*     */   {
/* 582 */     String normalizedName = normalize(name);
/*     */     Object lookedupResource;
/* 584 */     Object lookedupResource; if ((normalizedName != null) && (normalizedName.length() > 0)) {
/* 585 */       lookedupResource = context.lookup(normalizedName);
/*     */     }
/*     */     else {
/* 588 */       lookedupResource = context.lookup(clazz.getName() + "/" + field.getName());
/*     */     }
/*     */     
/* 591 */     synchronized (field) {
/* 592 */       boolean accessibility = field.isAccessible();
/* 593 */       field.setAccessible(true);
/* 594 */       field.set(instance, lookedupResource);
/* 595 */       field.setAccessible(accessibility);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     boolean accessibility;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static void lookupMethodResource(javax.naming.Context context, Object instance, Method method, String name, Class<?> clazz)
/*     */     throws NamingException, IllegalAccessException, InvocationTargetException
/*     */   {
/* 616 */     if (!Introspection.isValidSetter(method))
/*     */     {
/* 618 */       throw new IllegalArgumentException(sm.getString("defaultInstanceManager.invalidInjection"));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 624 */     String normalizedName = normalize(name);
/*     */     Object lookedupResource;
/* 626 */     Object lookedupResource; if ((normalizedName != null) && (normalizedName.length() > 0)) {
/* 627 */       lookedupResource = context.lookup(normalizedName);
/*     */     } else {
/* 629 */       lookedupResource = context.lookup(clazz
/* 630 */         .getName() + "/" + Introspection.getPropertyName(method));
/*     */     }
/*     */     
/* 633 */     synchronized (method) {
/* 634 */       boolean accessibility = method.isAccessible();
/* 635 */       method.setAccessible(true);
/* 636 */       method.invoke(instance, new Object[] { lookedupResource });
/* 637 */       method.setAccessible(accessibility);
/*     */     }
/*     */     boolean accessibility;
/*     */   }
/*     */   
/*     */   private static void loadProperties(Set<String> classNames, String resourceName, String messageKey, Log log) {
/* 643 */     Properties properties = new Properties();
/* 644 */     ClassLoader cl = DefaultInstanceManager.class.getClassLoader();
/* 645 */     try { InputStream is = cl.getResourceAsStream(resourceName);Throwable localThrowable3 = null;
/* 646 */       try { if (is == null) {
/* 647 */           log.error(sm.getString(messageKey, new Object[] { resourceName }));
/*     */         } else {
/* 649 */           properties.load(is);
/*     */         }
/*     */       }
/*     */       catch (Throwable localThrowable1)
/*     */       {
/* 645 */         localThrowable3 = localThrowable1;throw localThrowable1;
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*     */ 
/* 651 */         if (is != null) if (localThrowable3 != null) try { is.close(); } catch (Throwable localThrowable2) { localThrowable3.addSuppressed(localThrowable2); } else is.close();
/* 652 */       } } catch (IOException ioe) { log.error(sm.getString(messageKey, new Object[] { resourceName }), ioe);
/*     */     }
/* 654 */     if (properties.isEmpty()) {
/* 655 */       return;
/*     */     }
/* 657 */     for (Object e : properties.entrySet()) {
/* 658 */       if ("restricted".equals(((Map.Entry)e).getValue())) {
/* 659 */         classNames.add(((Map.Entry)e).getKey().toString());
/*     */       } else {
/* 661 */         log.warn(sm.getString("defaultInstanceManager.restrictedWrongValue", new Object[] { resourceName, ((Map.Entry)e)
/*     */         
/* 663 */           .getKey(), ((Map.Entry)e).getValue() }));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private static String normalize(String jndiName) {
/* 669 */     if ((jndiName != null) && (jndiName.startsWith("java:comp/env/"))) {
/* 670 */       return jndiName.substring(14);
/*     */     }
/* 672 */     return jndiName;
/*     */   }
/*     */   
/*     */   private static Method getMethod(Class<?> clazz, final AnnotationCacheEntry entry)
/*     */   {
/* 677 */     Method result = null;
/* 678 */     if (Globals.IS_SECURITY_ENABLED) {
/* 679 */       result = (Method)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Method run()
/*     */         {
/* 683 */           Method result = null;
/*     */           try {
/* 685 */             result = this.val$clazz.getDeclaredMethod(entry
/* 686 */               .getAccessibleObjectName(), entry
/* 687 */               .getParamTypes());
/*     */           }
/*     */           catch (NoSuchMethodException localNoSuchMethodException) {}
/*     */           
/*     */ 
/* 692 */           return result;
/*     */         }
/*     */       });
/*     */     } else {
/*     */       try {
/* 697 */         result = clazz.getDeclaredMethod(entry
/* 698 */           .getAccessibleObjectName(), entry.getParamTypes());
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException) {}
/*     */     }
/*     */     
/* 703 */     return result;
/*     */   }
/*     */   
/*     */   private static Field getField(Class<?> clazz, final AnnotationCacheEntry entry)
/*     */   {
/* 708 */     Field result = null;
/* 709 */     if (Globals.IS_SECURITY_ENABLED) {
/* 710 */       result = (Field)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Field run()
/*     */         {
/* 714 */           Field result = null;
/*     */           try {
/* 716 */             result = this.val$clazz.getDeclaredField(entry
/* 717 */               .getAccessibleObjectName());
/*     */           }
/*     */           catch (NoSuchFieldException localNoSuchFieldException) {}
/*     */           
/*     */ 
/* 722 */           return result;
/*     */         }
/*     */       });
/*     */     } else {
/*     */       try {
/* 727 */         result = clazz.getDeclaredField(entry
/* 728 */           .getAccessibleObjectName());
/*     */       }
/*     */       catch (NoSuchFieldException localNoSuchFieldException) {}
/*     */     }
/*     */     
/* 733 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   private static Method findPostConstruct(Method currentPostConstruct, String postConstructFromXml, Method method)
/*     */   {
/* 739 */     return findLifecycleCallback(currentPostConstruct, postConstructFromXml, method, PostConstruct.class);
/*     */   }
/*     */   
/*     */ 
/*     */   private static Method findPreDestroy(Method currentPreDestroy, String preDestroyFromXml, Method method)
/*     */   {
/* 745 */     return findLifecycleCallback(currentPreDestroy, preDestroyFromXml, method, PreDestroy.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static Method findLifecycleCallback(Method currentMethod, String methodNameFromXml, Method method, Class<? extends Annotation> annotation)
/*     */   {
/* 752 */     Method result = currentMethod;
/* 753 */     if (methodNameFromXml != null) {
/* 754 */       if (method.getName().equals(methodNameFromXml)) {
/* 755 */         if (!Introspection.isValidLifecycleCallback(method))
/*     */         {
/* 757 */           throw new IllegalArgumentException("Invalid " + annotation.getName() + " annotation");
/*     */         }
/* 759 */         result = method;
/*     */       }
/*     */     }
/* 762 */     else if (method.isAnnotationPresent(annotation)) {
/* 763 */       if ((currentMethod != null) || (!Introspection.isValidLifecycleCallback(method)))
/*     */       {
/* 765 */         throw new IllegalArgumentException("Invalid " + annotation.getName() + " annotation");
/*     */       }
/* 767 */       result = method;
/*     */     }
/*     */     
/* 770 */     return result;
/*     */   }
/*     */   
/*     */   private static final class AnnotationCacheEntry
/*     */   {
/*     */     private final String accessibleObjectName;
/*     */     private final Class<?>[] paramTypes;
/*     */     private final String name;
/*     */     private final DefaultInstanceManager.AnnotationCacheEntryType type;
/*     */     
/*     */     public AnnotationCacheEntry(String accessibleObjectName, Class<?>[] paramTypes, String name, DefaultInstanceManager.AnnotationCacheEntryType type)
/*     */     {
/* 782 */       this.accessibleObjectName = accessibleObjectName;
/* 783 */       this.paramTypes = paramTypes;
/* 784 */       this.name = name;
/* 785 */       this.type = type;
/*     */     }
/*     */     
/*     */     public String getAccessibleObjectName() {
/* 789 */       return this.accessibleObjectName;
/*     */     }
/*     */     
/*     */     public Class<?>[] getParamTypes() {
/* 793 */       return this.paramTypes;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 797 */       return this.name;
/*     */     }
/*     */     
/* 800 */     public DefaultInstanceManager.AnnotationCacheEntryType getType() { return this.type; }
/*     */   }
/*     */   
/*     */   private static enum AnnotationCacheEntryType
/*     */   {
/* 805 */     FIELD,  SETTER,  POST_CONSTRUCT,  PRE_DESTROY;
/*     */     
/*     */     private AnnotationCacheEntryType() {}
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\DefaultInstanceManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */