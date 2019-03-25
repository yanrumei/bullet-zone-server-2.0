/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class StdArraySerializers
/*     */ {
/*  24 */   protected static final HashMap<String, JsonSerializer<?>> _arraySerializers = new HashMap();
/*     */   
/*     */   static
/*     */   {
/*  28 */     _arraySerializers.put(boolean[].class.getName(), new BooleanArraySerializer());
/*  29 */     _arraySerializers.put(byte[].class.getName(), new ByteArraySerializer());
/*  30 */     _arraySerializers.put(char[].class.getName(), new CharArraySerializer());
/*  31 */     _arraySerializers.put(short[].class.getName(), new ShortArraySerializer());
/*  32 */     _arraySerializers.put(int[].class.getName(), new IntArraySerializer());
/*  33 */     _arraySerializers.put(long[].class.getName(), new LongArraySerializer());
/*  34 */     _arraySerializers.put(float[].class.getName(), new FloatArraySerializer());
/*  35 */     _arraySerializers.put(double[].class.getName(), new DoubleArraySerializer());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static JsonSerializer<?> findStandardImpl(Class<?> cls)
/*     */   {
/*  45 */     return (JsonSerializer)_arraySerializers.get(cls.getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static abstract class TypedPrimitiveArraySerializer<T>
/*     */     extends ArraySerializerBase<T>
/*     */   {
/*     */     protected final TypeSerializer _valueTypeSerializer;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected TypedPrimitiveArraySerializer(Class<T> cls)
/*     */     {
/*  67 */       super();
/*  68 */       this._valueTypeSerializer = null;
/*     */     }
/*     */     
/*     */     protected TypedPrimitiveArraySerializer(TypedPrimitiveArraySerializer<T> src, BeanProperty prop, TypeSerializer vts, Boolean unwrapSingle)
/*     */     {
/*  73 */       super(prop, unwrapSingle);
/*  74 */       this._valueTypeSerializer = vts;
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
/*     */   @JacksonStdImpl
/*     */   public static class BooleanArraySerializer
/*     */     extends ArraySerializerBase<boolean[]>
/*     */   {
/*  90 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Boolean.class);
/*     */     
/*  92 */     public BooleanArraySerializer() { super(); }
/*     */     
/*     */     protected BooleanArraySerializer(BooleanArraySerializer src, BeanProperty prop, Boolean unwrapSingle)
/*     */     {
/*  96 */       super(prop, unwrapSingle);
/*     */     }
/*     */     
/*     */     public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle)
/*     */     {
/* 101 */       return new BooleanArraySerializer(this, prop, unwrapSingle);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 110 */       return this;
/*     */     }
/*     */     
/*     */     public JavaType getContentType()
/*     */     {
/* 115 */       return VALUE_TYPE;
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<?> getContentSerializer()
/*     */     {
/* 121 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, boolean[] value)
/*     */     {
/* 126 */       return (value == null) || (value.length == 0);
/*     */     }
/*     */     
/*     */     public boolean hasSingleElement(boolean[] value)
/*     */     {
/* 131 */       return value.length == 1;
/*     */     }
/*     */     
/*     */     public final void serialize(boolean[] value, JsonGenerator g, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 137 */       int len = value.length;
/* 138 */       if ((len == 1) && (
/* 139 */         ((this._unwrapSingle == null) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) || (this._unwrapSingle == Boolean.TRUE)))
/*     */       {
/*     */ 
/* 142 */         serializeContents(value, g, provider);
/* 143 */         return;
/*     */       }
/*     */       
/* 146 */       g.writeStartArray(len);
/* 147 */       serializeContents(value, g, provider);
/* 148 */       g.writeEndArray();
/*     */     }
/*     */     
/*     */ 
/*     */     public void serializeContents(boolean[] value, JsonGenerator g, SerializerProvider provider)
/*     */       throws IOException, com.fasterxml.jackson.core.JsonGenerationException
/*     */     {
/* 155 */       int i = 0; for (int len = value.length; i < len; i++) {
/* 156 */         g.writeBoolean(value[i]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 163 */       ObjectNode o = createSchemaNode("array", true);
/* 164 */       o.set("items", createSchemaNode("boolean"));
/* 165 */       return o;
/*     */     }
/*     */     
/*     */ 
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 172 */       visitArrayFormat(visitor, typeHint, JsonFormatTypes.BOOLEAN);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class ShortArraySerializer
/*     */     extends StdArraySerializers.TypedPrimitiveArraySerializer<short[]>
/*     */   {
/* 181 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Short.TYPE);
/*     */     
/* 183 */     public ShortArraySerializer() { super(); }
/*     */     
/*     */     public ShortArraySerializer(ShortArraySerializer src, BeanProperty prop, TypeSerializer vts, Boolean unwrapSingle) {
/* 186 */       super(prop, vts, unwrapSingle);
/*     */     }
/*     */     
/*     */     public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle)
/*     */     {
/* 191 */       return new ShortArraySerializer(this, prop, this._valueTypeSerializer, unwrapSingle);
/*     */     }
/*     */     
/*     */     public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 196 */       return new ShortArraySerializer(this, this._property, vts, this._unwrapSingle);
/*     */     }
/*     */     
/*     */     public JavaType getContentType()
/*     */     {
/* 201 */       return VALUE_TYPE;
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<?> getContentSerializer()
/*     */     {
/* 207 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, short[] value)
/*     */     {
/* 212 */       return (value == null) || (value.length == 0);
/*     */     }
/*     */     
/*     */     public boolean hasSingleElement(short[] value)
/*     */     {
/* 217 */       return value.length == 1;
/*     */     }
/*     */     
/*     */     public final void serialize(short[] value, JsonGenerator g, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 223 */       int len = value.length;
/* 224 */       if ((len == 1) && (
/* 225 */         ((this._unwrapSingle == null) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) || (this._unwrapSingle == Boolean.TRUE)))
/*     */       {
/*     */ 
/* 228 */         serializeContents(value, g, provider);
/* 229 */         return;
/*     */       }
/*     */       
/* 232 */       g.writeStartArray(len);
/* 233 */       serializeContents(value, g, provider);
/* 234 */       g.writeEndArray();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void serializeContents(short[] value, JsonGenerator g, SerializerProvider provider)
/*     */       throws IOException, com.fasterxml.jackson.core.JsonGenerationException
/*     */     {
/* 242 */       if (this._valueTypeSerializer != null) {
/* 243 */         int i = 0; for (int len = value.length; i < len; i++) {
/* 244 */           this._valueTypeSerializer.writeTypePrefixForScalar(null, g, Short.TYPE);
/* 245 */           g.writeNumber(value[i]);
/* 246 */           this._valueTypeSerializer.writeTypeSuffixForScalar(null, g);
/*     */         }
/* 248 */         return;
/*     */       }
/* 250 */       int i = 0; for (int len = value.length; i < len; i++) {
/* 251 */         g.writeNumber(value[i]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 259 */       ObjectNode o = createSchemaNode("array", true);
/* 260 */       return o.set("items", createSchemaNode("integer"));
/*     */     }
/*     */     
/*     */ 
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 267 */       visitArrayFormat(visitor, typeHint, JsonFormatTypes.INTEGER);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class CharArraySerializer
/*     */     extends StdSerializer<char[]>
/*     */   {
/*     */     public CharArraySerializer()
/*     */     {
/* 281 */       super();
/*     */     }
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, char[] value) {
/* 285 */       return (value == null) || (value.length == 0);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void serialize(char[] value, JsonGenerator g, SerializerProvider provider)
/*     */       throws IOException, com.fasterxml.jackson.core.JsonGenerationException
/*     */     {
/* 293 */       if (provider.isEnabled(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS)) {
/* 294 */         g.writeStartArray(value.length);
/* 295 */         _writeArrayContents(g, value);
/* 296 */         g.writeEndArray();
/*     */       } else {
/* 298 */         g.writeString(value, 0, value.length);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void serializeWithType(char[] value, JsonGenerator g, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException, com.fasterxml.jackson.core.JsonGenerationException
/*     */     {
/* 308 */       if (provider.isEnabled(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS)) {
/* 309 */         typeSer.writeTypePrefixForArray(value, g);
/* 310 */         _writeArrayContents(g, value);
/* 311 */         typeSer.writeTypeSuffixForArray(value, g);
/*     */       } else {
/* 313 */         typeSer.writeTypePrefixForScalar(value, g);
/* 314 */         g.writeString(value, 0, value.length);
/* 315 */         typeSer.writeTypeSuffixForScalar(value, g);
/*     */       }
/*     */     }
/*     */     
/*     */     private final void _writeArrayContents(JsonGenerator g, char[] value)
/*     */       throws IOException, com.fasterxml.jackson.core.JsonGenerationException
/*     */     {
/* 322 */       int i = 0; for (int len = value.length; i < len; i++) {
/* 323 */         g.writeString(value, i, 1);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 330 */       ObjectNode o = createSchemaNode("array", true);
/* 331 */       ObjectNode itemSchema = createSchemaNode("string");
/* 332 */       itemSchema.put("type", "string");
/* 333 */       return o.set("items", itemSchema);
/*     */     }
/*     */     
/*     */ 
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 340 */       visitArrayFormat(visitor, typeHint, JsonFormatTypes.STRING);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class IntArraySerializer
/*     */     extends ArraySerializerBase<int[]>
/*     */   {
/* 349 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Integer.TYPE);
/*     */     
/* 351 */     public IntArraySerializer() { super(); }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected IntArraySerializer(IntArraySerializer src, BeanProperty prop, Boolean unwrapSingle)
/*     */     {
/* 358 */       super(prop, unwrapSingle);
/*     */     }
/*     */     
/*     */     public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle)
/*     */     {
/* 363 */       return new IntArraySerializer(this, prop, unwrapSingle);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 372 */       return this;
/*     */     }
/*     */     
/*     */     public JavaType getContentType()
/*     */     {
/* 377 */       return VALUE_TYPE;
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<?> getContentSerializer()
/*     */     {
/* 383 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, int[] value)
/*     */     {
/* 388 */       return (value == null) || (value.length == 0);
/*     */     }
/*     */     
/*     */     public boolean hasSingleElement(int[] value)
/*     */     {
/* 393 */       return value.length == 1;
/*     */     }
/*     */     
/*     */     public final void serialize(int[] value, JsonGenerator g, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 399 */       int len = value.length;
/* 400 */       if ((len == 1) && (
/* 401 */         ((this._unwrapSingle == null) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) || (this._unwrapSingle == Boolean.TRUE)))
/*     */       {
/*     */ 
/* 404 */         serializeContents(value, g, provider);
/* 405 */         return;
/*     */       }
/*     */       
/*     */ 
/* 409 */       g.setCurrentValue(value);
/* 410 */       g.writeArray(value, 0, value.length);
/*     */     }
/*     */     
/*     */ 
/*     */     public void serializeContents(int[] value, JsonGenerator g, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 417 */       int i = 0; for (int len = value.length; i < len; i++) {
/* 418 */         g.writeNumber(value[i]);
/*     */       }
/*     */     }
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 424 */       return createSchemaNode("array", true).set("items", createSchemaNode("integer"));
/*     */     }
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 430 */       visitArrayFormat(visitor, typeHint, JsonFormatTypes.INTEGER);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class LongArraySerializer
/*     */     extends StdArraySerializers.TypedPrimitiveArraySerializer<long[]>
/*     */   {
/* 439 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Long.TYPE);
/*     */     
/* 441 */     public LongArraySerializer() { super(); }
/*     */     
/*     */     public LongArraySerializer(LongArraySerializer src, BeanProperty prop, TypeSerializer vts, Boolean unwrapSingle) {
/* 444 */       super(prop, vts, unwrapSingle);
/*     */     }
/*     */     
/*     */     public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle)
/*     */     {
/* 449 */       return new LongArraySerializer(this, prop, this._valueTypeSerializer, unwrapSingle);
/*     */     }
/*     */     
/*     */     public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 454 */       return new LongArraySerializer(this, this._property, vts, this._unwrapSingle);
/*     */     }
/*     */     
/*     */     public JavaType getContentType()
/*     */     {
/* 459 */       return VALUE_TYPE;
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<?> getContentSerializer()
/*     */     {
/* 465 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, long[] value)
/*     */     {
/* 470 */       return (value == null) || (value.length == 0);
/*     */     }
/*     */     
/*     */     public boolean hasSingleElement(long[] value)
/*     */     {
/* 475 */       return value.length == 1;
/*     */     }
/*     */     
/*     */     public final void serialize(long[] value, JsonGenerator g, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 481 */       int len = value.length;
/* 482 */       if ((len == 1) && (
/* 483 */         ((this._unwrapSingle == null) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) || (this._unwrapSingle == Boolean.TRUE)))
/*     */       {
/*     */ 
/* 486 */         serializeContents(value, g, provider);
/* 487 */         return;
/*     */       }
/*     */       
/*     */ 
/* 491 */       g.setCurrentValue(value);
/* 492 */       g.writeArray(value, 0, value.length);
/*     */     }
/*     */     
/*     */ 
/*     */     public void serializeContents(long[] value, JsonGenerator g, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 499 */       if (this._valueTypeSerializer != null) {
/* 500 */         int i = 0; for (int len = value.length; i < len; i++) {
/* 501 */           this._valueTypeSerializer.writeTypePrefixForScalar(null, g, Long.TYPE);
/* 502 */           g.writeNumber(value[i]);
/* 503 */           this._valueTypeSerializer.writeTypeSuffixForScalar(null, g);
/*     */         }
/* 505 */         return;
/*     */       }
/*     */       
/* 508 */       int i = 0; for (int len = value.length; i < len; i++) {
/* 509 */         g.writeNumber(value[i]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 516 */       return createSchemaNode("array", true).set("items", createSchemaNode("number", true));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 524 */       visitArrayFormat(visitor, typeHint, JsonFormatTypes.NUMBER);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class FloatArraySerializer
/*     */     extends StdArraySerializers.TypedPrimitiveArraySerializer<float[]>
/*     */   {
/* 533 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Float.TYPE);
/*     */     
/*     */     public FloatArraySerializer() {
/* 536 */       super();
/*     */     }
/*     */     
/*     */     public FloatArraySerializer(FloatArraySerializer src, BeanProperty prop, TypeSerializer vts, Boolean unwrapSingle) {
/* 540 */       super(prop, vts, unwrapSingle);
/*     */     }
/*     */     
/*     */     public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 545 */       return new FloatArraySerializer(this, this._property, vts, this._unwrapSingle);
/*     */     }
/*     */     
/*     */     public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle)
/*     */     {
/* 550 */       return new FloatArraySerializer(this, prop, this._valueTypeSerializer, unwrapSingle);
/*     */     }
/*     */     
/*     */     public JavaType getContentType()
/*     */     {
/* 555 */       return VALUE_TYPE;
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<?> getContentSerializer()
/*     */     {
/* 561 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, float[] value)
/*     */     {
/* 566 */       return (value == null) || (value.length == 0);
/*     */     }
/*     */     
/*     */     public boolean hasSingleElement(float[] value)
/*     */     {
/* 571 */       return value.length == 1;
/*     */     }
/*     */     
/*     */     public final void serialize(float[] value, JsonGenerator g, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 577 */       int len = value.length;
/* 578 */       if ((len == 1) && (
/* 579 */         ((this._unwrapSingle == null) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) || (this._unwrapSingle == Boolean.TRUE)))
/*     */       {
/*     */ 
/* 582 */         serializeContents(value, g, provider);
/* 583 */         return;
/*     */       }
/*     */       
/* 586 */       g.writeStartArray(len);
/* 587 */       serializeContents(value, g, provider);
/* 588 */       g.writeEndArray();
/*     */     }
/*     */     
/*     */ 
/*     */     public void serializeContents(float[] value, JsonGenerator g, SerializerProvider provider)
/*     */       throws IOException, com.fasterxml.jackson.core.JsonGenerationException
/*     */     {
/* 595 */       if (this._valueTypeSerializer != null) {
/* 596 */         int i = 0; for (int len = value.length; i < len; i++) {
/* 597 */           this._valueTypeSerializer.writeTypePrefixForScalar(null, g, Float.TYPE);
/* 598 */           g.writeNumber(value[i]);
/* 599 */           this._valueTypeSerializer.writeTypeSuffixForScalar(null, g);
/*     */         }
/* 601 */         return;
/*     */       }
/* 603 */       int i = 0; for (int len = value.length; i < len; i++) {
/* 604 */         g.writeNumber(value[i]);
/*     */       }
/*     */     }
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 610 */       return createSchemaNode("array", true).set("items", createSchemaNode("number"));
/*     */     }
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 616 */       visitArrayFormat(visitor, typeHint, JsonFormatTypes.NUMBER);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class DoubleArraySerializer
/*     */     extends ArraySerializerBase<double[]>
/*     */   {
/* 625 */     private static final JavaType VALUE_TYPE = TypeFactory.defaultInstance().uncheckedSimpleType(Double.TYPE);
/*     */     
/* 627 */     public DoubleArraySerializer() { super(); }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected DoubleArraySerializer(DoubleArraySerializer src, BeanProperty prop, Boolean unwrapSingle)
/*     */     {
/* 634 */       super(prop, unwrapSingle);
/*     */     }
/*     */     
/*     */     public JsonSerializer<?> _withResolved(BeanProperty prop, Boolean unwrapSingle)
/*     */     {
/* 639 */       return new DoubleArraySerializer(this, prop, unwrapSingle);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */     {
/* 648 */       return this;
/*     */     }
/*     */     
/*     */     public JavaType getContentType()
/*     */     {
/* 653 */       return VALUE_TYPE;
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<?> getContentSerializer()
/*     */     {
/* 659 */       return null;
/*     */     }
/*     */     
/*     */     public boolean isEmpty(SerializerProvider prov, double[] value)
/*     */     {
/* 664 */       return (value == null) || (value.length == 0);
/*     */     }
/*     */     
/*     */     public boolean hasSingleElement(double[] value)
/*     */     {
/* 669 */       return value.length == 1;
/*     */     }
/*     */     
/*     */     public final void serialize(double[] value, JsonGenerator g, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 675 */       int len = value.length;
/* 676 */       if ((len == 1) && (
/* 677 */         ((this._unwrapSingle == null) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) || (this._unwrapSingle == Boolean.TRUE)))
/*     */       {
/*     */ 
/* 680 */         serializeContents(value, g, provider);
/* 681 */         return;
/*     */       }
/*     */       
/*     */ 
/* 685 */       g.setCurrentValue(value);
/* 686 */       g.writeArray(value, 0, value.length);
/*     */     }
/*     */     
/*     */     public void serializeContents(double[] value, JsonGenerator g, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 692 */       int i = 0; for (int len = value.length; i < len; i++) {
/* 693 */         g.writeNumber(value[i]);
/*     */       }
/*     */     }
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 699 */       return createSchemaNode("array", true).set("items", createSchemaNode("number"));
/*     */     }
/*     */     
/*     */ 
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 706 */       visitArrayFormat(visitor, typeHint, JsonFormatTypes.NUMBER);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\StdArraySerializers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */