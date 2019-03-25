/*     */ package org.hibernate.validator.internal.metadata.aggregated;
/*     */ 
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.validation.ElementKind;
/*     */ import javax.validation.groups.Default;
/*     */ import javax.validation.metadata.BeanDescriptor;
/*     */ import javax.validation.metadata.ConstructorDescriptor;
/*     */ import javax.validation.metadata.MethodType;
/*     */ import javax.validation.metadata.PropertyDescriptor;
/*     */ import org.hibernate.validator.internal.engine.MethodValidationConfiguration;
/*     */ import org.hibernate.validator.internal.engine.groups.Sequence;
/*     */ import org.hibernate.validator.internal.engine.groups.ValidationOrder;
/*     */ import org.hibernate.validator.internal.engine.groups.ValidationOrderGenerator;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintHelper;
/*     */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.BeanDescriptorImpl;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ExecutableDescriptorImpl;
/*     */ import org.hibernate.validator.internal.metadata.facets.Cascadable;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
/*     */ import org.hibernate.validator.internal.metadata.raw.BeanConfiguration;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConfigurationSource;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedElement;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedElement.ConstrainedElementKind;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedExecutable;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedField;
/*     */ import org.hibernate.validator.internal.metadata.raw.ConstrainedType;
/*     */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper.Partitioner;
/*     */ import org.hibernate.validator.internal.util.ExecutableHelper;
/*     */ import org.hibernate.validator.internal.util.classhierarchy.ClassHierarchyHelper;
/*     */ import org.hibernate.validator.internal.util.classhierarchy.Filter;
/*     */ import org.hibernate.validator.internal.util.classhierarchy.Filters;
/*     */ import org.hibernate.validator.internal.util.logging.Log;
/*     */ import org.hibernate.validator.internal.util.logging.LoggerFactory;
/*     */ import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class BeanMetaDataImpl<T>
/*     */   implements BeanMetaData<T>
/*     */ {
/*  68 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  73 */   private static final List<Class<?>> DEFAULT_GROUP_SEQUENCE = Collections.singletonList(Default.class);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ValidationOrderGenerator validationOrderGenerator;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Class<T> beanClass;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Set<MetaConstraint<?>> allMetaConstraints;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Set<MetaConstraint<?>> directMetaConstraints;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Map<String, ExecutableMetaData> executableMetaDataMap;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Map<String, PropertyMetaData> propertyMetaDataMap;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final Set<Cascadable> cascadedProperties;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final BeanDescriptor beanDescriptor;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final List<Class<?>> defaultGroupSequence;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final DefaultGroupSequenceProvider<? super T> defaultGroupSequenceProvider;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final ValidationOrder validationOrder;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final List<Class<? super T>> classHierarchyWithoutInterfaces;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanMetaDataImpl(Class<T> beanClass, List<Class<?>> defaultGroupSequence, DefaultGroupSequenceProvider<? super T> defaultGroupSequenceProvider, Set<ConstraintMetaData> constraintMetaDataSet, ValidationOrderGenerator validationOrderGenerator)
/*     */   {
/* 153 */     this.validationOrderGenerator = validationOrderGenerator;
/* 154 */     this.beanClass = beanClass;
/* 155 */     this.propertyMetaDataMap = CollectionHelper.newHashMap();
/*     */     
/* 157 */     Set<PropertyMetaData> propertyMetaDataSet = CollectionHelper.newHashSet();
/* 158 */     Set<ExecutableMetaData> executableMetaDataSet = CollectionHelper.newHashSet();
/*     */     
/* 160 */     for (ConstraintMetaData constraintMetaData : constraintMetaDataSet) {
/* 161 */       if (constraintMetaData.getKind() == ElementKind.PROPERTY) {
/* 162 */         propertyMetaDataSet.add((PropertyMetaData)constraintMetaData);
/*     */       }
/*     */       else {
/* 165 */         executableMetaDataSet.add((ExecutableMetaData)constraintMetaData);
/*     */       }
/*     */     }
/*     */     
/* 169 */     Object cascadedProperties = CollectionHelper.newHashSet();
/* 170 */     Set<MetaConstraint<?>> allMetaConstraints = CollectionHelper.newHashSet();
/*     */     
/* 172 */     for (PropertyMetaData propertyMetaData : propertyMetaDataSet) {
/* 173 */       this.propertyMetaDataMap.put(propertyMetaData.getName(), propertyMetaData);
/*     */       
/* 175 */       if (propertyMetaData.isCascading()) {
/* 176 */         ((Set)cascadedProperties).add(propertyMetaData);
/*     */       }
/*     */       else {
/* 179 */         allMetaConstraints.addAll(propertyMetaData.getTypeArgumentsConstraints());
/*     */       }
/*     */       
/* 182 */       allMetaConstraints.addAll(propertyMetaData.getConstraints());
/*     */     }
/*     */     
/* 185 */     this.cascadedProperties = Collections.unmodifiableSet((Set)cascadedProperties);
/* 186 */     this.allMetaConstraints = Collections.unmodifiableSet(allMetaConstraints);
/*     */     
/* 188 */     this.classHierarchyWithoutInterfaces = ClassHierarchyHelper.getHierarchy(beanClass, new Filter[] {
/*     */     
/* 190 */       Filters.excludeInterfaces() });
/*     */     
/*     */ 
/* 193 */     Object defaultGroupContext = getDefaultGroupSequenceData(beanClass, defaultGroupSequence, defaultGroupSequenceProvider, validationOrderGenerator);
/* 194 */     this.defaultGroupSequenceProvider = ((DefaultGroupSequenceContext)defaultGroupContext).defaultGroupSequenceProvider;
/* 195 */     this.defaultGroupSequence = Collections.unmodifiableList(((DefaultGroupSequenceContext)defaultGroupContext).defaultGroupSequence);
/* 196 */     this.validationOrder = ((DefaultGroupSequenceContext)defaultGroupContext).validationOrder;
/*     */     
/* 198 */     this.directMetaConstraints = getDirectConstraints();
/*     */     
/* 200 */     this.executableMetaDataMap = Collections.unmodifiableMap(bySignature(executableMetaDataSet));
/*     */     
/* 202 */     boolean defaultGroupSequenceIsRedefined = defaultGroupSequenceIsRedefined();
/* 203 */     List<Class<?>> resolvedDefaultGroupSequence = getDefaultGroupSequence(null);
/*     */     
/* 205 */     Map<String, PropertyDescriptor> propertyDescriptors = getConstrainedPropertiesAsDescriptors(this.propertyMetaDataMap, defaultGroupSequenceIsRedefined, resolvedDefaultGroupSequence);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 211 */     Map<String, ExecutableDescriptorImpl> methodsDescriptors = getConstrainedMethodsAsDescriptors(this.executableMetaDataMap, defaultGroupSequenceIsRedefined, resolvedDefaultGroupSequence);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 217 */     Map<String, ConstructorDescriptor> constructorsDescriptors = getConstrainedConstructorsAsDescriptors(this.executableMetaDataMap, defaultGroupSequenceIsRedefined, resolvedDefaultGroupSequence);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 225 */     this.beanDescriptor = new BeanDescriptorImpl(beanClass, getClassLevelConstraintsAsDescriptors(), propertyDescriptors, methodsDescriptors, constructorsDescriptors, defaultGroupSequenceIsRedefined, resolvedDefaultGroupSequence);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Class<T> getBeanClass()
/*     */   {
/* 236 */     return this.beanClass;
/*     */   }
/*     */   
/*     */   public boolean hasConstraints()
/*     */   {
/* 241 */     if ((!this.beanDescriptor.isBeanConstrained()) && 
/* 242 */       (this.beanDescriptor.getConstrainedConstructors().isEmpty())) {
/* 243 */       if (this.beanDescriptor.getConstrainedMethods(MethodType.NON_GETTER, new MethodType[] { MethodType.GETTER }).isEmpty()) {}
/* 244 */     } else { return true;
/*     */     }
/*     */     
/* 247 */     return false;
/*     */   }
/*     */   
/*     */   public BeanDescriptor getBeanDescriptor()
/*     */   {
/* 252 */     return this.beanDescriptor;
/*     */   }
/*     */   
/*     */   public Set<Cascadable> getCascadables()
/*     */   {
/* 257 */     return this.cascadedProperties;
/*     */   }
/*     */   
/*     */   public PropertyMetaData getMetaDataFor(String propertyName)
/*     */   {
/* 262 */     return (PropertyMetaData)this.propertyMetaDataMap.get(propertyName);
/*     */   }
/*     */   
/*     */   public Set<MetaConstraint<?>> getMetaConstraints()
/*     */   {
/* 267 */     return this.allMetaConstraints;
/*     */   }
/*     */   
/*     */   public Set<MetaConstraint<?>> getDirectMetaConstraints()
/*     */   {
/* 272 */     return this.directMetaConstraints;
/*     */   }
/*     */   
/*     */   public ExecutableMetaData getMetaDataFor(ExecutableElement executable)
/*     */   {
/* 277 */     return (ExecutableMetaData)this.executableMetaDataMap.get(executable.getSignature());
/*     */   }
/*     */   
/*     */   public List<Class<?>> getDefaultGroupSequence(T beanState)
/*     */   {
/* 282 */     if (hasDefaultGroupSequenceProvider()) {
/* 283 */       List<Class<?>> providerDefaultGroupSequence = this.defaultGroupSequenceProvider.getValidationGroups(beanState);
/* 284 */       return getValidDefaultGroupSequence(this.beanClass, providerDefaultGroupSequence);
/*     */     }
/*     */     
/* 287 */     return this.defaultGroupSequence;
/*     */   }
/*     */   
/*     */   public Iterator<Sequence> getDefaultValidationSequence(T beanState)
/*     */   {
/* 292 */     if (hasDefaultGroupSequenceProvider()) {
/* 293 */       List<Class<?>> providerDefaultGroupSequence = this.defaultGroupSequenceProvider.getValidationGroups(beanState);
/* 294 */       return this.validationOrderGenerator.getDefaultValidationOrder(this.beanClass, 
/*     */       
/* 296 */         getValidDefaultGroupSequence(this.beanClass, providerDefaultGroupSequence))
/*     */         
/* 298 */         .getSequenceIterator();
/*     */     }
/*     */     
/* 301 */     return this.validationOrder.getSequenceIterator();
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean defaultGroupSequenceIsRedefined()
/*     */   {
/* 307 */     return (this.defaultGroupSequence.size() > 1) || (hasDefaultGroupSequenceProvider());
/*     */   }
/*     */   
/*     */   public List<Class<? super T>> getClassHierarchy()
/*     */   {
/* 312 */     return this.classHierarchyWithoutInterfaces;
/*     */   }
/*     */   
/*     */   private Set<ConstraintDescriptorImpl<?>> getClassLevelConstraintsAsDescriptors() {
/* 316 */     Set<MetaConstraint<?>> classLevelConstraints = getClassLevelConstraints(this.allMetaConstraints);
/*     */     
/* 318 */     Set<ConstraintDescriptorImpl<?>> theValue = CollectionHelper.newHashSet();
/*     */     
/* 320 */     for (MetaConstraint<?> metaConstraint : classLevelConstraints) {
/* 321 */       theValue.add(metaConstraint.getDescriptor());
/*     */     }
/*     */     
/* 324 */     return theValue;
/*     */   }
/*     */   
/*     */   private static Map<String, PropertyDescriptor> getConstrainedPropertiesAsDescriptors(Map<String, PropertyMetaData> propertyMetaDataMap, boolean defaultGroupSequenceIsRedefined, List<Class<?>> resolvedDefaultGroupSequence)
/*     */   {
/* 329 */     Map<String, PropertyDescriptor> theValue = CollectionHelper.newHashMap();
/*     */     
/* 331 */     for (Map.Entry<String, PropertyMetaData> entry : propertyMetaDataMap.entrySet()) {
/* 332 */       if ((((PropertyMetaData)entry.getValue()).isConstrained()) && (((PropertyMetaData)entry.getValue()).getName() != null)) {
/* 333 */         theValue.put(entry
/* 334 */           .getKey(), 
/* 335 */           ((PropertyMetaData)entry.getValue()).asDescriptor(defaultGroupSequenceIsRedefined, resolvedDefaultGroupSequence));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 343 */     return theValue;
/*     */   }
/*     */   
/*     */   private static Map<String, ExecutableDescriptorImpl> getConstrainedMethodsAsDescriptors(Map<String, ExecutableMetaData> executableMetaDataMap, boolean defaultGroupSequenceIsRedefined, List<Class<?>> resolvedDefaultGroupSequence)
/*     */   {
/* 348 */     Map<String, ExecutableDescriptorImpl> constrainedMethodDescriptors = CollectionHelper.newHashMap();
/*     */     
/* 350 */     for (ExecutableMetaData executableMetaData : executableMetaDataMap.values()) {
/* 351 */       if ((executableMetaData.getKind() == ElementKind.METHOD) && 
/* 352 */         (executableMetaData.isConstrained())) {
/* 353 */         descriptor = executableMetaData.asDescriptor(defaultGroupSequenceIsRedefined, resolvedDefaultGroupSequence);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 358 */         for (String signature : executableMetaData.getSignatures()) {
/* 359 */           constrainedMethodDescriptors.put(signature, descriptor);
/*     */         }
/*     */       }
/*     */     }
/*     */     ExecutableDescriptorImpl descriptor;
/* 364 */     return constrainedMethodDescriptors;
/*     */   }
/*     */   
/*     */   private static Map<String, ConstructorDescriptor> getConstrainedConstructorsAsDescriptors(Map<String, ExecutableMetaData> executableMetaDataMap, boolean defaultGroupSequenceIsRedefined, List<Class<?>> resolvedDefaultGroupSequence)
/*     */   {
/* 369 */     Map<String, ConstructorDescriptor> constrainedMethodDescriptors = CollectionHelper.newHashMap();
/*     */     
/* 371 */     for (ExecutableMetaData executableMetaData : executableMetaDataMap.values()) {
/* 372 */       if ((executableMetaData.getKind() == ElementKind.CONSTRUCTOR) && (executableMetaData.isConstrained())) {
/* 373 */         constrainedMethodDescriptors.put(executableMetaData
/*     */         
/* 375 */           .getSignatures().iterator().next(), executableMetaData
/* 376 */           .asDescriptor(defaultGroupSequenceIsRedefined, resolvedDefaultGroupSequence));
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 384 */     return constrainedMethodDescriptors;
/*     */   }
/*     */   
/*     */   private static <T> DefaultGroupSequenceContext<T> getDefaultGroupSequenceData(Class<?> beanClass, List<Class<?>> defaultGroupSequence, DefaultGroupSequenceProvider<? super T> defaultGroupSequenceProvider, ValidationOrderGenerator validationOrderGenerator) {
/* 388 */     if ((defaultGroupSequence != null) && (defaultGroupSequenceProvider != null)) {
/* 389 */       throw log.getInvalidDefaultGroupSequenceDefinitionException();
/*     */     }
/*     */     
/* 392 */     DefaultGroupSequenceContext<T> context = new DefaultGroupSequenceContext(null);
/*     */     
/* 394 */     if (defaultGroupSequenceProvider != null) {
/* 395 */       context.defaultGroupSequenceProvider = defaultGroupSequenceProvider;
/* 396 */       context.defaultGroupSequence = Collections.emptyList();
/* 397 */       context.validationOrder = null;
/*     */     }
/* 399 */     else if ((defaultGroupSequence != null) && (!defaultGroupSequence.isEmpty())) {
/* 400 */       context.defaultGroupSequence = getValidDefaultGroupSequence(beanClass, defaultGroupSequence);
/* 401 */       context.validationOrder = validationOrderGenerator.getDefaultValidationOrder(beanClass, context.defaultGroupSequence);
/*     */     }
/*     */     else {
/* 404 */       context.defaultGroupSequence = DEFAULT_GROUP_SEQUENCE;
/* 405 */       context.validationOrder = ValidationOrder.DEFAULT_SEQUENCE;
/*     */     }
/*     */     
/* 408 */     return context;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Set<MetaConstraint<?>> getClassLevelConstraints(Set<MetaConstraint<?>> constraints)
/*     */   {
/* 415 */     Set<MetaConstraint<?>> classLevelConstraints = (Set)CollectionHelper.partition(constraints, byElementType()).get(ElementType.TYPE);
/*     */     
/* 417 */     return classLevelConstraints != null ? classLevelConstraints : Collections.emptySet();
/*     */   }
/*     */   
/*     */   private Set<MetaConstraint<?>> getDirectConstraints() {
/* 421 */     Set<MetaConstraint<?>> constraints = CollectionHelper.newHashSet();
/*     */     
/* 423 */     Set<Class<?>> classAndInterfaces = CollectionHelper.newHashSet();
/* 424 */     classAndInterfaces.add(this.beanClass);
/* 425 */     classAndInterfaces.addAll(ClassHierarchyHelper.getDirectlyImplementedInterfaces(this.beanClass));
/*     */     
/* 427 */     for (Iterator localIterator1 = classAndInterfaces.iterator(); localIterator1.hasNext();) { clazz = (Class)localIterator1.next();
/* 428 */       for (MetaConstraint<?> metaConstraint : this.allMetaConstraints) {
/* 429 */         if (metaConstraint.getLocation().getDeclaringClass().equals(clazz)) {
/* 430 */           constraints.add(metaConstraint);
/*     */         }
/*     */       }
/*     */     }
/*     */     Class<?> clazz;
/* 435 */     return Collections.unmodifiableSet(constraints);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Map<String, ExecutableMetaData> bySignature(Set<ExecutableMetaData> executables)
/*     */   {
/* 443 */     Map<String, ExecutableMetaData> theValue = CollectionHelper.newHashMap();
/*     */     
/* 445 */     for (Iterator localIterator1 = executables.iterator(); localIterator1.hasNext();) { executableMetaData = (ExecutableMetaData)localIterator1.next();
/* 446 */       for (String signature : executableMetaData.getSignatures()) {
/* 447 */         theValue.put(signature, executableMetaData);
/*     */       }
/*     */     }
/*     */     ExecutableMetaData executableMetaData;
/* 451 */     return theValue;
/*     */   }
/*     */   
/*     */   private static List<Class<?>> getValidDefaultGroupSequence(Class<?> beanClass, List<Class<?>> groupSequence) {
/* 455 */     List<Class<?>> validDefaultGroupSequence = new ArrayList();
/*     */     
/* 457 */     boolean groupSequenceContainsDefault = false;
/* 458 */     if (groupSequence != null) {
/* 459 */       for (Class<?> group : groupSequence) {
/* 460 */         if (group.getName().equals(beanClass.getName())) {
/* 461 */           validDefaultGroupSequence.add(Default.class);
/* 462 */           groupSequenceContainsDefault = true;
/*     */         } else {
/* 464 */           if (group.getName().equals(Default.class.getName())) {
/* 465 */             throw log.getNoDefaultGroupInGroupSequenceException();
/*     */           }
/*     */           
/* 468 */           validDefaultGroupSequence.add(group);
/*     */         }
/*     */       }
/*     */     }
/* 472 */     if (!groupSequenceContainsDefault) {
/* 473 */       throw log.getBeanClassMustBePartOfRedefinedDefaultGroupSequenceException(beanClass.getName());
/*     */     }
/* 475 */     if (log.isTraceEnabled()) {
/* 476 */       log.tracef("Members of the default group sequence for bean %s are: %s.", beanClass
/*     */       
/* 478 */         .getName(), validDefaultGroupSequence);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 483 */     return validDefaultGroupSequence;
/*     */   }
/*     */   
/*     */   private boolean hasDefaultGroupSequenceProvider() {
/* 487 */     return this.defaultGroupSequenceProvider != null;
/*     */   }
/*     */   
/*     */   private CollectionHelper.Partitioner<ElementType, MetaConstraint<?>> byElementType() {
/* 491 */     new CollectionHelper.Partitioner()
/*     */     {
/*     */       public ElementType getPartition(MetaConstraint<?> constraint) {
/* 494 */         return constraint.getElementType();
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 501 */     return 
/*     */     
/*     */ 
/*     */ 
/* 505 */       "BeanMetaDataImpl{beanClass=" + this.beanClass.getSimpleName() + ", constraintCount=" + getMetaConstraints().size() + ", cascadedPropertiesCount=" + this.cascadedProperties.size() + ", defaultGroupSequence=" + getDefaultGroupSequence(null) + '}';
/*     */   }
/*     */   
/*     */ 
/*     */   public static class BeanMetaDataBuilder<T>
/*     */   {
/*     */     private final ConstraintHelper constraintHelper;
/*     */     
/*     */     private final ValidationOrderGenerator validationOrderGenerator;
/*     */     
/*     */     private final Class<T> beanClass;
/* 516 */     private final Set<BeanMetaDataImpl.BuilderDelegate> builders = CollectionHelper.newHashSet();
/*     */     
/*     */ 
/*     */     private final ExecutableHelper executableHelper;
/*     */     
/*     */ 
/*     */     private ConfigurationSource sequenceSource;
/*     */     
/*     */ 
/*     */     private ConfigurationSource providerSource;
/*     */     
/*     */     private List<Class<?>> defaultGroupSequence;
/*     */     
/*     */     private DefaultGroupSequenceProvider<? super T> defaultGroupSequenceProvider;
/*     */     
/*     */     private final MethodValidationConfiguration methodValidationConfiguration;
/*     */     
/*     */ 
/*     */     private BeanMetaDataBuilder(ConstraintHelper constraintHelper, ExecutableHelper executableHelper, ValidationOrderGenerator validationOrderGenerator, Class<T> beanClass, MethodValidationConfiguration methodValidationConfiguration)
/*     */     {
/* 536 */       this.beanClass = beanClass;
/* 537 */       this.constraintHelper = constraintHelper;
/* 538 */       this.validationOrderGenerator = validationOrderGenerator;
/* 539 */       this.executableHelper = executableHelper;
/* 540 */       this.methodValidationConfiguration = methodValidationConfiguration;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public static <T> BeanMetaDataBuilder<T> getInstance(ConstraintHelper constraintHelper, ExecutableHelper executableHelper, ValidationOrderGenerator validationOrderGenerator, Class<T> beanClass, MethodValidationConfiguration methodValidationConfiguration)
/*     */     {
/* 549 */       return new BeanMetaDataBuilder(constraintHelper, executableHelper, validationOrderGenerator, beanClass, methodValidationConfiguration);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void add(BeanConfiguration<? super T> configuration)
/*     */     {
/* 558 */       if (configuration.getBeanClass().equals(this.beanClass)) {
/* 559 */         if ((configuration.getDefaultGroupSequence() != null) && ((this.sequenceSource == null) || 
/*     */         
/* 561 */           (configuration.getSource().getPriority() >= this.sequenceSource.getPriority())))
/*     */         {
/* 563 */           this.sequenceSource = configuration.getSource();
/* 564 */           this.defaultGroupSequence = configuration.getDefaultGroupSequence();
/*     */         }
/*     */         
/* 567 */         if ((configuration.getDefaultGroupSequenceProvider() != null) && ((this.providerSource == null) || 
/*     */         
/* 569 */           (configuration.getSource().getPriority() >= this.providerSource.getPriority())))
/*     */         {
/* 571 */           this.providerSource = configuration.getSource();
/* 572 */           this.defaultGroupSequenceProvider = configuration.getDefaultGroupSequenceProvider();
/*     */         }
/*     */       }
/*     */       
/* 576 */       for (ConstrainedElement constrainedElement : configuration.getConstrainedElements()) {
/* 577 */         addMetaDataToBuilder(constrainedElement, this.builders);
/*     */       }
/*     */     }
/*     */     
/*     */     private void addMetaDataToBuilder(ConstrainedElement constrainableElement, Set<BeanMetaDataImpl.BuilderDelegate> builders) {
/* 582 */       for (BeanMetaDataImpl.BuilderDelegate builder : builders) {
/* 583 */         boolean foundBuilder = builder.add(constrainableElement);
/*     */         
/* 585 */         if (foundBuilder) {
/* 586 */           return;
/*     */         }
/*     */       }
/*     */       
/* 590 */       builders.add(new BeanMetaDataImpl.BuilderDelegate(this.beanClass, constrainableElement, this.constraintHelper, this.executableHelper, this.methodValidationConfiguration));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public BeanMetaDataImpl<T> build()
/*     */     {
/* 602 */       Set<ConstraintMetaData> aggregatedElements = CollectionHelper.newHashSet();
/*     */       
/* 604 */       for (BeanMetaDataImpl.BuilderDelegate builder : this.builders) {
/* 605 */         aggregatedElements.addAll(builder.build());
/*     */       }
/*     */       
/* 608 */       return new BeanMetaDataImpl(this.beanClass, this.defaultGroupSequence, this.defaultGroupSequenceProvider, aggregatedElements, this.validationOrderGenerator);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class BuilderDelegate
/*     */   {
/*     */     private final Class<?> beanClass;
/*     */     
/*     */ 
/*     */     private final ConstraintHelper constraintHelper;
/*     */     
/*     */ 
/*     */     private final ExecutableHelper executableHelper;
/*     */     
/*     */ 
/*     */     private MetaDataBuilder propertyBuilder;
/*     */     
/*     */     private ExecutableMetaData.Builder methodBuilder;
/*     */     
/*     */     private final MethodValidationConfiguration methodValidationConfiguration;
/*     */     
/*     */ 
/*     */     public BuilderDelegate(Class<?> beanClass, ConstrainedElement constrainedElement, ConstraintHelper constraintHelper, ExecutableHelper executableHelper, MethodValidationConfiguration methodValidationConfiguration)
/*     */     {
/* 634 */       this.beanClass = beanClass;
/* 635 */       this.constraintHelper = constraintHelper;
/* 636 */       this.executableHelper = executableHelper;
/* 637 */       this.methodValidationConfiguration = methodValidationConfiguration;
/*     */       
/* 639 */       switch (BeanMetaDataImpl.2.$SwitchMap$org$hibernate$validator$internal$metadata$raw$ConstrainedElement$ConstrainedElementKind[constrainedElement.getKind().ordinal()]) {
/*     */       case 1: 
/* 641 */         ConstrainedField constrainedField = (ConstrainedField)constrainedElement;
/* 642 */         this.propertyBuilder = new PropertyMetaData.Builder(beanClass, constrainedField, constraintHelper);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 647 */         break;
/*     */       case 2: 
/*     */       case 3: 
/* 650 */         ConstrainedExecutable constrainedExecutable = (ConstrainedExecutable)constrainedElement;
/* 651 */         Member member = constrainedExecutable.getExecutable().getMember();
/*     */         
/*     */ 
/*     */ 
/* 655 */         if ((!Modifier.isPrivate(member.getModifiers())) || (beanClass == member.getDeclaringClass())) {
/* 656 */           this.methodBuilder = new ExecutableMetaData.Builder(beanClass, constrainedExecutable, constraintHelper, executableHelper, methodValidationConfiguration);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 665 */         if (constrainedExecutable.isGetterMethod()) {
/* 666 */           this.propertyBuilder = new PropertyMetaData.Builder(beanClass, constrainedExecutable, constraintHelper);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */         break;
/*     */       case 4: 
/* 674 */         ConstrainedType constrainedType = (ConstrainedType)constrainedElement;
/* 675 */         this.propertyBuilder = new PropertyMetaData.Builder(beanClass, constrainedType, constraintHelper);
/*     */       }
/*     */       
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public boolean add(ConstrainedElement constrainedElement)
/*     */     {
/* 685 */       boolean added = false;
/*     */       
/* 687 */       if ((this.methodBuilder != null) && (this.methodBuilder.accepts(constrainedElement))) {
/* 688 */         this.methodBuilder.add(constrainedElement);
/* 689 */         added = true;
/*     */       }
/*     */       
/* 692 */       if ((this.propertyBuilder != null) && (this.propertyBuilder.accepts(constrainedElement))) {
/* 693 */         this.propertyBuilder.add(constrainedElement);
/*     */         
/* 695 */         if ((!added) && (constrainedElement.getKind() == ConstrainedElement.ConstrainedElementKind.METHOD) && (this.methodBuilder == null)) {
/* 696 */           ConstrainedExecutable constrainedMethod = (ConstrainedExecutable)constrainedElement;
/* 697 */           this.methodBuilder = new ExecutableMetaData.Builder(this.beanClass, constrainedMethod, this.constraintHelper, this.executableHelper, this.methodValidationConfiguration);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 706 */         added = true;
/*     */       }
/*     */       
/* 709 */       return added;
/*     */     }
/*     */     
/*     */     public Set<ConstraintMetaData> build() {
/* 713 */       Set<ConstraintMetaData> metaDataSet = CollectionHelper.newHashSet();
/*     */       
/* 715 */       if (this.propertyBuilder != null) {
/* 716 */         metaDataSet.add(this.propertyBuilder.build());
/*     */       }
/*     */       
/* 719 */       if (this.methodBuilder != null) {
/* 720 */         metaDataSet.add(this.methodBuilder.build());
/*     */       }
/*     */       
/* 723 */       return metaDataSet;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class DefaultGroupSequenceContext<T>
/*     */   {
/*     */     List<Class<?>> defaultGroupSequence;
/*     */     DefaultGroupSequenceProvider<? super T> defaultGroupSequenceProvider;
/*     */     ValidationOrder validationOrder;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\aggregated\BeanMetaDataImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */