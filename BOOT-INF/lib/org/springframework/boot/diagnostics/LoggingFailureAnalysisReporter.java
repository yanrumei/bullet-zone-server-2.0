/*    */ package org.springframework.boot.diagnostics;
/*    */ 
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.util.StringUtils;
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
/*    */ public final class LoggingFailureAnalysisReporter
/*    */   implements FailureAnalysisReporter
/*    */ {
/* 33 */   private static final Log logger = LogFactory.getLog(LoggingFailureAnalysisReporter.class);
/*    */   
/*    */   public void report(FailureAnalysis failureAnalysis)
/*    */   {
/* 37 */     if (logger.isDebugEnabled()) {
/* 38 */       logger.debug("Application failed to start due to an exception", failureAnalysis
/* 39 */         .getCause());
/*    */     }
/* 41 */     if (logger.isErrorEnabled()) {
/* 42 */       logger.error(buildMessage(failureAnalysis));
/*    */     }
/*    */   }
/*    */   
/*    */   private String buildMessage(FailureAnalysis failureAnalysis) {
/* 47 */     StringBuilder builder = new StringBuilder();
/* 48 */     builder.append(String.format("%n%n", new Object[0]));
/* 49 */     builder.append(String.format("***************************%n", new Object[0]));
/* 50 */     builder.append(String.format("APPLICATION FAILED TO START%n", new Object[0]));
/* 51 */     builder.append(String.format("***************************%n%n", new Object[0]));
/* 52 */     builder.append(String.format("Description:%n%n", new Object[0]));
/* 53 */     builder.append(String.format("%s%n", new Object[] { failureAnalysis.getDescription() }));
/* 54 */     if (StringUtils.hasText(failureAnalysis.getAction())) {
/* 55 */       builder.append(String.format("%nAction:%n%n", new Object[0]));
/* 56 */       builder.append(String.format("%s%n", new Object[] { failureAnalysis.getAction() }));
/*    */     }
/* 58 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\diagnostics\LoggingFailureAnalysisReporter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */