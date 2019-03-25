/*     */ package com.fasterxml.jackson.databind.introspect;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.DeserializationConfig;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.SerializationConfig;
/*     */ import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder.Value;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.type.SimpleType;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.LRUMap;
/*     */ import java.io.Serializable;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BasicClassIntrospector
/*     */   extends ClassIntrospector
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   protected static final BasicBeanDescription STRING_DESC;
/*     */   protected static final BasicBeanDescription BOOLEAN_DESC;
/*     */   protected static final BasicBeanDescription INT_DESC;
/*     */   protected static final BasicBeanDescription LONG_DESC;
/*     */   
/*     */   static
/*     */   {
/*  33 */     AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(String.class, null);
/*  34 */     STRING_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(String.class), ac);
/*     */     
/*     */ 
/*     */ 
/*  38 */     AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(Boolean.TYPE, null);
/*  39 */     BOOLEAN_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(Boolean.TYPE), ac);
/*     */     
/*     */ 
/*     */ 
/*  43 */     AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(Integer.TYPE, null);
/*  44 */     INT_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(Integer.TYPE), ac);
/*     */     
/*     */ 
/*     */ 
/*  48 */     AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(Long.TYPE, null);
/*  49 */     LONG_DESC = BasicBeanDescription.forOtherUse(null, SimpleType.constructUnsafe(Long.TYPE), ac);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*  59 */   public static final BasicClassIntrospector instance = new BasicClassIntrospector();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final LRUMap<JavaType, BasicBeanDescription> _cachedFCA;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BasicClassIntrospector()
/*     */   {
/*  71 */     this._cachedFCA = new LRUMap(16, 64);
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
/*     */   public BasicBeanDescription forSerialization(SerializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r)
/*     */   {
/*  85 */     BasicBeanDescription desc = _findStdTypeDesc(type);
/*  86 */     if (desc == null)
/*     */     {
/*     */ 
/*  89 */       desc = _findStdJdkCollectionDesc(cfg, type);
/*  90 */       if (desc == null) {
/*  91 */         desc = BasicBeanDescription.forSerialization(collectProperties(cfg, type, r, true, "set"));
/*     */       }
/*     */       
/*     */ 
/*  95 */       this._cachedFCA.putIfAbsent(type, desc);
/*     */     }
/*  97 */     return desc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public BasicBeanDescription forDeserialization(DeserializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r)
/*     */   {
/* 105 */     BasicBeanDescription desc = _findStdTypeDesc(type);
/* 106 */     if (desc == null)
/*     */     {
/*     */ 
/* 109 */       desc = _findStdJdkCollectionDesc(cfg, type);
/* 110 */       if (desc == null) {
/* 111 */         desc = BasicBeanDescription.forDeserialization(collectProperties(cfg, type, r, false, "set"));
/*     */       }
/*     */       
/*     */ 
/* 115 */       this._cachedFCA.putIfAbsent(type, desc);
/*     */     }
/* 117 */     return desc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BasicBeanDescription forDeserializationWithBuilder(DeserializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r)
/*     */   {
/* 126 */     BasicBeanDescription desc = BasicBeanDescription.forDeserialization(collectPropertiesWithBuilder(cfg, type, r, false));
/*     */     
/*     */ 
/* 129 */     this._cachedFCA.putIfAbsent(type, desc);
/* 130 */     return desc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BasicBeanDescription forCreation(DeserializationConfig cfg, JavaType type, ClassIntrospector.MixInResolver r)
/*     */   {
/* 137 */     BasicBeanDescription desc = _findStdTypeDesc(type);
/* 138 */     if (desc == null)
/*     */     {
/*     */ 
/*     */ 
/* 142 */       desc = _findStdJdkCollectionDesc(cfg, type);
/* 143 */       if (desc == null) {
/* 144 */         desc = BasicBeanDescription.forDeserialization(collectProperties(cfg, type, r, false, "set"));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 149 */     return desc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BasicBeanDescription forClassAnnotations(MapperConfig<?> config, JavaType type, ClassIntrospector.MixInResolver r)
/*     */   {
/* 156 */     BasicBeanDescription desc = _findStdTypeDesc(type);
/* 157 */     if (desc == null) {
/* 158 */       desc = (BasicBeanDescription)this._cachedFCA.get(type);
/* 159 */       if (desc == null) {
/* 160 */         AnnotatedClass ac = AnnotatedClass.construct(type, config, r);
/* 161 */         desc = BasicBeanDescription.forOtherUse(config, type, ac);
/* 162 */         this._cachedFCA.put(type, desc);
/*     */       }
/*     */     }
/* 165 */     return desc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BasicBeanDescription forDirectClassAnnotations(MapperConfig<?> config, JavaType type, ClassIntrospector.MixInResolver r)
/*     */   {
/* 172 */     BasicBeanDescription desc = _findStdTypeDesc(type);
/* 173 */     if (desc == null) {
/* 174 */       AnnotatedClass ac = AnnotatedClass.constructWithoutSuperTypes(type.getRawClass(), config, r);
/* 175 */       desc = BasicBeanDescription.forOtherUse(config, type, ac);
/*     */     }
/* 177 */     return desc;
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
/*     */   protected POJOPropertiesCollector collectProperties(MapperConfig<?> config, JavaType type, ClassIntrospector.MixInResolver r, boolean forSerialization, String mutatorPrefix)
/*     */   {
/* 190 */     AnnotatedClass ac = AnnotatedClass.construct(type, config, r);
/* 191 */     return constructPropertyCollector(config, ac, type, forSerialization, mutatorPrefix);
/*     */   }
/*     */   
/*     */ 
/*     */   protected POJOPropertiesCollector collectPropertiesWithBuilder(MapperConfig<?> config, JavaType type, ClassIntrospector.MixInResolver r, boolean forSerialization)
/*     */   {
/* 197 */     boolean useAnnotations = config.isAnnotationProcessingEnabled();
/* 198 */     AnnotationIntrospector ai = useAnnotations ? config.getAnnotationIntrospector() : null;
/* 199 */     AnnotatedClass ac = AnnotatedClass.construct(type, config, r);
/* 200 */     JsonPOJOBuilder.Value builderConfig = ai == null ? null : ai.findPOJOBuilderConfig(ac);
/* 201 */     String mutatorPrefix = builderConfig == null ? "with" : builderConfig.withPrefix;
/* 202 */     return constructPropertyCollector(config, ac, type, forSerialization, mutatorPrefix);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected POJOPropertiesCollector constructPropertyCollector(MapperConfig<?> config, AnnotatedClass ac, JavaType type, boolean forSerialization, String mutatorPrefix)
/*     */   {
/* 212 */     return new POJOPropertiesCollector(config, forSerialization, type, ac, mutatorPrefix);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected BasicBeanDescription _findStdTypeDesc(JavaType type)
/*     */   {
/* 221 */     Class<?> cls = type.getRawClass();
/* 222 */     if (cls.isPrimitive()) {
/* 223 */       if (cls == Boolean.TYPE) {
/* 224 */         return BOOLEAN_DESC;
/*     */       }
/* 226 */       if (cls == Integer.TYPE) {
/* 227 */         return INT_DESC;
/*     */       }
/* 229 */       if (cls == Long.TYPE) {
/* 230 */         return LONG_DESC;
/*     */       }
/*     */     }
/* 233 */     else if (cls == String.class) {
/* 234 */       return STRING_DESC;
/*     */     }
/*     */     
/* 237 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean _isStdJDKCollection(JavaType type)
/*     */   {
/* 247 */     if ((!type.isContainerType()) || (type.isArrayType())) {
/* 248 */       return false;
/*     */     }
/* 250 */     Class<?> raw = type.getRawClass();
/* 251 */     String pkgName = ClassUtil.getPackageName(raw);
/* 252 */     if ((pkgName != null) && (
/* 253 */       (pkgName.startsWith("java.lang")) || (pkgName.startsWith("java.util"))))
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/* 258 */       if ((Collection.class.isAssignableFrom(raw)) || (Map.class.isAssignableFrom(raw)))
/*     */       {
/* 260 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 264 */     return false;
/*     */   }
/*     */   
/*     */   protected BasicBeanDescription _findStdJdkCollectionDesc(MapperConfig<?> cfg, JavaType type)
/*     */   {
/* 269 */     if (_isStdJDKCollection(type)) {
/* 270 */       AnnotatedClass ac = AnnotatedClass.construct(type, cfg);
/* 271 */       return BasicBeanDescription.forOtherUse(cfg, type, ac);
/*     */     }
/* 273 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\introspect\BasicClassIntrospector.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */