/*     */ package org.springframework.remoting.rmi;
/*     */ 
/*     */ import java.rmi.AlreadyBoundException;
/*     */ import java.rmi.NoSuchObjectException;
/*     */ import java.rmi.NotBoundException;
/*     */ import java.rmi.Remote;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.registry.LocateRegistry;
/*     */ import java.rmi.registry.Registry;
/*     */ import java.rmi.server.RMIClientSocketFactory;
/*     */ import java.rmi.server.RMIServerSocketFactory;
/*     */ import java.rmi.server.UnicastRemoteObject;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
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
/*     */ public class RmiServiceExporter
/*     */   extends RmiBasedExporter
/*     */   implements InitializingBean, DisposableBean
/*     */ {
/*     */   private String serviceName;
/*  73 */   private int servicePort = 0;
/*     */   
/*     */   private RMIClientSocketFactory clientSocketFactory;
/*     */   
/*     */   private RMIServerSocketFactory serverSocketFactory;
/*     */   
/*     */   private Registry registry;
/*     */   
/*     */   private String registryHost;
/*     */   
/*  83 */   private int registryPort = 1099;
/*     */   
/*     */   private RMIClientSocketFactory registryClientSocketFactory;
/*     */   
/*     */   private RMIServerSocketFactory registryServerSocketFactory;
/*     */   
/*  89 */   private boolean alwaysCreateRegistry = false;
/*     */   
/*  91 */   private boolean replaceExistingBinding = true;
/*     */   
/*     */   private Remote exportedObject;
/*     */   
/*  95 */   private boolean createdRegistry = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServiceName(String serviceName)
/*     */   {
/* 103 */     this.serviceName = serviceName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setServicePort(int servicePort)
/*     */   {
/* 111 */     this.servicePort = servicePort;
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
/*     */   public void setClientSocketFactory(RMIClientSocketFactory clientSocketFactory)
/*     */   {
/* 124 */     this.clientSocketFactory = clientSocketFactory;
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
/*     */   public void setServerSocketFactory(RMIServerSocketFactory serverSocketFactory)
/*     */   {
/* 137 */     this.serverSocketFactory = serverSocketFactory;
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
/*     */   public void setRegistry(Registry registry)
/*     */   {
/* 155 */     this.registry = registry;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRegistryHost(String registryHost)
/*     */   {
/* 164 */     this.registryHost = registryHost;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRegistryPort(int registryPort)
/*     */   {
/* 174 */     this.registryPort = registryPort;
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
/*     */   public void setRegistryClientSocketFactory(RMIClientSocketFactory registryClientSocketFactory)
/*     */   {
/* 187 */     this.registryClientSocketFactory = registryClientSocketFactory;
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
/*     */   public void setRegistryServerSocketFactory(RMIServerSocketFactory registryServerSocketFactory)
/*     */   {
/* 200 */     this.registryServerSocketFactory = registryServerSocketFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAlwaysCreateRegistry(boolean alwaysCreateRegistry)
/*     */   {
/* 211 */     this.alwaysCreateRegistry = alwaysCreateRegistry;
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
/*     */   public void setReplaceExistingBinding(boolean replaceExistingBinding)
/*     */   {
/* 224 */     this.replaceExistingBinding = replaceExistingBinding;
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet()
/*     */     throws RemoteException
/*     */   {
/* 230 */     prepare();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void prepare()
/*     */     throws RemoteException
/*     */   {
/* 239 */     checkService();
/*     */     
/* 241 */     if (this.serviceName == null) {
/* 242 */       throw new IllegalArgumentException("Property 'serviceName' is required");
/*     */     }
/*     */     
/*     */ 
/* 246 */     if ((this.clientSocketFactory instanceof RMIServerSocketFactory)) {
/* 247 */       this.serverSocketFactory = ((RMIServerSocketFactory)this.clientSocketFactory);
/*     */     }
/* 249 */     if (((this.clientSocketFactory != null) && (this.serverSocketFactory == null)) || ((this.clientSocketFactory == null) && (this.serverSocketFactory != null)))
/*     */     {
/* 251 */       throw new IllegalArgumentException("Both RMIClientSocketFactory and RMIServerSocketFactory or none required");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 256 */     if ((this.registryClientSocketFactory instanceof RMIServerSocketFactory)) {
/* 257 */       this.registryServerSocketFactory = ((RMIServerSocketFactory)this.registryClientSocketFactory);
/*     */     }
/* 259 */     if ((this.registryClientSocketFactory == null) && (this.registryServerSocketFactory != null)) {
/* 260 */       throw new IllegalArgumentException("RMIServerSocketFactory without RMIClientSocketFactory for registry not supported");
/*     */     }
/*     */     
/*     */ 
/* 264 */     this.createdRegistry = false;
/*     */     
/*     */ 
/* 267 */     if (this.registry == null) {
/* 268 */       this.registry = getRegistry(this.registryHost, this.registryPort, this.registryClientSocketFactory, this.registryServerSocketFactory);
/*     */       
/* 270 */       this.createdRegistry = true;
/*     */     }
/*     */     
/*     */ 
/* 274 */     this.exportedObject = getObjectToExport();
/*     */     
/* 276 */     if (this.logger.isInfoEnabled()) {
/* 277 */       this.logger.info("Binding service '" + this.serviceName + "' to RMI registry: " + this.registry);
/*     */     }
/*     */     
/*     */ 
/* 281 */     if (this.clientSocketFactory != null) {
/* 282 */       UnicastRemoteObject.exportObject(this.exportedObject, this.servicePort, this.clientSocketFactory, this.serverSocketFactory);
/*     */     }
/*     */     else
/*     */     {
/* 286 */       UnicastRemoteObject.exportObject(this.exportedObject, this.servicePort);
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 291 */       if (this.replaceExistingBinding) {
/* 292 */         this.registry.rebind(this.serviceName, this.exportedObject);
/*     */       }
/*     */       else {
/* 295 */         this.registry.bind(this.serviceName, this.exportedObject);
/*     */       }
/*     */     }
/*     */     catch (AlreadyBoundException ex)
/*     */     {
/* 300 */       unexportObjectSilently();
/*     */       
/* 302 */       throw new IllegalStateException("Already an RMI object bound for name '" + this.serviceName + "': " + ex.toString());
/*     */     }
/*     */     catch (RemoteException ex)
/*     */     {
/* 306 */       unexportObjectSilently();
/* 307 */       throw ex;
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
/*     */   protected Registry getRegistry(String registryHost, int registryPort, RMIClientSocketFactory clientSocketFactory, RMIServerSocketFactory serverSocketFactory)
/*     */     throws RemoteException
/*     */   {
/* 326 */     if (registryHost != null)
/*     */     {
/* 328 */       if (this.logger.isInfoEnabled()) {
/* 329 */         this.logger.info("Looking for RMI registry at port '" + registryPort + "' of host [" + registryHost + "]");
/*     */       }
/* 331 */       Registry reg = LocateRegistry.getRegistry(registryHost, registryPort, clientSocketFactory);
/* 332 */       testRegistry(reg);
/* 333 */       return reg;
/*     */     }
/*     */     
/*     */ 
/* 337 */     return getRegistry(registryPort, clientSocketFactory, serverSocketFactory);
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
/*     */   protected Registry getRegistry(int registryPort, RMIClientSocketFactory clientSocketFactory, RMIServerSocketFactory serverSocketFactory)
/*     */     throws RemoteException
/*     */   {
/* 353 */     if (clientSocketFactory != null) {
/* 354 */       if (this.alwaysCreateRegistry) {
/* 355 */         this.logger.info("Creating new RMI registry");
/* 356 */         return LocateRegistry.createRegistry(registryPort, clientSocketFactory, serverSocketFactory);
/*     */       }
/* 358 */       if (this.logger.isInfoEnabled()) {
/* 359 */         this.logger.info("Looking for RMI registry at port '" + registryPort + "', using custom socket factory");
/*     */       }
/* 361 */       synchronized (LocateRegistry.class)
/*     */       {
/*     */         try {
/* 364 */           Registry reg = LocateRegistry.getRegistry(null, registryPort, clientSocketFactory);
/* 365 */           testRegistry(reg);
/* 366 */           return reg;
/*     */         }
/*     */         catch (RemoteException ex) {
/* 369 */           this.logger.debug("RMI registry access threw exception", ex);
/* 370 */           this.logger.info("Could not detect RMI registry - creating new one");
/*     */           
/* 372 */           return LocateRegistry.createRegistry(registryPort, clientSocketFactory, serverSocketFactory);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 378 */     return getRegistry(registryPort);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Registry getRegistry(int registryPort)
/*     */     throws RemoteException
/*     */   {
/* 389 */     if (this.alwaysCreateRegistry) {
/* 390 */       this.logger.info("Creating new RMI registry");
/* 391 */       return LocateRegistry.createRegistry(registryPort);
/*     */     }
/* 393 */     if (this.logger.isInfoEnabled()) {
/* 394 */       this.logger.info("Looking for RMI registry at port '" + registryPort + "'");
/*     */     }
/* 396 */     synchronized (LocateRegistry.class)
/*     */     {
/*     */       try {
/* 399 */         Registry reg = LocateRegistry.getRegistry(registryPort);
/* 400 */         testRegistry(reg);
/* 401 */         return reg;
/*     */       }
/*     */       catch (RemoteException ex) {
/* 404 */         this.logger.debug("RMI registry access threw exception", ex);
/* 405 */         this.logger.info("Could not detect RMI registry - creating new one");
/*     */         
/* 407 */         return LocateRegistry.createRegistry(registryPort);
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
/*     */   protected void testRegistry(Registry registry)
/*     */     throws RemoteException
/*     */   {
/* 421 */     registry.list();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */     throws RemoteException
/*     */   {
/* 430 */     if (this.logger.isInfoEnabled()) {
/* 431 */       this.logger.info("Unbinding RMI service '" + this.serviceName + "' from registry" + (this.createdRegistry ? " at port '" + this.registryPort + "'" : ""));
/*     */     }
/*     */     try
/*     */     {
/* 435 */       this.registry.unbind(this.serviceName);
/*     */     }
/*     */     catch (NotBoundException ex) {
/* 438 */       if (this.logger.isWarnEnabled()) {
/* 439 */         this.logger.warn("RMI service '" + this.serviceName + "' is not bound to registry" + (this.createdRegistry ? " at port '" + this.registryPort + "' anymore" : ""), ex);
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 444 */       unexportObjectSilently();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void unexportObjectSilently()
/*     */   {
/*     */     try
/*     */     {
/* 453 */       UnicastRemoteObject.unexportObject(this.exportedObject, true);
/*     */     }
/*     */     catch (NoSuchObjectException ex) {
/* 456 */       if (this.logger.isWarnEnabled()) {
/* 457 */         this.logger.warn("RMI object for service '" + this.serviceName + "' isn't exported anymore", ex);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\remoting\rmi\RmiServiceExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */