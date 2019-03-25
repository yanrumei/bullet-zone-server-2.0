/*     */ package org.hibernate.validator.internal.engine.groups;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import javax.validation.GroupDefinitionException;
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
/*     */ public final class DefaultValidationOrder
/*     */   implements ValidationOrder
/*     */ {
/*  27 */   private static final Log log = ;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*  32 */   private List<Group> groupList = CollectionHelper.newArrayList();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  38 */   private Map<Class<?>, Sequence> sequenceMap = CollectionHelper.newHashMap();
/*     */   
/*     */   public Iterator<Group> getGroupIterator()
/*     */   {
/*  42 */     return this.groupList.iterator();
/*     */   }
/*     */   
/*     */   public Iterator<Sequence> getSequenceIterator()
/*     */   {
/*  47 */     return this.sequenceMap.values().iterator();
/*     */   }
/*     */   
/*     */   public void insertGroup(Group group) {
/*  51 */     if (!this.groupList.contains(group)) {
/*  52 */       this.groupList.add(group);
/*     */     }
/*     */   }
/*     */   
/*     */   public void insertSequence(Sequence sequence) {
/*  57 */     if (sequence == null) {
/*  58 */       return;
/*     */     }
/*     */     
/*  61 */     if (!this.sequenceMap.containsKey(sequence.getDefiningClass())) {
/*  62 */       this.sequenceMap.put(sequence.getDefiningClass(), sequence);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  68 */     return "ValidationOrder{groupList=" + this.groupList + ", sequenceMap=" + this.sequenceMap + '}';
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
/*     */   public void assertDefaultGroupSequenceIsExpandable(List<Class<?>> defaultGroupSequence)
/*     */     throws GroupDefinitionException
/*     */   {
/*  86 */     for (Map.Entry<Class<?>, Sequence> entry : this.sequenceMap.entrySet()) {
/*  87 */       List<Group> sequenceGroups = ((Sequence)entry.getValue()).getComposingGroups();
/*  88 */       int defaultGroupIndex = sequenceGroups.indexOf(Group.DEFAULT_GROUP);
/*  89 */       if (defaultGroupIndex != -1) {
/*  90 */         List<Group> defaultGroupList = buildTempGroupList(defaultGroupSequence);
/*  91 */         ensureDefaultGroupSequenceIsExpandable(sequenceGroups, defaultGroupList, defaultGroupIndex);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void ensureDefaultGroupSequenceIsExpandable(List<Group> groupList, List<Group> defaultGroupList, int defaultGroupIndex) {
/*  97 */     for (int i = 0; i < defaultGroupList.size(); i++) {
/*  98 */       Group group = (Group)defaultGroupList.get(i);
/*  99 */       if (!Group.DEFAULT_GROUP.equals(group))
/*     */       {
/*     */ 
/* 102 */         int index = groupList.indexOf(group);
/* 103 */         if (index != -1)
/*     */         {
/*     */ 
/*     */ 
/* 107 */           if (((i != 0) || (index != defaultGroupIndex - 1)) && ((i != defaultGroupList.size() - 1) || (index != defaultGroupIndex + 1)))
/*     */           {
/*     */ 
/*     */ 
/*     */ 
/* 112 */             throw log.getUnableToExpandDefaultGroupListException(defaultGroupList, groupList); } }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 117 */   private List<Group> buildTempGroupList(List<Class<?>> defaultGroupSequence) { List<Group> groups = new ArrayList();
/* 118 */     for (Class<?> clazz : defaultGroupSequence) {
/* 119 */       Group g = new Group(clazz);
/* 120 */       groups.add(g);
/*     */     }
/* 122 */     return groups;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\groups\DefaultValidationOrder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */