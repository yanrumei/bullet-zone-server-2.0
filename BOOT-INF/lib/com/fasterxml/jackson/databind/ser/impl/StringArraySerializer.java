/*     */ package com.fasterxml.jackson.databind.ser.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Feature;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.std.ArraySerializerBase;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class StringArraySerializer
/*     */   extends ArraySerializerBase<String[]>
/*     */   implements ContextualSerializer
/*     */ {
/*  32 */   private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(String.class);
/*     */   
/*  34 */   public static final StringArraySerializer instance = new StringArraySerializer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonSerializer<Object> _elementSerializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StringArraySerializer()
/*     */   {
/*  49 */     super(String[].class);
/*  50 */     this._elementSerializer = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public StringArraySerializer(StringArraySerializer src, BeanProperty prop, JsonSerializer<?> ser, Boolean unwrapSingle)
/*     */   {
/*  56 */     super(src, prop, unwrapSingle);
/*  57 */     this._elementSerializer = ser;
/*     */   }
/*     */   
/*     */   public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle)
/*     */   {
/*  62 */     return new StringArraySerializer(this, prop, this._elementSerializer, unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/*  71 */     return this;
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
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  89 */     JsonSerializer<?> ser = null;
/*     */     
/*     */ 
/*  92 */     if (property != null) {
/*  93 */       AnnotationIntrospector ai = provider.getAnnotationIntrospector();
/*  94 */       AnnotatedMember m = property.getMember();
/*  95 */       if (m != null) {
/*  96 */         Object serDef = ai.findContentSerializer(m);
/*  97 */         if (serDef != null) {
/*  98 */           ser = provider.serializerInstance(m, serDef);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 104 */     Boolean unwrapSingle = findFormatFeature(provider, property, String[].class, JsonFormat.Feature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED);
/*     */     
/* 106 */     if (ser == null) {
/* 107 */       ser = this._elementSerializer;
/*     */     }
/*     */     
/* 110 */     ser = findConvertingContentSerializer(provider, property, ser);
/* 111 */     if (ser == null) {
/* 112 */       ser = provider.findValueSerializer(String.class, property);
/*     */     } else {
/* 114 */       ser = provider.handleSecondaryContextualization(ser, property);
/*     */     }
/*     */     
/* 117 */     if (isDefaultSerializer(ser)) {
/* 118 */       ser = null;
/*     */     }
/*     */     
/* 121 */     if ((ser == this._elementSerializer) && (unwrapSingle == this._unwrapSingle)) {
/* 122 */       return this;
/*     */     }
/* 124 */     return new StringArraySerializer(this, property, ser, unwrapSingle);
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
/* 135 */     return VALUE_TYPE;
/*     */   }
/*     */   
/*     */   public JsonSerializer<?> getContentSerializer()
/*     */   {
/* 140 */     return this._elementSerializer;
/*     */   }
/*     */   
/*     */   public boolean isEmpty(SerializerProvider prov, String[] value)
/*     */   {
/* 145 */     return (value == null) || (value.length == 0);
/*     */   }
/*     */   
/*     */   public boolean hasSingleElement(String[] value)
/*     */   {
/* 150 */     return value.length == 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void serialize(String[] value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 163 */     int len = value.length;
/* 164 */     if ((len == 1) && (
/* 165 */       ((this._unwrapSingle == null) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) || (this._unwrapSingle == Boolean.TRUE)))
/*     */     {
/*     */ 
/* 168 */       serializeContents(value, gen, provider);
/* 169 */       return;
/*     */     }
/*     */     
/* 172 */     gen.writeStartArray(len);
/* 173 */     serializeContents(value, gen, provider);
/* 174 */     gen.writeEndArray();
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeContents(String[] value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 181 */     int len = value.length;
/* 182 */     if (len == 0) {
/* 183 */       return;
/*     */     }
/* 185 */     if (this._elementSerializer != null) {
/* 186 */       serializeContentsSlow(value, gen, provider, this._elementSerializer);
/* 187 */       return;
/*     */     }
/* 189 */     for (int i = 0; i < len; i++) {
/* 190 */       String str = value[i];
/* 191 */       if (str == null) {
/* 192 */         gen.writeNull();
/*     */       } else {
/* 194 */         gen.writeString(value[i]);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void serializeContentsSlow(String[] value, JsonGenerator gen, SerializerProvider provider, JsonSerializer<Object> ser)
/*     */     throws IOException
/*     */   {
/* 202 */     int i = 0; for (int len = value.length; i < len; i++) {
/* 203 */       String str = value[i];
/* 204 */       if (str == null) {
/* 205 */         provider.defaultSerializeNull(gen);
/*     */       } else {
/* 207 */         ser.serialize(value[i], gen, provider);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */   {
/* 214 */     return createSchemaNode("array", true).set("items", createSchemaNode("string"));
/*     */   }
/*     */   
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 220 */     visitArrayFormat(visitor, typeHint, JsonFormatTypes.STRING);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\impl\StringArraySerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */