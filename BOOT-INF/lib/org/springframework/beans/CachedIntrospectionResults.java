/*     */ package org.springframework.beans;
/*     */ 
/*     */ import java.beans.BeanDescriptor;
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.core.SpringProperties;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.core.io.support.SpringFactoriesLoader;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ConcurrentReferenceHashMap;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CachedIntrospectionResults
/*     */ {
/*     */   public static final String IGNORE_BEANINFO_PROPERTY_NAME = "spring.beaninfo.ignore";
/*  97 */   private static final boolean shouldIntrospectorIgnoreBeaninfoClasses = SpringProperties.getFlag("spring.beaninfo.ignore");
/*     */   
/*     */ 
/* 100 */   private static List<BeanInfoFactory> beanInfoFactories = SpringFactoriesLoader.loadFactories(BeanInfoFactory.class, CachedIntrospectionResults.class
/* 101 */     .getClassLoader());
/*     */   
/* 103 */   private static final Log logger = LogFactory.getLog(CachedIntrospectionResults.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 110 */   static final Set<ClassLoader> acceptedClassLoaders = Collections.newSetFromMap(new ConcurrentHashMap(16));
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 116 */   static final ConcurrentMap<Class<?>, CachedIntrospectionResults> strongClassCache = new ConcurrentHashMap(64);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 123 */   static final ConcurrentMap<Class<?>, CachedIntrospectionResults> softClassCache = new ConcurrentReferenceHashMap(64);
/*     */   
/*     */ 
/*     */ 
/*     */   private final BeanInfo beanInfo;
/*     */   
/*     */ 
/*     */ 
/*     */   private final Map<String, PropertyDescriptor> propertyDescriptorCache;
/*     */   
/*     */ 
/*     */   private final ConcurrentMap<PropertyDescriptor, TypeDescriptor> typeDescriptorCache;
/*     */   
/*     */ 
/*     */ 
/*     */   public static void acceptClassLoader(ClassLoader classLoader)
/*     */   {
/* 140 */     if (classLoader != null) {
/* 141 */       acceptedClassLoaders.add(classLoader);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void clearClassLoader(ClassLoader classLoader)
/*     */   {
/* 152 */     for (Iterator<ClassLoader> it = acceptedClassLoaders.iterator(); it.hasNext();) {
/* 153 */       ClassLoader registeredLoader = (ClassLoader)it.next();
/* 154 */       if (isUnderneathClassLoader(registeredLoader, classLoader)) {
/* 155 */         it.remove();
/*     */       }
/*     */     }
/* 158 */     for (Iterator<Class<?>> it = strongClassCache.keySet().iterator(); it.hasNext();) {
/* 159 */       Class<?> beanClass = (Class)it.next();
/* 160 */       if (isUnderneathClassLoader(beanClass.getClassLoader(), classLoader)) {
/* 161 */         it.remove();
/*     */       }
/*     */     }
/* 164 */     for (Iterator<Class<?>> it = softClassCache.keySet().iterator(); it.hasNext();) {
/* 165 */       Class<?> beanClass = (Class)it.next();
/* 166 */       if (isUnderneathClassLoader(beanClass.getClassLoader(), classLoader)) {
/* 167 */         it.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static CachedIntrospectionResults forClass(Class<?> beanClass)
/*     */     throws BeansException
/*     */   {
/* 180 */     CachedIntrospectionResults results = (CachedIntrospectionResults)strongClassCache.get(beanClass);
/* 181 */     if (results != null) {
/* 182 */       return results;
/*     */     }
/* 184 */     results = (CachedIntrospectionResults)softClassCache.get(beanClass);
/* 185 */     if (results != null) {
/* 186 */       return results;
/*     */     }
/*     */     
/* 189 */     results = new CachedIntrospectionResults(beanClass);
/*     */     ConcurrentMap<Class<?>, CachedIntrospectionResults> classCacheToUse;
/*     */     ConcurrentMap<Class<?>, CachedIntrospectionResults> classCacheToUse;
/* 192 */     if ((ClassUtils.isCacheSafe(beanClass, CachedIntrospectionResults.class.getClassLoader())) || 
/* 193 */       (isClassLoaderAccepted(beanClass.getClassLoader()))) {
/* 194 */       classCacheToUse = strongClassCache;
/*     */     }
/*     */     else {
/* 197 */       if (logger.isDebugEnabled()) {
/* 198 */         logger.debug("Not strongly caching class [" + beanClass.getName() + "] because it is not cache-safe");
/*     */       }
/* 200 */       classCacheToUse = softClassCache;
/*     */     }
/*     */     
/* 203 */     CachedIntrospectionResults existing = (CachedIntrospectionResults)classCacheToUse.putIfAbsent(beanClass, results);
/* 204 */     return existing != null ? existing : results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isClassLoaderAccepted(ClassLoader classLoader)
/*     */   {
/* 215 */     for (ClassLoader acceptedLoader : acceptedClassLoaders) {
/* 216 */       if (isUnderneathClassLoader(classLoader, acceptedLoader)) {
/* 217 */         return true;
/*     */       }
/*     */     }
/* 220 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isUnderneathClassLoader(ClassLoader candidate, ClassLoader parent)
/*     */   {
/* 230 */     if (candidate == parent) {
/* 231 */       return true;
/*     */     }
/* 233 */     if (candidate == null) {
/* 234 */       return false;
/*     */     }
/* 236 */     ClassLoader classLoaderToCheck = candidate;
/* 237 */     while (classLoaderToCheck != null) {
/* 238 */       classLoaderToCheck = classLoaderToCheck.getParent();
/* 239 */       if (classLoaderToCheck == parent) {
/* 240 */         return true;
/*     */       }
/*     */     }
/* 243 */     return false;
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
/*     */   private CachedIntrospectionResults(Class<?> beanClass)
/*     */     throws BeansException
/*     */   {
/*     */     try
/*     */     {
/* 264 */       if (logger.isTraceEnabled()) {
/* 265 */         logger.trace("Getting BeanInfo for class [" + beanClass.getName() + "]");
/*     */       }
/*     */       
/* 268 */       BeanInfo beanInfo = null;
/* 269 */       for (Iterator localIterator = beanInfoFactories.iterator(); localIterator.hasNext();) { beanInfoFactory = (BeanInfoFactory)localIterator.next();
/* 270 */         beanInfo = beanInfoFactory.getBeanInfo(beanClass);
/* 271 */         if (beanInfo != null)
/*     */           break;
/*     */       }
/*     */       BeanInfoFactory beanInfoFactory;
/* 275 */       if (beanInfo == null)
/*     */       {
/*     */ 
/*     */ 
/* 279 */         beanInfo = shouldIntrospectorIgnoreBeaninfoClasses ? Introspector.getBeanInfo(beanClass, 3) : Introspector.getBeanInfo(beanClass);
/*     */       }
/* 281 */       this.beanInfo = beanInfo;
/*     */       
/* 283 */       if (logger.isTraceEnabled()) {
/* 284 */         logger.trace("Caching PropertyDescriptors for class [" + beanClass.getName() + "]");
/*     */       }
/* 286 */       this.propertyDescriptorCache = new LinkedHashMap();
/*     */       
/*     */ 
/* 289 */       PropertyDescriptor[] pds = this.beanInfo.getPropertyDescriptors();
/* 290 */       PropertyDescriptor pd; for (pd : pds) {
/* 291 */         if ((Class.class != beanClass) || (
/* 292 */           (!"classLoader".equals(pd.getName())) && (!"protectionDomain".equals(pd.getName()))))
/*     */         {
/*     */ 
/*     */ 
/* 296 */           if (logger.isTraceEnabled()) {
/* 297 */             logger.trace("Found bean property '" + pd.getName() + "'" + (pd
/* 298 */               .getPropertyType() != null ? " of type [" + pd.getPropertyType().getName() + "]" : "") + (pd
/* 299 */               .getPropertyEditorClass() != null ? "; editor [" + pd
/* 300 */               .getPropertyEditorClass().getName() + "]" : ""));
/*     */           }
/* 302 */           pd = buildGenericTypeAwarePropertyDescriptor(beanClass, pd);
/* 303 */           this.propertyDescriptorCache.put(pd.getName(), pd);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 308 */       Class<?> clazz = beanClass;
/* 309 */       while (clazz != null) {
/* 310 */         Object ifcs = clazz.getInterfaces();
/* 311 */         Object localObject1 = ifcs;pd = localObject1.length; for (PropertyDescriptor localPropertyDescriptor1 = 0; localPropertyDescriptor1 < pd; localPropertyDescriptor1++) { Class<?> ifc = localObject1[localPropertyDescriptor1];
/* 312 */           BeanInfo ifcInfo = Introspector.getBeanInfo(ifc, 3);
/* 313 */           PropertyDescriptor[] ifcPds = ifcInfo.getPropertyDescriptors();
/* 314 */           for (PropertyDescriptor pd : ifcPds) {
/* 315 */             if (!this.propertyDescriptorCache.containsKey(pd.getName())) {
/* 316 */               pd = buildGenericTypeAwarePropertyDescriptor(beanClass, pd);
/* 317 */               this.propertyDescriptorCache.put(pd.getName(), pd);
/*     */             }
/*     */           }
/*     */         }
/* 321 */         clazz = clazz.getSuperclass();
/*     */       }
/*     */       
/* 324 */       this.typeDescriptorCache = new ConcurrentReferenceHashMap();
/*     */     }
/*     */     catch (IntrospectionException ex) {
/* 327 */       throw new FatalBeanException("Failed to obtain BeanInfo for class [" + beanClass.getName() + "]", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   BeanInfo getBeanInfo() {
/* 332 */     return this.beanInfo;
/*     */   }
/*     */   
/*     */   Class<?> getBeanClass() {
/* 336 */     return this.beanInfo.getBeanDescriptor().getBeanClass();
/*     */   }
/*     */   
/*     */   PropertyDescriptor getPropertyDescriptor(String name) {
/* 340 */     PropertyDescriptor pd = (PropertyDescriptor)this.propertyDescriptorCache.get(name);
/* 341 */     if ((pd == null) && (StringUtils.hasLength(name)))
/*     */     {
/* 343 */       pd = (PropertyDescriptor)this.propertyDescriptorCache.get(StringUtils.uncapitalize(name));
/* 344 */       if (pd == null) {
/* 345 */         pd = (PropertyDescriptor)this.propertyDescriptorCache.get(StringUtils.capitalize(name));
/*     */       }
/*     */     }
/* 348 */     return (pd == null) || ((pd instanceof GenericTypeAwarePropertyDescriptor)) ? pd : 
/* 349 */       buildGenericTypeAwarePropertyDescriptor(getBeanClass(), pd);
/*     */   }
/*     */   
/*     */   PropertyDescriptor[] getPropertyDescriptors() {
/* 353 */     PropertyDescriptor[] pds = new PropertyDescriptor[this.propertyDescriptorCache.size()];
/* 354 */     int i = 0;
/* 355 */     for (PropertyDescriptor pd : this.propertyDescriptorCache.values())
/*     */     {
/* 357 */       pds[i] = ((pd instanceof GenericTypeAwarePropertyDescriptor) ? pd : buildGenericTypeAwarePropertyDescriptor(getBeanClass(), pd));
/* 358 */       i++;
/*     */     }
/* 360 */     return pds;
/*     */   }
/*     */   
/*     */   private PropertyDescriptor buildGenericTypeAwarePropertyDescriptor(Class<?> beanClass, PropertyDescriptor pd) {
/*     */     try {
/* 365 */       return new GenericTypeAwarePropertyDescriptor(beanClass, pd.getName(), pd.getReadMethod(), pd
/* 366 */         .getWriteMethod(), pd.getPropertyEditorClass());
/*     */     }
/*     */     catch (IntrospectionException ex) {
/* 369 */       throw new FatalBeanException("Failed to re-introspect class [" + beanClass.getName() + "]", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   TypeDescriptor addTypeDescriptor(PropertyDescriptor pd, TypeDescriptor td) {
/* 374 */     TypeDescriptor existing = (TypeDescriptor)this.typeDescriptorCache.putIfAbsent(pd, td);
/* 375 */     return existing != null ? existing : td;
/*     */   }
/*     */   
/*     */   TypeDescriptor getTypeDescriptor(PropertyDescriptor pd) {
/* 379 */     return (TypeDescriptor)this.typeDescriptorCache.get(pd);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\CachedIntrospectionResults.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */