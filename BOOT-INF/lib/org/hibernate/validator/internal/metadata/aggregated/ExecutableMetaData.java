/*     */ package org.hibernate.validator.internal.metadata.aggregated;
/*     */ 
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Type;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.validation.ElementKind;
/*     */ import javax.validation.metadata.ParameterDescriptor;
/*     */ import org.hibernate.validator.internal.engine.MethodValidationConfiguration;
/*     */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*     */ import org.hibernate.validator.internal.metadata.aggregated.rule.MethodConfigurationRule;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ExecutableDescriptorImpl;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedElement.ConstrainedElementKind;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedParameter;
/*     */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.ExecutableHelper;
/*     */ import org.hibernate.validator.internal.util.ReflectionHelper;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExecutableMetaData
/*     */   extends AbstractConstraintMetaData
/*     */ {
/*  61 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */ 
/*     */   private final Class<?>[] parameterTypes;
/*     */   
/*     */ 
/*     */ 
/*     */   private final List<ParameterMetaData> parameterMetaDataList;
/*     */   
/*     */ 
/*     */ 
/*     */   private final Set<MetaConstraint<?>> crossParameterConstraints;
/*     */   
/*     */ 
/*     */ 
/*     */   private final boolean isGetter;
/*     */   
/*     */ 
/*     */ 
/*     */   private final Set<String> signatures;
/*     */   
/*     */ 
/*     */ 
/*     */   private final ReturnValueMetaData returnValueMetaData;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private ExecutableMetaData(String name, Type returnType, Class<?>[] parameterTypes, ElementKind kind, Set<String> signatures, Set<MetaConstraint<?>> returnValueConstraints, List<ParameterMetaData> parameterMetaData, Set<MetaConstraint<?>> crossParameterConstraints, Set<MetaConstraint<?>> typeArgumentsConstraints, Map<Class<?>, Class<?>> returnValueGroupConversions, boolean isCascading, boolean isConstrained, boolean isGetter, UnwrapMode unwrapMode)
/*     */   {
/*  92 */     super(name, returnType, returnValueConstraints, kind, isCascading, isConstrained, unwrapMode);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 102 */     this.parameterTypes = parameterTypes;
/* 103 */     this.parameterMetaDataList = Collections.unmodifiableList(parameterMetaData);
/* 104 */     this.crossParameterConstraints = Collections.unmodifiableSet(crossParameterConstraints);
/* 105 */     this.signatures = signatures;
/* 106 */     this.returnValueMetaData = new ReturnValueMetaData(returnType, returnValueConstraints, typeArgumentsConstraints, isCascading, returnValueGroupConversions, unwrapMode);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 114 */     this.isGetter = isGetter;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ParameterMetaData getParameterMetaData(int parameterIndex)
/*     */   {
/* 125 */     if ((parameterIndex < 0) || (parameterIndex > this.parameterMetaDataList.size() - 1)) {
/* 126 */       throw log.getInvalidExecutableParameterIndexException(
/* 127 */         ExecutableElement.getExecutableAsString(
/* 128 */         getType().toString() + "#" + getName(), this.parameterTypes), this.parameterTypes.length);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 134 */     return (ParameterMetaData)this.parameterMetaDataList.get(parameterIndex);
/*     */   }
/*     */   
/*     */   public Class<?>[] getParameterTypes() {
/* 138 */     return this.parameterTypes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<String> getSignatures()
/*     */   {
/* 150 */     return this.signatures;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<MetaConstraint<?>> getCrossParameterConstraints()
/*     */   {
/* 162 */     return this.crossParameterConstraints;
/*     */   }
/*     */   
/*     */   public ValidatableParametersMetaData getValidatableParametersMetaData() {
/* 166 */     Set<ParameterMetaData> cascadedParameters = CollectionHelper.newHashSet();
/*     */     
/* 168 */     for (ParameterMetaData parameterMetaData : this.parameterMetaDataList) {
/* 169 */       if (parameterMetaData.isCascading()) {
/* 170 */         cascadedParameters.add(parameterMetaData);
/*     */       }
/*     */     }
/*     */     
/* 174 */     return new ValidatableParametersMetaData(cascadedParameters);
/*     */   }
/*     */   
/*     */   public ReturnValueMetaData getReturnValueMetaData() {
/* 178 */     return this.returnValueMetaData;
/*     */   }
/*     */   
/*     */   public ExecutableDescriptorImpl asDescriptor(boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence)
/*     */   {
/* 183 */     return new ExecutableDescriptorImpl(
/* 184 */       getType(), 
/* 185 */       getName(), 
/* 186 */       asDescriptors(getCrossParameterConstraints()), this.returnValueMetaData
/* 187 */       .asDescriptor(defaultGroupSequenceRedefined, defaultGroupSequence), 
/*     */       
/*     */ 
/*     */ 
/* 191 */       parametersAsDescriptors(defaultGroupSequenceRedefined, defaultGroupSequence), defaultGroupSequenceRedefined, this.isGetter, defaultGroupSequence);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<ParameterDescriptor> parametersAsDescriptors(boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence)
/*     */   {
/* 199 */     List<ParameterDescriptor> parameterDescriptorList = CollectionHelper.newArrayList();
/*     */     
/* 201 */     for (ParameterMetaData parameterMetaData : this.parameterMetaDataList) {
/* 202 */       parameterDescriptorList.add(parameterMetaData
/* 203 */         .asDescriptor(defaultGroupSequenceRedefined, defaultGroupSequence));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 210 */     return parameterDescriptorList;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 215 */     StringBuilder parameterBuilder = new StringBuilder();
/*     */     
/* 217 */     for (Class<?> oneParameterType : getParameterTypes()) {
/* 218 */       parameterBuilder.append(oneParameterType.getSimpleName());
/* 219 */       parameterBuilder.append(", ");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 225 */     String parameters = parameterBuilder.length() > 0 ? parameterBuilder.substring(0, parameterBuilder.length() - 2) : parameterBuilder.toString();
/*     */     
/* 227 */     return "ExecutableMetaData [executable=" + getType() + " " + getName() + "(" + parameters + "), isCascading=" + isCascading() + ", isConstrained=" + 
/* 228 */       isConstrained() + "]";
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 233 */     int prime = 31;
/* 234 */     int result = super.hashCode();
/* 235 */     result = 31 * result + Arrays.hashCode(this.parameterTypes);
/* 236 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 241 */     if (this == obj) {
/* 242 */       return true;
/*     */     }
/* 244 */     if (!super.equals(obj)) {
/* 245 */       return false;
/*     */     }
/* 247 */     if (getClass() != obj.getClass()) {
/* 248 */       return false;
/*     */     }
/* 250 */     ExecutableMetaData other = (ExecutableMetaData)obj;
/* 251 */     if (!Arrays.equals(this.parameterTypes, other.parameterTypes)) {
/* 252 */       return false;
/*     */     }
/* 254 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static class Builder
/*     */     extends MetaDataBuilder
/*     */   {
/* 264 */     private final Set<String> signatures = CollectionHelper.newHashSet();
/*     */     
/*     */ 
/*     */     private final ConstrainedElement.ConstrainedElementKind kind;
/*     */     
/*     */ 
/* 270 */     private final Set<ConstrainedExecutable> constrainedExecutables = CollectionHelper.newHashSet();
/*     */     private ExecutableElement executable;
/* 272 */     private final Set<MetaConstraint<?>> crossParameterConstraints = CollectionHelper.newHashSet();
/* 273 */     private final Set<MetaConstraint<?>> typeArgumentsConstraints = CollectionHelper.newHashSet();
/*     */     private final Set<MethodConfigurationRule> rules;
/* 275 */     private boolean isConstrained = false;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 283 */     private final Map<Class<?>, ConstrainedExecutable> executablesByDeclaringType = CollectionHelper.newHashMap();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private final ExecutableHelper executableHelper;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Builder(Class<?> beanClass, ConstrainedExecutable constrainedExecutable, ConstraintHelper constraintHelper, ExecutableHelper executableHelper, MethodValidationConfiguration methodValidationConfiguration)
/*     */     {
/* 303 */       super(constraintHelper);
/*     */       
/* 305 */       this.executableHelper = executableHelper;
/* 306 */       this.kind = constrainedExecutable.getKind();
/* 307 */       this.executable = constrainedExecutable.getExecutable();
/* 308 */       this.rules = new HashSet(methodValidationConfiguration.getConfiguredRuleSet());
/*     */       
/* 310 */       add(constrainedExecutable);
/*     */     }
/*     */     
/*     */     public boolean accepts(ConstrainedElement constrainedElement)
/*     */     {
/* 315 */       if (this.kind != constrainedElement.getKind()) {
/* 316 */         return false;
/*     */       }
/*     */       
/* 319 */       ExecutableElement executableElement = ((ConstrainedExecutable)constrainedElement).getExecutable();
/*     */       
/*     */ 
/*     */ 
/* 323 */       return (this.executable.equals(executableElement)) || 
/* 324 */         (this.executableHelper.overrides(this.executable, executableElement)) || 
/* 325 */         (this.executableHelper.overrides(executableElement, this.executable));
/*     */     }
/*     */     
/*     */     public final void add(ConstrainedElement constrainedElement)
/*     */     {
/* 330 */       super.add(constrainedElement);
/* 331 */       ConstrainedExecutable constrainedExecutable = (ConstrainedExecutable)constrainedElement;
/*     */       
/* 333 */       this.signatures.add(constrainedExecutable.getExecutable().getSignature());
/*     */       
/* 335 */       this.constrainedExecutables.add(constrainedExecutable);
/* 336 */       this.isConstrained = ((this.isConstrained) || (constrainedExecutable.isConstrained()));
/* 337 */       this.crossParameterConstraints.addAll(constrainedExecutable.getCrossParameterConstraints());
/* 338 */       this.typeArgumentsConstraints.addAll(constrainedExecutable.getTypeArgumentsConstraints());
/*     */       
/* 340 */       addToExecutablesByDeclaringType(constrainedExecutable);
/*     */       
/*     */ 
/*     */ 
/* 344 */       if ((this.executable != null) && (this.executableHelper.overrides(constrainedExecutable
/* 345 */         .getExecutable(), this.executable)))
/*     */       {
/*     */ 
/* 348 */         this.executable = constrainedExecutable.getExecutable();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private void addToExecutablesByDeclaringType(ConstrainedExecutable executable)
/*     */     {
/* 359 */       Class<?> beanClass = executable.getLocation().getDeclaringClass();
/* 360 */       ConstrainedExecutable mergedExecutable = (ConstrainedExecutable)this.executablesByDeclaringType.get(beanClass);
/*     */       
/* 362 */       if (mergedExecutable != null) {
/* 363 */         mergedExecutable = mergedExecutable.merge(executable);
/*     */       }
/*     */       else {
/* 366 */         mergedExecutable = executable;
/*     */       }
/*     */       
/* 369 */       this.executablesByDeclaringType.put(beanClass, mergedExecutable.merge(executable));
/*     */     }
/*     */     
/*     */     public ExecutableMetaData build()
/*     */     {
/* 374 */       assertCorrectnessOfConfiguration();
/*     */       
/* 376 */       return new ExecutableMetaData(this.executable
/* 377 */         .getSimpleName(), 
/* 378 */         ReflectionHelper.typeOf(this.executable.getMember()), this.executable
/* 379 */         .getParameterTypes(), this.kind == ConstrainedElement.ConstrainedElementKind.CONSTRUCTOR ? ElementKind.CONSTRUCTOR : ElementKind.METHOD, this.kind == ConstrainedElement.ConstrainedElementKind.CONSTRUCTOR ? 
/*     */         
/* 381 */         Collections.singleton(this.executable.getSignature()) : this.signatures, 
/* 382 */         adaptOriginsAndImplicitGroups(getConstraints()), 
/* 383 */         findParameterMetaData(), 
/* 384 */         adaptOriginsAndImplicitGroups(this.crossParameterConstraints), this.typeArgumentsConstraints, 
/*     */         
/* 386 */         getGroupConversions(), 
/* 387 */         isCascading(), this.isConstrained, this.executable
/*     */         
/* 389 */         .isGetterMethod(), 
/* 390 */         unwrapMode(), null);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private List<ParameterMetaData> findParameterMetaData()
/*     */     {
/* 402 */       List<ParameterMetaData.Builder> parameterBuilders = null;
/*     */       
/* 404 */       for (Iterator localIterator1 = this.constrainedExecutables.iterator(); localIterator1.hasNext();) { oneExecutable = (ConstrainedExecutable)localIterator1.next();
/* 405 */         Iterator localIterator2; if (parameterBuilders == null) {
/* 406 */           parameterBuilders = CollectionHelper.newArrayList();
/*     */           
/* 408 */           for (localIterator2 = oneExecutable.getAllParameterMetaData().iterator(); localIterator2.hasNext();) { oneParameter = (ConstrainedParameter)localIterator2.next();
/* 409 */             parameterBuilders.add(new ParameterMetaData.Builder(this.executable
/*     */             
/* 411 */               .getMember().getDeclaringClass(), oneParameter, this.constraintHelper));
/*     */           }
/*     */           
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/*     */ 
/* 419 */           i = 0;
/* 420 */           for (ConstrainedParameter oneParameter : oneExecutable.getAllParameterMetaData()) {
/* 421 */             ((ParameterMetaData.Builder)parameterBuilders.get(i)).add(oneParameter);
/* 422 */             i++;
/*     */           } } }
/*     */       ConstrainedExecutable oneExecutable;
/*     */       ConstrainedParameter oneParameter;
/*     */       int i;
/* 427 */       Object parameterMetaDatas = CollectionHelper.newArrayList();
/*     */       
/* 429 */       for (ParameterMetaData.Builder oneBuilder : parameterBuilders) {
/* 430 */         ((List)parameterMetaDatas).add(oneBuilder.build());
/*     */       }
/*     */       
/* 433 */       return (List<ParameterMetaData>)parameterMetaDatas;
/*     */     }
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
/*     */     private void assertCorrectnessOfConfiguration()
/*     */     {
/* 453 */       for (Iterator localIterator1 = this.executablesByDeclaringType.entrySet().iterator(); localIterator1.hasNext();) { entry = (Map.Entry)localIterator1.next();
/* 454 */         for (localIterator2 = this.executablesByDeclaringType.entrySet().iterator(); localIterator2.hasNext();) { otherEntry = (Map.Entry)localIterator2.next();
/* 455 */           for (MethodConfigurationRule rule : this.rules) {
/* 456 */             rule.apply((ConstrainedExecutable)entry.getValue(), (ConstrainedExecutable)otherEntry.getValue());
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */       Map.Entry<Class<?>, ConstrainedExecutable> entry;
/*     */       
/*     */       Iterator localIterator2;
/*     */       Map.Entry<Class<?>, ConstrainedExecutable> otherEntry;
/*     */     }
/*     */     
/*     */     private <T> T run(PrivilegedAction<T> action)
/*     */     {
/* 469 */       return (T)(System.getSecurityManager() != null ? AccessController.doPrivileged(action) : action.run());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\aggregated\ExecutableMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */