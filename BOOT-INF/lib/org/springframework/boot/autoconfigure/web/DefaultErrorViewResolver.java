/*     */ package org.springframework.boot.autoconfigure.web;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProvider;
/*     */ import org.springframework.boot.autoconfigure.template.TemplateAvailabilityProviders;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.core.io.Resource;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.HttpStatus.Series;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.FileCopyUtils;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.View;
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
/*     */ public class DefaultErrorViewResolver
/*     */   implements ErrorViewResolver, Ordered
/*     */ {
/*     */   private static final Map<HttpStatus.Series, String> SERIES_VIEWS;
/*     */   private ApplicationContext applicationContext;
/*     */   private final ResourceProperties resourceProperties;
/*     */   private final TemplateAvailabilityProviders templateAvailabilityProviders;
/*     */   
/*     */   static
/*     */   {
/*  62 */     Map<HttpStatus.Series, String> views = new HashMap();
/*  63 */     views.put(HttpStatus.Series.CLIENT_ERROR, "4xx");
/*  64 */     views.put(HttpStatus.Series.SERVER_ERROR, "5xx");
/*  65 */     SERIES_VIEWS = Collections.unmodifiableMap(views);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  74 */   private int order = Integer.MAX_VALUE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public DefaultErrorViewResolver(ApplicationContext applicationContext, ResourceProperties resourceProperties)
/*     */   {
/*  83 */     Assert.notNull(applicationContext, "ApplicationContext must not be null");
/*  84 */     Assert.notNull(resourceProperties, "ResourceProperties must not be null");
/*  85 */     this.applicationContext = applicationContext;
/*  86 */     this.resourceProperties = resourceProperties;
/*  87 */     this.templateAvailabilityProviders = new TemplateAvailabilityProviders(applicationContext);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   DefaultErrorViewResolver(ApplicationContext applicationContext, ResourceProperties resourceProperties, TemplateAvailabilityProviders templateAvailabilityProviders)
/*     */   {
/*  94 */     Assert.notNull(applicationContext, "ApplicationContext must not be null");
/*  95 */     Assert.notNull(resourceProperties, "ResourceProperties must not be null");
/*  96 */     this.applicationContext = applicationContext;
/*  97 */     this.resourceProperties = resourceProperties;
/*  98 */     this.templateAvailabilityProviders = templateAvailabilityProviders;
/*     */   }
/*     */   
/*     */ 
/*     */   public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model)
/*     */   {
/* 104 */     ModelAndView modelAndView = resolve(String.valueOf(status), model);
/* 105 */     if ((modelAndView == null) && (SERIES_VIEWS.containsKey(status.series()))) {
/* 106 */       modelAndView = resolve((String)SERIES_VIEWS.get(status.series()), model);
/*     */     }
/* 108 */     return modelAndView;
/*     */   }
/*     */   
/*     */   private ModelAndView resolve(String viewName, Map<String, Object> model) {
/* 112 */     String errorViewName = "error/" + viewName;
/*     */     
/* 114 */     TemplateAvailabilityProvider provider = this.templateAvailabilityProviders.getProvider(errorViewName, this.applicationContext);
/* 115 */     if (provider != null) {
/* 116 */       return new ModelAndView(errorViewName, model);
/*     */     }
/* 118 */     return resolveResource(errorViewName, model);
/*     */   }
/*     */   
/*     */   private ModelAndView resolveResource(String viewName, Map<String, Object> model) {
/* 122 */     for (String location : this.resourceProperties.getStaticLocations()) {
/*     */       try {
/* 124 */         Resource resource = this.applicationContext.getResource(location);
/* 125 */         resource = resource.createRelative(viewName + ".html");
/* 126 */         if (resource.exists()) {
/* 127 */           return new ModelAndView(new HtmlResourceView(resource), model);
/*     */         }
/*     */       }
/*     */       catch (Exception localException) {}
/*     */     }
/*     */     
/* 133 */     return null;
/*     */   }
/*     */   
/*     */   public int getOrder()
/*     */   {
/* 138 */     return this.order;
/*     */   }
/*     */   
/*     */   public void setOrder(int order) {
/* 142 */     this.order = order;
/*     */   }
/*     */   
/*     */ 
/*     */   private static class HtmlResourceView
/*     */     implements View
/*     */   {
/*     */     private Resource resource;
/*     */     
/*     */     HtmlResourceView(Resource resource)
/*     */     {
/* 153 */       this.resource = resource;
/*     */     }
/*     */     
/*     */     public String getContentType()
/*     */     {
/* 158 */       return "text/html";
/*     */     }
/*     */     
/*     */     public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response)
/*     */       throws Exception
/*     */     {
/* 164 */       response.setContentType(getContentType());
/* 165 */       FileCopyUtils.copy(this.resource.getInputStream(), response
/* 166 */         .getOutputStream());
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-boot-autoconfigure-1.5.10.RELEASE.jar!\org\springframework\boot\autoconfigure\web\DefaultErrorViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */