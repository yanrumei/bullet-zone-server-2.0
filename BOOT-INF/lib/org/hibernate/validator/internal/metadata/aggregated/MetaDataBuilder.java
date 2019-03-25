/*     */ package org.hibernate.validator.internal.metadata.aggregated;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintOrigin;
/*     */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class MetaDataBuilder
/*     */ {
/*  36 */   private static final Log log = ;
/*     */   
/*     */   protected final ConstraintHelper constraintHelper;
/*     */   
/*     */   private final Class<?> beanClass;
/*  41 */   private final Set<MetaConstraint<?>> constraints = CollectionHelper.newHashSet();
/*  42 */   private final Map<Class<?>, Class<?>> groupConversions = CollectionHelper.newHashMap();
/*  43 */   private boolean isCascading = false;
/*  44 */   private UnwrapMode unwrapMode = UnwrapMode.AUTOMATIC;
/*     */   
/*     */   protected MetaDataBuilder(Class<?> beanClass, ConstraintHelper constraintHelper) {
/*  47 */     this.beanClass = beanClass;
/*  48 */     this.constraintHelper = constraintHelper;
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
/*     */   public abstract boolean accepts(ConstrainedElement paramConstrainedElement);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void add(ConstrainedElement constrainedElement)
/*     */   {
/*  71 */     this.constraints.addAll(constrainedElement.getConstraints());
/*  72 */     this.isCascading = ((this.isCascading) || (constrainedElement.isCascading()));
/*  73 */     this.unwrapMode = constrainedElement.unwrapMode();
/*     */     
/*  75 */     addGroupConversions(constrainedElement.getGroupConversions());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract ConstraintMetaData build();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addGroupConversions(Map<Class<?>, Class<?>> groupConversions)
/*     */   {
/*  88 */     for (Map.Entry<Class<?>, Class<?>> oneConversion : groupConversions.entrySet()) {
/*  89 */       if (this.groupConversions.containsKey(oneConversion.getKey())) {
/*  90 */         throw log.getMultipleGroupConversionsForSameSourceException(
/*  91 */           (Class)oneConversion.getKey(), 
/*  92 */           CollectionHelper.asSet(new Class[] {
/*  93 */           (Class)groupConversions.get(oneConversion.getKey()), 
/*  94 */           (Class)oneConversion.getValue() }));
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*  99 */       this.groupConversions.put(oneConversion.getKey(), oneConversion.getValue());
/*     */     }
/*     */   }
/*     */   
/*     */   protected Map<Class<?>, Class<?>> getGroupConversions()
/*     */   {
/* 105 */     return this.groupConversions;
/*     */   }
/*     */   
/*     */   protected Set<MetaConstraint<?>> getConstraints() {
/* 109 */     return this.constraints;
/*     */   }
/*     */   
/*     */   protected boolean isCascading() {
/* 113 */     return this.isCascading;
/*     */   }
/*     */   
/*     */   protected Class<?> getBeanClass() {
/* 117 */     return this.beanClass;
/*     */   }
/*     */   
/*     */   public UnwrapMode unwrapMode() {
/* 121 */     return this.unwrapMode;
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
/*     */   protected Set<MetaConstraint<?>> adaptOriginsAndImplicitGroups(Set<MetaConstraint<?>> constraints)
/*     */   {
/* 139 */     Set<MetaConstraint<?>> adaptedConstraints = CollectionHelper.newHashSet();
/*     */     
/* 141 */     for (MetaConstraint<?> oneConstraint : constraints) {
/* 142 */       adaptedConstraints.add(adaptOriginAndImplicitGroup(oneConstraint));
/*     */     }
/* 144 */     return adaptedConstraints;
/*     */   }
/*     */   
/*     */   private <A extends Annotation> MetaConstraint<A> adaptOriginAndImplicitGroup(MetaConstraint<A> constraint) {
/* 148 */     ConstraintOrigin definedIn = definedIn(this.beanClass, constraint.getLocation().getDeclaringClass());
/*     */     
/* 150 */     if (definedIn == ConstraintOrigin.DEFINED_LOCALLY) {
/* 151 */       return constraint;
/*     */     }
/*     */     
/* 154 */     Class<?> constraintClass = constraint.getLocation().getDeclaringClass();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 163 */     ConstraintDescriptorImpl<A> descriptor = new ConstraintDescriptorImpl(this.constraintHelper, constraint.getLocation().getMember(), constraint.getDescriptor().getAnnotation(), constraint.getElementType(), constraintClass.isInterface() ? constraintClass : null, definedIn, constraint.getDescriptor().getConstraintType());
/*     */     
/*     */ 
/* 166 */     return new MetaConstraint(descriptor, constraint
/*     */     
/* 168 */       .getLocation());
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
/*     */   private ConstraintOrigin definedIn(Class<?> rootClass, Class<?> hierarchyClass)
/*     */   {
/* 182 */     if (hierarchyClass.equals(rootClass)) {
/* 183 */       return ConstraintOrigin.DEFINED_LOCALLY;
/*     */     }
/*     */     
/* 186 */     return ConstraintOrigin.DEFINED_IN_HIERARCHY;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\aggregated\MetaDataBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */