/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.aspectj.lang.JoinPoint.StaticPart;
/*     */ import org.aspectj.lang.ProceedingJoinPoint;
/*     */ import org.aspectj.lang.Signature;
/*     */ import org.aspectj.lang.reflect.MethodSignature;
/*     */ import org.aspectj.lang.reflect.SourceLocation;
/*     */ import org.aspectj.runtime.internal.AroundClosure;
/*     */ import org.springframework.aop.ProxyMethodInvocation;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
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
/*     */ public class MethodInvocationProceedingJoinPoint
/*     */   implements ProceedingJoinPoint, JoinPoint.StaticPart
/*     */ {
/*  55 */   private static final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
/*     */   
/*     */ 
/*     */ 
/*     */   private final ProxyMethodInvocation methodInvocation;
/*     */   
/*     */ 
/*     */   private Object[] defensiveCopyOfArgs;
/*     */   
/*     */ 
/*     */   private Signature signature;
/*     */   
/*     */ 
/*     */   private SourceLocation sourceLocation;
/*     */   
/*     */ 
/*     */ 
/*     */   public MethodInvocationProceedingJoinPoint(ProxyMethodInvocation methodInvocation)
/*     */   {
/*  74 */     Assert.notNull(methodInvocation, "MethodInvocation must not be null");
/*  75 */     this.methodInvocation = methodInvocation;
/*     */   }
/*     */   
/*     */   public void set$AroundClosure(AroundClosure aroundClosure)
/*     */   {
/*  80 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */   public Object proceed() throws Throwable
/*     */   {
/*  85 */     return this.methodInvocation.invocableClone().proceed();
/*     */   }
/*     */   
/*     */   public Object proceed(Object[] arguments) throws Throwable
/*     */   {
/*  90 */     Assert.notNull(arguments, "Argument array passed to proceed cannot be null");
/*  91 */     if (arguments.length != this.methodInvocation.getArguments().length)
/*     */     {
/*  93 */       throw new IllegalArgumentException("Expecting " + this.methodInvocation.getArguments().length + " arguments to proceed, but was passed " + arguments.length + " arguments");
/*     */     }
/*     */     
/*  96 */     this.methodInvocation.setArguments(arguments);
/*  97 */     return this.methodInvocation.invocableClone(arguments).proceed();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getThis()
/*     */   {
/* 105 */     return this.methodInvocation.getProxy();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getTarget()
/*     */   {
/* 113 */     return this.methodInvocation.getThis();
/*     */   }
/*     */   
/*     */   public Object[] getArgs()
/*     */   {
/* 118 */     if (this.defensiveCopyOfArgs == null) {
/* 119 */       Object[] argsSource = this.methodInvocation.getArguments();
/* 120 */       this.defensiveCopyOfArgs = new Object[argsSource.length];
/* 121 */       System.arraycopy(argsSource, 0, this.defensiveCopyOfArgs, 0, argsSource.length);
/*     */     }
/* 123 */     return this.defensiveCopyOfArgs;
/*     */   }
/*     */   
/*     */   public Signature getSignature()
/*     */   {
/* 128 */     if (this.signature == null) {
/* 129 */       this.signature = new MethodSignatureImpl(null);
/*     */     }
/* 131 */     return this.signature;
/*     */   }
/*     */   
/*     */   public SourceLocation getSourceLocation()
/*     */   {
/* 136 */     if (this.sourceLocation == null) {
/* 137 */       this.sourceLocation = new SourceLocationImpl(null);
/*     */     }
/* 139 */     return this.sourceLocation;
/*     */   }
/*     */   
/*     */   public String getKind()
/*     */   {
/* 144 */     return "method-execution";
/*     */   }
/*     */   
/*     */ 
/*     */   public int getId()
/*     */   {
/* 150 */     return 0;
/*     */   }
/*     */   
/*     */   public JoinPoint.StaticPart getStaticPart()
/*     */   {
/* 155 */     return this;
/*     */   }
/*     */   
/*     */   public String toShortString()
/*     */   {
/* 160 */     return "execution(" + getSignature().toShortString() + ")";
/*     */   }
/*     */   
/*     */   public String toLongString()
/*     */   {
/* 165 */     return "execution(" + getSignature().toLongString() + ")";
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 170 */     return "execution(" + getSignature().toString() + ")";
/*     */   }
/*     */   
/*     */ 
/*     */   private class MethodSignatureImpl
/*     */     implements MethodSignature
/*     */   {
/*     */     private volatile String[] parameterNames;
/*     */     
/*     */     private MethodSignatureImpl() {}
/*     */     
/*     */     public String getName()
/*     */     {
/* 183 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getName();
/*     */     }
/*     */     
/*     */     public int getModifiers()
/*     */     {
/* 188 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getModifiers();
/*     */     }
/*     */     
/*     */     public Class<?> getDeclaringType()
/*     */     {
/* 193 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getDeclaringClass();
/*     */     }
/*     */     
/*     */     public String getDeclaringTypeName()
/*     */     {
/* 198 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getDeclaringClass().getName();
/*     */     }
/*     */     
/*     */     public Class<?> getReturnType()
/*     */     {
/* 203 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getReturnType();
/*     */     }
/*     */     
/*     */     public Method getMethod()
/*     */     {
/* 208 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod();
/*     */     }
/*     */     
/*     */     public Class<?>[] getParameterTypes()
/*     */     {
/* 213 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getParameterTypes();
/*     */     }
/*     */     
/*     */     public String[] getParameterNames()
/*     */     {
/* 218 */       if (this.parameterNames == null) {
/* 219 */         this.parameterNames = MethodInvocationProceedingJoinPoint.parameterNameDiscoverer.getParameterNames(getMethod());
/*     */       }
/* 221 */       return this.parameterNames;
/*     */     }
/*     */     
/*     */     public Class<?>[] getExceptionTypes()
/*     */     {
/* 226 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getMethod().getExceptionTypes();
/*     */     }
/*     */     
/*     */     public String toShortString()
/*     */     {
/* 231 */       return toString(false, false, false, false);
/*     */     }
/*     */     
/*     */     public String toLongString()
/*     */     {
/* 236 */       return toString(true, true, true, true);
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 241 */       return toString(false, true, false, true);
/*     */     }
/*     */     
/*     */ 
/*     */     private String toString(boolean includeModifier, boolean includeReturnTypeAndArgs, boolean useLongReturnAndArgumentTypeName, boolean useLongTypeName)
/*     */     {
/* 247 */       StringBuilder sb = new StringBuilder();
/* 248 */       if (includeModifier) {
/* 249 */         sb.append(Modifier.toString(getModifiers()));
/* 250 */         sb.append(" ");
/*     */       }
/* 252 */       if (includeReturnTypeAndArgs) {
/* 253 */         appendType(sb, getReturnType(), useLongReturnAndArgumentTypeName);
/* 254 */         sb.append(" ");
/*     */       }
/* 256 */       appendType(sb, getDeclaringType(), useLongTypeName);
/* 257 */       sb.append(".");
/* 258 */       sb.append(getMethod().getName());
/* 259 */       sb.append("(");
/* 260 */       Class<?>[] parametersTypes = getParameterTypes();
/* 261 */       appendTypes(sb, parametersTypes, includeReturnTypeAndArgs, useLongReturnAndArgumentTypeName);
/* 262 */       sb.append(")");
/* 263 */       return sb.toString();
/*     */     }
/*     */     
/*     */ 
/*     */     private void appendTypes(StringBuilder sb, Class<?>[] types, boolean includeArgs, boolean useLongReturnAndArgumentTypeName)
/*     */     {
/* 269 */       if (includeArgs) {
/* 270 */         int size = types.length; for (int i = 0; i < size; i++) {
/* 271 */           appendType(sb, types[i], useLongReturnAndArgumentTypeName);
/* 272 */           if (i < size - 1) {
/* 273 */             sb.append(",");
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 278 */       else if (types.length != 0) {
/* 279 */         sb.append("..");
/*     */       }
/*     */     }
/*     */     
/*     */     private void appendType(StringBuilder sb, Class<?> type, boolean useLongTypeName)
/*     */     {
/* 285 */       if (type.isArray()) {
/* 286 */         appendType(sb, type.getComponentType(), useLongTypeName);
/* 287 */         sb.append("[]");
/*     */       }
/*     */       else {
/* 290 */         sb.append(useLongTypeName ? type.getName() : type.getSimpleName());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private class SourceLocationImpl
/*     */     implements SourceLocation
/*     */   {
/*     */     private SourceLocationImpl() {}
/*     */     
/*     */     public Class<?> getWithinType()
/*     */     {
/* 303 */       if (MethodInvocationProceedingJoinPoint.this.methodInvocation.getThis() == null) {
/* 304 */         throw new UnsupportedOperationException("No source location joinpoint available: target is null");
/*     */       }
/* 306 */       return MethodInvocationProceedingJoinPoint.this.methodInvocation.getThis().getClass();
/*     */     }
/*     */     
/*     */     public String getFileName()
/*     */     {
/* 311 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public int getLine()
/*     */     {
/* 316 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public int getColumn()
/*     */     {
/* 322 */       throw new UnsupportedOperationException();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\aspectj\MethodInvocationProceedingJoinPoint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */