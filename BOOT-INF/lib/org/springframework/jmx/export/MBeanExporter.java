/*      */ package org.springframework.jmx.export;
/*      */ 
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import javax.management.DynamicMBean;
/*      */ import javax.management.JMException;
/*      */ import javax.management.MBeanException;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.MalformedObjectNameException;
/*      */ import javax.management.NotCompliantMBeanException;
/*      */ import javax.management.NotificationListener;
/*      */ import javax.management.ObjectName;
/*      */ import javax.management.StandardMBean;
/*      */ import javax.management.modelmbean.ModelMBean;
/*      */ import javax.management.modelmbean.ModelMBeanInfo;
/*      */ import javax.management.modelmbean.RequiredModelMBean;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.springframework.aop.framework.ProxyFactory;
/*      */ import org.springframework.aop.scope.ScopedProxyUtils;
/*      */ import org.springframework.aop.support.AopUtils;
/*      */ import org.springframework.aop.target.LazyInitTargetSource;
/*      */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryAware;
/*      */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*      */ import org.springframework.beans.factory.DisposableBean;
/*      */ import org.springframework.beans.factory.InitializingBean;
/*      */ import org.springframework.beans.factory.ListableBeanFactory;
/*      */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*      */ import org.springframework.beans.factory.config.BeanDefinition;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*      */ import org.springframework.core.Constants;
/*      */ import org.springframework.jmx.export.assembler.AutodetectCapableMBeanInfoAssembler;
/*      */ import org.springframework.jmx.export.assembler.MBeanInfoAssembler;
/*      */ import org.springframework.jmx.export.assembler.SimpleReflectiveMBeanInfoAssembler;
/*      */ import org.springframework.jmx.export.naming.KeyNamingStrategy;
/*      */ import org.springframework.jmx.export.naming.ObjectNamingStrategy;
/*      */ import org.springframework.jmx.export.naming.SelfNaming;
/*      */ import org.springframework.jmx.export.notification.ModelMBeanNotificationPublisher;
/*      */ import org.springframework.jmx.export.notification.NotificationPublisherAware;
/*      */ import org.springframework.jmx.support.JmxUtils;
/*      */ import org.springframework.jmx.support.MBeanRegistrationSupport;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.CollectionUtils;
/*      */ import org.springframework.util.ObjectUtils;
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
/*      */ public class MBeanExporter
/*      */   extends MBeanRegistrationSupport
/*      */   implements MBeanExportOperations, BeanClassLoaderAware, BeanFactoryAware, InitializingBean, SmartInitializingSingleton, DisposableBean
/*      */ {
/*      */   public static final int AUTODETECT_NONE = 0;
/*      */   public static final int AUTODETECT_MBEAN = 1;
/*      */   public static final int AUTODETECT_ASSEMBLER = 2;
/*      */   public static final int AUTODETECT_ALL = 3;
/*      */   private static final String WILDCARD = "*";
/*      */   private static final String MR_TYPE_OBJECT_REFERENCE = "ObjectReference";
/*      */   private static final String CONSTANT_PREFIX_AUTODETECT = "AUTODETECT_";
/*  140 */   private static final Constants constants = new Constants(MBeanExporter.class);
/*      */   
/*      */ 
/*      */   private Map<String, Object> beans;
/*      */   
/*      */ 
/*      */   private Integer autodetectMode;
/*      */   
/*      */ 
/*  149 */   private boolean allowEagerInit = false;
/*      */   
/*      */ 
/*  152 */   private MBeanInfoAssembler assembler = new SimpleReflectiveMBeanInfoAssembler();
/*      */   
/*      */ 
/*  155 */   private ObjectNamingStrategy namingStrategy = new KeyNamingStrategy();
/*      */   
/*      */ 
/*  158 */   private boolean ensureUniqueRuntimeObjectNames = true;
/*      */   
/*      */ 
/*  161 */   private boolean exposeManagedResourceClassLoader = true;
/*      */   
/*      */ 
/*  164 */   private Set<String> excludedBeans = new HashSet();
/*      */   
/*      */ 
/*      */   private MBeanExporterListener[] listeners;
/*      */   
/*      */ 
/*      */   private NotificationListenerBean[] notificationListeners;
/*      */   
/*      */ 
/*  173 */   private final Map<NotificationListenerBean, ObjectName[]> registeredNotificationListeners = new LinkedHashMap();
/*      */   
/*      */ 
/*      */ 
/*  177 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
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
/*      */   private ListableBeanFactory beanFactory;
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
/*      */   public void setBeans(Map<String, Object> beans)
/*      */   {
/*  201 */     this.beans = beans;
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
/*      */   public void setAutodetect(boolean autodetect)
/*      */   {
/*  215 */     this.autodetectMode = Integer.valueOf(autodetect ? 3 : 0);
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
/*      */   public void setAutodetectMode(int autodetectMode)
/*      */   {
/*  229 */     if (!constants.getValues("AUTODETECT_").contains(Integer.valueOf(autodetectMode))) {
/*  230 */       throw new IllegalArgumentException("Only values of autodetect constants allowed");
/*      */     }
/*  232 */     this.autodetectMode = Integer.valueOf(autodetectMode);
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
/*      */   public void setAutodetectModeName(String constantName)
/*      */   {
/*  246 */     if ((constantName == null) || (!constantName.startsWith("AUTODETECT_"))) {
/*  247 */       throw new IllegalArgumentException("Only autodetect constants allowed");
/*      */     }
/*  249 */     this.autodetectMode = ((Integer)constants.asNumber(constantName));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setAllowEagerInit(boolean allowEagerInit)
/*      */   {
/*  260 */     this.allowEagerInit = allowEagerInit;
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
/*      */   public void setAssembler(MBeanInfoAssembler assembler)
/*      */   {
/*  275 */     this.assembler = assembler;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNamingStrategy(ObjectNamingStrategy namingStrategy)
/*      */   {
/*  285 */     this.namingStrategy = namingStrategy;
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
/*      */   public void setEnsureUniqueRuntimeObjectNames(boolean ensureUniqueRuntimeObjectNames)
/*      */   {
/*  298 */     this.ensureUniqueRuntimeObjectNames = ensureUniqueRuntimeObjectNames;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setExposeManagedResourceClassLoader(boolean exposeManagedResourceClassLoader)
/*      */   {
/*  310 */     this.exposeManagedResourceClassLoader = exposeManagedResourceClassLoader;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void setExcludedBeans(String... excludedBeans)
/*      */   {
/*  317 */     this.excludedBeans.clear();
/*  318 */     if (excludedBeans != null) {
/*  319 */       this.excludedBeans.addAll(Arrays.asList(excludedBeans));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public void addExcludedBean(String excludedBean)
/*      */   {
/*  327 */     Assert.notNull(excludedBean, "ExcludedBean must not be null");
/*  328 */     this.excludedBeans.add(excludedBean);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setListeners(MBeanExporterListener... listeners)
/*      */   {
/*  337 */     this.listeners = listeners;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setNotificationListeners(NotificationListenerBean... notificationListeners)
/*      */   {
/*  349 */     this.notificationListeners = notificationListeners;
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
/*      */   public void setNotificationListenerMappings(Map<?, ? extends NotificationListener> listeners)
/*      */   {
/*  367 */     Assert.notNull(listeners, "'listeners' must not be null");
/*      */     
/*  369 */     List<NotificationListenerBean> notificationListeners = new ArrayList(listeners.size());
/*      */     
/*  371 */     for (Map.Entry<?, ? extends NotificationListener> entry : listeners.entrySet())
/*      */     {
/*  373 */       NotificationListenerBean bean = new NotificationListenerBean((NotificationListener)entry.getValue());
/*      */       
/*  375 */       Object key = entry.getKey();
/*  376 */       if ((key != null) && (!"*".equals(key)))
/*      */       {
/*  378 */         bean.setMappedObjectName(entry.getKey());
/*      */       }
/*  380 */       notificationListeners.add(bean);
/*      */     }
/*      */     
/*      */ 
/*  384 */     this.notificationListeners = ((NotificationListenerBean[])notificationListeners.toArray(new NotificationListenerBean[notificationListeners.size()]));
/*      */   }
/*      */   
/*      */   public void setBeanClassLoader(ClassLoader classLoader)
/*      */   {
/*  389 */     this.beanClassLoader = classLoader;
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
/*      */   public void setBeanFactory(BeanFactory beanFactory)
/*      */   {
/*  402 */     if ((beanFactory instanceof ListableBeanFactory)) {
/*  403 */       this.beanFactory = ((ListableBeanFactory)beanFactory);
/*      */     }
/*      */     else {
/*  406 */       this.logger.info("MBeanExporter not running in a ListableBeanFactory: autodetection of MBeans not available.");
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
/*      */   public void afterPropertiesSet()
/*      */   {
/*  419 */     if (this.server == null) {
/*  420 */       this.server = JmxUtils.locateMBeanServer();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void afterSingletonsInstantiated()
/*      */   {
/*      */     try
/*      */     {
/*  431 */       this.logger.info("Registering beans for JMX exposure on startup");
/*  432 */       registerBeans();
/*  433 */       registerNotificationListeners();
/*      */     }
/*      */     catch (RuntimeException ex)
/*      */     {
/*  437 */       unregisterNotificationListeners();
/*  438 */       unregisterBeans();
/*  439 */       throw ex;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void destroy()
/*      */   {
/*  449 */     this.logger.info("Unregistering JMX-exposed beans on shutdown");
/*  450 */     unregisterNotificationListeners();
/*  451 */     unregisterBeans();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectName registerManagedResource(Object managedResource)
/*      */     throws MBeanExportException
/*      */   {
/*  461 */     Assert.notNull(managedResource, "Managed resource must not be null");
/*      */     try
/*      */     {
/*  464 */       ObjectName objectName = getObjectName(managedResource, null);
/*  465 */       if (this.ensureUniqueRuntimeObjectNames) {
/*  466 */         objectName = JmxUtils.appendIdentityToObjectName(objectName, managedResource);
/*      */       }
/*      */     }
/*      */     catch (Throwable ex) {
/*  470 */       throw new MBeanExportException("Unable to generate ObjectName for MBean [" + managedResource + "]", ex); }
/*      */     ObjectName objectName;
/*  472 */     registerManagedResource(managedResource, objectName);
/*  473 */     return objectName;
/*      */   }
/*      */   
/*      */   public void registerManagedResource(Object managedResource, ObjectName objectName) throws MBeanExportException
/*      */   {
/*  478 */     Assert.notNull(managedResource, "Managed resource must not be null");
/*  479 */     Assert.notNull(objectName, "ObjectName must not be null");
/*      */     try {
/*  481 */       if (isMBean(managedResource.getClass())) {
/*  482 */         doRegister(managedResource, objectName);
/*      */       }
/*      */       else {
/*  485 */         ModelMBean mbean = createAndConfigureMBean(managedResource, managedResource.getClass().getName());
/*  486 */         doRegister(mbean, objectName);
/*  487 */         injectNotificationPublisherIfNecessary(managedResource, mbean, objectName);
/*      */       }
/*      */     }
/*      */     catch (JMException ex) {
/*  491 */       throw new UnableToRegisterMBeanException("Unable to register MBean [" + managedResource + "] with object name [" + objectName + "]", ex);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public void unregisterManagedResource(ObjectName objectName)
/*      */   {
/*  498 */     Assert.notNull(objectName, "ObjectName must not be null");
/*  499 */     doUnregister(objectName);
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
/*      */ 
/*      */ 
/*      */   protected void registerBeans()
/*      */   {
/*  522 */     if (this.beans == null) {
/*  523 */       this.beans = new HashMap();
/*      */       
/*  525 */       if (this.autodetectMode == null) {
/*  526 */         this.autodetectMode = Integer.valueOf(3);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  531 */     int mode = this.autodetectMode != null ? this.autodetectMode.intValue() : 0;
/*  532 */     if (mode != 0) {
/*  533 */       if (this.beanFactory == null) {
/*  534 */         throw new MBeanExportException("Cannot autodetect MBeans if not running in a BeanFactory");
/*      */       }
/*  536 */       if ((mode == 1) || (mode == 3))
/*      */       {
/*  538 */         this.logger.debug("Autodetecting user-defined JMX MBeans");
/*  539 */         autodetectMBeans();
/*      */       }
/*      */       
/*  542 */       if (((mode == 2) || (mode == 3)) && ((this.assembler instanceof AutodetectCapableMBeanInfoAssembler)))
/*      */       {
/*  544 */         autodetectBeans((AutodetectCapableMBeanInfoAssembler)this.assembler);
/*      */       }
/*      */     }
/*      */     
/*  548 */     if (!this.beans.isEmpty()) {
/*  549 */       for (Map.Entry<String, Object> entry : this.beans.entrySet()) {
/*  550 */         registerBeanNameOrInstance(entry.getValue(), (String)entry.getKey());
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
/*      */   protected boolean isBeanDefinitionLazyInit(ListableBeanFactory beanFactory, String beanName)
/*      */   {
/*  563 */     return ((beanFactory instanceof ConfigurableListableBeanFactory)) && (beanFactory.containsBeanDefinition(beanName)) && 
/*  564 */       (((ConfigurableListableBeanFactory)beanFactory).getBeanDefinition(beanName).isLazyInit());
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
/*      */   protected ObjectName registerBeanNameOrInstance(Object mapValue, String beanKey)
/*      */     throws MBeanExportException
/*      */   {
/*      */     try
/*      */     {
/*      */       Object bean;
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  590 */       if ((mapValue instanceof String))
/*      */       {
/*  592 */         if (this.beanFactory == null) {
/*  593 */           throw new MBeanExportException("Cannot resolve bean names if not running in a BeanFactory");
/*      */         }
/*  595 */         String beanName = (String)mapValue;
/*  596 */         if (isBeanDefinitionLazyInit(this.beanFactory, beanName)) {
/*  597 */           ObjectName objectName = registerLazyInit(beanName, beanKey);
/*  598 */           replaceNotificationListenerBeanNameKeysIfNecessary(beanName, objectName);
/*  599 */           return objectName;
/*      */         }
/*      */         
/*  602 */         bean = this.beanFactory.getBean(beanName);
/*  603 */         if (bean != null) {
/*  604 */           ObjectName objectName = registerBeanInstance(bean, beanKey);
/*  605 */           replaceNotificationListenerBeanNameKeysIfNecessary(beanName, objectName);
/*  606 */           return objectName;
/*      */         }
/*      */         
/*      */       }
/*  610 */       else if (mapValue != null)
/*      */       {
/*  612 */         if (this.beanFactory != null)
/*      */         {
/*  614 */           Map<String, ?> beansOfSameType = this.beanFactory.getBeansOfType(mapValue.getClass(), false, this.allowEagerInit);
/*  615 */           for (bean = beansOfSameType.entrySet().iterator(); ((Iterator)bean).hasNext();) { Map.Entry<String, ?> entry = (Map.Entry)((Iterator)bean).next();
/*  616 */             if (entry.getValue() == mapValue) {
/*  617 */               String beanName = (String)entry.getKey();
/*  618 */               ObjectName objectName = registerBeanInstance(mapValue, beanKey);
/*  619 */               replaceNotificationListenerBeanNameKeysIfNecessary(beanName, objectName);
/*  620 */               return objectName;
/*      */             }
/*      */           }
/*      */         }
/*  624 */         return registerBeanInstance(mapValue, beanKey);
/*      */       }
/*      */     }
/*      */     catch (Throwable ex) {
/*  628 */       throw new UnableToRegisterMBeanException("Unable to register MBean [" + mapValue + "] with key '" + beanKey + "'", ex);
/*      */     }
/*      */     
/*  631 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void replaceNotificationListenerBeanNameKeysIfNecessary(String beanName, ObjectName objectName)
/*      */   {
/*  642 */     if (this.notificationListeners != null) {
/*  643 */       for (NotificationListenerBean notificationListener : this.notificationListeners) {
/*  644 */         notificationListener.replaceObjectName(beanName, objectName);
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
/*      */   private ObjectName registerBeanInstance(Object bean, String beanKey)
/*      */     throws JMException
/*      */   {
/*  658 */     ObjectName objectName = getObjectName(bean, beanKey);
/*  659 */     Object mbeanToExpose = null;
/*  660 */     if (isMBean(bean.getClass())) {
/*  661 */       mbeanToExpose = bean;
/*      */     }
/*      */     else {
/*  664 */       DynamicMBean adaptedBean = adaptMBeanIfPossible(bean);
/*  665 */       if (adaptedBean != null) {
/*  666 */         mbeanToExpose = adaptedBean;
/*      */       }
/*      */     }
/*  669 */     if (mbeanToExpose != null) {
/*  670 */       if (this.logger.isInfoEnabled()) {
/*  671 */         this.logger.info("Located MBean '" + beanKey + "': registering with JMX server as MBean [" + objectName + "]");
/*      */       }
/*      */       
/*  674 */       doRegister(mbeanToExpose, objectName);
/*      */     }
/*      */     else {
/*  677 */       if (this.logger.isInfoEnabled()) {
/*  678 */         this.logger.info("Located managed bean '" + beanKey + "': registering with JMX server as MBean [" + objectName + "]");
/*      */       }
/*      */       
/*  681 */       ModelMBean mbean = createAndConfigureMBean(bean, beanKey);
/*  682 */       doRegister(mbean, objectName);
/*  683 */       injectNotificationPublisherIfNecessary(bean, mbean, objectName);
/*      */     }
/*  685 */     return objectName;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private ObjectName registerLazyInit(String beanName, String beanKey)
/*      */     throws JMException
/*      */   {
/*  697 */     ProxyFactory proxyFactory = new ProxyFactory();
/*  698 */     proxyFactory.setProxyTargetClass(true);
/*  699 */     proxyFactory.setFrozen(true);
/*      */     
/*  701 */     if (isMBean(this.beanFactory.getType(beanName)))
/*      */     {
/*  703 */       LazyInitTargetSource targetSource = new LazyInitTargetSource();
/*  704 */       targetSource.setTargetBeanName(beanName);
/*  705 */       targetSource.setBeanFactory(this.beanFactory);
/*  706 */       proxyFactory.setTargetSource(targetSource);
/*      */       
/*  708 */       Object proxy = proxyFactory.getProxy(this.beanClassLoader);
/*  709 */       ObjectName objectName = getObjectName(proxy, beanKey);
/*  710 */       if (this.logger.isDebugEnabled()) {
/*  711 */         this.logger.debug("Located MBean '" + beanKey + "': registering with JMX server as lazy-init MBean [" + objectName + "]");
/*      */       }
/*      */       
/*  714 */       doRegister(proxy, objectName);
/*  715 */       return objectName;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  720 */     NotificationPublisherAwareLazyTargetSource targetSource = new NotificationPublisherAwareLazyTargetSource(null);
/*  721 */     targetSource.setTargetBeanName(beanName);
/*  722 */     targetSource.setBeanFactory(this.beanFactory);
/*  723 */     proxyFactory.setTargetSource(targetSource);
/*      */     
/*  725 */     Object proxy = proxyFactory.getProxy(this.beanClassLoader);
/*  726 */     ObjectName objectName = getObjectName(proxy, beanKey);
/*  727 */     if (this.logger.isDebugEnabled()) {
/*  728 */       this.logger.debug("Located simple bean '" + beanKey + "': registering with JMX server as lazy-init MBean [" + objectName + "]");
/*      */     }
/*      */     
/*  731 */     ModelMBean mbean = createAndConfigureMBean(proxy, beanKey);
/*  732 */     targetSource.setModelMBean(mbean);
/*  733 */     targetSource.setObjectName(objectName);
/*  734 */     doRegister(mbean, objectName);
/*  735 */     return objectName;
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
/*      */   protected ObjectName getObjectName(Object bean, String beanKey)
/*      */     throws MalformedObjectNameException
/*      */   {
/*  751 */     if ((bean instanceof SelfNaming)) {
/*  752 */       return ((SelfNaming)bean).getObjectName();
/*      */     }
/*      */     
/*  755 */     return this.namingStrategy.getObjectName(bean, beanKey);
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
/*      */   protected boolean isMBean(Class<?> beanClass)
/*      */   {
/*  770 */     return JmxUtils.isMBean(beanClass);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DynamicMBean adaptMBeanIfPossible(Object bean)
/*      */     throws JMException
/*      */   {
/*  783 */     Class<?> targetClass = AopUtils.getTargetClass(bean);
/*  784 */     if (targetClass != bean.getClass()) {
/*  785 */       Class<?> ifc = JmxUtils.getMXBeanInterface(targetClass);
/*  786 */       if (ifc != null) {
/*  787 */         if (!ifc.isInstance(bean)) {
/*  788 */           throw new NotCompliantMBeanException("Managed bean [" + bean + "] has a target class with an MXBean interface but does not expose it in the proxy");
/*      */         }
/*      */         
/*  791 */         return new StandardMBean(bean, ifc, true);
/*      */       }
/*      */       
/*  794 */       ifc = JmxUtils.getMBeanInterface(targetClass);
/*  795 */       if (ifc != null) {
/*  796 */         if (!ifc.isInstance(bean)) {
/*  797 */           throw new NotCompliantMBeanException("Managed bean [" + bean + "] has a target class with an MBean interface but does not expose it in the proxy");
/*      */         }
/*      */         
/*  800 */         return new StandardMBean(bean, ifc);
/*      */       }
/*      */     }
/*      */     
/*  804 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ModelMBean createAndConfigureMBean(Object managedResource, String beanKey)
/*      */     throws MBeanExportException
/*      */   {
/*      */     try
/*      */     {
/*  818 */       ModelMBean mbean = createModelMBean();
/*  819 */       mbean.setModelMBeanInfo(getMBeanInfo(managedResource, beanKey));
/*  820 */       mbean.setManagedResource(managedResource, "ObjectReference");
/*  821 */       return mbean;
/*      */     }
/*      */     catch (Throwable ex) {
/*  824 */       throw new MBeanExportException("Could not create ModelMBean for managed resource [" + managedResource + "] with key '" + beanKey + "'", ex);
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
/*      */   protected ModelMBean createModelMBean()
/*      */     throws MBeanException
/*      */   {
/*  838 */     return this.exposeManagedResourceClassLoader ? new SpringModelMBean() : new RequiredModelMBean();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private ModelMBeanInfo getMBeanInfo(Object managedBean, String beanKey)
/*      */     throws JMException
/*      */   {
/*  846 */     ModelMBeanInfo info = this.assembler.getMBeanInfo(managedBean, beanKey);
/*  847 */     if ((this.logger.isWarnEnabled()) && (ObjectUtils.isEmpty(info.getAttributes())) && 
/*  848 */       (ObjectUtils.isEmpty(info.getOperations()))) {
/*  849 */       this.logger.warn("Bean with key '" + beanKey + "' has been registered as an MBean but has no exposed attributes or operations");
/*      */     }
/*      */     
/*  852 */     return info;
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
/*      */   private void autodetectBeans(final AutodetectCapableMBeanInfoAssembler assembler)
/*      */   {
/*  869 */     autodetect(new AutodetectCallback()
/*      */     {
/*      */       public boolean include(Class<?> beanClass, String beanName) {
/*  872 */         return assembler.includeBean(beanClass, beanName);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void autodetectMBeans()
/*      */   {
/*  882 */     autodetect(new AutodetectCallback()
/*      */     {
/*      */       public boolean include(Class<?> beanClass, String beanName) {
/*  885 */         return MBeanExporter.this.isMBean(beanClass);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void autodetect(AutodetectCallback callback)
/*      */   {
/*  898 */     Set<String> beanNames = new LinkedHashSet(this.beanFactory.getBeanDefinitionCount());
/*  899 */     beanNames.addAll(Arrays.asList(this.beanFactory.getBeanDefinitionNames()));
/*  900 */     if ((this.beanFactory instanceof ConfigurableBeanFactory)) {
/*  901 */       beanNames.addAll(Arrays.asList(((ConfigurableBeanFactory)this.beanFactory).getSingletonNames()));
/*      */     }
/*  903 */     for (String beanName : beanNames) {
/*  904 */       if ((!isExcluded(beanName)) && (!isBeanDefinitionAbstract(this.beanFactory, beanName))) {
/*      */         try {
/*  906 */           Class<?> beanClass = this.beanFactory.getType(beanName);
/*  907 */           if ((beanClass != null) && (callback.include(beanClass, beanName))) {
/*  908 */             boolean lazyInit = isBeanDefinitionLazyInit(this.beanFactory, beanName);
/*  909 */             Object beanInstance = !lazyInit ? this.beanFactory.getBean(beanName) : null;
/*  910 */             if ((!ScopedProxyUtils.isScopedTarget(beanName)) && (!this.beans.containsValue(beanName)) && ((beanInstance == null) || 
/*      */             
/*  912 */               (!CollectionUtils.containsInstance(this.beans.values(), beanInstance))))
/*      */             {
/*  914 */               this.beans.put(beanName, beanInstance != null ? beanInstance : beanName);
/*  915 */               if (this.logger.isInfoEnabled()) {
/*  916 */                 this.logger.info("Bean with name '" + beanName + "' has been autodetected for JMX exposure");
/*      */               }
/*      */               
/*      */             }
/*  920 */             else if (this.logger.isDebugEnabled()) {
/*  921 */               this.logger.debug("Bean with name '" + beanName + "' is already registered for JMX exposure");
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (CannotLoadBeanClassException ex)
/*      */         {
/*  927 */           if (this.allowEagerInit) {
/*  928 */             throw ex;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private boolean isExcluded(String beanName)
/*      */   {
/*  940 */     return (this.excludedBeans.contains(beanName)) || (
/*  941 */       (beanName.startsWith("&")) && 
/*  942 */       (this.excludedBeans.contains(beanName.substring("&".length()))));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean isBeanDefinitionAbstract(ListableBeanFactory beanFactory, String beanName)
/*      */   {
/*  949 */     return ((beanFactory instanceof ConfigurableListableBeanFactory)) && (beanFactory.containsBeanDefinition(beanName)) && 
/*  950 */       (((ConfigurableListableBeanFactory)beanFactory).getBeanDefinition(beanName).isAbstract());
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
/*      */   private void injectNotificationPublisherIfNecessary(Object managedResource, ModelMBean modelMBean, ObjectName objectName)
/*      */   {
/*  965 */     if ((managedResource instanceof NotificationPublisherAware)) {
/*  966 */       ((NotificationPublisherAware)managedResource).setNotificationPublisher(new ModelMBeanNotificationPublisher(modelMBean, objectName, managedResource));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void registerNotificationListeners()
/*      */     throws MBeanExportException
/*      */   {
/*  976 */     if (this.notificationListeners != null) {
/*  977 */       for (NotificationListenerBean bean : this.notificationListeners) {
/*      */         try {
/*  979 */           ObjectName[] mappedObjectNames = bean.getResolvedObjectNames();
/*  980 */           if (mappedObjectNames == null)
/*      */           {
/*  982 */             mappedObjectNames = getRegisteredObjectNames();
/*      */           }
/*  984 */           if (this.registeredNotificationListeners.put(bean, mappedObjectNames) == null) {
/*  985 */             for (ObjectName mappedObjectName : mappedObjectNames) {
/*  986 */               this.server.addNotificationListener(mappedObjectName, bean.getNotificationListener(), bean
/*  987 */                 .getNotificationFilter(), bean.getHandback());
/*      */             }
/*      */           }
/*      */         }
/*      */         catch (Throwable ex) {
/*  992 */           throw new MBeanExportException("Unable to register NotificationListener", ex);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void unregisterNotificationListeners()
/*      */   {
/* 1003 */     for (Map.Entry<NotificationListenerBean, ObjectName[]> entry : this.registeredNotificationListeners.entrySet()) {
/* 1004 */       NotificationListenerBean bean = (NotificationListenerBean)entry.getKey();
/* 1005 */       ObjectName[] mappedObjectNames = (ObjectName[])entry.getValue();
/* 1006 */       for (ObjectName mappedObjectName : mappedObjectNames) {
/*      */         try {
/* 1008 */           this.server.removeNotificationListener(mappedObjectName, bean.getNotificationListener(), bean
/* 1009 */             .getNotificationFilter(), bean.getHandback());
/*      */         }
/*      */         catch (Throwable ex) {
/* 1012 */           if (this.logger.isDebugEnabled()) {
/* 1013 */             this.logger.debug("Unable to unregister NotificationListener", ex);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1018 */     this.registeredNotificationListeners.clear();
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
/*      */   protected void onRegister(ObjectName objectName)
/*      */   {
/* 1033 */     notifyListenersOfRegistration(objectName);
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
/*      */   protected void onUnregister(ObjectName objectName)
/*      */   {
/* 1048 */     notifyListenersOfUnregistration(objectName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void notifyListenersOfRegistration(ObjectName objectName)
/*      */   {
/* 1057 */     if (this.listeners != null) {
/* 1058 */       for (MBeanExporterListener listener : this.listeners) {
/* 1059 */         listener.mbeanRegistered(objectName);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void notifyListenersOfUnregistration(ObjectName objectName)
/*      */   {
/* 1069 */     if (this.listeners != null) {
/* 1070 */       for (MBeanExporterListener listener : this.listeners) {
/* 1071 */         listener.mbeanUnregistered(objectName);
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
/*      */   private class NotificationPublisherAwareLazyTargetSource
/*      */     extends LazyInitTargetSource
/*      */   {
/*      */     private ModelMBean modelMBean;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private ObjectName objectName;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private NotificationPublisherAwareLazyTargetSource() {}
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public void setModelMBean(ModelMBean modelMBean)
/*      */     {
/* 1109 */       this.modelMBean = modelMBean;
/*      */     }
/*      */     
/*      */     public void setObjectName(ObjectName objectName) {
/* 1113 */       this.objectName = objectName;
/*      */     }
/*      */     
/*      */     public Object getTarget()
/*      */     {
/*      */       try {
/* 1119 */         return super.getTarget();
/*      */       }
/*      */       catch (RuntimeException ex) {
/* 1122 */         if (this.logger.isWarnEnabled()) {
/* 1123 */           this.logger.warn("Failed to retrieve target for JMX-exposed bean [" + this.objectName + "]: " + ex);
/*      */         }
/* 1125 */         throw ex;
/*      */       }
/*      */     }
/*      */     
/*      */     protected void postProcessTargetObject(Object targetObject)
/*      */     {
/* 1131 */       MBeanExporter.this.injectNotificationPublisherIfNecessary(targetObject, this.modelMBean, this.objectName);
/*      */     }
/*      */   }
/*      */   
/*      */   private static abstract interface AutodetectCallback
/*      */   {
/*      */     public abstract boolean include(Class<?> paramClass, String paramString);
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\jmx\export\MBeanExporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */