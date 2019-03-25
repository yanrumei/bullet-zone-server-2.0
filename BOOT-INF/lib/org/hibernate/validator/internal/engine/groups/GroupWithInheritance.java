/*    */ package org.hibernate.validator.internal.engine.groups;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GroupWithInheritance
/*    */   implements Iterable<Group>
/*    */ {
/*    */   private final Set<Group> groups;
/*    */   
/*    */   public GroupWithInheritance(Set<Group> groups)
/*    */   {
/* 23 */     this.groups = Collections.unmodifiableSet(groups);
/*    */   }
/*    */   
/*    */   public Iterator<Group> iterator()
/*    */   {
/* 28 */     return this.groups.iterator();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\groups\GroupWithInheritance.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */