/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NumberSerializers
/*     */ {
/*     */   public static void addAll(Map<String, JsonSerializer<?>> allDeserializers)
/*     */   {
/*  26 */     allDeserializers.put(Integer.class.getName(), new IntegerSerializer(Integer.class));
/*  27 */     allDeserializers.put(Integer.TYPE.getName(), new IntegerSerializer(Integer.TYPE));
/*  28 */     allDeserializers.put(Long.class.getName(), new LongSerializer(Long.class));
/*  29 */     allDeserializers.put(Long.TYPE.getName(), new LongSerializer(Long.TYPE));
/*     */     
/*  31 */     allDeserializers.put(Byte.class.getName(), IntLikeSerializer.instance);
/*  32 */     allDeserializers.put(Byte.TYPE.getName(), IntLikeSerializer.instance);
/*  33 */     allDeserializers.put(Short.class.getName(), ShortSerializer.instance);
/*  34 */     allDeserializers.put(Short.TYPE.getName(), ShortSerializer.instance);
/*     */     
/*     */ 
/*  37 */     allDeserializers.put(Double.class.getName(), new DoubleSerializer(Double.class));
/*  38 */     allDeserializers.put(Double.TYPE.getName(), new DoubleSerializer(Double.TYPE));
/*  39 */     allDeserializers.put(Float.class.getName(), FloatSerializer.instance);
/*  40 */     allDeserializers.put(Float.TYPE.getName(), FloatSerializer.instance);
/*     */   }
/*     */   
/*     */ 
/*     */   protected static abstract class Base<T>
/*     */     extends StdScalarSerializer<T>
/*     */     implements ContextualSerializer
/*     */   {
/*     */     protected final JsonParser.NumberType _numberType;
/*     */     
/*     */     protected final String _schemaType;
/*     */     
/*     */     protected final boolean _isInt;
/*     */     
/*     */ 
/*     */     protected Base(Class<?> cls, JsonParser.NumberType numberType, String schemaType)
/*     */     {
/*  57 */       super(false);
/*  58 */       this._numberType = numberType;
/*  59 */       this._schemaType = schemaType;
/*  60 */       this._isInt = ((numberType == JsonParser.NumberType.INT) || (numberType == JsonParser.NumberType.LONG) || (numberType == JsonParser.NumberType.BIG_INTEGER));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/*  67 */       return createSchemaNode(this._schemaType, true);
/*     */     }
/*     */     
/*     */ 
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/*  74 */       if (this._isInt) {
/*  75 */         visitIntFormat(visitor, typeHint, this._numberType);
/*     */       } else {
/*  77 */         visitFloatFormat(visitor, typeHint, this._numberType);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonSerializer<?> createContextual(SerializerProvider prov, BeanProperty property)
/*     */       throws JsonMappingException
/*     */     {
/*  85 */       JsonFormat.Value format = findFormatOverrides(prov, property, handledType());
/*  86 */       if (format != null) {
/*  87 */         switch (NumberSerializers.1.$SwitchMap$com$fasterxml$jackson$annotation$JsonFormat$Shape[format.getShape().ordinal()]) {
/*     */         case 1: 
/*  89 */           return ToStringSerializer.instance;
/*     */         }
/*     */         
/*     */       }
/*  93 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class ShortSerializer
/*     */     extends NumberSerializers.Base<Object>
/*     */   {
/* 105 */     static final ShortSerializer instance = new ShortSerializer();
/*     */     
/*     */     public ShortSerializer() {
/* 108 */       super(JsonParser.NumberType.INT, "number");
/*     */     }
/*     */     
/*     */     public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 114 */       gen.writeNumber(((Short)value).shortValue());
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
/*     */   @JacksonStdImpl
/*     */   public static final class IntegerSerializer
/*     */     extends NumberSerializers.Base<Object>
/*     */   {
/*     */     public IntegerSerializer(Class<?> type)
/*     */     {
/* 131 */       super(JsonParser.NumberType.INT, "integer");
/*     */     }
/*     */     
/*     */     public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 137 */       gen.writeNumber(((Integer)value).intValue());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException
/*     */     {
/* 146 */       serialize(value, gen, provider);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class IntLikeSerializer
/*     */     extends NumberSerializers.Base<Object>
/*     */   {
/* 157 */     static final IntLikeSerializer instance = new IntLikeSerializer();
/*     */     
/*     */     public IntLikeSerializer() {
/* 160 */       super(JsonParser.NumberType.INT, "integer");
/*     */     }
/*     */     
/*     */     public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 166 */       gen.writeNumber(((Number)value).intValue());
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static final class LongSerializer extends NumberSerializers.Base<Object> {
/*     */     public LongSerializer(Class<?> cls) {
/* 173 */       super(JsonParser.NumberType.LONG, "number");
/*     */     }
/*     */     
/*     */     public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 179 */       gen.writeNumber(((Long)value).longValue());
/*     */     }
/*     */   }
/*     */   
/*     */   @JacksonStdImpl
/*     */   public static final class FloatSerializer extends NumberSerializers.Base<Object> {
/* 185 */     static final FloatSerializer instance = new FloatSerializer();
/*     */     
/*     */     public FloatSerializer() {
/* 188 */       super(JsonParser.NumberType.FLOAT, "number");
/*     */     }
/*     */     
/*     */     public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 194 */       gen.writeNumber(((Float)value).floatValue());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static final class DoubleSerializer
/*     */     extends NumberSerializers.Base<Object>
/*     */   {
/*     */     public DoubleSerializer(Class<?> cls)
/*     */     {
/* 208 */       super(JsonParser.NumberType.DOUBLE, "number");
/*     */     }
/*     */     
/*     */     public void serialize(Object value, JsonGenerator gen, SerializerProvider provider)
/*     */       throws IOException
/*     */     {
/* 214 */       gen.writeNumber(((Double)value).doubleValue());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void serializeWithType(Object value, JsonGenerator gen, SerializerProvider provider, TypeSerializer typeSer)
/*     */       throws IOException
/*     */     {
/* 223 */       serialize(value, gen, provider);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\NumberSerializers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */