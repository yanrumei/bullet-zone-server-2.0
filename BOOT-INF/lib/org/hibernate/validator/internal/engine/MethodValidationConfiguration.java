/*     */ package org.hibernate.validator.internal.engine;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import org.hibernate.validator.internal.metadata.aggregated.rule.MethodConfigurationRule;
/*     */ import org.hibernate.validator.internal.metadata.aggregated.rule.OverridingMethodMustNotAlterParameterConstraints;
/*     */ import org.hibernate.validator.internal.metadata.aggregated.rule.ParallelMethodsMustNotDefineGroupConversionForCascadedReturnValue;
/*     */ import org.hibernate.validator.internal.metadata.aggregated.rule.ParallelMethodsMustNotDefineParameterConstraints;
/*     */ import org.hibernate.validator.internal.metadata.aggregated.rule.ReturnValueMayOnlyBeMarkedOnceAsCascadedPerHierarchyLine;
/*     */ import org.hibernate.validator.internal.metadata.aggregated.rule.VoidMethodsMustNotBeReturnValueConstrained;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MethodValidationConfiguration
/*     */ {
/*  38 */   private boolean allowOverridingMethodAlterParameterConstraint = false;
/*  39 */   private boolean allowMultipleCascadedValidationOnReturnValues = false;
/*  40 */   private boolean allowParallelMethodsDefineParameterConstraints = false;
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
/*     */   public MethodValidationConfiguration allowOverridingMethodAlterParameterConstraint(boolean allow)
/*     */   {
/*  58 */     this.allowOverridingMethodAlterParameterConstraint = allow;
/*  59 */     return this;
/*     */   }
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
/*     */   public MethodValidationConfiguration allowMultipleCascadedValidationOnReturnValues(boolean allow)
/*     */   {
/*  76 */     this.allowMultipleCascadedValidationOnReturnValues = allow;
/*  77 */     return this;
/*     */   }
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
/*     */   public MethodValidationConfiguration allowParallelMethodsDefineParameterConstraints(boolean allow)
/*     */   {
/*  96 */     this.allowParallelMethodsDefineParameterConstraints = allow;
/*  97 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAllowOverridingMethodAlterParameterConstraint()
/*     */   {
/* 105 */     return this.allowOverridingMethodAlterParameterConstraint;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isAllowMultipleCascadedValidationOnReturnValues()
/*     */   {
/* 113 */     return this.allowMultipleCascadedValidationOnReturnValues;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isAllowParallelMethodsDefineParameterConstraints()
/*     */   {
/* 120 */     return this.allowParallelMethodsDefineParameterConstraints;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<MethodConfigurationRule> getConfiguredRuleSet()
/*     */   {
/* 130 */     HashSet<MethodConfigurationRule> result = CollectionHelper.newHashSet();
/*     */     
/* 132 */     if (!isAllowOverridingMethodAlterParameterConstraint()) {
/* 133 */       result.add(new OverridingMethodMustNotAlterParameterConstraints());
/*     */     }
/*     */     
/* 136 */     if (!isAllowParallelMethodsDefineParameterConstraints()) {
/* 137 */       result.add(new ParallelMethodsMustNotDefineParameterConstraints());
/*     */     }
/*     */     
/* 140 */     result.add(new VoidMethodsMustNotBeReturnValueConstrained());
/*     */     
/* 142 */     if (!isAllowMultipleCascadedValidationOnReturnValues()) {
/* 143 */       result.add(new ReturnValueMayOnlyBeMarkedOnceAsCascadedPerHierarchyLine());
/*     */     }
/*     */     
/* 146 */     result.add(new ParallelMethodsMustNotDefineGroupConversionForCascadedReturnValue());
/*     */     
/* 148 */     return Collections.unmodifiableSet(result);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\MethodValidationConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */