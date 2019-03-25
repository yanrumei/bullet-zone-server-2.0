/*     */ package org.hibernate.validator.internal.engine.groups;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.ConcurrentMap;
/*     */ import javax.validation.GroupSequence;
/*     */ import javax.validation.groups.Default;
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
/*     */ public class ValidationOrderGenerator
/*     */ {
/*  30 */   private static final Log log = ;
/*     */   
/*  32 */   private final ConcurrentMap<Class<?>, Sequence> resolvedSequences = new ConcurrentHashMap();
/*     */   private final DefaultValidationOrder validationOrderForDefaultGroup;
/*     */   
/*     */   public ValidationOrderGenerator()
/*     */   {
/*  37 */     this.validationOrderForDefaultGroup = new DefaultValidationOrder();
/*  38 */     this.validationOrderForDefaultGroup.insertGroup(Group.DEFAULT_GROUP);
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
/*     */   public ValidationOrder getValidationOrder(Class<?> group, boolean expand)
/*     */   {
/*  52 */     if (expand) {
/*  53 */       return getValidationOrder(Arrays.asList(new Class[] { group }));
/*     */     }
/*     */     
/*  56 */     DefaultValidationOrder validationOrder = new DefaultValidationOrder();
/*  57 */     validationOrder.insertGroup(new Group(group));
/*  58 */     return validationOrder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ValidationOrder getValidationOrder(Collection<Class<?>> groups)
/*     */   {
/*  70 */     if ((groups == null) || (groups.size() == 0)) {
/*  71 */       throw log.getAtLeastOneGroupHasToBeSpecifiedException();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  76 */     if ((groups.size() == 1) && (groups.contains(Default.class))) {
/*  77 */       return this.validationOrderForDefaultGroup;
/*     */     }
/*     */     
/*  80 */     for (Iterator localIterator = groups.iterator(); localIterator.hasNext();) { clazz = (Class)localIterator.next();
/*  81 */       if (!clazz.isInterface()) {
/*  82 */         throw log.getGroupHasToBeAnInterfaceException(clazz.getName());
/*     */       }
/*     */     }
/*     */     Class<?> clazz;
/*  86 */     DefaultValidationOrder validationOrder = new DefaultValidationOrder();
/*  87 */     for (Class<?> clazz : groups) {
/*  88 */       if (Default.class.equals(clazz)) {
/*  89 */         validationOrder.insertGroup(Group.DEFAULT_GROUP);
/*     */       }
/*  91 */       else if (isGroupSequence(clazz)) {
/*  92 */         insertSequence(clazz, ((GroupSequence)clazz.getAnnotation(GroupSequence.class)).value(), true, validationOrder);
/*     */       }
/*     */       else {
/*  95 */         Group group = new Group(clazz);
/*  96 */         validationOrder.insertGroup(group);
/*  97 */         insertInheritedGroups(clazz, validationOrder);
/*     */       }
/*     */     }
/*     */     
/* 101 */     return validationOrder;
/*     */   }
/*     */   
/*     */   public ValidationOrder getDefaultValidationOrder(Class<?> clazz, List<Class<?>> defaultGroupSequence) {
/* 105 */     DefaultValidationOrder validationOrder = new DefaultValidationOrder();
/* 106 */     insertSequence(clazz, (Class[])defaultGroupSequence.toArray(new Class[0]), false, validationOrder);
/* 107 */     return validationOrder;
/*     */   }
/*     */   
/*     */   private boolean isGroupSequence(Class<?> clazz) {
/* 111 */     return clazz.getAnnotation(GroupSequence.class) != null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void insertInheritedGroups(Class<?> clazz, DefaultValidationOrder chain)
/*     */   {
/* 121 */     for (Class<?> inheritedGroup : clazz.getInterfaces()) {
/* 122 */       Group group = new Group(inheritedGroup);
/* 123 */       chain.insertGroup(group);
/* 124 */       insertInheritedGroups(inheritedGroup, chain);
/*     */     }
/*     */   }
/*     */   
/*     */   private void insertSequence(Class<?> sequenceClass, Class<?>[] sequenceElements, boolean cache, DefaultValidationOrder validationOrder) {
/* 129 */     Sequence sequence = cache ? (Sequence)this.resolvedSequences.get(sequenceClass) : null;
/* 130 */     if (sequence == null) {
/* 131 */       sequence = resolveSequence(sequenceClass, sequenceElements, new ArrayList());
/*     */       
/* 133 */       sequence.expandInheritedGroups();
/*     */       
/*     */ 
/* 136 */       if (cache) {
/* 137 */         Sequence cachedResolvedSequence = (Sequence)this.resolvedSequences.putIfAbsent(sequenceClass, sequence);
/* 138 */         if (cachedResolvedSequence != null) {
/* 139 */           sequence = cachedResolvedSequence;
/*     */         }
/*     */       }
/*     */     }
/* 143 */     validationOrder.insertSequence(sequence);
/*     */   }
/*     */   
/*     */   private Sequence resolveSequence(Class<?> sequenceClass, Class<?>[] sequenceElements, List<Class<?>> processedSequences) {
/* 147 */     if (processedSequences.contains(sequenceClass)) {
/* 148 */       throw log.getCyclicDependencyInGroupsDefinitionException();
/*     */     }
/*     */     
/* 151 */     processedSequences.add(sequenceClass);
/*     */     
/* 153 */     List<Group> resolvedSequenceGroups = new ArrayList();
/* 154 */     for (Class<?> clazz : sequenceElements) {
/* 155 */       if (isGroupSequence(clazz)) {
/* 156 */         Sequence tmpSequence = resolveSequence(clazz, ((GroupSequence)clazz.getAnnotation(GroupSequence.class)).value(), processedSequences);
/* 157 */         addGroups(resolvedSequenceGroups, tmpSequence.getComposingGroups());
/*     */       }
/*     */       else {
/* 160 */         List<Group> list = new ArrayList();
/* 161 */         list.add(new Group(clazz));
/* 162 */         addGroups(resolvedSequenceGroups, list);
/*     */       }
/*     */     }
/* 165 */     return new Sequence(sequenceClass, resolvedSequenceGroups);
/*     */   }
/*     */   
/*     */   private void addGroups(List<Group> resolvedGroupSequence, List<Group> groups) {
/* 169 */     for (Group tmpGroup : groups) {
/* 170 */       if ((resolvedGroupSequence.contains(tmpGroup)) && 
/* 171 */         (resolvedGroupSequence.indexOf(tmpGroup) < resolvedGroupSequence.size() - 1)) {
/* 172 */         throw log.getUnableToExpandGroupSequenceException();
/*     */       }
/* 174 */       resolvedGroupSequence.add(tmpGroup);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 180 */     StringBuilder sb = new StringBuilder();
/* 181 */     sb.append("ValidationOrderGenerator");
/* 182 */     sb.append("{resolvedSequences=").append(this.resolvedSequences);
/* 183 */     sb.append('}');
/* 184 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\groups\ValidationOrderGenerator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */