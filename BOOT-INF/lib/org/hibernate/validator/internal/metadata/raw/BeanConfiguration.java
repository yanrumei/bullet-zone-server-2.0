/*     */ package org.hibernate.validator.internal.metadata.raw;
/*     */ 
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.hibernate.validator.internal.util.CollectionHelper;
/*     */ import org.hibernate.validator.spi.group.DefaultGroupSequenceProvider;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BeanConfiguration<T>
/*     */ {
/*     */   private final ConfigurationSource source;
/*     */   private final Class<T> beanClass;
/*     */   private final Set<ConstrainedElement> constrainedElements;
/*     */   private final List<Class<?>> defaultGroupSequence;
/*     */   private final DefaultGroupSequenceProvider<? super T> defaultGroupSequenceProvider;
/*     */   
/*     */   public BeanConfiguration(ConfigurationSource source, Class<T> beanClass, Set<? extends ConstrainedElement> constrainedElements, List<Class<?>> defaultGroupSequence, DefaultGroupSequenceProvider<? super T> defaultGroupSequenceProvider)
/*     */   {
/*  54 */     this.source = source;
/*  55 */     this.beanClass = beanClass;
/*  56 */     this.constrainedElements = CollectionHelper.newHashSet(constrainedElements);
/*  57 */     this.defaultGroupSequence = defaultGroupSequence;
/*  58 */     this.defaultGroupSequenceProvider = defaultGroupSequenceProvider;
/*     */   }
/*     */   
/*     */   public ConfigurationSource getSource() {
/*  62 */     return this.source;
/*     */   }
/*     */   
/*     */   public Class<T> getBeanClass() {
/*  66 */     return this.beanClass;
/*     */   }
/*     */   
/*     */   public Set<ConstrainedElement> getConstrainedElements() {
/*  70 */     return this.constrainedElements;
/*     */   }
/*     */   
/*     */   public List<Class<?>> getDefaultGroupSequence() {
/*  74 */     return this.defaultGroupSequence;
/*     */   }
/*     */   
/*     */   public DefaultGroupSequenceProvider<? super T> getDefaultGroupSequenceProvider() {
/*  78 */     return this.defaultGroupSequenceProvider;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/*  83 */     return "BeanConfiguration [beanClass=" + this.beanClass.getSimpleName() + ", source=" + this.source + ", constrainedElements=" + this.constrainedElements + ", defaultGroupSequence=" + this.defaultGroupSequence + ", defaultGroupSequenceProvider=" + this.defaultGroupSequenceProvider + "]";
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  93 */     int prime = 31;
/*  94 */     int result = 1;
/*     */     
/*  96 */     result = 31 * result + (this.beanClass == null ? 0 : this.beanClass.hashCode());
/*  97 */     result = 31 * result + (this.source == null ? 0 : this.source.hashCode());
/*  98 */     return result;
/*     */   }
/*     */   
/*     */   public boolean equals(Object obj)
/*     */   {
/* 103 */     if (this == obj) {
/* 104 */       return true;
/*     */     }
/* 106 */     if (obj == null) {
/* 107 */       return false;
/*     */     }
/* 109 */     if (getClass() != obj.getClass()) {
/* 110 */       return false;
/*     */     }
/* 112 */     BeanConfiguration<?> other = (BeanConfiguration)obj;
/* 113 */     if (this.beanClass == null) {
/* 114 */       if (other.beanClass != null) {
/* 115 */         return false;
/*     */       }
/*     */     }
/* 118 */     else if (!this.beanClass.equals(other.beanClass)) {
/* 119 */       return false;
/*     */     }
/* 121 */     if (this.source != other.source) {
/* 122 */       return false;
/*     */     }
/* 124 */     return true;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\raw\BeanConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */