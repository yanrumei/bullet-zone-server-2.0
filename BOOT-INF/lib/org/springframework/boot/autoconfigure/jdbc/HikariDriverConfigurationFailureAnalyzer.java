/*    */ package org.springframework.boot.autoconfigure.jdbc;
/*    */ 
/*    */ import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
/*    */ import org.springframework.boot.diagnostics.FailureAnalysis;
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
/*    */ class HikariDriverConfigurationFailureAnalyzer
/*    */   extends AbstractFailureAnalyzer<IllegalStateException>
/*    */ {
/*    */   private static final String EXPECTED_MESSAGE = "both driverClassName and dataSourceClassName are specified, one or the other should be used";
/*    */   
/*    */   protected FailureAnalysis analyze(Throwable rootFailure, IllegalStateException cause)
/*    */   {
/* 37 */     if (!"both driverClassName and dataSourceClassName are specified, one or the other should be used".equals(cause.getMessage())) {
/* 38 */       return null;
/*    */     }
/* 40 */     return new FailureAnalysis("Configuration of the Hikari connection pool failed: 'dataSourceClassName' is not supported.", "Spring Boot auto-configures only a driver and can't specify a custom DataSource. Consider configuring the Hikari DataSource in your own configuration.", cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\HikariDriverConfigurationFailureAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */