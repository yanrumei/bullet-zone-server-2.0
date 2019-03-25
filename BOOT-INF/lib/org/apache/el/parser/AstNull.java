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
/*    */ public final class AstNull
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstNull(int id)
/*    */   {
/* 31 */     super(id);
/*    */   }
/*    */   
/*    */   public Class<?> getType(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 37 */     return null;
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 43 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstNull.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */