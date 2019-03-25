/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
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
/*     */ public final class ManagedReferenceProperty
/*     */   extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final String _referenceName;
/*     */   protected final boolean _isContainer;
/*     */   protected final SettableBeanProperty _managedProperty;
/*     */   protected final SettableBeanProperty _backProperty;
/*     */   
/*     */   public ManagedReferenceProperty(SettableBeanProperty forward, String refName, SettableBeanProperty backward, Annotations contextAnnotations, boolean isContainer)
/*     */   {
/*  39 */     super(forward.getFullName(), forward.getType(), forward.getWrapperName(), forward.getValueTypeDeserializer(), contextAnnotations, forward.getMetadata());
/*     */     
/*     */ 
/*  42 */     this._referenceName = refName;
/*  43 */     this._managedProperty = forward;
/*  44 */     this._backProperty = backward;
/*  45 */     this._isContainer = isContainer;
/*     */   }
/*     */   
/*     */   protected ManagedReferenceProperty(ManagedReferenceProperty src, JsonDeserializer<?> deser)
/*     */   {
/*  50 */     super(src, deser);
/*  51 */     this._referenceName = src._referenceName;
/*  52 */     this._isContainer = src._isContainer;
/*  53 */     this._managedProperty = src._managedProperty;
/*  54 */     this._backProperty = src._backProperty;
/*     */   }
/*     */   
/*     */   protected ManagedReferenceProperty(ManagedReferenceProperty src, PropertyName newName) {
/*  58 */     super(src, newName);
/*  59 */     this._referenceName = src._referenceName;
/*  60 */     this._isContainer = src._isContainer;
/*  61 */     this._managedProperty = src._managedProperty;
/*  62 */     this._backProperty = src._backProperty;
/*     */   }
/*     */   
/*     */   public ManagedReferenceProperty withName(PropertyName newName)
/*     */   {
/*  67 */     return new ManagedReferenceProperty(this, newName);
/*     */   }
/*     */   
/*     */   public ManagedReferenceProperty withValueDeserializer(JsonDeserializer<?> deser)
/*     */   {
/*  72 */     if (this._valueDeserializer == deser) {
/*  73 */       return this;
/*     */     }
/*  75 */     return new ManagedReferenceProperty(this, deser);
/*     */   }
/*     */   
/*     */   public void fixAccess(DeserializationConfig config)
/*     */   {
/*  80 */     this._managedProperty.fixAccess(config);
/*  81 */     this._backProperty.fixAccess(config);
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
/*  92 */     return this._managedProperty.getAnnotation(acls);
/*     */   }
/*     */   
/*  95 */   public AnnotatedMember getMember() { return this._managedProperty.getMember(); }
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
/* 106 */     set(instance, this._managedProperty.deserialize(p, ctxt));
/*     */   }
/*     */   
/*     */   public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance)
/*     */     throws IOException
/*     */   {
/* 112 */     return setAndReturn(instance, deserialize(p, ctxt));
/*     */   }
/*     */   
/*     */   public final void set(Object instance, Object value) throws IOException
/*     */   {
/* 117 */     setAndReturn(instance, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object setAndReturn(Object instance, Object value)
/*     */     throws IOException
/*     */   {
/* 126 */     if (value != null) {
/* 127 */       if (this._isContainer) {
/* 128 */         if ((value instanceof Object[])) {
/* 129 */           for (Object ob : (Object[])value) {
/* 130 */             if (ob != null) this._backProperty.set(ob, instance);
/*     */           }
/* 132 */         } else if ((value instanceof Collection)) {
/* 133 */           for (Object ob : (Collection)value) {
/* 134 */             if (ob != null) this._backProperty.set(ob, instance);
/*     */           }
/* 136 */         } else if ((value instanceof Map)) {
/* 137 */           for (Object ob : ((Map)value).values()) {
/* 138 */             if (ob != null) this._backProperty.set(ob, instance);
/*     */           }
/*     */         } else {
/* 141 */           throw new IllegalStateException("Unsupported container type (" + value.getClass().getName() + ") when resolving reference '" + this._referenceName + "'");
/*     */         }
/*     */       }
/*     */       else {
/* 145 */         this._backProperty.set(value, instance);
/*     */       }
/*     */     }
/*     */     
/* 149 */     return this._managedProperty.setAndReturn(instance, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\impl\ManagedReferenceProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */