/*    */ package org.hibernate.validator.internal.util.scriptengine;
/*    */ 
/*    */ import java.lang.ref.Reference;
/*    */ import java.lang.ref.SoftReference;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import javax.script.ScriptEngine;
/*    */ import javax.script.ScriptEngineManager;
/*    */ import javax.script.ScriptException;
/*    */ import org.hibernate.validator.internal.util.logging.Messages;
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
/*    */ public class ScriptEvaluatorFactory
/*    */ {
/* 32 */   private static Reference<ScriptEvaluatorFactory> INSTANCE = new SoftReference(new ScriptEvaluatorFactory());
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 37 */   private final ConcurrentMap<String, ScriptEvaluator> scriptExecutorCache = new ConcurrentHashMap();
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public static synchronized ScriptEvaluatorFactory getInstance()
/*    */   {
/* 48 */     ScriptEvaluatorFactory theValue = (ScriptEvaluatorFactory)INSTANCE.get();
/*    */     
/* 50 */     if (theValue == null) {
/* 51 */       theValue = new ScriptEvaluatorFactory();
/* 52 */       INSTANCE = new SoftReference(theValue);
/*    */     }
/*    */     
/* 55 */     return theValue;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public ScriptEvaluator getScriptEvaluatorByLanguageName(String languageName)
/*    */     throws ScriptException
/*    */   {
/* 68 */     if (!this.scriptExecutorCache.containsKey(languageName))
/*    */     {
/* 70 */       ScriptEvaluator scriptExecutor = createNewScriptEvaluator(languageName);
/* 71 */       this.scriptExecutorCache.putIfAbsent(languageName, scriptExecutor);
/*    */     }
/*    */     
/* 74 */     return (ScriptEvaluator)this.scriptExecutorCache.get(languageName);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private ScriptEvaluator createNewScriptEvaluator(String languageName)
/*    */     throws ScriptException
/*    */   {
/* 87 */     ScriptEngine engine = new ScriptEngineManager().getEngineByName(languageName);
/*    */     
/* 89 */     if (engine == null) {
/* 90 */       throw new ScriptException(Messages.MESSAGES.unableToFindScriptEngine(languageName));
/*    */     }
/*    */     
/* 93 */     return new ScriptEvaluator(engine);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\scriptengine\ScriptEvaluatorFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */