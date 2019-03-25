/*     */ package org.springframework.aop.target;
/*     */ 
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.aop.IntroductionAdvisor;
/*     */ import org.springframework.aop.support.DefaultIntroductionAdvisor;
/*     */ import org.springframework.aop.support.DelegatingIntroductionInterceptor;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.core.NamedThreadLocal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ThreadLocalTargetSource
/*     */   extends AbstractPrototypeBasedTargetSource
/*     */   implements ThreadLocalTargetSourceStats, DisposableBean
/*     */ {
/*  60 */   private final ThreadLocal<Object> targetInThread = new NamedThreadLocal("Thread-local instance of bean '" + 
/*  61 */     getTargetBeanName() + "'");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  66 */   private final Set<Object> targetSet = new HashSet();
/*     */   
/*     */ 
/*     */ 
/*     */   private int invocationCount;
/*     */   
/*     */ 
/*     */   private int hitCount;
/*     */   
/*     */ 
/*     */ 
/*     */   public Object getTarget()
/*     */     throws BeansException
/*     */   {
/*  80 */     this.invocationCount += 1;
/*  81 */     Object target = this.targetInThread.get();
/*  82 */     if (target == null) {
/*  83 */       if (this.logger.isDebugEnabled()) {
/*  84 */         this.logger.debug("No target for prototype '" + getTargetBeanName() + "' bound to thread: creating one and binding it to thread '" + 
/*  85 */           Thread.currentThread().getName() + "'");
/*     */       }
/*     */       
/*  88 */       target = newPrototypeInstance();
/*  89 */       this.targetInThread.set(target);
/*  90 */       synchronized (this.targetSet) {
/*  91 */         this.targetSet.add(target);
/*     */       }
/*     */     }
/*     */     else {
/*  95 */       this.hitCount += 1;
/*     */     }
/*  97 */     return target;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 106 */     this.logger.debug("Destroying ThreadLocalTargetSource bindings");
/* 107 */     synchronized (this.targetSet) {
/* 108 */       for (Object target : this.targetSet) {
/* 109 */         destroyPrototypeInstance(target);
/*     */       }
/* 111 */       this.targetSet.clear();
/*     */     }
/*     */     
/* 114 */     this.targetInThread.remove();
/*     */   }
/*     */   
/*     */ 
/*     */   public int getInvocationCount()
/*     */   {
/* 120 */     return this.invocationCount;
/*     */   }
/*     */   
/*     */   public int getHitCount()
/*     */   {
/* 125 */     return this.hitCount;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public int getObjectCount()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 14	org/springframework/aop/target/ThreadLocalTargetSource:targetSet	Ljava/util/Set;
/*     */     //   4: dup
/*     */     //   5: astore_1
/*     */     //   6: monitorenter
/*     */     //   7: aload_0
/*     */     //   8: getfield 14	org/springframework/aop/target/ThreadLocalTargetSource:targetSet	Ljava/util/Set;
/*     */     //   11: invokeinterface 35 1 0
/*     */     //   16: aload_1
/*     */     //   17: monitorexit
/*     */     //   18: ireturn
/*     */     //   19: astore_2
/*     */     //   20: aload_1
/*     */     //   21: monitorexit
/*     */     //   22: aload_2
/*     */     //   23: athrow
/*     */     // Line number table:
/*     */     //   Java source line #130	-> byte code offset #0
/*     */     //   Java source line #131	-> byte code offset #7
/*     */     //   Java source line #132	-> byte code offset #19
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	24	0	this	ThreadLocalTargetSource
/*     */     //   5	16	1	Ljava/lang/Object;	Object
/*     */     //   19	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   7	18	19	finally
/*     */     //   19	22	19	finally
/*     */   }
/*     */   
/*     */   public IntroductionAdvisor getStatsMixin()
/*     */   {
/* 141 */     DelegatingIntroductionInterceptor dii = new DelegatingIntroductionInterceptor(this);
/* 142 */     return new DefaultIntroductionAdvisor(dii, ThreadLocalTargetSourceStats.class);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\target\ThreadLocalTargetSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */