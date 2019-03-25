/*     */ package com.fasterxml.jackson.core.base;
/*     */ 
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*     */ import com.fasterxml.jackson.core.JsonStreamContext;
/*     */ import com.fasterxml.jackson.core.ObjectCodec;
/*     */ import com.fasterxml.jackson.core.PrettyPrinter;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.TreeNode;
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import com.fasterxml.jackson.core.json.DupDetector;
/*     */ import com.fasterxml.jackson.core.json.JsonWriteContext;
/*     */ import com.fasterxml.jackson.core.json.PackageVersion;
/*     */ import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.math.BigDecimal;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class GeneratorBase
/*     */   extends JsonGenerator
/*     */ {
/*     */   public static final int SURR1_FIRST = 55296;
/*     */   public static final int SURR1_LAST = 56319;
/*     */   public static final int SURR2_FIRST = 56320;
/*     */   public static final int SURR2_LAST = 57343;
/*  30 */   protected static final int DERIVED_FEATURES_MASK = JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.getMask() | JsonGenerator.Feature.ESCAPE_NON_ASCII.getMask() | JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION.getMask();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final String WRITE_BINARY = "write a binary value";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final String WRITE_BOOLEAN = "write a boolean value";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final String WRITE_NULL = "write a null";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final String WRITE_NUMBER = "write a number";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final String WRITE_RAW = "write a raw (unencoded) value";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final String WRITE_STRING = "write a string";
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected static final int MAX_BIG_DECIMAL_SCALE = 9999;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ObjectCodec _objectCodec;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected int _features;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _cfgNumbersAsStrings;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonWriteContext _writeContext;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _closed;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected GeneratorBase(int features, ObjectCodec codec)
/*     */   {
/* 104 */     this._features = features;
/* 105 */     this._objectCodec = codec;
/* 106 */     DupDetector dups = JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION.enabledIn(features) ? DupDetector.rootDetector(this) : null;
/*     */     
/* 108 */     this._writeContext = JsonWriteContext.createRootContext(dups);
/* 109 */     this._cfgNumbersAsStrings = JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.enabledIn(features);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected GeneratorBase(int features, ObjectCodec codec, JsonWriteContext ctxt)
/*     */   {
/* 117 */     this._features = features;
/* 118 */     this._objectCodec = codec;
/* 119 */     this._writeContext = ctxt;
/* 120 */     this._cfgNumbersAsStrings = JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.enabledIn(features);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Version version()
/*     */   {
/* 128 */     return PackageVersion.VERSION;
/*     */   }
/*     */   
/*     */   public Object getCurrentValue() {
/* 132 */     return this._writeContext.getCurrentValue();
/*     */   }
/*     */   
/*     */   public void setCurrentValue(Object v)
/*     */   {
/* 137 */     this._writeContext.setCurrentValue(v);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 147 */   public final boolean isEnabled(JsonGenerator.Feature f) { return (this._features & f.getMask()) != 0; }
/* 148 */   public int getFeatureMask() { return this._features; }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonGenerator enable(JsonGenerator.Feature f)
/*     */   {
/* 154 */     int mask = f.getMask();
/* 155 */     this._features |= mask;
/* 156 */     if ((mask & DERIVED_FEATURES_MASK) != 0)
/*     */     {
/* 158 */       if (f == JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS) {
/* 159 */         this._cfgNumbersAsStrings = true;
/* 160 */       } else if (f == JsonGenerator.Feature.ESCAPE_NON_ASCII) {
/* 161 */         setHighestNonEscapedChar(127);
/* 162 */       } else if ((f == JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION) && 
/* 163 */         (this._writeContext.getDupDetector() == null)) {
/* 164 */         this._writeContext = this._writeContext.withDupDetector(DupDetector.rootDetector(this));
/*     */       }
/*     */     }
/*     */     
/* 168 */     return this;
/*     */   }
/*     */   
/*     */   public JsonGenerator disable(JsonGenerator.Feature f)
/*     */   {
/* 173 */     int mask = f.getMask();
/* 174 */     this._features &= (mask ^ 0xFFFFFFFF);
/* 175 */     if ((mask & DERIVED_FEATURES_MASK) != 0) {
/* 176 */       if (f == JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS) {
/* 177 */         this._cfgNumbersAsStrings = false;
/* 178 */       } else if (f == JsonGenerator.Feature.ESCAPE_NON_ASCII) {
/* 179 */         setHighestNonEscapedChar(0);
/* 180 */       } else if (f == JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION) {
/* 181 */         this._writeContext = this._writeContext.withDupDetector(null);
/*     */       }
/*     */     }
/* 184 */     return this;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public JsonGenerator setFeatureMask(int newMask)
/*     */   {
/* 190 */     int changed = newMask ^ this._features;
/* 191 */     this._features = newMask;
/* 192 */     if (changed != 0) {
/* 193 */       _checkStdFeatureChanges(newMask, changed);
/*     */     }
/* 195 */     return this;
/*     */   }
/*     */   
/*     */   public JsonGenerator overrideStdFeatures(int values, int mask)
/*     */   {
/* 200 */     int oldState = this._features;
/* 201 */     int newState = oldState & (mask ^ 0xFFFFFFFF) | values & mask;
/* 202 */     int changed = oldState ^ newState;
/* 203 */     if (changed != 0) {
/* 204 */       this._features = newState;
/* 205 */       _checkStdFeatureChanges(newState, changed);
/*     */     }
/* 207 */     return this;
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
/*     */   protected void _checkStdFeatureChanges(int newFeatureFlags, int changedFeatures)
/*     */   {
/* 221 */     if ((changedFeatures & DERIVED_FEATURES_MASK) == 0) {
/* 222 */       return;
/*     */     }
/* 224 */     this._cfgNumbersAsStrings = JsonGenerator.Feature.WRITE_NUMBERS_AS_STRINGS.enabledIn(newFeatureFlags);
/* 225 */     if (JsonGenerator.Feature.ESCAPE_NON_ASCII.enabledIn(changedFeatures)) {
/* 226 */       if (JsonGenerator.Feature.ESCAPE_NON_ASCII.enabledIn(newFeatureFlags)) {
/* 227 */         setHighestNonEscapedChar(127);
/*     */       } else {
/* 229 */         setHighestNonEscapedChar(0);
/*     */       }
/*     */     }
/* 232 */     if (JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION.enabledIn(changedFeatures)) {
/* 233 */       if (JsonGenerator.Feature.STRICT_DUPLICATE_DETECTION.enabledIn(newFeatureFlags)) {
/* 234 */         if (this._writeContext.getDupDetector() == null) {
/* 235 */           this._writeContext = this._writeContext.withDupDetector(DupDetector.rootDetector(this));
/*     */         }
/*     */       } else {
/* 238 */         this._writeContext = this._writeContext.withDupDetector(null);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public JsonGenerator useDefaultPrettyPrinter()
/*     */   {
/* 245 */     if (getPrettyPrinter() != null) {
/* 246 */       return this;
/*     */     }
/* 248 */     return setPrettyPrinter(_constructDefaultPrettyPrinter());
/*     */   }
/*     */   
/*     */   public JsonGenerator setCodec(ObjectCodec oc) {
/* 252 */     this._objectCodec = oc;
/* 253 */     return this;
/*     */   }
/*     */   
/* 256 */   public ObjectCodec getCodec() { return this._objectCodec; }
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
/*     */   public JsonStreamContext getOutputContext()
/*     */   {
/* 269 */     return this._writeContext;
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
/*     */   public void writeStartObject(Object forValue)
/*     */     throws IOException
/*     */   {
/* 285 */     writeStartObject();
/* 286 */     if ((this._writeContext != null) && (forValue != null)) {
/* 287 */       this._writeContext.setCurrentValue(forValue);
/*     */     }
/* 289 */     setCurrentValue(forValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeFieldName(SerializableString name)
/*     */     throws IOException
/*     */   {
/* 299 */     writeFieldName(name.getValue());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeString(SerializableString text)
/*     */     throws IOException
/*     */   {
/* 312 */     writeString(text.getValue());
/*     */   }
/*     */   
/*     */   public void writeRawValue(String text) throws IOException {
/* 316 */     _verifyValueWrite("write raw value");
/* 317 */     writeRaw(text);
/*     */   }
/*     */   
/*     */   public void writeRawValue(String text, int offset, int len) throws IOException {
/* 321 */     _verifyValueWrite("write raw value");
/* 322 */     writeRaw(text, offset, len);
/*     */   }
/*     */   
/*     */   public void writeRawValue(char[] text, int offset, int len) throws IOException {
/* 326 */     _verifyValueWrite("write raw value");
/* 327 */     writeRaw(text, offset, len);
/*     */   }
/*     */   
/*     */   public void writeRawValue(SerializableString text) throws IOException {
/* 331 */     _verifyValueWrite("write raw value");
/* 332 */     writeRaw(text);
/*     */   }
/*     */   
/*     */   public int writeBinary(Base64Variant b64variant, InputStream data, int dataLength)
/*     */     throws IOException
/*     */   {
/* 338 */     _reportUnsupportedOperation();
/* 339 */     return 0;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void writeObject(Object value)
/*     */     throws IOException
/*     */   {
/* 368 */     if (value == null)
/*     */     {
/* 370 */       writeNull();
/*     */ 
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/*     */ 
/* 377 */       if (this._objectCodec != null) {
/* 378 */         this._objectCodec.writeValue(this, value);
/* 379 */         return;
/*     */       }
/* 381 */       _writeSimpleObject(value);
/*     */     }
/*     */   }
/*     */   
/*     */   public void writeTree(TreeNode rootNode)
/*     */     throws IOException
/*     */   {
/* 388 */     if (rootNode == null) {
/* 389 */       writeNull();
/*     */     } else {
/* 391 */       if (this._objectCodec == null) {
/* 392 */         throw new IllegalStateException("No ObjectCodec defined");
/*     */       }
/* 394 */       this._objectCodec.writeValue(this, rootNode);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void flush()
/*     */     throws IOException;
/*     */   
/*     */ 
/* 405 */   public void close()
/* 405 */     throws IOException { this._closed = true; }
/* 406 */   public boolean isClosed() { return this._closed; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void _releaseBuffers();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected abstract void _verifyValueWrite(String paramString)
/*     */     throws IOException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected PrettyPrinter _constructDefaultPrettyPrinter()
/*     */   {
/* 437 */     return new DefaultPrettyPrinter();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String _asString(BigDecimal value)
/*     */     throws IOException
/*     */   {
/* 447 */     if (JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN.enabledIn(this._features))
/*     */     {
/* 449 */       int scale = value.scale();
/* 450 */       if ((scale < 55537) || (scale > 9999)) {
/* 451 */         _reportError(String.format("Attempt to write plain `java.math.BigDecimal` (see JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN) with illegal scale (%d): needs to be between [-%d, %d]", new Object[] { Integer.valueOf(scale), Integer.valueOf(9999), Integer.valueOf(9999) }));
/*     */       }
/*     */       
/*     */ 
/* 455 */       return value.toPlainString();
/*     */     }
/* 457 */     return value.toString();
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
/*     */   protected final int _decodeSurrogate(int surr1, int surr2)
/*     */     throws IOException
/*     */   {
/* 472 */     if ((surr2 < 56320) || (surr2 > 57343)) {
/* 473 */       String msg = "Incomplete surrogate pair: first char 0x" + Integer.toHexString(surr1) + ", second 0x" + Integer.toHexString(surr2);
/* 474 */       _reportError(msg);
/*     */     }
/* 476 */     int c = 65536 + (surr1 - 55296 << 10) + (surr2 - 56320);
/* 477 */     return c;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\base\GeneratorBase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */