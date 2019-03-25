/*    */ package org.springframework.beans.factory.parsing;
/*    */ 
/*    */ import org.springframework.beans.factory.BeanDefinitionStoreException;
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
/*    */ public class BeanDefinitionParsingException
/*    */   extends BeanDefinitionStoreException
/*    */ {
/*    */   public BeanDefinitionParsingException(Problem problem)
/*    */   {
/* 37 */     super(problem.getResourceDescription(), problem.toString(), problem.getRootCause());
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\parsing\BeanDefinitionParsingException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */