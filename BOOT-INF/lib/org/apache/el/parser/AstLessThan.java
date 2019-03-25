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
/*    */ public final class AstLessThan
/*    */   extends BooleanNode
/*    */ {
/*    */   public AstLessThan(int id)
/*    */   {
/* 31 */     super(id);
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 37 */     Object obj0 = this.children[0].getValue(ctx);
/* 38 */     if (obj0 == null) {
/* 39 */       return Boolean.FALSE;
/*    */     }
/* 41 */     Object obj1 = this.children[1].getValue(ctx);
/* 42 */     if (obj1 == null) {
/* 43 */       return Boolean.FALSE;
/*    */     }
/* 45 */     return compare(ctx, obj0, obj1) < 0 ? Boolean.TRUE : Boolean.FALSE;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstLessThan.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */