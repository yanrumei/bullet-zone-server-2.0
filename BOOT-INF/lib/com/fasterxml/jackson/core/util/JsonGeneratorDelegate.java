/*     */ package com.fasterxml.jackson.core.util;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.FormatSchema;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.ObjectCodec;
/*     */ import com.fasterxml.jackson.core.PrettyPrinter;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.TreeNode;
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JsonGeneratorDelegate
/*     */   extends JsonGenerator
/*     */ {
/*     */   protected JsonGenerator delegate;
/*     */   protected boolean delegateCopyMethods;
/*     */   
/*     */   public JsonGeneratorDelegate(JsonGenerator d)
/*     */   {
/*  32 */     this(d, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonGeneratorDelegate(JsonGenerator d, boolean delegateCopyMethods)
/*     */   {
/*  41 */     this.delegate = d;
/*  42 */     this.delegateCopyMethods = delegateCopyMethods;
/*     */   }
/*     */   
/*     */   public Object getCurrentValue()
/*     */   {
/*  47 */     return this.delegate.getCurrentValue();
/*     */   }
/*     */   
/*     */   public void setCurrentValue(Object v)
/*     */   {
/*  52 */     this.delegate.setCurrentValue(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonGenerator getDelegate()
/*     */   {
/*  61 */     return this.delegate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  69 */   public ObjectCodec getCodec() { return this.delegate.getCodec(); }
/*     */   
/*     */   public JsonGenerator setCodec(ObjectCodec oc) {
/*  72 */     this.delegate.setCodec(oc);
/*  73 */     return this;
/*     */   }
/*     */   
/*  76 */   public void setSchema(FormatSchema schema) { this.delegate.setSchema(schema); }
/*  77 */   public FormatSchema getSchema() { return this.delegate.getSchema(); }
/*  78 */   public Version version() { return this.delegate.version(); }
/*  79 */   public Object getOutputTarget() { return this.delegate.getOutputTarget(); }
/*  80 */   public int getOutputBuffered() { return this.delegate.getOutputBuffered(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean canUseSchema(FormatSchema schema)
/*     */   {
/*  89 */     return this.delegate.canUseSchema(schema);
/*     */   }
/*     */   
/*  92 */   public boolean canWriteTypeId() { return this.delegate.canWriteTypeId(); }
/*     */   
/*     */   public boolean canWriteObjectId() {
/*  95 */     return this.delegate.canWriteObjectId();
/*     */   }
/*     */   
/*  98 */   public boolean canWriteBinaryNatively() { return this.delegate.canWriteBinaryNatively(); }
/*     */   
/*     */   public boolean canOmitFields() {
/* 101 */     return this.delegate.canOmitFields();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonGenerator enable(JsonGenerator.Feature f)
/*     */   {
/* 111 */     this.delegate.enable(f);
/* 112 */     return this;
/*     */   }
/*     */   
/*     */   public JsonGenerator disable(JsonGenerator.Feature f)
/*     */   {
/* 117 */     this.delegate.disable(f);
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   public boolean isEnabled(JsonGenerator.Feature f) {
/* 122 */     return this.delegate.isEnabled(f);
/*     */   }
/*     */   
/*     */ 
/*     */   public int getFeatureMask()
/*     */   {
/* 128 */     return this.delegate.getFeatureMask();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public JsonGenerator setFeatureMask(int mask) {
/* 133 */     this.delegate.setFeatureMask(mask);
/* 134 */     return this;
/*     */   }
/*     */   
/*     */   public JsonGenerator overrideStdFeatures(int values, int mask)
/*     */   {
/* 139 */     this.delegate.overrideStdFeatures(values, mask);
/* 140 */     return this;
/*     */   }
/*     */   
/*     */   public JsonGenerator overrideFormatFeatures(int values, int mask)
/*     */   {
/* 145 */     this.delegate.overrideFormatFeatures(values, mask);
/* 146 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonGenerator setPrettyPrinter(PrettyPrinter pp)
/*     */   {
/* 157 */     this.delegate.setPrettyPrinter(pp);
/* 158 */     return this;
/*     */   }
/*     */   
/*     */ 
/* 162 */   public PrettyPrinter getPrettyPrinter() { return this.delegate.getPrettyPrinter(); }
/*     */   
/*     */   public JsonGenerator useDefaultPrettyPrinter() {
/* 165 */     this.delegate.useDefaultPrettyPrinter();
/* 166 */     return this;
/*     */   }
/*     */   
/* 169 */   public JsonGenerator setHighestNonEscapedChar(int charCode) { this.delegate.setHighestNonEscapedChar(charCode);
/* 170 */     return this;
/*     */   }
/*     */   
/* 173 */   public int getHighestEscapedChar() { return this.delegate.getHighestEscapedChar(); }
/*     */   
/*     */ 
/* 176 */   public CharacterEscapes getCharacterEscapes() { return this.delegate.getCharacterEscapes(); }
/*     */   
/*     */   public JsonGenerator setCharacterEscapes(CharacterEscapes esc) {
/* 179 */     this.delegate.setCharacterEscapes(esc);
/* 180 */     return this;
/*     */   }
/*     */   
/* 183 */   public JsonGenerator setRootValueSeparator(SerializableString sep) { this.delegate.setRootValueSeparator(sep);
/* 184 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeStartArray()
/*     */     throws IOException
/*     */   {
/* 193 */     this.delegate.writeStartArray();
/*     */   }
/*     */   
/* 196 */   public void writeStartArray(int size) throws IOException { this.delegate.writeStartArray(size); }
/*     */   
/*     */   public void writeEndArray() throws IOException {
/* 199 */     this.delegate.writeEndArray();
/*     */   }
/*     */   
/* 202 */   public void writeStartObject() throws IOException { this.delegate.writeStartObject(); }
/*     */   
/*     */   public void writeStartObject(Object forValue) throws IOException {
/* 205 */     this.delegate.writeStartObject(forValue);
/*     */   }
/*     */   
/* 208 */   public void writeEndObject() throws IOException { this.delegate.writeEndObject(); }
/*     */   
/*     */   public void writeFieldName(String name) throws IOException
/*     */   {
/* 212 */     this.delegate.writeFieldName(name);
/*     */   }
/*     */   
/*     */   public void writeFieldName(SerializableString name) throws IOException
/*     */   {
/* 217 */     this.delegate.writeFieldName(name);
/*     */   }
/*     */   
/*     */   public void writeFieldId(long id) throws IOException
/*     */   {
/* 222 */     this.delegate.writeFieldId(id);
/*     */   }
/*     */   
/*     */   public void writeArray(int[] array, int offset, int length) throws IOException
/*     */   {
/* 227 */     this.delegate.writeArray(array, offset, length);
/*     */   }
/*     */   
/*     */   public void writeArray(long[] array, int offset, int length) throws IOException
/*     */   {
/* 232 */     this.delegate.writeArray(array, offset, length);
/*     */   }
/*     */   
/*     */   public void writeArray(double[] array, int offset, int length) throws IOException
/*     */   {
/* 237 */     this.delegate.writeArray(array, offset, length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeString(String text)
/*     */     throws IOException
/*     */   {
/* 247 */     this.delegate.writeString(text);
/*     */   }
/*     */   
/* 250 */   public void writeString(char[] text, int offset, int len) throws IOException { this.delegate.writeString(text, offset, len); }
/*     */   
/*     */   public void writeString(SerializableString text) throws IOException {
/* 253 */     this.delegate.writeString(text);
/*     */   }
/*     */   
/* 256 */   public void writeRawUTF8String(byte[] text, int offset, int length) throws IOException { this.delegate.writeRawUTF8String(text, offset, length); }
/*     */   
/*     */   public void writeUTF8String(byte[] text, int offset, int length) throws IOException {
/* 259 */     this.delegate.writeUTF8String(text, offset, length);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeRaw(String text)
/*     */     throws IOException
/*     */   {
/* 268 */     this.delegate.writeRaw(text);
/*     */   }
/*     */   
/* 271 */   public void writeRaw(String text, int offset, int len) throws IOException { this.delegate.writeRaw(text, offset, len); }
/*     */   
/*     */   public void writeRaw(SerializableString raw) throws IOException {
/* 274 */     this.delegate.writeRaw(raw);
/*     */   }
/*     */   
/* 277 */   public void writeRaw(char[] text, int offset, int len) throws IOException { this.delegate.writeRaw(text, offset, len); }
/*     */   
/*     */   public void writeRaw(char c) throws IOException {
/* 280 */     this.delegate.writeRaw(c);
/*     */   }
/*     */   
/* 283 */   public void writeRawValue(String text) throws IOException { this.delegate.writeRawValue(text); }
/*     */   
/*     */   public void writeRawValue(String text, int offset, int len) throws IOException {
/* 286 */     this.delegate.writeRawValue(text, offset, len);
/*     */   }
/*     */   
/* 289 */   public void writeRawValue(char[] text, int offset, int len) throws IOException { this.delegate.writeRawValue(text, offset, len); }
/*     */   
/*     */   public void writeBinary(Base64Variant b64variant, byte[] data, int offset, int len) throws IOException {
/* 292 */     this.delegate.writeBinary(b64variant, data, offset, len);
/*     */   }
/*     */   
/* 295 */   public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength) throws IOException { return this.delegate.writeBinary(b64variant, data, dataLength); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeNumber(short v)
/*     */     throws IOException
/*     */   {
/* 304 */     this.delegate.writeNumber(v);
/*     */   }
/*     */   
/* 307 */   public void writeNumber(int v) throws IOException { this.delegate.writeNumber(v); }
/*     */   
/*     */   public void writeNumber(long v) throws IOException {
/* 310 */     this.delegate.writeNumber(v);
/*     */   }
/*     */   
/* 313 */   public void writeNumber(BigInteger v) throws IOException { this.delegate.writeNumber(v); }
/*     */   
/*     */   public void writeNumber(double v) throws IOException {
/* 316 */     this.delegate.writeNumber(v);
/*     */   }
/*     */   
/* 319 */   public void writeNumber(float v) throws IOException { this.delegate.writeNumber(v); }
/*     */   
/*     */   public void writeNumber(BigDecimal v) throws IOException {
/* 322 */     this.delegate.writeNumber(v);
/*     */   }
/*     */   
/* 325 */   public void writeNumber(String encodedValue) throws IOException, UnsupportedOperationException { this.delegate.writeNumber(encodedValue); }
/*     */   
/*     */   public void writeBoolean(boolean state) throws IOException {
/* 328 */     this.delegate.writeBoolean(state);
/*     */   }
/*     */   
/* 331 */   public void writeNull() throws IOException { this.delegate.writeNull(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeOmittedField(String fieldName)
/*     */     throws IOException
/*     */   {
/* 340 */     this.delegate.writeOmittedField(fieldName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeObjectId(Object id)
/*     */     throws IOException
/*     */   {
/* 349 */     this.delegate.writeObjectId(id);
/*     */   }
/*     */   
/* 352 */   public void writeObjectRef(Object id) throws IOException { this.delegate.writeObjectRef(id); }
/*     */   
/*     */   public void writeTypeId(Object id) throws IOException {
/* 355 */     this.delegate.writeTypeId(id);
/*     */   }
/*     */   
/* 358 */   public void writeEmbeddedObject(Object object) throws IOException { this.delegate.writeEmbeddedObject(object); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeObject(Object pojo)
/*     */     throws IOException, JsonProcessingException
/*     */   {
/* 368 */     if (this.delegateCopyMethods) {
/* 369 */       this.delegate.writeObject(pojo);
/* 370 */       return;
/*     */     }
/*     */     
/* 373 */     if (pojo == null) {
/* 374 */       writeNull();
/*     */     } else {
/* 376 */       if (getCodec() != null) {
/* 377 */         getCodec().writeValue(this, pojo);
/* 378 */         return;
/*     */       }
/* 380 */       _writeSimpleObject(pojo);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTree(TreeNode rootNode) throws IOException
/*     */   {
/* 386 */     if (this.delegateCopyMethods) {
/* 387 */       this.delegate.writeTree(rootNode);
/* 388 */       return;
/*     */     }
/*     */     
/* 391 */     if (rootNode == null) {
/* 392 */       writeNull();
/*     */     } else {
/* 394 */       if (getCodec() == null) {
/* 395 */         throw new IllegalStateException("No ObjectCodec defined");
/*     */       }
/* 397 */       getCodec().writeValue(this, rootNode);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void copyCurrentEvent(JsonParser jp)
/*     */     throws IOException
/*     */   {
/* 417 */     if (this.delegateCopyMethods) this.delegate.copyCurrentEvent(jp); else {
/* 418 */       super.copyCurrentEvent(jp);
/*     */     }
/*     */   }
/*     */   
/*     */   public void copyCurrentStructure(JsonParser jp) throws IOException {
/* 423 */     if (this.delegateCopyMethods) this.delegate.copyCurrentStructure(jp); else {
/* 424 */       super.copyCurrentStructure(jp);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonStreamContext getOutputContext()
/*     */   {
/* 433 */     return this.delegate.getOutputContext();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 441 */   public void flush()
/* 441 */     throws IOException { this.delegate.flush(); }
/* 442 */   public void close() throws IOException { this.delegate.close(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isClosed()
/*     */   {
/* 450 */     return this.delegate.isClosed();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\cor\\util\JsonGeneratorDelegate.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */