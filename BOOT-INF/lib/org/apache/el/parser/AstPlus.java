/*    */ package org.apache.el.parser;
/*    */ 
/*    */ import javax.el.ELException;
/*    */ import org.apache.el.lang.ELArithmetic;
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
/*    */ public final class AstPlus
/*    */   extends ArithmeticNode
/*    */ {
/*    */   public AstPlus(int id)
/*    */   {
/* 32 */     super(id);
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 38 */     Object obj0 = this.children[0].getValue(ctx);
/* 39 */     Object obj1 = this.children[1].getValue(ctx);
/* 40 */     return ELArithmetic.add(obj0, obj1);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstPlus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */