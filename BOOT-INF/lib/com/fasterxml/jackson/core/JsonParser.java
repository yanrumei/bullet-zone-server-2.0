/*      */ package com.fasterxml.jackson.core;
/*      */ 
/*      */ import com.fasterxml.jackson.core.type.TypeReference;
/*      */ import com.fasterxml.jackson.core.util.RequestPayload;
/*      */ import java.io.Closeable;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Writer;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.Iterator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class JsonParser
/*      */   implements Closeable, Versioned
/*      */ {
/*      */   private static final int MIN_BYTE_I = -128;
/*      */   private static final int MAX_BYTE_I = 255;
/*      */   private static final int MIN_SHORT_I = -32768;
/*      */   private static final int MAX_SHORT_I = 32767;
/*      */   protected int _features;
/*      */   protected transient RequestPayload _requestPayload;
/*      */   protected JsonParser() {}
/*      */   
/*      */   public static enum NumberType
/*      */   {
/*   38 */     INT,  LONG,  BIG_INTEGER,  FLOAT,  DOUBLE,  BIG_DECIMAL;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private NumberType() {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static enum Feature
/*      */   {
/*   60 */     AUTO_CLOSE_SOURCE(true), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   76 */     ALLOW_COMMENTS(false), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   90 */     ALLOW_YAML_COMMENTS(false), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  101 */     ALLOW_UNQUOTED_FIELD_NAMES(false), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  114 */     ALLOW_SINGLE_QUOTES(false), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  127 */     ALLOW_UNQUOTED_CONTROL_CHARS(false), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  138 */     ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER(false), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  150 */     ALLOW_NUMERIC_LEADING_ZEROS(false), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  170 */     ALLOW_NON_NUMERIC_NUMBERS(false), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  188 */     STRICT_DUPLICATE_DETECTION(false), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  212 */     IGNORE_UNDEFINED(false), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  230 */     ALLOW_MISSING_VALUES(false);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private final boolean _defaultState;
/*      */     
/*      */ 
/*      */ 
/*      */     private final int _mask;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public static int collectDefaults()
/*      */     {
/*  246 */       int flags = 0;
/*  247 */       for (Feature f : values()) {
/*  248 */         if (f.enabledByDefault()) {
/*  249 */           flags |= f.getMask();
/*      */         }
/*      */       }
/*  252 */       return flags;
/*      */     }
/*      */     
/*      */     private Feature(boolean defaultState) {
/*  256 */       this._mask = (1 << ordinal());
/*  257 */       this._defaultState = defaultState;
/*      */     }
/*      */     
/*  260 */     public boolean enabledByDefault() { return this._defaultState; }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  265 */     public boolean enabledIn(int flags) { return (flags & this._mask) != 0; }
/*      */     
/*  267 */     public int getMask() { return this._mask; }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonParser(int features)
/*      */   {
/*  297 */     this._features = features;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract ObjectCodec getCodec();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void setCodec(ObjectCodec paramObjectCodec);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getInputSource()
/*      */   {
/*  328 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getCurrentValue()
/*      */   {
/*  344 */     JsonStreamContext ctxt = getParsingContext();
/*  345 */     return ctxt == null ? null : ctxt.getCurrentValue();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCurrentValue(Object v)
/*      */   {
/*  357 */     JsonStreamContext ctxt = getParsingContext();
/*  358 */     if (ctxt != null) {
/*  359 */       ctxt.setCurrentValue(v);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRequestPayloadOnError(RequestPayload payload)
/*      */   {
/*  369 */     this._requestPayload = payload;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRequestPayloadOnError(byte[] payload, String charset)
/*      */   {
/*  378 */     this._requestPayload = (payload == null ? null : new RequestPayload(payload, charset));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setRequestPayloadOnError(String payload)
/*      */   {
/*  387 */     this._requestPayload = (payload == null ? null : new RequestPayload(payload));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSchema(FormatSchema schema)
/*      */   {
/*  411 */     throw new UnsupportedOperationException("Parser of type " + getClass().getName() + " does not support schema of type '" + schema.getSchemaType() + "'");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FormatSchema getSchema()
/*      */   {
/*  421 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canUseSchema(FormatSchema schema)
/*      */   {
/*  431 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean requiresCustomCodec()
/*      */   {
/*  452 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract Version version();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void close()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int releaseBuffered(OutputStream out)
/*      */     throws IOException
/*      */   {
/*  512 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int releaseBuffered(Writer w)
/*      */     throws IOException
/*      */   {
/*  530 */     return -1;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser enable(Feature f)
/*      */   {
/*  543 */     this._features |= f.getMask();
/*  544 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser disable(Feature f)
/*      */   {
/*  552 */     this._features &= (f.getMask() ^ 0xFFFFFFFF);
/*  553 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser configure(Feature f, boolean state)
/*      */   {
/*  561 */     if (state) enable(f); else disable(f);
/*  562 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean isEnabled(Feature f)
/*      */   {
/*  568 */     return f.enabledIn(this._features);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getFeatureMask()
/*      */   {
/*  577 */     return this._features;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonParser setFeatureMask(int mask)
/*      */   {
/*  590 */     this._features = mask;
/*  591 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser overrideStdFeatures(int values, int mask)
/*      */   {
/*  610 */     int newState = this._features & (mask ^ 0xFFFFFFFF) | values & mask;
/*  611 */     return setFeatureMask(newState);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getFormatFeatures()
/*      */   {
/*  623 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser overrideFormatFeatures(int values, int mask)
/*      */   {
/*  640 */     throw new IllegalArgumentException("No FormatFeatures defined for parser of type " + getClass().getName());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract JsonToken nextToken()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract JsonToken nextValue()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean nextFieldName(SerializableString str)
/*      */     throws IOException
/*      */   {
/*  697 */     return (nextToken() == JsonToken.FIELD_NAME) && (str.getValue().equals(getCurrentName()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String nextFieldName()
/*      */     throws IOException
/*      */   {
/*  708 */     return nextToken() == JsonToken.FIELD_NAME ? getCurrentName() : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String nextTextValue()
/*      */     throws IOException
/*      */   {
/*  723 */     return nextToken() == JsonToken.VALUE_STRING ? getText() : null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int nextIntValue(int defaultValue)
/*      */     throws IOException
/*      */   {
/*  738 */     return nextToken() == JsonToken.VALUE_NUMBER_INT ? getIntValue() : defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long nextLongValue(long defaultValue)
/*      */     throws IOException
/*      */   {
/*  753 */     return nextToken() == JsonToken.VALUE_NUMBER_INT ? getLongValue() : defaultValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Boolean nextBooleanValue()
/*      */     throws IOException
/*      */   {
/*  771 */     JsonToken t = nextToken();
/*  772 */     if (t == JsonToken.VALUE_TRUE) return Boolean.TRUE;
/*  773 */     if (t == JsonToken.VALUE_FALSE) return Boolean.FALSE;
/*  774 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract JsonParser skipChildren()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void finishToken()
/*      */     throws IOException
/*      */   {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract boolean isClosed();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonToken currentToken()
/*      */   {
/*  841 */     return getCurrentToken();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int currentTokenId()
/*      */   {
/*  858 */     return getCurrentTokenId();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract JsonToken getCurrentToken();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract int getCurrentTokenId();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract boolean hasCurrentToken();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract boolean hasTokenId(int paramInt);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract boolean hasToken(JsonToken paramJsonToken);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract String getCurrentName()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract JsonStreamContext getParsingContext();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract JsonLocation getTokenLocation();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract JsonLocation getCurrentLocation();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isExpectedStartArrayToken()
/*      */   {
/*  967 */     return currentToken() == JsonToken.START_ARRAY;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isExpectedStartObjectToken()
/*      */   {
/*  975 */     return currentToken() == JsonToken.START_OBJECT;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void clearCurrentToken();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract JsonToken getLastClearedToken();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void overrideCurrentName(String paramString);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract String getText()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getText(Writer writer)
/*      */     throws IOException, UnsupportedOperationException
/*      */   {
/* 1050 */     String str = getText();
/* 1051 */     if (str == null) {
/* 1052 */       return 0;
/*      */     }
/* 1054 */     writer.write(str);
/* 1055 */     return str.length();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract char[] getTextCharacters()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract int getTextLength()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract int getTextOffset()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract boolean hasTextCharacters();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract Number getNumberValue()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract NumberType getNumberType()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public byte getByteValue()
/*      */     throws IOException
/*      */   {
/* 1160 */     int value = getIntValue();
/*      */     
/*      */ 
/*      */ 
/* 1164 */     if ((value < -128) || (value > 255)) {
/* 1165 */       throw _constructError("Numeric value (" + getText() + ") out of range of Java byte");
/*      */     }
/* 1167 */     return (byte)value;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public short getShortValue()
/*      */     throws IOException
/*      */   {
/* 1185 */     int value = getIntValue();
/* 1186 */     if ((value < 32768) || (value > 32767)) {
/* 1187 */       throw _constructError("Numeric value (" + getText() + ") out of range of Java short");
/*      */     }
/* 1189 */     return (short)value;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract int getIntValue()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract long getLongValue()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract BigInteger getBigIntegerValue()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract float getFloatValue()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract double getDoubleValue()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract BigDecimal getDecimalValue()
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getBooleanValue()
/*      */     throws IOException
/*      */   {
/* 1288 */     JsonToken t = currentToken();
/* 1289 */     if (t == JsonToken.VALUE_TRUE) return true;
/* 1290 */     if (t == JsonToken.VALUE_FALSE) return false;
/* 1291 */     throw new JsonParseException(this, String.format("Current token (%s) not of boolean type", new Object[] { t })).withRequestPayload(this._requestPayload);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getEmbeddedObject()
/*      */     throws IOException
/*      */   {
/* 1308 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract byte[] getBinaryValue(Base64Variant paramBase64Variant)
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public byte[] getBinaryValue()
/*      */     throws IOException
/*      */   {
/* 1346 */     return getBinaryValue(Base64Variants.getDefaultVariant());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int readBinaryValue(OutputStream out)
/*      */     throws IOException
/*      */   {
/* 1364 */     return readBinaryValue(Base64Variants.getDefaultVariant(), out);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int readBinaryValue(Base64Variant bv, OutputStream out)
/*      */     throws IOException
/*      */   {
/* 1379 */     _reportUnsupportedOperation();
/* 1380 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getValueAsInt()
/*      */     throws IOException
/*      */   {
/* 1401 */     return getValueAsInt(0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public int getValueAsInt(int def)
/*      */     throws IOException
/*      */   {
/* 1415 */     return def;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getValueAsLong()
/*      */     throws IOException
/*      */   {
/* 1429 */     return getValueAsLong(0L);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public long getValueAsLong(long def)
/*      */     throws IOException
/*      */   {
/* 1444 */     return def;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getValueAsDouble()
/*      */     throws IOException
/*      */   {
/* 1459 */     return getValueAsDouble(0.0D);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public double getValueAsDouble(double def)
/*      */     throws IOException
/*      */   {
/* 1474 */     return def;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getValueAsBoolean()
/*      */     throws IOException
/*      */   {
/* 1489 */     return getValueAsBoolean(false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean getValueAsBoolean(boolean def)
/*      */     throws IOException
/*      */   {
/* 1504 */     return def;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getValueAsString()
/*      */     throws IOException
/*      */   {
/* 1519 */     return getValueAsString(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract String getValueAsString(String paramString)
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canReadObjectId()
/*      */   {
/* 1553 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canReadTypeId()
/*      */   {
/* 1567 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getObjectId()
/*      */     throws IOException
/*      */   {
/* 1582 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getTypeId()
/*      */     throws IOException
/*      */   {
/* 1597 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValueAs(Class<T> valueType)
/*      */     throws IOException
/*      */   {
/* 1627 */     return (T)_codec().readValue(this, valueType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValueAs(TypeReference<?> valueTypeRef)
/*      */     throws IOException
/*      */   {
/* 1650 */     return (T)_codec().readValue(this, valueTypeRef);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T> Iterator<T> readValuesAs(Class<T> valueType)
/*      */     throws IOException
/*      */   {
/* 1658 */     return _codec().readValues(this, valueType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T> Iterator<T> readValuesAs(TypeReference<?> valueTypeRef)
/*      */     throws IOException
/*      */   {
/* 1666 */     return _codec().readValues(this, valueTypeRef);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends TreeNode> T readValueAsTree()
/*      */     throws IOException
/*      */   {
/* 1680 */     return _codec().readTree(this);
/*      */   }
/*      */   
/*      */   protected ObjectCodec _codec() {
/* 1684 */     ObjectCodec c = getCodec();
/* 1685 */     if (c == null) {
/* 1686 */       throw new IllegalStateException("No ObjectCodec defined for parser, needed for deserialization");
/*      */     }
/* 1688 */     return c;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonParseException _constructError(String msg)
/*      */   {
/* 1702 */     return new JsonParseException(this, msg).withRequestPayload(this._requestPayload);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _reportUnsupportedOperation()
/*      */   {
/* 1713 */     throw new UnsupportedOperationException("Operation not supported by parser of type " + getClass().getName());
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\JsonParser.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */