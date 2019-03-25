/*     */ package org.hibernate.validator.internal.metadata.descriptor;
/*     */ 
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.validation.metadata.ConstructorDescriptor;
/*     */ import javax.validation.metadata.CrossParameterDescriptor;
/*     */ import javax.validation.metadata.MethodDescriptor;
/*     */ import javax.validation.metadata.ParameterDescriptor;
/*     */ import javax.validation.metadata.ReturnValueDescriptor;
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
/*     */ public class ExecutableDescriptorImpl
/*     */   extends ElementDescriptorImpl
/*     */   implements ConstructorDescriptor, MethodDescriptor
/*     */ {
/*     */   private final String name;
/*     */   private final List<ParameterDescriptor> parameters;
/*     */   private final CrossParameterDescriptor crossParameterDescriptor;
/*     */   private final ReturnValueDescriptor returnValueDescriptor;
/*     */   private final boolean isGetter;
/*     */   
/*     */   public ExecutableDescriptorImpl(Type returnType, String name, Set<ConstraintDescriptorImpl<?>> crossParameterConstraints, ReturnValueDescriptor returnValueDescriptor, List<ParameterDescriptor> parameters, boolean defaultGroupSequenceRedefined, boolean isGetter, List<Class<?>> defaultGroupSequence)
/*     */   {
/*  41 */     super(returnType, 
/*     */     
/*  43 */       Collections.emptySet(), defaultGroupSequenceRedefined, defaultGroupSequence);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  48 */     this.name = name;
/*  49 */     this.parameters = Collections.unmodifiableList(parameters);
/*  50 */     this.returnValueDescriptor = returnValueDescriptor;
/*  51 */     this.crossParameterDescriptor = new CrossParameterDescriptorImpl(crossParameterConstraints, defaultGroupSequenceRedefined, defaultGroupSequence);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*  56 */     this.isGetter = isGetter;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  61 */     return this.name;
/*     */   }
/*     */   
/*     */   public List<ParameterDescriptor> getParameterDescriptors()
/*     */   {
/*  66 */     return this.parameters;
/*     */   }
/*     */   
/*     */   public ReturnValueDescriptor getReturnValueDescriptor()
/*     */   {
/*  71 */     return this.returnValueDescriptor;
/*     */   }
/*     */   
/*     */   public boolean hasConstrainedParameters()
/*     */   {
/*  76 */     if (this.crossParameterDescriptor.hasConstraints()) {
/*  77 */       return true;
/*     */     }
/*     */     
/*  80 */     for (ParameterDescriptor oneParameter : this.parameters) {
/*  81 */       if ((oneParameter.hasConstraints()) || (oneParameter.isCascaded())) {
/*  82 */         return true;
/*     */       }
/*     */     }
/*     */     
/*  86 */     return false;
/*     */   }
/*     */   
/*     */   public boolean hasConstrainedReturnValue()
/*     */   {
/*  91 */     return (this.returnValueDescriptor != null) && ((this.returnValueDescriptor.hasConstraints()) || 
/*  92 */       (this.returnValueDescriptor.isCascaded()));
/*     */   }
/*     */   
/*     */   public CrossParameterDescriptor getCrossParameterDescriptor()
/*     */   {
/*  97 */     return this.crossParameterDescriptor;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 102 */     StringBuilder sb = new StringBuilder();
/* 103 */     sb.append("ExecutableDescriptorImpl");
/* 104 */     sb.append("{name='").append(this.name).append('\'');
/* 105 */     sb.append('}');
/* 106 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public boolean isGetter() {
/* 110 */     return this.isGetter;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\descriptor\ExecutableDescriptorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */