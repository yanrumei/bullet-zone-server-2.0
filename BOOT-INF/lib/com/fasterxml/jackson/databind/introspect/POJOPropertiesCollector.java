/*      */ package com.fasterxml.jackson.databind.introspect;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonAnySetter;
/*      */ import com.fasterxml.jackson.annotation.JsonProperty.Access;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.MapperFeature;
/*      */ import com.fasterxml.jackson.databind.PropertyName;
/*      */ import com.fasterxml.jackson.databind.PropertyNamingStrategy;
/*      */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*      */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*      */ import com.fasterxml.jackson.databind.util.BeanUtil;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class POJOPropertiesCollector
/*      */ {
/*      */   protected final MapperConfig<?> _config;
/*      */   protected final boolean _forSerialization;
/*      */   protected final boolean _stdBeanNaming;
/*      */   protected final JavaType _type;
/*      */   protected final AnnotatedClass _classDef;
/*      */   protected final VisibilityChecker<?> _visibilityChecker;
/*      */   protected final AnnotationIntrospector _annotationIntrospector;
/*      */   protected final String _mutatorPrefix;
/*      */   protected boolean _collected;
/*      */   protected LinkedHashMap<String, POJOPropertyBuilder> _properties;
/*      */   protected LinkedList<POJOPropertyBuilder> _creatorProperties;
/*      */   protected LinkedList<AnnotatedMember> _anyGetters;
/*      */   protected LinkedList<AnnotatedMethod> _anySetters;
/*      */   protected LinkedList<AnnotatedMember> _anySetterField;
/*      */   protected LinkedList<AnnotatedMethod> _jsonValueGetters;
/*      */   protected HashSet<String> _ignoredPropertyNames;
/*      */   protected LinkedHashMap<Object, AnnotatedMember> _injectables;
/*      */   
/*      */   protected POJOPropertiesCollector(MapperConfig<?> config, boolean forSerialization, JavaType type, AnnotatedClass classDef, String mutatorPrefix)
/*      */   {
/*  119 */     this._config = config;
/*  120 */     this._stdBeanNaming = config.isEnabled(MapperFeature.USE_STD_BEAN_NAMING);
/*  121 */     this._forSerialization = forSerialization;
/*  122 */     this._type = type;
/*  123 */     this._classDef = classDef;
/*  124 */     this._mutatorPrefix = (mutatorPrefix == null ? "set" : mutatorPrefix);
/*  125 */     this._annotationIntrospector = (config.isAnnotationProcessingEnabled() ? this._config.getAnnotationIntrospector() : null);
/*      */     
/*  127 */     if (this._annotationIntrospector == null) {
/*  128 */       this._visibilityChecker = this._config.getDefaultVisibilityChecker();
/*      */     } else {
/*  130 */       this._visibilityChecker = this._annotationIntrospector.findAutoDetectVisibility(classDef, this._config.getDefaultVisibilityChecker());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public MapperConfig<?> getConfig()
/*      */   {
/*  142 */     return this._config;
/*      */   }
/*      */   
/*      */   public JavaType getType() {
/*  146 */     return this._type;
/*      */   }
/*      */   
/*      */   public AnnotatedClass getClassDef() {
/*  150 */     return this._classDef;
/*      */   }
/*      */   
/*      */   public AnnotationIntrospector getAnnotationIntrospector() {
/*  154 */     return this._annotationIntrospector;
/*      */   }
/*      */   
/*      */   public List<BeanPropertyDefinition> getProperties()
/*      */   {
/*  159 */     Map<String, POJOPropertyBuilder> props = getPropertyMap();
/*  160 */     return new ArrayList(props.values());
/*      */   }
/*      */   
/*      */   public Map<Object, AnnotatedMember> getInjectables() {
/*  164 */     if (!this._collected) {
/*  165 */       collectAll();
/*      */     }
/*  167 */     return this._injectables;
/*      */   }
/*      */   
/*      */   public AnnotatedMethod getJsonValueMethod()
/*      */   {
/*  172 */     if (!this._collected) {
/*  173 */       collectAll();
/*      */     }
/*      */     
/*  176 */     if (this._jsonValueGetters != null) {
/*  177 */       if (this._jsonValueGetters.size() > 1) {
/*  178 */         reportProblem("Multiple value properties defined (" + this._jsonValueGetters.get(0) + " vs " + this._jsonValueGetters.get(1) + ")");
/*      */       }
/*      */       
/*      */ 
/*  182 */       return (AnnotatedMethod)this._jsonValueGetters.get(0);
/*      */     }
/*  184 */     return null;
/*      */   }
/*      */   
/*      */   public AnnotatedMember getAnyGetter()
/*      */   {
/*  189 */     if (!this._collected) {
/*  190 */       collectAll();
/*      */     }
/*  192 */     if (this._anyGetters != null) {
/*  193 */       if (this._anyGetters.size() > 1) {
/*  194 */         reportProblem("Multiple 'any-getters' defined (" + this._anyGetters.get(0) + " vs " + this._anyGetters.get(1) + ")");
/*      */       }
/*      */       
/*  197 */       return (AnnotatedMember)this._anyGetters.getFirst();
/*      */     }
/*  199 */     return null;
/*      */   }
/*      */   
/*      */   public AnnotatedMember getAnySetterField()
/*      */   {
/*  204 */     if (!this._collected) {
/*  205 */       collectAll();
/*      */     }
/*  207 */     if (this._anySetterField != null) {
/*  208 */       if (this._anySetterField.size() > 1) {
/*  209 */         reportProblem("Multiple 'any-Setters' defined (" + this._anySetters.get(0) + " vs " + this._anySetterField.get(1) + ")");
/*      */       }
/*      */       
/*  212 */       return (AnnotatedMember)this._anySetterField.getFirst();
/*      */     }
/*  214 */     return null;
/*      */   }
/*      */   
/*      */   public AnnotatedMethod getAnySetterMethod()
/*      */   {
/*  219 */     if (!this._collected) {
/*  220 */       collectAll();
/*      */     }
/*  222 */     if (this._anySetters != null) {
/*  223 */       if (this._anySetters.size() > 1) {
/*  224 */         reportProblem("Multiple 'any-setters' defined (" + this._anySetters.get(0) + " vs " + this._anySetters.get(1) + ")");
/*      */       }
/*      */       
/*  227 */       return (AnnotatedMethod)this._anySetters.getFirst();
/*      */     }
/*  229 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<String> getIgnoredPropertyNames()
/*      */   {
/*  237 */     return this._ignoredPropertyNames;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectIdInfo getObjectIdInfo()
/*      */   {
/*  246 */     if (this._annotationIntrospector == null) {
/*  247 */       return null;
/*      */     }
/*  249 */     ObjectIdInfo info = this._annotationIntrospector.findObjectIdInfo(this._classDef);
/*  250 */     if (info != null) {
/*  251 */       info = this._annotationIntrospector.findObjectReferenceInfo(this._classDef, info);
/*      */     }
/*  253 */     return info;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<?> findPOJOBuilderClass()
/*      */   {
/*  261 */     return this._annotationIntrospector.findPOJOBuilder(this._classDef);
/*      */   }
/*      */   
/*      */   protected Map<String, POJOPropertyBuilder> getPropertyMap()
/*      */   {
/*  266 */     if (!this._collected) {
/*  267 */       collectAll();
/*      */     }
/*  269 */     return this._properties;
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
/*      */   public POJOPropertiesCollector collect()
/*      */   {
/*  289 */     return this;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void collectAll()
/*      */   {
/*  299 */     LinkedHashMap<String, POJOPropertyBuilder> props = new LinkedHashMap();
/*      */     
/*      */ 
/*  302 */     _addFields(props);
/*  303 */     _addMethods(props);
/*      */     
/*      */ 
/*  306 */     if (!this._classDef.isNonStaticInnerClass()) {
/*  307 */       _addCreators(props);
/*      */     }
/*  309 */     _addInjectables(props);
/*      */     
/*      */ 
/*      */ 
/*  313 */     _removeUnwantedProperties(props);
/*      */     
/*      */ 
/*  316 */     for (POJOPropertyBuilder property : props.values()) {
/*  317 */       property.mergeAnnotations(this._forSerialization);
/*      */     }
/*      */     
/*  320 */     _removeUnwantedAccessor(props);
/*      */     
/*      */ 
/*  323 */     _renameProperties(props);
/*      */     
/*      */ 
/*  326 */     PropertyNamingStrategy naming = _findNamingStrategy();
/*  327 */     if (naming != null) {
/*  328 */       _renameUsing(props, naming);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  335 */     for (POJOPropertyBuilder property : props.values()) {
/*  336 */       property.trimByVisibility();
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  342 */     if (this._config.isEnabled(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME)) {
/*  343 */       _renameWithWrappers(props);
/*      */     }
/*      */     
/*      */ 
/*  347 */     _sortProperties(props);
/*  348 */     this._properties = props;
/*  349 */     this._collected = true;
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
/*      */   protected void _addFields(Map<String, POJOPropertyBuilder> props)
/*      */   {
/*  363 */     AnnotationIntrospector ai = this._annotationIntrospector;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  368 */     boolean pruneFinalFields = (!this._forSerialization) && (!this._config.isEnabled(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS));
/*  369 */     boolean transientAsIgnoral = this._config.isEnabled(MapperFeature.PROPAGATE_TRANSIENT_MARKER);
/*      */     
/*  371 */     for (AnnotatedField f : this._classDef.fields()) {
/*  372 */       String implName = ai == null ? null : ai.findImplicitPropertyName(f);
/*  373 */       if (implName == null) {
/*  374 */         implName = f.getName();
/*      */       }
/*      */       
/*      */       PropertyName pn;
/*      */       PropertyName pn;
/*  379 */       if (ai == null) {
/*  380 */         pn = null; } else { PropertyName pn;
/*  381 */         if (this._forSerialization)
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  387 */           pn = ai.findNameForSerialization(f);
/*      */         } else
/*  389 */           pn = ai.findNameForDeserialization(f);
/*      */       }
/*  391 */       boolean hasName = pn != null;
/*  392 */       boolean nameExplicit = hasName;
/*      */       
/*  394 */       if ((nameExplicit) && (pn.isEmpty())) {
/*  395 */         pn = _propNameFromSimple(implName);
/*  396 */         nameExplicit = false;
/*      */       }
/*      */       
/*  399 */       boolean visible = pn != null;
/*  400 */       if (!visible) {
/*  401 */         visible = this._visibilityChecker.isFieldVisible(f);
/*      */       }
/*      */       
/*  404 */       boolean ignored = (ai != null) && (ai.hasIgnoreMarker(f));
/*      */       
/*      */ 
/*  407 */       if (f.isTransient())
/*      */       {
/*      */ 
/*  410 */         if (!hasName) {
/*  411 */           visible = false;
/*  412 */           if (transientAsIgnoral) {
/*  413 */             ignored = true;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  422 */       if ((!pruneFinalFields) || (pn != null) || (ignored) || (!Modifier.isFinal(f.getModifiers())))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  427 */         if (f.hasAnnotation(JsonAnySetter.class)) {
/*  428 */           if (this._anySetterField == null) {
/*  429 */             this._anySetterField = new LinkedList();
/*      */           }
/*  431 */           this._anySetterField.add(f);
/*      */         }
/*  433 */         _property(props, implName).addField(f, pn, nameExplicit, visible, ignored);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addCreators(Map<String, POJOPropertyBuilder> props)
/*      */   {
/*  443 */     if (this._annotationIntrospector == null) {
/*  444 */       return;
/*      */     }
/*  446 */     for (AnnotatedConstructor ctor : this._classDef.getConstructors()) {
/*  447 */       if (this._creatorProperties == null) {
/*  448 */         this._creatorProperties = new LinkedList();
/*      */       }
/*  450 */       int i = 0; for (int len = ctor.getParameterCount(); i < len; i++) {
/*  451 */         _addCreatorParam(props, ctor.getParameter(i));
/*      */       }
/*      */     }
/*  454 */     for (AnnotatedMethod factory : this._classDef.getStaticMethods()) {
/*  455 */       if (this._creatorProperties == null) {
/*  456 */         this._creatorProperties = new LinkedList();
/*      */       }
/*  458 */       int i = 0; for (int len = factory.getParameterCount(); i < len; i++) {
/*  459 */         _addCreatorParam(props, factory.getParameter(i));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addCreatorParam(Map<String, POJOPropertyBuilder> props, AnnotatedParameter param)
/*      */   {
/*  471 */     String impl = this._annotationIntrospector.findImplicitPropertyName(param);
/*  472 */     if (impl == null) {
/*  473 */       impl = "";
/*      */     }
/*  475 */     PropertyName pn = this._annotationIntrospector.findNameForDeserialization(param);
/*  476 */     boolean expl = (pn != null) && (!pn.isEmpty());
/*  477 */     if (!expl) {
/*  478 */       if (impl.isEmpty())
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  483 */         return;
/*      */       }
/*      */       
/*  486 */       if (!this._annotationIntrospector.hasCreatorAnnotation(param.getOwner())) {
/*  487 */         return;
/*      */       }
/*  489 */       pn = PropertyName.construct(impl);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  499 */     POJOPropertyBuilder prop = (expl) && (impl.isEmpty()) ? _property(props, pn) : _property(props, impl);
/*      */     
/*  501 */     prop.addCtor(param, pn, expl, true, false);
/*  502 */     this._creatorProperties.add(prop);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addMethods(Map<String, POJOPropertyBuilder> props)
/*      */   {
/*  510 */     AnnotationIntrospector ai = this._annotationIntrospector;
/*      */     
/*  512 */     for (AnnotatedMethod m : this._classDef.memberMethods())
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  518 */       int argCount = m.getParameterCount();
/*  519 */       if (argCount == 0) {
/*  520 */         _addGetterMethod(props, m, ai);
/*  521 */       } else if (argCount == 1) {
/*  522 */         _addSetterMethod(props, m, ai);
/*  523 */       } else if ((argCount == 2) && 
/*  524 */         (ai != null) && (ai.hasAnySetterAnnotation(m))) {
/*  525 */         if (this._anySetters == null) {
/*  526 */           this._anySetters = new LinkedList();
/*      */         }
/*  528 */         this._anySetters.add(m);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addGetterMethod(Map<String, POJOPropertyBuilder> props, AnnotatedMethod m, AnnotationIntrospector ai)
/*      */   {
/*  538 */     if (!m.hasReturnType()) {
/*  539 */       return;
/*      */     }
/*      */     
/*      */ 
/*  543 */     if (ai != null) {
/*  544 */       if (ai.hasAnyGetterAnnotation(m)) {
/*  545 */         if (this._anyGetters == null) {
/*  546 */           this._anyGetters = new LinkedList();
/*      */         }
/*  548 */         this._anyGetters.add(m);
/*  549 */         return;
/*      */       }
/*      */       
/*  552 */       if (ai.hasAsValueAnnotation(m)) {
/*  553 */         if (this._jsonValueGetters == null) {
/*  554 */           this._jsonValueGetters = new LinkedList();
/*      */         }
/*  556 */         this._jsonValueGetters.add(m);
/*  557 */         return;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  563 */     PropertyName pn = ai == null ? null : ai.findNameForSerialization(m);
/*  564 */     boolean nameExplicit = pn != null;
/*      */     boolean visible;
/*  566 */     String implName; boolean visible; if (!nameExplicit) {
/*  567 */       String implName = ai == null ? null : ai.findImplicitPropertyName(m);
/*  568 */       if (implName == null)
/*  569 */         implName = BeanUtil.okNameForRegularGetter(m, m.getName(), this._stdBeanNaming);
/*      */       boolean visible;
/*  571 */       if (implName == null) {
/*  572 */         implName = BeanUtil.okNameForIsGetter(m, m.getName(), this._stdBeanNaming);
/*  573 */         if (implName == null) {
/*  574 */           return;
/*      */         }
/*  576 */         visible = this._visibilityChecker.isIsGetterVisible(m);
/*      */       } else {
/*  578 */         visible = this._visibilityChecker.isGetterVisible(m);
/*      */       }
/*      */     }
/*      */     else {
/*  582 */       implName = ai == null ? null : ai.findImplicitPropertyName(m);
/*  583 */       if (implName == null) {
/*  584 */         implName = BeanUtil.okNameForGetter(m, this._stdBeanNaming);
/*      */       }
/*      */       
/*  587 */       if (implName == null) {
/*  588 */         implName = m.getName();
/*      */       }
/*  590 */       if (pn.isEmpty())
/*      */       {
/*  592 */         pn = _propNameFromSimple(implName);
/*  593 */         nameExplicit = false;
/*      */       }
/*  595 */       visible = true;
/*      */     }
/*  597 */     boolean ignore = ai == null ? false : ai.hasIgnoreMarker(m);
/*  598 */     _property(props, implName).addGetter(m, pn, nameExplicit, visible, ignore);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addSetterMethod(Map<String, POJOPropertyBuilder> props, AnnotatedMethod m, AnnotationIntrospector ai)
/*      */   {
/*  606 */     PropertyName pn = ai == null ? null : ai.findNameForDeserialization(m);
/*  607 */     boolean nameExplicit = pn != null;
/*  608 */     boolean visible; String implName; boolean visible; if (!nameExplicit) {
/*  609 */       String implName = ai == null ? null : ai.findImplicitPropertyName(m);
/*  610 */       if (implName == null) {
/*  611 */         implName = BeanUtil.okNameForMutator(m, this._mutatorPrefix, this._stdBeanNaming);
/*      */       }
/*  613 */       if (implName == null) {
/*  614 */         return;
/*      */       }
/*  616 */       visible = this._visibilityChecker.isSetterVisible(m);
/*      */     }
/*      */     else {
/*  619 */       implName = ai == null ? null : ai.findImplicitPropertyName(m);
/*  620 */       if (implName == null) {
/*  621 */         implName = BeanUtil.okNameForMutator(m, this._mutatorPrefix, this._stdBeanNaming);
/*      */       }
/*      */       
/*  624 */       if (implName == null) {
/*  625 */         implName = m.getName();
/*      */       }
/*  627 */       if (pn.isEmpty())
/*      */       {
/*  629 */         pn = _propNameFromSimple(implName);
/*  630 */         nameExplicit = false;
/*      */       }
/*  632 */       visible = true;
/*      */     }
/*  634 */     boolean ignore = ai == null ? false : ai.hasIgnoreMarker(m);
/*  635 */     _property(props, implName).addSetter(m, pn, nameExplicit, visible, ignore);
/*      */   }
/*      */   
/*      */   protected void _addInjectables(Map<String, POJOPropertyBuilder> props)
/*      */   {
/*  640 */     AnnotationIntrospector ai = this._annotationIntrospector;
/*  641 */     if (ai == null) {
/*  642 */       return;
/*      */     }
/*      */     
/*      */ 
/*  646 */     for (AnnotatedField f : this._classDef.fields()) {
/*  647 */       _doAddInjectable(ai.findInjectableValueId(f), f);
/*      */     }
/*      */     
/*  650 */     for (AnnotatedMethod m : this._classDef.memberMethods())
/*      */     {
/*      */ 
/*      */ 
/*  654 */       if (m.getParameterCount() == 1)
/*      */       {
/*      */ 
/*  657 */         _doAddInjectable(ai.findInjectableValueId(m), m);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void _doAddInjectable(Object id, AnnotatedMember m) {
/*  663 */     if (id == null) {
/*  664 */       return;
/*      */     }
/*  666 */     if (this._injectables == null) {
/*  667 */       this._injectables = new LinkedHashMap();
/*      */     }
/*  669 */     AnnotatedMember prev = (AnnotatedMember)this._injectables.put(id, m);
/*  670 */     if (prev != null) {
/*  671 */       String type = id.getClass().getName();
/*  672 */       throw new IllegalArgumentException("Duplicate injectable value with id '" + String.valueOf(id) + "' (of type " + type + ")");
/*      */     }
/*      */   }
/*      */   
/*      */   private PropertyName _propNameFromSimple(String simpleName)
/*      */   {
/*  678 */     return PropertyName.construct(simpleName, null);
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
/*      */   protected void _removeUnwantedProperties(Map<String, POJOPropertyBuilder> props)
/*      */   {
/*  693 */     Iterator<POJOPropertyBuilder> it = props.values().iterator();
/*  694 */     while (it.hasNext()) {
/*  695 */       POJOPropertyBuilder prop = (POJOPropertyBuilder)it.next();
/*      */       
/*      */ 
/*  698 */       if (!prop.anyVisible()) {
/*  699 */         it.remove();
/*      */ 
/*      */ 
/*      */       }
/*  703 */       else if (prop.anyIgnorals())
/*      */       {
/*  705 */         if (!prop.isExplicitlyIncluded()) {
/*  706 */           it.remove();
/*  707 */           _collectIgnorals(prop.getName());
/*      */         }
/*      */         else
/*      */         {
/*  711 */           prop.removeIgnored();
/*  712 */           if ((!this._forSerialization) && (!prop.couldDeserialize())) {
/*  713 */             _collectIgnorals(prop.getName());
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _removeUnwantedAccessor(Map<String, POJOPropertyBuilder> props)
/*      */   {
/*  726 */     boolean inferMutators = this._config.isEnabled(MapperFeature.INFER_PROPERTY_MUTATORS);
/*  727 */     Iterator<POJOPropertyBuilder> it = props.values().iterator();
/*      */     
/*  729 */     while (it.hasNext()) {
/*  730 */       POJOPropertyBuilder prop = (POJOPropertyBuilder)it.next();
/*      */       
/*  732 */       JsonProperty.Access acc = prop.removeNonVisible(inferMutators);
/*  733 */       if ((!this._forSerialization) && (acc == JsonProperty.Access.READ_ONLY)) {
/*  734 */         _collectIgnorals(prop.getName());
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _collectIgnorals(String name)
/*      */   {
/*  746 */     if (!this._forSerialization) {
/*  747 */       if (this._ignoredPropertyNames == null) {
/*  748 */         this._ignoredPropertyNames = new HashSet();
/*      */       }
/*  750 */       this._ignoredPropertyNames.add(name);
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
/*      */   protected void _renameProperties(Map<String, POJOPropertyBuilder> props)
/*      */   {
/*  763 */     Iterator<Map.Entry<String, POJOPropertyBuilder>> it = props.entrySet().iterator();
/*  764 */     LinkedList<POJOPropertyBuilder> renamed = null;
/*  765 */     while (it.hasNext()) {
/*  766 */       Map.Entry<String, POJOPropertyBuilder> entry = (Map.Entry)it.next();
/*  767 */       POJOPropertyBuilder prop = (POJOPropertyBuilder)entry.getValue();
/*      */       
/*  769 */       Collection<PropertyName> l = prop.findExplicitNames();
/*      */       
/*      */ 
/*  772 */       if (!l.isEmpty())
/*      */       {
/*      */ 
/*  775 */         it.remove();
/*  776 */         if (renamed == null) {
/*  777 */           renamed = new LinkedList();
/*      */         }
/*      */         
/*  780 */         if (l.size() == 1) {
/*  781 */           PropertyName n = (PropertyName)l.iterator().next();
/*  782 */           renamed.add(prop.withName(n));
/*      */         }
/*      */         else
/*      */         {
/*  786 */           renamed.addAll(prop.explode(l));
/*      */         }
/*      */       }
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
/*      */ 
/*  802 */     if (renamed != null) {
/*  803 */       for (POJOPropertyBuilder prop : renamed) {
/*  804 */         String name = prop.getName();
/*  805 */         POJOPropertyBuilder old = (POJOPropertyBuilder)props.get(name);
/*  806 */         if (old == null) {
/*  807 */           props.put(name, prop);
/*      */         } else {
/*  809 */           old.addAll(prop);
/*      */         }
/*      */         
/*  812 */         _updateCreatorProperty(prop, this._creatorProperties);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void _renameUsing(Map<String, POJOPropertyBuilder> propMap, PropertyNamingStrategy naming)
/*      */   {
/*  820 */     POJOPropertyBuilder[] props = (POJOPropertyBuilder[])propMap.values().toArray(new POJOPropertyBuilder[propMap.size()]);
/*  821 */     propMap.clear();
/*  822 */     for (POJOPropertyBuilder prop : props) {
/*  823 */       PropertyName fullName = prop.getFullName();
/*  824 */       String rename = null;
/*      */       
/*      */ 
/*  827 */       if ((!prop.isExplicitlyNamed()) || (this._config.isEnabled(MapperFeature.ALLOW_EXPLICIT_PROPERTY_RENAMING))) {
/*  828 */         if (this._forSerialization) {
/*  829 */           if (prop.hasGetter()) {
/*  830 */             rename = naming.nameForGetterMethod(this._config, prop.getGetter(), fullName.getSimpleName());
/*  831 */           } else if (prop.hasField()) {
/*  832 */             rename = naming.nameForField(this._config, prop.getField(), fullName.getSimpleName());
/*      */           }
/*      */         }
/*  835 */         else if (prop.hasSetter()) {
/*  836 */           rename = naming.nameForSetterMethod(this._config, prop.getSetter(), fullName.getSimpleName());
/*  837 */         } else if (prop.hasConstructorParameter()) {
/*  838 */           rename = naming.nameForConstructorParameter(this._config, prop.getConstructorParameter(), fullName.getSimpleName());
/*  839 */         } else if (prop.hasField()) {
/*  840 */           rename = naming.nameForField(this._config, prop.getField(), fullName.getSimpleName());
/*  841 */         } else if (prop.hasGetter())
/*      */         {
/*      */ 
/*      */ 
/*  845 */           rename = naming.nameForGetterMethod(this._config, prop.getGetter(), fullName.getSimpleName());
/*      */         }
/*      */       }
/*      */       String simpleName;
/*      */       String simpleName;
/*  850 */       if ((rename != null) && (!fullName.hasSimpleName(rename))) {
/*  851 */         prop = prop.withSimpleName(rename);
/*  852 */         simpleName = rename;
/*      */       } else {
/*  854 */         simpleName = fullName.getSimpleName();
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*  859 */       POJOPropertyBuilder old = (POJOPropertyBuilder)propMap.get(simpleName);
/*  860 */       if (old == null) {
/*  861 */         propMap.put(simpleName, prop);
/*      */       } else {
/*  863 */         old.addAll(prop);
/*      */       }
/*      */       
/*  866 */       _updateCreatorProperty(prop, this._creatorProperties);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _renameWithWrappers(Map<String, POJOPropertyBuilder> props)
/*      */   {
/*  875 */     Iterator<Map.Entry<String, POJOPropertyBuilder>> it = props.entrySet().iterator();
/*  876 */     LinkedList<POJOPropertyBuilder> renamed = null;
/*  877 */     while (it.hasNext()) {
/*  878 */       Map.Entry<String, POJOPropertyBuilder> entry = (Map.Entry)it.next();
/*  879 */       POJOPropertyBuilder prop = (POJOPropertyBuilder)entry.getValue();
/*  880 */       AnnotatedMember member = prop.getPrimaryMember();
/*  881 */       if (member != null)
/*      */       {
/*      */ 
/*  884 */         PropertyName wrapperName = this._annotationIntrospector.findWrapperName(member);
/*      */         
/*      */ 
/*      */ 
/*  888 */         if ((wrapperName != null) && (wrapperName.hasSimpleName()))
/*      */         {
/*      */ 
/*  891 */           if (!wrapperName.equals(prop.getFullName())) {
/*  892 */             if (renamed == null) {
/*  893 */               renamed = new LinkedList();
/*      */             }
/*  895 */             prop = prop.withName(wrapperName);
/*  896 */             renamed.add(prop);
/*  897 */             it.remove();
/*      */           } }
/*      */       }
/*      */     }
/*  901 */     if (renamed != null) {
/*  902 */       for (POJOPropertyBuilder prop : renamed) {
/*  903 */         String name = prop.getName();
/*  904 */         POJOPropertyBuilder old = (POJOPropertyBuilder)props.get(name);
/*  905 */         if (old == null) {
/*  906 */           props.put(name, prop);
/*      */         } else {
/*  908 */           old.addAll(prop);
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
/*      */   protected void _sortProperties(Map<String, POJOPropertyBuilder> props)
/*      */   {
/*  926 */     AnnotationIntrospector intr = this._annotationIntrospector;
/*  927 */     Boolean alpha = intr == null ? null : intr.findSerializationSortAlphabetically(this._classDef);
/*      */     boolean sort;
/*      */     boolean sort;
/*  930 */     if (alpha == null) {
/*  931 */       sort = this._config.shouldSortPropertiesAlphabetically();
/*      */     } else {
/*  933 */       sort = alpha.booleanValue();
/*      */     }
/*  935 */     String[] propertyOrder = intr == null ? null : intr.findSerializationPropertyOrder(this._classDef);
/*      */     
/*      */ 
/*  938 */     if ((!sort) && (this._creatorProperties == null) && (propertyOrder == null)) {
/*  939 */       return;
/*      */     }
/*  941 */     int size = props.size();
/*      */     Map<String, POJOPropertyBuilder> all;
/*      */     Map<String, POJOPropertyBuilder> all;
/*  944 */     if (sort) {
/*  945 */       all = new TreeMap();
/*      */     } else {
/*  947 */       all = new LinkedHashMap(size + size);
/*      */     }
/*      */     
/*  950 */     for (POJOPropertyBuilder prop : props.values()) {
/*  951 */       all.put(prop.getName(), prop);
/*      */     }
/*  953 */     Map<String, POJOPropertyBuilder> ordered = new LinkedHashMap(size + size);
/*      */     
/*  955 */     if (propertyOrder != null) {
/*  956 */       for (String name : propertyOrder) {
/*  957 */         POJOPropertyBuilder w = (POJOPropertyBuilder)all.get(name);
/*  958 */         if (w == null) {
/*  959 */           for (POJOPropertyBuilder prop : props.values()) {
/*  960 */             if (name.equals(prop.getInternalName())) {
/*  961 */               w = prop;
/*      */               
/*  963 */               name = prop.getName();
/*  964 */               break;
/*      */             }
/*      */           }
/*      */         }
/*  968 */         if (w != null) {
/*  969 */           ordered.put(name, w);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  974 */     if (this._creatorProperties != null)
/*      */     {
/*      */       Collection<POJOPropertyBuilder> cr;
/*      */       
/*      */ 
/*      */       Collection<POJOPropertyBuilder> cr;
/*      */       
/*  981 */       if (sort) {
/*  982 */         TreeMap<String, POJOPropertyBuilder> sorted = new TreeMap();
/*      */         
/*  984 */         for (POJOPropertyBuilder prop : this._creatorProperties) {
/*  985 */           sorted.put(prop.getName(), prop);
/*      */         }
/*  987 */         cr = sorted.values();
/*      */       } else {
/*  989 */         cr = this._creatorProperties;
/*      */       }
/*  991 */       for (POJOPropertyBuilder prop : cr)
/*      */       {
/*      */ 
/*  994 */         String name = prop.getName();
/*  995 */         if (all.containsKey(name)) {
/*  996 */           ordered.put(name, prop);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1001 */     ordered.putAll(all);
/*      */     
/* 1003 */     props.clear();
/* 1004 */     props.putAll(ordered);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void reportProblem(String msg)
/*      */   {
/* 1014 */     throw new IllegalArgumentException("Problem with definition of " + this._classDef + ": " + msg);
/*      */   }
/*      */   
/*      */   protected POJOPropertyBuilder _property(Map<String, POJOPropertyBuilder> props, PropertyName name)
/*      */   {
/* 1019 */     return _property(props, name.getSimpleName());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected POJOPropertyBuilder _property(Map<String, POJOPropertyBuilder> props, String implName)
/*      */   {
/* 1026 */     POJOPropertyBuilder prop = (POJOPropertyBuilder)props.get(implName);
/* 1027 */     if (prop == null) {
/* 1028 */       prop = new POJOPropertyBuilder(this._config, this._annotationIntrospector, this._forSerialization, PropertyName.construct(implName));
/*      */       
/* 1030 */       props.put(implName, prop);
/*      */     }
/* 1032 */     return prop;
/*      */   }
/*      */   
/*      */   private PropertyNamingStrategy _findNamingStrategy()
/*      */   {
/* 1037 */     Object namingDef = this._annotationIntrospector == null ? null : this._annotationIntrospector.findNamingStrategy(this._classDef);
/*      */     
/* 1039 */     if (namingDef == null) {
/* 1040 */       return this._config.getPropertyNamingStrategy();
/*      */     }
/* 1042 */     if ((namingDef instanceof PropertyNamingStrategy)) {
/* 1043 */       return (PropertyNamingStrategy)namingDef;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1048 */     if (!(namingDef instanceof Class)) {
/* 1049 */       throw new IllegalStateException("AnnotationIntrospector returned PropertyNamingStrategy definition of type " + namingDef.getClass().getName() + "; expected type PropertyNamingStrategy or Class<PropertyNamingStrategy> instead");
/*      */     }
/*      */     
/* 1052 */     Class<?> namingClass = (Class)namingDef;
/*      */     
/* 1054 */     if (namingClass == PropertyNamingStrategy.class) {
/* 1055 */       return null;
/*      */     }
/*      */     
/* 1058 */     if (!PropertyNamingStrategy.class.isAssignableFrom(namingClass)) {
/* 1059 */       throw new IllegalStateException("AnnotationIntrospector returned Class " + namingClass.getName() + "; expected Class<PropertyNamingStrategy>");
/*      */     }
/*      */     
/* 1062 */     HandlerInstantiator hi = this._config.getHandlerInstantiator();
/* 1063 */     if (hi != null) {
/* 1064 */       PropertyNamingStrategy pns = hi.namingStrategyInstance(this._config, this._classDef, namingClass);
/* 1065 */       if (pns != null) {
/* 1066 */         return pns;
/*      */       }
/*      */     }
/* 1069 */     return (PropertyNamingStrategy)ClassUtil.createInstance(namingClass, this._config.canOverrideAccessModifiers());
/*      */   }
/*      */   
/*      */   protected void _updateCreatorProperty(POJOPropertyBuilder prop, List<POJOPropertyBuilder> creatorProperties)
/*      */   {
/* 1074 */     if (creatorProperties != null) {
/* 1075 */       int i = 0; for (int len = creatorProperties.size(); i < len; i++) {
/* 1076 */         if (((POJOPropertyBuilder)creatorProperties.get(i)).getInternalName().equals(prop.getInternalName())) {
/* 1077 */           creatorProperties.set(i, prop);
/* 1078 */           break;
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\POJOPropertiesCollector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */