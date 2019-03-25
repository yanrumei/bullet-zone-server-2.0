/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
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
/*     */ public class StdDelegatingSerializer
/*     */   extends StdSerializer<Object>
/*     */   implements ContextualSerializer, ResolvableSerializer, JsonFormatVisitable, SchemaAware
/*     */ {
/*     */   protected final Converter<Object, ?> _converter;
/*     */   protected final JavaType _delegateType;
/*     */   protected final JsonSerializer<Object> _delegateSerializer;
/*     */   
/*     */   public StdDelegatingSerializer(Converter<?, ?> converter)
/*     */   {
/*  54 */     super(Object.class);
/*  55 */     this._converter = converter;
/*  56 */     this._delegateType = null;
/*  57 */     this._delegateSerializer = null;
/*     */   }
/*     */   
/*     */ 
/*     */   public <T> StdDelegatingSerializer(Class<T> cls, Converter<T, ?> converter)
/*     */   {
/*  63 */     super(cls, false);
/*  64 */     this._converter = converter;
/*  65 */     this._delegateType = null;
/*  66 */     this._delegateSerializer = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public StdDelegatingSerializer(Converter<Object, ?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer)
/*     */   {
/*  73 */     super(delegateType);
/*  74 */     this._converter = converter;
/*  75 */     this._delegateType = delegateType;
/*  76 */     this._delegateSerializer = delegateSerializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StdDelegatingSerializer withDelegate(Converter<Object, ?> converter, JavaType delegateType, JsonSerializer<?> delegateSerializer)
/*     */   {
/*  86 */     if (getClass() != StdDelegatingSerializer.class) {
/*  87 */       throw new IllegalStateException("Sub-class " + getClass().getName() + " must override 'withDelegate'");
/*     */     }
/*  89 */     return new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void resolve(SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 101 */     if ((this._delegateSerializer != null) && ((this._delegateSerializer instanceof ResolvableSerializer)))
/*     */     {
/* 103 */       ((ResolvableSerializer)this._delegateSerializer).resolve(provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonSerializer<?> createContextual(SerializerProvider provider, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 111 */     JsonSerializer<?> delSer = this._delegateSerializer;
/* 112 */     JavaType delegateType = this._delegateType;
/*     */     
/* 114 */     if (delSer == null)
/*     */     {
/* 116 */       if (delegateType == null) {
/* 117 */         delegateType = this._converter.getOutputType(provider.getTypeFactory());
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 122 */       if (!delegateType.isJavaLangObject()) {
/* 123 */         delSer = provider.findValueSerializer(delegateType);
/*     */       }
/*     */     }
/* 126 */     if ((delSer instanceof ContextualSerializer)) {
/* 127 */       delSer = provider.handleSecondaryContextualization(delSer, property);
/*     */     }
/* 129 */     if ((delSer == this._delegateSerializer) && (delegateType == this._delegateType)) {
/* 130 */       return this;
/*     */     }
/* 132 */     return withDelegate(this._converter, delegateType, delSer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Converter<Object, ?> getConverter()
/*     */   {
/* 142 */     return this._converter;
/*     */   }
/*     */   
/*     */   public JsonSerializer<?> getDelegatee()
/*     */   {
/* 147 */     return this._delegateSerializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 159 */     Object delegateValue = convertValue(value);
/*     */     
/* 161 */     if (delegateValue == null) {
/* 162 */       provider.defaultSerializeNull(gen);
/* 163 */       return;
/*     */     }
/*     */     
/* 166 */     JsonSerializer<Object> ser = this._delegateSerializer;
/* 167 */     if (ser == null) {
/* 168 */       ser = _findSerializer(delegateValue, provider);
/*     */     }
/* 170 */     ser.serialize(delegateValue, gen, provider);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */     throws IOException
/*     */   {
/* 180 */     Object delegateValue = convertValue(value);
/* 181 */     JsonSerializer<Object> ser = this._delegateSerializer;
/* 182 */     if (ser == null) {
/* 183 */       ser = _findSerializer(value, provider);
/*     */     }
/* 185 */     ser.serializeWithType(delegateValue, gen, provider, typeSer);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public boolean isEmpty(Object value)
/*     */   {
/* 191 */     return isEmpty(null, value);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEmpty(SerializerProvider prov, Object value)
/*     */   {
/* 197 */     Object delegateValue = convertValue(value);
/* 198 */     if (this._delegateSerializer == null) {
/* 199 */       return value == null;
/*     */     }
/* 201 */     return this._delegateSerializer.isEmpty(prov, delegateValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 214 */     if ((this._delegateSerializer instanceof SchemaAware)) {
/* 215 */       return ((SchemaAware)this._delegateSerializer).getSchema(provider, typeHint);
/*     */     }
/* 217 */     return super.getSchema(provider, typeHint);
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional)
/*     */     throws JsonMappingException
/*     */   {
/* 224 */     if ((this._delegateSerializer instanceof SchemaAware)) {
/* 225 */       return ((SchemaAware)this._delegateSerializer).getSchema(provider, typeHint, isOptional);
/*     */     }
/* 227 */     return super.getSchema(provider, typeHint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 238 */     if (this._delegateSerializer != null) {
/* 239 */       this._delegateSerializer.acceptJsonFormatVisitor(visitor, typeHint);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object convertValue(Object value)
/*     */   {
/* 261 */     return this._converter.convert(value);
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
/*     */   protected JsonSerializer<Object> _findSerializer(Object value, SerializerProvider serializers)
/*     */     throws JsonMappingException
/*     */   {
/* 276 */     return serializers.findValueSerializer(value.getClass());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\StdDelegatingSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */