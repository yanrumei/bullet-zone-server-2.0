/*     */ package com.fasterxml.classmate;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class Annotations
/*     */   implements Serializable, Iterable<Annotation>
/*     */ {
/*  16 */   private final Annotation[] NO_ANNOTATIONS = new Annotation[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected LinkedHashMap<Class<? extends Annotation>, Annotation> _annotations;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(Annotation override)
/*     */   {
/*  34 */     if (this._annotations == null) {
/*  35 */       this._annotations = new LinkedHashMap();
/*     */     }
/*  37 */     this._annotations.put(override.annotationType(), override);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addAll(Annotations overrides)
/*     */   {
/*  46 */     if (this._annotations == null) {
/*  47 */       this._annotations = new LinkedHashMap();
/*     */     }
/*  49 */     for (Annotation override : overrides._annotations.values()) {
/*  50 */       this._annotations.put(override.annotationType(), override);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addAsDefault(Annotation defValue)
/*     */   {
/*  60 */     Class<? extends Annotation> type = defValue.annotationType();
/*  61 */     if (this._annotations == null) {
/*  62 */       this._annotations = new LinkedHashMap();
/*  63 */       this._annotations.put(type, defValue);
/*  64 */     } else if (!this._annotations.containsKey(type)) {
/*  65 */       this._annotations.put(type, defValue);
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
/*     */   public Iterator<Annotation> iterator()
/*     */   {
/*  78 */     if (this._annotations == null) {
/*  79 */       this._annotations = new LinkedHashMap();
/*     */     }
/*  81 */     return this._annotations.values().iterator();
/*     */   }
/*     */   
/*     */   public int size() {
/*  85 */     return this._annotations == null ? 0 : this._annotations.size();
/*     */   }
/*     */   
/*     */ 
/*     */   public <A extends Annotation> A get(Class<A> cls)
/*     */   {
/*  91 */     if (this._annotations == null) {
/*  92 */       return null;
/*     */     }
/*  94 */     return (Annotation)this._annotations.get(cls);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Annotation[] asArray()
/*     */   {
/* 101 */     if ((this._annotations == null) || (this._annotations.isEmpty())) {
/* 102 */       return this.NO_ANNOTATIONS;
/*     */     }
/* 104 */     return (Annotation[])this._annotations.values().toArray(new Annotation[this._annotations.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<Annotation> asList()
/*     */   {
/* 111 */     if ((this._annotations == null) || (this._annotations.isEmpty())) {
/* 112 */       return Collections.emptyList();
/*     */     }
/* 114 */     List<Annotation> l = new ArrayList(this._annotations.size());
/* 115 */     l.addAll(this._annotations.values());
/* 116 */     return l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 127 */     if (this._annotations == null) {
/* 128 */       return "[null]";
/*     */     }
/* 130 */     return this._annotations.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\Annotations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */