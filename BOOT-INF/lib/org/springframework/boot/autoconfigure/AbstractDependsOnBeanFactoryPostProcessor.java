/*    */ package org.springframework.boot.autoconfigure;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryUtils;
/*    */ import org.springframework.beans.factory.FactoryBean;
/*    */ import org.springframework.beans.factory.ListableBeanFactory;
/*    */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public abstract class AbstractDependsOnBeanFactoryPostProcessor
/*    */   implements BeanFactoryPostProcessor
/*    */ {
/*    */   private final Class<?> beanClass;
/*    */   private final Class<? extends FactoryBean<?>> factoryBeanClass;
/*    */   private final String[] dependsOn;
/*    */   
/*    */   protected AbstractDependsOnBeanFactoryPostProcessor(Class<?> beanClass, Class<? extends FactoryBean<?>> factoryBeanClass, String... dependsOn)
/*    */   {
/* 56 */     this.beanClass = beanClass;
/* 57 */     this.factoryBeanClass = factoryBeanClass;
/* 58 */     this.dependsOn = dependsOn;
/*    */   }
/*    */   
/*    */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*    */   {
/* 63 */     for (String beanName : getBeanNames(beanFactory)) {
/* 64 */       BeanDefinition definition = getBeanDefinition(beanName, beanFactory);
/* 65 */       String[] dependencies = definition.getDependsOn();
/* 66 */       for (String bean : this.dependsOn) {
/* 67 */         dependencies = StringUtils.addStringToArray(dependencies, bean);
/*    */       }
/* 69 */       definition.setDependsOn(dependencies);
/*    */     }
/*    */   }
/*    */   
/*    */   private Iterable<String> getBeanNames(ListableBeanFactory beanFactory) {
/* 74 */     Set<String> names = new HashSet();
/* 75 */     names.addAll(Arrays.asList(BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, this.beanClass, true, false)));
/*    */     
/* 77 */     for (String factoryBeanName : BeanFactoryUtils.beanNamesForTypeIncludingAncestors(beanFactory, this.factoryBeanClass, true, false))
/*    */     {
/* 79 */       names.add(BeanFactoryUtils.transformedBeanName(factoryBeanName));
/*    */     }
/* 81 */     return names;
/*    */   }
/*    */   
/*    */   private static BeanDefinition getBeanDefinition(String beanName, ConfigurableListableBeanFactory beanFactory)
/*    */   {
/*    */     try {
/* 87 */       return beanFactory.getBeanDefinition(beanName);
/*    */     }
/*    */     catch (NoSuchBeanDefinitionException ex) {
/* 90 */       BeanFactory parentBeanFactory = beanFactory.getParentBeanFactory();
/* 91 */       if ((parentBeanFactory instanceof ConfigurableListableBeanFactory)) {
/* 92 */         return getBeanDefinition(beanName, (ConfigurableListableBeanFactory)parentBeanFactory);
/*    */       }
/*    */       
/* 95 */       throw ex;
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\AbstractDependsOnBeanFactoryPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */