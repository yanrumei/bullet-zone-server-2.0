/*    */ package org.hibernate.validator.internal.metadata.raw;
/*    */ 
/*    */ import java.util.Collections;
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
/*    */ public class ConstrainedType
/*    */   extends AbstractConstrainedElement
/*    */ {
/*    */   public ConstrainedType(ConfigurationSource source, ConstraintLocation location, Set<MetaConstraint<?>> constraints)
/*    */   {
/* 34 */     super(source, ConstrainedElement.ConstrainedElementKind.TYPE, location, constraints, 
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 39 */       Collections.emptyMap(), false, UnwrapMode.AUTOMATIC);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 47 */     int prime = 31;
/* 48 */     int result = super.hashCode();
/*    */     
/* 50 */     result = 31 * result + (getLocation().getDeclaringClass() == null ? 0 : getLocation().getDeclaringClass().hashCode());
/* 51 */     return result;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 56 */     if (this == obj) {
/* 57 */       return true;
/*    */     }
/* 59 */     if (!super.equals(obj)) {
/* 60 */       return false;
/*    */     }
/* 62 */     if (getClass() != obj.getClass()) {
/* 63 */       return false;
/*    */     }
/* 65 */     ConstrainedType other = (ConstrainedType)obj;
/* 66 */     if (getLocation().getDeclaringClass() == null) {
/* 67 */       if (other.getLocation().getDeclaringClass() != null) {
/* 68 */         return false;
/*    */       }
/*    */     }
/* 71 */     else if (!getLocation().getDeclaringClass().equals(other.getLocation().getDeclaringClass())) {
/* 72 */       return false;
/*    */     }
/* 74 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\raw\ConstrainedType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */