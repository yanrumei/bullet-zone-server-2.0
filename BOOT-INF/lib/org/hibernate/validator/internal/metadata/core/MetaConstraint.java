/*     */ package org.hibernate.validator.internal.metadata.core;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.util.Set;
/*     */ import org.hibernate.validator.internal.engine.ValidationContext;
/*     */ import org.hibernate.validator.internal.engine.ValueContext;
/*     */ import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintTree;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
/*     */ import org.hibernate.validator.internal.metadata.location.ConstraintLocation;
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
/*     */ public class MetaConstraint<A extends Annotation>
/*     */ {
/*     */   private final ConstraintTree<A> constraintTree;
/*     */   private final ConstraintDescriptorImpl<A> constraintDescriptor;
/*     */   private final ConstraintLocation location;
/*     */   
/*     */   public MetaConstraint(ConstraintDescriptorImpl<A> constraintDescriptor, ConstraintLocation location)
/*     */   {
/*  48 */     this.constraintTree = new ConstraintTree(constraintDescriptor);
/*  49 */     this.constraintDescriptor = constraintDescriptor;
/*  50 */     this.location = location;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Set<Class<?>> getGroupList()
/*     */   {
/*  58 */     return this.constraintDescriptor.getGroups();
/*     */   }
/*     */   
/*     */   public final ConstraintDescriptorImpl<A> getDescriptor() {
/*  62 */     return this.constraintDescriptor;
/*     */   }
/*     */   
/*     */   public final ElementType getElementType() {
/*  66 */     return this.constraintDescriptor.getElementType();
/*     */   }
/*     */   
/*     */   public boolean validateConstraint(ValidationContext<?> executionContext, ValueContext<?, ?> valueContext) {
/*  70 */     valueContext.setElementType(getElementType());
/*  71 */     valueContext.setDeclaredTypeOfValidatedElement(this.location.getTypeForValidatorResolution());
/*     */     
/*  73 */     boolean validationResult = this.constraintTree.validateConstraints(executionContext, valueContext);
/*  74 */     executionContext.markConstraintProcessed(valueContext.getCurrentBean(), valueContext.getPropertyPath(), this);
/*     */     
/*  76 */     return validationResult;
/*     */   }
/*     */   
/*     */   public ConstraintLocation getLocation() {
/*  80 */     return this.location;
/*     */   }
/*     */   
/*     */   public boolean equals(Object o)
/*     */   {
/*  85 */     if (this == o) {
/*  86 */       return true;
/*     */     }
/*  88 */     if ((o == null) || (getClass() != o.getClass())) {
/*  89 */       return false;
/*     */     }
/*     */     
/*  92 */     MetaConstraint<?> that = (MetaConstraint)o;
/*     */     
/*  94 */     if (this.constraintDescriptor != null ? !this.constraintDescriptor.equals(that.constraintDescriptor) : that.constraintDescriptor != null) {
/*  95 */       return false;
/*     */     }
/*  97 */     if (this.location != null ? !this.location.equals(that.location) : that.location != null) {
/*  98 */       return false;
/*     */     }
/*     */     
/* 101 */     return true;
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 106 */     int result = this.constraintDescriptor != null ? this.constraintDescriptor.hashCode() : 0;
/* 107 */     result = 31 * result + (this.location != null ? this.location.hashCode() : 0);
/* 108 */     return result;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 113 */     StringBuilder sb = new StringBuilder();
/* 114 */     sb.append("MetaConstraint");
/* 115 */     sb.append("{constraintType=").append(this.constraintDescriptor.getAnnotation().annotationType().getName());
/* 116 */     sb.append(", location=").append(this.location);
/* 117 */     sb.append("}");
/* 118 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\core\MetaConstraint.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */