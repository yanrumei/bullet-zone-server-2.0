/*    */ package org.hibernate.validator.internal.metadata.raw;
/*    */ 
/*    */ import java.util.Collections;
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
/*    */ public class ConstrainedField
/*    */   extends AbstractConstrainedElement
/*    */ {
/*    */   private final Set<MetaConstraint<?>> typeArgumentsConstraints;
/*    */   
/*    */   public ConstrainedField(ConfigurationSource source, ConstraintLocation location, Set<MetaConstraint<?>> constraints, Set<MetaConstraint<?>> typeArgumentsConstraints, Map<Class<?>, Class<?>> groupConversions, boolean isCascading, UnwrapMode unwrapMode)
/*    */   {
/* 49 */     super(source, ConstrainedElement.ConstrainedElementKind.FIELD, location, constraints, groupConversions, isCascading, unwrapMode);
/*    */     
/* 51 */     this.typeArgumentsConstraints = (typeArgumentsConstraints != null ? Collections.unmodifiableSet(typeArgumentsConstraints) : 
/*    */     
/* 53 */       Collections.emptySet());
/*    */   }
/*    */   
/*    */   public Set<MetaConstraint<?>> getTypeArgumentsConstraints() {
/* 57 */     return this.typeArgumentsConstraints;
/*    */   }
/*    */   
/*    */   public boolean isConstrained()
/*    */   {
/* 62 */     return (super.isConstrained()) || (!this.typeArgumentsConstraints.isEmpty());
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 67 */     int prime = 31;
/* 68 */     int result = super.hashCode();
/* 69 */     result = 31 * result + (getLocation().getMember() == null ? 0 : getLocation().getMember().hashCode());
/* 70 */     return result;
/*    */   }
/*    */   
/*    */   public boolean equals(Object obj)
/*    */   {
/* 75 */     if (this == obj) {
/* 76 */       return true;
/*    */     }
/* 78 */     if (!super.equals(obj)) {
/* 79 */       return false;
/*    */     }
/* 81 */     if (getClass() != obj.getClass()) {
/* 82 */       return false;
/*    */     }
/* 84 */     ConstrainedField other = (ConstrainedField)obj;
/* 85 */     if (getLocation().getMember() == null) {
/* 86 */       if (other.getLocation().getMember() != null) {
/* 87 */         return false;
/*    */       }
/*    */     }
/* 90 */     else if (!getLocation().getMember().equals(other.getLocation().getMember())) {
/* 91 */       return false;
/*    */     }
/* 93 */     return true;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\raw\ConstrainedField.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */