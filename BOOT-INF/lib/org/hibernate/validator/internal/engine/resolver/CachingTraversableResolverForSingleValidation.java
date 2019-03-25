/*     */ package org.hibernate.validator.internal.engine.resolver;
/*     */ 
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.validation.Path;
/*     */ import javax.validation.Path.Node;
/*     */ import javax.validation.TraversableResolver;
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
/*     */ public class CachingTraversableResolverForSingleValidation
/*     */   implements TraversableResolver
/*     */ {
/*     */   private final TraversableResolver delegate;
/*  25 */   private final Map<TraversableHolder, TraversableHolder> traversables = new HashMap();
/*     */   
/*     */   public CachingTraversableResolverForSingleValidation(TraversableResolver delegate) {
/*  28 */     this.delegate = delegate;
/*     */   }
/*     */   
/*     */   public boolean isReachable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType)
/*     */   {
/*  33 */     TraversableHolder currentLH = new TraversableHolder(traversableObject, traversableProperty, null);
/*  34 */     TraversableHolder cachedLH = (TraversableHolder)this.traversables.get(currentLH);
/*  35 */     if (cachedLH == null) {
/*  36 */       currentLH.isReachable = Boolean.valueOf(this.delegate.isReachable(traversableObject, traversableProperty, rootBeanType, pathToTraversableObject, elementType));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  43 */       this.traversables.put(currentLH, currentLH);
/*  44 */       cachedLH = currentLH;
/*     */     }
/*  46 */     else if (cachedLH.isReachable == null) {
/*  47 */       cachedLH.isReachable = Boolean.valueOf(this.delegate.isReachable(traversableObject, traversableProperty, rootBeanType, pathToTraversableObject, elementType));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */     return cachedLH.isReachable.booleanValue();
/*     */   }
/*     */   
/*     */   public boolean isCascadable(Object traversableObject, Path.Node traversableProperty, Class<?> rootBeanType, Path pathToTraversableObject, ElementType elementType)
/*     */   {
/*  60 */     TraversableHolder currentLH = new TraversableHolder(traversableObject, traversableProperty, null);
/*     */     
/*  62 */     TraversableHolder cachedLH = (TraversableHolder)this.traversables.get(currentLH);
/*  63 */     if (cachedLH == null) {
/*  64 */       currentLH.isCascadable = Boolean.valueOf(this.delegate.isCascadable(traversableObject, traversableProperty, rootBeanType, pathToTraversableObject, elementType));
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */       this.traversables.put(currentLH, currentLH);
/*  72 */       cachedLH = currentLH;
/*     */     }
/*  74 */     else if (cachedLH.isCascadable == null) {
/*  75 */       cachedLH.isCascadable = Boolean.valueOf(this.delegate.isCascadable(traversableObject, traversableProperty, rootBeanType, pathToTraversableObject, elementType));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  83 */     return cachedLH.isCascadable.booleanValue();
/*     */   }
/*     */   
/*     */   private static final class TraversableHolder
/*     */   {
/*     */     private final Object traversableObject;
/*     */     private final Path.Node traversableProperty;
/*     */     private final int hashCode;
/*     */     private Boolean isReachable;
/*     */     private Boolean isCascadable;
/*     */     
/*     */     private TraversableHolder(Object traversableObject, Path.Node traversableProperty)
/*     */     {
/*  96 */       this.traversableObject = traversableObject;
/*  97 */       this.traversableProperty = traversableProperty;
/*  98 */       this.hashCode = buildHashCode();
/*     */     }
/*     */     
/*     */     public boolean equals(Object o)
/*     */     {
/* 103 */       if (this == o) {
/* 104 */         return true;
/*     */       }
/* 106 */       if ((o == null) || (getClass() != o.getClass())) {
/* 107 */         return false;
/*     */       }
/*     */       
/* 110 */       TraversableHolder that = (TraversableHolder)o;
/*     */       
/* 112 */       if (this.traversableObject != null ? !this.traversableObject.equals(that.traversableObject) : that.traversableObject != null) {
/* 113 */         return false;
/*     */       }
/* 115 */       if (!this.traversableProperty.equals(that.traversableProperty)) {
/* 116 */         return false;
/*     */       }
/*     */       
/* 119 */       return true;
/*     */     }
/*     */     
/*     */     public int hashCode()
/*     */     {
/* 124 */       return this.hashCode;
/*     */     }
/*     */     
/*     */ 
/*     */     public int buildHashCode()
/*     */     {
/* 130 */       int result = this.traversableObject != null ? System.identityHashCode(this.traversableObject) : 0;
/* 131 */       result = 31 * result + this.traversableProperty.hashCode();
/* 132 */       return result;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\resolver\CachingTraversableResolverForSingleValidation.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */