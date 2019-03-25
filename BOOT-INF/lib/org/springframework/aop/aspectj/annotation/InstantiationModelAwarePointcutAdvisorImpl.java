/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aspectj.lang.reflect.AjType;
/*     */ import org.aspectj.lang.reflect.PerClause;
/*     */ import org.aspectj.lang.reflect.PerClauseKind;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.aspectj.AspectJExpressionPointcut;
/*     */ import org.springframework.aop.aspectj.AspectJPrecedenceInformation;
/*     */ import org.springframework.aop.aspectj.InstantiationModelAwarePointcutAdvisor;
/*     */ import org.springframework.aop.support.DynamicMethodMatcherPointcut;
/*     */ import org.springframework.aop.support.Pointcuts;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ class InstantiationModelAwarePointcutAdvisorImpl
/*     */   implements InstantiationModelAwarePointcutAdvisor, AspectJPrecedenceInformation, Serializable
/*     */ {
/*     */   private final AspectJExpressionPointcut declaredPointcut;
/*     */   private final Class<?> declaringClass;
/*     */   private final String methodName;
/*     */   private final Class<?>[] parameterTypes;
/*     */   private transient Method aspectJAdviceMethod;
/*     */   private final AspectJAdvisorFactory aspectJAdvisorFactory;
/*     */   private final MetadataAwareAspectInstanceFactory aspectInstanceFactory;
/*     */   private final int declarationOrder;
/*     */   private final String aspectName;
/*     */   private final Pointcut pointcut;
/*     */   private final boolean lazy;
/*     */   private Advice instantiatedAdvice;
/*     */   private Boolean isBeforeAdvice;
/*     */   private Boolean isAfterAdvice;
/*     */   
/*     */   public InstantiationModelAwarePointcutAdvisorImpl(AspectJExpressionPointcut declaredPointcut, Method aspectJAdviceMethod, AspectJAdvisorFactory aspectJAdvisorFactory, MetadataAwareAspectInstanceFactory aspectInstanceFactory, int declarationOrder, String aspectName)
/*     */   {
/*  80 */     this.declaredPointcut = declaredPointcut;
/*  81 */     this.declaringClass = aspectJAdviceMethod.getDeclaringClass();
/*  82 */     this.methodName = aspectJAdviceMethod.getName();
/*  83 */     this.parameterTypes = aspectJAdviceMethod.getParameterTypes();
/*  84 */     this.aspectJAdviceMethod = aspectJAdviceMethod;
/*  85 */     this.aspectJAdvisorFactory = aspectJAdvisorFactory;
/*  86 */     this.aspectInstanceFactory = aspectInstanceFactory;
/*  87 */     this.declarationOrder = declarationOrder;
/*  88 */     this.aspectName = aspectName;
/*     */     
/*  90 */     if (aspectInstanceFactory.getAspectMetadata().isLazilyInstantiated())
/*     */     {
/*  92 */       Pointcut preInstantiationPointcut = Pointcuts.union(aspectInstanceFactory
/*  93 */         .getAspectMetadata().getPerClausePointcut(), this.declaredPointcut);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  98 */       this.pointcut = new PerTargetInstantiationModelPointcut(this.declaredPointcut, preInstantiationPointcut, aspectInstanceFactory, null);
/*     */       
/* 100 */       this.lazy = true;
/*     */     }
/*     */     else
/*     */     {
/* 104 */       this.pointcut = this.declaredPointcut;
/* 105 */       this.lazy = false;
/* 106 */       this.instantiatedAdvice = instantiateAdvice(this.declaredPointcut);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Pointcut getPointcut()
/*     */   {
/* 117 */     return this.pointcut;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isPerInstance()
/*     */   {
/* 127 */     return getAspectMetadata().getAjType().getPerClause().getKind() != PerClauseKind.SINGLETON;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public AspectMetadata getAspectMetadata()
/*     */   {
/* 134 */     return this.aspectInstanceFactory.getAspectMetadata();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public synchronized Advice getAdvice()
/*     */   {
/* 142 */     if (this.instantiatedAdvice == null) {
/* 143 */       this.instantiatedAdvice = instantiateAdvice(this.declaredPointcut);
/*     */     }
/* 145 */     return this.instantiatedAdvice;
/*     */   }
/*     */   
/*     */   public boolean isLazy()
/*     */   {
/* 150 */     return this.lazy;
/*     */   }
/*     */   
/*     */   public synchronized boolean isAdviceInstantiated()
/*     */   {
/* 155 */     return this.instantiatedAdvice != null;
/*     */   }
/*     */   
/*     */   private Advice instantiateAdvice(AspectJExpressionPointcut pcut)
/*     */   {
/* 160 */     return this.aspectJAdvisorFactory.getAdvice(this.aspectJAdviceMethod, pcut, this.aspectInstanceFactory, this.declarationOrder, this.aspectName);
/*     */   }
/*     */   
/*     */   public MetadataAwareAspectInstanceFactory getAspectInstanceFactory()
/*     */   {
/* 165 */     return this.aspectInstanceFactory;
/*     */   }
/*     */   
/*     */   public AspectJExpressionPointcut getDeclaredPointcut() {
/* 169 */     return this.declaredPointcut;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 174 */     return this.aspectInstanceFactory.getOrder();
/*     */   }
/*     */   
/*     */   public String getAspectName()
/*     */   {
/* 179 */     return this.aspectName;
/*     */   }
/*     */   
/*     */   public int getDeclarationOrder()
/*     */   {
/* 184 */     return this.declarationOrder;
/*     */   }
/*     */   
/*     */   public boolean isBeforeAdvice()
/*     */   {
/* 189 */     if (this.isBeforeAdvice == null) {
/* 190 */       determineAdviceType();
/*     */     }
/* 192 */     return this.isBeforeAdvice.booleanValue();
/*     */   }
/*     */   
/*     */   public boolean isAfterAdvice()
/*     */   {
/* 197 */     if (this.isAfterAdvice == null) {
/* 198 */       determineAdviceType();
/*     */     }
/* 200 */     return this.isAfterAdvice.booleanValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void determineAdviceType()
/*     */   {
/* 209 */     AbstractAspectJAdvisorFactory.AspectJAnnotation<?> aspectJAnnotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(this.aspectJAdviceMethod);
/* 210 */     if (aspectJAnnotation == null) {
/* 211 */       this.isBeforeAdvice = Boolean.valueOf(false);
/* 212 */       this.isAfterAdvice = Boolean.valueOf(false);
/*     */     }
/*     */     else {
/* 215 */       switch (aspectJAnnotation.getAnnotationType()) {
/*     */       case AtAfter: 
/*     */       case AtAfterReturning: 
/*     */       case AtAfterThrowing: 
/* 219 */         this.isAfterAdvice = Boolean.valueOf(true);
/* 220 */         this.isBeforeAdvice = Boolean.valueOf(false);
/* 221 */         break;
/*     */       case AtAround: 
/*     */       case AtPointcut: 
/* 224 */         this.isAfterAdvice = Boolean.valueOf(false);
/* 225 */         this.isBeforeAdvice = Boolean.valueOf(false);
/* 226 */         break;
/*     */       case AtBefore: 
/* 228 */         this.isAfterAdvice = Boolean.valueOf(false);
/* 229 */         this.isBeforeAdvice = Boolean.valueOf(true);
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 237 */     return 
/*     */     
/* 239 */       "InstantiationModelAwarePointcutAdvisor: expression [" + getDeclaredPointcut().getExpression() + "]; advice method [" + this.aspectJAdviceMethod + "]; perClauseKind=" + this.aspectInstanceFactory.getAspectMetadata().getAjType().getPerClause().getKind();
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 244 */     inputStream.defaultReadObject();
/*     */     try {
/* 246 */       this.aspectJAdviceMethod = this.declaringClass.getMethod(this.methodName, this.parameterTypes);
/*     */     }
/*     */     catch (NoSuchMethodException ex) {
/* 249 */       throw new IllegalStateException("Failed to find advice method on deserialization", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private class PerTargetInstantiationModelPointcut
/*     */     extends DynamicMethodMatcherPointcut
/*     */   {
/*     */     private final AspectJExpressionPointcut declaredPointcut;
/*     */     
/*     */ 
/*     */     private final Pointcut preInstantiationPointcut;
/*     */     
/*     */ 
/*     */     private LazySingletonAspectInstanceFactoryDecorator aspectInstanceFactory;
/*     */     
/*     */ 
/*     */     private PerTargetInstantiationModelPointcut(AspectJExpressionPointcut declaredPointcut, Pointcut preInstantiationPointcut, MetadataAwareAspectInstanceFactory aspectInstanceFactory)
/*     */     {
/* 269 */       this.declaredPointcut = declaredPointcut;
/* 270 */       this.preInstantiationPointcut = preInstantiationPointcut;
/* 271 */       if ((aspectInstanceFactory instanceof LazySingletonAspectInstanceFactoryDecorator)) {
/* 272 */         this.aspectInstanceFactory = ((LazySingletonAspectInstanceFactoryDecorator)aspectInstanceFactory);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean matches(Method method, Class<?> targetClass)
/*     */     {
/* 279 */       return ((isAspectMaterialized()) && (this.declaredPointcut.matches(method, targetClass))) || 
/* 280 */         (this.preInstantiationPointcut.getMethodMatcher().matches(method, targetClass));
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean matches(Method method, Class<?> targetClass, Object... args)
/*     */     {
/* 286 */       return (isAspectMaterialized()) && (this.declaredPointcut.matches(method, targetClass));
/*     */     }
/*     */     
/*     */     private boolean isAspectMaterialized() {
/* 290 */       return (this.aspectInstanceFactory == null) || (this.aspectInstanceFactory.isMaterialized());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\aspectj\annotation\InstantiationModelAwarePointcutAdvisorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */