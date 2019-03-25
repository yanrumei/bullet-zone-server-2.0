/*      */ package com.fasterxml.jackson.core;
/*      */ 
/*      */ import com.fasterxml.jackson.core.format.InputAccessor;
/*      */ import com.fasterxml.jackson.core.format.MatchStrength;
/*      */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*      */ import com.fasterxml.jackson.core.io.DataOutputAsStream;
/*      */ import com.fasterxml.jackson.core.io.IOContext;
/*      */ import com.fasterxml.jackson.core.io.InputDecorator;
/*      */ import com.fasterxml.jackson.core.io.OutputDecorator;
/*      */ import com.fasterxml.jackson.core.io.SerializedString;
/*      */ import com.fasterxml.jackson.core.io.UTF8Writer;
/*      */ import com.fasterxml.jackson.core.json.ByteSourceJsonBootstrapper;
/*      */ import com.fasterxml.jackson.core.json.PackageVersion;
/*      */ import com.fasterxml.jackson.core.json.ReaderBasedJsonParser;
/*      */ import com.fasterxml.jackson.core.json.UTF8DataInputJsonParser;
/*      */ import com.fasterxml.jackson.core.json.UTF8JsonGenerator;
/*      */ import com.fasterxml.jackson.core.json.WriterBasedJsonGenerator;
/*      */ import com.fasterxml.jackson.core.sym.ByteQuadsCanonicalizer;
/*      */ import com.fasterxml.jackson.core.sym.CharsToNameCanonicalizer;
/*      */ import com.fasterxml.jackson.core.util.BufferRecycler;
/*      */ import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
/*      */ import java.io.CharArrayReader;
/*      */ import java.io.DataInput;
/*      */ import java.io.DataOutput;
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.io.StringReader;
/*      */ import java.io.Writer;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.net.URL;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class JsonFactory
/*      */   implements Versioned, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*      */   public static final String FORMAT_NAME_JSON = "JSON";
/*      */   
/*      */   public static enum Feature
/*      */   {
/*   76 */     INTERN_FIELD_NAMES(true), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   86 */     CANONICALIZE_FIELD_NAMES(true), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  102 */     FAIL_ON_SYMBOL_HASH_OVERFLOW(true), 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  117 */     USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING(true);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final boolean _defaultState;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public static int collectDefaults()
/*      */     {
/*  131 */       int flags = 0;
/*  132 */       for (Feature f : values()) {
/*  133 */         if (f.enabledByDefault()) flags |= f.getMask();
/*      */       }
/*  135 */       return flags;
/*      */     }
/*      */     
/*  138 */     private Feature(boolean defaultState) { this._defaultState = defaultState; }
/*      */     
/*  140 */     public boolean enabledByDefault() { return this._defaultState; }
/*  141 */     public boolean enabledIn(int flags) { return (flags & getMask()) != 0; }
/*  142 */     public int getMask() { return 1 << ordinal(); }
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
/*  160 */   protected static final int DEFAULT_FACTORY_FEATURE_FLAGS = ;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  166 */   protected static final int DEFAULT_PARSER_FEATURE_FLAGS = JsonParser.Feature.collectDefaults();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  172 */   protected static final int DEFAULT_GENERATOR_FEATURE_FLAGS = JsonGenerator.Feature.collectDefaults();
/*      */   
/*  174 */   private static final SerializableString DEFAULT_ROOT_VALUE_SEPARATOR = DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  187 */   protected static final ThreadLocal<SoftReference<BufferRecycler>> _recyclerRef = new ThreadLocal();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  195 */   protected final transient CharsToNameCanonicalizer _rootCharSymbols = CharsToNameCanonicalizer.createRoot();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  206 */   protected final transient ByteQuadsCanonicalizer _byteSymbolCanonicalizer = ByteQuadsCanonicalizer.createRoot();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectCodec _objectCodec;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  226 */   protected int _factoryFeatures = DEFAULT_FACTORY_FEATURE_FLAGS;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  231 */   protected int _parserFeatures = DEFAULT_PARSER_FEATURE_FLAGS;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  236 */   protected int _generatorFeatures = DEFAULT_GENERATOR_FEATURE_FLAGS;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected CharacterEscapes _characterEscapes;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected InputDecorator _inputDecorator;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected OutputDecorator _outputDecorator;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  264 */   protected SerializableString _rootValueSeparator = DEFAULT_ROOT_VALUE_SEPARATOR;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  282 */   public JsonFactory() { this(null); }
/*      */   
/*  284 */   public JsonFactory(ObjectCodec oc) { this._objectCodec = oc; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonFactory(JsonFactory src, ObjectCodec codec)
/*      */   {
/*  293 */     this._objectCodec = null;
/*  294 */     this._factoryFeatures = src._factoryFeatures;
/*  295 */     this._parserFeatures = src._parserFeatures;
/*  296 */     this._generatorFeatures = src._generatorFeatures;
/*  297 */     this._characterEscapes = src._characterEscapes;
/*  298 */     this._inputDecorator = src._inputDecorator;
/*  299 */     this._outputDecorator = src._outputDecorator;
/*  300 */     this._rootValueSeparator = src._rootValueSeparator;
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
/*      */   public JsonFactory copy()
/*      */   {
/*  324 */     _checkInvalidCopy(JsonFactory.class);
/*      */     
/*  326 */     return new JsonFactory(this, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _checkInvalidCopy(Class<?> exp)
/*      */   {
/*  335 */     if (getClass() != exp) {
/*  336 */       throw new IllegalStateException("Failed copy(): " + getClass().getName() + " (version: " + version() + ") does not override copy(); it has to");
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
/*      */   protected Object readResolve()
/*      */   {
/*  353 */     return new JsonFactory(this, this._objectCodec);
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
/*      */   public boolean requiresPropertyOrdering()
/*      */   {
/*  377 */     return false;
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
/*      */   public boolean canHandleBinaryNatively()
/*      */   {
/*  391 */     return false;
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
/*      */   public boolean canUseCharArrays()
/*      */   {
/*  405 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<? extends FormatFeature> getFormatReadFeatureType()
/*      */   {
/*  415 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<? extends FormatFeature> getFormatWriteFeatureType()
/*      */   {
/*  426 */     return null;
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
/*      */   public boolean canUseSchema(FormatSchema schema)
/*      */   {
/*  445 */     if (schema == null) {
/*  446 */       return false;
/*      */     }
/*  448 */     String ourFormat = getFormatName();
/*  449 */     return (ourFormat != null) && (ourFormat.equals(schema.getSchemaType()));
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
/*      */   public String getFormatName()
/*      */   {
/*  465 */     if (getClass() == JsonFactory.class) {
/*  466 */       return "JSON";
/*      */     }
/*  468 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MatchStrength hasFormat(InputAccessor acc)
/*      */     throws IOException
/*      */   {
/*  478 */     if (getClass() == JsonFactory.class) {
/*  479 */       return hasJSONFormat(acc);
/*      */     }
/*  481 */     return null;
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
/*      */   public boolean requiresCustomCodec()
/*      */   {
/*  498 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected MatchStrength hasJSONFormat(InputAccessor acc)
/*      */     throws IOException
/*      */   {
/*  507 */     return ByteSourceJsonBootstrapper.hasJSONFormat(acc);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Version version()
/*      */   {
/*  518 */     return PackageVersion.VERSION;
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
/*      */   public final JsonFactory configure(Feature f, boolean state)
/*      */   {
/*  532 */     return state ? enable(f) : disable(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonFactory enable(Feature f)
/*      */   {
/*  540 */     this._factoryFeatures |= f.getMask();
/*  541 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonFactory disable(Feature f)
/*      */   {
/*  549 */     this._factoryFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/*  550 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final boolean isEnabled(Feature f)
/*      */   {
/*  557 */     return (this._factoryFeatures & f.getMask()) != 0;
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
/*      */   public final JsonFactory configure(JsonParser.Feature f, boolean state)
/*      */   {
/*  571 */     return state ? enable(f) : disable(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonFactory enable(JsonParser.Feature f)
/*      */   {
/*  579 */     this._parserFeatures |= f.getMask();
/*  580 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonFactory disable(JsonParser.Feature f)
/*      */   {
/*  588 */     this._parserFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/*  589 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final boolean isEnabled(JsonParser.Feature f)
/*      */   {
/*  596 */     return (this._parserFeatures & f.getMask()) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public InputDecorator getInputDecorator()
/*      */   {
/*  604 */     return this._inputDecorator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JsonFactory setInputDecorator(InputDecorator d)
/*      */   {
/*  611 */     this._inputDecorator = d;
/*  612 */     return this;
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
/*      */   public final JsonFactory configure(JsonGenerator.Feature f, boolean state)
/*      */   {
/*  626 */     return state ? enable(f) : disable(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonFactory enable(JsonGenerator.Feature f)
/*      */   {
/*  635 */     this._generatorFeatures |= f.getMask();
/*  636 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonFactory disable(JsonGenerator.Feature f)
/*      */   {
/*  644 */     this._generatorFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/*  645 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public final boolean isEnabled(JsonGenerator.Feature f)
/*      */   {
/*  652 */     return (this._generatorFeatures & f.getMask()) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public CharacterEscapes getCharacterEscapes()
/*      */   {
/*  659 */     return this._characterEscapes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JsonFactory setCharacterEscapes(CharacterEscapes esc)
/*      */   {
/*  666 */     this._characterEscapes = esc;
/*  667 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public OutputDecorator getOutputDecorator()
/*      */   {
/*  675 */     return this._outputDecorator;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JsonFactory setOutputDecorator(OutputDecorator d)
/*      */   {
/*  682 */     this._outputDecorator = d;
/*  683 */     return this;
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
/*      */   public JsonFactory setRootValueSeparator(String sep)
/*      */   {
/*  696 */     this._rootValueSeparator = (sep == null ? null : new SerializedString(sep));
/*  697 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String getRootValueSeparator()
/*      */   {
/*  704 */     return this._rootValueSeparator == null ? null : this._rootValueSeparator.getValue();
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
/*      */   public JsonFactory setCodec(ObjectCodec oc)
/*      */   {
/*  721 */     this._objectCodec = oc;
/*  722 */     return this;
/*      */   }
/*      */   
/*  725 */   public ObjectCodec getCodec() { return this._objectCodec; }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser createParser(File f)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  755 */     IOContext ctxt = _createContext(f, true);
/*  756 */     InputStream in = new FileInputStream(f);
/*  757 */     return _createParser(_decorate(in, ctxt), ctxt);
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
/*      */   public JsonParser createParser(URL url)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  782 */     IOContext ctxt = _createContext(url, true);
/*  783 */     InputStream in = _optimizedStreamFromURL(url);
/*  784 */     return _createParser(_decorate(in, ctxt), ctxt);
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
/*      */   public JsonParser createParser(InputStream in)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  809 */     IOContext ctxt = _createContext(in, false);
/*  810 */     return _createParser(_decorate(in, ctxt), ctxt);
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
/*      */   public JsonParser createParser(Reader r)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  829 */     IOContext ctxt = _createContext(r, false);
/*  830 */     return _createParser(_decorate(r, ctxt), ctxt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser createParser(byte[] data)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  840 */     IOContext ctxt = _createContext(data, true);
/*  841 */     if (this._inputDecorator != null) {
/*  842 */       InputStream in = this._inputDecorator.decorate(ctxt, data, 0, data.length);
/*  843 */       if (in != null) {
/*  844 */         return _createParser(in, ctxt);
/*      */       }
/*      */     }
/*  847 */     return _createParser(data, 0, data.length, ctxt);
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
/*      */   public JsonParser createParser(byte[] data, int offset, int len)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  861 */     IOContext ctxt = _createContext(data, true);
/*      */     
/*  863 */     if (this._inputDecorator != null) {
/*  864 */       InputStream in = this._inputDecorator.decorate(ctxt, data, offset, len);
/*  865 */       if (in != null) {
/*  866 */         return _createParser(in, ctxt);
/*      */       }
/*      */     }
/*  869 */     return _createParser(data, offset, len, ctxt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser createParser(String content)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  879 */     int strLen = content.length();
/*      */     
/*  881 */     if ((this._inputDecorator != null) || (strLen > 32768) || (!canUseCharArrays()))
/*      */     {
/*      */ 
/*  884 */       return createParser(new StringReader(content));
/*      */     }
/*  886 */     IOContext ctxt = _createContext(content, true);
/*  887 */     char[] buf = ctxt.allocTokenBuffer(strLen);
/*  888 */     content.getChars(0, strLen, buf, 0);
/*  889 */     return _createParser(buf, 0, strLen, ctxt, true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser createParser(char[] content)
/*      */     throws IOException
/*      */   {
/*  899 */     return createParser(content, 0, content.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser createParser(char[] content, int offset, int len)
/*      */     throws IOException
/*      */   {
/*  908 */     if (this._inputDecorator != null) {
/*  909 */       return createParser(new CharArrayReader(content, offset, len));
/*      */     }
/*  911 */     return _createParser(content, offset, len, _createContext(content, true), false);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser createParser(DataInput in)
/*      */     throws IOException
/*      */   {
/*  920 */     IOContext ctxt = _createContext(in, false);
/*  921 */     return _createParser(_decorate(in, ctxt), ctxt);
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
/*      */   @Deprecated
/*      */   public JsonParser createJsonParser(File f)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  951 */     return createParser(f);
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
/*      */   @Deprecated
/*      */   public JsonParser createJsonParser(URL url)
/*      */     throws IOException, JsonParseException
/*      */   {
/*  976 */     return createParser(url);
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
/*      */   @Deprecated
/*      */   public JsonParser createJsonParser(InputStream in)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1002 */     return createParser(in);
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
/*      */   @Deprecated
/*      */   public JsonParser createJsonParser(Reader r)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1021 */     return createParser(r);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonParser createJsonParser(byte[] data)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1031 */     return createParser(data);
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
/*      */   @Deprecated
/*      */   public JsonParser createJsonParser(byte[] data, int offset, int len)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1046 */     return createParser(data, offset, len);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonParser createJsonParser(String content)
/*      */     throws IOException, JsonParseException
/*      */   {
/* 1057 */     return createParser(content);
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
/*      */   public JsonGenerator createGenerator(OutputStream out, JsonEncoding enc)
/*      */     throws IOException
/*      */   {
/* 1092 */     IOContext ctxt = _createContext(out, false);
/* 1093 */     ctxt.setEncoding(enc);
/* 1094 */     if (enc == JsonEncoding.UTF8) {
/* 1095 */       return _createUTF8Generator(_decorate(out, ctxt), ctxt);
/*      */     }
/* 1097 */     Writer w = _createWriter(out, enc, ctxt);
/* 1098 */     return _createGenerator(_decorate(w, ctxt), ctxt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonGenerator createGenerator(OutputStream out)
/*      */     throws IOException
/*      */   {
/* 1110 */     return createGenerator(out, JsonEncoding.UTF8);
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
/*      */   public JsonGenerator createGenerator(Writer w)
/*      */     throws IOException
/*      */   {
/* 1129 */     IOContext ctxt = _createContext(w, false);
/* 1130 */     return _createGenerator(_decorate(w, ctxt), ctxt);
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
/*      */   public JsonGenerator createGenerator(File f, JsonEncoding enc)
/*      */     throws IOException
/*      */   {
/* 1151 */     OutputStream out = new FileOutputStream(f);
/*      */     
/* 1153 */     IOContext ctxt = _createContext(out, true);
/* 1154 */     ctxt.setEncoding(enc);
/* 1155 */     if (enc == JsonEncoding.UTF8) {
/* 1156 */       return _createUTF8Generator(_decorate(out, ctxt), ctxt);
/*      */     }
/* 1158 */     Writer w = _createWriter(out, enc, ctxt);
/* 1159 */     return _createGenerator(_decorate(w, ctxt), ctxt);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonGenerator createGenerator(DataOutput out, JsonEncoding enc)
/*      */     throws IOException
/*      */   {
/* 1169 */     return createGenerator(_createDataOutputWrapper(out), enc);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonGenerator createGenerator(DataOutput out)
/*      */     throws IOException
/*      */   {
/* 1181 */     return createGenerator(_createDataOutputWrapper(out), JsonEncoding.UTF8);
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
/*      */   @Deprecated
/*      */   public JsonGenerator createJsonGenerator(OutputStream out, JsonEncoding enc)
/*      */     throws IOException
/*      */   {
/* 1214 */     return createGenerator(out, enc);
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
/*      */   @Deprecated
/*      */   public JsonGenerator createJsonGenerator(Writer out)
/*      */     throws IOException
/*      */   {
/* 1234 */     return createGenerator(out);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonGenerator createJsonGenerator(OutputStream out)
/*      */     throws IOException
/*      */   {
/* 1247 */     return createGenerator(out, JsonEncoding.UTF8);
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
/*      */   protected JsonParser _createParser(InputStream in, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1271 */     return new ByteSourceJsonBootstrapper(ctxt, in).constructParser(this._parserFeatures, this._objectCodec, this._byteSymbolCanonicalizer, this._rootCharSymbols, this._factoryFeatures);
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
/*      */   protected JsonParser _createParser(Reader r, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1288 */     return new ReaderBasedJsonParser(ctxt, this._parserFeatures, r, this._objectCodec, this._rootCharSymbols.makeChild(this._factoryFeatures));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonParser _createParser(char[] data, int offset, int len, IOContext ctxt, boolean recyclable)
/*      */     throws IOException
/*      */   {
/* 1300 */     return new ReaderBasedJsonParser(ctxt, this._parserFeatures, null, this._objectCodec, this._rootCharSymbols.makeChild(this._factoryFeatures), data, offset, offset + len, recyclable);
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
/*      */   protected JsonParser _createParser(byte[] data, int offset, int len, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1318 */     return new ByteSourceJsonBootstrapper(ctxt, data, offset, len).constructParser(this._parserFeatures, this._objectCodec, this._byteSymbolCanonicalizer, this._rootCharSymbols, this._factoryFeatures);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonParser _createParser(DataInput input, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1329 */     String format = getFormatName();
/* 1330 */     if (format != "JSON") {
/* 1331 */       throw new UnsupportedOperationException(String.format("InputData source not (yet?) support for this format (%s)", new Object[] { format }));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1336 */     int firstByte = ByteSourceJsonBootstrapper.skipUTF8BOM(input);
/* 1337 */     ByteQuadsCanonicalizer can = this._byteSymbolCanonicalizer.makeChild(this._factoryFeatures);
/* 1338 */     return new UTF8DataInputJsonParser(ctxt, this._parserFeatures, input, this._objectCodec, can, firstByte);
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
/*      */   protected JsonGenerator _createGenerator(Writer out, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1361 */     WriterBasedJsonGenerator gen = new WriterBasedJsonGenerator(ctxt, this._generatorFeatures, this._objectCodec, out);
/*      */     
/* 1363 */     if (this._characterEscapes != null) {
/* 1364 */       gen.setCharacterEscapes(this._characterEscapes);
/*      */     }
/* 1366 */     SerializableString rootSep = this._rootValueSeparator;
/* 1367 */     if (rootSep != DEFAULT_ROOT_VALUE_SEPARATOR) {
/* 1368 */       gen.setRootValueSeparator(rootSep);
/*      */     }
/* 1370 */     return gen;
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
/*      */   protected JsonGenerator _createUTF8Generator(OutputStream out, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1384 */     UTF8JsonGenerator gen = new UTF8JsonGenerator(ctxt, this._generatorFeatures, this._objectCodec, out);
/*      */     
/* 1386 */     if (this._characterEscapes != null) {
/* 1387 */       gen.setCharacterEscapes(this._characterEscapes);
/*      */     }
/* 1389 */     SerializableString rootSep = this._rootValueSeparator;
/* 1390 */     if (rootSep != DEFAULT_ROOT_VALUE_SEPARATOR) {
/* 1391 */       gen.setRootValueSeparator(rootSep);
/*      */     }
/* 1393 */     return gen;
/*      */   }
/*      */   
/*      */   protected Writer _createWriter(OutputStream out, JsonEncoding enc, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1399 */     if (enc == JsonEncoding.UTF8) {
/* 1400 */       return new UTF8Writer(ctxt, out);
/*      */     }
/*      */     
/* 1403 */     return new OutputStreamWriter(out, enc.getJavaName());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final InputStream _decorate(InputStream in, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1416 */     if (this._inputDecorator != null) {
/* 1417 */       InputStream in2 = this._inputDecorator.decorate(ctxt, in);
/* 1418 */       if (in2 != null) {
/* 1419 */         return in2;
/*      */       }
/*      */     }
/* 1422 */     return in;
/*      */   }
/*      */   
/*      */ 
/*      */   protected final Reader _decorate(Reader in, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1429 */     if (this._inputDecorator != null) {
/* 1430 */       Reader in2 = this._inputDecorator.decorate(ctxt, in);
/* 1431 */       if (in2 != null) {
/* 1432 */         return in2;
/*      */       }
/*      */     }
/* 1435 */     return in;
/*      */   }
/*      */   
/*      */ 
/*      */   protected final DataInput _decorate(DataInput in, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1442 */     if (this._inputDecorator != null) {
/* 1443 */       DataInput in2 = this._inputDecorator.decorate(ctxt, in);
/* 1444 */       if (in2 != null) {
/* 1445 */         return in2;
/*      */       }
/*      */     }
/* 1448 */     return in;
/*      */   }
/*      */   
/*      */ 
/*      */   protected final OutputStream _decorate(OutputStream out, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1455 */     if (this._outputDecorator != null) {
/* 1456 */       OutputStream out2 = this._outputDecorator.decorate(ctxt, out);
/* 1457 */       if (out2 != null) {
/* 1458 */         return out2;
/*      */       }
/*      */     }
/* 1461 */     return out;
/*      */   }
/*      */   
/*      */ 
/*      */   protected final Writer _decorate(Writer out, IOContext ctxt)
/*      */     throws IOException
/*      */   {
/* 1468 */     if (this._outputDecorator != null) {
/* 1469 */       Writer out2 = this._outputDecorator.decorate(ctxt, out);
/* 1470 */       if (out2 != null) {
/* 1471 */         return out2;
/*      */       }
/*      */     }
/* 1474 */     return out;
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
/*      */   public BufferRecycler _getBufferRecycler()
/*      */   {
/* 1495 */     if (isEnabled(Feature.USE_THREAD_LOCAL_FOR_BUFFER_RECYCLING)) {
/* 1496 */       SoftReference<BufferRecycler> ref = (SoftReference)_recyclerRef.get();
/* 1497 */       BufferRecycler br = ref == null ? null : (BufferRecycler)ref.get();
/*      */       
/* 1499 */       if (br == null) {
/* 1500 */         br = new BufferRecycler();
/* 1501 */         _recyclerRef.set(new SoftReference(br));
/*      */       }
/* 1503 */       return br;
/*      */     }
/* 1505 */     return new BufferRecycler();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected IOContext _createContext(Object srcRef, boolean resourceManaged)
/*      */   {
/* 1513 */     return new IOContext(_getBufferRecycler(), srcRef, resourceManaged);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected OutputStream _createDataOutputWrapper(DataOutput out)
/*      */   {
/* 1520 */     return new DataOutputAsStream(out);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected InputStream _optimizedStreamFromURL(URL url)
/*      */     throws IOException
/*      */   {
/* 1529 */     if ("file".equals(url.getProtocol()))
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1536 */       String host = url.getHost();
/* 1537 */       if ((host == null) || (host.length() == 0))
/*      */       {
/* 1539 */         String path = url.getPath();
/* 1540 */         if (path.indexOf('%') < 0) {
/* 1541 */           return new FileInputStream(url.getPath());
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1547 */     return url.openStream();
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-core-2.8.10.jar!\com\fasterxml\jackson\core\JsonFactory.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */