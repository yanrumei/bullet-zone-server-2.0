/*    */ package org.springframework.beans.factory.parsing;
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
/*    */ public class BeanEntry
/*    */   implements ParseState.Entry
/*    */ {
/*    */   private String beanDefinitionName;
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
/*    */   public BeanEntry(String beanDefinitionName)
/*    */   {
/* 35 */     this.beanDefinitionName = beanDefinitionName;
/*    */   }
/*    */   
/*    */ 
/*    */   public String toString()
/*    */   {
/* 41 */     return "Bean '" + this.beanDefinitionName + "'";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\parsing\BeanEntry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */