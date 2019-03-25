/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.util.EnumMap;
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
/*     */ public class EnumMapDeserializer
/*     */   extends ContainerDeserializerBase<EnumMap<?, ?>>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JavaType _mapType;
/*     */   protected final Class<?> _enumClass;
/*     */   protected KeyDeserializer _keyDeserializer;
/*     */   protected JsonDeserializer<Object> _valueDeserializer;
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   
/*     */   public EnumMapDeserializer(JavaType mapType, KeyDeserializer keyDeserializer, JsonDeserializer<?> valueDeser, TypeDeserializer valueTypeDeser)
/*     */   {
/*  46 */     super(mapType);
/*  47 */     this._mapType = mapType;
/*  48 */     this._enumClass = mapType.getKeyType().getRawClass();
/*  49 */     this._keyDeserializer = keyDeserializer;
/*  50 */     this._valueDeserializer = valueDeser;
/*  51 */     this._valueTypeDeserializer = valueTypeDeser;
/*     */   }
/*     */   
/*     */   public EnumMapDeserializer withResolved(KeyDeserializer keyDeserializer, JsonDeserializer<?> valueDeserializer, TypeDeserializer valueTypeDeser)
/*     */   {
/*  56 */     if ((keyDeserializer == this._keyDeserializer) && (valueDeserializer == this._valueDeserializer) && (valueTypeDeser == this._valueTypeDeserializer)) {
/*  57 */       return this;
/*     */     }
/*  59 */     return new EnumMapDeserializer(this._mapType, keyDeserializer, valueDeserializer, this._valueTypeDeserializer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  72 */     KeyDeserializer kd = this._keyDeserializer;
/*  73 */     if (kd == null) {
/*  74 */       kd = ctxt.findKeyDeserializer(this._mapType.getKeyType(), property);
/*     */     }
/*  76 */     JsonDeserializer<?> vd = this._valueDeserializer;
/*  77 */     JavaType vt = this._mapType.getContentType();
/*  78 */     if (vd == null) {
/*  79 */       vd = ctxt.findContextualValueDeserializer(vt, property);
/*     */     } else {
/*  81 */       vd = ctxt.handleSecondaryContextualization(vd, property, vt);
/*     */     }
/*  83 */     TypeDeserializer vtd = this._valueTypeDeserializer;
/*  84 */     if (vtd != null) {
/*  85 */       vtd = vtd.forProperty(property);
/*     */     }
/*  87 */     return withResolved(kd, vd, vtd);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isCachable()
/*     */   {
/*  97 */     return (this._valueDeserializer == null) && (this._keyDeserializer == null) && (this._valueTypeDeserializer == null);
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
/*     */   public JavaType getContentType()
/*     */   {
/* 110 */     return this._mapType.getContentType();
/*     */   }
/*     */   
/*     */   public JsonDeserializer<Object> getContentDeserializer()
/*     */   {
/* 115 */     return this._valueDeserializer;
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
/*     */   public EnumMap<?, ?> deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 129 */     if (p.getCurrentToken() != JsonToken.START_OBJECT) {
/* 130 */       return (EnumMap)_deserializeFromEmpty(p, ctxt);
/*     */     }
/* 132 */     EnumMap result = constructMap();
/* 133 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 134 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*     */     
/* 136 */     while (p.nextToken() == JsonToken.FIELD_NAME) {
/* 137 */       String keyName = p.getCurrentName();
/*     */       
/* 139 */       Enum<?> key = (Enum)this._keyDeserializer.deserializeKey(keyName, ctxt);
/* 140 */       if (key == null) {
/* 141 */         if (!ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
/* 142 */           return (EnumMap)ctxt.handleWeirdStringValue(this._enumClass, keyName, "value not one of declared Enum instance names for %s", new Object[] { this._mapType.getKeyType() });
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 149 */         p.nextToken();
/* 150 */         p.skipChildren();
/*     */       }
/*     */       else
/*     */       {
/* 154 */         JsonToken t = p.nextToken();
/*     */         
/*     */         Object value;
/*     */         
/*     */         try
/*     */         {
/*     */           Object value;
/* 161 */           if (t == JsonToken.VALUE_NULL) {
/* 162 */             value = valueDes.getNullValue(ctxt); } else { Object value;
/* 163 */             if (typeDeser == null) {
/* 164 */               value = valueDes.deserialize(p, ctxt);
/*     */             } else
/* 166 */               value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */           }
/*     */         } catch (Exception e) {
/* 169 */           wrapAndThrow(e, result, keyName);
/* 170 */           return null;
/*     */         }
/* 172 */         result.put(key, value);
/*     */       } }
/* 174 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object deserializeWithType(JsonParser jp, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 182 */     return typeDeserializer.deserializeTypedFromObject(jp, ctxt);
/*     */   }
/*     */   
/*     */   protected EnumMap<?, ?> constructMap() {
/* 186 */     return new EnumMap(this._enumClass);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\EnumMapDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */