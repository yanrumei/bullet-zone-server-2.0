/*     */ package com.fasterxml.jackson.databind.util;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class EnumResolver
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected final Class<Enum<?>> _enumClass;
/*     */   protected final Enum<?>[] _enums;
/*     */   protected final HashMap<String, Enum<?>> _enumsById;
/*     */   protected final Enum<?> _defaultValue;
/*     */   
/*     */   protected EnumResolver(Class<Enum<?>> enumClass, Enum<?>[] enums, HashMap<String, Enum<?>> map, Enum<?> defaultValue)
/*     */   {
/*  26 */     this._enumClass = enumClass;
/*  27 */     this._enums = enums;
/*  28 */     this._enumsById = map;
/*  29 */     this._defaultValue = defaultValue;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EnumResolver constructFor(Class<Enum<?>> enumCls, AnnotationIntrospector ai)
/*     */   {
/*  38 */     Enum<?>[] enumValues = (Enum[])enumCls.getEnumConstants();
/*  39 */     if (enumValues == null) {
/*  40 */       throw new IllegalArgumentException("No enum constants for class " + enumCls.getName());
/*     */     }
/*  42 */     String[] names = ai.findEnumValues(enumCls, enumValues, new String[enumValues.length]);
/*  43 */     HashMap<String, Enum<?>> map = new HashMap();
/*  44 */     int i = 0; for (int len = enumValues.length; i < len; i++) {
/*  45 */       String name = names[i];
/*  46 */       if (name == null) {
/*  47 */         name = enumValues[i].name();
/*     */       }
/*  49 */       map.put(name, enumValues[i]);
/*     */     }
/*     */     
/*  52 */     Enum<?> defaultEnum = ai.findDefaultEnumValue(enumCls);
/*     */     
/*  54 */     return new EnumResolver(enumCls, enumValues, map, defaultEnum);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static EnumResolver constructUsingToString(Class<Enum<?>> enumCls)
/*     */   {
/*  62 */     return constructUsingToString(enumCls, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EnumResolver constructUsingToString(Class<Enum<?>> enumCls, AnnotationIntrospector ai)
/*     */   {
/*  74 */     Enum<?>[] enumValues = (Enum[])enumCls.getEnumConstants();
/*  75 */     HashMap<String, Enum<?>> map = new HashMap();
/*     */     
/*  77 */     int i = enumValues.length; for (;;) { i--; if (i < 0) break;
/*  78 */       Enum<?> e = enumValues[i];
/*  79 */       map.put(e.toString(), e);
/*     */     }
/*  81 */     Enum<?> defaultEnum = ai == null ? null : ai.findDefaultEnumValue(enumCls);
/*  82 */     return new EnumResolver(enumCls, enumValues, map, defaultEnum);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static EnumResolver constructUsingMethod(Class<Enum<?>> enumCls, Method accessor)
/*     */   {
/*  90 */     return constructUsingMethod(enumCls, accessor, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EnumResolver constructUsingMethod(Class<Enum<?>> enumCls, Method accessor, AnnotationIntrospector ai)
/*     */   {
/*  99 */     Enum<?>[] enumValues = (Enum[])enumCls.getEnumConstants();
/* 100 */     HashMap<String, Enum<?>> map = new HashMap();
/*     */     
/* 102 */     int i = enumValues.length; for (;;) { i--; if (i < 0) break;
/* 103 */       Enum<?> en = enumValues[i];
/*     */       try {
/* 105 */         Object o = accessor.invoke(en, new Object[0]);
/* 106 */         if (o != null) {
/* 107 */           map.put(o.toString(), en);
/*     */         }
/*     */       } catch (Exception e) {
/* 110 */         throw new IllegalArgumentException("Failed to access @JsonValue of Enum value " + en + ": " + e.getMessage());
/*     */       }
/*     */     }
/* 113 */     Enum<?> defaultEnum = ai != null ? ai.findDefaultEnumValue(enumCls) : null;
/* 114 */     return new EnumResolver(enumCls, enumValues, map, defaultEnum);
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
/*     */   public static EnumResolver constructUnsafe(Class<?> rawEnumCls, AnnotationIntrospector ai)
/*     */   {
/* 127 */     Class<Enum<?>> enumCls = rawEnumCls;
/* 128 */     return constructFor(enumCls, ai);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static EnumResolver constructUnsafeUsingToString(Class<?> rawEnumCls)
/*     */   {
/* 137 */     return constructUnsafeUsingToString(rawEnumCls, null);
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
/*     */   public static EnumResolver constructUnsafeUsingToString(Class<?> rawEnumCls, AnnotationIntrospector ai)
/*     */   {
/* 151 */     Class<Enum<?>> enumCls = rawEnumCls;
/* 152 */     return constructUsingToString(enumCls, ai);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static EnumResolver constructUnsafeUsingMethod(Class<?> rawEnumCls, Method accessor)
/*     */   {
/* 160 */     return constructUnsafeUsingMethod(rawEnumCls, accessor, null);
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
/*     */   public static EnumResolver constructUnsafeUsingMethod(Class<?> rawEnumCls, Method accessor, AnnotationIntrospector ai)
/*     */   {
/* 174 */     Class<Enum<?>> enumCls = rawEnumCls;
/* 175 */     return constructUsingMethod(enumCls, accessor, ai);
/*     */   }
/*     */   
/*     */   public CompactStringObjectMap constructLookup() {
/* 179 */     return CompactStringObjectMap.construct(this._enumsById);
/*     */   }
/*     */   
/* 182 */   public Enum<?> findEnum(String key) { return (Enum)this._enumsById.get(key); }
/*     */   
/*     */   public Enum<?> getEnum(int index) {
/* 185 */     if ((index < 0) || (index >= this._enums.length)) {
/* 186 */       return null;
/*     */     }
/* 188 */     return this._enums[index];
/*     */   }
/*     */   
/*     */   public Enum<?> getDefaultValue() {
/* 192 */     return this._defaultValue;
/*     */   }
/*     */   
/*     */   public Enum<?>[] getRawEnums() {
/* 196 */     return this._enums;
/*     */   }
/*     */   
/*     */   public List<Enum<?>> getEnums() {
/* 200 */     ArrayList<Enum<?>> enums = new ArrayList(this._enums.length);
/* 201 */     for (Enum<?> e : this._enums) {
/* 202 */       enums.add(e);
/*     */     }
/* 204 */     return enums;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Collection<String> getEnumIds()
/*     */   {
/* 211 */     return this._enumsById.keySet();
/*     */   }
/*     */   
/* 214 */   public Class<Enum<?>> getEnumClass() { return this._enumClass; }
/*     */   
/* 216 */   public int lastValidIndex() { return this._enums.length - 1; }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databin\\util\EnumResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */