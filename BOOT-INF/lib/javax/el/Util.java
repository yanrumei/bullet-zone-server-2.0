/*     */ package javax.el;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.MissingResourceException;
/*     */ import java.util.ResourceBundle;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import java.util.concurrent.locks.ReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
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
/*     */ class Util
/*     */ {
/*  41 */   private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];
/*  42 */   private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static void handleThrowable(Throwable t)
/*     */   {
/*  50 */     if ((t instanceof ThreadDeath)) {
/*  51 */       throw ((ThreadDeath)t);
/*     */     }
/*  53 */     if ((t instanceof VirtualMachineError)) {
/*  54 */       throw ((VirtualMachineError)t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static String message(ELContext context, String name, Object... props)
/*     */   {
/*  61 */     Locale locale = null;
/*  62 */     if (context != null) {
/*  63 */       locale = context.getLocale();
/*     */     }
/*  65 */     if (locale == null) {
/*  66 */       locale = Locale.getDefault();
/*  67 */       if (locale == null) {
/*  68 */         return "";
/*     */       }
/*     */     }
/*  71 */     ResourceBundle bundle = ResourceBundle.getBundle("javax.el.LocalStrings", locale);
/*     */     try
/*     */     {
/*  74 */       String template = bundle.getString(name);
/*  75 */       if (props != null) {}
/*  76 */       return MessageFormat.format(template, props);
/*     */     }
/*     */     catch (MissingResourceException e) {}
/*     */     
/*  80 */     return "Missing Resource: '" + name + "' for Locale " + locale.getDisplayName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  85 */   private static final CacheValue nullTcclFactory = new CacheValue();
/*  86 */   private static final ConcurrentMap<CacheKey, CacheValue> factoryCache = new ConcurrentHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static ExpressionFactory getExpressionFactory()
/*     */   {
/*  95 */     ClassLoader tccl = Thread.currentThread().getContextClassLoader();
/*  96 */     CacheValue cacheValue = null;
/*  97 */     ExpressionFactory factory = null;
/*     */     
/*  99 */     if (tccl == null) {
/* 100 */       cacheValue = nullTcclFactory;
/*     */     } else {
/* 102 */       CacheKey key = new CacheKey(tccl);
/* 103 */       cacheValue = (CacheValue)factoryCache.get(key);
/* 104 */       if (cacheValue == null) {
/* 105 */         CacheValue newCacheValue = new CacheValue();
/* 106 */         cacheValue = (CacheValue)factoryCache.putIfAbsent(key, newCacheValue);
/* 107 */         if (cacheValue == null) {
/* 108 */           cacheValue = newCacheValue;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 113 */     Lock readLock = cacheValue.getLock().readLock();
/* 114 */     readLock.lock();
/*     */     try {
/* 116 */       factory = cacheValue.getExpressionFactory();
/*     */     } finally {
/* 118 */       readLock.unlock();
/*     */     }
/*     */     
/* 121 */     if (factory == null) {
/* 122 */       Lock writeLock = cacheValue.getLock().writeLock();
/* 123 */       writeLock.lock();
/*     */       try {
/* 125 */         factory = cacheValue.getExpressionFactory();
/* 126 */         if (factory == null) {
/* 127 */           factory = ExpressionFactory.newInstance();
/* 128 */           cacheValue.setExpressionFactory(factory);
/*     */         }
/*     */       } finally {
/* 131 */         writeLock.unlock();
/*     */       }
/*     */     }
/*     */     
/* 135 */     return factory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class CacheKey
/*     */   {
/*     */     private final int hash;
/*     */     
/*     */     private final WeakReference<ClassLoader> ref;
/*     */     
/*     */ 
/*     */     public CacheKey(ClassLoader key)
/*     */     {
/* 149 */       this.hash = key.hashCode();
/* 150 */       this.ref = new WeakReference(key);
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 155 */       return this.hash;
/*     */     }
/*     */     
/*     */     public boolean equals(Object obj)
/*     */     {
/* 160 */       if (obj == this) {
/* 161 */         return true;
/*     */       }
/* 163 */       if (!(obj instanceof CacheKey)) {
/* 164 */         return false;
/*     */       }
/* 166 */       ClassLoader thisKey = (ClassLoader)this.ref.get();
/* 167 */       if (thisKey == null) {
/* 168 */         return false;
/*     */       }
/* 170 */       return thisKey == ((CacheKey)obj).ref.get();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class CacheValue {
/* 175 */     private final ReadWriteLock lock = new ReentrantReadWriteLock();
/*     */     
/*     */     private WeakReference<ExpressionFactory> ref;
/*     */     
/*     */ 
/*     */     public ReadWriteLock getLock()
/*     */     {
/* 182 */       return this.lock;
/*     */     }
/*     */     
/*     */     public ExpressionFactory getExpressionFactory() {
/* 186 */       return this.ref != null ? (ExpressionFactory)this.ref.get() : null;
/*     */     }
/*     */     
/*     */     public void setExpressionFactory(ExpressionFactory factory) {
/* 190 */       this.ref = new WeakReference(factory);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static Method findMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes, Object[] paramValues)
/*     */   {
/* 202 */     if ((clazz == null) || (methodName == null))
/*     */     {
/* 204 */       throw new MethodNotFoundException(message(null, "util.method.notfound", new Object[] { clazz, methodName, 
/* 205 */         paramString(paramTypes) }));
/*     */     }
/*     */     
/* 208 */     if (paramTypes == null) {
/* 209 */       paramTypes = getTypesFromValues(paramValues);
/*     */     }
/*     */     
/* 212 */     Method[] methods = clazz.getMethods();
/*     */     
/* 214 */     List<Wrapper> wrappers = Wrapper.wrap(methods, methodName);
/*     */     
/* 216 */     Wrapper result = findWrapper(clazz, wrappers, methodName, paramTypes, paramValues);
/*     */     
/* 218 */     return getMethod(clazz, (Method)result.unWrap());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Wrapper findWrapper(Class<?> clazz, List<Wrapper> wrappers, String name, Class<?>[] paramTypes, Object[] paramValues)
/*     */   {
/* 229 */     Map<Wrapper, MatchResult> candidates = new HashMap();
/*     */     
/* 231 */     int paramCount = paramTypes.length;
/*     */     
/* 233 */     for (Wrapper w : wrappers) {
/* 234 */       Class<?>[] mParamTypes = w.getParameterTypes();
/*     */       int mParamCount;
/* 236 */       if (mParamTypes == null) {
/* 237 */         mParamCount = 0;
/*     */       } else {
/* 239 */         mParamCount = mParamTypes.length;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 244 */       if (((w.isVarArgs()) || (paramCount == mParamCount)) && 
/*     */       
/*     */ 
/*     */ 
/* 248 */         ((!w.isVarArgs()) || (paramCount >= mParamCount - 1)) && 
/*     */         
/*     */ 
/*     */ 
/* 252 */         ((!w.isVarArgs()) || (paramCount != mParamCount) || (paramValues == null) || (paramValues.length <= paramCount) || 
/* 253 */         (paramTypes[(mParamCount - 1)].isArray())) && 
/*     */         
/*     */ 
/*     */ 
/* 257 */         ((!w.isVarArgs()) || (paramCount <= mParamCount) || (paramValues == null) || (paramValues.length == paramCount)) && (
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 262 */         (w.isVarArgs()) || (paramValues == null) || (paramCount == paramValues.length)))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 268 */         int exactMatch = 0;
/* 269 */         int assignableMatch = 0;
/* 270 */         int coercibleMatch = 0;
/* 271 */         boolean noMatch = false;
/* 272 */         for (int i = 0; i < mParamCount; i++)
/*     */         {
/* 274 */           if ((w.isVarArgs()) && (i == mParamCount - 1)) {
/* 275 */             if ((i == paramCount) || ((paramValues != null) && (paramValues.length == i)))
/*     */             {
/* 277 */               assignableMatch++;
/* 278 */               break;
/*     */             }
/* 280 */             Class<?> varType = mParamTypes[i].getComponentType();
/* 281 */             for (int j = i; j < paramCount; j++) {
/* 282 */               if (isAssignableFrom(paramTypes[j], varType)) {
/* 283 */                 assignableMatch++;
/*     */               } else {
/* 285 */                 if (paramValues == null) {
/* 286 */                   noMatch = true;
/* 287 */                   break;
/*     */                 }
/* 289 */                 if (isCoercibleFrom(paramValues[j], varType)) {
/* 290 */                   coercibleMatch++;
/*     */                 } else {
/* 292 */                   noMatch = true;
/* 293 */                   break;
/*     */                 }
/*     */                 
/*     */               }
/*     */               
/*     */             }
/*     */             
/*     */ 
/*     */           }
/* 302 */           else if (mParamTypes[i].equals(paramTypes[i])) {
/* 303 */             exactMatch++;
/* 304 */           } else if ((paramTypes[i] != null) && (isAssignableFrom(paramTypes[i], mParamTypes[i]))) {
/* 305 */             assignableMatch++;
/*     */           } else {
/* 307 */             if (paramValues == null) {
/* 308 */               noMatch = true;
/* 309 */               break;
/*     */             }
/* 311 */             if (isCoercibleFrom(paramValues[i], mParamTypes[i])) {
/* 312 */               coercibleMatch++;
/*     */             } else {
/* 314 */               noMatch = true;
/* 315 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 321 */         if (!noMatch)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 327 */           if (exactMatch == paramCount) {
/* 328 */             return w;
/*     */           }
/*     */           
/* 331 */           candidates.put(w, new MatchResult(exactMatch, assignableMatch, coercibleMatch, w
/* 332 */             .isBridge()));
/*     */         }
/*     */       }
/*     */     }
/*     */     int mParamCount;
/* 337 */     MatchResult bestMatch = new MatchResult(0, 0, 0, false);
/* 338 */     Wrapper match = null;
/* 339 */     boolean multiple = false;
/* 340 */     for (Map.Entry<Wrapper, MatchResult> entry : candidates.entrySet()) {
/* 341 */       int cmp = ((MatchResult)entry.getValue()).compareTo(bestMatch);
/* 342 */       if ((cmp > 0) || (match == null)) {
/* 343 */         bestMatch = (MatchResult)entry.getValue();
/* 344 */         match = (Wrapper)entry.getKey();
/* 345 */         multiple = false;
/* 346 */       } else if (cmp == 0) {
/* 347 */         multiple = true;
/*     */       }
/*     */     }
/* 350 */     if (multiple) {
/* 351 */       if (bestMatch.getExact() == paramCount - 1)
/*     */       {
/*     */ 
/* 354 */         match = resolveAmbiguousWrapper(candidates.keySet(), paramTypes);
/*     */       } else {
/* 356 */         match = null;
/*     */       }
/*     */       
/* 359 */       if (match == null)
/*     */       {
/*     */ 
/* 362 */         throw new MethodNotFoundException(message(null, "util.method.ambiguous", new Object[] { clazz, name, 
/*     */         
/* 364 */           paramString(paramTypes) }));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 369 */     if (match == null) {
/* 370 */       throw new MethodNotFoundException(message(null, "util.method.notfound", new Object[] { clazz, name, 
/*     */       
/* 372 */         paramString(paramTypes) }));
/*     */     }
/*     */     
/* 375 */     return match;
/*     */   }
/*     */   
/*     */   private static final String paramString(Class<?>[] types)
/*     */   {
/* 380 */     if (types != null) {
/* 381 */       StringBuilder sb = new StringBuilder();
/* 382 */       for (int i = 0; i < types.length; i++) {
/* 383 */         if (types[i] == null) {
/* 384 */           sb.append("null, ");
/*     */         } else {
/* 386 */           sb.append(types[i].getName()).append(", ");
/*     */         }
/*     */       }
/* 389 */       if (sb.length() > 2) {
/* 390 */         sb.setLength(sb.length() - 2);
/*     */       }
/* 392 */       return sb.toString();
/*     */     }
/* 394 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Wrapper resolveAmbiguousWrapper(Set<Wrapper> candidates, Class<?>[] paramTypes)
/*     */   {
/* 405 */     Wrapper w = (Wrapper)candidates.iterator().next();
/*     */     
/* 407 */     int nonMatchIndex = 0;
/* 408 */     Class<?> nonMatchClass = null;
/*     */     
/* 410 */     for (int i = 0; i < paramTypes.length; i++) {
/* 411 */       if (w.getParameterTypes()[i] != paramTypes[i]) {
/* 412 */         nonMatchIndex = i;
/* 413 */         nonMatchClass = paramTypes[i];
/* 414 */         break;
/*     */       }
/*     */     }
/*     */     
/* 418 */     if (nonMatchClass == null)
/*     */     {
/* 420 */       return null;
/*     */     }
/*     */     
/* 423 */     for (i = candidates.iterator(); i.hasNext();) { c = (Wrapper)i.next();
/* 424 */       if (c.getParameterTypes()[nonMatchIndex] == paramTypes[nonMatchIndex])
/*     */       {
/*     */ 
/*     */ 
/* 428 */         return null;
/*     */       }
/*     */     }
/*     */     
/*     */     Wrapper c;
/* 433 */     Class<?> superClass = nonMatchClass.getSuperclass();
/* 434 */     Wrapper c; while (superClass != null) {
/* 435 */       for (c = candidates.iterator(); c.hasNext();) { c = (Wrapper)c.next();
/* 436 */         if (c.getParameterTypes()[nonMatchIndex].equals(superClass))
/*     */         {
/* 438 */           return c;
/*     */         }
/*     */       }
/* 441 */       superClass = superClass.getSuperclass();
/*     */     }
/*     */     
/*     */ 
/* 445 */     Wrapper match = null;
/* 446 */     if (Number.class.isAssignableFrom(nonMatchClass)) {
/* 447 */       for (Wrapper c : candidates) {
/* 448 */         Class<?> candidateType = c.getParameterTypes()[nonMatchIndex];
/* 449 */         if ((Number.class.isAssignableFrom(candidateType)) || 
/* 450 */           (candidateType.isPrimitive())) {
/* 451 */           if (match == null) {
/* 452 */             match = c;
/*     */           }
/*     */           else {
/* 455 */             match = null;
/* 456 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 462 */     return match;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static boolean isAssignableFrom(Class<?> src, Class<?> target)
/*     */   {
/* 474 */     if (src == null) {
/* 475 */       return true;
/*     */     }
/*     */     Class<?> targetClass;
/*     */     Class<?> targetClass;
/* 479 */     if (target.isPrimitive()) { Class<?> targetClass;
/* 480 */       if (target == Boolean.TYPE) {
/* 481 */         targetClass = Boolean.class; } else { Class<?> targetClass;
/* 482 */         if (target == Character.TYPE) {
/* 483 */           targetClass = Character.class; } else { Class<?> targetClass;
/* 484 */           if (target == Byte.TYPE) {
/* 485 */             targetClass = Byte.class; } else { Class<?> targetClass;
/* 486 */             if (target == Short.TYPE) {
/* 487 */               targetClass = Short.class; } else { Class<?> targetClass;
/* 488 */               if (target == Integer.TYPE) {
/* 489 */                 targetClass = Integer.class; } else { Class<?> targetClass;
/* 490 */                 if (target == Long.TYPE) {
/* 491 */                   targetClass = Long.class; } else { Class<?> targetClass;
/* 492 */                   if (target == Float.TYPE) {
/* 493 */                     targetClass = Float.class;
/*     */                   } else
/* 495 */                     targetClass = Double.class;
/*     */                 }
/*     */               }
/* 498 */             } } } } } else { targetClass = target;
/*     */     }
/* 500 */     return targetClass.isAssignableFrom(src);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isCoercibleFrom(Object src, Class<?> target)
/*     */   {
/*     */     try
/*     */     {
/* 512 */       getExpressionFactory().coerceToType(src, target);
/*     */     } catch (ELException e) {
/* 514 */       return false;
/*     */     }
/* 516 */     return true;
/*     */   }
/*     */   
/*     */   private static Class<?>[] getTypesFromValues(Object[] values)
/*     */   {
/* 521 */     if (values == null) {
/* 522 */       return EMPTY_CLASS_ARRAY;
/*     */     }
/*     */     
/* 525 */     Class<?>[] result = new Class[values.length];
/* 526 */     for (int i = 0; i < values.length; i++) {
/* 527 */       if (values[i] == null) {
/* 528 */         result[i] = null;
/*     */       } else {
/* 530 */         result[i] = values[i].getClass();
/*     */       }
/*     */     }
/* 533 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static Method getMethod(Class<?> type, Method m)
/*     */   {
/* 542 */     if ((m == null) || (Modifier.isPublic(type.getModifiers()))) {
/* 543 */       return m;
/*     */     }
/* 545 */     Class<?>[] inf = type.getInterfaces();
/* 546 */     Method mp = null;
/* 547 */     for (int i = 0; i < inf.length; i++) {
/*     */       try {
/* 549 */         mp = inf[i].getMethod(m.getName(), m.getParameterTypes());
/* 550 */         mp = getMethod(mp.getDeclaringClass(), mp);
/* 551 */         if (mp != null) {
/* 552 */           return mp;
/*     */         }
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException) {}
/*     */     }
/*     */     
/* 558 */     Class<?> sup = type.getSuperclass();
/* 559 */     if (sup != null) {
/*     */       try {
/* 561 */         mp = sup.getMethod(m.getName(), m.getParameterTypes());
/* 562 */         mp = getMethod(mp.getDeclaringClass(), mp);
/* 563 */         if (mp != null) {
/* 564 */           return mp;
/*     */         }
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException1) {}
/*     */     }
/*     */     
/* 570 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   static Constructor<?> findConstructor(Class<?> clazz, Class<?>[] paramTypes, Object[] paramValues)
/*     */   {
/* 577 */     String methodName = "<init>";
/*     */     
/* 579 */     if (clazz == null)
/*     */     {
/* 581 */       throw new MethodNotFoundException(message(null, "util.method.notfound", new Object[] { null, methodName, 
/* 582 */         paramString(paramTypes) }));
/*     */     }
/*     */     
/* 585 */     if (paramTypes == null) {
/* 586 */       paramTypes = getTypesFromValues(paramValues);
/*     */     }
/*     */     
/* 589 */     Constructor<?>[] constructors = clazz.getConstructors();
/*     */     
/* 591 */     List<Wrapper> wrappers = Wrapper.wrap(constructors);
/*     */     
/* 593 */     Wrapper result = findWrapper(clazz, wrappers, methodName, paramTypes, paramValues);
/*     */     
/* 595 */     return getConstructor(clazz, (Constructor)result.unWrap());
/*     */   }
/*     */   
/*     */   static Constructor<?> getConstructor(Class<?> type, Constructor<?> c)
/*     */   {
/* 600 */     if ((c == null) || (Modifier.isPublic(type.getModifiers()))) {
/* 601 */       return c;
/*     */     }
/* 603 */     Constructor<?> cp = null;
/* 604 */     Class<?> sup = type.getSuperclass();
/* 605 */     if (sup != null) {
/*     */       try {
/* 607 */         cp = sup.getConstructor(c.getParameterTypes());
/* 608 */         cp = getConstructor(cp.getDeclaringClass(), cp);
/* 609 */         if (cp != null) {
/* 610 */           return cp;
/*     */         }
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException) {}
/*     */     }
/*     */     
/* 616 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   static Object[] buildParameters(Class<?>[] parameterTypes, boolean isVarArgs, Object[] params)
/*     */   {
/* 622 */     ExpressionFactory factory = getExpressionFactory();
/* 623 */     Object[] parameters = null;
/* 624 */     if (parameterTypes.length > 0) {
/* 625 */       parameters = new Object[parameterTypes.length];
/*     */       
/* 627 */       if (params == null) {
/* 628 */         params = EMPTY_OBJECT_ARRAY;
/*     */       }
/* 630 */       int paramCount = params.length;
/* 631 */       if (isVarArgs) {
/* 632 */         int varArgIndex = parameterTypes.length - 1;
/*     */         
/* 634 */         for (int i = 0; i < varArgIndex; i++) {
/* 635 */           parameters[i] = factory.coerceToType(params[i], parameterTypes[i]);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 640 */         Class<?> varArgClass = parameterTypes[varArgIndex].getComponentType();
/* 641 */         Object varargs = Array.newInstance(varArgClass, paramCount - varArgIndex);
/*     */         
/*     */ 
/* 644 */         for (int i = varArgIndex; i < paramCount; i++) {
/* 645 */           Array.set(varargs, i - varArgIndex, factory
/* 646 */             .coerceToType(params[i], varArgClass));
/*     */         }
/* 648 */         parameters[varArgIndex] = varargs;
/*     */       } else {
/* 650 */         parameters = new Object[parameterTypes.length];
/* 651 */         for (int i = 0; i < parameterTypes.length; i++) {
/* 652 */           parameters[i] = factory.coerceToType(params[i], parameterTypes[i]);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 657 */     return parameters;
/*     */   }
/*     */   
/*     */   private static abstract class Wrapper
/*     */   {
/*     */     public static List<Wrapper> wrap(Method[] methods, String name)
/*     */     {
/* 664 */       List<Wrapper> result = new ArrayList();
/* 665 */       for (Method method : methods) {
/* 666 */         if (method.getName().equals(name)) {
/* 667 */           result.add(new Util.MethodWrapper(method));
/*     */         }
/*     */       }
/* 670 */       return result;
/*     */     }
/*     */     
/*     */     public static List<Wrapper> wrap(Constructor<?>[] constructors) {
/* 674 */       List<Wrapper> result = new ArrayList();
/* 675 */       for (Constructor<?> constructor : constructors) {
/* 676 */         result.add(new Util.ConstructorWrapper(constructor));
/*     */       }
/* 678 */       return result;
/*     */     }
/*     */     
/*     */     public abstract Object unWrap();
/*     */     
/*     */     public abstract Class<?>[] getParameterTypes();
/*     */     
/*     */     public abstract boolean isVarArgs();
/*     */     
/*     */     public abstract boolean isBridge(); }
/*     */   
/*     */   private static class MethodWrapper extends Util.Wrapper { private final Method m;
/*     */     
/* 691 */     public MethodWrapper(Method m) { super();
/* 692 */       this.m = m;
/*     */     }
/*     */     
/*     */     public Object unWrap()
/*     */     {
/* 697 */       return this.m;
/*     */     }
/*     */     
/*     */     public Class<?>[] getParameterTypes()
/*     */     {
/* 702 */       return this.m.getParameterTypes();
/*     */     }
/*     */     
/*     */     public boolean isVarArgs()
/*     */     {
/* 707 */       return this.m.isVarArgs();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 712 */     public boolean isBridge() { return this.m.isBridge(); }
/*     */   }
/*     */   
/*     */   private static class ConstructorWrapper extends Util.Wrapper {
/*     */     private final Constructor<?> c;
/*     */     
/*     */     public ConstructorWrapper(Constructor<?> c) {
/* 719 */       super();
/* 720 */       this.c = c;
/*     */     }
/*     */     
/*     */     public Object unWrap()
/*     */     {
/* 725 */       return this.c;
/*     */     }
/*     */     
/*     */     public Class<?>[] getParameterTypes()
/*     */     {
/* 730 */       return this.c.getParameterTypes();
/*     */     }
/*     */     
/*     */     public boolean isVarArgs()
/*     */     {
/* 735 */       return this.c.isVarArgs();
/*     */     }
/*     */     
/*     */     public boolean isBridge()
/*     */     {
/* 740 */       return false;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static class MatchResult
/*     */     implements Comparable<MatchResult>
/*     */   {
/*     */     private final int exact;
/*     */     
/*     */     private final int assignable;
/*     */     private final int coercible;
/*     */     private final boolean bridge;
/*     */     
/*     */     public MatchResult(int exact, int assignable, int coercible, boolean bridge)
/*     */     {
/* 756 */       this.exact = exact;
/* 757 */       this.assignable = assignable;
/* 758 */       this.coercible = coercible;
/* 759 */       this.bridge = bridge;
/*     */     }
/*     */     
/*     */     public int getExact() {
/* 763 */       return this.exact;
/*     */     }
/*     */     
/*     */     public int getAssignable() {
/* 767 */       return this.assignable;
/*     */     }
/*     */     
/*     */     public int getCoercible() {
/* 771 */       return this.coercible;
/*     */     }
/*     */     
/*     */     public boolean isBridge() {
/* 775 */       return this.bridge;
/*     */     }
/*     */     
/*     */     public int compareTo(MatchResult o)
/*     */     {
/* 780 */       int cmp = Integer.compare(getExact(), o.getExact());
/* 781 */       if (cmp == 0) {
/* 782 */         cmp = Integer.compare(getAssignable(), o.getAssignable());
/* 783 */         if (cmp == 0) {
/* 784 */           cmp = Integer.compare(getCoercible(), o.getCoercible());
/* 785 */           if (cmp == 0)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 790 */             cmp = Boolean.compare(o.isBridge(), isBridge());
/*     */           }
/*     */         }
/*     */       }
/* 794 */       return cmp;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\javax\el\Util.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */