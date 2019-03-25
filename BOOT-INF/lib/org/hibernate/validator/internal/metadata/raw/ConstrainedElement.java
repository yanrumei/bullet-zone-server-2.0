/*    */ package org.hibernate.validator.internal.metadata.raw;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*    */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*    */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
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
/*    */ public abstract interface ConstrainedElement
/*    */   extends Iterable<MetaConstraint<?>>
/*    */ {
/*    */   public abstract ConstrainedElementKind getKind();
/*    */   
/*    */   public abstract ConstraintLocation getLocation();
/*    */   
/*    */   public abstract Set<MetaConstraint<?>> getConstraints();
/*    */   
/*    */   public abstract Map<Class<?>, Class<?>> getGroupConversions();
/*    */   
/*    */   public abstract boolean isCascading();
/*    */   
/*    */   public abstract boolean isConstrained();
/*    */   
/*    */   public abstract UnwrapMode unwrapMode();
/*    */   
/*    */   public static enum ConstrainedElementKind
/*    */   {
/* 49 */     TYPE,  FIELD,  CONSTRUCTOR,  METHOD,  PARAMETER,  TYPE_USE;
/*    */     
/*    */     private ConstrainedElementKind() {}
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\raw\ConstrainedElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */