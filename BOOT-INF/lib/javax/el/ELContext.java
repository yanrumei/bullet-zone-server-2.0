/*     */ package javax.el;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Deque;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
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
/*     */ public abstract class ELContext
/*     */ {
/*     */   private Locale locale;
/*     */   private Map<Class<?>, Object> map;
/*     */   private boolean resolved;
/*  36 */   private ImportHandler importHandler = null;
/*     */   
/*  38 */   private List<EvaluationListener> listeners = new ArrayList();
/*     */   
/*  40 */   private Deque<Map<String, Object>> lambdaArguments = new LinkedList();
/*     */   
/*     */   public ELContext() {
/*  43 */     this.resolved = false;
/*     */   }
/*     */   
/*     */   public void setPropertyResolved(boolean resolved) {
/*  47 */     this.resolved = resolved;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPropertyResolved(Object base, Object property)
/*     */   {
/*  59 */     setPropertyResolved(true);
/*  60 */     notifyPropertyResolved(base, property);
/*     */   }
/*     */   
/*     */   public boolean isPropertyResolved() {
/*  64 */     return this.resolved;
/*     */   }
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
/*     */   public void putContext(Class key, Object contextObject)
/*     */   {
/*  79 */     Objects.requireNonNull(key);
/*  80 */     Objects.requireNonNull(contextObject);
/*     */     
/*  82 */     if (this.map == null) {
/*  83 */       this.map = new HashMap();
/*     */     }
/*     */     
/*  86 */     this.map.put(key, contextObject);
/*     */   }
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
/*     */   public Object getContext(Class key)
/*     */   {
/* 101 */     Objects.requireNonNull(key);
/* 102 */     if (this.map == null) {
/* 103 */       return null;
/*     */     }
/* 105 */     return this.map.get(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ELResolver getELResolver();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ImportHandler getImportHandler()
/*     */   {
/* 119 */     if (this.importHandler == null) {
/* 120 */       this.importHandler = new ImportHandler();
/*     */     }
/* 122 */     return this.importHandler;
/*     */   }
/*     */   
/*     */   public abstract FunctionMapper getFunctionMapper();
/*     */   
/*     */   public Locale getLocale() {
/* 128 */     return this.locale;
/*     */   }
/*     */   
/*     */   public void setLocale(Locale locale) {
/* 132 */     this.locale = locale;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract VariableMapper getVariableMapper();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addEvaluationListener(EvaluationListener listener)
/*     */   {
/* 145 */     this.listeners.add(listener);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<EvaluationListener> getEvaluationListeners()
/*     */   {
/* 156 */     return this.listeners;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void notifyBeforeEvaluation(String expression)
/*     */   {
/* 167 */     for (EvaluationListener listener : this.listeners) {
/*     */       try {
/* 169 */         listener.beforeEvaluation(this, expression);
/*     */       } catch (Throwable t) {
/* 171 */         Util.handleThrowable(t);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void notifyAfterEvaluation(String expression)
/*     */   {
/* 185 */     for (EvaluationListener listener : this.listeners) {
/*     */       try {
/* 187 */         listener.afterEvaluation(this, expression);
/*     */       } catch (Throwable t) {
/* 189 */         Util.handleThrowable(t);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void notifyPropertyResolved(Object base, Object property)
/*     */   {
/* 204 */     for (EvaluationListener listener : this.listeners) {
/*     */       try {
/* 206 */         listener.propertyResolved(this, base, property);
/*     */       } catch (Throwable t) {
/* 208 */         Util.handleThrowable(t);
/*     */       }
/*     */     }
/*     */   }
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
/*     */   public boolean isLambdaArgument(String name)
/*     */   {
/* 226 */     for (Map<String, Object> arguments : this.lambdaArguments) {
/* 227 */       if (arguments.containsKey(name)) {
/* 228 */         return true;
/*     */       }
/*     */     }
/* 231 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getLambdaArgument(String name)
/*     */   {
/* 244 */     for (Map<String, Object> arguments : this.lambdaArguments) {
/* 245 */       Object result = arguments.get(name);
/* 246 */       if (result != null) {
/* 247 */         return result;
/*     */       }
/*     */     }
/* 250 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void enterLambdaScope(Map<String, Object> arguments)
/*     */   {
/* 262 */     this.lambdaArguments.push(arguments);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void exitLambdaScope()
/*     */   {
/* 272 */     this.lambdaArguments.pop();
/*     */   }
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
/*     */   public Object convertToType(Object obj, Class<?> type)
/*     */   {
/* 290 */     boolean originalResolved = isPropertyResolved();
/* 291 */     setPropertyResolved(false);
/*     */     try {
/* 293 */       ELResolver resolver = getELResolver();
/* 294 */       if (resolver != null) {
/* 295 */         Object result = resolver.convertToType(this, obj, type);
/* 296 */         if (isPropertyResolved()) {
/* 297 */           return result;
/*     */         }
/*     */       }
/*     */     } finally {
/* 301 */       setPropertyResolved(originalResolved);
/*     */     }
/*     */     
/* 304 */     return ELManager.getExpressionFactory().coerceToType(obj, type);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\ELContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */