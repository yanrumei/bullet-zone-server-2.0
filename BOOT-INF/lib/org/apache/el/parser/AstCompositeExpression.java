/*    */ package org.apache.el.parser;
/*    */ 
/*    */ import javax.el.ELException;
/*    */ import org.apache.el.lang.ELSupport;
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
/*    */ 
/*    */ public final class AstCompositeExpression
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstCompositeExpression(int id)
/*    */   {
/* 33 */     super(id);
/*    */   }
/*    */   
/*    */   public Class<?> getType(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 39 */     return String.class;
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 45 */     StringBuilder sb = new StringBuilder(16);
/* 46 */     Object obj = null;
/* 47 */     if (this.children != null) {
/* 48 */       for (int i = 0; i < this.children.length; i++) {
/* 49 */         obj = this.children[i].getValue(ctx);
/* 50 */         if (obj != null) {
/* 51 */           sb.append(ELSupport.coerceToString(ctx, obj));
/*    */         }
/*    */       }
/*    */     }
/* 55 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstCompositeExpression.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */