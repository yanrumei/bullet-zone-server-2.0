/*    */ package org.apache.el.parser;
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
/*    */ public class AstLambdaParameters
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstLambdaParameters(int id)
/*    */   {
/* 23 */     super(id);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 30 */     StringBuilder result = new StringBuilder();
/* 31 */     result.append('(');
/* 32 */     if (this.children != null) {
/* 33 */       for (Node n : this.children) {
/* 34 */         result.append(n.toString());
/* 35 */         result.append(',');
/*    */       }
/*    */     }
/* 38 */     result.append(")->");
/* 39 */     return result.toString();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstLambdaParameters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */