/*    */ package org.hibernate.validator.internal.metadata.aggregated;
/*    */ 
/*    */ import org.hibernate.validator.internal.metadata.facets.Cascadable;
/*    */ import org.hibernate.validator.internal.metadata.facets.Validatable;
/*    */ import org.hibernate.validator.internal.util.CollectionHelper;
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
/*    */ public class ValidatableParametersMetaData
/*    */   implements Validatable
/*    */ {
/*    */   private final Iterable<Cascadable> cascadables;
/*    */   
/*    */   public ValidatableParametersMetaData(Iterable<? extends Cascadable> cascadables)
/*    */   {
/* 24 */     this.cascadables = CollectionHelper.newHashSet(cascadables);
/*    */   }
/*    */   
/*    */   public Iterable<Cascadable> getCascadables()
/*    */   {
/* 29 */     return this.cascadables;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\aggregated\ValidatableParametersMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */