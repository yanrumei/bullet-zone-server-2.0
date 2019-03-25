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
/*    */ public class AstAssign
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstAssign(int id)
/*    */   {
/* 27 */     super(id);
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 33 */     Object value = this.children[1].getValue(ctx);
/*    */     
/* 35 */     this.children[0].setValue(ctx, value);
/*    */     
/* 37 */     return value;
/*    */   }
/*    */   
/*    */   public Class<?> getType(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 43 */     Object value = this.children[1].getValue(ctx);
/*    */     
/* 45 */     this.children[0].setValue(ctx, value);
/*    */     
/* 47 */     return this.children[1].getType(ctx);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstAssign.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */