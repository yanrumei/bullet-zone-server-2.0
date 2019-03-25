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
/*    */ public final class AstEqual
/*    */   extends BooleanNode
/*    */ {
/*    */   public AstEqual(int id)
/*    */   {
/* 31 */     super(id);
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 37 */     Object obj0 = this.children[0].getValue(ctx);
/* 38 */     Object obj1 = this.children[1].getValue(ctx);
/* 39 */     return Boolean.valueOf(equals(ctx, obj0, obj1));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstEqual.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */