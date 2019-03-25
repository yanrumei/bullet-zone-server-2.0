/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.aspectj.lang.reflect.AjType;
/*     */ import org.aspectj.lang.reflect.PerClause;
/*     */ import org.aspectj.lang.reflect.PerClauseKind;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.aspectj.AspectJProxyUtils;
/*     */ import org.springframework.aop.framework.AopConfigException;
/*     */ import org.springframework.aop.framework.AopProxy;
/*     */ import org.springframework.aop.framework.ProxyCreatorSupport;
/*     */ import org.springframework.aop.support.AopUtils;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AspectJProxyFactory
/*     */   extends ProxyCreatorSupport
/*     */ {
/*  53 */   private static final Map<Class<?>, Object> aspectCache = new ConcurrentHashMap();
/*     */   
/*  55 */   private final AspectJAdvisorFactory aspectFactory = new ReflectiveAspectJAdvisorFactory();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AspectJProxyFactory() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AspectJProxyFactory(Object target)
/*     */   {
/*  70 */     Assert.notNull(target, "Target object must not be null");
/*  71 */     setInterfaces(ClassUtils.getAllInterfaces(target));
/*  72 */     setTarget(target);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public AspectJProxyFactory(Class<?>... interfaces)
/*     */   {
/*  80 */     setInterfaces(interfaces);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addAspect(Object aspectInstance)
/*     */   {
/*  92 */     Class<?> aspectClass = aspectInstance.getClass();
/*  93 */     String aspectName = aspectClass.getName();
/*  94 */     AspectMetadata am = createAspectMetadata(aspectClass, aspectName);
/*  95 */     if (am.getAjType().getPerClause().getKind() != PerClauseKind.SINGLETON)
/*     */     {
/*  97 */       throw new IllegalArgumentException("Aspect class [" + aspectClass.getName() + "] does not define a singleton aspect");
/*     */     }
/*  99 */     addAdvisorsFromAspectInstanceFactory(new SingletonMetadataAwareAspectInstanceFactory(aspectInstance, aspectName));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addAspect(Class<?> aspectClass)
/*     */   {
/* 108 */     String aspectName = aspectClass.getName();
/* 109 */     AspectMetadata am = createAspectMetadata(aspectClass, aspectName);
/* 110 */     MetadataAwareAspectInstanceFactory instanceFactory = createAspectInstanceFactory(am, aspectClass, aspectName);
/* 111 */     addAdvisorsFromAspectInstanceFactory(instanceFactory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addAdvisorsFromAspectInstanceFactory(MetadataAwareAspectInstanceFactory instanceFactory)
/*     */   {
/* 121 */     List<Advisor> advisors = this.aspectFactory.getAdvisors(instanceFactory);
/* 122 */     advisors = AopUtils.findAdvisorsThatCanApply(advisors, getTargetClass());
/* 123 */     AspectJProxyUtils.makeAdvisorChainAspectJCapableIfNecessary(advisors);
/* 124 */     AnnotationAwareOrderComparator.sort(advisors);
/* 125 */     addAdvisors(advisors);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private AspectMetadata createAspectMetadata(Class<?> aspectClass, String aspectName)
/*     */   {
/* 132 */     AspectMetadata am = new AspectMetadata(aspectClass, aspectName);
/* 133 */     if (!am.getAjType().isAspect()) {
/* 134 */       throw new IllegalArgumentException("Class [" + aspectClass.getName() + "] is not a valid aspect type");
/*     */     }
/* 136 */     return am;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private MetadataAwareAspectInstanceFactory createAspectInstanceFactory(AspectMetadata am, Class<?> aspectClass, String aspectName)
/*     */   {
/*     */     MetadataAwareAspectInstanceFactory instanceFactory;
/*     */     
/*     */ 
/*     */     MetadataAwareAspectInstanceFactory instanceFactory;
/*     */     
/* 148 */     if (am.getAjType().getPerClause().getKind() == PerClauseKind.SINGLETON)
/*     */     {
/* 150 */       Object instance = getSingletonAspectInstance(aspectClass);
/* 151 */       instanceFactory = new SingletonMetadataAwareAspectInstanceFactory(instance, aspectName);
/*     */     }
/*     */     else
/*     */     {
/* 155 */       instanceFactory = new SimpleMetadataAwareAspectInstanceFactory(aspectClass, aspectName);
/*     */     }
/* 157 */     return instanceFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object getSingletonAspectInstance(Class<?> aspectClass)
/*     */   {
/* 166 */     Object instance = aspectCache.get(aspectClass);
/* 167 */     if (instance == null) {
/* 168 */       synchronized (aspectCache)
/*     */       {
/* 170 */         instance = aspectCache.get(aspectClass);
/* 171 */         if (instance != null) {
/* 172 */           return instance;
/*     */         }
/*     */         try {
/* 175 */           instance = aspectClass.newInstance();
/* 176 */           aspectCache.put(aspectClass, instance);
/* 177 */           return instance;
/*     */         }
/*     */         catch (InstantiationException ex) {
/* 180 */           throw new AopConfigException("Unable to instantiate aspect class [" + aspectClass.getName() + "]", ex);
/*     */         }
/*     */         catch (IllegalAccessException ex) {
/* 183 */           throw new AopConfigException("Cannot access aspect class [" + aspectClass.getName() + "]", ex);
/*     */         }
/*     */       }
/*     */     }
/* 187 */     return instance;
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
/*     */   public <T> T getProxy()
/*     */   {
/* 201 */     return (T)createAopProxy().getProxy();
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
/*     */   public <T> T getProxy(ClassLoader classLoader)
/*     */   {
/* 214 */     return (T)createAopProxy().getProxy(classLoader);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\aspectj\annotation\AspectJProxyFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */