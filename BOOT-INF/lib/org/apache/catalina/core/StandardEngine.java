/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.io.File;
/*     */ import java.util.Locale;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import org.apache.catalina.AccessLog;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.ContainerEvent;
/*     */ import org.apache.catalina.ContainerListener;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Engine;
/*     */ import org.apache.catalina.Host;
/*     */ import org.apache.catalina.LifecycleEvent;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleListener;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Pipeline;
/*     */ import org.apache.catalina.Realm;
/*     */ import org.apache.catalina.Server;
/*     */ import org.apache.catalina.Service;
/*     */ import org.apache.catalina.connector.Request;
/*     */ import org.apache.catalina.connector.Response;
/*     */ import org.apache.catalina.realm.NullRealm;
/*     */ import org.apache.catalina.util.ServerInfo;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
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
/*     */ public class StandardEngine
/*     */   extends ContainerBase
/*     */   implements Engine
/*     */ {
/*  56 */   private static final Log log = LogFactory.getLog(StandardEngine.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StandardEngine()
/*     */   {
/*  67 */     this.pipeline.setBasic(new StandardEngineValve());
/*     */     try
/*     */     {
/*  70 */       setJvmRoute(System.getProperty("jvmRoute"));
/*     */     } catch (Exception ex) {
/*  72 */       log.warn(sm.getString("standardEngine.jvmRouteFail"));
/*     */     }
/*     */     
/*  75 */     this.backgroundProcessorDelay = 10;
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
/*  87 */   private String defaultHost = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  93 */   private Service service = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String jvmRouteId;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 105 */   private final AtomicReference<AccessLog> defaultAccessLog = new AtomicReference();
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
/*     */   public Realm getRealm()
/*     */   {
/* 118 */     Realm configured = super.getRealm();
/*     */     
/*     */ 
/* 121 */     if (configured == null) {
/* 122 */       configured = new NullRealm();
/* 123 */       setRealm(configured);
/*     */     }
/* 125 */     return configured;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getDefaultHost()
/*     */   {
/* 135 */     return this.defaultHost;
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
/*     */   public void setDefaultHost(String host)
/*     */   {
/* 148 */     String oldDefaultHost = this.defaultHost;
/* 149 */     if (host == null) {
/* 150 */       this.defaultHost = null;
/*     */     } else {
/* 152 */       this.defaultHost = host.toLowerCase(Locale.ENGLISH);
/*     */     }
/* 154 */     this.support.firePropertyChange("defaultHost", oldDefaultHost, this.defaultHost);
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
/*     */   public void setJvmRoute(String routeId)
/*     */   {
/* 168 */     this.jvmRouteId = routeId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getJvmRoute()
/*     */   {
/* 178 */     return this.jvmRouteId;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Service getService()
/*     */   {
/* 188 */     return this.service;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setService(Service service)
/*     */   {
/* 200 */     this.service = service;
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
/*     */   public void addChild(Container child)
/*     */   {
/* 215 */     if (!(child instanceof Host))
/*     */     {
/* 217 */       throw new IllegalArgumentException(sm.getString("standardEngine.notHost")); }
/* 218 */     super.addChild(child);
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
/*     */   public void setParent(Container container)
/*     */   {
/* 233 */     throw new IllegalArgumentException(sm.getString("standardEngine.notParent"));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {
/* 242 */     getRealm();
/* 243 */     super.initInternal();
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
/*     */   protected synchronized void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 258 */     if (log.isInfoEnabled()) {
/* 259 */       log.info("Starting Servlet Engine: " + ServerInfo.getServerInfo());
/*     */     }
/*     */     
/* 262 */     super.startInternal();
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
/*     */   public void logAccess(Request request, Response response, long time, boolean useDefault)
/*     */   {
/* 276 */     boolean logged = false;
/*     */     
/* 278 */     if (getAccessLog() != null) {
/* 279 */       this.accessLog.log(request, response, time);
/* 280 */       logged = true;
/*     */     }
/*     */     
/* 283 */     if ((!logged) && (useDefault)) {
/* 284 */       AccessLog newDefaultAccessLog = (AccessLog)this.defaultAccessLog.get();
/* 285 */       if (newDefaultAccessLog == null)
/*     */       {
/*     */ 
/* 288 */         Host host = (Host)findChild(getDefaultHost());
/* 289 */         Context context = null;
/* 290 */         if ((host != null) && (host.getState().isAvailable())) {
/* 291 */           newDefaultAccessLog = host.getAccessLog();
/*     */           
/* 293 */           if (newDefaultAccessLog != null) {
/* 294 */             if (this.defaultAccessLog.compareAndSet(null, newDefaultAccessLog))
/*     */             {
/* 296 */               AccessLogListener l = new AccessLogListener(this, host, null);
/*     */               
/* 298 */               l.install();
/*     */             }
/*     */           }
/*     */           else {
/* 302 */             context = (Context)host.findChild("");
/* 303 */             if ((context != null) && 
/* 304 */               (context.getState().isAvailable())) {
/* 305 */               newDefaultAccessLog = context.getAccessLog();
/* 306 */               if ((newDefaultAccessLog != null) && 
/* 307 */                 (this.defaultAccessLog.compareAndSet(null, newDefaultAccessLog)))
/*     */               {
/* 309 */                 AccessLogListener l = new AccessLogListener(this, null, context);
/*     */                 
/* 311 */                 l.install();
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 318 */         if (newDefaultAccessLog == null) {
/* 319 */           newDefaultAccessLog = new NoopAccessLog();
/* 320 */           if (this.defaultAccessLog.compareAndSet(null, newDefaultAccessLog))
/*     */           {
/* 322 */             AccessLogListener l = new AccessLogListener(this, host, context);
/*     */             
/* 324 */             l.install();
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 329 */       newDefaultAccessLog.log(request, response, time);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ClassLoader getParentClassLoader()
/*     */   {
/* 339 */     if (this.parentClassLoader != null)
/* 340 */       return this.parentClassLoader;
/* 341 */     if (this.service != null) {
/* 342 */       return this.service.getParentClassLoader();
/*     */     }
/* 344 */     return ClassLoader.getSystemClassLoader();
/*     */   }
/*     */   
/*     */ 
/*     */   public File getCatalinaBase()
/*     */   {
/* 350 */     if (this.service != null) {
/* 351 */       Server s = this.service.getServer();
/* 352 */       if (s != null) {
/* 353 */         File base = s.getCatalinaBase();
/* 354 */         if (base != null) {
/* 355 */           return base;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 360 */     return super.getCatalinaBase();
/*     */   }
/*     */   
/*     */ 
/*     */   public File getCatalinaHome()
/*     */   {
/* 366 */     if (this.service != null) {
/* 367 */       Server s = this.service.getServer();
/* 368 */       if (s != null) {
/* 369 */         File base = s.getCatalinaHome();
/* 370 */         if (base != null) {
/* 371 */           return base;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 376 */     return super.getCatalinaHome();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getObjectNameKeyProperties()
/*     */   {
/* 384 */     return "type=Engine";
/*     */   }
/*     */   
/*     */ 
/*     */   protected String getDomainInternal()
/*     */   {
/* 390 */     return getName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final class NoopAccessLog
/*     */     implements AccessLog
/*     */   {
/*     */     public void log(Request request, Response response, long time) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void setRequestAttributesEnabled(boolean requestAttributesEnabled) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean getRequestAttributesEnabled()
/*     */     {
/* 412 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected static final class AccessLogListener
/*     */     implements PropertyChangeListener, LifecycleListener, ContainerListener
/*     */   {
/*     */     private final StandardEngine engine;
/*     */     private final Host host;
/*     */     private final Context context;
/* 423 */     private volatile boolean disabled = false;
/*     */     
/*     */     public AccessLogListener(StandardEngine engine, Host host, Context context)
/*     */     {
/* 427 */       this.engine = engine;
/* 428 */       this.host = host;
/* 429 */       this.context = context;
/*     */     }
/*     */     
/*     */     public void install() {
/* 433 */       this.engine.addPropertyChangeListener(this);
/* 434 */       if (this.host != null) {
/* 435 */         this.host.addContainerListener(this);
/* 436 */         this.host.addLifecycleListener(this);
/*     */       }
/* 438 */       if (this.context != null) {
/* 439 */         this.context.addLifecycleListener(this);
/*     */       }
/*     */     }
/*     */     
/*     */     private void uninstall() {
/* 444 */       this.disabled = true;
/* 445 */       if (this.context != null) {
/* 446 */         this.context.removeLifecycleListener(this);
/*     */       }
/* 448 */       if (this.host != null) {
/* 449 */         this.host.removeLifecycleListener(this);
/* 450 */         this.host.removeContainerListener(this);
/*     */       }
/* 452 */       this.engine.removePropertyChangeListener(this);
/*     */     }
/*     */     
/*     */     public void lifecycleEvent(LifecycleEvent event)
/*     */     {
/* 457 */       if (this.disabled) { return;
/*     */       }
/* 459 */       String type = event.getType();
/* 460 */       if (("after_start".equals(type)) || 
/* 461 */         ("before_stop".equals(type)) || 
/* 462 */         ("before_destroy".equals(type)))
/*     */       {
/*     */ 
/*     */ 
/* 466 */         this.engine.defaultAccessLog.set(null);
/* 467 */         uninstall();
/*     */       }
/*     */     }
/*     */     
/*     */     public void propertyChange(PropertyChangeEvent evt)
/*     */     {
/* 473 */       if (this.disabled) return;
/* 474 */       if ("defaultHost".equals(evt.getPropertyName()))
/*     */       {
/*     */ 
/* 477 */         this.engine.defaultAccessLog.set(null);
/* 478 */         uninstall();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public void containerEvent(ContainerEvent event)
/*     */     {
/* 485 */       if (this.disabled) return;
/* 486 */       if ("addChild".equals(event.getType())) {
/* 487 */         Context context = (Context)event.getData();
/* 488 */         if ("".equals(context.getPath()))
/*     */         {
/*     */ 
/* 491 */           this.engine.defaultAccessLog.set(null);
/* 492 */           uninstall();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\StandardEngine.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */