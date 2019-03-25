/*    */ package org.springframework.scheduling.support;
/*    */ 
/*    */ import java.lang.reflect.InvocationTargetException;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.beans.factory.BeanClassLoaderAware;
/*    */ import org.springframework.beans.factory.InitializingBean;
/*    */ import org.springframework.beans.support.ArgumentConvertingMethodInvoker;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ public class MethodInvokingRunnable
/*    */   extends ArgumentConvertingMethodInvoker
/*    */   implements Runnable, BeanClassLoaderAware, InitializingBean
/*    */ {
/* 43 */   protected final Log logger = LogFactory.getLog(getClass());
/*    */   
/* 45 */   private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
/*    */   
/*    */ 
/*    */   public void setBeanClassLoader(ClassLoader classLoader)
/*    */   {
/* 50 */     this.beanClassLoader = classLoader;
/*    */   }
/*    */   
/*    */   protected Class<?> resolveClassName(String className) throws ClassNotFoundException
/*    */   {
/* 55 */     return ClassUtils.forName(className, this.beanClassLoader);
/*    */   }
/*    */   
/*    */   public void afterPropertiesSet() throws ClassNotFoundException, NoSuchMethodException
/*    */   {
/* 60 */     prepare();
/*    */   }
/*    */   
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 67 */       invoke();
/*    */     }
/*    */     catch (InvocationTargetException ex) {
/* 70 */       this.logger.error(getInvocationFailureMessage(), ex.getTargetException());
/*    */     }
/*    */     catch (Throwable ex)
/*    */     {
/* 74 */       this.logger.error(getInvocationFailureMessage(), ex);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   protected String getInvocationFailureMessage()
/*    */   {
/* 84 */     return 
/* 85 */       "Invocation of method '" + getTargetMethod() + "' on target class [" + getTargetClass() + "] failed";
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\scheduling\support\MethodInvokingRunnable.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */