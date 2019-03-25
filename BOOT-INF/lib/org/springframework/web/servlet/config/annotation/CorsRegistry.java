/*    */ package org.springframework.web.servlet.config.annotation;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.springframework.web.cors.CorsConfiguration;
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
/*    */ public class CorsRegistry
/*    */ {
/* 36 */   private final List<CorsRegistration> registrations = new ArrayList();
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
/*    */   public CorsRegistration addMapping(String pathPattern)
/*    */   {
/* 51 */     CorsRegistration registration = new CorsRegistration(pathPattern);
/* 52 */     this.registrations.add(registration);
/* 53 */     return registration;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   protected Map<String, CorsConfiguration> getCorsConfigurations()
/*    */   {
/* 61 */     Map<String, CorsConfiguration> configs = new LinkedHashMap(this.registrations.size());
/* 62 */     for (CorsRegistration registration : this.registrations) {
/* 63 */       configs.put(registration.getPathPattern(), registration.getCorsConfiguration());
/*    */     }
/* 65 */     return configs;
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\CorsRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */