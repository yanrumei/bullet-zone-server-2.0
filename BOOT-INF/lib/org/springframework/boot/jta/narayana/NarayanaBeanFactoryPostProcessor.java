/*    */ package org.springframework.boot.jta.narayana;
/*    */ 
/*    */ import javax.transaction.TransactionManager;
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*    */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*    */ import org.springframework.core.Ordered;
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
/*    */ public class NarayanaBeanFactoryPostProcessor
/*    */   implements BeanFactoryPostProcessor, Ordered
/*    */ {
/* 35 */   private static final String[] NO_BEANS = new String[0];
/*    */   
/*    */   private static final int ORDER = Integer.MAX_VALUE;
/*    */   
/*    */ 
/*    */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*    */     throws BeansException
/*    */   {
/* 43 */     String[] transactionManagers = beanFactory.getBeanNamesForType(TransactionManager.class, true, false);
/*    */     
/* 45 */     String[] recoveryManagers = beanFactory.getBeanNamesForType(NarayanaRecoveryManagerBean.class, true, false);
/* 46 */     addBeanDependencies(beanFactory, transactionManagers, "javax.sql.DataSource");
/* 47 */     addBeanDependencies(beanFactory, recoveryManagers, "javax.sql.DataSource");
/* 48 */     addBeanDependencies(beanFactory, transactionManagers, "javax.jms.ConnectionFactory");
/*    */     
/* 50 */     addBeanDependencies(beanFactory, recoveryManagers, "javax.jms.ConnectionFactory");
/*    */   }
/*    */   
/*    */   private void addBeanDependencies(ConfigurableListableBeanFactory beanFactory, String[] beanNames, String dependencyType)
/*    */   {
/* 55 */     for (String beanName : beanNames) {
/* 56 */       addBeanDependencies(beanFactory, beanName, dependencyType);
/*    */     }
/*    */   }
/*    */   
/*    */   private void addBeanDependencies(ConfigurableListableBeanFactory beanFactory, String beanName, String dependencyType)
/*    */   {
/* 62 */     for (String dependentBeanName : getBeanNamesForType(beanFactory, dependencyType))
/*    */     {
/* 64 */       beanFactory.registerDependentBean(beanName, dependentBeanName);
/*    */     }
/*    */   }
/*    */   
/*    */   private String[] getBeanNamesForType(ConfigurableListableBeanFactory beanFactory, String type)
/*    */   {
/*    */     try {
/* 71 */       return beanFactory.getBeanNamesForType(Class.forName(type), true, false);
/*    */     }
/*    */     catch (ClassNotFoundException localClassNotFoundException) {}catch (NoClassDefFoundError localNoClassDefFoundError) {}
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 79 */     return NO_BEANS;
/*    */   }
/*    */   
/*    */   public int getOrder()
/*    */   {
/* 84 */     return Integer.MAX_VALUE;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\narayana\NarayanaBeanFactoryPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */