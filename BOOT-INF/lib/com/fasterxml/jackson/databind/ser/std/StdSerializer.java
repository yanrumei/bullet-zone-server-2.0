/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Feature;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonArrayFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*     */ import com.fasterxml.jackson.databind.ser.PropertyFilter;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Type;
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class StdSerializer<T>
/*     */   extends JsonSerializer<T>
/*     */   implements JsonFormatVisitable, SchemaAware, Serializable
/*     */ {
/*  43 */   private static final Object CONVERTING_CONTENT_CONVERTER_LOCK = new Object();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Class<T> _handledType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StdSerializer(Class<T> t)
/*     */   {
/*  60 */     this._handledType = t;
/*     */   }
/*     */   
/*     */   protected StdSerializer(JavaType type)
/*     */   {
/*  65 */     this._handledType = type.getRawClass();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StdSerializer(Class<?> t, boolean dummy)
/*     */   {
/*  74 */     this._handledType = t;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StdSerializer(StdSerializer<?> src)
/*     */   {
/*  82 */     this._handledType = src._handledType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<T> handledType()
/*     */   {
/*  92 */     return this._handledType;
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
/*     */   public abstract void serialize(T paramT, JsonGenerator paramJsonGenerator, SerializerProvider paramSerializerProvider)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 118 */     visitor.expectAnyFormat(typeHint);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 128 */     return createSchemaNode("string");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint, boolean isOptional)
/*     */     throws JsonMappingException
/*     */   {
/* 139 */     ObjectNode schema = (ObjectNode)getSchema(provider, typeHint);
/* 140 */     if (!isOptional) {
/* 141 */       schema.put("required", !isOptional);
/*     */     }
/* 143 */     return schema;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ObjectNode createObjectNode()
/*     */   {
/* 153 */     return JsonNodeFactory.instance.objectNode();
/*     */   }
/*     */   
/*     */   protected ObjectNode createSchemaNode(String type)
/*     */   {
/* 158 */     ObjectNode schema = createObjectNode();
/* 159 */     schema.put("type", type);
/* 160 */     return schema;
/*     */   }
/*     */   
/*     */   protected ObjectNode createSchemaNode(String type, boolean isOptional)
/*     */   {
/* 165 */     ObjectNode schema = createSchemaNode(type);
/* 166 */     if (!isOptional) {
/* 167 */       schema.put("required", !isOptional);
/*     */     }
/* 169 */     return schema;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void visitStringFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 180 */     if (visitor != null) {
/* 181 */       visitor.expectStringFormat(typeHint);
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
/*     */   protected void visitStringFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonValueFormat format)
/*     */     throws JsonMappingException
/*     */   {
/* 196 */     if (visitor != null) {
/* 197 */       JsonStringFormatVisitor v2 = visitor.expectStringFormat(typeHint);
/* 198 */       if (v2 != null) {
/* 199 */         v2.format(format);
/*     */       }
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
/*     */   protected void visitIntFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonParser.NumberType numberType)
/*     */     throws JsonMappingException
/*     */   {
/* 214 */     if (visitor != null) {
/* 215 */       JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(typeHint);
/* 216 */       if ((v2 != null) && 
/* 217 */         (numberType != null)) {
/* 218 */         v2.numberType(numberType);
/*     */       }
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
/*     */   protected void visitIntFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonParser.NumberType numberType, JsonValueFormat format)
/*     */     throws JsonMappingException
/*     */   {
/* 235 */     if (visitor != null) {
/* 236 */       JsonIntegerFormatVisitor v2 = visitor.expectIntegerFormat(typeHint);
/* 237 */       if (v2 != null) {
/* 238 */         if (numberType != null) {
/* 239 */           v2.numberType(numberType);
/*     */         }
/* 241 */         if (format != null) {
/* 242 */           v2.format(format);
/*     */         }
/*     */       }
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
/*     */   protected void visitFloatFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonParser.NumberType numberType)
/*     */     throws JsonMappingException
/*     */   {
/* 258 */     if (visitor != null) {
/* 259 */       JsonNumberFormatVisitor v2 = visitor.expectNumberFormat(typeHint);
/* 260 */       if (v2 != null) {
/* 261 */         v2.numberType(numberType);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void visitArrayFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonSerializer<?> itemSerializer, JavaType itemType)
/*     */     throws JsonMappingException
/*     */   {
/* 273 */     if (visitor != null) {
/* 274 */       JsonArrayFormatVisitor v2 = visitor.expectArrayFormat(typeHint);
/* 275 */       if ((v2 != null) && 
/* 276 */         (itemSerializer != null)) {
/* 277 */         v2.itemsFormat(itemSerializer, itemType);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void visitArrayFormat(JsonFormatVisitorWrapper visitor, JavaType typeHint, JsonFormatTypes itemType)
/*     */     throws JsonMappingException
/*     */   {
/* 290 */     if (visitor != null) {
/* 291 */       JsonArrayFormatVisitor v2 = visitor.expectArrayFormat(typeHint);
/* 292 */       if (v2 != null) {
/* 293 */         v2.itemsFormat(itemType);
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void wrapAndThrow(SerializerProvider provider, Throwable t, Object bean, String fieldName)
/*     */     throws IOException
/*     */   {
/* 324 */     while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 325 */       t = t.getCause();
/*     */     }
/*     */     
/* 328 */     if ((t instanceof Error)) {
/* 329 */       throw ((Error)t);
/*     */     }
/*     */     
/* 332 */     boolean wrap = (provider == null) || (provider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS));
/* 333 */     if ((t instanceof IOException)) {
/* 334 */       if ((!wrap) || (!(t instanceof JsonMappingException))) {
/* 335 */         throw ((IOException)t);
/*     */       }
/* 337 */     } else if ((!wrap) && 
/* 338 */       ((t instanceof RuntimeException))) {
/* 339 */       throw ((RuntimeException)t);
/*     */     }
/*     */     
/*     */ 
/* 343 */     throw JsonMappingException.wrapWithPath(t, bean, fieldName);
/*     */   }
/*     */   
/*     */ 
/*     */   public void wrapAndThrow(SerializerProvider provider, Throwable t, Object bean, int index)
/*     */     throws IOException
/*     */   {
/* 350 */     while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 351 */       t = t.getCause();
/*     */     }
/*     */     
/* 354 */     if ((t instanceof Error)) {
/* 355 */       throw ((Error)t);
/*     */     }
/*     */     
/* 358 */     boolean wrap = (provider == null) || (provider.isEnabled(SerializationFeature.WRAP_EXCEPTIONS));
/* 359 */     if ((t instanceof IOException)) {
/* 360 */       if ((!wrap) || (!(t instanceof JsonMappingException))) {
/* 361 */         throw ((IOException)t);
/*     */       }
/* 363 */     } else if ((!wrap) && 
/* 364 */       ((t instanceof RuntimeException))) {
/* 365 */       throw ((RuntimeException)t);
/*     */     }
/*     */     
/*     */ 
/* 369 */     throw JsonMappingException.wrapWithPath(t, bean, index);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonSerializer<?> findConvertingContentSerializer(SerializerProvider provider, BeanProperty prop, JsonSerializer<?> existingSerializer)
/*     */     throws JsonMappingException
/*     */   {
/* 396 */     Object ob = provider.getAttribute(CONVERTING_CONTENT_CONVERTER_LOCK);
/* 397 */     if ((ob != null) && 
/* 398 */       (ob == Boolean.TRUE)) {
/* 399 */       return existingSerializer;
/*     */     }
/*     */     
/*     */ 
/* 403 */     AnnotationIntrospector intr = provider.getAnnotationIntrospector();
/* 404 */     if ((intr != null) && (prop != null)) {
/* 405 */       AnnotatedMember m = prop.getMember();
/* 406 */       if (m != null) {
/* 407 */         provider.setAttribute(CONVERTING_CONTENT_CONVERTER_LOCK, Boolean.TRUE);
/*     */         Object convDef;
/*     */         try {
/* 410 */           convDef = intr.findSerializationContentConverter(m);
/*     */         } finally {
/* 412 */           provider.setAttribute(CONVERTING_CONTENT_CONVERTER_LOCK, null);
/*     */         }
/* 414 */         if (convDef != null) {
/* 415 */           Object conv = provider.converterInstance(prop.getMember(), convDef);
/* 416 */           JavaType delegateType = ((Converter)conv).getOutputType(provider.getTypeFactory());
/*     */           
/* 418 */           if ((existingSerializer == null) && (!delegateType.isJavaLangObject())) {
/* 419 */             existingSerializer = provider.findValueSerializer(delegateType);
/*     */           }
/* 421 */           return new StdDelegatingSerializer((Converter)conv, delegateType, existingSerializer);
/*     */         }
/*     */       }
/*     */     }
/* 425 */     return existingSerializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PropertyFilter findPropertyFilter(SerializerProvider provider, Object filterId, Object valueToFilter)
/*     */     throws JsonMappingException
/*     */   {
/* 438 */     FilterProvider filters = provider.getFilterProvider();
/*     */     
/* 440 */     if (filters == null) {
/* 441 */       throw JsonMappingException.from(provider, "Can not resolve PropertyFilter with id '" + filterId + "'; no FilterProvider configured");
/*     */     }
/*     */     
/*     */ 
/* 445 */     return filters.findPropertyFilter(filterId, valueToFilter);
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
/*     */   protected JsonFormat.Value findFormatOverrides(SerializerProvider provider, BeanProperty prop, Class<?> typeForDefaults)
/*     */   {
/* 460 */     if (prop != null) {
/* 461 */       return prop.findPropertyFormat(provider.getConfig(), typeForDefaults);
/*     */     }
/*     */     
/* 464 */     return provider.getDefaultPropertyFormat(typeForDefaults);
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
/*     */   protected Boolean findFormatFeature(SerializerProvider provider, BeanProperty prop, Class<?> typeForDefaults, JsonFormat.Feature feat)
/*     */   {
/* 479 */     JsonFormat.Value format = findFormatOverrides(provider, prop, typeForDefaults);
/* 480 */     if (format != null) {
/* 481 */       return format.getFeature(feat);
/*     */     }
/* 483 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonInclude.Value findIncludeOverrides(SerializerProvider provider, BeanProperty prop, Class<?> typeForDefaults)
/*     */   {
/* 492 */     if (prop != null) {
/* 493 */       return prop.findPropertyInclusion(provider.getConfig(), typeForDefaults);
/*     */     }
/*     */     
/* 496 */     return provider.getDefaultPropertyInclusion(typeForDefaults);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonSerializer<?> findAnnotatedContentSerializer(SerializerProvider serializers, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 508 */     if (property != null)
/*     */     {
/* 510 */       AnnotatedMember m = property.getMember();
/* 511 */       AnnotationIntrospector intr = serializers.getAnnotationIntrospector();
/* 512 */       if (m != null) {
/* 513 */         Object serDef = intr.findContentSerializer(m);
/* 514 */         if (serDef != null) {
/* 515 */           return serializers.serializerInstance(m, serDef);
/*     */         }
/*     */       }
/*     */     }
/* 519 */     return null;
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
/*     */   protected boolean isDefaultSerializer(JsonSerializer<?> serializer)
/*     */   {
/* 535 */     return ClassUtil.isJacksonStdImpl(serializer);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\StdSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */