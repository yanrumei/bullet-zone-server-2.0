/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.ServletContext;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.web.HttpRequestHandler;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.servlet.handler.AbstractHandlerMapping;
/*     */ import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
/*     */ import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
/*     */ import org.springframework.web.util.UrlPathHelper;
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
/*     */ public class ResourceHandlerRegistry
/*     */ {
/*     */   private final ServletContext servletContext;
/*     */   private final ApplicationContext applicationContext;
/*     */   private final ContentNegotiationManager contentNegotiationManager;
/*     */   private final UrlPathHelper pathHelper;
/*  63 */   private final List<ResourceHandlerRegistration> registrations = new ArrayList();
/*     */   
/*  65 */   private int order = 2147483646;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceHandlerRegistry(ApplicationContext applicationContext, ServletContext servletContext)
/*     */   {
/*  74 */     this(applicationContext, servletContext, null);
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
/*     */   public ResourceHandlerRegistry(ApplicationContext applicationContext, ServletContext servletContext, ContentNegotiationManager contentNegotiationManager)
/*     */   {
/*  87 */     this(applicationContext, servletContext, contentNegotiationManager, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceHandlerRegistry(ApplicationContext applicationContext, ServletContext servletContext, ContentNegotiationManager contentNegotiationManager, UrlPathHelper pathHelper)
/*     */   {
/*  99 */     Assert.notNull(applicationContext, "ApplicationContext is required");
/* 100 */     this.applicationContext = applicationContext;
/* 101 */     this.servletContext = servletContext;
/* 102 */     this.contentNegotiationManager = contentNegotiationManager;
/* 103 */     this.pathHelper = pathHelper;
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
/*     */   public ResourceHandlerRegistration addResourceHandler(String... pathPatterns)
/*     */   {
/* 117 */     ResourceHandlerRegistration registration = new ResourceHandlerRegistration(pathPatterns);
/* 118 */     this.registrations.add(registration);
/* 119 */     return registration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasMappingForPattern(String pathPattern)
/*     */   {
/* 126 */     for (ResourceHandlerRegistration registration : this.registrations) {
/* 127 */       if (Arrays.asList(registration.getPathPatterns()).contains(pathPattern)) {
/* 128 */         return true;
/*     */       }
/*     */     }
/* 131 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public ResourceHandlerRegistry setOrder(int order)
/*     */   {
/* 140 */     this.order = order;
/* 141 */     return this;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected AbstractHandlerMapping getHandlerMapping()
/*     */   {
/* 149 */     if (this.registrations.isEmpty()) {
/* 150 */       return null;
/*     */     }
/*     */     
/* 153 */     Map<String, HttpRequestHandler> urlMap = new LinkedHashMap();
/* 154 */     for (ResourceHandlerRegistration registration : this.registrations) {
/* 155 */       for (String pathPattern : registration.getPathPatterns()) {
/* 156 */         ResourceHttpRequestHandler handler = registration.getRequestHandler();
/* 157 */         if (this.pathHelper != null) {
/* 158 */           handler.setUrlPathHelper(this.pathHelper);
/*     */         }
/* 160 */         if (this.contentNegotiationManager != null) {
/* 161 */           handler.setContentNegotiationManager(this.contentNegotiationManager);
/*     */         }
/* 163 */         handler.setServletContext(this.servletContext);
/* 164 */         handler.setApplicationContext(this.applicationContext);
/*     */         try {
/* 166 */           handler.afterPropertiesSet();
/*     */         }
/*     */         catch (Throwable ex) {
/* 169 */           throw new BeanInitializationException("Failed to init ResourceHttpRequestHandler", ex);
/*     */         }
/* 171 */         urlMap.put(pathPattern, handler);
/*     */       }
/*     */     }
/*     */     
/* 175 */     SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
/* 176 */     handlerMapping.setOrder(this.order);
/* 177 */     handlerMapping.setUrlMap(urlMap);
/* 178 */     return handlerMapping;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\ResourceHandlerRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */