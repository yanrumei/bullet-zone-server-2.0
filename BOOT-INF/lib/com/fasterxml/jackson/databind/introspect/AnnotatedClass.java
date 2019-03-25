/*      */ package com.fasterxml.jackson.databind.introspect;
/*      */ 
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.JavaType;
/*      */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*      */ import com.fasterxml.jackson.databind.type.TypeBindings;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil.Ctor;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ 
/*      */ public final class AnnotatedClass extends Annotated implements TypeResolutionContext
/*      */ {
/*   22 */   private static final AnnotationMap[] NO_ANNOTATION_MAPS = new AnnotationMap[0];
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final JavaType _type;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Class<?> _class;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final TypeBindings _bindings;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final List<JavaType> _superTypes;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final AnnotationIntrospector _annotationIntrospector;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final TypeFactory _typeFactory;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final ClassIntrospector.MixInResolver _mixInResolver;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final Class<?> _primaryMixIn;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected final AnnotationMap _classAnnotations;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   97 */   protected boolean _creatorsResolved = false;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected AnnotatedConstructor _defaultConstructor;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected List<AnnotatedConstructor> _constructors;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected List<AnnotatedMethod> _creatorMethods;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected AnnotatedMethodMap _memberMethods;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected List<AnnotatedField> _fields;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected transient Boolean _nonStaticInnerClass;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private AnnotatedClass(JavaType type, Class<?> rawType, TypeBindings bindings, List<JavaType> superTypes, AnnotationIntrospector aintr, ClassIntrospector.MixInResolver mir, TypeFactory tf)
/*      */   {
/*  149 */     this._type = type;
/*  150 */     this._class = rawType;
/*  151 */     this._bindings = bindings;
/*  152 */     this._superTypes = superTypes;
/*  153 */     this._annotationIntrospector = aintr;
/*  154 */     this._typeFactory = tf;
/*  155 */     this._mixInResolver = mir;
/*  156 */     this._primaryMixIn = (this._mixInResolver == null ? null : this._mixInResolver.findMixInClassFor(this._class));
/*      */     
/*  158 */     this._classAnnotations = _resolveClassAnnotations();
/*      */   }
/*      */   
/*      */   private AnnotatedClass(AnnotatedClass base, AnnotationMap clsAnn) {
/*  162 */     this._type = base._type;
/*  163 */     this._class = base._class;
/*  164 */     this._bindings = base._bindings;
/*  165 */     this._superTypes = base._superTypes;
/*  166 */     this._annotationIntrospector = base._annotationIntrospector;
/*  167 */     this._typeFactory = base._typeFactory;
/*  168 */     this._mixInResolver = base._mixInResolver;
/*  169 */     this._primaryMixIn = base._primaryMixIn;
/*  170 */     this._classAnnotations = clsAnn;
/*      */   }
/*      */   
/*      */   public AnnotatedClass withAnnotations(AnnotationMap ann)
/*      */   {
/*  175 */     return new AnnotatedClass(this, ann);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static AnnotatedClass construct(JavaType type, MapperConfig<?> config)
/*      */   {
/*  186 */     AnnotationIntrospector intr = config.isAnnotationProcessingEnabled() ? config.getAnnotationIntrospector() : null;
/*      */     
/*  188 */     Class<?> raw = type.getRawClass();
/*  189 */     return new AnnotatedClass(type, raw, type.getBindings(), ClassUtil.findSuperTypes(type, null, false), intr, config, config.getTypeFactory());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static AnnotatedClass construct(JavaType type, MapperConfig<?> config, ClassIntrospector.MixInResolver mir)
/*      */   {
/*  200 */     AnnotationIntrospector intr = config.isAnnotationProcessingEnabled() ? config.getAnnotationIntrospector() : null;
/*      */     
/*  202 */     Class<?> raw = type.getRawClass();
/*  203 */     return new AnnotatedClass(type, raw, type.getBindings(), ClassUtil.findSuperTypes(type, null, false), intr, mir, config.getTypeFactory());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public static AnnotatedClass constructWithoutSuperTypes(Class<?> cls, MapperConfig<?> config)
/*      */   {
/*  215 */     if (config == null) {
/*  216 */       return new AnnotatedClass(null, cls, TypeBindings.emptyBindings(), Collections.emptyList(), null, null, null);
/*      */     }
/*      */     
/*  219 */     AnnotationIntrospector intr = config.isAnnotationProcessingEnabled() ? config.getAnnotationIntrospector() : null;
/*      */     
/*  221 */     return new AnnotatedClass(null, cls, TypeBindings.emptyBindings(), Collections.emptyList(), intr, config, config.getTypeFactory());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public static AnnotatedClass constructWithoutSuperTypes(Class<?> cls, MapperConfig<?> config, ClassIntrospector.MixInResolver mir)
/*      */   {
/*  228 */     if (config == null) {
/*  229 */       return new AnnotatedClass(null, cls, TypeBindings.emptyBindings(), Collections.emptyList(), null, null, null);
/*      */     }
/*      */     
/*  232 */     AnnotationIntrospector intr = config.isAnnotationProcessingEnabled() ? config.getAnnotationIntrospector() : null;
/*      */     
/*  234 */     return new AnnotatedClass(null, cls, TypeBindings.emptyBindings(), Collections.emptyList(), intr, mir, config.getTypeFactory());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType resolveType(java.lang.reflect.Type type)
/*      */   {
/*  246 */     return this._typeFactory.constructType(type, this._bindings);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<?> getAnnotated()
/*      */   {
/*  256 */     return this._class;
/*      */   }
/*      */   
/*  259 */   public int getModifiers() { return this._class.getModifiers(); }
/*      */   
/*      */   public String getName() {
/*  262 */     return this._class.getName();
/*      */   }
/*      */   
/*      */   public <A extends Annotation> A getAnnotation(Class<A> acls) {
/*  266 */     return this._classAnnotations.get(acls);
/*      */   }
/*      */   
/*      */   public boolean hasAnnotation(Class<?> acls)
/*      */   {
/*  271 */     return this._classAnnotations.has(acls);
/*      */   }
/*      */   
/*      */   public boolean hasOneOf(Class<? extends Annotation>[] annoClasses)
/*      */   {
/*  276 */     return this._classAnnotations.hasOneOf(annoClasses);
/*      */   }
/*      */   
/*      */   public Class<?> getRawType()
/*      */   {
/*  281 */     return this._class;
/*      */   }
/*      */   
/*      */   public Iterable<Annotation> annotations()
/*      */   {
/*  286 */     return this._classAnnotations.annotations();
/*      */   }
/*      */   
/*      */   protected AnnotationMap getAllAnnotations()
/*      */   {
/*  291 */     return this._classAnnotations;
/*      */   }
/*      */   
/*      */   public JavaType getType()
/*      */   {
/*  296 */     return this._type;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public com.fasterxml.jackson.databind.util.Annotations getAnnotations()
/*      */   {
/*  306 */     return this._classAnnotations;
/*      */   }
/*      */   
/*      */   public boolean hasAnnotations() {
/*  310 */     return this._classAnnotations.size() > 0;
/*      */   }
/*      */   
/*      */   public AnnotatedConstructor getDefaultConstructor()
/*      */   {
/*  315 */     if (!this._creatorsResolved) {
/*  316 */       resolveCreators();
/*      */     }
/*  318 */     return this._defaultConstructor;
/*      */   }
/*      */   
/*      */   public List<AnnotatedConstructor> getConstructors()
/*      */   {
/*  323 */     if (!this._creatorsResolved) {
/*  324 */       resolveCreators();
/*      */     }
/*  326 */     return this._constructors;
/*      */   }
/*      */   
/*      */   public List<AnnotatedMethod> getStaticMethods()
/*      */   {
/*  331 */     if (!this._creatorsResolved) {
/*  332 */       resolveCreators();
/*      */     }
/*  334 */     return this._creatorMethods;
/*      */   }
/*      */   
/*      */   public Iterable<AnnotatedMethod> memberMethods()
/*      */   {
/*  339 */     if (this._memberMethods == null) {
/*  340 */       resolveMemberMethods();
/*      */     }
/*  342 */     return this._memberMethods;
/*      */   }
/*      */   
/*      */   public int getMemberMethodCount()
/*      */   {
/*  347 */     if (this._memberMethods == null) {
/*  348 */       resolveMemberMethods();
/*      */     }
/*  350 */     return this._memberMethods.size();
/*      */   }
/*      */   
/*      */   public AnnotatedMethod findMethod(String name, Class<?>[] paramTypes)
/*      */   {
/*  355 */     if (this._memberMethods == null) {
/*  356 */       resolveMemberMethods();
/*      */     }
/*  358 */     return this._memberMethods.find(name, paramTypes);
/*      */   }
/*      */   
/*      */   public int getFieldCount() {
/*  362 */     if (this._fields == null) {
/*  363 */       resolveFields();
/*      */     }
/*  365 */     return this._fields.size();
/*      */   }
/*      */   
/*      */   public Iterable<AnnotatedField> fields()
/*      */   {
/*  370 */     if (this._fields == null) {
/*  371 */       resolveFields();
/*      */     }
/*  373 */     return this._fields;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isNonStaticInnerClass()
/*      */   {
/*  381 */     Boolean B = this._nonStaticInnerClass;
/*  382 */     if (B == null) {
/*  383 */       this._nonStaticInnerClass = (B = Boolean.valueOf(ClassUtil.isNonStaticInnerClass(this._class)));
/*      */     }
/*  385 */     return B.booleanValue();
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
/*      */   private AnnotationMap _resolveClassAnnotations()
/*      */   {
/*  401 */     AnnotationMap ca = new AnnotationMap();
/*      */     
/*  403 */     if (this._annotationIntrospector != null)
/*      */     {
/*  405 */       if (this._primaryMixIn != null) {
/*  406 */         _addClassMixIns(ca, this._class, this._primaryMixIn);
/*      */       }
/*      */       
/*  409 */       _addAnnotationsIfNotPresent(ca, ClassUtil.findClassAnnotations(this._class));
/*      */       
/*      */ 
/*      */ 
/*  413 */       for (JavaType type : this._superTypes)
/*      */       {
/*  415 */         _addClassMixIns(ca, type);
/*  416 */         _addAnnotationsIfNotPresent(ca, ClassUtil.findClassAnnotations(type.getRawClass()));
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  426 */       _addClassMixIns(ca, Object.class);
/*      */     }
/*  428 */     return ca;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void resolveCreators()
/*      */   {
/*  438 */     TypeResolutionContext typeContext = this;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  445 */     List<AnnotatedConstructor> constructors = null;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  452 */     if (!this._type.isEnumType()) {
/*  453 */       ClassUtil.Ctor[] declaredCtors = ClassUtil.getConstructors(this._class);
/*  454 */       for (ClassUtil.Ctor ctor : declaredCtors) {
/*  455 */         if (_isIncludableConstructor(ctor.getConstructor())) {
/*  456 */           if (ctor.getParamCount() == 0) {
/*  457 */             this._defaultConstructor = _constructDefaultConstructor(ctor, typeContext);
/*      */           } else {
/*  459 */             if (constructors == null) {
/*  460 */               constructors = new ArrayList(Math.max(10, declaredCtors.length));
/*      */             }
/*  462 */             constructors.add(_constructNonDefaultConstructor(ctor, typeContext));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  467 */     if (constructors == null) {
/*  468 */       this._constructors = Collections.emptyList();
/*      */     } else {
/*  470 */       this._constructors = constructors;
/*      */     }
/*      */     
/*  473 */     if ((this._primaryMixIn != null) && (
/*  474 */       (this._defaultConstructor != null) || (!this._constructors.isEmpty()))) {
/*  475 */       _addConstructorMixIns(this._primaryMixIn);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  483 */     if (this._annotationIntrospector != null) {
/*  484 */       if ((this._defaultConstructor != null) && 
/*  485 */         (this._annotationIntrospector.hasIgnoreMarker(this._defaultConstructor))) {
/*  486 */         this._defaultConstructor = null;
/*      */       }
/*      */       
/*  489 */       if (this._constructors != null)
/*      */       {
/*  491 */         int i = this._constructors.size(); for (;;) { i--; if (i < 0) break;
/*  492 */           if (this._annotationIntrospector.hasIgnoreMarker((AnnotatedMember)this._constructors.get(i))) {
/*  493 */             this._constructors.remove(i);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  498 */     List<AnnotatedMethod> creatorMethods = null;
/*      */     
/*      */ 
/*  501 */     for (Method m : _findClassMethods(this._class))
/*  502 */       if (Modifier.isStatic(m.getModifiers()))
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  507 */         if (creatorMethods == null) {
/*  508 */           creatorMethods = new ArrayList(8);
/*      */         }
/*  510 */         creatorMethods.add(_constructCreatorMethod(m, typeContext));
/*      */       }
/*  512 */     if (creatorMethods == null) {
/*  513 */       this._creatorMethods = Collections.emptyList();
/*      */     } else {
/*  515 */       this._creatorMethods = creatorMethods;
/*      */       
/*  517 */       if (this._primaryMixIn != null) {
/*  518 */         _addFactoryMixIns(this._primaryMixIn);
/*      */       }
/*      */       
/*  521 */       if (this._annotationIntrospector != null)
/*      */       {
/*  523 */         int i = this._creatorMethods.size(); for (;;) { i--; if (i < 0) break;
/*  524 */           if (this._annotationIntrospector.hasIgnoreMarker((AnnotatedMember)this._creatorMethods.get(i))) {
/*  525 */             this._creatorMethods.remove(i);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  530 */     this._creatorsResolved = true;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void resolveMemberMethods()
/*      */   {
/*  541 */     this._memberMethods = _resolveMemberMethods();
/*      */   }
/*      */   
/*      */   private AnnotatedMethodMap _resolveMemberMethods()
/*      */   {
/*  546 */     AnnotatedMethodMap memberMethods = new AnnotatedMethodMap();
/*  547 */     AnnotatedMethodMap mixins = new AnnotatedMethodMap();
/*      */     
/*  549 */     _addMemberMethods(this._class, this, memberMethods, this._primaryMixIn, mixins);
/*      */     
/*      */ 
/*  552 */     for (JavaType type : this._superTypes) {
/*  553 */       Class<?> mixin = this._mixInResolver == null ? null : this._mixInResolver.findMixInClassFor(type.getRawClass());
/*  554 */       _addMemberMethods(type.getRawClass(), new TypeResolutionContext.Basic(this._typeFactory, type.getBindings()), memberMethods, mixin, mixins);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  559 */     if (this._mixInResolver != null) {
/*  560 */       Class<?> mixin = this._mixInResolver.findMixInClassFor(Object.class);
/*  561 */       if (mixin != null) {
/*  562 */         _addMethodMixIns(this._class, memberMethods, mixin, mixins);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  572 */     if ((this._annotationIntrospector != null) && 
/*  573 */       (!mixins.isEmpty())) {
/*  574 */       java.util.Iterator<AnnotatedMethod> it = mixins.iterator();
/*  575 */       while (it.hasNext()) {
/*  576 */         AnnotatedMethod mixIn = (AnnotatedMethod)it.next();
/*      */         try {
/*  578 */           Method m = Object.class.getDeclaredMethod(mixIn.getName(), mixIn.getRawParameterTypes());
/*  579 */           if (m != null)
/*      */           {
/*  581 */             AnnotatedMethod am = _constructMethod(m, this);
/*  582 */             _addMixOvers(mixIn.getAnnotated(), am, false);
/*  583 */             memberMethods.add(am);
/*      */           }
/*      */         }
/*      */         catch (Exception e) {}
/*      */       }
/*      */     }
/*  589 */     return memberMethods;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void resolveFields()
/*      */   {
/*  599 */     Map<String, AnnotatedField> foundFields = _findFields(this._type, this, null);
/*      */     List<AnnotatedField> f;
/*  601 */     List<AnnotatedField> f; if ((foundFields == null) || (foundFields.size() == 0)) {
/*  602 */       f = Collections.emptyList();
/*      */     } else {
/*  604 */       f = new ArrayList(foundFields.size());
/*  605 */       f.addAll(foundFields.values());
/*      */     }
/*  607 */     this._fields = f;
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
/*      */   protected void _addClassMixIns(AnnotationMap annotations, JavaType target)
/*      */   {
/*  624 */     if (this._mixInResolver != null) {
/*  625 */       Class<?> toMask = target.getRawClass();
/*  626 */       _addClassMixIns(annotations, toMask, this._mixInResolver.findMixInClassFor(toMask));
/*      */     }
/*      */   }
/*      */   
/*      */   protected void _addClassMixIns(AnnotationMap annotations, Class<?> target)
/*      */   {
/*  632 */     if (this._mixInResolver != null) {
/*  633 */       _addClassMixIns(annotations, target, this._mixInResolver.findMixInClassFor(target));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected void _addClassMixIns(AnnotationMap annotations, Class<?> toMask, Class<?> mixin)
/*      */   {
/*  640 */     if (mixin == null) {
/*  641 */       return;
/*      */     }
/*      */     
/*  644 */     _addAnnotationsIfNotPresent(annotations, ClassUtil.findClassAnnotations(mixin));
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  653 */     for (Class<?> parent : ClassUtil.findSuperClasses(mixin, toMask, false)) {
/*  654 */       _addAnnotationsIfNotPresent(annotations, ClassUtil.findClassAnnotations(parent));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addConstructorMixIns(Class<?> mixin)
/*      */   {
/*  666 */     MemberKey[] ctorKeys = null;
/*  667 */     int ctorCount = this._constructors == null ? 0 : this._constructors.size();
/*  668 */     for (ClassUtil.Ctor ctor0 : ClassUtil.getConstructors(mixin)) {
/*  669 */       Constructor<?> ctor = ctor0.getConstructor();
/*  670 */       if (ctor.getParameterTypes().length == 0) {
/*  671 */         if (this._defaultConstructor != null) {
/*  672 */           _addMixOvers(ctor, this._defaultConstructor, false);
/*      */         }
/*      */       } else {
/*  675 */         if (ctorKeys == null) {
/*  676 */           ctorKeys = new MemberKey[ctorCount];
/*  677 */           for (int i = 0; i < ctorCount; i++) {
/*  678 */             ctorKeys[i] = new MemberKey(((AnnotatedConstructor)this._constructors.get(i)).getAnnotated());
/*      */           }
/*      */         }
/*  681 */         MemberKey key = new MemberKey(ctor);
/*      */         
/*  683 */         for (int i = 0; i < ctorCount; i++) {
/*  684 */           if (key.equals(ctorKeys[i]))
/*      */           {
/*      */ 
/*  687 */             _addMixOvers(ctor, (AnnotatedConstructor)this._constructors.get(i), true);
/*  688 */             break;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void _addFactoryMixIns(Class<?> mixin) {
/*  696 */     MemberKey[] methodKeys = null;
/*  697 */     int methodCount = this._creatorMethods.size();
/*      */     
/*  699 */     for (Method m : ClassUtil.getDeclaredMethods(mixin)) {
/*  700 */       if (Modifier.isStatic(m.getModifiers()))
/*      */       {
/*      */ 
/*  703 */         if (m.getParameterTypes().length != 0)
/*      */         {
/*      */ 
/*  706 */           if (methodKeys == null) {
/*  707 */             methodKeys = new MemberKey[methodCount];
/*  708 */             for (int i = 0; i < methodCount; i++) {
/*  709 */               methodKeys[i] = new MemberKey(((AnnotatedMethod)this._creatorMethods.get(i)).getAnnotated());
/*      */             }
/*      */           }
/*  712 */           MemberKey key = new MemberKey(m);
/*  713 */           for (int i = 0; i < methodCount; i++) {
/*  714 */             if (key.equals(methodKeys[i]))
/*      */             {
/*      */ 
/*  717 */               _addMixOvers(m, (AnnotatedMethod)this._creatorMethods.get(i), true);
/*  718 */               break;
/*      */             }
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
/*      */ 
/*      */ 
/*      */   protected void _addMemberMethods(Class<?> cls, TypeResolutionContext typeContext, AnnotatedMethodMap methods, Class<?> mixInCls, AnnotatedMethodMap mixIns)
/*      */   {
/*  734 */     if (mixInCls != null) {
/*  735 */       _addMethodMixIns(cls, methods, mixInCls, mixIns);
/*      */     }
/*  737 */     if (cls == null) {
/*  738 */       return;
/*      */     }
/*      */     
/*  741 */     for (Method m : _findClassMethods(cls)) {
/*  742 */       if (_isIncludableMemberMethod(m))
/*      */       {
/*      */ 
/*  745 */         AnnotatedMethod old = methods.find(m);
/*  746 */         if (old == null) {
/*  747 */           AnnotatedMethod newM = _constructMethod(m, typeContext);
/*  748 */           methods.add(newM);
/*      */           
/*  750 */           old = mixIns.remove(m);
/*  751 */           if (old != null) {
/*  752 */             _addMixOvers(old.getAnnotated(), newM, false);
/*      */           }
/*      */           
/*      */         }
/*      */         else
/*      */         {
/*  758 */           _addMixUnders(m, old);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  767 */           if ((old.getDeclaringClass().isInterface()) && (!m.getDeclaringClass().isInterface())) {
/*  768 */             methods.add(old.withMethod(m));
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected void _addMethodMixIns(Class<?> targetClass, AnnotatedMethodMap methods, Class<?> mixInCls, AnnotatedMethodMap mixIns)
/*      */   {
/*  779 */     List<Class<?>> parents = ClassUtil.findRawSuperTypes(mixInCls, targetClass, true);
/*  780 */     for (Class<?> mixin : parents) {
/*  781 */       for (Method m : ClassUtil.getDeclaredMethods(mixin)) {
/*  782 */         if (_isIncludableMemberMethod(m))
/*      */         {
/*      */ 
/*  785 */           AnnotatedMethod am = methods.find(m);
/*      */           
/*      */ 
/*      */ 
/*      */ 
/*  790 */           if (am != null) {
/*  791 */             _addMixUnders(m, am);
/*      */ 
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/*      */ 
/*  798 */             am = mixIns.find(m);
/*  799 */             if (am != null) {
/*  800 */               _addMixUnders(m, am);
/*      */             }
/*      */             else
/*      */             {
/*  804 */               mixIns.add(_constructMethod(m, this));
/*      */             }
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Map<String, AnnotatedField> _findFields(JavaType type, TypeResolutionContext typeContext, Map<String, AnnotatedField> fields)
/*      */   {
/*  825 */     JavaType parent = type.getSuperClass();
/*  826 */     if (parent != null) {
/*  827 */       Class<?> cls = type.getRawClass();
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  832 */       fields = _findFields(parent, new TypeResolutionContext.Basic(this._typeFactory, parent.getBindings()), fields);
/*      */       
/*      */ 
/*  835 */       for (Field f : ClassUtil.getDeclaredFields(cls))
/*      */       {
/*  837 */         if (_isIncludableField(f))
/*      */         {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  845 */           if (fields == null) {
/*  846 */             fields = new java.util.LinkedHashMap();
/*      */           }
/*  848 */           fields.put(f.getName(), _constructField(f, typeContext));
/*      */         }
/*      */       }
/*  851 */       if (this._mixInResolver != null) {
/*  852 */         Class<?> mixin = this._mixInResolver.findMixInClassFor(cls);
/*  853 */         if (mixin != null) {
/*  854 */           _addFieldMixIns(mixin, cls, fields);
/*      */         }
/*      */       }
/*      */     }
/*  858 */     return fields;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addFieldMixIns(Class<?> mixInCls, Class<?> targetClass, Map<String, AnnotatedField> fields)
/*      */   {
/*  869 */     List<Class<?>> parents = ClassUtil.findSuperClasses(mixInCls, targetClass, true);
/*  870 */     for (Class<?> mixin : parents) {
/*  871 */       for (Field mixinField : ClassUtil.getDeclaredFields(mixin))
/*      */       {
/*  873 */         if (_isIncludableField(mixinField))
/*      */         {
/*      */ 
/*  876 */           String name = mixinField.getName();
/*      */           
/*  878 */           AnnotatedField maskedField = (AnnotatedField)fields.get(name);
/*  879 */           if (maskedField != null) {
/*  880 */             _addOrOverrideAnnotations(maskedField, mixinField.getDeclaredAnnotations());
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected AnnotatedMethod _constructMethod(Method m, TypeResolutionContext typeContext)
/*      */   {
/*  898 */     if (this._annotationIntrospector == null) {
/*  899 */       return new AnnotatedMethod(typeContext, m, _emptyAnnotationMap(), null);
/*      */     }
/*  901 */     return new AnnotatedMethod(typeContext, m, _collectRelevantAnnotations(m.getDeclaredAnnotations()), null);
/*      */   }
/*      */   
/*      */ 
/*      */   protected AnnotatedConstructor _constructDefaultConstructor(ClassUtil.Ctor ctor, TypeResolutionContext typeContext)
/*      */   {
/*  907 */     if (this._annotationIntrospector == null) {
/*  908 */       return new AnnotatedConstructor(typeContext, ctor.getConstructor(), _emptyAnnotationMap(), NO_ANNOTATION_MAPS);
/*      */     }
/*  910 */     return new AnnotatedConstructor(typeContext, ctor.getConstructor(), _collectRelevantAnnotations(ctor.getDeclaredAnnotations()), NO_ANNOTATION_MAPS);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected AnnotatedConstructor _constructNonDefaultConstructor(ClassUtil.Ctor ctor, TypeResolutionContext typeContext)
/*      */   {
/*  917 */     int paramCount = ctor.getParamCount();
/*  918 */     if (this._annotationIntrospector == null) {
/*  919 */       return new AnnotatedConstructor(typeContext, ctor.getConstructor(), _emptyAnnotationMap(), _emptyAnnotationMaps(paramCount));
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  927 */     if (paramCount == 0) {
/*  928 */       return new AnnotatedConstructor(typeContext, ctor.getConstructor(), _collectRelevantAnnotations(ctor.getDeclaredAnnotations()), NO_ANNOTATION_MAPS);
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  933 */     Annotation[][] paramAnns = ctor.getParameterAnnotations();
/*  934 */     AnnotationMap[] resolvedAnnotations; if (paramCount != paramAnns.length)
/*      */     {
/*      */ 
/*      */ 
/*  938 */       AnnotationMap[] resolvedAnnotations = null;
/*  939 */       Class<?> dc = ctor.getDeclaringClass();
/*      */       
/*  941 */       if ((dc.isEnum()) && (paramCount == paramAnns.length + 2)) {
/*  942 */         Annotation[][] old = paramAnns;
/*  943 */         paramAnns = new Annotation[old.length + 2][];
/*  944 */         System.arraycopy(old, 0, paramAnns, 2, old.length);
/*  945 */         resolvedAnnotations = _collectRelevantAnnotations(paramAnns);
/*  946 */       } else if (dc.isMemberClass())
/*      */       {
/*  948 */         if (paramCount == paramAnns.length + 1)
/*      */         {
/*  950 */           Annotation[][] old = paramAnns;
/*  951 */           paramAnns = new Annotation[old.length + 1][];
/*  952 */           System.arraycopy(old, 0, paramAnns, 1, old.length);
/*  953 */           resolvedAnnotations = _collectRelevantAnnotations(paramAnns);
/*      */         }
/*      */       }
/*  956 */       if (resolvedAnnotations == null) {
/*  957 */         throw new IllegalStateException("Internal error: constructor for " + ctor.getDeclaringClass().getName() + " has mismatch: " + paramCount + " parameters; " + paramAnns.length + " sets of annotations");
/*      */       }
/*      */     }
/*      */     else {
/*  961 */       resolvedAnnotations = _collectRelevantAnnotations(paramAnns);
/*      */     }
/*  963 */     return new AnnotatedConstructor(typeContext, ctor.getConstructor(), _collectRelevantAnnotations(ctor.getDeclaredAnnotations()), resolvedAnnotations);
/*      */   }
/*      */   
/*      */ 
/*      */   protected AnnotatedMethod _constructCreatorMethod(Method m, TypeResolutionContext typeContext)
/*      */   {
/*  969 */     int paramCount = m.getParameterTypes().length;
/*  970 */     if (this._annotationIntrospector == null) {
/*  971 */       return new AnnotatedMethod(typeContext, m, _emptyAnnotationMap(), _emptyAnnotationMaps(paramCount));
/*      */     }
/*  973 */     if (paramCount == 0) {
/*  974 */       return new AnnotatedMethod(typeContext, m, _collectRelevantAnnotations(m.getDeclaredAnnotations()), NO_ANNOTATION_MAPS);
/*      */     }
/*      */     
/*  977 */     return new AnnotatedMethod(typeContext, m, _collectRelevantAnnotations(m.getDeclaredAnnotations()), _collectRelevantAnnotations(m.getParameterAnnotations()));
/*      */   }
/*      */   
/*      */ 
/*      */   protected AnnotatedField _constructField(Field f, TypeResolutionContext typeContext)
/*      */   {
/*  983 */     if (this._annotationIntrospector == null) {
/*  984 */       return new AnnotatedField(typeContext, f, _emptyAnnotationMap());
/*      */     }
/*  986 */     return new AnnotatedField(typeContext, f, _collectRelevantAnnotations(f.getDeclaredAnnotations()));
/*      */   }
/*      */   
/*      */   private AnnotationMap _emptyAnnotationMap() {
/*  990 */     return new AnnotationMap();
/*      */   }
/*      */   
/*      */   private AnnotationMap[] _emptyAnnotationMaps(int count) {
/*  994 */     if (count == 0) {
/*  995 */       return NO_ANNOTATION_MAPS;
/*      */     }
/*  997 */     AnnotationMap[] maps = new AnnotationMap[count];
/*  998 */     for (int i = 0; i < count; i++) {
/*  999 */       maps[i] = _emptyAnnotationMap();
/*      */     }
/* 1001 */     return maps;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _isIncludableMemberMethod(Method m)
/*      */   {
/* 1012 */     if (Modifier.isStatic(m.getModifiers())) {
/* 1013 */       return false;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/* 1019 */     if ((m.isSynthetic()) || (m.isBridge())) {
/* 1020 */       return false;
/*      */     }
/*      */     
/* 1023 */     int pcount = m.getParameterTypes().length;
/* 1024 */     return pcount <= 2;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean _isIncludableField(Field f)
/*      */   {
/* 1030 */     if (f.isSynthetic()) {
/* 1031 */       return false;
/*      */     }
/*      */     
/*      */ 
/* 1035 */     int mods = f.getModifiers();
/* 1036 */     if (Modifier.isStatic(mods)) {
/* 1037 */       return false;
/*      */     }
/* 1039 */     return true;
/*      */   }
/*      */   
/*      */ 
/*      */   private boolean _isIncludableConstructor(Constructor<?> c)
/*      */   {
/* 1045 */     return !c.isSynthetic();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected AnnotationMap[] _collectRelevantAnnotations(Annotation[][] anns)
/*      */   {
/* 1056 */     int len = anns.length;
/* 1057 */     AnnotationMap[] result = new AnnotationMap[len];
/* 1058 */     for (int i = 0; i < len; i++) {
/* 1059 */       result[i] = _collectRelevantAnnotations(anns[i]);
/*      */     }
/* 1061 */     return result;
/*      */   }
/*      */   
/*      */   protected AnnotationMap _collectRelevantAnnotations(Annotation[] anns)
/*      */   {
/* 1066 */     return _addAnnotationsIfNotPresent(new AnnotationMap(), anns);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private AnnotationMap _addAnnotationsIfNotPresent(AnnotationMap result, Annotation[] anns)
/*      */   {
/* 1075 */     if (anns != null) {
/* 1076 */       List<Annotation> fromBundles = null;
/* 1077 */       for (Annotation ann : anns)
/*      */       {
/* 1079 */         boolean wasNotPresent = result.addIfNotPresent(ann);
/* 1080 */         if ((wasNotPresent) && (_isAnnotationBundle(ann))) {
/* 1081 */           fromBundles = _addFromBundle(ann, fromBundles);
/*      */         }
/*      */       }
/* 1084 */       if (fromBundles != null) {
/* 1085 */         _addAnnotationsIfNotPresent(result, (Annotation[])fromBundles.toArray(new Annotation[fromBundles.size()]));
/*      */       }
/*      */     }
/* 1088 */     return result;
/*      */   }
/*      */   
/*      */   private List<Annotation> _addFromBundle(Annotation bundle, List<Annotation> result)
/*      */   {
/* 1093 */     for (Annotation a : ClassUtil.findClassAnnotations(bundle.annotationType()))
/*      */     {
/* 1095 */       if ((!(a instanceof java.lang.annotation.Target)) && (!(a instanceof java.lang.annotation.Retention)))
/*      */       {
/*      */ 
/* 1098 */         if (result == null) {
/* 1099 */           result = new ArrayList();
/*      */         }
/* 1101 */         result.add(a);
/*      */       } }
/* 1103 */     return result;
/*      */   }
/*      */   
/*      */   private void _addAnnotationsIfNotPresent(AnnotatedMember target, Annotation[] anns)
/*      */   {
/* 1108 */     if (anns != null) {
/* 1109 */       List<Annotation> fromBundles = null;
/* 1110 */       for (Annotation ann : anns) {
/* 1111 */         boolean wasNotPresent = target.addIfNotPresent(ann);
/* 1112 */         if ((wasNotPresent) && (_isAnnotationBundle(ann))) {
/* 1113 */           fromBundles = _addFromBundle(ann, fromBundles);
/*      */         }
/*      */       }
/* 1116 */       if (fromBundles != null) {
/* 1117 */         _addAnnotationsIfNotPresent(target, (Annotation[])fromBundles.toArray(new Annotation[fromBundles.size()]));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private void _addOrOverrideAnnotations(AnnotatedMember target, Annotation[] anns)
/*      */   {
/* 1124 */     if (anns != null) {
/* 1125 */       List<Annotation> fromBundles = null;
/* 1126 */       for (Annotation ann : anns) {
/* 1127 */         boolean wasModified = target.addOrOverride(ann);
/* 1128 */         if ((wasModified) && (_isAnnotationBundle(ann))) {
/* 1129 */           fromBundles = _addFromBundle(ann, fromBundles);
/*      */         }
/*      */       }
/* 1132 */       if (fromBundles != null) {
/* 1133 */         _addOrOverrideAnnotations(target, (Annotation[])fromBundles.toArray(new Annotation[fromBundles.size()]));
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addMixOvers(Constructor<?> mixin, AnnotatedConstructor target, boolean addParamAnnotations)
/*      */   {
/* 1145 */     _addOrOverrideAnnotations(target, mixin.getDeclaredAnnotations());
/* 1146 */     if (addParamAnnotations) {
/* 1147 */       Annotation[][] pa = mixin.getParameterAnnotations();
/* 1148 */       int i = 0; for (int len = pa.length; i < len; i++) {
/* 1149 */         for (Annotation a : pa[i]) {
/* 1150 */           target.addOrOverrideParam(i, a);
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
/*      */   protected void _addMixOvers(Method mixin, AnnotatedMethod target, boolean addParamAnnotations)
/*      */   {
/* 1163 */     _addOrOverrideAnnotations(target, mixin.getDeclaredAnnotations());
/* 1164 */     if (addParamAnnotations) {
/* 1165 */       Annotation[][] pa = mixin.getParameterAnnotations();
/* 1166 */       int i = 0; for (int len = pa.length; i < len; i++) {
/* 1167 */         for (Annotation a : pa[i]) {
/* 1168 */           target.addOrOverrideParam(i, a);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void _addMixUnders(Method src, AnnotatedMethod target)
/*      */   {
/* 1179 */     _addAnnotationsIfNotPresent(target, src.getDeclaredAnnotations());
/*      */   }
/*      */   
/*      */   private final boolean _isAnnotationBundle(Annotation ann) {
/* 1183 */     return (this._annotationIntrospector != null) && (this._annotationIntrospector.isAnnotationBundle(ann));
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Method[] _findClassMethods(Class<?> cls)
/*      */   {
/*      */     try
/*      */     {
/* 1196 */       return ClassUtil.getDeclaredMethods(cls);
/*      */     }
/*      */     catch (NoClassDefFoundError ex)
/*      */     {
/* 1200 */       ClassLoader loader = Thread.currentThread().getContextClassLoader();
/* 1201 */       if (loader == null)
/*      */       {
/* 1203 */         throw ex;
/*      */       }
/*      */       Class<?> contextClass;
/*      */       try {
/* 1207 */         contextClass = loader.loadClass(cls.getName());
/*      */       }
/*      */       catch (ClassNotFoundException e)
/*      */       {
/* 1211 */         throw ex;
/*      */       }
/* 1213 */       return contextClass.getDeclaredMethods();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1225 */     return "[AnnotedClass " + this._class.getName() + "]";
/*      */   }
/*      */   
/*      */   public int hashCode()
/*      */   {
/* 1230 */     return this._class.getName().hashCode();
/*      */   }
/*      */   
/*      */   public boolean equals(Object o)
/*      */   {
/* 1235 */     if (o == this) return true;
/* 1236 */     if ((o == null) || (o.getClass() != getClass())) return false;
/* 1237 */     return ((AnnotatedClass)o)._class == this._class;
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\AnnotatedClass.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */