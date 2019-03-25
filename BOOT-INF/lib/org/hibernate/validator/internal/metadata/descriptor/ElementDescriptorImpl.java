/*     */ package org.hibernate.validator.internal.metadata.descriptor;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.validation.groups.Default;
/*     */ import javax.validation.metadata.ConstraintDescriptor;
/*     */ import javax.validation.metadata.ElementDescriptor;
/*     */ import javax.validation.metadata.ElementDescriptor.ConstraintFinder;
/*     */ import javax.validation.metadata.Scope;
/*     */ import org.hibernate.validator.internal.engine.groups.Group;
/*     */ import org.hibernate.validator.internal.engine.groups.ValidationOrder;
/*     */ import org.hibernate.validator.internal.engine.groups.ValidationOrderGenerator;
/*     */ import org.hibernate.validator.internal.metadata.core.ConstraintOrigin;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.internal.util.TypeHelper;
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
/*     */ public abstract class ElementDescriptorImpl
/*     */   implements ElementDescriptor, Serializable
/*     */ {
/*     */   private final Class<?> type;
/*     */   private final Set<ConstraintDescriptorImpl<?>> constraintDescriptors;
/*     */   private final boolean defaultGroupSequenceRedefined;
/*     */   private final List<Class<?>> defaultGroupSequence;
/*     */   
/*     */   public ElementDescriptorImpl(Type type, Set<ConstraintDescriptorImpl<?>> constraintDescriptors, boolean defaultGroupSequenceRedefined, List<Class<?>> defaultGroupSequence)
/*     */   {
/*  54 */     this.type = ((Class)TypeHelper.getErasedType(type));
/*  55 */     this.constraintDescriptors = Collections.unmodifiableSet(constraintDescriptors);
/*  56 */     this.defaultGroupSequenceRedefined = defaultGroupSequenceRedefined;
/*  57 */     this.defaultGroupSequence = Collections.unmodifiableList(defaultGroupSequence);
/*     */   }
/*     */   
/*     */   public final boolean hasConstraints()
/*     */   {
/*  62 */     return this.constraintDescriptors.size() != 0;
/*     */   }
/*     */   
/*     */   public final Class<?> getElementClass()
/*     */   {
/*  67 */     return this.type;
/*     */   }
/*     */   
/*     */   public final Set<ConstraintDescriptor<?>> getConstraintDescriptors()
/*     */   {
/*  72 */     return findConstraints().getConstraintDescriptors();
/*     */   }
/*     */   
/*     */   public final ElementDescriptor.ConstraintFinder findConstraints()
/*     */   {
/*  77 */     return new ConstraintFinderImpl();
/*     */   }
/*     */   
/*     */   private class ConstraintFinderImpl implements ElementDescriptor.ConstraintFinder {
/*     */     private List<Class<?>> groups;
/*     */     private final Set<ConstraintOrigin> definedInSet;
/*     */     private final Set<ElementType> elementTypes;
/*     */     
/*     */     ConstraintFinderImpl() {
/*  86 */       this.elementTypes = new HashSet();
/*  87 */       this.elementTypes.add(ElementType.TYPE);
/*  88 */       this.elementTypes.add(ElementType.METHOD);
/*  89 */       this.elementTypes.add(ElementType.CONSTRUCTOR);
/*  90 */       this.elementTypes.add(ElementType.FIELD);
/*     */       
/*     */ 
/*  93 */       this.elementTypes.add(ElementType.PARAMETER);
/*     */       
/*  95 */       this.definedInSet = CollectionHelper.newHashSet();
/*  96 */       this.definedInSet.add(ConstraintOrigin.DEFINED_LOCALLY);
/*  97 */       this.definedInSet.add(ConstraintOrigin.DEFINED_IN_HIERARCHY);
/*  98 */       this.groups = Collections.emptyList();
/*     */     }
/*     */     
/*     */     public ElementDescriptor.ConstraintFinder unorderedAndMatchingGroups(Class<?>... classes)
/*     */     {
/* 103 */       this.groups = CollectionHelper.newArrayList();
/* 104 */       for (Class<?> clazz : classes) {
/* 105 */         if ((Default.class.equals(clazz)) && (ElementDescriptorImpl.this.defaultGroupSequenceRedefined)) {
/* 106 */           this.groups.addAll(ElementDescriptorImpl.this.defaultGroupSequence);
/*     */         }
/*     */         else {
/* 109 */           this.groups.add(clazz);
/*     */         }
/*     */       }
/* 112 */       return this;
/*     */     }
/*     */     
/*     */     public ElementDescriptor.ConstraintFinder lookingAt(Scope visibility)
/*     */     {
/* 117 */       if (visibility.equals(Scope.LOCAL_ELEMENT)) {
/* 118 */         this.definedInSet.remove(ConstraintOrigin.DEFINED_IN_HIERARCHY);
/*     */       }
/* 120 */       return this;
/*     */     }
/*     */     
/*     */     public ElementDescriptor.ConstraintFinder declaredOn(ElementType... elementTypes)
/*     */     {
/* 125 */       this.elementTypes.clear();
/* 126 */       this.elementTypes.addAll(Arrays.asList(elementTypes));
/* 127 */       return this;
/*     */     }
/*     */     
/*     */     public Set<ConstraintDescriptor<?>> getConstraintDescriptors()
/*     */     {
/* 132 */       Set<ConstraintDescriptor<?>> matchingDescriptors = new HashSet();
/* 133 */       findMatchingDescriptors(matchingDescriptors);
/* 134 */       return Collections.unmodifiableSet(matchingDescriptors);
/*     */     }
/*     */     
/*     */     public boolean hasConstraints()
/*     */     {
/* 139 */       return getConstraintDescriptors().size() != 0;
/*     */     }
/*     */     
/*     */     private void addMatchingDescriptorsForGroup(Class<?> group, Set<ConstraintDescriptor<?>> matchingDescriptors) {
/* 143 */       for (ConstraintDescriptorImpl<?> descriptor : ElementDescriptorImpl.this.constraintDescriptors) {
/* 144 */         if ((this.definedInSet.contains(descriptor.getDefinedOn())) && (this.elementTypes.contains(descriptor.getElementType())) && 
/* 145 */           (descriptor.getGroups().contains(group)))
/* 146 */           matchingDescriptors.add(descriptor);
/*     */       }
/*     */     }
/*     */     
/*     */     private void findMatchingDescriptors(Set<ConstraintDescriptor<?>> matchingDescriptors) {
/*     */       ValidationOrder validationOrder;
/* 152 */       if (!this.groups.isEmpty()) {
/* 153 */         validationOrder = new ValidationOrderGenerator().getValidationOrder(this.groups);
/* 154 */         Iterator<Group> groupIterator = validationOrder.getGroupIterator();
/* 155 */         while (groupIterator.hasNext()) {
/* 156 */           Group g = (Group)groupIterator.next();
/* 157 */           addMatchingDescriptorsForGroup(g.getDefiningClass(), matchingDescriptors);
/*     */         }
/*     */       }
/*     */       else {
/* 161 */         for (ConstraintDescriptorImpl<?> descriptor : ElementDescriptorImpl.this.constraintDescriptors) {
/* 162 */           if ((this.definedInSet.contains(descriptor.getDefinedOn())) && (this.elementTypes.contains(descriptor.getElementType()))) {
/* 163 */             matchingDescriptors.add(descriptor);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\descriptor\ElementDescriptorImpl.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */