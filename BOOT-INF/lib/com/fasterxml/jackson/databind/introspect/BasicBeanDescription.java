/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import com.fasterxml.jackson.databind.util.Converter.None;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class BasicBeanDescription
/*     */   extends BeanDescription
/*     */ {
/*     */   protected final POJOPropertiesCollector _propCollector;
/*     */   protected final MapperConfig<?> _config;
/*     */   protected final AnnotationIntrospector _annotationIntrospector;
/*     */   protected final AnnotatedClass _classInfo;
/*     */   protected List<BeanPropertyDefinition> _properties;
/*     */   protected ObjectIdInfo _objectIdInfo;
/*     */   
/*     */   protected BasicBeanDescription(POJOPropertiesCollector coll, JavaType type, AnnotatedClass classDef)
/*     */   {
/*  76 */     super(type);
/*  77 */     this._propCollector = coll;
/*  78 */     this._config = coll.getConfig();
/*     */     
/*  80 */     if (this._config == null) {
/*  81 */       this._annotationIntrospector = null;
/*     */     } else {
/*  83 */       this._annotationIntrospector = this._config.getAnnotationIntrospector();
/*     */     }
/*  85 */     this._classInfo = classDef;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BasicBeanDescription(MapperConfig<?> config, JavaType type, AnnotatedClass classDef, List<BeanPropertyDefinition> props)
/*     */   {
/*  95 */     super(type);
/*  96 */     this._propCollector = null;
/*  97 */     this._config = config;
/*     */     
/*  99 */     if (this._config == null) {
/* 100 */       this._annotationIntrospector = null;
/*     */     } else {
/* 102 */       this._annotationIntrospector = this._config.getAnnotationIntrospector();
/*     */     }
/* 104 */     this._classInfo = classDef;
/* 105 */     this._properties = props;
/*     */   }
/*     */   
/*     */   protected BasicBeanDescription(POJOPropertiesCollector coll)
/*     */   {
/* 110 */     this(coll, coll.getType(), coll.getClassDef());
/* 111 */     this._objectIdInfo = coll.getObjectIdInfo();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BasicBeanDescription forDeserialization(POJOPropertiesCollector coll)
/*     */   {
/* 119 */     return new BasicBeanDescription(coll);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BasicBeanDescription forSerialization(POJOPropertiesCollector coll)
/*     */   {
/* 127 */     return new BasicBeanDescription(coll);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static BasicBeanDescription forOtherUse(MapperConfig<?> config, JavaType type, AnnotatedClass ac)
/*     */   {
/* 138 */     return new BasicBeanDescription(config, type, ac, Collections.emptyList());
/*     */   }
/*     */   
/*     */   protected List<BeanPropertyDefinition> _properties()
/*     */   {
/* 143 */     if (this._properties == null) {
/* 144 */       this._properties = this._propCollector.getProperties();
/*     */     }
/* 146 */     return this._properties;
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
/*     */   public boolean removeProperty(String propName)
/*     */   {
/* 164 */     Iterator<BeanPropertyDefinition> it = _properties().iterator();
/* 165 */     while (it.hasNext()) {
/* 166 */       BeanPropertyDefinition prop = (BeanPropertyDefinition)it.next();
/* 167 */       if (prop.getName().equals(propName)) {
/* 168 */         it.remove();
/* 169 */         return true;
/*     */       }
/*     */     }
/* 172 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean addProperty(BeanPropertyDefinition def)
/*     */   {
/* 178 */     if (hasProperty(def.getFullName())) {
/* 179 */       return false;
/*     */     }
/* 181 */     _properties().add(def);
/* 182 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasProperty(PropertyName name)
/*     */   {
/* 189 */     return findProperty(name) != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanPropertyDefinition findProperty(PropertyName name)
/*     */   {
/* 197 */     for (BeanPropertyDefinition prop : _properties()) {
/* 198 */       if (prop.hasName(name)) {
/* 199 */         return prop;
/*     */       }
/*     */     }
/* 202 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedClass getClassInfo()
/*     */   {
/* 212 */     return this._classInfo;
/*     */   }
/*     */   
/* 215 */   public ObjectIdInfo getObjectIdInfo() { return this._objectIdInfo; }
/*     */   
/*     */   public List<BeanPropertyDefinition> findProperties()
/*     */   {
/* 219 */     return _properties();
/*     */   }
/*     */   
/*     */   public AnnotatedMethod findJsonValueMethod()
/*     */   {
/* 224 */     return this._propCollector == null ? null : this._propCollector.getJsonValueMethod();
/*     */   }
/*     */   
/*     */ 
/*     */   public Set<String> getIgnoredPropertyNames()
/*     */   {
/* 230 */     Set<String> ign = this._propCollector == null ? null : this._propCollector.getIgnoredPropertyNames();
/*     */     
/* 232 */     if (ign == null) {
/* 233 */       return Collections.emptySet();
/*     */     }
/* 235 */     return ign;
/*     */   }
/*     */   
/*     */   public boolean hasKnownClassAnnotations()
/*     */   {
/* 240 */     return this._classInfo.hasAnnotations();
/*     */   }
/*     */   
/*     */   public Annotations getClassAnnotations()
/*     */   {
/* 245 */     return this._classInfo.getAnnotations();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public TypeBindings bindingsForBeanType()
/*     */   {
/* 251 */     return this._type.getBindings();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public JavaType resolveType(Type jdkType)
/*     */   {
/* 257 */     if (jdkType == null) {
/* 258 */       return null;
/*     */     }
/* 260 */     return this._config.getTypeFactory().constructType(jdkType, this._type.getBindings());
/*     */   }
/*     */   
/*     */   public AnnotatedConstructor findDefaultConstructor()
/*     */   {
/* 265 */     return this._classInfo.getDefaultConstructor();
/*     */   }
/*     */   
/*     */   public AnnotatedMethod findAnySetter()
/*     */     throws IllegalArgumentException
/*     */   {
/* 271 */     AnnotatedMethod anySetter = this._propCollector == null ? null : this._propCollector.getAnySetterMethod();
/*     */     
/* 273 */     if (anySetter != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 282 */       Class<?> type = anySetter.getRawParameterType(0);
/* 283 */       if ((type != String.class) && (type != Object.class)) {
/* 284 */         throw new IllegalArgumentException("Invalid 'any-setter' annotation on method " + anySetter.getName() + "(): first argument not of type String or Object, but " + type.getName());
/*     */       }
/*     */     }
/* 287 */     return anySetter;
/*     */   }
/*     */   
/*     */   public Map<Object, AnnotatedMember> findInjectables()
/*     */   {
/* 292 */     if (this._propCollector != null) {
/* 293 */       return this._propCollector.getInjectables();
/*     */     }
/* 295 */     return Collections.emptyMap();
/*     */   }
/*     */   
/*     */   public List<AnnotatedConstructor> getConstructors()
/*     */   {
/* 300 */     return this._classInfo.getConstructors();
/*     */   }
/*     */   
/*     */   public Object instantiateBean(boolean fixAccess)
/*     */   {
/* 305 */     AnnotatedConstructor ac = this._classInfo.getDefaultConstructor();
/* 306 */     if (ac == null) {
/* 307 */       return null;
/*     */     }
/* 309 */     if (fixAccess) {
/* 310 */       ac.fixAccess(this._config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*     */     }
/*     */     try {
/* 313 */       return ac.getAnnotated().newInstance(new Object[0]);
/*     */     } catch (Exception e) {
/* 315 */       Throwable t = e;
/* 316 */       while (t.getCause() != null) {
/* 317 */         t = t.getCause();
/*     */       }
/* 319 */       if ((t instanceof Error)) throw ((Error)t);
/* 320 */       if ((t instanceof RuntimeException)) throw ((RuntimeException)t);
/* 321 */       throw new IllegalArgumentException("Failed to instantiate bean of type " + this._classInfo.getAnnotated().getName() + ": (" + t.getClass().getName() + ") " + t.getMessage(), t);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedMethod findMethod(String name, Class<?>[] paramTypes)
/*     */   {
/* 333 */     return this._classInfo.findMethod(name, paramTypes);
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
/*     */   public JsonFormat.Value findExpectedFormat(JsonFormat.Value defValue)
/*     */   {
/* 347 */     if (this._annotationIntrospector != null) {
/* 348 */       JsonFormat.Value v = this._annotationIntrospector.findFormat(this._classInfo);
/* 349 */       if (v != null) {
/* 350 */         if (defValue == null) {
/* 351 */           defValue = v;
/*     */         } else {
/* 353 */           defValue = defValue.withOverrides(v);
/*     */         }
/*     */       }
/*     */     }
/* 357 */     JsonFormat.Value v = this._config.getDefaultPropertyFormat(this._classInfo.getRawType());
/* 358 */     if (v != null) {
/* 359 */       if (defValue == null) {
/* 360 */         defValue = v;
/*     */       } else {
/* 362 */         defValue = defValue.withOverrides(v);
/*     */       }
/*     */     }
/* 365 */     return defValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Converter<Object, Object> findSerializationConverter()
/*     */   {
/* 377 */     if (this._annotationIntrospector == null) {
/* 378 */       return null;
/*     */     }
/* 380 */     return _createConverter(this._annotationIntrospector.findSerializationConverter(this._classInfo));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonInclude.Value findPropertyInclusion(JsonInclude.Value defValue)
/*     */   {
/* 391 */     if (this._annotationIntrospector != null) {
/* 392 */       JsonInclude.Value incl = this._annotationIntrospector.findPropertyInclusion(this._classInfo);
/* 393 */       if (incl != null) {
/* 394 */         return defValue == null ? incl : defValue.withOverrides(incl);
/*     */       }
/*     */     }
/* 397 */     return defValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedMember findAnyGetter()
/*     */     throws IllegalArgumentException
/*     */   {
/* 409 */     AnnotatedMember anyGetter = this._propCollector == null ? null : this._propCollector.getAnyGetter();
/*     */     
/* 411 */     if (anyGetter != null)
/*     */     {
/*     */ 
/*     */ 
/* 415 */       Class<?> type = anyGetter.getRawType();
/* 416 */       if (!Map.class.isAssignableFrom(type)) {
/* 417 */         throw new IllegalArgumentException("Invalid 'any-getter' annotation on method " + anyGetter.getName() + "(): return type is not instance of java.util.Map");
/*     */       }
/*     */     }
/* 420 */     return anyGetter;
/*     */   }
/*     */   
/*     */   public AnnotatedMember findAnySetterField() throws IllegalArgumentException
/*     */   {
/* 425 */     AnnotatedMember anySetter = this._propCollector == null ? null : this._propCollector.getAnySetterField();
/* 426 */     if (anySetter != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 431 */       Class<?> type = anySetter.getRawType();
/* 432 */       if (!Map.class.isAssignableFrom(type)) {
/* 433 */         throw new IllegalArgumentException("Invalid 'any-setter' annotation on field " + anySetter.getName() + "(): type is not instance of java.util.Map");
/*     */       }
/*     */     }
/*     */     
/* 437 */     return anySetter;
/*     */   }
/*     */   
/*     */ 
/*     */   public Map<String, AnnotatedMember> findBackReferenceProperties()
/*     */   {
/* 443 */     HashMap<String, AnnotatedMember> result = null;
/*     */     
/*     */ 
/* 446 */     for (BeanPropertyDefinition property : _properties())
/*     */     {
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
/* 458 */       AnnotatedMember am = property.getMutator();
/* 459 */       if (am != null)
/*     */       {
/*     */ 
/* 462 */         AnnotationIntrospector.ReferenceProperty refDef = this._annotationIntrospector.findReferenceType(am);
/* 463 */         if ((refDef != null) && (refDef.isBackReference())) {
/* 464 */           if (result == null) {
/* 465 */             result = new HashMap();
/*     */           }
/* 467 */           String refName = refDef.getName();
/* 468 */           if (result.put(refName, am) != null)
/* 469 */             throw new IllegalArgumentException("Multiple back-reference properties with name '" + refName + "'");
/*     */         }
/*     */       }
/*     */     }
/* 473 */     return result;
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
/*     */   public List<AnnotatedMethod> getFactoryMethods()
/*     */   {
/* 486 */     List<AnnotatedMethod> candidates = this._classInfo.getStaticMethods();
/* 487 */     if (candidates.isEmpty()) {
/* 488 */       return candidates;
/*     */     }
/* 490 */     ArrayList<AnnotatedMethod> result = new ArrayList();
/* 491 */     for (AnnotatedMethod am : candidates) {
/* 492 */       if (isFactoryMethod(am)) {
/* 493 */         result.add(am);
/*     */       }
/*     */     }
/* 496 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public Constructor<?> findSingleArgConstructor(Class<?>... argTypes)
/*     */   {
/* 502 */     for (AnnotatedConstructor ac : this._classInfo.getConstructors())
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 507 */       if (ac.getParameterCount() == 1) {
/* 508 */         Class<?> actArg = ac.getRawParameterType(0);
/* 509 */         for (Class<?> expArg : argTypes) {
/* 510 */           if (expArg == actArg) {
/* 511 */             return ac.getAnnotated();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 516 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Method findFactoryMethod(Class<?>... expArgTypes)
/*     */   {
/* 523 */     for (AnnotatedMethod am : this._classInfo.getStaticMethods())
/*     */     {
/* 525 */       if ((isFactoryMethod(am)) && (am.getParameterCount() == 1))
/*     */       {
/* 527 */         Class<?> actualArgType = am.getRawParameterType(0);
/* 528 */         for (Class<?> expArgType : expArgTypes)
/*     */         {
/* 530 */           if (actualArgType.isAssignableFrom(expArgType)) {
/* 531 */             return am.getAnnotated();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 536 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isFactoryMethod(AnnotatedMethod am)
/*     */   {
/* 544 */     Class<?> rt = am.getRawReturnType();
/* 545 */     if (!getBeanClass().isAssignableFrom(rt)) {
/* 546 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 552 */     if (this._annotationIntrospector.hasCreatorAnnotation(am)) {
/* 553 */       return true;
/*     */     }
/* 555 */     String name = am.getName();
/*     */     
/* 557 */     if (("valueOf".equals(name)) && 
/* 558 */       (am.getParameterCount() == 1)) {
/* 559 */       return true;
/*     */     }
/*     */     
/*     */ 
/* 563 */     if (("fromString".equals(name)) && 
/* 564 */       (am.getParameterCount() == 1)) {
/* 565 */       Class<?> cls = am.getRawParameterType(0);
/* 566 */       if ((cls == String.class) || (CharSequence.class.isAssignableFrom(cls))) {
/* 567 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 571 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected PropertyName _findCreatorPropertyName(AnnotatedParameter param)
/*     */   {
/* 580 */     PropertyName name = this._annotationIntrospector.findNameForDeserialization(param);
/* 581 */     if ((name == null) || (name.isEmpty())) {
/* 582 */       String str = this._annotationIntrospector.findImplicitPropertyName(param);
/* 583 */       if ((str != null) && (!str.isEmpty())) {
/* 584 */         name = PropertyName.construct(str);
/*     */       }
/*     */     }
/* 587 */     return name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?> findPOJOBuilder()
/*     */   {
/* 598 */     return this._annotationIntrospector == null ? null : this._annotationIntrospector.findPOJOBuilder(this._classInfo);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonPOJOBuilder.Value findPOJOBuilderConfig()
/*     */   {
/* 605 */     return this._annotationIntrospector == null ? null : this._annotationIntrospector.findPOJOBuilderConfig(this._classInfo);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Converter<Object, Object> findDeserializationConverter()
/*     */   {
/* 612 */     if (this._annotationIntrospector == null) {
/* 613 */       return null;
/*     */     }
/* 615 */     return _createConverter(this._annotationIntrospector.findDeserializationConverter(this._classInfo));
/*     */   }
/*     */   
/*     */   public String findClassDescription()
/*     */   {
/* 620 */     return this._annotationIntrospector == null ? null : this._annotationIntrospector.findClassDescription(this._classInfo);
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
/*     */   @Deprecated
/*     */   public LinkedHashMap<String, AnnotatedField> _findPropertyFields(Collection<String> ignoredProperties, boolean forSerialization)
/*     */   {
/* 646 */     LinkedHashMap<String, AnnotatedField> results = new LinkedHashMap();
/* 647 */     for (BeanPropertyDefinition property : _properties()) {
/* 648 */       AnnotatedField f = property.getField();
/* 649 */       if (f != null) {
/* 650 */         String name = property.getName();
/* 651 */         if ((ignoredProperties == null) || 
/* 652 */           (!ignoredProperties.contains(name)))
/*     */         {
/*     */ 
/*     */ 
/* 656 */           results.put(name, f); }
/*     */       }
/*     */     }
/* 659 */     return results;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Converter<Object, Object> _createConverter(Object converterDef)
/*     */   {
/* 671 */     if (converterDef == null) {
/* 672 */       return null;
/*     */     }
/* 674 */     if ((converterDef instanceof Converter)) {
/* 675 */       return (Converter)converterDef;
/*     */     }
/* 677 */     if (!(converterDef instanceof Class)) {
/* 678 */       throw new IllegalStateException("AnnotationIntrospector returned Converter definition of type " + converterDef.getClass().getName() + "; expected type Converter or Class<Converter> instead");
/*     */     }
/*     */     
/* 681 */     Class<?> converterClass = (Class)converterDef;
/*     */     
/* 683 */     if ((converterClass == Converter.None.class) || (ClassUtil.isBogusClass(converterClass))) {
/* 684 */       return null;
/*     */     }
/* 686 */     if (!Converter.class.isAssignableFrom(converterClass)) {
/* 687 */       throw new IllegalStateException("AnnotationIntrospector returned Class " + converterClass.getName() + "; expected Class<Converter>");
/*     */     }
/*     */     
/* 690 */     HandlerInstantiator hi = this._config.getHandlerInstantiator();
/* 691 */     Converter<?, ?> conv = hi == null ? null : hi.converterInstance(this._config, this._classInfo, converterClass);
/* 692 */     if (conv == null) {
/* 693 */       conv = (Converter)ClassUtil.createInstance(converterClass, this._config.canOverrideAccessModifiers());
/*     */     }
/*     */     
/* 696 */     return conv;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\BasicBeanDescription.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */