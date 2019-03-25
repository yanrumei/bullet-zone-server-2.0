/*      */ package com.fasterxml.jackson.databind.introspect;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*      */ import com.fasterxml.jackson.annotation.JsonProperty.Access;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty;
/*      */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*      */ import com.fasterxml.jackson.databind.PropertyName;
/*      */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*      */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Set;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class POJOPropertyBuilder
/*      */   extends BeanPropertyDefinition
/*      */   implements Comparable<POJOPropertyBuilder>
/*      */ {
/*      */   protected final boolean _forSerialization;
/*      */   protected final MapperConfig<?> _config;
/*      */   protected final AnnotationIntrospector _annotationIntrospector;
/*      */   protected final PropertyName _name;
/*      */   protected final PropertyName _internalName;
/*      */   protected Linked<AnnotatedField> _fields;
/*      */   protected Linked<AnnotatedParameter> _ctorParameters;
/*      */   protected Linked<AnnotatedMethod> _getters;
/*      */   protected Linked<AnnotatedMethod> _setters;
/*      */   
/*      */   public POJOPropertyBuilder(MapperConfig<?> config, AnnotationIntrospector ai, boolean forSerialization, PropertyName internalName)
/*      */   {
/*   52 */     this(config, ai, forSerialization, internalName, internalName);
/*      */   }
/*      */   
/*      */ 
/*      */   protected POJOPropertyBuilder(MapperConfig<?> config, AnnotationIntrospector ai, boolean forSerialization, PropertyName internalName, PropertyName name)
/*      */   {
/*   58 */     this._config = config;
/*   59 */     this._annotationIntrospector = ai;
/*   60 */     this._internalName = internalName;
/*   61 */     this._name = name;
/*   62 */     this._forSerialization = forSerialization;
/*      */   }
/*      */   
/*      */   public POJOPropertyBuilder(POJOPropertyBuilder src, PropertyName newName)
/*      */   {
/*   67 */     this._config = src._config;
/*   68 */     this._annotationIntrospector = src._annotationIntrospector;
/*   69 */     this._internalName = src._internalName;
/*   70 */     this._name = newName;
/*   71 */     this._fields = src._fields;
/*   72 */     this._ctorParameters = src._ctorParameters;
/*   73 */     this._getters = src._getters;
/*   74 */     this._setters = src._setters;
/*   75 */     this._forSerialization = src._forSerialization;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public POJOPropertyBuilder withName(PropertyName newName)
/*      */   {
/*   86 */     return new POJOPropertyBuilder(this, newName);
/*      */   }
/*      */   
/*      */ 
/*      */   public POJOPropertyBuilder withSimpleName(String newSimpleName)
/*      */   {
/*   92 */     PropertyName newName = this._name.withSimpleName(newSimpleName);
/*   93 */     return newName == this._name ? this : new POJOPropertyBuilder(this, newName);
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
/*      */   public int compareTo(POJOPropertyBuilder other)
/*      */   {
/*  108 */     if (this._ctorParameters != null) {
/*  109 */       if (other._ctorParameters == null) {
/*  110 */         return -1;
/*      */       }
/*  112 */     } else if (other._ctorParameters != null) {
/*  113 */       return 1;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  118 */     return getName().compareTo(other.getName());
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String getName()
/*      */   {
/*  129 */     return this._name == null ? null : this._name.getSimpleName();
/*      */   }
/*      */   
/*      */   public PropertyName getFullName()
/*      */   {
/*  134 */     return this._name;
/*      */   }
/*      */   
/*      */   public boolean hasName(PropertyName name)
/*      */   {
/*  139 */     return this._name.equals(name);
/*      */   }
/*      */   
/*      */   public String getInternalName() {
/*  143 */     return this._internalName.getSimpleName();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public PropertyName getWrapperName()
/*      */   {
/*  152 */     AnnotatedMember member = getPrimaryMember();
/*  153 */     return (member == null) || (this._annotationIntrospector == null) ? null : this._annotationIntrospector.findWrapperName(member);
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
/*      */   public boolean isExplicitlyIncluded()
/*      */   {
/*  167 */     return (_anyExplicits(this._fields)) || (_anyExplicits(this._getters)) || (_anyExplicits(this._setters)) || (_anyExplicitNames(this._ctorParameters));
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
/*      */   public boolean isExplicitlyNamed()
/*      */   {
/*  180 */     return (_anyExplicitNames(this._fields)) || (_anyExplicitNames(this._getters)) || (_anyExplicitNames(this._setters)) || (_anyExplicitNames(this._ctorParameters));
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
/*      */   public boolean hasGetter()
/*      */   {
/*  194 */     return this._getters != null;
/*      */   }
/*      */   
/*  197 */   public boolean hasSetter() { return this._setters != null; }
/*      */   
/*      */   public boolean hasField() {
/*  200 */     return this._fields != null;
/*      */   }
/*      */   
/*  203 */   public boolean hasConstructorParameter() { return this._ctorParameters != null; }
/*      */   
/*      */   public boolean couldDeserialize()
/*      */   {
/*  207 */     return (this._ctorParameters != null) || (this._setters != null) || (this._fields != null);
/*      */   }
/*      */   
/*      */   public boolean couldSerialize()
/*      */   {
/*  212 */     return (this._getters != null) || (this._fields != null);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public AnnotatedMethod getGetter()
/*      */   {
/*  219 */     Linked<AnnotatedMethod> curr = this._getters;
/*  220 */     if (curr == null) {
/*  221 */       return null;
/*      */     }
/*  223 */     Linked<AnnotatedMethod> next = curr.next;
/*  224 */     if (next == null) {
/*  225 */       return (AnnotatedMethod)curr.value;
/*      */     }
/*  228 */     for (; 
/*  228 */         next != null; next = next.next)
/*      */     {
/*      */ 
/*      */ 
/*  232 */       Class<?> currClass = ((AnnotatedMethod)curr.value).getDeclaringClass();
/*  233 */       Class<?> nextClass = ((AnnotatedMethod)next.value).getDeclaringClass();
/*  234 */       if (currClass != nextClass) {
/*  235 */         if (currClass.isAssignableFrom(nextClass)) {
/*  236 */           curr = next;
/*      */ 
/*      */         }
/*  239 */         else if (nextClass.isAssignableFrom(currClass)) {}
/*      */ 
/*      */ 
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/*      */ 
/*      */ 
/*      */ 
/*  249 */         int priNext = _getterPriority((AnnotatedMethod)next.value);
/*  250 */         int priCurr = _getterPriority((AnnotatedMethod)curr.value);
/*      */         
/*  252 */         if (priNext != priCurr) {
/*  253 */           if (priNext < priCurr) {
/*  254 */             curr = next;
/*      */           }
/*      */         }
/*      */         else {
/*  258 */           throw new IllegalArgumentException("Conflicting getter definitions for property \"" + getName() + "\": " + ((AnnotatedMethod)curr.value).getFullName() + " vs " + ((AnnotatedMethod)next.value).getFullName());
/*      */         }
/*      */       }
/*      */     }
/*  262 */     this._getters = curr.withoutNext();
/*  263 */     return (AnnotatedMethod)curr.value;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public AnnotatedMethod getSetter()
/*      */   {
/*  270 */     Linked<AnnotatedMethod> curr = this._setters;
/*  271 */     if (curr == null) {
/*  272 */       return null;
/*      */     }
/*  274 */     Linked<AnnotatedMethod> next = curr.next;
/*  275 */     if (next == null) {
/*  276 */       return (AnnotatedMethod)curr.value;
/*      */     }
/*  279 */     for (; 
/*  279 */         next != null; next = next.next)
/*      */     {
/*  281 */       Class<?> currClass = ((AnnotatedMethod)curr.value).getDeclaringClass();
/*  282 */       Class<?> nextClass = ((AnnotatedMethod)next.value).getDeclaringClass();
/*  283 */       if (currClass != nextClass) {
/*  284 */         if (currClass.isAssignableFrom(nextClass)) {
/*  285 */           curr = next;
/*      */ 
/*      */         }
/*  288 */         else if (nextClass.isAssignableFrom(currClass)) {}
/*      */       }
/*      */       else
/*      */       {
/*  292 */         AnnotatedMethod nextM = (AnnotatedMethod)next.value;
/*  293 */         AnnotatedMethod currM = (AnnotatedMethod)curr.value;
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  300 */         int priNext = _setterPriority(nextM);
/*  301 */         int priCurr = _setterPriority(currM);
/*      */         
/*  303 */         if (priNext != priCurr) {
/*  304 */           if (priNext < priCurr) {
/*  305 */             curr = next;
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  310 */           if (this._annotationIntrospector != null) {
/*  311 */             AnnotatedMethod pref = this._annotationIntrospector.resolveSetterConflict(this._config, currM, nextM);
/*      */             
/*      */ 
/*      */ 
/*  315 */             if (pref == currM) {
/*      */               continue;
/*      */             }
/*  318 */             if (pref == nextM) {
/*  319 */               curr = next;
/*  320 */               continue;
/*      */             }
/*      */           }
/*  323 */           throw new IllegalArgumentException(String.format("Conflicting setter definitions for property \"%s\": %s vs %s", new Object[] { getName(), ((AnnotatedMethod)curr.value).getFullName(), ((AnnotatedMethod)next.value).getFullName() }));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*  328 */     this._setters = curr.withoutNext();
/*  329 */     return (AnnotatedMethod)curr.value;
/*      */   }
/*      */   
/*      */ 
/*      */   public AnnotatedField getField()
/*      */   {
/*  335 */     if (this._fields == null) {
/*  336 */       return null;
/*      */     }
/*      */     
/*  339 */     AnnotatedField field = (AnnotatedField)this._fields.value;
/*  340 */     for (Linked<AnnotatedField> next = this._fields.next; 
/*  341 */         next != null; next = next.next) {
/*  342 */       AnnotatedField nextField = (AnnotatedField)next.value;
/*  343 */       Class<?> fieldClass = field.getDeclaringClass();
/*  344 */       Class<?> nextClass = nextField.getDeclaringClass();
/*  345 */       if (fieldClass != nextClass) {
/*  346 */         if (fieldClass.isAssignableFrom(nextClass)) {
/*  347 */           field = nextField;
/*      */ 
/*      */         }
/*  350 */         else if (nextClass.isAssignableFrom(fieldClass)) {}
/*      */ 
/*      */       }
/*      */       else {
/*  354 */         throw new IllegalArgumentException("Multiple fields representing property \"" + getName() + "\": " + field.getFullName() + " vs " + nextField.getFullName());
/*      */       }
/*      */     }
/*  357 */     return field;
/*      */   }
/*      */   
/*      */ 
/*      */   public AnnotatedParameter getConstructorParameter()
/*      */   {
/*  363 */     if (this._ctorParameters == null) {
/*  364 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  374 */     Linked<AnnotatedParameter> curr = this._ctorParameters;
/*      */     do {
/*  376 */       if ((((AnnotatedParameter)curr.value).getOwner() instanceof AnnotatedConstructor)) {
/*  377 */         return (AnnotatedParameter)curr.value;
/*      */       }
/*  379 */       curr = curr.next;
/*  380 */     } while (curr != null);
/*  381 */     return (AnnotatedParameter)this._ctorParameters.value;
/*      */   }
/*      */   
/*      */   public Iterator<AnnotatedParameter> getConstructorParameters()
/*      */   {
/*  386 */     if (this._ctorParameters == null) {
/*  387 */       return ClassUtil.emptyIterator();
/*      */     }
/*  389 */     return new MemberIterator(this._ctorParameters);
/*      */   }
/*      */   
/*      */ 
/*      */   public AnnotatedMember getAccessor()
/*      */   {
/*  395 */     AnnotatedMember m = getGetter();
/*  396 */     if (m == null) {
/*  397 */       m = getField();
/*      */     }
/*  399 */     return m;
/*      */   }
/*      */   
/*      */ 
/*      */   public AnnotatedMember getMutator()
/*      */   {
/*  405 */     AnnotatedMember m = getConstructorParameter();
/*  406 */     if (m == null) {
/*  407 */       m = getSetter();
/*  408 */       if (m == null) {
/*  409 */         m = getField();
/*      */       }
/*      */     }
/*  412 */     return m;
/*      */   }
/*      */   
/*      */   public AnnotatedMember getNonConstructorMutator()
/*      */   {
/*  417 */     AnnotatedMember m = getSetter();
/*  418 */     if (m == null) {
/*  419 */       m = getField();
/*      */     }
/*  421 */     return m;
/*      */   }
/*      */   
/*      */   public AnnotatedMember getPrimaryMember()
/*      */   {
/*  426 */     if (this._forSerialization) {
/*  427 */       return getAccessor();
/*      */     }
/*  429 */     return getMutator();
/*      */   }
/*      */   
/*      */   protected int _getterPriority(AnnotatedMethod m)
/*      */   {
/*  434 */     String name = m.getName();
/*      */     
/*  436 */     if ((name.startsWith("get")) && (name.length() > 3))
/*      */     {
/*  438 */       return 1;
/*      */     }
/*  440 */     if ((name.startsWith("is")) && (name.length() > 2)) {
/*  441 */       return 2;
/*      */     }
/*  443 */     return 3;
/*      */   }
/*      */   
/*      */   protected int _setterPriority(AnnotatedMethod m)
/*      */   {
/*  448 */     String name = m.getName();
/*  449 */     if ((name.startsWith("set")) && (name.length() > 3))
/*      */     {
/*  451 */       return 1;
/*      */     }
/*  453 */     return 2;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Class<?>[] findViews()
/*      */   {
/*  464 */     (Class[])fromMemberAnnotations(new WithMember()
/*      */     {
/*      */       public Class<?>[] withMember(AnnotatedMember member) {
/*  467 */         return POJOPropertyBuilder.this._annotationIntrospector.findViews(member);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   public AnnotationIntrospector.ReferenceProperty findReferenceType()
/*      */   {
/*  474 */     (AnnotationIntrospector.ReferenceProperty)fromMemberAnnotations(new WithMember()
/*      */     {
/*      */       public AnnotationIntrospector.ReferenceProperty withMember(AnnotatedMember member) {
/*  477 */         return POJOPropertyBuilder.this._annotationIntrospector.findReferenceType(member);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   public boolean isTypeId()
/*      */   {
/*  484 */     Boolean b = (Boolean)fromMemberAnnotations(new WithMember()
/*      */     {
/*      */       public Boolean withMember(AnnotatedMember member) {
/*  487 */         return POJOPropertyBuilder.this._annotationIntrospector.isTypeId(member);
/*      */       }
/*  489 */     });
/*  490 */     return (b != null) && (b.booleanValue());
/*      */   }
/*      */   
/*      */   public PropertyMetadata getMetadata()
/*      */   {
/*  495 */     Boolean b = _findRequired();
/*  496 */     String desc = _findDescription();
/*  497 */     Integer idx = _findIndex();
/*  498 */     String def = _findDefaultValue();
/*  499 */     if ((b == null) && (idx == null) && (def == null)) {
/*  500 */       return desc == null ? PropertyMetadata.STD_REQUIRED_OR_OPTIONAL : PropertyMetadata.STD_REQUIRED_OR_OPTIONAL.withDescription(desc);
/*      */     }
/*      */     
/*  503 */     return PropertyMetadata.construct(b, desc, idx, def);
/*      */   }
/*      */   
/*      */   protected Boolean _findRequired() {
/*  507 */     (Boolean)fromMemberAnnotations(new WithMember()
/*      */     {
/*      */       public Boolean withMember(AnnotatedMember member) {
/*  510 */         return POJOPropertyBuilder.this._annotationIntrospector.hasRequiredMarker(member);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   protected String _findDescription() {
/*  516 */     (String)fromMemberAnnotations(new WithMember()
/*      */     {
/*      */       public String withMember(AnnotatedMember member) {
/*  519 */         return POJOPropertyBuilder.this._annotationIntrospector.findPropertyDescription(member);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   protected Integer _findIndex() {
/*  525 */     (Integer)fromMemberAnnotations(new WithMember()
/*      */     {
/*      */       public Integer withMember(AnnotatedMember member) {
/*  528 */         return POJOPropertyBuilder.this._annotationIntrospector.findPropertyIndex(member);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   protected String _findDefaultValue() {
/*  534 */     (String)fromMemberAnnotations(new WithMember()
/*      */     {
/*      */       public String withMember(AnnotatedMember member) {
/*  537 */         return POJOPropertyBuilder.this._annotationIntrospector.findPropertyDefaultValue(member);
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   public ObjectIdInfo findObjectIdInfo()
/*      */   {
/*  544 */     (ObjectIdInfo)fromMemberAnnotations(new WithMember()
/*      */     {
/*      */       public ObjectIdInfo withMember(AnnotatedMember member) {
/*  547 */         ObjectIdInfo info = POJOPropertyBuilder.this._annotationIntrospector.findObjectIdInfo(member);
/*  548 */         if (info != null) {
/*  549 */           info = POJOPropertyBuilder.this._annotationIntrospector.findObjectReferenceInfo(member, info);
/*      */         }
/*  551 */         return info;
/*      */       }
/*      */     });
/*      */   }
/*      */   
/*      */   public JsonInclude.Value findInclusion()
/*      */   {
/*  558 */     AnnotatedMember a = getAccessor();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*  563 */     JsonInclude.Value v = this._annotationIntrospector == null ? null : this._annotationIntrospector.findPropertyInclusion(a);
/*      */     
/*  565 */     return v == null ? JsonInclude.Value.empty() : v;
/*      */   }
/*      */   
/*      */   public JsonProperty.Access findAccess() {
/*  569 */     (JsonProperty.Access)fromMemberAnnotationsExcept(new WithMember()
/*      */     {
/*      */ 
/*  572 */       public JsonProperty.Access withMember(AnnotatedMember member) { return POJOPropertyBuilder.this._annotationIntrospector.findPropertyAccess(member); } }, JsonProperty.Access.AUTO);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addField(AnnotatedField a, PropertyName name, boolean explName, boolean visible, boolean ignored)
/*      */   {
/*  584 */     this._fields = new Linked(a, this._fields, name, explName, visible, ignored);
/*      */   }
/*      */   
/*      */   public void addCtor(AnnotatedParameter a, PropertyName name, boolean explName, boolean visible, boolean ignored) {
/*  588 */     this._ctorParameters = new Linked(a, this._ctorParameters, name, explName, visible, ignored);
/*      */   }
/*      */   
/*      */   public void addGetter(AnnotatedMethod a, PropertyName name, boolean explName, boolean visible, boolean ignored) {
/*  592 */     this._getters = new Linked(a, this._getters, name, explName, visible, ignored);
/*      */   }
/*      */   
/*      */   public void addSetter(AnnotatedMethod a, PropertyName name, boolean explName, boolean visible, boolean ignored) {
/*  596 */     this._setters = new Linked(a, this._setters, name, explName, visible, ignored);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void addAll(POJOPropertyBuilder src)
/*      */   {
/*  605 */     this._fields = merge(this._fields, src._fields);
/*  606 */     this._ctorParameters = merge(this._ctorParameters, src._ctorParameters);
/*  607 */     this._getters = merge(this._getters, src._getters);
/*  608 */     this._setters = merge(this._setters, src._setters);
/*      */   }
/*      */   
/*      */   private static <T> Linked<T> merge(Linked<T> chain1, Linked<T> chain2)
/*      */   {
/*  613 */     if (chain1 == null) {
/*  614 */       return chain2;
/*      */     }
/*  616 */     if (chain2 == null) {
/*  617 */       return chain1;
/*      */     }
/*  619 */     return chain1.append(chain2);
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
/*      */   public void removeIgnored()
/*      */   {
/*  634 */     this._fields = _removeIgnored(this._fields);
/*  635 */     this._getters = _removeIgnored(this._getters);
/*  636 */     this._setters = _removeIgnored(this._setters);
/*  637 */     this._ctorParameters = _removeIgnored(this._ctorParameters);
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
/*      */   public JsonProperty.Access removeNonVisible(boolean inferMutators)
/*      */   {
/*  650 */     JsonProperty.Access acc = findAccess();
/*  651 */     if (acc == null) {
/*  652 */       acc = JsonProperty.Access.AUTO;
/*      */     }
/*  654 */     switch (acc)
/*      */     {
/*      */     case READ_ONLY: 
/*  657 */       this._setters = null;
/*  658 */       this._ctorParameters = null;
/*  659 */       if (!this._forSerialization) {
/*  660 */         this._fields = null;
/*      */       }
/*      */       
/*      */ 
/*      */       break;
/*      */     case READ_WRITE: 
/*      */       break;
/*      */     case WRITE_ONLY: 
/*  668 */       this._getters = null;
/*  669 */       if (this._forSerialization) {
/*  670 */         this._fields = null;
/*      */       }
/*      */       break;
/*      */     case AUTO: 
/*      */     default: 
/*  675 */       this._getters = _removeNonVisible(this._getters);
/*  676 */       this._ctorParameters = _removeNonVisible(this._ctorParameters);
/*      */       
/*  678 */       if ((!inferMutators) || (this._getters == null)) {
/*  679 */         this._fields = _removeNonVisible(this._fields);
/*  680 */         this._setters = _removeNonVisible(this._setters);
/*      */       }
/*      */       break; }
/*  683 */     return acc;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void removeConstructors()
/*      */   {
/*  692 */     this._ctorParameters = null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void trimByVisibility()
/*      */   {
/*  702 */     this._fields = _trimByVisibility(this._fields);
/*  703 */     this._getters = _trimByVisibility(this._getters);
/*  704 */     this._setters = _trimByVisibility(this._setters);
/*  705 */     this._ctorParameters = _trimByVisibility(this._ctorParameters);
/*      */   }
/*      */   
/*      */ 
/*      */   public void mergeAnnotations(boolean forSerialization)
/*      */   {
/*  711 */     if (forSerialization) {
/*  712 */       if (this._getters != null) {
/*  713 */         AnnotationMap ann = _mergeAnnotations(0, new Linked[] { this._getters, this._fields, this._ctorParameters, this._setters });
/*  714 */         this._getters = _applyAnnotations(this._getters, ann);
/*  715 */       } else if (this._fields != null) {
/*  716 */         AnnotationMap ann = _mergeAnnotations(0, new Linked[] { this._fields, this._ctorParameters, this._setters });
/*  717 */         this._fields = _applyAnnotations(this._fields, ann);
/*      */       }
/*      */     }
/*  720 */     else if (this._ctorParameters != null) {
/*  721 */       AnnotationMap ann = _mergeAnnotations(0, new Linked[] { this._ctorParameters, this._setters, this._fields, this._getters });
/*  722 */       this._ctorParameters = _applyAnnotations(this._ctorParameters, ann);
/*  723 */     } else if (this._setters != null) {
/*  724 */       AnnotationMap ann = _mergeAnnotations(0, new Linked[] { this._setters, this._fields, this._getters });
/*  725 */       this._setters = _applyAnnotations(this._setters, ann);
/*  726 */     } else if (this._fields != null) {
/*  727 */       AnnotationMap ann = _mergeAnnotations(0, new Linked[] { this._fields, this._getters });
/*  728 */       this._fields = _applyAnnotations(this._fields, ann);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private AnnotationMap _mergeAnnotations(int index, Linked<? extends AnnotatedMember>... nodes)
/*      */   {
/*  736 */     AnnotationMap ann = _getAllAnnotations(nodes[index]);
/*  737 */     do { index++; if (index >= nodes.length) break;
/*  738 */     } while (nodes[index] == null);
/*  739 */     return AnnotationMap.merge(ann, _mergeAnnotations(index, nodes));
/*      */     
/*      */ 
/*  742 */     return ann;
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
/*      */   private <T extends AnnotatedMember> AnnotationMap _getAllAnnotations(Linked<T> node)
/*      */   {
/*  755 */     AnnotationMap ann = ((AnnotatedMember)node.value).getAllAnnotations();
/*  756 */     if (node.next != null) {
/*  757 */       ann = AnnotationMap.merge(ann, _getAllAnnotations(node.next));
/*      */     }
/*  759 */     return ann;
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
/*      */   private <T extends AnnotatedMember> Linked<T> _applyAnnotations(Linked<T> node, AnnotationMap ann)
/*      */   {
/*  773 */     T value = (AnnotatedMember)((AnnotatedMember)node.value).withAnnotations(ann);
/*  774 */     if (node.next != null) {
/*  775 */       node = node.withNext(_applyAnnotations(node.next, ann));
/*      */     }
/*  777 */     return node.withValue(value);
/*      */   }
/*      */   
/*      */   private <T> Linked<T> _removeIgnored(Linked<T> node)
/*      */   {
/*  782 */     if (node == null) {
/*  783 */       return node;
/*      */     }
/*  785 */     return node.withoutIgnored();
/*      */   }
/*      */   
/*      */   private <T> Linked<T> _removeNonVisible(Linked<T> node)
/*      */   {
/*  790 */     if (node == null) {
/*  791 */       return node;
/*      */     }
/*  793 */     return node.withoutNonVisible();
/*      */   }
/*      */   
/*      */   private <T> Linked<T> _trimByVisibility(Linked<T> node)
/*      */   {
/*  798 */     if (node == null) {
/*  799 */       return node;
/*      */     }
/*  801 */     return node.trimByVisibility();
/*      */   }
/*      */   
/*      */   private <T> boolean _anyExplicits(Linked<T> n)
/*      */   {
/*  812 */     for (; 
/*      */         
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  812 */         n != null; n = n.next) {
/*  813 */       if ((n.name != null) && (n.name.hasSimpleName())) {
/*  814 */         return true;
/*      */       }
/*      */     }
/*  817 */     return false;
/*      */   }
/*      */   
/*      */   private <T> boolean _anyExplicitNames(Linked<T> n)
/*      */   {
/*  822 */     for (; n != null; n = n.next) {
/*  823 */       if ((n.name != null) && (n.isNameExplicit)) {
/*  824 */         return true;
/*      */       }
/*      */     }
/*  827 */     return false;
/*      */   }
/*      */   
/*      */   public boolean anyVisible() {
/*  831 */     return (_anyVisible(this._fields)) || (_anyVisible(this._getters)) || (_anyVisible(this._setters)) || (_anyVisible(this._ctorParameters));
/*      */   }
/*      */   
/*      */   private <T> boolean _anyVisible(Linked<T> n)
/*      */   {
/*  840 */     for (; 
/*      */         
/*      */ 
/*      */ 
/*  840 */         n != null; n = n.next) {
/*  841 */       if (n.isVisible) {
/*  842 */         return true;
/*      */       }
/*      */     }
/*  845 */     return false;
/*      */   }
/*      */   
/*      */   public boolean anyIgnorals() {
/*  849 */     return (_anyIgnorals(this._fields)) || (_anyIgnorals(this._getters)) || (_anyIgnorals(this._setters)) || (_anyIgnorals(this._ctorParameters));
/*      */   }
/*      */   
/*      */   private <T> boolean _anyIgnorals(Linked<T> n)
/*      */   {
/*  858 */     for (; 
/*      */         
/*      */ 
/*      */ 
/*  858 */         n != null; n = n.next) {
/*  859 */       if (n.isMarkedIgnored) {
/*  860 */         return true;
/*      */       }
/*      */     }
/*  863 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Set<PropertyName> findExplicitNames()
/*      */   {
/*  874 */     Set<PropertyName> renamed = null;
/*  875 */     renamed = _findExplicitNames(this._fields, renamed);
/*  876 */     renamed = _findExplicitNames(this._getters, renamed);
/*  877 */     renamed = _findExplicitNames(this._setters, renamed);
/*  878 */     renamed = _findExplicitNames(this._ctorParameters, renamed);
/*  879 */     if (renamed == null) {
/*  880 */       return Collections.emptySet();
/*      */     }
/*  882 */     return renamed;
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
/*      */   public Collection<POJOPropertyBuilder> explode(Collection<PropertyName> newNames)
/*      */   {
/*  895 */     HashMap<PropertyName, POJOPropertyBuilder> props = new HashMap();
/*  896 */     _explode(newNames, props, this._fields);
/*  897 */     _explode(newNames, props, this._getters);
/*  898 */     _explode(newNames, props, this._setters);
/*  899 */     _explode(newNames, props, this._ctorParameters);
/*  900 */     return props.values();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void _explode(Collection<PropertyName> newNames, Map<PropertyName, POJOPropertyBuilder> props, Linked<?> accessors)
/*      */   {
/*  908 */     Linked<?> firstAcc = accessors;
/*  909 */     for (Linked<?> node = accessors; node != null; node = node.next) {
/*  910 */       PropertyName name = node.name;
/*  911 */       if ((!node.isNameExplicit) || (name == null))
/*      */       {
/*  913 */         if (node.isVisible)
/*      */         {
/*      */ 
/*      */ 
/*  917 */           throw new IllegalStateException("Conflicting/ambiguous property name definitions (implicit name '" + this._name + "'): found multiple explicit names: " + newNames + ", but also implicit accessor: " + node);
/*      */         }
/*      */       }
/*      */       else {
/*  921 */         POJOPropertyBuilder prop = (POJOPropertyBuilder)props.get(name);
/*  922 */         if (prop == null) {
/*  923 */           prop = new POJOPropertyBuilder(this._config, this._annotationIntrospector, this._forSerialization, this._internalName, name);
/*      */           
/*  925 */           props.put(name, prop);
/*      */         }
/*      */         
/*  928 */         if (firstAcc == this._fields) {
/*  929 */           Linked<AnnotatedField> n2 = node;
/*  930 */           prop._fields = n2.withNext(prop._fields);
/*  931 */         } else if (firstAcc == this._getters) {
/*  932 */           Linked<AnnotatedMethod> n2 = node;
/*  933 */           prop._getters = n2.withNext(prop._getters);
/*  934 */         } else if (firstAcc == this._setters) {
/*  935 */           Linked<AnnotatedMethod> n2 = node;
/*  936 */           prop._setters = n2.withNext(prop._setters);
/*  937 */         } else if (firstAcc == this._ctorParameters) {
/*  938 */           Linked<AnnotatedParameter> n2 = node;
/*  939 */           prop._ctorParameters = n2.withNext(prop._ctorParameters);
/*      */         } else {
/*  941 */           throw new IllegalStateException("Internal error: mismatched accessors, property: " + this);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private Set<PropertyName> _findExplicitNames(Linked<? extends AnnotatedMember> node, Set<PropertyName> renamed)
/*      */   {
/*  949 */     for (; node != null; node = node.next)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  956 */       if ((node.isNameExplicit) && (node.name != null))
/*      */       {
/*      */ 
/*  959 */         if (renamed == null) {
/*  960 */           renamed = new HashSet();
/*      */         }
/*  962 */         renamed.add(node.name);
/*      */       } }
/*  964 */     return renamed;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String toString()
/*      */   {
/*  971 */     StringBuilder sb = new StringBuilder();
/*  972 */     sb.append("[Property '").append(this._name).append("'; ctors: ").append(this._ctorParameters).append(", field(s): ").append(this._fields).append(", getter(s): ").append(this._getters).append(", setter(s): ").append(this._setters);
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  978 */     sb.append("]");
/*  979 */     return sb.toString();
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
/*      */   protected <T> T fromMemberAnnotations(WithMember<T> func)
/*      */   {
/*  994 */     T result = null;
/*  995 */     if (this._annotationIntrospector != null) {
/*  996 */       if (this._forSerialization) {
/*  997 */         if (this._getters != null) {
/*  998 */           result = func.withMember((AnnotatedMember)this._getters.value);
/*      */         }
/*      */       } else {
/* 1001 */         if (this._ctorParameters != null) {
/* 1002 */           result = func.withMember((AnnotatedMember)this._ctorParameters.value);
/*      */         }
/* 1004 */         if ((result == null) && (this._setters != null)) {
/* 1005 */           result = func.withMember((AnnotatedMember)this._setters.value);
/*      */         }
/*      */       }
/* 1008 */       if ((result == null) && (this._fields != null)) {
/* 1009 */         result = func.withMember((AnnotatedMember)this._fields.value);
/*      */       }
/*      */     }
/* 1012 */     return result;
/*      */   }
/*      */   
/*      */   protected <T> T fromMemberAnnotationsExcept(WithMember<T> func, T defaultValue)
/*      */   {
/* 1017 */     if (this._annotationIntrospector == null) {
/* 1018 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1023 */     if (this._forSerialization) {
/* 1024 */       if (this._getters != null) {
/* 1025 */         T result = func.withMember((AnnotatedMember)this._getters.value);
/* 1026 */         if ((result != null) && (result != defaultValue)) {
/* 1027 */           return result;
/*      */         }
/*      */       }
/* 1030 */       if (this._fields != null) {
/* 1031 */         T result = func.withMember((AnnotatedMember)this._fields.value);
/* 1032 */         if ((result != null) && (result != defaultValue)) {
/* 1033 */           return result;
/*      */         }
/*      */       }
/* 1036 */       if (this._ctorParameters != null) {
/* 1037 */         T result = func.withMember((AnnotatedMember)this._ctorParameters.value);
/* 1038 */         if ((result != null) && (result != defaultValue)) {
/* 1039 */           return result;
/*      */         }
/*      */       }
/* 1042 */       if (this._setters != null) {
/* 1043 */         T result = func.withMember((AnnotatedMember)this._setters.value);
/* 1044 */         if ((result != null) && (result != defaultValue)) {
/* 1045 */           return result;
/*      */         }
/*      */       }
/* 1048 */       return null;
/*      */     }
/* 1050 */     if (this._ctorParameters != null) {
/* 1051 */       T result = func.withMember((AnnotatedMember)this._ctorParameters.value);
/* 1052 */       if ((result != null) && (result != defaultValue)) {
/* 1053 */         return result;
/*      */       }
/*      */     }
/* 1056 */     if (this._setters != null) {
/* 1057 */       T result = func.withMember((AnnotatedMember)this._setters.value);
/* 1058 */       if ((result != null) && (result != defaultValue)) {
/* 1059 */         return result;
/*      */       }
/*      */     }
/* 1062 */     if (this._fields != null) {
/* 1063 */       T result = func.withMember((AnnotatedMember)this._fields.value);
/* 1064 */       if ((result != null) && (result != defaultValue)) {
/* 1065 */         return result;
/*      */       }
/*      */     }
/* 1068 */     if (this._getters != null) {
/* 1069 */       T result = func.withMember((AnnotatedMember)this._getters.value);
/* 1070 */       if ((result != null) && (result != defaultValue)) {
/* 1071 */         return result;
/*      */       }
/*      */     }
/* 1074 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private static abstract interface WithMember<T>
/*      */   {
/*      */     public abstract T withMember(AnnotatedMember paramAnnotatedMember);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   protected static class MemberIterator<T extends AnnotatedMember>
/*      */     implements Iterator<T>
/*      */   {
/*      */     private POJOPropertyBuilder.Linked<T> next;
/*      */     
/*      */ 
/*      */ 
/*      */     public MemberIterator(POJOPropertyBuilder.Linked<T> first)
/*      */     {
/* 1096 */       this.next = first;
/*      */     }
/*      */     
/*      */     public boolean hasNext()
/*      */     {
/* 1101 */       return this.next != null;
/*      */     }
/*      */     
/*      */     public T next()
/*      */     {
/* 1106 */       if (this.next == null) throw new NoSuchElementException();
/* 1107 */       T result = (AnnotatedMember)this.next.value;
/* 1108 */       this.next = this.next.next;
/* 1109 */       return result;
/*      */     }
/*      */     
/*      */     public void remove()
/*      */     {
/* 1114 */       throw new UnsupportedOperationException();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   protected static final class Linked<T>
/*      */   {
/*      */     public final T value;
/*      */     
/*      */     public final Linked<T> next;
/*      */     
/*      */     public final PropertyName name;
/*      */     
/*      */     public final boolean isNameExplicit;
/*      */     
/*      */     public final boolean isVisible;
/*      */     
/*      */     public final boolean isMarkedIgnored;
/*      */     
/*      */ 
/*      */     public Linked(T v, Linked<T> n, PropertyName name, boolean explName, boolean visible, boolean ignored)
/*      */     {
/* 1136 */       this.value = v;
/* 1137 */       this.next = n;
/*      */       
/* 1139 */       this.name = ((name == null) || (name.isEmpty()) ? null : name);
/*      */       
/* 1141 */       if (explName) {
/* 1142 */         if (this.name == null) {
/* 1143 */           throw new IllegalArgumentException("Can not pass true for 'explName' if name is null/empty");
/*      */         }
/*      */         
/*      */ 
/* 1147 */         if (!name.hasSimpleName()) {
/* 1148 */           explName = false;
/*      */         }
/*      */       }
/*      */       
/* 1152 */       this.isNameExplicit = explName;
/* 1153 */       this.isVisible = visible;
/* 1154 */       this.isMarkedIgnored = ignored;
/*      */     }
/*      */     
/*      */     public Linked<T> withoutNext() {
/* 1158 */       if (this.next == null) {
/* 1159 */         return this;
/*      */       }
/* 1161 */       return new Linked(this.value, null, this.name, this.isNameExplicit, this.isVisible, this.isMarkedIgnored);
/*      */     }
/*      */     
/*      */     public Linked<T> withValue(T newValue) {
/* 1165 */       if (newValue == this.value) {
/* 1166 */         return this;
/*      */       }
/* 1168 */       return new Linked(newValue, this.next, this.name, this.isNameExplicit, this.isVisible, this.isMarkedIgnored);
/*      */     }
/*      */     
/*      */     public Linked<T> withNext(Linked<T> newNext) {
/* 1172 */       if (newNext == this.next) {
/* 1173 */         return this;
/*      */       }
/* 1175 */       return new Linked(this.value, newNext, this.name, this.isNameExplicit, this.isVisible, this.isMarkedIgnored);
/*      */     }
/*      */     
/*      */     public Linked<T> withoutIgnored() {
/* 1179 */       if (this.isMarkedIgnored) {
/* 1180 */         return this.next == null ? null : this.next.withoutIgnored();
/*      */       }
/* 1182 */       if (this.next != null) {
/* 1183 */         Linked<T> newNext = this.next.withoutIgnored();
/* 1184 */         if (newNext != this.next) {
/* 1185 */           return withNext(newNext);
/*      */         }
/*      */       }
/* 1188 */       return this;
/*      */     }
/*      */     
/*      */     public Linked<T> withoutNonVisible() {
/* 1192 */       Linked<T> newNext = this.next == null ? null : this.next.withoutNonVisible();
/* 1193 */       return this.isVisible ? withNext(newNext) : newNext;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected Linked<T> append(Linked<T> appendable)
/*      */     {
/* 1201 */       if (this.next == null) {
/* 1202 */         return withNext(appendable);
/*      */       }
/* 1204 */       return withNext(this.next.append(appendable));
/*      */     }
/*      */     
/*      */     public Linked<T> trimByVisibility() {
/* 1208 */       if (this.next == null) {
/* 1209 */         return this;
/*      */       }
/* 1211 */       Linked<T> newNext = this.next.trimByVisibility();
/* 1212 */       if (this.name != null) {
/* 1213 */         if (newNext.name == null) {
/* 1214 */           return withNext(null);
/*      */         }
/*      */         
/* 1217 */         return withNext(newNext);
/*      */       }
/* 1219 */       if (newNext.name != null) {
/* 1220 */         return newNext;
/*      */       }
/*      */       
/* 1223 */       if (this.isVisible == newNext.isVisible) {
/* 1224 */         return withNext(newNext);
/*      */       }
/* 1226 */       return this.isVisible ? withNext(null) : newNext;
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1231 */       String msg = this.value.toString() + "[visible=" + this.isVisible + ",ignore=" + this.isMarkedIgnored + ",explicitName=" + this.isNameExplicit + "]";
/*      */       
/* 1233 */       if (this.next != null) {
/* 1234 */         msg = msg + ", " + this.next.toString();
/*      */       }
/* 1236 */       return msg;
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\POJOPropertyBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */