/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.core.SerializableString;
/*     */ import com.fasterxml.jackson.core.io.SerializedString;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.PropertyMetadata;
/*     */ import com.fasterxml.jackson.databind.PropertyName;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.jsonschema.JsonSchema;
/*     */ import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap.SerializerAndMapResult;
/*     */ import com.fasterxml.jackson.databind.ser.impl.UnwrappingBeanPropertyWriter;
/*     */ import com.fasterxml.jackson.databind.ser.std.BeanSerializerBase;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ @JacksonStdImpl
/*     */ public class BeanPropertyWriter
/*     */   extends PropertyWriter
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  47 */   public static final Object MARKER_FOR_EMPTY = JsonInclude.Include.NON_EMPTY;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final SerializedString _name;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final PropertyName _wrapperName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JavaType _declaredType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final JavaType _cfgSerializationType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JavaType _nonTrivialBaseType;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final transient Annotations _contextAnnotations;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final AnnotatedMember _member;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected transient Method _accessorMethod;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected transient Field _field;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonSerializer<Object> _serializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected JsonSerializer<Object> _nullSerializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected TypeSerializer _typeSerializer;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected transient PropertySerializerMap _dynamicSerializers;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final boolean _suppressNulls;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Object _suppressableValue;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final Class<?>[] _includeInViews;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected transient HashMap<Object, Object> _internalSettings;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanPropertyWriter(BeanPropertyDefinition propDef, AnnotatedMember member, Annotations contextAnnotations, JavaType declaredType, JsonSerializer<?> ser, TypeSerializer typeSer, JavaType serType, boolean suppressNulls, Object suppressableValue)
/*     */   {
/* 210 */     super(propDef);
/* 211 */     this._member = member;
/* 212 */     this._contextAnnotations = contextAnnotations;
/*     */     
/* 214 */     this._name = new SerializedString(propDef.getName());
/* 215 */     this._wrapperName = propDef.getWrapperName();
/* 216 */     this._includeInViews = propDef.findViews();
/*     */     
/* 218 */     this._declaredType = declaredType;
/* 219 */     this._serializer = ser;
/* 220 */     this._dynamicSerializers = (ser == null ? PropertySerializerMap.emptyForProperties() : null);
/*     */     
/* 222 */     this._typeSerializer = typeSer;
/* 223 */     this._cfgSerializationType = serType;
/*     */     
/* 225 */     if ((member instanceof AnnotatedField)) {
/* 226 */       this._accessorMethod = null;
/* 227 */       this._field = ((Field)member.getMember());
/* 228 */     } else if ((member instanceof AnnotatedMethod)) {
/* 229 */       this._accessorMethod = ((Method)member.getMember());
/* 230 */       this._field = null;
/*     */     }
/*     */     else
/*     */     {
/* 234 */       this._accessorMethod = null;
/* 235 */       this._field = null;
/*     */     }
/* 237 */     this._suppressNulls = suppressNulls;
/* 238 */     this._suppressableValue = suppressableValue;
/*     */     
/*     */ 
/* 241 */     this._nullSerializer = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanPropertyWriter()
/*     */   {
/* 252 */     super(PropertyMetadata.STD_REQUIRED_OR_OPTIONAL);
/* 253 */     this._member = null;
/* 254 */     this._contextAnnotations = null;
/*     */     
/* 256 */     this._name = null;
/* 257 */     this._wrapperName = null;
/* 258 */     this._includeInViews = null;
/*     */     
/* 260 */     this._declaredType = null;
/* 261 */     this._serializer = null;
/* 262 */     this._dynamicSerializers = null;
/* 263 */     this._typeSerializer = null;
/* 264 */     this._cfgSerializationType = null;
/*     */     
/* 266 */     this._accessorMethod = null;
/* 267 */     this._field = null;
/* 268 */     this._suppressNulls = false;
/* 269 */     this._suppressableValue = null;
/*     */     
/* 271 */     this._nullSerializer = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected BeanPropertyWriter(BeanPropertyWriter base)
/*     */   {
/* 278 */     this(base, base._name);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected BeanPropertyWriter(BeanPropertyWriter base, PropertyName name)
/*     */   {
/* 285 */     super(base);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 292 */     this._name = new SerializedString(name.getSimpleName());
/* 293 */     this._wrapperName = base._wrapperName;
/*     */     
/* 295 */     this._contextAnnotations = base._contextAnnotations;
/* 296 */     this._declaredType = base._declaredType;
/*     */     
/* 298 */     this._member = base._member;
/* 299 */     this._accessorMethod = base._accessorMethod;
/* 300 */     this._field = base._field;
/*     */     
/* 302 */     this._serializer = base._serializer;
/* 303 */     this._nullSerializer = base._nullSerializer;
/*     */     
/* 305 */     if (base._internalSettings != null) {
/* 306 */       this._internalSettings = new HashMap(base._internalSettings);
/*     */     }
/*     */     
/* 309 */     this._cfgSerializationType = base._cfgSerializationType;
/* 310 */     this._dynamicSerializers = base._dynamicSerializers;
/* 311 */     this._suppressNulls = base._suppressNulls;
/* 312 */     this._suppressableValue = base._suppressableValue;
/* 313 */     this._includeInViews = base._includeInViews;
/* 314 */     this._typeSerializer = base._typeSerializer;
/* 315 */     this._nonTrivialBaseType = base._nonTrivialBaseType;
/*     */   }
/*     */   
/*     */   protected BeanPropertyWriter(BeanPropertyWriter base, SerializedString name) {
/* 319 */     super(base);
/* 320 */     this._name = name;
/* 321 */     this._wrapperName = base._wrapperName;
/*     */     
/* 323 */     this._member = base._member;
/* 324 */     this._contextAnnotations = base._contextAnnotations;
/* 325 */     this._declaredType = base._declaredType;
/* 326 */     this._accessorMethod = base._accessorMethod;
/* 327 */     this._field = base._field;
/* 328 */     this._serializer = base._serializer;
/* 329 */     this._nullSerializer = base._nullSerializer;
/* 330 */     if (base._internalSettings != null) {
/* 331 */       this._internalSettings = new HashMap(base._internalSettings);
/*     */     }
/*     */     
/* 334 */     this._cfgSerializationType = base._cfgSerializationType;
/* 335 */     this._dynamicSerializers = base._dynamicSerializers;
/* 336 */     this._suppressNulls = base._suppressNulls;
/* 337 */     this._suppressableValue = base._suppressableValue;
/* 338 */     this._includeInViews = base._includeInViews;
/* 339 */     this._typeSerializer = base._typeSerializer;
/* 340 */     this._nonTrivialBaseType = base._nonTrivialBaseType;
/*     */   }
/*     */   
/*     */   public BeanPropertyWriter rename(NameTransformer transformer) {
/* 344 */     String newName = transformer.transform(this._name.getValue());
/* 345 */     if (newName.equals(this._name.toString())) {
/* 346 */       return this;
/*     */     }
/* 348 */     return _new(PropertyName.construct(newName));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanPropertyWriter _new(PropertyName newName)
/*     */   {
/* 357 */     return new BeanPropertyWriter(this, newName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void assignTypeSerializer(TypeSerializer typeSer)
/*     */   {
/* 367 */     this._typeSerializer = typeSer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void assignSerializer(JsonSerializer<Object> ser)
/*     */   {
/* 375 */     if ((this._serializer != null) && (this._serializer != ser)) {
/* 376 */       throw new IllegalStateException("Can not override serializer");
/*     */     }
/* 378 */     this._serializer = ser;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void assignNullSerializer(JsonSerializer<Object> nullSer)
/*     */   {
/* 386 */     if ((this._nullSerializer != null) && (this._nullSerializer != nullSer)) {
/* 387 */       throw new IllegalStateException("Can not override null serializer");
/*     */     }
/* 389 */     this._nullSerializer = nullSer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanPropertyWriter unwrappingWriter(NameTransformer unwrapper)
/*     */   {
/* 397 */     return new UnwrappingBeanPropertyWriter(this, unwrapper);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setNonTrivialBaseType(JavaType t)
/*     */   {
/* 406 */     this._nonTrivialBaseType = t;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fixAccess(SerializationConfig config)
/*     */   {
/* 417 */     this._member.fixAccess(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
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
/*     */   Object readResolve()
/*     */   {
/* 432 */     if ((this._member instanceof AnnotatedField)) {
/* 433 */       this._accessorMethod = null;
/* 434 */       this._field = ((Field)this._member.getMember());
/* 435 */     } else if ((this._member instanceof AnnotatedMethod)) {
/* 436 */       this._accessorMethod = ((Method)this._member.getMember());
/* 437 */       this._field = null;
/*     */     }
/* 439 */     if (this._serializer == null) {
/* 440 */       this._dynamicSerializers = PropertySerializerMap.emptyForProperties();
/*     */     }
/* 442 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getName()
/*     */   {
/* 454 */     return this._name.getValue();
/*     */   }
/*     */   
/*     */ 
/*     */   public PropertyName getFullName()
/*     */   {
/* 460 */     return new PropertyName(this._name.getValue());
/*     */   }
/*     */   
/*     */   public JavaType getType()
/*     */   {
/* 465 */     return this._declaredType;
/*     */   }
/*     */   
/*     */   public PropertyName getWrapperName()
/*     */   {
/* 470 */     return this._wrapperName;
/*     */   }
/*     */   
/*     */ 
/*     */   public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */   {
/* 476 */     return this._member == null ? null : this._member.getAnnotation(acls);
/*     */   }
/*     */   
/*     */ 
/*     */   public <A extends Annotation> A getContextAnnotation(Class<A> acls)
/*     */   {
/* 482 */     return this._contextAnnotations == null ? null : this._contextAnnotations.get(acls);
/*     */   }
/*     */   
/*     */ 
/*     */   public AnnotatedMember getMember()
/*     */   {
/* 488 */     return this._member;
/*     */   }
/*     */   
/*     */ 
/*     */   protected void _depositSchemaProperty(ObjectNode propertiesNode, JsonNode schemaNode)
/*     */   {
/* 494 */     propertiesNode.set(getName(), schemaNode);
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
/*     */   public Object getInternalSetting(Object key)
/*     */   {
/* 510 */     return this._internalSettings == null ? null : this._internalSettings.get(key);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object setInternalSetting(Object key, Object value)
/*     */   {
/* 519 */     if (this._internalSettings == null) {
/* 520 */       this._internalSettings = new HashMap();
/*     */     }
/* 522 */     return this._internalSettings.put(key, value);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object removeInternalSetting(Object key)
/*     */   {
/* 531 */     Object removed = null;
/* 532 */     if (this._internalSettings != null) {
/* 533 */       removed = this._internalSettings.remove(key);
/*     */       
/* 535 */       if (this._internalSettings.size() == 0) {
/* 536 */         this._internalSettings = null;
/*     */       }
/*     */     }
/* 539 */     return removed;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public SerializableString getSerializedName()
/*     */   {
/* 549 */     return this._name;
/*     */   }
/*     */   
/*     */   public boolean hasSerializer() {
/* 553 */     return this._serializer != null;
/*     */   }
/*     */   
/*     */   public boolean hasNullSerializer() {
/* 557 */     return this._nullSerializer != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public TypeSerializer getTypeSerializer()
/*     */   {
/* 564 */     return this._typeSerializer;
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
/*     */   public boolean isUnwrapping()
/*     */   {
/* 578 */     return false;
/*     */   }
/*     */   
/*     */   public boolean willSuppressNulls() {
/* 582 */     return this._suppressNulls;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean wouldConflictWithName(PropertyName name)
/*     */   {
/* 592 */     if (this._wrapperName != null) {
/* 593 */       return this._wrapperName.equals(name);
/*     */     }
/*     */     
/* 596 */     return (name.hasSimpleName(this._name.getValue())) && (!name.hasNamespace());
/*     */   }
/*     */   
/*     */   public JsonSerializer<Object> getSerializer()
/*     */   {
/* 601 */     return this._serializer;
/*     */   }
/*     */   
/*     */   public JavaType getSerializationType() {
/* 605 */     return this._cfgSerializationType;
/*     */   }
/*     */   
/*     */   public Class<?> getRawSerializationType() {
/* 609 */     return this._cfgSerializationType == null ? null : this._cfgSerializationType.getRawClass();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Class<?> getPropertyType()
/*     */   {
/* 618 */     if (this._accessorMethod != null) {
/* 619 */       return this._accessorMethod.getReturnType();
/*     */     }
/* 621 */     if (this._field != null) {
/* 622 */       return this._field.getType();
/*     */     }
/* 624 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Type getGenericPropertyType()
/*     */   {
/* 636 */     if (this._accessorMethod != null) {
/* 637 */       return this._accessorMethod.getGenericReturnType();
/*     */     }
/* 639 */     if (this._field != null) {
/* 640 */       return this._field.getGenericType();
/*     */     }
/* 642 */     return null;
/*     */   }
/*     */   
/*     */   public Class<?>[] getViews() {
/* 646 */     return this._includeInViews;
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
/*     */   public void serializeAsField(Object bean, JsonGenerator gen, SerializerProvider prov)
/*     */     throws Exception
/*     */   {
/* 664 */     Object value = this._accessorMethod == null ? this._field.get(bean) : this._accessorMethod.invoke(bean, new Object[0]);
/*     */     
/*     */ 
/*     */ 
/* 668 */     if (value == null) {
/* 669 */       if (this._nullSerializer != null) {
/* 670 */         gen.writeFieldName(this._name);
/* 671 */         this._nullSerializer.serialize(null, gen, prov);
/*     */       }
/* 673 */       return;
/*     */     }
/*     */     
/* 676 */     JsonSerializer<Object> ser = this._serializer;
/* 677 */     if (ser == null) {
/* 678 */       Class<?> cls = value.getClass();
/* 679 */       PropertySerializerMap m = this._dynamicSerializers;
/* 680 */       ser = m.serializerFor(cls);
/* 681 */       if (ser == null) {
/* 682 */         ser = _findAndAddDynamic(m, cls, prov);
/*     */       }
/*     */     }
/*     */     
/* 686 */     if (this._suppressableValue != null) {
/* 687 */       if (MARKER_FOR_EMPTY == this._suppressableValue) {
/* 688 */         if (!ser.isEmpty(prov, value)) {}
/*     */ 
/*     */       }
/* 691 */       else if (this._suppressableValue.equals(value)) {
/* 692 */         return;
/*     */       }
/*     */     }
/*     */     
/* 696 */     if (value == bean)
/*     */     {
/* 698 */       if (_handleSelfReference(bean, gen, prov, ser)) {
/* 699 */         return;
/*     */       }
/*     */     }
/* 702 */     gen.writeFieldName(this._name);
/* 703 */     if (this._typeSerializer == null) {
/* 704 */       ser.serialize(value, gen, prov);
/*     */     } else {
/* 706 */       ser.serializeWithType(value, gen, prov, this._typeSerializer);
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
/*     */   public void serializeAsOmittedField(Object bean, JsonGenerator gen, SerializerProvider prov)
/*     */     throws Exception
/*     */   {
/* 720 */     if (!gen.canOmitFields()) {
/* 721 */       gen.writeOmittedField(this._name.getValue());
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
/*     */   public void serializeAsElement(Object bean, JsonGenerator gen, SerializerProvider prov)
/*     */     throws Exception
/*     */   {
/* 736 */     Object value = this._accessorMethod == null ? this._field.get(bean) : this._accessorMethod.invoke(bean, new Object[0]);
/*     */     
/* 738 */     if (value == null) {
/* 739 */       if (this._nullSerializer != null) {
/* 740 */         this._nullSerializer.serialize(null, gen, prov);
/*     */       } else {
/* 742 */         gen.writeNull();
/*     */       }
/* 744 */       return;
/*     */     }
/*     */     
/* 747 */     JsonSerializer<Object> ser = this._serializer;
/* 748 */     if (ser == null) {
/* 749 */       Class<?> cls = value.getClass();
/* 750 */       PropertySerializerMap map = this._dynamicSerializers;
/* 751 */       ser = map.serializerFor(cls);
/* 752 */       if (ser == null) {
/* 753 */         ser = _findAndAddDynamic(map, cls, prov);
/*     */       }
/*     */     }
/*     */     
/* 757 */     if (this._suppressableValue != null) {
/* 758 */       if (MARKER_FOR_EMPTY == this._suppressableValue) {
/* 759 */         if (ser.isEmpty(prov, value))
/*     */         {
/* 761 */           serializeAsPlaceholder(bean, gen, prov);
/*     */         }
/*     */       }
/* 764 */       else if (this._suppressableValue.equals(value))
/*     */       {
/*     */ 
/* 767 */         serializeAsPlaceholder(bean, gen, prov);
/* 768 */         return;
/*     */       }
/*     */     }
/*     */     
/* 772 */     if ((value == bean) && 
/* 773 */       (_handleSelfReference(bean, gen, prov, ser))) {
/* 774 */       return;
/*     */     }
/*     */     
/* 777 */     if (this._typeSerializer == null) {
/* 778 */       ser.serialize(value, gen, prov);
/*     */     } else {
/* 780 */       ser.serializeWithType(value, gen, prov, this._typeSerializer);
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
/*     */   public void serializeAsPlaceholder(Object bean, JsonGenerator gen, SerializerProvider prov)
/*     */     throws Exception
/*     */   {
/* 795 */     if (this._nullSerializer != null) {
/* 796 */       this._nullSerializer.serialize(null, gen, prov);
/*     */     } else {
/* 798 */       gen.writeNull();
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
/*     */   public void depositSchemaProperty(JsonObjectFormatVisitor v, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 812 */     if (v != null) {
/* 813 */       if (isRequired()) {
/* 814 */         v.property(this);
/*     */       } else {
/* 816 */         v.optionalProperty(this);
/*     */       }
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
/*     */   @Deprecated
/*     */   public void depositSchemaProperty(ObjectNode propertiesNode, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/* 838 */     JavaType propType = getSerializationType();
/*     */     
/*     */ 
/* 841 */     Type hint = (Type)(propType == null ? getType() : propType.getRawClass());
/*     */     
/*     */ 
/* 844 */     JsonSerializer<Object> ser = getSerializer();
/* 845 */     if (ser == null) {
/* 846 */       ser = provider.findValueSerializer(getType(), this);
/*     */     }
/* 848 */     boolean isOptional = !isRequired();
/* 849 */     JsonNode schemaNode; JsonNode schemaNode; if ((ser instanceof SchemaAware)) {
/* 850 */       schemaNode = ((SchemaAware)ser).getSchema(provider, hint, isOptional);
/*     */     }
/*     */     else {
/* 853 */       schemaNode = JsonSchema.getDefaultSchemaNode();
/*     */     }
/*     */     
/* 856 */     _depositSchemaProperty(propertiesNode, schemaNode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected JsonSerializer<Object> _findAndAddDynamic(PropertySerializerMap map, Class<?> type, SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/*     */     PropertySerializerMap.SerializerAndMapResult result;
/*     */     
/*     */ 
/*     */     PropertySerializerMap.SerializerAndMapResult result;
/*     */     
/* 869 */     if (this._nonTrivialBaseType != null) {
/* 870 */       JavaType t = provider.constructSpecializedType(this._nonTrivialBaseType, type);
/*     */       
/* 872 */       result = map.findAndAddPrimarySerializer(t, provider, this);
/*     */     } else {
/* 874 */       result = map.findAndAddPrimarySerializer(type, provider, this);
/*     */     }
/*     */     
/* 877 */     if (map != result.map) {
/* 878 */       this._dynamicSerializers = result.map;
/*     */     }
/* 880 */     return result.serializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Object get(Object bean)
/*     */     throws Exception
/*     */   {
/* 892 */     return this._accessorMethod == null ? this._field.get(bean) : this._accessorMethod.invoke(bean, new Object[0]);
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
/*     */   protected boolean _handleSelfReference(Object bean, JsonGenerator gen, SerializerProvider prov, JsonSerializer<?> ser)
/*     */     throws JsonMappingException
/*     */   {
/* 913 */     if ((prov.isEnabled(SerializationFeature.FAIL_ON_SELF_REFERENCES)) && (!ser.usesObjectId()))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 920 */       if ((ser instanceof BeanSerializerBase)) {
/* 921 */         prov.reportMappingProblem("Direct self-reference leading to cycle", new Object[0]);
/*     */       }
/*     */     }
/* 924 */     return false;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 929 */     StringBuilder sb = new StringBuilder(40);
/* 930 */     sb.append("property '").append(getName()).append("' (");
/* 931 */     if (this._accessorMethod != null) {
/* 932 */       sb.append("via method ").append(this._accessorMethod.getDeclaringClass().getName()).append("#").append(this._accessorMethod.getName());
/*     */ 
/*     */     }
/* 935 */     else if (this._field != null) {
/* 936 */       sb.append("field \"").append(this._field.getDeclaringClass().getName()).append("#").append(this._field.getName());
/*     */     }
/*     */     else {
/* 939 */       sb.append("virtual");
/*     */     }
/* 941 */     if (this._serializer == null) {
/* 942 */       sb.append(", no static serializer");
/*     */     } else {
/* 944 */       sb.append(", static serializer of type " + this._serializer.getClass().getName());
/*     */     }
/*     */     
/* 947 */     sb.append(')');
/* 948 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\BeanPropertyWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */