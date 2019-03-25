/*     */ package org.hibernate.validator.internal.metadata.aggregated;
/*     */ 
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ import javax.validation.ElementKind;
/*     */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*     */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*     */ import org.hibernate.validator.internal.metadata.descriptor.ConstraintDescriptorImpl;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
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
/*     */ public abstract class AbstractConstraintMetaData
/*     */   implements ConstraintMetaData
/*     */ {
/*     */   private final String name;
/*     */   private final Type type;
/*     */   private final ElementKind constrainedMetaDataKind;
/*     */   private final Set<MetaConstraint<?>> constraints;
/*     */   private final boolean isCascading;
/*     */   private final boolean isConstrained;
/*     */   private final UnwrapMode unwrapMode;
/*     */   
/*     */   public AbstractConstraintMetaData(String name, Type type, Set<MetaConstraint<?>> constraints, ElementKind constrainedMetaDataKind, boolean isCascading, boolean isConstrained, UnwrapMode unwrapMode)
/*     */   {
/*  44 */     this.name = name;
/*  45 */     this.type = type;
/*  46 */     this.constraints = Collections.unmodifiableSet(constraints);
/*  47 */     this.constrainedMetaDataKind = constrainedMetaDataKind;
/*  48 */     this.isCascading = isCascading;
/*  49 */     this.isConstrained = isConstrained;
/*  50 */     this.unwrapMode = unwrapMode;
/*     */   }
/*     */   
/*     */   public String getName()
/*     */   {
/*  55 */     return this.name;
/*     */   }
/*     */   
/*     */   public Type getType()
/*     */   {
/*  60 */     return this.type;
/*     */   }
/*     */   
/*     */   public Iterator<MetaConstraint<?>> iterator()
/*     */   {
/*  65 */     return this.constraints.iterator();
/*     */   }
/*     */   
/*     */   public Set<MetaConstraint<?>> getConstraints() {
/*  69 */     return this.constraints;
/*     */   }
/*     */   
/*     */   public ElementKind getKind()
/*     */   {
/*  74 */     return this.constrainedMetaDataKind;
/*     */   }
/*     */   
/*     */   public final boolean isCascading()
/*     */   {
/*  79 */     return this.isCascading;
/*     */   }
/*     */   
/*     */   public boolean isConstrained()
/*     */   {
/*  84 */     return this.isConstrained;
/*     */   }
/*     */   
/*     */   public UnwrapMode unwrapMode()
/*     */   {
/*  89 */     return this.unwrapMode;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  94 */     return "AbstractConstraintMetaData [name=" + this.name + ", type=" + this.type + ", constrainedMetaDataKind=" + this.constrainedMetaDataKind + ", constraints=" + this.constraints + ", isCascading=" + this.isCascading + ", isConstrained=" + this.isConstrained + ", unwrapMode=" + this.unwrapMode + "]";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 103 */     int prime = 31;
/* 104 */     int result = 1;
/* 105 */     result = 31 * result + (this.name == null ? 0 : this.name.hashCode());
/* 106 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 111 */     if (this == obj) {
/* 112 */       return true;
/*     */     }
/* 114 */     if (obj == null) {
/* 115 */       return false;
/*     */     }
/* 117 */     if (getClass() != obj.getClass()) {
/* 118 */       return false;
/*     */     }
/* 120 */     AbstractConstraintMetaData other = (AbstractConstraintMetaData)obj;
/* 121 */     if (this.name == null) {
/* 122 */       if (other.name != null) {
/* 123 */         return false;
/*     */       }
/*     */     }
/* 126 */     else if (!this.name.equals(other.name)) {
/* 127 */       return false;
/*     */     }
/* 129 */     return true;
/*     */   }
/*     */   
/*     */   protected Set<ConstraintDescriptorImpl<?>> asDescriptors(Set<MetaConstraint<?>> constraints) {
/* 133 */     Set<ConstraintDescriptorImpl<?>> theValue = CollectionHelper.newHashSet();
/*     */     
/* 135 */     for (MetaConstraint<?> oneConstraint : constraints) {
/* 136 */       theValue.add(oneConstraint.getDescriptor());
/*     */     }
/*     */     
/* 139 */     return theValue;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\aggregated\AbstractConstraintMetaData.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */