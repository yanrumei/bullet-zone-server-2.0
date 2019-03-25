/*     */ package org.apache.catalina.mapper;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ import org.apache.catalina.Service;
/*     */ import org.apache.catalina.WebResourceRoot;
/*     */ import org.apache.catalina.Wrapper;
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
/*     */ public class MapperListener
/*     */   extends LifecycleMBeanBase
/*     */   implements ContainerListener, LifecycleListener
/*     */ {
/*  52 */   private static final Log log = LogFactory.getLog(MapperListener.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Mapper mapper;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Service service;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   private static final StringManager sm = StringManager.getManager("org.apache.catalina.mapper");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  76 */   private final String domain = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MapperListener(Service service)
/*     */   {
/*  87 */     this.service = service;
/*  88 */     this.mapper = service.getMapper();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void startInternal()
/*     */     throws LifecycleException
/*     */   {
/*  97 */     setState(LifecycleState.STARTING);
/*     */     
/*  99 */     Engine engine = this.service.getContainer();
/* 100 */     if (engine == null) {
/* 101 */       return;
/*     */     }
/*     */     
/* 104 */     findDefaultHost();
/*     */     
/* 106 */     addListeners(engine);
/*     */     
/* 108 */     Container[] conHosts = engine.findChildren();
/* 109 */     for (Container conHost : conHosts) {
/* 110 */       Host host = (Host)conHost;
/* 111 */       if (!LifecycleState.NEW.equals(host.getState()))
/*     */       {
/* 113 */         registerHost(host);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void stopInternal()
/*     */     throws LifecycleException
/*     */   {
/* 121 */     setState(LifecycleState.STOPPING);
/*     */     
/* 123 */     Engine engine = this.service.getContainer();
/* 124 */     if (engine == null) {
/* 125 */       return;
/*     */     }
/* 127 */     removeListeners(engine);
/*     */   }
/*     */   
/*     */ 
/*     */   protected String getDomainInternal()
/*     */   {
/* 133 */     if ((this.service instanceof LifecycleMBeanBase)) {
/* 134 */       return ((LifecycleMBeanBase)this.service).getDomain();
/*     */     }
/* 136 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getObjectNameKeyProperties()
/*     */   {
/* 144 */     return "type=Mapper";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void containerEvent(ContainerEvent event)
/*     */   {
/* 152 */     if ("addChild".equals(event.getType())) {
/* 153 */       Container child = (Container)event.getData();
/* 154 */       addListeners(child);
/*     */       
/*     */ 
/* 157 */       if (child.getState().isAvailable()) {
/* 158 */         if ((child instanceof Host)) {
/* 159 */           registerHost((Host)child);
/* 160 */         } else if ((child instanceof Context)) {
/* 161 */           registerContext((Context)child);
/* 162 */         } else if ((child instanceof Wrapper))
/*     */         {
/*     */ 
/* 165 */           if (child.getParent().getState().isAvailable()) {
/* 166 */             registerWrapper((Wrapper)child);
/*     */           }
/*     */         }
/*     */       }
/* 170 */     } else if ("removeChild".equals(event.getType())) {
/* 171 */       Container child = (Container)event.getData();
/* 172 */       removeListeners(child);
/*     */ 
/*     */     }
/* 175 */     else if ("addAlias".equals(event.getType()))
/*     */     {
/* 177 */       this.mapper.addHostAlias(((Host)event.getSource()).getName(), event
/* 178 */         .getData().toString());
/* 179 */     } else if ("removeAlias".equals(event.getType()))
/*     */     {
/* 181 */       this.mapper.removeHostAlias(event.getData().toString());
/* 182 */     } else if ("addMapping".equals(event.getType()))
/*     */     {
/* 184 */       Wrapper wrapper = (Wrapper)event.getSource();
/* 185 */       Context context = (Context)wrapper.getParent();
/* 186 */       String contextPath = context.getPath();
/* 187 */       if ("/".equals(contextPath)) {
/* 188 */         contextPath = "";
/*     */       }
/* 190 */       String version = context.getWebappVersion();
/* 191 */       String hostName = context.getParent().getName();
/* 192 */       String wrapperName = wrapper.getName();
/* 193 */       String mapping = (String)event.getData();
/*     */       
/* 195 */       boolean jspWildCard = ("jsp".equals(wrapperName)) && (mapping.endsWith("/*"));
/* 196 */       this.mapper.addWrapper(hostName, contextPath, version, mapping, wrapper, jspWildCard, context
/* 197 */         .isResourceOnlyServlet(wrapperName));
/* 198 */     } else if ("removeMapping".equals(event.getType()))
/*     */     {
/* 200 */       Wrapper wrapper = (Wrapper)event.getSource();
/*     */       
/* 202 */       Context context = (Context)wrapper.getParent();
/* 203 */       String contextPath = context.getPath();
/* 204 */       if ("/".equals(contextPath)) {
/* 205 */         contextPath = "";
/*     */       }
/* 207 */       String version = context.getWebappVersion();
/* 208 */       String hostName = context.getParent().getName();
/*     */       
/* 210 */       String mapping = (String)event.getData();
/*     */       
/* 212 */       this.mapper.removeWrapper(hostName, contextPath, version, mapping);
/* 213 */     } else if ("addWelcomeFile".equals(event.getType()))
/*     */     {
/* 215 */       Context context = (Context)event.getSource();
/*     */       
/* 217 */       String hostName = context.getParent().getName();
/*     */       
/* 219 */       String contextPath = context.getPath();
/* 220 */       if ("/".equals(contextPath)) {
/* 221 */         contextPath = "";
/*     */       }
/*     */       
/* 224 */       String welcomeFile = (String)event.getData();
/*     */       
/* 226 */       this.mapper.addWelcomeFile(hostName, contextPath, context
/* 227 */         .getWebappVersion(), welcomeFile);
/* 228 */     } else if ("removeWelcomeFile".equals(event.getType()))
/*     */     {
/* 230 */       Context context = (Context)event.getSource();
/*     */       
/* 232 */       String hostName = context.getParent().getName();
/*     */       
/* 234 */       String contextPath = context.getPath();
/* 235 */       if ("/".equals(contextPath)) {
/* 236 */         contextPath = "";
/*     */       }
/*     */       
/* 239 */       String welcomeFile = (String)event.getData();
/*     */       
/* 241 */       this.mapper.removeWelcomeFile(hostName, contextPath, context
/* 242 */         .getWebappVersion(), welcomeFile);
/* 243 */     } else if ("clearWelcomeFiles".equals(event.getType()))
/*     */     {
/* 245 */       Context context = (Context)event.getSource();
/*     */       
/* 247 */       String hostName = context.getParent().getName();
/*     */       
/* 249 */       String contextPath = context.getPath();
/* 250 */       if ("/".equals(contextPath)) {
/* 251 */         contextPath = "";
/*     */       }
/*     */       
/* 254 */       this.mapper.clearWelcomeFiles(hostName, contextPath, context
/* 255 */         .getWebappVersion());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void findDefaultHost()
/*     */   {
/* 264 */     Engine engine = this.service.getContainer();
/* 265 */     String defaultHost = engine.getDefaultHost();
/*     */     
/* 267 */     boolean found = false;
/*     */     
/* 269 */     if ((defaultHost != null) && (defaultHost.length() > 0)) {
/* 270 */       Container[] containers = engine.findChildren();
/*     */       
/* 272 */       for (Container container : containers) {
/* 273 */         Host host = (Host)container;
/* 274 */         if (defaultHost.equalsIgnoreCase(host.getName())) {
/* 275 */           found = true;
/* 276 */           break;
/*     */         }
/*     */         
/* 279 */         String[] aliases = host.findAliases();
/* 280 */         for (String alias : aliases) {
/* 281 */           if (defaultHost.equalsIgnoreCase(alias)) {
/* 282 */             found = true;
/* 283 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 289 */     if (found) {
/* 290 */       this.mapper.setDefaultHostName(defaultHost);
/*     */     } else {
/* 292 */       log.warn(sm.getString("mapperListener.unknownDefaultHost", new Object[] { defaultHost, this.service }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void registerHost(Host host)
/*     */   {
/* 303 */     String[] aliases = host.findAliases();
/* 304 */     this.mapper.addHost(host.getName(), aliases, host);
/*     */     
/* 306 */     for (Container container : host.findChildren()) {
/* 307 */       if (container.getState().isAvailable()) {
/* 308 */         registerContext((Context)container);
/*     */       }
/*     */     }
/* 311 */     if (log.isDebugEnabled()) {
/* 312 */       log.debug(sm.getString("mapperListener.registerHost", new Object[] {host
/* 313 */         .getName(), this.domain, this.service }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void unregisterHost(Host host)
/*     */   {
/* 323 */     String hostname = host.getName();
/*     */     
/* 325 */     this.mapper.removeHost(hostname);
/*     */     
/* 327 */     if (log.isDebugEnabled()) {
/* 328 */       log.debug(sm.getString("mapperListener.unregisterHost", new Object[] { hostname, this.domain, this.service }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void unregisterWrapper(Wrapper wrapper)
/*     */   {
/* 339 */     Context context = (Context)wrapper.getParent();
/* 340 */     String contextPath = context.getPath();
/* 341 */     String wrapperName = wrapper.getName();
/*     */     
/* 343 */     if ("/".equals(contextPath)) {
/* 344 */       contextPath = "";
/*     */     }
/* 346 */     String version = context.getWebappVersion();
/* 347 */     String hostName = context.getParent().getName();
/*     */     
/* 349 */     String[] mappings = wrapper.findMappings();
/*     */     
/* 351 */     for (String mapping : mappings) {
/* 352 */       this.mapper.removeWrapper(hostName, contextPath, version, mapping);
/*     */     }
/*     */     
/* 355 */     if (log.isDebugEnabled()) {
/* 356 */       log.debug(sm.getString("mapperListener.unregisterWrapper", new Object[] { wrapperName, contextPath, this.service }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void registerContext(Context context)
/*     */   {
/* 367 */     String contextPath = context.getPath();
/* 368 */     if ("/".equals(contextPath)) {
/* 369 */       contextPath = "";
/*     */     }
/* 371 */     Host host = (Host)context.getParent();
/*     */     
/* 373 */     WebResourceRoot resources = context.getResources();
/* 374 */     String[] welcomeFiles = context.findWelcomeFiles();
/* 375 */     List<WrapperMappingInfo> wrappers = new ArrayList();
/*     */     
/* 377 */     for (Container container : context.findChildren()) {
/* 378 */       prepareWrapperMappingInfo(context, (Wrapper)container, wrappers);
/*     */       
/* 380 */       if (log.isDebugEnabled()) {
/* 381 */         log.debug(sm.getString("mapperListener.registerWrapper", new Object[] {container
/* 382 */           .getName(), contextPath, this.service }));
/*     */       }
/*     */     }
/*     */     
/* 386 */     this.mapper.addContextVersion(host.getName(), host, contextPath, context
/* 387 */       .getWebappVersion(), context, welcomeFiles, resources, wrappers);
/*     */     
/*     */ 
/* 390 */     if (log.isDebugEnabled()) {
/* 391 */       log.debug(sm.getString("mapperListener.registerContext", new Object[] { contextPath, this.service }));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void unregisterContext(Context context)
/*     */   {
/* 402 */     String contextPath = context.getPath();
/* 403 */     if ("/".equals(contextPath)) {
/* 404 */       contextPath = "";
/*     */     }
/* 406 */     String hostName = context.getParent().getName();
/*     */     
/* 408 */     if (context.getPaused()) {
/* 409 */       if (log.isDebugEnabled()) {
/* 410 */         log.debug(sm.getString("mapperListener.pauseContext", new Object[] { contextPath, this.service }));
/*     */       }
/*     */       
/*     */ 
/* 414 */       this.mapper.pauseContextVersion(context, hostName, contextPath, context
/* 415 */         .getWebappVersion());
/*     */     } else {
/* 417 */       if (log.isDebugEnabled()) {
/* 418 */         log.debug(sm.getString("mapperListener.unregisterContext", new Object[] { contextPath, this.service }));
/*     */       }
/*     */       
/*     */ 
/* 422 */       this.mapper.removeContextVersion(context, hostName, contextPath, context
/* 423 */         .getWebappVersion());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void registerWrapper(Wrapper wrapper)
/*     */   {
/* 433 */     Context context = (Context)wrapper.getParent();
/* 434 */     String contextPath = context.getPath();
/* 435 */     if ("/".equals(contextPath)) {
/* 436 */       contextPath = "";
/*     */     }
/* 438 */     String version = context.getWebappVersion();
/* 439 */     String hostName = context.getParent().getName();
/*     */     
/* 441 */     List<WrapperMappingInfo> wrappers = new ArrayList();
/* 442 */     prepareWrapperMappingInfo(context, wrapper, wrappers);
/* 443 */     this.mapper.addWrappers(hostName, contextPath, version, wrappers);
/*     */     
/* 445 */     if (log.isDebugEnabled()) {
/* 446 */       log.debug(sm.getString("mapperListener.registerWrapper", new Object[] {wrapper
/* 447 */         .getName(), contextPath, this.service }));
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
/*     */   private void prepareWrapperMappingInfo(Context context, Wrapper wrapper, List<WrapperMappingInfo> wrappers)
/*     */   {
/* 461 */     String wrapperName = wrapper.getName();
/* 462 */     boolean resourceOnly = context.isResourceOnlyServlet(wrapperName);
/* 463 */     String[] mappings = wrapper.findMappings();
/* 464 */     for (String mapping : mappings)
/*     */     {
/* 466 */       boolean jspWildCard = (wrapperName.equals("jsp")) && (mapping.endsWith("/*"));
/* 467 */       wrappers.add(new WrapperMappingInfo(mapping, wrapper, jspWildCard, resourceOnly));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void lifecycleEvent(LifecycleEvent event)
/*     */   {
/* 474 */     if (event.getType().equals("after_start")) {
/* 475 */       Object obj = event.getSource();
/* 476 */       if ((obj instanceof Wrapper)) {
/* 477 */         Wrapper w = (Wrapper)obj;
/*     */         
/*     */ 
/* 480 */         if (w.getParent().getState().isAvailable()) {
/* 481 */           registerWrapper(w);
/*     */         }
/* 483 */       } else if ((obj instanceof Context)) {
/* 484 */         Context c = (Context)obj;
/*     */         
/*     */ 
/* 487 */         if (c.getParent().getState().isAvailable()) {
/* 488 */           registerContext(c);
/*     */         }
/* 490 */       } else if ((obj instanceof Host)) {
/* 491 */         registerHost((Host)obj);
/*     */       }
/* 493 */     } else if (event.getType().equals("before_stop")) {
/* 494 */       Object obj = event.getSource();
/* 495 */       if ((obj instanceof Wrapper)) {
/* 496 */         unregisterWrapper((Wrapper)obj);
/* 497 */       } else if ((obj instanceof Context)) {
/* 498 */         unregisterContext((Context)obj);
/* 499 */       } else if ((obj instanceof Host)) {
/* 500 */         unregisterHost((Host)obj);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addListeners(Container container)
/*     */   {
/* 512 */     container.addContainerListener(this);
/* 513 */     container.addLifecycleListener(this);
/* 514 */     for (Container child : container.findChildren()) {
/* 515 */       addListeners(child);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void removeListeners(Container container)
/*     */   {
/* 526 */     container.removeContainerListener(this);
/* 527 */     container.removeLifecycleListener(this);
/* 528 */     for (Container child : container.findChildren()) {
/* 529 */       removeListeners(child);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\mapper\MapperListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */