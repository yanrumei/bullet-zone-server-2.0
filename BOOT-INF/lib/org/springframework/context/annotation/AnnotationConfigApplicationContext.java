/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.beans.factory.support.DefaultListableBeanFactory;
/*     */ import org.springframework.context.support.GenericApplicationContext;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
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
/*     */ public class AnnotationConfigApplicationContext
/*     */   extends GenericApplicationContext
/*     */   implements AnnotationConfigRegistry
/*     */ {
/*     */   private final AnnotatedBeanDefinitionReader reader;
/*     */   private final ClassPathBeanDefinitionScanner scanner;
/*     */   
/*     */   public AnnotationConfigApplicationContext()
/*     */   {
/*  61 */     this.reader = new AnnotatedBeanDefinitionReader(this);
/*  62 */     this.scanner = new ClassPathBeanDefinitionScanner(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationConfigApplicationContext(DefaultListableBeanFactory beanFactory)
/*     */   {
/*  70 */     super(beanFactory);
/*  71 */     this.reader = new AnnotatedBeanDefinitionReader(this);
/*  72 */     this.scanner = new ClassPathBeanDefinitionScanner(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationConfigApplicationContext(Class<?>... annotatedClasses)
/*     */   {
/*  82 */     this();
/*  83 */     register(annotatedClasses);
/*  84 */     refresh();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationConfigApplicationContext(String... basePackages)
/*     */   {
/*  93 */     this();
/*  94 */     scan(basePackages);
/*  95 */     refresh();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEnvironment(ConfigurableEnvironment environment)
/*     */   {
/* 106 */     super.setEnvironment(environment);
/* 107 */     this.reader.setEnvironment(environment);
/* 108 */     this.scanner.setEnvironment(environment);
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
/*     */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator)
/*     */   {
/* 121 */     this.reader.setBeanNameGenerator(beanNameGenerator);
/* 122 */     this.scanner.setBeanNameGenerator(beanNameGenerator);
/* 123 */     getBeanFactory().registerSingleton("org.springframework.context.annotation.internalConfigurationBeanNameGenerator", beanNameGenerator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver)
/*     */   {
/* 134 */     this.reader.setScopeMetadataResolver(scopeMetadataResolver);
/* 135 */     this.scanner.setScopeMetadataResolver(scopeMetadataResolver);
/*     */   }
/*     */   
/*     */   protected void prepareRefresh()
/*     */   {
/* 140 */     this.scanner.clearCache();
/* 141 */     super.prepareRefresh();
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
/*     */   public void register(Class<?>... annotatedClasses)
/*     */   {
/* 159 */     Assert.notEmpty(annotatedClasses, "At least one annotated class must be specified");
/* 160 */     this.reader.register(annotatedClasses);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void scan(String... basePackages)
/*     */   {
/* 172 */     Assert.notEmpty(basePackages, "At least one base package must be specified");
/* 173 */     this.scanner.scan(basePackages);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\AnnotationConfigApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */