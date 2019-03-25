/*    */ package org.hibernate.validator.internal.metadata.provider;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*    */ import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
/*    */ import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
/*    */ import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
/*    */ import org.hibernate.validator.internal.util.CollectionHelper;
/*    */ import org.hibernate.validator.internal.util.Contracts;
/*    */ import org.hibernate.validator.internal.util.classhierarchy.ClassHierarchyHelper;
/*    */ import org.hibernate.validator.internal.util.classhierarchy.Filter;
/*    */ import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
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
/*    */ public abstract class MetaDataProviderKeyedByClassName
/*    */   implements MetaDataProvider
/*    */ {
/*    */   protected final ConstraintHelper constraintHelper;
/*    */   private final Map<String, BeanConfiguration<?>> configuredBeans;
/*    */   
/*    */   public MetaDataProviderKeyedByClassName(ConstraintHelper constraintHelper, Map<String, BeanConfiguration<?>> configuredBeans)
/*    */   {
/* 36 */     this.constraintHelper = constraintHelper;
/* 37 */     this.configuredBeans = Collections.unmodifiableMap(configuredBeans);
/*    */   }
/*    */   
/*    */   public <T> List<BeanConfiguration<? super T>> getBeanConfigurationForHierarchy(Class<T> beanClass)
/*    */   {
/* 42 */     List<BeanConfiguration<? super T>> configurations = CollectionHelper.newArrayList();
/*    */     
/* 44 */     for (Class<? super T> clazz : ClassHierarchyHelper.getHierarchy(beanClass, new Filter[0])) {
/* 45 */       BeanConfiguration<? super T> configuration = getBeanConfiguration(clazz);
/* 46 */       if (configuration != null) {
/* 47 */         configurations.add(configuration);
/*    */       }
/*    */     }
/*    */     
/* 51 */     return configurations;
/*    */   }
/*    */   
/*    */   protected <T> BeanConfiguration<T> getBeanConfiguration(Class<T> beanClass)
/*    */   {
/* 56 */     Contracts.assertNotNull(beanClass);
/* 57 */     return (BeanConfiguration)this.configuredBeans.get(beanClass.getName());
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected static <T> BeanConfiguration<T> createBeanConfiguration(ConfigurationSource source, Class<T> beanClass, Set<? extends ConstrainedElement> constrainedElements, List<Class<?>> defaultGroupSequence, DefaultGroupSequenceProvider<? super T> defaultGroupSequenceProvider)
/*    */   {
/* 65 */     return new BeanConfiguration(source, beanClass, constrainedElements, defaultGroupSequence, defaultGroupSequenceProvider);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\provider\MetaDataProviderKeyedByClassName.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */