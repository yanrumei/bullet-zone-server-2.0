/*     */ package com.fasterxml.jackson.databind.jsontype.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import java.io.IOException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsWrapperTypeSerializer
/*     */   extends TypeSerializerBase
/*     */ {
/*     */   public AsWrapperTypeSerializer(TypeIdResolver idRes, BeanProperty property)
/*     */   {
/*  22 */     super(idRes, property);
/*     */   }
/*     */   
/*     */   public AsWrapperTypeSerializer forProperty(BeanProperty prop)
/*     */   {
/*  27 */     return this._property == prop ? this : new AsWrapperTypeSerializer(this._idResolver, prop);
/*     */   }
/*     */   
/*     */   public JsonTypeInfo.As getTypeInclusion() {
/*  31 */     return JsonTypeInfo.As.WRAPPER_OBJECT;
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator g) throws IOException
/*     */   {
/*  36 */     String typeId = idFromValue(value);
/*  37 */     if (g.canWriteTypeId()) {
/*  38 */       if (typeId != null) {
/*  39 */         g.writeTypeId(typeId);
/*     */       }
/*  41 */       g.writeStartObject();
/*     */     }
/*     */     else {
/*  44 */       g.writeStartObject();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  49 */       g.writeObjectFieldStart(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator g, Class<?> type)
/*     */     throws IOException
/*     */   {
/*  56 */     String typeId = idFromValueAndType(value, type);
/*  57 */     if (g.canWriteTypeId()) {
/*  58 */       if (typeId != null) {
/*  59 */         g.writeTypeId(typeId);
/*     */       }
/*  61 */       g.writeStartObject();
/*     */     }
/*     */     else {
/*  64 */       g.writeStartObject();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*  69 */       g.writeObjectFieldStart(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForArray(Object value, JsonGenerator g)
/*     */     throws IOException
/*     */   {
/*  76 */     String typeId = idFromValue(value);
/*  77 */     if (g.canWriteTypeId()) {
/*  78 */       if (typeId != null) {
/*  79 */         g.writeTypeId(typeId);
/*     */       }
/*  81 */       g.writeStartArray();
/*     */     }
/*     */     else {
/*  84 */       g.writeStartObject();
/*  85 */       g.writeArrayFieldStart(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForArray(Object value, JsonGenerator g, Class<?> type)
/*     */     throws IOException
/*     */   {
/*  92 */     String typeId = idFromValueAndType(value, type);
/*  93 */     if (g.canWriteTypeId()) {
/*  94 */       if (typeId != null) {
/*  95 */         g.writeTypeId(typeId);
/*     */       }
/*  97 */       g.writeStartArray();
/*     */     }
/*     */     else {
/* 100 */       g.writeStartObject();
/*     */       
/* 102 */       g.writeArrayFieldStart(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForScalar(Object value, JsonGenerator g) throws IOException
/*     */   {
/* 108 */     String typeId = idFromValue(value);
/* 109 */     if (g.canWriteTypeId()) {
/* 110 */       if (typeId != null) {
/* 111 */         g.writeTypeId(typeId);
/*     */       }
/*     */     }
/*     */     else {
/* 115 */       g.writeStartObject();
/* 116 */       g.writeFieldName(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForScalar(Object value, JsonGenerator g, Class<?> type)
/*     */     throws IOException
/*     */   {
/* 123 */     String typeId = idFromValueAndType(value, type);
/* 124 */     if (g.canWriteTypeId()) {
/* 125 */       if (typeId != null) {
/* 126 */         g.writeTypeId(typeId);
/*     */       }
/*     */     }
/*     */     else {
/* 130 */       g.writeStartObject();
/* 131 */       g.writeFieldName(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeTypeSuffixForObject(Object value, JsonGenerator g)
/*     */     throws IOException
/*     */   {
/* 139 */     g.writeEndObject();
/* 140 */     if (!g.canWriteTypeId())
/*     */     {
/* 142 */       g.writeEndObject();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void writeTypeSuffixForArray(Object value, JsonGenerator g)
/*     */     throws IOException
/*     */   {
/* 150 */     g.writeEndArray();
/* 151 */     if (!g.canWriteTypeId())
/*     */     {
/* 153 */       g.writeEndObject();
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypeSuffixForScalar(Object value, JsonGenerator g) throws IOException
/*     */   {
/* 159 */     if (!g.canWriteTypeId())
/*     */     {
/* 161 */       g.writeEndObject();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeCustomTypePrefixForObject(Object value, JsonGenerator g, String typeId)
/*     */     throws IOException
/*     */   {
/* 173 */     if (g.canWriteTypeId()) {
/* 174 */       if (typeId != null) {
/* 175 */         g.writeTypeId(typeId);
/*     */       }
/* 177 */       g.writeStartObject();
/*     */     } else {
/* 179 */       g.writeStartObject();
/* 180 */       g.writeObjectFieldStart(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeCustomTypePrefixForArray(Object value, JsonGenerator g, String typeId) throws IOException
/*     */   {
/* 186 */     if (g.canWriteTypeId()) {
/* 187 */       if (typeId != null) {
/* 188 */         g.writeTypeId(typeId);
/*     */       }
/* 190 */       g.writeStartArray();
/*     */     } else {
/* 192 */       g.writeStartObject();
/* 193 */       g.writeArrayFieldStart(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeCustomTypePrefixForScalar(Object value, JsonGenerator g, String typeId) throws IOException
/*     */   {
/* 199 */     if (g.canWriteTypeId()) {
/* 200 */       if (typeId != null) {
/* 201 */         g.writeTypeId(typeId);
/*     */       }
/*     */     } else {
/* 204 */       g.writeStartObject();
/* 205 */       g.writeFieldName(_validTypeId(typeId));
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForObject(Object value, JsonGenerator g, String typeId) throws IOException
/*     */   {
/* 211 */     if (!g.canWriteTypeId()) {
/* 212 */       writeTypeSuffixForObject(value, g);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForArray(Object value, JsonGenerator g, String typeId) throws IOException
/*     */   {
/* 218 */     if (!g.canWriteTypeId()) {
/* 219 */       writeTypeSuffixForArray(value, g);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForScalar(Object value, JsonGenerator g, String typeId) throws IOException
/*     */   {
/* 225 */     if (!g.canWriteTypeId()) {
/* 226 */       writeTypeSuffixForScalar(value, g);
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
/*     */ 
/*     */   protected String _validTypeId(String typeId)
/*     */   {
/* 243 */     return typeId == null ? "" : typeId;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsWrapperTypeSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */