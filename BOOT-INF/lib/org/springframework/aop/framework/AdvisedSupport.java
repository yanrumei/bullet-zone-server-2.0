/*     */ package org.springframework.aop.framework;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.DynamicIntroductionAdvice;
/*     */ import org.springframework.aop.IntroductionAdvisor;
/*     */ import org.springframework.aop.IntroductionInfo;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.support.DefaultIntroductionAdvisor;
/*     */ import org.springframework.aop.support.DefaultPointcutAdvisor;
/*     */ import org.springframework.aop.target.EmptyTargetSource;
/*     */ import org.springframework.aop.target.SingletonTargetSource;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AdvisedSupport
/*     */   extends ProxyConfig
/*     */   implements Advised
/*     */ {
/*     */   private static final long serialVersionUID = 2651364800145442165L;
/*  71 */   public static final TargetSource EMPTY_TARGET_SOURCE = EmptyTargetSource.INSTANCE;
/*     */   
/*     */ 
/*     */ 
/*  75 */   TargetSource targetSource = EMPTY_TARGET_SOURCE;
/*     */   
/*     */ 
/*  78 */   private boolean preFiltered = false;
/*     */   
/*     */ 
/*  81 */   AdvisorChainFactory advisorChainFactory = new DefaultAdvisorChainFactory();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private transient Map<MethodCacheKey, List<Object>> methodCache;
/*     */   
/*     */ 
/*     */ 
/*  90 */   private List<Class<?>> interfaces = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  96 */   private List<Advisor> advisors = new LinkedList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */   private Advisor[] advisorArray = new Advisor[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AdvisedSupport()
/*     */   {
/* 109 */     initMethodCache();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AdvisedSupport(Class<?>... interfaces)
/*     */   {
/* 117 */     this();
/* 118 */     setInterfaces(interfaces);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void initMethodCache()
/*     */   {
/* 125 */     this.methodCache = new ConcurrentHashMap(32);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTarget(Object target)
/*     */   {
/* 136 */     setTargetSource(new SingletonTargetSource(target));
/*     */   }
/*     */   
/*     */   public void setTargetSource(TargetSource targetSource)
/*     */   {
/* 141 */     this.targetSource = (targetSource != null ? targetSource : EMPTY_TARGET_SOURCE);
/*     */   }
/*     */   
/*     */   public TargetSource getTargetSource()
/*     */   {
/* 146 */     return this.targetSource;
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
/*     */   public void setTargetClass(Class<?> targetClass)
/*     */   {
/* 163 */     this.targetSource = EmptyTargetSource.forClass(targetClass);
/*     */   }
/*     */   
/*     */   public Class<?> getTargetClass()
/*     */   {
/* 168 */     return this.targetSource.getTargetClass();
/*     */   }
/*     */   
/*     */   public void setPreFiltered(boolean preFiltered)
/*     */   {
/* 173 */     this.preFiltered = preFiltered;
/*     */   }
/*     */   
/*     */   public boolean isPreFiltered()
/*     */   {
/* 178 */     return this.preFiltered;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAdvisorChainFactory(AdvisorChainFactory advisorChainFactory)
/*     */   {
/* 186 */     Assert.notNull(advisorChainFactory, "AdvisorChainFactory must not be null");
/* 187 */     this.advisorChainFactory = advisorChainFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public AdvisorChainFactory getAdvisorChainFactory()
/*     */   {
/* 194 */     return this.advisorChainFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setInterfaces(Class<?>... interfaces)
/*     */   {
/* 202 */     Assert.notNull(interfaces, "Interfaces must not be null");
/* 203 */     this.interfaces.clear();
/* 204 */     for (Class<?> ifc : interfaces) {
/* 205 */       addInterface(ifc);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addInterface(Class<?> intf)
/*     */   {
/* 214 */     Assert.notNull(intf, "Interface must not be null");
/* 215 */     if (!intf.isInterface()) {
/* 216 */       throw new IllegalArgumentException("[" + intf.getName() + "] is not an interface");
/*     */     }
/* 218 */     if (!this.interfaces.contains(intf)) {
/* 219 */       this.interfaces.add(intf);
/* 220 */       adviceChanged();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean removeInterface(Class<?> intf)
/*     */   {
/* 232 */     return this.interfaces.remove(intf);
/*     */   }
/*     */   
/*     */   public Class<?>[] getProxiedInterfaces()
/*     */   {
/* 237 */     return (Class[])this.interfaces.toArray(new Class[this.interfaces.size()]);
/*     */   }
/*     */   
/*     */   public boolean isInterfaceProxied(Class<?> intf)
/*     */   {
/* 242 */     for (Class<?> proxyIntf : this.interfaces) {
/* 243 */       if (intf.isAssignableFrom(proxyIntf)) {
/* 244 */         return true;
/*     */       }
/*     */     }
/* 247 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public final Advisor[] getAdvisors()
/*     */   {
/* 253 */     return this.advisorArray;
/*     */   }
/*     */   
/*     */   public void addAdvisor(Advisor advisor)
/*     */   {
/* 258 */     int pos = this.advisors.size();
/* 259 */     addAdvisor(pos, advisor);
/*     */   }
/*     */   
/*     */   public void addAdvisor(int pos, Advisor advisor) throws AopConfigException
/*     */   {
/* 264 */     if ((advisor instanceof IntroductionAdvisor)) {
/* 265 */       validateIntroductionAdvisor((IntroductionAdvisor)advisor);
/*     */     }
/* 267 */     addAdvisorInternal(pos, advisor);
/*     */   }
/*     */   
/*     */   public boolean removeAdvisor(Advisor advisor)
/*     */   {
/* 272 */     int index = indexOf(advisor);
/* 273 */     if (index == -1) {
/* 274 */       return false;
/*     */     }
/*     */     
/* 277 */     removeAdvisor(index);
/* 278 */     return true;
/*     */   }
/*     */   
/*     */   public void removeAdvisor(int index)
/*     */     throws AopConfigException
/*     */   {
/* 284 */     if (isFrozen()) {
/* 285 */       throw new AopConfigException("Cannot remove Advisor: Configuration is frozen.");
/*     */     }
/* 287 */     if ((index < 0) || (index > this.advisors.size() - 1))
/*     */     {
/* 289 */       throw new AopConfigException("Advisor index " + index + " is out of bounds: This configuration only has " + this.advisors.size() + " advisors.");
/*     */     }
/*     */     
/* 292 */     Advisor advisor = (Advisor)this.advisors.get(index);
/* 293 */     if ((advisor instanceof IntroductionAdvisor)) {
/* 294 */       IntroductionAdvisor ia = (IntroductionAdvisor)advisor;
/*     */       
/* 296 */       for (int j = 0; j < ia.getInterfaces().length; j++) {
/* 297 */         removeInterface(ia.getInterfaces()[j]);
/*     */       }
/*     */     }
/*     */     
/* 301 */     this.advisors.remove(index);
/* 302 */     updateAdvisorArray();
/* 303 */     adviceChanged();
/*     */   }
/*     */   
/*     */   public int indexOf(Advisor advisor)
/*     */   {
/* 308 */     Assert.notNull(advisor, "Advisor must not be null");
/* 309 */     return this.advisors.indexOf(advisor);
/*     */   }
/*     */   
/*     */   public boolean replaceAdvisor(Advisor a, Advisor b) throws AopConfigException
/*     */   {
/* 314 */     Assert.notNull(a, "Advisor a must not be null");
/* 315 */     Assert.notNull(b, "Advisor b must not be null");
/* 316 */     int index = indexOf(a);
/* 317 */     if (index == -1) {
/* 318 */       return false;
/*     */     }
/* 320 */     removeAdvisor(index);
/* 321 */     addAdvisor(index, b);
/* 322 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addAdvisors(Advisor... advisors)
/*     */   {
/* 330 */     addAdvisors(Arrays.asList(advisors));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addAdvisors(Collection<Advisor> advisors)
/*     */   {
/* 338 */     if (isFrozen()) {
/* 339 */       throw new AopConfigException("Cannot add advisor: Configuration is frozen.");
/*     */     }
/* 341 */     if (!CollectionUtils.isEmpty(advisors)) {
/* 342 */       for (Advisor advisor : advisors) {
/* 343 */         if ((advisor instanceof IntroductionAdvisor)) {
/* 344 */           validateIntroductionAdvisor((IntroductionAdvisor)advisor);
/*     */         }
/* 346 */         Assert.notNull(advisor, "Advisor must not be null");
/* 347 */         this.advisors.add(advisor);
/*     */       }
/* 349 */       updateAdvisorArray();
/* 350 */       adviceChanged();
/*     */     }
/*     */   }
/*     */   
/*     */   private void validateIntroductionAdvisor(IntroductionAdvisor advisor) {
/* 355 */     advisor.validateInterfaces();
/*     */     
/* 357 */     Class<?>[] ifcs = advisor.getInterfaces();
/* 358 */     for (Class<?> ifc : ifcs) {
/* 359 */       addInterface(ifc);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addAdvisorInternal(int pos, Advisor advisor) throws AopConfigException {
/* 364 */     Assert.notNull(advisor, "Advisor must not be null");
/* 365 */     if (isFrozen()) {
/* 366 */       throw new AopConfigException("Cannot add advisor: Configuration is frozen.");
/*     */     }
/* 368 */     if (pos > this.advisors.size())
/*     */     {
/* 370 */       throw new IllegalArgumentException("Illegal position " + pos + " in advisor list with size " + this.advisors.size());
/*     */     }
/* 372 */     this.advisors.add(pos, advisor);
/* 373 */     updateAdvisorArray();
/* 374 */     adviceChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void updateAdvisorArray()
/*     */   {
/* 381 */     this.advisorArray = ((Advisor[])this.advisors.toArray(new Advisor[this.advisors.size()]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final List<Advisor> getAdvisorsInternal()
/*     */   {
/* 390 */     return this.advisors;
/*     */   }
/*     */   
/*     */   public void addAdvice(Advice advice)
/*     */     throws AopConfigException
/*     */   {
/* 396 */     int pos = this.advisors.size();
/* 397 */     addAdvice(pos, advice);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addAdvice(int pos, Advice advice)
/*     */     throws AopConfigException
/*     */   {
/* 405 */     Assert.notNull(advice, "Advice must not be null");
/* 406 */     if ((advice instanceof IntroductionInfo))
/*     */     {
/*     */ 
/* 409 */       addAdvisor(pos, new DefaultIntroductionAdvisor(advice, (IntroductionInfo)advice));
/*     */     } else {
/* 411 */       if ((advice instanceof DynamicIntroductionAdvice))
/*     */       {
/* 413 */         throw new AopConfigException("DynamicIntroductionAdvice may only be added as part of IntroductionAdvisor");
/*     */       }
/*     */       
/* 416 */       addAdvisor(pos, new DefaultPointcutAdvisor(advice));
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean removeAdvice(Advice advice) throws AopConfigException
/*     */   {
/* 422 */     int index = indexOf(advice);
/* 423 */     if (index == -1) {
/* 424 */       return false;
/*     */     }
/*     */     
/* 427 */     removeAdvisor(index);
/* 428 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   public int indexOf(Advice advice)
/*     */   {
/* 434 */     Assert.notNull(advice, "Advice must not be null");
/* 435 */     for (int i = 0; i < this.advisors.size(); i++) {
/* 436 */       Advisor advisor = (Advisor)this.advisors.get(i);
/* 437 */       if (advisor.getAdvice() == advice) {
/* 438 */         return i;
/*     */       }
/*     */     }
/* 441 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean adviceIncluded(Advice advice)
/*     */   {
/* 450 */     if (advice != null) {
/* 451 */       for (Advisor advisor : this.advisors) {
/* 452 */         if (advisor.getAdvice() == advice) {
/* 453 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 457 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int countAdvicesOfType(Class<?> adviceClass)
/*     */   {
/* 466 */     int count = 0;
/* 467 */     if (adviceClass != null) {
/* 468 */       for (Advisor advisor : this.advisors) {
/* 469 */         if (adviceClass.isInstance(advisor.getAdvice())) {
/* 470 */           count++;
/*     */         }
/*     */       }
/*     */     }
/* 474 */     return count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass)
/*     */   {
/* 486 */     MethodCacheKey cacheKey = new MethodCacheKey(method);
/* 487 */     List<Object> cached = (List)this.methodCache.get(cacheKey);
/* 488 */     if (cached == null) {
/* 489 */       cached = this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(this, method, targetClass);
/*     */       
/* 491 */       this.methodCache.put(cacheKey, cached);
/*     */     }
/* 493 */     return cached;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void adviceChanged()
/*     */   {
/* 500 */     this.methodCache.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void copyConfigurationFrom(AdvisedSupport other)
/*     */   {
/* 509 */     copyConfigurationFrom(other, other.targetSource, new ArrayList(other.advisors));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void copyConfigurationFrom(AdvisedSupport other, TargetSource targetSource, List<Advisor> advisors)
/*     */   {
/* 520 */     copyFrom(other);
/* 521 */     this.targetSource = targetSource;
/* 522 */     this.advisorChainFactory = other.advisorChainFactory;
/* 523 */     this.interfaces = new ArrayList(other.interfaces);
/* 524 */     for (Advisor advisor : advisors) {
/* 525 */       if ((advisor instanceof IntroductionAdvisor)) {
/* 526 */         validateIntroductionAdvisor((IntroductionAdvisor)advisor);
/*     */       }
/* 528 */       Assert.notNull(advisor, "Advisor must not be null");
/* 529 */       this.advisors.add(advisor);
/*     */     }
/* 531 */     updateAdvisorArray();
/* 532 */     adviceChanged();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   AdvisedSupport getConfigurationOnlyCopy()
/*     */   {
/* 540 */     AdvisedSupport copy = new AdvisedSupport();
/* 541 */     copy.copyFrom(this);
/* 542 */     copy.targetSource = EmptyTargetSource.forClass(getTargetClass(), getTargetSource().isStatic());
/* 543 */     copy.advisorChainFactory = this.advisorChainFactory;
/* 544 */     copy.interfaces = this.interfaces;
/* 545 */     copy.advisors = this.advisors;
/* 546 */     copy.updateAdvisorArray();
/* 547 */     return copy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void readObject(ObjectInputStream ois)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 557 */     ois.defaultReadObject();
/*     */     
/*     */ 
/* 560 */     initMethodCache();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toProxyConfigString()
/*     */   {
/* 566 */     return toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 574 */     StringBuilder sb = new StringBuilder(getClass().getName());
/* 575 */     sb.append(": ").append(this.interfaces.size()).append(" interfaces ");
/* 576 */     sb.append(ClassUtils.classNamesToString(this.interfaces)).append("; ");
/* 577 */     sb.append(this.advisors.size()).append(" advisors ");
/* 578 */     sb.append(this.advisors).append("; ");
/* 579 */     sb.append("targetSource [").append(this.targetSource).append("]; ");
/* 580 */     sb.append(super.toString());
/* 581 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class MethodCacheKey
/*     */     implements Comparable<MethodCacheKey>
/*     */   {
/*     */     private final Method method;
/*     */     
/*     */     private final int hashCode;
/*     */     
/*     */ 
/*     */     public MethodCacheKey(Method method)
/*     */     {
/* 596 */       this.method = method;
/* 597 */       this.hashCode = method.hashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object other)
/*     */     {
/* 602 */       return (this == other) || (((other instanceof MethodCacheKey)) && (this.method == ((MethodCacheKey)other).method));
/*     */     }
/*     */     
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 608 */       return this.hashCode;
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 613 */       return this.method.toString();
/*     */     }
/*     */     
/*     */     public int compareTo(MethodCacheKey other)
/*     */     {
/* 618 */       int result = this.method.getName().compareTo(other.method.getName());
/* 619 */       if (result == 0) {
/* 620 */         result = this.method.toString().compareTo(other.method.toString());
/*     */       }
/* 622 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\AdvisedSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */