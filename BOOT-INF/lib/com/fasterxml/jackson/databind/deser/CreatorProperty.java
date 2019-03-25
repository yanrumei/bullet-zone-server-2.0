/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CreatorProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotatedParameter _annotated;
/*     */   protected final Object _injectableValueId;
/*     */   protected final int _creatorIndex;
/*     */   protected SettableBeanProperty _fallbackSetter;
/*     */   
/*     */   public CreatorProperty(PropertyName name, JavaType type, PropertyName wrapperName, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedParameter param, int index, Object injectableValueId, PropertyMetadata metadata)
/*     */   {
/*  81 */     super(name, type, wrapperName, typeDeser, contextAnnotations, metadata);
/*  82 */     this._annotated = param;
/*  83 */     this._creatorIndex = index;
/*  84 */     this._injectableValueId = injectableValueId;
/*  85 */     this._fallbackSetter = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected CreatorProperty(CreatorProperty src, PropertyName newName)
/*     */   {
/*  92 */     super(src, newName);
/*  93 */     this._annotated = src._annotated;
/*  94 */     this._creatorIndex = src._creatorIndex;
/*  95 */     this._injectableValueId = src._injectableValueId;
/*  96 */     this._fallbackSetter = src._fallbackSetter;
/*     */   }
/*     */   
/*     */   protected CreatorProperty(CreatorProperty src, JsonDeserializer<?> deser) {
/* 100 */     super(src, deser);
/* 101 */     this._annotated = src._annotated;
/* 102 */     this._creatorIndex = src._creatorIndex;
/* 103 */     this._injectableValueId = src._injectableValueId;
/* 104 */     this._fallbackSetter = src._fallbackSetter;
/*     */   }
/*     */   
/*     */   public CreatorProperty withName(PropertyName newName)
/*     */   {
/* 109 */     return new CreatorProperty(this, newName);
/*     */   }
/*     */   
/*     */   public CreatorProperty withValueDeserializer(JsonDeserializer<?> deser)
/*     */   {
/* 114 */     if (this._valueDeserializer == deser) {
/* 115 */       return this;
/*     */     }
/* 117 */     return new CreatorProperty(this, deser);
/*     */   }
/*     */   
/*     */   public void fixAccess(DeserializationConfig config)
/*     */   {
/* 122 */     if (this._fallbackSetter != null) {
/* 123 */       this._fallbackSetter.fixAccess(config);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setFallbackSetter(SettableBeanProperty fallbackSetter)
/*     */   {
/* 134 */     this._fallbackSetter = fallbackSetter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object findInjectableValue(DeserializationContext context, Object beanInstance)
/*     */   {
/* 143 */     if (this._injectableValueId == null) {
/* 144 */       throw new IllegalStateException("Property '" + getName() + "' (type " + getClass().getName() + ") has no injectable value id configured");
/*     */     }
/*     */     
/* 147 */     return context.findInjectableValue(this._injectableValueId, this, beanInstance);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void inject(DeserializationContext context, Object beanInstance)
/*     */     throws IOException
/*     */   {
/* 156 */     set(beanInstance, findInjectableValue(context, beanInstance));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/* 167 */     if (this._annotated == null) {
/* 168 */       return null;
/*     */     }
/* 170 */     return this._annotated.getAnnotation(acls);
/*     */   }
/*     */   
/* 173 */   public AnnotatedMember getMember() { return this._annotated; }
/*     */   
/*     */   public int getCreatorIndex() {
/* 176 */     return this._creatorIndex;
/*     */   }
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
/* 189 */     set(instance, deserialize(p, ctxt));
/*     */   }
/*     */   
/*     */ 
/*     */   public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance)
/*     */     throws IOException
/*     */   {
/* 196 */     return setAndReturn(instance, deserialize(p, ctxt));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void set(Object instance, Object value)
/*     */     throws IOException
/*     */   {
/* 205 */     if (this._fallbackSetter == null) {
/* 206 */       throw new IllegalStateException("No fallback setter/field defined: can not use creator property for " + getClass().getName());
/*     */     }
/*     */     
/* 209 */     this._fallbackSetter.set(instance, value);
/*     */   }
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value)
/*     */     throws IOException
/*     */   {
/* 215 */     if (this._fallbackSetter == null) {
/* 216 */       throw new IllegalStateException("No fallback setter/field defined: can not use creator property for " + getClass().getName());
/*     */     }
/*     */     
/* 219 */     return this._fallbackSetter.setAndReturn(instance, value);
/*     */   }
/*     */   
/*     */   public Object getInjectableValueId()
/*     */   {
/* 224 */     return this._injectableValueId;
/*     */   }
/*     */   
/*     */   public String toString() {
/* 228 */     return "[creator property, name '" + getName() + "'; inject id '" + this._injectableValueId + "']";
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\CreatorProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */