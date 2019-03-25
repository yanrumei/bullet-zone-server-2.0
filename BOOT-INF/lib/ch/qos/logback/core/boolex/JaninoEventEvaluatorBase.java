/*    */ package ch.qos.logback.core.boolex;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.codehaus.janino.ScriptEvaluator;
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
/*    */ public abstract class JaninoEventEvaluatorBase<E>
/*    */   extends EventEvaluatorBase<E>
/*    */ {
/*    */   static Class<?> EXPRESSION_TYPE;
/*    */   static Class<?>[] THROWN_EXCEPTIONS;
/*    */   public static final int ERROR_THRESHOLD = 4;
/*    */   private String expression;
/*    */   ScriptEvaluator scriptEvaluator;
/*    */   
/*    */   static
/*    */   {
/* 30 */     EXPRESSION_TYPE = Boolean.TYPE;
/* 31 */     THROWN_EXCEPTIONS = new Class[1];
/*    */     
/*    */ 
/*    */ 
/* 35 */     THROWN_EXCEPTIONS[0] = EvaluationException.class;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 41 */   private int errorCount = 0;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 51 */   protected List<Matcher> matcherList = new ArrayList();
/*    */   
/*    */   public void start()
/*    */   {
/*    */     try {
/* 56 */       assert (this.context != null);
/* 57 */       this.scriptEvaluator = new ScriptEvaluator(getDecoratedExpression(), EXPRESSION_TYPE, getParameterNames(), getParameterTypes(), THROWN_EXCEPTIONS);
/* 58 */       super.start();
/*    */     } catch (Exception e) {
/* 60 */       addError("Could not start evaluator with expression [" + this.expression + "]", e);
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean evaluate(E event) throws EvaluationException {
/* 65 */     if (!isStarted()) {
/* 66 */       throw new IllegalStateException("Evaluator [" + this.name + "] was called in stopped state");
/*    */     }
/*    */     try {
/* 69 */       Boolean result = (Boolean)this.scriptEvaluator.evaluate(getParameterValues(event));
/* 70 */       return result.booleanValue();
/*    */     } catch (Exception ex) {
/* 72 */       this.errorCount += 1;
/* 73 */       if (this.errorCount >= 4) {
/* 74 */         stop();
/*    */       }
/* 76 */       throw new EvaluationException("Evaluator [" + this.name + "] caused an exception", ex);
/*    */     }
/*    */   }
/*    */   
/*    */   public String getExpression() {
/* 81 */     return this.expression;
/*    */   }
/*    */   
/*    */   public void setExpression(String expression) {
/* 85 */     this.expression = expression;
/*    */   }
/*    */   
/*    */   public void addMatcher(Matcher matcher) {
/* 89 */     this.matcherList.add(matcher);
/*    */   }
/*    */   
/*    */   public List<Matcher> getMatcherList() {
/* 93 */     return this.matcherList;
/*    */   }
/*    */   
/*    */   protected abstract String getDecoratedExpression();
/*    */   
/*    */   protected abstract String[] getParameterNames();
/*    */   
/*    */   protected abstract Class<?>[] getParameterTypes();
/*    */   
/*    */   protected abstract Object[] getParameterValues(E paramE);
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-core-1.1.11.jar!\ch\qos\logback\core\boolex\JaninoEventEvaluatorBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */