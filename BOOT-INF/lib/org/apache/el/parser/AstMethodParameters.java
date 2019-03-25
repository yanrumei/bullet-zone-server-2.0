/*    */ package org.apache.el.parser;
/*    */ 
/*    */ import java.util.ArrayList;
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
/*    */ public final class AstMethodParameters
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstMethodParameters(int id)
/*    */   {
/* 27 */     super(id);
/*    */   }
/*    */   
/*    */   public Object[] getParameters(EvaluationContext ctx) {
/* 31 */     ArrayList<Object> params = new ArrayList();
/* 32 */     for (int i = 0; i < jjtGetNumChildren(); i++) {
/* 33 */       params.add(jjtGetChild(i).getValue(ctx));
/*    */     }
/* 35 */     return params.toArray(new Object[params.size()]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 42 */     StringBuilder result = new StringBuilder();
/* 43 */     result.append('(');
/* 44 */     if (this.children != null) {
/* 45 */       for (Node n : this.children) {
/* 46 */         result.append(n.toString());
/* 47 */         result.append(',');
/*    */       }
/*    */     }
/* 50 */     result.append(')');
/* 51 */     return result.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstMethodParameters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */