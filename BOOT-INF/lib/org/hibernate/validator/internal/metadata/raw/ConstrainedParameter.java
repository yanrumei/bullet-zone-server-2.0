/*     */ package org.hibernate.validator.internal.metadata.raw;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collections;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*     */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
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
/*     */ public class ConstrainedParameter
/*     */   extends AbstractConstrainedElement
/*     */ {
/*     */   private final Type type;
/*     */   private final String name;
/*     */   private final int index;
/*     */   private final Set<MetaConstraint<?>> typeArgumentsConstraints;
/*     */   
/*     */   public ConstrainedParameter(ConfigurationSource source, ConstraintLocation location, Type type, int index, String name)
/*     */   {
/*  39 */     this(source, location, type, index, name, 
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  45 */       Collections.emptySet(), 
/*  46 */       Collections.emptySet(), 
/*  47 */       Collections.emptyMap(), false, UnwrapMode.AUTOMATIC);
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
/*     */   public ConstrainedParameter(ConfigurationSource source, ConstraintLocation location, Type type, int index, String name, Set<MetaConstraint<?>> constraints, Set<MetaConstraint<?>> typeArgumentsConstraints, Map<Class<?>, Class<?>> groupConversions, boolean isCascading, UnwrapMode unwrapMode)
/*     */   {
/*  80 */     super(source, ConstrainedElement.ConstrainedElementKind.PARAMETER, location, constraints, groupConversions, isCascading, unwrapMode);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  90 */     this.type = type;
/*  91 */     this.name = name;
/*  92 */     this.index = index;
/*  93 */     this.typeArgumentsConstraints = (typeArgumentsConstraints != null ? Collections.unmodifiableSet(typeArgumentsConstraints) : 
/*     */     
/*  95 */       Collections.emptySet());
/*     */   }
/*     */   
/*     */   public Type getType() {
/*  99 */     return this.type;
/*     */   }
/*     */   
/*     */   public String getName() {
/* 103 */     return this.name;
/*     */   }
/*     */   
/*     */   public int getIndex() {
/* 107 */     return this.index;
/*     */   }
/*     */   
/*     */   public Set<MetaConstraint<?>> getTypeArgumentsConstraints() {
/* 111 */     return this.typeArgumentsConstraints;
/*     */   }
/*     */   
/*     */   public boolean isConstrained()
/*     */   {
/* 116 */     return (super.isConstrained()) || (!this.typeArgumentsConstraints.isEmpty());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstrainedParameter merge(ConstrainedParameter other)
/*     */   {
/* 128 */     ConfigurationSource mergedSource = ConfigurationSource.max(this.source, other.source);
/*     */     String mergedName;
/*     */     String mergedName;
/* 131 */     if (this.source.getPriority() > other.source.getPriority()) {
/* 132 */       mergedName = this.name;
/*     */     }
/*     */     else {
/* 135 */       mergedName = other.name;
/*     */     }
/*     */     
/*     */     UnwrapMode mergedUnwrapMode;
/*     */     UnwrapMode mergedUnwrapMode;
/* 140 */     if (this.source.getPriority() > other.source.getPriority()) {
/* 141 */       mergedUnwrapMode = this.unwrapMode;
/*     */     }
/*     */     else {
/* 144 */       mergedUnwrapMode = other.unwrapMode;
/*     */     }
/*     */     
/* 147 */     Set<MetaConstraint<?>> mergedConstraints = CollectionHelper.newHashSet(this.constraints);
/* 148 */     mergedConstraints.addAll(other.constraints);
/*     */     
/* 150 */     Set<MetaConstraint<?>> mergedTypeArgumentsConstraints = CollectionHelper.newHashSet(this.typeArgumentsConstraints);
/* 151 */     mergedTypeArgumentsConstraints.addAll(other.typeArgumentsConstraints);
/*     */     
/* 153 */     Map<Class<?>, Class<?>> mergedGroupConversions = CollectionHelper.newHashMap(this.groupConversions);
/* 154 */     mergedGroupConversions.putAll(other.groupConversions);
/*     */     
/* 156 */     return new ConstrainedParameter(mergedSource, 
/*     */     
/* 158 */       getLocation(), this.type, this.index, mergedName, mergedConstraints, mergedTypeArgumentsConstraints, mergedGroupConversions, (this.isCascading) || (other.isCascading), mergedUnwrapMode);
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
/*     */   public String toString()
/*     */   {
/* 173 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 175 */     for (MetaConstraint<?> oneConstraint : getConstraints()) {
/* 176 */       sb.append(oneConstraint.getDescriptor().getAnnotation().annotationType().getSimpleName());
/* 177 */       sb.append(", ");
/*     */     }
/*     */     
/* 180 */     String constraintsAsString = sb.length() > 0 ? sb.substring(0, sb.length() - 2) : sb.toString();
/*     */     
/* 182 */     return "ParameterMetaData [location=" + getLocation() + "], name=" + this.name + "], constraints=[" + constraintsAsString + "], isCascading=" + 
/* 183 */       isCascading() + "]";
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 188 */     int prime = 31;
/* 189 */     int result = super.hashCode();
/* 190 */     result = 31 * result + this.index;
/* 191 */     result = 31 * result + (getLocation().getMember() == null ? 0 : getLocation().getMember().hashCode());
/* 192 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 197 */     if (this == obj) {
/* 198 */       return true;
/*     */     }
/* 200 */     if (!super.equals(obj)) {
/* 201 */       return false;
/*     */     }
/* 203 */     if (getClass() != obj.getClass()) {
/* 204 */       return false;
/*     */     }
/* 206 */     ConstrainedParameter other = (ConstrainedParameter)obj;
/* 207 */     if (this.index != other.index) {
/* 208 */       return false;
/*     */     }
/* 210 */     if (getLocation().getMember() == null) {
/* 211 */       if (other.getLocation().getMember() != null) {
/* 212 */         return false;
/*     */       }
/*     */     }
/* 215 */     else if (!getLocation().getMember().equals(other.getLocation().getMember())) {
/* 216 */       return false;
/*     */     }
/* 218 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\raw\ConstrainedParameter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */