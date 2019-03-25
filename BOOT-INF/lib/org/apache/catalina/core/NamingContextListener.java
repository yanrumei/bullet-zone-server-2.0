/*      */ package org.apache.catalina.core;
/*      */ 
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.StringTokenizer;
/*      */ import javax.management.MalformedObjectNameException;
/*      */ import javax.management.ObjectName;
/*      */ import javax.naming.NameAlreadyBoundException;
/*      */ import javax.naming.NamingException;
/*      */ import javax.naming.Reference;
/*      */ import javax.naming.StringRefAddr;
/*      */ import javax.servlet.ServletContext;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.ContainerEvent;
/*      */ import org.apache.catalina.ContainerListener;
/*      */ import org.apache.catalina.Engine;
/*      */ import org.apache.catalina.Host;
/*      */ import org.apache.catalina.LifecycleEvent;
/*      */ import org.apache.catalina.LifecycleListener;
/*      */ import org.apache.catalina.Loader;
/*      */ import org.apache.catalina.Server;
/*      */ import org.apache.catalina.Service;
/*      */ import org.apache.catalina.deploy.NamingResourcesImpl;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.naming.ContextAccessController;
/*      */ import org.apache.naming.ContextBindings;
/*      */ import org.apache.naming.EjbRef;
/*      */ import org.apache.naming.HandlerRef;
/*      */ import org.apache.naming.NamingContext;
/*      */ import org.apache.naming.ResourceEnvRef;
/*      */ import org.apache.naming.ResourceLinkRef;
/*      */ import org.apache.naming.ResourceRef;
/*      */ import org.apache.naming.ServiceRef;
/*      */ import org.apache.naming.TransactionRef;
/*      */ import org.apache.naming.factory.ResourceLinkFactory;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextEjb;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextEnvironment;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextHandler;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextLocalEjb;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextResource;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextResourceEnvRef;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextResourceLink;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextService;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextTransaction;
/*      */ import org.apache.tomcat.util.modeler.Registry;
/*      */ import org.apache.tomcat.util.res.StringManager;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class NamingContextListener
/*      */   implements LifecycleListener, ContainerListener, PropertyChangeListener
/*      */ {
/*   85 */   private static final Log log = LogFactory.getLog(NamingContextListener.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   93 */   protected String name = "/";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   99 */   protected Object container = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  104 */   private Object token = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  109 */   protected boolean initialized = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  115 */   protected NamingResourcesImpl namingResources = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  121 */   protected NamingContext namingContext = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  127 */   protected javax.naming.Context compCtx = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  133 */   protected javax.naming.Context envCtx = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  139 */   protected HashMap<String, ObjectName> objectNames = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  146 */   private boolean exceptionOnFailedWrite = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  153 */   protected static final StringManager sm = StringManager.getManager("org.apache.catalina.core");
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getExceptionOnFailedWrite()
/*      */   {
/*  163 */     return this.exceptionOnFailedWrite;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setExceptionOnFailedWrite(boolean exceptionOnFailedWrite)
/*      */   {
/*  174 */     this.exceptionOnFailedWrite = exceptionOnFailedWrite;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/*  182 */     return this.name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setName(String name)
/*      */   {
/*  192 */     this.name = name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public javax.naming.Context getEnvContext()
/*      */   {
/*  200 */     return this.envCtx;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void lifecycleEvent(LifecycleEvent event)
/*      */   {
/*  214 */     this.container = event.getLifecycle();
/*      */     
/*  216 */     if ((this.container instanceof org.apache.catalina.Context)) {
/*  217 */       this.namingResources = ((org.apache.catalina.Context)this.container).getNamingResources();
/*  218 */       this.token = ((org.apache.catalina.Context)this.container).getNamingToken();
/*  219 */     } else if ((this.container instanceof Server)) {
/*  220 */       this.namingResources = ((Server)this.container).getGlobalNamingResources();
/*  221 */       this.token = ((Server)this.container).getNamingToken();
/*      */     } else {
/*  223 */       return;
/*      */     }
/*      */     
/*  226 */     if ("configure_start".equals(event.getType()))
/*      */     {
/*  228 */       if (this.initialized) {
/*  229 */         return;
/*      */       }
/*      */       try {
/*  232 */         Hashtable<String, Object> contextEnv = new Hashtable();
/*  233 */         this.namingContext = new NamingContext(contextEnv, getName());
/*  234 */         ContextAccessController.setSecurityToken(getName(), this.token);
/*  235 */         ContextAccessController.setSecurityToken(this.container, this.token);
/*  236 */         ContextBindings.bindContext(this.container, this.namingContext, this.token);
/*  237 */         if (log.isDebugEnabled()) {
/*  238 */           log.debug("Bound " + this.container);
/*      */         }
/*      */         
/*      */ 
/*  242 */         this.namingContext.setExceptionOnFailedWrite(
/*  243 */           getExceptionOnFailedWrite());
/*      */         
/*      */ 
/*  246 */         ContextAccessController.setWritable(getName(), this.token);
/*      */         try
/*      */         {
/*  249 */           createNamingContext();
/*      */         }
/*      */         catch (NamingException e) {
/*  252 */           log.error(sm.getString("naming.namingContextCreationFailed", new Object[] { e }));
/*      */         }
/*      */         
/*  255 */         this.namingResources.addPropertyChangeListener(this);
/*      */         
/*      */ 
/*  258 */         if ((this.container instanceof org.apache.catalina.Context))
/*      */         {
/*  260 */           ContextAccessController.setReadOnly(getName());
/*      */           try {
/*  262 */             ContextBindings.bindClassLoader(this.container, this.token, ((org.apache.catalina.Context)this.container)
/*  263 */               .getLoader().getClassLoader());
/*      */           } catch (NamingException e) {
/*  265 */             log.error(sm.getString("naming.bindFailed", new Object[] { e }));
/*      */           }
/*      */         }
/*      */         
/*  269 */         if ((this.container instanceof Server))
/*      */         {
/*  271 */           ResourceLinkFactory.setGlobalContext(this.namingContext);
/*      */           try {
/*  273 */             ContextBindings.bindClassLoader(this.container, this.token, 
/*  274 */               getClass().getClassLoader());
/*      */           } catch (NamingException e) {
/*  276 */             log.error(sm.getString("naming.bindFailed", new Object[] { e }));
/*      */           }
/*  278 */           if ((this.container instanceof StandardServer))
/*      */           {
/*  280 */             ((StandardServer)this.container).setGlobalNamingContext(this.namingContext);
/*      */           }
/*      */         }
/*      */       }
/*      */       finally
/*      */       {
/*  286 */         this.initialized = true;
/*      */       }
/*      */     }
/*  289 */     else if ("configure_stop".equals(event.getType()))
/*      */     {
/*  291 */       if (!this.initialized) {
/*  292 */         return;
/*      */       }
/*      */       try
/*      */       {
/*  296 */         ContextAccessController.setWritable(getName(), this.token);
/*  297 */         ContextBindings.unbindContext(this.container, this.token);
/*      */         
/*  299 */         if ((this.container instanceof org.apache.catalina.Context)) {
/*  300 */           ContextBindings.unbindClassLoader(this.container, this.token, ((org.apache.catalina.Context)this.container)
/*  301 */             .getLoader().getClassLoader());
/*      */         }
/*      */         
/*  304 */         if ((this.container instanceof Server)) {
/*  305 */           this.namingResources.removePropertyChangeListener(this);
/*  306 */           ContextBindings.unbindClassLoader(this.container, this.token, 
/*  307 */             getClass().getClassLoader());
/*      */         }
/*      */         
/*  310 */         ContextAccessController.unsetSecurityToken(getName(), this.token);
/*  311 */         ContextAccessController.unsetSecurityToken(this.container, this.token);
/*      */         
/*      */         Registry registry;
/*  314 */         if (!this.objectNames.isEmpty()) {
/*  315 */           Collection<ObjectName> names = this.objectNames.values();
/*  316 */           registry = Registry.getRegistry(null, null);
/*  317 */           for (ObjectName objectName : names) {
/*  318 */             registry.unregisterComponent(objectName);
/*      */           }
/*      */         }
/*      */         
/*  322 */         javax.naming.Context global = getGlobalNamingContext();
/*  323 */         if (global != null) {
/*  324 */           ResourceLinkFactory.deregisterGlobalResourceAccess(global);
/*      */         }
/*      */       } finally {
/*  327 */         this.objectNames.clear();
/*      */         
/*  329 */         this.namingContext = null;
/*  330 */         this.envCtx = null;
/*  331 */         this.compCtx = null;
/*  332 */         this.initialized = false;
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void containerEvent(ContainerEvent event)
/*      */   {
/*  353 */     if (!this.initialized) {
/*  354 */       return;
/*      */     }
/*      */     
/*  357 */     ContextAccessController.setWritable(getName(), this.token);
/*      */     
/*  359 */     String type = event.getType();
/*      */     
/*  361 */     if (type.equals("addEjb"))
/*      */     {
/*  363 */       String ejbName = (String)event.getData();
/*  364 */       if (ejbName != null) {
/*  365 */         ContextEjb ejb = this.namingResources.findEjb(ejbName);
/*  366 */         addEjb(ejb);
/*      */       }
/*      */     }
/*  369 */     else if (type.equals("addEnvironment"))
/*      */     {
/*  371 */       String environmentName = (String)event.getData();
/*  372 */       if (environmentName != null)
/*      */       {
/*  374 */         ContextEnvironment env = this.namingResources.findEnvironment(environmentName);
/*  375 */         addEnvironment(env);
/*      */       }
/*      */     }
/*  378 */     else if (type.equals("addLocalEjb"))
/*      */     {
/*  380 */       String localEjbName = (String)event.getData();
/*  381 */       if (localEjbName != null)
/*      */       {
/*  383 */         ContextLocalEjb localEjb = this.namingResources.findLocalEjb(localEjbName);
/*  384 */         addLocalEjb(localEjb);
/*      */       }
/*      */     }
/*  387 */     else if (type.equals("addResource"))
/*      */     {
/*  389 */       String resourceName = (String)event.getData();
/*  390 */       if (resourceName != null)
/*      */       {
/*  392 */         ContextResource resource = this.namingResources.findResource(resourceName);
/*  393 */         addResource(resource);
/*      */       }
/*      */     }
/*  396 */     else if (type.equals("addResourceLink"))
/*      */     {
/*  398 */       String resourceLinkName = (String)event.getData();
/*  399 */       if (resourceLinkName != null)
/*      */       {
/*  401 */         ContextResourceLink resourceLink = this.namingResources.findResourceLink(resourceLinkName);
/*  402 */         addResourceLink(resourceLink);
/*      */       }
/*      */     }
/*  405 */     else if (type.equals("addResourceEnvRef"))
/*      */     {
/*  407 */       String resourceEnvRefName = (String)event.getData();
/*  408 */       if (resourceEnvRefName != null)
/*      */       {
/*  410 */         ContextResourceEnvRef resourceEnvRef = this.namingResources.findResourceEnvRef(resourceEnvRefName);
/*  411 */         addResourceEnvRef(resourceEnvRef);
/*      */       }
/*      */     }
/*  414 */     else if (type.equals("addService"))
/*      */     {
/*  416 */       String serviceName = (String)event.getData();
/*  417 */       if (serviceName != null)
/*      */       {
/*  419 */         ContextService service = this.namingResources.findService(serviceName);
/*  420 */         addService(service);
/*      */       }
/*      */     }
/*  423 */     else if (type.equals("removeEjb"))
/*      */     {
/*  425 */       String ejbName = (String)event.getData();
/*  426 */       if (ejbName != null) {
/*  427 */         removeEjb(ejbName);
/*      */       }
/*      */     }
/*  430 */     else if (type.equals("removeEnvironment"))
/*      */     {
/*  432 */       String environmentName = (String)event.getData();
/*  433 */       if (environmentName != null) {
/*  434 */         removeEnvironment(environmentName);
/*      */       }
/*      */     }
/*  437 */     else if (type.equals("removeLocalEjb"))
/*      */     {
/*  439 */       String localEjbName = (String)event.getData();
/*  440 */       if (localEjbName != null) {
/*  441 */         removeLocalEjb(localEjbName);
/*      */       }
/*      */     }
/*  444 */     else if (type.equals("removeResource"))
/*      */     {
/*  446 */       String resourceName = (String)event.getData();
/*  447 */       if (resourceName != null) {
/*  448 */         removeResource(resourceName);
/*      */       }
/*      */     }
/*  451 */     else if (type.equals("removeResourceLink"))
/*      */     {
/*  453 */       String resourceLinkName = (String)event.getData();
/*  454 */       if (resourceLinkName != null) {
/*  455 */         removeResourceLink(resourceLinkName);
/*      */       }
/*      */     }
/*  458 */     else if (type.equals("removeResourceEnvRef"))
/*      */     {
/*  460 */       String resourceEnvRefName = (String)event.getData();
/*  461 */       if (resourceEnvRefName != null) {
/*  462 */         removeResourceEnvRef(resourceEnvRefName);
/*      */       }
/*      */     }
/*  465 */     else if (type.equals("removeService"))
/*      */     {
/*  467 */       String serviceName = (String)event.getData();
/*  468 */       if (serviceName != null) {
/*  469 */         removeService(serviceName);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  475 */     ContextAccessController.setReadOnly(getName());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void propertyChange(PropertyChangeEvent event)
/*      */   {
/*  491 */     if (!this.initialized) {
/*  492 */       return;
/*      */     }
/*  494 */     Object source = event.getSource();
/*  495 */     if (source == this.namingResources)
/*      */     {
/*      */ 
/*  498 */       ContextAccessController.setWritable(getName(), this.token);
/*      */       
/*  500 */       processGlobalResourcesChange(event.getPropertyName(), event
/*  501 */         .getOldValue(), event
/*  502 */         .getNewValue());
/*      */       
/*      */ 
/*  505 */       ContextAccessController.setReadOnly(getName());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void processGlobalResourcesChange(String name, Object oldValue, Object newValue)
/*      */   {
/*  527 */     if (name.equals("ejb")) {
/*  528 */       if (oldValue != null) {
/*  529 */         ContextEjb ejb = (ContextEjb)oldValue;
/*  530 */         if (ejb.getName() != null) {
/*  531 */           removeEjb(ejb.getName());
/*      */         }
/*      */       }
/*  534 */       if (newValue != null) {
/*  535 */         ContextEjb ejb = (ContextEjb)newValue;
/*  536 */         if (ejb.getName() != null) {
/*  537 */           addEjb(ejb);
/*      */         }
/*      */       }
/*  540 */     } else if (name.equals("environment")) {
/*  541 */       if (oldValue != null) {
/*  542 */         ContextEnvironment env = (ContextEnvironment)oldValue;
/*  543 */         if (env.getName() != null) {
/*  544 */           removeEnvironment(env.getName());
/*      */         }
/*      */       }
/*  547 */       if (newValue != null) {
/*  548 */         ContextEnvironment env = (ContextEnvironment)newValue;
/*  549 */         if (env.getName() != null) {
/*  550 */           addEnvironment(env);
/*      */         }
/*      */       }
/*  553 */     } else if (name.equals("localEjb")) {
/*  554 */       if (oldValue != null) {
/*  555 */         ContextLocalEjb ejb = (ContextLocalEjb)oldValue;
/*  556 */         if (ejb.getName() != null) {
/*  557 */           removeLocalEjb(ejb.getName());
/*      */         }
/*      */       }
/*  560 */       if (newValue != null) {
/*  561 */         ContextLocalEjb ejb = (ContextLocalEjb)newValue;
/*  562 */         if (ejb.getName() != null) {
/*  563 */           addLocalEjb(ejb);
/*      */         }
/*      */       }
/*  566 */     } else if (name.equals("resource")) {
/*  567 */       if (oldValue != null) {
/*  568 */         ContextResource resource = (ContextResource)oldValue;
/*  569 */         if (resource.getName() != null) {
/*  570 */           removeResource(resource.getName());
/*      */         }
/*      */       }
/*  573 */       if (newValue != null) {
/*  574 */         ContextResource resource = (ContextResource)newValue;
/*  575 */         if (resource.getName() != null) {
/*  576 */           addResource(resource);
/*      */         }
/*      */       }
/*  579 */     } else if (name.equals("resourceEnvRef")) {
/*  580 */       if (oldValue != null) {
/*  581 */         ContextResourceEnvRef resourceEnvRef = (ContextResourceEnvRef)oldValue;
/*      */         
/*  583 */         if (resourceEnvRef.getName() != null) {
/*  584 */           removeResourceEnvRef(resourceEnvRef.getName());
/*      */         }
/*      */       }
/*  587 */       if (newValue != null) {
/*  588 */         ContextResourceEnvRef resourceEnvRef = (ContextResourceEnvRef)newValue;
/*      */         
/*  590 */         if (resourceEnvRef.getName() != null) {
/*  591 */           addResourceEnvRef(resourceEnvRef);
/*      */         }
/*      */       }
/*  594 */     } else if (name.equals("resourceLink")) {
/*  595 */       if (oldValue != null) {
/*  596 */         ContextResourceLink rl = (ContextResourceLink)oldValue;
/*  597 */         if (rl.getName() != null) {
/*  598 */           removeResourceLink(rl.getName());
/*      */         }
/*      */       }
/*  601 */       if (newValue != null) {
/*  602 */         ContextResourceLink rl = (ContextResourceLink)newValue;
/*  603 */         if (rl.getName() != null) {
/*  604 */           addResourceLink(rl);
/*      */         }
/*      */       }
/*  607 */     } else if (name.equals("service")) {
/*  608 */       if (oldValue != null) {
/*  609 */         ContextService service = (ContextService)oldValue;
/*  610 */         if (service.getName() != null) {
/*  611 */           removeService(service.getName());
/*      */         }
/*      */       }
/*  614 */       if (newValue != null) {
/*  615 */         ContextService service = (ContextService)newValue;
/*  616 */         if (service.getName() != null) {
/*  617 */           addService(service);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void createNamingContext()
/*      */     throws NamingException
/*      */   {
/*  633 */     if ((this.container instanceof Server)) {
/*  634 */       this.compCtx = this.namingContext;
/*  635 */       this.envCtx = this.namingContext;
/*      */     } else {
/*  637 */       this.compCtx = this.namingContext.createSubcontext("comp");
/*  638 */       this.envCtx = this.compCtx.createSubcontext("env");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  643 */     if (log.isDebugEnabled()) {
/*  644 */       log.debug("Creating JNDI naming context");
/*      */     }
/*  646 */     if (this.namingResources == null) {
/*  647 */       this.namingResources = new NamingResourcesImpl();
/*  648 */       this.namingResources.setContainer(this.container);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  653 */     ContextResourceLink[] resourceLinks = this.namingResources.findResourceLinks();
/*  654 */     for (int i = 0; i < resourceLinks.length; i++) {
/*  655 */       addResourceLink(resourceLinks[i]);
/*      */     }
/*      */     
/*      */ 
/*  659 */     ContextResource[] resources = this.namingResources.findResources();
/*  660 */     for (i = 0; i < resources.length; i++) {
/*  661 */       addResource(resources[i]);
/*      */     }
/*      */     
/*      */ 
/*  665 */     ContextResourceEnvRef[] resourceEnvRefs = this.namingResources.findResourceEnvRefs();
/*  666 */     for (i = 0; i < resourceEnvRefs.length; i++) {
/*  667 */       addResourceEnvRef(resourceEnvRefs[i]);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  672 */     ContextEnvironment[] contextEnvironments = this.namingResources.findEnvironments();
/*  673 */     for (i = 0; i < contextEnvironments.length; i++) {
/*  674 */       addEnvironment(contextEnvironments[i]);
/*      */     }
/*      */     
/*      */ 
/*  678 */     ContextEjb[] ejbs = this.namingResources.findEjbs();
/*  679 */     for (i = 0; i < ejbs.length; i++) {
/*  680 */       addEjb(ejbs[i]);
/*      */     }
/*      */     
/*      */ 
/*  684 */     ContextService[] services = this.namingResources.findServices();
/*  685 */     for (i = 0; i < services.length; i++) {
/*  686 */       addService(services[i]);
/*      */     }
/*      */     
/*      */ 
/*  690 */     if ((this.container instanceof org.apache.catalina.Context)) {
/*      */       try {
/*  692 */         Reference ref = new TransactionRef();
/*  693 */         this.compCtx.bind("UserTransaction", ref);
/*  694 */         ContextTransaction transaction = this.namingResources.getTransaction();
/*  695 */         if (transaction != null) {
/*  696 */           Iterator<String> params = transaction.listProperties();
/*  697 */           while (params.hasNext()) {
/*  698 */             String paramName = (String)params.next();
/*  699 */             String paramValue = (String)transaction.getProperty(paramName);
/*  700 */             StringRefAddr refAddr = new StringRefAddr(paramName, paramValue);
/*  701 */             ref.add(refAddr);
/*      */           }
/*      */           
/*      */         }
/*      */       }
/*      */       catch (NameAlreadyBoundException localNameAlreadyBoundException) {}catch (NamingException e)
/*      */       {
/*  708 */         log.error(sm.getString("naming.bindFailed", new Object[] { e }));
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  713 */     if ((this.container instanceof org.apache.catalina.Context)) {
/*      */       try {
/*  715 */         this.compCtx.bind("Resources", ((org.apache.catalina.Context)this.container)
/*  716 */           .getResources());
/*      */       } catch (NamingException e) {
/*  718 */         log.error(sm.getString("naming.bindFailed", new Object[] { e }));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectName createObjectName(ContextResource resource)
/*      */     throws MalformedObjectNameException
/*      */   {
/*  736 */     String domain = null;
/*  737 */     if ((this.container instanceof StandardServer)) {
/*  738 */       domain = ((StandardServer)this.container).getDomain();
/*  739 */     } else if ((this.container instanceof ContainerBase)) {
/*  740 */       domain = ((ContainerBase)this.container).getDomain();
/*      */     }
/*  742 */     if (domain == null) {
/*  743 */       domain = "Catalina";
/*      */     }
/*      */     
/*  746 */     ObjectName name = null;
/*  747 */     String quotedResourceName = ObjectName.quote(resource.getName());
/*  748 */     if ((this.container instanceof Server))
/*      */     {
/*  750 */       name = new ObjectName(domain + ":type=DataSource,class=" + resource.getType() + ",name=" + quotedResourceName);
/*      */     }
/*  752 */     else if ((this.container instanceof org.apache.catalina.Context)) {
/*  753 */       String contextName = ((org.apache.catalina.Context)this.container).getName();
/*  754 */       if (!contextName.startsWith("/"))
/*  755 */         contextName = "/" + contextName;
/*  756 */       Host host = (Host)((org.apache.catalina.Context)this.container).getParent();
/*      */       
/*      */ 
/*      */ 
/*  760 */       name = new ObjectName(domain + ":type=DataSource,host=" + host.getName() + ",context=" + contextName + ",class=" + resource.getType() + ",name=" + quotedResourceName);
/*      */     }
/*      */     
/*      */ 
/*  764 */     return name;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addEjb(ContextEjb ejb)
/*      */   {
/*  778 */     Reference ref = new EjbRef(ejb.getType(), ejb.getHome(), ejb.getRemote(), ejb.getLink());
/*      */     
/*  780 */     Iterator<String> params = ejb.listProperties();
/*  781 */     while (params.hasNext()) {
/*  782 */       String paramName = (String)params.next();
/*  783 */       String paramValue = (String)ejb.getProperty(paramName);
/*  784 */       StringRefAddr refAddr = new StringRefAddr(paramName, paramValue);
/*  785 */       ref.add(refAddr);
/*      */     }
/*      */     try {
/*  788 */       createSubcontexts(this.envCtx, ejb.getName());
/*  789 */       this.envCtx.bind(ejb.getName(), ref);
/*      */     } catch (NamingException e) {
/*  791 */       log.error(sm.getString("naming.bindFailed", new Object[] { e }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addEnvironment(ContextEnvironment env)
/*      */   {
/*  804 */     Object value = null;
/*      */     
/*      */ 
/*  807 */     String type = env.getType();
/*      */     try {
/*  809 */       if (type.equals("java.lang.String")) {
/*  810 */         value = env.getValue();
/*  811 */       } else if (type.equals("java.lang.Byte")) {
/*  812 */         if (env.getValue() == null) {
/*  813 */           value = Byte.valueOf((byte)0);
/*      */         } else {
/*  815 */           value = Byte.decode(env.getValue());
/*      */         }
/*  817 */       } else if (type.equals("java.lang.Short")) {
/*  818 */         if (env.getValue() == null) {
/*  819 */           value = Short.valueOf((short)0);
/*      */         } else {
/*  821 */           value = Short.decode(env.getValue());
/*      */         }
/*  823 */       } else if (type.equals("java.lang.Integer")) {
/*  824 */         if (env.getValue() == null) {
/*  825 */           value = Integer.valueOf(0);
/*      */         } else {
/*  827 */           value = Integer.decode(env.getValue());
/*      */         }
/*  829 */       } else if (type.equals("java.lang.Long")) {
/*  830 */         if (env.getValue() == null) {
/*  831 */           value = Long.valueOf(0L);
/*      */         } else {
/*  833 */           value = Long.decode(env.getValue());
/*      */         }
/*  835 */       } else if (type.equals("java.lang.Boolean")) {
/*  836 */         value = Boolean.valueOf(env.getValue());
/*  837 */       } else if (type.equals("java.lang.Double")) {
/*  838 */         if (env.getValue() == null) {
/*  839 */           value = Double.valueOf(0.0D);
/*      */         } else {
/*  841 */           value = Double.valueOf(env.getValue());
/*      */         }
/*  843 */       } else if (type.equals("java.lang.Float")) {
/*  844 */         if (env.getValue() == null) {
/*  845 */           value = Float.valueOf(0.0F);
/*      */         } else {
/*  847 */           value = Float.valueOf(env.getValue());
/*      */         }
/*  849 */       } else if (type.equals("java.lang.Character")) {
/*  850 */         if (env.getValue() == null) {
/*  851 */           value = Character.valueOf('\000');
/*      */         }
/*  853 */         else if (env.getValue().length() == 1) {
/*  854 */           value = Character.valueOf(env.getValue().charAt(0));
/*      */         } else {
/*  856 */           throw new IllegalArgumentException();
/*      */         }
/*      */       }
/*      */       else {
/*  860 */         value = constructEnvEntry(env.getType(), env.getValue());
/*  861 */         if (value == null) {
/*  862 */           log.error(sm.getString("naming.invalidEnvEntryType", new Object[] {env
/*  863 */             .getName() }));
/*      */         }
/*      */       }
/*      */     } catch (NumberFormatException e) {
/*  867 */       log.error(sm.getString("naming.invalidEnvEntryValue", new Object[] { env.getName() }));
/*      */     } catch (IllegalArgumentException e) {
/*  869 */       log.error(sm.getString("naming.invalidEnvEntryValue", new Object[] { env.getName() }));
/*      */     }
/*      */     
/*      */ 
/*  873 */     if (value != null) {
/*      */       try {
/*  875 */         if (log.isDebugEnabled())
/*  876 */           log.debug("  Adding environment entry " + env.getName());
/*  877 */         createSubcontexts(this.envCtx, env.getName());
/*  878 */         this.envCtx.bind(env.getName(), value);
/*      */       } catch (NamingException e) {
/*  880 */         log.error(sm.getString("naming.invalidEnvEntryValue", new Object[] { e }));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private Object constructEnvEntry(String type, String value)
/*      */   {
/*      */     try
/*      */     {
/*  889 */       Class<?> clazz = Class.forName(type);
/*  890 */       Constructor<?> c = null;
/*      */       try {
/*  892 */         c = clazz.getConstructor(new Class[] { String.class });
/*  893 */         return c.newInstance(new Object[] { value });
/*      */ 
/*      */       }
/*      */       catch (NoSuchMethodException localNoSuchMethodException)
/*      */       {
/*  898 */         if (value.length() != 1) {
/*  899 */           return null;
/*      */         }
/*      */         try
/*      */         {
/*  903 */           c = clazz.getConstructor(new Class[] { Character.TYPE });
/*  904 */           return c.newInstance(new Object[] { Character.valueOf(value.charAt(0)) });
/*      */         }
/*      */         catch (NoSuchMethodException localNoSuchMethodException1) {}
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  911 */       return null;
/*      */     }
/*      */     catch (Exception localException) {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addLocalEjb(ContextLocalEjb localEjb) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addService(ContextService service)
/*      */   {
/*  931 */     if (service.getWsdlfile() != null) {
/*  932 */       URL wsdlURL = null;
/*      */       try
/*      */       {
/*  935 */         wsdlURL = new URL(service.getWsdlfile());
/*      */       }
/*      */       catch (MalformedURLException localMalformedURLException1) {}
/*      */       
/*  939 */       if (wsdlURL == null)
/*      */       {
/*      */         try
/*      */         {
/*  943 */           wsdlURL = ((org.apache.catalina.Context)this.container).getServletContext().getResource(service.getWsdlfile());
/*      */         }
/*      */         catch (MalformedURLException localMalformedURLException2) {}
/*      */       }
/*      */       
/*  948 */       if (wsdlURL == null)
/*      */       {
/*      */         try
/*      */         {
/*  952 */           wsdlURL = ((org.apache.catalina.Context)this.container).getServletContext().getResource("/" + service.getWsdlfile());
/*  953 */           log.debug("  Changing service ref wsdl file for /" + service
/*  954 */             .getWsdlfile());
/*      */         } catch (MalformedURLException e) {
/*  956 */           log.error(sm.getString("naming.wsdlFailed", new Object[] { e }));
/*      */         }
/*      */       }
/*  959 */       if (wsdlURL == null) {
/*  960 */         service.setWsdlfile(null);
/*      */       } else {
/*  962 */         service.setWsdlfile(wsdlURL.toString());
/*      */       }
/*      */     }
/*  965 */     if (service.getJaxrpcmappingfile() != null) {
/*  966 */       URL jaxrpcURL = null;
/*      */       try
/*      */       {
/*  969 */         jaxrpcURL = new URL(service.getJaxrpcmappingfile());
/*      */       }
/*      */       catch (MalformedURLException localMalformedURLException3) {}
/*      */       
/*  973 */       if (jaxrpcURL == null)
/*      */       {
/*      */         try
/*      */         {
/*  977 */           jaxrpcURL = ((org.apache.catalina.Context)this.container).getServletContext().getResource(service.getJaxrpcmappingfile());
/*      */         }
/*      */         catch (MalformedURLException localMalformedURLException4) {}
/*      */       }
/*      */       
/*  982 */       if (jaxrpcURL == null)
/*      */       {
/*      */         try
/*      */         {
/*  986 */           jaxrpcURL = ((org.apache.catalina.Context)this.container).getServletContext().getResource("/" + service.getJaxrpcmappingfile());
/*  987 */           log.debug("  Changing service ref jaxrpc file for /" + service
/*  988 */             .getJaxrpcmappingfile());
/*      */         } catch (MalformedURLException e) {
/*  990 */           log.error(sm.getString("naming.wsdlFailed", new Object[] { e }));
/*      */         }
/*      */       }
/*  993 */       if (jaxrpcURL == null) {
/*  994 */         service.setJaxrpcmappingfile(null);
/*      */       } else {
/*  996 */         service.setJaxrpcmappingfile(jaxrpcURL.toString());
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1002 */     Reference ref = new ServiceRef(service.getName(), service.getType(), service.getServiceqname(), service.getWsdlfile(), service.getJaxrpcmappingfile());
/*      */     
/* 1004 */     Iterator<String> portcomponent = service.getServiceendpoints();
/* 1005 */     while (portcomponent.hasNext()) {
/* 1006 */       String serviceendpoint = (String)portcomponent.next();
/* 1007 */       StringRefAddr refAddr = new StringRefAddr("serviceendpointinterface", serviceendpoint);
/* 1008 */       ref.add(refAddr);
/* 1009 */       String portlink = service.getPortlink(serviceendpoint);
/* 1010 */       refAddr = new StringRefAddr("portcomponentlink", portlink);
/* 1011 */       ref.add(refAddr);
/*      */     }
/*      */     
/* 1014 */     Iterator<String> handlers = service.getHandlers();
/* 1015 */     while (handlers.hasNext()) {
/* 1016 */       String handlername = (String)handlers.next();
/* 1017 */       ContextHandler handler = service.getHandler(handlername);
/* 1018 */       HandlerRef handlerRef = new HandlerRef(handlername, handler.getHandlerclass());
/* 1019 */       Iterator<String> localParts = handler.getLocalparts();
/* 1020 */       while (localParts.hasNext()) {
/* 1021 */         String localPart = (String)localParts.next();
/* 1022 */         String namespaceURI = handler.getNamespaceuri(localPart);
/* 1023 */         handlerRef.add(new StringRefAddr("handlerlocalpart", localPart));
/* 1024 */         handlerRef.add(new StringRefAddr("handlernamespace", namespaceURI));
/*      */       }
/* 1026 */       Iterator<String> params = handler.listProperties();
/* 1027 */       while (params.hasNext()) {
/* 1028 */         String paramName = (String)params.next();
/* 1029 */         String paramValue = (String)handler.getProperty(paramName);
/* 1030 */         handlerRef.add(new StringRefAddr("handlerparamname", paramName));
/* 1031 */         handlerRef.add(new StringRefAddr("handlerparamvalue", paramValue));
/*      */       }
/* 1033 */       for (int i = 0; i < handler.getSoapRolesSize(); i++) {
/* 1034 */         handlerRef.add(new StringRefAddr("handlersoaprole", handler.getSoapRole(i)));
/*      */       }
/* 1036 */       for (int i = 0; i < handler.getPortNamesSize(); i++) {
/* 1037 */         handlerRef.add(new StringRefAddr("handlerportname", handler.getPortName(i)));
/*      */       }
/* 1039 */       ((ServiceRef)ref).addHandler(handlerRef);
/*      */     }
/*      */     try
/*      */     {
/* 1043 */       if (log.isDebugEnabled()) {
/* 1044 */         log.debug("  Adding service ref " + service
/* 1045 */           .getName() + "  " + ref);
/*      */       }
/* 1047 */       createSubcontexts(this.envCtx, service.getName());
/* 1048 */       this.envCtx.bind(service.getName(), ref);
/*      */     } catch (NamingException e) {
/* 1050 */       log.error(sm.getString("naming.bindFailed", new Object[] { e }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addResource(ContextResource resource)
/*      */   {
/* 1067 */     Reference ref = new ResourceRef(resource.getType(), resource.getDescription(), resource.getScope(), resource.getAuth(), resource.getSingleton());
/*      */     
/* 1069 */     Iterator<String> params = resource.listProperties();
/* 1070 */     while (params.hasNext()) {
/* 1071 */       String paramName = (String)params.next();
/* 1072 */       String paramValue = (String)resource.getProperty(paramName);
/* 1073 */       StringRefAddr refAddr = new StringRefAddr(paramName, paramValue);
/* 1074 */       ref.add(refAddr);
/*      */     }
/*      */     try {
/* 1077 */       if (log.isDebugEnabled()) {
/* 1078 */         log.debug("  Adding resource ref " + resource
/* 1079 */           .getName() + "  " + ref);
/*      */       }
/* 1081 */       createSubcontexts(this.envCtx, resource.getName());
/* 1082 */       this.envCtx.bind(resource.getName(), ref);
/*      */     } catch (NamingException e) {
/* 1084 */       log.error(sm.getString("naming.bindFailed", new Object[] { e }));
/*      */     }
/*      */     
/* 1087 */     if (("javax.sql.DataSource".equals(ref.getClassName())) && 
/* 1088 */       (resource.getSingleton())) {
/*      */       try {
/* 1090 */         ObjectName on = createObjectName(resource);
/* 1091 */         Object actualResource = this.envCtx.lookup(resource.getName());
/* 1092 */         Registry.getRegistry(null, null).registerComponent(actualResource, on, null);
/* 1093 */         this.objectNames.put(resource.getName(), on);
/*      */       } catch (Exception e) {
/* 1095 */         log.warn(sm.getString("naming.jmxRegistrationFailed", new Object[] { e }));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addResourceEnvRef(ContextResourceEnvRef resourceEnvRef)
/*      */   {
/* 1110 */     Reference ref = new ResourceEnvRef(resourceEnvRef.getType());
/*      */     
/* 1112 */     Iterator<String> params = resourceEnvRef.listProperties();
/* 1113 */     while (params.hasNext()) {
/* 1114 */       String paramName = (String)params.next();
/* 1115 */       String paramValue = (String)resourceEnvRef.getProperty(paramName);
/* 1116 */       StringRefAddr refAddr = new StringRefAddr(paramName, paramValue);
/* 1117 */       ref.add(refAddr);
/*      */     }
/*      */     try {
/* 1120 */       if (log.isDebugEnabled())
/* 1121 */         log.debug("  Adding resource env ref " + resourceEnvRef.getName());
/* 1122 */       createSubcontexts(this.envCtx, resourceEnvRef.getName());
/* 1123 */       this.envCtx.bind(resourceEnvRef.getName(), ref);
/*      */     } catch (NamingException e) {
/* 1125 */       log.error(sm.getString("naming.bindFailed", new Object[] { e }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addResourceLink(ContextResourceLink resourceLink)
/*      */   {
/* 1140 */     Reference ref = new ResourceLinkRef(resourceLink.getType(), resourceLink.getGlobal(), resourceLink.getFactory(), null);
/* 1141 */     Iterator<String> i = resourceLink.listProperties();
/* 1142 */     while (i.hasNext()) {
/* 1143 */       String key = (String)i.next();
/* 1144 */       Object val = resourceLink.getProperty(key);
/* 1145 */       if (val != null) {
/* 1146 */         StringRefAddr refAddr = new StringRefAddr(key, val.toString());
/* 1147 */         ref.add(refAddr);
/*      */       }
/*      */     }
/*      */     
/* 1151 */     javax.naming.Context ctx = "UserTransaction".equals(resourceLink.getName()) ? this.compCtx : this.envCtx;
/*      */     try
/*      */     {
/* 1154 */       if (log.isDebugEnabled())
/* 1155 */         log.debug("  Adding resource link " + resourceLink.getName());
/* 1156 */       createSubcontexts(this.envCtx, resourceLink.getName());
/* 1157 */       ctx.bind(resourceLink.getName(), ref);
/*      */     } catch (NamingException e) {
/* 1159 */       log.error(sm.getString("naming.bindFailed", new Object[] { e }));
/*      */     }
/*      */     
/* 1162 */     ResourceLinkFactory.registerGlobalResourceAccess(
/* 1163 */       getGlobalNamingContext(), resourceLink.getName(), resourceLink.getGlobal());
/*      */   }
/*      */   
/*      */   private javax.naming.Context getGlobalNamingContext()
/*      */   {
/* 1168 */     if ((this.container instanceof org.apache.catalina.Context)) {
/* 1169 */       Engine e = (Engine)((org.apache.catalina.Context)this.container).getParent().getParent();
/* 1170 */       return e.getService().getServer().getGlobalNamingContext();
/*      */     }
/* 1172 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeEjb(String name)
/*      */   {
/*      */     try
/*      */     {
/* 1184 */       this.envCtx.unbind(name);
/*      */     } catch (NamingException e) {
/* 1186 */       log.error(sm.getString("naming.unbindFailed", new Object[] { e }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeEnvironment(String name)
/*      */   {
/*      */     try
/*      */     {
/* 1200 */       this.envCtx.unbind(name);
/*      */     } catch (NamingException e) {
/* 1202 */       log.error(sm.getString("naming.unbindFailed", new Object[] { e }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeLocalEjb(String name)
/*      */   {
/*      */     try
/*      */     {
/* 1216 */       this.envCtx.unbind(name);
/*      */     } catch (NamingException e) {
/* 1218 */       log.error(sm.getString("naming.unbindFailed", new Object[] { e }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeService(String name)
/*      */   {
/*      */     try
/*      */     {
/* 1232 */       this.envCtx.unbind(name);
/*      */     } catch (NamingException e) {
/* 1234 */       log.error(sm.getString("naming.unbindFailed", new Object[] { e }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeResource(String name)
/*      */   {
/*      */     try
/*      */     {
/* 1248 */       this.envCtx.unbind(name);
/*      */     } catch (NamingException e) {
/* 1250 */       log.error(sm.getString("naming.unbindFailed", new Object[] { e }));
/*      */     }
/*      */     
/* 1253 */     ObjectName on = (ObjectName)this.objectNames.get(name);
/* 1254 */     if (on != null) {
/* 1255 */       Registry.getRegistry(null, null).unregisterComponent(on);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeResourceEnvRef(String name)
/*      */   {
/*      */     try
/*      */     {
/* 1269 */       this.envCtx.unbind(name);
/*      */     } catch (NamingException e) {
/* 1271 */       log.error(sm.getString("naming.unbindFailed", new Object[] { e }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeResourceLink(String name)
/*      */   {
/*      */     try
/*      */     {
/* 1285 */       this.envCtx.unbind(name);
/*      */     } catch (NamingException e) {
/* 1287 */       log.error(sm.getString("naming.unbindFailed", new Object[] { e }));
/*      */     }
/*      */     
/* 1290 */     ResourceLinkFactory.deregisterGlobalResourceAccess(getGlobalNamingContext(), name);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void createSubcontexts(javax.naming.Context ctx, String name)
/*      */     throws NamingException
/*      */   {
/* 1299 */     javax.naming.Context currentContext = ctx;
/* 1300 */     StringTokenizer tokenizer = new StringTokenizer(name, "/");
/* 1301 */     while (tokenizer.hasMoreTokens()) {
/* 1302 */       String token = tokenizer.nextToken();
/* 1303 */       if ((!token.equals("")) && (tokenizer.hasMoreTokens())) {
/*      */         try {
/* 1305 */           currentContext = currentContext.createSubcontext(token);
/*      */ 
/*      */         }
/*      */         catch (NamingException e)
/*      */         {
/* 1310 */           currentContext = (javax.naming.Context)currentContext.lookup(token);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\core\NamingContextListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */