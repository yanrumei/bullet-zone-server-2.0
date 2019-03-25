/*     */ package org.springframework.boot.autoconfigure.data.neo4j;
/*     */ 
/*     */ import java.util.List;
/*     */ import org.neo4j.ogm.session.SessionFactory;
/*     */ import org.neo4j.ogm.session.event.EventListener;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
/*     */ import org.springframework.boot.autoconfigure.transaction.TransactionManagerCustomizers;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.data.neo4j.template.Neo4jOperations;
/*     */ import org.springframework.data.neo4j.template.Neo4jTemplate;
/*     */ import org.springframework.data.neo4j.transaction.Neo4jTransactionManager;
/*     */ import org.springframework.data.neo4j.web.support.OpenSessionInViewInterceptor;
/*     */ import org.springframework.transaction.PlatformTransactionManager;
/*     */ import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
/*     */ import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
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
/*     */ @org.springframework.context.annotation.Configuration
/*     */ @ConditionalOnClass({SessionFactory.class, Neo4jTransactionManager.class, PlatformTransactionManager.class})
/*     */ @ConditionalOnMissingBean({SessionFactory.class})
/*     */ @EnableConfigurationProperties({Neo4jProperties.class})
/*     */ public class Neo4jDataAutoConfiguration
/*     */ {
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public org.neo4j.ogm.config.Configuration configuration(Neo4jProperties properties)
/*     */   {
/*  66 */     return properties.createConfiguration();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Bean
/*     */   public SessionFactory sessionFactory(org.neo4j.ogm.config.Configuration configuration, ApplicationContext applicationContext, ObjectProvider<List<EventListener>> eventListeners)
/*     */   {
/*  74 */     SessionFactory sessionFactory = new SessionFactory(configuration, getPackagesToScan(applicationContext));
/*  75 */     List<EventListener> providedEventListeners = (List)eventListeners.getIfAvailable();
/*  76 */     if (providedEventListeners != null) {
/*  77 */       for (EventListener eventListener : providedEventListeners) {
/*  78 */         sessionFactory.register(eventListener);
/*     */       }
/*     */     }
/*  81 */     return sessionFactory;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({Neo4jOperations.class})
/*     */   public Neo4jTemplate neo4jTemplate(SessionFactory sessionFactory) {
/*  87 */     return new Neo4jTemplate(sessionFactory);
/*     */   }
/*     */   
/*     */ 
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({PlatformTransactionManager.class})
/*     */   public Neo4jTransactionManager transactionManager(SessionFactory sessionFactory, Neo4jProperties properties, ObjectProvider<TransactionManagerCustomizers> transactionManagerCustomizers)
/*     */   {
/*  95 */     return customize(new Neo4jTransactionManager(sessionFactory), 
/*  96 */       (TransactionManagerCustomizers)transactionManagerCustomizers.getIfAvailable());
/*     */   }
/*     */   
/*     */   private Neo4jTransactionManager customize(Neo4jTransactionManager transactionManager, TransactionManagerCustomizers customizers)
/*     */   {
/* 101 */     if (customizers != null) {
/* 102 */       customizers.customize(transactionManager);
/*     */     }
/* 104 */     return transactionManager;
/*     */   }
/*     */   
/*     */   private String[] getPackagesToScan(ApplicationContext applicationContext)
/*     */   {
/* 109 */     List<String> packages = EntityScanPackages.get(applicationContext).getPackageNames();
/* 110 */     if ((packages.isEmpty()) && (AutoConfigurationPackages.has(applicationContext))) {
/* 111 */       packages = AutoConfigurationPackages.get(applicationContext);
/*     */     }
/* 113 */     return (String[])packages.toArray(new String[packages.size()]);
/*     */   }
/*     */   
/*     */   @org.springframework.context.annotation.Configuration
/*     */   @ConditionalOnWebApplication
/*     */   @ConditionalOnClass({WebMvcConfigurerAdapter.class, OpenSessionInViewInterceptor.class})
/*     */   @ConditionalOnMissingBean({OpenSessionInViewInterceptor.class})
/*     */   @ConditionalOnProperty(prefix="spring.data.neo4j", name={"open-in-view"}, havingValue="true", matchIfMissing=true)
/*     */   protected static class Neo4jWebConfiguration
/*     */   {
/*     */     @org.springframework.context.annotation.Configuration
/*     */     protected static class Neo4jWebMvcConfiguration extends WebMvcConfigurerAdapter
/*     */     {
/*     */       @Bean
/*     */       public OpenSessionInViewInterceptor neo4jOpenSessionInViewInterceptor()
/*     */       {
/* 129 */         return new OpenSessionInViewInterceptor();
/*     */       }
/*     */       
/*     */       public void addInterceptors(InterceptorRegistry registry)
/*     */       {
/* 134 */         registry.addWebRequestInterceptor(neo4jOpenSessionInViewInterceptor());
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\neo4j\Neo4jDataAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */