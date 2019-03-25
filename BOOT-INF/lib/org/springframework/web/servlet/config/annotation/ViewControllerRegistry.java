/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.web.servlet.handler.AbstractHandlerMapping;
/*     */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*     */ import org.springframework.web.servlet.mvc.ParameterizableViewController;
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
/*     */ public class ViewControllerRegistry
/*     */ {
/*     */   private ApplicationContext applicationContext;
/*  41 */   private final List<ViewControllerRegistration> registrations = new ArrayList(4);
/*     */   
/*  43 */   private final List<RedirectViewControllerRegistration> redirectRegistrations = new ArrayList(10);
/*     */   
/*     */ 
/*  46 */   private int order = 1;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ViewControllerRegistry(ApplicationContext applicationContext)
/*     */   {
/*  54 */     this.applicationContext = applicationContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public ViewControllerRegistry() {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ViewControllerRegistration addViewController(String urlPath)
/*     */   {
/*  70 */     ViewControllerRegistration registration = new ViewControllerRegistration(urlPath);
/*  71 */     registration.setApplicationContext(this.applicationContext);
/*  72 */     this.registrations.add(registration);
/*  73 */     return registration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public RedirectViewControllerRegistration addRedirectViewController(String urlPath, String redirectUrl)
/*     */   {
/*  83 */     RedirectViewControllerRegistration registration = new RedirectViewControllerRegistration(urlPath, redirectUrl);
/*  84 */     registration.setApplicationContext(this.applicationContext);
/*  85 */     this.redirectRegistrations.add(registration);
/*  86 */     return registration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addStatusController(String urlPath, HttpStatus statusCode)
/*     */   {
/*  95 */     ViewControllerRegistration registration = new ViewControllerRegistration(urlPath);
/*  96 */     registration.setApplicationContext(this.applicationContext);
/*  97 */     registration.setStatusCode(statusCode);
/*  98 */     registration.getViewController().setStatusOnly(true);
/*  99 */     this.registrations.add(registration);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOrder(int order)
/*     */   {
/* 109 */     this.order = order;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected SimpleUrlHandlerMapping buildHandlerMapping()
/*     */   {
/* 119 */     if ((this.registrations.isEmpty()) && (this.redirectRegistrations.isEmpty())) {
/* 120 */       return null;
/*     */     }
/*     */     
/* 123 */     Map<String, Object> urlMap = new LinkedHashMap();
/* 124 */     for (ViewControllerRegistration registration : this.registrations) {
/* 125 */       urlMap.put(registration.getUrlPath(), registration.getViewController());
/*     */     }
/* 127 */     for (RedirectViewControllerRegistration registration : this.redirectRegistrations) {
/* 128 */       urlMap.put(registration.getUrlPath(), registration.getViewController());
/*     */     }
/*     */     
/* 131 */     SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
/* 132 */     handlerMapping.setUrlMap(urlMap);
/* 133 */     handlerMapping.setOrder(this.order);
/* 134 */     return handlerMapping;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   protected AbstractHandlerMapping getHandlerMapping()
/*     */   {
/* 142 */     return buildHandlerMapping();
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected void setApplicationContext(ApplicationContext applicationContext) {
/* 147 */     this.applicationContext = applicationContext;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\ViewControllerRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */