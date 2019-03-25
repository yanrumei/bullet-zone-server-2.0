/*     */ package org.springframework.boot.context.embedded;
/*     */ 
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.beans.factory.support.BeanNameGenerator;
/*     */ import org.springframework.context.annotation.AnnotatedBeanDefinitionReader;
/*     */ import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
/*     */ import org.springframework.context.annotation.ScopeMetadataResolver;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationConfigEmbeddedWebApplicationContext
/*     */   extends EmbeddedWebApplicationContext
/*     */ {
/*     */   private final AnnotatedBeanDefinitionReader reader;
/*     */   private final ClassPathBeanDefinitionScanner scanner;
/*     */   private Class<?>[] annotatedClasses;
/*     */   private String[] basePackages;
/*     */   
/*     */   public AnnotationConfigEmbeddedWebApplicationContext()
/*     */   {
/*  66 */     this.reader = new AnnotatedBeanDefinitionReader(this);
/*  67 */     this.scanner = new ClassPathBeanDefinitionScanner(this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationConfigEmbeddedWebApplicationContext(Class<?>... annotatedClasses)
/*     */   {
/*  78 */     this();
/*  79 */     register(annotatedClasses);
/*  80 */     refresh();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationConfigEmbeddedWebApplicationContext(String... basePackages)
/*     */   {
/*  89 */     this();
/*  90 */     scan(basePackages);
/*  91 */     refresh();
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
/* 102 */     super.setEnvironment(environment);
/* 103 */     this.reader.setEnvironment(environment);
/* 104 */     this.scanner.setEnvironment(environment);
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
/*     */   public void setBeanNameGenerator(BeanNameGenerator beanNameGenerator)
/*     */   {
/* 122 */     this.reader.setBeanNameGenerator(beanNameGenerator);
/* 123 */     this.scanner.setBeanNameGenerator(beanNameGenerator);
/* 124 */     getBeanFactory().registerSingleton("org.springframework.context.annotation.internalConfigurationBeanNameGenerator", beanNameGenerator);
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
/*     */   public void setScopeMetadataResolver(ScopeMetadataResolver scopeMetadataResolver)
/*     */   {
/* 139 */     this.reader.setScopeMetadataResolver(scopeMetadataResolver);
/* 140 */     this.scanner.setScopeMetadataResolver(scopeMetadataResolver);
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
/*     */   public final void register(Class<?>... annotatedClasses)
/*     */   {
/* 156 */     this.annotatedClasses = annotatedClasses;
/* 157 */     Assert.notEmpty(annotatedClasses, "At least one annotated class must be specified");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void scan(String... basePackages)
/*     */   {
/* 169 */     this.basePackages = basePackages;
/* 170 */     Assert.notEmpty(basePackages, "At least one base package must be specified");
/*     */   }
/*     */   
/*     */   protected void prepareRefresh()
/*     */   {
/* 175 */     this.scanner.clearCache();
/* 176 */     super.prepareRefresh();
/*     */   }
/*     */   
/*     */   protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*     */   {
/* 181 */     super.postProcessBeanFactory(beanFactory);
/* 182 */     if ((this.basePackages != null) && (this.basePackages.length > 0)) {
/* 183 */       this.scanner.scan(this.basePackages);
/*     */     }
/* 185 */     if ((this.annotatedClasses != null) && (this.annotatedClasses.length > 0)) {
/* 186 */       this.reader.register(this.annotatedClasses);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\embedded\AnnotationConfigEmbeddedWebApplicationContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */