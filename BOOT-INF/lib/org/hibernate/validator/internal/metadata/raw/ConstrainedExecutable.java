/*     */ package org.hibernate.validator.internal.metadata.raw;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.validation.metadata.ConstraintDescriptor;
/*     */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*     */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
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
/*     */ 
/*     */ public class ConstrainedExecutable
/*     */   extends AbstractConstrainedElement
/*     */ {
/*  37 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ExecutableElement executable;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final List<ConstrainedParameter> parameterMetaData;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Set<MetaConstraint<?>> typeArgumentsConstraints;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final boolean hasParameterConstraints;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Set<MetaConstraint<?>> crossParameterConstraints;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ConstrainedExecutable(ConfigurationSource source, ConstraintLocation location, Set<MetaConstraint<?>> returnValueConstraints, Map<Class<?>, Class<?>> groupConversions, boolean isCascading, UnwrapMode unwrapMode)
/*     */   {
/*  71 */     this(source, location, 
/*     */     
/*     */ 
/*  74 */       Collections.emptyList(), 
/*  75 */       Collections.emptySet(), returnValueConstraints, 
/*     */       
/*  77 */       Collections.emptySet(), groupConversions, isCascading, unwrapMode);
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
/*     */ 
/*     */ 
/*     */   public ConstrainedExecutable(ConfigurationSource source, ConstraintLocation location, List<ConstrainedParameter> parameterMetaData, Set<MetaConstraint<?>> crossParameterConstraints, Set<MetaConstraint<?>> returnValueConstraints, Set<MetaConstraint<?>> typeArgumentsConstraints, Map<Class<?>, Class<?>> groupConversions, boolean isCascading, UnwrapMode unwrapMode)
/*     */   {
/* 112 */     super(source, 
/*     */     
/* 114 */       (location.getMember() instanceof Constructor) ? ConstrainedElement.ConstrainedElementKind.CONSTRUCTOR : ConstrainedElement.ConstrainedElementKind.METHOD, location, returnValueConstraints, groupConversions, isCascading, unwrapMode);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 124 */     this.executable = ((location.getMember() instanceof Method) ? ExecutableElement.forMethod((Method)location.getMember()) : ExecutableElement.forConstructor((Constructor)location.getMember()));
/*     */     
/* 126 */     if (parameterMetaData.size() != this.executable.getParameterTypes().length) {
/* 127 */       throw log.getInvalidLengthOfParameterMetaDataListException(this.executable
/* 128 */         .getAsString(), this.executable
/* 129 */         .getParameterTypes().length, parameterMetaData
/* 130 */         .size());
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 136 */     this.typeArgumentsConstraints = (typeArgumentsConstraints != null ? Collections.unmodifiableSet(typeArgumentsConstraints) : Collections.emptySet());
/* 137 */     this.crossParameterConstraints = crossParameterConstraints;
/* 138 */     this.parameterMetaData = Collections.unmodifiableList(parameterMetaData);
/* 139 */     this.hasParameterConstraints = ((hasParameterConstraints(parameterMetaData)) || (!crossParameterConstraints.isEmpty()));
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
/*     */   public ConstrainedParameter getParameterMetaData(int parameterIndex)
/*     */   {
/* 154 */     if ((parameterIndex < 0) || (parameterIndex > this.parameterMetaData.size() - 1)) {
/* 155 */       throw log.getInvalidExecutableParameterIndexException(this.executable
/* 156 */         .getAsString(), parameterIndex);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 161 */     return (ConstrainedParameter)this.parameterMetaData.get(parameterIndex);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<ConstrainedParameter> getAllParameterMetaData()
/*     */   {
/* 173 */     return this.parameterMetaData;
/*     */   }
/*     */   
/*     */   public Set<MetaConstraint<?>> getCrossParameterConstraints() {
/* 177 */     return this.crossParameterConstraints;
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
/*     */   public boolean isConstrained()
/*     */   {
/* 192 */     return (super.isConstrained()) || (!this.typeArgumentsConstraints.isEmpty()) || (this.hasParameterConstraints);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean hasParameterConstraints()
/*     */   {
/* 203 */     return this.hasParameterConstraints;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean isGetterMethod()
/*     */   {
/* 213 */     return this.executable.isGetterMethod();
/*     */   }
/*     */   
/*     */   public ExecutableElement getExecutable() {
/* 217 */     return this.executable;
/*     */   }
/*     */   
/*     */   public Set<MetaConstraint<?>> getTypeArgumentsConstraints() {
/* 221 */     return this.typeArgumentsConstraints;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 226 */     return "ConstrainedExecutable [location=" + getLocation() + ", parameterMetaData=" + this.parameterMetaData + ", hasParameterConstraints=" + this.hasParameterConstraints + "]";
/*     */   }
/*     */   
/*     */ 
/*     */   private boolean hasParameterConstraints(List<ConstrainedParameter> parameterMetaData)
/*     */   {
/* 232 */     for (ConstrainedParameter oneParameter : parameterMetaData) {
/* 233 */       if (oneParameter.isConstrained()) {
/* 234 */         return true;
/*     */       }
/*     */     }
/*     */     
/* 238 */     return false;
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
/*     */   public boolean isEquallyParameterConstrained(ConstrainedExecutable other)
/*     */   {
/* 252 */     if (!getDescriptors(this.crossParameterConstraints).equals(getDescriptors(other.crossParameterConstraints))) {
/* 253 */       return false;
/*     */     }
/*     */     
/* 256 */     int i = 0;
/* 257 */     for (ConstrainedParameter parameter : this.parameterMetaData) {
/* 258 */       ConstrainedParameter otherParameter = other.getParameterMetaData(i);
/* 259 */       if ((parameter.isCascading != otherParameter.isCascading) || 
/* 260 */         (!getDescriptors(parameter.getConstraints()).equals(getDescriptors(otherParameter.getConstraints())))) {
/* 261 */         return false;
/*     */       }
/* 263 */       i++;
/*     */     }
/*     */     
/* 266 */     return true;
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
/*     */   public ConstrainedExecutable merge(ConstrainedExecutable other)
/*     */   {
/* 279 */     ConfigurationSource mergedSource = ConfigurationSource.max(this.source, other.source);
/*     */     
/* 281 */     List<ConstrainedParameter> mergedParameterMetaData = CollectionHelper.newArrayList(this.parameterMetaData.size());
/* 282 */     int i = 0;
/* 283 */     for (ConstrainedParameter parameter : this.parameterMetaData) {
/* 284 */       mergedParameterMetaData.add(parameter.merge(other.getParameterMetaData(i)));
/* 285 */       i++;
/*     */     }
/*     */     
/* 288 */     Object mergedCrossParameterConstraints = CollectionHelper.newHashSet(this.crossParameterConstraints);
/* 289 */     ((Set)mergedCrossParameterConstraints).addAll(other.crossParameterConstraints);
/*     */     
/* 291 */     Set<MetaConstraint<?>> mergedReturnValueConstraints = CollectionHelper.newHashSet(this.constraints);
/* 292 */     mergedReturnValueConstraints.addAll(other.constraints);
/*     */     
/* 294 */     Set<MetaConstraint<?>> mergedTypeArgumentsConstraints = CollectionHelper.newHashSet(this.typeArgumentsConstraints);
/* 295 */     mergedTypeArgumentsConstraints.addAll(other.typeArgumentsConstraints);
/*     */     
/* 297 */     Map<Class<?>, Class<?>> mergedGroupConversions = CollectionHelper.newHashMap(this.groupConversions);
/* 298 */     mergedGroupConversions.putAll(other.groupConversions);
/*     */     
/*     */     UnwrapMode mergedUnwrapMode;
/*     */     UnwrapMode mergedUnwrapMode;
/* 302 */     if (this.source.getPriority() > other.source.getPriority()) {
/* 303 */       mergedUnwrapMode = this.unwrapMode;
/*     */     }
/*     */     else {
/* 306 */       mergedUnwrapMode = other.unwrapMode;
/*     */     }
/*     */     
/* 309 */     return new ConstrainedExecutable(mergedSource, 
/*     */     
/* 311 */       getLocation(), mergedParameterMetaData, (Set)mergedCrossParameterConstraints, mergedReturnValueConstraints, mergedTypeArgumentsConstraints, mergedGroupConversions, (this.isCascading) || (other.isCascading), mergedUnwrapMode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Set<ConstraintDescriptor<?>> getDescriptors(Iterable<MetaConstraint<?>> constraints)
/*     */   {
/* 323 */     Set<ConstraintDescriptor<?>> descriptors = CollectionHelper.newHashSet();
/*     */     
/* 325 */     for (MetaConstraint<?> constraint : constraints) {
/* 326 */       descriptors.add(constraint.getDescriptor());
/*     */     }
/*     */     
/* 329 */     return descriptors;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 334 */     int prime = 31;
/* 335 */     int result = super.hashCode();
/*     */     
/* 337 */     result = 31 * result + (this.executable == null ? 0 : this.executable.hashCode());
/* 338 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 343 */     if (this == obj) {
/* 344 */       return true;
/*     */     }
/* 346 */     if (!super.equals(obj)) {
/* 347 */       return false;
/*     */     }
/* 349 */     if (getClass() != obj.getClass()) {
/* 350 */       return false;
/*     */     }
/* 352 */     ConstrainedExecutable other = (ConstrainedExecutable)obj;
/* 353 */     if (this.executable == null) {
/* 354 */       if (other.executable != null) {
/* 355 */         return false;
/*     */       }
/*     */     }
/* 358 */     else if (!this.executable.equals(other.executable)) {
/* 359 */       return false;
/*     */     }
/* 361 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\raw\ConstrainedExecutable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */