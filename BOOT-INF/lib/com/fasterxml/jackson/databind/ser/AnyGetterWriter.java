/*     */ package com.fasterxml.jackson.databind.ser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.ser.std.MapSerializer;
/*     */ import java.util.Map;
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
/*     */ public class AnyGetterWriter
/*     */ {
/*     */   protected final BeanProperty _property;
/*     */   protected final AnnotatedMember _accessor;
/*     */   protected JsonSerializer<Object> _serializer;
/*     */   protected MapSerializer _mapSerializer;
/*     */   
/*     */   public AnyGetterWriter(BeanProperty property, AnnotatedMember accessor, JsonSerializer<?> serializer)
/*     */   {
/*  32 */     this._accessor = accessor;
/*  33 */     this._property = property;
/*  34 */     this._serializer = serializer;
/*  35 */     if ((serializer instanceof MapSerializer)) {
/*  36 */       this._mapSerializer = ((MapSerializer)serializer);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void fixAccess(SerializationConfig config)
/*     */   {
/*  44 */     this._accessor.fixAccess(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*     */   }
/*     */   
/*     */ 
/*     */   public void getAndSerialize(Object bean, JsonGenerator gen, SerializerProvider provider)
/*     */     throws Exception
/*     */   {
/*  51 */     Object value = this._accessor.getValue(bean);
/*  52 */     if (value == null) {
/*  53 */       return;
/*     */     }
/*  55 */     if (!(value instanceof Map)) {
/*  56 */       provider.reportMappingProblem("Value returned by 'any-getter' %s() not java.util.Map but %s", new Object[] { this._accessor.getName(), value.getClass().getName() });
/*     */     }
/*     */     
/*     */ 
/*  60 */     if (this._mapSerializer != null) {
/*  61 */       this._mapSerializer.serializeFields((Map)value, gen, provider);
/*  62 */       return;
/*     */     }
/*  64 */     this._serializer.serialize(value, gen, provider);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void getAndFilter(Object bean, JsonGenerator gen, SerializerProvider provider, PropertyFilter filter)
/*     */     throws Exception
/*     */   {
/*  74 */     Object value = this._accessor.getValue(bean);
/*  75 */     if (value == null) {
/*  76 */       return;
/*     */     }
/*  78 */     if (!(value instanceof Map)) {
/*  79 */       provider.reportMappingProblem("Value returned by 'any-getter' (%s()) not java.util.Map but %s", new Object[] { this._accessor.getName(), value.getClass().getName() });
/*     */     }
/*     */     
/*     */ 
/*  83 */     if (this._mapSerializer != null) {
/*  84 */       this._mapSerializer.serializeFilteredFields((Map)value, gen, provider, filter, null);
/*  85 */       return;
/*     */     }
/*     */     
/*  88 */     this._serializer.serialize(value, gen, provider);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void resolve(SerializerProvider provider)
/*     */     throws JsonMappingException
/*     */   {
/*  96 */     if ((this._serializer instanceof ContextualSerializer)) {
/*  97 */       JsonSerializer<?> ser = provider.handlePrimaryContextualization(this._serializer, this._property);
/*  98 */       this._serializer = ser;
/*  99 */       if ((ser instanceof MapSerializer)) {
/* 100 */         this._mapSerializer = ((MapSerializer)ser);
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\AnyGetterWriter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */