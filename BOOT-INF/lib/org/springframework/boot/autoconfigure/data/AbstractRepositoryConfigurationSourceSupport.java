/*     */ package org.springframework.boot.autoconfigure.data;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.support.BeanDefinitionRegistry;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.context.ResourceLoaderAware;
/*     */ import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.io.ResourceLoader;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.core.type.StandardAnnotationMetadata;
/*     */ import org.springframework.data.repository.config.AnnotationRepositoryConfigurationSource;
/*     */ import org.springframework.data.repository.config.RepositoryConfigurationDelegate;
/*     */ import org.springframework.data.repository.config.RepositoryConfigurationExtension;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractRepositoryConfigurationSourceSupport
/*     */   implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware
/*     */ {
/*     */   private ResourceLoader resourceLoader;
/*     */   private BeanFactory beanFactory;
/*     */   private Environment environment;
/*     */   
/*     */   public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry)
/*     */   {
/*  59 */     new RepositoryConfigurationDelegate(getConfigurationSource(registry), this.resourceLoader, this.environment).registerRepositoriesIn(registry, 
/*  60 */       getRepositoryConfigurationExtension());
/*     */   }
/*     */   
/*     */ 
/*     */   private AnnotationRepositoryConfigurationSource getConfigurationSource(BeanDefinitionRegistry registry)
/*     */   {
/*  66 */     StandardAnnotationMetadata metadata = new StandardAnnotationMetadata(getConfiguration(), true);
/*  67 */     new AnnotationRepositoryConfigurationSource(metadata, getAnnotation(), this.resourceLoader, this.environment, registry)
/*     */     {
/*     */       public Iterable<String> getBasePackages()
/*     */       {
/*  71 */         return 
/*  72 */           AbstractRepositoryConfigurationSourceSupport.this.getBasePackages();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   protected Iterable<String> getBasePackages() {
/*  78 */     return AutoConfigurationPackages.get(this.beanFactory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract Class<? extends Annotation> getAnnotation();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract Class<?> getConfiguration();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract RepositoryConfigurationExtension getRepositoryConfigurationExtension();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResourceLoader(ResourceLoader resourceLoader)
/*     */   {
/* 101 */     this.resourceLoader = resourceLoader;
/*     */   }
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory) throws BeansException
/*     */   {
/* 106 */     this.beanFactory = beanFactory;
/*     */   }
/*     */   
/*     */   public void setEnvironment(Environment environment)
/*     */   {
/* 111 */     this.environment = environment;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\AbstractRepositoryConfigurationSourceSupport.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */