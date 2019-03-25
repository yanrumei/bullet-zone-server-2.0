/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.asm.MethodVisitor;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.CodeFlow;
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
/*    */ public class RealLiteral
/*    */   extends Literal
/*    */ {
/*    */   private final TypedValue value;
/*    */   
/*    */   public RealLiteral(String payload, int pos, double value)
/*    */   {
/* 35 */     super(payload, pos);
/* 36 */     this.value = new TypedValue(Double.valueOf(value));
/* 37 */     this.exitTypeDescriptor = "D";
/*    */   }
/*    */   
/*    */ 
/*    */   public TypedValue getLiteralValue()
/*    */   {
/* 43 */     return this.value;
/*    */   }
/*    */   
/*    */   public boolean isCompilable()
/*    */   {
/* 48 */     return true;
/*    */   }
/*    */   
/*    */   public void generateCode(MethodVisitor mv, CodeFlow cf)
/*    */   {
/* 53 */     mv.visitLdcInsn(this.value.getValue());
/* 54 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\ast\RealLiteral.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */