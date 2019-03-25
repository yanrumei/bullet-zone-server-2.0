/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.base.Function;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.MoreObjects.ToStringHelper;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.base.Stopwatch;
/*     */ import com.google.common.collect.Collections2;
/*     */ import com.google.common.collect.ImmutableCollection;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.common.collect.ImmutableMultimap;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.ImmutableSetMultimap;
/*     */ import com.google.common.collect.ImmutableSetMultimap.Builder;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.MultimapBuilder;
/*     */ import com.google.common.collect.MultimapBuilder.MultimapBuilderWithKeys;
/*     */ import com.google.common.collect.MultimapBuilder.SetMultimapBuilder;
/*     */ import com.google.common.collect.Multimaps;
/*     */ import com.google.common.collect.Multiset;
/*     */ import com.google.common.collect.Ordering;
/*     */ import com.google.common.collect.SetMultimap;
/*     */ import com.google.common.collect.UnmodifiableIterator;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.concurrent.GuardedBy;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Beta
/*     */ @GwtIncompatible
/*     */ public final class ServiceManager
/*     */ {
/* 122 */   private static final Logger logger = Logger.getLogger(ServiceManager.class.getName());
/* 123 */   private static final ListenerCallQueue.Event<Listener> HEALTHY_EVENT = new ListenerCallQueue.Event()
/*     */   {
/*     */     public void call(ServiceManager.Listener listener)
/*     */     {
/* 127 */       listener.healthy();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 132 */       return "healthy()";
/*     */     }
/*     */   };
/* 135 */   private static final ListenerCallQueue.Event<Listener> STOPPED_EVENT = new ListenerCallQueue.Event()
/*     */   {
/*     */     public void call(ServiceManager.Listener listener)
/*     */     {
/* 139 */       listener.stopped();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 144 */       return "stopped()";
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ServiceManagerState state;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ImmutableList<Service> services;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static abstract class Listener
/*     */   {
/*     */     public void healthy() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void stopped() {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void failure(Service service) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ServiceManager(Iterable<? extends Service> services)
/*     */   {
/* 202 */     ImmutableList<Service> copy = ImmutableList.copyOf(services);
/* 203 */     if (copy.isEmpty())
/*     */     {
/*     */ 
/* 206 */       logger.log(Level.WARNING, "ServiceManager configured with no services.  Is your application configured properly?", new EmptyServiceManagerWarning(null));
/*     */       
/*     */ 
/*     */ 
/* 210 */       copy = ImmutableList.of(new NoOpService(null));
/*     */     }
/* 212 */     this.state = new ServiceManagerState(copy);
/* 213 */     this.services = copy;
/* 214 */     WeakReference<ServiceManagerState> stateReference = new WeakReference(this.state);
/*     */     
/* 216 */     for (UnmodifiableIterator localUnmodifiableIterator = copy.iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
/* 217 */       service.addListener(new ServiceListener(service, stateReference), MoreExecutors.directExecutor());
/*     */       
/*     */ 
/* 220 */       Preconditions.checkArgument(service.state() == Service.State.NEW, "Can only manage NEW services, %s", service);
/*     */     }
/*     */     
/*     */ 
/* 224 */     this.state.markReady();
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
/*     */   public void addListener(Listener listener, Executor executor)
/*     */   {
/* 251 */     this.state.addListener(listener, executor);
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
/*     */   public void addListener(Listener listener)
/*     */   {
/* 271 */     this.state.addListener(listener, MoreExecutors.directExecutor());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public ServiceManager startAsync()
/*     */   {
/* 284 */     for (UnmodifiableIterator localUnmodifiableIterator = this.services.iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
/* 285 */       Service.State state = service.state();
/* 286 */       Preconditions.checkState(state == Service.State.NEW, "Service %s is %s, cannot start it.", service, state);
/*     */     }
/* 288 */     for (localUnmodifiableIterator = this.services.iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
/*     */       try {
/* 290 */         this.state.tryStartTiming(service);
/* 291 */         service.startAsync();
/*     */ 
/*     */       }
/*     */       catch (IllegalStateException e)
/*     */       {
/*     */ 
/* 297 */         logger.log(Level.WARNING, "Unable to start Service " + service, e);
/*     */       }
/*     */     }
/* 300 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void awaitHealthy()
/*     */   {
/* 312 */     this.state.awaitHealthy();
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
/*     */   public void awaitHealthy(long timeout, TimeUnit unit)
/*     */     throws TimeoutException
/*     */   {
/* 327 */     this.state.awaitHealthy(timeout, unit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @CanIgnoreReturnValue
/*     */   public ServiceManager stopAsync()
/*     */   {
/* 338 */     for (UnmodifiableIterator localUnmodifiableIterator = this.services.iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
/* 339 */       service.stopAsync();
/*     */     }
/* 341 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void awaitStopped()
/*     */   {
/* 350 */     this.state.awaitStopped();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void awaitStopped(long timeout, TimeUnit unit)
/*     */     throws TimeoutException
/*     */   {
/* 363 */     this.state.awaitStopped(timeout, unit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isHealthy()
/*     */   {
/* 373 */     for (UnmodifiableIterator localUnmodifiableIterator = this.services.iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
/* 374 */       if (!service.isRunning()) {
/* 375 */         return false;
/*     */       }
/*     */     }
/* 378 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImmutableMultimap<Service.State, Service> servicesByState()
/*     */   {
/* 388 */     return this.state.servicesByState();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImmutableMap<Service, Long> startupTimes()
/*     */   {
/* 399 */     return this.state.startupTimes();
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 404 */     return 
/*     */     
/* 406 */       MoreObjects.toStringHelper(ServiceManager.class).add("services", Collections2.filter(this.services, Predicates.not(Predicates.instanceOf(NoOpService.class)))).toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final class ServiceManagerState
/*     */   {
/* 414 */     final Monitor monitor = new Monitor();
/*     */     
/*     */ 
/*     */     @GuardedBy("monitor")
/* 418 */     final SetMultimap<Service.State, Service> servicesByState = MultimapBuilder.enumKeys(Service.State.class).linkedHashSetValues().build();
/*     */     @GuardedBy("monitor")
/* 420 */     final Multiset<Service.State> states = this.servicesByState
/* 421 */       .keys();
/*     */     
/*     */     @GuardedBy("monitor")
/* 424 */     final Map<Service, Stopwatch> startupTimers = Maps.newIdentityHashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @GuardedBy("monitor")
/*     */     boolean ready;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @GuardedBy("monitor")
/*     */     boolean transitioned;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     final int numberOfServices;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 451 */     final Monitor.Guard awaitHealthGuard = new AwaitHealthGuard();
/*     */     
/*     */     final class AwaitHealthGuard extends Monitor.Guard
/*     */     {
/*     */       AwaitHealthGuard() {
/* 456 */         super();
/*     */       }
/*     */       
/*     */ 
/*     */       @GuardedBy("ServiceManagerState.this.monitor")
/*     */       public boolean isSatisfied()
/*     */       {
/* 463 */         return (ServiceManager.ServiceManagerState.this.states.count(Service.State.RUNNING) == ServiceManager.ServiceManagerState.this.numberOfServices) || 
/* 464 */           (ServiceManager.ServiceManagerState.this.states.contains(Service.State.STOPPING)) || 
/* 465 */           (ServiceManager.ServiceManagerState.this.states.contains(Service.State.TERMINATED)) || 
/* 466 */           (ServiceManager.ServiceManagerState.this.states.contains(Service.State.FAILED));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 473 */     final Monitor.Guard stoppedGuard = new StoppedGuard();
/*     */     
/*     */     final class StoppedGuard extends Monitor.Guard
/*     */     {
/*     */       StoppedGuard() {
/* 478 */         super();
/*     */       }
/*     */       
/*     */       @GuardedBy("ServiceManagerState.this.monitor")
/*     */       public boolean isSatisfied()
/*     */       {
/* 484 */         return ServiceManager.ServiceManagerState.this.states.count(Service.State.TERMINATED) + ServiceManager.ServiceManagerState.this.states.count(Service.State.FAILED) == ServiceManager.ServiceManagerState.this.numberOfServices;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 489 */     final ListenerCallQueue<ServiceManager.Listener> listeners = new ListenerCallQueue();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     ServiceManagerState(ImmutableCollection<Service> services)
/*     */     {
/* 498 */       this.numberOfServices = services.size();
/* 499 */       this.servicesByState.putAll(Service.State.NEW, services);
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     void tryStartTiming(Service service)
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 4	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */       //   4: invokevirtual 27	com/google/common/util/concurrent/Monitor:enter	()V
/*     */       //   7: aload_0
/*     */       //   8: getfield 13	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:startupTimers	Ljava/util/Map;
/*     */       //   11: aload_1
/*     */       //   12: invokeinterface 28 2 0
/*     */       //   17: checkcast 29	com/google/common/base/Stopwatch
/*     */       //   20: astore_2
/*     */       //   21: aload_2
/*     */       //   22: ifnonnull +17 -> 39
/*     */       //   25: aload_0
/*     */       //   26: getfield 13	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:startupTimers	Ljava/util/Map;
/*     */       //   29: aload_1
/*     */       //   30: invokestatic 30	com/google/common/base/Stopwatch:createStarted	()Lcom/google/common/base/Stopwatch;
/*     */       //   33: invokeinterface 31 3 0
/*     */       //   38: pop
/*     */       //   39: aload_0
/*     */       //   40: getfield 4	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */       //   43: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */       //   46: goto +13 -> 59
/*     */       //   49: astore_3
/*     */       //   50: aload_0
/*     */       //   51: getfield 4	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */       //   54: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */       //   57: aload_3
/*     */       //   58: athrow
/*     */       //   59: return
/*     */       // Line number table:
/*     */       //   Java source line #507	-> byte code offset #0
/*     */       //   Java source line #509	-> byte code offset #7
/*     */       //   Java source line #510	-> byte code offset #21
/*     */       //   Java source line #511	-> byte code offset #25
/*     */       //   Java source line #514	-> byte code offset #39
/*     */       //   Java source line #515	-> byte code offset #46
/*     */       //   Java source line #514	-> byte code offset #49
/*     */       //   Java source line #516	-> byte code offset #59
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	60	0	this	ServiceManagerState
/*     */       //   0	60	1	service	Service
/*     */       //   20	2	2	stopwatch	Stopwatch
/*     */       //   49	9	3	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   7	39	49	finally
/*     */     }
/*     */     
/*     */     void markReady()
/*     */     {
/* 523 */       this.monitor.enter();
/*     */       try {
/* 525 */         if (!this.transitioned)
/*     */         {
/* 527 */           this.ready = true;
/*     */         }
/*     */         else {
/* 530 */           List<Service> servicesInBadStates = Lists.newArrayList();
/* 531 */           for (UnmodifiableIterator localUnmodifiableIterator = servicesByState().values().iterator(); localUnmodifiableIterator.hasNext();) { Service service = (Service)localUnmodifiableIterator.next();
/* 532 */             if (service.state() != Service.State.NEW) {
/* 533 */               servicesInBadStates.add(service);
/*     */             }
/*     */           }
/* 536 */           throw new IllegalArgumentException("Services started transitioning asynchronously before the ServiceManager was constructed: " + servicesInBadStates);
/*     */         }
/*     */         
/*     */       }
/*     */       finally
/*     */       {
/* 542 */         this.monitor.leave();
/*     */       }
/*     */     }
/*     */     
/*     */     void addListener(ServiceManager.Listener listener, Executor executor) {
/* 547 */       this.listeners.addListener(listener, executor);
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     void awaitHealthy()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 4	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */       //   4: aload_0
/*     */       //   5: getfield 16	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:awaitHealthGuard	Lcom/google/common/util/concurrent/Monitor$Guard;
/*     */       //   8: invokevirtual 53	com/google/common/util/concurrent/Monitor:enterWhenUninterruptibly	(Lcom/google/common/util/concurrent/Monitor$Guard;)V
/*     */       //   11: aload_0
/*     */       //   12: invokevirtual 54	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:checkHealthy	()V
/*     */       //   15: aload_0
/*     */       //   16: getfield 4	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */       //   19: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */       //   22: goto +13 -> 35
/*     */       //   25: astore_1
/*     */       //   26: aload_0
/*     */       //   27: getfield 4	com/google/common/util/concurrent/ServiceManager$ServiceManagerState:monitor	Lcom/google/common/util/concurrent/Monitor;
/*     */       //   30: invokevirtual 32	com/google/common/util/concurrent/Monitor:leave	()V
/*     */       //   33: aload_1
/*     */       //   34: athrow
/*     */       //   35: return
/*     */       // Line number table:
/*     */       //   Java source line #551	-> byte code offset #0
/*     */       //   Java source line #553	-> byte code offset #11
/*     */       //   Java source line #555	-> byte code offset #15
/*     */       //   Java source line #556	-> byte code offset #22
/*     */       //   Java source line #555	-> byte code offset #25
/*     */       //   Java source line #557	-> byte code offset #35
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	36	0	this	ServiceManagerState
/*     */       //   25	9	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   11	15	25	finally
/*     */     }
/*     */     
/*     */     void awaitHealthy(long timeout, TimeUnit unit)
/*     */       throws TimeoutException
/*     */     {
/* 560 */       this.monitor.enter();
/*     */       try {
/* 562 */         if (!this.monitor.waitForUninterruptibly(this.awaitHealthGuard, timeout, unit))
/*     */         {
/*     */ 
/*     */ 
/* 566 */           throw new TimeoutException("Timeout waiting for the services to become healthy. The following services have not started: " + Multimaps.filterKeys(this.servicesByState, Predicates.in(ImmutableSet.of(Service.State.NEW, Service.State.STARTING))));
/*     */         }
/* 568 */         checkHealthy();
/*     */       } finally {
/* 570 */         this.monitor.leave();
/*     */       }
/*     */     }
/*     */     
/*     */     void awaitStopped() {
/* 575 */       this.monitor.enterWhenUninterruptibly(this.stoppedGuard);
/* 576 */       this.monitor.leave();
/*     */     }
/*     */     
/*     */     void awaitStopped(long timeout, TimeUnit unit) throws TimeoutException {
/* 580 */       this.monitor.enter();
/*     */       try {
/* 582 */         if (!this.monitor.waitForUninterruptibly(this.stoppedGuard, timeout, unit))
/*     */         {
/*     */ 
/*     */ 
/* 586 */           throw new TimeoutException("Timeout waiting for the services to stop. The following services have not stopped: " + Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.in(EnumSet.of(Service.State.TERMINATED, Service.State.FAILED)))));
/*     */         }
/*     */       } finally {
/* 589 */         this.monitor.leave();
/*     */       }
/*     */     }
/*     */     
/*     */     ImmutableMultimap<Service.State, Service> servicesByState() {
/* 594 */       ImmutableSetMultimap.Builder<Service.State, Service> builder = ImmutableSetMultimap.builder();
/* 595 */       this.monitor.enter();
/*     */       try {
/* 597 */         for (Map.Entry<Service.State, Service> entry : this.servicesByState.entries()) {
/* 598 */           if (!(entry.getValue() instanceof ServiceManager.NoOpService)) {
/* 599 */             builder.put(entry);
/*     */           }
/*     */         }
/*     */       } finally {
/* 603 */         this.monitor.leave();
/*     */       }
/* 605 */       return builder.build();
/*     */     }
/*     */     
/*     */     ImmutableMap<Service, Long> startupTimes()
/*     */     {
/* 610 */       this.monitor.enter();
/*     */       try {
/* 612 */         loadTimes = Lists.newArrayListWithCapacity(this.startupTimers.size());
/*     */         
/* 614 */         for (Map.Entry<Service, Stopwatch> entry : this.startupTimers.entrySet()) {
/* 615 */           Service service = (Service)entry.getKey();
/* 616 */           Stopwatch stopWatch = (Stopwatch)entry.getValue();
/* 617 */           if ((!stopWatch.isRunning()) && (!(service instanceof ServiceManager.NoOpService)))
/* 618 */             loadTimes.add(Maps.immutableEntry(service, Long.valueOf(stopWatch.elapsed(TimeUnit.MILLISECONDS))));
/*     */         }
/*     */       } finally {
/*     */         List<Map.Entry<Service, Long>> loadTimes;
/* 622 */         this.monitor.leave(); }
/*     */       List<Map.Entry<Service, Long>> loadTimes;
/* 624 */       Collections.sort(loadTimes, 
/*     */       
/* 626 */         Ordering.natural()
/* 627 */         .onResultOf(new Function()
/*     */         {
/*     */ 
/*     */           public Long apply(Map.Entry<Service, Long> input)
/*     */           {
/* 631 */             return (Long)input.getValue();
/*     */           }
/* 633 */         }));
/* 634 */       return ImmutableMap.copyOf(loadTimes);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     void transitionService(Service service, Service.State from, Service.State to)
/*     */     {
/* 649 */       Preconditions.checkNotNull(service);
/* 650 */       Preconditions.checkArgument(from != to);
/* 651 */       this.monitor.enter();
/*     */       try {
/* 653 */         this.transitioned = true;
/* 654 */         if (!this.ready) {
/* 655 */           return;
/*     */         }
/*     */         
/* 658 */         Preconditions.checkState(
/* 659 */           this.servicesByState.remove(from, service), "Service %s not at the expected location in the state map %s", service, from);
/*     */         
/*     */ 
/*     */ 
/* 663 */         Preconditions.checkState(this.servicesByState
/* 664 */           .put(to, service), "Service %s in the state map unexpectedly at %s", service, to);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 669 */         Stopwatch stopwatch = (Stopwatch)this.startupTimers.get(service);
/* 670 */         if (stopwatch == null)
/*     */         {
/* 672 */           stopwatch = Stopwatch.createStarted();
/* 673 */           this.startupTimers.put(service, stopwatch);
/*     */         }
/* 675 */         if ((to.compareTo(Service.State.RUNNING) >= 0) && (stopwatch.isRunning()))
/*     */         {
/* 677 */           stopwatch.stop();
/* 678 */           if (!(service instanceof ServiceManager.NoOpService)) {
/* 679 */             ServiceManager.logger.log(Level.FINE, "Started {0} in {1}.", new Object[] { service, stopwatch });
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 685 */         if (to == Service.State.FAILED) {
/* 686 */           enqueueFailedEvent(service);
/*     */         }
/*     */         
/* 689 */         if (this.states.count(Service.State.RUNNING) == this.numberOfServices)
/*     */         {
/*     */ 
/* 692 */           enqueueHealthyEvent();
/* 693 */         } else if (this.states.count(Service.State.TERMINATED) + this.states.count(Service.State.FAILED) == this.numberOfServices) {
/* 694 */           enqueueStoppedEvent();
/*     */         }
/*     */       } finally {
/* 697 */         this.monitor.leave();
/*     */         
/* 699 */         dispatchListenerEvents();
/*     */       }
/*     */     }
/*     */     
/*     */     void enqueueStoppedEvent() {
/* 704 */       this.listeners.enqueue(ServiceManager.STOPPED_EVENT);
/*     */     }
/*     */     
/*     */     void enqueueHealthyEvent() {
/* 708 */       this.listeners.enqueue(ServiceManager.HEALTHY_EVENT);
/*     */     }
/*     */     
/*     */     void enqueueFailedEvent(final Service service) {
/* 712 */       this.listeners.enqueue(new ListenerCallQueue.Event()
/*     */       {
/*     */         public void call(ServiceManager.Listener listener)
/*     */         {
/* 716 */           listener.failure(service);
/*     */         }
/*     */         
/*     */         public String toString()
/*     */         {
/* 721 */           return "failed({service=" + service + "})";
/*     */         }
/*     */       });
/*     */     }
/*     */     
/*     */     void dispatchListenerEvents()
/*     */     {
/* 728 */       Preconditions.checkState(
/* 729 */         !this.monitor.isOccupiedByCurrentThread(), "It is incorrect to execute listeners with the monitor held.");
/*     */       
/* 731 */       this.listeners.dispatch();
/*     */     }
/*     */     
/*     */     @GuardedBy("monitor")
/*     */     void checkHealthy() {
/* 736 */       if (this.states.count(Service.State.RUNNING) != this.numberOfServices)
/*     */       {
/*     */ 
/*     */ 
/* 740 */         IllegalStateException exception = new IllegalStateException("Expected to be healthy after starting. The following services are not running: " + Multimaps.filterKeys(this.servicesByState, Predicates.not(Predicates.equalTo(Service.State.RUNNING))));
/* 741 */         throw exception;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class ServiceListener
/*     */     extends Service.Listener
/*     */   {
/*     */     final Service service;
/*     */     
/*     */     final WeakReference<ServiceManager.ServiceManagerState> state;
/*     */     
/*     */ 
/*     */     ServiceListener(Service service, WeakReference<ServiceManager.ServiceManagerState> state)
/*     */     {
/* 758 */       this.service = service;
/* 759 */       this.state = state;
/*     */     }
/*     */     
/*     */     public void starting()
/*     */     {
/* 764 */       ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
/* 765 */       if (state != null) {
/* 766 */         state.transitionService(this.service, Service.State.NEW, Service.State.STARTING);
/* 767 */         if (!(this.service instanceof ServiceManager.NoOpService)) {
/* 768 */           ServiceManager.logger.log(Level.FINE, "Starting {0}.", this.service);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void running()
/*     */     {
/* 775 */       ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
/* 776 */       if (state != null) {
/* 777 */         state.transitionService(this.service, Service.State.STARTING, Service.State.RUNNING);
/*     */       }
/*     */     }
/*     */     
/*     */     public void stopping(Service.State from)
/*     */     {
/* 783 */       ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
/* 784 */       if (state != null) {
/* 785 */         state.transitionService(this.service, from, Service.State.STOPPING);
/*     */       }
/*     */     }
/*     */     
/*     */     public void terminated(Service.State from)
/*     */     {
/* 791 */       ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
/* 792 */       if (state != null) {
/* 793 */         if (!(this.service instanceof ServiceManager.NoOpService)) {
/* 794 */           ServiceManager.logger.log(Level.FINE, "Service {0} has terminated. Previous state was: {1}", new Object[] { this.service, from });
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 799 */         state.transitionService(this.service, from, Service.State.TERMINATED);
/*     */       }
/*     */     }
/*     */     
/*     */     public void failed(Service.State from, Throwable failure)
/*     */     {
/* 805 */       ServiceManager.ServiceManagerState state = (ServiceManager.ServiceManagerState)this.state.get();
/* 806 */       if (state != null)
/*     */       {
/*     */ 
/* 809 */         boolean log = !(this.service instanceof ServiceManager.NoOpService);
/* 810 */         if (log) {
/* 811 */           ServiceManager.logger.log(Level.SEVERE, "Service " + this.service + " has failed in the " + from + " state.", failure);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 816 */         state.transitionService(this.service, from, Service.State.FAILED);
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
/*     */   private static final class NoOpService
/*     */     extends AbstractService
/*     */   {
/*     */     protected void doStart()
/*     */     {
/* 832 */       notifyStarted();
/*     */     }
/*     */     
/*     */     protected void doStop()
/*     */     {
/* 837 */       notifyStopped();
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class EmptyServiceManagerWarning
/*     */     extends Throwable
/*     */   {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\ServiceManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */