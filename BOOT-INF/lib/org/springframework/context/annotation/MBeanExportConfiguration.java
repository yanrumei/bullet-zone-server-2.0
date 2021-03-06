/*     */ package org.springframework.context.annotation;
/*     */ 
/*     */ import java.util.Map;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.naming.NamingException;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.context.EnvironmentAware;
/*     */ import org.springframework.core.annotation.AnnotationAttributes;
/*     */ import org.springframework.core.env.Environment;
/*     */ import org.springframework.core.type.AnnotationMetadata;
/*     */ import org.springframework.jmx.MBeanServerNotFoundException;
/*     */ import org.springframework.jmx.export.annotation.AnnotationMBeanExporter;
/*     */ import org.springframework.jmx.support.RegistrationPolicy;
/*     */ import org.springframework.jmx.support.WebSphereMBeanServerFactoryBean;
/*     */ import org.springframework.jndi.JndiLocatorDelegate;
/*     */ import org.springframework.util.ClassUtils;
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
/*     */ @Configuration
/*     */ public class MBeanExportConfiguration
/*     */   implements ImportAware, EnvironmentAware, BeanFactoryAware
/*     */ {
/*     */   private static final String MBEAN_EXPORTER_BEAN_NAME = "mbeanExporter";
/*     */   private AnnotationAttributes enableMBeanExport;
/*     */   private Environment environment;
/*     */   private BeanFactory beanFactory;
/*     */   
/*     */   public void setImportMetadata(AnnotationMetadata importMetadata)
/*     */   {
/*  63 */     Map<String, Object> map = importMetadata.getAnnotationAttributes(EnableMBeanExport.class.getName());
/*  64 */     this.enableMBeanExport = AnnotationAttributes.fromMap(map);
/*  65 */     if (this.enableMBeanExport == null)
/*     */     {
/*  67 */       throw new IllegalArgumentException("@EnableMBeanExport is not present on importing class " + importMetadata.getClassName());
/*     */     }
/*     */   }
/*     */   
/*     */   public void setEnvironment(Environment environment)
/*     */   {
/*  73 */     this.environment = environment;
/*     */   }
/*     */   
/*     */   public void setBeanFactory(BeanFactory beanFactory)
/*     */   {
/*  78 */     this.beanFactory = beanFactory;
/*     */   }
/*     */   
/*     */   @Bean(name={"mbeanExporter"})
/*     */   @Role(2)
/*     */   public AnnotationMBeanExporter mbeanExporter()
/*     */   {
/*  85 */     AnnotationMBeanExporter exporter = new AnnotationMBeanExporter();
/*  86 */     setupDomain(exporter);
/*  87 */     setupServer(exporter);
/*  88 */     setupRegistrationPolicy(exporter);
/*  89 */     return exporter;
/*     */   }
/*     */   
/*     */   private void setupDomain(AnnotationMBeanExporter exporter) {
/*  93 */     String defaultDomain = this.enableMBeanExport.getString("defaultDomain");
/*  94 */     if ((defaultDomain != null) && (this.environment != null)) {
/*  95 */       defaultDomain = this.environment.resolvePlaceholders(defaultDomain);
/*     */     }
/*  97 */     if (StringUtils.hasText(defaultDomain)) {
/*  98 */       exporter.setDefaultDomain(defaultDomain);
/*     */     }
/*     */   }
/*     */   
/*     */   private void setupServer(AnnotationMBeanExporter exporter) {
/* 103 */     String server = this.enableMBeanExport.getString("server");
/* 104 */     if ((server != null) && (this.environment != null)) {
/* 105 */       server = this.environment.resolvePlaceholders(server);
/*     */     }
/* 107 */     if (StringUtils.hasText(server)) {
/* 108 */       exporter.setServer((MBeanServer)this.beanFactory.getBean(server, MBeanServer.class));
/*     */     }
/*     */     else {
/* 111 */       SpecificPlatform specificPlatform = SpecificPlatform.get();
/* 112 */       if (specificPlatform != null) {
/* 113 */         exporter.setServer(specificPlatform.getMBeanServer());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private void setupRegistrationPolicy(AnnotationMBeanExporter exporter) {
/* 119 */     RegistrationPolicy registrationPolicy = (RegistrationPolicy)this.enableMBeanExport.getEnum("registration");
/* 120 */     exporter.setRegistrationPolicy(registrationPolicy);
/*     */   }
/*     */   
/*     */ 
/*     */   public static abstract enum SpecificPlatform
/*     */   {
/* 126 */     WEBLOGIC("weblogic.management.Helper"), 
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
/* 138 */     WEBSPHERE("com.ibm.websphere.management.AdminServiceFactory");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private final String identifyingClass;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private SpecificPlatform(String identifyingClass)
/*     */     {
/* 150 */       this.identifyingClass = identifyingClass;
/*     */     }
/*     */     
/*     */     public abstract MBeanServer getMBeanServer();
/*     */     
/*     */     public static SpecificPlatform get() {
/* 156 */       ClassLoader classLoader = MBeanExportConfiguration.class.getClassLoader();
/* 157 */       for (SpecificPlatform environment : values()) {
/* 158 */         if (ClassUtils.isPresent(environment.identifyingClass, classLoader)) {
/* 159 */           return environment;
/*     */         }
/*     */       }
/* 162 */       return null;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-context-4.3.14.RELEASE.jar!\org\springframework\context\annotation\MBeanExportConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */