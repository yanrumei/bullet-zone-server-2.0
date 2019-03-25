/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.beans.factory.BeanFactory;
/*     */ import org.springframework.beans.factory.BeanFactoryAware;
/*     */ import org.springframework.beans.factory.InitializingBean;
/*     */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.MethodIntrospector;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.annotation.AnnotationAwareOrderComparator;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.task.AsyncTaskExecutor;
/*     */ import org.springframework.core.task.SimpleAsyncTaskExecutor;
/*     */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.converter.StringHttpMessageConverter;
/*     */ import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*     */ import org.springframework.ui.ModelMap;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.ReflectionUtils.MethodFilter;
/*     */ import org.springframework.web.accept.ContentNegotiationManager;
/*     */ import org.springframework.web.bind.annotation.InitBinder;
/*     */ import org.springframework.web.bind.annotation.ModelAttribute;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.support.DefaultDataBinderFactory;
/*     */ import org.springframework.web.bind.support.DefaultSessionAttributeStore;
/*     */ import org.springframework.web.bind.support.SessionAttributeStore;
/*     */ import org.springframework.web.bind.support.WebBindingInitializer;
/*     */ import org.springframework.web.bind.support.WebDataBinderFactory;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.context.request.async.AsyncWebRequest;
/*     */ import org.springframework.web.context.request.async.CallableProcessingInterceptor;
/*     */ import org.springframework.web.context.request.async.DeferredResultProcessingInterceptor;
/*     */ import org.springframework.web.context.request.async.WebAsyncManager;
/*     */ import org.springframework.web.context.request.async.WebAsyncUtils;
/*     */ import org.springframework.web.method.ControllerAdviceBean;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.method.annotation.ErrorsMethodArgumentResolver;
/*     */ import org.springframework.web.method.annotation.ExpressionValueMethodArgumentResolver;
/*     */ import org.springframework.web.method.annotation.InitBinderDataBinderFactory;
/*     */ import org.springframework.web.method.annotation.MapMethodProcessor;
/*     */ import org.springframework.web.method.annotation.ModelAttributeMethodProcessor;
/*     */ import org.springframework.web.method.annotation.ModelFactory;
/*     */ import org.springframework.web.method.annotation.ModelMethodProcessor;
/*     */ import org.springframework.web.method.annotation.RequestHeaderMapMethodArgumentResolver;
/*     */ import org.springframework.web.method.annotation.RequestHeaderMethodArgumentResolver;
/*     */ import org.springframework.web.method.annotation.RequestParamMapMethodArgumentResolver;
/*     */ import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
/*     */ import org.springframework.web.method.annotation.SessionAttributesHandler;
/*     */ import org.springframework.web.method.annotation.SessionStatusMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.HandlerMethodArgumentResolverComposite;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
/*     */ import org.springframework.web.method.support.HandlerMethodReturnValueHandlerComposite;
/*     */ import org.springframework.web.method.support.InvocableHandlerMethod;
/*     */ import org.springframework.web.method.support.ModelAndViewContainer;
/*     */ import org.springframework.web.servlet.FlashMap;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.mvc.annotation.ModelAndViewResolver;
/*     */ import org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter;
/*     */ import org.springframework.web.servlet.mvc.support.RedirectAttributes;
/*     */ import org.springframework.web.servlet.support.RequestContextUtils;
/*     */ import org.springframework.web.util.WebUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RequestMappingHandlerAdapter
/*     */   extends AbstractHandlerMethodAdapter
/*     */   implements BeanFactoryAware, InitializingBean
/*     */ {
/*     */   private List<HandlerMethodArgumentResolver> customArgumentResolvers;
/*     */   private HandlerMethodArgumentResolverComposite argumentResolvers;
/*     */   private HandlerMethodArgumentResolverComposite initBinderArgumentResolvers;
/*     */   private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;
/*     */   private HandlerMethodReturnValueHandlerComposite returnValueHandlers;
/*     */   private List<ModelAndViewResolver> modelAndViewResolvers;
/* 131 */   private ContentNegotiationManager contentNegotiationManager = new ContentNegotiationManager();
/*     */   
/*     */   private List<HttpMessageConverter<?>> messageConverters;
/*     */   
/* 135 */   private List<Object> requestResponseBodyAdvice = new ArrayList();
/*     */   
/*     */   private WebBindingInitializer webBindingInitializer;
/*     */   
/* 139 */   private AsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("MvcAsync");
/*     */   
/*     */   private Long asyncRequestTimeout;
/*     */   
/* 143 */   private CallableProcessingInterceptor[] callableInterceptors = new CallableProcessingInterceptor[0];
/*     */   
/* 145 */   private DeferredResultProcessingInterceptor[] deferredResultInterceptors = new DeferredResultProcessingInterceptor[0];
/*     */   
/* 147 */   private boolean ignoreDefaultModelOnRedirect = false;
/*     */   
/* 149 */   private int cacheSecondsForSessionAttributeHandlers = 0;
/*     */   
/* 151 */   private boolean synchronizeOnSession = false;
/*     */   
/* 153 */   private SessionAttributeStore sessionAttributeStore = new DefaultSessionAttributeStore();
/*     */   
/* 155 */   private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
/*     */   
/*     */ 
/*     */   private ConfigurableBeanFactory beanFactory;
/*     */   
/* 160 */   private final Map<Class<?>, SessionAttributesHandler> sessionAttributesHandlerCache = new ConcurrentHashMap(64);
/*     */   
/*     */ 
/* 163 */   private final Map<Class<?>, Set<Method>> initBinderCache = new ConcurrentHashMap(64);
/*     */   
/* 165 */   private final Map<ControllerAdviceBean, Set<Method>> initBinderAdviceCache = new LinkedHashMap();
/*     */   
/*     */ 
/* 168 */   private final Map<Class<?>, Set<Method>> modelAttributeCache = new ConcurrentHashMap(64);
/*     */   
/* 170 */   private final Map<ControllerAdviceBean, Set<Method>> modelAttributeAdviceCache = new LinkedHashMap();
/*     */   
/*     */ 
/*     */   public RequestMappingHandlerAdapter()
/*     */   {
/* 175 */     StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
/* 176 */     stringHttpMessageConverter.setWriteAcceptCharset(false);
/*     */     
/* 178 */     this.messageConverters = new ArrayList(4);
/* 179 */     this.messageConverters.add(new ByteArrayHttpMessageConverter());
/* 180 */     this.messageConverters.add(stringHttpMessageConverter);
/* 181 */     this.messageConverters.add(new SourceHttpMessageConverter());
/* 182 */     this.messageConverters.add(new AllEncompassingFormHttpMessageConverter());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCustomArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/*     */   {
/* 192 */     this.customArgumentResolvers = argumentResolvers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<HandlerMethodArgumentResolver> getCustomArgumentResolvers()
/*     */   {
/* 199 */     return this.customArgumentResolvers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/*     */   {
/* 207 */     if (argumentResolvers == null) {
/* 208 */       this.argumentResolvers = null;
/*     */     }
/*     */     else {
/* 211 */       this.argumentResolvers = new HandlerMethodArgumentResolverComposite();
/* 212 */       this.argumentResolvers.addResolvers(argumentResolvers);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<HandlerMethodArgumentResolver> getArgumentResolvers()
/*     */   {
/* 221 */     return this.argumentResolvers != null ? this.argumentResolvers.getResolvers() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setInitBinderArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
/*     */   {
/* 228 */     if (argumentResolvers == null) {
/* 229 */       this.initBinderArgumentResolvers = null;
/*     */     }
/*     */     else {
/* 232 */       this.initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite();
/* 233 */       this.initBinderArgumentResolvers.addResolvers(argumentResolvers);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<HandlerMethodArgumentResolver> getInitBinderArgumentResolvers()
/*     */   {
/* 242 */     return this.initBinderArgumentResolvers != null ? this.initBinderArgumentResolvers.getResolvers() : null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCustomReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers)
/*     */   {
/* 251 */     this.customReturnValueHandlers = returnValueHandlers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<HandlerMethodReturnValueHandler> getCustomReturnValueHandlers()
/*     */   {
/* 258 */     return this.customReturnValueHandlers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setReturnValueHandlers(List<HandlerMethodReturnValueHandler> returnValueHandlers)
/*     */   {
/* 266 */     if (returnValueHandlers == null) {
/* 267 */       this.returnValueHandlers = null;
/*     */     }
/*     */     else {
/* 270 */       this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
/* 271 */       this.returnValueHandlers.addHandlers(returnValueHandlers);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public List<HandlerMethodReturnValueHandler> getReturnValueHandlers()
/*     */   {
/* 280 */     return this.returnValueHandlers != null ? this.returnValueHandlers.getHandlers() : null;
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
/*     */   public void setModelAndViewResolvers(List<ModelAndViewResolver> modelAndViewResolvers)
/*     */   {
/* 298 */     this.modelAndViewResolvers = modelAndViewResolvers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<ModelAndViewResolver> getModelAndViewResolvers()
/*     */   {
/* 305 */     return this.modelAndViewResolvers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setContentNegotiationManager(ContentNegotiationManager contentNegotiationManager)
/*     */   {
/* 313 */     this.contentNegotiationManager = contentNegotiationManager;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMessageConverters(List<HttpMessageConverter<?>> messageConverters)
/*     */   {
/* 322 */     this.messageConverters = messageConverters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<HttpMessageConverter<?>> getMessageConverters()
/*     */   {
/* 329 */     return this.messageConverters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setRequestBodyAdvice(List<RequestBodyAdvice> requestBodyAdvice)
/*     */   {
/* 338 */     if (requestBodyAdvice != null) {
/* 339 */       this.requestResponseBodyAdvice.addAll(requestBodyAdvice);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setResponseBodyAdvice(List<ResponseBodyAdvice<?>> responseBodyAdvice)
/*     */   {
/* 349 */     if (responseBodyAdvice != null) {
/* 350 */       this.requestResponseBodyAdvice.addAll(responseBodyAdvice);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer)
/*     */   {
/* 359 */     this.webBindingInitializer = webBindingInitializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public WebBindingInitializer getWebBindingInitializer()
/*     */   {
/* 366 */     return this.webBindingInitializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setTaskExecutor(AsyncTaskExecutor taskExecutor)
/*     */   {
/* 378 */     this.taskExecutor = taskExecutor;
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
/*     */   public void setAsyncRequestTimeout(long timeout)
/*     */   {
/* 391 */     this.asyncRequestTimeout = Long.valueOf(timeout);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCallableInterceptors(List<CallableProcessingInterceptor> interceptors)
/*     */   {
/* 399 */     Assert.notNull(interceptors, "CallableProcessingInterceptor List must not be null");
/* 400 */     this.callableInterceptors = ((CallableProcessingInterceptor[])interceptors.toArray(new CallableProcessingInterceptor[interceptors.size()]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setDeferredResultInterceptors(List<DeferredResultProcessingInterceptor> interceptors)
/*     */   {
/* 408 */     Assert.notNull(interceptors, "DeferredResultProcessingInterceptor List must not be null");
/* 409 */     this.deferredResultInterceptors = ((DeferredResultProcessingInterceptor[])interceptors.toArray(new DeferredResultProcessingInterceptor[interceptors.size()]));
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
/*     */   public void setIgnoreDefaultModelOnRedirect(boolean ignoreDefaultModelOnRedirect)
/*     */   {
/* 427 */     this.ignoreDefaultModelOnRedirect = ignoreDefaultModelOnRedirect;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSessionAttributeStore(SessionAttributeStore sessionAttributeStore)
/*     */   {
/* 437 */     this.sessionAttributeStore = sessionAttributeStore;
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
/*     */   public void setCacheSecondsForSessionAttributeHandlers(int cacheSecondsForSessionAttributeHandlers)
/*     */   {
/* 457 */     this.cacheSecondsForSessionAttributeHandlers = cacheSecondsForSessionAttributeHandlers;
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
/*     */   public void setSynchronizeOnSession(boolean synchronizeOnSession)
/*     */   {
/* 479 */     this.synchronizeOnSession = synchronizeOnSession;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer)
/*     */   {
/* 488 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setBeanFactory(BeanFactory beanFactory)
/*     */   {
/* 497 */     if ((beanFactory instanceof ConfigurableBeanFactory)) {
/* 498 */       this.beanFactory = ((ConfigurableBeanFactory)beanFactory);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ConfigurableBeanFactory getBeanFactory()
/*     */   {
/* 506 */     return this.beanFactory;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void afterPropertiesSet()
/*     */   {
/* 513 */     initControllerAdviceCache();
/*     */     
/* 515 */     if (this.argumentResolvers == null) {
/* 516 */       List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
/* 517 */       this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
/*     */     }
/* 519 */     if (this.initBinderArgumentResolvers == null) {
/* 520 */       List<HandlerMethodArgumentResolver> resolvers = getDefaultInitBinderArgumentResolvers();
/* 521 */       this.initBinderArgumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
/*     */     }
/* 523 */     if (this.returnValueHandlers == null) {
/* 524 */       List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
/* 525 */       this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite().addHandlers(handlers);
/*     */     }
/*     */   }
/*     */   
/*     */   private void initControllerAdviceCache() {
/* 530 */     if (getApplicationContext() == null) {
/* 531 */       return;
/*     */     }
/* 533 */     if (this.logger.isInfoEnabled()) {
/* 534 */       this.logger.info("Looking for @ControllerAdvice: " + getApplicationContext());
/*     */     }
/*     */     
/* 537 */     List<ControllerAdviceBean> beans = ControllerAdviceBean.findAnnotatedBeans(getApplicationContext());
/* 538 */     AnnotationAwareOrderComparator.sort(beans);
/*     */     
/* 540 */     List<Object> requestResponseBodyAdviceBeans = new ArrayList();
/*     */     
/* 542 */     for (ControllerAdviceBean bean : beans) {
/* 543 */       Set<Method> attrMethods = MethodIntrospector.selectMethods(bean.getBeanType(), MODEL_ATTRIBUTE_METHODS);
/* 544 */       if (!attrMethods.isEmpty()) {
/* 545 */         this.modelAttributeAdviceCache.put(bean, attrMethods);
/* 546 */         if (this.logger.isInfoEnabled()) {
/* 547 */           this.logger.info("Detected @ModelAttribute methods in " + bean);
/*     */         }
/*     */       }
/* 550 */       Set<Method> binderMethods = MethodIntrospector.selectMethods(bean.getBeanType(), INIT_BINDER_METHODS);
/* 551 */       if (!binderMethods.isEmpty()) {
/* 552 */         this.initBinderAdviceCache.put(bean, binderMethods);
/* 553 */         if (this.logger.isInfoEnabled()) {
/* 554 */           this.logger.info("Detected @InitBinder methods in " + bean);
/*     */         }
/*     */       }
/* 557 */       if (RequestBodyAdvice.class.isAssignableFrom(bean.getBeanType())) {
/* 558 */         requestResponseBodyAdviceBeans.add(bean);
/* 559 */         if (this.logger.isInfoEnabled()) {
/* 560 */           this.logger.info("Detected RequestBodyAdvice bean in " + bean);
/*     */         }
/*     */       }
/* 563 */       if (ResponseBodyAdvice.class.isAssignableFrom(bean.getBeanType())) {
/* 564 */         requestResponseBodyAdviceBeans.add(bean);
/* 565 */         if (this.logger.isInfoEnabled()) {
/* 566 */           this.logger.info("Detected ResponseBodyAdvice bean in " + bean);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 571 */     if (!requestResponseBodyAdviceBeans.isEmpty()) {
/* 572 */       this.requestResponseBodyAdvice.addAll(0, requestResponseBodyAdviceBeans);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers()
/*     */   {
/* 581 */     List<HandlerMethodArgumentResolver> resolvers = new ArrayList();
/*     */     
/*     */ 
/* 584 */     resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
/* 585 */     resolvers.add(new RequestParamMapMethodArgumentResolver());
/* 586 */     resolvers.add(new PathVariableMethodArgumentResolver());
/* 587 */     resolvers.add(new PathVariableMapMethodArgumentResolver());
/* 588 */     resolvers.add(new MatrixVariableMethodArgumentResolver());
/* 589 */     resolvers.add(new MatrixVariableMapMethodArgumentResolver());
/* 590 */     resolvers.add(new ServletModelAttributeMethodProcessor(false));
/* 591 */     resolvers.add(new RequestResponseBodyMethodProcessor(getMessageConverters(), this.requestResponseBodyAdvice));
/* 592 */     resolvers.add(new RequestPartMethodArgumentResolver(getMessageConverters(), this.requestResponseBodyAdvice));
/* 593 */     resolvers.add(new RequestHeaderMethodArgumentResolver(getBeanFactory()));
/* 594 */     resolvers.add(new RequestHeaderMapMethodArgumentResolver());
/* 595 */     resolvers.add(new ServletCookieValueMethodArgumentResolver(getBeanFactory()));
/* 596 */     resolvers.add(new ExpressionValueMethodArgumentResolver(getBeanFactory()));
/* 597 */     resolvers.add(new SessionAttributeMethodArgumentResolver());
/* 598 */     resolvers.add(new RequestAttributeMethodArgumentResolver());
/*     */     
/*     */ 
/* 601 */     resolvers.add(new ServletRequestMethodArgumentResolver());
/* 602 */     resolvers.add(new ServletResponseMethodArgumentResolver());
/* 603 */     resolvers.add(new HttpEntityMethodProcessor(getMessageConverters(), this.requestResponseBodyAdvice));
/* 604 */     resolvers.add(new RedirectAttributesMethodArgumentResolver());
/* 605 */     resolvers.add(new ModelMethodProcessor());
/* 606 */     resolvers.add(new MapMethodProcessor());
/* 607 */     resolvers.add(new ErrorsMethodArgumentResolver());
/* 608 */     resolvers.add(new SessionStatusMethodArgumentResolver());
/* 609 */     resolvers.add(new UriComponentsBuilderMethodArgumentResolver());
/*     */     
/*     */ 
/* 612 */     if (getCustomArgumentResolvers() != null) {
/* 613 */       resolvers.addAll(getCustomArgumentResolvers());
/*     */     }
/*     */     
/*     */ 
/* 617 */     resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), true));
/* 618 */     resolvers.add(new ServletModelAttributeMethodProcessor(true));
/*     */     
/* 620 */     return resolvers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<HandlerMethodArgumentResolver> getDefaultInitBinderArgumentResolvers()
/*     */   {
/* 628 */     List<HandlerMethodArgumentResolver> resolvers = new ArrayList();
/*     */     
/*     */ 
/* 631 */     resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), false));
/* 632 */     resolvers.add(new RequestParamMapMethodArgumentResolver());
/* 633 */     resolvers.add(new PathVariableMethodArgumentResolver());
/* 634 */     resolvers.add(new PathVariableMapMethodArgumentResolver());
/* 635 */     resolvers.add(new MatrixVariableMethodArgumentResolver());
/* 636 */     resolvers.add(new MatrixVariableMapMethodArgumentResolver());
/* 637 */     resolvers.add(new ExpressionValueMethodArgumentResolver(getBeanFactory()));
/* 638 */     resolvers.add(new SessionAttributeMethodArgumentResolver());
/* 639 */     resolvers.add(new RequestAttributeMethodArgumentResolver());
/*     */     
/*     */ 
/* 642 */     resolvers.add(new ServletRequestMethodArgumentResolver());
/* 643 */     resolvers.add(new ServletResponseMethodArgumentResolver());
/*     */     
/*     */ 
/* 646 */     if (getCustomArgumentResolvers() != null) {
/* 647 */       resolvers.addAll(getCustomArgumentResolvers());
/*     */     }
/*     */     
/*     */ 
/* 651 */     resolvers.add(new RequestParamMethodArgumentResolver(getBeanFactory(), true));
/*     */     
/* 653 */     return resolvers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers()
/*     */   {
/* 661 */     List<HandlerMethodReturnValueHandler> handlers = new ArrayList();
/*     */     
/*     */ 
/* 664 */     handlers.add(new ModelAndViewMethodReturnValueHandler());
/* 665 */     handlers.add(new ModelMethodProcessor());
/* 666 */     handlers.add(new ViewMethodReturnValueHandler());
/* 667 */     handlers.add(new ResponseBodyEmitterReturnValueHandler(getMessageConverters()));
/* 668 */     handlers.add(new StreamingResponseBodyReturnValueHandler());
/* 669 */     handlers.add(new HttpEntityMethodProcessor(getMessageConverters(), this.contentNegotiationManager, this.requestResponseBodyAdvice));
/*     */     
/* 671 */     handlers.add(new HttpHeadersReturnValueHandler());
/* 672 */     handlers.add(new CallableMethodReturnValueHandler());
/* 673 */     handlers.add(new DeferredResultMethodReturnValueHandler());
/* 674 */     handlers.add(new AsyncTaskMethodReturnValueHandler(this.beanFactory));
/*     */     
/*     */ 
/* 677 */     handlers.add(new ModelAttributeMethodProcessor(false));
/* 678 */     handlers.add(new RequestResponseBodyMethodProcessor(getMessageConverters(), this.contentNegotiationManager, this.requestResponseBodyAdvice));
/*     */     
/*     */ 
/*     */ 
/* 682 */     handlers.add(new ViewNameMethodReturnValueHandler());
/* 683 */     handlers.add(new MapMethodProcessor());
/*     */     
/*     */ 
/* 686 */     if (getCustomReturnValueHandlers() != null) {
/* 687 */       handlers.addAll(getCustomReturnValueHandlers());
/*     */     }
/*     */     
/*     */ 
/* 691 */     if (!CollectionUtils.isEmpty(getModelAndViewResolvers())) {
/* 692 */       handlers.add(new ModelAndViewResolverMethodReturnValueHandler(getModelAndViewResolvers()));
/*     */     }
/*     */     else {
/* 695 */       handlers.add(new ModelAttributeMethodProcessor(true));
/*     */     }
/*     */     
/* 698 */     return handlers;
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
/*     */   protected boolean supportsInternal(HandlerMethod handlerMethod)
/*     */   {
/* 712 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod)
/*     */     throws Exception
/*     */   {
/* 720 */     checkRequest(request);
/*     */     ModelAndView mav;
/*     */     ModelAndView mav;
/* 723 */     if (this.synchronizeOnSession) {
/* 724 */       HttpSession session = request.getSession(false);
/* 725 */       ModelAndView mav; if (session != null) {
/* 726 */         Object mutex = WebUtils.getSessionMutex(session);
/* 727 */         ModelAndView mav; synchronized (mutex) {
/* 728 */           mav = invokeHandlerMethod(request, response, handlerMethod);
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 733 */         mav = invokeHandlerMethod(request, response, handlerMethod);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 738 */       mav = invokeHandlerMethod(request, response, handlerMethod);
/*     */     }
/*     */     
/* 741 */     if (!response.containsHeader("Cache-Control")) {
/* 742 */       if (getSessionAttributesHandler(handlerMethod).hasSessionAttributes()) {
/* 743 */         applyCacheSeconds(response, this.cacheSecondsForSessionAttributeHandlers);
/*     */       }
/*     */       else {
/* 746 */         prepareResponse(response);
/*     */       }
/*     */     }
/*     */     
/* 750 */     return mav;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected long getLastModifiedInternal(HttpServletRequest request, HandlerMethod handlerMethod)
/*     */   {
/* 760 */     return -1L;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private SessionAttributesHandler getSessionAttributesHandler(HandlerMethod handlerMethod)
/*     */   {
/* 769 */     Class<?> handlerType = handlerMethod.getBeanType();
/* 770 */     SessionAttributesHandler sessionAttrHandler = (SessionAttributesHandler)this.sessionAttributesHandlerCache.get(handlerType);
/* 771 */     if (sessionAttrHandler == null) {
/* 772 */       synchronized (this.sessionAttributesHandlerCache) {
/* 773 */         sessionAttrHandler = (SessionAttributesHandler)this.sessionAttributesHandlerCache.get(handlerType);
/* 774 */         if (sessionAttrHandler == null) {
/* 775 */           sessionAttrHandler = new SessionAttributesHandler(handlerType, this.sessionAttributeStore);
/* 776 */           this.sessionAttributesHandlerCache.put(handlerType, sessionAttrHandler);
/*     */         }
/*     */       }
/*     */     }
/* 780 */     return sessionAttrHandler;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod)
/*     */     throws Exception
/*     */   {
/* 792 */     ServletWebRequest webRequest = new ServletWebRequest(request, response);
/*     */     try {
/* 794 */       WebDataBinderFactory binderFactory = getDataBinderFactory(handlerMethod);
/* 795 */       ModelFactory modelFactory = getModelFactory(handlerMethod, binderFactory);
/*     */       
/* 797 */       ServletInvocableHandlerMethod invocableMethod = createInvocableHandlerMethod(handlerMethod);
/* 798 */       invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
/* 799 */       invocableMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
/* 800 */       invocableMethod.setDataBinderFactory(binderFactory);
/* 801 */       invocableMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
/*     */       
/* 803 */       ModelAndViewContainer mavContainer = new ModelAndViewContainer();
/* 804 */       mavContainer.addAllAttributes(RequestContextUtils.getInputFlashMap(request));
/* 805 */       modelFactory.initModel(webRequest, mavContainer, invocableMethod);
/* 806 */       mavContainer.setIgnoreDefaultModelOnRedirect(this.ignoreDefaultModelOnRedirect);
/*     */       
/* 808 */       AsyncWebRequest asyncWebRequest = WebAsyncUtils.createAsyncWebRequest(request, response);
/* 809 */       asyncWebRequest.setTimeout(this.asyncRequestTimeout);
/*     */       
/* 811 */       WebAsyncManager asyncManager = WebAsyncUtils.getAsyncManager(request);
/* 812 */       asyncManager.setTaskExecutor(this.taskExecutor);
/* 813 */       asyncManager.setAsyncWebRequest(asyncWebRequest);
/* 814 */       asyncManager.registerCallableInterceptors(this.callableInterceptors);
/* 815 */       asyncManager.registerDeferredResultInterceptors(this.deferredResultInterceptors);
/*     */       Object result;
/* 817 */       if (asyncManager.hasConcurrentResult()) {
/* 818 */         result = asyncManager.getConcurrentResult();
/* 819 */         mavContainer = (ModelAndViewContainer)asyncManager.getConcurrentResultContext()[0];
/* 820 */         asyncManager.clearConcurrentResult();
/* 821 */         if (this.logger.isDebugEnabled()) {
/* 822 */           this.logger.debug("Found concurrent result value [" + result + "]");
/*     */         }
/* 824 */         invocableMethod = invocableMethod.wrapConcurrentResult(result);
/*     */       }
/*     */       
/* 827 */       invocableMethod.invokeAndHandle(webRequest, mavContainer, new Object[0]);
/* 828 */       if (asyncManager.isConcurrentHandlingStarted()) {
/* 829 */         return null;
/*     */       }
/*     */       
/* 832 */       return getModelAndView(mavContainer, modelFactory, webRequest);
/*     */     }
/*     */     finally {
/* 835 */       webRequest.requestCompleted();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ServletInvocableHandlerMethod createInvocableHandlerMethod(HandlerMethod handlerMethod)
/*     */   {
/* 846 */     return new ServletInvocableHandlerMethod(handlerMethod);
/*     */   }
/*     */   
/*     */   private ModelFactory getModelFactory(HandlerMethod handlerMethod, WebDataBinderFactory binderFactory) {
/* 850 */     SessionAttributesHandler sessionAttrHandler = getSessionAttributesHandler(handlerMethod);
/* 851 */     Class<?> handlerType = handlerMethod.getBeanType();
/* 852 */     Set<Method> methods = (Set)this.modelAttributeCache.get(handlerType);
/* 853 */     if (methods == null) {
/* 854 */       methods = MethodIntrospector.selectMethods(handlerType, MODEL_ATTRIBUTE_METHODS);
/* 855 */       this.modelAttributeCache.put(handlerType, methods);
/*     */     }
/* 857 */     List<InvocableHandlerMethod> attrMethods = new ArrayList();
/*     */     
/* 859 */     for (Map.Entry<ControllerAdviceBean, Set<Method>> entry : this.modelAttributeAdviceCache.entrySet()) {
/* 860 */       if (((ControllerAdviceBean)entry.getKey()).isApplicableToBeanType(handlerType)) {
/* 861 */         bean = ((ControllerAdviceBean)entry.getKey()).resolveBean();
/* 862 */         for (Method method : (Set)entry.getValue())
/* 863 */           attrMethods.add(createModelAttributeMethod(binderFactory, bean, method));
/*     */       }
/*     */     }
/*     */     Object bean;
/* 867 */     for (Method method : methods) {
/* 868 */       Object bean = handlerMethod.getBean();
/* 869 */       attrMethods.add(createModelAttributeMethod(binderFactory, bean, method));
/*     */     }
/* 871 */     return new ModelFactory(attrMethods, binderFactory, sessionAttrHandler);
/*     */   }
/*     */   
/*     */   private InvocableHandlerMethod createModelAttributeMethod(WebDataBinderFactory factory, Object bean, Method method) {
/* 875 */     InvocableHandlerMethod attrMethod = new InvocableHandlerMethod(bean, method);
/* 876 */     attrMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
/* 877 */     attrMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
/* 878 */     attrMethod.setDataBinderFactory(factory);
/* 879 */     return attrMethod;
/*     */   }
/*     */   
/*     */   private WebDataBinderFactory getDataBinderFactory(HandlerMethod handlerMethod) throws Exception {
/* 883 */     Class<?> handlerType = handlerMethod.getBeanType();
/* 884 */     Set<Method> methods = (Set)this.initBinderCache.get(handlerType);
/* 885 */     if (methods == null) {
/* 886 */       methods = MethodIntrospector.selectMethods(handlerType, INIT_BINDER_METHODS);
/* 887 */       this.initBinderCache.put(handlerType, methods);
/*     */     }
/* 889 */     List<InvocableHandlerMethod> initBinderMethods = new ArrayList();
/*     */     
/* 891 */     for (Map.Entry<ControllerAdviceBean, Set<Method>> entry : this.initBinderAdviceCache.entrySet()) {
/* 892 */       if (((ControllerAdviceBean)entry.getKey()).isApplicableToBeanType(handlerType)) {
/* 893 */         bean = ((ControllerAdviceBean)entry.getKey()).resolveBean();
/* 894 */         for (Method method : (Set)entry.getValue())
/* 895 */           initBinderMethods.add(createInitBinderMethod(bean, method));
/*     */       }
/*     */     }
/*     */     Object bean;
/* 899 */     for (Method method : methods) {
/* 900 */       Object bean = handlerMethod.getBean();
/* 901 */       initBinderMethods.add(createInitBinderMethod(bean, method));
/*     */     }
/* 903 */     return createDataBinderFactory(initBinderMethods);
/*     */   }
/*     */   
/*     */   private InvocableHandlerMethod createInitBinderMethod(Object bean, Method method) {
/* 907 */     InvocableHandlerMethod binderMethod = new InvocableHandlerMethod(bean, method);
/* 908 */     binderMethod.setHandlerMethodArgumentResolvers(this.initBinderArgumentResolvers);
/* 909 */     binderMethod.setDataBinderFactory(new DefaultDataBinderFactory(this.webBindingInitializer));
/* 910 */     binderMethod.setParameterNameDiscoverer(this.parameterNameDiscoverer);
/* 911 */     return binderMethod;
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
/*     */   protected InitBinderDataBinderFactory createDataBinderFactory(List<InvocableHandlerMethod> binderMethods)
/*     */     throws Exception
/*     */   {
/* 925 */     return new ServletRequestDataBinderFactory(binderMethods, getWebBindingInitializer());
/*     */   }
/*     */   
/*     */   private ModelAndView getModelAndView(ModelAndViewContainer mavContainer, ModelFactory modelFactory, NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/* 931 */     modelFactory.updateModel(webRequest, mavContainer);
/* 932 */     if (mavContainer.isRequestHandled()) {
/* 933 */       return null;
/*     */     }
/* 935 */     ModelMap model = mavContainer.getModel();
/* 936 */     ModelAndView mav = new ModelAndView(mavContainer.getViewName(), model, mavContainer.getStatus());
/* 937 */     if (!mavContainer.isViewReference()) {
/* 938 */       mav.setView((View)mavContainer.getView());
/*     */     }
/* 940 */     if ((model instanceof RedirectAttributes)) {
/* 941 */       Map<String, ?> flashAttributes = ((RedirectAttributes)model).getFlashAttributes();
/* 942 */       HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 943 */       RequestContextUtils.getOutputFlashMap(request).putAll(flashAttributes);
/*     */     }
/* 945 */     return mav;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 952 */   public static final ReflectionUtils.MethodFilter INIT_BINDER_METHODS = new ReflectionUtils.MethodFilter()
/*     */   {
/*     */     public boolean matches(Method method) {
/* 955 */       return AnnotationUtils.findAnnotation(method, InitBinder.class) != null;
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 962 */   public static final ReflectionUtils.MethodFilter MODEL_ATTRIBUTE_METHODS = new ReflectionUtils.MethodFilter()
/*     */   {
/*     */     public boolean matches(Method method) {
/* 965 */       return (AnnotationUtils.findAnnotation(method, RequestMapping.class) == null) && 
/* 966 */         (AnnotationUtils.findAnnotation(method, ModelAttribute.class) != null);
/*     */     }
/*     */   };
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\RequestMappingHandlerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */