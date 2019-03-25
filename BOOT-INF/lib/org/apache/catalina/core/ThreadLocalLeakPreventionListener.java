/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.util.concurrent.Executor;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.ContainerEvent;
/*     */ import org.apache.catalina.ContainerListener;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Engine;
/*     */ import org.apache.catalina.Host;
/*     */ import org.apache.catalina.Lifecycle;
/*     */ import org.apache.catalina.LifecycleEvent;
/*     */ import org.apache.catalina.LifecycleListener;
/*     */ import org.apache.catalina.Server;
/*     */ import org.apache.catalina.Service;
/*     */ import org.apache.catalina.connector.Connector;
/*     */ import org.apache.coyote.ProtocolHandler;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.res.StringManager;
/*     */ import org.apache.tomcat.util.threads.ThreadPoolExecutor;
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
/*     */ public class ThreadLocalLeakPreventionListener
/*     */   implements LifecycleListener, ContainerListener
/*     */ {
/*  59 */   private static final Log log = LogFactory.getLog(ThreadLocalLeakPreventionListener.class);
/*     */   
/*  61 */   private volatile boolean serverStopping = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  67 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void lifecycleEvent(LifecycleEvent event)
/*     */   {
/*     */     try
/*     */     {
/*  76 */       Lifecycle lifecycle = event.getLifecycle();
/*  77 */       if (("after_start".equals(event.getType())) && ((lifecycle instanceof Server)))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  83 */         Server server = (Server)lifecycle;
/*  84 */         registerListenersForServer(server);
/*     */       }
/*     */       
/*  87 */       if (("before_stop".equals(event.getType())) && ((lifecycle instanceof Server)))
/*     */       {
/*     */ 
/*     */ 
/*  91 */         this.serverStopping = true;
/*     */       }
/*     */       
/*  94 */       if (("after_stop".equals(event.getType())) && ((lifecycle instanceof Context)))
/*     */       {
/*  96 */         stopIdleThreads((Context)lifecycle);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 100 */       String msg = sm.getString("threadLocalLeakPreventionListener.lifecycleEvent.error", new Object[] { event });
/*     */       
/*     */ 
/* 103 */       log.error(msg, e);
/*     */     }
/*     */   }
/*     */   
/*     */   public void containerEvent(ContainerEvent event)
/*     */   {
/*     */     try {
/* 110 */       String type = event.getType();
/* 111 */       if ("addChild".equals(type)) {
/* 112 */         processContainerAddChild(event.getContainer(), 
/* 113 */           (Container)event.getData());
/* 114 */       } else if ("removeChild".equals(type)) {
/* 115 */         processContainerRemoveChild(event.getContainer(), 
/* 116 */           (Container)event.getData());
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 120 */       String msg = sm.getString("threadLocalLeakPreventionListener.containerEvent.error", new Object[] { event });
/*     */       
/*     */ 
/* 123 */       log.error(msg, e);
/*     */     }
/*     */   }
/*     */   
/*     */   private void registerListenersForServer(Server server)
/*     */   {
/* 129 */     for (Service service : server.findServices()) {
/* 130 */       Engine engine = service.getContainer();
/* 131 */       engine.addContainerListener(this);
/* 132 */       registerListenersForEngine(engine);
/*     */     }
/*     */   }
/*     */   
/*     */   private void registerListenersForEngine(Engine engine)
/*     */   {
/* 138 */     for (Container hostContainer : engine.findChildren()) {
/* 139 */       Host host = (Host)hostContainer;
/* 140 */       host.addContainerListener(this);
/* 141 */       registerListenersForHost(host);
/*     */     }
/*     */   }
/*     */   
/*     */   private void registerListenersForHost(Host host) {
/* 146 */     for (Container contextContainer : host.findChildren()) {
/* 147 */       Context context = (Context)contextContainer;
/* 148 */       registerContextListener(context);
/*     */     }
/*     */   }
/*     */   
/*     */   private void registerContextListener(Context context) {
/* 153 */     context.addLifecycleListener(this);
/*     */   }
/*     */   
/*     */   protected void processContainerAddChild(Container parent, Container child) {
/* 157 */     if (log.isDebugEnabled()) {
/* 158 */       log.debug("Process addChild[parent=" + parent + ",child=" + child + "]");
/*     */     }
/*     */     
/* 161 */     if ((child instanceof Context)) {
/* 162 */       registerContextListener((Context)child);
/* 163 */     } else if ((child instanceof Engine)) {
/* 164 */       registerListenersForEngine((Engine)child);
/* 165 */     } else if ((child instanceof Host)) {
/* 166 */       registerListenersForHost((Host)child);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void processContainerRemoveChild(Container parent, Container child)
/*     */   {
/* 174 */     if (log.isDebugEnabled()) {
/* 175 */       log.debug("Process removeChild[parent=" + parent + ",child=" + child + "]");
/*     */     }
/*     */     
/* 178 */     if ((child instanceof Context)) {
/* 179 */       Context context = (Context)child;
/* 180 */       context.removeLifecycleListener(this);
/* 181 */     } else if (((child instanceof Host)) || ((child instanceof Engine))) {
/* 182 */       child.removeContainerListener(this);
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
/*     */   private void stopIdleThreads(Context context)
/*     */   {
/* 195 */     if (this.serverStopping) { return;
/*     */     }
/* 197 */     if ((!(context instanceof StandardContext)) || 
/* 198 */       (!((StandardContext)context).getRenewThreadsWhenStoppingContext())) {
/* 199 */       log.debug("Not renewing threads when the context is stopping. It is not configured to do it.");
/*     */       
/* 201 */       return;
/*     */     }
/*     */     
/* 204 */     Engine engine = (Engine)context.getParent().getParent();
/* 205 */     Service service = engine.getService();
/* 206 */     Connector[] connectors = service.findConnectors();
/* 207 */     if (connectors != null) {
/* 208 */       for (Connector connector : connectors) {
/* 209 */         ProtocolHandler handler = connector.getProtocolHandler();
/* 210 */         Executor executor = null;
/* 211 */         if (handler != null) {
/* 212 */           executor = handler.getExecutor();
/*     */         }
/*     */         
/* 215 */         if ((executor instanceof ThreadPoolExecutor)) {
/* 216 */           ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor)executor;
/*     */           
/* 218 */           threadPoolExecutor.contextStopping();
/* 219 */         } else if ((executor instanceof StandardThreadExecutor)) {
/* 220 */           StandardThreadExecutor stdThreadExecutor = (StandardThreadExecutor)executor;
/*     */           
/* 222 */           stdThreadExecutor.contextStopping();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\ThreadLocalLeakPreventionListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */