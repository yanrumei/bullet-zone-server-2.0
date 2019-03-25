/*     */ package org.springframework.web.servlet.mvc.annotation;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.Writer;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.Principal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.servlet.ServletException;
/*     */ import javax.servlet.ServletRequest;
/*     */ import javax.servlet.ServletResponse;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.springframework.core.ExceptionDepthComparator;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.annotation.AnnotatedElementUtils;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.annotation.SynthesizingMethodParameter;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.ByteArrayHttpMessageConverter;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.http.converter.StringHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.SourceHttpMessageConverter;
/*     */ import org.springframework.http.converter.xml.XmlAwareFormHttpMessageConverter;
/*     */ import org.springframework.http.server.ServletServerHttpRequest;
/*     */ import org.springframework.http.server.ServletServerHttpResponse;
/*     */ import org.springframework.ui.Model;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.ObjectUtils;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.util.ReflectionUtils.MethodCallback;
/*     */ import org.springframework.util.StringUtils;
/*     */ import org.springframework.web.bind.annotation.ExceptionHandler;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
/*     */ import org.springframework.web.bind.annotation.ResponseStatus;
/*     */ import org.springframework.web.bind.support.WebArgumentResolver;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.View;
/*     */ import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;
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
/*     */ @Deprecated
/*     */ public class AnnotationMethodHandlerExceptionResolver
/*     */   extends AbstractHandlerExceptionResolver
/*     */ {
/*  94 */   private static final Method NO_METHOD_FOUND = ClassUtils.getMethodIfAvailable(System.class, "currentTimeMillis", new Class[0]);
/*     */   
/*     */ 
/*  97 */   private final Map<Class<?>, Map<Class<? extends Throwable>, Method>> exceptionHandlerCache = new ConcurrentHashMap(64);
/*     */   
/*     */ 
/*     */   private WebArgumentResolver[] customArgumentResolvers;
/*     */   
/* 102 */   private HttpMessageConverter<?>[] messageConverters = { new ByteArrayHttpMessageConverter(), new StringHttpMessageConverter(), new SourceHttpMessageConverter(), new XmlAwareFormHttpMessageConverter() };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCustomArgumentResolver(WebArgumentResolver argumentResolver)
/*     */   {
/* 114 */     this.customArgumentResolvers = new WebArgumentResolver[] { argumentResolver };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setCustomArgumentResolvers(WebArgumentResolver[] argumentResolvers)
/*     */   {
/* 123 */     this.customArgumentResolvers = argumentResolvers;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setMessageConverters(HttpMessageConverter<?>[] messageConverters)
/*     */   {
/* 131 */     this.messageConverters = messageConverters;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
/*     */   {
/* 139 */     if (handler != null) {
/* 140 */       Method handlerMethod = findBestExceptionHandlerMethod(handler, ex);
/* 141 */       if (handlerMethod != null) {
/* 142 */         ServletWebRequest webRequest = new ServletWebRequest(request, response);
/*     */         try {
/* 144 */           Object[] args = resolveHandlerArguments(handlerMethod, handler, webRequest, ex);
/* 145 */           if (this.logger.isDebugEnabled()) {
/* 146 */             this.logger.debug("Invoking request handler method: " + handlerMethod);
/*     */           }
/* 148 */           Object retVal = doInvokeMethod(handlerMethod, handler, args);
/* 149 */           return getModelAndView(handlerMethod, retVal, webRequest);
/*     */         }
/*     */         catch (Exception invocationEx) {
/* 152 */           this.logger.error("Invoking request method resulted in exception : " + handlerMethod, invocationEx);
/*     */         }
/*     */       }
/*     */     }
/* 156 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Method findBestExceptionHandlerMethod(Object handler, Exception thrownException)
/*     */   {
/* 166 */     final Class<?> handlerType = ClassUtils.getUserClass(handler);
/* 167 */     final Class<? extends Throwable> thrownExceptionType = thrownException.getClass();
/* 168 */     Method handlerMethod = null;
/*     */     
/* 170 */     Map<Class<? extends Throwable>, Method> handlers = (Map)this.exceptionHandlerCache.get(handlerType);
/* 171 */     if (handlers != null) {
/* 172 */       handlerMethod = (Method)handlers.get(thrownExceptionType);
/* 173 */       if (handlerMethod != null) {
/* 174 */         return handlerMethod == NO_METHOD_FOUND ? null : handlerMethod;
/*     */       }
/*     */     }
/*     */     else {
/* 178 */       handlers = new ConcurrentHashMap(16);
/* 179 */       this.exceptionHandlerCache.put(handlerType, handlers);
/*     */     }
/*     */     
/* 182 */     final Map<Class<? extends Throwable>, Method> matchedHandlers = new HashMap();
/*     */     
/* 184 */     ReflectionUtils.doWithMethods(handlerType, new ReflectionUtils.MethodCallback()
/*     */     {
/*     */       public void doWith(Method method) {
/* 187 */         method = ClassUtils.getMostSpecificMethod(method, handlerType);
/* 188 */         List<Class<? extends Throwable>> handledExceptions = AnnotationMethodHandlerExceptionResolver.this.getHandledExceptions(method);
/* 189 */         for (Class<? extends Throwable> handledException : handledExceptions) {
/* 190 */           if (handledException.isAssignableFrom(thrownExceptionType)) {
/* 191 */             if (!matchedHandlers.containsKey(handledException)) {
/* 192 */               matchedHandlers.put(handledException, method);
/*     */             }
/*     */             else {
/* 195 */               Method oldMappedMethod = (Method)matchedHandlers.get(handledException);
/* 196 */               if (!oldMappedMethod.equals(method)) {
/* 197 */                 throw new IllegalStateException("Ambiguous exception handler mapped for " + handledException + "]: {" + oldMappedMethod + ", " + method + "}.");
/*     */               }
/*     */               
/*     */             }
/*     */             
/*     */           }
/*     */           
/*     */         }
/*     */       }
/* 206 */     });
/* 207 */     handlerMethod = getBestMatchingMethod(matchedHandlers, thrownException);
/* 208 */     handlers.put(thrownExceptionType, handlerMethod == null ? NO_METHOD_FOUND : handlerMethod);
/* 209 */     return handlerMethod;
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
/*     */   protected List<Class<? extends Throwable>> getHandledExceptions(Method method)
/*     */   {
/* 222 */     List<Class<? extends Throwable>> result = new ArrayList();
/* 223 */     ExceptionHandler exceptionHandler = (ExceptionHandler)AnnotationUtils.findAnnotation(method, ExceptionHandler.class);
/* 224 */     if (exceptionHandler != null) {
/* 225 */       if (!ObjectUtils.isEmpty(exceptionHandler.value())) {
/* 226 */         result.addAll(Arrays.asList(exceptionHandler.value()));
/*     */       }
/*     */       else {
/* 229 */         for (Class<?> param : method.getParameterTypes()) {
/* 230 */           if (Throwable.class.isAssignableFrom(param)) {
/* 231 */             result.add(param);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 236 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private Method getBestMatchingMethod(Map<Class<? extends Throwable>, Method> resolverMethods, Exception thrownException)
/*     */   {
/* 246 */     if (resolverMethods.isEmpty()) {
/* 247 */       return null;
/*     */     }
/*     */     
/* 250 */     Class<? extends Throwable> closestMatch = ExceptionDepthComparator.findClosestMatch(resolverMethods.keySet(), thrownException);
/* 251 */     Method method = (Method)resolverMethods.get(closestMatch);
/* 252 */     return (method == null) || (NO_METHOD_FOUND == method) ? null : method;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object[] resolveHandlerArguments(Method handlerMethod, Object handler, NativeWebRequest webRequest, Exception thrownException)
/*     */     throws Exception
/*     */   {
/* 261 */     Class<?>[] paramTypes = handlerMethod.getParameterTypes();
/* 262 */     Object[] args = new Object[paramTypes.length];
/* 263 */     Class<?> handlerType = handler.getClass();
/* 264 */     for (int i = 0; i < args.length; i++) {
/* 265 */       MethodParameter methodParam = new SynthesizingMethodParameter(handlerMethod, i);
/* 266 */       GenericTypeResolver.resolveParameterType(methodParam, handlerType);
/* 267 */       Class<?> paramType = methodParam.getParameterType();
/* 268 */       Object argValue = resolveCommonArgument(methodParam, webRequest, thrownException);
/* 269 */       if (argValue != WebArgumentResolver.UNRESOLVED) {
/* 270 */         args[i] = argValue;
/*     */       }
/*     */       else {
/* 273 */         throw new IllegalStateException("Unsupported argument [" + paramType.getName() + "] for @ExceptionHandler method: " + handlerMethod);
/*     */       }
/*     */     }
/*     */     
/* 277 */     return args;
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
/*     */   protected Object resolveCommonArgument(MethodParameter methodParameter, NativeWebRequest webRequest, Exception thrownException)
/*     */     throws Exception
/*     */   {
/* 292 */     if (this.customArgumentResolvers != null) {
/* 293 */       for (WebArgumentResolver argumentResolver : this.customArgumentResolvers) {
/* 294 */         Object value = argumentResolver.resolveArgument(methodParameter, webRequest);
/* 295 */         if (value != WebArgumentResolver.UNRESOLVED) {
/* 296 */           return value;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 302 */     Object paramType = methodParameter.getParameterType();
/* 303 */     Object value = resolveStandardArgument((Class)paramType, webRequest, thrownException);
/* 304 */     if ((value != WebArgumentResolver.UNRESOLVED) && (!ClassUtils.isAssignableValue((Class)paramType, value)))
/*     */     {
/*     */ 
/* 307 */       throw new IllegalStateException("Standard argument type [" + ((Class)paramType).getName() + "] resolved to incompatible value of type [" + (value != null ? value.getClass() : null) + "]. Consider declaring the argument type in a less specific fashion.");
/*     */     }
/*     */     
/* 310 */     return value;
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
/*     */   protected Object resolveStandardArgument(Class<?> parameterType, NativeWebRequest webRequest, Exception thrownException)
/*     */     throws Exception
/*     */   {
/* 326 */     if (parameterType.isInstance(thrownException)) {
/* 327 */       return thrownException;
/*     */     }
/* 329 */     if (WebRequest.class.isAssignableFrom(parameterType)) {
/* 330 */       return webRequest;
/*     */     }
/*     */     
/* 333 */     HttpServletRequest request = (HttpServletRequest)webRequest.getNativeRequest(HttpServletRequest.class);
/* 334 */     HttpServletResponse response = (HttpServletResponse)webRequest.getNativeResponse(HttpServletResponse.class);
/*     */     
/* 336 */     if (ServletRequest.class.isAssignableFrom(parameterType)) {
/* 337 */       return request;
/*     */     }
/* 339 */     if (ServletResponse.class.isAssignableFrom(parameterType)) {
/* 340 */       return response;
/*     */     }
/* 342 */     if (HttpSession.class.isAssignableFrom(parameterType)) {
/* 343 */       return request.getSession();
/*     */     }
/* 345 */     if (Principal.class.isAssignableFrom(parameterType)) {
/* 346 */       return request.getUserPrincipal();
/*     */     }
/* 348 */     if (Locale.class == parameterType) {
/* 349 */       return RequestContextUtils.getLocale(request);
/*     */     }
/* 351 */     if (InputStream.class.isAssignableFrom(parameterType)) {
/* 352 */       return request.getInputStream();
/*     */     }
/* 354 */     if (Reader.class.isAssignableFrom(parameterType)) {
/* 355 */       return request.getReader();
/*     */     }
/* 357 */     if (OutputStream.class.isAssignableFrom(parameterType)) {
/* 358 */       return response.getOutputStream();
/*     */     }
/* 360 */     if (Writer.class.isAssignableFrom(parameterType)) {
/* 361 */       return response.getWriter();
/*     */     }
/*     */     
/* 364 */     return WebArgumentResolver.UNRESOLVED;
/*     */   }
/*     */   
/*     */   private Object doInvokeMethod(Method method, Object target, Object[] args)
/*     */     throws Exception
/*     */   {
/* 370 */     ReflectionUtils.makeAccessible(method);
/*     */     try {
/* 372 */       return method.invoke(target, args);
/*     */     }
/*     */     catch (InvocationTargetException ex) {
/* 375 */       ReflectionUtils.rethrowException(ex.getTargetException());
/*     */       
/* 377 */       throw new IllegalStateException("Should never get here");
/*     */     }
/*     */   }
/*     */   
/*     */   private ModelAndView getModelAndView(Method handlerMethod, Object returnValue, ServletWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/* 384 */     ResponseStatus responseStatus = (ResponseStatus)AnnotatedElementUtils.findMergedAnnotation(handlerMethod, ResponseStatus.class);
/* 385 */     if (responseStatus != null) {
/* 386 */       HttpStatus statusCode = responseStatus.code();
/* 387 */       String reason = responseStatus.reason();
/* 388 */       if (!StringUtils.hasText(reason)) {
/* 389 */         webRequest.getResponse().setStatus(statusCode.value());
/*     */       }
/*     */       else {
/* 392 */         webRequest.getResponse().sendError(statusCode.value(), reason);
/*     */       }
/*     */     }
/*     */     
/* 396 */     if ((returnValue != null) && (AnnotationUtils.findAnnotation(handlerMethod, ResponseBody.class) != null)) {
/* 397 */       return handleResponseBody(returnValue, webRequest);
/*     */     }
/*     */     
/* 400 */     if ((returnValue instanceof ModelAndView)) {
/* 401 */       return (ModelAndView)returnValue;
/*     */     }
/* 403 */     if ((returnValue instanceof Model)) {
/* 404 */       return new ModelAndView().addAllObjects(((Model)returnValue).asMap());
/*     */     }
/* 406 */     if ((returnValue instanceof Map)) {
/* 407 */       return new ModelAndView().addAllObjects((Map)returnValue);
/*     */     }
/* 409 */     if ((returnValue instanceof View)) {
/* 410 */       return new ModelAndView((View)returnValue);
/*     */     }
/* 412 */     if ((returnValue instanceof String)) {
/* 413 */       return new ModelAndView((String)returnValue);
/*     */     }
/* 415 */     if (returnValue == null) {
/* 416 */       return new ModelAndView();
/*     */     }
/*     */     
/* 419 */     throw new IllegalArgumentException("Invalid handler method return value: " + returnValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private ModelAndView handleResponseBody(Object returnValue, ServletWebRequest webRequest)
/*     */     throws ServletException, IOException
/*     */   {
/* 427 */     HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest.getRequest());
/* 428 */     List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
/* 429 */     if (acceptedMediaTypes.isEmpty()) {
/* 430 */       acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
/*     */     }
/* 432 */     MediaType.sortByQualityValue(acceptedMediaTypes);
/* 433 */     HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());
/* 434 */     Class<?> returnValueType = returnValue.getClass();
/* 435 */     if (this.messageConverters != null) {
/* 436 */       for (MediaType acceptedMediaType : acceptedMediaTypes) {
/* 437 */         for (HttpMessageConverter messageConverter : this.messageConverters) {
/* 438 */           if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
/* 439 */             messageConverter.write(returnValue, acceptedMediaType, outputMessage);
/* 440 */             return new ModelAndView();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 445 */     if (this.logger.isWarnEnabled()) {
/* 446 */       this.logger.warn("Could not find HttpMessageConverter that supports return type [" + returnValueType + "] and " + acceptedMediaTypes);
/*     */     }
/*     */     
/* 449 */     return null;
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\annotation\AnnotationMethodHandlerExceptionResolver.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */