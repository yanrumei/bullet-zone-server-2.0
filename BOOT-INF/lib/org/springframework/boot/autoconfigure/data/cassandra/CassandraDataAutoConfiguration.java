/*     */ package org.springframework.boot.autoconfigure.data.cassandra;
/*     */ 
/*     */ import com.datastax.driver.core.Cluster;
/*     */ import com.datastax.driver.core.Session;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.domain.EntityScanPackages;
/*     */ import org.springframework.boot.bind.RelaxedPropertyResolver;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.PropertyResolver;
/*     */ import org.springframework.data.cassandra.config.CassandraEntityClassScanner;
/*     */ import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
/*     */ import org.springframework.data.cassandra.config.SchemaAction;
/*     */ import org.springframework.data.cassandra.convert.CassandraConverter;
/*     */ import org.springframework.data.cassandra.convert.MappingCassandraConverter;
/*     */ import org.springframework.data.cassandra.core.CassandraAdminOperations;
/*     */ import org.springframework.data.cassandra.core.CassandraTemplate;
/*     */ import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
/*     */ import org.springframework.data.cassandra.mapping.CassandraMappingContext;
/*     */ import org.springframework.data.cassandra.mapping.SimpleUserTypeResolver;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ @Configuration
/*     */ @ConditionalOnClass({Cluster.class, CassandraAdminOperations.class})
/*     */ @EnableConfigurationProperties({CassandraProperties.class})
/*     */ @AutoConfigureAfter({CassandraAutoConfiguration.class})
/*     */ public class CassandraDataAutoConfiguration
/*     */ {
/*     */   private final BeanFactory beanFactory;
/*     */   private final CassandraProperties properties;
/*     */   private final Cluster cluster;
/*     */   private final PropertyResolver propertyResolver;
/*     */   
/*     */   public CassandraDataAutoConfiguration(BeanFactory beanFactory, CassandraProperties properties, Cluster cluster, Environment environment)
/*     */   {
/*  75 */     this.beanFactory = beanFactory;
/*  76 */     this.properties = properties;
/*  77 */     this.cluster = cluster;
/*  78 */     this.propertyResolver = new RelaxedPropertyResolver(environment, "spring.data.cassandra.");
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public CassandraMappingContext cassandraMapping() throws ClassNotFoundException
/*     */   {
/*  85 */     BasicCassandraMappingContext context = new BasicCassandraMappingContext();
/*     */     
/*  87 */     List<String> packages = EntityScanPackages.get(this.beanFactory).getPackageNames();
/*  88 */     if ((packages.isEmpty()) && (AutoConfigurationPackages.has(this.beanFactory))) {
/*  89 */       packages = AutoConfigurationPackages.get(this.beanFactory);
/*     */     }
/*  91 */     if (!packages.isEmpty()) {
/*  92 */       context.setInitialEntitySet(CassandraEntityClassScanner.scan(packages));
/*     */     }
/*  94 */     if (StringUtils.hasText(this.properties.getKeyspaceName())) {
/*  95 */       context.setUserTypeResolver(new SimpleUserTypeResolver(this.cluster, this.properties
/*  96 */         .getKeyspaceName()));
/*     */     }
/*  98 */     return context;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public CassandraConverter cassandraConverter(CassandraMappingContext mapping) {
/* 104 */     return new MappingCassandraConverter(mapping);
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({Session.class})
/*     */   public CassandraSessionFactoryBean session(CassandraConverter converter) throws Exception
/*     */   {
/* 111 */     CassandraSessionFactoryBean session = new CassandraSessionFactoryBean();
/* 112 */     session.setCluster(this.cluster);
/* 113 */     session.setConverter(converter);
/* 114 */     session.setKeyspaceName(this.properties.getKeyspaceName());
/* 115 */     String name = this.propertyResolver.getProperty("schemaAction", SchemaAction.NONE
/* 116 */       .name());
/* 117 */     SchemaAction schemaAction = SchemaAction.valueOf(name.toUpperCase());
/* 118 */     session.setSchemaAction(schemaAction);
/* 119 */     return session;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public CassandraTemplate cassandraTemplate(Session session, CassandraConverter converter) throws Exception
/*     */   {
/* 126 */     return new CassandraTemplate(session, converter);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\cassandra\CassandraDataAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */