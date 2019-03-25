/*     */ package org.hibernate.validator.internal.metadata.aggregated;
/*     */ 
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.validation.ElementKind;
/*     */ import javax.validation.metadata.GroupConversionDescriptor;
/*     */ import javax.validation.metadata.ReturnValueDescriptor;
/*     */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*     */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ReturnValueDescriptorImpl;
/*     */ import org.hibernate.validator.internal.metadata.facets.Cascadable;
/*     */ import org.hibernate.validator.internal.metadata.facets.Validatable;
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
/*     */ public class ReturnValueMetaData
/*     */   extends AbstractConstraintMetaData
/*     */   implements Validatable, Cascadable
/*     */ {
/*  36 */   private static final String RETURN_VALUE_NODE_NAME = null;
/*     */   
/*     */ 
/*     */ 
/*     */   private final List<Cascadable> cascadables;
/*     */   
/*     */ 
/*     */   private final GroupConversionHelper groupConversionHelper;
/*     */   
/*     */ 
/*     */   private final Set<MetaConstraint<?>> typeArgumentsConstraints;
/*     */   
/*     */ 
/*     */ 
/*     */   public ReturnValueMetaData(Type type, Set<MetaConstraint<?>> constraints, Set<MetaConstraint<?>> typeArgumentsConstraints, boolean isCascading, Map<Class<?>, Class<?>> groupConversions, UnwrapMode unwrapMode)
/*     */   {
/*  52 */     super(RETURN_VALUE_NODE_NAME, type, constraints, ElementKind.RETURN_VALUE, isCascading, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  58 */       (!constraints.isEmpty()) || (isCascading) || (!typeArgumentsConstraints.isEmpty()), unwrapMode);
/*     */     
/*     */ 
/*     */ 
/*  62 */     this.typeArgumentsConstraints = Collections.unmodifiableSet(typeArgumentsConstraints);
/*  63 */     this.cascadables = Collections.unmodifiableList(isCascading ? Arrays.asList(new Cascadable[] { this }) : Collections.emptyList());
/*  64 */     this.groupConversionHelper = new GroupConversionHelper(groupConversions);
/*  65 */     this.groupConversionHelper.validateGroupConversions(isCascading(), toString());
/*     */   }
/*     */   
/*     */   public Iterable<Cascadable> getCascadables()
/*     */   {
/*  70 */     return this.cascadables;
/*     */   }
/*     */   
/*     */   public Class<?> convertGroup(Class<?> originalGroup)
/*     */   {
/*  75 */     return this.groupConversionHelper.convertGroup(originalGroup);
/*     */   }
/*     */   
/*     */   public Set<GroupConversionDescriptor> getGroupConversionDescriptors()
/*     */   {
/*  80 */     return this.groupConversionHelper.asDescriptors();
/*     */   }
/*     */   
/*     */   public ElementType getElementType()
/*     */   {
/*  85 */     return ElementType.METHOD;
/*     */   }
/*     */   
/*     */   public Set<MetaConstraint<?>> getTypeArgumentsConstraints()
/*     */   {
/*  90 */     return this.typeArgumentsConstraints;
/*     */   }
/*     */   
/*     */   public ReturnValueDescriptor asDescriptor(boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence)
/*     */   {
/*  95 */     return new ReturnValueDescriptorImpl(
/*  96 */       getType(), 
/*  97 */       asDescriptors(getConstraints()), 
/*  98 */       isCascading(), defaultGroupSequenceRedefined, defaultGroupSequence, this.groupConversionHelper
/*     */       
/*     */ 
/* 101 */       .asDescriptors());
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getValue(Object parent)
/*     */   {
/* 107 */     return parent;
/*     */   }
/*     */   
/*     */   public Type getCascadableType()
/*     */   {
/* 112 */     return getType();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\aggregated\ReturnValueMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */