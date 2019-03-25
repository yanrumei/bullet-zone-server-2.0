/*    */ package org.springframework.boot.autoconfigure.jdbc;
/*    */ 
/*    */ import javax.annotation.PreDestroy;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*    */ import org.springframework.boot.context.properties.EnableConfigurationProperties;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
/*    */ import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
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
/*    */ @EnableConfigurationProperties({DataSourceProperties.class})
/*    */ public class EmbeddedDataSourceConfiguration
/*    */   implements BeanClassLoaderAware
/*    */ {
/*    */   private EmbeddedDatabase database;
/*    */   private ClassLoader classLoader;
/*    */   private final DataSourceProperties properties;
/*    */   
/*    */   public EmbeddedDataSourceConfiguration(DataSourceProperties properties)
/*    */   {
/* 46 */     this.properties = properties;
/*    */   }
/*    */   
/*    */   public void setBeanClassLoader(ClassLoader classLoader)
/*    */   {
/* 51 */     this.classLoader = classLoader;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   public EmbeddedDatabase dataSource()
/*    */   {
/* 57 */     EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseConnection.get(this.classLoader).getType());
/*    */     
/* 59 */     this.database = builder.setName(this.properties.getName()).generateUniqueName(this.properties.isGenerateUniqueName()).build();
/* 60 */     return this.database;
/*    */   }
/*    */   
/*    */   @PreDestroy
/*    */   public void close() {
/* 65 */     if (this.database != null) {
/* 66 */       this.database.shutdown();
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\EmbeddedDataSourceConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */