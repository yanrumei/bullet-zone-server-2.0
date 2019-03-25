/*    */ package org.springframework.boot.autoconfigure.transaction.jta;
/*    */ 
/*    */ import org.springframework.boot.context.properties.ConfigurationProperties;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @ConfigurationProperties(prefix="spring.jta", ignoreUnknownFields=true)
/*    */ public class JtaProperties
/*    */ {
/*    */   private String logDir;
/*    */   private String transactionManagerId;
/*    */   
/*    */   public void setLogDir(String logDir)
/*    */   {
/* 46 */     this.logDir = logDir;
/*    */   }
/*    */   
/*    */   public String getLogDir() {
/* 50 */     return this.logDir;
/*    */   }
/*    */   
/*    */   public String getTransactionManagerId() {
/* 54 */     return this.transactionManagerId;
/*    */   }
/*    */   
/*    */   public void setTransactionManagerId(String transactionManagerId) {
/* 58 */     this.transactionManagerId = transactionManagerId;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\transaction\jta\JtaProperties.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */