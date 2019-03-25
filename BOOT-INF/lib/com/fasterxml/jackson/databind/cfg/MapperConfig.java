/*     */ package com.fasterxml.jackson.databind.cfg;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties.Value;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.io.SerializedString;
/*     */ import com.fasterxml.jackson.core.type.TypeReference;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector.MixInResolver;
/*     */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import java.io.Serializable;
/*     */ import java.text.DateFormat;
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
/*     */ public abstract class MapperConfig<T extends MapperConfig<T>>
/*     */   implements ClassIntrospector.MixInResolver, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  44 */   protected static final JsonInclude.Value EMPTY_INCLUDE = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  49 */   protected static final JsonFormat.Value EMPTY_FORMAT = JsonFormat.Value.empty();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final int _mapperFeatures;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final BaseSettings _base;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MapperConfig(BaseSettings base, int mapperFeatures)
/*     */   {
/*  69 */     this._base = base;
/*  70 */     this._mapperFeatures = mapperFeatures;
/*     */   }
/*     */   
/*     */   protected MapperConfig(MapperConfig<T> src, int mapperFeatures)
/*     */   {
/*  75 */     this._base = src._base;
/*  76 */     this._mapperFeatures = mapperFeatures;
/*     */   }
/*     */   
/*     */   protected MapperConfig(MapperConfig<T> src, BaseSettings base)
/*     */   {
/*  81 */     this._base = base;
/*  82 */     this._mapperFeatures = src._mapperFeatures;
/*     */   }
/*     */   
/*     */   protected MapperConfig(MapperConfig<T> src)
/*     */   {
/*  87 */     this._base = src._base;
/*  88 */     this._mapperFeatures = src._mapperFeatures;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static <F extends Enum<F>,  extends ConfigFeature> int collectFeatureDefaults(Class<F> enumClass)
/*     */   {
/*  97 */     int flags = 0;
/*  98 */     for (F value : (Enum[])enumClass.getEnumConstants()) {
/*  99 */       if (((ConfigFeature)value).enabledByDefault()) {
/* 100 */         flags |= ((ConfigFeature)value).getMask();
/*     */       }
/*     */     }
/* 103 */     return flags;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(MapperFeature... paramVarArgs);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T without(MapperFeature... paramVarArgs);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract T with(MapperFeature paramMapperFeature, boolean paramBoolean);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isEnabled(MapperFeature f)
/*     */   {
/* 140 */     return (this._mapperFeatures & f.getMask()) != 0;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean hasMapperFeatures(int featureMask)
/*     */   {
/* 150 */     return (this._mapperFeatures & featureMask) == featureMask;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean isAnnotationProcessingEnabled()
/*     */   {
/* 160 */     return isEnabled(MapperFeature.USE_ANNOTATIONS);
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
/*     */   public final boolean canOverrideAccessModifiers()
/*     */   {
/* 175 */     return isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final boolean shouldSortPropertiesAlphabetically()
/*     */   {
/* 183 */     return isEnabled(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
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
/*     */   public abstract boolean useRootWrapping();
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
/*     */   public SerializableString compileString(String src)
/*     */   {
/* 215 */     return new SerializedString(src);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ClassIntrospector getClassIntrospector()
/*     */   {
/* 225 */     return this._base.getClassIntrospector();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotationIntrospector getAnnotationIntrospector()
/*     */   {
/* 235 */     return this._base.getAnnotationIntrospector();
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
/* 248 */     return this._base.getVisibilityChecker();
/*     */   }
/*     */   
/*     */   public final PropertyNamingStrategy getPropertyNamingStrategy() {
/* 252 */     return this._base.getPropertyNamingStrategy();
/*     */   }
/*     */   
/*     */   public final HandlerInstantiator getHandlerInstantiator() {
/* 256 */     return this._base.getHandlerInstantiator();
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
/*     */   public final TypeResolverBuilder<?> getDefaultTyper(JavaType baseType)
/*     */   {
/* 272 */     return this._base.getTypeResolverBuilder();
/*     */   }
/*     */   
/*     */   public abstract SubtypeResolver getSubtypeResolver();
/*     */   
/*     */   public final TypeFactory getTypeFactory() {
/* 278 */     return this._base.getTypeFactory();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final JavaType constructType(Class<?> cls)
/*     */   {
/* 290 */     return getTypeFactory().constructType(cls);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final JavaType constructType(TypeReference<?> valueTypeRef)
/*     */   {
/* 302 */     return getTypeFactory().constructType(valueTypeRef.getType());
/*     */   }
/*     */   
/*     */   public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass) {
/* 306 */     return getTypeFactory().constructSpecializedType(baseType, subclass);
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
/*     */   public BeanDescription introspectClassAnnotations(Class<?> cls)
/*     */   {
/* 320 */     return introspectClassAnnotations(constructType(cls));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract BeanDescription introspectClassAnnotations(JavaType paramJavaType);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanDescription introspectDirectClassAnnotations(Class<?> cls)
/*     */   {
/* 335 */     return introspectDirectClassAnnotations(constructType(cls));
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
/*     */   public abstract BeanDescription introspectDirectClassAnnotations(JavaType paramJavaType);
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
/*     */   public abstract JsonInclude.Value getDefaultPropertyInclusion();
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
/*     */   public abstract JsonInclude.Value getDefaultPropertyInclusion(Class<?> paramClass);
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
/*     */   public abstract JsonInclude.Value getDefaultPropertyInclusion(Class<?> paramClass, JsonInclude.Value paramValue);
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
/*     */   public abstract JsonFormat.Value getDefaultPropertyFormat(Class<?> paramClass);
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
/*     */   public abstract JsonIgnoreProperties.Value getDefaultPropertyIgnorals(Class<?> paramClass);
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
/*     */   public abstract JsonIgnoreProperties.Value getDefaultPropertyIgnorals(Class<?> paramClass, AnnotatedClass paramAnnotatedClass);
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
/*     */   public abstract ConfigOverride findConfigOverride(Class<?> paramClass);
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
/*     */   public final DateFormat getDateFormat()
/*     */   {
/* 441 */     return this._base.getDateFormat();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final Locale getLocale()
/*     */   {
/* 448 */     return this._base.getLocale();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final TimeZone getTimeZone()
/*     */   {
/* 455 */     return this._base.getTimeZone();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Class<?> getActiveView();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Base64Variant getBase64Variant()
/*     */   {
/* 470 */     return this._base.getBase64Variant();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ContextAttributes getAttributes();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract PropertyName findRootName(JavaType paramJavaType);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract PropertyName findRootName(Class<?> paramClass);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeResolverBuilder<?> typeResolverBuilderInstance(Annotated annotated, Class<? extends TypeResolverBuilder<?>> builderClass)
/*     */   {
/* 505 */     HandlerInstantiator hi = getHandlerInstantiator();
/* 506 */     if (hi != null) {
/* 507 */       TypeResolverBuilder<?> builder = hi.typeResolverBuilderInstance(this, annotated, builderClass);
/* 508 */       if (builder != null) {
/* 509 */         return builder;
/*     */       }
/*     */     }
/* 512 */     return (TypeResolverBuilder)ClassUtil.createInstance(builderClass, canOverrideAccessModifiers());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public TypeIdResolver typeIdResolverInstance(Annotated annotated, Class<? extends TypeIdResolver> resolverClass)
/*     */   {
/* 522 */     HandlerInstantiator hi = getHandlerInstantiator();
/* 523 */     if (hi != null) {
/* 524 */       TypeIdResolver builder = hi.typeIdResolverInstance(this, annotated, resolverClass);
/* 525 */       if (builder != null) {
/* 526 */         return builder;
/*     */       }
/*     */     }
/* 529 */     return (TypeIdResolver)ClassUtil.createInstance(resolverClass, canOverrideAccessModifiers());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\cfg\MapperConfig.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */