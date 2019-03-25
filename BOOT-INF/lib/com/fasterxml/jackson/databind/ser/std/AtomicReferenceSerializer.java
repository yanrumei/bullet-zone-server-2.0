/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.annotation.JsonInclude.Include;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import com.fasterxml.jackson.databind.type.ReferenceType;
/*    */ import com.fasterxml.jackson.databind.util.NameTransformer;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AtomicReferenceSerializer
/*    */   extends ReferenceTypeSerializer<AtomicReference<?>>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   public AtomicReferenceSerializer(ReferenceType fullType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> ser)
/*    */   {
/* 26 */     super(fullType, staticTyping, vts, ser);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected AtomicReferenceSerializer(AtomicReferenceSerializer base, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSer, NameTransformer unwrapper, JsonInclude.Include contentIncl)
/*    */   {
/* 34 */     super(base, property, vts, valueSer, unwrapper, contentIncl);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected AtomicReferenceSerializer withResolved(BeanProperty prop, TypeSerializer vts, JsonSerializer<?> valueSer, NameTransformer unwrapper, JsonInclude.Include contentIncl)
/*    */   {
/* 43 */     if ((this._property == prop) && (contentIncl == this._contentInclusion) && (this._valueTypeSerializer == vts) && (this._valueSerializer == valueSer) && (this._unwrapper == unwrapper))
/*    */     {
/*    */ 
/* 46 */       return this;
/*    */     }
/* 48 */     return new AtomicReferenceSerializer(this, prop, vts, valueSer, unwrapper, contentIncl);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean _isValueEmpty(AtomicReference<?> value)
/*    */   {
/* 59 */     return value.get() == null;
/*    */   }
/*    */   
/*    */   protected Object _getReferenced(AtomicReference<?> value)
/*    */   {
/* 64 */     return value.get();
/*    */   }
/*    */   
/*    */   protected Object _getReferencedIfPresent(AtomicReference<?> value)
/*    */   {
/* 69 */     return value.get();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\AtomicReferenceSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */