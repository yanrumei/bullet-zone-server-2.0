/*     */ package org.hibernate.validator.internal.cfg.context;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Set;
/*     */ import javax.validation.Constraint;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import org.hibernate.validator.cfg.ConstraintMapping;
/*     */ import org.hibernate.validator.cfg.context.ConstraintDefinitionContext;
/*     */ import org.hibernate.validator.cfg.context.TypeConstraintMappingContext;
/*     */ import org.hibernate.validator.internal.engine.constraintdefinition.ConstraintDefinitionContribution;
/*     */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.Contracts;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.logging.Messages;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DefaultConstraintMapping
/*     */   implements ConstraintMapping
/*     */ {
/*  38 */   private static final Log log = ;
/*     */   private final AnnotationProcessingOptionsImpl annotationProcessingOptions;
/*     */   private final Set<Class<?>> configuredTypes;
/*     */   private final Set<TypeConstraintMappingContextImpl<?>> typeContexts;
/*     */   private final Set<Class<?>> definedConstraints;
/*     */   private final Set<ConstraintDefinitionContextImpl<?>> constraintContexts;
/*     */   
/*     */   public DefaultConstraintMapping()
/*     */   {
/*  47 */     this.annotationProcessingOptions = new AnnotationProcessingOptionsImpl();
/*  48 */     this.configuredTypes = CollectionHelper.newHashSet();
/*  49 */     this.typeContexts = CollectionHelper.newHashSet();
/*  50 */     this.definedConstraints = CollectionHelper.newHashSet();
/*  51 */     this.constraintContexts = CollectionHelper.newHashSet();
/*     */   }
/*     */   
/*     */   public final <C> TypeConstraintMappingContext<C> type(Class<C> type)
/*     */   {
/*  56 */     Contracts.assertNotNull(type, Messages.MESSAGES.beanTypeMustNotBeNull());
/*     */     
/*  58 */     if (this.configuredTypes.contains(type)) {
/*  59 */       throw log.getBeanClassHasAlreadyBeConfiguredViaProgrammaticApiException(type.getName());
/*     */     }
/*     */     
/*  62 */     TypeConstraintMappingContextImpl<C> typeContext = new TypeConstraintMappingContextImpl(this, type);
/*  63 */     this.typeContexts.add(typeContext);
/*  64 */     this.configuredTypes.add(type);
/*     */     
/*  66 */     return typeContext;
/*     */   }
/*     */   
/*     */   public final AnnotationProcessingOptionsImpl getAnnotationProcessingOptions() {
/*  70 */     return this.annotationProcessingOptions;
/*     */   }
/*     */   
/*     */   public Set<Class<?>> getConfiguredTypes() {
/*  74 */     return this.configuredTypes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<BeanConfiguration<?>> getBeanConfigurations(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider)
/*     */   {
/*  86 */     Set<BeanConfiguration<?>> configurations = CollectionHelper.newHashSet();
/*     */     
/*  88 */     for (TypeConstraintMappingContextImpl<?> typeContext : this.typeContexts) {
/*  89 */       configurations.add(typeContext.build(constraintHelper, parameterNameProvider));
/*     */     }
/*     */     
/*  92 */     return configurations;
/*     */   }
/*     */   
/*     */   public <A extends Annotation> ConstraintDefinitionContext<A> constraintDefinition(Class<A> annotationClass)
/*     */   {
/*  97 */     Contracts.assertNotNull(annotationClass, Messages.MESSAGES.annotationTypeMustNotBeNull());
/*  98 */     Contracts.assertTrue(annotationClass.isAnnotationPresent(Constraint.class), Messages.MESSAGES
/*  99 */       .annotationTypeMustBeAnnotatedWithConstraint());
/*     */     
/* 101 */     if (this.definedConstraints.contains(annotationClass))
/*     */     {
/* 103 */       throw log.getConstraintHasAlreadyBeenConfiguredViaProgrammaticApiException(annotationClass.getName());
/*     */     }
/*     */     
/* 106 */     ConstraintDefinitionContextImpl<A> constraintContext = new ConstraintDefinitionContextImpl(this, annotationClass);
/* 107 */     this.constraintContexts.add(constraintContext);
/* 108 */     this.definedConstraints.add(annotationClass);
/*     */     
/* 110 */     return constraintContext;
/*     */   }
/*     */   
/*     */   public Set<ConstraintDefinitionContribution<?>> getConstraintDefinitionContributions() {
/* 114 */     Set<ConstraintDefinitionContribution<?>> contributions = CollectionHelper.newHashSet();
/*     */     
/* 116 */     for (ConstraintDefinitionContextImpl<?> constraintContext : this.constraintContexts) {
/* 117 */       contributions.add(constraintContext.build());
/*     */     }
/*     */     
/* 120 */     return contributions;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\cfg\context\DefaultConstraintMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */