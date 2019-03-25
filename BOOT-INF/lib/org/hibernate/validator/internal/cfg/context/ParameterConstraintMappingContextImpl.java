/*     */ package org.hibernate.validator.internal.cfg.context;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import org.hibernate.validator.cfg.ConstraintDef;
/*     */ import org.hibernate.validator.cfg.context.ConstructorConstraintMappingContext;
/*     */ import org.hibernate.validator.cfg.context.CrossParameterConstraintMappingContext;
/*     */ import org.hibernate.validator.cfg.context.MethodConstraintMappingContext;
/*     */ import org.hibernate.validator.cfg.context.ParameterConstraintMappingContext;
/*     */ import org.hibernate.validator.cfg.context.ReturnValueConstraintMappingContext;
/*     */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl.ConstraintType;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedParameter;
/*     */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
/*     */ import org.hibernate.validator.internal.util.ReflectionHelper;
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
/*     */ final class ParameterConstraintMappingContextImpl
/*     */   extends CascadableConstraintMappingContextImplBase<ParameterConstraintMappingContext>
/*     */   implements ParameterConstraintMappingContext
/*     */ {
/*     */   private final ExecutableConstraintMappingContextImpl executableContext;
/*     */   private final int parameterIndex;
/*     */   
/*     */   ParameterConstraintMappingContextImpl(ExecutableConstraintMappingContextImpl executableContext, int parameterIndex)
/*     */   {
/*  42 */     super(executableContext.getTypeContext().getConstraintMapping());
/*     */     
/*  44 */     this.executableContext = executableContext;
/*  45 */     this.parameterIndex = parameterIndex;
/*     */   }
/*     */   
/*     */   protected ParameterConstraintMappingContext getThis()
/*     */   {
/*  50 */     return this;
/*     */   }
/*     */   
/*     */   public ParameterConstraintMappingContext constraint(ConstraintDef<?, ?> definition)
/*     */   {
/*  55 */     super.addConstraint(
/*  56 */       ConfiguredConstraint.forParameter(definition, this.executableContext
/*     */       
/*  58 */       .getExecutable(), this.parameterIndex));
/*     */     
/*     */ 
/*     */ 
/*  62 */     return this;
/*     */   }
/*     */   
/*     */   public ParameterConstraintMappingContext ignoreAnnotations(boolean ignoreAnnotations)
/*     */   {
/*  67 */     this.mapping.getAnnotationProcessingOptions().ignoreConstraintAnnotationsOnParameter(this.executableContext
/*  68 */       .getExecutable().getMember(), this.parameterIndex, 
/*     */       
/*  70 */       Boolean.valueOf(ignoreAnnotations));
/*     */     
/*  72 */     return this;
/*     */   }
/*     */   
/*     */   public ParameterConstraintMappingContext parameter(int index)
/*     */   {
/*  77 */     return this.executableContext.parameter(index);
/*     */   }
/*     */   
/*     */   public CrossParameterConstraintMappingContext crossParameter()
/*     */   {
/*  82 */     return this.executableContext.crossParameter();
/*     */   }
/*     */   
/*     */   public ReturnValueConstraintMappingContext returnValue()
/*     */   {
/*  87 */     return this.executableContext.returnValue();
/*     */   }
/*     */   
/*     */   public ConstructorConstraintMappingContext constructor(Class<?>... parameterTypes)
/*     */   {
/*  92 */     return this.executableContext.getTypeContext().constructor(parameterTypes);
/*     */   }
/*     */   
/*     */   public MethodConstraintMappingContext method(String name, Class<?>... parameterTypes)
/*     */   {
/*  97 */     return this.executableContext.getTypeContext().method(name, parameterTypes);
/*     */   }
/*     */   
/*     */   public ConstrainedParameter build(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider)
/*     */   {
/* 102 */     return new ConstrainedParameter(ConfigurationSource.API, 
/*     */     
/* 104 */       ConstraintLocation.forParameter(this.executableContext.getExecutable(), this.parameterIndex), 
/* 105 */       ReflectionHelper.typeOf(this.executableContext.getExecutable(), this.parameterIndex), this.parameterIndex, 
/*     */       
/* 107 */       (String)this.executableContext.getExecutable().getParameterNames(parameterNameProvider).get(this.parameterIndex), 
/* 108 */       getConstraints(constraintHelper), 
/* 109 */       Collections.emptySet(), this.groupConversions, this.isCascading, 
/*     */       
/*     */ 
/* 112 */       unwrapMode());
/*     */   }
/*     */   
/*     */ 
/*     */   protected ConstraintDescriptorImpl.ConstraintType getConstraintType()
/*     */   {
/* 118 */     return ConstraintDescriptorImpl.ConstraintType.GENERIC;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\cfg\context\ParameterConstraintMappingContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */