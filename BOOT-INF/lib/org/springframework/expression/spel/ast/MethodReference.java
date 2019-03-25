/*     */ package org.springframework.expression.spel.ast;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.ExpressionInvocationTargetException;
/*     */ import org.springframework.expression.MethodExecutor;
/*     */ import org.springframework.expression.MethodResolver;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.ExpressionState;
/*     */ import org.springframework.expression.spel.SpelEvaluationException;
/*     */ import org.springframework.expression.spel.SpelMessage;
/*     */ import org.springframework.expression.spel.SpelNode;
/*     */ import org.springframework.expression.spel.support.ReflectiveMethodExecutor;
/*     */ import org.springframework.expression.spel.support.ReflectiveMethodResolver;
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
/*     */ public class MethodReference
/*     */   extends SpelNodeImpl
/*     */ {
/*     */   private final String name;
/*     */   private final boolean nullSafe;
/*     */   private volatile CachedMethodExecutor cachedExecutor;
/*     */   
/*     */   public MethodReference(boolean nullSafe, String methodName, int pos, SpelNodeImpl... arguments)
/*     */   {
/*  60 */     super(pos, arguments);
/*  61 */     this.name = methodName;
/*  62 */     this.nullSafe = nullSafe;
/*     */   }
/*     */   
/*     */   public final String getName()
/*     */   {
/*  67 */     return this.name;
/*     */   }
/*     */   
/*     */   protected ValueRef getValueRef(ExpressionState state) throws EvaluationException
/*     */   {
/*  72 */     Object[] arguments = getArguments(state);
/*  73 */     if (state.getActiveContextObject().getValue() == null) {
/*  74 */       throwIfNotNullSafe(getArgumentTypes(arguments));
/*  75 */       return ValueRef.NullValueRef.INSTANCE;
/*     */     }
/*  77 */     return new MethodValueRef(state, arguments);
/*     */   }
/*     */   
/*     */   public TypedValue getValueInternal(ExpressionState state) throws EvaluationException
/*     */   {
/*  82 */     EvaluationContext evaluationContext = state.getEvaluationContext();
/*  83 */     Object value = state.getActiveContextObject().getValue();
/*  84 */     TypeDescriptor targetType = state.getActiveContextObject().getTypeDescriptor();
/*  85 */     Object[] arguments = getArguments(state);
/*  86 */     TypedValue result = getValueInternal(evaluationContext, value, targetType, arguments);
/*  87 */     updateExitTypeDescriptor();
/*  88 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   private TypedValue getValueInternal(EvaluationContext evaluationContext, Object value, TypeDescriptor targetType, Object[] arguments)
/*     */   {
/*  94 */     List<TypeDescriptor> argumentTypes = getArgumentTypes(arguments);
/*  95 */     if (value == null) {
/*  96 */       throwIfNotNullSafe(argumentTypes);
/*  97 */       return TypedValue.NULL;
/*     */     }
/*     */     
/* 100 */     MethodExecutor executorToUse = getCachedExecutor(evaluationContext, value, targetType, argumentTypes);
/* 101 */     if (executorToUse != null) {
/*     */       try {
/* 103 */         return executorToUse.execute(evaluationContext, value, arguments);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/*     */       catch (AccessException ex)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 117 */         throwSimpleExceptionIfPossible(value, ex);
/*     */         
/*     */ 
/*     */ 
/* 121 */         this.cachedExecutor = null;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 126 */     executorToUse = findAccessorForMethod(this.name, argumentTypes, value, evaluationContext);
/* 127 */     this.cachedExecutor = new CachedMethodExecutor(executorToUse, (value instanceof Class) ? (Class)value : null, targetType, argumentTypes);
/*     */     try
/*     */     {
/* 130 */       return executorToUse.execute(evaluationContext, value, arguments);
/*     */     }
/*     */     catch (AccessException ex)
/*     */     {
/* 134 */       throwSimpleExceptionIfPossible(value, ex);
/*     */       
/*     */ 
/* 137 */       throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.EXCEPTION_DURING_METHOD_INVOCATION, new Object[] { this.name, value.getClass().getName(), ex.getMessage() });
/*     */     }
/*     */   }
/*     */   
/*     */   private void throwIfNotNullSafe(List<TypeDescriptor> argumentTypes) {
/* 142 */     if (!this.nullSafe)
/*     */     {
/*     */ 
/* 145 */       throw new SpelEvaluationException(getStartPosition(), SpelMessage.METHOD_CALL_ON_NULL_OBJECT_NOT_ALLOWED, new Object[] {FormatHelper.formatMethodForMessage(this.name, argumentTypes) });
/*     */     }
/*     */   }
/*     */   
/*     */   private Object[] getArguments(ExpressionState state) {
/* 150 */     Object[] arguments = new Object[getChildCount()];
/* 151 */     for (int i = 0; i < arguments.length; i++) {
/*     */       try
/*     */       {
/* 154 */         state.pushActiveContextObject(state.getScopeRootContextObject());
/* 155 */         arguments[i] = this.children[i].getValueInternal(state).getValue();
/*     */       }
/*     */       finally {
/* 158 */         state.popActiveContextObject();
/*     */       }
/*     */     }
/* 161 */     return arguments;
/*     */   }
/*     */   
/*     */   private List<TypeDescriptor> getArgumentTypes(Object... arguments) {
/* 165 */     List<TypeDescriptor> descriptors = new ArrayList(arguments.length);
/* 166 */     for (Object argument : arguments) {
/* 167 */       descriptors.add(TypeDescriptor.forObject(argument));
/*     */     }
/* 169 */     return Collections.unmodifiableList(descriptors);
/*     */   }
/*     */   
/*     */ 
/*     */   private MethodExecutor getCachedExecutor(EvaluationContext evaluationContext, Object value, TypeDescriptor target, List<TypeDescriptor> argumentTypes)
/*     */   {
/* 175 */     List<MethodResolver> methodResolvers = evaluationContext.getMethodResolvers();
/* 176 */     if ((methodResolvers == null) || (methodResolvers.size() != 1) || 
/* 177 */       (!(methodResolvers.get(0) instanceof ReflectiveMethodResolver)))
/*     */     {
/* 179 */       return null;
/*     */     }
/*     */     
/* 182 */     CachedMethodExecutor executorToCheck = this.cachedExecutor;
/* 183 */     if ((executorToCheck != null) && (executorToCheck.isSuitable(value, target, argumentTypes))) {
/* 184 */       return executorToCheck.get();
/*     */     }
/* 186 */     this.cachedExecutor = null;
/* 187 */     return null;
/*     */   }
/*     */   
/*     */   private MethodExecutor findAccessorForMethod(String name, List<TypeDescriptor> argumentTypes, Object targetObject, EvaluationContext evaluationContext)
/*     */     throws SpelEvaluationException
/*     */   {
/* 193 */     List<MethodResolver> methodResolvers = evaluationContext.getMethodResolvers();
/* 194 */     if (methodResolvers != null) {
/* 195 */       for (MethodResolver methodResolver : methodResolvers) {
/*     */         try {
/* 197 */           MethodExecutor methodExecutor = methodResolver.resolve(evaluationContext, targetObject, name, argumentTypes);
/*     */           
/* 199 */           if (methodExecutor != null) {
/* 200 */             return methodExecutor;
/*     */           }
/*     */         }
/*     */         catch (AccessException ex)
/*     */         {
/* 205 */           throw new SpelEvaluationException(getStartPosition(), ex, SpelMessage.PROBLEM_LOCATING_METHOD, new Object[] { name, targetObject.getClass() });
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 212 */     throw new SpelEvaluationException(getStartPosition(), SpelMessage.METHOD_NOT_FOUND, new Object[] {FormatHelper.formatMethodForMessage(name, argumentTypes), FormatHelper.formatClassNameForMessage((targetObject instanceof Class) ? (Class)targetObject : targetObject
/* 213 */       .getClass()) });
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void throwSimpleExceptionIfPossible(Object value, AccessException ex)
/*     */   {
/* 221 */     if ((ex.getCause() instanceof InvocationTargetException)) {
/* 222 */       Throwable rootCause = ex.getCause().getCause();
/* 223 */       if ((rootCause instanceof RuntimeException)) {
/* 224 */         throw ((RuntimeException)rootCause);
/*     */       }
/*     */       
/*     */ 
/* 228 */       throw new ExpressionInvocationTargetException(getStartPosition(), "A problem occurred when trying to execute method '" + this.name + "' on object of type [" + value.getClass().getName() + "]", rootCause);
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateExitTypeDescriptor() {
/* 233 */     CachedMethodExecutor executorToCheck = this.cachedExecutor;
/* 234 */     if ((executorToCheck != null) && ((executorToCheck.get() instanceof ReflectiveMethodExecutor))) {
/* 235 */       Method method = ((ReflectiveMethodExecutor)executorToCheck.get()).getMethod();
/* 236 */       this.exitTypeDescriptor = CodeFlow.toDescriptor(method.getReturnType());
/*     */     }
/*     */   }
/*     */   
/*     */   public String toStringAST()
/*     */   {
/* 242 */     StringBuilder sb = new StringBuilder(this.name);
/* 243 */     sb.append("(");
/* 244 */     for (int i = 0; i < getChildCount(); i++) {
/* 245 */       if (i > 0) {
/* 246 */         sb.append(",");
/*     */       }
/* 248 */       sb.append(getChild(i).toStringAST());
/*     */     }
/* 250 */     sb.append(")");
/* 251 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCompilable()
/*     */   {
/* 260 */     CachedMethodExecutor executorToCheck = this.cachedExecutor;
/* 261 */     if ((executorToCheck == null) || (executorToCheck.hasProxyTarget()) || 
/* 262 */       (!(executorToCheck.get() instanceof ReflectiveMethodExecutor))) {
/* 263 */       return false;
/*     */     }
/*     */     
/* 266 */     for (SpelNodeImpl child : this.children) {
/* 267 */       if (!child.isCompilable()) {
/* 268 */         return false;
/*     */       }
/*     */     }
/*     */     
/* 272 */     ReflectiveMethodExecutor executor = (ReflectiveMethodExecutor)executorToCheck.get();
/* 273 */     if (executor.didArgumentConversionOccur()) {
/* 274 */       return false;
/*     */     }
/* 276 */     Object clazz = executor.getMethod().getDeclaringClass();
/* 277 */     if ((!Modifier.isPublic(((Class)clazz).getModifiers())) && (executor.getPublicDeclaringClass() == null)) {
/* 278 */       return false;
/*     */     }
/*     */     
/* 281 */     return true;
/*     */   }
/*     */   
/*     */   public void generateCode(MethodVisitor mv, CodeFlow cf)
/*     */   {
/* 286 */     CachedMethodExecutor executorToCheck = this.cachedExecutor;
/* 287 */     if ((executorToCheck == null) || (!(executorToCheck.get() instanceof ReflectiveMethodExecutor))) {
/* 288 */       throw new IllegalStateException("No applicable cached executor found: " + executorToCheck);
/*     */     }
/*     */     
/* 291 */     ReflectiveMethodExecutor methodExecutor = (ReflectiveMethodExecutor)executorToCheck.get();
/* 292 */     Method method = methodExecutor.getMethod();
/* 293 */     boolean isStaticMethod = Modifier.isStatic(method.getModifiers());
/* 294 */     String descriptor = cf.lastDescriptor();
/*     */     
/* 296 */     if (descriptor == null) {
/* 297 */       if (!isStaticMethod)
/*     */       {
/* 299 */         cf.loadTarget(mv);
/*     */       }
/*     */       
/*     */     }
/* 303 */     else if (isStaticMethod)
/*     */     {
/* 305 */       mv.visitInsn(87);
/*     */     }
/*     */     
/*     */ 
/* 309 */     if (CodeFlow.isPrimitive(descriptor)) {
/* 310 */       CodeFlow.insertBoxIfNecessary(mv, descriptor.charAt(0));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 315 */     String classDesc = Modifier.isPublic(method.getDeclaringClass().getModifiers()) ? method.getDeclaringClass().getName().replace('.', '/') : methodExecutor.getPublicDeclaringClass().getName().replace('.', '/');
/* 316 */     if ((!isStaticMethod) && (
/* 317 */       (descriptor == null) || (!descriptor.substring(1).equals(classDesc)))) {
/* 318 */       CodeFlow.insertCheckCast(mv, "L" + classDesc);
/*     */     }
/*     */     
/*     */ 
/* 322 */     generateCodeForArguments(mv, cf, method, this.children);
/* 323 */     mv.visitMethodInsn(isStaticMethod ? 184 : 182, classDesc, method.getName(), 
/* 324 */       CodeFlow.createSignatureDescriptor(method), method.getDeclaringClass().isInterface());
/* 325 */     cf.pushDescriptor(this.exitTypeDescriptor);
/*     */   }
/*     */   
/*     */ 
/*     */   private class MethodValueRef
/*     */     implements ValueRef
/*     */   {
/*     */     private final EvaluationContext evaluationContext;
/*     */     
/*     */     private final Object value;
/*     */     private final TypeDescriptor targetType;
/*     */     private final Object[] arguments;
/*     */     
/*     */     public MethodValueRef(ExpressionState state, Object[] arguments)
/*     */     {
/* 340 */       this.evaluationContext = state.getEvaluationContext();
/* 341 */       this.value = state.getActiveContextObject().getValue();
/* 342 */       this.targetType = state.getActiveContextObject().getTypeDescriptor();
/* 343 */       this.arguments = arguments;
/*     */     }
/*     */     
/*     */     public TypedValue getValue()
/*     */     {
/* 348 */       TypedValue result = MethodReference.this.getValueInternal(this.evaluationContext, this.value, this.targetType, this.arguments);
/*     */       
/* 350 */       MethodReference.this.updateExitTypeDescriptor();
/* 351 */       return result;
/*     */     }
/*     */     
/*     */     public void setValue(Object newValue)
/*     */     {
/* 356 */       throw new IllegalAccessError();
/*     */     }
/*     */     
/*     */     public boolean isWritable()
/*     */     {
/* 361 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class CachedMethodExecutor
/*     */   {
/*     */     private final MethodExecutor methodExecutor;
/*     */     
/*     */     private final Class<?> staticClass;
/*     */     
/*     */     private final TypeDescriptor target;
/*     */     
/*     */     private final List<TypeDescriptor> argumentTypes;
/*     */     
/*     */     public CachedMethodExecutor(MethodExecutor methodExecutor, Class<?> staticClass, TypeDescriptor target, List<TypeDescriptor> argumentTypes)
/*     */     {
/* 378 */       this.methodExecutor = methodExecutor;
/* 379 */       this.staticClass = staticClass;
/* 380 */       this.target = target;
/* 381 */       this.argumentTypes = argumentTypes;
/*     */     }
/*     */     
/*     */     public boolean isSuitable(Object value, TypeDescriptor target, List<TypeDescriptor> argumentTypes) {
/* 385 */       return ((this.staticClass == null) || (this.staticClass == value)) && 
/* 386 */         (this.target.equals(target)) && (this.argumentTypes.equals(argumentTypes));
/*     */     }
/*     */     
/*     */     public boolean hasProxyTarget() {
/* 390 */       return (this.target != null) && (Proxy.isProxyClass(this.target.getType()));
/*     */     }
/*     */     
/*     */     public MethodExecutor get() {
/* 394 */       return this.methodExecutor;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\ast\MethodReference.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */