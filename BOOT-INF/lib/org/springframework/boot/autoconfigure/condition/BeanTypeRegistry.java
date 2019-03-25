/*     */ package org.springframework.boot.autoconfigure.condition;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.CannotLoadBeanClassException;
/*     */ import org.springframework.beans.factory.FactoryBean;
/*     */ import org.springframework.beans.factory.ListableBeanFactory;
/*     */ import org.springframework.beans.factory.SmartInitializingSingleton;
/*     */ import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.config.ConstructorArgumentValues;
/*     */ import org.springframework.beans.factory.support.AbstractBeanDefinition;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.RootBeanDefinition;
/*     */ import org.springframework.core.ResolvableType;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.type.MethodMetadata;
/*     */ import org.springframework.core.type.StandardMethodMetadata;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ 
/*     */ 
/*     */ final class BeanTypeRegistry
/*     */   implements SmartInitializingSingleton
/*     */ {
/*  69 */   private static final Log logger = LogFactory.getLog(BeanTypeRegistry.class);
/*     */   
/*     */   static final String FACTORY_BEAN_OBJECT_TYPE = "factoryBeanObjectType";
/*     */   
/*  73 */   private static final String BEAN_NAME = BeanTypeRegistry.class.getName();
/*     */   
/*     */   private final DefaultListableBeanFactory beanFactory;
/*     */   
/*  77 */   private final Map<String, Class<?>> beanTypes = new HashMap();
/*     */   
/*  79 */   private int lastBeanDefinitionCount = 0;
/*     */   
/*     */   private BeanTypeRegistry(DefaultListableBeanFactory beanFactory) {
/*  82 */     this.beanFactory = beanFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static BeanTypeRegistry get(ListableBeanFactory beanFactory)
/*     */   {
/*  91 */     Assert.isInstanceOf(DefaultListableBeanFactory.class, beanFactory);
/*  92 */     DefaultListableBeanFactory listableBeanFactory = (DefaultListableBeanFactory)beanFactory;
/*  93 */     Assert.isTrue(listableBeanFactory.isAllowEagerClassLoading(), "Bean factory must allow eager class loading");
/*     */     
/*  95 */     if (!listableBeanFactory.containsLocalBean(BEAN_NAME)) {
/*  96 */       BeanDefinition bd = new RootBeanDefinition(BeanTypeRegistry.class);
/*  97 */       bd.getConstructorArgumentValues().addIndexedArgumentValue(0, beanFactory);
/*  98 */       listableBeanFactory.registerBeanDefinition(BEAN_NAME, bd);
/*     */     }
/*     */     
/* 101 */     return (BeanTypeRegistry)listableBeanFactory.getBean(BEAN_NAME, BeanTypeRegistry.class);
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
/*     */   Set<String> getNamesForType(Class<?> type)
/*     */   {
/* 114 */     updateTypesIfNecessary();
/* 115 */     Set<String> matches = new LinkedHashSet();
/* 116 */     for (Map.Entry<String, Class<?>> entry : this.beanTypes.entrySet()) {
/* 117 */       if ((entry.getValue() != null) && (type.isAssignableFrom((Class)entry.getValue()))) {
/* 118 */         matches.add(entry.getKey());
/*     */       }
/*     */     }
/* 121 */     return matches;
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
/*     */   Set<String> getNamesForAnnotation(Class<? extends Annotation> annotation)
/*     */   {
/* 134 */     updateTypesIfNecessary();
/* 135 */     Set<String> matches = new LinkedHashSet();
/* 136 */     for (Map.Entry<String, Class<?>> entry : this.beanTypes.entrySet()) {
/* 137 */       if ((entry.getValue() != null) && 
/* 138 */         (AnnotationUtils.findAnnotation((Class)entry.getValue(), annotation) != null)) {
/* 139 */         matches.add(entry.getKey());
/*     */       }
/*     */     }
/* 142 */     return matches;
/*     */   }
/*     */   
/*     */ 
/*     */   public void afterSingletonsInstantiated()
/*     */   {
/* 148 */     this.beanTypes.clear();
/* 149 */     this.lastBeanDefinitionCount = 0;
/*     */   }
/*     */   
/*     */   private void addBeanType(String name) {
/* 153 */     if (this.beanFactory.containsSingleton(name)) {
/* 154 */       this.beanTypes.put(name, this.beanFactory.getType(name));
/*     */     }
/* 156 */     else if (!this.beanFactory.isAlias(name)) {
/* 157 */       addBeanTypeForNonAliasDefinition(name);
/*     */     }
/*     */   }
/*     */   
/*     */   private void addBeanTypeForNonAliasDefinition(String name) {
/*     */     try {
/* 163 */       String factoryName = "&" + name;
/*     */       
/* 165 */       RootBeanDefinition beanDefinition = (RootBeanDefinition)this.beanFactory.getMergedBeanDefinition(name);
/* 166 */       if ((!beanDefinition.isAbstract()) && 
/* 167 */         (!requiresEagerInit(beanDefinition.getFactoryBeanName()))) {
/* 168 */         if (this.beanFactory.isFactoryBean(factoryName)) {
/* 169 */           Class<?> factoryBeanGeneric = getFactoryBeanGeneric(this.beanFactory, beanDefinition, name);
/*     */           
/* 171 */           this.beanTypes.put(name, factoryBeanGeneric);
/* 172 */           this.beanTypes.put(factoryName, this.beanFactory
/* 173 */             .getType(factoryName));
/*     */         }
/*     */         else {
/* 176 */           this.beanTypes.put(name, this.beanFactory.getType(name));
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (CannotLoadBeanClassException ex)
/*     */     {
/* 182 */       logIgnoredError("bean class loading failure for bean", name, ex);
/*     */     }
/*     */     catch (BeanDefinitionStoreException ex)
/*     */     {
/* 186 */       logIgnoredError("unresolvable metadata in bean definition", name, ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private void logIgnoredError(String message, String name, Exception ex) {
/* 191 */     if (logger.isDebugEnabled()) {
/* 192 */       logger.debug("Ignoring " + message + " '" + name + "'", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean requiresEagerInit(String factoryBeanName) {
/* 197 */     return (factoryBeanName != null) && (this.beanFactory.isFactoryBean(factoryBeanName)) && 
/* 198 */       (!this.beanFactory.containsSingleton(factoryBeanName));
/*     */   }
/*     */   
/*     */   private void updateTypesIfNecessary() {
/* 202 */     if (this.lastBeanDefinitionCount != this.beanFactory.getBeanDefinitionCount()) {
/* 203 */       Iterator<String> names = this.beanFactory.getBeanNamesIterator();
/* 204 */       while (names.hasNext()) {
/* 205 */         String name = (String)names.next();
/* 206 */         if (!this.beanTypes.containsKey(name)) {
/* 207 */           addBeanType(name);
/*     */         }
/*     */       }
/* 210 */       this.lastBeanDefinitionCount = this.beanFactory.getBeanDefinitionCount();
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
/*     */   private Class<?> getFactoryBeanGeneric(ConfigurableListableBeanFactory beanFactory, BeanDefinition definition, String name)
/*     */   {
/*     */     try
/*     */     {
/* 225 */       return doGetFactoryBeanGeneric(beanFactory, definition, name);
/*     */     }
/*     */     catch (Exception ex) {}
/* 228 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   private Class<?> doGetFactoryBeanGeneric(ConfigurableListableBeanFactory beanFactory, BeanDefinition definition, String name)
/*     */     throws Exception, ClassNotFoundException, LinkageError
/*     */   {
/* 235 */     if ((StringUtils.hasLength(definition.getFactoryBeanName())) && 
/* 236 */       (StringUtils.hasLength(definition.getFactoryMethodName()))) {
/* 237 */       return getConfigurationClassFactoryBeanGeneric(beanFactory, definition, name);
/*     */     }
/* 239 */     if (StringUtils.hasLength(definition.getBeanClassName())) {
/* 240 */       return getDirectFactoryBeanGeneric(beanFactory, definition, name);
/*     */     }
/* 242 */     return null;
/*     */   }
/*     */   
/*     */   private Class<?> getConfigurationClassFactoryBeanGeneric(ConfigurableListableBeanFactory beanFactory, BeanDefinition definition, String name)
/*     */     throws Exception
/*     */   {
/* 248 */     Method method = getFactoryMethod(beanFactory, definition);
/*     */     
/* 250 */     Class<?> generic = ResolvableType.forMethodReturnType(method).as(FactoryBean.class).resolveGeneric(new int[0]);
/* 251 */     if (((generic == null) || (generic.equals(Object.class))) && 
/* 252 */       (definition.hasAttribute("factoryBeanObjectType"))) {
/* 253 */       generic = getTypeFromAttribute(definition
/* 254 */         .getAttribute("factoryBeanObjectType"));
/*     */     }
/* 256 */     return generic;
/*     */   }
/*     */   
/*     */   private Method getFactoryMethod(ConfigurableListableBeanFactory beanFactory, BeanDefinition definition) throws Exception
/*     */   {
/* 261 */     if ((definition instanceof AnnotatedBeanDefinition))
/*     */     {
/* 263 */       MethodMetadata factoryMethodMetadata = ((AnnotatedBeanDefinition)definition).getFactoryMethodMetadata();
/* 264 */       if ((factoryMethodMetadata instanceof StandardMethodMetadata)) {
/* 265 */         return 
/* 266 */           ((StandardMethodMetadata)factoryMethodMetadata).getIntrospectedMethod();
/*     */       }
/*     */     }
/*     */     
/* 270 */     BeanDefinition factoryDefinition = beanFactory.getBeanDefinition(definition.getFactoryBeanName());
/* 271 */     Class<?> factoryClass = ClassUtils.forName(factoryDefinition.getBeanClassName(), beanFactory
/* 272 */       .getBeanClassLoader());
/* 273 */     return getFactoryMethod(definition, factoryClass);
/*     */   }
/*     */   
/*     */   private Method getFactoryMethod(BeanDefinition definition, Class<?> factoryClass) {
/* 277 */     Method uniqueMethod = null;
/* 278 */     for (Method candidate : getCandidateFactoryMethods(definition, factoryClass)) {
/* 279 */       if (candidate.getName().equals(definition.getFactoryMethodName())) {
/* 280 */         if (uniqueMethod == null) {
/* 281 */           uniqueMethod = candidate;
/*     */         }
/* 283 */         else if (!hasMatchingParameterTypes(candidate, uniqueMethod)) {
/* 284 */           return null;
/*     */         }
/*     */       }
/*     */     }
/* 288 */     return uniqueMethod;
/*     */   }
/*     */   
/*     */   private Method[] getCandidateFactoryMethods(BeanDefinition definition, Class<?> factoryClass)
/*     */   {
/* 293 */     return shouldConsiderNonPublicMethods(definition) ? 
/* 294 */       ReflectionUtils.getAllDeclaredMethods(factoryClass) : factoryClass
/* 295 */       .getMethods();
/*     */   }
/*     */   
/*     */   private boolean shouldConsiderNonPublicMethods(BeanDefinition definition) {
/* 299 */     return ((definition instanceof AbstractBeanDefinition)) && 
/* 300 */       (((AbstractBeanDefinition)definition).isNonPublicAccessAllowed());
/*     */   }
/*     */   
/*     */   private boolean hasMatchingParameterTypes(Method candidate, Method current) {
/* 304 */     return Arrays.equals(candidate.getParameterTypes(), current.getParameterTypes());
/*     */   }
/*     */   
/*     */   private Class<?> getDirectFactoryBeanGeneric(ConfigurableListableBeanFactory beanFactory, BeanDefinition definition, String name)
/*     */     throws ClassNotFoundException, LinkageError
/*     */   {
/* 310 */     Class<?> factoryBeanClass = ClassUtils.forName(definition.getBeanClassName(), beanFactory
/* 311 */       .getBeanClassLoader());
/*     */     
/* 313 */     Class<?> generic = ResolvableType.forClass(factoryBeanClass).as(FactoryBean.class).resolveGeneric(new int[0]);
/* 314 */     if (((generic == null) || (generic.equals(Object.class))) && 
/* 315 */       (definition.hasAttribute("factoryBeanObjectType"))) {
/* 316 */       generic = getTypeFromAttribute(definition
/* 317 */         .getAttribute("factoryBeanObjectType"));
/*     */     }
/* 319 */     return generic;
/*     */   }
/*     */   
/*     */   private Class<?> getTypeFromAttribute(Object attribute) throws ClassNotFoundException, LinkageError
/*     */   {
/* 324 */     if ((attribute instanceof Class)) {
/* 325 */       return (Class)attribute;
/*     */     }
/* 327 */     if ((attribute instanceof String)) {
/* 328 */       return ClassUtils.forName((String)attribute, null);
/*     */     }
/* 330 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\condition\BeanTypeRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */