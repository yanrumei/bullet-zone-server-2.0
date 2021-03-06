/*     */ package org.springframework.beans.factory;
/*     */ 
/*     */ import org.springframework.beans.FatalBeanException;
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
/*     */ public class BeanDefinitionStoreException
/*     */   extends FatalBeanException
/*     */ {
/*     */   private String resourceDescription;
/*     */   private String beanName;
/*     */   
/*     */   public BeanDefinitionStoreException(String msg)
/*     */   {
/*  42 */     super(msg);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanDefinitionStoreException(String msg, Throwable cause)
/*     */   {
/*  51 */     super(msg, cause);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanDefinitionStoreException(String resourceDescription, String msg)
/*     */   {
/*  60 */     super(msg);
/*  61 */     this.resourceDescription = resourceDescription;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanDefinitionStoreException(String resourceDescription, String msg, Throwable cause)
/*     */   {
/*  71 */     super(msg, cause);
/*  72 */     this.resourceDescription = resourceDescription;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanDefinitionStoreException(String resourceDescription, String beanName, String msg)
/*     */   {
/*  83 */     this(resourceDescription, beanName, msg, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public BeanDefinitionStoreException(String resourceDescription, String beanName, String msg, Throwable cause)
/*     */   {
/*  95 */     super("Invalid bean definition with name '" + beanName + "' defined in " + resourceDescription + ": " + msg, cause);
/*  96 */     this.resourceDescription = resourceDescription;
/*  97 */     this.beanName = beanName;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public String getResourceDescription()
/*     */   {
/* 106 */     return this.resourceDescription;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public String getBeanName()
/*     */   {
/* 113 */     return this.beanName;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\BeanDefinitionStoreException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */