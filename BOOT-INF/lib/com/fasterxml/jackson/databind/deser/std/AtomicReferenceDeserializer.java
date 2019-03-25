/*    */ package com.fasterxml.jackson.databind.deser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.DeserializationContext;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*    */ import java.util.concurrent.atomic.AtomicReference;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AtomicReferenceDeserializer
/*    */   extends ReferenceTypeDeserializer<AtomicReference<Object>>
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   
/*    */   @Deprecated
/*    */   public AtomicReferenceDeserializer(JavaType fullType)
/*    */   {
/* 21 */     this(fullType, null, null);
/*    */   }
/*    */   
/*    */ 
/*    */   public AtomicReferenceDeserializer(JavaType fullType, TypeDeserializer typeDeser, JsonDeserializer<?> deser)
/*    */   {
/* 27 */     super(fullType, typeDeser, deser);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public AtomicReferenceDeserializer withResolved(TypeDeserializer typeDeser, JsonDeserializer<?> valueDeser)
/*    */   {
/* 38 */     return new AtomicReferenceDeserializer(this._fullType, typeDeser, valueDeser);
/*    */   }
/*    */   
/*    */   public AtomicReference<Object> getNullValue(DeserializationContext ctxt)
/*    */   {
/* 43 */     return new AtomicReference();
/*    */   }
/*    */   
/*    */   public AtomicReference<Object> referenceValue(Object contents)
/*    */   {
/* 48 */     return new AtomicReference(contents);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\AtomicReferenceDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */