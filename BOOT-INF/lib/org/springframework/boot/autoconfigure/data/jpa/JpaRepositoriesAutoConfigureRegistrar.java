/*    */ package org.springframework.boot.autoconfigure.data.jpa;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
/*    */ import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
/*    */ import org.springframework.data.jpa.repository.config.JpaRepositoryConfigExtension;
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
/*    */ class JpaRepositoriesAutoConfigureRegistrar
/*    */   extends AbstractRepositoryConfigurationSourceSupport
/*    */ {
/*    */   protected Class<? extends Annotation> getAnnotation()
/*    */   {
/* 39 */     return EnableJpaRepositories.class;
/*    */   }
/*    */   
/*    */   protected Class<?> getConfiguration()
/*    */   {
/* 44 */     return EnableJpaRepositoriesConfiguration.class;
/*    */   }
/*    */   
/*    */   protected RepositoryConfigurationExtension getRepositoryConfigurationExtension()
/*    */   {
/* 49 */     return new JpaRepositoryConfigExtension();
/*    */   }
/*    */   
/*    */   @EnableJpaRepositories
/*    */   private static class EnableJpaRepositoriesConfiguration {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\jpa\JpaRepositoriesAutoConfigureRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */