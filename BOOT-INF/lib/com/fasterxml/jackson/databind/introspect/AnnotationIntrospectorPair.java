/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonCreator.Mode;
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties.Value;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*     */ import com.fasterxml.jackson.annotation.JsonProperty.Access;
/*     */ import com.fasterxml.jackson.core.Version;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector.ReferenceProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer.None;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer.None;
/*     */ import com.fasterxml.jackson.databind.KeyDeserializer.None;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*     */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnnotationIntrospectorPair
/*     */   extends AnnotationIntrospector
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final AnnotationIntrospector _primary;
/*     */   protected final AnnotationIntrospector _secondary;
/*     */   
/*     */   public AnnotationIntrospectorPair(AnnotationIntrospector p, AnnotationIntrospector s)
/*     */   {
/*  50 */     this._primary = p;
/*  51 */     this._secondary = s;
/*     */   }
/*     */   
/*     */   public Version version()
/*     */   {
/*  56 */     return this._primary.version();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static AnnotationIntrospector create(AnnotationIntrospector primary, AnnotationIntrospector secondary)
/*     */   {
/*  67 */     if (primary == null) {
/*  68 */       return secondary;
/*     */     }
/*  70 */     if (secondary == null) {
/*  71 */       return primary;
/*     */     }
/*  73 */     return new AnnotationIntrospectorPair(primary, secondary);
/*     */   }
/*     */   
/*     */   public Collection<AnnotationIntrospector> allIntrospectors()
/*     */   {
/*  78 */     return allIntrospectors(new ArrayList());
/*     */   }
/*     */   
/*     */ 
/*     */   public Collection<AnnotationIntrospector> allIntrospectors(Collection<AnnotationIntrospector> result)
/*     */   {
/*  84 */     this._primary.allIntrospectors(result);
/*  85 */     this._secondary.allIntrospectors(result);
/*  86 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean isAnnotationBundle(Annotation ann)
/*     */   {
/*  93 */     return (this._primary.isAnnotationBundle(ann)) || (this._secondary.isAnnotationBundle(ann));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyName findRootName(AnnotatedClass ac)
/*     */   {
/* 105 */     PropertyName name1 = this._primary.findRootName(ac);
/* 106 */     if (name1 == null) {
/* 107 */       return this._secondary.findRootName(ac);
/*     */     }
/* 109 */     if (name1.hasSimpleName()) {
/* 110 */       return name1;
/*     */     }
/*     */     
/* 113 */     PropertyName name2 = this._secondary.findRootName(ac);
/* 114 */     return name2 == null ? name1 : name2;
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonIgnoreProperties.Value findPropertyIgnorals(Annotated a)
/*     */   {
/* 120 */     JsonIgnoreProperties.Value v2 = this._secondary.findPropertyIgnorals(a);
/* 121 */     JsonIgnoreProperties.Value v1 = this._primary.findPropertyIgnorals(a);
/*     */     
/* 123 */     if (v2 == null) {
/* 124 */       return v1;
/*     */     }
/* 126 */     return v2.withOverrides(v1);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public String[] findPropertiesToIgnore(Annotated ac)
/*     */   {
/* 132 */     String[] result = this._primary.findPropertiesToIgnore(ac);
/* 133 */     if (result == null) {
/* 134 */       result = this._secondary.findPropertiesToIgnore(ac);
/*     */     }
/* 136 */     return result;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public String[] findPropertiesToIgnore(Annotated ac, boolean forSerialization)
/*     */   {
/* 142 */     String[] result = this._primary.findPropertiesToIgnore(ac, forSerialization);
/* 143 */     if (result == null) {
/* 144 */       result = this._secondary.findPropertiesToIgnore(ac, forSerialization);
/*     */     }
/* 146 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   @Deprecated
/*     */   public Boolean findIgnoreUnknownProperties(AnnotatedClass ac)
/*     */   {
/* 153 */     Boolean result = this._primary.findIgnoreUnknownProperties(ac);
/* 154 */     if (result == null) {
/* 155 */       result = this._secondary.findIgnoreUnknownProperties(ac);
/*     */     }
/* 157 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public Boolean isIgnorableType(AnnotatedClass ac)
/*     */   {
/* 163 */     Boolean result = this._primary.isIgnorableType(ac);
/* 164 */     if (result == null) {
/* 165 */       result = this._secondary.isIgnorableType(ac);
/*     */     }
/* 167 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findFilterId(Annotated ann)
/*     */   {
/* 173 */     Object id = this._primary.findFilterId(ann);
/* 174 */     if (id == null) {
/* 175 */       id = this._secondary.findFilterId(ann);
/*     */     }
/* 177 */     return id;
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findNamingStrategy(AnnotatedClass ac)
/*     */   {
/* 183 */     Object str = this._primary.findNamingStrategy(ac);
/* 184 */     if (str == null) {
/* 185 */       str = this._secondary.findNamingStrategy(ac);
/*     */     }
/* 187 */     return str;
/*     */   }
/*     */   
/*     */   public String findClassDescription(AnnotatedClass ac)
/*     */   {
/* 192 */     String str = this._primary.findClassDescription(ac);
/* 193 */     if ((str == null) || (str.isEmpty())) {
/* 194 */       str = this._secondary.findClassDescription(ac);
/*     */     }
/* 196 */     return str;
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
/*     */   public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker)
/*     */   {
/* 212 */     checker = this._secondary.findAutoDetectVisibility(ac, checker);
/* 213 */     return this._primary.findAutoDetectVisibility(ac, checker);
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
/*     */   public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> config, AnnotatedClass ac, JavaType baseType)
/*     */   {
/* 226 */     TypeResolverBuilder<?> b = this._primary.findTypeResolver(config, ac, baseType);
/* 227 */     if (b == null) {
/* 228 */       b = this._secondary.findTypeResolver(config, ac, baseType);
/*     */     }
/* 230 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TypeResolverBuilder<?> findPropertyTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType baseType)
/*     */   {
/* 237 */     TypeResolverBuilder<?> b = this._primary.findPropertyTypeResolver(config, am, baseType);
/* 238 */     if (b == null) {
/* 239 */       b = this._secondary.findPropertyTypeResolver(config, am, baseType);
/*     */     }
/* 241 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TypeResolverBuilder<?> findPropertyContentTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType baseType)
/*     */   {
/* 248 */     TypeResolverBuilder<?> b = this._primary.findPropertyContentTypeResolver(config, am, baseType);
/* 249 */     if (b == null) {
/* 250 */       b = this._secondary.findPropertyContentTypeResolver(config, am, baseType);
/*     */     }
/* 252 */     return b;
/*     */   }
/*     */   
/*     */ 
/*     */   public List<NamedType> findSubtypes(Annotated a)
/*     */   {
/* 258 */     List<NamedType> types1 = this._primary.findSubtypes(a);
/* 259 */     List<NamedType> types2 = this._secondary.findSubtypes(a);
/* 260 */     if ((types1 == null) || (types1.isEmpty())) return types2;
/* 261 */     if ((types2 == null) || (types2.isEmpty())) return types1;
/* 262 */     ArrayList<NamedType> result = new ArrayList(types1.size() + types2.size());
/* 263 */     result.addAll(types1);
/* 264 */     result.addAll(types2);
/* 265 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */   public String findTypeName(AnnotatedClass ac)
/*     */   {
/* 271 */     String name = this._primary.findTypeName(ac);
/* 272 */     if ((name == null) || (name.length() == 0)) {
/* 273 */       name = this._secondary.findTypeName(ac);
/*     */     }
/* 275 */     return name;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public AnnotationIntrospector.ReferenceProperty findReferenceType(AnnotatedMember member)
/*     */   {
/* 282 */     AnnotationIntrospector.ReferenceProperty r = this._primary.findReferenceType(member);
/* 283 */     return r == null ? this._secondary.findReferenceType(member) : r;
/*     */   }
/*     */   
/*     */   public NameTransformer findUnwrappingNameTransformer(AnnotatedMember member)
/*     */   {
/* 288 */     NameTransformer r = this._primary.findUnwrappingNameTransformer(member);
/* 289 */     return r == null ? this._secondary.findUnwrappingNameTransformer(member) : r;
/*     */   }
/*     */   
/*     */   public Object findInjectableValueId(AnnotatedMember m)
/*     */   {
/* 294 */     Object r = this._primary.findInjectableValueId(m);
/* 295 */     return r == null ? this._secondary.findInjectableValueId(m) : r;
/*     */   }
/*     */   
/*     */   public boolean hasIgnoreMarker(AnnotatedMember m)
/*     */   {
/* 300 */     return (this._primary.hasIgnoreMarker(m)) || (this._secondary.hasIgnoreMarker(m));
/*     */   }
/*     */   
/*     */   public Boolean hasRequiredMarker(AnnotatedMember m)
/*     */   {
/* 305 */     Boolean r = this._primary.hasRequiredMarker(m);
/* 306 */     return r == null ? this._secondary.hasRequiredMarker(m) : r;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object findSerializer(Annotated am)
/*     */   {
/* 313 */     Object r = this._primary.findSerializer(am);
/* 314 */     return _isExplicitClassOrOb(r, JsonSerializer.None.class) ? r : this._secondary.findSerializer(am);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findKeySerializer(Annotated a)
/*     */   {
/* 320 */     Object r = this._primary.findKeySerializer(a);
/* 321 */     return _isExplicitClassOrOb(r, JsonSerializer.None.class) ? r : this._secondary.findKeySerializer(a);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findContentSerializer(Annotated a)
/*     */   {
/* 327 */     Object r = this._primary.findContentSerializer(a);
/* 328 */     return _isExplicitClassOrOb(r, JsonSerializer.None.class) ? r : this._secondary.findContentSerializer(a);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findNullSerializer(Annotated a)
/*     */   {
/* 334 */     Object r = this._primary.findNullSerializer(a);
/* 335 */     return _isExplicitClassOrOb(r, JsonSerializer.None.class) ? r : this._secondary.findNullSerializer(a);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public JsonInclude.Include findSerializationInclusion(Annotated a, JsonInclude.Include defValue)
/*     */   {
/* 345 */     defValue = this._secondary.findSerializationInclusion(a, defValue);
/* 346 */     defValue = this._primary.findSerializationInclusion(a, defValue);
/* 347 */     return defValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public JsonInclude.Include findSerializationInclusionForContent(Annotated a, JsonInclude.Include defValue)
/*     */   {
/* 355 */     defValue = this._secondary.findSerializationInclusionForContent(a, defValue);
/* 356 */     defValue = this._primary.findSerializationInclusionForContent(a, defValue);
/* 357 */     return defValue;
/*     */   }
/*     */   
/*     */ 
/*     */   public JsonInclude.Value findPropertyInclusion(Annotated a)
/*     */   {
/* 363 */     JsonInclude.Value v2 = this._secondary.findPropertyInclusion(a);
/* 364 */     JsonInclude.Value v1 = this._primary.findPropertyInclusion(a);
/*     */     
/* 366 */     if (v2 == null) {
/* 367 */       return v1;
/*     */     }
/* 369 */     return v2.withOverrides(v1);
/*     */   }
/*     */   
/*     */   public JsonSerialize.Typing findSerializationTyping(Annotated a)
/*     */   {
/* 374 */     JsonSerialize.Typing r = this._primary.findSerializationTyping(a);
/* 375 */     return r == null ? this._secondary.findSerializationTyping(a) : r;
/*     */   }
/*     */   
/*     */   public Object findSerializationConverter(Annotated a)
/*     */   {
/* 380 */     Object r = this._primary.findSerializationConverter(a);
/* 381 */     return r == null ? this._secondary.findSerializationConverter(a) : r;
/*     */   }
/*     */   
/*     */   public Object findSerializationContentConverter(AnnotatedMember a)
/*     */   {
/* 386 */     Object r = this._primary.findSerializationContentConverter(a);
/* 387 */     return r == null ? this._secondary.findSerializationContentConverter(a) : r;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<?>[] findViews(Annotated a)
/*     */   {
/* 396 */     Class<?>[] result = this._primary.findViews(a);
/* 397 */     if (result == null) {
/* 398 */       result = this._secondary.findViews(a);
/*     */     }
/* 400 */     return result;
/*     */   }
/*     */   
/*     */   public Boolean isTypeId(AnnotatedMember member)
/*     */   {
/* 405 */     Boolean b = this._primary.isTypeId(member);
/* 406 */     return b == null ? this._secondary.isTypeId(member) : b;
/*     */   }
/*     */   
/*     */   public ObjectIdInfo findObjectIdInfo(Annotated ann)
/*     */   {
/* 411 */     ObjectIdInfo r = this._primary.findObjectIdInfo(ann);
/* 412 */     return r == null ? this._secondary.findObjectIdInfo(ann) : r;
/*     */   }
/*     */   
/*     */ 
/*     */   public ObjectIdInfo findObjectReferenceInfo(Annotated ann, ObjectIdInfo objectIdInfo)
/*     */   {
/* 418 */     objectIdInfo = this._secondary.findObjectReferenceInfo(ann, objectIdInfo);
/* 419 */     objectIdInfo = this._primary.findObjectReferenceInfo(ann, objectIdInfo);
/* 420 */     return objectIdInfo;
/*     */   }
/*     */   
/*     */   public JsonFormat.Value findFormat(Annotated ann)
/*     */   {
/* 425 */     JsonFormat.Value v1 = this._primary.findFormat(ann);
/* 426 */     JsonFormat.Value v2 = this._secondary.findFormat(ann);
/* 427 */     if (v2 == null) {
/* 428 */       return v1;
/*     */     }
/* 430 */     return v2.withOverrides(v1);
/*     */   }
/*     */   
/*     */   public PropertyName findWrapperName(Annotated ann)
/*     */   {
/* 435 */     PropertyName name = this._primary.findWrapperName(ann);
/* 436 */     if (name == null) {
/* 437 */       name = this._secondary.findWrapperName(ann);
/* 438 */     } else if (name == PropertyName.USE_DEFAULT)
/*     */     {
/* 440 */       PropertyName name2 = this._secondary.findWrapperName(ann);
/* 441 */       if (name2 != null) {
/* 442 */         name = name2;
/*     */       }
/*     */     }
/* 445 */     return name;
/*     */   }
/*     */   
/*     */   public String findPropertyDefaultValue(Annotated ann)
/*     */   {
/* 450 */     String str = this._primary.findPropertyDefaultValue(ann);
/* 451 */     return (str == null) || (str.isEmpty()) ? this._secondary.findPropertyDefaultValue(ann) : str;
/*     */   }
/*     */   
/*     */   public String findPropertyDescription(Annotated ann)
/*     */   {
/* 456 */     String r = this._primary.findPropertyDescription(ann);
/* 457 */     return r == null ? this._secondary.findPropertyDescription(ann) : r;
/*     */   }
/*     */   
/*     */   public Integer findPropertyIndex(Annotated ann)
/*     */   {
/* 462 */     Integer r = this._primary.findPropertyIndex(ann);
/* 463 */     return r == null ? this._secondary.findPropertyIndex(ann) : r;
/*     */   }
/*     */   
/*     */   public String findImplicitPropertyName(AnnotatedMember param)
/*     */   {
/* 468 */     String r = this._primary.findImplicitPropertyName(param);
/* 469 */     return r == null ? this._secondary.findImplicitPropertyName(param) : r;
/*     */   }
/*     */   
/*     */   public JsonProperty.Access findPropertyAccess(Annotated ann)
/*     */   {
/* 474 */     JsonProperty.Access acc = this._primary.findPropertyAccess(ann);
/* 475 */     if ((acc != null) && (acc != JsonProperty.Access.AUTO)) {
/* 476 */       return acc;
/*     */     }
/* 478 */     acc = this._secondary.findPropertyAccess(ann);
/* 479 */     if (acc != null) {
/* 480 */       return acc;
/*     */     }
/* 482 */     return JsonProperty.Access.AUTO;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public AnnotatedMethod resolveSetterConflict(MapperConfig<?> config, AnnotatedMethod setter1, AnnotatedMethod setter2)
/*     */   {
/* 489 */     AnnotatedMethod res = this._primary.resolveSetterConflict(config, setter1, setter2);
/* 490 */     if (res == null) {
/* 491 */       res = this._secondary.resolveSetterConflict(config, setter1, setter2);
/*     */     }
/* 493 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType refineSerializationType(MapperConfig<?> config, Annotated a, JavaType baseType)
/*     */     throws JsonMappingException
/*     */   {
/* 502 */     JavaType t = this._secondary.refineSerializationType(config, a, baseType);
/* 503 */     return this._primary.refineSerializationType(config, a, t);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Class<?> findSerializationType(Annotated a)
/*     */   {
/* 509 */     Class<?> r = this._primary.findSerializationType(a);
/* 510 */     return r == null ? this._secondary.findSerializationType(a) : r;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Class<?> findSerializationKeyType(Annotated am, JavaType baseType)
/*     */   {
/* 516 */     Class<?> r = this._primary.findSerializationKeyType(am, baseType);
/* 517 */     return r == null ? this._secondary.findSerializationKeyType(am, baseType) : r;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Class<?> findSerializationContentType(Annotated am, JavaType baseType)
/*     */   {
/* 523 */     Class<?> r = this._primary.findSerializationContentType(am, baseType);
/* 524 */     return r == null ? this._secondary.findSerializationContentType(am, baseType) : r;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String[] findSerializationPropertyOrder(AnnotatedClass ac)
/*     */   {
/* 531 */     String[] r = this._primary.findSerializationPropertyOrder(ac);
/* 532 */     return r == null ? this._secondary.findSerializationPropertyOrder(ac) : r;
/*     */   }
/*     */   
/*     */   public Boolean findSerializationSortAlphabetically(Annotated ann)
/*     */   {
/* 537 */     Boolean r = this._primary.findSerializationSortAlphabetically(ann);
/* 538 */     return r == null ? this._secondary.findSerializationSortAlphabetically(ann) : r;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void findAndAddVirtualProperties(MapperConfig<?> config, AnnotatedClass ac, List<BeanPropertyWriter> properties)
/*     */   {
/* 545 */     this._primary.findAndAddVirtualProperties(config, ac, properties);
/* 546 */     this._secondary.findAndAddVirtualProperties(config, ac, properties);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PropertyName findNameForSerialization(Annotated a)
/*     */   {
/* 553 */     PropertyName n = this._primary.findNameForSerialization(a);
/*     */     
/* 555 */     if (n == null) {
/* 556 */       n = this._secondary.findNameForSerialization(a);
/* 557 */     } else if (n == PropertyName.USE_DEFAULT) {
/* 558 */       PropertyName n2 = this._secondary.findNameForSerialization(a);
/* 559 */       if (n2 != null) {
/* 560 */         n = n2;
/*     */       }
/*     */     }
/* 563 */     return n;
/*     */   }
/*     */   
/*     */   public boolean hasAsValueAnnotation(AnnotatedMethod am)
/*     */   {
/* 568 */     return (this._primary.hasAsValueAnnotation(am)) || (this._secondary.hasAsValueAnnotation(am));
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public String findEnumValue(Enum<?> value)
/*     */   {
/* 574 */     String r = this._primary.findEnumValue(value);
/* 575 */     return r == null ? this._secondary.findEnumValue(value) : r;
/*     */   }
/*     */   
/*     */ 
/*     */   public String[] findEnumValues(Class<?> enumType, Enum<?>[] enumValues, String[] names)
/*     */   {
/* 581 */     names = this._secondary.findEnumValues(enumType, enumValues, names);
/* 582 */     names = this._primary.findEnumValues(enumType, enumValues, names);
/* 583 */     return names;
/*     */   }
/*     */   
/*     */   public Enum<?> findDefaultEnumValue(Class<Enum<?>> enumCls)
/*     */   {
/* 588 */     Enum<?> en = this._primary.findDefaultEnumValue(enumCls);
/* 589 */     return en == null ? this._secondary.findDefaultEnumValue(enumCls) : en;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object findDeserializer(Annotated am)
/*     */   {
/* 596 */     Object r = this._primary.findDeserializer(am);
/* 597 */     return _isExplicitClassOrOb(r, JsonDeserializer.None.class) ? r : this._secondary.findDeserializer(am);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findKeyDeserializer(Annotated am)
/*     */   {
/* 603 */     Object r = this._primary.findKeyDeserializer(am);
/* 604 */     return _isExplicitClassOrOb(r, KeyDeserializer.None.class) ? r : this._secondary.findKeyDeserializer(am);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findContentDeserializer(Annotated am)
/*     */   {
/* 610 */     Object r = this._primary.findContentDeserializer(am);
/* 611 */     return _isExplicitClassOrOb(r, JsonDeserializer.None.class) ? r : this._secondary.findContentDeserializer(am);
/*     */   }
/*     */   
/*     */ 
/*     */   public Object findDeserializationConverter(Annotated a)
/*     */   {
/* 617 */     Object ob = this._primary.findDeserializationConverter(a);
/* 618 */     return ob == null ? this._secondary.findDeserializationConverter(a) : ob;
/*     */   }
/*     */   
/*     */   public Object findDeserializationContentConverter(AnnotatedMember a)
/*     */   {
/* 623 */     Object ob = this._primary.findDeserializationContentConverter(a);
/* 624 */     return ob == null ? this._secondary.findDeserializationContentConverter(a) : ob;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType refineDeserializationType(MapperConfig<?> config, Annotated a, JavaType baseType)
/*     */     throws JsonMappingException
/*     */   {
/* 634 */     JavaType t = this._secondary.refineDeserializationType(config, a, baseType);
/* 635 */     return this._primary.refineDeserializationType(config, a, t);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Class<?> findDeserializationType(Annotated am, JavaType baseType)
/*     */   {
/* 641 */     Class<?> r = this._primary.findDeserializationType(am, baseType);
/* 642 */     return r != null ? r : this._secondary.findDeserializationType(am, baseType);
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Class<?> findDeserializationKeyType(Annotated am, JavaType baseKeyType)
/*     */   {
/* 648 */     Class<?> result = this._primary.findDeserializationKeyType(am, baseKeyType);
/* 649 */     return result == null ? this._secondary.findDeserializationKeyType(am, baseKeyType) : result;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public Class<?> findDeserializationContentType(Annotated am, JavaType baseContentType)
/*     */   {
/* 655 */     Class<?> result = this._primary.findDeserializationContentType(am, baseContentType);
/* 656 */     return result == null ? this._secondary.findDeserializationContentType(am, baseContentType) : result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Object findValueInstantiator(AnnotatedClass ac)
/*     */   {
/* 663 */     Object result = this._primary.findValueInstantiator(ac);
/* 664 */     return result == null ? this._secondary.findValueInstantiator(ac) : result;
/*     */   }
/*     */   
/*     */   public Class<?> findPOJOBuilder(AnnotatedClass ac)
/*     */   {
/* 669 */     Class<?> result = this._primary.findPOJOBuilder(ac);
/* 670 */     return result == null ? this._secondary.findPOJOBuilder(ac) : result;
/*     */   }
/*     */   
/*     */   public JsonPOJOBuilder.Value findPOJOBuilderConfig(AnnotatedClass ac)
/*     */   {
/* 675 */     JsonPOJOBuilder.Value result = this._primary.findPOJOBuilderConfig(ac);
/* 676 */     return result == null ? this._secondary.findPOJOBuilderConfig(ac) : result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyName findNameForDeserialization(Annotated a)
/*     */   {
/* 685 */     PropertyName n = this._primary.findNameForDeserialization(a);
/* 686 */     if (n == null) {
/* 687 */       n = this._secondary.findNameForDeserialization(a);
/* 688 */     } else if (n == PropertyName.USE_DEFAULT) {
/* 689 */       PropertyName n2 = this._secondary.findNameForDeserialization(a);
/* 690 */       if (n2 != null) {
/* 691 */         n = n2;
/*     */       }
/*     */     }
/* 694 */     return n;
/*     */   }
/*     */   
/*     */   public boolean hasAnySetterAnnotation(AnnotatedMethod am)
/*     */   {
/* 699 */     return (this._primary.hasAnySetterAnnotation(am)) || (this._secondary.hasAnySetterAnnotation(am));
/*     */   }
/*     */   
/*     */   public boolean hasAnyGetterAnnotation(AnnotatedMethod am)
/*     */   {
/* 704 */     return (this._primary.hasAnyGetterAnnotation(am)) || (this._secondary.hasAnyGetterAnnotation(am));
/*     */   }
/*     */   
/*     */   public boolean hasCreatorAnnotation(Annotated a)
/*     */   {
/* 709 */     return (this._primary.hasCreatorAnnotation(a)) || (this._secondary.hasCreatorAnnotation(a));
/*     */   }
/*     */   
/*     */   public JsonCreator.Mode findCreatorBinding(Annotated a)
/*     */   {
/* 714 */     JsonCreator.Mode mode = this._primary.findCreatorBinding(a);
/* 715 */     if (mode != null) {
/* 716 */       return mode;
/*     */     }
/* 718 */     return this._secondary.findCreatorBinding(a);
/*     */   }
/*     */   
/*     */   protected boolean _isExplicitClassOrOb(Object maybeCls, Class<?> implicit) {
/* 722 */     if (maybeCls == null) {
/* 723 */       return false;
/*     */     }
/* 725 */     if (!(maybeCls instanceof Class)) {
/* 726 */       return true;
/*     */     }
/* 728 */     Class<?> cls = (Class)maybeCls;
/* 729 */     return (cls != implicit) && (!ClassUtil.isBogusClass(cls));
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\AnnotationIntrospectorPair.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */