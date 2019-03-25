/*    */ package org.hibernate.validator.internal.cfg.context;
/*    */ 
/*    */ import org.hibernate.validator.cfg.ConstraintDef;
/*    */ import org.hibernate.validator.cfg.context.ConstructorConstraintMappingContext;
/*    */ import org.hibernate.validator.cfg.context.CrossParameterConstraintMappingContext;
/*    */ import org.hibernate.validator.cfg.context.MethodConstraintMappingContext;
/*    */ import org.hibernate.validator.cfg.context.ParameterConstraintMappingContext;
/*    */ import org.hibernate.validator.cfg.context.ReturnValueConstraintMappingContext;
/*    */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
/*    */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl.ConstraintType;
/*    */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
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
/*    */ final class ReturnValueConstraintMappingContextImpl
/*    */   extends CascadableConstraintMappingContextImplBase<ReturnValueConstraintMappingContext>
/*    */   implements ReturnValueConstraintMappingContext
/*    */ {
/*    */   private final ExecutableConstraintMappingContextImpl executableContext;
/*    */   
/*    */   ReturnValueConstraintMappingContextImpl(ExecutableConstraintMappingContextImpl executableContext)
/*    */   {
/* 31 */     super(executableContext.getTypeContext().getConstraintMapping());
/* 32 */     this.executableContext = executableContext;
/*    */   }
/*    */   
/*    */   protected ReturnValueConstraintMappingContext getThis()
/*    */   {
/* 37 */     return this;
/*    */   }
/*    */   
/*    */   public ReturnValueConstraintMappingContext constraint(ConstraintDef<?, ?> definition)
/*    */   {
/* 42 */     super.addConstraint(ConfiguredConstraint.forReturnValue(definition, this.executableContext.getExecutable()));
/* 43 */     return this;
/*    */   }
/*    */   
/*    */   public ReturnValueConstraintMappingContext ignoreAnnotations(boolean ignoreAnnotations)
/*    */   {
/* 48 */     this.mapping.getAnnotationProcessingOptions().ignoreConstraintAnnotationsForReturnValue(this.executableContext
/* 49 */       .getExecutable().getMember(), Boolean.valueOf(ignoreAnnotations));
/*    */     
/* 51 */     return this;
/*    */   }
/*    */   
/*    */   public ParameterConstraintMappingContext parameter(int index)
/*    */   {
/* 56 */     return this.executableContext.parameter(index);
/*    */   }
/*    */   
/*    */   public CrossParameterConstraintMappingContext crossParameter()
/*    */   {
/* 61 */     return this.executableContext.crossParameter();
/*    */   }
/*    */   
/*    */   public MethodConstraintMappingContext method(String name, Class<?>... parameterTypes)
/*    */   {
/* 66 */     return this.executableContext.getTypeContext().method(name, parameterTypes);
/*    */   }
/*    */   
/*    */   public ConstructorConstraintMappingContext constructor(Class<?>... parameterTypes)
/*    */   {
/* 71 */     return this.executableContext.getTypeContext().constructor(parameterTypes);
/*    */   }
/*    */   
/*    */   protected ConstraintDescriptorImpl.ConstraintType getConstraintType()
/*    */   {
/* 76 */     return ConstraintDescriptorImpl.ConstraintType.GENERIC;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\cfg\context\ReturnValueConstraintMappingContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */