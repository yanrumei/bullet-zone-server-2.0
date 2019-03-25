/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import org.springframework.asm.Label;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.TypeComparator;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.NumberUtils;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ public abstract class Operator
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final String operatorName;
/*     */   protected String leftActualDescriptor;
/*     */   protected String rightActualDescriptor;
/*     */   
/*     */   public Operator(String payload, int pos, SpelNodeImpl... operands)
/*     */   {
/*  55 */     super(pos, operands);
/*  56 */     this.operatorName = payload;
/*     */   }
/*     */   
/*     */   public SpelNodeImpl getLeftOperand()
/*     */   {
/*  61 */     return this.children[0];
/*     */   }
/*     */   
/*     */   public SpelNodeImpl getRightOperand() {
/*  65 */     return this.children[1];
/*     */   }
/*     */   
/*     */   public final String getOperatorName() {
/*  69 */     return this.operatorName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toStringAST()
/*     */   {
/*  77 */     StringBuilder sb = new StringBuilder("(");
/*  78 */     sb.append(getChild(0).toStringAST());
/*  79 */     for (int i = 1; i < getChildCount(); i++) {
/*  80 */       sb.append(" ").append(getOperatorName()).append(" ");
/*  81 */       sb.append(getChild(i).toStringAST());
/*     */     }
/*  83 */     sb.append(")");
/*  84 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected boolean isCompilableOperatorUsingNumerics()
/*     */   {
/*  89 */     SpelNodeImpl left = getLeftOperand();
/*  90 */     SpelNodeImpl right = getRightOperand();
/*  91 */     if ((!left.isCompilable()) || (!right.isCompilable())) {
/*  92 */       return false;
/*     */     }
/*     */     
/*     */ 
/*  96 */     String leftDesc = left.exitTypeDescriptor;
/*  97 */     String rightDesc = right.exitTypeDescriptor;
/*  98 */     DescriptorComparison dc = DescriptorComparison.checkNumericCompatibility(leftDesc, rightDesc, this.leftActualDescriptor, this.rightActualDescriptor);
/*     */     
/* 100 */     return (dc.areNumbers) && (dc.areCompatible);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void generateComparisonCode(MethodVisitor mv, CodeFlow cf, int compInstruction1, int compInstruction2)
/*     */   {
/* 108 */     String leftDesc = getLeftOperand().exitTypeDescriptor;
/* 109 */     String rightDesc = getRightOperand().exitTypeDescriptor;
/*     */     
/* 111 */     boolean unboxLeft = !CodeFlow.isPrimitive(leftDesc);
/* 112 */     boolean unboxRight = !CodeFlow.isPrimitive(rightDesc);
/* 113 */     DescriptorComparison dc = DescriptorComparison.checkNumericCompatibility(leftDesc, rightDesc, this.leftActualDescriptor, this.rightActualDescriptor);
/*     */     
/* 115 */     char targetType = dc.compatibleType;
/*     */     
/* 117 */     cf.enterCompilationScope();
/* 118 */     getLeftOperand().generateCode(mv, cf);
/* 119 */     cf.exitCompilationScope();
/* 120 */     if (unboxLeft) {
/* 121 */       CodeFlow.insertUnboxInsns(mv, targetType, leftDesc);
/*     */     }
/*     */     
/* 124 */     cf.enterCompilationScope();
/* 125 */     getRightOperand().generateCode(mv, cf);
/* 126 */     cf.exitCompilationScope();
/* 127 */     if (unboxRight) {
/* 128 */       CodeFlow.insertUnboxInsns(mv, targetType, rightDesc);
/*     */     }
/*     */     
/*     */ 
/* 132 */     Label elseTarget = new Label();
/* 133 */     Label endOfIf = new Label();
/* 134 */     if (targetType == 'D') {
/* 135 */       mv.visitInsn(152);
/* 136 */       mv.visitJumpInsn(compInstruction1, elseTarget);
/*     */     }
/* 138 */     else if (targetType == 'F') {
/* 139 */       mv.visitInsn(150);
/* 140 */       mv.visitJumpInsn(compInstruction1, elseTarget);
/*     */     }
/* 142 */     else if (targetType == 'J') {
/* 143 */       mv.visitInsn(148);
/* 144 */       mv.visitJumpInsn(compInstruction1, elseTarget);
/*     */     }
/* 146 */     else if (targetType == 'I') {
/* 147 */       mv.visitJumpInsn(compInstruction2, elseTarget);
/*     */     }
/*     */     else {
/* 150 */       throw new IllegalStateException("Unexpected descriptor " + leftDesc);
/*     */     }
/*     */     
/*     */ 
/* 154 */     mv.visitInsn(4);
/* 155 */     mv.visitJumpInsn(167, endOfIf);
/* 156 */     mv.visitLabel(elseTarget);
/* 157 */     mv.visitInsn(3);
/* 158 */     mv.visitLabel(endOfIf);
/* 159 */     cf.pushDescriptor("Z");
/*     */   }
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
/*     */   public static boolean equalityCheck(EvaluationContext context, Object left, Object right)
/*     */   {
/* 173 */     if (((left instanceof Number)) && ((right instanceof Number))) {
/* 174 */       Number leftNumber = (Number)left;
/* 175 */       Number rightNumber = (Number)right;
/*     */       
/* 177 */       if (((leftNumber instanceof BigDecimal)) || ((rightNumber instanceof BigDecimal))) {
/* 178 */         BigDecimal leftBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(leftNumber, BigDecimal.class);
/* 179 */         BigDecimal rightBigDecimal = (BigDecimal)NumberUtils.convertNumberToTargetClass(rightNumber, BigDecimal.class);
/* 180 */         return rightBigDecimal == null;
/*     */       }
/* 182 */       if (((leftNumber instanceof Double)) || ((rightNumber instanceof Double))) {
/* 183 */         return leftNumber.doubleValue() == rightNumber.doubleValue();
/*     */       }
/* 185 */       if (((leftNumber instanceof Float)) || ((rightNumber instanceof Float))) {
/* 186 */         return leftNumber.floatValue() == rightNumber.floatValue();
/*     */       }
/* 188 */       if (((leftNumber instanceof BigInteger)) || ((rightNumber instanceof BigInteger))) {
/* 189 */         BigInteger leftBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(leftNumber, BigInteger.class);
/* 190 */         BigInteger rightBigInteger = (BigInteger)NumberUtils.convertNumberToTargetClass(rightNumber, BigInteger.class);
/* 191 */         return rightBigInteger == null;
/*     */       }
/* 193 */       if (((leftNumber instanceof Long)) || ((rightNumber instanceof Long))) {
/* 194 */         return leftNumber.longValue() == rightNumber.longValue();
/*     */       }
/* 196 */       if (((leftNumber instanceof Integer)) || ((rightNumber instanceof Integer))) {
/* 197 */         return leftNumber.intValue() == rightNumber.intValue();
/*     */       }
/* 199 */       if (((leftNumber instanceof Short)) || ((rightNumber instanceof Short))) {
/* 200 */         return leftNumber.shortValue() == rightNumber.shortValue();
/*     */       }
/* 202 */       if (((leftNumber instanceof Byte)) || ((rightNumber instanceof Byte))) {
/* 203 */         return leftNumber.byteValue() == rightNumber.byteValue();
/*     */       }
/*     */       
/*     */ 
/* 207 */       return leftNumber.doubleValue() == rightNumber.doubleValue();
/*     */     }
/*     */     
/*     */ 
/* 211 */     if (((left instanceof CharSequence)) && ((right instanceof CharSequence))) {
/* 212 */       return left.toString().equals(right.toString());
/*     */     }
/*     */     
/* 215 */     if (ObjectUtils.nullSafeEquals(left, right)) {
/* 216 */       return true;
/*     */     }
/*     */     
/* 219 */     if (((left instanceof Comparable)) && ((right instanceof Comparable))) {
/* 220 */       Class<?> ancestor = ClassUtils.determineCommonAncestor(left.getClass(), right.getClass());
/* 221 */       if ((ancestor != null) && (Comparable.class.isAssignableFrom(ancestor))) {
/* 222 */         return context.getTypeComparator().compare(left, right) == 0;
/*     */       }
/*     */     }
/*     */     
/* 226 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static class DescriptorComparison
/*     */   {
/* 236 */     static DescriptorComparison NOT_NUMBERS = new DescriptorComparison(false, false, ' ');
/*     */     
/* 238 */     static DescriptorComparison INCOMPATIBLE_NUMBERS = new DescriptorComparison(true, false, ' ');
/*     */     
/*     */     final boolean areNumbers;
/*     */     
/*     */     final boolean areCompatible;
/*     */     final char compatibleType;
/*     */     
/*     */     private DescriptorComparison(boolean areNumbers, boolean areCompatible, char compatibleType)
/*     */     {
/* 247 */       this.areNumbers = areNumbers;
/* 248 */       this.areCompatible = areCompatible;
/* 249 */       this.compatibleType = compatibleType;
/*     */     }
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
/*     */     public static DescriptorComparison checkNumericCompatibility(String leftDeclaredDescriptor, String rightDeclaredDescriptor, String leftActualDescriptor, String rightActualDescriptor)
/*     */     {
/* 269 */       String ld = leftDeclaredDescriptor;
/* 270 */       String rd = rightDeclaredDescriptor;
/*     */       
/* 272 */       boolean leftNumeric = CodeFlow.isPrimitiveOrUnboxableSupportedNumberOrBoolean(ld);
/* 273 */       boolean rightNumeric = CodeFlow.isPrimitiveOrUnboxableSupportedNumberOrBoolean(rd);
/*     */       
/*     */ 
/* 276 */       if ((!leftNumeric) && (!ObjectUtils.nullSafeEquals(ld, leftActualDescriptor))) {
/* 277 */         ld = leftActualDescriptor;
/* 278 */         leftNumeric = CodeFlow.isPrimitiveOrUnboxableSupportedNumberOrBoolean(ld);
/*     */       }
/* 280 */       if ((!rightNumeric) && (!ObjectUtils.nullSafeEquals(rd, rightActualDescriptor))) {
/* 281 */         rd = rightActualDescriptor;
/* 282 */         rightNumeric = CodeFlow.isPrimitiveOrUnboxableSupportedNumberOrBoolean(rd);
/*     */       }
/*     */       
/* 285 */       if ((leftNumeric) && (rightNumeric)) {
/* 286 */         if (CodeFlow.areBoxingCompatible(ld, rd)) {
/* 287 */           return new DescriptorComparison(true, true, CodeFlow.toPrimitiveTargetDesc(ld));
/*     */         }
/*     */         
/* 290 */         return INCOMPATIBLE_NUMBERS;
/*     */       }
/*     */       
/*     */ 
/* 294 */       return NOT_NUMBERS;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\ast\Operator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */