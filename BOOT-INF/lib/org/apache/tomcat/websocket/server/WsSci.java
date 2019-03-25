/*     */ package org.apache.tomcat.websocket.server;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.servlet.ServletContainerInitializer;
/*     */ import javax.servlet.ServletContext;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.annotation.HandlesTypes;
/*     */ import javax.websocket.ContainerProvider;
/*     */ import javax.websocket.DeploymentException;
/*     */ import javax.websocket.Endpoint;
/*     */ import javax.websocket.server.ServerApplicationConfig;
/*     */ import javax.websocket.server.ServerEndpoint;
/*     */ import javax.websocket.server.ServerEndpointConfig;
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
/*     */ @HandlesTypes({ServerEndpoint.class, ServerApplicationConfig.class, Endpoint.class})
/*     */ public class WsSci
/*     */   implements ServletContainerInitializer
/*     */ {
/*     */   public void onStartup(Set<Class<?>> clazzes, ServletContext ctx)
/*     */     throws ServletException
/*     */   {
/*  47 */     WsServerContainer sc = init(ctx, true);
/*     */     
/*  49 */     if ((clazzes == null) || (clazzes.size() == 0)) {
/*  50 */       return;
/*     */     }
/*     */     
/*     */ 
/*  54 */     Set<ServerApplicationConfig> serverApplicationConfigs = new HashSet();
/*  55 */     Set<Class<? extends Endpoint>> scannedEndpointClazzes = new HashSet();
/*  56 */     Set<Class<?>> scannedPojoEndpoints = new HashSet();
/*     */     Class<?> clazz;
/*     */     try
/*     */     {
/*  60 */       wsPackage = ContainerProvider.class.getName();
/*  61 */       wsPackage = wsPackage.substring(0, wsPackage.lastIndexOf('.') + 1);
/*  62 */       for (localIterator = clazzes.iterator(); localIterator.hasNext();) { clazz = (Class)localIterator.next();
/*  63 */         int modifiers = clazz.getModifiers();
/*  64 */         if ((Modifier.isPublic(modifiers)) && 
/*  65 */           (!Modifier.isAbstract(modifiers)) && 
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*  70 */           (!clazz.getName().startsWith(wsPackage)))
/*     */         {
/*     */ 
/*  73 */           if (ServerApplicationConfig.class.isAssignableFrom(clazz)) {
/*  74 */             serverApplicationConfigs.add(
/*  75 */               (ServerApplicationConfig)clazz.getConstructor(new Class[0]).newInstance(new Object[0]));
/*     */           }
/*  77 */           if (Endpoint.class.isAssignableFrom(clazz))
/*     */           {
/*  79 */             Class<? extends Endpoint> endpoint = clazz;
/*     */             
/*  81 */             scannedEndpointClazzes.add(endpoint);
/*     */           }
/*  83 */           if (clazz.isAnnotationPresent(ServerEndpoint.class))
/*  84 */             scannedPojoEndpoints.add(clazz);
/*     */         }
/*     */       } } catch (ReflectiveOperationException e) { String wsPackage;
/*     */       Iterator localIterator;
/*  88 */       throw new ServletException(e);
/*     */     }
/*     */     
/*     */ 
/*  92 */     Set<ServerEndpointConfig> filteredEndpointConfigs = new HashSet();
/*  93 */     Object filteredPojoEndpoints = new HashSet();
/*     */     
/*  95 */     if (serverApplicationConfigs.isEmpty()) {
/*  96 */       ((Set)filteredPojoEndpoints).addAll(scannedPojoEndpoints);
/*     */     } else {
/*  98 */       for (ServerApplicationConfig config : serverApplicationConfigs)
/*     */       {
/* 100 */         Set<ServerEndpointConfig> configFilteredEndpoints = config.getEndpointConfigs(scannedEndpointClazzes);
/* 101 */         if (configFilteredEndpoints != null) {
/* 102 */           filteredEndpointConfigs.addAll(configFilteredEndpoints);
/*     */         }
/*     */         
/* 105 */         Set<Class<?>> configFilteredPojos = config.getAnnotatedEndpointClasses(scannedPojoEndpoints);
/*     */         
/* 107 */         if (configFilteredPojos != null) {
/* 108 */           ((Set)filteredPojoEndpoints).addAll(configFilteredPojos);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     try
/*     */     {
/* 115 */       for (ServerEndpointConfig config : filteredEndpointConfigs) {
/* 116 */         sc.addEndpoint(config);
/*     */       }
/*     */       
/* 119 */       for (Class<?> clazz : (Set)filteredPojoEndpoints) {
/* 120 */         sc.addEndpoint(clazz);
/*     */       }
/*     */     } catch (DeploymentException e) {
/* 123 */       throw new ServletException(e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static WsServerContainer init(ServletContext servletContext, boolean initBySciMechanism)
/*     */   {
/* 131 */     WsServerContainer sc = new WsServerContainer(servletContext);
/*     */     
/* 133 */     servletContext.setAttribute("javax.websocket.server.ServerContainer", sc);
/*     */     
/*     */ 
/* 136 */     servletContext.addListener(new WsSessionListener(sc));
/*     */     
/*     */ 
/* 139 */     if (initBySciMechanism) {
/* 140 */       servletContext.addListener(new WsContextListener());
/*     */     }
/*     */     
/* 143 */     return sc;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-websocket-8.5.27.jar!\org\apache\tomcat\websocket\server\WsSci.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */