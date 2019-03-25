/*     */ package org.springframework.boot.context.embedded.jetty;
/*     */ 
/*     */ import java.net.BindException;
/*     */ import java.util.List;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.eclipse.jetty.server.Connector;
/*     */ import org.eclipse.jetty.server.Handler;
/*     */ import org.eclipse.jetty.server.NetworkConnector;
/*     */ import org.eclipse.jetty.server.Server;
/*     */ import org.eclipse.jetty.server.handler.HandlerCollection;
/*     */ import org.eclipse.jetty.server.handler.HandlerWrapper;
/*     */ import org.eclipse.jetty.util.component.AbstractLifeCycle;
/*     */ import org.springframework.boot.context.embedded.EmbeddedServletContainer;
/*     */ import org.springframework.boot.context.embedded.EmbeddedServletContainerException;
/*     */ import org.springframework.boot.context.embedded.PortInUseException;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class JettyEmbeddedServletContainer
/*     */   implements EmbeddedServletContainer
/*     */ {
/*  53 */   private static final Log logger = LogFactory.getLog(JettyEmbeddedServletContainer.class);
/*     */   
/*  55 */   private final Object monitor = new Object();
/*     */   
/*     */ 
/*     */   private final Server server;
/*     */   
/*     */ 
/*     */   private final boolean autoStart;
/*     */   
/*     */   private Connector[] connectors;
/*     */   
/*     */   private volatile boolean started;
/*     */   
/*     */ 
/*     */   public JettyEmbeddedServletContainer(Server server)
/*     */   {
/*  70 */     this(server, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JettyEmbeddedServletContainer(Server server, boolean autoStart)
/*     */   {
/*  79 */     this.autoStart = autoStart;
/*  80 */     Assert.notNull(server, "Jetty Server must not be null");
/*  81 */     this.server = server;
/*  82 */     initialize();
/*     */   }
/*     */   
/*     */   private void initialize() {
/*  86 */     synchronized (this.monitor)
/*     */     {
/*     */       try
/*     */       {
/*  90 */         this.connectors = this.server.getConnectors();
/*  91 */         this.server.addBean(new AbstractLifeCycle()
/*     */         {
/*     */           protected void doStart() throws Exception
/*     */           {
/*  95 */             for (Connector connector : JettyEmbeddedServletContainer.this.connectors) {
/*  96 */               Assert.state(connector.isStopped(), "Connector " + connector + " has been started prematurely");
/*     */             }
/*     */             
/*  99 */             JettyEmbeddedServletContainer.this.server.setConnectors(null);
/*     */           }
/*     */           
/*     */ 
/* 103 */         });
/* 104 */         this.server.start();
/* 105 */         this.server.setStopAtShutdown(false);
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 109 */         stopSilently();
/* 110 */         throw new EmbeddedServletContainerException("Unable to start embedded Jetty servlet container", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void stopSilently()
/*     */   {
/*     */     try {
/* 118 */       this.server.stop();
/*     */     }
/*     */     catch (Exception localException) {}
/*     */   }
/*     */   
/*     */ 
/*     */   public void start()
/*     */     throws EmbeddedServletContainerException
/*     */   {
/* 127 */     synchronized (this.monitor) {
/* 128 */       if (this.started) {
/* 129 */         return;
/*     */       }
/* 131 */       this.server.setConnectors(this.connectors);
/* 132 */       if (!this.autoStart) {
/* 133 */         return;
/*     */       }
/*     */       try {
/* 136 */         this.server.start();
/* 137 */         Handler[] arrayOfHandler = this.server.getHandlers();int i = arrayOfHandler.length; for (Handler localHandler1 = 0; localHandler1 < i; localHandler1++) { handler = arrayOfHandler[localHandler1];
/* 138 */           handleDeferredInitialize(new Handler[] { handler });
/*     */         }
/* 140 */         Connector[] connectors = this.server.getConnectors();
/* 141 */         Connector[] arrayOfConnector1 = connectors;localHandler1 = arrayOfConnector1.length; for (Handler handler = 0; handler < localHandler1; handler++) { Connector connector = arrayOfConnector1[handler];
/*     */           try {
/* 143 */             connector.start();
/*     */           }
/*     */           catch (BindException ex) {
/* 146 */             if ((connector instanceof NetworkConnector))
/*     */             {
/* 148 */               throw new PortInUseException(((NetworkConnector)connector).getPort());
/*     */             }
/* 150 */             throw ex;
/*     */           }
/*     */         }
/* 153 */         this.started = true;
/* 154 */         logger
/* 155 */           .info("Jetty started on port(s) " + getActualPortsDescription());
/*     */       }
/*     */       catch (EmbeddedServletContainerException ex) {
/* 158 */         throw ex;
/*     */       }
/*     */       catch (Exception ex) {
/* 161 */         throw new EmbeddedServletContainerException("Unable to start embedded Jetty servlet container", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private String getActualPortsDescription()
/*     */   {
/* 168 */     StringBuilder ports = new StringBuilder();
/* 169 */     for (Connector connector : this.server.getConnectors()) {
/* 170 */       ports.append(ports.length() == 0 ? "" : ", ");
/* 171 */       ports.append(getLocalPort(connector) + getProtocols(connector));
/*     */     }
/* 173 */     return ports.toString();
/*     */   }
/*     */   
/*     */   private Integer getLocalPort(Connector connector)
/*     */   {
/*     */     try {
/* 179 */       return (Integer)ReflectionUtils.invokeMethod(
/* 180 */         ReflectionUtils.findMethod(connector.getClass(), "getLocalPort"), connector);
/*     */ 
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 185 */       logger.info("could not determine port ( " + ex.getMessage() + ")"); }
/* 186 */     return Integer.valueOf(0);
/*     */   }
/*     */   
/*     */   private String getProtocols(Connector connector)
/*     */   {
/*     */     try {
/* 192 */       List<String> protocols = connector.getProtocols();
/* 193 */       return " (" + StringUtils.collectionToDelimitedString(protocols, ", ") + ")";
/*     */     }
/*     */     catch (NoSuchMethodError ex) {}
/*     */     
/* 197 */     return "";
/*     */   }
/*     */   
/*     */   private void handleDeferredInitialize(Handler... handlers)
/*     */     throws Exception
/*     */   {
/* 203 */     for (Handler handler : handlers) {
/* 204 */       if ((handler instanceof JettyEmbeddedWebAppContext)) {
/* 205 */         ((JettyEmbeddedWebAppContext)handler).deferredInitialize();
/*     */       }
/* 207 */       else if ((handler instanceof HandlerWrapper)) {
/* 208 */         handleDeferredInitialize(new Handler[] { ((HandlerWrapper)handler).getHandler() });
/*     */       }
/* 210 */       else if ((handler instanceof HandlerCollection)) {
/* 211 */         handleDeferredInitialize(((HandlerCollection)handler).getHandlers());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void stop()
/*     */   {
/* 218 */     synchronized (this.monitor) {
/* 219 */       this.started = false;
/*     */       try {
/* 221 */         this.server.stop();
/*     */       }
/*     */       catch (InterruptedException ex) {
/* 224 */         Thread.currentThread().interrupt();
/*     */       }
/*     */       catch (Exception ex) {
/* 227 */         throw new EmbeddedServletContainerException("Unable to stop embedded Jetty servlet container", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int getPort()
/*     */   {
/* 235 */     Connector[] connectors = this.server.getConnectors();
/* 236 */     Connector[] arrayOfConnector1 = connectors;int i = arrayOfConnector1.length;int j = 0; if (j < i) { Connector connector = arrayOfConnector1[j];
/*     */       
/* 238 */       return getLocalPort(connector).intValue();
/*     */     }
/* 240 */     return 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Server getServer()
/*     */   {
/* 248 */     return this.server;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\jetty\JettyEmbeddedServletContainer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */