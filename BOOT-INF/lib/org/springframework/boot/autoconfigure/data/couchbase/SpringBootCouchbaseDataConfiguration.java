/*    */ package org.springframework.boot.autoconfigure.data.couchbase;
/*    */ 
/*    */ import java.util.Set;
/*    */ import org.springframework.beans.factory.ObjectProvider;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.domain.EntityScanner;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.data.annotation.Persistent;
/*    */ import org.springframework.data.couchbase.config.AbstractCouchbaseDataConfiguration;
/*    */ import org.springframework.data.couchbase.config.CouchbaseConfigurer;
/*    */ import org.springframework.data.couchbase.core.CouchbaseTemplate;
/*    */ import org.springframework.data.couchbase.core.convert.CustomConversions;
/*    */ import org.springframework.data.couchbase.core.mapping.Document;
/*    */ import org.springframework.data.couchbase.core.query.Consistency;
/*    */ import org.springframework.data.couchbase.repository.support.IndexManager;
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
/*    */ @Configuration
/*    */ @ConditionalOnMissingBean({AbstractCouchbaseDataConfiguration.class})
/*    */ @ConditionalOnBean({CouchbaseConfigurer.class})
/*    */ class SpringBootCouchbaseDataConfiguration
/*    */   extends AbstractCouchbaseDataConfiguration
/*    */ {
/*    */   private final ApplicationContext applicationContext;
/*    */   private final CouchbaseDataProperties properties;
/*    */   private final CouchbaseConfigurer couchbaseConfigurer;
/*    */   
/*    */   SpringBootCouchbaseDataConfiguration(ApplicationContext applicationContext, CouchbaseDataProperties properties, ObjectProvider<CouchbaseConfigurer> couchbaseConfigurer)
/*    */   {
/* 57 */     this.applicationContext = applicationContext;
/* 58 */     this.properties = properties;
/* 59 */     this.couchbaseConfigurer = ((CouchbaseConfigurer)couchbaseConfigurer.getIfAvailable());
/*    */   }
/*    */   
/*    */   protected CouchbaseConfigurer couchbaseConfigurer()
/*    */   {
/* 64 */     return this.couchbaseConfigurer;
/*    */   }
/*    */   
/*    */   protected Consistency getDefaultConsistency()
/*    */   {
/* 69 */     return this.properties.getConsistency();
/*    */   }
/*    */   
/*    */   protected Set<Class<?>> getInitialEntitySet() throws ClassNotFoundException
/*    */   {
/* 74 */     return new EntityScanner(this.applicationContext).scan(new Class[] { Document.class, Persistent.class });
/*    */   }
/*    */   
/*    */   @ConditionalOnMissingBean(name={"couchbaseTemplate"})
/*    */   @Bean(name={"couchbaseTemplate"})
/*    */   public CouchbaseTemplate couchbaseTemplate()
/*    */     throws Exception
/*    */   {
/* 82 */     return super.couchbaseTemplate();
/*    */   }
/*    */   
/*    */   @ConditionalOnMissingBean(name={"couchbaseCustomConversions"})
/*    */   @Bean(name={"couchbaseCustomConversions"})
/*    */   public CustomConversions customConversions()
/*    */   {
/* 89 */     return super.customConversions();
/*    */   }
/*    */   
/*    */   @ConditionalOnMissingBean(name={"couchbaseIndexManager"})
/*    */   @Bean(name={"couchbaseIndexManager"})
/*    */   public IndexManager indexManager()
/*    */   {
/* 96 */     if (this.properties.isAutoIndex()) {
/* 97 */       return new IndexManager(true, true, true);
/*    */     }
/* 99 */     return new IndexManager(false, false, false);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\couchbase\SpringBootCouchbaseDataConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */