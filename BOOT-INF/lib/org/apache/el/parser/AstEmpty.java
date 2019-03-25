/*    */ package org.apache.el.parser;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ public final class AstEmpty
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstEmpty(int id)
/*    */   {
/* 34 */     super(id);
/*    */   }
/*    */   
/*    */   public Class<?> getType(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 40 */     return Boolean.class;
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 46 */     Object obj = this.children[0].getValue(ctx);
/* 47 */     if (obj == null)
/* 48 */       return Boolean.TRUE;
/* 49 */     if ((obj instanceof String))
/* 50 */       return Boolean.valueOf(((String)obj).length() == 0);
/* 51 */     if ((obj instanceof Object[]))
/* 52 */       return Boolean.valueOf(((Object[])obj).length == 0);
/* 53 */     if ((obj instanceof Collection))
/* 54 */       return Boolean.valueOf(((Collection)obj).isEmpty());
/* 55 */     if ((obj instanceof Map)) {
/* 56 */       return Boolean.valueOf(((Map)obj).isEmpty());
/*    */     }
/* 58 */     return Boolean.FALSE;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstEmpty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */