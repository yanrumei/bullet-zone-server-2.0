/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.io.NumberInput;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.EnumResolver;
/*     */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.util.Calendar;
/*     */ import java.util.Currency;
/*     */ import java.util.Date;
/*     */ import java.util.Locale;
/*     */ import java.util.UUID;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class StdKeyDeserializer
/*     */   extends KeyDeserializer
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   public static final int TYPE_BOOLEAN = 1;
/*     */   public static final int TYPE_BYTE = 2;
/*     */   public static final int TYPE_SHORT = 3;
/*     */   public static final int TYPE_CHAR = 4;
/*     */   public static final int TYPE_INT = 5;
/*     */   public static final int TYPE_LONG = 6;
/*     */   public static final int TYPE_FLOAT = 7;
/*     */   public static final int TYPE_DOUBLE = 8;
/*     */   public static final int TYPE_LOCALE = 9;
/*     */   public static final int TYPE_DATE = 10;
/*     */   public static final int TYPE_CALENDAR = 11;
/*     */   public static final int TYPE_UUID = 12;
/*     */   public static final int TYPE_URI = 13;
/*     */   public static final int TYPE_URL = 14;
/*     */   public static final int TYPE_CLASS = 15;
/*     */   public static final int TYPE_CURRENCY = 16;
/*     */   protected final int _kind;
/*     */   protected final Class<?> _keyClass;
/*     */   protected final FromStringDeserializer<?> _deser;
/*     */   
/*     */   protected StdKeyDeserializer(int kind, Class<?> cls)
/*     */   {
/*  60 */     this(kind, cls, null);
/*     */   }
/*     */   
/*     */   protected StdKeyDeserializer(int kind, Class<?> cls, FromStringDeserializer<?> deser) {
/*  64 */     this._kind = kind;
/*  65 */     this._keyClass = cls;
/*  66 */     this._deser = deser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static StdKeyDeserializer forType(Class<?> raw)
/*     */   {
/*  74 */     if ((raw == String.class) || (raw == Object.class) || (raw == CharSequence.class))
/*  75 */       return StringKD.forType(raw);
/*  76 */     int kind; if (raw == UUID.class) {
/*  77 */       kind = 12; } else { int kind;
/*  78 */       if (raw == Integer.class) {
/*  79 */         kind = 5; } else { int kind;
/*  80 */         if (raw == Long.class) {
/*  81 */           kind = 6; } else { int kind;
/*  82 */           if (raw == Date.class) {
/*  83 */             kind = 10; } else { int kind;
/*  84 */             if (raw == Calendar.class) {
/*  85 */               kind = 11;
/*     */             } else { int kind;
/*  87 */               if (raw == Boolean.class) {
/*  88 */                 kind = 1; } else { int kind;
/*  89 */                 if (raw == Byte.class) {
/*  90 */                   kind = 2; } else { int kind;
/*  91 */                   if (raw == Character.class) {
/*  92 */                     kind = 4; } else { int kind;
/*  93 */                     if (raw == Short.class) {
/*  94 */                       kind = 3; } else { int kind;
/*  95 */                       if (raw == Float.class) {
/*  96 */                         kind = 7; } else { int kind;
/*  97 */                         if (raw == Double.class) {
/*  98 */                           kind = 8; } else { int kind;
/*  99 */                           if (raw == URI.class) {
/* 100 */                             kind = 13; } else { int kind;
/* 101 */                             if (raw == URL.class) {
/* 102 */                               kind = 14; } else { int kind;
/* 103 */                               if (raw == Class.class) {
/* 104 */                                 kind = 15;
/* 105 */                               } else { if (raw == Locale.class) {
/* 106 */                                   FromStringDeserializer<?> deser = FromStringDeserializer.findDeserializer(Locale.class);
/* 107 */                                   return new StdKeyDeserializer(9, raw, deser); }
/* 108 */                                 if (raw == Currency.class) {
/* 109 */                                   FromStringDeserializer<?> deser = FromStringDeserializer.findDeserializer(Currency.class);
/* 110 */                                   return new StdKeyDeserializer(16, raw, deser);
/*     */                                 }
/* 112 */                                 return null; } } } } } } } } } } } } } }
/*     */     int kind;
/* 114 */     return new StdKeyDeserializer(kind, raw);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object deserializeKey(String key, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 121 */     if (key == null) {
/* 122 */       return null;
/*     */     }
/*     */     try {
/* 125 */       Object result = _parse(key, ctxt);
/* 126 */       if (result != null) {
/* 127 */         return result;
/*     */       }
/*     */     } catch (Exception re) {
/* 130 */       return ctxt.handleWeirdKey(this._keyClass, key, "not a valid representation, problem: (%s) %s", new Object[] { re.getClass().getName(), re.getMessage() });
/*     */     }
/*     */     
/* 133 */     if ((this._keyClass.isEnum()) && (ctxt.getConfig().isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL))) {
/* 134 */       return null;
/*     */     }
/* 136 */     return ctxt.handleWeirdKey(this._keyClass, key, "not a valid representation", new Object[0]);
/*     */   }
/*     */   
/* 139 */   public Class<?> getKeyClass() { return this._keyClass; }
/*     */   
/*     */   protected Object _parse(String key, DeserializationContext ctxt) throws Exception
/*     */   {
/* 143 */     switch (this._kind) {
/*     */     case 1: 
/* 145 */       if ("true".equals(key)) {
/* 146 */         return Boolean.TRUE;
/*     */       }
/* 148 */       if ("false".equals(key)) {
/* 149 */         return Boolean.FALSE;
/*     */       }
/* 151 */       return ctxt.handleWeirdKey(this._keyClass, key, "value not 'true' or 'false'", new Object[0]);
/*     */     
/*     */     case 2: 
/* 154 */       int value = _parseInt(key);
/*     */       
/* 156 */       if ((value < -128) || (value > 255)) {
/* 157 */         return ctxt.handleWeirdKey(this._keyClass, key, "overflow, value can not be represented as 8-bit value", new Object[0]);
/*     */       }
/* 159 */       return Byte.valueOf((byte)value);
/*     */     
/*     */ 
/*     */     case 3: 
/* 163 */       int value = _parseInt(key);
/* 164 */       if ((value < 32768) || (value > 32767)) {
/* 165 */         return ctxt.handleWeirdKey(this._keyClass, key, "overflow, value can not be represented as 16-bit value", new Object[0]);
/*     */       }
/*     */       
/* 168 */       return Short.valueOf((short)value);
/*     */     
/*     */     case 4: 
/* 171 */       if (key.length() == 1) {
/* 172 */         return Character.valueOf(key.charAt(0));
/*     */       }
/* 174 */       return ctxt.handleWeirdKey(this._keyClass, key, "can only convert 1-character Strings", new Object[0]);
/*     */     case 5: 
/* 176 */       return Integer.valueOf(_parseInt(key));
/*     */     
/*     */     case 6: 
/* 179 */       return Long.valueOf(_parseLong(key));
/*     */     
/*     */ 
/*     */     case 7: 
/* 183 */       return Float.valueOf((float)_parseDouble(key));
/*     */     case 8: 
/* 185 */       return Double.valueOf(_parseDouble(key));
/*     */     case 9: 
/*     */       try {
/* 188 */         return this._deser._deserialize(key, ctxt);
/*     */       } catch (IOException e) {
/* 190 */         return ctxt.handleWeirdKey(this._keyClass, key, "unable to parse key as locale", new Object[0]);
/*     */       }
/*     */     case 16: 
/*     */       try {
/* 194 */         return this._deser._deserialize(key, ctxt);
/*     */       } catch (IOException e) {
/* 196 */         return ctxt.handleWeirdKey(this._keyClass, key, "unable to parse key as currency", new Object[0]);
/*     */       }
/*     */     case 10: 
/* 199 */       return ctxt.parseDate(key);
/*     */     case 11: 
/* 201 */       Date date = ctxt.parseDate(key);
/* 202 */       return date == null ? null : ctxt.constructCalendar(date);
/*     */     case 12: 
/*     */       try {
/* 205 */         return UUID.fromString(key);
/*     */       } catch (Exception e) {
/* 207 */         return ctxt.handleWeirdKey(this._keyClass, key, "problem: %s", new Object[] { e.getMessage() });
/*     */       }
/*     */     case 13: 
/*     */       try {
/* 211 */         return URI.create(key);
/*     */       } catch (Exception e) {
/* 213 */         return ctxt.handleWeirdKey(this._keyClass, key, "problem: %s", new Object[] { e.getMessage() });
/*     */       }
/*     */     case 14: 
/*     */       try {
/* 217 */         return new URL(key);
/*     */       } catch (MalformedURLException e) {
/* 219 */         return ctxt.handleWeirdKey(this._keyClass, key, "problem: %s", new Object[] { e.getMessage() });
/*     */       }
/*     */     case 15: 
/*     */       try {
/* 223 */         return ctxt.findClass(key);
/*     */       } catch (Exception e) {
/* 225 */         return ctxt.handleWeirdKey(this._keyClass, key, "unable to parse key as Class", new Object[0]);
/*     */       }
/*     */     }
/* 228 */     throw new IllegalStateException("Internal error: unknown key type " + this._keyClass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int _parseInt(String key)
/*     */     throws IllegalArgumentException
/*     */   {
/* 239 */     return Integer.parseInt(key);
/*     */   }
/*     */   
/*     */   protected long _parseLong(String key) throws IllegalArgumentException {
/* 243 */     return Long.parseLong(key);
/*     */   }
/*     */   
/*     */   protected double _parseDouble(String key) throws IllegalArgumentException {
/* 247 */     return NumberInput.parseDouble(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class StringKD
/*     */     extends StdKeyDeserializer
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/* 260 */     private static final StringKD sString = new StringKD(String.class);
/* 261 */     private static final StringKD sObject = new StringKD(Object.class);
/*     */     
/* 263 */     private StringKD(Class<?> nominalType) { super(nominalType); }
/*     */     
/*     */     public static StringKD forType(Class<?> nominalType)
/*     */     {
/* 267 */       if (nominalType == String.class) {
/* 268 */         return sString;
/*     */       }
/* 270 */       if (nominalType == Object.class) {
/* 271 */         return sObject;
/*     */       }
/* 273 */       return new StringKD(nominalType);
/*     */     }
/*     */     
/*     */     public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException
/*     */     {
/* 278 */       return key;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   static final class DelegatingKD
/*     */     extends KeyDeserializer
/*     */     implements Serializable
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */ 
/*     */ 
/*     */     protected final Class<?> _keyClass;
/*     */     
/*     */ 
/*     */ 
/*     */     protected final JsonDeserializer<?> _delegate;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     protected DelegatingKD(Class<?> cls, JsonDeserializer<?> deser)
/*     */     {
/* 304 */       this._keyClass = cls;
/* 305 */       this._delegate = deser;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public final Object deserializeKey(String key, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 313 */       if (key == null) {
/* 314 */         return null;
/*     */       }
/* 316 */       TokenBuffer tb = new TokenBuffer(ctxt.getParser(), ctxt);
/* 317 */       tb.writeString(key);
/*     */       try
/*     */       {
/* 320 */         JsonParser p = tb.asParser();
/* 321 */         p.nextToken();
/* 322 */         Object result = this._delegate.deserialize(p, ctxt);
/* 323 */         if (result != null) {
/* 324 */           return result;
/*     */         }
/* 326 */         return ctxt.handleWeirdKey(this._keyClass, key, "not a valid representation", new Object[0]);
/*     */       } catch (Exception re) {}
/* 328 */       return ctxt.handleWeirdKey(this._keyClass, key, "not a valid representation: %s", tmp86_83);
/*     */     }
/*     */     
/*     */     public Class<?> getKeyClass() {
/* 332 */       return this._keyClass;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @JacksonStdImpl
/*     */   static final class EnumKD
/*     */     extends StdKeyDeserializer
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected final EnumResolver _byNameResolver;
/*     */     
/*     */     protected final AnnotatedMethod _factory;
/*     */     
/*     */     protected EnumResolver _byToStringResolver;
/*     */     
/*     */ 
/*     */     protected EnumKD(EnumResolver er, AnnotatedMethod factory)
/*     */     {
/* 353 */       super(er.getEnumClass());
/* 354 */       this._byNameResolver = er;
/* 355 */       this._factory = factory;
/*     */     }
/*     */     
/*     */     public Object _parse(String key, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 361 */       if (this._factory != null) {
/*     */         try {
/* 363 */           return this._factory.call1(key);
/*     */         } catch (Exception e) {
/* 365 */           ClassUtil.unwrapAndThrowAsIAE(e);
/*     */         }
/*     */       }
/* 368 */       EnumResolver res = ctxt.isEnabled(DeserializationFeature.READ_ENUMS_USING_TO_STRING) ? _getToStringResolver(ctxt) : this._byNameResolver;
/*     */       
/* 370 */       Enum<?> e = res.findEnum(key);
/* 371 */       if ((e == null) && (!ctxt.getConfig().isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL))) {
/* 372 */         return ctxt.handleWeirdKey(this._keyClass, key, "not one of values excepted for Enum class: %s", new Object[] { res.getEnumIds() });
/*     */       }
/*     */       
/*     */ 
/* 376 */       return e;
/*     */     }
/*     */     
/*     */     private EnumResolver _getToStringResolver(DeserializationContext ctxt)
/*     */     {
/* 381 */       EnumResolver res = this._byToStringResolver;
/* 382 */       if (res == null) {
/* 383 */         synchronized (this) {
/* 384 */           res = EnumResolver.constructUnsafeUsingToString(this._byNameResolver.getEnumClass(), ctxt.getAnnotationIntrospector());
/*     */         }
/*     */       }
/*     */       
/* 388 */       return res;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class StringCtorKeyDeserializer
/*     */     extends StdKeyDeserializer
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     protected final Constructor<?> _ctor;
/*     */     
/*     */ 
/*     */     public StringCtorKeyDeserializer(Constructor<?> ctor)
/*     */     {
/* 403 */       super(ctor.getDeclaringClass());
/* 404 */       this._ctor = ctor;
/*     */     }
/*     */     
/*     */     public Object _parse(String key, DeserializationContext ctxt)
/*     */       throws Exception
/*     */     {
/* 410 */       return this._ctor.newInstance(new Object[] { key });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   static final class StringFactoryKeyDeserializer
/*     */     extends StdKeyDeserializer
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     final Method _factoryMethod;
/*     */     
/*     */ 
/*     */     public StringFactoryKeyDeserializer(Method fm)
/*     */     {
/* 425 */       super(fm.getDeclaringClass());
/* 426 */       this._factoryMethod = fm;
/*     */     }
/*     */     
/*     */     public Object _parse(String key, DeserializationContext ctxt)
/*     */       throws Exception
/*     */     {
/* 432 */       return this._factoryMethod.invoke(null, new Object[] { key });
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\StdKeyDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */