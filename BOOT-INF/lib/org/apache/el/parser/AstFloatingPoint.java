/*    */ package org.apache.el.parser;
/*    */ 
/*    */ import java.math.BigDecimal;
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
/*    */ public final class AstFloatingPoint
/*    */   extends SimpleNode
/*    */ {
/*    */   private volatile Number number;
/*    */   
/*    */   public AstFloatingPoint(int id)
/*    */   {
/* 33 */     super(id);
/*    */   }
/*    */   
/*    */ 
/*    */   public Number getFloatingPoint()
/*    */   {
/* 39 */     if (this.number == null) {
/*    */       try {
/* 41 */         this.number = Double.valueOf(this.image);
/*    */       } catch (ArithmeticException e0) {
/* 43 */         this.number = new BigDecimal(this.image);
/*    */       }
/*    */     }
/* 46 */     return this.number;
/*    */   }
/*    */   
/*    */   public Object getValue(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 52 */     return getFloatingPoint();
/*    */   }
/*    */   
/*    */   public Class<?> getType(EvaluationContext ctx)
/*    */     throws ELException
/*    */   {
/* 58 */     return getFloatingPoint().getClass();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\el\parser\AstFloatingPoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */