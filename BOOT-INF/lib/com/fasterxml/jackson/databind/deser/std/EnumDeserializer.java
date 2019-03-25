/*     */ package com.fasterxml.jackson.databind.deser.std;
/*     */ 
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonToken;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.DeserializationContext;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.MapperFeature;
/*     */ import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
/*     */ import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
/*     */ import com.fasterxml.jackson.databind.deser.ValueInstantiator;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.CompactStringObjectMap;
/*     */ import com.fasterxml.jackson.databind.util.EnumResolver;
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
/*     */ @JacksonStdImpl
/*     */ public class EnumDeserializer
/*     */   extends StdScalarDeserializer<Object>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected Object[] _enumsByIndex;
/*     */   private final Enum<?> _enumDefaultValue;
/*     */   protected final CompactStringObjectMap _lookupByName;
/*     */   protected CompactStringObjectMap _lookupByToString;
/*     */   
/*     */   public EnumDeserializer(EnumResolver byNameResolver)
/*     */   {
/*  47 */     super(byNameResolver.getEnumClass());
/*  48 */     this._lookupByName = byNameResolver.constructLookup();
/*  49 */     this._enumsByIndex = byNameResolver.getRawEnums();
/*  50 */     this._enumDefaultValue = byNameResolver.getDefaultValue();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static JsonDeserializer<?> deserializerForCreator(DeserializationConfig config, Class<?> enumClass, AnnotatedMethod factory)
/*     */   {
/*  59 */     return deserializerForCreator(config, enumClass, factory, null, null);
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
/*     */   public static JsonDeserializer<?> deserializerForCreator(DeserializationConfig config, Class<?> enumClass, AnnotatedMethod factory, ValueInstantiator valueInstantiator, SettableBeanProperty[] creatorProps)
/*     */   {
/*  74 */     if (config.canOverrideAccessModifiers()) {
/*  75 */       ClassUtil.checkAndFixAccess(factory.getMember(), config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*     */     }
/*     */     
/*  78 */     return new FactoryBasedEnumDeserializer(enumClass, factory, factory.getParameterType(0), valueInstantiator, creatorProps);
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
/*     */   public static JsonDeserializer<?> deserializerForNoArgsCreator(DeserializationConfig config, Class<?> enumClass, AnnotatedMethod factory)
/*     */   {
/*  94 */     if (config.canOverrideAccessModifiers()) {
/*  95 */       ClassUtil.checkAndFixAccess(factory.getMember(), config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
/*     */     }
/*     */     
/*  98 */     return new FactoryBasedEnumDeserializer(enumClass, factory);
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
/*     */   public boolean isCachable()
/*     */   {
/* 112 */     return true;
/*     */   }
/*     */   
/*     */   public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException
/*     */   {
/* 117 */     JsonToken curr = p.getCurrentToken();
/*     */     
/*     */ 
/* 120 */     if ((curr == JsonToken.VALUE_STRING) || (curr == JsonToken.FIELD_NAME)) {
/* 121 */       CompactStringObjectMap lookup = ctxt.isEnabled(DeserializationFeature.READ_ENUMS_USING_TO_STRING) ? _getToStringLookup(ctxt) : this._lookupByName;
/*     */       
/* 123 */       String name = p.getText();
/* 124 */       Object result = lookup.find(name);
/* 125 */       if (result == null) {
/* 126 */         return _deserializeAltString(p, ctxt, lookup, name);
/*     */       }
/* 128 */       return result;
/*     */     }
/*     */     
/* 131 */     if (curr == JsonToken.VALUE_NUMBER_INT)
/*     */     {
/* 133 */       int index = p.getIntValue();
/* 134 */       if (ctxt.isEnabled(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS)) {
/* 135 */         return ctxt.handleWeirdNumberValue(_enumClass(), Integer.valueOf(index), "not allowed to deserialize Enum value out of number: disable DeserializationConfig.DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS to allow", new Object[0]);
/*     */       }
/*     */       
/*     */ 
/* 139 */       if ((index >= 0) && (index < this._enumsByIndex.length)) {
/* 140 */         return this._enumsByIndex[index];
/*     */       }
/* 142 */       if ((this._enumDefaultValue != null) && (ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)))
/*     */       {
/* 144 */         return this._enumDefaultValue;
/*     */       }
/* 146 */       if (!ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
/* 147 */         return ctxt.handleWeirdNumberValue(_enumClass(), Integer.valueOf(index), "index value outside legal index range [0..%s]", new Object[] { Integer.valueOf(this._enumsByIndex.length - 1) });
/*     */       }
/*     */       
/*     */ 
/* 151 */       return null;
/*     */     }
/* 153 */     return _deserializeOther(p, ctxt);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Object _deserializeAltString(JsonParser p, DeserializationContext ctxt, CompactStringObjectMap lookup, String name)
/*     */     throws IOException
/*     */   {
/* 165 */     name = name.trim();
/* 166 */     if (name.length() == 0) {
/* 167 */       if (ctxt.isEnabled(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)) {
/* 168 */         return getEmptyValue(ctxt);
/*     */       }
/* 170 */     } else if (!ctxt.isEnabled(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS))
/*     */     {
/* 172 */       char c = name.charAt(0);
/* 173 */       if ((c >= '0') && (c <= '9')) {
/*     */         try {
/* 175 */           int index = Integer.parseInt(name);
/* 176 */           if ((index >= 0) && (index < this._enumsByIndex.length)) {
/* 177 */             return this._enumsByIndex[index];
/*     */           }
/*     */         }
/*     */         catch (NumberFormatException e) {}
/*     */       }
/*     */     }
/*     */     
/* 184 */     if ((this._enumDefaultValue != null) && (ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE)))
/*     */     {
/* 186 */       return this._enumDefaultValue;
/*     */     }
/* 188 */     if (!ctxt.isEnabled(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL)) {
/* 189 */       return ctxt.handleWeirdStringValue(_enumClass(), name, "value not one of declared Enum instance names: %s", new Object[] { lookup.keys() });
/*     */     }
/*     */     
/* 192 */     return null;
/*     */   }
/*     */   
/*     */   protected Object _deserializeOther(JsonParser p, DeserializationContext ctxt)
/*     */     throws IOException
/*     */   {
/* 198 */     if (p.hasToken(JsonToken.START_ARRAY)) {
/* 199 */       return _deserializeFromArray(p, ctxt);
/*     */     }
/* 201 */     return ctxt.handleUnexpectedToken(_enumClass(), p);
/*     */   }
/*     */   
/*     */   protected Class<?> _enumClass() {
/* 205 */     return handledType();
/*     */   }
/*     */   
/*     */   protected CompactStringObjectMap _getToStringLookup(DeserializationContext ctxt)
/*     */   {
/* 210 */     CompactStringObjectMap lookup = this._lookupByToString;
/*     */     
/*     */ 
/* 213 */     if (lookup == null) {
/* 214 */       synchronized (this) {
/* 215 */         lookup = EnumResolver.constructUnsafeUsingToString(_enumClass(), ctxt.getAnnotationIntrospector()).constructLookup();
/*     */       }
/*     */       
/*     */ 
/* 219 */       this._lookupByToString = lookup;
/*     */     }
/* 221 */     return lookup;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\deser\std\EnumDeserializer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */