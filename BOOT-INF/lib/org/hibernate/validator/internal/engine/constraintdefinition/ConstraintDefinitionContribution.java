/*    */ package org.hibernate.validator.internal.engine.constraintdefinition;
/*    */ 
/*    */ import java.lang.annotation.Annotation;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.validation.ConstraintValidator;
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
/*    */ public class ConstraintDefinitionContribution<A extends Annotation>
/*    */ {
/*    */   private final Class<A> constraintType;
/* 21 */   private final List<Class<? extends ConstraintValidator<A, ?>>> constraintValidators = new ArrayList();
/*    */   
/*    */   private final boolean includeExisting;
/*    */   
/*    */   public ConstraintDefinitionContribution(Class<A> constraintType, List<Class<? extends ConstraintValidator<A, ?>>> constraintValidators, boolean includeExisting)
/*    */   {
/* 27 */     this.constraintType = constraintType;
/* 28 */     this.constraintValidators.addAll(constraintValidators);
/* 29 */     this.includeExisting = includeExisting;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public Class<A> getConstraintType()
/*    */   {
/* 38 */     return this.constraintType;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public List<Class<? extends ConstraintValidator<A, ?>>> getConstraintValidators()
/*    */   {
/* 47 */     return this.constraintValidators;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public boolean includeExisting()
/*    */   {
/* 57 */     return this.includeExisting;
/*    */   }
/*    */   
/*    */   public boolean equals(Object o)
/*    */   {
/* 62 */     if (this == o) {
/* 63 */       return true;
/*    */     }
/* 65 */     if ((o == null) || (getClass() != o.getClass())) {
/* 66 */       return false;
/*    */     }
/*    */     
/* 69 */     ConstraintDefinitionContribution<?> that = (ConstraintDefinitionContribution)o;
/*    */     
/* 71 */     if (!this.constraintType.equals(that.constraintType)) {
/* 72 */       return false;
/*    */     }
/* 74 */     if (!this.constraintValidators.equals(that.constraintValidators)) {
/* 75 */       return false;
/*    */     }
/*    */     
/* 78 */     return true;
/*    */   }
/*    */   
/*    */   public int hashCode()
/*    */   {
/* 83 */     int result = this.constraintType.hashCode();
/* 84 */     result = 31 * result + this.constraintValidators.hashCode();
/* 85 */     return result;
/*    */   }
/*    */   
/*    */   public String toString()
/*    */   {
/* 90 */     return "ConstraintDefinitionContribution{constraintType=" + this.constraintType + ", constraintValidators=" + this.constraintValidators + ", includeExisting=" + this.includeExisting + '}';
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\engine\constraintdefinition\ConstraintDefinitionContribution.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */