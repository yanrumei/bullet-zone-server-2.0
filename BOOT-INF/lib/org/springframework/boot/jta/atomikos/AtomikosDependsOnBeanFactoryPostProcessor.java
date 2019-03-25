/*     */ package org.springframework.boot.jta.atomikos;
/*     */ 
/*     */ import com.atomikos.icatch.jta.UserTransactionManager;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.config.BeanDefinition;
/*     */ import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
/*     */ import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
/*     */ import org.springframework.core.Ordered;
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
/*     */ public class AtomikosDependsOnBeanFactoryPostProcessor
/*     */   implements BeanFactoryPostProcessor, Ordered
/*     */ {
/*  45 */   private static final String[] NO_BEANS = new String[0];
/*     */   
/*  47 */   private int order = Integer.MAX_VALUE;
/*     */   
/*     */ 
/*     */   public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
/*     */     throws BeansException
/*     */   {
/*  53 */     String[] transactionManagers = beanFactory.getBeanNamesForType(UserTransactionManager.class, true, false);
/*  54 */     for (String transactionManager : transactionManagers) {
/*  55 */       addTransactionManagerDependencies(beanFactory, transactionManager);
/*     */     }
/*  57 */     addMessageDrivenContainerDependencies(beanFactory, transactionManagers);
/*     */   }
/*     */   
/*     */   private void addTransactionManagerDependencies(ConfigurableListableBeanFactory beanFactory, String transactionManager)
/*     */   {
/*  62 */     BeanDefinition bean = beanFactory.getBeanDefinition(transactionManager);
/*  63 */     Set<String> dependsOn = new LinkedHashSet(asList(bean.getDependsOn()));
/*  64 */     int initialSize = dependsOn.size();
/*  65 */     addDependencies(beanFactory, "javax.jms.ConnectionFactory", dependsOn);
/*  66 */     addDependencies(beanFactory, "javax.sql.DataSource", dependsOn);
/*  67 */     if (dependsOn.size() != initialSize) {
/*  68 */       bean.setDependsOn((String[])dependsOn.toArray(new String[dependsOn.size()]));
/*     */     }
/*     */   }
/*     */   
/*     */   private void addMessageDrivenContainerDependencies(ConfigurableListableBeanFactory beanFactory, String[] transactionManagers)
/*     */   {
/*  74 */     String[] messageDrivenContainers = getBeanNamesForType(beanFactory, "com.atomikos.jms.extra.MessageDrivenContainer");
/*     */     
/*  76 */     for (String messageDrivenContainer : messageDrivenContainers) {
/*  77 */       BeanDefinition bean = beanFactory.getBeanDefinition(messageDrivenContainer);
/*     */       
/*  79 */       Set<String> dependsOn = new LinkedHashSet(asList(bean.getDependsOn()));
/*  80 */       dependsOn.addAll(asList(transactionManagers));
/*  81 */       bean.setDependsOn((String[])dependsOn.toArray(new String[dependsOn.size()]));
/*     */     }
/*     */   }
/*     */   
/*     */   private void addDependencies(ConfigurableListableBeanFactory beanFactory, String type, Set<String> dependsOn)
/*     */   {
/*  87 */     dependsOn.addAll(asList(getBeanNamesForType(beanFactory, type)));
/*     */   }
/*     */   
/*     */   private String[] getBeanNamesForType(ConfigurableListableBeanFactory beanFactory, String type)
/*     */   {
/*     */     try {
/*  93 */       return beanFactory.getBeanNamesForType(Class.forName(type), true, false);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {}catch (NoClassDefFoundError localNoClassDefFoundError) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 101 */     return NO_BEANS;
/*     */   }
/*     */   
/*     */   private List<String> asList(String[] array) {
/* 105 */     return array == null ? Collections.emptyList() : Arrays.asList(array);
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 110 */     return this.order;
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 114 */     this.order = order;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\atomikos\AtomikosDependsOnBeanFactoryPostProcessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */