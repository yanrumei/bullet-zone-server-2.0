/*    */ package com.fasterxml.jackson.databind.ser.std;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.SerializationFeature;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import java.io.IOException;
/*    */ import java.util.EnumSet;
/*    */ 
/*    */ public class EnumSetSerializer
/*    */   extends AsArraySerializerBase<EnumSet<? extends Enum<?>>>
/*    */ {
/*    */   public EnumSetSerializer(JavaType elemType)
/*    */   {
/* 18 */     super(EnumSet.class, elemType, true, null, null);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   @Deprecated
/*    */   public EnumSetSerializer(JavaType elemType, BeanProperty property)
/*    */   {
/* 26 */     this(elemType);
/*    */   }
/*    */   
/*    */ 
/*    */   public EnumSetSerializer(EnumSetSerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSerializer, Boolean unwrapSingle)
/*    */   {
/* 32 */     super(src, property, vts, valueSerializer, unwrapSingle);
/*    */   }
/*    */   
/*    */ 
/*    */   public EnumSetSerializer _withValueTypeSerializer(TypeSerializer vts)
/*    */   {
/* 38 */     return this;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public EnumSetSerializer withResolved(BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer, Boolean unwrapSingle)
/*    */   {
/* 45 */     return new EnumSetSerializer(this, property, vts, elementSerializer, unwrapSingle);
/*    */   }
/*    */   
/*    */   public boolean isEmpty(SerializerProvider prov, EnumSet<? extends Enum<?>> value)
/*    */   {
/* 50 */     return (value == null) || (value.isEmpty());
/*    */   }
/*    */   
/*    */   public boolean hasSingleElement(EnumSet<? extends Enum<?>> value)
/*    */   {
/* 55 */     return value.size() == 1;
/*    */   }
/*    */   
/*    */ 
/*    */   public final void serialize(EnumSet<? extends Enum<?>> value, JsonGenerator gen, SerializerProvider provider)
/*    */     throws IOException
/*    */   {
/* 62 */     int len = value.size();
/* 63 */     if ((len == 1) && (
/* 64 */       ((this._unwrapSingle == null) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) || (this._unwrapSingle == Boolean.TRUE)))
/*    */     {
/*    */ 
/* 67 */       serializeContents(value, gen, provider);
/* 68 */       return;
/*    */     }
/*    */     
/* 71 */     gen.writeStartArray(len);
/* 72 */     serializeContents(value, gen, provider);
/* 73 */     gen.writeEndArray();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void serializeContents(EnumSet<? extends Enum<?>> value, JsonGenerator gen, SerializerProvider provider)
/*    */     throws IOException
/*    */   {
/* 81 */     JsonSerializer<Object> enumSer = this._elementSerializer;
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 86 */     for (Enum<?> en : value) {
/* 87 */       if (enumSer == null)
/*    */       {
/*    */ 
/*    */ 
/* 91 */         enumSer = provider.findValueSerializer(en.getDeclaringClass(), this._property);
/*    */       }
/* 93 */       enumSer.serialize(en, gen, provider);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\EnumSetSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */