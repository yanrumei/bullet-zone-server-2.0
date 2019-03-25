/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import org.springframework.asm.MethodVisitor;
/*    */ import org.springframework.expression.EvaluationContext;
/*    */ import org.springframework.expression.EvaluationException;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.CodeFlow;
/*    */ import org.springframework.expression.spel.ExpressionState;
/*    */ import org.springframework.expression.spel.support.BooleanTypedValue;
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
/*    */ public class OpEQ
/*    */   extends Operator
/*    */ {
/*    */   public OpEQ(int pos, SpelNodeImpl... operands)
/*    */   {
/* 35 */     super("==", pos, operands);
/* 36 */     this.exitTypeDescriptor = "Z";
/*    */   }
/*    */   
/*    */   public BooleanTypedValue getValueInternal(ExpressionState state)
/*    */     throws EvaluationException
/*    */   {
/* 42 */     Object left = getLeftOperand().getValueInternal(state).getValue();
/* 43 */     Object right = getRightOperand().getValueInternal(state).getValue();
/* 44 */     this.leftActualDescriptor = CodeFlow.toDescriptorFromObject(left);
/* 45 */     this.rightActualDescriptor = CodeFlow.toDescriptorFromObject(right);
/* 46 */     return BooleanTypedValue.forValue(
/* 47 */       equalityCheck(state.getEvaluationContext(), left, right));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isCompilable()
/*    */   {
/* 54 */     SpelNodeImpl left = getLeftOperand();
/* 55 */     SpelNodeImpl right = getRightOperand();
/* 56 */     if ((!left.isCompilable()) || (!right.isCompilable())) {
/* 57 */       return false;
/*    */     }
/*    */     
/* 60 */     String leftDesc = left.exitTypeDescriptor;
/* 61 */     String rightDesc = right.exitTypeDescriptor;
/* 62 */     Operator.DescriptorComparison dc = Operator.DescriptorComparison.checkNumericCompatibility(leftDesc, rightDesc, this.leftActualDescriptor, this.rightActualDescriptor);
/*    */     
/* 64 */     return (!dc.areNumbers) || (dc.areCompatible);
/*    */   }
/*    */   
/*    */   public void generateCode(MethodVisitor mv, CodeFlow cf)
/*    */   {
/* 69 */     cf.loadEvaluationContext(mv);
/* 70 */     String leftDesc = getLeftOperand().exitTypeDescriptor;
/* 71 */     String rightDesc = getRightOperand().exitTypeDescriptor;
/* 72 */     boolean leftPrim = CodeFlow.isPrimitive(leftDesc);
/* 73 */     boolean rightPrim = CodeFlow.isPrimitive(rightDesc);
/*    */     
/* 75 */     cf.enterCompilationScope();
/* 76 */     getLeftOperand().generateCode(mv, cf);
/* 77 */     cf.exitCompilationScope();
/* 78 */     if (leftPrim) {
/* 79 */       CodeFlow.insertBoxIfNecessary(mv, leftDesc.charAt(0));
/*    */     }
/* 81 */     cf.enterCompilationScope();
/* 82 */     getRightOperand().generateCode(mv, cf);
/* 83 */     cf.exitCompilationScope();
/* 84 */     if (rightPrim) {
/* 85 */       CodeFlow.insertBoxIfNecessary(mv, rightDesc.charAt(0));
/*    */     }
/*    */     
/* 88 */     String operatorClassName = Operator.class.getName().replace('.', '/');
/* 89 */     String evaluationContextClassName = EvaluationContext.class.getName().replace('.', '/');
/* 90 */     mv.visitMethodInsn(184, operatorClassName, "equalityCheck", "(L" + evaluationContextClassName + ";Ljava/lang/Object;Ljava/lang/Object;)Z", false);
/*    */     
/* 92 */     cf.pushDescriptor("Z");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\ast\OpEQ.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */