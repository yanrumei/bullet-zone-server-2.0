/*    */ package org.hibernate.validator.internal.cfg.context;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.Set;
/*    */ import javax.validation.ConstraintValidator;
/*    */ import org.hibernate.validator.cfg.context.ConstraintDefinitionContext;
/*    */ import org.hibernate.validator.internal.engine.constraintdefinition.ConstraintDefinitionContribution;
/*    */ import org.hibernate.validator.internal.util.CollectionHelper;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ConstraintDefinitionContextImpl<A extends Annotation>
/*    */   extends ConstraintContextImplBase
/*    */   implements ConstraintDefinitionContext<A>
/*    */ {
/*    */   private final Class<A> annotationType;
/* 33 */   private boolean includeExistingValidators = true;
/*    */   
/* 35 */   private final Set<Class<? extends ConstraintValidator<A, ?>>> validatorTypes = CollectionHelper.newHashSet();
/*    */   
/*    */   ConstraintDefinitionContextImpl(DefaultConstraintMapping mapping, Class<A> annotationType) {
/* 38 */     super(mapping);
/* 39 */     this.annotationType = annotationType;
/*    */   }
/*    */   
/*    */   public ConstraintDefinitionContext<A> includeExistingValidators(boolean includeExistingValidators)
/*    */   {
/* 44 */     this.includeExistingValidators = includeExistingValidators;
/* 45 */     return this;
/*    */   }
/*    */   
/*    */   public ConstraintDefinitionContext<A> validatedBy(Class<? extends ConstraintValidator<A, ?>> validator)
/*    */   {
/* 50 */     this.validatorTypes.add(validator);
/* 51 */     return this;
/*    */   }
/*    */   
/*    */   ConstraintDefinitionContribution<A> build()
/*    */   {
/* 56 */     return new ConstraintDefinitionContribution(this.annotationType, 
/*    */     
/* 58 */       CollectionHelper.newArrayList(new Iterable[] { this.validatorTypes }), this.includeExistingValidators);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\cfg\context\ConstraintDefinitionContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */