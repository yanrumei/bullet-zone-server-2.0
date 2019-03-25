/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
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
/*     */ public final class SetterlessProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotatedMethod _annotated;
/*     */   protected final Method _getter;
/*     */   
/*     */   public SetterlessProperty(BeanPropertyDefinition propDef, JavaType type, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedMethod method)
/*     */   {
/*  38 */     super(propDef, type, typeDeser, contextAnnotations);
/*  39 */     this._annotated = method;
/*  40 */     this._getter = method.getAnnotated();
/*     */   }
/*     */   
/*     */   protected SetterlessProperty(SetterlessProperty src, JsonDeserializer<?> deser) {
/*  44 */     super(src, deser);
/*  45 */     this._annotated = src._annotated;
/*  46 */     this._getter = src._getter;
/*     */   }
/*     */   
/*     */   protected SetterlessProperty(SetterlessProperty src, PropertyName newName) {
/*  50 */     super(src, newName);
/*  51 */     this._annotated = src._annotated;
/*  52 */     this._getter = src._getter;
/*     */   }
/*     */   
/*     */   public SetterlessProperty withName(PropertyName newName)
/*     */   {
/*  57 */     return new SetterlessProperty(this, newName);
/*     */   }
/*     */   
/*     */   public SetterlessProperty withValueDeserializer(JsonDeserializer<?> deser)
/*     */   {
/*  62 */     if (this._valueDeserializer == deser) {
/*  63 */       return this;
/*     */     }
/*  65 */     return new SetterlessProperty(this, deser);
/*     */   }
/*     */   
/*     */   public void fixAccess(DeserializationConfig config)
/*     */   {
/*  70 */     this._annotated.fixAccess(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
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
/*  82 */     return this._annotated.getAnnotation(acls);
/*     */   }
/*     */   
/*  85 */   public AnnotatedMember getMember() { return this._annotated; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance)
/*     */     throws IOException
/*     */   {
/*  97 */     JsonToken t = p.getCurrentToken();
/*  98 */     if (t == JsonToken.VALUE_NULL)
/*     */     {
/*     */ 
/*     */ 
/* 102 */       return;
/*     */     }
/*     */     
/*     */ 
/* 106 */     if (this._valueTypeDeserializer != null) {
/* 107 */       ctxt.reportMappingException("Problem deserializing 'setterless' property (\"%s\"): no way to handle typed deser with setterless yet", new Object[] { getName() });
/*     */     }
/*     */     
/*     */ 
/*     */     Object toModify;
/*     */     
/*     */ 
/*     */     try
/*     */     {
/* 116 */       toModify = this._getter.invoke(instance, new Object[0]);
/*     */     } catch (Exception e) {
/* 118 */       _throwAsIOE(p, e);
/* 119 */       return;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 126 */     if (toModify == null) {
/* 127 */       throw JsonMappingException.from(p, "Problem deserializing 'setterless' property '" + getName() + "': get method returned null");
/*     */     }
/*     */     
/* 130 */     this._valueDeserializer.deserialize(p, ctxt, toModify);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance)
/*     */     throws IOException
/*     */   {
/* 137 */     deserializeAndSet(p, ctxt, instance);
/* 138 */     return instance;
/*     */   }
/*     */   
/*     */   public final void set(Object instance, Object value) throws IOException
/*     */   {
/* 143 */     throw new UnsupportedOperationException("Should never call 'set' on setterless property");
/*     */   }
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value)
/*     */     throws IOException
/*     */   {
/* 149 */     set(instance, value);
/* 150 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\impl\SetterlessProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */