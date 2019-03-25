/*     */ package org.apache.coyote;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CopyOnWriteArraySet;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.tomcat.util.net.AbstractEndpoint.Handler.SocketState;
/*     */ import org.apache.tomcat.util.net.DispatchType;
/*     */ import org.apache.tomcat.util.net.SocketEvent;
/*     */ import org.apache.tomcat.util.net.SocketWrapperBase;
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
/*     */ public abstract class AbstractProcessorLight
/*     */   implements Processor
/*     */ {
/*  37 */   private Set<DispatchType> dispatches = new CopyOnWriteArraySet();
/*     */   
/*     */ 
/*     */ 
/*     */   public AbstractEndpoint.Handler.SocketState process(SocketWrapperBase<?> socketWrapper, SocketEvent status)
/*     */     throws IOException
/*     */   {
/*  44 */     AbstractEndpoint.Handler.SocketState state = AbstractEndpoint.Handler.SocketState.CLOSED;
/*  45 */     Iterator<DispatchType> dispatches = null;
/*     */     do {
/*  47 */       if (dispatches != null) {
/*  48 */         DispatchType nextDispatch = (DispatchType)dispatches.next();
/*  49 */         state = dispatch(nextDispatch.getSocketStatus());
/*  50 */       } else if (status != SocketEvent.DISCONNECT)
/*     */       {
/*  52 */         if ((isAsync()) || (isUpgrade()) || (state == AbstractEndpoint.Handler.SocketState.ASYNC_END)) {
/*  53 */           state = dispatch(status);
/*  54 */           if (state == AbstractEndpoint.Handler.SocketState.OPEN)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  60 */             state = service(socketWrapper);
/*     */           }
/*  62 */         } else if (status == SocketEvent.OPEN_WRITE)
/*     */         {
/*  64 */           state = AbstractEndpoint.Handler.SocketState.LONG;
/*  65 */         } else if (status == SocketEvent.OPEN_READ) {
/*  66 */           state = service(socketWrapper);
/*     */         }
/*     */         else
/*     */         {
/*  70 */           state = AbstractEndpoint.Handler.SocketState.CLOSED;
/*     */         }
/*     */       }
/*  73 */       if ((state != AbstractEndpoint.Handler.SocketState.CLOSED) && (isAsync())) {
/*  74 */         state = asyncPostProcess();
/*     */       }
/*     */       
/*  77 */       if (getLog().isDebugEnabled()) {
/*  78 */         getLog().debug("Socket: [" + socketWrapper + "], Status in: [" + status + "], State out: [" + state + "]");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  83 */       if ((dispatches == null) || (!dispatches.hasNext()))
/*     */       {
/*     */ 
/*  86 */         dispatches = getIteratorAndClearDispatches();
/*     */       }
/*  88 */     } while ((state == AbstractEndpoint.Handler.SocketState.ASYNC_END) || ((dispatches != null) && (state != AbstractEndpoint.Handler.SocketState.CLOSED)));
/*     */     
/*     */ 
/*  91 */     return state;
/*     */   }
/*     */   
/*     */   public void addDispatch(DispatchType dispatchType)
/*     */   {
/*  96 */     synchronized (this.dispatches) {
/*  97 */       this.dispatches.add(dispatchType);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Iterator<DispatchType> getIteratorAndClearDispatches()
/*     */   {
/* 107 */     synchronized (this.dispatches)
/*     */     {
/*     */ 
/* 110 */       Iterator<DispatchType> result = this.dispatches.iterator();
/* 111 */       if (result.hasNext()) {
/* 112 */         this.dispatches.clear();
/*     */       } else
/* 114 */         result = null;
/*     */     }
/*     */     Iterator<DispatchType> result;
/* 117 */     return result;
/*     */   }
/*     */   
/*     */   protected void clearDispatches()
/*     */   {
/* 122 */     synchronized (this.dispatches) {
/* 123 */       this.dispatches.clear();
/*     */     }
/*     */   }
/*     */   
/*     */   protected abstract AbstractEndpoint.Handler.SocketState service(SocketWrapperBase<?> paramSocketWrapperBase)
/*     */     throws IOException;
/*     */   
/*     */   protected abstract AbstractEndpoint.Handler.SocketState dispatch(SocketEvent paramSocketEvent);
/*     */   
/*     */   protected abstract AbstractEndpoint.Handler.SocketState asyncPostProcess();
/*     */   
/*     */   protected abstract Log getLog();
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\coyote\AbstractProcessorLight.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */