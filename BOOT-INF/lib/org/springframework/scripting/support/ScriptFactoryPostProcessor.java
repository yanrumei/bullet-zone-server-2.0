/*     */ package org.springframework.scripting.support;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.aop.framework.AopInfrastructureBean;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.aop.support.DelegatingIntroductionInterceptor;
/*     */ import org.springframework.asm.Type;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.MutablePropertyValues;
/*     */ import org.springframework.beans.PropertyValue;
/*     */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*     */ import org.springframework.beans.factory.BeanCreationException;
/*     */ import org.springframework.beans.factory.BeanCurrentlyInCreationException;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionValidationException;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.GenericBeanDefinition;
/*     */ import org.springframework.cglib.core.Signature;
/*     */ import org.springframework.cglib.proxy.InterfaceMaker;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.io.DefaultResourceLoader;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.scripting.ScriptFactory;
/*     */ import org.springframework.scripting.ScriptSource;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public class ScriptFactoryPostProcessor
/*     */   extends InstantiationAwareBeanPostProcessorAdapter
/*     */   implements BeanClassLoaderAware, BeanFactoryAware, ResourceLoaderAware, DisposableBean, Ordered
/*     */ {
/*     */   public static final String INLINE_SCRIPT_PREFIX = "inline:";
/* 150 */   public static final String REFRESH_CHECK_DELAY_ATTRIBUTE = Conventions.getQualifiedAttributeName(ScriptFactoryPostProcessor.class, "refreshCheckDelay");
/*     */   
/*     */ 
/* 153 */   public static final String PROXY_TARGET_CLASS_ATTRIBUTE = Conventions.getQualifiedAttributeName(ScriptFactoryPostProcessor.class, "proxyTargetClass");
/*     */   
/*     */ 
/* 156 */   public static final String LANGUAGE_ATTRIBUTE = Conventions.getQualifiedAttributeName(ScriptFactoryPostProcessor.class, "language");
/*     */   
/*     */ 
/*     */   private static final String SCRIPT_FACTORY_NAME_PREFIX = "scriptFactory.";
/*     */   
/*     */ 
/*     */   private static final String SCRIPTED_OBJECT_NAME_PREFIX = "scriptedObject.";
/*     */   
/* 164 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/* 166 */   private long defaultRefreshCheckDelay = -1L;
/*     */   
/* 168 */   private boolean defaultProxyTargetClass = false;
/*     */   
/* 170 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*     */   
/*     */   private ConfigurableBeanFactory beanFactory;
/*     */   
/* 174 */   private ResourceLoader resourceLoader = new DefaultResourceLoader();
/*     */   
/* 176 */   final DefaultListableBeanFactory scriptBeanFactory = new DefaultListableBeanFactory();
/*     */   
/*     */ 
/* 179 */   private final Map<String, ScriptSource> scriptSourceCache = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultRefreshCheckDelay(long defaultRefreshCheckDelay)
/*     */   {
/* 190 */     this.defaultRefreshCheckDelay = defaultRefreshCheckDelay;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultProxyTargetClass(boolean defaultProxyTargetClass)
/*     */   {
/* 198 */     this.defaultProxyTargetClass = defaultProxyTargetClass;
/*     */   }
/*     */   
/*     */   public void setBeanClassLoader(ClassLoader classLoader)
/*     */   {
/* 203 */     this.beanClassLoader = classLoader;
/*     */   }
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory)
/*     */   {
/* 208 */     if (!(beanFactory instanceof ConfigurableBeanFactory))
/*     */     {
/* 210 */       throw new IllegalStateException("ScriptFactoryPostProcessor doesn't work with non-ConfigurableBeanFactory: " + beanFactory.getClass());
/*     */     }
/* 212 */     this.beanFactory = ((ConfigurableBeanFactory)beanFactory);
/*     */     
/*     */ 
/* 215 */     this.scriptBeanFactory.setParentBeanFactory(this.beanFactory);
/*     */     
/*     */ 
/* 218 */     this.scriptBeanFactory.copyConfigurationFrom(this.beanFactory);
/*     */     
/*     */ 
/*     */ 
/* 222 */     for (Iterator<BeanPostProcessor> it = this.scriptBeanFactory.getBeanPostProcessors().iterator(); it.hasNext();) {
/* 223 */       if ((it.next() instanceof AopInfrastructureBean)) {
/* 224 */         it.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setResourceLoader(ResourceLoader resourceLoader)
/*     */   {
/* 231 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 236 */     return Integer.MIN_VALUE;
/*     */   }
/*     */   
/*     */ 
/*     */   public Class<?> predictBeanType(Class<?> beanClass, String beanName)
/*     */   {
/* 242 */     if (!ScriptFactory.class.isAssignableFrom(beanClass)) {
/* 243 */       return null;
/*     */     }
/*     */     
/* 246 */     BeanDefinition bd = this.beanFactory.getMergedBeanDefinition(beanName);
/*     */     try
/*     */     {
/* 249 */       String scriptFactoryBeanName = "scriptFactory." + beanName;
/* 250 */       String scriptedObjectBeanName = "scriptedObject." + beanName;
/* 251 */       prepareScriptBeans(bd, scriptFactoryBeanName, scriptedObjectBeanName);
/*     */       
/* 253 */       ScriptFactory scriptFactory = (ScriptFactory)this.scriptBeanFactory.getBean(scriptFactoryBeanName, ScriptFactory.class);
/* 254 */       ScriptSource scriptSource = getScriptSource(scriptFactoryBeanName, scriptFactory.getScriptSourceLocator());
/* 255 */       Class<?>[] interfaces = scriptFactory.getScriptInterfaces();
/*     */       
/* 257 */       Class<?> scriptedType = scriptFactory.getScriptedObjectType(scriptSource);
/* 258 */       if (scriptedType != null) {
/* 259 */         return scriptedType;
/*     */       }
/* 261 */       if (!ObjectUtils.isEmpty(interfaces)) {
/* 262 */         return interfaces.length == 1 ? interfaces[0] : createCompositeInterface(interfaces);
/*     */       }
/*     */       
/* 265 */       if (bd.isSingleton()) {
/* 266 */         Object bean = this.scriptBeanFactory.getBean(scriptedObjectBeanName);
/* 267 */         if (bean != null) {
/* 268 */           return bean.getClass();
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 274 */       if (((ex instanceof BeanCreationException)) && 
/* 275 */         ((((BeanCreationException)ex).getMostSpecificCause() instanceof BeanCurrentlyInCreationException))) {
/* 276 */         if (this.logger.isTraceEnabled()) {
/* 277 */           this.logger.trace("Could not determine scripted object type for bean '" + beanName + "': " + ex
/* 278 */             .getMessage());
/*     */         }
/*     */         
/*     */       }
/* 282 */       else if (this.logger.isDebugEnabled()) {
/* 283 */         this.logger.debug("Could not determine scripted object type for bean '" + beanName + "'", ex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 288 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName)
/*     */   {
/* 294 */     if (!ScriptFactory.class.isAssignableFrom(beanClass)) {
/* 295 */       return null;
/*     */     }
/*     */     
/* 298 */     BeanDefinition bd = this.beanFactory.getMergedBeanDefinition(beanName);
/* 299 */     String scriptFactoryBeanName = "scriptFactory." + beanName;
/* 300 */     String scriptedObjectBeanName = "scriptedObject." + beanName;
/* 301 */     prepareScriptBeans(bd, scriptFactoryBeanName, scriptedObjectBeanName);
/*     */     
/* 303 */     ScriptFactory scriptFactory = (ScriptFactory)this.scriptBeanFactory.getBean(scriptFactoryBeanName, ScriptFactory.class);
/* 304 */     ScriptSource scriptSource = getScriptSource(scriptFactoryBeanName, scriptFactory.getScriptSourceLocator());
/* 305 */     boolean isFactoryBean = false;
/*     */     try {
/* 307 */       Class<?> scriptedObjectType = scriptFactory.getScriptedObjectType(scriptSource);
/*     */       
/* 309 */       if (scriptedObjectType != null) {
/* 310 */         isFactoryBean = FactoryBean.class.isAssignableFrom(scriptedObjectType);
/*     */       }
/*     */     }
/*     */     catch (Exception ex) {
/* 314 */       throw new BeanCreationException(beanName, "Could not determine scripted object type for " + scriptFactory, ex);
/*     */     }
/*     */     
/*     */ 
/* 318 */     long refreshCheckDelay = resolveRefreshCheckDelay(bd);
/* 319 */     if (refreshCheckDelay >= 0L) {
/* 320 */       Class<?>[] interfaces = scriptFactory.getScriptInterfaces();
/* 321 */       RefreshableScriptTargetSource ts = new RefreshableScriptTargetSource(this.scriptBeanFactory, scriptedObjectBeanName, scriptFactory, scriptSource, isFactoryBean);
/*     */       
/* 323 */       boolean proxyTargetClass = resolveProxyTargetClass(bd);
/* 324 */       String language = (String)bd.getAttribute(LANGUAGE_ATTRIBUTE);
/* 325 */       if ((proxyTargetClass) && ((language == null) || (!language.equals("groovy")))) {
/* 326 */         throw new BeanDefinitionValidationException("Cannot use proxyTargetClass=true with script beans where language is not 'groovy': '" + language + "'");
/*     */       }
/*     */       
/*     */ 
/* 330 */       ts.setRefreshCheckDelay(refreshCheckDelay);
/* 331 */       return createRefreshableProxy(ts, interfaces, proxyTargetClass);
/*     */     }
/*     */     
/* 334 */     if (isFactoryBean) {
/* 335 */       scriptedObjectBeanName = "&" + scriptedObjectBeanName;
/*     */     }
/* 337 */     return this.scriptBeanFactory.getBean(scriptedObjectBeanName);
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
/*     */   protected void prepareScriptBeans(BeanDefinition bd, String scriptFactoryBeanName, String scriptedObjectBeanName)
/*     */   {
/* 350 */     synchronized (this.scriptBeanFactory) {
/* 351 */       if (!this.scriptBeanFactory.containsBeanDefinition(scriptedObjectBeanName))
/*     */       {
/* 353 */         this.scriptBeanFactory.registerBeanDefinition(scriptFactoryBeanName, 
/* 354 */           createScriptFactoryBeanDefinition(bd));
/*     */         
/* 356 */         ScriptFactory scriptFactory = (ScriptFactory)this.scriptBeanFactory.getBean(scriptFactoryBeanName, ScriptFactory.class);
/*     */         
/* 358 */         ScriptSource scriptSource = getScriptSource(scriptFactoryBeanName, scriptFactory.getScriptSourceLocator());
/* 359 */         Class<?>[] interfaces = scriptFactory.getScriptInterfaces();
/*     */         
/* 361 */         Class<?>[] scriptedInterfaces = interfaces;
/* 362 */         if ((scriptFactory.requiresConfigInterface()) && (!bd.getPropertyValues().isEmpty())) {
/* 363 */           Class<?> configInterface = createConfigInterface(bd, interfaces);
/* 364 */           scriptedInterfaces = (Class[])ObjectUtils.addObjectToArray(interfaces, configInterface);
/*     */         }
/*     */         
/* 367 */         BeanDefinition objectBd = createScriptedObjectBeanDefinition(bd, scriptFactoryBeanName, scriptSource, scriptedInterfaces);
/*     */         
/* 369 */         long refreshCheckDelay = resolveRefreshCheckDelay(bd);
/* 370 */         if (refreshCheckDelay >= 0L) {
/* 371 */           objectBd.setScope("prototype");
/*     */         }
/*     */         
/* 374 */         this.scriptBeanFactory.registerBeanDefinition(scriptedObjectBeanName, objectBd);
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
/*     */   protected long resolveRefreshCheckDelay(BeanDefinition beanDefinition)
/*     */   {
/* 390 */     long refreshCheckDelay = this.defaultRefreshCheckDelay;
/* 391 */     Object attributeValue = beanDefinition.getAttribute(REFRESH_CHECK_DELAY_ATTRIBUTE);
/* 392 */     if ((attributeValue instanceof Number)) {
/* 393 */       refreshCheckDelay = ((Number)attributeValue).longValue();
/*     */     }
/* 395 */     else if ((attributeValue instanceof String)) {
/* 396 */       refreshCheckDelay = Long.parseLong((String)attributeValue);
/*     */     }
/* 398 */     else if (attributeValue != null) {
/* 399 */       throw new BeanDefinitionStoreException("Invalid refresh check delay attribute [" + REFRESH_CHECK_DELAY_ATTRIBUTE + "] with value '" + attributeValue + "': needs to be of type Number or String");
/*     */     }
/*     */     
/*     */ 
/* 403 */     return refreshCheckDelay;
/*     */   }
/*     */   
/*     */   protected boolean resolveProxyTargetClass(BeanDefinition beanDefinition) {
/* 407 */     boolean proxyTargetClass = this.defaultProxyTargetClass;
/* 408 */     Object attributeValue = beanDefinition.getAttribute(PROXY_TARGET_CLASS_ATTRIBUTE);
/* 409 */     if ((attributeValue instanceof Boolean)) {
/* 410 */       proxyTargetClass = ((Boolean)attributeValue).booleanValue();
/*     */     }
/* 412 */     else if ((attributeValue instanceof String)) {
/* 413 */       proxyTargetClass = Boolean.valueOf((String)attributeValue).booleanValue();
/*     */     }
/* 415 */     else if (attributeValue != null) {
/* 416 */       throw new BeanDefinitionStoreException("Invalid proxy target class attribute [" + PROXY_TARGET_CLASS_ATTRIBUTE + "] with value '" + attributeValue + "': needs to be of type Boolean or String");
/*     */     }
/*     */     
/*     */ 
/* 420 */     return proxyTargetClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanDefinition createScriptFactoryBeanDefinition(BeanDefinition bd)
/*     */   {
/* 432 */     GenericBeanDefinition scriptBd = new GenericBeanDefinition();
/* 433 */     scriptBd.setBeanClassName(bd.getBeanClassName());
/* 434 */     scriptBd.getConstructorArgumentValues().addArgumentValues(bd.getConstructorArgumentValues());
/* 435 */     return scriptBd;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ScriptSource getScriptSource(String beanName, String scriptSourceLocator)
/*     */   {
/* 447 */     synchronized (this.scriptSourceCache) {
/* 448 */       ScriptSource scriptSource = (ScriptSource)this.scriptSourceCache.get(beanName);
/* 449 */       if (scriptSource == null) {
/* 450 */         scriptSource = convertToScriptSource(beanName, scriptSourceLocator, this.resourceLoader);
/* 451 */         this.scriptSourceCache.put(beanName, scriptSource);
/*     */       }
/* 453 */       return scriptSource;
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
/*     */   protected ScriptSource convertToScriptSource(String beanName, String scriptSourceLocator, ResourceLoader resourceLoader)
/*     */   {
/* 470 */     if (scriptSourceLocator.startsWith("inline:")) {
/* 471 */       return new StaticScriptSource(scriptSourceLocator.substring("inline:".length()), beanName);
/*     */     }
/*     */     
/* 474 */     return new ResourceScriptSource(resourceLoader.getResource(scriptSourceLocator));
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
/*     */   protected Class<?> createConfigInterface(BeanDefinition bd, Class<?>[] interfaces)
/*     */   {
/* 493 */     InterfaceMaker maker = new InterfaceMaker();
/* 494 */     PropertyValue[] pvs = bd.getPropertyValues().getPropertyValues();
/* 495 */     for (PropertyValue pv : pvs) {
/* 496 */       String propertyName = pv.getName();
/* 497 */       Class<?> propertyType = BeanUtils.findPropertyType(propertyName, interfaces);
/* 498 */       String setterName = "set" + StringUtils.capitalize(propertyName);
/* 499 */       Signature signature = new Signature(setterName, Type.VOID_TYPE, new Type[] { Type.getType(propertyType) });
/* 500 */       maker.add(signature, new Type[0]);
/*     */     }
/* 502 */     if ((bd instanceof AbstractBeanDefinition)) {
/* 503 */       AbstractBeanDefinition abd = (AbstractBeanDefinition)bd;
/* 504 */       if (abd.getInitMethodName() != null) {
/* 505 */         Signature signature = new Signature(abd.getInitMethodName(), Type.VOID_TYPE, new Type[0]);
/* 506 */         maker.add(signature, new Type[0]);
/*     */       }
/* 508 */       if (StringUtils.hasText(abd.getDestroyMethodName())) {
/* 509 */         Signature signature = new Signature(abd.getDestroyMethodName(), Type.VOID_TYPE, new Type[0]);
/* 510 */         maker.add(signature, new Type[0]);
/*     */       }
/*     */     }
/* 513 */     return maker.create();
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
/*     */   protected Class<?> createCompositeInterface(Class<?>[] interfaces)
/*     */   {
/* 526 */     return ClassUtils.createCompositeInterface(interfaces, this.beanClassLoader);
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
/*     */   protected BeanDefinition createScriptedObjectBeanDefinition(BeanDefinition bd, String scriptFactoryBeanName, ScriptSource scriptSource, Class<?>[] interfaces)
/*     */   {
/* 543 */     GenericBeanDefinition objectBd = new GenericBeanDefinition(bd);
/* 544 */     objectBd.setFactoryBeanName(scriptFactoryBeanName);
/* 545 */     objectBd.setFactoryMethodName("getScriptedObject");
/* 546 */     objectBd.getConstructorArgumentValues().clear();
/* 547 */     objectBd.getConstructorArgumentValues().addIndexedArgumentValue(0, scriptSource);
/* 548 */     objectBd.getConstructorArgumentValues().addIndexedArgumentValue(1, interfaces);
/* 549 */     return objectBd;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object createRefreshableProxy(TargetSource ts, Class<?>[] interfaces, boolean proxyTargetClass)
/*     */   {
/* 561 */     ProxyFactory proxyFactory = new ProxyFactory();
/* 562 */     proxyFactory.setTargetSource(ts);
/* 563 */     ClassLoader classLoader = this.beanClassLoader;
/*     */     
/* 565 */     if (interfaces == null) {
/* 566 */       interfaces = ClassUtils.getAllInterfacesForClass(ts.getTargetClass(), this.beanClassLoader);
/*     */     }
/* 568 */     proxyFactory.setInterfaces(interfaces);
/* 569 */     if (proxyTargetClass) {
/* 570 */       classLoader = null;
/* 571 */       proxyFactory.setProxyTargetClass(true);
/*     */     }
/*     */     
/* 574 */     DelegatingIntroductionInterceptor introduction = new DelegatingIntroductionInterceptor(ts);
/* 575 */     introduction.suppressInterface(TargetSource.class);
/* 576 */     proxyFactory.addAdvice(introduction);
/*     */     
/* 578 */     return proxyFactory.getProxy(classLoader);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void destroy()
/*     */   {
/* 586 */     this.scriptBeanFactory.destroySingletons();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scripting\support\ScriptFactoryPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */