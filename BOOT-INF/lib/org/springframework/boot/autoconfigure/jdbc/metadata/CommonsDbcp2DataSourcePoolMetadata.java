/*    */ package org.springframework.boot.autoconfigure.jdbc.metadata;
/*    */ 
/*    */ import org.apache.commons.dbcp2.BasicDataSource;
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
/*    */ public class CommonsDbcp2DataSourcePoolMetadata
/*    */   extends AbstractDataSourcePoolMetadata<BasicDataSource>
/*    */ {
/*    */   public CommonsDbcp2DataSourcePoolMetadata(BasicDataSource dataSource)
/*    */   {
/* 33 */     super(dataSource);
/*    */   }
/*    */   
/*    */   public Integer getActive()
/*    */   {
/* 38 */     return Integer.valueOf(((BasicDataSource)getDataSource()).getNumActive());
/*    */   }
/*    */   
/*    */   public Integer getMax()
/*    */   {
/* 43 */     return Integer.valueOf(((BasicDataSource)getDataSource()).getMaxTotal());
/*    */   }
/*    */   
/*    */   public Integer getMin()
/*    */   {
/* 48 */     return Integer.valueOf(((BasicDataSource)getDataSource()).getMinIdle());
/*    */   }
/*    */   
/*    */   public String getValidationQuery()
/*    */   {
/* 53 */     return ((BasicDataSource)getDataSource()).getValidationQuery();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\metadata\CommonsDbcp2DataSourcePoolMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */