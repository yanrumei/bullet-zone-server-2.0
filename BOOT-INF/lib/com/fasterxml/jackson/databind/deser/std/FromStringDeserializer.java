/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.util.VersionUtil;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.exc.InvalidFormatException;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.Currency;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ public abstract class FromStringDeserializer<T>
/*     */   extends StdScalarDeserializer<T>
/*     */ {
/*     */   public static Class<?>[] types()
/*     */   {
/*  31 */     return new Class[] { File.class, URL.class, URI.class, Class.class, JavaType.class, Currency.class, Pattern.class, Locale.class, Charset.class, TimeZone.class, InetAddress.class, InetSocketAddress.class, StringBuilder.class };
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
/*     */   protected FromStringDeserializer(Class<?> vc)
/*     */   {
/*  55 */     super(vc);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static Std findDeserializer(Class<?> rawType)
/*     */   {
/*  64 */     int kind = 0;
/*  65 */     if (rawType == File.class) {
/*  66 */       kind = 1;
/*  67 */     } else if (rawType == URL.class) {
/*  68 */       kind = 2;
/*  69 */     } else if (rawType == URI.class) {
/*  70 */       kind = 3;
/*  71 */     } else if (rawType == Class.class) {
/*  72 */       kind = 4;
/*  73 */     } else if (rawType == JavaType.class) {
/*  74 */       kind = 5;
/*  75 */     } else if (rawType == Currency.class) {
/*  76 */       kind = 6;
/*  77 */     } else if (rawType == Pattern.class) {
/*  78 */       kind = 7;
/*  79 */     } else if (rawType == Locale.class) {
/*  80 */       kind = 8;
/*  81 */     } else if (rawType == Charset.class) {
/*  82 */       kind = 9;
/*  83 */     } else if (rawType == TimeZone.class) {
/*  84 */       kind = 10;
/*  85 */     } else if (rawType == InetAddress.class) {
/*  86 */       kind = 11;
/*  87 */     } else if (rawType == InetSocketAddress.class) {
/*  88 */       kind = 12;
/*  89 */     } else if (rawType == StringBuilder.class) {
/*  90 */       kind = 13;
/*     */     } else {
/*  92 */       return null;
/*     */     }
/*  94 */     return new Std(rawType, kind);
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
/*     */   public T deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 108 */     String text = p.getValueAsString();
/* 109 */     if (text != null) {
/* 110 */       if ((text.length() == 0) || ((text = text.trim()).length() == 0))
/*     */       {
/* 112 */         return (T)_deserializeFromEmptyString();
/*     */       }
/* 114 */       Exception cause = null;
/*     */       
/*     */ 
/*     */       try
/*     */       {
/* 119 */         return (T)_deserialize(text, ctxt);
/*     */       } catch (IllegalArgumentException iae) {
/* 121 */         cause = iae;
/*     */       } catch (MalformedURLException me) {
/* 123 */         cause = me;
/*     */       }
/* 125 */       String msg = "not a valid textual representation";
/* 126 */       if (cause != null) {
/* 127 */         String m2 = cause.getMessage();
/* 128 */         if (m2 != null) {
/* 129 */           msg = msg + ", problem: " + m2;
/*     */         }
/*     */       }
/*     */       
/* 133 */       JsonMappingException e = ctxt.weirdStringException(text, this._valueClass, msg);
/* 134 */       if (cause != null) {
/* 135 */         e.initCause(cause);
/*     */       }
/* 137 */       throw e;
/*     */     }
/*     */     
/* 140 */     JsonToken t = p.getCurrentToken();
/*     */     
/* 142 */     if (t == JsonToken.START_ARRAY) {
/* 143 */       return (T)_deserializeFromArray(p, ctxt);
/*     */     }
/* 145 */     if (t == JsonToken.VALUE_EMBEDDED_OBJECT)
/*     */     {
/* 147 */       Object ob = p.getEmbeddedObject();
/* 148 */       if (ob == null) {
/* 149 */         return null;
/*     */       }
/* 151 */       if (this._valueClass.isAssignableFrom(ob.getClass())) {
/* 152 */         return (T)ob;
/*     */       }
/* 154 */       return (T)_deserializeEmbedded(ob, ctxt);
/*     */     }
/* 156 */     return (T)ctxt.handleUnexpectedToken(this._valueClass, p);
/*     */   }
/*     */   
/*     */   protected abstract T _deserialize(String paramString, DeserializationContext paramDeserializationContext) throws IOException;
/*     */   
/*     */   protected T _deserializeEmbedded(Object ob, DeserializationContext ctxt) throws IOException
/*     */   {
/* 163 */     ctxt.reportMappingException("Don't know how to convert embedded Object of type %s into %s", new Object[] { ob.getClass().getName(), this._valueClass.getName() });
/*     */     
/* 165 */     return null;
/*     */   }
/*     */   
/*     */   protected T _deserializeFromEmptyString() throws IOException {
/* 169 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public static class Std
/*     */     extends FromStringDeserializer<Object>
/*     */   {
/*     */     private static final long serialVersionUID = 1L;
/*     */     
/*     */     public static final int STD_FILE = 1;
/*     */     
/*     */     public static final int STD_URL = 2;
/*     */     
/*     */     public static final int STD_URI = 3;
/*     */     
/*     */     public static final int STD_CLASS = 4;
/*     */     
/*     */     public static final int STD_JAVA_TYPE = 5;
/*     */     
/*     */     public static final int STD_CURRENCY = 6;
/*     */     
/*     */     public static final int STD_PATTERN = 7;
/*     */     
/*     */     public static final int STD_LOCALE = 8;
/*     */     
/*     */     public static final int STD_CHARSET = 9;
/*     */     
/*     */     public static final int STD_TIME_ZONE = 10;
/*     */     
/*     */     public static final int STD_INET_ADDRESS = 11;
/*     */     public static final int STD_INET_SOCKET_ADDRESS = 12;
/*     */     public static final int STD_STRING_BUILDER = 13;
/*     */     protected final int _kind;
/*     */     
/*     */     protected Std(Class<?> valueType, int kind)
/*     */     {
/* 205 */       super();
/* 206 */       this._kind = kind;
/*     */     }
/*     */     
/*     */     protected Object _deserialize(String value, DeserializationContext ctxt)
/*     */       throws IOException
/*     */     {
/* 212 */       switch (this._kind) {
/*     */       case 1: 
/* 214 */         return new File(value);
/*     */       case 2: 
/* 216 */         return new URL(value);
/*     */       case 3: 
/* 218 */         return URI.create(value);
/*     */       case 4: 
/*     */         try {
/* 221 */           return ctxt.findClass(value);
/*     */         } catch (Exception e) {
/* 223 */           return ctxt.handleInstantiationProblem(this._valueClass, value, ClassUtil.getRootCause(e));
/*     */         }
/*     */       
/*     */       case 5: 
/* 227 */         return ctxt.getTypeFactory().constructFromCanonical(value);
/*     */       
/*     */       case 6: 
/* 230 */         return Currency.getInstance(value);
/*     */       
/*     */       case 7: 
/* 233 */         return Pattern.compile(value);
/*     */       
/*     */       case 8: 
/* 236 */         int ix = _firstHyphenOrUnderscore(value);
/* 237 */         if (ix < 0) {
/* 238 */           return new Locale(value);
/*     */         }
/* 240 */         String first = value.substring(0, ix);
/* 241 */         value = value.substring(ix + 1);
/* 242 */         ix = _firstHyphenOrUnderscore(value);
/* 243 */         if (ix < 0) {
/* 244 */           return new Locale(first, value);
/*     */         }
/* 246 */         String second = value.substring(0, ix);
/* 247 */         return new Locale(first, second, value.substring(ix + 1));
/*     */       
/*     */       case 9: 
/* 250 */         return Charset.forName(value);
/*     */       case 10: 
/* 252 */         return TimeZone.getTimeZone(value);
/*     */       case 11: 
/* 254 */         return InetAddress.getByName(value);
/*     */       case 12: 
/* 256 */         if (value.startsWith("["))
/*     */         {
/*     */ 
/* 259 */           int i = value.lastIndexOf(']');
/* 260 */           if (i == -1) {
/* 261 */             throw new InvalidFormatException(ctxt.getParser(), "Bracketed IPv6 address must contain closing bracket", value, InetSocketAddress.class);
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 266 */           int j = value.indexOf(':', i);
/* 267 */           int port = j > -1 ? Integer.parseInt(value.substring(j + 1)) : 0;
/* 268 */           return new InetSocketAddress(value.substring(0, i + 1), port);
/*     */         }
/* 270 */         int ix = value.indexOf(':');
/* 271 */         if ((ix >= 0) && (value.indexOf(':', ix + 1) < 0))
/*     */         {
/* 273 */           int port = Integer.parseInt(value.substring(ix + 1));
/* 274 */           return new InetSocketAddress(value.substring(0, ix), port);
/*     */         }
/*     */         
/* 277 */         return new InetSocketAddress(value, 0);
/*     */       case 13: 
/* 279 */         return new StringBuilder(value);
/*     */       }
/* 281 */       VersionUtil.throwInternal();
/* 282 */       return null;
/*     */     }
/*     */     
/*     */     protected Object _deserializeFromEmptyString()
/*     */       throws IOException
/*     */     {
/* 288 */       if (this._kind == 3) {
/* 289 */         return URI.create("");
/*     */       }
/*     */       
/* 292 */       if (this._kind == 8) {
/* 293 */         return Locale.ROOT;
/*     */       }
/* 295 */       if (this._kind == 13) {
/* 296 */         return new StringBuilder();
/*     */       }
/* 298 */       return super._deserializeFromEmptyString();
/*     */     }
/*     */     
/*     */     protected int _firstHyphenOrUnderscore(String str)
/*     */     {
/* 303 */       int i = 0; for (int end = str.length(); i < end; i++) {
/* 304 */         char c = str.charAt(i);
/* 305 */         if ((c == '_') || (c == '-')) {
/* 306 */           return i;
/*     */         }
/*     */       }
/* 309 */       return -1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\FromStringDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */