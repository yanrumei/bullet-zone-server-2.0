/*      */ package com.fasterxml.jackson.core;
/*      */ 
/*      */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*      */ import com.fasterxml.jackson.core.util.VersionUtil;
/*      */ import java.io.Closeable;
/*      */ import java.io.Flushable;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.util.concurrent.atomic.AtomicBoolean;
/*      */ import java.util.concurrent.atomic.AtomicInteger;
/*      */ import java.util.concurrent.atomic.AtomicLong;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class JsonGenerator
/*      */   implements Closeable, Flushable, Versioned
/*      */ {
/*      */   protected PrettyPrinter _cfgPrettyPrinter;
/*      */   public abstract JsonGenerator setCodec(ObjectCodec paramObjectCodec);
/*      */   
/*      */   public abstract ObjectCodec getCodec();
/*      */   
/*      */   public abstract Version version();
/*      */   
/*      */   public abstract JsonGenerator enable(Feature paramFeature);
/*      */   
/*      */   public abstract JsonGenerator disable(Feature paramFeature);
/*      */   
/*      */   public static enum Feature
/*      */   {
/*   49 */     AUTO_CLOSE_TARGET(true), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   61 */     AUTO_CLOSE_JSON_CONTENT(true), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   74 */     FLUSH_PASSED_TO_STREAM(true), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   87 */     QUOTE_FIELD_NAMES(true), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  101 */     QUOTE_NON_NUMERIC_NUMBERS(true), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  118 */     WRITE_NUMBERS_AS_STRINGS(false), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  130 */     WRITE_BIGDECIMAL_AS_PLAIN(false), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  147 */     ESCAPE_NON_ASCII(false), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  190 */     STRICT_DUPLICATE_DETECTION(false), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  212 */     IGNORE_UNKNOWN(false);
/*      */     
/*      */ 
/*      */ 
/*      */     private final boolean _defaultState;
/*      */     
/*      */ 
/*      */     private final int _mask;
/*      */     
/*      */ 
/*      */     public static int collectDefaults()
/*      */     {
/*  224 */       int flags = 0;
/*  225 */       for (Feature f : values()) {
/*  226 */         if (f.enabledByDefault()) {
/*  227 */           flags |= f.getMask();
/*      */         }
/*      */       }
/*  230 */       return flags;
/*      */     }
/*      */     
/*      */     private Feature(boolean defaultState) {
/*  234 */       this._defaultState = defaultState;
/*  235 */       this._mask = (1 << ordinal());
/*      */     }
/*      */     
/*  238 */     public boolean enabledByDefault() { return this._defaultState; }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  243 */     public boolean enabledIn(int flags) { return (flags & this._mask) != 0; }
/*      */     
/*  245 */     public int getMask() { return this._mask; }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final JsonGenerator configure(Feature f, boolean state)
/*      */   {
/*  320 */     if (state) enable(f); else disable(f);
/*  321 */     return this;
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
/*      */   public abstract boolean isEnabled(Feature paramFeature);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract int getFeatureMask();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public abstract JsonGenerator setFeatureMask(int paramInt);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonGenerator overrideStdFeatures(int values, int mask)
/*      */   {
/*  371 */     int oldState = getFeatureMask();
/*  372 */     int newState = oldState & (mask ^ 0xFFFFFFFF) | values & mask;
/*  373 */     return setFeatureMask(newState);
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
/*  385 */     return 0;
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
/*      */   public JsonGenerator overrideFormatFeatures(int values, int mask)
/*      */   {
/*  402 */     throw new IllegalArgumentException("No FormatFeatures defined for generator of type " + getClass().getName());
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
/*      */   public void setSchema(FormatSchema schema)
/*      */   {
/*  432 */     throw new UnsupportedOperationException("Generator of type " + getClass().getName() + " does not support schema of type '" + schema.getSchemaType() + "'");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FormatSchema getSchema()
/*      */   {
/*  442 */     return null;
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
/*      */   public JsonGenerator setPrettyPrinter(PrettyPrinter pp)
/*      */   {
/*  462 */     this._cfgPrettyPrinter = pp;
/*  463 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PrettyPrinter getPrettyPrinter()
/*      */   {
/*  473 */     return this._cfgPrettyPrinter;
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
/*      */   public abstract JsonGenerator useDefaultPrettyPrinter();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonGenerator setHighestNonEscapedChar(int charCode)
/*      */   {
/*  506 */     return this;
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
/*      */   public int getHighestEscapedChar()
/*      */   {
/*  520 */     return 0;
/*      */   }
/*      */   
/*      */ 
/*      */   public CharacterEscapes getCharacterEscapes()
/*      */   {
/*  526 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonGenerator setCharacterEscapes(CharacterEscapes esc)
/*      */   {
/*  534 */     return this;
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
/*      */   public JsonGenerator setRootValueSeparator(SerializableString sep)
/*      */   {
/*  548 */     throw new UnsupportedOperationException();
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
/*      */   public Object getOutputTarget()
/*      */   {
/*  573 */     return null;
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
/*      */   public int getOutputBuffered()
/*      */   {
/*  595 */     return -1;
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
/*      */   public Object getCurrentValue()
/*      */   {
/*  612 */     JsonStreamContext ctxt = getOutputContext();
/*  613 */     return ctxt == null ? null : ctxt.getCurrentValue();
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
/*  625 */     JsonStreamContext ctxt = getOutputContext();
/*  626 */     if (ctxt != null) {
/*  627 */       ctxt.setCurrentValue(v);
/*      */     }
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
/*      */   public boolean canUseSchema(FormatSchema schema)
/*      */   {
/*  645 */     return false;
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
/*      */   public boolean canWriteObjectId()
/*      */   {
/*  661 */     return false;
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
/*      */   public boolean canWriteTypeId()
/*      */   {
/*  677 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canWriteBinaryNatively()
/*      */   {
/*  689 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canOmitFields()
/*      */   {
/*  699 */     return true;
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
/*      */   public boolean canWriteFormattedNumbers()
/*      */   {
/*  713 */     return false;
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
/*      */   public abstract void writeStartArray()
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
/*      */   public void writeStartArray(int size)
/*      */     throws IOException
/*      */   {
/*  748 */     writeStartArray();
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
/*      */   public abstract void writeEndArray()
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
/*      */   public abstract void writeStartObject()
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
/*      */   public void writeStartObject(Object forValue)
/*      */     throws IOException
/*      */   {
/*  788 */     writeStartObject();
/*  789 */     setCurrentValue(forValue);
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
/*      */   public abstract void writeEndObject()
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
/*      */   public abstract void writeFieldName(String paramString)
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
/*      */   public abstract void writeFieldName(SerializableString paramSerializableString)
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
/*      */   public void writeFieldId(long id)
/*      */     throws IOException
/*      */   {
/*  839 */     writeFieldName(Long.toString(id));
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
/*      */   public void writeArray(int[] array, int offset, int length)
/*      */     throws IOException
/*      */   {
/*  861 */     if (array == null) {
/*  862 */       throw new IllegalArgumentException("null array");
/*      */     }
/*  864 */     _verifyOffsets(array.length, offset, length);
/*  865 */     writeStartArray();
/*  866 */     int i = offset; for (int end = offset + length; i < end; i++) {
/*  867 */       writeNumber(array[i]);
/*      */     }
/*  869 */     writeEndArray();
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
/*      */   public void writeArray(long[] array, int offset, int length)
/*      */     throws IOException
/*      */   {
/*  885 */     if (array == null) {
/*  886 */       throw new IllegalArgumentException("null array");
/*      */     }
/*  888 */     _verifyOffsets(array.length, offset, length);
/*  889 */     writeStartArray();
/*  890 */     int i = offset; for (int end = offset + length; i < end; i++) {
/*  891 */       writeNumber(array[i]);
/*      */     }
/*  893 */     writeEndArray();
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
/*      */   public void writeArray(double[] array, int offset, int length)
/*      */     throws IOException
/*      */   {
/*  909 */     if (array == null) {
/*  910 */       throw new IllegalArgumentException("null array");
/*      */     }
/*  912 */     _verifyOffsets(array.length, offset, length);
/*  913 */     writeStartArray();
/*  914 */     int i = offset; for (int end = offset + length; i < end; i++) {
/*  915 */       writeNumber(array[i]);
/*      */     }
/*  917 */     writeEndArray();
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
/*      */   public abstract void writeString(String paramString)
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
/*      */   public abstract void writeString(char[] paramArrayOfChar, int paramInt1, int paramInt2)
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
/*      */   public abstract void writeString(SerializableString paramSerializableString)
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
/*      */   public abstract void writeRawUTF8String(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
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
/*      */   public abstract void writeUTF8String(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
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
/*      */   public abstract void writeRaw(String paramString)
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
/*      */   public abstract void writeRaw(String paramString, int paramInt1, int paramInt2)
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
/*      */   public abstract void writeRaw(char[] paramArrayOfChar, int paramInt1, int paramInt2)
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
/*      */   public abstract void writeRaw(char paramChar)
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
/*      */   public void writeRaw(SerializableString raw)
/*      */     throws IOException
/*      */   {
/* 1076 */     writeRaw(raw.getValue());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void writeRawValue(String paramString)
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void writeRawValue(String paramString, int paramInt1, int paramInt2)
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */   public abstract void writeRawValue(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */     throws IOException;
/*      */   
/*      */ 
/*      */ 
/*      */   public void writeRawValue(SerializableString raw)
/*      */     throws IOException
/*      */   {
/* 1101 */     writeRawValue(raw.getValue());
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
/*      */   public abstract void writeBinary(Base64Variant paramBase64Variant, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
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
/*      */   public void writeBinary(byte[] data, int offset, int len)
/*      */     throws IOException
/*      */   {
/* 1134 */     writeBinary(Base64Variants.getDefaultVariant(), data, offset, len);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeBinary(byte[] data)
/*      */     throws IOException
/*      */   {
/* 1144 */     writeBinary(Base64Variants.getDefaultVariant(), data, 0, data.length);
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
/*      */   public int writeBinary(InputStream data, int dataLength)
/*      */     throws IOException
/*      */   {
/* 1162 */     return writeBinary(Base64Variants.getDefaultVariant(), data, dataLength);
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
/*      */   public abstract int writeBinary(Base64Variant paramBase64Variant, InputStream paramInputStream, int paramInt)
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
/*      */ 
/*      */   public void writeNumber(short v)
/*      */     throws IOException
/*      */   {
/* 1206 */     writeNumber(v);
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
/*      */   public abstract void writeNumber(int paramInt)
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
/*      */   public abstract void writeNumber(long paramLong)
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
/*      */   public abstract void writeNumber(BigInteger paramBigInteger)
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
/*      */   public abstract void writeNumber(double paramDouble)
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
/*      */   public abstract void writeNumber(float paramFloat)
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
/*      */   public abstract void writeNumber(BigDecimal paramBigDecimal)
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
/*      */   public abstract void writeNumber(String paramString)
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
/*      */   public abstract void writeBoolean(boolean paramBoolean)
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
/*      */   public abstract void writeNull()
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
/*      */   public void writeEmbeddedObject(Object object)
/*      */     throws IOException
/*      */   {
/* 1330 */     if (object == null) {
/* 1331 */       writeNull();
/* 1332 */       return;
/*      */     }
/* 1334 */     if ((object instanceof byte[])) {
/* 1335 */       writeBinary((byte[])object);
/* 1336 */       return;
/*      */     }
/* 1338 */     throw new JsonGenerationException("No native support for writing embedded objects of type " + object.getClass().getName(), this);
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
/*      */   public void writeObjectId(Object id)
/*      */     throws IOException
/*      */   {
/* 1361 */     throw new JsonGenerationException("No native support for writing Object Ids", this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeObjectRef(Object id)
/*      */     throws IOException
/*      */   {
/* 1374 */     throw new JsonGenerationException("No native support for writing Object Ids", this);
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
/*      */   public void writeTypeId(Object id)
/*      */     throws IOException
/*      */   {
/* 1389 */     throw new JsonGenerationException("No native support for writing Type Ids", this);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void writeObject(Object paramObject)
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void writeTree(TreeNode paramTreeNode)
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeStringField(String fieldName, String value)
/*      */     throws IOException
/*      */   {
/* 1505 */     writeFieldName(fieldName);
/* 1506 */     writeString(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeBooleanField(String fieldName, boolean value)
/*      */     throws IOException
/*      */   {
/* 1518 */     writeFieldName(fieldName);
/* 1519 */     writeBoolean(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeNullField(String fieldName)
/*      */     throws IOException
/*      */   {
/* 1531 */     writeFieldName(fieldName);
/* 1532 */     writeNull();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeNumberField(String fieldName, int value)
/*      */     throws IOException
/*      */   {
/* 1544 */     writeFieldName(fieldName);
/* 1545 */     writeNumber(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeNumberField(String fieldName, long value)
/*      */     throws IOException
/*      */   {
/* 1557 */     writeFieldName(fieldName);
/* 1558 */     writeNumber(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeNumberField(String fieldName, double value)
/*      */     throws IOException
/*      */   {
/* 1570 */     writeFieldName(fieldName);
/* 1571 */     writeNumber(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeNumberField(String fieldName, float value)
/*      */     throws IOException
/*      */   {
/* 1583 */     writeFieldName(fieldName);
/* 1584 */     writeNumber(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeNumberField(String fieldName, BigDecimal value)
/*      */     throws IOException
/*      */   {
/* 1597 */     writeFieldName(fieldName);
/* 1598 */     writeNumber(value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeBinaryField(String fieldName, byte[] data)
/*      */     throws IOException
/*      */   {
/* 1611 */     writeFieldName(fieldName);
/* 1612 */     writeBinary(data);
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
/*      */   public final void writeArrayFieldStart(String fieldName)
/*      */     throws IOException
/*      */   {
/* 1629 */     writeFieldName(fieldName);
/* 1630 */     writeStartArray();
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
/*      */   public final void writeObjectFieldStart(String fieldName)
/*      */     throws IOException
/*      */   {
/* 1647 */     writeFieldName(fieldName);
/* 1648 */     writeStartObject();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final void writeObjectField(String fieldName, Object pojo)
/*      */     throws IOException
/*      */   {
/* 1661 */     writeFieldName(fieldName);
/* 1662 */     writeObject(pojo);
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
/*      */   public void writeOmittedField(String fieldName)
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
/*      */   public void copyCurrentEvent(JsonParser p)
/*      */     throws IOException
/*      */   {
/* 1694 */     JsonToken t = p.currentToken();
/*      */     
/* 1696 */     if (t == null) {
/* 1697 */       _reportError("No current event to copy");
/*      */     }
/* 1699 */     switch (t.id()) {
/*      */     case -1: 
/* 1701 */       _reportError("No current event to copy");
/* 1702 */       break;
/*      */     case 1: 
/* 1704 */       writeStartObject();
/* 1705 */       break;
/*      */     case 2: 
/* 1707 */       writeEndObject();
/* 1708 */       break;
/*      */     case 3: 
/* 1710 */       writeStartArray();
/* 1711 */       break;
/*      */     case 4: 
/* 1713 */       writeEndArray();
/* 1714 */       break;
/*      */     case 5: 
/* 1716 */       writeFieldName(p.getCurrentName());
/* 1717 */       break;
/*      */     case 6: 
/* 1719 */       if (p.hasTextCharacters()) {
/* 1720 */         writeString(p.getTextCharacters(), p.getTextOffset(), p.getTextLength());
/*      */       } else {
/* 1722 */         writeString(p.getText());
/*      */       }
/* 1724 */       break;
/*      */     
/*      */     case 7: 
/* 1727 */       JsonParser.NumberType n = p.getNumberType();
/* 1728 */       if (n == JsonParser.NumberType.INT) {
/* 1729 */         writeNumber(p.getIntValue());
/* 1730 */       } else if (n == JsonParser.NumberType.BIG_INTEGER) {
/* 1731 */         writeNumber(p.getBigIntegerValue());
/*      */       } else {
/* 1733 */         writeNumber(p.getLongValue());
/*      */       }
/* 1735 */       break;
/*      */     
/*      */ 
/*      */     case 8: 
/* 1739 */       JsonParser.NumberType n = p.getNumberType();
/* 1740 */       if (n == JsonParser.NumberType.BIG_DECIMAL) {
/* 1741 */         writeNumber(p.getDecimalValue());
/* 1742 */       } else if (n == JsonParser.NumberType.FLOAT) {
/* 1743 */         writeNumber(p.getFloatValue());
/*      */       } else {
/* 1745 */         writeNumber(p.getDoubleValue());
/*      */       }
/* 1747 */       break;
/*      */     
/*      */     case 9: 
/* 1750 */       writeBoolean(true);
/* 1751 */       break;
/*      */     case 10: 
/* 1753 */       writeBoolean(false);
/* 1754 */       break;
/*      */     case 11: 
/* 1756 */       writeNull();
/* 1757 */       break;
/*      */     case 12: 
/* 1759 */       writeObject(p.getEmbeddedObject());
/* 1760 */       break;
/*      */     case 0: default: 
/* 1762 */       _throwInternal();
/*      */     }
/*      */     
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
/*      */ 
/*      */ 
/*      */ 
/*      */   public void copyCurrentStructure(JsonParser p)
/*      */     throws IOException
/*      */   {
/* 1798 */     JsonToken t = p.currentToken();
/* 1799 */     if (t == null) {
/* 1800 */       _reportError("No current event to copy");
/*      */     }
/*      */     
/* 1803 */     int id = t.id();
/* 1804 */     if (id == 5) {
/* 1805 */       writeFieldName(p.getCurrentName());
/* 1806 */       t = p.nextToken();
/* 1807 */       id = t.id();
/*      */     }
/*      */     
/* 1810 */     switch (id) {
/*      */     case 1: 
/* 1812 */       writeStartObject();
/* 1813 */       while (p.nextToken() != JsonToken.END_OBJECT) {
/* 1814 */         copyCurrentStructure(p);
/*      */       }
/* 1816 */       writeEndObject();
/* 1817 */       break;
/*      */     case 3: 
/* 1819 */       writeStartArray();
/* 1820 */       while (p.nextToken() != JsonToken.END_ARRAY) {
/* 1821 */         copyCurrentStructure(p);
/*      */       }
/* 1823 */       writeEndArray();
/* 1824 */       break;
/*      */     default: 
/* 1826 */       copyCurrentEvent(p);
/*      */     }
/*      */     
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
/*      */   public abstract JsonStreamContext getOutputContext();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void flush()
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
/*      */   protected void _reportError(String msg)
/*      */     throws JsonGenerationException
/*      */   {
/* 1897 */     throw new JsonGenerationException(msg, this);
/*      */   }
/*      */   
/*      */   protected final void _throwInternal() {}
/*      */   
/*      */   protected void _reportUnsupportedOperation() {
/* 1903 */     throw new UnsupportedOperationException("Operation not supported by generator of type " + getClass().getName());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _verifyOffsets(int arrayLength, int offset, int length)
/*      */   {
/* 1911 */     if ((offset < 0) || (offset + length > arrayLength)) {
/* 1912 */       throw new IllegalArgumentException(String.format("invalid argument(s) (offset=%d, length=%d) for input array of %d element", new Object[] { Integer.valueOf(offset), Integer.valueOf(length), Integer.valueOf(arrayLength) }));
/*      */     }
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
/*      */   protected void _writeSimpleObject(Object value)
/*      */     throws IOException
/*      */   {
/* 1931 */     if (value == null) {
/* 1932 */       writeNull();
/* 1933 */       return;
/*      */     }
/* 1935 */     if ((value instanceof String)) {
/* 1936 */       writeString((String)value);
/* 1937 */       return;
/*      */     }
/* 1939 */     if ((value instanceof Number)) {
/* 1940 */       Number n = (Number)value;
/* 1941 */       if ((n instanceof Integer)) {
/* 1942 */         writeNumber(n.intValue());
/* 1943 */         return; }
/* 1944 */       if ((n instanceof Long)) {
/* 1945 */         writeNumber(n.longValue());
/* 1946 */         return; }
/* 1947 */       if ((n instanceof Double)) {
/* 1948 */         writeNumber(n.doubleValue());
/* 1949 */         return; }
/* 1950 */       if ((n instanceof Float)) {
/* 1951 */         writeNumber(n.floatValue());
/* 1952 */         return; }
/* 1953 */       if ((n instanceof Short)) {
/* 1954 */         writeNumber(n.shortValue());
/* 1955 */         return; }
/* 1956 */       if ((n instanceof Byte)) {
/* 1957 */         writeNumber((short)n.byteValue());
/* 1958 */         return; }
/* 1959 */       if ((n instanceof BigInteger)) {
/* 1960 */         writeNumber((BigInteger)n);
/* 1961 */         return; }
/* 1962 */       if ((n instanceof BigDecimal)) {
/* 1963 */         writeNumber((BigDecimal)n);
/* 1964 */         return;
/*      */       }
/*      */       
/* 1967 */       if ((n instanceof AtomicInteger)) {
/* 1968 */         writeNumber(((AtomicInteger)n).get());
/* 1969 */         return; }
/* 1970 */       if ((n instanceof AtomicLong)) {
/* 1971 */         writeNumber(((AtomicLong)n).get());
/* 1972 */         return;
/*      */       }
/* 1974 */     } else { if ((value instanceof byte[])) {
/* 1975 */         writeBinary((byte[])value);
/* 1976 */         return; }
/* 1977 */       if ((value instanceof Boolean)) {
/* 1978 */         writeBoolean(((Boolean)value).booleanValue());
/* 1979 */         return; }
/* 1980 */       if ((value instanceof AtomicBoolean)) {
/* 1981 */         writeBoolean(((AtomicBoolean)value).get());
/* 1982 */         return;
/*      */       } }
/* 1984 */     throw new IllegalStateException("No ObjectCodec defined for the generator, can only serialize simple wrapper types (type passed " + value.getClass().getName() + ")");
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\JsonGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */