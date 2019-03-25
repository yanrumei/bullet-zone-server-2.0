/*     */ package org.hibernate.validator.internal.engine.groups;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
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
/*     */ 
/*     */ 
/*     */ public class Sequence
/*     */   implements Iterable<GroupWithInheritance>
/*     */ {
/*  32 */   public static Sequence DEFAULT = new Sequence();
/*     */   
/*  34 */   private static final Log log = LoggerFactory.make();
/*     */   private final Class<?> sequence;
/*     */   private List<Group> groups;
/*     */   private List<GroupWithInheritance> expandedGroups;
/*     */   
/*     */   private Sequence()
/*     */   {
/*  41 */     this.sequence = Default.class;
/*  42 */     this.groups = Collections.singletonList(Group.DEFAULT_GROUP);
/*  43 */     this.expandedGroups = Collections.singletonList(new GroupWithInheritance(
/*  44 */       Collections.singleton(Group.DEFAULT_GROUP)));
/*     */   }
/*     */   
/*     */   public Sequence(Class<?> sequence, List<Group> groups)
/*     */   {
/*  49 */     this.groups = groups;
/*  50 */     this.sequence = sequence;
/*     */   }
/*     */   
/*     */   public List<Group> getComposingGroups() {
/*  54 */     return this.groups;
/*     */   }
/*     */   
/*     */   public Class<?> getDefiningClass() {
/*  58 */     return this.sequence;
/*     */   }
/*     */   
/*     */   public void expandInheritedGroups() {
/*  62 */     if (this.expandedGroups != null) {
/*  63 */       return;
/*     */     }
/*     */     
/*  66 */     this.expandedGroups = new ArrayList();
/*  67 */     ArrayList<Group> tmpGroups = new ArrayList();
/*     */     
/*  69 */     for (Group group : this.groups) {
/*  70 */       HashSet<Group> groupsOfGroup = new HashSet();
/*     */       
/*  72 */       groupsOfGroup.add(group);
/*  73 */       addInheritedGroups(group, groupsOfGroup);
/*     */       
/*  75 */       this.expandedGroups.add(new GroupWithInheritance(groupsOfGroup));
/*  76 */       tmpGroups.addAll(groupsOfGroup);
/*     */     }
/*     */     
/*  79 */     this.groups = tmpGroups;
/*     */   }
/*     */   
/*     */   public Iterator<GroupWithInheritance> iterator()
/*     */   {
/*  84 */     return this.expandedGroups.iterator();
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/*  89 */     if (this == o) {
/*  90 */       return true;
/*     */     }
/*  92 */     if ((o == null) || (getClass() != o.getClass())) {
/*  93 */       return false;
/*     */     }
/*     */     
/*  96 */     Sequence sequence1 = (Sequence)o;
/*     */     
/*  98 */     if (this.groups != null ? !this.groups.equals(sequence1.groups) : sequence1.groups != null) {
/*  99 */       return false;
/*     */     }
/* 101 */     if (this.sequence != null ? !this.sequence.equals(sequence1.sequence) : sequence1.sequence != null) {
/* 102 */       return false;
/*     */     }
/*     */     
/* 105 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 110 */     int result = this.sequence != null ? this.sequence.hashCode() : 0;
/* 111 */     result = 31 * result + (this.groups != null ? this.groups.hashCode() : 0);
/* 112 */     return result;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 117 */     StringBuilder sb = new StringBuilder();
/* 118 */     sb.append("Sequence");
/* 119 */     sb.append("{sequence=").append(this.sequence);
/* 120 */     sb.append(", groups=").append(this.groups);
/* 121 */     sb.append('}');
/* 122 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void addInheritedGroups(Group group, Set<Group> expandedGroups)
/*     */   {
/* 132 */     for (Class<?> inheritedGroup : group.getDefiningClass().getInterfaces()) {
/* 133 */       if (isGroupSequence(inheritedGroup)) {
/* 134 */         throw log.getSequenceDefinitionsNotAllowedException();
/*     */       }
/* 136 */       Group g = new Group(inheritedGroup);
/* 137 */       expandedGroups.add(g);
/* 138 */       addInheritedGroups(g, expandedGroups);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean isGroupSequence(Class<?> clazz) {
/* 143 */     return clazz.getAnnotation(GroupSequence.class) != null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\groups\Sequence.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */