/*     */ package org.springframework.expression.spel.standard;
/*     */ 
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.Expression;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.common.ExpressionUtils;
/*     */ import org.springframework.expression.spel.CompiledExpression;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelCompilerMode;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.expression.spel.SpelParserConfiguration;
/*     */ import org.springframework.expression.spel.ast.SpelNodeImpl;
/*     */ import org.springframework.expression.spel.support.StandardEvaluationContext;
/*     */ import org.springframework.util.Assert;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SpelExpression
/*     */   implements Expression
/*     */ {
/*     */   private static final int INTERPRETED_COUNT_THRESHOLD = 100;
/*     */   private static final int FAILED_ATTEMPTS_THRESHOLD = 100;
/*     */   private final String expression;
/*     */   private final SpelNodeImpl ast;
/*     */   private final SpelParserConfiguration configuration;
/*     */   private EvaluationContext evaluationContext;
/*     */   private CompiledExpression compiledAst;
/*  69 */   private volatile int interpretedCount = 0;
/*     */   
/*     */ 
/*     */ 
/*  73 */   private volatile int failedAttempts = 0;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SpelExpression(String expression, SpelNodeImpl ast, SpelParserConfiguration configuration)
/*     */   {
/*  80 */     this.expression = expression;
/*  81 */     this.ast = ast;
/*  82 */     this.configuration = configuration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEvaluationContext(EvaluationContext evaluationContext)
/*     */   {
/*  91 */     this.evaluationContext = evaluationContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public EvaluationContext getEvaluationContext()
/*     */   {
/*  99 */     if (this.evaluationContext == null) {
/* 100 */       this.evaluationContext = new StandardEvaluationContext();
/*     */     }
/* 102 */     return this.evaluationContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getExpressionString()
/*     */   {
/* 110 */     return this.expression;
/*     */   }
/*     */   
/*     */   public Object getValue() throws EvaluationException
/*     */   {
/* 115 */     if (this.compiledAst != null) {
/*     */       try
/*     */       {
/* 118 */         TypedValue contextRoot = this.evaluationContext != null ? this.evaluationContext.getRootObject() : null;
/* 119 */         return this.compiledAst.getValue(contextRoot != null ? contextRoot
/* 120 */           .getValue() : null, this.evaluationContext);
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/* 124 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 125 */           this.interpretedCount = 0;
/* 126 */           this.compiledAst = null;
/*     */         }
/*     */         else
/*     */         {
/* 130 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 135 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), this.configuration);
/* 136 */     Object result = this.ast.getValue(expressionState);
/* 137 */     checkCompile(expressionState);
/* 138 */     return result;
/*     */   }
/*     */   
/*     */   public <T> T getValue(Class<T> expectedResultType)
/*     */     throws EvaluationException
/*     */   {
/* 144 */     if (this.compiledAst != null) {
/*     */       try
/*     */       {
/* 147 */         TypedValue contextRoot = this.evaluationContext != null ? this.evaluationContext.getRootObject() : null;
/* 148 */         Object result = this.compiledAst.getValue(contextRoot != null ? contextRoot
/* 149 */           .getValue() : null, this.evaluationContext);
/* 150 */         if (expectedResultType == null) {
/* 151 */           return (T)result;
/*     */         }
/*     */         
/* 154 */         return (T)ExpressionUtils.convertTypedValue(
/* 155 */           getEvaluationContext(), new TypedValue(result), expectedResultType);
/*     */ 
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/* 160 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 161 */           this.interpretedCount = 0;
/* 162 */           this.compiledAst = null;
/*     */         }
/*     */         else
/*     */         {
/* 166 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 171 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), this.configuration);
/* 172 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/* 173 */     checkCompile(expressionState);
/* 174 */     return (T)ExpressionUtils.convertTypedValue(expressionState
/* 175 */       .getEvaluationContext(), typedResultValue, expectedResultType);
/*     */   }
/*     */   
/*     */   public Object getValue(Object rootObject) throws EvaluationException
/*     */   {
/* 180 */     if (this.compiledAst != null) {
/*     */       try {
/* 182 */         return this.compiledAst.getValue(rootObject, this.evaluationContext);
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/* 186 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 187 */           this.interpretedCount = 0;
/* 188 */           this.compiledAst = null;
/*     */         }
/*     */         else
/*     */         {
/* 192 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 198 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration);
/* 199 */     Object result = this.ast.getValue(expressionState);
/* 200 */     checkCompile(expressionState);
/* 201 */     return result;
/*     */   }
/*     */   
/*     */   public <T> T getValue(Object rootObject, Class<T> expectedResultType)
/*     */     throws EvaluationException
/*     */   {
/* 207 */     if (this.compiledAst != null) {
/*     */       try {
/* 209 */         Object result = this.compiledAst.getValue(rootObject, null);
/* 210 */         if (expectedResultType == null) {
/* 211 */           return (T)result;
/*     */         }
/*     */         
/* 214 */         return (T)ExpressionUtils.convertTypedValue(
/* 215 */           getEvaluationContext(), new TypedValue(result), expectedResultType);
/*     */ 
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/* 220 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 221 */           this.interpretedCount = 0;
/* 222 */           this.compiledAst = null;
/*     */         }
/*     */         else
/*     */         {
/* 226 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 232 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration);
/* 233 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/* 234 */     checkCompile(expressionState);
/* 235 */     return (T)ExpressionUtils.convertTypedValue(expressionState
/* 236 */       .getEvaluationContext(), typedResultValue, expectedResultType);
/*     */   }
/*     */   
/*     */   public Object getValue(EvaluationContext context) throws EvaluationException
/*     */   {
/* 241 */     Assert.notNull(context, "EvaluationContext is required");
/*     */     
/* 243 */     if (this.compiledAst != null) {
/*     */       try {
/* 245 */         TypedValue contextRoot = context.getRootObject();
/* 246 */         return this.compiledAst.getValue(contextRoot.getValue(), context);
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/* 250 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 251 */           this.interpretedCount = 0;
/* 252 */           this.compiledAst = null;
/*     */         }
/*     */         else
/*     */         {
/* 256 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 261 */     ExpressionState expressionState = new ExpressionState(context, this.configuration);
/* 262 */     Object result = this.ast.getValue(expressionState);
/* 263 */     checkCompile(expressionState);
/* 264 */     return result;
/*     */   }
/*     */   
/*     */   public <T> T getValue(EvaluationContext context, Class<T> expectedResultType)
/*     */     throws EvaluationException
/*     */   {
/* 270 */     Assert.notNull(context, "EvaluationContext is required");
/*     */     
/* 272 */     if (this.compiledAst != null) {
/*     */       try {
/* 274 */         TypedValue contextRoot = context.getRootObject();
/* 275 */         Object result = this.compiledAst.getValue(contextRoot.getValue(), context);
/* 276 */         if (expectedResultType != null) {
/* 277 */           return (T)ExpressionUtils.convertTypedValue(context, new TypedValue(result), expectedResultType);
/*     */         }
/*     */         
/* 280 */         return (T)result;
/*     */ 
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/* 285 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 286 */           this.interpretedCount = 0;
/* 287 */           this.compiledAst = null;
/*     */         }
/*     */         else
/*     */         {
/* 291 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 296 */     ExpressionState expressionState = new ExpressionState(context, this.configuration);
/* 297 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/* 298 */     checkCompile(expressionState);
/* 299 */     return (T)ExpressionUtils.convertTypedValue(context, typedResultValue, expectedResultType);
/*     */   }
/*     */   
/*     */   public Object getValue(EvaluationContext context, Object rootObject) throws EvaluationException
/*     */   {
/* 304 */     Assert.notNull(context, "EvaluationContext is required");
/*     */     
/* 306 */     if (this.compiledAst != null) {
/*     */       try {
/* 308 */         return this.compiledAst.getValue(rootObject, context);
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/* 312 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 313 */           this.interpretedCount = 0;
/* 314 */           this.compiledAst = null;
/*     */         }
/*     */         else
/*     */         {
/* 318 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 323 */     ExpressionState expressionState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 324 */     Object result = this.ast.getValue(expressionState);
/* 325 */     checkCompile(expressionState);
/* 326 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public <T> T getValue(EvaluationContext context, Object rootObject, Class<T> expectedResultType)
/*     */     throws EvaluationException
/*     */   {
/* 334 */     Assert.notNull(context, "EvaluationContext is required");
/*     */     
/* 336 */     if (this.compiledAst != null) {
/*     */       try {
/* 338 */         Object result = this.compiledAst.getValue(rootObject, context);
/* 339 */         if (expectedResultType != null) {
/* 340 */           return (T)ExpressionUtils.convertTypedValue(context, new TypedValue(result), expectedResultType);
/*     */         }
/*     */         
/* 343 */         return (T)result;
/*     */ 
/*     */       }
/*     */       catch (Throwable ex)
/*     */       {
/* 348 */         if (this.configuration.getCompilerMode() == SpelCompilerMode.MIXED) {
/* 349 */           this.interpretedCount = 0;
/* 350 */           this.compiledAst = null;
/*     */         }
/*     */         else
/*     */         {
/* 354 */           throw new SpelEvaluationException(ex, SpelMessage.EXCEPTION_RUNNING_COMPILED_EXPRESSION, new Object[0]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 359 */     ExpressionState expressionState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 360 */     TypedValue typedResultValue = this.ast.getTypedValue(expressionState);
/* 361 */     checkCompile(expressionState);
/* 362 */     return (T)ExpressionUtils.convertTypedValue(context, typedResultValue, expectedResultType);
/*     */   }
/*     */   
/*     */   public Class<?> getValueType() throws EvaluationException
/*     */   {
/* 367 */     return getValueType(getEvaluationContext());
/*     */   }
/*     */   
/*     */   public Class<?> getValueType(Object rootObject) throws EvaluationException
/*     */   {
/* 372 */     return getValueType(getEvaluationContext(), rootObject);
/*     */   }
/*     */   
/*     */   public Class<?> getValueType(EvaluationContext context) throws EvaluationException
/*     */   {
/* 377 */     Assert.notNull(context, "EvaluationContext is required");
/* 378 */     ExpressionState expressionState = new ExpressionState(context, this.configuration);
/* 379 */     TypeDescriptor typeDescriptor = this.ast.getValueInternal(expressionState).getTypeDescriptor();
/* 380 */     return typeDescriptor != null ? typeDescriptor.getType() : null;
/*     */   }
/*     */   
/*     */   public Class<?> getValueType(EvaluationContext context, Object rootObject) throws EvaluationException
/*     */   {
/* 385 */     ExpressionState expressionState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 386 */     TypeDescriptor typeDescriptor = this.ast.getValueInternal(expressionState).getTypeDescriptor();
/* 387 */     return typeDescriptor != null ? typeDescriptor.getType() : null;
/*     */   }
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor() throws EvaluationException
/*     */   {
/* 392 */     return getValueTypeDescriptor(getEvaluationContext());
/*     */   }
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(Object rootObject)
/*     */     throws EvaluationException
/*     */   {
/* 398 */     ExpressionState expressionState = new ExpressionState(getEvaluationContext(), toTypedValue(rootObject), this.configuration);
/* 399 */     return this.ast.getValueInternal(expressionState).getTypeDescriptor();
/*     */   }
/*     */   
/*     */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context) throws EvaluationException
/*     */   {
/* 404 */     Assert.notNull(context, "EvaluationContext is required");
/* 405 */     ExpressionState expressionState = new ExpressionState(context, this.configuration);
/* 406 */     return this.ast.getValueInternal(expressionState).getTypeDescriptor();
/*     */   }
/*     */   
/*     */ 
/*     */   public TypeDescriptor getValueTypeDescriptor(EvaluationContext context, Object rootObject)
/*     */     throws EvaluationException
/*     */   {
/* 413 */     Assert.notNull(context, "EvaluationContext is required");
/* 414 */     ExpressionState expressionState = new ExpressionState(context, toTypedValue(rootObject), this.configuration);
/* 415 */     return this.ast.getValueInternal(expressionState).getTypeDescriptor();
/*     */   }
/*     */   
/*     */   public boolean isWritable(Object rootObject) throws EvaluationException
/*     */   {
/* 420 */     return this.ast.isWritable(new ExpressionState(
/* 421 */       getEvaluationContext(), toTypedValue(rootObject), this.configuration));
/*     */   }
/*     */   
/*     */   public boolean isWritable(EvaluationContext context) throws EvaluationException
/*     */   {
/* 426 */     Assert.notNull(context, "EvaluationContext is required");
/* 427 */     return this.ast.isWritable(new ExpressionState(context, this.configuration));
/*     */   }
/*     */   
/*     */   public boolean isWritable(EvaluationContext context, Object rootObject) throws EvaluationException
/*     */   {
/* 432 */     Assert.notNull(context, "EvaluationContext is required");
/* 433 */     return this.ast.isWritable(new ExpressionState(context, toTypedValue(rootObject), this.configuration));
/*     */   }
/*     */   
/*     */   public void setValue(Object rootObject, Object value) throws EvaluationException
/*     */   {
/* 438 */     this.ast.setValue(new ExpressionState(
/* 439 */       getEvaluationContext(), toTypedValue(rootObject), this.configuration), value);
/*     */   }
/*     */   
/*     */   public void setValue(EvaluationContext context, Object value) throws EvaluationException
/*     */   {
/* 444 */     Assert.notNull(context, "EvaluationContext is required");
/* 445 */     this.ast.setValue(new ExpressionState(context, this.configuration), value);
/*     */   }
/*     */   
/*     */ 
/*     */   public void setValue(EvaluationContext context, Object rootObject, Object value)
/*     */     throws EvaluationException
/*     */   {
/* 452 */     Assert.notNull(context, "EvaluationContext is required");
/* 453 */     this.ast.setValue(new ExpressionState(context, toTypedValue(rootObject), this.configuration), value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void checkCompile(ExpressionState expressionState)
/*     */   {
/* 463 */     this.interpretedCount += 1;
/* 464 */     SpelCompilerMode compilerMode = expressionState.getConfiguration().getCompilerMode();
/* 465 */     if (compilerMode != SpelCompilerMode.OFF) {
/* 466 */       if (compilerMode == SpelCompilerMode.IMMEDIATE) {
/* 467 */         if (this.interpretedCount > 1) {
/* 468 */           compileExpression();
/*     */         }
/*     */         
/*     */ 
/*     */       }
/* 473 */       else if (this.interpretedCount > 100) {
/* 474 */         compileExpression();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean compileExpression()
/*     */   {
/* 487 */     if (this.failedAttempts > 100)
/*     */     {
/* 489 */       return false;
/*     */     }
/* 491 */     if (this.compiledAst == null) {
/* 492 */       synchronized (this.expression)
/*     */       {
/* 494 */         if (this.compiledAst != null) {
/* 495 */           return true;
/*     */         }
/* 497 */         SpelCompiler compiler = SpelCompiler.getCompiler(this.configuration.getCompilerClassLoader());
/* 498 */         this.compiledAst = compiler.compile(this.ast);
/* 499 */         if (this.compiledAst == null) {
/* 500 */           this.failedAttempts += 1;
/*     */         }
/*     */       }
/*     */     }
/* 504 */     return this.compiledAst != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void revertToInterpreted()
/*     */   {
/* 513 */     this.compiledAst = null;
/* 514 */     this.interpretedCount = 0;
/* 515 */     this.failedAttempts = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SpelNode getAST()
/*     */   {
/* 522 */     return this.ast;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toStringAST()
/*     */   {
/* 532 */     return this.ast.toStringAST();
/*     */   }
/*     */   
/*     */   private TypedValue toTypedValue(Object object) {
/* 536 */     return object != null ? new TypedValue(object) : TypedValue.NULL;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\standard\SpelExpression.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */