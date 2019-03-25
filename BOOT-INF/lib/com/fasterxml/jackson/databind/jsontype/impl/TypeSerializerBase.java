/*    */ package com.fasterxml.jackson.databind.jsontype.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ 
/*    */ public abstract class TypeSerializerBase
/*    */   extends TypeSerializer
/*    */ {
/*    */   protected final TypeIdResolver _idResolver;
/*    */   protected final BeanProperty _property;
/*    */   
/*    */   protected TypeSerializerBase(TypeIdResolver idRes, BeanProperty property)
/*    */   {
/* 16 */     this._idResolver = idRes;
/* 17 */     this._property = property;
/*    */   }
/*    */   
/*    */   public abstract JsonTypeInfo.As getTypeInclusion();
/*    */   
/*    */   public String getPropertyName()
/*    */   {
/* 24 */     return null;
/*    */   }
/*    */   
/* 27 */   public TypeIdResolver getTypeIdResolver() { return this._idResolver; }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String idFromValue(Object value)
/*    */   {
/* 36 */     String id = this._idResolver.idFromValue(value);
/* 37 */     if (id == null) {
/* 38 */       handleMissingId(value);
/*    */     }
/* 40 */     return id;
/*    */   }
/*    */   
/*    */   protected String idFromValueAndType(Object value, Class<?> type) {
/* 44 */     String id = this._idResolver.idFromValueAndType(value, type);
/* 45 */     if (id == null) {
/* 46 */       handleMissingId(value);
/*    */     }
/* 48 */     return id;
/*    */   }
/*    */   
/*    */   protected void handleMissingId(Object value) {}
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsontype\impl\TypeSerializerBase.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */