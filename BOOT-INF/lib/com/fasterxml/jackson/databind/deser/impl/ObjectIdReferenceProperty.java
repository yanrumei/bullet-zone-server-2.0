/*     */ package com.fasterxml.jackson.databind.deser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import java.io.IOException;
/*     */ import java.lang.annotation.Annotation;
/*     */ 
/*     */ public class ObjectIdReferenceProperty extends SettableBeanProperty
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   private final SettableBeanProperty _forward;
/*     */   
/*     */   public ObjectIdReferenceProperty(SettableBeanProperty forward, ObjectIdInfo objectIdInfo)
/*     */   {
/*  22 */     super(forward);
/*  23 */     this._forward = forward;
/*  24 */     this._objectIdInfo = objectIdInfo;
/*     */   }
/*     */   
/*     */   public ObjectIdReferenceProperty(ObjectIdReferenceProperty src, JsonDeserializer<?> deser)
/*     */   {
/*  29 */     super(src, deser);
/*  30 */     this._forward = src._forward;
/*  31 */     this._objectIdInfo = src._objectIdInfo;
/*     */   }
/*     */   
/*     */   public ObjectIdReferenceProperty(ObjectIdReferenceProperty src, PropertyName newName)
/*     */   {
/*  36 */     super(src, newName);
/*  37 */     this._forward = src._forward;
/*  38 */     this._objectIdInfo = src._objectIdInfo;
/*     */   }
/*     */   
/*     */   public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser)
/*     */   {
/*  43 */     if (this._valueDeserializer == deser) {
/*  44 */       return this;
/*     */     }
/*  46 */     return new ObjectIdReferenceProperty(this, deser);
/*     */   }
/*     */   
/*     */   public SettableBeanProperty withName(PropertyName newName)
/*     */   {
/*  51 */     return new ObjectIdReferenceProperty(this, newName);
/*     */   }
/*     */   
/*     */   public void fixAccess(DeserializationConfig config)
/*     */   {
/*  56 */     if (this._forward != null) {
/*  57 */       this._forward.fixAccess(config);
/*     */     }
/*     */   }
/*     */   
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/*  63 */     return this._forward.getAnnotation(acls);
/*     */   }
/*     */   
/*     */   public com.fasterxml.jackson.databind.introspect.AnnotatedMember getMember()
/*     */   {
/*  68 */     return this._forward.getMember();
/*     */   }
/*     */   
/*     */   public int getCreatorIndex()
/*     */   {
/*  73 */     return this._forward.getCreatorIndex();
/*     */   }
/*     */   
/*     */   public void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException
/*     */   {
/*  78 */     deserializeSetAndReturn(p, ctxt, instance);
/*     */   }
/*     */   
/*     */   public Object deserializeSetAndReturn(JsonParser p, DeserializationContext ctxt, Object instance) throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  85 */       return setAndReturn(instance, deserialize(p, ctxt));
/*     */     } catch (UnresolvedForwardReference reference) {
/*  87 */       boolean usingIdentityInfo = (this._objectIdInfo != null) || (this._valueDeserializer.getObjectIdReader() != null);
/*  88 */       if (!usingIdentityInfo) {
/*  89 */         throw JsonMappingException.from(p, "Unresolved forward reference but no identity info.", reference);
/*     */       }
/*  91 */       reference.getRoid().appendReferring(new PropertyReferring(this, reference, this._type.getRawClass(), instance)); }
/*  92 */     return null;
/*     */   }
/*     */   
/*     */   public void set(Object instance, Object value)
/*     */     throws IOException
/*     */   {
/*  98 */     this._forward.set(instance, value);
/*     */   }
/*     */   
/*     */   public Object setAndReturn(Object instance, Object value) throws IOException
/*     */   {
/* 103 */     return this._forward.setAndReturn(instance, value);
/*     */   }
/*     */   
/*     */   public static final class PropertyReferring extends ReadableObjectId.Referring
/*     */   {
/*     */     private final ObjectIdReferenceProperty _parent;
/*     */     public final Object _pojo;
/*     */     
/*     */     public PropertyReferring(ObjectIdReferenceProperty parent, UnresolvedForwardReference ref, Class<?> type, Object ob)
/*     */     {
/* 113 */       super(type);
/* 114 */       this._parent = parent;
/* 115 */       this._pojo = ob;
/*     */     }
/*     */     
/*     */     public void handleResolvedForwardReference(Object id, Object value)
/*     */       throws IOException
/*     */     {
/* 121 */       if (!hasId(id)) {
/* 122 */         throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id + "] that wasn't previously seen as unresolved.");
/*     */       }
/*     */       
/* 125 */       this._parent.set(this._pojo, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\impl\ObjectIdReferenceProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */