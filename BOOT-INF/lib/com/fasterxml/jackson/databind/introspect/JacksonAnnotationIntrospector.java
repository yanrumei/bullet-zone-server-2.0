/*      */ package com.fasterxml.jackson.databind.introspect;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonBackReference;
/*      */ import com.fasterxml.jackson.annotation.JsonCreator;
/*      */ import com.fasterxml.jackson.annotation.JsonIdentityInfo;
/*      */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties.Value;
/*      */ import com.fasterxml.jackson.annotation.JsonInclude;
/*      */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*      */ import com.fasterxml.jackson.annotation.JsonManagedReference;
/*      */ import com.fasterxml.jackson.annotation.JsonProperty;
/*      */ import com.fasterxml.jackson.annotation.JsonPropertyOrder;
/*      */ import com.fasterxml.jackson.annotation.JsonRawValue;
/*      */ import com.fasterxml.jackson.annotation.JsonTypeInfo;
/*      */ import com.fasterxml.jackson.annotation.JsonUnwrapped;
/*      */ import com.fasterxml.jackson.annotation.JsonView;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*      */ import com.fasterxml.jackson.databind.PropertyName;
/*      */ import com.fasterxml.jackson.databind.annotation.JsonAppend;
/*      */ import com.fasterxml.jackson.databind.annotation.JsonAppend.Attr;
/*      */ import com.fasterxml.jackson.databind.annotation.JsonAppend.Prop;
/*      */ import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
/*      */ import com.fasterxml.jackson.databind.annotation.JsonSerialize;
/*      */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*      */ import com.fasterxml.jackson.databind.ext.Java7Support;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*      */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*      */ import com.fasterxml.jackson.databind.util.LRUMap;
/*      */ import java.lang.reflect.Field;
/*      */ import java.util.List;
/*      */ 
/*      */ public class JacksonAnnotationIntrospector extends com.fasterxml.jackson.databind.AnnotationIntrospector implements java.io.Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 1L;
/*   35 */   private static final Class<? extends java.lang.annotation.Annotation>[] ANNOTATIONS_TO_INFER_SER = (Class[])new Class[] { JsonSerialize.class, JsonView.class, com.fasterxml.jackson.annotation.JsonFormat.class, JsonTypeInfo.class, JsonRawValue.class, JsonUnwrapped.class, JsonBackReference.class, JsonManagedReference.class };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   48 */   private static final Class<? extends java.lang.annotation.Annotation>[] ANNOTATIONS_TO_INFER_DESER = (Class[])new Class[] { JsonDeserialize.class, JsonView.class, com.fasterxml.jackson.annotation.JsonFormat.class, JsonTypeInfo.class, JsonUnwrapped.class, JsonBackReference.class, JsonManagedReference.class };
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private static final Java7Support _java7Helper;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static
/*      */   {
/*   63 */     Java7Support x = null;
/*      */     try {
/*   65 */       x = Java7Support.instance();
/*      */     } catch (Throwable t) {}
/*   67 */     _java7Helper = x;
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
/*   79 */   protected transient LRUMap<Class<?>, Boolean> _annotationsInside = new LRUMap(48, 48);
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   95 */   protected boolean _cfgConstructorPropertiesImpliesCreator = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public com.fasterxml.jackson.core.Version version()
/*      */   {
/*  107 */     return com.fasterxml.jackson.databind.cfg.PackageVersion.VERSION;
/*      */   }
/*      */   
/*      */   protected Object readResolve() {
/*  111 */     if (this._annotationsInside == null) {
/*  112 */       this._annotationsInside = new LRUMap(48, 48);
/*      */     }
/*  114 */     return this;
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
/*      */   public JacksonAnnotationIntrospector setConstructorPropertiesImpliesCreator(boolean b)
/*      */   {
/*  135 */     this._cfgConstructorPropertiesImpliesCreator = b;
/*  136 */     return this;
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
/*      */   public boolean isAnnotationBundle(java.lang.annotation.Annotation ann)
/*      */   {
/*  155 */     Class<?> type = ann.annotationType();
/*  156 */     Boolean b = (Boolean)this._annotationsInside.get(type);
/*  157 */     if (b == null) {
/*  158 */       b = Boolean.valueOf(type.getAnnotation(com.fasterxml.jackson.annotation.JacksonAnnotationsInside.class) != null);
/*  159 */       this._annotationsInside.putIfAbsent(type, b);
/*      */     }
/*  161 */     return b.booleanValue();
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
/*      */   @Deprecated
/*      */   public String findEnumValue(Enum<?> value)
/*      */   {
/*      */     try
/*      */     {
/*  183 */       Field f = value.getClass().getField(value.name());
/*  184 */       if (f != null) {
/*  185 */         JsonProperty prop = (JsonProperty)f.getAnnotation(JsonProperty.class);
/*  186 */         if (prop != null) {
/*  187 */           String n = prop.value();
/*  188 */           if ((n != null) && (!n.isEmpty())) {
/*  189 */             return n;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (SecurityException e) {}catch (NoSuchFieldException e) {}
/*      */     
/*      */ 
/*      */ 
/*  198 */     return value.name();
/*      */   }
/*      */   
/*      */   public String[] findEnumValues(Class<?> enumType, Enum<?>[] enumValues, String[] names)
/*      */   {
/*  203 */     java.util.HashMap<String, String> expl = null;
/*  204 */     for (Field f : com.fasterxml.jackson.databind.util.ClassUtil.getDeclaredFields(enumType))
/*  205 */       if (f.isEnumConstant())
/*      */       {
/*      */ 
/*  208 */         JsonProperty prop = (JsonProperty)f.getAnnotation(JsonProperty.class);
/*  209 */         if (prop != null)
/*      */         {
/*      */ 
/*  212 */           String n = prop.value();
/*  213 */           if (!n.isEmpty())
/*      */           {
/*      */ 
/*  216 */             if (expl == null) {
/*  217 */               expl = new java.util.HashMap();
/*      */             }
/*  219 */             expl.put(f.getName(), n);
/*      */           }
/*      */         } }
/*  222 */     if (expl != null) {
/*  223 */       int i = 0; for (int end = enumValues.length; i < end; i++) {
/*  224 */         String defName = enumValues[i].name();
/*  225 */         String explValue = (String)expl.get(defName);
/*  226 */         if (explValue != null) {
/*  227 */           names[i] = explValue;
/*      */         }
/*      */       }
/*      */     }
/*  231 */     return names;
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
/*      */   public Enum<?> findDefaultEnumValue(Class<Enum<?>> enumCls)
/*      */   {
/*  245 */     return com.fasterxml.jackson.databind.util.ClassUtil.findFirstAnnotatedEnumValue(enumCls, com.fasterxml.jackson.annotation.JsonEnumDefaultValue.class);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PropertyName findRootName(AnnotatedClass ac)
/*      */   {
/*  257 */     com.fasterxml.jackson.annotation.JsonRootName ann = (com.fasterxml.jackson.annotation.JsonRootName)_findAnnotation(ac, com.fasterxml.jackson.annotation.JsonRootName.class);
/*  258 */     if (ann == null) {
/*  259 */       return null;
/*      */     }
/*  261 */     String ns = ann.namespace();
/*  262 */     if ((ns != null) && (ns.length() == 0)) {
/*  263 */       ns = null;
/*      */     }
/*  265 */     return PropertyName.construct(ann.value(), ns);
/*      */   }
/*      */   
/*      */ 
/*      */   public JsonIgnoreProperties.Value findPropertyIgnorals(Annotated a)
/*      */   {
/*  271 */     com.fasterxml.jackson.annotation.JsonIgnoreProperties v = (com.fasterxml.jackson.annotation.JsonIgnoreProperties)_findAnnotation(a, com.fasterxml.jackson.annotation.JsonIgnoreProperties.class);
/*  272 */     if (v == null)
/*      */     {
/*  274 */       return null;
/*      */     }
/*  276 */     return JsonIgnoreProperties.Value.from(v);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public String[] findPropertiesToIgnore(Annotated a, boolean forSerialization)
/*      */   {
/*  282 */     JsonIgnoreProperties.Value v = findPropertyIgnorals(a);
/*  283 */     if (v == null) {
/*  284 */       return null;
/*      */     }
/*      */     
/*  287 */     if (forSerialization) {
/*  288 */       if (v.getAllowGetters()) {
/*  289 */         return null;
/*      */       }
/*      */     }
/*  292 */     else if (v.getAllowSetters()) {
/*  293 */       return null;
/*      */     }
/*      */     
/*  296 */     java.util.Set<String> ignored = v.getIgnored();
/*  297 */     return (String[])ignored.toArray(new String[ignored.size()]);
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public Boolean findIgnoreUnknownProperties(AnnotatedClass a)
/*      */   {
/*  303 */     JsonIgnoreProperties.Value v = findPropertyIgnorals(a);
/*  304 */     return v == null ? null : Boolean.valueOf(v.getIgnoreUnknown());
/*      */   }
/*      */   
/*      */   public Boolean isIgnorableType(AnnotatedClass ac)
/*      */   {
/*  309 */     com.fasterxml.jackson.annotation.JsonIgnoreType ignore = (com.fasterxml.jackson.annotation.JsonIgnoreType)_findAnnotation(ac, com.fasterxml.jackson.annotation.JsonIgnoreType.class);
/*  310 */     return ignore == null ? null : Boolean.valueOf(ignore.value());
/*      */   }
/*      */   
/*      */   public Object findFilterId(Annotated a)
/*      */   {
/*  315 */     com.fasterxml.jackson.annotation.JsonFilter ann = (com.fasterxml.jackson.annotation.JsonFilter)_findAnnotation(a, com.fasterxml.jackson.annotation.JsonFilter.class);
/*  316 */     if (ann != null) {
/*  317 */       String id = ann.value();
/*      */       
/*  319 */       if (id.length() > 0) {
/*  320 */         return id;
/*      */       }
/*      */     }
/*  323 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public Object findNamingStrategy(AnnotatedClass ac)
/*      */   {
/*  329 */     com.fasterxml.jackson.databind.annotation.JsonNaming ann = (com.fasterxml.jackson.databind.annotation.JsonNaming)_findAnnotation(ac, com.fasterxml.jackson.databind.annotation.JsonNaming.class);
/*  330 */     return ann == null ? null : ann.value();
/*      */   }
/*      */   
/*      */   public String findClassDescription(AnnotatedClass ac)
/*      */   {
/*  335 */     com.fasterxml.jackson.annotation.JsonClassDescription ann = (com.fasterxml.jackson.annotation.JsonClassDescription)_findAnnotation(ac, com.fasterxml.jackson.annotation.JsonClassDescription.class);
/*  336 */     return ann == null ? null : ann.value();
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
/*      */   public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker)
/*      */   {
/*  349 */     com.fasterxml.jackson.annotation.JsonAutoDetect ann = (com.fasterxml.jackson.annotation.JsonAutoDetect)_findAnnotation(ac, com.fasterxml.jackson.annotation.JsonAutoDetect.class);
/*  350 */     return ann == null ? checker : checker.with(ann);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String findImplicitPropertyName(AnnotatedMember m)
/*      */   {
/*  361 */     PropertyName n = _findConstructorName(m);
/*  362 */     return n == null ? null : n.getSimpleName();
/*      */   }
/*      */   
/*      */   public boolean hasIgnoreMarker(AnnotatedMember m)
/*      */   {
/*  367 */     return _isIgnorable(m);
/*      */   }
/*      */   
/*      */ 
/*      */   public Boolean hasRequiredMarker(AnnotatedMember m)
/*      */   {
/*  373 */     JsonProperty ann = (JsonProperty)_findAnnotation(m, JsonProperty.class);
/*  374 */     if (ann != null) {
/*  375 */       return Boolean.valueOf(ann.required());
/*      */     }
/*  377 */     return null;
/*      */   }
/*      */   
/*      */   public com.fasterxml.jackson.annotation.JsonProperty.Access findPropertyAccess(Annotated m)
/*      */   {
/*  382 */     JsonProperty ann = (JsonProperty)_findAnnotation(m, JsonProperty.class);
/*  383 */     if (ann != null) {
/*  384 */       return ann.access();
/*      */     }
/*  386 */     return null;
/*      */   }
/*      */   
/*      */   public String findPropertyDescription(Annotated ann)
/*      */   {
/*  391 */     com.fasterxml.jackson.annotation.JsonPropertyDescription desc = (com.fasterxml.jackson.annotation.JsonPropertyDescription)_findAnnotation(ann, com.fasterxml.jackson.annotation.JsonPropertyDescription.class);
/*  392 */     return desc == null ? null : desc.value();
/*      */   }
/*      */   
/*      */   public Integer findPropertyIndex(Annotated ann)
/*      */   {
/*  397 */     JsonProperty prop = (JsonProperty)_findAnnotation(ann, JsonProperty.class);
/*  398 */     if (prop != null) {
/*  399 */       int ix = prop.index();
/*  400 */       if (ix != -1) {
/*  401 */         return Integer.valueOf(ix);
/*      */       }
/*      */     }
/*  404 */     return null;
/*      */   }
/*      */   
/*      */   public String findPropertyDefaultValue(Annotated ann)
/*      */   {
/*  409 */     JsonProperty prop = (JsonProperty)_findAnnotation(ann, JsonProperty.class);
/*  410 */     if (prop == null) {
/*  411 */       return null;
/*      */     }
/*  413 */     String str = prop.defaultValue();
/*      */     
/*  415 */     return str.isEmpty() ? null : str;
/*      */   }
/*      */   
/*      */   public com.fasterxml.jackson.annotation.JsonFormat.Value findFormat(Annotated ann)
/*      */   {
/*  420 */     com.fasterxml.jackson.annotation.JsonFormat f = (com.fasterxml.jackson.annotation.JsonFormat)_findAnnotation(ann, com.fasterxml.jackson.annotation.JsonFormat.class);
/*  421 */     return f == null ? null : new com.fasterxml.jackson.annotation.JsonFormat.Value(f);
/*      */   }
/*      */   
/*      */ 
/*      */   public com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty findReferenceType(AnnotatedMember member)
/*      */   {
/*  427 */     JsonManagedReference ref1 = (JsonManagedReference)_findAnnotation(member, JsonManagedReference.class);
/*  428 */     if (ref1 != null) {
/*  429 */       return com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty.managed(ref1.value());
/*      */     }
/*  431 */     JsonBackReference ref2 = (JsonBackReference)_findAnnotation(member, JsonBackReference.class);
/*  432 */     if (ref2 != null) {
/*  433 */       return com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty.back(ref2.value());
/*      */     }
/*  435 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public com.fasterxml.jackson.databind.util.NameTransformer findUnwrappingNameTransformer(AnnotatedMember member)
/*      */   {
/*  441 */     JsonUnwrapped ann = (JsonUnwrapped)_findAnnotation(member, JsonUnwrapped.class);
/*      */     
/*      */ 
/*  444 */     if ((ann == null) || (!ann.enabled())) {
/*  445 */       return null;
/*      */     }
/*  447 */     String prefix = ann.prefix();
/*  448 */     String suffix = ann.suffix();
/*  449 */     return com.fasterxml.jackson.databind.util.NameTransformer.simpleTransformer(prefix, suffix);
/*      */   }
/*      */   
/*      */ 
/*      */   public Object findInjectableValueId(AnnotatedMember m)
/*      */   {
/*  455 */     com.fasterxml.jackson.annotation.JacksonInject ann = (com.fasterxml.jackson.annotation.JacksonInject)_findAnnotation(m, com.fasterxml.jackson.annotation.JacksonInject.class);
/*  456 */     if (ann == null) {
/*  457 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  462 */     String id = ann.value();
/*  463 */     if (id.length() == 0)
/*      */     {
/*  465 */       if (!(m instanceof AnnotatedMethod)) {
/*  466 */         return m.getRawType().getName();
/*      */       }
/*  468 */       AnnotatedMethod am = (AnnotatedMethod)m;
/*  469 */       if (am.getParameterCount() == 0) {
/*  470 */         return m.getRawType().getName();
/*      */       }
/*  472 */       return am.getRawParameterType(0).getName();
/*      */     }
/*  474 */     return id;
/*      */   }
/*      */   
/*      */ 
/*      */   public Class<?>[] findViews(Annotated a)
/*      */   {
/*  480 */     JsonView ann = (JsonView)_findAnnotation(a, JsonView.class);
/*  481 */     return ann == null ? null : ann.value();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public AnnotatedMethod resolveSetterConflict(MapperConfig<?> config, AnnotatedMethod setter1, AnnotatedMethod setter2)
/*      */   {
/*  488 */     Class<?> cls1 = setter1.getRawParameterType(0);
/*  489 */     Class<?> cls2 = setter2.getRawParameterType(0);
/*      */     
/*      */ 
/*      */ 
/*  493 */     if (cls1.isPrimitive()) {
/*  494 */       if (!cls2.isPrimitive()) {
/*  495 */         return setter1;
/*      */       }
/*  497 */     } else if (cls2.isPrimitive()) {
/*  498 */       return setter2;
/*      */     }
/*      */     
/*  501 */     if (cls1 == String.class) {
/*  502 */       if (cls2 != String.class) {
/*  503 */         return setter1;
/*      */       }
/*  505 */     } else if (cls2 == String.class) {
/*  506 */       return setter2;
/*      */     }
/*      */     
/*  509 */     return null;
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
/*      */   public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> config, AnnotatedClass ac, JavaType baseType)
/*      */   {
/*  522 */     return _findTypeResolver(config, ac, baseType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TypeResolverBuilder<?> findPropertyTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType baseType)
/*      */   {
/*  533 */     if ((baseType.isContainerType()) || (baseType.isReferenceType())) {
/*  534 */       return null;
/*      */     }
/*      */     
/*  537 */     return _findTypeResolver(config, am, baseType);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public TypeResolverBuilder<?> findPropertyContentTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType containerType)
/*      */   {
/*  547 */     if (containerType.getContentType() == null) {
/*  548 */       throw new IllegalArgumentException("Must call method with a container or reference type (got " + containerType + ")");
/*      */     }
/*  550 */     return _findTypeResolver(config, am, containerType);
/*      */   }
/*      */   
/*      */ 
/*      */   public List<com.fasterxml.jackson.databind.jsontype.NamedType> findSubtypes(Annotated a)
/*      */   {
/*  556 */     com.fasterxml.jackson.annotation.JsonSubTypes t = (com.fasterxml.jackson.annotation.JsonSubTypes)_findAnnotation(a, com.fasterxml.jackson.annotation.JsonSubTypes.class);
/*  557 */     if (t == null) return null;
/*  558 */     com.fasterxml.jackson.annotation.JsonSubTypes.Type[] types = t.value();
/*  559 */     java.util.ArrayList<com.fasterxml.jackson.databind.jsontype.NamedType> result = new java.util.ArrayList(types.length);
/*  560 */     for (com.fasterxml.jackson.annotation.JsonSubTypes.Type type : types) {
/*  561 */       result.add(new com.fasterxml.jackson.databind.jsontype.NamedType(type.value(), type.name()));
/*      */     }
/*  563 */     return result;
/*      */   }
/*      */   
/*      */ 
/*      */   public String findTypeName(AnnotatedClass ac)
/*      */   {
/*  569 */     com.fasterxml.jackson.annotation.JsonTypeName tn = (com.fasterxml.jackson.annotation.JsonTypeName)_findAnnotation(ac, com.fasterxml.jackson.annotation.JsonTypeName.class);
/*  570 */     return tn == null ? null : tn.value();
/*      */   }
/*      */   
/*      */   public Boolean isTypeId(AnnotatedMember member)
/*      */   {
/*  575 */     return Boolean.valueOf(_hasAnnotation(member, com.fasterxml.jackson.annotation.JsonTypeId.class));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectIdInfo findObjectIdInfo(Annotated ann)
/*      */   {
/*  586 */     JsonIdentityInfo info = (JsonIdentityInfo)_findAnnotation(ann, JsonIdentityInfo.class);
/*  587 */     if ((info == null) || (info.generator() == com.fasterxml.jackson.annotation.ObjectIdGenerators.None.class)) {
/*  588 */       return null;
/*      */     }
/*      */     
/*  591 */     PropertyName name = PropertyName.construct(info.property());
/*  592 */     return new ObjectIdInfo(name, info.scope(), info.generator(), info.resolver());
/*      */   }
/*      */   
/*      */   public ObjectIdInfo findObjectReferenceInfo(Annotated ann, ObjectIdInfo objectIdInfo)
/*      */   {
/*  597 */     com.fasterxml.jackson.annotation.JsonIdentityReference ref = (com.fasterxml.jackson.annotation.JsonIdentityReference)_findAnnotation(ann, com.fasterxml.jackson.annotation.JsonIdentityReference.class);
/*  598 */     if (ref == null) {
/*  599 */       return objectIdInfo;
/*      */     }
/*  601 */     if (objectIdInfo == null) {
/*  602 */       objectIdInfo = ObjectIdInfo.empty();
/*      */     }
/*  604 */     return objectIdInfo.withAlwaysAsId(ref.alwaysAsId());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object findSerializer(Annotated a)
/*      */   {
/*  616 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/*  617 */     if (ann != null)
/*      */     {
/*  619 */       Class<? extends com.fasterxml.jackson.databind.JsonSerializer> serClass = ann.using();
/*  620 */       if (serClass != com.fasterxml.jackson.databind.JsonSerializer.None.class) {
/*  621 */         return serClass;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  629 */     JsonRawValue annRaw = (JsonRawValue)_findAnnotation(a, JsonRawValue.class);
/*  630 */     if ((annRaw != null) && (annRaw.value()))
/*      */     {
/*  632 */       Class<?> cls = a.getRawType();
/*  633 */       return new com.fasterxml.jackson.databind.ser.std.RawSerializer(cls);
/*      */     }
/*  635 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public Object findKeySerializer(Annotated a)
/*      */   {
/*  641 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/*  642 */     if (ann != null)
/*      */     {
/*  644 */       Class<? extends com.fasterxml.jackson.databind.JsonSerializer> serClass = ann.keyUsing();
/*  645 */       if (serClass != com.fasterxml.jackson.databind.JsonSerializer.None.class) {
/*  646 */         return serClass;
/*      */       }
/*      */     }
/*  649 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public Object findContentSerializer(Annotated a)
/*      */   {
/*  655 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/*  656 */     if (ann != null)
/*      */     {
/*  658 */       Class<? extends com.fasterxml.jackson.databind.JsonSerializer> serClass = ann.contentUsing();
/*  659 */       if (serClass != com.fasterxml.jackson.databind.JsonSerializer.None.class) {
/*  660 */         return serClass;
/*      */       }
/*      */     }
/*  663 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public Object findNullSerializer(Annotated a)
/*      */   {
/*  669 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/*  670 */     if (ann != null)
/*      */     {
/*  672 */       Class<? extends com.fasterxml.jackson.databind.JsonSerializer> serClass = ann.nullsUsing();
/*  673 */       if (serClass != com.fasterxml.jackson.databind.JsonSerializer.None.class) {
/*  674 */         return serClass;
/*      */       }
/*      */     }
/*  677 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JsonInclude.Include findSerializationInclusion(Annotated a, JsonInclude.Include defValue)
/*      */   {
/*  684 */     JsonInclude inc = (JsonInclude)_findAnnotation(a, JsonInclude.class);
/*  685 */     if (inc != null) {
/*  686 */       JsonInclude.Include v = inc.value();
/*  687 */       if (v != JsonInclude.Include.USE_DEFAULTS) {
/*  688 */         return v;
/*      */       }
/*      */     }
/*  691 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/*  692 */     if (ann != null) {
/*  693 */       com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion i2 = ann.include();
/*  694 */       switch (i2) {
/*      */       case ALWAYS: 
/*  696 */         return JsonInclude.Include.ALWAYS;
/*      */       case NON_NULL: 
/*  698 */         return JsonInclude.Include.NON_NULL;
/*      */       case NON_DEFAULT: 
/*  700 */         return JsonInclude.Include.NON_DEFAULT;
/*      */       case NON_EMPTY: 
/*  702 */         return JsonInclude.Include.NON_EMPTY;
/*      */       }
/*      */       
/*      */     }
/*      */     
/*  707 */     return defValue;
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public JsonInclude.Include findSerializationInclusionForContent(Annotated a, JsonInclude.Include defValue)
/*      */   {
/*  714 */     JsonInclude inc = (JsonInclude)_findAnnotation(a, JsonInclude.class);
/*  715 */     if (inc != null) {
/*  716 */       JsonInclude.Include incl = inc.content();
/*  717 */       if (incl != JsonInclude.Include.USE_DEFAULTS) {
/*  718 */         return incl;
/*      */       }
/*      */     }
/*  721 */     return defValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public com.fasterxml.jackson.annotation.JsonInclude.Value findPropertyInclusion(Annotated a)
/*      */   {
/*  728 */     JsonInclude inc = (JsonInclude)_findAnnotation(a, JsonInclude.class);
/*  729 */     JsonInclude.Include valueIncl = inc == null ? JsonInclude.Include.USE_DEFAULTS : inc.value();
/*  730 */     if (valueIncl == JsonInclude.Include.USE_DEFAULTS) {
/*  731 */       JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/*  732 */       if (ann != null) {
/*  733 */         com.fasterxml.jackson.databind.annotation.JsonSerialize.Inclusion i2 = ann.include();
/*  734 */         switch (i2) {
/*      */         case ALWAYS: 
/*  736 */           valueIncl = JsonInclude.Include.ALWAYS;
/*  737 */           break;
/*      */         case NON_NULL: 
/*  739 */           valueIncl = JsonInclude.Include.NON_NULL;
/*  740 */           break;
/*      */         case NON_DEFAULT: 
/*  742 */           valueIncl = JsonInclude.Include.NON_DEFAULT;
/*  743 */           break;
/*      */         case NON_EMPTY: 
/*  745 */           valueIncl = JsonInclude.Include.NON_EMPTY;
/*  746 */           break;
/*      */         }
/*      */         
/*      */       }
/*      */     }
/*      */     
/*  752 */     JsonInclude.Include contentIncl = inc == null ? JsonInclude.Include.USE_DEFAULTS : inc.content();
/*  753 */     return com.fasterxml.jackson.annotation.JsonInclude.Value.construct(valueIncl, contentIncl);
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public Class<?> findSerializationType(Annotated am)
/*      */   {
/*  760 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(am, JsonSerialize.class);
/*  761 */     return ann == null ? null : _classIfExplicit(ann.as());
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public Class<?> findSerializationKeyType(Annotated am, JavaType baseType)
/*      */   {
/*  768 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(am, JsonSerialize.class);
/*  769 */     return ann == null ? null : _classIfExplicit(ann.keyAs());
/*      */   }
/*      */   
/*      */ 
/*      */   @Deprecated
/*      */   public Class<?> findSerializationContentType(Annotated am, JavaType baseType)
/*      */   {
/*  776 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(am, JsonSerialize.class);
/*  777 */     return ann == null ? null : _classIfExplicit(ann.contentAs());
/*      */   }
/*      */   
/*      */ 
/*      */   public com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing findSerializationTyping(Annotated a)
/*      */   {
/*  783 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/*  784 */     return ann == null ? null : ann.typing();
/*      */   }
/*      */   
/*      */   public Object findSerializationConverter(Annotated a)
/*      */   {
/*  789 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/*  790 */     return ann == null ? null : _classIfExplicit(ann.converter(), com.fasterxml.jackson.databind.util.Converter.None.class);
/*      */   }
/*      */   
/*      */   public Object findSerializationContentConverter(AnnotatedMember a)
/*      */   {
/*  795 */     JsonSerialize ann = (JsonSerialize)_findAnnotation(a, JsonSerialize.class);
/*  796 */     return ann == null ? null : _classIfExplicit(ann.contentConverter(), com.fasterxml.jackson.databind.util.Converter.None.class);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String[] findSerializationPropertyOrder(AnnotatedClass ac)
/*      */   {
/*  807 */     JsonPropertyOrder order = (JsonPropertyOrder)_findAnnotation(ac, JsonPropertyOrder.class);
/*  808 */     return order == null ? null : order.value();
/*      */   }
/*      */   
/*      */   public Boolean findSerializationSortAlphabetically(Annotated ann)
/*      */   {
/*  813 */     return _findSortAlpha(ann);
/*      */   }
/*      */   
/*      */   private final Boolean _findSortAlpha(Annotated ann) {
/*  817 */     JsonPropertyOrder order = (JsonPropertyOrder)_findAnnotation(ann, JsonPropertyOrder.class);
/*      */     
/*      */ 
/*      */ 
/*  821 */     if ((order != null) && (order.alphabetic())) {
/*  822 */       return Boolean.TRUE;
/*      */     }
/*  824 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public void findAndAddVirtualProperties(MapperConfig<?> config, AnnotatedClass ac, List<BeanPropertyWriter> properties)
/*      */   {
/*  830 */     JsonAppend ann = (JsonAppend)_findAnnotation(ac, JsonAppend.class);
/*  831 */     if (ann == null) {
/*  832 */       return;
/*      */     }
/*  834 */     boolean prepend = ann.prepend();
/*  835 */     JavaType propType = null;
/*      */     
/*      */ 
/*  838 */     JsonAppend.Attr[] attrs = ann.attrs();
/*  839 */     int i = 0; for (int len = attrs.length; i < len; i++) {
/*  840 */       if (propType == null) {
/*  841 */         propType = config.constructType(Object.class);
/*      */       }
/*  843 */       BeanPropertyWriter bpw = _constructVirtualProperty(attrs[i], config, ac, propType);
/*      */       
/*  845 */       if (prepend) {
/*  846 */         properties.add(i, bpw);
/*      */       } else {
/*  848 */         properties.add(bpw);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  853 */     JsonAppend.Prop[] props = ann.props();
/*  854 */     int i = 0; for (int len = props.length; i < len; i++) {
/*  855 */       BeanPropertyWriter bpw = _constructVirtualProperty(props[i], config, ac);
/*      */       
/*  857 */       if (prepend) {
/*  858 */         properties.add(i, bpw);
/*      */       } else {
/*  860 */         properties.add(bpw);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected BeanPropertyWriter _constructVirtualProperty(JsonAppend.Attr attr, MapperConfig<?> config, AnnotatedClass ac, JavaType type)
/*      */   {
/*  868 */     PropertyMetadata metadata = attr.required() ? PropertyMetadata.STD_REQUIRED : PropertyMetadata.STD_OPTIONAL;
/*      */     
/*      */ 
/*  871 */     String attrName = attr.value();
/*      */     
/*      */ 
/*  874 */     PropertyName propName = _propertyName(attr.propName(), attr.propNamespace());
/*  875 */     if (!propName.hasSimpleName()) {
/*  876 */       propName = PropertyName.construct(attrName);
/*      */     }
/*      */     
/*  879 */     AnnotatedMember member = new VirtualAnnotatedMember(ac, ac.getRawType(), attrName, type);
/*      */     
/*      */ 
/*  882 */     com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition propDef = com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition.construct(config, member, propName, metadata, attr.include());
/*      */     
/*      */ 
/*  885 */     return com.fasterxml.jackson.databind.ser.impl.AttributePropertyWriter.construct(attrName, propDef, ac.getAnnotations(), type);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected BeanPropertyWriter _constructVirtualProperty(JsonAppend.Prop prop, MapperConfig<?> config, AnnotatedClass ac)
/*      */   {
/*  892 */     PropertyMetadata metadata = prop.required() ? PropertyMetadata.STD_REQUIRED : PropertyMetadata.STD_OPTIONAL;
/*      */     
/*  894 */     PropertyName propName = _propertyName(prop.name(), prop.namespace());
/*  895 */     JavaType type = config.constructType(prop.type());
/*      */     
/*  897 */     AnnotatedMember member = new VirtualAnnotatedMember(ac, ac.getRawType(), propName.getSimpleName(), type);
/*      */     
/*      */ 
/*  900 */     com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition propDef = com.fasterxml.jackson.databind.util.SimpleBeanPropertyDefinition.construct(config, member, propName, metadata, prop.include());
/*      */     
/*      */ 
/*  903 */     Class<?> implClass = prop.value();
/*      */     
/*  905 */     com.fasterxml.jackson.databind.cfg.HandlerInstantiator hi = config.getHandlerInstantiator();
/*  906 */     com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter bpw = hi == null ? null : hi.virtualPropertyWriterInstance(config, implClass);
/*      */     
/*  908 */     if (bpw == null) {
/*  909 */       bpw = (com.fasterxml.jackson.databind.ser.VirtualBeanPropertyWriter)com.fasterxml.jackson.databind.util.ClassUtil.createInstance(implClass, config.canOverrideAccessModifiers());
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  914 */     return bpw.withConfig(config, ac, propDef, type);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PropertyName findNameForSerialization(Annotated a)
/*      */   {
/*  926 */     com.fasterxml.jackson.annotation.JsonGetter jg = (com.fasterxml.jackson.annotation.JsonGetter)_findAnnotation(a, com.fasterxml.jackson.annotation.JsonGetter.class);
/*  927 */     if (jg != null) {
/*  928 */       return PropertyName.construct(jg.value());
/*      */     }
/*  930 */     JsonProperty pann = (JsonProperty)_findAnnotation(a, JsonProperty.class);
/*  931 */     if (pann != null) {
/*  932 */       return PropertyName.construct(pann.value());
/*      */     }
/*  934 */     if (_hasOneOf(a, ANNOTATIONS_TO_INFER_SER)) {
/*  935 */       return PropertyName.USE_DEFAULT;
/*      */     }
/*  937 */     return null;
/*      */   }
/*      */   
/*      */   public boolean hasAsValueAnnotation(AnnotatedMethod am)
/*      */   {
/*  942 */     com.fasterxml.jackson.annotation.JsonValue ann = (com.fasterxml.jackson.annotation.JsonValue)_findAnnotation(am, com.fasterxml.jackson.annotation.JsonValue.class);
/*      */     
/*  944 */     return (ann != null) && (ann.value());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object findDeserializer(Annotated a)
/*      */   {
/*  956 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(a, JsonDeserialize.class);
/*  957 */     if (ann != null)
/*      */     {
/*  959 */       Class<? extends com.fasterxml.jackson.databind.JsonDeserializer> deserClass = ann.using();
/*  960 */       if (deserClass != com.fasterxml.jackson.databind.JsonDeserializer.None.class) {
/*  961 */         return deserClass;
/*      */       }
/*      */     }
/*  964 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public Object findKeyDeserializer(Annotated a)
/*      */   {
/*  970 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(a, JsonDeserialize.class);
/*  971 */     if (ann != null) {
/*  972 */       Class<? extends com.fasterxml.jackson.databind.KeyDeserializer> deserClass = ann.keyUsing();
/*  973 */       if (deserClass != com.fasterxml.jackson.databind.KeyDeserializer.None.class) {
/*  974 */         return deserClass;
/*      */       }
/*      */     }
/*  977 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public Object findContentDeserializer(Annotated a)
/*      */   {
/*  983 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(a, JsonDeserialize.class);
/*  984 */     if (ann != null)
/*      */     {
/*  986 */       Class<? extends com.fasterxml.jackson.databind.JsonDeserializer> deserClass = ann.contentUsing();
/*  987 */       if (deserClass != com.fasterxml.jackson.databind.JsonDeserializer.None.class) {
/*  988 */         return deserClass;
/*      */       }
/*      */     }
/*  991 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */   public Object findDeserializationConverter(Annotated a)
/*      */   {
/*  997 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(a, JsonDeserialize.class);
/*  998 */     return ann == null ? null : _classIfExplicit(ann.converter(), com.fasterxml.jackson.databind.util.Converter.None.class);
/*      */   }
/*      */   
/*      */ 
/*      */   public Object findDeserializationContentConverter(AnnotatedMember a)
/*      */   {
/* 1004 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(a, JsonDeserialize.class);
/* 1005 */     return ann == null ? null : _classIfExplicit(ann.contentConverter(), com.fasterxml.jackson.databind.util.Converter.None.class);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public Class<?> findDeserializationContentType(Annotated am, JavaType baseContentType)
/*      */   {
/* 1018 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(am, JsonDeserialize.class);
/* 1019 */     return ann == null ? null : _classIfExplicit(ann.contentAs());
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public Class<?> findDeserializationType(Annotated am, JavaType baseType)
/*      */   {
/* 1025 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(am, JsonDeserialize.class);
/* 1026 */     return ann == null ? null : _classIfExplicit(ann.as());
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public Class<?> findDeserializationKeyType(Annotated am, JavaType baseKeyType)
/*      */   {
/* 1032 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(am, JsonDeserialize.class);
/* 1033 */     return ann == null ? null : _classIfExplicit(ann.keyAs());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object findValueInstantiator(AnnotatedClass ac)
/*      */   {
/* 1045 */     com.fasterxml.jackson.databind.annotation.JsonValueInstantiator ann = (com.fasterxml.jackson.databind.annotation.JsonValueInstantiator)_findAnnotation(ac, com.fasterxml.jackson.databind.annotation.JsonValueInstantiator.class);
/*      */     
/* 1047 */     return ann == null ? null : ann.value();
/*      */   }
/*      */   
/*      */ 
/*      */   public Class<?> findPOJOBuilder(AnnotatedClass ac)
/*      */   {
/* 1053 */     JsonDeserialize ann = (JsonDeserialize)_findAnnotation(ac, JsonDeserialize.class);
/* 1054 */     return ann == null ? null : _classIfExplicit(ann.builder());
/*      */   }
/*      */   
/*      */ 
/*      */   public com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value findPOJOBuilderConfig(AnnotatedClass ac)
/*      */   {
/* 1060 */     com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder ann = (com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder)_findAnnotation(ac, com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.class);
/* 1061 */     return ann == null ? null : new com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value(ann);
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
/*      */   public PropertyName findNameForDeserialization(Annotated a)
/*      */   {
/* 1075 */     com.fasterxml.jackson.annotation.JsonSetter js = (com.fasterxml.jackson.annotation.JsonSetter)_findAnnotation(a, com.fasterxml.jackson.annotation.JsonSetter.class);
/* 1076 */     if (js != null) {
/* 1077 */       return PropertyName.construct(js.value());
/*      */     }
/* 1079 */     JsonProperty pann = (JsonProperty)_findAnnotation(a, JsonProperty.class);
/* 1080 */     if (pann != null) {
/* 1081 */       return PropertyName.construct(pann.value());
/*      */     }
/* 1083 */     if (_hasOneOf(a, ANNOTATIONS_TO_INFER_DESER)) {
/* 1084 */       return PropertyName.USE_DEFAULT;
/*      */     }
/* 1086 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasAnySetterAnnotation(AnnotatedMethod am)
/*      */   {
/* 1096 */     return _hasAnnotation(am, com.fasterxml.jackson.annotation.JsonAnySetter.class);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasAnyGetterAnnotation(AnnotatedMethod am)
/*      */   {
/* 1105 */     return _hasAnnotation(am, com.fasterxml.jackson.annotation.JsonAnyGetter.class);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasCreatorAnnotation(Annotated a)
/*      */   {
/* 1114 */     JsonCreator ann = (JsonCreator)_findAnnotation(a, JsonCreator.class);
/* 1115 */     if (ann != null) {
/* 1116 */       return ann.mode() != com.fasterxml.jackson.annotation.JsonCreator.Mode.DISABLED;
/*      */     }
/*      */     
/*      */ 
/* 1120 */     if ((this._cfgConstructorPropertiesImpliesCreator) && 
/* 1121 */       ((a instanceof AnnotatedConstructor)) && 
/* 1122 */       (_java7Helper != null)) {
/* 1123 */       Boolean b = _java7Helper.hasCreatorAnnotation(a);
/* 1124 */       if (b != null) {
/* 1125 */         return b.booleanValue();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1130 */     return false;
/*      */   }
/*      */   
/*      */   public com.fasterxml.jackson.annotation.JsonCreator.Mode findCreatorBinding(Annotated a)
/*      */   {
/* 1135 */     JsonCreator ann = (JsonCreator)_findAnnotation(a, JsonCreator.class);
/* 1136 */     return ann == null ? null : ann.mode();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _isIgnorable(Annotated a)
/*      */   {
/* 1147 */     com.fasterxml.jackson.annotation.JsonIgnore ann = (com.fasterxml.jackson.annotation.JsonIgnore)_findAnnotation(a, com.fasterxml.jackson.annotation.JsonIgnore.class);
/* 1148 */     if (ann != null) {
/* 1149 */       return ann.value();
/*      */     }
/* 1151 */     if (_java7Helper != null) {
/* 1152 */       Boolean b = _java7Helper.findTransient(a);
/* 1153 */       if (b != null) {
/* 1154 */         return b.booleanValue();
/*      */       }
/*      */     }
/* 1157 */     return false;
/*      */   }
/*      */   
/*      */   protected Class<?> _classIfExplicit(Class<?> cls) {
/* 1161 */     if ((cls == null) || (com.fasterxml.jackson.databind.util.ClassUtil.isBogusClass(cls))) {
/* 1162 */       return null;
/*      */     }
/* 1164 */     return cls;
/*      */   }
/*      */   
/*      */   protected Class<?> _classIfExplicit(Class<?> cls, Class<?> implicit) {
/* 1168 */     cls = _classIfExplicit(cls);
/* 1169 */     return (cls == null) || (cls == implicit) ? null : cls;
/*      */   }
/*      */   
/*      */   protected PropertyName _propertyName(String localName, String namespace) {
/* 1173 */     if (localName.isEmpty()) {
/* 1174 */       return PropertyName.USE_DEFAULT;
/*      */     }
/* 1176 */     if ((namespace == null) || (namespace.isEmpty())) {
/* 1177 */       return PropertyName.construct(localName);
/*      */     }
/* 1179 */     return PropertyName.construct(localName, namespace);
/*      */   }
/*      */   
/*      */   protected PropertyName _findConstructorName(Annotated a)
/*      */   {
/* 1184 */     if ((a instanceof AnnotatedParameter)) {
/* 1185 */       AnnotatedParameter p = (AnnotatedParameter)a;
/* 1186 */       AnnotatedWithParams ctor = p.getOwner();
/*      */       
/* 1188 */       if ((ctor != null) && 
/* 1189 */         (_java7Helper != null)) {
/* 1190 */         PropertyName name = _java7Helper.findConstructorName(p);
/* 1191 */         if (name != null) {
/* 1192 */           return name;
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1197 */     return null;
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
/*      */   protected TypeResolverBuilder<?> _findTypeResolver(MapperConfig<?> config, Annotated ann, JavaType baseType)
/*      */   {
/* 1210 */     JsonTypeInfo info = (JsonTypeInfo)_findAnnotation(ann, JsonTypeInfo.class);
/* 1211 */     com.fasterxml.jackson.databind.annotation.JsonTypeResolver resAnn = (com.fasterxml.jackson.databind.annotation.JsonTypeResolver)_findAnnotation(ann, com.fasterxml.jackson.databind.annotation.JsonTypeResolver.class);
/*      */     TypeResolverBuilder<?> b;
/* 1213 */     if (resAnn != null) {
/* 1214 */       if (info == null) {
/* 1215 */         return null;
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/* 1221 */       b = config.typeResolverBuilderInstance(ann, resAnn.value());
/*      */     } else {
/* 1223 */       if (info == null) {
/* 1224 */         return null;
/*      */       }
/*      */       
/* 1227 */       if (info.use() == com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NONE) {
/* 1228 */         return _constructNoTypeResolverBuilder();
/*      */       }
/* 1230 */       b = _constructStdTypeResolverBuilder();
/*      */     }
/*      */     
/* 1233 */     com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver idResInfo = (com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver)_findAnnotation(ann, com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver.class);
/* 1234 */     com.fasterxml.jackson.databind.jsontype.TypeIdResolver idRes = idResInfo == null ? null : config.typeIdResolverInstance(ann, idResInfo.value());
/*      */     
/* 1236 */     if (idRes != null) {
/* 1237 */       idRes.init(baseType);
/*      */     }
/* 1239 */     TypeResolverBuilder<?> b = b.init(info.use(), idRes);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1244 */     com.fasterxml.jackson.annotation.JsonTypeInfo.As inclusion = info.include();
/* 1245 */     if ((inclusion == com.fasterxml.jackson.annotation.JsonTypeInfo.As.EXTERNAL_PROPERTY) && ((ann instanceof AnnotatedClass))) {
/* 1246 */       inclusion = com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
/*      */     }
/* 1248 */     b = b.inclusion(inclusion);
/* 1249 */     b = b.typeProperty(info.property());
/* 1250 */     Class<?> defaultImpl = info.defaultImpl();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1256 */     if ((defaultImpl != com.fasterxml.jackson.annotation.JsonTypeInfo.None.class) && (!defaultImpl.isAnnotation())) {
/* 1257 */       b = b.defaultImpl(defaultImpl);
/*      */     }
/* 1259 */     b = b.typeIdVisibility(info.visible());
/* 1260 */     return b;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder _constructStdTypeResolverBuilder()
/*      */   {
/* 1268 */     return new com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder _constructNoTypeResolverBuilder()
/*      */   {
/* 1276 */     return com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder.noTypeInfoBuilder();
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\JacksonAnnotationIntrospector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */