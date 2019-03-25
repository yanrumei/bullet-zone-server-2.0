/*     */ package org.springframework.jndi;
/*     */ 
/*     */ import javax.naming.NamingException;
/*     */ import org.springframework.aop.TargetSource;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JndiObjectTargetSource
/*     */   extends JndiObjectLocator
/*     */   implements TargetSource
/*     */ {
/*  63 */   private boolean lookupOnStartup = true;
/*     */   
/*  65 */   private boolean cache = true;
/*     */   
/*     */ 
/*     */ 
/*     */   private Object cachedObject;
/*     */   
/*     */ 
/*     */ 
/*     */   private Class<?> targetClass;
/*     */   
/*     */ 
/*     */ 
/*     */   public void setLookupOnStartup(boolean lookupOnStartup)
/*     */   {
/*  79 */     this.lookupOnStartup = lookupOnStartup;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCache(boolean cache)
/*     */   {
/*  90 */     this.cache = cache;
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet() throws NamingException
/*     */   {
/*  95 */     super.afterPropertiesSet();
/*  96 */     if (this.lookupOnStartup) {
/*  97 */       Object object = lookup();
/*  98 */       if (this.cache) {
/*  99 */         this.cachedObject = object;
/*     */       }
/*     */       else {
/* 102 */         this.targetClass = object.getClass();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?> getTargetClass()
/*     */   {
/* 110 */     if (this.cachedObject != null) {
/* 111 */       return this.cachedObject.getClass();
/*     */     }
/* 113 */     if (this.targetClass != null) {
/* 114 */       return this.targetClass;
/*     */     }
/*     */     
/* 117 */     return getExpectedType();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isStatic()
/*     */   {
/* 123 */     return this.cachedObject != null;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Object getTarget()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 2	org/springframework/jndi/JndiObjectTargetSource:lookupOnStartup	Z
/*     */     //   4: ifne +10 -> 14
/*     */     //   7: aload_0
/*     */     //   8: getfield 3	org/springframework/jndi/JndiObjectTargetSource:cache	Z
/*     */     //   11: ifne +22 -> 33
/*     */     //   14: aload_0
/*     */     //   15: getfield 6	org/springframework/jndi/JndiObjectTargetSource:cachedObject	Ljava/lang/Object;
/*     */     //   18: ifnull +10 -> 28
/*     */     //   21: aload_0
/*     */     //   22: getfield 6	org/springframework/jndi/JndiObjectTargetSource:cachedObject	Ljava/lang/Object;
/*     */     //   25: goto +7 -> 32
/*     */     //   28: aload_0
/*     */     //   29: invokevirtual 5	org/springframework/jndi/JndiObjectTargetSource:lookup	()Ljava/lang/Object;
/*     */     //   32: areturn
/*     */     //   33: aload_0
/*     */     //   34: dup
/*     */     //   35: astore_1
/*     */     //   36: monitorenter
/*     */     //   37: aload_0
/*     */     //   38: getfield 6	org/springframework/jndi/JndiObjectTargetSource:cachedObject	Ljava/lang/Object;
/*     */     //   41: ifnonnull +11 -> 52
/*     */     //   44: aload_0
/*     */     //   45: aload_0
/*     */     //   46: invokevirtual 5	org/springframework/jndi/JndiObjectTargetSource:lookup	()Ljava/lang/Object;
/*     */     //   49: putfield 6	org/springframework/jndi/JndiObjectTargetSource:cachedObject	Ljava/lang/Object;
/*     */     //   52: aload_0
/*     */     //   53: getfield 6	org/springframework/jndi/JndiObjectTargetSource:cachedObject	Ljava/lang/Object;
/*     */     //   56: aload_1
/*     */     //   57: monitorexit
/*     */     //   58: areturn
/*     */     //   59: astore_2
/*     */     //   60: aload_1
/*     */     //   61: monitorexit
/*     */     //   62: aload_2
/*     */     //   63: athrow
/*     */     //   64: astore_1
/*     */     //   65: new 11	org/springframework/jndi/JndiLookupFailureException
/*     */     //   68: dup
/*     */     //   69: ldc 12
/*     */     //   71: aload_1
/*     */     //   72: invokespecial 13	org/springframework/jndi/JndiLookupFailureException:<init>	(Ljava/lang/String;Ljavax/naming/NamingException;)V
/*     */     //   75: athrow
/*     */     // Line number table:
/*     */     //   Java source line #129	-> byte code offset #0
/*     */     //   Java source line #130	-> byte code offset #14
/*     */     //   Java source line #133	-> byte code offset #33
/*     */     //   Java source line #134	-> byte code offset #37
/*     */     //   Java source line #135	-> byte code offset #44
/*     */     //   Java source line #137	-> byte code offset #52
/*     */     //   Java source line #138	-> byte code offset #59
/*     */     //   Java source line #141	-> byte code offset #64
/*     */     //   Java source line #142	-> byte code offset #65
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	76	0	this	JndiObjectTargetSource
/*     */     //   64	8	1	ex	NamingException
/*     */     //   59	4	2	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   37	58	59	finally
/*     */     //   59	62	59	finally
/*     */     //   0	32	64	javax/naming/NamingException
/*     */     //   33	58	64	javax/naming/NamingException
/*     */     //   59	64	64	javax/naming/NamingException
/*     */   }
/*     */   
/*     */   public void releaseTarget(Object target) {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jndi\JndiObjectTargetSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */