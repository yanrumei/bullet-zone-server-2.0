/*     */ package org.springframework.web.servlet.handler;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.springframework.beans.BeansException;
/*     */ import org.springframework.beans.factory.BeanFactoryUtils;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.core.Ordered;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.web.HttpRequestHandler;
/*     */ import org.springframework.web.context.request.WebRequestInterceptor;
/*     */ import org.springframework.web.context.support.WebApplicationObjectSupport;
/*     */ import org.springframework.web.cors.CorsConfiguration;
/*     */ import org.springframework.web.cors.CorsConfigurationSource;
/*     */ import org.springframework.web.cors.CorsProcessor;
/*     */ import org.springframework.web.cors.CorsUtils;
/*     */ import org.springframework.web.cors.DefaultCorsProcessor;
/*     */ import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
/*     */ import org.springframework.web.servlet.HandlerExecutionChain;
/*     */ import org.springframework.web.servlet.HandlerInterceptor;
/*     */ import org.springframework.web.servlet.HandlerMapping;
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
/*     */ public abstract class AbstractHandlerMapping
/*     */   extends WebApplicationObjectSupport
/*     */   implements HandlerMapping, Ordered
/*     */ {
/*  69 */   private int order = Integer.MAX_VALUE;
/*     */   
/*     */   private Object defaultHandler;
/*     */   
/*  73 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*     */   
/*  75 */   private PathMatcher pathMatcher = new AntPathMatcher();
/*     */   
/*  77 */   private final List<Object> interceptors = new ArrayList();
/*     */   
/*  79 */   private final List<HandlerInterceptor> adaptedInterceptors = new ArrayList();
/*     */   
/*  81 */   private final UrlBasedCorsConfigurationSource globalCorsConfigSource = new UrlBasedCorsConfigurationSource();
/*     */   
/*  83 */   private CorsProcessor corsProcessor = new DefaultCorsProcessor();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setOrder(int order)
/*     */   {
/*  92 */     this.order = order;
/*     */   }
/*     */   
/*     */   public final int getOrder()
/*     */   {
/*  97 */     return this.order;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDefaultHandler(Object defaultHandler)
/*     */   {
/* 106 */     this.defaultHandler = defaultHandler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object getDefaultHandler()
/*     */   {
/* 114 */     return this.defaultHandler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath)
/*     */   {
/* 125 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
/* 126 */     this.globalCorsConfigSource.setAlwaysUseFullPath(alwaysUseFullPath);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUrlDecode(boolean urlDecode)
/*     */   {
/* 137 */     this.urlPathHelper.setUrlDecode(urlDecode);
/* 138 */     this.globalCorsConfigSource.setUrlDecode(urlDecode);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRemoveSemicolonContent(boolean removeSemicolonContent)
/*     */   {
/* 147 */     this.urlPathHelper.setRemoveSemicolonContent(removeSemicolonContent);
/* 148 */     this.globalCorsConfigSource.setRemoveSemicolonContent(removeSemicolonContent);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/*     */   {
/* 158 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/* 159 */     this.urlPathHelper = urlPathHelper;
/* 160 */     this.globalCorsConfigSource.setUrlPathHelper(urlPathHelper);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public UrlPathHelper getUrlPathHelper()
/*     */   {
/* 167 */     return this.urlPathHelper;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setPathMatcher(PathMatcher pathMatcher)
/*     */   {
/* 176 */     Assert.notNull(pathMatcher, "PathMatcher must not be null");
/* 177 */     this.pathMatcher = pathMatcher;
/* 178 */     this.globalCorsConfigSource.setPathMatcher(pathMatcher);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public PathMatcher getPathMatcher()
/*     */   {
/* 186 */     return this.pathMatcher;
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
/*     */   public void setInterceptors(Object... interceptors)
/*     */   {
/* 200 */     this.interceptors.addAll(Arrays.asList(interceptors));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCorsConfigurations(Map<String, CorsConfiguration> corsConfigurations)
/*     */   {
/* 210 */     this.globalCorsConfigSource.setCorsConfigurations(corsConfigurations);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Map<String, CorsConfiguration> getCorsConfigurations()
/*     */   {
/* 217 */     return this.globalCorsConfigSource.getCorsConfigurations();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCorsProcessor(CorsProcessor corsProcessor)
/*     */   {
/* 227 */     Assert.notNull(corsProcessor, "CorsProcessor must not be null");
/* 228 */     this.corsProcessor = corsProcessor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CorsProcessor getCorsProcessor()
/*     */   {
/* 235 */     return this.corsProcessor;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initApplicationContext()
/*     */     throws BeansException
/*     */   {
/* 246 */     extendInterceptors(this.interceptors);
/* 247 */     detectMappedInterceptors(this.adaptedInterceptors);
/* 248 */     initInterceptors();
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
/*     */   protected void extendInterceptors(List<Object> interceptors) {}
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void detectMappedInterceptors(List<HandlerInterceptor> mappedInterceptors)
/*     */   {
/* 271 */     mappedInterceptors.addAll(
/* 272 */       BeanFactoryUtils.beansOfTypeIncludingAncestors(
/* 273 */       getApplicationContext(), MappedInterceptor.class, true, false).values());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void initInterceptors()
/*     */   {
/* 283 */     if (!this.interceptors.isEmpty()) {
/* 284 */       for (int i = 0; i < this.interceptors.size(); i++) {
/* 285 */         Object interceptor = this.interceptors.get(i);
/* 286 */         if (interceptor == null) {
/* 287 */           throw new IllegalArgumentException("Entry number " + i + " in interceptors array is null");
/*     */         }
/* 289 */         this.adaptedInterceptors.add(adaptInterceptor(interceptor));
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
/*     */   protected HandlerInterceptor adaptInterceptor(Object interceptor)
/*     */   {
/* 307 */     if ((interceptor instanceof HandlerInterceptor)) {
/* 308 */       return (HandlerInterceptor)interceptor;
/*     */     }
/* 310 */     if ((interceptor instanceof WebRequestInterceptor)) {
/* 311 */       return new WebRequestHandlerInterceptorAdapter((WebRequestInterceptor)interceptor);
/*     */     }
/*     */     
/* 314 */     throw new IllegalArgumentException("Interceptor type not supported: " + interceptor.getClass().getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final HandlerInterceptor[] getAdaptedInterceptors()
/*     */   {
/* 323 */     int count = this.adaptedInterceptors.size();
/* 324 */     return count > 0 ? (HandlerInterceptor[])this.adaptedInterceptors.toArray(new HandlerInterceptor[count]) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final MappedInterceptor[] getMappedInterceptors()
/*     */   {
/* 332 */     List<MappedInterceptor> mappedInterceptors = new ArrayList();
/* 333 */     for (HandlerInterceptor interceptor : this.adaptedInterceptors) {
/* 334 */       if ((interceptor instanceof MappedInterceptor)) {
/* 335 */         mappedInterceptors.add((MappedInterceptor)interceptor);
/*     */       }
/*     */     }
/* 338 */     int count = mappedInterceptors.size();
/* 339 */     return count > 0 ? (MappedInterceptor[])mappedInterceptors.toArray(new MappedInterceptor[count]) : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final HandlerExecutionChain getHandler(HttpServletRequest request)
/*     */     throws Exception
/*     */   {
/* 352 */     Object handler = getHandlerInternal(request);
/* 353 */     if (handler == null) {
/* 354 */       handler = getDefaultHandler();
/*     */     }
/* 356 */     if (handler == null) {
/* 357 */       return null;
/*     */     }
/*     */     
/* 360 */     if ((handler instanceof String)) {
/* 361 */       String handlerName = (String)handler;
/* 362 */       handler = getApplicationContext().getBean(handlerName);
/*     */     }
/*     */     
/* 365 */     HandlerExecutionChain executionChain = getHandlerExecutionChain(handler, request);
/* 366 */     if (CorsUtils.isCorsRequest(request)) {
/* 367 */       CorsConfiguration globalConfig = this.globalCorsConfigSource.getCorsConfiguration(request);
/* 368 */       CorsConfiguration handlerConfig = getCorsConfiguration(handler, request);
/* 369 */       CorsConfiguration config = globalConfig != null ? globalConfig.combine(handlerConfig) : handlerConfig;
/* 370 */       executionChain = getCorsHandlerExecutionChain(request, executionChain, config);
/*     */     }
/* 372 */     return executionChain;
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
/*     */ 
/*     */   protected abstract Object getHandlerInternal(HttpServletRequest paramHttpServletRequest)
/*     */     throws Exception;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request)
/*     */   {
/* 414 */     HandlerExecutionChain chain = (handler instanceof HandlerExecutionChain) ? (HandlerExecutionChain)handler : new HandlerExecutionChain(handler);
/*     */     
/*     */ 
/* 417 */     String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
/* 418 */     for (HandlerInterceptor interceptor : this.adaptedInterceptors) {
/* 419 */       if ((interceptor instanceof MappedInterceptor)) {
/* 420 */         MappedInterceptor mappedInterceptor = (MappedInterceptor)interceptor;
/* 421 */         if (mappedInterceptor.matches(lookupPath, this.pathMatcher)) {
/* 422 */           chain.addInterceptor(mappedInterceptor.getInterceptor());
/*     */         }
/*     */       }
/*     */       else {
/* 426 */         chain.addInterceptor(interceptor);
/*     */       }
/*     */     }
/* 429 */     return chain;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected CorsConfiguration getCorsConfiguration(Object handler, HttpServletRequest request)
/*     */   {
/* 440 */     Object resolvedHandler = handler;
/* 441 */     if ((handler instanceof HandlerExecutionChain)) {
/* 442 */       resolvedHandler = ((HandlerExecutionChain)handler).getHandler();
/*     */     }
/* 444 */     if ((resolvedHandler instanceof CorsConfigurationSource)) {
/* 445 */       return ((CorsConfigurationSource)resolvedHandler).getCorsConfiguration(request);
/*     */     }
/* 447 */     return null;
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
/*     */   protected HandlerExecutionChain getCorsHandlerExecutionChain(HttpServletRequest request, HandlerExecutionChain chain, CorsConfiguration config)
/*     */   {
/* 465 */     if (CorsUtils.isPreFlightRequest(request)) {
/* 466 */       HandlerInterceptor[] interceptors = chain.getInterceptors();
/* 467 */       chain = new HandlerExecutionChain(new PreFlightHandler(config), interceptors);
/*     */     }
/*     */     else {
/* 470 */       chain.addInterceptor(new CorsInterceptor(config));
/*     */     }
/* 472 */     return chain;
/*     */   }
/*     */   
/*     */   private class PreFlightHandler implements HttpRequestHandler, CorsConfigurationSource
/*     */   {
/*     */     private final CorsConfiguration config;
/*     */     
/*     */     public PreFlightHandler(CorsConfiguration config)
/*     */     {
/* 481 */       this.config = config;
/*     */     }
/*     */     
/*     */     public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException
/*     */     {
/* 486 */       AbstractHandlerMapping.this.corsProcessor.processRequest(this.config, request, response);
/*     */     }
/*     */     
/*     */     public CorsConfiguration getCorsConfiguration(HttpServletRequest request)
/*     */     {
/* 491 */       return this.config;
/*     */     }
/*     */   }
/*     */   
/*     */   private class CorsInterceptor extends HandlerInterceptorAdapter implements CorsConfigurationSource
/*     */   {
/*     */     private final CorsConfiguration config;
/*     */     
/*     */     public CorsInterceptor(CorsConfiguration config)
/*     */     {
/* 501 */       this.config = config;
/*     */     }
/*     */     
/*     */ 
/*     */     public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*     */       throws Exception
/*     */     {
/* 508 */       return AbstractHandlerMapping.this.corsProcessor.processRequest(this.config, request, response);
/*     */     }
/*     */     
/*     */     public CorsConfiguration getCorsConfiguration(HttpServletRequest request)
/*     */     {
/* 513 */       return this.config;
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\handler\AbstractHandlerMapping.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */