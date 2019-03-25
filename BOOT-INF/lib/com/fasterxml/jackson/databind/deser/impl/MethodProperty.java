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
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class MethodProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotatedMethod _annotated;
/*     */   protected final transient Method _setter;
/*     */   
/*     */   public MethodProperty(BeanPropertyDefinition propDef, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedMethod method)
/*     */   {
/*  36 */     super(propDef, type, typeDeser, contextAnnotations);
/*  37 */     this._annotated = method;
/*  38 */     this._setter = method.getAnnotated();
/*     */   }
/*     */   
/*     */   protected MethodProperty(MethodProperty src, JsonDeserializer<?> deser) {
/*  42 */     super(src, deser);
/*  43 */     this._annotated = src._annotated;
/*  44 */     this._setter = src._setter;
/*     */   }
/*     */   
/*     */   protected MethodProperty(MethodProperty src, PropertyName newName) {
/*  48 */     super(src, newName);
/*  49 */     this._annotated = src._annotated;
/*  50 */     this._setter = src._setter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected MethodProperty(MethodProperty src, Method m)
/*     */   {
/*  57 */     super(src);
/*  58 */     this._annotated = src._annotated;
/*  59 */     this._setter = m;
/*     */   }
/*     */   
/*     */   public MethodProperty withName(PropertyName newName)
/*     */   {
/*  64 */     return new MethodProperty(this, newName);
/*     */   }
/*     */   
/*     */   public MethodProperty withValueDeserializer(JsonDeserializer<?> deser)
/*     */   {
/*  69 */     if (this._valueDeserializer == deser) {
/*  70 */       return this;
/*     */     }
/*  72 */     return new MethodProperty(this, deser);
/*     */   }
/*     */   
/*     */   public void fixAccess(DeserializationConfig config)
/*     */   {
/*  77 */     this._annotated.fixAccess(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
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
/*  89 */     return this._annotated == null ? null : this._annotated.getAnnotation(acls);
/*     */   }
/*     */   
/*  92 */   public AnnotatedMember getMember() { return this._annotated; }
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
/* 104 */     Object value = deserialize(p, ctxt);
/*     */     try {
/* 106 */       this._setter.invoke(instance, new Object[] { value });
/*     */     } catch (Exception e) {
/* 108 */       _throwAsIOE(p, e, value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance)
/*     */     throws IOException
/*     */   {
/* 116 */     Object value = deserialize(p, ctxt);
/*     */     try {
/* 118 */       Object result = this._setter.invoke(instance, new Object[] { value });
/* 119 */       return result == null ? instance : result;
/*     */     } catch (Exception e) {
/* 121 */       _throwAsIOE(p, e, value); }
/* 122 */     return null;
/*     */   }
/*     */   
/*     */   public final void set(Object instance, Object value)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 130 */       this._setter.invoke(instance, new Object[] { value });
/*     */     }
/*     */     catch (Exception e) {
/* 133 */       _throwAsIOE(e, value);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value) throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 141 */       Object result = this._setter.invoke(instance, new Object[] { value });
/* 142 */       return result == null ? instance : result;
/*     */     }
/*     */     catch (Exception e) {
/* 145 */       _throwAsIOE(e, value); }
/* 146 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   Object readResolve()
/*     */   {
/* 157 */     return new MethodProperty(this, this._annotated.getAnnotated());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\impl\MethodProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */