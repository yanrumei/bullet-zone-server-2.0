/*      */ package com.fasterxml.jackson.databind;
/*      */ 
/*      */ import com.fasterxml.jackson.core.FormatFeature;
/*      */ import com.fasterxml.jackson.core.FormatSchema;
/*      */ import com.fasterxml.jackson.core.JsonEncoding;
/*      */ import com.fasterxml.jackson.core.JsonFactory;
/*      */ import com.fasterxml.jackson.core.JsonGenerationException;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*      */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*      */ import com.fasterxml.jackson.core.JsonProcessingException;
/*      */ import com.fasterxml.jackson.core.PrettyPrinter;
/*      */ import com.fasterxml.jackson.core.SerializableString;
/*      */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*      */ import com.fasterxml.jackson.core.io.SegmentedStringWriter;
/*      */ import com.fasterxml.jackson.core.type.TypeReference;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.core.util.Instantiatable;
/*      */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*      */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*      */ import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
/*      */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*      */ import com.fasterxml.jackson.databind.ser.impl.TypeWrappedSerializer;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import java.io.Closeable;
/*      */ import java.io.DataOutput;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.io.Writer;
/*      */ import java.text.DateFormat;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ 
/*      */ public class ObjectWriter implements com.fasterxml.jackson.core.Versioned, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   40 */   protected static final PrettyPrinter NULL_PRETTY_PRINTER = new com.fasterxml.jackson.core.util.MinimalPrettyPrinter();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final SerializationConfig _config;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final DefaultSerializerProvider _serializerProvider;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final com.fasterxml.jackson.databind.ser.SerializerFactory _serializerFactory;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JsonFactory _generatorFactory;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final GeneratorSettings _generatorSettings;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Prefetch _prefetch;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter(ObjectMapper mapper, SerializationConfig config, JavaType rootType, PrettyPrinter pp)
/*      */   {
/*   98 */     this._config = config;
/*   99 */     this._serializerProvider = mapper._serializerProvider;
/*  100 */     this._serializerFactory = mapper._serializerFactory;
/*  101 */     this._generatorFactory = mapper._jsonFactory;
/*  102 */     this._generatorSettings = (pp == null ? GeneratorSettings.empty : new GeneratorSettings(pp, null, null, null));
/*      */     
/*      */ 
/*      */ 
/*  106 */     if ((rootType == null) || (rootType.hasRawClass(Object.class))) {
/*  107 */       this._prefetch = Prefetch.empty;
/*      */     } else {
/*  109 */       rootType = rootType.withStaticTyping();
/*  110 */       this._prefetch = Prefetch.empty.forRootType(this, rootType);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter(ObjectMapper mapper, SerializationConfig config)
/*      */   {
/*  119 */     this._config = config;
/*  120 */     this._serializerProvider = mapper._serializerProvider;
/*  121 */     this._serializerFactory = mapper._serializerFactory;
/*  122 */     this._generatorFactory = mapper._jsonFactory;
/*      */     
/*  124 */     this._generatorSettings = GeneratorSettings.empty;
/*  125 */     this._prefetch = Prefetch.empty;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter(ObjectMapper mapper, SerializationConfig config, FormatSchema s)
/*      */   {
/*  134 */     this._config = config;
/*      */     
/*  136 */     this._serializerProvider = mapper._serializerProvider;
/*  137 */     this._serializerFactory = mapper._serializerFactory;
/*  138 */     this._generatorFactory = mapper._jsonFactory;
/*      */     
/*  140 */     this._generatorSettings = (s == null ? GeneratorSettings.empty : new GeneratorSettings(null, s, null, null));
/*      */     
/*  142 */     this._prefetch = Prefetch.empty;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter(ObjectWriter base, SerializationConfig config, GeneratorSettings genSettings, Prefetch prefetch)
/*      */   {
/*  151 */     this._config = config;
/*      */     
/*  153 */     this._serializerProvider = base._serializerProvider;
/*  154 */     this._serializerFactory = base._serializerFactory;
/*  155 */     this._generatorFactory = base._generatorFactory;
/*      */     
/*  157 */     this._generatorSettings = genSettings;
/*  158 */     this._prefetch = prefetch;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter(ObjectWriter base, SerializationConfig config)
/*      */   {
/*  166 */     this._config = config;
/*      */     
/*  168 */     this._serializerProvider = base._serializerProvider;
/*  169 */     this._serializerFactory = base._serializerFactory;
/*  170 */     this._generatorFactory = base._generatorFactory;
/*      */     
/*  172 */     this._generatorSettings = base._generatorSettings;
/*  173 */     this._prefetch = base._prefetch;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter(ObjectWriter base, JsonFactory f)
/*      */   {
/*  182 */     this._config = base._config.with(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, f.requiresPropertyOrdering());
/*      */     
/*      */ 
/*  185 */     this._serializerProvider = base._serializerProvider;
/*  186 */     this._serializerFactory = base._serializerFactory;
/*  187 */     this._generatorFactory = f;
/*      */     
/*  189 */     this._generatorSettings = base._generatorSettings;
/*  190 */     this._prefetch = base._prefetch;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public com.fasterxml.jackson.core.Version version()
/*      */   {
/*  199 */     return com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION;
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
/*      */   protected ObjectWriter _new(ObjectWriter base, JsonFactory f)
/*      */   {
/*  216 */     return new ObjectWriter(base, f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter _new(ObjectWriter base, SerializationConfig config)
/*      */   {
/*  225 */     return new ObjectWriter(base, config);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter _new(GeneratorSettings genSettings, Prefetch prefetch)
/*      */   {
/*  236 */     return new ObjectWriter(this, this._config, genSettings, prefetch);
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
/*      */   protected SequenceWriter _newSequenceWriter(boolean wrapInArray, JsonGenerator gen, boolean managedInput)
/*      */     throws IOException
/*      */   {
/*  250 */     _configureGenerator(gen);
/*  251 */     return new SequenceWriter(_serializerProvider(), gen, managedInput, this._prefetch).init(wrapInArray);
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
/*      */   public ObjectWriter with(SerializationFeature feature)
/*      */   {
/*  267 */     SerializationConfig newConfig = this._config.with(feature);
/*  268 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter with(SerializationFeature first, SerializationFeature... other)
/*      */   {
/*  276 */     SerializationConfig newConfig = this._config.with(first, other);
/*  277 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter withFeatures(SerializationFeature... features)
/*      */   {
/*  285 */     SerializationConfig newConfig = this._config.withFeatures(features);
/*  286 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter without(SerializationFeature feature)
/*      */   {
/*  294 */     SerializationConfig newConfig = this._config.without(feature);
/*  295 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter without(SerializationFeature first, SerializationFeature... other)
/*      */   {
/*  303 */     SerializationConfig newConfig = this._config.without(first, other);
/*  304 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter withoutFeatures(SerializationFeature... features)
/*      */   {
/*  312 */     SerializationConfig newConfig = this._config.withoutFeatures(features);
/*  313 */     return newConfig == this._config ? this : _new(this, newConfig);
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
/*      */   public ObjectWriter with(JsonGenerator.Feature feature)
/*      */   {
/*  326 */     SerializationConfig newConfig = this._config.with(feature);
/*  327 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter withFeatures(JsonGenerator.Feature... features)
/*      */   {
/*  334 */     SerializationConfig newConfig = this._config.withFeatures(features);
/*  335 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter without(JsonGenerator.Feature feature)
/*      */   {
/*  342 */     SerializationConfig newConfig = this._config.without(feature);
/*  343 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter withoutFeatures(JsonGenerator.Feature... features)
/*      */   {
/*  350 */     SerializationConfig newConfig = this._config.withoutFeatures(features);
/*  351 */     return newConfig == this._config ? this : _new(this, newConfig);
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
/*      */   public ObjectWriter with(FormatFeature feature)
/*      */   {
/*  364 */     SerializationConfig newConfig = this._config.with(feature);
/*  365 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter withFeatures(FormatFeature... features)
/*      */   {
/*  372 */     SerializationConfig newConfig = this._config.withFeatures(features);
/*  373 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter without(FormatFeature feature)
/*      */   {
/*  380 */     SerializationConfig newConfig = this._config.without(feature);
/*  381 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter withoutFeatures(FormatFeature... features)
/*      */   {
/*  388 */     SerializationConfig newConfig = this._config.withoutFeatures(features);
/*  389 */     return newConfig == this._config ? this : _new(this, newConfig);
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
/*      */   public ObjectWriter forType(JavaType rootType)
/*      */   {
/*  410 */     Prefetch pf = this._prefetch.forRootType(this, rootType);
/*  411 */     return pf == this._prefetch ? this : _new(this._generatorSettings, pf);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter forType(Class<?> rootType)
/*      */   {
/*  422 */     if (rootType == Object.class) {
/*  423 */       return forType((JavaType)null);
/*      */     }
/*  425 */     return forType(this._config.constructType(rootType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter forType(TypeReference<?> rootType)
/*      */   {
/*  436 */     return forType(this._config.getTypeFactory().constructType(rootType.getType()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectWriter withType(JavaType rootType)
/*      */   {
/*  444 */     return forType(rootType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectWriter withType(Class<?> rootType)
/*      */   {
/*  452 */     return forType(rootType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectWriter withType(TypeReference<?> rootType)
/*      */   {
/*  460 */     return forType(rootType);
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
/*      */   public ObjectWriter with(DateFormat df)
/*      */   {
/*  478 */     SerializationConfig newConfig = this._config.with(df);
/*  479 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter withDefaultPrettyPrinter()
/*      */   {
/*  487 */     return with(this._config.getDefaultPrettyPrinter());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter with(FilterProvider filterProvider)
/*      */   {
/*  495 */     return filterProvider == this._config.getFilterProvider() ? this : _new(this, this._config.withFilters(filterProvider));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter with(PrettyPrinter pp)
/*      */   {
/*  504 */     GeneratorSettings genSet = this._generatorSettings.with(pp);
/*  505 */     if (genSet == this._generatorSettings) {
/*  506 */       return this;
/*      */     }
/*  508 */     return _new(genSet, this._prefetch);
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
/*      */   public ObjectWriter withRootName(String rootName)
/*      */   {
/*  523 */     SerializationConfig newConfig = (SerializationConfig)this._config.withRootName(rootName);
/*  524 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter withRootName(PropertyName rootName)
/*      */   {
/*  531 */     SerializationConfig newConfig = this._config.withRootName(rootName);
/*  532 */     return newConfig == this._config ? this : _new(this, newConfig);
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
/*      */   public ObjectWriter withoutRootName()
/*      */   {
/*  546 */     SerializationConfig newConfig = this._config.withRootName(PropertyName.NO_NAME);
/*  547 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter with(FormatSchema schema)
/*      */   {
/*  558 */     GeneratorSettings genSet = this._generatorSettings.with(schema);
/*  559 */     if (genSet == this._generatorSettings) {
/*  560 */       return this;
/*      */     }
/*  562 */     _verifySchemaType(schema);
/*  563 */     return _new(genSet, this._prefetch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectWriter withSchema(FormatSchema schema)
/*      */   {
/*  571 */     return with(schema);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter withView(Class<?> view)
/*      */   {
/*  583 */     SerializationConfig newConfig = this._config.withView(view);
/*  584 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */   public ObjectWriter with(Locale l) {
/*  588 */     SerializationConfig newConfig = this._config.with(l);
/*  589 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */   public ObjectWriter with(java.util.TimeZone tz) {
/*  593 */     SerializationConfig newConfig = this._config.with(tz);
/*  594 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter with(com.fasterxml.jackson.core.Base64Variant b64variant)
/*      */   {
/*  604 */     SerializationConfig newConfig = this._config.with(b64variant);
/*  605 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter with(CharacterEscapes escapes)
/*      */   {
/*  612 */     GeneratorSettings genSet = this._generatorSettings.with(escapes);
/*  613 */     if (genSet == this._generatorSettings) {
/*  614 */       return this;
/*      */     }
/*  616 */     return _new(genSet, this._prefetch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter with(JsonFactory f)
/*      */   {
/*  623 */     return f == this._generatorFactory ? this : _new(this, f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter with(ContextAttributes attrs)
/*      */   {
/*  630 */     SerializationConfig newConfig = this._config.with(attrs);
/*  631 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter withAttributes(Map<?, ?> attrs)
/*      */   {
/*  641 */     SerializationConfig newConfig = (SerializationConfig)this._config.withAttributes(attrs);
/*  642 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter withAttribute(Object key, Object value)
/*      */   {
/*  649 */     SerializationConfig newConfig = (SerializationConfig)this._config.withAttribute(key, value);
/*  650 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter withoutAttribute(Object key)
/*      */   {
/*  657 */     SerializationConfig newConfig = (SerializationConfig)this._config.withoutAttribute(key);
/*  658 */     return newConfig == this._config ? this : _new(this, newConfig);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter withRootValueSeparator(String sep)
/*      */   {
/*  665 */     GeneratorSettings genSet = this._generatorSettings.withRootValueSeparator(sep);
/*  666 */     if (genSet == this._generatorSettings) {
/*  667 */       return this;
/*      */     }
/*  669 */     return _new(genSet, this._prefetch);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectWriter withRootValueSeparator(SerializableString sep)
/*      */   {
/*  676 */     GeneratorSettings genSet = this._generatorSettings.withRootValueSeparator(sep);
/*  677 */     if (genSet == this._generatorSettings) {
/*  678 */       return this;
/*      */     }
/*  680 */     return _new(genSet, this._prefetch);
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
/*      */   public SequenceWriter writeValues(File out)
/*      */     throws IOException
/*      */   {
/*  703 */     return _newSequenceWriter(false, this._generatorFactory.createGenerator(out, JsonEncoding.UTF8), true);
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
/*      */   public SequenceWriter writeValues(JsonGenerator gen)
/*      */     throws IOException
/*      */   {
/*  723 */     _configureGenerator(gen);
/*  724 */     return _newSequenceWriter(false, gen, false);
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
/*      */   public SequenceWriter writeValues(Writer out)
/*      */     throws IOException
/*      */   {
/*  741 */     return _newSequenceWriter(false, this._generatorFactory.createGenerator(out), true);
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
/*      */   public SequenceWriter writeValues(OutputStream out)
/*      */     throws IOException
/*      */   {
/*  759 */     return _newSequenceWriter(false, this._generatorFactory.createGenerator(out, JsonEncoding.UTF8), true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public SequenceWriter writeValues(DataOutput out)
/*      */     throws IOException
/*      */   {
/*  767 */     return _newSequenceWriter(false, this._generatorFactory.createGenerator(out), true);
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
/*      */   public SequenceWriter writeValuesAsArray(File out)
/*      */     throws IOException
/*      */   {
/*  787 */     return _newSequenceWriter(true, this._generatorFactory.createGenerator(out, JsonEncoding.UTF8), true);
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
/*      */   public SequenceWriter writeValuesAsArray(JsonGenerator gen)
/*      */     throws IOException
/*      */   {
/*  808 */     return _newSequenceWriter(true, gen, false);
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
/*      */   public SequenceWriter writeValuesAsArray(Writer out)
/*      */     throws IOException
/*      */   {
/*  827 */     return _newSequenceWriter(true, this._generatorFactory.createGenerator(out), true);
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
/*      */   public SequenceWriter writeValuesAsArray(OutputStream out)
/*      */     throws IOException
/*      */   {
/*  846 */     return _newSequenceWriter(true, this._generatorFactory.createGenerator(out, JsonEncoding.UTF8), true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public SequenceWriter writeValuesAsArray(DataOutput out)
/*      */     throws IOException
/*      */   {
/*  854 */     return _newSequenceWriter(true, this._generatorFactory.createGenerator(out), true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEnabled(SerializationFeature f)
/*      */   {
/*  864 */     return this._config.isEnabled(f);
/*      */   }
/*      */   
/*      */   public boolean isEnabled(MapperFeature f) {
/*  868 */     return this._config.isEnabled(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public boolean isEnabled(JsonParser.Feature f)
/*      */   {
/*  876 */     return this._generatorFactory.isEnabled(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public boolean isEnabled(JsonGenerator.Feature f)
/*      */   {
/*  883 */     return this._generatorFactory.isEnabled(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public SerializationConfig getConfig()
/*      */   {
/*  890 */     return this._config;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JsonFactory getFactory()
/*      */   {
/*  897 */     return this._generatorFactory;
/*      */   }
/*      */   
/*      */   public com.fasterxml.jackson.databind.type.TypeFactory getTypeFactory() {
/*  901 */     return this._config.getTypeFactory();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasPrefetchedSerializer()
/*      */   {
/*  913 */     return this._prefetch.hasSerializer();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ContextAttributes getAttributes()
/*      */   {
/*  920 */     return this._config.getAttributes();
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
/*      */   public void writeValue(JsonGenerator gen, Object value)
/*      */     throws IOException
/*      */   {
/*  935 */     _configureGenerator(gen);
/*  936 */     if ((this._config.isEnabled(SerializationFeature.CLOSE_CLOSEABLE)) && ((value instanceof Closeable)))
/*      */     {
/*      */ 
/*  939 */       Closeable toClose = (Closeable)value;
/*      */       try {
/*  941 */         this._prefetch.serialize(gen, value, _serializerProvider());
/*  942 */         if (this._config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/*  943 */           gen.flush();
/*      */         }
/*      */       } catch (Exception e) {
/*  946 */         ClassUtil.closeOnFailAndThrowAsIAE(null, toClose, e);
/*  947 */         return;
/*      */       }
/*  949 */       toClose.close();
/*      */     } else {
/*  951 */       this._prefetch.serialize(gen, value, _serializerProvider());
/*  952 */       if (this._config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/*  953 */         gen.flush();
/*      */       }
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
/*      */   public void writeValue(File resultFile, Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/*  971 */     _configAndWriteValue(this._generatorFactory.createGenerator(resultFile, JsonEncoding.UTF8), value);
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
/*      */   public void writeValue(OutputStream out, Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/*  988 */     _configAndWriteValue(this._generatorFactory.createGenerator(out, JsonEncoding.UTF8), value);
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
/*      */   public void writeValue(Writer w, Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 1004 */     _configAndWriteValue(this._generatorFactory.createGenerator(w), value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeValue(DataOutput out, Object value)
/*      */     throws IOException
/*      */   {
/* 1013 */     _configAndWriteValue(this._generatorFactory.createGenerator(out), value);
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
/*      */   public String writeValueAsString(Object value)
/*      */     throws JsonProcessingException
/*      */   {
/* 1029 */     SegmentedStringWriter sw = new SegmentedStringWriter(this._generatorFactory._getBufferRecycler());
/*      */     try {
/* 1031 */       _configAndWriteValue(this._generatorFactory.createGenerator(sw), value);
/*      */     } catch (JsonProcessingException e) {
/* 1033 */       throw e;
/*      */     } catch (IOException e) {
/* 1035 */       throw JsonMappingException.fromUnexpectedIOE(e);
/*      */     }
/* 1037 */     return sw.getAndClear();
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
/*      */   public byte[] writeValueAsBytes(Object value)
/*      */     throws JsonProcessingException
/*      */   {
/* 1053 */     ByteArrayBuilder bb = new ByteArrayBuilder(this._generatorFactory._getBufferRecycler());
/*      */     try {
/* 1055 */       _configAndWriteValue(this._generatorFactory.createGenerator(bb, JsonEncoding.UTF8), value);
/*      */     } catch (JsonProcessingException e) {
/* 1057 */       throw e;
/*      */     } catch (IOException e) {
/* 1059 */       throw JsonMappingException.fromUnexpectedIOE(e);
/*      */     }
/* 1061 */     byte[] result = bb.toByteArray();
/* 1062 */     bb.release();
/* 1063 */     return result;
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
/*      */   public void acceptJsonFormatVisitor(JavaType type, JsonFormatVisitorWrapper visitor)
/*      */     throws JsonMappingException
/*      */   {
/* 1086 */     if (type == null) {
/* 1087 */       throw new IllegalArgumentException("type must be provided");
/*      */     }
/* 1089 */     _serializerProvider().acceptJsonFormatVisitor(type, visitor);
/*      */   }
/*      */   
/*      */ 
/*      */   public void acceptJsonFormatVisitor(Class<?> rawType, JsonFormatVisitorWrapper visitor)
/*      */     throws JsonMappingException
/*      */   {
/* 1096 */     acceptJsonFormatVisitor(this._config.constructType(rawType), visitor);
/*      */   }
/*      */   
/*      */   public boolean canSerialize(Class<?> type) {
/* 1100 */     return _serializerProvider().hasSerializerFor(type, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canSerialize(Class<?> type, java.util.concurrent.atomic.AtomicReference<Throwable> cause)
/*      */   {
/* 1110 */     return _serializerProvider().hasSerializerFor(type, cause);
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
/*      */   protected DefaultSerializerProvider _serializerProvider()
/*      */   {
/* 1124 */     return this._serializerProvider.createInstance(this._config, this._serializerFactory);
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
/*      */   protected void _verifySchemaType(FormatSchema schema)
/*      */   {
/* 1138 */     if ((schema != null) && 
/* 1139 */       (!this._generatorFactory.canUseSchema(schema))) {
/* 1140 */       throw new IllegalArgumentException("Can not use FormatSchema of type " + schema.getClass().getName() + " for format " + this._generatorFactory.getFormatName());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _configAndWriteValue(JsonGenerator gen, Object value)
/*      */     throws IOException
/*      */   {
/* 1152 */     _configureGenerator(gen);
/* 1153 */     if ((this._config.isEnabled(SerializationFeature.CLOSE_CLOSEABLE)) && ((value instanceof Closeable))) {
/* 1154 */       _writeCloseable(gen, value);
/* 1155 */       return;
/*      */     }
/*      */     try {
/* 1158 */       this._prefetch.serialize(gen, value, _serializerProvider());
/*      */     } catch (Exception e) {
/* 1160 */       ClassUtil.closeOnFailAndThrowAsIAE(gen, e);
/* 1161 */       return;
/*      */     }
/* 1163 */     gen.close();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _writeCloseable(JsonGenerator gen, Object value)
/*      */     throws IOException
/*      */   {
/* 1173 */     Closeable toClose = (Closeable)value;
/*      */     try {
/* 1175 */       this._prefetch.serialize(gen, value, _serializerProvider());
/* 1176 */       Closeable tmpToClose = toClose;
/* 1177 */       toClose = null;
/* 1178 */       tmpToClose.close();
/*      */     } catch (Exception e) {
/* 1180 */       ClassUtil.closeOnFailAndThrowAsIAE(gen, toClose, e);
/* 1181 */       return;
/*      */     }
/* 1183 */     gen.close();
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
/*      */   protected final void _configureGenerator(JsonGenerator gen)
/*      */   {
/* 1196 */     this._config.initialize(gen);
/* 1197 */     this._generatorSettings.initialize(gen);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final class GeneratorSettings
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1218 */     public static final GeneratorSettings empty = new GeneratorSettings(null, null, null, null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public final PrettyPrinter prettyPrinter;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public final FormatSchema schema;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public final CharacterEscapes characterEscapes;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public final SerializableString rootValueSeparator;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public GeneratorSettings(PrettyPrinter pp, FormatSchema sch, CharacterEscapes esc, SerializableString rootSep)
/*      */     {
/* 1249 */       this.prettyPrinter = pp;
/* 1250 */       this.schema = sch;
/* 1251 */       this.characterEscapes = esc;
/* 1252 */       this.rootValueSeparator = rootSep;
/*      */     }
/*      */     
/*      */     public GeneratorSettings with(PrettyPrinter pp)
/*      */     {
/* 1257 */       if (pp == null) {
/* 1258 */         pp = ObjectWriter.NULL_PRETTY_PRINTER;
/*      */       }
/* 1260 */       return pp == this.prettyPrinter ? this : new GeneratorSettings(pp, this.schema, this.characterEscapes, this.rootValueSeparator);
/*      */     }
/*      */     
/*      */     public GeneratorSettings with(FormatSchema sch)
/*      */     {
/* 1265 */       return this.schema == sch ? this : new GeneratorSettings(this.prettyPrinter, sch, this.characterEscapes, this.rootValueSeparator);
/*      */     }
/*      */     
/*      */     public GeneratorSettings with(CharacterEscapes esc)
/*      */     {
/* 1270 */       return this.characterEscapes == esc ? this : new GeneratorSettings(this.prettyPrinter, this.schema, esc, this.rootValueSeparator);
/*      */     }
/*      */     
/*      */     public GeneratorSettings withRootValueSeparator(String sep)
/*      */     {
/* 1275 */       if (sep == null) {
/* 1276 */         if (this.rootValueSeparator == null) {
/* 1277 */           return this;
/*      */         }
/* 1279 */       } else if (sep.equals(this.rootValueSeparator)) {
/* 1280 */         return this;
/*      */       }
/* 1282 */       return new GeneratorSettings(this.prettyPrinter, this.schema, this.characterEscapes, sep == null ? null : new com.fasterxml.jackson.core.io.SerializedString(sep));
/*      */     }
/*      */     
/*      */     public GeneratorSettings withRootValueSeparator(SerializableString sep)
/*      */     {
/* 1287 */       if (sep == null) {
/* 1288 */         if (this.rootValueSeparator == null) {
/* 1289 */           return this;
/*      */         }
/*      */       }
/* 1292 */       else if ((this.rootValueSeparator != null) && (sep.getValue().equals(this.rootValueSeparator.getValue())))
/*      */       {
/* 1294 */         return this;
/*      */       }
/*      */       
/* 1297 */       return new GeneratorSettings(this.prettyPrinter, this.schema, this.characterEscapes, sep);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     public void initialize(JsonGenerator gen)
/*      */     {
/* 1305 */       PrettyPrinter pp = this.prettyPrinter;
/* 1306 */       if (this.prettyPrinter != null) {
/* 1307 */         if (pp == ObjectWriter.NULL_PRETTY_PRINTER) {
/* 1308 */           gen.setPrettyPrinter(null);
/*      */         } else {
/* 1310 */           if ((pp instanceof Instantiatable)) {
/* 1311 */             pp = (PrettyPrinter)((Instantiatable)pp).createInstance();
/*      */           }
/* 1313 */           gen.setPrettyPrinter(pp);
/*      */         }
/*      */       }
/* 1316 */       if (this.characterEscapes != null) {
/* 1317 */         gen.setCharacterEscapes(this.characterEscapes);
/*      */       }
/* 1319 */       if (this.schema != null) {
/* 1320 */         gen.setSchema(this.schema);
/*      */       }
/* 1322 */       if (this.rootValueSeparator != null) {
/* 1323 */         gen.setRootValueSeparator(this.rootValueSeparator);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final class Prefetch
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1340 */     public static final Prefetch empty = new Prefetch(null, null, null);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final JavaType rootType;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final JsonSerializer<Object> valueSerializer;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final TypeSerializer typeSerializer;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private Prefetch(JavaType rootT, JsonSerializer<Object> ser, TypeSerializer typeSer)
/*      */     {
/* 1366 */       this.rootType = rootT;
/* 1367 */       this.valueSerializer = ser;
/* 1368 */       this.typeSerializer = typeSer;
/*      */     }
/*      */     
/*      */ 
/*      */     public Prefetch forRootType(ObjectWriter parent, JavaType newType)
/*      */     {
/* 1374 */       boolean noType = (newType == null) || (newType.isJavaLangObject());
/*      */       
/* 1376 */       if (noType) {
/* 1377 */         if ((this.rootType == null) || (this.valueSerializer == null)) {
/* 1378 */           return this;
/*      */         }
/* 1380 */         return new Prefetch(null, null, this.typeSerializer);
/*      */       }
/* 1382 */       if (newType.equals(this.rootType)) {
/* 1383 */         return this;
/*      */       }
/* 1385 */       if (parent.isEnabled(SerializationFeature.EAGER_SERIALIZER_FETCH)) {
/* 1386 */         DefaultSerializerProvider prov = parent._serializerProvider();
/*      */         
/*      */ 
/*      */ 
/*      */         try
/*      */         {
/* 1392 */           JsonSerializer<Object> ser = prov.findTypedValueSerializer(newType, true, null);
/*      */           
/* 1394 */           if ((ser instanceof TypeWrappedSerializer)) {
/* 1395 */             return new Prefetch(newType, null, ((TypeWrappedSerializer)ser).typeSerializer());
/*      */           }
/*      */           
/* 1398 */           return new Prefetch(newType, ser, null);
/*      */         }
/*      */         catch (JsonProcessingException e) {}
/*      */       }
/*      */       
/*      */ 
/* 1404 */       return new Prefetch(newType, null, this.typeSerializer);
/*      */     }
/*      */     
/*      */     public final JsonSerializer<Object> getValueSerializer() {
/* 1408 */       return this.valueSerializer;
/*      */     }
/*      */     
/*      */     public final TypeSerializer getTypeSerializer() {
/* 1412 */       return this.typeSerializer;
/*      */     }
/*      */     
/*      */     public boolean hasSerializer() {
/* 1416 */       return (this.valueSerializer != null) || (this.typeSerializer != null);
/*      */     }
/*      */     
/*      */     public void serialize(JsonGenerator gen, Object value, DefaultSerializerProvider prov)
/*      */       throws IOException
/*      */     {
/* 1422 */       if (this.typeSerializer != null) {
/* 1423 */         prov.serializePolymorphic(gen, value, this.rootType, this.valueSerializer, this.typeSerializer);
/* 1424 */       } else if (this.valueSerializer != null) {
/* 1425 */         prov.serializeValue(gen, value, this.rootType, this.valueSerializer);
/* 1426 */       } else if (this.rootType != null) {
/* 1427 */         prov.serializeValue(gen, value, this.rootType);
/*      */       } else {
/* 1429 */         prov.serializeValue(gen, value);
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ObjectWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */