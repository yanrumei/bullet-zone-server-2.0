/*     */ package org.springframework.boot;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class SpringBootExceptionHandler
/*     */   implements Thread.UncaughtExceptionHandler
/*     */ {
/*     */   private static Set<String> LOG_CONFIGURATION_MESSAGES;
/*     */   
/*     */   static
/*     */   {
/*  38 */     Set<String> messages = new HashSet();
/*  39 */     messages.add("Logback configuration error detected");
/*  40 */     LOG_CONFIGURATION_MESSAGES = Collections.unmodifiableSet(messages);
/*     */   }
/*     */   
/*  43 */   private static LoggedExceptionHandlerThreadLocal handler = new LoggedExceptionHandlerThreadLocal(null);
/*     */   
/*     */   private final Thread.UncaughtExceptionHandler parent;
/*     */   
/*  47 */   private final List<Throwable> loggedExceptions = new ArrayList();
/*     */   
/*  49 */   private int exitCode = 0;
/*     */   
/*     */   SpringBootExceptionHandler(Thread.UncaughtExceptionHandler parent) {
/*  52 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public void registerLoggedException(Throwable exception) {
/*  56 */     this.loggedExceptions.add(exception);
/*     */   }
/*     */   
/*     */   public void registerExitCode(int exitCode) {
/*  60 */     this.exitCode = exitCode;
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
/*     */   private boolean isPassedToParent(Throwable ex)
/*     */   {
/*  79 */     return (isLogConfigurationMessage(ex)) || (!isRegistered(ex));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isLogConfigurationMessage(Throwable ex)
/*     */   {
/*  89 */     String message = ex.getMessage();
/*  90 */     if (message != null) {
/*  91 */       for (String candidate : LOG_CONFIGURATION_MESSAGES) {
/*  92 */         if (message.contains(candidate)) {
/*  93 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*  97 */     return false;
/*     */   }
/*     */   
/*     */   private boolean isRegistered(Throwable ex) {
/* 101 */     if (this.loggedExceptions.contains(ex)) {
/* 102 */       return true;
/*     */     }
/* 104 */     if ((ex instanceof InvocationTargetException)) {
/* 105 */       return isRegistered(ex.getCause());
/*     */     }
/* 107 */     return false;
/*     */   }
/*     */   
/*     */   static SpringBootExceptionHandler forCurrentThread() {
/* 111 */     return (SpringBootExceptionHandler)handler.get();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void uncaughtException(Thread thread, Throwable ex)
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_2
/*     */     //   2: invokespecial 8	org/springframework/boot/SpringBootExceptionHandler:isPassedToParent	(Ljava/lang/Throwable;)Z
/*     */     //   5: ifeq +21 -> 26
/*     */     //   8: aload_0
/*     */     //   9: getfield 6	org/springframework/boot/SpringBootExceptionHandler:parent	Ljava/lang/Thread$UncaughtExceptionHandler;
/*     */     //   12: ifnull +14 -> 26
/*     */     //   15: aload_0
/*     */     //   16: getfield 6	org/springframework/boot/SpringBootExceptionHandler:parent	Ljava/lang/Thread$UncaughtExceptionHandler;
/*     */     //   19: aload_1
/*     */     //   20: aload_2
/*     */     //   21: invokeinterface 9 3 0
/*     */     //   26: aload_0
/*     */     //   27: getfield 4	org/springframework/boot/SpringBootExceptionHandler:loggedExceptions	Ljava/util/List;
/*     */     //   30: invokeinterface 10 1 0
/*     */     //   35: aload_0
/*     */     //   36: getfield 5	org/springframework/boot/SpringBootExceptionHandler:exitCode	I
/*     */     //   39: ifeq +39 -> 78
/*     */     //   42: aload_0
/*     */     //   43: getfield 5	org/springframework/boot/SpringBootExceptionHandler:exitCode	I
/*     */     //   46: invokestatic 11	java/lang/System:exit	(I)V
/*     */     //   49: goto +29 -> 78
/*     */     //   52: astore_3
/*     */     //   53: aload_0
/*     */     //   54: getfield 4	org/springframework/boot/SpringBootExceptionHandler:loggedExceptions	Ljava/util/List;
/*     */     //   57: invokeinterface 10 1 0
/*     */     //   62: aload_0
/*     */     //   63: getfield 5	org/springframework/boot/SpringBootExceptionHandler:exitCode	I
/*     */     //   66: ifeq +10 -> 76
/*     */     //   69: aload_0
/*     */     //   70: getfield 5	org/springframework/boot/SpringBootExceptionHandler:exitCode	I
/*     */     //   73: invokestatic 11	java/lang/System:exit	(I)V
/*     */     //   76: aload_3
/*     */     //   77: athrow
/*     */     //   78: return
/*     */     // Line number table:
/*     */     //   Java source line #66	-> byte code offset #0
/*     */     //   Java source line #67	-> byte code offset #15
/*     */     //   Java source line #71	-> byte code offset #26
/*     */     //   Java source line #72	-> byte code offset #35
/*     */     //   Java source line #73	-> byte code offset #42
/*     */     //   Java source line #71	-> byte code offset #52
/*     */     //   Java source line #72	-> byte code offset #62
/*     */     //   Java source line #73	-> byte code offset #69
/*     */     //   Java source line #76	-> byte code offset #78
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	79	0	this	SpringBootExceptionHandler
/*     */     //   0	79	1	thread	Thread
/*     */     //   0	79	2	ex	Throwable
/*     */     //   52	25	3	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   0	26	52	finally
/*     */   }
/*     */   
/*     */   private static class LoggedExceptionHandlerThreadLocal
/*     */     extends ThreadLocal<SpringBootExceptionHandler>
/*     */   {
/*     */     protected SpringBootExceptionHandler initialValue()
/*     */     {
/* 123 */       SpringBootExceptionHandler handler = new SpringBootExceptionHandler(Thread.currentThread().getUncaughtExceptionHandler());
/* 124 */       Thread.currentThread().setUncaughtExceptionHandler(handler);
/* 125 */       return handler;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\SpringBootExceptionHandler.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */