/*    */ package org.apache.el.parser;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
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
/*    */ public class AstListData
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstListData(int id)
/*    */   {
/* 29 */     super(id);
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx) throws ELException
/*    */   {
/* 34 */     List<Object> result = new ArrayList();
/*    */     
/* 36 */     if (this.children != null) {
/* 37 */       for (Node child : this.children) {
/* 38 */         result.add(child.getValue(ctx));
/*    */       }
/*    */     }
/*    */     
/* 42 */     return result;
/*    */   }
/*    */   
/*    */   public Class<?> getType(EvaluationContext ctx) throws ELException
/*    */   {
/* 47 */     return List.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstListData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */