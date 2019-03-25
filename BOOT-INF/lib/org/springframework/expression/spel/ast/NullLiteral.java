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
/*    */ public class NullLiteral
/*    */   extends Literal
/*    */ {
/*    */   public NullLiteral(int pos)
/*    */   {
/* 32 */     super(null, pos);
/* 33 */     this.exitTypeDescriptor = "Ljava/lang/Object";
/*    */   }
/*    */   
/*    */ 
/*    */   public TypedValue getLiteralValue()
/*    */   {
/* 39 */     return TypedValue.NULL;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 44 */     return "null";
/*    */   }
/*    */   
/*    */   public boolean isCompilable()
/*    */   {
/* 49 */     return true;
/*    */   }
/*    */   
/*    */   public void generateCode(MethodVisitor mv, CodeFlow cf)
/*    */   {
/* 54 */     mv.visitInsn(1);
/* 55 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\ast\NullLiteral.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */