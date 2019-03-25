/*     */ package org.hibernate.validator.internal.engine.constraintvalidation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.validation.ConstraintValidator;
/*     */ import javax.validation.ConstraintValidatorContext;
/*     */ import javax.validation.ConstraintValidatorFactory;
/*     */ import javax.validation.constraints.Null;
/*     */ import javax.validation.metadata.ConstraintDescriptor;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.Contracts;
/*     */ import org.hibernate.validator.internal.util.TypeHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
/*     */ public class ConstraintValidatorManager
/*     */ {
/*  35 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  41 */   private static ConstraintValidator<?, ?> DUMMY_CONSTRAINT_VALIDATOR = new ConstraintValidator()
/*     */   {
/*     */     public void initialize(Null constraintAnnotation) {}
/*     */     
/*     */ 
/*     */     public boolean isValid(Object value, ConstraintValidatorContext context)
/*     */     {
/*  48 */       return false;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ConstraintValidatorFactory defaultConstraintValidatorFactory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ConstraintValidatorFactory leastRecentlyUsedNonDefaultConstraintValidatorFactory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ConcurrentHashMap<CacheKey, ConstraintValidator<?, ?>> constraintValidatorCache;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstraintValidatorManager(ConstraintValidatorFactory constraintValidatorFactory)
/*     */   {
/*  77 */     this.defaultConstraintValidatorFactory = constraintValidatorFactory;
/*  78 */     this.constraintValidatorCache = new ConcurrentHashMap();
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
/*     */   public <V, A extends Annotation> ConstraintValidator<A, V> getInitializedValidator(Type validatedValueType, ConstraintDescriptorImpl<A> descriptor, ConstraintValidatorFactory constraintFactory)
/*     */   {
/*  94 */     Contracts.assertNotNull(validatedValueType);
/*  95 */     Contracts.assertNotNull(descriptor);
/*  96 */     Contracts.assertNotNull(constraintFactory);
/*     */     
/*     */ 
/*  99 */     CacheKey key = new CacheKey(descriptor.getAnnotation(), validatedValueType, constraintFactory, null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 104 */     if (this.constraintValidatorCache.containsKey(key))
/*     */     {
/* 106 */       ConstraintValidator<A, V> constraintValidator = (ConstraintValidator)this.constraintValidatorCache.get(key);
/*     */       
/*     */ 
/* 109 */       if (DUMMY_CONSTRAINT_VALIDATOR.equals(constraintValidator)) {
/* 110 */         return null;
/*     */       }
/*     */       
/* 113 */       log.tracef("Constraint validator %s found in cache.", constraintValidator);
/* 114 */       return constraintValidator;
/*     */     }
/*     */     
/*     */ 
/* 118 */     Class<? extends ConstraintValidator<?, ?>> validatorClass = findMatchingValidatorClass(descriptor, validatedValueType);
/*     */     
/*     */ 
/*     */ 
/* 122 */     ConstraintValidator<A, V> constraintValidator = createAndInitializeValidator(constraintFactory, validatorClass, descriptor);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 127 */     if (constraintValidator == null) {
/* 128 */       putInitializedValidator(validatedValueType, descriptor
/*     */       
/* 130 */         .getAnnotation(), constraintFactory, DUMMY_CONSTRAINT_VALIDATOR);
/*     */       
/*     */ 
/*     */ 
/* 134 */       return null;
/*     */     }
/*     */     
/* 137 */     putInitializedValidator(validatedValueType, descriptor
/*     */     
/* 139 */       .getAnnotation(), constraintFactory, constraintValidator);
/*     */     
/*     */ 
/*     */ 
/* 143 */     return constraintValidator;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void putInitializedValidator(Type validatedValueType, Annotation annotation, ConstraintValidatorFactory constraintFactory, ConstraintValidator<?, ?> constraintValidator)
/*     */   {
/* 153 */     if ((constraintFactory != this.defaultConstraintValidatorFactory) && (constraintFactory != this.leastRecentlyUsedNonDefaultConstraintValidatorFactory)) {
/* 154 */       clearEntriesForFactory(this.leastRecentlyUsedNonDefaultConstraintValidatorFactory);
/* 155 */       this.leastRecentlyUsedNonDefaultConstraintValidatorFactory = constraintFactory;
/*     */     }
/*     */     
/* 158 */     CacheKey key = new CacheKey(annotation, validatedValueType, constraintFactory, null);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 164 */     this.constraintValidatorCache.putIfAbsent(key, constraintValidator);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private <V, A extends Annotation> ConstraintValidator<A, V> createAndInitializeValidator(ConstraintValidatorFactory constraintFactory, Class<? extends ConstraintValidator<?, ?>> validatorClass, ConstraintDescriptor<A> descriptor)
/*     */   {
/* 172 */     if (validatorClass == null) {
/* 173 */       return null;
/*     */     }
/*     */     
/*     */ 
/* 177 */     ConstraintValidator<A, V> constraintValidator = constraintFactory.getInstance(validatorClass);
/*     */     
/*     */ 
/* 180 */     if (constraintValidator == null) {
/* 181 */       throw log.getConstraintFactoryMustNotReturnNullException(validatorClass.getName());
/*     */     }
/* 183 */     initializeConstraint(descriptor, constraintValidator);
/* 184 */     return constraintValidator;
/*     */   }
/*     */   
/*     */   private void clearEntriesForFactory(ConstraintValidatorFactory constraintFactory) {
/* 188 */     List<CacheKey> entriesToRemove = new ArrayList();
/* 189 */     for (Map.Entry<CacheKey, ConstraintValidator<?, ?>> entry : this.constraintValidatorCache.entrySet()) {
/* 190 */       if (((CacheKey)entry.getKey()).getConstraintFactory() == constraintFactory) {
/* 191 */         entriesToRemove.add(entry.getKey());
/*     */       }
/*     */     }
/* 194 */     for (CacheKey key : entriesToRemove) {
/* 195 */       this.constraintValidatorCache.remove(key);
/*     */     }
/*     */   }
/*     */   
/*     */   public void clear() {
/* 200 */     for (Map.Entry<CacheKey, ConstraintValidator<?, ?>> entry : this.constraintValidatorCache.entrySet()) {
/* 201 */       ((CacheKey)entry.getKey()).getConstraintFactory().releaseInstance((ConstraintValidator)entry.getValue());
/*     */     }
/* 203 */     this.constraintValidatorCache.clear();
/*     */   }
/*     */   
/*     */   public ConstraintValidatorFactory getDefaultConstraintValidatorFactory() {
/* 207 */     return this.defaultConstraintValidatorFactory;
/*     */   }
/*     */   
/*     */   public int numberOfCachedConstraintValidatorInstances() {
/* 211 */     return this.constraintValidatorCache.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private <A extends Annotation> Class<? extends ConstraintValidator<A, ?>> findMatchingValidatorClass(ConstraintDescriptorImpl<A> descriptor, Type validatedValueType)
/*     */   {
/* 222 */     Map<Type, Class<? extends ConstraintValidator<A, ?>>> availableValidatorTypes = TypeHelper.getValidatorsTypes(descriptor
/* 223 */       .getAnnotationType(), descriptor
/* 224 */       .getMatchingConstraintValidatorClasses());
/*     */     
/*     */ 
/* 227 */     List<Type> discoveredSuitableTypes = findSuitableValidatorTypes(validatedValueType, availableValidatorTypes);
/* 228 */     resolveAssignableTypes(discoveredSuitableTypes);
/*     */     
/* 230 */     if (discoveredSuitableTypes.size() == 0) {
/* 231 */       return null;
/*     */     }
/*     */     
/* 234 */     if (discoveredSuitableTypes.size() > 1) {
/* 235 */       StringBuilder builder = new StringBuilder();
/* 236 */       for (Type clazz : discoveredSuitableTypes) {
/* 237 */         builder.append(clazz);
/* 238 */         builder.append(", ");
/*     */       }
/* 240 */       builder.delete(builder.length() - 2, builder.length());
/* 241 */       throw log.getMoreThanOneValidatorFoundForTypeException(validatedValueType, builder.toString());
/*     */     }
/*     */     
/* 244 */     Type suitableType = (Type)discoveredSuitableTypes.get(0);
/* 245 */     return (Class)availableValidatorTypes.get(suitableType);
/*     */   }
/*     */   
/*     */   private <A extends Annotation> List<Type> findSuitableValidatorTypes(Type type, Map<Type, Class<? extends ConstraintValidator<A, ?>>> availableValidatorTypes) {
/* 249 */     List<Type> determinedSuitableTypes = CollectionHelper.newArrayList();
/* 250 */     for (Type validatorType : availableValidatorTypes.keySet()) {
/* 251 */       if ((TypeHelper.isAssignable(validatorType, type)) && 
/* 252 */         (!determinedSuitableTypes.contains(validatorType))) {
/* 253 */         determinedSuitableTypes.add(validatorType);
/*     */       }
/*     */     }
/* 256 */     return determinedSuitableTypes;
/*     */   }
/*     */   
/*     */   private <A extends Annotation> void initializeConstraint(ConstraintDescriptor<A> descriptor, ConstraintValidator<A, ?> constraintValidator) {
/*     */     try {
/* 261 */       constraintValidator.initialize(descriptor.getAnnotation());
/*     */     }
/*     */     catch (RuntimeException e) {
/* 264 */       throw log.getUnableToInitializeConstraintValidatorException(constraintValidator.getClass().getName(), e);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void resolveAssignableTypes(List<Type> assignableTypes)
/*     */   {
/* 275 */     if ((assignableTypes.size() == 0) || (assignableTypes.size() == 1)) {
/* 276 */       return;
/*     */     }
/*     */     
/* 279 */     List<Type> typesToRemove = new ArrayList();
/*     */     do {
/* 281 */       typesToRemove.clear();
/* 282 */       Type type = (Type)assignableTypes.get(0);
/* 283 */       for (int i = 1; i < assignableTypes.size(); i++) {
/* 284 */         if (TypeHelper.isAssignable(type, (Type)assignableTypes.get(i))) {
/* 285 */           typesToRemove.add(type);
/*     */         }
/* 287 */         else if (TypeHelper.isAssignable((Type)assignableTypes.get(i), type)) {
/* 288 */           typesToRemove.add(assignableTypes.get(i));
/*     */         }
/*     */       }
/* 291 */       assignableTypes.removeAll(typesToRemove);
/* 292 */     } while (typesToRemove.size() > 0);
/*     */   }
/*     */   
/*     */   private static final class CacheKey {
/*     */     private final Annotation annotation;
/*     */     private final Type validatedType;
/*     */     private final ConstraintValidatorFactory constraintFactory;
/*     */     private final int hashCode;
/*     */     
/*     */     private CacheKey(Annotation annotation, Type validatorType, ConstraintValidatorFactory constraintFactory) {
/* 302 */       this.annotation = annotation;
/* 303 */       this.validatedType = validatorType;
/* 304 */       this.constraintFactory = constraintFactory;
/* 305 */       this.hashCode = createHashCode();
/*     */     }
/*     */     
/*     */     public ConstraintValidatorFactory getConstraintFactory() {
/* 309 */       return this.constraintFactory;
/*     */     }
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 314 */       if (this == o) {
/* 315 */         return true;
/*     */       }
/* 317 */       if ((o == null) || (getClass() != o.getClass())) {
/* 318 */         return false;
/*     */       }
/*     */       
/* 321 */       CacheKey cacheKey = (CacheKey)o;
/*     */       
/* 323 */       if (this.annotation != null ? !this.annotation.equals(cacheKey.annotation) : cacheKey.annotation != null) {
/* 324 */         return false;
/*     */       }
/* 326 */       if (this.constraintFactory != null ? !this.constraintFactory.equals(cacheKey.constraintFactory) : cacheKey.constraintFactory != null) {
/* 327 */         return false;
/*     */       }
/* 329 */       if (this.validatedType != null ? !this.validatedType.equals(cacheKey.validatedType) : cacheKey.validatedType != null) {
/* 330 */         return false;
/*     */       }
/*     */       
/* 333 */       return true;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 338 */       return this.hashCode;
/*     */     }
/*     */     
/*     */     private int createHashCode() {
/* 342 */       int result = this.annotation != null ? this.annotation.hashCode() : 0;
/* 343 */       result = 31 * result + (this.validatedType != null ? this.validatedType.hashCode() : 0);
/* 344 */       result = 31 * result + (this.constraintFactory != null ? this.constraintFactory.hashCode() : 0);
/* 345 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\constraintvalidation\ConstraintValidatorManager.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */