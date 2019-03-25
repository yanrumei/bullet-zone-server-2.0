/*    */ package org.apache.el.parser;
/*    */ 
/*    */ import java.util.HashSet;
/*    */ import java.util.Set;
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
/*    */ public class AstSetData
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstSetData(int id)
/*    */   {
/* 30 */     super(id);
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx) throws ELException
/*    */   {
/* 35 */     Set<Object> result = new HashSet();
/*    */     
/* 37 */     if (this.children != null) {
/* 38 */       for (Node child : this.children) {
/* 39 */         result.add(child.getValue(ctx));
/*    */       }
/*    */     }
/*    */     
/* 43 */     return result;
/*    */   }
/*    */   
/*    */   public Class<?> getType(EvaluationContext ctx) throws ELException
/*    */   {
/* 48 */     return Set.class;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstSetData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */