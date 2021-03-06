/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.beans.factory.config.BeanDefinition;
/*    */ import org.springframework.beans.factory.config.BeanReference;
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
/*    */ public abstract class AbstractComponentDefinition
/*    */   implements ComponentDefinition
/*    */ {
/*    */   public String getDescription()
/*    */   {
/* 40 */     return getName();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public BeanDefinition[] getBeanDefinitions()
/*    */   {
/* 48 */     return new BeanDefinition[0];
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public BeanDefinition[] getInnerBeanDefinitions()
/*    */   {
/* 56 */     return new BeanDefinition[0];
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public BeanReference[] getBeanReferences()
/*    */   {
/* 64 */     return new BeanReference[0];
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public String toString()
/*    */   {
/* 72 */     return getDescription();
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\parsing\AbstractComponentDefinition.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */