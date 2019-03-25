/*    */ package org.springframework.boot.jta.bitronix;
/*    */ 
/*    */ import javax.sql.XADataSource;
/*    */ import org.springframework.boot.jta.XADataSourceWrapper;
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
/*    */ public class BitronixXADataSourceWrapper
/*    */   implements XADataSourceWrapper
/*    */ {
/*    */   public PoolingDataSourceBean wrapDataSource(XADataSource dataSource)
/*    */     throws Exception
/*    */   {
/* 35 */     PoolingDataSourceBean pool = new PoolingDataSourceBean();
/* 36 */     pool.setDataSource(dataSource);
/* 37 */     return pool;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\bitronix\BitronixXADataSourceWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */