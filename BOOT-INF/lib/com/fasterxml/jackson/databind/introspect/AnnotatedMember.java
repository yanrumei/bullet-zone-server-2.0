/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Member;
/*     */ import java.util.Collections;
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
/*     */ public abstract class AnnotatedMember
/*     */   extends Annotated
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final transient TypeResolutionContext _typeContext;
/*     */   protected final transient AnnotationMap _annotations;
/*     */   
/*     */   protected AnnotatedMember(TypeResolutionContext ctxt, AnnotationMap annotations)
/*     */   {
/*  37 */     this._typeContext = ctxt;
/*  38 */     this._annotations = annotations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AnnotatedMember(AnnotatedMember base)
/*     */   {
/*  47 */     this._typeContext = base._typeContext;
/*  48 */     this._annotations = base._annotations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Class<?> getDeclaringClass();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Member getMember();
/*     */   
/*     */ 
/*     */ 
/*     */   public TypeResolutionContext getTypeContext()
/*     */   {
/*  65 */     return this._typeContext;
/*     */   }
/*     */   
/*     */   public final <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/*  70 */     if (this._annotations == null) {
/*  71 */       return null;
/*     */     }
/*  73 */     return this._annotations.get(acls);
/*     */   }
/*     */   
/*     */   public final boolean hasAnnotation(Class<?> acls)
/*     */   {
/*  78 */     if (this._annotations == null) {
/*  79 */       return false;
/*     */     }
/*  81 */     return this._annotations.has(acls);
/*     */   }
/*     */   
/*     */   public boolean hasOneOf(Class<? extends Annotation>[] annoClasses)
/*     */   {
/*  86 */     if (this._annotations == null) {
/*  87 */       return false;
/*     */     }
/*  89 */     return this._annotations.hasOneOf(annoClasses);
/*     */   }
/*     */   
/*     */   public Iterable<Annotation> annotations()
/*     */   {
/*  94 */     if (this._annotations == null) {
/*  95 */       return Collections.emptyList();
/*     */     }
/*  97 */     return this._annotations.annotations();
/*     */   }
/*     */   
/*     */   protected AnnotationMap getAllAnnotations()
/*     */   {
/* 102 */     return this._annotations;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean addOrOverride(Annotation a)
/*     */   {
/* 111 */     return this._annotations.add(a);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean addIfNotPresent(Annotation a)
/*     */   {
/* 120 */     return this._annotations.addIfNotPresent(a);
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
/*     */   public final void fixAccess(boolean force)
/*     */   {
/* 136 */     Member m = getMember();
/* 137 */     if (m != null) {
/* 138 */       ClassUtil.checkAndFixAccess(m, force);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final void fixAccess()
/*     */   {
/* 148 */     fixAccess(true);
/*     */   }
/*     */   
/*     */   public abstract void setValue(Object paramObject1, Object paramObject2)
/*     */     throws UnsupportedOperationException, IllegalArgumentException;
/*     */   
/*     */   public abstract Object getValue(Object paramObject)
/*     */     throws UnsupportedOperationException, IllegalArgumentException;
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedMember.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */