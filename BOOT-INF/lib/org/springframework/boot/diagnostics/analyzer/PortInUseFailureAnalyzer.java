/*    */ package org.springframework.boot.diagnostics.analyzer;
/*    */ 
/*    */ import org.springframework.boot.context.embedded.PortInUseException;
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
/*    */ class PortInUseFailureAnalyzer
/*    */   extends AbstractFailureAnalyzer<PortInUseException>
/*    */ {
/*    */   protected FailureAnalysis analyze(Throwable rootFailure, PortInUseException cause)
/*    */   {
/* 33 */     return new FailureAnalysis("Embedded servlet container failed to start. Port " + cause
/* 34 */       .getPort() + " was already in use.", "Identify and stop the process that's listening on port " + cause
/*    */       
/*    */ 
/* 37 */       .getPort() + " or configure this application to listen on another port.", cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\diagnostics\analyzer\PortInUseFailureAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */