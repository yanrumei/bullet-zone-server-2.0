/*     */ package org.springframework.web.bind.annotation.support;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import org.apache.commons.logging.Log;
/*     */ import org.apache.commons.logging.LogFactory;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.core.BridgeMethodResolver;
/*     */ import org.springframework.core.Conventions;
/*     */ import org.springframework.core.GenericTypeResolver;
/*     */ import org.springframework.core.MethodParameter;
/*     */ import org.springframework.core.ParameterNameDiscoverer;
/*     */ import org.springframework.core.annotation.AnnotationUtils;
/*     */ import org.springframework.core.annotation.SynthesizingMethodParameter;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpInputMessage;
/*     */ import org.springframework.http.HttpOutputMessage;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.converter.HttpMessageConverter;
/*     */ import org.springframework.ui.ExtendedModelMap;
/*     */ import org.springframework.ui.Model;
/*     */ import org.springframework.util.Assert;
/*     */ import org.springframework.util.ClassUtils;
/*     */ import org.springframework.util.LinkedMultiValueMap;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.util.ReflectionUtils;
/*     */ import org.springframework.validation.BindException;
/*     */ import org.springframework.validation.BindingResult;
/*     */ import org.springframework.validation.Errors;
/*     */ import org.springframework.validation.annotation.Validated;
/*     */ import org.springframework.web.HttpMediaTypeNotSupportedException;
/*     */ import org.springframework.web.bind.WebDataBinder;
/*     */ import org.springframework.web.bind.annotation.CookieValue;
/*     */ import org.springframework.web.bind.annotation.InitBinder;
/*     */ import org.springframework.web.bind.annotation.ModelAttribute;
/*     */ import org.springframework.web.bind.annotation.PathVariable;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestHeader;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.support.DefaultSessionAttributeStore;
/*     */ import org.springframework.web.bind.support.SessionAttributeStore;
/*     */ import org.springframework.web.bind.support.SessionStatus;
/*     */ import org.springframework.web.bind.support.SimpleSessionStatus;
/*     */ import org.springframework.web.bind.support.WebArgumentResolver;
/*     */ import org.springframework.web.bind.support.WebBindingInitializer;
/*     */ import org.springframework.web.bind.support.WebRequestDataBinder;
/*     */ import org.springframework.web.context.request.NativeWebRequest;
/*     */ import org.springframework.web.context.request.WebRequest;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ import org.springframework.web.multipart.MultipartRequest;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
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
/*     */ public class HandlerMethodInvoker
/*     */ {
/* 102 */   private static final String MODEL_KEY_PREFIX_STALE = SessionAttributeStore.class.getName() + ".STALE.";
/*     */   
/*     */ 
/* 105 */   private static final Log logger = LogFactory.getLog(HandlerMethodInvoker.class);
/*     */   
/*     */   private final HandlerMethodResolver methodResolver;
/*     */   
/*     */   private final WebBindingInitializer bindingInitializer;
/*     */   
/*     */   private final SessionAttributeStore sessionAttributeStore;
/*     */   
/*     */   private final ParameterNameDiscoverer parameterNameDiscoverer;
/*     */   
/*     */   private final WebArgumentResolver[] customArgumentResolvers;
/*     */   
/*     */   private final HttpMessageConverter<?>[] messageConverters;
/*     */   
/* 119 */   private final SimpleSessionStatus sessionStatus = new SimpleSessionStatus();
/*     */   
/*     */   public HandlerMethodInvoker(HandlerMethodResolver methodResolver)
/*     */   {
/* 123 */     this(methodResolver, null);
/*     */   }
/*     */   
/*     */   public HandlerMethodInvoker(HandlerMethodResolver methodResolver, WebBindingInitializer bindingInitializer) {
/* 127 */     this(methodResolver, bindingInitializer, new DefaultSessionAttributeStore(), null, null, null);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public HandlerMethodInvoker(HandlerMethodResolver methodResolver, WebBindingInitializer bindingInitializer, SessionAttributeStore sessionAttributeStore, ParameterNameDiscoverer parameterNameDiscoverer, WebArgumentResolver[] customArgumentResolvers, HttpMessageConverter<?>[] messageConverters)
/*     */   {
/* 134 */     this.methodResolver = methodResolver;
/* 135 */     this.bindingInitializer = bindingInitializer;
/* 136 */     this.sessionAttributeStore = sessionAttributeStore;
/* 137 */     this.parameterNameDiscoverer = parameterNameDiscoverer;
/* 138 */     this.customArgumentResolvers = customArgumentResolvers;
/* 139 */     this.messageConverters = messageConverters;
/*     */   }
/*     */   
/*     */ 
/*     */   public final Object invokeHandlerMethod(Method handlerMethod, Object handler, NativeWebRequest webRequest, ExtendedModelMap implicitModel)
/*     */     throws Exception
/*     */   {
/* 146 */     Method handlerMethodToInvoke = BridgeMethodResolver.findBridgedMethod(handlerMethod);
/*     */     try {
/* 148 */       boolean debug = logger.isDebugEnabled();
/* 149 */       for (String attrName : this.methodResolver.getActualSessionAttributeNames()) {
/* 150 */         Object attrValue = this.sessionAttributeStore.retrieveAttribute(webRequest, attrName);
/* 151 */         if (attrValue != null) {
/* 152 */           implicitModel.addAttribute(attrName, attrValue);
/*     */         }
/*     */       }
/* 155 */       for (Method attributeMethod : this.methodResolver.getModelAttributeMethods()) {
/* 156 */         Method attributeMethodToInvoke = BridgeMethodResolver.findBridgedMethod(attributeMethod);
/* 157 */         Object[] args = resolveHandlerArguments(attributeMethodToInvoke, handler, webRequest, implicitModel);
/* 158 */         if (debug) {
/* 159 */           logger.debug("Invoking model attribute method: " + attributeMethodToInvoke);
/*     */         }
/* 161 */         String attrName = ((ModelAttribute)AnnotationUtils.findAnnotation(attributeMethod, ModelAttribute.class)).value();
/* 162 */         if (("".equals(attrName)) || (!implicitModel.containsAttribute(attrName)))
/*     */         {
/*     */ 
/* 165 */           ReflectionUtils.makeAccessible(attributeMethodToInvoke);
/* 166 */           Object attrValue = attributeMethodToInvoke.invoke(handler, args);
/* 167 */           if ("".equals(attrName)) {
/* 168 */             Class<?> resolvedType = GenericTypeResolver.resolveReturnType(attributeMethodToInvoke, handler.getClass());
/* 169 */             attrName = Conventions.getVariableNameForReturnType(attributeMethodToInvoke, resolvedType, attrValue);
/*     */           }
/* 171 */           if (!implicitModel.containsAttribute(attrName))
/* 172 */             implicitModel.addAttribute(attrName, attrValue);
/*     */         }
/*     */       }
/* 175 */       Object[] args = resolveHandlerArguments(handlerMethodToInvoke, handler, webRequest, implicitModel);
/* 176 */       if (debug) {
/* 177 */         logger.debug("Invoking request handler method: " + handlerMethodToInvoke);
/*     */       }
/* 179 */       ReflectionUtils.makeAccessible(handlerMethodToInvoke);
/* 180 */       return handlerMethodToInvoke.invoke(handler, args);
/*     */ 
/*     */     }
/*     */     catch (IllegalStateException ex)
/*     */     {
/* 185 */       throw new HandlerMethodInvocationException(handlerMethodToInvoke, ex);
/*     */     }
/*     */     catch (InvocationTargetException ex)
/*     */     {
/* 189 */       ReflectionUtils.rethrowException(ex.getTargetException()); }
/* 190 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public final void updateModelAttributes(Object handler, Map<String, Object> mavModel, ExtendedModelMap implicitModel, NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/* 197 */     if ((this.methodResolver.hasSessionAttributes()) && (this.sessionStatus.isComplete())) {
/* 198 */       for (String attrName : this.methodResolver.getActualSessionAttributeNames()) {
/* 199 */         this.sessionAttributeStore.cleanupAttribute(webRequest, attrName);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 205 */     Object model = mavModel != null ? mavModel : implicitModel;
/* 206 */     if (model != null) {
/*     */       try {
/* 208 */         String[] originalAttrNames = (String[])((Map)model).keySet().toArray(new String[((Map)model).size()]);
/* 209 */         for (String attrName : originalAttrNames) {
/* 210 */           Object attrValue = ((Map)model).get(attrName);
/* 211 */           boolean isSessionAttr = this.methodResolver.isSessionAttribute(attrName, attrValue != null ? attrValue
/* 212 */             .getClass() : null);
/* 213 */           if (isSessionAttr) {
/* 214 */             if (this.sessionStatus.isComplete()) {
/* 215 */               implicitModel.put(MODEL_KEY_PREFIX_STALE + attrName, Boolean.TRUE);
/*     */             }
/* 217 */             else if (!implicitModel.containsKey(MODEL_KEY_PREFIX_STALE + attrName)) {
/* 218 */               this.sessionAttributeStore.storeAttribute(webRequest, attrName, attrValue);
/*     */             }
/*     */           }
/* 221 */           if ((!attrName.startsWith(BindingResult.MODEL_KEY_PREFIX)) && ((isSessionAttr) || 
/* 222 */             (isBindingCandidate(attrValue)))) {
/* 223 */             String bindingResultKey = BindingResult.MODEL_KEY_PREFIX + attrName;
/* 224 */             if ((mavModel != null) && (!((Map)model).containsKey(bindingResultKey))) {
/* 225 */               WebDataBinder binder = createBinder(webRequest, attrValue, attrName);
/* 226 */               initBinder(handler, attrName, binder, webRequest);
/* 227 */               mavModel.put(bindingResultKey, binder.getBindingResult());
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (InvocationTargetException ex)
/*     */       {
/* 234 */         ReflectionUtils.rethrowException(ex.getTargetException());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private Object[] resolveHandlerArguments(Method handlerMethod, Object handler, NativeWebRequest webRequest, ExtendedModelMap implicitModel)
/*     */     throws Exception
/*     */   {
/* 243 */     Class<?>[] paramTypes = handlerMethod.getParameterTypes();
/* 244 */     Object[] args = new Object[paramTypes.length];
/*     */     
/* 246 */     for (int i = 0; i < args.length; i++) {
/* 247 */       MethodParameter methodParam = new SynthesizingMethodParameter(handlerMethod, i);
/* 248 */       methodParam.initParameterNameDiscovery(this.parameterNameDiscoverer);
/* 249 */       GenericTypeResolver.resolveParameterType(methodParam, handler.getClass());
/* 250 */       String paramName = null;
/* 251 */       String headerName = null;
/* 252 */       boolean requestBodyFound = false;
/* 253 */       String cookieName = null;
/* 254 */       String pathVarName = null;
/* 255 */       String attrName = null;
/* 256 */       boolean required = false;
/* 257 */       String defaultValue = null;
/* 258 */       boolean validate = false;
/* 259 */       Object[] validationHints = null;
/* 260 */       int annotationsFound = 0;
/* 261 */       Annotation[] paramAnns = methodParam.getParameterAnnotations();
/*     */       
/* 263 */       for (Annotation paramAnn : paramAnns) {
/* 264 */         if (RequestParam.class.isInstance(paramAnn)) {
/* 265 */           RequestParam requestParam = (RequestParam)paramAnn;
/* 266 */           paramName = requestParam.name();
/* 267 */           required = requestParam.required();
/* 268 */           defaultValue = parseDefaultValueAttribute(requestParam.defaultValue());
/* 269 */           annotationsFound++;
/*     */         }
/* 271 */         else if (RequestHeader.class.isInstance(paramAnn)) {
/* 272 */           RequestHeader requestHeader = (RequestHeader)paramAnn;
/* 273 */           headerName = requestHeader.name();
/* 274 */           required = requestHeader.required();
/* 275 */           defaultValue = parseDefaultValueAttribute(requestHeader.defaultValue());
/* 276 */           annotationsFound++;
/*     */         }
/* 278 */         else if (RequestBody.class.isInstance(paramAnn)) {
/* 279 */           requestBodyFound = true;
/* 280 */           annotationsFound++;
/*     */         }
/* 282 */         else if (CookieValue.class.isInstance(paramAnn)) {
/* 283 */           CookieValue cookieValue = (CookieValue)paramAnn;
/* 284 */           cookieName = cookieValue.name();
/* 285 */           required = cookieValue.required();
/* 286 */           defaultValue = parseDefaultValueAttribute(cookieValue.defaultValue());
/* 287 */           annotationsFound++;
/*     */         }
/* 289 */         else if (PathVariable.class.isInstance(paramAnn)) {
/* 290 */           PathVariable pathVar = (PathVariable)paramAnn;
/* 291 */           pathVarName = pathVar.value();
/* 292 */           annotationsFound++;
/*     */         }
/* 294 */         else if (ModelAttribute.class.isInstance(paramAnn)) {
/* 295 */           ModelAttribute attr = (ModelAttribute)paramAnn;
/* 296 */           attrName = attr.value();
/* 297 */           annotationsFound++;
/*     */         }
/* 299 */         else if (Value.class.isInstance(paramAnn)) {
/* 300 */           defaultValue = ((Value)paramAnn).value();
/*     */         }
/*     */         else {
/* 303 */           Validated validatedAnn = (Validated)AnnotationUtils.getAnnotation(paramAnn, Validated.class);
/* 304 */           if ((validatedAnn != null) || (paramAnn.annotationType().getSimpleName().startsWith("Valid"))) {
/* 305 */             validate = true;
/* 306 */             Object hints = validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(paramAnn);
/* 307 */             validationHints = new Object[] { (hints instanceof Object[]) ? (Object[])hints : hints };
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 312 */       if (annotationsFound > 1) {
/* 313 */         throw new IllegalStateException("Handler parameter annotations are exclusive choices - do not specify more than one such annotation on the same parameter: " + handlerMethod);
/*     */       }
/*     */       
/*     */ 
/* 317 */       if (annotationsFound == 0) {
/* 318 */         Object argValue = resolveCommonArgument(methodParam, webRequest);
/* 319 */         if (argValue != WebArgumentResolver.UNRESOLVED) {
/* 320 */           args[i] = argValue;
/*     */         }
/* 322 */         else if (defaultValue != null) {
/* 323 */           args[i] = resolveDefaultValue(defaultValue);
/*     */         }
/*     */         else {
/* 326 */           Object paramType = methodParam.getParameterType();
/* 327 */           if ((Model.class.isAssignableFrom((Class)paramType)) || (Map.class.isAssignableFrom((Class)paramType))) {
/* 328 */             if (!((Class)paramType).isAssignableFrom(implicitModel.getClass())) {
/* 329 */               throw new IllegalStateException("Argument [" + ((Class)paramType).getSimpleName() + "] is of type Model or Map but is not assignable from the actual model. You may need to switch newer MVC infrastructure classes to use this argument.");
/*     */             }
/*     */             
/*     */ 
/* 333 */             args[i] = implicitModel;
/*     */           }
/* 335 */           else if (SessionStatus.class.isAssignableFrom((Class)paramType)) {
/* 336 */             args[i] = this.sessionStatus;
/*     */           }
/* 338 */           else if (HttpEntity.class.isAssignableFrom((Class)paramType)) {
/* 339 */             args[i] = resolveHttpEntityRequest(methodParam, webRequest);
/*     */           } else {
/* 341 */             if (Errors.class.isAssignableFrom((Class)paramType)) {
/* 342 */               throw new IllegalStateException("Errors/BindingResult argument declared without preceding model attribute. Check your handler method signature!");
/*     */             }
/*     */             
/* 345 */             if (BeanUtils.isSimpleProperty((Class)paramType)) {
/* 346 */               paramName = "";
/*     */             }
/*     */             else {
/* 349 */               attrName = "";
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 354 */       if (paramName != null) {
/* 355 */         args[i] = resolveRequestParam(paramName, required, defaultValue, methodParam, webRequest, handler);
/*     */       }
/* 357 */       else if (headerName != null) {
/* 358 */         args[i] = resolveRequestHeader(headerName, required, defaultValue, methodParam, webRequest, handler);
/*     */       }
/* 360 */       else if (requestBodyFound) {
/* 361 */         args[i] = resolveRequestBody(methodParam, webRequest, handler);
/*     */       }
/* 363 */       else if (cookieName != null) {
/* 364 */         args[i] = resolveCookieValue(cookieName, required, defaultValue, methodParam, webRequest, handler);
/*     */       }
/* 366 */       else if (pathVarName != null) {
/* 367 */         args[i] = resolvePathVariable(pathVarName, methodParam, webRequest, handler);
/*     */       }
/* 369 */       else if (attrName != null)
/*     */       {
/* 371 */         WebDataBinder binder = resolveModelAttribute(attrName, methodParam, implicitModel, webRequest, handler);
/* 372 */         boolean assignBindingResult = (args.length > i + 1) && (Errors.class.isAssignableFrom(paramTypes[(i + 1)]));
/* 373 */         if (binder.getTarget() != null) {
/* 374 */           doBind(binder, webRequest, validate, validationHints, !assignBindingResult);
/*     */         }
/* 376 */         args[i] = binder.getTarget();
/* 377 */         if (assignBindingResult) {
/* 378 */           args[(i + 1)] = binder.getBindingResult();
/* 379 */           i++;
/*     */         }
/* 381 */         implicitModel.putAll(binder.getBindingResult().getModel());
/*     */       }
/*     */     }
/*     */     
/* 385 */     return args;
/*     */   }
/*     */   
/*     */   protected void initBinder(Object handler, String attrName, WebDataBinder binder, NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/* 391 */     if (this.bindingInitializer != null)
/* 392 */       this.bindingInitializer.initBinder(binder, webRequest);
/*     */     boolean debug;
/* 394 */     if (handler != null) {
/* 395 */       Set<Method> initBinderMethods = this.methodResolver.getInitBinderMethods();
/* 396 */       if (!initBinderMethods.isEmpty()) {
/* 397 */         debug = logger.isDebugEnabled();
/* 398 */         for (Method initBinderMethod : initBinderMethods) {
/* 399 */           Method methodToInvoke = BridgeMethodResolver.findBridgedMethod(initBinderMethod);
/* 400 */           String[] targetNames = ((InitBinder)AnnotationUtils.findAnnotation(initBinderMethod, InitBinder.class)).value();
/* 401 */           if ((targetNames.length == 0) || (Arrays.asList(targetNames).contains(attrName)))
/*     */           {
/* 403 */             Object[] initBinderArgs = resolveInitBinderArguments(handler, methodToInvoke, binder, webRequest);
/* 404 */             if (debug) {
/* 405 */               logger.debug("Invoking init-binder method: " + methodToInvoke);
/*     */             }
/* 407 */             ReflectionUtils.makeAccessible(methodToInvoke);
/* 408 */             Object returnValue = methodToInvoke.invoke(handler, initBinderArgs);
/* 409 */             if (returnValue != null) {
/* 410 */               throw new IllegalStateException("InitBinder methods must not have a return value: " + methodToInvoke);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   private Object[] resolveInitBinderArguments(Object handler, Method initBinderMethod, WebDataBinder binder, NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/* 422 */     Class<?>[] initBinderParams = initBinderMethod.getParameterTypes();
/* 423 */     Object[] initBinderArgs = new Object[initBinderParams.length];
/*     */     
/* 425 */     for (int i = 0; i < initBinderArgs.length; i++) {
/* 426 */       MethodParameter methodParam = new SynthesizingMethodParameter(initBinderMethod, i);
/* 427 */       methodParam.initParameterNameDiscovery(this.parameterNameDiscoverer);
/* 428 */       GenericTypeResolver.resolveParameterType(methodParam, handler.getClass());
/* 429 */       String paramName = null;
/* 430 */       boolean paramRequired = false;
/* 431 */       String paramDefaultValue = null;
/* 432 */       String pathVarName = null;
/* 433 */       Annotation[] paramAnns = methodParam.getParameterAnnotations();
/*     */       
/* 435 */       for (Annotation paramAnn : paramAnns) {
/* 436 */         if (RequestParam.class.isInstance(paramAnn)) {
/* 437 */           RequestParam requestParam = (RequestParam)paramAnn;
/* 438 */           paramName = requestParam.name();
/* 439 */           paramRequired = requestParam.required();
/* 440 */           paramDefaultValue = parseDefaultValueAttribute(requestParam.defaultValue());
/* 441 */           break;
/*     */         }
/* 443 */         if (ModelAttribute.class.isInstance(paramAnn)) {
/* 444 */           throw new IllegalStateException("@ModelAttribute is not supported on @InitBinder methods: " + initBinderMethod);
/*     */         }
/*     */         
/* 447 */         if (PathVariable.class.isInstance(paramAnn)) {
/* 448 */           PathVariable pathVar = (PathVariable)paramAnn;
/* 449 */           pathVarName = pathVar.value();
/*     */         }
/*     */       }
/*     */       
/* 453 */       if ((paramName == null) && (pathVarName == null)) {
/* 454 */         Object argValue = resolveCommonArgument(methodParam, webRequest);
/* 455 */         if (argValue != WebArgumentResolver.UNRESOLVED) {
/* 456 */           initBinderArgs[i] = argValue;
/*     */         }
/*     */         else {
/* 459 */           Class<?> paramType = initBinderParams[i];
/* 460 */           if (paramType.isInstance(binder)) {
/* 461 */             initBinderArgs[i] = binder;
/*     */           }
/* 463 */           else if (BeanUtils.isSimpleProperty(paramType)) {
/* 464 */             paramName = "";
/*     */           }
/*     */           else {
/* 467 */             throw new IllegalStateException("Unsupported argument [" + paramType.getName() + "] for @InitBinder method: " + initBinderMethod);
/*     */           }
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 473 */       if (paramName != null)
/*     */       {
/* 475 */         initBinderArgs[i] = resolveRequestParam(paramName, paramRequired, paramDefaultValue, methodParam, webRequest, null);
/*     */       }
/* 477 */       else if (pathVarName != null) {
/* 478 */         initBinderArgs[i] = resolvePathVariable(pathVarName, methodParam, webRequest, null);
/*     */       }
/*     */     }
/*     */     
/* 482 */     return initBinderArgs;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Object resolveRequestParam(String paramName, boolean required, String defaultValue, MethodParameter methodParam, NativeWebRequest webRequest, Object handlerForInitBinderCall)
/*     */     throws Exception
/*     */   {
/* 490 */     Class<?> paramType = methodParam.getParameterType();
/* 491 */     if ((Map.class.isAssignableFrom(paramType)) && (paramName.length() == 0)) {
/* 492 */       return resolveRequestParamMap(paramType, webRequest);
/*     */     }
/* 494 */     if (paramName.length() == 0) {
/* 495 */       paramName = getRequiredParameterName(methodParam);
/*     */     }
/* 497 */     Object paramValue = null;
/* 498 */     MultipartRequest multipartRequest = (MultipartRequest)webRequest.getNativeRequest(MultipartRequest.class);
/* 499 */     if (multipartRequest != null) {
/* 500 */       List<MultipartFile> files = multipartRequest.getFiles(paramName);
/* 501 */       if (!files.isEmpty()) {
/* 502 */         paramValue = files.size() == 1 ? files.get(0) : files;
/*     */       }
/*     */     }
/* 505 */     if (paramValue == null) {
/* 506 */       String[] paramValues = webRequest.getParameterValues(paramName);
/* 507 */       if (paramValues != null) {
/* 508 */         paramValue = paramValues.length == 1 ? paramValues[0] : paramValues;
/*     */       }
/*     */     }
/* 511 */     if (paramValue == null) {
/* 512 */       if (defaultValue != null) {
/* 513 */         paramValue = resolveDefaultValue(defaultValue);
/*     */       }
/* 515 */       else if (required) {
/* 516 */         raiseMissingParameterException(paramName, paramType);
/*     */       }
/* 518 */       paramValue = checkValue(paramName, paramValue, paramType);
/*     */     }
/* 520 */     WebDataBinder binder = createBinder(webRequest, null, paramName);
/* 521 */     initBinder(handlerForInitBinderCall, paramName, binder, webRequest);
/* 522 */     return binder.convertIfNecessary(paramValue, paramType, methodParam);
/*     */   }
/*     */   
/*     */   private Map<String, ?> resolveRequestParamMap(Class<? extends Map<?, ?>> mapType, NativeWebRequest webRequest) {
/* 526 */     Map<String, String[]> parameterMap = webRequest.getParameterMap();
/* 527 */     if (MultiValueMap.class.isAssignableFrom(mapType)) {
/* 528 */       MultiValueMap<String, String> result = new LinkedMultiValueMap(parameterMap.size());
/* 529 */       for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
/* 530 */         for (String value : (String[])entry.getValue()) {
/* 531 */           result.add(entry.getKey(), value);
/*     */         }
/*     */       }
/* 534 */       return result;
/*     */     }
/*     */     
/* 537 */     Map<String, String> result = new LinkedHashMap(parameterMap.size());
/* 538 */     for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
/* 539 */       if (((String[])entry.getValue()).length > 0) {
/* 540 */         result.put(entry.getKey(), ((String[])entry.getValue())[0]);
/*     */       }
/*     */     }
/* 543 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private Object resolveRequestHeader(String headerName, boolean required, String defaultValue, MethodParameter methodParam, NativeWebRequest webRequest, Object handlerForInitBinderCall)
/*     */     throws Exception
/*     */   {
/* 552 */     Class<?> paramType = methodParam.getParameterType();
/* 553 */     if (Map.class.isAssignableFrom(paramType)) {
/* 554 */       return resolveRequestHeaderMap(paramType, webRequest);
/*     */     }
/* 556 */     if (headerName.length() == 0) {
/* 557 */       headerName = getRequiredParameterName(methodParam);
/*     */     }
/* 559 */     Object headerValue = null;
/* 560 */     String[] headerValues = webRequest.getHeaderValues(headerName);
/* 561 */     if (headerValues != null) {
/* 562 */       headerValue = headerValues.length == 1 ? headerValues[0] : headerValues;
/*     */     }
/* 564 */     if (headerValue == null) {
/* 565 */       if (defaultValue != null) {
/* 566 */         headerValue = resolveDefaultValue(defaultValue);
/*     */       }
/* 568 */       else if (required) {
/* 569 */         raiseMissingHeaderException(headerName, paramType);
/*     */       }
/* 571 */       headerValue = checkValue(headerName, headerValue, paramType);
/*     */     }
/* 573 */     WebDataBinder binder = createBinder(webRequest, null, headerName);
/* 574 */     initBinder(handlerForInitBinderCall, headerName, binder, webRequest);
/* 575 */     return binder.convertIfNecessary(headerValue, paramType, methodParam);
/*     */   }
/*     */   
/*     */   private Map<String, ?> resolveRequestHeaderMap(Class<? extends Map<?, ?>> mapType, NativeWebRequest webRequest) {
/* 579 */     if (MultiValueMap.class.isAssignableFrom(mapType)) { MultiValueMap<String, String> result;
/*     */       MultiValueMap<String, String> result;
/* 581 */       if (HttpHeaders.class.isAssignableFrom(mapType)) {
/* 582 */         result = new HttpHeaders();
/*     */       }
/*     */       else {
/* 585 */         result = new LinkedMultiValueMap();
/*     */       }
/* 587 */       for (Iterator<String> iterator = webRequest.getHeaderNames(); iterator.hasNext();) {
/* 588 */         String headerName = (String)iterator.next();
/* 589 */         for (String headerValue : webRequest.getHeaderValues(headerName)) {
/* 590 */           result.add(headerName, headerValue);
/*     */         }
/*     */       }
/* 593 */       return result;
/*     */     }
/*     */     
/* 596 */     Map<String, String> result = new LinkedHashMap();
/* 597 */     for (Iterator<String> iterator = webRequest.getHeaderNames(); iterator.hasNext();) {
/* 598 */       String headerName = (String)iterator.next();
/* 599 */       String headerValue = webRequest.getHeader(headerName);
/* 600 */       result.put(headerName, headerValue);
/*     */     }
/* 602 */     return result;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object resolveRequestBody(MethodParameter methodParam, NativeWebRequest webRequest, Object handler)
/*     */     throws Exception
/*     */   {
/* 612 */     return readWithMessageConverters(methodParam, createHttpInputMessage(webRequest), methodParam.getParameterType());
/*     */   }
/*     */   
/*     */   private HttpEntity<?> resolveHttpEntityRequest(MethodParameter methodParam, NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/* 618 */     HttpInputMessage inputMessage = createHttpInputMessage(webRequest);
/* 619 */     Class<?> paramType = getHttpEntityType(methodParam);
/* 620 */     Object body = readWithMessageConverters(methodParam, inputMessage, paramType);
/* 621 */     return new HttpEntity(body, inputMessage.getHeaders());
/*     */   }
/*     */   
/*     */ 
/*     */   private Object readWithMessageConverters(MethodParameter methodParam, HttpInputMessage inputMessage, Class<?> paramType)
/*     */     throws Exception
/*     */   {
/* 628 */     MediaType contentType = inputMessage.getHeaders().getContentType();
/* 629 */     String paramName; if (contentType == null) {
/* 630 */       StringBuilder builder = new StringBuilder(ClassUtils.getShortName(methodParam.getParameterType()));
/* 631 */       paramName = methodParam.getParameterName();
/* 632 */       if (paramName != null) {
/* 633 */         builder.append(' ');
/* 634 */         builder.append(paramName);
/*     */       }
/*     */       
/* 637 */       throw new HttpMediaTypeNotSupportedException("Cannot extract parameter (" + builder.toString() + "): no Content-Type found");
/*     */     }
/*     */     
/* 640 */     List<MediaType> allSupportedMediaTypes = new ArrayList();
/* 641 */     if (this.messageConverters != null) {
/* 642 */       for (HttpMessageConverter<?> messageConverter : this.messageConverters) {
/* 643 */         allSupportedMediaTypes.addAll(messageConverter.getSupportedMediaTypes());
/* 644 */         if (messageConverter.canRead(paramType, contentType)) {
/* 645 */           if (logger.isDebugEnabled()) {
/* 646 */             logger.debug("Reading [" + paramType.getName() + "] as \"" + contentType + "\" using [" + messageConverter + "]");
/*     */           }
/*     */           
/* 649 */           return messageConverter.read(paramType, inputMessage);
/*     */         }
/*     */       }
/*     */     }
/* 653 */     throw new HttpMediaTypeNotSupportedException(contentType, allSupportedMediaTypes);
/*     */   }
/*     */   
/*     */   private Class<?> getHttpEntityType(MethodParameter methodParam) {
/* 657 */     Assert.isAssignable(HttpEntity.class, methodParam.getParameterType());
/* 658 */     ParameterizedType type = (ParameterizedType)methodParam.getGenericParameterType();
/* 659 */     if (type.getActualTypeArguments().length == 1) {
/* 660 */       Type typeArgument = type.getActualTypeArguments()[0];
/* 661 */       if ((typeArgument instanceof Class)) {
/* 662 */         return (Class)typeArgument;
/*     */       }
/* 664 */       if ((typeArgument instanceof GenericArrayType)) {
/* 665 */         Type componentType = ((GenericArrayType)typeArgument).getGenericComponentType();
/* 666 */         if ((componentType instanceof Class))
/*     */         {
/* 668 */           Object array = Array.newInstance((Class)componentType, 0);
/* 669 */           return array.getClass();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 674 */     throw new IllegalArgumentException("HttpEntity parameter (" + methodParam.getParameterName() + ") is not parameterized");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private Object resolveCookieValue(String cookieName, boolean required, String defaultValue, MethodParameter methodParam, NativeWebRequest webRequest, Object handlerForInitBinderCall)
/*     */     throws Exception
/*     */   {
/* 682 */     Class<?> paramType = methodParam.getParameterType();
/* 683 */     if (cookieName.length() == 0) {
/* 684 */       cookieName = getRequiredParameterName(methodParam);
/*     */     }
/* 686 */     Object cookieValue = resolveCookieValue(cookieName, paramType, webRequest);
/* 687 */     if (cookieValue == null) {
/* 688 */       if (defaultValue != null) {
/* 689 */         cookieValue = resolveDefaultValue(defaultValue);
/*     */       }
/* 691 */       else if (required) {
/* 692 */         raiseMissingCookieException(cookieName, paramType);
/*     */       }
/* 694 */       cookieValue = checkValue(cookieName, cookieValue, paramType);
/*     */     }
/* 696 */     WebDataBinder binder = createBinder(webRequest, null, cookieName);
/* 697 */     initBinder(handlerForInitBinderCall, cookieName, binder, webRequest);
/* 698 */     return binder.convertIfNecessary(cookieValue, paramType, methodParam);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected Object resolveCookieValue(String cookieName, Class<?> paramType, NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/* 708 */     throw new UnsupportedOperationException("@CookieValue not supported");
/*     */   }
/*     */   
/*     */   private Object resolvePathVariable(String pathVarName, MethodParameter methodParam, NativeWebRequest webRequest, Object handlerForInitBinderCall)
/*     */     throws Exception
/*     */   {
/* 714 */     Class<?> paramType = methodParam.getParameterType();
/* 715 */     if (pathVarName.length() == 0) {
/* 716 */       pathVarName = getRequiredParameterName(methodParam);
/*     */     }
/* 718 */     String pathVarValue = resolvePathVariable(pathVarName, paramType, webRequest);
/* 719 */     WebDataBinder binder = createBinder(webRequest, null, pathVarName);
/* 720 */     initBinder(handlerForInitBinderCall, pathVarName, binder, webRequest);
/* 721 */     return binder.convertIfNecessary(pathVarValue, paramType, methodParam);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected String resolvePathVariable(String pathVarName, Class<?> paramType, NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/* 731 */     throw new UnsupportedOperationException("@PathVariable not supported");
/*     */   }
/*     */   
/*     */   private String getRequiredParameterName(MethodParameter methodParam) {
/* 735 */     String name = methodParam.getParameterName();
/* 736 */     if (name == null)
/*     */     {
/* 738 */       throw new IllegalStateException("No parameter name specified for argument of type [" + methodParam.getParameterType().getName() + "], and no parameter name information found in class file either.");
/*     */     }
/*     */     
/* 741 */     return name;
/*     */   }
/*     */   
/*     */   private Object checkValue(String name, Object value, Class<?> paramType) {
/* 745 */     if (value == null) {
/* 746 */       if (Boolean.TYPE == paramType) {
/* 747 */         return Boolean.FALSE;
/*     */       }
/* 749 */       if (paramType.isPrimitive()) {
/* 750 */         throw new IllegalStateException("Optional " + paramType + " parameter '" + name + "' is not present but cannot be translated into a null value due to being declared as a primitive type. Consider declaring it as object wrapper for the corresponding primitive type.");
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 755 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */   private WebDataBinder resolveModelAttribute(String attrName, MethodParameter methodParam, ExtendedModelMap implicitModel, NativeWebRequest webRequest, Object handler)
/*     */     throws Exception
/*     */   {
/* 762 */     String name = attrName;
/* 763 */     if ("".equals(name)) {
/* 764 */       name = Conventions.getVariableNameForParameter(methodParam);
/*     */     }
/* 766 */     Class<?> paramType = methodParam.getParameterType();
/*     */     Object bindObject;
/* 768 */     Object bindObject; if (implicitModel.containsKey(name)) {
/* 769 */       bindObject = implicitModel.get(name);
/*     */     }
/* 771 */     else if (this.methodResolver.isSessionAttribute(name, paramType)) {
/* 772 */       Object bindObject = this.sessionAttributeStore.retrieveAttribute(webRequest, name);
/* 773 */       if (bindObject == null) {
/* 774 */         raiseSessionRequiredException("Session attribute '" + name + "' required - not found in session");
/*     */       }
/*     */     }
/*     */     else {
/* 778 */       bindObject = BeanUtils.instantiateClass(paramType);
/*     */     }
/* 780 */     WebDataBinder binder = createBinder(webRequest, bindObject, name);
/* 781 */     initBinder(handler, name, binder, webRequest);
/* 782 */     return binder;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected boolean isBindingCandidate(Object value)
/*     */   {
/* 791 */     return (value != null) && (!value.getClass().isArray()) && (!(value instanceof Collection)) && (!(value instanceof Map)) && 
/* 792 */       (!BeanUtils.isSimpleValueType(value.getClass()));
/*     */   }
/*     */   
/*     */   protected void raiseMissingParameterException(String paramName, Class<?> paramType) throws Exception {
/* 796 */     throw new IllegalStateException("Missing parameter '" + paramName + "' of type [" + paramType.getName() + "]");
/*     */   }
/*     */   
/*     */   protected void raiseMissingHeaderException(String headerName, Class<?> paramType) throws Exception {
/* 800 */     throw new IllegalStateException("Missing header '" + headerName + "' of type [" + paramType.getName() + "]");
/*     */   }
/*     */   
/*     */   protected void raiseMissingCookieException(String cookieName, Class<?> paramType) throws Exception
/*     */   {
/* 805 */     throw new IllegalStateException("Missing cookie value '" + cookieName + "' of type [" + paramType.getName() + "]");
/*     */   }
/*     */   
/*     */   protected void raiseSessionRequiredException(String message) throws Exception {
/* 809 */     throw new IllegalStateException(message);
/*     */   }
/*     */   
/*     */   protected WebDataBinder createBinder(NativeWebRequest webRequest, Object target, String objectName)
/*     */     throws Exception
/*     */   {
/* 815 */     return new WebRequestDataBinder(target, objectName);
/*     */   }
/*     */   
/*     */   private void doBind(WebDataBinder binder, NativeWebRequest webRequest, boolean validate, Object[] validationHints, boolean failOnErrors)
/*     */     throws Exception
/*     */   {
/* 821 */     doBind(binder, webRequest);
/* 822 */     if (validate) {
/* 823 */       binder.validate(validationHints);
/*     */     }
/* 825 */     if ((failOnErrors) && (binder.getBindingResult().hasErrors())) {
/* 826 */       throw new BindException(binder.getBindingResult());
/*     */     }
/*     */   }
/*     */   
/*     */   protected void doBind(WebDataBinder binder, NativeWebRequest webRequest) throws Exception {
/* 831 */     ((WebRequestDataBinder)binder).bind(webRequest);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected HttpInputMessage createHttpInputMessage(NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/* 839 */     throw new UnsupportedOperationException("@RequestBody not supported");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected HttpOutputMessage createHttpOutputMessage(NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/* 847 */     throw new UnsupportedOperationException("@Body not supported");
/*     */   }
/*     */   
/*     */   protected String parseDefaultValueAttribute(String value) {
/* 851 */     return "\n\t\t\n\t\t\n\n\t\t\t\t\n".equals(value) ? null : value;
/*     */   }
/*     */   
/*     */   protected Object resolveDefaultValue(String value) {
/* 855 */     return value;
/*     */   }
/*     */   
/*     */ 
/*     */   protected Object resolveCommonArgument(MethodParameter methodParameter, NativeWebRequest webRequest)
/*     */     throws Exception
/*     */   {
/* 862 */     if (this.customArgumentResolvers != null) {
/* 863 */       for (WebArgumentResolver argumentResolver : this.customArgumentResolvers) {
/* 864 */         Object value = argumentResolver.resolveArgument(methodParameter, webRequest);
/* 865 */         if (value != WebArgumentResolver.UNRESOLVED) {
/* 866 */           return value;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 872 */     Object paramType = methodParameter.getParameterType();
/* 873 */     Object value = resolveStandardArgument((Class)paramType, webRequest);
/* 874 */     if ((value != WebArgumentResolver.UNRESOLVED) && (!ClassUtils.isAssignableValue((Class)paramType, value)))
/*     */     {
/* 876 */       throw new IllegalStateException("Standard argument type [" + ((Class)paramType).getName() + "] resolved to incompatible value of type [" + (value != null ? value.getClass() : null) + "]. Consider declaring the argument type in a less specific fashion.");
/*     */     }
/*     */     
/* 879 */     return value;
/*     */   }
/*     */   
/*     */   protected Object resolveStandardArgument(Class<?> parameterType, NativeWebRequest webRequest) throws Exception {
/* 883 */     if (WebRequest.class.isAssignableFrom(parameterType)) {
/* 884 */       return webRequest;
/*     */     }
/* 886 */     return WebArgumentResolver.UNRESOLVED;
/*     */   }
/*     */   
/*     */ 
/*     */   protected final void addReturnValueAsModelAttribute(Method handlerMethod, Class<?> handlerType, Object returnValue, ExtendedModelMap implicitModel)
/*     */   {
/* 892 */     ModelAttribute attr = (ModelAttribute)AnnotationUtils.findAnnotation(handlerMethod, ModelAttribute.class);
/* 893 */     String attrName = attr != null ? attr.value() : "";
/* 894 */     if ("".equals(attrName)) {
/* 895 */       Class<?> resolvedType = GenericTypeResolver.resolveReturnType(handlerMethod, handlerType);
/* 896 */       attrName = Conventions.getVariableNameForReturnType(handlerMethod, resolvedType, returnValue);
/*     */     }
/* 898 */     implicitModel.addAttribute(attrName, returnValue);
/*     */   }
/*     */ }


/* Location:              C:\Users\ikatwal\Downloads\bullet-zone-server-2.0.jar!\BOOT-INF\lib\spring-web-4.3.14.RELEASE.jar!\org\springframework\web\bind\annotation\support\HandlerMethodInvoker.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */