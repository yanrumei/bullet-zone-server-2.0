/*    */ package org.springframework.boot.autoconfigure.batch;
/*    */ 
/*    */ import javax.sql.DataSource;
/*    */ import org.springframework.boot.autoconfigure.AbstractDatabaseInitializer;
/*    */ import org.springframework.core.io.ResourceLoader;
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
/*    */ public class BatchDatabaseInitializer
/*    */   extends AbstractDatabaseInitializer
/*    */ {
/*    */   private final BatchProperties properties;
/*    */   
/*    */   public BatchDatabaseInitializer(DataSource dataSource, ResourceLoader resourceLoader, BatchProperties properties)
/*    */   {
/* 37 */     super(dataSource, resourceLoader);
/* 38 */     Assert.notNull(properties, "BatchProperties must not be null");
/* 39 */     this.properties = properties;
/*    */   }
/*    */   
/*    */   protected boolean isEnabled()
/*    */   {
/* 44 */     return this.properties.getInitializer().isEnabled();
/*    */   }
/*    */   
/*    */   protected String getSchemaLocation()
/*    */   {
/* 49 */     return this.properties.getSchema();
/*    */   }
/*    */   
/*    */   protected String getDatabaseName()
/*    */   {
/* 54 */     String databaseName = super.getDatabaseName();
/* 55 */     if ("oracle".equals(databaseName)) {
/* 56 */       return "oracle10g";
/*    */     }
/* 58 */     return databaseName;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\batch\BatchDatabaseInitializer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */