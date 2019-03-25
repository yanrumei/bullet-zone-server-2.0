/*     */ package org.springframework.beans.factory.support;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import org.springframework.beans.BeanInstantiationException;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleInstantiationStrategy
/*     */   implements InstantiationStrategy
/*     */ {
/*  45 */   private static final ThreadLocal<Method> currentlyInvokedFactoryMethod = new ThreadLocal();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Method getCurrentlyInvokedFactoryMethod()
/*     */   {
/*  54 */     return (Method)currentlyInvokedFactoryMethod.get();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner)
/*     */   {
/*  61 */     if (bd.getMethodOverrides().isEmpty())
/*     */     {
/*  63 */       synchronized (bd.constructorArgumentLock) {
/*  64 */         Constructor<?> constructorToUse = (Constructor)bd.resolvedConstructorOrFactoryMethod;
/*  65 */         if (constructorToUse == null) {
/*  66 */           final Class<?> clazz = bd.getBeanClass();
/*  67 */           if (clazz.isInterface()) {
/*  68 */             throw new BeanInstantiationException(clazz, "Specified class is an interface");
/*     */           }
/*     */           try {
/*  71 */             if (System.getSecurityManager() != null) {
/*  72 */               constructorToUse = (Constructor)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */               {
/*     */                 public Constructor<?> run() throws Exception {
/*  75 */                   return clazz.getDeclaredConstructor((Class[])null);
/*     */                 }
/*     */                 
/*     */               });
/*     */             } else {
/*  80 */               constructorToUse = clazz.getDeclaredConstructor((Class[])null);
/*     */             }
/*  82 */             bd.resolvedConstructorOrFactoryMethod = constructorToUse;
/*     */           }
/*     */           catch (Throwable ex) {
/*  85 */             throw new BeanInstantiationException(clazz, "No default constructor found", ex);
/*     */           }
/*     */         } }
/*     */       Constructor<?> constructorToUse;
/*  89 */       return BeanUtils.instantiateClass(constructorToUse, new Object[0]);
/*     */     }
/*     */     
/*     */ 
/*  93 */     return instantiateWithMethodInjection(bd, beanName, owner);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object instantiateWithMethodInjection(RootBeanDefinition bd, String beanName, BeanFactory owner)
/*     */   {
/* 104 */     throw new UnsupportedOperationException("Method Injection not supported in SimpleInstantiationStrategy");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner, final Constructor<?> ctor, Object... args)
/*     */   {
/* 111 */     if (bd.getMethodOverrides().isEmpty()) {
/* 112 */       if (System.getSecurityManager() != null)
/*     */       {
/* 114 */         AccessController.doPrivileged(new PrivilegedAction()
/*     */         {
/*     */           public Object run() {
/* 117 */             ReflectionUtils.makeAccessible(ctor);
/* 118 */             return null;
/*     */           }
/*     */         });
/*     */       }
/* 122 */       return BeanUtils.instantiateClass(ctor, args);
/*     */     }
/*     */     
/* 125 */     return instantiateWithMethodInjection(bd, beanName, owner, ctor, args);
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
/*     */   protected Object instantiateWithMethodInjection(RootBeanDefinition bd, String beanName, BeanFactory owner, Constructor<?> ctor, Object... args)
/*     */   {
/* 138 */     throw new UnsupportedOperationException("Method Injection not supported in SimpleInstantiationStrategy");
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public Object instantiate(RootBeanDefinition bd, String beanName, BeanFactory owner, Object factoryBean, final Method factoryMethod, Object... args)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: invokestatic 15	java/lang/System:getSecurityManager	()Ljava/lang/SecurityManager;
/*     */     //   3: ifnull +20 -> 23
/*     */     //   6: new 34	org/springframework/beans/factory/support/SimpleInstantiationStrategy$3
/*     */     //   9: dup
/*     */     //   10: aload_0
/*     */     //   11: aload 5
/*     */     //   13: invokespecial 35	org/springframework/beans/factory/support/SimpleInstantiationStrategy$3:<init>	(Lorg/springframework/beans/factory/support/SimpleInstantiationStrategy;Ljava/lang/reflect/Method;)V
/*     */     //   16: invokestatic 32	java/security/AccessController:doPrivileged	(Ljava/security/PrivilegedAction;)Ljava/lang/Object;
/*     */     //   19: pop
/*     */     //   20: goto +8 -> 28
/*     */     //   23: aload 5
/*     */     //   25: invokestatic 36	org/springframework/util/ReflectionUtils:makeAccessible	(Ljava/lang/reflect/Method;)V
/*     */     //   28: getstatic 2	org/springframework/beans/factory/support/SimpleInstantiationStrategy:currentlyInvokedFactoryMethod	Ljava/lang/ThreadLocal;
/*     */     //   31: invokevirtual 3	java/lang/ThreadLocal:get	()Ljava/lang/Object;
/*     */     //   34: checkcast 4	java/lang/reflect/Method
/*     */     //   37: astore 7
/*     */     //   39: getstatic 2	org/springframework/beans/factory/support/SimpleInstantiationStrategy:currentlyInvokedFactoryMethod	Ljava/lang/ThreadLocal;
/*     */     //   42: aload 5
/*     */     //   44: invokevirtual 37	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/*     */     //   47: aload 5
/*     */     //   49: aload 4
/*     */     //   51: aload 6
/*     */     //   53: invokevirtual 38	java/lang/reflect/Method:invoke	(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   56: astore 8
/*     */     //   58: aload 7
/*     */     //   60: ifnull +14 -> 74
/*     */     //   63: getstatic 2	org/springframework/beans/factory/support/SimpleInstantiationStrategy:currentlyInvokedFactoryMethod	Ljava/lang/ThreadLocal;
/*     */     //   66: aload 7
/*     */     //   68: invokevirtual 37	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/*     */     //   71: goto +9 -> 80
/*     */     //   74: getstatic 2	org/springframework/beans/factory/support/SimpleInstantiationStrategy:currentlyInvokedFactoryMethod	Ljava/lang/ThreadLocal;
/*     */     //   77: invokevirtual 39	java/lang/ThreadLocal:remove	()V
/*     */     //   80: aload 8
/*     */     //   82: areturn
/*     */     //   83: astore 9
/*     */     //   85: aload 7
/*     */     //   87: ifnull +14 -> 101
/*     */     //   90: getstatic 2	org/springframework/beans/factory/support/SimpleInstantiationStrategy:currentlyInvokedFactoryMethod	Ljava/lang/ThreadLocal;
/*     */     //   93: aload 7
/*     */     //   95: invokevirtual 37	java/lang/ThreadLocal:set	(Ljava/lang/Object;)V
/*     */     //   98: goto +9 -> 107
/*     */     //   101: getstatic 2	org/springframework/beans/factory/support/SimpleInstantiationStrategy:currentlyInvokedFactoryMethod	Ljava/lang/ThreadLocal;
/*     */     //   104: invokevirtual 39	java/lang/ThreadLocal:remove	()V
/*     */     //   107: aload 9
/*     */     //   109: athrow
/*     */     //   110: astore 7
/*     */     //   112: new 12	org/springframework/beans/BeanInstantiationException
/*     */     //   115: dup
/*     */     //   116: aload 5
/*     */     //   118: new 41	java/lang/StringBuilder
/*     */     //   121: dup
/*     */     //   122: invokespecial 42	java/lang/StringBuilder:<init>	()V
/*     */     //   125: ldc 43
/*     */     //   127: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   130: aload 5
/*     */     //   132: invokevirtual 45	java/lang/reflect/Method:getName	()Ljava/lang/String;
/*     */     //   135: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   138: ldc 46
/*     */     //   140: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   143: aload 6
/*     */     //   145: invokestatic 47	org/springframework/util/StringUtils:arrayToCommaDelimitedString	([Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   148: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   151: invokevirtual 48	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   154: aload 7
/*     */     //   156: invokespecial 49	org/springframework/beans/BeanInstantiationException:<init>	(Ljava/lang/reflect/Method;Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   159: athrow
/*     */     //   160: astore 7
/*     */     //   162: new 12	org/springframework/beans/BeanInstantiationException
/*     */     //   165: dup
/*     */     //   166: aload 5
/*     */     //   168: new 41	java/lang/StringBuilder
/*     */     //   171: dup
/*     */     //   172: invokespecial 42	java/lang/StringBuilder:<init>	()V
/*     */     //   175: ldc 51
/*     */     //   177: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   180: aload 5
/*     */     //   182: invokevirtual 45	java/lang/reflect/Method:getName	()Ljava/lang/String;
/*     */     //   185: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   188: ldc 52
/*     */     //   190: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   193: invokevirtual 48	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   196: aload 7
/*     */     //   198: invokespecial 49	org/springframework/beans/BeanInstantiationException:<init>	(Ljava/lang/reflect/Method;Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   201: athrow
/*     */     //   202: astore 7
/*     */     //   204: new 41	java/lang/StringBuilder
/*     */     //   207: dup
/*     */     //   208: invokespecial 42	java/lang/StringBuilder:<init>	()V
/*     */     //   211: ldc 54
/*     */     //   213: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   216: aload 5
/*     */     //   218: invokevirtual 45	java/lang/reflect/Method:getName	()Ljava/lang/String;
/*     */     //   221: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   224: ldc 55
/*     */     //   226: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   229: invokevirtual 48	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   232: astore 8
/*     */     //   234: aload_1
/*     */     //   235: invokevirtual 56	org/springframework/beans/factory/support/RootBeanDefinition:getFactoryBeanName	()Ljava/lang/String;
/*     */     //   238: ifnull +60 -> 298
/*     */     //   241: aload_3
/*     */     //   242: instanceof 57
/*     */     //   245: ifeq +53 -> 298
/*     */     //   248: aload_3
/*     */     //   249: checkcast 57	org/springframework/beans/factory/config/ConfigurableBeanFactory
/*     */     //   252: aload_1
/*     */     //   253: invokevirtual 56	org/springframework/beans/factory/support/RootBeanDefinition:getFactoryBeanName	()Ljava/lang/String;
/*     */     //   256: invokeinterface 58 2 0
/*     */     //   261: ifeq +37 -> 298
/*     */     //   264: new 41	java/lang/StringBuilder
/*     */     //   267: dup
/*     */     //   268: invokespecial 42	java/lang/StringBuilder:<init>	()V
/*     */     //   271: ldc 59
/*     */     //   273: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   276: aload_1
/*     */     //   277: invokevirtual 56	org/springframework/beans/factory/support/RootBeanDefinition:getFactoryBeanName	()Ljava/lang/String;
/*     */     //   280: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   283: ldc 60
/*     */     //   285: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   288: aload 8
/*     */     //   290: invokevirtual 44	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   293: invokevirtual 48	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   296: astore 8
/*     */     //   298: new 12	org/springframework/beans/BeanInstantiationException
/*     */     //   301: dup
/*     */     //   302: aload 5
/*     */     //   304: aload 8
/*     */     //   306: aload 7
/*     */     //   308: invokevirtual 61	java/lang/reflect/InvocationTargetException:getTargetException	()Ljava/lang/Throwable;
/*     */     //   311: invokespecial 49	org/springframework/beans/BeanInstantiationException:<init>	(Ljava/lang/reflect/Method;Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   314: athrow
/*     */     // Line number table:
/*     */     //   Java source line #146	-> byte code offset #0
/*     */     //   Java source line #147	-> byte code offset #6
/*     */     //   Java source line #156	-> byte code offset #23
/*     */     //   Java source line #159	-> byte code offset #28
/*     */     //   Java source line #161	-> byte code offset #39
/*     */     //   Java source line #162	-> byte code offset #47
/*     */     //   Java source line #165	-> byte code offset #58
/*     */     //   Java source line #166	-> byte code offset #63
/*     */     //   Java source line #169	-> byte code offset #74
/*     */     //   Java source line #162	-> byte code offset #80
/*     */     //   Java source line #165	-> byte code offset #83
/*     */     //   Java source line #166	-> byte code offset #90
/*     */     //   Java source line #169	-> byte code offset #101
/*     */     //   Java source line #173	-> byte code offset #110
/*     */     //   Java source line #174	-> byte code offset #112
/*     */     //   Java source line #175	-> byte code offset #132
/*     */     //   Java source line #176	-> byte code offset #145
/*     */     //   Java source line #178	-> byte code offset #160
/*     */     //   Java source line #179	-> byte code offset #162
/*     */     //   Java source line #180	-> byte code offset #182
/*     */     //   Java source line #182	-> byte code offset #202
/*     */     //   Java source line #183	-> byte code offset #204
/*     */     //   Java source line #184	-> byte code offset #234
/*     */     //   Java source line #185	-> byte code offset #253
/*     */     //   Java source line #186	-> byte code offset #264
/*     */     //   Java source line #189	-> byte code offset #298
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	315	0	this	SimpleInstantiationStrategy
/*     */     //   0	315	1	bd	RootBeanDefinition
/*     */     //   0	315	2	beanName	String
/*     */     //   0	315	3	owner	BeanFactory
/*     */     //   0	315	4	factoryBean	Object
/*     */     //   0	315	5	factoryMethod	Method
/*     */     //   0	315	6	args	Object[]
/*     */     //   37	57	7	priorInvokedFactoryMethod	Method
/*     */     //   110	45	7	ex	IllegalArgumentException
/*     */     //   160	37	7	ex	IllegalAccessException
/*     */     //   202	105	7	ex	java.lang.reflect.InvocationTargetException
/*     */     //   232	73	8	msg	String
/*     */     //   83	25	9	localObject2	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   39	58	83	finally
/*     */     //   83	85	83	finally
/*     */     //   0	80	110	java/lang/IllegalArgumentException
/*     */     //   83	110	110	java/lang/IllegalArgumentException
/*     */     //   0	80	160	java/lang/IllegalAccessException
/*     */     //   83	110	160	java/lang/IllegalAccessException
/*     */     //   0	80	202	java/lang/reflect/InvocationTargetException
/*     */     //   83	110	202	java/lang/reflect/InvocationTargetException
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\support\SimpleInstantiationStrategy.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */