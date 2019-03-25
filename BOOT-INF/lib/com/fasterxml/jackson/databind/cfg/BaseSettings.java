/*     */ package com.fasterxml.jackson.databind.cfg;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
/*     */ import com.fasterxml.jackson.annotation.PropertyAccessor;
/*     */ import com.fasterxml.jackson.core.Base64Variant;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
/*     */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*     */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.StdDateFormat;
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
/*     */ public final class BaseSettings
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  32 */   private static final TimeZone DEFAULT_TIMEZONE = TimeZone.getTimeZone("UTC");
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ClassIntrospector _classIntrospector;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final AnnotationIntrospector _annotationIntrospector;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final VisibilityChecker<?> _visibilityChecker;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final PropertyNamingStrategy _propertyNamingStrategy;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final TypeFactory _typeFactory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final TypeResolverBuilder<?> _typeResolverBuilder;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final DateFormat _dateFormat;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final HandlerInstantiator _handlerInstantiator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Locale _locale;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final TimeZone _timeZone;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Base64Variant _defaultBase64;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BaseSettings(ClassIntrospector ci, AnnotationIntrospector ai, VisibilityChecker<?> vc, PropertyNamingStrategy pns, TypeFactory tf, TypeResolverBuilder<?> typer, DateFormat dateFormat, HandlerInstantiator hi, Locale locale, TimeZone tz, Base64Variant defaultBase64)
/*     */   {
/* 152 */     this._classIntrospector = ci;
/* 153 */     this._annotationIntrospector = ai;
/* 154 */     this._visibilityChecker = vc;
/* 155 */     this._propertyNamingStrategy = pns;
/* 156 */     this._typeFactory = tf;
/* 157 */     this._typeResolverBuilder = typer;
/* 158 */     this._dateFormat = dateFormat;
/* 159 */     this._handlerInstantiator = hi;
/* 160 */     this._locale = locale;
/* 161 */     this._timeZone = tz;
/* 162 */     this._defaultBase64 = defaultBase64;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BaseSettings withClassIntrospector(ClassIntrospector ci)
/*     */   {
/* 172 */     if (this._classIntrospector == ci) {
/* 173 */       return this;
/*     */     }
/* 175 */     return new BaseSettings(ci, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */   public BaseSettings withAnnotationIntrospector(AnnotationIntrospector ai)
/*     */   {
/* 181 */     if (this._annotationIntrospector == ai) {
/* 182 */       return this;
/*     */     }
/* 184 */     return new BaseSettings(this._classIntrospector, ai, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */   public BaseSettings withInsertedAnnotationIntrospector(AnnotationIntrospector ai)
/*     */   {
/* 190 */     return withAnnotationIntrospector(AnnotationIntrospectorPair.create(ai, this._annotationIntrospector));
/*     */   }
/*     */   
/*     */   public BaseSettings withAppendedAnnotationIntrospector(AnnotationIntrospector ai) {
/* 194 */     return withAnnotationIntrospector(AnnotationIntrospectorPair.create(this._annotationIntrospector, ai));
/*     */   }
/*     */   
/*     */   public BaseSettings withVisibilityChecker(VisibilityChecker<?> vc) {
/* 198 */     if (this._visibilityChecker == vc) {
/* 199 */       return this;
/*     */     }
/* 201 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, vc, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */   public BaseSettings withVisibility(PropertyAccessor forMethod, JsonAutoDetect.Visibility visibility)
/*     */   {
/* 207 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker.withVisibility(forMethod, visibility), this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BaseSettings withPropertyNamingStrategy(PropertyNamingStrategy pns)
/*     */   {
/* 215 */     if (this._propertyNamingStrategy == pns) {
/* 216 */       return this;
/*     */     }
/* 218 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, pns, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */   public BaseSettings withTypeFactory(TypeFactory tf)
/*     */   {
/* 224 */     if (this._typeFactory == tf) {
/* 225 */       return this;
/*     */     }
/* 227 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, tf, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */   public BaseSettings withTypeResolverBuilder(TypeResolverBuilder<?> typer)
/*     */   {
/* 233 */     if (this._typeResolverBuilder == typer) {
/* 234 */       return this;
/*     */     }
/* 236 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, typer, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */   public BaseSettings withDateFormat(DateFormat df)
/*     */   {
/* 242 */     if (this._dateFormat == df) {
/* 243 */       return this;
/*     */     }
/*     */     
/*     */ 
/* 247 */     if ((df != null) && (hasExplicitTimeZone())) {
/* 248 */       df = _force(df, this._timeZone);
/*     */     }
/* 250 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, df, this._handlerInstantiator, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */   public BaseSettings withHandlerInstantiator(HandlerInstantiator hi)
/*     */   {
/* 256 */     if (this._handlerInstantiator == hi) {
/* 257 */       return this;
/*     */     }
/* 259 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, hi, this._locale, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */   public BaseSettings with(Locale l)
/*     */   {
/* 265 */     if (this._locale == l) {
/* 266 */       return this;
/*     */     }
/* 268 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, l, this._timeZone, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BaseSettings with(TimeZone tz)
/*     */   {
/* 280 */     if (tz == null) {
/* 281 */       throw new IllegalArgumentException();
/*     */     }
/* 283 */     if (tz == this._timeZone) {
/* 284 */       return this;
/*     */     }
/*     */     
/* 287 */     DateFormat df = _force(this._dateFormat, tz);
/* 288 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, df, this._handlerInstantiator, this._locale, tz, this._defaultBase64);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BaseSettings with(Base64Variant base64)
/*     */   {
/* 298 */     if (base64 == this._defaultBase64) {
/* 299 */       return this;
/*     */     }
/* 301 */     return new BaseSettings(this._classIntrospector, this._annotationIntrospector, this._visibilityChecker, this._propertyNamingStrategy, this._typeFactory, this._typeResolverBuilder, this._dateFormat, this._handlerInstantiator, this._locale, this._timeZone, base64);
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
/*     */   public ClassIntrospector getClassIntrospector()
/*     */   {
/* 314 */     return this._classIntrospector;
/*     */   }
/*     */   
/*     */   public AnnotationIntrospector getAnnotationIntrospector() {
/* 318 */     return this._annotationIntrospector;
/*     */   }
/*     */   
/*     */   public VisibilityChecker<?> getVisibilityChecker() {
/* 322 */     return this._visibilityChecker;
/*     */   }
/*     */   
/*     */   public PropertyNamingStrategy getPropertyNamingStrategy() {
/* 326 */     return this._propertyNamingStrategy;
/*     */   }
/*     */   
/*     */   public TypeFactory getTypeFactory() {
/* 330 */     return this._typeFactory;
/*     */   }
/*     */   
/*     */   public TypeResolverBuilder<?> getTypeResolverBuilder() {
/* 334 */     return this._typeResolverBuilder;
/*     */   }
/*     */   
/*     */   public DateFormat getDateFormat() {
/* 338 */     return this._dateFormat;
/*     */   }
/*     */   
/*     */   public HandlerInstantiator getHandlerInstantiator() {
/* 342 */     return this._handlerInstantiator;
/*     */   }
/*     */   
/*     */   public Locale getLocale() {
/* 346 */     return this._locale;
/*     */   }
/*     */   
/*     */   public TimeZone getTimeZone() {
/* 350 */     TimeZone tz = this._timeZone;
/* 351 */     return tz == null ? DEFAULT_TIMEZONE : tz;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasExplicitTimeZone()
/*     */   {
/* 362 */     return this._timeZone != null;
/*     */   }
/*     */   
/*     */   public Base64Variant getBase64Variant() {
/* 366 */     return this._defaultBase64;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private DateFormat _force(DateFormat df, TimeZone tz)
/*     */   {
/* 377 */     if ((df instanceof StdDateFormat)) {
/* 378 */       return ((StdDateFormat)df).withTimeZone(tz);
/*     */     }
/*     */     
/* 381 */     df = (DateFormat)df.clone();
/* 382 */     df.setTimeZone(tz);
/* 383 */     return df;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\cfg\BaseSettings.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */