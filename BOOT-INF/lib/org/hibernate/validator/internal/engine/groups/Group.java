/*    */ package org.hibernate.validator.internal.engine.groups;
/*    */ 
/*    */ import javax.validation.groups.Default;
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
/*    */ 
/*    */ public class Group
/*    */ {
/* 17 */   public static final Group DEFAULT_GROUP = new Group(Default.class);
/*    */   
/*    */ 
/*    */   private Class<?> group;
/*    */   
/*    */ 
/*    */   public Group(Class<?> group)
/*    */   {
/* 25 */     this.group = group;
/*    */   }
/*    */   
/*    */   public Class<?> getDefiningClass() {
/* 29 */     return this.group;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 34 */     if (this == o) {
/* 35 */       return true;
/*    */     }
/* 37 */     if ((o == null) || (getClass() != o.getClass())) {
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     Group group1 = (Group)o;
/*    */     
/* 43 */     if (this.group != null ? !this.group.equals(group1.group) : group1.group != null) {
/* 44 */       return false;
/*    */     }
/* 46 */     return true;
/*    */   }
/*    */   
/*    */   public boolean isDefaultGroup() {
/* 50 */     return getDefiningClass().getName().equals(Default.class.getName());
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 55 */     return this.group != null ? this.group.hashCode() : 0;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 60 */     return "Group{group=" + this.group.getName() + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\groups\Group.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */