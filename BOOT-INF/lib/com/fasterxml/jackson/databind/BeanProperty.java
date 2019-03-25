/*     */ package com.fasterxml.jackson.databind;
/*     */ 
/*     */ import com.fasterxml.jackson.annotation.JsonFormat.Value;
/*     */ import com.fasterxml.jackson.annotation.JsonInclude.Value;
/*     */ import com.fasterxml.jackson.databind.cfg.MapperConfig;
/*     */ import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
/*     */ import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonObjectFormatVisitor;
/*     */ import com.fasterxml.jackson.databind.util.Annotations;
/*     */ import com.fasterxml.jackson.databind.util.Named;
/*     */ import java.lang.annotation.Annotation;
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
/*     */ public abstract interface BeanProperty
/*     */   extends Named
/*     */ {
/*  34 */   public static final JsonFormat.Value EMPTY_FORMAT = new JsonFormat.Value();
/*  35 */   public static final JsonInclude.Value EMPTY_INCLUDE = JsonInclude.Value.empty();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract String getName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract PropertyName getFullName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JavaType getType();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract PropertyName getWrapperName();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract PropertyMetadata getMetadata();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean isRequired();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract boolean isVirtual();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract <A extends Annotation> A getAnnotation(Class<A> paramClass);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract <A extends Annotation> A getContextAnnotation(Class<A> paramClass);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract AnnotatedMember getMember();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public abstract JsonFormat.Value findFormatOverrides(AnnotationIntrospector paramAnnotationIntrospector);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JsonFormat.Value findPropertyFormat(MapperConfig<?> paramMapperConfig, Class<?> paramClass);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract JsonInclude.Value findPropertyInclusion(MapperConfig<?> paramMapperConfig, Class<?> paramClass);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void depositSchemaProperty(JsonObjectFormatVisitor paramJsonObjectFormatVisitor, SerializerProvider paramSerializerProvider)
/*     */     throws JsonMappingException;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Std
/*     */     implements BeanProperty
/*     */   {
/*     */     protected final PropertyName _name;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final JavaType _type;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final PropertyName _wrapperName;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final PropertyMetadata _metadata;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final AnnotatedMember _member;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     protected final Annotations _contextAnnotations;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Std(PropertyName name, JavaType type, PropertyName wrapperName, Annotations contextAnnotations, AnnotatedMember member, PropertyMetadata metadata)
/*     */     {
/* 224 */       this._name = name;
/* 225 */       this._type = type;
/* 226 */       this._wrapperName = wrapperName;
/* 227 */       this._metadata = metadata;
/* 228 */       this._member = member;
/* 229 */       this._contextAnnotations = contextAnnotations;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public Std(Std base, JavaType newType)
/*     */     {
/* 236 */       this(base._name, newType, base._wrapperName, base._contextAnnotations, base._member, base._metadata);
/*     */     }
/*     */     
/*     */     public Std withType(JavaType type) {
/* 240 */       return new Std(this, type);
/*     */     }
/*     */     
/*     */     public <A extends Annotation> A getAnnotation(Class<A> acls)
/*     */     {
/* 245 */       return this._member == null ? null : this._member.getAnnotation(acls);
/*     */     }
/*     */     
/*     */     public <A extends Annotation> A getContextAnnotation(Class<A> acls)
/*     */     {
/* 250 */       return this._contextAnnotations == null ? null : this._contextAnnotations.get(acls);
/*     */     }
/*     */     
/*     */     @Deprecated
/*     */     public JsonFormat.Value findFormatOverrides(AnnotationIntrospector intr)
/*     */     {
/* 256 */       if ((this._member != null) && (intr != null)) {
/* 257 */         JsonFormat.Value v = intr.findFormat(this._member);
/* 258 */         if (v != null) {
/* 259 */           return v;
/*     */         }
/*     */       }
/* 262 */       return EMPTY_FORMAT;
/*     */     }
/*     */     
/*     */     public JsonFormat.Value findPropertyFormat(MapperConfig<?> config, Class<?> baseType)
/*     */     {
/* 267 */       JsonFormat.Value v0 = config.getDefaultPropertyFormat(baseType);
/* 268 */       AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 269 */       if ((intr == null) || (this._member == null)) {
/* 270 */         return v0;
/*     */       }
/* 272 */       JsonFormat.Value v = intr.findFormat(this._member);
/* 273 */       if (v == null) {
/* 274 */         return v0;
/*     */       }
/* 276 */       return v0.withOverrides(v);
/*     */     }
/*     */     
/*     */ 
/*     */     public JsonInclude.Value findPropertyInclusion(MapperConfig<?> config, Class<?> baseType)
/*     */     {
/* 282 */       JsonInclude.Value v0 = config.getDefaultPropertyInclusion(baseType);
/* 283 */       AnnotationIntrospector intr = config.getAnnotationIntrospector();
/* 284 */       if ((intr == null) || (this._member == null)) {
/* 285 */         return v0;
/*     */       }
/* 287 */       JsonInclude.Value v = intr.findPropertyInclusion(this._member);
/* 288 */       if (v == null) {
/* 289 */         return v0;
/*     */       }
/* 291 */       return v0.withOverrides(v);
/*     */     }
/*     */     
/* 294 */     public String getName() { return this._name.getSimpleName(); }
/* 295 */     public PropertyName getFullName() { return this._name; }
/* 296 */     public JavaType getType() { return this._type; }
/* 297 */     public PropertyName getWrapperName() { return this._wrapperName; }
/* 298 */     public boolean isRequired() { return this._metadata.isRequired(); }
/* 299 */     public PropertyMetadata getMetadata() { return this._metadata; }
/* 300 */     public AnnotatedMember getMember() { return this._member; }
/*     */     
/*     */     public boolean isVirtual() {
/* 303 */       return false;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void depositSchemaProperty(JsonObjectFormatVisitor objectVisitor, SerializerProvider provider)
/*     */     {
/* 314 */       throw new UnsupportedOperationException("Instances of " + getClass().getName() + " should not get visited");
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\jackson-databind-2.8.10.jar!\com\fasterxml\jackson\databind\BeanProperty.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */