/*     */ package org.springframework.aop.aspectj.annotation;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.aspectj.lang.annotation.After;
/*     */ import org.aspectj.lang.annotation.AfterReturning;
/*     */ import org.aspectj.lang.annotation.AfterThrowing;
/*     */ import org.aspectj.lang.annotation.Around;
/*     */ import org.aspectj.lang.annotation.Before;
/*     */ import org.aspectj.lang.annotation.DeclareParents;
/*     */ import org.aspectj.lang.annotation.Pointcut;
/*     */ import org.springframework.aop.Advisor;
/*     */ import org.springframework.aop.MethodBeforeAdvice;
/*     */ import org.springframework.aop.aspectj.AbstractAspectJAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAfterAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAfterReturningAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAfterThrowingAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJAroundAdvice;
/*     */ import org.springframework.aop.aspectj.AspectJExpressionPointcut;
/*     */ import org.springframework.aop.aspectj.AspectJMethodBeforeAdvice;
/*     */ import org.springframework.aop.aspectj.DeclareParentsAdvisor;
/*     */ import org.springframework.aop.framework.AopConfigException;
/*     */ import org.springframework.aop.support.DefaultPointcutAdvisor;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.convert.converter.Converter;
/*     */ import org.springframework.core.convert.converter.ConvertingComparator;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.ReflectionUtils.MethodCallback;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.util.comparator.CompoundComparator;
/*     */ import org.springframework.util.comparator.InstanceComparator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReflectiveAspectJAdvisorFactory
/*     */   extends AbstractAspectJAdvisorFactory
/*     */   implements Serializable
/*     */ {
/*     */   private static final Comparator<Method> METHOD_COMPARATOR;
/*     */   private final BeanFactory beanFactory;
/*     */   
/*     */   static
/*     */   {
/*  76 */     CompoundComparator<Method> comparator = new CompoundComparator();
/*  77 */     comparator.addComparator(new ConvertingComparator(new InstanceComparator(new Class[] { Around.class, Before.class, After.class, AfterReturning.class, AfterThrowing.class }), new Converter()
/*     */     {
/*     */ 
/*     */ 
/*     */       public Annotation convert(Method method)
/*     */       {
/*     */ 
/*  84 */         AbstractAspectJAdvisorFactory.AspectJAnnotation<?> annotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(method);
/*  85 */         return annotation != null ? annotation.getAnnotation() : null;
/*     */       }
/*  87 */     }));
/*  88 */     comparator.addComparator(new ConvertingComparator(new Converter()
/*     */     {
/*     */       public String convert(Method method)
/*     */       {
/*  92 */         return method.getName();
/*     */       }
/*  94 */     }));
/*  95 */     METHOD_COMPARATOR = comparator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ReflectiveAspectJAdvisorFactory()
/*     */   {
/* 106 */     this(null);
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
/*     */   public ReflectiveAspectJAdvisorFactory(BeanFactory beanFactory)
/*     */   {
/* 119 */     this.beanFactory = beanFactory;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<Advisor> getAdvisors(MetadataAwareAspectInstanceFactory aspectInstanceFactory)
/*     */   {
/* 125 */     Class<?> aspectClass = aspectInstanceFactory.getAspectMetadata().getAspectClass();
/* 126 */     String aspectName = aspectInstanceFactory.getAspectMetadata().getAspectName();
/* 127 */     validate(aspectClass);
/*     */     
/*     */ 
/*     */ 
/* 131 */     MetadataAwareAspectInstanceFactory lazySingletonAspectInstanceFactory = new LazySingletonAspectInstanceFactoryDecorator(aspectInstanceFactory);
/*     */     
/*     */ 
/* 134 */     List<Advisor> advisors = new LinkedList();
/* 135 */     for (Iterator localIterator = getAdvisorMethods(aspectClass).iterator(); localIterator.hasNext();) { method = (Method)localIterator.next();
/* 136 */       advisor = getAdvisor(method, lazySingletonAspectInstanceFactory, advisors.size(), aspectName);
/* 137 */       if (advisor != null) {
/* 138 */         advisors.add(advisor);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 143 */     if ((!advisors.isEmpty()) && (lazySingletonAspectInstanceFactory.getAspectMetadata().isLazilyInstantiated())) {
/* 144 */       instantiationAdvisor = new SyntheticInstantiationAdvisor(lazySingletonAspectInstanceFactory);
/* 145 */       advisors.add(0, instantiationAdvisor);
/*     */     }
/*     */     
/*     */ 
/* 149 */     Object instantiationAdvisor = aspectClass.getDeclaredFields();Method method = instantiationAdvisor.length; for (Advisor advisor = 0; advisor < method; advisor++) { Field field = instantiationAdvisor[advisor];
/* 150 */       Advisor advisor = getDeclareParentsAdvisor(field);
/* 151 */       if (advisor != null) {
/* 152 */         advisors.add(advisor);
/*     */       }
/*     */     }
/*     */     
/* 156 */     return advisors;
/*     */   }
/*     */   
/*     */   private List<Method> getAdvisorMethods(Class<?> aspectClass) {
/* 160 */     final List<Method> methods = new LinkedList();
/* 161 */     ReflectionUtils.doWithMethods(aspectClass, new ReflectionUtils.MethodCallback()
/*     */     {
/*     */       public void doWith(Method method) throws IllegalArgumentException
/*     */       {
/* 165 */         if (AnnotationUtils.getAnnotation(method, Pointcut.class) == null) {
/* 166 */           methods.add(method);
/*     */         }
/*     */       }
/* 169 */     });
/* 170 */     Collections.sort(methods, METHOD_COMPARATOR);
/* 171 */     return methods;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Advisor getDeclareParentsAdvisor(Field introductionField)
/*     */   {
/* 182 */     DeclareParents declareParents = (DeclareParents)introductionField.getAnnotation(DeclareParents.class);
/* 183 */     if (declareParents == null)
/*     */     {
/* 185 */       return null;
/*     */     }
/*     */     
/* 188 */     if (DeclareParents.class == declareParents.defaultImpl()) {
/* 189 */       throw new IllegalStateException("'defaultImpl' attribute must be set on DeclareParents");
/*     */     }
/*     */     
/* 192 */     return new DeclareParentsAdvisor(introductionField
/* 193 */       .getType(), declareParents.value(), declareParents.defaultImpl());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Advisor getAdvisor(Method candidateAdviceMethod, MetadataAwareAspectInstanceFactory aspectInstanceFactory, int declarationOrderInAspect, String aspectName)
/*     */   {
/* 201 */     validate(aspectInstanceFactory.getAspectMetadata().getAspectClass());
/*     */     
/* 203 */     AspectJExpressionPointcut expressionPointcut = getPointcut(candidateAdviceMethod, aspectInstanceFactory
/* 204 */       .getAspectMetadata().getAspectClass());
/* 205 */     if (expressionPointcut == null) {
/* 206 */       return null;
/*     */     }
/*     */     
/* 209 */     return new InstantiationModelAwarePointcutAdvisorImpl(expressionPointcut, candidateAdviceMethod, this, aspectInstanceFactory, declarationOrderInAspect, aspectName);
/*     */   }
/*     */   
/*     */ 
/*     */   private AspectJExpressionPointcut getPointcut(Method candidateAdviceMethod, Class<?> candidateAspectClass)
/*     */   {
/* 215 */     AbstractAspectJAdvisorFactory.AspectJAnnotation<?> aspectJAnnotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(candidateAdviceMethod);
/* 216 */     if (aspectJAnnotation == null) {
/* 217 */       return null;
/*     */     }
/*     */     
/* 220 */     AspectJExpressionPointcut ajexp = new AspectJExpressionPointcut(candidateAspectClass, new String[0], new Class[0]);
/*     */     
/* 222 */     ajexp.setExpression(aspectJAnnotation.getPointcutExpression());
/* 223 */     ajexp.setBeanFactory(this.beanFactory);
/* 224 */     return ajexp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Advice getAdvice(Method candidateAdviceMethod, AspectJExpressionPointcut expressionPointcut, MetadataAwareAspectInstanceFactory aspectInstanceFactory, int declarationOrder, String aspectName)
/*     */   {
/* 232 */     Class<?> candidateAspectClass = aspectInstanceFactory.getAspectMetadata().getAspectClass();
/* 233 */     validate(candidateAspectClass);
/*     */     
/*     */ 
/* 236 */     AbstractAspectJAdvisorFactory.AspectJAnnotation<?> aspectJAnnotation = AbstractAspectJAdvisorFactory.findAspectJAnnotationOnMethod(candidateAdviceMethod);
/* 237 */     if (aspectJAnnotation == null) {
/* 238 */       return null;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 243 */     if (!isAspect(candidateAspectClass))
/*     */     {
/*     */ 
/* 246 */       throw new AopConfigException("Advice must be declared inside an aspect type: Offending method '" + candidateAdviceMethod + "' in class [" + candidateAspectClass.getName() + "]");
/*     */     }
/*     */     
/* 249 */     if (this.logger.isDebugEnabled()) {
/* 250 */       this.logger.debug("Found AspectJ method: " + candidateAdviceMethod);
/*     */     }
/*     */     AbstractAspectJAdvice springAdvice;
/*     */     AbstractAspectJAdvice springAdvice;
/*     */     AbstractAspectJAdvice springAdvice;
/* 255 */     switch (aspectJAnnotation.getAnnotationType()) {
/*     */     case AtBefore: 
/* 257 */       springAdvice = new AspectJMethodBeforeAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
/*     */       
/* 259 */       break;
/*     */     case AtAfter: 
/* 261 */       springAdvice = new AspectJAfterAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
/*     */       
/* 263 */       break;
/*     */     case AtAfterReturning: 
/* 265 */       AbstractAspectJAdvice springAdvice = new AspectJAfterReturningAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
/*     */       
/* 267 */       AfterReturning afterReturningAnnotation = (AfterReturning)aspectJAnnotation.getAnnotation();
/* 268 */       if (StringUtils.hasText(afterReturningAnnotation.returning())) {
/* 269 */         springAdvice.setReturningName(afterReturningAnnotation.returning());
/*     */       }
/*     */       break;
/*     */     case AtAfterThrowing: 
/* 273 */       AbstractAspectJAdvice springAdvice = new AspectJAfterThrowingAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
/*     */       
/* 275 */       AfterThrowing afterThrowingAnnotation = (AfterThrowing)aspectJAnnotation.getAnnotation();
/* 276 */       if (StringUtils.hasText(afterThrowingAnnotation.throwing())) {
/* 277 */         springAdvice.setThrowingName(afterThrowingAnnotation.throwing());
/*     */       }
/*     */       break;
/*     */     case AtAround: 
/* 281 */       springAdvice = new AspectJAroundAdvice(candidateAdviceMethod, expressionPointcut, aspectInstanceFactory);
/*     */       
/* 283 */       break;
/*     */     case AtPointcut: 
/* 285 */       if (this.logger.isDebugEnabled()) {
/* 286 */         this.logger.debug("Processing pointcut '" + candidateAdviceMethod.getName() + "'");
/*     */       }
/* 288 */       return null;
/*     */     default: 
/* 290 */       throw new UnsupportedOperationException("Unsupported advice type on method: " + candidateAdviceMethod);
/*     */     }
/*     */     
/*     */     
/*     */     AbstractAspectJAdvice springAdvice;
/* 295 */     springAdvice.setAspectName(aspectName);
/* 296 */     springAdvice.setDeclarationOrder(declarationOrder);
/* 297 */     String[] argNames = this.parameterNameDiscoverer.getParameterNames(candidateAdviceMethod);
/* 298 */     if (argNames != null) {
/* 299 */       springAdvice.setArgumentNamesFromStringArray(argNames);
/*     */     }
/* 301 */     springAdvice.calculateArgumentBindings();
/* 302 */     return springAdvice;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static class SyntheticInstantiationAdvisor
/*     */     extends DefaultPointcutAdvisor
/*     */   {
/*     */     public SyntheticInstantiationAdvisor(MetadataAwareAspectInstanceFactory aif)
/*     */     {
/* 315 */       super(new MethodBeforeAdvice()
/*     */       {
/*     */         public void before(Method method, Object[] args, Object target)
/*     */         {
/* 319 */           ReflectiveAspectJAdvisorFactory.SyntheticInstantiationAdvisor.this.getAspectInstance();
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\aspectj\annotation\ReflectiveAspectJAdvisorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */