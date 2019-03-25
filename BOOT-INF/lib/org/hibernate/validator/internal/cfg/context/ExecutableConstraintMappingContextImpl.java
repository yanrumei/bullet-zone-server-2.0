/*     */ package org.hibernate.validator.internal.cfg.context;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import org.hibernate.validator.cfg.context.CrossParameterConstraintMappingContext;
/*     */ import org.hibernate.validator.cfg.context.ParameterConstraintMappingContext;
/*     */ import org.hibernate.validator.cfg.context.ReturnValueConstraintMappingContext;
/*     */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedParameter;
/*     */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.ReflectionHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
/*     */ abstract class ExecutableConstraintMappingContextImpl
/*     */ {
/*  43 */   private static final Log log = ;
/*     */   protected final TypeConstraintMappingContextImpl<?> typeContext;
/*     */   protected final ExecutableElement executable;
/*     */   private final ParameterConstraintMappingContextImpl[] parameterContexts;
/*     */   private ReturnValueConstraintMappingContextImpl returnValueContext;
/*     */   private CrossParameterConstraintMappingContextImpl crossParameterContext;
/*     */   
/*     */   <T> ExecutableConstraintMappingContextImpl(TypeConstraintMappingContextImpl<T> typeContext, Constructor<T> constructor)
/*     */   {
/*  52 */     this(typeContext, ExecutableElement.forConstructor(constructor));
/*     */   }
/*     */   
/*     */   ExecutableConstraintMappingContextImpl(TypeConstraintMappingContextImpl<?> typeContext, Method method) {
/*  56 */     this(typeContext, ExecutableElement.forMethod(method));
/*     */   }
/*     */   
/*     */   private ExecutableConstraintMappingContextImpl(TypeConstraintMappingContextImpl<?> typeContext, ExecutableElement executable) {
/*  60 */     this.typeContext = typeContext;
/*  61 */     this.executable = executable;
/*  62 */     this.parameterContexts = new ParameterConstraintMappingContextImpl[executable.getParameterTypes().length];
/*     */   }
/*     */   
/*     */   public ParameterConstraintMappingContext parameter(int index) {
/*  66 */     if ((index < 0) || (index >= this.executable.getParameterTypes().length)) {
/*  67 */       throw log.getInvalidExecutableParameterIndexException(this.executable.getAsString(), index);
/*     */     }
/*     */     
/*  70 */     ParameterConstraintMappingContextImpl context = this.parameterContexts[index];
/*     */     
/*  72 */     if (context != null) {
/*  73 */       throw log.getParameterHasAlreadyBeConfiguredViaProgrammaticApiException(this.typeContext
/*  74 */         .getBeanClass().getName(), this.executable
/*  75 */         .getAsString(), index);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  80 */     context = new ParameterConstraintMappingContextImpl(this, index);
/*  81 */     this.parameterContexts[index] = context;
/*  82 */     return context;
/*     */   }
/*     */   
/*     */   public CrossParameterConstraintMappingContext crossParameter() {
/*  86 */     if (this.crossParameterContext != null) {
/*  87 */       throw log.getCrossParameterElementHasAlreadyBeConfiguredViaProgrammaticApiException(this.typeContext
/*  88 */         .getBeanClass().getName(), this.executable
/*  89 */         .getAsString());
/*     */     }
/*     */     
/*     */ 
/*  93 */     this.crossParameterContext = new CrossParameterConstraintMappingContextImpl(this);
/*  94 */     return this.crossParameterContext;
/*     */   }
/*     */   
/*     */   public ReturnValueConstraintMappingContext returnValue() {
/*  98 */     if (this.returnValueContext != null) {
/*  99 */       throw log.getReturnValueHasAlreadyBeConfiguredViaProgrammaticApiException(this.typeContext
/* 100 */         .getBeanClass().getName(), this.executable
/* 101 */         .getAsString());
/*     */     }
/*     */     
/*     */ 
/* 105 */     this.returnValueContext = new ReturnValueConstraintMappingContextImpl(this);
/* 106 */     return this.returnValueContext;
/*     */   }
/*     */   
/*     */   public ExecutableElement getExecutable() {
/* 110 */     return this.executable;
/*     */   }
/*     */   
/*     */   public TypeConstraintMappingContextImpl<?> getTypeContext() {
/* 114 */     return this.typeContext;
/*     */   }
/*     */   
/*     */   public ConstrainedElement build(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider)
/*     */   {
/* 119 */     return new ConstrainedExecutable(ConfigurationSource.API, 
/*     */     
/* 121 */       ConstraintLocation.forReturnValue(this.executable), 
/* 122 */       getParameters(constraintHelper, parameterNameProvider), this.crossParameterContext != null ? this.crossParameterContext
/* 123 */       .getConstraints(constraintHelper) : Collections.emptySet(), this.returnValueContext != null ? this.returnValueContext
/* 124 */       .getConstraints(constraintHelper) : Collections.emptySet(), 
/* 125 */       Collections.emptySet(), this.returnValueContext != null ? this.returnValueContext
/* 126 */       .getGroupConversions() : Collections.emptyMap(), this.returnValueContext != null ? this.returnValueContext
/* 127 */       .isCascading() : false, this.returnValueContext != null ? this.returnValueContext
/* 128 */       .unwrapMode() : UnwrapMode.AUTOMATIC);
/*     */   }
/*     */   
/*     */   private List<ConstrainedParameter> getParameters(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider)
/*     */   {
/* 133 */     List<ConstrainedParameter> constrainedParameters = CollectionHelper.newArrayList();
/*     */     
/* 135 */     for (int i = 0; i < this.parameterContexts.length; i++) {
/* 136 */       ParameterConstraintMappingContextImpl parameter = this.parameterContexts[i];
/* 137 */       if (parameter != null) {
/* 138 */         constrainedParameters.add(parameter.build(constraintHelper, parameterNameProvider));
/*     */       }
/*     */       else {
/* 141 */         constrainedParameters.add(new ConstrainedParameter(ConfigurationSource.API, 
/*     */         
/*     */ 
/* 144 */           ConstraintLocation.forParameter(this.executable, i), 
/* 145 */           ReflectionHelper.typeOf(this.executable, i), i, 
/*     */           
/* 147 */           (String)this.executable.getParameterNames(parameterNameProvider).get(i)));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 153 */     return constrainedParameters;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\cfg\context\ExecutableConstraintMappingContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */