/*     */ package ch.qos.logback.core.net.server;
/*     */ 
/*     */ import ch.qos.logback.core.spi.ContextAwareBase;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.RejectedExecutionException;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReentrantLock;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class ConcurrentServerRunner<T extends Client>
/*     */   extends ContextAwareBase
/*     */   implements Runnable, ServerRunner<T>
/*     */ {
/*  49 */   private final Lock clientsLock = new ReentrantLock();
/*     */   
/*  51 */   private final Collection<T> clients = new ArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */   private final ServerListener<T> listener;
/*     */   
/*     */ 
/*     */ 
/*     */   private final Executor executor;
/*     */   
/*     */ 
/*     */   private boolean running;
/*     */   
/*     */ 
/*     */ 
/*     */   public ConcurrentServerRunner(ServerListener<T> listener, Executor executor)
/*     */   {
/*  68 */     this.listener = listener;
/*  69 */     this.executor = executor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isRunning()
/*     */   {
/*  76 */     return this.running;
/*     */   }
/*     */   
/*     */   protected void setRunning(boolean running) {
/*  80 */     this.running = running;
/*     */   }
/*     */   
/*     */ 
/*     */   public void stop()
/*     */     throws IOException
/*     */   {
/*  87 */     this.listener.close();
/*  88 */     accept(new ClientVisitor() {
/*     */       public void visit(T client) {
/*  90 */         client.close();
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void accept(ClientVisitor<T> visitor)
/*     */   {
/*  99 */     Collection<T> clients = copyClients();
/* 100 */     for (T client : clients) {
/*     */       try {
/* 102 */         visitor.visit(client);
/*     */       } catch (RuntimeException ex) {
/* 104 */         addError(client + ": " + ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Collection<T> copyClients()
/*     */   {
/* 115 */     this.clientsLock.lock();
/*     */     try {
/* 117 */       Collection<T> copy = new ArrayList(this.clients);
/* 118 */       return copy;
/*     */     } finally {
/* 120 */       this.clientsLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/* 128 */     setRunning(true);
/*     */     try {
/* 130 */       addInfo("listening on " + this.listener);
/* 131 */       while (!Thread.currentThread().isInterrupted()) {
/* 132 */         T client = this.listener.acceptClient();
/* 133 */         if (!configureClient(client)) {
/* 134 */           addError(client + ": connection dropped");
/* 135 */           client.close();
/*     */         }
/*     */         else {
/*     */           try {
/* 139 */             this.executor.execute(new ClientWrapper(client));
/*     */           } catch (RejectedExecutionException ex) {
/* 141 */             addError(client + ": connection dropped");
/* 142 */             client.close();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (InterruptedException ex) {}catch (Exception ex) {
/* 148 */       addError("listener: " + ex);
/*     */     }
/*     */     
/* 151 */     setRunning(false);
/* 152 */     addInfo("shutting down");
/* 153 */     this.listener.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract boolean configureClient(T paramT);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addClient(T client)
/*     */   {
/* 173 */     this.clientsLock.lock();
/*     */     try {
/* 175 */       this.clients.add(client);
/*     */     } finally {
/* 177 */       this.clientsLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeClient(T client)
/*     */   {
/* 186 */     this.clientsLock.lock();
/*     */     try {
/* 188 */       this.clients.remove(client);
/*     */     } finally {
/* 190 */       this.clientsLock.unlock();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class ClientWrapper
/*     */     implements Client
/*     */   {
/*     */     private final T delegate;
/*     */     
/*     */ 
/*     */     public ClientWrapper()
/*     */     {
/* 203 */       this.delegate = client;
/*     */     }
/*     */     
/*     */     public void run() {
/* 207 */       ConcurrentServerRunner.this.addClient(this.delegate);
/*     */       try {
/* 209 */         this.delegate.run();
/*     */       } finally {
/* 211 */         ConcurrentServerRunner.this.removeClient(this.delegate);
/*     */       }
/*     */     }
/*     */     
/*     */     public void close() {
/* 216 */       this.delegate.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\net\server\ConcurrentServerRunner.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */