/*     */ package org.springframework.web.servlet.config.annotation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.BeanInitializationException;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.ViewResolver;
/*     */ import org.springframework.web.servlet.view.BeanNameViewResolver;
/*     */ import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
/*     */ import org.springframework.web.servlet.view.InternalResourceViewResolver;
/*     */ import org.springframework.web.servlet.view.UrlBasedViewResolver;
/*     */ import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
/*     */ import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;
/*     */ import org.springframework.web.servlet.view.groovy.GroovyMarkupConfigurer;
/*     */ import org.springframework.web.servlet.view.groovy.GroovyMarkupViewResolver;
/*     */ import org.springframework.web.servlet.view.script.ScriptTemplateConfigurer;
/*     */ import org.springframework.web.servlet.view.script.ScriptTemplateViewResolver;
/*     */ import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
/*     */ import org.springframework.web.servlet.view.tiles3.TilesViewResolver;
/*     */ import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
/*     */ import org.springframework.web.servlet.view.velocity.VelocityViewResolver;
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
/*     */ public class ViewResolverRegistry
/*     */ {
/*     */   private ContentNegotiationManager contentNegotiationManager;
/*     */   private ApplicationContext applicationContext;
/*     */   private ContentNegotiatingViewResolver contentNegotiatingResolver;
/*  62 */   private final List<ViewResolver> viewResolvers = new ArrayList(4);
/*     */   
/*     */ 
/*     */ 
/*     */   private Integer order;
/*     */   
/*     */ 
/*     */ 
/*     */   public ViewResolverRegistry(ContentNegotiationManager contentNegotiationManager, ApplicationContext context)
/*     */   {
/*  72 */     this.contentNegotiationManager = contentNegotiationManager;
/*  73 */     this.applicationContext = context;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public ViewResolverRegistry() {}
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean hasRegistrations()
/*     */   {
/*  85 */     return (this.contentNegotiatingResolver != null) || (!this.viewResolvers.isEmpty());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void enableContentNegotiation(View... defaultViews)
/*     */   {
/*  97 */     initContentNegotiatingViewResolver(defaultViews);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void enableContentNegotiation(boolean useNotAcceptableStatus, View... defaultViews)
/*     */   {
/* 109 */     initContentNegotiatingViewResolver(defaultViews);
/* 110 */     this.contentNegotiatingResolver.setUseNotAcceptableStatusCode(useNotAcceptableStatus);
/*     */   }
/*     */   
/*     */   private void initContentNegotiatingViewResolver(View[] defaultViews)
/*     */   {
/* 115 */     this.order = Integer.valueOf(this.order != null ? this.order.intValue() : Integer.MIN_VALUE);
/*     */     
/* 117 */     if (this.contentNegotiatingResolver != null) {
/* 118 */       if ((!ObjectUtils.isEmpty(defaultViews)) && 
/* 119 */         (!CollectionUtils.isEmpty(this.contentNegotiatingResolver.getDefaultViews()))) {
/* 120 */         List<View> views = new ArrayList(this.contentNegotiatingResolver.getDefaultViews());
/* 121 */         views.addAll(Arrays.asList(defaultViews));
/* 122 */         this.contentNegotiatingResolver.setDefaultViews(views);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 127 */       this.contentNegotiatingResolver = new ContentNegotiatingViewResolver();
/* 128 */       this.contentNegotiatingResolver.setDefaultViews(Arrays.asList(defaultViews));
/* 129 */       this.contentNegotiatingResolver.setViewResolvers(this.viewResolvers);
/* 130 */       this.contentNegotiatingResolver.setContentNegotiationManager(this.contentNegotiationManager);
/*     */     }
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
/*     */   public UrlBasedViewResolverRegistration jsp()
/*     */   {
/* 144 */     return jsp("/WEB-INF/", ".jsp");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlBasedViewResolverRegistration jsp(String prefix, String suffix)
/*     */   {
/* 156 */     InternalResourceViewResolver resolver = new InternalResourceViewResolver();
/* 157 */     resolver.setPrefix(prefix);
/* 158 */     resolver.setSuffix(suffix);
/* 159 */     this.viewResolvers.add(resolver);
/* 160 */     return new UrlBasedViewResolverRegistration(resolver);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlBasedViewResolverRegistration tiles()
/*     */   {
/* 169 */     if (!checkBeanOfType(TilesConfigurer.class)) {
/* 170 */       throw new BeanInitializationException("In addition to a Tiles view resolver there must also be a single TilesConfigurer bean in this web application context (or its parent).");
/*     */     }
/*     */     
/*     */ 
/* 174 */     TilesRegistration registration = new TilesRegistration();
/* 175 */     this.viewResolvers.add(registration.getViewResolver());
/* 176 */     return registration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlBasedViewResolverRegistration freeMarker()
/*     */   {
/* 186 */     if (!checkBeanOfType(FreeMarkerConfigurer.class)) {
/* 187 */       throw new BeanInitializationException("In addition to a FreeMarker view resolver there must also be a single FreeMarkerConfig bean in this web application context (or its parent): FreeMarkerConfigurer is the usual implementation. This bean may be given any name.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 192 */     FreeMarkerRegistration registration = new FreeMarkerRegistration();
/* 193 */     this.viewResolvers.add(registration.getViewResolver());
/* 194 */     return registration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public UrlBasedViewResolverRegistration velocity()
/*     */   {
/* 206 */     if (!checkBeanOfType(VelocityConfigurer.class)) {
/* 207 */       throw new BeanInitializationException("In addition to a Velocity view resolver there must also be a single VelocityConfig bean in this web application context (or its parent): VelocityConfigurer is the usual implementation. This bean may be given any name.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 212 */     VelocityRegistration registration = new VelocityRegistration();
/* 213 */     this.viewResolvers.add(registration.getViewResolver());
/* 214 */     return registration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlBasedViewResolverRegistration groovy()
/*     */   {
/* 222 */     if (!checkBeanOfType(GroovyMarkupConfigurer.class)) {
/* 223 */       throw new BeanInitializationException("In addition to a Groovy markup view resolver there must also be a single GroovyMarkupConfig bean in this web application context (or its parent): GroovyMarkupConfigurer is the usual implementation. This bean may be given any name.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 228 */     GroovyMarkupRegistration registration = new GroovyMarkupRegistration();
/* 229 */     this.viewResolvers.add(registration.getViewResolver());
/* 230 */     return registration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public UrlBasedViewResolverRegistration scriptTemplate()
/*     */   {
/* 238 */     if (!checkBeanOfType(ScriptTemplateConfigurer.class)) {
/* 239 */       throw new BeanInitializationException("In addition to a script template view resolver there must also be a single ScriptTemplateConfig bean in this web application context (or its parent): ScriptTemplateConfigurer is the usual implementation. This bean may be given any name.");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 244 */     ScriptRegistration registration = new ScriptRegistration();
/* 245 */     this.viewResolvers.add(registration.getViewResolver());
/* 246 */     return registration;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void beanName()
/*     */   {
/* 254 */     BeanNameViewResolver resolver = new BeanNameViewResolver();
/* 255 */     this.viewResolvers.add(resolver);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void viewResolver(ViewResolver viewResolver)
/*     */   {
/* 265 */     if ((viewResolver instanceof ContentNegotiatingViewResolver)) {
/* 266 */       throw new BeanInitializationException("addViewResolver cannot be used to configure a ContentNegotiatingViewResolver. Please use the method enableContentNegotiation instead.");
/*     */     }
/*     */     
/*     */ 
/* 270 */     this.viewResolvers.add(viewResolver);
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
/*     */ 
/*     */ 
/*     */   public void order(int order)
/*     */   {
/* 286 */     this.order = Integer.valueOf(order);
/*     */   }
/*     */   
/*     */   protected int getOrder()
/*     */   {
/* 291 */     return this.order != null ? this.order.intValue() : Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */   protected List<ViewResolver> getViewResolvers() {
/* 295 */     if (this.contentNegotiatingResolver != null) {
/* 296 */       return Collections.singletonList(this.contentNegotiatingResolver);
/*     */     }
/*     */     
/* 299 */     return this.viewResolvers;
/*     */   }
/*     */   
/*     */   private boolean checkBeanOfType(Class<?> beanType)
/*     */   {
/* 304 */     return (this.applicationContext == null) || 
/* 305 */       (!ObjectUtils.isEmpty(BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.applicationContext, beanType, false, false)));
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected boolean hasBeanOfType(Class<?> beanType)
/*     */   {
/* 311 */     return !ObjectUtils.isEmpty(BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.applicationContext, beanType, false, false));
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager)
/*     */   {
/* 317 */     this.contentNegotiationManager = contentNegotiationManager;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   protected void setApplicationContext(ApplicationContext applicationContext) {
/* 322 */     this.applicationContext = applicationContext;
/*     */   }
/*     */   
/*     */   private static class TilesRegistration extends UrlBasedViewResolverRegistration
/*     */   {
/*     */     public TilesRegistration()
/*     */     {
/* 329 */       super();
/*     */     }
/*     */   }
/*     */   
/*     */   private static class VelocityRegistration
/*     */     extends UrlBasedViewResolverRegistration
/*     */   {
/*     */     public VelocityRegistration()
/*     */     {
/* 338 */       super();
/* 339 */       getViewResolver().setSuffix(".vm");
/*     */     }
/*     */   }
/*     */   
/*     */   private static class FreeMarkerRegistration extends UrlBasedViewResolverRegistration
/*     */   {
/*     */     public FreeMarkerRegistration()
/*     */     {
/* 347 */       super();
/* 348 */       getViewResolver().setSuffix(".ftl");
/*     */     }
/*     */   }
/*     */   
/*     */   private static class GroovyMarkupRegistration extends UrlBasedViewResolverRegistration
/*     */   {
/*     */     public GroovyMarkupRegistration()
/*     */     {
/* 356 */       super();
/* 357 */       getViewResolver().setSuffix(".tpl");
/*     */     }
/*     */   }
/*     */   
/*     */   private static class ScriptRegistration extends UrlBasedViewResolverRegistration
/*     */   {
/*     */     public ScriptRegistration()
/*     */     {
/* 365 */       super();
/* 366 */       getViewResolver();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\config\annotation\ViewResolverRegistry.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */