/*     */ package com.google.common.util.concurrent;
/*     */ 
/*     */ import com.google.common.annotations.Beta;
/*     */ import com.google.common.annotations.GwtIncompatible;
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.base.MoreObjects;
/*     */ import com.google.common.base.Preconditions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.MapMaker;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.google.errorprone.annotations.CanIgnoreReturnValue;
/*     */ import com.google.j2objc.annotations.Weak;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.annotation.Nullable;
/*     */ import javax.annotation.concurrent.ThreadSafe;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ @CanIgnoreReturnValue
/*     */ @ThreadSafe
/*     */ @GwtIncompatible
/*     */ public class CycleDetectingLockFactory
/*     */ {
/*     */   @Beta
/*     */   @ThreadSafe
/*     */   public static abstract interface Policy
/*     */   {
/*     */     public abstract void handlePotentialDeadlock(CycleDetectingLockFactory.PotentialDeadlockException paramPotentialDeadlockException);
/*     */   }
/*     */   
/*     */   @Beta
/*     */   public static abstract enum Policies
/*     */     implements CycleDetectingLockFactory.Policy
/*     */   {
/* 201 */     THROW, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 213 */     WARN, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 228 */     DISABLED;
/*     */     
/*     */ 
/*     */ 
/*     */     private Policies() {}
/*     */   }
/*     */   
/*     */ 
/*     */   public static CycleDetectingLockFactory newInstance(Policy policy)
/*     */   {
/* 238 */     return new CycleDetectingLockFactory(policy);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ReentrantLock newReentrantLock(String lockName)
/*     */   {
/* 245 */     return newReentrantLock(lockName, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReentrantLock newReentrantLock(String lockName, boolean fair)
/*     */   {
/* 253 */     return this.policy == Policies.DISABLED ? new ReentrantLock(fair) : new CycleDetectingReentrantLock(new LockGraphNode(lockName), fair, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName)
/*     */   {
/* 262 */     return newReentrantReadWriteLock(lockName, false);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReentrantReadWriteLock newReentrantReadWriteLock(String lockName, boolean fair)
/*     */   {
/* 271 */     return this.policy == Policies.DISABLED ? new ReentrantReadWriteLock(fair) : new CycleDetectingReentrantReadWriteLock(new LockGraphNode(lockName), fair, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 278 */   private static final ConcurrentMap<Class<? extends Enum>, Map<? extends Enum, LockGraphNode>> lockGraphNodesPerType = new MapMaker().weakKeys().makeMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <E extends Enum<E>> WithExplicitOrdering<E> newInstanceWithExplicitOrdering(Class<E> enumClass, Policy policy)
/*     */   {
/* 287 */     Preconditions.checkNotNull(enumClass);
/* 288 */     Preconditions.checkNotNull(policy);
/*     */     
/* 290 */     Map<E, LockGraphNode> lockGraphNodes = getOrCreateNodes(enumClass);
/* 291 */     return new WithExplicitOrdering(policy, lockGraphNodes);
/*     */   }
/*     */   
/*     */   private static Map<? extends Enum, LockGraphNode> getOrCreateNodes(Class<? extends Enum> clazz) {
/* 295 */     Map<? extends Enum, LockGraphNode> existing = (Map)lockGraphNodesPerType.get(clazz);
/* 296 */     if (existing != null) {
/* 297 */       return existing;
/*     */     }
/* 299 */     Map<? extends Enum, LockGraphNode> created = createNodes(clazz);
/* 300 */     existing = (Map)lockGraphNodesPerType.putIfAbsent(clazz, created);
/* 301 */     return (Map)MoreObjects.firstNonNull(existing, created);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @VisibleForTesting
/*     */   static <E extends Enum<E>> Map<E, LockGraphNode> createNodes(Class<E> clazz)
/*     */   {
/* 312 */     EnumMap<E, LockGraphNode> map = Maps.newEnumMap(clazz);
/* 313 */     E[] keys = (Enum[])clazz.getEnumConstants();
/* 314 */     int numKeys = keys.length;
/* 315 */     ArrayList<LockGraphNode> nodes = Lists.newArrayListWithCapacity(numKeys);
/*     */     
/* 317 */     for (E key : keys) {
/* 318 */       LockGraphNode node = new LockGraphNode(getLockName(key));
/* 319 */       nodes.add(node);
/* 320 */       map.put(key, node);
/*     */     }
/*     */     
/* 323 */     for (int i = 1; i < numKeys; i++) {
/* 324 */       ((LockGraphNode)nodes.get(i)).checkAcquiredLocks(Policies.THROW, nodes.subList(0, i));
/*     */     }
/*     */     
/* 327 */     for (int i = 0; i < numKeys - 1; i++) {
/* 328 */       ((LockGraphNode)nodes.get(i)).checkAcquiredLocks(Policies.DISABLED, nodes.subList(i + 1, numKeys));
/*     */     }
/* 330 */     return Collections.unmodifiableMap(map);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static String getLockName(Enum<?> rank)
/*     */   {
/* 338 */     return rank.getDeclaringClass().getSimpleName() + "." + rank.name();
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Beta
/*     */   public static final class WithExplicitOrdering<E extends Enum<E>>
/*     */     extends CycleDetectingLockFactory
/*     */   {
/*     */     private final Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @VisibleForTesting
/*     */     WithExplicitOrdering(CycleDetectingLockFactory.Policy policy, Map<E, CycleDetectingLockFactory.LockGraphNode> lockGraphNodes)
/*     */     {
/* 406 */       super(null);
/* 407 */       this.lockGraphNodes = lockGraphNodes;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public ReentrantLock newReentrantLock(E rank)
/*     */     {
/* 414 */       return newReentrantLock(rank, false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ReentrantLock newReentrantLock(E rank, boolean fair)
/*     */     {
/* 426 */       return this.policy == CycleDetectingLockFactory.Policies.DISABLED ? new ReentrantLock(fair) : new CycleDetectingLockFactory.CycleDetectingReentrantLock(this, 
/*     */       
/* 428 */         (CycleDetectingLockFactory.LockGraphNode)this.lockGraphNodes.get(rank), fair, null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public ReentrantReadWriteLock newReentrantReadWriteLock(E rank)
/*     */     {
/* 435 */       return newReentrantReadWriteLock(rank, false);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ReentrantReadWriteLock newReentrantReadWriteLock(E rank, boolean fair)
/*     */     {
/* 447 */       return this.policy == CycleDetectingLockFactory.Policies.DISABLED ? new ReentrantReadWriteLock(fair) : new CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock(this, 
/*     */       
/* 449 */         (CycleDetectingLockFactory.LockGraphNode)this.lockGraphNodes.get(rank), fair, null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/* 455 */   private static final Logger logger = Logger.getLogger(CycleDetectingLockFactory.class.getName());
/*     */   final Policy policy;
/*     */   
/*     */   private CycleDetectingLockFactory(Policy policy)
/*     */   {
/* 460 */     this.policy = ((Policy)Preconditions.checkNotNull(policy));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 469 */   private static final ThreadLocal<ArrayList<LockGraphNode>> acquiredLocks = new ThreadLocal()
/*     */   {
/*     */     protected ArrayList<CycleDetectingLockFactory.LockGraphNode> initialValue()
/*     */     {
/* 473 */       return Lists.newArrayListWithCapacity(3);
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class ExampleStackTrace
/*     */     extends IllegalStateException
/*     */   {
/* 492 */     static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
/*     */     
/*     */ 
/* 495 */     static final ImmutableSet<String> EXCLUDED_CLASS_NAMES = ImmutableSet.of(CycleDetectingLockFactory.class
/* 496 */       .getName(), ExampleStackTrace.class
/* 497 */       .getName(), CycleDetectingLockFactory.LockGraphNode.class
/* 498 */       .getName());
/*     */     
/*     */     ExampleStackTrace(CycleDetectingLockFactory.LockGraphNode node1, CycleDetectingLockFactory.LockGraphNode node2) {
/* 501 */       super();
/* 502 */       StackTraceElement[] origStackTrace = getStackTrace();
/* 503 */       int i = 0; for (int n = origStackTrace.length; i < n; i++) {
/* 504 */         if (CycleDetectingLockFactory.WithExplicitOrdering.class.getName().equals(origStackTrace[i].getClassName()))
/*     */         {
/* 506 */           setStackTrace(EMPTY_STACK_TRACE);
/* 507 */           break;
/*     */         }
/* 509 */         if (!EXCLUDED_CLASS_NAMES.contains(origStackTrace[i].getClassName())) {
/* 510 */           setStackTrace((StackTraceElement[])Arrays.copyOfRange(origStackTrace, i, n));
/* 511 */           break;
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
/*     */   @Beta
/*     */   public static final class PotentialDeadlockException
/*     */     extends CycleDetectingLockFactory.ExampleStackTrace
/*     */   {
/*     */     private final CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private PotentialDeadlockException(CycleDetectingLockFactory.LockGraphNode node1, CycleDetectingLockFactory.LockGraphNode node2, CycleDetectingLockFactory.ExampleStackTrace conflictingStackTrace)
/*     */     {
/* 544 */       super(node2);
/* 545 */       this.conflictingStackTrace = conflictingStackTrace;
/* 546 */       initCause(conflictingStackTrace);
/*     */     }
/*     */     
/*     */     public CycleDetectingLockFactory.ExampleStackTrace getConflictingStackTrace() {
/* 550 */       return this.conflictingStackTrace;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public String getMessage()
/*     */     {
/* 559 */       StringBuilder message = new StringBuilder(super.getMessage());
/* 560 */       for (Throwable t = this.conflictingStackTrace; t != null; t = t.getCause()) {
/* 561 */         message.append(", ").append(t.getMessage());
/*     */       }
/* 563 */       return message.toString();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static abstract interface CycleDetectingLock
/*     */   {
/*     */     public abstract CycleDetectingLockFactory.LockGraphNode getLockGraphNode();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public abstract boolean isAcquiredByCurrentThread();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class LockGraphNode
/*     */   {
/* 591 */     final Map<LockGraphNode, CycleDetectingLockFactory.ExampleStackTrace> allowedPriorLocks = new MapMaker()
/* 592 */       .weakKeys().makeMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 598 */     final Map<LockGraphNode, CycleDetectingLockFactory.PotentialDeadlockException> disallowedPriorLocks = new MapMaker()
/* 599 */       .weakKeys().makeMap();
/*     */     final String lockName;
/*     */     
/*     */     LockGraphNode(String lockName)
/*     */     {
/* 604 */       this.lockName = ((String)Preconditions.checkNotNull(lockName));
/*     */     }
/*     */     
/*     */     String getLockName() {
/* 608 */       return this.lockName;
/*     */     }
/*     */     
/*     */     void checkAcquiredLocks(CycleDetectingLockFactory.Policy policy, List<LockGraphNode> acquiredLocks) {
/* 612 */       int i = 0; for (int size = acquiredLocks.size(); i < size; i++) {
/* 613 */         checkAcquiredLock(policy, (LockGraphNode)acquiredLocks.get(i));
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     void checkAcquiredLock(CycleDetectingLockFactory.Policy policy, LockGraphNode acquiredLock)
/*     */     {
/* 633 */       Preconditions.checkState(this != acquiredLock, "Attempted to acquire multiple locks with the same rank %s", acquiredLock
/*     */       
/*     */ 
/* 636 */         .getLockName());
/*     */       
/* 638 */       if (this.allowedPriorLocks.containsKey(acquiredLock))
/*     */       {
/*     */ 
/*     */ 
/* 642 */         return;
/*     */       }
/* 644 */       CycleDetectingLockFactory.PotentialDeadlockException previousDeadlockException = (CycleDetectingLockFactory.PotentialDeadlockException)this.disallowedPriorLocks.get(acquiredLock);
/* 645 */       if (previousDeadlockException != null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 651 */         CycleDetectingLockFactory.PotentialDeadlockException exception = new CycleDetectingLockFactory.PotentialDeadlockException(acquiredLock, this, previousDeadlockException.getConflictingStackTrace(), null);
/* 652 */         policy.handlePotentialDeadlock(exception);
/* 653 */         return;
/*     */       }
/*     */       
/*     */ 
/* 657 */       Set<LockGraphNode> seen = Sets.newIdentityHashSet();
/* 658 */       CycleDetectingLockFactory.ExampleStackTrace path = acquiredLock.findPathTo(this, seen);
/*     */       
/* 660 */       if (path == null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 669 */         this.allowedPriorLocks.put(acquiredLock, new CycleDetectingLockFactory.ExampleStackTrace(acquiredLock, this));
/*     */       }
/*     */       else
/*     */       {
/* 673 */         CycleDetectingLockFactory.PotentialDeadlockException exception = new CycleDetectingLockFactory.PotentialDeadlockException(acquiredLock, this, path, null);
/*     */         
/* 675 */         this.disallowedPriorLocks.put(acquiredLock, exception);
/* 676 */         policy.handlePotentialDeadlock(exception);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     @Nullable
/*     */     private CycleDetectingLockFactory.ExampleStackTrace findPathTo(LockGraphNode node, Set<LockGraphNode> seen)
/*     */     {
/* 689 */       if (!seen.add(this)) {
/* 690 */         return null;
/*     */       }
/* 692 */       CycleDetectingLockFactory.ExampleStackTrace found = (CycleDetectingLockFactory.ExampleStackTrace)this.allowedPriorLocks.get(node);
/* 693 */       if (found != null) {
/* 694 */         return found;
/*     */       }
/*     */       
/* 697 */       for (Map.Entry<LockGraphNode, CycleDetectingLockFactory.ExampleStackTrace> entry : this.allowedPriorLocks.entrySet()) {
/* 698 */         LockGraphNode preAcquiredLock = (LockGraphNode)entry.getKey();
/* 699 */         found = preAcquiredLock.findPathTo(node, seen);
/* 700 */         if (found != null)
/*     */         {
/*     */ 
/*     */ 
/* 704 */           CycleDetectingLockFactory.ExampleStackTrace path = new CycleDetectingLockFactory.ExampleStackTrace(preAcquiredLock, this);
/* 705 */           path.setStackTrace(((CycleDetectingLockFactory.ExampleStackTrace)entry.getValue()).getStackTrace());
/* 706 */           path.initCause(found);
/* 707 */           return path;
/*     */         }
/*     */       }
/* 710 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void aboutToAcquire(CycleDetectingLock lock)
/*     */   {
/* 718 */     if (!lock.isAcquiredByCurrentThread()) {
/* 719 */       ArrayList<LockGraphNode> acquiredLockList = (ArrayList)acquiredLocks.get();
/* 720 */       LockGraphNode node = lock.getLockGraphNode();
/* 721 */       node.checkAcquiredLocks(this.policy, acquiredLockList);
/* 722 */       acquiredLockList.add(node);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void lockStateChanged(CycleDetectingLock lock)
/*     */   {
/* 732 */     if (!lock.isAcquiredByCurrentThread()) {
/* 733 */       ArrayList<LockGraphNode> acquiredLockList = (ArrayList)acquiredLocks.get();
/* 734 */       LockGraphNode node = lock.getLockGraphNode();
/*     */       
/*     */ 
/* 737 */       for (int i = acquiredLockList.size() - 1; i >= 0; i--) {
/* 738 */         if (acquiredLockList.get(i) == node) {
/* 739 */           acquiredLockList.remove(i);
/* 740 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   final class CycleDetectingReentrantLock extends ReentrantLock implements CycleDetectingLockFactory.CycleDetectingLock
/*     */   {
/*     */     private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;
/*     */     
/*     */     private CycleDetectingReentrantLock(CycleDetectingLockFactory.LockGraphNode lockGraphNode, boolean fair) {
/* 751 */       super();
/* 752 */       this.lockGraphNode = ((CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(lockGraphNode));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public CycleDetectingLockFactory.LockGraphNode getLockGraphNode()
/*     */     {
/* 759 */       return this.lockGraphNode;
/*     */     }
/*     */     
/*     */     public boolean isAcquiredByCurrentThread()
/*     */     {
/* 764 */       return isHeldByCurrentThread();
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void lock()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 2	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantLock:this$0	Lcom/google/common/util/concurrent/CycleDetectingLockFactory;
/*     */       //   4: aload_0
/*     */       //   5: invokestatic 8	com/google/common/util/concurrent/CycleDetectingLockFactory:access$600	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory;Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   8: aload_0
/*     */       //   9: invokespecial 9	java/util/concurrent/locks/ReentrantLock:lock	()V
/*     */       //   12: aload_0
/*     */       //   13: invokestatic 10	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   16: goto +10 -> 26
/*     */       //   19: astore_1
/*     */       //   20: aload_0
/*     */       //   21: invokestatic 10	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   24: aload_1
/*     */       //   25: athrow
/*     */       //   26: return
/*     */       // Line number table:
/*     */       //   Java source line #771	-> byte code offset #0
/*     */       //   Java source line #773	-> byte code offset #8
/*     */       //   Java source line #775	-> byte code offset #12
/*     */       //   Java source line #776	-> byte code offset #16
/*     */       //   Java source line #775	-> byte code offset #19
/*     */       //   Java source line #777	-> byte code offset #26
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	27	0	this	CycleDetectingReentrantLock
/*     */       //   19	6	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   8	12	19	finally
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void lockInterruptibly()
/*     */       throws InterruptedException
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 2	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantLock:this$0	Lcom/google/common/util/concurrent/CycleDetectingLockFactory;
/*     */       //   4: aload_0
/*     */       //   5: invokestatic 8	com/google/common/util/concurrent/CycleDetectingLockFactory:access$600	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory;Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   8: aload_0
/*     */       //   9: invokespecial 11	java/util/concurrent/locks/ReentrantLock:lockInterruptibly	()V
/*     */       //   12: aload_0
/*     */       //   13: invokestatic 10	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   16: goto +10 -> 26
/*     */       //   19: astore_1
/*     */       //   20: aload_0
/*     */       //   21: invokestatic 10	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   24: aload_1
/*     */       //   25: athrow
/*     */       //   26: return
/*     */       // Line number table:
/*     */       //   Java source line #781	-> byte code offset #0
/*     */       //   Java source line #783	-> byte code offset #8
/*     */       //   Java source line #785	-> byte code offset #12
/*     */       //   Java source line #786	-> byte code offset #16
/*     */       //   Java source line #785	-> byte code offset #19
/*     */       //   Java source line #787	-> byte code offset #26
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	27	0	this	CycleDetectingReentrantLock
/*     */       //   19	6	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   8	12	19	finally
/*     */     }
/*     */     
/*     */     public boolean tryLock()
/*     */     {
/* 791 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*     */       try {
/* 793 */         return super.tryLock();
/*     */       } finally {
/* 795 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException
/*     */     {
/* 801 */       CycleDetectingLockFactory.this.aboutToAcquire(this);
/*     */       try {
/* 803 */         return super.tryLock(timeout, unit);
/*     */       } finally {
/* 805 */         CycleDetectingLockFactory.lockStateChanged(this);
/*     */       }
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void unlock()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokespecial 14	java/util/concurrent/locks/ReentrantLock:unlock	()V
/*     */       //   4: aload_0
/*     */       //   5: invokestatic 10	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   8: goto +10 -> 18
/*     */       //   11: astore_1
/*     */       //   12: aload_0
/*     */       //   13: invokestatic 10	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   16: aload_1
/*     */       //   17: athrow
/*     */       //   18: return
/*     */       // Line number table:
/*     */       //   Java source line #812	-> byte code offset #0
/*     */       //   Java source line #814	-> byte code offset #4
/*     */       //   Java source line #815	-> byte code offset #8
/*     */       //   Java source line #814	-> byte code offset #11
/*     */       //   Java source line #816	-> byte code offset #18
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	19	0	this	CycleDetectingReentrantLock
/*     */       //   11	6	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   0	4	11	finally
/*     */     }
/*     */   }
/*     */   
/*     */   final class CycleDetectingReentrantReadWriteLock
/*     */     extends ReentrantReadWriteLock
/*     */     implements CycleDetectingLockFactory.CycleDetectingLock
/*     */   {
/*     */     private final CycleDetectingLockFactory.CycleDetectingReentrantReadLock readLock;
/*     */     private final CycleDetectingLockFactory.CycleDetectingReentrantWriteLock writeLock;
/*     */     private final CycleDetectingLockFactory.LockGraphNode lockGraphNode;
/*     */     
/*     */     private CycleDetectingReentrantReadWriteLock(CycleDetectingLockFactory.LockGraphNode lockGraphNode, boolean fair)
/*     */     {
/* 832 */       super();
/* 833 */       this.readLock = new CycleDetectingLockFactory.CycleDetectingReentrantReadLock(CycleDetectingLockFactory.this, this);
/* 834 */       this.writeLock = new CycleDetectingLockFactory.CycleDetectingReentrantWriteLock(CycleDetectingLockFactory.this, this);
/* 835 */       this.lockGraphNode = ((CycleDetectingLockFactory.LockGraphNode)Preconditions.checkNotNull(lockGraphNode));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public ReentrantReadWriteLock.ReadLock readLock()
/*     */     {
/* 842 */       return this.readLock;
/*     */     }
/*     */     
/*     */     public ReentrantReadWriteLock.WriteLock writeLock()
/*     */     {
/* 847 */       return this.writeLock;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public CycleDetectingLockFactory.LockGraphNode getLockGraphNode()
/*     */     {
/* 854 */       return this.lockGraphNode;
/*     */     }
/*     */     
/*     */     public boolean isAcquiredByCurrentThread()
/*     */     {
/* 859 */       return (isWriteLockedByCurrentThread()) || (getReadHoldCount() > 0);
/*     */     }
/*     */   }
/*     */   
/*     */   private class CycleDetectingReentrantReadLock extends ReentrantReadWriteLock.ReadLock {
/*     */     @Weak
/*     */     final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;
/*     */     
/*     */     CycleDetectingReentrantReadLock(CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock) {
/* 868 */       super();
/* 869 */       this.readWriteLock = readWriteLock;
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void lock()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 1	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:this$0	Lcom/google/common/util/concurrent/CycleDetectingLockFactory;
/*     */       //   4: aload_0
/*     */       //   5: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   8: invokestatic 4	com/google/common/util/concurrent/CycleDetectingLockFactory:access$600	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory;Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   11: aload_0
/*     */       //   12: invokespecial 5	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lock	()V
/*     */       //   15: aload_0
/*     */       //   16: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   19: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   22: goto +13 -> 35
/*     */       //   25: astore_1
/*     */       //   26: aload_0
/*     */       //   27: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   30: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   33: aload_1
/*     */       //   34: athrow
/*     */       //   35: return
/*     */       // Line number table:
/*     */       //   Java source line #874	-> byte code offset #0
/*     */       //   Java source line #876	-> byte code offset #11
/*     */       //   Java source line #878	-> byte code offset #15
/*     */       //   Java source line #879	-> byte code offset #22
/*     */       //   Java source line #878	-> byte code offset #25
/*     */       //   Java source line #880	-> byte code offset #35
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	36	0	this	CycleDetectingReentrantReadLock
/*     */       //   25	9	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   11	15	25	finally
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void lockInterruptibly()
/*     */       throws InterruptedException
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 1	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:this$0	Lcom/google/common/util/concurrent/CycleDetectingLockFactory;
/*     */       //   4: aload_0
/*     */       //   5: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   8: invokestatic 4	com/google/common/util/concurrent/CycleDetectingLockFactory:access$600	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory;Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   11: aload_0
/*     */       //   12: invokespecial 7	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:lockInterruptibly	()V
/*     */       //   15: aload_0
/*     */       //   16: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   19: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   22: goto +13 -> 35
/*     */       //   25: astore_1
/*     */       //   26: aload_0
/*     */       //   27: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   30: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   33: aload_1
/*     */       //   34: athrow
/*     */       //   35: return
/*     */       // Line number table:
/*     */       //   Java source line #884	-> byte code offset #0
/*     */       //   Java source line #886	-> byte code offset #11
/*     */       //   Java source line #888	-> byte code offset #15
/*     */       //   Java source line #889	-> byte code offset #22
/*     */       //   Java source line #888	-> byte code offset #25
/*     */       //   Java source line #890	-> byte code offset #35
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	36	0	this	CycleDetectingReentrantReadLock
/*     */       //   25	9	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   11	15	25	finally
/*     */     }
/*     */     
/*     */     public boolean tryLock()
/*     */     {
/* 894 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 896 */         return super.tryLock();
/*     */       } finally {
/* 898 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException
/*     */     {
/* 904 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 906 */         return super.tryLock(timeout, unit);
/*     */       } finally {
/* 908 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       }
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void unlock()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokespecial 10	java/util/concurrent/locks/ReentrantReadWriteLock$ReadLock:unlock	()V
/*     */       //   4: aload_0
/*     */       //   5: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   8: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   11: goto +13 -> 24
/*     */       //   14: astore_1
/*     */       //   15: aload_0
/*     */       //   16: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   19: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   22: aload_1
/*     */       //   23: athrow
/*     */       //   24: return
/*     */       // Line number table:
/*     */       //   Java source line #915	-> byte code offset #0
/*     */       //   Java source line #917	-> byte code offset #4
/*     */       //   Java source line #918	-> byte code offset #11
/*     */       //   Java source line #917	-> byte code offset #14
/*     */       //   Java source line #919	-> byte code offset #24
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	25	0	this	CycleDetectingReentrantReadLock
/*     */       //   14	9	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   0	4	14	finally
/*     */     }
/*     */   }
/*     */   
/*     */   private class CycleDetectingReentrantWriteLock
/*     */     extends ReentrantReadWriteLock.WriteLock
/*     */   {
/*     */     @Weak
/*     */     final CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock;
/*     */     
/*     */     CycleDetectingReentrantWriteLock(CycleDetectingLockFactory.CycleDetectingReentrantReadWriteLock readWriteLock)
/*     */     {
/* 927 */       super();
/* 928 */       this.readWriteLock = readWriteLock;
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void lock()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 1	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:this$0	Lcom/google/common/util/concurrent/CycleDetectingLockFactory;
/*     */       //   4: aload_0
/*     */       //   5: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   8: invokestatic 4	com/google/common/util/concurrent/CycleDetectingLockFactory:access$600	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory;Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   11: aload_0
/*     */       //   12: invokespecial 5	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lock	()V
/*     */       //   15: aload_0
/*     */       //   16: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   19: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   22: goto +13 -> 35
/*     */       //   25: astore_1
/*     */       //   26: aload_0
/*     */       //   27: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   30: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   33: aload_1
/*     */       //   34: athrow
/*     */       //   35: return
/*     */       // Line number table:
/*     */       //   Java source line #933	-> byte code offset #0
/*     */       //   Java source line #935	-> byte code offset #11
/*     */       //   Java source line #937	-> byte code offset #15
/*     */       //   Java source line #938	-> byte code offset #22
/*     */       //   Java source line #937	-> byte code offset #25
/*     */       //   Java source line #939	-> byte code offset #35
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	36	0	this	CycleDetectingReentrantWriteLock
/*     */       //   25	9	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   11	15	25	finally
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void lockInterruptibly()
/*     */       throws InterruptedException
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield 1	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:this$0	Lcom/google/common/util/concurrent/CycleDetectingLockFactory;
/*     */       //   4: aload_0
/*     */       //   5: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   8: invokestatic 4	com/google/common/util/concurrent/CycleDetectingLockFactory:access$600	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory;Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   11: aload_0
/*     */       //   12: invokespecial 7	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:lockInterruptibly	()V
/*     */       //   15: aload_0
/*     */       //   16: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   19: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   22: goto +13 -> 35
/*     */       //   25: astore_1
/*     */       //   26: aload_0
/*     */       //   27: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   30: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   33: aload_1
/*     */       //   34: athrow
/*     */       //   35: return
/*     */       // Line number table:
/*     */       //   Java source line #943	-> byte code offset #0
/*     */       //   Java source line #945	-> byte code offset #11
/*     */       //   Java source line #947	-> byte code offset #15
/*     */       //   Java source line #948	-> byte code offset #22
/*     */       //   Java source line #947	-> byte code offset #25
/*     */       //   Java source line #949	-> byte code offset #35
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	36	0	this	CycleDetectingReentrantWriteLock
/*     */       //   25	9	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   11	15	25	finally
/*     */     }
/*     */     
/*     */     public boolean tryLock()
/*     */     {
/* 953 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 955 */         return super.tryLock();
/*     */       } finally {
/* 957 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean tryLock(long timeout, TimeUnit unit) throws InterruptedException
/*     */     {
/* 963 */       CycleDetectingLockFactory.this.aboutToAcquire(this.readWriteLock);
/*     */       try {
/* 965 */         return super.tryLock(timeout, unit);
/*     */       } finally {
/* 967 */         CycleDetectingLockFactory.lockStateChanged(this.readWriteLock);
/*     */       }
/*     */     }
/*     */     
/*     */     /* Error */
/*     */     public void unlock()
/*     */     {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokespecial 10	java/util/concurrent/locks/ReentrantReadWriteLock$WriteLock:unlock	()V
/*     */       //   4: aload_0
/*     */       //   5: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   8: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   11: goto +13 -> 24
/*     */       //   14: astore_1
/*     */       //   15: aload_0
/*     */       //   16: getfield 3	com/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantWriteLock:readWriteLock	Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingReentrantReadWriteLock;
/*     */       //   19: invokestatic 6	com/google/common/util/concurrent/CycleDetectingLockFactory:access$700	(Lcom/google/common/util/concurrent/CycleDetectingLockFactory$CycleDetectingLock;)V
/*     */       //   22: aload_1
/*     */       //   23: athrow
/*     */       //   24: return
/*     */       // Line number table:
/*     */       //   Java source line #974	-> byte code offset #0
/*     */       //   Java source line #976	-> byte code offset #4
/*     */       //   Java source line #977	-> byte code offset #11
/*     */       //   Java source line #976	-> byte code offset #14
/*     */       //   Java source line #978	-> byte code offset #24
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	25	0	this	CycleDetectingReentrantWriteLock
/*     */       //   14	9	1	localObject	Object
/*     */       // Exception table:
/*     */       //   from	to	target	type
/*     */       //   0	4	14	finally
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\guava-22.0.jar!\com\google\commo\\util\concurrent\CycleDetectingLockFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       0.7.1
 */