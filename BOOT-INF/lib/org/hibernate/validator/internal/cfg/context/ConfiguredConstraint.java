/*     */ package org.hibernate.validator.internal.cfg.context;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import org.hibernate.validator.cfg.ConstraintDef;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*     */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
/*     */ import org.hibernate.validator.internal.util.annotationfactory.AnnotationDescriptor;
/*     */ import org.hibernate.validator.internal.util.annotationfactory.AnnotationFactory;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
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
/*     */ class ConfiguredConstraint<A extends Annotation>
/*     */ {
/*  31 */   private static final Log log = ;
/*     */   
/*     */   private final ConstraintDefAccessor<A> constraint;
/*     */   private final ConstraintLocation location;
/*     */   private final ElementType elementType;
/*     */   
/*     */   private ConfiguredConstraint(ConstraintDef<?, A> constraint, ConstraintLocation location, ElementType elementType)
/*     */   {
/*  39 */     this.constraint = new ConstraintDefAccessor(constraint, null);
/*  40 */     this.location = location;
/*  41 */     this.elementType = elementType;
/*     */   }
/*     */   
/*     */   static <A extends Annotation> ConfiguredConstraint<A> forType(ConstraintDef<?, A> constraint, Class<?> beanType) {
/*  45 */     return new ConfiguredConstraint(constraint, ConstraintLocation.forClass(beanType), ElementType.TYPE);
/*     */   }
/*     */   
/*     */   static <A extends Annotation> ConfiguredConstraint<A> forProperty(ConstraintDef<?, A> constraint, Member member) {
/*  49 */     return new ConfiguredConstraint(constraint, 
/*     */     
/*  51 */       ConstraintLocation.forProperty(member), (member instanceof Field) ? ElementType.FIELD : ElementType.METHOD);
/*     */   }
/*     */   
/*     */ 
/*     */   public static <A extends Annotation> ConfiguredConstraint<A> forParameter(ConstraintDef<?, A> constraint, ExecutableElement executable, int parameterIndex)
/*     */   {
/*  57 */     return new ConfiguredConstraint(constraint, 
/*  58 */       ConstraintLocation.forParameter(executable, parameterIndex), executable.getElementType());
/*     */   }
/*     */   
/*     */   public static <A extends Annotation> ConfiguredConstraint<A> forReturnValue(ConstraintDef<?, A> constraint, ExecutableElement executable)
/*     */   {
/*  63 */     return new ConfiguredConstraint(constraint, 
/*  64 */       ConstraintLocation.forReturnValue(executable), executable.getElementType());
/*     */   }
/*     */   
/*     */   public static <A extends Annotation> ConfiguredConstraint<A> forCrossParameter(ConstraintDef<?, A> constraint, ExecutableElement executable)
/*     */   {
/*  69 */     return new ConfiguredConstraint(constraint, 
/*  70 */       ConstraintLocation.forCrossParameter(executable), executable.getElementType());
/*     */   }
/*     */   
/*     */   public ConstraintDef<?, A> getConstraint()
/*     */   {
/*  75 */     return this.constraint;
/*     */   }
/*     */   
/*     */   public ConstraintLocation getLocation() {
/*  79 */     return this.location;
/*     */   }
/*     */   
/*     */   public Class<A> getConstraintType() {
/*  83 */     return this.constraint.getConstraintType();
/*     */   }
/*     */   
/*     */   public Map<String, Object> getParameters() {
/*  87 */     return this.constraint.getParameters();
/*     */   }
/*     */   
/*     */   public A createAnnotationProxy()
/*     */   {
/*  92 */     AnnotationDescriptor<A> annotationDescriptor = new AnnotationDescriptor(getConstraintType());
/*  93 */     for (Map.Entry<String, Object> parameter : getParameters().entrySet()) {
/*  94 */       annotationDescriptor.setValue((String)parameter.getKey(), parameter.getValue());
/*     */     }
/*     */     try
/*     */     {
/*  98 */       return AnnotationFactory.create(annotationDescriptor);
/*     */     }
/*     */     catch (RuntimeException e) {
/* 101 */       throw log.getUnableToCreateAnnotationForConfiguredConstraintException(e);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 107 */     return this.constraint.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class ConstraintDefAccessor<A extends Annotation>
/*     */     extends ConstraintDef<ConstraintDefAccessor<A>, A>
/*     */   {
/*     */     private ConstraintDefAccessor(ConstraintDef<?, A> original)
/*     */     {
/* 117 */       super();
/*     */     }
/*     */     
/*     */     private Class<A> getConstraintType() {
/* 121 */       return this.constraintType;
/*     */     }
/*     */     
/*     */     private Map<String, Object> getParameters() {
/* 125 */       return this.parameters;
/*     */     }
/*     */   }
/*     */   
/*     */   public ElementType getElementType() {
/* 130 */     return this.elementType;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\cfg\context\ConfiguredConstraint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */