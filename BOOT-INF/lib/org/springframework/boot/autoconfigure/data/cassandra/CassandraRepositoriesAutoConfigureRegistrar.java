/*    */ package org.springframework.boot.autoconfigure.data.cassandra;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
/*    */ import org.springframework.data.cassandra.repository.config.CassandraRepositoryConfigurationExtension;
/*    */ import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
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
/*    */ 
/*    */ class CassandraRepositoriesAutoConfigureRegistrar
/*    */   extends AbstractRepositoryConfigurationSourceSupport
/*    */ {
/*    */   protected Class<? extends Annotation> getAnnotation()
/*    */   {
/* 39 */     return EnableCassandraRepositories.class;
/*    */   }
/*    */   
/*    */   protected Class<?> getConfiguration()
/*    */   {
/* 44 */     return EnableCassandraRepositoriesConfiguration.class;
/*    */   }
/*    */   
/*    */   protected RepositoryConfigurationExtension getRepositoryConfigurationExtension()
/*    */   {
/* 49 */     return new CassandraRepositoryConfigurationExtension();
/*    */   }
/*    */   
/*    */   @EnableCassandraRepositories
/*    */   private static class EnableCassandraRepositoriesConfiguration {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\cassandra\CassandraRepositoriesAutoConfigureRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */