/*    */ package org.hibernate.validator.internal.cfg.context;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.hibernate.validator.cfg.context.ConstraintDefinitionContext;
/*    */ import org.hibernate.validator.cfg.context.TypeConstraintMappingContext;
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
/*    */ abstract class ConstraintContextImplBase
/*    */ {
/*    */   protected final DefaultConstraintMapping mapping;
/*    */   
/*    */   public ConstraintContextImplBase(DefaultConstraintMapping mapping)
/*    */   {
/* 25 */     this.mapping = mapping;
/*    */   }
/*    */   
/*    */   public <C> TypeConstraintMappingContext<C> type(Class<C> type) {
/* 29 */     return this.mapping.type(type);
/*    */   }
/*    */   
/*    */   public <A extends Annotation> ConstraintDefinitionContext<A> constraintDefinition(Class<A> annotationClass) {
/* 33 */     return this.mapping.constraintDefinition(annotationClass);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\cfg\context\ConstraintContextImplBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */