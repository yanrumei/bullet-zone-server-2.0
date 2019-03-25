/*     */ package org.hibernate.validator.internal.metadata.raw;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.hibernate.validator.internal.engine.valuehandling.UnwrapMode;
/*     */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
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
/*     */ public abstract class AbstractConstrainedElement
/*     */   implements ConstrainedElement
/*     */ {
/*     */   private final ConstrainedElement.ConstrainedElementKind kind;
/*     */   protected final ConfigurationSource source;
/*     */   protected final ConstraintLocation location;
/*     */   protected final Set<MetaConstraint<?>> constraints;
/*     */   protected final Map<Class<?>, Class<?>> groupConversions;
/*     */   protected final boolean isCascading;
/*     */   protected final UnwrapMode unwrapMode;
/*     */   
/*     */   public AbstractConstrainedElement(ConfigurationSource source, ConstrainedElement.ConstrainedElementKind kind, ConstraintLocation location, Set<MetaConstraint<?>> constraints, Map<Class<?>, Class<?>> groupConversions, boolean isCascading, UnwrapMode unwrapMode)
/*     */   {
/*  40 */     this.kind = kind;
/*  41 */     this.source = source;
/*  42 */     this.location = location;
/*  43 */     this.constraints = (constraints != null ? Collections.unmodifiableSet(constraints) : Collections.emptySet());
/*  44 */     this.groupConversions = Collections.unmodifiableMap(groupConversions);
/*  45 */     this.isCascading = isCascading;
/*  46 */     this.unwrapMode = unwrapMode;
/*     */   }
/*     */   
/*     */   public ConstrainedElement.ConstrainedElementKind getKind()
/*     */   {
/*  51 */     return this.kind;
/*     */   }
/*     */   
/*     */   public ConstraintLocation getLocation()
/*     */   {
/*  56 */     return this.location;
/*     */   }
/*     */   
/*     */   public Iterator<MetaConstraint<?>> iterator()
/*     */   {
/*  61 */     return this.constraints.iterator();
/*     */   }
/*     */   
/*     */   public Set<MetaConstraint<?>> getConstraints()
/*     */   {
/*  66 */     return this.constraints;
/*     */   }
/*     */   
/*     */   public Map<Class<?>, Class<?>> getGroupConversions()
/*     */   {
/*  71 */     return this.groupConversions;
/*     */   }
/*     */   
/*     */   public boolean isCascading()
/*     */   {
/*  76 */     return this.isCascading;
/*     */   }
/*     */   
/*     */   public boolean isConstrained()
/*     */   {
/*  81 */     return (this.isCascading) || (!this.constraints.isEmpty());
/*     */   }
/*     */   
/*     */   public UnwrapMode unwrapMode()
/*     */   {
/*  86 */     return this.unwrapMode;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  91 */     return "AbstractConstrainedElement [kind=" + this.kind + ", source=" + this.source + ", location=" + this.location + ", constraints=" + this.constraints + ", groupConversions=" + this.groupConversions + ", isCascading=" + this.isCascading + ", unwrapMode=" + this.unwrapMode + "]";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 100 */     int prime = 31;
/* 101 */     int result = 1;
/* 102 */     result = 31 * result + (this.source == null ? 0 : this.source.hashCode());
/* 103 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 108 */     if (this == obj) {
/* 109 */       return true;
/*     */     }
/* 111 */     if (obj == null) {
/* 112 */       return false;
/*     */     }
/* 114 */     if (getClass() != obj.getClass()) {
/* 115 */       return false;
/*     */     }
/* 117 */     AbstractConstrainedElement other = (AbstractConstrainedElement)obj;
/* 118 */     if (this.source != other.source) {
/* 119 */       return false;
/*     */     }
/* 121 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\raw\AbstractConstrainedElement.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */