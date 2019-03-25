/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.ExpressionState;
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
/*    */ public class Identifier
/*    */   extends SpelNodeImpl
/*    */ {
/*    */   private final TypedValue id;
/*    */   
/*    */   public Identifier(String payload, int pos)
/*    */   {
/* 32 */     super(pos, new SpelNodeImpl[0]);
/* 33 */     this.id = new TypedValue(payload);
/*    */   }
/*    */   
/*    */ 
/*    */   public String toStringAST()
/*    */   {
/* 39 */     return (String)this.id.getValue();
/*    */   }
/*    */   
/*    */   public TypedValue getValueInternal(ExpressionState state)
/*    */   {
/* 44 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\ast\Identifier.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */