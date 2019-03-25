/*    */ package org.springframework.aop.framework.autoproxy;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanNameAware;
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
/*    */ public class DefaultAdvisorAutoProxyCreator
/*    */   extends AbstractAdvisorAutoProxyCreator
/*    */   implements BeanNameAware
/*    */ {
/*    */   public static final String SEPARATOR = ".";
/* 44 */   private boolean usePrefix = false;
/*    */   
/*    */ 
/*    */ 
/*    */   private String advisorBeanNamePrefix;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setUsePrefix(boolean usePrefix)
/*    */   {
/* 55 */     this.usePrefix = usePrefix;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public boolean isUsePrefix()
/*    */   {
/* 62 */     return this.usePrefix;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   public void setAdvisorBeanNamePrefix(String advisorBeanNamePrefix)
/*    */   {
/* 72 */     this.advisorBeanNamePrefix = advisorBeanNamePrefix;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String getAdvisorBeanNamePrefix()
/*    */   {
/* 80 */     return this.advisorBeanNamePrefix;
/*    */   }
/*    */   
/*    */ 
/*    */   public void setBeanName(String name)
/*    */   {
/* 86 */     if (this.advisorBeanNamePrefix == null) {
/* 87 */       this.advisorBeanNamePrefix = (name + ".");
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected boolean isEligibleAdvisorBean(String beanName)
/*    */   {
/* 99 */     return (!isUsePrefix()) || (beanName.startsWith(getAdvisorBeanNamePrefix()));
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\autoproxy\DefaultAdvisorAutoProxyCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */