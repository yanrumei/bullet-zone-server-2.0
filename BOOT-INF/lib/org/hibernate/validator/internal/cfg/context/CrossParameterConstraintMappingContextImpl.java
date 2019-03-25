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
/*    */ final class CrossParameterConstraintMappingContextImpl
/*    */   extends ConstraintMappingContextImplBase
/*    */   implements CrossParameterConstraintMappingContext
/*    */ {
/*    */   private final ExecutableConstraintMappingContextImpl executableContext;
/*    */   
/*    */   CrossParameterConstraintMappingContextImpl(ExecutableConstraintMappingContextImpl executableContext)
/*    */   {
/* 29 */     super(executableContext.getTypeContext().getConstraintMapping());
/* 30 */     this.executableContext = executableContext;
/*    */   }
/*    */   
/*    */   public CrossParameterConstraintMappingContext constraint(ConstraintDef<?, ?> definition)
/*    */   {
/* 35 */     super.addConstraint(ConfiguredConstraint.forCrossParameter(definition, this.executableContext.getExecutable()));
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public CrossParameterConstraintMappingContext ignoreAnnotations(boolean ignoreAnnotations)
/*    */   {
/* 41 */     this.mapping.getAnnotationProcessingOptions().ignoreConstraintAnnotationsForCrossParameterConstraint(this.executableContext
/* 42 */       .getExecutable().getMember(), Boolean.valueOf(ignoreAnnotations));
/*    */     
/* 44 */     return this;
/*    */   }
/*    */   
/*    */   public ParameterConstraintMappingContext parameter(int index)
/*    */   {
/* 49 */     return this.executableContext.parameter(index);
/*    */   }
/*    */   
/*    */   public MethodConstraintMappingContext method(String name, Class<?>... parameterTypes)
/*    */   {
/* 54 */     return this.executableContext.getTypeContext().method(name, parameterTypes);
/*    */   }
/*    */   
/*    */   public ConstructorConstraintMappingContext constructor(Class<?>... parameterTypes)
/*    */   {
/* 59 */     return this.executableContext.getTypeContext().constructor(parameterTypes);
/*    */   }
/*    */   
/*    */   public ReturnValueConstraintMappingContext returnValue()
/*    */   {
/* 64 */     return this.executableContext.returnValue();
/*    */   }
/*    */   
/*    */   protected ConstraintDescriptorImpl.ConstraintType getConstraintType()
/*    */   {
/* 69 */     return ConstraintDescriptorImpl.ConstraintType.CROSS_PARAMETER;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\cfg\context\CrossParameterConstraintMappingContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */