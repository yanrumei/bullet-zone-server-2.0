/*     */ package org.apache.el.util;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.el.ELException;
/*     */ import javax.el.MethodNotFoundException;
/*     */ import org.apache.el.lang.ELSupport;
/*     */ import org.apache.el.lang.EvaluationContext;
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
/*     */ public class ReflectionUtil
/*     */ {
/*  41 */   protected static final String[] PRIMITIVE_NAMES = { "boolean", "byte", "char", "double", "float", "int", "long", "short", "void" };
/*     */   
/*     */ 
/*  44 */   protected static final Class<?>[] PRIMITIVES = { Boolean.TYPE, Byte.TYPE, Character.TYPE, Double.TYPE, Float.TYPE, Integer.TYPE, Long.TYPE, Short.TYPE, Void.TYPE };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class<?> forName(String name)
/*     */     throws ClassNotFoundException
/*     */   {
/*  53 */     if ((null == name) || ("".equals(name))) {
/*  54 */       return null;
/*     */     }
/*  56 */     Class<?> c = forNamePrimitive(name);
/*  57 */     if (c == null) {
/*  58 */       if (name.endsWith("[]")) {
/*  59 */         String nc = name.substring(0, name.length() - 2);
/*  60 */         c = Class.forName(nc, true, Thread.currentThread().getContextClassLoader());
/*  61 */         c = Array.newInstance(c, 0).getClass();
/*     */       } else {
/*  63 */         c = Class.forName(name, true, Thread.currentThread().getContextClassLoader());
/*     */       }
/*     */     }
/*  66 */     return c;
/*     */   }
/*     */   
/*     */   protected static Class<?> forNamePrimitive(String name) {
/*  70 */     if (name.length() <= 8) {
/*  71 */       int p = Arrays.binarySearch(PRIMITIVE_NAMES, name);
/*  72 */       if (p >= 0) {
/*  73 */         return PRIMITIVES[p];
/*     */       }
/*     */     }
/*  76 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Class<?>[] toTypeArray(String[] s)
/*     */     throws ClassNotFoundException
/*     */   {
/*  88 */     if (s == null)
/*  89 */       return null;
/*  90 */     Class<?>[] c = new Class[s.length];
/*  91 */     for (int i = 0; i < s.length; i++) {
/*  92 */       c[i] = forName(s[i]);
/*     */     }
/*  94 */     return c;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static String[] toTypeNameArray(Class<?>[] c)
/*     */   {
/* 104 */     if (c == null)
/* 105 */       return null;
/* 106 */     String[] s = new String[c.length];
/* 107 */     for (int i = 0; i < c.length; i++) {
/* 108 */       s[i] = c[i].getName();
/*     */     }
/* 110 */     return s;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Method getMethod(EvaluationContext ctx, Object base, Object property, Class<?>[] paramTypes, Object[] paramValues)
/*     */     throws MethodNotFoundException
/*     */   {
/* 133 */     if ((base == null) || (property == null)) {
/* 134 */       throw new MethodNotFoundException(MessageFactory.get("error.method.notfound", new Object[] { base, property, 
/*     */       
/* 136 */         paramString(paramTypes) }));
/*     */     }
/*     */     
/*     */ 
/* 140 */     String methodName = (property instanceof String) ? (String)property : property.toString();
/*     */     int paramCount;
/*     */     int paramCount;
/* 143 */     if (paramTypes == null) {
/* 144 */       paramCount = 0;
/*     */     } else {
/* 146 */       paramCount = paramTypes.length;
/*     */     }
/*     */     
/* 149 */     Method[] methods = base.getClass().getMethods();
/* 150 */     Map<Method, MatchResult> candidates = new HashMap();
/*     */     Method m;
/* 152 */     for (m : methods) {
/* 153 */       if (m.getName().equals(methodName))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 158 */         Class<?>[] mParamTypes = m.getParameterTypes();
/*     */         int mParamCount;
/* 160 */         int mParamCount; if (mParamTypes == null) {
/* 161 */           mParamCount = 0;
/*     */         } else {
/* 163 */           mParamCount = mParamTypes.length;
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 168 */         if ((m.isVarArgs()) || (paramCount == mParamCount))
/*     */         {
/*     */ 
/*     */ 
/* 172 */           if ((!m.isVarArgs()) || (paramCount >= mParamCount - 1))
/*     */           {
/*     */ 
/*     */ 
/* 176 */             if ((!m.isVarArgs()) || (paramCount != mParamCount) || (paramValues == null) || (paramValues.length <= paramCount) || 
/* 177 */               (paramTypes[(mParamCount - 1)].isArray()))
/*     */             {
/*     */ 
/*     */ 
/* 181 */               if ((!m.isVarArgs()) || (paramCount <= mParamCount) || (paramValues == null) || (paramValues.length == paramCount))
/*     */               {
/*     */ 
/*     */ 
/*     */ 
/* 186 */                 if ((m.isVarArgs()) || (paramValues == null) || (paramCount == paramValues.length))
/*     */                 {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 192 */                   int exactMatch = 0;
/* 193 */                   int assignableMatch = 0;
/* 194 */                   int coercibleMatch = 0;
/* 195 */                   boolean noMatch = false;
/* 196 */                   for (int i = 0; i < mParamCount; i++)
/*     */                   {
/* 198 */                     if ((m.isVarArgs()) && (i == mParamCount - 1)) {
/* 199 */                       if ((i == paramCount) || ((paramValues != null) && (paramValues.length == i)))
/*     */                       {
/* 201 */                         assignableMatch++;
/* 202 */                         break;
/*     */                       }
/* 204 */                       Class<?> varType = mParamTypes[i].getComponentType();
/* 205 */                       for (int j = i; j < paramCount; j++) {
/* 206 */                         if (isAssignableFrom(paramTypes[j], varType)) {
/* 207 */                           assignableMatch++;
/*     */                         } else {
/* 209 */                           if (paramValues == null) {
/* 210 */                             noMatch = true;
/* 211 */                             break;
/*     */                           }
/* 213 */                           if (isCoercibleFrom(ctx, paramValues[j], varType)) {
/* 214 */                             coercibleMatch++;
/*     */                           } else {
/* 216 */                             noMatch = true;
/* 217 */                             break;
/*     */                           }
/*     */                           
/*     */                         }
/*     */                         
/*     */                       }
/*     */                       
/*     */ 
/*     */                     }
/* 226 */                     else if (mParamTypes[i].equals(paramTypes[i])) {
/* 227 */                       exactMatch++;
/* 228 */                     } else if ((paramTypes[i] != null) && (isAssignableFrom(paramTypes[i], mParamTypes[i]))) {
/* 229 */                       assignableMatch++;
/*     */                     } else {
/* 231 */                       if (paramValues == null) {
/* 232 */                         noMatch = true;
/* 233 */                         break;
/*     */                       }
/* 235 */                       if (isCoercibleFrom(ctx, paramValues[i], mParamTypes[i])) {
/* 236 */                         coercibleMatch++;
/*     */                       } else {
/* 238 */                         noMatch = true;
/* 239 */                         break;
/*     */                       }
/*     */                     }
/*     */                   }
/*     */                   
/*     */ 
/* 245 */                   if (!noMatch)
/*     */                   {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 251 */                     if (exactMatch == paramCount) {
/* 252 */                       return getMethod(base.getClass(), m);
/*     */                     }
/*     */                     
/* 255 */                     candidates.put(m, new MatchResult(exactMatch, assignableMatch, coercibleMatch, m
/* 256 */                       .isBridge()));
/*     */                   }
/*     */                 } } } } }
/*     */       }
/*     */     }
/* 261 */     MatchResult bestMatch = new MatchResult(0, 0, 0, false);
/* 262 */     Method match = null;
/* 263 */     boolean multiple = false;
/* 264 */     for (Map.Entry<Method, MatchResult> entry : candidates.entrySet()) {
/* 265 */       int cmp = ((MatchResult)entry.getValue()).compareTo(bestMatch);
/* 266 */       if ((cmp > 0) || (match == null)) {
/* 267 */         bestMatch = (MatchResult)entry.getValue();
/* 268 */         match = (Method)entry.getKey();
/* 269 */         multiple = false;
/* 270 */       } else if (cmp == 0) {
/* 271 */         multiple = true;
/*     */       }
/*     */     }
/* 274 */     if (multiple) {
/* 275 */       if (bestMatch.getExact() == paramCount - 1)
/*     */       {
/*     */ 
/* 278 */         match = resolveAmbiguousMethod(candidates.keySet(), paramTypes);
/*     */       } else {
/* 280 */         match = null;
/*     */       }
/*     */       
/* 283 */       if (match == null)
/*     */       {
/*     */ 
/* 286 */         throw new MethodNotFoundException(MessageFactory.get("error.method.ambiguous", new Object[] { base, property, 
/*     */         
/* 288 */           paramString(paramTypes) }));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 293 */     if (match == null) {
/* 294 */       throw new MethodNotFoundException(MessageFactory.get("error.method.notfound", new Object[] { base, property, 
/*     */       
/* 296 */         paramString(paramTypes) }));
/*     */     }
/*     */     
/* 299 */     return getMethod(base.getClass(), match);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Method resolveAmbiguousMethod(Set<Method> candidates, Class<?>[] paramTypes)
/*     */   {
/* 309 */     Method m = (Method)candidates.iterator().next();
/*     */     
/* 311 */     int nonMatchIndex = 0;
/* 312 */     Class<?> nonMatchClass = null;
/*     */     
/* 314 */     for (int i = 0; i < paramTypes.length; i++) {
/* 315 */       if (m.getParameterTypes()[i] != paramTypes[i]) {
/* 316 */         nonMatchIndex = i;
/* 317 */         nonMatchClass = paramTypes[i];
/* 318 */         break;
/*     */       }
/*     */     }
/*     */     
/* 322 */     if (nonMatchClass == null)
/*     */     {
/* 324 */       return null;
/*     */     }
/*     */     
/* 327 */     for (i = candidates.iterator(); i.hasNext();) { c = (Method)i.next();
/* 328 */       if (c.getParameterTypes()[nonMatchIndex] == paramTypes[nonMatchIndex])
/*     */       {
/*     */ 
/*     */ 
/* 332 */         return null;
/*     */       }
/*     */     }
/*     */     
/*     */     Method c;
/* 337 */     Class<?> superClass = nonMatchClass.getSuperclass();
/* 338 */     Method c; while (superClass != null) {
/* 339 */       for (c = candidates.iterator(); c.hasNext();) { c = (Method)c.next();
/* 340 */         if (c.getParameterTypes()[nonMatchIndex].equals(superClass))
/*     */         {
/* 342 */           return c;
/*     */         }
/*     */       }
/* 345 */       superClass = superClass.getSuperclass();
/*     */     }
/*     */     
/*     */ 
/* 349 */     Method match = null;
/* 350 */     if (Number.class.isAssignableFrom(nonMatchClass)) {
/* 351 */       for (Method c : candidates) {
/* 352 */         Class<?> candidateType = c.getParameterTypes()[nonMatchIndex];
/* 353 */         if ((Number.class.isAssignableFrom(candidateType)) || 
/* 354 */           (candidateType.isPrimitive())) {
/* 355 */           if (match == null) {
/* 356 */             match = c;
/*     */           }
/*     */           else {
/* 359 */             match = null;
/* 360 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 366 */     return match;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isAssignableFrom(Class<?> src, Class<?> target)
/*     */   {
/* 378 */     if (src == null) {
/* 379 */       return true;
/*     */     }
/*     */     Class<?> targetClass;
/*     */     Class<?> targetClass;
/* 383 */     if (target.isPrimitive()) { Class<?> targetClass;
/* 384 */       if (target == Boolean.TYPE) {
/* 385 */         targetClass = Boolean.class; } else { Class<?> targetClass;
/* 386 */         if (target == Character.TYPE) {
/* 387 */           targetClass = Character.class; } else { Class<?> targetClass;
/* 388 */           if (target == Byte.TYPE) {
/* 389 */             targetClass = Byte.class; } else { Class<?> targetClass;
/* 390 */             if (target == Short.TYPE) {
/* 391 */               targetClass = Short.class; } else { Class<?> targetClass;
/* 392 */               if (target == Integer.TYPE) {
/* 393 */                 targetClass = Integer.class; } else { Class<?> targetClass;
/* 394 */                 if (target == Long.TYPE) {
/* 395 */                   targetClass = Long.class; } else { Class<?> targetClass;
/* 396 */                   if (target == Float.TYPE) {
/* 397 */                     targetClass = Float.class;
/*     */                   } else
/* 399 */                     targetClass = Double.class;
/*     */                 }
/*     */               }
/* 402 */             } } } } } else { targetClass = target;
/*     */     }
/* 404 */     return targetClass.isAssignableFrom(src);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static boolean isCoercibleFrom(EvaluationContext ctx, Object src, Class<?> target)
/*     */   {
/*     */     try
/*     */     {
/* 416 */       ELSupport.coerceToType(ctx, src, target);
/*     */     } catch (ELException e) {
/* 418 */       return false;
/*     */     }
/* 420 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Method getMethod(Class<?> type, Method m)
/*     */   {
/* 429 */     if ((m == null) || (Modifier.isPublic(type.getModifiers()))) {
/* 430 */       return m;
/*     */     }
/* 432 */     Class<?>[] inf = type.getInterfaces();
/* 433 */     Method mp = null;
/* 434 */     for (int i = 0; i < inf.length; i++) {
/*     */       try {
/* 436 */         mp = inf[i].getMethod(m.getName(), m.getParameterTypes());
/* 437 */         mp = getMethod(mp.getDeclaringClass(), mp);
/* 438 */         if (mp != null) {
/* 439 */           return mp;
/*     */         }
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException) {}
/*     */     }
/*     */     
/* 445 */     Class<?> sup = type.getSuperclass();
/* 446 */     if (sup != null) {
/*     */       try {
/* 448 */         mp = sup.getMethod(m.getName(), m.getParameterTypes());
/* 449 */         mp = getMethod(mp.getDeclaringClass(), mp);
/* 450 */         if (mp != null) {
/* 451 */           return mp;
/*     */         }
/*     */       }
/*     */       catch (NoSuchMethodException localNoSuchMethodException1) {}
/*     */     }
/*     */     
/* 457 */     return null;
/*     */   }
/*     */   
/*     */   private static final String paramString(Class<?>[] types)
/*     */   {
/* 462 */     if (types != null) {
/* 463 */       StringBuilder sb = new StringBuilder();
/* 464 */       for (int i = 0; i < types.length; i++) {
/* 465 */         if (types[i] == null) {
/* 466 */           sb.append("null, ");
/*     */         } else {
/* 468 */           sb.append(types[i].getName()).append(", ");
/*     */         }
/*     */       }
/* 471 */       if (sb.length() > 2) {
/* 472 */         sb.setLength(sb.length() - 2);
/*     */       }
/* 474 */       return sb.toString();
/*     */     }
/* 476 */     return null;
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
/* 491 */       this.exact = exact;
/* 492 */       this.assignable = assignable;
/* 493 */       this.coercible = coercible;
/* 494 */       this.bridge = bridge;
/*     */     }
/*     */     
/*     */     public int getExact() {
/* 498 */       return this.exact;
/*     */     }
/*     */     
/*     */     public int getAssignable() {
/* 502 */       return this.assignable;
/*     */     }
/*     */     
/*     */     public int getCoercible() {
/* 506 */       return this.coercible;
/*     */     }
/*     */     
/*     */     public boolean isBridge() {
/* 510 */       return this.bridge;
/*     */     }
/*     */     
/*     */     public int compareTo(MatchResult o)
/*     */     {
/* 515 */       int cmp = Integer.compare(getExact(), o.getExact());
/* 516 */       if (cmp == 0) {
/* 517 */         cmp = Integer.compare(getAssignable(), o.getAssignable());
/* 518 */         if (cmp == 0) {
/* 519 */           cmp = Integer.compare(getCoercible(), o.getCoercible());
/* 520 */           if (cmp == 0)
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 525 */             cmp = Boolean.compare(o.isBridge(), isBridge());
/*     */           }
/*     */         }
/*     */       }
/* 529 */       return cmp;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\tomcat-embed-el-8.5.27.jar!\org\apache\e\\util\ReflectionUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */