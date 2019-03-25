/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.context.ApplicationContext;
/*     */ import org.springframework.context.ApplicationContextAware;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.converter.StringHttpMessageConverter;
/*     */ import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*     */ import org.springframework.ui.ModelMap;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.method.ControllerAdviceBean;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.method.annotation.ExceptionHandlerMethodResolver;
/*     */ import org.springframework.web.method.annotation.MapMethodProcessor;
/*     */ import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
/*     */ import org.springframework.web.method.annotation.ModelMethodProcessor;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.servlet.FlashMap;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.handler.AbstractHandlerMethodExceptionResolver;
/*     */ import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExceptionHandlerExceptionResolver
/*     */   extends AbstractHandlerMethodExceptionResolver
/*     */   implements ApplicationContextAware, InitializingBean
/*     */ {
/*     */   private List<HandlerMethodArgumentResolver> customArgumentResolvers;
/*     */   private HandlerMethodArgumentResolverComposite argumentResolvers;
/*     */   private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;
/*     */   private HandlerMethodReturnValueHandlerComposite returnValueHandlers;
/*     */   private List<HttpMessageConverter<?>> messageConverters;
/*  88 */   private ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();
/*     */   
/*  90 */   private final List<Object> responseBodyAdvice = new ArrayList();
/*     */   
/*     */   private ApplicationContext applicationContext;
/*     */   
/*  94 */   private final Map<Class<?>, ExceptionHandlerMethodResolver> exceptionHandlerCache = new ConcurrentHashMap(64);
/*     */   
/*     */ 
/*  97 */   private final Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdviceCache = new LinkedHashMap();
/*     */   
/*     */ 
/*     */   public ExceptionHandlerExceptionResolver()
/*     */   {
/* 102 */     StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
/* 103 */     stringHttpMessageConverter.setWriteAcceptCharset(false);
/*     */     
/* 105 */     this.messageConverters = new ArrayList();
/* 106 */     this.messageConverters.add(new ByteArrayHttpMessageConverter());
/* 107 */     this.messageConverters.add(stringHttpMessageConverter);
/* 108 */     this.messageConverters.add(new SourceHttpMessageConverter());
/* 109 */     this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCustomArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/*     */   {
/* 119 */     this.customArgumentResolvers = argumentResolvers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<HandlerMethodArgumentResolver> getCustomArgumentResolvers()
/*     */   {
/* 126 */     return this.customArgumentResolvers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/*     */   {
/* 134 */     if (argumentResolvers == null) {
/* 135 */       this.argumentResolvers = null;
/*     */     }
/*     */     else {
/* 138 */       this.argumentResolvers = new HandlerMethodArgumentResolverComposite();
/* 139 */       this.argumentResolvers.addResolvers(argumentResolvers);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HandlerMethodArgumentResolverComposite getArgumentResolvers()
/*     */   {
/* 148 */     return this.argumentResolvers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCustomReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers)
/*     */   {
/* 157 */     this.customReturnValueHandlers = returnValueHandlers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<HandlerMethodReturnValueHandler> getCustomReturnValueHandlers()
/*     */   {
/* 164 */     return this.customReturnValueHandlers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers)
/*     */   {
/* 172 */     if (returnValueHandlers == null) {
/* 173 */       this.returnValueHandlers = null;
/*     */     }
/*     */     else {
/* 176 */       this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
/* 177 */       this.returnValueHandlers.addHandlers(returnValueHandlers);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public HandlerMethodReturnValueHandlerComposite getReturnValueHandlers()
/*     */   {
/* 186 */     return this.returnValueHandlers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters)
/*     */   {
/* 194 */     this.messageConverters = messageConverters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<HttpMessageConverter<?>> getMessageConverters()
/*     */   {
/* 201 */     return this.messageConverters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager)
/*     */   {
/* 209 */     this.contentNegotiationManager = contentNegotiationManager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public ContentNegotiationManager getContentNegotiationManager()
/*     */   {
/* 216 */     return this.contentNegotiationManager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResponseBodyAdvice(List<ResponseBodyAdvice<?>> responseBodyAdvice)
/*     */   {
/* 226 */     this.responseBodyAdvice.clear();
/* 227 */     if (responseBodyAdvice != null) {
/* 228 */       this.responseBodyAdvice.addAll(responseBodyAdvice);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setApplicationContext(ApplicationContext applicationContext)
/*     */   {
/* 234 */     this.applicationContext = applicationContext;
/*     */   }
/*     */   
/*     */   public ApplicationContext getApplicationContext() {
/* 238 */     return this.applicationContext;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */   {
/* 245 */     initExceptionHandlerAdviceCache();
/*     */     
/* 247 */     if (this.argumentResolvers == null) {
/* 248 */       List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
/* 249 */       this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
/*     */     }
/* 251 */     if (this.returnValueHandlers == null) {
/* 252 */       List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
/* 253 */       this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite().addHandlers(handlers);
/*     */     }
/*     */   }
/*     */   
/*     */   private void initExceptionHandlerAdviceCache() {
/* 258 */     if (getApplicationContext() == null) {
/* 259 */       return;
/*     */     }
/* 261 */     if (this.logger.isDebugEnabled()) {
/* 262 */       this.logger.debug("Looking for exception mappings: " + getApplicationContext());
/*     */     }
/*     */     
/* 265 */     List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(getApplicationContext());
/* 266 */     AnnotationAwareOrderComparator.sort(adviceBeans);
/*     */     
/* 268 */     for (ControllerAdviceBean adviceBean : adviceBeans) {
/* 269 */       ExceptionHandlerMethodResolver resolver = new ExceptionHandlerMethodResolver(adviceBean.getBeanType());
/* 270 */       if (resolver.hasExceptionMappings()) {
/* 271 */         this.exceptionHandlerAdviceCache.put(adviceBean, resolver);
/* 272 */         if (this.logger.isInfoEnabled()) {
/* 273 */           this.logger.info("Detected @ExceptionHandler methods in " + adviceBean);
/*     */         }
/*     */       }
/* 276 */       if (ResponseBodyAdvice.class.isAssignableFrom(adviceBean.getBeanType())) {
/* 277 */         this.responseBodyAdvice.add(adviceBean);
/* 278 */         if (this.logger.isInfoEnabled()) {
/* 279 */           this.logger.info("Detected ResponseBodyAdvice implementation in " + adviceBean);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> getExceptionHandlerAdviceCache()
/*     */   {
/* 292 */     return Collections.unmodifiableMap(this.exceptionHandlerAdviceCache);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers()
/*     */   {
/* 300 */     List<HandlerMethodArgumentResolver> resolvers = new ArrayList();
/*     */     
/*     */ 
/* 303 */     resolvers.add(new SessionAttributeMethodArgumentResolver());
/* 304 */     resolvers.add(new RequestAttributeMethodArgumentResolver());
/*     */     
/*     */ 
/* 307 */     resolvers.add(new ServletRequestMethodArgumentResolver());
/* 308 */     resolvers.add(new ServletResponseMethodArgumentResolver());
/* 309 */     resolvers.add(new RedirectAttributesMethodArgumentResolver());
/* 310 */     resolvers.add(new ModelMethodProcessor());
/*     */     
/*     */ 
/* 313 */     if (getCustomArgumentResolvers() != null) {
/* 314 */       resolvers.addAll(getCustomArgumentResolvers());
/*     */     }
/*     */     
/* 317 */     return resolvers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers()
/*     */   {
/* 325 */     List<HandlerMethodReturnValueHandler> handlers = new ArrayList();
/*     */     
/*     */ 
/* 328 */     handlers.add(new ModelAndViewMethodReturnValueHandler());
/* 329 */     handlers.add(new ModelMethodProcessor());
/* 330 */     handlers.add(new ViewMethodReturnValueHandler());
/* 331 */     handlers.add(new HttpEntityMethodProcessor(
/* 332 */       getMessageConverters(), this.contentNegotiationManager, this.responseBodyAdvice));
/*     */     
/*     */ 
/* 335 */     handlers.add(new ModelAttributeMethodProcessor(false));
/* 336 */     handlers.add(new RequestResponseBodyMethodProcessor(
/* 337 */       getMessageConverters(), this.contentNegotiationManager, this.responseBodyAdvice));
/*     */     
/*     */ 
/* 340 */     handlers.add(new ViewNameMethodReturnValueHandler());
/* 341 */     handlers.add(new MapMethodProcessor());
/*     */     
/*     */ 
/* 344 */     if (getCustomReturnValueHandlers() != null) {
/* 345 */       handlers.addAll(getCustomReturnValueHandlers());
/*     */     }
/*     */     
/*     */ 
/* 349 */     handlers.add(new ModelAttributeMethodProcessor(true));
/*     */     
/* 351 */     return handlers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception)
/*     */   {
/* 362 */     ServletInvocableHandlerMethod exceptionHandlerMethod = getExceptionHandlerMethod(handlerMethod, exception);
/* 363 */     if (exceptionHandlerMethod == null) {
/* 364 */       return null;
/*     */     }
/*     */     
/* 367 */     exceptionHandlerMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
/* 368 */     exceptionHandlerMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
/*     */     
/* 370 */     ServletWebRequest webRequest = new ServletWebRequest(request, response);
/* 371 */     ModelAndViewContainer mavContainer = new ModelAndViewContainer();
/*     */     try
/*     */     {
/* 374 */       if (this.logger.isDebugEnabled()) {
/* 375 */         this.logger.debug("Invoking @ExceptionHandler method: " + exceptionHandlerMethod);
/*     */       }
/* 377 */       Throwable cause = exception.getCause();
/* 378 */       if (cause != null)
/*     */       {
/* 380 */         exceptionHandlerMethod.invokeAndHandle(webRequest, mavContainer, new Object[] { exception, cause, handlerMethod });
/*     */       }
/*     */       else
/*     */       {
/* 384 */         exceptionHandlerMethod.invokeAndHandle(webRequest, mavContainer, new Object[] { exception, handlerMethod });
/*     */       }
/*     */       
/*     */     }
/*     */     catch (Throwable invocationEx)
/*     */     {
/* 390 */       if ((invocationEx != exception) && (this.logger.isWarnEnabled())) {
/* 391 */         this.logger.warn("Failed to invoke @ExceptionHandler method: " + exceptionHandlerMethod, invocationEx);
/*     */       }
/*     */       
/* 394 */       return null;
/*     */     }
/*     */     
/* 397 */     if (mavContainer.isRequestHandled()) {
/* 398 */       return new ModelAndView();
/*     */     }
/*     */     
/* 401 */     ModelMap model = mavContainer.getModel();
/* 402 */     HttpStatus status = mavContainer.getStatus();
/* 403 */     ModelAndView mav = new ModelAndView(mavContainer.getViewName(), model, status);
/* 404 */     mav.setViewName(mavContainer.getViewName());
/* 405 */     if (!mavContainer.isViewReference()) {
/* 406 */       mav.setView((View)mavContainer.getView());
/*     */     }
/* 408 */     if ((model instanceof RedirectAttributes)) {
/* 409 */       Map<String, ?> flashAttributes = ((RedirectAttributes)model).getFlashAttributes();
/* 410 */       request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 411 */       RequestContextUtils.getOutputFlashMap(request).putAll(flashAttributes);
/*     */     }
/* 413 */     return mav;
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
/*     */   protected ServletInvocableHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception exception)
/*     */   {
/* 428 */     Class<?> handlerType = handlerMethod != null ? handlerMethod.getBeanType() : null;
/*     */     ExceptionHandlerMethodResolver resolver;
/* 430 */     if (handlerMethod != null) {
/* 431 */       resolver = (ExceptionHandlerMethodResolver)this.exceptionHandlerCache.get(handlerType);
/* 432 */       if (resolver == null) {
/* 433 */         resolver = new ExceptionHandlerMethodResolver(handlerType);
/* 434 */         this.exceptionHandlerCache.put(handlerType, resolver);
/*     */       }
/* 436 */       Method method = resolver.resolveMethod(exception);
/* 437 */       if (method != null) {
/* 438 */         return new ServletInvocableHandlerMethod(handlerMethod.getBean(), method);
/*     */       }
/*     */     }
/*     */     
/* 442 */     for (Map.Entry<ControllerAdviceBean, ExceptionHandlerMethodResolver> entry : this.exceptionHandlerAdviceCache.entrySet()) {
/* 443 */       if (((ControllerAdviceBean)entry.getKey()).isApplicableToBeanType(handlerType)) {
/* 444 */         ExceptionHandlerMethodResolver resolver = (ExceptionHandlerMethodResolver)entry.getValue();
/* 445 */         Method method = resolver.resolveMethod(exception);
/* 446 */         if (method != null) {
/* 447 */           return new ServletInvocableHandlerMethod(((ControllerAdviceBean)entry.getKey()).resolveBean(), method);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 452 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\ExceptionHandlerExceptionResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */