/*     */ package org.apache.catalina.valves;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.Semaphore;
/*     */ import javax.servlet.ServletException;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Valve;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.catalina.connector.Response;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SemaphoreValve
/*     */   extends ValveBase
/*     */ {
/*     */   public SemaphoreValve()
/*     */   {
/*  44 */     super(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  53 */   protected Semaphore semaphore = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  62 */   protected int concurrency = 10;
/*  63 */   public int getConcurrency() { return this.concurrency; }
/*  64 */   public void setConcurrency(int concurrency) { this.concurrency = concurrency; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  70 */   protected boolean fairness = false;
/*  71 */   public boolean getFairness() { return this.fairness; }
/*  72 */   public void setFairness(boolean fairness) { this.fairness = fairness; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */   protected boolean block = true;
/*  79 */   public boolean getBlock() { return this.block; }
/*  80 */   public void setBlock(boolean block) { this.block = block; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */   protected boolean interruptible = false;
/*  87 */   public boolean getInterruptible() { return this.interruptible; }
/*  88 */   public void setInterruptible(boolean interruptible) { this.interruptible = interruptible; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected synchronized void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 101 */     this.semaphore = new Semaphore(this.concurrency, this.fairness);
/*     */     
/* 103 */     setState(LifecycleState.STARTING);
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
/*     */   protected synchronized void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/* 117 */     setState(LifecycleState.STOPPING);
/*     */     
/* 119 */     this.semaphore = null;
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
/*     */   public void invoke(Request request, Response response)
/*     */     throws IOException, ServletException
/*     */   {
/* 138 */     if (controlConcurrency(request, response)) {
/* 139 */       boolean shouldRelease = true;
/*     */       try {
/* 141 */         if (this.block) {
/* 142 */           if (this.interruptible) {
/*     */             try {
/* 144 */               this.semaphore.acquire();
/*     */             } catch (InterruptedException e) {
/* 146 */               shouldRelease = false;
/* 147 */               permitDenied(request, response);
/* 148 */               return;
/*     */             }
/*     */           } else {
/* 151 */             this.semaphore.acquireUninterruptibly();
/*     */           }
/*     */         }
/* 154 */         else if (!this.semaphore.tryAcquire()) {
/* 155 */           shouldRelease = false;
/* 156 */           permitDenied(request, response);
/* 157 */           return;
/*     */         }
/*     */         
/* 160 */         getNext().invoke(request, response);
/*     */       } finally {
/* 162 */         if (shouldRelease) {
/* 163 */           this.semaphore.release();
/*     */         }
/*     */       }
/*     */     } else {
/* 167 */       getNext().invoke(request, response);
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
/*     */   public boolean controlConcurrency(Request request, Response response)
/*     */   {
/* 181 */     return true;
/*     */   }
/*     */   
/*     */   public void permitDenied(Request request, Response response)
/*     */     throws IOException, ServletException
/*     */   {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\valves\SemaphoreValve.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */