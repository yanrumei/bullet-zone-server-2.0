/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.springframework.aop.ClassFilter;
/*     */ import org.springframework.aop.IntroductionAdvisor;
/*     */ import org.springframework.aop.support.ClassFilters;
/*     */ import org.springframework.aop.support.DelegatePerTargetObjectIntroductionInterceptor;
/*     */ import org.springframework.aop.support.DelegatingIntroductionInterceptor;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DeclareParentsAdvisor
/*     */   implements IntroductionAdvisor
/*     */ {
/*     */   private final Class<?> introducedInterface;
/*     */   private final ClassFilter typePatternClassFilter;
/*     */   private final Advice advice;
/*     */   
/*     */   public DeclareParentsAdvisor(Class<?> interfaceType, String typePattern, Class<?> defaultImpl)
/*     */   {
/*  51 */     this(interfaceType, typePattern, defaultImpl, new DelegatePerTargetObjectIntroductionInterceptor(defaultImpl, interfaceType));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeclareParentsAdvisor(Class<?> interfaceType, String typePattern, Object delegateRef)
/*     */   {
/*  62 */     this(interfaceType, typePattern, delegateRef.getClass(), new DelegatingIntroductionInterceptor(delegateRef));
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
/*     */   private DeclareParentsAdvisor(Class<?> interfaceType, String typePattern, Class<?> implementationClass, Advice advice)
/*     */   {
/*  75 */     this.introducedInterface = interfaceType;
/*  76 */     ClassFilter typePatternFilter = new TypePatternClassFilter(typePattern);
/*     */     
/*     */ 
/*  79 */     ClassFilter exclusion = new ClassFilter()
/*     */     {
/*     */       public boolean matches(Class<?> clazz) {
/*  82 */         return !DeclareParentsAdvisor.this.introducedInterface.isAssignableFrom(clazz);
/*     */       }
/*     */       
/*  85 */     };
/*  86 */     this.typePatternClassFilter = ClassFilters.intersection(typePatternFilter, exclusion);
/*  87 */     this.advice = advice;
/*     */   }
/*     */   
/*     */ 
/*     */   public ClassFilter getClassFilter()
/*     */   {
/*  93 */     return this.typePatternClassFilter;
/*     */   }
/*     */   
/*     */ 
/*     */   public void validateInterfaces()
/*     */     throws IllegalArgumentException
/*     */   {}
/*     */   
/*     */   public boolean isPerInstance()
/*     */   {
/* 103 */     return true;
/*     */   }
/*     */   
/*     */   public Advice getAdvice()
/*     */   {
/* 108 */     return this.advice;
/*     */   }
/*     */   
/*     */   public Class<?>[] getInterfaces()
/*     */   {
/* 113 */     return new Class[] { this.introducedInterface };
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\aspectj\DeclareParentsAdvisor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */