/*    */ package org.springframework.expression.spel.ast;
/*    */ 
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import java.util.concurrent.ConcurrentMap;
/*    */ import java.util.regex.Matcher;
/*    */ import java.util.regex.Pattern;
/*    */ import java.util.regex.PatternSyntaxException;
/*    */ import org.springframework.expression.EvaluationException;
/*    */ import org.springframework.expression.TypedValue;
/*    */ import org.springframework.expression.spel.ExpressionState;
/*    */ import org.springframework.expression.spel.SpelEvaluationException;
/*    */ import org.springframework.expression.spel.SpelMessage;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OperatorMatches
/*    */   extends Operator
/*    */ {
/* 43 */   private final ConcurrentMap<String, Pattern> patternCache = new ConcurrentHashMap();
/*    */   
/*    */   public OperatorMatches(int pos, SpelNodeImpl... operands)
/*    */   {
/* 47 */     super("matches", pos, operands);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public BooleanTypedValue getValueInternal(ExpressionState state)
/*    */     throws EvaluationException
/*    */   {
/* 61 */     SpelNodeImpl leftOp = getLeftOperand();
/* 62 */     SpelNodeImpl rightOp = getRightOperand();
/* 63 */     Object left = leftOp.getValue(state, String.class);
/* 64 */     Object right = getRightOperand().getValueInternal(state).getValue();
/*    */     
/* 66 */     if (!(left instanceof String)) {
/* 67 */       throw new SpelEvaluationException(leftOp.getStartPosition(), SpelMessage.INVALID_FIRST_OPERAND_FOR_MATCHES_OPERATOR, new Object[] { left });
/*    */     }
/*    */     
/* 70 */     if (!(right instanceof String)) {
/* 71 */       throw new SpelEvaluationException(rightOp.getStartPosition(), SpelMessage.INVALID_SECOND_OPERAND_FOR_MATCHES_OPERATOR, new Object[] { right });
/*    */     }
/*    */     
/*    */     try
/*    */     {
/* 76 */       String leftString = (String)left;
/* 77 */       String rightString = (String)right;
/* 78 */       Pattern pattern = (Pattern)this.patternCache.get(rightString);
/* 79 */       if (pattern == null) {
/* 80 */         pattern = Pattern.compile(rightString);
/* 81 */         this.patternCache.putIfAbsent(rightString, pattern);
/*    */       }
/* 83 */       Matcher matcher = pattern.matcher(leftString);
/* 84 */       return BooleanTypedValue.forValue(matcher.matches());
/*    */     }
/*    */     catch (PatternSyntaxException ex) {
/* 87 */       throw new SpelEvaluationException(rightOp.getStartPosition(), ex, SpelMessage.INVALID_PATTERN, new Object[] { right });
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\ast\OperatorMatches.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */