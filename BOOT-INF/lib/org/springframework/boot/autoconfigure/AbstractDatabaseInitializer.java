/*    */ package org.springframework.boot.autoconfigure;
/*    */ 
/*    */ import javax.annotation.PostConstruct;
/*    */ import javax.sql.DataSource;
/*    */ import org.springframework.boot.jdbc.DatabaseDriver;
/*    */ import org.springframework.core.io.ResourceLoader;
/*    */ import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
/*    */ import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
/*    */ import org.springframework.jdbc.support.JdbcUtils;
/*    */ import org.springframework.jdbc.support.MetaDataAccessException;
/*    */ import org.springframework.util.Assert;
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
/*    */ 
/*    */ public abstract class AbstractDatabaseInitializer
/*    */ {
/*    */   private static final String PLATFORM_PLACEHOLDER = "@@platform@@";
/*    */   private final DataSource dataSource;
/*    */   private final ResourceLoader resourceLoader;
/*    */   
/*    */   protected AbstractDatabaseInitializer(DataSource dataSource, ResourceLoader resourceLoader)
/*    */   {
/* 47 */     Assert.notNull(dataSource, "DataSource must not be null");
/* 48 */     Assert.notNull(resourceLoader, "ResourceLoader must not be null");
/* 49 */     this.dataSource = dataSource;
/* 50 */     this.resourceLoader = resourceLoader;
/*    */   }
/*    */   
/*    */   @PostConstruct
/*    */   protected void initialize() {
/* 55 */     if (!isEnabled()) {
/* 56 */       return;
/*    */     }
/* 58 */     ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
/* 59 */     String schemaLocation = getSchemaLocation();
/* 60 */     if (schemaLocation.contains("@@platform@@")) {
/* 61 */       String platform = getDatabaseName();
/* 62 */       schemaLocation = schemaLocation.replace("@@platform@@", platform);
/*    */     }
/* 64 */     populator.addScript(this.resourceLoader.getResource(schemaLocation));
/* 65 */     populator.setContinueOnError(true);
/* 66 */     DatabasePopulatorUtils.execute(populator, this.dataSource);
/*    */   }
/*    */   
/*    */   protected abstract boolean isEnabled();
/*    */   
/*    */   protected abstract String getSchemaLocation();
/*    */   
/*    */   protected String getDatabaseName() {
/*    */     try {
/* 75 */       String productName = JdbcUtils.commonDatabaseName(
/* 76 */         JdbcUtils.extractDatabaseMetaData(this.dataSource, "getDatabaseProductName")
/* 77 */         .toString());
/* 78 */       DatabaseDriver databaseDriver = DatabaseDriver.fromProductName(productName);
/* 79 */       if (databaseDriver == DatabaseDriver.UNKNOWN) {
/* 80 */         throw new IllegalStateException("Unable to detect database type");
/*    */       }
/* 82 */       return databaseDriver.getId();
/*    */     }
/*    */     catch (MetaDataAccessException ex) {
/* 85 */       throw new IllegalStateException("Unable to detect database type", ex);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\AbstractDatabaseInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */