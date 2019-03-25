/*     */ package ch.qos.logback.classic.net;
/*     */ 
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketAddress;
/*     */ import java.net.SocketException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SocketNode
/*     */   implements Runnable
/*     */ {
/*     */   Socket socket;
/*     */   LoggerContext context;
/*     */   ObjectInputStream ois;
/*     */   SocketAddress remoteSocketAddress;
/*     */   Logger logger;
/*  51 */   boolean closed = false;
/*     */   SimpleSocketServer socketServer;
/*     */   
/*     */   public SocketNode(SimpleSocketServer socketServer, Socket socket, LoggerContext context) {
/*  55 */     this.socketServer = socketServer;
/*  56 */     this.socket = socket;
/*  57 */     this.remoteSocketAddress = socket.getRemoteSocketAddress();
/*  58 */     this.context = context;
/*  59 */     this.logger = context.getLogger(SocketNode.class);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/*  71 */       this.ois = new ObjectInputStream(new BufferedInputStream(this.socket.getInputStream()));
/*     */     } catch (Exception e) {
/*  73 */       this.logger.error("Could not open ObjectInputStream to " + this.socket, e);
/*  74 */       this.closed = true;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  81 */       while (!this.closed)
/*     */       {
/*  83 */         ILoggingEvent event = (ILoggingEvent)this.ois.readObject();
/*     */         
/*     */ 
/*  86 */         Logger remoteLogger = this.context.getLogger(event.getLoggerName());
/*     */         
/*  88 */         if (remoteLogger.isEnabledFor(event.getLevel()))
/*     */         {
/*  90 */           remoteLogger.callAppenders(event);
/*     */         }
/*     */       }
/*     */     } catch (EOFException localEOFException) {
/*  94 */       this.logger.info("Caught java.io.EOFException closing connection.");
/*     */     } catch (SocketException localSocketException) {
/*  96 */       this.logger.info("Caught java.net.SocketException closing connection.");
/*     */     } catch (IOException e) {
/*  98 */       this.logger.info("Caught java.io.IOException: " + e);
/*  99 */       this.logger.info("Closing connection.");
/*     */     } catch (Exception e) {
/* 101 */       this.logger.error("Unexpected exception. Closing connection.", e);
/*     */     }
/*     */     
/* 104 */     this.socketServer.socketNodeClosing(this);
/* 105 */     close();
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   void close()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: getfield 27	ch/qos/logback/classic/net/SocketNode:closed	Z
/*     */     //   4: ifeq +4 -> 8
/*     */     //   7: return
/*     */     //   8: aload_0
/*     */     //   9: iconst_1
/*     */     //   10: putfield 27	ch/qos/logback/classic/net/SocketNode:closed	Z
/*     */     //   13: aload_0
/*     */     //   14: getfield 68	ch/qos/logback/classic/net/SocketNode:ois	Ljava/io/ObjectInputStream;
/*     */     //   17: ifnull +45 -> 62
/*     */     //   20: aload_0
/*     */     //   21: getfield 68	ch/qos/logback/classic/net/SocketNode:ois	Ljava/io/ObjectInputStream;
/*     */     //   24: invokevirtual 152	java/io/ObjectInputStream:close	()V
/*     */     //   27: goto +30 -> 57
/*     */     //   30: astore_1
/*     */     //   31: aload_0
/*     */     //   32: getfield 49	ch/qos/logback/classic/net/SocketNode:logger	Lch/qos/logback/classic/Logger;
/*     */     //   35: ldc -103
/*     */     //   37: aload_1
/*     */     //   38: invokevirtual 155	ch/qos/logback/classic/Logger:warn	(Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   41: aload_0
/*     */     //   42: aconst_null
/*     */     //   43: putfield 68	ch/qos/logback/classic/net/SocketNode:ois	Ljava/io/ObjectInputStream;
/*     */     //   46: goto +16 -> 62
/*     */     //   49: astore_2
/*     */     //   50: aload_0
/*     */     //   51: aconst_null
/*     */     //   52: putfield 68	ch/qos/logback/classic/net/SocketNode:ois	Ljava/io/ObjectInputStream;
/*     */     //   55: aload_2
/*     */     //   56: athrow
/*     */     //   57: aload_0
/*     */     //   58: aconst_null
/*     */     //   59: putfield 68	ch/qos/logback/classic/net/SocketNode:ois	Ljava/io/ObjectInputStream;
/*     */     //   62: return
/*     */     // Line number table:
/*     */     //   Java source line #109	-> byte code offset #0
/*     */     //   Java source line #110	-> byte code offset #7
/*     */     //   Java source line #112	-> byte code offset #8
/*     */     //   Java source line #113	-> byte code offset #13
/*     */     //   Java source line #115	-> byte code offset #20
/*     */     //   Java source line #116	-> byte code offset #27
/*     */     //   Java source line #117	-> byte code offset #31
/*     */     //   Java source line #119	-> byte code offset #41
/*     */     //   Java source line #118	-> byte code offset #49
/*     */     //   Java source line #119	-> byte code offset #50
/*     */     //   Java source line #120	-> byte code offset #55
/*     */     //   Java source line #119	-> byte code offset #57
/*     */     //   Java source line #122	-> byte code offset #62
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	63	0	this	SocketNode
/*     */     //   30	8	1	e	IOException
/*     */     //   49	7	2	localObject	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   20	27	30	java/io/IOException
/*     */     //   20	41	49	finally
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 126 */     return getClass().getName() + this.remoteSocketAddress.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\net\SocketNode.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */