/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.sql.Date;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Collection;
/*     */ import java.util.Currency;
/*     */ import java.util.HashMap;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ public class StdJdkSerializers
/*     */ {
/*     */   public static Collection<java.util.Map.Entry<Class<?>, Object>> all()
/*     */   {
/*  27 */     HashMap<Class<?>, Object> sers = new HashMap();
/*     */     
/*     */ 
/*  30 */     ToStringSerializer sls = ToStringSerializer.instance;
/*     */     
/*  32 */     sers.put(java.net.URL.class, sls);
/*  33 */     sers.put(java.net.URI.class, sls);
/*     */     
/*  35 */     sers.put(Currency.class, sls);
/*  36 */     sers.put(java.util.UUID.class, new UUIDSerializer());
/*  37 */     sers.put(Pattern.class, sls);
/*  38 */     sers.put(java.util.Locale.class, sls);
/*     */     
/*     */ 
/*  41 */     sers.put(AtomicBoolean.class, AtomicBooleanSerializer.class);
/*  42 */     sers.put(AtomicInteger.class, AtomicIntegerSerializer.class);
/*  43 */     sers.put(AtomicLong.class, AtomicLongSerializer.class);
/*     */     
/*     */ 
/*  46 */     sers.put(java.io.File.class, FileSerializer.class);
/*  47 */     sers.put(Class.class, ClassSerializer.class);
/*     */     
/*     */ 
/*  50 */     sers.put(Void.class, NullSerializer.instance);
/*  51 */     sers.put(Void.TYPE, NullSerializer.instance);
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  57 */       sers.put(Timestamp.class, DateSerializer.instance);
/*     */       
/*     */ 
/*  60 */       sers.put(Date.class, SqlDateSerializer.class);
/*  61 */       sers.put(java.sql.Time.class, SqlTimeSerializer.class);
/*     */     }
/*     */     catch (NoClassDefFoundError e) {}
/*     */     
/*     */ 
/*  66 */     return sers.entrySet();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class AtomicBooleanSerializer
/*     */     extends StdScalarSerializer<AtomicBoolean>
/*     */   {
/*     */     public AtomicBooleanSerializer()
/*     */     {
/*  78 */       super(false);
/*     */     }
/*     */     
/*     */     public void serialize(AtomicBoolean value, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonGenerationException {
/*  82 */       gen.writeBoolean(value.get());
/*     */     }
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/*  87 */       return createSchemaNode("boolean", true);
/*     */     }
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException
/*     */     {
/*  92 */       visitor.expectBooleanFormat(typeHint);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AtomicIntegerSerializer extends StdScalarSerializer<AtomicInteger>
/*     */   {
/*     */     public AtomicIntegerSerializer() {
/*  99 */       super(false);
/*     */     }
/*     */     
/*     */     public void serialize(AtomicInteger value, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonGenerationException {
/* 103 */       gen.writeNumber(value.get());
/*     */     }
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 108 */       return createSchemaNode("integer", true);
/*     */     }
/*     */     
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 114 */       visitIntFormat(visitor, typeHint, JsonParser.NumberType.INT);
/*     */     }
/*     */   }
/*     */   
/*     */   public static class AtomicLongSerializer extends StdScalarSerializer<AtomicLong>
/*     */   {
/*     */     public AtomicLongSerializer() {
/* 121 */       super(false);
/*     */     }
/*     */     
/*     */     public void serialize(AtomicLong value, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonGenerationException {
/* 125 */       gen.writeNumber(value.get());
/*     */     }
/*     */     
/*     */     public JsonNode getSchema(SerializerProvider provider, Type typeHint)
/*     */     {
/* 130 */       return createSchemaNode("integer", true);
/*     */     }
/*     */     
/*     */ 
/*     */     public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */       throws JsonMappingException
/*     */     {
/* 137 */       visitIntFormat(visitor, typeHint, JsonParser.NumberType.LONG);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\StdJdkSerializers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */