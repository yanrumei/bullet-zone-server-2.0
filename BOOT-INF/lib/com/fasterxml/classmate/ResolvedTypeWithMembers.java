/*     */ package com.fasterxml.classmate;
/*     */ 
/*     */ import com.fasterxml.classmate.members.HierarchicType;
/*     */ import com.fasterxml.classmate.members.RawConstructor;
/*     */ import com.fasterxml.classmate.members.RawField;
/*     */ import com.fasterxml.classmate.members.RawMethod;
/*     */ import com.fasterxml.classmate.members.ResolvedConstructor;
/*     */ import com.fasterxml.classmate.members.ResolvedField;
/*     */ import com.fasterxml.classmate.members.ResolvedMethod;
/*     */ import com.fasterxml.classmate.util.MethodKey;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Inherited;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ 
/*     */ public class ResolvedTypeWithMembers
/*     */ {
/*  26 */   private static final ResolvedType[] NO_RESOLVED_TYPES = new ResolvedType[0];
/*     */   
/*  28 */   private static final ResolvedMethod[] NO_RESOLVED_METHODS = new ResolvedMethod[0];
/*  29 */   private static final ResolvedField[] NO_RESOLVED_FIELDS = new ResolvedField[0];
/*  30 */   private static final ResolvedConstructor[] NO_RESOLVED_CONSTRUCTORS = new ResolvedConstructor[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  35 */   protected static final AnnotationConfiguration DEFAULT_ANNOTATION_CONFIG = new AnnotationConfiguration.StdConfiguration(AnnotationInclusion.DONT_INCLUDE);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final TypeResolver _typeResolver;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final AnnotationHandler _annotationHandler;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final HierarchicType _mainType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final HierarchicType[] _types;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Filter<RawField> _fieldFilter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Filter<RawConstructor> _constructorFilter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Filter<RawMethod> _methodFilter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  81 */   protected ResolvedMethod[] _staticMethods = null;
/*     */   
/*  83 */   protected ResolvedField[] _staticFields = null;
/*     */   
/*  85 */   protected ResolvedMethod[] _memberMethods = null;
/*     */   
/*  87 */   protected ResolvedField[] _memberFields = null;
/*     */   
/*  89 */   protected ResolvedConstructor[] _constructors = null;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResolvedTypeWithMembers(TypeResolver typeResolver, AnnotationConfiguration annotationConfig, HierarchicType mainType, HierarchicType[] types, Filter<RawConstructor> constructorFilter, Filter<RawField> fieldFilter, Filter<RawMethod> methodFilter)
/*     */   {
/* 101 */     this._typeResolver = typeResolver;
/* 102 */     this._mainType = mainType;
/* 103 */     this._types = types;
/* 104 */     if (annotationConfig == null) {
/* 105 */       annotationConfig = DEFAULT_ANNOTATION_CONFIG;
/*     */     }
/* 107 */     this._annotationHandler = new AnnotationHandler(annotationConfig);
/* 108 */     this._constructorFilter = constructorFilter;
/* 109 */     this._fieldFilter = fieldFilter;
/* 110 */     this._methodFilter = methodFilter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int size()
/*     */   {
/* 119 */     return this._types.length;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<HierarchicType> allTypesAndOverrides()
/*     */   {
/* 126 */     return Arrays.asList(this._types);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<HierarchicType> mainTypeAndOverrides()
/*     */   {
/* 135 */     List<HierarchicType> l = Arrays.asList(this._types);
/* 136 */     int end = this._mainType.getPriority() + 1;
/* 137 */     if (end < l.size()) {
/* 138 */       l = l.subList(0, end);
/*     */     }
/* 140 */     return l;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<HierarchicType> overridesOnly()
/*     */   {
/* 148 */     int index = this._mainType.getPriority();
/* 149 */     if (index == 0) {
/* 150 */       return java.util.Collections.emptyList();
/*     */     }
/* 152 */     List<HierarchicType> l = Arrays.asList(this._types);
/* 153 */     return l.subList(0, index);
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
/*     */   public ResolvedField[] getStaticFields()
/*     */   {
/* 171 */     if (this._staticFields == null) {
/* 172 */       this._staticFields = resolveStaticFields();
/*     */     }
/* 174 */     return this._staticFields;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResolvedMethod[] getStaticMethods()
/*     */   {
/* 184 */     if (this._staticMethods == null) {
/* 185 */       this._staticMethods = resolveStaticMethods();
/*     */     }
/* 187 */     return this._staticMethods;
/*     */   }
/*     */   
/*     */   public ResolvedField[] getMemberFields()
/*     */   {
/* 192 */     if (this._memberFields == null) {
/* 193 */       this._memberFields = resolveMemberFields();
/*     */     }
/* 195 */     return this._memberFields;
/*     */   }
/*     */   
/*     */   public ResolvedMethod[] getMemberMethods()
/*     */   {
/* 200 */     if (this._memberMethods == null) {
/* 201 */       this._memberMethods = resolveMemberMethods();
/*     */     }
/* 203 */     return this._memberMethods;
/*     */   }
/*     */   
/*     */   public ResolvedConstructor[] getConstructors()
/*     */   {
/* 208 */     if (this._constructors == null) {
/* 209 */       this._constructors = resolveConstructors();
/*     */     }
/* 211 */     return this._constructors;
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
/*     */   protected ResolvedConstructor[] resolveConstructors()
/*     */   {
/* 227 */     LinkedHashMap<MethodKey, ResolvedConstructor> constructors = new LinkedHashMap();
/* 228 */     for (RawConstructor constructor : this._mainType.getType().getConstructors())
/*     */     {
/* 230 */       if ((this._constructorFilter == null) || (this._constructorFilter.include(constructor))) {
/* 231 */         constructors.put(constructor.createKey(), resolveConstructor(constructor));
/*     */       }
/*     */     }
/*     */     
/* 235 */     for (HierarchicType type : overridesOnly()) {
/* 236 */       for (RawConstructor raw : type.getType().getConstructors()) {
/* 237 */         ResolvedConstructor constructor = (ResolvedConstructor)constructors.get(raw.createKey());
/*     */         
/* 239 */         if (constructor != null) {
/* 240 */           for (Annotation ann : raw.getAnnotations()) {
/* 241 */             if (this._annotationHandler.includeMethodAnnotation(ann)) {
/* 242 */               constructor.applyOverride(ann);
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 247 */           Annotation[][] params = raw.getRawMember().getParameterAnnotations();
/* 248 */           for (int i = 0; i < params.length; i++) {
/* 249 */             for (Annotation annotation : params[i]) {
/* 250 */               if (this._annotationHandler.includeParameterAnnotation(annotation)) {
/* 251 */                 constructor.applyParamOverride(i, annotation);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 258 */     if (constructors.size() == 0) {
/* 259 */       return NO_RESOLVED_CONSTRUCTORS;
/*     */     }
/* 261 */     return (ResolvedConstructor[])constructors.values().toArray(new ResolvedConstructor[constructors.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ResolvedField[] resolveMemberFields()
/*     */   {
/* 272 */     LinkedHashMap<String, ResolvedField> fields = new LinkedHashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 278 */     int typeIndex = this._types.length; for (;;) { typeIndex--; if (typeIndex < 0) break;
/* 279 */       HierarchicType thisType = this._types[typeIndex];
/*     */       
/* 281 */       if (thisType.isMixin()) {
/* 282 */         for (RawField raw : thisType.getType().getMemberFields()) {
/* 283 */           if ((this._fieldFilter == null) || (this._fieldFilter.include(raw)))
/*     */           {
/*     */ 
/* 286 */             ResolvedField field = (ResolvedField)fields.get(raw.getName());
/* 287 */             if (field != null) {
/* 288 */               for (Annotation ann : raw.getAnnotations()) {
/* 289 */                 if (this._annotationHandler.includeMethodAnnotation(ann))
/* 290 */                   field.applyOverride(ann);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       } else {
/* 296 */         for (RawField field : thisType.getType().getMemberFields()) {
/* 297 */           if ((this._fieldFilter == null) || (this._fieldFilter.include(field)))
/*     */           {
/*     */ 
/* 300 */             fields.put(field.getName(), resolveField(field));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 305 */     if (fields.size() == 0) {
/* 306 */       return NO_RESOLVED_FIELDS;
/*     */     }
/* 308 */     return (ResolvedField[])fields.values().toArray(new ResolvedField[fields.size()]);
/*     */   }
/*     */   
/*     */   protected ResolvedMethod[] resolveMemberMethods()
/*     */   {
/* 313 */     LinkedHashMap<MethodKey, ResolvedMethod> methods = new LinkedHashMap();
/* 314 */     LinkedHashMap<MethodKey, Annotations> overrides = new LinkedHashMap();
/* 315 */     LinkedHashMap<MethodKey, Annotations[]> paramOverrides = new LinkedHashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 321 */     for (Iterator i$ = allTypesAndOverrides().iterator(); i$.hasNext();) { type = (HierarchicType)i$.next();
/* 322 */       for (RawMethod method : type.getType().getMemberMethods())
/*     */       {
/* 324 */         if ((this._methodFilter == null) || (this._methodFilter.include(method)))
/*     */         {
/*     */ 
/*     */ 
/* 328 */           MethodKey key = method.createKey();
/* 329 */           ResolvedMethod old = (ResolvedMethod)methods.get(key);
/*     */           
/*     */ 
/* 332 */           if (type.isMixin()) {
/* 333 */             for (Annotation ann : method.getAnnotations())
/*     */             {
/* 335 */               if (old != null) {
/* 336 */                 if (methodCanInherit(ann))
/*     */                 {
/*     */ 
/*     */ 
/* 340 */                   old.applyDefault(ann); }
/*     */               } else {
/* 342 */                 Annotations oldAnn = (Annotations)overrides.get(key);
/* 343 */                 if (oldAnn == null) {
/* 344 */                   oldAnn = new Annotations();
/* 345 */                   oldAnn.add(ann);
/* 346 */                   overrides.put(key, oldAnn);
/*     */                 } else {
/* 348 */                   oldAnn.addAsDefault(ann);
/*     */                 }
/*     */               }
/*     */             }
/*     */             
/*     */ 
/* 354 */             Annotation[][] argAnnotations = method.getRawMember().getParameterAnnotations();
/* 355 */             if (old == null) {
/* 356 */               Annotations[] oldParamAnns = (Annotations[])paramOverrides.get(key);
/* 357 */               if (oldParamAnns == null) {
/* 358 */                 oldParamAnns = new Annotations[argAnnotations.length];
/* 359 */                 for (int i = 0; i < argAnnotations.length; i++) {
/* 360 */                   oldParamAnns[i] = new Annotations();
/* 361 */                   for (Annotation annotation : argAnnotations[i]) {
/* 362 */                     if (parameterCanInherit(annotation)) {
/* 363 */                       oldParamAnns[i].add(annotation);
/*     */                     }
/*     */                   }
/*     */                 }
/* 367 */                 paramOverrides.put(key, oldParamAnns);
/*     */               } else {
/* 369 */                 for (int i = 0; i < argAnnotations.length; i++) {
/* 370 */                   for (Annotation annotation : argAnnotations[i]) {
/* 371 */                     if (parameterCanInherit(annotation)) {
/* 372 */                       oldParamAnns[i].addAsDefault(annotation);
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             } else {
/* 378 */               for (int i = 0; i < argAnnotations.length; i++) {
/* 379 */                 for (Annotation annotation : argAnnotations[i]) {
/* 380 */                   if (parameterCanInherit(annotation)) {
/* 381 */                     old.applyParamDefault(i, annotation);
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/* 387 */           else if (old == null) {
/* 388 */             ResolvedMethod newMethod = resolveMethod(method);
/* 389 */             methods.put(key, newMethod);
/*     */             
/* 391 */             Annotations overrideAnn = (Annotations)overrides.get(key);
/* 392 */             if (overrideAnn != null) {
/* 393 */               newMethod.applyOverrides(overrideAnn);
/*     */             }
/*     */             
/* 396 */             Annotations[] annotations = (Annotations[])paramOverrides.get(key);
/* 397 */             if (annotations != null) {
/* 398 */               for (int i = 0; i < annotations.length; i++) {
/* 399 */                 newMethod.applyParamOverrides(i, annotations[i]);
/*     */               }
/*     */             }
/*     */           } else {
/* 403 */             for (Annotation ann : method.getAnnotations()) {
/* 404 */               if (methodCanInherit(ann)) {
/* 405 */                 old.applyDefault(ann);
/*     */               }
/*     */             }
/*     */             
/* 409 */             Annotation[][] parameterAnnotations = method.getRawMember().getParameterAnnotations();
/* 410 */             for (int i = 0; i < parameterAnnotations.length; i++) {
/* 411 */               for (Annotation annotation : parameterAnnotations[i]) {
/* 412 */                 if (parameterCanInherit(annotation)) {
/* 413 */                   old.applyParamDefault(i, annotation);
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     HierarchicType type;
/* 422 */     if (methods.size() == 0) {
/* 423 */       return NO_RESOLVED_METHODS;
/*     */     }
/* 425 */     return (ResolvedMethod[])methods.values().toArray(new ResolvedMethod[methods.size()]);
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
/*     */   protected ResolvedField[] resolveStaticFields()
/*     */   {
/* 439 */     LinkedHashMap<String, ResolvedField> fields = new LinkedHashMap();
/* 440 */     for (RawField field : this._mainType.getType().getStaticFields()) {
/* 441 */       if ((this._fieldFilter == null) || (this._fieldFilter.include(field))) {
/* 442 */         fields.put(field.getName(), resolveField(field));
/*     */       }
/*     */     }
/*     */     
/* 446 */     for (HierarchicType type : overridesOnly()) {
/* 447 */       for (RawField raw : type.getType().getStaticFields()) {
/* 448 */         ResolvedField field = (ResolvedField)fields.get(raw.getName());
/*     */         
/* 450 */         if (field != null) {
/* 451 */           for (Annotation ann : raw.getAnnotations()) {
/* 452 */             if (this._annotationHandler.includeFieldAnnotation(ann)) {
/* 453 */               field.applyOverride(ann);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 460 */     if (fields.isEmpty()) {
/* 461 */       return NO_RESOLVED_FIELDS;
/*     */     }
/* 463 */     return (ResolvedField[])fields.values().toArray(new ResolvedField[fields.size()]);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ResolvedMethod[] resolveStaticMethods()
/*     */   {
/* 473 */     LinkedHashMap<MethodKey, ResolvedMethod> methods = new LinkedHashMap();
/* 474 */     for (RawMethod method : this._mainType.getType().getStaticMethods()) {
/* 475 */       if ((this._methodFilter == null) || (this._methodFilter.include(method))) {
/* 476 */         methods.put(method.createKey(), resolveMethod(method));
/*     */       }
/*     */     }
/*     */     
/* 480 */     for (HierarchicType type : overridesOnly()) {
/* 481 */       for (RawMethod raw : type.getType().getStaticMethods()) {
/* 482 */         ResolvedMethod method = (ResolvedMethod)methods.get(raw.createKey());
/*     */         
/* 484 */         if (method != null) {
/* 485 */           for (Annotation ann : raw.getAnnotations()) {
/* 486 */             if (this._annotationHandler.includeMethodAnnotation(ann)) {
/* 487 */               method.applyOverride(ann);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 493 */     if (methods.size() == 0) {
/* 494 */       return NO_RESOLVED_METHODS;
/*     */     }
/* 496 */     return (ResolvedMethod[])methods.values().toArray(new ResolvedMethod[methods.size()]);
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
/*     */   protected ResolvedConstructor resolveConstructor(RawConstructor raw)
/*     */   {
/* 510 */     ResolvedType context = raw.getDeclaringType();
/* 511 */     TypeBindings bindings = context.getTypeBindings();
/* 512 */     Constructor<?> ctor = raw.getRawMember();
/* 513 */     Type[] rawTypes = ctor.getGenericParameterTypes();
/*     */     ResolvedType[] argTypes;
/* 515 */     ResolvedType[] argTypes; if ((rawTypes == null) || (rawTypes.length == 0)) {
/* 516 */       argTypes = NO_RESOLVED_TYPES;
/*     */     } else {
/* 518 */       argTypes = new ResolvedType[rawTypes.length];
/* 519 */       int i = 0; for (int len = rawTypes.length; i < len; i++) {
/* 520 */         argTypes[i] = this._typeResolver.resolve(bindings, rawTypes[i]);
/*     */       }
/*     */     }
/*     */     
/* 524 */     Annotations anns = new Annotations();
/* 525 */     for (Annotation ann : ctor.getAnnotations()) {
/* 526 */       if (this._annotationHandler.includeConstructorAnnotation(ann)) {
/* 527 */         anns.add(ann);
/*     */       }
/*     */     }
/*     */     
/* 531 */     ResolvedConstructor constructor = new ResolvedConstructor(context, anns, ctor, argTypes);
/*     */     
/*     */ 
/* 534 */     Annotation[][] annotations = ctor.getParameterAnnotations();
/* 535 */     for (int i = 0; i < argTypes.length; i++) {
/* 536 */       for (Annotation ann : annotations[i]) {
/* 537 */         constructor.applyParamOverride(i, ann);
/*     */       }
/*     */     }
/*     */     
/* 541 */     return constructor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ResolvedField resolveField(RawField raw)
/*     */   {
/* 549 */     ResolvedType context = raw.getDeclaringType();
/* 550 */     Field field = raw.getRawMember();
/* 551 */     ResolvedType type = this._typeResolver.resolve(context.getTypeBindings(), field.getGenericType());
/*     */     
/* 553 */     Annotations anns = new Annotations();
/* 554 */     for (Annotation ann : field.getAnnotations()) {
/* 555 */       if (this._annotationHandler.includeFieldAnnotation(ann)) {
/* 556 */         anns.add(ann);
/*     */       }
/*     */     }
/* 559 */     return new ResolvedField(context, anns, field, type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ResolvedMethod resolveMethod(RawMethod raw)
/*     */   {
/* 567 */     ResolvedType context = raw.getDeclaringType();
/* 568 */     TypeBindings bindings = context.getTypeBindings();
/* 569 */     Method m = raw.getRawMember();
/* 570 */     Type rawType = m.getGenericReturnType();
/* 571 */     ResolvedType rt = rawType == Void.TYPE ? null : this._typeResolver.resolve(bindings, rawType);
/* 572 */     Type[] rawTypes = m.getGenericParameterTypes();
/*     */     ResolvedType[] argTypes;
/* 574 */     ResolvedType[] argTypes; if ((rawTypes == null) || (rawTypes.length == 0)) {
/* 575 */       argTypes = NO_RESOLVED_TYPES;
/*     */     } else {
/* 577 */       argTypes = new ResolvedType[rawTypes.length];
/* 578 */       int i = 0; for (int len = rawTypes.length; i < len; i++) {
/* 579 */         argTypes[i] = this._typeResolver.resolve(bindings, rawTypes[i]);
/*     */       }
/*     */     }
/*     */     
/* 583 */     Annotations anns = new Annotations();
/* 584 */     for (Annotation ann : m.getAnnotations()) {
/* 585 */       if (this._annotationHandler.includeMethodAnnotation(ann)) {
/* 586 */         anns.add(ann);
/*     */       }
/*     */     }
/*     */     
/* 590 */     ResolvedMethod method = new ResolvedMethod(context, anns, m, rt, argTypes);
/*     */     
/*     */ 
/* 593 */     Annotation[][] annotations = m.getParameterAnnotations();
/* 594 */     for (int i = 0; i < argTypes.length; i++) {
/* 595 */       for (Annotation ann : annotations[i]) {
/* 596 */         method.applyParamOverride(i, ann);
/*     */       }
/*     */     }
/* 599 */     return method;
/*     */   }
/*     */   
/*     */   protected boolean methodCanInherit(Annotation annotation) {
/* 603 */     AnnotationInclusion annotationInclusion = this._annotationHandler.methodInclusion(annotation);
/* 604 */     if (annotationInclusion == AnnotationInclusion.INCLUDE_AND_INHERIT_IF_INHERITED) {
/* 605 */       return annotation.annotationType().isAnnotationPresent(Inherited.class);
/*     */     }
/* 607 */     return annotationInclusion == AnnotationInclusion.INCLUDE_AND_INHERIT;
/*     */   }
/*     */   
/*     */   protected boolean parameterCanInherit(Annotation annotation) {
/* 611 */     AnnotationInclusion annotationInclusion = this._annotationHandler.parameterInclusion(annotation);
/* 612 */     if (annotationInclusion == AnnotationInclusion.INCLUDE_AND_INHERIT_IF_INHERITED) {
/* 613 */       return annotation.annotationType().isAnnotationPresent(Inherited.class);
/*     */     }
/* 615 */     return annotationInclusion == AnnotationInclusion.INCLUDE_AND_INHERIT;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static final class AnnotationHandler
/*     */   {
/*     */     private final AnnotationConfiguration _annotationConfig;
/*     */     
/*     */ 
/*     */     private HashMap<Class<? extends Annotation>, AnnotationInclusion> _fieldInclusions;
/*     */     
/*     */ 
/*     */     private HashMap<Class<? extends Annotation>, AnnotationInclusion> _constructorInclusions;
/*     */     
/*     */ 
/*     */     private HashMap<Class<? extends Annotation>, AnnotationInclusion> _methodInclusions;
/*     */     
/*     */     private HashMap<Class<? extends Annotation>, AnnotationInclusion> _parameterInclusions;
/*     */     
/*     */ 
/*     */     public AnnotationHandler(AnnotationConfiguration annotationConfig)
/*     */     {
/* 638 */       this._annotationConfig = annotationConfig;
/*     */     }
/*     */     
/*     */     public boolean includeConstructorAnnotation(Annotation ann)
/*     */     {
/* 643 */       Class<? extends Annotation> annType = ann.annotationType();
/* 644 */       if (this._constructorInclusions == null) {
/* 645 */         this._constructorInclusions = new HashMap();
/*     */       } else {
/* 647 */         AnnotationInclusion incl = (AnnotationInclusion)this._constructorInclusions.get(annType);
/* 648 */         if (incl != null) {
/* 649 */           return incl != AnnotationInclusion.DONT_INCLUDE;
/*     */         }
/*     */       }
/* 652 */       AnnotationInclusion incl = this._annotationConfig.getInclusionForConstructor(annType);
/* 653 */       this._constructorInclusions.put(annType, incl);
/* 654 */       return incl != AnnotationInclusion.DONT_INCLUDE;
/*     */     }
/*     */     
/*     */     public boolean includeFieldAnnotation(Annotation ann)
/*     */     {
/* 659 */       Class<? extends Annotation> annType = ann.annotationType();
/* 660 */       if (this._fieldInclusions == null) {
/* 661 */         this._fieldInclusions = new HashMap();
/*     */       } else {
/* 663 */         AnnotationInclusion incl = (AnnotationInclusion)this._fieldInclusions.get(annType);
/* 664 */         if (incl != null) {
/* 665 */           return incl != AnnotationInclusion.DONT_INCLUDE;
/*     */         }
/*     */       }
/* 668 */       AnnotationInclusion incl = this._annotationConfig.getInclusionForField(annType);
/* 669 */       this._fieldInclusions.put(annType, incl);
/* 670 */       return incl != AnnotationInclusion.DONT_INCLUDE;
/*     */     }
/*     */     
/*     */     public boolean includeMethodAnnotation(Annotation ann)
/*     */     {
/* 675 */       return methodInclusion(ann) != AnnotationInclusion.DONT_INCLUDE;
/*     */     }
/*     */     
/*     */     public AnnotationInclusion methodInclusion(Annotation ann)
/*     */     {
/* 680 */       Class<? extends Annotation> annType = ann.annotationType();
/* 681 */       if (this._methodInclusions == null) {
/* 682 */         this._methodInclusions = new HashMap();
/*     */       } else {
/* 684 */         AnnotationInclusion incl = (AnnotationInclusion)this._methodInclusions.get(annType);
/* 685 */         if (incl != null) {
/* 686 */           return incl;
/*     */         }
/*     */       }
/* 689 */       AnnotationInclusion incl = this._annotationConfig.getInclusionForMethod(annType);
/* 690 */       this._methodInclusions.put(annType, incl);
/* 691 */       return incl;
/*     */     }
/*     */     
/*     */     public boolean includeParameterAnnotation(Annotation ann)
/*     */     {
/* 696 */       return parameterInclusion(ann) != AnnotationInclusion.DONT_INCLUDE;
/*     */     }
/*     */     
/*     */     public AnnotationInclusion parameterInclusion(Annotation ann)
/*     */     {
/* 701 */       Class<? extends Annotation> annType = ann.annotationType();
/* 702 */       if (this._parameterInclusions == null) {
/* 703 */         this._parameterInclusions = new HashMap();
/*     */       } else {
/* 705 */         AnnotationInclusion incl = (AnnotationInclusion)this._parameterInclusions.get(annType);
/* 706 */         if (incl != null) {
/* 707 */           return incl;
/*     */         }
/*     */       }
/* 710 */       AnnotationInclusion incl = this._annotationConfig.getInclusionForParameter(annType);
/* 711 */       this._parameterInclusions.put(annType, incl);
/* 712 */       return incl;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\classmate-1.3.4.jar!\com\fasterxml\classmate\ResolvedTypeWithMembers.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */