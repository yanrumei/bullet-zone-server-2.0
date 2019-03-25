/*    */ package org.springframework.boot.diagnostics.analyzer;
/*    */ 
/*    */ import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
/*    */ import org.springframework.boot.diagnostics.FailureAnalysis;
/*    */ import org.springframework.util.CollectionUtils;
/*    */ import org.springframework.validation.BindException;
/*    */ import org.springframework.validation.FieldError;
/*    */ import org.springframework.validation.ObjectError;
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
/*    */ class BindFailureAnalyzer
/*    */   extends AbstractFailureAnalyzer<BindException>
/*    */ {
/*    */   protected FailureAnalysis analyze(Throwable rootFailure, BindException cause)
/*    */   {
/* 37 */     if (CollectionUtils.isEmpty(cause.getAllErrors())) {
/* 38 */       return null;
/*    */     }
/*    */     
/* 41 */     StringBuilder description = new StringBuilder(String.format("Binding to target %s failed:%n", new Object[] { cause.getTarget() }));
/* 42 */     for (ObjectError error : cause.getAllErrors()) {
/* 43 */       if ((error instanceof FieldError)) {
/* 44 */         FieldError fieldError = (FieldError)error;
/* 45 */         description.append(String.format("%n    Property: %s", new Object[] {cause
/* 46 */           .getObjectName() + "." + fieldError.getField() }));
/* 47 */         description.append(
/* 48 */           String.format("%n    Value: %s", new Object[] {fieldError.getRejectedValue() }));
/*    */       }
/* 50 */       description.append(
/* 51 */         String.format("%n    Reason: %s%n", new Object[] {error.getDefaultMessage() }));
/*    */     }
/* 53 */     return new FailureAnalysis(description.toString(), "Update your application's configuration", cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\diagnostics\analyzer\BindFailureAnalyzer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */