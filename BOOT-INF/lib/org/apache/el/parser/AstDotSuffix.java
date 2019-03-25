/*    */ package org.apache.el.parser;
/*    */ 
/*    */ import javax.el.ELException;
/*    */ import org.apache.el.lang.EvaluationContext;
/*    */ import org.apache.el.util.MessageFactory;
/*    */ import org.apache.el.util.Validation;
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
/*    */ public final class AstDotSuffix
/*    */   extends SimpleNode
/*    */ {
/*    */   public AstDotSuffix(int id)
/*    */   {
/* 33 */     super(id);
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 39 */     return this.image;
/*    */   }
/*    */   
/*    */   public void setImage(String image)
/*    */   {
/* 44 */     if (!Validation.isIdentifier(image)) {
/* 45 */       throw new ELException(MessageFactory.get("error.identifier.notjava", new Object[] { image }));
/*    */     }
/*    */     
/* 48 */     this.image = image;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstDotSuffix.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */