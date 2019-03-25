/*    */ package com.fasterxml.jackson.databind.jsontype.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*    */ import java.io.IOException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AsExistingPropertyTypeSerializer
/*    */   extends AsPropertyTypeSerializer
/*    */ {
/*    */   public AsExistingPropertyTypeSerializer(TypeIdResolver idRes, BeanProperty property, String propName)
/*    */   {
/* 22 */     super(idRes, property, propName);
/*    */   }
/*    */   
/*    */   public AsExistingPropertyTypeSerializer forProperty(BeanProperty prop)
/*    */   {
/* 27 */     return this._property == prop ? this : new AsExistingPropertyTypeSerializer(this._idResolver, prop, this._typePropertyName);
/*    */   }
/*    */   
/*    */   public JsonTypeInfo.As getTypeInclusion()
/*    */   {
/* 32 */     return JsonTypeInfo.As.EXISTING_PROPERTY;
/*    */   }
/*    */   
/*    */   public void writeTypePrefixForObject(Object value, JsonGenerator gen) throws IOException
/*    */   {
/* 37 */     String typeId = idFromValue(value);
/* 38 */     if ((typeId != null) && (gen.canWriteTypeId())) {
/* 39 */       gen.writeTypeId(typeId);
/*    */     }
/* 41 */     gen.writeStartObject();
/*    */   }
/*    */   
/*    */   public void writeTypePrefixForObject(Object value, JsonGenerator gen, Class<?> type)
/*    */     throws IOException
/*    */   {
/* 47 */     String typeId = idFromValueAndType(value, type);
/* 48 */     if ((typeId != null) && (gen.canWriteTypeId())) {
/* 49 */       gen.writeTypeId(typeId);
/*    */     }
/* 51 */     gen.writeStartObject();
/*    */   }
/*    */   
/*    */   public void writeCustomTypePrefixForObject(Object value, JsonGenerator gen, String typeId)
/*    */     throws IOException
/*    */   {
/* 57 */     if ((typeId != null) && (gen.canWriteTypeId())) {
/* 58 */       gen.writeTypeId(typeId);
/*    */     }
/* 60 */     gen.writeStartObject();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsontype\impl\AsExistingPropertyTypeSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */