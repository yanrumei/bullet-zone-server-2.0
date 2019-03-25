/*     */ package org.springframework.web.servlet.mvc.method.annotation;
/*     */ 
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.aopalliance.intercept.MethodInvocation;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.aop.framework.ProxyFactory;
/*     */ import org.springframework.aop.target.EmptyTargetSource;
/*     */ import org.springframework.beans.factory.NoSuchBeanDefinitionException;
/*     */ import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
/*     */ import org.springframework.cglib.core.SpringNamingPolicy;
/*     */ import org.springframework.cglib.proxy.Callback;
/*     */ import org.springframework.cglib.proxy.Enhancer;
/*     */ import org.springframework.cglib.proxy.Factory;
/*     */ import org.springframework.cglib.proxy.MethodProxy;
/*     */ import org.springframework.core.DefaultParameterNameDiscoverer;
/*     */ import org.springframework.core.MethodIntrospector;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.SynthesizingMethodParameter;
/*     */ import org.springframework.objenesis.ObjenesisException;
/*     */ import org.springframework.objenesis.SpringObjenesis;
/*     */ import org.springframework.util.AntPathMatcher;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.PathMatcher;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.ReflectionUtils.MethodFilter;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.context.WebApplicationContext;
/*     */ import org.springframework.web.context.request.RequestAttributes;
/*     */ import org.springframework.web.context.request.RequestContextHolder;
/*     */ import org.springframework.web.context.request.ServletRequestAttributes;
/*     */ import org.springframework.web.method.HandlerMethod;
/*     */ import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
/*     */ import org.springframework.web.method.support.CompositeUriComponentsContributor;
/*     */ import org.springframework.web.method.support.UriComponentsContributor;
/*     */ import org.springframework.web.servlet.DispatcherServlet;
/*     */ import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMapping;
/*     */ import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
/*     */ import org.springframework.web.util.UriComponents;
/*     */ import org.springframework.web.util.UriComponents.UriTemplateVariables;
/*     */ import org.springframework.web.util.UriComponentsBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MvcUriComponentsBuilder
/*     */ {
/*     */   public static final String MVC_URI_COMPONENTS_CONTRIBUTOR_BEAN_NAME = "mvcUriComponentsContributor";
/* 107 */   private static final Log logger = LogFactory.getLog(MvcUriComponentsBuilder.class);
/*     */   
/* 109 */   private static final SpringObjenesis objenesis = new SpringObjenesis();
/*     */   
/* 111 */   private static final PathMatcher pathMatcher = new AntPathMatcher();
/*     */   
/* 113 */   private static final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 118 */   private static final CompositeUriComponentsContributor defaultUriComponentsContributor = new CompositeUriComponentsContributor(new UriComponentsContributor[] { new PathVariableMethodArgumentResolver(), new RequestParamMethodArgumentResolver(false) });
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private final UriComponentsBuilder baseUrl;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected MvcUriComponentsBuilder(UriComponentsBuilder baseUrl)
/*     */   {
/* 134 */     Assert.notNull(baseUrl, "'baseUrl' is required");
/* 135 */     this.baseUrl = baseUrl;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MvcUriComponentsBuilder relativeTo(UriComponentsBuilder baseUrl)
/*     */   {
/* 145 */     return new MvcUriComponentsBuilder(baseUrl);
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
/*     */   public static UriComponentsBuilder fromController(Class<?> controllerType)
/*     */   {
/* 159 */     return fromController(null, controllerType);
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
/*     */   public static UriComponentsBuilder fromController(UriComponentsBuilder builder, Class<?> controllerType)
/*     */   {
/* 177 */     builder = getBaseUrlToUse(builder);
/* 178 */     String mapping = getTypeRequestMapping(controllerType);
/* 179 */     return builder.path(mapping);
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
/*     */   public static UriComponentsBuilder fromMethodName(Class<?> controllerType, String methodName, Object... args)
/*     */   {
/* 198 */     Method method = getMethod(controllerType, methodName, args);
/* 199 */     return fromMethodInternal(null, controllerType, method, args);
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
/*     */   public static UriComponentsBuilder fromMethodName(UriComponentsBuilder builder, Class<?> controllerType, String methodName, Object... args)
/*     */   {
/* 221 */     Method method = getMethod(controllerType, methodName, args);
/* 222 */     return fromMethodInternal(builder, controllerType, method, args);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static UriComponentsBuilder fromMethodCall(Object info)
/*     */   {
/* 265 */     Assert.isInstanceOf(MethodInvocationInfo.class, info, "MethodInvocationInfo required");
/* 266 */     MethodInvocationInfo invocationInfo = (MethodInvocationInfo)info;
/* 267 */     Class<?> controllerType = invocationInfo.getControllerType();
/* 268 */     Method method = invocationInfo.getControllerMethod();
/* 269 */     Object[] arguments = invocationInfo.getArgumentValues();
/* 270 */     return fromMethodInternal(null, controllerType, method, arguments);
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
/*     */   public static UriComponentsBuilder fromMethodCall(UriComponentsBuilder builder, Object info)
/*     */   {
/* 287 */     Assert.isInstanceOf(MethodInvocationInfo.class, info, "MethodInvocationInfo required");
/* 288 */     MethodInvocationInfo invocationInfo = (MethodInvocationInfo)info;
/* 289 */     Class<?> controllerType = invocationInfo.getControllerType();
/* 290 */     Method method = invocationInfo.getControllerMethod();
/* 291 */     Object[] arguments = invocationInfo.getArgumentValues();
/* 292 */     return fromMethodInternal(builder, controllerType, method, arguments);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static MethodArgumentBuilder fromMappingName(String mappingName)
/*     */   {
/* 341 */     return fromMappingName(null, mappingName);
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
/*     */   public static MethodArgumentBuilder fromMappingName(UriComponentsBuilder builder, String name)
/*     */   {
/* 360 */     RequestMappingInfoHandlerMapping handlerMapping = getRequestMappingInfoHandlerMapping();
/* 361 */     List<HandlerMethod> handlerMethods = handlerMapping.getHandlerMethodsForMappingName(name);
/* 362 */     if (handlerMethods == null) {
/* 363 */       throw new IllegalArgumentException("Mapping mappingName not found: " + name);
/*     */     }
/* 365 */     if (handlerMethods.size() != 1) {
/* 366 */       throw new IllegalArgumentException("No unique match for mapping mappingName " + name + ": " + handlerMethods);
/*     */     }
/*     */     
/* 369 */     HandlerMethod handlerMethod = (HandlerMethod)handlerMethods.get(0);
/* 370 */     Class<?> controllerType = handlerMethod.getBeanType();
/* 371 */     Method method = handlerMethod.getMethod();
/* 372 */     return new MethodArgumentBuilder(builder, controllerType, method);
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
/*     */   public static UriComponentsBuilder fromMethod(Class<?> controllerType, Method method, Object... args)
/*     */   {
/* 392 */     return fromMethodInternal(null, controllerType, method, args);
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
/*     */   public static UriComponentsBuilder fromMethod(UriComponentsBuilder baseUrl, Class<?> controllerType, Method method, Object... args)
/*     */   {
/* 414 */     return fromMethodInternal(baseUrl, controllerType != null ? controllerType : method
/* 415 */       .getDeclaringClass(), method, args);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   @Deprecated
/*     */   public static UriComponentsBuilder fromMethod(Method method, Object... args)
/*     */   {
/* 426 */     return fromMethodInternal(null, method.getDeclaringClass(), method, args);
/*     */   }
/*     */   
/*     */ 
/*     */   private static UriComponentsBuilder fromMethodInternal(UriComponentsBuilder baseUrl, Class<?> controllerType, Method method, Object... args)
/*     */   {
/* 432 */     baseUrl = getBaseUrlToUse(baseUrl);
/* 433 */     String typePath = getTypeRequestMapping(controllerType);
/* 434 */     String methodPath = getMethodRequestMapping(method);
/* 435 */     String path = pathMatcher.combine(typePath, methodPath);
/* 436 */     baseUrl.path(path);
/* 437 */     UriComponents uriComponents = applyContributors(baseUrl, method, args);
/* 438 */     return UriComponentsBuilder.newInstance().uriComponents(uriComponents);
/*     */   }
/*     */   
/*     */   private static UriComponentsBuilder getBaseUrlToUse(UriComponentsBuilder baseUrl) {
/* 442 */     if (baseUrl != null) {
/* 443 */       return baseUrl.cloneBuilder();
/*     */     }
/*     */     
/* 446 */     return ServletUriComponentsBuilder.fromCurrentServletMapping();
/*     */   }
/*     */   
/*     */   private static String getTypeRequestMapping(Class<?> controllerType)
/*     */   {
/* 451 */     Assert.notNull(controllerType, "'controllerType' must not be null");
/* 452 */     RequestMapping requestMapping = (RequestMapping)AnnotatedElementUtils.findMergedAnnotation(controllerType, RequestMapping.class);
/* 453 */     if (requestMapping == null) {
/* 454 */       return "/";
/*     */     }
/* 456 */     String[] paths = requestMapping.path();
/* 457 */     if ((ObjectUtils.isEmpty(paths)) || (StringUtils.isEmpty(paths[0]))) {
/* 458 */       return "/";
/*     */     }
/* 460 */     if ((paths.length > 1) && (logger.isWarnEnabled())) {
/* 461 */       logger.warn("Multiple paths on controller " + controllerType.getName() + ", using first one");
/*     */     }
/* 463 */     return paths[0];
/*     */   }
/*     */   
/*     */   private static String getMethodRequestMapping(Method method) {
/* 467 */     Assert.notNull(method, "'method' must not be null");
/* 468 */     RequestMapping requestMapping = (RequestMapping)AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
/* 469 */     if (requestMapping == null) {
/* 470 */       throw new IllegalArgumentException("No @RequestMapping on: " + method.toGenericString());
/*     */     }
/* 472 */     String[] paths = requestMapping.path();
/* 473 */     if ((ObjectUtils.isEmpty(paths)) || (StringUtils.isEmpty(paths[0]))) {
/* 474 */       return "/";
/*     */     }
/* 476 */     if ((paths.length > 1) && (logger.isWarnEnabled())) {
/* 477 */       logger.warn("Multiple paths on method " + method.toGenericString() + ", using first one");
/*     */     }
/* 479 */     return paths[0];
/*     */   }
/*     */   
/*     */   private static Method getMethod(Class<?> controllerType, String methodName, final Object... args) {
/* 483 */     ReflectionUtils.MethodFilter selector = new ReflectionUtils.MethodFilter()
/*     */     {
/*     */       public boolean matches(Method method) {
/* 486 */         String name = method.getName();
/* 487 */         int argLength = method.getParameterTypes().length;
/* 488 */         return (name.equals(this.val$methodName)) && (argLength == args.length);
/*     */       }
/* 490 */     };
/* 491 */     Set<Method> methods = MethodIntrospector.selectMethods(controllerType, selector);
/* 492 */     if (methods.size() == 1) {
/* 493 */       return (Method)methods.iterator().next();
/*     */     }
/* 495 */     if (methods.size() > 1) {
/* 496 */       throw new IllegalArgumentException(String.format("Found two methods named '%s' accepting arguments %s in controller %s: [%s]", new Object[] { methodName, 
/*     */       
/* 498 */         Arrays.asList(args), controllerType.getName(), methods }));
/*     */     }
/*     */     
/*     */ 
/* 502 */     throw new IllegalArgumentException("No method named '" + methodName + "' with " + args.length + " arguments found in controller " + controllerType.getName());
/*     */   }
/*     */   
/*     */   private static UriComponents applyContributors(UriComponentsBuilder builder, Method method, Object... args)
/*     */   {
/* 507 */     CompositeUriComponentsContributor contributor = getConfiguredUriComponentsContributor();
/* 508 */     if (contributor == null) {
/* 509 */       logger.debug("Using default CompositeUriComponentsContributor");
/* 510 */       contributor = defaultUriComponentsContributor;
/*     */     }
/*     */     
/* 513 */     int paramCount = method.getParameterTypes().length;
/* 514 */     int argCount = args.length;
/* 515 */     if (paramCount != argCount) {
/* 516 */       throw new IllegalArgumentException("Number of method parameters " + paramCount + " does not match number of argument values " + argCount);
/*     */     }
/*     */     
/*     */ 
/* 520 */     Map<String, Object> uriVars = new HashMap();
/* 521 */     for (int i = 0; i < paramCount; i++) {
/* 522 */       MethodParameter param = new SynthesizingMethodParameter(method, i);
/* 523 */       param.initParameterNameDiscovery(parameterNameDiscoverer);
/* 524 */       contributor.contributeMethodArgument(param, args[i], builder, uriVars);
/*     */     }
/*     */     
/*     */ 
/* 528 */     builder.build().expand(new UriComponents.UriTemplateVariables()
/*     */     {
/*     */       public Object getValue(String name) {
/* 531 */         return this.val$uriVars.containsKey(name) ? this.val$uriVars.get(name) : UriComponents.UriTemplateVariables.SKIP_VALUE;
/*     */       }
/*     */     });
/*     */   }
/*     */   
/*     */   private static CompositeUriComponentsContributor getConfiguredUriComponentsContributor() {
/* 537 */     WebApplicationContext wac = getWebApplicationContext();
/* 538 */     if (wac == null) {
/* 539 */       return null;
/*     */     }
/*     */     try {
/* 542 */       return (CompositeUriComponentsContributor)wac.getBean("mvcUriComponentsContributor", CompositeUriComponentsContributor.class);
/*     */     }
/*     */     catch (NoSuchBeanDefinitionException ex) {
/* 545 */       if (logger.isDebugEnabled()) {
/* 546 */         logger.debug("No CompositeUriComponentsContributor bean with name 'mvcUriComponentsContributor'");
/*     */       }
/*     */     }
/* 549 */     return null;
/*     */   }
/*     */   
/*     */   private static RequestMappingInfoHandlerMapping getRequestMappingInfoHandlerMapping()
/*     */   {
/* 554 */     WebApplicationContext wac = getWebApplicationContext();
/* 555 */     Assert.notNull(wac, "Cannot lookup handler method mappings without WebApplicationContext");
/*     */     try {
/* 557 */       return (RequestMappingInfoHandlerMapping)wac.getBean(RequestMappingInfoHandlerMapping.class);
/*     */     }
/*     */     catch (NoUniqueBeanDefinitionException ex) {
/* 560 */       throw new IllegalStateException("More than one RequestMappingInfoHandlerMapping beans found", ex);
/*     */     }
/*     */     catch (NoSuchBeanDefinitionException ex) {
/* 563 */       throw new IllegalStateException("No RequestMappingInfoHandlerMapping bean", ex);
/*     */     }
/*     */   }
/*     */   
/*     */   private static WebApplicationContext getWebApplicationContext() {
/* 568 */     RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
/* 569 */     if (requestAttributes == null) {
/* 570 */       logger.debug("No request bound to the current thread: not in a DispatcherServlet request?");
/* 571 */       return null;
/*     */     }
/*     */     
/* 574 */     HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
/*     */     
/* 576 */     WebApplicationContext wac = (WebApplicationContext)request.getAttribute(DispatcherServlet.WEB_APPLICATION_CONTEXT_ATTRIBUTE);
/* 577 */     if (wac == null) {
/* 578 */       logger.debug("No WebApplicationContext found: not in a DispatcherServlet request?");
/* 579 */       return null;
/*     */     }
/* 581 */     return wac;
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
/*     */   public static <T> T on(Class<T> controllerType)
/*     */   {
/* 600 */     return (T)controller(controllerType);
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
/*     */ 
/*     */ 
/*     */   public static <T> T controller(Class<T> controllerType)
/*     */   {
/* 624 */     Assert.notNull(controllerType, "'controllerType' must not be null");
/* 625 */     return (T)initProxy(controllerType, new ControllerMethodInvocationInterceptor(controllerType));
/*     */   }
/*     */   
/*     */   private static <T> T initProxy(Class<?> type, ControllerMethodInvocationInterceptor interceptor)
/*     */   {
/* 630 */     if (type.isInterface()) {
/* 631 */       ProxyFactory factory = new ProxyFactory(EmptyTargetSource.INSTANCE);
/* 632 */       factory.addInterface(type);
/* 633 */       factory.addInterface(MethodInvocationInfo.class);
/* 634 */       factory.addAdvice(interceptor);
/* 635 */       return (T)factory.getProxy();
/*     */     }
/*     */     
/*     */ 
/* 639 */     Enhancer enhancer = new Enhancer();
/* 640 */     enhancer.setSuperclass(type);
/* 641 */     enhancer.setInterfaces(new Class[] { MethodInvocationInfo.class });
/* 642 */     enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
/* 643 */     enhancer.setCallbackType(org.springframework.cglib.proxy.MethodInterceptor.class);
/*     */     
/* 645 */     Class<?> proxyClass = enhancer.createClass();
/* 646 */     Object proxy = null;
/*     */     
/* 648 */     if (objenesis.isWorthTrying()) {
/*     */       try {
/* 650 */         proxy = objenesis.newInstance(proxyClass, enhancer.getUseCache());
/*     */       }
/*     */       catch (ObjenesisException ex) {
/* 653 */         logger.debug("Unable to instantiate controller proxy using Objenesis, falling back to regular construction", ex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 658 */     if (proxy == null) {
/*     */       try {
/* 660 */         proxy = proxyClass.newInstance();
/*     */       }
/*     */       catch (Throwable ex) {
/* 663 */         throw new IllegalStateException("Unable to instantiate controller proxy using Objenesis, and regular controller instantiation via default constructor fails as well", ex);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 668 */     ((Factory)proxy).setCallbacks(new Callback[] { interceptor });
/* 669 */     return (T)proxy;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder withController(Class<?> controllerType)
/*     */   {
/* 681 */     return fromController(this.baseUrl, controllerType);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder withMethodName(Class<?> controllerType, String methodName, Object... args)
/*     */   {
/* 692 */     return fromMethodName(this.baseUrl, controllerType, methodName, args);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder withMethodCall(Object invocationInfo)
/*     */   {
/* 703 */     return fromMethodCall(this.baseUrl, invocationInfo);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MethodArgumentBuilder withMappingName(String mappingName)
/*     */   {
/* 714 */     return fromMappingName(this.baseUrl, mappingName);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public UriComponentsBuilder withMethod(Class<?> controllerType, Method method, Object... args)
/*     */   {
/* 725 */     return fromMethod(this.baseUrl, controllerType, method, args);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static class ControllerMethodInvocationInterceptor
/*     */     implements org.springframework.cglib.proxy.MethodInterceptor, org.aopalliance.intercept.MethodInterceptor
/*     */   {
/* 733 */     private static final Method getControllerMethod = ReflectionUtils.findMethod(MvcUriComponentsBuilder.MethodInvocationInfo.class, "getControllerMethod");
/*     */     
/*     */ 
/* 736 */     private static final Method getArgumentValues = ReflectionUtils.findMethod(MvcUriComponentsBuilder.MethodInvocationInfo.class, "getArgumentValues");
/*     */     
/*     */ 
/* 739 */     private static final Method getControllerType = ReflectionUtils.findMethod(MvcUriComponentsBuilder.MethodInvocationInfo.class, "getControllerType");
/*     */     
/*     */     private Method controllerMethod;
/*     */     
/*     */     private Object[] argumentValues;
/*     */     private Class<?> controllerType;
/*     */     
/*     */     ControllerMethodInvocationInterceptor(Class<?> controllerType)
/*     */     {
/* 748 */       this.controllerType = controllerType;
/*     */     }
/*     */     
/*     */     public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy)
/*     */     {
/* 753 */       if (getControllerMethod.equals(method)) {
/* 754 */         return this.controllerMethod;
/*     */       }
/* 756 */       if (getArgumentValues.equals(method)) {
/* 757 */         return this.argumentValues;
/*     */       }
/* 759 */       if (getControllerType.equals(method)) {
/* 760 */         return this.controllerType;
/*     */       }
/* 762 */       if (ReflectionUtils.isObjectMethod(method)) {
/* 763 */         return ReflectionUtils.invokeMethod(method, obj, args);
/*     */       }
/*     */       
/* 766 */       this.controllerMethod = method;
/* 767 */       this.argumentValues = args;
/* 768 */       Class<?> returnType = method.getReturnType();
/* 769 */       return Void.TYPE == returnType ? null : returnType.cast(MvcUriComponentsBuilder.initProxy(returnType, this));
/*     */     }
/*     */     
/*     */     public Object invoke(MethodInvocation inv)
/*     */       throws Throwable
/*     */     {
/* 775 */       return intercept(inv.getThis(), inv.getMethod(), inv.getArguments(), null);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public static abstract interface MethodInvocationInfo
/*     */   {
/*     */     public abstract Method getControllerMethod();
/*     */     
/*     */ 
/*     */     public abstract Object[] getArgumentValues();
/*     */     
/*     */ 
/*     */     public abstract Class<?> getControllerType();
/*     */   }
/*     */   
/*     */ 
/*     */   public static class MethodArgumentBuilder
/*     */   {
/*     */     private final Class<?> controllerType;
/*     */     
/*     */     private final Method method;
/*     */     
/*     */     private final Object[] argumentValues;
/*     */     
/*     */     private final UriComponentsBuilder baseUrl;
/*     */     
/*     */     public MethodArgumentBuilder(Class<?> controllerType, Method method)
/*     */     {
/* 804 */       this(null, controllerType, method);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     public MethodArgumentBuilder(UriComponentsBuilder baseUrl, Class<?> controllerType, Method method)
/*     */     {
/* 811 */       Assert.notNull(controllerType, "'controllerType' is required");
/* 812 */       Assert.notNull(method, "'method' is required");
/* 813 */       this.baseUrl = (baseUrl != null ? baseUrl : initBaseUrl());
/* 814 */       this.controllerType = controllerType;
/* 815 */       this.method = method;
/* 816 */       this.argumentValues = new Object[method.getParameterTypes().length];
/* 817 */       for (int i = 0; i < this.argumentValues.length; i++) {
/* 818 */         this.argumentValues[i] = null;
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     @Deprecated
/*     */     public MethodArgumentBuilder(Method method)
/*     */     {
/* 828 */       this(method.getDeclaringClass(), method);
/*     */     }
/*     */     
/*     */     private static UriComponentsBuilder initBaseUrl() {
/* 832 */       UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentServletMapping();
/* 833 */       return UriComponentsBuilder.fromPath(builder.build().getPath());
/*     */     }
/*     */     
/*     */     public MethodArgumentBuilder arg(int index, Object value) {
/* 837 */       this.argumentValues[index] = value;
/* 838 */       return this;
/*     */     }
/*     */     
/*     */     public String build() {
/* 842 */       return 
/* 843 */         MvcUriComponentsBuilder.fromMethodInternal(this.baseUrl, this.controllerType, this.method, this.argumentValues).build(false).encode().toUriString();
/*     */     }
/*     */     
/*     */     public String buildAndExpand(Object... uriVars) {
/* 847 */       return 
/* 848 */         MvcUriComponentsBuilder.fromMethodInternal(this.baseUrl, this.controllerType, this.method, this.argumentValues).build(false).expand(uriVars).encode().toString();
/*     */     }
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\method\annotation\MvcUriComponentsBuilder.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */