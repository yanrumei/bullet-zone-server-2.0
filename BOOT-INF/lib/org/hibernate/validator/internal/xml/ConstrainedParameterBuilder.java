/*    */ package org.hibernate.validator.internal.xml;
/*    */ 
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import javax.validation.ParameterNameProvider;
/*    */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*    */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
/*    */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*    */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*    */ import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
/*    */ import org.hibernate.validator.internal.metadata.raw.ConstrainedParameter;
/*    */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
/*    */ import org.hibernate.validator.internal.util.CollectionHelper;
/*    */ import org.hibernate.validator.internal.util.ReflectionHelper;
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
/*    */ class ConstrainedParameterBuilder
/*    */ {
/*    */   private final GroupConversionBuilder groupConversionBuilder;
/*    */   private final ParameterNameProvider parameterNameProvider;
/*    */   private final MetaConstraintBuilder metaConstraintBuilder;
/*    */   private final AnnotationProcessingOptionsImpl annotationProcessingOptions;
/*    */   
/*    */   ConstrainedParameterBuilder(MetaConstraintBuilder metaConstraintBuilder, ParameterNameProvider parameterNameProvider, GroupConversionBuilder groupConversionBuilder, AnnotationProcessingOptionsImpl annotationProcessingOptions)
/*    */   {
/* 43 */     this.metaConstraintBuilder = metaConstraintBuilder;
/* 44 */     this.parameterNameProvider = parameterNameProvider;
/* 45 */     this.groupConversionBuilder = groupConversionBuilder;
/* 46 */     this.annotationProcessingOptions = annotationProcessingOptions;
/*    */   }
/*    */   
/*    */ 
/*    */   List<ConstrainedParameter> buildConstrainedParameters(List<ParameterType> parameterList, ExecutableElement executableElement, String defaultPackage)
/*    */   {
/* 52 */     List<ConstrainedParameter> constrainedParameters = CollectionHelper.newArrayList();
/* 53 */     int i = 0;
/* 54 */     List<String> parameterNames = executableElement.getParameterNames(this.parameterNameProvider);
/* 55 */     for (ParameterType parameterType : parameterList) {
/* 56 */       ConstraintLocation constraintLocation = ConstraintLocation.forParameter(executableElement, i);
/* 57 */       Set<MetaConstraint<?>> metaConstraints = CollectionHelper.newHashSet();
/* 58 */       for (ConstraintType constraint : parameterType.getConstraint()) {
/* 59 */         MetaConstraint<?> metaConstraint = this.metaConstraintBuilder.buildMetaConstraint(constraintLocation, constraint, ElementType.PARAMETER, defaultPackage, null);
/*    */         
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 66 */         metaConstraints.add(metaConstraint);
/*    */       }
/* 68 */       Object groupConversions = this.groupConversionBuilder.buildGroupConversionMap(parameterType
/* 69 */         .getConvertGroup(), defaultPackage);
/*    */       
/*    */ 
/*    */ 
/*    */ 
/* 74 */       if (parameterType.getIgnoreAnnotations() != null) {
/* 75 */         this.annotationProcessingOptions.ignoreConstraintAnnotationsOnParameter(executableElement
/* 76 */           .getMember(), i, parameterType
/*    */           
/* 78 */           .getIgnoreAnnotations());
/*    */       }
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
/* 92 */       ConstrainedParameter constrainedParameter = new ConstrainedParameter(ConfigurationSource.XML, constraintLocation, ReflectionHelper.typeOf(executableElement, i), i, (String)parameterNames.get(i), metaConstraints, Collections.emptySet(), (Map)groupConversions, parameterType.getValid() != null, UnwrapMode.AUTOMATIC);
/*    */       
/*    */ 
/* 95 */       constrainedParameters.add(constrainedParameter);
/* 96 */       i++;
/*    */     }
/*    */     
/* 99 */     return constrainedParameters;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\ConstrainedParameterBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */