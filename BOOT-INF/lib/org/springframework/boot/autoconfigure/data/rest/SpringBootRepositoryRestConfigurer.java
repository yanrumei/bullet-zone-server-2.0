/*    */ package org.springframework.boot.autoconfigure.data.rest;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.core.annotation.Order;
/*    */ import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
/*    */ import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
/*    */ import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
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
/*    */ @Order(0)
/*    */ class SpringBootRepositoryRestConfigurer
/*    */   extends RepositoryRestConfigurerAdapter
/*    */ {
/*    */   @Autowired(required=false)
/*    */   private Jackson2ObjectMapperBuilder objectMapperBuilder;
/*    */   @Autowired
/*    */   private RepositoryRestProperties properties;
/*    */   
/*    */   public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config)
/*    */   {
/* 47 */     this.properties.applyTo(config);
/*    */   }
/*    */   
/*    */   public void configureJacksonObjectMapper(ObjectMapper objectMapper)
/*    */   {
/* 52 */     if (this.objectMapperBuilder != null) {
/* 53 */       this.objectMapperBuilder.configure(objectMapper);
/*    */     }
/*    */   }
/*    */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\data\rest\SpringBootRepositoryRestConfigurer.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */