/*    */ package org.springframework.boot.autoconfigure.jdbc.metadata;
/*    */ 
/*    */ import com.zaxxer.hikari.HikariDataSource;
/*    */ import com.zaxxer.hikari.pool.HikariPool;
/*    */ import org.springframework.beans.DirectFieldAccessor;
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
/*    */ public class HikariDataSourcePoolMetadata
/*    */   extends AbstractDataSourcePoolMetadata<HikariDataSource>
/*    */ {
/*    */   public HikariDataSourcePoolMetadata(HikariDataSource dataSource)
/*    */   {
/* 36 */     super(dataSource);
/*    */   }
/*    */   
/*    */   public Integer getActive()
/*    */   {
/*    */     try {
/* 42 */       return Integer.valueOf(getHikariPool().getActiveConnections());
/*    */     }
/*    */     catch (Exception ex) {}
/* 45 */     return null;
/*    */   }
/*    */   
/*    */   private HikariPool getHikariPool()
/*    */   {
/* 50 */     return 
/* 51 */       (HikariPool)new DirectFieldAccessor(getDataSource()).getPropertyValue("pool");
/*    */   }
/*    */   
/*    */   public Integer getMax()
/*    */   {
/* 56 */     return Integer.valueOf(((HikariDataSource)getDataSource()).getMaximumPoolSize());
/*    */   }
/*    */   
/*    */   public Integer getMin()
/*    */   {
/* 61 */     return Integer.valueOf(((HikariDataSource)getDataSource()).getMinimumIdle());
/*    */   }
/*    */   
/*    */   public String getValidationQuery()
/*    */   {
/* 66 */     return ((HikariDataSource)getDataSource()).getConnectionTestQuery();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\metadata\HikariDataSourcePoolMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */