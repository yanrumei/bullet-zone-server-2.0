/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.Random;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Server;
/*     */ import org.apache.catalina.Service;
/*     */ import org.apache.catalina.deploy.NamingResourcesImpl;
/*     */ import org.apache.catalina.mbeans.MBeanFactory;
/*     */ import org.apache.catalina.startup.Catalina;
/*     */ import org.apache.catalina.util.ExtensionValidator;
/*     */ import org.apache.catalina.util.LifecycleMBeanBase;
/*     */ import org.apache.catalina.util.ServerInfo;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.ExceptionUtils;
/*     */ import org.apache.tomcat.util.buf.StringCache;
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
/*     */ public final class StandardServer
/*     */   extends LifecycleMBeanBase
/*     */   implements Server
/*     */ {
/*  64 */   private static final Log log = LogFactory.getLog(StandardServer.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StandardServer()
/*     */   {
/*  77 */     this.globalNamingResources = new NamingResourcesImpl();
/*  78 */     this.globalNamingResources.setContainer(this);
/*     */     
/*  80 */     if (isUseNaming()) {
/*  81 */       this.namingContextListener = new NamingContextListener();
/*  82 */       addLifecycleListener(this.namingContextListener);
/*     */     } else {
/*  84 */       this.namingContextListener = null;
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
/*  96 */   private javax.naming.Context globalNamingContext = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */   private NamingResourcesImpl globalNamingResources = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final NamingContextListener namingContextListener;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 114 */   private int port = 8005;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 119 */   private String address = "localhost";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 126 */   private Random random = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 132 */   private Service[] services = new Service[0];
/* 133 */   private final Object servicesLock = new Object();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 139 */   private String shutdown = "SHUTDOWN";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 146 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 152 */   final PropertyChangeSupport support = new PropertyChangeSupport(this);
/*     */   
/* 154 */   private volatile boolean stopAwait = false;
/*     */   
/* 156 */   private Catalina catalina = null;
/*     */   
/* 158 */   private ClassLoader parentClassLoader = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 163 */   private volatile Thread awaitThread = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 168 */   private volatile ServerSocket awaitSocket = null;
/*     */   
/* 170 */   private File catalinaHome = null;
/*     */   
/* 172 */   private File catalinaBase = null;
/*     */   
/* 174 */   private final Object namingToken = new Object();
/*     */   
/*     */   private ObjectName onameStringCache;
/*     */   private ObjectName onameMBeanFactory;
/*     */   
/*     */   public Object getNamingToken()
/*     */   {
/* 181 */     return this.namingToken;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public javax.naming.Context getGlobalNamingContext()
/*     */   {
/* 191 */     return this.globalNamingContext;
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
/*     */   public void setGlobalNamingContext(javax.naming.Context globalNamingContext)
/*     */   {
/* 204 */     this.globalNamingContext = globalNamingContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public NamingResourcesImpl getGlobalNamingResources()
/*     */   {
/* 215 */     return this.globalNamingResources;
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
/*     */   public void setGlobalNamingResources(NamingResourcesImpl globalNamingResources)
/*     */   {
/* 229 */     NamingResourcesImpl oldGlobalNamingResources = this.globalNamingResources;
/*     */     
/* 231 */     this.globalNamingResources = globalNamingResources;
/* 232 */     this.globalNamingResources.setContainer(this);
/* 233 */     this.support.firePropertyChange("globalNamingResources", oldGlobalNamingResources, this.globalNamingResources);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getServerInfo()
/*     */   {
/* 245 */     return ServerInfo.getServerInfo();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getServerBuilt()
/*     */   {
/* 254 */     return ServerInfo.getServerBuilt();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getServerNumber()
/*     */   {
/* 263 */     return ServerInfo.getServerNumber();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 273 */     return this.port;
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
/*     */   public void setPort(int port)
/*     */   {
/* 286 */     this.port = port;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getAddress()
/*     */   {
/* 297 */     return this.address;
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
/*     */   public void setAddress(String address)
/*     */   {
/* 310 */     this.address = address;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getShutdown()
/*     */   {
/* 320 */     return this.shutdown;
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
/*     */   public void setShutdown(String shutdown)
/*     */   {
/* 333 */     this.shutdown = shutdown;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Catalina getCatalina()
/*     */   {
/* 343 */     return this.catalina;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCatalina(Catalina catalina)
/*     */   {
/* 352 */     this.catalina = catalina;
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
/*     */   public void addService(Service service)
/*     */   {
/* 366 */     service.setServer(this);
/*     */     
/* 368 */     synchronized (this.servicesLock) {
/* 369 */       Service[] results = new Service[this.services.length + 1];
/* 370 */       System.arraycopy(this.services, 0, results, 0, this.services.length);
/* 371 */       results[this.services.length] = service;
/* 372 */       this.services = results;
/*     */       
/* 374 */       if (getState().isAvailable()) {
/*     */         try {
/* 376 */           service.start();
/*     */         }
/*     */         catch (LifecycleException localLifecycleException) {}
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 383 */       this.support.firePropertyChange("service", null, service);
/*     */     }
/*     */   }
/*     */   
/*     */   public void stopAwait()
/*     */   {
/* 389 */     this.stopAwait = true;
/* 390 */     Thread t = this.awaitThread;
/* 391 */     if (t != null) {
/* 392 */       ServerSocket s = this.awaitSocket;
/* 393 */       if (s != null) {
/* 394 */         this.awaitSocket = null;
/*     */         try {
/* 396 */           s.close();
/*     */         }
/*     */         catch (IOException localIOException) {}
/*     */       }
/*     */       
/* 401 */       t.interrupt();
/*     */       try {
/* 403 */         t.join(1000L);
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {}
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void await()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 4	org/apache/catalina/core/StandardServer:port	I
/*     */     //   4: bipush -2
/*     */     //   6: if_icmpne +4 -> 10
/*     */     //   9: return
/*     */     //   10: aload_0
/*     */     //   11: getfield 4	org/apache/catalina/core/StandardServer:port	I
/*     */     //   14: iconst_m1
/*     */     //   15: if_icmpne +47 -> 62
/*     */     //   18: aload_0
/*     */     //   19: invokestatic 53	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   22: putfield 21	org/apache/catalina/core/StandardServer:awaitThread	Ljava/lang/Thread;
/*     */     //   25: aload_0
/*     */     //   26: getfield 18	org/apache/catalina/core/StandardServer:stopAwait	Z
/*     */     //   29: ifne +16 -> 45
/*     */     //   32: ldc2_w 54
/*     */     //   35: invokestatic 56	java/lang/Thread:sleep	(J)V
/*     */     //   38: goto -13 -> 25
/*     */     //   41: astore_1
/*     */     //   42: goto -17 -> 25
/*     */     //   45: aload_0
/*     */     //   46: aconst_null
/*     */     //   47: putfield 21	org/apache/catalina/core/StandardServer:awaitThread	Ljava/lang/Thread;
/*     */     //   50: goto +11 -> 61
/*     */     //   53: astore_2
/*     */     //   54: aload_0
/*     */     //   55: aconst_null
/*     */     //   56: putfield 21	org/apache/catalina/core/StandardServer:awaitThread	Ljava/lang/Thread;
/*     */     //   59: aload_2
/*     */     //   60: athrow
/*     */     //   61: return
/*     */     //   62: aload_0
/*     */     //   63: new 57	java/net/ServerSocket
/*     */     //   66: dup
/*     */     //   67: aload_0
/*     */     //   68: getfield 4	org/apache/catalina/core/StandardServer:port	I
/*     */     //   71: iconst_1
/*     */     //   72: aload_0
/*     */     //   73: getfield 6	org/apache/catalina/core/StandardServer:address	Ljava/lang/String;
/*     */     //   76: invokestatic 58	java/net/InetAddress:getByName	(Ljava/lang/String;)Ljava/net/InetAddress;
/*     */     //   79: invokespecial 59	java/net/ServerSocket:<init>	(IILjava/net/InetAddress;)V
/*     */     //   82: putfield 22	org/apache/catalina/core/StandardServer:awaitSocket	Ljava/net/ServerSocket;
/*     */     //   85: goto +53 -> 138
/*     */     //   88: astore_1
/*     */     //   89: getstatic 60	org/apache/catalina/core/StandardServer:log	Lorg/apache/juli/logging/Log;
/*     */     //   92: new 61	java/lang/StringBuilder
/*     */     //   95: dup
/*     */     //   96: invokespecial 62	java/lang/StringBuilder:<init>	()V
/*     */     //   99: ldc 63
/*     */     //   101: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   104: aload_0
/*     */     //   105: getfield 6	org/apache/catalina/core/StandardServer:address	Ljava/lang/String;
/*     */     //   108: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   111: ldc 65
/*     */     //   113: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   116: aload_0
/*     */     //   117: getfield 4	org/apache/catalina/core/StandardServer:port	I
/*     */     //   120: invokevirtual 66	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */     //   123: ldc 67
/*     */     //   125: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   128: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   131: aload_1
/*     */     //   132: invokeinterface 69 3 0
/*     */     //   137: return
/*     */     //   138: aload_0
/*     */     //   139: invokestatic 53	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   142: putfield 21	org/apache/catalina/core/StandardServer:awaitThread	Ljava/lang/Thread;
/*     */     //   145: aload_0
/*     */     //   146: getfield 18	org/apache/catalina/core/StandardServer:stopAwait	Z
/*     */     //   149: ifne +436 -> 585
/*     */     //   152: aload_0
/*     */     //   153: getfield 22	org/apache/catalina/core/StandardServer:awaitSocket	Ljava/net/ServerSocket;
/*     */     //   156: astore_1
/*     */     //   157: aload_1
/*     */     //   158: ifnonnull +6 -> 164
/*     */     //   161: goto +424 -> 585
/*     */     //   164: aconst_null
/*     */     //   165: astore_2
/*     */     //   166: new 61	java/lang/StringBuilder
/*     */     //   169: dup
/*     */     //   170: invokespecial 62	java/lang/StringBuilder:<init>	()V
/*     */     //   173: astore_3
/*     */     //   174: invokestatic 70	java/lang/System:currentTimeMillis	()J
/*     */     //   177: lstore 5
/*     */     //   179: aload_1
/*     */     //   180: invokevirtual 71	java/net/ServerSocket:accept	()Ljava/net/Socket;
/*     */     //   183: astore_2
/*     */     //   184: aload_2
/*     */     //   185: sipush 10000
/*     */     //   188: invokevirtual 72	java/net/Socket:setSoTimeout	(I)V
/*     */     //   191: aload_2
/*     */     //   192: invokevirtual 73	java/net/Socket:getInputStream	()Ljava/io/InputStream;
/*     */     //   195: astore 4
/*     */     //   197: goto +159 -> 356
/*     */     //   200: astore 7
/*     */     //   202: getstatic 60	org/apache/catalina/core/StandardServer:log	Lorg/apache/juli/logging/Log;
/*     */     //   205: getstatic 75	org/apache/catalina/core/StandardServer:sm	Lorg/apache/tomcat/util/res/StringManager;
/*     */     //   208: ldc 76
/*     */     //   210: iconst_1
/*     */     //   211: anewarray 10	java/lang/Object
/*     */     //   214: dup
/*     */     //   215: iconst_0
/*     */     //   216: invokestatic 70	java/lang/System:currentTimeMillis	()J
/*     */     //   219: lload 5
/*     */     //   221: lsub
/*     */     //   222: invokestatic 77	java/lang/Long:valueOf	(J)Ljava/lang/Long;
/*     */     //   225: aastore
/*     */     //   226: invokevirtual 78	org/apache/tomcat/util/res/StringManager:getString	(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
/*     */     //   229: aload 7
/*     */     //   231: invokeinterface 79 3 0
/*     */     //   236: aload_2
/*     */     //   237: ifnull +7 -> 244
/*     */     //   240: aload_2
/*     */     //   241: invokevirtual 80	java/net/Socket:close	()V
/*     */     //   244: goto -99 -> 145
/*     */     //   247: astore 8
/*     */     //   249: goto -104 -> 145
/*     */     //   252: astore 7
/*     */     //   254: getstatic 60	org/apache/catalina/core/StandardServer:log	Lorg/apache/juli/logging/Log;
/*     */     //   257: new 61	java/lang/StringBuilder
/*     */     //   260: dup
/*     */     //   261: invokespecial 62	java/lang/StringBuilder:<init>	()V
/*     */     //   264: ldc 82
/*     */     //   266: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   269: aload 7
/*     */     //   271: invokevirtual 83	java/security/AccessControlException:getMessage	()Ljava/lang/String;
/*     */     //   274: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   277: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   280: aload 7
/*     */     //   282: invokeinterface 79 3 0
/*     */     //   287: aload_2
/*     */     //   288: ifnull +7 -> 295
/*     */     //   291: aload_2
/*     */     //   292: invokevirtual 80	java/net/Socket:close	()V
/*     */     //   295: goto -150 -> 145
/*     */     //   298: astore 8
/*     */     //   300: goto -155 -> 145
/*     */     //   303: astore 7
/*     */     //   305: aload_0
/*     */     //   306: getfield 18	org/apache/catalina/core/StandardServer:stopAwait	Z
/*     */     //   309: ifeq +19 -> 328
/*     */     //   312: aload_2
/*     */     //   313: ifnull +7 -> 320
/*     */     //   316: aload_2
/*     */     //   317: invokevirtual 80	java/net/Socket:close	()V
/*     */     //   320: goto +265 -> 585
/*     */     //   323: astore 8
/*     */     //   325: goto +260 -> 585
/*     */     //   328: getstatic 60	org/apache/catalina/core/StandardServer:log	Lorg/apache/juli/logging/Log;
/*     */     //   331: ldc 84
/*     */     //   333: aload 7
/*     */     //   335: invokeinterface 69 3 0
/*     */     //   340: aload_2
/*     */     //   341: ifnull +7 -> 348
/*     */     //   344: aload_2
/*     */     //   345: invokevirtual 80	java/net/Socket:close	()V
/*     */     //   348: goto +237 -> 585
/*     */     //   351: astore 8
/*     */     //   353: goto +232 -> 585
/*     */     //   356: sipush 1024
/*     */     //   359: istore 7
/*     */     //   361: iload 7
/*     */     //   363: aload_0
/*     */     //   364: getfield 14	org/apache/catalina/core/StandardServer:shutdown	Ljava/lang/String;
/*     */     //   367: invokevirtual 85	java/lang/String:length	()I
/*     */     //   370: if_icmpge +40 -> 410
/*     */     //   373: aload_0
/*     */     //   374: getfield 7	org/apache/catalina/core/StandardServer:random	Ljava/util/Random;
/*     */     //   377: ifnonnull +14 -> 391
/*     */     //   380: aload_0
/*     */     //   381: new 86	java/util/Random
/*     */     //   384: dup
/*     */     //   385: invokespecial 87	java/util/Random:<init>	()V
/*     */     //   388: putfield 7	org/apache/catalina/core/StandardServer:random	Ljava/util/Random;
/*     */     //   391: iload 7
/*     */     //   393: aload_0
/*     */     //   394: getfield 7	org/apache/catalina/core/StandardServer:random	Ljava/util/Random;
/*     */     //   397: invokevirtual 88	java/util/Random:nextInt	()I
/*     */     //   400: sipush 1024
/*     */     //   403: irem
/*     */     //   404: iadd
/*     */     //   405: istore 7
/*     */     //   407: goto -46 -> 361
/*     */     //   410: iload 7
/*     */     //   412: ifle +64 -> 476
/*     */     //   415: iconst_m1
/*     */     //   416: istore 8
/*     */     //   418: aload 4
/*     */     //   420: invokevirtual 89	java/io/InputStream:read	()I
/*     */     //   423: istore 8
/*     */     //   425: goto +20 -> 445
/*     */     //   428: astore 9
/*     */     //   430: getstatic 60	org/apache/catalina/core/StandardServer:log	Lorg/apache/juli/logging/Log;
/*     */     //   433: ldc 90
/*     */     //   435: aload 9
/*     */     //   437: invokeinterface 79 3 0
/*     */     //   442: iconst_m1
/*     */     //   443: istore 8
/*     */     //   445: iload 8
/*     */     //   447: bipush 32
/*     */     //   449: if_icmplt +27 -> 476
/*     */     //   452: iload 8
/*     */     //   454: bipush 127
/*     */     //   456: if_icmpne +6 -> 462
/*     */     //   459: goto +17 -> 476
/*     */     //   462: aload_3
/*     */     //   463: iload 8
/*     */     //   465: i2c
/*     */     //   466: invokevirtual 91	java/lang/StringBuilder:append	(C)Ljava/lang/StringBuilder;
/*     */     //   469: pop
/*     */     //   470: iinc 7 -1
/*     */     //   473: goto -63 -> 410
/*     */     //   476: aload_2
/*     */     //   477: ifnull +7 -> 484
/*     */     //   480: aload_2
/*     */     //   481: invokevirtual 80	java/net/Socket:close	()V
/*     */     //   484: goto +26 -> 510
/*     */     //   487: astore 4
/*     */     //   489: goto +21 -> 510
/*     */     //   492: astore 10
/*     */     //   494: aload_2
/*     */     //   495: ifnull +7 -> 502
/*     */     //   498: aload_2
/*     */     //   499: invokevirtual 80	java/net/Socket:close	()V
/*     */     //   502: goto +5 -> 507
/*     */     //   505: astore 11
/*     */     //   507: aload 10
/*     */     //   509: athrow
/*     */     //   510: aload_3
/*     */     //   511: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   514: aload_0
/*     */     //   515: getfield 14	org/apache/catalina/core/StandardServer:shutdown	Ljava/lang/String;
/*     */     //   518: invokevirtual 92	java/lang/String:equals	(Ljava/lang/Object;)Z
/*     */     //   521: istore 4
/*     */     //   523: iload 4
/*     */     //   525: ifeq +22 -> 547
/*     */     //   528: getstatic 60	org/apache/catalina/core/StandardServer:log	Lorg/apache/juli/logging/Log;
/*     */     //   531: getstatic 75	org/apache/catalina/core/StandardServer:sm	Lorg/apache/tomcat/util/res/StringManager;
/*     */     //   534: ldc 93
/*     */     //   536: invokevirtual 94	org/apache/tomcat/util/res/StringManager:getString	(Ljava/lang/String;)Ljava/lang/String;
/*     */     //   539: invokeinterface 95 2 0
/*     */     //   544: goto +41 -> 585
/*     */     //   547: getstatic 60	org/apache/catalina/core/StandardServer:log	Lorg/apache/juli/logging/Log;
/*     */     //   550: new 61	java/lang/StringBuilder
/*     */     //   553: dup
/*     */     //   554: invokespecial 62	java/lang/StringBuilder:<init>	()V
/*     */     //   557: ldc 96
/*     */     //   559: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   562: aload_3
/*     */     //   563: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   566: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   569: ldc 97
/*     */     //   571: invokevirtual 64	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*     */     //   574: invokevirtual 68	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   577: invokeinterface 98 2 0
/*     */     //   582: goto -437 -> 145
/*     */     //   585: aload_0
/*     */     //   586: getfield 22	org/apache/catalina/core/StandardServer:awaitSocket	Ljava/net/ServerSocket;
/*     */     //   589: astore_1
/*     */     //   590: aload_0
/*     */     //   591: aconst_null
/*     */     //   592: putfield 21	org/apache/catalina/core/StandardServer:awaitThread	Ljava/lang/Thread;
/*     */     //   595: aload_0
/*     */     //   596: aconst_null
/*     */     //   597: putfield 22	org/apache/catalina/core/StandardServer:awaitSocket	Ljava/net/ServerSocket;
/*     */     //   600: aload_1
/*     */     //   601: ifnull +11 -> 612
/*     */     //   604: aload_1
/*     */     //   605: invokevirtual 46	java/net/ServerSocket:close	()V
/*     */     //   608: goto +4 -> 612
/*     */     //   611: astore_2
/*     */     //   612: goto +39 -> 651
/*     */     //   615: astore 12
/*     */     //   617: aload_0
/*     */     //   618: getfield 22	org/apache/catalina/core/StandardServer:awaitSocket	Ljava/net/ServerSocket;
/*     */     //   621: astore 13
/*     */     //   623: aload_0
/*     */     //   624: aconst_null
/*     */     //   625: putfield 21	org/apache/catalina/core/StandardServer:awaitThread	Ljava/lang/Thread;
/*     */     //   628: aload_0
/*     */     //   629: aconst_null
/*     */     //   630: putfield 22	org/apache/catalina/core/StandardServer:awaitSocket	Ljava/net/ServerSocket;
/*     */     //   633: aload 13
/*     */     //   635: ifnull +13 -> 648
/*     */     //   638: aload 13
/*     */     //   640: invokevirtual 46	java/net/ServerSocket:close	()V
/*     */     //   643: goto +5 -> 648
/*     */     //   646: astore 14
/*     */     //   648: aload 12
/*     */     //   650: athrow
/*     */     //   651: return
/*     */     // Line number table:
/*     */     //   Java source line #418	-> byte code offset #0
/*     */     //   Java source line #420	-> byte code offset #9
/*     */     //   Java source line #422	-> byte code offset #10
/*     */     //   Java source line #424	-> byte code offset #18
/*     */     //   Java source line #425	-> byte code offset #25
/*     */     //   Java source line #427	-> byte code offset #32
/*     */     //   Java source line #430	-> byte code offset #38
/*     */     //   Java source line #428	-> byte code offset #41
/*     */     //   Java source line #430	-> byte code offset #42
/*     */     //   Java source line #433	-> byte code offset #45
/*     */     //   Java source line #434	-> byte code offset #50
/*     */     //   Java source line #433	-> byte code offset #53
/*     */     //   Java source line #435	-> byte code offset #61
/*     */     //   Java source line #440	-> byte code offset #62
/*     */     //   Java source line #441	-> byte code offset #76
/*     */     //   Java source line #447	-> byte code offset #85
/*     */     //   Java source line #442	-> byte code offset #88
/*     */     //   Java source line #443	-> byte code offset #89
/*     */     //   Java source line #446	-> byte code offset #137
/*     */     //   Java source line #450	-> byte code offset #138
/*     */     //   Java source line #453	-> byte code offset #145
/*     */     //   Java source line #454	-> byte code offset #152
/*     */     //   Java source line #455	-> byte code offset #157
/*     */     //   Java source line #456	-> byte code offset #161
/*     */     //   Java source line #460	-> byte code offset #164
/*     */     //   Java source line #461	-> byte code offset #166
/*     */     //   Java source line #464	-> byte code offset #174
/*     */     //   Java source line #466	-> byte code offset #179
/*     */     //   Java source line #467	-> byte code offset #184
/*     */     //   Java source line #468	-> byte code offset #191
/*     */     //   Java source line #486	-> byte code offset #197
/*     */     //   Java source line #469	-> byte code offset #200
/*     */     //   Java source line #472	-> byte code offset #202
/*     */     //   Java source line #473	-> byte code offset #216
/*     */     //   Java source line #472	-> byte code offset #226
/*     */     //   Java source line #513	-> byte code offset #236
/*     */     //   Java source line #514	-> byte code offset #240
/*     */     //   Java source line #518	-> byte code offset #244
/*     */     //   Java source line #516	-> byte code offset #247
/*     */     //   Java source line #518	-> byte code offset #249
/*     */     //   Java source line #475	-> byte code offset #252
/*     */     //   Java source line #476	-> byte code offset #254
/*     */     //   Java source line #477	-> byte code offset #271
/*     */     //   Java source line #476	-> byte code offset #282
/*     */     //   Java source line #513	-> byte code offset #287
/*     */     //   Java source line #514	-> byte code offset #291
/*     */     //   Java source line #518	-> byte code offset #295
/*     */     //   Java source line #516	-> byte code offset #298
/*     */     //   Java source line #518	-> byte code offset #300
/*     */     //   Java source line #479	-> byte code offset #303
/*     */     //   Java source line #480	-> byte code offset #305
/*     */     //   Java source line #513	-> byte code offset #312
/*     */     //   Java source line #514	-> byte code offset #316
/*     */     //   Java source line #518	-> byte code offset #320
/*     */     //   Java source line #516	-> byte code offset #323
/*     */     //   Java source line #518	-> byte code offset #325
/*     */     //   Java source line #484	-> byte code offset #328
/*     */     //   Java source line #513	-> byte code offset #340
/*     */     //   Java source line #514	-> byte code offset #344
/*     */     //   Java source line #518	-> byte code offset #348
/*     */     //   Java source line #516	-> byte code offset #351
/*     */     //   Java source line #518	-> byte code offset #353
/*     */     //   Java source line #489	-> byte code offset #356
/*     */     //   Java source line #490	-> byte code offset #361
/*     */     //   Java source line #491	-> byte code offset #373
/*     */     //   Java source line #492	-> byte code offset #380
/*     */     //   Java source line #493	-> byte code offset #391
/*     */     //   Java source line #495	-> byte code offset #410
/*     */     //   Java source line #496	-> byte code offset #415
/*     */     //   Java source line #498	-> byte code offset #418
/*     */     //   Java source line #502	-> byte code offset #425
/*     */     //   Java source line #499	-> byte code offset #428
/*     */     //   Java source line #500	-> byte code offset #430
/*     */     //   Java source line #501	-> byte code offset #442
/*     */     //   Java source line #504	-> byte code offset #445
/*     */     //   Java source line #505	-> byte code offset #459
/*     */     //   Java source line #507	-> byte code offset #462
/*     */     //   Java source line #508	-> byte code offset #470
/*     */     //   Java source line #509	-> byte code offset #473
/*     */     //   Java source line #513	-> byte code offset #476
/*     */     //   Java source line #514	-> byte code offset #480
/*     */     //   Java source line #518	-> byte code offset #484
/*     */     //   Java source line #516	-> byte code offset #487
/*     */     //   Java source line #519	-> byte code offset #489
/*     */     //   Java source line #512	-> byte code offset #492
/*     */     //   Java source line #513	-> byte code offset #494
/*     */     //   Java source line #514	-> byte code offset #498
/*     */     //   Java source line #518	-> byte code offset #502
/*     */     //   Java source line #516	-> byte code offset #505
/*     */     //   Java source line #518	-> byte code offset #507
/*     */     //   Java source line #522	-> byte code offset #510
/*     */     //   Java source line #523	-> byte code offset #523
/*     */     //   Java source line #524	-> byte code offset #528
/*     */     //   Java source line #525	-> byte code offset #544
/*     */     //   Java source line #527	-> byte code offset #547
/*     */     //   Java source line #528	-> byte code offset #563
/*     */     //   Java source line #527	-> byte code offset #577
/*     */     //   Java source line #529	-> byte code offset #582
/*     */     //   Java source line #531	-> byte code offset #585
/*     */     //   Java source line #532	-> byte code offset #590
/*     */     //   Java source line #533	-> byte code offset #595
/*     */     //   Java source line #536	-> byte code offset #600
/*     */     //   Java source line #538	-> byte code offset #604
/*     */     //   Java source line #541	-> byte code offset #608
/*     */     //   Java source line #539	-> byte code offset #611
/*     */     //   Java source line #543	-> byte code offset #612
/*     */     //   Java source line #531	-> byte code offset #615
/*     */     //   Java source line #532	-> byte code offset #623
/*     */     //   Java source line #533	-> byte code offset #628
/*     */     //   Java source line #536	-> byte code offset #633
/*     */     //   Java source line #538	-> byte code offset #638
/*     */     //   Java source line #541	-> byte code offset #643
/*     */     //   Java source line #539	-> byte code offset #646
/*     */     //   Java source line #543	-> byte code offset #648
/*     */     //   Java source line #544	-> byte code offset #651
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	652	0	this	StandardServer
/*     */     //   41	1	1	localInterruptedException	InterruptedException
/*     */     //   88	44	1	e	IOException
/*     */     //   156	24	1	serverSocket	ServerSocket
/*     */     //   589	16	1	serverSocket	ServerSocket
/*     */     //   53	7	2	localObject1	Object
/*     */     //   165	334	2	socket	java.net.Socket
/*     */     //   611	1	2	localIOException7	IOException
/*     */     //   611	1	2	localIOException9	IOException
/*     */     //   173	390	3	command	StringBuilder
/*     */     //   195	3	4	stream	java.io.InputStream
/*     */     //   356	63	4	stream	java.io.InputStream
/*     */     //   487	1	4	localIOException5	IOException
/*     */     //   521	3	4	match	boolean
/*     */     //   177	43	5	acceptStartTime	long
/*     */     //   200	30	7	ste	java.net.SocketTimeoutException
/*     */     //   252	29	7	ace	java.security.AccessControlException
/*     */     //   303	31	7	e	IOException
/*     */     //   359	112	7	expected	int
/*     */     //   247	1	8	localIOException1	IOException
/*     */     //   298	1	8	localIOException2	IOException
/*     */     //   323	1	8	localIOException3	IOException
/*     */     //   351	1	8	localIOException4	IOException
/*     */     //   416	48	8	ch	int
/*     */     //   428	8	9	e	IOException
/*     */     //   492	16	10	localObject2	Object
/*     */     //   505	1	11	localIOException6	IOException
/*     */     //   615	34	12	localObject3	Object
/*     */     //   621	18	13	serverSocket	ServerSocket
/*     */     //   646	1	14	localIOException8	IOException
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   32	38	41	java/lang/InterruptedException
/*     */     //   18	45	53	finally
/*     */     //   62	85	88	java/io/IOException
/*     */     //   179	197	200	java/net/SocketTimeoutException
/*     */     //   236	244	247	java/io/IOException
/*     */     //   179	197	252	java/security/AccessControlException
/*     */     //   287	295	298	java/io/IOException
/*     */     //   179	197	303	java/io/IOException
/*     */     //   312	320	323	java/io/IOException
/*     */     //   340	348	351	java/io/IOException
/*     */     //   418	425	428	java/io/IOException
/*     */     //   476	484	487	java/io/IOException
/*     */     //   174	236	492	finally
/*     */     //   252	287	492	finally
/*     */     //   303	312	492	finally
/*     */     //   328	340	492	finally
/*     */     //   356	476	492	finally
/*     */     //   492	494	492	finally
/*     */     //   494	502	505	java/io/IOException
/*     */     //   604	608	611	java/io/IOException
/*     */     //   138	585	615	finally
/*     */     //   615	617	615	finally
/*     */     //   638	643	646	java/io/IOException
/*     */   }
/*     */   
/*     */   public Service findService(String name)
/*     */   {
/* 556 */     if (name == null) {
/* 557 */       return null;
/*     */     }
/* 559 */     synchronized (this.servicesLock) {
/* 560 */       for (int i = 0; i < this.services.length; i++) {
/* 561 */         if (name.equals(this.services[i].getName())) {
/* 562 */           return this.services[i];
/*     */         }
/*     */       }
/*     */     }
/* 566 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Service[] findServices()
/*     */   {
/* 577 */     return this.services;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectName[] getServiceNames()
/*     */   {
/* 585 */     ObjectName[] onames = new ObjectName[this.services.length];
/* 586 */     for (int i = 0; i < this.services.length; i++) {
/* 587 */       onames[i] = ((StandardService)this.services[i]).getObjectName();
/*     */     }
/* 589 */     return onames;
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
/*     */   public void removeService(Service service)
/*     */   {
/* 602 */     synchronized (this.servicesLock) {
/* 603 */       int j = -1;
/* 604 */       for (int i = 0; i < this.services.length; i++) {
/* 605 */         if (service == this.services[i]) {
/* 606 */           j = i;
/* 607 */           break;
/*     */         }
/*     */       }
/* 610 */       if (j < 0)
/* 611 */         return;
/*     */       try {
/* 613 */         this.services[j].stop();
/*     */       }
/*     */       catch (LifecycleException localLifecycleException) {}
/*     */       
/* 617 */       int k = 0;
/* 618 */       Service[] results = new Service[this.services.length - 1];
/* 619 */       for (int i = 0; i < this.services.length; i++) {
/* 620 */         if (i != j)
/* 621 */           results[(k++)] = this.services[i];
/*     */       }
/* 623 */       this.services = results;
/*     */       
/*     */ 
/* 626 */       this.support.firePropertyChange("service", service, null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public File getCatalinaBase()
/*     */   {
/* 634 */     if (this.catalinaBase != null) {
/* 635 */       return this.catalinaBase;
/*     */     }
/*     */     
/* 638 */     this.catalinaBase = getCatalinaHome();
/* 639 */     return this.catalinaBase;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setCatalinaBase(File catalinaBase)
/*     */   {
/* 645 */     this.catalinaBase = catalinaBase;
/*     */   }
/*     */   
/*     */ 
/*     */   public File getCatalinaHome()
/*     */   {
/* 651 */     return this.catalinaHome;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setCatalinaHome(File catalinaHome)
/*     */   {
/* 657 */     this.catalinaHome = catalinaHome;
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
/*     */   public void addPropertyChangeListener(PropertyChangeListener listener)
/*     */   {
/* 670 */     this.support.addPropertyChangeListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removePropertyChangeListener(PropertyChangeListener listener)
/*     */   {
/* 682 */     this.support.removePropertyChangeListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 693 */     StringBuilder sb = new StringBuilder("StandardServer[");
/* 694 */     sb.append(getPort());
/* 695 */     sb.append("]");
/* 696 */     return sb.toString();
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
/*     */   public synchronized void storeConfig()
/*     */     throws InstanceNotFoundException, MBeanException
/*     */   {
/*     */     try
/*     */     {
/* 716 */       ObjectName sname = new ObjectName("Catalina:type=StoreConfig");
/* 717 */       if (this.mserver.isRegistered(sname)) {
/* 718 */         this.mserver.invoke(sname, "storeConfig", null, null);
/*     */       } else {
/* 720 */         log.error(sm.getString("standardServer.storeConfig.notAvailable", new Object[] { sname }));
/*     */       }
/*     */     } catch (Throwable t) {
/* 723 */       ExceptionUtils.handleThrowable(t);
/* 724 */       log.error(t);
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
/*     */ 
/*     */ 
/*     */   public synchronized void storeContext(org.apache.catalina.Context context)
/*     */     throws InstanceNotFoundException, MBeanException
/*     */   {
/*     */     try
/*     */     {
/* 745 */       ObjectName sname = new ObjectName("Catalina:type=StoreConfig");
/* 746 */       if (this.mserver.isRegistered(sname)) {
/* 747 */         this.mserver.invoke(sname, "store", new Object[] { context }, new String[] { "java.lang.String" });
/*     */       }
/*     */       else
/*     */       {
/* 751 */         log.error(sm.getString("standardServer.storeConfig.notAvailable", new Object[] { sname }));
/*     */       }
/*     */     } catch (Throwable t) {
/* 754 */       ExceptionUtils.handleThrowable(t);
/* 755 */       log.error(t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isUseNaming()
/*     */   {
/* 764 */     boolean useNaming = true;
/*     */     
/* 766 */     String useNamingProperty = System.getProperty("catalina.useNaming");
/* 767 */     if ((useNamingProperty != null) && 
/* 768 */       (useNamingProperty.equals("false"))) {
/* 769 */       useNaming = false;
/*     */     }
/* 771 */     return useNaming;
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
/*     */   protected void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 785 */     fireLifecycleEvent("configure_start", null);
/* 786 */     setState(LifecycleState.STARTING);
/*     */     
/* 788 */     this.globalNamingResources.start();
/*     */     
/*     */ 
/* 791 */     synchronized (this.servicesLock) {
/* 792 */       for (int i = 0; i < this.services.length; i++) {
/* 793 */         this.services[i].start();
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
/*     */   protected void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/* 809 */     setState(LifecycleState.STOPPING);
/* 810 */     fireLifecycleEvent("configure_stop", null);
/*     */     
/*     */ 
/* 813 */     for (int i = 0; i < this.services.length; i++) {
/* 814 */       this.services[i].stop();
/*     */     }
/*     */     
/* 817 */     this.globalNamingResources.stop();
/*     */     
/* 819 */     stopAwait();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {
/* 829 */     super.initInternal();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 835 */     this.onameStringCache = register(new StringCache(), "type=StringCache");
/*     */     
/*     */ 
/* 838 */     MBeanFactory factory = new MBeanFactory();
/* 839 */     factory.setContainer(this);
/* 840 */     this.onameMBeanFactory = register(factory, "type=MBeanFactory");
/*     */     
/*     */ 
/* 843 */     this.globalNamingResources.init();
/*     */     
/*     */ 
/*     */ 
/* 847 */     if (getCatalina() != null) {
/* 848 */       ClassLoader cl = getCatalina().getParentClassLoader();
/*     */       
/*     */ 
/* 851 */       while ((cl != null) && (cl != ClassLoader.getSystemClassLoader())) {
/* 852 */         if ((cl instanceof URLClassLoader)) {
/* 853 */           URL[] urls = ((URLClassLoader)cl).getURLs();
/* 854 */           for (URL url : urls) {
/* 855 */             if (url.getProtocol().equals("file")) {
/*     */               try {
/* 857 */                 File f = new File(url.toURI());
/* 858 */                 if ((f.isFile()) && 
/* 859 */                   (f.getName().endsWith(".jar"))) {
/* 860 */                   ExtensionValidator.addSystemResource(f);
/*     */                 }
/*     */               }
/*     */               catch (URISyntaxException localURISyntaxException) {}catch (IOException localIOException) {}
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 870 */         cl = cl.getParent();
/*     */       }
/*     */     }
/*     */     
/* 874 */     for (int i = 0; i < this.services.length; i++) {
/* 875 */       this.services[i].init();
/*     */     }
/*     */   }
/*     */   
/*     */   protected void destroyInternal()
/*     */     throws LifecycleException
/*     */   {
/* 882 */     for (int i = 0; i < this.services.length; i++) {
/* 883 */       this.services[i].destroy();
/*     */     }
/*     */     
/* 886 */     this.globalNamingResources.destroy();
/*     */     
/* 888 */     unregister(this.onameMBeanFactory);
/*     */     
/* 890 */     unregister(this.onameStringCache);
/*     */     
/* 892 */     super.destroyInternal();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ClassLoader getParentClassLoader()
/*     */   {
/* 900 */     if (this.parentClassLoader != null)
/* 901 */       return this.parentClassLoader;
/* 902 */     if (this.catalina != null) {
/* 903 */       return this.catalina.getParentClassLoader();
/*     */     }
/* 905 */     return ClassLoader.getSystemClassLoader();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParentClassLoader(ClassLoader parent)
/*     */   {
/* 915 */     ClassLoader oldParentClassLoader = this.parentClassLoader;
/* 916 */     this.parentClassLoader = parent;
/* 917 */     this.support.firePropertyChange("parentClassLoader", oldParentClassLoader, this.parentClassLoader);
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
/*     */   protected String getDomainInternal()
/*     */   {
/* 936 */     String domain = null;
/*     */     
/* 938 */     Service[] services = findServices();
/* 939 */     if (services.length > 0) {
/* 940 */       Service service = services[0];
/* 941 */       if (service != null) {
/* 942 */         domain = service.getDomain();
/*     */       }
/*     */     }
/* 945 */     return domain;
/*     */   }
/*     */   
/*     */ 
/*     */   protected final String getObjectNameKeyProperties()
/*     */   {
/* 951 */     return "type=Server";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\StandardServer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */