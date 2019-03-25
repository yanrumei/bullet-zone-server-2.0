/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import org.springframework.web.cors.CorsConfiguration;
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
/*     */ 
/*     */ 
/*     */ public class CorsRegistration
/*     */ {
/*     */   private final String pathPattern;
/*     */   private final CorsConfiguration config;
/*     */   
/*     */   public CorsRegistration(String pathPattern)
/*     */   {
/*  53 */     this.pathPattern = pathPattern;
/*     */     
/*  55 */     this.config = new CorsConfiguration().applyPermitDefaultValues();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CorsRegistration allowedOrigins(String... origins)
/*     */   {
/*  65 */     this.config.setAllowedOrigins(Arrays.asList(origins));
/*  66 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CorsRegistration allowedMethods(String... methods)
/*     */   {
/*  77 */     this.config.setAllowedMethods(Arrays.asList(methods));
/*  78 */     return this;
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
/*     */   public CorsRegistration allowedHeaders(String... headers)
/*     */   {
/*  91 */     this.config.setAllowedHeaders(Arrays.asList(headers));
/*  92 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CorsRegistration exposedHeaders(String... headers)
/*     */   {
/* 104 */     this.config.setExposedHeaders(Arrays.asList(headers));
/* 105 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CorsRegistration maxAge(long maxAge)
/*     */   {
/* 114 */     this.config.setMaxAge(Long.valueOf(maxAge));
/* 115 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public CorsRegistration allowCredentials(boolean allowCredentials)
/*     */   {
/* 124 */     this.config.setAllowCredentials(Boolean.valueOf(allowCredentials));
/* 125 */     return this;
/*     */   }
/*     */   
/*     */   protected String getPathPattern() {
/* 129 */     return this.pathPattern;
/*     */   }
/*     */   
/*     */   protected CorsConfiguration getCorsConfiguration() {
/* 133 */     return this.config;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\CorsRegistration.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */