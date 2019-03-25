/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.util.AbstractMap.SimpleEntry;
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
/*     */ @JacksonStdImpl
/*     */ public class MapEntryDeserializer
/*     */   extends ContainerDeserializerBase<Map.Entry<Object, Object>>
/*     */   implements ContextualDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final JavaType _type;
/*     */   protected final KeyDeserializer _keyDeserializer;
/*     */   protected final JsonDeserializer<Object> _valueDeserializer;
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   
/*     */   public MapEntryDeserializer(JavaType type, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser)
/*     */   {
/*  60 */     super(type);
/*  61 */     if (type.containedTypeCount() != 2) {
/*  62 */       throw new IllegalArgumentException("Missing generic type information for " + type);
/*     */     }
/*  64 */     this._type = type;
/*  65 */     this._keyDeserializer = keyDeser;
/*  66 */     this._valueDeserializer = valueDeser;
/*  67 */     this._valueTypeDeserializer = valueTypeDeser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapEntryDeserializer(MapEntryDeserializer src)
/*     */   {
/*  76 */     super(src._type);
/*  77 */     this._type = src._type;
/*  78 */     this._keyDeserializer = src._keyDeserializer;
/*  79 */     this._valueDeserializer = src._valueDeserializer;
/*  80 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected MapEntryDeserializer(MapEntryDeserializer src, KeyDeserializer keyDeser, JsonDeserializer<Object> valueDeser, TypeDeserializer valueTypeDeser)
/*     */   {
/*  87 */     super(src._type);
/*  88 */     this._type = src._type;
/*  89 */     this._keyDeserializer = keyDeser;
/*  90 */     this._valueDeserializer = valueDeser;
/*  91 */     this._valueTypeDeserializer = valueTypeDeser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapEntryDeserializer withResolved(KeyDeserializer keyDeser, TypeDeserializer valueTypeDeser, JsonDeserializer<?> valueDeser)
/*     */   {
/* 103 */     if ((this._keyDeserializer == keyDeser) && (this._valueDeserializer == valueDeser) && (this._valueTypeDeserializer == valueTypeDeser))
/*     */     {
/* 105 */       return this;
/*     */     }
/* 107 */     return new MapEntryDeserializer(this, keyDeser, valueDeser, valueTypeDeser);
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
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 125 */     KeyDeserializer kd = this._keyDeserializer;
/* 126 */     if (kd == null) {
/* 127 */       kd = ctxt.findKeyDeserializer(this._type.containedType(0), property);
/*     */     }
/* 129 */     else if ((kd instanceof ContextualKeyDeserializer)) {
/* 130 */       kd = ((ContextualKeyDeserializer)kd).createContextual(ctxt, property);
/*     */     }
/*     */     
/* 133 */     JsonDeserializer<?> vd = this._valueDeserializer;
/* 134 */     vd = findConvertingContentDeserializer(ctxt, property, vd);
/* 135 */     JavaType contentType = this._type.containedType(1);
/* 136 */     if (vd == null) {
/* 137 */       vd = ctxt.findContextualValueDeserializer(contentType, property);
/*     */     } else {
/* 139 */       vd = ctxt.handleSecondaryContextualization(vd, property, contentType);
/*     */     }
/* 141 */     TypeDeserializer vtd = this._valueTypeDeserializer;
/* 142 */     if (vtd != null) {
/* 143 */       vtd = vtd.forProperty(property);
/*     */     }
/* 145 */     return withResolved(kd, vtd, vd);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType getContentType()
/*     */   {
/* 156 */     return this._type.containedType(1);
/*     */   }
/*     */   
/*     */   public JsonDeserializer<Object> getContentDeserializer()
/*     */   {
/* 161 */     return this._valueDeserializer;
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
/*     */   public Map.Entry<Object, Object> deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 175 */     JsonToken t = p.getCurrentToken();
/* 176 */     if ((t != JsonToken.START_OBJECT) && (t != JsonToken.FIELD_NAME) && (t != JsonToken.END_OBJECT))
/*     */     {
/*     */ 
/* 179 */       return (Map.Entry)_deserializeFromEmpty(p, ctxt);
/*     */     }
/* 181 */     if (t == JsonToken.START_OBJECT) {
/* 182 */       t = p.nextToken();
/*     */     }
/* 184 */     if (t != JsonToken.FIELD_NAME) {
/* 185 */       if (t == JsonToken.END_OBJECT) {
/* 186 */         ctxt.reportMappingException("Can not deserialize a Map.Entry out of empty JSON Object", new Object[0]);
/* 187 */         return null;
/*     */       }
/* 189 */       return (Map.Entry)ctxt.handleUnexpectedToken(handledType(), p);
/*     */     }
/*     */     
/* 192 */     KeyDeserializer keyDes = this._keyDeserializer;
/* 193 */     JsonDeserializer<Object> valueDes = this._valueDeserializer;
/* 194 */     TypeDeserializer typeDeser = this._valueTypeDeserializer;
/*     */     
/* 196 */     String keyStr = p.getCurrentName();
/* 197 */     Object key = keyDes.deserializeKey(keyStr, ctxt);
/* 198 */     Object value = null;
/*     */     
/* 200 */     t = p.nextToken();
/*     */     try
/*     */     {
/* 203 */       if (t == JsonToken.VALUE_NULL) {
/* 204 */         value = valueDes.getNullValue(ctxt);
/* 205 */       } else if (typeDeser == null) {
/* 206 */         value = valueDes.deserialize(p, ctxt);
/*     */       } else {
/* 208 */         value = valueDes.deserializeWithType(p, ctxt, typeDeser);
/*     */       }
/*     */     } catch (Exception e) {
/* 211 */       wrapAndThrow(e, Map.Entry.class, keyStr);
/*     */     }
/*     */     
/*     */ 
/* 215 */     t = p.nextToken();
/* 216 */     if (t != JsonToken.END_OBJECT) {
/* 217 */       if (t == JsonToken.FIELD_NAME) {
/* 218 */         ctxt.reportMappingException("Problem binding JSON into Map.Entry: more than one entry in JSON (second field: '" + p.getCurrentName() + "')", new Object[0]);
/*     */       }
/*     */       else {
/* 221 */         ctxt.reportMappingException("Problem binding JSON into Map.Entry: unexpected content after JSON Object entry: " + t, new Object[0]);
/*     */       }
/* 223 */       return null;
/*     */     }
/* 225 */     return new AbstractMap.SimpleEntry(key, value);
/*     */   }
/*     */   
/*     */ 
/*     */   public Map.Entry<Object, Object> deserialize(JsonParser p, DeserializationContext ctxt, Map.Entry<Object, Object> result)
/*     */     throws IOException
/*     */   {
/* 232 */     throw new IllegalStateException("Can not update Map.Entry values");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 241 */     return typeDeserializer.deserializeTypedFromObject(p, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType getValueType()
/*     */   {
/* 250 */     return this._type;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\MapEntryDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */