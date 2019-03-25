/*    */ package org.springframework.boot.autoconfigure.jdbc.metadata;
/*    */ 
/*    */ import org.apache.tomcat.jdbc.pool.ConnectionPool;
/*    */ import org.apache.tomcat.jdbc.pool.DataSource;
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
/*    */ public class TomcatDataSourcePoolMetadata
/*    */   extends AbstractDataSourcePoolMetadata<DataSource>
/*    */ {
/*    */   public TomcatDataSourcePoolMetadata(DataSource dataSource)
/*    */   {
/* 31 */     super(dataSource);
/*    */   }
/*    */   
/*    */   public Integer getActive()
/*    */   {
/* 36 */     ConnectionPool pool = ((DataSource)getDataSource()).getPool();
/* 37 */     return Integer.valueOf(pool == null ? 0 : pool.getActive());
/*    */   }
/*    */   
/*    */   public Integer getMax()
/*    */   {
/* 42 */     return Integer.valueOf(((DataSource)getDataSource()).getMaxActive());
/*    */   }
/*    */   
/*    */   public Integer getMin()
/*    */   {
/* 47 */     return Integer.valueOf(((DataSource)getDataSource()).getMinIdle());
/*    */   }
/*    */   
/*    */   public String getValidationQuery()
/*    */   {
/* 52 */     return ((DataSource)getDataSource()).getValidationQuery();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\metadata\TomcatDataSourcePoolMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */