/*     */ package org.springframework.web.servlet.view;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.PatternMatchUtils;
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
/*     */ public class UrlBasedViewResolver
/*     */   extends AbstractCachingViewResolver
/*     */   implements Ordered
/*     */ {
/*     */   public static final String REDIRECT_URL_PREFIX = "redirect:";
/*     */   public static final String FORWARD_URL_PREFIX = "forward:";
/*     */   private Class<?> viewClass;
/* 106 */   private String prefix = "";
/*     */   
/* 108 */   private String suffix = "";
/*     */   
/*     */   private String contentType;
/*     */   
/* 112 */   private boolean redirectContextRelative = true;
/*     */   
/* 114 */   private boolean redirectHttp10Compatible = true;
/*     */   
/*     */ 
/*     */   private String[] redirectHosts;
/*     */   
/*     */   private String requestContextAttribute;
/*     */   
/* 121 */   private final Map<String, Object> staticAttributes = new HashMap();
/*     */   
/*     */   private Boolean exposePathVariables;
/*     */   
/*     */   private Boolean exposeContextBeansAsAttributes;
/*     */   
/*     */   private String[] exposedContextBeanNames;
/*     */   
/*     */   private String[] viewNames;
/*     */   
/* 131 */   private int order = Integer.MAX_VALUE;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setViewClass(Class<?> viewClass)
/*     */   {
/* 141 */     if ((viewClass == null) || (!requiredViewClass().isAssignableFrom(viewClass)))
/*     */     {
/*     */ 
/* 144 */       throw new IllegalArgumentException("Given view class [" + (viewClass != null ? viewClass.getName() : null) + "] is not of type [" + requiredViewClass().getName() + "]");
/*     */     }
/* 146 */     this.viewClass = viewClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Class<?> getViewClass()
/*     */   {
/* 153 */     return this.viewClass;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Class<?> requiredViewClass()
/*     */   {
/* 162 */     return AbstractUrlBasedView.class;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setPrefix(String prefix)
/*     */   {
/* 169 */     this.prefix = (prefix != null ? prefix : "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getPrefix()
/*     */   {
/* 176 */     return this.prefix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setSuffix(String suffix)
/*     */   {
/* 183 */     this.suffix = (suffix != null ? suffix : "");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getSuffix()
/*     */   {
/* 190 */     return this.suffix;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentType(String contentType)
/*     */   {
/* 199 */     this.contentType = contentType;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getContentType()
/*     */   {
/* 206 */     return this.contentType;
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
/*     */   public void setRedirectContextRelative(boolean redirectContextRelative)
/*     */   {
/* 222 */     this.redirectContextRelative = redirectContextRelative;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isRedirectContextRelative()
/*     */   {
/* 231 */     return this.redirectContextRelative;
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
/*     */   public void setRedirectHttp10Compatible(boolean redirectHttp10Compatible)
/*     */   {
/* 249 */     this.redirectHttp10Compatible = redirectHttp10Compatible;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected boolean isRedirectHttp10Compatible()
/*     */   {
/* 256 */     return this.redirectHttp10Compatible;
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
/*     */   public void setRedirectHosts(String... redirectHosts)
/*     */   {
/* 270 */     this.redirectHosts = redirectHosts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public String[] getRedirectHosts()
/*     */   {
/* 278 */     return this.redirectHosts;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRequestContextAttribute(String requestContextAttribute)
/*     */   {
/* 287 */     this.requestContextAttribute = requestContextAttribute;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected String getRequestContextAttribute()
/*     */   {
/* 294 */     return this.requestContextAttribute;
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
/*     */   public void setAttributes(Properties props)
/*     */   {
/* 309 */     CollectionUtils.mergePropertiesIntoMap(props, this.staticAttributes);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAttributesMap(Map<String, ?> attributes)
/*     */   {
/* 320 */     if (attributes != null) {
/* 321 */       this.staticAttributes.putAll(attributes);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<String, Object> getAttributesMap()
/*     */   {
/* 333 */     return this.staticAttributes;
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
/*     */   public void setExposePathVariables(Boolean exposePathVariables)
/*     */   {
/* 349 */     this.exposePathVariables = exposePathVariables;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected Boolean getExposePathVariables()
/*     */   {
/* 356 */     return this.exposePathVariables;
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
/*     */   public void setExposeContextBeansAsAttributes(boolean exposeContextBeansAsAttributes)
/*     */   {
/* 369 */     this.exposeContextBeansAsAttributes = Boolean.valueOf(exposeContextBeansAsAttributes);
/*     */   }
/*     */   
/*     */   protected Boolean getExposeContextBeansAsAttributes() {
/* 373 */     return this.exposeContextBeansAsAttributes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setExposedContextBeanNames(String... exposedContextBeanNames)
/*     */   {
/* 383 */     this.exposedContextBeanNames = exposedContextBeanNames;
/*     */   }
/*     */   
/*     */   protected String[] getExposedContextBeanNames() {
/* 387 */     return this.exposedContextBeanNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setViewNames(String... viewNames)
/*     */   {
/* 398 */     this.viewNames = viewNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String[] getViewNames()
/*     */   {
/* 406 */     return this.viewNames;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setOrder(int order)
/*     */   {
/* 414 */     this.order = order;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getOrder()
/*     */   {
/* 423 */     return this.order;
/*     */   }
/*     */   
/*     */   protected void initApplicationContext()
/*     */   {
/* 428 */     super.initApplicationContext();
/* 429 */     if (getViewClass() == null) {
/* 430 */       throw new IllegalArgumentException("Property 'viewClass' is required");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object getCacheKey(String viewName, Locale locale)
/*     */   {
/* 441 */     return viewName;
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
/*     */   protected View createView(String viewName, Locale locale)
/*     */     throws Exception
/*     */   {
/* 456 */     if (!canHandle(viewName, locale)) {
/* 457 */       return null;
/*     */     }
/*     */     
/* 460 */     if (viewName.startsWith("redirect:")) {
/* 461 */       String redirectUrl = viewName.substring("redirect:".length());
/* 462 */       RedirectView view = new RedirectView(redirectUrl, isRedirectContextRelative(), isRedirectHttp10Compatible());
/* 463 */       view.setHosts(getRedirectHosts());
/* 464 */       return applyLifecycleMethods(viewName, view);
/*     */     }
/*     */     
/* 467 */     if (viewName.startsWith("forward:")) {
/* 468 */       String forwardUrl = viewName.substring("forward:".length());
/* 469 */       return new InternalResourceView(forwardUrl);
/*     */     }
/*     */     
/* 472 */     return super.createView(viewName, locale);
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
/*     */   protected boolean canHandle(String viewName, Locale locale)
/*     */   {
/* 486 */     String[] viewNames = getViewNames();
/* 487 */     return (viewNames == null) || (PatternMatchUtils.simpleMatch(viewNames, viewName));
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
/*     */   protected View loadView(String viewName, Locale locale)
/*     */     throws Exception
/*     */   {
/* 507 */     AbstractUrlBasedView view = buildView(viewName);
/* 508 */     View result = applyLifecycleMethods(viewName, view);
/* 509 */     return view.checkResource(locale) ? result : null;
/*     */   }
/*     */   
/*     */   private View applyLifecycleMethods(String viewName, AbstractView view) {
/* 513 */     return (View)getApplicationContext().getAutowireCapableBeanFactory().initializeBean(view, viewName);
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
/*     */   protected AbstractUrlBasedView buildView(String viewName)
/*     */     throws Exception
/*     */   {
/* 531 */     AbstractUrlBasedView view = (AbstractUrlBasedView)BeanUtils.instantiateClass(getViewClass());
/* 532 */     view.setUrl(getPrefix() + viewName + getSuffix());
/*     */     
/* 534 */     String contentType = getContentType();
/* 535 */     if (contentType != null) {
/* 536 */       view.setContentType(contentType);
/*     */     }
/*     */     
/* 539 */     view.setRequestContextAttribute(getRequestContextAttribute());
/* 540 */     view.setAttributesMap(getAttributesMap());
/*     */     
/* 542 */     Boolean exposePathVariables = getExposePathVariables();
/* 543 */     if (exposePathVariables != null) {
/* 544 */       view.setExposePathVariables(exposePathVariables.booleanValue());
/*     */     }
/* 546 */     Boolean exposeContextBeansAsAttributes = getExposeContextBeansAsAttributes();
/* 547 */     if (exposeContextBeansAsAttributes != null) {
/* 548 */       view.setExposeContextBeansAsAttributes(exposeContextBeansAsAttributes.booleanValue());
/*     */     }
/* 550 */     String[] exposedContextBeanNames = getExposedContextBeanNames();
/* 551 */     if (exposedContextBeanNames != null) {
/* 552 */       view.setExposedContextBeanNames(exposedContextBeanNames);
/*     */     }
/*     */     
/* 555 */     return view;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\view\UrlBasedViewResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */