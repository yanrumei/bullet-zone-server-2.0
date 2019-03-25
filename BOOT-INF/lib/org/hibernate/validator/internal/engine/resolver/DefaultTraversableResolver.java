/*     */ package org.hibernate.validator.internal.engine.resolver;
/*     */ 
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import javax.validation.Path;
/*     */ import javax.validation.Path.Node;
/*     */ import javax.validation.TraversableResolver;
/*     */ import javax.validation.ValidationException;
/*     */ import org.hibernate.validator.internal.util.ReflectionHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetMethod;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.LoadClass;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.NewInstance;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultTraversableResolver
/*     */   implements TraversableResolver
/*     */ {
/*  33 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final String PERSISTENCE_CLASS_NAME = "javax.persistence.Persistence";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String PERSISTENCE_UTIL_METHOD = "getPersistenceUtil";
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String JPA_AWARE_TRAVERSABLE_RESOLVER_CLASS_NAME = "org.hibernate.validator.internal.engine.resolver.JPATraversableResolver";
/*     */   
/*     */ 
/*     */ 
/*     */   private TraversableResolver jpaTraversableResolver;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultTraversableResolver()
/*     */   {
/*  57 */     detectJPA();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void detectJPA()
/*     */   {
/*     */     try
/*     */     {
/*  67 */       persistenceClass = (Class)run(LoadClass.action("javax.persistence.Persistence", getClass().getClassLoader()));
/*     */     } catch (ValidationException e) {
/*     */       Class<?> persistenceClass;
/*  70 */       log.debugf("Cannot find %s on classpath. Assuming non JPA 2 environment. All properties will per default be traversable.", "javax.persistence.Persistence"); return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     Class<?> persistenceClass;
/*     */     
/*     */ 
/*  78 */     Method persistenceUtilGetter = (Method)run(GetMethod.action(persistenceClass, "getPersistenceUtil"));
/*  79 */     if (persistenceUtilGetter == null) {
/*  80 */       log.debugf("Found %s on classpath, but no method '%s'. Assuming JPA 1 environment. All properties will per default be traversable.", "javax.persistence.Persistence", "getPersistenceUtil");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  85 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */     try
/*     */     {
/*  91 */       Object persistence = run(NewInstance.action(persistenceClass, "persistence provider"));
/*  92 */       ReflectionHelper.getValue(persistenceUtilGetter, persistence);
/*     */     }
/*     */     catch (Exception e) {
/*  95 */       log.debugf("Unable to invoke %s.%s. Inconsistent JPA environment. All properties will per default be traversable.", "javax.persistence.Persistence", "getPersistenceUtil");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 100 */       return;
/*     */     }
/*     */     
/* 103 */     log.debugf("Found %s on classpath containing '%s'. Assuming JPA 2 environment. Trying to instantiate JPA aware TraversableResolver", "javax.persistence.Persistence", "getPersistenceUtil");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 112 */       Class<? extends TraversableResolver> jpaAwareResolverClass = (Class)run(LoadClass.action("org.hibernate.validator.internal.engine.resolver.JPATraversableResolver", getClass().getClassLoader()));
/* 113 */       this.jpaTraversableResolver = ((TraversableResolver)run(NewInstance.action(jpaAwareResolverClass, "")));
/* 114 */       log.debugf("Instantiated JPA aware TraversableResolver of type %s.", "org.hibernate.validator.internal.engine.resolver.JPATraversableResolver");
/*     */ 
/*     */     }
/*     */     catch (ValidationException e)
/*     */     {
/* 119 */       log.debugf("Unable to load or instantiate JPA aware resolver %s. All properties will per default be traversable.", "org.hibernate.validator.internal.engine.resolver.JPATraversableResolver");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType)
/*     */   {
/* 128 */     return (this.jpaTraversableResolver == null) || (this.jpaTraversableResolver.isReachable(traversableObject, traversableProperty, rootBeanType, pathToTraversableObject, elementType));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType)
/*     */   {
/* 135 */     return (this.jpaTraversableResolver == null) || (this.jpaTraversableResolver.isCascadable(traversableObject, traversableProperty, rootBeanType, pathToTraversableObject, elementType));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 147 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\resolver\DefaultTraversableResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */