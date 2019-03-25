/*     */ package org.hibernate.validator.internal.xml;
/*     */ 
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.Method;
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
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.internal.util.privilegedactions.GetMethodFromPropertyName;
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
/*     */ class ConstrainedGetterBuilder
/*     */ {
/*  37 */   private static final Log log = ;
/*     */   
/*     */   private final GroupConversionBuilder groupConversionBuilder;
/*     */   private final MetaConstraintBuilder metaConstraintBuilder;
/*     */   private final AnnotationProcessingOptionsImpl annotationProcessingOptions;
/*     */   
/*     */   ConstrainedGetterBuilder(MetaConstraintBuilder metaConstraintBuilder, GroupConversionBuilder groupConversionBuilder, AnnotationProcessingOptionsImpl annotationProcessingOptions)
/*     */   {
/*  45 */     this.metaConstraintBuilder = metaConstraintBuilder;
/*  46 */     this.groupConversionBuilder = groupConversionBuilder;
/*  47 */     this.annotationProcessingOptions = annotationProcessingOptions;
/*     */   }
/*     */   
/*     */ 
/*     */   Set<ConstrainedExecutable> buildConstrainedGetters(List<GetterType> getterList, Class<?> beanClass, String defaultPackage)
/*     */   {
/*  53 */     Set<ConstrainedExecutable> constrainedExecutables = CollectionHelper.newHashSet();
/*  54 */     List<String> alreadyProcessedGetterNames = CollectionHelper.newArrayList();
/*  55 */     for (GetterType getterType : getterList) {
/*  56 */       String getterName = getterType.getName();
/*  57 */       Method getter = findGetter(beanClass, getterName, alreadyProcessedGetterNames);
/*  58 */       ConstraintLocation constraintLocation = ConstraintLocation.forProperty(getter);
/*     */       
/*  60 */       Set<MetaConstraint<?>> metaConstraints = CollectionHelper.newHashSet();
/*  61 */       for (ConstraintType constraint : getterType.getConstraint()) {
/*  62 */         MetaConstraint<?> metaConstraint = this.metaConstraintBuilder.buildMetaConstraint(constraintLocation, constraint, ElementType.METHOD, defaultPackage, null);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */         metaConstraints.add(metaConstraint);
/*     */       }
/*  71 */       Object groupConversions = this.groupConversionBuilder.buildGroupConversionMap(getterType
/*  72 */         .getConvertGroup(), defaultPackage);
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
/*  85 */       ConstrainedExecutable constrainedGetter = new ConstrainedExecutable(ConfigurationSource.XML, constraintLocation, Collections.emptyList(), Collections.emptySet(), metaConstraints, Collections.emptySet(), (Map)groupConversions, getterType.getValid() != null, UnwrapMode.AUTOMATIC);
/*     */       
/*     */ 
/*  88 */       constrainedExecutables.add(constrainedGetter);
/*     */       
/*     */ 
/*  91 */       if (getterType.getIgnoreAnnotations() != null) {
/*  92 */         this.annotationProcessingOptions.ignoreConstraintAnnotationsOnMember(getter, getterType
/*     */         
/*  94 */           .getIgnoreAnnotations());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  99 */     return constrainedExecutables;
/*     */   }
/*     */   
/*     */   private static Method findGetter(Class<?> beanClass, String getterName, List<String> alreadyProcessedGetterNames) {
/* 103 */     if (alreadyProcessedGetterNames.contains(getterName)) {
/* 104 */       throw log.getIsDefinedTwiceInMappingXmlForBeanException(getterName, beanClass.getName());
/*     */     }
/*     */     
/* 107 */     alreadyProcessedGetterNames.add(getterName);
/*     */     
/*     */ 
/* 110 */     Method method = (Method)run(GetMethodFromPropertyName.action(beanClass, getterName));
/* 111 */     if (method == null) {
/* 112 */       throw log.getBeanDoesNotContainThePropertyException(beanClass.getName(), getterName);
/*     */     }
/*     */     
/* 115 */     return method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static <T> T run(PrivilegedAction<T> action)
/*     */   {
/* 125 */     return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\xml\ConstrainedGetterBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */