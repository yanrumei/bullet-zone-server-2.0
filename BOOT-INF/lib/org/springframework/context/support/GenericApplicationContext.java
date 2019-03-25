/*     */ package org.springframework.context.support;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanDefinitionStoreException;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.io.support.ResourcePatternResolver;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GenericApplicationContext
/*     */   extends AbstractApplicationContext
/*     */   implements BeanDefinitionRegistry
/*     */ {
/*     */   private final DefaultListableBeanFactory beanFactory;
/*     */   private ResourceLoader resourceLoader;
/*  94 */   private boolean customClassLoader = false;
/*     */   
/*  96 */   private final AtomicBoolean refreshed = new AtomicBoolean();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GenericApplicationContext()
/*     */   {
/* 105 */     this.beanFactory = new DefaultListableBeanFactory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GenericApplicationContext(DefaultListableBeanFactory beanFactory)
/*     */   {
/* 115 */     Assert.notNull(beanFactory, "BeanFactory must not be null");
/* 116 */     this.beanFactory = beanFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GenericApplicationContext(ApplicationContext parent)
/*     */   {
/* 126 */     this();
/* 127 */     setParent(parent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public GenericApplicationContext(DefaultListableBeanFactory beanFactory, ApplicationContext parent)
/*     */   {
/* 138 */     this(beanFactory);
/* 139 */     setParent(parent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParent(ApplicationContext parent)
/*     */   {
/* 150 */     super.setParent(parent);
/* 151 */     this.beanFactory.setParentBeanFactory(getInternalParentBeanFactory());
/*     */   }
/*     */   
/*     */   public void setId(String id)
/*     */   {
/* 156 */     super.setId(id);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllowBeanDefinitionOverriding(boolean allowBeanDefinitionOverriding)
/*     */   {
/* 167 */     this.beanFactory.setAllowBeanDefinitionOverriding(allowBeanDefinitionOverriding);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAllowCircularReferences(boolean allowCircularReferences)
/*     */   {
/* 179 */     this.beanFactory.setAllowCircularReferences(allowCircularReferences);
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
/*     */   public void setResourceLoader(ResourceLoader resourceLoader)
/*     */   {
/* 201 */     this.resourceLoader = resourceLoader;
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
/*     */   public Resource getResource(String location)
/*     */   {
/* 216 */     if (this.resourceLoader != null) {
/* 217 */       return this.resourceLoader.getResource(location);
/*     */     }
/* 219 */     return super.getResource(location);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Resource[] getResources(String locationPattern)
/*     */     throws IOException
/*     */   {
/* 230 */     if ((this.resourceLoader instanceof ResourcePatternResolver)) {
/* 231 */       return ((ResourcePatternResolver)this.resourceLoader).getResources(locationPattern);
/*     */     }
/* 233 */     return super.getResources(locationPattern);
/*     */   }
/*     */   
/*     */   public void setClassLoader(ClassLoader classLoader)
/*     */   {
/* 238 */     super.setClassLoader(classLoader);
/* 239 */     this.customClassLoader = true;
/*     */   }
/*     */   
/*     */   public ClassLoader getClassLoader()
/*     */   {
/* 244 */     if ((this.resourceLoader != null) && (!this.customClassLoader)) {
/* 245 */       return this.resourceLoader.getClassLoader();
/*     */     }
/* 247 */     return super.getClassLoader();
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
/*     */   protected final void refreshBeanFactory()
/*     */     throws IllegalStateException
/*     */   {
/* 262 */     if (!this.refreshed.compareAndSet(false, true)) {
/* 263 */       throw new IllegalStateException("GenericApplicationContext does not support multiple refresh attempts: just call 'refresh' once");
/*     */     }
/*     */     
/* 266 */     this.beanFactory.setSerializationId(getId());
/*     */   }
/*     */   
/*     */   protected void cancelRefresh(BeansException ex)
/*     */   {
/* 271 */     this.beanFactory.setSerializationId(null);
/* 272 */     super.cancelRefresh(ex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final void closeBeanFactory()
/*     */   {
/* 281 */     this.beanFactory.setSerializationId(null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ConfigurableListableBeanFactory getBeanFactory()
/*     */   {
/* 290 */     return this.beanFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final DefaultListableBeanFactory getDefaultListableBeanFactory()
/*     */   {
/* 302 */     return this.beanFactory;
/*     */   }
/*     */   
/*     */   public AutowireCapableBeanFactory getAutowireCapableBeanFactory() throws IllegalStateException
/*     */   {
/* 307 */     assertBeanFactoryActive();
/* 308 */     return this.beanFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
/*     */     throws BeanDefinitionStoreException
/*     */   {
/* 320 */     this.beanFactory.registerBeanDefinition(beanName, beanDefinition);
/*     */   }
/*     */   
/*     */   public void removeBeanDefinition(String beanName) throws NoSuchBeanDefinitionException
/*     */   {
/* 325 */     this.beanFactory.removeBeanDefinition(beanName);
/*     */   }
/*     */   
/*     */   public BeanDefinition getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException
/*     */   {
/* 330 */     return this.beanFactory.getBeanDefinition(beanName);
/*     */   }
/*     */   
/*     */   public boolean isBeanNameInUse(String beanName)
/*     */   {
/* 335 */     return this.beanFactory.isBeanNameInUse(beanName);
/*     */   }
/*     */   
/*     */   public void registerAlias(String beanName, String alias)
/*     */   {
/* 340 */     this.beanFactory.registerAlias(beanName, alias);
/*     */   }
/*     */   
/*     */   public void removeAlias(String alias)
/*     */   {
/* 345 */     this.beanFactory.removeAlias(alias);
/*     */   }
/*     */   
/*     */   public boolean isAlias(String beanName)
/*     */   {
/* 350 */     return this.beanFactory.isAlias(beanName);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\support\GenericApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */