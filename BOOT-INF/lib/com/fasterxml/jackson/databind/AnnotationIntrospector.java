/*      */ package com.fasterxml.jackson.databind;
/*      */ 
/*      */ import com.fasterxml.jackson.annotation.JsonCreator.Mode;
/*      */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*      */ import com.fasterxml.jackson.annotation.JsonIgnoreProperties.Value;
/*      */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*      */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*      */ import com.fasterxml.jackson.annotation.JsonProperty.Access;
/*      */ import com.fasterxml.jackson.core.Version;
/*      */ import com.fasterxml.jackson.core.Versioned;
/*      */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value;
/*      */ import com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing;
/*      */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*      */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*      */ import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
/*      */ import com.fasterxml.jackson.databind.introspect.NopAnnotationIntrospector;
/*      */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*      */ import com.fasterxml.jackson.databind.introspect.VisibilityChecker;
/*      */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*      */ import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
/*      */ import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
/*      */ import com.fasterxml.jackson.databind.type.MapLikeType;
/*      */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*      */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*      */ import java.io.Serializable;
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.List;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public abstract class AnnotationIntrospector
/*      */   implements Versioned, Serializable
/*      */ {
/*      */   public static class ReferenceProperty
/*      */   {
/*      */     private final Type _type;
/*      */     private final String _name;
/*      */     
/*      */     public static enum Type
/*      */     {
/*   69 */       MANAGED_REFERENCE, 
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*   77 */       BACK_REFERENCE;
/*      */       
/*      */ 
/*      */       private Type() {}
/*      */     }
/*      */     
/*      */     public ReferenceProperty(Type t, String n)
/*      */     {
/*   85 */       this._type = t;
/*   86 */       this._name = n;
/*      */     }
/*      */     
/*   89 */     public static ReferenceProperty managed(String name) { return new ReferenceProperty(Type.MANAGED_REFERENCE, name); }
/*   90 */     public static ReferenceProperty back(String name) { return new ReferenceProperty(Type.BACK_REFERENCE, name); }
/*      */     
/*   92 */     public Type getType() { return this._type; }
/*   93 */     public String getName() { return this._name; }
/*      */     
/*   95 */     public boolean isManagedReference() { return this._type == Type.MANAGED_REFERENCE; }
/*   96 */     public boolean isBackReference() { return this._type == Type.BACK_REFERENCE; }
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
/*      */   public static AnnotationIntrospector nopInstance()
/*      */   {
/*  111 */     return NopAnnotationIntrospector.instance;
/*      */   }
/*      */   
/*      */   public static AnnotationIntrospector pair(AnnotationIntrospector a1, AnnotationIntrospector a2) {
/*  115 */     return new AnnotationIntrospectorPair(a1, a2);
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
/*      */   public Collection<AnnotationIntrospector> allIntrospectors()
/*      */   {
/*  136 */     return Collections.singletonList(this);
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
/*      */   public Collection<AnnotationIntrospector> allIntrospectors(Collection<AnnotationIntrospector> result)
/*      */   {
/*  150 */     result.add(this);
/*  151 */     return result;
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
/*      */   public abstract Version version();
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean isAnnotationBundle(Annotation ann)
/*      */   {
/*  177 */     return false;
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
/*      */   public ObjectIdInfo findObjectIdInfo(Annotated ann)
/*      */   {
/*  197 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ObjectIdInfo findObjectReferenceInfo(Annotated ann, ObjectIdInfo objectIdInfo)
/*      */   {
/*  206 */     return objectIdInfo;
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
/*      */   public PropertyName findRootName(AnnotatedClass ac)
/*      */   {
/*  226 */     return null;
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
/*      */   public JsonIgnoreProperties.Value findPropertyIgnorals(Annotated ac)
/*      */   {
/*  243 */     String[] ignorals = findPropertiesToIgnore(ac, true);
/*  244 */     Boolean b = (ac instanceof AnnotatedClass) ? findIgnoreUnknownProperties((AnnotatedClass)ac) : null;
/*      */     JsonIgnoreProperties.Value v;
/*      */     JsonIgnoreProperties.Value v;
/*  247 */     if (ignorals == null) {
/*  248 */       if (b == null) {
/*  249 */         return null;
/*      */       }
/*  251 */       v = JsonIgnoreProperties.Value.empty();
/*      */     } else {
/*  253 */       v = JsonIgnoreProperties.Value.forIgnoredProperties(ignorals);
/*      */     }
/*  255 */     if (b != null) {
/*  256 */       v = b.booleanValue() ? v.withIgnoreUnknown() : v.withoutIgnoreUnknown();
/*      */     }
/*  258 */     return v;
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
/*      */   public String[] findPropertiesToIgnore(Annotated ac, boolean forSerialization)
/*      */   {
/*  271 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public String[] findPropertiesToIgnore(Annotated ac)
/*      */   {
/*  279 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   @Deprecated
/*      */   public Boolean findIgnoreUnknownProperties(AnnotatedClass ac)
/*      */   {
/*  288 */     return null;
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
/*      */   public Boolean isIgnorableType(AnnotatedClass ac)
/*      */   {
/*  301 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object findFilterId(Annotated ann)
/*      */   {
/*  310 */     return null;
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
/*      */   public Object findNamingStrategy(AnnotatedClass ac)
/*      */   {
/*  323 */     return null;
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
/*      */   public String findClassDescription(AnnotatedClass ac)
/*      */   {
/*  336 */     return null;
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
/*      */   public VisibilityChecker<?> findAutoDetectVisibility(AnnotatedClass ac, VisibilityChecker<?> checker)
/*      */   {
/*  352 */     return checker;
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
/*      */   public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> config, AnnotatedClass ac, JavaType baseType)
/*      */   {
/*  377 */     return null;
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
/*      */   public TypeResolverBuilder<?> findPropertyTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType baseType)
/*      */   {
/*  397 */     return null;
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
/*      */   public TypeResolverBuilder<?> findPropertyContentTypeResolver(MapperConfig<?> config, AnnotatedMember am, JavaType containerType)
/*      */   {
/*  419 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public List<NamedType> findSubtypes(Annotated a)
/*      */   {
/*  431 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public String findTypeName(AnnotatedClass ac)
/*      */   {
/*  438 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public Boolean isTypeId(AnnotatedMember member)
/*      */   {
/*  445 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public ReferenceProperty findReferenceType(AnnotatedMember member)
/*      */   {
/*  457 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public NameTransformer findUnwrappingNameTransformer(AnnotatedMember member)
/*      */   {
/*  467 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public boolean hasIgnoreMarker(AnnotatedMember m)
/*      */   {
/*  476 */     return false;
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
/*      */   public Object findInjectableValueId(AnnotatedMember m)
/*      */   {
/*  491 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Boolean hasRequiredMarker(AnnotatedMember m)
/*      */   {
/*  500 */     return null;
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
/*      */   public Class<?>[] findViews(Annotated a)
/*      */   {
/*  515 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonFormat.Value findFormat(Annotated memberOrClass)
/*      */   {
/*  525 */     return null;
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
/*      */   public PropertyName findWrapperName(Annotated ann)
/*      */   {
/*  538 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String findPropertyDefaultValue(Annotated ann)
/*      */   {
/*  548 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public String findPropertyDescription(Annotated ann)
/*      */   {
/*  560 */     return null;
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
/*      */   public Integer findPropertyIndex(Annotated ann)
/*      */   {
/*  573 */     return null;
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
/*      */   public String findImplicitPropertyName(AnnotatedMember member)
/*      */   {
/*  588 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonProperty.Access findPropertyAccess(Annotated ann)
/*      */   {
/*  599 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public AnnotatedMethod resolveSetterConflict(MapperConfig<?> config, AnnotatedMethod setter1, AnnotatedMethod setter2)
/*      */   {
/*  611 */     return null;
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
/*      */   public Object findSerializer(Annotated am)
/*      */   {
/*  628 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object findKeySerializer(Annotated am)
/*      */   {
/*  639 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object findContentSerializer(Annotated am)
/*      */   {
/*  651 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object findNullSerializer(Annotated am)
/*      */   {
/*  661 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonSerialize.Typing findSerializationTyping(Annotated a)
/*      */   {
/*  673 */     return null;
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
/*      */   public Object findSerializationConverter(Annotated a)
/*      */   {
/*  698 */     return null;
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
/*      */   public Object findSerializationContentConverter(AnnotatedMember a)
/*      */   {
/*  720 */     return null;
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
/*      */   @Deprecated
/*      */   public JsonInclude.Include findSerializationInclusion(Annotated a, JsonInclude.Include defValue)
/*      */   {
/*  742 */     return defValue;
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
/*      */   public JsonInclude.Include findSerializationInclusionForContent(Annotated a, JsonInclude.Include defValue)
/*      */   {
/*  756 */     return defValue;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonInclude.Value findPropertyInclusion(Annotated a)
/*      */   {
/*  768 */     return JsonInclude.Value.empty();
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
/*      */   @Deprecated
/*      */   public Class<?> findSerializationType(Annotated a)
/*      */   {
/*  790 */     return null;
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
/*      */   @Deprecated
/*      */   public Class<?> findSerializationKeyType(Annotated am, JavaType baseType)
/*      */   {
/*  805 */     return null;
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
/*      */   @Deprecated
/*      */   public Class<?> findSerializationContentType(Annotated am, JavaType baseType)
/*      */   {
/*  820 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JavaType refineSerializationType(MapperConfig<?> config, Annotated a, JavaType baseType)
/*      */     throws JsonMappingException
/*      */   {
/*  832 */     JavaType type = baseType;
/*  833 */     TypeFactory tf = config.getTypeFactory();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  843 */     Class<?> serClass = findSerializationType(a);
/*  844 */     if (serClass != null) {
/*  845 */       if (type.hasRawClass(serClass))
/*      */       {
/*      */ 
/*  848 */         type = type.withStaticTyping();
/*      */       } else {
/*  850 */         Class<?> currRaw = type.getRawClass();
/*      */         
/*      */         try
/*      */         {
/*  854 */           if (serClass.isAssignableFrom(currRaw)) {
/*  855 */             type = tf.constructGeneralizedType(type, serClass);
/*  856 */           } else if (currRaw.isAssignableFrom(serClass)) {
/*  857 */             type = tf.constructSpecializedType(type, serClass);
/*      */           } else {
/*  859 */             throw new JsonMappingException(null, String.format("Can not refine serialization type %s into %s; types not related", new Object[] { type, serClass.getName() }));
/*      */           }
/*      */         }
/*      */         catch (IllegalArgumentException iae)
/*      */         {
/*  864 */           throw new JsonMappingException(null, String.format("Failed to widen type %s with annotation (value %s), from '%s': %s", new Object[] { type, serClass.getName(), a.getName(), iae.getMessage() }), iae);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  874 */     if (type.isMapLikeType()) {
/*  875 */       JavaType keyType = type.getKeyType();
/*  876 */       Class<?> keyClass = findSerializationKeyType(a, keyType);
/*  877 */       if (keyClass != null) {
/*  878 */         if (keyType.hasRawClass(keyClass)) {
/*  879 */           keyType = keyType.withStaticTyping();
/*      */         } else {
/*  881 */           Class<?> currRaw = keyType.getRawClass();
/*      */           
/*      */ 
/*      */           try
/*      */           {
/*  886 */             if (keyClass.isAssignableFrom(currRaw)) {
/*  887 */               keyType = tf.constructGeneralizedType(keyType, keyClass);
/*  888 */             } else if (currRaw.isAssignableFrom(keyClass)) {
/*  889 */               keyType = tf.constructSpecializedType(keyType, keyClass);
/*      */             } else {
/*  891 */               throw new JsonMappingException(null, String.format("Can not refine serialization key type %s into %s; types not related", new Object[] { keyType, keyClass.getName() }));
/*      */             }
/*      */           }
/*      */           catch (IllegalArgumentException iae)
/*      */           {
/*  896 */             throw new JsonMappingException(null, String.format("Failed to widen key type of %s with concrete-type annotation (value %s), from '%s': %s", new Object[] { type, keyClass.getName(), a.getName(), iae.getMessage() }), iae);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  902 */         type = ((MapLikeType)type).withKeyType(keyType);
/*      */       }
/*      */     }
/*      */     
/*  906 */     JavaType contentType = type.getContentType();
/*  907 */     if (contentType != null)
/*      */     {
/*  909 */       Class<?> contentClass = findSerializationContentType(a, contentType);
/*  910 */       if (contentClass != null) {
/*  911 */         if (contentType.hasRawClass(contentClass)) {
/*  912 */           contentType = contentType.withStaticTyping();
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  917 */           Class<?> currRaw = contentType.getRawClass();
/*      */           try {
/*  919 */             if (contentClass.isAssignableFrom(currRaw)) {
/*  920 */               contentType = tf.constructGeneralizedType(contentType, contentClass);
/*  921 */             } else if (currRaw.isAssignableFrom(contentClass)) {
/*  922 */               contentType = tf.constructSpecializedType(contentType, contentClass);
/*      */             } else {
/*  924 */               throw new JsonMappingException(null, String.format("Can not refine serialization content type %s into %s; types not related", new Object[] { contentType, contentClass.getName() }));
/*      */             }
/*      */           }
/*      */           catch (IllegalArgumentException iae)
/*      */           {
/*  929 */             throw new JsonMappingException(null, String.format("Internal error: failed to refine value type of %s with concrete-type annotation (value %s), from '%s': %s", new Object[] { type, contentClass.getName(), a.getName(), iae.getMessage() }), iae);
/*      */           }
/*      */         }
/*      */         
/*      */ 
/*      */ 
/*  935 */         type = type.withContentType(contentType);
/*      */       }
/*      */     }
/*  938 */     return type;
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
/*      */   public String[] findSerializationPropertyOrder(AnnotatedClass ac)
/*      */   {
/*  952 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Boolean findSerializationSortAlphabetically(Annotated ann)
/*      */   {
/*  961 */     return null;
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
/*      */   public void findAndAddVirtualProperties(MapperConfig<?> config, AnnotatedClass ac, List<BeanPropertyWriter> properties) {}
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
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
/* 1002 */     return null;
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
/*      */   public boolean hasAsValueAnnotation(AnnotatedMethod am)
/*      */   {
/* 1015 */     return false;
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
/*      */   public String findEnumValue(Enum<?> value)
/*      */   {
/* 1032 */     return value.name();
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
/*      */   public String[] findEnumValues(Class<?> enumType, Enum<?>[] enumValues, String[] names)
/*      */   {
/* 1047 */     int i = 0; for (int len = enumValues.length; i < len; i++)
/*      */     {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1053 */       if (names[i] == null) {
/* 1054 */         names[i] = findEnumValue(enumValues[i]);
/*      */       }
/*      */     }
/* 1057 */     return names;
/*      */   }
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
/* 1069 */     return null;
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
/*      */   public Object findDeserializer(Annotated am)
/*      */   {
/* 1087 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public Object findKeyDeserializer(Annotated am)
/*      */   {
/* 1099 */     return null;
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
/*      */   public Object findContentDeserializer(Annotated am)
/*      */   {
/* 1112 */     return null;
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
/*      */   public Object findDeserializationConverter(Annotated a)
/*      */   {
/* 1138 */     return null;
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
/*      */   public Object findDeserializationContentConverter(AnnotatedMember a)
/*      */   {
/* 1160 */     return null;
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
/*      */   public JavaType refineDeserializationType(MapperConfig<?> config, Annotated a, JavaType baseType)
/*      */     throws JsonMappingException
/*      */   {
/* 1178 */     JavaType type = baseType;
/* 1179 */     TypeFactory tf = config.getTypeFactory();
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1189 */     Class<?> valueClass = findDeserializationType(a, type);
/* 1190 */     if ((valueClass != null) && (!type.hasRawClass(valueClass))) {
/*      */       try {
/* 1192 */         type = tf.constructSpecializedType(type, valueClass);
/*      */       } catch (IllegalArgumentException iae) {
/* 1194 */         throw new JsonMappingException(null, String.format("Failed to narrow type %s with annotation (value %s), from '%s': %s", new Object[] { type, valueClass.getName(), a.getName(), iae.getMessage() }), iae);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/* 1203 */     if (type.isMapLikeType()) {
/* 1204 */       JavaType keyType = type.getKeyType();
/* 1205 */       Class<?> keyClass = findDeserializationKeyType(a, keyType);
/* 1206 */       if (keyClass != null) {
/*      */         try {
/* 1208 */           keyType = tf.constructSpecializedType(keyType, keyClass);
/* 1209 */           type = ((MapLikeType)type).withKeyType(keyType);
/*      */         } catch (IllegalArgumentException iae) {
/* 1211 */           throw new JsonMappingException(null, String.format("Failed to narrow key type of %s with concrete-type annotation (value %s), from '%s': %s", new Object[] { type, keyClass.getName(), a.getName(), iae.getMessage() }), iae);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1218 */     JavaType contentType = type.getContentType();
/* 1219 */     if (contentType != null)
/*      */     {
/* 1221 */       Class<?> contentClass = findDeserializationContentType(a, contentType);
/* 1222 */       if (contentClass != null) {
/*      */         try {
/* 1224 */           contentType = tf.constructSpecializedType(contentType, contentClass);
/* 1225 */           type = type.withContentType(contentType);
/*      */         } catch (IllegalArgumentException iae) {
/* 1227 */           throw new JsonMappingException(null, String.format("Failed to narrow value type of %s with concrete-type annotation (value %s), from '%s': %s", new Object[] { type, contentClass.getName(), a.getName(), iae.getMessage() }), iae);
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1234 */     return type;
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
/*      */   public Class<?> findDeserializationType(Annotated am, JavaType baseType)
/*      */   {
/* 1252 */     return null;
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
/*      */   public Class<?> findDeserializationKeyType(Annotated am, JavaType baseKeyType)
/*      */   {
/* 1269 */     return null;
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
/*      */   public Class<?> findDeserializationContentType(Annotated am, JavaType baseContentType)
/*      */   {
/* 1287 */     return null;
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
/*      */   public Object findValueInstantiator(AnnotatedClass ac)
/*      */   {
/* 1302 */     return null;
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
/*      */   public Class<?> findPOJOBuilder(AnnotatedClass ac)
/*      */   {
/* 1319 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public JsonPOJOBuilder.Value findPOJOBuilderConfig(AnnotatedClass ac)
/*      */   {
/* 1326 */     return null;
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
/*      */   public PropertyName findNameForDeserialization(Annotated a)
/*      */   {
/* 1358 */     return null;
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
/*      */   public boolean hasAnySetterAnnotation(AnnotatedMethod am)
/*      */   {
/* 1371 */     return false;
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
/*      */   public boolean hasAnyGetterAnnotation(AnnotatedMethod am)
/*      */   {
/* 1384 */     return false;
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
/*      */   public boolean hasCreatorAnnotation(Annotated a)
/*      */   {
/* 1398 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public JsonCreator.Mode findCreatorBinding(Annotated a)
/*      */   {
/* 1410 */     return null;
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
/*      */   protected <A extends Annotation> A _findAnnotation(Annotated annotated, Class<A> annoClass)
/*      */   {
/* 1436 */     return annotated.getAnnotation(annoClass);
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
/*      */   protected boolean _hasAnnotation(Annotated annotated, Class<? extends Annotation> annoClass)
/*      */   {
/* 1453 */     return annotated.hasAnnotation(annoClass);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected boolean _hasOneOf(Annotated annotated, Class<? extends Annotation>[] annoClasses)
/*      */   {
/* 1463 */     return annotated.hasOneOf(annoClasses);
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\AnnotationIntrospector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */