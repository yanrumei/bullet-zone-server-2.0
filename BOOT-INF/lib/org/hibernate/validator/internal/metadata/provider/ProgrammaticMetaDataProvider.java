/*     */ package org.hibernate.validator.internal.metadata.provider;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import org.hibernate.validator.internal.cfg.context.DefaultConstraintMapping;
/*     */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptions;
/*     */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.Contracts;
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
/*     */ public class ProgrammaticMetaDataProvider
/*     */   extends MetaDataProviderKeyedByClassName
/*     */ {
/*  33 */   private static final Log log = ;
/*     */   
/*     */   private final AnnotationProcessingOptions annotationProcessingOptions;
/*     */   
/*     */ 
/*     */   public ProgrammaticMetaDataProvider(ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider, Set<DefaultConstraintMapping> constraintMappings)
/*     */   {
/*  40 */     super(constraintHelper, createBeanConfigurations(constraintMappings, constraintHelper, parameterNameProvider));
/*  41 */     Contracts.assertNotNull(constraintMappings);
/*     */     
/*  43 */     assertUniquenessOfConfiguredTypes(constraintMappings);
/*  44 */     this.annotationProcessingOptions = mergeAnnotationProcessingOptions(constraintMappings);
/*     */   }
/*     */   
/*     */   private void assertUniquenessOfConfiguredTypes(Set<DefaultConstraintMapping> mappings) {
/*  48 */     Set<Class<?>> allConfiguredTypes = CollectionHelper.newHashSet();
/*     */     
/*  50 */     for (DefaultConstraintMapping constraintMapping : mappings) {
/*  51 */       for (Class<?> configuredType : constraintMapping.getConfiguredTypes()) {
/*  52 */         if (allConfiguredTypes.contains(configuredType)) {
/*  53 */           throw log.getBeanClassHasAlreadyBeConfiguredViaProgrammaticApiException(configuredType.getName());
/*     */         }
/*     */       }
/*     */       
/*  57 */       allConfiguredTypes.addAll(constraintMapping.getConfiguredTypes());
/*     */     }
/*     */   }
/*     */   
/*     */   private static Map<String, BeanConfiguration<?>> createBeanConfigurations(Set<DefaultConstraintMapping> mappings, ConstraintHelper constraintHelper, ParameterNameProvider parameterNameProvider)
/*     */   {
/*  63 */     Map<String, BeanConfiguration<?>> configuredBeans = new HashMap();
/*  64 */     for (DefaultConstraintMapping mapping : mappings) {
/*  65 */       Set<BeanConfiguration<?>> beanConfigurations = mapping.getBeanConfigurations(constraintHelper, parameterNameProvider);
/*     */       
/*  67 */       for (BeanConfiguration<?> beanConfiguration : beanConfigurations) {
/*  68 */         configuredBeans.put(beanConfiguration.getBeanClass().getName(), beanConfiguration);
/*     */       }
/*     */     }
/*  71 */     return configuredBeans;
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
/*     */   private AnnotationProcessingOptions mergeAnnotationProcessingOptions(Set<DefaultConstraintMapping> mappings)
/*     */   {
/*  87 */     if (mappings.size() == 1) {
/*  88 */       return ((DefaultConstraintMapping)mappings.iterator().next()).getAnnotationProcessingOptions();
/*     */     }
/*     */     
/*  91 */     AnnotationProcessingOptions options = new AnnotationProcessingOptionsImpl();
/*     */     
/*  93 */     for (DefaultConstraintMapping mapping : mappings) {
/*  94 */       options.merge(mapping.getAnnotationProcessingOptions());
/*     */     }
/*     */     
/*  97 */     return options;
/*     */   }
/*     */   
/*     */   public AnnotationProcessingOptions getAnnotationProcessingOptions()
/*     */   {
/* 102 */     return this.annotationProcessingOptions;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\provider\ProgrammaticMetaDataProvider.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */