/*     */ package org.springframework.boot.autoconfigure.session;
/*     */ 
/*     */ import javax.annotation.PostConstruct;
/*     */ import org.springframework.beans.factory.ObjectProvider;
/*     */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*     */ import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
/*     */ import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
/*     */ import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
/*     */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*     */ import org.springframework.context.annotation.Configuration;
/*     */ import org.springframework.context.annotation.Import;
/*     */ import org.springframework.context.annotation.ImportSelector;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.session.Session;
/*     */ import org.springframework.session.SessionRepository;
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
/*     */ @ConditionalOnMissingBean({SessionRepository.class})
/*     */ @ConditionalOnClass({Session.class})
/*     */ @ConditionalOnWebApplication
/*     */ @EnableConfigurationProperties({SessionProperties.class})
/*     */ @AutoConfigureAfter({DataSourceAutoConfiguration.class, HazelcastAutoConfiguration.class, JdbcTemplateAutoConfiguration.class, MongoAutoConfiguration.class, RedisAutoConfiguration.class})
/*     */ @Import({SessionConfigurationImportSelector.class, SessionRepositoryValidator.class})
/*     */ public class SessionAutoConfiguration
/*     */ {
/*     */   static class SessionConfigurationImportSelector
/*     */     implements ImportSelector
/*     */   {
/*     */     public String[] selectImports(AnnotationMetadata importingClassMetadata)
/*     */     {
/*  69 */       StoreType[] types = StoreType.values();
/*  70 */       String[] imports = new String[types.length];
/*  71 */       for (int i = 0; i < types.length; i++) {
/*  72 */         imports[i] = SessionStoreMappings.getConfigurationClass(types[i]);
/*     */       }
/*  74 */       return imports;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static class SessionRepositoryValidator
/*     */   {
/*     */     private SessionProperties sessionProperties;
/*     */     
/*     */ 
/*     */     private ObjectProvider<SessionRepository<?>> sessionRepositoryProvider;
/*     */     
/*     */ 
/*     */ 
/*     */     SessionRepositoryValidator(SessionProperties sessionProperties, ObjectProvider<SessionRepository<?>> sessionRepositoryProvider)
/*     */     {
/*  91 */       this.sessionProperties = sessionProperties;
/*  92 */       this.sessionRepositoryProvider = sessionRepositoryProvider;
/*     */     }
/*     */     
/*     */     @PostConstruct
/*     */     public void checkSessionRepository() {
/*  97 */       StoreType storeType = this.sessionProperties.getStoreType();
/*  98 */       if ((storeType != StoreType.NONE) && 
/*  99 */         (this.sessionRepositoryProvider.getIfAvailable() == null)) {
/* 100 */         if (storeType != null)
/*     */         {
/*     */ 
/* 103 */           throw new IllegalArgumentException("No session repository could be auto-configured, check your configuration (session store type is '" + storeType.name().toLowerCase() + "')");
/*     */         }
/* 105 */         throw new IllegalArgumentException("No Spring Session store is configured: set the 'spring.session.store-type' property");
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\session\SessionAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */