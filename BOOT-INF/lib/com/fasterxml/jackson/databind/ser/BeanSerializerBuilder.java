/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.BeanDescription;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanSerializerBuilder
/*     */ {
/*  19 */   private static final BeanPropertyWriter[] NO_PROPERTIES = new BeanPropertyWriter[0];
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final BeanDescription _beanDesc;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SerializationConfig _config;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<BeanPropertyWriter> _properties;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BeanPropertyWriter[] _filteredProperties;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AnyGetterWriter _anyGetter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object _filterId;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AnnotatedMember _typeId;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ObjectIdWriter _objectIdWriter;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanSerializerBuilder(BeanDescription beanDesc)
/*     */   {
/*  77 */     this._beanDesc = beanDesc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected BeanSerializerBuilder(BeanSerializerBuilder src)
/*     */   {
/*  84 */     this._beanDesc = src._beanDesc;
/*  85 */     this._properties = src._properties;
/*  86 */     this._filteredProperties = src._filteredProperties;
/*  87 */     this._anyGetter = src._anyGetter;
/*  88 */     this._filterId = src._filterId;
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
/*     */   protected void setConfig(SerializationConfig config)
/*     */   {
/* 101 */     this._config = config;
/*     */   }
/*     */   
/*     */   public void setProperties(List<BeanPropertyWriter> properties) {
/* 105 */     this._properties = properties;
/*     */   }
/*     */   
/*     */   public void setFilteredProperties(BeanPropertyWriter[] properties) {
/* 109 */     this._filteredProperties = properties;
/*     */   }
/*     */   
/*     */   public void setAnyGetter(AnyGetterWriter anyGetter) {
/* 113 */     this._anyGetter = anyGetter;
/*     */   }
/*     */   
/*     */   public void setFilterId(Object filterId) {
/* 117 */     this._filterId = filterId;
/*     */   }
/*     */   
/*     */   public void setTypeId(AnnotatedMember idProp)
/*     */   {
/* 122 */     if (this._typeId != null) {
/* 123 */       throw new IllegalArgumentException("Multiple type ids specified with " + this._typeId + " and " + idProp);
/*     */     }
/* 125 */     this._typeId = idProp;
/*     */   }
/*     */   
/*     */   public void setObjectIdWriter(ObjectIdWriter w) {
/* 129 */     this._objectIdWriter = w;
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
/* 140 */   public AnnotatedClass getClassInfo() { return this._beanDesc.getClassInfo(); }
/*     */   
/* 142 */   public BeanDescription getBeanDescription() { return this._beanDesc; }
/*     */   
/* 144 */   public List<BeanPropertyWriter> getProperties() { return this._properties; }
/*     */   
/* 146 */   public boolean hasProperties() { return (this._properties != null) && (this._properties.size() > 0); }
/*     */   
/*     */ 
/* 149 */   public BeanPropertyWriter[] getFilteredProperties() { return this._filteredProperties; }
/*     */   
/* 151 */   public AnyGetterWriter getAnyGetter() { return this._anyGetter; }
/*     */   
/* 153 */   public Object getFilterId() { return this._filterId; }
/*     */   
/* 155 */   public AnnotatedMember getTypeId() { return this._typeId; }
/*     */   
/* 157 */   public ObjectIdWriter getObjectIdWriter() { return this._objectIdWriter; }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonSerializer<?> build()
/*     */   {
/*     */     BeanPropertyWriter[] properties;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     BeanPropertyWriter[] properties;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 175 */     if ((this._properties == null) || (this._properties.isEmpty())) {
/* 176 */       if ((this._anyGetter == null) && (this._objectIdWriter == null)) {
/* 177 */         return null;
/*     */       }
/* 179 */       properties = NO_PROPERTIES;
/*     */     } else {
/* 181 */       properties = (BeanPropertyWriter[])this._properties.toArray(new BeanPropertyWriter[this._properties.size()]);
/* 182 */       if (this._config.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS)) {
/* 183 */         int i = 0; for (int end = properties.length; i < end; i++) {
/* 184 */           properties[i].fixAccess(this._config);
/*     */         }
/*     */       }
/*     */     }
/* 188 */     if (this._anyGetter != null) {
/* 189 */       this._anyGetter.fixAccess(this._config);
/*     */     }
/* 191 */     if ((this._typeId != null) && 
/* 192 */       (this._config.isEnabled(MapperFeature.CAN_OVERRIDE_ACCESS_MODIFIERS))) {
/* 193 */       this._typeId.fixAccess(this._config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*     */     }
/*     */     
/* 196 */     return new BeanSerializer(this._beanDesc.getType(), this, properties, this._filteredProperties);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanSerializer createDummy()
/*     */   {
/* 206 */     return BeanSerializer.createDummy(this._beanDesc.getType());
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\BeanSerializerBuilder.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */