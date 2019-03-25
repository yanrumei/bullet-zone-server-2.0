/*    */ package org.springframework.boot.diagnostics.analyzer;
/*    */ 
/*    */ import javax.validation.ValidationException;
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
/*    */ 
/*    */ class ValidationExceptionFailureAnalyzer
/*    */   extends AbstractFailureAnalyzer<ValidationException>
/*    */ {
/*    */   private static final String MISSING_IMPLEMENTATION_MESSAGE = "Unable to create a Configuration, because no Bean Validation provider could be found";
/*    */   
/*    */   protected FailureAnalysis analyze(Throwable rootFailure, ValidationException cause)
/*    */   {
/* 39 */     if (cause.getMessage().startsWith("Unable to create a Configuration, because no Bean Validation provider could be found")) {
/* 40 */       return new FailureAnalysis("The Bean Validation API is on the classpath but no implementation could be found", "Add an implementation, such as Hibernate Validator, to the classpath", cause);
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 47 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\diagnostics\analyzer\ValidationExceptionFailureAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */