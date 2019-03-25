/*    */ package org.hibernate.validator.internal.xml;
/*    */ 
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
/*    */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*    */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*    */ import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
/*    */ import org.hibernate.validator.internal.metadata.raw.ConstrainedType;
/*    */ import org.hibernate.validator.internal.util.CollectionHelper;
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
/*    */ class ConstrainedTypeBuilder
/*    */ {
/*    */   private final ClassLoadingHelper classLoadingHelper;
/*    */   private final MetaConstraintBuilder metaConstraintBuilder;
/*    */   private final AnnotationProcessingOptionsImpl annotationProcessingOptions;
/*    */   private final Map<Class<?>, List<Class<?>>> defaultSequences;
/*    */   
/*    */   public ConstrainedTypeBuilder(ClassLoadingHelper classLoadingHelper, MetaConstraintBuilder metaConstraintBuilder, AnnotationProcessingOptionsImpl annotationProcessingOptions, Map<Class<?>, List<Class<?>>> defaultSequences)
/*    */   {
/* 38 */     this.classLoadingHelper = classLoadingHelper;
/* 39 */     this.metaConstraintBuilder = metaConstraintBuilder;
/* 40 */     this.annotationProcessingOptions = annotationProcessingOptions;
/* 41 */     this.defaultSequences = defaultSequences;
/*    */   }
/*    */   
/*    */   ConstrainedType buildConstrainedType(ClassType classType, Class<?> beanClass, String defaultPackage) {
/* 45 */     if (classType == null) {
/* 46 */       return null;
/*    */     }
/*    */     
/*    */ 
/* 50 */     List<Class<?>> groupSequence = createGroupSequence(classType.getGroupSequence(), defaultPackage);
/* 51 */     if (!groupSequence.isEmpty()) {
/* 52 */       this.defaultSequences.put(beanClass, groupSequence);
/*    */     }
/*    */     
/*    */ 
/* 56 */     ConstraintLocation constraintLocation = ConstraintLocation.forClass(beanClass);
/* 57 */     Set<MetaConstraint<?>> metaConstraints = CollectionHelper.newHashSet();
/* 58 */     for (ConstraintType constraint : classType.getConstraint()) {
/* 59 */       MetaConstraint<?> metaConstraint = this.metaConstraintBuilder.buildMetaConstraint(constraintLocation, constraint, ElementType.TYPE, defaultPackage, null);
/*    */       
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 66 */       metaConstraints.add(metaConstraint);
/*    */     }
/*    */     
/*    */ 
/* 70 */     if (classType.getIgnoreAnnotations() != null) {
/* 71 */       this.annotationProcessingOptions.ignoreClassLevelConstraintAnnotations(beanClass, classType
/*    */       
/* 73 */         .getIgnoreAnnotations().booleanValue());
/*    */     }
/*    */     
/*    */ 
/* 77 */     return new ConstrainedType(ConfigurationSource.XML, constraintLocation, metaConstraints);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private List<Class<?>> createGroupSequence(GroupSequenceType groupSequenceType, String defaultPackage)
/*    */   {
/* 85 */     List<Class<?>> groupSequence = CollectionHelper.newArrayList();
/* 86 */     if (groupSequenceType != null) {
/* 87 */       for (String groupName : groupSequenceType.getValue()) {
/* 88 */         Class<?> group = this.classLoadingHelper.loadClass(groupName, defaultPackage);
/* 89 */         groupSequence.add(group);
/*    */       }
/*    */     }
/* 92 */     return groupSequence;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\ConstrainedTypeBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */