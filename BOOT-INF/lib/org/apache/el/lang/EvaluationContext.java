/*     */ package org.apache.el.lang;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import javax.el.ELContext;
/*     */ import javax.el.ELResolver;
/*     */ import javax.el.EvaluationListener;
/*     */ import javax.el.FunctionMapper;
/*     */ import javax.el.ImportHandler;
/*     */ import javax.el.VariableMapper;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class EvaluationContext
/*     */   extends ELContext
/*     */ {
/*     */   private final ELContext elContext;
/*     */   private final FunctionMapper fnMapper;
/*     */   private final VariableMapper varMapper;
/*     */   
/*     */   public EvaluationContext(ELContext elContext, FunctionMapper fnMapper, VariableMapper varMapper)
/*     */   {
/*  41 */     this.elContext = elContext;
/*  42 */     this.fnMapper = fnMapper;
/*  43 */     this.varMapper = varMapper;
/*     */   }
/*     */   
/*     */   public ELContext getELContext() {
/*  47 */     return this.elContext;
/*     */   }
/*     */   
/*     */   public FunctionMapper getFunctionMapper()
/*     */   {
/*  52 */     return this.fnMapper;
/*     */   }
/*     */   
/*     */   public VariableMapper getVariableMapper()
/*     */   {
/*  57 */     return this.varMapper;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getContext(Class key)
/*     */   {
/*  63 */     return this.elContext.getContext(key);
/*     */   }
/*     */   
/*     */   public ELResolver getELResolver()
/*     */   {
/*  68 */     return this.elContext.getELResolver();
/*     */   }
/*     */   
/*     */   public boolean isPropertyResolved()
/*     */   {
/*  73 */     return this.elContext.isPropertyResolved();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void putContext(Class key, Object contextObject)
/*     */   {
/*  80 */     this.elContext.putContext(key, contextObject);
/*     */   }
/*     */   
/*     */   public void setPropertyResolved(boolean resolved)
/*     */   {
/*  85 */     this.elContext.setPropertyResolved(resolved);
/*     */   }
/*     */   
/*     */   public Locale getLocale()
/*     */   {
/*  90 */     return this.elContext.getLocale();
/*     */   }
/*     */   
/*     */   public void setLocale(Locale locale)
/*     */   {
/*  95 */     this.elContext.setLocale(locale);
/*     */   }
/*     */   
/*     */   public void setPropertyResolved(Object base, Object property)
/*     */   {
/* 100 */     this.elContext.setPropertyResolved(base, property);
/*     */   }
/*     */   
/*     */   public ImportHandler getImportHandler()
/*     */   {
/* 105 */     return this.elContext.getImportHandler();
/*     */   }
/*     */   
/*     */   public void addEvaluationListener(EvaluationListener listener)
/*     */   {
/* 110 */     this.elContext.addEvaluationListener(listener);
/*     */   }
/*     */   
/*     */   public List<EvaluationListener> getEvaluationListeners()
/*     */   {
/* 115 */     return this.elContext.getEvaluationListeners();
/*     */   }
/*     */   
/*     */   public void notifyBeforeEvaluation(String expression)
/*     */   {
/* 120 */     this.elContext.notifyBeforeEvaluation(expression);
/*     */   }
/*     */   
/*     */   public void notifyAfterEvaluation(String expression)
/*     */   {
/* 125 */     this.elContext.notifyAfterEvaluation(expression);
/*     */   }
/*     */   
/*     */   public void notifyPropertyResolved(Object base, Object property)
/*     */   {
/* 130 */     this.elContext.notifyPropertyResolved(base, property);
/*     */   }
/*     */   
/*     */   public boolean isLambdaArgument(String name)
/*     */   {
/* 135 */     return this.elContext.isLambdaArgument(name);
/*     */   }
/*     */   
/*     */   public Object getLambdaArgument(String name)
/*     */   {
/* 140 */     return this.elContext.getLambdaArgument(name);
/*     */   }
/*     */   
/*     */   public void enterLambdaScope(Map<String, Object> arguments)
/*     */   {
/* 145 */     this.elContext.enterLambdaScope(arguments);
/*     */   }
/*     */   
/*     */   public void exitLambdaScope()
/*     */   {
/* 150 */     this.elContext.exitLambdaScope();
/*     */   }
/*     */   
/*     */   public Object convertToType(Object obj, Class<?> type)
/*     */   {
/* 155 */     return this.elContext.convertToType(obj, type);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\lang\EvaluationContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */