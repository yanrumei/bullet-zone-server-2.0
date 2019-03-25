/*     */ package com.fasterxml.jackson.databind.jsontype.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.std.NullifyingDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
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
/*     */ public abstract class TypeDeserializerBase
/*     */   extends TypeDeserializer
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final TypeIdResolver _idResolver;
/*     */   protected final JavaType _baseType;
/*     */   protected final BeanProperty _property;
/*     */   protected final JavaType _defaultImpl;
/*     */   protected final String _typePropertyName;
/*     */   protected final boolean _typeIdVisible;
/*     */   protected final Map<String, JsonDeserializer<Object>> _deserializers;
/*     */   protected JsonDeserializer<Object> _defaultImplDeserializer;
/*     */   
/*     */   protected TypeDeserializerBase(JavaType baseType, TypeIdResolver idRes, String typePropertyName, boolean typeIdVisible, JavaType defaultImpl)
/*     */   {
/*  75 */     this._baseType = baseType;
/*  76 */     this._idResolver = idRes;
/*     */     
/*  78 */     this._typePropertyName = (typePropertyName == null ? "" : typePropertyName);
/*  79 */     this._typeIdVisible = typeIdVisible;
/*     */     
/*  81 */     this._deserializers = new ConcurrentHashMap(16, 0.75F, 2);
/*  82 */     this._defaultImpl = defaultImpl;
/*  83 */     this._property = null;
/*     */   }
/*     */   
/*     */   protected TypeDeserializerBase(TypeDeserializerBase src, BeanProperty property)
/*     */   {
/*  88 */     this._baseType = src._baseType;
/*  89 */     this._idResolver = src._idResolver;
/*  90 */     this._typePropertyName = src._typePropertyName;
/*  91 */     this._typeIdVisible = src._typeIdVisible;
/*  92 */     this._deserializers = src._deserializers;
/*  93 */     this._defaultImpl = src._defaultImpl;
/*  94 */     this._defaultImplDeserializer = src._defaultImplDeserializer;
/*  95 */     this._property = property;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract TypeDeserializer forProperty(BeanProperty paramBeanProperty);
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract JsonTypeInfo.As getTypeInclusion();
/*     */   
/*     */ 
/*     */ 
/*     */   public String baseTypeName()
/*     */   {
/* 110 */     return this._baseType.getRawClass().getName();
/*     */   }
/*     */   
/* 113 */   public final String getPropertyName() { return this._typePropertyName; }
/*     */   
/*     */   public TypeIdResolver getTypeIdResolver() {
/* 116 */     return this._idResolver;
/*     */   }
/*     */   
/*     */   public Class<?> getDefaultImpl() {
/* 120 */     return this._defaultImpl == null ? null : this._defaultImpl.getRawClass();
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 126 */     StringBuilder sb = new StringBuilder();
/* 127 */     sb.append('[').append(getClass().getName());
/* 128 */     sb.append("; base-type:").append(this._baseType);
/* 129 */     sb.append("; id-resolver: ").append(this._idResolver);
/* 130 */     sb.append(']');
/* 131 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonDeserializer<Object> _findDeserializer(DeserializationContext ctxt, String typeId)
/*     */     throws IOException
/*     */   {
/* 143 */     JsonDeserializer<Object> deser = (JsonDeserializer)this._deserializers.get(typeId);
/* 144 */     if (deser == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 150 */       JavaType type = this._idResolver.typeFromId(ctxt, typeId);
/* 151 */       if (type == null)
/*     */       {
/* 153 */         deser = _findDefaultImplDeserializer(ctxt);
/* 154 */         if (deser == null)
/*     */         {
/* 156 */           JavaType actual = _handleUnknownTypeId(ctxt, typeId, this._idResolver, this._baseType);
/* 157 */           if (actual == null)
/*     */           {
/* 159 */             return null;
/*     */           }
/*     */           
/* 162 */           deser = ctxt.findContextualValueDeserializer(actual, this._property);
/*     */ 
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/*     */ 
/* 173 */         if ((this._baseType != null) && (this._baseType.getClass() == type.getClass()))
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 184 */           if (!type.hasGenericTypes()) {
/* 185 */             type = ctxt.getTypeFactory().constructSpecializedType(this._baseType, type.getRawClass());
/*     */           }
/*     */         }
/* 188 */         deser = ctxt.findContextualValueDeserializer(type, this._property);
/*     */       }
/* 190 */       this._deserializers.put(typeId, deser);
/*     */     }
/* 192 */     return deser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonDeserializer<Object> _findDefaultImplDeserializer(DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 201 */     if (this._defaultImpl == null) {
/* 202 */       if (!ctxt.isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)) {
/* 203 */         return NullifyingDeserializer.instance;
/*     */       }
/* 205 */       return null;
/*     */     }
/* 207 */     Class<?> raw = this._defaultImpl.getRawClass();
/* 208 */     if (ClassUtil.isBogusClass(raw)) {
/* 209 */       return NullifyingDeserializer.instance;
/*     */     }
/*     */     
/* 212 */     synchronized (this._defaultImpl) {
/* 213 */       if (this._defaultImplDeserializer == null) {
/* 214 */         this._defaultImplDeserializer = ctxt.findContextualValueDeserializer(this._defaultImpl, this._property);
/*     */       }
/*     */       
/* 217 */       return this._defaultImplDeserializer;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 230 */     return _deserializeWithNativeTypeId(jp, ctxt, jp.getTypeId());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object _deserializeWithNativeTypeId(JsonParser jp, DeserializationContext ctxt, Object typeId)
/*     */     throws IOException
/*     */   {
/*     */     JsonDeserializer<Object> deser;
/*     */     
/*     */ 
/*     */ 
/* 243 */     if (typeId == null)
/*     */     {
/*     */ 
/*     */ 
/* 247 */       JsonDeserializer<Object> deser = _findDefaultImplDeserializer(ctxt);
/* 248 */       if (deser == null) {
/* 249 */         ctxt.reportMappingException("No (native) type id found when one was expected for polymorphic type handling", new Object[0]);
/* 250 */         return null;
/*     */       }
/*     */     } else {
/* 253 */       String typeIdStr = (typeId instanceof String) ? (String)typeId : String.valueOf(typeId);
/* 254 */       deser = _findDeserializer(ctxt, typeIdStr);
/*     */     }
/* 256 */     return deser.deserialize(jp, ctxt);
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
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JavaType _handleUnknownTypeId(DeserializationContext ctxt, String typeId, TypeIdResolver idResolver, JavaType baseType)
/*     */     throws IOException
/*     */   {
/* 276 */     String extraDesc = idResolver.getDescForKnownTypeIds();
/* 277 */     if (extraDesc == null) {
/* 278 */       extraDesc = "known type ids are not statically known";
/*     */     } else {
/* 280 */       extraDesc = "known type ids = " + extraDesc;
/*     */     }
/* 282 */     return ctxt.handleUnknownTypeId(this._baseType, typeId, idResolver, extraDesc);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsontype\impl\TypeDeserializerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */