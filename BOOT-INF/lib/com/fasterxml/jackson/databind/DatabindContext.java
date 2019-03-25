/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdGenerator;
/*     */ import com.fasterxml.jackson.annotation.ObjectIdResolver;
/*     */ import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.Annotated;
/*     */ import com.fasterxml.jackson.databind.introspect.ObjectIdInfo;
/*     */ import com.fasterxml.jackson.databind.type.TypeFactory;
/*     */ import com.fasterxml.jackson.databind.util.ClassUtil;
/*     */ import com.fasterxml.jackson.databind.util.Converter;
/*     */ import com.fasterxml.jackson.databind.util.Converter.None;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Locale;
/*     */ import java.util.TimeZone;
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
/*     */ public abstract class DatabindContext
/*     */ {
/*     */   public abstract MapperConfig<?> getConfig();
/*     */   
/*     */   public abstract AnnotationIntrospector getAnnotationIntrospector();
/*     */   
/*     */   public abstract boolean isEnabled(MapperFeature paramMapperFeature);
/*     */   
/*     */   public abstract boolean canOverrideAccessModifiers();
/*     */   
/*     */   public abstract Class<?> getActiveView();
/*     */   
/*     */   public abstract Locale getLocale();
/*     */   
/*     */   public abstract TimeZone getTimeZone();
/*     */   
/*     */   public abstract JsonFormat.Value getDefaultPropertyFormat(Class<?> paramClass);
/*     */   
/*     */   public abstract Object getAttribute(Object paramObject);
/*     */   
/*     */   public abstract DatabindContext setAttribute(Object paramObject1, Object paramObject2);
/*     */   
/*     */   public JavaType constructType(Type type)
/*     */   {
/* 137 */     return getTypeFactory().constructType(type);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public JavaType constructSpecializedType(JavaType baseType, Class<?> subclass)
/*     */   {
/* 146 */     if (baseType.getRawClass() == subclass) {
/* 147 */       return baseType;
/*     */     }
/* 149 */     return getConfig().constructSpecializedType(baseType, subclass);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract TypeFactory getTypeFactory();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public ObjectIdGenerator<?> objectIdGeneratorInstance(Annotated annotated, ObjectIdInfo objectIdInfo)
/*     */     throws JsonMappingException
/*     */   {
/* 164 */     Class<?> implClass = objectIdInfo.getGeneratorType();
/* 165 */     MapperConfig<?> config = getConfig();
/* 166 */     HandlerInstantiator hi = config.getHandlerInstantiator();
/* 167 */     ObjectIdGenerator<?> gen = hi == null ? null : hi.objectIdGeneratorInstance(config, annotated, implClass);
/* 168 */     if (gen == null) {
/* 169 */       gen = (ObjectIdGenerator)ClassUtil.createInstance(implClass, config.canOverrideAccessModifiers());
/*     */     }
/*     */     
/* 172 */     return gen.forScope(objectIdInfo.getScope());
/*     */   }
/*     */   
/*     */   public ObjectIdResolver objectIdResolverInstance(Annotated annotated, ObjectIdInfo objectIdInfo)
/*     */   {
/* 177 */     Class<? extends ObjectIdResolver> implClass = objectIdInfo.getResolverType();
/* 178 */     MapperConfig<?> config = getConfig();
/* 179 */     HandlerInstantiator hi = config.getHandlerInstantiator();
/* 180 */     ObjectIdResolver resolver = hi == null ? null : hi.resolverIdGeneratorInstance(config, annotated, implClass);
/* 181 */     if (resolver == null) {
/* 182 */       resolver = (ObjectIdResolver)ClassUtil.createInstance(implClass, config.canOverrideAccessModifiers());
/*     */     }
/*     */     
/* 185 */     return resolver;
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
/*     */   public Converter<Object, Object> converterInstance(Annotated annotated, Object converterDef)
/*     */     throws JsonMappingException
/*     */   {
/* 199 */     if (converterDef == null) {
/* 200 */       return null;
/*     */     }
/* 202 */     if ((converterDef instanceof Converter)) {
/* 203 */       return (Converter)converterDef;
/*     */     }
/* 205 */     if (!(converterDef instanceof Class)) {
/* 206 */       throw new IllegalStateException("AnnotationIntrospector returned Converter definition of type " + converterDef.getClass().getName() + "; expected type Converter or Class<Converter> instead");
/*     */     }
/*     */     
/* 209 */     Class<?> converterClass = (Class)converterDef;
/*     */     
/* 211 */     if ((converterClass == Converter.None.class) || (ClassUtil.isBogusClass(converterClass))) {
/* 212 */       return null;
/*     */     }
/* 214 */     if (!Converter.class.isAssignableFrom(converterClass)) {
/* 215 */       throw new IllegalStateException("AnnotationIntrospector returned Class " + converterClass.getName() + "; expected Class<Converter>");
/*     */     }
/*     */     
/* 218 */     MapperConfig<?> config = getConfig();
/* 219 */     HandlerInstantiator hi = config.getHandlerInstantiator();
/* 220 */     Converter<?, ?> conv = hi == null ? null : hi.converterInstance(config, annotated, converterClass);
/* 221 */     if (conv == null) {
/* 222 */       conv = (Converter)ClassUtil.createInstance(converterClass, config.canOverrideAccessModifiers());
/*     */     }
/*     */     
/* 225 */     return conv;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\DatabindContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */