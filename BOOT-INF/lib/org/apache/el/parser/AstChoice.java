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
/*    */ public final class AstChoice
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstChoice(int id)
/*    */   {
/* 31 */     super(id);
/*    */   }
/*    */   
/*    */   public Class<?> getType(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 37 */     Object val = getValue(ctx);
/* 38 */     return val != null ? val.getClass() : null;
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 44 */     Object obj0 = this.children[0].getValue(ctx);
/* 45 */     Boolean b0 = coerceToBoolean(ctx, obj0, true);
/* 46 */     return this.children[2].getValue(ctx);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstChoice.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */