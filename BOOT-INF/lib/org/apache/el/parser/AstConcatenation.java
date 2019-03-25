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
/*    */ public class AstConcatenation
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstConcatenation(int id)
/*    */   {
/* 27 */     super(id);
/*    */   }
/*    */   
/*    */ 
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 34 */     String s1 = coerceToString(ctx, this.children[0].getValue(ctx));
/* 35 */     String s2 = coerceToString(ctx, this.children[1].getValue(ctx));
/* 36 */     return s1 + s2;
/*    */   }
/*    */   
/*    */   public Class<?> getType(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 42 */     return String.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstConcatenation.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */