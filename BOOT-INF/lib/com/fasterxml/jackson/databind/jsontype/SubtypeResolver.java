/*     */ package com.fasterxml.jackson.databind.jsontype;
/*     */ 
/*     */ import com.fasterxml.jackson.databind.AnnotationIntrospector;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import java.util.Collection;
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
/*     */ public abstract class SubtypeResolver
/*     */ {
/*     */   public abstract void registerSubtypes(NamedType... paramVarArgs);
/*     */   
/*     */   public abstract void registerSubtypes(Class<?>... paramVarArgs);
/*     */   
/*     */   public Collection<NamedType> collectAndResolveSubtypesByClass(MapperConfig<?> config, AnnotatedMember property, JavaType baseType)
/*     */   {
/*  53 */     return collectAndResolveSubtypes(property, config, config.getAnnotationIntrospector(), baseType);
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
/*     */   public Collection<NamedType> collectAndResolveSubtypesByClass(MapperConfig<?> config, AnnotatedClass baseType)
/*     */   {
/*  71 */     return collectAndResolveSubtypes(baseType, config, config.getAnnotationIntrospector());
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
/*     */   public Collection<NamedType> collectAndResolveSubtypesByTypeId(MapperConfig<?> config, AnnotatedMember property, JavaType baseType)
/*     */   {
/*  89 */     return collectAndResolveSubtypes(property, config, config.getAnnotationIntrospector(), baseType);
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
/*     */   public Collection<NamedType> collectAndResolveSubtypesByTypeId(MapperConfig<?> config, AnnotatedClass baseType)
/*     */   {
/* 107 */     return collectAndResolveSubtypes(baseType, config, config.getAnnotationIntrospector());
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
/*     */   @Deprecated
/*     */   public Collection<NamedType> collectAndResolveSubtypes(AnnotatedMember property, MapperConfig<?> config, AnnotationIntrospector ai, JavaType baseType)
/*     */   {
/* 125 */     return collectAndResolveSubtypesByClass(config, property, baseType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public Collection<NamedType> collectAndResolveSubtypes(AnnotatedClass baseType, MapperConfig<?> config, AnnotationIntrospector ai)
/*     */   {
/* 137 */     return collectAndResolveSubtypesByClass(config, baseType);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\jsontype\SubtypeResolver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */