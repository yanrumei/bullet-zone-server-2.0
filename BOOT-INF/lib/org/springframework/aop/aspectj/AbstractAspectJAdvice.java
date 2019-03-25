/*     */ package org.springframework.aop.aspectj;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.aopalliance.aop.Advice;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.aspectj.lang.JoinPoint;
/*     */ import org.aspectj.lang.JoinPoint.StaticPart;
/*     */ import org.aspectj.lang.ProceedingJoinPoint;
/*     */ import org.aspectj.weaver.tools.JoinPointMatch;
/*     */ import org.aspectj.weaver.tools.PointcutParameter;
/*     */ import org.springframework.aop.AopInvocationException;
/*     */ import org.springframework.aop.MethodMatcher;
/*     */ import org.springframework.aop.Pointcut;
/*     */ import org.springframework.aop.ProxyMethodInvocation;
/*     */ import org.springframework.aop.interceptor.ExposeInvocationInterceptor;
/*     */ import org.springframework.aop.support.ComposablePointcut;
/*     */ import org.springframework.aop.support.MethodMatchers;
/*     */ import org.springframework.aop.support.StaticMethodMatcher;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ReflectionUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractAspectJAdvice
/*     */   implements Advice, AspectJPrecedenceInformation, Serializable
/*     */ {
/*  67 */   protected static final String JOIN_POINT_KEY = JoinPoint.class.getName();
/*     */   
/*     */   private final Class<?> declaringClass;
/*     */   
/*     */   private final String methodName;
/*     */   
/*     */   private final Class<?>[] parameterTypes;
/*     */   protected transient Method aspectJAdviceMethod;
/*     */   private final AspectJExpressionPointcut pointcut;
/*     */   
/*     */   public static JoinPoint currentJoinPoint()
/*     */   {
/*  79 */     MethodInvocation mi = ExposeInvocationInterceptor.currentInvocation();
/*  80 */     if (!(mi instanceof ProxyMethodInvocation)) {
/*  81 */       throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
/*     */     }
/*  83 */     ProxyMethodInvocation pmi = (ProxyMethodInvocation)mi;
/*  84 */     JoinPoint jp = (JoinPoint)pmi.getUserAttribute(JOIN_POINT_KEY);
/*  85 */     if (jp == null) {
/*  86 */       jp = new MethodInvocationProceedingJoinPoint(pmi);
/*  87 */       pmi.setUserAttribute(JOIN_POINT_KEY, jp);
/*     */     }
/*  89 */     return jp;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final AspectInstanceFactory aspectInstanceFactory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String aspectName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private int declarationOrder;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String[] argumentNames;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String throwingName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private String returningName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 129 */   private Class<?> discoveredReturningType = Object.class;
/*     */   
/* 131 */   private Class<?> discoveredThrowingType = Object.class;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 137 */   private int joinPointArgumentIndex = -1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 143 */   private int joinPointStaticPartArgumentIndex = -1;
/*     */   
/*     */   private Map<String, Integer> argumentBindings;
/*     */   
/* 147 */   private boolean argumentsIntrospected = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Type discoveredReturningGenericType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AbstractAspectJAdvice(Method aspectJAdviceMethod, AspectJExpressionPointcut pointcut, AspectInstanceFactory aspectInstanceFactory)
/*     */   {
/* 163 */     Assert.notNull(aspectJAdviceMethod, "Advice method must not be null");
/* 164 */     this.declaringClass = aspectJAdviceMethod.getDeclaringClass();
/* 165 */     this.methodName = aspectJAdviceMethod.getName();
/* 166 */     this.parameterTypes = aspectJAdviceMethod.getParameterTypes();
/* 167 */     this.aspectJAdviceMethod = aspectJAdviceMethod;
/* 168 */     this.pointcut = pointcut;
/* 169 */     this.aspectInstanceFactory = aspectInstanceFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Method getAspectJAdviceMethod()
/*     */   {
/* 177 */     return this.aspectJAdviceMethod;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final AspectJExpressionPointcut getPointcut()
/*     */   {
/* 184 */     calculateArgumentBindings();
/* 185 */     return this.pointcut;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Pointcut buildSafePointcut()
/*     */   {
/* 194 */     Pointcut pc = getPointcut();
/* 195 */     MethodMatcher safeMethodMatcher = MethodMatchers.intersection(new AdviceExcludingMethodMatcher(this.aspectJAdviceMethod), pc
/* 196 */       .getMethodMatcher());
/* 197 */     return new ComposablePointcut(pc.getClassFilter(), safeMethodMatcher);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final AspectInstanceFactory getAspectInstanceFactory()
/*     */   {
/* 204 */     return this.aspectInstanceFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final ClassLoader getAspectClassLoader()
/*     */   {
/* 211 */     return this.aspectInstanceFactory.getAspectClassLoader();
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 216 */     return this.aspectInstanceFactory.getOrder();
/*     */   }
/*     */   
/*     */   public void setAspectName(String name)
/*     */   {
/* 221 */     this.aspectName = name;
/*     */   }
/*     */   
/*     */   public String getAspectName()
/*     */   {
/* 226 */     return this.aspectName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDeclarationOrder(int order)
/*     */   {
/* 233 */     this.declarationOrder = order;
/*     */   }
/*     */   
/*     */   public int getDeclarationOrder()
/*     */   {
/* 238 */     return this.declarationOrder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setArgumentNames(String argNames)
/*     */   {
/* 248 */     String[] tokens = StringUtils.commaDelimitedListToStringArray(argNames);
/* 249 */     setArgumentNamesFromStringArray(tokens);
/*     */   }
/*     */   
/*     */   public void setArgumentNamesFromStringArray(String... args) {
/* 253 */     this.argumentNames = new String[args.length];
/* 254 */     for (int i = 0; i < args.length; i++) {
/* 255 */       this.argumentNames[i] = StringUtils.trimWhitespace(args[i]);
/* 256 */       if (!isVariableName(this.argumentNames[i])) {
/* 257 */         throw new IllegalArgumentException("'argumentNames' property of AbstractAspectJAdvice contains an argument name '" + this.argumentNames[i] + "' that is not a valid Java identifier");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 262 */     if ((this.argumentNames != null) && 
/* 263 */       (this.aspectJAdviceMethod.getParameterTypes().length == this.argumentNames.length + 1))
/*     */     {
/* 265 */       Class<?> firstArgType = this.aspectJAdviceMethod.getParameterTypes()[0];
/* 266 */       if ((firstArgType == JoinPoint.class) || (firstArgType == ProceedingJoinPoint.class) || (firstArgType == JoinPoint.StaticPart.class))
/*     */       {
/*     */ 
/* 269 */         String[] oldNames = this.argumentNames;
/* 270 */         this.argumentNames = new String[oldNames.length + 1];
/* 271 */         this.argumentNames[0] = "THIS_JOIN_POINT";
/* 272 */         System.arraycopy(oldNames, 0, this.argumentNames, 1, oldNames.length);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void setReturningName(String name)
/*     */   {
/* 279 */     throw new UnsupportedOperationException("Only afterReturning advice can be used to bind a return value");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setReturningNameNoCheck(String name)
/*     */   {
/* 288 */     if (isVariableName(name)) {
/* 289 */       this.returningName = name;
/*     */     }
/*     */     else {
/*     */       try
/*     */       {
/* 294 */         this.discoveredReturningType = ClassUtils.forName(name, getAspectClassLoader());
/*     */       }
/*     */       catch (Throwable ex) {
/* 297 */         throw new IllegalArgumentException("Returning name '" + name + "' is neither a valid argument name nor the fully-qualified name of a Java type on the classpath. Root cause: " + ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected Class<?> getDiscoveredReturningType()
/*     */   {
/* 305 */     return this.discoveredReturningType;
/*     */   }
/*     */   
/*     */   protected Type getDiscoveredReturningGenericType() {
/* 309 */     return this.discoveredReturningGenericType;
/*     */   }
/*     */   
/*     */   public void setThrowingName(String name) {
/* 313 */     throw new UnsupportedOperationException("Only afterThrowing advice can be used to bind a thrown exception");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void setThrowingNameNoCheck(String name)
/*     */   {
/* 322 */     if (isVariableName(name)) {
/* 323 */       this.throwingName = name;
/*     */     }
/*     */     else {
/*     */       try
/*     */       {
/* 328 */         this.discoveredThrowingType = ClassUtils.forName(name, getAspectClassLoader());
/*     */       }
/*     */       catch (Throwable ex) {
/* 331 */         throw new IllegalArgumentException("Throwing name '" + name + "' is neither a valid argument name nor the fully-qualified name of a Java type on the classpath. Root cause: " + ex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   protected Class<?> getDiscoveredThrowingType()
/*     */   {
/* 339 */     return this.discoveredThrowingType;
/*     */   }
/*     */   
/*     */   private boolean isVariableName(String name) {
/* 343 */     char[] chars = name.toCharArray();
/* 344 */     if (!Character.isJavaIdentifierStart(chars[0])) {
/* 345 */       return false;
/*     */     }
/* 347 */     for (int i = 1; i < chars.length; i++) {
/* 348 */       if (!Character.isJavaIdentifierPart(chars[i])) {
/* 349 */         return false;
/*     */       }
/*     */     }
/* 352 */     return true;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final synchronized void calculateArgumentBindings()
/*     */   {
/* 371 */     if ((this.argumentsIntrospected) || (this.parameterTypes.length == 0)) {
/* 372 */       return;
/*     */     }
/*     */     
/* 375 */     int numUnboundArgs = this.parameterTypes.length;
/* 376 */     Class<?>[] parameterTypes = this.aspectJAdviceMethod.getParameterTypes();
/* 377 */     if ((maybeBindJoinPoint(parameterTypes[0])) || (maybeBindProceedingJoinPoint(parameterTypes[0]))) {
/* 378 */       numUnboundArgs--;
/*     */     }
/* 380 */     else if (maybeBindJoinPointStaticPart(parameterTypes[0])) {
/* 381 */       numUnboundArgs--;
/*     */     }
/*     */     
/* 384 */     if (numUnboundArgs > 0)
/*     */     {
/* 386 */       bindArgumentsByName(numUnboundArgs);
/*     */     }
/*     */     
/* 389 */     this.argumentsIntrospected = true;
/*     */   }
/*     */   
/*     */   private boolean maybeBindJoinPoint(Class<?> candidateParameterType) {
/* 393 */     if (JoinPoint.class == candidateParameterType) {
/* 394 */       this.joinPointArgumentIndex = 0;
/* 395 */       return true;
/*     */     }
/*     */     
/* 398 */     return false;
/*     */   }
/*     */   
/*     */   private boolean maybeBindProceedingJoinPoint(Class<?> candidateParameterType)
/*     */   {
/* 403 */     if (ProceedingJoinPoint.class == candidateParameterType) {
/* 404 */       if (!supportsProceedingJoinPoint()) {
/* 405 */         throw new IllegalArgumentException("ProceedingJoinPoint is only supported for around advice");
/*     */       }
/* 407 */       this.joinPointArgumentIndex = 0;
/* 408 */       return true;
/*     */     }
/*     */     
/* 411 */     return false;
/*     */   }
/*     */   
/*     */   protected boolean supportsProceedingJoinPoint()
/*     */   {
/* 416 */     return false;
/*     */   }
/*     */   
/*     */   private boolean maybeBindJoinPointStaticPart(Class<?> candidateParameterType) {
/* 420 */     if (JoinPoint.StaticPart.class == candidateParameterType) {
/* 421 */       this.joinPointStaticPartArgumentIndex = 0;
/* 422 */       return true;
/*     */     }
/*     */     
/* 425 */     return false;
/*     */   }
/*     */   
/*     */   private void bindArgumentsByName(int numArgumentsExpectingToBind)
/*     */   {
/* 430 */     if (this.argumentNames == null) {
/* 431 */       this.argumentNames = createParameterNameDiscoverer().getParameterNames(this.aspectJAdviceMethod);
/*     */     }
/* 433 */     if (this.argumentNames != null)
/*     */     {
/* 435 */       bindExplicitArguments(numArgumentsExpectingToBind);
/*     */     }
/*     */     else {
/* 438 */       throw new IllegalStateException("Advice method [" + this.aspectJAdviceMethod.getName() + "] requires " + numArgumentsExpectingToBind + " arguments to be bound by name, but the argument names were not specified and could not be discovered.");
/*     */     }
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
/*     */   protected ParameterNameDiscoverer createParameterNameDiscoverer()
/*     */   {
/* 452 */     DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
/*     */     
/* 454 */     AspectJAdviceParameterNameDiscoverer adviceParameterNameDiscoverer = new AspectJAdviceParameterNameDiscoverer(this.pointcut.getExpression());
/* 455 */     adviceParameterNameDiscoverer.setReturningName(this.returningName);
/* 456 */     adviceParameterNameDiscoverer.setThrowingName(this.throwingName);
/*     */     
/* 458 */     adviceParameterNameDiscoverer.setRaiseExceptions(true);
/* 459 */     discoverer.addDiscoverer(adviceParameterNameDiscoverer);
/* 460 */     return discoverer;
/*     */   }
/*     */   
/*     */   private void bindExplicitArguments(int numArgumentsLeftToBind) {
/* 464 */     this.argumentBindings = new HashMap();
/*     */     
/* 466 */     int numExpectedArgumentNames = this.aspectJAdviceMethod.getParameterTypes().length;
/* 467 */     if (this.argumentNames.length != numExpectedArgumentNames) {
/* 468 */       throw new IllegalStateException("Expecting to find " + numExpectedArgumentNames + " arguments to bind by name in advice, but actually found " + this.argumentNames.length + " arguments.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 474 */     int argumentIndexOffset = this.parameterTypes.length - numArgumentsLeftToBind;
/* 475 */     for (int i = argumentIndexOffset; i < this.argumentNames.length; i++) {
/* 476 */       this.argumentBindings.put(this.argumentNames[i], Integer.valueOf(i));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 481 */     if (this.returningName != null) {
/* 482 */       if (!this.argumentBindings.containsKey(this.returningName)) {
/* 483 */         throw new IllegalStateException("Returning argument name '" + this.returningName + "' was not bound in advice arguments");
/*     */       }
/*     */       
/*     */ 
/* 487 */       Integer index = (Integer)this.argumentBindings.get(this.returningName);
/* 488 */       this.discoveredReturningType = this.aspectJAdviceMethod.getParameterTypes()[index.intValue()];
/* 489 */       this.discoveredReturningGenericType = this.aspectJAdviceMethod.getGenericParameterTypes()[index.intValue()];
/*     */     }
/*     */     
/* 492 */     if (this.throwingName != null) {
/* 493 */       if (!this.argumentBindings.containsKey(this.throwingName)) {
/* 494 */         throw new IllegalStateException("Throwing argument name '" + this.throwingName + "' was not bound in advice arguments");
/*     */       }
/*     */       
/*     */ 
/* 498 */       Integer index = (Integer)this.argumentBindings.get(this.throwingName);
/* 499 */       this.discoveredThrowingType = this.aspectJAdviceMethod.getParameterTypes()[index.intValue()];
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 504 */     configurePointcutParameters(argumentIndexOffset);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void configurePointcutParameters(int argumentIndexOffset)
/*     */   {
/* 513 */     int numParametersToRemove = argumentIndexOffset;
/* 514 */     if (this.returningName != null) {
/* 515 */       numParametersToRemove++;
/*     */     }
/* 517 */     if (this.throwingName != null) {
/* 518 */       numParametersToRemove++;
/*     */     }
/* 520 */     String[] pointcutParameterNames = new String[this.argumentNames.length - numParametersToRemove];
/* 521 */     Class<?>[] pointcutParameterTypes = new Class[pointcutParameterNames.length];
/* 522 */     Class<?>[] methodParameterTypes = this.aspectJAdviceMethod.getParameterTypes();
/*     */     
/* 524 */     int index = 0;
/* 525 */     for (int i = 0; i < this.argumentNames.length; i++) {
/* 526 */       if (i >= argumentIndexOffset)
/*     */       {
/*     */ 
/* 529 */         if ((!this.argumentNames[i].equals(this.returningName)) && 
/* 530 */           (!this.argumentNames[i].equals(this.throwingName)))
/*     */         {
/*     */ 
/* 533 */           pointcutParameterNames[index] = this.argumentNames[i];
/* 534 */           pointcutParameterTypes[index] = methodParameterTypes[i];
/* 535 */           index++;
/*     */         } }
/*     */     }
/* 538 */     this.pointcut.setParameterNames(pointcutParameterNames);
/* 539 */     this.pointcut.setParameterTypes(pointcutParameterTypes);
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
/*     */   protected Object[] argBinding(JoinPoint jp, JoinPointMatch jpMatch, Object returnValue, Throwable ex)
/*     */   {
/* 552 */     calculateArgumentBindings();
/*     */     
/*     */ 
/* 555 */     Object[] adviceInvocationArgs = new Object[this.parameterTypes.length];
/* 556 */     int numBound = 0;
/*     */     
/* 558 */     if (this.joinPointArgumentIndex != -1) {
/* 559 */       adviceInvocationArgs[this.joinPointArgumentIndex] = jp;
/* 560 */       numBound++;
/*     */     }
/* 562 */     else if (this.joinPointStaticPartArgumentIndex != -1) {
/* 563 */       adviceInvocationArgs[this.joinPointStaticPartArgumentIndex] = jp.getStaticPart();
/* 564 */       numBound++;
/*     */     }
/*     */     
/* 567 */     if (!CollectionUtils.isEmpty(this.argumentBindings))
/*     */     {
/* 569 */       if (jpMatch != null) {
/* 570 */         PointcutParameter[] parameterBindings = jpMatch.getParameterBindings();
/* 571 */         for (PointcutParameter parameter : parameterBindings) {
/* 572 */           String name = parameter.getName();
/* 573 */           Integer index = (Integer)this.argumentBindings.get(name);
/* 574 */           adviceInvocationArgs[index.intValue()] = parameter.getBinding();
/* 575 */           numBound++;
/*     */         }
/*     */       }
/*     */       
/* 579 */       if (this.returningName != null) {
/* 580 */         Integer index = (Integer)this.argumentBindings.get(this.returningName);
/* 581 */         adviceInvocationArgs[index.intValue()] = returnValue;
/* 582 */         numBound++;
/*     */       }
/*     */       
/* 585 */       if (this.throwingName != null) {
/* 586 */         Integer index = (Integer)this.argumentBindings.get(this.throwingName);
/* 587 */         adviceInvocationArgs[index.intValue()] = ex;
/* 588 */         numBound++;
/*     */       }
/*     */     }
/*     */     
/* 592 */     if (numBound != this.parameterTypes.length) {
/* 593 */       throw new IllegalStateException("Required to bind " + this.parameterTypes.length + " arguments, but only bound " + numBound + " (JoinPointMatch " + (jpMatch == null ? "was NOT" : "WAS") + " bound in invocation)");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 598 */     return adviceInvocationArgs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object invokeAdviceMethod(JoinPointMatch jpMatch, Object returnValue, Throwable ex)
/*     */     throws Throwable
/*     */   {
/* 611 */     return invokeAdviceMethodWithGivenArgs(argBinding(getJoinPoint(), jpMatch, returnValue, ex));
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object invokeAdviceMethod(JoinPoint jp, JoinPointMatch jpMatch, Object returnValue, Throwable t)
/*     */     throws Throwable
/*     */   {
/* 618 */     return invokeAdviceMethodWithGivenArgs(argBinding(jp, jpMatch, returnValue, t));
/*     */   }
/*     */   
/*     */   protected Object invokeAdviceMethodWithGivenArgs(Object[] args) throws Throwable {
/* 622 */     Object[] actualArgs = args;
/* 623 */     if (this.aspectJAdviceMethod.getParameterTypes().length == 0) {
/* 624 */       actualArgs = null;
/*     */     }
/*     */     try {
/* 627 */       ReflectionUtils.makeAccessible(this.aspectJAdviceMethod);
/*     */       
/* 629 */       return this.aspectJAdviceMethod.invoke(this.aspectInstanceFactory.getAspectInstance(), actualArgs);
/*     */ 
/*     */     }
/*     */     catch (IllegalArgumentException ex)
/*     */     {
/* 634 */       throw new AopInvocationException("Mismatch on arguments to advice method [" + this.aspectJAdviceMethod + "]; pointcut expression [" + this.pointcut.getPointcutExpression() + "]", ex);
/*     */     }
/*     */     catch (InvocationTargetException ex) {
/* 637 */       throw ex.getTargetException();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JoinPoint getJoinPoint()
/*     */   {
/* 645 */     return currentJoinPoint();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JoinPointMatch getJoinPointMatch()
/*     */   {
/* 652 */     MethodInvocation mi = ExposeInvocationInterceptor.currentInvocation();
/* 653 */     if (!(mi instanceof ProxyMethodInvocation)) {
/* 654 */       throw new IllegalStateException("MethodInvocation is not a Spring ProxyMethodInvocation: " + mi);
/*     */     }
/* 656 */     return getJoinPointMatch((ProxyMethodInvocation)mi);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JoinPointMatch getJoinPointMatch(ProxyMethodInvocation pmi)
/*     */   {
/* 666 */     return (JoinPointMatch)pmi.getUserAttribute(this.pointcut.getExpression());
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 672 */     return getClass().getName() + ": advice method [" + this.aspectJAdviceMethod + "]; aspect name '" + this.aspectName + "'";
/*     */   }
/*     */   
/*     */   private void readObject(ObjectInputStream inputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 677 */     inputStream.defaultReadObject();
/*     */     try {
/* 679 */       this.aspectJAdviceMethod = this.declaringClass.getMethod(this.methodName, this.parameterTypes);
/*     */     }
/*     */     catch (NoSuchMethodException ex) {
/* 682 */       throw new IllegalStateException("Failed to find advice method on deserialization", ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class AdviceExcludingMethodMatcher
/*     */     extends StaticMethodMatcher
/*     */   {
/*     */     private final Method adviceMethod;
/*     */     
/*     */ 
/*     */     public AdviceExcludingMethodMatcher(Method adviceMethod)
/*     */     {
/* 696 */       this.adviceMethod = adviceMethod;
/*     */     }
/*     */     
/*     */     public boolean matches(Method method, Class<?> targetClass)
/*     */     {
/* 701 */       return !this.adviceMethod.equals(method);
/*     */     }
/*     */     
/*     */     public boolean equals(Object other)
/*     */     {
/* 706 */       if (this == other) {
/* 707 */         return true;
/*     */       }
/* 709 */       if (!(other instanceof AdviceExcludingMethodMatcher)) {
/* 710 */         return false;
/*     */       }
/* 712 */       AdviceExcludingMethodMatcher otherMm = (AdviceExcludingMethodMatcher)other;
/* 713 */       return this.adviceMethod.equals(otherMm.adviceMethod);
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 718 */       return this.adviceMethod.hashCode();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\aspectj\AbstractAspectJAdvice.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */