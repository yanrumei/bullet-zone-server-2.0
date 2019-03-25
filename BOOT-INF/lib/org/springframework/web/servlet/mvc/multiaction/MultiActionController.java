/*     */ package org.springframework.web.servlet.mvc.multiaction;
/*     */ 
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import javax.servlet.http.HttpSession;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.validation.ValidationUtils;
/*     */ import org.springframework.validation.Validator;
/*     */ import org.springframework.web.HttpSessionRequiredException;
/*     */ import org.springframework.web.bind.ServletRequestDataBinder;
/*     */ import org.springframework.web.bind.support.WebBindingInitializer;
/*     */ import org.springframework.web.context.request.ServletWebRequest;
/*     */ import org.springframework.web.servlet.ModelAndView;
/*     */ import org.springframework.web.servlet.mvc.AbstractController;
/*     */ import org.springframework.web.servlet.mvc.LastModified;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class MultiActionController
/*     */   extends AbstractController
/*     */   implements LastModified
/*     */ {
/*     */   public static final String LAST_MODIFIED_METHOD_SUFFIX = "LastModified";
/*     */   public static final String DEFAULT_COMMAND_NAME = "command";
/*     */   public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";
/* 152 */   protected static final Log pageNotFoundLogger = LogFactory.getLog("org.springframework.web.servlet.PageNotFound");
/*     */   
/*     */ 
/*     */   private Object delegate;
/*     */   
/*     */ 
/* 158 */   private MethodNameResolver methodNameResolver = new InternalPathMethodNameResolver();
/*     */   
/*     */ 
/*     */   private Validator[] validators;
/*     */   
/*     */ 
/*     */   private WebBindingInitializer webBindingInitializer;
/*     */   
/*     */ 
/* 167 */   private final Map<String, Method> handlerMethodMap = new HashMap();
/*     */   
/*     */ 
/* 170 */   private final Map<String, Method> lastModifiedMethodMap = new HashMap();
/*     */   
/*     */ 
/* 173 */   private final Map<Class<?>, Method> exceptionHandlerMap = new HashMap();
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MultiActionController()
/*     */   {
/* 181 */     this.delegate = this;
/* 182 */     registerHandlerMethods(this.delegate);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public MultiActionController(Object delegate)
/*     */   {
/* 193 */     setDelegate(delegate);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setDelegate(Object delegate)
/*     */   {
/* 205 */     Assert.notNull(delegate, "Delegate must not be null");
/* 206 */     this.delegate = delegate;
/* 207 */     registerHandlerMethods(this.delegate);
/*     */     
/* 209 */     if (this.handlerMethodMap.isEmpty()) {
/* 210 */       throw new IllegalStateException("No handler methods in class [" + this.delegate.getClass() + "]");
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setMethodNameResolver(MethodNameResolver methodNameResolver)
/*     */   {
/* 219 */     this.methodNameResolver = methodNameResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final MethodNameResolver getMethodNameResolver()
/*     */   {
/* 226 */     return this.methodNameResolver;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setValidators(Validator[] validators)
/*     */   {
/* 234 */     this.validators = validators;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public final Validator[] getValidators()
/*     */   {
/* 241 */     return this.validators;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void setWebBindingInitializer(WebBindingInitializer webBindingInitializer)
/*     */   {
/* 251 */     this.webBindingInitializer = webBindingInitializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final WebBindingInitializer getWebBindingInitializer()
/*     */   {
/* 259 */     return this.webBindingInitializer;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void registerHandlerMethods(Object delegate)
/*     */   {
/* 267 */     this.handlerMethodMap.clear();
/* 268 */     this.lastModifiedMethodMap.clear();
/* 269 */     this.exceptionHandlerMap.clear();
/*     */     
/*     */ 
/*     */ 
/* 273 */     Method[] methods = delegate.getClass().getMethods();
/* 274 */     for (Method method : methods)
/*     */     {
/* 276 */       if (isExceptionHandlerMethod(method)) {
/* 277 */         registerExceptionHandlerMethod(method);
/*     */       }
/* 279 */       else if (isHandlerMethod(method)) {
/* 280 */         registerHandlerMethod(method);
/* 281 */         registerLastModifiedMethodIfExists(delegate, method);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private boolean isHandlerMethod(Method method)
/*     */   {
/* 292 */     Class<?> returnType = method.getReturnType();
/* 293 */     if ((ModelAndView.class == returnType) || (Map.class == returnType) || (String.class == returnType) || (Void.TYPE == returnType))
/*     */     {
/* 295 */       Class<?>[] parameterTypes = method.getParameterTypes();
/* 296 */       if ((parameterTypes.length >= 2) && (HttpServletRequest.class == parameterTypes[0]) && (HttpServletResponse.class == parameterTypes[1])) {} return 
/*     */       
/*     */ 
/* 299 */         (!"handleRequest".equals(method.getName())) || (parameterTypes.length != 2);
/*     */     }
/* 301 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private boolean isExceptionHandlerMethod(Method method)
/*     */   {
/* 308 */     return (isHandlerMethod(method)) && 
/* 309 */       (method.getParameterTypes().length == 3) && 
/* 310 */       (Throwable.class.isAssignableFrom(method.getParameterTypes()[2]));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void registerHandlerMethod(Method method)
/*     */   {
/* 317 */     if (this.logger.isDebugEnabled()) {
/* 318 */       this.logger.debug("Found action method [" + method + "]");
/*     */     }
/* 320 */     this.handlerMethodMap.put(method.getName(), method);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private void registerLastModifiedMethodIfExists(Object delegate, Method method)
/*     */   {
/*     */     try
/*     */     {
/* 330 */       Method lastModifiedMethod = delegate.getClass().getMethod(method
/* 331 */         .getName() + "LastModified", new Class[] { HttpServletRequest.class });
/*     */       
/* 333 */       Class<?> returnType = lastModifiedMethod.getReturnType();
/* 334 */       if ((Long.TYPE != returnType) && (Long.class != returnType)) {
/* 335 */         throw new IllegalStateException("last-modified method [" + lastModifiedMethod + "] declares an invalid return type - needs to be 'long' or 'Long'");
/*     */       }
/*     */       
/*     */ 
/* 339 */       this.lastModifiedMethodMap.put(method.getName(), lastModifiedMethod);
/* 340 */       if (this.logger.isDebugEnabled()) {
/* 341 */         this.logger.debug("Found last-modified method for handler method [" + method + "]");
/*     */       }
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException) {}
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private void registerExceptionHandlerMethod(Method method)
/*     */   {
/* 353 */     this.exceptionHandlerMap.put(method.getParameterTypes()[2], method);
/* 354 */     if (this.logger.isDebugEnabled()) {
/* 355 */       this.logger.debug("Found exception handler method [" + method + "]");
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
/*     */   public long getLastModified(HttpServletRequest request)
/*     */   {
/*     */     try
/*     */     {
/* 372 */       String handlerMethodName = this.methodNameResolver.getHandlerMethodName(request);
/* 373 */       Method lastModifiedMethod = (Method)this.lastModifiedMethodMap.get(handlerMethodName);
/* 374 */       if (lastModifiedMethod != null) {
/*     */         try
/*     */         {
/* 377 */           Long wrappedLong = (Long)lastModifiedMethod.invoke(this.delegate, new Object[] { request });
/* 378 */           return wrappedLong != null ? wrappedLong.longValue() : -1L;
/*     */ 
/*     */         }
/*     */         catch (Exception ex)
/*     */         {
/* 383 */           this.logger.error("Failed to invoke last-modified method", ex);
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (NoSuchRequestHandlingMethodException localNoSuchRequestHandlingMethodException) {}
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 392 */     return -1L;
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
/*     */   protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/*     */     try
/*     */     {
/* 410 */       String methodName = this.methodNameResolver.getHandlerMethodName(request);
/* 411 */       return invokeNamedMethod(methodName, request, response);
/*     */     }
/*     */     catch (NoSuchRequestHandlingMethodException ex) {
/* 414 */       return handleNoSuchRequestHandlingMethod(ex, request, response);
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
/*     */   protected ModelAndView handleNoSuchRequestHandlingMethod(NoSuchRequestHandlingMethodException ex, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 433 */     pageNotFoundLogger.warn(ex.getMessage());
/* 434 */     response.sendError(404);
/* 435 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected final ModelAndView invokeNamedMethod(String methodName, HttpServletRequest request, HttpServletResponse response)
/*     */     throws Exception
/*     */   {
/* 446 */     Method method = (Method)this.handlerMethodMap.get(methodName);
/* 447 */     if (method == null) {
/* 448 */       throw new NoSuchRequestHandlingMethodException(methodName, getClass());
/*     */     }
/*     */     try
/*     */     {
/* 452 */       Class<?>[] paramTypes = method.getParameterTypes();
/* 453 */       List<Object> params = new ArrayList(4);
/* 454 */       params.add(request);
/* 455 */       params.add(response);
/*     */       
/* 457 */       if ((paramTypes.length >= 3) && (HttpSession.class == paramTypes[2])) {
/* 458 */         HttpSession session = request.getSession(false);
/* 459 */         if (session == null) {
/* 460 */           throw new HttpSessionRequiredException("Pre-existing session required for handler method '" + methodName + "'");
/*     */         }
/*     */         
/* 463 */         params.add(session);
/*     */       }
/*     */       
/*     */ 
/* 467 */       if ((paramTypes.length >= 3) && (HttpSession.class != paramTypes[(paramTypes.length - 1)])) {
/* 468 */         Object command = newCommandObject(paramTypes[(paramTypes.length - 1)]);
/* 469 */         params.add(command);
/* 470 */         bind(request, command);
/*     */       }
/*     */       
/* 473 */       Object returnValue = method.invoke(this.delegate, params.toArray(new Object[params.size()]));
/* 474 */       return massageReturnValueIfNecessary(returnValue);
/*     */     }
/*     */     catch (InvocationTargetException ex)
/*     */     {
/* 478 */       return handleException(request, response, ex.getTargetException());
/*     */     }
/*     */     catch (Exception ex)
/*     */     {
/* 482 */       return handleException(request, response, ex);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private ModelAndView massageReturnValueIfNecessary(Object returnValue)
/*     */   {
/* 493 */     if ((returnValue instanceof ModelAndView)) {
/* 494 */       return (ModelAndView)returnValue;
/*     */     }
/* 496 */     if ((returnValue instanceof Map)) {
/* 497 */       return new ModelAndView().addAllObjects((Map)returnValue);
/*     */     }
/* 499 */     if ((returnValue instanceof String)) {
/* 500 */       return new ModelAndView((String)returnValue);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 505 */     return null;
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
/*     */   protected Object newCommandObject(Class<?> clazz)
/*     */     throws Exception
/*     */   {
/* 519 */     if (this.logger.isDebugEnabled()) {
/* 520 */       this.logger.debug("Creating new command of class [" + clazz.getName() + "]");
/*     */     }
/* 522 */     return BeanUtils.instantiateClass(clazz);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected void bind(HttpServletRequest request, Object command)
/*     */     throws Exception
/*     */   {
/* 532 */     this.logger.debug("Binding request parameters onto MultiActionController command");
/* 533 */     ServletRequestDataBinder binder = createBinder(request, command);
/* 534 */     binder.bind(request);
/* 535 */     if (this.validators != null) {
/* 536 */       for (Validator validator : this.validators) {
/* 537 */         if (validator.supports(command.getClass())) {
/* 538 */           ValidationUtils.invokeValidator(validator, command, binder.getBindingResult());
/*     */         }
/*     */       }
/*     */     }
/* 542 */     binder.closeNoCatch();
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
/*     */   protected ServletRequestDataBinder createBinder(HttpServletRequest request, Object command)
/*     */     throws Exception
/*     */   {
/* 560 */     ServletRequestDataBinder binder = new ServletRequestDataBinder(command, getCommandName(command));
/* 561 */     initBinder(request, binder);
/* 562 */     return binder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String getCommandName(Object command)
/*     */   {
/* 573 */     return "command";
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
/*     */   protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
/*     */     throws Exception
/*     */   {
/* 594 */     if (this.webBindingInitializer != null) {
/* 595 */       this.webBindingInitializer.initBinder(binder, new ServletWebRequest(request));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Method getExceptionHandler(Throwable exception)
/*     */   {
/* 607 */     Class<?> exceptionClass = exception.getClass();
/* 608 */     if (this.logger.isDebugEnabled()) {
/* 609 */       this.logger.debug("Trying to find handler for exception class [" + exceptionClass.getName() + "]");
/*     */     }
/* 611 */     Method handler = (Method)this.exceptionHandlerMap.get(exceptionClass);
/* 612 */     while ((handler == null) && (exceptionClass != Throwable.class)) {
/* 613 */       if (this.logger.isDebugEnabled()) {
/* 614 */         this.logger.debug("Trying to find handler for exception superclass [" + exceptionClass.getName() + "]");
/*     */       }
/* 616 */       exceptionClass = exceptionClass.getSuperclass();
/* 617 */       handler = (Method)this.exceptionHandlerMap.get(exceptionClass);
/*     */     }
/* 619 */     return handler;
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
/*     */   private ModelAndView handleException(HttpServletRequest request, HttpServletResponse response, Throwable ex)
/*     */     throws Exception
/*     */   {
/* 633 */     Method handler = getExceptionHandler(ex);
/* 634 */     if (handler != null) {
/* 635 */       if (this.logger.isDebugEnabled()) {
/* 636 */         this.logger.debug("Invoking exception handler [" + handler + "] for exception: " + ex);
/*     */       }
/*     */       try {
/* 639 */         Object returnValue = handler.invoke(this.delegate, new Object[] { request, response, ex });
/* 640 */         return massageReturnValueIfNecessary(returnValue);
/*     */       }
/*     */       catch (InvocationTargetException ex2) {
/* 643 */         this.logger.error("Original exception overridden by exception handling failure", ex);
/* 644 */         ReflectionUtils.rethrowException(ex2.getTargetException());
/*     */       }
/*     */       catch (Exception ex2) {
/* 647 */         this.logger.error("Failed to invoke exception handler method", ex2);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 652 */       ReflectionUtils.rethrowException(ex);
/*     */     }
/* 654 */     throw new IllegalStateException("Should never get here");
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-webmvc-4.3.14.RELEASE.jar!\org\springframework\web\servlet\mvc\multiaction\MultiActionController.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */