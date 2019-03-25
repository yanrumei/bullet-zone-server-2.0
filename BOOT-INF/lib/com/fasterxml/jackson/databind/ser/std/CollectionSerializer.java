/*     */ package com.fasterxml.jackson.databind.ser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonGenerationException;
/*     */ import com.fasterxml.jackson.core.JsonGenerator;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.SerializerProvider;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.ContainerSerializer;
/*     */ import com.fasterxml.jackson.databind.ser.impl.PropertySerializerMap;
/*     */ import java.io.IOException;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
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
/*     */ 
/*     */ public class CollectionSerializer
/*     */   extends AsArraySerializerBase<Collection<?>>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   
/*     */   public CollectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, JsonSerializer<Object> valueSerializer)
/*     */   {
/*  41 */     super(Collection.class, elemType, staticTyping, vts, valueSerializer);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public CollectionSerializer(JavaType elemType, boolean staticTyping, TypeSerializer vts, BeanProperty property, JsonSerializer<Object> valueSerializer)
/*     */   {
/*  51 */     this(elemType, staticTyping, vts, valueSerializer);
/*     */   }
/*     */   
/*     */ 
/*     */   public CollectionSerializer(CollectionSerializer src, BeanProperty property, TypeSerializer vts, JsonSerializer<?> valueSerializer, Boolean unwrapSingle)
/*     */   {
/*  57 */     super(src, property, vts, valueSerializer, unwrapSingle);
/*     */   }
/*     */   
/*     */   public ContainerSerializer<?> _withValueTypeSerializer(TypeSerializer vts)
/*     */   {
/*  62 */     return new CollectionSerializer(this, this._property, vts, this._elementSerializer, this._unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CollectionSerializer withResolved(BeanProperty property, TypeSerializer vts, JsonSerializer<?> elementSerializer, Boolean unwrapSingle)
/*     */   {
/*  69 */     return new CollectionSerializer(this, property, vts, elementSerializer, unwrapSingle);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isEmpty(SerializerProvider prov, Collection<?> value)
/*     */   {
/*  80 */     return (value == null) || (value.isEmpty());
/*     */   }
/*     */   
/*     */   public boolean hasSingleElement(Collection<?> value)
/*     */   {
/*  85 */     Iterator<?> it = value.iterator();
/*  86 */     if (!it.hasNext()) {
/*  87 */       return false;
/*     */     }
/*  89 */     it.next();
/*  90 */     return !it.hasNext();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void serialize(Collection<?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 102 */     int len = value.size();
/* 103 */     if ((len == 1) && (
/* 104 */       ((this._unwrapSingle == null) && (provider.isEnabled(SerializationFeature.WRITE_SINGLE_ELEM_ARRAYS_UNWRAPPED))) || (this._unwrapSingle == Boolean.TRUE)))
/*     */     {
/*     */ 
/* 107 */       serializeContents(value, jgen, provider);
/* 108 */       return;
/*     */     }
/*     */     
/* 111 */     jgen.writeStartArray(len);
/* 112 */     serializeContents(value, jgen, provider);
/* 113 */     jgen.writeEndArray();
/*     */   }
/*     */   
/*     */   public void serializeContents(Collection<?> value, JsonGenerator jgen, SerializerProvider provider)
/*     */     throws IOException
/*     */   {
/* 119 */     if (this._elementSerializer != null) {
/* 120 */       serializeContentsUsing(value, jgen, provider, this._elementSerializer);
/* 121 */       return;
/*     */     }
/* 123 */     Iterator<?> it = value.iterator();
/* 124 */     if (!it.hasNext()) {
/* 125 */       return;
/*     */     }
/* 127 */     PropertySerializerMap serializers = this._dynamicSerializers;
/* 128 */     TypeSerializer typeSer = this._valueTypeSerializer;
/*     */     
/* 130 */     int i = 0;
/*     */     try {
/*     */       do {
/* 133 */         Object elem = it.next();
/* 134 */         if (elem == null) {
/* 135 */           provider.defaultSerializeNull(jgen);
/*     */         } else {
/* 137 */           Class<?> cc = elem.getClass();
/* 138 */           JsonSerializer<Object> serializer = serializers.serializerFor(cc);
/* 139 */           if (serializer == null) {
/* 140 */             if (this._elementType.hasGenericTypes()) {
/* 141 */               serializer = _findAndAddDynamic(serializers, provider.constructSpecializedType(this._elementType, cc), provider);
/*     */             }
/*     */             else {
/* 144 */               serializer = _findAndAddDynamic(serializers, cc, provider);
/*     */             }
/* 146 */             serializers = this._dynamicSerializers;
/*     */           }
/* 148 */           if (typeSer == null) {
/* 149 */             serializer.serialize(elem, jgen, provider);
/*     */           } else {
/* 151 */             serializer.serializeWithType(elem, jgen, provider, typeSer);
/*     */           }
/*     */         }
/* 154 */         i++;
/* 155 */       } while (it.hasNext());
/*     */     } catch (Exception e) {
/* 157 */       wrapAndThrow(provider, e, value, i);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void serializeContentsUsing(Collection<?> value, JsonGenerator jgen, SerializerProvider provider, JsonSerializer<Object> ser)
/*     */     throws IOException, JsonGenerationException
/*     */   {
/* 165 */     Iterator<?> it = value.iterator();
/* 166 */     if (it.hasNext()) {
/* 167 */       TypeSerializer typeSer = this._valueTypeSerializer;
/* 168 */       int i = 0;
/*     */       do {
/* 170 */         Object elem = it.next();
/*     */         try {
/* 172 */           if (elem == null) {
/* 173 */             provider.defaultSerializeNull(jgen);
/*     */           }
/* 175 */           else if (typeSer == null) {
/* 176 */             ser.serialize(elem, jgen, provider);
/*     */           } else {
/* 178 */             ser.serializeWithType(elem, jgen, provider, typeSer);
/*     */           }
/*     */           
/* 181 */           i++;
/*     */         } catch (Exception e) {
/* 183 */           wrapAndThrow(provider, e, value, i);
/*     */         }
/* 185 */       } while (it.hasNext());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\ser\std\CollectionSerializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */