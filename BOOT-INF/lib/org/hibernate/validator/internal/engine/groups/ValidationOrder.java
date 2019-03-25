/*    */ package org.hibernate.validator.internal.engine.groups;
/*    */ 
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import javax.validation.GroupDefinitionException;
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
/*    */ public abstract interface ValidationOrder
/*    */ {
/* 42 */   public static final ValidationOrder DEFAULT_SEQUENCE = new DefaultValidationOrder(null);
/*    */   
/*    */   public abstract Iterator<Group> getGroupIterator();
/*    */   
/*    */   public static class DefaultValidationOrder implements ValidationOrder
/*    */   {
/*    */     private DefaultValidationOrder() {
/* 49 */       this.defaultSequences = Collections.singletonList(Sequence.DEFAULT);
/*    */     }
/*    */     
/*    */     private final List<Sequence> defaultSequences;
/*    */     public Iterator<Group> getGroupIterator()
/*    */     {
/* 55 */       return Collections.emptyList().iterator();
/*    */     }
/*    */     
/*    */     public Iterator<Sequence> getSequenceIterator()
/*    */     {
/* 60 */       return this.defaultSequences.iterator();
/*    */     }
/*    */     
/*    */     public void assertDefaultGroupSequenceIsExpandable(List<Class<?>> defaultGroupSequence)
/*    */       throws GroupDefinitionException
/*    */     {}
/*    */   }
/*    */   
/*    */   public abstract Iterator<Sequence> getSequenceIterator();
/*    */   
/*    */   public abstract void assertDefaultGroupSequenceIsExpandable(List<Class<?>> paramList)
/*    */     throws GroupDefinitionException;
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\groups\ValidationOrder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */