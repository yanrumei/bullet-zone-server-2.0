/*      */ package org.springframework.web.servlet.mvc.annotation;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Reader;
/*      */ import java.io.Writer;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.Principal;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import javax.servlet.ServletException;
/*      */ import javax.servlet.ServletRequest;
/*      */ import javax.servlet.ServletResponse;
/*      */ import javax.servlet.http.Cookie;
/*      */ import javax.servlet.http.HttpServletRequest;
/*      */ import javax.servlet.http.HttpServletResponse;
/*      */ import javax.servlet.http.HttpSession;
/*      */ import org.apache.commons.logging.Log;
/*      */ import org.apache.commons.logging.LogFactory;
/*      */ import org.springframework.beans.BeanUtils;
/*      */ import org.springframework.beans.factory.BeanFactory;
/*      */ import org.springframework.beans.factory.BeanFactoryAware;
/*      */ import org.springframework.beans.factory.config.BeanExpressionContext;
/*      */ import org.springframework.beans.factory.config.BeanExpressionResolver;
/*      */ import org.springframework.beans.factory.config.ConfigurableBeanFactory;
/*      */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*      */ import org.springframework.core.Ordered;
/*      */ import org.springframework.core.ParameterNameDiscoverer;
/*      */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*      */ import org.springframework.core.annotation.AnnotationUtils;
/*      */ import org.springframework.http.HttpEntity;
/*      */ import org.springframework.http.HttpHeaders;
/*      */ import org.springframework.http.HttpInputMessage;
/*      */ import org.springframework.http.HttpOutputMessage;
/*      */ import org.springframework.http.HttpStatus;
/*      */ import org.springframework.http.MediaType;
/*      */ import org.springframework.http.ResponseEntity;
/*      */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*      */ import org.springframework.http.converter.HttpMessageConverter;
/*      */ import org.springframework.http.converter.StringHttpMessageConverter;
/*      */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*      */ import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
/*      */ import org.springframework.http.server.ServerHttpRequest;
/*      */ import org.springframework.http.server.ServerHttpResponse;
/*      */ import org.springframework.http.server.ServletServerHttpRequest;
/*      */ import org.springframework.http.server.ServletServerHttpResponse;
/*      */ import org.springframework.ui.ExtendedModelMap;
/*      */ import org.springframework.ui.Model;
/*      */ import org.springframework.ui.ModelMap;
/*      */ import org.springframework.util.AntPathMatcher;
/*      */ import org.springframework.util.Assert;
/*      */ import org.springframework.util.ClassUtils;
/*      */ import org.springframework.util.ObjectUtils;
/*      */ import org.springframework.util.PathMatcher;
/*      */ import org.springframework.util.StringUtils;
/*      */ import org.springframework.validation.support.BindingAwareModelMap;
/*      */ import org.springframework.web.HttpMediaTypeNotAcceptableException;
/*      */ import org.springframework.web.HttpRequestMethodNotSupportedException;
/*      */ import org.springframework.web.HttpSessionRequiredException;
/*      */ import org.springframework.web.bind.MissingServletRequestParameterException;
/*      */ import org.springframework.web.bind.ServletRequestDataBinder;
/*      */ import org.springframework.web.bind.WebDataBinder;
/*      */ import org.springframework.web.bind.annotation.ModelAttribute;
/*      */ import org.springframework.web.bind.annotation.RequestMapping;
/*      */ import org.springframework.web.bind.annotation.RequestMethod;
/*      */ import org.springframework.web.bind.annotation.ResponseBody;
/*      */ import org.springframework.web.bind.annotation.ResponseStatus;
/*      */ import org.springframework.web.bind.annotation.support.HandlerMethodInvoker;
/*      */ import org.springframework.web.bind.annotation.support.HandlerMethodResolver;
/*      */ import org.springframework.web.bind.support.DefaultSessionAttributeStore;
/*      */ import org.springframework.web.bind.support.SessionAttributeStore;
/*      */ import org.springframework.web.bind.support.WebArgumentResolver;
/*      */ import org.springframework.web.bind.support.WebBindingInitializer;
/*      */ import org.springframework.web.context.request.NativeWebRequest;
/*      */ import org.springframework.web.context.request.RequestScope;
/*      */ import org.springframework.web.context.request.ServletWebRequest;
/*      */ import org.springframework.web.multipart.MultipartRequest;
/*      */ import org.springframework.web.servlet.HandlerAdapter;
/*      */ import org.springframework.web.servlet.HandlerMapping;
/*      */ import org.springframework.web.servlet.ModelAndView;
/*      */ import org.springframework.web.servlet.View;
/*      */ import org.springframework.web.servlet.mvc.multiaction.InternalPathMethodNameResolver;
/*      */ import org.springframework.web.servlet.mvc.multiaction.MethodNameResolver;
/*      */ import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;
/*      */ import org.springframework.web.servlet.support.RequestContextUtils;
/*      */ import org.springframework.web.servlet.support.WebContentGenerator;
/*      */ import org.springframework.web.util.UrlPathHelper;
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
/*      */ @Deprecated
/*      */ public class AnnotationMethodHandlerAdapter
/*      */   extends WebContentGenerator
/*      */   implements HandlerAdapter, Ordered, BeanFactoryAware
/*      */ {
/*      */   public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
/*  155 */   protected static final Log pageNotFoundLogger = LogFactory.getLog("org.springframework.web.servlet.PageNotFound");
/*      */   
/*      */ 
/*  158 */   private UrlPathHelper urlPathHelper = new UrlPathHelper();
/*      */   
/*  160 */   private PathMatcher pathMatcher = new AntPathMatcher();
/*      */   
/*  162 */   private MethodNameResolver methodNameResolver = new InternalPathMethodNameResolver();
/*      */   
/*      */ 
/*      */   private WebBindingInitializer webBindingInitializer;
/*      */   
/*  167 */   private SessionAttributeStore sessionAttributeStore = new DefaultSessionAttributeStore();
/*      */   
/*  169 */   private int cacheSecondsForSessionAttributeHandlers = 0;
/*      */   
/*  171 */   private boolean synchronizeOnSession = false;
/*      */   
/*  173 */   private ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
/*      */   
/*      */   private WebArgumentResolver[] customArgumentResolvers;
/*      */   
/*      */   private ModelAndViewResolver[] customModelAndViewResolvers;
/*      */   
/*      */   private HttpMessageConverter<?>[] messageConverters;
/*      */   
/*  181 */   private int order = Integer.MAX_VALUE;
/*      */   
/*      */   private ConfigurableBeanFactory beanFactory;
/*      */   
/*      */   private BeanExpressionContext expressionContext;
/*      */   
/*  187 */   private final Map<Class<?>, ServletHandlerMethodResolver> methodResolverCache = new ConcurrentHashMap(64);
/*      */   
/*      */ 
/*  190 */   private final Map<Class<?>, Boolean> sessionAnnotatedClassesCache = new ConcurrentHashMap(64);
/*      */   
/*      */ 
/*      */   public AnnotationMethodHandlerAdapter()
/*      */   {
/*  195 */     super(false);
/*      */     
/*      */ 
/*  198 */     StringHttpMessageConverter stringHttpMessageConverter = new StringHttpMessageConverter();
/*  199 */     stringHttpMessageConverter.setWriteAcceptCharset(false);
/*  200 */     this.messageConverters = new HttpMessageConverter[] { new ByteArrayHttpMessageConverter(), stringHttpMessageConverter, new SourceHttpMessageConverter(), new XmlAwareFormHttpMessageConverter() };
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
/*      */   public void setAlwaysUseFullPath(boolean alwaysUseFullPath)
/*      */   {
/*  215 */     this.urlPathHelper.setAlwaysUseFullPath(alwaysUseFullPath);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUrlDecode(boolean urlDecode)
/*      */   {
/*  226 */     this.urlPathHelper.setUrlDecode(urlDecode);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setUrlPathHelper(UrlPathHelper urlPathHelper)
/*      */   {
/*  235 */     Assert.notNull(urlPathHelper, "UrlPathHelper must not be null");
/*  236 */     this.urlPathHelper = urlPathHelper;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setPathMatcher(PathMatcher pathMatcher)
/*      */   {
/*  244 */     Assert.notNull(pathMatcher, "PathMatcher must not be null");
/*  245 */     this.pathMatcher = pathMatcher;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMethodNameResolver(MethodNameResolver methodNameResolver)
/*      */   {
/*  255 */     this.methodNameResolver = methodNameResolver;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setWebBindingInitializer(WebBindingInitializer webBindingInitializer)
/*      */   {
/*  263 */     this.webBindingInitializer = webBindingInitializer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setSessionAttributeStore(SessionAttributeStore sessionAttributeStore)
/*      */   {
/*  272 */     Assert.notNull(sessionAttributeStore, "SessionAttributeStore must not be null");
/*  273 */     this.sessionAttributeStore = sessionAttributeStore;
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
/*      */   public void setCacheSecondsForSessionAttributeHandlers(int cacheSecondsForSessionAttributeHandlers)
/*      */   {
/*  286 */     this.cacheSecondsForSessionAttributeHandlers = cacheSecondsForSessionAttributeHandlers;
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
/*      */   public void setSynchronizeOnSession(boolean synchronizeOnSession)
/*      */   {
/*  308 */     this.synchronizeOnSession = synchronizeOnSession;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setParameterNameDiscoverer(ParameterNameDiscoverer parameterNameDiscoverer)
/*      */   {
/*  317 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCustomArgumentResolver(WebArgumentResolver argumentResolver)
/*      */   {
/*  326 */     this.customArgumentResolvers = new WebArgumentResolver[] { argumentResolver };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCustomArgumentResolvers(WebArgumentResolver... argumentResolvers)
/*      */   {
/*  335 */     this.customArgumentResolvers = argumentResolvers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCustomModelAndViewResolver(ModelAndViewResolver customModelAndViewResolver)
/*      */   {
/*  344 */     this.customModelAndViewResolvers = new ModelAndViewResolver[] { customModelAndViewResolver };
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setCustomModelAndViewResolvers(ModelAndViewResolver... customModelAndViewResolvers)
/*      */   {
/*  353 */     this.customModelAndViewResolvers = customModelAndViewResolvers;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setMessageConverters(HttpMessageConverter<?>[] messageConverters)
/*      */   {
/*  361 */     this.messageConverters = messageConverters;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   public HttpMessageConverter<?>[] getMessageConverters()
/*      */   {
/*  368 */     return this.messageConverters;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   public void setOrder(int order)
/*      */   {
/*  377 */     this.order = order;
/*      */   }
/*      */   
/*      */   public int getOrder()
/*      */   {
/*  382 */     return this.order;
/*      */   }
/*      */   
/*      */   public void setBeanFactory(BeanFactory beanFactory)
/*      */   {
/*  387 */     if ((beanFactory instanceof ConfigurableBeanFactory)) {
/*  388 */       this.beanFactory = ((ConfigurableBeanFactory)beanFactory);
/*  389 */       this.expressionContext = new BeanExpressionContext(this.beanFactory, new RequestScope());
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */   public boolean supports(Object handler)
/*      */   {
/*  396 */     return getMethodResolver(handler).hasHandlerMethods();
/*      */   }
/*      */   
/*      */   /* Error */
/*      */   public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
/*      */     throws Exception
/*      */   {
/*      */     // Byte code:
/*      */     //   0: aload_3
/*      */     //   1: invokestatic 58	org/springframework/util/ClassUtils:getUserClass	(Ljava/lang/Object;)Ljava/lang/Class;
/*      */     //   4: astore 4
/*      */     //   6: aload_0
/*      */     //   7: getfield 32	org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerAdapter:sessionAnnotatedClassesCache	Ljava/util/Map;
/*      */     //   10: aload 4
/*      */     //   12: invokeinterface 59 2 0
/*      */     //   17: checkcast 60	java/lang/Boolean
/*      */     //   20: astore 5
/*      */     //   22: aload 5
/*      */     //   24: ifnonnull +37 -> 61
/*      */     //   27: aload 4
/*      */     //   29: ldc 61
/*      */     //   31: invokestatic 62	org/springframework/core/annotation/AnnotationUtils:findAnnotation	(Ljava/lang/Class;Ljava/lang/Class;)Ljava/lang/annotation/Annotation;
/*      */     //   34: ifnull +7 -> 41
/*      */     //   37: iconst_1
/*      */     //   38: goto +4 -> 42
/*      */     //   41: iconst_0
/*      */     //   42: invokestatic 63	java/lang/Boolean:valueOf	(Z)Ljava/lang/Boolean;
/*      */     //   45: astore 5
/*      */     //   47: aload_0
/*      */     //   48: getfield 32	org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerAdapter:sessionAnnotatedClassesCache	Ljava/util/Map;
/*      */     //   51: aload 4
/*      */     //   53: aload 5
/*      */     //   55: invokeinterface 64 3 0
/*      */     //   60: pop
/*      */     //   61: aload 5
/*      */     //   63: invokevirtual 65	java/lang/Boolean:booleanValue	()Z
/*      */     //   66: ifeq +17 -> 83
/*      */     //   69: aload_0
/*      */     //   70: aload_1
/*      */     //   71: aload_2
/*      */     //   72: aload_0
/*      */     //   73: getfield 22	org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerAdapter:cacheSecondsForSessionAttributeHandlers	I
/*      */     //   76: iconst_1
/*      */     //   77: invokevirtual 66	org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerAdapter:checkAndPrepare	(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;IZ)V
/*      */     //   80: goto +10 -> 90
/*      */     //   83: aload_0
/*      */     //   84: aload_1
/*      */     //   85: aload_2
/*      */     //   86: iconst_1
/*      */     //   87: invokevirtual 67	org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerAdapter:checkAndPrepare	(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;Z)V
/*      */     //   90: aload_0
/*      */     //   91: getfield 23	org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerAdapter:synchronizeOnSession	Z
/*      */     //   94: ifeq +49 -> 143
/*      */     //   97: aload_1
/*      */     //   98: iconst_0
/*      */     //   99: invokeinterface 68 2 0
/*      */     //   104: astore 6
/*      */     //   106: aload 6
/*      */     //   108: ifnull +35 -> 143
/*      */     //   111: aload 6
/*      */     //   113: invokestatic 69	org/springframework/web/util/WebUtils:getSessionMutex	(Ljavax/servlet/http/HttpSession;)Ljava/lang/Object;
/*      */     //   116: astore 7
/*      */     //   118: aload 7
/*      */     //   120: dup
/*      */     //   121: astore 8
/*      */     //   123: monitorenter
/*      */     //   124: aload_0
/*      */     //   125: aload_1
/*      */     //   126: aload_2
/*      */     //   127: aload_3
/*      */     //   128: invokevirtual 70	org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerAdapter:invokeHandlerMethod	(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;Ljava/lang/Object;)Lorg/springframework/web/servlet/ModelAndView;
/*      */     //   131: aload 8
/*      */     //   133: monitorexit
/*      */     //   134: areturn
/*      */     //   135: astore 9
/*      */     //   137: aload 8
/*      */     //   139: monitorexit
/*      */     //   140: aload 9
/*      */     //   142: athrow
/*      */     //   143: aload_0
/*      */     //   144: aload_1
/*      */     //   145: aload_2
/*      */     //   146: aload_3
/*      */     //   147: invokevirtual 70	org/springframework/web/servlet/mvc/annotation/AnnotationMethodHandlerAdapter:invokeHandlerMethod	(Ljavax/servlet/http/HttpServletRequest;Ljavax/servlet/http/HttpServletResponse;Ljava/lang/Object;)Lorg/springframework/web/servlet/ModelAndView;
/*      */     //   150: areturn
/*      */     // Line number table:
/*      */     //   Java source line #403	-> byte code offset #0
/*      */     //   Java source line #404	-> byte code offset #6
/*      */     //   Java source line #405	-> byte code offset #22
/*      */     //   Java source line #406	-> byte code offset #27
/*      */     //   Java source line #407	-> byte code offset #47
/*      */     //   Java source line #410	-> byte code offset #61
/*      */     //   Java source line #411	-> byte code offset #69
/*      */     //   Java source line #414	-> byte code offset #83
/*      */     //   Java source line #418	-> byte code offset #90
/*      */     //   Java source line #419	-> byte code offset #97
/*      */     //   Java source line #420	-> byte code offset #106
/*      */     //   Java source line #421	-> byte code offset #111
/*      */     //   Java source line #422	-> byte code offset #118
/*      */     //   Java source line #423	-> byte code offset #124
/*      */     //   Java source line #424	-> byte code offset #135
/*      */     //   Java source line #428	-> byte code offset #143
/*      */     // Local variable table:
/*      */     //   start	length	slot	name	signature
/*      */     //   0	151	0	this	AnnotationMethodHandlerAdapter
/*      */     //   0	151	1	request	HttpServletRequest
/*      */     //   0	151	2	response	HttpServletResponse
/*      */     //   0	151	3	handler	Object
/*      */     //   4	48	4	clazz	Class<?>
/*      */     //   20	42	5	annotatedWithSessionAttributes	Boolean
/*      */     //   104	8	6	session	HttpSession
/*      */     //   116	3	7	mutex	Object
/*      */     //   121	17	8	Ljava/lang/Object;	Object
/*      */     //   135	6	9	localObject1	Object
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   124	134	135	finally
/*      */     //   135	140	135	finally
/*      */   }
/*      */   
/*      */   protected ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, Object handler)
/*      */     throws Exception
/*      */   {
/*  434 */     ServletHandlerMethodResolver methodResolver = getMethodResolver(handler);
/*  435 */     Method handlerMethod = methodResolver.resolveHandlerMethod(request);
/*  436 */     ServletHandlerMethodInvoker methodInvoker = new ServletHandlerMethodInvoker(methodResolver, null);
/*  437 */     ServletWebRequest webRequest = new ServletWebRequest(request, response);
/*  438 */     ExtendedModelMap implicitModel = new BindingAwareModelMap();
/*      */     
/*  440 */     Object result = methodInvoker.invokeHandlerMethod(handlerMethod, handler, webRequest, implicitModel);
/*      */     
/*  442 */     ModelAndView mav = methodInvoker.getModelAndView(handlerMethod, handler.getClass(), result, implicitModel, webRequest);
/*  443 */     methodInvoker.updateModelAttributes(handler, mav != null ? mav.getModel() : null, implicitModel, webRequest);
/*  444 */     return mav;
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
/*      */   public long getLastModified(HttpServletRequest request, Object handler)
/*      */   {
/*  457 */     return -1L;
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   private ServletHandlerMethodResolver getMethodResolver(Object handler)
/*      */   {
/*  465 */     Class<?> handlerClass = ClassUtils.getUserClass(handler);
/*  466 */     ServletHandlerMethodResolver resolver = (ServletHandlerMethodResolver)this.methodResolverCache.get(handlerClass);
/*  467 */     if (resolver == null) {
/*  468 */       synchronized (this.methodResolverCache) {
/*  469 */         resolver = (ServletHandlerMethodResolver)this.methodResolverCache.get(handlerClass);
/*  470 */         if (resolver == null) {
/*  471 */           resolver = new ServletHandlerMethodResolver(handlerClass, null);
/*  472 */           this.methodResolverCache.put(handlerClass, resolver);
/*      */         }
/*      */       }
/*      */     }
/*  476 */     return resolver;
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
/*      */   protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object target, String objectName)
/*      */     throws Exception
/*      */   {
/*  494 */     return new ServletRequestDataBinder(target, objectName);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected HttpInputMessage createHttpInputMessage(HttpServletRequest servletRequest)
/*      */     throws Exception
/*      */   {
/*  506 */     return new ServletServerHttpRequest(servletRequest);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   protected HttpOutputMessage createHttpOutputMessage(HttpServletResponse servletResponse)
/*      */     throws Exception
/*      */   {
/*  518 */     return new ServletServerHttpResponse(servletResponse);
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private class ServletHandlerMethodResolver
/*      */     extends HandlerMethodResolver
/*      */   {
/*  528 */     private final Map<Method, AnnotationMethodHandlerAdapter.RequestMappingInfo> mappings = new HashMap();
/*      */     
/*      */     private ServletHandlerMethodResolver() {
/*  531 */       init(handlerType);
/*      */     }
/*      */     
/*      */     protected boolean isHandlerMethod(Method method)
/*      */     {
/*  536 */       if (this.mappings.containsKey(method)) {
/*  537 */         return true;
/*      */       }
/*  539 */       RequestMapping mapping = (RequestMapping)AnnotationUtils.findAnnotation(method, RequestMapping.class);
/*  540 */       if (mapping != null) {
/*  541 */         String[] patterns = mapping.value();
/*  542 */         RequestMethod[] methods = new RequestMethod[0];
/*  543 */         String[] params = new String[0];
/*  544 */         String[] headers = new String[0];
/*  545 */         if ((!hasTypeLevelMapping()) || (!Arrays.equals(mapping.method(), getTypeLevelMapping().method()))) {
/*  546 */           methods = mapping.method();
/*      */         }
/*  548 */         if ((!hasTypeLevelMapping()) || (!Arrays.equals(mapping.params(), getTypeLevelMapping().params()))) {
/*  549 */           params = mapping.params();
/*      */         }
/*  551 */         if ((!hasTypeLevelMapping()) || (!Arrays.equals(mapping.headers(), getTypeLevelMapping().headers()))) {
/*  552 */           headers = mapping.headers();
/*      */         }
/*  554 */         AnnotationMethodHandlerAdapter.RequestMappingInfo mappingInfo = new AnnotationMethodHandlerAdapter.RequestMappingInfo(patterns, methods, params, headers);
/*  555 */         this.mappings.put(method, mappingInfo);
/*  556 */         return true;
/*      */       }
/*  558 */       return false;
/*      */     }
/*      */     
/*      */     public Method resolveHandlerMethod(HttpServletRequest request) throws ServletException {
/*  562 */       String lookupPath = AnnotationMethodHandlerAdapter.this.urlPathHelper.getLookupPathForRequest(request);
/*  563 */       Comparator<String> pathComparator = AnnotationMethodHandlerAdapter.this.pathMatcher.getPatternComparator(lookupPath);
/*  564 */       Map<AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo, Method> targetHandlerMethods = new LinkedHashMap();
/*  565 */       Set<String> allowedMethods = new LinkedHashSet(7);
/*  566 */       String resolvedMethodName = null;
/*  567 */       for (Method handlerMethod : getHandlerMethods()) {
/*  568 */         AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo mappingInfo = new AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo((AnnotationMethodHandlerAdapter.RequestMappingInfo)this.mappings.get(handlerMethod));
/*  569 */         boolean match = false;
/*  570 */         String str1; String pattern; if (mappingInfo.hasPatterns()) {
/*  571 */           String[] arrayOfString1 = mappingInfo.getPatterns();int i = arrayOfString1.length; for (str1 = 0; str1 < i; str1++) { pattern = arrayOfString1[str1];
/*  572 */             if ((!hasTypeLevelMapping()) && (!pattern.startsWith("/"))) {
/*  573 */               pattern = "/" + pattern;
/*      */             }
/*  575 */             String combinedPattern = getCombinedPattern(pattern, lookupPath, request);
/*  576 */             if (combinedPattern != null) {
/*  577 */               if (mappingInfo.matches(request)) {
/*  578 */                 match = true;
/*  579 */                 mappingInfo.addMatchedPattern(combinedPattern);
/*      */               }
/*      */               else {
/*  582 */                 if (mappingInfo.matchesRequestMethod(request)) break;
/*  583 */                 allowedMethods.addAll(mappingInfo.methodNames()); break;
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*  589 */           mappingInfo.sortMatchedPatterns(pathComparator);
/*      */         }
/*  591 */         else if (useTypeLevelMapping(request)) {
/*  592 */           String[] typeLevelPatterns = getTypeLevelMapping().value();
/*  593 */           String[] arrayOfString2 = typeLevelPatterns;str1 = arrayOfString2.length; for (pattern = 0; pattern < str1; pattern++) { String typeLevelPattern = arrayOfString2[pattern];
/*  594 */             if (!typeLevelPattern.startsWith("/")) {
/*  595 */               typeLevelPattern = "/" + typeLevelPattern;
/*      */             }
/*  597 */             boolean useSuffixPattern = useSuffixPattern(request);
/*  598 */             if (getMatchingPattern(typeLevelPattern, lookupPath, useSuffixPattern) != null) {
/*  599 */               if (mappingInfo.matches(request)) {
/*  600 */                 match = true;
/*  601 */                 mappingInfo.addMatchedPattern(typeLevelPattern);
/*      */               }
/*      */               else {
/*  604 */                 if (mappingInfo.matchesRequestMethod(request)) break;
/*  605 */                 allowedMethods.addAll(mappingInfo.methodNames()); break;
/*      */               }
/*      */             }
/*      */           }
/*      */           
/*      */ 
/*  611 */           mappingInfo.sortMatchedPatterns(pathComparator);
/*      */         }
/*      */         else
/*      */         {
/*  615 */           match = mappingInfo.matches(request);
/*  616 */           if ((match) && (mappingInfo.getMethodCount() == 0) && (mappingInfo.getParamCount() == 0) && (resolvedMethodName != null) && 
/*  617 */             (!resolvedMethodName.equals(handlerMethod.getName()))) {
/*  618 */             match = false;
/*      */ 
/*      */           }
/*  621 */           else if (!mappingInfo.matchesRequestMethod(request)) {
/*  622 */             allowedMethods.addAll(mappingInfo.methodNames());
/*      */           }
/*      */         }
/*      */         
/*  626 */         if (match) {
/*  627 */           Method oldMappedMethod = (Method)targetHandlerMethods.put(mappingInfo, handlerMethod);
/*  628 */           if ((oldMappedMethod != null) && (oldMappedMethod != handlerMethod)) {
/*  629 */             if ((AnnotationMethodHandlerAdapter.this.methodNameResolver != null) && (!mappingInfo.hasPatterns()) && 
/*  630 */               (!oldMappedMethod.getName().equals(handlerMethod.getName()))) {
/*  631 */               if (resolvedMethodName == null) {
/*  632 */                 resolvedMethodName = AnnotationMethodHandlerAdapter.this.methodNameResolver.getHandlerMethodName(request);
/*      */               }
/*  634 */               if (!resolvedMethodName.equals(oldMappedMethod.getName())) {
/*  635 */                 oldMappedMethod = null;
/*      */               }
/*  637 */               if (!resolvedMethodName.equals(handlerMethod.getName())) {
/*  638 */                 if (oldMappedMethod != null) {
/*  639 */                   targetHandlerMethods.put(mappingInfo, oldMappedMethod);
/*  640 */                   oldMappedMethod = null;
/*      */                 }
/*      */                 else {
/*  643 */                   targetHandlerMethods.remove(mappingInfo);
/*      */                 }
/*      */               }
/*      */             }
/*      */             
/*  648 */             if (oldMappedMethod != null) {
/*  649 */               throw new IllegalStateException("Ambiguous handler methods mapped for HTTP path '" + lookupPath + "': {" + oldMappedMethod + ", " + handlerMethod + "}. If you intend to handle the same path in multiple methods, then factor them out into a dedicated handler class with that path mapped at the type level!");
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*      */ 
/*      */ 
/*      */ 
/*  658 */       if (!targetHandlerMethods.isEmpty()) {
/*  659 */         Object matches = new ArrayList(targetHandlerMethods.keySet());
/*  660 */         AnnotationMethodHandlerAdapter.RequestSpecificMappingInfoComparator requestMappingInfoComparator = new AnnotationMethodHandlerAdapter.RequestSpecificMappingInfoComparator(pathComparator, request);
/*      */         
/*  662 */         Collections.sort((List)matches, requestMappingInfoComparator);
/*  663 */         AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo bestMappingMatch = (AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo)((List)matches).get(0);
/*  664 */         String bestMatchedPath = bestMappingMatch.bestMatchedPattern();
/*  665 */         if (bestMatchedPath != null) {
/*  666 */           extractHandlerMethodUriTemplates(bestMatchedPath, lookupPath, request);
/*      */         }
/*  668 */         return (Method)targetHandlerMethods.get(bestMappingMatch);
/*      */       }
/*      */       
/*  671 */       if (!allowedMethods.isEmpty()) {
/*  672 */         throw new HttpRequestMethodNotSupportedException(request.getMethod(), StringUtils.toStringArray(allowedMethods));
/*      */       }
/*      */       
/*  675 */       throw new NoSuchRequestHandlingMethodException(lookupPath, request.getMethod(), request.getParameterMap());
/*      */     }
/*      */     
/*      */     private boolean useTypeLevelMapping(HttpServletRequest request)
/*      */     {
/*  680 */       if ((!hasTypeLevelMapping()) || (ObjectUtils.isEmpty(getTypeLevelMapping().value()))) {
/*  681 */         return false;
/*      */       }
/*  683 */       Object value = request.getAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING);
/*  684 */       return (value != null ? (Boolean)value : Boolean.TRUE).booleanValue();
/*      */     }
/*      */     
/*      */     private boolean useSuffixPattern(HttpServletRequest request) {
/*  688 */       Object value = request.getAttribute(DefaultAnnotationHandlerMapping.USE_DEFAULT_SUFFIX_PATTERN);
/*  689 */       return (value != null ? (Boolean)value : Boolean.TRUE).booleanValue();
/*      */     }
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
/*      */     private String getCombinedPattern(String methodLevelPattern, String lookupPath, HttpServletRequest request)
/*      */     {
/*  704 */       boolean useSuffixPattern = useSuffixPattern(request);
/*  705 */       if (useTypeLevelMapping(request)) {
/*  706 */         String[] typeLevelPatterns = getTypeLevelMapping().value();
/*  707 */         for (String typeLevelPattern : typeLevelPatterns) {
/*  708 */           if (!typeLevelPattern.startsWith("/")) {
/*  709 */             typeLevelPattern = "/" + typeLevelPattern;
/*      */           }
/*  711 */           String combinedPattern = AnnotationMethodHandlerAdapter.this.pathMatcher.combine(typeLevelPattern, methodLevelPattern);
/*  712 */           String matchingPattern = getMatchingPattern(combinedPattern, lookupPath, useSuffixPattern);
/*  713 */           if (matchingPattern != null) {
/*  714 */             return matchingPattern;
/*      */           }
/*      */         }
/*  717 */         return null;
/*      */       }
/*  719 */       String bestMatchingPattern = (String)request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
/*  720 */       if ((StringUtils.hasText(bestMatchingPattern)) && (bestMatchingPattern.endsWith("*"))) {
/*  721 */         String combinedPattern = AnnotationMethodHandlerAdapter.this.pathMatcher.combine(bestMatchingPattern, methodLevelPattern);
/*  722 */         String matchingPattern = getMatchingPattern(combinedPattern, lookupPath, useSuffixPattern);
/*  723 */         if ((matchingPattern != null) && (!matchingPattern.equals(bestMatchingPattern))) {
/*  724 */           return matchingPattern;
/*      */         }
/*      */       }
/*  727 */       return getMatchingPattern(methodLevelPattern, lookupPath, useSuffixPattern);
/*      */     }
/*      */     
/*      */     private String getMatchingPattern(String pattern, String lookupPath, boolean useSuffixPattern) {
/*  731 */       if (pattern.equals(lookupPath)) {
/*  732 */         return pattern;
/*      */       }
/*  734 */       boolean hasSuffix = pattern.indexOf('.') != -1;
/*  735 */       if ((useSuffixPattern) && (!hasSuffix)) {
/*  736 */         String patternWithSuffix = pattern + ".*";
/*  737 */         if (AnnotationMethodHandlerAdapter.this.pathMatcher.match(patternWithSuffix, lookupPath)) {
/*  738 */           return patternWithSuffix;
/*      */         }
/*      */       }
/*  741 */       if (AnnotationMethodHandlerAdapter.this.pathMatcher.match(pattern, lookupPath)) {
/*  742 */         return pattern;
/*      */       }
/*  744 */       boolean endsWithSlash = pattern.endsWith("/");
/*  745 */       if ((useSuffixPattern) && (!endsWithSlash)) {
/*  746 */         String patternWithSlash = pattern + "/";
/*  747 */         if (AnnotationMethodHandlerAdapter.this.pathMatcher.match(patternWithSlash, lookupPath)) {
/*  748 */           return patternWithSlash;
/*      */         }
/*      */       }
/*  751 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */     private void extractHandlerMethodUriTemplates(String mappedPattern, String lookupPath, HttpServletRequest request)
/*      */     {
/*  757 */       Map<String, String> variables = (Map)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
/*  758 */       int patternVariableCount = StringUtils.countOccurrencesOf(mappedPattern, "{");
/*  759 */       if (((variables == null) || (patternVariableCount != variables.size())) && (AnnotationMethodHandlerAdapter.this.pathMatcher.match(mappedPattern, lookupPath))) {
/*  760 */         variables = AnnotationMethodHandlerAdapter.this.pathMatcher.extractUriTemplateVariables(mappedPattern, lookupPath);
/*  761 */         Map<String, String> decodedVariables = AnnotationMethodHandlerAdapter.this.urlPathHelper.decodePathVariables(request, variables);
/*  762 */         request.setAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE, decodedVariables);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   private class ServletHandlerMethodInvoker
/*      */     extends HandlerMethodInvoker
/*      */   {
/*  774 */     private boolean responseArgumentUsed = false;
/*      */     
/*      */     private ServletHandlerMethodInvoker(HandlerMethodResolver resolver) {
/*  777 */       super(AnnotationMethodHandlerAdapter.this.webBindingInitializer, AnnotationMethodHandlerAdapter.this.sessionAttributeStore, AnnotationMethodHandlerAdapter.this.parameterNameDiscoverer, 
/*  778 */         AnnotationMethodHandlerAdapter.this.customArgumentResolvers, AnnotationMethodHandlerAdapter.this.messageConverters);
/*      */     }
/*      */     
/*      */     protected void raiseMissingParameterException(String paramName, Class<?> paramType) throws Exception
/*      */     {
/*  783 */       throw new MissingServletRequestParameterException(paramName, paramType.getSimpleName());
/*      */     }
/*      */     
/*      */     protected void raiseSessionRequiredException(String message) throws Exception
/*      */     {
/*  788 */       throw new HttpSessionRequiredException(message);
/*      */     }
/*      */     
/*      */ 
/*      */     protected WebDataBinder createBinder(NativeWebRequest webRequest, Object target, String objectName)
/*      */       throws Exception
/*      */     {
/*  795 */       return AnnotationMethodHandlerAdapter.this.createBinder(
/*  796 */         (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class), target, objectName);
/*      */     }
/*      */     
/*      */     protected void doBind(WebDataBinder binder, NativeWebRequest webRequest) throws Exception
/*      */     {
/*  801 */       ServletRequestDataBinder servletBinder = (ServletRequestDataBinder)binder;
/*  802 */       servletBinder.bind((ServletRequest)webRequest.getNativeRequest(ServletRequest.class));
/*      */     }
/*      */     
/*      */     protected HttpInputMessage createHttpInputMessage(NativeWebRequest webRequest) throws Exception
/*      */     {
/*  807 */       HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/*  808 */       return AnnotationMethodHandlerAdapter.this.createHttpInputMessage(servletRequest);
/*      */     }
/*      */     
/*      */     protected HttpOutputMessage createHttpOutputMessage(NativeWebRequest webRequest) throws Exception
/*      */     {
/*  813 */       HttpServletResponse servletResponse = (HttpServletResponse)webRequest.getNativeResponse();
/*  814 */       return AnnotationMethodHandlerAdapter.this.createHttpOutputMessage(servletResponse);
/*      */     }
/*      */     
/*      */     protected Object resolveDefaultValue(String value)
/*      */     {
/*  819 */       if (AnnotationMethodHandlerAdapter.this.beanFactory == null) {
/*  820 */         return value;
/*      */       }
/*  822 */       String placeholdersResolved = AnnotationMethodHandlerAdapter.this.beanFactory.resolveEmbeddedValue(value);
/*  823 */       BeanExpressionResolver exprResolver = AnnotationMethodHandlerAdapter.this.beanFactory.getBeanExpressionResolver();
/*  824 */       if (exprResolver == null) {
/*  825 */         return value;
/*      */       }
/*  827 */       return exprResolver.evaluate(placeholdersResolved, AnnotationMethodHandlerAdapter.this.expressionContext);
/*      */     }
/*      */     
/*      */ 
/*      */     protected Object resolveCookieValue(String cookieName, Class<?> paramType, NativeWebRequest webRequest)
/*      */       throws Exception
/*      */     {
/*  834 */       HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/*  835 */       Cookie cookieValue = WebUtils.getCookie(servletRequest, cookieName);
/*  836 */       if (Cookie.class.isAssignableFrom(paramType)) {
/*  837 */         return cookieValue;
/*      */       }
/*  839 */       if (cookieValue != null) {
/*  840 */         return AnnotationMethodHandlerAdapter.this.urlPathHelper.decodeRequestString(servletRequest, cookieValue.getValue());
/*      */       }
/*      */       
/*  843 */       return null;
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */     protected String resolvePathVariable(String pathVarName, Class<?> paramType, NativeWebRequest webRequest)
/*      */       throws Exception
/*      */     {
/*  852 */       HttpServletRequest servletRequest = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/*      */       
/*  854 */       Map<String, String> uriTemplateVariables = (Map)servletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
/*  855 */       if ((uriTemplateVariables == null) || (!uriTemplateVariables.containsKey(pathVarName))) {
/*  856 */         throw new IllegalStateException("Could not find @PathVariable [" + pathVarName + "] in @RequestMapping");
/*      */       }
/*      */       
/*  859 */       return (String)uriTemplateVariables.get(pathVarName);
/*      */     }
/*      */     
/*      */     protected Object resolveStandardArgument(Class<?> parameterType, NativeWebRequest webRequest) throws Exception
/*      */     {
/*  864 */       HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/*  865 */       HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/*      */       
/*  867 */       if ((ServletRequest.class.isAssignableFrom(parameterType)) || 
/*  868 */         (MultipartRequest.class.isAssignableFrom(parameterType))) {
/*  869 */         Object nativeRequest = webRequest.getNativeRequest(parameterType);
/*  870 */         if (nativeRequest == null)
/*      */         {
/*  872 */           throw new IllegalStateException("Current request is not of type [" + parameterType.getName() + "]: " + request);
/*      */         }
/*  874 */         return nativeRequest;
/*      */       }
/*  876 */       if (ServletResponse.class.isAssignableFrom(parameterType)) {
/*  877 */         this.responseArgumentUsed = true;
/*  878 */         Object nativeResponse = webRequest.getNativeResponse(parameterType);
/*  879 */         if (nativeResponse == null)
/*      */         {
/*  881 */           throw new IllegalStateException("Current response is not of type [" + parameterType.getName() + "]: " + response);
/*      */         }
/*  883 */         return nativeResponse;
/*      */       }
/*  885 */       if (HttpSession.class.isAssignableFrom(parameterType)) {
/*  886 */         return request.getSession();
/*      */       }
/*  888 */       if (Principal.class.isAssignableFrom(parameterType)) {
/*  889 */         return request.getUserPrincipal();
/*      */       }
/*  891 */       if (Locale.class == parameterType) {
/*  892 */         return RequestContextUtils.getLocale(request);
/*      */       }
/*  894 */       if (InputStream.class.isAssignableFrom(parameterType)) {
/*  895 */         return request.getInputStream();
/*      */       }
/*  897 */       if (Reader.class.isAssignableFrom(parameterType)) {
/*  898 */         return request.getReader();
/*      */       }
/*  900 */       if (OutputStream.class.isAssignableFrom(parameterType)) {
/*  901 */         this.responseArgumentUsed = true;
/*  902 */         return response.getOutputStream();
/*      */       }
/*  904 */       if (Writer.class.isAssignableFrom(parameterType)) {
/*  905 */         this.responseArgumentUsed = true;
/*  906 */         return response.getWriter();
/*      */       }
/*  908 */       return super.resolveStandardArgument(parameterType, webRequest);
/*      */     }
/*      */     
/*      */ 
/*      */     public ModelAndView getModelAndView(Method handlerMethod, Class<?> handlerType, Object returnValue, ExtendedModelMap implicitModel, ServletWebRequest webRequest)
/*      */       throws Exception
/*      */     {
/*  915 */       ResponseStatus responseStatus = (ResponseStatus)AnnotatedElementUtils.findMergedAnnotation(handlerMethod, ResponseStatus.class);
/*  916 */       HttpStatus statusCode; String reason; if (responseStatus != null) {
/*  917 */         statusCode = responseStatus.code();
/*  918 */         reason = responseStatus.reason();
/*  919 */         if (!StringUtils.hasText(reason)) {
/*  920 */           webRequest.getResponse().setStatus(statusCode.value());
/*      */         }
/*      */         else {
/*  923 */           webRequest.getResponse().sendError(statusCode.value(), reason);
/*      */         }
/*      */         
/*      */ 
/*  927 */         webRequest.getRequest().setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, statusCode);
/*      */         
/*  929 */         this.responseArgumentUsed = true;
/*      */       }
/*      */       
/*      */ 
/*  933 */       if (AnnotationMethodHandlerAdapter.this.customModelAndViewResolvers != null) {
/*  934 */         statusCode = AnnotationMethodHandlerAdapter.this.customModelAndViewResolvers;reason = statusCode.length; for (String str1 = 0; str1 < reason; str1++) { ModelAndViewResolver mavResolver = statusCode[str1];
/*  935 */           ModelAndView mav = mavResolver.resolveModelAndView(handlerMethod, handlerType, returnValue, implicitModel, webRequest);
/*      */           
/*  937 */           if (mav != ModelAndViewResolver.UNRESOLVED) {
/*  938 */             return mav;
/*      */           }
/*      */         }
/*      */       }
/*      */       
/*  943 */       if ((returnValue instanceof HttpEntity)) {
/*  944 */         handleHttpEntityResponse((HttpEntity)returnValue, webRequest);
/*  945 */         return null;
/*      */       }
/*  947 */       if (AnnotationUtils.findAnnotation(handlerMethod, ResponseBody.class) != null) {
/*  948 */         handleResponseBody(returnValue, webRequest);
/*  949 */         return null;
/*      */       }
/*  951 */       if ((returnValue instanceof ModelAndView)) {
/*  952 */         ModelAndView mav = (ModelAndView)returnValue;
/*  953 */         mav.getModelMap().mergeAttributes(implicitModel);
/*  954 */         return mav;
/*      */       }
/*  956 */       if ((returnValue instanceof Model)) {
/*  957 */         return new ModelAndView().addAllObjects(implicitModel).addAllObjects(((Model)returnValue).asMap());
/*      */       }
/*  959 */       if ((returnValue instanceof View)) {
/*  960 */         return new ModelAndView((View)returnValue).addAllObjects(implicitModel);
/*      */       }
/*  962 */       if (AnnotationUtils.findAnnotation(handlerMethod, ModelAttribute.class) != null) {
/*  963 */         addReturnValueAsModelAttribute(handlerMethod, handlerType, returnValue, implicitModel);
/*  964 */         return new ModelAndView().addAllObjects(implicitModel);
/*      */       }
/*  966 */       if ((returnValue instanceof Map)) {
/*  967 */         return new ModelAndView().addAllObjects(implicitModel).addAllObjects((Map)returnValue);
/*      */       }
/*  969 */       if ((returnValue instanceof String)) {
/*  970 */         return new ModelAndView((String)returnValue).addAllObjects(implicitModel);
/*      */       }
/*  972 */       if (returnValue == null)
/*      */       {
/*  974 */         if ((this.responseArgumentUsed) || (webRequest.isNotModified())) {
/*  975 */           return null;
/*      */         }
/*      */         
/*      */ 
/*  979 */         return new ModelAndView().addAllObjects(implicitModel);
/*      */       }
/*      */       
/*  982 */       if (!BeanUtils.isSimpleProperty(returnValue.getClass()))
/*      */       {
/*  984 */         addReturnValueAsModelAttribute(handlerMethod, handlerType, returnValue, implicitModel);
/*  985 */         return new ModelAndView().addAllObjects(implicitModel);
/*      */       }
/*      */       
/*  988 */       throw new IllegalArgumentException("Invalid handler method return value: " + returnValue);
/*      */     }
/*      */     
/*      */     private void handleResponseBody(Object returnValue, ServletWebRequest webRequest) throws Exception
/*      */     {
/*  993 */       if (returnValue == null) {
/*  994 */         return;
/*      */       }
/*  996 */       HttpInputMessage inputMessage = createHttpInputMessage(webRequest);
/*  997 */       HttpOutputMessage outputMessage = createHttpOutputMessage(webRequest);
/*  998 */       writeWithMessageConverters(returnValue, inputMessage, outputMessage);
/*      */     }
/*      */     
/*      */     private void handleHttpEntityResponse(HttpEntity<?> responseEntity, ServletWebRequest webRequest) throws Exception {
/* 1002 */       if (responseEntity == null) {
/* 1003 */         return;
/*      */       }
/* 1005 */       HttpInputMessage inputMessage = createHttpInputMessage(webRequest);
/* 1006 */       HttpOutputMessage outputMessage = createHttpOutputMessage(webRequest);
/* 1007 */       if (((responseEntity instanceof ResponseEntity)) && ((outputMessage instanceof ServerHttpResponse))) {
/* 1008 */         ((ServerHttpResponse)outputMessage).setStatusCode(((ResponseEntity)responseEntity).getStatusCode());
/*      */       }
/* 1010 */       HttpHeaders entityHeaders = responseEntity.getHeaders();
/* 1011 */       if (!entityHeaders.isEmpty()) {
/* 1012 */         outputMessage.getHeaders().putAll(entityHeaders);
/*      */       }
/* 1014 */       Object body = responseEntity.getBody();
/* 1015 */       if (body != null) {
/* 1016 */         writeWithMessageConverters(body, inputMessage, outputMessage);
/*      */       }
/*      */       else
/*      */       {
/* 1020 */         outputMessage.getBody();
/*      */       }
/*      */     }
/*      */     
/*      */ 
/*      */ 
/*      */     private void writeWithMessageConverters(Object returnValue, HttpInputMessage inputMessage, HttpOutputMessage outputMessage)
/*      */       throws IOException, HttpMediaTypeNotAcceptableException
/*      */     {
/* 1029 */       List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
/* 1030 */       if (acceptedMediaTypes.isEmpty()) {
/* 1031 */         acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
/*      */       }
/* 1033 */       MediaType.sortByQualityValue(acceptedMediaTypes);
/* 1034 */       Class<?> returnValueType = returnValue.getClass();
/* 1035 */       List<MediaType> allSupportedMediaTypes = new ArrayList();
/* 1036 */       if (AnnotationMethodHandlerAdapter.this.getMessageConverters() != null) {
/* 1037 */         for (Object localObject = acceptedMediaTypes.iterator(); ((Iterator)localObject).hasNext();) { acceptedMediaType = (MediaType)((Iterator)localObject).next();
/* 1038 */           for (HttpMessageConverter messageConverter : AnnotationMethodHandlerAdapter.this.getMessageConverters()) {
/* 1039 */             if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
/* 1040 */               messageConverter.write(returnValue, acceptedMediaType, outputMessage);
/* 1041 */               if (AnnotationMethodHandlerAdapter.this.logger.isDebugEnabled()) {
/* 1042 */                 MediaType contentType = outputMessage.getHeaders().getContentType();
/* 1043 */                 if (contentType == null) {
/* 1044 */                   contentType = acceptedMediaType;
/*      */                 }
/* 1046 */                 AnnotationMethodHandlerAdapter.this.logger.debug("Written [" + returnValue + "] as \"" + contentType + "\" using [" + messageConverter + "]");
/*      */               }
/*      */               
/* 1049 */               this.responseArgumentUsed = true;
/* 1050 */               return;
/*      */             }
/*      */           }
/*      */         }
/* 1054 */         localObject = AnnotationMethodHandlerAdapter.this.messageConverters;MediaType acceptedMediaType = localObject.length; for (MediaType localMediaType1 = 0; localMediaType1 < acceptedMediaType; localMediaType1++) { HttpMessageConverter messageConverter = localObject[localMediaType1];
/* 1055 */           allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
/*      */         }
/*      */       }
/* 1058 */       throw new HttpMediaTypeNotAcceptableException(allSupportedMediaTypes);
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */   static class RequestMappingInfo
/*      */   {
/*      */     private final String[] patterns;
/*      */     
/*      */     private final RequestMethod[] methods;
/*      */     
/*      */     private final String[] params;
/*      */     
/*      */     private final String[] headers;
/*      */     
/*      */ 
/*      */     RequestMappingInfo(String[] patterns, RequestMethod[] methods, String[] params, String[] headers)
/*      */     {
/* 1077 */       this.patterns = (patterns != null ? patterns : new String[0]);
/* 1078 */       this.methods = (methods != null ? methods : new RequestMethod[0]);
/* 1079 */       this.params = (params != null ? params : new String[0]);
/* 1080 */       this.headers = (headers != null ? headers : new String[0]);
/*      */     }
/*      */     
/*      */     public boolean hasPatterns() {
/* 1084 */       return this.patterns.length > 0;
/*      */     }
/*      */     
/*      */     public String[] getPatterns() {
/* 1088 */       return this.patterns;
/*      */     }
/*      */     
/*      */     public int getMethodCount() {
/* 1092 */       return this.methods.length;
/*      */     }
/*      */     
/*      */     public int getParamCount() {
/* 1096 */       return this.params.length;
/*      */     }
/*      */     
/*      */     public int getHeaderCount() {
/* 1100 */       return this.headers.length;
/*      */     }
/*      */     
/*      */     public boolean matches(HttpServletRequest request) {
/* 1104 */       return (matchesRequestMethod(request)) && (matchesParameters(request)) && (matchesHeaders(request));
/*      */     }
/*      */     
/*      */     public boolean matchesHeaders(HttpServletRequest request) {
/* 1108 */       return ServletAnnotationMappingUtils.checkHeaders(this.headers, request);
/*      */     }
/*      */     
/*      */     public boolean matchesParameters(HttpServletRequest request) {
/* 1112 */       return ServletAnnotationMappingUtils.checkParameters(this.params, request);
/*      */     }
/*      */     
/*      */     public boolean matchesRequestMethod(HttpServletRequest request) {
/* 1116 */       return ServletAnnotationMappingUtils.checkRequestMethod(this.methods, request);
/*      */     }
/*      */     
/*      */     public Set<String> methodNames() {
/* 1120 */       Set<String> methodNames = new LinkedHashSet(this.methods.length);
/* 1121 */       for (RequestMethod method : this.methods) {
/* 1122 */         methodNames.add(method.name());
/*      */       }
/* 1124 */       return methodNames;
/*      */     }
/*      */     
/*      */     public boolean equals(Object obj)
/*      */     {
/* 1129 */       RequestMappingInfo other = (RequestMappingInfo)obj;
/* 1130 */       return (Arrays.equals(this.patterns, other.patterns)) && (Arrays.equals(this.methods, other.methods)) && 
/* 1131 */         (Arrays.equals(this.params, other.params)) && (Arrays.equals(this.headers, other.headers));
/*      */     }
/*      */     
/*      */     public int hashCode()
/*      */     {
/* 1136 */       return 
/* 1137 */         Arrays.hashCode(this.patterns) * 23 + Arrays.hashCode(this.methods) * 29 + Arrays.hashCode(this.params) * 31 + Arrays.hashCode(this.headers);
/*      */     }
/*      */     
/*      */     public String toString()
/*      */     {
/* 1142 */       StringBuilder builder = new StringBuilder();
/* 1143 */       builder.append(Arrays.asList(this.patterns));
/* 1144 */       if (this.methods.length > 0) {
/* 1145 */         builder.append(',');
/* 1146 */         builder.append(Arrays.asList(this.methods));
/*      */       }
/* 1148 */       if (this.headers.length > 0) {
/* 1149 */         builder.append(',');
/* 1150 */         builder.append(Arrays.asList(this.headers));
/*      */       }
/* 1152 */       if (this.params.length > 0) {
/* 1153 */         builder.append(',');
/* 1154 */         builder.append(Arrays.asList(this.params));
/*      */       }
/* 1156 */       return builder.toString();
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */   static class RequestSpecificMappingInfo
/*      */     extends AnnotationMethodHandlerAdapter.RequestMappingInfo
/*      */   {
/* 1166 */     private final List<String> matchedPatterns = new ArrayList();
/*      */     
/*      */     RequestSpecificMappingInfo(String[] patterns, RequestMethod[] methods, String[] params, String[] headers) {
/* 1169 */       super(methods, params, headers);
/*      */     }
/*      */     
/*      */     RequestSpecificMappingInfo(AnnotationMethodHandlerAdapter.RequestMappingInfo other) {
/* 1173 */       super(AnnotationMethodHandlerAdapter.RequestMappingInfo.access$1600(other), AnnotationMethodHandlerAdapter.RequestMappingInfo.access$1700(other), AnnotationMethodHandlerAdapter.RequestMappingInfo.access$1800(other));
/*      */     }
/*      */     
/*      */     public void addMatchedPattern(String matchedPattern) {
/* 1177 */       this.matchedPatterns.add(matchedPattern);
/*      */     }
/*      */     
/*      */     public void sortMatchedPatterns(Comparator<String> pathComparator) {
/* 1181 */       Collections.sort(this.matchedPatterns, pathComparator);
/*      */     }
/*      */     
/*      */     public String bestMatchedPattern() {
/* 1185 */       return !this.matchedPatterns.isEmpty() ? (String)this.matchedPatterns.get(0) : null;
/*      */     }
/*      */   }
/*      */   
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   static class RequestSpecificMappingInfoComparator
/*      */     implements Comparator<AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo>
/*      */   {
/*      */     private final Comparator<String> pathComparator;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     private final ServerHttpRequest request;
/*      */     
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     RequestSpecificMappingInfoComparator(Comparator<String> pathComparator, HttpServletRequest request)
/*      */     {
/* 1211 */       this.pathComparator = pathComparator;
/* 1212 */       this.request = new ServletServerHttpRequest(request);
/*      */     }
/*      */     
/*      */     public int compare(AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo info1, AnnotationMethodHandlerAdapter.RequestSpecificMappingInfo info2)
/*      */     {
/* 1217 */       int pathComparison = this.pathComparator.compare(info1.bestMatchedPattern(), info2.bestMatchedPattern());
/* 1218 */       if (pathComparison != 0) {
/* 1219 */         return pathComparison;
/*      */       }
/* 1221 */       int info1ParamCount = info1.getParamCount();
/* 1222 */       int info2ParamCount = info2.getParamCount();
/* 1223 */       if (info1ParamCount != info2ParamCount) {
/* 1224 */         return info2ParamCount - info1ParamCount;
/*      */       }
/* 1226 */       int info1HeaderCount = info1.getHeaderCount();
/* 1227 */       int info2HeaderCount = info2.getHeaderCount();
/* 1228 */       if (info1HeaderCount != info2HeaderCount) {
/* 1229 */         return info2HeaderCount - info1HeaderCount;
/*      */       }
/* 1231 */       int acceptComparison = compareAcceptHeaders(info1, info2);
/* 1232 */       if (acceptComparison != 0) {
/* 1233 */         return acceptComparison;
/*      */       }
/* 1235 */       int info1MethodCount = info1.getMethodCount();
/* 1236 */       int info2MethodCount = info2.getMethodCount();
/* 1237 */       if ((info1MethodCount == 0) && (info2MethodCount > 0)) {
/* 1238 */         return 1;
/*      */       }
/* 1240 */       if ((info2MethodCount == 0) && (info1MethodCount > 0)) {
/* 1241 */         return -1;
/*      */       }
/* 1243 */       if (((info1MethodCount == 1 ? 1 : 0) & (info2MethodCount > 1 ? 1 : 0)) != 0) {
/* 1244 */         return -1;
/*      */       }
/* 1246 */       if (((info2MethodCount == 1 ? 1 : 0) & (info1MethodCount > 1 ? 1 : 0)) != 0) {
/* 1247 */         return 1;
/*      */       }
/* 1249 */       return 0;
/*      */     }
/*      */     
/*      */     private int compareAcceptHeaders(AnnotationMethodHandlerAdapter.RequestMappingInfo info1, AnnotationMethodHandlerAdapter.RequestMappingInfo info2) {
/* 1253 */       List<MediaType> requestAccepts = this.request.getHeaders().getAccept();
/* 1254 */       MediaType.sortByQualityValue(requestAccepts);
/*      */       
/* 1256 */       List<MediaType> info1Accepts = getAcceptHeaderValue(info1);
/* 1257 */       List<MediaType> info2Accepts = getAcceptHeaderValue(info2);
/*      */       
/* 1259 */       for (MediaType requestAccept : requestAccepts) {
/* 1260 */         int pos1 = indexOfIncluded(info1Accepts, requestAccept);
/* 1261 */         int pos2 = indexOfIncluded(info2Accepts, requestAccept);
/* 1262 */         if (pos1 != pos2) {
/* 1263 */           return pos2 - pos1;
/*      */         }
/*      */       }
/* 1266 */       return 0;
/*      */     }
/*      */     
/*      */     private int indexOfIncluded(List<MediaType> infoAccepts, MediaType requestAccept) {
/* 1270 */       for (int i = 0; i < infoAccepts.size(); i++) {
/* 1271 */         MediaType info1Accept = (MediaType)infoAccepts.get(i);
/* 1272 */         if (requestAccept.includes(info1Accept)) {
/* 1273 */           return i;
/*      */         }
/*      */       }
/* 1276 */       return -1;
/*      */     }
/*      */     
/*      */     private List<MediaType> getAcceptHeaderValue(AnnotationMethodHandlerAdapter.RequestMappingInfo info) {
/* 1280 */       for (String header : AnnotationMethodHandlerAdapter.RequestMappingInfo.access$1800(info)) {
/* 1281 */         int separator = header.indexOf('=');
/* 1282 */         if (separator != -1) {
/* 1283 */           String key = header.substring(0, separator);
/* 1284 */           String value = header.substring(separator + 1);
/* 1285 */           if ("Accept".equalsIgnoreCase(key)) {
/* 1286 */             return MediaType.parseMediaTypes(value);
/*      */           }
/*      */         }
/*      */       }
/* 1290 */       return Collections.emptyList();
/*      */     }
/*      */   }
/*      */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\annotation\AnnotationMethodHandlerAdapter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */