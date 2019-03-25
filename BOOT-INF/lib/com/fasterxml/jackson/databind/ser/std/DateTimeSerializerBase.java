/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Shape;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat;
/*     */ import com.fasterxml.jackson.databind.ser.ContextualSerializer;
/*     */ import com.fasterxml.jackson.databind.util.StdDateFormat;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Type;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class DateTimeSerializerBase<T>
/*     */   extends StdScalarSerializer<T>
/*     */   implements ContextualSerializer
/*     */ {
/*     */   protected final Boolean _useTimestamp;
/*     */   protected final DateFormat _customFormat;
/*     */   
/*     */   protected DateTimeSerializerBase(Class<T> type, Boolean useTimestamp, DateFormat customFormat)
/*     */   {
/*  41 */     super(type);
/*  42 */     this._useTimestamp = useTimestamp;
/*  43 */     this._customFormat = customFormat;
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract DateTimeSerializerBase<T> withFormat(Boolean paramBoolean, DateFormat paramDateFormat);
/*     */   
/*     */   public JsonSerializer<?> createContextual(SerializerProvider serializers, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/*  52 */     if (property == null) {
/*  53 */       return this;
/*     */     }
/*  55 */     JsonFormat.Value format = findFormatOverrides(serializers, property, handledType());
/*  56 */     if (format == null) {
/*  57 */       return this;
/*     */     }
/*     */     
/*  60 */     JsonFormat.Shape shape = format.getShape();
/*  61 */     if (shape.isNumeric()) {
/*  62 */       return withFormat(Boolean.TRUE, null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  67 */     if (format.hasPattern()) {
/*  68 */       Locale loc = format.hasLocale() ? format.getLocale() : serializers.getLocale();
/*     */       
/*     */ 
/*  71 */       SimpleDateFormat df = new SimpleDateFormat(format.getPattern(), loc);
/*  72 */       TimeZone tz = format.hasTimeZone() ? format.getTimeZone() : serializers.getTimeZone();
/*     */       
/*  74 */       df.setTimeZone(tz);
/*  75 */       return withFormat(Boolean.FALSE, df);
/*     */     }
/*     */     
/*     */ 
/*  79 */     boolean hasLocale = format.hasLocale();
/*  80 */     boolean hasTZ = format.hasTimeZone();
/*  81 */     boolean asString = shape == JsonFormat.Shape.STRING;
/*     */     
/*  83 */     if ((!hasLocale) && (!hasTZ) && (!asString)) {
/*  84 */       return this;
/*     */     }
/*     */     
/*  87 */     DateFormat df0 = serializers.getConfig().getDateFormat();
/*     */     
/*  89 */     if ((df0 instanceof StdDateFormat)) {
/*  90 */       StdDateFormat std = (StdDateFormat)df0;
/*  91 */       if (format.hasLocale()) {
/*  92 */         std = std.withLocale(format.getLocale());
/*     */       }
/*  94 */       if (format.hasTimeZone()) {
/*  95 */         std = std.withTimeZone(format.getTimeZone());
/*     */       }
/*  97 */       return withFormat(Boolean.FALSE, std);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 103 */     if (!(df0 instanceof SimpleDateFormat))
/*     */     {
/* 105 */       serializers.reportMappingProblem("Configured `DateFormat` (%s) not a `SimpleDateFormat`; can not configure `Locale` or `TimeZone`", new Object[] { df0.getClass().getName() });
/*     */     }
/*     */     
/*     */ 
/* 109 */     SimpleDateFormat df = (SimpleDateFormat)df0;
/* 110 */     if (hasLocale)
/*     */     {
/* 112 */       df = new SimpleDateFormat(df.toPattern(), format.getLocale());
/*     */     } else {
/* 114 */       df = (SimpleDateFormat)df.clone();
/*     */     }
/* 116 */     TimeZone newTz = format.getTimeZone();
/* 117 */     boolean changeTZ = (newTz != null) && (!newTz.equals(df.getTimeZone()));
/* 118 */     if (changeTZ) {
/* 119 */       df.setTimeZone(newTz);
/*     */     }
/* 121 */     return withFormat(Boolean.FALSE, df);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public boolean isEmpty(T value)
/*     */   {
/* 134 */     return (value == null) || (_timestamp(value) == 0L);
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean isEmpty(SerializerProvider serializers, T value)
/*     */   {
/* 140 */     return (value == null) || (_timestamp(value) == 0L);
/*     */   }
/*     */   
/*     */ 
/*     */   protected abstract long _timestamp(T paramT);
/*     */   
/*     */   public JsonNode getSchema(SerializerProvider serializers, Type typeHint)
/*     */   {
/* 148 */     return createSchemaNode(_asTimestamp(serializers) ? "number" : "string", true);
/*     */   }
/*     */   
/*     */   public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint)
/*     */     throws JsonMappingException
/*     */   {
/* 154 */     _acceptJsonFormatVisitor(visitor, typeHint, _asTimestamp(visitor.getProvider()));
/*     */   }
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
/*     */   protected boolean _asTimestamp(SerializerProvider serializers)
/*     */   {
/* 175 */     if (this._useTimestamp != null) {
/* 176 */       return this._useTimestamp.booleanValue();
/*     */     }
/* 178 */     if (this._customFormat == null) {
/* 179 */       if (serializers != null) {
/* 180 */         return serializers.isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
/*     */       }
/*     */       
/* 183 */       throw new IllegalArgumentException("Null SerializerProvider passed for " + handledType().getName());
/*     */     }
/* 185 */     return false;
/*     */   }
/*     */   
/*     */   protected void _acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint, boolean asNumber)
/*     */     throws JsonMappingException
/*     */   {
/* 191 */     if (asNumber) {
/* 192 */       visitIntFormat(visitor, typeHint, JsonParser.NumberType.LONG, JsonValueFormat.UTC_MILLISEC);
/*     */     }
/*     */     else {
/* 195 */       visitStringFormat(visitor, typeHint, JsonValueFormat.DATE_TIME);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\DateTimeSerializerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */