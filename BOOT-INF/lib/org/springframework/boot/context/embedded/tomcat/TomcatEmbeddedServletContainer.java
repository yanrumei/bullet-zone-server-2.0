/*     */ package org.springframework.boot.context.embedded.tomcat;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.catalina.Container;
/*     */ import org.apache.catalina.Context;
/*     */ import org.apache.catalina.Engine;
/*     */ import org.apache.catalina.Host;
/*     */ import org.apache.catalina.LifecycleException;
/*     */ import org.apache.catalina.LifecycleState;
/*     */ import org.apache.catalina.Server;
/*     */ import org.apache.catalina.Service;
/*     */ import org.apache.catalina.connector.Connector;
/*     */ import org.apache.catalina.startup.Tomcat;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.apache.coyote.ProtocolHandler;
/*     */ import org.apache.naming.ContextBindings;
/*     */ import org.springframework.boot.context.embedded.EmbeddedServletContainer;
/*     */ import org.springframework.boot.context.embedded.EmbeddedServletContainerException;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class TomcatEmbeddedServletContainer
/*     */   implements EmbeddedServletContainer
/*     */ {
/*  53 */   private static final Log logger = LogFactory.getLog(TomcatEmbeddedServletContainer.class);
/*     */   
/*  55 */   private static final AtomicInteger containerCounter = new AtomicInteger(-1);
/*     */   
/*  57 */   private final Object monitor = new Object();
/*     */   
/*  59 */   private final Map<Service, Connector[]> serviceConnectors = new HashMap();
/*     */   
/*     */ 
/*     */   private final Tomcat tomcat;
/*     */   
/*     */ 
/*     */   private final boolean autoStart;
/*     */   
/*     */   private volatile boolean started;
/*     */   
/*     */ 
/*     */   public TomcatEmbeddedServletContainer(Tomcat tomcat)
/*     */   {
/*  72 */     this(tomcat, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TomcatEmbeddedServletContainer(Tomcat tomcat, boolean autoStart)
/*     */   {
/*  81 */     Assert.notNull(tomcat, "Tomcat Server must not be null");
/*  82 */     this.tomcat = tomcat;
/*  83 */     this.autoStart = autoStart;
/*  84 */     initialize();
/*     */   }
/*     */   
/*     */   private void initialize() throws EmbeddedServletContainerException
/*     */   {
/*  89 */     logger.info("Tomcat initialized with port(s): " + getPortsDescription(false));
/*  90 */     synchronized (this.monitor) {
/*     */       try {
/*  92 */         addInstanceIdToEngineName();
/*     */         
/*     */         try
/*     */         {
/*  96 */           removeServiceConnectors();
/*     */           
/*     */ 
/*  99 */           this.tomcat.start();
/*     */           
/*     */ 
/* 102 */           rethrowDeferredStartupExceptions();
/*     */           
/* 104 */           Context context = findContext();
/*     */           try {
/* 106 */             ContextBindings.bindClassLoader(context, getNamingToken(context), 
/* 107 */               getClass().getClassLoader());
/*     */           }
/*     */           catch (NamingException localNamingException) {}
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 115 */           startDaemonAwaitThread();
/*     */         }
/*     */         catch (Exception ex) {
/* 118 */           containerCounter.decrementAndGet();
/* 119 */           throw ex;
/*     */         }
/*     */       }
/*     */       catch (Exception ex) {
/* 123 */         throw new EmbeddedServletContainerException("Unable to start embedded Tomcat", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private Context findContext()
/*     */   {
/* 130 */     for (Container child : this.tomcat.getHost().findChildren()) {
/* 131 */       if ((child instanceof Context)) {
/* 132 */         return (Context)child;
/*     */       }
/*     */     }
/* 135 */     throw new IllegalStateException("The host does not contain a Context");
/*     */   }
/*     */   
/*     */   private void addInstanceIdToEngineName() {
/* 139 */     int instanceId = containerCounter.incrementAndGet();
/* 140 */     if (instanceId > 0) {
/* 141 */       Engine engine = this.tomcat.getEngine();
/* 142 */       engine.setName(engine.getName() + "-" + instanceId);
/*     */     }
/*     */   }
/*     */   
/*     */   private void removeServiceConnectors() {
/* 147 */     for (Service service : this.tomcat.getServer().findServices()) {
/* 148 */       Connector[] connectors = (Connector[])service.findConnectors().clone();
/* 149 */       this.serviceConnectors.put(service, connectors);
/* 150 */       for (Connector connector : connectors) {
/* 151 */         service.removeConnector(connector);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void rethrowDeferredStartupExceptions() throws Exception {
/* 157 */     Container[] children = this.tomcat.getHost().findChildren();
/* 158 */     for (Container container : children) {
/* 159 */       if ((container instanceof TomcatEmbeddedContext))
/*     */       {
/* 161 */         Exception exception = ((TomcatEmbeddedContext)container).getStarter().getStartUpException();
/* 162 */         if (exception != null) {
/* 163 */           throw exception;
/*     */         }
/*     */       }
/* 166 */       if (!LifecycleState.STARTED.equals(container.getState())) {
/* 167 */         throw new IllegalStateException(container + " failed to start");
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void startDaemonAwaitThread() {
/* 173 */     Thread awaitThread = new Thread("container-" + containerCounter.get())
/*     */     {
/*     */       public void run()
/*     */       {
/* 177 */         TomcatEmbeddedServletContainer.this.tomcat.getServer().await();
/*     */       }
/*     */       
/* 180 */     };
/* 181 */     awaitThread.setContextClassLoader(getClass().getClassLoader());
/* 182 */     awaitThread.setDaemon(false);
/* 183 */     awaitThread.start();
/*     */   }
/*     */   
/*     */   public void start() throws EmbeddedServletContainerException
/*     */   {
/* 188 */     synchronized (this.monitor) {
/* 189 */       if (this.started) {
/* 190 */         return;
/*     */       }
/*     */       try {
/* 193 */         addPreviouslyRemovedConnectors();
/* 194 */         Connector connector = this.tomcat.getConnector();
/* 195 */         if ((connector != null) && (this.autoStart)) {
/* 196 */           startConnector(connector);
/*     */         }
/* 198 */         checkThatConnectorsHaveStarted();
/* 199 */         this.started = true;
/* 200 */         logger
/* 201 */           .info("Tomcat started on port(s): " + getPortsDescription(true));
/*     */       } catch (ConnectorStartFailedException ex) {
/*     */         Context context;
/* 204 */         stopSilently();
/* 205 */         throw ex;
/*     */       }
/*     */       catch (Exception ex) {
/* 208 */         throw new EmbeddedServletContainerException("Unable to start embedded Tomcat servlet container", ex);
/*     */       }
/*     */       finally
/*     */       {
/* 212 */         Context context = findContext();
/* 213 */         ContextBindings.unbindClassLoader(context, getNamingToken(context), 
/* 214 */           getClass().getClassLoader());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void checkThatConnectorsHaveStarted() {
/* 220 */     for (Connector connector : this.tomcat.getService().findConnectors()) {
/* 221 */       if (LifecycleState.FAILED.equals(connector.getState())) {
/* 222 */         throw new ConnectorStartFailedException(connector.getPort());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void stopSilently() {
/*     */     try {
/* 229 */       stopTomcat();
/*     */     }
/*     */     catch (LifecycleException localLifecycleException) {}
/*     */   }
/*     */   
/*     */ 
/*     */   private void stopTomcat()
/*     */     throws LifecycleException
/*     */   {
/* 238 */     if ((Thread.currentThread().getContextClassLoader() instanceof TomcatEmbeddedWebappClassLoader)) {
/* 239 */       Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
/*     */     }
/* 241 */     this.tomcat.stop();
/*     */   }
/*     */   
/*     */   private void addPreviouslyRemovedConnectors() {
/* 245 */     Service[] services = this.tomcat.getServer().findServices();
/* 246 */     for (Service service : services) {
/* 247 */       Connector[] connectors = (Connector[])this.serviceConnectors.get(service);
/* 248 */       if (connectors != null) {
/* 249 */         for (Connector connector : connectors) {
/* 250 */           service.addConnector(connector);
/* 251 */           if (!this.autoStart) {
/* 252 */             stopProtocolHandler(connector);
/*     */           }
/*     */         }
/* 255 */         this.serviceConnectors.remove(service);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void stopProtocolHandler(Connector connector) {
/*     */     try {
/* 262 */       connector.getProtocolHandler().stop();
/*     */     }
/*     */     catch (Exception ex) {
/* 265 */       logger.error("Cannot pause connector: ", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private void startConnector(Connector connector) {
/*     */     try {
/* 271 */       for (Container child : this.tomcat.getHost().findChildren()) {
/* 272 */         if ((child instanceof TomcatEmbeddedContext)) {
/* 273 */           ((TomcatEmbeddedContext)child).deferredLoadOnStartup();
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/* 278 */       logger.error("Cannot start connector: ", ex);
/* 279 */       throw new EmbeddedServletContainerException("Unable to start embedded Tomcat connectors", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   Map<Service, Connector[]> getServiceConnectors()
/*     */   {
/* 285 */     return this.serviceConnectors;
/*     */   }
/*     */   
/*     */   public void stop() throws EmbeddedServletContainerException
/*     */   {
/* 290 */     synchronized (this.monitor) {
/* 291 */       boolean wasStarted = this.started;
/*     */       try {
/* 293 */         this.started = false;
/*     */         try {
/* 295 */           stopTomcat();
/* 296 */           this.tomcat.destroy();
/*     */ 
/*     */         }
/*     */         catch (LifecycleException localLifecycleException) {}
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 303 */         throw new EmbeddedServletContainerException("Unable to stop embedded Tomcat", ex);
/*     */       }
/*     */       finally
/*     */       {
/* 307 */         if (wasStarted) {
/* 308 */           containerCounter.decrementAndGet();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private String getPortsDescription(boolean localPort) {
/* 315 */     StringBuilder ports = new StringBuilder();
/* 316 */     for (Connector connector : this.tomcat.getService().findConnectors()) {
/* 317 */       ports.append(ports.length() == 0 ? "" : " ");
/* 318 */       int port = localPort ? connector.getLocalPort() : connector.getPort();
/* 319 */       ports.append(port + " (" + connector.getScheme() + ")");
/*     */     }
/* 321 */     return ports.toString();
/*     */   }
/*     */   
/*     */   public int getPort()
/*     */   {
/* 326 */     Connector connector = this.tomcat.getConnector();
/* 327 */     if (connector != null) {
/* 328 */       return connector.getLocalPort();
/*     */     }
/* 330 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Tomcat getTomcat()
/*     */   {
/* 338 */     return this.tomcat;
/*     */   }
/*     */   
/*     */   private Object getNamingToken(Context context) {
/*     */     try {
/* 343 */       return context.getNamingToken();
/*     */     }
/*     */     catch (NoSuchMethodError ex) {}
/*     */     
/* 347 */     return context;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\tomcat\TomcatEmbeddedServletContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */