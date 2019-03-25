/*    */ package org.hibernate.validator.internal.constraintvalidators.hv;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.script.ScriptException;
/*    */ import org.hibernate.validator.internal.util.CollectionHelper;
/*    */ import org.hibernate.validator.internal.util.logging.Log;
/*    */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*    */ import org.hibernate.validator.internal.util.scriptengine.ScriptEvaluator;
/*    */ import org.hibernate.validator.internal.util.scriptengine.ScriptEvaluatorFactory;
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
/*    */ class ScriptAssertContext
/*    */ {
/* 27 */   private static final Log log = ;
/*    */   private final String script;
/*    */   private final ScriptEvaluator scriptEvaluator;
/*    */   
/*    */   public ScriptAssertContext(String languageName, String script)
/*    */   {
/* 33 */     this.script = script;
/* 34 */     this.scriptEvaluator = getScriptEvaluator(languageName);
/*    */   }
/*    */   
/*    */   public boolean evaluateScriptAssertExpression(Object object, String alias) {
/* 38 */     Map<String, Object> bindings = CollectionHelper.newHashMap();
/* 39 */     bindings.put(alias, object);
/*    */     
/* 41 */     return evaluateScriptAssertExpression(bindings);
/*    */   }
/*    */   
/*    */   public boolean evaluateScriptAssertExpression(Map<String, Object> bindings)
/*    */   {
/*    */     try
/*    */     {
/* 48 */       result = this.scriptEvaluator.evaluate(this.script, bindings);
/*    */     } catch (ScriptException e) {
/*    */       Object result;
/* 51 */       throw log.getErrorDuringScriptExecutionException(this.script, e);
/*    */     }
/*    */     Object result;
/* 54 */     return handleResult(result);
/*    */   }
/*    */   
/*    */   private ScriptEvaluator getScriptEvaluator(String languageName) {
/*    */     try {
/* 59 */       ScriptEvaluatorFactory evaluatorFactory = ScriptEvaluatorFactory.getInstance();
/* 60 */       return evaluatorFactory.getScriptEvaluatorByLanguageName(languageName);
/*    */     }
/*    */     catch (ScriptException e) {
/* 63 */       throw log.getCreationOfScriptExecutorFailedException(languageName, e);
/*    */     }
/*    */   }
/*    */   
/*    */   private boolean handleResult(Object evaluationResult) {
/* 68 */     if (evaluationResult == null) {
/* 69 */       throw log.getScriptMustReturnTrueOrFalseException(this.script);
/*    */     }
/*    */     
/* 72 */     if (!(evaluationResult instanceof Boolean)) {
/* 73 */       throw log.getScriptMustReturnTrueOrFalseException(this.script, evaluationResult, evaluationResult
/*    */       
/*    */ 
/* 76 */         .getClass().getCanonicalName());
/*    */     }
/*    */     
/*    */ 
/* 80 */     return Boolean.TRUE.equals(evaluationResult);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\constraintvalidators\hv\ScriptAssertContext.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */