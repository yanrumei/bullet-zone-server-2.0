/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.expression.spel.support.ReflectionHelper;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ public class FunctionReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final String name;
/*     */   private volatile Method method;
/*     */   
/*     */   public FunctionReference(String functionName, int pos, SpelNodeImpl... arguments)
/*     */   {
/*  62 */     super(pos, arguments);
/*  63 */     this.name = functionName;
/*     */   }
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state)
/*     */     throws EvaluationException
/*     */   {
/*  69 */     TypedValue value = state.lookupVariable(this.name);
/*  70 */     if (value == TypedValue.NULL) {
/*  71 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.FUNCTION_NOT_DEFINED, new Object[] { this.name });
/*     */     }
/*  73 */     if (!(value.getValue() instanceof Method))
/*     */     {
/*     */ 
/*  76 */       throw new SpelEvaluationException(SpelMessage.FUNCTION_REFERENCE_CANNOT_BE_INVOKED, new Object[] { this.name, value.getClass() });
/*     */     }
/*     */     try
/*     */     {
/*  80 */       return executeFunctionJLRMethod(state, (Method)value.getValue());
/*     */     }
/*     */     catch (SpelEvaluationException ex) {
/*  83 */       ex.setPosition(getStartPosition());
/*  84 */       throw ex;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private TypedValue executeFunctionJLRMethod(ExpressionState state, Method method)
/*     */     throws EvaluationException
/*     */   {
/*  96 */     Object[] functionArgs = getArguments(state);
/*     */     
/*  98 */     if (!method.isVarArgs()) {
/*  99 */       int declaredParamCount = method.getParameterTypes().length;
/* 100 */       if (declaredParamCount != functionArgs.length)
/*     */       {
/* 102 */         throw new SpelEvaluationException(SpelMessage.INCORRECT_NUMBER_OF_ARGUMENTS_TO_FUNCTION, new Object[] {Integer.valueOf(functionArgs.length), Integer.valueOf(declaredParamCount) });
/*     */       }
/*     */     }
/* 105 */     if (!Modifier.isStatic(method.getModifiers()))
/*     */     {
/* 107 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.FUNCTION_MUST_BE_STATIC, new Object[] {ClassUtils.getQualifiedMethodName(method), this.name });
/*     */     }
/*     */     
/*     */ 
/* 111 */     TypeConverter converter = state.getEvaluationContext().getTypeConverter();
/* 112 */     boolean argumentConversionOccurred = ReflectionHelper.convertAllArguments(converter, functionArgs, method);
/* 113 */     if (method.isVarArgs()) {
/* 114 */       functionArgs = ReflectionHelper.setupArgumentsForVarargsInvocation(method
/* 115 */         .getParameterTypes(), functionArgs);
/*     */     }
/* 117 */     boolean compilable = false;
/*     */     try
/*     */     {
/* 120 */       ReflectionUtils.makeAccessible(method);
/* 121 */       Object result = method.invoke(method.getClass(), functionArgs);
/* 122 */       compilable = !argumentConversionOccurred;
/* 123 */       return new TypedValue(result, new TypeDescriptor(new MethodParameter(method, -1)).narrow(result));
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 127 */       throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_FUNCTION_CALL, new Object[] { this.name, ex.getMessage() });
/*     */     }
/*     */     finally {
/* 130 */       if (compilable) {
/* 131 */         this.exitTypeDescriptor = CodeFlow.toDescriptor(method.getReturnType());
/* 132 */         this.method = method;
/*     */       }
/*     */       else {
/* 135 */         this.exitTypeDescriptor = null;
/* 136 */         this.method = null;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public String toStringAST()
/*     */   {
/* 143 */     StringBuilder sb = new StringBuilder("#").append(this.name);
/* 144 */     sb.append("(");
/* 145 */     for (int i = 0; i < getChildCount(); i++) {
/* 146 */       if (i > 0) {
/* 147 */         sb.append(",");
/*     */       }
/* 149 */       sb.append(getChild(i).toStringAST());
/*     */     }
/* 151 */     sb.append(")");
/* 152 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object[] getArguments(ExpressionState state)
/*     */     throws EvaluationException
/*     */   {
/* 161 */     Object[] arguments = new Object[getChildCount()];
/* 162 */     for (int i = 0; i < arguments.length; i++) {
/* 163 */       arguments[i] = this.children[i].getValueInternal(state).getValue();
/*     */     }
/* 165 */     return arguments;
/*     */   }
/*     */   
/*     */   public boolean isCompilable()
/*     */   {
/* 170 */     Method method = this.method;
/* 171 */     if (method == null) {
/* 172 */       return false;
/*     */     }
/* 174 */     int methodModifiers = method.getModifiers();
/* 175 */     if ((!Modifier.isStatic(methodModifiers)) || (!Modifier.isPublic(methodModifiers)) || 
/* 176 */       (!Modifier.isPublic(method.getDeclaringClass().getModifiers()))) {
/* 177 */       return false;
/*     */     }
/* 179 */     for (SpelNodeImpl child : this.children) {
/* 180 */       if (!child.isCompilable()) {
/* 181 */         return false;
/*     */       }
/*     */     }
/* 184 */     return true;
/*     */   }
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf)
/*     */   {
/* 189 */     Method method = this.method;
/* 190 */     Assert.state(method != null, "No method handle");
/* 191 */     String classDesc = method.getDeclaringClass().getName().replace('.', '/');
/* 192 */     generateCodeForArguments(mv, cf, method, this.children);
/* 193 */     mv.visitMethodInsn(184, classDesc, method.getName(), 
/* 194 */       CodeFlow.createSignatureDescriptor(method), false);
/* 195 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\ast\FunctionReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */