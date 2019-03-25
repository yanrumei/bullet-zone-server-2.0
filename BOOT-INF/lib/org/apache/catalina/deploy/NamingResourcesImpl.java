/*      */ package org.apache.catalina.deploy;
/*      */ 
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.PropertyChangeSupport;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import javax.naming.NamingException;
/*      */ import org.apache.catalina.Container;
/*      */ import org.apache.catalina.Engine;
/*      */ import org.apache.catalina.JmxEnabled;
/*      */ import org.apache.catalina.LifecycleException;
/*      */ import org.apache.catalina.LifecycleState;
/*      */ import org.apache.catalina.Server;
/*      */ import org.apache.catalina.Service;
/*      */ import org.apache.catalina.mbeans.MBeanUtils;
/*      */ import org.apache.catalina.util.Introspection;
/*      */ import org.apache.catalina.util.LifecycleMBeanBase;
/*      */ import org.apache.juli.logging.Log;
/*      */ import org.apache.juli.logging.LogFactory;
/*      */ import org.apache.naming.ContextBindings;
/*      */ import org.apache.tomcat.util.ExceptionUtils;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextEjb;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextEnvironment;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextLocalEjb;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextResource;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextResourceEnvRef;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextResourceLink;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextService;
/*      */ import org.apache.tomcat.util.descriptor.web.ContextTransaction;
/*      */ import org.apache.tomcat.util.descriptor.web.InjectionTarget;
/*      */ import org.apache.tomcat.util.descriptor.web.MessageDestinationRef;
/*      */ import org.apache.tomcat.util.descriptor.web.NamingResources;
/*      */ import org.apache.tomcat.util.descriptor.web.ResourceBase;
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
/*      */ public class NamingResourcesImpl
/*      */   extends LifecycleMBeanBase
/*      */   implements Serializable, NamingResources
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   71 */   private static final Log log = LogFactory.getLog(NamingResourcesImpl.class);
/*      */   
/*   73 */   private static final StringManager sm = StringManager.getManager(NamingResourcesImpl.class);
/*      */   
/*   75 */   private volatile boolean resourceRequireExplicitRegistration = false;
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
/*   94 */   private Object container = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  100 */   private final Set<String> entries = new HashSet();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  106 */   private final HashMap<String, ContextEjb> ejbs = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  112 */   private final HashMap<String, ContextEnvironment> envs = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  119 */   private final HashMap<String, ContextLocalEjb> localEjbs = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  126 */   private final HashMap<String, MessageDestinationRef> mdrs = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  133 */   private final HashMap<String, ContextResourceEnvRef> resourceEnvRefs = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  140 */   private final HashMap<String, ContextResource> resources = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  147 */   private final HashMap<String, ContextResourceLink> resourceLinks = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  154 */   private final HashMap<String, ContextService> services = new HashMap();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  161 */   private ContextTransaction transaction = null;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  167 */   protected final PropertyChangeSupport support = new PropertyChangeSupport(this);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getContainer()
/*      */   {
/*  179 */     return this.container;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setContainer(Object container)
/*      */   {
/*  188 */     this.container = container;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setTransaction(ContextTransaction transaction)
/*      */   {
/*  197 */     this.transaction = transaction;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ContextTransaction getTransaction()
/*      */   {
/*  205 */     return this.transaction;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addEjb(ContextEjb ejb)
/*      */   {
/*  216 */     if (this.entries.contains(ejb.getName())) {
/*  217 */       return;
/*      */     }
/*  219 */     this.entries.add(ejb.getName());
/*      */     
/*      */ 
/*  222 */     synchronized (this.ejbs) {
/*  223 */       ejb.setNamingResources(this);
/*  224 */       this.ejbs.put(ejb.getName(), ejb);
/*      */     }
/*  226 */     this.support.firePropertyChange("ejb", null, ejb);
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
/*      */   public void addEnvironment(ContextEnvironment environment)
/*      */   {
/*  239 */     if (this.entries.contains(environment.getName())) {
/*  240 */       ContextEnvironment ce = findEnvironment(environment.getName());
/*  241 */       ContextResourceLink rl = findResourceLink(environment.getName());
/*  242 */       if (ce != null) {
/*  243 */         if (ce.getOverride()) {
/*  244 */           removeEnvironment(environment.getName());
/*      */         }
/*      */         
/*      */       }
/*  248 */       else if (rl != null)
/*      */       {
/*  250 */         NamingResourcesImpl global = getServer().getGlobalNamingResources();
/*  251 */         if (global.findEnvironment(rl.getGlobal()) != null) {
/*  252 */           if (global.findEnvironment(rl.getGlobal()).getOverride()) {
/*  253 */             removeResourceLink(environment.getName());
/*      */           } else {
/*  255 */             return;
/*      */           }
/*      */         }
/*      */       }
/*      */       else {
/*  260 */         return;
/*      */       }
/*      */     }
/*      */     
/*  264 */     if (!checkResourceType(environment)) {
/*  265 */       throw new IllegalArgumentException(sm.getString("namingResources.resourceTypeFail", new Object[] {environment
/*  266 */         .getName(), environment
/*  267 */         .getType() }));
/*      */     }
/*      */     
/*  270 */     this.entries.add(environment.getName());
/*      */     
/*  272 */     synchronized (this.envs) {
/*  273 */       environment.setNamingResources(this);
/*  274 */       this.envs.put(environment.getName(), environment);
/*      */     }
/*  276 */     this.support.firePropertyChange("environment", null, environment);
/*      */     
/*      */ 
/*  279 */     if (this.resourceRequireExplicitRegistration) {
/*      */       try {
/*  281 */         MBeanUtils.createMBean(environment);
/*      */       } catch (Exception e) {
/*  283 */         log.warn(sm.getString("namingResources.mbeanCreateFail", new Object[] {environment
/*  284 */           .getName() }), e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   private Server getServer()
/*      */   {
/*  292 */     if ((this.container instanceof Server)) {
/*  293 */       return (Server)this.container;
/*      */     }
/*  295 */     if ((this.container instanceof org.apache.catalina.Context))
/*      */     {
/*      */ 
/*  298 */       Engine engine = (Engine)((org.apache.catalina.Context)this.container).getParent().getParent();
/*  299 */       return engine.getService().getServer();
/*      */     }
/*  301 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addLocalEjb(ContextLocalEjb ejb)
/*      */   {
/*  311 */     if (this.entries.contains(ejb.getName())) {
/*  312 */       return;
/*      */     }
/*  314 */     this.entries.add(ejb.getName());
/*      */     
/*      */ 
/*  317 */     synchronized (this.localEjbs) {
/*  318 */       ejb.setNamingResources(this);
/*  319 */       this.localEjbs.put(ejb.getName(), ejb);
/*      */     }
/*  321 */     this.support.firePropertyChange("localEjb", null, ejb);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addMessageDestinationRef(MessageDestinationRef mdr)
/*      */   {
/*  333 */     if (this.entries.contains(mdr.getName())) {
/*  334 */       return;
/*      */     }
/*  336 */     if (!checkResourceType(mdr)) {
/*  337 */       throw new IllegalArgumentException(sm.getString("namingResources.resourceTypeFail", new Object[] {mdr
/*  338 */         .getName(), mdr
/*  339 */         .getType() }));
/*      */     }
/*  341 */     this.entries.add(mdr.getName());
/*      */     
/*      */ 
/*  344 */     synchronized (this.mdrs) {
/*  345 */       mdr.setNamingResources(this);
/*  346 */       this.mdrs.put(mdr.getName(), mdr);
/*      */     }
/*  348 */     this.support.firePropertyChange("messageDestinationRef", null, mdr);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addPropertyChangeListener(PropertyChangeListener listener)
/*      */   {
/*  360 */     this.support.addPropertyChangeListener(listener);
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
/*      */   public void addResource(ContextResource resource)
/*      */   {
/*  373 */     if (this.entries.contains(resource.getName())) {
/*  374 */       return;
/*      */     }
/*  376 */     if (!checkResourceType(resource)) {
/*  377 */       throw new IllegalArgumentException(sm.getString("namingResources.resourceTypeFail", new Object[] {resource
/*  378 */         .getName(), resource
/*  379 */         .getType() }));
/*      */     }
/*  381 */     this.entries.add(resource.getName());
/*      */     
/*      */ 
/*  384 */     synchronized (this.resources) {
/*  385 */       resource.setNamingResources(this);
/*  386 */       this.resources.put(resource.getName(), resource);
/*      */     }
/*  388 */     this.support.firePropertyChange("resource", null, resource);
/*      */     
/*      */ 
/*  391 */     if (this.resourceRequireExplicitRegistration) {
/*      */       try {
/*  393 */         MBeanUtils.createMBean(resource);
/*      */       } catch (Exception e) {
/*  395 */         log.warn(sm.getString("namingResources.mbeanCreateFail", new Object[] {resource
/*  396 */           .getName() }), e);
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
/*      */   public void addResourceEnvRef(ContextResourceEnvRef resource)
/*      */   {
/*  409 */     if (this.entries.contains(resource.getName())) {
/*  410 */       return;
/*      */     }
/*  412 */     if (!checkResourceType(resource)) {
/*  413 */       throw new IllegalArgumentException(sm.getString("namingResources.resourceTypeFail", new Object[] {resource
/*  414 */         .getName(), resource
/*  415 */         .getType() }));
/*      */     }
/*  417 */     this.entries.add(resource.getName());
/*      */     
/*      */ 
/*  420 */     synchronized (this.resourceEnvRefs) {
/*  421 */       resource.setNamingResources(this);
/*  422 */       this.resourceEnvRefs.put(resource.getName(), resource);
/*      */     }
/*  424 */     this.support.firePropertyChange("resourceEnvRef", null, resource);
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
/*      */   public void addResourceLink(ContextResourceLink resourceLink)
/*      */   {
/*  437 */     if (this.entries.contains(resourceLink.getName())) {
/*  438 */       return;
/*      */     }
/*  440 */     this.entries.add(resourceLink.getName());
/*      */     
/*      */ 
/*  443 */     synchronized (this.resourceLinks) {
/*  444 */       resourceLink.setNamingResources(this);
/*  445 */       this.resourceLinks.put(resourceLink.getName(), resourceLink);
/*      */     }
/*  447 */     this.support.firePropertyChange("resourceLink", null, resourceLink);
/*      */     
/*      */ 
/*  450 */     if (this.resourceRequireExplicitRegistration) {
/*      */       try {
/*  452 */         MBeanUtils.createMBean(resourceLink);
/*      */       } catch (Exception e) {
/*  454 */         log.warn(sm.getString("namingResources.mbeanCreateFail", new Object[] {resourceLink
/*  455 */           .getName() }), e);
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
/*      */   public void addService(ContextService service)
/*      */   {
/*  468 */     if (this.entries.contains(service.getName())) {
/*  469 */       return;
/*      */     }
/*  471 */     this.entries.add(service.getName());
/*      */     
/*      */ 
/*  474 */     synchronized (this.services) {
/*  475 */       service.setNamingResources(this);
/*  476 */       this.services.put(service.getName(), service);
/*      */     }
/*  478 */     this.support.firePropertyChange("service", null, service);
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public ContextEjb findEjb(String name)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 9	org/apache/catalina/deploy/NamingResourcesImpl:ejbs	Ljava/util/HashMap;
/*      */     //   4: dup
/*      */     //   5: astore_2
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 9	org/apache/catalina/deploy/NamingResourcesImpl:ejbs	Ljava/util/HashMap;
/*      */     //   11: aload_1
/*      */     //   12: invokevirtual 83	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   15: checkcast 84	org/apache/tomcat/util/descriptor/web/ContextEjb
/*      */     //   18: aload_2
/*      */     //   19: monitorexit
/*      */     //   20: areturn
/*      */     //   21: astore_3
/*      */     //   22: aload_2
/*      */     //   23: monitorexit
/*      */     //   24: aload_3
/*      */     //   25: athrow
/*      */     // Line number table:
/*      */     //   Java source line #491	-> byte code offset #0
/*      */     //   Java source line #492	-> byte code offset #7
/*      */     //   Java source line #493	-> byte code offset #21
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	26	0	this	NamingResourcesImpl
/*      */     //   0	26	1	name	String
/*      */     //   5	18	2	Ljava/lang/Object;	Object
/*      */     //   21	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	20	21	finally
/*      */     //   21	24	21	finally
/*      */   }
/*      */   
/*      */   public ContextEjb[] findEjbs()
/*      */   {
/*  504 */     synchronized (this.ejbs) {
/*  505 */       ContextEjb[] results = new ContextEjb[this.ejbs.size()];
/*  506 */       return (ContextEjb[])this.ejbs.values().toArray(results);
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public ContextEnvironment findEnvironment(String name)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 10	org/apache/catalina/deploy/NamingResourcesImpl:envs	Ljava/util/HashMap;
/*      */     //   4: dup
/*      */     //   5: astore_2
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 10	org/apache/catalina/deploy/NamingResourcesImpl:envs	Ljava/util/HashMap;
/*      */     //   11: aload_1
/*      */     //   12: invokevirtual 83	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   15: checkcast 89	org/apache/tomcat/util/descriptor/web/ContextEnvironment
/*      */     //   18: aload_2
/*      */     //   19: monitorexit
/*      */     //   20: areturn
/*      */     //   21: astore_3
/*      */     //   22: aload_2
/*      */     //   23: monitorexit
/*      */     //   24: aload_3
/*      */     //   25: athrow
/*      */     // Line number table:
/*      */     //   Java source line #520	-> byte code offset #0
/*      */     //   Java source line #521	-> byte code offset #7
/*      */     //   Java source line #522	-> byte code offset #21
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	26	0	this	NamingResourcesImpl
/*      */     //   0	26	1	name	String
/*      */     //   5	18	2	Ljava/lang/Object;	Object
/*      */     //   21	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	20	21	finally
/*      */     //   21	24	21	finally
/*      */   }
/*      */   
/*      */   public ContextEnvironment[] findEnvironments()
/*      */   {
/*  534 */     synchronized (this.envs) {
/*  535 */       ContextEnvironment[] results = new ContextEnvironment[this.envs.size()];
/*  536 */       return (ContextEnvironment[])this.envs.values().toArray(results);
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public ContextLocalEjb findLocalEjb(String name)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 11	org/apache/catalina/deploy/NamingResourcesImpl:localEjbs	Ljava/util/HashMap;
/*      */     //   4: dup
/*      */     //   5: astore_2
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 11	org/apache/catalina/deploy/NamingResourcesImpl:localEjbs	Ljava/util/HashMap;
/*      */     //   11: aload_1
/*      */     //   12: invokevirtual 83	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   15: checkcast 91	org/apache/tomcat/util/descriptor/web/ContextLocalEjb
/*      */     //   18: aload_2
/*      */     //   19: monitorexit
/*      */     //   20: areturn
/*      */     //   21: astore_3
/*      */     //   22: aload_2
/*      */     //   23: monitorexit
/*      */     //   24: aload_3
/*      */     //   25: athrow
/*      */     // Line number table:
/*      */     //   Java source line #550	-> byte code offset #0
/*      */     //   Java source line #551	-> byte code offset #7
/*      */     //   Java source line #552	-> byte code offset #21
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	26	0	this	NamingResourcesImpl
/*      */     //   0	26	1	name	String
/*      */     //   5	18	2	Ljava/lang/Object;	Object
/*      */     //   21	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	20	21	finally
/*      */     //   21	24	21	finally
/*      */   }
/*      */   
/*      */   public ContextLocalEjb[] findLocalEjbs()
/*      */   {
/*  563 */     synchronized (this.localEjbs) {
/*  564 */       ContextLocalEjb[] results = new ContextLocalEjb[this.localEjbs.size()];
/*  565 */       return (ContextLocalEjb[])this.localEjbs.values().toArray(results);
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public MessageDestinationRef findMessageDestinationRef(String name)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 12	org/apache/catalina/deploy/NamingResourcesImpl:mdrs	Ljava/util/HashMap;
/*      */     //   4: dup
/*      */     //   5: astore_2
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 12	org/apache/catalina/deploy/NamingResourcesImpl:mdrs	Ljava/util/HashMap;
/*      */     //   11: aload_1
/*      */     //   12: invokevirtual 83	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   15: checkcast 93	org/apache/tomcat/util/descriptor/web/MessageDestinationRef
/*      */     //   18: aload_2
/*      */     //   19: monitorexit
/*      */     //   20: areturn
/*      */     //   21: astore_3
/*      */     //   22: aload_2
/*      */     //   23: monitorexit
/*      */     //   24: aload_3
/*      */     //   25: athrow
/*      */     // Line number table:
/*      */     //   Java source line #579	-> byte code offset #0
/*      */     //   Java source line #580	-> byte code offset #7
/*      */     //   Java source line #581	-> byte code offset #21
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	26	0	this	NamingResourcesImpl
/*      */     //   0	26	1	name	String
/*      */     //   5	18	2	Ljava/lang/Object;	Object
/*      */     //   21	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	20	21	finally
/*      */     //   21	24	21	finally
/*      */   }
/*      */   
/*      */   public MessageDestinationRef[] findMessageDestinationRefs()
/*      */   {
/*  592 */     synchronized (this.mdrs)
/*      */     {
/*  594 */       MessageDestinationRef[] results = new MessageDestinationRef[this.mdrs.size()];
/*  595 */       return (MessageDestinationRef[])this.mdrs.values().toArray(results);
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public ContextResource findResource(String name)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 14	org/apache/catalina/deploy/NamingResourcesImpl:resources	Ljava/util/HashMap;
/*      */     //   4: dup
/*      */     //   5: astore_2
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 14	org/apache/catalina/deploy/NamingResourcesImpl:resources	Ljava/util/HashMap;
/*      */     //   11: aload_1
/*      */     //   12: invokevirtual 83	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   15: checkcast 95	org/apache/tomcat/util/descriptor/web/ContextResource
/*      */     //   18: aload_2
/*      */     //   19: monitorexit
/*      */     //   20: areturn
/*      */     //   21: astore_3
/*      */     //   22: aload_2
/*      */     //   23: monitorexit
/*      */     //   24: aload_3
/*      */     //   25: athrow
/*      */     // Line number table:
/*      */     //   Java source line #609	-> byte code offset #0
/*      */     //   Java source line #610	-> byte code offset #7
/*      */     //   Java source line #611	-> byte code offset #21
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	26	0	this	NamingResourcesImpl
/*      */     //   0	26	1	name	String
/*      */     //   5	18	2	Ljava/lang/Object;	Object
/*      */     //   21	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	20	21	finally
/*      */     //   21	24	21	finally
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public ContextResourceLink findResourceLink(String name)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 15	org/apache/catalina/deploy/NamingResourcesImpl:resourceLinks	Ljava/util/HashMap;
/*      */     //   4: dup
/*      */     //   5: astore_2
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 15	org/apache/catalina/deploy/NamingResourcesImpl:resourceLinks	Ljava/util/HashMap;
/*      */     //   11: aload_1
/*      */     //   12: invokevirtual 83	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   15: checkcast 96	org/apache/tomcat/util/descriptor/web/ContextResourceLink
/*      */     //   18: aload_2
/*      */     //   19: monitorexit
/*      */     //   20: areturn
/*      */     //   21: astore_3
/*      */     //   22: aload_2
/*      */     //   23: monitorexit
/*      */     //   24: aload_3
/*      */     //   25: athrow
/*      */     // Line number table:
/*      */     //   Java source line #624	-> byte code offset #0
/*      */     //   Java source line #625	-> byte code offset #7
/*      */     //   Java source line #626	-> byte code offset #21
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	26	0	this	NamingResourcesImpl
/*      */     //   0	26	1	name	String
/*      */     //   5	18	2	Ljava/lang/Object;	Object
/*      */     //   21	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	20	21	finally
/*      */     //   21	24	21	finally
/*      */   }
/*      */   
/*      */   public ContextResourceLink[] findResourceLinks()
/*      */   {
/*  637 */     synchronized (this.resourceLinks)
/*      */     {
/*  639 */       ContextResourceLink[] results = new ContextResourceLink[this.resourceLinks.size()];
/*  640 */       return (ContextResourceLink[])this.resourceLinks.values().toArray(results);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ContextResource[] findResources()
/*      */   {
/*  652 */     synchronized (this.resources) {
/*  653 */       ContextResource[] results = new ContextResource[this.resources.size()];
/*  654 */       return (ContextResource[])this.resources.values().toArray(results);
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public ContextResourceEnvRef findResourceEnvRef(String name)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 13	org/apache/catalina/deploy/NamingResourcesImpl:resourceEnvRefs	Ljava/util/HashMap;
/*      */     //   4: dup
/*      */     //   5: astore_2
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 13	org/apache/catalina/deploy/NamingResourcesImpl:resourceEnvRefs	Ljava/util/HashMap;
/*      */     //   11: aload_1
/*      */     //   12: invokevirtual 83	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   15: checkcast 99	org/apache/tomcat/util/descriptor/web/ContextResourceEnvRef
/*      */     //   18: aload_2
/*      */     //   19: monitorexit
/*      */     //   20: areturn
/*      */     //   21: astore_3
/*      */     //   22: aload_2
/*      */     //   23: monitorexit
/*      */     //   24: aload_3
/*      */     //   25: athrow
/*      */     // Line number table:
/*      */     //   Java source line #668	-> byte code offset #0
/*      */     //   Java source line #669	-> byte code offset #7
/*      */     //   Java source line #670	-> byte code offset #21
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	26	0	this	NamingResourcesImpl
/*      */     //   0	26	1	name	String
/*      */     //   5	18	2	Ljava/lang/Object;	Object
/*      */     //   21	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	20	21	finally
/*      */     //   21	24	21	finally
/*      */   }
/*      */   
/*      */   public ContextResourceEnvRef[] findResourceEnvRefs()
/*      */   {
/*  682 */     synchronized (this.resourceEnvRefs) {
/*  683 */       ContextResourceEnvRef[] results = new ContextResourceEnvRef[this.resourceEnvRefs.size()];
/*  684 */       return (ContextResourceEnvRef[])this.resourceEnvRefs.values().toArray(results);
/*      */     }
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public ContextService findService(String name)
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_0
/*      */     //   1: getfield 16	org/apache/catalina/deploy/NamingResourcesImpl:services	Ljava/util/HashMap;
/*      */     //   4: dup
/*      */     //   5: astore_2
/*      */     //   6: monitorenter
/*      */     //   7: aload_0
/*      */     //   8: getfield 16	org/apache/catalina/deploy/NamingResourcesImpl:services	Ljava/util/HashMap;
/*      */     //   11: aload_1
/*      */     //   12: invokevirtual 83	java/util/HashMap:get	(Ljava/lang/Object;)Ljava/lang/Object;
/*      */     //   15: checkcast 101	org/apache/tomcat/util/descriptor/web/ContextService
/*      */     //   18: aload_2
/*      */     //   19: monitorexit
/*      */     //   20: areturn
/*      */     //   21: astore_3
/*      */     //   22: aload_2
/*      */     //   23: monitorexit
/*      */     //   24: aload_3
/*      */     //   25: athrow
/*      */     // Line number table:
/*      */     //   Java source line #698	-> byte code offset #0
/*      */     //   Java source line #699	-> byte code offset #7
/*      */     //   Java source line #700	-> byte code offset #21
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	26	0	this	NamingResourcesImpl
/*      */     //   0	26	1	name	String
/*      */     //   5	18	2	Ljava/lang/Object;	Object
/*      */     //   21	4	3	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   7	20	21	finally
/*      */     //   21	24	21	finally
/*      */   }
/*      */   
/*      */   public ContextService[] findServices()
/*      */   {
/*  711 */     synchronized (this.services) {
/*  712 */       ContextService[] results = new ContextService[this.services.size()];
/*  713 */       return (ContextService[])this.services.values().toArray(results);
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
/*      */   public void removeEjb(String name)
/*      */   {
/*  726 */     this.entries.remove(name);
/*      */     
/*  728 */     ContextEjb ejb = null;
/*  729 */     synchronized (this.ejbs) {
/*  730 */       ejb = (ContextEjb)this.ejbs.remove(name);
/*      */     }
/*  732 */     if (ejb != null) {
/*  733 */       this.support.firePropertyChange("ejb", ejb, null);
/*  734 */       ejb.setNamingResources(null);
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
/*      */   public void removeEnvironment(String name)
/*      */   {
/*  748 */     this.entries.remove(name);
/*      */     
/*  750 */     ContextEnvironment environment = null;
/*  751 */     synchronized (this.envs) {
/*  752 */       environment = (ContextEnvironment)this.envs.remove(name);
/*      */     }
/*  754 */     if (environment != null) {
/*  755 */       this.support.firePropertyChange("environment", environment, null);
/*      */       
/*  757 */       if (this.resourceRequireExplicitRegistration) {
/*      */         try {
/*  759 */           MBeanUtils.destroyMBean(environment);
/*      */         } catch (Exception e) {
/*  761 */           log.warn(sm.getString("namingResources.mbeanDestroyFail", new Object[] {environment
/*  762 */             .getName() }), e);
/*      */         }
/*      */       }
/*  765 */       environment.setNamingResources(null);
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
/*  777 */     this.entries.remove(name);
/*      */     
/*  779 */     ContextLocalEjb localEjb = null;
/*  780 */     synchronized (this.localEjbs) {
/*  781 */       localEjb = (ContextLocalEjb)this.localEjbs.remove(name);
/*      */     }
/*  783 */     if (localEjb != null) {
/*  784 */       this.support.firePropertyChange("localEjb", localEjb, null);
/*  785 */       localEjb.setNamingResources(null);
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
/*      */   public void removeMessageDestinationRef(String name)
/*      */   {
/*  798 */     this.entries.remove(name);
/*      */     
/*  800 */     MessageDestinationRef mdr = null;
/*  801 */     synchronized (this.mdrs) {
/*  802 */       mdr = (MessageDestinationRef)this.mdrs.remove(name);
/*      */     }
/*  804 */     if (mdr != null) {
/*  805 */       this.support.firePropertyChange("messageDestinationRef", mdr, null);
/*      */       
/*  807 */       mdr.setNamingResources(null);
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
/*      */   public void removePropertyChangeListener(PropertyChangeListener listener)
/*      */   {
/*  820 */     this.support.removePropertyChangeListener(listener);
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
/*      */   public void removeResource(String name)
/*      */   {
/*  833 */     this.entries.remove(name);
/*      */     
/*  835 */     ContextResource resource = null;
/*  836 */     synchronized (this.resources) {
/*  837 */       resource = (ContextResource)this.resources.remove(name);
/*      */     }
/*  839 */     if (resource != null) {
/*  840 */       this.support.firePropertyChange("resource", resource, null);
/*      */       
/*  842 */       if (this.resourceRequireExplicitRegistration) {
/*      */         try {
/*  844 */           MBeanUtils.destroyMBean(resource);
/*      */         } catch (Exception e) {
/*  846 */           log.warn(sm.getString("namingResources.mbeanDestroyFail", new Object[] {resource
/*  847 */             .getName() }), e);
/*      */         }
/*      */       }
/*  850 */       resource.setNamingResources(null);
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
/*  862 */     this.entries.remove(name);
/*      */     
/*  864 */     ContextResourceEnvRef resourceEnvRef = null;
/*  865 */     synchronized (this.resourceEnvRefs)
/*      */     {
/*  867 */       resourceEnvRef = (ContextResourceEnvRef)this.resourceEnvRefs.remove(name);
/*      */     }
/*  869 */     if (resourceEnvRef != null) {
/*  870 */       this.support.firePropertyChange("resourceEnvRef", resourceEnvRef, null);
/*  871 */       resourceEnvRef.setNamingResources(null);
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
/*      */   public void removeResourceLink(String name)
/*      */   {
/*  885 */     this.entries.remove(name);
/*      */     
/*  887 */     ContextResourceLink resourceLink = null;
/*  888 */     synchronized (this.resourceLinks) {
/*  889 */       resourceLink = (ContextResourceLink)this.resourceLinks.remove(name);
/*      */     }
/*  891 */     if (resourceLink != null) {
/*  892 */       this.support.firePropertyChange("resourceLink", resourceLink, null);
/*      */       
/*  894 */       if (this.resourceRequireExplicitRegistration) {
/*      */         try {
/*  896 */           MBeanUtils.destroyMBean(resourceLink);
/*      */         } catch (Exception e) {
/*  898 */           log.warn(sm.getString("namingResources.mbeanDestroyFail", new Object[] {resourceLink
/*  899 */             .getName() }), e);
/*      */         }
/*      */       }
/*  902 */       resourceLink.setNamingResources(null);
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
/*  914 */     this.entries.remove(name);
/*      */     
/*  916 */     ContextService service = null;
/*  917 */     synchronized (this.services) {
/*  918 */       service = (ContextService)this.services.remove(name);
/*      */     }
/*  920 */     if (service != null) {
/*  921 */       this.support.firePropertyChange("service", service, null);
/*  922 */       service.setNamingResources(null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void initInternal()
/*      */     throws LifecycleException
/*      */   {
/*  932 */     super.initInternal();
/*      */     
/*      */ 
/*      */ 
/*  936 */     this.resourceRequireExplicitRegistration = true;
/*      */     
/*  938 */     for (ContextResource cr : this.resources.values()) {
/*      */       try {
/*  940 */         MBeanUtils.createMBean(cr);
/*      */       } catch (Exception e) {
/*  942 */         log.warn(sm.getString("namingResources.mbeanCreateFail", new Object[] {cr
/*  943 */           .getName() }), e);
/*      */       }
/*      */     }
/*      */     
/*  947 */     for (ContextEnvironment ce : this.envs.values()) {
/*      */       try {
/*  949 */         MBeanUtils.createMBean(ce);
/*      */       } catch (Exception e) {
/*  951 */         log.warn(sm.getString("namingResources.mbeanCreateFail", new Object[] {ce
/*  952 */           .getName() }), e);
/*      */       }
/*      */     }
/*      */     
/*  956 */     for (ContextResourceLink crl : this.resourceLinks.values()) {
/*      */       try {
/*  958 */         MBeanUtils.createMBean(crl);
/*      */       } catch (Exception e) {
/*  960 */         log.warn(sm.getString("namingResources.mbeanCreateFail", new Object[] {crl
/*  961 */           .getName() }), e);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void startInternal()
/*      */     throws LifecycleException
/*      */   {
/*  969 */     fireLifecycleEvent("configure_start", null);
/*  970 */     setState(LifecycleState.STARTING);
/*      */   }
/*      */   
/*      */   protected void stopInternal()
/*      */     throws LifecycleException
/*      */   {
/*  976 */     cleanUp();
/*  977 */     setState(LifecycleState.STOPPING);
/*  978 */     fireLifecycleEvent("configure_stop", null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void cleanUp()
/*      */   {
/*  985 */     if (this.resources.size() == 0) {
/*  986 */       return;
/*      */     }
/*      */     try {
/*      */       javax.naming.Context ctxt;
/*  990 */       if ((this.container instanceof Server)) {
/*  991 */         ctxt = ((Server)this.container).getGlobalNamingContext();
/*      */       } else {
/*  993 */         javax.naming.Context ctxt = ContextBindings.getClassLoader();
/*  994 */         ctxt = (javax.naming.Context)ctxt.lookup("comp/env");
/*      */       }
/*      */     } catch (NamingException e) {
/*  997 */       log.warn(sm.getString("namingResources.cleanupNoContext", new Object[] { this.container }), e); return;
/*      */     }
/*      */     
/*      */     javax.naming.Context ctxt;
/* 1001 */     for (ContextResource cr : this.resources.values()) {
/* 1002 */       if (cr.getSingleton()) {
/* 1003 */         String closeMethod = cr.getCloseMethod();
/* 1004 */         if ((closeMethod != null) && (closeMethod.length() > 0)) {
/* 1005 */           String name = cr.getName();
/*      */           try
/*      */           {
/* 1008 */             resource = ctxt.lookup(name);
/*      */           } catch (NamingException e) { Object resource;
/* 1010 */             log.warn(sm.getString("namingResources.cleanupNoResource", new Object[] {cr
/*      */             
/* 1012 */               .getName(), this.container }), e); }
/* 1013 */           continue;
/*      */           Object resource;
/* 1015 */           cleanUp(resource, name, closeMethod);
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
/*      */ 
/*      */ 
/*      */   private void cleanUp(Object resource, String name, String closeMethod)
/*      */   {
/* 1032 */     Method m = null;
/*      */     try {
/* 1034 */       m = resource.getClass().getMethod(closeMethod, (Class[])null);
/*      */     } catch (SecurityException e) {
/* 1036 */       log.debug(sm.getString("namingResources.cleanupCloseSecurity", new Object[] { closeMethod, name, this.container }));
/*      */       
/* 1038 */       return;
/*      */     } catch (NoSuchMethodException e) {
/* 1040 */       log.debug(sm.getString("namingResources.cleanupNoClose", new Object[] { name, this.container, closeMethod }));
/*      */       
/* 1042 */       return;
/*      */     }
/*      */     try {
/* 1045 */       m.invoke(resource, (Object[])null);
/*      */     } catch (IllegalArgumentException|IllegalAccessException e) {
/* 1047 */       log.warn(sm.getString("namingResources.cleanupCloseFailed", new Object[] { closeMethod, name, this.container }), e);
/*      */     }
/*      */     catch (InvocationTargetException e) {
/* 1050 */       Throwable t = ExceptionUtils.unwrapInvocationTargetException(e);
/* 1051 */       ExceptionUtils.handleThrowable(t);
/* 1052 */       log.warn(sm.getString("namingResources.cleanupCloseFailed", new Object[] { closeMethod, name, this.container }), t);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void destroyInternal()
/*      */     throws LifecycleException
/*      */   {
/* 1062 */     this.resourceRequireExplicitRegistration = false;
/*      */     
/*      */ 
/* 1065 */     for (ContextResourceLink crl : this.resourceLinks.values()) {
/*      */       try {
/* 1067 */         MBeanUtils.destroyMBean(crl);
/*      */       } catch (Exception e) {
/* 1069 */         log.warn(sm.getString("namingResources.mbeanDestroyFail", new Object[] {crl
/* 1070 */           .getName() }), e);
/*      */       }
/*      */     }
/*      */     
/* 1074 */     for (ContextEnvironment ce : this.envs.values()) {
/*      */       try {
/* 1076 */         MBeanUtils.destroyMBean(ce);
/*      */       } catch (Exception e) {
/* 1078 */         log.warn(sm.getString("namingResources.mbeanDestroyFail", new Object[] {ce
/* 1079 */           .getName() }), e);
/*      */       }
/*      */     }
/*      */     
/* 1083 */     for (ContextResource cr : this.resources.values()) {
/*      */       try {
/* 1085 */         MBeanUtils.destroyMBean(cr);
/*      */       } catch (Exception e) {
/* 1087 */         log.warn(sm.getString("namingResources.mbeanDestroyFail", new Object[] {cr
/* 1088 */           .getName() }), e);
/*      */       }
/*      */     }
/*      */     
/* 1092 */     super.destroyInternal();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected String getDomainInternal()
/*      */   {
/* 1099 */     Object c = getContainer();
/*      */     
/* 1101 */     if ((c instanceof JmxEnabled)) {
/* 1102 */       return ((JmxEnabled)c).getDomain();
/*      */     }
/*      */     
/* 1105 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   protected String getObjectNameKeyProperties()
/*      */   {
/* 1111 */     Object c = getContainer();
/* 1112 */     if ((c instanceof Container)) {
/* 1113 */       return 
/* 1114 */         "type=NamingResources" + ((Container)c).getMBeanKeyProperties();
/*      */     }
/*      */     
/* 1117 */     return "type=NamingResources";
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
/*      */   private boolean checkResourceType(ResourceBase resource)
/*      */   {
/* 1133 */     if (!(this.container instanceof org.apache.catalina.Context))
/*      */     {
/* 1135 */       return true;
/*      */     }
/*      */     
/* 1138 */     if ((resource.getInjectionTargets() == null) || 
/* 1139 */       (resource.getInjectionTargets().size() == 0))
/*      */     {
/* 1141 */       return true;
/*      */     }
/*      */     
/* 1144 */     org.apache.catalina.Context context = (org.apache.catalina.Context)this.container;
/*      */     
/* 1146 */     String typeName = resource.getType();
/* 1147 */     Class<?> typeClass = null;
/* 1148 */     if (typeName != null) {
/* 1149 */       typeClass = Introspection.loadClass(context, typeName);
/* 1150 */       if (typeClass == null)
/*      */       {
/*      */ 
/* 1153 */         return true;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1158 */     Class<?> compatibleClass = getCompatibleType(context, resource, typeClass);
/* 1159 */     if (compatibleClass == null)
/*      */     {
/*      */ 
/* 1162 */       return false;
/*      */     }
/*      */     
/* 1165 */     resource.setType(compatibleClass.getCanonicalName());
/* 1166 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private Class<?> getCompatibleType(org.apache.catalina.Context context, ResourceBase resource, Class<?> typeClass)
/*      */   {
/* 1172 */     Class<?> result = null;
/*      */     
/* 1174 */     for (InjectionTarget injectionTarget : resource.getInjectionTargets()) {
/* 1175 */       Class<?> clazz = Introspection.loadClass(context, injectionTarget
/* 1176 */         .getTargetClass());
/* 1177 */       if (clazz != null)
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1183 */         String targetName = injectionTarget.getTargetName();
/*      */         
/* 1185 */         Class<?> targetType = getSetterType(clazz, targetName);
/* 1186 */         if (targetType == null)
/*      */         {
/* 1188 */           targetType = getFieldType(clazz, targetName);
/*      */         }
/* 1190 */         if (targetType != null)
/*      */         {
/*      */ 
/*      */ 
/* 1194 */           targetType = Introspection.convertPrimitiveType(targetType);
/*      */           
/* 1196 */           if (typeClass == null)
/*      */           {
/* 1198 */             if (result == null) {
/* 1199 */               result = targetType;
/* 1200 */             } else if (!targetType.isAssignableFrom(result))
/*      */             {
/* 1202 */               if (result.isAssignableFrom(targetType))
/*      */               {
/* 1204 */                 result = targetType;
/*      */               }
/*      */               else {
/* 1207 */                 return null;
/*      */               }
/*      */               
/*      */             }
/*      */           }
/* 1212 */           else if (targetType.isAssignableFrom(typeClass)) {
/* 1213 */             result = typeClass;
/*      */           }
/*      */           else
/* 1216 */             return null;
/*      */         }
/*      */       }
/*      */     }
/* 1220 */     return result;
/*      */   }
/*      */   
/*      */   private Class<?> getSetterType(Class<?> clazz, String name) {
/* 1224 */     Method[] methods = Introspection.getDeclaredMethods(clazz);
/* 1225 */     if ((methods != null) && (methods.length > 0)) {
/* 1226 */       for (Method method : methods) {
/* 1227 */         if ((Introspection.isValidSetter(method)) && 
/* 1228 */           (Introspection.getPropertyName(method).equals(name))) {
/* 1229 */           return method.getParameterTypes()[0];
/*      */         }
/*      */       }
/*      */     }
/* 1233 */     return null;
/*      */   }
/*      */   
/*      */   private Class<?> getFieldType(Class<?> clazz, String name) {
/* 1237 */     Field[] fields = Introspection.getDeclaredFields(clazz);
/* 1238 */     if ((fields != null) && (fields.length > 0)) {
/* 1239 */       for (Field field : fields) {
/* 1240 */         if (field.getName().equals(name)) {
/* 1241 */           return field.getType();
/*      */         }
/*      */       }
/*      */     }
/* 1245 */     return null;
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\catalina\deploy\NamingResourcesImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */