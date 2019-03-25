/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator.IdKey;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdResolver;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.InjectableValues;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer.None;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer.None;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map.Entry;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DefaultDeserializationContext
/*     */   extends DeserializationContext
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected transient LinkedHashMap<ObjectIdGenerator.IdKey, ReadableObjectId> _objectIds;
/*     */   private List<ObjectIdResolver> _objectIdResolvers;
/*     */   
/*     */   protected DefaultDeserializationContext(DeserializerFactory df, DeserializerCache cache)
/*     */   {
/*  44 */     super(df, cache);
/*     */   }
/*     */   
/*     */   protected DefaultDeserializationContext(DefaultDeserializationContext src, DeserializationConfig config, JsonParser jp, InjectableValues values)
/*     */   {
/*  49 */     super(src, config, jp, values);
/*     */   }
/*     */   
/*     */   protected DefaultDeserializationContext(DefaultDeserializationContext src, DeserializerFactory factory)
/*     */   {
/*  54 */     super(src, factory);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected DefaultDeserializationContext(DefaultDeserializationContext src)
/*     */   {
/*  61 */     super(src);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultDeserializationContext copy()
/*     */   {
/*  73 */     throw new IllegalStateException("DefaultDeserializationContext sub-class not overriding copy()");
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
/*     */   public ReadableObjectId findObjectId(Object id, ObjectIdGenerator<?> gen, ObjectIdResolver resolverType)
/*     */   {
/*  88 */     if (id == null) {
/*  89 */       return null;
/*     */     }
/*     */     
/*  92 */     ObjectIdGenerator.IdKey key = gen.key(id);
/*     */     
/*  94 */     if (this._objectIds == null) {
/*  95 */       this._objectIds = new LinkedHashMap();
/*     */     } else {
/*  97 */       ReadableObjectId entry = (ReadableObjectId)this._objectIds.get(key);
/*  98 */       if (entry != null) {
/*  99 */         return entry;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 104 */     ObjectIdResolver resolver = null;
/*     */     
/* 106 */     if (this._objectIdResolvers == null) {
/* 107 */       this._objectIdResolvers = new ArrayList(8);
/*     */     } else {
/* 109 */       for (ObjectIdResolver res : this._objectIdResolvers) {
/* 110 */         if (res.canUseFor(resolverType)) {
/* 111 */           resolver = res;
/* 112 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 117 */     if (resolver == null) {
/* 118 */       resolver = resolverType.newForDeserialization(this);
/* 119 */       this._objectIdResolvers.add(resolver);
/*     */     }
/*     */     
/* 122 */     ReadableObjectId entry = createReadableObjectId(key);
/* 123 */     entry.setResolver(resolver);
/* 124 */     this._objectIds.put(key, entry);
/* 125 */     return entry;
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
/*     */   protected ReadableObjectId createReadableObjectId(ObjectIdGenerator.IdKey key)
/*     */   {
/* 141 */     return new ReadableObjectId(key);
/*     */   }
/*     */   
/*     */   public void checkUnresolvedObjectId()
/*     */     throws UnresolvedForwardReference
/*     */   {
/* 147 */     if (this._objectIds == null) {
/* 148 */       return;
/*     */     }
/*     */     
/* 151 */     if (!isEnabled(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS)) {
/* 152 */       return;
/*     */     }
/* 154 */     UnresolvedForwardReference exception = null;
/* 155 */     for (Map.Entry<ObjectIdGenerator.IdKey, ReadableObjectId> entry : this._objectIds.entrySet()) {
/* 156 */       ReadableObjectId roid = (ReadableObjectId)entry.getValue();
/* 157 */       if ((roid.hasReferringProperties()) && 
/*     */       
/*     */ 
/*     */ 
/* 161 */         (!tryToResolveUnresolvedObjectId(roid)))
/*     */       {
/*     */ 
/* 164 */         if (exception == null) {
/* 165 */           exception = new UnresolvedForwardReference(getParser(), "Unresolved forward references for: ");
/*     */         }
/* 167 */         key = roid.getKey().key;
/* 168 */         for (iterator = roid.referringProperties(); iterator.hasNext();) {
/* 169 */           ReadableObjectId.Referring referring = (ReadableObjectId.Referring)iterator.next();
/* 170 */           exception.addUnresolvedId(key, referring.getBeanType(), referring.getLocation()); } } }
/*     */     Object key;
/*     */     Iterator<ReadableObjectId.Referring> iterator;
/* 173 */     if (exception != null) {
/* 174 */       throw exception;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean tryToResolveUnresolvedObjectId(ReadableObjectId roid)
/*     */   {
/* 190 */     return roid.tryToResolveUnresolved(this);
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
/*     */   public JsonDeserializer<Object> deserializerInstance(Annotated ann, Object deserDef)
/*     */     throws JsonMappingException
/*     */   {
/* 204 */     if (deserDef == null) {
/* 205 */       return null;
/*     */     }
/*     */     JsonDeserializer<?> deser;
/*     */     JsonDeserializer<?> deser;
/* 209 */     if ((deserDef instanceof JsonDeserializer)) {
/* 210 */       deser = (JsonDeserializer)deserDef;
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 215 */       if (!(deserDef instanceof Class)) {
/* 216 */         throw new IllegalStateException("AnnotationIntrospector returned deserializer definition of type " + deserDef.getClass().getName() + "; expected type JsonDeserializer or Class<JsonDeserializer> instead");
/*     */       }
/* 218 */       Class<?> deserClass = (Class)deserDef;
/*     */       
/* 220 */       if ((deserClass == JsonDeserializer.None.class) || (ClassUtil.isBogusClass(deserClass))) {
/* 221 */         return null;
/*     */       }
/* 223 */       if (!JsonDeserializer.class.isAssignableFrom(deserClass)) {
/* 224 */         throw new IllegalStateException("AnnotationIntrospector returned Class " + deserClass.getName() + "; expected Class<JsonDeserializer>");
/*     */       }
/* 226 */       HandlerInstantiator hi = this._config.getHandlerInstantiator();
/* 227 */       deser = hi == null ? null : hi.deserializerInstance(this._config, ann, deserClass);
/* 228 */       if (deser == null) {
/* 229 */         deser = (JsonDeserializer)ClassUtil.createInstance(deserClass, this._config.canOverrideAccessModifiers());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 234 */     if ((deser instanceof ResolvableDeserializer)) {
/* 235 */       ((ResolvableDeserializer)deser).resolve(this);
/*     */     }
/* 237 */     return deser;
/*     */   }
/*     */   
/*     */ 
/*     */   public final KeyDeserializer keyDeserializerInstance(Annotated ann, Object deserDef)
/*     */     throws JsonMappingException
/*     */   {
/* 244 */     if (deserDef == null) {
/* 245 */       return null;
/*     */     }
/*     */     
/*     */     KeyDeserializer deser;
/*     */     KeyDeserializer deser;
/* 250 */     if ((deserDef instanceof KeyDeserializer)) {
/* 251 */       deser = (KeyDeserializer)deserDef;
/*     */     } else {
/* 253 */       if (!(deserDef instanceof Class)) {
/* 254 */         throw new IllegalStateException("AnnotationIntrospector returned key deserializer definition of type " + deserDef.getClass().getName() + "; expected type KeyDeserializer or Class<KeyDeserializer> instead");
/*     */       }
/*     */       
/*     */ 
/* 258 */       Class<?> deserClass = (Class)deserDef;
/*     */       
/* 260 */       if ((deserClass == KeyDeserializer.None.class) || (ClassUtil.isBogusClass(deserClass))) {
/* 261 */         return null;
/*     */       }
/* 263 */       if (!KeyDeserializer.class.isAssignableFrom(deserClass)) {
/* 264 */         throw new IllegalStateException("AnnotationIntrospector returned Class " + deserClass.getName() + "; expected Class<KeyDeserializer>");
/*     */       }
/*     */       
/* 267 */       HandlerInstantiator hi = this._config.getHandlerInstantiator();
/* 268 */       deser = hi == null ? null : hi.keyDeserializerInstance(this._config, ann, deserClass);
/* 269 */       if (deser == null) {
/* 270 */         deser = (KeyDeserializer)ClassUtil.createInstance(deserClass, this._config.canOverrideAccessModifiers());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 275 */     if ((deser instanceof ResolvableDeserializer)) {
/* 276 */       ((ResolvableDeserializer)deser).resolve(this);
/*     */     }
/* 278 */     return deser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract DefaultDeserializationContext with(DeserializerFactory paramDeserializerFactory);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract DefaultDeserializationContext createInstance(DeserializationConfig paramDeserializationConfig, JsonParser paramJsonParser, InjectableValues paramInjectableValues);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final class Impl
/*     */     extends DefaultDeserializationContext
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Impl(DeserializerFactory df)
/*     */     {
/* 318 */       super(null);
/*     */     }
/*     */     
/*     */     protected Impl(Impl src, DeserializationConfig config, JsonParser jp, InjectableValues values)
/*     */     {
/* 323 */       super(config, jp, values);
/*     */     }
/*     */     
/* 326 */     protected Impl(Impl src) { super(); }
/*     */     
/*     */     protected Impl(Impl src, DeserializerFactory factory) {
/* 329 */       super(factory);
/*     */     }
/*     */     
/*     */     public DefaultDeserializationContext copy()
/*     */     {
/* 334 */       if (getClass() != Impl.class) {
/* 335 */         return super.copy();
/*     */       }
/* 337 */       return new Impl(this);
/*     */     }
/*     */     
/*     */ 
/*     */     public DefaultDeserializationContext createInstance(DeserializationConfig config, JsonParser jp, InjectableValues values)
/*     */     {
/* 343 */       return new Impl(this, config, jp, values);
/*     */     }
/*     */     
/*     */     public DefaultDeserializationContext with(DeserializerFactory factory)
/*     */     {
/* 348 */       return new Impl(this, factory);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\DefaultDeserializationContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */