/*    */ package org.springframework.boot.autoconfigure.data.mongo;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
/*    */ import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
/*    */ import org.springframework.data.mongodb.repository.config.MongoRepositoryConfigurationExtension;
/*    */ import org.springframework.data.repository.config.RepositoryConfigurationExtension;
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
/*    */ class MongoRepositoriesAutoConfigureRegistrar
/*    */   extends AbstractRepositoryConfigurationSourceSupport
/*    */ {
/*    */   protected Class<? extends Annotation> getAnnotation()
/*    */   {
/* 38 */     return EnableMongoRepositories.class;
/*    */   }
/*    */   
/*    */   protected Class<?> getConfiguration()
/*    */   {
/* 43 */     return EnableMongoRepositoriesConfiguration.class;
/*    */   }
/*    */   
/*    */   protected RepositoryConfigurationExtension getRepositoryConfigurationExtension()
/*    */   {
/* 48 */     return new MongoRepositoryConfigurationExtension();
/*    */   }
/*    */   
/*    */   @EnableMongoRepositories
/*    */   private static class EnableMongoRepositoriesConfiguration {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\mongo\MongoRepositoriesAutoConfigureRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */