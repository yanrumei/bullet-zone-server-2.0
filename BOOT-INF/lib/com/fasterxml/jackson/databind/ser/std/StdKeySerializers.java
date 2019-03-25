/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap.SerializerAndMapResult;
/*     */ import com.fasterxml.jackson.databind.util.EnumValues;
/*     */ import java.io.IOException;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ 
/*     */ public class StdKeySerializers
/*     */ {
/*  17 */   protected static final JsonSerializer<Object> DEFAULT_KEY_SERIALIZER = new StdKeySerializer();
/*     */   
/*  19 */   protected static final JsonSerializer<Object> DEFAULT_STRING_SERIALIZER = new StringKeySerializer();
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
/*     */   public static JsonSerializer<Object> getStdKeySerializer(SerializationConfig config, Class<?> rawKeyType, boolean useDefault)
/*     */   {
/*  39 */     if ((rawKeyType == null) || (rawKeyType == Object.class)) {
/*  40 */       return new Dynamic();
/*     */     }
/*  42 */     if (rawKeyType == String.class) {
/*  43 */       return DEFAULT_STRING_SERIALIZER;
/*     */     }
/*  45 */     if ((rawKeyType.isPrimitive()) || (Number.class.isAssignableFrom(rawKeyType)))
/*     */     {
/*     */ 
/*  48 */       return new Default(5, rawKeyType);
/*     */     }
/*  50 */     if (rawKeyType == Class.class) {
/*  51 */       return new Default(3, rawKeyType);
/*     */     }
/*  53 */     if (Date.class.isAssignableFrom(rawKeyType)) {
/*  54 */       return new Default(1, rawKeyType);
/*     */     }
/*  56 */     if (Calendar.class.isAssignableFrom(rawKeyType)) {
/*  57 */       return new Default(2, rawKeyType);
/*     */     }
/*     */     
/*  60 */     if (rawKeyType == java.util.UUID.class) {
/*  61 */       return new Default(5, rawKeyType);
/*     */     }
/*  63 */     if (useDefault) {
/*  64 */       return DEFAULT_KEY_SERIALIZER;
/*     */     }
/*  66 */     return null;
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
/*     */   public static JsonSerializer<Object> getFallbackKeySerializer(SerializationConfig config, Class<?> rawKeyType)
/*     */   {
/*  79 */     if (rawKeyType != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */       if (rawKeyType == Enum.class) {
/*  87 */         return new Dynamic();
/*     */       }
/*  89 */       if (rawKeyType.isEnum()) {
/*  90 */         return EnumKeySerializer.construct(rawKeyType, EnumValues.constructFromName(config, rawKeyType));
/*     */       }
/*     */     }
/*     */     
/*  94 */     return DEFAULT_KEY_SERIALIZER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static JsonSerializer<Object> getDefault()
/*     */   {
/* 102 */     return DEFAULT_KEY_SERIALIZER;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class Default
/*     */     extends StdSerializer<Object>
/*     */   {
/*     */     static final int TYPE_DATE = 1;
/*     */     
/*     */ 
/*     */     static final int TYPE_CALENDAR = 2;
/*     */     
/*     */ 
/*     */     static final int TYPE_CLASS = 3;
/*     */     
/*     */ 
/*     */     static final int TYPE_ENUM = 4;
/*     */     
/*     */ 
/*     */     static final int TYPE_TO_STRING = 5;
/*     */     
/*     */     protected final int _typeId;
/*     */     
/*     */ 
/*     */     public Default(int typeId, Class<?> type)
/*     */     {
/* 129 */       super(false);
/* 130 */       this._typeId = typeId;
/*     */     }
/*     */     
/*     */     public void serialize(Object value, JsonGenerator g, SerializerProvider provider) throws IOException
/*     */     {
/* 135 */       switch (this._typeId) {
/*     */       case 1: 
/* 137 */         provider.defaultSerializeDateKey((Date)value, g);
/* 138 */         break;
/*     */       case 2: 
/* 140 */         provider.defaultSerializeDateKey(((Calendar)value).getTimeInMillis(), g);
/* 141 */         break;
/*     */       case 3: 
/* 143 */         g.writeFieldName(((Class)value).getName());
/* 144 */         break;
/*     */       case 4: 
/*     */         String key;
/*     */         
/*     */         String key;
/* 149 */         if (provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)) {
/* 150 */           key = value.toString();
/*     */         } else {
/* 152 */           Enum<?> e = (Enum)value;
/* 153 */           String key; if (provider.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)) {
/* 154 */             key = String.valueOf(e.ordinal());
/*     */           } else {
/* 156 */             key = e.name();
/*     */           }
/*     */         }
/* 159 */         g.writeFieldName(key);
/*     */         
/* 161 */         break;
/*     */       case 5: 
/*     */       default: 
/* 164 */         g.writeFieldName(value.toString());
/*     */       }
/*     */       
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Dynamic
/*     */     extends StdSerializer<Object>
/*     */   {
/*     */     protected transient PropertySerializerMap _dynamicSerializers;
/*     */     
/*     */ 
/*     */     public Dynamic()
/*     */     {
/* 179 */       super(false);
/* 180 */       this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
/*     */     }
/*     */     
/*     */     Object readResolve()
/*     */     {
/* 185 */       this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
/* 186 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     public void serialize(Object value, JsonGenerator g, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 193 */       Class<?> cls = value.getClass();
/* 194 */       PropertySerializerMap m = this._dynamicSerializers;
/* 195 */       JsonSerializer<Object> ser = m.serializerFor(cls);
/* 196 */       if (ser == null) {
/* 197 */         ser = _findAndAddDynamic(m, cls, provider);
/*     */       }
/* 199 */       ser.serialize(value, g, provider);
/*     */     }
/*     */     
/*     */     public void acceptJsonFormatVisitor(com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper visitor, com.fasterxml.jackson.databind.JavaType typeHint) throws com.fasterxml.jackson.databind.JsonMappingException
/*     */     {
/* 204 */       visitStringFormat(visitor, typeHint);
/*     */     }
/*     */     
/*     */ 
/*     */     protected JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider)
/*     */       throws com.fasterxml.jackson.databind.JsonMappingException
/*     */     {
/* 211 */       if (type == Object.class)
/*     */       {
/* 213 */         JsonSerializer<Object> ser = new StdKeySerializers.Default(5, type);
/* 214 */         this._dynamicSerializers = map.newWith(type, ser);
/* 215 */         return ser;
/*     */       }
/* 217 */       PropertySerializerMap.SerializerAndMapResult result = map.findAndAddKeySerializer(type, provider, null);
/*     */       
/*     */ 
/*     */ 
/* 221 */       if (map != result.map) {
/* 222 */         this._dynamicSerializers = result.map;
/*     */       }
/* 224 */       return result.serializer;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class StringKeySerializer
/*     */     extends StdSerializer<Object>
/*     */   {
/*     */     public StringKeySerializer()
/*     */     {
/* 233 */       super(false);
/*     */     }
/*     */     
/*     */     public void serialize(Object value, JsonGenerator g, SerializerProvider provider) throws IOException {
/* 237 */       g.writeFieldName((String)value);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static class EnumKeySerializer
/*     */     extends StdSerializer<Object>
/*     */   {
/*     */     protected final EnumValues _values;
/*     */     
/*     */ 
/*     */     protected EnumKeySerializer(Class<?> enumType, EnumValues values)
/*     */     {
/* 251 */       super(false);
/* 252 */       this._values = values;
/*     */     }
/*     */     
/*     */ 
/*     */     public static EnumKeySerializer construct(Class<?> enumType, EnumValues enumValues)
/*     */     {
/* 258 */       return new EnumKeySerializer(enumType, enumValues);
/*     */     }
/*     */     
/*     */ 
/*     */     public void serialize(Object value, JsonGenerator g, SerializerProvider serializers)
/*     */       throws IOException
/*     */     {
/* 265 */       if (serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_TO_STRING)) {
/* 266 */         g.writeFieldName(value.toString());
/* 267 */         return;
/*     */       }
/* 269 */       Enum<?> en = (Enum)value;
/* 270 */       if (serializers.isEnabled(SerializationFeature.WRITE_ENUMS_USING_INDEX)) {
/* 271 */         g.writeFieldName(String.valueOf(en.ordinal()));
/* 272 */         return;
/*     */       }
/* 274 */       g.writeFieldName(this._values.serializedValueFor(en));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\StdKeySerializers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */