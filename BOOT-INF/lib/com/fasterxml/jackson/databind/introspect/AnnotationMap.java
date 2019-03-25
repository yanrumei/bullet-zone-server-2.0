/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class AnnotationMap
/*     */   implements Annotations
/*     */ {
/*     */   protected HashMap<Class<?>, Annotation> _annotations;
/*     */   
/*     */   public AnnotationMap() {}
/*     */   
/*     */   private AnnotationMap(HashMap<Class<?>, Annotation> a)
/*     */   {
/*  21 */     this._annotations = a;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public <A extends Annotation> A get(Class<A> cls)
/*     */   {
/*  28 */     if (this._annotations == null) {
/*  29 */       return null;
/*     */     }
/*  31 */     return (Annotation)this._annotations.get(cls);
/*     */   }
/*     */   
/*     */   public boolean has(Class<?> cls)
/*     */   {
/*  36 */     if (this._annotations == null) {
/*  37 */       return false;
/*     */     }
/*  39 */     return this._annotations.containsKey(cls);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasOneOf(Class<? extends Annotation>[] annoClasses)
/*     */   {
/*  49 */     if (this._annotations != null) {
/*  50 */       int i = 0; for (int end = annoClasses.length; i < end; i++) {
/*  51 */         if (this._annotations.containsKey(annoClasses[i])) {
/*  52 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*  56 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Iterable<Annotation> annotations()
/*     */   {
/*  63 */     if ((this._annotations == null) || (this._annotations.size() == 0)) {
/*  64 */       return Collections.emptyList();
/*     */     }
/*  66 */     return this._annotations.values();
/*     */   }
/*     */   
/*     */   public static AnnotationMap merge(AnnotationMap primary, AnnotationMap secondary)
/*     */   {
/*  71 */     if ((primary == null) || (primary._annotations == null) || (primary._annotations.isEmpty())) {
/*  72 */       return secondary;
/*     */     }
/*  74 */     if ((secondary == null) || (secondary._annotations == null) || (secondary._annotations.isEmpty())) {
/*  75 */       return primary;
/*     */     }
/*  77 */     HashMap<Class<?>, Annotation> annotations = new HashMap();
/*     */     
/*  79 */     for (Annotation ann : secondary._annotations.values()) {
/*  80 */       annotations.put(ann.annotationType(), ann);
/*     */     }
/*     */     
/*  83 */     for (Annotation ann : primary._annotations.values()) {
/*  84 */       annotations.put(ann.annotationType(), ann);
/*     */     }
/*  86 */     return new AnnotationMap(annotations);
/*     */   }
/*     */   
/*     */   public int size()
/*     */   {
/*  91 */     return this._annotations == null ? 0 : this._annotations.size();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean addIfNotPresent(Annotation ann)
/*     */   {
/* 100 */     if ((this._annotations == null) || (!this._annotations.containsKey(ann.annotationType()))) {
/* 101 */       _add(ann);
/* 102 */       return true;
/*     */     }
/* 104 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean add(Annotation ann)
/*     */   {
/* 114 */     return _add(ann);
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 119 */     if (this._annotations == null) {
/* 120 */       return "[null]";
/*     */     }
/* 122 */     return this._annotations.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean _add(Annotation ann)
/*     */   {
/* 132 */     if (this._annotations == null) {
/* 133 */       this._annotations = new HashMap();
/*     */     }
/* 135 */     Annotation previous = (Annotation)this._annotations.put(ann.annotationType(), ann);
/* 136 */     return (previous == null) || (!previous.equals(ann));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\AnnotationMap.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */