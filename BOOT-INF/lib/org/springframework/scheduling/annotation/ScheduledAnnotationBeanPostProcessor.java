/*     */ package org.springframework.scheduling.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TimeZone;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ScheduledExecutorService;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.framework.AopProxyUtils;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.BeanNameAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.NamedBeanHolder;
/*     */ import org.springframework.beans.factory.support.MergedBeanDefinitionPostProcessor;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.EmbeddedValueResolverAware;
/*     */ import org.springframework.context.event.ContextRefreshedEvent;
/*     */ import org.springframework.core.MethodIntrospector;
/*     */ import org.springframework.core.MethodIntrospector.MetadataLookup;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.scheduling.TaskScheduler;
/*     */ import org.springframework.scheduling.config.CronTask;
/*     */ import org.springframework.scheduling.config.IntervalTask;
/*     */ import org.springframework.scheduling.config.ScheduledTask;
/*     */ import org.springframework.scheduling.config.ScheduledTaskRegistrar;
/*     */ import org.springframework.scheduling.support.CronTrigger;
/*     */ import org.springframework.scheduling.support.ScheduledMethodRunnable;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.StringValueResolver;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScheduledAnnotationBeanPostProcessor
/*     */   implements MergedBeanDefinitionPostProcessor, DestructionAwareBeanPostProcessor, Ordered, EmbeddedValueResolverAware, BeanNameAware, BeanFactoryAware, ApplicationContextAware, SmartInitializingSingleton, ApplicationListener<ContextRefreshedEvent>, DisposableBean
/*     */ {
/*     */   public static final String DEFAULT_TASK_SCHEDULER_BEAN_NAME = "taskScheduler";
/* 112 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */   private Object scheduler;
/*     */   
/*     */   private StringValueResolver embeddedValueResolver;
/*     */   
/*     */   private String beanName;
/*     */   
/*     */   private BeanFactory beanFactory;
/*     */   
/*     */   private ApplicationContext applicationContext;
/*     */   
/* 124 */   private final ScheduledTaskRegistrar registrar = new ScheduledTaskRegistrar();
/*     */   
/*     */ 
/* 127 */   private final Set<Class<?>> nonAnnotatedClasses = Collections.newSetFromMap(new ConcurrentHashMap(64));
/*     */   
/* 129 */   private final Map<Object, Set<ScheduledTask>> scheduledTasks = new IdentityHashMap(16);
/*     */   
/*     */ 
/*     */ 
/*     */   public int getOrder()
/*     */   {
/* 135 */     return Integer.MAX_VALUE;
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
/*     */   public void setScheduler(Object scheduler)
/*     */   {
/* 150 */     this.scheduler = scheduler;
/*     */   }
/*     */   
/*     */   public void setEmbeddedValueResolver(StringValueResolver resolver)
/*     */   {
/* 155 */     this.embeddedValueResolver = resolver;
/*     */   }
/*     */   
/*     */   public void setBeanName(String beanName)
/*     */   {
/* 160 */     this.beanName = beanName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBeanFactory(BeanFactory beanFactory)
/*     */   {
/* 170 */     this.beanFactory = beanFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setApplicationContext(ApplicationContext applicationContext)
/*     */   {
/* 180 */     this.applicationContext = applicationContext;
/* 181 */     if (this.beanFactory == null) {
/* 182 */       this.beanFactory = applicationContext;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void afterSingletonsInstantiated()
/*     */   {
/* 190 */     this.nonAnnotatedClasses.clear();
/*     */     
/* 192 */     if (this.applicationContext == null)
/*     */     {
/* 194 */       finishRegistration();
/*     */     }
/*     */   }
/*     */   
/*     */   public void onApplicationEvent(ContextRefreshedEvent event)
/*     */   {
/* 200 */     if (event.getApplicationContext() == this.applicationContext)
/*     */     {
/*     */ 
/*     */ 
/* 204 */       finishRegistration();
/*     */     }
/*     */   }
/*     */   
/*     */   private void finishRegistration() {
/* 209 */     if (this.scheduler != null) {
/* 210 */       this.registrar.setScheduler(this.scheduler);
/*     */     }
/*     */     
/* 213 */     if ((this.beanFactory instanceof ListableBeanFactory))
/*     */     {
/* 215 */       Map<String, SchedulingConfigurer> beans = ((ListableBeanFactory)this.beanFactory).getBeansOfType(SchedulingConfigurer.class);
/* 216 */       List<SchedulingConfigurer> configurers = new ArrayList(beans.values());
/* 217 */       AnnotationAwareOrderComparator.sort(configurers);
/* 218 */       for (SchedulingConfigurer configurer : configurers) {
/* 219 */         configurer.configureTasks(this.registrar);
/*     */       }
/*     */     }
/*     */     
/* 223 */     if ((this.registrar.hasTasks()) && (this.registrar.getScheduler() == null)) {
/* 224 */       Assert.state(this.beanFactory != null, "BeanFactory must be set to find scheduler by type");
/*     */       try
/*     */       {
/* 227 */         this.registrar.setTaskScheduler((TaskScheduler)resolveSchedulerBean(TaskScheduler.class, false));
/*     */       }
/*     */       catch (NoUniqueBeanDefinitionException ex) {
/* 230 */         this.logger.debug("Could not find unique TaskScheduler bean", ex);
/*     */         try {
/* 232 */           this.registrar.setTaskScheduler((TaskScheduler)resolveSchedulerBean(TaskScheduler.class, true));
/*     */         }
/*     */         catch (NoSuchBeanDefinitionException ex2) {
/* 235 */           if (this.logger.isInfoEnabled()) {
/* 236 */             this.logger.info("More than one TaskScheduler bean exists within the context, and none is named 'taskScheduler'. Mark one of them as primary or name it 'taskScheduler' (possibly as an alias); or implement the SchedulingConfigurer interface and call ScheduledTaskRegistrar#setScheduler explicitly within the configureTasks() callback: " + ex
/*     */             
/*     */ 
/*     */ 
/* 240 */               .getBeanNamesFound());
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (NoSuchBeanDefinitionException ex) {
/* 245 */         this.logger.debug("Could not find default TaskScheduler bean", ex);
/*     */         try
/*     */         {
/* 248 */           this.registrar.setScheduler(resolveSchedulerBean(ScheduledExecutorService.class, false));
/*     */         }
/*     */         catch (NoUniqueBeanDefinitionException ex2) {
/* 251 */           this.logger.debug("Could not find unique ScheduledExecutorService bean", ex2);
/*     */           try {
/* 253 */             this.registrar.setScheduler(resolveSchedulerBean(ScheduledExecutorService.class, true));
/*     */           }
/*     */           catch (NoSuchBeanDefinitionException ex3) {
/* 256 */             if (this.logger.isInfoEnabled()) {
/* 257 */               this.logger.info("More than one ScheduledExecutorService bean exists within the context, and none is named 'taskScheduler'. Mark one of them as primary or name it 'taskScheduler' (possibly as an alias); or implement the SchedulingConfigurer interface and call ScheduledTaskRegistrar#setScheduler explicitly within the configureTasks() callback: " + ex2
/*     */               
/*     */ 
/*     */ 
/* 261 */                 .getBeanNamesFound());
/*     */             }
/*     */           }
/*     */         }
/*     */         catch (NoSuchBeanDefinitionException ex2) {
/* 266 */           this.logger.debug("Could not find default ScheduledExecutorService bean", ex2);
/*     */           
/* 268 */           this.logger.info("No TaskScheduler/ScheduledExecutorService bean found for scheduled processing");
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 273 */     this.registrar.afterPropertiesSet();
/*     */   }
/*     */   
/*     */   private <T> T resolveSchedulerBean(Class<T> schedulerType, boolean byName) {
/* 277 */     if (byName) {
/* 278 */       T scheduler = this.beanFactory.getBean("taskScheduler", schedulerType);
/* 279 */       if ((this.beanFactory instanceof ConfigurableBeanFactory)) {
/* 280 */         ((ConfigurableBeanFactory)this.beanFactory).registerDependentBean("taskScheduler", this.beanName);
/*     */       }
/*     */       
/* 283 */       return scheduler;
/*     */     }
/* 285 */     if ((this.beanFactory instanceof AutowireCapableBeanFactory)) {
/* 286 */       NamedBeanHolder<T> holder = ((AutowireCapableBeanFactory)this.beanFactory).resolveNamedBean(schedulerType);
/* 287 */       if ((this.beanFactory instanceof ConfigurableBeanFactory)) {
/* 288 */         ((ConfigurableBeanFactory)this.beanFactory).registerDependentBean(holder
/* 289 */           .getBeanName(), this.beanName);
/*     */       }
/* 291 */       return (T)holder.getBeanInstance();
/*     */     }
/*     */     
/* 294 */     return (T)this.beanFactory.getBean(schedulerType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void postProcessMergedBeanDefinition(RootBeanDefinition beanDefinition, Class<?> beanType, String beanName) {}
/*     */   
/*     */ 
/*     */ 
/*     */   public Object postProcessBeforeInitialization(Object bean, String beanName)
/*     */   {
/* 305 */     return bean;
/*     */   }
/*     */   
/*     */   public Object postProcessAfterInitialization(Object bean, String beanName)
/*     */   {
/* 310 */     Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
/* 311 */     if (!this.nonAnnotatedClasses.contains(targetClass)) {
/* 312 */       Map<Method, Set<Scheduled>> annotatedMethods = MethodIntrospector.selectMethods(targetClass, new MethodIntrospector.MetadataLookup()
/*     */       {
/*     */         public Set<Scheduled> inspect(Method method)
/*     */         {
/* 316 */           Set<Scheduled> scheduledMethods = AnnotatedElementUtils.getMergedRepeatableAnnotations(method, Scheduled.class, Schedules.class);
/*     */           
/* 318 */           return !scheduledMethods.isEmpty() ? scheduledMethods : null;
/*     */         }
/*     */       });
/* 321 */       if (annotatedMethods.isEmpty()) {
/* 322 */         this.nonAnnotatedClasses.add(targetClass);
/* 323 */         if (this.logger.isTraceEnabled()) {
/* 324 */           this.logger.trace("No @Scheduled annotations found on bean class: " + bean.getClass());
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 329 */         for (Map.Entry<Method, Set<Scheduled>> entry : annotatedMethods.entrySet()) {
/* 330 */           method = (Method)entry.getKey();
/* 331 */           for (Scheduled scheduled : (Set)entry.getValue())
/* 332 */             processScheduled(scheduled, method, bean);
/*     */         }
/*     */         Method method;
/* 335 */         if (this.logger.isDebugEnabled()) {
/* 336 */           this.logger.debug(annotatedMethods.size() + " @Scheduled methods processed on bean '" + beanName + "': " + annotatedMethods);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 341 */     return bean;
/*     */   }
/*     */   
/*     */   protected void processScheduled(Scheduled scheduled, Method method, Object bean) {
/*     */     try {
/* 346 */       Assert.isTrue(method.getParameterTypes().length == 0, "Only no-arg methods may be annotated with @Scheduled");
/*     */       
/*     */ 
/* 349 */       Method invocableMethod = AopUtils.selectInvocableMethod(method, bean.getClass());
/* 350 */       Runnable runnable = new ScheduledMethodRunnable(bean, invocableMethod);
/* 351 */       boolean processedSchedule = false;
/* 352 */       String errorMessage = "Exactly one of the 'cron', 'fixedDelay(String)', or 'fixedRate(String)' attributes is required";
/*     */       
/*     */ 
/* 355 */       Set<ScheduledTask> tasks = new LinkedHashSet(4);
/*     */       
/*     */ 
/* 358 */       long initialDelay = scheduled.initialDelay();
/* 359 */       String initialDelayString = scheduled.initialDelayString();
/* 360 */       if (StringUtils.hasText(initialDelayString)) {
/* 361 */         Assert.isTrue(initialDelay < 0L, "Specify 'initialDelay' or 'initialDelayString', not both");
/* 362 */         if (this.embeddedValueResolver != null) {
/* 363 */           initialDelayString = this.embeddedValueResolver.resolveStringValue(initialDelayString);
/*     */         }
/*     */         try {
/* 366 */           initialDelay = Long.parseLong(initialDelayString);
/*     */         }
/*     */         catch (NumberFormatException ex) {
/* 369 */           throw new IllegalArgumentException("Invalid initialDelayString value \"" + initialDelayString + "\" - cannot parse into integer");
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 375 */       String cron = scheduled.cron();
/* 376 */       if (StringUtils.hasText(cron)) {
/* 377 */         Assert.isTrue(initialDelay == -1L, "'initialDelay' not supported for cron triggers");
/* 378 */         processedSchedule = true;
/* 379 */         String zone = scheduled.zone();
/* 380 */         if (this.embeddedValueResolver != null) {
/* 381 */           cron = this.embeddedValueResolver.resolveStringValue(cron);
/* 382 */           zone = this.embeddedValueResolver.resolveStringValue(zone); }
/*     */         TimeZone timeZone;
/*     */         TimeZone timeZone;
/* 385 */         if (StringUtils.hasText(zone)) {
/* 386 */           timeZone = StringUtils.parseTimeZoneString(zone);
/*     */         }
/*     */         else {
/* 389 */           timeZone = TimeZone.getDefault();
/*     */         }
/* 391 */         tasks.add(this.registrar.scheduleCronTask(new CronTask(runnable, new CronTrigger(cron, timeZone))));
/*     */       }
/*     */       
/*     */ 
/* 395 */       if (initialDelay < 0L) {
/* 396 */         initialDelay = 0L;
/*     */       }
/*     */       
/*     */ 
/* 400 */       long fixedDelay = scheduled.fixedDelay();
/* 401 */       if (fixedDelay >= 0L) {
/* 402 */         Assert.isTrue(!processedSchedule, errorMessage);
/* 403 */         processedSchedule = true;
/* 404 */         tasks.add(this.registrar.scheduleFixedDelayTask(new IntervalTask(runnable, fixedDelay, initialDelay)));
/*     */       }
/* 406 */       String fixedDelayString = scheduled.fixedDelayString();
/* 407 */       if (StringUtils.hasText(fixedDelayString)) {
/* 408 */         Assert.isTrue(!processedSchedule, errorMessage);
/* 409 */         processedSchedule = true;
/* 410 */         if (this.embeddedValueResolver != null) {
/* 411 */           fixedDelayString = this.embeddedValueResolver.resolveStringValue(fixedDelayString);
/*     */         }
/*     */         try {
/* 414 */           fixedDelay = Long.parseLong(fixedDelayString);
/*     */         }
/*     */         catch (NumberFormatException ex) {
/* 417 */           throw new IllegalArgumentException("Invalid fixedDelayString value \"" + fixedDelayString + "\" - cannot parse into integer");
/*     */         }
/*     */         
/* 420 */         tasks.add(this.registrar.scheduleFixedDelayTask(new IntervalTask(runnable, fixedDelay, initialDelay)));
/*     */       }
/*     */       
/*     */ 
/* 424 */       long fixedRate = scheduled.fixedRate();
/* 425 */       if (fixedRate >= 0L) {
/* 426 */         Assert.isTrue(!processedSchedule, errorMessage);
/* 427 */         processedSchedule = true;
/* 428 */         tasks.add(this.registrar.scheduleFixedRateTask(new IntervalTask(runnable, fixedRate, initialDelay)));
/*     */       }
/* 430 */       String fixedRateString = scheduled.fixedRateString();
/* 431 */       if (StringUtils.hasText(fixedRateString)) {
/* 432 */         Assert.isTrue(!processedSchedule, errorMessage);
/* 433 */         processedSchedule = true;
/* 434 */         if (this.embeddedValueResolver != null) {
/* 435 */           fixedRateString = this.embeddedValueResolver.resolveStringValue(fixedRateString);
/*     */         }
/*     */         try {
/* 438 */           fixedRate = Long.parseLong(fixedRateString);
/*     */         }
/*     */         catch (NumberFormatException ex) {
/* 441 */           throw new IllegalArgumentException("Invalid fixedRateString value \"" + fixedRateString + "\" - cannot parse into integer");
/*     */         }
/*     */         
/* 444 */         tasks.add(this.registrar.scheduleFixedRateTask(new IntervalTask(runnable, fixedRate, initialDelay)));
/*     */       }
/*     */       
/*     */ 
/* 448 */       Assert.isTrue(processedSchedule, errorMessage);
/*     */       
/*     */ 
/* 451 */       synchronized (this.scheduledTasks) {
/* 452 */         Set<ScheduledTask> registeredTasks = (Set)this.scheduledTasks.get(bean);
/* 453 */         if (registeredTasks == null) {
/* 454 */           registeredTasks = new LinkedHashSet(4);
/* 455 */           this.scheduledTasks.put(bean, registeredTasks);
/*     */         }
/* 457 */         registeredTasks.addAll(tasks);
/*     */       }
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 462 */       throw new IllegalStateException("Encountered invalid @Scheduled method '" + method.getName() + "': " + ex.getMessage());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void postProcessBeforeDestruction(Object bean, String beanName)
/*     */   {
/*     */     Set<ScheduledTask> tasks;
/* 470 */     synchronized (this.scheduledTasks) {
/* 471 */       tasks = (Set)this.scheduledTasks.remove(bean); }
/*     */     Set<ScheduledTask> tasks;
/* 473 */     if (tasks != null) {
/* 474 */       for (??? = tasks.iterator(); ((Iterator)???).hasNext();) { ScheduledTask task = (ScheduledTask)((Iterator)???).next();
/* 475 */         task.cancel();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public boolean requiresDestruction(Object bean)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 14	org/springframework/scheduling/annotation/ScheduledAnnotationBeanPostProcessor:scheduledTasks	Ljava/util/Map;
/*     */     //   4: dup
/*     */     //   5: astore_2
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 14	org/springframework/scheduling/annotation/ScheduledAnnotationBeanPostProcessor:scheduledTasks	Ljava/util/Map;
/*     */     //   11: aload_1
/*     */     //   12: invokeinterface 152 2 0
/*     */     //   17: aload_2
/*     */     //   18: monitorexit
/*     */     //   19: ireturn
/*     */     //   20: astore_3
/*     */     //   21: aload_2
/*     */     //   22: monitorexit
/*     */     //   23: aload_3
/*     */     //   24: athrow
/*     */     // Line number table:
/*     */     //   Java source line #482	-> byte code offset #0
/*     */     //   Java source line #483	-> byte code offset #7
/*     */     //   Java source line #484	-> byte code offset #20
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	25	0	this	ScheduledAnnotationBeanPostProcessor
/*     */     //   0	25	1	bean	Object
/*     */     //   5	17	2	Ljava/lang/Object;	Object
/*     */     //   20	4	3	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	19	20	finally
/*     */     //   20	23	20	finally
/*     */   }
/*     */   
/*     */   public void destroy()
/*     */   {
/* 489 */     synchronized (this.scheduledTasks) {
/* 490 */       Collection<Set<ScheduledTask>> allTasks = this.scheduledTasks.values();
/* 491 */       for (Set<ScheduledTask> tasks : allTasks) {
/* 492 */         for (ScheduledTask task : tasks) {
/* 493 */           task.cancel();
/*     */         }
/*     */       }
/* 496 */       this.scheduledTasks.clear();
/*     */     }
/* 498 */     this.registrar.destroy();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\annotation\ScheduledAnnotationBeanPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */