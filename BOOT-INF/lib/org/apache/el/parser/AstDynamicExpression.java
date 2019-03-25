/*    */ package org.apache.el.parser;
/*    */ 
/*    */ import javax.el.ELException;
/*    */ import org.apache.el.lang.EvaluationContext;
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
/*    */ public final class AstDynamicExpression
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstDynamicExpression(int id)
/*    */   {
/* 31 */     super(id);
/*    */   }
/*    */   
/*    */   public Class<?> getType(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 37 */     return this.children[0].getType(ctx);
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 43 */     return this.children[0].getValue(ctx);
/*    */   }
/*    */   
/*    */   public boolean isReadOnly(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 49 */     return this.children[0].isReadOnly(ctx);
/*    */   }
/*    */   
/*    */   public void setValue(EvaluationContext ctx, Object value)
/*    */     throws ELException
/*    */   {
/* 55 */     this.children[0].setValue(ctx, value);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstDynamicExpression.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */