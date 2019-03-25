/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonStringFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Set;
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
/*     */ @JacksonStdImpl
/*     */ public class JsonValueSerializer
/*     */   extends StdSerializer<Object>
/*     */   implements ContextualSerializer, JsonFormatVisitable, SchemaAware
/*     */ {
/*     */   protected final AnnotatedMethod _accessorMethod;
/*     */   protected final JsonSerializer<Object> _valueSerializer;
/*     */   protected final BeanProperty _property;
/*     */   protected final boolean _forceTypeInformation;
/*     */   
/*     */   public JsonValueSerializer(AnnotatedMethod valueMethod, JsonSerializer<?> ser)
/*     */   {
/*  76 */     super(valueMethod.getType());
/*  77 */     this._accessorMethod = valueMethod;
/*  78 */     this._valueSerializer = ser;
/*  79 */     this._property = null;
/*  80 */     this._forceTypeInformation = true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonValueSerializer(JsonValueSerializer src, BeanProperty property, JsonSerializer<?> ser, boolean forceTypeInfo)
/*     */   {
/*  87 */     super(_notNullClass(src.handledType()));
/*  88 */     this._accessorMethod = src._accessorMethod;
/*  89 */     this._valueSerializer = ser;
/*  90 */     this._property = property;
/*  91 */     this._forceTypeInformation = forceTypeInfo;
/*     */   }
/*     */   
/*     */   private static final Class<Object> _notNullClass(Class<?> cls)
/*     */   {
/*  96 */     return cls == null ? Object.class : cls;
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonValueSerializer withResolved(BeanProperty property, JsonSerializer<?> ser, boolean forceTypeInfo)
/*     */   {
/* 102 */     if ((this._property == property) && (this._valueSerializer == ser) && (forceTypeInfo == this._forceTypeInformation))
/*     */     {
/* 104 */       return this;
/*     */     }
/* 106 */     return new JsonValueSerializer(this, property, ser, forceTypeInfo);
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
/* 124 */     JsonSerializer<?> ser = this._valueSerializer;
/* 125 */     if (ser == null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 130 */       JavaType t = this._accessorMethod.getType();
/* 131 */       if ((provider.isEnabled(MapperFeature.USE_STATIC_TYPING)) || (t.isFinal()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 138 */         ser = provider.findPrimaryPropertySerializer(t, property);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 143 */         boolean forceTypeInformation = isNaturalTypeWithStdHandling(t.getRawClass(), ser);
/* 144 */         return withResolved(property, ser, forceTypeInformation);
/*     */       }
/*     */     }
/*     */     else {
/* 148 */       ser = provider.handlePrimaryContextualization(ser, property);
/* 149 */       return withResolved(property, ser, this._forceTypeInformation);
/*     */     }
/* 151 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void serialize(Object bean, JsonGenerator gen, SerializerProvider prov)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 164 */       Object value = this._accessorMethod.getValue(bean);
/* 165 */       if (value == null) {
/* 166 */         prov.defaultSerializeNull(gen);
/* 167 */         return;
/*     */       }
/* 169 */       JsonSerializer<Object> ser = this._valueSerializer;
/* 170 */       if (ser == null) {
/* 171 */         Class<?> c = value.getClass();
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 177 */         ser = prov.findTypedValueSerializer(c, true, this._property);
/*     */       }
/* 179 */       ser.serialize(value, gen, prov);
/*     */     } catch (IOException ioe) {
/* 181 */       throw ioe;
/*     */     } catch (Exception e) {
/* 183 */       Throwable t = e;
/*     */       
/* 185 */       while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 186 */         t = t.getCause();
/*     */       }
/*     */       
/* 189 */       if ((t instanceof Error)) {
/* 190 */         throw ((Error)t);
/*     */       }
/*     */       
/* 193 */       throw JsonMappingException.wrapWithPath(t, bean, this._accessorMethod.getName() + "()");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void serializeWithType(Object bean, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer0)
/*     */     throws IOException
/*     */   {
/* 202 */     Object value = null;
/*     */     try {
/* 204 */       value = this._accessorMethod.getValue(bean);
/*     */       
/* 206 */       if (value == null) {
/* 207 */         provider.defaultSerializeNull(gen);
/* 208 */         return;
/*     */       }
/* 210 */       JsonSerializer<Object> ser = this._valueSerializer;
/* 211 */       if (ser == null)
/*     */       {
/* 213 */         ser = provider.findValueSerializer(value.getClass(), this._property);
/*     */ 
/*     */ 
/*     */ 
/*     */       }
/* 218 */       else if (this._forceTypeInformation) {
/* 219 */         typeSer0.writeTypePrefixForScalar(bean, gen);
/* 220 */         ser.serialize(value, gen, provider);
/* 221 */         typeSer0.writeTypeSuffixForScalar(bean, gen);
/* 222 */         return;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 228 */       TypeSerializerRerouter rr = new TypeSerializerRerouter(typeSer0, bean);
/* 229 */       ser.serializeWithType(value, gen, provider, rr);
/*     */     } catch (IOException ioe) {
/* 231 */       throw ioe;
/*     */     } catch (Exception e) {
/* 233 */       Throwable t = e;
/*     */       
/* 235 */       while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 236 */         t = t.getCause();
/*     */       }
/*     */       
/* 239 */       if ((t instanceof Error)) {
/* 240 */         throw ((Error)t);
/*     */       }
/*     */       
/* 243 */       throw JsonMappingException.wrapWithPath(t, bean, this._accessorMethod.getName() + "()");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 252 */     if ((this._valueSerializer instanceof SchemaAware)) {
/* 253 */       return ((SchemaAware)this._valueSerializer).getSchema(provider, null);
/*     */     }
/* 255 */     return JsonSchema.getDefaultSchemaNode();
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
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 271 */     JavaType type = this._accessorMethod.getType();
/* 272 */     Class<?> declaring = this._accessorMethod.getDeclaringClass();
/* 273 */     if ((declaring != null) && (declaring.isEnum()) && 
/* 274 */       (_acceptJsonFormatVisitorForEnum(visitor, typeHint, declaring))) {
/* 275 */       return;
/*     */     }
/*     */     
/* 278 */     JsonSerializer<Object> ser = this._valueSerializer;
/* 279 */     if (ser == null) {
/* 280 */       ser = visitor.getProvider().findTypedValueSerializer(type, false, this._property);
/* 281 */       if (ser == null) {
/* 282 */         visitor.expectAnyFormat(typeHint);
/* 283 */         return;
/*     */       }
/*     */     }
/* 286 */     ser.acceptJsonFormatVisitor(visitor, null);
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
/*     */   protected boolean _acceptJsonFormatVisitorForEnum(JsonFormatVisitorWrapper visitor, JavaType typeHint, Class<?> enumType)
/*     */     throws JsonMappingException
/*     */   {
/* 303 */     JsonStringFormatVisitor stringVisitor = visitor.expectStringFormat(typeHint);
/* 304 */     if (stringVisitor != null) {
/* 305 */       Set<String> enums = new LinkedHashSet();
/* 306 */       for (Object en : enumType.getEnumConstants())
/*     */       {
/*     */         try
/*     */         {
/*     */ 
/* 311 */           enums.add(String.valueOf(this._accessorMethod.callOn(en)));
/*     */         } catch (Exception e) {
/* 313 */           Throwable t = e;
/* 314 */           while (((t instanceof InvocationTargetException)) && (t.getCause() != null)) {
/* 315 */             t = t.getCause();
/*     */           }
/* 317 */           if ((t instanceof Error)) {
/* 318 */             throw ((Error)t);
/*     */           }
/* 320 */           throw JsonMappingException.wrapWithPath(t, en, this._accessorMethod.getName() + "()");
/*     */         }
/*     */       }
/* 323 */       stringVisitor.enumTypes(enums);
/*     */     }
/* 325 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */   protected boolean isNaturalTypeWithStdHandling(Class<?> rawType, JsonSerializer<?> ser)
/*     */   {
/* 331 */     if (rawType.isPrimitive()) {
/* 332 */       if ((rawType != Integer.TYPE) && (rawType != Boolean.TYPE) && (rawType != Double.TYPE)) {
/* 333 */         return false;
/*     */       }
/*     */     }
/* 336 */     else if ((rawType != String.class) && (rawType != Integer.class) && (rawType != Boolean.class) && (rawType != Double.class))
/*     */     {
/* 338 */       return false;
/*     */     }
/*     */     
/* 341 */     return isDefaultSerializer(ser);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String toString()
/*     */   {
/* 352 */     return "(@JsonValue serializer for method " + this._accessorMethod.getDeclaringClass() + "#" + this._accessorMethod.getName() + ")";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static class TypeSerializerRerouter
/*     */     extends TypeSerializer
/*     */   {
/*     */     protected final TypeSerializer _typeSerializer;
/*     */     
/*     */ 
/*     */ 
/*     */     protected final Object _forObject;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public TypeSerializerRerouter(TypeSerializer ts, Object ob)
/*     */     {
/* 373 */       this._typeSerializer = ts;
/* 374 */       this._forObject = ob;
/*     */     }
/*     */     
/*     */     public TypeSerializer forProperty(BeanProperty prop)
/*     */     {
/* 379 */       throw new UnsupportedOperationException();
/*     */     }
/*     */     
/*     */     public JsonTypeInfo.As getTypeInclusion()
/*     */     {
/* 384 */       return this._typeSerializer.getTypeInclusion();
/*     */     }
/*     */     
/*     */     public String getPropertyName()
/*     */     {
/* 389 */       return this._typeSerializer.getPropertyName();
/*     */     }
/*     */     
/*     */     public TypeIdResolver getTypeIdResolver()
/*     */     {
/* 394 */       return this._typeSerializer.getTypeIdResolver();
/*     */     }
/*     */     
/*     */     public void writeTypePrefixForScalar(Object value, JsonGenerator gen) throws IOException
/*     */     {
/* 399 */       this._typeSerializer.writeTypePrefixForScalar(this._forObject, gen);
/*     */     }
/*     */     
/*     */     public void writeTypePrefixForObject(Object value, JsonGenerator gen) throws IOException
/*     */     {
/* 404 */       this._typeSerializer.writeTypePrefixForObject(this._forObject, gen);
/*     */     }
/*     */     
/*     */     public void writeTypePrefixForArray(Object value, JsonGenerator gen) throws IOException
/*     */     {
/* 409 */       this._typeSerializer.writeTypePrefixForArray(this._forObject, gen);
/*     */     }
/*     */     
/*     */     public void writeTypeSuffixForScalar(Object value, JsonGenerator gen) throws IOException
/*     */     {
/* 414 */       this._typeSerializer.writeTypeSuffixForScalar(this._forObject, gen);
/*     */     }
/*     */     
/*     */     public void writeTypeSuffixForObject(Object value, JsonGenerator gen) throws IOException
/*     */     {
/* 419 */       this._typeSerializer.writeTypeSuffixForObject(this._forObject, gen);
/*     */     }
/*     */     
/*     */     public void writeTypeSuffixForArray(Object value, JsonGenerator gen) throws IOException
/*     */     {
/* 424 */       this._typeSerializer.writeTypeSuffixForArray(this._forObject, gen);
/*     */     }
/*     */     
/*     */     public void writeTypePrefixForScalar(Object value, JsonGenerator gen, Class<?> type) throws IOException
/*     */     {
/* 429 */       this._typeSerializer.writeTypePrefixForScalar(this._forObject, gen, type);
/*     */     }
/*     */     
/*     */     public void writeTypePrefixForObject(Object value, JsonGenerator gen, Class<?> type) throws IOException
/*     */     {
/* 434 */       this._typeSerializer.writeTypePrefixForObject(this._forObject, gen, type);
/*     */     }
/*     */     
/*     */     public void writeTypePrefixForArray(Object value, JsonGenerator gen, Class<?> type) throws IOException
/*     */     {
/* 439 */       this._typeSerializer.writeTypePrefixForArray(this._forObject, gen, type);
/*     */     }
/*     */     
/*     */     public void writeCustomTypePrefixForScalar(Object value, JsonGenerator gen, String typeId)
/*     */       throws IOException
/*     */     {
/* 445 */       this._typeSerializer.writeCustomTypePrefixForScalar(this._forObject, gen, typeId);
/*     */     }
/*     */     
/*     */     public void writeCustomTypePrefixForObject(Object value, JsonGenerator gen, String typeId) throws IOException
/*     */     {
/* 450 */       this._typeSerializer.writeCustomTypePrefixForObject(this._forObject, gen, typeId);
/*     */     }
/*     */     
/*     */     public void writeCustomTypePrefixForArray(Object value, JsonGenerator gen, String typeId) throws IOException
/*     */     {
/* 455 */       this._typeSerializer.writeCustomTypePrefixForArray(this._forObject, gen, typeId);
/*     */     }
/*     */     
/*     */     public void writeCustomTypeSuffixForScalar(Object value, JsonGenerator gen, String typeId) throws IOException
/*     */     {
/* 460 */       this._typeSerializer.writeCustomTypeSuffixForScalar(this._forObject, gen, typeId);
/*     */     }
/*     */     
/*     */     public void writeCustomTypeSuffixForObject(Object value, JsonGenerator gen, String typeId) throws IOException
/*     */     {
/* 465 */       this._typeSerializer.writeCustomTypeSuffixForObject(this._forObject, gen, typeId);
/*     */     }
/*     */     
/*     */     public void writeCustomTypeSuffixForArray(Object value, JsonGenerator gen, String typeId) throws IOException
/*     */     {
/* 470 */       this._typeSerializer.writeCustomTypeSuffixForArray(this._forObject, gen, typeId);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\JsonValueSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */