/*    */ package org.springframework.boot.context;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.util.Collection;
/*    */ import java.util.Map;
/*    */ import org.springframework.beans.BeansException;
/*    */ import org.springframework.beans.factory.BeanFactory;
/*    */ import org.springframework.beans.factory.BeanFactoryAware;
/*    */ import org.springframework.beans.factory.ListableBeanFactory;
/*    */ import org.springframework.core.type.classreading.MetadataReader;
/*    */ import org.springframework.core.type.classreading.MetadataReaderFactory;
/*    */ import org.springframework.core.type.filter.TypeFilter;
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
/*    */ 
/*    */ 
/*    */ public class TypeExcludeFilter
/*    */   implements TypeFilter, BeanFactoryAware
/*    */ {
/*    */   private BeanFactory beanFactory;
/*    */   
/*    */   public void setBeanFactory(BeanFactory beanFactory)
/*    */     throws BeansException
/*    */   {
/* 56 */     this.beanFactory = beanFactory;
/*    */   }
/*    */   
/*    */   public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory)
/*    */     throws IOException
/*    */   {
/* 62 */     if (((this.beanFactory instanceof ListableBeanFactory)) && 
/* 63 */       (getClass().equals(TypeExcludeFilter.class)))
/*    */     {
/* 65 */       Collection<TypeExcludeFilter> delegates = ((ListableBeanFactory)this.beanFactory).getBeansOfType(TypeExcludeFilter.class).values();
/* 66 */       for (TypeExcludeFilter delegate : delegates) {
/* 67 */         if (delegate.match(metadataReader, metadataReaderFactory)) {
/* 68 */           return true;
/*    */         }
/*    */       }
/*    */     }
/* 72 */     return false;
/*    */   }
/*    */   
/*    */ 
/*    */   public boolean equals(Object obj)
/*    */   {
/* 78 */     throw new IllegalStateException("TypeExcludeFilter " + getClass() + " has not implemented equals");
/*    */   }
/*    */   
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 84 */     throw new IllegalStateException("TypeExcludeFilter " + getClass() + " has not implemented hashCode");
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\context\TypeExcludeFilter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */