/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.context.Lifecycle;
/*     */ import org.springframework.context.LifecycleProcessor;
/*     */ import org.springframework.context.Phased;
/*     */ import org.springframework.context.SmartLifecycle;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultLifecycleProcessor
/*     */   implements LifecycleProcessor, BeanFactoryAware
/*     */ {
/*  52 */   private final Log logger = LogFactory.getLog(getClass());
/*     */   
/*  54 */   private volatile long timeoutPerShutdownPhase = 30000L;
/*     */   
/*     */ 
/*     */ 
/*     */   private volatile boolean running;
/*     */   
/*     */ 
/*     */   private volatile ConfigurableListableBeanFactory beanFactory;
/*     */   
/*     */ 
/*     */ 
/*     */   public void setTimeoutPerShutdownPhase(long timeoutPerShutdownPhase)
/*     */   {
/*  67 */     this.timeoutPerShutdownPhase = timeoutPerShutdownPhase;
/*     */   }
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory)
/*     */   {
/*  72 */     if (!(beanFactory instanceof ConfigurableListableBeanFactory)) {
/*  73 */       throw new IllegalArgumentException("DefaultLifecycleProcessor requires a ConfigurableListableBeanFactory: " + beanFactory);
/*     */     }
/*     */     
/*  76 */     this.beanFactory = ((ConfigurableListableBeanFactory)beanFactory);
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
/*     */   public void start()
/*     */   {
/*  93 */     startBeans(false);
/*  94 */     this.running = true;
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
/*     */   public void stop()
/*     */   {
/* 108 */     stopBeans();
/* 109 */     this.running = false;
/*     */   }
/*     */   
/*     */   public void onRefresh()
/*     */   {
/* 114 */     startBeans(true);
/* 115 */     this.running = true;
/*     */   }
/*     */   
/*     */   public void onClose()
/*     */   {
/* 120 */     stopBeans();
/* 121 */     this.running = false;
/*     */   }
/*     */   
/*     */   public boolean isRunning()
/*     */   {
/* 126 */     return this.running;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void startBeans(boolean autoStartupOnly)
/*     */   {
/* 133 */     Map<String, Lifecycle> lifecycleBeans = getLifecycleBeans();
/* 134 */     Map<Integer, LifecycleGroup> phases = new HashMap();
/* 135 */     for (Iterator localIterator = lifecycleBeans.entrySet().iterator(); localIterator.hasNext();) { entry = (Map.Entry)localIterator.next();
/* 136 */       Lifecycle bean = (Lifecycle)entry.getValue();
/* 137 */       if ((!autoStartupOnly) || (((bean instanceof SmartLifecycle)) && (((SmartLifecycle)bean).isAutoStartup()))) {
/* 138 */         int phase = getPhase(bean);
/* 139 */         LifecycleGroup group = (LifecycleGroup)phases.get(Integer.valueOf(phase));
/* 140 */         if (group == null) {
/* 141 */           group = new LifecycleGroup(phase, this.timeoutPerShutdownPhase, lifecycleBeans, autoStartupOnly);
/* 142 */           phases.put(Integer.valueOf(phase), group);
/*     */         }
/* 144 */         group.add((String)entry.getKey(), bean);
/*     */       } }
/*     */     Map.Entry<String, ? extends Lifecycle> entry;
/* 147 */     if (!phases.isEmpty()) {
/* 148 */       Object keys = new ArrayList(phases.keySet());
/* 149 */       Collections.sort((List)keys);
/* 150 */       for (Integer key : (List)keys) {
/* 151 */         ((LifecycleGroup)phases.get(key)).start();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void doStart(Map<String, ? extends Lifecycle> lifecycleBeans, String beanName, boolean autoStartupOnly)
/*     */   {
/* 163 */     Lifecycle bean = (Lifecycle)lifecycleBeans.remove(beanName);
/* 164 */     if ((bean != null) && (!equals(bean))) {
/* 165 */       String[] dependenciesForBean = this.beanFactory.getDependenciesForBean(beanName);
/* 166 */       for (String dependency : dependenciesForBean) {
/* 167 */         doStart(lifecycleBeans, dependency, autoStartupOnly);
/*     */       }
/* 169 */       if ((!bean.isRunning()) && ((!autoStartupOnly) || (!(bean instanceof SmartLifecycle)) || 
/* 170 */         (((SmartLifecycle)bean).isAutoStartup()))) {
/* 171 */         if (this.logger.isDebugEnabled()) {
/* 172 */           this.logger.debug("Starting bean '" + beanName + "' of type [" + bean.getClass() + "]");
/*     */         }
/*     */         try {
/* 175 */           bean.start();
/*     */         }
/*     */         catch (Throwable ex) {
/* 178 */           throw new ApplicationContextException("Failed to start bean '" + beanName + "'", ex);
/*     */         }
/* 180 */         if (this.logger.isDebugEnabled()) {
/* 181 */           this.logger.debug("Successfully started bean '" + beanName + "'");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void stopBeans() {
/* 188 */     Map<String, Lifecycle> lifecycleBeans = getLifecycleBeans();
/* 189 */     Map<Integer, LifecycleGroup> phases = new HashMap();
/* 190 */     for (Iterator localIterator = lifecycleBeans.entrySet().iterator(); localIterator.hasNext();) { entry = (Map.Entry)localIterator.next();
/* 191 */       Lifecycle bean = (Lifecycle)entry.getValue();
/* 192 */       int shutdownOrder = getPhase(bean);
/* 193 */       LifecycleGroup group = (LifecycleGroup)phases.get(Integer.valueOf(shutdownOrder));
/* 194 */       if (group == null) {
/* 195 */         group = new LifecycleGroup(shutdownOrder, this.timeoutPerShutdownPhase, lifecycleBeans, false);
/* 196 */         phases.put(Integer.valueOf(shutdownOrder), group);
/*     */       }
/* 198 */       group.add((String)entry.getKey(), bean); }
/*     */     Map.Entry<String, Lifecycle> entry;
/* 200 */     if (!phases.isEmpty()) {
/* 201 */       Object keys = new ArrayList(phases.keySet());
/* 202 */       Collections.sort((List)keys, Collections.reverseOrder());
/* 203 */       for (Integer key : (List)keys) {
/* 204 */         ((LifecycleGroup)phases.get(key)).stop();
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
/*     */   private void doStop(Map<String, ? extends Lifecycle> lifecycleBeans, final String beanName, final CountDownLatch latch, final Set<String> countDownBeanNames)
/*     */   {
/* 218 */     Lifecycle bean = (Lifecycle)lifecycleBeans.remove(beanName);
/* 219 */     if (bean != null) {
/* 220 */       String[] dependentBeans = this.beanFactory.getDependentBeans(beanName);
/* 221 */       for (String dependentBean : dependentBeans) {
/* 222 */         doStop(lifecycleBeans, dependentBean, latch, countDownBeanNames);
/*     */       }
/*     */       try {
/* 225 */         if (bean.isRunning()) {
/* 226 */           if ((bean instanceof SmartLifecycle)) {
/* 227 */             if (this.logger.isDebugEnabled()) {
/* 228 */               this.logger.debug("Asking bean '" + beanName + "' of type [" + bean.getClass() + "] to stop");
/*     */             }
/* 230 */             countDownBeanNames.add(beanName);
/* 231 */             ((SmartLifecycle)bean).stop(new Runnable()
/*     */             {
/*     */               public void run() {
/* 234 */                 latch.countDown();
/* 235 */                 countDownBeanNames.remove(beanName);
/* 236 */                 if (DefaultLifecycleProcessor.this.logger.isDebugEnabled()) {
/* 237 */                   DefaultLifecycleProcessor.this.logger.debug("Bean '" + beanName + "' completed its stop procedure");
/*     */                 }
/*     */               }
/*     */             });
/*     */           }
/*     */           else {
/* 243 */             if (this.logger.isDebugEnabled()) {
/* 244 */               this.logger.debug("Stopping bean '" + beanName + "' of type [" + bean.getClass() + "]");
/*     */             }
/* 246 */             bean.stop();
/* 247 */             if (this.logger.isDebugEnabled()) {
/* 248 */               this.logger.debug("Successfully stopped bean '" + beanName + "'");
/*     */             }
/*     */           }
/*     */         }
/* 252 */         else if ((bean instanceof SmartLifecycle))
/*     */         {
/* 254 */           latch.countDown();
/*     */         }
/*     */       }
/*     */       catch (Throwable ex) {
/* 258 */         if (this.logger.isWarnEnabled()) {
/* 259 */           this.logger.warn("Failed to stop bean '" + beanName + "'", ex);
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
/*     */   protected Map<String, Lifecycle> getLifecycleBeans()
/*     */   {
/* 274 */     Map<String, Lifecycle> beans = new LinkedHashMap();
/* 275 */     String[] beanNames = this.beanFactory.getBeanNamesForType(Lifecycle.class, false, false);
/* 276 */     for (String beanName : beanNames) {
/* 277 */       String beanNameToRegister = BeanFactoryUtils.transformedBeanName(beanName);
/* 278 */       boolean isFactoryBean = this.beanFactory.isFactoryBean(beanNameToRegister);
/* 279 */       String beanNameToCheck = isFactoryBean ? "&" + beanName : beanName;
/* 280 */       if (((this.beanFactory.containsSingleton(beanNameToRegister)) && ((!isFactoryBean) || 
/* 281 */         (Lifecycle.class.isAssignableFrom(this.beanFactory.getType(beanNameToCheck))))) || 
/* 282 */         (SmartLifecycle.class.isAssignableFrom(this.beanFactory.getType(beanNameToCheck)))) {
/* 283 */         Lifecycle bean = (Lifecycle)this.beanFactory.getBean(beanNameToCheck, Lifecycle.class);
/* 284 */         if (bean != this) {
/* 285 */           beans.put(beanNameToRegister, bean);
/*     */         }
/*     */       }
/*     */     }
/* 289 */     return beans;
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
/*     */   protected int getPhase(Lifecycle bean)
/*     */   {
/* 302 */     return (bean instanceof Phased) ? ((Phased)bean).getPhase() : 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private class LifecycleGroup
/*     */   {
/* 312 */     private final List<DefaultLifecycleProcessor.LifecycleGroupMember> members = new ArrayList();
/*     */     
/*     */     private final int phase;
/*     */     
/*     */     private final long timeout;
/*     */     
/*     */     private final Map<String, ? extends Lifecycle> lifecycleBeans;
/*     */     
/*     */     private final boolean autoStartupOnly;
/*     */     private volatile int smartMemberCount;
/*     */     
/*     */     public LifecycleGroup(long phase, Map<String, ? extends Lifecycle> arg4, boolean lifecycleBeans)
/*     */     {
/* 325 */       this.phase = phase;
/* 326 */       this.timeout = timeout;
/* 327 */       this.lifecycleBeans = lifecycleBeans;
/* 328 */       this.autoStartupOnly = autoStartupOnly;
/*     */     }
/*     */     
/*     */     public void add(String name, Lifecycle bean) {
/* 332 */       if ((bean instanceof SmartLifecycle)) {
/* 333 */         this.smartMemberCount += 1;
/*     */       }
/* 335 */       this.members.add(new DefaultLifecycleProcessor.LifecycleGroupMember(DefaultLifecycleProcessor.this, name, bean));
/*     */     }
/*     */     
/*     */     public void start() {
/* 339 */       if (this.members.isEmpty()) {
/* 340 */         return;
/*     */       }
/* 342 */       if (DefaultLifecycleProcessor.this.logger.isInfoEnabled()) {
/* 343 */         DefaultLifecycleProcessor.this.logger.info("Starting beans in phase " + this.phase);
/*     */       }
/* 345 */       Collections.sort(this.members);
/* 346 */       for (DefaultLifecycleProcessor.LifecycleGroupMember member : this.members) {
/* 347 */         if (this.lifecycleBeans.containsKey(member.name)) {
/* 348 */           DefaultLifecycleProcessor.this.doStart(this.lifecycleBeans, member.name, this.autoStartupOnly);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void stop() {
/* 354 */       if (this.members.isEmpty()) {
/* 355 */         return;
/*     */       }
/* 357 */       if (DefaultLifecycleProcessor.this.logger.isInfoEnabled()) {
/* 358 */         DefaultLifecycleProcessor.this.logger.info("Stopping beans in phase " + this.phase);
/*     */       }
/* 360 */       Collections.sort(this.members, Collections.reverseOrder());
/* 361 */       CountDownLatch latch = new CountDownLatch(this.smartMemberCount);
/* 362 */       Set<String> countDownBeanNames = Collections.synchronizedSet(new LinkedHashSet());
/* 363 */       for (DefaultLifecycleProcessor.LifecycleGroupMember member : this.members) {
/* 364 */         if (this.lifecycleBeans.containsKey(member.name)) {
/* 365 */           DefaultLifecycleProcessor.this.doStop(this.lifecycleBeans, member.name, latch, countDownBeanNames);
/*     */         }
/* 367 */         else if ((member.bean instanceof SmartLifecycle))
/*     */         {
/* 369 */           latch.countDown();
/*     */         }
/*     */       }
/*     */       try {
/* 373 */         latch.await(this.timeout, TimeUnit.MILLISECONDS);
/* 374 */         if ((latch.getCount() > 0L) && (!countDownBeanNames.isEmpty()) && (DefaultLifecycleProcessor.this.logger.isWarnEnabled())) {
/* 375 */           DefaultLifecycleProcessor.this.logger.warn("Failed to shut down " + countDownBeanNames.size() + " bean" + (countDownBeanNames
/* 376 */             .size() > 1 ? "s" : "") + " with phase value " + this.phase + " within timeout of " + this.timeout + ": " + countDownBeanNames);
/*     */         }
/*     */       }
/*     */       catch (InterruptedException ex)
/*     */       {
/* 381 */         Thread.currentThread().interrupt();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class LifecycleGroupMember
/*     */     implements Comparable<LifecycleGroupMember>
/*     */   {
/*     */     private final String name;
/*     */     
/*     */     private final Lifecycle bean;
/*     */     
/*     */ 
/*     */     LifecycleGroupMember(String name, Lifecycle bean)
/*     */     {
/* 397 */       this.name = name;
/* 398 */       this.bean = bean;
/*     */     }
/*     */     
/*     */     public int compareTo(LifecycleGroupMember other)
/*     */     {
/* 403 */       int thisOrder = DefaultLifecycleProcessor.this.getPhase(this.bean);
/* 404 */       int otherOrder = DefaultLifecycleProcessor.this.getPhase(other.bean);
/* 405 */       return thisOrder < otherOrder ? -1 : thisOrder == otherOrder ? 0 : 1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\support\DefaultLifecycleProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */