/*    */ package org.springframework.aop.framework.autoproxy.target;
/*    */ 
/*    */ import org.springframework.aop.target.AbstractBeanFactoryBasedTargetSource;
/*    */ import org.springframework.aop.target.CommonsPool2TargetSource;
/*    */ import org.springframework.aop.target.PrototypeTargetSource;
/*    */ import org.springframework.aop.target.ThreadLocalTargetSource;
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
/*    */ public class QuickTargetSourceCreator
/*    */   extends AbstractBeanFactoryBasedTargetSourceCreator
/*    */ {
/*    */   public static final String PREFIX_COMMONS_POOL = ":";
/*    */   public static final String PREFIX_THREAD_LOCAL = "%";
/*    */   public static final String PREFIX_PROTOTYPE = "!";
/*    */   
/*    */   protected final AbstractBeanFactoryBasedTargetSource createBeanFactoryBasedTargetSource(Class<?> beanClass, String beanName)
/*    */   {
/* 47 */     if (beanName.startsWith(":")) {
/* 48 */       CommonsPool2TargetSource cpts = new CommonsPool2TargetSource();
/* 49 */       cpts.setMaxSize(25);
/* 50 */       return cpts;
/*    */     }
/* 52 */     if (beanName.startsWith("%")) {
/* 53 */       return new ThreadLocalTargetSource();
/*    */     }
/* 55 */     if (beanName.startsWith("!")) {
/* 56 */       return new PrototypeTargetSource();
/*    */     }
/*    */     
/*    */ 
/* 60 */     return null;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-aop-4.3.14.RELEASE.jar!\org\springframework\aop\framework\autoproxy\target\QuickTargetSourceCreator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */