/*     */ package org.springframework.aop.target;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.TargetSource;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.util.ObjectUtils;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractBeanFactoryBasedTargetSource
/*     */   implements TargetSource, BeanFactoryAware, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -4721607536018568393L;
/*  57 */   protected final Log logger = LogFactory.getLog(getClass());
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private String targetBeanName;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Class<?> targetClass;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private BeanFactory beanFactory;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTargetBeanName(String targetBeanName)
/*     */   {
/*  82 */     this.targetBeanName = targetBeanName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getTargetBeanName()
/*     */   {
/*  89 */     return this.targetBeanName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTargetClass(Class<?> targetClass)
/*     */   {
/*  99 */     this.targetClass = targetClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBeanFactory(BeanFactory beanFactory)
/*     */   {
/* 108 */     if (this.targetBeanName == null) {
/* 109 */       throw new IllegalStateException("Property 'targetBeanName' is required");
/*     */     }
/* 111 */     this.beanFactory = beanFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public BeanFactory getBeanFactory()
/*     */   {
/* 118 */     return this.beanFactory;
/*     */   }
/*     */   
/*     */ 
/*     */   public synchronized Class<?> getTargetClass()
/*     */   {
/* 124 */     if ((this.targetClass == null) && (this.beanFactory != null))
/*     */     {
/* 126 */       this.targetClass = this.beanFactory.getType(this.targetBeanName);
/* 127 */       if (this.targetClass == null) {
/* 128 */         if (this.logger.isTraceEnabled()) {
/* 129 */           this.logger.trace("Getting bean with name '" + this.targetBeanName + "' in order to determine type");
/*     */         }
/* 131 */         Object beanInstance = this.beanFactory.getBean(this.targetBeanName);
/* 132 */         if (beanInstance != null) {
/* 133 */           this.targetClass = beanInstance.getClass();
/*     */         }
/*     */       }
/*     */     }
/* 137 */     return this.targetClass;
/*     */   }
/*     */   
/*     */   public boolean isStatic()
/*     */   {
/* 142 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void releaseTarget(Object target)
/*     */     throws Exception
/*     */   {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void copyFrom(AbstractBeanFactoryBasedTargetSource other)
/*     */   {
/* 157 */     this.targetBeanName = other.targetBeanName;
/* 158 */     this.targetClass = other.targetClass;
/* 159 */     this.beanFactory = other.beanFactory;
/*     */   }
/*     */   
/*     */ 
/*     */   public boolean equals(Object other)
/*     */   {
/* 165 */     if (this == other) {
/* 166 */       return true;
/*     */     }
/* 168 */     if ((other == null) || (getClass() != other.getClass())) {
/* 169 */       return false;
/*     */     }
/* 171 */     AbstractBeanFactoryBasedTargetSource otherTargetSource = (AbstractBeanFactoryBasedTargetSource)other;
/* 172 */     return (ObjectUtils.nullSafeEquals(this.beanFactory, otherTargetSource.beanFactory)) && 
/* 173 */       (ObjectUtils.nullSafeEquals(this.targetBeanName, otherTargetSource.targetBeanName));
/*     */   }
/*     */   
/*     */   public int hashCode()
/*     */   {
/* 178 */     int hashCode = getClass().hashCode();
/* 179 */     hashCode = 13 * hashCode + ObjectUtils.nullSafeHashCode(this.beanFactory);
/* 180 */     hashCode = 13 * hashCode + ObjectUtils.nullSafeHashCode(this.targetBeanName);
/* 181 */     return hashCode;
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 186 */     StringBuilder sb = new StringBuilder(getClass().getSimpleName());
/* 187 */     sb.append(" for target bean '").append(this.targetBeanName).append("'");
/* 188 */     if (this.targetClass != null) {
/* 189 */       sb.append(" of type [").append(this.targetClass.getName()).append("]");
/*     */     }
/* 191 */     return sb.toString();
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\target\AbstractBeanFactoryBasedTargetSource.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */