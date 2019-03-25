/*    */ package org.springframework.cache.interceptor;
/*    */ 
/*    */ import org.springframework.expression.EvaluationException;
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
/*    */ class VariableNotAvailableException
/*    */   extends EvaluationException
/*    */ {
/*    */   private final String name;
/*    */   
/*    */   public VariableNotAvailableException(String name)
/*    */   {
/* 34 */     super("Variable '" + name + "' is not available");
/* 35 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getName()
/*    */   {
/* 40 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\cache\interceptor\VariableNotAvailableException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */