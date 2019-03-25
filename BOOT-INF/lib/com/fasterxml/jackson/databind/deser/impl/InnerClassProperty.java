/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Constructor;
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
/*     */ public final class InnerClassProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final SettableBeanProperty _delegate;
/*     */   protected final transient Constructor<?> _creator;
/*     */   protected AnnotatedConstructor _annotated;
/*     */   
/*     */   public InnerClassProperty(SettableBeanProperty delegate, Constructor<?> ctor)
/*     */   {
/*  44 */     super(delegate);
/*  45 */     this._delegate = delegate;
/*  46 */     this._creator = ctor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected InnerClassProperty(InnerClassProperty src, AnnotatedConstructor ann)
/*     */   {
/*  55 */     super(src);
/*  56 */     this._delegate = src._delegate;
/*  57 */     this._annotated = ann;
/*  58 */     this._creator = (this._annotated == null ? null : this._annotated.getAnnotated());
/*  59 */     if (this._creator == null) {
/*  60 */       throw new IllegalArgumentException("Missing constructor (broken JDK (de)serialization?)");
/*     */     }
/*     */   }
/*     */   
/*     */   protected InnerClassProperty(InnerClassProperty src, JsonDeserializer<?> deser)
/*     */   {
/*  66 */     super(src, deser);
/*  67 */     this._delegate = src._delegate.withValueDeserializer(deser);
/*  68 */     this._creator = src._creator;
/*     */   }
/*     */   
/*     */   protected InnerClassProperty(InnerClassProperty src, PropertyName newName) {
/*  72 */     super(src, newName);
/*  73 */     this._delegate = src._delegate.withName(newName);
/*  74 */     this._creator = src._creator;
/*     */   }
/*     */   
/*     */   public InnerClassProperty withName(PropertyName newName)
/*     */   {
/*  79 */     return new InnerClassProperty(this, newName);
/*     */   }
/*     */   
/*     */   public InnerClassProperty withValueDeserializer(JsonDeserializer<?> deser)
/*     */   {
/*  84 */     if (this._valueDeserializer == deser) {
/*  85 */       return this;
/*     */     }
/*  87 */     return new InnerClassProperty(this, deser);
/*     */   }
/*     */   
/*     */   public void assignIndex(int index) {
/*  91 */     this._delegate.assignIndex(index);
/*     */   }
/*     */   
/*  94 */   public int getPropertyIndex() { return this._delegate.getPropertyIndex(); }
/*     */   
/*     */   public int getCreatorIndex() {
/*  97 */     return this._delegate.getCreatorIndex();
/*     */   }
/*     */   
/*     */   public void fixAccess(DeserializationConfig config) {
/* 101 */     this._delegate.fixAccess(config);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/* 108 */     return this._delegate.getAnnotation(acls);
/*     */   }
/*     */   
/* 111 */   public AnnotatedMember getMember() { return this._delegate.getMember(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void deserializeAndSet(JsonParser jp, DeserializationContext ctxt, Object bean)
/*     */     throws IOException
/*     */   {
/* 123 */     JsonToken t = jp.getCurrentToken();
/*     */     Object value;
/* 125 */     Object value; if (t == JsonToken.VALUE_NULL) {
/* 126 */       value = this._valueDeserializer.getNullValue(ctxt); } else { Object value;
/* 127 */       if (this._valueTypeDeserializer != null) {
/* 128 */         value = this._valueDeserializer.deserializeWithType(jp, ctxt, this._valueTypeDeserializer);
/*     */       } else {
/*     */         try {
/* 131 */           value = this._creator.newInstance(new Object[] { bean });
/*     */         } catch (Exception e) {
/* 133 */           ClassUtil.unwrapAndThrowAsIAE(e, "Failed to instantiate class " + this._creator.getDeclaringClass().getName() + ", problem: " + e.getMessage());
/* 134 */           value = null;
/*     */         }
/* 136 */         this._valueDeserializer.deserialize(jp, ctxt, value);
/*     */       } }
/* 138 */     set(bean, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object deserializeSetAndReturn(JsonParser jp, DeserializationContext ctxt, Object instance)
/*     */     throws IOException
/*     */   {
/* 146 */     return setAndReturn(instance, deserialize(jp, ctxt));
/*     */   }
/*     */   
/*     */   public final void set(Object instance, Object value) throws IOException
/*     */   {
/* 151 */     this._delegate.set(instance, value);
/*     */   }
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value) throws IOException
/*     */   {
/* 156 */     return this._delegate.setAndReturn(instance, value);
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
/* 167 */     return new InnerClassProperty(this, this._annotated);
/*     */   }
/*     */   
/*     */   Object writeReplace()
/*     */   {
/* 172 */     if (this._annotated != null) {
/* 173 */       return this;
/*     */     }
/* 175 */     return new InnerClassProperty(this, new AnnotatedConstructor(null, this._creator, null, null));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\impl\InnerClassProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */