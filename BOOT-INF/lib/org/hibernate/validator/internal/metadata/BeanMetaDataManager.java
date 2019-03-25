/*     */ package org.hibernate.validator.internal.metadata;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.EnumSet;
/*     */ import java.util.List;
/*     */ import javax.validation.ParameterNameProvider;
/*     */ import org.hibernate.validator.internal.engine.DefaultParameterNameProvider;
/*     */ import org.hibernate.validator.internal.engine.MethodValidationConfiguration;
/*     */ import org.hibernate.validator.internal.engine.groups.ValidationOrderGenerator;
/*     */ import org.hibernate.validator.internal.metadata.aggregated.BeanMetaData;
/*     */ import org.hibernate.validator.internal.metadata.aggregated.BeanMetaDataImpl;
/*     */ import org.hibernate.validator.internal.metadata.aggregated.BeanMetaDataImpl.BeanMetaDataBuilder;
/*     */ import org.hibernate.validator.internal.metadata.aggregated.UnconstrainedEntityMetaDataSingleton;
/*     */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptions;
/*     */ import org.hibernate.validator.internal.metadata.core.AnnotationProcessingOptionsImpl;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.provider.AnnotationMetaDataProvider;
/*     */ import org.hibernate.validator.internal.metadata.provider.MetaDataProvider;
/*     */ import org.hibernate.validator.internal.metadata.provider.TypeAnnotationAwareMetaDataProvider;
/*     */ import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap;
/*     */ import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap.Option;
/*     */ import org.hibernate.validator.internal.util.ConcurrentReferenceHashMap.ReferenceType;
/*     */ import org.hibernate.validator.internal.util.Contracts;
/*     */ import org.hibernate.validator.internal.util.ExecutableHelper;
/*     */ import org.hibernate.validator.internal.util.Version;
/*     */ import org.hibernate.validator.internal.util.logging.Messages;
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
/*     */ public class BeanMetaDataManager
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 16;
/*     */   private static final float DEFAULT_LOAD_FACTOR = 0.75F;
/*     */   private static final int DEFAULT_CONCURRENCY_LEVEL = 16;
/*     */   private final List<MetaDataProvider> metaDataProviders;
/*     */   private final ConstraintHelper constraintHelper;
/*     */   private final ConcurrentReferenceHashMap<Class<?>, BeanMetaData<?>> beanMetaDataCache;
/*     */   private final ExecutableHelper executableHelper;
/*  94 */   private final ValidationOrderGenerator validationOrderGenerator = new ValidationOrderGenerator();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final MethodValidationConfiguration methodValidationConfiguration;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanMetaDataManager(ConstraintHelper constraintHelper, ExecutableHelper executableHelper)
/*     */   {
/* 111 */     this(constraintHelper, executableHelper, new DefaultParameterNameProvider(), Collections.emptyList());
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
/*     */   public BeanMetaDataManager(ConstraintHelper constraintHelper, ExecutableHelper executableHelper, ParameterNameProvider parameterNameProvider, List<MetaDataProvider> optionalMetaDataProviders)
/*     */   {
/* 126 */     this(constraintHelper, executableHelper, parameterNameProvider, optionalMetaDataProviders, new MethodValidationConfiguration());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanMetaDataManager(ConstraintHelper constraintHelper, ExecutableHelper executableHelper, ParameterNameProvider parameterNameProvider, List<MetaDataProvider> optionalMetaDataProviders, MethodValidationConfiguration methodValidationConfiguration)
/*     */   {
/* 138 */     this.constraintHelper = constraintHelper;
/* 139 */     this.metaDataProviders = CollectionHelper.newArrayList();
/* 140 */     this.metaDataProviders.addAll(optionalMetaDataProviders);
/* 141 */     this.executableHelper = executableHelper;
/*     */     
/* 143 */     this.methodValidationConfiguration = methodValidationConfiguration;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 151 */     this.beanMetaDataCache = new ConcurrentReferenceHashMap(16, 0.75F, 16, ConcurrentReferenceHashMap.ReferenceType.SOFT, ConcurrentReferenceHashMap.ReferenceType.SOFT, EnumSet.of(ConcurrentReferenceHashMap.Option.IDENTITY_COMPARISONS));
/*     */     
/*     */ 
/* 154 */     AnnotationProcessingOptions annotationProcessingOptions = getAnnotationProcessingOptionsFromNonDefaultProviders();
/*     */     AnnotationMetaDataProvider defaultProvider;
/* 156 */     AnnotationMetaDataProvider defaultProvider; if (Version.getJavaRelease() >= 8) {
/* 157 */       defaultProvider = new TypeAnnotationAwareMetaDataProvider(constraintHelper, parameterNameProvider, annotationProcessingOptions);
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 164 */       defaultProvider = new AnnotationMetaDataProvider(constraintHelper, parameterNameProvider, annotationProcessingOptions);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 170 */     this.metaDataProviders.add(defaultProvider);
/*     */   }
/*     */   
/*     */   public boolean isConstrained(Class<?> beanClass) {
/* 174 */     return getOrCreateBeanMetaData(beanClass, true).hasConstraints();
/*     */   }
/*     */   
/*     */   public <T> BeanMetaData<T> getBeanMetaData(Class<T> beanClass) {
/* 178 */     return getOrCreateBeanMetaData(beanClass, false);
/*     */   }
/*     */   
/*     */   public void clear() {
/* 182 */     this.beanMetaDataCache.clear();
/*     */   }
/*     */   
/*     */   public int numberOfCachedBeanMetaDataInstances() {
/* 186 */     return this.beanMetaDataCache.size();
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
/*     */   private <T> BeanMetaDataImpl<T> createBeanMetaData(Class<T> clazz)
/*     */   {
/* 199 */     BeanMetaDataImpl.BeanMetaDataBuilder<T> builder = BeanMetaDataImpl.BeanMetaDataBuilder.getInstance(this.constraintHelper, this.executableHelper, this.validationOrderGenerator, clazz, this.methodValidationConfiguration);
/*     */     
/*     */ 
/* 202 */     for (MetaDataProvider provider : this.metaDataProviders) {
/* 203 */       for (BeanConfiguration<? super T> beanConfiguration : provider.getBeanConfigurationForHierarchy(clazz)) {
/* 204 */         builder.add(beanConfiguration);
/*     */       }
/*     */     }
/*     */     
/* 208 */     return builder.build();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private AnnotationProcessingOptions getAnnotationProcessingOptionsFromNonDefaultProviders()
/*     */   {
/* 215 */     AnnotationProcessingOptions options = new AnnotationProcessingOptionsImpl();
/* 216 */     for (MetaDataProvider metaDataProvider : this.metaDataProviders) {
/* 217 */       options.merge(metaDataProvider.getAnnotationProcessingOptions());
/*     */     }
/*     */     
/* 220 */     return options;
/*     */   }
/*     */   
/*     */   private <T> BeanMetaData<T> getOrCreateBeanMetaData(Class<T> beanClass, boolean allowUnconstrainedTypeSingleton)
/*     */   {
/* 225 */     Contracts.assertNotNull(beanClass, Messages.MESSAGES.beanTypeCannotBeNull());
/*     */     
/* 227 */     BeanMetaData<T> beanMetaData = (BeanMetaData)this.beanMetaDataCache.get(beanClass);
/*     */     
/*     */ 
/* 230 */     if (beanMetaData == null) {
/* 231 */       beanMetaData = createBeanMetaData(beanClass);
/* 232 */       if ((!beanMetaData.hasConstraints()) && (allowUnconstrainedTypeSingleton)) {
/* 233 */         beanMetaData = UnconstrainedEntityMetaDataSingleton.getSingleton();
/*     */       }
/*     */       
/* 236 */       BeanMetaData<T> cachedBeanMetaData = (BeanMetaData)this.beanMetaDataCache.putIfAbsent(beanClass, beanMetaData);
/*     */       
/*     */ 
/*     */ 
/* 240 */       if (cachedBeanMetaData != null) {
/* 241 */         beanMetaData = cachedBeanMetaData;
/*     */       }
/*     */     }
/*     */     
/* 245 */     if (((beanMetaData instanceof UnconstrainedEntityMetaDataSingleton)) && (!allowUnconstrainedTypeSingleton)) {
/* 246 */       beanMetaData = createBeanMetaData(beanClass);
/* 247 */       this.beanMetaDataCache.put(beanClass, beanMetaData);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 253 */     return beanMetaData;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\BeanMetaDataManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */