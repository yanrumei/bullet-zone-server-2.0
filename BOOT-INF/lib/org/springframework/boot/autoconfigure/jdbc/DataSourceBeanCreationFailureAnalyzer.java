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
/*    */ class DataSourceBeanCreationFailureAnalyzer
/*    */   extends AbstractFailureAnalyzer<DataSourceProperties.DataSourceBeanCreationException>
/*    */ {
/*    */   protected FailureAnalysis analyze(Throwable rootFailure, DataSourceProperties.DataSourceBeanCreationException cause)
/*    */   {
/* 35 */     String message = cause.getMessage();
/* 36 */     String description = message.substring(0, message.indexOf(".")).trim();
/* 37 */     String action = message.substring(message.indexOf(".") + 1).trim();
/* 38 */     return new FailureAnalysis(description, action, cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\jdbc\DataSourceBeanCreationFailureAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */