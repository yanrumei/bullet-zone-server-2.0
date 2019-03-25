/*    */ package org.apache.el.parser;
/*    */ 
/*    */ import java.math.BigInteger;
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
/*    */ public final class AstInteger
/*    */   extends SimpleNode
/*    */ {
/*    */   private volatile Number number;
/*    */   
/*    */   public AstInteger(int id)
/*    */   {
/* 33 */     super(id);
/*    */   }
/*    */   
/*    */ 
/*    */   protected Number getInteger()
/*    */   {
/* 39 */     if (this.number == null) {
/*    */       try {
/* 41 */         this.number = Long.valueOf(this.image);
/*    */       } catch (ArithmeticException e1) {
/* 43 */         this.number = new BigInteger(this.image);
/*    */       }
/*    */     }
/* 46 */     return this.number;
/*    */   }
/*    */   
/*    */   public Class<?> getType(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 52 */     return getInteger().getClass();
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 58 */     return getInteger();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstInteger.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */