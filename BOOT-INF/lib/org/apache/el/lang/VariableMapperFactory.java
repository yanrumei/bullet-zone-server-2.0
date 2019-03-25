/*    */ package org.apache.el.lang;
/*    */ 
/*    */ import javax.el.ValueExpression;
/*    */ import javax.el.VariableMapper;
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
/*    */ public class VariableMapperFactory
/*    */   extends VariableMapper
/*    */ {
/*    */   private final VariableMapper target;
/*    */   private VariableMapper momento;
/*    */   
/*    */   public VariableMapperFactory(VariableMapper target)
/*    */   {
/* 29 */     if (target == null) {
/* 30 */       throw new NullPointerException("Target VariableMapper cannot be null");
/*    */     }
/* 32 */     this.target = target;
/*    */   }
/*    */   
/*    */   public VariableMapper create() {
/* 36 */     return this.momento;
/*    */   }
/*    */   
/*    */   public ValueExpression resolveVariable(String variable)
/*    */   {
/* 41 */     ValueExpression expr = this.target.resolveVariable(variable);
/* 42 */     if (expr != null) {
/* 43 */       if (this.momento == null) {
/* 44 */         this.momento = new VariableMapperImpl();
/*    */       }
/* 46 */       this.momento.setVariable(variable, expr);
/*    */     }
/* 48 */     return expr;
/*    */   }
/*    */   
/*    */   public ValueExpression setVariable(String variable, ValueExpression expression)
/*    */   {
/* 53 */     throw new UnsupportedOperationException("Cannot Set Variables on Factory");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\lang\VariableMapperFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */