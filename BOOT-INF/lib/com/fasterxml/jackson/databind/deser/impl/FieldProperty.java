/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class FieldProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotatedField _annotated;
/*     */   protected final transient Field _field;
/*     */   
/*     */   public FieldProperty(BeanPropertyDefinition propDef, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedField field)
/*     */   {
/*  38 */     super(propDef, type, typeDeser, contextAnnotations);
/*  39 */     this._annotated = field;
/*  40 */     this._field = field.getAnnotated();
/*     */   }
/*     */   
/*     */   protected FieldProperty(FieldProperty src, JsonDeserializer<?> deser) {
/*  44 */     super(src, deser);
/*  45 */     this._annotated = src._annotated;
/*  46 */     this._field = src._field;
/*     */   }
/*     */   
/*     */   protected FieldProperty(FieldProperty src, PropertyName newName) {
/*  50 */     super(src, newName);
/*  51 */     this._annotated = src._annotated;
/*  52 */     this._field = src._field;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected FieldProperty(FieldProperty src)
/*     */   {
/*  60 */     super(src);
/*  61 */     this._annotated = src._annotated;
/*  62 */     Field f = this._annotated.getAnnotated();
/*  63 */     if (f == null) {
/*  64 */       throw new IllegalArgumentException("Missing field (broken JDK (de)serialization?)");
/*     */     }
/*  66 */     this._field = f;
/*     */   }
/*     */   
/*     */   public FieldProperty withName(PropertyName newName)
/*     */   {
/*  71 */     return new FieldProperty(this, newName);
/*     */   }
/*     */   
/*     */   public FieldProperty withValueDeserializer(JsonDeserializer<?> deser)
/*     */   {
/*  76 */     if (this._valueDeserializer == deser) {
/*  77 */       return this;
/*     */     }
/*  79 */     return new FieldProperty(this, deser);
/*     */   }
/*     */   
/*     */   public void fixAccess(DeserializationConfig config)
/*     */   {
/*  84 */     ClassUtil.checkAndFixAccess(this._field, config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/*  96 */     return this._annotated == null ? null : this._annotated.getAnnotation(acls);
/*     */   }
/*     */   
/*  99 */   public AnnotatedMember getMember() { return this._annotated; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
/*     */     throws IOException
/*     */   {
/* 111 */     Object value = deserialize(p, ctxt);
/*     */     try {
/* 113 */       this._field.set(instance, value);
/*     */     } catch (Exception e) {
/* 115 */       _throwAsIOE(p, e, value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance)
/*     */     throws IOException
/*     */   {
/* 123 */     Object value = deserialize(p, ctxt);
/*     */     try {
/* 125 */       this._field.set(instance, value);
/*     */     } catch (Exception e) {
/* 127 */       _throwAsIOE(p, e, value);
/*     */     }
/* 129 */     return instance;
/*     */   }
/*     */   
/*     */   public final void set(Object instance, Object value) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 136 */       this._field.set(instance, value);
/*     */     }
/*     */     catch (Exception e) {
/* 139 */       _throwAsIOE(e, value);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 147 */       this._field.set(instance, value);
/*     */     }
/*     */     catch (Exception e) {
/* 150 */       _throwAsIOE(e, value);
/*     */     }
/* 152 */     return instance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Object readResolve()
/*     */   {
/* 162 */     return new FieldProperty(this);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\impl\FieldProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */