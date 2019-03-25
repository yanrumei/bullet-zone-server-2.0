/*    */ package org.springframework.beans.factory;
/*    */ 
/*    */ import org.springframework.beans.FatalBeanException;
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
/*    */ public class FactoryBeanNotInitializedException
/*    */   extends FatalBeanException
/*    */ {
/*    */   public FactoryBeanNotInitializedException()
/*    */   {
/* 44 */     super("FactoryBean is not fully initialized yet");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public FactoryBeanNotInitializedException(String msg)
/*    */   {
/* 52 */     super(msg);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-beans-4.3.14.RELEASE.jar!\org\springframework\beans\factory\FactoryBeanNotInitializedException.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */