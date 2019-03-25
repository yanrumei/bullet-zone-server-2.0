/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import java.io.Serializable;
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
/*     */ public abstract class ConcreteBeanPropertyBase
/*     */   implements BeanProperty, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final PropertyMetadata _metadata;
/*     */   protected transient JsonFormat.Value _propertyFormat;
/*     */   
/*     */   protected ConcreteBeanPropertyBase(PropertyMetadata md)
/*     */   {
/*  36 */     this._metadata = (md == null ? PropertyMetadata.STD_REQUIRED_OR_OPTIONAL : md);
/*     */   }
/*     */   
/*     */   protected ConcreteBeanPropertyBase(ConcreteBeanPropertyBase src) {
/*  40 */     this._metadata = src._metadata;
/*  41 */     this._propertyFormat = src._propertyFormat;
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/*  45 */     return this._metadata.isRequired();
/*     */   }
/*     */   
/*  48 */   public PropertyMetadata getMetadata() { return this._metadata; }
/*     */   
/*     */   public boolean isVirtual() {
/*  51 */     return false;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public final JsonFormat.Value findFormatOverrides(AnnotationIntrospector intr) {
/*  56 */     JsonFormat.Value f = null;
/*  57 */     if (intr != null) {
/*  58 */       AnnotatedMember member = getMember();
/*  59 */       if (member != null) {
/*  60 */         f = intr.findFormat(member);
/*     */       }
/*     */     }
/*  63 */     if (f == null) {
/*  64 */       f = EMPTY_FORMAT;
/*     */     }
/*  66 */     return f;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonFormat.Value findPropertyFormat(MapperConfig<?> config, Class<?> baseType)
/*     */   {
/*  74 */     JsonFormat.Value v = this._propertyFormat;
/*  75 */     if (v == null) {
/*  76 */       JsonFormat.Value v1 = config.getDefaultPropertyFormat(baseType);
/*  77 */       JsonFormat.Value v2 = null;
/*  78 */       AnnotationIntrospector intr = config.getAnnotationIntrospector();
/*  79 */       if (intr != null) {
/*  80 */         AnnotatedMember member = getMember();
/*  81 */         if (member != null) {
/*  82 */           v2 = intr.findFormat(member);
/*     */         }
/*     */       }
/*  85 */       if (v1 == null) {
/*  86 */         v = v2 == null ? EMPTY_FORMAT : v2;
/*     */       } else {
/*  88 */         v = v2 == null ? v1 : v1.withOverrides(v2);
/*     */       }
/*  90 */       this._propertyFormat = v;
/*     */     }
/*  92 */     return v;
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonInclude.Value findPropertyInclusion(MapperConfig<?> config, Class<?> baseType)
/*     */   {
/*  98 */     JsonInclude.Value v0 = config.getDefaultPropertyInclusion(baseType);
/*  99 */     AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 100 */     AnnotatedMember member = getMember();
/* 101 */     if ((intr == null) || (member == null)) {
/* 102 */       return v0;
/*     */     }
/* 104 */     JsonInclude.Value v = intr.findPropertyInclusion(member);
/* 105 */     if (v == null) {
/* 106 */       return v0;
/*     */     }
/* 108 */     return v0.withOverrides(v);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\ConcreteBeanPropertyBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */