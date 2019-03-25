/*     */ package org.springframework.boot.autoconfigure.data.mongo;
/*     */ 
/*     */ import com.mongodb.DB;
/*     */ import com.mongodb.MongoClient;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.Collections;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.domain.EntityScanner;
/*     */ import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.mongo.MongoProperties;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.annotation.Bean;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.dao.DataAccessException;
/*     */ import org.springframework.dao.support.PersistenceExceptionTranslator;
/*     */ import org.springframework.data.annotation.Persistent;
/*     */ import org.springframework.data.mapping.model.FieldNamingStrategy;
/*     */ import org.springframework.data.mongodb.MongoDbFactory;
/*     */ import org.springframework.data.mongodb.core.MongoTemplate;
/*     */ import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
/*     */ import org.springframework.data.mongodb.core.convert.CustomConversions;
/*     */ import org.springframework.data.mongodb.core.convert.DbRefResolver;
/*     */ import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
/*     */ import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
/*     */ import org.springframework.data.mongodb.core.convert.MongoConverter;
/*     */ import org.springframework.data.mongodb.core.mapping.Document;
/*     */ import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
/*     */ import org.springframework.data.mongodb.gridfs.GridFsTemplate;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Configuration
/*     */ @ConditionalOnClass({MongoClient.class, MongoTemplate.class})
/*     */ @EnableConfigurationProperties({MongoProperties.class})
/*     */ @AutoConfigureAfter({MongoAutoConfiguration.class})
/*     */ public class MongoDataAutoConfiguration
/*     */ {
/*     */   private final ApplicationContext applicationContext;
/*     */   private final MongoProperties properties;
/*     */   
/*     */   public MongoDataAutoConfiguration(ApplicationContext applicationContext, MongoProperties properties)
/*     */   {
/*  85 */     this.applicationContext = applicationContext;
/*  86 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({MongoDbFactory.class})
/*     */   public SimpleMongoDbFactory mongoDbFactory(MongoClient mongo) throws Exception {
/*  92 */     String database = this.properties.getMongoClientDatabase();
/*  93 */     return new SimpleMongoDbFactory(mongo, database);
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory, MongoConverter converter) throws UnknownHostException
/*     */   {
/* 100 */     return new MongoTemplate(mongoDbFactory, converter);
/*     */   }
/*     */   
/*     */ 
/*     */   @Bean
/*     */   @ConditionalOnMissingBean({MongoConverter.class})
/*     */   public MappingMongoConverter mappingMongoConverter(MongoDbFactory factory, MongoMappingContext context, BeanFactory beanFactory, CustomConversions conversions)
/*     */   {
/* 108 */     DbRefResolver dbRefResolver = new DefaultDbRefResolver(factory);
/* 109 */     MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, context);
/*     */     
/* 111 */     mappingConverter.setCustomConversions(conversions);
/* 112 */     return mappingConverter;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public MongoMappingContext mongoMappingContext(BeanFactory beanFactory, CustomConversions conversions) throws ClassNotFoundException
/*     */   {
/* 119 */     MongoMappingContext context = new MongoMappingContext();
/* 120 */     context.setInitialEntitySet(new EntityScanner(this.applicationContext)
/* 121 */       .scan(new Class[] { Document.class, Persistent.class }));
/* 122 */     Class<?> strategyClass = this.properties.getFieldNamingStrategy();
/* 123 */     if (strategyClass != null) {
/* 124 */       context.setFieldNamingStrategy(
/* 125 */         (FieldNamingStrategy)BeanUtils.instantiate(strategyClass));
/*     */     }
/* 127 */     context.setSimpleTypeHolder(conversions.getSimpleTypeHolder());
/* 128 */     return context;
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public GridFsTemplate gridFsTemplate(MongoDbFactory mongoDbFactory, MongoTemplate mongoTemplate)
/*     */   {
/* 135 */     return new GridFsTemplate(new GridFsMongoDbFactory(mongoDbFactory, this.properties), mongoTemplate
/*     */     
/* 137 */       .getConverter());
/*     */   }
/*     */   
/*     */   @Bean
/*     */   @ConditionalOnMissingBean
/*     */   public CustomConversions mongoCustomConversions() {
/* 143 */     return new CustomConversions(Collections.emptyList());
/*     */   }
/*     */   
/*     */ 
/*     */   private static class GridFsMongoDbFactory
/*     */     implements MongoDbFactory
/*     */   {
/*     */     private final MongoDbFactory mongoDbFactory;
/*     */     
/*     */     private final MongoProperties properties;
/*     */     
/*     */ 
/*     */     GridFsMongoDbFactory(MongoDbFactory mongoDbFactory, MongoProperties properties)
/*     */     {
/* 157 */       Assert.notNull(mongoDbFactory, "MongoDbFactory must not be null");
/* 158 */       Assert.notNull(properties, "Properties must not be null");
/* 159 */       this.mongoDbFactory = mongoDbFactory;
/* 160 */       this.properties = properties;
/*     */     }
/*     */     
/*     */     public DB getDb() throws DataAccessException
/*     */     {
/* 165 */       String gridFsDatabase = this.properties.getGridFsDatabase();
/* 166 */       if (StringUtils.hasText(gridFsDatabase)) {
/* 167 */         return this.mongoDbFactory.getDb(gridFsDatabase);
/*     */       }
/* 169 */       return this.mongoDbFactory.getDb();
/*     */     }
/*     */     
/*     */     public DB getDb(String dbName) throws DataAccessException
/*     */     {
/* 174 */       return this.mongoDbFactory.getDb(dbName);
/*     */     }
/*     */     
/*     */     public PersistenceExceptionTranslator getExceptionTranslator()
/*     */     {
/* 179 */       return this.mongoDbFactory.getExceptionTranslator();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\mongo\MongoDataAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */