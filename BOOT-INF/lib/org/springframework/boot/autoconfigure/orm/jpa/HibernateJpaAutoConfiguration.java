/*     */ package org.springframework.boot.autoconfigure.orm.jpa;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import javax.persistence.EntityManager;
/*     */ import javax.sql.DataSource;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Builder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.ItemsBuilder;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionMessage.Style;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionOutcome;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.SpringBootCondition;
/*     */ import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
/*     */ import org.springframework.boot.orm.jpa.hibernate.SpringJtaPlatform;
/*     */ import org.springframework.context.annotation.ConditionContext;
/*     */ import org.springframework.context.annotation.Conditional;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.core.annotation.Order;
/*     */ import org.springframework.core.type.AnnotatedTypeMetadata;
/*     */ import org.springframework.jndi.JndiLocatorDelegate;
/*     */ import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
/*     */ import org.springframework.orm.jpa.vendor.AbstractJpaVendorAdapter;
/*     */ import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
/*     */ import org.springframework.transaction.jta.JtaTransactionManager;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ @Configuration
/*     */ @ConditionalOnClass({LocalContainerEntityManagerFactoryBean.class, EntityManager.class})
/*     */ @Conditional({HibernateEntityManagerCondition.class})
/*     */ @AutoConfigureAfter({DataSourceAutoConfiguration.class})
/*     */ public class HibernateJpaAutoConfiguration
/*     */   extends JpaBaseConfiguration
/*     */ {
/*  69 */   private static final Log logger = LogFactory.getLog(HibernateJpaAutoConfiguration.class);
/*     */   
/*     */ 
/*     */ 
/*     */   private static final String JTA_PLATFORM = "hibernate.transaction.jta.platform";
/*     */   
/*     */ 
/*  76 */   private static final String[] NO_JTA_PLATFORM_CLASSES = { "org.hibernate.engine.transaction.jta.platform.internal.NoJtaPlatform", "org.hibernate.service.jta.platform.internal.NoJtaPlatform" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  84 */   private static final String[] WEBSPHERE_JTA_PLATFORM_CLASSES = { "org.hibernate.engine.transaction.jta.platform.internal.WebSphereExtendedJtaPlatform", "org.hibernate.service.jta.platform.internal.WebSphereExtendedJtaPlatform" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public HibernateJpaAutoConfiguration(DataSource dataSource, JpaProperties jpaProperties, ObjectProvider<JtaTransactionManager> jtaTransactionManager, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers)
/*     */   {
/*  92 */     super(dataSource, jpaProperties, jtaTransactionManager, transactionManagerCustomizers);
/*     */   }
/*     */   
/*     */ 
/*     */   protected AbstractJpaVendorAdapter createJpaVendorAdapter()
/*     */   {
/*  98 */     return new HibernateJpaVendorAdapter();
/*     */   }
/*     */   
/*     */   protected Map<String, Object> getVendorProperties()
/*     */   {
/* 103 */     Map<String, Object> vendorProperties = new LinkedHashMap();
/* 104 */     vendorProperties.putAll(getProperties().getHibernateProperties(getDataSource()));
/* 105 */     return vendorProperties;
/*     */   }
/*     */   
/*     */   protected void customizeVendorProperties(Map<String, Object> vendorProperties)
/*     */   {
/* 110 */     super.customizeVendorProperties(vendorProperties);
/* 111 */     if (!vendorProperties.containsKey("hibernate.transaction.jta.platform")) {
/* 112 */       configureJtaPlatform(vendorProperties);
/*     */     }
/*     */   }
/*     */   
/*     */   private void configureJtaPlatform(Map<String, Object> vendorProperties) throws LinkageError
/*     */   {
/* 118 */     JtaTransactionManager jtaTransactionManager = getJtaTransactionManager();
/* 119 */     if (jtaTransactionManager != null) {
/* 120 */       if (runningOnWebSphere())
/*     */       {
/*     */ 
/*     */ 
/* 124 */         configureWebSphereTransactionPlatform(vendorProperties);
/*     */       }
/*     */       else {
/* 127 */         configureSpringJtaPlatform(vendorProperties, jtaTransactionManager);
/*     */       }
/*     */     }
/*     */     else {
/* 131 */       vendorProperties.put("hibernate.transaction.jta.platform", getNoJtaPlatformManager());
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean runningOnWebSphere() {
/* 136 */     return ClassUtils.isPresent("com.ibm.websphere.jtaextensions.ExtendedJTATransaction", 
/*     */     
/* 138 */       getClass().getClassLoader());
/*     */   }
/*     */   
/*     */   private void configureWebSphereTransactionPlatform(Map<String, Object> vendorProperties)
/*     */   {
/* 143 */     vendorProperties.put("hibernate.transaction.jta.platform", getWebSphereJtaPlatformManager());
/*     */   }
/*     */   
/*     */   private Object getWebSphereJtaPlatformManager() {
/* 147 */     return getJtaPlatformManager(WEBSPHERE_JTA_PLATFORM_CLASSES);
/*     */   }
/*     */   
/*     */   private void configureSpringJtaPlatform(Map<String, Object> vendorProperties, JtaTransactionManager jtaTransactionManager)
/*     */   {
/*     */     try {
/* 153 */       vendorProperties.put("hibernate.transaction.jta.platform", new SpringJtaPlatform(jtaTransactionManager));
/*     */ 
/*     */     }
/*     */     catch (LinkageError ex)
/*     */     {
/*     */ 
/* 159 */       if (!isUsingJndi()) {
/* 160 */         throw new IllegalStateException("Unable to set Hibernate JTA platform, are you using the correct version of Hibernate?", ex);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 165 */       if (logger.isDebugEnabled()) {
/* 166 */         logger.debug("Unable to set Hibernate JTA platform : " + ex.getMessage());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isUsingJndi() {
/*     */     try {
/* 173 */       return JndiLocatorDelegate.isDefaultJndiEnvironmentAvailable();
/*     */     }
/*     */     catch (Error ex) {}
/* 176 */     return false;
/*     */   }
/*     */   
/*     */   private Object getNoJtaPlatformManager()
/*     */   {
/* 181 */     return getJtaPlatformManager(NO_JTA_PLATFORM_CLASSES);
/*     */   }
/*     */   
/*     */   private Object getJtaPlatformManager(String[] candidates) {
/* 185 */     for (String candidate : candidates) {
/*     */       try {
/* 187 */         return Class.forName(candidate).newInstance();
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */     
/*     */ 
/* 193 */     throw new IllegalStateException("Could not configure JTA platform");
/*     */   }
/*     */   
/*     */   @Order(-2147483628)
/*     */   static class HibernateEntityManagerCondition extends SpringBootCondition
/*     */   {
/* 199 */     private static String[] CLASS_NAMES = { "org.hibernate.ejb.HibernateEntityManager", "org.hibernate.jpa.HibernateEntityManager" };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata)
/*     */     {
/* 207 */       ConditionMessage.Builder message = ConditionMessage.forCondition("HibernateEntityManager", new Object[0]);
/* 208 */       for (String className : CLASS_NAMES) {
/* 209 */         if (ClassUtils.isPresent(className, context.getClassLoader())) {
/* 210 */           return 
/* 211 */             ConditionOutcome.match(message.found("class").items(ConditionMessage.Style.QUOTE, new Object[] { className }));
/*     */         }
/*     */       }
/* 214 */       return ConditionOutcome.noMatch(message.didNotFind("class", "classes")
/* 215 */         .items(ConditionMessage.Style.QUOTE, Arrays.asList(CLASS_NAMES)));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\orm\jpa\HibernateJpaAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */