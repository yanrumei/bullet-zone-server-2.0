/*    */ package org.springframework.boot.autoconfigure.admin;
/*    */ 
/*    */ import java.util.List;
/*    */ import javax.management.MalformedObjectNameException;
/*    */ import org.springframework.beans.factory.ObjectProvider;
/*    */ import org.springframework.boot.admin.SpringApplicationAdminMXBeanRegistrar;
/*    */ import org.springframework.boot.autoconfigure.AutoConfigureAfter;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
/*    */ import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
/*    */ import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.core.env.Environment;
/*    */ import org.springframework.jmx.export.MBeanExporter;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @AutoConfigureAfter({JmxAutoConfiguration.class})
/*    */ @ConditionalOnProperty(prefix="spring.application.admin", value={"enabled"}, havingValue="true", matchIfMissing=false)
/*    */ public class SpringApplicationAdminJmxAutoConfiguration
/*    */ {
/*    */   private static final String JMX_NAME_PROPERTY = "spring.application.admin.jmx-name";
/*    */   private static final String DEFAULT_JMX_NAME = "org.springframework.boot:type=Admin,name=SpringApplication";
/*    */   private final List<MBeanExporter> mbeanExporters;
/*    */   private final Environment environment;
/*    */   
/*    */   public SpringApplicationAdminJmxAutoConfiguration(ObjectProvider<List<MBeanExporter>> mbeanExporters, Environment environment)
/*    */   {
/* 66 */     this.mbeanExporters = ((List)mbeanExporters.getIfAvailable());
/* 67 */     this.environment = environment;
/*    */   }
/*    */   
/*    */   @Bean
/*    */   @ConditionalOnMissingBean
/*    */   public SpringApplicationAdminMXBeanRegistrar springApplicationAdminRegistrar() throws MalformedObjectNameException
/*    */   {
/* 74 */     String jmxName = this.environment.getProperty("spring.application.admin.jmx-name", "org.springframework.boot:type=Admin,name=SpringApplication");
/*    */     
/* 76 */     if (this.mbeanExporters != null) {
/* 77 */       for (MBeanExporter mbeanExporter : this.mbeanExporters) {
/* 78 */         mbeanExporter.addExcludedBean(jmxName);
/*    */       }
/*    */     }
/* 81 */     return new SpringApplicationAdminMXBeanRegistrar(jmxName);
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\admin\SpringApplicationAdminJmxAutoConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */