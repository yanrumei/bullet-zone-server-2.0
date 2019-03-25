/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Elvis
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   public Elvis(int pos, SpelNodeImpl... args)
/*     */   {
/*  38 */     super(pos, args);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypedValue getValueInternal(ExpressionState state)
/*     */     throws EvaluationException
/*     */   {
/*  51 */     TypedValue value = this.children[0].getValueInternal(state);
/*     */     
/*  53 */     if (!StringUtils.isEmpty(value.getValue())) {
/*  54 */       return value;
/*     */     }
/*     */     
/*  57 */     TypedValue result = this.children[1].getValueInternal(state);
/*  58 */     computeExitTypeDescriptor();
/*  59 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public String toStringAST()
/*     */   {
/*  65 */     return getChild(0).toStringAST() + " ?: " + getChild(1).toStringAST();
/*     */   }
/*     */   
/*     */   public boolean isCompilable()
/*     */   {
/*  70 */     SpelNodeImpl condition = this.children[0];
/*  71 */     SpelNodeImpl ifNullValue = this.children[1];
/*  72 */     return (condition.isCompilable()) && (ifNullValue.isCompilable()) && (condition.exitTypeDescriptor != null) && (ifNullValue.exitTypeDescriptor != null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf)
/*     */   {
/*  79 */     computeExitTypeDescriptor();
/*  80 */     this.children[0].generateCode(mv, cf);
/*  81 */     CodeFlow.insertBoxIfNecessary(mv, cf.lastDescriptor().charAt(0));
/*  82 */     Label elseTarget = new Label();
/*  83 */     Label endOfIf = new Label();
/*  84 */     mv.visitInsn(89);
/*  85 */     mv.visitJumpInsn(198, elseTarget);
/*     */     
/*  87 */     mv.visitInsn(89);
/*  88 */     mv.visitLdcInsn("");
/*  89 */     mv.visitInsn(95);
/*  90 */     mv.visitMethodInsn(182, "java/lang/String", "equals", "(Ljava/lang/Object;)Z", false);
/*  91 */     mv.visitJumpInsn(153, endOfIf);
/*  92 */     mv.visitLabel(elseTarget);
/*  93 */     mv.visitInsn(87);
/*  94 */     this.children[1].generateCode(mv, cf);
/*  95 */     if (!CodeFlow.isPrimitive(this.exitTypeDescriptor)) {
/*  96 */       CodeFlow.insertBoxIfNecessary(mv, cf.lastDescriptor().charAt(0));
/*     */     }
/*  98 */     mv.visitLabel(endOfIf);
/*  99 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */   
/*     */   private void computeExitTypeDescriptor() {
/* 103 */     if ((this.exitTypeDescriptor == null) && (this.children[0].exitTypeDescriptor != null) && (this.children[1].exitTypeDescriptor != null))
/*     */     {
/* 105 */       String conditionDescriptor = this.children[0].exitTypeDescriptor;
/* 106 */       String ifNullValueDescriptor = this.children[1].exitTypeDescriptor;
/* 107 */       if (conditionDescriptor.equals(ifNullValueDescriptor)) {
/* 108 */         this.exitTypeDescriptor = conditionDescriptor;
/*     */       }
/*     */       else
/*     */       {
/* 112 */         this.exitTypeDescriptor = "Ljava/lang/Object";
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\ast\Elvis.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */