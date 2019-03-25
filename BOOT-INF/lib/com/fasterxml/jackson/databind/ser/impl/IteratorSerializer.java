/*    */ package com.fasterxml.jackson.databind.ser.impl;
/*    */ 
/*    */ import com.fasterxml.jackson.core.JsonGenerator;
/*    */ import com.fasterxml.jackson.databind.BeanProperty;
/*    */ import com.fasterxml.jackson.databind.JavaType;
/*    */ import com.fasterxml.jackson.databind.JsonSerializer;
/*    */ import com.fasterxml.jackson.databind.SerializationFeature;
/*    */ import com.fasterxml.jackson.databind.SerializerProvider;
/*    */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*    */ import com.fasterxml.jackson.databind.ser.std.AsArraySerializerBase;
/*    */ import java.io.IOException;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ @com.fasterxml.jackson.databind.annotation.JacksonStdImpl
/*    */ public class IteratorSerializer extends AsArraySerializerBase<Iterator<?>>
/*    */ {
/*    */   public IteratorSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts)
/*    */   {
/* 19 */     super(Iterator.class, elemType, staticTyping, vts, null);
/*    */   }
/*    */   
/*    */ 
/*    */   public IteratorSerializer(IteratorSerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSerializer, Boolean unwrapSingle)
/*    */   {
/* 25 */     super(src, property, vts, valueSerializer, unwrapSingle);
/*    */   }
/*    */   
/*    */   public boolean isEmpty(SerializerProvider prov, Iterator<?> value)
/*    */   {
/* 30 */     return (value == null) || (!value.hasNext());
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean hasSingleElement(Iterator<?> value)
/*    */   {
/* 36 */     return false;
/*    */   }
/*    */   
/*    */   public com.fasterxml.jackson.databind.ser.ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*    */   {
/* 41 */     return new IteratorSerializer(this, this._property, vts, this._elementSerializer, this._unwrapSingle);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public IteratorSerializer withResolved(BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer, Boolean unwrapSingle)
/*    */   {
/* 48 */     return new IteratorSerializer(this, property, vts, elementSerializer, unwrapSingle);
/*    */   }
/*    */   
/*    */ 
/*    */   public final void serialize(Iterator<?> value, JsonGenerator gen, SerializerProvider provider)
/*    */     throws IOException
/*    */   {
/* 55 */     if (((this._unwrapSingle == null) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) || (this._unwrapSingle == Boolean.TRUE))
/*    */     {
/*    */ 
/* 58 */       if (hasSingleElement(value)) {
/* 59 */         serializeContents(value, gen, provider);
/* 60 */         return;
/*    */       }
/*    */     }
/* 63 */     gen.writeStartArray();
/* 64 */     serializeContents(value, gen, provider);
/* 65 */     gen.writeEndArray();
/*    */   }
/*    */   
/*    */ 
/*    */   public void serializeContents(Iterator<?> value, JsonGenerator gen, SerializerProvider provider)
/*    */     throws IOException
/*    */   {
/* 72 */     if (value.hasNext()) {
/* 73 */       TypeSerializer typeSer = this._valueTypeSerializer;
/* 74 */       JsonSerializer<Object> prevSerializer = null;
/* 75 */       Class<?> prevClass = null;
/*    */       do {
/* 77 */         Object elem = value.next();
/* 78 */         if (elem == null) {
/* 79 */           provider.defaultSerializeNull(gen);
/*    */         }
/*    */         else {
/* 82 */           JsonSerializer<Object> currSerializer = this._elementSerializer;
/* 83 */           if (currSerializer == null)
/*    */           {
/* 85 */             Class<?> cc = elem.getClass();
/* 86 */             if (cc == prevClass) {
/* 87 */               currSerializer = prevSerializer;
/*    */             } else {
/* 89 */               currSerializer = provider.findValueSerializer(cc, this._property);
/* 90 */               prevSerializer = currSerializer;
/* 91 */               prevClass = cc;
/*    */             }
/*    */           }
/* 94 */           if (typeSer == null) {
/* 95 */             currSerializer.serialize(elem, gen, provider);
/*    */           } else
/* 97 */             currSerializer.serializeWithType(elem, gen, provider, typeSer);
/*    */         }
/* 99 */       } while (value.hasNext());
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\impl\IteratorSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */