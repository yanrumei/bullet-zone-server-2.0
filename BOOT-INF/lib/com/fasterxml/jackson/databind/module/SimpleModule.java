/*     */ package com.fasterxml.jackson.databind.module;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.Module;
/*     */ import com.fasterxml.jackson.databind.Module.SetupContext;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
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
/*     */ public class SimpleModule
/*     */   extends Module
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final String _name;
/*     */   protected final Version _version;
/*  37 */   protected SimpleSerializers _serializers = null;
/*  38 */   protected SimpleDeserializers _deserializers = null;
/*     */   
/*  40 */   protected SimpleSerializers _keySerializers = null;
/*  41 */   protected SimpleKeyDeserializers _keyDeserializers = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  48 */   protected SimpleAbstractTypeResolver _abstractTypes = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  55 */   protected SimpleValueInstantiators _valueInstantiators = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  60 */   protected BeanDeserializerModifier _deserializerModifier = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  65 */   protected BeanSerializerModifier _serializerModifier = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  71 */   protected HashMap<Class<?>, Class<?>> _mixins = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  76 */   protected LinkedHashSet<NamedType> _subtypes = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  81 */   protected PropertyNamingStrategy _namingStrategy = null;
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
/*     */   public SimpleModule()
/*     */   {
/*  97 */     this._name = (getClass() == SimpleModule.class ? "SimpleModule-" + System.identityHashCode(this) : getClass().getName());
/*     */     
/*     */ 
/* 100 */     this._version = Version.unknownVersion();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleModule(String name)
/*     */   {
/* 108 */     this(name, Version.unknownVersion());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleModule(Version version)
/*     */   {
/* 116 */     this._name = version.getArtifactId();
/* 117 */     this._version = version;
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
/*     */   public SimpleModule(String name, Version version)
/*     */   {
/* 130 */     this._name = name;
/* 131 */     this._version = version;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleModule(String name, Version version, Map<Class<?>, JsonDeserializer<?>> deserializers)
/*     */   {
/* 139 */     this(name, version, deserializers, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleModule(String name, Version version, List<JsonSerializer<?>> serializers)
/*     */   {
/* 147 */     this(name, version, null, serializers);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleModule(String name, Version version, Map<Class<?>, JsonDeserializer<?>> deserializers, List<JsonSerializer<?>> serializers)
/*     */   {
/* 157 */     this._name = name;
/* 158 */     this._version = version;
/* 159 */     if (deserializers != null) {
/* 160 */       this._deserializers = new SimpleDeserializers(deserializers);
/*     */     }
/* 162 */     if (serializers != null) {
/* 163 */       this._serializers = new SimpleSerializers(serializers);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getTypeId()
/*     */   {
/* 174 */     if (getClass() == SimpleModule.class) {
/* 175 */       return null;
/*     */     }
/* 177 */     return super.getTypeId();
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
/*     */   public void setSerializers(SimpleSerializers s)
/*     */   {
/* 190 */     this._serializers = s;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setDeserializers(SimpleDeserializers d)
/*     */   {
/* 197 */     this._deserializers = d;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setKeySerializers(SimpleSerializers ks)
/*     */   {
/* 204 */     this._keySerializers = ks;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setKeyDeserializers(SimpleKeyDeserializers kd)
/*     */   {
/* 211 */     this._keyDeserializers = kd;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setAbstractTypes(SimpleAbstractTypeResolver atr)
/*     */   {
/* 218 */     this._abstractTypes = atr;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setValueInstantiators(SimpleValueInstantiators svi)
/*     */   {
/* 225 */     this._valueInstantiators = svi;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SimpleModule setDeserializerModifier(BeanDeserializerModifier mod)
/*     */   {
/* 232 */     this._deserializerModifier = mod;
/* 233 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public SimpleModule setSerializerModifier(BeanSerializerModifier mod)
/*     */   {
/* 240 */     this._serializerModifier = mod;
/* 241 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected SimpleModule setNamingStrategy(PropertyNamingStrategy naming)
/*     */   {
/* 248 */     this._namingStrategy = naming;
/* 249 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleModule addSerializer(JsonSerializer<?> ser)
/*     */   {
/* 260 */     if (this._serializers == null) {
/* 261 */       this._serializers = new SimpleSerializers();
/*     */     }
/* 263 */     this._serializers.addSerializer(ser);
/* 264 */     return this;
/*     */   }
/*     */   
/*     */   public <T> SimpleModule addSerializer(Class<? extends T> type, JsonSerializer<T> ser)
/*     */   {
/* 269 */     if (this._serializers == null) {
/* 270 */       this._serializers = new SimpleSerializers();
/*     */     }
/* 272 */     this._serializers.addSerializer(type, ser);
/* 273 */     return this;
/*     */   }
/*     */   
/*     */   public <T> SimpleModule addKeySerializer(Class<? extends T> type, JsonSerializer<T> ser)
/*     */   {
/* 278 */     if (this._keySerializers == null) {
/* 279 */       this._keySerializers = new SimpleSerializers();
/*     */     }
/* 281 */     this._keySerializers.addSerializer(type, ser);
/* 282 */     return this;
/*     */   }
/*     */   
/*     */   public <T> SimpleModule addDeserializer(Class<T> type, JsonDeserializer<? extends T> deser)
/*     */   {
/* 287 */     if (this._deserializers == null) {
/* 288 */       this._deserializers = new SimpleDeserializers();
/*     */     }
/* 290 */     this._deserializers.addDeserializer(type, deser);
/* 291 */     return this;
/*     */   }
/*     */   
/*     */   public SimpleModule addKeyDeserializer(Class<?> type, KeyDeserializer deser)
/*     */   {
/* 296 */     if (this._keyDeserializers == null) {
/* 297 */       this._keyDeserializers = new SimpleKeyDeserializers();
/*     */     }
/* 299 */     this._keyDeserializers.addDeserializer(type, deser);
/* 300 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T> SimpleModule addAbstractTypeMapping(Class<T> superType, Class<? extends T> subType)
/*     */   {
/* 311 */     if (this._abstractTypes == null) {
/* 312 */       this._abstractTypes = new SimpleAbstractTypeResolver();
/*     */     }
/*     */     
/* 315 */     this._abstractTypes = this._abstractTypes.addMapping(superType, subType);
/* 316 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleModule addValueInstantiator(Class<?> beanType, ValueInstantiator inst)
/*     */   {
/* 328 */     if (this._valueInstantiators == null) {
/* 329 */       this._valueInstantiators = new SimpleValueInstantiators();
/*     */     }
/* 331 */     this._valueInstantiators = this._valueInstantiators.addValueInstantiator(beanType, inst);
/* 332 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleModule registerSubtypes(Class<?>... subtypes)
/*     */   {
/* 342 */     if (this._subtypes == null) {
/* 343 */       this._subtypes = new LinkedHashSet(Math.max(16, subtypes.length));
/*     */     }
/* 345 */     for (Class<?> subtype : subtypes) {
/* 346 */       this._subtypes.add(new NamedType(subtype));
/*     */     }
/* 348 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleModule registerSubtypes(NamedType... subtypes)
/*     */   {
/* 358 */     if (this._subtypes == null) {
/* 359 */       this._subtypes = new LinkedHashSet(Math.max(16, subtypes.length));
/*     */     }
/* 361 */     for (NamedType subtype : subtypes) {
/* 362 */       this._subtypes.add(subtype);
/*     */     }
/* 364 */     return this;
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
/*     */   public SimpleModule setMixInAnnotation(Class<?> targetType, Class<?> mixinClass)
/*     */   {
/* 377 */     if (this._mixins == null) {
/* 378 */       this._mixins = new HashMap();
/*     */     }
/* 380 */     this._mixins.put(targetType, mixinClass);
/* 381 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getModuleName()
/*     */   {
/* 392 */     return this._name;
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
/*     */   public void setupModule(Module.SetupContext context)
/*     */   {
/* 405 */     if (this._serializers != null) {
/* 406 */       context.addSerializers(this._serializers);
/*     */     }
/* 408 */     if (this._deserializers != null) {
/* 409 */       context.addDeserializers(this._deserializers);
/*     */     }
/* 411 */     if (this._keySerializers != null) {
/* 412 */       context.addKeySerializers(this._keySerializers);
/*     */     }
/* 414 */     if (this._keyDeserializers != null) {
/* 415 */       context.addKeyDeserializers(this._keyDeserializers);
/*     */     }
/* 417 */     if (this._abstractTypes != null) {
/* 418 */       context.addAbstractTypeResolver(this._abstractTypes);
/*     */     }
/* 420 */     if (this._valueInstantiators != null) {
/* 421 */       context.addValueInstantiators(this._valueInstantiators);
/*     */     }
/* 423 */     if (this._deserializerModifier != null) {
/* 424 */       context.addBeanDeserializerModifier(this._deserializerModifier);
/*     */     }
/* 426 */     if (this._serializerModifier != null) {
/* 427 */       context.addBeanSerializerModifier(this._serializerModifier);
/*     */     }
/* 429 */     if ((this._subtypes != null) && (this._subtypes.size() > 0)) {
/* 430 */       context.registerSubtypes((NamedType[])this._subtypes.toArray(new NamedType[this._subtypes.size()]));
/*     */     }
/* 432 */     if (this._namingStrategy != null) {
/* 433 */       context.setNamingStrategy(this._namingStrategy);
/*     */     }
/* 435 */     if (this._mixins != null) {
/* 436 */       for (Map.Entry<Class<?>, Class<?>> entry : this._mixins.entrySet()) {
/* 437 */         context.setMixInAnnotations((Class)entry.getKey(), (Class)entry.getValue());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Version version() {
/* 443 */     return this._version;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\module\SimpleModule.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */