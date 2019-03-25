/*    */ package org.hibernate.validator.internal.metadata.aggregated;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import javax.validation.ConstraintDeclarationException;
/*    */ import javax.validation.metadata.BeanDescriptor;
/*    */ import org.hibernate.validator.internal.engine.groups.Sequence;
/*    */ import org.hibernate.validator.internal.metadata.core.MetaConstraint;
/*    */ import org.hibernate.validator.internal.metadata.facets.Cascadable;
/*    */ import org.hibernate.validator.internal.metadata.raw.ExecutableElement;
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
/*    */ public final class UnconstrainedEntityMetaDataSingleton<T>
/*    */   implements BeanMetaData<T>
/*    */ {
/* 28 */   private static final UnconstrainedEntityMetaDataSingleton<?> singletonDummy = new UnconstrainedEntityMetaDataSingleton();
/*    */   
/*    */ 
/*    */ 
/*    */   public static UnconstrainedEntityMetaDataSingleton<?> getSingleton()
/*    */   {
/* 34 */     return singletonDummy;
/*    */   }
/*    */   
/*    */   public Class<T> getBeanClass()
/*    */   {
/* 39 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public boolean hasConstraints()
/*    */   {
/* 44 */     return false;
/*    */   }
/*    */   
/*    */   public BeanDescriptor getBeanDescriptor()
/*    */   {
/* 49 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public PropertyMetaData getMetaDataFor(String propertyName)
/*    */   {
/* 54 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public List<Class<?>> getDefaultGroupSequence(T beanState)
/*    */   {
/* 59 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public boolean defaultGroupSequenceIsRedefined()
/*    */   {
/* 64 */     return false;
/*    */   }
/*    */   
/*    */   public Iterator<Sequence> getDefaultValidationSequence(T beanState)
/*    */   {
/* 69 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public Set<MetaConstraint<?>> getMetaConstraints()
/*    */   {
/* 74 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public Set<MetaConstraint<?>> getDirectMetaConstraints()
/*    */   {
/* 79 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public ExecutableMetaData getMetaDataFor(ExecutableElement method) throws ConstraintDeclarationException
/*    */   {
/* 84 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public List<Class<? super T>> getClassHierarchy()
/*    */   {
/* 89 */     throw new UnsupportedOperationException();
/*    */   }
/*    */   
/*    */   public Iterable<Cascadable> getCascadables()
/*    */   {
/* 94 */     throw new UnsupportedOperationException();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\hibernate-validator-5.3.6.Final.jar!\org\hibernate\validator\internal\metadata\aggregated\UnconstrainedEntityMetaDataSingleton.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */