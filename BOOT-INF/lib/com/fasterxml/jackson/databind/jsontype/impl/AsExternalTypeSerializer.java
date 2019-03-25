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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AsExternalTypeSerializer
/*     */   extends TypeSerializerBase
/*     */ {
/*     */   protected final String _typePropertyName;
/*     */   
/*     */   public AsExternalTypeSerializer(TypeIdResolver idRes, BeanProperty property, String propName)
/*     */   {
/*  29 */     super(idRes, property);
/*  30 */     this._typePropertyName = propName;
/*     */   }
/*     */   
/*     */   public AsExternalTypeSerializer forProperty(BeanProperty prop)
/*     */   {
/*  35 */     return this._property == prop ? this : new AsExternalTypeSerializer(this._idResolver, prop, this._typePropertyName);
/*     */   }
/*     */   
/*     */   public String getPropertyName() {
/*  39 */     return this._typePropertyName;
/*     */   }
/*     */   
/*  42 */   public JsonTypeInfo.As getTypeInclusion() { return JsonTypeInfo.As.EXTERNAL_PROPERTY; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator gen)
/*     */     throws IOException
/*     */   {
/*  52 */     _writeObjectPrefix(value, gen);
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator gen, Class<?> type) throws IOException
/*     */   {
/*  57 */     _writeObjectPrefix(value, gen);
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForArray(Object value, JsonGenerator gen) throws IOException
/*     */   {
/*  62 */     _writeArrayPrefix(value, gen);
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForArray(Object value, JsonGenerator gen, Class<?> type) throws IOException
/*     */   {
/*  67 */     _writeArrayPrefix(value, gen);
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForScalar(Object value, JsonGenerator gen) throws IOException
/*     */   {
/*  72 */     _writeScalarPrefix(value, gen);
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForScalar(Object value, JsonGenerator gen, Class<?> type) throws IOException
/*     */   {
/*  77 */     _writeScalarPrefix(value, gen);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeTypeSuffixForObject(Object value, JsonGenerator gen)
/*     */     throws IOException
/*     */   {
/*  88 */     _writeObjectSuffix(value, gen, idFromValue(value));
/*     */   }
/*     */   
/*     */   public void writeTypeSuffixForArray(Object value, JsonGenerator gen) throws IOException
/*     */   {
/*  93 */     _writeArraySuffix(value, gen, idFromValue(value));
/*     */   }
/*     */   
/*     */   public void writeTypeSuffixForScalar(Object value, JsonGenerator gen) throws IOException
/*     */   {
/*  98 */     _writeScalarSuffix(value, gen, idFromValue(value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeCustomTypePrefixForScalar(Object value, JsonGenerator gen, String typeId)
/*     */     throws IOException
/*     */   {
/* 109 */     _writeScalarPrefix(value, gen);
/*     */   }
/*     */   
/*     */   public void writeCustomTypePrefixForObject(Object value, JsonGenerator gen, String typeId) throws IOException
/*     */   {
/* 114 */     _writeObjectPrefix(value, gen);
/*     */   }
/*     */   
/*     */   public void writeCustomTypePrefixForArray(Object value, JsonGenerator gen, String typeId) throws IOException
/*     */   {
/* 119 */     _writeArrayPrefix(value, gen);
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForScalar(Object value, JsonGenerator gen, String typeId) throws IOException
/*     */   {
/* 124 */     _writeScalarSuffix(value, gen, typeId);
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForObject(Object value, JsonGenerator gen, String typeId) throws IOException
/*     */   {
/* 129 */     _writeObjectSuffix(value, gen, typeId);
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForArray(Object value, JsonGenerator gen, String typeId) throws IOException
/*     */   {
/* 134 */     _writeArraySuffix(value, gen, typeId);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected final void _writeScalarPrefix(Object value, JsonGenerator gen)
/*     */     throws IOException
/*     */   {}
/*     */   
/*     */ 
/*     */   protected final void _writeObjectPrefix(Object value, JsonGenerator gen)
/*     */     throws IOException
/*     */   {
/* 147 */     gen.writeStartObject();
/*     */   }
/*     */   
/*     */   protected final void _writeArrayPrefix(Object value, JsonGenerator gen) throws IOException {
/* 151 */     gen.writeStartArray();
/*     */   }
/*     */   
/*     */   protected final void _writeScalarSuffix(Object value, JsonGenerator gen, String typeId) throws IOException {
/* 155 */     if (typeId != null) {
/* 156 */       gen.writeStringField(this._typePropertyName, typeId);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void _writeObjectSuffix(Object value, JsonGenerator gen, String typeId) throws IOException {
/* 161 */     gen.writeEndObject();
/* 162 */     if (typeId != null) {
/* 163 */       gen.writeStringField(this._typePropertyName, typeId);
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void _writeArraySuffix(Object value, JsonGenerator gen, String typeId) throws IOException {
/* 168 */     gen.writeEndArray();
/* 169 */     if (typeId != null) {
/* 170 */       gen.writeStringField(this._typePropertyName, typeId);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsExternalTypeSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */