/*     */ package com.fasterxml.jackson.databind.deser;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.BeanProperty;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonMappingException;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
/*     */ import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedField;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
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
/*     */ public class SettableAnyProperty
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final BeanProperty _property;
/*     */   protected final AnnotatedMember _setter;
/*     */   final boolean _setterIsField;
/*     */   protected final JavaType _type;
/*     */   protected JsonDeserializer<Object> _valueDeserializer;
/*     */   protected final TypeDeserializer _valueTypeDeserializer;
/*     */   
/*     */   public SettableAnyProperty(BeanProperty property, AnnotatedMember setter, JavaType type, JsonDeserializer<Object> valueDeser, TypeDeserializer typeDeser)
/*     */   {
/*  55 */     this._property = property;
/*  56 */     this._setter = setter;
/*  57 */     this._type = type;
/*  58 */     this._valueDeserializer = valueDeser;
/*  59 */     this._valueTypeDeserializer = typeDeser;
/*  60 */     this._setterIsField = (setter instanceof AnnotatedField);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SettableAnyProperty(SettableAnyProperty src)
/*     */   {
/*  68 */     this._property = src._property;
/*  69 */     this._setter = src._setter;
/*  70 */     this._type = src._type;
/*  71 */     this._valueDeserializer = src._valueDeserializer;
/*  72 */     this._valueTypeDeserializer = src._valueTypeDeserializer;
/*  73 */     this._setterIsField = src._setterIsField;
/*     */   }
/*     */   
/*     */   public SettableAnyProperty withValueDeserializer(JsonDeserializer<Object> deser) {
/*  77 */     return new SettableAnyProperty(this._property, this._setter, this._type, deser, this._valueTypeDeserializer);
/*     */   }
/*     */   
/*     */   public void fixAccess(DeserializationConfig config)
/*     */   {
/*  82 */     this._setter.fixAccess(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
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
/*     */   Object readResolve()
/*     */   {
/*  97 */     if ((this._setter == null) || (this._setter.getAnnotated() == null)) {
/*  98 */       throw new IllegalArgumentException("Missing method (broken JDK (de)serialization?)");
/*     */     }
/* 100 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 109 */   public BeanProperty getProperty() { return this._property; }
/*     */   
/* 111 */   public boolean hasValueDeserializer() { return this._valueDeserializer != null; }
/*     */   
/* 113 */   public JavaType getType() { return this._type; }
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
/*     */   public final void deserializeAndSet(JsonParser p, DeserializationContext ctxt, Object instance, String propName)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 130 */       set(instance, propName, deserialize(p, ctxt));
/*     */     } catch (UnresolvedForwardReference reference) {
/* 132 */       if (this._valueDeserializer.getObjectIdReader() == null) {
/* 133 */         throw JsonMappingException.from(p, "Unresolved forward reference but no identity info.", reference);
/*     */       }
/* 135 */       AnySetterReferring referring = new AnySetterReferring(this, reference, this._type.getRawClass(), instance, propName);
/*     */       
/* 137 */       reference.getRoid().appendReferring(referring);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
/*     */   {
/* 143 */     JsonToken t = p.getCurrentToken();
/* 144 */     if (t == JsonToken.VALUE_NULL) {
/* 145 */       return this._valueDeserializer.getNullValue(ctxt);
/*     */     }
/* 147 */     if (this._valueTypeDeserializer != null) {
/* 148 */       return this._valueDeserializer.deserializeWithType(p, ctxt, this._valueTypeDeserializer);
/*     */     }
/* 150 */     return this._valueDeserializer.deserialize(p, ctxt);
/*     */   }
/*     */   
/*     */   public void set(Object instance, String propName, Object value)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 158 */       if (this._setterIsField) {
/* 159 */         AnnotatedField field = (AnnotatedField)this._setter;
/* 160 */         Map<Object, Object> val = (Map)field.getValue(instance);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 166 */         if (val != null)
/*     */         {
/* 168 */           val.put(propName, value);
/*     */         }
/*     */       }
/*     */       else {
/* 172 */         ((AnnotatedMethod)this._setter).callOnWith(instance, new Object[] { propName, value });
/*     */       }
/*     */     } catch (Exception e) {
/* 175 */       _throwAsIOE(e, propName, value);
/*     */     }
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
/*     */   protected void _throwAsIOE(Exception e, String propName, Object value)
/*     */     throws IOException
/*     */   {
/* 193 */     if ((e instanceof IllegalArgumentException)) {
/* 194 */       String actType = value == null ? "[NULL]" : value.getClass().getName();
/* 195 */       StringBuilder msg = new StringBuilder("Problem deserializing \"any\" property '").append(propName);
/* 196 */       msg.append("' of class " + getClassName() + " (expected type: ").append(this._type);
/* 197 */       msg.append("; actual type: ").append(actType).append(")");
/* 198 */       String origMsg = e.getMessage();
/* 199 */       if (origMsg != null) {
/* 200 */         msg.append(", problem: ").append(origMsg);
/*     */       } else {
/* 202 */         msg.append(" (no error message provided)");
/*     */       }
/* 204 */       throw new JsonMappingException(null, msg.toString(), e);
/*     */     }
/* 206 */     if ((e instanceof IOException)) {
/* 207 */       throw ((IOException)e);
/*     */     }
/* 209 */     if ((e instanceof RuntimeException)) {
/* 210 */       throw ((RuntimeException)e);
/*     */     }
/*     */     
/* 213 */     Throwable t = e;
/* 214 */     while (t.getCause() != null) {
/* 215 */       t = t.getCause();
/*     */     }
/* 217 */     throw new JsonMappingException(null, t.getMessage(), t);
/*     */   }
/*     */   
/* 220 */   private String getClassName() { return this._setter.getDeclaringClass().getName(); }
/*     */   
/* 222 */   public String toString() { return "[any property on class " + getClassName() + "]"; }
/*     */   
/*     */   private static class AnySetterReferring extends ReadableObjectId.Referring
/*     */   {
/*     */     private final SettableAnyProperty _parent;
/*     */     private final Object _pojo;
/*     */     private final String _propName;
/*     */     
/*     */     public AnySetterReferring(SettableAnyProperty parent, UnresolvedForwardReference reference, Class<?> type, Object instance, String propName)
/*     */     {
/* 232 */       super(type);
/* 233 */       this._parent = parent;
/* 234 */       this._pojo = instance;
/* 235 */       this._propName = propName;
/*     */     }
/*     */     
/*     */ 
/*     */     public void handleResolvedForwardReference(Object id, Object value)
/*     */       throws IOException
/*     */     {
/* 242 */       if (!hasId(id)) {
/* 243 */         throw new IllegalArgumentException("Trying to resolve a forward reference with id [" + id.toString() + "] that wasn't previously registered.");
/*     */       }
/*     */       
/* 246 */       this._parent.set(this._pojo, this._propName, value);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\SettableAnyProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */