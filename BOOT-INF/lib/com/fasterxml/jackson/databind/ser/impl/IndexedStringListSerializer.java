/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
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
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public final class IndexedStringListSerializer
/*     */   extends StaticListSerializerBase<List<String>>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  27 */   public static final IndexedStringListSerializer instance = new IndexedStringListSerializer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected IndexedStringListSerializer()
/*     */   {
/*  36 */     super(List.class);
/*     */   }
/*     */   
/*     */   public IndexedStringListSerializer(IndexedStringListSerializer src, JsonSerializer<?> ser, Boolean unwrapSingle)
/*     */   {
/*  41 */     super(src, ser, unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonSerializer<?> _withResolved(BeanProperty prop, JsonSerializer<?> ser, Boolean unwrapSingle)
/*     */   {
/*  47 */     return new IndexedStringListSerializer(this, ser, unwrapSingle);
/*     */   }
/*     */   
/*  50 */   protected JsonNode contentSchema() { return createSchemaNode("string", true); }
/*     */   
/*     */   protected void acceptContentVisitor(JsonArrayFormatVisitor visitor) throws JsonMappingException
/*     */   {
/*  54 */     visitor.itemsFormat(JsonFormatTypes.STRING);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serialize(List<String> value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/*  67 */     int len = value.size();
/*  68 */     if ((len == 1) && (
/*  69 */       ((this._unwrapSingle == null) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) || (this._unwrapSingle == Boolean.TRUE)))
/*     */     {
/*     */ 
/*  72 */       _serializeUnwrapped(value, gen, provider);
/*  73 */       return;
/*     */     }
/*     */     
/*     */ 
/*  77 */     gen.writeStartArray(len);
/*  78 */     if (this._serializer == null) {
/*  79 */       serializeContents(value, gen, provider, len);
/*     */     } else {
/*  81 */       serializeUsingCustom(value, gen, provider, len);
/*     */     }
/*  83 */     gen.writeEndArray();
/*     */   }
/*     */   
/*     */   private final void _serializeUnwrapped(List<String> value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/*  89 */     if (this._serializer == null) {
/*  90 */       serializeContents(value, gen, provider, 1);
/*     */     } else {
/*  92 */       serializeUsingCustom(value, gen, provider, 1);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void serializeWithType(List<String> value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 101 */     int len = value.size();
/* 102 */     typeSer.writeTypePrefixForArray(value, gen);
/* 103 */     if (this._serializer == null) {
/* 104 */       serializeContents(value, gen, provider, len);
/*     */     } else {
/* 106 */       serializeUsingCustom(value, gen, provider, len);
/*     */     }
/* 108 */     typeSer.writeTypeSuffixForArray(value, gen);
/*     */   }
/*     */   
/*     */   private final void serializeContents(List<String> value, JsonGenerator gen, SerializerProvider provider, int len)
/*     */     throws IOException
/*     */   {
/* 114 */     int i = 0;
/*     */     try {
/* 116 */       for (; i < len; i++) {
/* 117 */         String str = (String)value.get(i);
/* 118 */         if (str == null) {
/* 119 */           provider.defaultSerializeNull(gen);
/*     */         } else {
/* 121 */           gen.writeString(str);
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 125 */       wrapAndThrow(provider, e, value, i);
/*     */     }
/*     */   }
/*     */   
/*     */   private final void serializeUsingCustom(List<String> value, JsonGenerator gen, SerializerProvider provider, int len)
/*     */     throws IOException
/*     */   {
/* 132 */     int i = 0;
/*     */     try {
/* 134 */       JsonSerializer<String> ser = this._serializer;
/* 135 */       for (i = 0; i < len; i++) {
/* 136 */         String str = (String)value.get(i);
/* 137 */         if (str == null) {
/* 138 */           provider.defaultSerializeNull(gen);
/*     */         } else {
/* 140 */           ser.serialize(str, gen, provider);
/*     */         }
/*     */       }
/*     */     } catch (Exception e) {
/* 144 */       wrapAndThrow(provider, e, value, i);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\impl\IndexedStringListSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */