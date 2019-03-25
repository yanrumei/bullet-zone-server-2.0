/*    */ package org.springframework.boot.jta.atomikos;
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
/*    */ public class AtomikosXADataSourceWrapper
/*    */   implements XADataSourceWrapper
/*    */ {
/*    */   public AtomikosDataSourceBean wrapDataSource(XADataSource dataSource)
/*    */     throws Exception
/*    */   {
/* 35 */     AtomikosDataSourceBean bean = new AtomikosDataSourceBean();
/* 36 */     bean.setXaDataSource(dataSource);
/* 37 */     return bean;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\jta\atomikos\AtomikosXADataSourceWrapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */