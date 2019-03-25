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
/*     */ public class AsPropertyTypeSerializer
/*     */   extends AsArrayTypeSerializer
/*     */ {
/*     */   protected final String _typePropertyName;
/*     */   
/*     */   public AsPropertyTypeSerializer(TypeIdResolver idRes, BeanProperty property, String propName)
/*     */   {
/*  25 */     super(idRes, property);
/*  26 */     this._typePropertyName = propName;
/*     */   }
/*     */   
/*     */   public AsPropertyTypeSerializer forProperty(BeanProperty prop)
/*     */   {
/*  31 */     return this._property == prop ? this : new AsPropertyTypeSerializer(this._idResolver, prop, this._typePropertyName);
/*     */   }
/*     */   
/*     */   public String getPropertyName() {
/*  35 */     return this._typePropertyName;
/*     */   }
/*     */   
/*  38 */   public JsonTypeInfo.As getTypeInclusion() { return JsonTypeInfo.As.PROPERTY; }
/*     */   
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator jgen)
/*     */     throws IOException
/*     */   {
/*  43 */     String typeId = idFromValue(value);
/*  44 */     if (typeId == null) {
/*  45 */       jgen.writeStartObject();
/*  46 */     } else if (jgen.canWriteTypeId()) {
/*  47 */       jgen.writeTypeId(typeId);
/*  48 */       jgen.writeStartObject();
/*     */     } else {
/*  50 */       jgen.writeStartObject();
/*  51 */       jgen.writeStringField(this._typePropertyName, typeId);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTypePrefixForObject(Object value, JsonGenerator jgen, Class<?> type)
/*     */     throws IOException
/*     */   {
/*  58 */     String typeId = idFromValueAndType(value, type);
/*  59 */     if (typeId == null) {
/*  60 */       jgen.writeStartObject();
/*  61 */     } else if (jgen.canWriteTypeId()) {
/*  62 */       jgen.writeTypeId(typeId);
/*  63 */       jgen.writeStartObject();
/*     */     } else {
/*  65 */       jgen.writeStartObject();
/*  66 */       jgen.writeStringField(this._typePropertyName, typeId);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeTypeSuffixForObject(Object value, JsonGenerator jgen)
/*     */     throws IOException
/*     */   {
/*  78 */     jgen.writeEndObject();
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
/*     */   public void writeCustomTypePrefixForObject(Object value, JsonGenerator jgen, String typeId)
/*     */     throws IOException
/*     */   {
/*  96 */     if (typeId == null) {
/*  97 */       jgen.writeStartObject();
/*  98 */     } else if (jgen.canWriteTypeId()) {
/*  99 */       jgen.writeTypeId(typeId);
/* 100 */       jgen.writeStartObject();
/*     */     } else {
/* 102 */       jgen.writeStartObject();
/* 103 */       jgen.writeStringField(this._typePropertyName, typeId);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeCustomTypeSuffixForObject(Object value, JsonGenerator jgen, String typeId) throws IOException
/*     */   {
/* 109 */     jgen.writeEndObject();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsPropertyTypeSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */