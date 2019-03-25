/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
/*     */ import com.fasterxml.jackson.databind.deser.ResolvableDeserializer;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import java.io.IOException;
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
/*     */ public class StdDelegatingDeserializer<T>
/*     */   extends StdDeserializer<T>
/*     */   implements ContextualDeserializer, ResolvableDeserializer
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final Converter<Object, T> _converter;
/*     */   protected final JavaType _delegateType;
/*     */   protected final JsonDeserializer<Object> _delegateDeserializer;
/*     */   
/*     */   public StdDelegatingDeserializer(Converter<?, T> converter)
/*     */   {
/*  61 */     super(Object.class);
/*  62 */     this._converter = converter;
/*  63 */     this._delegateType = null;
/*  64 */     this._delegateDeserializer = null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public StdDelegatingDeserializer(Converter<Object, T> converter, JavaType delegateType, JsonDeserializer<?> delegateDeserializer)
/*     */   {
/*  71 */     super(delegateType);
/*  72 */     this._converter = converter;
/*  73 */     this._delegateType = delegateType;
/*  74 */     this._delegateDeserializer = delegateDeserializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StdDelegatingDeserializer(StdDelegatingDeserializer<T> src)
/*     */   {
/*  82 */     super(src);
/*  83 */     this._converter = src._converter;
/*  84 */     this._delegateType = src._delegateType;
/*  85 */     this._delegateDeserializer = src._delegateDeserializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected StdDelegatingDeserializer<T> withDelegate(Converter<Object, T> converter, JavaType delegateType, JsonDeserializer<?> delegateDeserializer)
/*     */   {
/*  95 */     if (getClass() != StdDelegatingDeserializer.class) {
/*  96 */       throw new IllegalStateException("Sub-class " + getClass().getName() + " must override 'withDelegate'");
/*     */     }
/*  98 */     return new StdDelegatingDeserializer(converter, delegateType, delegateDeserializer);
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
/*     */   public void resolve(DeserializationContext ctxt)
/*     */     throws JsonMappingException
/*     */   {
/* 113 */     if ((this._delegateDeserializer != null) && ((this._delegateDeserializer instanceof ResolvableDeserializer))) {
/* 114 */       ((ResolvableDeserializer)this._delegateDeserializer).resolve(ctxt);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property)
/*     */     throws JsonMappingException
/*     */   {
/* 123 */     if (this._delegateDeserializer != null) {
/* 124 */       JsonDeserializer<?> deser = ctxt.handleSecondaryContextualization(this._delegateDeserializer, property, this._delegateType);
/*     */       
/* 126 */       if (deser != this._delegateDeserializer) {
/* 127 */         return withDelegate(this._converter, this._delegateType, deser);
/*     */       }
/* 129 */       return this;
/*     */     }
/*     */     
/* 132 */     JavaType delegateType = this._converter.getInputType(ctxt.getTypeFactory());
/* 133 */     return withDelegate(this._converter, delegateType, ctxt.findContextualValueDeserializer(delegateType, property));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JsonDeserializer<?> getDelegatee()
/*     */   {
/* 145 */     return this._delegateDeserializer;
/*     */   }
/*     */   
/*     */   public Class<?> handledType()
/*     */   {
/* 150 */     return this._delegateDeserializer.handledType();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public T deserialize(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 162 */     Object delegateValue = this._delegateDeserializer.deserialize(p, ctxt);
/* 163 */     if (delegateValue == null) {
/* 164 */       return null;
/*     */     }
/* 166 */     return (T)convertValue(delegateValue);
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
/*     */   public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer)
/*     */     throws IOException
/*     */   {
/* 184 */     Object delegateValue = this._delegateDeserializer.deserialize(p, ctxt);
/* 185 */     if (delegateValue == null) {
/* 186 */       return null;
/*     */     }
/* 188 */     return convertValue(delegateValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public T deserialize(JsonParser p, DeserializationContext ctxt, Object intoValue)
/*     */     throws IOException
/*     */   {
/* 196 */     if (this._delegateType.getRawClass().isAssignableFrom(intoValue.getClass())) {
/* 197 */       return (T)this._delegateDeserializer.deserialize(p, ctxt, intoValue);
/*     */     }
/* 199 */     return (T)_handleIncompatibleUpdateValue(p, ctxt, intoValue);
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
/*     */   protected Object _handleIncompatibleUpdateValue(JsonParser p, DeserializationContext ctxt, Object intoValue)
/*     */     throws IOException
/*     */   {
/* 215 */     throw new UnsupportedOperationException(String.format("Can not update object of type %s (using deserializer for type %s)" + intoValue.getClass().getName(), new Object[] { this._delegateType }));
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected T convertValue(Object delegateValue)
/*     */   {
/* 239 */     return (T)this._converter.convert(delegateValue);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\StdDelegatingDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */