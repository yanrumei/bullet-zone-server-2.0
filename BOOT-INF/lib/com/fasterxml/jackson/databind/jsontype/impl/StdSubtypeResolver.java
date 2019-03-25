/*     */ package com.fasterxml.jackson.databind.jsontype.impl;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsontype.NamedType;
/*     */ import com.fasterxml.jackson.databind.jsontype.SubtypeResolver;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class StdSubtypeResolver
/*     */   extends SubtypeResolver
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected LinkedHashSet<NamedType> _registeredSubtypes;
/*     */   
/*     */   public void registerSubtypes(NamedType... types)
/*     */   {
/*  33 */     if (this._registeredSubtypes == null) {
/*  34 */       this._registeredSubtypes = new LinkedHashSet();
/*     */     }
/*  36 */     for (NamedType type : types) {
/*  37 */       this._registeredSubtypes.add(type);
/*     */     }
/*     */   }
/*     */   
/*     */   public void registerSubtypes(Class<?>... classes)
/*     */   {
/*  43 */     NamedType[] types = new NamedType[classes.length];
/*  44 */     int i = 0; for (int len = classes.length; i < len; i++) {
/*  45 */       types[i] = new NamedType(classes[i]);
/*     */     }
/*  47 */     registerSubtypes(types);
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
/*     */   public Collection<NamedType> collectAndResolveSubtypesByClass(MapperConfig<?> config, AnnotatedMember property, JavaType baseType)
/*     */   {
/*  60 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/*     */     
/*  62 */     Class<?> rawBase = baseType == null ? property.getRawType() : baseType.getRawClass();
/*     */     
/*  64 */     HashMap<NamedType, NamedType> collected = new HashMap();
/*     */     
/*  66 */     if (this._registeredSubtypes != null) {
/*  67 */       for (NamedType subtype : this._registeredSubtypes)
/*     */       {
/*  69 */         if (rawBase.isAssignableFrom(subtype.getType())) {
/*  70 */           AnnotatedClass curr = AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), config);
/*  71 */           _collectAndResolve(curr, subtype, config, ai, collected);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  77 */     Collection<NamedType> st = ai.findSubtypes(property);
/*  78 */     if (st != null) {
/*  79 */       for (NamedType nt : st) {
/*  80 */         AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(nt.getType(), config);
/*  81 */         _collectAndResolve(ac, nt, config, ai, collected);
/*     */       }
/*     */     }
/*     */     
/*  85 */     NamedType rootType = new NamedType(rawBase, null);
/*  86 */     AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(rawBase, config);
/*     */     
/*     */ 
/*  89 */     _collectAndResolve(ac, rootType, config, ai, collected);
/*     */     
/*  91 */     return new ArrayList(collected.values());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Collection<NamedType> collectAndResolveSubtypesByClass(MapperConfig<?> config, AnnotatedClass type)
/*     */   {
/*  98 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/*  99 */     HashMap<NamedType, NamedType> subtypes = new HashMap();
/*     */     Class<?> rawBase;
/* 101 */     if (this._registeredSubtypes != null) {
/* 102 */       rawBase = type.getRawType();
/* 103 */       for (NamedType subtype : this._registeredSubtypes)
/*     */       {
/* 105 */         if (rawBase.isAssignableFrom(subtype.getType())) {
/* 106 */           AnnotatedClass curr = AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), config);
/* 107 */           _collectAndResolve(curr, subtype, config, ai, subtypes);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 112 */     NamedType rootType = new NamedType(type.getRawType(), null);
/* 113 */     _collectAndResolve(type, rootType, config, ai, subtypes);
/* 114 */     return new ArrayList(subtypes.values());
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
/*     */   public Collection<NamedType> collectAndResolveSubtypesByTypeId(MapperConfig<?> config, AnnotatedMember property, JavaType baseType)
/*     */   {
/* 127 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 128 */     Class<?> rawBase = baseType == null ? property.getRawType() : baseType.getRawClass();
/*     */     
/*     */ 
/* 131 */     Set<Class<?>> typesHandled = new HashSet();
/* 132 */     Map<String, NamedType> byName = new LinkedHashMap();
/*     */     
/*     */ 
/* 135 */     NamedType rootType = new NamedType(rawBase, null);
/* 136 */     AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(rawBase, config);
/* 137 */     _collectAndResolveByTypeId(ac, rootType, config, typesHandled, byName);
/*     */     
/*     */ 
/* 140 */     Collection<NamedType> st = ai.findSubtypes(property);
/* 141 */     if (st != null) {
/* 142 */       for (NamedType nt : st) {
/* 143 */         ac = AnnotatedClass.constructWithoutSuperTypes(nt.getType(), config);
/* 144 */         _collectAndResolveByTypeId(ac, nt, config, typesHandled, byName);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 149 */     if (this._registeredSubtypes != null) {
/* 150 */       for (NamedType subtype : this._registeredSubtypes)
/*     */       {
/* 152 */         if (rawBase.isAssignableFrom(subtype.getType())) {
/* 153 */           AnnotatedClass curr = AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), config);
/* 154 */           _collectAndResolveByTypeId(curr, subtype, config, typesHandled, byName);
/*     */         }
/*     */       }
/*     */     }
/* 158 */     return _combineNamedAndUnnamed(typesHandled, byName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Collection<NamedType> collectAndResolveSubtypesByTypeId(MapperConfig<?> config, AnnotatedClass type)
/*     */   {
/* 165 */     Set<Class<?>> typesHandled = new HashSet();
/* 166 */     Map<String, NamedType> byName = new LinkedHashMap();
/*     */     
/* 168 */     NamedType rootType = new NamedType(type.getRawType(), null);
/* 169 */     _collectAndResolveByTypeId(type, rootType, config, typesHandled, byName);
/*     */     Class<?> rawBase;
/* 171 */     if (this._registeredSubtypes != null) {
/* 172 */       rawBase = type.getRawType();
/* 173 */       for (NamedType subtype : this._registeredSubtypes)
/*     */       {
/* 175 */         if (rawBase.isAssignableFrom(subtype.getType())) {
/* 176 */           AnnotatedClass curr = AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), config);
/* 177 */           _collectAndResolveByTypeId(curr, subtype, config, typesHandled, byName);
/*     */         }
/*     */       }
/*     */     }
/* 181 */     return _combineNamedAndUnnamed(typesHandled, byName);
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
/*     */   protected void _collectAndResolve(AnnotatedClass annotatedType, NamedType namedType, MapperConfig<?> config, AnnotationIntrospector ai, HashMap<NamedType, NamedType> collectedSubtypes)
/*     */   {
/* 198 */     if (!namedType.hasName()) {
/* 199 */       String name = ai.findTypeName(annotatedType);
/* 200 */       if (name != null) {
/* 201 */         namedType = new NamedType(namedType.getType(), name);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 206 */     if (collectedSubtypes.containsKey(namedType))
/*     */     {
/* 208 */       if (namedType.hasName()) {
/* 209 */         NamedType prev = (NamedType)collectedSubtypes.get(namedType);
/* 210 */         if (!prev.hasName()) {
/* 211 */           collectedSubtypes.put(namedType, namedType);
/*     */         }
/*     */       }
/* 214 */       return;
/*     */     }
/*     */     
/* 217 */     collectedSubtypes.put(namedType, namedType);
/* 218 */     Collection<NamedType> st = ai.findSubtypes(annotatedType);
/* 219 */     if ((st != null) && (!st.isEmpty())) {
/* 220 */       for (NamedType subtype : st) {
/* 221 */         AnnotatedClass subtypeClass = AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), config);
/* 222 */         _collectAndResolve(subtypeClass, subtype, config, ai, collectedSubtypes);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void _collectAndResolveByTypeId(AnnotatedClass annotatedType, NamedType namedType, MapperConfig<?> config, Set<Class<?>> typesHandled, Map<String, NamedType> byName)
/*     */   {
/* 235 */     AnnotationIntrospector ai = config.getAnnotationIntrospector();
/* 236 */     if (!namedType.hasName()) {
/* 237 */       String name = ai.findTypeName(annotatedType);
/* 238 */       if (name != null) {
/* 239 */         namedType = new NamedType(namedType.getType(), name);
/*     */       }
/*     */     }
/* 242 */     if (namedType.hasName()) {
/* 243 */       byName.put(namedType.getName(), namedType);
/*     */     }
/*     */     
/*     */ 
/* 247 */     if (typesHandled.add(namedType.getType())) {
/* 248 */       Collection<NamedType> st = ai.findSubtypes(annotatedType);
/* 249 */       if ((st != null) && (!st.isEmpty())) {
/* 250 */         for (NamedType subtype : st) {
/* 251 */           AnnotatedClass subtypeClass = AnnotatedClass.constructWithoutSuperTypes(subtype.getType(), config);
/* 252 */           _collectAndResolveByTypeId(subtypeClass, subtype, config, typesHandled, byName);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Collection<NamedType> _combineNamedAndUnnamed(Set<Class<?>> typesHandled, Map<String, NamedType> byName)
/*     */   {
/* 265 */     ArrayList<NamedType> result = new ArrayList(byName.values());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 270 */     for (NamedType t : byName.values()) {
/* 271 */       typesHandled.remove(t.getType());
/*     */     }
/* 273 */     for (Class<?> cls : typesHandled) {
/* 274 */       result.add(new NamedType(cls));
/*     */     }
/* 276 */     return result;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsontype\impl\StdSubtypeResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */