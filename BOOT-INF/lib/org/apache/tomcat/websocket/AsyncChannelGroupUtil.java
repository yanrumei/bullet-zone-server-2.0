/*     */ package org.apache.tomcat.websocket;
/*     */ 
/*     */ import java.nio.channels.AsynchronousChannelGroup;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.concurrent.ThreadFactory;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsyncChannelGroupUtil
/*     */ {
/*  40 */   private static final StringManager sm = StringManager.getManager(AsyncChannelGroupUtil.class);
/*     */   
/*  42 */   private static AsynchronousChannelGroup group = null;
/*  43 */   private static int usageCount = 0;
/*  44 */   private static final Object lock = new Object();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static AsynchronousChannelGroup register()
/*     */   {
/*  53 */     synchronized (lock) {
/*  54 */       if (usageCount == 0) {
/*  55 */         group = createAsynchronousChannelGroup();
/*     */       }
/*  57 */       usageCount += 1;
/*  58 */       return group;
/*     */     }
/*     */   }
/*     */   
/*     */   public static void unregister()
/*     */   {
/*  64 */     synchronized (lock) {
/*  65 */       usageCount -= 1;
/*  66 */       if (usageCount == 0) {
/*  67 */         group.shutdown();
/*  68 */         group = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   private static AsynchronousChannelGroup createAsynchronousChannelGroup()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: invokestatic 7	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   3: invokevirtual 8	java/lang/Thread:getContextClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   6: astore_0
/*     */     //   7: invokestatic 7	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   10: ldc 9
/*     */     //   12: invokevirtual 10	java/lang/Class:getClassLoader	()Ljava/lang/ClassLoader;
/*     */     //   15: invokevirtual 11	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   18: invokestatic 12	java/lang/Runtime:getRuntime	()Ljava/lang/Runtime;
/*     */     //   21: invokevirtual 13	java/lang/Runtime:availableProcessors	()I
/*     */     //   24: istore_1
/*     */     //   25: new 14	org/apache/tomcat/util/threads/ThreadPoolExecutor
/*     */     //   28: dup
/*     */     //   29: iconst_0
/*     */     //   30: ldc 16
/*     */     //   32: ldc2_w 18
/*     */     //   35: getstatic 20	java/util/concurrent/TimeUnit:MILLISECONDS	Ljava/util/concurrent/TimeUnit;
/*     */     //   38: new 21	java/util/concurrent/SynchronousQueue
/*     */     //   41: dup
/*     */     //   42: invokespecial 22	java/util/concurrent/SynchronousQueue:<init>	()V
/*     */     //   45: new 9	org/apache/tomcat/websocket/AsyncChannelGroupUtil$AsyncIOThreadFactory
/*     */     //   48: dup
/*     */     //   49: aconst_null
/*     */     //   50: invokespecial 23	org/apache/tomcat/websocket/AsyncChannelGroupUtil$AsyncIOThreadFactory:<init>	(Lorg/apache/tomcat/websocket/AsyncChannelGroupUtil$1;)V
/*     */     //   53: invokespecial 24	org/apache/tomcat/util/threads/ThreadPoolExecutor:<init>	(IIJLjava/util/concurrent/TimeUnit;Ljava/util/concurrent/BlockingQueue;Ljava/util/concurrent/ThreadFactory;)V
/*     */     //   56: astore_2
/*     */     //   57: aload_2
/*     */     //   58: iload_1
/*     */     //   59: invokestatic 25	java/nio/channels/AsynchronousChannelGroup:withCachedThreadPool	(Ljava/util/concurrent/ExecutorService;I)Ljava/nio/channels/AsynchronousChannelGroup;
/*     */     //   62: astore_3
/*     */     //   63: invokestatic 7	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   66: aload_0
/*     */     //   67: invokevirtual 11	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   70: aload_3
/*     */     //   71: areturn
/*     */     //   72: astore_3
/*     */     //   73: new 27	java/lang/IllegalStateException
/*     */     //   76: dup
/*     */     //   77: getstatic 28	org/apache/tomcat/websocket/AsyncChannelGroupUtil:sm	Lorg/apache/tomcat/util/res/StringManager;
/*     */     //   80: ldc 29
/*     */     //   82: invokevirtual 30	org/apache/tomcat/util/res/StringManager:getString	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   85: invokespecial 31	java/lang/IllegalStateException:<init>	(Ljava/lang/String;)V
/*     */     //   88: athrow
/*     */     //   89: astore 4
/*     */     //   91: invokestatic 7	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   94: aload_0
/*     */     //   95: invokevirtual 11	java/lang/Thread:setContextClassLoader	(Ljava/lang/ClassLoader;)V
/*     */     //   98: aload 4
/*     */     //   100: athrow
/*     */     // Line number table:
/*     */     //   Java source line #77	-> byte code offset #0
/*     */     //   Java source line #80	-> byte code offset #7
/*     */     //   Java source line #81	-> byte code offset #12
/*     */     //   Java source line #80	-> byte code offset #15
/*     */     //   Java source line #85	-> byte code offset #18
/*     */     //   Java source line #86	-> byte code offset #25
/*     */     //   Java source line #94	-> byte code offset #57
/*     */     //   Java source line #101	-> byte code offset #63
/*     */     //   Java source line #94	-> byte code offset #70
/*     */     //   Java source line #96	-> byte code offset #72
/*     */     //   Java source line #98	-> byte code offset #73
/*     */     //   Java source line #101	-> byte code offset #89
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   6	89	0	original	ClassLoader
/*     */     //   24	35	1	initialSize	int
/*     */     //   56	2	2	executorService	java.util.concurrent.ExecutorService
/*     */     //   62	9	3	localAsynchronousChannelGroup	AsynchronousChannelGroup
/*     */     //   72	2	3	e	java.io.IOException
/*     */     //   89	10	4	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   57	63	72	java/io/IOException
/*     */     //   7	63	89	finally
/*     */     //   72	91	89	finally
/*     */   }
/*     */   
/*     */   private static class AsyncIOThreadFactory
/*     */     implements ThreadFactory
/*     */   {
/*     */     public Thread newThread(Runnable r)
/*     */     {
/* 122 */       return (Thread)AccessController.doPrivileged(new NewThreadPrivilegedAction(r));
/*     */     }
/*     */     
/*     */     static {}
/*     */     
/*     */     private static class NewThreadPrivilegedAction implements PrivilegedAction<Thread>
/*     */     {
/* 129 */       private static AtomicInteger count = new AtomicInteger(0);
/*     */       private final Runnable r;
/*     */       
/*     */       public NewThreadPrivilegedAction(Runnable r)
/*     */       {
/* 134 */         this.r = r;
/*     */       }
/*     */       
/*     */       public Thread run()
/*     */       {
/* 139 */         Thread t = new Thread(this.r);
/* 140 */         t.setName("WebSocketClient-AsyncIO-" + count.incrementAndGet());
/* 141 */         t.setContextClassLoader(getClass().getClassLoader());
/* 142 */         t.setDaemon(true);
/* 143 */         return t;
/*     */       }
/*     */       
/*     */       private static void load() {}
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\AsyncChannelGroupUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */