/*    */ package org.springframework.boot.autoconfigure.data.ldap;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import org.springframework.boot.autoconfigure.data.AbstractRepositoryConfigurationSourceSupport;
/*    */ import org.springframework.data.ldap.repository.config.EnableLdapRepositories;
/*    */ import org.springframework.data.ldap.repository.config.LdapRepositoryConfigurationExtension;
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
/*    */ class LdapRepositoriesRegistrar
/*    */   extends AbstractRepositoryConfigurationSourceSupport
/*    */ {
/*    */   protected Class<? extends Annotation> getAnnotation()
/*    */   {
/* 38 */     return EnableLdapRepositories.class;
/*    */   }
/*    */   
/*    */   protected Class<?> getConfiguration()
/*    */   {
/* 43 */     return EnableLdapRepositoriesConfiguration.class;
/*    */   }
/*    */   
/*    */   protected RepositoryConfigurationExtension getRepositoryConfigurationExtension()
/*    */   {
/* 48 */     return new LdapRepositoryConfigurationExtension();
/*    */   }
/*    */   
/*    */   @EnableLdapRepositories
/*    */   private static class EnableLdapRepositoriesConfiguration {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\ldap\LdapRepositoriesRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */