/*     */ package org.hibernate.validator.internal.metadata.descriptor;
/*     */ 
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.validation.metadata.BeanDescriptor;
/*     */ import javax.validation.metadata.ConstructorDescriptor;
/*     */ import javax.validation.metadata.MethodDescriptor;
/*     */ import javax.validation.metadata.MethodType;
/*     */ import javax.validation.metadata.PropertyDescriptor;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.Contracts;
/*     */ import org.hibernate.validator.internal.util.ExecutableHelper;
/*     */ import org.hibernate.validator.internal.util.logging.Messages;
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
/*     */ public class BeanDescriptorImpl
/*     */   extends ElementDescriptorImpl
/*     */   implements BeanDescriptor
/*     */ {
/*     */   private final Map<String, PropertyDescriptor> constrainedProperties;
/*     */   private final Map<String, ExecutableDescriptorImpl> constrainedMethods;
/*     */   private final Map<String, ConstructorDescriptor> constrainedConstructors;
/*     */   
/*     */   public BeanDescriptorImpl(Type beanClass, Set<ConstraintDescriptorImpl<?>> classLevelConstraints, Map<String, PropertyDescriptor> constrainedProperties, Map<String, ExecutableDescriptorImpl> constrainedMethods, Map<String, ConstructorDescriptor> constrainedConstructors, boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence)
/*     */   {
/*  47 */     super(beanClass, classLevelConstraints, defaultGroupSequenceRedefined, defaultGroupSequence);
/*     */     
/*  49 */     this.constrainedProperties = Collections.unmodifiableMap(constrainedProperties);
/*  50 */     this.constrainedMethods = Collections.unmodifiableMap(constrainedMethods);
/*  51 */     this.constrainedConstructors = Collections.unmodifiableMap(constrainedConstructors);
/*     */   }
/*     */   
/*     */   public final boolean isBeanConstrained()
/*     */   {
/*  56 */     return (hasConstraints()) || (!this.constrainedProperties.isEmpty());
/*     */   }
/*     */   
/*     */   public final PropertyDescriptor getConstraintsForProperty(String propertyName)
/*     */   {
/*  61 */     Contracts.assertNotNull(propertyName, "The property name cannot be null");
/*  62 */     return (PropertyDescriptor)this.constrainedProperties.get(propertyName);
/*     */   }
/*     */   
/*     */   public final Set<PropertyDescriptor> getConstrainedProperties()
/*     */   {
/*  67 */     return CollectionHelper.newHashSet(this.constrainedProperties.values());
/*     */   }
/*     */   
/*     */   public ConstructorDescriptor getConstraintsForConstructor(Class<?>... parameterTypes)
/*     */   {
/*  72 */     return (ConstructorDescriptor)this.constrainedConstructors.get(ExecutableHelper.getSignature(getElementClass().getSimpleName(), parameterTypes));
/*     */   }
/*     */   
/*     */   public Set<ConstructorDescriptor> getConstrainedConstructors()
/*     */   {
/*  77 */     return CollectionHelper.newHashSet(this.constrainedConstructors.values());
/*     */   }
/*     */   
/*     */   public Set<MethodDescriptor> getConstrainedMethods(MethodType methodType, MethodType... methodTypes)
/*     */   {
/*  82 */     boolean includeGetters = MethodType.GETTER.equals(methodType);
/*  83 */     boolean includeNonGetters = MethodType.NON_GETTER.equals(methodType);
/*  84 */     if (methodTypes != null) {
/*  85 */       for (MethodType type : methodTypes) {
/*  86 */         if (MethodType.GETTER.equals(type)) {
/*  87 */           includeGetters = true;
/*     */         }
/*  89 */         if (MethodType.NON_GETTER.equals(type)) {
/*  90 */           includeNonGetters = true;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*  95 */     Object matchingMethodDescriptors = CollectionHelper.newHashSet();
/*  96 */     for (ExecutableDescriptorImpl constrainedMethod : this.constrainedMethods.values()) {
/*  97 */       boolean addToSet = false;
/*  98 */       if (((constrainedMethod.isGetter()) && (includeGetters)) || ((!constrainedMethod.isGetter()) && (includeNonGetters))) {
/*  99 */         addToSet = true;
/*     */       }
/*     */       
/* 102 */       if (addToSet) {
/* 103 */         ((Set)matchingMethodDescriptors).add(constrainedMethod);
/*     */       }
/*     */     }
/*     */     
/* 107 */     return (Set<MethodDescriptor>)matchingMethodDescriptors;
/*     */   }
/*     */   
/*     */   public MethodDescriptor getConstraintsForMethod(String methodName, Class<?>... parameterTypes)
/*     */   {
/* 112 */     Contracts.assertNotNull(methodName, Messages.MESSAGES.methodNameMustNotBeNull());
/* 113 */     return (MethodDescriptor)this.constrainedMethods.get(ExecutableHelper.getSignature(methodName, parameterTypes));
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 118 */     StringBuilder sb = new StringBuilder();
/* 119 */     sb.append("BeanDescriptorImpl");
/* 120 */     sb.append("{class='");
/* 121 */     sb.append(getElementClass().getSimpleName());
/* 122 */     sb.append("'}");
/* 123 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\descriptor\BeanDescriptorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */