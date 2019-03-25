/*     */ package com.fasterxml.jackson.core.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.FormatSchema;
/*     */ import com.fasterxml.jackson.core.JsonLocation;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*     */ import com.fasterxml.jackson.core.JsonParser.NumberType;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.core.ObjectCodec;
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Writer;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ public class JsonParserDelegate
/*     */   extends JsonParser
/*     */ {
/*     */   protected JsonParser delegate;
/*     */   
/*     */   public JsonParserDelegate(JsonParser d)
/*     */   {
/*  26 */     this.delegate = d;
/*     */   }
/*     */   
/*     */   public Object getCurrentValue()
/*     */   {
/*  31 */     return this.delegate.getCurrentValue();
/*     */   }
/*     */   
/*     */   public void setCurrentValue(Object v)
/*     */   {
/*  36 */     this.delegate.setCurrentValue(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  45 */   public void setCodec(ObjectCodec c) { this.delegate.setCodec(c); }
/*  46 */   public ObjectCodec getCodec() { return this.delegate.getCodec(); }
/*     */   
/*     */   public JsonParser enable(JsonParser.Feature f)
/*     */   {
/*  50 */     this.delegate.enable(f);
/*  51 */     return this;
/*     */   }
/*     */   
/*     */   public JsonParser disable(JsonParser.Feature f)
/*     */   {
/*  56 */     this.delegate.disable(f);
/*  57 */     return this;
/*     */   }
/*     */   
/*  60 */   public boolean isEnabled(JsonParser.Feature f) { return this.delegate.isEnabled(f); }
/*  61 */   public int getFeatureMask() { return this.delegate.getFeatureMask(); }
/*     */   
/*     */   @Deprecated
/*     */   public JsonParser setFeatureMask(int mask)
/*     */   {
/*  66 */     this.delegate.setFeatureMask(mask);
/*  67 */     return this;
/*     */   }
/*     */   
/*     */   public JsonParser overrideStdFeatures(int values, int mask)
/*     */   {
/*  72 */     this.delegate.overrideStdFeatures(values, mask);
/*  73 */     return this;
/*     */   }
/*     */   
/*     */   public JsonParser overrideFormatFeatures(int values, int mask)
/*     */   {
/*  78 */     this.delegate.overrideFormatFeatures(values, mask);
/*  79 */     return this;
/*     */   }
/*     */   
/*  82 */   public FormatSchema getSchema() { return this.delegate.getSchema(); }
/*  83 */   public void setSchema(FormatSchema schema) { this.delegate.setSchema(schema); }
/*  84 */   public boolean canUseSchema(FormatSchema schema) { return this.delegate.canUseSchema(schema); }
/*  85 */   public Version version() { return this.delegate.version(); }
/*  86 */   public Object getInputSource() { return this.delegate.getInputSource(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean requiresCustomCodec()
/*     */   {
/*  94 */     return this.delegate.requiresCustomCodec();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */   public void close()
/* 102 */     throws IOException { this.delegate.close(); }
/* 103 */   public boolean isClosed() { return this.delegate.isClosed(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 111 */   public JsonToken currentToken() { return this.delegate.currentToken(); }
/* 112 */   public int currentTokenId() { return this.delegate.currentTokenId(); }
/*     */   
/* 114 */   public JsonToken getCurrentToken() { return this.delegate.getCurrentToken(); }
/* 115 */   public int getCurrentTokenId() { return this.delegate.getCurrentTokenId(); }
/* 116 */   public boolean hasCurrentToken() { return this.delegate.hasCurrentToken(); }
/* 117 */   public boolean hasTokenId(int id) { return this.delegate.hasTokenId(id); }
/* 118 */   public boolean hasToken(JsonToken t) { return this.delegate.hasToken(t); }
/*     */   
/* 120 */   public String getCurrentName() throws IOException { return this.delegate.getCurrentName(); }
/* 121 */   public JsonLocation getCurrentLocation() { return this.delegate.getCurrentLocation(); }
/* 122 */   public JsonStreamContext getParsingContext() { return this.delegate.getParsingContext(); }
/* 123 */   public boolean isExpectedStartArrayToken() { return this.delegate.isExpectedStartArrayToken(); }
/* 124 */   public boolean isExpectedStartObjectToken() { return this.delegate.isExpectedStartObjectToken(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 132 */   public void clearCurrentToken() { this.delegate.clearCurrentToken(); }
/* 133 */   public JsonToken getLastClearedToken() { return this.delegate.getLastClearedToken(); }
/* 134 */   public void overrideCurrentName(String name) { this.delegate.overrideCurrentName(name); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 142 */   public String getText()
/* 142 */     throws IOException { return this.delegate.getText(); }
/* 143 */   public boolean hasTextCharacters() { return this.delegate.hasTextCharacters(); }
/* 144 */   public char[] getTextCharacters() throws IOException { return this.delegate.getTextCharacters(); }
/* 145 */   public int getTextLength() throws IOException { return this.delegate.getTextLength(); }
/* 146 */   public int getTextOffset() throws IOException { return this.delegate.getTextOffset(); }
/* 147 */   public int getText(Writer writer) throws IOException, UnsupportedOperationException { return this.delegate.getText(writer); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BigInteger getBigIntegerValue()
/*     */     throws IOException
/*     */   {
/* 156 */     return this.delegate.getBigIntegerValue();
/*     */   }
/*     */   
/* 159 */   public boolean getBooleanValue() throws IOException { return this.delegate.getBooleanValue(); }
/*     */   
/*     */   public byte getByteValue() throws IOException {
/* 162 */     return this.delegate.getByteValue();
/*     */   }
/*     */   
/* 165 */   public short getShortValue() throws IOException { return this.delegate.getShortValue(); }
/*     */   
/*     */   public BigDecimal getDecimalValue() throws IOException {
/* 168 */     return this.delegate.getDecimalValue();
/*     */   }
/*     */   
/* 171 */   public double getDoubleValue() throws IOException { return this.delegate.getDoubleValue(); }
/*     */   
/*     */   public float getFloatValue() throws IOException {
/* 174 */     return this.delegate.getFloatValue();
/*     */   }
/*     */   
/* 177 */   public int getIntValue() throws IOException { return this.delegate.getIntValue(); }
/*     */   
/*     */   public long getLongValue() throws IOException {
/* 180 */     return this.delegate.getLongValue();
/*     */   }
/*     */   
/* 183 */   public JsonParser.NumberType getNumberType() throws IOException { return this.delegate.getNumberType(); }
/*     */   
/*     */   public Number getNumberValue() throws IOException {
/* 186 */     return this.delegate.getNumberValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 194 */   public int getValueAsInt()
/* 194 */     throws IOException { return this.delegate.getValueAsInt(); }
/* 195 */   public int getValueAsInt(int defaultValue) throws IOException { return this.delegate.getValueAsInt(defaultValue); }
/* 196 */   public long getValueAsLong() throws IOException { return this.delegate.getValueAsLong(); }
/* 197 */   public long getValueAsLong(long defaultValue) throws IOException { return this.delegate.getValueAsLong(defaultValue); }
/* 198 */   public double getValueAsDouble() throws IOException { return this.delegate.getValueAsDouble(); }
/* 199 */   public double getValueAsDouble(double defaultValue) throws IOException { return this.delegate.getValueAsDouble(defaultValue); }
/* 200 */   public boolean getValueAsBoolean() throws IOException { return this.delegate.getValueAsBoolean(); }
/* 201 */   public boolean getValueAsBoolean(boolean defaultValue) throws IOException { return this.delegate.getValueAsBoolean(defaultValue); }
/* 202 */   public String getValueAsString() throws IOException { return this.delegate.getValueAsString(); }
/* 203 */   public String getValueAsString(String defaultValue) throws IOException { return this.delegate.getValueAsString(defaultValue); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 211 */   public Object getEmbeddedObject()
/* 211 */     throws IOException { return this.delegate.getEmbeddedObject(); }
/* 212 */   public byte[] getBinaryValue(Base64Variant b64variant) throws IOException { return this.delegate.getBinaryValue(b64variant); }
/* 213 */   public int readBinaryValue(Base64Variant b64variant, OutputStream out) throws IOException { return this.delegate.readBinaryValue(b64variant, out); }
/* 214 */   public JsonLocation getTokenLocation() { return this.delegate.getTokenLocation(); }
/* 215 */   public JsonToken nextToken() throws IOException { return this.delegate.nextToken(); }
/* 216 */   public JsonToken nextValue() throws IOException { return this.delegate.nextValue(); }
/*     */   
/* 218 */   public void finishToken() throws IOException { this.delegate.finishToken(); }
/*     */   
/*     */   public JsonParser skipChildren() throws IOException
/*     */   {
/* 222 */     this.delegate.skipChildren();
/*     */     
/* 224 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 233 */   public boolean canReadObjectId() { return this.delegate.canReadObjectId(); }
/* 234 */   public boolean canReadTypeId() { return this.delegate.canReadTypeId(); }
/* 235 */   public Object getObjectId() throws IOException { return this.delegate.getObjectId(); }
/* 236 */   public Object getTypeId() throws IOException { return this.delegate.getTypeId(); }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\cor\\util\JsonParserDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */