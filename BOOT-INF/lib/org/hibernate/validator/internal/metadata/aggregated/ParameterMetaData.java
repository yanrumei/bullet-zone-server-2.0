/*     */ package org.hibernate.validator.internal.metadata.aggregated;
/*     */ 
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.validation.ElementKind;
/*     */ import javax.validation.metadata.GroupConversionDescriptor;
/*     */ import javax.validation.metadata.ParameterDescriptor;
/*     */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ParameterDescriptorImpl;
/*     */ import org.hibernate.validator.internal.metadata.facets.Cascadable;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedElement.ConstrainedElementKind;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedParameter;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
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
/*     */ public class ParameterMetaData
/*     */   extends AbstractConstraintMetaData
/*     */   implements Cascadable
/*     */ {
/*     */   private final GroupConversionHelper groupConversionHelper;
/*     */   private final int index;
/*     */   private final Set<MetaConstraint<?>> typeArgumentsConstraints;
/*     */   
/*     */   private ParameterMetaData(int index, String name, Type type, Set<MetaConstraint<?>> constraints, Set<MetaConstraint<?>> typeArgumentsConstraints, boolean isCascading, Map<Class<?>, Class<?>> groupConversions, UnwrapMode unwrapMode)
/*     */   {
/*  56 */     super(name, type, constraints, ElementKind.PARAMETER, isCascading, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  62 */       (!constraints.isEmpty()) || (isCascading) || (!typeArgumentsConstraints.isEmpty()), unwrapMode);
/*     */     
/*     */ 
/*     */ 
/*  66 */     this.index = index;
/*     */     
/*  68 */     this.typeArgumentsConstraints = Collections.unmodifiableSet(typeArgumentsConstraints);
/*  69 */     this.groupConversionHelper = new GroupConversionHelper(groupConversions);
/*  70 */     this.groupConversionHelper.validateGroupConversions(isCascading(), toString());
/*     */   }
/*     */   
/*     */   public int getIndex() {
/*  74 */     return this.index;
/*     */   }
/*     */   
/*     */   public Class<?> convertGroup(Class<?> originalGroup)
/*     */   {
/*  79 */     return this.groupConversionHelper.convertGroup(originalGroup);
/*     */   }
/*     */   
/*     */   public Set<GroupConversionDescriptor> getGroupConversionDescriptors()
/*     */   {
/*  84 */     return this.groupConversionHelper.asDescriptors();
/*     */   }
/*     */   
/*     */   public ElementType getElementType()
/*     */   {
/*  89 */     return ElementType.PARAMETER;
/*     */   }
/*     */   
/*     */   public Set<MetaConstraint<?>> getTypeArgumentsConstraints()
/*     */   {
/*  94 */     return this.typeArgumentsConstraints;
/*     */   }
/*     */   
/*     */   public ParameterDescriptor asDescriptor(boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence)
/*     */   {
/*  99 */     return new ParameterDescriptorImpl(
/* 100 */       getType(), this.index, 
/*     */       
/* 102 */       getName(), 
/* 103 */       asDescriptors(getConstraints()), 
/* 104 */       isCascading(), defaultGroupSequenceRedefined, defaultGroupSequence, 
/*     */       
/*     */ 
/* 107 */       getGroupConversionDescriptors());
/*     */   }
/*     */   
/*     */ 
/*     */   public Object getValue(Object parent)
/*     */   {
/* 113 */     return ((Object[])(Object[])parent)[getIndex()];
/*     */   }
/*     */   
/*     */   public Type getCascadableType()
/*     */   {
/* 118 */     return getType();
/*     */   }
/*     */   
/*     */   public static class Builder extends MetaDataBuilder {
/*     */     private final Type parameterType;
/*     */     private final int parameterIndex;
/*     */     private ConstrainedParameter constrainedParameter;
/* 125 */     private final Set<MetaConstraint<?>> typeArgumentsConstraints = CollectionHelper.newHashSet();
/*     */     
/*     */     public Builder(Class<?> beanClass, ConstrainedParameter constrainedParameter, ConstraintHelper constraintHelper) {
/* 128 */       super(constraintHelper);
/*     */       
/* 130 */       this.parameterType = constrainedParameter.getType();
/* 131 */       this.parameterIndex = constrainedParameter.getIndex();
/*     */       
/* 133 */       add(constrainedParameter);
/*     */     }
/*     */     
/*     */     public boolean accepts(ConstrainedElement constrainedElement)
/*     */     {
/* 138 */       if (constrainedElement.getKind() != ConstrainedElement.ConstrainedElementKind.PARAMETER) {
/* 139 */         return false;
/*     */       }
/*     */       
/* 142 */       return ((ConstrainedParameter)constrainedElement).getIndex() == this.parameterIndex;
/*     */     }
/*     */     
/*     */     public void add(ConstrainedElement constrainedElement)
/*     */     {
/* 147 */       super.add(constrainedElement);
/*     */       
/* 149 */       ConstrainedParameter newConstrainedParameter = (ConstrainedParameter)constrainedElement;
/*     */       
/* 151 */       this.typeArgumentsConstraints.addAll(newConstrainedParameter.getTypeArgumentsConstraints());
/*     */       
/* 153 */       if (this.constrainedParameter == null) {
/* 154 */         this.constrainedParameter = newConstrainedParameter;
/*     */       }
/* 156 */       else if (newConstrainedParameter.getLocation().getDeclaringClass().isAssignableFrom(this.constrainedParameter
/* 157 */         .getLocation().getDeclaringClass()))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 163 */         this.constrainedParameter = newConstrainedParameter;
/*     */       }
/*     */     }
/*     */     
/*     */     public ParameterMetaData build()
/*     */     {
/* 169 */       return new ParameterMetaData(this.parameterIndex, this.constrainedParameter
/*     */       
/* 171 */         .getName(), this.parameterType, 
/*     */         
/* 173 */         adaptOriginsAndImplicitGroups(getConstraints()), this.typeArgumentsConstraints, 
/*     */         
/* 175 */         isCascading(), 
/* 176 */         getGroupConversions(), 
/* 177 */         unwrapMode(), null);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\aggregated\ParameterMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */