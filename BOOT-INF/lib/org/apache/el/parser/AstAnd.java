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
/*    */ public final class AstAnd
/*    */   extends BooleanNode
/*    */ {
/*    */   public AstAnd(int id)
/*    */   {
/* 31 */     super(id);
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 37 */     Object obj = this.children[0].getValue(ctx);
/* 38 */     Boolean b = coerceToBoolean(ctx, obj, true);
/* 39 */     if (!b.booleanValue()) {
/* 40 */       return b;
/*    */     }
/* 42 */     obj = this.children[1].getValue(ctx);
/* 43 */     b = coerceToBoolean(ctx, obj, true);
/* 44 */     return b;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstAnd.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */