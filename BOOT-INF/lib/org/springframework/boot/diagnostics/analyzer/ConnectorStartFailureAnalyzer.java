/*    */ package org.springframework.boot.diagnostics.analyzer;
/*    */ 
/*    */ import org.springframework.boot.context.embedded.tomcat.ConnectorStartFailedException;
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
/*    */ class ConnectorStartFailureAnalyzer
/*    */   extends AbstractFailureAnalyzer<ConnectorStartFailedException>
/*    */ {
/*    */   protected FailureAnalysis analyze(Throwable rootFailure, ConnectorStartFailedException cause)
/*    */   {
/* 34 */     return new FailureAnalysis("The Tomcat connector configured to listen on port " + cause
/* 35 */       .getPort() + " failed to start. The port may already be in use or the connector may be misconfigured.", "Verify the connector's configuration, identify and stop any process that's listening on port " + cause
/*    */       
/*    */ 
/*    */ 
/* 39 */       .getPort() + ", or configure this application to listen on another port.", cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\diagnostics\analyzer\ConnectorStartFailureAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */