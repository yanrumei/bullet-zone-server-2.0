/*     */ package org.springframework.expression.spel.support;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.springframework.asm.MethodVisitor;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.convert.Property;
/*     */ import org.springframework.core.convert.TypeDescriptor;
/*     */ import org.springframework.expression.AccessException;
/*     */ import org.springframework.expression.EvaluationContext;
/*     */ import org.springframework.expression.EvaluationException;
/*     */ import org.springframework.expression.PropertyAccessor;
/*     */ import org.springframework.expression.TypeConverter;
/*     */ import org.springframework.expression.TypedValue;
/*     */ import org.springframework.expression.spel.CodeFlow;
/*     */ import org.springframework.expression.spel.CompilablePropertyAccessor;
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
/*     */ public class ReflectivePropertyAccessor
/*     */   implements PropertyAccessor
/*     */ {
/*  60 */   private static final Set<Class<?>> ANY_TYPES = ;
/*     */   private static final Set<Class<?>> BOOLEAN_TYPES;
/*     */   
/*     */   static
/*     */   {
/*  65 */     Set<Class<?>> booleanTypes = new HashSet();
/*  66 */     booleanTypes.add(Boolean.class);
/*  67 */     booleanTypes.add(Boolean.TYPE);
/*  68 */     BOOLEAN_TYPES = Collections.unmodifiableSet(booleanTypes);
/*     */   }
/*     */   
/*     */ 
/*  72 */   private final Map<PropertyCacheKey, InvokerPair> readerCache = new ConcurrentHashMap(64);
/*     */   
/*     */ 
/*  75 */   private final Map<PropertyCacheKey, Member> writerCache = new ConcurrentHashMap(64);
/*     */   
/*     */ 
/*  78 */   private final Map<PropertyCacheKey, TypeDescriptor> typeDescriptorCache = new ConcurrentHashMap(64);
/*     */   
/*     */ 
/*     */ 
/*     */   private InvokerPair lastReadInvokerPair;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?>[] getSpecificTargetClasses()
/*     */   {
/*  89 */     return null;
/*     */   }
/*     */   
/*     */   public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException
/*     */   {
/*  94 */     if (target == null) {
/*  95 */       return false;
/*     */     }
/*  97 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/*  98 */     if ((type.isArray()) && (name.equals("length"))) {
/*  99 */       return true;
/*     */     }
/* 101 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 102 */     if (this.readerCache.containsKey(cacheKey)) {
/* 103 */       return true;
/*     */     }
/* 105 */     Method method = findGetterForProperty(name, type, target);
/* 106 */     if (method != null)
/*     */     {
/*     */ 
/* 109 */       Property property = new Property(type, method, null);
/* 110 */       TypeDescriptor typeDescriptor = new TypeDescriptor(property);
/* 111 */       this.readerCache.put(cacheKey, new InvokerPair(method, typeDescriptor));
/* 112 */       this.typeDescriptorCache.put(cacheKey, typeDescriptor);
/* 113 */       return true;
/*     */     }
/*     */     
/* 116 */     Field field = findField(name, type, target);
/* 117 */     if (field != null) {
/* 118 */       TypeDescriptor typeDescriptor = new TypeDescriptor(field);
/* 119 */       this.readerCache.put(cacheKey, new InvokerPair(field, typeDescriptor));
/* 120 */       this.typeDescriptorCache.put(cacheKey, typeDescriptor);
/* 121 */       return true;
/*     */     }
/*     */     
/* 124 */     return false;
/*     */   }
/*     */   
/*     */   public Member getLastReadInvokerPair() {
/* 128 */     return this.lastReadInvokerPair.member;
/*     */   }
/*     */   
/*     */   public TypedValue read(EvaluationContext context, Object target, String name) throws AccessException
/*     */   {
/* 133 */     if (target == null) {
/* 134 */       throw new AccessException("Cannot read property of null target");
/*     */     }
/* 136 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/*     */     
/* 138 */     if ((type.isArray()) && (name.equals("length"))) {
/* 139 */       if ((target instanceof Class)) {
/* 140 */         throw new AccessException("Cannot access length on array class itself");
/*     */       }
/* 142 */       return new TypedValue(Integer.valueOf(Array.getLength(target)));
/*     */     }
/*     */     
/* 145 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 146 */     InvokerPair invoker = (InvokerPair)this.readerCache.get(cacheKey);
/* 147 */     this.lastReadInvokerPair = invoker;
/*     */     
/* 149 */     if ((invoker == null) || ((invoker.member instanceof Method))) {
/* 150 */       Method method = (Method)(invoker != null ? invoker.member : null);
/* 151 */       if (method == null) {
/* 152 */         method = findGetterForProperty(name, type, target);
/* 153 */         if (method != null)
/*     */         {
/*     */ 
/*     */ 
/* 157 */           Property property = new Property(type, method, null);
/* 158 */           TypeDescriptor typeDescriptor = new TypeDescriptor(property);
/* 159 */           invoker = new InvokerPair(method, typeDescriptor);
/* 160 */           this.lastReadInvokerPair = invoker;
/* 161 */           this.readerCache.put(cacheKey, invoker);
/*     */         }
/*     */       }
/* 164 */       if (method != null) {
/*     */         try {
/* 166 */           ReflectionUtils.makeAccessible(method);
/* 167 */           Object value = method.invoke(target, new Object[0]);
/* 168 */           return new TypedValue(value, invoker.typeDescriptor.narrow(value));
/*     */         }
/*     */         catch (Exception ex) {
/* 171 */           throw new AccessException("Unable to access property '" + name + "' through getter method", ex);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 176 */     if ((invoker == null) || ((invoker.member instanceof Field))) {
/* 177 */       Field field = (Field)(invoker == null ? null : invoker.member);
/* 178 */       if (field == null) {
/* 179 */         field = findField(name, type, target);
/* 180 */         if (field != null) {
/* 181 */           invoker = new InvokerPair(field, new TypeDescriptor(field));
/* 182 */           this.lastReadInvokerPair = invoker;
/* 183 */           this.readerCache.put(cacheKey, invoker);
/*     */         }
/*     */       }
/* 186 */       if (field != null) {
/*     */         try {
/* 188 */           ReflectionUtils.makeAccessible(field);
/* 189 */           Object value = field.get(target);
/* 190 */           return new TypedValue(value, invoker.typeDescriptor.narrow(value));
/*     */         }
/*     */         catch (Exception ex) {
/* 193 */           throw new AccessException("Unable to access field '" + name + "'", ex);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 198 */     throw new AccessException("Neither getter method nor field found for property '" + name + "'");
/*     */   }
/*     */   
/*     */   public boolean canWrite(EvaluationContext context, Object target, String name) throws AccessException
/*     */   {
/* 203 */     if (target == null) {
/* 204 */       return false;
/*     */     }
/* 206 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 207 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 208 */     if (this.writerCache.containsKey(cacheKey)) {
/* 209 */       return true;
/*     */     }
/* 211 */     Method method = findSetterForProperty(name, type, target);
/* 212 */     if (method != null)
/*     */     {
/* 214 */       Property property = new Property(type, null, method);
/* 215 */       TypeDescriptor typeDescriptor = new TypeDescriptor(property);
/* 216 */       this.writerCache.put(cacheKey, method);
/* 217 */       this.typeDescriptorCache.put(cacheKey, typeDescriptor);
/* 218 */       return true;
/*     */     }
/*     */     
/* 221 */     Field field = findField(name, type, target);
/* 222 */     if (field != null) {
/* 223 */       this.writerCache.put(cacheKey, field);
/* 224 */       this.typeDescriptorCache.put(cacheKey, new TypeDescriptor(field));
/* 225 */       return true;
/*     */     }
/*     */     
/* 228 */     return false;
/*     */   }
/*     */   
/*     */   public void write(EvaluationContext context, Object target, String name, Object newValue) throws AccessException
/*     */   {
/* 233 */     if (target == null) {
/* 234 */       throw new AccessException("Cannot write property on null target");
/*     */     }
/* 236 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/*     */     
/* 238 */     Object possiblyConvertedNewValue = newValue;
/* 239 */     TypeDescriptor typeDescriptor = getTypeDescriptor(context, target, name);
/* 240 */     if (typeDescriptor != null) {
/*     */       try {
/* 242 */         possiblyConvertedNewValue = context.getTypeConverter().convertValue(newValue, 
/* 243 */           TypeDescriptor.forObject(newValue), typeDescriptor);
/*     */       }
/*     */       catch (EvaluationException evaluationException) {
/* 246 */         throw new AccessException("Type conversion failure", evaluationException);
/*     */       }
/*     */     }
/* 249 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 250 */     Member cachedMember = (Member)this.writerCache.get(cacheKey);
/*     */     
/* 252 */     if ((cachedMember == null) || ((cachedMember instanceof Method))) {
/* 253 */       Method method = (Method)cachedMember;
/* 254 */       if (method == null) {
/* 255 */         method = findSetterForProperty(name, type, target);
/* 256 */         if (method != null) {
/* 257 */           cachedMember = method;
/* 258 */           this.writerCache.put(cacheKey, cachedMember);
/*     */         }
/*     */       }
/* 261 */       if (method != null) {
/*     */         try {
/* 263 */           ReflectionUtils.makeAccessible(method);
/* 264 */           method.invoke(target, new Object[] { possiblyConvertedNewValue });
/* 265 */           return;
/*     */         }
/*     */         catch (Exception ex) {
/* 268 */           throw new AccessException("Unable to access property '" + name + "' through setter method", ex);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 273 */     if ((cachedMember == null) || ((cachedMember instanceof Field))) {
/* 274 */       Field field = (Field)cachedMember;
/* 275 */       if (field == null) {
/* 276 */         field = findField(name, type, target);
/* 277 */         if (field != null) {
/* 278 */           cachedMember = field;
/* 279 */           this.writerCache.put(cacheKey, cachedMember);
/*     */         }
/*     */       }
/* 282 */       if (field != null) {
/*     */         try {
/* 284 */           ReflectionUtils.makeAccessible(field);
/* 285 */           field.set(target, possiblyConvertedNewValue);
/* 286 */           return;
/*     */         }
/*     */         catch (Exception ex) {
/* 289 */           throw new AccessException("Unable to access field '" + name + "'", ex);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 294 */     throw new AccessException("Neither setter method nor field found for property '" + name + "'");
/*     */   }
/*     */   
/*     */   private TypeDescriptor getTypeDescriptor(EvaluationContext context, Object target, String name) {
/* 298 */     if (target == null) {
/* 299 */       return null;
/*     */     }
/* 301 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/*     */     
/* 303 */     if ((type.isArray()) && (name.equals("length"))) {
/* 304 */       return TypeDescriptor.valueOf(Integer.TYPE);
/*     */     }
/* 306 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 307 */     TypeDescriptor typeDescriptor = (TypeDescriptor)this.typeDescriptorCache.get(cacheKey);
/* 308 */     if (typeDescriptor == null) {
/*     */       try
/*     */       {
/* 311 */         if (canRead(context, target, name)) {
/* 312 */           typeDescriptor = (TypeDescriptor)this.typeDescriptorCache.get(cacheKey);
/*     */         }
/* 314 */         else if (canWrite(context, target, name)) {
/* 315 */           typeDescriptor = (TypeDescriptor)this.typeDescriptorCache.get(cacheKey);
/*     */         }
/*     */       }
/*     */       catch (AccessException localAccessException) {}
/*     */     }
/*     */     
/*     */ 
/* 322 */     return typeDescriptor;
/*     */   }
/*     */   
/*     */   private Method findGetterForProperty(String propertyName, Class<?> clazz, Object target) {
/* 326 */     Method method = findGetterForProperty(propertyName, clazz, target instanceof Class);
/* 327 */     if ((method == null) && ((target instanceof Class))) {
/* 328 */       method = findGetterForProperty(propertyName, target.getClass(), false);
/*     */     }
/* 330 */     return method;
/*     */   }
/*     */   
/*     */   private Method findSetterForProperty(String propertyName, Class<?> clazz, Object target) {
/* 334 */     Method method = findSetterForProperty(propertyName, clazz, target instanceof Class);
/* 335 */     if ((method == null) && ((target instanceof Class))) {
/* 336 */       method = findSetterForProperty(propertyName, target.getClass(), false);
/*     */     }
/* 338 */     return method;
/*     */   }
/*     */   
/*     */   private Field findField(String name, Class<?> clazz, Object target) {
/* 342 */     Field field = findField(name, clazz, target instanceof Class);
/* 343 */     if ((field == null) && ((target instanceof Class))) {
/* 344 */       field = findField(name, target.getClass(), false);
/*     */     }
/* 346 */     return field;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Method findGetterForProperty(String propertyName, Class<?> clazz, boolean mustBeStatic)
/*     */   {
/* 353 */     Method method = findMethodForProperty(getPropertyMethodSuffixes(propertyName), "get", clazz, mustBeStatic, 0, ANY_TYPES);
/*     */     
/* 355 */     if (method == null) {
/* 356 */       method = findMethodForProperty(getPropertyMethodSuffixes(propertyName), "is", clazz, mustBeStatic, 0, BOOLEAN_TYPES);
/*     */     }
/*     */     
/* 359 */     return method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Method findSetterForProperty(String propertyName, Class<?> clazz, boolean mustBeStatic)
/*     */   {
/* 366 */     return findMethodForProperty(getPropertyMethodSuffixes(propertyName), "set", clazz, mustBeStatic, 1, ANY_TYPES);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Method findMethodForProperty(String[] methodSuffixes, String prefix, Class<?> clazz, boolean mustBeStatic, int numberOfParams, Set<Class<?>> requiredReturnTypes)
/*     */   {
/* 373 */     Method[] methods = getSortedClassMethods(clazz);
/* 374 */     for (String methodSuffix : methodSuffixes) {
/* 375 */       for (Method method : methods) {
/* 376 */         if ((method.getName().equals(prefix + methodSuffix)) && 
/* 377 */           (method.getParameterTypes().length == numberOfParams) && ((!mustBeStatic) || 
/* 378 */           (Modifier.isStatic(method.getModifiers()))) && (
/* 379 */           (requiredReturnTypes.isEmpty()) || (requiredReturnTypes.contains(method.getReturnType())))) {
/* 380 */           return method;
/*     */         }
/*     */       }
/*     */     }
/* 384 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Method[] getSortedClassMethods(Class<?> clazz)
/*     */   {
/* 392 */     Method[] methods = clazz.getMethods();
/* 393 */     Arrays.sort(methods, new Comparator()
/*     */     {
/*     */       public int compare(Method o1, Method o2) {
/* 396 */         return o1.isBridge() ? 1 : o1.isBridge() == o2.isBridge() ? 0 : -1;
/*     */       }
/* 398 */     });
/* 399 */     return methods;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String[] getPropertyMethodSuffixes(String propertyName)
/*     */   {
/* 409 */     String suffix = getPropertyMethodSuffix(propertyName);
/* 410 */     if ((suffix.length() > 0) && (Character.isUpperCase(suffix.charAt(0)))) {
/* 411 */       return new String[] { suffix };
/*     */     }
/* 413 */     return new String[] { suffix, StringUtils.capitalize(suffix) };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getPropertyMethodSuffix(String propertyName)
/*     */   {
/* 421 */     if ((propertyName.length() > 1) && (Character.isUpperCase(propertyName.charAt(1)))) {
/* 422 */       return propertyName;
/*     */     }
/* 424 */     return StringUtils.capitalize(propertyName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Field findField(String name, Class<?> clazz, boolean mustBeStatic)
/*     */   {
/* 431 */     Field[] fields = clazz.getFields();
/* 432 */     for (Field field : fields) {
/* 433 */       if ((field.getName().equals(name)) && ((!mustBeStatic) || (Modifier.isStatic(field.getModifiers())))) {
/* 434 */         return field;
/*     */       }
/*     */     }
/*     */     
/*     */     Object field;
/* 439 */     if (clazz.getSuperclass() != null) {
/* 440 */       field = findField(name, clazz.getSuperclass(), mustBeStatic);
/* 441 */       if (field != null) {
/* 442 */         return (Field)field;
/*     */       }
/*     */     }
/* 445 */     for (Class<?> implementedInterface : clazz.getInterfaces()) {
/* 446 */       Field field = findField(name, implementedInterface, mustBeStatic);
/* 447 */       if (field != null) {
/* 448 */         return field;
/*     */       }
/*     */     }
/* 451 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyAccessor createOptimalAccessor(EvaluationContext evalContext, Object target, String name)
/*     */   {
/* 463 */     if (target == null) {
/* 464 */       return this;
/*     */     }
/* 466 */     Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 467 */     if (type.isArray()) {
/* 468 */       return this;
/*     */     }
/*     */     
/* 471 */     PropertyCacheKey cacheKey = new PropertyCacheKey(type, name, target instanceof Class);
/* 472 */     InvokerPair invocationTarget = (InvokerPair)this.readerCache.get(cacheKey);
/*     */     
/* 474 */     if ((invocationTarget == null) || ((invocationTarget.member instanceof Method))) {
/* 475 */       Method method = (Method)(invocationTarget != null ? invocationTarget.member : null);
/* 476 */       if (method == null) {
/* 477 */         method = findGetterForProperty(name, type, target);
/* 478 */         if (method != null) {
/* 479 */           invocationTarget = new InvokerPair(method, new TypeDescriptor(new MethodParameter(method, -1)));
/* 480 */           ReflectionUtils.makeAccessible(method);
/* 481 */           this.readerCache.put(cacheKey, invocationTarget);
/*     */         }
/*     */       }
/* 484 */       if (method != null) {
/* 485 */         return new OptimalPropertyAccessor(invocationTarget);
/*     */       }
/*     */     }
/*     */     
/* 489 */     if ((invocationTarget == null) || ((invocationTarget.member instanceof Field))) {
/* 490 */       Field field = invocationTarget != null ? (Field)invocationTarget.member : null;
/* 491 */       if (field == null) {
/* 492 */         field = findField(name, type, target instanceof Class);
/* 493 */         if (field != null) {
/* 494 */           invocationTarget = new InvokerPair(field, new TypeDescriptor(field));
/* 495 */           ReflectionUtils.makeAccessible(field);
/* 496 */           this.readerCache.put(cacheKey, invocationTarget);
/*     */         }
/*     */       }
/* 499 */       if (field != null) {
/* 500 */         return new OptimalPropertyAccessor(invocationTarget);
/*     */       }
/*     */     }
/*     */     
/* 504 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class InvokerPair
/*     */   {
/*     */     final Member member;
/*     */     
/*     */ 
/*     */     final TypeDescriptor typeDescriptor;
/*     */     
/*     */ 
/*     */     public InvokerPair(Member member, TypeDescriptor typeDescriptor)
/*     */     {
/* 519 */       this.member = member;
/* 520 */       this.typeDescriptor = typeDescriptor;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private static final class PropertyCacheKey
/*     */     implements Comparable<PropertyCacheKey>
/*     */   {
/*     */     private final Class<?> clazz;
/*     */     private final String property;
/*     */     private boolean targetIsClass;
/*     */     
/*     */     public PropertyCacheKey(Class<?> clazz, String name, boolean targetIsClass)
/*     */     {
/* 534 */       this.clazz = clazz;
/* 535 */       this.property = name;
/* 536 */       this.targetIsClass = targetIsClass;
/*     */     }
/*     */     
/*     */     public boolean equals(Object other)
/*     */     {
/* 541 */       if (this == other) {
/* 542 */         return true;
/*     */       }
/* 544 */       if (!(other instanceof PropertyCacheKey)) {
/* 545 */         return false;
/*     */       }
/* 547 */       PropertyCacheKey otherKey = (PropertyCacheKey)other;
/* 548 */       return (this.clazz == otherKey.clazz) && (this.property.equals(otherKey.property)) && (this.targetIsClass == otherKey.targetIsClass);
/*     */     }
/*     */     
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 554 */       return this.clazz.hashCode() * 29 + this.property.hashCode();
/*     */     }
/*     */     
/*     */     public String toString()
/*     */     {
/* 559 */       return "CacheKey [clazz=" + this.clazz.getName() + ", property=" + this.property + ", " + this.property + ", targetIsClass=" + this.targetIsClass + "]";
/*     */     }
/*     */     
/*     */ 
/*     */     public int compareTo(PropertyCacheKey other)
/*     */     {
/* 565 */       int result = this.clazz.getName().compareTo(other.clazz.getName());
/* 566 */       if (result == 0) {
/* 567 */         result = this.property.compareTo(other.property);
/*     */       }
/* 569 */       return result;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class OptimalPropertyAccessor
/*     */     implements CompilablePropertyAccessor
/*     */   {
/*     */     public final Member member;
/*     */     
/*     */ 
/*     */     private final TypeDescriptor typeDescriptor;
/*     */     
/*     */ 
/*     */     private final boolean needsToBeMadeAccessible;
/*     */     
/*     */ 
/*     */ 
/*     */     OptimalPropertyAccessor(ReflectivePropertyAccessor.InvokerPair target)
/*     */     {
/* 591 */       this.member = target.member;
/* 592 */       this.typeDescriptor = target.typeDescriptor;
/*     */       
/* 594 */       this.needsToBeMadeAccessible = ((!Modifier.isPublic(this.member.getModifiers())) || (!Modifier.isPublic(this.member.getDeclaringClass().getModifiers())));
/*     */     }
/*     */     
/*     */     public Class<?>[] getSpecificTargetClasses()
/*     */     {
/* 599 */       throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
/*     */     }
/*     */     
/*     */     public boolean canRead(EvaluationContext context, Object target, String name) throws AccessException
/*     */     {
/* 604 */       if (target == null) {
/* 605 */         return false;
/*     */       }
/*     */       
/* 608 */       Class<?> type = (target instanceof Class) ? (Class)target : target.getClass();
/* 609 */       if (type.isArray()) {
/* 610 */         return false;
/*     */       }
/*     */       
/* 613 */       if ((this.member instanceof Method)) {
/* 614 */         Method method = (Method)this.member;
/* 615 */         String getterName = "get" + StringUtils.capitalize(name);
/* 616 */         if (getterName.equals(method.getName())) {
/* 617 */           return true;
/*     */         }
/* 619 */         getterName = "is" + StringUtils.capitalize(name);
/* 620 */         return getterName.equals(method.getName());
/*     */       }
/*     */       
/* 623 */       Field field = (Field)this.member;
/* 624 */       return field.getName().equals(name);
/*     */     }
/*     */     
/*     */     public TypedValue read(EvaluationContext context, Object target, String name)
/*     */       throws AccessException
/*     */     {
/* 630 */       if ((this.member instanceof Method)) {
/* 631 */         Method method = (Method)this.member;
/*     */         try {
/* 633 */           if ((this.needsToBeMadeAccessible) && (!method.isAccessible())) {
/* 634 */             method.setAccessible(true);
/*     */           }
/* 636 */           Object value = method.invoke(target, new Object[0]);
/* 637 */           return new TypedValue(value, this.typeDescriptor.narrow(value));
/*     */         }
/*     */         catch (Exception ex) {
/* 640 */           throw new AccessException("Unable to access property '" + name + "' through getter method", ex);
/*     */         }
/*     */       }
/*     */       
/* 644 */       Field field = (Field)this.member;
/*     */       try {
/* 646 */         if ((this.needsToBeMadeAccessible) && (!field.isAccessible())) {
/* 647 */           field.setAccessible(true);
/*     */         }
/* 649 */         Object value = field.get(target);
/* 650 */         return new TypedValue(value, this.typeDescriptor.narrow(value));
/*     */       }
/*     */       catch (Exception ex) {
/* 653 */         throw new AccessException("Unable to access field '" + name + "'", ex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean canWrite(EvaluationContext context, Object target, String name)
/*     */     {
/* 660 */       throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
/*     */     }
/*     */     
/*     */     public void write(EvaluationContext context, Object target, String name, Object newValue)
/*     */     {
/* 665 */       throw new UnsupportedOperationException("Should not be called on an OptimalPropertyAccessor");
/*     */     }
/*     */     
/*     */     public boolean isCompilable()
/*     */     {
/* 670 */       return (Modifier.isPublic(this.member.getModifiers())) && 
/* 671 */         (Modifier.isPublic(this.member.getDeclaringClass().getModifiers()));
/*     */     }
/*     */     
/*     */     public Class<?> getPropertyType()
/*     */     {
/* 676 */       if ((this.member instanceof Method)) {
/* 677 */         return ((Method)this.member).getReturnType();
/*     */       }
/*     */       
/* 680 */       return ((Field)this.member).getType();
/*     */     }
/*     */     
/*     */ 
/*     */     public void generateCode(String propertyName, MethodVisitor mv, CodeFlow cf)
/*     */     {
/* 686 */       boolean isStatic = Modifier.isStatic(this.member.getModifiers());
/* 687 */       String descriptor = cf.lastDescriptor();
/* 688 */       String classDesc = this.member.getDeclaringClass().getName().replace('.', '/');
/*     */       
/* 690 */       if (!isStatic) {
/* 691 */         if (descriptor == null) {
/* 692 */           cf.loadTarget(mv);
/*     */         }
/* 694 */         if ((descriptor == null) || (!classDesc.equals(descriptor.substring(1)))) {
/* 695 */           mv.visitTypeInsn(192, classDesc);
/*     */         }
/*     */         
/*     */       }
/* 699 */       else if (descriptor != null)
/*     */       {
/*     */ 
/* 702 */         mv.visitInsn(87);
/*     */       }
/*     */       
/*     */ 
/* 706 */       if ((this.member instanceof Method)) {
/* 707 */         mv.visitMethodInsn(isStatic ? 184 : 182, classDesc, this.member.getName(), 
/* 708 */           CodeFlow.createSignatureDescriptor((Method)this.member), false);
/*     */       }
/*     */       else {
/* 711 */         mv.visitFieldInsn(isStatic ? 178 : 180, classDesc, this.member.getName(), 
/* 712 */           CodeFlow.toJvmDescriptor(((Field)this.member).getType()));
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-expression-4.3.14.RELEASE.jar!\org\springframework\expression\spel\support\ReflectivePropertyAccessor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */