/*      */ package com.fasterxml.jackson.databind;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*      */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*      */ import com.fasterxml.jackson.annotation.ObjectIdResolver;
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*      */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*      */ import com.fasterxml.jackson.databind.deser.ContextualKeyDeserializer;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializerCache;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializerFactory;
/*      */ import com.fasterxml.jackson.databind.deser.UnresolvedForwardReference;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
/*      */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*      */ import com.fasterxml.jackson.databind.deser.impl.TypeWrappedDeserializer;
/*      */ import com.fasterxml.jackson.databind.exc.InvalidFormatException;
/*      */ import com.fasterxml.jackson.databind.exc.InvalidTypeIdException;
/*      */ import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*      */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.util.ArrayBuilders;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.LinkedNode;
/*      */ import com.fasterxml.jackson.databind.util.ObjectBuffer;
/*      */ import java.io.IOException;
/*      */ import java.io.Serializable;
/*      */ import java.text.DateFormat;
/*      */ import java.text.ParseException;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collection;
/*      */ import java.util.Date;
/*      */ import java.util.Locale;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.atomic.AtomicReference;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class DeserializationContext
/*      */   extends DatabindContext
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*      */   private static final int MAX_ERROR_STR_LEN = 500;
/*      */   protected final DeserializerCache _cache;
/*      */   protected final DeserializerFactory _factory;
/*      */   protected final DeserializationConfig _config;
/*      */   protected final int _featureFlags;
/*      */   protected final Class<?> _view;
/*      */   protected transient JsonParser _parser;
/*      */   protected final InjectableValues _injectableValues;
/*      */   protected transient ArrayBuilders _arrayBuilders;
/*      */   protected transient ObjectBuffer _objectBuffer;
/*      */   protected transient DateFormat _dateFormat;
/*      */   protected transient ContextAttributes _attributes;
/*      */   protected LinkedNode<JavaType> _currentType;
/*      */   
/*      */   protected DeserializationContext(DeserializerFactory df)
/*      */   {
/*  153 */     this(df, null);
/*      */   }
/*      */   
/*      */ 
/*      */   protected DeserializationContext(DeserializerFactory df, DeserializerCache cache)
/*      */   {
/*  159 */     if (df == null) {
/*  160 */       throw new IllegalArgumentException("Can not pass null DeserializerFactory");
/*      */     }
/*  162 */     this._factory = df;
/*  163 */     this._cache = (cache == null ? new DeserializerCache() : cache);
/*      */     
/*  165 */     this._featureFlags = 0;
/*  166 */     this._config = null;
/*  167 */     this._injectableValues = null;
/*  168 */     this._view = null;
/*  169 */     this._attributes = null;
/*      */   }
/*      */   
/*      */ 
/*      */   protected DeserializationContext(DeserializationContext src, DeserializerFactory factory)
/*      */   {
/*  175 */     this._cache = src._cache;
/*  176 */     this._factory = factory;
/*      */     
/*  178 */     this._config = src._config;
/*  179 */     this._featureFlags = src._featureFlags;
/*  180 */     this._view = src._view;
/*  181 */     this._parser = src._parser;
/*  182 */     this._injectableValues = src._injectableValues;
/*  183 */     this._attributes = src._attributes;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DeserializationContext(DeserializationContext src, DeserializationConfig config, JsonParser p, InjectableValues injectableValues)
/*      */   {
/*  193 */     this._cache = src._cache;
/*  194 */     this._factory = src._factory;
/*      */     
/*  196 */     this._config = config;
/*  197 */     this._featureFlags = config.getDeserializationFeatures();
/*  198 */     this._view = config.getActiveView();
/*  199 */     this._parser = p;
/*  200 */     this._injectableValues = injectableValues;
/*  201 */     this._attributes = config.getAttributes();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected DeserializationContext(DeserializationContext src)
/*      */   {
/*  208 */     this._cache = new DeserializerCache();
/*  209 */     this._factory = src._factory;
/*      */     
/*  211 */     this._config = src._config;
/*  212 */     this._featureFlags = src._featureFlags;
/*  213 */     this._view = src._view;
/*  214 */     this._injectableValues = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DeserializationConfig getConfig()
/*      */   {
/*  224 */     return this._config;
/*      */   }
/*      */   
/*  227 */   public final Class<?> getActiveView() { return this._view; }
/*      */   
/*      */   public final boolean canOverrideAccessModifiers()
/*      */   {
/*  231 */     return this._config.canOverrideAccessModifiers();
/*      */   }
/*      */   
/*      */   public final boolean isEnabled(MapperFeature feature)
/*      */   {
/*  236 */     return this._config.isEnabled(feature);
/*      */   }
/*      */   
/*      */   public final JsonFormat.Value getDefaultPropertyFormat(Class<?> baseType)
/*      */   {
/*  241 */     return this._config.getDefaultPropertyFormat(baseType);
/*      */   }
/*      */   
/*      */   public final AnnotationIntrospector getAnnotationIntrospector()
/*      */   {
/*  246 */     return this._config.getAnnotationIntrospector();
/*      */   }
/*      */   
/*      */   public final TypeFactory getTypeFactory()
/*      */   {
/*  251 */     return this._config.getTypeFactory();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  262 */     return this._config.getLocale();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TimeZone getTimeZone()
/*      */   {
/*  273 */     return this._config.getTimeZone();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object getAttribute(Object key)
/*      */   {
/*  284 */     return this._attributes.getAttribute(key);
/*      */   }
/*      */   
/*      */ 
/*      */   public DeserializationContext setAttribute(Object key, Object value)
/*      */   {
/*  290 */     this._attributes = this._attributes.withPerCallAttribute(key, value);
/*  291 */     return this;
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
/*      */   public JavaType getContextualType()
/*      */   {
/*  308 */     return this._currentType == null ? null : (JavaType)this._currentType.value();
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
/*      */   public DeserializerFactory getFactory()
/*      */   {
/*  321 */     return this._factory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean isEnabled(DeserializationFeature feat)
/*      */   {
/*  332 */     return (this._featureFlags & feat.getMask()) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final int getDeserializationFeatures()
/*      */   {
/*  342 */     return this._featureFlags;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean hasDeserializationFeatures(int featureMask)
/*      */   {
/*  352 */     return (this._featureFlags & featureMask) == featureMask;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final boolean hasSomeOfFeatures(int featureMask)
/*      */   {
/*  362 */     return (this._featureFlags & featureMask) != 0;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final JsonParser getParser()
/*      */   {
/*  373 */     return this._parser;
/*      */   }
/*      */   
/*      */   public final Object findInjectableValue(Object valueId, BeanProperty forProperty, Object beanInstance)
/*      */   {
/*  378 */     if (this._injectableValues == null) {
/*  379 */       throw new IllegalStateException("No 'injectableValues' configured, can not inject value with id [" + valueId + "]");
/*      */     }
/*  381 */     return this._injectableValues.findInjectableValue(valueId, this, forProperty, beanInstance);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final Base64Variant getBase64Variant()
/*      */   {
/*  393 */     return this._config.getBase64Variant();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final JsonNodeFactory getNodeFactory()
/*      */   {
/*  403 */     return this._config.getNodeFactory();
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
/*      */   public boolean hasValueDeserializerFor(JavaType type, AtomicReference<Throwable> cause)
/*      */   {
/*      */     try
/*      */     {
/*  421 */       return this._cache.hasValueDeserializerFor(this, this._factory, type);
/*      */     } catch (JsonMappingException e) {
/*  423 */       if (cause != null) {
/*  424 */         cause.set(e);
/*      */       }
/*      */     } catch (RuntimeException e) {
/*  427 */       if (cause == null) {
/*  428 */         throw e;
/*      */       }
/*  430 */       cause.set(e);
/*      */     }
/*  432 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final JsonDeserializer<Object> findContextualValueDeserializer(JavaType type, BeanProperty prop)
/*      */     throws JsonMappingException
/*      */   {
/*  443 */     JsonDeserializer<Object> deser = this._cache.findValueDeserializer(this, this._factory, type);
/*  444 */     if (deser != null) {
/*  445 */       deser = handleSecondaryContextualization(deser, prop, type);
/*      */     }
/*  447 */     return deser;
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
/*      */   public final JsonDeserializer<Object> findNonContextualValueDeserializer(JavaType type)
/*      */     throws JsonMappingException
/*      */   {
/*  466 */     return this._cache.findValueDeserializer(this, this._factory, type);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final JsonDeserializer<Object> findRootValueDeserializer(JavaType type)
/*      */     throws JsonMappingException
/*      */   {
/*  476 */     JsonDeserializer<Object> deser = this._cache.findValueDeserializer(this, this._factory, type);
/*      */     
/*  478 */     if (deser == null) {
/*  479 */       return null;
/*      */     }
/*  481 */     deser = handleSecondaryContextualization(deser, null, type);
/*  482 */     TypeDeserializer typeDeser = this._factory.findTypeDeserializer(this._config, type);
/*  483 */     if (typeDeser != null)
/*      */     {
/*  485 */       typeDeser = typeDeser.forProperty(null);
/*  486 */       return new TypeWrappedDeserializer(typeDeser, deser);
/*      */     }
/*  488 */     return deser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final KeyDeserializer findKeyDeserializer(JavaType keyType, BeanProperty prop)
/*      */     throws JsonMappingException
/*      */   {
/*  499 */     KeyDeserializer kd = this._cache.findKeyDeserializer(this, this._factory, keyType);
/*      */     
/*      */ 
/*  502 */     if ((kd instanceof ContextualKeyDeserializer)) {
/*  503 */       kd = ((ContextualKeyDeserializer)kd).createContextual(this, prop);
/*      */     }
/*  505 */     return kd;
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
/*      */   public abstract ReadableObjectId findObjectId(Object paramObject, ObjectIdGenerator<?> paramObjectIdGenerator, ObjectIdResolver paramObjectIdResolver);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract void checkUnresolvedObjectId()
/*      */     throws UnresolvedForwardReference;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final JavaType constructType(Class<?> cls)
/*      */   {
/*  542 */     return this._config.constructType(cls);
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
/*      */   public Class<?> findClass(String className)
/*      */     throws ClassNotFoundException
/*      */   {
/*  556 */     return getTypeFactory().findClass(className);
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
/*      */   public final ObjectBuffer leaseObjectBuffer()
/*      */   {
/*  573 */     ObjectBuffer buf = this._objectBuffer;
/*  574 */     if (buf == null) {
/*  575 */       buf = new ObjectBuffer();
/*      */     } else {
/*  577 */       this._objectBuffer = null;
/*      */     }
/*  579 */     return buf;
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
/*      */   public final void returnObjectBuffer(ObjectBuffer buf)
/*      */   {
/*  593 */     if ((this._objectBuffer == null) || (buf.initialCapacity() >= this._objectBuffer.initialCapacity()))
/*      */     {
/*  595 */       this._objectBuffer = buf;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final ArrayBuilders getArrayBuilders()
/*      */   {
/*  605 */     if (this._arrayBuilders == null) {
/*  606 */       this._arrayBuilders = new ArrayBuilders();
/*      */     }
/*  608 */     return this._arrayBuilders;
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
/*      */   public abstract JsonDeserializer<Object> deserializerInstance(Annotated paramAnnotated, Object paramObject)
/*      */     throws JsonMappingException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public abstract KeyDeserializer keyDeserializerInstance(Annotated paramAnnotated, Object paramObject)
/*      */     throws JsonMappingException;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonDeserializer<?> handlePrimaryContextualization(JsonDeserializer<?> deser, BeanProperty prop, JavaType type)
/*      */     throws JsonMappingException
/*      */   {
/*  647 */     if ((deser instanceof ContextualDeserializer)) {
/*  648 */       this._currentType = new LinkedNode(type, this._currentType);
/*      */       try {
/*  650 */         deser = ((ContextualDeserializer)deser).createContextual(this, prop);
/*      */       } finally {
/*  652 */         this._currentType = this._currentType.next();
/*      */       }
/*      */     }
/*  655 */     return deser;
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
/*      */   public JsonDeserializer<?> handleSecondaryContextualization(JsonDeserializer<?> deser, BeanProperty prop, JavaType type)
/*      */     throws JsonMappingException
/*      */   {
/*  678 */     if ((deser instanceof ContextualDeserializer)) {
/*  679 */       this._currentType = new LinkedNode(type, this._currentType);
/*      */       try {
/*  681 */         deser = ((ContextualDeserializer)deser).createContextual(this, prop);
/*      */       } finally {
/*  683 */         this._currentType = this._currentType.next();
/*      */       }
/*      */     }
/*  686 */     return deser;
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public JsonDeserializer<?> handlePrimaryContextualization(JsonDeserializer<?> deser, BeanProperty prop) throws JsonMappingException {
/*  691 */     return handlePrimaryContextualization(deser, prop, TypeFactory.unknownType());
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public JsonDeserializer<?> handleSecondaryContextualization(JsonDeserializer<?> deser, BeanProperty prop) throws JsonMappingException {
/*  696 */     if ((deser instanceof ContextualDeserializer)) {
/*  697 */       deser = ((ContextualDeserializer)deser).createContextual(this, prop);
/*      */     }
/*  699 */     return deser;
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
/*      */   public Date parseDate(String dateStr)
/*      */     throws IllegalArgumentException
/*      */   {
/*      */     try
/*      */     {
/*  721 */       DateFormat df = getDateFormat();
/*  722 */       return df.parse(dateStr);
/*      */     } catch (ParseException e) {
/*  724 */       throw new IllegalArgumentException(String.format("Failed to parse Date value '%s': %s", new Object[] { dateStr, e.getMessage() }));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Calendar constructCalendar(Date d)
/*      */   {
/*  735 */     Calendar c = Calendar.getInstance(getTimeZone());
/*  736 */     c.setTime(d);
/*  737 */     return c;
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
/*      */   public <T> T readValue(JsonParser p, Class<T> type)
/*      */     throws IOException
/*      */   {
/*  758 */     return (T)readValue(p, getTypeFactory().constructType(type));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T> T readValue(JsonParser p, JavaType type)
/*      */     throws IOException
/*      */   {
/*  766 */     JsonDeserializer<Object> deser = findRootValueDeserializer(type);
/*  767 */     if (deser == null) {
/*  768 */       reportMappingException("Could not find JsonDeserializer for type %s", new Object[] { type });
/*      */     }
/*  770 */     return (T)deser.deserialize(p, this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T readPropertyValue(JsonParser p, BeanProperty prop, Class<T> type)
/*      */     throws IOException
/*      */   {
/*  782 */     return (T)readPropertyValue(p, prop, getTypeFactory().constructType(type));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T> T readPropertyValue(JsonParser p, BeanProperty prop, JavaType type)
/*      */     throws IOException
/*      */   {
/*  790 */     JsonDeserializer<Object> deser = findContextualValueDeserializer(type, prop);
/*  791 */     if (deser == null) {
/*  792 */       String propName = "'" + prop.getName() + "'";
/*  793 */       reportMappingException("Could not find JsonDeserializer for type %s (via property %s)", new Object[] { type, propName });
/*      */     }
/*      */     
/*      */ 
/*  797 */     return (T)deser.deserialize(p, this);
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
/*      */   public boolean handleUnknownProperty(JsonParser p, JsonDeserializer<?> deser, Object instanceOrClass, String propName)
/*      */     throws IOException
/*      */   {
/*  819 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/*  820 */     while (h != null)
/*      */     {
/*  822 */       if (((DeserializationProblemHandler)h.value()).handleUnknownProperty(this, p, deser, instanceOrClass, propName)) {
/*  823 */         return true;
/*      */       }
/*  825 */       h = h.next();
/*      */     }
/*      */     
/*  828 */     if (!isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
/*  829 */       p.skipChildren();
/*  830 */       return true;
/*      */     }
/*      */     
/*  833 */     Collection<Object> propIds = deser == null ? null : deser.getKnownPropertyNames();
/*  834 */     throw UnrecognizedPropertyException.from(this._parser, instanceOrClass, propName, propIds);
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
/*      */   public Object handleWeirdKey(Class<?> keyClass, String keyValue, String msg, Object... msgArgs)
/*      */     throws IOException
/*      */   {
/*  862 */     if (msgArgs.length > 0) {
/*  863 */       msg = String.format(msg, msgArgs);
/*      */     }
/*  865 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/*  866 */     while (h != null)
/*      */     {
/*  868 */       Object key = ((DeserializationProblemHandler)h.value()).handleWeirdKey(this, keyClass, keyValue, msg);
/*  869 */       if (key != DeserializationProblemHandler.NOT_HANDLED)
/*      */       {
/*  871 */         if ((key == null) || (keyClass.isInstance(key))) {
/*  872 */           return key;
/*      */         }
/*  874 */         throw weirdStringException(keyValue, keyClass, String.format("DeserializationProblemHandler.handleWeirdStringValue() for type %s returned value of type %s", new Object[] { keyClass, key.getClass() }));
/*      */       }
/*      */       
/*      */ 
/*  878 */       h = h.next();
/*      */     }
/*  880 */     throw weirdKeyException(keyClass, keyValue, msg);
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
/*      */   public Object handleWeirdStringValue(Class<?> targetClass, String value, String msg, Object... msgArgs)
/*      */     throws IOException
/*      */   {
/*  908 */     if (msgArgs.length > 0) {
/*  909 */       msg = String.format(msg, msgArgs);
/*      */     }
/*  911 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/*  912 */     while (h != null)
/*      */     {
/*  914 */       Object instance = ((DeserializationProblemHandler)h.value()).handleWeirdStringValue(this, targetClass, value, msg);
/*  915 */       if (instance != DeserializationProblemHandler.NOT_HANDLED)
/*      */       {
/*  917 */         if ((instance == null) || (targetClass.isInstance(instance))) {
/*  918 */           return instance;
/*      */         }
/*  920 */         throw weirdStringException(value, targetClass, String.format("DeserializationProblemHandler.handleWeirdStringValue() for type %s returned value of type %s", new Object[] { targetClass, instance.getClass() }));
/*      */       }
/*      */       
/*      */ 
/*  924 */       h = h.next();
/*      */     }
/*  926 */     throw weirdStringException(value, targetClass, msg);
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
/*      */   public Object handleWeirdNumberValue(Class<?> targetClass, Number value, String msg, Object... msgArgs)
/*      */     throws IOException
/*      */   {
/*  953 */     if (msgArgs.length > 0) {
/*  954 */       msg = String.format(msg, msgArgs);
/*      */     }
/*  956 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/*  957 */     while (h != null)
/*      */     {
/*  959 */       Object key = ((DeserializationProblemHandler)h.value()).handleWeirdNumberValue(this, targetClass, value, msg);
/*  960 */       if (key != DeserializationProblemHandler.NOT_HANDLED)
/*      */       {
/*  962 */         if ((key == null) || (targetClass.isInstance(key))) {
/*  963 */           return key;
/*      */         }
/*  965 */         throw weirdNumberException(value, targetClass, String.format("DeserializationProblemHandler.handleWeirdNumberValue() for type %s returned value of type %s", new Object[] { targetClass, key.getClass() }));
/*      */       }
/*      */       
/*      */ 
/*  969 */       h = h.next();
/*      */     }
/*  971 */     throw weirdNumberException(value, targetClass, msg);
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
/*      */   public Object handleMissingInstantiator(Class<?> instClass, JsonParser p, String msg, Object... msgArgs)
/*      */     throws IOException
/*      */   {
/*  993 */     if (msgArgs.length > 0) {
/*  994 */       msg = String.format(msg, msgArgs);
/*      */     }
/*  996 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/*  997 */     while (h != null)
/*      */     {
/*  999 */       Object instance = ((DeserializationProblemHandler)h.value()).handleMissingInstantiator(this, instClass, p, msg);
/*      */       
/* 1001 */       if (instance != DeserializationProblemHandler.NOT_HANDLED)
/*      */       {
/* 1003 */         if ((instance == null) || (instClass.isInstance(instance))) {
/* 1004 */           return instance;
/*      */         }
/* 1006 */         throw instantiationException(instClass, String.format("DeserializationProblemHandler.handleMissingInstantiator() for type %s returned value of type %s", new Object[] { instClass, instance.getClass() }));
/*      */       }
/*      */       
/*      */ 
/* 1010 */       h = h.next();
/*      */     }
/* 1012 */     throw instantiationException(instClass, msg);
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
/*      */   public Object handleInstantiationProblem(Class<?> instClass, Object argument, Throwable t)
/*      */     throws IOException
/*      */   {
/* 1036 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/* 1037 */     while (h != null)
/*      */     {
/* 1039 */       Object instance = ((DeserializationProblemHandler)h.value()).handleInstantiationProblem(this, instClass, argument, t);
/* 1040 */       if (instance != DeserializationProblemHandler.NOT_HANDLED)
/*      */       {
/* 1042 */         if ((instance == null) || (instClass.isInstance(instance))) {
/* 1043 */           return instance;
/*      */         }
/* 1045 */         throw instantiationException(instClass, String.format("DeserializationProblemHandler.handleInstantiationProblem() for type %s returned value of type %s", new Object[] { instClass, instance.getClass() }));
/*      */       }
/*      */       
/*      */ 
/* 1049 */       h = h.next();
/*      */     }
/*      */     
/* 1052 */     if ((t instanceof IOException)) {
/* 1053 */       throw ((IOException)t);
/*      */     }
/* 1055 */     throw instantiationException(instClass, t);
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
/*      */   public Object handleUnexpectedToken(Class<?> instClass, JsonParser p)
/*      */     throws IOException
/*      */   {
/* 1075 */     return handleUnexpectedToken(instClass, p.getCurrentToken(), p, null, new Object[0]);
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
/*      */   public Object handleUnexpectedToken(Class<?> instClass, JsonToken t, JsonParser p, String msg, Object... msgArgs)
/*      */     throws IOException
/*      */   {
/* 1097 */     if (msgArgs.length > 0) {
/* 1098 */       msg = String.format(msg, msgArgs);
/*      */     }
/* 1100 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/* 1101 */     while (h != null) {
/* 1102 */       Object instance = ((DeserializationProblemHandler)h.value()).handleUnexpectedToken(this, instClass, t, p, msg);
/*      */       
/* 1104 */       if (instance != DeserializationProblemHandler.NOT_HANDLED) {
/* 1105 */         if ((instance == null) || (instClass.isInstance(instance))) {
/* 1106 */           return instance;
/*      */         }
/* 1108 */         reportMappingException("DeserializationProblemHandler.handleUnexpectedToken() for type %s returned value of type %s", new Object[] { instClass, instance.getClass() });
/*      */       }
/*      */       
/* 1111 */       h = h.next();
/*      */     }
/* 1113 */     if (msg == null) {
/* 1114 */       if (t == null) {
/* 1115 */         msg = String.format("Unexpected end-of-input when binding data into %s", new Object[] { _calcName(instClass) });
/*      */       }
/*      */       else {
/* 1118 */         msg = String.format("Can not deserialize instance of %s out of %s token", new Object[] { _calcName(instClass), t });
/*      */       }
/*      */     }
/*      */     
/* 1122 */     reportMappingException(msg, new Object[0]);
/* 1123 */     return null;
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
/*      */   public JavaType handleUnknownTypeId(JavaType baseType, String id, TypeIdResolver idResolver, String extraDesc)
/*      */     throws IOException
/*      */   {
/* 1149 */     LinkedNode<DeserializationProblemHandler> h = this._config.getProblemHandlers();
/* 1150 */     while (h != null)
/*      */     {
/* 1152 */       JavaType type = ((DeserializationProblemHandler)h.value()).handleUnknownTypeId(this, baseType, id, idResolver, extraDesc);
/* 1153 */       if (type != null) {
/* 1154 */         if (type.hasRawClass(Void.class)) {
/* 1155 */           return null;
/*      */         }
/*      */         
/* 1158 */         if (type.isTypeOrSubTypeOf(baseType.getRawClass())) {
/* 1159 */           return type;
/*      */         }
/* 1161 */         throw unknownTypeIdException(baseType, id, "problem handler tried to resolve into non-subtype: " + type);
/*      */       }
/*      */       
/* 1164 */       h = h.next();
/*      */     }
/*      */     
/* 1167 */     if (!isEnabled(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)) {
/* 1168 */       return null;
/*      */     }
/* 1170 */     throw unknownTypeIdException(baseType, id, extraDesc);
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
/*      */   public void reportWrongTokenException(JsonParser p, JsonToken expToken, String msg, Object... msgArgs)
/*      */     throws JsonMappingException
/*      */   {
/* 1194 */     if ((msg != null) && (msgArgs.length > 0)) {
/* 1195 */       msg = String.format(msg, msgArgs);
/*      */     }
/* 1197 */     throw wrongTokenException(p, expToken, msg);
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
/*      */   public void reportUnknownProperty(Object instanceOrClass, String fieldName, JsonDeserializer<?> deser)
/*      */     throws JsonMappingException
/*      */   {
/* 1216 */     if (!isEnabled(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)) {
/* 1217 */       return;
/*      */     }
/*      */     
/* 1220 */     Collection<Object> propIds = deser == null ? null : deser.getKnownPropertyNames();
/* 1221 */     throw UnrecognizedPropertyException.from(this._parser, instanceOrClass, fieldName, propIds);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void reportMappingException(String msg, Object... msgArgs)
/*      */     throws JsonMappingException
/*      */   {
/* 1231 */     if (msgArgs.length > 0) {
/* 1232 */       msg = String.format(msg, msgArgs);
/*      */     }
/* 1234 */     throw JsonMappingException.from(getParser(), msg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void reportMissingContent(String msg, Object... msgArgs)
/*      */     throws JsonMappingException
/*      */   {
/* 1243 */     if (msg == null) {
/* 1244 */       msg = "No content to map due to end-of-input";
/* 1245 */     } else if (msgArgs.length > 0) {
/* 1246 */       msg = String.format(msg, msgArgs);
/*      */     }
/* 1248 */     throw JsonMappingException.from(getParser(), msg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void reportUnresolvedObjectId(ObjectIdReader oidReader, Object bean)
/*      */     throws JsonMappingException
/*      */   {
/* 1257 */     String msg = String.format("No Object Id found for an instance of %s, to assign to property '%s'", new Object[] { bean.getClass().getName(), oidReader.propertyName });
/*      */     
/* 1259 */     throw JsonMappingException.from(getParser(), msg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T reportBadTypeDefinition(BeanDescription bean, String message, Object... args)
/*      */     throws JsonMappingException
/*      */   {
/* 1271 */     if ((args != null) && (args.length > 0)) {
/* 1272 */       message = String.format(message, args);
/*      */     }
/* 1274 */     String beanDesc = bean == null ? "N/A" : _desc(bean.getType().getGenericSignature());
/* 1275 */     throw mappingException("Invalid type definition for type %s: %s", new Object[] { beanDesc, message });
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T reportBadPropertyDefinition(BeanDescription bean, BeanPropertyDefinition prop, String message, Object... args)
/*      */     throws JsonMappingException
/*      */   {
/* 1288 */     if ((args != null) && (args.length > 0)) {
/* 1289 */       message = String.format(message, args);
/*      */     }
/* 1291 */     String propName = prop == null ? "N/A" : _quotedString(prop.getName());
/* 1292 */     String beanDesc = bean == null ? "N/A" : _desc(bean.getType().getGenericSignature());
/* 1293 */     throw mappingException("Invalid definition for property %s (of type %s): %s", new Object[] { propName, beanDesc, message });
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
/*      */   public JsonMappingException mappingException(String message)
/*      */   {
/* 1313 */     return JsonMappingException.from(getParser(), message);
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
/*      */   public JsonMappingException mappingException(String msgTemplate, Object... args)
/*      */   {
/* 1326 */     if ((args != null) && (args.length > 0)) {
/* 1327 */       msgTemplate = String.format(msgTemplate, args);
/*      */     }
/* 1329 */     return JsonMappingException.from(getParser(), msgTemplate);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonMappingException mappingException(Class<?> targetClass)
/*      */   {
/* 1339 */     return mappingException(targetClass, this._parser.getCurrentToken());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonMappingException mappingException(Class<?> targetClass, JsonToken token)
/*      */   {
/* 1347 */     String tokenDesc = token == null ? "<end of input>" : String.format("%s token", new Object[] { token });
/* 1348 */     return JsonMappingException.from(this._parser, String.format("Can not deserialize instance of %s out of %s", new Object[] { _calcName(targetClass), tokenDesc }));
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
/*      */   public JsonMappingException wrongTokenException(JsonParser p, JsonToken expToken, String msg0)
/*      */   {
/* 1371 */     String msg = String.format("Unexpected token (%s), expected %s", new Object[] { p.getCurrentToken(), expToken });
/*      */     
/* 1373 */     if (msg0 != null) {
/* 1374 */       msg = msg + ": " + msg0;
/*      */     }
/* 1376 */     return JsonMappingException.from(p, msg);
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
/*      */   public JsonMappingException weirdKeyException(Class<?> keyClass, String keyValue, String msg)
/*      */   {
/* 1389 */     return InvalidFormatException.from(this._parser, String.format("Can not deserialize Map key of type %s from String %s: %s", new Object[] { keyClass.getName(), _quotedString(keyValue), msg }), keyValue, keyClass);
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
/*      */   public JsonMappingException weirdStringException(String value, Class<?> instClass, String msg)
/*      */   {
/* 1410 */     return InvalidFormatException.from(this._parser, String.format("Can not deserialize value of type %s from String %s: %s", new Object[] { instClass.getName(), _quotedString(value), msg }), value, instClass);
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
/*      */   public JsonMappingException weirdNumberException(Number value, Class<?> instClass, String msg)
/*      */   {
/* 1425 */     return InvalidFormatException.from(this._parser, String.format("Can not deserialize value of type %s from number %s: %s", new Object[] { instClass.getName(), String.valueOf(value), msg }), value, instClass);
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
/*      */   public JsonMappingException instantiationException(Class<?> instClass, Throwable t)
/*      */   {
/* 1441 */     return JsonMappingException.from(this._parser, String.format("Can not construct instance of %s, problem: %s", new Object[] { instClass.getName(), t.getMessage() }), t);
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
/*      */   public JsonMappingException instantiationException(Class<?> instClass, String msg)
/*      */   {
/* 1456 */     return JsonMappingException.from(this._parser, String.format("Can not construct instance of %s: %s", new Object[] { instClass.getName(), msg }));
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
/*      */   public JsonMappingException unknownTypeIdException(JavaType baseType, String typeId, String extraDesc)
/*      */   {
/* 1472 */     String msg = String.format("Could not resolve type id '%s' into a subtype of %s", new Object[] { typeId, baseType });
/*      */     
/* 1474 */     if (extraDesc != null) {
/* 1475 */       msg = msg + ": " + extraDesc;
/*      */     }
/* 1477 */     return InvalidTypeIdException.from(this._parser, msg, baseType, typeId);
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
/*      */   @Deprecated
/*      */   public JsonMappingException unknownTypeException(JavaType type, String id, String extraDesc)
/*      */   {
/* 1494 */     String msg = String.format("Could not resolve type id '%s' into a subtype of %s", new Object[] { id, type });
/*      */     
/* 1496 */     if (extraDesc != null) {
/* 1497 */       msg = msg + ": " + extraDesc;
/*      */     }
/* 1499 */     return JsonMappingException.from(this._parser, msg);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonMappingException endOfInputException(Class<?> instClass)
/*      */   {
/* 1510 */     return JsonMappingException.from(this._parser, "Unexpected end-of-input when trying to deserialize a " + instClass.getName());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DateFormat getDateFormat()
/*      */   {
/* 1522 */     if (this._dateFormat != null) {
/* 1523 */       return this._dateFormat;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1530 */     DateFormat df = this._config.getDateFormat();
/* 1531 */     this._dateFormat = (df = (DateFormat)df.clone());
/* 1532 */     return df;
/*      */   }
/*      */   
/*      */   protected String determineClassName(Object instance) {
/* 1536 */     return ClassUtil.getClassDescription(instance);
/*      */   }
/*      */   
/*      */   protected String _calcName(Class<?> cls) {
/* 1540 */     if (cls.isArray()) {
/* 1541 */       return _calcName(cls.getComponentType()) + "[]";
/*      */     }
/* 1543 */     return cls.getName();
/*      */   }
/*      */   
/*      */   protected String _valueDesc() {
/*      */     try {
/* 1548 */       return _desc(this._parser.getText());
/*      */     } catch (Exception e) {}
/* 1550 */     return "[N/A]";
/*      */   }
/*      */   
/*      */   protected String _desc(String desc)
/*      */   {
/* 1555 */     if (desc == null) {
/* 1556 */       return "[N/A]";
/*      */     }
/*      */     
/* 1559 */     if (desc.length() > 500) {
/* 1560 */       desc = desc.substring(0, 500) + "]...[" + desc.substring(desc.length() - 500);
/*      */     }
/* 1562 */     return desc;
/*      */   }
/*      */   
/*      */   protected String _quotedString(String desc)
/*      */   {
/* 1567 */     if (desc == null) {
/* 1568 */       return "[N/A]";
/*      */     }
/*      */     
/* 1571 */     if (desc.length() > 500) {
/* 1572 */       return String.format("\"%s]...[%s\"", new Object[] { desc.substring(0, 500), desc.substring(desc.length() - 500) });
/*      */     }
/*      */     
/*      */ 
/* 1576 */     return "\"" + desc + "\"";
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\DeserializationContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */