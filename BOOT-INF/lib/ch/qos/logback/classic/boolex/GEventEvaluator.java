/*    */ package ch.qos.logback.classic.boolex;
/*    */ 
/*    */ import ch.qos.logback.classic.spi.ILoggingEvent;
/*    */ import ch.qos.logback.core.boolex.EvaluationException;
/*    */ import ch.qos.logback.core.boolex.EventEvaluatorBase;
/*    */ import ch.qos.logback.core.util.FileUtil;
/*    */ import groovy.lang.GroovyClassLoader;
/*    */ import groovy.lang.GroovyObject;
/*    */ import groovy.lang.Script;
/*    */ import org.codehaus.groovy.control.CompilationFailedException;
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
/*    */ public class GEventEvaluator
/*    */   extends EventEvaluatorBase<ILoggingEvent>
/*    */ {
/*    */   String expression;
/*    */   IEvaluator delegateEvaluator;
/*    */   Script script;
/*    */   
/*    */   public String getExpression()
/*    */   {
/* 34 */     return this.expression;
/*    */   }
/*    */   
/*    */   public void setExpression(String expression) {
/* 38 */     this.expression = expression;
/*    */   }
/*    */   
/*    */   public void start() {
/* 42 */     int errors = 0;
/* 43 */     if ((this.expression == null) || (this.expression.length() == 0)) {
/* 44 */       addError("Empty expression");
/* 45 */       return;
/*    */     }
/* 47 */     addInfo("Expression to evaluate [" + this.expression + "]");
/*    */     
/*    */ 
/* 50 */     ClassLoader classLoader = getClass().getClassLoader();
/* 51 */     String currentPackageName = getClass().getPackage().getName();
/* 52 */     currentPackageName = currentPackageName.replace('.', '/');
/*    */     
/* 54 */     FileUtil fileUtil = new FileUtil(getContext());
/* 55 */     String scriptText = fileUtil.resourceAsString(classLoader, currentPackageName + "/EvaluatorTemplate.groovy");
/* 56 */     if (scriptText == null) {
/* 57 */       return;
/*    */     }
/*    */     
/*    */ 
/* 61 */     scriptText = scriptText.replace("//EXPRESSION", this.expression);
/*    */     
/* 63 */     GroovyClassLoader gLoader = new GroovyClassLoader(classLoader);
/*    */     try {
/* 65 */       Class scriptClass = gLoader.parseClass(scriptText);
/*    */       
/* 67 */       GroovyObject goo = (GroovyObject)scriptClass.newInstance();
/* 68 */       this.delegateEvaluator = ((IEvaluator)goo);
/*    */     }
/*    */     catch (CompilationFailedException cfe) {
/* 71 */       addError("Failed to compile expression [" + this.expression + "]", cfe);
/* 72 */       errors++;
/*    */     } catch (Exception e) {
/* 74 */       addError("Failed to compile expression [" + this.expression + "]", e);
/* 75 */       errors++;
/*    */     }
/* 77 */     if (errors == 0)
/* 78 */       super.start();
/*    */   }
/*    */   
/*    */   public boolean evaluate(ILoggingEvent event) throws NullPointerException, EvaluationException {
/* 82 */     if (this.delegateEvaluator == null) {
/* 83 */       return false;
/*    */     }
/* 85 */     return this.delegateEvaluator.doEvaluate(event);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\logback-classic-1.1.11.jar!\ch\qos\logback\classic\boolex\GEventEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */