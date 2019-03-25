/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ 
/*     */ public class SimpleBeanPropertyDefinition
/*     */   extends BeanPropertyDefinition
/*     */ {
/*     */   protected final AnnotationIntrospector _introspector;
/*     */   protected final AnnotatedMember _member;
/*     */   protected final PropertyMetadata _metadata;
/*     */   protected final PropertyName _fullName;
/*     */   protected final JsonInclude.Value _inclusion;
/*     */   @Deprecated
/*     */   protected final String _name;
/*     */   
/*     */   protected SimpleBeanPropertyDefinition(AnnotatedMember member, PropertyName fullName, AnnotationIntrospector intr, PropertyMetadata metadata, JsonInclude.Include inclusion)
/*     */   {
/*  64 */     this(member, fullName, intr, metadata, (inclusion == null) || (inclusion == JsonInclude.Include.USE_DEFAULTS) ? EMPTY_INCLUDE : JsonInclude.Value.construct(inclusion, null));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SimpleBeanPropertyDefinition(AnnotatedMember member, PropertyName fullName, AnnotationIntrospector intr, PropertyMetadata metadata, JsonInclude.Value inclusion)
/*     */   {
/*  73 */     this._introspector = intr;
/*  74 */     this._member = member;
/*  75 */     this._fullName = fullName;
/*  76 */     this._name = fullName.getSimpleName();
/*  77 */     this._metadata = (metadata == null ? PropertyMetadata.STD_OPTIONAL : metadata);
/*  78 */     this._inclusion = inclusion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected SimpleBeanPropertyDefinition(AnnotatedMember member, String name, AnnotationIntrospector intr)
/*     */   {
/*  87 */     this(member, new PropertyName(name), intr, null, EMPTY_INCLUDE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SimpleBeanPropertyDefinition construct(MapperConfig<?> config, AnnotatedMember member)
/*     */   {
/*  95 */     return new SimpleBeanPropertyDefinition(member, PropertyName.construct(member.getName()), config == null ? null : config.getAnnotationIntrospector(), null, EMPTY_INCLUDE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static SimpleBeanPropertyDefinition construct(MapperConfig<?> config, AnnotatedMember member, String name)
/*     */   {
/* 106 */     return new SimpleBeanPropertyDefinition(member, PropertyName.construct(name), config == null ? null : config.getAnnotationIntrospector(), null, EMPTY_INCLUDE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SimpleBeanPropertyDefinition construct(MapperConfig<?> config, AnnotatedMember member, PropertyName name)
/*     */   {
/* 116 */     return construct(config, member, name, null, EMPTY_INCLUDE);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SimpleBeanPropertyDefinition construct(MapperConfig<?> config, AnnotatedMember member, PropertyName name, PropertyMetadata metadata, JsonInclude.Include inclusion)
/*     */   {
/* 125 */     return new SimpleBeanPropertyDefinition(member, name, config == null ? null : config.getAnnotationIntrospector(), metadata, inclusion);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static SimpleBeanPropertyDefinition construct(MapperConfig<?> config, AnnotatedMember member, PropertyName name, PropertyMetadata metadata, JsonInclude.Value inclusion)
/*     */   {
/* 136 */     return new SimpleBeanPropertyDefinition(member, name, config == null ? null : config.getAnnotationIntrospector(), metadata, inclusion);
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
/*     */   @Deprecated
/*     */   public BeanPropertyDefinition withName(String newName)
/*     */   {
/* 150 */     return withSimpleName(newName);
/*     */   }
/*     */   
/*     */   public BeanPropertyDefinition withSimpleName(String newName)
/*     */   {
/* 155 */     if ((this._fullName.hasSimpleName(newName)) && (!this._fullName.hasNamespace())) {
/* 156 */       return this;
/*     */     }
/* 158 */     return new SimpleBeanPropertyDefinition(this._member, new PropertyName(newName), this._introspector, this._metadata, this._inclusion);
/*     */   }
/*     */   
/*     */ 
/*     */   public BeanPropertyDefinition withName(PropertyName newName)
/*     */   {
/* 164 */     if (this._fullName.equals(newName)) {
/* 165 */       return this;
/*     */     }
/* 167 */     return new SimpleBeanPropertyDefinition(this._member, newName, this._introspector, this._metadata, this._inclusion);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanPropertyDefinition withMetadata(PropertyMetadata metadata)
/*     */   {
/* 175 */     if (metadata.equals(this._metadata)) {
/* 176 */       return this;
/*     */     }
/* 178 */     return new SimpleBeanPropertyDefinition(this._member, this._fullName, this._introspector, metadata, this._inclusion);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanPropertyDefinition withInclusion(JsonInclude.Value inclusion)
/*     */   {
/* 186 */     if (this._inclusion == inclusion) {
/* 187 */       return this;
/*     */     }
/* 189 */     return new SimpleBeanPropertyDefinition(this._member, this._fullName, this._introspector, this._metadata, inclusion);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 200 */     return this._fullName.getSimpleName();
/*     */   }
/*     */   
/* 203 */   public PropertyName getFullName() { return this._fullName; }
/*     */   
/*     */   public boolean hasName(PropertyName name)
/*     */   {
/* 207 */     return this._fullName.equals(name);
/*     */   }
/*     */   
/*     */   public String getInternalName() {
/* 211 */     return getName();
/*     */   }
/*     */   
/*     */   public PropertyName getWrapperName() {
/* 215 */     return (this._introspector == null) && (this._member != null) ? null : this._introspector.findWrapperName(this._member);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 221 */   public boolean isExplicitlyIncluded() { return false; }
/* 222 */   public boolean isExplicitlyNamed() { return false; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyMetadata getMetadata()
/*     */   {
/* 230 */     return this._metadata;
/*     */   }
/*     */   
/*     */   public JsonInclude.Value findInclusion()
/*     */   {
/* 235 */     return this._inclusion;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasGetter()
/*     */   {
/* 245 */     return getGetter() != null;
/*     */   }
/*     */   
/* 248 */   public boolean hasSetter() { return getSetter() != null; }
/*     */   
/*     */   public boolean hasField() {
/* 251 */     return this._member instanceof AnnotatedField;
/*     */   }
/*     */   
/* 254 */   public boolean hasConstructorParameter() { return this._member instanceof AnnotatedParameter; }
/*     */   
/*     */   public AnnotatedMethod getGetter()
/*     */   {
/* 258 */     if (((this._member instanceof AnnotatedMethod)) && (((AnnotatedMethod)this._member).getParameterCount() == 0))
/*     */     {
/* 260 */       return (AnnotatedMethod)this._member;
/*     */     }
/* 262 */     return null;
/*     */   }
/*     */   
/*     */   public AnnotatedMethod getSetter()
/*     */   {
/* 267 */     if (((this._member instanceof AnnotatedMethod)) && (((AnnotatedMethod)this._member).getParameterCount() == 1))
/*     */     {
/* 269 */       return (AnnotatedMethod)this._member;
/*     */     }
/* 271 */     return null;
/*     */   }
/*     */   
/*     */   public AnnotatedField getField()
/*     */   {
/* 276 */     return (this._member instanceof AnnotatedField) ? (AnnotatedField)this._member : null;
/*     */   }
/*     */   
/*     */   public AnnotatedParameter getConstructorParameter()
/*     */   {
/* 281 */     return (this._member instanceof AnnotatedParameter) ? (AnnotatedParameter)this._member : null;
/*     */   }
/*     */   
/*     */   public Iterator<AnnotatedParameter> getConstructorParameters()
/*     */   {
/* 286 */     AnnotatedParameter param = getConstructorParameter();
/* 287 */     if (param == null) {
/* 288 */       return ClassUtil.emptyIterator();
/*     */     }
/* 290 */     return Collections.singleton(param).iterator();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedMember getAccessor()
/*     */   {
/* 300 */     AnnotatedMember acc = getGetter();
/* 301 */     if (acc == null) {
/* 302 */       acc = getField();
/*     */     }
/* 304 */     return acc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public AnnotatedMember getMutator()
/*     */   {
/* 314 */     AnnotatedMember acc = getConstructorParameter();
/* 315 */     if (acc == null) {
/* 316 */       acc = getSetter();
/* 317 */       if (acc == null) {
/* 318 */         acc = getField();
/*     */       }
/*     */     }
/* 321 */     return acc;
/*     */   }
/*     */   
/*     */   public AnnotatedMember getNonConstructorMutator()
/*     */   {
/* 326 */     AnnotatedMember acc = getSetter();
/* 327 */     if (acc == null) {
/* 328 */       acc = getField();
/*     */     }
/* 330 */     return acc;
/*     */   }
/*     */   
/*     */   public AnnotatedMember getPrimaryMember() {
/* 334 */     return this._member;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databin\\util\SimpleBeanPropertyDefinition.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */