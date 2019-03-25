/*    */ package org.springframework.boot.autoconfigure.data.couchbase;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
/*    */ import org.springframework.data.couchbase.repository.config.CouchbaseRepositoryConfigurationExtension;
/*    */ import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
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
/*    */ class CouchbaseRepositoriesRegistrar
/*    */   extends AbstractRepositoryConfigurationSourceSupport
/*    */ {
/*    */   protected Class<? extends Annotation> getAnnotation()
/*    */   {
/* 38 */     return EnableCouchbaseRepositories.class;
/*    */   }
/*    */   
/*    */   protected Class<?> getConfiguration()
/*    */   {
/* 43 */     return EnableCouchbaseRepositoriesConfiguration.class;
/*    */   }
/*    */   
/*    */   protected RepositoryConfigurationExtension getRepositoryConfigurationExtension()
/*    */   {
/* 48 */     return new CouchbaseRepositoryConfigurationExtension();
/*    */   }
/*    */   
/*    */   @EnableCouchbaseRepositories
/*    */   private static class EnableCouchbaseRepositoriesConfiguration {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\couchbase\CouchbaseRepositoriesRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */