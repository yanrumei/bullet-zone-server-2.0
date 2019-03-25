/*     */ package org.springframework.boot.context.config;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.context.ApplicationContextInitializer;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.env.ConfigurableEnvironment;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ public class DelegatingApplicationContextInitializer
/*     */   implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered
/*     */ {
/*     */   private static final String PROPERTY_NAME = "context.initializer.classes";
/*  49 */   private int order = 0;
/*     */   
/*     */   public void initialize(ConfigurableApplicationContext context)
/*     */   {
/*  53 */     ConfigurableEnvironment environment = context.getEnvironment();
/*  54 */     List<Class<?>> initializerClasses = getInitializerClasses(environment);
/*  55 */     if (!initializerClasses.isEmpty()) {
/*  56 */       applyInitializerClasses(context, initializerClasses);
/*     */     }
/*     */   }
/*     */   
/*     */   private List<Class<?>> getInitializerClasses(ConfigurableEnvironment env) {
/*  61 */     String classNames = env.getProperty("context.initializer.classes");
/*  62 */     List<Class<?>> classes = new ArrayList();
/*  63 */     if (StringUtils.hasLength(classNames)) {
/*  64 */       for (String className : StringUtils.tokenizeToStringArray(classNames, ",")) {
/*  65 */         classes.add(getInitializerClass(className));
/*     */       }
/*     */     }
/*  68 */     return classes;
/*     */   }
/*     */   
/*     */   private Class<?> getInitializerClass(String className) throws LinkageError {
/*     */     try {
/*  73 */       Class<?> initializerClass = ClassUtils.forName(className, 
/*  74 */         ClassUtils.getDefaultClassLoader());
/*  75 */       Assert.isAssignable(ApplicationContextInitializer.class, initializerClass);
/*  76 */       return initializerClass;
/*     */     }
/*     */     catch (ClassNotFoundException ex) {
/*  79 */       throw new ApplicationContextException("Failed to load context initializer class [" + className + "]", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private void applyInitializerClasses(ConfigurableApplicationContext context, List<Class<?>> initializerClasses)
/*     */   {
/*  86 */     Class<?> contextClass = context.getClass();
/*  87 */     List<ApplicationContextInitializer<?>> initializers = new ArrayList();
/*  88 */     for (Class<?> initializerClass : initializerClasses) {
/*  89 */       initializers.add(instantiateInitializer(contextClass, initializerClass));
/*     */     }
/*  91 */     applyInitializers(context, initializers);
/*     */   }
/*     */   
/*     */   private ApplicationContextInitializer<?> instantiateInitializer(Class<?> contextClass, Class<?> initializerClass)
/*     */   {
/*  96 */     Class<?> requireContextClass = GenericTypeResolver.resolveTypeArgument(initializerClass, ApplicationContextInitializer.class);
/*     */     
/*  98 */     Assert.isAssignable(requireContextClass, contextClass, 
/*  99 */       String.format("Could not add context initializer [%s] as its generic parameter [%s] is not assignable from the type of application context used by this context loader [%s]: ", new Object[] {initializerClass
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 104 */       .getName(), requireContextClass.getName(), contextClass
/* 105 */       .getName() }));
/* 106 */     return 
/* 107 */       (ApplicationContextInitializer)BeanUtils.instantiateClass(initializerClass);
/*     */   }
/*     */   
/*     */ 
/*     */   private void applyInitializers(ConfigurableApplicationContext context, List<ApplicationContextInitializer<?>> initializers)
/*     */   {
/* 113 */     Collections.sort(initializers, new AnnotationAwareOrderComparator());
/* 114 */     for (ApplicationContextInitializer initializer : initializers) {
/* 115 */       initializer.initialize(context);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 120 */     this.order = order;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 125 */     return this.order;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\config\DelegatingApplicationContextInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */