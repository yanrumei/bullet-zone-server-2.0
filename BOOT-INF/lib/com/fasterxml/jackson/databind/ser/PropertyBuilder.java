/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ 
/*     */ public class PropertyBuilder
/*     */ {
/*  18 */   private static final Object NO_DEFAULT_MARKER = Boolean.FALSE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final SerializationConfig _config;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final BeanDescription _beanDesc;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final AnnotationIntrospector _annotationIntrospector;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object _defaultBean;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JsonInclude.Value _defaultInclusion;
/*     */   
/*     */ 
/*     */ 
/*     */   protected final boolean _useRealPropertyDefaults;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PropertyBuilder(SerializationConfig config, BeanDescription beanDesc)
/*     */   {
/*  54 */     this._config = config;
/*  55 */     this._beanDesc = beanDesc;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  64 */     JsonInclude.Value inclPerType = JsonInclude.Value.merge(beanDesc.findPropertyInclusion(JsonInclude.Value.empty()), config.getDefaultPropertyInclusion(beanDesc.getBeanClass(), JsonInclude.Value.empty()));
/*     */     
/*     */ 
/*     */ 
/*  68 */     this._defaultInclusion = JsonInclude.Value.merge(config.getDefaultPropertyInclusion(), inclPerType);
/*     */     
/*  70 */     this._useRealPropertyDefaults = (inclPerType.getValueInclusion() == JsonInclude.Include.NON_DEFAULT);
/*  71 */     this._annotationIntrospector = this._config.getAnnotationIntrospector();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public com.fasterxml.jackson.databind.util.Annotations getClassAnnotations()
/*     */   {
/*  81 */     return this._beanDesc.getClassAnnotations();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanPropertyWriter buildWriter(SerializerProvider prov, BeanPropertyDefinition propDef, JavaType declaredType, com.fasterxml.jackson.databind.JsonSerializer<?> ser, TypeSerializer typeSer, TypeSerializer contentTypeSer, AnnotatedMember am, boolean defaultUseStaticTyping)
/*     */     throws JsonMappingException
/*     */   {
/*     */     JavaType serializationType;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/*  99 */       serializationType = findSerializationType(am, defaultUseStaticTyping, declaredType);
/*     */     } catch (JsonMappingException e) {
/* 101 */       return (BeanPropertyWriter)prov.reportBadPropertyDefinition(this._beanDesc, propDef, e.getMessage(), new Object[0]);
/*     */     }
/*     */     
/*     */ 
/* 105 */     if (contentTypeSer != null)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 110 */       if (serializationType == null)
/*     */       {
/* 112 */         serializationType = declaredType;
/*     */       }
/* 114 */       JavaType ct = serializationType.getContentType();
/*     */       
/* 116 */       if (ct == null) {
/* 117 */         prov.reportBadPropertyDefinition(this._beanDesc, propDef, "serialization type " + serializationType + " has no content", new Object[0]);
/*     */       }
/*     */       
/* 120 */       serializationType = serializationType.withContentTypeHandler(contentTypeSer);
/* 121 */       ct = serializationType.getContentType();
/*     */     }
/*     */     
/* 124 */     Object valueToSuppress = null;
/* 125 */     boolean suppressNulls = false;
/*     */     
/*     */ 
/* 128 */     JavaType actualType = serializationType == null ? declaredType : serializationType;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 133 */     JsonInclude.Value inclV = this._config.getDefaultPropertyInclusion(actualType.getRawClass(), this._defaultInclusion);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 138 */     inclV = inclV.withOverrides(propDef.findInclusion());
/* 139 */     JsonInclude.Include inclusion = inclV.getValueInclusion();
/*     */     
/* 141 */     if (inclusion == JsonInclude.Include.USE_DEFAULTS) {
/* 142 */       inclusion = JsonInclude.Include.ALWAYS;
/*     */     }
/*     */     
/* 145 */     switch (inclusion)
/*     */     {
/*     */     case NON_DEFAULT: 
/*     */       Object defaultBean;
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 158 */       if ((this._useRealPropertyDefaults) && ((defaultBean = getDefaultBean()) != null))
/*     */       {
/* 160 */         if (prov.isEnabled(com.fasterxml.jackson.databind.MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
/* 161 */           am.fixAccess(this._config.isEnabled(com.fasterxml.jackson.databind.MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*     */         }
/*     */         try {
/* 164 */           valueToSuppress = am.getValue(defaultBean);
/*     */         } catch (Exception e) {
/* 166 */           _throwWrapped(e, propDef.getName(), defaultBean);
/*     */         }
/*     */       } else {
/* 169 */         valueToSuppress = getDefaultValue(actualType);
/* 170 */         suppressNulls = true;
/*     */       }
/* 172 */       if (valueToSuppress == null) {
/* 173 */         suppressNulls = true;
/*     */       }
/* 175 */       else if (valueToSuppress.getClass().isArray()) {
/* 176 */         valueToSuppress = com.fasterxml.jackson.databind.util.ArrayBuilders.getArrayComparator(valueToSuppress);
/*     */       }
/*     */       
/*     */ 
/*     */       break;
/*     */     case NON_ABSENT: 
/* 182 */       suppressNulls = true;
/*     */       
/* 184 */       if (actualType.isReferenceType()) {
/* 185 */         valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
/*     */       }
/*     */       
/*     */       break;
/*     */     case NON_EMPTY: 
/* 190 */       suppressNulls = true;
/*     */       
/* 192 */       valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
/* 193 */       break;
/*     */     case NON_NULL: 
/* 195 */       suppressNulls = true;
/*     */     
/*     */ 
/*     */     case ALWAYS: 
/*     */     default: 
/* 200 */       if ((actualType.isContainerType()) && (!this._config.isEnabled(com.fasterxml.jackson.databind.SerializationFeature.WRITE_EMPTY_JSON_ARRAYS)))
/*     */       {
/* 202 */         valueToSuppress = BeanPropertyWriter.MARKER_FOR_EMPTY;
/*     */       }
/*     */       break;
/*     */     }
/* 206 */     BeanPropertyWriter bpw = new BeanPropertyWriter(propDef, am, this._beanDesc.getClassAnnotations(), declaredType, ser, typeSer, serializationType, suppressNulls, valueToSuppress);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 211 */     Object serDef = this._annotationIntrospector.findNullSerializer(am);
/* 212 */     if (serDef != null) {
/* 213 */       bpw.assignNullSerializer(prov.serializerInstance(am, serDef));
/*     */     }
/*     */     
/* 216 */     com.fasterxml.jackson.databind.util.NameTransformer unwrapper = this._annotationIntrospector.findUnwrappingNameTransformer(am);
/* 217 */     if (unwrapper != null) {
/* 218 */       bpw = bpw.unwrappingWriter(unwrapper);
/*     */     }
/* 220 */     return bpw;
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
/*     */   protected JavaType findSerializationType(Annotated a, boolean useStaticTyping, JavaType declaredType)
/*     */     throws JsonMappingException
/*     */   {
/* 238 */     JavaType secondary = this._annotationIntrospector.refineSerializationType(this._config, a, declaredType);
/*     */     
/*     */ 
/*     */ 
/* 242 */     if (secondary != declaredType) {
/* 243 */       Class<?> serClass = secondary.getRawClass();
/*     */       
/* 245 */       Class<?> rawDeclared = declaredType.getRawClass();
/* 246 */       if (!serClass.isAssignableFrom(rawDeclared))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 255 */         if (!rawDeclared.isAssignableFrom(serClass)) {
/* 256 */           throw new IllegalArgumentException("Illegal concrete-type annotation for method '" + a.getName() + "': class " + serClass.getName() + " not a super-type of (declared) class " + rawDeclared.getName());
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 263 */       useStaticTyping = true;
/* 264 */       declaredType = secondary;
/*     */     }
/*     */     
/* 267 */     com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing typing = this._annotationIntrospector.findSerializationTyping(a);
/* 268 */     if ((typing != null) && (typing != com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing.DEFAULT_TYPING)) {
/* 269 */       useStaticTyping = typing == com.fasterxml.jackson.databind.annotation.JsonSerialize.Typing.STATIC;
/*     */     }
/* 271 */     if (useStaticTyping)
/*     */     {
/* 273 */       return declaredType.withStaticTyping();
/*     */     }
/*     */     
/* 276 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getDefaultBean()
/*     */   {
/* 287 */     Object def = this._defaultBean;
/* 288 */     if (def == null)
/*     */     {
/*     */ 
/*     */ 
/* 292 */       def = this._beanDesc.instantiateBean(this._config.canOverrideAccessModifiers());
/* 293 */       if (def == null)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 301 */         def = NO_DEFAULT_MARKER;
/*     */       }
/* 303 */       this._defaultBean = def;
/*     */     }
/* 305 */     return def == NO_DEFAULT_MARKER ? null : this._defaultBean;
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
/*     */   @Deprecated
/*     */   protected Object getPropertyDefaultValue(String name, AnnotatedMember member, JavaType type)
/*     */   {
/* 327 */     Object defaultBean = getDefaultBean();
/* 328 */     if (defaultBean == null) {
/* 329 */       return getDefaultValue(type);
/*     */     }
/*     */     try {
/* 332 */       return member.getValue(defaultBean);
/*     */     } catch (Exception e) {
/* 334 */       return _throwWrapped(e, name, defaultBean);
/*     */     }
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
/*     */   protected Object getDefaultValue(JavaType type)
/*     */   {
/* 355 */     Class<?> cls = type.getRawClass();
/*     */     
/* 357 */     Class<?> prim = com.fasterxml.jackson.databind.util.ClassUtil.primitiveType(cls);
/* 358 */     if (prim != null) {
/* 359 */       return com.fasterxml.jackson.databind.util.ClassUtil.defaultValue(prim);
/*     */     }
/* 361 */     if ((type.isContainerType()) || (type.isReferenceType())) {
/* 362 */       return JsonInclude.Include.NON_EMPTY;
/*     */     }
/* 364 */     if (cls == String.class) {
/* 365 */       return "";
/*     */     }
/* 367 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object _throwWrapped(Exception e, String propName, Object defaultBean)
/*     */   {
/* 378 */     Throwable t = e;
/* 379 */     while (t.getCause() != null) {
/* 380 */       t = t.getCause();
/*     */     }
/* 382 */     if ((t instanceof Error)) throw ((Error)t);
/* 383 */     if ((t instanceof RuntimeException)) throw ((RuntimeException)t);
/* 384 */     throw new IllegalArgumentException("Failed to get property '" + propName + "' of default " + defaultBean.getClass().getName() + " instance");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\PropertyBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */