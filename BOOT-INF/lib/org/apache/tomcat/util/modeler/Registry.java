/*     */ package org.apache.tomcat.util.modeler;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.InputStream;
/*     */ import java.lang.management.ManagementFactory;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.management.DynamicMBean;
/*     */ import javax.management.MBeanAttributeInfo;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.MBeanOperationInfo;
/*     */ import javax.management.MBeanRegistration;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MBeanServerFactory;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.juli.logging.Log;
/*     */ import org.apache.juli.logging.LogFactory;
/*     */ import org.apache.tomcat.util.modeler.modules.ModelerSource;
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
/*     */ public class Registry
/*     */   implements RegistryMBean, MBeanRegistration
/*     */ {
/*  74 */   private static final Log log = LogFactory.getLog(Registry.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  80 */   private static final HashMap<Object, Registry> perLoaderRegistries = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */   private static Registry registry = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  94 */   private MBeanServer server = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 100 */   private HashMap<String, ManagedBean> descriptors = new HashMap();
/*     */   
/*     */ 
/*     */ 
/* 104 */   private HashMap<String, ManagedBean> descriptorsByClass = new HashMap();
/*     */   
/*     */ 
/* 107 */   private HashMap<String, URL> searchedPaths = new HashMap();
/*     */   
/*     */ 
/*     */   private Object guard;
/*     */   
/*     */ 
/* 113 */   private final Hashtable<String, Hashtable<String, Integer>> idDomains = new Hashtable();
/*     */   
/* 115 */   private final Hashtable<String, int[]> ids = new Hashtable();
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
/*     */   public static synchronized Registry getRegistry(Object key, Object guard)
/*     */   {
/* 145 */     if (perLoaderRegistries != null) {
/* 146 */       if (key == null)
/* 147 */         key = Thread.currentThread().getContextClassLoader();
/* 148 */       if (key != null) {
/* 149 */         Registry localRegistry = (Registry)perLoaderRegistries.get(key);
/* 150 */         if (localRegistry == null) {
/* 151 */           localRegistry = new Registry();
/*     */           
/* 153 */           localRegistry.guard = guard;
/* 154 */           perLoaderRegistries.put(key, localRegistry);
/* 155 */           return localRegistry;
/*     */         }
/* 157 */         if ((localRegistry.guard != null) && (localRegistry.guard != guard))
/*     */         {
/* 159 */           return null;
/*     */         }
/* 161 */         return localRegistry;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 166 */     if (registry == null) {
/* 167 */       registry = new Registry();
/*     */     }
/* 169 */     if ((registry.guard != null) && (registry.guard != guard))
/*     */     {
/* 171 */       return null;
/*     */     }
/* 173 */     return registry;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void stop()
/*     */   {
/* 185 */     this.descriptorsByClass = new HashMap();
/* 186 */     this.descriptors = new HashMap();
/* 187 */     this.searchedPaths = new HashMap();
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
/*     */   public void registerComponent(Object bean, String oname, String type)
/*     */     throws Exception
/*     */   {
/* 224 */     registerComponent(bean, new ObjectName(oname), type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void unregisterComponent(String oname)
/*     */   {
/*     */     try
/*     */     {
/* 238 */       unregisterComponent(new ObjectName(oname));
/*     */     } catch (MalformedObjectNameException e) {
/* 240 */       log.info("Error creating object name " + e);
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
/*     */   public void invoke(List<ObjectName> mbeans, String operation, boolean failFirst)
/*     */     throws Exception
/*     */   {
/* 258 */     if (mbeans == null) {
/* 259 */       return;
/*     */     }
/* 261 */     Iterator<ObjectName> itr = mbeans.iterator();
/* 262 */     while (itr.hasNext()) {
/* 263 */       ObjectName current = (ObjectName)itr.next();
/*     */       try {
/* 265 */         if ((current != null) && 
/*     */         
/*     */ 
/* 268 */           (getMethodInfo(current, operation) != null))
/*     */         {
/*     */ 
/* 271 */           getMBeanServer().invoke(current, operation, new Object[0], new String[0]);
/*     */         }
/*     */       }
/*     */       catch (Exception t) {
/* 275 */         if (failFirst) throw t;
/* 276 */         log.info("Error initializing " + current + " " + t.toString());
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
/*     */   public synchronized int getId(String domain, String name)
/*     */   {
/* 294 */     if (domain == null) {
/* 295 */       domain = "";
/*     */     }
/* 297 */     Hashtable<String, Integer> domainTable = (Hashtable)this.idDomains.get(domain);
/* 298 */     if (domainTable == null) {
/* 299 */       domainTable = new Hashtable();
/* 300 */       this.idDomains.put(domain, domainTable);
/*     */     }
/* 302 */     if (name == null) {
/* 303 */       name = "";
/*     */     }
/* 305 */     Integer i = (Integer)domainTable.get(name);
/*     */     
/* 307 */     if (i != null) {
/* 308 */       return i.intValue();
/*     */     }
/*     */     
/* 311 */     int[] id = (int[])this.ids.get(domain);
/* 312 */     if (id == null) {
/* 313 */       id = new int[1];
/* 314 */       this.ids.put(domain, id);
/*     */     }
/* 316 */     int tmp106_105 = 0; int[] tmp106_103 = id; int tmp108_107 = tmp106_103[tmp106_105];tmp106_103[tmp106_105] = (tmp108_107 + 1);int code = tmp108_107;
/* 317 */     domainTable.put(name, Integer.valueOf(code));
/* 318 */     return code;
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
/*     */   public void addManagedBean(ManagedBean bean)
/*     */   {
/* 333 */     this.descriptors.put(bean.getName(), bean);
/* 334 */     if (bean.getType() != null) {
/* 335 */       this.descriptorsByClass.put(bean.getType(), bean);
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
/*     */   public ManagedBean findManagedBean(String name)
/*     */   {
/* 351 */     ManagedBean mb = (ManagedBean)this.descriptors.get(name);
/* 352 */     if (mb == null)
/* 353 */       mb = (ManagedBean)this.descriptorsByClass.get(name);
/* 354 */     return mb;
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
/*     */   public String getType(ObjectName oname, String attName)
/*     */   {
/* 369 */     String type = null;
/* 370 */     MBeanInfo info = null;
/*     */     try {
/* 372 */       info = this.server.getMBeanInfo(oname);
/*     */     } catch (Exception e) {
/* 374 */       log.info("Can't find metadata for object" + oname);
/* 375 */       return null;
/*     */     }
/*     */     
/* 378 */     MBeanAttributeInfo[] attInfo = info.getAttributes();
/* 379 */     for (int i = 0; i < attInfo.length; i++) {
/* 380 */       if (attName.equals(attInfo[i].getName())) {
/* 381 */         type = attInfo[i].getType();
/* 382 */         return type;
/*     */       }
/*     */     }
/* 385 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MBeanOperationInfo getMethodInfo(ObjectName oname, String opName)
/*     */   {
/* 397 */     MBeanInfo info = null;
/*     */     try {
/* 399 */       info = this.server.getMBeanInfo(oname);
/*     */     } catch (Exception e) {
/* 401 */       log.info("Can't find metadata " + oname);
/* 402 */       return null;
/*     */     }
/* 404 */     MBeanOperationInfo[] attInfo = info.getOperations();
/* 405 */     for (int i = 0; i < attInfo.length; i++) {
/* 406 */       if (opName.equals(attInfo[i].getName())) {
/* 407 */         return attInfo[i];
/*     */       }
/*     */     }
/* 410 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void unregisterComponent(ObjectName oname)
/*     */   {
/*     */     try
/*     */     {
/* 421 */       if ((oname != null) && (getMBeanServer().isRegistered(oname))) {
/* 422 */         getMBeanServer().unregisterMBean(oname);
/*     */       }
/*     */     } catch (Throwable t) {
/* 425 */       log.error("Error unregistering mbean", t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized MBeanServer getMBeanServer()
/*     */   {
/* 435 */     if (this.server == null) {
/* 436 */       long t1 = System.currentTimeMillis();
/* 437 */       if (MBeanServerFactory.findMBeanServer(null).size() > 0) {
/* 438 */         this.server = ((MBeanServer)MBeanServerFactory.findMBeanServer(null).get(0));
/* 439 */         if (log.isDebugEnabled()) {
/* 440 */           log.debug("Using existing MBeanServer " + (System.currentTimeMillis() - t1));
/*     */         }
/*     */       } else {
/* 443 */         this.server = ManagementFactory.getPlatformMBeanServer();
/* 444 */         if (log.isDebugEnabled()) {
/* 445 */           log.debug("Creating MBeanServer" + (System.currentTimeMillis() - t1));
/*     */         }
/*     */       }
/*     */     }
/* 449 */     return this.server;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ManagedBean findManagedBean(Object bean, Class<?> beanClass, String type)
/*     */     throws Exception
/*     */   {
/* 462 */     if ((bean != null) && (beanClass == null)) {
/* 463 */       beanClass = bean.getClass();
/*     */     }
/*     */     
/* 466 */     if (type == null) {
/* 467 */       type = beanClass.getName();
/*     */     }
/*     */     
/*     */ 
/* 471 */     ManagedBean managed = findManagedBean(type);
/*     */     
/*     */ 
/* 474 */     if (managed == null)
/*     */     {
/* 476 */       if (log.isDebugEnabled()) {
/* 477 */         log.debug("Looking for descriptor ");
/*     */       }
/* 479 */       findDescriptor(beanClass, type);
/*     */       
/* 481 */       managed = findManagedBean(type);
/*     */     }
/*     */     
/*     */ 
/* 485 */     if (managed == null) {
/* 486 */       if (log.isDebugEnabled()) {
/* 487 */         log.debug("Introspecting ");
/*     */       }
/*     */       
/*     */ 
/* 491 */       load("MbeansDescriptorsIntrospectionSource", beanClass, type);
/*     */       
/* 493 */       managed = findManagedBean(type);
/* 494 */       if (managed == null) {
/* 495 */         log.warn("No metadata found for " + type);
/* 496 */         return null;
/*     */       }
/* 498 */       managed.setName(type);
/* 499 */       addManagedBean(managed);
/*     */     }
/* 501 */     return managed;
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
/*     */   public Object convertValue(String type, String value)
/*     */   {
/* 516 */     Object objValue = value;
/*     */     
/* 518 */     if ((type == null) || ("java.lang.String".equals(type)))
/*     */     {
/* 520 */       objValue = value;
/* 521 */     } else if (("javax.management.ObjectName".equals(type)) || 
/* 522 */       ("ObjectName".equals(type))) {
/*     */       try {
/* 524 */         objValue = new ObjectName(value);
/*     */       } catch (MalformedObjectNameException e) {
/* 526 */         return null;
/*     */       }
/* 528 */     } else if (("java.lang.Integer".equals(type)) || 
/* 529 */       ("int".equals(type))) {
/* 530 */       objValue = Integer.valueOf(value);
/* 531 */     } else if (("java.lang.Long".equals(type)) || 
/* 532 */       ("long".equals(type))) {
/* 533 */       objValue = Long.valueOf(value);
/* 534 */     } else if (("java.lang.Boolean".equals(type)) || 
/* 535 */       ("boolean".equals(type))) {
/* 536 */       objValue = Boolean.valueOf(value);
/*     */     }
/* 538 */     return objValue;
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
/*     */   public List<ObjectName> load(String sourceType, Object source, String param)
/*     */     throws Exception
/*     */   {
/* 552 */     if (log.isTraceEnabled()) {
/* 553 */       log.trace("load " + source);
/*     */     }
/* 555 */     String location = null;
/* 556 */     String type = null;
/* 557 */     Object inputsource = null;
/*     */     
/* 559 */     if ((source instanceof URL)) {
/* 560 */       URL url = (URL)source;
/* 561 */       location = url.toString();
/* 562 */       type = param;
/* 563 */       inputsource = url.openStream();
/* 564 */       if ((sourceType == null) && (location.endsWith(".xml"))) {
/* 565 */         sourceType = "MbeansDescriptorsDigesterSource";
/*     */       }
/* 567 */     } else if ((source instanceof File)) {
/* 568 */       location = ((File)source).getAbsolutePath();
/* 569 */       inputsource = new FileInputStream((File)source);
/* 570 */       type = param;
/* 571 */       if ((sourceType == null) && (location.endsWith(".xml"))) {
/* 572 */         sourceType = "MbeansDescriptorsDigesterSource";
/*     */       }
/* 574 */     } else if ((source instanceof InputStream)) {
/* 575 */       type = param;
/* 576 */       inputsource = source;
/* 577 */     } else if ((source instanceof Class)) {
/* 578 */       location = ((Class)source).getName();
/* 579 */       type = param;
/* 580 */       inputsource = source;
/* 581 */       if (sourceType == null) {
/* 582 */         sourceType = "MbeansDescriptorsIntrospectionSource";
/*     */       }
/*     */     }
/*     */     
/* 586 */     if (sourceType == null) {
/* 587 */       sourceType = "MbeansDescriptorsDigesterSource";
/*     */     }
/* 589 */     ModelerSource ds = getModelerSource(sourceType);
/*     */     
/* 591 */     List<ObjectName> mbeans = ds.loadDescriptors(this, type, inputsource);
/*     */     
/* 593 */     return mbeans;
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
/*     */   public void registerComponent(Object bean, ObjectName oname, String type)
/*     */     throws Exception
/*     */   {
/* 608 */     if (log.isDebugEnabled()) {
/* 609 */       log.debug("Managed= " + oname);
/*     */     }
/*     */     
/* 612 */     if (bean == null) {
/* 613 */       log.error("Null component " + oname);
/* 614 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 618 */       if (type == null) {
/* 619 */         type = bean.getClass().getName();
/*     */       }
/*     */       
/* 622 */       ManagedBean managed = findManagedBean(null, bean.getClass(), type);
/*     */       
/*     */ 
/* 625 */       DynamicMBean mbean = managed.createMBean(bean);
/*     */       
/* 627 */       if (getMBeanServer().isRegistered(oname)) {
/* 628 */         if (log.isDebugEnabled()) {
/* 629 */           log.debug("Unregistering existing component " + oname);
/*     */         }
/* 631 */         getMBeanServer().unregisterMBean(oname);
/*     */       }
/*     */       
/* 634 */       getMBeanServer().registerMBean(mbean, oname);
/*     */     } catch (Exception ex) {
/* 636 */       log.error("Error registering " + oname, ex);
/* 637 */       throw ex;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void loadDescriptors(String packageName, ClassLoader classLoader)
/*     */   {
/* 649 */     String res = packageName.replace('.', '/');
/*     */     
/* 651 */     if (log.isTraceEnabled()) {
/* 652 */       log.trace("Finding descriptor " + res);
/*     */     }
/*     */     
/* 655 */     if (this.searchedPaths.get(packageName) != null) {
/* 656 */       return;
/*     */     }
/*     */     
/* 659 */     String descriptors = res + "/mbeans-descriptors.xml";
/* 660 */     URL dURL = classLoader.getResource(descriptors);
/*     */     
/* 662 */     if (dURL == null) {
/* 663 */       return;
/*     */     }
/*     */     
/* 666 */     log.debug("Found " + dURL);
/* 667 */     this.searchedPaths.put(packageName, dURL);
/*     */     try {
/* 669 */       load("MbeansDescriptorsDigesterSource", dURL, null);
/*     */     } catch (Exception ex) {
/* 671 */       log.error("Error loading " + dURL);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void findDescriptor(Class<?> beanClass, String type)
/*     */   {
/* 680 */     if (type == null) {
/* 681 */       type = beanClass.getName();
/*     */     }
/* 683 */     ClassLoader classLoader = null;
/* 684 */     if (beanClass != null) {
/* 685 */       classLoader = beanClass.getClassLoader();
/*     */     }
/* 687 */     if (classLoader == null) {
/* 688 */       classLoader = Thread.currentThread().getContextClassLoader();
/*     */     }
/* 690 */     if (classLoader == null) {
/* 691 */       classLoader = getClass().getClassLoader();
/*     */     }
/*     */     
/* 694 */     String className = type;
/* 695 */     String pkg = className;
/* 696 */     while (pkg.indexOf(".") > 0) {
/* 697 */       int lastComp = pkg.lastIndexOf(".");
/* 698 */       if (lastComp <= 0) return;
/* 699 */       pkg = pkg.substring(0, lastComp);
/* 700 */       if (this.searchedPaths.get(pkg) != null) {
/* 701 */         return;
/*     */       }
/* 703 */       loadDescriptors(pkg, classLoader);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private ModelerSource getModelerSource(String type)
/*     */     throws Exception
/*     */   {
/* 711 */     if (type == null) type = "MbeansDescriptorsDigesterSource";
/* 712 */     if (type.indexOf(".") < 0) {
/* 713 */       type = "org.apache.tomcat.util.modeler.modules." + type;
/*     */     }
/*     */     
/* 716 */     Class<?> c = Class.forName(type);
/* 717 */     ModelerSource ds = (ModelerSource)c.getConstructor(new Class[0]).newInstance(new Object[0]);
/* 718 */     return ds;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectName preRegister(MBeanServer server, ObjectName name)
/*     */     throws Exception
/*     */   {
/* 728 */     this.server = server;
/* 729 */     return name;
/*     */   }
/*     */   
/*     */   public void postRegister(Boolean registrationDone) {}
/*     */   
/*     */   public void preDeregister()
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */   public void postDeregister() {}
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-core-8.5.27.jar!\org\apache\tomca\\util\modeler\Registry.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */