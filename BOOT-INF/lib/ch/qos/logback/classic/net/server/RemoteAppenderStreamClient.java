/*     */ package ch.qos.logback.classic.net.server;
/*     */ 
/*     */ import ch.qos.logback.classic.Logger;
/*     */ import ch.qos.logback.classic.LoggerContext;
/*     */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*     */ import ch.qos.logback.core.util.CloseUtil;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.net.Socket;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class RemoteAppenderStreamClient
/*     */   implements RemoteAppenderClient
/*     */ {
/*     */   private final String id;
/*     */   private final Socket socket;
/*     */   private final InputStream inputStream;
/*     */   private LoggerContext lc;
/*     */   private Logger logger;
/*     */   
/*     */   public RemoteAppenderStreamClient(String id, Socket socket)
/*     */   {
/*  48 */     this.id = id;
/*  49 */     this.socket = socket;
/*  50 */     this.inputStream = null;
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
/*     */   public RemoteAppenderStreamClient(String id, InputStream inputStream)
/*     */   {
/*  63 */     this.id = id;
/*  64 */     this.socket = null;
/*  65 */     this.inputStream = inputStream;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setLoggerContext(LoggerContext lc)
/*     */   {
/*  72 */     this.lc = lc;
/*  73 */     this.logger = lc.getLogger(getClass().getPackage().getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void close()
/*     */   {
/*  80 */     if (this.socket == null)
/*  81 */       return;
/*  82 */     CloseUtil.closeQuietly(this.socket);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/*  89 */     this.logger.info(this + ": connected");
/*  90 */     ObjectInputStream ois = null;
/*     */     try {
/*  92 */       ois = createObjectInputStream();
/*     */       for (;;)
/*     */       {
/*  95 */         ILoggingEvent event = (ILoggingEvent)ois.readObject();
/*     */         
/*     */ 
/*  98 */         Logger remoteLogger = this.lc.getLogger(event.getLoggerName());
/*     */         
/* 100 */         if (remoteLogger.isEnabledFor(event.getLevel()))
/*     */         {
/* 102 */           remoteLogger.callAppenders(event);
/*     */         }
/*     */         
/*     */       }
/*     */     }
/*     */     catch (EOFException localEOFException) {}catch (IOException ex)
/*     */     {
/* 109 */       this.logger.info(this + ": " + ex);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 111 */       this.logger.error(this + ": unknown event class");
/*     */     } catch (RuntimeException ex) {
/* 113 */       this.logger.error(this + ": " + ex);
/*     */     } finally {
/* 115 */       if (ois != null) {
/* 116 */         CloseUtil.closeQuietly(ois);
/*     */       }
/* 118 */       close();
/* 119 */       this.logger.info(this + ": connection closed");
/*     */     }
/*     */   }
/*     */   
/*     */   private ObjectInputStream createObjectInputStream() throws IOException {
/* 124 */     if (this.inputStream != null) {
/* 125 */       return new ObjectInputStream(this.inputStream);
/*     */     }
/* 127 */     return new ObjectInputStream(this.socket.getInputStream());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 135 */     return "client " + this.id;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\net\server\RemoteAppenderStreamClient.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */