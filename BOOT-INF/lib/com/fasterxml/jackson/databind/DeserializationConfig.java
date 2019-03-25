/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*     */ import com.fasterxml.jackson.annotation.PropertyAccessor;
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.FormatFeature;
/*     */ import com.fasterxml.jackson.core.JsonFactory;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*     */ import com.fasterxml.jackson.databind.cfg.BaseSettings;
/*     */ import com.fasterxml.jackson.databind.cfg.ConfigOverride;
/*     */ import com.fasterxml.jackson.databind.cfg.ConfigOverrides;
/*     */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfigBase;
/*     */ import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.SimpleMixInResolver;
/*     */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.LinkedNode;
/*     */ import com.fasterxml.jackson.databind.util.RootNameLookup;
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Collection;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class DeserializationConfig
/*     */   extends MapperConfigBase<DeserializationFeature, DeserializationConfig>
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final LinkedNode<DeserializationProblemHandler> _problemHandlers;
/*     */   protected final JsonNodeFactory _nodeFactory;
/*     */   protected final int _deserFeatures;
/*     */   protected final int _parserFeatures;
/*     */   protected final int _parserFeaturesToChange;
/*     */   protected final int _formatReadFeatures;
/*     */   protected final int _formatReadFeaturesToChange;
/*     */   
/*     */   public DeserializationConfig(BaseSettings base, SubtypeResolver str, SimpleMixInResolver mixins, RootNameLookup rootNames, ConfigOverrides configOverrides)
/*     */   {
/* 107 */     super(base, str, mixins, rootNames, configOverrides);
/* 108 */     this._deserFeatures = collectFeatureDefaults(DeserializationFeature.class);
/* 109 */     this._nodeFactory = JsonNodeFactory.instance;
/* 110 */     this._problemHandlers = null;
/* 111 */     this._parserFeatures = 0;
/* 112 */     this._parserFeaturesToChange = 0;
/* 113 */     this._formatReadFeatures = 0;
/* 114 */     this._formatReadFeaturesToChange = 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public DeserializationConfig(BaseSettings base, SubtypeResolver str, SimpleMixInResolver mixins, RootNameLookup rootNames)
/*     */   {
/* 123 */     this(base, str, mixins, rootNames, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private DeserializationConfig(DeserializationConfig src, int mapperFeatures, int deserFeatures, int parserFeatures, int parserFeatureMask, int formatFeatures, int formatFeatureMask)
/*     */   {
/* 131 */     super(src, mapperFeatures);
/* 132 */     this._deserFeatures = deserFeatures;
/* 133 */     this._nodeFactory = src._nodeFactory;
/* 134 */     this._problemHandlers = src._problemHandlers;
/* 135 */     this._parserFeatures = parserFeatures;
/* 136 */     this._parserFeaturesToChange = parserFeatureMask;
/* 137 */     this._formatReadFeatures = formatFeatures;
/* 138 */     this._formatReadFeaturesToChange = formatFeatureMask;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private DeserializationConfig(DeserializationConfig src, SubtypeResolver str)
/*     */   {
/* 147 */     super(src, str);
/* 148 */     this._deserFeatures = src._deserFeatures;
/* 149 */     this._nodeFactory = src._nodeFactory;
/* 150 */     this._problemHandlers = src._problemHandlers;
/* 151 */     this._parserFeatures = src._parserFeatures;
/* 152 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 153 */     this._formatReadFeatures = src._formatReadFeatures;
/* 154 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, BaseSettings base)
/*     */   {
/* 159 */     super(src, base);
/* 160 */     this._deserFeatures = src._deserFeatures;
/* 161 */     this._nodeFactory = src._nodeFactory;
/* 162 */     this._problemHandlers = src._problemHandlers;
/* 163 */     this._parserFeatures = src._parserFeatures;
/* 164 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 165 */     this._formatReadFeatures = src._formatReadFeatures;
/* 166 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, JsonNodeFactory f)
/*     */   {
/* 171 */     super(src);
/* 172 */     this._deserFeatures = src._deserFeatures;
/* 173 */     this._problemHandlers = src._problemHandlers;
/* 174 */     this._nodeFactory = f;
/* 175 */     this._parserFeatures = src._parserFeatures;
/* 176 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 177 */     this._formatReadFeatures = src._formatReadFeatures;
/* 178 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */   
/*     */ 
/*     */   private DeserializationConfig(DeserializationConfig src, LinkedNode<DeserializationProblemHandler> problemHandlers)
/*     */   {
/* 184 */     super(src);
/* 185 */     this._deserFeatures = src._deserFeatures;
/* 186 */     this._problemHandlers = problemHandlers;
/* 187 */     this._nodeFactory = src._nodeFactory;
/* 188 */     this._parserFeatures = src._parserFeatures;
/* 189 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 190 */     this._formatReadFeatures = src._formatReadFeatures;
/* 191 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, PropertyName rootName)
/*     */   {
/* 196 */     super(src, rootName);
/* 197 */     this._deserFeatures = src._deserFeatures;
/* 198 */     this._problemHandlers = src._problemHandlers;
/* 199 */     this._nodeFactory = src._nodeFactory;
/* 200 */     this._parserFeatures = src._parserFeatures;
/* 201 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 202 */     this._formatReadFeatures = src._formatReadFeatures;
/* 203 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */   
/*     */   private DeserializationConfig(DeserializationConfig src, Class<?> view)
/*     */   {
/* 208 */     super(src, view);
/* 209 */     this._deserFeatures = src._deserFeatures;
/* 210 */     this._problemHandlers = src._problemHandlers;
/* 211 */     this._nodeFactory = src._nodeFactory;
/* 212 */     this._parserFeatures = src._parserFeatures;
/* 213 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 214 */     this._formatReadFeatures = src._formatReadFeatures;
/* 215 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */   
/*     */   protected DeserializationConfig(DeserializationConfig src, ContextAttributes attrs)
/*     */   {
/* 220 */     super(src, attrs);
/* 221 */     this._deserFeatures = src._deserFeatures;
/* 222 */     this._problemHandlers = src._problemHandlers;
/* 223 */     this._nodeFactory = src._nodeFactory;
/* 224 */     this._parserFeatures = src._parserFeatures;
/* 225 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 226 */     this._formatReadFeatures = src._formatReadFeatures;
/* 227 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */   
/*     */   protected DeserializationConfig(DeserializationConfig src, SimpleMixInResolver mixins)
/*     */   {
/* 232 */     super(src, mixins);
/* 233 */     this._deserFeatures = src._deserFeatures;
/* 234 */     this._problemHandlers = src._problemHandlers;
/* 235 */     this._nodeFactory = src._nodeFactory;
/* 236 */     this._parserFeatures = src._parserFeatures;
/* 237 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 238 */     this._formatReadFeatures = src._formatReadFeatures;
/* 239 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected DeserializationConfig(DeserializationConfig src, SimpleMixInResolver mixins, RootNameLookup rootNames, ConfigOverrides configOverrides)
/*     */   {
/* 250 */     super(src, mixins, rootNames, configOverrides);
/* 251 */     this._deserFeatures = src._deserFeatures;
/* 252 */     this._problemHandlers = src._problemHandlers;
/* 253 */     this._nodeFactory = src._nodeFactory;
/* 254 */     this._parserFeatures = src._parserFeatures;
/* 255 */     this._parserFeaturesToChange = src._parserFeaturesToChange;
/* 256 */     this._formatReadFeatures = src._formatReadFeatures;
/* 257 */     this._formatReadFeaturesToChange = src._formatReadFeaturesToChange;
/*     */   }
/*     */   
/*     */   protected BaseSettings getBaseSettings() {
/* 261 */     return this._base;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig with(MapperFeature... features)
/*     */   {
/* 272 */     int newMapperFlags = this._mapperFeatures;
/* 273 */     for (MapperFeature f : features) {
/* 274 */       newMapperFlags |= f.getMask();
/*     */     }
/* 276 */     return newMapperFlags == this._mapperFeatures ? this : new DeserializationConfig(this, newMapperFlags, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig without(MapperFeature... features)
/*     */   {
/* 286 */     int newMapperFlags = this._mapperFeatures;
/* 287 */     for (MapperFeature f : features) {
/* 288 */       newMapperFlags &= (f.getMask() ^ 0xFFFFFFFF);
/*     */     }
/* 290 */     return newMapperFlags == this._mapperFeatures ? this : new DeserializationConfig(this, newMapperFlags, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */   public DeserializationConfig with(MapperFeature feature, boolean state)
/*     */   {
/*     */     int newMapperFlags;
/*     */     
/*     */     int newMapperFlags;
/*     */     
/* 300 */     if (state) {
/* 301 */       newMapperFlags = this._mapperFeatures | feature.getMask();
/*     */     } else {
/* 303 */       newMapperFlags = this._mapperFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/*     */     }
/* 305 */     return newMapperFlags == this._mapperFeatures ? this : new DeserializationConfig(this, newMapperFlags, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig with(ClassIntrospector ci)
/*     */   {
/* 313 */     return _withBase(this._base.withClassIntrospector(ci));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(AnnotationIntrospector ai)
/*     */   {
/* 318 */     return _withBase(this._base.withAnnotationIntrospector(ai));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(VisibilityChecker<?> vc)
/*     */   {
/* 323 */     return _withBase(this._base.withVisibilityChecker(vc));
/*     */   }
/*     */   
/*     */   public DeserializationConfig withVisibility(PropertyAccessor forMethod, JsonAutoDetect.Visibility visibility)
/*     */   {
/* 328 */     return _withBase(this._base.withVisibility(forMethod, visibility));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(TypeResolverBuilder<?> trb)
/*     */   {
/* 333 */     return _withBase(this._base.withTypeResolverBuilder(trb));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(SubtypeResolver str)
/*     */   {
/* 338 */     return this._subtypeResolver == str ? this : new DeserializationConfig(this, str);
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(PropertyNamingStrategy pns)
/*     */   {
/* 343 */     return _withBase(this._base.withPropertyNamingStrategy(pns));
/*     */   }
/*     */   
/*     */   public DeserializationConfig withRootName(PropertyName rootName)
/*     */   {
/* 348 */     if (rootName == null) {
/* 349 */       if (this._rootName == null) {
/* 350 */         return this;
/*     */       }
/* 352 */     } else if (rootName.equals(this._rootName)) {
/* 353 */       return this;
/*     */     }
/* 355 */     return new DeserializationConfig(this, rootName);
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(TypeFactory tf)
/*     */   {
/* 360 */     return _withBase(this._base.withTypeFactory(tf));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(DateFormat df)
/*     */   {
/* 365 */     return _withBase(this._base.withDateFormat(df));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(HandlerInstantiator hi)
/*     */   {
/* 370 */     return _withBase(this._base.withHandlerInstantiator(hi));
/*     */   }
/*     */   
/*     */   public DeserializationConfig withInsertedAnnotationIntrospector(AnnotationIntrospector ai)
/*     */   {
/* 375 */     return _withBase(this._base.withInsertedAnnotationIntrospector(ai));
/*     */   }
/*     */   
/*     */   public DeserializationConfig withAppendedAnnotationIntrospector(AnnotationIntrospector ai)
/*     */   {
/* 380 */     return _withBase(this._base.withAppendedAnnotationIntrospector(ai));
/*     */   }
/*     */   
/*     */   public DeserializationConfig withView(Class<?> view)
/*     */   {
/* 385 */     return this._view == view ? this : new DeserializationConfig(this, view);
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(Locale l)
/*     */   {
/* 390 */     return _withBase(this._base.with(l));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(TimeZone tz)
/*     */   {
/* 395 */     return _withBase(this._base.with(tz));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(Base64Variant base64)
/*     */   {
/* 400 */     return _withBase(this._base.with(base64));
/*     */   }
/*     */   
/*     */   public DeserializationConfig with(ContextAttributes attrs)
/*     */   {
/* 405 */     return attrs == this._attributes ? this : new DeserializationConfig(this, attrs);
/*     */   }
/*     */   
/*     */   private final DeserializationConfig _withBase(BaseSettings newBase) {
/* 409 */     return this._base == newBase ? this : new DeserializationConfig(this, newBase);
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
/*     */   public DeserializationConfig with(DeserializationFeature feature)
/*     */   {
/* 424 */     int newDeserFeatures = this._deserFeatures | feature.getMask();
/* 425 */     return newDeserFeatures == this._deserFeatures ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig with(DeserializationFeature first, DeserializationFeature... features)
/*     */   {
/* 438 */     int newDeserFeatures = this._deserFeatures | first.getMask();
/* 439 */     for (DeserializationFeature f : features) {
/* 440 */       newDeserFeatures |= f.getMask();
/*     */     }
/* 442 */     return newDeserFeatures == this._deserFeatures ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig withFeatures(DeserializationFeature... features)
/*     */   {
/* 454 */     int newDeserFeatures = this._deserFeatures;
/* 455 */     for (DeserializationFeature f : features) {
/* 456 */       newDeserFeatures |= f.getMask();
/*     */     }
/* 458 */     return newDeserFeatures == this._deserFeatures ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig without(DeserializationFeature feature)
/*     */   {
/* 470 */     int newDeserFeatures = this._deserFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 471 */     return newDeserFeatures == this._deserFeatures ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig without(DeserializationFeature first, DeserializationFeature... features)
/*     */   {
/* 484 */     int newDeserFeatures = this._deserFeatures & (first.getMask() ^ 0xFFFFFFFF);
/* 485 */     for (DeserializationFeature f : features) {
/* 486 */       newDeserFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/*     */     }
/* 488 */     return newDeserFeatures == this._deserFeatures ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig withoutFeatures(DeserializationFeature... features)
/*     */   {
/* 500 */     int newDeserFeatures = this._deserFeatures;
/* 501 */     for (DeserializationFeature f : features) {
/* 502 */       newDeserFeatures &= (f.getMask() ^ 0xFFFFFFFF);
/*     */     }
/* 504 */     return newDeserFeatures == this._deserFeatures ? this : new DeserializationConfig(this, this._mapperFeatures, newDeserFeatures, this._parserFeatures, this._parserFeaturesToChange, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig with(JsonParser.Feature feature)
/*     */   {
/* 524 */     int newSet = this._parserFeatures | feature.getMask();
/* 525 */     int newMask = this._parserFeaturesToChange | feature.getMask();
/* 526 */     return (this._parserFeatures == newSet) && (this._parserFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, newSet, newMask, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig withFeatures(JsonParser.Feature... features)
/*     */   {
/* 540 */     int newSet = this._parserFeatures;
/* 541 */     int newMask = this._parserFeaturesToChange;
/* 542 */     for (JsonParser.Feature f : features) {
/* 543 */       int mask = f.getMask();
/* 544 */       newSet |= mask;
/* 545 */       newMask |= mask;
/*     */     }
/* 547 */     return (this._parserFeatures == newSet) && (this._parserFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, newSet, newMask, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig without(JsonParser.Feature feature)
/*     */   {
/* 561 */     int newSet = this._parserFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 562 */     int newMask = this._parserFeaturesToChange | feature.getMask();
/* 563 */     return (this._parserFeatures == newSet) && (this._parserFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, newSet, newMask, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig withoutFeatures(JsonParser.Feature... features)
/*     */   {
/* 577 */     int newSet = this._parserFeatures;
/* 578 */     int newMask = this._parserFeaturesToChange;
/* 579 */     for (JsonParser.Feature f : features) {
/* 580 */       int mask = f.getMask();
/* 581 */       newSet &= (mask ^ 0xFFFFFFFF);
/* 582 */       newMask |= mask;
/*     */     }
/* 584 */     return (this._parserFeatures == newSet) && (this._parserFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, newSet, newMask, this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */   public DeserializationConfig with(FormatFeature feature)
/*     */   {
/* 604 */     int newSet = this._formatReadFeatures | feature.getMask();
/* 605 */     int newMask = this._formatReadFeaturesToChange | feature.getMask();
/* 606 */     return (this._formatReadFeatures == newSet) && (this._formatReadFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, newSet, newMask);
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
/*     */   public DeserializationConfig withFeatures(FormatFeature... features)
/*     */   {
/* 620 */     int newSet = this._formatReadFeatures;
/* 621 */     int newMask = this._formatReadFeaturesToChange;
/* 622 */     for (FormatFeature f : features) {
/* 623 */       int mask = f.getMask();
/* 624 */       newSet |= mask;
/* 625 */       newMask |= mask;
/*     */     }
/* 627 */     return (this._formatReadFeatures == newSet) && (this._formatReadFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, newSet, newMask);
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
/*     */   public DeserializationConfig without(FormatFeature feature)
/*     */   {
/* 641 */     int newSet = this._formatReadFeatures & (feature.getMask() ^ 0xFFFFFFFF);
/* 642 */     int newMask = this._formatReadFeaturesToChange | feature.getMask();
/* 643 */     return (this._formatReadFeatures == newSet) && (this._formatReadFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, newSet, newMask);
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
/*     */   public DeserializationConfig withoutFeatures(FormatFeature... features)
/*     */   {
/* 657 */     int newSet = this._formatReadFeatures;
/* 658 */     int newMask = this._formatReadFeaturesToChange;
/* 659 */     for (FormatFeature f : features) {
/* 660 */       int mask = f.getMask();
/* 661 */       newSet &= (mask ^ 0xFFFFFFFF);
/* 662 */       newMask |= mask;
/*     */     }
/* 664 */     return (this._formatReadFeatures == newSet) && (this._formatReadFeaturesToChange == newMask) ? this : new DeserializationConfig(this, this._mapperFeatures, this._deserFeatures, this._parserFeatures, this._parserFeaturesToChange, newSet, newMask);
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
/*     */   public DeserializationConfig with(JsonNodeFactory f)
/*     */   {
/* 681 */     if (this._nodeFactory == f) {
/* 682 */       return this;
/*     */     }
/* 684 */     return new DeserializationConfig(this, f);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig withHandler(DeserializationProblemHandler h)
/*     */   {
/* 694 */     if (LinkedNode.contains(this._problemHandlers, h)) {
/* 695 */       return this;
/*     */     }
/* 697 */     return new DeserializationConfig(this, new LinkedNode(h, this._problemHandlers));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DeserializationConfig withNoProblemHandlers()
/*     */   {
/* 706 */     if (this._problemHandlers == null) {
/* 707 */       return this;
/*     */     }
/* 709 */     return new DeserializationConfig(this, (LinkedNode)null);
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
/*     */   public void initialize(JsonParser p)
/*     */   {
/* 727 */     if (this._parserFeaturesToChange != 0) {
/* 728 */       p.overrideStdFeatures(this._parserFeatures, this._parserFeaturesToChange);
/*     */     }
/* 730 */     if (this._formatReadFeaturesToChange != 0) {
/* 731 */       p.overrideFormatFeatures(this._formatReadFeatures, this._formatReadFeaturesToChange);
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
/*     */ 
/*     */   public AnnotationIntrospector getAnnotationIntrospector()
/*     */   {
/* 751 */     if (isEnabled(MapperFeature.USE_ANNOTATIONS)) {
/* 752 */       return super.getAnnotationIntrospector();
/*     */     }
/* 754 */     return NopAnnotationIntrospector.instance;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanDescription introspectClassAnnotations(JavaType type)
/*     */   {
/* 763 */     return getClassIntrospector().forClassAnnotations(this, type, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanDescription introspectDirectClassAnnotations(JavaType type)
/*     */   {
/* 773 */     return getClassIntrospector().forDirectClassAnnotations(this, type, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonInclude.Value getDefaultPropertyInclusion()
/*     */   {
/* 784 */     return EMPTY_INCLUDE;
/*     */   }
/*     */   
/*     */   public JsonInclude.Value getDefaultPropertyInclusion(Class<?> baseType)
/*     */   {
/* 789 */     ConfigOverride overrides = findConfigOverride(baseType);
/* 790 */     if (overrides != null) {
/* 791 */       JsonInclude.Value v = overrides.getInclude();
/* 792 */       if (v != null) {
/* 793 */         return v;
/*     */       }
/*     */     }
/* 796 */     return EMPTY_INCLUDE;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonInclude.Value getDefaultPropertyInclusion(Class<?> baseType, JsonInclude.Value defaultIncl)
/*     */   {
/* 803 */     ConfigOverride overrides = findConfigOverride(baseType);
/* 804 */     if (overrides != null) {
/* 805 */       JsonInclude.Value v = overrides.getInclude();
/* 806 */       if (v != null) {
/* 807 */         return v;
/*     */       }
/*     */     }
/* 810 */     return defaultIncl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean useRootWrapping()
/*     */   {
/* 822 */     if (this._rootName != null) {
/* 823 */       return !this._rootName.isEmpty();
/*     */     }
/* 825 */     return isEnabled(DeserializationFeature.UNWRAP_ROOT_VALUE);
/*     */   }
/*     */   
/*     */   public final boolean isEnabled(DeserializationFeature f) {
/* 829 */     return (this._deserFeatures & f.getMask()) != 0;
/*     */   }
/*     */   
/*     */   public final boolean isEnabled(JsonParser.Feature f, JsonFactory factory) {
/* 833 */     int mask = f.getMask();
/* 834 */     if ((this._parserFeaturesToChange & mask) != 0) {
/* 835 */       return (this._parserFeatures & f.getMask()) != 0;
/*     */     }
/* 837 */     return factory.isEnabled(f);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean hasDeserializationFeatures(int featureMask)
/*     */   {
/* 847 */     return (this._deserFeatures & featureMask) == featureMask;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean hasSomeOfFeatures(int featureMask)
/*     */   {
/* 857 */     return (this._deserFeatures & featureMask) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int getDeserializationFeatures()
/*     */   {
/* 865 */     return this._deserFeatures;
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
/*     */   public LinkedNode<DeserializationProblemHandler> getProblemHandlers()
/*     */   {
/* 879 */     return this._problemHandlers;
/*     */   }
/*     */   
/*     */   public final JsonNodeFactory getNodeFactory() {
/* 883 */     return this._nodeFactory;
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
/*     */   public <T extends BeanDescription> T introspect(JavaType type)
/*     */   {
/* 900 */     return getClassIntrospector().forDeserialization(this, type, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends BeanDescription> T introspectForCreation(JavaType type)
/*     */   {
/* 909 */     return getClassIntrospector().forCreation(this, type, this);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public <T extends BeanDescription> T introspectForBuilder(JavaType type)
/*     */   {
/* 917 */     return getClassIntrospector().forDeserializationWithBuilder(this, type, this);
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
/*     */   public TypeDeserializer findTypeDeserializer(JavaType baseType)
/*     */     throws JsonMappingException
/*     */   {
/* 936 */     BeanDescription bean = introspectClassAnnotations(baseType.getRawClass());
/* 937 */     AnnotatedClass ac = bean.getClassInfo();
/* 938 */     TypeResolverBuilder<?> b = getAnnotationIntrospector().findTypeResolver(this, ac, baseType);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 943 */     Collection<NamedType> subtypes = null;
/* 944 */     if (b == null) {
/* 945 */       b = getDefaultTyper(baseType);
/* 946 */       if (b == null) {
/* 947 */         return null;
/*     */       }
/*     */     } else {
/* 950 */       subtypes = getSubtypeResolver().collectAndResolveSubtypesByTypeId(this, ac);
/*     */     }
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
/* 965 */     return b.buildTypeDeserializer(this, baseType, subtypes);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\DeserializationConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */