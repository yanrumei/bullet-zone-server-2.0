/*     */ package org.springframework.context.access;
/*     */ 
/*     */ import javax.naming.NamingException;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.access.BeanFactoryLocator;
/*     */ import org.springframework.beans.factory.access.BeanFactoryReference;
/*     */ import org.springframework.beans.factory.access.BootstrapException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.support.ClassPathXmlApplicationContext;
/*     */ import org.springframework.jndi.JndiLocatorSupport;
/*     */ import org.springframework.util.StringUtils;
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
/*     */ public class ContextJndiBeanFactoryLocator
/*     */   extends JndiLocatorSupport
/*     */   implements BeanFactoryLocator
/*     */ {
/*     */   public static final String BEAN_FACTORY_PATH_DELIMITERS = ",; \t\n";
/*     */   
/*     */   public BeanFactoryReference useBeanFactory(String factoryKey)
/*     */     throws BeansException
/*     */   {
/*     */     try
/*     */     {
/*  63 */       String beanFactoryPath = (String)lookup(factoryKey, String.class);
/*  64 */       if (this.logger.isTraceEnabled()) {
/*  65 */         this.logger.trace("Bean factory path from JNDI environment variable [" + factoryKey + "] is: " + beanFactoryPath);
/*     */       }
/*     */       
/*  68 */       String[] paths = StringUtils.tokenizeToStringArray(beanFactoryPath, ",; \t\n");
/*  69 */       return createBeanFactory(paths);
/*     */     }
/*     */     catch (NamingException ex) {
/*  72 */       throw new BootstrapException("Define an environment variable [" + factoryKey + "] containing the class path locations of XML bean definition files", ex);
/*     */     }
/*     */   }
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
/*     */   protected BeanFactoryReference createBeanFactory(String[] resources)
/*     */     throws BeansException
/*     */   {
/*  91 */     ApplicationContext ctx = createApplicationContext(resources);
/*  92 */     return new ContextBeanFactoryReference(ctx);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ApplicationContext createApplicationContext(String[] resources)
/*     */     throws BeansException
/*     */   {
/* 103 */     return new ClassPathXmlApplicationContext(resources);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\access\ContextJndiBeanFactoryLocator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */