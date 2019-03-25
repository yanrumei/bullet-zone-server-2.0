/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.MethodIntrospector;
/*     */ import org.springframework.core.MethodIntrospector.MetadataLookup;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.web.cors.CorsConfiguration;
/*     */ import org.springframework.web.cors.CorsUtils;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.servlet.HandlerMapping;
/*     */ import org.springframework.web.util.UrlPathHelper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractHandlerMethodMapping<T>
/*     */   extends AbstractHandlerMapping
/*     */   implements InitializingBean
/*     */ {
/*     */   private static final String SCOPED_TARGET_NAME_PREFIX = "scopedTarget.";
/*  75 */   private static final HandlerMethod PREFLIGHT_AMBIGUOUS_MATCH = new HandlerMethod(new EmptyHandler(null), 
/*  76 */     ClassUtils.getMethod(EmptyHandler.class, "handle", new Class[0]));
/*     */   
/*  78 */   private static final CorsConfiguration ALLOW_CORS_CONFIG = new CorsConfiguration();
/*     */   
/*     */   static {
/*  81 */     ALLOW_CORS_CONFIG.addAllowedOrigin("*");
/*  82 */     ALLOW_CORS_CONFIG.addAllowedMethod("*");
/*  83 */     ALLOW_CORS_CONFIG.addAllowedHeader("*");
/*  84 */     ALLOW_CORS_CONFIG.setAllowCredentials(Boolean.valueOf(true));
/*     */   }
/*     */   
/*     */ 
/*  88 */   private boolean detectHandlerMethodsInAncestorContexts = false;
/*     */   
/*     */   private HandlerMethodMappingNamingStrategy<T> namingStrategy;
/*     */   
/*  92 */   private final AbstractHandlerMethodMapping<T>.MappingRegistry mappingRegistry = new MappingRegistry();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDetectHandlerMethodsInAncestorContexts(boolean detectHandlerMethodsInAncestorContexts)
/*     */   {
/* 104 */     this.detectHandlerMethodsInAncestorContexts = detectHandlerMethodsInAncestorContexts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setHandlerMethodMappingNamingStrategy(HandlerMethodMappingNamingStrategy<T> namingStrategy)
/*     */   {
/* 115 */     this.namingStrategy = namingStrategy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HandlerMethodMappingNamingStrategy<T> getNamingStrategy()
/*     */   {
/* 122 */     return this.namingStrategy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Map<T, HandlerMethod> getHandlerMethods()
/*     */   {
/* 129 */     this.mappingRegistry.acquireReadLock();
/*     */     try {
/* 131 */       return Collections.unmodifiableMap(this.mappingRegistry.getMappings());
/*     */     }
/*     */     finally {
/* 134 */       this.mappingRegistry.releaseReadLock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<HandlerMethod> getHandlerMethodsForMappingName(String mappingName)
/*     */   {
/* 146 */     return this.mappingRegistry.getHandlerMethodsByMappingName(mappingName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   AbstractHandlerMethodMapping<T>.MappingRegistry getMappingRegistry()
/*     */   {
/* 153 */     return this.mappingRegistry;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerMapping(T mapping, Object handler, Method method)
/*     */   {
/* 164 */     this.mappingRegistry.register(mapping, handler, method);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void unregisterMapping(T mapping)
/*     */   {
/* 173 */     this.mappingRegistry.unregister(mapping);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */   {
/* 184 */     initHandlerMethods();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initHandlerMethods()
/*     */   {
/* 194 */     if (this.logger.isDebugEnabled()) {
/* 195 */       this.logger.debug("Looking for request mappings in application context: " + getApplicationContext());
/*     */     }
/*     */     
/*     */ 
/* 199 */     String[] beanNames = this.detectHandlerMethodsInAncestorContexts ? BeanFactoryUtils.beanNamesForTypeIncludingAncestors(getApplicationContext(), Object.class) : getApplicationContext().getBeanNamesForType(Object.class);
/*     */     
/* 201 */     for (String beanName : beanNames) {
/* 202 */       if (!beanName.startsWith("scopedTarget.")) {
/* 203 */         Class<?> beanType = null;
/*     */         try {
/* 205 */           beanType = getApplicationContext().getType(beanName);
/*     */         }
/*     */         catch (Throwable ex)
/*     */         {
/* 209 */           if (this.logger.isDebugEnabled()) {
/* 210 */             this.logger.debug("Could not resolve target class for bean with name '" + beanName + "'", ex);
/*     */           }
/*     */         }
/* 213 */         if ((beanType != null) && (isHandler(beanType))) {
/* 214 */           detectHandlerMethods(beanName);
/*     */         }
/*     */       }
/*     */     }
/* 218 */     handlerMethodsInitialized(getHandlerMethods());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void detectHandlerMethods(Object handler)
/*     */   {
/* 227 */     Class<?> handlerType = (handler instanceof String) ? getApplicationContext().getType((String)handler) : handler.getClass();
/* 228 */     final Class<?> userType = ClassUtils.getUserClass(handlerType);
/*     */     
/* 230 */     Map<Method, T> methods = MethodIntrospector.selectMethods(userType, new MethodIntrospector.MetadataLookup()
/*     */     {
/*     */       public T inspect(Method method)
/*     */       {
/*     */         try {
/* 235 */           return (T)AbstractHandlerMethodMapping.this.getMappingForMethod(method, userType);
/*     */         }
/*     */         catch (Throwable ex)
/*     */         {
/* 239 */           throw new IllegalStateException("Invalid mapping on handler class [" + userType.getName() + "]: " + method, ex);
/*     */         }
/*     */       }
/*     */     });
/*     */     
/* 244 */     if (this.logger.isDebugEnabled()) {
/* 245 */       this.logger.debug(methods.size() + " request handler methods found on " + userType + ": " + methods);
/*     */     }
/* 247 */     for (Map.Entry<Method, T> entry : methods.entrySet()) {
/* 248 */       Method invocableMethod = AopUtils.selectInvocableMethod((Method)entry.getKey(), userType);
/* 249 */       T mapping = entry.getValue();
/* 250 */       registerHandlerMethod(handler, invocableMethod, mapping);
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
/*     */   protected void registerHandlerMethod(Object handler, Method method, T mapping)
/*     */   {
/* 264 */     this.mappingRegistry.register(mapping, handler, method);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected HandlerMethod createHandlerMethod(Object handler, Method method)
/*     */   {
/*     */     HandlerMethod handlerMethod;
/*     */     
/*     */     HandlerMethod handlerMethod;
/*     */     
/* 275 */     if ((handler instanceof String)) {
/* 276 */       String beanName = (String)handler;
/*     */       
/* 278 */       handlerMethod = new HandlerMethod(beanName, getApplicationContext().getAutowireCapableBeanFactory(), method);
/*     */     }
/*     */     else {
/* 281 */       handlerMethod = new HandlerMethod(handler, method);
/*     */     }
/* 283 */     return handlerMethod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected CorsConfiguration initCorsConfiguration(Object handler, Method method, T mapping)
/*     */   {
/* 290 */     return null;
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
/*     */   protected HandlerMethod getHandlerInternal(HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/* 308 */     String lookupPath = getUrlPathHelper().getLookupPathForRequest(request);
/* 309 */     if (this.logger.isDebugEnabled()) {
/* 310 */       this.logger.debug("Looking up handler method for path " + lookupPath);
/*     */     }
/* 312 */     this.mappingRegistry.acquireReadLock();
/*     */     try {
/* 314 */       HandlerMethod handlerMethod = lookupHandlerMethod(lookupPath, request);
/* 315 */       if (this.logger.isDebugEnabled()) {
/* 316 */         if (handlerMethod != null) {
/* 317 */           this.logger.debug("Returning handler method [" + handlerMethod + "]");
/*     */         }
/*     */         else {
/* 320 */           this.logger.debug("Did not find handler method for [" + lookupPath + "]");
/*     */         }
/*     */       }
/* 323 */       return handlerMethod != null ? handlerMethod.createWithResolvedBean() : null;
/*     */     }
/*     */     finally {
/* 326 */       this.mappingRegistry.releaseReadLock();
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
/*     */   protected HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/* 340 */     List<AbstractHandlerMethodMapping<T>.Match> matches = new ArrayList();
/* 341 */     List<T> directPathMatches = this.mappingRegistry.getMappingsByUrl(lookupPath);
/* 342 */     if (directPathMatches != null) {
/* 343 */       addMatchingMappings(directPathMatches, matches, request);
/*     */     }
/* 345 */     if (matches.isEmpty())
/*     */     {
/* 347 */       addMatchingMappings(this.mappingRegistry.getMappings().keySet(), matches, request);
/*     */     }
/*     */     
/* 350 */     if (!matches.isEmpty()) {
/* 351 */       Comparator<AbstractHandlerMethodMapping<T>.Match> comparator = new MatchComparator(getMappingComparator(request));
/* 352 */       Collections.sort(matches, comparator);
/* 353 */       if (this.logger.isTraceEnabled()) {
/* 354 */         this.logger.trace("Found " + matches.size() + " matching mapping(s) for [" + lookupPath + "] : " + matches);
/*     */       }
/*     */       
/* 357 */       AbstractHandlerMethodMapping<T>.Match bestMatch = (Match)matches.get(0);
/* 358 */       if (matches.size() > 1) {
/* 359 */         if (CorsUtils.isPreFlightRequest(request)) {
/* 360 */           return PREFLIGHT_AMBIGUOUS_MATCH;
/*     */         }
/* 362 */         AbstractHandlerMethodMapping<T>.Match secondBestMatch = (Match)matches.get(1);
/* 363 */         if (comparator.compare(bestMatch, secondBestMatch) == 0) {
/* 364 */           Method m1 = bestMatch.handlerMethod.getMethod();
/* 365 */           Method m2 = secondBestMatch.handlerMethod.getMethod();
/*     */           
/* 367 */           throw new IllegalStateException("Ambiguous handler methods mapped for HTTP path '" + request.getRequestURL() + "': {" + m1 + ", " + m2 + "}");
/*     */         }
/*     */       }
/* 370 */       handleMatch(bestMatch.mapping, lookupPath, request);
/* 371 */       return bestMatch.handlerMethod;
/*     */     }
/*     */     
/* 374 */     return handleNoMatch(this.mappingRegistry.getMappings().keySet(), lookupPath, request);
/*     */   }
/*     */   
/*     */   private void addMatchingMappings(Collection<T> mappings, List<AbstractHandlerMethodMapping<T>.Match> matches, HttpServletRequest request)
/*     */   {
/* 379 */     for (T mapping : mappings) {
/* 380 */       T match = getMatchingMapping(mapping, request);
/* 381 */       if (match != null) {
/* 382 */         matches.add(new Match(match, (HandlerMethod)this.mappingRegistry.getMappings().get(mapping)));
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handleMatch(T mapping, String lookupPath, HttpServletRequest request)
/*     */   {
/* 394 */     request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, lookupPath);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HandlerMethod handleNoMatch(Set<T> mappings, String lookupPath, HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/* 407 */     return null;
/*     */   }
/*     */   
/*     */   protected CorsConfiguration getCorsConfiguration(Object handler, HttpServletRequest request)
/*     */   {
/* 412 */     CorsConfiguration corsConfig = super.getCorsConfiguration(handler, request);
/* 413 */     if ((handler instanceof HandlerMethod)) {
/* 414 */       HandlerMethod handlerMethod = (HandlerMethod)handler;
/* 415 */       if (handlerMethod.equals(PREFLIGHT_AMBIGUOUS_MATCH)) {
/* 416 */         return ALLOW_CORS_CONFIG;
/*     */       }
/*     */       
/* 419 */       CorsConfiguration corsConfigFromMethod = this.mappingRegistry.getCorsConfiguration(handlerMethod);
/* 420 */       corsConfig = corsConfig != null ? corsConfig.combine(corsConfigFromMethod) : corsConfigFromMethod;
/*     */     }
/*     */     
/* 423 */     return corsConfig;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void handlerMethodsInitialized(Map<T, HandlerMethod> handlerMethods) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean isHandler(Class<?> paramClass);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract T getMappingForMethod(Method paramMethod, Class<?> paramClass);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract Set<String> getMappingPathPatterns(T paramT);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract T getMatchingMapping(T paramT, HttpServletRequest paramHttpServletRequest);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract Comparator<T> getMappingComparator(HttpServletRequest paramHttpServletRequest);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   class MappingRegistry
/*     */   {
/* 477 */     private final Map<T, AbstractHandlerMethodMapping.MappingRegistration<T>> registry = new HashMap();
/*     */     
/* 479 */     private final Map<T, HandlerMethod> mappingLookup = new LinkedHashMap();
/*     */     
/* 481 */     private final MultiValueMap<String, T> urlLookup = new LinkedMultiValueMap();
/*     */     
/* 483 */     private final Map<String, List<HandlerMethod>> nameLookup = new ConcurrentHashMap();
/*     */     
/*     */ 
/* 486 */     private final Map<HandlerMethod, CorsConfiguration> corsLookup = new ConcurrentHashMap();
/*     */     
/*     */ 
/* 489 */     private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
/*     */     
/*     */ 
/*     */     MappingRegistry() {}
/*     */     
/*     */     public Map<T, HandlerMethod> getMappings()
/*     */     {
/* 496 */       return this.mappingLookup;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public List<T> getMappingsByUrl(String urlPath)
/*     */     {
/* 504 */       return (List)this.urlLookup.get(urlPath);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public List<HandlerMethod> getHandlerMethodsByMappingName(String mappingName)
/*     */     {
/* 511 */       return (List)this.nameLookup.get(mappingName);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public CorsConfiguration getCorsConfiguration(HandlerMethod handlerMethod)
/*     */     {
/* 518 */       HandlerMethod original = handlerMethod.getResolvedFromHandlerMethod();
/* 519 */       return (CorsConfiguration)this.corsLookup.get(original != null ? original : handlerMethod);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void acquireReadLock()
/*     */     {
/* 526 */       this.readWriteLock.readLock().lock();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void releaseReadLock()
/*     */     {
/* 533 */       this.readWriteLock.readLock().unlock();
/*     */     }
/*     */     
/*     */     public void register(T mapping, Object handler, Method method) {
/* 537 */       this.readWriteLock.writeLock().lock();
/*     */       try {
/* 539 */         HandlerMethod handlerMethod = AbstractHandlerMethodMapping.this.createHandlerMethod(handler, method);
/* 540 */         assertUniqueMethodMapping(handlerMethod, mapping);
/*     */         
/* 542 */         if (AbstractHandlerMethodMapping.this.logger.isInfoEnabled()) {
/* 543 */           AbstractHandlerMethodMapping.this.logger.info("Mapped \"" + mapping + "\" onto " + handlerMethod);
/*     */         }
/* 545 */         this.mappingLookup.put(mapping, handlerMethod);
/*     */         
/* 547 */         List<String> directUrls = getDirectUrls(mapping);
/* 548 */         for (String url : directUrls) {
/* 549 */           this.urlLookup.add(url, mapping);
/*     */         }
/*     */         
/* 552 */         String name = null;
/* 553 */         if (AbstractHandlerMethodMapping.this.getNamingStrategy() != null) {
/* 554 */           name = AbstractHandlerMethodMapping.this.getNamingStrategy().getName(handlerMethod, mapping);
/* 555 */           addMappingName(name, handlerMethod);
/*     */         }
/*     */         
/* 558 */         CorsConfiguration corsConfig = AbstractHandlerMethodMapping.this.initCorsConfiguration(handler, method, mapping);
/* 559 */         if (corsConfig != null) {
/* 560 */           this.corsLookup.put(handlerMethod, corsConfig);
/*     */         }
/*     */         
/* 563 */         this.registry.put(mapping, new AbstractHandlerMethodMapping.MappingRegistration(mapping, handlerMethod, directUrls, name));
/*     */       }
/*     */       finally {
/* 566 */         this.readWriteLock.writeLock().unlock();
/*     */       }
/*     */     }
/*     */     
/*     */     private void assertUniqueMethodMapping(HandlerMethod newHandlerMethod, T mapping) {
/* 571 */       HandlerMethod handlerMethod = (HandlerMethod)this.mappingLookup.get(mapping);
/* 572 */       if ((handlerMethod != null) && (!handlerMethod.equals(newHandlerMethod)))
/*     */       {
/*     */ 
/*     */ 
/* 576 */         throw new IllegalStateException("Ambiguous mapping. Cannot map '" + newHandlerMethod.getBean() + "' method \n" + newHandlerMethod + "\nto " + mapping + ": There is already '" + handlerMethod.getBean() + "' bean method\n" + handlerMethod + " mapped.");
/*     */       }
/*     */     }
/*     */     
/*     */     private List<String> getDirectUrls(T mapping) {
/* 581 */       List<String> urls = new ArrayList(1);
/* 582 */       for (String path : AbstractHandlerMethodMapping.this.getMappingPathPatterns(mapping)) {
/* 583 */         if (!AbstractHandlerMethodMapping.this.getPathMatcher().isPattern(path)) {
/* 584 */           urls.add(path);
/*     */         }
/*     */       }
/* 587 */       return urls;
/*     */     }
/*     */     
/*     */     private void addMappingName(String name, HandlerMethod handlerMethod) {
/* 591 */       List<HandlerMethod> oldList = (List)this.nameLookup.get(name);
/* 592 */       if (oldList == null) {
/* 593 */         oldList = Collections.emptyList();
/*     */       }
/*     */       
/* 596 */       for (HandlerMethod current : oldList) {
/* 597 */         if (handlerMethod.equals(current)) {
/* 598 */           return;
/*     */         }
/*     */       }
/*     */       
/* 602 */       if (AbstractHandlerMethodMapping.this.logger.isTraceEnabled()) {
/* 603 */         AbstractHandlerMethodMapping.this.logger.trace("Mapping name '" + name + "'");
/*     */       }
/*     */       
/* 606 */       Object newList = new ArrayList(oldList.size() + 1);
/* 607 */       ((List)newList).addAll(oldList);
/* 608 */       ((List)newList).add(handlerMethod);
/* 609 */       this.nameLookup.put(name, newList);
/*     */       
/* 611 */       if ((((List)newList).size() > 1) && 
/* 612 */         (AbstractHandlerMethodMapping.this.logger.isTraceEnabled())) {
/* 613 */         AbstractHandlerMethodMapping.this.logger.trace("Mapping name clash for handlerMethods " + newList + ". Consider assigning explicit names.");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void unregister(T mapping)
/*     */     {
/* 620 */       this.readWriteLock.writeLock().lock();
/*     */       try {
/* 622 */         AbstractHandlerMethodMapping.MappingRegistration<T> definition = (AbstractHandlerMethodMapping.MappingRegistration)this.registry.remove(mapping);
/* 623 */         if (definition == null) {
/* 624 */           return;
/*     */         }
/*     */         
/* 627 */         this.mappingLookup.remove(definition.getMapping());
/*     */         
/* 629 */         for (String url : definition.getDirectUrls()) {
/* 630 */           List<T> list = (List)this.urlLookup.get(url);
/* 631 */           if (list != null) {
/* 632 */             list.remove(definition.getMapping());
/* 633 */             if (list.isEmpty()) {
/* 634 */               this.urlLookup.remove(url);
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 639 */         removeMappingName(definition);
/*     */         
/* 641 */         this.corsLookup.remove(definition.getHandlerMethod());
/*     */       }
/*     */       finally {
/* 644 */         this.readWriteLock.writeLock().unlock();
/*     */       }
/*     */     }
/*     */     
/*     */     private void removeMappingName(AbstractHandlerMethodMapping.MappingRegistration<T> definition) {
/* 649 */       String name = definition.getMappingName();
/* 650 */       if (name == null) {
/* 651 */         return;
/*     */       }
/* 653 */       HandlerMethod handlerMethod = definition.getHandlerMethod();
/* 654 */       List<HandlerMethod> oldList = (List)this.nameLookup.get(name);
/* 655 */       if (oldList == null) {
/* 656 */         return;
/*     */       }
/* 658 */       if (oldList.size() <= 1) {
/* 659 */         this.nameLookup.remove(name);
/* 660 */         return;
/*     */       }
/* 662 */       List<HandlerMethod> newList = new ArrayList(oldList.size() - 1);
/* 663 */       for (HandlerMethod current : oldList) {
/* 664 */         if (!current.equals(handlerMethod)) {
/* 665 */           newList.add(current);
/*     */         }
/*     */       }
/* 668 */       this.nameLookup.put(name, newList);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class MappingRegistration<T>
/*     */   {
/*     */     private final T mapping;
/*     */     
/*     */     private final HandlerMethod handlerMethod;
/*     */     
/*     */     private final List<String> directUrls;
/*     */     private final String mappingName;
/*     */     
/*     */     public MappingRegistration(T mapping, HandlerMethod handlerMethod, List<String> directUrls, String mappingName)
/*     */     {
/* 684 */       Assert.notNull(mapping, "Mapping must not be null");
/* 685 */       Assert.notNull(handlerMethod, "HandlerMethod must not be null");
/* 686 */       this.mapping = mapping;
/* 687 */       this.handlerMethod = handlerMethod;
/* 688 */       this.directUrls = (directUrls != null ? directUrls : Collections.emptyList());
/* 689 */       this.mappingName = mappingName;
/*     */     }
/*     */     
/*     */     public T getMapping() {
/* 693 */       return (T)this.mapping;
/*     */     }
/*     */     
/*     */     public HandlerMethod getHandlerMethod() {
/* 697 */       return this.handlerMethod;
/*     */     }
/*     */     
/*     */     public List<String> getDirectUrls() {
/* 701 */       return this.directUrls;
/*     */     }
/*     */     
/*     */     public String getMappingName() {
/* 705 */       return this.mappingName;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class Match
/*     */   {
/*     */     private final T mapping;
/*     */     
/*     */ 
/*     */     private final HandlerMethod handlerMethod;
/*     */     
/*     */ 
/*     */     public Match(HandlerMethod mapping)
/*     */     {
/* 721 */       this.mapping = mapping;
/* 722 */       this.handlerMethod = handlerMethod;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 727 */       return this.mapping.toString();
/*     */     }
/*     */   }
/*     */   
/*     */   private class MatchComparator implements Comparator<AbstractHandlerMethodMapping<T>.Match>
/*     */   {
/*     */     private final Comparator<T> comparator;
/*     */     
/*     */     public MatchComparator()
/*     */     {
/* 737 */       this.comparator = comparator;
/*     */     }
/*     */     
/*     */     public int compare(AbstractHandlerMethodMapping<T>.Match match1, AbstractHandlerMethodMapping<T>.Match match2)
/*     */     {
/* 742 */       return this.comparator.compare(AbstractHandlerMethodMapping.Match.access$200(match1), AbstractHandlerMethodMapping.Match.access$200(match2));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class EmptyHandler
/*     */   {
/*     */     public void handle()
/*     */     {
/* 750 */       throw new UnsupportedOperationException("Not implemented");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\AbstractHandlerMethodMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */