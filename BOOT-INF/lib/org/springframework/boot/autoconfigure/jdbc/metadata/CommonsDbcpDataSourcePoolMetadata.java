/*    */ package org.springframework.boot.autoconfigure.jdbc.metadata;
/*    */ 
/*    */ import org.apache.commons.dbcp.BasicDataSource;
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
/*    */ @Deprecated
/*    */ public class CommonsDbcpDataSourcePoolMetadata
/*    */   extends AbstractDataSourcePoolMetadata<BasicDataSource>
/*    */ {
/*    */   public CommonsDbcpDataSourcePoolMetadata(BasicDataSource dataSource)
/*    */   {
/* 34 */     super(dataSource);
/*    */   }
/*    */   
/*    */   public Integer getActive()
/*    */   {
/* 39 */     return Integer.valueOf(((BasicDataSource)getDataSource()).getNumActive());
/*    */   }
/*    */   
/*    */   public Integer getMax()
/*    */   {
/* 44 */     return Integer.valueOf(((BasicDataSource)getDataSource()).getMaxActive());
/*    */   }
/*    */   
/*    */   public Integer getMin()
/*    */   {
/* 49 */     return Integer.valueOf(((BasicDataSource)getDataSource()).getMinIdle());
/*    */   }
/*    */   
/*    */   public String getValidationQuery()
/*    */   {
/* 54 */     return ((BasicDataSource)getDataSource()).getValidationQuery();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\metadata\CommonsDbcpDataSourcePoolMetadata.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */