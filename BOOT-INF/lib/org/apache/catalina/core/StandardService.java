/*     */ package org.apache.catalina.core;
/*     */ 
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Engine;
/*     */ import org.apache.catalina.Executor;
/*     */ import org.apache.catalina.JmxEnabled;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Server;
/*     */ import org.apache.catalina.Service;
/*     */ import org.apache.catalina.connector.Connector;
/*     */ import org.apache.catalina.mapper.Mapper;
/*     */ import org.apache.catalina.mapper.MapperListener;
/*     */ import org.apache.catalina.util.LifecycleMBeanBase;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StandardService
/*     */   extends LifecycleMBeanBase
/*     */   implements Service
/*     */ {
/*  53 */   private static final Log log = LogFactory.getLog(StandardService.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  61 */   private String name = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  68 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  73 */   private Server server = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  78 */   protected final PropertyChangeSupport support = new PropertyChangeSupport(this);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */   protected Connector[] connectors = new Connector[0];
/*  85 */   private final Object connectorsLock = new Object();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  90 */   protected final ArrayList<Executor> executors = new ArrayList();
/*     */   
/*  92 */   private Engine engine = null;
/*     */   
/*  94 */   private ClassLoader parentClassLoader = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  99 */   protected final Mapper mapper = new Mapper();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 105 */   protected final MapperListener mapperListener = new MapperListener(this);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Mapper getMapper()
/*     */   {
/* 112 */     return this.mapper;
/*     */   }
/*     */   
/*     */ 
/*     */   public Engine getContainer()
/*     */   {
/* 118 */     return this.engine;
/*     */   }
/*     */   
/*     */ 
/*     */   public void setContainer(Engine engine)
/*     */   {
/* 124 */     Engine oldEngine = this.engine;
/* 125 */     if (oldEngine != null) {
/* 126 */       oldEngine.setService(null);
/*     */     }
/* 128 */     this.engine = engine;
/* 129 */     if (this.engine != null) {
/* 130 */       this.engine.setService(this);
/*     */     }
/* 132 */     if (getState().isAvailable()) {
/* 133 */       if (this.engine != null) {
/*     */         try {
/* 135 */           this.engine.start();
/*     */         } catch (LifecycleException e) {
/* 137 */           log.warn(sm.getString("standardService.engine.startFailed"), e);
/*     */         }
/*     */       }
/*     */       try
/*     */       {
/* 142 */         this.mapperListener.stop();
/*     */       } catch (LifecycleException e) {
/* 144 */         log.warn(sm.getString("standardService.mapperListener.stopFailed"), e);
/*     */       }
/*     */       try {
/* 147 */         this.mapperListener.start();
/*     */       } catch (LifecycleException e) {
/* 149 */         log.warn(sm.getString("standardService.mapperListener.startFailed"), e);
/*     */       }
/* 151 */       if (oldEngine != null) {
/*     */         try {
/* 153 */           oldEngine.stop();
/*     */         } catch (LifecycleException e) {
/* 155 */           log.warn(sm.getString("standardService.engine.stopFailed"), e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 161 */     this.support.firePropertyChange("container", oldEngine, this.engine);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 170 */     return this.name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setName(String name)
/*     */   {
/* 181 */     this.name = name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Server getServer()
/*     */   {
/* 190 */     return this.server;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServer(Server server)
/*     */   {
/* 201 */     this.server = server;
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
/*     */   public void addConnector(Connector connector)
/*     */   {
/* 216 */     synchronized (this.connectorsLock) {
/* 217 */       connector.setService(this);
/* 218 */       Connector[] results = new Connector[this.connectors.length + 1];
/* 219 */       System.arraycopy(this.connectors, 0, results, 0, this.connectors.length);
/* 220 */       results[this.connectors.length] = connector;
/* 221 */       this.connectors = results;
/*     */       
/* 223 */       if (getState().isAvailable()) {
/*     */         try {
/* 225 */           connector.start();
/*     */         } catch (LifecycleException e) {
/* 227 */           log.error(sm.getString("standardService.connector.startFailed", new Object[] { connector }), e);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 234 */       this.support.firePropertyChange("connector", null, connector);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public ObjectName[] getConnectorNames()
/*     */   {
/* 241 */     ObjectName[] results = new ObjectName[this.connectors.length];
/* 242 */     for (int i = 0; i < results.length; i++) {
/* 243 */       results[i] = this.connectors[i].getObjectName();
/*     */     }
/* 245 */     return results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addPropertyChangeListener(PropertyChangeListener listener)
/*     */   {
/* 255 */     this.support.addPropertyChangeListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Connector[] findConnectors()
/*     */   {
/* 264 */     return this.connectors;
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
/*     */   public void removeConnector(Connector connector)
/*     */   {
/* 278 */     synchronized (this.connectorsLock) {
/* 279 */       int j = -1;
/* 280 */       for (int i = 0; i < this.connectors.length; i++) {
/* 281 */         if (connector == this.connectors[i]) {
/* 282 */           j = i;
/* 283 */           break;
/*     */         }
/*     */       }
/* 286 */       if (j < 0)
/* 287 */         return;
/* 288 */       if (this.connectors[j].getState().isAvailable()) {
/*     */         try {
/* 290 */           this.connectors[j].stop();
/*     */         } catch (LifecycleException e) {
/* 292 */           log.error(sm.getString("standardService.connector.stopFailed", new Object[] { this.connectors[j] }), e);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 297 */       connector.setService(null);
/* 298 */       int k = 0;
/* 299 */       Connector[] results = new Connector[this.connectors.length - 1];
/* 300 */       for (int i = 0; i < this.connectors.length; i++) {
/* 301 */         if (i != j)
/* 302 */           results[(k++)] = this.connectors[i];
/*     */       }
/* 304 */       this.connectors = results;
/*     */       
/*     */ 
/* 307 */       this.support.firePropertyChange("connector", connector, null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removePropertyChangeListener(PropertyChangeListener listener)
/*     */   {
/* 318 */     this.support.removePropertyChangeListener(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 327 */     StringBuilder sb = new StringBuilder("StandardService[");
/* 328 */     sb.append(getName());
/* 329 */     sb.append("]");
/* 330 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addExecutor(Executor ex)
/*     */   {
/* 340 */     synchronized (this.executors) {
/* 341 */       if (!this.executors.contains(ex)) {
/* 342 */         this.executors.add(ex);
/* 343 */         if (getState().isAvailable()) {
/*     */           try {
/* 345 */             ex.start();
/*     */           } catch (LifecycleException x) {
/* 347 */             log.error("Executor.start", x);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Executor[] findExecutors()
/*     */   {
/* 361 */     synchronized (this.executors) {
/* 362 */       Executor[] arr = new Executor[this.executors.size()];
/* 363 */       this.executors.toArray(arr);
/* 364 */       return arr;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Executor getExecutor(String executorName)
/*     */   {
/* 376 */     synchronized (this.executors) {
/* 377 */       for (Executor executor : this.executors) {
/* 378 */         if (executorName.equals(executor.getName()))
/* 379 */           return executor;
/*     */       }
/*     */     }
/* 382 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void removeExecutor(Executor ex)
/*     */   {
/* 392 */     synchronized (this.executors) {
/* 393 */       if ((this.executors.remove(ex)) && (getState().isAvailable())) {
/*     */         try {
/* 395 */           ex.stop();
/*     */         } catch (LifecycleException e) {
/* 397 */           log.error("Executor.stop", e);
/*     */         }
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
/*     */ 
/*     */   protected void startInternal()
/*     */     throws LifecycleException
/*     */   {
/* 415 */     if (log.isInfoEnabled())
/* 416 */       log.info(sm.getString("standardService.start.name", new Object[] { this.name }));
/* 417 */     setState(LifecycleState.STARTING);
/*     */     
/*     */ 
/* 420 */     if (this.engine != null)
/* 421 */       synchronized (this.engine) {
/* 422 */         this.engine.start();
/*     */       }
/*     */     Object localObject2;
/*     */     Executor executor;
/* 426 */     synchronized (this.executors) {
/* 427 */       for (localObject2 = this.executors.iterator(); ((Iterator)localObject2).hasNext();) { executor = (Executor)((Iterator)localObject2).next();
/* 428 */         executor.start();
/*     */       }
/*     */     }
/*     */     
/* 432 */     this.mapperListener.start();
/*     */     
/*     */ 
/* 435 */     synchronized (this.connectorsLock) {
/* 436 */       localObject2 = this.connectors;executor = localObject2.length; for (Executor localExecutor1 = 0; localExecutor1 < executor; localExecutor1++) { Connector connector = localObject2[localExecutor1];
/*     */         try
/*     */         {
/* 439 */           if (connector.getState() != LifecycleState.FAILED) {
/* 440 */             connector.start();
/*     */           }
/*     */         } catch (Exception e) {
/* 443 */           log.error(sm.getString("standardService.connector.startFailed", new Object[] { connector }), e);
/*     */         }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/* 464 */     synchronized (this.connectorsLock) {
/* 465 */       for (Connector connector : this.connectors) {
/*     */         try {
/* 467 */           connector.pause();
/*     */         } catch (Exception e) {
/* 469 */           log.error(sm.getString("standardService.connector.pauseFailed", new Object[] { connector }), e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 476 */     if (log.isInfoEnabled())
/* 477 */       log.info(sm.getString("standardService.stop.name", new Object[] { this.name }));
/* 478 */     setState(LifecycleState.STOPPING);
/*     */     
/*     */ 
/* 481 */     if (this.engine != null) {
/* 482 */       synchronized (this.engine) {
/* 483 */         this.engine.stop();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 488 */     synchronized (this.connectorsLock) {
/* 489 */       for (Connector connector : this.connectors) {
/* 490 */         if (LifecycleState.STARTED.equals(connector
/* 491 */           .getState()))
/*     */         {
/*     */ 
/*     */           try
/*     */           {
/*     */ 
/*     */ 
/* 498 */             connector.stop();
/*     */           } catch (Exception e) {
/* 500 */             log.error(sm.getString("standardService.connector.stopFailed", new Object[] { connector }), e);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 509 */     if (this.mapperListener.getState() != LifecycleState.INITIALIZED) {
/* 510 */       this.mapperListener.stop();
/*     */     }
/*     */     
/* 513 */     synchronized (this.executors) {
/* 514 */       for (??? = this.executors.iterator(); ((Iterator)???).hasNext();) { Executor executor = (Executor)((Iterator)???).next();
/* 515 */         executor.stop();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initInternal()
/*     */     throws LifecycleException
/*     */   {
/* 528 */     super.initInternal();
/*     */     
/* 530 */     if (this.engine != null) {
/* 531 */       this.engine.init();
/*     */     }
/*     */     
/*     */ 
/* 535 */     Executor[] arrayOfExecutor = findExecutors();int i = arrayOfExecutor.length; Executor executor; for (Executor localExecutor1 = 0; localExecutor1 < i; localExecutor1++) { executor = arrayOfExecutor[localExecutor1];
/* 536 */       if ((executor instanceof JmxEnabled)) {
/* 537 */         ((JmxEnabled)executor).setDomain(getDomain());
/*     */       }
/* 539 */       executor.init();
/*     */     }
/*     */     
/*     */ 
/* 543 */     this.mapperListener.init();
/*     */     
/*     */ 
/* 546 */     synchronized (this.connectorsLock) {
/* 547 */       Connector[] arrayOfConnector = this.connectors;localExecutor1 = arrayOfConnector.length; for (executor = 0; executor < localExecutor1; executor++) { Connector connector = arrayOfConnector[executor];
/*     */         try {
/* 549 */           connector.init();
/*     */         } catch (Exception e) {
/* 551 */           String message = sm.getString("standardService.connector.initFailed", new Object[] { connector });
/*     */           
/* 553 */           log.error(message, e);
/*     */           
/* 555 */           if (Boolean.getBoolean("org.apache.catalina.startup.EXIT_ON_INIT_FAILURE")) {
/* 556 */             throw new LifecycleException(message);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void destroyInternal() throws LifecycleException
/*     */   {
/* 565 */     this.mapperListener.destroy();
/*     */     
/*     */ 
/* 568 */     synchronized (this.connectorsLock) {
/* 569 */       for (Connector connector : this.connectors) {
/*     */         try {
/* 571 */           connector.destroy();
/*     */         } catch (Exception e) {
/* 573 */           log.error(sm.getString("standardService.connector.destroyFailed", new Object[] { connector }), e);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 580 */     for (Executor executor : findExecutors()) {
/* 581 */       executor.destroy();
/*     */     }
/*     */     
/* 584 */     if (this.engine != null) {
/* 585 */       this.engine.destroy();
/*     */     }
/*     */     
/* 588 */     super.destroyInternal();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ClassLoader getParentClassLoader()
/*     */   {
/* 597 */     if (this.parentClassLoader != null)
/* 598 */       return this.parentClassLoader;
/* 599 */     if (this.server != null) {
/* 600 */       return this.server.getParentClassLoader();
/*     */     }
/* 602 */     return ClassLoader.getSystemClassLoader();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParentClassLoader(ClassLoader parent)
/*     */   {
/* 613 */     ClassLoader oldParentClassLoader = this.parentClassLoader;
/* 614 */     this.parentClassLoader = parent;
/* 615 */     this.support.firePropertyChange("parentClassLoader", oldParentClassLoader, this.parentClassLoader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getDomainInternal()
/*     */   {
/* 622 */     String domain = null;
/* 623 */     Container engine = getContainer();
/*     */     
/*     */ 
/* 626 */     if (engine != null) {
/* 627 */       domain = engine.getName();
/*     */     }
/*     */     
/*     */ 
/* 631 */     if (domain == null) {
/* 632 */       domain = getName();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 637 */     return domain;
/*     */   }
/*     */   
/*     */ 
/*     */   public final String getObjectNameKeyProperties()
/*     */   {
/* 643 */     return "type=Service";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\StandardService.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */