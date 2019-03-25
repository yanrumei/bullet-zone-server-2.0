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
/*    */ public class AstSemicolon
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstSemicolon(int id)
/*    */   {
/* 27 */     super(id);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 35 */     this.children[0].getValue(ctx);
/*    */     
/* 37 */     return this.children[1].getValue(ctx);
/*    */   }
/*    */   
/*    */ 
/*    */   public Class<?> getType(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 44 */     this.children[0].getType(ctx);
/*    */     
/* 46 */     return this.children[1].getType(ctx);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstSemicolon.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */