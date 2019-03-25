/*      */ package org.springframework.web.servlet;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.Set;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryUtils;
/*      */ import org.springframework.beans.factory.BeanInitializationException;
/*      */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*      */ import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
/*      */ import org.springframework.context.ApplicationContext;
/*      */ import org.springframework.context.i18n.LocaleContext;
/*      */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*      */ import org.springframework.core.io.ClassPathResource;
/*      */ import org.springframework.core.io.support.PropertiesLoaderUtils;
/*      */ import org.springframework.http.HttpStatus;
/*      */ import org.springframework.http.server.ServletServerHttpRequest;
/*      */ import org.springframework.ui.context.ThemeSource;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.StringUtils;
/*      */ import org.springframework.web.context.WebApplicationContext;
/*      */ import org.springframework.web.context.request.ServletWebRequest;
/*      */ import org.springframework.web.context.request.async.WebAsyncManager;
/*      */ import org.springframework.web.context.request.async.WebAsyncUtils;
/*      */ import org.springframework.web.multipart.MultipartException;
/*      */ import org.springframework.web.multipart.MultipartHttpServletRequest;
/*      */ import org.springframework.web.multipart.MultipartResolver;
/*      */ import org.springframework.web.util.NestedServletException;
/*      */ import org.springframework.web.util.WebUtils;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class DispatcherServlet
/*      */   extends FrameworkServlet
/*      */ {
/*      */   public static final String MULTIPART_RESOLVER_BEAN_NAME = "multipartResolver";
/*      */   public static final String LOCALE_RESOLVER_BEAN_NAME = "localeResolver";
/*      */   public static final String THEME_RESOLVER_BEAN_NAME = "themeResolver";
/*      */   public static final String HANDLER_MAPPING_BEAN_NAME = "handlerMapping";
/*      */   public static final String HANDLER_ADAPTER_BEAN_NAME = "handlerAdapter";
/*      */   public static final String HANDLER_EXCEPTION_RESOLVER_BEAN_NAME = "handlerExceptionResolver";
/*      */   public static final String REQUEST_TO_VIEW_NAME_TRANSLATOR_BEAN_NAME = "viewNameTranslator";
/*      */   public static final String VIEW_RESOLVER_BEAN_NAME = "viewResolver";
/*      */   public static final String FLASH_MAP_MANAGER_BEAN_NAME = "flashMapManager";
/*  211 */   public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  217 */   public static final String LOCALE_RESOLVER_ATTRIBUTE = DispatcherServlet.class.getName() + ".LOCALE_RESOLVER";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  223 */   public static final String THEME_RESOLVER_ATTRIBUTE = DispatcherServlet.class.getName() + ".THEME_RESOLVER";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  229 */   public static final String THEME_SOURCE_ATTRIBUTE = DispatcherServlet.class.getName() + ".THEME_SOURCE";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  236 */   public static final String INPUT_FLASH_MAP_ATTRIBUTE = DispatcherServlet.class.getName() + ".INPUT_FLASH_MAP";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  243 */   public static final String OUTPUT_FLASH_MAP_ATTRIBUTE = DispatcherServlet.class.getName() + ".OUTPUT_FLASH_MAP";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  249 */   public static final String FLASH_MAP_MANAGER_ATTRIBUTE = DispatcherServlet.class.getName() + ".FLASH_MAP_MANAGER";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  256 */   public static final String EXCEPTION_ATTRIBUTE = DispatcherServlet.class.getName() + ".EXCEPTION";
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";
/*      */   
/*      */ 
/*      */ 
/*      */   private static final String DEFAULT_STRATEGIES_PREFIX = "org.springframework.web.servlet";
/*      */   
/*      */ 
/*      */ 
/*  273 */   protected static final Log pageNotFoundLogger = LogFactory.getLog("org.springframework.web.servlet.PageNotFound");
/*      */   
/*      */   private static final Properties defaultStrategies;
/*      */   
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/*  282 */       ClassPathResource resource = new ClassPathResource("DispatcherServlet.properties", DispatcherServlet.class);
/*  283 */       defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
/*      */     }
/*      */     catch (IOException ex) {
/*  286 */       throw new IllegalStateException("Could not load 'DispatcherServlet.properties': " + ex.getMessage());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*  291 */   private boolean detectAllHandlerMappings = true;
/*      */   
/*      */ 
/*  294 */   private boolean detectAllHandlerAdapters = true;
/*      */   
/*      */ 
/*  297 */   private boolean detectAllHandlerExceptionResolvers = true;
/*      */   
/*      */ 
/*  300 */   private boolean detectAllViewResolvers = true;
/*      */   
/*      */ 
/*  303 */   private boolean throwExceptionIfNoHandlerFound = false;
/*      */   
/*      */ 
/*  306 */   private boolean cleanupAfterInclude = true;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private MultipartResolver multipartResolver;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private LocaleResolver localeResolver;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private ThemeResolver themeResolver;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private List<HandlerMapping> handlerMappings;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private List<HandlerAdapter> handlerAdapters;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private List<HandlerExceptionResolver> handlerExceptionResolvers;
/*      */   
/*      */ 
/*      */ 
/*      */   private RequestToViewNameTranslator viewNameTranslator;
/*      */   
/*      */ 
/*      */ 
/*      */   private FlashMapManager flashMapManager;
/*      */   
/*      */ 
/*      */ 
/*      */   private List<ViewResolver> viewResolvers;
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public DispatcherServlet()
/*      */   {
/*  355 */     setDispatchOptionsRequest(true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public DispatcherServlet(WebApplicationContext webApplicationContext)
/*      */   {
/*  398 */     super(webApplicationContext);
/*  399 */     setDispatchOptionsRequest(true);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDetectAllHandlerMappings(boolean detectAllHandlerMappings)
/*      */   {
/*  410 */     this.detectAllHandlerMappings = detectAllHandlerMappings;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDetectAllHandlerAdapters(boolean detectAllHandlerAdapters)
/*      */   {
/*  420 */     this.detectAllHandlerAdapters = detectAllHandlerAdapters;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDetectAllHandlerExceptionResolvers(boolean detectAllHandlerExceptionResolvers)
/*      */   {
/*  430 */     this.detectAllHandlerExceptionResolvers = detectAllHandlerExceptionResolvers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setDetectAllViewResolvers(boolean detectAllViewResolvers)
/*      */   {
/*  440 */     this.detectAllViewResolvers = detectAllViewResolvers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setThrowExceptionIfNoHandlerFound(boolean throwExceptionIfNoHandlerFound)
/*      */   {
/*  455 */     this.throwExceptionIfNoHandlerFound = throwExceptionIfNoHandlerFound;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCleanupAfterInclude(boolean cleanupAfterInclude)
/*      */   {
/*  471 */     this.cleanupAfterInclude = cleanupAfterInclude;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void onRefresh(ApplicationContext context)
/*      */   {
/*  480 */     initStrategies(context);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void initStrategies(ApplicationContext context)
/*      */   {
/*  488 */     initMultipartResolver(context);
/*  489 */     initLocaleResolver(context);
/*  490 */     initThemeResolver(context);
/*  491 */     initHandlerMappings(context);
/*  492 */     initHandlerAdapters(context);
/*  493 */     initHandlerExceptionResolvers(context);
/*  494 */     initRequestToViewNameTranslator(context);
/*  495 */     initViewResolvers(context);
/*  496 */     initFlashMapManager(context);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initMultipartResolver(ApplicationContext context)
/*      */   {
/*      */     try
/*      */     {
/*  506 */       this.multipartResolver = ((MultipartResolver)context.getBean("multipartResolver", MultipartResolver.class));
/*  507 */       if (this.logger.isDebugEnabled()) {
/*  508 */         this.logger.debug("Using MultipartResolver [" + this.multipartResolver + "]");
/*      */       }
/*      */     }
/*      */     catch (NoSuchBeanDefinitionException ex)
/*      */     {
/*  513 */       this.multipartResolver = null;
/*  514 */       if (this.logger.isDebugEnabled()) {
/*  515 */         this.logger.debug("Unable to locate MultipartResolver with name 'multipartResolver': no multipart request handling provided");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initLocaleResolver(ApplicationContext context)
/*      */   {
/*      */     try
/*      */     {
/*  528 */       this.localeResolver = ((LocaleResolver)context.getBean("localeResolver", LocaleResolver.class));
/*  529 */       if (this.logger.isDebugEnabled()) {
/*  530 */         this.logger.debug("Using LocaleResolver [" + this.localeResolver + "]");
/*      */       }
/*      */     }
/*      */     catch (NoSuchBeanDefinitionException ex)
/*      */     {
/*  535 */       this.localeResolver = ((LocaleResolver)getDefaultStrategy(context, LocaleResolver.class));
/*  536 */       if (this.logger.isDebugEnabled()) {
/*  537 */         this.logger.debug("Unable to locate LocaleResolver with name 'localeResolver': using default [" + this.localeResolver + "]");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initThemeResolver(ApplicationContext context)
/*      */   {
/*      */     try
/*      */     {
/*  550 */       this.themeResolver = ((ThemeResolver)context.getBean("themeResolver", ThemeResolver.class));
/*  551 */       if (this.logger.isDebugEnabled()) {
/*  552 */         this.logger.debug("Using ThemeResolver [" + this.themeResolver + "]");
/*      */       }
/*      */     }
/*      */     catch (NoSuchBeanDefinitionException ex)
/*      */     {
/*  557 */       this.themeResolver = ((ThemeResolver)getDefaultStrategy(context, ThemeResolver.class));
/*  558 */       if (this.logger.isDebugEnabled()) {
/*  559 */         this.logger.debug("Unable to locate ThemeResolver with name 'themeResolver': using default [" + this.themeResolver + "]");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initHandlerMappings(ApplicationContext context)
/*      */   {
/*  571 */     this.handlerMappings = null;
/*      */     
/*  573 */     if (this.detectAllHandlerMappings)
/*      */     {
/*      */ 
/*  576 */       Map<String, HandlerMapping> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerMapping.class, true, false);
/*  577 */       if (!matchingBeans.isEmpty()) {
/*  578 */         this.handlerMappings = new ArrayList(matchingBeans.values());
/*      */         
/*  580 */         AnnotationAwareOrderComparator.sort(this.handlerMappings);
/*      */       }
/*      */     }
/*      */     else {
/*      */       try {
/*  585 */         HandlerMapping hm = (HandlerMapping)context.getBean("handlerMapping", HandlerMapping.class);
/*  586 */         this.handlerMappings = Collections.singletonList(hm);
/*      */       }
/*      */       catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  595 */     if (this.handlerMappings == null) {
/*  596 */       this.handlerMappings = getDefaultStrategies(context, HandlerMapping.class);
/*  597 */       if (this.logger.isDebugEnabled()) {
/*  598 */         this.logger.debug("No HandlerMappings found in servlet '" + getServletName() + "': using default");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initHandlerAdapters(ApplicationContext context)
/*      */   {
/*  609 */     this.handlerAdapters = null;
/*      */     
/*  611 */     if (this.detectAllHandlerAdapters)
/*      */     {
/*      */ 
/*  614 */       Map<String, HandlerAdapter> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerAdapter.class, true, false);
/*  615 */       if (!matchingBeans.isEmpty()) {
/*  616 */         this.handlerAdapters = new ArrayList(matchingBeans.values());
/*      */         
/*  618 */         AnnotationAwareOrderComparator.sort(this.handlerAdapters);
/*      */       }
/*      */     }
/*      */     else {
/*      */       try {
/*  623 */         HandlerAdapter ha = (HandlerAdapter)context.getBean("handlerAdapter", HandlerAdapter.class);
/*  624 */         this.handlerAdapters = Collections.singletonList(ha);
/*      */       }
/*      */       catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  633 */     if (this.handlerAdapters == null) {
/*  634 */       this.handlerAdapters = getDefaultStrategies(context, HandlerAdapter.class);
/*  635 */       if (this.logger.isDebugEnabled()) {
/*  636 */         this.logger.debug("No HandlerAdapters found in servlet '" + getServletName() + "': using default");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initHandlerExceptionResolvers(ApplicationContext context)
/*      */   {
/*  647 */     this.handlerExceptionResolvers = null;
/*      */     
/*  649 */     if (this.detectAllHandlerExceptionResolvers)
/*      */     {
/*      */ 
/*  652 */       Map<String, HandlerExceptionResolver> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerExceptionResolver.class, true, false);
/*  653 */       if (!matchingBeans.isEmpty()) {
/*  654 */         this.handlerExceptionResolvers = new ArrayList(matchingBeans.values());
/*      */         
/*  656 */         AnnotationAwareOrderComparator.sort(this.handlerExceptionResolvers);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*      */       try {
/*  662 */         HandlerExceptionResolver her = (HandlerExceptionResolver)context.getBean("handlerExceptionResolver", HandlerExceptionResolver.class);
/*  663 */         this.handlerExceptionResolvers = Collections.singletonList(her);
/*      */       }
/*      */       catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  672 */     if (this.handlerExceptionResolvers == null) {
/*  673 */       this.handlerExceptionResolvers = getDefaultStrategies(context, HandlerExceptionResolver.class);
/*  674 */       if (this.logger.isDebugEnabled()) {
/*  675 */         this.logger.debug("No HandlerExceptionResolvers found in servlet '" + getServletName() + "': using default");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initRequestToViewNameTranslator(ApplicationContext context)
/*      */   {
/*      */     try
/*      */     {
/*  687 */       this.viewNameTranslator = ((RequestToViewNameTranslator)context.getBean("viewNameTranslator", RequestToViewNameTranslator.class));
/*  688 */       if (this.logger.isDebugEnabled()) {
/*  689 */         this.logger.debug("Using RequestToViewNameTranslator [" + this.viewNameTranslator + "]");
/*      */       }
/*      */     }
/*      */     catch (NoSuchBeanDefinitionException ex)
/*      */     {
/*  694 */       this.viewNameTranslator = ((RequestToViewNameTranslator)getDefaultStrategy(context, RequestToViewNameTranslator.class));
/*  695 */       if (this.logger.isDebugEnabled()) {
/*  696 */         this.logger.debug("Unable to locate RequestToViewNameTranslator with name 'viewNameTranslator': using default [" + this.viewNameTranslator + "]");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initViewResolvers(ApplicationContext context)
/*      */   {
/*  709 */     this.viewResolvers = null;
/*      */     
/*  711 */     if (this.detectAllViewResolvers)
/*      */     {
/*      */ 
/*  714 */       Map<String, ViewResolver> matchingBeans = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, ViewResolver.class, true, false);
/*  715 */       if (!matchingBeans.isEmpty()) {
/*  716 */         this.viewResolvers = new ArrayList(matchingBeans.values());
/*      */         
/*  718 */         AnnotationAwareOrderComparator.sort(this.viewResolvers);
/*      */       }
/*      */     }
/*      */     else {
/*      */       try {
/*  723 */         ViewResolver vr = (ViewResolver)context.getBean("viewResolver", ViewResolver.class);
/*  724 */         this.viewResolvers = Collections.singletonList(vr);
/*      */       }
/*      */       catch (NoSuchBeanDefinitionException localNoSuchBeanDefinitionException) {}
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*  733 */     if (this.viewResolvers == null) {
/*  734 */       this.viewResolvers = getDefaultStrategies(context, ViewResolver.class);
/*  735 */       if (this.logger.isDebugEnabled()) {
/*  736 */         this.logger.debug("No ViewResolvers found in servlet '" + getServletName() + "': using default");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private void initFlashMapManager(ApplicationContext context)
/*      */   {
/*      */     try
/*      */     {
/*  748 */       this.flashMapManager = ((FlashMapManager)context.getBean("flashMapManager", FlashMapManager.class));
/*  749 */       if (this.logger.isDebugEnabled()) {
/*  750 */         this.logger.debug("Using FlashMapManager [" + this.flashMapManager + "]");
/*      */       }
/*      */     }
/*      */     catch (NoSuchBeanDefinitionException ex)
/*      */     {
/*  755 */       this.flashMapManager = ((FlashMapManager)getDefaultStrategy(context, FlashMapManager.class));
/*  756 */       if (this.logger.isDebugEnabled()) {
/*  757 */         this.logger.debug("Unable to locate FlashMapManager with name 'flashMapManager': using default [" + this.flashMapManager + "]");
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final ThemeSource getThemeSource()
/*      */   {
/*  771 */     if ((getWebApplicationContext() instanceof ThemeSource)) {
/*  772 */       return (ThemeSource)getWebApplicationContext();
/*      */     }
/*      */     
/*  775 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public final MultipartResolver getMultipartResolver()
/*      */   {
/*  785 */     return this.multipartResolver;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected <T> T getDefaultStrategy(ApplicationContext context, Class<T> strategyInterface)
/*      */   {
/*  798 */     List<T> strategies = getDefaultStrategies(context, strategyInterface);
/*  799 */     if (strategies.size() != 1)
/*      */     {
/*  801 */       throw new BeanInitializationException("DispatcherServlet needs exactly 1 strategy for interface [" + strategyInterface.getName() + "]");
/*      */     }
/*  803 */     return (T)strategies.get(0);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected <T> List<T> getDefaultStrategies(ApplicationContext context, Class<T> strategyInterface)
/*      */   {
/*  817 */     String key = strategyInterface.getName();
/*  818 */     String value = defaultStrategies.getProperty(key);
/*  819 */     if (value != null) {
/*  820 */       String[] classNames = StringUtils.commaDelimitedListToStringArray(value);
/*  821 */       List<T> strategies = new ArrayList(classNames.length);
/*  822 */       for (String className : classNames) {
/*      */         try {
/*  824 */           Class<?> clazz = ClassUtils.forName(className, DispatcherServlet.class.getClassLoader());
/*  825 */           Object strategy = createDefaultStrategy(context, clazz);
/*  826 */           strategies.add(strategy);
/*      */         }
/*      */         catch (ClassNotFoundException ex) {
/*  829 */           throw new BeanInitializationException("Could not find DispatcherServlet's default strategy class [" + className + "] for interface [" + key + "]", ex);
/*      */ 
/*      */         }
/*      */         catch (LinkageError err)
/*      */         {
/*  834 */           throw new BeanInitializationException("Error loading DispatcherServlet's default strategy class [" + className + "] for interface [" + key + "]: problem with class file or dependent class", err);
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*  839 */       return strategies;
/*      */     }
/*      */     
/*  842 */     return new LinkedList();
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected Object createDefaultStrategy(ApplicationContext context, Class<?> clazz)
/*      */   {
/*  857 */     return context.getAutowireCapableBeanFactory().createBean(clazz);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void doService(HttpServletRequest request, HttpServletResponse response)
/*      */     throws Exception
/*      */   {
/*  867 */     if (this.logger.isDebugEnabled()) {
/*  868 */       String resumed = WebAsyncUtils.getAsyncManager(request).hasConcurrentResult() ? " resumed" : "";
/*  869 */       this.logger.debug("DispatcherServlet with name '" + getServletName() + "'" + resumed + " processing " + request
/*  870 */         .getMethod() + " request for [" + getRequestUri(request) + "]");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*  875 */     Map<String, Object> attributesSnapshot = null;
/*  876 */     if (WebUtils.isIncludeRequest(request)) {
/*  877 */       attributesSnapshot = new HashMap();
/*  878 */       Enumeration<?> attrNames = request.getAttributeNames();
/*  879 */       while (attrNames.hasMoreElements()) {
/*  880 */         String attrName = (String)attrNames.nextElement();
/*  881 */         if ((this.cleanupAfterInclude) || (attrName.startsWith("org.springframework.web.servlet"))) {
/*  882 */           attributesSnapshot.put(attrName, request.getAttribute(attrName));
/*      */         }
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*  888 */     request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, getWebApplicationContext());
/*  889 */     request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver);
/*  890 */     request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver);
/*  891 */     request.setAttribute(THEME_SOURCE_ATTRIBUTE, getThemeSource());
/*      */     
/*  893 */     FlashMap inputFlashMap = this.flashMapManager.retrieveAndUpdate(request, response);
/*  894 */     if (inputFlashMap != null) {
/*  895 */       request.setAttribute(INPUT_FLASH_MAP_ATTRIBUTE, Collections.unmodifiableMap(inputFlashMap));
/*      */     }
/*  897 */     request.setAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE, new FlashMap());
/*  898 */     request.setAttribute(FLASH_MAP_MANAGER_ATTRIBUTE, this.flashMapManager);
/*      */     try
/*      */     {
/*  901 */       doDispatch(request, response);
/*      */     }
/*      */     finally {
/*  904 */       if (!WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted())
/*      */       {
/*  906 */         if (attributesSnapshot != null) {
/*  907 */           restoreAttributesAfterInclude(request, attributesSnapshot);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void doDispatch(HttpServletRequest request, HttpServletResponse response)
/*      */     throws Exception
/*      */   {
/*  925 */     HttpServletRequest processedRequest = request;
/*  926 */     HandlerExecutionChain mappedHandler = null;
/*  927 */     boolean multipartRequestParsed = false;
/*      */     
/*  929 */     WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
/*      */     try
/*      */     {
/*  932 */       ModelAndView mv = null;
/*  933 */       Exception dispatchException = null;
/*      */       try
/*      */       {
/*  936 */         processedRequest = checkMultipart(request);
/*  937 */         multipartRequestParsed = processedRequest != request;
/*      */         
/*      */ 
/*  940 */         mappedHandler = getHandler(processedRequest);
/*  941 */         if ((mappedHandler == null) || (mappedHandler.getHandler() == null)) {
/*  942 */           noHandlerFound(processedRequest, response);
/*  943 */           return;
/*      */         }
/*      */         
/*      */ 
/*  947 */         HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());
/*      */         
/*      */ 
/*  950 */         String method = request.getMethod();
/*  951 */         boolean isGet = "GET".equals(method);
/*  952 */         if ((isGet) || ("HEAD".equals(method))) {
/*  953 */           long lastModified = ha.getLastModified(request, mappedHandler.getHandler());
/*  954 */           if (this.logger.isDebugEnabled()) {
/*  955 */             this.logger.debug("Last-Modified value for [" + getRequestUri(request) + "] is: " + lastModified);
/*      */           }
/*  957 */           if ((new ServletWebRequest(request, response).checkNotModified(lastModified)) && (isGet)) {
/*  958 */             return;
/*      */           }
/*      */         }
/*      */         
/*  962 */         if (!mappedHandler.applyPreHandle(processedRequest, response)) {
/*  963 */           return;
/*      */         }
/*      */         
/*      */ 
/*  967 */         mv = ha.handle(processedRequest, response, mappedHandler.getHandler());
/*      */         
/*  969 */         if (asyncManager.isConcurrentHandlingStarted()) {
/*  970 */           return;
/*      */         }
/*      */         
/*  973 */         applyDefaultViewName(processedRequest, mv);
/*  974 */         mappedHandler.applyPostHandle(processedRequest, response, mv);
/*      */       }
/*      */       catch (Exception ex) {
/*  977 */         dispatchException = ex;
/*      */ 
/*      */       }
/*      */       catch (Throwable err)
/*      */       {
/*  982 */         dispatchException = new NestedServletException("Handler dispatch failed", err);
/*      */       }
/*  984 */       processDispatchResult(processedRequest, response, mappedHandler, mv, dispatchException);
/*      */     }
/*      */     catch (Exception ex) {
/*  987 */       triggerAfterCompletion(processedRequest, response, mappedHandler, ex);
/*      */     }
/*      */     catch (Throwable err) {
/*  990 */       triggerAfterCompletion(processedRequest, response, mappedHandler, new NestedServletException("Handler processing failed", err));
/*      */     }
/*      */     finally
/*      */     {
/*  994 */       if (asyncManager.isConcurrentHandlingStarted())
/*      */       {
/*  996 */         if (mappedHandler != null) {
/*  997 */           mappedHandler.applyAfterConcurrentHandlingStarted(processedRequest, response);
/*      */         }
/*      */         
/*      */ 
/*      */       }
/* 1002 */       else if (multipartRequestParsed) {
/* 1003 */         cleanupMultipart(processedRequest);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private void applyDefaultViewName(HttpServletRequest request, ModelAndView mv)
/*      */     throws Exception
/*      */   {
/* 1013 */     if ((mv != null) && (!mv.hasView())) {
/* 1014 */       mv.setViewName(getDefaultViewName(request));
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void processDispatchResult(HttpServletRequest request, HttpServletResponse response, HandlerExecutionChain mappedHandler, ModelAndView mv, Exception exception)
/*      */     throws Exception
/*      */   {
/* 1025 */     boolean errorView = false;
/*      */     
/* 1027 */     if (exception != null) {
/* 1028 */       if ((exception instanceof ModelAndViewDefiningException)) {
/* 1029 */         this.logger.debug("ModelAndViewDefiningException encountered", exception);
/* 1030 */         mv = ((ModelAndViewDefiningException)exception).getModelAndView();
/*      */       }
/*      */       else {
/* 1033 */         Object handler = mappedHandler != null ? mappedHandler.getHandler() : null;
/* 1034 */         mv = processHandlerException(request, response, handler, exception);
/* 1035 */         errorView = mv != null;
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1040 */     if ((mv != null) && (!mv.wasCleared())) {
/* 1041 */       render(mv, request, response);
/* 1042 */       if (errorView) {
/* 1043 */         WebUtils.clearErrorRequestAttributes(request);
/*      */       }
/*      */       
/*      */     }
/* 1047 */     else if (this.logger.isDebugEnabled()) {
/* 1048 */       this.logger.debug("Null ModelAndView returned to DispatcherServlet with name '" + getServletName() + "': assuming HandlerAdapter completed request handling");
/*      */     }
/*      */     
/*      */ 
/*      */ 
/* 1053 */     if (WebAsyncUtils.getAsyncManager(request).isConcurrentHandlingStarted())
/*      */     {
/* 1055 */       return;
/*      */     }
/*      */     
/* 1058 */     if (mappedHandler != null) {
/* 1059 */       mappedHandler.triggerAfterCompletion(request, response, null);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected LocaleContext buildLocaleContext(final HttpServletRequest request)
/*      */   {
/* 1072 */     if ((this.localeResolver instanceof LocaleContextResolver)) {
/* 1073 */       return ((LocaleContextResolver)this.localeResolver).resolveLocaleContext(request);
/*      */     }
/*      */     
/* 1076 */     new LocaleContext()
/*      */     {
/*      */       public Locale getLocale() {
/* 1079 */         return DispatcherServlet.this.localeResolver.resolveLocale(request);
/*      */       }
/*      */     };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected HttpServletRequest checkMultipart(HttpServletRequest request)
/*      */     throws MultipartException
/*      */   {
/* 1093 */     if ((this.multipartResolver != null) && (this.multipartResolver.isMultipart(request))) {
/* 1094 */       if (WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class) != null) {
/* 1095 */         this.logger.debug("Request is already a MultipartHttpServletRequest - if not in a forward, this typically results from an additional MultipartFilter in web.xml");
/*      */ 
/*      */       }
/* 1098 */       else if (hasMultipartException(request)) {
/* 1099 */         this.logger.debug("Multipart resolution failed for current request before - skipping re-resolution for undisturbed error rendering");
/*      */       }
/*      */       else {
/*      */         try
/*      */         {
/* 1104 */           return this.multipartResolver.resolveMultipart(request);
/*      */         }
/*      */         catch (MultipartException ex) {
/* 1107 */           if (request.getAttribute("javax.servlet.error.exception") != null) {
/* 1108 */             this.logger.debug("Multipart resolution failed for error dispatch", ex);
/*      */           }
/*      */           else
/*      */           {
/* 1112 */             throw ex;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */     
/* 1118 */     return request;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   private boolean hasMultipartException(HttpServletRequest request)
/*      */   {
/* 1125 */     Throwable error = (Throwable)request.getAttribute("javax.servlet.error.exception");
/* 1126 */     while (error != null) {
/* 1127 */       if ((error instanceof MultipartException)) {
/* 1128 */         return true;
/*      */       }
/* 1130 */       error = error.getCause();
/*      */     }
/* 1132 */     return false;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void cleanupMultipart(HttpServletRequest request)
/*      */   {
/* 1142 */     MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest)WebUtils.getNativeRequest(request, MultipartHttpServletRequest.class);
/* 1143 */     if (multipartRequest != null) {
/* 1144 */       this.multipartResolver.cleanupMultipart(multipartRequest);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected HandlerExecutionChain getHandler(HttpServletRequest request)
/*      */     throws Exception
/*      */   {
/* 1155 */     for (HandlerMapping hm : this.handlerMappings) {
/* 1156 */       if (this.logger.isTraceEnabled()) {
/* 1157 */         this.logger.trace("Testing handler map [" + hm + "] in DispatcherServlet with name '" + 
/* 1158 */           getServletName() + "'");
/*      */       }
/* 1160 */       HandlerExecutionChain handler = hm.getHandler(request);
/* 1161 */       if (handler != null) {
/* 1162 */         return handler;
/*      */       }
/*      */     }
/* 1165 */     return null;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response)
/*      */     throws Exception
/*      */   {
/* 1175 */     if (pageNotFoundLogger.isWarnEnabled()) {
/* 1176 */       pageNotFoundLogger.warn("No mapping found for HTTP request with URI [" + getRequestUri(request) + "] in DispatcherServlet with name '" + 
/* 1177 */         getServletName() + "'");
/*      */     }
/* 1179 */     if (this.throwExceptionIfNoHandlerFound)
/*      */     {
/* 1181 */       throw new NoHandlerFoundException(request.getMethod(), getRequestUri(request), new ServletServerHttpRequest(request).getHeaders());
/*      */     }
/*      */     
/* 1184 */     response.sendError(404);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected HandlerAdapter getHandlerAdapter(Object handler)
/*      */     throws ServletException
/*      */   {
/* 1194 */     for (HandlerAdapter ha : this.handlerAdapters) {
/* 1195 */       if (this.logger.isTraceEnabled()) {
/* 1196 */         this.logger.trace("Testing handler adapter [" + ha + "]");
/*      */       }
/* 1198 */       if (ha.supports(handler)) {
/* 1199 */         return ha;
/*      */       }
/*      */     }
/* 1202 */     throw new ServletException("No adapter for handler [" + handler + "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*      */     throws Exception
/*      */   {
/* 1220 */     ModelAndView exMv = null;
/* 1221 */     for (HandlerExceptionResolver handlerExceptionResolver : this.handlerExceptionResolvers) {
/* 1222 */       exMv = handlerExceptionResolver.resolveException(request, response, handler, ex);
/* 1223 */       if (exMv != null) {
/*      */         break;
/*      */       }
/*      */     }
/* 1227 */     if (exMv != null) {
/* 1228 */       if (exMv.isEmpty()) {
/* 1229 */         request.setAttribute(EXCEPTION_ATTRIBUTE, ex);
/* 1230 */         return null;
/*      */       }
/*      */       
/* 1233 */       if (!exMv.hasView()) {
/* 1234 */         exMv.setViewName(getDefaultViewName(request));
/*      */       }
/* 1236 */       if (this.logger.isDebugEnabled()) {
/* 1237 */         this.logger.debug("Handler execution resulted in exception - forwarding to resolved error view: " + exMv, ex);
/*      */       }
/* 1239 */       WebUtils.exposeErrorRequestAttributes(request, ex, getServletName());
/* 1240 */       return exMv;
/*      */     }
/*      */     
/* 1243 */     throw ex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected void render(ModelAndView mv, HttpServletRequest request, HttpServletResponse response)
/*      */     throws Exception
/*      */   {
/* 1257 */     Locale locale = this.localeResolver.resolveLocale(request);
/* 1258 */     response.setLocale(locale);
/*      */     
/*      */     View view;
/* 1261 */     if (mv.isReference())
/*      */     {
/* 1263 */       View view = resolveViewName(mv.getViewName(), mv.getModelInternal(), locale, request);
/* 1264 */       if (view == null)
/*      */       {
/* 1266 */         throw new ServletException("Could not resolve view with name '" + mv.getViewName() + "' in servlet with name '" + getServletName() + "'");
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1271 */       view = mv.getView();
/* 1272 */       if (view == null)
/*      */       {
/* 1274 */         throw new ServletException("ModelAndView [" + mv + "] neither contains a view name nor a View object in servlet with name '" + getServletName() + "'");
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1279 */     if (this.logger.isDebugEnabled()) {
/* 1280 */       this.logger.debug("Rendering view [" + view + "] in DispatcherServlet with name '" + getServletName() + "'");
/*      */     }
/*      */     try {
/* 1283 */       if (mv.getStatus() != null) {
/* 1284 */         response.setStatus(mv.getStatus().value());
/*      */       }
/* 1286 */       view.render(mv.getModelInternal(), request, response);
/*      */     }
/*      */     catch (Exception ex) {
/* 1289 */       if (this.logger.isDebugEnabled()) {
/* 1290 */         this.logger.debug("Error rendering view [" + view + "] in DispatcherServlet with name '" + 
/* 1291 */           getServletName() + "'", ex);
/*      */       }
/* 1293 */       throw ex;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected String getDefaultViewName(HttpServletRequest request)
/*      */     throws Exception
/*      */   {
/* 1304 */     return this.viewNameTranslator.getViewName(request);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected View resolveViewName(String viewName, Map<String, Object> model, Locale locale, HttpServletRequest request)
/*      */     throws Exception
/*      */   {
/* 1324 */     for (ViewResolver viewResolver : this.viewResolvers) {
/* 1325 */       View view = viewResolver.resolveViewName(viewName, locale);
/* 1326 */       if (view != null) {
/* 1327 */         return view;
/*      */       }
/*      */     }
/* 1330 */     return null;
/*      */   }
/*      */   
/*      */   private void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, HandlerExecutionChain mappedHandler, Exception ex)
/*      */     throws Exception
/*      */   {
/* 1336 */     if (mappedHandler != null) {
/* 1337 */       mappedHandler.triggerAfterCompletion(request, response, ex);
/*      */     }
/* 1339 */     throw ex;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private void restoreAttributesAfterInclude(HttpServletRequest request, Map<?, ?> attributesSnapshot)
/*      */   {
/* 1351 */     Set<String> attrsToCheck = new HashSet();
/* 1352 */     Enumeration<?> attrNames = request.getAttributeNames();
/* 1353 */     String attrName; while (attrNames.hasMoreElements()) {
/* 1354 */       attrName = (String)attrNames.nextElement();
/* 1355 */       if ((this.cleanupAfterInclude) || (attrName.startsWith("org.springframework.web.servlet"))) {
/* 1356 */         attrsToCheck.add(attrName);
/*      */       }
/*      */     }
/*      */     
/*      */ 
/* 1361 */     attrsToCheck.addAll(attributesSnapshot.keySet());
/*      */     
/*      */ 
/*      */ 
/* 1365 */     for (String attrName : attrsToCheck) {
/* 1366 */       Object attrValue = attributesSnapshot.get(attrName);
/* 1367 */       if (attrValue == null) {
/* 1368 */         request.removeAttribute(attrName);
/*      */       }
/* 1370 */       else if (attrValue != request.getAttribute(attrName)) {
/* 1371 */         request.setAttribute(attrName, attrValue);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   private static String getRequestUri(HttpServletRequest request) {
/* 1377 */     String uri = (String)request.getAttribute("javax.servlet.include.request_uri");
/* 1378 */     if (uri == null) {
/* 1379 */       uri = request.getRequestURI();
/*      */     }
/* 1381 */     return uri;
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\DispatcherServlet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */