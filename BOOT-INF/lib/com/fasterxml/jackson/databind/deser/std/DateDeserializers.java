/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.StdDateFormat;
/*     */ import java.io.IOException;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.DateFormat;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashSet;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ public class DateDeserializers
/*     */ {
/*  26 */   private static final HashSet<String> _classNames = new HashSet();
/*     */   
/*  28 */   static { Class<?>[] numberTypes = { Calendar.class, GregorianCalendar.class, java.sql.Date.class, java.util.Date.class, Timestamp.class };
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  35 */     for (Class<?> cls : numberTypes) {
/*  36 */       _classNames.add(cls.getName());
/*     */     }
/*     */   }
/*     */   
/*     */   public static JsonDeserializer<?> find(Class<?> rawType, String clsName)
/*     */   {
/*  42 */     if (_classNames.contains(clsName))
/*     */     {
/*  44 */       if (rawType == Calendar.class) {
/*  45 */         return new CalendarDeserializer();
/*     */       }
/*  47 */       if (rawType == java.util.Date.class) {
/*  48 */         return DateDeserializer.instance;
/*     */       }
/*  50 */       if (rawType == java.sql.Date.class) {
/*  51 */         return new SqlDateDeserializer();
/*     */       }
/*  53 */       if (rawType == Timestamp.class) {
/*  54 */         return new TimestampDeserializer();
/*     */       }
/*  56 */       if (rawType == GregorianCalendar.class) {
/*  57 */         return new CalendarDeserializer(GregorianCalendar.class);
/*     */       }
/*     */     }
/*  60 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static abstract class DateBasedDeserializer<T>
/*     */     extends StdScalarDeserializer<T>
/*     */     implements ContextualDeserializer
/*     */   {
/*     */     protected final DateFormat _customFormat;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final String _formatString;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected DateBasedDeserializer(Class<?> clz)
/*     */     {
/*  85 */       super();
/*  86 */       this._customFormat = null;
/*  87 */       this._formatString = null;
/*     */     }
/*     */     
/*     */     protected DateBasedDeserializer(DateBasedDeserializer<T> base, DateFormat format, String formatStr)
/*     */     {
/*  92 */       super();
/*  93 */       this._customFormat = format;
/*  94 */       this._formatString = formatStr;
/*     */     }
/*     */     
/*     */ 
/*     */     protected abstract DateBasedDeserializer<T> withDateFormat(DateFormat paramDateFormat, String paramString);
/*     */     
/*     */     public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */       throws com.fasterxml.jackson.databind.JsonMappingException
/*     */     {
/* 103 */       if (property != null) {
/* 104 */         JsonFormat.Value format = findFormatOverrides(ctxt, property, handledType());
/*     */         
/* 106 */         if (format != null) {
/* 107 */           TimeZone tz = format.getTimeZone();
/*     */           
/* 109 */           if (format.hasPattern()) {
/* 110 */             String pattern = format.getPattern();
/* 111 */             Locale loc = format.hasLocale() ? format.getLocale() : ctxt.getLocale();
/* 112 */             SimpleDateFormat df = new SimpleDateFormat(pattern, loc);
/* 113 */             if (tz == null) {
/* 114 */               tz = ctxt.getTimeZone();
/*     */             }
/* 116 */             df.setTimeZone(tz);
/* 117 */             return withDateFormat(df, pattern);
/*     */           }
/*     */           
/* 120 */           if (tz != null) {
/* 121 */             DateFormat df = ctxt.getConfig().getDateFormat();
/*     */             
/* 123 */             if (df.getClass() == StdDateFormat.class) {
/* 124 */               Locale loc = format.hasLocale() ? format.getLocale() : ctxt.getLocale();
/* 125 */               StdDateFormat std = (StdDateFormat)df;
/* 126 */               std = std.withTimeZone(tz);
/* 127 */               std = std.withLocale(loc);
/* 128 */               df = std;
/*     */             }
/*     */             else {
/* 131 */               df = (DateFormat)df.clone();
/* 132 */               df.setTimeZone(tz);
/*     */             }
/* 134 */             return withDateFormat(df, this._formatString);
/*     */           }
/*     */         }
/*     */       }
/* 138 */       return this;
/*     */     }
/*     */     
/*     */ 
/*     */     protected java.util.Date _parseDate(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 145 */       if (this._customFormat != null) {
/* 146 */         JsonToken t = p.getCurrentToken();
/* 147 */         if (t == JsonToken.VALUE_STRING) {
/* 148 */           String str = p.getText().trim();
/* 149 */           if (str.length() == 0) {
/* 150 */             return (java.util.Date)getEmptyValue(ctxt);
/*     */           }
/* 152 */           synchronized (this._customFormat) {
/*     */             try {
/* 154 */               return this._customFormat.parse(str);
/*     */             } catch (ParseException e) {
/* 156 */               return (java.util.Date)ctxt.handleWeirdStringValue(handledType(), str, "expected format \"%s\"", new Object[] { this._formatString });
/*     */             }
/*     */           }
/*     */         }
/*     */         
/*     */ 
/* 162 */         if ((t == JsonToken.START_ARRAY) && (ctxt.isEnabled(com.fasterxml.jackson.databind.DeserializationFeature.UNWRAP_SINGLE_VALUE_ARRAYS))) {
/* 163 */           p.nextToken();
/* 164 */           java.util.Date parsed = _parseDate(p, ctxt);
/* 165 */           t = p.nextToken();
/* 166 */           if (t != JsonToken.END_ARRAY) {
/* 167 */             handleMissingEndArrayForSingle(p, ctxt);
/*     */           }
/* 169 */           return parsed;
/*     */         }
/*     */       }
/* 172 */       return super._parseDate(p, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   public static class CalendarDeserializer
/*     */     extends DateDeserializers.DateBasedDeserializer<Calendar>
/*     */   {
/*     */     protected final Class<? extends Calendar> _calendarClass;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public CalendarDeserializer()
/*     */     {
/* 192 */       super();
/* 193 */       this._calendarClass = null;
/*     */     }
/*     */     
/*     */     public CalendarDeserializer(Class<? extends Calendar> cc) {
/* 197 */       super();
/* 198 */       this._calendarClass = cc;
/*     */     }
/*     */     
/*     */     public CalendarDeserializer(CalendarDeserializer src, DateFormat df, String formatString) {
/* 202 */       super(df, formatString);
/* 203 */       this._calendarClass = src._calendarClass;
/*     */     }
/*     */     
/*     */     protected CalendarDeserializer withDateFormat(DateFormat df, String formatString)
/*     */     {
/* 208 */       return new CalendarDeserializer(this, df, formatString);
/*     */     }
/*     */     
/*     */     public Calendar deserialize(JsonParser p, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 214 */       java.util.Date d = _parseDate(p, ctxt);
/* 215 */       if (d == null) {
/* 216 */         return null;
/*     */       }
/* 218 */       if (this._calendarClass == null) {
/* 219 */         return ctxt.constructCalendar(d);
/*     */       }
/*     */       try {
/* 222 */         Calendar c = (Calendar)this._calendarClass.newInstance();
/* 223 */         c.setTimeInMillis(d.getTime());
/* 224 */         TimeZone tz = ctxt.getTimeZone();
/* 225 */         if (tz != null) {
/* 226 */           c.setTimeZone(tz);
/*     */         }
/* 228 */         return c;
/*     */       } catch (Exception e) {
/* 230 */         return (Calendar)ctxt.handleInstantiationProblem(this._calendarClass, d, e);
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
/*     */   @JacksonStdImpl
/*     */   public static class DateDeserializer
/*     */     extends DateDeserializers.DateBasedDeserializer<java.util.Date>
/*     */   {
/* 245 */     public static final DateDeserializer instance = new DateDeserializer();
/*     */     
/* 247 */     public DateDeserializer() { super(); }
/*     */     
/* 249 */     public DateDeserializer(DateDeserializer base, DateFormat df, String formatString) { super(df, formatString); }
/*     */     
/*     */ 
/*     */     protected DateDeserializer withDateFormat(DateFormat df, String formatString)
/*     */     {
/* 254 */       return new DateDeserializer(this, df, formatString);
/*     */     }
/*     */     
/*     */     public java.util.Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */     {
/* 259 */       return _parseDate(jp, ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class SqlDateDeserializer
/*     */     extends DateDeserializers.DateBasedDeserializer<java.sql.Date>
/*     */   {
/* 270 */     public SqlDateDeserializer() { super(); }
/*     */     
/* 272 */     public SqlDateDeserializer(SqlDateDeserializer src, DateFormat df, String formatString) { super(df, formatString); }
/*     */     
/*     */ 
/*     */     protected SqlDateDeserializer withDateFormat(DateFormat df, String formatString)
/*     */     {
/* 277 */       return new SqlDateDeserializer(this, df, formatString);
/*     */     }
/*     */     
/*     */     public java.sql.Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException
/*     */     {
/* 282 */       java.util.Date d = _parseDate(jp, ctxt);
/* 283 */       return d == null ? null : new java.sql.Date(d.getTime());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class TimestampDeserializer
/*     */     extends DateDeserializers.DateBasedDeserializer<Timestamp>
/*     */   {
/* 296 */     public TimestampDeserializer() { super(); }
/*     */     
/* 298 */     public TimestampDeserializer(TimestampDeserializer src, DateFormat df, String formatString) { super(df, formatString); }
/*     */     
/*     */ 
/*     */     protected TimestampDeserializer withDateFormat(DateFormat df, String formatString)
/*     */     {
/* 303 */       return new TimestampDeserializer(this, df, formatString);
/*     */     }
/*     */     
/*     */     public Timestamp deserialize(JsonParser jp, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 309 */       java.util.Date d = _parseDate(jp, ctxt);
/* 310 */       return d == null ? null : new Timestamp(d.getTime());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\DateDeserializers.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */