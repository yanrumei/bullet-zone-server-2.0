/*      */ package com.fasterxml.jackson.databind;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
/*      */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*      */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*      */ import com.fasterxml.jackson.annotation.PropertyAccessor;
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.FormatFeature;
/*      */ import com.fasterxml.jackson.core.JsonFactory;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*      */ import com.fasterxml.jackson.core.PrettyPrinter;
/*      */ import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
/*      */ import com.fasterxml.jackson.core.util.Instantiatable;
/*      */ import com.fasterxml.jackson.databind.cfg.BaseSettings;
/*      */ import com.fasterxml.jackson.databind.cfg.ConfigOverride;
/*      */ import com.fasterxml.jackson.databind.cfg.ConfigOverrides;
/*      */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*      */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*      */ import com.fasterxml.jackson.databind.cfg.MapperConfigBase;
/*      */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*      */ import com.fasterxml.jackson.databind.introspect.SimpleMixInResolver;
/*      */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*      */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*      */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.util.RootNameLookup;
/*      */ import java.io.Serializable;
/*      */ import java.text.DateFormat;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public final class SerializationConfig
/*      */   extends MapperConfigBase<SerializationFeature, SerializationConfig>
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   42 */   protected static final PrettyPrinter DEFAULT_PRETTY_PRINTER = new DefaultPrettyPrinter();
/*      */   
/*      */ 
/*      */ 
/*   46 */   protected static final JsonInclude.Value DEFAULT_INCLUSION = JsonInclude.Value.empty();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final FilterProvider _filterProvider;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final PrettyPrinter _defaultPrettyPrinter;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int _serFeatures;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int _generatorFeatures;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int _generatorFeaturesToChange;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int _formatWriteFeatures;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final int _formatWriteFeaturesToChange;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JsonInclude.Value _serializationInclusion;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SerializationConfig(BaseSettings base, SubtypeResolver str, SimpleMixInResolver mixins, RootNameLookup rootNames, ConfigOverrides configOverrides)
/*      */   {
/*  138 */     super(base, str, mixins, rootNames, configOverrides);
/*  139 */     this._serFeatures = collectFeatureDefaults(SerializationFeature.class);
/*  140 */     this._filterProvider = null;
/*  141 */     this._defaultPrettyPrinter = DEFAULT_PRETTY_PRINTER;
/*  142 */     this._generatorFeatures = 0;
/*  143 */     this._generatorFeaturesToChange = 0;
/*  144 */     this._formatWriteFeatures = 0;
/*  145 */     this._formatWriteFeaturesToChange = 0;
/*  146 */     this._serializationInclusion = DEFAULT_INCLUSION;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public SerializationConfig(BaseSettings base, SubtypeResolver str, SimpleMixInResolver mixins, RootNameLookup rootNames)
/*      */   {
/*  157 */     this(base, str, mixins, rootNames, null);
/*      */   }
/*      */   
/*      */   private SerializationConfig(SerializationConfig src, SubtypeResolver str)
/*      */   {
/*  162 */     super(src, str);
/*  163 */     this._serFeatures = src._serFeatures;
/*  164 */     this._serializationInclusion = src._serializationInclusion;
/*  165 */     this._filterProvider = src._filterProvider;
/*  166 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/*  167 */     this._generatorFeatures = src._generatorFeatures;
/*  168 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*  169 */     this._formatWriteFeatures = src._formatWriteFeatures;
/*  170 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private SerializationConfig(SerializationConfig src, int mapperFeatures, int serFeatures, int generatorFeatures, int generatorFeatureMask, int formatFeatures, int formatFeaturesMask)
/*      */   {
/*  178 */     super(src, mapperFeatures);
/*  179 */     this._serFeatures = serFeatures;
/*  180 */     this._serializationInclusion = src._serializationInclusion;
/*  181 */     this._filterProvider = src._filterProvider;
/*  182 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/*  183 */     this._generatorFeatures = generatorFeatures;
/*  184 */     this._generatorFeaturesToChange = generatorFeatureMask;
/*  185 */     this._formatWriteFeatures = formatFeatures;
/*  186 */     this._formatWriteFeaturesToChange = formatFeaturesMask;
/*      */   }
/*      */   
/*      */   private SerializationConfig(SerializationConfig src, BaseSettings base)
/*      */   {
/*  191 */     super(src, base);
/*  192 */     this._serFeatures = src._serFeatures;
/*  193 */     this._serializationInclusion = src._serializationInclusion;
/*  194 */     this._filterProvider = src._filterProvider;
/*  195 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/*  196 */     this._generatorFeatures = src._generatorFeatures;
/*  197 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*  198 */     this._formatWriteFeatures = src._formatWriteFeatures;
/*  199 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*      */   }
/*      */   
/*      */   private SerializationConfig(SerializationConfig src, FilterProvider filters)
/*      */   {
/*  204 */     super(src);
/*  205 */     this._serFeatures = src._serFeatures;
/*  206 */     this._serializationInclusion = src._serializationInclusion;
/*  207 */     this._filterProvider = filters;
/*  208 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/*  209 */     this._generatorFeatures = src._generatorFeatures;
/*  210 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*  211 */     this._formatWriteFeatures = src._formatWriteFeatures;
/*  212 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*      */   }
/*      */   
/*      */   private SerializationConfig(SerializationConfig src, Class<?> view)
/*      */   {
/*  217 */     super(src, view);
/*  218 */     this._serFeatures = src._serFeatures;
/*  219 */     this._serializationInclusion = src._serializationInclusion;
/*  220 */     this._filterProvider = src._filterProvider;
/*  221 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/*  222 */     this._generatorFeatures = src._generatorFeatures;
/*  223 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*  224 */     this._formatWriteFeatures = src._formatWriteFeatures;
/*  225 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*      */   }
/*      */   
/*      */   private SerializationConfig(SerializationConfig src, JsonInclude.Value incl)
/*      */   {
/*  230 */     super(src);
/*  231 */     this._serFeatures = src._serFeatures;
/*  232 */     this._serializationInclusion = incl;
/*  233 */     this._filterProvider = src._filterProvider;
/*  234 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/*  235 */     this._generatorFeatures = src._generatorFeatures;
/*  236 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*  237 */     this._formatWriteFeatures = src._formatWriteFeatures;
/*  238 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*      */   }
/*      */   
/*      */   private SerializationConfig(SerializationConfig src, PropertyName rootName)
/*      */   {
/*  243 */     super(src, rootName);
/*  244 */     this._serFeatures = src._serFeatures;
/*  245 */     this._serializationInclusion = src._serializationInclusion;
/*  246 */     this._filterProvider = src._filterProvider;
/*  247 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/*  248 */     this._generatorFeatures = src._generatorFeatures;
/*  249 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*  250 */     this._formatWriteFeatures = src._formatWriteFeatures;
/*  251 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SerializationConfig(SerializationConfig src, ContextAttributes attrs)
/*      */   {
/*  259 */     super(src, attrs);
/*  260 */     this._serFeatures = src._serFeatures;
/*  261 */     this._serializationInclusion = src._serializationInclusion;
/*  262 */     this._filterProvider = src._filterProvider;
/*  263 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/*  264 */     this._generatorFeatures = src._generatorFeatures;
/*  265 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*  266 */     this._formatWriteFeatures = src._formatWriteFeatures;
/*  267 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SerializationConfig(SerializationConfig src, SimpleMixInResolver mixins)
/*      */   {
/*  275 */     super(src, mixins);
/*  276 */     this._serFeatures = src._serFeatures;
/*  277 */     this._serializationInclusion = src._serializationInclusion;
/*  278 */     this._filterProvider = src._filterProvider;
/*  279 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/*  280 */     this._generatorFeatures = src._generatorFeatures;
/*  281 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*  282 */     this._formatWriteFeatures = src._formatWriteFeatures;
/*  283 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SerializationConfig(SerializationConfig src, PrettyPrinter defaultPP)
/*      */   {
/*  291 */     super(src);
/*  292 */     this._serFeatures = src._serFeatures;
/*  293 */     this._serializationInclusion = src._serializationInclusion;
/*  294 */     this._filterProvider = src._filterProvider;
/*  295 */     this._defaultPrettyPrinter = defaultPP;
/*  296 */     this._generatorFeatures = src._generatorFeatures;
/*  297 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*  298 */     this._formatWriteFeatures = src._formatWriteFeatures;
/*  299 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SerializationConfig(SerializationConfig src, SimpleMixInResolver mixins, RootNameLookup rootNames, ConfigOverrides configOverrides)
/*      */   {
/*  310 */     super(src, mixins, rootNames, configOverrides);
/*  311 */     this._serFeatures = src._serFeatures;
/*  312 */     this._serializationInclusion = src._serializationInclusion;
/*  313 */     this._filterProvider = src._filterProvider;
/*  314 */     this._defaultPrettyPrinter = src._defaultPrettyPrinter;
/*  315 */     this._generatorFeatures = src._generatorFeatures;
/*  316 */     this._generatorFeaturesToChange = src._generatorFeaturesToChange;
/*  317 */     this._formatWriteFeatures = src._formatWriteFeatures;
/*  318 */     this._formatWriteFeaturesToChange = src._formatWriteFeaturesToChange;
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
/*      */   public SerializationConfig with(MapperFeature... features)
/*      */   {
/*  334 */     int newMapperFlags = this._mapperFeatures;
/*  335 */     for (MapperFeature f : features) {
/*  336 */       newMapperFlags |= f.getMask();
/*      */     }
/*  338 */     return newMapperFlags == this._mapperFeatures ? this : new SerializationConfig(this, newMapperFlags, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*      */   public SerializationConfig without(MapperFeature... features)
/*      */   {
/*  351 */     int newMapperFlags = this._mapperFeatures;
/*  352 */     for (MapperFeature f : features) {
/*  353 */       newMapperFlags &= (f.getMask() ^ 0xFFFFFFFF);
/*      */     }
/*  355 */     return newMapperFlags == this._mapperFeatures ? this : new SerializationConfig(this, newMapperFlags, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
/*      */   }
/*      */   
/*      */ 
/*      */   public SerializationConfig with(MapperFeature feature, boolean state)
/*      */   {
/*      */     int newMapperFlags;
/*      */     
/*      */     int newMapperFlags;
/*      */     
/*  365 */     if (state) {
/*  366 */       newMapperFlags = this._mapperFeatures | feature.getMask();
/*      */     } else {
/*  368 */       newMapperFlags = this._mapperFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/*      */     }
/*  370 */     return newMapperFlags == this._mapperFeatures ? this : new SerializationConfig(this, newMapperFlags, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public SerializationConfig with(AnnotationIntrospector ai)
/*      */   {
/*  378 */     return _withBase(this._base.withAnnotationIntrospector(ai));
/*      */   }
/*      */   
/*      */   public SerializationConfig withAppendedAnnotationIntrospector(AnnotationIntrospector ai)
/*      */   {
/*  383 */     return _withBase(this._base.withAppendedAnnotationIntrospector(ai));
/*      */   }
/*      */   
/*      */   public SerializationConfig withInsertedAnnotationIntrospector(AnnotationIntrospector ai)
/*      */   {
/*  388 */     return _withBase(this._base.withInsertedAnnotationIntrospector(ai));
/*      */   }
/*      */   
/*      */   public SerializationConfig with(ClassIntrospector ci)
/*      */   {
/*  393 */     return _withBase(this._base.withClassIntrospector(ci));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SerializationConfig with(DateFormat df)
/*      */   {
/*  403 */     SerializationConfig cfg = new SerializationConfig(this, this._base.withDateFormat(df));
/*      */     
/*  405 */     if (df == null) {
/*  406 */       cfg = cfg.with(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
/*      */     } else {
/*  408 */       cfg = cfg.without(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
/*      */     }
/*  410 */     return cfg;
/*      */   }
/*      */   
/*      */   public SerializationConfig with(HandlerInstantiator hi)
/*      */   {
/*  415 */     return _withBase(this._base.withHandlerInstantiator(hi));
/*      */   }
/*      */   
/*      */   public SerializationConfig with(PropertyNamingStrategy pns)
/*      */   {
/*  420 */     return _withBase(this._base.withPropertyNamingStrategy(pns));
/*      */   }
/*      */   
/*      */   public SerializationConfig withRootName(PropertyName rootName)
/*      */   {
/*  425 */     if (rootName == null) {
/*  426 */       if (this._rootName == null) {
/*  427 */         return this;
/*      */       }
/*  429 */     } else if (rootName.equals(this._rootName)) {
/*  430 */       return this;
/*      */     }
/*  432 */     return new SerializationConfig(this, rootName);
/*      */   }
/*      */   
/*      */   public SerializationConfig with(SubtypeResolver str)
/*      */   {
/*  437 */     return str == this._subtypeResolver ? this : new SerializationConfig(this, str);
/*      */   }
/*      */   
/*      */   public SerializationConfig with(TypeFactory tf)
/*      */   {
/*  442 */     return _withBase(this._base.withTypeFactory(tf));
/*      */   }
/*      */   
/*      */   public SerializationConfig with(TypeResolverBuilder<?> trb)
/*      */   {
/*  447 */     return _withBase(this._base.withTypeResolverBuilder(trb));
/*      */   }
/*      */   
/*      */   public SerializationConfig withView(Class<?> view)
/*      */   {
/*  452 */     return this._view == view ? this : new SerializationConfig(this, view);
/*      */   }
/*      */   
/*      */   public SerializationConfig with(VisibilityChecker<?> vc)
/*      */   {
/*  457 */     return _withBase(this._base.withVisibilityChecker(vc));
/*      */   }
/*      */   
/*      */   public SerializationConfig withVisibility(PropertyAccessor forMethod, JsonAutoDetect.Visibility visibility)
/*      */   {
/*  462 */     return _withBase(this._base.withVisibility(forMethod, visibility));
/*      */   }
/*      */   
/*      */   public SerializationConfig with(Locale l)
/*      */   {
/*  467 */     return _withBase(this._base.with(l));
/*      */   }
/*      */   
/*      */   public SerializationConfig with(TimeZone tz)
/*      */   {
/*  472 */     return _withBase(this._base.with(tz));
/*      */   }
/*      */   
/*      */   public SerializationConfig with(Base64Variant base64)
/*      */   {
/*  477 */     return _withBase(this._base.with(base64));
/*      */   }
/*      */   
/*      */   public SerializationConfig with(ContextAttributes attrs)
/*      */   {
/*  482 */     return attrs == this._attributes ? this : new SerializationConfig(this, attrs);
/*      */   }
/*      */   
/*      */   private final SerializationConfig _withBase(BaseSettings newBase) {
/*  486 */     return this._base == newBase ? this : new SerializationConfig(this, newBase);
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
/*      */   public SerializationConfig with(SerializationFeature feature)
/*      */   {
/*  501 */     int newSerFeatures = this._serFeatures | feature.getMask();
/*  502 */     return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SerializationConfig with(SerializationFeature first, SerializationFeature... features)
/*      */   {
/*  514 */     int newSerFeatures = this._serFeatures | first.getMask();
/*  515 */     for (SerializationFeature f : features) {
/*  516 */       newSerFeatures |= f.getMask();
/*      */     }
/*  518 */     return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SerializationConfig withFeatures(SerializationFeature... features)
/*      */   {
/*  530 */     int newSerFeatures = this._serFeatures;
/*  531 */     for (SerializationFeature f : features) {
/*  532 */       newSerFeatures |= f.getMask();
/*      */     }
/*  534 */     return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SerializationConfig without(SerializationFeature feature)
/*      */   {
/*  546 */     int newSerFeatures = this._serFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/*  547 */     return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SerializationConfig without(SerializationFeature first, SerializationFeature... features)
/*      */   {
/*  559 */     int newSerFeatures = this._serFeatures & (first.getMask() ^ 0xFFFFFFFF);
/*  560 */     for (SerializationFeature f : features) {
/*  561 */       newSerFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/*      */     }
/*  563 */     return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SerializationConfig withoutFeatures(SerializationFeature... features)
/*      */   {
/*  575 */     int newSerFeatures = this._serFeatures;
/*  576 */     for (SerializationFeature f : features) {
/*  577 */       newSerFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/*      */     }
/*  579 */     return newSerFeatures == this._serFeatures ? this : new SerializationConfig(this, this._mapperFeatures, newSerFeatures, this._generatorFeatures, this._generatorFeaturesToChange, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*      */   public SerializationConfig with(JsonGenerator.Feature feature)
/*      */   {
/*  598 */     int newSet = this._generatorFeatures | feature.getMask();
/*  599 */     int newMask = this._generatorFeaturesToChange | feature.getMask();
/*  600 */     return (this._generatorFeatures == newSet) && (this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*      */   public SerializationConfig withFeatures(JsonGenerator.Feature... features)
/*      */   {
/*  614 */     int newSet = this._generatorFeatures;
/*  615 */     int newMask = this._generatorFeaturesToChange;
/*  616 */     for (JsonGenerator.Feature f : features) {
/*  617 */       int mask = f.getMask();
/*  618 */       newSet |= mask;
/*  619 */       newMask |= mask;
/*      */     }
/*  621 */     return (this._generatorFeatures == newSet) && (this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*      */   public SerializationConfig without(JsonGenerator.Feature feature)
/*      */   {
/*  635 */     int newSet = this._generatorFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/*  636 */     int newMask = this._generatorFeaturesToChange | feature.getMask();
/*  637 */     return (this._generatorFeatures == newSet) && (this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*      */   public SerializationConfig withoutFeatures(JsonGenerator.Feature... features)
/*      */   {
/*  651 */     int newSet = this._generatorFeatures;
/*  652 */     int newMask = this._generatorFeaturesToChange;
/*  653 */     for (JsonGenerator.Feature f : features) {
/*  654 */       int mask = f.getMask();
/*  655 */       newSet &= (mask ^ 0xFFFFFFFF);
/*  656 */       newMask |= mask;
/*      */     }
/*  658 */     return (this._generatorFeatures == newSet) && (this._generatorFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, newSet, newMask, this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*      */   public SerializationConfig with(FormatFeature feature)
/*      */   {
/*  677 */     int newSet = this._formatWriteFeatures | feature.getMask();
/*  678 */     int newMask = this._formatWriteFeaturesToChange | feature.getMask();
/*  679 */     return (this._formatWriteFeatures == newSet) && (this._formatWriteFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, newSet, newMask);
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
/*      */   public SerializationConfig withFeatures(FormatFeature... features)
/*      */   {
/*  693 */     int newSet = this._formatWriteFeatures;
/*  694 */     int newMask = this._formatWriteFeaturesToChange;
/*  695 */     for (FormatFeature f : features) {
/*  696 */       int mask = f.getMask();
/*  697 */       newSet |= mask;
/*  698 */       newMask |= mask;
/*      */     }
/*  700 */     return (this._formatWriteFeatures == newSet) && (this._formatWriteFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, newSet, newMask);
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
/*      */   public SerializationConfig without(FormatFeature feature)
/*      */   {
/*  714 */     int newSet = this._formatWriteFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/*  715 */     int newMask = this._formatWriteFeaturesToChange | feature.getMask();
/*  716 */     return (this._formatWriteFeatures == newSet) && (this._formatWriteFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, newSet, newMask);
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
/*      */   public SerializationConfig withoutFeatures(FormatFeature... features)
/*      */   {
/*  730 */     int newSet = this._formatWriteFeatures;
/*  731 */     int newMask = this._formatWriteFeaturesToChange;
/*  732 */     for (FormatFeature f : features) {
/*  733 */       int mask = f.getMask();
/*  734 */       newSet &= (mask ^ 0xFFFFFFFF);
/*  735 */       newMask |= mask;
/*      */     }
/*  737 */     return (this._formatWriteFeatures == newSet) && (this._formatWriteFeaturesToChange == newMask) ? this : new SerializationConfig(this, this._mapperFeatures, this._serFeatures, this._generatorFeatures, this._generatorFeaturesToChange, newSet, newMask);
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
/*      */   public SerializationConfig withFilters(FilterProvider filterProvider)
/*      */   {
/*  750 */     return filterProvider == this._filterProvider ? this : new SerializationConfig(this, filterProvider);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public SerializationConfig withSerializationInclusion(JsonInclude.Include incl)
/*      */   {
/*  758 */     return withPropertyInclusion(DEFAULT_INCLUSION.withValueInclusion(incl));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public SerializationConfig withPropertyInclusion(JsonInclude.Value incl)
/*      */   {
/*  765 */     if (this._serializationInclusion.equals(incl)) {
/*  766 */       return this;
/*      */     }
/*  768 */     return new SerializationConfig(this, incl);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public SerializationConfig withDefaultPrettyPrinter(PrettyPrinter pp)
/*      */   {
/*  775 */     return this._defaultPrettyPrinter == pp ? this : new SerializationConfig(this, pp);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PrettyPrinter constructDefaultPrettyPrinter()
/*      */   {
/*  785 */     PrettyPrinter pp = this._defaultPrettyPrinter;
/*  786 */     if ((pp instanceof Instantiatable)) {
/*  787 */       pp = (PrettyPrinter)((Instantiatable)pp).createInstance();
/*      */     }
/*  789 */     return pp;
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
/*      */   public void initialize(JsonGenerator g)
/*      */   {
/*  807 */     if (SerializationFeature.INDENT_OUTPUT.enabledIn(this._serFeatures))
/*      */     {
/*  809 */       if (g.getPrettyPrinter() == null) {
/*  810 */         PrettyPrinter pp = constructDefaultPrettyPrinter();
/*  811 */         if (pp != null) {
/*  812 */           g.setPrettyPrinter(pp);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  817 */     boolean useBigDec = SerializationFeature.WRITE_BIGDECIMAL_AS_PLAIN.enabledIn(this._serFeatures);
/*      */     
/*  819 */     int mask = this._generatorFeaturesToChange;
/*  820 */     if ((mask != 0) || (useBigDec)) {
/*  821 */       int newFlags = this._generatorFeatures;
/*      */       
/*  823 */       if (useBigDec) {
/*  824 */         int f = JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN.getMask();
/*  825 */         newFlags |= f;
/*  826 */         mask |= f;
/*      */       }
/*  828 */       g.overrideStdFeatures(newFlags, mask);
/*      */     }
/*  830 */     if (this._formatWriteFeaturesToChange != 0) {
/*  831 */       g.overrideFormatFeatures(this._formatWriteFeatures, this._formatWriteFeaturesToChange);
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
/*      */   public AnnotationIntrospector getAnnotationIntrospector()
/*      */   {
/*  844 */     if (isEnabled(MapperFeature.USE_ANNOTATIONS)) {
/*  845 */       return super.getAnnotationIntrospector();
/*      */     }
/*  847 */     return AnnotationIntrospector.nopInstance();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BeanDescription introspectClassAnnotations(JavaType type)
/*      */   {
/*  856 */     return getClassIntrospector().forClassAnnotations(this, type, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public BeanDescription introspectDirectClassAnnotations(JavaType type)
/*      */   {
/*  866 */     return getClassIntrospector().forDirectClassAnnotations(this, type, this);
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
/*      */   @Deprecated
/*      */   public JsonInclude.Include getSerializationInclusion()
/*      */   {
/*  881 */     JsonInclude.Include incl = this._serializationInclusion.getValueInclusion();
/*  882 */     return incl == JsonInclude.Include.USE_DEFAULTS ? JsonInclude.Include.ALWAYS : incl;
/*      */   }
/*      */   
/*      */   public JsonInclude.Value getDefaultPropertyInclusion()
/*      */   {
/*  887 */     return this._serializationInclusion;
/*      */   }
/*      */   
/*      */   public JsonInclude.Value getDefaultPropertyInclusion(Class<?> baseType)
/*      */   {
/*  892 */     ConfigOverride overrides = findConfigOverride(baseType);
/*  893 */     if (overrides != null) {
/*  894 */       JsonInclude.Value v = overrides.getInclude();
/*  895 */       if (v != null) {
/*  896 */         return v;
/*      */       }
/*      */     }
/*  899 */     return this._serializationInclusion;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JsonInclude.Value getDefaultPropertyInclusion(Class<?> baseType, JsonInclude.Value defaultIncl)
/*      */   {
/*  906 */     ConfigOverride overrides = findConfigOverride(baseType);
/*  907 */     if (overrides != null) {
/*  908 */       JsonInclude.Value v = overrides.getInclude();
/*  909 */       if (v != null) {
/*  910 */         return v;
/*      */       }
/*      */     }
/*  913 */     return defaultIncl;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean useRootWrapping()
/*      */   {
/*  925 */     if (this._rootName != null) {
/*  926 */       return !this._rootName.isEmpty();
/*      */     }
/*  928 */     return isEnabled(SerializationFeature.WRAP_ROOT_VALUE);
/*      */   }
/*      */   
/*      */   public final boolean isEnabled(SerializationFeature f) {
/*  932 */     return (this._serFeatures & f.getMask()) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean isEnabled(JsonGenerator.Feature f, JsonFactory factory)
/*      */   {
/*  943 */     int mask = f.getMask();
/*  944 */     if ((this._generatorFeaturesToChange & mask) != 0) {
/*  945 */       return (this._generatorFeatures & f.getMask()) != 0;
/*      */     }
/*  947 */     return factory.isEnabled(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean hasSerializationFeatures(int featureMask)
/*      */   {
/*  957 */     return (this._serFeatures & featureMask) == featureMask;
/*      */   }
/*      */   
/*      */   public final int getSerializationFeatures() {
/*  961 */     return this._serFeatures;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public FilterProvider getFilterProvider()
/*      */   {
/*  971 */     return this._filterProvider;
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
/*      */   public PrettyPrinter getDefaultPrettyPrinter()
/*      */   {
/*  985 */     return this._defaultPrettyPrinter;
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
/*      */   public <T extends BeanDescription> T introspect(JavaType type)
/*      */   {
/* 1000 */     return getClassIntrospector().forSerialization(this, type, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1011 */     return "[SerializationConfig: flags=0x" + Integer.toHexString(this._serFeatures) + "]";
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\SerializationConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */