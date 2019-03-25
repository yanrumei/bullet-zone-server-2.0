/*     */ package org.springframework.web.servlet.view.velocity;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.TimeZone;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.velocity.Template;
/*     */ import org.apache.velocity.VelocityContext;
/*     */ import org.apache.velocity.app.VelocityEngine;
/*     */ import org.apache.velocity.context.Context;
/*     */ import org.apache.velocity.exception.MethodInvocationException;
/*     */ import org.apache.velocity.exception.ResourceNotFoundException;
/*     */ import org.apache.velocity.tools.generic.DateTool;
/*     */ import org.apache.velocity.tools.generic.NumberTool;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.context.ApplicationContextException;
/*     */ import org.springframework.core.NestedIOException;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
/*     */ import org.springframework.web.servlet.view.AbstractTemplateView;
/*     */ import org.springframework.web.util.NestedServletException;
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
/*     */ @Deprecated
/*     */ public class VelocityView
/*     */   extends AbstractTemplateView
/*     */ {
/*     */   private Map<String, Class<?>> toolAttributes;
/*     */   private String dateToolAttribute;
/*     */   private String numberToolAttribute;
/*     */   private String encoding;
/*  99 */   private boolean cacheTemplate = false;
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
/*     */   private VelocityEngine velocityEngine;
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
/*     */   private Template template;
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
/*     */   public void setToolAttributes(Map<String, Class<?>> toolAttributes)
/*     */   {
/* 134 */     this.toolAttributes = toolAttributes;
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
/*     */   public void setDateToolAttribute(String dateToolAttribute)
/*     */   {
/* 148 */     this.dateToolAttribute = dateToolAttribute;
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
/*     */   public void setNumberToolAttribute(String numberToolAttribute)
/*     */   {
/* 162 */     this.numberToolAttribute = numberToolAttribute;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setEncoding(String encoding)
/*     */   {
/* 172 */     this.encoding = encoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getEncoding()
/*     */   {
/* 179 */     return this.encoding;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCacheTemplate(boolean cacheTemplate)
/*     */   {
/* 190 */     this.cacheTemplate = cacheTemplate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isCacheTemplate()
/*     */   {
/* 197 */     return this.cacheTemplate;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setVelocityEngine(VelocityEngine velocityEngine)
/*     */   {
/* 207 */     this.velocityEngine = velocityEngine;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected VelocityEngine getVelocityEngine()
/*     */   {
/* 214 */     return this.velocityEngine;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initApplicationContext()
/*     */     throws BeansException
/*     */   {
/* 224 */     super.initApplicationContext();
/*     */     
/* 226 */     if (getVelocityEngine() == null)
/*     */     {
/* 228 */       setVelocityEngine(autodetectVelocityEngine());
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected VelocityEngine autodetectVelocityEngine()
/*     */     throws BeansException
/*     */   {
/*     */     try
/*     */     {
/* 242 */       VelocityConfig velocityConfig = (VelocityConfig)BeanFactoryUtils.beanOfTypeIncludingAncestors(
/* 243 */         getApplicationContext(), VelocityConfig.class, true, false);
/* 244 */       return velocityConfig.getVelocityEngine();
/*     */     }
/*     */     catch (NoSuchBeanDefinitionException ex) {
/* 247 */       throw new ApplicationContextException("Must define a single VelocityConfig bean in this web application context (may be inherited): VelocityConfigurer is the usual implementation. This bean may be given any name.", ex);
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
/*     */   public boolean checkResource(Locale locale)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 263 */       this.template = getTemplate(getUrl());
/* 264 */       return true;
/*     */     }
/*     */     catch (ResourceNotFoundException ex) {
/* 267 */       if (this.logger.isDebugEnabled()) {
/* 268 */         this.logger.debug("No Velocity view found for URL: " + getUrl());
/*     */       }
/* 270 */       return false;
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 274 */       throw new NestedIOException("Could not load Velocity template for URL [" + getUrl() + "]", ex);
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
/*     */   protected void renderMergedTemplateModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 288 */     exposeHelpers(model, request);
/*     */     
/* 290 */     Context velocityContext = createVelocityContext(model, request, response);
/* 291 */     exposeHelpers(velocityContext, request, response);
/* 292 */     exposeToolAttributes(velocityContext, request);
/*     */     
/* 294 */     doRender(velocityContext, response);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request)
/*     */     throws Exception
/*     */   {}
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
/*     */   protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 333 */     return createVelocityContext(model);
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
/*     */   protected Context createVelocityContext(Map<String, Object> model)
/*     */     throws Exception
/*     */   {
/* 348 */     return new VelocityContext(model);
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
/*     */ 
/*     */ 
/*     */   protected void exposeHelpers(Context velocityContext, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 367 */     exposeHelpers(velocityContext, request);
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
/*     */   protected void exposeHelpers(Context velocityContext, HttpServletRequest request)
/*     */     throws Exception
/*     */   {}
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
/*     */   protected void exposeToolAttributes(Context velocityContext, HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/* 397 */     if (this.toolAttributes != null) {
/* 398 */       for (Map.Entry<String, Class<?>> entry : this.toolAttributes.entrySet()) {
/* 399 */         String attributeName = (String)entry.getKey();
/* 400 */         Class<?> toolClass = (Class)entry.getValue();
/*     */         try {
/* 402 */           Object tool = toolClass.newInstance();
/* 403 */           initTool(tool, velocityContext);
/* 404 */           velocityContext.put(attributeName, tool);
/*     */         }
/*     */         catch (Exception ex) {
/* 407 */           throw new NestedServletException("Could not instantiate Velocity tool '" + attributeName + "'", ex);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 413 */     if ((this.dateToolAttribute != null) || (this.numberToolAttribute != null)) {
/* 414 */       if (this.dateToolAttribute != null) {
/* 415 */         velocityContext.put(this.dateToolAttribute, new LocaleAwareDateTool(request));
/*     */       }
/* 417 */       if (this.numberToolAttribute != null) {
/* 418 */         velocityContext.put(this.numberToolAttribute, new LocaleAwareNumberTool(request));
/*     */       }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initTool(Object tool, Context velocityContext)
/*     */     throws Exception
/*     */   {}
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
/*     */   protected void doRender(Context context, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 461 */     if (this.logger.isDebugEnabled()) {
/* 462 */       this.logger.debug("Rendering Velocity template [" + getUrl() + "] in VelocityView '" + getBeanName() + "'");
/*     */     }
/* 464 */     mergeTemplate(getTemplate(), context, response);
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
/*     */ 
/*     */   protected Template getTemplate()
/*     */     throws Exception
/*     */   {
/* 482 */     if ((isCacheTemplate()) && (this.template != null)) {
/* 483 */       return this.template;
/*     */     }
/*     */     
/* 486 */     return getTemplate(getUrl());
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
/*     */   protected Template getTemplate(String name)
/*     */     throws Exception
/*     */   {
/* 501 */     return getEncoding() != null ? 
/* 502 */       getVelocityEngine().getTemplate(name, getEncoding()) : 
/* 503 */       getVelocityEngine().getTemplate(name);
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
/*     */   protected void mergeTemplate(Template template, Context context, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 519 */       template.merge(context, response.getWriter());
/*     */     }
/*     */     catch (MethodInvocationException ex) {
/* 522 */       Throwable cause = ex.getWrappedThrowable();
/*     */       
/*     */ 
/*     */ 
/* 526 */       throw new NestedServletException("Method invocation failed during rendering of Velocity view with name '" + getBeanName() + "': " + ex.getMessage() + "; reference [" + ex.getReferenceName() + "], method '" + ex.getMethodName() + "'", cause == null ? ex : cause);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static class LocaleAwareDateTool
/*     */     extends DateTool
/*     */   {
/*     */     private final HttpServletRequest request;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public LocaleAwareDateTool(HttpServletRequest request)
/*     */     {
/* 543 */       this.request = request;
/*     */     }
/*     */     
/*     */     public Locale getLocale()
/*     */     {
/* 548 */       return RequestContextUtils.getLocale(this.request);
/*     */     }
/*     */     
/*     */     public TimeZone getTimeZone()
/*     */     {
/* 553 */       TimeZone timeZone = RequestContextUtils.getTimeZone(this.request);
/* 554 */       return timeZone != null ? timeZone : super.getTimeZone();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class LocaleAwareNumberTool
/*     */     extends NumberTool
/*     */   {
/*     */     private final HttpServletRequest request;
/*     */     
/*     */ 
/*     */ 
/*     */     public LocaleAwareNumberTool(HttpServletRequest request)
/*     */     {
/* 569 */       this.request = request;
/*     */     }
/*     */     
/*     */     public Locale getLocale()
/*     */     {
/* 574 */       return RequestContextUtils.getLocale(this.request);
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\velocity\VelocityView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */