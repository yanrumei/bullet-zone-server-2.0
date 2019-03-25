/*    */ package org.hibernate.validator.internal.util.scriptengine;
/*    */ 
/*    */ import java.util.Map;
/*    */ import javax.script.ScriptEngine;
/*    */ import javax.script.ScriptEngineFactory;
/*    */ import javax.script.ScriptException;
/*    */ import javax.script.SimpleBindings;
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
/*    */ public class ScriptEvaluator
/*    */ {
/*    */   private final ScriptEngine engine;
/*    */   
/*    */   public ScriptEvaluator(ScriptEngine engine)
/*    */   {
/* 30 */     this.engine = engine;
/*    */   }
/*    */   
/*    */   /* Error */
/*    */   public Object evaluate(String script, Map<String, Object> bindings)
/*    */     throws ScriptException
/*    */   {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: invokespecial 3	org/hibernate/validator/internal/util/scriptengine/ScriptEvaluator:engineAllowsParallelAccessFromMultipleThreads	()Z
/*    */     //   4: ifeq +10 -> 14
/*    */     //   7: aload_0
/*    */     //   8: aload_1
/*    */     //   9: aload_2
/*    */     //   10: invokespecial 4	org/hibernate/validator/internal/util/scriptengine/ScriptEvaluator:doEvaluate	(Ljava/lang/String;Ljava/util/Map;)Ljava/lang/Object;
/*    */     //   13: areturn
/*    */     //   14: aload_0
/*    */     //   15: getfield 2	org/hibernate/validator/internal/util/scriptengine/ScriptEvaluator:engine	Ljavax/script/ScriptEngine;
/*    */     //   18: dup
/*    */     //   19: astore_3
/*    */     //   20: monitorenter
/*    */     //   21: aload_0
/*    */     //   22: aload_1
/*    */     //   23: aload_2
/*    */     //   24: invokespecial 4	org/hibernate/validator/internal/util/scriptengine/ScriptEvaluator:doEvaluate	(Ljava/lang/String;Ljava/util/Map;)Ljava/lang/Object;
/*    */     //   27: aload_3
/*    */     //   28: monitorexit
/*    */     //   29: areturn
/*    */     //   30: astore 4
/*    */     //   32: aload_3
/*    */     //   33: monitorexit
/*    */     //   34: aload 4
/*    */     //   36: athrow
/*    */     // Line number table:
/*    */     //   Java source line #45	-> byte code offset #0
/*    */     //   Java source line #46	-> byte code offset #7
/*    */     //   Java source line #49	-> byte code offset #14
/*    */     //   Java source line #50	-> byte code offset #21
/*    */     //   Java source line #51	-> byte code offset #30
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	37	0	this	ScriptEvaluator
/*    */     //   0	37	1	script	String
/*    */     //   0	37	2	bindings	Map<String, Object>
/*    */     //   19	14	3	Ljava/lang/Object;	Object
/*    */     //   30	5	4	localObject1	Object
/*    */     // Exception table:
/*    */     //   from	to	target	type
/*    */     //   21	29	30	finally
/*    */     //   30	34	30	finally
/*    */   }
/*    */   
/*    */   private Object doEvaluate(String script, Map<String, Object> bindings)
/*    */     throws ScriptException
/*    */   {
/* 56 */     return this.engine.eval(script, new SimpleBindings(bindings));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   private boolean engineAllowsParallelAccessFromMultipleThreads()
/*    */   {
/* 65 */     String threadingType = (String)this.engine.getFactory().getParameter("THREADING");
/*    */     
/* 67 */     return ("THREAD-ISOLATED".equals(threadingType)) || ("STATELESS".equals(threadingType));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\interna\\util\scriptengine\ScriptEvaluator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */