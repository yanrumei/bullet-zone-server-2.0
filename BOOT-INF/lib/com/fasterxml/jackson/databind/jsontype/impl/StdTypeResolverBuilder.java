/*     */ package com.fasterxml.jackson.databind.jsontype.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.annotation.NoClass;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StdTypeResolverBuilder
/*     */   implements TypeResolverBuilder<StdTypeResolverBuilder>
/*     */ {
/*     */   protected JsonTypeInfo.Id _idType;
/*     */   protected JsonTypeInfo.As _includeAs;
/*     */   protected String _typeProperty;
/*  29 */   protected boolean _typeIdVisible = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Class<?> _defaultImpl;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TypeIdResolver _customIdResolver;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static StdTypeResolverBuilder noTypeInfoBuilder()
/*     */   {
/*  50 */     return new StdTypeResolverBuilder().init(JsonTypeInfo.Id.NONE, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public StdTypeResolverBuilder init(JsonTypeInfo.Id idType, TypeIdResolver idRes)
/*     */   {
/*  57 */     if (idType == null) {
/*  58 */       throw new IllegalArgumentException("idType can not be null");
/*     */     }
/*  60 */     this._idType = idType;
/*  61 */     this._customIdResolver = idRes;
/*     */     
/*  63 */     this._typeProperty = idType.getDefaultPropertyName();
/*  64 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TypeSerializer buildTypeSerializer(SerializationConfig config, JavaType baseType, Collection<NamedType> subtypes)
/*     */   {
/*  71 */     if (this._idType == JsonTypeInfo.Id.NONE) { return null;
/*     */     }
/*     */     
/*  74 */     if (baseType.isPrimitive()) {
/*  75 */       return null;
/*     */     }
/*  77 */     TypeIdResolver idRes = idResolver(config, baseType, subtypes, true, false);
/*  78 */     switch (this._includeAs) {
/*     */     case WRAPPER_ARRAY: 
/*  80 */       return new AsArrayTypeSerializer(idRes, null);
/*     */     case PROPERTY: 
/*  82 */       return new AsPropertyTypeSerializer(idRes, null, this._typeProperty);
/*     */     case WRAPPER_OBJECT: 
/*  84 */       return new AsWrapperTypeSerializer(idRes, null);
/*     */     case EXTERNAL_PROPERTY: 
/*  86 */       return new AsExternalTypeSerializer(idRes, null, this._typeProperty);
/*     */     
/*     */     case EXISTING_PROPERTY: 
/*  89 */       return new AsExistingPropertyTypeSerializer(idRes, null, this._typeProperty);
/*     */     }
/*  91 */     throw new IllegalStateException("Do not know how to construct standard type serializer for inclusion type: " + this._includeAs);
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
/*     */   public TypeDeserializer buildTypeDeserializer(DeserializationConfig config, JavaType baseType, Collection<NamedType> subtypes)
/*     */   {
/* 104 */     if (this._idType == JsonTypeInfo.Id.NONE) { return null;
/*     */     }
/*     */     
/* 107 */     if (baseType.isPrimitive()) {
/* 108 */       return null;
/*     */     }
/*     */     
/* 111 */     TypeIdResolver idRes = idResolver(config, baseType, subtypes, false, true);
/*     */     
/*     */     JavaType defaultImpl;
/*     */     JavaType defaultImpl;
/* 115 */     if (this._defaultImpl == null) {
/* 116 */       defaultImpl = null;
/*     */     }
/*     */     else
/*     */     {
/*     */       JavaType defaultImpl;
/*     */       
/*     */ 
/*     */ 
/* 124 */       if ((this._defaultImpl == Void.class) || (this._defaultImpl == NoClass.class))
/*     */       {
/* 126 */         defaultImpl = config.getTypeFactory().constructType(this._defaultImpl);
/*     */       } else {
/* 128 */         defaultImpl = config.getTypeFactory().constructSpecializedType(baseType, this._defaultImpl);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 134 */     switch (this._includeAs) {
/*     */     case WRAPPER_ARRAY: 
/* 136 */       return new AsArrayTypeDeserializer(baseType, idRes, this._typeProperty, this._typeIdVisible, defaultImpl);
/*     */     
/*     */     case PROPERTY: 
/*     */     case EXISTING_PROPERTY: 
/* 140 */       return new AsPropertyTypeDeserializer(baseType, idRes, this._typeProperty, this._typeIdVisible, defaultImpl, this._includeAs);
/*     */     
/*     */     case WRAPPER_OBJECT: 
/* 143 */       return new AsWrapperTypeDeserializer(baseType, idRes, this._typeProperty, this._typeIdVisible, defaultImpl);
/*     */     
/*     */     case EXTERNAL_PROPERTY: 
/* 146 */       return new AsExternalTypeDeserializer(baseType, idRes, this._typeProperty, this._typeIdVisible, defaultImpl);
/*     */     }
/*     */     
/* 149 */     throw new IllegalStateException("Do not know how to construct standard type serializer for inclusion type: " + this._includeAs);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StdTypeResolverBuilder inclusion(JsonTypeInfo.As includeAs)
/*     */   {
/* 160 */     if (includeAs == null) {
/* 161 */       throw new IllegalArgumentException("includeAs can not be null");
/*     */     }
/* 163 */     this._includeAs = includeAs;
/* 164 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public StdTypeResolverBuilder typeProperty(String typeIdPropName)
/*     */   {
/* 174 */     if ((typeIdPropName == null) || (typeIdPropName.length() == 0)) {
/* 175 */       typeIdPropName = this._idType.getDefaultPropertyName();
/*     */     }
/* 177 */     this._typeProperty = typeIdPropName;
/* 178 */     return this;
/*     */   }
/*     */   
/*     */   public StdTypeResolverBuilder defaultImpl(Class<?> defaultImpl)
/*     */   {
/* 183 */     this._defaultImpl = defaultImpl;
/* 184 */     return this;
/*     */   }
/*     */   
/*     */   public StdTypeResolverBuilder typeIdVisibility(boolean isVisible)
/*     */   {
/* 189 */     this._typeIdVisible = isVisible;
/* 190 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 199 */   public Class<?> getDefaultImpl() { return this._defaultImpl; }
/*     */   
/* 201 */   public String getTypeProperty() { return this._typeProperty; }
/* 202 */   public boolean isTypeIdVisible() { return this._typeIdVisible; }
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
/*     */   protected TypeIdResolver idResolver(MapperConfig<?> config, JavaType baseType, Collection<NamedType> subtypes, boolean forSer, boolean forDeser)
/*     */   {
/* 219 */     if (this._customIdResolver != null) return this._customIdResolver;
/* 220 */     if (this._idType == null) throw new IllegalStateException("Can not build, 'init()' not yet called");
/* 221 */     switch (this._idType) {
/*     */     case CLASS: 
/* 223 */       return new ClassNameIdResolver(baseType, config.getTypeFactory());
/*     */     case MINIMAL_CLASS: 
/* 225 */       return new MinimalClassNameIdResolver(baseType, config.getTypeFactory());
/*     */     case NAME: 
/* 227 */       return TypeNameIdResolver.construct(config, baseType, subtypes, forSer, forDeser);
/*     */     case NONE: 
/* 229 */       return null;
/*     */     }
/*     */     
/* 232 */     throw new IllegalStateException("Do not know how to construct standard type id resolver for idType: " + this._idType);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsontype\impl\StdTypeResolverBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */