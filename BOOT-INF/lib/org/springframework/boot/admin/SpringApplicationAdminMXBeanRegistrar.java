/*     */ package org.springframework.boot.admin;
/*     */ 
/*     */ import java.lang.management.ManagementFactory;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MalformedObjectNameException;
/*     */ import javax.management.ObjectName;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.DisposableBean;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.boot.context.embedded.EmbeddedWebApplicationContext;
/*     */ import org.springframework.boot.context.event.ApplicationReadyEvent;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.context.ApplicationListener;
/*     */ import org.springframework.context.ConfigurableApplicationContext;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.env.StandardEnvironment;
/*     */ import org.springframework.util.Assert;
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
/*     */ public class SpringApplicationAdminMXBeanRegistrar
/*     */   implements ApplicationContextAware, EnvironmentAware, InitializingBean, DisposableBean, ApplicationListener<ApplicationReadyEvent>
/*     */ {
/*  54 */   private static final Log logger = LogFactory.getLog(SpringApplicationAdmin.class);
/*     */   
/*     */   private ConfigurableApplicationContext applicationContext;
/*     */   
/*  58 */   private Environment environment = new StandardEnvironment();
/*     */   
/*     */   private final ObjectName objectName;
/*     */   
/*  62 */   private boolean ready = false;
/*     */   
/*     */   public SpringApplicationAdminMXBeanRegistrar(String name) throws MalformedObjectNameException
/*     */   {
/*  66 */     this.objectName = new ObjectName(name);
/*     */   }
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext)
/*     */     throws BeansException
/*     */   {
/*  72 */     Assert.state(applicationContext instanceof ConfigurableApplicationContext, "ApplicationContext does not implement ConfigurableApplicationContext");
/*     */     
/*  74 */     this.applicationContext = ((ConfigurableApplicationContext)applicationContext);
/*     */   }
/*     */   
/*     */   public void setEnvironment(Environment environment)
/*     */   {
/*  79 */     this.environment = environment;
/*     */   }
/*     */   
/*     */   public void onApplicationEvent(ApplicationReadyEvent event)
/*     */   {
/*  84 */     if (this.applicationContext.equals(event.getApplicationContext())) {
/*  85 */       this.ready = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public void afterPropertiesSet() throws Exception
/*     */   {
/*  91 */     MBeanServer server = ManagementFactory.getPlatformMBeanServer();
/*  92 */     server.registerMBean(new SpringApplicationAdmin(null), this.objectName);
/*  93 */     if (logger.isDebugEnabled()) {
/*  94 */       logger.debug("Application Admin MBean registered with name '" + this.objectName + "'");
/*     */     }
/*     */   }
/*     */   
/*     */   public void destroy()
/*     */     throws Exception
/*     */   {
/* 101 */     ManagementFactory.getPlatformMBeanServer().unregisterMBean(this.objectName);
/*     */   }
/*     */   
/*     */   private class SpringApplicationAdmin implements SpringApplicationAdminMXBean {
/*     */     private SpringApplicationAdmin() {}
/*     */     
/*     */     public boolean isReady() {
/* 108 */       return SpringApplicationAdminMXBeanRegistrar.this.ready;
/*     */     }
/*     */     
/*     */     public boolean isEmbeddedWebApplication()
/*     */     {
/* 113 */       return (SpringApplicationAdminMXBeanRegistrar.this.applicationContext != null) && 
/* 114 */         ((SpringApplicationAdminMXBeanRegistrar.this.applicationContext instanceof EmbeddedWebApplicationContext));
/*     */     }
/*     */     
/*     */     public String getProperty(String key)
/*     */     {
/* 119 */       return 
/* 120 */         SpringApplicationAdminMXBeanRegistrar.this.environment.getProperty(key);
/*     */     }
/*     */     
/*     */     public void shutdown()
/*     */     {
/* 125 */       SpringApplicationAdminMXBeanRegistrar.logger.info("Application shutdown requested.");
/* 126 */       SpringApplicationAdminMXBeanRegistrar.this.applicationContext.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-1.5.10.RELEASE.jar!\org\springframework\boot\admin\SpringApplicationAdminMXBeanRegistrar.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */