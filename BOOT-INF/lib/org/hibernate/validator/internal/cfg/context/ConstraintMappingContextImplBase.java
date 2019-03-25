/*    */ package org.hibernate.validator.internal.cfg.context;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.Collections;
/*    */ import java.util.Set;
/*    */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*    */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*    */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
/*    */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl.ConstraintType;
/*    */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
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
/*    */ abstract class ConstraintMappingContextImplBase
/*    */   extends ConstraintContextImplBase
/*    */ {
/*    */   private final Set<ConfiguredConstraint<?>> constraints;
/*    */   
/*    */   ConstraintMappingContextImplBase(DefaultConstraintMapping mapping)
/*    */   {
/* 30 */     super(mapping);
/* 31 */     this.constraints = CollectionHelper.newHashSet();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   protected abstract ConstraintDescriptorImpl.ConstraintType getConstraintType();
/*    */   
/*    */ 
/*    */ 
/*    */   protected DefaultConstraintMapping getConstraintMapping()
/*    */   {
/* 42 */     return this.mapping;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected void addConstraint(ConfiguredConstraint<?> constraint)
/*    */   {
/* 51 */     this.constraints.add(constraint);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Set<MetaConstraint<?>> getConstraints(ConstraintHelper constraintHelper)
/*    */   {
/* 62 */     if (this.constraints == null) {
/* 63 */       return Collections.emptySet();
/*    */     }
/*    */     
/* 66 */     Set<MetaConstraint<?>> metaConstraints = CollectionHelper.newHashSet();
/*    */     
/* 68 */     for (ConfiguredConstraint<?> configuredConstraint : this.constraints) {
/* 69 */       metaConstraints.add(asMetaConstraint(configuredConstraint, constraintHelper));
/*    */     }
/*    */     
/* 72 */     return metaConstraints;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private <A extends Annotation> MetaConstraint<A> asMetaConstraint(ConfiguredConstraint<A> config, ConstraintHelper constraintHelper)
/*    */   {
/* 81 */     ConstraintDescriptorImpl<A> constraintDescriptor = new ConstraintDescriptorImpl(constraintHelper, config.getLocation().getMember(), config.createAnnotationProxy(), config.getElementType(), getConstraintType());
/*    */     
/*    */ 
/* 84 */     return new MetaConstraint(constraintDescriptor, config.getLocation());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\cfg\context\ConstraintMappingContextImplBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */