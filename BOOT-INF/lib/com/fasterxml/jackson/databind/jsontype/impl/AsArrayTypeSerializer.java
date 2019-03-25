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
/*     */ public class AsArrayTypeSerializer
/*     */   extends TypeSerializerBase
/*     */ {
/*     */   public AsArrayTypeSerializer(TypeIdResolver idRes, BeanProperty property)
/*     */   {
/*  18 */     super(idRes, property);
/*     */   }
/*     */   
/*     */   public AsArrayTypeSerializer forProperty(BeanProperty prop)
/*     */   {
/*  23 */     return this._property == prop ? this : new AsArrayTypeSerializer(this._idResolver, prop);
/*     */   }
/*     */   
/*     */   public JsonTypeInfo.As getTypeInclusion() {
/*  27 */     return JsonTypeInfo.As.WRAPPER_ARRAY;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator g)
/*     */     throws IOException
/*     */   {
/*  37 */     String typeId = idFromValue(value);
/*     */     
/*  39 */     if (g.canWriteTypeId()) {
/*  40 */       if (typeId != null) {
/*  41 */         g.writeTypeId(typeId);
/*     */       }
/*     */     } else {
/*  44 */       g.writeStartArray();
/*  45 */       g.writeString(typeId);
/*     */     }
/*  47 */     g.writeStartObject();
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator g, Class<?> type) throws IOException
/*     */   {
/*  52 */     String typeId = idFromValueAndType(value, type);
/*     */     
/*  54 */     if (g.canWriteTypeId()) {
/*  55 */       if (typeId != null) {
/*  56 */         g.writeTypeId(typeId);
/*     */       }
/*     */     } else {
/*  59 */       g.writeStartArray();
/*  60 */       g.writeString(typeId);
/*     */     }
/*  62 */     g.writeStartObject();
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForArray(Object value, JsonGenerator g) throws IOException
/*     */   {
/*  67 */     String typeId = idFromValue(value);
/*  68 */     if (g.canWriteTypeId()) {
/*  69 */       if (typeId != null) {
/*  70 */         g.writeTypeId(typeId);
/*     */       }
/*     */     } else {
/*  73 */       g.writeStartArray();
/*  74 */       g.writeString(typeId);
/*     */     }
/*  76 */     g.writeStartArray();
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForArray(Object value, JsonGenerator g, Class<?> type) throws IOException
/*     */   {
/*  81 */     String typeId = idFromValueAndType(value, type);
/*  82 */     if (g.canWriteTypeId()) {
/*  83 */       if (typeId != null) {
/*  84 */         g.writeTypeId(typeId);
/*     */       }
/*     */     } else {
/*  87 */       g.writeStartArray();
/*  88 */       g.writeString(typeId);
/*     */     }
/*  90 */     g.writeStartArray();
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForScalar(Object value, JsonGenerator g) throws IOException
/*     */   {
/*  95 */     String typeId = idFromValue(value);
/*  96 */     if (g.canWriteTypeId()) {
/*  97 */       if (typeId != null) {
/*  98 */         g.writeTypeId(typeId);
/*     */       }
/*     */     }
/*     */     else {
/* 102 */       g.writeStartArray();
/* 103 */       g.writeString(typeId);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForScalar(Object value, JsonGenerator g, Class<?> type) throws IOException
/*     */   {
/* 109 */     String typeId = idFromValueAndType(value, type);
/* 110 */     if (g.canWriteTypeId()) {
/* 111 */       if (typeId != null) {
/* 112 */         g.writeTypeId(typeId);
/*     */       }
/*     */     }
/*     */     else {
/* 116 */       g.writeStartArray();
/* 117 */       g.writeString(typeId);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeTypeSuffixForObject(Object value, JsonGenerator g)
/*     */     throws IOException
/*     */   {
/* 129 */     g.writeEndObject();
/* 130 */     if (!g.canWriteTypeId()) {
/* 131 */       g.writeEndArray();
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypeSuffixForArray(Object value, JsonGenerator g)
/*     */     throws IOException
/*     */   {
/* 138 */     g.writeEndArray();
/* 139 */     if (!g.canWriteTypeId()) {
/* 140 */       g.writeEndArray();
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypeSuffixForScalar(Object value, JsonGenerator g) throws IOException
/*     */   {
/* 146 */     if (!g.canWriteTypeId())
/*     */     {
/* 148 */       g.writeEndArray();
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
/* 160 */     if (g.canWriteTypeId()) {
/* 161 */       if (typeId != null) {
/* 162 */         g.writeTypeId(typeId);
/*     */       }
/*     */     } else {
/* 165 */       g.writeStartArray();
/* 166 */       g.writeString(typeId);
/*     */     }
/* 168 */     g.writeStartObject();
/*     */   }
/*     */   
/*     */   public void writeCustomTypePrefixForArray(Object value, JsonGenerator g, String typeId) throws IOException
/*     */   {
/* 173 */     if (g.canWriteTypeId()) {
/* 174 */       if (typeId != null) {
/* 175 */         g.writeTypeId(typeId);
/*     */       }
/*     */     } else {
/* 178 */       g.writeStartArray();
/* 179 */       g.writeString(typeId);
/*     */     }
/* 181 */     g.writeStartArray();
/*     */   }
/*     */   
/*     */   public void writeCustomTypePrefixForScalar(Object value, JsonGenerator g, String typeId) throws IOException
/*     */   {
/* 186 */     if (g.canWriteTypeId()) {
/* 187 */       if (typeId != null) {
/* 188 */         g.writeTypeId(typeId);
/*     */       }
/*     */     } else {
/* 191 */       g.writeStartArray();
/* 192 */       g.writeString(typeId);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForObject(Object value, JsonGenerator g, String typeId) throws IOException
/*     */   {
/* 198 */     if (!g.canWriteTypeId()) {
/* 199 */       writeTypeSuffixForObject(value, g);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForArray(Object value, JsonGenerator g, String typeId) throws IOException
/*     */   {
/* 205 */     if (!g.canWriteTypeId()) {
/* 206 */       writeTypeSuffixForArray(value, g);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForScalar(Object value, JsonGenerator g, String typeId) throws IOException
/*     */   {
/* 212 */     if (!g.canWriteTypeId()) {
/* 213 */       writeTypeSuffixForScalar(value, g);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsArrayTypeSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */