/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value;
/*     */ import com.fasterxml.jackson.databind.deser.impl.BeanPropertyMap;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdReader;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ObjectIdValueProperty;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ValueInjector;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class BeanDeserializerBuilder
/*     */ {
/*     */   protected final DeserializationConfig _config;
/*     */   protected final BeanDescription _beanDesc;
/*  49 */   protected final Map<String, SettableBeanProperty> _properties = new LinkedHashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<ValueInjector> _injectables;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HashMap<String, SettableBeanProperty> _backRefProperties;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HashSet<String> _ignorableProps;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ValueInstantiator _valueInstantiator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ObjectIdReader _objectIdReader;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableAnyProperty _anySetter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _ignoreAllUnknown;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AnnotatedMethod _buildMethod;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonPOJOBuilder.Value _builderConfig;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanDeserializerBuilder(BeanDescription beanDesc, DeserializationConfig config)
/*     */   {
/* 112 */     this._beanDesc = beanDesc;
/* 113 */     this._config = config;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanDeserializerBuilder(BeanDeserializerBuilder src)
/*     */   {
/* 122 */     this._beanDesc = src._beanDesc;
/* 123 */     this._config = src._config;
/*     */     
/*     */ 
/* 126 */     this._properties.putAll(src._properties);
/* 127 */     this._injectables = _copy(src._injectables);
/* 128 */     this._backRefProperties = _copy(src._backRefProperties);
/*     */     
/* 130 */     this._ignorableProps = src._ignorableProps;
/* 131 */     this._valueInstantiator = src._valueInstantiator;
/* 132 */     this._objectIdReader = src._objectIdReader;
/*     */     
/* 134 */     this._anySetter = src._anySetter;
/* 135 */     this._ignoreAllUnknown = src._ignoreAllUnknown;
/*     */     
/* 137 */     this._buildMethod = src._buildMethod;
/* 138 */     this._builderConfig = src._builderConfig;
/*     */   }
/*     */   
/*     */   private static HashMap<String, SettableBeanProperty> _copy(HashMap<String, SettableBeanProperty> src) {
/* 142 */     return src == null ? null : new HashMap(src);
/*     */   }
/*     */   
/*     */   private static <T> List<T> _copy(List<T> src)
/*     */   {
/* 147 */     return src == null ? null : new ArrayList(src);
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
/*     */   public void addOrReplaceProperty(SettableBeanProperty prop, boolean allowOverride)
/*     */   {
/* 160 */     this._properties.put(prop.getName(), prop);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addProperty(SettableBeanProperty prop)
/*     */   {
/* 170 */     SettableBeanProperty old = (SettableBeanProperty)this._properties.put(prop.getName(), prop);
/* 171 */     if ((old != null) && (old != prop)) {
/* 172 */       throw new IllegalArgumentException("Duplicate property '" + prop.getName() + "' for " + this._beanDesc.getType());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addBackReferenceProperty(String referenceName, SettableBeanProperty prop)
/*     */   {
/* 183 */     if (this._backRefProperties == null) {
/* 184 */       this._backRefProperties = new HashMap(4);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 189 */     prop.fixAccess(this._config);
/* 190 */     this._backRefProperties.put(referenceName, prop);
/*     */     
/* 192 */     if (this._properties != null) {
/* 193 */       this._properties.remove(prop.getName());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addInjectable(PropertyName propName, JavaType propType, Annotations contextAnnotations, AnnotatedMember member, Object valueId)
/*     */   {
/* 203 */     if (this._injectables == null) {
/* 204 */       this._injectables = new ArrayList();
/*     */     }
/* 206 */     boolean fixAccess = this._config.canOverrideAccessModifiers();
/* 207 */     boolean forceAccess = (fixAccess) && (this._config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/* 208 */     if (fixAccess) {
/* 209 */       member.fixAccess(forceAccess);
/*     */     }
/* 211 */     this._injectables.add(new ValueInjector(propName, propType, contextAnnotations, member, valueId));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addIgnorable(String propName)
/*     */   {
/* 221 */     if (this._ignorableProps == null) {
/* 222 */       this._ignorableProps = new HashSet();
/*     */     }
/* 224 */     this._ignorableProps.add(propName);
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
/*     */   public void addCreatorProperty(SettableBeanProperty prop)
/*     */   {
/* 239 */     addProperty(prop);
/*     */   }
/*     */   
/*     */   public void setAnySetter(SettableAnyProperty s)
/*     */   {
/* 244 */     if ((this._anySetter != null) && (s != null)) {
/* 245 */       throw new IllegalStateException("_anySetter already set to non-null");
/*     */     }
/* 247 */     this._anySetter = s;
/*     */   }
/*     */   
/*     */   public void setIgnoreUnknownProperties(boolean ignore) {
/* 251 */     this._ignoreAllUnknown = ignore;
/*     */   }
/*     */   
/*     */   public void setValueInstantiator(ValueInstantiator inst) {
/* 255 */     this._valueInstantiator = inst;
/*     */   }
/*     */   
/*     */   public void setObjectIdReader(ObjectIdReader r) {
/* 259 */     this._objectIdReader = r;
/*     */   }
/*     */   
/*     */   public void setPOJOBuilder(AnnotatedMethod buildMethod, JsonPOJOBuilder.Value config) {
/* 263 */     this._buildMethod = buildMethod;
/* 264 */     this._builderConfig = config;
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
/*     */   public Iterator<SettableBeanProperty> getProperties()
/*     */   {
/* 282 */     return this._properties.values().iterator();
/*     */   }
/*     */   
/*     */   public SettableBeanProperty findProperty(PropertyName propertyName) {
/* 286 */     return (SettableBeanProperty)this._properties.get(propertyName.getSimpleName());
/*     */   }
/*     */   
/*     */   public boolean hasProperty(PropertyName propertyName) {
/* 290 */     return findProperty(propertyName) != null;
/*     */   }
/*     */   
/*     */   public SettableBeanProperty removeProperty(PropertyName name) {
/* 294 */     return (SettableBeanProperty)this._properties.remove(name.getSimpleName());
/*     */   }
/*     */   
/*     */   public SettableAnyProperty getAnySetter() {
/* 298 */     return this._anySetter;
/*     */   }
/*     */   
/*     */   public ValueInstantiator getValueInstantiator() {
/* 302 */     return this._valueInstantiator;
/*     */   }
/*     */   
/*     */   public List<ValueInjector> getInjectables() {
/* 306 */     return this._injectables;
/*     */   }
/*     */   
/*     */   public ObjectIdReader getObjectIdReader() {
/* 310 */     return this._objectIdReader;
/*     */   }
/*     */   
/*     */   public AnnotatedMethod getBuildMethod() {
/* 314 */     return this._buildMethod;
/*     */   }
/*     */   
/*     */   public JsonPOJOBuilder.Value getBuilderConfig() {
/* 318 */     return this._builderConfig;
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
/*     */   public JsonDeserializer<?> build()
/*     */   {
/* 333 */     Collection<SettableBeanProperty> props = this._properties.values();
/* 334 */     _fixAccess(props);
/*     */     
/* 336 */     BeanPropertyMap propertyMap = BeanPropertyMap.construct(props, this._config.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES));
/*     */     
/* 338 */     propertyMap.assignIndexes();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 343 */     boolean anyViews = !this._config.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION);
/* 344 */     if (!anyViews) {
/* 345 */       for (SettableBeanProperty prop : props) {
/* 346 */         if (prop.hasViews()) {
/* 347 */           anyViews = true;
/* 348 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 354 */     if (this._objectIdReader != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 359 */       ObjectIdValueProperty prop = new ObjectIdValueProperty(this._objectIdReader, PropertyMetadata.STD_REQUIRED);
/* 360 */       propertyMap = propertyMap.withProperty(prop);
/*     */     }
/*     */     
/* 363 */     return new BeanDeserializer(this, this._beanDesc, propertyMap, this._backRefProperties, this._ignorableProps, this._ignoreAllUnknown, anyViews);
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
/*     */   public AbstractDeserializer buildAbstract()
/*     */   {
/* 376 */     return new AbstractDeserializer(this, this._beanDesc, this._backRefProperties);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> buildBuilderBased(JavaType valueType, String expBuildMethodName)
/*     */   {
/* 387 */     if (this._buildMethod == null)
/*     */     {
/* 389 */       if (!expBuildMethodName.isEmpty()) {
/* 390 */         throw new IllegalArgumentException(String.format("Builder class %s does not have build method (name: '%s')", new Object[] { this._beanDesc.getBeanClass().getName(), expBuildMethodName }));
/*     */       }
/*     */       
/*     */ 
/*     */     }
/*     */     else
/*     */     {
/* 397 */       Class<?> rawBuildType = this._buildMethod.getRawReturnType();
/* 398 */       Class<?> rawValueType = valueType.getRawClass();
/* 399 */       if ((rawBuildType != rawValueType) && (!rawBuildType.isAssignableFrom(rawValueType)) && (!rawValueType.isAssignableFrom(rawBuildType)))
/*     */       {
/*     */ 
/* 402 */         throw new IllegalArgumentException("Build method '" + this._buildMethod.getFullName() + " has bad return type (" + rawBuildType.getName() + "), not compatible with POJO type (" + valueType.getRawClass().getName() + ")");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 408 */     Collection<SettableBeanProperty> props = this._properties.values();
/* 409 */     _fixAccess(props);
/* 410 */     BeanPropertyMap propertyMap = BeanPropertyMap.construct(props, this._config.isEnabled(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES));
/*     */     
/* 412 */     propertyMap.assignIndexes();
/*     */     
/* 414 */     boolean anyViews = !this._config.isEnabled(MapperFeature.DEFAULT_VIEW_INCLUSION);
/*     */     
/* 416 */     if (!anyViews) {
/* 417 */       for (SettableBeanProperty prop : props) {
/* 418 */         if (prop.hasViews()) {
/* 419 */           anyViews = true;
/* 420 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 425 */     if (this._objectIdReader != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 430 */       ObjectIdValueProperty prop = new ObjectIdValueProperty(this._objectIdReader, PropertyMetadata.STD_REQUIRED);
/*     */       
/* 432 */       propertyMap = propertyMap.withProperty(prop);
/*     */     }
/*     */     
/* 435 */     return new BuilderBasedDeserializer(this, this._beanDesc, propertyMap, this._backRefProperties, this._ignorableProps, this._ignoreAllUnknown, anyViews);
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
/*     */   private void _fixAccess(Collection<SettableBeanProperty> mainProps)
/*     */   {
/* 460 */     for (SettableBeanProperty prop : mainProps)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 467 */       prop.fixAccess(this._config);
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
/* 478 */     if (this._anySetter != null) {
/* 479 */       this._anySetter.fixAccess(this._config);
/*     */     }
/* 481 */     if (this._buildMethod != null) {
/* 482 */       this._buildMethod.fixAccess(this._config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\BeanDeserializerBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */