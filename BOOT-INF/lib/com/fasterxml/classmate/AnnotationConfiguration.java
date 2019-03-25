/*     */ package com.fasterxml.classmate;
/*     */ 
/*     */ import com.fasterxml.classmate.util.ClassKey;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.HashMap;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AnnotationConfiguration
/*     */   implements Serializable
/*     */ {
/*     */   public abstract AnnotationInclusion getInclusionForClass(Class<? extends Annotation> paramClass);
/*     */   
/*     */   public abstract AnnotationInclusion getInclusionForConstructor(Class<? extends Annotation> paramClass);
/*     */   
/*     */   public abstract AnnotationInclusion getInclusionForField(Class<? extends Annotation> paramClass);
/*     */   
/*     */   public abstract AnnotationInclusion getInclusionForMethod(Class<? extends Annotation> paramClass);
/*     */   
/*     */   public abstract AnnotationInclusion getInclusionForParameter(Class<? extends Annotation> paramClass);
/*     */   
/*     */   public static class StdConfiguration
/*     */     extends AnnotationConfiguration
/*     */     implements Serializable
/*     */   {
/*     */     protected final AnnotationInclusion _defaultInclusion;
/*  71 */     protected final HashMap<ClassKey, AnnotationInclusion> _inclusions = new HashMap();
/*     */     
/*     */     public StdConfiguration(AnnotationInclusion defaultBehavior)
/*     */     {
/*  75 */       this._defaultInclusion = defaultBehavior;
/*     */     }
/*     */     
/*     */     public AnnotationInclusion getInclusionForClass(Class<? extends Annotation> annotationType)
/*     */     {
/*  80 */       return _inclusionFor(annotationType);
/*     */     }
/*     */     
/*     */     public AnnotationInclusion getInclusionForConstructor(Class<? extends Annotation> annotationType)
/*     */     {
/*  85 */       return _inclusionFor(annotationType);
/*     */     }
/*     */     
/*     */     public AnnotationInclusion getInclusionForField(Class<? extends Annotation> annotationType)
/*     */     {
/*  90 */       return getInclusionForClass(annotationType);
/*     */     }
/*     */     
/*     */     public AnnotationInclusion getInclusionForMethod(Class<? extends Annotation> annotationType)
/*     */     {
/*  95 */       return getInclusionForClass(annotationType);
/*     */     }
/*     */     
/*     */     public AnnotationInclusion getInclusionForParameter(Class<? extends Annotation> annotationType)
/*     */     {
/* 100 */       return getInclusionForClass(annotationType);
/*     */     }
/*     */     
/*     */     public void setInclusion(Class<? extends Annotation> annotationType, AnnotationInclusion incl)
/*     */     {
/* 105 */       this._inclusions.put(new ClassKey(annotationType), incl);
/*     */     }
/*     */     
/*     */     protected AnnotationInclusion _inclusionFor(Class<? extends Annotation> annotationType)
/*     */     {
/* 110 */       ClassKey key = new ClassKey(annotationType);
/* 111 */       AnnotationInclusion beh = (AnnotationInclusion)this._inclusions.get(key);
/* 112 */       return beh == null ? this._defaultInclusion : beh;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\AnnotationConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */