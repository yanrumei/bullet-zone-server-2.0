/*    */ package org.springframework.boot.autoconfigure.thymeleaf;
/*    */ 
/*    */ import java.nio.charset.Charset;
/*    */ import javax.annotation.PostConstruct;
/*    */ import org.apache.commons.logging.Log;
/*    */ import org.apache.commons.logging.LogFactory;
/*    */ import org.springframework.boot.autoconfigure.template.TemplateLocation;
/*    */ import org.springframework.context.ApplicationContext;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
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
/*    */ abstract class AbstractTemplateResolverConfiguration
/*    */ {
/* 38 */   private static final Log logger = LogFactory.getLog(AbstractTemplateResolverConfiguration.class);
/*    */   
/*    */   private final ThymeleafProperties properties;
/*    */   
/*    */   private final ApplicationContext applicationContext;
/*    */   
/*    */   AbstractTemplateResolverConfiguration(ThymeleafProperties properties, ApplicationContext applicationContext)
/*    */   {
/* 46 */     this.properties = properties;
/* 47 */     this.applicationContext = applicationContext;
/*    */   }
/*    */   
/*    */   protected final ThymeleafProperties getProperties() {
/* 51 */     return this.properties;
/*    */   }
/*    */   
/*    */   @PostConstruct
/*    */   public void checkTemplateLocationExists() {
/* 56 */     boolean checkTemplateLocation = this.properties.isCheckTemplateLocation();
/* 57 */     if (checkTemplateLocation) {
/* 58 */       TemplateLocation location = new TemplateLocation(this.properties.getPrefix());
/* 59 */       if (!location.exists(this.applicationContext)) {
/* 60 */         logger.warn("Cannot find template location: " + location + " (please add some templates or check your Thymeleaf configuration)");
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   @Bean
/*    */   public SpringResourceTemplateResolver defaultTemplateResolver()
/*    */   {
/* 69 */     SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
/* 70 */     resolver.setApplicationContext(this.applicationContext);
/* 71 */     resolver.setPrefix(this.properties.getPrefix());
/* 72 */     resolver.setSuffix(this.properties.getSuffix());
/* 73 */     resolver.setTemplateMode(this.properties.getMode());
/* 74 */     if (this.properties.getEncoding() != null) {
/* 75 */       resolver.setCharacterEncoding(this.properties.getEncoding().name());
/*    */     }
/* 77 */     resolver.setCacheable(this.properties.isCache());
/* 78 */     Integer order = this.properties.getTemplateResolverOrder();
/* 79 */     if (order != null) {
/* 80 */       resolver.setOrder(order);
/*    */     }
/* 82 */     return resolver;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\thymeleaf\AbstractTemplateResolverConfiguration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */