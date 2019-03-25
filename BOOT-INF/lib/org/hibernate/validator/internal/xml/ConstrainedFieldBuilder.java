/*     */ package org.hibernate.validator.internal.xml;
/*     */ 
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.Field;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*     */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
/*     */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedField;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetDeclaredField;
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
/*     */ class ConstrainedFieldBuilder
/*     */ {
/*  36 */   private static final Log log = ;
/*     */   
/*     */   private final GroupConversionBuilder groupConversionBuilder;
/*     */   private final MetaConstraintBuilder metaConstraintBuilder;
/*     */   private final AnnotationProcessingOptionsImpl annotationProcessingOptions;
/*     */   
/*     */   ConstrainedFieldBuilder(MetaConstraintBuilder metaConstraintBuilder, GroupConversionBuilder groupConversionBuilder, AnnotationProcessingOptionsImpl annotationProcessingOptions)
/*     */   {
/*  44 */     this.metaConstraintBuilder = metaConstraintBuilder;
/*  45 */     this.groupConversionBuilder = groupConversionBuilder;
/*  46 */     this.annotationProcessingOptions = annotationProcessingOptions;
/*     */   }
/*     */   
/*     */ 
/*     */   Set<ConstrainedField> buildConstrainedFields(List<FieldType> fields, Class<?> beanClass, String defaultPackage)
/*     */   {
/*  52 */     Set<ConstrainedField> constrainedFields = CollectionHelper.newHashSet();
/*  53 */     List<String> alreadyProcessedFieldNames = CollectionHelper.newArrayList();
/*  54 */     for (FieldType fieldType : fields) {
/*  55 */       Field field = findField(beanClass, fieldType.getName(), alreadyProcessedFieldNames);
/*  56 */       ConstraintLocation constraintLocation = ConstraintLocation.forProperty(field);
/*  57 */       Set<MetaConstraint<?>> metaConstraints = CollectionHelper.newHashSet();
/*  58 */       for (ConstraintType constraint : fieldType.getConstraint()) {
/*  59 */         MetaConstraint<?> metaConstraint = this.metaConstraintBuilder.buildMetaConstraint(constraintLocation, constraint, ElementType.FIELD, defaultPackage, null);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  66 */         metaConstraints.add(metaConstraint);
/*     */       }
/*  68 */       Object groupConversions = this.groupConversionBuilder.buildGroupConversionMap(fieldType
/*  69 */         .getConvertGroup(), defaultPackage);
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
/*  80 */       ConstrainedField constrainedField = new ConstrainedField(ConfigurationSource.XML, constraintLocation, metaConstraints, Collections.emptySet(), (Map)groupConversions, fieldType.getValid() != null, UnwrapMode.AUTOMATIC);
/*     */       
/*     */ 
/*  83 */       constrainedFields.add(constrainedField);
/*     */       
/*     */ 
/*     */ 
/*  87 */       if (fieldType.getIgnoreAnnotations() != null) {
/*  88 */         this.annotationProcessingOptions.ignoreConstraintAnnotationsOnMember(field, fieldType
/*     */         
/*  90 */           .getIgnoreAnnotations());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  95 */     return constrainedFields;
/*     */   }
/*     */   
/*     */   private static Field findField(Class<?> beanClass, String fieldName, List<String> alreadyProcessedFieldNames) {
/*  99 */     if (alreadyProcessedFieldNames.contains(fieldName)) {
/* 100 */       throw log.getIsDefinedTwiceInMappingXmlForBeanException(fieldName, beanClass.getName());
/*     */     }
/*     */     
/* 103 */     alreadyProcessedFieldNames.add(fieldName);
/*     */     
/*     */ 
/* 106 */     Field field = (Field)run(GetDeclaredField.action(beanClass, fieldName));
/* 107 */     if (field == null) {
/* 108 */       throw log.getBeanDoesNotContainTheFieldException(beanClass.getName(), fieldName);
/*     */     }
/* 110 */     return field;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 120 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\ConstrainedFieldBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */