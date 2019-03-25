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
/*    */ public final class AstBracketSuffix
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstBracketSuffix(int id)
/*    */   {
/* 31 */     super(id);
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 37 */     return this.children[0].getValue(ctx);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstBracketSuffix.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */