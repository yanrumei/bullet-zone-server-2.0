/*     */ package org.springframework.context.event;
/*     */ 
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.aop.framework.AopProxyUtils;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public abstract class AbstractApplicationEventMulticaster
/*     */   implements ApplicationEventMulticaster, BeanClassLoaderAware, BeanFactoryAware
/*     */ {
/*  62 */   private final ListenerRetriever defaultRetriever = new ListenerRetriever(false);
/*     */   
/*  64 */   final Map<ListenerCacheKey, ListenerRetriever> retrieverCache = new ConcurrentHashMap(64);
/*     */   
/*     */ 
/*     */   private ClassLoader beanClassLoader;
/*     */   
/*     */   private BeanFactory beanFactory;
/*     */   
/*  71 */   private Object retrievalMutex = this.defaultRetriever;
/*     */   
/*     */ 
/*     */   public void setBeanClassLoader(ClassLoader classLoader)
/*     */   {
/*  76 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory)
/*     */   {
/*  81 */     this.beanFactory = beanFactory;
/*  82 */     if ((beanFactory instanceof ConfigurableBeanFactory)) {
/*  83 */       ConfigurableBeanFactory cbf = (ConfigurableBeanFactory)beanFactory;
/*  84 */       if (this.beanClassLoader == null) {
/*  85 */         this.beanClassLoader = cbf.getBeanClassLoader();
/*     */       }
/*  87 */       this.retrievalMutex = cbf.getSingletonMutex();
/*     */     }
/*     */   }
/*     */   
/*     */   private BeanFactory getBeanFactory() {
/*  92 */     if (this.beanFactory == null) {
/*  93 */       throw new IllegalStateException("ApplicationEventMulticaster cannot retrieve listener beans because it is not associated with a BeanFactory");
/*     */     }
/*     */     
/*  96 */     return this.beanFactory;
/*     */   }
/*     */   
/*     */ 
/*     */   public void addApplicationListener(ApplicationListener<?> listener)
/*     */   {
/* 102 */     synchronized (this.retrievalMutex)
/*     */     {
/*     */ 
/* 105 */       Object singletonTarget = AopProxyUtils.getSingletonTarget(listener);
/* 106 */       if ((singletonTarget instanceof ApplicationListener)) {
/* 107 */         this.defaultRetriever.applicationListeners.remove(singletonTarget);
/*     */       }
/* 109 */       this.defaultRetriever.applicationListeners.add(listener);
/* 110 */       this.retrieverCache.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public void addApplicationListenerBean(String listenerBeanName)
/*     */   {
/* 116 */     synchronized (this.retrievalMutex) {
/* 117 */       this.defaultRetriever.applicationListenerBeans.add(listenerBeanName);
/* 118 */       this.retrieverCache.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeApplicationListener(ApplicationListener<?> listener)
/*     */   {
/* 124 */     synchronized (this.retrievalMutex) {
/* 125 */       this.defaultRetriever.applicationListeners.remove(listener);
/* 126 */       this.retrieverCache.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeApplicationListenerBean(String listenerBeanName)
/*     */   {
/* 132 */     synchronized (this.retrievalMutex) {
/* 133 */       this.defaultRetriever.applicationListenerBeans.remove(listenerBeanName);
/* 134 */       this.retrieverCache.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   public void removeAllListeners()
/*     */   {
/* 140 */     synchronized (this.retrievalMutex) {
/* 141 */       this.defaultRetriever.applicationListeners.clear();
/* 142 */       this.defaultRetriever.applicationListenerBeans.clear();
/* 143 */       this.retrieverCache.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected Collection<ApplicationListener<?>> getApplicationListeners()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 9	org/springframework/context/event/AbstractApplicationEventMulticaster:retrievalMutex	Ljava/lang/Object;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 5	org/springframework/context/event/AbstractApplicationEventMulticaster:defaultRetriever	Lorg/springframework/context/event/AbstractApplicationEventMulticaster$ListenerRetriever;
/*     */     //   11: invokevirtual 26	org/springframework/context/event/AbstractApplicationEventMulticaster$ListenerRetriever:getApplicationListeners	()Ljava/util/Collection;
/*     */     //   14: aload_1
/*     */     //   15: monitorexit
/*     */     //   16: areturn
/*     */     //   17: astore_2
/*     */     //   18: aload_1
/*     */     //   19: monitorexit
/*     */     //   20: aload_2
/*     */     //   21: athrow
/*     */     // Line number table:
/*     */     //   Java source line #154	-> byte code offset #0
/*     */     //   Java source line #155	-> byte code offset #7
/*     */     //   Java source line #156	-> byte code offset #17
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	22	0	this	AbstractApplicationEventMulticaster
/*     */     //   5	14	1	Ljava/lang/Object;	Object
/*     */     //   17	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	16	17	finally
/*     */     //   17	20	17	finally
/*     */   }
/*     */   
/*     */   protected Collection<ApplicationListener<?>> getApplicationListeners(ApplicationEvent event, ResolvableType eventType)
/*     */   {
/* 171 */     Object source = event.getSource();
/* 172 */     Class<?> sourceType = source != null ? source.getClass() : null;
/* 173 */     ListenerCacheKey cacheKey = new ListenerCacheKey(eventType, sourceType);
/*     */     
/*     */ 
/* 176 */     ListenerRetriever retriever = (ListenerRetriever)this.retrieverCache.get(cacheKey);
/* 177 */     if (retriever != null) {
/* 178 */       return retriever.getApplicationListeners();
/*     */     }
/*     */     
/* 181 */     if ((this.beanClassLoader == null) || (
/* 182 */       (ClassUtils.isCacheSafe(event.getClass(), this.beanClassLoader)) && ((sourceType == null) || 
/* 183 */       (ClassUtils.isCacheSafe(sourceType, this.beanClassLoader)))))
/*     */     {
/* 185 */       synchronized (this.retrievalMutex) {
/* 186 */         retriever = (ListenerRetriever)this.retrieverCache.get(cacheKey);
/* 187 */         if (retriever != null) {
/* 188 */           return retriever.getApplicationListeners();
/*     */         }
/* 190 */         retriever = new ListenerRetriever(true);
/*     */         
/* 192 */         Collection<ApplicationListener<?>> listeners = retrieveApplicationListeners(eventType, sourceType, retriever);
/* 193 */         this.retrieverCache.put(cacheKey, retriever);
/* 194 */         return listeners;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 199 */     return retrieveApplicationListeners(eventType, sourceType, null);
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
/*     */   private Collection<ApplicationListener<?>> retrieveApplicationListeners(ResolvableType eventType, Class<?> sourceType, ListenerRetriever retriever)
/*     */   {
/* 213 */     LinkedList<ApplicationListener<?>> allListeners = new LinkedList();
/*     */     
/*     */     Set<String> listenerBeans;
/* 216 */     synchronized (this.retrievalMutex) {
/* 217 */       Set<ApplicationListener<?>> listeners = new LinkedHashSet(this.defaultRetriever.applicationListeners);
/* 218 */       listenerBeans = new LinkedHashSet(this.defaultRetriever.applicationListenerBeans); }
/*     */     Set<String> listenerBeans;
/* 220 */     Set<ApplicationListener<?>> listeners; for (??? = listeners.iterator(); ((Iterator)???).hasNext();) { listener = (ApplicationListener)((Iterator)???).next();
/* 221 */       if (supportsEvent((ApplicationListener)listener, eventType, sourceType)) {
/* 222 */         if (retriever != null) {
/* 223 */           retriever.applicationListeners.add(listener);
/*     */         }
/* 225 */         allListeners.add(listener); } }
/*     */     Object listener;
/*     */     BeanFactory beanFactory;
/* 228 */     if (!listenerBeans.isEmpty()) {
/* 229 */       beanFactory = getBeanFactory();
/* 230 */       for (listener = listenerBeans.iterator(); ((Iterator)listener).hasNext();) { String listenerBeanName = (String)((Iterator)listener).next();
/*     */         try {
/* 232 */           Class<?> listenerType = beanFactory.getType(listenerBeanName);
/* 233 */           if ((listenerType == null) || (supportsEvent(listenerType, eventType)))
/*     */           {
/* 235 */             ApplicationListener<?> listener = (ApplicationListener)beanFactory.getBean(listenerBeanName, ApplicationListener.class);
/* 236 */             if ((!allListeners.contains(listener)) && (supportsEvent(listener, eventType, sourceType))) {
/* 237 */               if (retriever != null) {
/* 238 */                 retriever.applicationListenerBeans.add(listenerBeanName);
/*     */               }
/* 240 */               allListeners.add(listener);
/*     */             }
/*     */           }
/*     */         }
/*     */         catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {}
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 250 */     AnnotationAwareOrderComparator.sort(allListeners);
/* 251 */     return allListeners;
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
/*     */   protected boolean supportsEvent(Class<?> listenerType, ResolvableType eventType)
/*     */   {
/* 266 */     if ((GenericApplicationListener.class.isAssignableFrom(listenerType)) || 
/* 267 */       (SmartApplicationListener.class.isAssignableFrom(listenerType))) {
/* 268 */       return true;
/*     */     }
/* 270 */     ResolvableType declaredEventType = GenericApplicationListenerAdapter.resolveDeclaredEventType(listenerType);
/* 271 */     return (declaredEventType == null) || (declaredEventType.isAssignableFrom(eventType));
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
/*     */   protected boolean supportsEvent(ApplicationListener<?> listener, ResolvableType eventType, Class<?> sourceType)
/*     */   {
/* 287 */     GenericApplicationListener smartListener = (listener instanceof GenericApplicationListener) ? (GenericApplicationListener)listener : new GenericApplicationListenerAdapter(listener);
/*     */     
/* 289 */     return (smartListener.supportsEventType(eventType)) && (smartListener.supportsSourceType(sourceType));
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class ListenerCacheKey
/*     */     implements Comparable<ListenerCacheKey>
/*     */   {
/*     */     private final ResolvableType eventType;
/*     */     
/*     */     private final Class<?> sourceType;
/*     */     
/*     */ 
/*     */     public ListenerCacheKey(ResolvableType eventType, Class<?> sourceType)
/*     */     {
/* 303 */       this.eventType = eventType;
/* 304 */       this.sourceType = sourceType;
/*     */     }
/*     */     
/*     */     public boolean equals(Object other)
/*     */     {
/* 309 */       if (this == other) {
/* 310 */         return true;
/*     */       }
/* 312 */       ListenerCacheKey otherKey = (ListenerCacheKey)other;
/* 313 */       return (ObjectUtils.nullSafeEquals(this.eventType, otherKey.eventType)) && 
/* 314 */         (ObjectUtils.nullSafeEquals(this.sourceType, otherKey.sourceType));
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 319 */       return ObjectUtils.nullSafeHashCode(this.eventType) * 29 + ObjectUtils.nullSafeHashCode(this.sourceType);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 324 */       return "ListenerCacheKey [eventType = " + this.eventType + ", sourceType = " + this.sourceType.getName() + "]";
/*     */     }
/*     */     
/*     */     public int compareTo(ListenerCacheKey other)
/*     */     {
/* 329 */       int result = 0;
/* 330 */       if (this.eventType != null) {
/* 331 */         result = this.eventType.toString().compareTo(other.eventType.toString());
/*     */       }
/* 333 */       if ((result == 0) && (this.sourceType != null)) {
/* 334 */         result = this.sourceType.getName().compareTo(other.sourceType.getName());
/*     */       }
/* 336 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class ListenerRetriever
/*     */   {
/*     */     public final Set<ApplicationListener<?>> applicationListeners;
/*     */     
/*     */ 
/*     */     public final Set<String> applicationListenerBeans;
/*     */     
/*     */ 
/*     */     private final boolean preFiltered;
/*     */     
/*     */ 
/*     */     public ListenerRetriever(boolean preFiltered)
/*     */     {
/* 355 */       this.applicationListeners = new LinkedHashSet();
/* 356 */       this.applicationListenerBeans = new LinkedHashSet();
/* 357 */       this.preFiltered = preFiltered;
/*     */     }
/*     */     
/*     */     public Collection<ApplicationListener<?>> getApplicationListeners() {
/* 361 */       LinkedList<ApplicationListener<?>> allListeners = new LinkedList();
/* 362 */       for (Iterator localIterator = this.applicationListeners.iterator(); localIterator.hasNext();) { listener = (ApplicationListener)localIterator.next();
/* 363 */         allListeners.add(listener); }
/*     */       ApplicationListener<?> listener;
/* 365 */       BeanFactory beanFactory; if (!this.applicationListenerBeans.isEmpty()) {
/* 366 */         beanFactory = AbstractApplicationEventMulticaster.this.getBeanFactory();
/* 367 */         for (String listenerBeanName : this.applicationListenerBeans) {
/*     */           try {
/* 369 */             ApplicationListener<?> listener = (ApplicationListener)beanFactory.getBean(listenerBeanName, ApplicationListener.class);
/* 370 */             if ((this.preFiltered) || (!allListeners.contains(listener))) {
/* 371 */               allListeners.add(listener);
/*     */             }
/*     */           }
/*     */           catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {}
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 380 */       AnnotationAwareOrderComparator.sort(allListeners);
/* 381 */       return allListeners;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\event\AbstractApplicationEventMulticaster.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */