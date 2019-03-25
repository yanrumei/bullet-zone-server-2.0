/*      */ package com.fasterxml.jackson.databind;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
/*      */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*      */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*      */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*      */ import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
/*      */ import com.fasterxml.jackson.annotation.PropertyAccessor;
/*      */ import com.fasterxml.jackson.core.Base64Variant;
/*      */ import com.fasterxml.jackson.core.Base64Variants;
/*      */ import com.fasterxml.jackson.core.FormatSchema;
/*      */ import com.fasterxml.jackson.core.JsonEncoding;
/*      */ import com.fasterxml.jackson.core.JsonFactory;
/*      */ import com.fasterxml.jackson.core.JsonFactory.Feature;
/*      */ import com.fasterxml.jackson.core.JsonGenerationException;
/*      */ import com.fasterxml.jackson.core.JsonGenerator;
/*      */ import com.fasterxml.jackson.core.JsonGenerator.Feature;
/*      */ import com.fasterxml.jackson.core.JsonParseException;
/*      */ import com.fasterxml.jackson.core.JsonParser;
/*      */ import com.fasterxml.jackson.core.JsonParser.Feature;
/*      */ import com.fasterxml.jackson.core.JsonProcessingException;
/*      */ import com.fasterxml.jackson.core.JsonToken;
/*      */ import com.fasterxml.jackson.core.ObjectCodec;
/*      */ import com.fasterxml.jackson.core.PrettyPrinter;
/*      */ import com.fasterxml.jackson.core.TreeNode;
/*      */ import com.fasterxml.jackson.core.Version;
/*      */ import com.fasterxml.jackson.core.Versioned;
/*      */ import com.fasterxml.jackson.core.io.CharacterEscapes;
/*      */ import com.fasterxml.jackson.core.io.SegmentedStringWriter;
/*      */ import com.fasterxml.jackson.core.type.ResolvedType;
/*      */ import com.fasterxml.jackson.core.type.TypeReference;
/*      */ import com.fasterxml.jackson.core.util.ByteArrayBuilder;
/*      */ import com.fasterxml.jackson.databind.cfg.BaseSettings;
/*      */ import com.fasterxml.jackson.databind.cfg.ConfigOverrides;
/*      */ import com.fasterxml.jackson.databind.cfg.ContextAttributes;
/*      */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*      */ import com.fasterxml.jackson.databind.cfg.MutableConfigOverride;
/*      */ import com.fasterxml.jackson.databind.cfg.PackageVersion;
/*      */ import com.fasterxml.jackson.databind.deser.BeanDeserializerFactory;
/*      */ import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
/*      */ import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext;
/*      */ import com.fasterxml.jackson.databind.deser.DefaultDeserializationContext.Impl;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializationProblemHandler;
/*      */ import com.fasterxml.jackson.databind.deser.DeserializerFactory;
/*      */ import com.fasterxml.jackson.databind.deser.Deserializers;
/*      */ import com.fasterxml.jackson.databind.deser.KeyDeserializers;
/*      */ import com.fasterxml.jackson.databind.deser.ValueInstantiators;
/*      */ import com.fasterxml.jackson.databind.introspect.BasicClassIntrospector;
/*      */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector;
/*      */ import com.fasterxml.jackson.databind.introspect.ClassIntrospector.MixInResolver;
/*      */ import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.introspect.SimpleMixInResolver;
/*      */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*      */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker.Std;
/*      */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
/*      */ import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
/*      */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*      */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*      */ import com.fasterxml.jackson.databind.jsontype.impl.StdSubtypeResolver;
/*      */ import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
/*      */ import com.fasterxml.jackson.databind.node.ArrayNode;
/*      */ import com.fasterxml.jackson.databind.node.JsonNodeFactory;
/*      */ import com.fasterxml.jackson.databind.node.NullNode;
/*      */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*      */ import com.fasterxml.jackson.databind.node.POJONode;
/*      */ import com.fasterxml.jackson.databind.node.TreeTraversingParser;
/*      */ import com.fasterxml.jackson.databind.ser.BeanSerializerFactory;
/*      */ import com.fasterxml.jackson.databind.ser.BeanSerializerModifier;
/*      */ import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider;
/*      */ import com.fasterxml.jackson.databind.ser.DefaultSerializerProvider.Impl;
/*      */ import com.fasterxml.jackson.databind.ser.FilterProvider;
/*      */ import com.fasterxml.jackson.databind.ser.SerializerFactory;
/*      */ import com.fasterxml.jackson.databind.ser.Serializers;
/*      */ import com.fasterxml.jackson.databind.type.SimpleType;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.type.TypeModifier;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.RootNameLookup;
/*      */ import com.fasterxml.jackson.databind.util.StdDateFormat;
/*      */ import com.fasterxml.jackson.databind.util.TokenBuffer;
/*      */ import java.io.Closeable;
/*      */ import java.io.DataInput;
/*      */ import java.io.DataOutput;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Type;
/*      */ import java.net.URL;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.text.DateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.ServiceLoader;
/*      */ import java.util.Set;
/*      */ import java.util.TimeZone;
/*      */ import java.util.concurrent.ConcurrentHashMap;
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
/*      */ public class ObjectMapper
/*      */   extends ObjectCodec
/*      */   implements Versioned, Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*      */   
/*      */   public static enum DefaultTyping
/*      */   {
/*  150 */     JAVA_LANG_OBJECT, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  160 */     OBJECT_AND_NON_CONCRETE, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  169 */     NON_CONCRETE_AND_ARRAYS, 
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  180 */     NON_FINAL;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     private DefaultTyping() {}
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static class DefaultTypeResolverBuilder
/*      */     extends StdTypeResolverBuilder
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1L;
/*      */     
/*      */ 
/*      */ 
/*      */     protected final ObjectMapper.DefaultTyping _appliesFor;
/*      */     
/*      */ 
/*      */ 
/*      */     public DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping t)
/*      */     {
/*  205 */       this._appliesFor = t;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public TypeDeserializer buildTypeDeserializer(DeserializationConfig config, JavaType baseType, Collection<NamedType> subtypes)
/*      */     {
/*  212 */       return useForType(baseType) ? super.buildTypeDeserializer(config, baseType, subtypes) : null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     public TypeSerializer buildTypeSerializer(SerializationConfig config, JavaType baseType, Collection<NamedType> subtypes)
/*      */     {
/*  219 */       return useForType(baseType) ? super.buildTypeSerializer(config, baseType, subtypes) : null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     public boolean useForType(JavaType t)
/*      */     {
/*  234 */       if (t.isPrimitive()) {
/*  235 */         return false;
/*      */       }
/*      */       
/*  238 */       switch (ObjectMapper.3.$SwitchMap$com$fasterxml$jackson$databind$ObjectMapper$DefaultTyping[this._appliesFor.ordinal()]) {
/*      */       case 1: 
/*  240 */         while (t.isArrayType()) {
/*  241 */           t = t.getContentType();
/*      */         }
/*      */       
/*      */ 
/*      */       case 2: 
/*  246 */         while (t.isReferenceType()) {
/*  247 */           t = t.getReferencedType();
/*      */         }
/*  249 */         return (t.isJavaLangObject()) || ((!t.isConcrete()) && (!TreeNode.class.isAssignableFrom(t.getRawClass())));
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */       case 3: 
/*  255 */         while (t.isArrayType()) {
/*  256 */           t = t.getContentType();
/*      */         }
/*      */         
/*  259 */         while (t.isReferenceType()) {
/*  260 */           t = t.getReferencedType();
/*      */         }
/*      */         
/*  263 */         return (!t.isFinal()) && (!TreeNode.class.isAssignableFrom(t.getRawClass()));
/*      */       }
/*      */       
/*  266 */       return t.isJavaLangObject();
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
/*  280 */   private static final JavaType JSON_NODE_TYPE = SimpleType.constructUnsafe(JsonNode.class);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*  285 */   protected static final AnnotationIntrospector DEFAULT_ANNOTATION_INTROSPECTOR = new JacksonAnnotationIntrospector();
/*      */   
/*  287 */   protected static final VisibilityChecker<?> STD_VISIBILITY_CHECKER = VisibilityChecker.Std.defaultInstance();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  293 */   protected static final BaseSettings DEFAULT_BASE = new BaseSettings(null, DEFAULT_ANNOTATION_INTROSPECTOR, STD_VISIBILITY_CHECKER, null, TypeFactory.defaultInstance(), null, StdDateFormat.instance, null, Locale.getDefault(), null, Base64Variants.getDefaultVariant());
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JsonFactory _jsonFactory;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected TypeFactory _typeFactory;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected InjectableValues _injectableValues;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SubtypeResolver _subtypeResolver;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ConfigOverrides _propertyOverrides;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SimpleMixInResolver _mixIns;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SerializationConfig _serializationConfig;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DefaultSerializerProvider _serializerProvider;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected SerializerFactory _serializerFactory;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DeserializationConfig _deserializationConfig;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected DefaultDeserializationContext _deserializationContext;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Set<Object> _registeredModuleTypes;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  454 */   protected final ConcurrentHashMap<JavaType, JsonDeserializer<Object>> _rootDeserializers = new ConcurrentHashMap(64, 0.6F, 2);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper()
/*      */   {
/*  476 */     this(null, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper(JsonFactory jf)
/*      */   {
/*  485 */     this(jf, null, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectMapper(ObjectMapper src)
/*      */   {
/*  495 */     this._jsonFactory = src._jsonFactory.copy();
/*  496 */     this._jsonFactory.setCodec(this);
/*  497 */     this._subtypeResolver = src._subtypeResolver;
/*  498 */     this._typeFactory = src._typeFactory;
/*  499 */     this._injectableValues = src._injectableValues;
/*  500 */     this._propertyOverrides = src._propertyOverrides.copy();
/*  501 */     this._mixIns = src._mixIns.copy();
/*      */     
/*  503 */     RootNameLookup rootNames = new RootNameLookup();
/*  504 */     this._serializationConfig = new SerializationConfig(src._serializationConfig, this._mixIns, rootNames, this._propertyOverrides);
/*  505 */     this._deserializationConfig = new DeserializationConfig(src._deserializationConfig, this._mixIns, rootNames, this._propertyOverrides);
/*  506 */     this._serializerProvider = src._serializerProvider.copy();
/*  507 */     this._deserializationContext = src._deserializationContext.copy();
/*      */     
/*      */ 
/*  510 */     this._serializerFactory = src._serializerFactory;
/*      */     
/*      */ 
/*  513 */     Set<Object> reg = src._registeredModuleTypes;
/*  514 */     if (reg == null) {
/*  515 */       this._registeredModuleTypes = null;
/*      */     } else {
/*  517 */       this._registeredModuleTypes = new LinkedHashSet(reg);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper(JsonFactory jf, DefaultSerializerProvider sp, DefaultDeserializationContext dc)
/*      */   {
/*  542 */     if (jf == null) {
/*  543 */       this._jsonFactory = new MappingJsonFactory(this);
/*      */     } else {
/*  545 */       this._jsonFactory = jf;
/*  546 */       if (jf.getCodec() == null) {
/*  547 */         this._jsonFactory.setCodec(this);
/*      */       }
/*      */     }
/*  550 */     this._subtypeResolver = new StdSubtypeResolver();
/*  551 */     RootNameLookup rootNames = new RootNameLookup();
/*      */     
/*  553 */     this._typeFactory = TypeFactory.defaultInstance();
/*      */     
/*  555 */     SimpleMixInResolver mixins = new SimpleMixInResolver(null);
/*  556 */     this._mixIns = mixins;
/*  557 */     BaseSettings base = DEFAULT_BASE.withClassIntrospector(defaultClassIntrospector());
/*  558 */     ConfigOverrides propOverrides = new ConfigOverrides();
/*  559 */     this._propertyOverrides = propOverrides;
/*  560 */     this._serializationConfig = new SerializationConfig(base, this._subtypeResolver, mixins, rootNames, propOverrides);
/*      */     
/*  562 */     this._deserializationConfig = new DeserializationConfig(base, this._subtypeResolver, mixins, rootNames, propOverrides);
/*      */     
/*      */ 
/*      */ 
/*  566 */     boolean needOrder = this._jsonFactory.requiresPropertyOrdering();
/*  567 */     if ((needOrder ^ this._serializationConfig.isEnabled(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY))) {
/*  568 */       configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, needOrder);
/*      */     }
/*      */     
/*  571 */     this._serializerProvider = (sp == null ? new DefaultSerializerProvider.Impl() : sp);
/*  572 */     this._deserializationContext = (dc == null ? new DefaultDeserializationContext.Impl(BeanDeserializerFactory.instance) : dc);
/*      */     
/*      */ 
/*      */ 
/*  576 */     this._serializerFactory = BeanSerializerFactory.instance;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ClassIntrospector defaultClassIntrospector()
/*      */   {
/*  586 */     return new BasicClassIntrospector();
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
/*      */   public ObjectMapper copy()
/*      */   {
/*  611 */     _checkInvalidCopy(ObjectMapper.class);
/*  612 */     return new ObjectMapper(this);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _checkInvalidCopy(Class<?> exp)
/*      */   {
/*  620 */     if (getClass() != exp) {
/*  621 */       throw new IllegalStateException("Failed copy(): " + getClass().getName() + " (version: " + version() + ") does not override copy(); it has to");
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
/*      */ 
/*      */ 
/*      */   protected ObjectReader _newReader(DeserializationConfig config)
/*      */   {
/*  640 */     return new ObjectReader(this, config);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectReader _newReader(DeserializationConfig config, JavaType valueType, Object valueToUpdate, FormatSchema schema, InjectableValues injectableValues)
/*      */   {
/*  652 */     return new ObjectReader(this, config, valueType, valueToUpdate, schema, injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter _newWriter(SerializationConfig config)
/*      */   {
/*  662 */     return new ObjectWriter(this, config);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter _newWriter(SerializationConfig config, FormatSchema schema)
/*      */   {
/*  672 */     return new ObjectWriter(this, config, schema);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ObjectWriter _newWriter(SerializationConfig config, JavaType rootType, PrettyPrinter pp)
/*      */   {
/*  683 */     return new ObjectWriter(this, config, rootType, pp);
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
/*      */   public Version version()
/*      */   {
/*  698 */     return PackageVersion.VERSION;
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
/*      */   public ObjectMapper registerModule(Module module)
/*      */   {
/*  716 */     if (isEnabled(MapperFeature.IGNORE_DUPLICATE_MODULE_REGISTRATIONS)) {
/*  717 */       Object typeId = module.getTypeId();
/*  718 */       if (typeId != null) {
/*  719 */         if (this._registeredModuleTypes == null)
/*      */         {
/*      */ 
/*  722 */           this._registeredModuleTypes = new LinkedHashSet();
/*      */         }
/*      */         
/*  725 */         if (!this._registeredModuleTypes.add(typeId)) {
/*  726 */           return this;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  735 */     String name = module.getModuleName();
/*  736 */     if (name == null) {
/*  737 */       throw new IllegalArgumentException("Module without defined name");
/*      */     }
/*  739 */     Version version = module.version();
/*  740 */     if (version == null) {
/*  741 */       throw new IllegalArgumentException("Module without defined version");
/*      */     }
/*      */     
/*  744 */     final ObjectMapper mapper = this;
/*      */     
/*      */ 
/*  747 */     module.setupModule(new Module.SetupContext()
/*      */     {
/*      */ 
/*      */       public Version getMapperVersion()
/*      */       {
/*      */ 
/*  753 */         return ObjectMapper.this.version();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       public <C extends ObjectCodec> C getOwner()
/*      */       {
/*  760 */         return mapper;
/*      */       }
/*      */       
/*      */       public TypeFactory getTypeFactory()
/*      */       {
/*  765 */         return ObjectMapper.this._typeFactory;
/*      */       }
/*      */       
/*      */       public boolean isEnabled(MapperFeature f)
/*      */       {
/*  770 */         return mapper.isEnabled(f);
/*      */       }
/*      */       
/*      */       public boolean isEnabled(DeserializationFeature f)
/*      */       {
/*  775 */         return mapper.isEnabled(f);
/*      */       }
/*      */       
/*      */       public boolean isEnabled(SerializationFeature f)
/*      */       {
/*  780 */         return mapper.isEnabled(f);
/*      */       }
/*      */       
/*      */       public boolean isEnabled(JsonFactory.Feature f)
/*      */       {
/*  785 */         return mapper.isEnabled(f);
/*      */       }
/*      */       
/*      */       public boolean isEnabled(JsonParser.Feature f)
/*      */       {
/*  790 */         return mapper.isEnabled(f);
/*      */       }
/*      */       
/*      */       public boolean isEnabled(JsonGenerator.Feature f)
/*      */       {
/*  795 */         return mapper.isEnabled(f);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       public MutableConfigOverride configOverride(Class<?> type)
/*      */       {
/*  802 */         return mapper.configOverride(type);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       public void addDeserializers(Deserializers d)
/*      */       {
/*  809 */         DeserializerFactory df = mapper._deserializationContext._factory.withAdditionalDeserializers(d);
/*  810 */         mapper._deserializationContext = mapper._deserializationContext.with(df);
/*      */       }
/*      */       
/*      */       public void addKeyDeserializers(KeyDeserializers d)
/*      */       {
/*  815 */         DeserializerFactory df = mapper._deserializationContext._factory.withAdditionalKeyDeserializers(d);
/*  816 */         mapper._deserializationContext = mapper._deserializationContext.with(df);
/*      */       }
/*      */       
/*      */       public void addBeanDeserializerModifier(BeanDeserializerModifier modifier)
/*      */       {
/*  821 */         DeserializerFactory df = mapper._deserializationContext._factory.withDeserializerModifier(modifier);
/*  822 */         mapper._deserializationContext = mapper._deserializationContext.with(df);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       public void addSerializers(Serializers s)
/*      */       {
/*  829 */         mapper._serializerFactory = mapper._serializerFactory.withAdditionalSerializers(s);
/*      */       }
/*      */       
/*      */       public void addKeySerializers(Serializers s)
/*      */       {
/*  834 */         mapper._serializerFactory = mapper._serializerFactory.withAdditionalKeySerializers(s);
/*      */       }
/*      */       
/*      */       public void addBeanSerializerModifier(BeanSerializerModifier modifier)
/*      */       {
/*  839 */         mapper._serializerFactory = mapper._serializerFactory.withSerializerModifier(modifier);
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */       public void addAbstractTypeResolver(AbstractTypeResolver resolver)
/*      */       {
/*  846 */         DeserializerFactory df = mapper._deserializationContext._factory.withAbstractTypeResolver(resolver);
/*  847 */         mapper._deserializationContext = mapper._deserializationContext.with(df);
/*      */       }
/*      */       
/*      */       public void addTypeModifier(TypeModifier modifier)
/*      */       {
/*  852 */         TypeFactory f = mapper._typeFactory;
/*  853 */         f = f.withModifier(modifier);
/*  854 */         mapper.setTypeFactory(f);
/*      */       }
/*      */       
/*      */       public void addValueInstantiators(ValueInstantiators instantiators)
/*      */       {
/*  859 */         DeserializerFactory df = mapper._deserializationContext._factory.withValueInstantiators(instantiators);
/*  860 */         mapper._deserializationContext = mapper._deserializationContext.with(df);
/*      */       }
/*      */       
/*      */       public void setClassIntrospector(ClassIntrospector ci)
/*      */       {
/*  865 */         mapper._deserializationConfig = mapper._deserializationConfig.with(ci);
/*  866 */         mapper._serializationConfig = mapper._serializationConfig.with(ci);
/*      */       }
/*      */       
/*      */       public void insertAnnotationIntrospector(AnnotationIntrospector ai)
/*      */       {
/*  871 */         mapper._deserializationConfig = mapper._deserializationConfig.withInsertedAnnotationIntrospector(ai);
/*  872 */         mapper._serializationConfig = mapper._serializationConfig.withInsertedAnnotationIntrospector(ai);
/*      */       }
/*      */       
/*      */       public void appendAnnotationIntrospector(AnnotationIntrospector ai)
/*      */       {
/*  877 */         mapper._deserializationConfig = mapper._deserializationConfig.withAppendedAnnotationIntrospector(ai);
/*  878 */         mapper._serializationConfig = mapper._serializationConfig.withAppendedAnnotationIntrospector(ai);
/*      */       }
/*      */       
/*      */       public void registerSubtypes(Class<?>... subtypes)
/*      */       {
/*  883 */         mapper.registerSubtypes(subtypes);
/*      */       }
/*      */       
/*      */       public void registerSubtypes(NamedType... subtypes)
/*      */       {
/*  888 */         mapper.registerSubtypes(subtypes);
/*      */       }
/*      */       
/*      */       public void setMixInAnnotations(Class<?> target, Class<?> mixinSource)
/*      */       {
/*  893 */         mapper.addMixIn(target, mixinSource);
/*      */       }
/*      */       
/*      */       public void addDeserializationProblemHandler(DeserializationProblemHandler handler)
/*      */       {
/*  898 */         mapper.addHandler(handler);
/*      */       }
/*      */       
/*      */       public void setNamingStrategy(PropertyNamingStrategy naming)
/*      */       {
/*  903 */         mapper.setPropertyNamingStrategy(naming);
/*      */       }
/*  905 */     });
/*  906 */     return this;
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
/*      */   public ObjectMapper registerModules(Module... modules)
/*      */   {
/*  922 */     for (Module module : modules) {
/*  923 */       registerModule(module);
/*      */     }
/*  925 */     return this;
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
/*      */   public ObjectMapper registerModules(Iterable<Module> modules)
/*      */   {
/*  941 */     for (Module module : modules) {
/*  942 */       registerModule(module);
/*      */     }
/*  944 */     return this;
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
/*      */   public static List<Module> findModules()
/*      */   {
/*  957 */     return findModules(null);
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
/*      */   public static List<Module> findModules(ClassLoader classLoader)
/*      */   {
/*  971 */     ArrayList<Module> modules = new ArrayList();
/*  972 */     ServiceLoader<Module> loader = secureGetServiceLoader(Module.class, classLoader);
/*  973 */     for (Module module : loader) {
/*  974 */       modules.add(module);
/*      */     }
/*  976 */     return modules;
/*      */   }
/*      */   
/*      */   private static <T> ServiceLoader<T> secureGetServiceLoader(final Class<T> clazz, ClassLoader classLoader) {
/*  980 */     SecurityManager sm = System.getSecurityManager();
/*  981 */     if (sm == null) {
/*  982 */       return classLoader == null ? ServiceLoader.load(clazz) : ServiceLoader.load(clazz, classLoader);
/*      */     }
/*      */     
/*  985 */     (ServiceLoader)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public ServiceLoader<T> run() {
/*  988 */         return this.val$classLoader == null ? ServiceLoader.load(clazz) : ServiceLoader.load(clazz, this.val$classLoader);
/*      */       }
/*      */     });
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
/*      */   public ObjectMapper findAndRegisterModules()
/*      */   {
/* 1007 */     return registerModules(findModules());
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
/*      */   public SerializationConfig getSerializationConfig()
/*      */   {
/* 1025 */     return this._serializationConfig;
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
/*      */   public DeserializationConfig getDeserializationConfig()
/*      */   {
/* 1038 */     return this._deserializationConfig;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DeserializationContext getDeserializationContext()
/*      */   {
/* 1049 */     return this._deserializationContext;
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
/*      */   public ObjectMapper setSerializerFactory(SerializerFactory f)
/*      */   {
/* 1063 */     this._serializerFactory = f;
/* 1064 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SerializerFactory getSerializerFactory()
/*      */   {
/* 1075 */     return this._serializerFactory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setSerializerProvider(DefaultSerializerProvider p)
/*      */   {
/* 1084 */     this._serializerProvider = p;
/* 1085 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SerializerProvider getSerializerProvider()
/*      */   {
/* 1096 */     return this._serializerProvider;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public SerializerProvider getSerializerProviderInstance()
/*      */   {
/* 1108 */     return _serializerProvider(this._serializationConfig);
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
/*      */   public ObjectMapper setMixIns(Map<Class<?>, Class<?>> sourceMixins)
/*      */   {
/* 1137 */     this._mixIns.setLocalDefinitions(sourceMixins);
/* 1138 */     return this;
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
/*      */   public ObjectMapper addMixIn(Class<?> target, Class<?> mixinSource)
/*      */   {
/* 1155 */     this._mixIns.addLocalDefinition(target, mixinSource);
/* 1156 */     return this;
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
/*      */   public ObjectMapper setMixInResolver(ClassIntrospector.MixInResolver resolver)
/*      */   {
/* 1169 */     SimpleMixInResolver r = this._mixIns.withOverrides(resolver);
/* 1170 */     if (r != this._mixIns) {
/* 1171 */       this._mixIns = r;
/* 1172 */       this._deserializationConfig = new DeserializationConfig(this._deserializationConfig, r);
/* 1173 */       this._serializationConfig = new SerializationConfig(this._serializationConfig, r);
/*      */     }
/* 1175 */     return this;
/*      */   }
/*      */   
/*      */   public Class<?> findMixInClassFor(Class<?> cls) {
/* 1179 */     return this._mixIns.findMixInClassFor(cls);
/*      */   }
/*      */   
/*      */   public int mixInCount()
/*      */   {
/* 1184 */     return this._mixIns.localSize();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public void setMixInAnnotations(Map<Class<?>, Class<?>> sourceMixins)
/*      */   {
/* 1192 */     setMixIns(sourceMixins);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public final void addMixInAnnotations(Class<?> target, Class<?> mixinSource)
/*      */   {
/* 1200 */     addMixIn(target, mixinSource);
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
/*      */   public VisibilityChecker<?> getVisibilityChecker()
/*      */   {
/* 1215 */     return this._serializationConfig.getDefaultVisibilityChecker();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public void setVisibilityChecker(VisibilityChecker<?> vc)
/*      */   {
/* 1223 */     setVisibility(vc);
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
/*      */   public ObjectMapper setVisibility(VisibilityChecker<?> vc)
/*      */   {
/* 1236 */     this._deserializationConfig = this._deserializationConfig.with(vc);
/* 1237 */     this._serializationConfig = this._serializationConfig.with(vc);
/* 1238 */     return this;
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
/*      */   public ObjectMapper setVisibility(PropertyAccessor forMethod, JsonAutoDetect.Visibility visibility)
/*      */   {
/* 1267 */     this._deserializationConfig = this._deserializationConfig.withVisibility(forMethod, visibility);
/* 1268 */     this._serializationConfig = this._serializationConfig.withVisibility(forMethod, visibility);
/* 1269 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public SubtypeResolver getSubtypeResolver()
/*      */   {
/* 1276 */     return this._subtypeResolver;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectMapper setSubtypeResolver(SubtypeResolver str)
/*      */   {
/* 1283 */     this._subtypeResolver = str;
/* 1284 */     this._deserializationConfig = this._deserializationConfig.with(str);
/* 1285 */     this._serializationConfig = this._serializationConfig.with(str);
/* 1286 */     return this;
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
/*      */   public ObjectMapper setAnnotationIntrospector(AnnotationIntrospector ai)
/*      */   {
/* 1300 */     this._serializationConfig = this._serializationConfig.with(ai);
/* 1301 */     this._deserializationConfig = this._deserializationConfig.with(ai);
/* 1302 */     return this;
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
/*      */   public ObjectMapper setAnnotationIntrospectors(AnnotationIntrospector serializerAI, AnnotationIntrospector deserializerAI)
/*      */   {
/* 1322 */     this._serializationConfig = this._serializationConfig.with(serializerAI);
/* 1323 */     this._deserializationConfig = this._deserializationConfig.with(deserializerAI);
/* 1324 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public ObjectMapper setPropertyNamingStrategy(PropertyNamingStrategy s)
/*      */   {
/* 1331 */     this._serializationConfig = this._serializationConfig.with(s);
/* 1332 */     this._deserializationConfig = this._deserializationConfig.with(s);
/* 1333 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public PropertyNamingStrategy getPropertyNamingStrategy()
/*      */   {
/* 1341 */     return this._serializationConfig.getPropertyNamingStrategy();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setSerializationInclusion(JsonInclude.Include incl)
/*      */   {
/* 1351 */     setPropertyInclusion(JsonInclude.Value.construct(incl, JsonInclude.Include.USE_DEFAULTS));
/* 1352 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setPropertyInclusion(JsonInclude.Value incl)
/*      */   {
/* 1361 */     this._serializationConfig = this._serializationConfig.withPropertyInclusion(incl);
/* 1362 */     return this;
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
/*      */   public ObjectMapper setDefaultPrettyPrinter(PrettyPrinter pp)
/*      */   {
/* 1376 */     this._serializationConfig = this._serializationConfig.withDefaultPrettyPrinter(pp);
/* 1377 */     return this;
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
/*      */   public ObjectMapper enableDefaultTyping()
/*      */   {
/* 1393 */     return enableDefaultTyping(DefaultTyping.OBJECT_AND_NON_CONCRETE);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper enableDefaultTyping(DefaultTyping dti)
/*      */   {
/* 1403 */     return enableDefaultTyping(dti, JsonTypeInfo.As.WRAPPER_ARRAY);
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
/*      */   public ObjectMapper enableDefaultTyping(DefaultTyping applicability, JsonTypeInfo.As includeAs)
/*      */   {
/* 1423 */     if (includeAs == JsonTypeInfo.As.EXTERNAL_PROPERTY) {
/* 1424 */       throw new IllegalArgumentException("Can not use includeAs of " + includeAs);
/*      */     }
/*      */     
/* 1427 */     TypeResolverBuilder<?> typer = new DefaultTypeResolverBuilder(applicability);
/*      */     
/* 1429 */     typer = typer.init(JsonTypeInfo.Id.CLASS, null);
/* 1430 */     typer = typer.inclusion(includeAs);
/* 1431 */     return setDefaultTyping(typer);
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
/*      */   public ObjectMapper enableDefaultTypingAsProperty(DefaultTyping applicability, String propertyName)
/*      */   {
/* 1444 */     TypeResolverBuilder<?> typer = new DefaultTypeResolverBuilder(applicability);
/*      */     
/* 1446 */     typer = typer.init(JsonTypeInfo.Id.CLASS, null);
/* 1447 */     typer = typer.inclusion(JsonTypeInfo.As.PROPERTY);
/* 1448 */     typer = typer.typeProperty(propertyName);
/* 1449 */     return setDefaultTyping(typer);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper disableDefaultTyping()
/*      */   {
/* 1459 */     return setDefaultTyping(null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setDefaultTyping(TypeResolverBuilder<?> typer)
/*      */   {
/* 1470 */     this._deserializationConfig = this._deserializationConfig.with(typer);
/* 1471 */     this._serializationConfig = this._serializationConfig.with(typer);
/* 1472 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void registerSubtypes(Class<?>... classes)
/*      */   {
/* 1483 */     getSubtypeResolver().registerSubtypes(classes);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void registerSubtypes(NamedType... types)
/*      */   {
/* 1495 */     getSubtypeResolver().registerSubtypes(types);
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
/*      */   public MutableConfigOverride configOverride(Class<?> type)
/*      */   {
/* 1522 */     return this._propertyOverrides.findOrCreateOverride(type);
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
/*      */   public TypeFactory getTypeFactory()
/*      */   {
/* 1535 */     return this._typeFactory;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setTypeFactory(TypeFactory f)
/*      */   {
/* 1547 */     this._typeFactory = f;
/* 1548 */     this._deserializationConfig = this._deserializationConfig.with(f);
/* 1549 */     this._serializationConfig = this._serializationConfig.with(f);
/* 1550 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType constructType(Type t)
/*      */   {
/* 1559 */     return this._typeFactory.constructType(t);
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
/*      */   public JsonNodeFactory getNodeFactory()
/*      */   {
/* 1579 */     return this._deserializationConfig.getNodeFactory();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setNodeFactory(JsonNodeFactory f)
/*      */   {
/* 1588 */     this._deserializationConfig = this._deserializationConfig.with(f);
/* 1589 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper addHandler(DeserializationProblemHandler h)
/*      */   {
/* 1597 */     this._deserializationConfig = this._deserializationConfig.withHandler(h);
/* 1598 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper clearProblemHandlers()
/*      */   {
/* 1606 */     this._deserializationConfig = this._deserializationConfig.withNoProblemHandlers();
/* 1607 */     return this;
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
/*      */   public ObjectMapper setConfig(DeserializationConfig config)
/*      */   {
/* 1625 */     this._deserializationConfig = config;
/* 1626 */     return this;
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
/*      */   public void setFilters(FilterProvider filterProvider)
/*      */   {
/* 1640 */     this._serializationConfig = this._serializationConfig.withFilters(filterProvider);
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
/*      */   public ObjectMapper setFilterProvider(FilterProvider filterProvider)
/*      */   {
/* 1655 */     this._serializationConfig = this._serializationConfig.withFilters(filterProvider);
/* 1656 */     return this;
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
/*      */   public ObjectMapper setBase64Variant(Base64Variant v)
/*      */   {
/* 1670 */     this._serializationConfig = this._serializationConfig.with(v);
/* 1671 */     this._deserializationConfig = this._deserializationConfig.with(v);
/* 1672 */     return this;
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
/*      */   public ObjectMapper setConfig(SerializationConfig config)
/*      */   {
/* 1690 */     this._serializationConfig = config;
/* 1691 */     return this;
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
/*      */   public JsonFactory getFactory()
/*      */   {
/* 1709 */     return this._jsonFactory;
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public JsonFactory getJsonFactory()
/*      */   {
/* 1716 */     return getFactory();
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
/*      */   public ObjectMapper setDateFormat(DateFormat dateFormat)
/*      */   {
/* 1730 */     this._deserializationConfig = this._deserializationConfig.with(dateFormat);
/* 1731 */     this._serializationConfig = this._serializationConfig.with(dateFormat);
/* 1732 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DateFormat getDateFormat()
/*      */   {
/* 1740 */     return this._serializationConfig.getDateFormat();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object setHandlerInstantiator(HandlerInstantiator hi)
/*      */   {
/* 1752 */     this._deserializationConfig = this._deserializationConfig.with(hi);
/* 1753 */     this._serializationConfig = this._serializationConfig.with(hi);
/* 1754 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setInjectableValues(InjectableValues injectableValues)
/*      */   {
/* 1762 */     this._injectableValues = injectableValues;
/* 1763 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public InjectableValues getInjectableValues()
/*      */   {
/* 1770 */     return this._injectableValues;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setLocale(Locale l)
/*      */   {
/* 1778 */     this._deserializationConfig = this._deserializationConfig.with(l);
/* 1779 */     this._serializationConfig = this._serializationConfig.with(l);
/* 1780 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper setTimeZone(TimeZone tz)
/*      */   {
/* 1788 */     this._deserializationConfig = this._deserializationConfig.with(tz);
/* 1789 */     this._serializationConfig = this._serializationConfig.with(tz);
/* 1790 */     return this;
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
/*      */   public boolean isEnabled(MapperFeature f)
/*      */   {
/* 1804 */     return this._serializationConfig.isEnabled(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper configure(MapperFeature f, boolean state)
/*      */   {
/* 1812 */     this._serializationConfig = (state ? this._serializationConfig.with(new MapperFeature[] { f }) : this._serializationConfig.without(new MapperFeature[] { f }));
/*      */     
/* 1814 */     this._deserializationConfig = (state ? this._deserializationConfig.with(new MapperFeature[] { f }) : this._deserializationConfig.without(new MapperFeature[] { f }));
/*      */     
/* 1816 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper enable(MapperFeature... f)
/*      */   {
/* 1824 */     this._deserializationConfig = this._deserializationConfig.with(f);
/* 1825 */     this._serializationConfig = this._serializationConfig.with(f);
/* 1826 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper disable(MapperFeature... f)
/*      */   {
/* 1834 */     this._deserializationConfig = this._deserializationConfig.without(f);
/* 1835 */     this._serializationConfig = this._serializationConfig.without(f);
/* 1836 */     return this;
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
/*      */   public boolean isEnabled(SerializationFeature f)
/*      */   {
/* 1850 */     return this._serializationConfig.isEnabled(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper configure(SerializationFeature f, boolean state)
/*      */   {
/* 1858 */     this._serializationConfig = (state ? this._serializationConfig.with(f) : this._serializationConfig.without(f));
/*      */     
/* 1860 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper enable(SerializationFeature f)
/*      */   {
/* 1868 */     this._serializationConfig = this._serializationConfig.with(f);
/* 1869 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper enable(SerializationFeature first, SerializationFeature... f)
/*      */   {
/* 1878 */     this._serializationConfig = this._serializationConfig.with(first, f);
/* 1879 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper disable(SerializationFeature f)
/*      */   {
/* 1887 */     this._serializationConfig = this._serializationConfig.without(f);
/* 1888 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper disable(SerializationFeature first, SerializationFeature... f)
/*      */   {
/* 1897 */     this._serializationConfig = this._serializationConfig.without(first, f);
/* 1898 */     return this;
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
/*      */   public boolean isEnabled(DeserializationFeature f)
/*      */   {
/* 1912 */     return this._deserializationConfig.isEnabled(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper configure(DeserializationFeature f, boolean state)
/*      */   {
/* 1920 */     this._deserializationConfig = (state ? this._deserializationConfig.with(f) : this._deserializationConfig.without(f));
/*      */     
/* 1922 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper enable(DeserializationFeature feature)
/*      */   {
/* 1930 */     this._deserializationConfig = this._deserializationConfig.with(feature);
/* 1931 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper enable(DeserializationFeature first, DeserializationFeature... f)
/*      */   {
/* 1940 */     this._deserializationConfig = this._deserializationConfig.with(first, f);
/* 1941 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper disable(DeserializationFeature feature)
/*      */   {
/* 1949 */     this._deserializationConfig = this._deserializationConfig.without(feature);
/* 1950 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper disable(DeserializationFeature first, DeserializationFeature... f)
/*      */   {
/* 1959 */     this._deserializationConfig = this._deserializationConfig.without(first, f);
/* 1960 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEnabled(JsonParser.Feature f)
/*      */   {
/* 1970 */     return this._deserializationConfig.isEnabled(f, this._jsonFactory);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper configure(JsonParser.Feature f, boolean state)
/*      */   {
/* 1981 */     this._jsonFactory.configure(f, state);
/* 1982 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper enable(JsonParser.Feature... features)
/*      */   {
/* 1994 */     for (JsonParser.Feature f : features) {
/* 1995 */       this._jsonFactory.enable(f);
/*      */     }
/* 1997 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper disable(JsonParser.Feature... features)
/*      */   {
/* 2009 */     for (JsonParser.Feature f : features) {
/* 2010 */       this._jsonFactory.disable(f);
/*      */     }
/* 2012 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isEnabled(JsonGenerator.Feature f)
/*      */   {
/* 2022 */     return this._serializationConfig.isEnabled(f, this._jsonFactory);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper configure(JsonGenerator.Feature f, boolean state)
/*      */   {
/* 2033 */     this._jsonFactory.configure(f, state);
/* 2034 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper enable(JsonGenerator.Feature... features)
/*      */   {
/* 2046 */     for (JsonGenerator.Feature f : features) {
/* 2047 */       this._jsonFactory.enable(f);
/*      */     }
/* 2049 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectMapper disable(JsonGenerator.Feature... features)
/*      */   {
/* 2061 */     for (JsonGenerator.Feature f : features) {
/* 2062 */       this._jsonFactory.disable(f);
/*      */     }
/* 2064 */     return this;
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
/*      */   public boolean isEnabled(JsonFactory.Feature f)
/*      */   {
/* 2080 */     return this._jsonFactory.isEnabled(f);
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
/*      */   public <T> T readValue(JsonParser p, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2115 */     return (T)_readValue(getDeserializationConfig(), p, this._typeFactory.constructType(valueType));
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
/*      */   public <T> T readValue(JsonParser p, TypeReference<?> valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2139 */     return (T)_readValue(getDeserializationConfig(), p, this._typeFactory.constructType(valueTypeRef));
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
/*      */   public final <T> T readValue(JsonParser p, ResolvedType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2162 */     return (T)_readValue(getDeserializationConfig(), p, (JavaType)valueType);
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
/*      */   public <T> T readValue(JsonParser p, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2181 */     return (T)_readValue(getDeserializationConfig(), p, valueType);
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
/*      */   public <T extends TreeNode> T readTree(JsonParser p)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2215 */     DeserializationConfig cfg = getDeserializationConfig();
/* 2216 */     JsonToken t = p.getCurrentToken();
/* 2217 */     if (t == null) {
/* 2218 */       t = p.nextToken();
/* 2219 */       if (t == null) {
/* 2220 */         return null;
/*      */       }
/*      */     }
/* 2223 */     JsonNode n = (JsonNode)_readValue(cfg, p, JSON_NODE_TYPE);
/* 2224 */     if (n == null) {
/* 2225 */       n = getNodeFactory().nullNode();
/*      */     }
/*      */     
/* 2228 */     T result = n;
/* 2229 */     return result;
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
/*      */   public <T> MappingIterator<T> readValues(JsonParser p, ResolvedType valueType)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2254 */     return readValues(p, (JavaType)valueType);
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
/*      */   public <T> MappingIterator<T> readValues(JsonParser p, JavaType valueType)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2268 */     DeserializationConfig config = getDeserializationConfig();
/* 2269 */     DeserializationContext ctxt = createDeserializationContext(p, config);
/* 2270 */     JsonDeserializer<?> deser = _findRootDeserializer(ctxt, valueType);
/*      */     
/* 2272 */     return new MappingIterator(valueType, p, ctxt, deser, false, null);
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
/*      */   public <T> MappingIterator<T> readValues(JsonParser p, Class<T> valueType)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2288 */     return readValues(p, this._typeFactory.constructType(valueType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> MappingIterator<T> readValues(JsonParser p, TypeReference<?> valueTypeRef)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2298 */     return readValues(p, this._typeFactory.constructType(valueTypeRef));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonNode readTree(InputStream in)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2337 */     JsonNode n = (JsonNode)_readMapAndClose(this._jsonFactory.createParser(in), JSON_NODE_TYPE);
/* 2338 */     return n == null ? NullNode.instance : n;
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
/*      */   public JsonNode readTree(Reader r)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2367 */     JsonNode n = (JsonNode)_readMapAndClose(this._jsonFactory.createParser(r), JSON_NODE_TYPE);
/* 2368 */     return n == null ? NullNode.instance : n;
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
/*      */   public JsonNode readTree(String content)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2397 */     JsonNode n = (JsonNode)_readMapAndClose(this._jsonFactory.createParser(content), JSON_NODE_TYPE);
/* 2398 */     return n == null ? NullNode.instance : n;
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
/*      */   public JsonNode readTree(byte[] content)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2420 */     JsonNode n = (JsonNode)_readMapAndClose(this._jsonFactory.createParser(content), JSON_NODE_TYPE);
/* 2421 */     return n == null ? NullNode.instance : n;
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
/*      */   public JsonNode readTree(File file)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2447 */     JsonNode n = (JsonNode)_readMapAndClose(this._jsonFactory.createParser(file), JSON_NODE_TYPE);
/* 2448 */     return n == null ? NullNode.instance : n;
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
/*      */   public JsonNode readTree(URL source)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2474 */     JsonNode n = (JsonNode)_readMapAndClose(this._jsonFactory.createParser(source), JSON_NODE_TYPE);
/* 2475 */     return n == null ? NullNode.instance : n;
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
/*      */   public void writeValue(JsonGenerator g, Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 2493 */     SerializationConfig config = getSerializationConfig();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 2501 */     if ((config.isEnabled(SerializationFeature.INDENT_OUTPUT)) && 
/* 2502 */       (g.getPrettyPrinter() == null)) {
/* 2503 */       g.setPrettyPrinter(config.constructDefaultPrettyPrinter());
/*      */     }
/*      */     
/* 2506 */     if ((config.isEnabled(SerializationFeature.CLOSE_CLOSEABLE)) && ((value instanceof Closeable))) {
/* 2507 */       _writeCloseableValue(g, value, config);
/*      */     } else {
/* 2509 */       _serializerProvider(config).serializeValue(g, value);
/* 2510 */       if (config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/* 2511 */         g.flush();
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
/*      */   public void writeTree(JsonGenerator jgen, TreeNode rootNode)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2526 */     SerializationConfig config = getSerializationConfig();
/* 2527 */     _serializerProvider(config).serializeValue(jgen, rootNode);
/* 2528 */     if (config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/* 2529 */       jgen.flush();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeTree(JsonGenerator jgen, JsonNode rootNode)
/*      */     throws IOException, JsonProcessingException
/*      */   {
/* 2540 */     SerializationConfig config = getSerializationConfig();
/* 2541 */     _serializerProvider(config).serializeValue(jgen, rootNode);
/* 2542 */     if (config.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/* 2543 */       jgen.flush();
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
/*      */   public ObjectNode createObjectNode()
/*      */   {
/* 2556 */     return this._deserializationConfig.getNodeFactory().objectNode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ArrayNode createArrayNode()
/*      */   {
/* 2568 */     return this._deserializationConfig.getNodeFactory().arrayNode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonParser treeAsTokens(TreeNode n)
/*      */   {
/* 2579 */     return new TreeTraversingParser((JsonNode)n, this);
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
/*      */   public <T> T treeToValue(TreeNode n, Class<T> valueType)
/*      */     throws JsonProcessingException
/*      */   {
/*      */     try
/*      */     {
/* 2599 */       if ((valueType != Object.class) && (valueType.isAssignableFrom(n.getClass()))) {
/* 2600 */         return n;
/*      */       }
/*      */       
/*      */ 
/* 2604 */       if ((n.asToken() == JsonToken.VALUE_EMBEDDED_OBJECT) && 
/* 2605 */         ((n instanceof POJONode))) {
/* 2606 */         Object ob = ((POJONode)n).getPojo();
/* 2607 */         if ((ob == null) || (valueType.isInstance(ob))) {
/* 2608 */           return (T)ob;
/*      */         }
/*      */       }
/*      */       
/* 2612 */       return (T)readValue(treeAsTokens(n), valueType);
/*      */     } catch (JsonProcessingException e) {
/* 2614 */       throw e;
/*      */     } catch (IOException e) {
/* 2616 */       throw new IllegalArgumentException(e.getMessage(), e);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T extends JsonNode> T valueToTree(Object fromValue)
/*      */     throws IllegalArgumentException
/*      */   {
/* 2644 */     if (fromValue == null) return null;
/* 2645 */     TokenBuffer buf = new TokenBuffer(this, false);
/* 2646 */     if (isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 2647 */       buf = buf.forceUseOfBigDecimal(true);
/*      */     }
/*      */     JsonNode result;
/*      */     try {
/* 2651 */       writeValue(buf, fromValue);
/* 2652 */       JsonParser p = buf.asParser();
/* 2653 */       result = (JsonNode)readTree(p);
/* 2654 */       p.close();
/*      */     } catch (IOException e) {
/* 2656 */       throw new IllegalArgumentException(e.getMessage(), e);
/*      */     }
/* 2658 */     return result;
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
/*      */   public boolean canSerialize(Class<?> type)
/*      */   {
/* 2683 */     return _serializerProvider(getSerializationConfig()).hasSerializerFor(type, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean canSerialize(Class<?> type, AtomicReference<Throwable> cause)
/*      */   {
/* 2694 */     return _serializerProvider(getSerializationConfig()).hasSerializerFor(type, cause);
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
/*      */   public boolean canDeserialize(JavaType type)
/*      */   {
/* 2716 */     return createDeserializationContext(null, getDeserializationConfig()).hasValueDeserializerFor(type, null);
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
/*      */   public boolean canDeserialize(JavaType type, AtomicReference<Throwable> cause)
/*      */   {
/* 2729 */     return createDeserializationContext(null, getDeserializationConfig()).hasValueDeserializerFor(type, cause);
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
/*      */   public <T> T readValue(File src, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2756 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueType));
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
/*      */   public <T> T readValue(File src, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2775 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
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
/*      */   public <T> T readValue(File src, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2794 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
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
/*      */   public <T> T readValue(URL src, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2813 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueType));
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
/*      */   public <T> T readValue(URL src, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2832 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(URL src, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2839 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
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
/*      */   public <T> T readValue(String content, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2858 */     return (T)_readMapAndClose(this._jsonFactory.createParser(content), this._typeFactory.constructType(valueType));
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
/*      */   public <T> T readValue(String content, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2877 */     return (T)_readMapAndClose(this._jsonFactory.createParser(content), this._typeFactory.constructType(valueTypeRef));
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
/*      */   public <T> T readValue(String content, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2896 */     return (T)_readMapAndClose(this._jsonFactory.createParser(content), valueType);
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(Reader src, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2903 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueType));
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(Reader src, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2910 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(Reader src, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2917 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(InputStream src, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2924 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueType));
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(InputStream src, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2931 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(InputStream src, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2938 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(byte[] src, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2945 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T> T readValue(byte[] src, int offset, int len, Class<T> valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2953 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src, offset, len), this._typeFactory.constructType(valueType));
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(byte[] src, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2960 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueTypeRef));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T> T readValue(byte[] src, int offset, int len, TypeReference valueTypeRef)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2968 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src, offset, len), this._typeFactory.constructType(valueTypeRef));
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(byte[] src, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2975 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public <T> T readValue(byte[] src, int offset, int len, JavaType valueType)
/*      */     throws IOException, JsonParseException, JsonMappingException
/*      */   {
/* 2983 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src, offset, len), valueType);
/*      */   }
/*      */   
/*      */   public <T> T readValue(DataInput src, Class<T> valueType)
/*      */     throws IOException
/*      */   {
/* 2989 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), this._typeFactory.constructType(valueType));
/*      */   }
/*      */   
/*      */ 
/*      */   public <T> T readValue(DataInput src, JavaType valueType)
/*      */     throws IOException
/*      */   {
/* 2996 */     return (T)_readMapAndClose(this._jsonFactory.createParser(src), valueType);
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
/*      */   public void writeValue(File resultFile, Object value)
/*      */     throws IOException, JsonGenerationException, JsonMappingException
/*      */   {
/* 3013 */     _configAndWriteValue(this._jsonFactory.createGenerator(resultFile, JsonEncoding.UTF8), value);
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
/* 3030 */     _configAndWriteValue(this._jsonFactory.createGenerator(out, JsonEncoding.UTF8), value);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void writeValue(DataOutput out, Object value)
/*      */     throws IOException
/*      */   {
/* 3039 */     _configAndWriteValue(this._jsonFactory.createGenerator(out, JsonEncoding.UTF8), value);
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
/* 3055 */     _configAndWriteValue(this._jsonFactory.createGenerator(w), value);
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
/* 3071 */     SegmentedStringWriter sw = new SegmentedStringWriter(this._jsonFactory._getBufferRecycler());
/*      */     try {
/* 3073 */       _configAndWriteValue(this._jsonFactory.createGenerator(sw), value);
/*      */     } catch (JsonProcessingException e) {
/* 3075 */       throw e;
/*      */     } catch (IOException e) {
/* 3077 */       throw JsonMappingException.fromUnexpectedIOE(e);
/*      */     }
/* 3079 */     return sw.getAndClear();
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
/* 3095 */     ByteArrayBuilder bb = new ByteArrayBuilder(this._jsonFactory._getBufferRecycler());
/*      */     try {
/* 3097 */       _configAndWriteValue(this._jsonFactory.createGenerator(bb, JsonEncoding.UTF8), value);
/*      */     } catch (JsonProcessingException e) {
/* 3099 */       throw e;
/*      */     } catch (IOException e) {
/* 3101 */       throw JsonMappingException.fromUnexpectedIOE(e);
/*      */     }
/* 3103 */     byte[] result = bb.toByteArray();
/* 3104 */     bb.release();
/* 3105 */     return result;
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
/*      */   public ObjectWriter writer()
/*      */   {
/* 3120 */     return _newWriter(getSerializationConfig());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter writer(SerializationFeature feature)
/*      */   {
/* 3129 */     return _newWriter(getSerializationConfig().with(feature));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter writer(SerializationFeature first, SerializationFeature... other)
/*      */   {
/* 3139 */     return _newWriter(getSerializationConfig().with(first, other));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter writer(DateFormat df)
/*      */   {
/* 3148 */     return _newWriter(getSerializationConfig().with(df));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter writerWithView(Class<?> serializationView)
/*      */   {
/* 3156 */     return _newWriter(getSerializationConfig().withView(serializationView));
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
/*      */   public ObjectWriter writerFor(Class<?> rootType)
/*      */   {
/* 3171 */     return _newWriter(getSerializationConfig(), rootType == null ? null : this._typeFactory.constructType(rootType), null);
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
/*      */   public ObjectWriter writerFor(TypeReference<?> rootType)
/*      */   {
/* 3188 */     return _newWriter(getSerializationConfig(), rootType == null ? null : this._typeFactory.constructType(rootType), null);
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
/*      */   public ObjectWriter writerFor(JavaType rootType)
/*      */   {
/* 3205 */     return _newWriter(getSerializationConfig(), rootType, null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter writer(PrettyPrinter pp)
/*      */   {
/* 3214 */     if (pp == null) {
/* 3215 */       pp = ObjectWriter.NULL_PRETTY_PRINTER;
/*      */     }
/* 3217 */     return _newWriter(getSerializationConfig(), null, pp);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter writerWithDefaultPrettyPrinter()
/*      */   {
/* 3225 */     SerializationConfig config = getSerializationConfig();
/* 3226 */     return _newWriter(config, null, config.getDefaultPrettyPrinter());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter writer(FilterProvider filterProvider)
/*      */   {
/* 3235 */     return _newWriter(getSerializationConfig().withFilters(filterProvider));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter writer(FormatSchema schema)
/*      */   {
/* 3246 */     _verifySchemaType(schema);
/* 3247 */     return _newWriter(getSerializationConfig(), schema);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter writer(Base64Variant defaultBase64)
/*      */   {
/* 3257 */     return _newWriter(getSerializationConfig().with(defaultBase64));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter writer(CharacterEscapes escapes)
/*      */   {
/* 3267 */     return _newWriter(getSerializationConfig()).with(escapes);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectWriter writer(ContextAttributes attrs)
/*      */   {
/* 3277 */     return _newWriter(getSerializationConfig().with(attrs));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectWriter writerWithType(Class<?> rootType)
/*      */   {
/* 3285 */     return _newWriter(getSerializationConfig(), rootType == null ? null : this._typeFactory.constructType(rootType), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectWriter writerWithType(TypeReference<?> rootType)
/*      */   {
/* 3296 */     return _newWriter(getSerializationConfig(), rootType == null ? null : this._typeFactory.constructType(rootType), null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectWriter writerWithType(JavaType rootType)
/*      */   {
/* 3307 */     return _newWriter(getSerializationConfig(), rootType, null);
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
/*      */   public ObjectReader reader()
/*      */   {
/* 3323 */     return _newReader(getDeserializationConfig()).with(this._injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader reader(DeserializationFeature feature)
/*      */   {
/* 3334 */     return _newReader(getDeserializationConfig().with(feature));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader reader(DeserializationFeature first, DeserializationFeature... other)
/*      */   {
/* 3346 */     return _newReader(getDeserializationConfig().with(first, other));
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
/*      */   public ObjectReader readerForUpdating(Object valueToUpdate)
/*      */   {
/* 3360 */     JavaType t = this._typeFactory.constructType(valueToUpdate.getClass());
/* 3361 */     return _newReader(getDeserializationConfig(), t, valueToUpdate, null, this._injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader readerFor(JavaType type)
/*      */   {
/* 3372 */     return _newReader(getDeserializationConfig(), type, null, null, this._injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader readerFor(Class<?> type)
/*      */   {
/* 3383 */     return _newReader(getDeserializationConfig(), this._typeFactory.constructType(type), null, null, this._injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader readerFor(TypeReference<?> type)
/*      */   {
/* 3394 */     return _newReader(getDeserializationConfig(), this._typeFactory.constructType(type), null, null, this._injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader reader(JsonNodeFactory f)
/*      */   {
/* 3403 */     return _newReader(getDeserializationConfig()).with(f);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader reader(FormatSchema schema)
/*      */   {
/* 3414 */     _verifySchemaType(schema);
/* 3415 */     return _newReader(getDeserializationConfig(), null, null, schema, this._injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader reader(InjectableValues injectableValues)
/*      */   {
/* 3426 */     return _newReader(getDeserializationConfig(), null, null, null, injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader readerWithView(Class<?> view)
/*      */   {
/* 3435 */     return _newReader(getDeserializationConfig().withView(view));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader reader(Base64Variant defaultBase64)
/*      */   {
/* 3445 */     return _newReader(getDeserializationConfig().with(defaultBase64));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectReader reader(ContextAttributes attrs)
/*      */   {
/* 3455 */     return _newReader(getDeserializationConfig().with(attrs));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectReader reader(JavaType type)
/*      */   {
/* 3463 */     return _newReader(getDeserializationConfig(), type, null, null, this._injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectReader reader(Class<?> type)
/*      */   {
/* 3472 */     return _newReader(getDeserializationConfig(), this._typeFactory.constructType(type), null, null, this._injectableValues);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public ObjectReader reader(TypeReference<?> type)
/*      */   {
/* 3481 */     return _newReader(getDeserializationConfig(), this._typeFactory.constructType(type), null, null, this._injectableValues);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T convertValue(Object fromValue, Class<T> toValueType)
/*      */     throws IllegalArgumentException
/*      */   {
/* 3523 */     if (fromValue == null) return null;
/* 3524 */     return (T)_convert(fromValue, this._typeFactory.constructType(toValueType));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T convertValue(Object fromValue, TypeReference<?> toValueTypeRef)
/*      */     throws IllegalArgumentException
/*      */   {
/* 3534 */     return (T)convertValue(fromValue, this._typeFactory.constructType(toValueTypeRef));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public <T> T convertValue(Object fromValue, JavaType toValueType)
/*      */     throws IllegalArgumentException
/*      */   {
/* 3545 */     if (fromValue == null) return null;
/* 3546 */     return (T)_convert(fromValue, toValueType);
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
/*      */   protected Object _convert(Object fromValue, JavaType toValueType)
/*      */     throws IllegalArgumentException
/*      */   {
/* 3566 */     Class<?> targetType = toValueType.getRawClass();
/* 3567 */     if ((targetType != Object.class) && (!toValueType.hasGenericTypes()) && (targetType.isAssignableFrom(fromValue.getClass())))
/*      */     {
/*      */ 
/* 3570 */       return fromValue;
/*      */     }
/*      */     
/*      */ 
/* 3574 */     TokenBuffer buf = new TokenBuffer(this, false);
/* 3575 */     if (isEnabled(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)) {
/* 3576 */       buf = buf.forceUseOfBigDecimal(true);
/*      */     }
/*      */     
/*      */     try
/*      */     {
/* 3581 */       SerializationConfig config = getSerializationConfig().without(SerializationFeature.WRAP_ROOT_VALUE);
/*      */       
/* 3583 */       _serializerProvider(config).serializeValue(buf, fromValue);
/*      */       
/*      */ 
/* 3586 */       JsonParser p = buf.asParser();
/*      */       
/*      */ 
/* 3589 */       DeserializationConfig deserConfig = getDeserializationConfig();
/* 3590 */       JsonToken t = _initForReading(p);
/* 3591 */       Object result; Object result; if (t == JsonToken.VALUE_NULL) {
/* 3592 */         DeserializationContext ctxt = createDeserializationContext(p, deserConfig);
/* 3593 */         result = _findRootDeserializer(ctxt, toValueType).getNullValue(ctxt); } else { Object result;
/* 3594 */         if ((t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 3595 */           result = null;
/*      */         } else {
/* 3597 */           DeserializationContext ctxt = createDeserializationContext(p, deserConfig);
/* 3598 */           JsonDeserializer<Object> deser = _findRootDeserializer(ctxt, toValueType);
/*      */           
/* 3600 */           result = deser.deserialize(p, ctxt);
/*      */         } }
/* 3602 */       p.close();
/* 3603 */       return result;
/*      */     } catch (IOException e) {
/* 3605 */       throw new IllegalArgumentException(e.getMessage(), e);
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public JsonSchema generateJsonSchema(Class<?> t)
/*      */     throws JsonMappingException
/*      */   {
/* 3628 */     return _serializerProvider(getSerializationConfig()).generateJsonSchema(t);
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
/*      */   public void acceptJsonFormatVisitor(Class<?> type, JsonFormatVisitorWrapper visitor)
/*      */     throws JsonMappingException
/*      */   {
/* 3645 */     acceptJsonFormatVisitor(this._typeFactory.constructType(type), visitor);
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
/*      */   public void acceptJsonFormatVisitor(JavaType type, JsonFormatVisitorWrapper visitor)
/*      */     throws JsonMappingException
/*      */   {
/* 3663 */     if (type == null) {
/* 3664 */       throw new IllegalArgumentException("type must be provided");
/*      */     }
/* 3666 */     _serializerProvider(getSerializationConfig()).acceptJsonFormatVisitor(type, visitor);
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
/*      */   protected DefaultSerializerProvider _serializerProvider(SerializationConfig config)
/*      */   {
/* 3680 */     return this._serializerProvider.createInstance(config, this._serializerFactory);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final void _configAndWriteValue(JsonGenerator g, Object value)
/*      */     throws IOException
/*      */   {
/* 3690 */     SerializationConfig cfg = getSerializationConfig();
/* 3691 */     cfg.initialize(g);
/* 3692 */     if ((cfg.isEnabled(SerializationFeature.CLOSE_CLOSEABLE)) && ((value instanceof Closeable))) {
/* 3693 */       _configAndWriteCloseable(g, value, cfg);
/* 3694 */       return;
/*      */     }
/*      */     try {
/* 3697 */       _serializerProvider(cfg).serializeValue(g, value);
/*      */     } catch (Exception e) {
/* 3699 */       ClassUtil.closeOnFailAndThrowAsIAE(g, e);
/* 3700 */       return;
/*      */     }
/* 3702 */     g.close();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _configAndWriteCloseable(JsonGenerator g, Object value, SerializationConfig cfg)
/*      */     throws IOException
/*      */   {
/* 3712 */     Closeable toClose = (Closeable)value;
/*      */     try {
/* 3714 */       _serializerProvider(cfg).serializeValue(g, value);
/* 3715 */       Closeable tmpToClose = toClose;
/* 3716 */       toClose = null;
/* 3717 */       tmpToClose.close();
/*      */     } catch (Exception e) {
/* 3719 */       ClassUtil.closeOnFailAndThrowAsIAE(g, toClose, e);
/* 3720 */       return;
/*      */     }
/* 3722 */     g.close();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private final void _writeCloseableValue(JsonGenerator g, Object value, SerializationConfig cfg)
/*      */     throws IOException
/*      */   {
/* 3732 */     Closeable toClose = (Closeable)value;
/*      */     try {
/* 3734 */       _serializerProvider(cfg).serializeValue(g, value);
/* 3735 */       if (cfg.isEnabled(SerializationFeature.FLUSH_AFTER_WRITE_VALUE)) {
/* 3736 */         g.flush();
/*      */       }
/*      */     } catch (Exception e) {
/* 3739 */       ClassUtil.closeOnFailAndThrowAsIAE(null, toClose, e);
/* 3740 */       return;
/*      */     }
/* 3742 */     toClose.close();
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
/*      */   protected DefaultDeserializationContext createDeserializationContext(JsonParser p, DeserializationConfig cfg)
/*      */   {
/* 3758 */     return this._deserializationContext.createInstance(cfg, p, this._injectableValues);
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
/*      */   protected Object _readValue(DeserializationConfig cfg, JsonParser p, JavaType valueType)
/*      */     throws IOException
/*      */   {
/* 3772 */     JsonToken t = _initForReading(p);
/* 3773 */     Object result; Object result; if (t == JsonToken.VALUE_NULL)
/*      */     {
/* 3775 */       DeserializationContext ctxt = createDeserializationContext(p, cfg);
/* 3776 */       result = _findRootDeserializer(ctxt, valueType).getNullValue(ctxt); } else { Object result;
/* 3777 */       if ((t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 3778 */         result = null;
/*      */       } else {
/* 3780 */         DeserializationContext ctxt = createDeserializationContext(p, cfg);
/* 3781 */         JsonDeserializer<Object> deser = _findRootDeserializer(ctxt, valueType);
/*      */         Object result;
/* 3783 */         if (cfg.useRootWrapping()) {
/* 3784 */           result = _unwrapAndDeserialize(p, ctxt, cfg, valueType, deser);
/*      */         } else {
/* 3786 */           result = deser.deserialize(p, ctxt);
/*      */         }
/*      */       }
/*      */     }
/* 3790 */     p.clearCurrentToken();
/* 3791 */     return result;
/*      */   }
/*      */   
/*      */   protected Object _readMapAndClose(JsonParser p0, JavaType valueType)
/*      */     throws IOException
/*      */   {
/* 3797 */     JsonParser p = p0;Throwable localThrowable2 = null;
/*      */     try {
/* 3799 */       JsonToken t = _initForReading(p);
/* 3800 */       Object result; DeserializationConfig cfg; Object result; if (t == JsonToken.VALUE_NULL)
/*      */       {
/* 3802 */         DeserializationContext ctxt = createDeserializationContext(p, getDeserializationConfig());
/*      */         
/* 3804 */         result = _findRootDeserializer(ctxt, valueType).getNullValue(ctxt); } else { Object result;
/* 3805 */         if ((t == JsonToken.END_ARRAY) || (t == JsonToken.END_OBJECT)) {
/* 3806 */           result = null;
/*      */         } else {
/* 3808 */           cfg = getDeserializationConfig();
/* 3809 */           DeserializationContext ctxt = createDeserializationContext(p, cfg);
/* 3810 */           JsonDeserializer<Object> deser = _findRootDeserializer(ctxt, valueType);
/* 3811 */           Object result; if (cfg.useRootWrapping()) {
/* 3812 */             result = _unwrapAndDeserialize(p, ctxt, cfg, valueType, deser);
/*      */           } else {
/* 3814 */             result = deser.deserialize(p, ctxt);
/*      */           }
/* 3816 */           ctxt.checkUnresolvedObjectId();
/*      */         }
/*      */       }
/* 3819 */       p.clearCurrentToken();
/* 3820 */       return (DeserializationConfig)result;
/*      */     }
/*      */     catch (Throwable localThrowable1)
/*      */     {
/* 3797 */       localThrowable2 = localThrowable1;throw localThrowable1;
/*      */ 
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
/* 3821 */       if (p != null) { if (localThrowable2 != null) try { p.close(); } catch (Throwable x2) { localThrowable2.addSuppressed(x2); } else { p.close();
/*      */         }
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
/*      */ 
/*      */   protected JsonToken _initForReading(JsonParser p)
/*      */     throws IOException
/*      */   {
/* 3841 */     this._deserializationConfig.initialize(p);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 3847 */     JsonToken t = p.getCurrentToken();
/* 3848 */     if (t == null)
/*      */     {
/* 3850 */       t = p.nextToken();
/* 3851 */       if (t == null)
/*      */       {
/*      */ 
/* 3854 */         throw JsonMappingException.from(p, "No content to map due to end-of-input");
/*      */       }
/*      */     }
/* 3857 */     return t;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected Object _unwrapAndDeserialize(JsonParser p, DeserializationContext ctxt, DeserializationConfig config, JavaType rootType, JsonDeserializer<Object> deser)
/*      */     throws IOException
/*      */   {
/* 3865 */     PropertyName expRootName = config.findRootName(rootType);
/*      */     
/* 3867 */     String expSimpleName = expRootName.getSimpleName();
/* 3868 */     if (p.getCurrentToken() != JsonToken.START_OBJECT) {
/* 3869 */       ctxt.reportWrongTokenException(p, JsonToken.START_OBJECT, "Current token not START_OBJECT (needed to unwrap root name '%s'), but %s", new Object[] { expSimpleName, p.getCurrentToken() });
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 3874 */     if (p.nextToken() != JsonToken.FIELD_NAME) {
/* 3875 */       ctxt.reportWrongTokenException(p, JsonToken.FIELD_NAME, "Current token not FIELD_NAME (to contain expected root name '" + expSimpleName + "'), but " + p.getCurrentToken(), new Object[0]);
/*      */     }
/*      */     
/*      */ 
/* 3879 */     String actualName = p.getCurrentName();
/* 3880 */     if (!expSimpleName.equals(actualName)) {
/* 3881 */       ctxt.reportMappingException("Root name '%s' does not match expected ('%s') for type %s", new Object[] { actualName, expSimpleName, rootType });
/*      */     }
/*      */     
/*      */ 
/* 3885 */     p.nextToken();
/* 3886 */     Object result = deser.deserialize(p, ctxt);
/*      */     
/* 3888 */     if (p.nextToken() != JsonToken.END_OBJECT) {
/* 3889 */       ctxt.reportWrongTokenException(p, JsonToken.END_OBJECT, "Current token not END_OBJECT (to match wrapper object with root name '%s'), but %s", new Object[] { expSimpleName, p.getCurrentToken() });
/*      */     }
/*      */     
/*      */ 
/* 3893 */     return result;
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
/*      */   protected JsonDeserializer<Object> _findRootDeserializer(DeserializationContext ctxt, JavaType valueType)
/*      */     throws JsonMappingException
/*      */   {
/* 3910 */     JsonDeserializer<Object> deser = (JsonDeserializer)this._rootDeserializers.get(valueType);
/* 3911 */     if (deser != null) {
/* 3912 */       return deser;
/*      */     }
/*      */     
/* 3915 */     deser = ctxt.findRootValueDeserializer(valueType);
/* 3916 */     if (deser == null) {
/* 3917 */       throw JsonMappingException.from(ctxt, "Can not find a deserializer for type " + valueType);
/*      */     }
/*      */     
/* 3920 */     this._rootDeserializers.put(valueType, deser);
/* 3921 */     return deser;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _verifySchemaType(FormatSchema schema)
/*      */   {
/* 3929 */     if ((schema != null) && 
/* 3930 */       (!this._jsonFactory.canUseSchema(schema))) {
/* 3931 */       throw new IllegalArgumentException("Can not use FormatSchema of type " + schema.getClass().getName() + " for format " + this._jsonFactory.getFormatName());
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ObjectMapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */