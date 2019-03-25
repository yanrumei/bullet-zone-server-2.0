/*      */ package com.fasterxml.jackson.databind;
/*      */ 
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.FormatFeature;
/*      */ import com.fasterxml.jackson.core.FormatSchema;
/*      */ import com.fasterxml.jackson.core.JsonFactory;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*      */ import com.fasterxml.jackson.core.JsonPointer;
/*      */ import com.fasterxml.jackson.core.JsonProcessingException;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.TreeNode;
/*      */ import com.fasterxml.jackson.core.filter.FilteringParserDelegate;
/*      */ import com.fasterxml.jackson.core.filter.JsonPointerBasedFilter;
/*      */ import com.fasterxml.jackson.core.filter.TokenFilter;
/*      */ import com.fasterxml.jackson.core.type.ResolvedType;
/*      */ import com.fasterxml.jackson.core.type.TypeReference;
/*      */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*      */ import com.fasterxml.jackson.databind.deser.DataFormatReaders;
/*      */ import com.fasterxml.jackson.databind.deser.DataFormatReaders.Match;
/*      */ import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
/*      */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import java.io.DataInput;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.lang.reflect.Type;
/*      */ import java.net.URL;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ 
/*      */ public class ObjectReader extends com.fasterxml.jackson.core.ObjectCodec implements com.fasterxml.jackson.core.Versioned, java.io.Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   42 */   private static final JavaType JSON_NODE_TYPE = com.fasterxml.jackson.databind.type.SimpleType.constructUnsafe(JsonNode.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final DeserializationConfig _config;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final DefaultDeserializationContext _context;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JsonFactory _parserFactory;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final boolean _unwrapRoot;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final TokenFilter _filter;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JavaType _valueType;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JsonDeserializer<Object> _rootDeserializer;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Object _valueToUpdate;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final FormatSchema _schema;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final InjectableValues _injectableValues;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final DataFormatReaders _dataFormatReaders;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectReader(ObjectMapper mapper, DeserializationConfig config)
/*      */   {
/*  161 */     this(mapper, config, null, null, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectReader(ObjectMapper mapper, DeserializationConfig config, JavaType valueType, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues)
/*      */   {
/*  172 */     this._config = config;
/*  173 */     this._context = mapper._deserializationContext;
/*  174 */     this._rootDeserializers = mapper._rootDeserializers;
/*  175 */     this._parserFactory = mapper._jsonFactory;
/*  176 */     this._valueType = valueType;
/*  177 */     this._valueToUpdate = valueToUpdate;
/*  178 */     if ((valueToUpdate != null) && (valueType.isArrayType())) {
/*  179 */       throw new IllegalArgumentException("Can not update an array value");
/*      */     }
/*  181 */     this._schema = schema;
/*  182 */     this._injectableValues = injectableValues;
/*  183 */     this._unwrapRoot = config.useRootWrapping();
/*      */     
/*  185 */     this._rootDeserializer = _prefetchRootDeserializer(valueType);
/*  186 */     this._dataFormatReaders = null;
/*  187 */     this._filter = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectReader(ObjectReader base, DeserializationConfig config, JavaType valueType, JsonDeserializer<Object> rootDeser, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues, DataFormatReaders dataFormatReaders)
/*      */   {
/*  198 */     this._config = config;
/*  199 */     this._context = base._context;
/*      */     
/*  201 */     this._rootDeserializers = base._rootDeserializers;
/*  202 */     this._parserFactory = base._parserFactory;
/*      */     
/*  204 */     this._valueType = valueType;
/*  205 */     this._rootDeserializer = rootDeser;
/*  206 */     this._valueToUpdate = valueToUpdate;
/*  207 */     if ((valueToUpdate != null) && (valueType.isArrayType())) {
/*  208 */       throw new IllegalArgumentException("Can not update an array value");
/*      */     }
/*  210 */     this._schema = schema;
/*  211 */     this._injectableValues = injectableValues;
/*  212 */     this._unwrapRoot = config.useRootWrapping();
/*  213 */     this._dataFormatReaders = dataFormatReaders;
/*  214 */     this._filter = base._filter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectReader(ObjectReader base, DeserializationConfig config)
/*      */   {
/*  222 */     this._config = config;
/*  223 */     this._context = base._context;
/*      */     
/*  225 */     this._rootDeserializers = base._rootDeserializers;
/*  226 */     this._parserFactory = base._parserFactory;
/*      */     
/*  228 */     this._valueType = base._valueType;
/*  229 */     this._rootDeserializer = base._rootDeserializer;
/*  230 */     this._valueToUpdate = base._valueToUpdate;
/*  231 */     this._schema = base._schema;
/*  232 */     this._injectableValues = base._injectableValues;
/*  233 */     this._unwrapRoot = config.useRootWrapping();
/*  234 */     this._dataFormatReaders = base._dataFormatReaders;
/*  235 */     this._filter = base._filter;
/*      */   }
/*      */   
/*      */ 
/*      */   protected ObjectReader(ObjectReader base, JsonFactory f)
/*      */   {
/*  241 */     this._config = base._config.with(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, f.requiresPropertyOrdering());
/*      */     
/*  243 */     this._context = base._context;
/*      */     
/*  245 */     this._rootDeserializers = base._rootDeserializers;
/*  246 */     this._parserFactory = f;
/*      */     
/*  248 */     this._valueType = base._valueType;
/*  249 */     this._rootDeserializer = base._rootDeserializer;
/*  250 */     this._valueToUpdate = base._valueToUpdate;
/*  251 */     this._schema = base._schema;
/*  252 */     this._injectableValues = base._injectableValues;
/*  253 */     this._unwrapRoot = base._unwrapRoot;
/*  254 */     this._dataFormatReaders = base._dataFormatReaders;
/*  255 */     this._filter = base._filter;
/*      */   }
/*      */   
/*      */   protected ObjectReader(ObjectReader base, TokenFilter filter) {
/*  259 */     this._config = base._config;
/*  260 */     this._context = base._context;
/*  261 */     this._rootDeserializers = base._rootDeserializers;
/*  262 */     this._parserFactory = base._parserFactory;
/*  263 */     this._valueType = base._valueType;
/*  264 */     this._rootDeserializer = base._rootDeserializer;
/*  265 */     this._valueToUpdate = base._valueToUpdate;
/*  266 */     this._schema = base._schema;
/*  267 */     this._injectableValues = base._injectableValues;
/*  268 */     this._unwrapRoot = base._unwrapRoot;
/*  269 */     this._dataFormatReaders = base._dataFormatReaders;
/*  270 */     this._filter = filter;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public com.fasterxml.jackson.core.Version version()
/*      */   {
/*  279 */     return com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION;
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
/*      */   protected ObjectReader _new(ObjectReader base, JsonFactory f)
/*      */   {
/*  296 */     return new ObjectReader(base, f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectReader _new(ObjectReader base, DeserializationConfig config)
/*      */   {
/*  305 */     return new ObjectReader(base, config);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectReader _new(ObjectReader base, DeserializationConfig config, JavaType valueType, JsonDeserializer<Object> rootDeser, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues, DataFormatReaders dataFormatReaders)
/*      */   {
/*  317 */     return new ObjectReader(base, config, valueType, rootDeser, valueToUpdate, schema, injectableValues, dataFormatReaders);
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
/*      */   protected <T> MappingIterator<T> _newIterator(JsonParser p, DeserializationContext ctxt, JsonDeserializer<?> deser, boolean parserManaged)
/*      */   {
/*  330 */     return new MappingIterator(this._valueType, p, ctxt, deser, parserManaged, this._valueToUpdate);
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
/*      */   protected JsonToken _initForReading(DeserializationContext ctxt, JsonParser p)
/*      */     throws IOException
/*      */   {
/*  344 */     if (this._schema != null) {
/*  345 */       p.setSchema(this._schema);
/*      */     }
/*  347 */     this._config.initialize(p);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  353 */     JsonToken t = p.getCurrentToken();
/*  354 */     if (t == null) {
/*  355 */       t = p.nextToken();
/*  356 */       if (t == null)
/*      */       {
/*  358 */         ctxt.reportMissingContent(null, new Object[0]);
/*      */       }
/*      */     }
/*  361 */     return t;
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
/*      */   protected void _initForMultiRead(DeserializationContext ctxt, JsonParser p)
/*      */     throws IOException
/*      */   {
/*  376 */     if (this._schema != null) {
/*  377 */       p.setSchema(this._schema);
/*      */     }
/*  379 */     this._config.initialize(p);
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
/*      */   public ObjectReader with(DeserializationFeature feature)
/*      */   {
/*  393 */     return _with(this._config.with(feature));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader with(DeserializationFeature first, DeserializationFeature... other)
/*      */   {
/*  403 */     return _with(this._config.with(first, other));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader withFeatures(DeserializationFeature... features)
/*      */   {
/*  411 */     return _with(this._config.withFeatures(features));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader without(DeserializationFeature feature)
/*      */   {
/*  419 */     return _with(this._config.without(feature));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader without(DeserializationFeature first, DeserializationFeature... other)
/*      */   {
/*  428 */     return _with(this._config.without(first, other));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader withoutFeatures(DeserializationFeature... features)
/*      */   {
/*  436 */     return _with(this._config.withoutFeatures(features));
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
/*      */   public ObjectReader with(JsonParser.Feature feature)
/*      */   {
/*  450 */     return _with(this._config.with(feature));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader withFeatures(JsonParser.Feature... features)
/*      */   {
/*  458 */     return _with(this._config.withFeatures(features));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader without(JsonParser.Feature feature)
/*      */   {
/*  466 */     return _with(this._config.without(feature));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader withoutFeatures(JsonParser.Feature... features)
/*      */   {
/*  474 */     return _with(this._config.withoutFeatures(features));
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
/*      */   public ObjectReader with(FormatFeature feature)
/*      */   {
/*  490 */     return _with(this._config.with(feature));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader withFeatures(FormatFeature... features)
/*      */   {
/*  500 */     return _with(this._config.withFeatures(features));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader without(FormatFeature feature)
/*      */   {
/*  510 */     return _with(this._config.without(feature));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader withoutFeatures(FormatFeature... features)
/*      */   {
/*  520 */     return _with(this._config.withoutFeatures(features));
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
/*      */   public ObjectReader at(String value)
/*      */   {
/*  535 */     return new ObjectReader(this, new JsonPointerBasedFilter(value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader at(JsonPointer pointer)
/*      */   {
/*  544 */     return new ObjectReader(this, new JsonPointerBasedFilter(pointer));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader with(DeserializationConfig config)
/*      */   {
/*  555 */     return _with(config);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader with(InjectableValues injectableValues)
/*      */   {
/*  567 */     if (this._injectableValues == injectableValues) {
/*  568 */       return this;
/*      */     }
/*  570 */     return _new(this, this._config, this._valueType, this._rootDeserializer, this._valueToUpdate, this._schema, injectableValues, this._dataFormatReaders);
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
/*      */   public ObjectReader with(JsonNodeFactory f)
/*      */   {
/*  584 */     return _with(this._config.with(f));
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
/*      */   public ObjectReader with(JsonFactory f)
/*      */   {
/*  599 */     if (f == this._parserFactory) {
/*  600 */       return this;
/*      */     }
/*  602 */     ObjectReader r = _new(this, f);
/*      */     
/*  604 */     if (f.getCodec() == null) {
/*  605 */       f.setCodec(r);
/*      */     }
/*  607 */     return r;
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
/*      */   public ObjectReader withRootName(String rootName)
/*      */   {
/*  620 */     return _with((DeserializationConfig)this._config.withRootName(rootName));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectReader withRootName(PropertyName rootName)
/*      */   {
/*  627 */     return _with(this._config.withRootName(rootName));
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
/*      */   public ObjectReader withoutRootName()
/*      */   {
/*  641 */     return _with(this._config.withRootName(PropertyName.NO_NAME));
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
/*      */   public ObjectReader with(FormatSchema schema)
/*      */   {
/*  654 */     if (this._schema == schema) {
/*  655 */       return this;
/*      */     }
/*  657 */     _verifySchemaType(schema);
/*  658 */     return _new(this, this._config, this._valueType, this._rootDeserializer, this._valueToUpdate, schema, this._injectableValues, this._dataFormatReaders);
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
/*      */   public ObjectReader forType(JavaType valueType)
/*      */   {
/*  673 */     if ((valueType != null) && (valueType.equals(this._valueType))) {
/*  674 */       return this;
/*      */     }
/*  676 */     JsonDeserializer<Object> rootDeser = _prefetchRootDeserializer(valueType);
/*      */     
/*  678 */     DataFormatReaders det = this._dataFormatReaders;
/*  679 */     if (det != null) {
/*  680 */       det = det.withType(valueType);
/*      */     }
/*  682 */     return _new(this, this._config, valueType, rootDeser, this._valueToUpdate, this._schema, this._injectableValues, det);
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
/*      */   public ObjectReader forType(Class<?> valueType)
/*      */   {
/*  696 */     return forType(this._config.constructType(valueType));
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
/*      */   public ObjectReader forType(TypeReference<?> valueTypeRef)
/*      */   {
/*  709 */     return forType(this._config.getTypeFactory().constructType(valueTypeRef.getType()));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectReader withType(JavaType valueType)
/*      */   {
/*  717 */     return forType(valueType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectReader withType(Class<?> valueType)
/*      */   {
/*  725 */     return forType(this._config.constructType(valueType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectReader withType(Type valueType)
/*      */   {
/*  733 */     return forType(this._config.getTypeFactory().constructType(valueType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectReader withType(TypeReference<?> valueTypeRef)
/*      */   {
/*  741 */     return forType(this._config.getTypeFactory().constructType(valueTypeRef.getType()));
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
/*      */   public ObjectReader withValueToUpdate(Object value)
/*      */   {
/*  754 */     if (value == this._valueToUpdate) return this;
/*  755 */     if (value == null)
/*      */     {
/*      */ 
/*  758 */       return _new(this, this._config, this._valueType, this._rootDeserializer, null, this._schema, this._injectableValues, this._dataFormatReaders);
/*      */     }
/*      */     
/*      */ 
/*      */     JavaType t;
/*      */     
/*      */ 
/*      */     JavaType t;
/*      */     
/*  767 */     if (this._valueType == null) {
/*  768 */       t = this._config.constructType(value.getClass());
/*      */     } else {
/*  770 */       t = this._valueType;
/*      */     }
/*  772 */     return _new(this, this._config, t, this._rootDeserializer, value, this._schema, this._injectableValues, this._dataFormatReaders);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader withView(Class<?> activeView)
/*      */   {
/*  784 */     return _with(this._config.withView(activeView));
/*      */   }
/*      */   
/*      */   public ObjectReader with(Locale l) {
/*  788 */     return _with(this._config.with(l));
/*      */   }
/*      */   
/*      */   public ObjectReader with(TimeZone tz) {
/*  792 */     return _with(this._config.with(tz));
/*      */   }
/*      */   
/*      */   public ObjectReader withHandler(com.fasterxml.jackson.databind.deser.DeserializationProblemHandler h) {
/*  796 */     return _with(this._config.withHandler(h));
/*      */   }
/*      */   
/*      */   public ObjectReader with(Base64Variant defaultBase64) {
/*  800 */     return _with(this._config.with(defaultBase64));
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
/*      */   public ObjectReader withFormatDetection(ObjectReader... readers)
/*      */   {
/*  826 */     return withFormatDetection(new DataFormatReaders(readers));
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
/*      */   public ObjectReader withFormatDetection(DataFormatReaders readers)
/*      */   {
/*  845 */     return _new(this, this._config, this._valueType, this._rootDeserializer, this._valueToUpdate, this._schema, this._injectableValues, readers);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader with(ContextAttributes attrs)
/*      */   {
/*  853 */     return _with(this._config.with(attrs));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectReader withAttributes(Map<?, ?> attrs)
/*      */   {
/*  860 */     return _with((DeserializationConfig)this._config.withAttributes(attrs));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectReader withAttribute(Object key, Object value)
/*      */   {
/*  867 */     return _with((DeserializationConfig)this._config.withAttribute(key, value));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectReader withoutAttribute(Object key)
/*      */   {
/*  874 */     return _with((DeserializationConfig)this._config.withoutAttribute(key));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectReader _with(DeserializationConfig newConfig)
/*      */   {
/*  884 */     if (newConfig == this._config) {
/*  885 */       return this;
/*      */     }
/*  887 */     ObjectReader r = _new(this, newConfig);
/*  888 */     if (this._dataFormatReaders != null) {
/*  889 */       r = r.withFormatDetection(this._dataFormatReaders.with(newConfig));
/*      */     }
/*  891 */     return r;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEnabled(DeserializationFeature f)
/*      */   {
/*  901 */     return this._config.isEnabled(f);
/*      */   }
/*      */   
/*      */   public boolean isEnabled(MapperFeature f) {
/*  905 */     return this._config.isEnabled(f);
/*      */   }
/*      */   
/*      */   public boolean isEnabled(JsonParser.Feature f) {
/*  909 */     return this._parserFactory.isEnabled(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public DeserializationConfig getConfig()
/*      */   {
/*  916 */     return this._config;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonFactory getFactory()
/*      */   {
/*  924 */     return this._parserFactory;
/*      */   }
/*      */   
/*      */   public TypeFactory getTypeFactory() {
/*  928 */     return this._config.getTypeFactory();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ContextAttributes getAttributes()
/*      */   {
/*  935 */     return this._config.getAttributes();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public InjectableValues getInjectableValues()
/*      */   {
/*  942 */     return this._injectableValues;
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
/*      */   public <T> T readValue(JsonParser p)
/*      */     throws IOException
/*      */   {
/*  964 */     return (T)_bind(p, this._valueToUpdate);
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
/*      */   public <T> T readValue(JsonParser p, Class<T> valueType)
/*      */     throws IOException
/*      */   {
/*  981 */     return (T)forType(valueType).readValue(p);
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
/*      */   public <T> T readValue(JsonParser p, TypeReference<?> valueTypeRef)
/*      */     throws IOException
/*      */   {
/*  998 */     return (T)forType(valueTypeRef).readValue(p);
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
/*      */   public <T> T readValue(JsonParser p, ResolvedType valueType)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1014 */     return (T)forType((JavaType)valueType).readValue(p);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(JsonParser p, JavaType valueType)
/*      */     throws IOException
/*      */   {
/* 1025 */     return (T)forType(valueType).readValue(p);
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
/*      */   public <T> Iterator<T> readValues(JsonParser p, Class<T> valueType)
/*      */     throws IOException
/*      */   {
/* 1049 */     return forType(valueType).readValues(p);
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
/*      */   public <T> Iterator<T> readValues(JsonParser p, TypeReference<?> valueTypeRef)
/*      */     throws IOException
/*      */   {
/* 1073 */     return forType(valueTypeRef).readValues(p);
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
/*      */   public <T> Iterator<T> readValues(JsonParser p, ResolvedType valueType)
/*      */     throws IOException
/*      */   {
/* 1097 */     return readValues(p, (JavaType)valueType);
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
/*      */   public <T> Iterator<T> readValues(JsonParser p, JavaType valueType)
/*      */     throws IOException
/*      */   {
/* 1120 */     return forType(valueType).readValues(p);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonNode createArrayNode()
/*      */   {
/* 1131 */     return this._config.getNodeFactory().arrayNode();
/*      */   }
/*      */   
/*      */   public JsonNode createObjectNode()
/*      */   {
/* 1136 */     return this._config.getNodeFactory().objectNode();
/*      */   }
/*      */   
/*      */   public JsonParser treeAsTokens(TreeNode n)
/*      */   {
/* 1141 */     return new com.fasterxml.jackson.databind.node.TreeTraversingParser((JsonNode)n, this);
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
/*      */   public <T extends TreeNode> T readTree(JsonParser p)
/*      */     throws IOException
/*      */   {
/* 1158 */     return _bindAsTree(p);
/*      */   }
/*      */   
/*      */   public void writeTree(JsonGenerator jgen, TreeNode rootNode)
/*      */   {
/* 1163 */     throw new UnsupportedOperationException();
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
/*      */   public <T> T readValue(InputStream src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1182 */     if (this._dataFormatReaders != null) {
/* 1183 */       return (T)_detectBindAndClose(this._dataFormatReaders.findFormat(src), false);
/*      */     }
/*      */     
/* 1186 */     return (T)_bindAndClose(_considerFilter(this._parserFactory.createParser(src), false));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(Reader src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1199 */     if (this._dataFormatReaders != null) {
/* 1200 */       _reportUndetectableSource(src);
/*      */     }
/*      */     
/* 1203 */     return (T)_bindAndClose(_considerFilter(this._parserFactory.createParser(src), false));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(String src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1216 */     if (this._dataFormatReaders != null) {
/* 1217 */       _reportUndetectableSource(src);
/*      */     }
/*      */     
/* 1220 */     return (T)_bindAndClose(_considerFilter(this._parserFactory.createParser(src), false));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(byte[] src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1233 */     if (this._dataFormatReaders != null) {
/* 1234 */       return (T)_detectBindAndClose(src, 0, src.length);
/*      */     }
/*      */     
/* 1237 */     return (T)_bindAndClose(_considerFilter(this._parserFactory.createParser(src), false));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(byte[] src, int offset, int length)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1250 */     if (this._dataFormatReaders != null) {
/* 1251 */       return (T)_detectBindAndClose(src, offset, length);
/*      */     }
/*      */     
/* 1254 */     return (T)_bindAndClose(_considerFilter(this._parserFactory.createParser(src, offset, length), false));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T> T readValue(File src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1262 */     if (this._dataFormatReaders != null) {
/* 1263 */       return (T)_detectBindAndClose(this._dataFormatReaders.findFormat(_inputStream(src)), true);
/*      */     }
/*      */     
/* 1266 */     return (T)_bindAndClose(_considerFilter(this._parserFactory.createParser(src), false));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(URL src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1279 */     if (this._dataFormatReaders != null) {
/* 1280 */       return (T)_detectBindAndClose(this._dataFormatReaders.findFormat(_inputStream(src)), true);
/*      */     }
/*      */     
/* 1283 */     return (T)_bindAndClose(_considerFilter(this._parserFactory.createParser(src), false));
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
/*      */   public <T> T readValue(JsonNode src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1297 */     if (this._dataFormatReaders != null) {
/* 1298 */       _reportUndetectableSource(src);
/*      */     }
/*      */     
/* 1301 */     return (T)_bindAndClose(_considerFilter(treeAsTokens(src), false));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readValue(DataInput src)
/*      */     throws IOException
/*      */   {
/* 1310 */     if (this._dataFormatReaders != null) {
/* 1311 */       _reportUndetectableSource(src);
/*      */     }
/* 1313 */     return (T)_bindAndClose(_considerFilter(this._parserFactory.createParser(src), false));
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
/*      */   public JsonNode readTree(InputStream in)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1328 */     if (this._dataFormatReaders != null) {
/* 1329 */       return _detectBindAndCloseAsTree(in);
/*      */     }
/* 1331 */     return _bindAndCloseAsTree(_considerFilter(this._parserFactory.createParser(in), false));
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
/*      */   public JsonNode readTree(Reader r)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1346 */     if (this._dataFormatReaders != null) {
/* 1347 */       _reportUndetectableSource(r);
/*      */     }
/* 1349 */     return _bindAndCloseAsTree(_considerFilter(this._parserFactory.createParser(r), false));
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
/*      */   public JsonNode readTree(String json)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1364 */     if (this._dataFormatReaders != null) {
/* 1365 */       _reportUndetectableSource(json);
/*      */     }
/* 1367 */     return _bindAndCloseAsTree(_considerFilter(this._parserFactory.createParser(json), false));
/*      */   }
/*      */   
/*      */   public JsonNode readTree(DataInput src) throws IOException
/*      */   {
/* 1372 */     if (this._dataFormatReaders != null) {
/* 1373 */       _reportUndetectableSource(src);
/*      */     }
/* 1375 */     return _bindAndCloseAsTree(_considerFilter(this._parserFactory.createParser(src), false));
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
/*      */   public <T> MappingIterator<T> readValues(JsonParser p)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1398 */     DeserializationContext ctxt = createDeserializationContext(p);
/*      */     
/* 1400 */     return _newIterator(p, ctxt, _findRootDeserializer(ctxt), false);
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
/*      */   public <T> MappingIterator<T> readValues(InputStream src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1426 */     if (this._dataFormatReaders != null) {
/* 1427 */       return _detectBindAndReadValues(this._dataFormatReaders.findFormat(src), false);
/*      */     }
/*      */     
/* 1430 */     return _bindAndReadValues(_considerFilter(this._parserFactory.createParser(src), true));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> MappingIterator<T> readValues(Reader src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1440 */     if (this._dataFormatReaders != null) {
/* 1441 */       _reportUndetectableSource(src);
/*      */     }
/* 1443 */     JsonParser p = _considerFilter(this._parserFactory.createParser(src), true);
/* 1444 */     DeserializationContext ctxt = createDeserializationContext(p);
/* 1445 */     _initForMultiRead(ctxt, p);
/* 1446 */     p.nextToken();
/* 1447 */     return _newIterator(p, ctxt, _findRootDeserializer(ctxt), true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> MappingIterator<T> readValues(String json)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1459 */     if (this._dataFormatReaders != null) {
/* 1460 */       _reportUndetectableSource(json);
/*      */     }
/* 1462 */     JsonParser p = _considerFilter(this._parserFactory.createParser(json), true);
/* 1463 */     DeserializationContext ctxt = createDeserializationContext(p);
/* 1464 */     _initForMultiRead(ctxt, p);
/* 1465 */     p.nextToken();
/* 1466 */     return _newIterator(p, ctxt, _findRootDeserializer(ctxt), true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> MappingIterator<T> readValues(byte[] src, int offset, int length)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1475 */     if (this._dataFormatReaders != null) {
/* 1476 */       return _detectBindAndReadValues(this._dataFormatReaders.findFormat(src, offset, length), false);
/*      */     }
/* 1478 */     return _bindAndReadValues(_considerFilter(this._parserFactory.createParser(src, offset, length), true));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public final <T> MappingIterator<T> readValues(byte[] src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1487 */     return readValues(src, 0, src.length);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> MappingIterator<T> readValues(File src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1496 */     if (this._dataFormatReaders != null) {
/* 1497 */       return _detectBindAndReadValues(this._dataFormatReaders.findFormat(_inputStream(src)), false);
/*      */     }
/*      */     
/* 1500 */     return _bindAndReadValues(_considerFilter(this._parserFactory.createParser(src), true));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> MappingIterator<T> readValues(URL src)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1511 */     if (this._dataFormatReaders != null) {
/* 1512 */       return _detectBindAndReadValues(this._dataFormatReaders.findFormat(_inputStream(src)), true);
/*      */     }
/*      */     
/* 1515 */     return _bindAndReadValues(_considerFilter(this._parserFactory.createParser(src), true));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T> MappingIterator<T> readValues(DataInput src)
/*      */     throws IOException
/*      */   {
/* 1523 */     if (this._dataFormatReaders != null) {
/* 1524 */       _reportUndetectableSource(src);
/*      */     }
/* 1526 */     return _bindAndReadValues(_considerFilter(this._parserFactory.createParser(src), true));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T treeToValue(TreeNode n, Class<T> valueType)
/*      */     throws JsonProcessingException
/*      */   {
/*      */     try
/*      */     {
/* 1539 */       return (T)readValue(treeAsTokens(n), valueType);
/*      */     } catch (JsonProcessingException e) {
/* 1541 */       throw e;
/*      */     } catch (IOException e) {
/* 1543 */       throw new IllegalArgumentException(e.getMessage(), e);
/*      */     }
/*      */   }
/*      */   
/*      */   public void writeValue(JsonGenerator gen, Object value) throws IOException, JsonProcessingException
/*      */   {
/* 1549 */     throw new UnsupportedOperationException("Not implemented for ObjectReader");
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
/*      */   protected Object _bind(JsonParser p, Object valueToUpdate)
/*      */     throws IOException
/*      */   {
/* 1567 */     DeserializationContext ctxt = createDeserializationContext(p);
/* 1568 */     JsonToken t = _initForReading(ctxt, p);
/* 1569 */     Object result; Object result; if (t == JsonToken.VALUE_NULL) { Object result;
/* 1570 */       if (valueToUpdate == null) {
/* 1571 */         result = _findRootDeserializer(ctxt).getNullValue(ctxt);
/*      */       } else
/* 1573 */         result = valueToUpdate;
/*      */     } else { Object result;
/* 1575 */       if ((t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 1576 */         result = valueToUpdate;
/*      */       } else {
/* 1578 */         JsonDeserializer<Object> deser = _findRootDeserializer(ctxt);
/* 1579 */         Object result; if (this._unwrapRoot) {
/* 1580 */           result = _unwrapAndDeserialize(p, ctxt, this._valueType, deser);
/*      */         } else { Object result;
/* 1582 */           if (valueToUpdate == null) {
/* 1583 */             result = deser.deserialize(p, ctxt);
/*      */           } else {
/* 1585 */             deser.deserialize(p, ctxt, valueToUpdate);
/* 1586 */             result = valueToUpdate;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/* 1591 */     p.clearCurrentToken();
/* 1592 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonParser _considerFilter(JsonParser p, boolean multiValue)
/*      */   {
/* 1601 */     return (this._filter == null) || (FilteringParserDelegate.class.isInstance(p)) ? p : new FilteringParserDelegate(p, this._filter, false, multiValue);
/*      */   }
/*      */   
/*      */   protected Object _bindAndClose(JsonParser p0)
/*      */     throws IOException
/*      */   {
/* 1607 */     JsonParser p = p0;Throwable localThrowable2 = null;
/*      */     try
/*      */     {
/* 1610 */       DeserializationContext ctxt = createDeserializationContext(p);
/* 1611 */       JsonToken t = _initForReading(ctxt, p);
/* 1612 */       Object result; JsonDeserializer<Object> deser; Object result; if (t == JsonToken.VALUE_NULL) { Object result;
/* 1613 */         if (this._valueToUpdate == null) {
/* 1614 */           result = _findRootDeserializer(ctxt).getNullValue(ctxt);
/*      */         } else
/* 1616 */           result = this._valueToUpdate;
/*      */       } else { Object result;
/* 1618 */         if ((t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 1619 */           result = this._valueToUpdate;
/*      */         } else {
/* 1621 */           deser = _findRootDeserializer(ctxt);
/* 1622 */           Object result; if (this._unwrapRoot) {
/* 1623 */             result = _unwrapAndDeserialize(p, ctxt, this._valueType, deser);
/*      */           } else { Object result;
/* 1625 */             if (this._valueToUpdate == null) {
/* 1626 */               result = deser.deserialize(p, ctxt);
/*      */             } else {
/* 1628 */               deser.deserialize(p, ctxt, this._valueToUpdate);
/* 1629 */               result = this._valueToUpdate;
/*      */             }
/*      */           }
/*      */         } }
/* 1633 */       return (JsonDeserializer<Object>)result;
/*      */     }
/*      */     catch (Throwable localThrowable1)
/*      */     {
/* 1607 */       localThrowable2 = localThrowable1;throw localThrowable1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1634 */       if (p != null) if (localThrowable2 != null) try { p.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else p.close();
/*      */     }
/*      */   }
/*      */   
/* 1638 */   protected JsonNode _bindAndCloseAsTree(JsonParser p0) throws IOException { JsonParser p = p0;Throwable localThrowable2 = null;
/* 1639 */     try { return _bindAsTree(p);
/*      */     }
/*      */     catch (Throwable localThrowable3)
/*      */     {
/* 1638 */       localThrowable2 = localThrowable3;throw localThrowable3;
/*      */     } finally {
/* 1640 */       if (p != null) if (localThrowable2 != null) try { p.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else p.close();
/*      */     }
/*      */   }
/*      */   
/*      */   protected JsonNode _bindAsTree(JsonParser p) throws IOException
/*      */   {
/* 1646 */     DeserializationContext ctxt = createDeserializationContext(p);
/* 1647 */     JsonToken t = _initForReading(ctxt, p);
/* 1648 */     JsonNode result; JsonNode result; if ((t == JsonToken.VALUE_NULL) || (t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 1649 */       result = com.fasterxml.jackson.databind.node.NullNode.instance;
/*      */     } else {
/* 1651 */       JsonDeserializer<Object> deser = _findTreeDeserializer(ctxt);
/* 1652 */       JsonNode result; if (this._unwrapRoot) {
/* 1653 */         result = (JsonNode)_unwrapAndDeserialize(p, ctxt, JSON_NODE_TYPE, deser);
/*      */       } else {
/* 1655 */         result = (JsonNode)deser.deserialize(p, ctxt);
/*      */       }
/*      */     }
/*      */     
/* 1659 */     p.clearCurrentToken();
/* 1660 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected <T> MappingIterator<T> _bindAndReadValues(JsonParser p)
/*      */     throws IOException
/*      */   {
/* 1668 */     DeserializationContext ctxt = createDeserializationContext(p);
/* 1669 */     _initForMultiRead(ctxt, p);
/* 1670 */     p.nextToken();
/* 1671 */     return _newIterator(p, ctxt, _findRootDeserializer(ctxt), true);
/*      */   }
/*      */   
/*      */   protected Object _unwrapAndDeserialize(JsonParser p, DeserializationContext ctxt, JavaType rootType, JsonDeserializer<Object> deser)
/*      */     throws IOException
/*      */   {
/* 1677 */     PropertyName expRootName = this._config.findRootName(rootType);
/*      */     
/* 1679 */     String expSimpleName = expRootName.getSimpleName();
/*      */     
/* 1681 */     if (p.getCurrentToken() != JsonToken.START_OBJECT) {
/* 1682 */       ctxt.reportWrongTokenException(p, JsonToken.START_OBJECT, "Current token not START_OBJECT (needed to unwrap root name '%s'), but %s", new Object[] { expSimpleName, p.getCurrentToken() });
/*      */     }
/*      */     
/*      */ 
/* 1686 */     if (p.nextToken() != JsonToken.FIELD_NAME) {
/* 1687 */       ctxt.reportWrongTokenException(p, JsonToken.FIELD_NAME, "Current token not FIELD_NAME (to contain expected root name '%s'), but %s", new Object[] { expSimpleName, p.getCurrentToken() });
/*      */     }
/*      */     
/*      */ 
/* 1691 */     String actualName = p.getCurrentName();
/* 1692 */     if (!expSimpleName.equals(actualName)) {
/* 1693 */       ctxt.reportMappingException("Root name '%s' does not match expected ('%s') for type %s", new Object[] { actualName, expSimpleName, rootType });
/*      */     }
/*      */     
/*      */ 
/* 1697 */     p.nextToken();
/*      */     Object result;
/* 1699 */     Object result; if (this._valueToUpdate == null) {
/* 1700 */       result = deser.deserialize(p, ctxt);
/*      */     } else {
/* 1702 */       deser.deserialize(p, ctxt, this._valueToUpdate);
/* 1703 */       result = this._valueToUpdate;
/*      */     }
/*      */     
/* 1706 */     if (p.nextToken() != JsonToken.END_OBJECT) {
/* 1707 */       ctxt.reportWrongTokenException(p, JsonToken.END_OBJECT, "Current token not END_OBJECT (to match wrapper object with root name '%s'), but %s", new Object[] { expSimpleName, p.getCurrentToken() });
/*      */     }
/*      */     
/*      */ 
/* 1711 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object _detectBindAndClose(byte[] src, int offset, int length)
/*      */     throws IOException
/*      */   {
/* 1723 */     DataFormatReaders.Match match = this._dataFormatReaders.findFormat(src, offset, length);
/* 1724 */     if (!match.hasMatch()) {
/* 1725 */       _reportUnkownFormat(this._dataFormatReaders, match);
/*      */     }
/* 1727 */     JsonParser p = match.createParserWithMatch();
/* 1728 */     return match.getReader()._bindAndClose(p);
/*      */   }
/*      */   
/*      */ 
/*      */   protected Object _detectBindAndClose(DataFormatReaders.Match match, boolean forceClosing)
/*      */     throws IOException
/*      */   {
/* 1735 */     if (!match.hasMatch()) {
/* 1736 */       _reportUnkownFormat(this._dataFormatReaders, match);
/*      */     }
/* 1738 */     JsonParser p = match.createParserWithMatch();
/*      */     
/*      */ 
/* 1741 */     if (forceClosing) {
/* 1742 */       p.enable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
/*      */     }
/*      */     
/* 1745 */     return match.getReader()._bindAndClose(p);
/*      */   }
/*      */   
/*      */ 
/*      */   protected <T> MappingIterator<T> _detectBindAndReadValues(DataFormatReaders.Match match, boolean forceClosing)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 1752 */     if (!match.hasMatch()) {
/* 1753 */       _reportUnkownFormat(this._dataFormatReaders, match);
/*      */     }
/* 1755 */     JsonParser p = match.createParserWithMatch();
/*      */     
/*      */ 
/* 1758 */     if (forceClosing) {
/* 1759 */       p.enable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
/*      */     }
/*      */     
/* 1762 */     return match.getReader()._bindAndReadValues(p);
/*      */   }
/*      */   
/*      */   protected JsonNode _detectBindAndCloseAsTree(InputStream in)
/*      */     throws IOException
/*      */   {
/* 1768 */     DataFormatReaders.Match match = this._dataFormatReaders.findFormat(in);
/* 1769 */     if (!match.hasMatch()) {
/* 1770 */       _reportUnkownFormat(this._dataFormatReaders, match);
/*      */     }
/* 1772 */     JsonParser p = match.createParserWithMatch();
/* 1773 */     p.enable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
/* 1774 */     return match.getReader()._bindAndCloseAsTree(p);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _reportUnkownFormat(DataFormatReaders detector, DataFormatReaders.Match match)
/*      */     throws JsonProcessingException
/*      */   {
/* 1784 */     throw new JsonParseException(null, "Can not detect format from input, does not look like any of detectable formats " + detector.toString());
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
/*      */   protected void _verifySchemaType(FormatSchema schema)
/*      */   {
/* 1799 */     if ((schema != null) && 
/* 1800 */       (!this._parserFactory.canUseSchema(schema))) {
/* 1801 */       throw new IllegalArgumentException("Can not use FormatSchema of type " + schema.getClass().getName() + " for format " + this._parserFactory.getFormatName());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DefaultDeserializationContext createDeserializationContext(JsonParser p)
/*      */   {
/* 1813 */     return this._context.createInstance(this._config, p, this._injectableValues);
/*      */   }
/*      */   
/*      */   protected void _reportUndetectableSource(Object src)
/*      */     throws JsonProcessingException
/*      */   {
/* 1819 */     throw new JsonParseException(null, "Can not use source of type " + src.getClass().getName() + " with format auto-detection: must be byte- not char-based");
/*      */   }
/*      */   
/*      */   protected InputStream _inputStream(URL src) throws IOException
/*      */   {
/* 1824 */     return src.openStream();
/*      */   }
/*      */   
/*      */   protected InputStream _inputStream(File f) throws IOException {
/* 1828 */     return new java.io.FileInputStream(f);
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
/*      */   protected JsonDeserializer<Object> _findRootDeserializer(DeserializationContext ctxt)
/*      */     throws JsonMappingException
/*      */   {
/* 1843 */     if (this._rootDeserializer != null) {
/* 1844 */       return this._rootDeserializer;
/*      */     }
/*      */     
/*      */ 
/* 1848 */     JavaType t = this._valueType;
/* 1849 */     if (t == null) {
/* 1850 */       ctxt.reportMappingException("No value type configured for ObjectReader", new Object[0]);
/*      */     }
/*      */     
/*      */ 
/* 1854 */     JsonDeserializer<Object> deser = (JsonDeserializer)this._rootDeserializers.get(t);
/* 1855 */     if (deser != null) {
/* 1856 */       return deser;
/*      */     }
/*      */     
/* 1859 */     deser = ctxt.findRootValueDeserializer(t);
/* 1860 */     if (deser == null) {
/* 1861 */       ctxt.reportMappingException("Can not find a deserializer for type %s", new Object[] { t });
/*      */     }
/* 1863 */     this._rootDeserializers.put(t, deser);
/* 1864 */     return deser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonDeserializer<Object> _findTreeDeserializer(DeserializationContext ctxt)
/*      */     throws JsonMappingException
/*      */   {
/* 1873 */     JsonDeserializer<Object> deser = (JsonDeserializer)this._rootDeserializers.get(JSON_NODE_TYPE);
/* 1874 */     if (deser == null)
/*      */     {
/* 1876 */       deser = ctxt.findRootValueDeserializer(JSON_NODE_TYPE);
/* 1877 */       if (deser == null) {
/* 1878 */         ctxt.reportMappingException("Can not find a deserializer for type %s", new Object[] { JSON_NODE_TYPE });
/*      */       }
/*      */       
/* 1881 */       this._rootDeserializers.put(JSON_NODE_TYPE, deser);
/*      */     }
/* 1883 */     return deser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected JsonDeserializer<Object> _prefetchRootDeserializer(JavaType valueType)
/*      */   {
/* 1893 */     if ((valueType == null) || (!this._config.isEnabled(DeserializationFeature.EAGER_DESERIALIZER_FETCH))) {
/* 1894 */       return null;
/*      */     }
/*      */     
/* 1897 */     JsonDeserializer<Object> deser = (JsonDeserializer)this._rootDeserializers.get(valueType);
/* 1898 */     if (deser == null) {
/*      */       try
/*      */       {
/* 1901 */         DeserializationContext ctxt = createDeserializationContext(null);
/* 1902 */         deser = ctxt.findRootValueDeserializer(valueType);
/* 1903 */         if (deser != null) {
/* 1904 */           this._rootDeserializers.put(valueType, deser);
/*      */         }
/* 1906 */         return deser;
/*      */       }
/*      */       catch (JsonProcessingException e) {}
/*      */     }
/*      */     
/* 1911 */     return deser;
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ObjectReader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */