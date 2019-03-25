/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelNode;
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
/*     */ public class CompoundExpression
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   public CompoundExpression(int pos, SpelNodeImpl... expressionComponents)
/*     */   {
/*  35 */     super(pos, expressionComponents);
/*  36 */     if (expressionComponents.length < 2) {
/*  37 */       throw new IllegalStateException("Do not build compound expressions with less than two entries: " + expressionComponents.length);
/*     */     }
/*     */   }
/*     */   
/*     */   /* Error */
/*     */   protected ValueRef getValueRef(ExpressionState state)
/*     */     throws EvaluationException
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: invokevirtual 10	org/springframework/expression/spel/ast/CompoundExpression:getChildCount	()I
/*     */     //   4: iconst_1
/*     */     //   5: if_icmpne +14 -> 19
/*     */     //   8: aload_0
/*     */     //   9: getfield 11	org/springframework/expression/spel/ast/CompoundExpression:children	[Lorg/springframework/expression/spel/ast/SpelNodeImpl;
/*     */     //   12: iconst_0
/*     */     //   13: aaload
/*     */     //   14: aload_1
/*     */     //   15: invokevirtual 12	org/springframework/expression/spel/ast/SpelNodeImpl:getValueRef	(Lorg/springframework/expression/spel/ExpressionState;)Lorg/springframework/expression/spel/ast/ValueRef;
/*     */     //   18: areturn
/*     */     //   19: aload_0
/*     */     //   20: getfield 11	org/springframework/expression/spel/ast/CompoundExpression:children	[Lorg/springframework/expression/spel/ast/SpelNodeImpl;
/*     */     //   23: iconst_0
/*     */     //   24: aaload
/*     */     //   25: astore_2
/*     */     //   26: aload_2
/*     */     //   27: aload_1
/*     */     //   28: invokevirtual 13	org/springframework/expression/spel/ast/SpelNodeImpl:getValueInternal	(Lorg/springframework/expression/spel/ExpressionState;)Lorg/springframework/expression/TypedValue;
/*     */     //   31: astore_3
/*     */     //   32: aload_0
/*     */     //   33: invokevirtual 10	org/springframework/expression/spel/ast/CompoundExpression:getChildCount	()I
/*     */     //   36: istore 4
/*     */     //   38: iconst_1
/*     */     //   39: istore 5
/*     */     //   41: iload 5
/*     */     //   43: iload 4
/*     */     //   45: iconst_1
/*     */     //   46: isub
/*     */     //   47: if_icmpge +44 -> 91
/*     */     //   50: aload_1
/*     */     //   51: aload_3
/*     */     //   52: invokevirtual 14	org/springframework/expression/spel/ExpressionState:pushActiveContextObject	(Lorg/springframework/expression/TypedValue;)V
/*     */     //   55: aload_0
/*     */     //   56: getfield 11	org/springframework/expression/spel/ast/CompoundExpression:children	[Lorg/springframework/expression/spel/ast/SpelNodeImpl;
/*     */     //   59: iload 5
/*     */     //   61: aaload
/*     */     //   62: astore_2
/*     */     //   63: aload_2
/*     */     //   64: aload_1
/*     */     //   65: invokevirtual 13	org/springframework/expression/spel/ast/SpelNodeImpl:getValueInternal	(Lorg/springframework/expression/spel/ExpressionState;)Lorg/springframework/expression/TypedValue;
/*     */     //   68: astore_3
/*     */     //   69: aload_1
/*     */     //   70: invokevirtual 15	org/springframework/expression/spel/ExpressionState:popActiveContextObject	()V
/*     */     //   73: goto +12 -> 85
/*     */     //   76: astore 6
/*     */     //   78: aload_1
/*     */     //   79: invokevirtual 15	org/springframework/expression/spel/ExpressionState:popActiveContextObject	()V
/*     */     //   82: aload 6
/*     */     //   84: athrow
/*     */     //   85: iinc 5 1
/*     */     //   88: goto -47 -> 41
/*     */     //   91: aload_1
/*     */     //   92: aload_3
/*     */     //   93: invokevirtual 14	org/springframework/expression/spel/ExpressionState:pushActiveContextObject	(Lorg/springframework/expression/TypedValue;)V
/*     */     //   96: aload_0
/*     */     //   97: getfield 11	org/springframework/expression/spel/ast/CompoundExpression:children	[Lorg/springframework/expression/spel/ast/SpelNodeImpl;
/*     */     //   100: iload 4
/*     */     //   102: iconst_1
/*     */     //   103: isub
/*     */     //   104: aaload
/*     */     //   105: astore_2
/*     */     //   106: aload_2
/*     */     //   107: aload_1
/*     */     //   108: invokevirtual 12	org/springframework/expression/spel/ast/SpelNodeImpl:getValueRef	(Lorg/springframework/expression/spel/ExpressionState;)Lorg/springframework/expression/spel/ast/ValueRef;
/*     */     //   111: astore 5
/*     */     //   113: aload_1
/*     */     //   114: invokevirtual 15	org/springframework/expression/spel/ExpressionState:popActiveContextObject	()V
/*     */     //   117: aload 5
/*     */     //   119: areturn
/*     */     //   120: astore 7
/*     */     //   122: aload_1
/*     */     //   123: invokevirtual 15	org/springframework/expression/spel/ExpressionState:popActiveContextObject	()V
/*     */     //   126: aload 7
/*     */     //   128: athrow
/*     */     //   129: astore_3
/*     */     //   130: aload_3
/*     */     //   131: aload_2
/*     */     //   132: invokevirtual 17	org/springframework/expression/spel/ast/SpelNodeImpl:getStartPosition	()I
/*     */     //   135: invokevirtual 18	org/springframework/expression/spel/SpelEvaluationException:setPosition	(I)V
/*     */     //   138: aload_3
/*     */     //   139: athrow
/*     */     // Line number table:
/*     */     //   Java source line #45	-> byte code offset #0
/*     */     //   Java source line #46	-> byte code offset #8
/*     */     //   Java source line #49	-> byte code offset #19
/*     */     //   Java source line #51	-> byte code offset #26
/*     */     //   Java source line #52	-> byte code offset #32
/*     */     //   Java source line #53	-> byte code offset #38
/*     */     //   Java source line #55	-> byte code offset #50
/*     */     //   Java source line #56	-> byte code offset #55
/*     */     //   Java source line #57	-> byte code offset #63
/*     */     //   Java source line #60	-> byte code offset #69
/*     */     //   Java source line #61	-> byte code offset #73
/*     */     //   Java source line #60	-> byte code offset #76
/*     */     //   Java source line #53	-> byte code offset #85
/*     */     //   Java source line #64	-> byte code offset #91
/*     */     //   Java source line #65	-> byte code offset #96
/*     */     //   Java source line #66	-> byte code offset #106
/*     */     //   Java source line #69	-> byte code offset #113
/*     */     //   Java source line #66	-> byte code offset #117
/*     */     //   Java source line #69	-> byte code offset #120
/*     */     //   Java source line #72	-> byte code offset #129
/*     */     //   Java source line #74	-> byte code offset #130
/*     */     //   Java source line #75	-> byte code offset #138
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	140	0	this	CompoundExpression
/*     */     //   0	140	1	state	ExpressionState
/*     */     //   25	107	2	nextNode	SpelNodeImpl
/*     */     //   31	62	3	result	TypedValue
/*     */     //   129	10	3	ex	org.springframework.expression.spel.SpelEvaluationException
/*     */     //   36	65	4	cc	int
/*     */     //   39	79	5	i	int
/*     */     //   76	7	6	localObject1	Object
/*     */     //   120	7	7	localObject2	Object
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   50	69	76	finally
/*     */     //   76	78	76	finally
/*     */     //   91	113	120	finally
/*     */     //   120	122	120	finally
/*     */     //   26	117	129	org/springframework/expression/spel/SpelEvaluationException
/*     */     //   120	129	129	org/springframework/expression/spel/SpelEvaluationException
/*     */   }
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state)
/*     */     throws EvaluationException
/*     */   {
/*  87 */     ValueRef ref = getValueRef(state);
/*  88 */     TypedValue result = ref.getValue();
/*  89 */     this.exitTypeDescriptor = this.children[(this.children.length - 1)].exitTypeDescriptor;
/*  90 */     return result;
/*     */   }
/*     */   
/*     */   public void setValue(ExpressionState state, Object value) throws EvaluationException
/*     */   {
/*  95 */     getValueRef(state).setValue(value);
/*     */   }
/*     */   
/*     */   public boolean isWritable(ExpressionState state) throws EvaluationException
/*     */   {
/* 100 */     return getValueRef(state).isWritable();
/*     */   }
/*     */   
/*     */   public String toStringAST()
/*     */   {
/* 105 */     StringBuilder sb = new StringBuilder();
/* 106 */     for (int i = 0; i < getChildCount(); i++) {
/* 107 */       if (i > 0) {
/* 108 */         sb.append(".");
/*     */       }
/* 110 */       sb.append(getChild(i).toStringAST());
/*     */     }
/* 112 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public boolean isCompilable()
/*     */   {
/* 117 */     for (SpelNodeImpl child : this.children) {
/* 118 */       if (!child.isCompilable()) {
/* 119 */         return false;
/*     */       }
/*     */     }
/* 122 */     return true;
/*     */   }
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf)
/*     */   {
/* 127 */     for (int i = 0; i < this.children.length; i++) {
/* 128 */       this.children[i].generateCode(mv, cf);
/*     */     }
/* 130 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\ast\CompoundExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */