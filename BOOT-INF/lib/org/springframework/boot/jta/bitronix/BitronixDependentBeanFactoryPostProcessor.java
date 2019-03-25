/*    */ package org.springframework.boot.jta.bitronix;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BitronixDependentBeanFactoryPostProcessor
/*    */   implements BeanFactoryPostProcessor, Ordered
/*    */ {
/* 39 */   private static final String[] NO_BEANS = new String[0];
/*    */   
/* 41 */   private int order = Integer.MAX_VALUE;
/*    */   
/*    */ 
/*    */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*    */     throws BeansException
/*    */   {
/* 47 */     String[] transactionManagers = beanFactory.getBeanNamesForType(TransactionManager.class, true, false);
/* 48 */     for (String transactionManager : transactionManagers) {
/* 49 */       addTransactionManagerDependencies(beanFactory, transactionManager);
/*    */     }
/*    */   }
/*    */   
/*    */   private void addTransactionManagerDependencies(ConfigurableListableBeanFactory beanFactory, String transactionManager)
/*    */   {
/* 55 */     for (String dependentBeanName : getBeanNamesForType(beanFactory, "javax.jms.ConnectionFactory"))
/*    */     {
/* 57 */       beanFactory.registerDependentBean(transactionManager, dependentBeanName);
/*    */     }
/* 59 */     for (String dependentBeanName : getBeanNamesForType(beanFactory, "javax.sql.DataSource"))
/*    */     {
/* 61 */       beanFactory.registerDependentBean(transactionManager, dependentBeanName);
/*    */     }
/*    */   }
/*    */   
/*    */   private String[] getBeanNamesForType(ConfigurableListableBeanFactory beanFactory, String type)
/*    */   {
/*    */     try {
/* 68 */       return beanFactory.getBeanNamesForType(Class.forName(type), true, false);
/*    */     }
/*    */     catch (ClassNotFoundException localClassNotFoundException) {}catch (NoClassDefFoundError localNoClassDefFoundError) {}
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 76 */     return NO_BEANS;
/*    */   }
/*    */   
/*    */   public int getOrder()
/*    */   {
/* 81 */     return this.order;
/*    */   }
/*    */   
/*    */   public void setOrder(int order) {
/* 85 */     this.order = order;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\bitronix\BitronixDependentBeanFactoryPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */