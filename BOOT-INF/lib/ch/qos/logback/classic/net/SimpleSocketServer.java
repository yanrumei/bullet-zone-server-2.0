/*     */ package ch.qos.logback.classic.net;
/*     */ 
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.joran.JoranConfigurator;
/*     */ import ch.qos.logback.core.joran.spi.JoranException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import javax.net.ServerSocketFactory;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SimpleSocketServer
/*     */   extends Thread
/*     */ {
/*  53 */   Logger logger = LoggerFactory.getLogger(SimpleSocketServer.class);
/*     */   
/*     */   private final int port;
/*     */   private final LoggerContext lc;
/*  57 */   private boolean closed = false;
/*     */   private ServerSocket serverSocket;
/*  59 */   private List<SocketNode> socketNodeList = new ArrayList();
/*     */   private CountDownLatch latch;
/*     */   
/*     */   public static void main(String[] argv)
/*     */     throws Exception
/*     */   {
/*  65 */     doMain(SimpleSocketServer.class, argv);
/*     */   }
/*     */   
/*     */   protected static void doMain(Class<? extends SimpleSocketServer> serverClass, String[] argv) throws Exception {
/*  69 */     int port = -1;
/*  70 */     if (argv.length == 2) {
/*  71 */       port = parsePortNumber(argv[0]);
/*     */     } else {
/*  73 */       usage("Wrong number of arguments.");
/*     */     }
/*     */     
/*  76 */     String configFile = argv[1];
/*  77 */     LoggerContext lc = (LoggerContext)LoggerFactory.getILoggerFactory();
/*  78 */     configureLC(lc, configFile);
/*     */     
/*  80 */     SimpleSocketServer sss = new SimpleSocketServer(lc, port);
/*  81 */     sss.start();
/*     */   }
/*     */   
/*     */   public SimpleSocketServer(LoggerContext lc, int port) {
/*  85 */     this.lc = lc;
/*  86 */     this.port = port;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void run()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: invokestatic 96	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   3: invokevirtual 100	java/lang/Thread:getName	()Ljava/lang/String;
/*     */     //   6: astore_1
/*     */     //   7: aload_0
/*     */     //   8: invokevirtual 104	ch/qos/logback/classic/net/SimpleSocketServer:getServerThreadName	()Ljava/lang/String;
/*     */     //   11: astore_2
/*     */     //   12: invokestatic 96	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   15: aload_2
/*     */     //   16: invokevirtual 107	java/lang/Thread:setName	(Ljava/lang/String;)V
/*     */     //   19: aload_0
/*     */     //   20: getfield 81	ch/qos/logback/classic/net/SimpleSocketServer:logger	Lorg/slf4j/Logger;
/*     */     //   23: new 110	java/lang/StringBuilder
/*     */     //   26: dup
/*     */     //   27: ldc 112
/*     */     //   29: invokespecial 114	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   32: aload_0
/*     */     //   33: getfield 92	ch/qos/logback/classic/net/SimpleSocketServer:port	I
/*     */     //   36: invokevirtual 116	java/lang/StringBuilder:append	(I)Ljava/lang/StringBuilder;
/*     */     //   39: invokevirtual 120	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   42: invokeinterface 123 2 0
/*     */     //   47: aload_0
/*     */     //   48: aload_0
/*     */     //   49: invokevirtual 128	ch/qos/logback/classic/net/SimpleSocketServer:getServerSocketFactory	()Ljavax/net/ServerSocketFactory;
/*     */     //   52: aload_0
/*     */     //   53: getfield 92	ch/qos/logback/classic/net/SimpleSocketServer:port	I
/*     */     //   56: invokevirtual 132	javax/net/ServerSocketFactory:createServerSocket	(I)Ljava/net/ServerSocket;
/*     */     //   59: putfield 138	ch/qos/logback/classic/net/SimpleSocketServer:serverSocket	Ljava/net/ServerSocket;
/*     */     //   62: goto +131 -> 193
/*     */     //   65: aload_0
/*     */     //   66: getfield 81	ch/qos/logback/classic/net/SimpleSocketServer:logger	Lorg/slf4j/Logger;
/*     */     //   69: ldc -116
/*     */     //   71: invokeinterface 123 2 0
/*     */     //   76: aload_0
/*     */     //   77: invokevirtual 142	ch/qos/logback/classic/net/SimpleSocketServer:signalAlmostReadiness	()V
/*     */     //   80: aload_0
/*     */     //   81: getfield 138	ch/qos/logback/classic/net/SimpleSocketServer:serverSocket	Ljava/net/ServerSocket;
/*     */     //   84: invokevirtual 145	java/net/ServerSocket:accept	()Ljava/net/Socket;
/*     */     //   87: astore_3
/*     */     //   88: aload_0
/*     */     //   89: getfield 81	ch/qos/logback/classic/net/SimpleSocketServer:logger	Lorg/slf4j/Logger;
/*     */     //   92: new 110	java/lang/StringBuilder
/*     */     //   95: dup
/*     */     //   96: ldc -105
/*     */     //   98: invokespecial 114	java/lang/StringBuilder:<init>	(Ljava/lang/String;)V
/*     */     //   101: aload_3
/*     */     //   102: invokevirtual 153	java/net/Socket:getInetAddress	()Ljava/net/InetAddress;
/*     */     //   105: invokevirtual 159	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*     */     //   108: invokevirtual 120	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*     */     //   111: invokeinterface 123 2 0
/*     */     //   116: aload_0
/*     */     //   117: getfield 81	ch/qos/logback/classic/net/SimpleSocketServer:logger	Lorg/slf4j/Logger;
/*     */     //   120: ldc -94
/*     */     //   122: invokeinterface 123 2 0
/*     */     //   127: new 164	ch/qos/logback/classic/net/SocketNode
/*     */     //   130: dup
/*     */     //   131: aload_0
/*     */     //   132: aload_3
/*     */     //   133: aload_0
/*     */     //   134: getfield 90	ch/qos/logback/classic/net/SimpleSocketServer:lc	Lch/qos/logback/classic/LoggerContext;
/*     */     //   137: invokespecial 166	ch/qos/logback/classic/net/SocketNode:<init>	(Lch/qos/logback/classic/net/SimpleSocketServer;Ljava/net/Socket;Lch/qos/logback/classic/LoggerContext;)V
/*     */     //   140: astore 4
/*     */     //   142: aload_0
/*     */     //   143: getfield 88	ch/qos/logback/classic/net/SimpleSocketServer:socketNodeList	Ljava/util/List;
/*     */     //   146: dup
/*     */     //   147: astore 5
/*     */     //   149: monitorenter
/*     */     //   150: aload_0
/*     */     //   151: getfield 88	ch/qos/logback/classic/net/SimpleSocketServer:socketNodeList	Ljava/util/List;
/*     */     //   154: aload 4
/*     */     //   156: invokeinterface 169 2 0
/*     */     //   161: pop
/*     */     //   162: aload 5
/*     */     //   164: monitorexit
/*     */     //   165: goto +7 -> 172
/*     */     //   168: aload 5
/*     */     //   170: monitorexit
/*     */     //   171: athrow
/*     */     //   172: aload_0
/*     */     //   173: aload_3
/*     */     //   174: invokevirtual 175	ch/qos/logback/classic/net/SimpleSocketServer:getClientThreadName	(Ljava/net/Socket;)Ljava/lang/String;
/*     */     //   177: astore 5
/*     */     //   179: new 3	java/lang/Thread
/*     */     //   182: dup
/*     */     //   183: aload 4
/*     */     //   185: aload 5
/*     */     //   187: invokespecial 179	java/lang/Thread:<init>	(Ljava/lang/Runnable;Ljava/lang/String;)V
/*     */     //   190: invokevirtual 182	java/lang/Thread:start	()V
/*     */     //   193: aload_0
/*     */     //   194: getfield 83	ch/qos/logback/classic/net/SimpleSocketServer:closed	Z
/*     */     //   197: ifeq -132 -> 65
/*     */     //   200: goto +59 -> 259
/*     */     //   203: astore_2
/*     */     //   204: aload_0
/*     */     //   205: getfield 83	ch/qos/logback/classic/net/SimpleSocketServer:closed	Z
/*     */     //   208: ifeq +17 -> 225
/*     */     //   211: aload_0
/*     */     //   212: getfield 81	ch/qos/logback/classic/net/SimpleSocketServer:logger	Lorg/slf4j/Logger;
/*     */     //   215: ldc -73
/*     */     //   217: invokeinterface 123 2 0
/*     */     //   222: goto +15 -> 237
/*     */     //   225: aload_0
/*     */     //   226: getfield 81	ch/qos/logback/classic/net/SimpleSocketServer:logger	Lorg/slf4j/Logger;
/*     */     //   229: ldc -71
/*     */     //   231: aload_2
/*     */     //   232: invokeinterface 187 3 0
/*     */     //   237: invokestatic 96	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   240: aload_1
/*     */     //   241: invokevirtual 107	java/lang/Thread:setName	(Ljava/lang/String;)V
/*     */     //   244: goto +22 -> 266
/*     */     //   247: astore 6
/*     */     //   249: invokestatic 96	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   252: aload_1
/*     */     //   253: invokevirtual 107	java/lang/Thread:setName	(Ljava/lang/String;)V
/*     */     //   256: aload 6
/*     */     //   258: athrow
/*     */     //   259: invokestatic 96	java/lang/Thread:currentThread	()Ljava/lang/Thread;
/*     */     //   262: aload_1
/*     */     //   263: invokevirtual 107	java/lang/Thread:setName	(Ljava/lang/String;)V
/*     */     //   266: return
/*     */     // Line number table:
/*     */     //   Java source line #91	-> byte code offset #0
/*     */     //   Java source line #95	-> byte code offset #7
/*     */     //   Java source line #96	-> byte code offset #12
/*     */     //   Java source line #98	-> byte code offset #19
/*     */     //   Java source line #99	-> byte code offset #47
/*     */     //   Java source line #100	-> byte code offset #62
/*     */     //   Java source line #101	-> byte code offset #65
/*     */     //   Java source line #102	-> byte code offset #76
/*     */     //   Java source line #103	-> byte code offset #80
/*     */     //   Java source line #104	-> byte code offset #88
/*     */     //   Java source line #105	-> byte code offset #116
/*     */     //   Java source line #106	-> byte code offset #127
/*     */     //   Java source line #107	-> byte code offset #142
/*     */     //   Java source line #108	-> byte code offset #150
/*     */     //   Java source line #107	-> byte code offset #162
/*     */     //   Java source line #110	-> byte code offset #172
/*     */     //   Java source line #111	-> byte code offset #179
/*     */     //   Java source line #100	-> byte code offset #193
/*     */     //   Java source line #113	-> byte code offset #200
/*     */     //   Java source line #114	-> byte code offset #204
/*     */     //   Java source line #115	-> byte code offset #211
/*     */     //   Java source line #116	-> byte code offset #222
/*     */     //   Java source line #117	-> byte code offset #225
/*     */     //   Java source line #122	-> byte code offset #237
/*     */     //   Java source line #121	-> byte code offset #247
/*     */     //   Java source line #122	-> byte code offset #249
/*     */     //   Java source line #123	-> byte code offset #256
/*     */     //   Java source line #122	-> byte code offset #259
/*     */     //   Java source line #124	-> byte code offset #266
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	267	0	this	SimpleSocketServer
/*     */     //   6	257	1	oldThreadName	String
/*     */     //   11	5	2	newThreadName	String
/*     */     //   203	29	2	e	Exception
/*     */     //   87	87	3	socket	Socket
/*     */     //   140	44	4	newSocketNode	SocketNode
/*     */     //   147	22	5	Ljava/lang/Object;	Object
/*     */     //   177	9	5	clientThreadName	String
/*     */     //   247	10	6	localObject1	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   150	165	168	finally
/*     */     //   168	171	168	finally
/*     */     //   7	200	203	java/lang/Exception
/*     */     //   7	237	247	finally
/*     */   }
/*     */   
/*     */   protected String getServerThreadName()
/*     */   {
/* 130 */     return String.format("Logback %s (port %d)", new Object[] { getClass().getSimpleName(), Integer.valueOf(this.port) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getClientThreadName(Socket socket)
/*     */   {
/* 137 */     return String.format("Logback SocketNode (client: %s)", new Object[] { socket.getRemoteSocketAddress() });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ServerSocketFactory getServerSocketFactory()
/*     */   {
/* 146 */     return ServerSocketFactory.getDefault();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void signalAlmostReadiness()
/*     */   {
/* 154 */     if ((this.latch != null) && (this.latch.getCount() != 0L))
/*     */     {
/* 156 */       this.latch.countDown();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   void setLatch(CountDownLatch latch)
/*     */   {
/* 165 */     this.latch = latch;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CountDownLatch getLatch()
/*     */   {
/* 172 */     return this.latch;
/*     */   }
/*     */   
/*     */   public boolean isClosed() {
/* 176 */     return this.closed;
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   public void close()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: iconst_1
/*     */     //   2: putfield 83	ch/qos/logback/classic/net/SimpleSocketServer:closed	Z
/*     */     //   5: aload_0
/*     */     //   6: getfield 138	ch/qos/logback/classic/net/SimpleSocketServer:serverSocket	Ljava/net/ServerSocket;
/*     */     //   9: ifnull +48 -> 57
/*     */     //   12: aload_0
/*     */     //   13: getfield 138	ch/qos/logback/classic/net/SimpleSocketServer:serverSocket	Ljava/net/ServerSocket;
/*     */     //   16: invokevirtual 254	java/net/ServerSocket:close	()V
/*     */     //   19: goto +33 -> 52
/*     */     //   22: astore_1
/*     */     //   23: aload_0
/*     */     //   24: getfield 81	ch/qos/logback/classic/net/SimpleSocketServer:logger	Lorg/slf4j/Logger;
/*     */     //   27: ldc_w 256
/*     */     //   30: aload_1
/*     */     //   31: invokeinterface 187 3 0
/*     */     //   36: aload_0
/*     */     //   37: aconst_null
/*     */     //   38: putfield 138	ch/qos/logback/classic/net/SimpleSocketServer:serverSocket	Ljava/net/ServerSocket;
/*     */     //   41: goto +16 -> 57
/*     */     //   44: astore_2
/*     */     //   45: aload_0
/*     */     //   46: aconst_null
/*     */     //   47: putfield 138	ch/qos/logback/classic/net/SimpleSocketServer:serverSocket	Ljava/net/ServerSocket;
/*     */     //   50: aload_2
/*     */     //   51: athrow
/*     */     //   52: aload_0
/*     */     //   53: aconst_null
/*     */     //   54: putfield 138	ch/qos/logback/classic/net/SimpleSocketServer:serverSocket	Ljava/net/ServerSocket;
/*     */     //   57: aload_0
/*     */     //   58: getfield 81	ch/qos/logback/classic/net/SimpleSocketServer:logger	Lorg/slf4j/Logger;
/*     */     //   61: ldc_w 258
/*     */     //   64: invokeinterface 123 2 0
/*     */     //   69: aload_0
/*     */     //   70: getfield 88	ch/qos/logback/classic/net/SimpleSocketServer:socketNodeList	Ljava/util/List;
/*     */     //   73: dup
/*     */     //   74: astore_1
/*     */     //   75: monitorenter
/*     */     //   76: aload_0
/*     */     //   77: getfield 88	ch/qos/logback/classic/net/SimpleSocketServer:socketNodeList	Ljava/util/List;
/*     */     //   80: invokeinterface 260 1 0
/*     */     //   85: astore_3
/*     */     //   86: goto +17 -> 103
/*     */     //   89: aload_3
/*     */     //   90: invokeinterface 264 1 0
/*     */     //   95: checkcast 164	ch/qos/logback/classic/net/SocketNode
/*     */     //   98: astore_2
/*     */     //   99: aload_2
/*     */     //   100: invokevirtual 270	ch/qos/logback/classic/net/SocketNode:close	()V
/*     */     //   103: aload_3
/*     */     //   104: invokeinterface 271 1 0
/*     */     //   109: ifne -20 -> 89
/*     */     //   112: aload_1
/*     */     //   113: monitorexit
/*     */     //   114: goto +6 -> 120
/*     */     //   117: aload_1
/*     */     //   118: monitorexit
/*     */     //   119: athrow
/*     */     //   120: aload_0
/*     */     //   121: getfield 88	ch/qos/logback/classic/net/SimpleSocketServer:socketNodeList	Ljava/util/List;
/*     */     //   124: invokeinterface 274 1 0
/*     */     //   129: ifeq +15 -> 144
/*     */     //   132: aload_0
/*     */     //   133: getfield 81	ch/qos/logback/classic/net/SimpleSocketServer:logger	Lorg/slf4j/Logger;
/*     */     //   136: ldc_w 278
/*     */     //   139: invokeinterface 280 2 0
/*     */     //   144: return
/*     */     // Line number table:
/*     */     //   Java source line #180	-> byte code offset #0
/*     */     //   Java source line #181	-> byte code offset #5
/*     */     //   Java source line #183	-> byte code offset #12
/*     */     //   Java source line #184	-> byte code offset #19
/*     */     //   Java source line #185	-> byte code offset #23
/*     */     //   Java source line #187	-> byte code offset #36
/*     */     //   Java source line #186	-> byte code offset #44
/*     */     //   Java source line #187	-> byte code offset #45
/*     */     //   Java source line #188	-> byte code offset #50
/*     */     //   Java source line #187	-> byte code offset #52
/*     */     //   Java source line #191	-> byte code offset #57
/*     */     //   Java source line #192	-> byte code offset #69
/*     */     //   Java source line #193	-> byte code offset #76
/*     */     //   Java source line #194	-> byte code offset #99
/*     */     //   Java source line #193	-> byte code offset #103
/*     */     //   Java source line #192	-> byte code offset #112
/*     */     //   Java source line #197	-> byte code offset #120
/*     */     //   Java source line #198	-> byte code offset #132
/*     */     //   Java source line #201	-> byte code offset #144
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	145	0	this	SimpleSocketServer
/*     */     //   22	9	1	e	java.io.IOException
/*     */     //   74	44	1	Ljava/lang/Object;	Object
/*     */     //   44	7	2	localObject1	Object
/*     */     //   98	2	2	sn	SocketNode
/*     */     //   85	19	3	localIterator	java.util.Iterator
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   12	19	22	java/io/IOException
/*     */     //   12	36	44	finally
/*     */     //   76	114	117	finally
/*     */     //   117	119	117	finally
/*     */   }
/*     */   
/*     */   public void socketNodeClosing(SocketNode sn)
/*     */   {
/* 204 */     this.logger.debug("Removing {}", sn);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 209 */     synchronized (this.socketNodeList) {
/* 210 */       this.socketNodeList.remove(sn);
/*     */     }
/*     */   }
/*     */   
/*     */   static void usage(String msg) {
/* 215 */     System.err.println(msg);
/* 216 */     System.err.println("Usage: java " + SimpleSocketServer.class.getName() + " port configFile");
/* 217 */     System.exit(1);
/*     */   }
/*     */   
/*     */   static int parsePortNumber(String portStr) {
/*     */     try {
/* 222 */       return Integer.parseInt(portStr);
/*     */     } catch (NumberFormatException e) {
/* 224 */       e.printStackTrace();
/* 225 */       usage("Could not interpret port number [" + portStr + "].");
/*     */     }
/* 227 */     return -1;
/*     */   }
/*     */   
/*     */   public static void configureLC(LoggerContext lc, String configFile) throws JoranException
/*     */   {
/* 232 */     JoranConfigurator configurator = new JoranConfigurator();
/* 233 */     lc.reset();
/* 234 */     configurator.setContext(lc);
/* 235 */     configurator.doConfigure(configFile);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\net\SimpleSocketServer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */