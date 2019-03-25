/*     */ package org.hibernate.validator.internal.cfg.context;
/*     */ 
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import org.hibernate.validator.cfg.ConstraintDef;
/*     */ import org.hibernate.validator.cfg.context.ConstructorConstraintMappingContext;
/*     */ import org.hibernate.validator.cfg.context.MethodConstraintMappingContext;
/*     */ import org.hibernate.validator.cfg.context.PropertyConstraintMappingContext;
/*     */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl.ConstraintType;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedField;
/*     */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
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
/*     */ final class PropertyConstraintMappingContextImpl
/*     */   extends CascadableConstraintMappingContextImplBase<PropertyConstraintMappingContext>
/*     */   implements PropertyConstraintMappingContext
/*     */ {
/*     */   private final TypeConstraintMappingContextImpl<?> typeContext;
/*     */   private final Member member;
/*     */   
/*     */   PropertyConstraintMappingContextImpl(TypeConstraintMappingContextImpl<?> typeContext, Member member)
/*     */   {
/*  44 */     super(typeContext.getConstraintMapping());
/*  45 */     this.typeContext = typeContext;
/*  46 */     this.member = member;
/*     */   }
/*     */   
/*     */   protected PropertyConstraintMappingContextImpl getThis()
/*     */   {
/*  51 */     return this;
/*     */   }
/*     */   
/*     */   public PropertyConstraintMappingContext constraint(ConstraintDef<?, ?> definition)
/*     */   {
/*  56 */     if ((this.member instanceof Field)) {
/*  57 */       super.addConstraint(
/*  58 */         ConfiguredConstraint.forProperty(definition, this.member));
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/*  64 */       super.addConstraint(
/*  65 */         ConfiguredConstraint.forReturnValue(definition, 
/*  66 */         ExecutableElement.forMethod((Method)this.member)));
/*     */     }
/*     */     
/*     */ 
/*  70 */     return this;
/*     */   }
/*     */   
/*     */   public PropertyConstraintMappingContext ignoreAnnotations()
/*     */   {
/*  75 */     return ignoreAnnotations(true);
/*     */   }
/*     */   
/*     */   public PropertyConstraintMappingContext ignoreAnnotations(boolean ignoreAnnotations)
/*     */   {
/*  80 */     this.mapping.getAnnotationProcessingOptions().ignoreConstraintAnnotationsOnMember(this.member, Boolean.valueOf(ignoreAnnotations));
/*  81 */     return this;
/*     */   }
/*     */   
/*     */   public PropertyConstraintMappingContext property(String property, ElementType elementType)
/*     */   {
/*  86 */     return this.typeContext.property(property, elementType);
/*     */   }
/*     */   
/*     */   public ConstructorConstraintMappingContext constructor(Class<?>... parameterTypes)
/*     */   {
/*  91 */     return this.typeContext.constructor(parameterTypes);
/*     */   }
/*     */   
/*     */   public MethodConstraintMappingContext method(String name, Class<?>... parameterTypes)
/*     */   {
/*  96 */     return this.typeContext.method(name, parameterTypes);
/*     */   }
/*     */   
/*     */   ConstrainedElement build(ConstraintHelper constraintHelper)
/*     */   {
/* 101 */     if ((this.member instanceof Field)) {
/* 102 */       return new ConstrainedField(ConfigurationSource.API, 
/*     */       
/* 104 */         ConstraintLocation.forProperty(this.member), 
/* 105 */         getConstraints(constraintHelper), 
/* 106 */         Collections.emptySet(), this.groupConversions, this.isCascading, 
/*     */         
/*     */ 
/* 109 */         unwrapMode());
/*     */     }
/*     */     
/*     */ 
/* 113 */     return new ConstrainedExecutable(ConfigurationSource.API, 
/*     */     
/* 115 */       ConstraintLocation.forProperty(this.member), 
/* 116 */       getConstraints(constraintHelper), this.groupConversions, this.isCascading, 
/*     */       
/*     */ 
/* 119 */       unwrapMode());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ConstraintDescriptorImpl.ConstraintType getConstraintType()
/*     */   {
/* 126 */     return ConstraintDescriptorImpl.ConstraintType.GENERIC;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\cfg\context\PropertyConstraintMappingContextImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */