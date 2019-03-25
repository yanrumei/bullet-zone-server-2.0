/*     */ package com.fasterxml.jackson.databind.cfg;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties.Value;
/*     */ import com.fasterxml.jackson.annotation.PropertyAccessor;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.SimpleMixInResolver;
/*     */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.RootNameLookup;
/*     */ import java.text.DateFormat;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ 
/*     */ public abstract class MapperConfigBase<CFG extends ConfigFeature, T extends MapperConfigBase<CFG, T>> extends MapperConfig<T> implements java.io.Serializable
/*     */ {
/*  26 */   private static final int DEFAULT_MAPPER_FEATURES = collectFeatureDefaults(MapperFeature.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final SimpleMixInResolver _mixIns;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final SubtypeResolver _subtypeResolver;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final PropertyName _rootName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Class<?> _view;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ContextAttributes _attributes;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final RootNameLookup _rootNames;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ConfigOverrides _configOverrides;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapperConfigBase(BaseSettings base, SubtypeResolver str, SimpleMixInResolver mixins, RootNameLookup rootNames, ConfigOverrides configOverrides)
/*     */   {
/* 108 */     super(base, DEFAULT_MAPPER_FEATURES);
/* 109 */     this._mixIns = mixins;
/* 110 */     this._subtypeResolver = str;
/* 111 */     this._rootNames = rootNames;
/* 112 */     this._rootName = null;
/* 113 */     this._view = null;
/*     */     
/* 115 */     this._attributes = ContextAttributes.getEmpty();
/* 116 */     this._configOverrides = configOverrides;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected MapperConfigBase(BaseSettings base, SubtypeResolver str, SimpleMixInResolver mixins, RootNameLookup rootNames)
/*     */   {
/* 127 */     this(base, str, mixins, rootNames, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src)
/*     */   {
/* 136 */     super(src);
/* 137 */     this._mixIns = src._mixIns;
/* 138 */     this._subtypeResolver = src._subtypeResolver;
/* 139 */     this._rootNames = src._rootNames;
/* 140 */     this._rootName = src._rootName;
/* 141 */     this._view = src._view;
/* 142 */     this._attributes = src._attributes;
/* 143 */     this._configOverrides = src._configOverrides;
/*     */   }
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, BaseSettings base)
/*     */   {
/* 148 */     super(src, base);
/* 149 */     this._mixIns = src._mixIns;
/* 150 */     this._subtypeResolver = src._subtypeResolver;
/* 151 */     this._rootNames = src._rootNames;
/* 152 */     this._rootName = src._rootName;
/* 153 */     this._view = src._view;
/* 154 */     this._attributes = src._attributes;
/* 155 */     this._configOverrides = src._configOverrides;
/*     */   }
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, int mapperFeatures)
/*     */   {
/* 160 */     super(src, mapperFeatures);
/* 161 */     this._mixIns = src._mixIns;
/* 162 */     this._subtypeResolver = src._subtypeResolver;
/* 163 */     this._rootNames = src._rootNames;
/* 164 */     this._rootName = src._rootName;
/* 165 */     this._view = src._view;
/* 166 */     this._attributes = src._attributes;
/* 167 */     this._configOverrides = src._configOverrides;
/*     */   }
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, SubtypeResolver str) {
/* 171 */     super(src);
/* 172 */     this._mixIns = src._mixIns;
/* 173 */     this._subtypeResolver = str;
/* 174 */     this._rootNames = src._rootNames;
/* 175 */     this._rootName = src._rootName;
/* 176 */     this._view = src._view;
/* 177 */     this._attributes = src._attributes;
/* 178 */     this._configOverrides = src._configOverrides;
/*     */   }
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, PropertyName rootName) {
/* 182 */     super(src);
/* 183 */     this._mixIns = src._mixIns;
/* 184 */     this._subtypeResolver = src._subtypeResolver;
/* 185 */     this._rootNames = src._rootNames;
/* 186 */     this._rootName = rootName;
/* 187 */     this._view = src._view;
/* 188 */     this._attributes = src._attributes;
/* 189 */     this._configOverrides = src._configOverrides;
/*     */   }
/*     */   
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, Class<?> view)
/*     */   {
/* 194 */     super(src);
/* 195 */     this._mixIns = src._mixIns;
/* 196 */     this._subtypeResolver = src._subtypeResolver;
/* 197 */     this._rootNames = src._rootNames;
/* 198 */     this._rootName = src._rootName;
/* 199 */     this._view = view;
/* 200 */     this._attributes = src._attributes;
/* 201 */     this._configOverrides = src._configOverrides;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, SimpleMixInResolver mixins)
/*     */   {
/* 209 */     super(src);
/* 210 */     this._mixIns = mixins;
/* 211 */     this._subtypeResolver = src._subtypeResolver;
/* 212 */     this._rootNames = src._rootNames;
/* 213 */     this._rootName = src._rootName;
/* 214 */     this._view = src._view;
/* 215 */     this._attributes = src._attributes;
/* 216 */     this._configOverrides = src._configOverrides;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, ContextAttributes attr)
/*     */   {
/* 224 */     super(src);
/* 225 */     this._mixIns = src._mixIns;
/* 226 */     this._subtypeResolver = src._subtypeResolver;
/* 227 */     this._rootNames = src._rootNames;
/* 228 */     this._rootName = src._rootName;
/* 229 */     this._view = src._view;
/* 230 */     this._attributes = attr;
/* 231 */     this._configOverrides = src._configOverrides;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapperConfigBase(MapperConfigBase<CFG, T> src, SimpleMixInResolver mixins, RootNameLookup rootNames, ConfigOverrides configOverrides)
/*     */   {
/* 240 */     super(src);
/* 241 */     this._mixIns = mixins;
/* 242 */     this._subtypeResolver = src._subtypeResolver;
/* 243 */     this._rootNames = rootNames;
/* 244 */     this._rootName = src._rootName;
/* 245 */     this._view = src._view;
/* 246 */     this._attributes = src._attributes;
/* 247 */     this._configOverrides = configOverrides;
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
/*     */   public VisibilityChecker<?> getDefaultVisibilityChecker()
/*     */   {
/* 260 */     VisibilityChecker<?> vchecker = super.getDefaultVisibilityChecker();
/*     */     
/* 262 */     if (!isEnabled(MapperFeature.AUTO_DETECT_SETTERS)) {
/* 263 */       vchecker = vchecker.withSetterVisibility(JsonAutoDetect.Visibility.NONE);
/*     */     }
/* 265 */     if (!isEnabled(MapperFeature.AUTO_DETECT_CREATORS)) {
/* 266 */       vchecker = vchecker.withCreatorVisibility(JsonAutoDetect.Visibility.NONE);
/*     */     }
/* 268 */     if (!isEnabled(MapperFeature.AUTO_DETECT_GETTERS)) {
/* 269 */       vchecker = vchecker.withGetterVisibility(JsonAutoDetect.Visibility.NONE);
/*     */     }
/* 271 */     if (!isEnabled(MapperFeature.AUTO_DETECT_IS_GETTERS)) {
/* 272 */       vchecker = vchecker.withIsGetterVisibility(JsonAutoDetect.Visibility.NONE);
/*     */     }
/* 274 */     if (!isEnabled(MapperFeature.AUTO_DETECT_FIELDS)) {
/* 275 */       vchecker = vchecker.withFieldVisibility(JsonAutoDetect.Visibility.NONE);
/*     */     }
/* 277 */     return vchecker;
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
/*     */   public abstract T with(AnnotationIntrospector paramAnnotationIntrospector);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T withAppendedAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T withInsertedAnnotationIntrospector(AnnotationIntrospector paramAnnotationIntrospector);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(ClassIntrospector paramClassIntrospector);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(DateFormat paramDateFormat);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(HandlerInstantiator paramHandlerInstantiator);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(com.fasterxml.jackson.databind.PropertyNamingStrategy paramPropertyNamingStrategy);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T withRootName(PropertyName paramPropertyName);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T withRootName(String rootName)
/*     */   {
/* 366 */     if (rootName == null) {
/* 367 */       return withRootName((PropertyName)null);
/*     */     }
/* 369 */     return withRootName(PropertyName.construct(rootName));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(SubtypeResolver paramSubtypeResolver);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(TypeFactory paramTypeFactory);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(TypeResolverBuilder<?> paramTypeResolverBuilder);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T withView(Class<?> paramClass);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(VisibilityChecker<?> paramVisibilityChecker);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T withVisibility(PropertyAccessor paramPropertyAccessor, JsonAutoDetect.Visibility paramVisibility);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(Locale paramLocale);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(TimeZone paramTimeZone);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(com.fasterxml.jackson.core.Base64Variant paramBase64Variant);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(ContextAttributes paramContextAttributes);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T withAttributes(Map<?, ?> attributes)
/*     */   {
/* 447 */     return with(getAttributes().withSharedAttributes(attributes));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T withAttribute(Object key, Object value)
/*     */   {
/* 457 */     return with(getAttributes().withSharedAttribute(key, value));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T withoutAttribute(Object key)
/*     */   {
/* 467 */     return with(getAttributes().withoutSharedAttribute(key));
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
/*     */   public final SubtypeResolver getSubtypeResolver()
/*     */   {
/* 483 */     return this._subtypeResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public final String getRootName()
/*     */   {
/* 491 */     return this._rootName == null ? null : this._rootName.getSimpleName();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final PropertyName getFullRootName()
/*     */   {
/* 498 */     return this._rootName;
/*     */   }
/*     */   
/*     */   public final Class<?> getActiveView()
/*     */   {
/* 503 */     return this._view;
/*     */   }
/*     */   
/*     */   public final ContextAttributes getAttributes()
/*     */   {
/* 508 */     return this._attributes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final ConfigOverride findConfigOverride(Class<?> type)
/*     */   {
/* 519 */     return this._configOverrides.findOverride(type);
/*     */   }
/*     */   
/*     */   public final JsonFormat.Value getDefaultPropertyFormat(Class<?> type)
/*     */   {
/* 524 */     ConfigOverride overrides = this._configOverrides.findOverride(type);
/* 525 */     if (overrides != null) {
/* 526 */       JsonFormat.Value v = overrides.getFormat();
/* 527 */       if (v != null) {
/* 528 */         return v;
/*     */       }
/*     */     }
/* 531 */     return EMPTY_FORMAT;
/*     */   }
/*     */   
/*     */   public final JsonIgnoreProperties.Value getDefaultPropertyIgnorals(Class<?> type)
/*     */   {
/* 536 */     ConfigOverride overrides = this._configOverrides.findOverride(type);
/* 537 */     if (overrides != null) {
/* 538 */       JsonIgnoreProperties.Value v = overrides.getIgnorals();
/* 539 */       if (v != null) {
/* 540 */         return v;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 545 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final JsonIgnoreProperties.Value getDefaultPropertyIgnorals(Class<?> baseType, AnnotatedClass actualClass)
/*     */   {
/* 552 */     AnnotationIntrospector intr = getAnnotationIntrospector();
/* 553 */     JsonIgnoreProperties.Value base = intr == null ? null : intr.findPropertyIgnorals(actualClass);
/*     */     
/* 555 */     JsonIgnoreProperties.Value overrides = getDefaultPropertyIgnorals(baseType);
/* 556 */     return JsonIgnoreProperties.Value.merge(base, overrides);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyName findRootName(JavaType rootType)
/*     */   {
/* 567 */     if (this._rootName != null) {
/* 568 */       return this._rootName;
/*     */     }
/* 570 */     return this._rootNames.findRootName(rootType, this);
/*     */   }
/*     */   
/*     */   public PropertyName findRootName(Class<?> rawRootType)
/*     */   {
/* 575 */     if (this._rootName != null) {
/* 576 */       return this._rootName;
/*     */     }
/* 578 */     return this._rootNames.findRootName(rawRootType, this);
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
/*     */   public final Class<?> findMixInClassFor(Class<?> cls)
/*     */   {
/* 593 */     return this._mixIns.findMixInClassFor(cls);
/*     */   }
/*     */   
/*     */ 
/*     */   public com.fasterxml.jackson.databind.introspect.ClassIntrospector.MixInResolver copy()
/*     */   {
/* 599 */     throw new UnsupportedOperationException();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final int mixInCount()
/*     */   {
/* 607 */     return this._mixIns.localSize();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\cfg\MapperConfigBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */