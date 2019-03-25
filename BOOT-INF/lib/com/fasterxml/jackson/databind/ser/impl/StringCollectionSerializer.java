/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.StaticListSerializerBase;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class StringCollectionSerializer
/*     */   extends StaticListSerializerBase<Collection<String>>
/*     */ {
/*  26 */   public static final StringCollectionSerializer instance = new StringCollectionSerializer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StringCollectionSerializer()
/*     */   {
/*  35 */     super(Collection.class);
/*     */   }
/*     */   
/*     */ 
/*     */   protected StringCollectionSerializer(StringCollectionSerializer src, JsonSerializer<?> ser, Boolean unwrapSingle)
/*     */   {
/*  41 */     super(src, ser, unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonSerializer<?> _withResolved(BeanProperty prop, JsonSerializer<?> ser, Boolean unwrapSingle)
/*     */   {
/*  47 */     return new StringCollectionSerializer(this, ser, unwrapSingle);
/*     */   }
/*     */   
/*     */   protected JsonNode contentSchema() {
/*  51 */     return createSchemaNode("string", true);
/*     */   }
/*     */   
/*     */   protected void acceptContentVisitor(JsonArrayFormatVisitor visitor)
/*     */     throws JsonMappingException
/*     */   {
/*  57 */     visitor.itemsFormat(JsonFormatTypes.STRING);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serialize(Collection<String> value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/*  70 */     int len = value.size();
/*  71 */     if ((len == 1) && (
/*  72 */       ((this._unwrapSingle == null) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) || (this._unwrapSingle == Boolean.TRUE)))
/*     */     {
/*     */ 
/*  75 */       _serializeUnwrapped(value, gen, provider);
/*  76 */       return;
/*     */     }
/*     */     
/*  79 */     gen.writeStartArray(len);
/*  80 */     if (this._serializer == null) {
/*  81 */       serializeContents(value, gen, provider);
/*     */     } else {
/*  83 */       serializeUsingCustom(value, gen, provider);
/*     */     }
/*  85 */     gen.writeEndArray();
/*     */   }
/*     */   
/*     */   private final void _serializeUnwrapped(Collection<String> value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/*  91 */     if (this._serializer == null) {
/*  92 */       serializeContents(value, gen, provider);
/*     */     } else {
/*  94 */       serializeUsingCustom(value, gen, provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void serializeWithType(Collection<String> value, JsonGenerator jgen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 103 */     typeSer.writeTypePrefixForArray(value, jgen);
/* 104 */     if (this._serializer == null) {
/* 105 */       serializeContents(value, jgen, provider);
/*     */     } else {
/* 107 */       serializeUsingCustom(value, jgen, provider);
/*     */     }
/* 109 */     typeSer.writeTypeSuffixForArray(value, jgen);
/*     */   }
/*     */   
/*     */   private final void serializeContents(Collection<String> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 115 */     if (this._serializer != null) {
/* 116 */       serializeUsingCustom(value, jgen, provider);
/* 117 */       return;
/*     */     }
/* 119 */     int i = 0;
/* 120 */     for (String str : value) {
/*     */       try {
/* 122 */         if (str == null) {
/* 123 */           provider.defaultSerializeNull(jgen);
/*     */         } else {
/* 125 */           jgen.writeString(str);
/*     */         }
/* 127 */         i++;
/*     */       } catch (Exception e) {
/* 129 */         wrapAndThrow(provider, e, value, i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void serializeUsingCustom(Collection<String> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 137 */     JsonSerializer<String> ser = this._serializer;
/* 138 */     int i = 0;
/* 139 */     for (String str : value) {
/*     */       try {
/* 141 */         if (str == null) {
/* 142 */           provider.defaultSerializeNull(jgen);
/*     */         } else {
/* 144 */           ser.serialize(str, jgen, provider);
/*     */         }
/*     */       } catch (Exception e) {
/* 147 */         wrapAndThrow(provider, e, value, i);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\impl\StringCollectionSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */